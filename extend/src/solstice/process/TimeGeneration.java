/*
 * Created on 2005-06-22
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.TimeUtil;
import org.compiere.util.Trx;

import java.util.Properties;
import java.util.logging.Level;

import solstice.model.IBookletTimeSheet;
import solstice.model.IBookletTimeSheetDetail;
import solstice.model.P_Assignment;
import solstice.model.P_Assignment_Param;
import solstice.model.P_Assignment_RWT;
import solstice.model.P_Booklet;
import solstice.model.P_Booklet_Detail;
import solstice.model.P_Employee;
import solstice.model.P_Employee_Expensereports;
import solstice.model.P_Frequency;
import solstice.model.P_Period;
import solstice.model.P_Time_Sheet;
import solstice.model.P_Time_Sheet_Detail;
import solstice.model.P_Time_Sheet_Error;
import solstice.model.P_Employee_LongTermLeave;
import solstice.model.P_NonBusinessDay;
import solstice.model.P_Employee_NonBusinessDay;
import solstice.model.P_Standard_Time;
import solstice.model.P_Particular_Sheet;
import solstice.model.P_Payment;
import solstice.model.P_Gain;
import solstice.model.P_Timecode;
import solstice.model.X_I_Employee_NonBusinessDay;
import solstice.model.X_I_Expense_Account;
import solstice.model.P_SelfFinancedLeave;
import solstice.model.P_Schedule;
import solstice.model.P_Collective_Labour_Agr;
import solstice.model.P_Absence_Detail;
import solstice.model.P_Payment_Group;
import solstice.model.P_Gain_GainInfo;
import solstice.model.P_GainInfo;

import org.compiere.model.MActivity;
import org.compiere.model.MCharge;
import org.compiere.model.MRole;
import org.compiere.util.Env;


/**
 * @author marmor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TimeGeneration 
{
    private char generationType;

	private P_Frequency Frequency;
	private P_Payment_Group PaymentGroup ;
	private P_Period Period;
	private P_Period m_Period;
	private P_Employee Employee;
//	private P_Assignment_Param Assignment_Employee;
	private P_Assignment_Param AssignmentParam;
	private IBookletTimeSheet TimeSheet;

    private P_Gain Gain;
    private P_Timecode Timecode;
    private String startTime;
    private String endTime;
	private P_Schedule Schedule;
	private MActivity Activity;
	private P_SelfFinancedLeave SelfFinancedLeave;
	private P_Employee_LongTermLeave EmployeeLongTermLeave = null;
	private Timestamp PeriodDate;
	private Timestamp GenStartDate;
	private Timestamp GenEndDate;
	private BigDecimal Dayqty;
	private BigDecimal Hourly_Rate;
	
	private String sheetType;

/*
	private BigDecimal Sunday;
	private BigDecimal Monday;
	private BigDecimal Tuesday;
	private BigDecimal Wednesday;
	private BigDecimal Thursday;
	private BigDecimal Friday;
	private BigDecimal Saturday;
*/	
    private String     OriginTime;
	
	private Hashtable 	s_GenTime = new Hashtable();	//	

	public int DisplayDetail = 1; //Ajout Bug Gen 
	private int	m_inserted = 0;
	private int weekNumber;
	
	Properties m_ctx;

	private static CLogger		log = CLogger.getCLogger (TimeGeneration.class);

	/**	Big Decimal 0	 */
	static final public java.math.BigDecimal ZERO = new java.math.BigDecimal(0.0);

	private boolean GenerationDetail; 

    private boolean IsRework = false;

    private boolean IsPlanifico = false;
    
    private boolean Particular_Cut_Std = PgiUtil.getSolsticeParameter( Env.getCtx(), "ParticutarCutRegular").equals("YES");
    
    private boolean IsNegativeCut = false;
    private int Period_ID = 0;

    // Multi-thread control fields
    private boolean m_multiThread = true;
    private boolean m_isWorker = false;
    private int m_customThreadCount = 0;

    public void setMultiThread(boolean multiThread) {
        this.m_multiThread = multiThread;
    }

    public boolean isMultiThread() {
        return m_multiThread;
    }

    public void setWorker(boolean isWorker) {
        this.m_isWorker = isWorker;
    }

    public boolean isWorker() {
        return m_isWorker;
    }

    public void setThreadCount(int threadCount) {
        this.m_customThreadCount = threadCount;
    }

    public int getThreadCount() {
        return m_customThreadCount;
    }

    private static class EmployeeGenTask {
        final int employeeId;
        final int paymentGroupId;

        EmployeeGenTask(int employeeId, int paymentGroupId) {
            this.employeeId = employeeId;
            this.paymentGroupId = paymentGroupId;
        }
    }
	
    //  Constructeur par défaut
	public TimeGeneration( Properties ctx, char generationType, boolean _IsRework, boolean _IsPlanifico, String sheetType)
	{
	    this.generationType = generationType;
	    this.GenerationDetail = false;
	    this.IsRework = _IsRework;
	    this.sheetType = sheetType;

	    this.IsPlanifico = _IsPlanifico;

	    m_ctx = ctx;
	}
	
	/**
	 * Cette méthode retourne une instance de feuille ou de livert de temps
	 * dépendemment du type de génération
	 */
	public IBookletTimeSheet getInstance(Properties ctx, int id, String trxName)
	{
	    if(this.generationType == 'S')
	    {
	        // On renvoit une instance de feuille de temps
	        return new P_Time_Sheet(ctx, id, trxName);
	    }
	    
	    // Sinon, on renvoit une instance de livert de temps
	    return new P_Booklet(ctx, id, trxName);
	}

	/**
	 * Cette méthode retourne une instance de feuille ou de livert de temps
	 * dépendemment du type de génération
	 */
	private IBookletTimeSheetDetail getInstanceDetail(Properties ctx, int id, String trxName)
	{
	    if(this.generationType == 'S')
	    {
	        // On renvoit une instance de feuille de temps
	        return new P_Time_Sheet_Detail(ctx, id, trxName);
	    }
	    
	    // Sinon, on renvoit une instance de livert de temps
	    return new P_Booklet_Detail(ctx, id, trxName);
	}

	/**
	 * Cette méthode retourne le nom de la table dépendemment du type de génération
	 */
	private String getTableDetail()
	{
	    if(this.generationType == 'S')
	    {
	        // On renvoit une instance de feuille de temps
	        return "P_Time_Sheet_Detail";
	    }
	    
	    // Sinon, on renvoit une instance de livert de temps
	    return "P_Booklet_Detail";
	}

	/**
	 * Cette méthode retourne le nom de la table dépendemment du type de génération
	 */
	private String getTable()
	{
	    if(this.generationType == 'S')
	    {
	        // On renvoit une instance de feuille de temps
	        return "P_Time_Sheet";
	    }
	    
	    // Sinon, on renvoit une instance de livert de temps
	    return "P_Booklet";
	}

	private String m_trxName;
	
	public String Generation( Properties ctx, int Client_ID, int Org_ID, int Activity_ID ,int Employee_ID, int Payment_Group_ID, int Period_ID, int P_Distribution_Booklet_ID, int P_Collective_Labour_Agr_ID, int P_Time_Sheet_ID, String TrxName ) throws Exception
	{
//		Env.setCtx(ctx);
		m_ctx = ctx;
		
    	String trxName = null;
    	
    	
        P_Period period = P_Period.get( Env.getCtx(), Period_ID, trxName);
        
		//+ 2010.05.04 
        // stop this procedure if the period is closed
        if (period.getPeriodStatus().equals(P_Period.PERIODSTATUS_Closed) || period.getPeriodStatus().equals(P_Period.PERIODSTATUS_PermanentlyClosed) )
    		return "@inserted@ " + m_inserted;
		//- 2010.05.04 

        //+ 2011.03.01 stop this procedure if the period is a adjustment Period
        if ( period.isAdjustmentPeriod() )
    		return "@inserted@ " + m_inserted;
        //- 2011.03.01 
        
        P_Frequency frequency = P_Frequency.get( Env.getCtx(), period.getP_Frequency_ID(), trxName);
        //TODO read PaymentGroup
        if ( frequency.getNumberOfPeriod() == 24 )
        	DisplayDetail = 1;	
        else
        	DisplayDetail = 0;
        
  /*      
		if (Employee_ID == 0)
		{
			P_Gain.loadAll(Env.getCtx());
			P_Standard_Time.loadAll(Env.getCtx());
			P_Employee.loadAll(Env.getCtx());
			P_Assignment.loadAll(Env.getCtx());
		}
	*/	
		m_trxName = TrxName;

//		Env.setContext( Env.getCtx(), "#AD_Client_ID", 11);
//		Env.setContext( Env.getCtx(), "#AD_Org_ID", 11);

		//2023-06-05 + import les vacances de intranet seulment s'il n'y pas d'info deja importé
		// pas de reprise.
//		if ( PgiUtil.getSolsticeParameter( Env.getCtx(), "KRISPY").trim().equals("Test") )
//		{
/*		
			String sql2 = "exec KRISPY_IMPORT_NONBUSINESSDAY_V2 " + period.getP_Period_ID() + ", 0" ;
			if ( Employee_ID != 0 )
				sql2 = "exec KRISPY_IMPORT_NONBUSINESSDAY_V2 " + period.getP_Period_ID() + ", " + Employee_ID ;
			DB.executeUpdate(sql2, null);
*/			
//		}
//		else {
//			String sql2 = "exec KRISPY_IMPORT_NONBUSINESSDAY " + period.getP_Period_ID() ;
//			DB.executeUpdate(sql2, null);
//		}
		
		
		int AD_Session_ID = Env.getContextAsInt(Env.getCtx(), "#AD_Session_ID");

		m_Period = P_Period.get( getCtx (), Period_ID , m_trxName);
		Period = P_Period.get( getCtx (), Period_ID , m_trxName);
		Frequency = P_Frequency.get( getCtx(), Period.getP_Frequency_ID(), m_trxName);
			
/*		if ( Period.getP_Frequency_ID() != Frequency_ID )
		{
			return " ** Error - Error with Parameter ";
		}
*/
		String sql = null;
		if ( this.generationType != 'S' )
			P_Time_Sheet_ID = 0;

		sql = "Select P_Employee.P_Employee_ID, P_Payment_Group.P_Frequency_ID, P_Employee.P_Payment_Group_ID FROM P_Employee, P_Payment_Group " 
            + " WHERE P_Payment_Group.P_Payment_Group_ID = P_Employee.P_Payment_Group_ID "
			+ "   AND P_Employee.AD_Client_ID = " + Client_ID
			;

		// inclus uniquement les employés actif et non terminer.
		if ( Employee_ID == 0 )
			sql += " AND P_Employee.IsActive='Y' AND P_LONGTERMLEAVE_ID <> 1000156  ";

		
		if (Org_ID > 0)
			sql += "   AND P_Employee.AD_Org_ID = " + Org_ID;
			
			
		if (Activity_ID > 0)
			sql +=  "   AND P_Employee.C_Activity_ID = " + Activity_ID
			
			
//			+ "   AND P_Payment_Group.P_Frequency_ID= " + Period.getP_Frequency_ID() 
			;
		if (Employee_ID > 0)
			sql += " AND P_Employee.P_Employee_ID=" + Employee_ID;
		if (Payment_Group_ID > 0)
			sql += " AND P_Employee.P_Payment_Group_ID=" + Payment_Group_ID;

		if (P_Distribution_Booklet_ID > 0)
			sql += " AND P_Employee.P_Distribution_Booklet_ID=" + P_Distribution_Booklet_ID;

		if (P_Collective_Labour_Agr_ID > 0)
			sql += " AND P_Employee.P_Collective_Labour_Agr_ID=" + P_Collective_Labour_Agr_ID;
		
		if ( this.IsPlanifico )
			sql += " AND P_Employee.P_Employee_ID in ( SELECT P_Employee_ID FROM P_Punch_Time ) ";
		
		sql += " AND P_Employee.AD_Client_ID in ( " + Env.getAD_Client_ID(Env.getCtx()) + " , 0 ) ";
		if ( !IsRework )
		{
			if(this.generationType == 'S' && P_Time_Sheet_ID == 0 )
				sql += " AND Not Exists( Select 1 From " + this.getTable() + " Where " + this.getTable() + ".P_Period_ID = " + Period_ID + " and " + this.getTable() + ".P_Employee_ID = P_Employee.P_Employee_ID  and SheetType = 'Regular'  )";
			else if ( this.generationType != 'S' )
				sql += " AND Not Exists( Select 1 From " + this.getTable() + " Where " + this.getTable() + ".P_Period_ID = " + Period_ID + " and " + this.getTable() + ".P_Employee_ID = P_Employee.P_Employee_ID  )";
		}
		// 2008-06-06
		// Si c'est une reprise, la feuille de temps ou le livret de temps doit être au statut initial.
		else
		{
			if(this.generationType == 'S' && P_Time_Sheet_ID == 0 )
				sql += " AND Not Exists( Select 1 From " + this.getTable() + " Where " + this.getTable() + ".P_Period_ID = " + Period_ID + " and " + this.getTable() + ".P_Employee_ID = P_Employee.P_Employee_ID  and SheetType = 'Regular' and timeSheetStatus != 'I' )";
			else if ( this.generationType != 'S' )
				sql += " AND Not Exists( Select 1 From " + this.getTable() + " Where " + this.getTable() + ".P_Period_ID = " + Period_ID + " and " + this.getTable() + ".P_Employee_ID = P_Employee.P_Employee_ID  and timeSheetStatus != 'I' )";
		}
			
			
		// Si on doit générer des livrets, on doit vérifier si l'employé appartient à une
		// distribution utilisant les livrets
		if(this.generationType != 'S')
			sql += " and exists (select 1 from P_Distribution_Booklet, P_Employee_Distribution_Booklet where P_Employee_Distribution_Booklet.P_Employee_ID = P_Employee.P_Employee_ID and P_Employee_Distribution_Booklet.P_Distribution_Booklet_ID = P_Distribution_Booklet.P_Distribution_Booklet_ID and IsUsingWEB = 'Y' and Start_Date <= " + DB.TO_DATE(Period.getEndDate()) + " and (End_Date is null or End_Date >= " + DB.TO_DATE( Period.getStartDate()) + " ))";
//		    sql += " and exists (select 1 from P_Distribution_Booklet where P_Distribution_Booklet_ID = P_Employee.P_Distribution_Booklet_ID and IsUsingWEB = 'Y')";
		sql += " ORDER BY P_Employee.value";

		sql = MRole.getDefault(Env.getCtx(), false).addAccessSQL (sql, "P_Employee", true, false);	// fully qualidfied - RO 

				PreparedStatement pstmt = null;
		List<EmployeeGenTask> employeeTasks = new ArrayList<EmployeeGenTask>();

		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				employeeTasks.add(new EmployeeGenTask(
					rs.getInt("P_Employee_ID"),
					rs.getInt("P_Payment_Group_ID")
				));
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"doIt - " + sql, e);
			throw e;
		}

		if (employeeTasks.isEmpty())
		{
			return "Enregistrements cr\u00e9\u00e9s 0";
		}

		// Mode mono-thread / s\u00e9quentiel si 1 seul employ\u00e9, si forc\u00e9 mono-thread ou si d\u00e9j\u00e0 dans un worker
		if (m_isWorker || !m_multiThread || employeeTasks.size() <= 1)
		{
			for (EmployeeGenTask task : employeeTasks)
			{
				generateSingleEmployee(ctx, Client_ID, Org_ID, Activity_ID,
					task.employeeId, task.paymentGroupId, Period_ID,
					P_Distribution_Booklet_ID, P_Collective_Labour_Agr_ID, P_Time_Sheet_ID, TrxName);
			}
		}
		else
		{
			// D\u00e9terminer le nombre de threads concurrents (au moins 50 par d\u00e9faut, ou selon param\u00e8tres/c\u0153urs)
			int nbrThread = 50;
			if (m_customThreadCount > 0)
			{
				nbrThread = m_customThreadCount;
			}
			else
			{
				try
				{
					String pThread = PgiUtil.getSolsticeParameter(getCtx(), "TimeGenerationThread");
					if (pThread == null || pThread.trim().isEmpty())
						pThread = PgiUtil.getSolsticeParameter(getCtx(), "CalculPayrollThread");
					if (pThread != null && !pThread.trim().isEmpty())
						nbrThread = Integer.parseInt(pThread.trim());
				}
				catch (Exception e)
				{
					log.log(Level.WARNING, "TimeGeneration - error reading thread parameter: " + e.getMessage());
				}
				if (nbrThread <= 0)
				{
					nbrThread = Math.max(50, Runtime.getRuntime().availableProcessors() * 4);
				}
			}

			if (nbrThread > employeeTasks.size())
			{
				nbrThread = employeeTasks.size();
			}

			log.log(Level.INFO, "TimeGeneration - Starting multi-thread generation with " + nbrThread
					+ " concurrent threads for " + employeeTasks.size() + " employees (Type: " + this.generationType + ")");

			long startTimeMillis = System.currentTimeMillis();

			final Properties workerCtx = (m_ctx != null) ? m_ctx : Env.getCtx();
			final int fClientId = Client_ID;
			final int fOrgId = Org_ID;
			final int fActivityId = Activity_ID;
			final int fPeriodId = Period_ID;
			final int fBookletId = P_Distribution_Booklet_ID;
			final int fColAgrId = P_Collective_Labour_Agr_ID;
			final int fTimeSheetId = P_Time_Sheet_ID;
			final char fGenType = this.generationType;
			final boolean fRework = this.IsRework;
			final boolean fPlanifico = this.IsPlanifico;
			final String fSheetType = this.sheetType;

			ExecutorService executor = null;
			final Semaphore semaphore = new Semaphore(nbrThread);
			try
			{
				// Java 21+ / Java 27 : Virtual Threads
				java.lang.reflect.Method m = Executors.class.getMethod("newVirtualThreadPerTaskExecutor");
				executor = (ExecutorService) m.invoke(null);
				log.log(Level.INFO, "TimeGeneration - Using Java 27 Virtual Threads Executor (Concurrency limit: " + nbrThread + ")");
			}
			catch (Throwable t)
			{
				executor = Executors.newFixedThreadPool(nbrThread, new ThreadFactory() {
					private final AtomicInteger threadNum = new AtomicInteger(1);
					public Thread newThread(Runnable r) {
						Thread th = new Thread(r, "TimeGen-Worker-" + threadNum.getAndIncrement());
						th.setDaemon(true);
						return th;
					}
				});
				log.log(Level.INFO, "TimeGeneration - Using Fixed Thread Pool (" + nbrThread + " threads)");
			}

			List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
			for (final EmployeeGenTask task : employeeTasks)
			{
				futures.add(executor.submit(new Callable<Integer>() {
					public Integer call() throws Exception {
						semaphore.acquire();
						try {
							TimeGeneration worker = new TimeGeneration(workerCtx, fGenType, fRework, fPlanifico, fSheetType);
							worker.setWorker(true);
							worker.setMultiThread(false);
							int inserted = worker.generateSingleEmployee(
								workerCtx, fClientId, fOrgId, fActivityId,
								task.employeeId, task.paymentGroupId, fPeriodId,
								fBookletId, fColAgrId, fTimeSheetId, null
							);
							return Integer.valueOf(inserted);
						}
						finally {
							semaphore.release();
						}
					}
				}));
			}

			executor.shutdown();
			int totalInserted = 0;
			int taskErrors = 0;
			for (Future<Integer> f : futures)
			{
				try
				{
					Integer count = f.get();
					if (count != null)
					{
						totalInserted += count.intValue();
					}
				}
				catch (Exception ex)
				{
					taskErrors++;
					log.log(Level.SEVERE, "TimeGeneration - Worker task failed", ex);
				}
			}

			long elapsed = System.currentTimeMillis() - startTimeMillis;
			m_inserted = totalInserted;
			log.log(Level.INFO, "TimeGeneration - Multi-thread generation finished in " + elapsed + " ms ("
					+ totalInserted + " created, " + taskErrors + " errors) across " + nbrThread + " threads.");
		}
		
		// Employé ou juste compte de dépense, mais pas lors de la génération général
		if ( sheetType.equals( P_Time_Sheet.SHEETTYPE_ExpenseAccount )  ) //|| sheetType.equals("ALL" ))  if (Employee_ID > 0) 
		{
			GenerationExpense_Account( ctx, Client_ID, Org_ID, Activity_ID ,Employee_ID, Payment_Group_ID, Period_ID, P_Distribution_Booklet_ID, P_Collective_Labour_Agr_ID, P_Time_Sheet_ID, TrxName );
		}

		if ( sheetType.equals( P_Time_Sheet.SHEETTYPE_HolidayPayment ) || sheetType.equals("ALL" )) 
		{
			Generation_Vacation_Import( ctx, Client_ID, Org_ID, Activity_ID ,Employee_ID, Payment_Group_ID, Period_ID, P_Distribution_Booklet_ID, P_Collective_Labour_Agr_ID, P_Time_Sheet_ID, TrxName  );
		}
		
		

		// Afficher automatiquement le rapport des messages et erreurs a l'ecran (mode client)
		showErrorReportUI(ctx, Period_ID, Employee_ID, P_Time_Sheet_ID);

		return "Enregistrements créés " + m_inserted;
		
		
	}	

	
	/**
	 * G\u00e9n\u00e9ration unitaire pour un seul employ\u00e9.
	 * Ex\u00e9cut\u00e9e soit s\u00e9quentiellement soit par un worker multi-thread d\u00e9di\u00e9.
	 *
	 * @return nombre d'enregistrements cr\u00e9\u00e9s (1 ou 0)
	 */
	protected int generateSingleEmployee(Properties ctx, int Client_ID, int Org_ID, int Activity_ID,
			int empId, int paymentGroupId, int periodId, int bookletId, int colAgrId, int timeSheetId, String trxName)
	{
		this.m_ctx = ctx;
		this.GenerationDetail = false;
		this.m_inserted = 0;

		try {
			this.Period_ID = periodId;
			this.m_Period = P_Period.get(getCtx(), periodId, trxName);
			this.Period = P_Period.get(getCtx(), periodId, trxName);
			this.Frequency = P_Frequency.get(getCtx(), Period.getP_Frequency_ID(), trxName);

			if (this.Frequency != null && this.Frequency.getNumberOfPeriod() == 24)
				this.DisplayDetail = 1;
			else
				this.DisplayDetail = 0;

			PaymentGroup = P_Payment_Group.get(getCtx(), paymentGroupId, null);

			Trx trx = null;
			try {
				m_trxName = null;
				if (trx != null) trx.start();

				Employee = P_Employee.get(getCtx(), empId, m_trxName);
				log.log(Level.INFO, "Generation - Employee " + Employee.toString());

				if (IsRework && (sheetType.equals(P_Time_Sheet.SHEETTYPE_Regular) || sheetType.equals("ALL")))
				{
					String delSql = "delete from " + this.getTable()
						+ " Where " + this.getTable() + ".P_Period_ID = " + Period.getP_Period_ID()
						+ " and " + this.getTable() + ".P_Employee_ID = " + empId
						+ " and SHEETTYPE = 'Regular' "
						+ " and TimesheetStatus = 'I' ";
					DB.executeUpdate(delSql, null);
					log.log(Level.INFO, delSql);
				}

				if ((this.generationType == 'S'
						&& !this.createFromBooklet(empId, periodId, timeSheetId))
					|| this.generationType != 'S')
				{
					s_GenTime.clear();

					if (sheetType == null) sheetType = "ALL";

					if (sheetType.equals(P_Time_Sheet.SHEETTYPE_Regular) || sheetType.equals("ALL"))
					{
						if (timeSheetId == 0)
							create_time_sheet(P_Time_Sheet.SHEETTYPE_Regular, m_trxName);
						else
							TimeSheet = this.getInstance(getCtx(), timeSheetId, m_trxName);

						Hourly_Rate = null;
						AssignmentParam = null;

						boolean isLongTermLeave = this.isLongTermLeave(Period.getStartDate(), Period.getEndDate());

						if (isLongTermLeave)
						{
							log.log(Level.INFO, "Generation - Employee " + Employee.toString() + " gen_longTermLeave ");
							gen_longTermLeave();

							log.log(Level.INFO, "Generation - Employee " + Employee.toString() + " gen_holidays_calendar ");
							gen_holidays_calendar();

							log.log(Level.INFO, "Generation - Employee " + Employee.toString() + " gen_particular_sheet ");
							int no = gen_particular_sheet();

							log.log(Level.INFO, "Generation - Employee " + Employee.toString() + " gen_punch_time ");
							gen_punch_time();

							log.log(Level.INFO, "Generation - Employee " + Employee.toString() + " gen_web_time ");
							gen_bonus();

							if (this.generationType == 'S')
							{
								generate_special_gain(false, TimeSheet);
							}
						}
						else
						{
							if (Employee.isCustomFieldYesNo01())
							{
								Period_ID = getLastPeriod();
								this.Period = P_Period.get(Env.getCtx(), Period_ID, m_trxName);
							}
							else
							{
								Period_ID = m_Period.getP_Period_ID();
								this.Period = P_Period.get(Env.getCtx(), m_Period.getP_Period_ID(), m_trxName);
							}

							log.log(Level.INFO, "Generation - Employee " + Employee.toString() + " gen_standard_time ");
							gen_standard_time();
							log.log(Level.INFO, "Generation - Employee " + Employee.toString() + " gen_standard_time_money ");
							gen_standard_time_money(0);

							log.log(Level.INFO, "Generation - Employee " + Employee.toString() + " gen_punch_time ");
							gen_punch_time();

							boolean ftWeb = false;
							Timecode = null;
							startTime = null;
							endTime = null;
							Hourly_Rate = null;

							log.log(Level.INFO, "Generation - Employee " + Employee.toString() + " gen_holidays_calendar ");
							if (ftWeb == false)
								gen_holidays_calendar();

							log.log(Level.INFO, "Generation - Employee " + Employee.toString() + " gen_longTermLeave ");
							gen_longTermLeave();

							log.log(Level.INFO, "Generation - Employee " + Employee.toString() + " gen_employee_calendar ");
							gen_employee_calendar();

							log.log(Level.INFO, "Generation - Employee " + Employee.toString() + " gen_particular_sheet ");
							gen_particular_sheet();

							gen_bonus();

							log.log(Level.INFO, "Generation - Employee " + Employee.toString() + " generate_special_gain ");
							if (this.generationType == 'S')
							{
								generate_special_gain(false, TimeSheet);
							}

							Period = P_Period.get(Env.getCtx(), m_Period.getP_Period_ID(), m_trxName);

							log.log(Level.INFO, "Generation - Employee " + Employee.toString() + " Cut_Bonus ");
							Cut_Bonus();
						}

						if (!GenerationDetail)
						{
							if (this.generationType == 'S')
							{
								TimeSheet.delete(true);
								m_inserted--;
							}
						}
					}
				}
				log.log(Level.INFO, "Generation - Employee " + Employee.toString() + " Commit ");
				if (trx != null) trx.commit();
			}
			catch (Exception e) {
				log.log(Level.SEVERE, "TimeGeneration - generateSingleEmployee error for employee " + empId, e);
				if (trx != null) trx.rollback();
			}
			finally {
				if (trx != null) trx.close();
			}
		}
		catch (Exception ex) {
			log.log(Level.SEVERE, "TimeGeneration - outer error for employee " + empId, ex);
		}

		return m_inserted;
	}

	public String GenerationBonusOnly( Properties ctx, int P_Time_Sheet_ID, String TrxName ) throws Exception
	{
		m_ctx = ctx;
		
    	String trxName = null;

		TimeSheet = this.getInstance( getCtx (), P_Time_Sheet_ID, m_trxName);
    	
        P_Period period = P_Period.get( Env.getCtx(), TimeSheet.getP_Period_ID(), trxName);
        
		//+ 2010.05.04 
        // stop this procedure if the period is closed
        if (period.getPeriodStatus().equals(P_Period.PERIODSTATUS_Closed) || period.getPeriodStatus().equals(P_Period.PERIODSTATUS_PermanentlyClosed) )
    		return "@inserted@ " + m_inserted;
		//- 2010.05.04 

        //+ 2011.03.01 stop this procedure if the period is a adjustment Period
        if ( period.isAdjustmentPeriod() )
    		return "@inserted@ " + m_inserted;
        //- 2011.03.01 

		if ( ! sheetType.equals( P_Time_Sheet.SHEETTYPE_Complementary )) 
    		return "@inserted@ " + m_inserted;

        
        P_Frequency frequency = P_Frequency.get( Env.getCtx(), period.getP_Frequency_ID(), trxName);
        //TODO read PaymentGroup
        if ( frequency.getNumberOfPeriod() == 24 )
        	DisplayDetail = 1;	
        else
        	DisplayDetail = 0;
        
        m_trxName = TrxName;


		m_Period = P_Period.get( getCtx (), TimeSheet.getP_Period_ID() , m_trxName);
		Period = P_Period.get( getCtx (), TimeSheet.getP_Period_ID() , m_trxName);
		Frequency = P_Frequency.get( getCtx(), Period.getP_Frequency_ID(), m_trxName);

//		Connaitre la période
		
		this.GenerationDetail = false;

		PaymentGroup = P_Payment_Group.get( getCtx(), TimeSheet.getP_Payment_Group_ID(), null );
		
		Trx trx = null;
	    try {
	        m_trxName = null; // Trx.createTrxName("tsg_"+rs.getInt("P_Employee_ID"));
	        if( trx != null ) trx.start();

			Employee = P_Employee.get( getCtx (), TimeSheet.getP_Employee_ID(), m_trxName );
			log.log (Level.INFO,"Generation - Employee " + Employee.toString() );
	        
			if ( sheetType.equals( P_Time_Sheet.SHEETTYPE_Complementary )) 
			{
		
				TimeSheet = this.getInstance( getCtx (), P_Time_Sheet_ID, m_trxName);
				// A chaque employe on doit initialiser Hourlyrate et assignmentparam
				// Sinon la programmation assume le premier taux trouvé
				Hourly_Rate = null ;
				AssignmentParam = null ;
				
				Hourly_Rate = null ;

				
				PreparedStatement pstmt = null;
				String sql = "Select distinct P_Period_ID From P_Time_Sheet_Detail Where P_Time_Sheet_ID = " + P_Time_Sheet_ID;
				try
				{
					pstmt = DB.prepareStatement (sql, null);
					ResultSet rs = pstmt.executeQuery ();
					while (rs.next ())
					{
						gen_bonus_V2(  rs.getInt("P_Period_ID") );
					}
					rs.close ();
					pstmt.close ();
					pstmt = null;
				}
				catch (Exception e)
				{
					log.log (Level.SEVERE,"GenerationBonusOnly - " + sql, e);
					throw e;
				}

//		        Period = P_Period.get( Env.getCtx(), m_Period.getP_Period_ID(), m_trxName ); 

		    	//
		    	// Les heures non rémunéré coupe le prime sur quantité ainsi que les jours en CSST.
		    	// La différence avec l'autre méthode de coupe, c'est que le jour coupé est dans la même semaine, 
		    	// mais pas nécessairement le même jours.
		        //
//				log.log (Level.INFO,"Generation - Employee " + Employee.toString() + " Cut_Bonus " );
//		        Cut_Bonus();
			}
			log.log (Level.INFO,"Generation Bonus - Employee " + Employee.toString() + " Commit " );

		    if( trx != null ) trx.commit();
	    }
	    catch( Exception e ) {
			log.log (Level.SEVERE, "TimeGeneration", e);
			if( trx != null ) trx.rollback();
	    }
	    finally {
	        if( trx != null ) trx.close();
	    }
		

		int bPeriodId = (Period != null) ? Period.getP_Period_ID() : 0;
		int bEmpId = (Employee != null) ? Employee.getP_Employee_ID() : 0;
		showErrorReportUI(ctx, bPeriodId, bEmpId, P_Time_Sheet_ID);

		return "Enregistrements crs " + m_inserted;
		
		
	}	

	/**
	 * Affiche le rapport d'erreurs/messages P_Time_Sheet_Error a l'ecran en fin de generation.
	 * Utilise la reflexion pour eviter toute dependance circulaire vers le module client.
	 */
	private void showErrorReportUI(Properties ctx, int periodId, int employeeId, int timeSheetId)
	{
		if (m_isWorker)
			return;

		try
		{
			if (java.awt.GraphicsEnvironment.isHeadless())
				return;

			Class<?> clazz = Class.forName("org.solstice.apps.form.VTimeSheetErrorReportDialog");
			java.lang.reflect.Method method = clazz.getMethod("showReportIfErrorsOrNotify", Properties.class, int.class, int.class, int.class);
			method.invoke(null, ctx, periodId, employeeId, timeSheetId);
		}
		catch (ClassNotFoundException e)
		{
			log.info("VTimeSheetErrorReportDialog non present dans le classpath (mode batch/serveur)");
		}
		catch (Throwable t)
		{
			log.log(Level.WARNING, "Impossible d'afficher le rapport d'erreurs: " + t.getMessage(), t);
		}
	}
	private void generate_special_gain( boolean fromBooklet, IBookletTimeSheet TimeSheet) throws Exception
	{
		//
		// Lecture du nombre de période de paie que la feuille traite.
		//
	    Vector<BigDecimal> payPeriodIdList = new Vector<BigDecimal>();
        String sqlPep = "select Distinct P_Period_ID from " + this.getTableDetail() + " where " + this.getTable() +"_ID = " + TimeSheet.getRecord_ID();
        try
        {
            PreparedStatement pstmtPep = DB.prepareStatement(sqlPep, m_trxName );
            ResultSet rsPep = pstmtPep.executeQuery();

            while (rsPep.next())
            {
            	payPeriodIdList.add(rsPep.getBigDecimal(1));
            }

            rsPep.close();
            pstmtPep.close();

        }
        catch (Exception e)
        {
            log.log (Level.SEVERE,"Read Period List", e);
        }

        //
        // Génération des primes, des gain %, etc selon le nombre de période traité sur la feuille de temps. 
        //
        for (int i = 0; i < payPeriodIdList.size(); i++)
        {
        	Period = P_Period.get( Env.getCtx(), ((BigDecimal)payPeriodIdList.get(i)).intValue(), m_trxName );
        	//
        	// La génération a quelques différence si l'on fait un correction période antérieur ou si c'est une génération de la période courante.
        	//
        	if ( m_Period.getP_Period_ID() != Period.getP_Period_ID())
        		gen_special_gain( true, fromBooklet );
        	else
				gen_special_gain( false, fromBooklet );

        	// Generat premimum with fix amount
        	if ( Period.getP_Period_ID() != TimeSheet.getP_Period_ID() && existsStdEarning( TimeSheet.getRecord_ID() ,Period.getP_Period_ID() ))
        	{
        		gen_standard_time_money(106);
        		gen_standard_time_money(104);
        	}
        }

        if ( payPeriodIdList.size() == 0 )
        {
            Period = P_Period.get( Env.getCtx(), TimeSheet.getP_Period_ID(), m_trxName);

			gen_special_gain( false, fromBooklet );
        	
        }

	}

	/**
	 * Cette méthode vérifie s'il existe un livret de temps pour
	 * l'employé et la période demandés. Si oui, elle crée la feuille
	 * de temps selon ce livret.
	 * @return VRAI si le livret a été généré
	 */
	private boolean createFromBooklet(int employeeId, int periodId, int P_Time_Sheet_ID)
	{
	    boolean generated = false;
		// Si on doit générer des feuilles de temps...
	    if(this.generationType == 'S')
	    {
	        // ...on doit d'abord vérifier s'il existe un livret de temps.
	        String sql
	        = "select P_Booklet_ID"
	            + " from P_Booklet"
	            + " where P_Employee_ID = " + employeeId
	            + " and P_Period_ID = " + periodId
	        	+ " and TimeSheetStatus in  ('"+P_Booklet.TIMESHEETSTATUS_Transferred+"','"+P_Booklet.TIMESHEETSTATUS_Calculated+"','"+P_Booklet.TIMESHEETSTATUS_Approved+"')";
	        try
	        {
	            PreparedStatement stmt = DB.prepareStatement(sql, m_trxName);
	            ResultSet rs = stmt.executeQuery();
	            
	            if(rs.next())
	            {
	                // Dans le cas où il existe bel et bien un livret de temps,
	                // on doit simplement transférer l'information du livert
	                // vers la feuille de temps.
	                int timeSheetID = this.copyBookletToTimeSheet(rs.getInt("P_Booklet_ID"), P_Time_Sheet_ID);

	                //2008.11.21
	                TimeSheet = this.getInstance( getCtx (), timeSheetID, m_trxName);
	                generate_special_gain( true, TimeSheet );
	                
	                generated = true;
	            }
	            
	            rs.close();
	            stmt.close();
	        }
	        catch (Exception e)
	        {
	            log.log(Level.SEVERE, "Generation", e);
	        }
	    }

	    return generated;
	}
	
	/**
	 * Cette méthode copie le livret de temps passé en paramètre vers
	 * une nouvelle feuille de temps
	 */
	private int copyBookletToTimeSheet(int bookletId, int P_Time_Sheet_ID)
	{
		int timeSheetID = 0;
	    P_Booklet booklet = new P_Booklet(this.m_ctx, bookletId, m_trxName);

	    P_Time_Sheet timeSheet;
	    if ( P_Time_Sheet_ID == 0 )
	    	timeSheet = new P_Time_Sheet(this.m_ctx, -1, m_trxName);
	    else
	    	timeSheet = new P_Time_Sheet(this.m_ctx, P_Time_Sheet_ID, m_trxName);
	    
	    // On copie l'enregistrement principal du livert de temps
	    // dans une nouvelle feuille de temps
	    timeSheet.setAD_Org_ID(booklet.getAD_Org_ID());
	    timeSheet.setP_Department_ID(booklet.getP_Department_ID());
	    timeSheet.setC_Activity_ID( booklet.getC_Activity_ID());
	    timeSheet.setDescription(booklet.getDescription());
	    timeSheet.setIsActive(booklet.isActive());
	    timeSheet.setIsComplete(booklet.isComplete());
	    timeSheet.setIsError(booklet.isError());
	    timeSheet.setIsWarning(booklet.isWarning());
//	    timeSheet.setP_Distribution_Booklet_ID(booklet.getP_Distribution_Booklet_ID());
	    timeSheet.setP_Distribution_Booklet_ID( Employee.getP_Distribution_Booklet_ID( Period.getEndDate() ));
	    
	    timeSheet.setP_Distribution_ID(booklet.getP_Distribution_ID());
	    timeSheet.setP_Employee_ID(booklet.getP_Employee_ID());
	    timeSheet.setP_Frequency_ID(booklet.getP_Frequency_ID());
	    timeSheet.setP_Job_Title_ID(booklet.getP_Job_Title_ID());
	    timeSheet.setP_Job_Type_ID(booklet.getP_Job_Type_ID());
	    timeSheet.setP_Occupation_Group_ID(booklet.getP_Occupation_Group_ID());
	    timeSheet.setP_Payment_Group_ID(booklet.getP_Payment_Group_ID());
//+ 2011.11.16 ajout numéro employeur et lieu de travail sur la feuille de paie
	    timeSheet.setP_Employer_ID(  Employee.getP_Employer_ID());
	    timeSheet.setP_Workplace_ID( Employee.getP_Workplace_ID());
//-	2011.11.16 			    

	    timeSheet.setP_Payment_ID(booklet.getP_Payment_ID());
	    timeSheet.setP_Period_ID(booklet.getP_Period_ID());
	    timeSheet.setP_Year_ID(booklet.getP_Year_ID());
	    timeSheet.setPaymentType(Employee.getPaymentType());
	    timeSheet.setSheetType(booklet.getSheetType());
	    timeSheet.setP_Collective_Labour_Agr_ID(booklet.getP_Collective_Labour_Agr_ID());
	    timeSheet.setTimeSheetStatus(P_Time_Sheet.TIMESHEETSTATUS_Initial);
	    timeSheet.setP_Booklet_ID( bookletId );
	    
//	    Employee = P_Employee.get( getCtx (), booklet.getP_Employee_ID(), m_trxName );
		String value = DB.getDocumentNo (booklet.getAD_Client_ID() , "P_Time_Sheet", m_trxName );
		Period = P_Period.get( Env.getCtx(), booklet.getP_Period_ID(), m_trxName);
		timeSheet.setValue( Period.getName() + "-" + value.substring( 3, 7 ) );
		
        timeSheet.setPayDate( Period.getPayDate());

//	    timeSheet.setValue(booklet.getValue());
		
		//Mettre le status du livret de temps à jour
//        if ( ! booklet.getTimeSheetStatusHisto( timeSheet.getP_Distribution_Booklet_ID() ).equals(P_Booklet.TIMESHEETSTATUS_Approved))
       	booklet.setTimeSheetStatus(P_Booklet.TIMESHEETSTATUS_Transferred);
	    

	    // Si la sauvegarde a réussi...
	    if(timeSheet.save() && booklet.save())
	    {
			m_inserted++;

			timeSheetID = timeSheet.getP_Time_Sheet_ID(); 

	    	System.out.println("Sauvegarde ok");
	        // ...on récupère le détail du livret et on l'ajoute au détail
	        // de la feuille
	        String sql
	        = "select P_Booklet_Detail_ID"
	            + " from P_Booklet_Detail"
	            + " where P_Booklet_ID = " + bookletId;
	        
	        try
	        {
	            PreparedStatement stmt = DB.prepareStatement(sql, m_trxName);
	            ResultSet rs = stmt.executeQuery();
	            
	            P_Booklet_Detail bookletDetail;
	            P_Time_Sheet_Detail timeSheetDetail;
	            while(rs.next())
	            {
	                bookletDetail = new P_Booklet_Detail(this.m_ctx, rs.getInt("P_Booklet_Detail_ID"), m_trxName);
	                timeSheetDetail = new P_Time_Sheet_Detail(this.m_ctx, -1, m_trxName);
	                
	                // On copie le détail
	                timeSheetDetail.setP_Time_Sheet_ID(timeSheet.getP_Time_Sheet_ID());
	                timeSheetDetail.setC_Activity_ID(bookletDetail.getC_Activity_ID());
	                timeSheetDetail.setC_Campaign_ID(bookletDetail.getC_Campaign_ID());
	                timeSheetDetail.setC_Project_ID(bookletDetail.getC_Project_ID());
	                timeSheetDetail.setDay(bookletDetail.getDay());
	                timeSheetDetail.setIsActive(bookletDetail.isActive());
	                timeSheetDetail.setDayQty(bookletDetail.getDayQty());
	                timeSheetDetail.setOriginTime(bookletDetail.getOriginTime());
	                timeSheetDetail.setP_Assignment_ID(bookletDetail.getP_Assignment_ID());
	                timeSheetDetail.setP_Employee_LongTermLeave_ID(bookletDetail.getP_Employee_LongTermLeave_ID());
	                timeSheetDetail.setP_Gain_ID(bookletDetail.getP_Gain_ID());
	                timeSheetDetail.setP_Period_ID(bookletDetail.getP_Period_ID());
	                timeSheetDetail.setP_Schedule_ID(bookletDetail.getP_Schedule_ID());
	                timeSheetDetail.setSalaryPercentage(bookletDetail.getSalaryPercentage());
	                timeSheetDetail.setWeekIndex(bookletDetail.getWeekIndex());
	                timeSheetDetail.setStartDate(bookletDetail.getStartDate());
	                timeSheetDetail.setEndDate(bookletDetail.getEndDate());
	                timeSheetDetail.setHourly_Rate(bookletDetail.getHourly_Rate());

	                if(!timeSheetDetail.save())
	                {
	    		        log.log(Level.SEVERE, "copyBookletToTimeSheet");
	                }
	            }
	            
	            rs.close();
	            stmt.close();
	        }
	        catch (SQLException e)
	        {
		        log.log(Level.SEVERE, "copyBookletToTimeSheet", e);
	        }
	    }
	    else
	    {
	        log.log(Level.SEVERE, "copyBookletToTimeSheet");
	    }
	    return timeSheetID;
	}
	
	private void gen_holidays_calendar ()
	{
		// on verifie sil faut payer le ferie et si le conge est ferie
		// sinon on ne génère pas de gain
		if( ! Employee.isRemunerateHoliday() )
		{
			return;
		}
		
		int Activity_ID       = 0;
		String sql = null;
		sql = "Select P_NonBusinessDay_ID, NonBusinessDate From " 
			+ " P_Holidays_Calendar, "
            + " P_Holidays_Year, "
	        + " P_NonBusinessDay "
            + " WHERE P_Holidays_Year.P_Holidays_Calendar_ID = P_Holidays_Calendar.P_Holidays_Calendar_ID "
	        + " AND P_NonBusinessDay.P_Holidays_Year_ID = P_Holidays_Year.P_Holidays_Year_ID "
            + " AND P_NonBusinessDay.NonBusinessDate Between " + DB.TO_DATE( Period.getStartDate() ) + " and " + DB.TO_DATE( Period.getEndDate() )
		    + " AND P_Holidays_Calendar.P_Holidays_Calendar_ID=" + Employee.getP_Holidays_Calendar_ID();
		
		OriginTime = "FER";

		PreparedStatement pstmt  = null;
		try
		{
			pstmt = DB.prepareStatement (sql, m_trxName);
			ResultSet rs = pstmt.executeQuery ();
			
			P_NonBusinessDay NonBusinessDay;
			P_Assignment Assignment;
			
			while (rs.next ())
			{
				NonBusinessDay = P_NonBusinessDay.get( getCtx(), rs.getInt(1), m_trxName);
				
//				int Gain_Account_ID   = 0;
//				P_Gain_Account GainAccount = P_Gain_Account.get( getCtx () , NonBusinessDay.getP_Gain_ID(), Employee.getP_Occupation_Group_ID(), Employee.getP_Job_Type_ID(), Employee.getP_Job_Title_ID() , trxName);
//				Activity_ID = GainAccount.getC_Activity_ID();

				PeriodDate = NonBusinessDay.getNonBusinessDate();

				//2011.05.24 ajout de la condition pour Helico, ne pas généré les férié si le jour est déjà en ALD
				//           la condition s'applique uniquement pour les systéme qui on le temps sommaire.
/*				if (  this.isLongTermLeave( PeriodDate, PeriodDate ) )
					;
				else
*/				 
				if (( Employee.getDateHired().compareTo( PeriodDate ) > 0 ) || ( Employee.getDateLayoff() != null && Employee.getDateLayoff().compareTo( Period.getStartDate() ) >= 0 && Employee.getDateLayoff().compareTo( PeriodDate) <= 0 ))
					;
				else
				{
					GenStartDate  = PeriodDate;
					GenEndDate    = PeriodDate;
					weekNumber = getWeek(new BigDecimal( TimeUtil.getDaysBetween(Period.getStartDate(), PeriodDate) +1 ) .divide(new BigDecimal(7), 0, BigDecimal.ROUND_UP).intValue());
//					weekNumber = (TimeUtil.getDaysBetween( Period.getStartDate(), PeriodDate) /7 ) +1;

					if ( Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ) != 0)
						AssignmentParam = P_Assignment_Param.get( getCtx(), Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ), GenStartDate, m_trxName );
					else 
						AssignmentParam = Employee.getLastEmployeeAssignment(Employee.getP_Employee_ID(), PeriodDate, m_trxName);
				
					Gain = P_Gain.get( getCtx(), NonBusinessDay.getP_Gain_ID(), m_trxName );

			        // SM 2012-11-29 Pour la méthode 121		
					ArrayList admissiblevalue = new ArrayList<BigDecimal>(2);

					//2024-01-10 AJOUT de la méthode 110 comme condition pour les cammionneur.
					//|| Gain.getP_Method_Gain_ID() == 110
					if ( Gain.getP_Method_Gain_ID() == 121 )
					{
						//Dayqty = Employee.getIndemnityStatutoryHolidays( l_Period.getP_Period_ID());
						admissiblevalue = Employee.getIndemnityStatutoryHolidays( Period.getStartDate(), rs.getTimestamp("NonBusinessDate"), AssignmentParam.getP_Assignment_Param_ID(), Gain.getP_Column_Adm_ID(), null, m_trxName );
						
						Dayqty = (BigDecimal)admissiblevalue.get(0);
						
						//2023-09-12 pour la paie des chaufeurs.
						if ( Employee.getStatutoryHolidayGenerationType().equals(P_Employee.STATUTORYHOLIDAYGENERATIONTYPE_110thOfHoursAndAmount))
						{
							Dayqty = (BigDecimal)admissiblevalue.get(1);
						}
						if ( Employee.getStatutoryHolidayGenerationType().equals(P_Employee.STATUTORYHOLIDAYGENERATIONTYPE_120thOfHoursAndAmount))
						{
							Dayqty = (BigDecimal)admissiblevalue.get(1);
						}
						if ( Employee.getStatutoryHolidayGenerationType().equals(P_Employee.STATUTORYHOLIDAYGENERATIONTYPE_160thOfHoursAndAmount))
						{
							Dayqty = (BigDecimal)admissiblevalue.get(1);
						}
							

						
					}
					// Spécial pour Krispy Kernels
					else if ( ( Gain.getValue().equals("GC02") ||
							  Gain.getValue().equals("GC05") ) 
							&& Employee.getStatutoryHolidayGenerationType().equals(P_Employee.STATUTORYHOLIDAYGENERATIONTYPE_MoyenSur37Hours) )
					{
						admissiblevalue = Employee.getIndemnityStatutoryHolidays( Period.getStartDate(), rs.getTimestamp("NonBusinessDate"), AssignmentParam.getP_Assignment_Param_ID(), Gain.getP_Column_Adm_ID(), null, m_trxName );
						
						Dayqty = (BigDecimal)admissiblevalue.get(0);
					}
					
					else
					{
						Dayqty     = AssignmentParam.getDay_Hours();
					}	
					
			        GregorianCalendar cal = new GregorianCalendar();
			        cal.setTime( (Date)PeriodDate );

			        Assignment = P_Assignment.get( Env.getCtx(), AssignmentParam.getP_Assignment_ID(), m_trxName );
			        //si l'employé est en ARTT il est possible que l'on doivent généré un gain pour 
			        // les fériés différents, si le jours du ARTT arrive le jours du férié.
					if ( Assignment.isRWT( PeriodDate) && Assignment.dayRWT( PeriodDate ) == cal.get(Calendar.DAY_OF_WEEK) )
					{
						P_Gain GainRef = P_Gain.get( getCtx(), NonBusinessDay.getP_Gain_ID(), m_trxName );
						if ( GainRef.getP_Gain_Compensation() != 0)
						{
							Gain = P_Gain.get( getCtx(), GainRef.getP_Gain_Compensation(), m_trxName );
							//TODO ne pas harcodé le %
							// 20 % des heures travailler hebdomadaire.
							Dayqty = Assignment.getWeekly_HoursRWT( PeriodDate ).multiply( new BigDecimal(0.20)).setScale(2,BigDecimal.ROUND_HALF_UP);
						}
					}
					else if ( Assignment.isRWT( PeriodDate))
					{
						Dayqty = Assignment.getDay_HoursRWT( PeriodDate);
						Gain = P_Gain.get( getCtx(), NonBusinessDay.getP_Gain_ID(), m_trxName );
					}
					else
						Gain = P_Gain.get( getCtx(), NonBusinessDay.getP_Gain_ID(), m_trxName );

					//PATH
					if ( Employee.getStatutoryHolidayGenerationType().equals(P_Employee.STATUTORYHOLIDAYGENERATIONTYPE_110thOfHoursAndAmount))
					{
						Gain = P_Gain.getWithValue(Env.getCtx(), "GC03M", null);
					}
					if ( Employee.getStatutoryHolidayGenerationType().equals(P_Employee.STATUTORYHOLIDAYGENERATIONTYPE_120thOfHoursAndAmount))
					{
						Gain = P_Gain.getWithValue(Env.getCtx(), "GC03M", null);
					}
					if ( Employee.getStatutoryHolidayGenerationType().equals(P_Employee.STATUTORYHOLIDAYGENERATIONTYPE_160thOfHoursAndAmount))
					{
						Gain = P_Gain.getWithValue(Env.getCtx(), "GC03M", null);
					}
					
					
					Schedule = P_Schedule.get( getCtx(), AssignmentParam.getP_Schedule_ID(), m_trxName );
					if ( Dayqty.compareTo( Env.ZERO ) != 0 )
						create_time_sheet_detail ( Period.getP_Period_ID() );
					
				}
				
				
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"holidays_calendar - " + sql, e);
		}
		
		return ;
		
	}   //	
	
	private void gen_employee_calendar ( )
	{
		
		String sql = null;
		sql = "Select P_Employee_NonBusinessDay_ID  "                                                                          
			+ " From P_Employee_Calendar     "                                                
	        + "	 inner join P_Employee_NonBusinessDay ON  P_Employee_NonBusinessDay.P_Employee_Calendar_ID = P_Employee_Calendar.P_Employee_Calendar_ID"
	        + "   left outer join P_Absence_Detail ON P_Absence_Detail.P_Absence_Detail_ID = P_Employee_NonBusinessDay.P_Absence_Detail_ID "
            + " WHERE P_Employee_Calendar.P_Employee_ID=" + Employee.getP_Employee_ID()
			+ " AND ( P_Employee_NonBusinessDay.NonBusinessDate Between " + DB.TO_DATE( Period.getStartDate() ) + " and " + DB.TO_DATE( Period.getEndDate() )
			+ "        OR ( P_Absence_Detail.Processed = 'N' AND P_Absence_Detail.absenceDate < " + DB.TO_DATE(Period.getStartDate()) + ")  "
			+ "        OR ( P_Absence_Detail.P_Period_ID = " + Period.getP_Period_ID() + " ) )"
			;
		OriginTime = "CAL";
		int Period_ID = Period.getP_Period_ID();

		PreparedStatement pstmt  = null;
		try
		{
			pstmt = DB.prepareStatement (sql, m_trxName);
			ResultSet rs = pstmt.executeQuery ();
			
			P_Period l_Period = P_Period.get( Env.getCtx(), Period_ID, m_trxName);
			
			P_Employee_NonBusinessDay EmployeeNonBusinessDay;
			P_Absence_Detail AbsenceDetail;
			while (rs.next ())
			{

				EmployeeNonBusinessDay = P_Employee_NonBusinessDay.get( getCtx(), rs.getInt(1), m_trxName);

				PeriodDate = EmployeeNonBusinessDay.getNonBusinessDate();
				l_Period = P_Period.get( Env.getCtx(), PeriodDate, m_trxName);
				GenStartDate  = PeriodDate;
				GenEndDate    = PeriodDate;
//2008-08-11 
				weekNumber = getWeek(new BigDecimal( TimeUtil.getDaysBetween(Period.getStartDate(), PeriodDate) +1 ) .divide(new BigDecimal(7), 0, BigDecimal.ROUND_UP).intValue());
//				weekNumber = new BigDecimal( TimeUtil.getDaysBetween(l_Period.getStartDate(), PeriodDate) +1 ) .divide(new BigDecimal(7), 0, BigDecimal.ROUND_UP).intValue();
//				weekNumber = (TimeUtil.getDaysBetween( Period.getStartDate(), PeriodDate) /7 ) +1;

				if ( Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ) != 0)
					AssignmentParam = P_Assignment_Param.get( getCtx(), Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ), GenStartDate, m_trxName );
				else 
					AssignmentParam = Employee.getLastEmployeeAssignment(Employee.getP_Employee_ID(), PeriodDate, m_trxName);
				
				if ( EmployeeNonBusinessDay.getDayQty().compareTo( ZERO ) != 0 )
					Dayqty     = EmployeeNonBusinessDay.getDayQty();
				else
					Dayqty     = AssignmentParam.getDay_Hours();

				Gain = P_Gain.get( getCtx(), EmployeeNonBusinessDay.getP_Gain_ID(), m_trxName );

				Schedule = P_Schedule.get( getCtx(), AssignmentParam.getP_Schedule_ID(), m_trxName );
				create_time_sheet_detail ( l_Period.getP_Period_ID() );
				if ( EmployeeNonBusinessDay.getP_Absence_Detail_ID() != 0)
				{
					AbsenceDetail = P_Absence_Detail.get( getCtx(), EmployeeNonBusinessDay.getP_Absence_Detail_ID(), m_trxName );
					AbsenceDetail.setProcessed( true );
					AbsenceDetail.setP_Period_ID( Period_ID );
					AbsenceDetail.save();
				}

			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"Employee_calendar - " + sql, e);
		}
		
		
		return ;
		
	}   //	




	// Retourne si l'employé est en absence depuis le début de la période 
