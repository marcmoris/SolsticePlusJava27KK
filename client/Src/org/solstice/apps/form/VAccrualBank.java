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

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;


import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.model.GridField;
import org.compiere.model.MTab;
import org.compiere.model.MTable;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Language;
import org.compiere.util.Msg;

/**
 * @author Marc Morissette
 *
 * Calcul des gains pour un interval de périodes
 */
public class VAccrualBank extends CustomWindowQuery
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	/**
	 *  VGainTotal Constructor
	 */
	public VAccrualBank()
	{
	}   //  VAccrualBank

	//

    public String getWindowTitle()
    {
    	return "Analyse des banques";
    };

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
		Language language = Env.getLanguage(Env.getCtx());

		m_sql = miniTable.prepareTable(getColumn(),
			//  0..4
	
			//	FROM
			"RV_P_Accrual_Bank " ,
			
			//	WHERE 
			" 1 = 1",
         	//	additional where & order in loadTableInfo() 
			true, "RV_P_Accrual_Bank");
		//
//		miniTable.getModel().addTableModelListener(this);
		//
//		m_cbbPayPeriodFrom.setMandatory(false);
//		m_cbbPayPeriodTo.setMandatory(false);
		//
	}   //  dynInit


	public ColumnInfo[] getColumn()
	{
		Properties ctx = Env.getCtx();
		if ( m_GroupByOption == 1 )
		{
			m_GroupByQuery  = "Org, Employee, EmployeeName, P_Year_ID, PaymentPeriod, Credits";

			return new ColumnInfo[] 
			          		    {
			          				//  0..4
			          					new ColumnInfo(" ", "1", IDColumn.class, false, false, null),
			          					new ColumnInfo(Msg.translate(ctx, "AD_Org_ID"), "Org", String.class, true, false, null),
			          					new ColumnInfo(Msg.translate(ctx, "P_Employee_ID"), "Employee", String.class, true, false, null),
			          					new ColumnInfo(Msg.translate(ctx, "Name"), "EmployeeName", String.class, true, false, null),
			          					new ColumnInfo(Msg.translate(ctx, "P_Year_ID"), "Year", String.class),
			          					new ColumnInfo(Msg.translate(ctx, "P_Period_ID") + " Paiement ", "PaymentPeriod", String.class),
			          					new ColumnInfo(Msg.translate(ctx, "P_Credits_ID"), "Credits", String.class, true, false, null),
			          					new ColumnInfo(Msg.translate(ctx, "PMOVEMENTVARIATION"), "SUM(PMOVEMENTVARIATION)", BigDecimal.class),
			          		    };
		}

		if ( m_GroupByOption == 2 )
		{
			m_GroupByQuery  = "Org, Employee, EmployeeName, P_Year_ID, PaymentPeriod, Day, Credits, Payment";

			return new ColumnInfo[] 
			          		    {
			          				//  0..4
			          					new ColumnInfo(" ", "1", IDColumn.class, false, false, null),
			          					new ColumnInfo(Msg.translate(ctx, "AD_Org_ID"), "Org", String.class, true, false, null),
			          					new ColumnInfo(Msg.translate(ctx, "P_Employee_ID"), "Employee", String.class, true, false, null),
			          					new ColumnInfo(Msg.translate(ctx, "Name"), "EmployeeName", String.class, true, false, null),
			          					new ColumnInfo(Msg.translate(ctx, "P_Year_ID"), "Year", String.class),
			          					new ColumnInfo(Msg.translate(ctx, "P_Period_ID") + " Paiement ", "PaymentPeriod", String.class),
			          					new ColumnInfo(Msg.translate(ctx, "Day"), "Day", String.class),
			          					new ColumnInfo(Msg.translate(ctx, "P_Credits_ID"), "Credits", String.class, true, false, null),
			          					new ColumnInfo(Msg.translate(ctx, "PMOVEMENTVARIATION"), "SUM(PMOVEMENTVARIATION)", BigDecimal.class),
			          					new ColumnInfo(Msg.translate(ctx, "P_Payment_ID"), "Payment", String.class),
			          		    };
		}

		return null;
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
	private BigDecimal Qty = new BigDecimal(0.0);

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

	        Qty = new BigDecimal(0.0);
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
			        		BigDecimal qty = (BigDecimal)miniTable.getModel().getValueAt(i, j);
			        		if ( colName.equals("SUM(PMOVEMENTVARIATION)") )
			        			Qty = Qty.add(qty);
			        	}
			        }
					m_noSelected++;
				}
			}
			
		}


		//  Information
		StringBuffer info = new StringBuffer();
		info.append(m_noSelected).append(" ").append(Msg.getMsg(Env.getCtx(), "Selected")).append(" - ");
		info.append(" Quantité : ");  //.append(", ");
		info.append(m_format.format(Qty)).append(", ");
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

		sql += " Group By " + m_GroupByQuery;
		
		try
		{
			
		    PreparedStatement pstmt = DB.prepareStatement(sql, null);
			//
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
        		tab.getAD_Tab_ID(), mAD_Table_ID, "RV_P_Accrual_Bank", tab.getWhereClause(), findFields, 1, CustomWindowFind.QUERYTYPE_AccrualBank );

/*    	find.setGroupByOption ( 1, "Solde de banque sommaire");
    	find.setGroupByOption ( 2, "Solde de banque détaillé par jour");
 */   	
    /*	    m_TypeQuery.addItem(new KeyNamePair(1, "Sommaire"));
    	    m_TypeQuery.addItem(new KeyNamePair(2, "Détaillé"));
    	    m_TypeQuery.addItem(new KeyNamePair(3, "Très Détaillé"));
    */
        query = find.getQuery();

//        ici
        m_GroupByOption = find.getGroupByOption();
		System.out.println("group By Option = " + m_GroupByQuery);
      
		System.out.println("query = " + query);

        find = null;
        if(query != null)
        {
        	loadTableInfo();        	
//            headerPanel.resetDefaultPeriod();
        }
        
        super.cmd_find();
   } //	cmd_find



}
