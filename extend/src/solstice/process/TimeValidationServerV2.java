/*
 * Created on 2005-06-28
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

import org.compiere.model.MRole;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;
import org.compiere.util.Trx;

import solstice.model.P_Assignment;
import solstice.model.P_Assignment_Distribution;
import solstice.model.P_Assignment_Param;
import solstice.model.P_Credits;
//import solstice.model.P_Deduction;
import solstice.model.P_Employee;
import solstice.model.P_Employee_Bonus;
import solstice.model.P_Employee_Deduction;
import solstice.model.P_Employee_LongTermLeave;
import solstice.model.P_Frequency;
import solstice.model.P_Gain;
import solstice.model.P_Gain_Bound;
import solstice.model.P_Gain_Parameter;
import solstice.model.P_LongTermLeave_Deduction;
import solstice.model.P_Payment;
import solstice.model.P_Payment_Deduction;
import solstice.model.P_Payment_Deduction_Excep;
import solstice.model.P_Payment_Gain;
import solstice.model.P_Payment_Gain_Distribution;
import solstice.model.P_Payment_Gain_Distribution_360;
import solstice.model.P_Payment_Taxable_Benefit;
import solstice.model.P_Period;
import solstice.model.P_Post;
import solstice.model.P_Post_Distribution;
import solstice.model.P_Time_Sheet;
import solstice.model.P_Time_Sheet_Error;
//import solstice.model.P_Timecode;
import solstice.model.P_UOM;
import solstice.model.P_Collective_Labour_Agr;
import solstice.model.P_Gain_Account;
import solstice.model.P_Gain_Account_360;
import solstice.model.P_Workplace;

//import solstice.model.P_Employee_Deduction_Excep;
import solstice.model.MPayProcessorV2;
import solstice.model.P_Payment_Group;
import solstice.utils.PgiUtil;

/**
 * @author kevmar01
 *
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class TimeValidationServerV2 implements Runnable {
	private MPayProcessorV2 m_payProcessorModel = null;
	private String m_threadWorkerName = null;
	private int m_workerId = 0;

	public TimeValidationServerV2(MPayProcessorV2 model, String name, int ID) {
		this.m_payProcessorModel = model;
		this.m_threadWorkerName = name;
		this.m_workerId = ID;
	}

	@Override
	public void run() {
		if (m_payProcessorModel == null || m_payProcessorModel.m_stack == null)
			return;

		int workerOrgId = 0;
		int workerActivityId = 0;
		int workerEmployeeId = 0;
		int workerPaymentGroupId = 0;
		try {
			workerPaymentGroupId = Integer.parseInt(PgiUtil.getSolsticeParameter(Env.getCtx(), "CalculPayrollPayment_Group_ID"));
		} catch (Exception e) {}

		P_Payment_Group pg = P_Payment_Group.get(Env.getCtx(), workerPaymentGroupId, null);
		Properties workerCtx = Env.getCtx();
		if (pg != null) {
			Env.setContext(workerCtx, "#AD_Client_ID", pg.getAD_Client_ID());
			Env.setContext(workerCtx, "#AD_Org_ID", pg.getAD_Org_ID());
			Env.setContext(workerCtx, "#AD_Role_ID", 1000027);
			workerOrgId = pg.getAD_Org_ID();
		}

		P_Period openPeriod = P_Period.getOpenPeriod(Env.getCtx(), null);
		int workerPeriodId = (openPeriod != null) ? openPeriod.getP_Period_ID() : 0;
		int workerFreqId = (openPeriod != null) ? openPeriod.getP_Frequency_ID() : 0;
		int workerTimeSheetId = 0;
		boolean workerIsRework = true;

		while (true) {
			String s_Emp_ID = null;
			synchronized (m_payProcessorModel.m_stack) {
				if (!m_payProcessorModel.m_stack.empty()) {
					s_Emp_ID = m_payProcessorModel.m_stack.pop();
				}
			}
			if (s_Emp_ID == null)
				break;

			try {
				int teamId = 0;
				int colAgrId = 0;
				workerEmployeeId = Integer.parseInt(s_Emp_ID);

				TimeValidationServerV2 tmp = new TimeValidationServerV2(
					Env.getCtx(), m_payProcessorModel.getAD_Client_ID(), workerOrgId, workerActivityId,
					workerEmployeeId, workerTimeSheetId, workerPaymentGroupId, workerFreqId,
					workerPeriodId, teamId, colAgrId, workerIsRework, null);
				tmp.setMultiThread(false);
				tmp.Validation();
			} catch (Exception e) {
				log.log(Level.SEVERE, "TimeValidationServerV2 worker exception", e);
			}
		}
	}
	private int AD_Org_ID = 0;
	private int Org_ID = 0;
	private int Activity_ID = 0;

	private int Employee_ID = 0;
	private int Time_Sheet_ID = 0;
	private int Frequency_ID = 0;
	private int Period_ID = 0;
	private int Payment_Group_ID = 0;
	private int P_Distribution_Booklet_ID = 0;
	private int P_Collective_Labour_Agr_ID = 0;
	private boolean IsRework = false;

	private int m_inserted = 0;

	private P_Period Period;
	private P_Employee Employee;
	// private P_Assignment Assignment;
	private P_Time_Sheet TimeSheet;
	private P_Payment Payment;

	private static CLogger log = CLogger.getCLogger(TimeValidation.class);

	/** Big Decimal 0 */
	static final public java.math.BigDecimal ZERO = new java.math.BigDecimal(0.0);

	String m_trxName;

	// Multi-thread control fields (JDK 8 compatible)
	private Properties m_ctx;
	private int m_Client_ID = 0;
	private boolean m_isWorker = false;
	private int m_customThreadCount = 0;
	private boolean m_multiThread = true;

	public void setThreadCount(int threadCount) {
		this.m_customThreadCount = threadCount;
	}

	public int getThreadCount() {
		return this.m_customThreadCount;
	}

	public void setMultiThread(boolean multiThread) {
		this.m_multiThread = multiThread;
	}

	public boolean isMultiThread() {
		return this.m_multiThread;
	}

	public void setWorker(boolean isWorker) {
		this.m_isWorker = isWorker;
	}

	/**
	 * Structure interne representant une feuille de temps e traiter.
	 * Compatible JDK 8.
	 */
	private static class SheetRecord {
		final int timeSheetId;
		final int employeeId;
		final int periodId;

		SheetRecord(int timeSheetId, int employeeId, int periodId) {
			this.timeSheetId = timeSheetId;
			this.employeeId = employeeId;
			this.periodId = periodId;
		}
	}

	public TimeValidationServerV2(Properties ctx, int _Client_ID, int _Org_ID, int _Activity_ID, int _Employee_ID,
			int _Time_Sheet_ID, int _Payment_Group_ID,
			int _Frequency_ID, int _Period_ID, int _P_Distribution_Booklet_ID, int _Collective_Labour_Agr_ID,
			boolean _IsRework, String trxName) {
		setTrxName(trxName);
		// Env.setCtx(ctx);
		m_ctx = ctx;
		m_Client_ID = _Client_ID;
		Org_ID = _Client_ID;
		AD_Org_ID = _Org_ID;
		Activity_ID = _Activity_ID;

		Employee_ID = _Employee_ID;
		Time_Sheet_ID = _Time_Sheet_ID;
		Payment_Group_ID = _Payment_Group_ID;
		Frequency_ID = _Frequency_ID;
		Period_ID = _Period_ID;
		IsRework = _IsRework;
		P_Distribution_Booklet_ID = _P_Distribution_Booklet_ID;
		P_Collective_Labour_Agr_ID = _Collective_Labour_Agr_ID;

		P_Payment_Group Payment_Group = P_Payment_Group.get(Env.getCtx(), Payment_Group_ID, null);
		Env.setContext(ctx, "#AD_Client_ID", Payment_Group.getAD_Client_ID());
		// Env.setContext( ctx, "#AD_Org_ID", Payment_Group.getAD_Org_ID());
		Org_ID = Payment_Group.getAD_Org_ID();

		Env.setContext(Env.getCtx(), "#AD_Client_ID", Payment_Group.getAD_Client_ID());
		// Env.setContext( Env.getCtx(), "#AD_Org_ID", Payment_Group.getAD_Org_ID());

		Env.setContext(ctx, "#AD_Role_ID", 1000027);
		Env.setContext(Env.getCtx(), "#AD_Role_ID", 1000027);

	} // prepare

	// appel de la feuille de temps - Ajustement .
	public TimeValidationServerV2(P_Time_Sheet _TimeSheet, String trxName) {

		setTrxName(trxName);
		TimeSheet = _TimeSheet;

		Org_ID = TimeSheet.getAD_Org_ID();
		Employee_ID = TimeSheet.getP_Employee_ID();
		Time_Sheet_ID = TimeSheet.getP_Time_Sheet_ID();
		Payment_Group_ID = TimeSheet.getP_Payment_Group_ID();
		Frequency_ID = TimeSheet.getP_Frequency_ID();
		Period_ID = TimeSheet.getP_Period_ID();
		IsRework = true;
		P_Distribution_Booklet_ID = TimeSheet.getP_Distribution_Booklet_ID();
		P_Collective_Labour_Agr_ID = TimeSheet.getP_Collective_Labour_Agr_ID();
		Payment = new P_Payment(Env.getCtx(), TimeSheet.getP_Payment_ID(), getTrxName());
		Employee = P_Employee.get(Env.getCtx(), Payment.getP_Employee_ID(), getTrxName());
		Period = P_Period.get(Env.getCtx(), Payment.getP_Period_ID(), getTrxName());

	} // prepare

	/**
	 * Read each time sheet
	 * 
	 * @return
	 * @throws Exception
	 */

	private Calcul_Credits calCredits;
	// private CalculNet cal;

	public String Validation() throws Exception {

		Env.setContext(Env.getCtx(), "#AD_Client_ID", 1000004);
		// Env.setContext( Env.getCtx(), "#AD_Org_ID", AD_Org_ID);

		// log.log (Level.INFO,"Validation - start");
		boolean isError = false;

		if (Period_ID == 0)
			log.log(Level.WARNING, "doIt - Period not found");

		Period = P_Period.get(Env.getCtx(), Period_ID, getTrxName());

		// + 2010.05.04
		// stop this procedure if the period is closed
		if (Period != null && (Period.getPeriodStatus().equals(P_Period.PERIODSTATUS_Closed)
				|| Period.getPeriodStatus().equals(P_Period.PERIODSTATUS_PermanentlyClosed)))
			return "@inserted@ " + m_inserted;
		// - 2010.05.04

		String sql = null;
		sql = "Select P_Time_Sheet_ID, P_Employee_ID, P_Period_ID FROM P_Time_Sheet "
				+ " WHERE P_Time_Sheet.IsActive='Y' ";

		if (IsRework)
			sql += "AND (P_Time_Sheet.TimesheetStatus IN ( 'I','F','V','C') AND P_Time_Sheet.SHEETTYPE != 'Adjustement') ";
		else
			sql += "AND P_Time_Sheet.TimesheetStatus IN ( 'I','F' )";

		if (AD_Org_ID > 0)
			sql += "   AND P_Time_Sheet.AD_Org_ID = " + AD_Org_ID;

		if (Activity_ID > 0)
			sql += "   AND P_Time_Sheet.C_Activity_ID = " + Activity_ID;

		if (Employee_ID > 0)
			sql += " AND P_Employee_ID=" + Employee_ID;
		if (Period_ID > 0)
			sql += " AND P_Period_ID=" + Period_ID;
		if (Time_Sheet_ID > 0)
			sql += " AND P_Time_Sheet_ID=" + Time_Sheet_ID;
		if (Payment_Group_ID > 0)
			sql += " AND P_Payment_Group_ID=" + Payment_Group_ID;

		if (P_Distribution_Booklet_ID > 0)
			sql += " AND P_Distribution_Booklet_ID=" + P_Distribution_Booklet_ID;

		if (P_Collective_Labour_Agr_ID > 0)
			sql += " AND P_Collective_Labour_Agr_ID=" + P_Collective_Labour_Agr_ID;

		//
		sql += " ORDER BY case when SheetType = 'Regular' THEN 1 Else 2 END, value";

		sql = MRole.getDefault(Env.getCtx(), false).addAccessSQL(sql, "P_Time_Sheet", true, false); // fully qualidfied
																									// - RO

		// CalculNet cal = new CalculNet( Env.getCtx(), Period.getPayDate() ,
		// getTrxName());

		List<SheetRecord> records = new ArrayList<SheetRecord>();
		Map<Integer, List<SheetRecord>> recordsByEmployee = new LinkedHashMap<Integer, List<SheetRecord>>();

		PreparedStatement pstmt = null;
		try {
			pstmt = DB.prepareStatement(sql, getTrxName());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int tsId = rs.getInt(1);
				int empId = rs.getInt(2);
				int perId = rs.getInt(3);
				SheetRecord rec = new SheetRecord(tsId, empId, perId);
				records.add(rec);
				List<SheetRecord> empList = recordsByEmployee.get(Integer.valueOf(empId));
				if (empList == null) {
					empList = new ArrayList<SheetRecord>();
					recordsByEmployee.put(Integer.valueOf(empId), empList);
				}
				empList.add(rec);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		} catch (Exception e) {
			P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error,
					" Internal error - Exception  " + e.toString(), getTrxName());
			log.log(Level.SEVERE, "Validation - " + sql, e);
			throw e;
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception ignored) {
				}
			}
		}

		if (records.isEmpty()) {
			return "Enregistrement valides : " + m_inserted;
		}

		// Determiner le nombre de threads (parametre Solstice CalculPayrollThread ou
		// nombre de ceurs CPU)
		int nbrThread = 1;
		if (m_multiThread && !m_isWorker) {
			if (m_customThreadCount > 0) {
				nbrThread = m_customThreadCount;
			} else {
				try {
					String pThread = PgiUtil.getSolsticeParameter(Env.getCtx(), "CalculPayrollThread");
					if (pThread != null && !pThread.trim().isEmpty()) {
						nbrThread = Integer.parseInt(pThread.trim());
					}
				} catch (Exception e) {
					log.log(Level.WARNING,
							"TimeValidation - error reading CalculPayrollThread parameter: " + e.getMessage());
				}
				if (nbrThread <= 0) {
					nbrThread = Runtime.getRuntime().availableProcessors();
				}
			}
		}

		// Si mode mono-thread force, un seul employe cible ou worker enfant : executer
		// sequentiellement
		if (m_isWorker || !m_multiThread || recordsByEmployee.size() <= 1 || nbrThread <= 1) {
			m_inserted = processRecords(records);
			return "Enregistrement valides : " + m_inserted;
		}

		// Execution multi-thread par employe (partitionnement securise)
		if (nbrThread > recordsByEmployee.size()) {
			nbrThread = recordsByEmployee.size();
		}

		log.log(Level.INFO, "TimeValidation - Starting multi-thread validation with " + nbrThread + " threads for "
				+ recordsByEmployee.size() + " employees (" + records.size() + " timesheets)");

		final Properties workerCtx = (m_ctx != null) ? m_ctx : Env.getCtx();
		final int workerClientId = (m_Client_ID > 0) ? m_Client_ID : Org_ID;
		final int workerOrgId = AD_Org_ID;
		final int workerActivityId = Activity_ID;
		final int workerPaymentGroupId = Payment_Group_ID;
		final int workerFrequencyId = Frequency_ID;
		final int workerPeriodId = Period_ID;
		final int workerBookletId = P_Distribution_Booklet_ID;
		final int workerAgrId = P_Collective_Labour_Agr_ID;
		final boolean workerIsRework = IsRework;

				ExecutorService executor;
		try {
			// Java 21+ / Java 27 : Virtual Threads (haute performance, ultra-leger, non-bloquant)
			java.lang.reflect.Method m = Executors.class.getMethod("newVirtualThreadPerTaskExecutor");
			executor = (ExecutorService) m.invoke(null);
			log.log(Level.INFO, "TimeValidation - Using Java 27 Virtual Threads Executor");
		} catch (Throwable t) {
			executor = Executors.newFixedThreadPool(nbrThread, new ThreadFactory() {
				private final AtomicInteger threadNum = new AtomicInteger(1);

				public Thread newThread(Runnable r) {
					Thread th = new Thread(r, "TimeValidation-Worker-" + threadNum.getAndIncrement());
					th.setDaemon(true);
					return th;
				}
			});
			log.log(Level.INFO, "TimeValidation - Using Fixed Thread Pool (" + nbrThread + " threads)");
		}

		List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
		for (Map.Entry<Integer, List<SheetRecord>> entry : recordsByEmployee.entrySet()) {
			final int targetEmpId = entry.getKey().intValue();
			final List<SheetRecord> empRecords = entry.getValue();

			futures.add(executor.submit(new Callable<Integer>() {
				public Integer call() throws Exception {
					TimeValidationServerV2 worker = new TimeValidationServerV2(
							workerCtx,
							workerClientId,
							workerOrgId,
							workerActivityId,
							targetEmpId,
							0,
							workerPaymentGroupId,
							workerFrequencyId,
							workerPeriodId,
							workerBookletId,
							workerAgrId,
							workerIsRework,
							null);
					worker.setWorker(true);
					return Integer.valueOf(worker.processRecords(empRecords));
				}
			}));
		}

		executor.shutdown();
		int totalInserted = 0;
		for (Future<Integer> f : futures) {
			try {
				Integer count = f.get();
				if (count != null) {
					totalInserted += count.intValue();
				}
			} catch (Exception ex) {
				log.log(Level.SEVERE, "TimeValidation - worker task failed", ex);
			}
		}

		m_inserted = totalInserted;
		log.log(Level.INFO, "TimeValidation - Multi-thread validation finished. Total validated: " + m_inserted);
		return "Enregistrement valides : " + m_inserted;
	}

	/**
	 * Traitement d'une liste de feuilles de temps pour un worker ou en mode
	 * mono-thread.
	 * Conserve integralement les regles d'affaires de calcul de paie existantes.
	 * Compatible JDK 8.
	 *
	 * @param records Liste des feuilles de temps ordonnees e valider
	 * @return Nombre de feuilles de temps validees avec succes
	 * @throws Exception
	 */
	public int processRecords(List<SheetRecord> records) throws Exception {
		int localInserted = 0;
		if (records == null || records.isEmpty())
			return localInserted;

		if (Period == null && Period_ID > 0)
			Period = P_Period.get(Env.getCtx(), Period_ID, getTrxName());

		for (SheetRecord rec : records) {
			Trx trx = null;
			try {
				String trxName = Trx.createTrxName("TimeVal_TS_" + rec.timeSheetId);
				trx = Trx.get(trxName, true);
				setTrxName(trxName);

				CalculNet cal = new CalculNet(Env.getCtx(), (Period != null ? Period.getPayDate() : null), trxName);
				cal.resetTrxName(trxName);

				Employee = P_Employee.get(Env.getCtx(), rec.employeeId, trxName);
				Period = P_Period.get(Env.getCtx(), rec.periodId, trxName);

				TimeSheet = new P_Time_Sheet(Env.getCtx(), rec.timeSheetId, getTrxName());

				//
				// Cette condition est utiliser pour s'assure que la feuille de temps
				// n'a pas ete detruit durant l'execution du traitement.
				//
				if (TimeSheet != null && TimeSheet.getP_Time_Sheet_ID() != 0) {
					ArrayList<Object> archives = new ArrayList<Object>();
					log.log(Level.INFO,
							"Validation - Employee " + Employee.toString() + " " + (m_inserted + localInserted));

					archiveModification(TimeSheet, archives);
					undo(); // TimeSheet

					// 2009.04.28 - Regular timesheet need existe and to be calculated for calculate
					// a complementary
					if (!validate_RegularSheetIsCaculated()) {
						// break ;
					} else {
						// 2009.01.23 Validation que la coupe negative pour les vacances et la maladie a
						// bien ete effectuer
						valide_negative_cut();

						if (TimeSheet.getP_Payment_ID() != 0) {
							Payment = new P_Payment(Env.getCtx(), TimeSheet.getP_Payment_ID(), getTrxName());
						} else {
							Payment = new P_Payment(Env.getCtx(), -1, getTrxName());
						}

						Payment.setValue(Payment.getP_Payment_ID() + "");
						Payment.setP_Employee_ID(TimeSheet.getP_Employee_ID());

						Payment.setAD_Org_ID(TimeSheet.getAD_Org_ID());
						Payment.setP_Department_ID(TimeSheet.getP_Department_ID());
						Payment.setC_Activity_ID(TimeSheet.getC_Activity_ID());

						Payment.setIsActive(true);
						Payment.setP_Period_ID(Period.getP_Period_ID());
						Payment.setP_Year_ID(Period.getP_Year_ID());
						Payment.setP_Frequency_ID(Period.getP_Frequency_ID());

						if (TimeSheet.getPayDate() == null) {
							TimeSheet.setPayDate(Period.getPayDate());
						}

						Payment.setPayDate(TimeSheet.getPayDate());

						Payment.setPaymentType(TimeSheet.getPaymentType());
						Payment.setP_Employer_ID(TimeSheet.getP_Employer_ID());
						Payment.setP_Workplace_ID(TimeSheet.getP_Workplace_ID());

						Payment.setP_Payment_Group_ID(TimeSheet.getP_Payment_Group_ID());

						Payment.setPaymentTypeDoc(TimeSheet.getSheetType());
						Payment.setAnnualSalary(Employee.getAnnualSalary(Period.getEndDate()));
						// + 2011.11.07 Si l'employe n'a pas d'affectation principal alors on genere une
						// erreur.
						if (Payment.getAnnualSalary() == null) {
							P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error,
									"Le systeme n'a pas trouve de salaire annuel de base pour l'employe, verifier si l'employe a une d'affectation principal",
									getTrxName());
						}
						// - 2011.11.07

						Payment.setP_Job_Title_ID(TimeSheet.getP_Job_Title_ID());
						Payment.setP_Job_Type_ID(TimeSheet.getP_Job_Type_ID());
						Payment.setP_Occupation_Group_ID(TimeSheet.getP_Occupation_Group_ID());
						Payment.setP_Distribution_Booklet_ID(TimeSheet.getP_Distribution_Booklet_ID());
						Payment.setP_Distribution_ID(TimeSheet.getP_Distribution_ID());
						Payment.setValue(TimeSheet.getValue());
						Payment.setIsTransfer(false);
						Payment.setIsReconciled(false);
						Payment.setIsSelfFinancedLeave(false);
						Payment.setTaxation_Region_ID(TimeSheet.getTaxation_Region_ID());
						Payment.setP_Workplace_ID(Employee.getP_Workplace_ID());

						Payment.save(m_trxName);

						TimeSheet.setP_Payment_ID(Payment.getP_Payment_ID());
						TimeSheet.save(m_trxName);

						calCredits = new Calcul_Credits(TimeSheet, getTrxName());

						// import de mouvement de banque provenant d'un autre systeme
						calCredits.import_credits_movement();

						// copy detail of the time sheet of the payment
						gen_time_sheet_detail(Payment, getTrxName());
						//
						// Les ajustements sont deje au statut calcule lors de la saisie, mais si l'on
						// saisie des gains
						// il faut quand meme les calculer.
						//
						if (TimeSheet.getSheetType().equals(P_Time_Sheet.SHEETTYPE_Adjustment)) {
							calcul_gain_credits(Payment, getTrxName());
							Payment.createEntryLine360(m_trxName);
						} else {
							// 2009.06.18
							calCredits.CreditsPaymentOthersBank(Payment);

							// Calcul Gain and Credits day by day
							boolean isError = calcul_gain_credits(Payment, getTrxName());

							if (Payment.getGrossEarnings() == null
									|| Payment.getGrossEarnings().compareTo(Env.ZERO) < 0) {
								P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error,
										"Le montant de la paie brute calcule est negatif ou null", getTrxName());
								isError = true;
							}

							if (isError) {
								TimeSheet.setTimeSheetStatus(P_Time_Sheet.TIMESHEETSTATUS_Error);
								TimeSheet.setIsError(true);
							} else {
								TimeSheet.setTimeSheetStatus(P_Time_Sheet.TIMESHEETSTATUS_Validated);
								TimeSheet.setIsError(false);
							}

							TimeSheet.save(m_trxName);

							cal.Calculation(Payment);

							// Calcul provision
							calCredits.calcul_provision();

							appliqueModification(TimeSheet, archives);

							Create_PaymentGainDistribution_360(Payment, 0);
							Payment.createEntryLine360(trxName);

							CheckPaymentEntryLine360(Payment, m_trxName);

							if (Payment.getPaymentType().equals(P_Payment.PAYMENTTYPE_Deposit))
								Payment.UpdatePaymentDistribution(m_trxName);
						}

						boolean IsError = false;
						boolean IsWarning = false;

						IsError = P_Time_Sheet.CheckError(TimeSheet.getP_Time_Sheet_ID(), getTrxName());
						IsWarning = P_Time_Sheet.CheckWarning(TimeSheet.getP_Time_Sheet_ID(), getTrxName());

						if (IsError) // || IsWarning
							TimeSheet.setTimeSheetStatus(P_Time_Sheet.TIMESHEETSTATUS_Error);

						TimeSheet.setIsError(IsError);
						TimeSheet.setIsWarning(IsWarning);
						TimeSheet.save(m_trxName);

						localInserted++;
					}
				}
				if (trx != null) {
					trx.commit();
				}
			} catch (Exception e) {
				if (trx != null) {
					try {
						trx.rollback();
					} catch (Exception exRollback) {
						log.log(Level.WARNING, "Rollback failed", exRollback);
					}
				}
				P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error,
						" Internal error - Exception  " + e.toString(), null);

				if (TimeSheet != null) {
					TimeSheet.setTimeSheetStatus(P_Time_Sheet.TIMESHEETSTATUS_Error);
					TimeSheet.setIsError(true);
					TimeSheet.save();
				}

				log.log(Level.SEVERE, "Validation - timesheet " + rec.timeSheetId, e);
			} finally {
				if (trx != null) {
					try {
						trx.close();
					} catch (Exception exClose) {
						log.log(Level.WARNING, "Close failed", exClose);
					}
				}
				setTrxName(null);
			}
		}
		return localInserted;
	}

	// 2012.02.16 Valide que l'ecriture comptable e ete genere.
	private void CheckPaymentEntryLine(P_Payment Payment, String trxName) {
		if (Payment.getGrossEarnings().compareTo(Env.ZERO) == 0)
			return;

		String sql = "select 1 from p_payment where not exists "
				+ " ( select 1 from p_payment_entryline where p_payment_entryline.p_payment_id = p_payment.P_PAYMENT_ID ) "
				+ " And P_payment.P_Payment_ID = " + Payment.getP_Payment_ID();
		boolean isError = false;
		try {

			PreparedStatement pstmt;

			pstmt = DB.prepareStatement(sql, trxName);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				isError = true;
			}

			rs.close();
			pstmt.close();

		} catch (Exception e) {
			log.log(Level.SEVERE, "CheckError", e);
		}

		if (isError) {
			P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error,
					"Le systeme n'a pas genere d'ecriture comptable ", getTrxName());
		}
	}

	// 2012.02.16 Valide que l'ecriture comptable e ete genere.
	private void CheckPaymentEntryLine360(P_Payment Payment, String trxName) {
		if (Payment.getGrossEarnings().compareTo(Env.ZERO) == 0)
			return;

		String sql = "select 1 from p_payment where not exists "
				+ " ( select 1 from p_payment_entryline_360 p_payment_entryline where p_payment_entryline.p_payment_id = p_payment.P_PAYMENT_ID ) "
				+ " And P_payment.P_Payment_ID = " + Payment.getP_Payment_ID();
		boolean isError = false;
		try {

			PreparedStatement pstmt;

			pstmt = DB.prepareStatement(sql, trxName);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				isError = true;
			}

			rs.close();
			pstmt.close();

		} catch (Exception e) {
			log.log(Level.SEVERE, "CheckError", e);
		}

		if (isError) {
			P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error,
					"Le systeme n'a pas genere d'ecriture comptable ", getTrxName());
		}
	}

	//
	// need exists a regular timesheet with status Calculated or more.
	//
	private boolean validate_RegularSheetIsCaculated() throws Exception {
		String sql;
		boolean found = true;

		if (TimeSheet.getSheetType().equals(P_Time_Sheet.SHEETTYPE_Complementary))
		// || TimeSheet.getSheetType().equals( P_Time_Sheet.SHEETTYPE_SeparationPay ) )
		{
			sql = "Select 1 From P_Time_Sheet where P_Employee_ID = " + TimeSheet.getP_Employee_ID()
					+ " and P_Period_ID = " + TimeSheet.getP_Period_ID()
					+ " and SheetType ='" + P_Time_Sheet.SHEETTYPE_Regular + "'"
					+ " and TimeSheetStatus in ( '" + P_Time_Sheet.TIMESHEETSTATUS_Calculated + "', '"
					+ P_Time_Sheet.TIMESHEETSTATUS_Issued + "', '" + P_Time_Sheet.TIMESHEETSTATUS_Transferred + "')";

			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement(sql, getTrxName());
			ResultSet rs = pstmt.executeQuery();

			if (!rs.next()) {
				P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error,
						" La feuille de temps reguliere n'est pas valide ou cree.  ", getTrxName());
				TimeSheet.setTimeSheetStatus(P_Time_Sheet.TIMESHEETSTATUS_Error);
				TimeSheet.save();
				found = false;
			}

			rs.close();
			pstmt.close();
			pstmt = null;
		}

		if (TimeSheet.getSheetType().equals(P_Time_Sheet.SHEETTYPE_Regular)) {
			sql = "Select 1 From P_Time_Sheet where P_Employee_ID = " + TimeSheet.getP_Employee_ID()
					+ " and P_Period_ID = " + TimeSheet.getP_Period_ID()
					+ " and SheetType in ('" + P_Time_Sheet.SHEETTYPE_Complementary + "', '"
					+ P_Time_Sheet.SHEETTYPE_SeparationPay + "' )"
					+ " and TimeSheetStatus in ( '" + P_Time_Sheet.TIMESHEETSTATUS_Calculated + "', '"
					+ P_Time_Sheet.TIMESHEETSTATUS_Issued + "', '" + P_Time_Sheet.TIMESHEETSTATUS_Transferred + "', '"
					+ P_Time_Sheet.TIMESHEETSTATUS_Error + "')";

			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement(sql, getTrxName());
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error,
						" La feuille de temps complementaire doit etre e l'etat initial.  ", getTrxName());
				TimeSheet.setTimeSheetStatus(P_Time_Sheet.TIMESHEETSTATUS_Error);
				TimeSheet.save();
				found = false;
			}

			rs.close();
			pstmt.close();
			pstmt = null;

		}

		return found;
	}

	public void gen_time_sheet_detail(P_Payment Payment, String trxName) throws Exception {

		String sql = null;
		sql = "Select P_Time_Sheet_Detail_ID, P_Gain_ID, P_Assignment_ID, DayQty, P_Period_ID, Day, C_Activity_ID, WeekIndex, SalaryPercentage, P_Employee_LongTermLeave_ID, OriginTime, StartDate, EndDate, Hourly_Rate, P_Schedule_ID "
				+ " From P_Time_Sheet_Detail "
				+ " WHERE P_Time_Sheet_ID = " + TimeSheet.getP_Time_Sheet_ID()
				+ " Order By Day ";

		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement(sql, trxName);
		ResultSet rs = pstmt.executeQuery();
		int line = 0;

		P_Assignment Assignment;
		P_Assignment_Param AssignmentParam;
		P_Gain_Parameter GainParameter;
		P_Collective_Labour_Agr CollectiveLabourArg;
		P_Payment_Gain PaymentGain;
		P_Gain Gain;
		P_Payment_Group PaymentGroup = P_Payment_Group.get(Env.getCtx(), Payment.getP_Payment_Group_ID(), m_trxName);

		while (rs.next()) {
			line++;
			Gain = P_Gain.get(Env.getCtx(), rs.getInt("P_Gain_ID"), trxName);
			Assignment = P_Assignment.get(Env.getCtx(), rs.getInt("P_Assignment_ID"), trxName);
			AssignmentParam = P_Assignment_Param.get(Env.getCtx(), Assignment.getP_Assignment_ID(),
					rs.getTimestamp("Day"), trxName);
			if (AssignmentParam != null) {
				if (Employee.getAD_Client_ID() == 11) {
					GainParameter = P_Gain_Parameter.get(Env.getCtx(), Gain.getP_Gain_ID(),
							AssignmentParam.getP_Collective_Labour_Agr_ID(), trxName);
				} else {
					GainParameter = P_Gain_Parameter.get(Env.getCtx(), Gain.getP_Gain_ID(),
							Employee.getP_Collective_Labour_Agr_ID(), trxName);
				}

				if (GainParameter == null) {
					CollectiveLabourArg = P_Collective_Labour_Agr.get(Env.getCtx(),
							AssignmentParam.getP_Collective_Labour_Agr_ID(), trxName);
					P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error,
							"Le gain  " + Gain.getValue() + " n'a pas de parametre actif - Convention collective : "
									+ CollectiveLabourArg.getValue() + " - " + CollectiveLabourArg.getName(),
							getTrxName());
				} else {
					PaymentGain = new P_Payment_Gain(Env.getCtx(), -1, trxName);
					PaymentGain.setP_Payment_ID(Payment.getP_Payment_ID());
					PaymentGain.setP_Employee_ID(Payment.getP_Employee_ID());

					PaymentGain.setP_Assignment_ID(Assignment.getP_Assignment_ID());
					PaymentGain.setP_Post_ID(Assignment.getP_Post_ID());
					PaymentGain.setP_Gain_ID(Gain.getP_Gain_ID());
					PaymentGain.setP_Gain_Parameter_ID(GainParameter.getP_Gain_Parameter_ID());
					PaymentGain.setMultiplyRate(GainParameter.getMultiplyRate());
					PaymentGain.setType_Rate(Gain.getType_Rate());

					PaymentGain.setQuantity(rs.getBigDecimal("DayQty"));
					PaymentGain.setFixed_Amount(GainParameter.getFixed_Amount());
					PaymentGain.setBonus_Amount(GainParameter.getBonus_Amount());
					PaymentGain.setLine(line);
					PaymentGain.setP_Year_ID(Payment.getP_Year_ID());
					PaymentGain.setP_Period_ID(rs.getInt("P_Period_ID"));
					PaymentGain.setP_Advance_ID(Gain.getP_Advance_ID());
					PaymentGain.setDay(rs.getTimestamp("Day"));
					PaymentGain.setStartDate(rs.getTimestamp("StartDate"));
					PaymentGain.setEndDate(rs.getTimestamp("EndDate"));
					PaymentGain.setC_Activity_ID(rs.getInt("C_Activity_ID"));
					PaymentGain.setP_Assignment_Param_ID(AssignmentParam.getP_Assignment_Param_ID());

					// 2009.07.08
					/*
					 * if ( rs.getInt( "Org_ID") != 0 )
					 * PaymentGain.setAD_Org_ID( rs.getInt( "Org_ID"));
					 * else
					 * PaymentGain.setAD_Org_ID( Payment.getAD_Org_ID());
					 */
					if (!AssignmentParam.isOut_Of_Range()) {
						PaymentGain.setP_Job_Title_ID(AssignmentParam.getP_Job_Title_ID());
						PaymentGain.setP_Collective_Labour_Agr_ID(AssignmentParam.getP_Collective_Labour_Agr_ID());
						PaymentGain.setP_Salary_Scale_Detail_ID(AssignmentParam.getP_Salary_Scale_Detail_ID());
						PaymentGain.setP_Salary_Scale_ID(AssignmentParam.getP_Salary_Scale_ID());
					} else {
						PaymentGain.setP_Job_Title_ID(Employee.getP_Job_Title_ID());
						PaymentGain.setP_Collective_Labour_Agr_ID(Employee.getP_Collective_Labour_Agr_ID());
					}
					PaymentGain.setP_UOM_ID(Gain.getP_UOM_ID());
					PaymentGain.setWeekIndex(rs.getInt("WeekIndex"));
					PaymentGain.setSalaryPercentage(rs.getBigDecimal("SalaryPercentage"));
					PaymentGain.setOriginTime(rs.getString("OriginTime"));
					PaymentGain.setP_Employee_LongTermLeave_ID(rs.getInt("P_Employee_LongTermLeave_ID"));

					//
					// parameter set by payment_group
					//

					if (Gain.getType_Rate().equals(P_Gain.TYPE_RATE_VacanceTauxEmploye)) {
						BigDecimal HourlyRate = Employee.getvacationHourlyRate();
						// 2024-03-20
						if (HourlyRate == null || HourlyRate.compareTo(Env.ZERO) == 0)
							HourlyRate = AssignmentParam.getHourly_Rate();

						PaymentGain.setHourly_Rate(HourlyRate);

					}

					else if (PaymentGroup.getTimeSheetModel().equals(P_Payment_Group.TIMESHEETMODEL_Detail))
						PaymentGain.setHourly_Rate(AssignmentParam.getHourly_Rate());
					else
						PaymentGain.setHourly_Rate(rs.getBigDecimal("Hourly_Rate"));

					// 2025-11-06
					if (rs.getBigDecimal("Hourly_Rate") != null && PaymentGain.getOriginTime().equals("PAR"))
						PaymentGain.setHourly_Rate(rs.getBigDecimal("Hourly_Rate"));

					PaymentGain.setP_Schedule_ID(rs.getInt("P_Schedule_ID"));

					PaymentGain.save();

					if (PaymentGain.getSalaryPercentage() != null
							&& PaymentGain.getSalaryPercentage().compareTo(ZERO) != 0) {
						Payment.setSalaryPercentage(PaymentGain.getSalaryPercentage());
						Payment.setIsSelfFinancedLeave(true);
						Payment.save();
					}

					// ISINCUMBENT
					P_Assignment_Param AssIncumbent = getAssIncumbent(Env.getCtx(), Payment.getP_Employee_ID(),
							PaymentGain.getDay(), trxName);
					if (AssIncumbent != null
							&& AssIncumbent.getP_Assignment_ID() != PaymentGain.getP_Assignment_ID()
							&& PaymentGain.getHourly_Rate().compareTo(ZERO) != 0
							&& PaymentGain.getHourly_Rate().compareTo(AssIncumbent.getHourly_Rate()) < 0) {

						P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information,
								"A valider, le taux horaire est inferieur a l'affectation titulaire. ", getTrxName());

					}
				}
			} else {
				P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error,
						"L'employe " + Employee.getValue() + " n'a pas de parametre d'affectation ("
								+ Assignment.getValue() + ") en date du " + rs.getTimestamp("Day").toString()
								+ " gain : " + Gain.getValue(),
						getTrxName());
				// P_Time_Sheet_Error.NewMessage( TimeSheet,
				// P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "L'employe
				// "+Employee.getValue()+" n'a pas d'affectation en date du
				// "+rs.getTimestamp("Day").toString(), getTrxName());
			}

		}
		rs.close();
		pstmt.close();
		pstmt = null;

		return;

	} //

	private P_Assignment_Param getAssIncumbent(Properties ctx, int P_Employee_ID, Timestamp day, String trxName) {
		String sql = "SELECT MAX(P_Assignment_Param_ID) P_Assignment_Param_ID FROM P_Assignment"
				+ " INNER JOIN P_Assignment_Param ON P_Assignment_Param.P_Assignment_ID = P_Assignment.P_Assignment_ID "
				+ " AND ( P_Assignment_Param.Effectto is null OR P_Assignment_Param.Effectto >= " + DB.TO_DATE(day)
				+ ") "
				+ "  WHERE P_Employee_ID= " + P_Employee_ID
				+ " AND ISINCUMBENT = 'Y' AND P_Assignment.IsActive = 'Y' ";
		PreparedStatement pstmt = null;

		int id = 0;
		try {
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				id = rs.getInt(1);
			rs.close();
			pstmt.close();
			pstmt = null;
		} catch (Exception e) {
			log.log(Level.SEVERE, " TimeValidation - getAssIncumbent - " + sql, e);
		}

		if (id == 0)
			return null;

		P_Assignment_Param AssIncumbent = P_Assignment_Param.get(ctx, id, trxName);
		return AssIncumbent;
	}

	/*
	 * private BigDecimal nvl ( BigDecimal amount )
	 * {
	 * if ( amount == null )
	 * return ZERO;
	 * 
	 * return amount;
	 * }
	 */

	//
	// Generation d'une line de bonus.
	//
	private void gen_bonus_read_line(P_Payment Payment, P_Employee_Bonus EmployeeBonus, String trxName) {
		// TODO
		/*
		 * int line = 1500;
		 * String sql = null;
		 * sql = "Select P_Time_Sheet_Detail_ID From P_Time_Sheet_Detail "
		 * + " WHERE P_Time_Sheet_ID = " + TimeSheet.getP_Time_Sheet_ID();
		 * 
		 * BigDecimal Quantity = new BigDecimal(0);
		 * 
		 * PreparedStatement pstmt = null;
		 * try
		 * {
		 * pstmt = DB.prepareStatement (sql);
		 * ResultSet rs = pstmt.executeQuery ();
		 * 
		 * while (rs.next ())
		 * {
		 * 
		 * P_Time_Sheet_Detail TimeSheetDetail = P_Time_Sheet_Detail.get( Env.getCtx(),
		 * rs.getInt(1) );
		 * P_Gain GainLine = P_Gain.get( Env.getCtx (), TimeSheetDetail.getP_Gain_ID(),
		 * trxName );
		 * //
		 * // Pas de prime automatique pour les gains d'absence.
		 * //
		 * if ( ! GainLine.isAbsence() )
		 * {
		 * line++;
		 * P_Assignment Assignment = P_Assignment.get( Env.getCtx(),
		 * TimeSheetDetail.getP_Assignment_ID(), trxName );
		 * P_Bonus Bonus = P_Bonus.get( Env.getCtx (), EmployeeBonus.getP_Bonus_ID(),
		 * trxName );
		 * P_Gain Gain = P_Gain.get( Env.getCtx (), Bonus.getP_Gain_ID(), trxName );
		 * 
		 * boolean isSchedule = false;
		 * boolean isWeekly = false;
		 * boolean isPost = false;
		 * boolean isLocalization = false;
		 * 
		 * if ( Bonus.isWeekly())
		 * {
		 * if ( Bonus.isSunday() && nvl( TimeSheetDetail.getSunday()).compareTo( ZERO )
		 * != 0 )
		 * {
		 * isWeekly = true;
		 * Quantity = Quantity.add( TimeSheetDetail.getSunday() );
		 * }
		 * if ( Bonus.isMonday() && nvl( TimeSheetDetail.getMonday()).compareTo( ZERO )
		 * != 0 )
		 * {
		 * isWeekly = true;
		 * Quantity = Quantity.add( TimeSheetWeekly.getMonday() );
		 * }
		 * if ( Bonus.isThursday() && nvl(TimeSheetWeekly.getThursday()).compareTo( ZERO
		 * ) != 0 )
		 * {
		 * isWeekly = true;
		 * Quantity = Quantity.add( TimeSheetWeekly.getThursday() );
		 * }
		 * if ( Bonus.isWednesday() && nvl(TimeSheetWeekly.getWednesday()).compareTo(
		 * ZERO ) != 0 )
		 * {
		 * isWeekly = true;
		 * Quantity = Quantity.add( TimeSheetWeekly.getWednesday() );
		 * }
		 * if ( Bonus.isTuesday() && nvl(TimeSheetWeekly.getTuesday()).compareTo( ZERO )
		 * != 0 )
		 * {
		 * isWeekly = true;
		 * Quantity = Quantity.add( TimeSheetWeekly.getTuesday() );
		 * }
		 * if ( Bonus.isFriday() && nvl(TimeSheetWeekly.getFriday()).compareTo( ZERO )
		 * != 0 )
		 * {
		 * isWeekly = true;
		 * Quantity = Quantity.add( TimeSheetWeekly.getFriday() );
		 * }
		 * if ( Bonus.isSaturday() && nvl(TimeSheetWeekly.getSaturday()).compareTo( ZERO
		 * ) != 0 )
		 * {
		 * isWeekly = true;
		 * Quantity = Quantity.add( TimeSheetWeekly.getSaturday() );
		 * }
		 * }
		 * else isWeekly = true;
		 * 
		 * if ( Bonus.isSchedule())
		 * {
		 * if ( TimeSheetDetail.getP_Schedule_ID() == Bonus.getP_Schedule_ID() )
		 * {
		 * isSchedule = true;
		 * if ( ! Bonus.isWeekly() )
		 * {
		 * Quantity = nvl( TimeSheetDetail.getDayQty() );
		 * Quantity = Quantity.add( nvl( TimeSheetWeekly.getSunday() ));
		 * Quantity = Quantity.add( nvl( TimeSheetWeekly.getMonday() ));
		 * Quantity = Quantity.add( nvl( TimeSheetWeekly.getThursday() ));
		 * Quantity = Quantity.add( nvl( TimeSheetWeekly.getWednesday() ));
		 * Quantity = Quantity.add( nvl( TimeSheetWeekly.getTuesday() ));
		 * Quantity = Quantity.add( nvl( TimeSheetWeekly.getFriday() ));
		 * Quantity = Quantity.add( nvl( TimeSheetWeekly.getSaturday() ));
		 * }
		 * }
		 * }
		 * else isSchedule = true;
		 * 
		 * 
		 * if ( Bonus.isPost() )
		 * {
		 * if ( Assignment.getP_Post_ID() == Bonus.getP_Post_ID() )
		 * {
		 * isPost = true;
		 * if ( ! Bonus.isWeekly() )
		 * {
		 * Quantity = nvl( TimeSheetWeekly.getDayQty());
		 * Quantity = Quantity.add( nvl(TimeSheetWeekly.getSunday() ));
		 * Quantity = Quantity.add( nvl(TimeSheetWeekly.getMonday() ));
		 * Quantity = Quantity.add( nvl(TimeSheetWeekly.getThursday() ));
		 * Quantity = Quantity.add( nvl(TimeSheetWeekly.getWednesday() ));
		 * Quantity = Quantity.add( nvl(TimeSheetWeekly.getTuesday() ));
		 * Quantity = Quantity.add( nvl(TimeSheetWeekly.getFriday() ));
		 * Quantity = Quantity.add( nvl(TimeSheetWeekly.getSaturday() ));
		 * }
		 * }
		 * }
		 * else isPost = true;
		 * 
		 * if ( Bonus.isLocalization() )
		 * {
		 * if ( Assignment.getP_Workplace_ID() == Bonus.getP_Workplace_ID() )
		 * {
		 * isLocalization = true;
		 * if ( ! Bonus.isWeekly() )
		 * {
		 * Quantity = nvl(TimeSheetWeekly.getDayQty());
		 * Quantity = Quantity.add( nvl(TimeSheetWeekly.getSunday() ));
		 * Quantity = Quantity.add( nvl(TimeSheetWeekly.getMonday() ));
		 * Quantity = Quantity.add( nvl(TimeSheetWeekly.getThursday() ));
		 * Quantity = Quantity.add( nvl(TimeSheetWeekly.getWednesday() ));
		 * Quantity = Quantity.add( nvl(TimeSheetWeekly.getTuesday() ));
		 * Quantity = Quantity.add( nvl(TimeSheetWeekly.getFriday() ));
		 * Quantity = Quantity.add( nvl(TimeSheetWeekly.getSaturday() ));
		 * }
		 * }
		 * }
		 * else isLocalization = true;
		 * 
		 * if ( isSchedule && isWeekly && isPost && isLocalization )
		 * {
		 * P_Payment_Gain PaymentGain = new P_Payment_Gain(Env.getCtx (), -1, trxName);
		 * P_Gain_Parameter GainParameter = P_Gain_Parameter.get( Env.getCtx (),
		 * PaymentGain.getP_Gain_ID(), Assignment.getP_Collective_Labour_Agr_ID(),
		 * trxName );
		 * 
		 * PaymentGain.setP_Payment_ID( Payment.getP_Payment_ID() );
		 * PaymentGain.setP_Employee_ID( Payment.getP_Employee_ID());
		 * 
		 * 
		 * PaymentGain.setP_Gain_ID( Bonus.getP_Gain_ID() );
		 * PaymentGain.setP_Gain_Parameter_ID( GainParameter.getP_Gain_Parameter_ID() );
		 * 
		 * PaymentGain.setType_Rate( Gain.getType_Rate() );
		 * PaymentGain.setFixed_Amount( Gain.getFixed_Amount() );
		 * PaymentGain.setBonus_Amount( Gain.getBonus_Amount() );
		 * PaymentGain.setLine( line );
		 * PaymentGain.setP_Year_ID( Payment.getP_Year_ID() );
		 * PaymentGain.setP_Period_ID( Period.getP_Period_ID() );
		 * PaymentGain.setP_Advance_ID( Gain.getP_Advance_ID() );
		 * PaymentGain.setMultiplyRate( GainParameter.getMultiplyRate());
		 * PaymentGain.setP_Assignment_ID( TimeSheetDetail.getP_Assignment_ID() );
		 * PaymentGain.setQuantity( Quantity );
		 * 
		 * PaymentGain.setP_Collective_Labour_Agr_ID(
		 * Assignment.getP_Collective_Labour_Agr_ID());
		 * PaymentGain.setP_Job_Title_ID(Assignment.getP_Job_Title_ID());
		 * PaymentGain.setP_Salary_Scale_Detail_ID(
		 * Assignment.getP_Salary_Scale_Detail_ID());
		 * PaymentGain.setP_Salary_Scale_ID( Assignment.getP_Salary_Scale_ID());
		 * PaymentGain.setP_UOM_ID( Gain.getP_UOM_ID());
		 * 
		 * PaymentGain.setDay( Period.getEndDate() );
		 * PaymentGain.setWeekIndex( (TimeUtil.getDaysBetween( Period.getStartDate(),
		 * Period.getEndDate()) /7 ) +1 );
		 * PaymentGain.setSalaryPercentage( null );
		 * PaymentGain.setOriginTime( "PRI");
		 * PaymentGain.setP_Assignment_Param_ID(
		 * AssignmentParam.getP_Assignment_Param_ID());
		 * 
		 * PaymentGain.save();
		 * 
		 * }
		 * 
		 * }
		 * }
		 * rs.close ();
		 * pstmt.close ();
		 * pstmt = null;
		 * 
		 * }
		 * catch (Exception e)
		 * {
		 * log.log (Level.SEVERE,"bonus_read_line - " + sql, e);
		 * }
		 * 
		 * return ;
		 */
	}

	private void CreditsVariationOpenAnnuel(int P_Period_ID, P_Employee Employee, String trxName) {
		P_Period l_Period = P_Period.get(Env.getCtx(), P_Period_ID, m_trxName);
		int nbrDays = TimeUtil.getDaysBetween(l_Period.getStartDate(), l_Period.getEndDate()) + 1;

		// Les employes en ALD pas d'attribution.
		if (TimeSheet.getSheetType().equals(P_Time_Sheet.SHEETTYPE_Regular)) {
			Timestamp Day = l_Period.getStartDate();

			for (int j = 0; j < nbrDays; j++) {
				Day = TimeUtil.addDays(l_Period.getStartDate(), j);

				calCredits.CreditsVariationAnnuelOpen(Day);
			}
		}

	}

	private void CreditsVariationCloseAnnuel(int P_Period_ID, P_Employee Employee, String trxName) {
		P_Period l_Period = P_Period.get(Env.getCtx(), P_Period_ID, m_trxName);
		int nbrDays = TimeUtil.getDaysBetween(l_Period.getStartDate(), l_Period.getEndDate()) + 1;

		// Les employes en ALD pas d'attribution.
		if (TimeSheet.getSheetType().equals(P_Time_Sheet.SHEETTYPE_Regular)) {
			Timestamp Day = l_Period.getStartDate();

			for (int j = 0; j < nbrDays; j++) {
				Day = TimeUtil.addDays(l_Period.getStartDate(), j);

				calCredits.CreditsVariationAnnuelClose(Day);
			}
		}

	}

	public boolean calcul_gain_credits(P_Payment Payment, String trxName) throws Exception {

		boolean isError = false;

		CreditsVariationOpenAnnuel(Payment.getP_Period_ID(), Employee, trxName);

		String sql = null;
		sql = "Select distinct P_Period_ID From P_Payment_Gain WHERE P_Payment_ID = " + Payment.getP_Payment_ID()
				+ " Order By P_Period_ID ";

		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement(sql, getTrxName());
		ResultSet rs = pstmt.executeQuery();

		P_Period l_Period;
		while (rs.next()) {

			l_Period = P_Period.get(Env.getCtx(), rs.getInt("P_Period_ID"), getTrxName());
			int nbrDays = TimeUtil.getDaysBetween(l_Period.getStartDate(), l_Period.getEndDate()) + 1;

			Timestamp Day = l_Period.getStartDate();

			for (int j = 0; j < nbrDays; j++) {
				Day = TimeUtil.addDays(l_Period.getStartDate(), j);

				if (TimeSheet.getSheetType().equals(P_Time_Sheet.SHEETTYPE_Regular) ||
						TimeSheet.getSheetType().equals(P_Time_Sheet.SHEETTYPE_Complementary) ||
						TimeSheet.getSheetType().equals(P_Time_Sheet.SHEETTYPE_HolidayPayment) ||
						TimeSheet.getSheetType().equals(P_Time_Sheet.SHEETTYPE_ExpenseAccount) ||
						TimeSheet.getSheetType().equals(P_Time_Sheet.SHEETTYPE_SeparationPay)) {
					/*
					 * // log.log (Level.INFO,"Validation - CreditsVariationAnnuelOpen " +
					 * Day.toString() );
					 * if ( Payment.getP_Period_ID() == l_Period.getP_Period_ID() &&
					 * TimeSheet.getSheetType().equals( P_Time_Sheet.SHEETTYPE_Regular) )
					 * calCredits.CreditsVariationAnnuelOpen( Day);
					 * else if ( Employee.isCustomFieldYesNo01() && TimeSheet.getSheetType().equals(
					 * P_Time_Sheet.SHEETTYPE_Regular))
					 * {
					 * calCredits.CreditsVariationAnnuelOpen( Day);
					 * }
					 */

					if (exists_time(Payment, Day)) {
						// log.log (Level.INFO,"Validation - calcul_gain " + Day.toString());
						calcul_gain(Payment, Day);
						// log.log (Level.INFO,"Validation - CreditsEvaluationTransaction " +
						// Day.toString());
						calCredits.CreditsEvaluationTransaction(Payment, Day);
					}

					// calCredits.CreditsEvaluationFixe( Day );

					if (Payment.getP_Period_ID() == l_Period.getP_Period_ID()
							&& TimeSheet.getSheetType().equals(P_Time_Sheet.SHEETTYPE_Regular)) {

						// log.log (Level.INFO,"Validation - CreditsEvaluationFixe " + Day.toString() );
						calCredits.CreditsEvaluationFixe(Day);
						// log.log (Level.INFO,"Validation - CreditsVariationAnnuelClose " +
						// Day.toString() );
						calCredits.CreditsVariationAnnuelClose(Day);
					}

					// validation without error
					// log.log (Level.INFO,"Validation - CreditsValidation " + Day.toString() );
					isError = calCredits.CreditsValidation(Day);

					// log.log (Level.INFO,"Validation - CreditsParting " + Day.toString() );
					if (Payment.getP_Period_ID() == l_Period.getP_Period_ID()
							&& TimeSheet.getSheetType().equals(P_Time_Sheet.SHEETTYPE_Regular))
						calCredits.CreditsParting(Day);

				} else {
					calcul_gain(Payment, Day);
				}

			}

		}
		rs.close();
		pstmt.close();
		pstmt = null;

		// CreditsVariationCloseAnnuel( Payment.getP_Period_ID(), Employee, trxName);

		return isError;
	}

	/*
	 * public boolean calcul_gain_credits( P_Payment Payment , String trxName)
	 * throws Exception
	 * {
	 * 
	 * boolean isError = false;
	 * 
	 * String sql = null;
	 * sql = "Select distinct P_Period_ID From P_Payment_Gain WHERE P_Payment_ID = "
	 * + Payment.getP_Payment_ID() + " Order By P_Period_ID ";
	 * 
	 * PreparedStatement pstmt = null;
	 * pstmt = DB.prepareStatement (sql,getTrxName());
	 * ResultSet rs = pstmt.executeQuery ();
	 * 
	 * P_Period l_Period;
	 * while (rs.next ())
	 * {
	 * 
	 * l_Period = P_Period.get( Env.getCtx(), rs.getInt("P_Period_ID"),
	 * getTrxName());
	 * int nbrDays = TimeUtil.getDaysBetween( l_Period.getStartDate() ,
	 * l_Period.getEndDate() ) + 1;
	 * 
	 * 
	 * Timestamp Day = l_Period.getStartDate();
	 * 
	 * for (int j = 0; j < nbrDays ; j++)
	 * {
	 * Day = TimeUtil.addDays( l_Period.getStartDate(), j );
	 * 
	 * if ( TimeSheet.getSheetType().equals( P_Time_Sheet.SHEETTYPE_Regular) ||
	 * TimeSheet.getSheetType().equals( P_Time_Sheet.SHEETTYPE_Complementary))
	 * {
	 * 
	 * log.log (Level.INFO,"Validation - CreditsVariationAnnuelOpen " +
	 * Day.toString() );
	 * if ( Payment.getP_Period_ID() == l_Period.getP_Period_ID() )
	 * calCredits.CreditsVariationAnnuelOpen( Day);
	 * 
	 * if ( exists_time( Payment, Day ) )
	 * {
	 * log.log (Level.INFO,"Validation - calcul_gain " + Day.toString());
	 * calcul_gain( Payment, Day );
	 * log.log (Level.INFO,"Validation - CreditsEvaluationTransaction " +
	 * Day.toString());
	 * calCredits.CreditsEvaluationTransaction( Payment, Day );
	 * }
	 * 
	 * 
	 * if ( Payment.getP_Period_ID() == l_Period.getP_Period_ID() )
	 * {
	 * 
	 * log.log (Level.INFO,"Validation - CreditsEvaluationFixe " + Day.toString() );
	 * calCredits.CreditsEvaluationFixe( Day );
	 * log.log (Level.INFO,"Validation - CreditsVariationAnnuelClose " +
	 * Day.toString() );
	 * calCredits.CreditsVariationAnnuelClose( Day);
	 * }
	 * 
	 * // validation without error
	 * log.log (Level.INFO,"Validation - CreditsValidation " + Day.toString() );
	 * isError = calCredits.CreditsValidation( Day );
	 * 
	 * log.log (Level.INFO,"Validation - CreditsParting " + Day.toString() );
	 * //if ( Payment.getP_Period_ID() == l_Period.getP_Period_ID() )
	 * calCredits.CreditsParting( Day );
	 * 
	 * 
	 * }
	 * else
	 * {
	 * calcul_gain( Payment, Day );
	 * }
	 * 
	 * }
	 * 
	 * }
	 * rs.close ();
	 * pstmt.close ();
	 * pstmt = null;
	 * 
	 * return isError;
	 * }
	 * 
	 */

	private void gen_bonus(P_Payment Payment, String trxName) throws Exception {

		String sql = null;
		sql = "Select P_Employee_Bonus_ID From P_Employee_Bonus "
				+ " WHERE IsActive = 'Y'"
				+ "   AND P_Employee_ID = " + Employee.getP_Employee_ID();

		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement(sql, trxName);
		ResultSet rs = pstmt.executeQuery();

		P_Employee_Bonus EmployeeBonus;
		while (rs.next()) {
			EmployeeBonus = P_Employee_Bonus.get(Env.getCtx(), rs.getInt(1), trxName);
			gen_bonus_read_line(Payment, EmployeeBonus, trxName);
		}
		rs.close();
		pstmt.close();
		pstmt = null;

		return;

	} //

	public void calcul_gain(P_Payment_Gain PaymentGain) throws Exception {
		P_Credits Credits = null;
		BigDecimal CreditsSoldes = new BigDecimal(0);

		BigDecimal quantityCalc = new BigDecimal(0);
		BigDecimal amountCalc = new BigDecimal(0);

		// Absense longue duree - Generation des exceptions, Deduction
		// if ( PaymentGain.getOriginTime() != null &&
		// PaymentGain.getOriginTime().equals("ALD"))
		if (Employee.isLongTermLeave(Period, m_trxName)) {
			Generate_LongTermLeave_Deduction(PaymentGain);
		}

		P_Gain Gain = P_Gain.get(Env.getCtx(), PaymentGain.getP_Gain_ID(), getTrxName());

		P_Assignment_Param AssignmentParam = P_Assignment_Param.get(Env.getCtx(),
				PaymentGain.getP_Assignment_Param_ID(), getTrxName());

		P_Gain_Parameter GainParameter;
		if (Employee.getAD_Client_ID() == 11) {
			GainParameter = P_Gain_Parameter.get(Env.getCtx(), Gain.getP_Gain_ID(),
					AssignmentParam.getP_Collective_Labour_Agr_ID(), getTrxName());
		} else {
			GainParameter = P_Gain_Parameter.get(Env.getCtx(), Gain.getP_Gain_ID(),
					Employee.getP_Collective_Labour_Agr_ID(), getTrxName());
		}

		// validation du poste de l'employe pour qu'il soit actif.
		P_Post Post = P_Post.get(Env.getCtx(), PaymentGain.getP_Post_ID(), getTrxName());
		if (!Post.isActive()) {
			P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Warning,
					"Le poste de l'employe est inactif " + Post.getValue() + ' ' + Post.getName(), getTrxName());
		}

		// Si le gain n'est pas de la famille de HRN ( Heures non renumerees )
		// Gain of % admisible Gain
		if (Gain.getP_Gain_Family_ID() != 105 && Gain.getP_Method_Gain_ID() != 107
				&& Gain.getP_Method_Gain_ID() != 300) {
			if (PaymentGain.getHourly_Rate() == null)
				PaymentGain.setHourly_Rate(AssignmentParam.getHourly_Rate());
		} else {
			PaymentGain.setHourly_Rate(ZERO);
		}

		BigDecimal amountAdmissible = Env.ZERO;
		BigDecimal quantityAdmissible = Env.ZERO;

		BigDecimal multiplyRate = GainParameter.getMultiplyRate();

		// OverTime deja le bon taux sur la feuille de temps )
		/*
		 * if ( Payment.getAD_Client_ID() != 11 )
		 * {
		 * if ( Gain.getP_Method_Gain_ID() == 101 )
		 * multiplyRate = Env.ONE;
		 * }
		 */

		switch (Gain.getP_Method_Gain_ID()) {
			case 101:
				quantityCalc = PaymentGain.getQuantity();
				quantityCalc = quantityCalc.setScale(4, BigDecimal.ROUND_HALF_UP);
				amountCalc = quantityCalc.multiply(PaymentGain.getHourly_Rate().multiply(multiplyRate));

				// gain en jours
				// 2024-05-07
				if (Gain.getP_UOM_ID() == 102 && Gain.getType_Rate().equals(P_Gain.TYPE_RATE_VacanceTauxEmploye)) {
					BigDecimal HourlyRate = Employee.getvacationHourlyRate();
					// 2024-03-20
					if (HourlyRate == null || HourlyRate.compareTo(Env.ZERO) == 0)
						HourlyRate = AssignmentParam.getHourly_Rate();

					PaymentGain.setHourly_Rate(HourlyRate);

					amountCalc = quantityCalc.multiply(PaymentGain.getHourly_Rate().multiply(multiplyRate))
							.multiply(AssignmentParam.getDay_Hours());
				}

				// Taux horaire special pour les employes retraite progressive.
				if (Gain.getValue().equals("GC02") && Employee.getP_LongTermLeave_ID() == 1100164
						&& Employee.getAD_Org_ID() == 1000002 && Employee.getSickLeaveHourlyRate() != null
						&& PaymentGain.getOriginTime().equals("PAR") == false

				) {
					BigDecimal HourlyRate = Employee.getSickLeaveHourlyRate();
					if (HourlyRate == null || HourlyRate.compareTo(Env.ZERO) == 0)
						HourlyRate = AssignmentParam.getHourly_Rate();

					PaymentGain.setHourly_Rate(HourlyRate);

					// amountCalc = quantityCalc.multiply( PaymentGain.getHourly_Rate().multiply(
					// multiplyRate ) ).multiply( AssignmentParam.getDay_Hours() ) ;
					amountCalc = quantityCalc.multiply(PaymentGain.getHourly_Rate().multiply(multiplyRate));// .multiply(
																											// AssignmentParam.getDay_Hours()
																											// ) ;
				}

				break;
			// Prime de disponibilite
			case 117:
				quantityCalc = PaymentGain.getQuantity();
				quantityCalc = quantityCalc.setScale(4, BigDecimal.ROUND_HALF_UP);
				amountCalc = quantityCalc.multiply(PaymentGain.getHourly_Rate().multiply(multiplyRate));
				break;
			case 102:
				quantityCalc = new BigDecimal(0);
				amountCalc = PaymentGain.getQuantity();
				break;
			case 103:
				// Avance net data entry
				quantityCalc = new BigDecimal(0);
				amountCalc = PaymentGain.getQuantity();
				break;
			case 104:
				// gain no remunerate
				quantityCalc = PaymentGain.getQuantity();
				amountCalc = new BigDecimal(0);
				break;
			case 105:
				// Bonus of Quantity
				quantityCalc = PaymentGain.getQuantity();
				if (Gain.getType_Rate().equals(P_Gain.TYPE_RATE_FixedRate))
					PaymentGain.setHourly_Rate(Gain.getHourly_Rate());

				if (GainParameter.getBonus_Amount() != null && GainParameter.getBonus_Amount().compareTo(Env.ZERO) != 0)
					amountCalc = quantityCalc.multiply(GainParameter.getBonus_Amount().multiply(multiplyRate));
				else
					amountCalc = quantityCalc.multiply(PaymentGain.getHourly_Rate().multiply(multiplyRate));
				// PaymentGain.setHourly_Rate( ZERO );
				// quantityCalc = new BigDecimal( 0 );
				break;
			case 106:
				// Bonus fixe amount
				quantityCalc = new BigDecimal(0);
				if (PaymentGain.getQuantity() != null)
					amountCalc = PaymentGain.getQuantity();
				else
					amountCalc = GainParameter.getFixed_Amount();
				break;
			case 108:
				if (Gain.getP_Credits_ID() == 0)
					P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error,
							"Le systeme n'a pas trouve banque pour le code de gain", getTrxName());
				Credits = P_Credits.get(Env.getCtx(), Gain.getP_Credits_ID(), getTrxName());
				quantityCalc = new BigDecimal(0);
				CreditsSoldes = P_Credits.getEmployeeSolde(Credits, Employee, PaymentGain.getDay(), getTrxName());
				Credits.setEmployeeSolde(Payment, Period, PaymentGain.getDay(),
						CreditsSoldes.multiply(new BigDecimal(-1)), getTrxName());
				amountCalc = CreditsSoldes;
				break;
			case 208:
				P_Assignment Assignment208 = P_Assignment.get(Env.getCtx(), PaymentGain.getP_Assignment_ID(),
						getTrxName());
				P_Assignment_Param AssignmentParam208 = P_Assignment_Param.get(Env.getCtx(),
						Assignment208.getP_Assignment_ID(), TimeUtil.addDays(PaymentGain.getDay(), -8), getTrxName());
				PaymentGain.setHourly_Rate(AssignmentParam208.getHourly_Rate());

				// Sld of the accumulated credit ( Money )
				if (Gain.getP_Credits_ID() == 0)
					Credits = getCreditsVideBanque(Env.getCtx(), TimeSheet, Gain.getP_Gain_ID(),
							Employee.getP_Employee_ID(), getTrxName());
				else
					Credits = P_Credits.get(Env.getCtx(), Gain.getP_Credits_ID(), getTrxName());
				quantityCalc = new BigDecimal(0);
				P_UOM creditsUnits208 = P_UOM.get(Env.getCtx(), Credits.getP_UOM_ID(), getTrxName());
				if (creditsUnits208.getValue().equals("J")) {
					CreditsSoldes = P_Credits.getEmployeeSolde(Credits, Employee, PaymentGain.getDay(), getTrxName());
					// Credits.setEmployeeSolde( Payment, Period, PaymentGain.getDay(),
					// CreditsSoldes.multiply( new BigDecimal( -1 )), getTrxName() );
					quantityCalc = CreditsSoldes.multiply(AssignmentParam.getDay_Hours());
				} else {
					CreditsSoldes = P_Credits.getEmployeeSolde(Credits, Employee, PaymentGain.getDay(), getTrxName());
					// Credits.setEmployeeSolde( Payment, Period, PaymentGain.getDay(),
					// CreditsSoldes.multiply( new BigDecimal( -1 )), getTrxName() );
					quantityCalc = CreditsSoldes;

				}
				amountCalc = quantityCalc.multiply(PaymentGain.getHourly_Rate().multiply(multiplyRate));
				break;
			case 109:
				// avance net caculated
				quantityCalc = PaymentGain.getQuantity();
				amountCalc = new BigDecimal(0);
				break;
			case 110:
				// Gain in money
				quantityCalc = new BigDecimal(0);
				amountCalc = PaymentGain.getQuantity();
				break;
			case 111:
				// Sld of the accumulated credit ( Quantity )
				Credits = P_Credits.get(Env.getCtx(), Gain.getP_Credits_ID(), getTrxName());
				CreditsSoldes = P_Credits.getEmployeeSolde(Credits, Employee, PaymentGain.getDay(), getTrxName());
				Credits.setEmployeeSolde(Payment, Period, PaymentGain.getDay(),
						CreditsSoldes.multiply(new BigDecimal(-1)), getTrxName());
				quantityCalc = CreditsSoldes;

				// 2023-09-20
				P_UOM creditsUnits = P_UOM.get(Env.getCtx(), Credits.getP_UOM_ID(), getTrxName());
				if (creditsUnits.getValue().equals("J")) {
					quantityCalc = quantityCalc.multiply(AssignmentParam.getDay_Hours());
				}

				quantityCalc = quantityCalc.setScale(4, BigDecimal.ROUND_HALF_UP);
				amountCalc = quantityCalc.multiply(PaymentGain.getHourly_Rate().multiply(multiplyRate));
				break;
			case 112:
				// Avance brut
				quantityCalc = PaymentGain.getQuantity();
				quantityCalc = quantityCalc.setScale(4, BigDecimal.ROUND_HALF_UP);
				amountCalc = quantityCalc.multiply(PaymentGain.getHourly_Rate().multiply(multiplyRate));
				break;
			case 114:
				// Gain avec taux speciaux.
				P_Assignment Assignment = new P_Assignment(Env.getCtx(), PaymentGain.getP_Assignment_ID(),
						getTrxName());
				BigDecimal SpecialRate = Assignment.getSpecialRate(Env.getCtx(), Gain.getP_SpecialRate_ID(),
						PaymentGain.getDay(), getTrxName());
				if (SpecialRate == null)
					P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error,
							Msg.getMsg(Env.getCtx(), "P_SpecialRateError"), getTrxName());
				else
					PaymentGain.setHourly_Rate(SpecialRate);
				quantityCalc = PaymentGain.getQuantity();
				quantityCalc = quantityCalc.setScale(4, BigDecimal.ROUND_HALF_UP);
				amountCalc = quantityCalc.multiply(PaymentGain.getHourly_Rate().multiply(multiplyRate));
				break;
			case 115:
				// Prime sur qte. selon borne.
				P_Gain_Bound GainBound = P_Gain_Bound.get(Env.getCtx(), Payment.getP_Employee_ID(), Gain.getP_Gain_ID(),
						Payment.getP_Job_Type_ID(), PaymentGain.getDay(), getTrxName());
				if (GainBound != null) {
					quantityCalc = PaymentGain.getQuantity();
					quantityCalc = quantityCalc.setScale(4, BigDecimal.ROUND_HALF_UP);
					amountCalc = quantityCalc.multiply(PaymentGain.getHourly_Rate().multiply(multiplyRate)).multiply(
							GainBound.getMultiplyRate().divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
					quantityCalc = Env.ZERO;
				}
				break;

			case 107:
				if (PaymentGain.getQuantity() != null && PaymentGain.getQuantity().compareTo(Env.ZERO) != 0) {
					quantityCalc = new BigDecimal(0);
					amountCalc = PaymentGain.getQuantity();
				} else {
					amountAdmissible = getGainAdmissible(Payment, Gain, PaymentGain.getStartDate(),
							PaymentGain.getEndDate(), PaymentGain.getP_Assignment_ID());
					quantityAdmissible = getQuantityAdmissible(Payment, Gain, PaymentGain.getStartDate(),
							PaymentGain.getEndDate(), PaymentGain.getP_Assignment_ID(), PaymentGain.getP_Schedule_ID());
					amountCalc = amountAdmissible.multiply(PaymentGain.getMultiplyRate()); // Gain.getMultiplyRate()

				}
				break;

			case 300: {
				quantityCalc = PaymentGain.getQuantity();
				if (quantityCalc == null || quantityCalc.compareTo(Env.ZERO) == 0)
					quantityCalc = getQuantityAdmissible(Payment, Gain, Period.getStartDate(), Period.getEndDate(),
							PaymentGain.getP_Assignment_ID(), PaymentGain.getP_Schedule_ID());
				amountCalc = quantityCalc.multiply(PaymentGain.getMultiplyRate()); // Gain.getMultiplyRate()

			}
				break;

			case 113:
				// Traitement differe.
				amountAdmissible = getGainAdmissible(Payment, Gain, PaymentGain.getStartDate(),
						PaymentGain.getEndDate(), PaymentGain.getP_Assignment_ID());
				quantityAdmissible = getQuantityAdmissible(Payment, Gain, PaymentGain.getStartDate(),
						PaymentGain.getEndDate(), PaymentGain.getP_Assignment_ID(), PaymentGain.getP_Schedule_ID());
				PaymentGain.setMultiplyRate((PaymentGain.getSalaryPercentage().subtract(new BigDecimal(100)))
						.divide(new BigDecimal(100), 6, BigDecimal.ROUND_HALF_UP));
				amountCalc = amountAdmissible.multiply(PaymentGain.getMultiplyRate()); // Gain.getMultiplyRate()
				break;

			case 213:
				quantityCalc = PaymentGain.getQuantity();
				quantityCalc = quantityCalc.setScale(4, BigDecimal.ROUND_HALF_UP);
				amountCalc = Env.ZERO;
				break;

			case 116:

				amountAdmissible = getGainAdmissible(Payment, Gain, PaymentGain.getStartDate(),
						PaymentGain.getEndDate(), PaymentGain.getP_Assignment_ID());
				quantityAdmissible = getQuantityAdmissible(Payment, Gain, PaymentGain.getStartDate(),
						PaymentGain.getEndDate(), PaymentGain.getP_Assignment_ID(), PaymentGain.getP_Schedule_ID());
				if (Gain.getType_Rate().equals(P_Gain.TYPE_RATE_FixedRate)) {
					quantityCalc = quantityAdmissible;
					amountCalc = quantityAdmissible.multiply(PaymentGain.getMultiplyRate());

				} else {
					amountCalc = quantityAdmissible.multiply(AssignmentParam.getHourly_Rate())
							.multiply(PaymentGain.getMultiplyRate()); // Gain.getMultiplyRate()

				}
				break;

			// gain de base - en montant
			case 118:
				quantityCalc = ZERO;

				P_Frequency Frequency = P_Frequency.get(Env.getCtx(), Period.getP_Frequency_ID(), m_trxName);
				BigDecimal nbrPeriod = new BigDecimal(Frequency.getNumberOfPeriod());

				if (Gain.getP_UOM_ID() == 100) {
					if (PaymentGain.getQuantity().compareTo(Env.ZERO) != 0) {
						quantityCalc = PaymentGain.getQuantity();
					}
					// 2008-05-12 on ne calcul pas si la quantity saisie est 0
					// else
					// {
					// quantityCalc = (AssignmentParam.getWeekly_Hours().multiply( new
					// BigDecimal(52))).divide( nbrPeriod, 2, BigDecimal.ROUND_HALF_UP );
					// }

					/*
					 * if ( Period.getStartDate().compareTo( PaymentGain.getStartDate() ) != 0 ||
					 * Period.getEndDate().compareTo( PaymentGain.getEndDate() ) != 0 )
					 * {
					 * int nbrDays = TimeUtil.getDaysBetween( PaymentGain.getStartDate() ,
					 * PaymentGain.getEndDate() ) + 1;
					 * int nbrDaysTot = TimeUtil.getDaysBetween( Period.getStartDate() ,
					 * Period.getEndDate() ) + 1;
					 * 
					 * BigDecimal facteur = new BigDecimal( nbrDaysTot ).divide( new BigDecimal(
					 * nbrDays ), 6, BigDecimal.ROUND_HALF_UP );
					 * if ( facteur.compareTo( Env.ZERO ) != 0)
					 * quantityCalc = quantityCalc.divide( facteur, 6, BigDecimal.ROUND_HALF_UP
					 * ).setScale(2, BigDecimal.ROUND_HALF_UP);
					 * 
					 * }
					 */
					amountCalc = quantityCalc.multiply(PaymentGain.getHourly_Rate()).setScale(2,
							BigDecimal.ROUND_HALF_UP);

				} else {
					if (PaymentGain.getQuantity().compareTo(Env.ZERO) != 0) {
						amountCalc = PaymentGain.getQuantity();
						// 2023-05-25
						quantityCalc = Env.ZERO;
					}
					// 2023-11-28 mis en commentaire, je ne sais pas a quoi ca sert.
					/*
					 * else
					 * {
					 * amountCalc = AssignmentParam.getAnnual_Salary().divide( nbrPeriod, 2,
					 * BigDecimal.ROUND_HALF_UP );
					 * 
					 * if ( Period.getStartDate().compareTo( PaymentGain.getStartDate() ) != 0 ||
					 * Period.getEndDate().compareTo( PaymentGain.getEndDate() ) != 0 )
					 * {
					 * int nbrDays = TimeUtil.getDaysBetween( PaymentGain.getStartDate() ,
					 * PaymentGain.getEndDate() ) + 1;
					 * int nbrDaysTot = TimeUtil.getDaysBetween( Period.getStartDate() ,
					 * Period.getEndDate() ) + 1;
					 * 
					 * BigDecimal facteur = new BigDecimal( nbrDaysTot ).divide( new BigDecimal(
					 * nbrDays ), 6, BigDecimal.ROUND_HALF_UP );
					 * if ( facteur.compareTo( Env.ZERO ) != 0)
					 * amountCalc = amountCalc.divide( facteur, 6, BigDecimal.ROUND_HALF_UP
					 * ).setScale(2, BigDecimal.ROUND_HALF_UP);
					 * }
					 * 
					 * }
					 */
					// 2022-05-06 division par 0

					if (PaymentGain.getHourly_Rate() != null && PaymentGain.getHourly_Rate().compareTo(Env.ZERO) != 0) {
						// quantityCalc = amountCalc.divide( PaymentGain.getHourly_Rate(),2,
						// BigDecimal.ROUND_HALF_UP );
						quantityCalc = Env.ZERO;
					} else {
						// 2023-05-25
						if (Gain.getP_UOM_ID() == 101) {
							quantityCalc = Env.ZERO;

						} else {
							quantityCalc = amountCalc;

						}

					}

				}
				break;

			case 121: // Indemnity Statutory Holidays / indemnite pour les conges feries
				// PaymentGain.setHourly_Rate( Env.ZERO );
				// || PaymentGain.getOriginTime().equals("HOR")
				/*
				 * if ( PaymentGain.getOriginTime().equals("FER") )
				 * {
				 * 
				 * ArrayList<BigDecimal> admissiblevalue =
				 * Employee.getIndemnityStatutoryHolidays( Period.getStartDate(),
				 * PaymentGain.getDay(), AssignmentParam.getP_Assignment_Param_ID(),
				 * Gain.getP_Column_Adm_ID(), GainParameter.getMultiplyRate(), getTrxName() );
				 * 
				 * quantityCalc = (BigDecimal)admissiblevalue.get(0);
				 * amountCalc = (BigDecimal)admissiblevalue.get(1);
				 * 
				 * if (amountCalc.compareTo(new BigDecimal(0)) > 0)
				 * {
				 * PaymentGain.setHourly_Rate(amountCalc.divide(quantityCalc));
				 * }
				 * else
				 * {
				 * amountCalc = quantityCalc.multiply( PaymentGain.getHourly_Rate().multiply(
				 * multiplyRate ) ).setScale(2,BigDecimal.ROUND_HALF_UP);
				 * }
				 * }
				 * else
				 * {
				 */
				quantityCalc = PaymentGain.getQuantity();
				amountCalc = quantityCalc.multiply(PaymentGain.getHourly_Rate().multiply(multiplyRate)).setScale(2,
						BigDecimal.ROUND_HALF_UP);
				// }
				break;

			/*
			 * case 119:
			 * P_Frequency Frequency = P_Frequency.get( Env.getCtx(),
			 * Period.getP_Frequency_ID(), m_trxName);
			 * BigDecimal nbrPeriod = new BigDecimal( Frequency.getNumberOfPeriod() );
			 * 
			 * P_Payment_Group PaymentGroup =
			 * P_Payment_Group.get(Env.getCtx(),Payment.getP_Payment_Group_ID(),m_trxName);
			 * 
			 * BigDecimal fctAnnual = PaymentGroup.getAnnual_Increase();
			 * 
			 * amountAdmissible = getGainAdmissible( Payment, Gain,
			 * PaymentGain.getStartDate(), PaymentGain.getEndDate(),
			 * PaymentGain.getP_Assignment_ID() );
			 * quantityAdmissible = getQuantityAdmissible( Payment, Gain,
			 * PaymentGain.getStartDate(), PaymentGain.getEndDate(),
			 * PaymentGain.getP_Assignment_ID() );
			 * quantityCalc = AssignmentParam.getHoursPerPay().multiply(nbrPeriod) ; //
			 * amountAdmissible.multiply( PaymentGain.getMultiplyRate()); //
			 * Gain.getMultiplyRate()
			 * quantityCalc = quantityCalc.divide(fctAnnual);
			 * quantityCalc = quantityCalc.divide(new BigDecimal(5));
			 * amountCalc = quantityCalc.multiply(AssignmentParam.getHourly_Rate());
			 * 
			 * break;
			 */
		}

		/*
		 * if ( PaymentGain.getSalaryPercentage() != null &&
		 * PaymentGain.getSalaryPercentage().compareTo( ZERO) != 0 )
		 * {
		 * amountCalc = amountCalc.multiply( PaymentGain.getSalaryPercentage().divide(
		 * new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ).add( Env.ONE ) );
		 * }
		 */
		amountCalc = amountCalc.setScale(2, BigDecimal.ROUND_HALF_UP);

		PaymentGain.setQuantityCalc(quantityCalc);
		PaymentGain.setAmountCalc(amountCalc);

		PaymentGain.save();

		//
		// Creation automatique d'une deduction pour l'employe si la deduction n'existe
		// pas ou que le montant est different.
		//
		if (Gain.getP_Deduction_ID() != 0 && amountCalc.compareTo(Env.ZERO) != 0) {
			P_Employee_Deduction EmployeeDeduction = P_Employee_Deduction.get(Env.getCtx(), Payment.getP_Employee_ID(),
					Gain.getP_Deduction_ID(), Period.getStartDate(), m_trxName);
			if (EmployeeDeduction.getP_Employee_Deduction_ID() <= 0) {
				EmployeeDeduction = new P_Employee_Deduction(Env.getCtx(), -1, m_trxName);
				EmployeeDeduction.setP_Deduction_ID(Gain.getP_Deduction_ID());
				EmployeeDeduction.setIsActive(true);
				EmployeeDeduction.setP_Employee_ID(Payment.getP_Employee_ID());
				EmployeeDeduction.setEffectIn(Period.getStartDate());
				// EmployeeDeduction.setEmployeeAmount( amountCalc );
				EmployeeDeduction.save();
			} else if (!EmployeeDeduction.isActive()) {
				EmployeeDeduction = new P_Employee_Deduction(Env.getCtx(), -1, m_trxName);
				EmployeeDeduction.setP_Deduction_ID(Gain.getP_Deduction_ID());
				EmployeeDeduction.setIsActive(true);
				EmployeeDeduction.setP_Employee_ID(Payment.getP_Employee_ID());
				EmployeeDeduction.setEffectIn(Period.getStartDate());
				// EmployeeDeduction.setEmployeeAmount( amountCalc );
				EmployeeDeduction.save();
			}

			// 2009.07.08 change Employee_Deduction_Excep by Payment_Deduction_Excep
			P_Payment_Deduction_Excep DeductionExcep = P_Payment_Deduction_Excep.get(Env.getCtx(),
					PaymentGain.getP_Payment_ID(), EmployeeDeduction.getP_Employee_Deduction_ID(),
					PaymentGain.getP_Period_ID(), m_trxName);
			if (DeductionExcep == null) {
				DeductionExcep = new P_Payment_Deduction_Excep(Env.getCtx(), -1, m_trxName);
			}

			DeductionExcep.setP_Employee_Deduction_ID(EmployeeDeduction.getP_Employee_Deduction_ID());

			if (DeductionExcep.getExceptionAmountEmployee() != null)
				DeductionExcep.setExceptionAmountEmployee(DeductionExcep.getExceptionAmountEmployee().add(amountCalc));
			else {
				DeductionExcep.setExceptionAmountEmployee(amountCalc);
			}

			DeductionExcep.setEmployeeAssessment(P_LongTermLeave_Deduction.EMPLOYEEASSESSMENT_Relieved);
			DeductionExcep.setEmployerAssessment(P_LongTermLeave_Deduction.EMPLOYERASSESSMENT_No);
			// DeductionExcep.setExceptionAmountEmployee(amountCalc );
			DeductionExcep.setP_Payment_ID(Payment.getP_Payment_ID());
			DeductionExcep.setExceptionAmountEmployer(Env.ZERO);
			// DeductionExcep.setP_Deduction_ID( Gain.getP_Deduction_ID());
			// DeductionExcep.setP_Employee_ID(Payment.getP_Employee_ID());
			DeductionExcep.setP_Period_ID(PaymentGain.getP_Period_ID());
			DeductionExcep.setIsActive(true);
			DeductionExcep.save();
		}

	}

	private BigDecimal get_gain_cumulative(int P_Gain_ID, int P_Period_ID) throws Exception {
		String query = null;
		query = "Select isnull( sum( AmountCalc ),0) AmountCalc "
				+ " from P_Payment_Gain, P_Payment"
				+ " where P_Payment_Gain.P_Gain_ID = " + P_Gain_ID
				+ " and P_Payment.P_Year_ID = " + this.Period.getP_Year_ID()
				+ " and P_Payment_Gain.P_Period_ID = " + P_Period_ID
				+ " and P_Payment.P_Employee_ID = " + this.Payment.getP_Employee_ID()
				+ " and P_Payment_Gain.P_Payment_ID = P_Payment.P_Payment_ID ";

		BigDecimal Amount = Env.ZERO;
		// Log.trace( 5, EmployeeDeduction.getValue() + " - " +
		// EmployeeDeduction.getName() + " Query " + query ) ;

		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement(query, this.getTrxName());
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			Amount = rs.getBigDecimal("AmountCalc");
		}
		rs.close();
		pstmt.close();
		pstmt = null;

		return Amount;
	}

	private boolean exists_time(P_Payment Payment, Timestamp Day) throws Exception {

		if (Env.getAD_Client_ID(Env.getCtx()) == 11)
			return true;

		boolean isFound = false;

		String sql = null;
		sql = "Select Top 1 P_Payment_Gain_ID From P_Payment_Gain "
				+ " WHERE P_Payment_ID = " + Payment.getP_Payment_ID()
				+ "  AND P_Payment_Gain.Day = " + DB.TO_DATE(Day);

		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement(sql, getTrxName());
		ResultSet rs = pstmt.executeQuery();

		if (rs.next()) {
			isFound = true;
		}
		rs.close();
		pstmt.close();
		pstmt = null;

		return isFound;

	}

	// 2009.01.23
	// Validation que la coupe negative pour les vacances et la maladie a bien ete
	// effectuer
	//
	private void valide_negative_cut() throws Exception {
		String sql = null;
		sql = "	select p_employee_id from p_time_sheet  "
				+ " where   exists( select 1 from p_gain_join, p_time_sheet_detail where p_time_sheet_detail.p_time_sheet_id = p_time_sheet.p_time_sheet_id and isnegative = 'Y' and P_Gain_join.p_gain_id = p_time_sheet_detail.p_gain_id )   "
				+ " and not exists( select 1 from p_gain_join, p_time_sheet_detail where p_time_sheet_detail.p_time_sheet_id = p_time_sheet.p_time_sheet_id and isnegative = 'Y' and p_gain_join.p_gain_id_affected = p_time_sheet_detail.p_gain_id )   "
				+ " and p_time_sheet.p_time_sheet_id =  " + TimeSheet.getP_Time_Sheet_ID();

		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement(sql, getTrxName());
		ResultSet rs = pstmt.executeQuery();

		if (rs.next()) {
			P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error,
					"Il n'a pas ete possible de couper negativement un code de gain", getTrxName());
		}
		rs.close();
		pstmt.close();
		pstmt = null;

	}

	private void calcul_gain(P_Payment Payment, Timestamp Day) throws Exception {

		String sql = null;
		sql = "Select P_Payment_Gain_ID From P_Payment_Gain, P_Gain "
				+ " WHERE P_Payment_ID = " + Payment.getP_Payment_ID()
				+ "  AND P_Payment_Gain.Day = " + DB.TO_DATE(Day)
				+ "  AND P_Gain.P_Gain_ID = P_Payment_Gain.P_Gain_ID "
				+ "  AND P_Method_Gain_ID NOT IN (107, 113, 116, 300)";

		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement(sql, getTrxName());
		ResultSet rs = pstmt.executeQuery();

		P_Payment_Gain PaymentGain;

		while (rs.next()) {
			PaymentGain = P_Payment_Gain.get(Env.getCtx(), rs.getInt("P_Payment_Gain_ID"), getTrxName());
			calcul_gain(PaymentGain);

			// log.log (Level.INFO,"Validation - Calcul Credits 2" );
			// Calcul_Credits calCredits = new Calcul_Credits( TimeSheet, getTrxName() );
			// calCredits.CreditsEvaluationTransaction( PaymentGain, PaymentGain.getDay() );
		}
		rs.close();
		pstmt.close();
		pstmt = null;

		//
		// Calcul les gain en % sur d'autre gain se fait suite au calcul de tout les
		// gains
		// et seulement une fois par periode.
		//
		sql = "Select P_Payment_Gain.P_Gain_ID, P_Payment_Gain_ID P_Payment_Gain_ID, StartDate, EndDate"
				+ " From P_Payment_Gain, P_Gain "
				+ " WHERE P_Payment_ID = " + Payment.getP_Payment_ID()
				// + " AND P_Payment_Gain.EndDate = " + DB.TO_DATE( Day )
				+ "  AND  P_Payment_Gain.P_Gain_ID = P_Gain.P_Gain_ID "
				+ "  AND P_Method_Gain_ID IN ( 107, 113, 116, 300 )"
				+ " ORDER BY P_Gain.Value ";

		pstmt = DB.prepareStatement(sql, getTrxName());
		rs = pstmt.executeQuery();

		while (rs.next()) {
			PaymentGain = P_Payment_Gain.get(Env.getCtx(), rs.getInt("P_Payment_Gain_ID"), getTrxName());
			calcul_gain(PaymentGain);
		}
		rs.close();
		pstmt.close();
		pstmt = null;

		return;

	} //

	private void Generate_LongTermLeave_Deduction(P_Payment_Gain PaymentGain) throws Exception {
		P_Employee_LongTermLeave EmployeeLongTermLeave = P_Employee_LongTermLeave.get(Env.getCtx(),
				PaymentGain.getP_Employee_LongTermLeave_ID(), m_trxName);

		if (EmployeeLongTermLeave.getStartDate() == null) {
			EmployeeLongTermLeave.setStartDate(Period.getStartDate());
		}

		int nbrDays = TimeUtil.getDaysBetween(EmployeeLongTermLeave.getStartDate(), PaymentGain.getDay()) + 1;

		String sql = "SELECT P_LongTermLeave_Deduction_ID FROM P_LongTermLeave_Deduction "
				+ " WHERE P_LongTermLeave_ID = " + EmployeeLongTermLeave.getP_LongTermLeave_ID()
				+ " AND Day <= " + nbrDays
				+ " ORDER BY Day DESC ";

		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement(sql, getTrxName());
		ResultSet rs = pstmt.executeQuery();

		P_LongTermLeave_Deduction LongTermLeave_Deduction;
		P_Employee_Deduction EmployeeDeduction;
		P_Payment_Deduction_Excep DeductionExcep;

		while (rs.next()) {
			LongTermLeave_Deduction = P_LongTermLeave_Deduction.get(Env.getCtx(),
					rs.getInt("P_LongTermLeave_Deduction_ID"), m_trxName);
			EmployeeDeduction = P_Employee_Deduction.get(Env.getCtx(), EmployeeLongTermLeave.getP_Employee_ID(),
					LongTermLeave_Deduction.getP_Deduction_ID(), Period.getStartDate(), m_trxName);
			// Si l'employe a la deduction en question.
			if (EmployeeDeduction.getP_Employee_Deduction_ID() != 0) {
				// P_Deduction Deduction = P_Deduction.get( Env.getCtx(),
				// LongTermLeave_Deduction.getP_Deduction_ID(), m_trxName);
				// P_Deduction_Param DeductionParam = P_Deduction_Param.get( Env.getCtx(),
				// LongTermLeave_Deduction.getP_Deduction_ID(), Period.getStartDate(),
				// m_trxName);

				DeductionExcep = P_Payment_Deduction_Excep.get(Env.getCtx(), Payment.getP_Payment_ID(),
						EmployeeDeduction.getP_Employee_Deduction_ID(), Payment.getP_Period_ID(), m_trxName);

				// if newrecord.
				if (DeductionExcep == null) {
					DeductionExcep = new P_Payment_Deduction_Excep(Env.getCtx(), -1, m_trxName);
					DeductionExcep.setP_Employee_Deduction_ID(EmployeeDeduction.getP_Employee_Deduction_ID());
					DeductionExcep.setP_Payment_ID(Payment.getP_Payment_ID());
					DeductionExcep.setEmployeeAssessment(LongTermLeave_Deduction.getEmployeeAssessment());
					DeductionExcep.setEmployerAssessment(LongTermLeave_Deduction.getEmployerAssessment());
					DeductionExcep.setP_Period_ID(PaymentGain.getP_Period_ID());

					if (LongTermLeave_Deduction.getEmployeeAssessment()
							.equals(P_LongTermLeave_Deduction.EMPLOYEEASSESSMENT_No) &&
							LongTermLeave_Deduction.getEmployerAssessment()
									.equals(P_LongTermLeave_Deduction.EMPLOYERASSESSMENT_No)) {
						DeductionExcep.setIsActive(false);
					}

					DeductionExcep.save();

				}

			}
		}
		rs.close();
		pstmt.close();
		pstmt = null;

	}

	private BigDecimal getGainAdmissible(P_Payment Payment, P_Gain Gain, Timestamp StartDate, Timestamp EndDate,
			int P_Assignment_ID) throws Exception {
		BigDecimal Amount = Env.ZERO;
		String sql = "SELECT P_Gain_ID, P_Assignment_ID, isnull( SUM( AmountCalc ), 0) AmountCalc, isnull( SUM( QuantityCalc ), 0) QuantityCalc "
				+ " FROM P_Payment_Gain, P_Gain_Column "
				+ " WHERE P_Payment_Gain.P_Payment_ID = ? "
				+ " AND P_Payment_Gain.P_Gain_Parameter_ID = P_Gain_Column.P_Gain_Parameter_ID "
				+ " AND P_Column_Adm_ID = ? "
				+ " AND [Day] Between " + DB.TO_DATE(StartDate) + " AND " + DB.TO_DATE(EndDate)
				+ " AND IsAdmissible = 'Y' "
				+ " AND P_Assignment_ID = " + P_Assignment_ID
				+ " GROUP BY P_Gain_ID, P_Assignment_ID ";
		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement(sql, getTrxName());
		pstmt.setInt(1, Payment.getP_Payment_ID());
		pstmt.setInt(2, Gain.getP_Column_Adm_ID());

		ResultSet rs = pstmt.executeQuery();
		P_Gain GainAdm;
		P_Assignment Assignment;
		P_Assignment_Param AssignmentParam;

		while (rs.next()) {
			Amount = Amount.add(rs.getBigDecimal("AmountCalc"));
			//
			// Permet d'ajouter les Gain d'heures non-remuneres
			// admissibles. On calcul un montant admissible
			// e partir des heures de la feuilles de temps.
			//
			// HNR P_Method_Gain_ID = 104
			// CARRA P_Column_Adm_ID = 104

			GainAdm = P_Gain.get(Env.getCtx(), rs.getInt("P_Gain_ID"), getTrxName());
			if (GainAdm.getP_Method_Gain_ID() == 104) {
				Assignment = P_Assignment.get(Env.getCtx(), rs.getInt("P_Assignment_ID"), getTrxName());
				AssignmentParam = P_Assignment_Param.get(Env.getCtx(), Assignment.getP_Assignment_ID(), EndDate,
						getTrxName());

				if (AssignmentParam != null)
					Amount = Amount.add((rs.getBigDecimal("QuantityCalc").multiply(AssignmentParam.getHourly_Rate()))
							.setScale(2, BigDecimal.ROUND_HALF_UP));
				else {
					P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error,
							"L'employe " + Employee.getValue() + " n'a pas de parametre d'affectation ("
									+ Assignment.getValue() + ") en date du " + StartDate.toString() + " gain : "
									+ GainAdm.getValue(),
							getTrxName());
				}
			}

		}
		rs.close();
		pstmt.close();
		pstmt = null;

		return Amount;

	}

	private BigDecimal getQuantityAdmissible(P_Payment Payment, P_Gain Gain, Timestamp StartDate, Timestamp EndDate,
			int P_Assignment_ID, int P_Schedule_ID) throws Exception {
		BigDecimal Quantity = Env.ZERO;
		String sql = "SELECT P_Gain_ID, P_Assignment_ID, isnull( SUM( AmountCalc ), 0) AmountCalc, isnull( SUM( QuantityCalc ), 0) QuantityCalc "
				+ " FROM P_Payment_Gain, P_Gain_Column "
				+ " WHERE P_Payment_Gain.P_Payment_ID = ? "
				+ " AND P_Payment_Gain.P_Gain_Parameter_ID = P_Gain_Column.P_Gain_Parameter_ID "
				+ " AND P_Column_Adm_ID = ? "
				+ " AND [Day] Between " + DB.TO_DATE(StartDate) + " AND " + DB.TO_DATE(EndDate)
				+ " AND IsAdmissible = 'Y' "
		// + " AND P_Assignment_ID = " + P_Assignment_ID
		;

		if (P_Schedule_ID != 100000)
			sql = sql + " AND( P_Payment_Gain.P_Schedule_ID = " + P_Schedule_ID + " )";

		sql = sql + " GROUP BY P_Gain_ID, P_Assignment_ID ";
		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement(sql, getTrxName());
		pstmt.setInt(1, Payment.getP_Payment_ID());
		pstmt.setInt(2, Gain.getP_Column_Adm_ID());

		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			Quantity = Quantity.add(rs.getBigDecimal("QuantityCalc"));
		}
		rs.close();
		pstmt.close();
		pstmt = null;

		return Quantity;

	}

	public void Create_PaymentGainDistribution(P_Payment payment, int P_Payment_Gain_ID) throws Exception {
		P_Gain_Account GainAccount = null;

		// 1 - Aller voir si ya une unite administrative inscrite sur la
		// feuille de temps
		// 2 - aller chercher la ventilation sur l'Asssignation
		// 3 - si inexistante aller chercher la ventilation sur le poste
		// 4 - pour chaque ligne de ventilation, si pas de compte aller
		// le chercher dans le gain

		P_Workplace Workplace = P_Workplace.get(Env.getCtx(), Payment.getP_Workplace_ID(), getTrxName());

		P_Assignment_Distribution assignment_Distro = null;
		P_Post_Distribution post_Distro = null;
		// P_Gain_Account gain_Account = null;
		boolean weHaveDistro = false;

		BigDecimal AmountCalc = Env.ZERO;
		BigDecimal QuantityCalc = Env.ZERO;
		BigDecimal TotAmountCalc = Env.ZERO;
		BigDecimal TotQuantityCalc = Env.ZERO;

		String sql = null;
		// sql = "Select P_Payment_Gain_ID, AmountCalc, QuantityCalc, P_Assignment_ID,
		// P_Post_ID, P_Payment_Gain.C_Activity_ID, P_Payment_Gain.P_Gain_ID, Day "
		// sql = "Select P_Payment_Gain_ID, P_Payment_Gain.AmountCalc,
		// P_Payment_Gain.QuantityCalc, P_Payment_Gain.P_Assignment_ID,
		// P_Payment_Gain.P_Post_ID, P_Payment_Gain.P_Gain_ID, Day,
		// P_Payment_Gain.C_Activity_ID, P_Payment_Gain.AD_Org_ID,
		// P_Assignment.P_Workplace_ID "
		sql = "Select P_Payment_Gain_ID, P_Payment_Gain.AmountCalc, P_Payment_Gain.QuantityCalc, P_Payment_Gain.P_Assignment_ID, P_Payment_Gain.P_Post_ID,  P_Payment_Gain.P_Gain_ID, Day, P_Payment_Gain.C_Activity_ID, P_Payment_Gain.AD_Org_ID, P_Post.P_Workplace_ID "
				+ " From P_Payment_Gain "
				// 2014.04.15
				+ " LEFT OUTER JOIN P_Assignment on P_Assignment.P_Assignment_ID = P_Payment_Gain.P_Assignment_ID "
				// 2023-08-22
				+ " LEFT OUTER JOIN P_POST on P_Post.P_Post_ID = P_Assignment.P_Post_ID "

				+ " WHERE P_Payment_ID = " + Payment.getP_Payment_ID()
				+ "  AND AmountCalc <> 0 ";

		// + " LEFT OUTER JOIN P_Gain_Account ON ( P_Payment_Gain.p_Gain_id =
		// P_Gain_Account.p_Gain_id and P_Gain_Account.P_Job_Title_ID =
		// P_Payment_Gain.P_Job_Title_ID AND P_Gain_Account.P_Job_Type_ID =" +
		// Payment.getP_Job_Type_ID() + " ) "

		if (P_Payment_Gain_ID != 0)
			sql += " AND P_Payment_Gain_ID = " + P_Payment_Gain_ID;

		PreparedStatement pstmt_Payment = null;
		pstmt_Payment = DB.prepareStatement(sql, getTrxName());
		ResultSet rs_Payment = pstmt_Payment.executeQuery();

		int line = 0;

		P_Payment_Gain_Distribution paymentGainDistribution = null;
		while (rs_Payment.next()) {

			// 2014.04.15 modification pour HCL car la comtablite est par lieu de travail.
			if (rs_Payment.getInt("P_Workplace_ID") != 0) {
				Workplace = P_Workplace.get(Env.getCtx(), rs_Payment.getInt("P_Workplace_ID"), null);
				GainAccount = P_Gain_Account.getWithSalesRegion(Env.getCtx(), Payment.getAD_Client_ID(),
						Payment.getAD_Org_ID(), Workplace.getC_SalesRegion_ID(), rs_Payment.getInt("P_Gain_ID"), null);
			}

			/*
			 * if ( Payment.getAD_Client_ID() == 11 )
			 * GainAccount = P_Gain_Account.get(Env.getCtx(), Payment.getAD_Client_ID(),
			 * Payment.getAD_Org_ID(), rs_Payment.getInt( "P_Gain_ID"),
			 * Payment.getP_Occupation_Group_ID(), Payment.getP_Job_Type_ID(),
			 * Payment.getP_Job_Title_ID(), null);
			 * else
			 * {
			 * // if ( rs_Payment.getInt( "C_Activity_ID") != 0 )
			 * // GainAccount = P_Gain_Account.get(Env.getCtx(), Payment.getAD_Client_ID(),
			 * Payment.getAD_Org_ID(), rs_Payment.getInt( "C_Activity_ID"),
			 * rs_Payment.getInt( "P_Gain_ID"), null);
			 * // else
			 * P_Post Post = P_Post.get(Env.getCtx(), rs_Payment.getInt("P_Post_ID") ,
			 * null);
			 * Workplace = P_Workplace.get( Env.getCtx(), Post.getP_Workplace_ID(), null );
			 * GainAccount = P_Gain_Account.getWithSalesRegion(Env.getCtx(),
			 * Payment.getAD_Client_ID(), Payment.getAD_Org_ID(),
			 * Workplace.getC_SalesRegion_ID() , rs_Payment.getInt( "P_Gain_ID"), null);
			 * 
			 * 
			 * //inAccount = P_Gain_Account.get(Env.getCtx(), Payment.getAD_Client_ID(),
			 * Payment.getAD_Org_ID(), Payment.getC_Activity_ID(), rs_Payment.getInt(
			 * "P_Gain_ID"), null);
			 * //
			 * }
			 */
			// primo, on va chercher le gain-account, il risque de servir dans la
			// ventilation
			if (GainAccount != null) {
				AmountCalc = rs_Payment.getBigDecimal("AmountCalc");
				QuantityCalc = rs_Payment.getBigDecimal("QuantityCalc");
				TotAmountCalc = rs_Payment.getBigDecimal("AmountCalc");
				TotQuantityCalc = rs_Payment.getBigDecimal("QuantityCalc");
				line = line + 10;

				weHaveDistro = true;

				// Etape 1, verifier sil y a une unite administrative de saisie
				// sur la feuille de temps
				// si oui, on ventile sur cette unite et on prend le compte du gain

				if (rs_Payment.getInt("C_Activity_ID") != 0 || GainAccount.getC_Activity_ID() != 0) {
					int ActivityID = 0;
					if (rs_Payment.getInt("C_Activity_ID") != 0)
						ActivityID = rs_Payment.getInt("C_Activity_ID");
					else
						ActivityID = GainAccount.getC_Activity_ID();

					// on cree la nouvelle ligne d'imputation de paiement

					paymentGainDistribution = new P_Payment_Gain_Distribution(Env.getCtx(), -1, getTrxName());
					paymentGainDistribution.setC_Activity_ID(ActivityID);

					if (GainAccount.getC_SalesRegion_ID() != 0)
						paymentGainDistribution.setC_SalesRegion_ID(GainAccount.getC_SalesRegion_ID());
					else
						paymentGainDistribution.setC_SalesRegion_ID(Workplace.getC_SalesRegion_ID());
					paymentGainDistribution.setOrg_ID(Payment.getAD_Org_ID());
					paymentGainDistribution.setAD_Org_ID(Payment.getAD_Org_ID());

					paymentGainDistribution.setP_Payment_Gain_ID(rs_Payment.getInt("P_Payment_Gain_ID"));
					paymentGainDistribution.setLine(new BigDecimal(line));

					paymentGainDistribution.setAccount_ID(GainAccount.getP_Gain_Acct());

					paymentGainDistribution.setDistribution_Type(P_Payment_Gain_Distribution.DISTRIBUTION_TYPE_S);
					paymentGainDistribution.setC_SalesRegion_ID(GainAccount.getC_SalesRegion_ID());
					paymentGainDistribution.setAmount(AmountCalc);
					paymentGainDistribution.setQuantity(QuantityCalc);

					paymentGainDistribution.save();

				} else// donc on a pas d'unite administrative sur la feuille de temps
				{
					// Etape 2, verifier si on peut ventiler sur l'Assignation
					weHaveDistro = Distribution_Assignment(rs_Payment, GainAccount);
					// Etape 3, on verifie si on a eu une ventilation sur l'Assignation
					// si oui, on continue, si non, on ventile sur le poste

					if (weHaveDistro == false) {
						weHaveDistro = Distribution_Post(rs_Payment, GainAccount, rs_Payment.getTimestamp("Day"));
					}

					// AJOUT FRANCIS FORTIER 2006-10-25 (magnetho ref. #436)
					// Si on n'a toujours pas trouve de distribution, on refait la recherche,
					// mais cette fois ci, on utilise la date courante.
					if (weHaveDistro == false) {
						Timestamp searchDate = new Timestamp(GregorianCalendar.getInstance().getTimeInMillis());
						weHaveDistro = Distribution_Post(rs_Payment, GainAccount, searchDate);

						// Puisqu'on a utilise la date du jour, on doit ajouter un avertissement
						// e la feuille de temps (s'il y a lieu)
						if (weHaveDistro) {
							String message = "Une transaction anterieure e la date du jour utilise une distribution comptable active en date d'aujourd'hui.";
							P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Warning,
									message, getTrxName());
						}
					}
				}

				if (!weHaveDistro) {
					String message = "La distribution comptable sous l'affectation ou le poste est obligatoire.";
					// String message = Msg.getMsg(Env.getCtx(), "AssignmentDistributionRequired");
					P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, message,
							getTrxName());
				}
			} else {
				P_Gain Gain = P_Gain.get(Env.getCtx(), rs_Payment.getInt("P_Gain_ID"), m_trxName);
				String message = Msg.getMsg(Env.getCtx(), "GainAccountRequired") + " " + Gain.getValue();
				P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, message,
						getTrxName());
				return;
			}

			// rs.close ();
			// pstmt.close ();
			// pstmt = null;

		}
		rs_Payment.close();
		pstmt_Payment.close();
		pstmt_Payment = null;

	}

	private boolean Distribution_Assignment(ResultSet rs_Payment, P_Gain_Account GainAccount) throws SQLException {

		BigDecimal AmountCalc = Env.ZERO;
		BigDecimal QuantityCalc = Env.ZERO;
		BigDecimal TotAmountCalc = Env.ZERO;
		BigDecimal TotQuantityCalc = Env.ZERO;

		boolean weHaveDistro = false;
		int line = 0;

		// 2014.04.17 performance
		// String sqlAssDis = "SELECT P_Assignment_Distribution_ID "
		String sqlAssDis = "SELECT * "
				+ " FROM P_Assignment_Distribution "
				+ "   WHERE P_Assignment_Distribution.IsActive = 'Y' "
				+ "   AND P_Assignment_ID = " + rs_Payment.getInt("P_Assignment_ID");

		PreparedStatement pstmt = DB.prepareStatement(sqlAssDis, getTrxName());
		ResultSet rs = pstmt.executeQuery();

		P_Assignment_Distribution Distribution;
		P_Payment_Gain_Distribution paymentGainDistribution = null;

		while (rs.next()) {
			weHaveDistro = true;
			line = line + 10;

			Distribution = new P_Assignment_Distribution(Env.getCtx(), rs, getTrxName());

			// on verifie si la distribution de l'Assignation, pour cette ligne
			// possede un compte
			int iAccount = -1;
			// si id == 0, pas de records
			// donc, si<0 pas de compte au niveau de l'Assignation, donc on prend le compte
			// du code de gain
			if (Distribution.getAccount_ID() < 1) {
				iAccount = GainAccount.getP_Gain_Acct();
			} else {
				iAccount = Distribution.getAccount_ID();
			}

			// on cree la nouvelle ligne d'imputation de paiement

			paymentGainDistribution = new P_Payment_Gain_Distribution(Env.getCtx(), -1, getTrxName());
			paymentGainDistribution.setC_Activity_ID(Distribution.getC_Activity_ID());

			if (GainAccount.getC_SalesRegion_ID() != 0)
				paymentGainDistribution.setC_SalesRegion_ID(GainAccount.getC_SalesRegion_ID());
			else {
				paymentGainDistribution.setC_SalesRegion_ID(Distribution.getC_SalesRegion_ID());

			}
			paymentGainDistribution.setC_SalesRegion_ID(Distribution.getC_SalesRegion_ID());

			paymentGainDistribution.setOrg_ID(Payment.getAD_Org_ID());
			paymentGainDistribution.setAD_Org_ID(Payment.getAD_Org_ID());

			paymentGainDistribution.setP_Payment_Gain_ID(rs_Payment.getInt("P_Payment_Gain_ID"));
			paymentGainDistribution.setLine(new BigDecimal(line));
			paymentGainDistribution.setAccount_ID(iAccount);

			BigDecimal rate = Distribution.getRatio().divide(new BigDecimal(100), 8, BigDecimal.ROUND_HALF_UP);
			if (rate == null || rate.compareTo(new BigDecimal(0)) == 0) {
				rate = new BigDecimal(1);
			}

			AmountCalc = rs_Payment.getBigDecimal("AmountCalc").multiply(rate);
			QuantityCalc = rs_Payment.getBigDecimal("QuantityCalc").multiply(rate);

			AmountCalc = AmountCalc.setScale(2, BigDecimal.ROUND_HALF_UP);
			QuantityCalc = QuantityCalc.setScale(2, BigDecimal.ROUND_HALF_UP);

			TotAmountCalc = TotAmountCalc.subtract(AmountCalc);
			TotQuantityCalc = TotQuantityCalc.subtract(QuantityCalc);

			paymentGainDistribution.setDistribution_Type(P_Payment_Gain_Distribution.DISTRIBUTION_TYPE_S);

			paymentGainDistribution.setAmount(AmountCalc);
			paymentGainDistribution.setQuantity(QuantityCalc);
			// if (paymentGainDistribution.getAmount().compareTo(Env.ZERO) != 0 )
			// {
			paymentGainDistribution.save();
			// }

		}
		rs.close();
		pstmt.close();
		pstmt = null;

		return weHaveDistro;

	}

	private boolean Distribution_Post(ResultSet rs_Payment, P_Gain_Account GainAccount, Timestamp searchDate)
			throws SQLException {

		BigDecimal AmountCalc = Env.ZERO;
		BigDecimal QuantityCalc = Env.ZERO;
		BigDecimal TotAmountCalc = Env.ZERO;
		BigDecimal TotQuantityCalc = Env.ZERO;

		boolean weHaveDistro = false;
		int line = 0;

		// 2014.04.17 performance
		// String strPostAcct = "SELECT P_Post_Distribution_ID "
		String strPostAcct = "SELECT * "
				+ " FROM P_Post_Distribution "
				+ " WHERE P_Post_ID=" + rs_Payment.getInt("P_Post_ID")
				+ " AND EffectIn = (Select Max( EffectIn ) FROM P_Post_Distribution WHERE IsActive = 'Y' and P_Post_ID ="
				+ rs_Payment.getInt("P_Post_ID") + " AND Effectin <=" + DB.TO_DATE(searchDate) + ")"
				+ " AND IsActive = 'Y'";

		PreparedStatement pstmt = DB.prepareStatement(strPostAcct, getTrxName());
		ResultSet rs = pstmt.executeQuery();

		P_Post_Distribution Distribution;
		P_Payment_Gain_Distribution paymentGainDistribution = null;

		while (rs.next()) {
			weHaveDistro = true;
			line = line + 10;

			// Distribution = P_Post_Distribution.get( Env.getCtx(), rs.getInt(1),
			// getTrxName() );
			Distribution = new P_Post_Distribution(Env.getCtx(), rs, getTrxName());

			// on verifie si la distribution de l'Assignation, pour cette ligne
			// possede un compte
			int iAccount = -1;
			// si id == 0, pas de records
			// donc, si<0 pas de compte au niveau de l'Assignation, donc on prend le compte
			// du code de gain
			if (Distribution.getAccount_ID() < 1) {
				iAccount = GainAccount.getP_Gain_Acct();
			} else {
				iAccount = Distribution.getAccount_ID();
			}

			// on cree la nouvelle ligne d'imputation de paiement

			paymentGainDistribution = new P_Payment_Gain_Distribution(Env.getCtx(), -1, getTrxName());
			paymentGainDistribution.setC_Activity_ID(Distribution.getC_Activity_ID());

			if (GainAccount.getC_SalesRegion_ID() != 0)
				paymentGainDistribution.setC_SalesRegion_ID(GainAccount.getC_SalesRegion_ID());
			else
				paymentGainDistribution.setC_SalesRegion_ID(Distribution.getC_SalesRegion_ID());

			paymentGainDistribution.setOrg_ID(Payment.getAD_Org_ID());
			paymentGainDistribution.setAD_Org_ID(Payment.getAD_Org_ID());

			paymentGainDistribution.setP_Payment_Gain_ID(rs_Payment.getInt("P_Payment_Gain_ID"));
			paymentGainDistribution.setLine(new BigDecimal(line));
			paymentGainDistribution.setAccount_ID(iAccount);

			BigDecimal rate = Distribution.getRatio().divide(new BigDecimal(100), 8, BigDecimal.ROUND_HALF_UP);
			if (rate == null || rate.compareTo(new BigDecimal(0)) == 0) {
				rate = new BigDecimal(1);
			}

			if (rs_Payment.getBigDecimal("AmountCalc") != null)
				AmountCalc = rs_Payment.getBigDecimal("AmountCalc").multiply(rate);

			if (rs_Payment.getBigDecimal("QuantityCalc") != null)
				QuantityCalc = rs_Payment.getBigDecimal("QuantityCalc").multiply(rate);

			AmountCalc = AmountCalc.setScale(2, BigDecimal.ROUND_HALF_UP);
			QuantityCalc = QuantityCalc.setScale(2, BigDecimal.ROUND_HALF_UP);

			TotAmountCalc = TotAmountCalc.subtract(AmountCalc);
			TotQuantityCalc = TotQuantityCalc.subtract(QuantityCalc);

			paymentGainDistribution.setDistribution_Type(P_Payment_Gain_Distribution.DISTRIBUTION_TYPE_S);
			paymentGainDistribution.setAmount(AmountCalc);
			paymentGainDistribution.setQuantity(QuantityCalc);
			// if (paymentGainDistribution.getAmount().compareTo(Env.ZERO) != 0 )
			// {
			paymentGainDistribution.save();
			// }

		}
		rs.close();
		pstmt.close();
		pstmt = null;

		return weHaveDistro;

	}

	class PaymentDeduction {
		public int m_deductionId;
		public int m_periodId;
		public BigDecimal m_salaryEligible;
		public BigDecimal m_hoursEligible;
		public BigDecimal m_employeePart;
		public BigDecimal m_employerPart;
		public BigDecimal m_accumulationAmount;
		public BigDecimal m_retrievalAmount;

		public PaymentDeduction(
				int deductionId, int periodId, BigDecimal salaryEligible,
				BigDecimal hoursEligible, BigDecimal employeePart, BigDecimal employerPart,
				BigDecimal accumulationAmount, BigDecimal retrievalAmount) {
			m_deductionId = deductionId;
			m_periodId = periodId;
			m_salaryEligible = salaryEligible;
			m_hoursEligible = hoursEligible;
			m_employeePart = employeePart;
			m_employerPart = employerPart;
			m_accumulationAmount = accumulationAmount;
			m_retrievalAmount = retrievalAmount;
		}
	}

	class PaymentTaxableBenefit {
		public int m_taxableBenefitId;
		public int m_periodId;
		public BigDecimal m_taxableBenefitAmount;

		public PaymentTaxableBenefit(int taxableBenefitId, int periodId, BigDecimal taxableBenefitAmount) {
			m_taxableBenefitId = taxableBenefitId;
			m_periodId = periodId;
			m_taxableBenefitAmount = taxableBenefitAmount;
		}
	}

	// Recuperer les modifications faites par l'utilisateur pour les reappliquer e
	// la fin du calcul
	public void archiveModification(P_Time_Sheet timeSheet, List<Object> archives) {
		int paymentId = timeSheet.getP_Payment_ID();
		PreparedStatement statm = null;
		ResultSet rs = null;
		try {
			statm = DB.prepareStatement(
					"SELECT P_Deduction_ID, P_Period_ID, Salary_Eligible, Hours_Eligible, Employee_Part, Employer_Part, AccumulationAmount, RetrievalAmount "
							+
							"FROM P_Payment_Deduction " +
							"WHERE Origine = 'AJT' AND P_Payment_ID = ?",
					getTrxName());
			statm.setInt(1, paymentId);
			rs = statm.executeQuery();
			while (rs.next()) {
				archives.add(new PaymentDeduction(
						rs.getInt(1), rs.getInt(2), rs.getBigDecimal(3),
						rs.getBigDecimal(4), rs.getBigDecimal(5), rs.getBigDecimal(6),
						rs.getBigDecimal(7), rs.getBigDecimal(8)));
			}
			rs.close();
			statm.close();
		} catch (Exception e) {
			log.log(Level.SEVERE, "Archive PaymentDeduction", e);
		} finally {
			// DB.DB_CLOSE(statm);
			// DB.DB_CLOSE(rs);
		}

		try {
			statm = DB.prepareStatement(
					"SELECT P_Taxable_Benefit_ID, P_Period_ID, Taxable_Benefit_Amount " +
							"FROM P_Payment_Taxable_Benefit " +
							"WHERE Origine = 'AJT' AND P_Payment_ID = ?",
					getTrxName());
			statm.setInt(1, paymentId);
			rs = statm.executeQuery();
			while (rs.next()) {
				archives.add(new PaymentTaxableBenefit(
						rs.getInt(1), rs.getInt(2), rs.getBigDecimal(3)));
			}

			rs.close();
			statm.close();

		} catch (Exception e) {
			log.log(Level.SEVERE, "Archive PaymentTaxableBenefit", e);
		} finally {
			// DB.DB_CLOSE(statm);
			// DB.DB_CLOSE(rs);
		}
	}

	// Reappliquer les modifications de l'utilisateur
	public void appliqueModification(P_Time_Sheet timeSheet, List<Object> archives) {
		for (Iterator iter = archives.iterator(); iter.hasNext();) {
			Object obj = iter.next();
			if (obj instanceof PaymentDeduction) {
				PaymentDeduction apd = (PaymentDeduction) obj;
				P_Payment_Deduction pd = new P_Payment_Deduction(Env.getCtx(), -1, getTrxName());
				pd.setP_Employee_ID(timeSheet.getP_Employee_ID());
				pd.setP_Payment_ID(timeSheet.getP_Payment_ID());
				pd.setP_Deduction_ID(apd.m_deductionId);
				pd.setSalary_Eligible(apd.m_salaryEligible);
				pd.setHours_Eligible(apd.m_hoursEligible);
				pd.setEmployee_Part(apd.m_employeePart);
				pd.setEmployer_Part(apd.m_employerPart);
				pd.setAccumulationAmount(apd.m_accumulationAmount);
				pd.setRetrievalAmount(apd.m_retrievalAmount);
				pd.setP_Period_ID(apd.m_periodId);
				pd.setOrigine("AJT");
				pd.save();
			} else {
				PaymentTaxableBenefit ptb = (PaymentTaxableBenefit) obj;
				P_Payment_Taxable_Benefit tb = new P_Payment_Taxable_Benefit(Env.getCtx(), -1, getTrxName());
				tb.setP_Employee_ID(timeSheet.getP_Employee_ID());
				tb.setP_Payment_ID(timeSheet.getP_Payment_ID());
				tb.setP_Taxable_Benefit_ID(ptb.m_taxableBenefitId);
				tb.setP_Period_ID(ptb.m_periodId);
				tb.setTaxable_Benefit_Amount(ptb.m_taxableBenefitAmount);
				tb.setOrigine("AJT");
				tb.save();

			}
		}
	}

	//
	// Undo
	//
	public void undo() {
		StringBuffer sql = new StringBuffer("Delete P_Time_Sheet_Error Where P_Time_Sheet_ID = ")
				.append(TimeSheet.getP_Time_Sheet_ID());
		int no = DB.executeUpdate(sql.toString(), getTrxName());

		// +
		// Delete if the payment already exists.
		if (TimeSheet.getP_Payment_ID() != 0) {
			// P_Payment PaymentDel = P_Payment.get( Env.getCtx (),
			// TimeSheet.getP_Payment_ID(), getTrxName());
			// PaymentDel.delete( true );

			TimeSheet = P_Time_Sheet.SetTimeSheetInitial(TimeSheet.getP_Time_Sheet_ID(), getTrxName());

		}
		// -
	}

	public void deleteDetail() {
		StringBuffer sql = new StringBuffer("Delete From P_Payment_Gain Where P_Payment_ID = ")
				.append(TimeSheet.getP_Payment_ID());
		int no = DB.executeUpdate(sql.toString(), null);
	}

	private void setTrxName(String trxName) {
		m_trxName = trxName;
	}

	private String getTrxName() {
		return m_trxName;
	}

	private boolean background = false;

	// appel de la feuille de temps - Ajustement .
	public TimeValidationServerV2(P_Time_Sheet _TimeSheet, String trxName, boolean _background) {

		setTrxName(trxName);
		TimeSheet = _TimeSheet;
		background = _background;

		Org_ID = TimeSheet.getAD_Org_ID();
		Employee_ID = TimeSheet.getP_Employee_ID();
		Time_Sheet_ID = TimeSheet.getP_Time_Sheet_ID();
		Payment_Group_ID = TimeSheet.getP_Payment_Group_ID();
		Frequency_ID = TimeSheet.getP_Frequency_ID();
		Period_ID = TimeSheet.getP_Period_ID();
		IsRework = true;
		P_Distribution_Booklet_ID = TimeSheet.getP_Distribution_Booklet_ID();
		P_Collective_Labour_Agr_ID = TimeSheet.getP_Collective_Labour_Agr_ID();
		Payment = new P_Payment(Env.getCtx(), TimeSheet.getP_Payment_ID(), getTrxName());
		// 2011.11.30 Utilise les informations de la feuille de temps et non du paiement
		Employee = P_Employee.get(Env.getCtx(), TimeSheet.getP_Employee_ID(), getTrxName());
		Period = P_Period.get(Env.getCtx(), TimeSheet.getP_Period_ID(), getTrxName());

	} // prepare

	public static P_Credits getCreditsVideBanque(Properties ctx, P_Time_Sheet TimeSheet, int P_Gain_ID,
			int P_Employee_ID, String trxName) {
		int id = 0;
		String sql = "SELECT TOP 1 P_CREDITS.P_CREDITS_ID FROM P_GAIN_CREDITS "
				+ " INNER JOIN P_GAIN_PARAMETER ON P_GAIN_PARAMETER.P_GAIN_PARAMETER_ID = P_GAIN_CREDITS.P_GAIN_PARAMETER_ID "
				+ " INNER JOIN P_GAIN ON P_GAIN.P_GAIN_ID = P_GAIN_PARAMETER.P_GAIN_ID "
				+ " INNER JOIN P_EMPLOYEE_CREDITS ON P_EMPLOYEE_CREDITS.P_CREDITS_ID = P_GAIN_CREDITS.P_CREDITS_ID "
				+ " INNER JOIN P_CREDITS ON P_CREDITS.P_CREDITS_ID = P_EMPLOYEE_CREDITS.P_CREDITS_ID "
				+ " INNER JOIN P_EMPLOYEE ON P_EMPLOYEE.P_EMPLOYEE_ID = P_EMPLOYEE_CREDITS.P_EMPLOYEE_ID "
				+ " WHERE P_GAIN.P_GAIN_ID = " + P_Gain_ID
				+ " AND P_EMPLOYEE.P_Employee_ID = " + P_Employee_ID
				+ " AND CREDITS_FUNCTION = '='";
		PreparedStatement pstmt = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				id = rs.getInt("P_Credits_ID");
			} else
				P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error,
						"Le systeme n'a pas trouve banque pour le code de gain", trxName);

			rs.close();
			pstmt.close();
			pstmt = null;
		} catch (Exception e) {
			System.err.println("P_Credits.get - " + e);
		}

		return P_Credits.get(ctx, id, trxName);

	}

	private boolean Distribution_Assignment_360(ResultSet rs_Payment, P_Gain_Account_360 GainAccount)
			throws SQLException {

		BigDecimal AmountCalc = Env.ZERO;
		BigDecimal QuantityCalc = Env.ZERO;
		BigDecimal TotAmountCalc = Env.ZERO;
		BigDecimal TotQuantityCalc = Env.ZERO;

		boolean weHaveDistro = false;
		int line = 0;

		// 2014.04.17 performance
		// String sqlAssDis = "SELECT P_Assignment_Distribution_ID "
		String sqlAssDis = "SELECT * "
				+ " FROM P_Assignment_Distribution "
				+ "   WHERE P_Assignment_Distribution.IsActive = 'Y' "
				+ "   AND P_Assignment_ID = " + rs_Payment.getInt("P_Assignment_ID");

		PreparedStatement pstmt = DB.prepareStatement(sqlAssDis, getTrxName());
		ResultSet rs = pstmt.executeQuery();

		P_Assignment_Distribution Distribution;
		P_Payment_Gain_Distribution_360 paymentGainDistribution = null;

		while (rs.next()) {
			weHaveDistro = true;
			line = line + 10;

			Distribution = new P_Assignment_Distribution(Env.getCtx(), rs, getTrxName());

			// on verifie si la distribution de l'Assignation, pour cette ligne
			// possede un compte
			int iAccount = -1;
			// si id == 0, pas de records
			// donc, si<0 pas de compte au niveau de l'Assignation, donc on prend le compte
			// du code de gain
			if (Distribution.getAccount_ID() < 1) {
				iAccount = GainAccount.getP_Gain_Acct();
			} else {
				iAccount = Distribution.getAccount_ID();
			}

			// on cree la nouvelle ligne d'imputation de paiement

			paymentGainDistribution = new P_Payment_Gain_Distribution_360(Env.getCtx(), -1, getTrxName());
			paymentGainDistribution.setC_Activity_ID(Distribution.getC_Activity_ID());

			if (GainAccount.getC_Activity_ID() != 0)
				paymentGainDistribution.setC_Activity_ID(GainAccount.getC_Activity_ID());

			if (GainAccount.getC_SalesRegion_ID() != 0)
				paymentGainDistribution.setC_SalesRegion_ID(GainAccount.getC_SalesRegion_ID());
			else {
				paymentGainDistribution.setC_SalesRegion_ID(Distribution.getC_SalesRegion_ID());

			}
			// paymentGainDistribution.setC_SalesRegion_ID(
			// Distribution.getC_SalesRegion_ID() );

			paymentGainDistribution.setOrg_ID(Payment.getAD_Org_ID());
			paymentGainDistribution.setAD_Org_ID(Payment.getAD_Org_ID());

			paymentGainDistribution.setP_Payment_Gain_ID(rs_Payment.getInt("P_Payment_Gain_ID"));
			paymentGainDistribution.setLine(line);
			paymentGainDistribution.setAccount_ID(iAccount);

			BigDecimal rate = Distribution.getRatio().divide(new BigDecimal(100), 8, BigDecimal.ROUND_HALF_UP);
			if (rate == null || rate.compareTo(new BigDecimal(0)) == 0) {
				rate = new BigDecimal(1);
			}

			AmountCalc = rs_Payment.getBigDecimal("AmountCalc").multiply(rate);
			QuantityCalc = rs_Payment.getBigDecimal("QuantityCalc").multiply(rate);

			AmountCalc = AmountCalc.setScale(2, BigDecimal.ROUND_HALF_UP);
			QuantityCalc = QuantityCalc.setScale(2, BigDecimal.ROUND_HALF_UP);

			TotAmountCalc = TotAmountCalc.subtract(AmountCalc);
			TotQuantityCalc = TotQuantityCalc.subtract(QuantityCalc);

			paymentGainDistribution.setDistribution_Type(P_Payment_Gain_Distribution.DISTRIBUTION_TYPE_S);

			paymentGainDistribution.setAmount(AmountCalc);
			paymentGainDistribution.setQuantity(QuantityCalc);
			// if (paymentGainDistribution.getAmount().compareTo(Env.ZERO) != 0 )
			// {
			paymentGainDistribution.save();
			// }

		}
		rs.close();
		pstmt.close();
		pstmt = null;

		return weHaveDistro;

	}

	private boolean Distribution_Post_360(ResultSet rs_Payment, P_Gain_Account_360 GainAccount, Timestamp searchDate)
			throws SQLException {

		BigDecimal AmountCalc = Env.ZERO;
		BigDecimal QuantityCalc = Env.ZERO;
		BigDecimal TotAmountCalc = Env.ZERO;
		BigDecimal TotQuantityCalc = Env.ZERO;

		boolean weHaveDistro = false;
		int line = 0;

		// 2014.04.17 performance
		// String strPostAcct = "SELECT P_Post_Distribution_ID "
		String strPostAcct = "SELECT * "
				+ " FROM P_Post_Distribution "
				+ " WHERE P_Post_ID=" + rs_Payment.getInt("P_Post_ID")
				+ " AND EffectIn = (Select Max( EffectIn ) FROM P_Post_Distribution WHERE IsActive = 'Y' and P_Post_ID ="
				+ rs_Payment.getInt("P_Post_ID") + " AND Effectin <=" + DB.TO_DATE(searchDate) + ")"
				+ " AND IsActive = 'Y'";

		PreparedStatement pstmt = DB.prepareStatement(strPostAcct, getTrxName());
		ResultSet rs = pstmt.executeQuery();

		P_Post_Distribution Distribution;
		P_Payment_Gain_Distribution_360 paymentGainDistribution = null;

		while (rs.next()) {
			weHaveDistro = true;
			line = line + 10;

			// Distribution = P_Post_Distribution.get( Env.getCtx(), rs.getInt(1),
			// getTrxName() );
			Distribution = new P_Post_Distribution(Env.getCtx(), rs, getTrxName());

			// on verifie si la distribution de l'Assignation, pour cette ligne
			// possede un compte
			int iAccount = -1;
			// si id == 0, pas de records
			// donc, si<0 pas de compte au niveau de l'Assignation, donc on prend le compte
			// du code de gain
			if (Distribution.getAccount_ID() < 1) {
				iAccount = GainAccount.getP_Gain_Acct();
			} else {
				iAccount = Distribution.getAccount_ID();
			}

			// on cree la nouvelle ligne d'imputation de paiement

			paymentGainDistribution = new P_Payment_Gain_Distribution_360(Env.getCtx(), -1, getTrxName());
			paymentGainDistribution.setC_Activity_ID(Distribution.getC_Activity_ID());

			if (GainAccount.getC_Activity_ID() != 0)
				paymentGainDistribution.setC_Activity_ID(GainAccount.getC_Activity_ID());

			if (GainAccount.getC_SalesRegion_ID() != 0)
				paymentGainDistribution.setC_SalesRegion_ID(GainAccount.getC_SalesRegion_ID());
			else
				paymentGainDistribution.setC_SalesRegion_ID(Distribution.getC_SalesRegion_ID());

			paymentGainDistribution.setOrg_ID(Payment.getAD_Org_ID());
			paymentGainDistribution.setAD_Org_ID(Payment.getAD_Org_ID());

			paymentGainDistribution.setP_Payment_Gain_ID(rs_Payment.getInt("P_Payment_Gain_ID"));
			paymentGainDistribution.setLine(line);
			paymentGainDistribution.setAccount_ID(iAccount);

			BigDecimal rate = Distribution.getRatio().divide(new BigDecimal(100), 8, BigDecimal.ROUND_HALF_UP);
			if (rate == null || rate.compareTo(new BigDecimal(0)) == 0) {
				rate = new BigDecimal(1);
			}

			if (rs_Payment.getBigDecimal("AmountCalc") != null)
				AmountCalc = rs_Payment.getBigDecimal("AmountCalc").multiply(rate);

			if (rs_Payment.getBigDecimal("QuantityCalc") != null)
				QuantityCalc = rs_Payment.getBigDecimal("QuantityCalc").multiply(rate);

			AmountCalc = AmountCalc.setScale(2, BigDecimal.ROUND_HALF_UP);
			QuantityCalc = QuantityCalc.setScale(2, BigDecimal.ROUND_HALF_UP);

			TotAmountCalc = TotAmountCalc.subtract(AmountCalc);
			TotQuantityCalc = TotQuantityCalc.subtract(QuantityCalc);

			paymentGainDistribution.setDistribution_Type(P_Payment_Gain_Distribution.DISTRIBUTION_TYPE_S);
			paymentGainDistribution.setAmount(AmountCalc);
			paymentGainDistribution.setQuantity(QuantityCalc);
			// if (paymentGainDistribution.getAmount().compareTo(Env.ZERO) != 0 )
			// {
			paymentGainDistribution.save();
			// }

		}
		rs.close();
		pstmt.close();
		pstmt = null;

		return weHaveDistro;

	}

	public void Create_PaymentGainDistribution_360(P_Payment payment, int P_Payment_Gain_ID) throws Exception {

		P_Gain_Account_360 GainAccount = null;
		// 1 - Aller voir si ya une unite administrative inscrite sur la
		// feuille de temps
		// 2 - aller chercher la ventilation sur l'Asssignation
		// 3 - si inexistante aller chercher la ventilation sur le poste
		// 4 - pour chaque ligne de ventilation, si pas de compte aller
		// le chercher dans le gain

		// P_Workplace Workplace = P_Workplace.get( Env.getCtx(),
		// Payment.getP_Workplace_ID(), getTrxName() );

		P_Assignment_Distribution assignment_Distro = null;
		P_Post_Distribution post_Distro = null;
		// P_Gain_Account gain_Account = null;
		boolean weHaveDistro = false;

		BigDecimal AmountCalc = Env.ZERO;
		BigDecimal QuantityCalc = Env.ZERO;
		BigDecimal TotAmountCalc = Env.ZERO;
		BigDecimal TotQuantityCalc = Env.ZERO;

		String sql = null;
		// sql = "Select P_Payment_Gain_ID, P_Payment_Gain.AmountCalc,
		// P_Payment_Gain.QuantityCalc, P_Payment_Gain.P_Assignment_ID,
		// P_Payment_Gain.P_Post_ID, P_Payment_Gain.P_Gain_ID, Day, isnull(
		// P_Payment_Gain.C_Activity_ID, P_Post.C_Activity_ID ) C_Activity_ID,
		// P_Payment_Gain.AD_Org_ID, P_Post.P_Workplace_ID "
		sql = "Select P_Payment_Gain_ID, P_Payment_Gain.AmountCalc, P_Payment_Gain.QuantityCalc, P_Payment_Gain.P_Assignment_ID, P_Payment_Gain.P_Post_ID,  P_Payment_Gain.P_Gain_ID, Day, P_Post.C_Activity_ID C_Activity_ID, P_Payment_Gain.AD_Org_ID, P_Post.P_Workplace_ID "
				+ " From P_Payment_Gain "
				+ " LEFT OUTER JOIN P_Assignment on P_Assignment.P_Assignment_ID = P_Payment_Gain.P_Assignment_ID "
				+ " LEFT OUTER JOIN P_POST on P_Post.P_Post_ID = P_Assignment.P_Post_ID "
				+ " WHERE P_Payment_ID = " + Payment.getP_Payment_ID()
				+ "  AND AmountCalc <> 0 ";

		if (P_Payment_Gain_ID != 0)
			sql += " AND P_Payment_Gain_ID = " + P_Payment_Gain_ID;

		PreparedStatement pstmt_Payment = null;
		pstmt_Payment = DB.prepareStatement(sql, getTrxName());
		ResultSet rs_Payment = pstmt_Payment.executeQuery();

		int line = 0;

		P_Payment_Gain_Distribution_360 paymentGainDistribution = null;
		while (rs_Payment.next()) {

			P_Workplace Workplace = P_Workplace.get(Env.getCtx(), rs_Payment.getInt("P_Workplace_ID"), null);
			GainAccount = P_Gain_Account_360.getWithSalesRegion(Env.getCtx(), Payment.getAD_Client_ID(),
					Payment.getAD_Org_ID(), Workplace.getC_SalesRegion_ID(), rs_Payment.getInt("P_Gain_ID"), null);

			// primo, on va chercher le gain-account, il risque de servir dans la
			// ventilation
			if (GainAccount != null) {
				AmountCalc = rs_Payment.getBigDecimal("AmountCalc");
				QuantityCalc = rs_Payment.getBigDecimal("QuantityCalc");
				TotAmountCalc = rs_Payment.getBigDecimal("AmountCalc");
				TotQuantityCalc = rs_Payment.getBigDecimal("QuantityCalc");
				line = line + 10;

				weHaveDistro = true;

				// Etape 1, verifier sil y a une unite administrative de saisie
				// sur la feuille de temps
				// si oui, on ventile sur cette unite et on prend le compte du gain

				if (rs_Payment.getInt("C_Activity_ID") != 0 || GainAccount.getC_Activity_ID() != 0) {
					int ActivityID = 0;
					/*
					 * if ( rs_Payment.getInt("C_Activity_ID") != 0 )
					 * ActivityID = rs_Payment.getInt("C_Activity_ID");
					 * else
					 * ActivityID = GainAccount.getC_Activity_ID();
					 */
					if (GainAccount.getC_Activity_ID() != 0)
						ActivityID = GainAccount.getC_Activity_ID();
					else
						ActivityID = rs_Payment.getInt("C_Activity_ID");
					// on cree la nouvelle ligne d'imputation de paiement

					paymentGainDistribution = new P_Payment_Gain_Distribution_360(Env.getCtx(), -1, getTrxName());
					paymentGainDistribution.setC_Activity_ID(ActivityID);

					/*
					 * if ( GainAccount.getC_SalesRegion_ID() != 0)
					 * paymentGainDistribution.setC_SalesRegion_ID(
					 * GainAccount.getC_SalesRegion_ID() );
					 * else
					 */
					paymentGainDistribution.setC_SalesRegion_ID(Workplace.getC_SalesRegion_ID());

					paymentGainDistribution.setOrg_ID(Payment.getAD_Org_ID());
					paymentGainDistribution.setAD_Org_ID(Payment.getAD_Org_ID());

					paymentGainDistribution.setP_Payment_Gain_ID(rs_Payment.getInt("P_Payment_Gain_ID"));
					paymentGainDistribution.setLine(line);

					paymentGainDistribution.setAccount_ID(GainAccount.getP_Gain_Acct());

					paymentGainDistribution.setDistribution_Type(P_Payment_Gain_Distribution_360.DISTRIBUTION_TYPE_S);
					// paymentGainDistribution.setC_SalesRegion_ID(
					// GainAccount.getC_SalesRegion_ID() );
					paymentGainDistribution.setAmount(AmountCalc);
					paymentGainDistribution.setQuantity(QuantityCalc);

					paymentGainDistribution.save();

				} else// donc on a pas d'unite administrative sur la feuille de temps
				{
					// Etape 2, verifier si on peut ventiler sur l'Assignation
					weHaveDistro = Distribution_Assignment_360(rs_Payment, GainAccount);
					// Etape 3, on verifie si on a eu une ventilation sur l'Assignation

					if (weHaveDistro == false) {
						weHaveDistro = Distribution_Post_360(rs_Payment, GainAccount, rs_Payment.getTimestamp("Day"));
					}

					if (weHaveDistro == false) {
						Timestamp searchDate = new Timestamp(GregorianCalendar.getInstance().getTimeInMillis());
						weHaveDistro = Distribution_Post_360(rs_Payment, GainAccount, searchDate);

						// Puisqu'on a utilise la date du jour, on doit ajouter un avertissement
						// e la feuille de temps (s'il y a lieu)
						if (weHaveDistro) {
							String message = "Une transaction anterieure e la date du jour utilise une distribution comptable active en date d'aujourd'hui.";
							P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Warning,
									message, getTrxName());
						}
					}
				}

				if (!weHaveDistro) {
					String message = "La distribution comptable sous l'affectation ou le poste est obligatoire.";
					// String message = Msg.getMsg(Env.getCtx(), "AssignmentDistributionRequired");
					P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, message,
							getTrxName());
				}
			} else {
				P_Gain Gain = P_Gain.get(Env.getCtx(), rs_Payment.getInt("P_Gain_ID"), m_trxName);
				String message = Msg.getMsg(Env.getCtx(), "GainAccountRequired") + " " + Gain.getValue();
				P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, message,
						getTrxName());
				return;
			}

			// rs.close ();
			// pstmt.close ();
			// pstmt = null;

		}
		rs_Payment.close();
		pstmt_Payment.close();
		pstmt_Payment = null;

	}

}