/*	private boolean isLongTermLeave()
	{
		if ( Env.getAD_Client_ID(Env.getCtx()) == 11)
			return false;
		
		String sql = null;
		sql = "Select P_Employee_LongTermLeave_ID "                                                                          
			+ " From P_Employee_LongTermLeave "
            + " WHERE P_Employee_LongTermLeave.P_Employee_ID=" + Employee.getP_Employee_ID()
			+ " AND IsActive = 'Y' "
			+ " AND StartDate <= " + DB.TO_DATE( Period.getStartDate() )
			+ " AND ( EndDate is null OR EndDate >= " + DB.TO_DATE( Period.getEndDate() ) 
			//2011.05.09 - 1518 2 ALD consécutif
//  		    + "  OR exists( select 1 from P_Employee_LongTermLeave m where m.P_Employee_ID = P_Employee_LongTermLeave.P_Employee_ID and m.startDate = P_Employee_LongTermLeave.Enddate + 1  )"
			+ ") "
			;

		Boolean result = false;
		
		PreparedStatement pstmt  = null;
		try
		{
			pstmt = DB.prepareStatement (sql, m_trxName);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				result = true;
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;

		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"isLongTermLeave - " + sql, e);
		}
	
		
		return result;
	}
*/
	// Retourne si l'employé est en absence depuis le début de la période 
	private boolean isLongTermLeave( Timestamp StartDate, Timestamp EndDate )
	{
        String client = PgiUtil.getSolsticeParameter(getCtx(),"Client");
        if ( client.equals("SIQ") ||  client.equals("krispy") )
        {
			return false;
        }
		
		String sql = null;
		sql = "Select P_Employee_LongTermLeave_ID "                                                                          
			+ " From P_Employee_LongTermLeave "
            + " WHERE P_Employee_LongTermLeave.P_Employee_ID=" + Employee.getP_Employee_ID()
			+ " AND IsActive = 'Y' "
			+ " AND StartDate <= " + DB.TO_DATE( StartDate )
			+ " AND ( EndDate is null OR EndDate >= " + DB.TO_DATE( EndDate )
			//2011.05.09 - 1518 2 ALD consécutif
//  		    + "  OR exists( select 1 from P_Employee_LongTermLeave m where m.P_Employee_ID = P_Employee_LongTermLeave.P_Employee_ID and m.startDate = P_Employee_LongTermLeave.Enddate + 1  )"
			+ ") "
			;

		Boolean result = false;
		
		PreparedStatement pstmt  = null;
		try
		{
			pstmt = DB.prepareStatement (sql, m_trxName);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				result = true;
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;

		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"isLongTermLeave - " + sql, e);
		}
	
		
		return result;
	}

	private void gen_longTermLeave ( )
	{
		String sql = null;
		sql = "Select P_Employee_LongTermLeave_ID "                                                                          
			+ " From P_Employee_LongTermLeave "
            + " WHERE P_Employee_LongTermLeave.P_Employee_ID=" + Employee.getP_Employee_ID()
			+ " AND IsActive = 'Y' "
			;


		OriginTime = "ALD";

//		log.debug( "Read Employee Calendar - Sql : " + sql );

		PreparedStatement pstmt  = null;
		try
		{
			pstmt = DB.prepareStatement (sql, m_trxName);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{

				//+ 2009.12.21
				Hourly_Rate = null ;
				//- 2009.12.21
				
				EmployeeLongTermLeave = P_Employee_LongTermLeave.get( getCtx(), rs.getInt(1), m_trxName);

				Timestamp StartDate = Period.getStartDate();
				Timestamp EndDate = Period.getEndDate();

/*				if ( EmployeeLongTermLeave.getStartDate().compareTo( Period.getEndDate()) > 0)
				{
					StartDate = null;
					EndDate = null;
				}
*/				

				if ( EmployeeLongTermLeave.getStartDate().compareTo( Period.getStartDate()) > 0)
					StartDate = EmployeeLongTermLeave.getStartDate();
				
				
				if ( EmployeeLongTermLeave.getEndDate() != null && EmployeeLongTermLeave.getEndDate().compareTo( Period.getEndDate()) < 0)
					EndDate = EmployeeLongTermLeave.getEndDate();


                if ( EmployeeLongTermLeave.getWeekNumber() != null && Integer.parseInt( EmployeeLongTermLeave.getWeekNumber()) != 0 )
                {
                	// Semaine 1 - i = 0
                	// Semaine 2 - i = 1
                	// Selon la semaine, la date de début et de fin modifier.
                	//
                	//TODO Autre que 2 semaines.
                	int i = Integer.parseInt( EmployeeLongTermLeave.getWeekNumber() ) -1 ;
                	if ( i == 0)
                	{
                    	EndDate   = TimeUtil.addDays( StartDate, ((i+1) * 7)-1 );
                	}
                	else
                	{
                		StartDate = TimeUtil.addDays( StartDate, (i * 7)   );
                	}
                }

				int nbrDays = TimeUtil.getDaysBetween( StartDate , EndDate ) + 1;

				if ( nbrDays > 0 )
				{

    				// mode sommaire (pas détaillé a la journée)
        	        if (DisplayDetail == 1) // ***
        	        {
        	        	ald_method_03(nbrDays, StartDate, EndDate);
        	        }

					//
					// S'il y a un montant on doit généré seulement 2 lignes, et 1 seul ligne s'il y a une semaine de mentionner.
					//
        	        else if ( EmployeeLongTermLeave.getAmount() != null && EmployeeLongTermLeave.getAmount().compareTo( Env.ZERO) != 0)
			        {
						ald_method_01(nbrDays, StartDate, EndDate);

			        }
					else
					{
						ald_method_02( nbrDays, StartDate, EndDate);
					}
					
				}
					
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			EmployeeLongTermLeave = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"Employee_longTermLeave - " + sql, e);
		}
	
		
	}
	

	private void ald_method_01(int nbrDays, Timestamp StartDate, Timestamp EndDate)
	{
		
		// todo... autre que 2 semaines.
//		int nbrWeek = 2;
        int nbrWeek = getWeek(new BigDecimal( TimeUtil.getDaysBetween(StartDate, EndDate) +1 ) .divide(new BigDecimal(7), 0, BigDecimal.ROUND_UP).intValue());
		if ( EmployeeLongTermLeave.getWeekNumber() != null )
		{
			nbrWeek = 1;
		}

		if ( nbrWeek == 2 && EndDate.compareTo( TimeUtil.addDays( StartDate, 7 )) < 0 )
		{
			nbrWeek = 1;
		}
		
		int nbrDaysAld = 0;

		

		for (int j = 0; j < nbrWeek ; j++)
		{
			int nday = 7 ;     
			StartDate = TimeUtil.addDays( StartDate, j*7 );

			//
			// Si la date de début du ALD est plus grande que la date de début de période.
			//
			if ( StartDate.compareTo( Period.getStartDate()) != 0 && j == 0)
			{
				nday = TimeUtil.getDaysBetween( StartDate, TimeUtil.addDays( Period.getStartDate(), 7 ) ); // + 1;
				//TODO revoir si plus de 2 semaines de paie
				if ( nday < 0 )
				{
					nday = TimeUtil.getDaysBetween( StartDate, Period.getEndDate() ) + 1;
					j =+ 1;
				}
			}
			BigDecimal amt = EmployeeLongTermLeave.getAmount();

			//
			// Si la date de fin du ALD est plus petit que la date de fin de la semaine.
			//
			if ( EmployeeLongTermLeave.getEndDate() != null && EmployeeLongTermLeave.getEndDate().compareTo( TimeUtil.addDays( StartDate, j*7 ) ) < 0 )
			{
				nday = TimeUtil.getDaysBetween( StartDate, EmployeeLongTermLeave.getEndDate() ) + 1;
			}
			
			Dayqty = Env.ZERO;
			for (int i = 0; i < nday ; i++)  // nbrDays
			{
				PeriodDate = TimeUtil.addDays( StartDate, i );
				nbrDaysAld = TimeUtil.getDaysBetween( EmployeeLongTermLeave.getStartDate() , PeriodDate )+1;
				weekNumber = getWeek(j+1); //(TimeUtil.getDaysBetween( Period.getStartDate(), PeriodDate) /7 ) +1;
				
				// 	on exclue les samedi dimande
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTimeInMillis( PeriodDate.getTime());

				if ( cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY )
				{
					// TODO revoir si plus de 2 semaines de paie.
					// Le montant est diviser par 10 pour connaitre le montant par jours. 
					if ( EmployeeLongTermLeave.getP_Gain_ID( nbrDaysAld ) != 0 )
					{
						Dayqty = Dayqty.add( amt.divide( new BigDecimal(10),8,BigDecimal.ROUND_HALF_UP));
					}
				}
			}
			GenStartDate  = PeriodDate;
			GenEndDate    = PeriodDate;

			Dayqty = Dayqty.setScale(2, BigDecimal.ROUND_HALF_UP);
			
			Gain = P_Gain.get( getCtx(), EmployeeLongTermLeave.getP_Gain_ID( nbrDaysAld ), m_trxName );

			if ( AssignmentParam == null)
			{
				if ( Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ) != 0)
					AssignmentParam = P_Assignment_Param.get( getCtx(), Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ), GenStartDate, m_trxName );
				else 
					AssignmentParam = Employee.getLastEmployeeAssignment(Employee.getP_Employee_ID(), PeriodDate, m_trxName);
				
			}


			Schedule = P_Schedule.get( getCtx(), AssignmentParam.getP_Schedule_ID(), m_trxName );
			if ( Dayqty.compareTo(Env.ZERO) != 0 ) 
				create_time_sheet_detail ( Period.getP_Period_ID() );
		}
	}
	
	private void ald_method_02( int nbrDays, Timestamp StartDate, Timestamp EndDate)
	{
		for (int i = 0; i < nbrDays ; i++)
		{
			PeriodDate = TimeUtil.addDays( StartDate, i);

			GenStartDate  = PeriodDate;
			GenEndDate    = PeriodDate;

			int nbrDaysAld = TimeUtil.getDaysBetween( EmployeeLongTermLeave.getStartDate() , PeriodDate ) ;
//			weekNumber = (TimeUtil.getDaysBetween( Period.getStartDate(), PeriodDate) /7 ) +1;
			weekNumber = getWeek(new BigDecimal( TimeUtil.getDaysBetween(Period.getStartDate(), PeriodDate) +1 ).divide(new BigDecimal(7), 0, BigDecimal.ROUND_UP).intValue());
			if ( Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ) != 0)
				AssignmentParam = P_Assignment_Param.get( getCtx(), Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ), GenStartDate, m_trxName );
			else 
				AssignmentParam = Employee.getLastEmployeeAssignment(Employee.getP_Employee_ID(), PeriodDate, m_trxName);

			Dayqty     = AssignmentParam.getDay_Hours();

	        GregorianCalendar cal = new GregorianCalendar();
	        cal.setTime( (Date)PeriodDate );
	        //
	        // Si c'est un montant, on arrete la boucle sur le nombre de jour
	        //
			switch ( cal.get(Calendar.DAY_OF_WEEK) ) 
			{
				case Calendar.SUNDAY : 	
					Dayqty = EmployeeLongTermLeave.getSunday();
					break;
				case Calendar.MONDAY : 	
					Dayqty = EmployeeLongTermLeave.getMonday();
					break;
				case Calendar.TUESDAY : 	
					Dayqty = EmployeeLongTermLeave.getTuesday();
					break;
				case Calendar.WEDNESDAY : 	
					Dayqty = EmployeeLongTermLeave.getWednesday();
					break;
				case Calendar.THURSDAY : 	
					Dayqty = EmployeeLongTermLeave.getThursday();
					break;
				case Calendar.FRIDAY : 	
					Dayqty = EmployeeLongTermLeave.getFriday();
					break;
				case Calendar.SATURDAY : 	
					Dayqty = EmployeeLongTermLeave.getSaturday();
					break;
			}

			if ( EmployeeLongTermLeave.getP_Gain_ID( nbrDaysAld ) != 0 )
			{
				BigDecimal DayqtyTmp = Dayqty; 
				Gain = P_Gain.get( getCtx(), EmployeeLongTermLeave.getP_Gain_ID( nbrDaysAld ), m_trxName );
				Schedule = P_Schedule.get( getCtx(), AssignmentParam.getP_Schedule_ID(), m_trxName );
				if ( Dayqty.compareTo(Env.ZERO) != 0 ) 
					create_time_sheet_detail ( Period.getP_Period_ID() );
				
				//
				// Génération des gains de compensation de férier pour les employés en assurances salaires.
				//
				if ( Gain.getP_Gain_Compensation() != 0 && isHolidaysDay( PeriodDate ) )
				{
					P_Collective_Labour_Agr CollectiveLabourArg = P_Collective_Labour_Agr.get( Env.getCtx(), AssignmentParam.getP_Collective_Labour_Agr_ID(), m_trxName);
					
					if ( CollectiveLabourArg.isPayCompForNonBuninessDay())
					{
						P_Gain GainRef = Gain;
						Gain = P_Gain.get( getCtx(), GainRef.getP_Gain_Compensation(), m_trxName );
						Schedule = P_Schedule.get( getCtx(), AssignmentParam.getP_Schedule_ID(), m_trxName );
						Dayqty = DayqtyTmp;
						create_time_sheet_detail ( Period.getP_Period_ID() );
					}
				}
			
			}

		}
	
	}

	
	private void ald_method_03(int nbrDays, Timestamp StartDate, Timestamp EndDate)
	{
		PeriodDate = StartDate ;
		weekNumber = 1;
		int nbrDaysAld = 0;

		Dayqty = Env.ZERO;
		BigDecimal amt = EmployeeLongTermLeave.getAmount();

		if ( Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ) != 0)
			AssignmentParam = P_Assignment_Param.get( getCtx(), Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ), GenStartDate, m_trxName );
		else 
			AssignmentParam = Employee.getLastEmployeeAssignment(Employee.getP_Employee_ID(), PeriodDate, m_trxName);

		if ( amt.compareTo( Env.ZERO) == 0 )
		{
			amt = AssignmentParam.getHoursPerPay();
		}
		
		BigDecimal nbrWorkDay = Period.getNbrWorkDay( );
		
		if ( nbrWorkDay != null && amt != null)
			amt = amt.divide( nbrWorkDay, 8, BigDecimal.ROUND_HALF_UP );
		else 
			amt = Env.ZERO;

		//
		//
		int nday = TimeUtil.getDaysBetween( StartDate, EndDate ) + 1;

		for (int i = 0; i < nday ; i++)  // nbrDays
		{
			PeriodDate = TimeUtil.addDays( StartDate, i );
			nbrDaysAld = TimeUtil.getDaysBetween( EmployeeLongTermLeave.getStartDate() , PeriodDate )+1;
			
			// 	on exclue les samedi dimande
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTimeInMillis( PeriodDate.getTime());

			if ( cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY )
			{
				if ( EmployeeLongTermLeave.getP_Gain_ID( nbrDaysAld ) != 0 )
				{
					Dayqty = Dayqty.add( amt );
				}
			}
		}

		GenStartDate  = StartDate;
		GenEndDate    = EndDate;

		Dayqty = Dayqty.setScale(2, BigDecimal.ROUND_HALF_UP);
		
		Gain = P_Gain.get( getCtx(), EmployeeLongTermLeave.getP_Gain_ID( nbrDaysAld ), m_trxName );
		Schedule = P_Schedule.get( getCtx(), AssignmentParam.getP_Schedule_ID(), m_trxName );
		if ( Dayqty.compareTo(Env.ZERO) != 0 ) 
			create_time_sheet_detail ( Period.getP_Period_ID() );
	}

	private boolean isHolidaysDay( Timestamp PeriodDate )
	{
		String sql = "Select P_NonBusinessDay_ID From " 
			+ " P_Holidays_Calendar, "
            + " P_Holidays_Year, "
	        + " P_NonBusinessDay "
            + " WHERE P_Holidays_Year.P_Holidays_Calendar_ID = P_Holidays_Calendar.P_Holidays_Calendar_ID "
	        + " AND P_NonBusinessDay.P_Holidays_Year_ID = P_Holidays_Year.P_Holidays_Year_ID "
            + " AND P_NonBusinessDay.NonBusinessDate = " + DB.TO_DATE( PeriodDate )
		    + " AND P_Holidays_Calendar.P_Holidays_Calendar_ID=" + Employee.getP_Holidays_Calendar_ID();
		
		boolean ret = false;
		
		PreparedStatement pstmt  = null;
		try
		{
			pstmt = DB.prepareStatement (sql, m_trxName);
			ResultSet rs = pstmt.executeQuery ();
			
			if (rs.next ())
			{
				ret = true;
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"holidays_calendar - " + sql, e);
		}
		
		return ret;	
	}
	
	private void gen_standard_time ()
	{
		// le order by sert a faire la génération du temps standard avant les lignes de Traitement différé.
		String sql = "Select P_Standard_Time_ID "                                                                          
				+ " From P_Standard_Time, P_Gain "
		        + " WHERE P_Standard_Time.P_Employee_ID=" + Employee.getP_Employee_ID()
		        + " AND P_Standard_Time.isActive = 'Y'"
		        + " AND ( P_Standard_Time.EndDate is null OR P_Standard_Time.EndDate >=" + DB.TO_DATE(Period.getStartDate()) +  ")"
		        + " AND ( P_Standard_Time.StartDate is null OR P_Standard_Time.StartDate <=" + DB.TO_DATE(Period.getEndDate()) +  ")"
		        + " AND P_Gain.P_Gain_ID = P_Standard_Time.P_Gain_ID "
		        + " AND (P_Method_Gain_ID not in ( 105, 107, 113, 213, 116, 118, 300 )) " // P_UOM_ID = 100 AND 
				+ " ORDER BY P_SelfFinancedLeave_ID "
				;

//		+ " AND " + weekNumber + " = isnull( weekNumber, " + weekNumber + " ) "

	    boolean oneweek = false;

	    
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, m_trxName);
			ResultSet rs = pstmt.executeQuery ();
			
			P_Standard_Time StandardTime;
			while (rs.next ())
			{
				StandardTime = P_Standard_Time.get( getCtx(), rs.getInt("P_Standard_Time_ID"), m_trxName);
				
				Timestamp StartDate = Period.getStartDate();
				Timestamp EndDate = Period.getEndDate();

				PeriodDate = Period.getStartDate();
				GenStartDate  = PeriodDate;
				GenEndDate    = PeriodDate;

				if ( StandardTime.getP_SelfFinancedLeave_ID() != 0 )
				{
					OriginTime = "TRD";
					SelfFinancedLeave = P_SelfFinancedLeave.get( getCtx(), StandardTime.getP_SelfFinancedLeave_ID(), m_trxName );
				}
				else
				{
					OriginTime = "STD";
					SelfFinancedLeave = null;
				}


				Gain = P_Gain.get( getCtx(), StandardTime.getP_Gain_ID(), m_trxName );

//				Activity = new MActivity( getCtx(), StandardTime.get) 


				//
				// On vérifie les dates de débuts et de fin du temps standard
				// S'il ne sont pas inclus dans les dates de la période.
				//
				if ( StandardTime.getStartDate() != null && StandardTime.getStartDate().compareTo( Period.getStartDate()) > 0)
					StartDate = StandardTime.getStartDate();
				
				if ( StandardTime.getEndDate() != null && StandardTime.getEndDate().compareTo( Period.getEndDate()) < 0)
					EndDate = StandardTime.getEndDate();

				if ( Employee.getDateLayoff() != null && Employee.getDateLayoff().compareTo( Period.getStartDate() ) >= 0 && Employee.getDateLayoff().compareTo( PeriodDate) <= 0 )
					EndDate = Employee.getDateLayoff();

                if ( StandardTime.getWeekNumber() != null && Integer.parseInt( StandardTime.getWeekNumber()) != 0 )
                {
                	// Semaine 1 - i = 0
                	// Semaine 2 - i = 1
                	// Selon la semaine, la date de début et de fin modifier.
                	//
                	//TODO Autre que 2 semaines.
                	int i = Integer.parseInt( StandardTime.getWeekNumber() ) -1 ;
                	if ( i == 0)
                	{
                    	EndDate   = TimeUtil.addDays( StartDate, ((i+1) * 7)-1 );
                	}
                	else
                	{
                		StartDate = TimeUtil.addDays( StartDate, (i * 7)   );
                	}
                }
                //DisplayDetail
                int nbrDays = 1; // Ajout Gen ***
                if (DisplayDetail == 0) // ***
                {
                	 nbrDays = TimeUtil.getDaysBetween( StartDate , EndDate ) + 1;           	
                }
                
				//int nbrDays = TimeUtil.getDaysBetween( StartDate , EndDate ) + 1;
				
				for (int i = 0; i < nbrDays ; i++)
				{
					// Reset a cause des employé en ARTT.
					if ( StandardTime.getP_Gain_ID() != Gain.getP_Gain_ID())
						Gain = P_Gain.get( getCtx(), StandardTime.getP_Gain_ID(), m_trxName );
					
					PeriodDate = TimeUtil.addDays( StartDate, i);
	                if (DisplayDetail == 0) // ***
	                {
						GenStartDate  = PeriodDate;
						GenEndDate    = PeriodDate;	                	
	                }
	                else
	                {
						GenStartDate  = StartDate;
						GenEndDate    = EndDate;                
	                }

					weekNumber = getWeek(new BigDecimal( TimeUtil.getDaysBetween(Period.getStartDate(), PeriodDate) +1 ).divide(new BigDecimal(7), 0, BigDecimal.ROUND_UP).intValue());
					// weekNumber = (TimeUtil.getDaysBetween( Period.getStartDate(), PeriodDate) /7 ) +1;

			        GregorianCalendar cal = new GregorianCalendar();
			        cal.setTime( (Date)PeriodDate );
					switch ( cal.get(Calendar.DAY_OF_WEEK) ) 
					{
						case Calendar.SUNDAY : 	
							Dayqty = StandardTime.getSunday();
							break;
						case Calendar.MONDAY : 	
							Dayqty = StandardTime.getMonday();
							break;
						case Calendar.TUESDAY : 	
							Dayqty = StandardTime.getTuesday();
							break;
						case Calendar.WEDNESDAY : 	
							Dayqty = StandardTime.getWednesday();
							break;
						case Calendar.THURSDAY : 	
							Dayqty = StandardTime.getThursday();
							break;
						case Calendar.FRIDAY : 	
							Dayqty = StandardTime.getFriday();
							break;
						case Calendar.SATURDAY : 	
							Dayqty = StandardTime.getSaturday();
							break;
					}
					// Ajout Stéphane Morin 25 septembre 2007
					// .equals au lieu de == pour bien traiter chaîne de caractères
					if ( Gain.getP_UOM_ID() != 100 || PaymentGroup.getTimeSheetModel().equals("1")  )
					{
						Dayqty = StandardTime.getStandard_Time_Amount();
					}

					//					Gain = P_Gain.get( getCtx(), StandardTime.getP_Gain_ID(), m_trxName );

//	                AssignmentParam = P_Assignment_Param.get( getCtx(), rs.getInt("P_Assignment_ID"), GenStartDate, m_trxName );

					Hourly_Rate = StandardTime.getHourly_Rate();
					
					if ( StandardTime.getP_Assignment_ID() != 0 )
					{
		                AssignmentParam = P_Assignment_Param.get( getCtx(), StandardTime.getP_Assignment_ID(), GenStartDate, m_trxName );
					}
					else
					{
						if ( Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ) != 0)
							AssignmentParam = P_Assignment_Param.get( getCtx(), Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ), GenStartDate, m_trxName );
						else 
							AssignmentParam = Employee.getLastEmployeeAssignment(Employee.getP_Employee_ID(), PeriodDate, m_trxName);

					}
                    P_Assignment Assignment = P_Assignment.get( Env.getCtx(), AssignmentParam.getP_Assignment_ID(), m_trxName);


                    //+ 2008-04-24
                    if ( PaymentGroup.getTimeSheetModel().equals(P_Payment_Group.TIMESHEETMODEL_Summary))
                    {
        				BigDecimal nbrWorkDay = Period.getNbrWorkDay( );
        				BigDecimal nbrWorkDayEmployee = Period.getNbrWorkDay( GenStartDate, GenEndDate);

                        if ( nbrWorkDay.compareTo(nbrWorkDayEmployee) != 0)
                        	Dayqty = (Dayqty.divide(nbrWorkDay,8,BigDecimal.ROUND_HALF_UP)).multiply(nbrWorkDayEmployee).setScale(2, BigDecimal.ROUND_HALF_UP) ; 
                    	
                    }
                    //-
                    
                    if ( Dayqty.compareTo(Env.ZERO) != 0)
                    {
                    	Schedule = P_Schedule.get( getCtx(), AssignmentParam.getP_Schedule_ID(), m_trxName );
                    	
                    	create_time_sheet_detail ( Period.getP_Period_ID() );

        				//
        				// Gestion de la réduction de temps de travail. ( on doit généré une ligne de temps suplémentaire ) 
        				//
        				// 
        				if ( Assignment.isRWT( PeriodDate) && Assignment.dayRWT( PeriodDate ) == cal.get(Calendar.DAY_OF_WEEK) )
        				{
        					P_Assignment_RWT AssignmentRWT = Assignment.GetAssignment_RWT( PeriodDate );
        					Dayqty = AssignmentRWT.getRwtQuantity();
        					Gain = P_Gain.get( getCtx(), AssignmentRWT.getP_Gain_ID(), m_trxName );
        					Schedule = P_Schedule.get( getCtx(), AssignmentParam.getP_Schedule_ID(), m_trxName );
        					create_time_sheet_detail ( Period.getP_Period_ID() );
        				}

        				// 
        				// Génér juste 1 fois les gain de type montant;
        				if ( Gain.getP_UOM_ID() != 100 || PaymentGroup.getTimeSheetModel().equals("1")  )
    					{
    						break;
    					}

                    }
				}
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"standard_time - " + sql, e);
		}
		
	}   //	

	private int getWeek(int valeur)
	{
		if (DisplayDetail == 0)
		{
			return valeur;
		}
		else
		{
			return 1;
		}
	}
	
	private void gen_standard_time_money ( int P_Method_Gain_ID)
	{
		//TODO Géré le mode détailler
		if (DisplayDetail != 1 )
			return;
		
		// Gère la coupure sur la période de couverture du temps standard
//		BigDecimal facteur = new BigDecimal( 1 );
		// Gère la coupure sur la période de couverture des paramètres d'affectation
//		BigDecimal facteur2 = new BigDecimal( 1 );
		// Va chercher la date du lendemain
		Timestamp PeriodDateNext = null;
		// Conserve la couverture des paramètres d'affectation courants
		Timestamp PeriodLastDate = null;
		
		// le order by sert a faire la génération du temps standard avant les lignes de Traitement différé.
		String sql = "Select P_Standard_Time_ID "                                                                          
				+ " From P_Standard_Time, P_Gain "
		        + " WHERE P_Standard_Time.P_Employee_ID=" + Employee.getP_Employee_ID()
		        + " AND P_Standard_Time.isActive = 'Y'"
		        + " AND ( P_Standard_Time.EndDate is null OR P_Standard_Time.EndDate >=" + DB.TO_DATE(Period.getStartDate()) +  ")"
		        + " AND P_Gain.P_Gain_ID = P_Standard_Time.P_Gain_ID "
		        ;
		
		       if ( P_Method_Gain_ID != 0 )
					sql += " AND ( P_Method_Gain_ID = " + P_Method_Gain_ID +  " ) ";
		       else
		    	   sql += " AND ( P_Method_Gain_ID in (118)  ) ";
					
				sql += " ORDER BY P_SelfFinancedLeave_ID ";
				;
	    
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, m_trxName);
			ResultSet rs = pstmt.executeQuery ();
			
			P_Standard_Time StandardTime;
			while (rs.next ())
			{
				StandardTime = P_Standard_Time.get( getCtx(), rs.getInt("P_Standard_Time_ID"), m_trxName);
				
				Timestamp StartDate = Period.getStartDate();
				Timestamp EndDate = Period.getEndDate();

				PeriodDate = Period.getStartDate();
				GenStartDate  = PeriodDate;
				GenEndDate    = PeriodDate;

				if ( StandardTime.getP_SelfFinancedLeave_ID() != 0 )
				{
					OriginTime = "TRD";
					SelfFinancedLeave = P_SelfFinancedLeave.get( getCtx(), StandardTime.getP_SelfFinancedLeave_ID(), m_trxName );
				}
				else
				{
					OriginTime = "STD";
					SelfFinancedLeave = null;
				}


				Gain = P_Gain.get( getCtx(), StandardTime.getP_Gain_ID(), m_trxName );

				//
				// On vérifie les dates de débuts et de fin du temps standard
				// S'il ne sont pas inclus dans les dates de la période.
				//
				if ( StandardTime.getStartDate() != null && StandardTime.getStartDate().compareTo( Period.getStartDate()) > 0)
					StartDate = StandardTime.getStartDate();
				
				if ( StandardTime.getEndDate() != null && StandardTime.getEndDate().compareTo( Period.getEndDate()) < 0)
					EndDate = StandardTime.getEndDate();

				//2008.12.02
//				if ( Employee.getDateLayoff() != null && Employee.getDateLayoff().compareTo( Period.getStartDate() ) > 0 && Employee.getDateLayoff().compareTo( PeriodDate) < 0 )
				if ( Employee.getDateLayoff() != null && Employee.getDateLayoff().compareTo( Period.getStartDate() ) >= 0 && Employee.getDateLayoff().compareTo( Period.getEndDate() ) <= 0 )
					EndDate = Employee.getDateLayoff();

				Hourly_Rate = StandardTime.getHourly_Rate();

				BigDecimal nbrWorkDay = Period.getNbrWorkDay( );
				BigDecimal nbrWorkDayEmployee = nbrWorkDay; 

	/*			if ( Period.getStartDate().compareTo( StartDate ) != 0  || Period.getEndDate().compareTo( EndDate ) != 0 )
				{
					nbrWorkDayEmployee = Period.getNbrWorkDay( StartDate, EndDate );
				}
*/
				int P_Assignment_ID = 0;
					
   				if ( StandardTime.getP_Assignment_ID() != 0 )
   				{
   					P_Assignment_ID = StandardTime.getP_Assignment_ID();
   				}
   				else
   				{
					if ( Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ) != 0)
						AssignmentParam = P_Assignment_Param.get( getCtx(), Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ), GenStartDate, m_trxName );
					else 
						AssignmentParam = Employee.getLastEmployeeAssignment(Employee.getP_Employee_ID(), PeriodDate, m_trxName);

   					P_Assignment_ID = AssignmentParam.getP_Assignment_ID(); 
   				}
					
                int NbrAssignment = Employee.getNbrAssignment (Employee.getP_Employee_ID(), P_Assignment_ID, StartDate, EndDate, m_trxName );

                GenStartDate  = StartDate;

               	for (int i = 0; i < NbrAssignment ; i++)
        		{

                    GenEndDate    = EndDate;

               		// Vérifie si mon temps standard spécifie une affectation, si non j'en cherche une active au dossier employé 
    				if ( StandardTime.getP_Assignment_ID() != 0 )
    				{
    	                AssignmentParam = P_Assignment_Param.get( getCtx(), StandardTime.getP_Assignment_ID(), GenStartDate, m_trxName );
    				}
    				else
    				{
    					if ( Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ) != 0)
    						AssignmentParam = P_Assignment_Param.get( getCtx(), Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ), GenStartDate, m_trxName );
    					else 
    						AssignmentParam = Employee.getLastEmployeeAssignment(Employee.getP_Employee_ID(), PeriodDate, m_trxName);
    					
    				}
    				


    				if ( AssignmentParam.getEffectIn().compareTo(StartDate) > 0)
    					GenStartDate = AssignmentParam.getEffectIn();
    					
    				if ( AssignmentParam.getEffectTo() != null && AssignmentParam.getEffectTo().compareTo(EndDate) < 0)
    					GenEndDate = AssignmentParam.getEffectTo();
    					
					nbrWorkDayEmployee = Period.getNbrWorkDay( GenStartDate, GenEndDate);

    				
