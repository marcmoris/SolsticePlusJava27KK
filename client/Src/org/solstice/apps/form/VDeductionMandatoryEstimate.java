/******************************************************************************
 * Product: Solstice+ Payroll & Human Resources Management                    *
 * Copyright (C) 2008 ProGestion Informatique, Inc. All Rights Reserved.      *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ProGestion Informatique, 210-5300 Bld des Galerie, Quebec, G2K 2A2 Canada  *
 * or via info@progestion.net or http://www.progestion.net/license.html       *
 ******************************************************************************/

package org.solstice.apps.form;

import java.awt.Cursor;
import java.awt.Frame;
import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;

import javax.swing.JFileChooser;



import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.model.GridField;
import org.compiere.model.MClient;
import org.compiere.model.MQuery;
import org.compiere.model.MTab;
import org.compiere.model.MTable;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

import solstice.model.P_Frequency;
import solstice.model.P_Period;
import solstice.process.Calcul_federal_tax;


/**
 * @author Marc Morissette
 *
 * Calcul des gains pour un interval de périodes
 */
public class VDeductionMandatoryEstimate extends CustomWindowQuery
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 *  VDeductionMandatoryEstimate Constructor
	 */
	public VDeductionMandatoryEstimate()
	{
		getConstance( "P_T_DeductionMandatoryEstimate" , 2100924);
//		dynInit();
	}   //  VDeductionMandatoryEstimate

	//


	private int m_GroupByOption = 1;

	/**
	 *  Dynamic Init.
	 *  - Load Bank Info
	 *  - Load Employee
	 *  - Init Table
	 */
	public void dynInit()
	{
		int rows = miniTable.getRowCount();
	     ColumnInfo[] colInfo = this.getColumn();
		for (int i = 0; i < rows; i++)
		{
			miniTable.removeColumn( miniTable.getColumn(colInfo[i].getColHeader()) );
		}
		
		m_sql = miniTable.prepareTable(getColumn(),
			//  0..4
	
			//	FROM
			"P_T_DeductionMandatoryEstimate "
//			+ " INNER JOIN P_Employee ON P_Employee.P_Employee_ID = P_T_DeductionMandatoryEstimate.P_Employee_ID "	
//			+ " INNER JOIN P_Employer ON P_Employer.P_Employer_ID = P_T_DeductionMandatoryEstimate.P_Employer_ID "	
				,
			
			//	WHERE 
			" 1 = 1",
         	//	additional where & order in loadTableInfo() 
			true, "P_T_DeductionMandatoryEstimate");
		//
		//

	}   //  dynInit


	public ColumnInfo[] getColumn()
	{
		Properties ctx = Env.getCtx();
		return new ColumnInfo[] 
	       {
				new ColumnInfo(Msg.translate(ctx,"P_Year_ID"),"( SELECT P_Year.year FROM P_Year where P_Year.P_Year_ID = P_T_DeductionMandatoryEstimate.P_Year_ID)", String.class ),
//				new ColumnInfo(Msg.translate(ctx,"P_Employee_ID"),"( SELECT P_Employee.value FROM P_EMPLOYEE where P_Employee.P_Employee_ID = P_T_DeductionMandatoryEstimate.P_Employee_ID)", KeyNamePair.class, "P_Employee_ID"),
				new ColumnInfo(Msg.translate(ctx,"P_Employee_ID"),"( SELECT P_Employee.value FROM P_EMPLOYEE where P_Employee.P_Employee_ID = P_T_DeductionMandatoryEstimate.P_Employee_ID)", String.class),
				new ColumnInfo(Msg.translate(ctx,"Name"),"Name", String.class, true, false, null),
				new ColumnInfo(Msg.translate(ctx,"Age"),"Age", String.class, true, false, null),
				new ColumnInfo(Msg.translate(ctx,"IsActive"),"IsActive", String.class, true, false, null),
			//	new ColumnInfo(Msg.translate(ctx,"P_Employer_ID"), "( SELECT P_Employer.value FROM P_Employer where P_Employer.P_Employer_ID = P_T_DeductionMandatoryEstimate.P_Employer_ID)", KeyNamePair.class, "P_Employer_ID"),
				new ColumnInfo(Msg.translate(ctx,"P_Employer_ID"), "( SELECT P_Employer.value FROM P_Employer where P_Employer.P_Employer_ID = P_T_DeductionMandatoryEstimate.P_Employer_ID)", String.class),
				new ColumnInfo(Msg.translate(ctx,"NbrProvince"),"NbrProvince", String.class, true, false, null),
//				new ColumnInfo(Msg.translate(ctx,"P_Payment_Group_ID"),"( SELECT P_Payment_Group.value FROM P_Payment_Group where P_Payment_Group.P_Payment_Group_ID = P_T_DeductionMandatoryEstimate.P_Payment_Group_ID)", KeyNamePair.class, "P_Payment_Group_ID"),
				new ColumnInfo(Msg.translate(ctx,"P_Payment_Group_ID"),"( SELECT P_Payment_Group.value FROM P_Payment_Group where P_Payment_Group.P_Payment_Group_ID = P_T_DeductionMandatoryEstimate.P_Payment_Group_ID)", String.class),
				new ColumnInfo(Msg.translate(ctx,"P_Distribution_Booklet_ID"),"( SELECT P_Distribution_Booklet.value + ' ' + P_Distribution_Booklet.Name FROM P_Distribution_Booklet where P_Distribution_Booklet.P_Distribution_Booklet_ID = P_T_DeductionMandatoryEstimate.P_Distribution_Booklet_ID)", String.class),
//				new ColumnInfo(Msg.translate(ctx,"P_Deduction_ID"),"( SELECT P_Deduction.value FROM P_Deduction where P_Deduction.P_Deduction_ID = P_T_DeductionMandatoryEstimate.P_Deduction_ID)", KeyNamePair.class, "P_Deduction_ID"),
				new ColumnInfo(Msg.translate(ctx,"P_Deduction_ID"),"( SELECT P_Deduction.value + ' ' + P_Deduction.Name FROM P_Deduction where P_Deduction.P_Deduction_ID = P_T_DeductionMandatoryEstimate.P_Deduction_ID)", String.class),
//				new ColumnInfo(Msg.translate(ctx,"Taxation_Region_ID"),"( SELECT C_Region.value FROM C_Region where C_Region.C_Region_ID = P_T_DeductionMandatoryEstimate.Taxation_Region_ID)", KeyNamePair.class, "Taxation_Region_ID"),
				new ColumnInfo(Msg.translate(ctx,"NbrPeriod"),"NbrPeriod", String.class, true, false, null),
				new ColumnInfo(Msg.translate(ctx,"Hours_Eligible"),"Hours_Eligible", BigDecimal.class, true, false, null),
				new ColumnInfo(Msg.translate(ctx,"Salary_Eligible"),"Salary_Eligible", BigDecimal.class, true, false, null),
				new ColumnInfo(Msg.translate(ctx,"SalaryEligibleCalc"),"SalaryEligibleCalc", BigDecimal.class, true, false, null),
				new ColumnInfo(Msg.translate(ctx,"SalaryEligibleDiff"),"SalaryEligibleDiff", BigDecimal.class, true, false, null),
				new ColumnInfo(Msg.translate(ctx,"Employee_Part"),"Employee_Part", BigDecimal.class, true, false, null),
				new ColumnInfo(Msg.translate(ctx,"EmployeeAmtCalc"),"EmployeeAmtCalc", BigDecimal.class, true, false, null),
				new ColumnInfo(Msg.translate(ctx,"EmployeeAmtDiff"),"EmployeeAmtDiff", BigDecimal.class, true, false, null),
				new ColumnInfo(Msg.translate(ctx,"Employer_Part"),"Employer_Part", BigDecimal.class, true, false, null),
				new ColumnInfo(Msg.translate(ctx,"EmployerAmtCalc"),"EmployerAmtCalc", BigDecimal.class, true, false, null),
				new ColumnInfo(Msg.translate(ctx,"EmployerAmtDiff"),"EmployerAmtDiff", BigDecimal.class, true, false, null),
				new ColumnInfo(Msg.translate(ctx,"TotalExemption"),"TotalExemption", BigDecimal.class, true, false, null),
//				new ColumnInfo(Msg.translate(ctx,"P_Period_From_ID"),"P_Period_From_ID", String.class, true, false, null),
//				new ColumnInfo(Msg.translate(ctx,"P_Period_TO_ID"),"P_Period_TO_ID", String.class, true, false, null),
//				new ColumnInfo(Msg.translate(ctx,"P_Year_ID"),"P_Year_ID", KeyNamePair.class, true, false, null),
//				new ColumnInfo(Msg.translate(ctx,"Method_Deduction"),"Method_Deduction", String.class, true, false, null),
				new ColumnInfo(Msg.translate(ctx,"AccumulationAmount"),"AccumulationAmount", BigDecimal.class, true, false, null),
				new ColumnInfo(Msg.translate(ctx,"RetrievalAmount"),"RetrievalAmount", BigDecimal.class, true, false, null)
			  };

		
	}



	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		m_disposing = true;

		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	}	//	dispose

	/*************************************************************************/

	private int m_noSelected = 0;


	/**
	 *  Calculate selected rows.
	 *  - add up selected rows
	 */
	public void calculateSelection( int firstRow, int lastRow )
	{
		int rows = miniTable.getRowCount();
		if ( firstRow != -1 )
		{
			ColumnInfo[] colInfo = this.getColumn();
			m_noSelected = 0;
			for (int i = 0; i < rows; i++)
			{
				boolean isSelected = miniTable.isRowSelected(i);
//				IDColumn id = (IDColumn) miniTable.getModel().getValueAt(i, 0);
				if (isSelected ) 
				{
			        for(int j = 0; j <= colInfo.length -1 ; j++)
			        {
			        	String colName = colInfo[j].getColSQL();
			        	if ( colInfo[j].getColClass().equals(BigDecimal.class) )
			        	{
			        		BigDecimal qty = Env.ZERO;
			        	}
			        }
					m_noSelected++;
				}
			}

		}


		
		//  Information
		StringBuffer info = new StringBuffer();
		info.append(m_noSelected).append(" ").append(Msg.getMsg(Env.getCtx(), "Selected")).append(" - ");
		statusBar.setInfo(info.toString());
		statusBar.setStatusLine( m_infoQuery );
        getStatusBar().setStatusDB(  m_noSelected + "/" + rows, null);
		//
	}   //  calculateSelection

	/**
	 *  Query and create TableInfo
	 */
	public void loadTableInfo()
	{
		//  Get
		String sql =  m_sql;

		if ( query != null && query.getWhereClause() != null && query.getWhereClause().length() != 0 )
		{
			sql += " and " + query.getWhereClause();
		}

		if ( sql == null)
			return;

		
		try
		{
			
		    PreparedStatement pstmt = DB.prepareStatement(sql, null);
			//
			setBusy( true );
			ResultSet rs = pstmt.executeQuery();
			miniTable.loadTable(rs);
			setBusy( false );
			rs.close();
			pstmt.close();
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"loadTableInfo", e);
		}
		calculateSelection( -1, -1);
	}   //  loadTableInfo


	public void cmd_refresh()
	{
		setBusy( true );
		loadTableInfo();
		setBusy( false );
	}

	private Hashtable<String, BigDecimal> 	taxTable = new Hashtable<String, BigDecimal>();

	private void load_tax_parameter( Timestamp CurrentDate ) throws Exception
	{
		taxTable.clear();

		int taxID = Calcul_federal_tax.get_federal_tax_id( CurrentDate );
		taxTable = Calcul_federal_tax.get_federal_tax_para(  taxID, CurrentDate);

	}

    /**
     *	Find - Set Query
     */
    public void cmd_find()
    {
        String trxName = null;
        
        MTable table = new MTable(Env.getCtx(), mAD_Table_ID, trxName);
        MTab   tab  = new MTab(Env.getCtx(), mAD_Tab_ID, trxName);
        
        GridField[] findFields = GridField.createFields(Env.getCtx(), 0, 0, mAD_Tab_ID);

        CustomWindowFind find = new CustomWindowFind (Env.getFrame(this),  mAD_Window_ID, getTitle( this.getClass().getName() ),
        		tab.getAD_Tab_ID(), mAD_Table_ID, "P_T_DeductionMandatoryEstimate", tab.getWhereClause(), findFields, 1, CustomWindowFind.QUERYTYPE_VDeductionMandatoryEstimate );


 
        query = find.getQuery();
		m_infoQuery = find.getInfoQuery();

		//+2012.12.16
		if ( find != null && find.getEmployeeStatus() != null &&  find.getEmployeeStatus().equals("A")  )
		{
			query.addRestriction(" ( P_Employee_ID IN ( Select P_Employee.P_Employee_ID from P_Employee WHERE P_Employee.isActive = 'Y' )) " );
			m_infoQuery += " - Employés actifs ";
		}

		if ( find != null && find.getEmployeeStatus() != null &&  find.getEmployeeStatus().equals("I")  )
		{
			query.addRestriction(" ( P_Employee_ID IN ( Select P_Employee.P_Employee_ID from P_Employee WHERE P_Employee.isActive = 'N' )) " );
			m_infoQuery += " - Employés inactifs ";
		}
		
		//-2012.12.16
		
		System.out.println("query = " + query);

    	P_Period Period = P_Period.get(Env.getCtx(), find.getP_Period_To_ID(), trxName);

        if(query != null)
        {
        	if ( find.isRefreshData() )
        	{
    		    try
    		    {

        		String sql = "DELETE FROM DBO.P_T_DeductionMandatoryEstimate";
            	DB.executeUpdate(sql, null);
            	
            	
            	
            	sql = " INSERT INTO DBO.P_T_DeductionMandatoryEstimate ("
            	+ "			[AD_Client_ID] "                                           
            	+ "		   ,[AD_Org_ID] "
            	+ "		   ,[P_Employee_ID] "
            	+ "		   ,[Name] "
            	+ "		   ,[Age] "
            	+ "		   ,[IsActive] "
            	+ "		   ,[P_Employer_ID] "
            	+ "		   ,[P_Payment_Group_ID] "
            	+ "		   ,[P_Deduction_ID] "
            	+ "		   ,[Taxation_Region_ID] "
            	+ "		   ,[NbrPeriod] "
            	+ "		   ,[Hours_Eligible] "
            	+ "		   ,[Salary_Eligible] "
            	+ "		   ,[Employee_Part] "
            	+ "		   ,[Employer_Part] "
            	+ "		   ,[AccumulationAmount] "
            	+ "		   ,[RetrievalAmount] "
            	+ "		   ,[TotalExemption] "
            	+ "		   ,[EmployeeAmtCalc] "
            	+ "		   ,[EmployeeAmtDiff] "
            	+ "		   ,[EmployerAmtCalc] "
            	+ "		   ,[EmployerAmtDiff] "
            	+ "		   ,P_Period_From_ID "
            	+ "		   ,P_Period_TO_ID "
            	+ "		   ,P_Year_ID "
            	+ "		   ,P_FREQUENCY_ID "
            	+ "		   ,P_Distribution_Booklet_ID "
            	+ "		   ,Method_Deduction) "
            	+ "Select "
            	+ "   P_Payment.AD_Client_ID  , "
            	+ "   max(P_Employee.AD_Org_ID   )  , "
            	+ "   P_Employee.P_Employee_ID , "
            	+ "   max(P_Employee.Name ) as name			 , "
            	+ "   dbo.getAge( max(birthdate), getdate() ) as AGE  , "
            	+ "   max( P_Employee.IsActive ) as isActive     , "
            	+ "   case when P_Method_Deduction.Value in ('004' ) then P_Payment.P_EMPLOYER_ID else null end , "
            	+ "   max(P_Payment.P_Payment_Group_ID ), "
            	+ "   P_Payment_Deduction.P_Deduction_ID , "
            	+ "   P_Payment.Taxation_Region_ID , "
            	+ "   0 as nbrPeriod	  , "
            	+ "   isnull(sum(P_Payment_Deduction.Hours_Eligible), 0)  as Hours_Eligible  , "
            	+ "   isnull(sum(P_Payment_Deduction.Salary_Eligible), 0) as Salary_Eligible , "
            	+ "   isnull(sum(P_Payment_Deduction.Employee_Part), 0)   as Employee_Part , "
            	+ "   isnull(sum(P_Payment_Deduction.Employer_Part), 0)   as Employer_Part   , "
            	+ "   isnull(sum(P_Payment_Deduction.AccumulationAmount), 0) as AccumulationAmount , "
            	+ "   isnull(sum(P_Payment_Deduction.RetrievalAmount), 0) as RetrievalAmount , "
            	+ "   0 as TotalExemption  , "
            	+ "   0 as EmployeeAmtCalc , "
            	+ "   0 as EmployeeAmtDiff , "
            	+ "   0 as EmployerAmtCalc , "
            	+ "   0 as EmployerAmtDiff , "
            	+ "   max(PeriodFrom.P_Period_ID), "
            	+ "   max(PeriodTo.P_Period_ID), "
            	+ "   MAX(PeriodFrom.P_Year_ID ), "
            	+ "   P_Payment.P_FREQUENCY_ID, "
            	+ "   max(P_Employee.P_Distribution_Booklet_ID), "
            	+ "   MAX( P_Method_Deduction.Value )"
            	+ "FROM P_Employee "
            	+ "  inner join P_Payment on P_Employee.P_Employee_ID = P_Payment.P_Employee_ID "
            	+ "  left outer join C_Region on C_Region.C_Region_ID = P_Payment.Taxation_Region_ID "
            	+ "  inner join P_Employer on P_Employer.P_Employer_ID = P_Payment.P_Employer_ID "
            	+ "  inner join P_Payment_Deduction on P_Payment.P_Payment_ID = P_Payment_Deduction.P_Payment_ID "
            	+ "  inner join P_Deduction on P_Deduction.P_Deduction_ID = P_Payment_Deduction.P_Deduction_ID "
            	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
            	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
            	+ "  inner join P_Period on P_Payment.P_Period_ID = P_Period.P_Period_ID "
            	+ "  inner join P_Distribution_Booklet on P_Employee.P_Distribution_Booklet_ID = P_Distribution_Booklet.P_Distribution_Booklet_ID "
            	+ "  left outer join C_Activity on P_Payment.C_Activity_ID = C_Activity.C_Activity_ID "
            	+ "  left join P_Department on P_Payment.P_Department_ID = P_Department.P_Department_ID " 
            	+ "  inner join P_Payment_Group on P_Payment.P_Payment_Group_ID = P_Payment_Group.P_Payment_Group_ID "
            	+ "  inner join P_Period PeriodFrom on PeriodFrom.P_period_ID = " + find.getP_Period_From_ID() + " and P_Payment.P_FREQUENCY_ID = PeriodFrom.P_FREQUENCY_ID "
            	+ "  inner join P_Period PeriodTo On PeriodTo.P_Period_ID = " + find.getP_Period_To_ID() + " and P_Payment.P_FREQUENCY_ID = PeriodTo.P_FREQUENCY_ID "
            	+ "  where P_DEDUCTION_PARAM.P_DEDUCTION_PARAM_ID in ( select MAX( P_DEDUCTION_PARAM_ID ) From P_DEDUCTION_PARAM M where M.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID ) "
            	+ "	and p_payment.p_year_id = " + Period.getP_Year_ID()  
            	+ "	and P_Method_Deduction.Value in ('002','003','004','031', '92', '93' ) "
            	+ "group by P_Payment.AD_Client_ID, "
            	+ "		 P_Payment.Taxation_Region_ID, "
            	+ "		 P_Employee.P_Employee_ID, "
            	+ "		 P_Payment_Deduction.P_Deduction_ID, "
            	+ "		 case when P_Method_Deduction.Value in ('004' ) then P_Payment.P_EMPLOYER_ID else null end , "
            	+ "		 P_Period.P_Year_ID, "
            	+ "		 P_Payment.P_FREQUENCY_ID "
            	+ "" ;
            	 
            	DB.executeUpdate(sql, null);
 	    		DB.commit(true, null);
            	
                // calcul les salaire éligible et les parts employé et employeur théorique
             	//TotalExemption  = dbo.fn_GetTotalDeductionCal( P_Deduction_ID, Salary_Eligible, Employee_Part, P_Employee_ID, P_Employer_ID , P_FREQUENCY.NumberOfPeriod, nbrPeriod, P_Tax_ID ,1, Taxation_Region_ID),
             	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
             	   + " set SalaryEligibleCalc = [dbo].[fn_getsalaire_eligible_gouv]( P_Employee_ID, P_Deduction_ID, P_Employer_ID, P_Year_ID, P_Period_From_ID , P_Period_TO_ID  , Taxation_Region_ID )"
             	   + " From P_FREQUENCY, P_Tax "
             	   + " Where P_FREQUENCY.P_FREQUENCY_ID = P_T_DeductionMandatoryEstimate.P_FREQUENCY_ID"
             	   + " AND P_Tax.P_Tax_ID in ( select MAX( P_Tax_ID) from P_Tax Where datepart( year, P_TAX.EFFECTIN ) in ( select YEAR from P_YEAR where P_Year.P_YEAR_ID = P_T_DeductionMandatoryEstimate.P_Year_ID )) "
             	   ;
             	DB.executeUpdate(sql, null);
 	    		DB.commit(true, null);

            	
            	// m.a.j du nombre de période.	 
            	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
            	    + " set nbrPeriod = dbo.fn_GetNbrPeriod( P_Employee_ID, P_Deduction_ID, P_Employer_ID, P_Year_ID, P_Period_From_ID , P_Period_TO_ID, Taxation_Region_ID ) ";
            	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);

            	

   		    	load_tax_parameter( Period.getPayDate() );

    		    // RRQ
    			BigDecimal rpc_max_gain  = (BigDecimal)taxTable.get( "RRQ.2");
               	BigDecimal rpc_rate      = (BigDecimal)taxTable.get( "RRQ.1");
               	BigDecimal rpc_exemption = (BigDecimal)taxTable.get( "RRQ.3");
               	BigDecimal rpc_max       = (BigDecimal)taxTable.get( "RRQ.4");

    			BigDecimal rpc2_max_gain  = (BigDecimal)taxTable.get( "RRQ2.3");
               	BigDecimal rpc2_rate      = (BigDecimal)taxTable.get( "RRQ2.1");
//               	BigDecimal rpc2_exemption = (BigDecimal)taxTable.get( "RRQ2.3");
               	BigDecimal rpc2_max       = (BigDecimal)taxTable.get( "RRQ2.4");
               	BigDecimal rpc2_max2       = (BigDecimal)taxTable.get( "RRQ2.6");

               	
               	
               	P_Frequency frequency = P_Frequency.get(Env.getCtx(), Period.getP_Frequency_ID(), trxName);


               	rpc_rate = rpc_rate.divide( new BigDecimal( 100 ), 6, BigDecimal.ROUND_HALF_UP );
               	rpc2_rate = rpc2_rate.divide( new BigDecimal( 100 ), 6, BigDecimal.ROUND_HALF_UP );

            	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set Salary_Eligible = " + rpc_max_gain
                		+ " , NbrPeriod = " + frequency.getNumberOfPeriod()
                		+ " where Salary_Eligible >= " + rpc_max_gain
                		+ " and P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ "	and P_Method_Deduction.Value in ('002' ) )"
                		;
               	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);

               	

            	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set SalaryEligibleCalc = " + rpc_max_gain
                		+ " where SalaryEligibleCalc >= " + rpc_max_gain
                		+ " and P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ "	and P_Method_Deduction.Value in ('002' ) )"
                		;
               	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);


               	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set TotalExemption = dbo.divide( " + rpc_exemption + " , " + frequency.getNumberOfPeriod() + " )  * nbrPeriod " 
                		+ " where P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ "	and P_Method_Deduction.Value in ('002' ) )"
                		;
            	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);

               	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set EmployeeAmtCalc = round( (SalaryEligibleCalc - TotalExemption ) * " + rpc_rate + ", 2)" 
                		+ " where P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ "	and P_Method_Deduction.Value in ('002' ) )"
                		;
            	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);

            	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set EmployeeAmtCalc = " + rpc_max 
                		+ " where P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ " and EmployeeAmtCalc > " + rpc_max
                    	+ "	and P_Method_Deduction.Value in ('002' ) )"
                		;
            	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);

            	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set SalaryEligibleCalc = case when SalaryEligibleCalc >= " + rpc2_max2 + " THEN SalaryEligibleCalc - " + rpc2_max2 + " ELSE 0 END"
                		+ " where SalaryEligibleCalc >= " + rpc2_max2
                		+ " and P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ "	and P_Method_Deduction.Value in ('92' ) )"
                		;
               	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);
	    		
	    		
            	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set SalaryEligibleCalc = " + rpc2_max_gain
                		+ " , NbrPeriod = " + frequency.getNumberOfPeriod()
                		+ " where SalaryEligibleCalc >= " + rpc2_max_gain
                		+ " and P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ "	and P_Method_Deduction.Value in ('92' ) )"
                		;
               	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);
	    		
	    		//RRQ2
               	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set EmployeeAmtCalc = round( (SalaryEligibleCalc  ) * " + rpc2_rate + ", 2)" 
                		+ " where P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ "	and P_Method_Deduction.Value in ('92' ) )"
                		;
            	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);

 	    		//RRQ3
            	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set EmployeeAmtCalc = " + rpc2_max 
                		+ " where P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ " and EmployeeAmtCalc > " + rpc2_max
                    	+ "	and P_Method_Deduction.Value in ('92' ) )"
                		;
            	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);

              	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set EmployerAmtCalc = round( (SalaryEligibleCalc ) * " + rpc2_rate + ", 2)" 
                		+ " where P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ "	and P_Method_Deduction.Value in ('92' ) )"
                		;
            	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);	    		
	    		
            	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set EmployerAmtCalc = " + rpc2_max 
                		+ " where P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ " and EmployerAmtCalc > " + rpc2_max
                    	+ "	and P_Method_Deduction.Value in ('92' ) )"
                		;
            	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);
	    		
