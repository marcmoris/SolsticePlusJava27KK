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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;

import javax.swing.JFileChooser;


import org.compiere.apps.ConfirmPanel;
import org.compiere.grid.ed.VLookup;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.model.GridField;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MTab;
import org.compiere.model.MTable;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import solstice.model.P_LongTermLeave;
import solstice.utils.PgiUtil;


/**
 * @author Marc Morissette
 *
 * 
 */
public class EmployeeGlobalChange extends CustomWindowQuery
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**	Logger							*/
	public static CLogger		s_log = CLogger.getCLogger (EmployeeGlobalChange.class);


	/**
	 *  VDeductionTotal Constructor
	 */
	public EmployeeGlobalChange()
	{
		getConstance( "P_Employee" , 2200415);
		

//		dynInit();
	}   //  EmployeeGlobalChange

	//
	private CPanel southPanel = new CPanel(new GridBagLayout());
	private BorderLayout southLayout = new BorderLayout();


	public CLabel labelManager = new CLabel();
	public VLookup fieldManager;
	private ConfirmPanel confirmPanelS = new ConfirmPanel(true);


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
			"RV_P_Employee_Global_Change " ,
			
			//	WHERE 
			" 1 = 1",
         	//	additional where & order in loadTableInfo() 
			true, "RV_P_Employee_Global_Change");
		//
		//

	}   //  dynInit


	public ColumnInfo[] getColumn()
	{
		Properties ctx = Env.getCtx();
        String client = PgiUtil.getSolsticeParameter(Env.getCtx(),"Client");
    		return new ColumnInfo[] 
   		    {
    				//  0..4
//					new ColumnInfo(" ", "1", IDColumn.class, false, false, null),
					new ColumnInfo(Msg.translate(ctx, "AD_Org_ID"), "Org", String.class, true, false, null),
					new ColumnInfo(Msg.translate(ctx, "C_Activity_ID"),"C_Activity", String.class, true, false, null),   
					new ColumnInfo(Msg.translate(ctx, "P_Department_ID"),"P_Department", String.class, true, false, null),   
					new ColumnInfo(Msg.translate(ctx, "P_Workplace_ID"),"P_Workplace", String.class, true, false, null),
					new ColumnInfo(Msg.translate(ctx, "P_Payment_Group_ID"),"P_Payment_Group", String.class, true, false, null),   
					new ColumnInfo(Msg.translate(ctx, "P_Employee_Manager_ID"), "Manager", String.class, true, false, null),
					new ColumnInfo(Msg.translate(ctx, "P_Employee_ID"), "Employee", String.class, true, false, null),
					new ColumnInfo(Msg.translate(ctx, "Name"), "EmployeeName", String.class, true, false, null),
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


	/**
	 *  Calculate selected rows.
	 *  - add up selected rows
	 */

	private int m_noSelected = 0;


	
	public void calculateSelection(int firstRow, int lastRow )
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
			        		
			        	}
			        }
					m_noSelected++;
				}
			}
		}

		
		//  Information
		StringBuffer info = new StringBuffer();
		info.append(m_noSelected).append(" ").append(Msg.getMsg(Env.getCtx(), "Selected")).append(" - ");

/*		
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
*/				
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
	private CPanel scontentPanel = new CPanel(new GridBagLayout());
	private GridBagLayout scontentLayout = new GridBagLayout();
	private CPanel simplePanel = new CPanel(new BorderLayout());
	private BorderLayout simpleLayout = new BorderLayout();


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
		

		southPanel.setPreferredSize(new Dimension(1024, 60));
		southPanel.setLayout(southLayout);
		

		labelManager.setText(Msg.translate(Env.getCtx(), "P_Employee_Manager_ID"));
		MLookup ManagerL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, getAD_Column_ID("P_Employee_Manager_ID"), DisplayType.Table);
		fieldManager = new VLookup ("P_Employee_Manager_ID", false, false, true, ManagerL);
		fieldManager.setBounds(206, 20, 210, 19);

		scontentPanel.setLayout(scontentLayout);
		simplePanel.setLayout(simpleLayout);

		int				sLine = 1;
		scontentPanel.add(labelManager,   new GridBagConstraints(1, sLine, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(7, 5, 5, 5), 0, 0));
		scontentPanel.add((Component)fieldManager,   new GridBagConstraints(2, sLine, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));


		
		confirmPanelS.getOKButton().setToolTipText(Msg.getMsg(Env.getCtx(),"QueryEnter"));
		confirmPanelS.getCancelButton().setToolTipText(Msg.getMsg(Env.getCtx(),"QueryCancel"));
		confirmPanelS.addActionListener(this);

		sLine = 2;
		scontentPanel.add((Component)confirmPanelS,   new GridBagConstraints(2, sLine, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));

//		scontentPanel.add(confirmPanelS );

		this.getFrame().add(scontentPanel, BorderLayout.SOUTH);



//		simplePanel.add(scontentPanel, BorderLayout.CENTER);


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
        		tab.getAD_Tab_ID(), mAD_Table_ID, "RV_P_Employee_Global_Change", tab.getWhereClause(), findFields, 1, CustomWindowFind.QUERYTYPE_Employee  );

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
				org.solstice.util.ExcelExportUtil.exportResultSet(rs, this.getColumn(), "Employes_Changement_Global", this);
			} finally {
				rs.close();
				stmt.close();
			}
		} catch (Exception e) {
			s_log.log(Level.SEVERE, "export", e);
		}
	}

	private int getAD_Column_ID( String ColumnName )
	{
		int id = 0;
		try
		{
			String sql = "select AD_COLUMN_ID from AD_COLUMN where columnNAME = '" +ColumnName+ "'   and AD_TABLE_ID = 2000092";
			PreparedStatement pstmt = null;
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
			System.err.println("CustomWindowFind getAD_Column_ID - " + e);
		}
		if ( id == 0)
		{
			try
			{
				String sql = "select AD_COLUMN_ID from AD_COLUMN where columnNAME = '" +ColumnName+ "'   and AD_TABLE_ID = 2100659";
				PreparedStatement pstmt = null;
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
				System.err.println("CustomWindowFind getAD_Column_ID - " + e);
			}
			
		}
		
		return id;
		
	}

	private void cmd_cancel()
	{
		dispose();
	}	//	cmd_ok

	public int getP_Employee_Manager_ID()
	{
		return Integer.parseInt( this.fieldManager.getValue().toString());
	}

	/**
	 *	Simple OK Button pressed
	 */
	private void cmd_ok()
	{
		int P_Employee_Manager_ID = getP_Employee_Manager_ID();

		if ( query != null && query.getWhereClause().length() != 0 )
		{
			String sql = "Update P_Employee set P_Employee_Manager_ID = " + P_Employee_Manager_ID + " WHERE " + query.getWhereClause();
					
			try
			{
				setBusy( true );
				int no = DB.executeUpdate (sql, null);
				setBusy( false );

				cmd_refresh();
			}
			catch (Exception e)
			{
				s_log.log(Level.SEVERE, sql, e);
			}
		}
	}

	@Override
	public void actionPerformed (ActionEvent e)
	{
		Object source = e.getSource();
		
		if (e.getActionCommand() == ConfirmPanel.A_CANCEL)
			cmd_cancel();

		else if (e.getActionCommand() == ConfirmPanel.A_REFRESH)
			cmd_refresh();
		
		if (source == confirmPanelS.getCancelButton())
			cmd_cancel();
		
		if (source == confirmPanelS.getOKButton())
			cmd_ok();

	}

    
}