//                    P_Assignment Assignment = P_Assignment.get( Env.getCtx(), AssignmentParam.getP_Assignment_ID(), m_trxName);
               		
                    BigDecimal nbrPeriod = new BigDecimal( Frequency.getNumberOfPeriod() );
                    // Prends le salaire annuel diviser par le nombre de période pour avoir un salaire périodique

					Hourly_Rate = AssignmentParam.getHourly_Rate();

                    if ( Gain.getP_Method_Gain_ID() == 106 || Gain.getP_Method_Gain_ID() == 104 )
                    {
                    	Dayqty = StandardTime.getStandard_Time_Amount();
                    	Hourly_Rate = Env.ZERO ; 
                    }
                    else if ( Gain.getP_UOM_ID() != 100)
                    	Dayqty = AssignmentParam.getAnnual_Salary().divide( nbrPeriod, 2, BigDecimal.ROUND_HALF_UP );
                    else
                    	//TODO Annual_INcrease
                    	Dayqty =  (AssignmentParam.getWeekly_Hours().multiply( new BigDecimal(52))).divide( nbrPeriod, 2, BigDecimal.ROUND_HALF_UP );

                    if ( nbrWorkDay.compareTo(nbrWorkDayEmployee) != 0)
                    	Dayqty = (Dayqty.divide(nbrWorkDay,8,BigDecimal.ROUND_HALF_UP)).multiply(nbrWorkDayEmployee).setScale(2, BigDecimal.ROUND_HALF_UP) ; 


					weekNumber = 1;
					PeriodDate = GenStartDate;
					
					// 2009.07.17
					// si le gain n'existe pas pour cette période.
					if ( TimeSheet.getP_Period_ID() != Period.getP_Period_ID() && exist_timeSheetDetail( Employee.getP_Employee_ID(), Period.getP_Period_ID(), Gain.getP_Gain_ID() ) == true )						
						Dayqty = Env.ZERO;
				
					
				    // Et on crée finalement notre ligne de temps détaillé
                    if ( Dayqty.compareTo(Env.ZERO) != 0)
                    {
                    	Schedule = P_Schedule.get( getCtx(), AssignmentParam.getP_Schedule_ID(), m_trxName );
                       	create_time_sheet_detail ( Period.getP_Period_ID() );
                    }

                    // Si ne nombre d'affectation (paramètre est > 1
                    if ( NbrAssignment > 1 )
                    	GenStartDate = TimeUtil.addDays( GenEndDate, 1 );
        		
        		}
						
            }
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"standard_time - " + sql, e);
		}
		
	}   //	

	private void gen_special_gain( boolean IsCorrectionOriginPeriod, boolean fromBooklet )
	{
		
		// le order by sert a faire la génération du temps standard avant les lignes de Traitement différé.
		//
		// On vérifier pour géréné les gains spéciaux seulement sur les affectations déjà générer.
		
		String sql = null;
		if ( IsCorrectionOriginPeriod )
		{
			sql = "Select P_Standard_Time_ID, isnull( P_Standard_Time.P_Assignment_ID, P_Assignment.P_Assignment_ID ) P_Assignment_ID "                                                                          
				+ " From P_Standard_Time, P_Assignment, P_Gain "
		        + " WHERE P_Standard_Time.P_Employee_ID=" + Employee.getP_Employee_ID()
		        + " AND P_Standard_Time.isActive = 'Y'"
		        + " AND ( P_Standard_Time.EndDate is null OR P_Standard_Time.EndDate >=" + DB.TO_DATE(Period.getStartDate()) +  ")"
		        + " AND P_Assignment.P_Assignment_ID = isnull( P_Standard_Time.P_Assignment_ID, P_Assignment.P_Assignment_ID )"
		        + " AND P_Assignment.P_Employee_ID = P_Standard_Time.P_Employee_ID "
		        + " AND ( P_Assignment.EndDate >= " + DB.TO_DATE(Period.getStartDate()) + " OR P_Assignment.EndDate IS NULL )"
		        + " AND ( P_Assignment.EndDate >= P_Standard_Time.StartDate OR P_Assignment.EndDate IS NULL )"
		        + " AND ( P_Assignment.StartDate <= isnull( P_Standard_Time.EndDate, P_Assignment.StartDate ) OR P_Assignment.StartDate IS NULL ) " 
		        + " AND P_Assignment.IsActive = 'Y' "
		        + " AND P_Gain.P_Gain_ID = P_Standard_Time.P_Gain_ID ";

/*		    if ( fromBooklet )
		    	sql += " AND ( P_Method_Gain_ID in ( 107 ))";  
		    else*/
		    	sql += " AND ( P_Method_Gain_ID in ( 107,113, 213,116, 300 ))";  // 105, P_UOM_ID <> 100
				
			sql += " AND P_Assignment.P_Assignment_ID IN ( Select DISTINCT P_Assignment_ID FROM " + this.getTableDetail() + " Detail Where Detail." + this.getTable() + "_ID = " + TimeSheet.getRecord_ID() + " ) "
				+ " ORDER BY P_SelfFinancedLeave_ID "
				;
		}
		else
		{
			sql = "Select P_Standard_Time_ID, isnull( P_Standard_Time.P_Assignment_ID, P_Assignment.P_Assignment_ID ) P_Assignment_ID "                                                                          
				+ " From P_Standard_Time, P_Assignment, P_Gain "
		        + " WHERE P_Standard_Time.P_Employee_ID=" + Employee.getP_Employee_ID()
		        + " AND P_Standard_Time.isActive = 'Y'"
		        + " AND ( P_Standard_Time.EndDate is null OR P_Standard_Time.EndDate >=" + DB.TO_DATE(Period.getStartDate()) +  ")"
		        + " AND P_Assignment.P_Assignment_ID = isnull( P_Standard_Time.P_Assignment_ID, P_Assignment.P_Assignment_ID )"
		        + " AND P_Assignment.P_Employee_ID = P_Standard_Time.P_Employee_ID "
		        + " AND ( P_Assignment.EndDate >= " + DB.TO_DATE(Period.getStartDate()) + " OR P_Assignment.EndDate IS NULL )"
		        + " AND ( P_Assignment.EndDate >= P_Standard_Time.StartDate OR P_Assignment.EndDate IS NULL )"
		        + " AND ( P_Assignment.StartDate <= isnull( P_Standard_Time.EndDate, P_Assignment.StartDate ) OR P_Assignment.StartDate IS NULL ) " 
		        + " AND P_Assignment.IsActive = 'Y' "
		        + " AND P_Gain.P_Gain_ID = P_Standard_Time.P_Gain_ID ";
/*			if ( fromBooklet )
			   	sql += " AND ( P_Method_Gain_ID in ( 107 ))";   
			else */
			   	sql += " AND ( P_Method_Gain_ID in ( 105, 107,113, 213,116, 300 ))";  // ( P_UOM_ID <> 100 AND P_Method_Gain_ID <> 118) OR
			
			sql += " AND P_Assignment.P_Assignment_ID IN ( Select DISTINCT P_Assignment_ID FROM " + this.getTableDetail() + " Detail Where Detail." + this.getTable() + "_ID = " + TimeSheet.getRecord_ID() + " ) "
				+ " ORDER BY P_SelfFinancedLeave_ID "
				;
			
		}

	    boolean oneweek = false;

//	    Period = m_Period;
    
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, m_trxName);
			ResultSet rs = pstmt.executeQuery ();
			
			P_Standard_Time StandardTime;
			P_Assignment    Assignment;
			while (rs.next ())
			{
				StandardTime = P_Standard_Time.get( getCtx(), rs.getInt("P_Standard_Time_ID"), m_trxName);
				Assignment = P_Assignment.get( getCtx(),  rs.getInt("P_Assignment_ID"), m_trxName);

				Timestamp StartDate = Period.getStartDate();
				Timestamp EndDate = Period.getEndDate();
				Timestamp WeekStartDate = null;
				Timestamp WeekEndDate = null;
				
				PeriodDate = Period.getStartDate();
				GenStartDate  = PeriodDate;
				GenEndDate    = PeriodDate;

				if ( StandardTime.getP_SelfFinancedLeave_ID() != 0 )
				{
					OriginTime = "TRD";
					SelfFinancedLeave = P_SelfFinancedLeave.get( getCtx(), StandardTime.getP_SelfFinancedLeave_ID(), m_trxName );
				}
				else
				{
					OriginTime = "STD";
					SelfFinancedLeave = null;
				}


				Gain = P_Gain.get( getCtx(), StandardTime.getP_Gain_ID(), m_trxName );

//				Activity = new MActivity( getCtx(), StandardTime.get) 


				//
				// On vérifie les dates de débuts et de fin du temps standard
				// S'il ne sont pas inclus dans les dates de la période.
				//
				if ( StandardTime.getStartDate() != null && StandardTime.getStartDate().compareTo( Period.getStartDate()) > 0)
					StartDate = StandardTime.getStartDate();
				
				if ( StandardTime.getEndDate() != null && StandardTime.getEndDate().compareTo( Period.getEndDate()) < 0)
					EndDate = StandardTime.getEndDate();

                if ( StandardTime.getWeekNumber() != null && Integer.parseInt( StandardTime.getWeekNumber()) != 0 )
                {
                	// Semaine 1 - i = 0
                	// Semaine 2 - i = 1
                	// Selon la semaine, la date de début et de fin modifier.
                	//
                	//TODO Autre que 2 semaines.
                	int i = Integer.parseInt( StandardTime.getWeekNumber() ) -1 ;
                	if ( i == 0)
                	{
                    	EndDate   = TimeUtil.addDays( StartDate, ((i+1) * 7)-1 );
                	}
                	else
                	{
                		StartDate = TimeUtil.addDays( StartDate, (i * 7)   );
                	}
                }
                //DisplayDetail
                int nbrWeek = 1; // Ajout Gen ***

                
                    nbrWeek = getWeek(new BigDecimal( TimeUtil.getDaysBetween(StartDate, EndDate) +1 ) .divide(new BigDecimal(7), 0, BigDecimal.ROUND_UP).intValue());
                    // int nbrWeek = (TimeUtil.getDaysBetween( StartDate, EndDate) /7 ) +1;             	
                

				for (int i = 0; i < nbrWeek ; i++)
				{
	                if (DisplayDetail == 0) // Ajout Gen ***
	                {
						GenStartDate = TimeUtil.addDays( Period.getStartDate(), (i * 7)   );
	    				GenEndDate   = TimeUtil.addDays( Period.getStartDate(), ((i+1) * 7)-1 );         	
	                }
	                else
	                {
						GenStartDate = StartDate; 
	    				GenEndDate   = EndDate;
	                }					
    				WeekStartDate = GenStartDate;
    				WeekEndDate  = GenEndDate;

    				if ( GenStartDate.compareTo( StartDate) < 0 )
    					GenStartDate = StartDate;
    				if (  GenEndDate.compareTo( EndDate ) > 0)	
    					GenEndDate = EndDate;
    				// ce cas ce produit si l'employé travail seulement la 2iem semaine
    				if ( GenEndDate.compareTo( GenStartDate) < 0 )
    					GenEndDate = Period.getEndDate();

    				PeriodDate = GenEndDate;

    				if ( StandardTime.getEndDate() != null && StandardTime.getEndDate().compareTo( PeriodDate ) < 0)
    					PeriodDate = StandardTime.getEndDate();

    				if (Assignment.getEndDate() != null && Assignment.getEndDate().compareTo( PeriodDate) < 0 )
    					PeriodDate = Assignment.getEndDate();
    				
    				weekNumber = getWeek(new BigDecimal( TimeUtil.getDaysBetween(Period.getStartDate(), GenStartDate) +1 ).divide(new BigDecimal(7), 0, BigDecimal.ROUND_UP).intValue());
//					weekNumber = (TimeUtil.getDaysBetween( Period.getStartDate(), GenStartDate) /7 ) +1;
					
					if ( exist_assignment_for_this_week( rs.getInt("P_Assignment_ID"), weekNumber, Period.getP_Period_ID()) )
					{
						Dayqty = ZERO;
						
						if ( Gain.getP_UOM_ID() != 100 )
						{
							Dayqty = StandardTime.getStandard_Time_Amount();
						}
						
						
		                AssignmentParam = P_Assignment_Param.get( getCtx(), rs.getInt("P_Assignment_ID"), GenStartDate, m_trxName );

						if ( Assignment.getStartDate().compareTo( GenStartDate ) > 0)
							GenStartDate = Assignment.getStartDate();

						if (Assignment.getEndDate() != null && Assignment.getEndDate().compareTo( GenEndDate ) < 0)
							GenEndDate = Assignment.getEndDate();

						if ( Gain.getP_Method_Gain_ID() == 105 )
						{
							BigDecimal tQty = Env.ZERO;
							if (StandardTime.getSunday()    != null) { tQty = tQty.add(StandardTime.getSunday());}
							if (StandardTime.getMonday()    != null) { tQty = tQty.add(StandardTime.getMonday());}
							if (StandardTime.getTuesday()   != null) { tQty = tQty.add(StandardTime.getTuesday());}
							if (StandardTime.getWednesday() != null) { tQty = tQty.add(StandardTime.getWednesday());}
							if (StandardTime.getThursday()  != null) { tQty = tQty.add(StandardTime.getThursday());}
							if (StandardTime.getFriday()    != null) { tQty = tQty.add(StandardTime.getFriday());}
							if (StandardTime.getSaturday()  != null) { tQty = tQty.add(StandardTime.getSaturday());}
							
							Dayqty = tQty;
							
							if ( GenEndDate.compareTo( WeekEndDate) < 0)
							{
								BigDecimal nbrDay = Env.ZERO;
						        Calendar cal = Calendar.getInstance();
						        cal.setTime( (Date)GenEndDate );
						        // TODO gestion d'une semaine qui ne commence pas la Jeudi.
								if ( cal.get( Calendar.DAY_OF_WEEK) == Calendar.THURSDAY ) { nbrDay = Env.ONE;}
								if ( cal.get( Calendar.DAY_OF_WEEK) == Calendar.FRIDAY ) { nbrDay = new BigDecimal(2);}
								if ( cal.get( Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ) { nbrDay = new BigDecimal(2);}
								if ( cal.get( Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ) { nbrDay = new BigDecimal(2);}
								if ( cal.get( Calendar.DAY_OF_WEEK) == Calendar.MONDAY ) { nbrDay = new BigDecimal(3);}
								if ( cal.get( Calendar.DAY_OF_WEEK) == Calendar.TUESDAY ) { nbrDay = new BigDecimal(4);}
								if ( cal.get( Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY ) { nbrDay = new BigDecimal(5);}
								Dayqty = Dayqty.divide(new BigDecimal(5),2,BigDecimal.ROUND_HALF_UP).multiply( nbrDay);
							}
							
							if ( GenStartDate.compareTo( WeekStartDate) > 0 )
							{
								BigDecimal nbrDay = Env.ZERO;
						        Calendar cal = Calendar.getInstance();
						        cal.setTime( (Date)GenStartDate );
						        // TODO gestion d'une semaine qui ne commence pas la Jeudi.
								if ( cal.get( Calendar.DAY_OF_WEEK) == Calendar.THURSDAY ) { nbrDay = new BigDecimal(5);}
								if ( cal.get( Calendar.DAY_OF_WEEK) == Calendar.FRIDAY ) { nbrDay = new BigDecimal(4);}
								if ( cal.get( Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ) { nbrDay = new BigDecimal(3);}
								if ( cal.get( Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ) { nbrDay = new BigDecimal(3);}
								if ( cal.get( Calendar.DAY_OF_WEEK) == Calendar.MONDAY ) { nbrDay = new BigDecimal(3);}
								if ( cal.get( Calendar.DAY_OF_WEEK) == Calendar.TUESDAY ) { nbrDay = new BigDecimal(2);}
								if ( cal.get( Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY ) { nbrDay = new BigDecimal(1);}
								Dayqty = Dayqty.divide(new BigDecimal(5),2,BigDecimal.ROUND_HALF_UP).multiply( nbrDay);
							}

						}
						
						if ( Gain.getP_Method_Gain_ID() == 213 )
						{
							GenStartDate = Period.getStartDate();
							GenEndDate = Period.getEndDate();
							BigDecimal qt = getQuantityAdmissible213( TimeSheet, Period.getStartDate(), Period.getEndDate(), m_trxName );

							BigDecimal max = new BigDecimal(40);
							if ( qt.add( StandardTime.getSaturday() ).compareTo( max ) > 0)
							{
								if ( qt.compareTo(max) >= 0)
									Dayqty = Env.ZERO;
								else
									Dayqty = max.subtract(qt);
							}
							else
							{
								Dayqty = StandardTime.getSaturday();
							}

							
						}
						
						Schedule = P_Schedule.get( getCtx(), AssignmentParam.getP_Schedule_ID(), m_trxName );
						create_time_sheet_detail ( Period.getP_Period_ID() );
					}
				}
            }
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"standard_time - " + sql, e);
		}
	}   //	

	private boolean exist_assignment_for_this_week( int P_Assignment_ID, int weekNumber, int P_Period_ID ) throws SQLException
	{
		boolean ret = false;
		String sql = "Select 1 "
			       + "FROM " + this.getTableDetail() + " Detail "
			       + "Where Detail." + this.getTable() + "_ID = " +  TimeSheet.getRecord_ID() 
			       + "  And weekIndex = " + weekNumber
			       + "  And P_Assignment_ID = " + P_Assignment_ID
				   + "  And P_Period_ID = " + P_Period_ID;
			       ;
		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement (sql, m_trxName);
		ResultSet rs = pstmt.executeQuery ();
		
		if (rs.next ())
		{
			ret = true;
        }
		rs.close ();
		pstmt.close ();
		pstmt = null;
	
		return ret;
	}
	
	private boolean existsStdEarning( int P_Time_Sheet_ID  , int P_Period_ID ) throws Exception
	{
		P_GainInfo Gaininfo = P_GainInfo.get(getCtx(), "STD");

		boolean ret = false;
		String sql = "Select 1 "
			       + "FROM P_Time_Sheet_Detail , P_Time_Sheet "
			       + "Where P_Time_Sheet.P_Time_Sheet_ID = " + P_Time_Sheet_ID
			       + "  And P_Time_Sheet.P_Time_Sheet_ID = P_Time_Sheet_Detail.P_Time_Sheet_ID "
			       + "  And P_Time_Sheet_Detail.P_Period_ID = " + P_Period_ID
				   + "  And P_Time_Sheet_Detail.P_Gain_ID in ( Select P_Gain_ID from P_Gain_GainInfo Where to_consider = 'Y' and P_GainInfo_ID = " + Gaininfo.getP_GainInfo_ID() + ")" 
				   		
			       ;
		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement (sql, m_trxName);
		ResultSet rs = pstmt.executeQuery ();
		
		if (rs.next ())
		{
			ret = true;
        }
		rs.close ();
		pstmt.close ();
		pstmt = null;
	
		return ret;
	}
	
	private boolean exist_timeSheetDetail( int P_Employee_ID, int P_Period_ID, int P_Gain_ID ) throws SQLException
	{
		boolean ret = false;
		String sql = "Select 1 "
			       + "FROM P_Time_Sheet_Detail , P_Time_Sheet "
			       + "Where P_Time_Sheet.P_Employee_ID = " + P_Employee_ID
			       + "  And P_Time_Sheet.P_Time_Sheet_ID = P_Time_Sheet_Detail.P_Time_Sheet_ID "
			       + "  And P_Time_Sheet_Detail.P_Period_ID = " + P_Period_ID
				   + "  And P_Time_Sheet_Detail.P_Gain_ID = " + P_Gain_ID;
			       ;
		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement (sql, m_trxName);
		ResultSet rs = pstmt.executeQuery ();
		
		if (rs.next ())
		{
			ret = true;
        }
		rs.close ();
		pstmt.close ();
		pstmt = null;
	
		return ret;
	}

	private int gen_bonus ( )
	{
		int count = 0;
		// P_Bonus
		String sql = "Select *"                                                                          
			+ " From RV_Bonus_Solstice "
	        + " WHERE P_Employee_ID = " + Employee.getP_Employee_ID()
	        + "   AND P_Period_ID   = " + TimeSheet.getP_Period_ID()
	        ;

		OriginTime = "PAR";

//		MPunch_Time Punch_Time;
		
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, m_trxName);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{

//				Punch_Time = new MPunch_Time( getCtx(), rs, m_trxName);
				P_Period ParticularPeriod = P_Period.get( getCtx(), rs.getInt("P_Period_ID"), m_trxName ); 
				
				PeriodDate = rs.getTimestamp( "Day" );
				
				GenStartDate  = rs.getTimestamp("StartDate");
				GenEndDate    = rs.getTimestamp("EndDate");
					
				if ( rs.getInt("P_Assignment_ID") != 0 )
				{
	                AssignmentParam = P_Assignment_Param.get( getCtx(), rs.getInt("P_Assignment_ID"), GenStartDate, m_trxName );
				}
				else
				{
					if ( Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ) != 0)
						AssignmentParam = P_Assignment_Param.get( getCtx(), Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ), GenStartDate, m_trxName );
					else 
						AssignmentParam = Employee.getLastEmployeeAssignment(Employee.getP_Employee_ID(), PeriodDate, m_trxName);
				}
				
				if ( AssignmentParam == null )
				{
	                AssignmentParam = P_Assignment_Param.get( getCtx(), rs.getInt("P_Assignment_ID"), GenEndDate, m_trxName );
				}
					

				weekNumber = getWeek(new BigDecimal( TimeUtil.getDaysBetween(ParticularPeriod.getStartDate(), PeriodDate) +1 ).divide(new BigDecimal(7), 0, BigDecimal.ROUND_UP).intValue());
//				weekNumber = (TimeUtil.getDaysBetween( ParticularPeriod.getStartDate(), PeriodDate) /7 ) +1;

				Gain = P_Gain.get( getCtx(), rs.getInt("P_Gain_ID"), m_trxName );

				Dayqty = rs.getBigDecimal("Dayqty");
				Hourly_Rate = AssignmentParam.getHourly_Rate();
				//Activity.setC_Activity_ID(ParticularSheet.getC_Activity_ID());
				
				Schedule = P_Schedule.get( getCtx(), rs.getInt("P_Schedule_ID"), m_trxName );
				create_time_sheet_detail ( rs.getInt("P_Period_ID") );
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"ParticularSheet - " + sql, e);
		}
		return count;
	}   //	

	
	public int gen_gift ( int P_Period_ID)
	{
		int count = 0;
		// P_Bonus
		String sql = "Select *"                                                                          
			+ " From RV_Gift "
	        + " WHERE P_Employee_ID = " + Employee.getP_Employee_ID()
//	        + "   AND P_Period_ID   = " + P_Period_ID
//	        + "   AND P_TIME_SHEET_ID   = " + TimeSheet.getRecord_ID()
	        ;

		OriginTime = "PAR";

//		MPunch_Time Punch_Time;
		
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, m_trxName);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{

//				Punch_Time = new MPunch_Time( getCtx(), rs, m_trxName);
				P_Period ParticularPeriod = P_Period.get( getCtx(), P_Period_ID, m_trxName ); 
				
				PeriodDate = ParticularPeriod.getEndDate();
				
				GenStartDate  = ParticularPeriod.getStartDate();
				GenEndDate    = ParticularPeriod.getEndDate();
					
				if ( rs.getInt("P_Assignment_ID") != 0 )
				{
	                AssignmentParam = P_Assignment_Param.get( getCtx(), rs.getInt("P_Assignment_ID"), GenStartDate, m_trxName );
				}
				else
				{
					if ( Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ) != 0)
						AssignmentParam = P_Assignment_Param.get( getCtx(), Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ), GenStartDate, m_trxName );
					else 
						AssignmentParam = Employee.getLastEmployeeAssignment(Employee.getP_Employee_ID(), PeriodDate, m_trxName);
				}
				
				if ( AssignmentParam == null )
				{
	                AssignmentParam = P_Assignment_Param.get( getCtx(), rs.getInt("P_Assignment_ID"), GenEndDate, m_trxName );
				}
					

				weekNumber = getWeek(new BigDecimal( TimeUtil.getDaysBetween(ParticularPeriod.getStartDate(), PeriodDate) +1 ).divide(new BigDecimal(7), 0, BigDecimal.ROUND_UP).intValue());
