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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;

import org.compiere.apps.AChat;
import org.compiere.apps.AEnv;
import org.compiere.apps.search.Find;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VNumber;
import org.compiere.model.GridField;
import org.compiere.model.MQuery;
import org.compiere.model.MTab;
import org.compiere.model.MTable;
import org.compiere.model.MWindow;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CButton;
import org.compiere.swing.CCheckBox;
import org.compiere.swing.CComboBox;
import org.compiere.swing.CDialog;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.solstice.apps.form.VEmployeeCalendarUtil.CalendarGridModel;
import org.solstice.apps.form.VEmployeeCalendarUtil.CalendarModel;
import org.solstice.apps.form.VTimeSheetModel.DETableRenderer;
import org.solstice.apps.form.VTimeSheetModel.Lookup.LookupGain;

import solstice.model.P_Employee;
import solstice.model.P_Employee_Calendar;
import solstice.model.P_Period;

/**
 * Create Charge from Accounts
 * 
 * @author Mathieu Dufour
 * @version $Id: VEmployeeCalendar.java,v 1.2 2007/07/19 15:09:35 marmor01 Exp $
 */
public class VEmployeeCalendar extends CustomWindow {
    public int P_EMPLOYEE_AD_Window_ID; 
    public int P_EMPLOYEE_AD_Tab_ID;  
    public int P_EMPLOYEE_AD_Table_ID;
    public int P_EMPLOYEE_CALENDAR_AD_Table_ID;

	private final String MOIS[] = { 
	    "January", "February", "March", "April", "May", "June", 
	    "July", "August", "September", "October", "November", "December" 
    };
	
    private VEmployeeListPanel m_employeePanel;

    private P_Employee m_employee = null;
    
    private JTable m_table;
    private JButton m_btnMaintenant;
    private Calendar m_maintenant = Calendar.getInstance();

    private CComboBox cbGain = null;
    private LookupGain m_lookup;

    private JTable m_gridTable;

    /**
     * Constructor
     */
    public VEmployeeCalendar() {
        super();
        
        m_lookup = new LookupGain() {
            protected void load() {
                if ( DB.isMSSQLServer())
                {
                    genericLoadFromSQL(
                    		"SELECT -1 as P_Gain_ID, '' as Name, 'Y' as IsAbsence, '' as Value UNION ALL " +
                            "SELECT P_Gain.P_Gain_ID, P_Gain.Value + '-' + P_Gain_TRL.Name , IsAbsence, P_Gain.value " +
                            "FROM P_Gain  " +
                            " INNER JOIN P_Gain_TRL on P_Gain_Trl.P_Gain_ID = P_Gain.P_Gain_ID AND P_Gain_Trl.AD_Language = 'fr_CA'" +
                            "WHERE P_Gain.IsActive = 'Y' ORDER BY IsAbsence DESC, value " );  // AND IsAbsence = 'Y'
                	
                }
                else
                {
                    genericLoadFromSQL(
                    		"SELECT -1 as P_Gain_ID, '' as Name, 'Y' as IsAbsence, '' as Value UNION ALL " +
                            "SELECT P_Gain.P_Gain_ID, P_Gain.Value || '-' || P_Gain_TRL.Name , IsAbsence, P_Gain.value  " +
                            "FROM P_Gain  " +
                            " INNER JOIN P_Gain_TRL on P_Gain_Trl.P_Gain_ID = P_Gain.P_Gain_ID AND P_Gain_Trl.AD_Language = 'fr_CA'" +
                            "WHERE P_Gain.IsActive = 'Y' ORDER BY IsAbsence DESC, value " );  // AND IsAbsence = 'Y'

                }
                	
            }
        };
                
        m_gridTable = new JTable();
        
        CalendarGridModel model = new CalendarGridModel( new Vector(), m_lookup );
	    model.addTableModelListener( new TableModelListener() { 
            public void tableChanged(TableModelEvent e) {
                if( e.getType() == TableModelEvent.UPDATE ) enableSave( true ); 
            }
	    } );
        new DETableRenderer().bindTable(m_gridTable, model);

    } //  VEmployeeCalendar

    /** Logger */
    private static CLogger s_log = CLogger.getCLogger(VEmployeeCalendar.class);

