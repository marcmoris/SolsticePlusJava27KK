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
import java.awt.Dimension;
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
 *         Calcul des gains pour un interval de périodes
 */
public class VGainDetail extends CustomWindowQuery {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * VGainTotal Constructor
	 */
	public VGainDetail() {
		getConstance("P_Payment_Gain", 2000042);

		// dynInit();
	} // VGainDetail

	//

	/** Dispose active */
	private boolean m_disposing = false;

	private int m_GroupByOption = 1;
	private String m_GroupByQuery;

	/**
	 * Dynamic Init. - Load Bank Info - Load Employee - Init Table
	 */
	public void dynInit() {
		int rows = miniTable.getRowCount();
		ColumnInfo[] colInfo = this.getColumn();
		for (int i = 0; i < rows; i++) {
			miniTable.removeColumn(miniTable.getColumn(colInfo[i]
					.getColHeader()));
		}

		m_sql = miniTable.prepareTable(getColumn(),
		// 0..4

				// FROM
				"RV_P_Payment_Gain ",

				// WHERE
				" 1 = 1",
				// additional where & order in loadTableInfo()
				true, "RV_P_Payment_Gain");
		//
		//

	} // dynInit

	public ColumnInfo[] getColumn() {
		Properties ctx = Env.getCtx();
        String client = PgiUtil.getSolsticeParameter(Env.getCtx(),"Client");
        if ( client.equals("CHL") )
        {
    		return new ColumnInfo[] {
    				// 0..4
 //   				new ColumnInfo(" ", "1", IDColumn.class, false, false, null),
    				new ColumnInfo(Msg.translate(ctx, "AD_Org_ID"), "Org",	String.class, true, false, null),
    				new ColumnInfo(Msg.translate(ctx, "C_Activity_ID"),"C_Activity", String.class, true, false, null),   
    				new ColumnInfo(Msg.translate(ctx, "P_Department_ID"),"P_Department", String.class, true, false, null),   
    				new ColumnInfo(Msg.translate(ctx, "P_Workplace_ID"),"P_Workplace", String.class, true, false, null),   
    				new ColumnInfo(Msg.translate(ctx, "P_Payment_Group_ID"),"P_Payment_Group", String.class, true, false, null),   
    				new ColumnInfo(Msg.translate(ctx, "P_Employee_ID"), "Employee",	String.class, true, false, null),
    				new ColumnInfo(Msg.translate(ctx, "Name"), "EmployeeName",	String.class, true, false, null),
    				new ColumnInfo(
    						Msg.translate(ctx, "P_Period_ID") + " Paiement ",
    						"PaymentPeriod", String.class),
    				new ColumnInfo(Msg.translate(ctx, "P_Period_ID"), "Period",
    						String.class),
    				new ColumnInfo(Msg.translate(ctx, "Day"), "dbo.fnFormatDate(DAY, N'YYYY-MM-DD', 1)", String.class),
    				new ColumnInfo(Msg.translate(ctx, "P_Gain_ID"), "Earning",
    						String.class, true, false, null),
    				new ColumnInfo(Msg.translate(ctx, "QuantityCalc"),
    						"QuantityCalc", BigDecimal.class),
    				new ColumnInfo(Msg.translate(ctx, "AmountCalc"), "AmountCalc",
    						BigDecimal.class),
    				new ColumnInfo(Msg.translate(ctx, "P_Payment_ID"), "Payment",
    						String.class), };
        	
        }
        else
        {
        	return new ColumnInfo[] {
    				// 0..4
//    				new ColumnInfo(" ", "1", IDColumn.class, false, false, null),
    				new ColumnInfo(Msg.translate(ctx, "AD_Org_ID"), "Org",	String.class, true, false, null),
    				new ColumnInfo(Msg.translate(ctx, "P_Payment_Group_ID"),"P_Payment_Group", String.class, true, false, null),   
    				new ColumnInfo(Msg.translate(ctx, "P_Occupation_Group_ID"),"P_Occupation_Group", String.class, true, false, null),   
    				new ColumnInfo(Msg.translate(ctx, "P_Employee_ID"), "Employee",	String.class, true, false, null),
    				new ColumnInfo(Msg.translate(ctx, "Name"), "EmployeeName",	String.class, true, false, null),
    				new ColumnInfo(Msg.translate(ctx, "IsActive"), "IsActive", Boolean.class),
    				new ColumnInfo(
    						Msg.translate(ctx, "P_Period_ID") + " Paiement ",
    						"PaymentPeriod", String.class),
    				new ColumnInfo(Msg.translate(ctx, "P_Period_ID"), "Period",
    						String.class),
 	    			new ColumnInfo(Msg.translate(ctx, "Day"), "dbo.fnFormatDate(DAY, N'YYYY-MM-DD', 1)", String.class),
    				new ColumnInfo(Msg.translate(ctx, "P_Gain_ID"), "Earning",
    						String.class, true, false, null),
    				new ColumnInfo(Msg.translate(ctx, "QuantityCalc"),
    						"QuantityCalc", BigDecimal.class),
    				new ColumnInfo(Msg.translate(ctx, "AmountCalc"), "AmountCalc",
    						BigDecimal.class),
    				new ColumnInfo(Msg.translate(ctx, "P_Payment_ID"), "Payment",
    						String.class), };

        }
    		


	}