//				weekNumber = (TimeUtil.getDaysBetween( ParticularPeriod.getStartDate(), PeriodDate) /7 ) +1;

				Gain = P_Gain.get( getCtx(), rs.getInt("P_Gain_ID"), m_trxName );

				Dayqty = rs.getBigDecimal("Dayqty");
				Hourly_Rate = AssignmentParam.getHourly_Rate();
				//Activity.setC_Activity_ID(ParticularSheet.getC_Activity_ID());
				
				Schedule = P_Schedule.get( getCtx(), rs.getInt("P_Schedule_ID"), m_trxName );
				create_time_sheet_detail ( P_Period_ID );
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"ParticularSheet - " + sql, e);
		}
		return count;
	}   //	


	private int gen_bonus_V2 ( int P_Period_ID)
	{
		int count = 0;
		// P_Bonus
		String sql = "Select *"                                                                          
			+ " From RV_Bonus_Solstice_V2 "
	        + " WHERE P_Employee_ID = " + Employee.getP_Employee_ID()
//	        + "   AND P_Period_ID   = " + P_Period_ID
	        + "   AND P_TIME_SHEET_ID   = " + TimeSheet.getRecord_ID()
	        ;

		OriginTime = "PAR";

//		MPunch_Time Punch_Time;
		
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, m_trxName);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{

//				Punch_Time = new MPunch_Time( getCtx(), rs, m_trxName);
				P_Period ParticularPeriod = P_Period.get( getCtx(), P_Period_ID, m_trxName ); 
				
				PeriodDate = ParticularPeriod.getEndDate();
				
				GenStartDate  = ParticularPeriod.getStartDate();
				GenEndDate    = ParticularPeriod.getEndDate();
					
				if ( rs.getInt("P_Assignment_ID") != 0 )
				{
	                AssignmentParam = P_Assignment_Param.get( getCtx(), rs.getInt("P_Assignment_ID"), GenStartDate, m_trxName );
				}
				else
				{
					if ( Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ) != 0)
						AssignmentParam = P_Assignment_Param.get( getCtx(), Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ), GenStartDate, m_trxName );
					else 
						AssignmentParam = Employee.getLastEmployeeAssignment(Employee.getP_Employee_ID(), PeriodDate, m_trxName);
				}
				
				if ( AssignmentParam == null )
				{
	                AssignmentParam = P_Assignment_Param.get( getCtx(), rs.getInt("P_Assignment_ID"), GenEndDate, m_trxName );
				}
					

				weekNumber = getWeek(new BigDecimal( TimeUtil.getDaysBetween(ParticularPeriod.getStartDate(), PeriodDate) +1 ).divide(new BigDecimal(7), 0, BigDecimal.ROUND_UP).intValue());
