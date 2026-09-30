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
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;

import javax.swing.JFileChooser;



import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.model.GridField;
import org.compiere.model.MTab;
import org.compiere.model.MTable;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import solstice.model.P_LongTermLeave;
import solstice.model.P_Period;
import solstice.utils.PgiUtil;


/**
 * @author Marc Morissette
 *
 * Calcul des Deductions pour un interval de périodes
 */
public class VDeductionDetail extends CustomWindowQuery
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	/**
	 *  VDeductionTotal Constructor
	 */
	public VDeductionDetail()
	{
		getConstance( "P_Payment_Deduction" , 2000042);
//		dynInit();
	}   //  VDeductionDetail

	//


	/** Dispose active                                  */
	private boolean         m_disposing = false;

	private int m_GroupByOption = 1;
	private String m_GroupByQuery ;



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
			"RV_P_Payment_Deduction " ,
			
			//	WHERE 
			" 1 = 1",
         	//	additional where & order in loadTableInfo() 
			true, "RV_P_Payment_Deduction");
		//
		//
	}   //  dynInit


	public ColumnInfo[] getColumn()
	{
		Properties ctx = Env.getCtx();
        String client = PgiUtil.getSolsticeParameter(Env.getCtx(),"Client");
        if ( client.equals("CHL") )
        {
    		return new ColumnInfo[] 
   		    {
    				//  0..4
//					new ColumnInfo(" ", "1", IDColumn.class, false, false, null),
					new ColumnInfo(Msg.translate(ctx, "AD_Org_ID"), "Org", String.class, true, false, null),
					new ColumnInfo(Msg.translate(ctx, "C_Activity_ID"),"C_Activity", String.class, true, false, null),   
					new ColumnInfo(Msg.translate(ctx, "P_Department_ID"),"P_Department", String.class, true, false, null),   
					new ColumnInfo(Msg.translate(ctx, "P_Workplace_ID"),"P_Workplace", String.class, true, false, null),
					new ColumnInfo(Msg.translate(ctx, "P_Payment_Group_ID"),"P_Payment_Group", String.class, true, false, null),   
					new ColumnInfo(Msg.translate(ctx, "P_Employee_ID"), "Employee", String.class, true, false, null),
					new ColumnInfo(Msg.translate(ctx, "Name"), "EmployeeName", String.class, true, false, null),
					new ColumnInfo(Msg.translate(ctx, "P_Year_ID"), "Year", String.class),
					new ColumnInfo(Msg.translate(ctx, "P_Period_ID") + " Paiement ", "PaymentPeriod", String.class),
					new ColumnInfo(Msg.translate(ctx, "P_Period_ID"), "Period", String.class),
					new ColumnInfo(Msg.translate(ctx, "P_Deduction_ID"), "Deduction", String.class, true, false, null),
					new ColumnInfo(Msg.translate(ctx, "Salary_Eligible"), "Salary_Eligible", BigDecimal.class),
					new ColumnInfo(Msg.translate(ctx, "Hours_Eligible"), "Hours_Eligible", BigDecimal.class),
					new ColumnInfo(Msg.translate(ctx, "Employee_Part"), "Employee_Part", BigDecimal.class),
					new ColumnInfo(Msg.translate(ctx, "Employer_Part"), "Employer_Part", BigDecimal.class),
					new ColumnInfo(Msg.translate(ctx, "AccumulationAmount"), "AccumulationAmount", BigDecimal.class),
					new ColumnInfo(Msg.translate(ctx, "RetrievalAmount"), "RetrievalAmount", BigDecimal.class),
					new ColumnInfo(Msg.translate(ctx, "P_Payment_ID"), "Payment", String.class),
   		    };
        	
        }
        else
        {
    		return new ColumnInfo[] 
  		    {
    				//  0..4
//					new ColumnInfo(" ", "1", IDColumn.class, false, false, null),
					new ColumnInfo(Msg.translate(ctx, "AD_Org_ID"), "Org", String.class, true, false, null),
					new ColumnInfo(Msg.translate(ctx, "P_Payment_Group_ID"),"P_Payment_Group", String.class, true, false, null),   
					new ColumnInfo(Msg.translate(ctx, "P_Employee_ID"), "Employee", String.class, true, false, null),
					new ColumnInfo(Msg.translate(ctx, "Name"), "EmployeeName", String.class, true, false, null),
					new ColumnInfo(Msg.translate(ctx, "IsActive"), "IsActive", Boolean.class),
					new ColumnInfo(Msg.translate(ctx, "P_Year_ID"), "Year", String.class),
					new ColumnInfo(Msg.translate(ctx, "P_Period_ID") + " Paiement ", "PaymentPeriod", String.class),
					new ColumnInfo(Msg.translate(ctx, "P_Period_ID"), "Period", String.class),
					new ColumnInfo(Msg.translate(ctx, "P_Deduction_ID"), "Deduction", String.class, true, false, null),
					new ColumnInfo(Msg.translate(ctx, "Salary_Eligible"), "Salary_Eligible", BigDecimal.class),
					new ColumnInfo(Msg.translate(ctx, "Hours_Eligible"), "Hours_Eligible", BigDecimal.class),
					new ColumnInfo(Msg.translate(ctx, "Employee_Part"), "Employee_Part", BigDecimal.class),
					new ColumnInfo(Msg.translate(ctx, "Employer_Part"), "Employer_Part", BigDecimal.class),
					new ColumnInfo(Msg.translate(ctx, "AccumulationAmount"), "AccumulationAmount", BigDecimal.class),
					new ColumnInfo(Msg.translate(ctx, "RetrievalAmount"), "RetrievalAmount", BigDecimal.class),
					new ColumnInfo(Msg.translate(ctx, "P_Payment_ID"), "Payment", String.class),
   		    };
        	
        }
        	

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


	/**
	 *  Calculate selected rows.
	 *  - add up selected rows
	 */

	private BigDecimal Salary_Eligible = new BigDecimal(0.0);
	private BigDecimal Hours_Eligible  = new BigDecimal(0.0);
	private BigDecimal Employee_Part = new BigDecimal(0.0);
	private BigDecimal Employer_Part = new BigDecimal(0.0);
	private BigDecimal AccumulationAmount = new BigDecimal(0.0);
	private BigDecimal RetrievalAmount = new BigDecimal(0.0);
	private int m_noSelected = 0;

	public void calculateSelection(int firstRow, int lastRow )
	{
		int rows = miniTable.getRowCount();

		if ( firstRow != -1 )
		{
	        ColumnInfo[] colInfo = this.getColumn();
		       
		   	Salary_Eligible = new BigDecimal(0.0);
			Hours_Eligible  = new BigDecimal(0.0);
			Employee_Part   = new BigDecimal(0.0);
			Employer_Part   = new BigDecimal(0.0);
			AccumulationAmount = new BigDecimal(0.0);
			RetrievalAmount = new BigDecimal(0.0);
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
			        		BigDecimal amt = Env.ZERO;
			        		if ( colName.contains("Employee_Part") )
			        		{
			        			amt = (BigDecimal)miniTable.getModel().getValueAt(i, j);
			        			Employee_Part = Employee_Part.add(amt);
			        		}
			        		if ( colName.contains("Employer_Part") )
			        		{
			        			amt = (BigDecimal)miniTable.getModel().getValueAt(i, j);
			        			Employer_Part = Employer_Part.add(amt);
			        		}
			        		if ( colName.contains("AccumulationAmount") )
			        		{
			        			amt = (BigDecimal)miniTable.getModel().getValueAt(i, j);
			        			AccumulationAmount = AccumulationAmount.add(amt);
			        		}
			        		if ( colName.contains("RetrievalAmount") )
			        		{
			        			amt = (BigDecimal)miniTable.getModel().getValueAt(i, j);
			        			RetrievalAmount = RetrievalAmount.add(amt);
			        		}
			        		if ( colName.contains("Salary_Eligible") )
			        		{
			        			amt = (BigDecimal)miniTable.getModel().getValueAt(i, j);
			        			Salary_Eligible = Salary_Eligible.add(amt);
			        		}
			        		if ( colName.contains("Hours_Eligible") )
			        		{
			        			amt = (BigDecimal)miniTable.getModel().getValueAt(i, j);
			        			Hours_Eligible = Hours_Eligible.add(amt);
			        		}

			        		
			        	}
			        }
					m_noSelected++;
				}
			}
		}

		
		//  Information
		StringBuffer info = new StringBuffer();
		info.append(m_noSelected).append(" ").append(Msg.getMsg(Env.getCtx(), "Selected")).append(" - ");

		info.append(Msg.translate(Env.getCtx(), "Salary_Eligible")).append(" ");
		info.append(m_format.format(Salary_Eligible)).append(", ");
		info.append(Msg.translate(Env.getCtx(), "Hours_Eligible")).append(" ");
		info.append(m_format.format(Hours_Eligible)).append(", ");
		info.append(Msg.translate(Env.getCtx(), "Employee_Part")).append(" ");
		info.append(m_format.format(Employee_Part)).append(", ");
		info.append(Msg.translate(Env.getCtx(), "Employer_Part") ).append(" ");
		info.append(m_format.format(Employer_Part)).append(", ");
		info.append(Msg.translate(Env.getCtx(), "AccumulationAmount") ).append(" ");
		info.append(m_format.format(AccumulationAmount)).append(", ");
		info.append(Msg.translate(Env.getCtx(), "RetrievalAmount") ).append(" ");
		info.append(m_format.format(RetrievalAmount));
				
		statusBar.setInfo(info.toString());
		statusBar.setStatusLine( m_infoQuery );
		if ( miniTable.getSelectedRow() != -1)
			getStatusBar().setStatusDB( m_noSelected + "/" + rows, null);
		else
			getStatusBar().setStatusDB( m_noSelected + "/" + rows, null);
		//
	}   //  calculateSelection

	/**
	 *  Query and create TableInfo
	 */
	public void loadTableInfo()
	{
		//  Get
		String sql =  m_sql;

		if ( query != null && query.getWhereClause().length() != 0 )
		{
			sql += " and " + query.getWhereClause();
		}

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
        		tab.getAD_Tab_ID(), mAD_Table_ID, "RV_P_Payment_Deduction", tab.getWhereClause(), findFields, 1, CustomWindowFind.QUERYTYPE_Deduction  );

        query = find.getQuery();
        m_infoQuery = find.getInfoQuery();

		if ( find.getColumnAdm() != null )
			query.addRestriction(" RV_P_Payment_Deduction.P_Deduction_Param_ID in ( select P_Deduction_Param_ID from P_Deduction_Param where P_Deduction_Param.P_Column_Adm_ID = " + find.getColumnAdm()  + " )" );

		if ( find.getDeductionCategory() != null )
			query.addRestriction(" RV_P_Payment_Deduction.P_Deduction_ID in ( select P_Deduction_ID from P_Deduction where P_Deduction.P_Deduction_Category_ID = " + find.getDeductionCategory()  + " )" );

		if ( find.getDeductionFamily() != null )
			query.addRestriction(" RV_P_Payment_Deduction.P_Deduction_ID in ( select P_Deduction_ID from P_Deduction where P_Deduction.P_Deduction_Family_ID = " + find.getDeductionFamily()  + " )" );
		
		
		if ( query != null && find.getP_Year_ID() != 0)
		{
			query.addRestriction(" P_YEAR_ID = " + find.getP_Year_ID() ) ;
		}

		if ( query != null && find.getP_Period_From_ID() != 0)
		{
			if ( find.isSearchPeriodPayment())
				query.addRestriction(" ( P_Period_ID between " + find.getP_Period_From_ID() + " AND " + find.getP_Period_To_ID() + " ) " );
			else
				query.addRestriction(" ( P_Period_Origine_ID between " + find.getP_Period_From_ID() + " AND " + find.getP_Period_To_ID() + " ) " );
			
			if ( find.getP_Period_To_ID() == 0 || find.getP_Period_From_ID() == find.getP_Period_To_ID()  )
			{
				P_Period Period = P_Period.get(Env.getCtx(), find.getP_Period_From_ID(), null);
				m_infoQuery += " - Période " + Period.getName();
			}
			else
			{
				P_Period Period = P_Period.get(Env.getCtx(), find.getP_Period_From_ID(), null);
				m_infoQuery += " - Période entre  " + Period.getName() + " et ";
				Period = P_Period.get(Env.getCtx(), find.getP_Period_To_ID(), null);
				m_infoQuery += Period.getName();
			}

		}
		
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

		
		if ( find != null && find.getLongTermLeave_ID() != -1 )
		{
			query.addRestriction(" ( P_Employee_ID IN ( Select P_Employee.P_Employee_ID from P_Employee WHERE P_Employee.P_LongTermLeave_ID = " + find.getLongTermLeave_ID() + " )) " );
			m_infoQuery += " - Statut d'activité " + P_LongTermLeave.get(Env.getCtx(), find.getLongTermLeave_ID(), null).getName();
		}

		//-2012.12.16

		System.out.println("query = " + query);
		
        m_GroupByOption = find.getGroupByOption();
		System.out.println("selection = " +  m_GroupByOption);

        find = null;
        if(query != null)
        {
        	loadTableInfo();        	
        }
        
        super.cmd_find();
   } //	cmd_find


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
				org.solstice.util.ExcelExportUtil.exportResultSet(rs, this.getColumn(), "Deduction_Detail", this);
			} finally {
				rs.close();
				stmt.close();
			}
		} catch (Exception e) {
			s_log.log(Level.SEVERE, "export", e);
		}
	}
}