    private CPanel m_container;
    private Box m_mainPanel;
    private JScrollPane m_gridPanel;
    public JComponent getMainPanel() {
        m_container = new CPanel( new BorderLayout(2,0) );
        try {
            m_employeePanel = new VEmployeeListPanel();
            m_employeePanel.addListSelectionListener( SelectionListener );
            CompiereColor.setBackground(this);
            

            Box box = Box.createHorizontalBox();
            
    	    m_mainPanel = Box.createVerticalBox();
            m_mainPanel.add( initMainBar() );
            m_mainPanel.add( Box.createVerticalStrut(5) );
            
            m_mainPanel.add( initNavBar() );
            m_mainPanel.add( Box.createVerticalStrut(5) );
            
            CalendarModel model = new CalendarModel(this);
    	    model.addTableModelListener( new TableModelListener() { 
                public void tableChanged(TableModelEvent e) {
                    if( e.getType() == TableModelEvent.UPDATE ) enableSave( true ); 
                }
    	    } );

            
            m_table = new JTable();
            model.bindTable( m_table, m_lookup );
            
            m_mainPanel.add( m_table.getTableHeader() );
            m_mainPanel.add( m_table );
            m_mainPanel.add( Box.createVerticalGlue() );
            
            Box lefty = Box.createVerticalBox();
            lefty.add( m_employeePanel );
            lefty.add( initLegende() );
            lefty.setPreferredSize( new Dimension(180, 300) );

            m_gridPanel = new JScrollPane(m_gridTable);
            m_gridPanel.setVisible( false );
            
            m_container.add(lefty, BorderLayout.WEST);
            Box middle = (Box)m_container.add( Box.createVerticalBox() );
            middle.add(m_mainPanel);
            middle.add(m_gridPanel);
            
            cbGain.setModel( m_lookup );
            cbGain.setSelectedIndex(0);
            
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
        	else s_log.log(Level.SEVERE,"VTimeSheet.init");
            
            rs.close();
            statm.close();

            
        } catch (Exception e) {
            s_log.log(Level.SEVERE, "VEmployeeCalendar.init", e);
        }
        
        // P_TIMESHEET constants
        try
        {
            Statement statm = DB.createStatement();
            ResultSet rs = statm.executeQuery(
            	"SELECT tbl.AD_Table_ID " + 
				"FROM AD_Table tbl " + 
				"WHERE tbl.tableName = 'P_Employee_Calendar'");
            
            if( rs.next() ) {
            	P_EMPLOYEE_CALENDAR_AD_Table_ID = rs.getInt(1); 
            }
        	else s_log.log(Level.SEVERE,"VTimeSheet.init");
            
            rs.close();
            statm.close();
        }
        catch (Exception e)
        {
        	s_log.log(Level.SEVERE,"VTimeSheet.init", e);
        }


        m_employeePanel.selectFirst();
        
        return m_container;
    } //	init
    private void reloadInfo() {
    	
        m_employee = new P_Employee( Env.getCtx(), m_employeePanel.getSelectedEmployeeID(), null );
    	
        if( m_calendarMode ) {
	        TableCellEditor editor = m_table.getCellEditor();
	        if( editor != null ) editor.cancelCellEditing();
	        CalendarModel model = (CalendarModel)m_table.getModel();
	        model.reset();
	        model.loadData( m_employee.getP_Employee_ID(), m_maintenant );
        }
        else {
	        TableCellEditor editor = m_table.getCellEditor();
	        if( editor != null ) editor.cancelCellEditing();
	        CalendarGridModel model = (CalendarGridModel)m_gridTable.getModel();
	        model.load( m_employee.getP_Employee_ID(), m_maintenant.get(Calendar.YEAR) );
        }
        enableSave( false );
        
		int year =  m_maintenant.get(Calendar.YEAR);
	    int record_ID = P_Employee_Calendar.getEmployeeCalendarId(m_employee.getP_Employee_ID(), year);

        if (aChat != null)
        {
    		aChat.setPressed(super.hasChat(P_EMPLOYEE_CALENDAR_AD_Table_ID,record_ID));
        }

        //+ 2010.01.11
        if (aAttachment != null)
        {
        	aAttachment.setPressed(super.hasAttachment(P_EMPLOYEE_CALENDAR_AD_Table_ID, record_ID));
        }
    }
    