//				weekNumber = (TimeUtil.getDaysBetween( ParticularPeriod.getStartDate(), PeriodDate) /7 ) +1;

				Gain = P_Gain.get( getCtx(), rs.getInt("P_Gain_ID"), m_trxName );

				Dayqty = rs.getBigDecimal("Dayqty");
				Hourly_Rate = AssignmentParam.getHourly_Rate();
				//Activity.setC_Activity_ID(ParticularSheet.getC_Activity_ID());
				
				Schedule = P_Schedule.get( getCtx(), rs.getInt("P_Schedule_ID"), m_trxName );
				create_time_sheet_detail ( P_Period_ID );
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"ParticularSheet - " + sql, e);
		}
		return count;
	}   //	


	private int gen_web_time () throws Exception
	{
		int count = 0;
		String sql = "Select *"                                                                          
				+ " From P_KRISPY_TIMESHEET "
		        + " WHERE P_Employee_ID = " + Employee.getP_Employee_ID()
//		        + "   AND P_Period_ID   = " + TimeSheet.getP_Period_ID()
		        ;

		
		OriginTime = "WEB";

//		MPunch_Time Punch_Time;
		
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, m_trxName);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				count = count + 1;
//				Punch_Time = new MPunch_Time( getCtx(), rs, m_trxName);
				P_Period ParticularPeriod = P_Period.get( getCtx(), rs.getInt("P_Period_ID"), m_trxName ); 
				
				PeriodDate = rs.getTimestamp( "Day" );
				
/*				if ( rs.getString("Bonus").equals("Y") )
				{
					GenStartDate  = ParticularPeriod.getStartDate();
					GenEndDate    = ParticularPeriod.getEndDate();
					
				}
				else
				{
*/				
					GenStartDate  = PeriodDate;
					GenEndDate    = PeriodDate;
					