/*	    		
               	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set SalaryEligibleCalc = SalaryEligibleCalc - " + rpc2_max2
                		+ " where P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ "	and P_Method_Deduction.Value in ('92' ) )"
                		;
            	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);
	    		
	    		

               	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set SalaryEligibleCalc = " + rpc2_exemption
                		+ " where SalaryEligibleCalc >= " + rpc2_exemption
                		+ " and P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ "	and P_Method_Deduction.Value in ('92' ) )"
                		;
               	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);
*/

	    		
            	
               	//RPC
    			rpc_rate      = (BigDecimal)taxTable.get( "RPC.1");
    			rpc_max_gain  = (BigDecimal)taxTable.get( "RPC.2");
    			rpc_exemption = (BigDecimal)taxTable.get( "RPC.3");
    			rpc_max       = (BigDecimal)taxTable.get( "RPC.4");
//    			rpc_rate = rpc_rate.divide( new BigDecimal( 100 ), 6, BigDecimal.ROUND_HALF_UP );

    			rpc2_max_gain  = (BigDecimal)taxTable.get( "RPC2.3");
               	rpc2_rate      = (BigDecimal)taxTable.get( "RPC2.1");
//               	rpc2_exemption = (BigDecimal)taxTable.get( "RPC2.3");
               	rpc2_max       = (BigDecimal)taxTable.get( "RPC2.4");
               	rpc2_max2       = (BigDecimal)taxTable.get( "RPC2.6");

               	rpc_rate = rpc_rate.divide( new BigDecimal( 100 ), 6, BigDecimal.ROUND_HALF_UP );
               	rpc2_rate = rpc2_rate.divide( new BigDecimal( 100 ), 6, BigDecimal.ROUND_HALF_UP );

               	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set TotalExemption = dbo.divide( " + rpc_exemption + " , " + frequency.getNumberOfPeriod() + " )  * nbrPeriod " 
                		+ " where P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ "	and P_Method_Deduction.Value in ('002', '003' ) )"
                		;
            	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);

               	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                    		+ "	set Salary_EligibleCalc = " + rpc_max_gain
                    		+ " , NbrPeriod = " + frequency.getNumberOfPeriod()
                    		+ " where Salary_EligibleCalc >= " + rpc_max_gain
                    		+ " and P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
                        	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                        	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                        	+ "	and P_Method_Deduction.Value in ('003' ) )"
                    		;
               	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);

               	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set SalaryEligibleCalc = " + rpc_max_gain
                		+ " where SalaryEligibleCalc >= " + rpc_max_gain
                		+ " and P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ "	and P_Method_Deduction.Value in ('003' ) )"
                		;
               	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);

               	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set EmployeeAmtCalc = round( (SalaryEligibleCalc - TotalExemption ) * " + rpc_rate + ", 2)" 
                		+ " where P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ "	and P_Method_Deduction.Value in ('003' ) )"
                		;
            	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);


               	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set EmployeeAmtCalc = " + rpc_max 
                		+ " where P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ " and EmployeeAmtCalc > " + rpc_max
                    	+ "	and P_Method_Deduction.Value in ('003' ) )"
                		;
            	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);

            	
	           	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
	            		+ "	set EmployerAmtCalc = EmployeeAmtCalc" 
	            		+ " where P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
	                	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
	                	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
	                	+ "	and P_Method_Deduction.Value in ('002', '003' ) )"
	            		;
	        	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);
	    		
            	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set SalaryEligibleCalc = case when SalaryEligibleCalc >= " + rpc2_max2 + " THEN SalaryEligibleCalc - " + rpc2_max2 + " ELSE 0 END"
                		+ " where SalaryEligibleCalc >= " + rpc2_max2
                		+ " and P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ "	and P_Method_Deduction.Value in ('93' ) )"
                		;
               	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);
	    		
            	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set SalaryEligibleCalc = " + rpc2_max_gain
                		+ " , NbrPeriod = " + frequency.getNumberOfPeriod()
                		+ " where SalaryEligibleCalc >= " + rpc_max_gain
                		+ " and P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ "	and P_Method_Deduction.Value in ('93' ) )"
                		;
               	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);
               	
	    		
               	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set EmployeeAmtCalc = round( (SalaryEligibleCalc - TotalExemption ) * " + rpc2_rate + ", 2)" 
                		+ " where P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ "	and P_Method_Deduction.Value in ('93' ) )"
                		;
            	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);
	    		
	    		
	    		//RRQ3
            	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set EmployeeAmtCalc = " + rpc2_max 
                		+ " where P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ " and EmployeeAmtCalc > " + rpc2_max
                    	+ "	and P_Method_Deduction.Value in ('93' ) )"
                		;
            	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);
	    		
               	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set EmployerAmtCalc = round( (SalaryEligibleCalc - TotalExemption ) * " + rpc2_rate + ", 2)" 
                		+ " where P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ "	and P_Method_Deduction.Value in ('93' ) )"
                		;
            	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);
	    		
	    		
	    		//RRQ3
            	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set EmployerAmtCalc = " + rpc2_max 
                		+ " where P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ " and EmployerAmtCalc > " + rpc2_max
                    	+ "	and P_Method_Deduction.Value in ('93' ) )"
                		;
            	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);
	    		
