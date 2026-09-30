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

public class ImportPaymentKrispy extends SvrProcess {

	
	/**	Client to be imported to		*/
	private int				m_AD_Client_ID = 1000004;
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
		
		String sql2 = "UPDATE I_PAYMENT_GAIN set  P_PAYMENT_ID = ( SELECT I_PAYMENT_PAY.P_PAYMENT_ID FROM I_PAYMENT_PAY WHERE I_PAYMENT_PAY.PAY_CHQ_NO = I_PAYMENT_GAIN.PAY_CHQ_NO  )";
		DB.executeUpdate(sql2, null);
		sql2 = "UPDATE I_PAYMENT_DEDUCTION set  P_PAYMENT_ID = ( SELECT I_PAYMENT_PAY.P_PAYMENT_ID FROM I_PAYMENT_PAY WHERE I_PAYMENT_PAY.PAY_CHQ_NO = I_PAYMENT_DEDUCTION.PAY_CHQ_NO  )";
		DB.executeUpdate(sql2, null);
		
		sql2 = "update I_PAYMENT_DEDUCTION set p_period_id = ( select p_period_id from p_payment where p_payment.p_payment_id = i_payment_deduction.p_payment_id )";
		DB.executeUpdate(sql2, null);
		
		sql2 = "UPDATE I_PAYMENT_GAIN SET P_EMPLOYEE_id = ( SELECT P_EMPLOYEE_ID FROM P_PAYMENT WHERE P_PAYMENT.P_PAYMENT_ID = I_PAYMENT_GAIN.P_Payment_ID )";
    	DB.executeUpdate(sql2, null);

    	sql2 = "update I_PAYMENT_GAIN set p_period_id = ( select p_period_id from p_payment where p_payment.p_payment_id = I_PAYMENT_GAIN.p_payment_id )";
    	DB.executeUpdate(sql2, null);
    	

		create_payment_deduction();
		create_payment_gain();

				
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
		timesheet.setP_Employer_ID( 1100057 );
		
//		timesheet.setPayDate(period.getPayDate());
//		timesheet.setPayDate(impPayment.getPayDate());
		
		if ( impPayment.getPAY_CHQ_TYPE().trim().equals("D"))
			timesheet.setPaymentType(P_Time_Sheet.PAYMENTTYPE_Deposit);
		else
			timesheet.setPaymentType(P_Time_Sheet.PAYMENTTYPE_Cheque );

		if ( impPayment.getPAY_CHQ_GENRE().equals("1") )
			timesheet.setSheetType(P_Time_Sheet.SHEETTYPE_Regular);
		else
			timesheet.setSheetType(P_Time_Sheet.SHEETTYPE_Complementary );
		
