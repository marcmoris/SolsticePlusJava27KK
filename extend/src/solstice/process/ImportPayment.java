package solstice.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

import solstice.model.P_Assignment_Param;
import solstice.model.P_BankAccount;
import solstice.model.P_Employee;
import solstice.model.P_Employee_Deduction;
import solstice.model.P_Employee_Taxable_Benefit;
import solstice.model.P_Gain;
import solstice.model.P_Gain_Parameter;
import solstice.model.P_Payment;
import solstice.model.P_Payment_Deduction;
import solstice.model.P_Payment_Gain;
import solstice.model.P_Payment_Taxable_Benefit;
import solstice.model.P_Period;
import solstice.model.P_Region;
import solstice.model.P_Time_Sheet;
import solstice.model.P_Time_Sheet_Detail;
import solstice.model.X_I_Employee;
import solstice.model.X_I_Payment_Pay;
import solstice.model.X_I_YTD_Deduction;
import solstice.model.X_I_YTD_Gain;

public class ImportPayment extends SvrProcess {

	
	/**	Client to be imported to		*/
	private int				m_AD_Client_ID = 0;
	private int				m_P_Period_ID = 0;
	/**	Delete old Imported				*/
	private boolean			m_deleteOldImported = false;

	/** Effective						*/
	private Timestamp		m_DateValue = null;

	private X_I_YTD_Deduction imp;

