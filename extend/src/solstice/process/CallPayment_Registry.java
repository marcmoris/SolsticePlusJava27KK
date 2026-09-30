package solstice.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import solstice.model.*;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.model.MPInstancePara;

public class CallPayment_Registry extends SvrProcess {

	private int P_Frequency_ID=0;
	private int P_Period_ID=0;
	private int P_Payment_Group_ID=0;
	private int Taxation_Region_ID=0;
	private int P_Employee_ID=0;
	private int P_Employer_ID=0;
	private int P_Occupation_Group_ID=0;
	private int P_Distribution_Booklet_ID=0;
	private String _ClassErrorMessage="Error P_Payment_Registry_Fill_Table";
	private String m_Format = "PDF";

	private int P_Period_From=0;
	private int P_Period_To=0;
	private int AD_Pinstance_ID;

	/**
	 * 
	 */
	public CallPayment_Registry() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#prepare()
	 */
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		String paramName = "";
		
		//Read the parameters
		for(int i=0; i < para.length; i++){
			paramName = para[i].getParameterName();
			if( paramName.equals("P_Frequency_ID"))
			{
				this.P_Frequency_ID = para[i].getParameterAsInt();
			}
			else if (paramName.equals("P_Period_ID"))
			{
				this.P_Period_ID = para[i].getParameterAsInt();
			}
			else if (paramName.equals("P_Period_From"))
			{
				this.P_Period_From = para[i].getParameterAsInt();
			}
			else if (paramName.equals("P_Payment_Group_ID"))
			{
				this.P_Payment_Group_ID = para[i].getParameterAsInt();
			}
			else if (paramName.equals("P_Employee_ID"))
			{
				this.P_Employee_ID = para[i].getParameterAsInt();
			}
			else if (paramName.equals("P_Employer_ID"))
			{
				this.P_Employer_ID = para[i].getParameterAsInt();
			}
			else if (paramName.equals("P_Occupation_Group_ID"))
			{
				this.P_Occupation_Group_ID = para[i].getParameterAsInt();
			}
			else if (paramName.equals("P_Distribution_Booklet_ID"))
			{
				this.P_Distribution_Booklet_ID = para[i].getParameterAsInt();
			}
			else if (paramName.equals("Taxation_Region_ID"))
			{
				this.Taxation_Region_ID = para[i].getParameterAsInt();
			}
            else if (paramName.equals("CrystalFormat"))
				m_Format = (String)para[i].getParameter();
		}	
	}
	
	//
	// Donnée pour 1 période
	// 
	private void insertData()
	{
		String Sql = null;
		int count = 0;

		Sql = "Insert into rv_P_registry  ([ad_client_id] ,[ad_org_id] ,[p_year_id] ,[p_payment_group_id] ,[taxation_region_id], [p_employer_id] ,[p_employee_id] ,[p_last_payment_id] ,[p_period_id] ,[taxableAmountCumul] ,[GainAmountCalcCumul] ,[EmployeePartCumul] ,[EmployerPartCumul] ,[ad_PInstance_ID] ,[p_Distribution_Booklet_ID], P_Occupation_Group_ID)                 select [ad_client_id] ,[ad_org_id] ,[p_year_id] ,[p_payment_group_id] ,[taxation_region_id], [p_employer_id] ,[p_employee_id] ,[p_last_payment_id] ,[p_period_id] ,[taxableAmountCumul] ,[GainAmountCalcCumul] ,[EmployeePartCumul] ,[EmployerPartCumul] ," + AD_Pinstance_ID + " ,[p_Distribution_Booklet_ID], P_Occupation_Group_ID from rv_P_registry_view ";
		Sql += " Where P_Period_ID= " + P_Period_ID;
		if ( P_Payment_Group_ID != 0)
			Sql += " AND P_Payment_Group_ID= " + P_Payment_Group_ID;
		if ( P_Employee_ID != 0 )
			Sql += " AND P_Employee_ID= " + P_Employee_ID;

		if ( P_Employer_ID != 0 )
			Sql += " AND P_Employer_ID= " + P_Employer_ID;

		if (P_Occupation_Group_ID != 0)
			Sql += " AND P_Occupation_Group_ID= " + P_Occupation_Group_ID;
		if ( P_Distribution_Booklet_ID != 0)
			Sql += " AND P_Distribution_Booklet_ID= " + P_Distribution_Booklet_ID;

		if ( Taxation_Region_ID != 0)
			Sql += " AND Taxation_Region_ID= " + Taxation_Region_ID;

		//2019-05-08 ajout sécurité organisation
		Sql += " AND rv_P_registry_view.AD_Org_ID in ( SELECT AD_Org_ID FROM AD_Role_OrgAccess ra WHERE ra.AD_Role_ID= " + Env.getAD_Role_ID( Env.getCtx() ) + " AND ra.IsActive='Y')";
		
		count = DB.executeUpdate(Sql, null);
		
		Sql = "Insert into rv_P_Payment_registry  ([AD_Client_ID] ,[AD_Org_ID] ,[P_Payment_Group_ID] ,[P_Employee_ID] ,[P_Year_ID] ,[Taxation_Region_ID] , [p_employer_id],[P_LAST_PAYMENT_ID] ,[C_Activity_ID] ,[P_Department_ID] ,[P_Workplace_ID] ,[P_Distribution_Booklet_ID] ,[EmployeeValue] ,[EmployeeName] ,[sin] ,[P_Frequency_ID] ,[P_Period_ID] ,[CollectiveValue] ,[CollectiveName] ,[PaymentTypeDoc] ,[P_Payment_ID] ,[PaymentGroupName] ,[P_Occupation_Group_ID] ,[PreprintedNo] ,[PayDate] ,[PaymentValue] ,[PaymentType] ,[TaxableAmount] ,[GainAmountCalc] ,[EmployeePart] ,[EmployerPart] ,[TaxableAmountCumul] ,[GainAmountCalcCumul] ,[EmployeePartCumul] ,[EmployerPartCumul] ,[ad_PInstance_ID])          select  [AD_Client_ID] ,[AD_Org_ID] ,[P_Payment_Group_ID] ,[P_Employee_ID] ,[P_Year_ID] ,[Taxation_Region_ID] , [p_employer_id],[P_LAST_PAYMENT_ID] ,[C_Activity_ID] ,[P_Department_ID] ,[P_Workplace_ID] ,[P_Distribution_Booklet_ID] ,[EmployeeValue] ,[EmployeeName] ,[sin] ,[P_Frequency_ID] ,[P_Period_ID] ,[CollectiveValue] ,[CollectiveName] ,[PaymentTypeDoc] ,[P_Payment_ID] ,[PaymentGroupName] ,[P_Occupation_Group_ID] ,[PreprintedNo] ,[PayDate] ,[PaymentValue] ,[PaymentType] ,[TaxableAmount] ,[GainAmountCalc] ,[EmployeePart] ,[EmployerPart] ,[TaxableAmountCumul] ,[GainAmountCalcCumul] ,[EmployeePartCumul] ,[EmployerPartCumul] ,[ad_PInstance_ID] from rv_P_Payment_registry_view      WHERE AD_Pinstance_ID = " + AD_Pinstance_ID;
		count = DB.executeUpdate(Sql, null);
		Sql = "Insert into rv_P_Payment_registry_gain ([AD_ORG_ID] ,[P_YEAR_ID] ,[P_PAYMENT_GROUP_ID] ,[TAXATION_REGION_ID] , [p_employer_id],[P_PERIOD_ID] ,[P_EMPLOYEE_ID] ,[P_LAST_PAYMENT_ID] ,[P_PAYMENT_ID] ,[P_GAIN_ID] ,[VALUE] ,[NAME] ,[QUANTITYCALC] ,[AMOUNTCALC] ,[QUANTITYCUMUL] ,[AMOUNTCUMUL] ,[P_UOM_VALUE] ,[TYPE] ,[ad_PInstance_ID])     select [AD_ORG_ID] ,[P_YEAR_ID] ,[P_PAYMENT_GROUP_ID] ,[TAXATION_REGION_ID] , [p_employer_id],[P_PERIOD_ID] ,[P_EMPLOYEE_ID] ,[P_LAST_PAYMENT_ID] ,[P_PAYMENT_ID] ,[P_GAIN_ID] ,[VALUE] ,[NAME] ,[QUANTITYCALC] ,[AMOUNTCALC] ,[QUANTITYCUMUL] ,[AMOUNTCUMUL] ,[P_UOM_VALUE] ,[TYPE] ,[ad_PInstance_ID] from rv_P_Payment_registry_gain_view WHERE AD_Pinstance_ID = " + AD_Pinstance_ID;
		count = DB.executeUpdate(Sql, null);
		Sql = "Insert into rv_P_Payment_registry_deduction ([AD_ORG_ID] ,[P_YEAR_ID] ,[P_PAYMENT_GROUP_ID] ,[TAXATION_REGION_ID] , [p_employer_id],[P_PERIOD_ID] ,[P_EMPLOYEE_ID] ,[P_LAST_PAYMENT_ID] ,[P_PAYMENT_ID] ,[P_DEDUCTION_ID] ,[VALUE] ,[NAME] ,[EMPLOYEE_PART] ,[EMPLOYER_PART] ,[EMPLOYEE_PARTCUMUL] ,[EMPLOYER_PARTCUMUL] ,[ad_PInstance_ID]) select [AD_ORG_ID] ,[P_YEAR_ID] ,[P_PAYMENT_GROUP_ID] ,[TAXATION_REGION_ID], [p_employer_id] ,[P_PERIOD_ID] ,[P_EMPLOYEE_ID] ,[P_LAST_PAYMENT_ID] ,[P_PAYMENT_ID] ,[P_DEDUCTION_ID] ,[VALUE] ,[NAME] ,[EMPLOYEE_PART] ,[EMPLOYER_PART] ,[EMPLOYEE_PARTCUMUL] ,[EMPLOYER_PARTCUMUL] ,[ad_PInstance_ID] from rv_P_Payment_registry_deduction_view WHERE AD_Pinstance_ID = " + AD_Pinstance_ID;
		count = DB.executeUpdate(Sql, null);

		Sql = "INSERT INTO [RV_P_PAYMENT_REGISTRY_DEDUCTION_SUMMARY] ([AD_PINSTANCE_ID],[AD_ORG_ID],[P_YEAR_ID],[P_PAYMENT_GROUP_ID],[TAXATION_REGION_ID], [p_employer_id],[P_PERIOD_ID],[P_DEDUCTION_ID],[VALUE],[NAME],[EMPLOYEE_PART],[EMPLOYER_PART],[EMPLOYEE_PARTCUMUL],[EMPLOYER_PARTCUMUL]) SELECT [AD_PINSTANCE_ID],[AD_ORG_ID],[P_YEAR_ID],[P_PAYMENT_GROUP_ID],[TAXATION_REGION_ID], [p_employer_id],[P_PERIOD_ID],[P_DEDUCTION_ID],[VALUE],[NAME],[EMPLOYEE_PART],[EMPLOYER_PART],[EMPLOYEE_PARTCUMUL],[EMPLOYER_PARTCUMUL] FROM [RV_P_PAYMENT_REGISTRY_DEDUCTION_SUMMARY_VIEW] WHERE AD_Pinstance_ID = " + AD_Pinstance_ID;
		if ( P_Payment_Group_ID != 0)
			Sql += " AND P_Payment_Group_ID= " + P_Payment_Group_ID;

		if ( P_Employer_ID != 0 )
			Sql += " AND P_Employer_ID= " + P_Employer_ID;
		
		count = DB.executeUpdate(Sql, null);

		Sql = "INSERT INTO [RV_P_PAYMENT_REGISTRY_GAIN_SUMMARY] ([AD_PINSTANCE_ID],[AD_ORG_ID],[P_YEAR_ID],[P_PAYMENT_GROUP_ID],[TAXATION_REGION_ID], [p_employer_id],[P_PERIOD_ID],[P_GAIN_ID],[VALUE],[NAME],[QUANTITYCALC],[AMOUNTCALC],[QUANTITYCUMUL],[AMOUNTCUMUL],[P_UOM_VALUE],[TYPE]) SELECT [AD_PINSTANCE_ID],[AD_ORG_ID],[P_YEAR_ID],[P_PAYMENT_GROUP_ID],[TAXATION_REGION_ID], [p_employer_id],[P_PERIOD_ID],[P_GAIN_ID],[VALUE],[NAME],[QUANTITYCALC],[AMOUNTCALC],[QUANTITYCUMUL],[AMOUNTCUMUL],[P_UOM_VALUE],[TYPE] FROM RV_P_PAYMENT_REGISTRY_GAIN_SUMMARY_VIEW WHERE AD_Pinstance_ID = " + AD_Pinstance_ID;
		if ( P_Payment_Group_ID != 0)
			Sql += " AND P_Payment_Group_ID= " + P_Payment_Group_ID;
		
		if ( P_Employer_ID != 0 )
			Sql += " AND P_Employer_ID= " + P_Employer_ID;
		
		count = DB.executeUpdate(Sql, null);
		
		
	}

	private void multiPeriod()
	{
		int l_AD_Pinstance_ID =  AD_Pinstance_ID * 100 ;
		int l_AD_Pinstance_ID_From = l_AD_Pinstance_ID;
		int l_AD_Pinstance_ID_To = l_AD_Pinstance_ID;
		String sql = "Select P_Period_ID "
			       + " From P_Period " 
			       + " Where P_Period_ID Between " + P_Period_From + " and " + P_Period_To
		           + " Order By P_Period_ID ";
		
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        try
        {
	        ResultSet rs = stmt.executeQuery();
	        while(rs.next())
	        {
	        	insertDataMultiPeriod( l_AD_Pinstance_ID, rs.getInt("P_Period_ID"));
	        	l_AD_Pinstance_ID = l_AD_Pinstance_ID + 1;
	        }
	        
	        l_AD_Pinstance_ID_To = l_AD_Pinstance_ID;
	        rs.close();
	        stmt.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace(System.out);
        }

        String Sql;
        int count;
        
		Sql = "Insert into rv_P_registry  ([ad_client_id] ,[ad_org_id] ,[p_year_id] ,[p_payment_group_id] ,[taxation_region_id], [p_employer_id] ,[p_employee_id] ,[p_last_payment_id] ,[p_period_id] ,[taxableAmountCumul] ,[GainAmountCalcCumul] ,[EmployeePartCumul] ,[EmployerPartCumul] ,[ad_PInstance_ID] ,[p_Distribution_Booklet_ID]) "
			+ " Select [ad_client_id] ,[ad_org_id] ,[p_year_id] ,[p_payment_group_id] ,[taxation_region_id], [p_employer_id] ,[p_employee_id] ,[p_last_payment_id] ," + P_Period_To + " ,sum([taxableAmountCumul]) ,sum([GainAmountCalcCumul]) ,sum([EmployeePartCumul]) ,sum([EmployerPartCumul]) ," + AD_Pinstance_ID + " ,[p_Distribution_Booklet_ID] "
			+ " from rv_P_registry ";
		Sql += " Where AD_Pinstance_ID between " + l_AD_Pinstance_ID_From + " and " + l_AD_Pinstance_ID_To;
		Sql += " And P_Period_ID = " + P_Period_To;
		Sql += " Group By [ad_client_id] ,[ad_org_id] ,[p_year_id] ,[p_payment_group_id] ,[taxation_region_id], [p_employer_id] ,[p_employee_id] ,[p_last_payment_id] ,[p_Distribution_Booklet_ID]";
			
		count = DB.executeUpdate(Sql, null);
		Sql = "Insert into rv_P_Payment_registry  ([AD_Client_ID] ,[AD_Org_ID] ,[P_Payment_Group_ID] ,[P_Employee_ID] ,[P_Year_ID] ,[Taxation_Region_ID], [p_employer_id] ,[P_LAST_PAYMENT_ID] ,[C_Activity_ID] ,[P_Department_ID] ,[P_Workplace_ID] ,[P_Distribution_Booklet_ID] ,[EmployeeValue] ,[EmployeeName] ,[sin] ,[P_Frequency_ID] ,[P_Period_ID] ,[CollectiveValue] ,[CollectiveName] ,[PaymentTypeDoc] ,[P_Payment_ID] ,[PaymentGroupName] ,[P_Occupation_Group_ID] ,[PreprintedNo] ,[PayDate] ,[PaymentValue] ,[PaymentType] ,[TaxableAmount] ,[GainAmountCalc] ,[EmployeePart] ,[EmployerPart] ,[TaxableAmountCumul] ,[GainAmountCalcCumul] ,[EmployeePartCumul] ,[EmployerPartCumul] ,[ad_PInstance_ID])          " 
			+ " select  [AD_Client_ID] ,[AD_Org_ID] ,[P_Payment_Group_ID] ,[P_Employee_ID] ,[P_Year_ID] ,[Taxation_Region_ID], [p_employer_id] ,[P_LAST_PAYMENT_ID] ,[C_Activity_ID] ,[P_Department_ID] ,[P_Workplace_ID] ,[P_Distribution_Booklet_ID] ,[EmployeeValue] ,[EmployeeName] ,[sin] ,[P_Frequency_ID] ," + P_Period_To + " ,[CollectiveValue] ,[CollectiveName] ,'' ,max([P_Payment_ID]) ,[PaymentGroupName] ,max([P_Occupation_Group_ID]) ,max([PreprintedNo]) ,max([PayDate]) ,max([PaymentValue]) ,max([PaymentType]) ,sum([TaxableAmount]) ,sum([GainAmountCalc]) ,sum([EmployeePart]) ,sum([EmployerPart]) ,sum([TaxableAmount]) ,max(case when P_Period_ID = " + P_Period_To + " then [GainAmountCalcCumul] else 0 end) ,max(case when P_Period_ID = " + P_Period_To + " then [EmployeePartCumul] else 0 end) ,max(case when P_Period_ID = " + P_Period_To + " then [EmployerPartCumul] else 0 end) ," + this.AD_Pinstance_ID 
			+ " from rv_P_Payment_registry" 
			+ " WHERE AD_Pinstance_ID between " + l_AD_Pinstance_ID_From + " and " + l_AD_Pinstance_ID_To;
//		Sql += " And P_Period_ID = " + P_Period_To;
		Sql += " Group By [AD_Client_ID] ,[AD_Org_ID] ,[P_Payment_Group_ID] ,[P_Employee_ID] ,[P_Year_ID] ,[Taxation_Region_ID], [p_employer_id] ,[P_LAST_PAYMENT_ID] ,[C_Activity_ID] ,[P_Department_ID] ,[P_Workplace_ID] ,[P_Distribution_Booklet_ID] ,[EmployeeValue] ,[EmployeeName] ,[sin] ,[P_Frequency_ID] , [CollectiveValue] ,[CollectiveName] ,[PaymentGroupName] "; 
		
		count = DB.executeUpdate(Sql, null);
		
		Sql = "Insert into rv_P_Payment_registry_gain ([AD_ORG_ID] ,[P_YEAR_ID] ,[P_PAYMENT_GROUP_ID] ,[TAXATION_REGION_ID], [p_employer_id] ,[P_PERIOD_ID] ,[P_EMPLOYEE_ID] ,[P_LAST_PAYMENT_ID] ,[P_PAYMENT_ID] ,[P_GAIN_ID] ,[VALUE] ,[NAME] ,[QUANTITYCALC] ,[AMOUNTCALC] ,[QUANTITYCUMUL] ,[AMOUNTCUMUL] ,[P_UOM_VALUE] ,[TYPE] ,[ad_PInstance_ID]) "
			+ " select [AD_ORG_ID] ,[P_YEAR_ID] ,[P_PAYMENT_GROUP_ID] ,[TAXATION_REGION_ID], [p_employer_id] ," + P_Period_To + " ,[P_EMPLOYEE_ID] ,[P_LAST_PAYMENT_ID] ,max([P_PAYMENT_ID]) ,[P_GAIN_ID] ,[VALUE] ,[NAME] ,sum([QUANTITYCALC]) ,sum([AMOUNTCALC]) ,sum([QUANTITYCUMUL]) ,sum([AMOUNTCUMUL]) ,[P_UOM_VALUE] ,[TYPE] ," + this.AD_Pinstance_ID 
			+ " from rv_P_Payment_registry_gain" 
			+ " WHERE AD_Pinstance_ID between " + l_AD_Pinstance_ID_From + " and " + l_AD_Pinstance_ID_To;
		Sql += " Group By [AD_ORG_ID] ,[P_YEAR_ID] ,[P_PAYMENT_GROUP_ID] ,[TAXATION_REGION_ID] , [p_employer_id],[P_EMPLOYEE_ID] ,[P_LAST_PAYMENT_ID] ,[P_GAIN_ID] ,[VALUE] ,[NAME], [P_UOM_VALUE] ,[TYPE]";
		
		count = DB.executeUpdate(Sql, null);
		Sql = "Insert into rv_P_Payment_registry_deduction ([AD_ORG_ID] ,[P_YEAR_ID] ,[P_PAYMENT_GROUP_ID] ,[TAXATION_REGION_ID], [p_employer_id] ,[P_PERIOD_ID] ,[P_EMPLOYEE_ID] ,[P_LAST_PAYMENT_ID] ,[P_PAYMENT_ID] ,[P_DEDUCTION_ID] ,[VALUE] ,[NAME] ,[EMPLOYEE_PART] ,[EMPLOYER_PART] ,[EMPLOYEE_PARTCUMUL] ,[EMPLOYER_PARTCUMUL] ,[ad_PInstance_ID]) "
			+ " select [AD_ORG_ID] ,[P_YEAR_ID] ,[P_PAYMENT_GROUP_ID] ,[TAXATION_REGION_ID], [p_employer_id] ," + P_Period_To + " ,[P_EMPLOYEE_ID] ,[P_LAST_PAYMENT_ID] ,MAX([P_PAYMENT_ID]) ,[P_DEDUCTION_ID] ,[VALUE] ,[NAME] ,sum([EMPLOYEE_PART]) ,sum([EMPLOYER_PART]) ,sum([EMPLOYEE_PARTCUMUL]) ,sum([EMPLOYER_PARTCUMUL]) ," + this.AD_Pinstance_ID
			+ " from rv_P_Payment_registry_deduction" 
			+ " WHERE AD_Pinstance_ID between " + l_AD_Pinstance_ID_From + " and " + l_AD_Pinstance_ID_To;
		Sql += " Group By [AD_ORG_ID] ,[P_YEAR_ID] ,[P_PAYMENT_GROUP_ID] ,[TAXATION_REGION_ID], [p_employer_id] , [P_EMPLOYEE_ID] ,[P_LAST_PAYMENT_ID] , [P_DEDUCTION_ID] ,[VALUE] ,[NAME] ";
		count = DB.executeUpdate(Sql, null);


		Sql = "INSERT INTO [RV_P_PAYMENT_REGISTRY_DEDUCTION_SUMMARY] ([AD_PINSTANCE_ID],[AD_ORG_ID],[P_YEAR_ID],[P_PAYMENT_GROUP_ID],[TAXATION_REGION_ID], [p_employer_id],[P_PERIOD_ID],[P_DEDUCTION_ID],[VALUE],[NAME],[EMPLOYEE_PART],[EMPLOYER_PART],[EMPLOYEE_PARTCUMUL],[EMPLOYER_PARTCUMUL]) " 
			+ " SELECT " + this.AD_Pinstance_ID + ",[AD_ORG_ID],[P_YEAR_ID],[P_PAYMENT_GROUP_ID],[TAXATION_REGION_ID], [p_employer_id]," + P_Period_To + " ,[P_DEDUCTION_ID],[VALUE],[NAME],sum([EMPLOYEE_PART]),sum([EMPLOYER_PART]),sum([EMPLOYEE_PARTCUMUL]),sum([EMPLOYER_PARTCUMUL]) FROM [RV_P_PAYMENT_REGISTRY_DEDUCTION_SUMMARY] "
			+ " WHERE AD_Pinstance_ID between " + l_AD_Pinstance_ID_From + " and " + l_AD_Pinstance_ID_To;
		Sql += " Group By [AD_ORG_ID],[P_YEAR_ID],[P_PAYMENT_GROUP_ID],[TAXATION_REGION_ID], [p_employer_id],[P_DEDUCTION_ID],[VALUE],[NAME] ";
		count = DB.executeUpdate(Sql, null);

		Sql = "INSERT INTO [RV_P_PAYMENT_REGISTRY_GAIN_SUMMARY] ([AD_PINSTANCE_ID],[AD_ORG_ID],[P_YEAR_ID],[P_PAYMENT_GROUP_ID],[TAXATION_REGION_ID], [p_employer_id],[P_PERIOD_ID],[P_GAIN_ID],[VALUE],[NAME],[QUANTITYCALC],[AMOUNTCALC],[QUANTITYCUMUL],[AMOUNTCUMUL],[P_UOM_VALUE],[TYPE]) " 
			+ " SELECT " + this.AD_Pinstance_ID + ",[AD_ORG_ID],[P_YEAR_ID],[P_PAYMENT_GROUP_ID],[TAXATION_REGION_ID], [p_employer_id]," + P_Period_To + " ,[P_GAIN_ID],[VALUE],[NAME],sum([QUANTITYCALC]),sum([AMOUNTCALC]),sum([QUANTITYCUMUL]),sum([AMOUNTCUMUL]),[P_UOM_VALUE],[TYPE] FROM RV_P_PAYMENT_REGISTRY_GAIN_SUMMARY "
			+ " WHERE AD_Pinstance_ID between " + l_AD_Pinstance_ID_From + " and " + l_AD_Pinstance_ID_To;
		Sql += " Group By [AD_ORG_ID],[P_YEAR_ID],[P_PAYMENT_GROUP_ID],[TAXATION_REGION_ID], [p_employer_id],[P_GAIN_ID],[VALUE],[NAME],[P_UOM_VALUE],[TYPE] ";
		count = DB.executeUpdate(Sql, null);

        
	}
	//
	// Donnée pour plusieurs périodes
	// 
	private void insertDataMultiPeriod( int AD_Pinstance_ID, int P_Period_ID )
	{
		String Sql = null;
		int count = 0;
		
		Sql = "Insert into rv_P_registry  ([ad_client_id] ,[ad_org_id] ,[p_year_id] ,[p_payment_group_id] ,[taxation_region_id], [p_employer_id] ,[p_employee_id] ,[p_last_payment_id] ,[p_period_id] ,[taxableAmountCumul] ,[GainAmountCalcCumul] ,[EmployeePartCumul] ,[EmployerPartCumul] ,[ad_PInstance_ID] ,[p_Distribution_Booklet_ID]) "
			+ " Select [ad_client_id] ,[ad_org_id] ,[p_year_id] ,[p_payment_group_id] ,[taxation_region_id], [p_employer_id] ,[p_employee_id] ,[p_last_payment_id] ,[p_period_id] ,[taxableAmountCumul] ,[GainAmountCalcCumul] ,[EmployeePartCumul] ,[EmployerPartCumul] ," + AD_Pinstance_ID + " ,[p_Distribution_Booklet_ID] "
			+ " from rv_P_registry_view ";
		Sql += " Where P_Period_ID= " + P_Period_ID;

		if ( P_Payment_Group_ID != 0)
			Sql += " AND P_Payment_Group_ID= " + P_Payment_Group_ID;
		if ( P_Employee_ID != 0 )
			Sql += " AND P_Employee_ID= " + P_Employee_ID;

		if ( P_Employer_ID != 0 )
			Sql += " AND P_Employer_ID= " + P_Employer_ID;

		
		if (P_Occupation_Group_ID != 0)
			Sql += " AND P_Occupation_Group_ID= " + P_Occupation_Group_ID;
		if ( P_Distribution_Booklet_ID != 0)
			Sql += " AND P_Distribution_Booklet_ID= " + P_Distribution_Booklet_ID;

		if ( Taxation_Region_ID != 0)
			Sql += " AND Taxation_Region_ID= " + Taxation_Region_ID;

		//2019-05-08 ajout sécurité organisation
		Sql += " AND rv_P_registry_view.AD_Org_ID in ( SELECT AD_Org_ID FROM AD_Role_OrgAccess ra WHERE ra.AD_Role_ID= " + Env.getAD_Role_ID( Env.getCtx() ) + " AND ra.IsActive='Y')";

		
		count = DB.executeUpdate(Sql, null);

		Sql = "Insert into rv_P_Payment_registry  ([AD_Client_ID] ,[AD_Org_ID] ,[P_Payment_Group_ID] ,[P_Employee_ID] ,[P_Year_ID] ,[Taxation_Region_ID], [p_employer_id] ,[P_LAST_PAYMENT_ID] ,[C_Activity_ID] ,[P_Department_ID] ,[P_Workplace_ID] ,[P_Distribution_Booklet_ID] ,[EmployeeValue] ,[EmployeeName] ,[sin] ,[P_Frequency_ID] ,[P_Period_ID] ,[CollectiveValue] ,[CollectiveName] ,[PaymentTypeDoc] ,[P_Payment_ID] ,[PaymentGroupName] ,[P_Occupation_Group_ID] ,[PreprintedNo] ,[PayDate] ,[PaymentValue] ,[PaymentType] ,[TaxableAmount] ,[GainAmountCalc] ,[EmployeePart] ,[EmployerPart] ,[TaxableAmountCumul] ,[GainAmountCalcCumul] ,[EmployeePartCumul] ,[EmployerPartCumul] ,[ad_PInstance_ID])          " 
			+ " select  [AD_Client_ID] ,[AD_Org_ID] ,[P_Payment_Group_ID] ,[P_Employee_ID] ,[P_Year_ID] ,[Taxation_Region_ID], [p_employer_id] ,[P_LAST_PAYMENT_ID] ,[C_Activity_ID] ,[P_Department_ID] ,[P_Workplace_ID] ,[P_Distribution_Booklet_ID] ,[EmployeeValue] ,[EmployeeName] ,[sin] ,[P_Frequency_ID] ," + P_Period_ID + " ,[CollectiveValue] ,[CollectiveName] ,'' ,[P_Payment_ID] ,[PaymentGroupName] ,[P_Occupation_Group_ID] ,[PreprintedNo] ,[PayDate] ,[PaymentValue] ,[PaymentType] ,[TaxableAmount] ,[GainAmountCalc] ,[EmployeePart] ,[EmployerPart] ,[TaxableAmountCumul] ,[GainAmountCalcCumul] ,[EmployeePartCumul] ,[EmployerPartCumul] , [ad_PInstance_ID]"  
			+ " from rv_P_Payment_registry_view WHERE AD_Pinstance_ID = " + AD_Pinstance_ID
			;
		count = DB.executeUpdate(Sql, null);

		
		Sql = "Insert into rv_P_Payment_registry_gain ([AD_ORG_ID] ,[P_YEAR_ID] ,[P_PAYMENT_GROUP_ID] ,[TAXATION_REGION_ID], [p_employer_id] ,[P_PERIOD_ID] ,[P_EMPLOYEE_ID] ,[P_LAST_PAYMENT_ID] ,[P_PAYMENT_ID] ,[P_GAIN_ID] ,[VALUE] ,[NAME] ,[QUANTITYCALC] ,[AMOUNTCALC] ,[QUANTITYCUMUL] ,[AMOUNTCUMUL] ,[P_UOM_VALUE] ,[TYPE] ,[ad_PInstance_ID]) "
				+ " select [AD_ORG_ID] ,[P_YEAR_ID] ,[P_PAYMENT_GROUP_ID] ,[TAXATION_REGION_ID], [p_employer_id] ,[P_PERIOD_ID] ,[P_EMPLOYEE_ID] ,[P_LAST_PAYMENT_ID] ,[P_PAYMENT_ID] ,[P_GAIN_ID] ,[VALUE] ,[NAME] ,[QUANTITYCALC] ,[AMOUNTCALC] ,0 ,0 ,[P_UOM_VALUE] ,[TYPE] ,[ad_PInstance_ID] " 
				+ " from rv_P_Payment_registry_gain_view WHERE AD_Pinstance_ID = " + AD_Pinstance_ID
			    ;
		        
		count = DB.executeUpdate(Sql, null);
		
		Sql = "Insert into rv_P_Payment_registry_deduction ([AD_ORG_ID] ,[P_YEAR_ID] ,[P_PAYMENT_GROUP_ID] ,[TAXATION_REGION_ID], [p_employer_id] ,[P_PERIOD_ID] ,[P_EMPLOYEE_ID] ,[P_LAST_PAYMENT_ID] ,[P_PAYMENT_ID] ,[P_DEDUCTION_ID] ,[VALUE] ,[NAME] ,[EMPLOYEE_PART] ,[EMPLOYER_PART] ,[EMPLOYEE_PARTCUMUL] ,[EMPLOYER_PARTCUMUL] ,[ad_PInstance_ID]) "
				+ " select [AD_ORG_ID] ,[P_YEAR_ID] ,[P_PAYMENT_GROUP_ID] ,[TAXATION_REGION_ID], [p_employer_id] ,[P_PERIOD_ID] ,[P_EMPLOYEE_ID] ,[P_LAST_PAYMENT_ID] ,[P_PAYMENT_ID] ,[P_DEDUCTION_ID] ,[VALUE] ,[NAME] ,[EMPLOYEE_PART] ,[EMPLOYER_PART] ,0 ,0 ,[ad_PInstance_ID] "
				+ " from rv_P_Payment_registry_deduction_view WHERE AD_Pinstance_ID = " + AD_Pinstance_ID
				;
		count = DB.executeUpdate(Sql, null);

		Sql = "INSERT INTO [RV_P_PAYMENT_REGISTRY_DEDUCTION_SUMMARY] ([AD_PINSTANCE_ID],[AD_ORG_ID],[P_YEAR_ID],[P_PAYMENT_GROUP_ID],[TAXATION_REGION_ID], [p_employer_id],[P_PERIOD_ID],[P_DEDUCTION_ID],[VALUE],[NAME],[EMPLOYEE_PART],[EMPLOYER_PART],[EMPLOYEE_PARTCUMUL],[EMPLOYER_PARTCUMUL]) SELECT [AD_PINSTANCE_ID],[AD_ORG_ID],[P_YEAR_ID],[P_PAYMENT_GROUP_ID],[TAXATION_REGION_ID], [p_employer_id],[P_PERIOD_ID],[P_DEDUCTION_ID],[VALUE],[NAME],[EMPLOYEE_PART],[EMPLOYER_PART],0,0 FROM [RV_P_PAYMENT_REGISTRY_DEDUCTION_SUMMARY_VIEW] WHERE AD_Pinstance_ID = " + AD_Pinstance_ID;

		if ( P_Payment_Group_ID != 0)
			Sql += " AND P_Payment_Group_ID= " + P_Payment_Group_ID;

		if ( P_Employer_ID != 0 )
			Sql += " AND P_Employer_ID= " + P_Employer_ID;

		count = DB.executeUpdate(Sql, null);

		Sql = "INSERT INTO [RV_P_PAYMENT_REGISTRY_GAIN_SUMMARY] ([AD_PINSTANCE_ID],[AD_ORG_ID],[P_YEAR_ID],[P_PAYMENT_GROUP_ID],[TAXATION_REGION_ID], [p_employer_id],[P_PERIOD_ID],[P_GAIN_ID],[VALUE],[NAME],[QUANTITYCALC],[AMOUNTCALC],[QUANTITYCUMUL],[AMOUNTCUMUL],[P_UOM_VALUE],[TYPE]) SELECT [AD_PINSTANCE_ID],[AD_ORG_ID],[P_YEAR_ID],[P_PAYMENT_GROUP_ID],[TAXATION_REGION_ID], [p_employer_id],[P_PERIOD_ID],[P_GAIN_ID],[VALUE],[NAME],[QUANTITYCALC],[AMOUNTCALC],0,0,[P_UOM_VALUE],[TYPE] FROM RV_P_PAYMENT_REGISTRY_GAIN_SUMMARY_VIEW WHERE AD_Pinstance_ID = " + AD_Pinstance_ID;

		if ( P_Payment_Group_ID != 0)
			Sql += " AND P_Payment_Group_ID= " + P_Payment_Group_ID;

		if ( P_Employer_ID != 0 )
			Sql += " AND P_Employer_ID= " + P_Employer_ID;

		count = DB.executeUpdate(Sql, null);
		
		
		// Les cumulatif sont insérer seulement pour la dernière period et seulement pour 1 seul payment par employée.
		if ( P_Period_ID == P_Period_To)
		{
			Sql = "Insert into rv_P_Payment_registry_gain ([AD_ORG_ID] ,[P_YEAR_ID] ,[P_PAYMENT_GROUP_ID] ,[TAXATION_REGION_ID], [p_employer_id] ,[P_PERIOD_ID] ,[P_EMPLOYEE_ID] ,[P_LAST_PAYMENT_ID] ,[P_PAYMENT_ID] ,[P_GAIN_ID] ,[VALUE] ,[NAME] ,[QUANTITYCALC] ,[AMOUNTCALC] ,[QUANTITYCUMUL] ,[AMOUNTCUMUL] ,[P_UOM_VALUE] ,[TYPE] ,[ad_PInstance_ID]) "
				+ " select [AD_ORG_ID] ,[P_YEAR_ID] ,[P_PAYMENT_GROUP_ID] ,[TAXATION_REGION_ID], [p_employer_id] ,[P_PERIOD_ID] ,[P_EMPLOYEE_ID] ,[P_LAST_PAYMENT_ID] ,max([P_PAYMENT_ID]) ,[P_GAIN_ID] ,[VALUE] ,[NAME] ,0 ,0 ,sum([QUANTITYCUMUL]) ,sum([AMOUNTCUMUL]) ,[P_UOM_VALUE] ,[TYPE] ,[ad_PInstance_ID] " 
				+ " from rv_P_Payment_registry_gain_view WHERE AD_Pinstance_ID = " + AD_Pinstance_ID
				+ "  AND P_Payment_ID in ( Select Max(P_Payment_ID) From rv_P_Payment_registry Where rv_P_Payment_registry.AD_Pinstance_ID = " + AD_Pinstance_ID + " And rv_P_Payment_registry.P_Employee_ID = rv_P_Payment_registry_gain_view.P_Employee_ID )"
			    + " group by [AD_ORG_ID] ,[P_YEAR_ID] ,[P_PAYMENT_GROUP_ID] ,[TAXATION_REGION_ID] , [p_employer_id],[P_PERIOD_ID] ,[P_EMPLOYEE_ID] ,[P_LAST_PAYMENT_ID],[P_GAIN_ID] ,[VALUE] ,[NAME],[P_UOM_VALUE] ,[TYPE] ,[ad_PInstance_ID]"
			    ;

			count = DB.executeUpdate(Sql, null);

			Sql = "Insert into rv_P_Payment_registry_deduction ([AD_ORG_ID] ,[P_YEAR_ID] ,[P_PAYMENT_GROUP_ID] ,[TAXATION_REGION_ID], [p_employer_id] ,[P_PERIOD_ID] ,[P_EMPLOYEE_ID] ,[P_LAST_PAYMENT_ID] ,[P_PAYMENT_ID] ,[P_DEDUCTION_ID] ,[VALUE] ,[NAME] ,[EMPLOYEE_PART] ,[EMPLOYER_PART] ,[EMPLOYEE_PARTCUMUL] ,[EMPLOYER_PARTCUMUL] ,[ad_PInstance_ID]) "
				+ " select [AD_ORG_ID] ,[P_YEAR_ID] ,[P_PAYMENT_GROUP_ID] ,[TAXATION_REGION_ID], [p_employer_id] ,[P_PERIOD_ID] ,[P_EMPLOYEE_ID] ,[P_LAST_PAYMENT_ID] ,max([P_PAYMENT_ID]) ,[P_DEDUCTION_ID] ,[VALUE] ,[NAME] ,0 ,0 ,sum([EMPLOYEE_PARTCUMUL]) ,sum([EMPLOYER_PARTCUMUL]) ,[ad_PInstance_ID] "
				+ " from rv_P_Payment_registry_deduction_view WHERE AD_Pinstance_ID = " + AD_Pinstance_ID
				+ "  AND P_Payment_ID in ( Select Max(P_Payment_ID) From rv_P_Payment_registry Where rv_P_Payment_registry.AD_Pinstance_ID = " + AD_Pinstance_ID + " And rv_P_Payment_registry.P_Employee_ID = rv_P_Payment_registry_deduction_view.P_Employee_ID )"
				+ " group by [AD_ORG_ID] ,[P_YEAR_ID] ,[P_PAYMENT_GROUP_ID] ,[TAXATION_REGION_ID], [p_employer_id] ,[P_PERIOD_ID] ,[P_EMPLOYEE_ID] ,[P_LAST_PAYMENT_ID] ,[P_DEDUCTION_ID] ,[VALUE] ,[NAME] ,[ad_PInstance_ID] "
				;
			
			count = DB.executeUpdate(Sql, null);

			Sql = "INSERT INTO [RV_P_PAYMENT_REGISTRY_DEDUCTION_SUMMARY] ([AD_PINSTANCE_ID],[AD_ORG_ID],[P_YEAR_ID],[P_PAYMENT_GROUP_ID],[TAXATION_REGION_ID], [p_employer_id],[P_PERIOD_ID],[P_DEDUCTION_ID],[VALUE],[NAME],[EMPLOYEE_PART],[EMPLOYER_PART],[EMPLOYEE_PARTCUMUL],[EMPLOYER_PARTCUMUL]) SELECT [AD_PINSTANCE_ID],[AD_ORG_ID],[P_YEAR_ID],[P_PAYMENT_GROUP_ID],[TAXATION_REGION_ID], [p_employer_id],[P_PERIOD_ID],[P_DEDUCTION_ID],[VALUE],[NAME],0,0,[EMPLOYEE_PARTCUMUL],[EMPLOYER_PARTCUMUL] FROM [RV_P_PAYMENT_REGISTRY_DEDUCTION_SUMMARY_VIEW] WHERE AD_Pinstance_ID = " + AD_Pinstance_ID;

			if ( P_Payment_Group_ID != 0)
				Sql += " AND P_Payment_Group_ID= " + P_Payment_Group_ID;

			if ( P_Employer_ID != 0 )
				Sql += " AND P_Employer_ID= " + P_Employer_ID;

			count = DB.executeUpdate(Sql, null);

			Sql = "INSERT INTO [RV_P_PAYMENT_REGISTRY_GAIN_SUMMARY] ([AD_PINSTANCE_ID],[AD_ORG_ID],[P_YEAR_ID],[P_PAYMENT_GROUP_ID],[TAXATION_REGION_ID], [p_employer_id],[P_PERIOD_ID],[P_GAIN_ID],[VALUE],[NAME],[QUANTITYCALC],[AMOUNTCALC],[QUANTITYCUMUL],[AMOUNTCUMUL],[P_UOM_VALUE],[TYPE]) SELECT [AD_PINSTANCE_ID],[AD_ORG_ID],[P_YEAR_ID],[P_PAYMENT_GROUP_ID],[TAXATION_REGION_ID], [p_employer_id],[P_PERIOD_ID],[P_GAIN_ID],[VALUE],[NAME],0,0,[QUANTITYCUMUL],[AMOUNTCUMUL],[P_UOM_VALUE],[TYPE] FROM RV_P_PAYMENT_REGISTRY_GAIN_SUMMARY_VIEW WHERE AD_Pinstance_ID = " + AD_Pinstance_ID;

			if ( P_Payment_Group_ID != 0)
				Sql += " AND P_Payment_Group_ID= " + P_Payment_Group_ID;

			if ( P_Employer_ID != 0 )
				Sql += " AND P_Employer_ID= " + P_Employer_ID;

			count = DB.executeUpdate(Sql, null);

		}
	}

	private void AddInstanceParameters(int AD_Pinstance_ID)
	{
		int ClientID = this.getAD_Client_ID(); 
        int OrgID = Env.getAD_Org_ID(Env.getCtx()); 
        int UserID = this.getAD_User_ID();
        
        try
		{        
        	String SQLInsert = "INSERT INTO AD_PINSTANCE_PARA (AD_PINSTANCE_ID, SEQNO, PARAMETERNAME, P_STRING, P_STRING_TO, P_NUMBER, P_NUMBER_TO, P_DATE, P_DATE_TO, INFO, INFO_TO, AD_CLIENT_ID, AD_ORG_ID, CREATED, CREATEDBY, UPDATED, UPDATEDBY, ISACTIVE) " +
	        "SELECT " + AD_Pinstance_ID + ", " +
	        "(SELECT ISNULL(MAX(SEQNO) + 1,0) FROM AD_PINSTANCE_PARA WHERE AD_PINSTANCE_ID = " + AD_Pinstance_ID + "), " +
	        "'AD_PInstance_ID',NULL, NULL," + AD_Pinstance_ID + ", NULL,NULL, NULL, NULL, NULL, " + ClientID + ", " + OrgID + ", GETDATE(), " + UserID + ", GETDATE(), " + UserID + ", 'Y'";
        		
			PreparedStatement pstmp = null;
			
			pstmp = DB.prepareStatement(SQLInsert, null);
			pstmp.execute();
		}
		catch (Exception e)
		{
			System.err.println(_ClassErrorMessage + " [AddParameters] " + e);
		}
	}