    private JTextField m_txtName;
    private JTextField initTxtName() {
        if( m_txtName == null ) {
            m_txtName = new JTextField();
	        Dimension taille = new Dimension( 200, 22 );
	        m_txtName.setPreferredSize( taille );
	        m_txtName.setMaximumSize( taille );
	        m_txtName.setEditable(false);
        }
        return m_txtName;
    }

    private Box initLegendeItem( String texte, Color couleur ) {
        Box ligne = Box.createHorizontalBox();
        ligne.setBorder( BorderFactory.createEmptyBorder(0, 2, 0, 2));

        ligne.add( new JLabel(texte) );
        
        ligne.add( Box.createHorizontalGlue() );
        
        JLabel bloc = new JLabel();
        Dimension taille = new Dimension(20, 10);
        bloc.setBorder( BorderFactory.createLineBorder(Color.BLACK) );
        bloc.setMinimumSize( taille );
        bloc.setPreferredSize( taille );
        bloc.setMaximumSize( taille );
        bloc.setBackground( couleur );
        bloc.setOpaque(true);
        ligne.add( bloc );
        return ligne;
    }
    
    private Box initLegende() {
        Box box = Box.createVerticalBox();

        box.add( initLegendeItem(Msg.translate(Env.getCtx(), "calTempsStandard"), CalendarModel.COLOR_STANDARD) );
        box.add( initLegendeItem(Msg.translate(Env.getCtx(), "calCongesFeries"), CalendarModel.COLOR_FERIE) );
        box.add( initLegendeItem(Msg.translate(Env.getCtx(), "calAutorisationAbsences"), CalendarModel.COLOR_AUTH_ABSENCE) );
        box.add( initLegendeItem(Msg.translate(Env.getCtx(), "calAutresAbsences"), CalendarModel.COLOR_AUTRE_ABSENCE) );
        box.add( initLegendeItem(Msg.translate(Env.getCtx(), "calMulticodes"), CalendarModel.COLOR_MULTI_CODE) );
        
        return box;
    }
    
    private void loadDate( Calendar date ) {
        TableCellEditor editor = m_table.getCellEditor();
        if( editor != null ) editor.cancelCellEditing();
        
        m_maintenant = date;
        m_btnMaintenant.setText( Msg.translate(Env.getCtx(), MOIS[date.get(Calendar.MONTH)]) + " " + date.get(Calendar.YEAR) );
        ((CalendarModel)m_table.getModel()).loadData( m_employee.getP_Employee_ID(), m_maintenant );
        reloadInfo();
    }
    
    private Box initNavBar() {
        Box navBar = Box.createHorizontalBox();
        
        JButton btn = new JButton("|<<");
        btn.setPreferredSize( new Dimension(50, 22) );
        btn.addActionListener( new ActionListener() { 
            public void actionPerformed(ActionEvent ev) { 
                m_maintenant.add( Calendar.YEAR, -1 );
                loadDate( m_maintenant );
            }
        } );
        navBar.add( btn );
        
        btn = new JButton("<");
        btn.setPreferredSize( new Dimension(50, 22) );
        btn.addActionListener( new ActionListener() { 
            public void actionPerformed(ActionEvent ev) { 
                m_maintenant.add( Calendar.MONTH, -1 );
                loadDate( m_maintenant );
            }
        } );
        navBar.add( btn );
        
        m_btnMaintenant = new JButton();
        m_btnMaintenant.setMinimumSize( new Dimension(160, 22) );
        m_btnMaintenant.setPreferredSize( new Dimension(160, 22) );
        m_btnMaintenant.setMaximumSize( new Dimension(160, 22) );
        m_btnMaintenant.addActionListener( new ActionListener() { 
            public void actionPerformed(ActionEvent ev) { 
                loadDate( Calendar.getInstance() );
            }
        } );
        navBar.add( m_btnMaintenant );
        m_btnMaintenant.setText( Msg.translate(Env.getCtx(), MOIS[m_maintenant.get(Calendar.MONTH)]) + " " + m_maintenant.get(Calendar.YEAR) );

        
        btn = new JButton(">");
        btn.setPreferredSize( new Dimension(50, 22) );
        btn.addActionListener( new ActionListener() { 
            public void actionPerformed(ActionEvent ev) { 
                m_maintenant.add( Calendar.MONTH, 1 );
                loadDate( m_maintenant );
            }
        } );
        navBar.add( btn );

        btn = new JButton(">>|");
        btn.setPreferredSize( new Dimension(50, 22) );
        btn.addActionListener( new ActionListener() { 
            public void actionPerformed(ActionEvent ev) { 
                m_maintenant.add( Calendar.YEAR, 1 );
                loadDate( m_maintenant );
            }
        } );
        navBar.add( btn );

        navBar.setAlignmentX( Box.CENTER_ALIGNMENT );
        
        return navBar;
    }
    