//				}
					
				if ( rs.getInt("P_Assignment_ID") != 0 )
				{
	                AssignmentParam = P_Assignment_Param.get( getCtx(), rs.getInt("P_Assignment_ID"), GenStartDate, m_trxName );
				}
				else
				{
					if ( Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ) != 0)
						AssignmentParam = P_Assignment_Param.get( getCtx(), Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ), GenStartDate, m_trxName );
					else 
						AssignmentParam = Employee.getLastEmployeeAssignment(Employee.getP_Employee_ID(), PeriodDate, m_trxName);
				}

				weekNumber = getWeek(new BigDecimal( TimeUtil.getDaysBetween(ParticularPeriod.getStartDate(), PeriodDate) +1 ).divide(new BigDecimal(7), 0, BigDecimal.ROUND_UP).intValue());
//				weekNumber = (TimeUtil.getDaysBetween( ParticularPeriod.getStartDate(), PeriodDate) /7 ) +1;

				Gain = P_Gain.get( getCtx(), rs.getInt("P_Gain_ID"), m_trxName );
//				Timecode = P_Timecode.get( getCtx(), rs.getInt("P_Timecode_ID"), m_trxName );
//			    startTime = rs.getString("startTime");
//			    endTime = rs.getString("endTime");

				
				Dayqty = rs.getBigDecimal("Dayqty");
				
				if ( Gain.getP_Method_Gain_ID() == 121)
				{
					//Dayqty = Employee.getIndemnityStatutoryHolidays( l_Period.getP_Period_ID());
					ArrayList<BigDecimal> admissiblevalue = Employee.getIndemnityStatutoryHolidays( Period.getStartDate(), PeriodDate, AssignmentParam.getP_Assignment_Param_ID(), Gain.getP_Column_Adm_ID(), null, m_trxName );
					
					Dayqty = (BigDecimal)admissiblevalue.get(0); 
							//;((BigDecimal)admissiblevalue.get(1).divide( (BigDecimal)admissiblevalue.get(0), 8, BigDecimal.ROUND_HALF_UP)).divide( new BigDecimal(5),1,BigDecimal.ROUND_DOWN ) ;
				}
				
				if ( AssignmentParam == null )
				{
					Hourly_Rate = Env.ZERO;
//					P_Time_Sheet_Error.NewMessage( TimeSheet.get, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, "Le système n'a pas trouvé de paramètre d'affactation",null);

					log.log(Level.SEVERE,"Le système n'a pas trouvé de paramètre d'affectation : Employé : " + Employee.getValue()  );
					

				}
				else
				{
					Hourly_Rate = AssignmentParam.getHourly_Rate();
					
				}
				//Activity.setC_Activity_ID(ParticularSheet.getC_Activity_ID());
				
				Schedule = P_Schedule.get( getCtx(), rs.getInt("P_Schedule_ID"), m_trxName );
				create_time_sheet_detail ( rs.getInt("P_Period_ID") );
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"gen_web_time - " + sql, e);
		}
		
		return count;
	}   //	
	
	private int gen_punch_time ( ) throws Exception
	{
		int count = 0;

/*		String sql = "Select *"                                                                          
			+ " From RV_Punch_Time "
	        + " WHERE P_Employee_ID = " + Employee.getP_Employee_ID()
	        + "   AND P_Period_ID   = " + TimeSheet.getP_Period_ID()
	        ;
*/	        
		String sql = "Select *"                                                                          
				+ " From RV_Punch_Time_Solstice "
		        + " WHERE P_Employee_ID = " + Employee.getP_Employee_ID()
//		        + "   AND P_Period_ID   = " + TimeSheet.getP_Period_ID()
		        ;

		
		OriginTime = "HOR";

//		MPunch_Time Punch_Time;
		
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, m_trxName);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{

//				Punch_Time = new MPunch_Time( getCtx(), rs, m_trxName);
				P_Period ParticularPeriod = P_Period.get( getCtx(), rs.getInt("P_Period_ID"), m_trxName ); 
				
				PeriodDate = rs.getTimestamp( "Day" );
				
				if ( rs.getString("Bonus").equals("Y") )
				{
					GenStartDate  = ParticularPeriod.getStartDate();
					GenEndDate    = ParticularPeriod.getEndDate();
					
				}
				else
				{
					GenStartDate  = PeriodDate;
					GenEndDate    = PeriodDate;
					
				}
					
				if ( rs.getInt("P_Assignment_ID") != 0 )
				{
	                AssignmentParam = P_Assignment_Param.get( getCtx(), rs.getInt("P_Assignment_ID"), GenStartDate, m_trxName );
				}
				else
				{
					if ( Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ) != 0)
						AssignmentParam = P_Assignment_Param.get( getCtx(), Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ), GenStartDate, m_trxName );
					else 
						AssignmentParam = Employee.getLastEmployeeAssignment(Employee.getP_Employee_ID(), PeriodDate, m_trxName);
				}

				weekNumber = getWeek(new BigDecimal( TimeUtil.getDaysBetween(ParticularPeriod.getStartDate(), PeriodDate) +1 ).divide(new BigDecimal(7), 0, BigDecimal.ROUND_UP).intValue());
//				weekNumber = (TimeUtil.getDaysBetween( ParticularPeriod.getStartDate(), PeriodDate) /7 ) +1;

				Gain = P_Gain.get( getCtx(), rs.getInt("P_Gain_ID"), m_trxName );
				Timecode = P_Timecode.get( getCtx(), rs.getInt("P_Timecode_ID"), m_trxName );
			    startTime = rs.getString("startTime");
			    endTime = rs.getString("endTime");

				
				Dayqty = rs.getBigDecimal("Dayqty");
				
				if ( Gain.getP_Method_Gain_ID() == 121)
				{
					if ( AssignmentParam == null )
					{
						log.log(Level.SEVERE,"Le système n'a pas trouvé de paramètre d'affectation : Employé : " + Employee.getValue()  );
					}
					
					ArrayList<BigDecimal> admissiblevalue = null ;
					//Dayqty = Employee.getIndemnityStatutoryHolidays( l_Period.getP_Period_ID());
					try {
						admissiblevalue = Employee.getIndemnityStatutoryHolidays( Period.getStartDate(), PeriodDate, AssignmentParam.getP_Assignment_Param_ID(), Gain.getP_Column_Adm_ID(), null, m_trxName );
					}
					catch (Exception e)
					{
					   P_Time_Sheet Timesheet = P_Time_Sheet.get(Env.getCtx(), TimeSheet.getRecord_ID(), null);
					   P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, "Le système n'arrive pas a calculer le férié",null);
					   log.log(Level.SEVERE,"Le système n'arrive pas a calculer le férié");
					}
					String statutoryHolidayGenerationType = Employee.getStatutoryHolidayGenerationType();
			
					if ( statutoryHolidayGenerationType.equals( P_Employee.STATUTORYHOLIDAYGENERATIONTYPE_FixedNbrOfHours )  )
					{
						if ( Dayqty.compareTo( (BigDecimal)admissiblevalue.get(0) ) < 0) 
							Dayqty = (BigDecimal)admissiblevalue.get(0); 
						
					}
					else
					{
						Dayqty = (BigDecimal)admissiblevalue.get(0); 
					}
					
					// Exception pour les distributeurs
					if ( Employee.getP_Occupation_Group_ID() == 1100277)
						Gain = P_Gain.getWithValue(Env.getCtx(), "0028", null);

				}
				
				if ( AssignmentParam == null )
				{
					Hourly_Rate = Env.ZERO;
//					P_Time_Sheet_Error.NewMessage( TimeSheet.get, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, "Le système n'a pas trouvé de paramètre d'affactation",null);

					log.log(Level.SEVERE,"Le système n'a pas trouvé de paramètre d'affectation : Employé : " + Employee.getValue()  );
					

				}
				else
				{
					Hourly_Rate = AssignmentParam.getHourly_Rate();
					
				}
				//Activity.setC_Activity_ID(ParticularSheet.getC_Activity_ID());
				
				Schedule = P_Schedule.get( getCtx(), rs.getInt("P_Schedule_ID"), m_trxName );
				create_time_sheet_detail ( rs.getInt("P_Period_ID") );
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"ParticularSheet - " + sql, e);
		}
		
		 String sqlGE18 = "UPDATE p_time_sheet_Detail set dayqty = dbo.KRISPY_get_GE18_Qty( P_Time_Sheet_ID, Day  ) "
				 + " WHERE P_TIME_SHEET_ID = " + TimeSheet.getRecord_ID()
				 + " AND dayqty = 0 "
				 + " AND P_GAIN_ID IN ( SELECT P_GAIN_ID FROM P_GAIN WHERE VALUE = 'GE18')"
				 ;
		DB.executeUpdate(sqlGE18, null);

		
		return count;
	}   //	

	
	
	private int gen_particular_sheet ( )
	{
		int count = 0;

		String sql = "Select P_Particular_Sheet_ID "                                                                          
			+ " From P_Particular_Sheet "
	        + " WHERE P_Employee_ID=" + Employee.getP_Employee_ID();

		OriginTime = "PAR";

		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, m_trxName);
			ResultSet rs = pstmt.executeQuery ();
			P_Particular_Sheet ParticularSheet;
			while (rs.next ())
			{

				ParticularSheet = P_Particular_Sheet.get( getCtx(), rs.getInt("P_Particular_Sheet_ID"), m_trxName);
				P_Period ParticularPeriod = P_Period.get( getCtx(), ParticularSheet.getP_Period_ID(), m_trxName ); 
				
				//2009.04.30 seulement les gains réguliers.
				P_GainInfo Gaininfo = P_GainInfo.get(getCtx(), "STD");
				P_Gain_GainInfo Gain_GainInfo = P_Gain_GainInfo.get(getCtx(), ParticularSheet.getP_Gain_ID(), Gaininfo.getP_GainInfo_ID());
				if ( ParticularSheet.getP_Period_ID() == TimeSheet.getP_Period_ID() && Gain_GainInfo.isTo_Consider() )  
					count++;

				if ( ParticularSheet.getDay() == null)
				{
					PeriodDate = ParticularPeriod.getEndDate();
					
					GenStartDate  = ParticularPeriod.getStartDate();
					GenEndDate    = PeriodDate;
				}
				else
				{
					PeriodDate = ParticularSheet.getDay();
					GenStartDate  = PeriodDate;
					GenEndDate    = PeriodDate;
					
				}
				
				if ( ParticularSheet.getP_Assignment_ID() != 0 )
				{
	                AssignmentParam = P_Assignment_Param.get( getCtx(), ParticularSheet.getP_Assignment_ID(), GenStartDate, m_trxName );
				}
				else
				{
					if ( Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ) != 0)
						AssignmentParam = P_Assignment_Param.get( getCtx(), Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ), GenStartDate, m_trxName );
					else 
						AssignmentParam = Employee.getLastEmployeeAssignment(Employee.getP_Employee_ID(), PeriodDate, m_trxName);
				}

				weekNumber = getWeek(new BigDecimal( TimeUtil.getDaysBetween(ParticularPeriod.getStartDate(), PeriodDate) +1 ).divide(new BigDecimal(7), 0, BigDecimal.ROUND_UP).intValue());
//				weekNumber = (TimeUtil.getDaysBetween( ParticularPeriod.getStartDate(), PeriodDate) /7 ) +1;

				Gain = P_Gain.get( getCtx(), ParticularSheet.getP_Gain_ID(), m_trxName );

				Dayqty = ParticularSheet.getDayQty();
				Hourly_Rate = ParticularSheet.getHourly_Rate();
				//Activity.setC_Activity_ID(ParticularSheet.getC_Activity_ID());
				
				if ( ParticularSheet.getP_Schedule_ID() != 0 )
				{
					Schedule = P_Schedule.get( getCtx(), ParticularSheet.getP_Schedule_ID(), m_trxName );
				}
				else {
					Schedule = P_Schedule.get( getCtx(), AssignmentParam.getP_Schedule_ID(), m_trxName );
				}
				create_time_sheet_detail ( ParticularSheet.getP_Period_ID() );
				if ( ParticularSheet.getP_Absence_Detail_ID() != 0)
				{
					P_Absence_Detail AbsenceDetail = P_Absence_Detail.get( getCtx(), ParticularSheet.getP_Absence_Detail_ID(), m_trxName );
					AbsenceDetail.setProcessed( true );
//2008-04-03 m.m
					AbsenceDetail.setP_Period_ID( ParticularPeriod.getP_Period_ID() );
					AbsenceDetail.save();
				}
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"ParticularSheet - " + sql, e);
		}
		return count;
	}   //	

	
	private void create_time_sheet ( String SheetType, String trxName )
	{
		
		TimeSheet = this.getInstance( getCtx (), -1, trxName);

		
		String value = DB.getDocumentNo (Employee.getAD_Client_ID() , TimeSheet.get_TableName(), TimeSheet.get_TrxName() );

		TimeSheet.setAD_Org_ID( Employee.getAD_Org_ID());
		TimeSheet.setP_Department_ID( Employee.getP_Department_ID());
		TimeSheet.setC_Activity_ID( Employee.getC_Activity_ID());
		
		TimeSheet.setValue( Period.getName() + "-" + value.substring( 3, 7 ) );

		//		TimeSheet.setDescription( "");
		TimeSheet.setIsComplete( false );
		TimeSheet.setP_Employee_ID( Employee.getP_Employee_ID());
		TimeSheet.setP_Frequency_ID( Period.getP_Frequency_ID() );
		TimeSheet.setP_Year_ID( Period.getP_Year_ID());
		TimeSheet.setP_Period_ID( Period.getP_Period_ID());
		TimeSheet.setPaymentType( Employee.getPaymentType() );
        TimeSheet.setSheetType( SheetType );
		TimeSheet.setTimeSheetStatus( P_Time_Sheet.TIMESHEETSTATUS_Initial );
		TimeSheet.setP_Distribution_Booklet_ID(Employee.getP_Distribution_Booklet_ID( Period.getEndDate()));
		TimeSheet.setP_Distribution_ID( Employee.getP_Distribution_ID());
		TimeSheet.setP_Job_Title_ID( Employee.getP_Job_Title_ID());
		TimeSheet.setP_Job_Type_ID( Employee.getP_Job_Type_ID());
		TimeSheet.setP_Occupation_Group_ID( Employee.getP_Occupation_Group_ID());
		TimeSheet.setP_Collective_Labour_Agr_ID( Employee.getP_Collective_Labour_Agr_ID());
		TimeSheet.setIsError( false );
		TimeSheet.setP_Payment_Group_ID( Employee.getP_Payment_Group_ID()  );
//		+ 2011.11.16 ajout numéro employeur et lieu de travail sur la feuille de paie
	    TimeSheet.setP_Employer_ID(  Employee.getP_Employer_ID());
	    TimeSheet.setP_Workplace_ID( Employee.getP_Workplace_ID());
//-	2011.11.16 			    

        TimeSheet.setPayDate( Period.getPayDate());

		TimeSheet.save();		
		
		m_inserted++;

	}   //	

	
	private int create_time_sheet_detail ( int P_Period_ID )
	{
		
		if ( AssignmentParam == null )
		{
			if ( Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ) != 0)
				AssignmentParam = P_Assignment_Param.get( getCtx(), Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ), GenStartDate, m_trxName );
			else 
				AssignmentParam = Employee.getLastEmployeeAssignment(Employee.getP_Employee_ID(), PeriodDate, m_trxName);
		}

		if ( AssignmentParam == null || AssignmentParam.getP_Assignment_Param_ID() == 0 )
		 	return 0;
		
//		Schedule = P_Schedule.get( getCtx(), AssignmentParam.getP_Schedule_ID(), m_trxName );

		
		//		if ( Schedule == null || Schedule.getP_Schedule_ID() == 0 )
//			Schedule = P_Schedule.get( getCtx(), 1000000, m_trxName );
			
		
		Period = P_Period.get( getCtx (), P_Period_ID , m_trxName);

		IBookletTimeSheetDetail TimeSheetDetail;
		
		TimeSheetDetail = this.getInstanceDetail( getCtx(), -1, m_trxName); //getTime_Sheet_Detail( false );

		TimeSheetDetail.setParent_ID( TimeSheet.getRecord_ID() );
		
		
		TimeSheetDetail.setP_Assignment_ID( AssignmentParam.getP_Assignment_ID() );
		TimeSheetDetail.setP_Period_ID( Period.getP_Period_ID() );
		
		// 2023-06-04 Spécial pour Krispy
//		if ( Employee.getP_Collective_Labour_Agr_ID() == 1000006 )  // NDISTRIBUTEURS CORPORATIFS   
//		if ( Employee.isCustomFieldYesNo01() )	
//			TimeSheetDetail.setP_Period_ID( getLastPeriod() );
		
		TimeSheetDetail.setP_Gain_ID( Gain.getP_Gain_ID() );
		if ( Timecode != null && Timecode.getP_Timecode_ID() != 0)
			TimeSheetDetail.setP_Timecode_ID( Timecode.getP_Timecode_ID());
		
		if ( startTime != null )
			TimeSheetDetail.setStartTime( startTime.substring(10,16) );
		if ( endTime != null )
			TimeSheetDetail.setEndTime( endTime.substring(10,16) ); 

		
		TimeSheetDetail.setP_Schedule_ID( Schedule.getP_Schedule_ID() );
		TimeSheetDetail.setWeekIndex( weekNumber );
		TimeSheetDetail.setOriginTime( OriginTime );
		TimeSheetDetail.setDay( PeriodDate);
		TimeSheetDetail.setStartDate(GenStartDate);
		TimeSheetDetail.setEndDate(GenEndDate);

		if ( Activity != null )
			TimeSheetDetail.setC_Activity_ID( Activity.getC_Activity_ID());
		
		if ( SelfFinancedLeave != null)
			TimeSheetDetail.setSalaryPercentage( SelfFinancedLeave.getSalaryPercentage() );

		if ( EmployeeLongTermLeave != null )
			TimeSheetDetail.setP_Employee_LongTermLeave_ID( EmployeeLongTermLeave.getP_Employee_LongTermLeave_ID() );
		
		TimeSheetDetail.setDayQty( Dayqty );
		
//		P_Payment_Group PaymentGroup = P_Payment_Group.get( getCtx(), TimeSheet.getP_Payment_Group_ID(), null );
		PaymentGroup = P_Payment_Group.get( getCtx(), TimeSheet.getP_Payment_Group_ID(), null );
		
//TODO		ici ajoute un paramètre.
		if ( PaymentGroup.getTimeSheetModel().equals( P_Payment_Group.TIMESHEETMODEL_Summary ))
		{
			// Stephane Morin 16 septembre 2007
			// Ajout de || Hourly_Rate.compareTo( ZERO ) == 0 pour pallier au return de la fonction 
			// ParticularSheet.getHourly_Rate() qui retourne zero si jamais elle trouve null!!
			

			
			if ( Hourly_Rate == null || Hourly_Rate.compareTo( ZERO ) == 0 )
			{
				//2009.11.11 taux le plus récent si l'employé change de taux en cours de période.
				//Hourly_Rate = AssignmentParam.getHourlyRate( Gain, GenStartDate );
				Hourly_Rate = AssignmentParam.getHourlyRate( Gain, GenEndDate );
			}
			

			//2021-06-02 
			if ( Gain != null && Gain.getType_Rate() != null && Gain.getType_Rate().equals(  P_Gain.TYPE_RATE_FixedRate ) )
			{
				TimeSheetDetail.setHourly_Rate(Gain.getHourly_Rate()) ;
			}
			else {
				TimeSheetDetail.setHourly_Rate(Hourly_Rate) ;	
			}
			
			
			
			
		}