/*	    		
               	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set SalaryEligibleCalc = SalaryEligibleCalc - " + rpc2_max2
                		+ " where P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ "	and P_Method_Deduction.Value in ('93' ) )"
                		;
            	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);
	    		
	    		

               	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set SalaryEligibleCalc = " + rpc2_exemption
                		+ " where SalaryEligibleCalc >= " + rpc2_exemption
                		+ " and P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ "	and P_Method_Deduction.Value in ('93' ) )"
                		;
               	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);
*/
	    		

               	// AE
        		BigDecimal ei_max_gain      = (BigDecimal)taxTable.get( "AE.1");

               	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set Salary_Eligible = " + ei_max_gain
                		+ " where Salary_Eligible > " + ei_max_gain
                		+ " and P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ "	and P_Method_Deduction.Value in ('004' ) )"
                		;
            	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);

            	
               	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set SalaryEligibleCalc = " + ei_max_gain
                		+ " where SalaryEligibleCalc > " + ei_max_gain
                		+ " and P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ "	and P_Method_Deduction.Value in ('004' ) )"
                		;
            	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);

    			BigDecimal ei_rate_employee = (BigDecimal)taxTable.get( "AE.2");
    			ei_rate_employee = ei_rate_employee.divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP );

               	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set EmployeeAmtCalc = round( SalaryEligibleCalc * " + ei_rate_employee + ", 2)" 
                		+ " where P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ "	and P_Method_Deduction.Value in ('004' ) )" 
                    	+ " and Taxation_Region_ID = 166 "
                		;
            	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);

            	
            	ei_rate_employee = (BigDecimal)taxTable.get( "AE.5");
    			ei_rate_employee = ei_rate_employee.divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP );
    			
               	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set EmployeeAmtCalc = round( SalaryEligibleCalc * " + ei_rate_employee + ", 2)" 
                		+ " where P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ "	and P_Method_Deduction.Value in ('004' ) )" 
                    	+ " and Taxation_Region_ID != 166 "
                		;
            	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);

            	
    			BigDecimal ei_rate_employer = (BigDecimal)taxTable.get( "AE.41");

               	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set EmployerAmtCalc = round( EmployeeAmtCalc * " + ei_rate_employer + ", 2)" 
                		+ " where P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ "  inner join P_Employer on P_Employer.P_Employer_ID = P_T_DeductionMandatoryEstimate.P_Employer_ID and P_Employer.isReducedEI = 'N'"
                    	+ "	and P_Method_Deduction.Value in ('004' ) )"
                    	+ " "
                		;
            	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);

    			ei_rate_employer = (BigDecimal)taxTable.get( "AE.42");

               	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set EmployerAmtCalc = round( EmployeeAmtCalc * " + ei_rate_employer + ", 2)" 
                		+ " where P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ "  inner join P_Employer on P_Employer.P_Employer_ID = P_T_DeductionMandatoryEstimate.P_Employer_ID and P_Employer.isReducedEI = 'Y'"
                    	+ "	and P_Method_Deduction.Value in ('004' ) )"
                    	+ " "
                		;
            	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);

	    		
	    		// acasta.
    			ei_rate_employer = (BigDecimal)taxTable.get( "AE.43");

               	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set EmployerAmtCalc = round( EmployeeAmtCalc * " + ei_rate_employer + ", 2)" 
                		+ " where P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ "  inner join P_Employer on P_Employer.P_Employer_ID = P_T_DeductionMandatoryEstimate.P_Employer_ID and P_Employer.isReducedEI = 'Y'"
                    	+ "	and P_Method_Deduction.Value in ('004' ) )" 
                    	+ " and AD_Org_ID IN ( 1000011, 1000012 )"
                    	+ " "
                		;
            	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);

            	
