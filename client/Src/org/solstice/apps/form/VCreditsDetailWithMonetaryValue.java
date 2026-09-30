/******************************************************************************
			PMovementVariation = new BigDecimal(0.0);
			m_noSelected = 0;
			for (int i = 0; i < rows; i++)
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
import java.util.Calendar;
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


/**
 * @author Marc Morissette
 *
 * Calcul des Creditss pour un interval de périodes
 */
public class VCreditsDetailWithMonetaryValue extends CustomWindowQuery
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *  VCreditsTotal Constructor
	 */
	public VCreditsDetailWithMonetaryValue()
	{
		getConstance( "P_Credits_Movement" , 2000042);
//		dynInit();
	}   //  VCreditsDetail

	//


	/** Dispose active                                  */
	private boolean         m_disposing = false;

	private int m_GroupByOption = 1;
	private String m_GroupByQuery ;

	private Calendar calendar = Calendar.getInstance();
	private java.util.Date now = calendar.getTime();

	private Timestamp EvaluationDate = new java.sql.Timestamp(now.getTime());

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
			"RV_P_Credits_Movement " ,
			
			//	WHERE 
			" 1 = 1",
         	//	additional where & order in loadTableInfo() 
			true, "RV_P_Credits_Movement");
		//
		//
	}   //  dynInit


	public ColumnInfo[] getColumn()
	{
		Properties ctx = Env.getCtx();
		
		return new ColumnInfo[] 
  		    {
				//  0..4
				new ColumnInfo(" ", "1", IDColumn.class, false, false, null),
				new ColumnInfo(Msg.translate(ctx, "AD_Org_ID"), "Org", String.class, true, false, null),
				new ColumnInfo(Msg.translate(ctx, "P_Employee_ID"), "Employee", String.class, true, false, null),
				new ColumnInfo(Msg.translate(ctx, "Name"), "EmployeeName", String.class, true, false, null),
				new ColumnInfo(Msg.translate(ctx, "P_Year_ID"), "Year", String.class),
				new ColumnInfo(Msg.translate(ctx, "C_Activity_ID"), "Activity", String.class),
				new ColumnInfo(Msg.translate(ctx, "Day"), "Day", String.class),
				new ColumnInfo(Msg.translate(ctx, "P_Credits_ID"), "Credits", String.class, true, false, null),
				new ColumnInfo(Msg.translate(ctx, "PMOVEMENTVARIATION"), "PMovementVariation", BigDecimal.class),
				new ColumnInfo(Msg.translate(ctx, "P_UOM_ID"), "UOMValue", String.class),
				new ColumnInfo(Msg.translate(ctx, "Hourly_Rate"), "dbo.FN_Employee_Hourly_Rate_others( P_Employee_ID, ? ) as Hourly_Rate ", BigDecimal.class),
				new ColumnInfo(Msg.translate(ctx, "MonetaryValue"), "CASE WHEN P_UOM_ID = 100 THEN PMovementVariation * dbo.FN_Employee_Hourly_Rate( P_Employee_ID, ? ) WHEN P_UOM_ID = 101 THEN PMovementVariation / dbo.FN_Employee_Hourly_Rate( P_Employee_ID, ? ) ELSE null END as MonetaryValue ", BigDecimal.class),
				new ColumnInfo(Msg.translate(ctx, "P_Payment_ID"), "Payment", String.class),
				new ColumnInfo(Msg.translate(ctx, "Messages"), "[dbo].[FN_Validate_Assignment]( P_Employee_ID, ? )  ", String.class)
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
	private  BigDecimal PMovementVariation = new BigDecimal(0.0);
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

			PMovementVariation = new BigDecimal(0.0);
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
			        		if ( colName.contains("PMovementVariation") )
			        		{
			        			amt = (BigDecimal)miniTable.getModel().getValueAt(i, j);
			        			PMovementVariation = PMovementVariation.add(amt);
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

		info.append(Msg.translate(Env.getCtx(), "PMovementVariation")).append(" ");
		info.append(m_format.format(PMovementVariation)).append(", ");
				
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

		try
		{
			
		    PreparedStatement pstmt = DB.prepareStatement(sql, null);
			//
		    pstmt.setTimestamp(1, EvaluationDate );
		    pstmt.setTimestamp(2, EvaluationDate );
		    pstmt.setTimestamp(3, EvaluationDate );
		    pstmt.setTimestamp(4, EvaluationDate );

			ResultSet rs = pstmt.executeQuery();
			miniTable.loadTable(rs);
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
        		tab.getAD_Tab_ID(), mAD_Table_ID, "RV_P_Credits_Movement", tab.getWhereClause(), findFields, 1, CustomWindowFind.QUERYTYPE_AccrualBankWithMonetaryValue  );

        query = find.getQuery();
        
        if ( query != null )
        {
    		if ( find.getDate_From() != null && find.getDate_To() != null)
    		{
    			query.addRestriction(" ( day between " + DB.TO_DATE( find.getDate_From()) + " AND " + DB.TO_DATE( find.getDate_To()) + " ) " );
    		}
    		else if ( find.getDate_From() != null )
    		{
    			query.addRestriction(" day >= " + DB.TO_DATE( find.getDate_From())  );
    		}
    		else if ( find.getDate_To() != null )
    		{
    			query.addRestriction(" day <= " + DB.TO_DATE( find.getDate_To())  );
    		}
        	
    		if ( find.getEvaluationDate() != null )
    			EvaluationDate = find.getEvaluationDate();
    		else
    			EvaluationDate = new java.sql.Timestamp(now.getTime());

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
			stmt.setTimestamp(1, EvaluationDate);
			stmt.setTimestamp(2, EvaluationDate);
			stmt.setTimestamp(3, EvaluationDate);
			stmt.setTimestamp(4, EvaluationDate);

			ResultSet rs = stmt.executeQuery();
			try {
				org.solstice.util.ExcelExportUtil.exportResultSet(rs, this.getColumn(), "Credits_Detail_Valeur_Monetaire", this);
			} finally {
				rs.close();
				stmt.close();
			}
		} catch (Exception e) {
			s_log.log(Level.SEVERE, "export", e);
		}
	}
}