    private Box initMainBar() {
        Box box = Box.createHorizontalBox();
        
        box.add(new JLabel(Msg.translate(Env.getCtx(), "P_Employee_ID")));
        box.add( Box.createHorizontalStrut(5) );
        box.add(initTxtName());
        
        JButton btnOpenEmployee = new JButton("...");
        btnOpenEmployee.addActionListener(this);
        btnOpenEmployee.setActionCommand("open_employee");
        btnOpenEmployee.setPreferredSize(new Dimension(30, 23));
        btnOpenEmployee.setMaximumSize(new Dimension(30, 23));
        box.add(btnOpenEmployee);
        box.add( Box.createHorizontalStrut(5) );
        
        box.add(new JLabel(Msg.getMsg(Env.getCtx(), "Absence")));
        box.add( Box.createHorizontalStrut(5) );
        
        JComboBox cb = getCbGain();
        cb.setMaximumSize( new Dimension(Integer.MAX_VALUE, 22) );
        box.add( cb );
        
        CButton btn = new CButton(Msg.getMsg(Env.getCtx(),"Generate"));
        btn.setMaximumSize( new Dimension(Integer.MAX_VALUE, 22) );
        box.add( btn );
        btn.addActionListener( new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                final CComboBox m_gains = new CComboBox(cbGain.getModel()); 
                final VDate m_dateDebut = new VDate();
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis( m_maintenant.getTimeInMillis() );
                cal.set(Calendar.DAY_OF_MONTH, 1);
                m_dateDebut.setValue(new Timestamp(cal.getTimeInMillis()));
                final VDate m_dateFin = new VDate();
                cal.add(Calendar.MONTH, 1);
                cal.add(Calendar.DAY_OF_MONTH, -1);
                m_dateFin.setValue(new Timestamp(cal.getTimeInMillis()));
                final CCheckBox m_lundi = new CCheckBox();
                final CCheckBox m_mardi = new CCheckBox();
                final CCheckBox m_mercredi = new CCheckBox();
                final CCheckBox m_jeudi = new CCheckBox();
                final CCheckBox m_vendredi = new CCheckBox();
                final CCheckBox m_samedi = new CCheckBox();
                final CCheckBox m_dimanche = new CCheckBox();
                final CButton btnOk = new CButton(Msg.getMsg(Env.getCtx(),"OK"));
                final CButton btnCancel = new CButton(Msg.getMsg(Env.getCtx(),"Cancel"));
                final VNumber m_qte = new VNumber();
                final CCheckBox m_remove = new CCheckBox();
                
                final CDialog dlg = new CDialog( JOptionPane.getFrameForComponent(VEmployeeCalendar.this), "Génération calendrier", true) {
                    public void actionPerformed(ActionEvent e) {
                        super.actionPerformed(e);
                        if( btnOk.equals(e.getSource()) ) {
                            ((CalendarModel)m_table.getModel()).genererCalendrier( ((KeyNamePair)m_gains.getSelectedItem()).getKey(), 
                                m_dateDebut.getTimestamp(), m_dateFin.getTimestamp(),
                                m_lundi.isSelected(), m_mardi.isSelected(), m_mercredi.isSelected(),
                                m_jeudi.isSelected(), m_vendredi.isSelected(), m_samedi.isSelected(),
                                m_dimanche.isSelected(), (BigDecimal)m_qte.getValue(), m_remove.isSelected() );
                        }
                        dispose();
                    }
                };
                btnOk.addActionListener(dlg);
                btnCancel.addActionListener(dlg);
                
                CPanel panel = new CPanel( new GridLayout(13, 2, 10, 2) );
                panel.setBorder( BorderFactory.createEmptyBorder(3,3,3,3) );
                panel.add( new CLabel(Msg.getMsg(Env.getCtx(),"Absence")) );
                panel.add( m_gains );
                panel.add( new CLabel(Msg.getMsg(Env.getCtx(),"StartDate")) );
                panel.add( m_dateDebut );
                panel.add( new CLabel(Msg.getMsg(Env.getCtx(),"EndDate")) );
                panel.add( m_dateFin );
                panel.add( new CLabel(Msg.getMsg(Env.getCtx(),"Quantity")) );
                panel.add( m_qte );
                panel.add( new CLabel(Msg.getMsg(Env.getCtx(),"Monday")) );
                panel.add( m_lundi );
                panel.add( new CLabel(Msg.getMsg(Env.getCtx(),"Tuesday")) );
                panel.add( m_mardi );
                panel.add( new CLabel(Msg.getMsg(Env.getCtx(),"Wednesday")) );
                panel.add( m_mercredi );
                panel.add( new CLabel(Msg.getMsg(Env.getCtx(),"Thursday")) );
                panel.add( m_jeudi );
                panel.add( new CLabel(Msg.getMsg(Env.getCtx(),"Friday")) );
                panel.add( m_vendredi );
                panel.add( new CLabel(Msg.getMsg(Env.getCtx(),"Saturday")) );
                panel.add( m_samedi );
                panel.add( new CLabel(Msg.getMsg(Env.getCtx(),"Sunday")) );
                panel.add( m_dimanche );
                panel.add( new CLabel(Msg.getMsg(Env.getCtx(),"Delete")) );
                panel.add( m_remove );
                panel.add( btnOk );
                panel.add( btnCancel );
                
                dlg.getContentPane().add(panel);

                AEnv.showCenterWindow( JOptionPane.getFrameForComponent(VEmployeeCalendar.this), dlg );
            }
        } );

        return box;
    }

    private CComboBox getCbGain() {
        if (cbGain == null) {
            cbGain = new org.compiere.swing.CComboBox();
            cbGain.setPreferredSize( new Dimension(210, 22) );
            cbGain.setMaximumSize( new Dimension(Integer.MAX_VALUE, 22) );
            cbGain.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    KeyNamePair knp = (KeyNamePair)cbGain.getSelectedItem();
                    if ( knp != null)
                    	((CalendarModel)m_table.getModel()).setDefaultGainId(knp.getKey());
                }
            });
        }
        return cbGain;
    }
    
	/**
	 * Chat
	 */
  
	public void cmd_chat()
	{
		int year =  m_maintenant.get(Calendar.YEAR);
	    int calId = P_Employee_Calendar.getEmployeeCalendarId(m_employee.getP_Employee_ID(), year);

        // 2009.03.05 Ne pas permettre de créer un chat s'il n'y a pas de calendrier.
        if ( calId <= 0 )
              return;
        // 2009.03.05

		int record_ID = calId;
		int employeeID = m_employee.getP_Employee_ID();
//	    int periodID = headerPanel.getSelectedPeriodID();
		
		s_log.info("Record_ID=" + record_ID);
		if (record_ID == -1)	// No Key
		{
			aChat.setEnabled(false);
			return;
		}
		// Find display
//		P_Period Period = P_Period.get(Env.getCtx(), periodID, null);
        P_Employee Employee = P_Employee.get( Env.getCtx(), employeeID, null);
		
		String description = Employee.getName() ;
		
		new AChat (Env.getFrame(this), getWindowNo(),
		    super.getCM_ChatID(P_EMPLOYEE_CALENDAR_AD_Table_ID,record_ID),
		    P_EMPLOYEE_CALENDAR_AD_Table_ID, record_ID, 
			description, null);
		//P_TIMESHEET
		super.loadChats(P_EMPLOYEE_CALENDAR_AD_Table_ID);				// reload
		aChat.setPressed(super.hasChat(P_EMPLOYEE_CALENDAR_AD_Table_ID,record_ID));
	}	// chat
    
    protected void cmd_moveFirst() {
        cmd_save(false);
        m_employeePanel.selectFirst();
    }
    
    protected void cmd_movePrevious() {
	    cmd_save(false);
	    m_employeePanel.selectPrev();
	}
    
    protected void cmd_moveNext() {
        cmd_save(false);
        m_employeePanel.selectNext();
    }

    protected void cmd_moveLast() {
        cmd_save(false);
        m_employeePanel.selectLast();
    }

    public void cmd_new(boolean copy) {
        cmd_save(false);
        enableSave( false );
    } //  cmd_new

    /**
     * If required ask if you want to save and save it
     * 
     * @param manualCmd
     *            true if invoked manually (i.e. force)
     * @return true if saved
     */
    public boolean cmd_save(boolean manualCmd) {
        if( m_calendarMode ) {
	        TableCellEditor editor = m_table.getCellEditor();
	        if( editor != null ) editor.stopCellEditing();
	        ((CalendarModel)m_table.getModel()).save();
        }
        else {
	        TableCellEditor editor = m_gridTable.getCellEditor();
	        if( editor != null ) editor.stopCellEditing();
            ((CalendarGridModel)m_gridTable.getModel()).save();
        }
        cmd_refresh();
        enableSave( false ); 
        return true;
    } //  cmd_save

    /**
     * Refresh
     */
    public void cmd_refresh() {
        reloadInfo();
        enableSave( false ); 
    } //  cmd_refresh

    /**
     * Zoom Across Menu
     */
    private void cmd_zoomAcross(Object source) {
    	int record_ID = m_employee.getP_Employee_ID(); 
		if (record_ID <= 0) return;

		MQuery query = new MQuery();
		query.addRestriction("P_Employee_ID", MQuery.EQUAL, new Integer(record_ID));
		doZoomAcross( "P_Employee", query );
    } //	cmd_zoom

    /**
     * Find - Set Query
     */
    private static final int AD_Window_ID = 2100304; // Employée
    private static final int AD_Tab_ID = 2100615; // Employée
    private static final int AD_Table_ID = 2100659;

    private ListSelectionListener SelectionListener = new ListSelectionListener() 
    {
		public void valueChanged(ListSelectionEvent ev) { if( ev.getValueIsAdjusting() == false ) reloadInfo(); }
    };

    public void cmd_find() {
    	m_employeePanel.removeListSelectionListener(SelectionListener );

    	System.out.println(" VTimeSheet Find ");
        
        MWindow win = new MWindow(Env.getCtx(), AD_Window_ID, null);
        MTable table = new MTable(Env.getCtx(), AD_Table_ID, null);
        MTab tab = new MTab(Env.getCtx(), AD_Tab_ID, null);

        cmd_save(false);
        //
        GridField[] findFields = GridField.createFields(Env.getCtx(), getWindowNo(), 0, AD_Tab_ID);

        Find find = new Find(Env.getFrame(this), getWindowNo(), tab.getName(), tab.getAD_Tab_ID(), tab.getAD_Table_ID(), table.getTableName(), tab.getWhereClause(), findFields, 1);
        
        MQuery query = find.getQuery();
        if( query != null ) {
            System.out.println(" VTimeSheet Find Query " + query.getWhereClause());
            m_employeePanel.setWhereClause(query.getWhereClause());
        }
        m_employeePanel.addListSelectionListener(SelectionListener );
        m_employeePanel.selectFirst();

        
    } //	cmd_find

    public void cmd_zoomAcross()
    {
    	int record_ID = m_employeePanel.getSelectedEmployeeID(); 
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

    /*
     * 		    "SELECT E.P_EMPLOYEE_NONBUSINESSDAY_ID, E.NONBUSINESSDATE, E.P_GAIN_ID, E.DAYQTY, " +
		    	    "CASE WHEN EXISTS( SELECT 1 FROM P_Absence_Detail ad WHERE SOURCE = 'C' AND ad.SOURCE_ID = E.P_EMPLOYEE_NONBUSINESSDAY_ID ) THEN 1 ELSE 0 END AUTH " +
			"FROM P_EMPLOYEE_NONBUSINESSDAY E " +
			"WHERE P_EMPLOYEE_CALENDAR_ID = ? " +
			"AND NONBUSINESSDATE BETWEEN ? AND ? ";

     */
    
    private boolean m_calendarMode = true;
    protected void cmd_multi() {
        cmd_save(false);
        m_calendarMode = !m_calendarMode;
        m_gridPanel.setVisible( !m_calendarMode );
        m_mainPanel.setVisible( m_calendarMode );
        reloadInfo();
    }
    
} //  VEmployeeCalendar