/*        			BigDecimal ei_max_annual = (BigDecimal)taxTable.get( "AE.3");
                   	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                    		+ "	set SalaryEligibleCalc = " + ei_max_annual
                    		+ " where SalaryEligibleCalc >= " + ei_max_annual
                    		+ " and P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
                        	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                        	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                        	+ "	and P_Method_Deduction.Value in ('004' ) )"
                        	+ " and Taxation_Region_ID = 166 "
                    		;
                   	DB.executeUpdate(sql, null);

        			ei_max_annual = (BigDecimal)taxTable.get( "AE.6");
                   	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                    		+ "	set SalaryEligibleCalc = " + ei_max_annual
                    		+ " where SalaryEligibleCalc >= " + ei_max_annual
                    		+ " and P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
                        	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                        	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                        	+ "	and P_Method_Deduction.Value in ('004' ) )"
                        	+ " and Taxation_Region_ID != 166 "
                    		;
                   	DB.executeUpdate(sql, null);
*/
                   	

            	
               	// RQAP
        		BigDecimal 		MaxRevenusAss      = (BigDecimal)taxTable.get( "RQAP.1");

               	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set Salary_Eligible = " + MaxRevenusAss
                		+ " where Salary_Eligible > " + MaxRevenusAss
                		+ " and P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ "	and P_Method_Deduction.Value in ('031' ) )"
                		;
            	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);

            	
               	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set SalaryEligibleCalc = " + MaxRevenusAss
                		+ " where SalaryEligibleCalc > " + MaxRevenusAss
                		+ " and P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ "	and P_Method_Deduction.Value in ('031' ) )"
                		;
            	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);

        		BigDecimal rqap_rate_employee = ((BigDecimal)taxTable.get( "RQAP.2")).divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP);
        		BigDecimal rqap_rate_employer = ((BigDecimal)taxTable.get( "RQAP.4")).divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP);

               	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set EmployeeAmtCalc = round( SalaryEligibleCalc * " + rqap_rate_employee + ", 2)" 
                		+ " where P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction  "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ "	and P_Method_Deduction.Value in ('031' ) )"
                		;
            	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);

               	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
                		+ "	set EmployerAmtCalc = round( SalaryEligibleCalc * " + rqap_rate_employer + ", 2)" 
                		+ " where P_Deduction_ID in ( Select P_Deduction.P_Deduction_ID From P_Deduction "
                    	+ "  inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                    	+ "  inner join P_Method_Deduction on P_Method_Deduction.P_METHOD_DEDUCTION_ID = P_DEDUCTION_PARAM.P_Method_Deduction_ID "
                    	+ "	and P_Method_Deduction.Value in ('031' ) )"
                		;
            	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);

            	
            	/*
            	//
            	sql = "Update DBO.P_T_DeductionMandatoryEstimate " 
            		+ " set EmployeeAmtCalc = dbo.fn_GetTotalDeductionCal( P_Deduction_ID, SalaryEligibleCalc, Employee_Part, P_Employee_ID, P_Employer_ID , P_FREQUENCY.NumberOfPeriod, nbrPeriod, P_Tax_ID ,2, Taxation_Region_ID), "
            		+ " EmployerAmtCalc = dbo.fn_GetTotalDeductionCal( P_Deduction_ID, SalaryEligibleCalc, Employee_Part, P_Employee_ID, P_Employer_ID , P_FREQUENCY.NumberOfPeriod, nbrPeriod, P_Tax_ID ,4, Taxation_Region_ID) "
            		+ " From P_FREQUENCY, P_Tax "
            	    + " Where P_FREQUENCY.P_FREQUENCY_ID = P_T_DeductionMandatoryEstimate.P_FREQUENCY_ID "
            		+ " AND P_Tax.P_Tax_ID in ( select MAX( P_Tax_ID) from P_Tax Where datepart( year, P_TAX.EFFECTIN ) in ( select YEAR from P_YEAR where P_Year.P_YEAR_ID = P_T_DeductionMandatoryEstimate.P_Year_ID )) "
            	    ;
            	DB.executeUpdate(sql, null);
*/

            	sql = "Update DBO.P_T_DeductionMandatoryEstimate " 
            		+ "set NbrProvince = dbo.fn_GetNbrProvince( P_Employee_ID, P_Deduction_ID, P_Employer_ID, P_Year_ID, P_Period_From_ID , P_Period_TO_ID ) "
            	  ;
              	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);

 /*             	sql = "Update DBO.P_T_DeductionMandatoryEstimate  "
              		+ " set SalaryEligibleCalc  = Salary_Eligible "
              		+ " where NbrProvince <> 1 ";

               	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);
*/


                // Calcul les montants de différences 
            	sql = " Update DBO.P_T_DeductionMandatoryEstimate " 
            	    + " set  EmployeeAmtDiff =  Employee_Part - EmployeeAmtCalc, "
            	    + " 	 EmployerAmtDiff =  Employer_Part - EmployerAmtCalc, "
            	    + "      SalaryEligibleDiff = Salary_Eligible - SalaryEligibleCalc " ;
               	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);
                         		
                         		
                     
            	sql = " delete from DBO.P_T_DeductionMandatoryEstimate "  
            			+ " where [Hours_Eligible] = 0 "
            			+ " and [Salary_Eligible] = 0 "
            			+ " and [Employee_Part] = 0 "
            			+ " and [Employer_Part] = 0 "
                ;

            	
               	DB.executeUpdate(sql, null);
	    		DB.commit(true, null);
               	
           		
    		    }
    		    catch (Exception e)
    		    {
    		        s_log.log(Level.SEVERE, "export", e);
    		    }
        	}

        	loadTableInfo();        	
