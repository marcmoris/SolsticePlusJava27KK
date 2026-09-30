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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.compiere.grid.ed.VLookup;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MRole;
import org.compiere.model.MUser;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

/**
 * @author frabou01
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class VEmployeeListPanel extends CPanel {

    //MainPanel
    private JList listEmployee = null;
    private CPanel statusPanel = null;

    //	private CPanel timeSheetStatusPanel = null;
    private JRadioButton rdbActive = null;
    private JRadioButton rdbInactive = null;

    private VLookup cbCalendar;

    //Status Panel
    private ButtonGroup statusGroup = null;
    private JLabel lblActif = null;
    private JLabel lblInactif = null;


    //Utility
    private Hashtable<Integer, Object> employeeObjectList = null;
    private Hashtable<Integer, Object> employeeIndexList = null;

    DefaultListModel listModel;

    private String WhereClause = null;
    private Hashtable<String,String> HastWhereClause = null;
    private boolean load = true;

    /** Logger */
    private static CLogger s_log = CLogger.getCLogger(VEmployeeListPanel.class);

    private boolean m_autoLoad = true;

    /**
     * This is the default constructor
     */
    public VEmployeeListPanel() {
        this(true);
    }

    public VEmployeeListPanel(boolean autoLoad) {
        super();
        this.m_autoLoad = autoLoad;
        initialize();
    }

    public void addListSelectionListener(ListSelectionListener listener) {
        listEmployee.addListSelectionListener(listener);
    }

    public void removeListSelectionListener(ListSelectionListener listener) {
        listEmployee.removeListSelectionListener(listener);
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        HastWhereClause = new Hashtable<String,String>();
        employeeObjectList = new Hashtable<Integer,Object>();
        employeeIndexList = new Hashtable<Integer,Object>();
        
        JScrollPane scrollPane = new JScrollPane(getListEmployee());
        scrollPane.setMinimumSize(new Dimension(180, 0));
        scrollPane.setPreferredSize(new Dimension(180, 300));
        scrollPane.setMaximumSize(new Dimension(180, Integer.MAX_VALUE));
        scrollPane.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        
        //setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setLayout( new BorderLayout() );
        add(scrollPane, BorderLayout.CENTER);
        add(getStatusPanel(), BorderLayout.SOUTH);

        listModel = new DefaultListModel();
        listEmployee.setModel(listModel);

        //Load the default Employees

        load = true;

        rdbActive.setSelected(true);

        if (m_autoLoad)
            LoadEmployees();

        load = false;
    }

    /**
     * This method initializes jList
     * 
     * @return javax.swing.JList
     */
    private ListSelectionListener SelectionListener = new ListSelectionListener()
	{
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting() == false && listEmployee.getSelectedIndex() != -1) {
                listEmployee.ensureIndexIsVisible(listEmployee.getSelectedIndex());
            }
        }
    }; 
    
    private JList getListEmployee() {
        if (listEmployee == null) {
            listEmployee = new JList();
            listEmployee.setBorder(BorderFactory.createEtchedBorder());
            listEmployee.addListSelectionListener( SelectionListener );
            listEmployee.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
        return listEmployee;
    }

    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private CPanel getStatusPanel() {
        if (statusPanel == null) {
            lblInactif = new JLabel();
            lblActif = new JLabel();
            statusGroup = new ButtonGroup();
            statusPanel = new CPanel();
            statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
            statusPanel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
            statusPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Msg.translate(Env.getCtx(), "Filter")), BorderFactory.createEmptyBorder(0, 5, 5, 5)));

            lblActif.setText(Msg.translate(Env.getCtx(), "Active"));
            lblInactif.setText(Msg.translate(Env.getCtx(), "Inactive"));

            statusGroup.add(getRdbActive());
            statusGroup.add(getRdbInactive());

            Box statusBox = Box.createHorizontalBox();
            statusBox.add(getRdbActive(), null);
            statusBox.add(lblActif, null);
            statusBox.add(Box.createHorizontalStrut(5));
            statusBox.add(getRdbInactive(), null);
            statusBox.add(lblInactif, null);
            statusBox.add(Box.createGlue());

            
            statusPanel.add(statusBox);

