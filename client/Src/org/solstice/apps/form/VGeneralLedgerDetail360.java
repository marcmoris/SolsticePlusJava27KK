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

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.*;
import java.text.*;
import java.util.*;

import org.compiere.util.*;
import org.compiere.apps.*;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.apps.form.VPayPrint;
import org.compiere.grid.ed.*;
import org.compiere.minigrid.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.plaf.*;
import org.compiere.process.*;
import java.util.logging.*;


import solstice.model.P_Period;
import solstice.utils.PgiUtil;

/**
 *  Consultation des soldes de banques
 *
 *  @author Marc Morissette
 *  @version $Id: VGeneralLedgerDetail360.java,v 1.2 2008/05/21 13:01:43 marmor01 Exp $
 */

public class VGeneralLedgerDetail360  extends CustomWindowQuery
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	/**
	 *  VGeneralLedgerDetail360 Constructor
	 */
	public VGeneralLedgerDetail360()
	{
		getConstance( "P_Payment_Entryline_360" , 2000042);
//		dynInit();
	}   //  VGeneralLedgerDetail360

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
			"RV_P_Payment_Entryline_360 " ,
			
			//	WHERE 
			" 1 = 1",
         	//	additional where & order in loadTableInfo() 
			true, "RV_P_Payment_Entryline_360");
		//
		//
	}   //  dynInit


	public ColumnInfo[] getColumn()
	{
		Properties ctx = Env.getCtx();
		
			return new ColumnInfo[] 
		               {
				//  0..4
    				new ColumnInfo(Msg.translate(ctx, "AD_Org_ID"), "Org",	String.class, true, false, null),
       				new ColumnInfo(Msg.translate(ctx, "P_Payment_Group_ID"),"P_Payment_Group", String.class, true, false, null),   
    				new ColumnInfo(Msg.translate(ctx, "P_Payment_ID"), "Payment",	String.class),
    				new ColumnInfo(Msg.translate(ctx, "P_Period_ID") + " Paiement ","PaymentPeriod", String.class), 
    				new ColumnInfo(Msg.translate(ctx, "P_Department_ID"),"P_Department", String.class, true, false, null),   
    				new ColumnInfo(Msg.translate(ctx, "P_Employee_ID"), "Employee",	String.class, true, false, null),
    				new ColumnInfo(Msg.translate(ctx, "Name"), "EmployeeName",	String.class, true, false, null),
//    				new ColumnInfo(Msg.translate(ctx, "P_Workplace_ID"),"P_Workplace", String.class, true, false, null),   
    				new ColumnInfo(Msg.translate(ctx, "AD_Org_ID"), "Org_imp",	String.class, true, false, null),
    				new ColumnInfo(Msg.translate(ctx, "P_Workplace_ID"), "SalesRegion", String.class, true, false, null),
    				new ColumnInfo(Msg.translate(ctx, "C_ElementValue_ID"), "ElementValue", String.class, true, false, null),
       				new ColumnInfo(Msg.translate(ctx, "C_Activity_ID"),"C_Activity", String.class, true, false, null),   
    				new ColumnInfo(Msg.translate(ctx, "AmtAcctDR"), "isnull(AmtAcctDR, 0) as AmtAcctDR", BigDecimal.class),
    				new ColumnInfo(Msg.translate(ctx, "AmtAcctCR"), "isnull(AmtAcctCR, 0) as AmtAcctCR", BigDecimal.class)
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

	private 		BigDecimal AmtAcctDR = new BigDecimal(0.0);
	private 		BigDecimal AmtAcctCR = new BigDecimal(0.0);


	/**
	 *  Calculate selected rows.
	 *  - add up selected rows
	 */
	public void calculateSelection( int firstRow, int lastRow)
	{
		int rows = miniTable.getRowCount();
		if ( firstRow != -1 )
		{
	       ColumnInfo[] colInfo = this.getColumn();

	       	AmtAcctDR = new BigDecimal(0.0);
	       	AmtAcctCR = new BigDecimal(0.0);
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
			        		if ( colName.contains("AmtAcctDR") )
			        		{
			        			amt = (BigDecimal)miniTable.getModel().getValueAt(i, j);
			        			AmtAcctDR = AmtAcctDR.add(amt);
			        		}
			        		if ( colName.contains("AmtAcctCR") )
			        		{
			        			amt = (BigDecimal)miniTable.getModel().getValueAt(i, j);
			        			AmtAcctCR = AmtAcctCR.add(amt);
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

		info.append(" Débit : ");  //.append(", ");
		info.append(m_format.format(AmtAcctDR));  //.append(", ");
		info.append(" Crédit : ");  //.append(", ");
		info.append(m_format.format(AmtAcctCR));  //.append(", ");
				
		statusBar.setInfo(info.toString());
		statusBar.setStatusLine(" Information sur la recherche : "  );
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

		if ( sql == null)
			return;

//		sql += " Group By Org, Employee, EmployeeName, C_ElementValue_ID, C_Activity_ID ";

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
		calculateSelection(-1, -1);
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
            		tab.getAD_Tab_ID(), mAD_Table_ID, "RV_P_Payment_EntryLine_360", tab.getWhereClause(), findFields, 1, CustomWindowFind.QUERYTYPE_GeneralLedger360  );
        
        query = find.getQuery();
       
		if ( query != null && find.getP_Year_ID() != 0)
		{
			query.addRestriction(" P_YEAR_ID = " + find.getP_Year_ID() ) ;
		}
        
		if (find.isSearchPeriod() && query != null && find.getP_Period_From_ID() != 0) {
			query.addRestriction(" ( P_Period_ID between "
					+ find.getP_Period_From_ID() + " AND "
					+ find.getP_Period_To_ID() + " ) ");
		}

  

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
				org.solstice.util.ExcelExportUtil.exportResultSet(rs, this.getColumn(), "Grand_Livre_Detail360", this);
			} finally {
				rs.close();
				stmt.close();
			}
		} catch (Exception e) {
			s_log.log(Level.SEVERE, "export", e);
		}
	}
}