		timesheet.setTimeSheetStatus(P_Time_Sheet.TIMESHEETSTATUS_Transferred);

//		P_BankAccount BankAccount = P_BankAccount.getByOrgID(Env.getCtx(), this.getAD_Org_ID(), Payment_Group.getP_Bank_ID(),  null);
//		timesheet.setP_BankAccount_ID( BankAccount.getP_BankAccount_ID() );

//		String value = DB.getDocumentNo (employee.getAD_Client_ID() , "P_Time_Sheet", trxName );
//		timesheet.setValue( period.getName() + "-" + value.substring( 3, 7 ) );
		timesheet.setValue( period.getName() + "-" + impPayment.getPAY_CHQ_NO()  );
		
		

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

			
			payment.setP_Employer_ID(1100057);
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
//			payment.setTransferred( true);
//			payment.setTransferredToBank( true );
			if ( payment.save() )
			{
				timesheet.setP_Payment_ID(payment.getP_Payment_ID());
				timesheet.save();
				
				impPayment.setP_Payment_ID( payment.getP_Payment_ID());
				impPayment.setI_IsImported(true);
				impPayment.save();
				
				
			}
		}
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

    private void create_payment_deduction(  )
    {
    	
		StringBuffer sql = new StringBuffer ("SELECT * FROM I_PAYMENT_DEDUCTION "
				+ "WHERE P_PAYMENT_ID IS NOT NULL AND I_IsImported='N'").append(clientCheck)  
//				.append( " AND P_Payment_ID = " + payment.getP_Payment_ID() )
				.append( " ORDER BY Pay_Chq_no ")
				;
		log.info( "Sql " + sql);
		try
		{

			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), trxName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				
				P_Payment payment = P_Payment.get( getCtx(), rs.getInt("P_Payment_ID"), null);
				P_Period period = P_Period.get( getCtx(), payment.getP_Period_ID(), null);
				
				boolean isError = false;
				
				int P_Deduction_ID = rs.getInt("P_Deduction_ID");
				
				// Créer les déductions que ne sont pas présent dans le dossier employé.
				P_Employee_Deduction employeeDeduction = P_Employee_Deduction.get(getCtx(), payment.getP_Employee_ID(), P_Deduction_ID, period.getStartDate(), trxName);
				if ( employeeDeduction.getP_Employee_Deduction_ID() <= 0)
				{
					employeeDeduction.setAD_Org_ID( 0);
					employeeDeduction.setEffectIn(TimeUtil.getDay(2007, 01, 01));
//					employeeDeduction.setEffectTo(period.getEndDate());
					employeeDeduction.setIsActive(true);
					employeeDeduction.setP_Deduction_ID( P_Deduction_ID);
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
				pdeduction.setEmployee_Part( rs.getBigDecimal("EMPLOYEE_PART" ) );
				pdeduction.setEmployer_Part( rs.getBigDecimal("EMPLOYER_PART" ) );
				pdeduction.setSalary_Eligible(rs.getBigDecimal("SALARY_ELIGIBLE" ));
				pdeduction.setHours_Eligible(rs.getBigDecimal("HOURS_ELIGIBLE" ));
				pdeduction.setIsActive(true);
				pdeduction.setIsExonerated(false);
				pdeduction.setOrigine("FTP");
				pdeduction.save();		
				
				
				String sql2 = "UPDATE I_PAYMENT_DEDUCTION set i_isImported = 'Y'  WHERE I_PAYMENT_DEDUCTION_ID = " + rs.getInt( "I_PAYMENT_DEDUCTION_ID");
				DB.executeUpdate(sql2, null);
				
			}
		rs.close();
		pstmt.close();
		}
		catch (Exception e)
		{
			System.err.println("ImportYTD create_payment_deduction - " + e);
		}
		
    }
    
	private void create_payment_gain(  )
	{
		
		StringBuffer sql = new StringBuffer ("SELECT * FROM I_PAYMENT_GAIN "
				+ "WHERE  I_IsImported='N'").append(clientCheck)  
//				.append( " AND P_Payment_ID = " + payment.getP_Payment_ID() )
				.append( " AND ( AmountCalc <> 0 OR QuantityCalc <> 0 )")
				.append( " ORDER BY P_Period_ID, P_Employee_ID ")
				;
		log.info( "Sql " + sql);
		try
		{

			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), trxName);
			ResultSet rs = pstmt.executeQuery();
			int Line = 0;
			int P_Employee_ID = 0;
			while (rs.next())
			{
				Line++;
				if ( rs.getInt("P_Employee_ID") != P_Employee_ID )
					Line = 1;
				
				P_Employee_ID = rs.getInt("P_Employee_ID");
				
				P_Payment payment = P_Payment.get( getCtx(), rs.getInt("P_Payment_ID"), null);
				P_Period period = P_Period.get( getCtx(), payment.getP_Period_ID(), null);
				
				P_Time_Sheet timesheet = P_Time_Sheet.GetWithPaymentID( getCtx(), payment.getP_Payment_ID(), null);
				
				employee = P_Employee.get( getCtx(), P_Employee_ID, null);
				
				boolean isError = false;

				
				boolean taxablebenefit = false;
				int P_Taxable_Benefit_ID = 0;
				
				int P_Gain_ID = rs.getInt("P_Gain_ID");
				
				if ( rs.getString("pay_gai_code").startsWith( "GS") )
				{
						P_Taxable_Benefit_ID = rs.getInt("P_Gain_ID");
						taxablebenefit = true;
				}
					
				if ( P_Gain_ID == 0  )
				{
//					impGain.setI_ErrorMsg( impGain.getI_ErrorMsg() + " Gain non trouvé -  " + gain  + "\n");
					return;
				}
			
				if ( ! taxablebenefit )
				{
		//			P_Assignment_Param AssignmentParam = P_Assignment_Param.get( getCtx(), employee.GetPrincipalAssignment_Param(), trxName); 

					P_Assignment_Param AssignmentParam = P_Assignment_Param.get( getCtx(), rs.getInt("P_Assignment_Param_ID"), trxName);
					P_Time_Sheet_Detail detail = new P_Time_Sheet_Detail( getCtx(), -1, null);
					detail.setAD_Org_ID( timesheet.getAD_Org_ID());
//					P_Gain Gain = P_Gain.get( getCtx(), P_Gain_ID, trxName );
					detail.setDayQty(rs.getBigDecimal("Quantity"));
					detail.setStartDate(period.getStartDate());
					detail.setEndDate(period.getEndDate());
					detail.setDay(period.getEndDate());
					detail.setIsActive(true);
					detail.setOriginTime("PER");
					detail.setP_Gain_ID(P_Gain_ID);
					detail.setP_Period_ID(period.getP_Period_ID());
					detail.setP_Time_Sheet_ID(timesheet.getP_Time_Sheet_ID());
//					detail.setP_Assignment_ID(AssignmentParam.getP_Assignment_ID());
					detail.setP_Assignment_ID( rs.getInt("P_Assignment_ID"));
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
//					pdetail.setP_Assignment_ID(AssignmentParam.getP_Assignment_ID());
					pdetail.setP_Assignment_ID( rs.getInt("P_Assignment_ID"));
//					pdetail.setP_Assignment_Param_ID(AssignmentParam.getP_Assignment_Param_ID());
					
					pdetail.setP_Assignment_Param_ID( rs.getInt("P_Assignment_Param_ID"));
					pdetail.setP_Post_ID(rs.getInt("P_Post_ID"));
					
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
					pdetail.setQuantity( rs.getBigDecimal("Quantity"));
					pdetail.setQuantityCalc(  rs.getBigDecimal("QuantityCalc") );
					pdetail.setAmountCalc( rs.getBigDecimal("AmountCalc"));
					pdetail.setStartDate(detail.getStartDate());
					pdetail.setType_Rate(P_Payment_Gain.TYPE_RATE_EmployeeAffectation);
					pdetail.setWeekIndex(1);
					
					pdetail.save();
					
				}
				else
				{

					P_Payment_Taxable_Benefit pdetail = new P_Payment_Taxable_Benefit( getCtx(), -1, trxName);
					P_Employee_Taxable_Benefit emptb = P_Employee_Taxable_Benefit.get(getCtx(), payment.getP_Employee_ID(), P_Taxable_Benefit_ID, trxName);
					if ( emptb.getP_Employee_Taxable_Benefit_ID() <= 0 )
					{
						emptb.setAD_Org_ID(0);
						emptb.setEffectIn(TimeUtil.getDay(2021, 01, 01));
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
					pdetail.setTaxable_Benefit_Amount( rs.getBigDecimal("AmountCalc") );
					pdetail.save();
				}


				
				String sql2 = "UPDATE I_PAYMENT_GAIN set i_isImported = 'Y'  WHERE I_PAYMENT_GAIN_ID = " + rs.getInt( "I_PAYMENT_GAIN_ID");
				DB.executeUpdate(sql2, null);
			
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e)
		{
			System.err.println("ImportYTD create_payment_gain - " + e);
		}

	}

	

}

	