//            statusPanel.add( getCbCalendar());

        }
        return statusPanel;
    }
    
    public void addFilter( JComponent filter ) {
        statusPanel.add(filter);
    }

    public void SelectEmployee(int employeeNumber) {
        int index;
        if (employeeIndexList.containsKey(new Integer(employeeNumber))) {
            index = ((Integer) employeeIndexList.get(new Integer(employeeNumber))).intValue();
            //Set the employee and simulate a selection in order to refresh the detail
            listEmployee.setSelectedIndex(index);

        } else
            return;
    }

    /**
     * This method initializes jRadioButton
     * 
     * @return javax.swi rdbActive.setBounds(7, 18, 21, 21);
     */
    private JRadioButton getRdbActive() {
        if (rdbActive == null) {
            rdbActive = new JRadioButton();
            rdbActive.setBounds(6, 18, 21, 21);
            rdbActive.setText("");
            rdbActive.setActionCommand("Active");
            rdbActive.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (!load) LoadEmployees();
                }
            });

        }
        return rdbActive;
    }

    private VLookup getCbCalendar()
    {
    	if ( cbCalendar == null )
    	{
        	MLookup calendarL = MLookupFactory.get (Env.getCtx(), 0, 0, 2111015, DisplayType.Table);
        	cbCalendar = new VLookup ("P_Calendar_ID", false, false, true, calendarL);
        	cbCalendar.setBounds(206, 20, 210, 19);
        	cbCalendar.addActionListener( new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    if (!load)
                    {
                    	LoadEmployees();
                        selectFirst();
                    }
                }
            } );
    	}

    	return cbCalendar; 
    }

    /**
     * This method initializes jRadioButton
     * 
     * @return javax.swing.JRa rdbInactive.setBounds(85, 18, 21, 21);
     */
    private JRadioButton getRdbInactive() {
        if (rdbInactive == null) {
            rdbInactive = new JRadioButton();
            rdbInactive.setBounds(71, 18, 21, 21);
            rdbInactive.setActionCommand("Inactive");
            rdbInactive.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (!load) LoadEmployees();
                }
            });

        }
        return rdbInactive;
    }

    /**
     * We take for granted this method is called when a item is selected
     * 
     * @return the selected employee ID
     */
    public int getSelectedEmployeeID() {

        KeyNamePair pair = null;
        if (employeeObjectList == null) {
            return -1;
        }
        //Get the selected item Index from the list
        int index = listEmployee.getSelectedIndex();
        //No item selected
        if (index == -1 || index > employeeObjectList.size()) {
            return -1;
        }

        pair = (KeyNamePair) employeeObjectList.get(new Integer(index));
        if (pair == null)
            return -1;

        return Integer.parseInt(pair.getID());
    }

    public void setWhereClause(String FindQuery) {
        WhereClause = new String("");

        if (FindQuery != null)
            if (FindQuery.length() != 0)
                WhereClause = " and " + FindQuery;
        LoadEmployees();
    }

    public void setWhereClause(String column, String valueKey) {

//        System.out.println("SetWhereClause  valueKey " + valueKey);

        // ici enlever la condition faire avec un hasttable.
        if (valueKey.compareTo("0") == 0) {
            HastWhereClause.remove(column);
        } else {
            if (HastWhereClause.containsKey(column)) {
                HastWhereClause.remove(column);
            }
            HastWhereClause.put(column, valueKey);
        }

        WhereClause = new String("");

        Iterator it = HastWhereClause.keySet().iterator();
        Iterator it2 = HastWhereClause.values().iterator();
        while (it.hasNext()) {
            //	  	 Get key
            Object key = it.next();
            Object value = it2.next();
            WhereClause += " and " + key + " = " + value;
        }
//        System.out.println("employeeListPanel load list " + WhereClause);
        LoadEmployees();
    };

    public int getCalendarID()
    {
    	if ( cbCalendar == null || cbCalendar.getValue() == null )
    		return 0;
    	
        int P_Calendar_ID =  Integer.parseInt( cbCalendar.getValue().toString() );
        return P_Calendar_ID;
    }

    /**
     * Load the active employees in the Employee View List
     *  
     */
    public void LoadEmployees() {

        String sql = "Select P_Employee.P_EMPLOYEE_ID, P_Employee.Value, P_Employee.Name FROM P_Employee ";

		sql = MRole.getDefault().addAccessSQL (sql, "P_Employee", true, false);	// fully qualidfied - RO 

//		MUser User = MUser.get( Env.getCtx());
	
		String org_id = "@#AD_Org_ID@";
		org_id = Env.parseContext(Env.getCtx(), 0, org_id, false);
		
		if ( ! org_id.equals("0"))
		{
			sql = sql + " AND P_Employee.AD_ORG_ID = @#AD_Org_ID@ ";
			sql = Env.parseContext(Env.getCtx(), 0, sql, false);
			
		}
/*
		
		if ( User.isDistributionBookletRestricted() )
		{
			sql += " AND P_Employee.P_Distribution_Booklet_ID in ( " 
			    + " select p_distribution_booklet.p_distribution_booklet_id "  
			    + " from p_distribution_booklet, p_employee " 
			    + " where (p_distribution_booklet.p_employee_id = p_employee.p_employee_id " 
			    + "  or p_distribution_booklet.p_employee_gestionnaire_id = p_employee.p_employee_id  " 
			    + "  or p_distribution_booklet.p_employee_Coordinating_id = p_employee.p_employee_id ) " 
			    + " and  p_employee.ad_user_id = " + User.getAD_User_ID() 
			    + " )" ;
		}
*/		
		
		//+2012.01.18 
		if ( WhereClause == null || WhereClause.contains("P_Employee_ID") == false )
		{
		//-2012.01.18 
	        if (rdbActive.isSelected()) {
	            sql += " AND P_Employee.IsActive = 'Y' ";
	        } else if (rdbInactive.isSelected()) {
	            sql += " AND P_Employee.IsActive = 'N' ";
	        } else {
	            sql += " AND P_Employee.IsActive IN ( 'Y','N' ) ";
	        }
		}

        int P_Calendar_ID = getCalendarID();
        if ( P_Calendar_ID != 0 )
        	sql += " AND P_Employee.P_Payment_Group_ID in ( Select P_Payment_Group_ID from P_Payment_Group Where P_Calendar_ID = " + P_Calendar_ID + ")"; 
        	
        if (WhereClause != null) {
            sql = sql + WhereClause;
        }

        sql += " Order by P_Employee.Name ";

        s_log.log(Level.INFO, "VEmployeeListPanel.LoadEmployees : " + sql);
        
//        System.out.println(" loadEmployee " + sql);

        try {
            KeyNamePair idNamePair = null;
            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            // pstmt.setString(0,"Y");
            ResultSet rs = pstmt.executeQuery();
            int index = 0;
            //Flush anything in the list
            removeListSelectionListener( SelectionListener );
            listModel.clear();
            employeeObjectList.clear();
            while (rs.next()) {
                idNamePair = new KeyNamePair(rs.getInt(1), rs.getString(3));

                listModel.addElement(idNamePair);
                employeeObjectList.put(new Integer(index), (Object) idNamePair);
                employeeIndexList.put(new Integer(rs.getInt(1)), new Integer(index));
                index = index + 1;
            }
            rs.close();
            pstmt.close();
            addListSelectionListener( SelectionListener );

            //	   		  if ( ! load )
            //                 listEmployee.setSelectedIndex( 0 );

        } catch (Exception e) {
            s_log.log(Level.SEVERE, "VEmployeeListPanel.LoadEmployees", e);
        }

    }

    public void selectFirst() {
    	if ( listEmployee == null) return;
        if( listEmployee.getModel().getSize() <= 0 ) return;
        if ( listEmployee.getVisibleRowCount() <= 0 ) return;
        listEmployee.setSelectedIndex(0);
    }
    
    public void selectLast() {
        if( listEmployee.getModel().getSize() <= 0 ) return;
        if( listEmployee.getModel().getSize() == 1 ) listEmployee.setSelectedIndex(0);
        listEmployee.setSelectedIndex(listEmployee.getModel().getSize()-1);
    }
    
    public void selectNext() {
        if( listEmployee.getModel().getSize() <= 0 ) return;
        if( listEmployee.getModel().getSize() == 1 ) listEmployee.setSelectedIndex(0);
        int sel = listEmployee.getSelectedIndex();
        if( sel < listEmployee.getModel().getSize()-1 )
            listEmployee.setSelectedIndex(sel+1);
    }
    
    public void selectPrev() {
        if( listEmployee.getModel().getSize() <= 0 ) return;
        int sel = listEmployee.getSelectedIndex();
        if( sel > 0 )
            listEmployee.setSelectedIndex(sel-1);
    }
    
/*    public void setSelectedIndex(int index) {
        listEmployee.setSelectedIndex(index);
    }
*/
    public int getItemPosition() {
        return listEmployee.getSelectedIndex()+1;
    }
    
    public int getItemCount() {
        return listEmployee.getModel().getSize();
    }

} //  @jve:decl-index=0:visual-constraint="10,10"