// 2025-11-06
		if ( ! ( Hourly_Rate == null || Hourly_Rate.compareTo( ZERO ) == 0 ) )
		{
			TimeSheetDetail.setHourly_Rate(Hourly_Rate) ;
		}
		
		TimeSheetDetail.save();
		
		BigDecimal DayOld = Dayqty;

		
		if ( ! OriginTime.equals("STD") && ! OriginTime.equals("HOR") && ! OriginTime.equals("DEP")   )  
		{
			if ( OriginTime.equals("PAR") && Particular_Cut_Std ) 
			{
				// TODO HARCODE
				//P_Gain_Join gj = P_Gain_Join.g
				//select * from  P_Gain_Join where P_Gain_ID = Gain.getP_Gain_ID()
				if ( Gain.getValue().equals("01") )
					Cut_Gain_Particular( Gain.getP_Gain_ID(), TimeSheetDetail.getRecord_ID(), P_Period_ID);
			}

			Cut_Gain( Gain.getP_Gain_ID(), TimeSheetDetail.getRecord_ID(), P_Period_ID);
		}
		
	    this.GenerationDetail = true;

		return TimeSheetDetail.getRecord_ID();
		
	}   //	

	public Properties getCtx()
	{
		return m_ctx;
	}

	private void Cut_Gain_Particular( int GainID, int Origine, int P_Period_ID)
	{

		IBookletTimeSheetDetail Cut;
		try
		{
			Cut = getTime_Sheet_Detail( GainID, P_Period_ID, "STD", true);
			if ( Cut != null && Cut.getRecord_ID() != Origine )
				Cut.delete( true, m_trxName);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"* ERROR * Cut_gain error  - " , e);
		}

	}

	private void Cut_Gain( int GainID, int Origine, int P_Period_ID)
	{
		Vector<IBookletTimeSheetDetail> vecCut = new Vector<IBookletTimeSheetDetail>();
		IBookletTimeSheetDetail Cut;
        boolean isCut = false;
        BigDecimal qtyCut;
        boolean isGainCoupant = false; 
        
        String addWhereClause = (IsNegativeCut)? " AND P_Gain_Join.ISNEGATIVE = 'Y' "  : "";
		String query = "Select P_Gain_ID_Affected FROM P_Gain_Join, P_Gain "
			         + " WHERE P_Gain_Join.P_Gain_ID = ? "
					 + "   AND P_Gain_Join.P_Gain_ID = P_Gain.P_Gain_ID "
					 + addWhereClause
					 + "  ORDER BY P_Gain.value ";
					 
		PreparedStatement pstmt = null;
		try
		{
			
			pstmt = DB.prepareStatement (query, m_trxName);
			pstmt.setInt(1, GainID );
			
			ResultSet rs = pstmt.executeQuery ();
			P_Gain Gain_Affected;
			P_Gain Gain_coupant; // Ajout Stéphane Morin 18 septembre 2007
			P_Time_Sheet_Detail CurrentTime;
			while (rs.next () && ! isCut )
			{
				Gain_Affected = P_Gain.get( getCtx(), rs.getInt("P_Gain_ID_Affected"), m_trxName);
				// 2009.02.24
    	        if (DisplayDetail == 1)
    	        {
    				CurrentTime = P_Time_Sheet_Detail.get( Env.getCtx(), Origine);
    				PeriodDate = CurrentTime.getStartDate();
    	        }
				// 2009.02.24

				Cut = getTime_Sheet_Detail( Gain_Affected.getP_Gain_ID(), P_Period_ID, null, true);
				
				//2024-05-09 on ne coupe pas le temps de l'horodateur.
				if ( Cut != null && Cut.getRecord_ID() != Origine && Cut.getOriginTime().equals("HOR") == false && Cut.getOriginTime().equals("WEB") == false  )
				{
					isGainCoupant = true;
					Gain_coupant = P_Gain.get( getCtx(), GainID, m_trxName);
					// Stéphane Morin 18 septembre 2007
					// Ajout de la gestion des unités de mesures
					// Je ne gère que heures versus montants pour l'instant
					if (Gain_coupant.getP_UOM_ID() != Gain_Affected.getP_UOM_ID())
					{
						if (Gain_coupant.getP_UOM_ID() == 100 && Gain_Affected.getP_UOM_ID() == 101)
						{
							// Si mon coupant est des heures et mon coupé des montants, je multiplie 
							// le coupant par le taux horaire
							isCut = isCut(Cut.getDayQty() , Dayqty.multiply(Cut.getHourly_Rate()));					
						    qtyCut = cut( Cut.getDayQty() , Dayqty.multiply(Cut.getHourly_Rate()));				    
							
						}
						else
						{
							// Si mon coupant est des montants et mon coupé des heures, je divise 
							// le coupant par le taux horaire
							
							if ( Cut.getHourly_Rate().compareTo(Env.ZERO) != 0 )
							{
								isCut = isCut(Cut.getDayQty() , Dayqty.divide(Cut.getHourly_Rate()));					
							    qtyCut = cut( Cut.getDayQty() , Dayqty.divide(Cut.getHourly_Rate()));			    		
								
							}
							else {
								isCut = false;					
							    qtyCut = Env.ZERO;			    		
								
							} 
						}
					}
					else
					{
						// Si mes deux (coupant et coupé) ont la même unité de mesure
						// je me soucie de rien
						isCut = isCut(Cut.getDayQty() , Dayqty);					
					    qtyCut = cut( Cut.getDayQty() , Dayqty ) ;				    
					}
					
					Cut.setDayQty( Cut.getDayQty().add( qtyCut));					
					//m.m
//					Dayqty = Cut.getDayQty(); 		DayQty =6 amd getday = 80.67

					boolean canDoNegativeCut = checkForNegativeCut(GainID, rs.getInt("P_Gain_ID_Affected"));

					Dayqty = Dayqty.add( qtyCut );
					if ( Cut.getDayQty().compareTo(ZERO) != 0)
						canDoNegativeCut = false;
					
					Cut.save();		
					
					if (Cut.getDayQty().compareTo( ZERO ) == 0 && ! canDoNegativeCut)
					{
						Cut.delete( true, null);
					}
					else if (canDoNegativeCut && !  IsNegativeCut)
					{
						vecCut.add(Cut);
					}
				}
				
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;			
			//BigDecimal t = ZERO.subtract(Dayqty);
			//Dayqty = Dayqty.add( t );
			if ( Dayqty.compareTo( ZERO ) == 0)
			{
				for (int i = 0; i < vecCut.size(); i++)
				{
					vecCut.get(i).delete( true, m_trxName);
				}
				IsNegativeCut = false;
			}
//			m.m
//			else if (isGainCoupant && checkForNegativeCut(GainID, -1) && Dayqty.compareTo( ZERO ) > 0)
			else if (isGainCoupant && checkForNegativeCut(GainID, -1))
			{
				IsNegativeCut = true;
				Cut_Gain(GainID, Origine, P_Period_ID);
			}		
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"* ERROR * Cut_gain error  - " + query, e);
		}

	}
	
	//Cette fonction valide si la combinaison de P_Gain_ID et de P_Gain_ID_Affected 
	//peut être couper en négatif
	private boolean checkForNegativeCut(int GainID, int AffectedID)
	{
		String insertCondition = (AffectedID != -1) ? " AND P_Gain_Join.P_Gain_ID_Affected = ? " : "";
		//String whereClause = (newRecord)? "" : " P_Gain_Join_ID <> " + this.getP_Gain_Join_ID() + " and "; 
		String query = "Select P_Gain_ID_Affected FROM P_Gain_Join, P_Gain "
	         + " WHERE "
	         + " P_Gain_Join.P_Gain_ID = ? "
	         + insertCondition 
			 + " AND P_Gain_Join.P_Gain_ID = P_Gain.P_Gain_ID "
			 + " AND P_Gain_Join.ISNEGATIVE = 'Y'"
			 + " ORDER BY P_Gain.value ";	    
	    try
	    {   	        
	        PreparedStatement stmt = DB.prepareStatement(query, null);
	        stmt.setInt(1, GainID );
	        if (AffectedID != -1)
	        	stmt.setInt(2, AffectedID );
	        ResultSet rs = stmt.executeQuery();		        
	        if(rs != null && rs.next())
	        {
				return true;		        	
	        }        
	        rs.close();
	        stmt.close();
	        return false;
	    }
	    catch (SQLException e)
	    {
	    	log.log(Level.SEVERE,"* ERROR * checkForNegativeCut - " + query, e);
	        return false;
	    }		
	}

	private void _Cut_Bonus( int GainID, int Origine, int P_Period_ID, int WeekIndex)
	{

		IBookletTimeSheetDetail Cut;
        boolean isCut = false;
        BigDecimal qtyCut;
        
		String query = "Select P_Gain_ID_Affected FROM P_Gain_Join, P_Gain "
			         + " WHERE P_Gain_Join.P_Gain_ID = ? "
					 + "   AND P_Gain_Join.P_Gain_ID = P_Gain.P_Gain_ID "
					 + "  ORDER BY P_Gain.value ";
					 ;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (query, m_trxName);
			pstmt.setInt(1, GainID );
			
			ResultSet rs = pstmt.executeQuery ();
			P_Gain Gain_Affected;
			while (rs.next () && ! isCut )
			{
				Gain_Affected = P_Gain.get( getCtx(), rs.getInt("P_Gain_ID_Affected"), m_trxName);
				if ( Gain_Affected.getP_Method_Gain_ID() == 105 )
				{
					Cut = getTime_Sheet_DetailBonus( Gain_Affected.getP_Gain_ID(), P_Period_ID, WeekIndex );
					
					if ( Cut != null && Cut.getRecord_ID() != Origine )
					{
						isCut = isCut(Cut.getDayQty() , Dayqty);
					    qtyCut = cut( Cut.getDayQty() , Dayqty ) ;
						Cut.setDayQty( Cut.getDayQty().add( qtyCut));
						Dayqty = Dayqty.add( qtyCut );
						Cut.save();
						
						if ( 
								 Cut.getDayQty().compareTo( ZERO ) == 0 
							)
						{
							Cut.delete( true, m_trxName);
						}
					}
				}
				
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"* ERROR * Cut_gain error  - " + query, e);
		}

	}

	private boolean isCut( BigDecimal QtyVo, BigDecimal QtySubtract )
	{
 	    if ( QtyVo.compareTo( QtySubtract) < 0)
		   return false;

		return true;
	}
	private BigDecimal cut( BigDecimal QtyVo, BigDecimal QtySubtract )
	{
	   if (QtyVo.compareTo( ZERO) < 0 && IsNegativeCut == false)
		   return ZERO.subtract( ZERO);		
	   if (QtyVo.compareTo( QtySubtract) < 0 && IsNegativeCut == false)
	   return ZERO.subtract( QtyVo);
	  
	   return ZERO.subtract( QtySubtract );
	   
	}

	public IBookletTimeSheetDetail getTime_Sheet_Detail ( int P_Gain_ID,  int P_Period_ID, String OrigineTime, boolean firstCall )
	{
		String query = null ;
		
		int P_Time_Sheet_Detail_ID = -1;
        // Stéphane Morin 18 septembre 2007
		// Si je suis en mode sommaire, on doit plutôt essayer de retrouver des gains à couper entre dates et non pas à une date 
		// précise.  Car la saisie se fait de façon sommaire et non pas détaillée à la journée.  Sinon je cherche à la journée.
		if (DisplayDetail == 1) // Fréquence 24 pour Hélico pour l'instant
		{
			// 2008-08-07 on essait de trouvé le jour à coupé dans un interval de date en premier
			// après l'on recherche sur l'ensemble de la feuille de temps.
			if ( firstCall )
			{
				query = "Select " + this.getTableDetail() + "_ID " 
		         + " FROM " + this.getTableDetail()
				 + " WHERE " + this.getTable() + "_ID = ? AND P_Period_ID = ? AND P_Gain_ID = ? AND P_Assignment_ID = ? "
				 + "AND " + DB.TO_DATE(PeriodDate) + " between StartDate and EndDate "
				 + "   AND OriginTime <> 'PAR'"
				 ;
				
			}
			else
			{
				query = "Select " + this.getTableDetail() + "_ID " 
		         + " FROM " + this.getTableDetail()
				 + " WHERE " + this.getTable() + "_ID = ? AND P_Period_ID = ? AND P_Gain_ID = ? AND P_Assignment_ID = ? "
				 // + "AND " + DB.TO_DATE(PeriodDate) + " between StartDate and EndDate"
				 + "   AND OriginTime <> 'PAR' "
				 ;
				
			}
				


		}
		else
		{
			query = "Select " + this.getTableDetail() + "_ID " 
	         + " FROM " + this.getTableDetail()
			 + " WHERE " + this.getTable() + "_ID = ? AND P_Period_ID = ? AND P_Gain_ID = ? AND P_Assignment_ID = ? AND Day = " + DB.TO_DATE(PeriodDate)
			 + "   AND OriginTime <> 'PAR'"
			 ;
			
		}
		
		if ( OrigineTime != null )
			query = query + " AND OriginTime = '" + OrigineTime + "'";
		
		
		if ( Activity != null )
			query = query + " And ( isnull( C_Activity_ID, 0 ) = ? ) ";
		
		 // 2009.09.09 
		query = query + " Order by StartDate Desc ";

		
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (query, m_trxName);
			pstmt.setInt(1, TimeSheet.getRecord_ID() );
			pstmt.setInt(2, P_Period_ID );
			pstmt.setInt(3, P_Gain_ID );
			pstmt.setInt(4, AssignmentParam.getP_Assignment_ID() );
			//pstmt.setInt(5, Schedule.getP_Schedule_ID() );

			//if ( Activity != null )
			//	pstmt.setInt(7, Activity.getC_Activity_ID() );
			
			ResultSet rs = pstmt.executeQuery ();
			
			if (rs.next ())
			{
				P_Time_Sheet_Detail_ID = rs.getInt(1);
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"* ERROR * Read Time_Sheet_Weekly error  - " + query, e);
		}
        if ( P_Time_Sheet_Detail_ID != -1 )
			return this.getInstanceDetail(getCtx(), P_Time_Sheet_Detail_ID, m_trxName );
        else if ( firstCall )
        {
        	if ( OriginTime != "ALD" )
        		return getTime_Sheet_Detail ( P_Gain_ID,  P_Period_ID, OrigineTime, false );
        	else return null;
        	
        }
        else return null;
	}	//	get

	public IBookletTimeSheetDetail getTime_Sheet_DetailBonus ( int P_Gain_ID,  int P_Period_ID, int WeekIndex )
	{
		
		int P_Time_Sheet_Detail_ID = -1;

		String query = "Select " + this.getTableDetail() + "_ID " 
			         + " FROM " + this.getTableDetail()
					 + " WHERE " + this.getTable() + "_ID = ? AND P_Period_ID = ? AND P_Gain_ID = ? AND P_Assignment_ID = ? AND P_Schedule_ID = ? AND WeekIndex = " + WeekIndex
					 + "   AND OriginTime <> 'PAR' and DayQty <> 0 "
					 ;
		
		if ( Activity != null )
			query = query + " And ( isnull( C_Activity_ID, 0 ) = ? ) ";
		
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (query, m_trxName);
			pstmt.setInt(1, TimeSheet.getRecord_ID() );
			pstmt.setInt(2, P_Period_ID );
			pstmt.setInt(3, P_Gain_ID );
			pstmt.setInt(4, AssignmentParam.getP_Assignment_ID() );
			pstmt.setInt(5, Schedule.getP_Schedule_ID() );

			if ( Activity != null )
				pstmt.setInt(7, Activity.getC_Activity_ID() );
			
			ResultSet rs = pstmt.executeQuery ();
			
			if (rs.next ())
			{
				P_Time_Sheet_Detail_ID = rs.getInt(1);
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"* ERROR * Read Time_Sheet_Weekly error  - " + query, e);
		}
        if ( P_Time_Sheet_Detail_ID != -1 )
			return this.getInstanceDetail(getCtx(), P_Time_Sheet_Detail_ID, m_trxName );
        else 
            return null;
	}	//	get

	//
	// Les heures non rémunéré coupe le prime sur quantité ainsi que les jours en CSST.
	// La différence avec l'autre méthode de coupe, c'est que le jour coupé est dans la même semaine, 
	// mais pas nécessairement le même jours.
    //
	private void Cut_Bonus()
	{
		String query = "Select " + this.getTableDetail() + "_ID, DayQty, P_Period_ID, Detail.P_Gain_ID, Day, P_Assignment_ID " 
         + " FROM " + this.getTableDetail() + " Detail, P_Gain "
		 + " WHERE " + this.getTable() + "_ID = ? AND OriginTime <> 'STD' "
		 + " AND Detail.P_Gain_ID = P_Gain.P_Gain_ID "
		 + " AND P_Gain.P_Method_Gain_ID in ( 104 , 114 ) "
		 ;

		int P_Period_ID;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (query, m_trxName);
			pstmt.setInt(1, TimeSheet.getRecord_ID() );

			ResultSet rs = pstmt.executeQuery ();

			while (rs.next ())
			{
				IBookletTimeSheetDetail TimeSheetDetail = this.getInstanceDetail( getCtx(), rs.getInt(1), m_trxName); 

				P_Period_ID = rs.getInt("P_Period_ID");
				Dayqty = rs.getBigDecimal("DayQty");

				BigDecimal DayOld = Dayqty;

				PeriodDate = rs.getTimestamp("Day");
                AssignmentParam = P_Assignment_Param.get( getCtx(), rs.getInt("P_Assignment_ID"), PeriodDate, m_trxName );
        		Schedule = P_Schedule.get( getCtx(), AssignmentParam.getP_Schedule_ID(), m_trxName );
        		
				Gain = P_Gain.get( Env.getCtx(), rs.getInt("P_Gain_ID"), m_trxName);
//				Cut_Gain( Gain.getP_Gain_ID(), TimeSheetDetail.getRecord_ID(), P_Period_ID);
				//
				// Les heures non rémunéré coupe le prime sur quantité.
				//
				if ( Gain.getP_Method_Gain_ID() == 104 || Gain.getP_Method_Gain_ID() == 114 )
				{
					Dayqty = DayOld;
					_Cut_Bonus( Gain.getP_Gain_ID(), TimeSheetDetail.getRecord_ID(), P_Period_ID, TimeSheetDetail.getWeekIndex());
				}
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"* ERROR * Global Cut.  - " + query, e);
		}
		
	}
	
	/*	
	private void gen_production_time ( )
	{
		int Activity_ID       = 0;
		String sql = null;
		sql = "Select DateOperation, P_Gain_ID, CodeUniteAdm, sum( qty ) qty "                                                                          
			+ " From RV_P_PRODUCTION_TIME "                                                    
	        + " Where P_Period_ID = ";
		sql += Period.getP_Period_ID();
		sql += " AND P_Employee_ID=" + Employee.getP_Employee_ID();
		sql += " Group By P_Period_ID, P_Employee_ID, DateOperation, P_Gain_ID, CodeUniteAdm";

//		log.info( "Read production_time - Sql : " + sql );

		PreparedStatement pstmt  = null;
		try
		{
			pstmt = DB.prepareStatement (sql);
			ResultSet rs = pstmt.executeQuery ();

			while (rs.next ())
			{

//				log.info("production_time"  );
				int P_Assignment_ID   = Assignment.getP_Assignment_ID(); 
				int Schedule_ID       = Assignment.getP_Schedule_ID();
				Timestamp Day         = rs.getTimestamp(1);
				int Gain_ID           = rs.getInt(2);
				BigDecimal Dayqty     = rs.getBigDecimal(4);
				Activity_ID =  rs.getInt(3);
				
//				if ( s_GenTime.containsKey( Day ) )
//				{
//					Dayqty = Dayqty.subtract( (BigDecimal)s_GenTime.get( Day ));
//				}
					
				if ( Dayqty == null )
				{
					Dayqty = new BigDecimal( 0 );
				}

				if ( Dayqty.compareTo( new BigDecimal( 0 )  ) > 0 )
				{
					create_time_sheet_detail ( Day, P_Assignment_ID, Dayqty, Schedule_ID, Gain_ID, Activity_ID, false, -1, true );
				}
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"production_time - " + sql, e);
		}
		
		
		return ;
		
	}   //	
*/

	public void GenerationExpense_Account_DetV2( Properties ctx, int Client_ID, int Org_ID, int Employee_ID, int Payment_Group_ID, int Period_ID, int P_Time_Sheet_ID, String TrxName )
	{
		String sql = "SELECT P_Employee_Expensereports.P_Employee_Expensereports_ID, P_Employee_Expensereports.P_Period_ID, \r\n"
				+ " sum( P_Employee_Expensereports_Detail.Tps_Amt ) Tps_Amt, sum( P_Employee_Expensereports_Detail.tvq_amt ) Tvq_Amt, sum( P_Employee_Expensereports_Detail.Tvh_Amt ) Tvh_Amt, sum( P_Employee_Expensereports_Detail.amount) amt"
				+ " from P_Employee_Expensereports "
  				   + " INNER JOIN P_Employee_Expensereports_Detail on P_Employee_Expensereports_Detail.P_Employee_Expensereports_ID = P_Employee_Expensereports.P_Employee_Expensereports_ID "
				   + " WHERE P_Employee_Expensereports.isactive = 'Y' and is_reimbursable = 'Y' " ;

			if ( Employee_ID != 0 )
				sql = sql  + " AND P_Employee_Expensereports.P_Employee_ID = " + Employee_ID;
			
			sql = sql   + " GROUP BY P_Employee_Expensereports.P_Employee_Expensereports_ID, P_Employee_Expensereports.P_Period_ID "
				   ;
		OriginTime = "DEP";
//RETRO		OriginTime = "PER";

//		MPunch_Time Punch_Time;
		
		PreparedStatement pstmt = null;
		try
		{
			
			pstmt = DB.prepareStatement (sql, TrxName);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{

				P_Employee_Expensereports Expense_Account = new P_Employee_Expensereports( ctx, rs.getInt("P_Employee_Expensereports_ID"), TrxName);
				Expense_Account.setP_Time_Sheet_ID( P_Time_Sheet_ID );
				Expense_Account.save();
				
//				Punch_Time = new MPunch_Time( getCtx(), rs, m_trxName);
				P_Period ParticularPeriod = P_Period.get( ctx, Period_ID, TrxName ); 
				
				
				PeriodDate = ParticularPeriod.getEndDate();
				
				GenStartDate  = PeriodDate;
				GenEndDate    = PeriodDate;
				if ( Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ) != 0)
					AssignmentParam = P_Assignment_Param.get( getCtx(), Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ), GenStartDate, m_trxName );
				else 
					AssignmentParam = Employee.getLastEmployeeAssignment(Employee.getP_Employee_ID(), PeriodDate, m_trxName);

				weekNumber = getWeek(new BigDecimal( TimeUtil.getDaysBetween(ParticularPeriod.getStartDate(), PeriodDate) +1 ).divide(new BigDecimal(7), 0, BigDecimal.ROUND_UP).intValue());

//				Gain = P_Gain.get( ctx, rs.getInt("P_Gain_ID"), TrxName );
				Gain = P_Gain.getWithValue(ctx, "GL14", null);;

//RETRO				Gain = P_Gain.get( ctx, 1003602, TrxName );
				Hourly_Rate = Env.ZERO;
				Schedule = P_Schedule.get( ctx, 100000, TrxName );

				
				if (  rs.getBigDecimal("Tps_Amt") != null 
					||	rs.getBigDecimal("Tvq_Amt") != null
					||	rs.getBigDecimal("Tvh_Amt") != null
					)
				{
					BigDecimal Tps_Amt =rs.getBigDecimal("Tps_Amt");
					if ( Tps_Amt == null) Tps_Amt = Env.ZERO;

					BigDecimal Tvq_Amt =rs.getBigDecimal("Tvq_Amt");
					if ( Tvq_Amt == null) Tvq_Amt = Env.ZERO;
					BigDecimal Tvh_Amt =rs.getBigDecimal("Tvh_Amt");
					if ( Tvh_Amt == null) Tvh_Amt = Env.ZERO;

					Dayqty = Tps_Amt;
					
					if ( Dayqty.compareTo( Env.ZERO) != 0 )
					{
						create_time_sheet_detail ( Period_ID );
						
					}
					Dayqty = Tvq_Amt;
					if ( Dayqty.compareTo( Env.ZERO) != 0 )
					{
						Gain = P_Gain.getWithValue(ctx, "GL15", null);;
						create_time_sheet_detail ( Period_ID );
						
					}
					Dayqty = Tvh_Amt;
					if ( Dayqty.compareTo( Env.ZERO) != 0 )
					{
						Gain = P_Gain.getWithValue(ctx, "GL16", null);
						create_time_sheet_detail ( Period_ID );
						
					}

					Dayqty = rs.getBigDecimal("Amt").subtract(Tps_Amt).subtract( Tvq_Amt).subtract(Tvh_Amt);
					if ( Dayqty.compareTo( Env.ZERO) != 0  )
					{
						Gain = P_Gain.getWithValue(ctx, "GL02", null);
						//P_Gain.get( ctx, rs.getInt("P_Gain_ID"), TrxName );
						create_time_sheet_detail ( Period_ID );
						
					}

					
				}
				else {
					Dayqty = rs.getBigDecimal("Amt");
					create_time_sheet_detail ( Period_ID );
					
				}
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"GenerationExpense_Account_Det V2 - " + sql, e);
		}

		
	}

	
	public void GenerationExpense_Account_Det( Properties ctx, int Client_ID, int Org_ID, int Employee_ID, int Payment_Group_ID, int Period_ID, int P_Time_Sheet_ID, String TrxName )
	{
		String sql = "SELECT * FROM I_Expense_Account WHERE  IsActive = 'Y' AND I_Expense_Account.P_Employee_ID = " + Employee_ID ;
		OriginTime = "DEP";
//RETRO		OriginTime = "PER";

//		MPunch_Time Punch_Time;
		
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, TrxName);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{

				X_I_Expense_Account Expense_Account = new X_I_Expense_Account( ctx, rs, TrxName);
				Expense_Account.setP_Time_Sheet_ID( TimeSheet.getRecord_ID() );
				Expense_Account.save();
				
//				Punch_Time = new MPunch_Time( getCtx(), rs, m_trxName);
				P_Period ParticularPeriod = P_Period.get( ctx, Period_ID, TrxName ); 
				
				
				PeriodDate = ParticularPeriod.getEndDate();
				
				GenStartDate  = PeriodDate;
				GenEndDate    = PeriodDate;
				if ( Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ) != 0)
					AssignmentParam = P_Assignment_Param.get( getCtx(), Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ), GenStartDate, m_trxName );
				else 
					AssignmentParam = Employee.getLastEmployeeAssignment(Employee.getP_Employee_ID(), PeriodDate, m_trxName);

				weekNumber = getWeek(new BigDecimal( TimeUtil.getDaysBetween(ParticularPeriod.getStartDate(), PeriodDate) +1 ).divide(new BigDecimal(7), 0, BigDecimal.ROUND_UP).intValue());

				Gain = P_Gain.get( ctx, rs.getInt("P_Gain_ID"), TrxName );