/*	private void AddParameters(int AD_Pinstance_ID)
	{
		int ClientID = this.getAD_Client_ID(); 
        int OrgID = Env.getAD_Org_ID(Env.getCtx()); 
        int UserID = this.getAD_User_ID();
        
        try
		{       
			P_Period Period_From = P_Period.get( Env.getCtx(), P_Period_From, this.get_TrxName());
        	
        	String SQLInsert = "INSERT INTO AD_PINSTANCE_PARA (AD_PINSTANCE_ID, SEQNO, PARAMETERNAME, P_STRING, P_STRING_TO, P_NUMBER, P_NUMBER_TO, P_DATE, P_DATE_TO, INFO, INFO_TO, AD_CLIENT_ID, AD_ORG_ID, CREATED, CREATEDBY, UPDATED, UPDATEDBY, ISACTIVE) " +
	        "SELECT " + AD_Pinstance_ID + ", " +
	        "(SELECT ISNULL(MAX(SEQNO) + 1,0) FROM AD_PINSTANCE_PARA WHERE AD_PINSTANCE_ID = " + AD_Pinstance_ID + "), " +
	        "'Period_From','" + Period_From.getName() + "', NULL, NULL, NULL,NULL, NULL, NULL, NULL, " + ClientID + ", " + OrgID + ", GETDATE(), " + UserID + ", GETDATE(), " + UserID + ", 'Y'";
        		
			PreparedStatement pstmp = null;
			
			pstmp = DB.prepareStatement(SQLInsert, null);
			pstmp.execute();
		}
		catch (Exception e)
		{
			System.err.println(_ClassErrorMessage + " [AddParameters] " + e);
		}
	}
*/
	
	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	protected String doIt() throws Exception 
	{
		AD_Pinstance_ID = this.getAD_PInstance_ID();

        AddInstanceParameters(AD_Pinstance_ID);

		if ( this.P_Period_From == 0)
		{
			MPInstancePara Param = new MPInstancePara( Env.getCtx(), this.getAD_PInstance_ID(), 99);
			Param.setParameterName("Period_From");
			Param.setP_String( " " );
			Param.save();

			insertData();
		}
		else
		{
			P_Period_To = P_Period_ID;
//	        AddParameters(AD_Pinstance_ID);
			
			P_Period Period_From = P_Period.get( Env.getCtx(), P_Period_From, this.get_TrxName());
			MPInstancePara Param = new MPInstancePara( Env.getCtx(), this.getAD_PInstance_ID(), 99);
			Param.setParameterName("Period_From");
			Param.setP_String( Period_From.getName() );
			Param.save();
			
			multiPeriod();
		}
		
		
		if ((this.P_Frequency_ID > 0) && (this.P_Period_ID>0))
		{
/*			P_Payment_Registry PaymentRegistry = new P_Payment_Registry( getCtx(), -1, null);
			//PaymentRegistry.StartToFillTable(this.P_Frequency_ID,this.P_Period_ID,this.P_Payment_Group_ID,this.P_Occupation_Group_ID,this.P_Employee_ID);

	        
	        AddInstanceParameters(AD_Pinstance_ID);
	        
	        PaymentRegistry.StartToFillEmployeesTable(this.P_Period_ID,AD_Pinstance_ID);
	        
			if (this.P_Employee_ID==0)
				PaymentRegistry.StartToFillCumulativesTables(this.P_Period_ID);
*/	        

/*	        int l_Role = Env.getAD_Role_ID(Env.getCtx());
	        String repUrl = srvUrl+"Crystal/reports.jsp?AD_PInstance_ID=" + AD_Pinstance_ID + "&Format=" + m_Format + "&Ad_Role_ID=" + l_Role;

	        Env.startBrowser(repUrl);
*/
			int l_Role = Env.getAD_Role_ID(Env.getCtx());
	        ReportLauncher.callReport( AD_Pinstance_ID, l_Role, m_Format, getParameter() );

	        return "";
		}
		return "";
	}

}