	private String clientCheck ;
	private String trxName = null;
	private P_Payment payment = null;
	private P_Time_Sheet timesheet = null;
	private P_Period period = null;
	private P_Employee employee = null;
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("AD_Client_ID"))
				m_AD_Client_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("P_Period_ID"))
				m_P_Period_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("DeleteOldImported"))
				m_deleteOldImported = "Y".equals(para[i].getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		if (m_DateValue == null)
			m_DateValue = new Timestamp (System.currentTimeMillis());
	}	//	prepare


	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception
	 */
	protected String doIt() throws java.lang.Exception
	{
		int no = 0;
		
		clientCheck = " AND AD_Client_ID=" + m_AD_Client_ID;
		
		StringBuffer sql = new StringBuffer ("SELECT * FROM I_PAYMENT_PAY "
				+ "WHERE I_IsImported='N'").append(clientCheck)  
				.append(" Order by P_Employee_ID, P_Period_ID  ") 
				;
		log.info( "Sql " + sql);
		try
		{
			//TODO.
//			period = P_Period.get( getCtx(), m_P_Period_ID, trxName);

			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), trxName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				
				boolean isError = false;
				
				X_I_Payment_Pay impPayment = new X_I_Payment_Pay (getCtx(), rs, null);
				 
				
				if (  rs.getInt( "P_Employee_ID") == 0 )
				{
					impPayment.setI_ErrorMsg("Ce numéro d'employé n'existe pas");
					impPayment.setI_IsImported(false);
					impPayment.save();
					isError = true;
					commit();
				}
				if ( ! isError )
				{
					create_payment( impPayment );
				}
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
			System.err.println( "error : " + e);
		}
		

			
		

				
		return "";
	}

    private void create_payment( X_I_Payment_Pay impPayment )
    {
    	employee = P_Employee.get(getCtx(), impPayment.getP_Employee_ID(), trxName);
    	period = P_Period.get( getCtx(), impPayment.getP_Period_ID(), trxName);
    	
		timesheet = new P_Time_Sheet( getCtx(), -1, trxName );
		timesheet.setP_Employee_ID( impPayment.getP_Employee_ID() );
		timesheet.setAD_Org_ID( impPayment.getAD_Org_ID() );
		timesheet.setP_Department_ID( employee.getP_Department_ID() );
		timesheet.setC_Activity_ID( employee.getC_Activity_ID()  );
		
		timesheet.setIsActive(true);
//		timesheet.setP_Booklet_ID(employee.getP_Booklet_ID());
		timesheet.setP_Collective_Labour_Agr_ID(employee.getP_Collective_Labour_Agr_ID());
		timesheet.setP_Distribution_Booklet_ID( employee.getP_Distribution_Booklet_ID());
		timesheet.setP_Distribution_ID(employee.getP_Distribution_ID());
		timesheet.setP_Frequency_ID(period.getP_Frequency_ID());
		timesheet.setP_Job_Title_ID(employee.getP_Job_Title_ID());
		timesheet.setP_Job_Type_ID(employee.getP_Job_Type_ID());
		timesheet.setP_Occupation_Group_ID(employee.getP_Occupation_Group_ID());
		timesheet.setP_Payment_Group_ID(employee.getP_Payment_Group_ID());
		timesheet.setP_Period_ID(period.getP_Period_ID());
		timesheet.setIsComplete(true);
		timesheet.setP_Year_ID(period.getP_Year_ID());
		timesheet.setPayDate(period.getPayDate());
		timesheet.setPaymentType(P_Time_Sheet.PAYMENTTYPE_Deposit);
		timesheet.setSheetType(P_Time_Sheet.SHEETTYPE_Regular);
		timesheet.setTimeSheetStatus(P_Time_Sheet.TIMESHEETSTATUS_Transferred);

//		P_BankAccount BankAccount = P_BankAccount.getByOrgID(Env.getCtx(), this.getAD_Org_ID(), Payment_Group.getP_Bank_ID(),  null);
//		timesheet.setP_BankAccount_ID( BankAccount.getP_BankAccount_ID() );

		String value = DB.getDocumentNo (employee.getAD_Client_ID() , "P_Time_Sheet", trxName );
		timesheet.setValue( period.getName() + "-" + value.substring( 3, 7 ) );

		log.info( "save " + timesheet );

		if ( timesheet.save() )
		{
			payment = new P_Payment( getCtx(), -1, trxName);
			payment.setAD_Org_ID( timesheet.getAD_Org_ID());
			payment.setP_Department_ID( timesheet.getP_Department_ID() );
			payment.setC_Activity_ID(  timesheet.getC_Activity_ID() );

			payment.setAnnualSalary(employee.getAnnualSalary(period.getEndDate()));
			payment.setDateTransfer(period.getPayDate());
			payment.setIsActive(true);
			payment.setIsTransfer(true);
			payment.setP_Distribution_Booklet_ID(timesheet.getP_Distribution_Booklet_ID());
			payment.setP_Distribution_ID(timesheet.getP_Distribution_ID());
			payment.setP_Employee_ID(timesheet.getP_Employee_ID());
			
			payment.setTaxation_Region_ID( employee.getTaxation_Region_ID());

			
			payment.setP_Employer_ID(1000045);
			// la déduction détermine employeur
			
			payment.setP_Frequency_ID(timesheet.getP_Frequency_ID());
			payment.setP_Job_Title_ID(timesheet.getP_Job_Title_ID());
			payment.setP_Job_Type_ID(timesheet.getP_Job_Type_ID());
			payment.setP_Occupation_Group_ID(timesheet.getP_Occupation_Group_ID());
			payment.setP_Payment_Group_ID(timesheet.getP_Payment_Group_ID());
			payment.setP_Period_ID(timesheet.getP_Period_ID());
			payment.setP_Year_ID(timesheet.getP_Year_ID());
			payment.setPayDate(timesheet.getPayDate());
			payment.setPaymentType(timesheet.getPaymentType());
			payment.setPaymentTypeDoc(P_Payment.PAYMENTTYPEDOC_Regular);
//			payment.setTimeSheetStatus(timesheet.getTimeSheetStatus());
			payment.setValue(timesheet.getValue());
			if ( payment.save() )
			{
				timesheet.setP_Payment_ID(payment.getP_Payment_ID());
				timesheet.save();
			}
		}
    }
 
	private void import_deduction()
	{
		//	Go through Records
		StringBuffer sql = new StringBuffer ("SELECT i_YTD_Deduction_ID FROM i_YTD_Deduction "
			+ "WHERE I_IsImported='N'").append(clientCheck)  .append(" AND P_Period_ID is not null "  ) 
			.append( " and P_Period_ID = 1001937 " )
			.append(" Order by P_Employee_ID, P_Period_ID  ") 
			;
		log.info( "Sql " + sql);
		try
		{
			//TODO.
//			period = P_Period.get( getCtx(), m_P_Period_ID, trxName);

			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), trxName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				imp = new X_I_YTD_Deduction (getCtx(), rs.getInt(1), trxName);
				period = P_Period.get( getCtx(), imp.getP_Period_ID(), trxName);
				
				employee = P_Employee.get( getCtx(), imp.getP_Employee_ID(), trxName);

				int P_Payment_ID = getP_Payment_ID( imp.getP_Employee_ID(), imp.getP_Period_ID(), imp.getAD_Org_ID() );
//				if ( P_Payment_ID == 0 )
//					create_payment( imp.getP_Employee_ID(), imp.getAD_Org_ID(), imp.getP_Department_ID(),  imp.getC_Activity_ID(), imp.getACTIVITY(), imp.getOrg());
//				else
				{
					payment = P_Payment.get( getCtx() , P_Payment_ID, trxName);
					timesheet = P_Time_Sheet.GetWithPaymentID(getCtx() , P_Payment_ID, trxName);
				}


				P_Region Region = P_Region.get( Env.getCtx (), employee.getTaxation_Region_ID(), trxName); 

				if ( Region.getName().trim().equals(  "Québec" ) || Region.getName().trim().equals("QC") )
				{
					if ( imp.getDedn01() != null) add_deduction( payment, 1002712, imp.getDedn01(), "01" );
				}
				
				else 
				{
					if ( imp.getDedn01() != null) add_deduction( payment, getP_Deduction_ID("01"), imp.getDedn01(), "01" );
				}
				
				if ( imp.getDedn02() != null) add_deduction( payment, getP_Deduction_ID("02"), imp.getDedn02(), "02" );
				if ( imp.getDedn03() != null) add_deduction( payment, getP_Deduction_ID("03"), imp.getDedn03(), "03" );
				if ( imp.getDedn04() != null) add_deduction( payment, getP_Deduction_ID("04"), imp.getDedn04(), "04" );
				if ( imp.getDedn05() != null) add_deduction( payment, getP_Deduction_ID("05"), imp.getDedn05(), "05" );
				if ( imp.getDedn06() != null) add_deduction( payment, getP_Deduction_ID("06"), imp.getDedn06(), "06" );
				if ( imp.getDedn07() != null) add_deduction( payment, getP_Deduction_ID("07"), imp.getDedn07(), "07" );
				if ( imp.getDedn08() != null) add_deduction( payment, getP_Deduction_ID("08"), imp.getDedn08(), "08" );
				if ( imp.getDedn09() != null) add_deduction( payment, getP_Deduction_ID("09"), imp.getDedn09(), "09" );
				if ( imp.getDedn10() != null) add_deduction( payment, getP_Deduction_ID("10"), imp.getDedn10(), "10" );
				if ( imp.getDedn11() != null) add_deduction( payment, getP_Deduction_ID("11"), imp.getDedn11(), "11" );
				if ( imp.getDedn12() != null) add_deduction( payment, getP_Deduction_ID("12"), imp.getDedn12(), "12" );
				if ( imp.getDedn13() != null) add_deduction( payment, getP_Deduction_ID("13"), imp.getDedn13(), "13" );
				if ( imp.getDedn14() != null) add_deduction( payment, getP_Deduction_ID("14"), imp.getDedn14(), "14" );
				if ( imp.getDedn15() != null) add_deduction( payment, getP_Deduction_ID("15"), imp.getDedn15(), "15" );
				if ( imp.getDedn16() != null) add_deduction( payment, getP_Deduction_ID("16"), imp.getDedn16(), "16" );
				if ( imp.getDedn17() != null) add_deduction( payment, getP_Deduction_ID("17"), imp.getDedn17(), "17" );
				if ( imp.getDedn18() != null) add_deduction( payment, getP_Deduction_ID("18"), imp.getDedn18(), "18" );
				if ( imp.getDedn19() != null) add_deduction( payment, getP_Deduction_ID("19"), imp.getDedn19(), "19" );
				if ( imp.getDedn20() != null) add_deduction( payment, getP_Deduction_ID("20"), imp.getDedn20(), "20" );
				if ( imp.getDedn21() != null) add_deduction( payment, getP_Deduction_ID("21"), imp.getDedn21(), "21" );
				if ( imp.getDedn22() != null) add_deduction( payment, getP_Deduction_ID("22"), imp.getDedn22(), "22" );
				if ( imp.getDedn23() != null) add_deduction( payment, getP_Deduction_ID("23"), imp.getDedn23(), "23" );
				if ( imp.getDedn24() != null) add_deduction( payment, getP_Deduction_ID("24"), imp.getDedn24(), "24" );
				if ( imp.getDedn25() != null) add_deduction( payment, getP_Deduction_ID("25"), imp.getDedn25(), "25" );
				if ( imp.getDedn26() != null) add_deduction( payment, getP_Deduction_ID("26"), imp.getDedn26(), "26" );
				if ( imp.getDedn27() != null) add_deduction( payment, getP_Deduction_ID("27"), imp.getDedn27(), "27" );
				if ( imp.getDedn28() != null) add_deduction( payment, getP_Deduction_ID("28"), imp.getDedn28(), "28" );
				if ( imp.getDedn29() != null) add_deduction( payment, getP_Deduction_ID("29"), imp.getDedn29(), "29" );
				if ( imp.getDedn30() != null) add_deduction( payment, getP_Deduction_ID("30"), imp.getDedn30(), "30" );
				if ( imp.getDedn31() != null) add_deduction( payment, getP_Deduction_ID("31"), imp.getDedn31(), "31" );
				if ( imp.getDedn32() != null) add_deduction( payment, getP_Deduction_ID("32"), imp.getDedn32(), "32" );
				if ( imp.getDedn33() != null) add_deduction( payment, getP_Deduction_ID("33"), imp.getDedn33(), "33" );
				if ( imp.getDedn34() != null) add_deduction( payment, getP_Deduction_ID("34"), imp.getDedn34(), "34" );
				if ( imp.getDedn35() != null) add_deduction( payment, getP_Deduction_ID("35"), imp.getDedn35(), "35" );
				if ( imp.getDedn36() != null) add_deduction( payment, getP_Deduction_ID("36"), imp.getDedn36(), "36" );
				if ( imp.getDedn37() != null) add_deduction( payment, getP_Deduction_ID("37"), imp.getDedn37(), "37" );
				if ( imp.getDedn38() != null) add_deduction( payment, getP_Deduction_ID("38"), imp.getDedn38(), "38" );
				if ( imp.getDedn39() != null) add_deduction( payment, getP_Deduction_ID("39"), imp.getDedn39(), "39" );
				if ( imp.getDedn40() != null) add_deduction( payment, getP_Deduction_ID("40"), imp.getDedn40(), "40" );
				if ( imp.getDedn41() != null) add_deduction( payment, getP_Deduction_ID("41"), imp.getDedn41(), "41" );
				if ( imp.getDedn42() != null) add_deduction( payment, getP_Deduction_ID("42"), imp.getDedn42(), "42" );
				if ( imp.getDedn43() != null) add_deduction( payment, getP_Deduction_ID("43"), imp.getDedn43(), "43" );
				if ( imp.getDedn44() != null) add_deduction( payment, getP_Deduction_ID("44"), imp.getDedn44(), "44" );
				if ( imp.getDedn45() != null) add_deduction( payment, getP_Deduction_ID("45"), imp.getDedn45(), "45" );
				if ( imp.getDedn46() != null) add_deduction( payment, getP_Deduction_ID("46"), imp.getDedn46(), "46" );
				if ( imp.getDedn47() != null) add_deduction( payment, getP_Deduction_ID("47"), imp.getDedn47(), "47" );
				if ( imp.getDedn48() != null) add_deduction( payment, getP_Deduction_ID("48"), imp.getDedn48(), "48" );
				if ( imp.getDedn49() != null) add_deduction( payment, getP_Deduction_ID("49"), imp.getDedn49(), "49" );
				if ( imp.getDedn50() != null) add_deduction( payment, getP_Deduction_ID("50"), imp.getDedn50(), "50" );
				if ( imp.getDedn51() != null) add_deduction( payment, getP_Deduction_ID("51"), imp.getDedn51(), "51" );
				if ( imp.getDedn52() != null) add_deduction( payment, getP_Deduction_ID("52"), imp.getDedn52(), "52" );
				if ( imp.getDedn53() != null) add_deduction( payment, getP_Deduction_ID("53"), imp.getDedn53(), "53" );
				if ( imp.getDedn54() != null) add_deduction( payment, getP_Deduction_ID("54"), imp.getDedn54(), "54" );
				if ( imp.getDedn55() != null) add_deduction( payment, getP_Deduction_ID("55"), imp.getDedn55(), "55" );
				if ( imp.getDedn56() != null) add_deduction( payment, getP_Deduction_ID("56"), imp.getDedn56(), "56" );
				if ( imp.getDedn57() != null) add_deduction( payment, getP_Deduction_ID("57"), imp.getDedn57(), "57" );
				if ( imp.getDedn58() != null) add_deduction( payment, getP_Deduction_ID("58"), imp.getDedn58(), "58" );
				if ( imp.getDedn59() != null) add_deduction( payment, getP_Deduction_ID("59"), imp.getDedn59(), "59" );
				if ( imp.getDedn60() != null) add_deduction( payment, getP_Deduction_ID("60"), imp.getDedn60(), "60" );
				if ( imp.getDedn61() != null) add_deduction( payment, getP_Deduction_ID("61"), imp.getDedn61(), "61" );
				if ( imp.getDedn62() != null) add_deduction( payment, getP_Deduction_ID("62"), imp.getDedn62(), "62" );
//				 pas importé cumulatif 65 + gain 89	
				if ( imp.getDedn63() != null) add_deduction( payment, getP_Deduction_ID("63"), imp.getDedn63(), "63" );
				if ( imp.getDedn64() != null) add_deduction( payment, getP_Deduction_ID("64"), imp.getDedn64(), "64" );
				
//				if ( imp.getDedn65() != null) add_deduction( payment, getP_Deduction_ID("65"), imp.getDedn65(), "65" );
				if ( imp.getDedn66() != null) add_deduction( payment, getP_Deduction_ID("66"), imp.getDedn66(), "66" );
				if ( imp.getDedn67() != null) add_deduction( payment, getP_Deduction_ID("67"), imp.getDedn67(), "67" );
				if ( imp.getDedn68() != null) add_deduction( payment, getP_Deduction_ID("68"), imp.getDedn68(), "68" );
				if ( imp.getDedn69() != null) add_deduction( payment, getP_Deduction_ID("69"), imp.getDedn69(), "69" );
				if ( imp.getDedn70() != null) add_deduction( payment, getP_Deduction_ID("70"), imp.getDedn70(), "70" );
				if ( imp.getDedn71() != null) add_deduction( payment, getP_Deduction_ID("71"), imp.getDedn71(), "71" );
				if ( imp.getDedn72() != null) add_deduction( payment, getP_Deduction_ID("72"), imp.getDedn72(), "72" );
				if ( imp.getDedn73() != null) add_deduction( payment, getP_Deduction_ID("73"), imp.getDedn73(), "73" );
//				if ( imp.getDedn74() != null) add_deduction( payment, getP_Deduction_ID("74"), imp.getDedn74(), "74" );
				if ( imp.getDedn75() != null) add_deduction( payment, getP_Deduction_ID("75"), imp.getDedn75(), "75" );
				if ( imp.getDedn76() != null) add_deduction( payment, getP_Deduction_ID("76"), imp.getDedn76(), "76" );
				if ( imp.getDedn77() != null) add_deduction( payment, getP_Deduction_ID("77"), imp.getDedn77(), "77" );
				if ( imp.getDedn78() != null) add_deduction( payment, getP_Deduction_ID("78"), imp.getDedn78(), "78" );
				if ( imp.getDedn79() != null) add_deduction( payment, getP_Deduction_ID("79"), imp.getDedn79(), "79" );
				if ( imp.getDedn80() != null) add_deduction( payment, getP_Deduction_ID("80"), imp.getDedn80(), "80" );
				if ( imp.getDedn81() != null) add_deduction( payment, getP_Deduction_ID("81"), imp.getDedn81(), "81" );
				if ( imp.getDedn82() != null) add_deduction( payment, getP_Deduction_ID("82"), imp.getDedn82(), "82" );
				if ( imp.getDedn83() != null) add_deduction( payment, getP_Deduction_ID("83"), imp.getDedn83(), "83" );
				if ( imp.getDedn84() != null) add_deduction( payment, getP_Deduction_ID("84"), imp.getDedn84(), "84" );
				if ( imp.getDedn85() != null) add_deduction( payment, getP_Deduction_ID("85"), imp.getDedn85(), "85" );
				if ( imp.getDedn86() != null) add_deduction( payment, getP_Deduction_ID("86"), imp.getDedn86(), "86" );
				if ( imp.getDedn87() != null) add_deduction( payment, getP_Deduction_ID("87"), imp.getDedn87(), "87" );
				if ( imp.getDedn88() != null) add_deduction( payment, getP_Deduction_ID("88"), imp.getDedn88(), "88" );
//				if ( imp.getDedn89() != null) add_deduction( payment, getP_Deduction_ID("89"), imp.getDedn89(), "89" );
				if ( imp.getDedn90() != null) add_deduction( payment, getP_Deduction_ID("90"), imp.getDedn90(), "90" );
//*						if ( imp.getDedn91() != null) add_deduction( payment, getP_Deduction_ID("91"), imp.getDedn91(), "91" );
				if ( imp.getDedn92() != null) add_deduction( payment, getP_Deduction_ID("92"), imp.getDedn92(), "92" );
				if ( imp.getDedn93() != null) add_deduction( payment, getP_Deduction_ID("93"), imp.getDedn93(), "93" );
				if ( imp.getDedn94() != null) add_deduction( payment, getP_Deduction_ID("94"), imp.getDedn94(), "94" );
				if ( imp.getDedn95() != null) add_deduction( payment, getP_Deduction_ID("95"), imp.getDedn95(), "95" );
				if ( imp.getDedn96() != null) add_deduction( payment, getP_Deduction_ID("96"), imp.getDedn96(), "96" );
					
					
				if ( imp.getI_ErrorMsg() == null )
					imp.setI_IsImported(true);

				imp.save();
				
/*				String sql2 = "update P_Payment set Taxation_Region_ID =  p_deduction.C_Region_ID " + 
			      " from p_payment_deduction, p_deduction where p_deduction.c_region_id <> 0 " +
			      " and p_payment_deduction.p_deduction_id = p_deduction.p_deduction_id " +
			      " and P_Payment.P_Payment_ID = P_Payment_Deduction.P_Payment_ID " +
			      " and P_Payment.P_Payment_ID = " + payment.getP_Payment_ID(); 
				DB.executeUpdate(sql2, null);
*/
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
			System.err.println( "error : " + e);
		}

	}
	
	private void add_deduction( P_Payment payment, int P_Deduction_ID, BigDecimal employeePart, String deduction )
	{
		if ( P_Deduction_ID == 0 && employeePart.compareTo(Env.ZERO) != 0 )
		{
			if ( imp.getI_ErrorMsg() == null)
				imp.setI_ErrorMsg("");
			
			imp.setI_ErrorMsg( imp.getI_ErrorMsg() + " Déduction non trouvé -  " + deduction  + "\n");
			return;
		}

		if ( P_Deduction_ID == 0 )
			return;
		
//		BigDecimal cumAmount = getCumDeduction( payment.getP_Employee_ID(), payment.getP_Employer_ID(), P_Deduction_ID, payment.getTaxation_Region_ID()  );
//		BigDecimal amount = employeePart.subtract( cumAmount );

		BigDecimal amount = employeePart;

		if ( amount.compareTo(Env.ZERO) == 0 )
		  return;

		// Créer les déductions que ne sont pas présent dans le dossier employé.
		P_Employee_Deduction employeeDeduction = P_Employee_Deduction.get(getCtx(), payment.getP_Employee_ID(), P_Deduction_ID, period.getStartDate(), trxName);
		if ( employeeDeduction.getP_Employee_Deduction_ID() <= 0)
		{
			employeeDeduction.setAD_Org_ID( 0);
			employeeDeduction.setEffectIn(TimeUtil.getDay(2007, 01, 01));
//			employeeDeduction.setEffectTo(period.getEndDate());
			employeeDeduction.setIsActive(true);
			employeeDeduction.setP_Deduction_ID(P_Deduction_ID);
			employeeDeduction.setP_Employee_ID(payment.getP_Employee_ID());
			employeeDeduction.save();
		}
		
		
		P_Payment_Deduction pdeduction = new P_Payment_Deduction( getCtx(), -1, null);
		pdeduction.setAD_Org_ID(payment.getAD_Org_ID());
		pdeduction.setP_Employee_ID(payment.getP_Employee_ID());
		pdeduction.setP_Payment_ID(payment.getP_Payment_ID());
		pdeduction.setP_Period_ID(payment.getP_Period_ID());
		pdeduction.setP_Year_ID(payment.getP_Year_ID());
		pdeduction.setP_Deduction_ID( P_Deduction_ID);
		pdeduction.setP_Employee_Deduction_ID(employeeDeduction.getP_Employee_Deduction_ID());

		// Deduction 34 54 Hb assurance. Période 2007-01
/*		if ( ( P_Deduction_ID == 1002619 || P_Deduction_ID == 1002605 ) && m_P_Period_ID == 1001918)
		{
			pdeduction.setEmployee_Part( employeeDeduction.getEmployeeAmount() );
			pdeduction.setEmployer_Part(Env.ZERO);
		}
		else
		{
		*/
		// deduction 66 Kicker Company. et 61 = Company Pension exécutive par employEUR 
		if ( P_Deduction_ID != 1002630 && P_Deduction_ID != 1002713)
		{
			pdeduction.setEmployee_Part( amount );
			pdeduction.setEmployer_Part(Env.ZERO);
		}
		else
		{
			pdeduction.setEmployee_Part( Env.ZERO );
			pdeduction.setEmployer_Part( amount );
		}

		pdeduction.setSalary_Eligible(Env.ZERO);
		pdeduction.setHours_Eligible(Env.ZERO);
		pdeduction.setIsActive(true);
		pdeduction.setIsExonerated(false);
		pdeduction.setOrigine("FTP");
		pdeduction.save();
		
	}

	public int getP_Deduction_ID( String value )
	{
		int id = 0;
		String sql = "Select P_Deduction_ID from P_Deduction where value = rtrim('" + value + "')";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				id = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("ImportYTD getP_Deduction_ID - " + e);
		}
		
		return id;
		
	}

	public int getP_Gain_ID( String value )
	{
		int id = 0;
		String sql = "Select P_Gain_ID from P_Gain where isactive='Y' and value = rtrim('" + value + "')";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				id = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("ImportYTD getP_Gain_ID - " + e);
		}
		
		return id;
		
	}

	public int getP_Payment_ID( int P_Employee_ID, int P_Period_ID, int AD_Org_ID )
	{
		int id = 0;
		String sql = "Select P_Payment_ID from P_Payment where P_Employee_ID = " + P_Employee_ID + " And P_Period_ID = " + P_Period_ID + " And AD_Org_ID = " + AD_Org_ID;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				id = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("ImportYTD getP_Payment_ID - " + e);
		}
		
		return id;
		
	}

	
	public int getP_Taxable_Benefit_ID( String value )
	{
		int id = 0;
		String sql = "Select P_Taxable_Benefit_ID from P_Taxable_Benefit where value = rtrim('" + value + "')";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				id = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("ImportYTD getP_Taxable_Benefit_ID - " + e);
		}
		
		return id;
		
	}

	
	private int Line;
	private X_I_YTD_Gain impGain;
	
	private void import_gain( )
	{

		//	Go through Records
		StringBuffer sql = new StringBuffer ("SELECT i_YTD_Gain_ID FROM i_YTD_Gain "
			+ "WHERE I_IsImported='N'").append(clientCheck).append(" AND P_Period_ID is not null "  )
			.append( " and P_Period_ID = 1001937 " )
			.append( " Order By P_Employee_ID, P_Period_ID ")
            ;
//            .append( " AND employee = " + imp.getEmployee());            
//			+ " AND P_Period_ID = " + period.getP_Period_ID() 		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), trxName);
			ResultSet rs = pstmt.executeQuery();
			Line = 0;
			while (rs.next())
			{
				Line++;
				impGain = new X_I_YTD_Gain (getCtx(), rs.getInt(1), trxName);
				period = P_Period.get( getCtx(), impGain.getP_Period_ID(), trxName);

				employee = P_Employee.get( getCtx(), impGain.getP_Employee_ID(), trxName);
				
				int P_Payment_ID = getP_Payment_ID( impGain.getP_Employee_ID(), impGain.getP_Period_ID(), impGain.getAD_Org_ID() );
//				if ( P_Payment_ID == 0 )
//					create_payment( impGain.getP_Employee_ID(), impGain.getAD_Org_ID(), impGain.getP_Department_ID(),  impGain.getC_Activity_ID(), impGain.getACTIVITY(), impGain.getOrg() );
//				else
				{
					payment = P_Payment.get( getCtx() , P_Payment_ID, trxName);
					timesheet = P_Time_Sheet.GetWithPaymentID(getCtx() , P_Payment_ID, trxName);
				}
				
				if ( impGain.getEarn01() != null || impGain.getHours01() != null) add_gain( payment, getP_Gain_ID("01"), impGain.getEarn01(), impGain.getHours01(), "01" );
				if ( impGain.getEarn02() != null || impGain.getHours02() != null) add_gain( payment, getP_Gain_ID("02"), impGain.getEarn02(), impGain.getHours02(), "02" );
				if ( impGain.getEarn03() != null || impGain.getHours03() != null) add_gain( payment, getP_Gain_ID("03"), impGain.getEarn03(), impGain.getHours03(), "03" );
				if ( impGain.getEarn04() != null || impGain.getHours04() != null) add_gain( payment, getP_Gain_ID("04"), impGain.getEarn04(), impGain.getHours04(), "04" );
				if ( impGain.getEarn05() != null || impGain.getHours05() != null) add_gain( payment, getP_Gain_ID("05"), impGain.getEarn05(), impGain.getHours05(), "05" );
				if ( impGain.getEarn06() != null || impGain.getHours06() != null) add_gain( payment, getP_Gain_ID("06"), impGain.getEarn06(), impGain.getHours06(), "06" );
				if ( impGain.getEarn07() != null || impGain.getHours07() != null) add_gain( payment, getP_Gain_ID("07"), impGain.getEarn07(), impGain.getHours07(), "07" );
				if ( impGain.getEarn08() != null || impGain.getHours08() != null) add_gain( payment, getP_Gain_ID("08"), impGain.getEarn08(), impGain.getHours08(), "08" );
				if ( impGain.getEarn09() != null || impGain.getHours09() != null) add_gain( payment, getP_Gain_ID("09"), impGain.getEarn09(), impGain.getHours09(), "09" );
				if ( impGain.getEarn10() != null || impGain.getHours10() != null) add_gain( payment, getP_Gain_ID("10"), impGain.getEarn10(), impGain.getHours10(), "10" );
				if ( impGain.getEarn11() != null || impGain.getHours11() != null) add_gain( payment, getP_Gain_ID("11"), impGain.getEarn11(), impGain.getHours11(), "11" );
				if ( impGain.getEarn12() != null || impGain.getHours12() != null) add_gain( payment, getP_Gain_ID("12"), impGain.getEarn12(), impGain.getHours12(), "12" );
				if ( impGain.getEarn13() != null || impGain.getHours13() != null) add_gain( payment, getP_Gain_ID("13"), impGain.getEarn13(), impGain.getHours13(), "13" );
				if ( impGain.getEarn14() != null || impGain.getHours14() != null) add_gain( payment, getP_Gain_ID("14"), impGain.getEarn14(), impGain.getHours14(), "14" );
				if ( impGain.getEarn15() != null || impGain.getHours15() != null) add_gain( payment, getP_Gain_ID("15"), impGain.getEarn15(), impGain.getHours15(), "15" );
				if ( impGain.getEarn16() != null || impGain.getHours16() != null) add_gain( payment, getP_Gain_ID("16"), impGain.getEarn16(), impGain.getHours16(), "16" );
				if ( impGain.getEarn17() != null || impGain.getHours17() != null) add_gain( payment, getP_Gain_ID("17"), impGain.getEarn17(), impGain.getHours17(), "17" );
				
				if ( impGain.getEarn18() != null || impGain.getHours18() != null) add_gain( payment, getP_Gain_ID("18"), Env.ZERO, impGain.getEarn18(), "18" );
				if ( impGain.getEarn19() != null || impGain.getHours19() != null) add_gain( payment, getP_Gain_ID("19"), Env.ZERO, impGain.getEarn19(), "19" );
				
				if ( impGain.getEarn20() != null || impGain.getHours20() != null) add_gain( payment, getP_Gain_ID("20"), impGain.getEarn20(), impGain.getHours20(), "20" );
				if ( impGain.getEarn21() != null || impGain.getHours21() != null) add_gain( payment, getP_Gain_ID("21"), impGain.getEarn21(), impGain.getHours21(), "21" );
				if ( impGain.getEarn22() != null || impGain.getHours22() != null) add_gain( payment, getP_Gain_ID("22"), impGain.getEarn22(), impGain.getHours22(), "22" );
				if ( impGain.getEarn23() != null || impGain.getHours23() != null) add_gain( payment, getP_Gain_ID("23"), impGain.getEarn23(), impGain.getHours23(), "23" );
				if ( impGain.getEarn24() != null || impGain.getHours24() != null) add_gain( payment, getP_Gain_ID("24"), impGain.getEarn24(), impGain.getHours24(), "24" );
				if ( impGain.getEarn25() != null || impGain.getHours25() != null) add_gain( payment, getP_Gain_ID("25"), impGain.getEarn25(), impGain.getHours25(), "25" );
				if ( impGain.getEarn26() != null || impGain.getHours26() != null) add_gain( payment, getP_Gain_ID("26"), impGain.getEarn26(), impGain.getHours26(), "26" );
				if ( impGain.getEarn27() != null || impGain.getHours27() != null) add_gain( payment, getP_Gain_ID("27"), impGain.getEarn27(), impGain.getHours27(), "27" );
				if ( impGain.getEarn28() != null || impGain.getHours28() != null) add_gain( payment, getP_Gain_ID("28"), impGain.getEarn28(), impGain.getHours28(), "28" );
				if ( impGain.getEarn29() != null || impGain.getHours29() != null) add_gain( payment, getP_Gain_ID("29"), impGain.getEarn29(), impGain.getHours29(), "29" );
				if ( impGain.getEarn30() != null || impGain.getHours30() != null) add_gain( payment, getP_Gain_ID("30"), impGain.getEarn30(), impGain.getHours30(), "30" );
				if ( impGain.getEarn31() != null || impGain.getHours31() != null) add_gain( payment, getP_Gain_ID("31"), impGain.getEarn31(), impGain.getHours31(), "31" );
				if ( impGain.getEarn32() != null || impGain.getHours32() != null) add_gain( payment, getP_Gain_ID("32"), impGain.getEarn32(), impGain.getHours32(), "32" );
				if ( impGain.getEarn33() != null || impGain.getHours33() != null) add_gain( payment, getP_Gain_ID("33"), impGain.getEarn33(), impGain.getHours33(), "33" );
				if ( impGain.getEarn34() != null || impGain.getHours34() != null) add_gain( payment, getP_Gain_ID("34"), impGain.getEarn34(), impGain.getHours34(), "34" );
				if ( impGain.getEarn35() != null || impGain.getHours35() != null) add_gain( payment, getP_Gain_ID("35"), impGain.getEarn35(), impGain.getHours35(), "35" );
				if ( impGain.getEarn36() != null || impGain.getHours36() != null) add_gain( payment, getP_Gain_ID("36"), impGain.getEarn36(), impGain.getHours36(), "36" );
				if ( impGain.getEarn37() != null || impGain.getHours37() != null) add_gain( payment, getP_Gain_ID("37"), impGain.getEarn37(), impGain.getHours37(), "37" );
				if ( impGain.getEarn38() != null || impGain.getHours38() != null) add_gain( payment, getP_Gain_ID("38"), impGain.getEarn38(), impGain.getHours38(), "38" );
				if ( impGain.getEarn39() != null || impGain.getHours39() != null) add_gain( payment, getP_Gain_ID("39"), impGain.getEarn39(), impGain.getHours39(), "39" );
				if ( impGain.getEarn40() != null || impGain.getHours40() != null) add_gain( payment, getP_Gain_ID("40"), impGain.getEarn40(), impGain.getHours40(), "40" );
				if ( impGain.getEarn41() != null || impGain.getHours41() != null) add_gain( payment, getP_Gain_ID("41"), impGain.getEarn41(), impGain.getHours41(), "41" );
				if ( impGain.getEarn42() != null || impGain.getHours42() != null) add_gain( payment, getP_Gain_ID("42"), impGain.getEarn42(), impGain.getHours42(), "42" );
				if ( impGain.getEarn43() != null || impGain.getHours43() != null) add_gain( payment, getP_Gain_ID("43"), impGain.getEarn43(), impGain.getHours43(), "43" );
				if ( impGain.getEarn44() != null || impGain.getHours44() != null) add_gain( payment, getP_Gain_ID("44"), impGain.getEarn44(), impGain.getHours44(), "44" );
				if ( impGain.getEarn45() != null || impGain.getHours45() != null) add_gain( payment, getP_Gain_ID("45"), impGain.getEarn45(), impGain.getHours45(), "45" );
				if ( impGain.getEarn46() != null || impGain.getHours46() != null) add_gain( payment, getP_Gain_ID("46"), impGain.getEarn46(), impGain.getHours46(), "46" );
				if ( impGain.getEarn47() != null || impGain.getHours47() != null) add_gain( payment, getP_Gain_ID("47"), impGain.getEarn47(), impGain.getHours47(), "47" );
				if ( impGain.getEarn48() != null || impGain.getHours48() != null) add_gain( payment, getP_Gain_ID("48"), impGain.getEarn48(), impGain.getHours48(), "48" );
				if ( impGain.getEarn49() != null || impGain.getHours49() != null) add_gain( payment, getP_Gain_ID("49"), impGain.getEarn49(), impGain.getHours49(), "49" );
				if ( impGain.getEarn50() != null || impGain.getHours50() != null) add_gain( payment, getP_Gain_ID("50"), impGain.getEarn50(), impGain.getHours50(), "50" );
//				if ( impGain.getEarn51() != null || impGain.getHours51() != null) add_gain( payment, getP_Gain_ID("51"), impGain.getEarn51(), impGain.getHours51(), "51" );
//				if ( impGain.getEarn52() != null || impGain.getHours52() != null) add_gain( payment, getP_Gain_ID("52"), impGain.getEarn52(), impGain.getHours52(), "52" );
				if ( impGain.getEarn53() != null || impGain.getHours53() != null) add_gain( payment, getP_Gain_ID("53"), impGain.getEarn53(), impGain.getHours53(), "53" );
				if ( impGain.getEarn54() != null || impGain.getHours54() != null) add_gain( payment, getP_Gain_ID("54"), impGain.getEarn54(), impGain.getHours54(), "54" );
				if ( impGain.getEarn55() != null || impGain.getHours55() != null) add_gain( payment, getP_Gain_ID("55"), impGain.getEarn55(), impGain.getHours55(), "55" );
//				if ( impGain.getEarn56() != null || impGain.getHours56() != null) add_gain( payment, getP_Gain_ID("56"), impGain.getEarn56(), impGain.getHours56(), "56" );
				if ( impGain.getEarn57() != null || impGain.getHours57() != null) add_gain( payment, getP_Gain_ID("57"), impGain.getEarn57(), impGain.getHours57(), "57" );
				if ( impGain.getEarn58() != null || impGain.getHours58() != null) add_gain( payment, getP_Gain_ID("58"), impGain.getEarn58(), impGain.getHours58(), "58" );
				if ( impGain.getEarn59() != null || impGain.getHours59() != null) add_gain( payment, getP_Gain_ID("59"), impGain.getEarn59(), impGain.getHours59(), "59" );
				if ( impGain.getEarn60() != null || impGain.getHours60() != null) add_gain( payment, getP_Gain_ID("60"), impGain.getEarn60(), impGain.getHours60(), "60" );
				if ( impGain.getEarn61() != null || impGain.getHours61() != null) add_gain( payment, getP_Gain_ID("61"), impGain.getEarn61(), impGain.getHours61(), "61" );
				if ( impGain.getEarn62() != null || impGain.getHours62() != null) add_gain( payment, getP_Gain_ID("62"), impGain.getEarn62(), impGain.getHours62(), "62" );
				if ( impGain.getEarn63() != null || impGain.getHours63() != null) add_gain( payment, getP_Gain_ID("63"), impGain.getEarn63(), impGain.getHours63(), "63" );
				if ( impGain.getEarn64() != null || impGain.getHours64() != null) add_gain( payment, getP_Gain_ID("64"), impGain.getEarn64(), impGain.getHours64(), "64" );
				if ( impGain.getEarn65() != null || impGain.getHours65() != null) add_gain( payment, getP_Gain_ID("65"), impGain.getEarn65(), impGain.getHours65(), "65" );
				if ( impGain.getEarn66() != null || impGain.getHours66() != null) add_gain( payment, getP_Gain_ID("66"), impGain.getEarn66(), impGain.getHours66(), "66" );
				if ( impGain.getEarn67() != null || impGain.getHours67() != null) add_gain( payment, getP_Gain_ID("67"), impGain.getEarn67(), impGain.getHours67(), "67" );
				if ( impGain.getEarn68() != null || impGain.getHours68() != null) add_gain( payment, getP_Gain_ID("68"), impGain.getEarn68(), impGain.getHours68(), "68" );
				if ( impGain.getEarn69() != null || impGain.getHours69() != null) add_gain( payment, getP_Gain_ID("69"), impGain.getEarn69(), impGain.getHours69(), "69" );
				if ( impGain.getEarn70() != null || impGain.getHours70() != null) add_gain( payment, getP_Gain_ID("70"), impGain.getEarn70(), impGain.getHours70(), "70" );
				if ( impGain.getEarn71() != null || impGain.getHours71() != null) add_gain( payment, getP_Gain_ID("71"), impGain.getEarn71(), impGain.getHours71(), "71" );
				if ( impGain.getEarn72() != null || impGain.getHours72() != null) add_gain( payment, getP_Gain_ID("72"), impGain.getEarn72(), impGain.getHours72(), "72" );
				if ( impGain.getEarn73() != null || impGain.getHours73() != null) add_gain( payment, getP_Gain_ID("73"), impGain.getEarn73(), impGain.getHours73(), "73" );
				if ( impGain.getEarn74() != null || impGain.getHours74() != null) add_gain( payment, getP_Gain_ID("74"), impGain.getEarn74(), impGain.getHours74(), "74" );
				if ( impGain.getEarn75() != null || impGain.getHours75() != null) add_gain( payment, getP_Gain_ID("75"), impGain.getEarn75(), impGain.getHours75(), "75" );
				if ( impGain.getEarn76() != null || impGain.getHours76() != null) add_gain( payment, getP_Gain_ID("76"), impGain.getEarn76(), impGain.getHours76(), "76" );
				if ( impGain.getEarn77() != null || impGain.getHours77() != null) add_gain( payment, getP_Gain_ID("77"), impGain.getEarn77(), impGain.getHours77(), "77" );
//				if ( impGain.getEarn78() != null || impGain.getHours78() != null) add_gain( payment, getP_Gain_ID("78"), impGain.getEarn78(), impGain.getHours78(), "78" );
				if ( impGain.getEarn79() != null || impGain.getHours79() != null) add_gain( payment, getP_Gain_ID("79"), impGain.getEarn79(), impGain.getHours79(), "79" );
				if ( impGain.getEarn80() != null || impGain.getHours80() != null) add_gain( payment, getP_Gain_ID("80"), impGain.getEarn80(), impGain.getHours80(), "80" );
				if ( impGain.getEarn81() != null || impGain.getHours81() != null) add_gain( payment, getP_Gain_ID("81"), impGain.getEarn81(), impGain.getHours81(), "81" );
				if ( impGain.getEarn82() != null || impGain.getHours82() != null) add_gain( payment, getP_Gain_ID("82"), impGain.getEarn82(), impGain.getHours82(), "82" );
				if ( impGain.getEarn83() != null || impGain.getHours83() != null) add_gain( payment, getP_Gain_ID("83"), impGain.getEarn83(), impGain.getHours83(), "83" );
				if ( impGain.getEarn84() != null || impGain.getHours84() != null) add_gain( payment, getP_Gain_ID("84"), impGain.getEarn84(), impGain.getHours84(), "84" );
				if ( impGain.getEarn85() != null || impGain.getHours85() != null) add_gain( payment, getP_Gain_ID("85"), impGain.getEarn85(), impGain.getHours85(), "85" );
				if ( impGain.getEarn86() != null || impGain.getHours86() != null) add_gain( payment, getP_Gain_ID("86"), impGain.getEarn86(), impGain.getHours86(), "86" );
				if ( impGain.getEarn87() != null || impGain.getHours87() != null) add_gain( payment, getP_Gain_ID("87"), impGain.getEarn87(), impGain.getHours87(), "87" );
				if ( impGain.getEarn88() != null || impGain.getHours88() != null) add_gain( payment, getP_Gain_ID("88"), impGain.getEarn88(), impGain.getHours88(), "88" );
				if ( impGain.getEarn89() != null || impGain.getHours89() != null) add_gain( payment, getP_Gain_ID("89"), impGain.getEarn89(), impGain.getHours89(), "89" );
				if ( impGain.getEarn90() != null || impGain.getHours90() != null) add_gain( payment, getP_Gain_ID("90"), impGain.getEarn90(), impGain.getHours90(), "90" );
				if ( impGain.getEarn91() != null || impGain.getHours91() != null) add_gain( payment, getP_Gain_ID("91"), impGain.getEarn91(), impGain.getHours91(), "91" );
				if ( impGain.getEarn92() != null || impGain.getHours92() != null) add_gain( payment, getP_Gain_ID("92"), impGain.getEarn92(), impGain.getHours92(), "92" );
				if ( impGain.getEarn93() != null || impGain.getHours93() != null) add_gain( payment, getP_Gain_ID("93"), impGain.getEarn93(), impGain.getHours93(), "93" );
				if ( impGain.getEarn94() != null || impGain.getHours94() != null) add_gain( payment, getP_Gain_ID("94"), impGain.getEarn94(), impGain.getHours94(), "94" );
				if ( impGain.getEarn95() != null || impGain.getHours95() != null) add_gain( payment, getP_Gain_ID("95"), impGain.getEarn95(), impGain.getHours95(), "95" );
				if ( impGain.getEarn96() != null || impGain.getHours96() != null) add_gain( payment, getP_Gain_ID("96"), impGain.getEarn96(), impGain.getHours96(), "96" );

				if ( impGain.getI_ErrorMsg() == null )
					impGain.setI_IsImported(true);

				impGain.save();

			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "ERROR : ", e);
			System.err.println( "error : " + e);
		}

	}

	
	private void add_gain( P_Payment payment, int P_Gain_ID, BigDecimal Earn, BigDecimal Hours, String gain )
	{
		boolean taxablebenefit = false;
		int P_Taxable_Benefit_ID = 0;
		if ( P_Gain_ID == 0 )
		{
			P_Gain_ID = this.getP_Taxable_Benefit_ID(gain);
			if ( P_Gain_ID != 0 )
			{
				P_Taxable_Benefit_ID = P_Gain_ID;
				taxablebenefit = true;
			}
		}
			
		
		if ( P_Gain_ID == 0 && (Earn.compareTo(Env.ZERO) != 0 || Hours.compareTo(Env.ZERO) != 0 ) )
		{
			if ( impGain.getI_ErrorMsg() == null)
				impGain.setI_ErrorMsg("");
			
			impGain.setI_ErrorMsg( impGain.getI_ErrorMsg() + " Gain non trouvé -  " + gain  + "\n");
			return;
		}

//		if ( (Earn.compareTo(Env.ZERO) == 0 && Hours.compareTo(Env.ZERO) == 0 ) )
//			return;


		if ( ! taxablebenefit )
		{
/*
			BigDecimal cumEarn = getCumGain( payment.getP_Employee_ID(), payment.getP_Employer_ID(), P_Gain_ID, payment.getTaxation_Region_ID() );
			BigDecimal cumHours = getCumGainHours( payment.getP_Employee_ID(), payment.getP_Employer_ID(), P_Gain_ID, payment.getTaxation_Region_ID()  );
			BigDecimal Earn2 = Earn.subtract( cumEarn );

			BigDecimal Hours2 = Hours.subtract( cumHours );
*/
			BigDecimal Earn2 = Earn;

			BigDecimal Hours2 = Hours;

			if ( (Earn2.compareTo(Env.ZERO) == 0 && Hours2.compareTo(Env.ZERO) == 0 ) )
				return;

			
/*			// Gain 18 et 19
			if ( P_Gain_ID == 1003554 || P_Gain_ID == 1003555)
			{
				Hours = Hours.subtract( cumHours );
			}
*/
			P_Assignment_Param AssignmentParam = P_Assignment_Param.get( getCtx(), employee.GetPrincipalAssignment_Param(), trxName); 

			P_Time_Sheet_Detail detail = new P_Time_Sheet_Detail( getCtx(), -1, null);
			detail.setAD_Org_ID( timesheet.getAD_Org_ID());
//			detail.setC_Activity_ID( null);

			P_Gain Gain = P_Gain.get( getCtx(), P_Gain_ID, trxName );
//			if ( Gain.getP_UOM_ID() !=  100 )  // Heures 
//			if ( Hours.compareTo(Env.ZERO) != 0)
//				detail.setDayQty(Earn2);
//			else
				detail.setDayQty(Hours2);

			// Montant
			if ( Gain.getP_UOM_ID() == 101  && Hours2.compareTo(Env.ZERO) == 0)
				detail.setDayQty(Earn2);
				
			detail.setStartDate(period.getStartDate());
			detail.setEndDate(period.getEndDate());
			detail.setDay(period.getEndDate());
			detail.setIsActive(true);
			detail.setOriginTime("PER");
			detail.setP_Gain_ID(P_Gain_ID);
			detail.setP_Period_ID(period.getP_Period_ID());
			detail.setP_Time_Sheet_ID(timesheet.getP_Time_Sheet_ID());
			detail.setP_Assignment_ID(AssignmentParam.getP_Assignment_ID());
			detail.setWeekIndex(1);
			detail.save();
			
			P_Gain p_gain = P_Gain.get( getCtx(), P_Gain_ID, trxName);
			P_Gain_Parameter gainParam = P_Gain_Parameter.get(getCtx(), P_Gain_ID, 0, trxName);
				
			
			P_Payment_Gain pdetail = new P_Payment_Gain( getCtx(), -1, null);
			pdetail.setAD_Org_ID(timesheet.getAD_Org_ID());
			pdetail.setDay(detail.getDay());
			pdetail.setEndDate(detail.getEndDate());
			pdetail.setHourly_Rate(AssignmentParam.getHourly_Rate());
			pdetail.setIsActive(true);
			pdetail.setLine(Line);
			pdetail.setOriginTime(detail.getOriginTime());
			pdetail.setP_Assignment_ID(AssignmentParam.getP_Assignment_ID());
			pdetail.setP_Assignment_Param_ID(AssignmentParam.getP_Assignment_Param_ID());
			pdetail.setP_Collective_Labour_Agr_ID(timesheet.getP_Collective_Labour_Agr_ID());
			pdetail.setP_Employee_ID(timesheet.getP_Employee_ID());
			pdetail.setP_Gain_ID(P_Gain_ID);
			pdetail.setP_Gain_Parameter_ID(gainParam.getP_Gain_Parameter_ID());
			pdetail.setP_Job_Title_ID(timesheet.getP_Job_Title_ID());
			pdetail.setP_Payment_ID(payment.getP_Payment_ID());
			pdetail.setP_Period_ID(detail.getP_Period_ID());
			pdetail.setP_Salary_Scale_Detail_ID(AssignmentParam.getP_Salary_Scale_Detail_ID());
			pdetail.setP_Salary_Scale_ID(AssignmentParam.getP_Salary_Scale_ID());
			pdetail.setP_UOM_ID(p_gain.getP_UOM_ID());
			pdetail.setP_Year_ID(period.getP_Year_ID());
			pdetail.setQuantity(Hours2);
			pdetail.setQuantityCalc(Hours2);
			pdetail.setAmountCalc(Earn2);
			pdetail.setStartDate(detail.getStartDate());
			pdetail.setType_Rate(P_Payment_Gain.TYPE_RATE_EmployeeAffectation);
			
			pdetail.save();
			
		}
		else
		{

/*			BigDecimal cumEarn = getCumTaxableBenefit( payment.getP_Employee_ID(), payment.getP_Employer_ID(), P_Taxable_Benefit_ID, payment.getTaxation_Region_ID() );
			BigDecimal Earn2 = Earn.subtract( cumEarn );
*/
			BigDecimal Earn2 = Earn;

			if ( (Earn2.compareTo(Env.ZERO) == 0 ) )
				return;

			P_Payment_Taxable_Benefit pdetail = new P_Payment_Taxable_Benefit( getCtx(), -1, trxName);
			P_Employee_Taxable_Benefit emptb = P_Employee_Taxable_Benefit.get(getCtx(), payment.getP_Employee_ID(), P_Taxable_Benefit_ID, trxName);
			if ( emptb.getP_Employee_Taxable_Benefit_ID() <= 0 )
			{
				emptb.setAD_Org_ID(0);
				emptb.setEffectIn(TimeUtil.getDay(2007, 01, 01));
				emptb.setIsActive(true);
				emptb.setP_Employee_ID(payment.getP_Employee_ID());
				emptb.setP_Taxable_Benefit_ID(P_Taxable_Benefit_ID);
				emptb.save();
			}
			pdetail.setOrigine("FTP");
			pdetail.setIsActive(true);
			pdetail.setAD_Org_ID(payment.getAD_Org_ID());
			pdetail.setP_Employee_ID(payment.getP_Employee_ID());
			pdetail.setP_Employee_Taxable_Benefit_ID(emptb.getP_Employee_Taxable_Benefit_ID());
			pdetail.setP_Payment_ID(payment.getP_Payment_ID());
			pdetail.setP_Period_ID(payment.getP_Period_ID());
			pdetail.setP_Taxable_Benefit_ID(P_Taxable_Benefit_ID);
			pdetail.setP_Year_ID(payment.getP_Year_ID());
			pdetail.setTaxable_Benefit_Amount(Earn2);
			pdetail.save();
		}
	}

	private BigDecimal getCumDeduction( int P_Employee_ID, int P_Employer_ID, int P_Deduction_ID, int taxation_Region_ID) // , int AD_Org_ID 
	{
		BigDecimal cumEmpAmount = Env.ZERO;
		
		String query = null;
		query = "Select isnull( sum( Employee_Part ),0) Employee_Part, isnull(sum( Employer_Part),0) Employer_Part, isnull(sum( Salary_Eligible ), 0) Salary_Eligible, isnull( sum( Hours_Eligible ), 0) Hours_Eligible, isnull(sum( RetrievalAmount),0) RetrievalAmount, isnull(sum( AccumulationAmount),0) AccumulationAmount "
			+ " from P_Payment_Deduction, P_Payment"
			+ " where P_Payment_Deduction.P_Deduction_ID = " + P_Deduction_ID
			+ " and P_Payment.P_Employee_ID = " + P_Employee_ID
			+ " and P_Payment.P_Employer_ID = " + P_Employer_ID
			+ " and P_Payment.taxation_Region_ID = " + taxation_Region_ID
//			+ " and P_Payment.ad_org_id = " + AD_Org_ID
 		    + " and P_Payment_Deduction.P_Payment_ID = P_Payment.P_Payment_ID " 
 		    + " and P_Payment.P_Period_ID < " + period.getP_Period_ID() //m_P_Period_ID 
 		    ;
		//Log.trace( 5, EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " Query " + query ) ;
		
		try
		{
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (query, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				cumEmpAmount     =   rs.getBigDecimal("Employee_Part");
			}
			else
			{
				cumEmpAmount     =   Env.ZERO;
			}

			//Log.trace( 5,"calcul_Deduction - cumulatif period emp!! =" + cumEmpAmount );
			//Log.trace( 5,"calcul_Deduction - cumulatif period emr!! =" + cumEmprAmount );
			
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
			System.err.println( "error : " + e);
		}
    	return 	cumEmpAmount;
	}

	private BigDecimal getCumGain( int P_Employee_ID, int P_Employer_ID, int P_Gain_ID, int taxation_Region_ID  ) // int AD_Org_ID
	{
		BigDecimal cumEmpAmount = Env.ZERO;
		
		String query = null;
		query = "Select IsNull( sum ( amountCalc ), 0) as amountCalc "
			+ " from P_Payment_Gain, P_Payment"
			+ " where P_Payment_Gain.P_Gain_ID = " + P_Gain_ID
			+ " and P_Payment.P_Employee_ID = " + P_Employee_ID
			+ " and P_Payment.P_Employer_ID = " + P_Employer_ID
			+ " and P_Payment.taxation_Region_ID = " + taxation_Region_ID
//			+ " and P_Payment.ad_org_id = " + AD_Org_ID
 		    + " and P_Payment_Gain.P_Payment_ID = P_Payment.P_Payment_ID " 
 		    + " and P_Payment.P_Period_ID < " + period.getP_Period_ID() //m_P_Period_ID 
 		    ;
		//Log.trace( 5, EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " Query " + query ) ;
		
		try
		{
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (query, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				cumEmpAmount     =   rs.getBigDecimal("amountCalc");
			}
			else
			{
				cumEmpAmount     =   Env.ZERO;
			}

			//Log.trace( 5,"calcul_Deduction - cumulatif period emp!! =" + cumEmpAmount );
			//Log.trace( 5,"calcul_Deduction - cumulatif period emr!! =" + cumEmprAmount );
			
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
			System.err.println( "error : " + e);
		}
    	return 	cumEmpAmount;
	}

	private BigDecimal getCumGainHours( int P_Employee_ID, int P_Employer_ID, int P_Gain_ID, int taxation_Region_ID ) //, int AD_Org_ID
	{
		BigDecimal cumEmpAmount = Env.ZERO;
		
		String query = null;
		query = "Select IsNull( sum ( quantityCalc ), 0) as amountCalc "
			+ " from P_Payment_Gain, P_Payment"
			+ " where P_Payment_Gain.P_Gain_ID = " + P_Gain_ID
			+ " and P_Payment.P_Employee_ID = " + P_Employee_ID
			+ " and P_Payment.P_Employer_ID = " + P_Employer_ID
			+ " and P_Payment.taxation_Region_ID = " + taxation_Region_ID
//			+ " and P_Payment.ad_org_id = " + AD_Org_ID
 		    + " and P_Payment_Gain.P_Payment_ID = P_Payment.P_Payment_ID " 
 		    + " and P_Payment.P_Period_ID < " + period.getP_Period_ID() //m_P_Period_ID 
 		    ;
		//Log.trace( 5, EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " Query " + query ) ;
		
		try
		{
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (query, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				cumEmpAmount     =   rs.getBigDecimal("amountCalc");
			}
			else
			{
				cumEmpAmount     =   Env.ZERO;
			}

			//Log.trace( 5,"calcul_Deduction - cumulatif period emp!! =" + cumEmpAmount );
			//Log.trace( 5,"calcul_Deduction - cumulatif period emr!! =" + cumEmprAmount );
			
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
			System.err.println( "error : " + e);
		}
    	return 	cumEmpAmount;
	}

	private BigDecimal getCumTaxableBenefit( int P_Employee_ID, int P_Employer_ID, int P_Taxable_Benefit_ID, int taxation_Region_ID ) // , int AD_Org_ID
	{
		BigDecimal cumEmpAmount = Env.ZERO;
		
		String query = null;
		query = "Select IsNull(sum ( Taxable_Benefit_amount ),0) as Taxable_Benefit_amount "
			+ " from P_Payment_Taxable_Benefit, P_Payment"
			+ " where P_Payment_Taxable_Benefit.P_Taxable_Benefit_ID = " + P_Taxable_Benefit_ID
			+ " and P_Payment.P_Employee_ID = " + P_Employee_ID
			+ " and P_Payment.P_Employer_ID = " + P_Employer_ID
			+ " and P_Payment.taxation_Region_ID = " + taxation_Region_ID
//			+ " and P_Payment.ad_org_id = " + AD_Org_ID
 		    + " and P_Payment_Taxable_Benefit.P_Payment_ID = P_Payment.P_Payment_ID " 
 		    + " and P_Payment.P_Period_ID < " + period.getP_Period_ID() //m_P_Period_ID 
 		    ;
		//Log.trace( 5, EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " Query " + query ) ;
		
		try
		{
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (query, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				cumEmpAmount     =   rs.getBigDecimal("Taxable_Benefit_amount");
			}
			else
			{
				cumEmpAmount     =   Env.ZERO;
			}

			//Log.trace( 5,"calcul_Deduction - cumulatif period emp!! =" + cumEmpAmount );
			//Log.trace( 5,"calcul_Deduction - cumulatif period emr!! =" + cumEmprAmount );
			
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
			System.err.println( "error : " + e);
		}
    	return 	cumEmpAmount;
	}


}

	