//RETRO				Gain = P_Gain.get( ctx, 1003602, TrxName );
				Hourly_Rate = Env.ZERO;
				Schedule = P_Schedule.get( ctx, 100000, TrxName );

				
				if (  rs.getBigDecimal("Tps_Amt") != null 
					||	rs.getBigDecimal("Tvq_Amt") != null
					||	rs.getBigDecimal("Tvh_Amt") != null
					)
				{
					BigDecimal Tps_Amt =rs.getBigDecimal("Tps_Amt");
					if ( Tps_Amt == null) Tps_Amt = Env.ZERO;

					BigDecimal Tvq_Amt =rs.getBigDecimal("Tvq_Amt");
					if ( Tvq_Amt == null) Tvq_Amt = Env.ZERO;
					BigDecimal Tvh_Amt =rs.getBigDecimal("Tvh_Amt");
					if ( Tvh_Amt == null) Tvh_Amt = Env.ZERO;

					Dayqty = Tps_Amt;
					
					if ( Dayqty.compareTo( Env.ZERO) != 0 )
					{
						Gain = P_Gain.getWithValue(ctx, "GL14", null);;
						create_time_sheet_detail ( Period_ID );
						
					}
					Dayqty = Tvq_Amt;
					if ( Dayqty.compareTo( Env.ZERO) != 0 )
					{
						Gain = P_Gain.getWithValue(ctx, "GL15", null);;
						create_time_sheet_detail ( Period_ID );
						
					}
					Dayqty = Tvh_Amt;
					if ( Dayqty.compareTo( Env.ZERO) != 0 )
					{
						Gain = P_Gain.getWithValue(ctx, "GL16", null);
						create_time_sheet_detail ( Period_ID );
						
					}

					Dayqty = rs.getBigDecimal("Amt").subtract(Tps_Amt).subtract( Tvq_Amt).subtract(Tvh_Amt);
					if ( Dayqty.compareTo( Env.ZERO) != 0  )
					{
						Gain = P_Gain.get( ctx, rs.getInt("P_Gain_ID"), TrxName );
						create_time_sheet_detail ( Period_ID );
						
					}

					
				}
				else {
					Dayqty = rs.getBigDecimal("Amt");
					create_time_sheet_detail ( Period_ID );
					
				}
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"GenerationExpense_Account_Det - " + sql, e);
		}

		
	}

	public void GenerationExpense_Cell_Det( Properties ctx, int Client_ID, int Org_ID, int Employee_ID, int Payment_Group_ID, int Period_ID, int P_Time_Sheet_ID, String TrxName )
	{
		P_Period r_Period = P_Period.get( ctx, Period_ID, TrxName ); 
		
		Timestamp currentDate = new Timestamp( System.currentTimeMillis() );

		// paiement le 15 du mois alors on utilise le 8 vue que la période est 1 une semaine.
		Timestamp dateref = TimeUtil.getDay( TimeUtil.getYear(currentDate), TimeUtil.getMonth(currentDate) ,8 );
		
		if (  dateref.compareTo( r_Period.getStartDate() ) >= 0 &&
			  dateref.compareTo( r_Period.getEndDate() ) <= 0 )
	    {
	/*		
			if ( Employee.isCustomFieldYesNo01() )	
			{
				Period_ID = getLastPeriod() ;
				this.Period  = P_Period.get( Env.getCtx(), Period_ID, m_trxName );
			}
			else
			{
				Period_ID = m_Period.getP_Period_ID();
				this.Period = P_Period.get( Env.getCtx(), m_Period.getP_Period_ID(), m_trxName ); 
			}
*/			
			P_Period ParticularPeriod = P_Period.get( ctx, Period_ID, TrxName ); 
			
//			String sql = "select *, 1103861 as P_Gain_ID, 1103862 as P_Gain_GST_ID, round( amt * (tps_rate / 100),2)  as amt_gst "
			String sql = "select *, 1103861 as P_Gain_ID, 1103862 as P_Gain_GST_ID, 1103876 as P_Gain_TVQ_ID, 1103877 as P_Gain_TVH_ID  "
					+ " from P_Employee_Product "
					+ " inner join C_Charge on C_Charge.C_Charge_ID = P_Employee_Product.C_Charge_ID "
					+ " where P_Employee_Product.M_PRODUCT_ID = 1100001 AND P_Employee_Product.IsActive = 'Y' AND P_Employee_Product.P_Employee_ID = " + Employee_ID ;
			OriginTime = "DEP";
	//RETRO		OriginTime = "PER";

			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement (sql, TrxName);
				ResultSet rs = pstmt.executeQuery ();
				while (rs.next ())
				{

					MCharge Charge = new MCharge( ctx, rs.getInt( "C_Charge_ID"), null);

					// , round( amt * (tps_rate / 100),2)  as amt_gst
					BigDecimal amt = rs.getBigDecimal("Amt");

//					BigDecimal tps_rate = rs.getBigDecimal("tps_rate").divide( new BigDecimal(100));
//					BigDecimal amt_gst = amt.multiply( tps_rate ).setScale(2, BigDecimal.ROUND_HALF_UP);
					BigDecimal amt_gst = Env.ZERO;
					BigDecimal amt_tvq = Env.ZERO;
					BigDecimal amt_tvh = Env.ZERO;

					
					if ( Charge != null && Charge.getTPS_Rate() != null && Charge.getTPS_Rate().compareTo( Env.ZERO) != 0  )
						amt_gst = amt.multiply( Charge.getTPS_Rate().divide(new BigDecimal(100)) ) ;
					if ( Charge != null && Charge.getTVQ_Rate() != null && Charge.getTVQ_Rate().compareTo( Env.ZERO) != 0  )
						amt_tvq = amt.multiply( Charge.getTVQ_Rate().divide(new BigDecimal(100)) );
					if ( Charge != null && Charge.getTVH_Rate() != null && Charge.getTVH_Rate().compareTo( Env.ZERO) != 0  )
						amt_tvh = amt.multiply( Charge.getTVH_Rate().divide(new BigDecimal(100)) );
					
					
					
					PeriodDate = ParticularPeriod.getEndDate();
					
					GenStartDate  = PeriodDate;
					GenEndDate    = PeriodDate;
					if ( Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ) != 0)
						AssignmentParam = P_Assignment_Param.get( getCtx(), Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ), GenStartDate, m_trxName );
					else 
						AssignmentParam = Employee.getLastEmployeeAssignment(Employee.getP_Employee_ID(), PeriodDate, m_trxName);

					weekNumber = getWeek(new BigDecimal( TimeUtil.getDaysBetween(ParticularPeriod.getStartDate(), PeriodDate) +1 ).divide(new BigDecimal(7), 0, BigDecimal.ROUND_UP).intValue());

					Gain = P_Gain.get( ctx, rs.getInt("P_Gain_ID"), TrxName );
					Dayqty = amt.subtract(amt_gst).subtract(amt_tvq).subtract(amt_tvh);
//					Dayqty = rs.getBigDecimal("Amt").subtract(rs.getBigDecimal("Amt_Gst"));
					Hourly_Rate = Env.ZERO;
					
					Schedule = P_Schedule.get( ctx, 100000, TrxName );
					create_time_sheet_detail ( Period_ID );
					
					if ( amt_gst != null && amt_gst.compareTo(Env.ZERO) != 0 )
					{
						Gain = P_Gain.get( ctx, rs.getInt("P_Gain_GST_ID"), TrxName );
						Dayqty = amt_gst;
						create_time_sheet_detail ( Period_ID );
					}
					if ( amt_tvq != null && amt_tvq.compareTo(Env.ZERO) != 0 )
					{
						Gain = P_Gain.get( ctx, rs.getInt("P_Gain_TVQ_ID"), TrxName );
						Dayqty = amt_tvq;
						create_time_sheet_detail ( Period_ID );
					}
					if ( amt_tvh != null && amt_tvh.compareTo(Env.ZERO) != 0 )
					{
						Gain = P_Gain.get( ctx, rs.getInt("P_Gain_TVH_ID"), TrxName );
						Dayqty = amt_tvh;
						create_time_sheet_detail ( Period_ID );
					}

					
				}
				rs.close ();
				pstmt.close ();
				pstmt = null;
			}
			catch (Exception e)
			{
				log.log (Level.SEVERE,"GenerationExpense_Account_Det - " + sql, e);
			}
			
	    }
		

		
	}

	
	public String GenerationExpense_Account( Properties ctx, int Client_ID, int Org_ID, int Activity_ID ,int Employee_ID, int Payment_Group_ID, int Period_ID, int P_Distribution_Booklet_ID, int P_Collective_Labour_Agr_ID, int P_Time_Sheet_ID, String TrxName ) throws Exception
	{
		String sql = null;

		P_Period ParticularPeriod = P_Period.get( ctx, Period_ID, TrxName ); 
		Timestamp currentDate = new Timestamp( System.currentTimeMillis() );

		// paiement le 15 du mois alors on utilise le 8 vue que la période est 1 une semaine.
		Timestamp dateref = TimeUtil.getDay( TimeUtil.getYear(currentDate), TimeUtil.getMonth(currentDate) ,8 );

		if (  dateref.compareTo( ParticularPeriod.getStartDate() ) >= 0 &&
				  dateref.compareTo( ParticularPeriod.getEndDate() ) <= 0 )
		{
			sql = "Select P_Employee.P_Employee_ID, P_Payment_Group.P_Frequency_ID, P_Employee.P_Payment_Group_ID "
					+ " FROM P_Employee"
					+ " INNER JOIN  P_Payment_Group ON P_Payment_Group.P_Payment_Group_ID = P_Employee.P_Payment_Group_ID  "
		            + " WHERE P_Employee.AD_Client_ID = " + Client_ID
					+ " AND ( EXISTS( SELECT 1 FROM I_Expense_Account WHERE I_Expense_Account.P_Employee_ID = P_Employee.P_Employee_ID and isActive = 'Y' ) "
					+ "  OR EXISTS( SELECT 1 FROM P_Employee_Expensereports WHERE P_Employee_Expensereports.P_Employee_ID = P_Employee.P_Employee_ID and isActive = 'Y' ) "
					+ "  OR EXISTS( SELECT 1 FROM P_Employee_Product WHERE  M_PRODUCT_ID = 1100001 AND P_Employee_Product.P_Employee_ID = P_Employee.P_Employee_ID and isActive = 'Y' ))"
					+ ""
					;
			
		}
		else
		{
			sql = "Select P_Employee.P_Employee_ID, P_Payment_Group.P_Frequency_ID, P_Employee.P_Payment_Group_ID "
					+ " FROM P_Employee"
					+ " INNER JOIN  P_Payment_Group ON P_Payment_Group.P_Payment_Group_ID = P_Employee.P_Payment_Group_ID  "
		            + " WHERE P_Employee.AD_Client_ID = " + Client_ID
					+ " AND ( EXISTS( SELECT 1 FROM I_Expense_Account WHERE I_Expense_Account.P_Employee_ID = P_Employee.P_Employee_ID and isActive = 'Y' ) "
					+ "  OR EXISTS( SELECT 1 FROM P_Employee_Expensereports WHERE P_Employee_Expensereports.P_Employee_ID = P_Employee.P_Employee_ID and isActive = 'Y' ) "
//					+ "  OR EXISTS( SELECT 1 FROM P_Employee_Product WHERE  M_PRODUCT_ID = 1100001 AND P_Employee_Product.P_Employee_ID = P_Employee.P_Employee_ID and isActive = 'Y' )
					+ " )"
					+ ""
					;
			
		}

		

		if ( Employee_ID == 0 )
			sql += " AND P_Employee.IsActive='Y' AND P_Employee.P_LONGTERMLEAVE_ID <> 1000156 "; // Terminer

		
		if (Org_ID > 0)
			sql += "   AND P_Employee.AD_Org_ID = " + Org_ID;
			
			
		if (Activity_ID > 0)
			sql +=  "   AND P_Employee.C_Activity_ID = " + Activity_ID
			
			
//			+ "   AND P_Payment_Group.P_Frequency_ID= " + Period.getP_Frequency_ID() 
			;
		if (Employee_ID > 0)
			sql += " AND P_Employee.P_Employee_ID=" + Employee_ID;
		if (Payment_Group_ID > 0)
			sql += " AND P_Employee.P_Payment_Group_ID=" + Payment_Group_ID;

		if (P_Distribution_Booklet_ID > 0)
			sql += " AND P_Employee.P_Distribution_Booklet_ID=" + P_Distribution_Booklet_ID;
		
		if (P_Collective_Labour_Agr_ID > 0)
			sql += " AND P_Employee.P_Collective_Labour_Agr_ID=" + P_Collective_Labour_Agr_ID;
		

		if ( ! IsRework )
		{
			sql += " AND NOT EXISTS( SELECT 1 FROM P_TIME_SHEET WHERE P_TIME_SHEET.P_Employee_ID = P_Employee.P_Employee_ID AND P_TIME_SHEET.P_Period_ID = " + Period_ID + " AND SheetType = '" +  P_Time_Sheet.SHEETTYPE_ExpenseAccount + "')";
		}

		
		sql += " AND P_Employee.AD_Client_ID in ( " + Env.getAD_Client_ID(Env.getCtx()) + " , 0 ) ";
		

		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, m_trxName);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				
				if ( IsRework )
				{
					String sql2 = null;
			        sql2 = "delete from " + this.getTable() 
		            + " Where " + this.getTable() + ".P_Period_ID = " + Period_ID 
		            + " and " + this.getTable() + ".P_Employee_ID = " + rs.getInt("P_Employee_ID")
		            + " and SHEETTYPE = '" + P_Time_Sheet.SHEETTYPE_ExpenseAccount + "'"
		        	+ " and TimesheetStatus = 'I' "
		        	;
			        DB.executeUpdate(sql2, null);
				}


				
				Employee = P_Employee.get( ctx, rs.getInt( "P_Employee_ID"), TrxName );
				PaymentGroup = P_Payment_Group.get( getCtx(), rs.getInt("P_Payment_Group_ID"), null );
	
				Hourly_Rate = null ;
				AssignmentParam = null ;
				
				create_time_sheet( P_Time_Sheet.SHEETTYPE_ExpenseAccount, m_trxName);
//RETRO				create_time_sheet( P_Time_Sheet.SHEETTYPE_Complementary, m_trxName);

				GenerationExpense_Account_Det( ctx, Client_ID, Org_ID, rs.getInt("P_Employee_ID"),  Payment_Group_ID, Period_ID, TimeSheet.getRecord_ID(), TrxName );
				
				GenerationExpense_Account_DetV2( ctx, Client_ID, Org_ID, rs.getInt("P_Employee_ID"),  Payment_Group_ID, Period_ID, TimeSheet.getRecord_ID(), TrxName );
				
				
				if (  dateref.compareTo( ParticularPeriod.getStartDate() ) >= 0 &&
					  dateref.compareTo( ParticularPeriod.getEndDate() ) <= 0 )
			    {
					GenerationExpense_Cell_Det( ctx, Client_ID, Org_ID, rs.getInt("P_Employee_ID"),  Payment_Group_ID, Period_ID, TimeSheet.getRecord_ID(), TrxName ); 
					
			    }

				// 2025-06-09
				//TODO appeler le calcul pour vue qu'il n'y a pas de déduction.
				
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"GenerationExpense_Account - " + sql, e);
		}
	

		
		return "Enregistrements créés " + m_inserted;

	}
	


	public String Generation_Vacation_Import( Properties ctx, int Client_ID, int Org_ID, int Activity_ID ,int Employee_ID, int Payment_Group_ID, int Period_ID, int P_Distribution_Booklet_ID, int P_Collective_Labour_Agr_ID, int P_Time_Sheet_ID, String TrxName ) throws Exception
	{

		String sql = null;
		
		sql = "Select P_Employee.P_Employee_ID, P_Payment_Group.P_Frequency_ID, P_Employee.P_Payment_Group_ID,  I_Employee_NonBusinessDay.P_Period_ID "
			+ " FROM P_Employee "
			+ " INNER JOIN I_Employee_NonBusinessDay ON I_Employee_NonBusinessDay.P_Employee_ID = P_Employee.P_Employee_ID and I_Employee_NonBusinessDay.isActive = 'Y' " 
			+ " INNER JOIN  P_Payment_Group ON P_Payment_Group.P_Payment_Group_ID = P_Employee.P_Payment_Group_ID  "
            + " WHERE P_Employee.AD_Client_ID = " + Client_ID
			;

		if ( Employee_ID == 0 )
			sql += " AND P_Employee.IsActive='Y' ";

		
		if (Org_ID > 0)
			sql += "   AND P_Employee.AD_Org_ID = " + Org_ID;
			
			
/*		if (Activity_ID > 0)
			sql +=  "   AND P_Employee.C_Activity_ID = " + Activity_ID
*/			
			
//			+ "   AND P_Payment_Group.P_Frequency_ID= " + Period.getP_Frequency_ID() 
			;
		if (Employee_ID > 0)
			sql += " AND P_Employee.P_Employee_ID=" + Employee_ID;
		if (Payment_Group_ID > 0)
			sql += " AND P_Employee.P_Payment_Group_ID=" + Payment_Group_ID;

/*		if (P_Distribution_Booklet_ID > 0)
			sql += " AND P_Employee.P_Distribution_Booklet_ID=" + P_Distribution_Booklet_ID;
*/			

		if (P_Collective_Labour_Agr_ID > 0)
			sql += " AND P_Employee.P_Collective_Labour_Agr_ID =" + P_Collective_Labour_Agr_ID;
		
		sql += " AND P_Employee.AD_Client_ID in ( " + Env.getAD_Client_ID(Env.getCtx()) + " , 0 ) ";

		
		if ( ! IsRework )
		{
			sql += " AND NOT EXISTS( SELECT 1 FROM P_TIME_SHEET WHERE P_TIME_SHEET.P_Employee_ID = P_Employee.P_Employee_ID AND P_TIME_SHEET.P_Period_ID = " + Period_ID + " AND SheetType = '" +  P_Time_Sheet.SHEETTYPE_HolidayPayment + "')";
		}


		
		sql += " Group BY P_Employee.AD_Client_ID, P_Employee.AD_Org_ID, P_Employee.P_Employee_ID, P_Payment_Group.P_Frequency_ID, P_Employee.P_Payment_Group_ID,  I_Employee_NonBusinessDay.P_Period_ID";

		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, m_trxName);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
			
				if ( IsRework )
				{
					String sql2 = "delete from " + this.getTable() 
		            + " Where " + this.getTable() + ".P_Period_ID = " + Period_ID 
		            + " and " + this.getTable() + ".P_Employee_ID = " + rs.getInt("P_Employee_ID")
		            + " and SHEETTYPE = '" + P_Time_Sheet.SHEETTYPE_HolidayPayment + "'"
		        	+ " and TimesheetStatus = 'I' "
		        	;
			        DB.executeUpdate(sql2, null);
				}
				
				Employee = P_Employee.get( ctx, rs.getInt( "P_Employee_ID"), TrxName );
				PaymentGroup = P_Payment_Group.get( getCtx(), rs.getInt("P_Payment_Group_ID"), null );
	
				Hourly_Rate = null ;
				AssignmentParam = null ;

				Period = P_Period.getOpenPeriod(getCtx(), TrxName);

				create_time_sheet( P_Time_Sheet.SHEETTYPE_HolidayPayment, m_trxName);

				Generation_Vacation_Import_Det( ctx, rs.getInt("P_Employee_ID"),  rs.getInt("P_Period_ID"), TimeSheet.getRecord_ID(), TrxName ); 
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"Generation_Vacation_Import - " + sql, e);
		}
	

		
		return "Enregistrements créés " + m_inserted;

	}

	
//	public void Generation_Vacation_Import_Det( Properties ctx, int Client_ID, int Org_ID, int Employee_ID, int Payment_Group_ID, int Period_ID, int P_Time_Sheet_ID, String TrxName )
	public void Generation_Vacation_Import_Det( Properties ctx, int Employee_ID, int Period_ID, int P_Time_Sheet_ID, String TrxName )
	{
		String sql = "SELECT * FROM RV_I_Employee_NonBusinessDay WHERE RV_I_Employee_NonBusinessDay.P_Employee_ID = " + Employee_ID 
				   + " AND P_Period_ID = " + Period_ID;
		OriginTime = "VAC";

		
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, TrxName);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{

/*				X_I_Employee_NonBusinessDay Vac = new X_I_Employee_NonBusinessDay( ctx, rs, TrxName);
				Vac.setP_Time_Sheet_ID( TimeSheet.getRecord_ID() );
				Vac.save();
	*/			
				P_Period ParticularPeriod = P_Period.get( ctx, rs.getInt( "P_Period_ID"), TrxName ); 
				
				
				PeriodDate = rs.getTimestamp("PeriodDate");
				
				GenStartDate  = PeriodDate;
				GenEndDate    = PeriodDate;
				if ( Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ) != 0)
					AssignmentParam = P_Assignment_Param.get( getCtx(), Employee.getLastEmployeeAssignmentTime( TimeSheet.getRecord_ID() ), GenStartDate, m_trxName );
				else 
					AssignmentParam = Employee.getLastEmployeeAssignment(Employee.getP_Employee_ID(), PeriodDate, m_trxName);

				weekNumber = getWeek(new BigDecimal( TimeUtil.getDaysBetween(ParticularPeriod.getStartDate(), PeriodDate) +1 ).divide(new BigDecimal(7), 0, BigDecimal.ROUND_UP).intValue());

				Gain = P_Gain.get( ctx, rs.getInt("P_Gain_ID"), TrxName );
			
				Dayqty = rs.getBigDecimal("Qty");
				Hourly_Rate = Env.ZERO;
				
				Schedule = P_Schedule.get( ctx, 100000, TrxName );
				create_time_sheet_detail ( Period_ID );
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"Generation_Vacation_Import_Det - " + sql, e);
		}

		
	}
	

	private int getLastPeriod( )
	{
		
		String sql = "SELECT MAX(P_PERIOD_ID ) P_PERIOD_ID FROM P_PERIOD WHERE isAdjustmentPeriod = 'N' AND P_PERIOD_ID < " + m_Period.getP_Period_ID() + "   AND P_FREQUENCY_ID = " + PaymentGroup.getP_Frequency_ID();
		int Id = 0;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, m_trxName);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				Id = rs.getInt( "P_PERIOD_ID");
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"getLastPeriod - " + sql, e);
		}
	
		if ( Id == 0)
			return Period.getP_Period_ID();
			
		return Id;	
	}

	private BigDecimal getQuantityAdmissible213( IBookletTimeSheet sheet, Timestamp StartDate, Timestamp EndDate, String trxName ) throws Exception
	{
		BigDecimal Quantity = Env.ZERO;
		String sql = "SELECT isnull( SUM( P_TIME_SHEET_DETAIL.DAYQTY ), 0) Quantity "
			       + " FROM P_TIME_SHEET_DETAIL"
			       + " WHERE P_TIME_SHEET_DETAIL.P_Time_Sheet_ID = ? "
// Info gain std
                   + " AND P_TIME_SHEET_DETAIL.P_GAIN_ID IN ( select P_Gain_ID from P_GAIN_GAININFO where p_gaininfo_id = 1000014 and P_GAIN_GAININFO.TO_CONSIDER = 'Y' ) "
				   + " AND [Day] Between " + DB.TO_DATE( StartDate )
				   + " AND " + DB.TO_DATE( EndDate )
                   ;
        PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement(sql, trxName);
		pstmt.setInt(1, sheet.getRecord_ID() );
		
		ResultSet rs = pstmt.executeQuery();
		if (rs.next())
		{
			Quantity = rs.getBigDecimal("Quantity");
		}
		rs.close();
		pstmt.close();
		pstmt = null;
		
		return Quantity;
		
	}

	
}