	/**
	 * Dispose
	 */
	public void dispose() {
		m_disposing = true;

		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	} // dispose

	private int m_noSelected = 0;

	private BigDecimal GainAmt = new BigDecimal(0.0);
	private BigDecimal GainQty = new BigDecimal(0.0);

	
	/*************************************************************************/

	/**
	 * Calculate selected rows. - add up selected rows
	 */
	public void calculateSelection(int firstRow, int lastRow) {
		int rows = miniTable.getRowCount();
		if ( firstRow != -1 )
		{

			ColumnInfo[] colInfo = this.getColumn();
			m_noSelected = 0;
			GainAmt = new BigDecimal(0.0);
			GainQty = new BigDecimal(0.0);

			for (int i = 0; i < rows; i++) 
			{
				boolean isSelected = miniTable.isRowSelected(i);
//				IDColumn id = (IDColumn) miniTable.getModel().getValueAt(i, 0);
				if (isSelected ) 
				{
					for (int j = 0; j <= colInfo.length - 1; j++) {
						String colName = colInfo[j].getColSQL();
						if (colInfo[j].getColClass().equals(BigDecimal.class)) {
							BigDecimal qty = Env.ZERO;
							if (colName.equals("QuantityCalc")) {
								qty = (BigDecimal) miniTable.getModel().getValueAt(
										i, j);
								GainQty = GainQty.add(qty);
							}
							if (colName.equals("AmountCalc")) {
								qty = (BigDecimal) miniTable.getModel().getValueAt(
										i, j);
								GainAmt = GainAmt.add(qty);
							}
						}
					}
					m_noSelected++;
				}
			}
		}


		// Information
		StringBuffer info = new StringBuffer();
		info.append(m_noSelected).append(" ")
				.append(Msg.getMsg(Env.getCtx(), "Selected")).append(" - ");
		info.append(Msg.translate(Env.getCtx(), "QuantityCalc")).append(" : ");
		info.append(m_format.format(GainQty)).append(", ");
		info.append(Msg.translate(Env.getCtx(), "AmountCalc")).append(" : ");
		info.append(m_format.format(GainAmt)); // .append(", ");
		statusBar.setInfo(info.toString());
//		statusBar.setStatusLine("Recherche : ");
		statusBar.setStatusLine( m_infoQuery );
		statusBar.setAutoscrolls(true);
		
		getStatusBar().setStatusDB( m_noSelected + "/" + rows, null);
		//
	} // calculateSelection

