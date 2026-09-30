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

import java.awt.Dimension;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.compiere.apps.search.Find;
import org.compiere.model.GridField;
import org.compiere.model.MQuery;
import org.compiere.model.MRole;
import org.compiere.model.MTab;
import org.compiere.model.MTable;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.solstice.apps.form.VTimeSheetModel.DETParticularTimeSheetModel;
import org.solstice.apps.form.VTimeSheetModel.DETParticularTimeSheetModelSum;
import org.solstice.apps.form.VTimeSheetModel.DETableEditor;
import org.solstice.apps.form.VTimeSheetModel.DETableRenderer;
import org.solstice.apps.form.VTimeSheetModel.Lookup.LookupActivity;
import org.solstice.apps.form.VTimeSheetModel.Lookup.LookupAssignment;
import org.solstice.apps.form.VTimeSheetModel.Lookup.LookupEmployee;
import org.solstice.apps.form.VTimeSheetModel.Lookup.LookupGain;
import org.solstice.apps.form.VTimeSheetModel.Lookup.LookupPeriod;
import org.solstice.apps.form.VTimeSheetModel.Lookup.LookupDept;

import solstice.model.P_Frequency;
import solstice.model.P_Period;

/**
 * @author jeacat01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class VParticularTimeSheet extends CustomWindow {
    
    private DETParticularTimeSheetModel m_model; 
    private DETParticularTimeSheetModelSum m_modelSum; 
    private JTable m_table; 
    
    private LookupEmployee m_employees;
    private LookupAssignment m_assignements;
    private LookupGain m_gains;
    private LookupPeriod m_periods;
    private LookupActivity m_activity;
    private LookupDept p_department;
    

    final public int AD_Form_ID = 2100078;

    public VParticularTimeSheet() {
	    MRole role = MRole.getDefault(Env.getCtx(), true);
	    setReadOnly( !role.getFormAccess(AD_Form_ID).booleanValue() );
	}

    private boolean m_isReadOnly = false;
    public void setReadOnly( boolean readOnly ) { m_isReadOnly = readOnly; }
    public boolean isReadOnly() { return m_isReadOnly; }
    
	public int DisplayDetail = 0;

    public JComponent getMainPanel() 
    {

        //TODO read PaymentGroup
        if ( Env.getAD_Client_ID(Env.getCtx()) != 11 )
        	DisplayDetail = 1;	
        else
        	DisplayDetail = 0;

        JScrollPane panel;
        
    	if (DisplayDetail == 0)
    	{
    	    m_employees = new LookupEmployee();
    	    m_assignements = new LookupAssignment(true);
    	    m_gains = new LookupGain();
    	    m_periods = new LookupPeriod();
    	    
    	    m_model = new DETParticularTimeSheetModel( new Vector(), m_employees, m_gains, m_assignements, m_periods );

    	    m_model.addTableModelListener( new TableModelListener() { 
                public void tableChanged(TableModelEvent e) {
                    if( e.getType() == TableModelEvent.UPDATE ) enableSave( true ); 
                }
    	    } );
    	    
    	    m_table = new JTable( m_model );
    	    m_table.setEnabled( isReadOnly() == false );

    	    new DETableRenderer().bindTable( m_table, m_model, new DETableEditor() );
    	    panel = new JScrollPane(m_table);
    	    
    	    panel.setPreferredSize( new Dimension(900, 600) );
    	    
    	    m_model.load();
  		
    	}
    	else
    	{
        	m_employees = new LookupEmployee();
    	    m_assignements = new LookupAssignment(true);
    	    m_gains = new LookupGain();
    	    m_periods = new LookupPeriod();
    	    m_activity = new LookupActivity();
    	    p_department = new LookupDept();
    	    m_modelSum = new DETParticularTimeSheetModelSum( new Vector(), m_employees, m_gains, m_assignements, m_periods,m_activity,p_department);
    	    
    	    m_modelSum.addTableModelListener( new TableModelListener() { 
                public void tableChanged(TableModelEvent e) {
                    if( e.getType() == TableModelEvent.UPDATE ) enableSave( true ); 
                }
    	    } );
    	    
    	    m_table = new JTable( m_modelSum );
    	    m_table.setEnabled( isReadOnly() == false );

    	    new DETableRenderer().bindTable( m_table, m_modelSum, new DETableEditor() );
    	    panel = new JScrollPane(m_table);
    	    
    	    panel.setPreferredSize( new Dimension(900, 600) );
    	    
    	    m_modelSum.load();
    		
    	}
	    return panel;
	}
    
    protected boolean cmd_save(boolean manualCmd) {
        if( isReadOnly() ) return false;
        
        boolean ret = false;
    	if (DisplayDetail == 0)
    		ret = m_model.save(null);
    	else
    		ret = m_modelSum.save(null);
    		
        if( ret == true ) cmd_refresh();
        return ret;
    }
    
    protected void cmd_refresh() 
    { 
    	if (DisplayDetail == 0)
    		m_model.load();
    	else
    		m_modelSum.load();
    		
    	enableSave( false ); 
    }

    protected void cmd_ignore() { cmd_refresh(); }

    public void cmd_zoomAcross()
    {
    	int record_ID = m_employees.lookup(m_table.getValueAt(m_table.getSelectedRow(), DETParticularTimeSheetModel.COL_EMPLOYEE)); 
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
		doZoomAcross("P_Employee", query);
    } //	cmd_zoom
    
    protected void cmd_new(boolean copy) {
        if( isReadOnly() ) return;
        
        cmd_save(false);
        
    	if (DisplayDetail == 0)
    		m_model.clear();
    	else
    		m_modelSum.clear();
    		
    }
    
    public void cmd_find()
    {
        int P_EMPLOYEE_AD_Window_ID = -1;
        int P_EMPLOYEE_AD_Tab_ID = -1;
        int P_EMPLOYEE_AD_Table_ID = -1;
        try
        {
            Statement statm = DB.createStatement();
            ResultSet rs = statm.executeQuery(
            	"SELECT tab.AD_Window_ID, tab.AD_Tab_ID, tbl.AD_Table_ID " + 
				"FROM AD_Tab tab, AD_Table tbl " + 
				"WHERE tab.AD_Table_ID = tbl.AD_Table_ID " +
				"AND tbl.Name = 'P_Employee' AND tab.Name = 'Employee'");
            
            if( rs.next() ) {
                P_EMPLOYEE_AD_Window_ID = rs.getInt(1); 
                P_EMPLOYEE_AD_Tab_ID = rs.getInt(2);
                P_EMPLOYEE_AD_Table_ID = rs.getInt(3);
            }
            
            rs.close();
            statm.close();
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        }
        
        System.out.println(" VTimeSheet Find ");
        String trxName = null;
        
        MTable table = new MTable(Env.getCtx(), P_EMPLOYEE_AD_Table_ID, trxName);
        MTab   tab  = new MTab(Env.getCtx(), P_EMPLOYEE_AD_Tab_ID, trxName);
        
        GridField[] findFields = GridField.createFields(Env.getCtx(), 0, 0, P_EMPLOYEE_AD_Tab_ID);

        Find find = new Find (Env.getFrame(this), P_EMPLOYEE_AD_Window_ID, tab.getName(), tab.getAD_Tab_ID(),
        		P_EMPLOYEE_AD_Table_ID, table.getName(), tab.getWhereClause(), findFields, 0);

        MQuery query = find.getQuery();
        
    	if (DisplayDetail == 0)
    	{
            if(query != null)
            	m_model.load(query.getWhereClause());
            else
            	m_model.load();
    	}
    	else
    	{
            if(query != null)
            	m_modelSum.load(query.getWhereClause());
            else
            	m_modelSum.load();
    	}

    } //	cmd_find

}