//            headerPanel.resetDefaultPeriod();
        }
        
        super.cmd_find();
        find = null;

    } //	cmd_find

    /**
     * Zoom Across Menu
     */
    public void cmd_zoomAcross()
    {
    	int record_ID = 1000000; 
		if (record_ID <= 0)	return;

		//	Query
		MQuery query = new MQuery();
		//	Link for detail records
		String link = "P_Employee_ID"; //  m_curTab.getLinkColumnName();
		if (link.length() != 0)	{
			if (link.endsWith("_ID"))
				query.addRestriction(link, MQuery.EQUAL, new Integer(record_ID));
			else
				query.addRestriction(link, MQuery.EQUAL,record_ID);
		}
		query.setRecordCount(1);

		doZoomAcross("P_Employee", query);
    } //	cmd_zoom

	/**
	 * Cette méthode sert à exporter le contenue du grid dans un fichier Excel
	 */

	public void cmd_export() 
	{
		String sql = m_sql;

		if (query != null && query.getWhereClause() != null
				&& query.getWhereClause().length() != 0) {
			sql += " and " + query.getWhereClause();
		}

		if (sql == null)
			return;

		try {
			PreparedStatement stmt = DB.prepareStatement(sql, null);
			ResultSet rs = stmt.executeQuery();
			try {
				org.solstice.util.ExcelExportUtil.exportResultSet(rs, this.getColumn(), "Deduction_Obligatoire_Estimation", this);
			} finally {
				rs.close();
				stmt.close();
			}
		} catch (Exception e) {
			s_log.log(Level.SEVERE, "export", e);
		}
	}
}