	/**
	 * Query and create TableInfo
	 */
	public void loadTableInfo() {
		// Get
		String sql = m_sql;

		if (query != null && query.getWhereClause() != null
				&& query.getWhereClause().length() != 0) {
			sql += " and " + query.getWhereClause();
		}

		if (sql == null)
			return;

		try {

			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			//
			setBusy( true );
			ResultSet rs = pstmt.executeQuery();
			miniTable.loadTable(rs);
			setBusy( false );
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			s_log.log(Level.SEVERE, "loadTableInfo", e);
		}
		calculateSelection( -1, -1);
	} // loadTableInfo

	public void cmd_refresh() {
		setBusy(true);
		loadTableInfo();
		setBusy(false);
	}

	/**
	 * Find - Set Query
	 */
	public void cmd_find() {

		String trxName = null;

		MTable table = new MTable(Env.getCtx(), mAD_Table_ID, trxName);
		MTab tab = new MTab(Env.getCtx(), mAD_Tab_ID, trxName);

		GridField[] findFields = GridField.createFields(Env.getCtx(), 0, 0,
				mAD_Tab_ID);

 
		CustomWindowFind find = new CustomWindowFind(Env.getFrame(this),
				mAD_Window_ID, getTitle(this.getClass().getName()),
				tab.getAD_Tab_ID(), mAD_Table_ID, "RV_P_Payment_Gain",
				tab.getWhereClause(), findFields, 1, 
				CustomWindowFind.QUERYTYPE_Earning);

		query = find.getQuery();

		m_infoQuery = find.getInfoQuery();

		if ( find.getEarningInfo() != null )
			query.addRestriction(" Exists ( Select 1 from P_Gain_GainInfo Where To_Consider = 'Y' and P_Gain_GainInfo.P_Gain_ID = RV_P_Payment_Gain.P_Gain_ID and P_Gain_GainInfo.P_GainInfo_ID = " + find.getEarningInfo()  + " )" );

		if ( find.getColumnAdm() != null )
			query.addRestriction(" Exists ( Select 1 from RV_P_Gain_Column_Adm where RV_P_Gain_Column_Adm.P_Gain_ID = RV_P_Payment_Gain.P_Gain_ID and RV_P_Gain_Column_Adm.P_Column_Adm_ID = " + find.getColumnAdm()  + " )" );

		if ( query != null && find.getP_Year_ID() != 0)
		{
			query.addRestriction(" P_YEAR_ID = " + find.getP_Year_ID() ) ;
		}
	
		if (find.isSearchPeriod() && query != null && find.getP_Period_From_ID() != 0) {
			if ( find.isSearchPeriodPayment())
				query.addRestriction(" ( P_Period_ID between " + find.getP_Period_From_ID() + " AND " + find.getP_Period_To_ID() + " ) " );
			else
				query.addRestriction(" ( P_Period_Origine_ID between "
					+ find.getP_Period_From_ID() + " AND "
					+ find.getP_Period_To_ID() + " ) ");
			
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

		//+ 2012.04.16
		if ( find.isSearchDate() && query != null && find.getDate_From() != null )
		{
			query.addRestriction(" ( Day between "
					+ DB.TO_DATE( find.getDate_From() ) + " AND "
					+ DB.TO_DATE( find.getDate_To() ) + " ) ");

			if ( find.getDate_To() != null)
			{
				m_infoQuery += " - Entre le  " + find.getDate_From().toString().substring(0,10) + " et " + find.getDate_To().toString().substring(0,10) ;	
			}
			else
			{
				m_infoQuery += " - Entre le  " + find.getDate_From().toString().substring(0,10) + " et aujourd'hui" ;
			}
		
		}
		//- 2012.04.16

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
		
		System.out.println("selection = " + m_GroupByOption);

		find = null;
		if (query != null) {
			loadTableInfo();
			// headerPanel.resetDefaultPeriod();
		}

		super.cmd_find();
	} // cmd_find

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
				org.solstice.util.ExcelExportUtil.exportResultSet(rs, this.getColumn(), "Gain_Detail", this);
			} finally {
				rs.close();
				stmt.close();
			}
		} catch (Exception e) {
			s_log.log(Level.SEVERE, "export", e);
		}
	}
}