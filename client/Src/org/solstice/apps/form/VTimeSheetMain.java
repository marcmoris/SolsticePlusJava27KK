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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Locale;
import java.util.logging.Level;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.solstice.apps.form.VTimeSheetModel.CreditsModel;



import org.compiere.apps.ADialog;
//import org.compiere.apps.search.Find;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.model.DataStatusEvent;
import org.compiere.model.GridField;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MPeriod;
import org.compiere.model.MQuery;
import org.compiere.model.MRegion;
import org.compiere.model.MRole;
import org.compiere.model.MTab;
import org.compiere.model.MTable;
import org.compiere.model.MOrg;
import org.compiere.model.MActivity;

import org.compiere.model.Query;
import org.compiere.print.ReportCtl;
import org.compiere.print.ReportEngine;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Ini;
import org.compiere.util.Msg;

import solstice.model.P_Collective_Labour_Agr;
import solstice.model.P_Distribution_Booklet;
import solstice.model.P_Job_Title;
import solstice.model.P_Job_Type;
import solstice.model.P_Payment;
import solstice.model.P_Time_Sheet;
import solstice.model.P_Period;
import solstice.model.P_Employee;
import solstice.model.P_Payment_Group;
import org.compiere.apps.AChat;
import org.compiere.apps.Attachment;
import org.compiere.grid.ed.VLookup;
import org.solstice.apps.form.VEmployeeCalendarUtil.DayData;
import org.solstice.apps.form.VEmployeeCalendarUtil.DayDialog;
import org.solstice.apps.form.CustomWindowFind;
/**
 * @author frabou01
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class VTimeSheetMain extends CustomWindow// implements ListSelectionListener
{
    public int P_EMPLOYEE_AD_Window_ID; 
    public int P_EMPLOYEE_AD_Tab_ID;  
    public int P_EMPLOYEE_AD_Table_ID;
    public int P_TIMESHEET_AD_Table_ID;
    public int P_TIME_SHEET_AD_Window_ID = 2100439; 

    private static volatile boolean s_constanceLoaded = false;
    private static int s_P_EMPLOYEE_AD_Window_ID = 0;
    private static int s_P_EMPLOYEE_AD_Tab_ID = 0;
    private static int s_P_EMPLOYEE_AD_Table_ID = 0;
    private static int s_P_TIMESHEET_AD_Table_ID = 0;

    private final AtomicLong m_reloadToken = new AtomicLong(0);
    private static final ExecutorService s_bgExecutor = Executors.newVirtualThreadPerTaskExecutor();


    public int AD_Form_ID = 2100001;

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (MPeriod.class);

    /** Default panel */
    VEmployeeListPanel employeeListPanel = null;
    VTimeSheetHeader headerPanel = null;
    VTimeSheetCredits creditsPanel = null;
    VTimeSheetDetail detailPanel = null;
    
    private boolean m_readOnly = false;
    public boolean isReadOnly() { return m_readOnly; }
    public void setReadOnly( boolean readOnly ) { m_readOnly = readOnly; }
    
    private ListSelectionListener SelectionListener = new ListSelectionListener() 
    {
		public void valueChanged(ListSelectionEvent ev) { if( ev.getValueIsAdjusting() == false ) reloadInfo(); }
    };
    
	private int m_curWindowNo;

	public int getWindowNo() { return m_curWindowNo; }

	private void getConstance()
	{
		if (s_constanceLoaded)
		{
			P_EMPLOYEE_AD_Window_ID = s_P_EMPLOYEE_AD_Window_ID;
			P_EMPLOYEE_AD_Tab_ID = s_P_EMPLOYEE_AD_Tab_ID;
			P_EMPLOYEE_AD_Table_ID = s_P_EMPLOYEE_AD_Table_ID;
			P_TIMESHEET_AD_Table_ID = s_P_TIMESHEET_AD_Table_ID;
			return;
		}

        try
        {
            Statement statm = DB.createStatement();
            ResultSet rs = statm.executeQuery(
            	"SELECT tab.AD_Window_ID, tab.AD_Tab_ID, tbl.AD_Table_ID " + 
				"FROM AD_Tab tab, AD_Table tbl " + 
				"WHERE tab.AD_Table_ID = tbl.AD_Table_ID " +
				"AND tbl.Name = 'P_Employee' AND tab.Name = 'Employee'");
            
            if( rs.next() ) {
                s_P_EMPLOYEE_AD_Window_ID = rs.getInt(1); 
                s_P_EMPLOYEE_AD_Tab_ID = rs.getInt(2);
                s_P_EMPLOYEE_AD_Table_ID = rs.getInt(3);
                P_EMPLOYEE_AD_Window_ID = s_P_EMPLOYEE_AD_Window_ID; 
                P_EMPLOYEE_AD_Tab_ID = s_P_EMPLOYEE_AD_Tab_ID;
                P_EMPLOYEE_AD_Table_ID = s_P_EMPLOYEE_AD_Table_ID;
            }
        	else s_log.log(Level.SEVERE,"VTimeSheet.init");
            
            rs.close();
            statm.close();
        }
        catch (Exception e)
        {
        	s_log.log(Level.SEVERE,"VTimeSheet.init", e);
        }


        // P_TIMESHEET constants
        try
        {
            Statement statm = DB.createStatement();
            ResultSet rs = statm.executeQuery(
            	"SELECT tbl.AD_Table_ID " + 
				"FROM AD_Table tbl " + 
				"WHERE tbl.tableName = 'P_Time_Sheet'");
            
            if( rs.next() ) {
            	s_P_TIMESHEET_AD_Table_ID = rs.getInt(1); 
            	P_TIMESHEET_AD_Table_ID = s_P_TIMESHEET_AD_Table_ID;
            }
        	else s_log.log(Level.SEVERE,"VTimeSheet.init");
            
            rs.close();
            statm.close();
        }
        catch (Exception e)
        {
        	s_log.log(Level.SEVERE,"VTimeSheet.init", e);
        }
		s_constanceLoaded = true;
	}
    public JComponent getMainPanel() {

//    	getConstance( "P_Employee",2100304 );
    	
    	getConstance();
    	
    	MRole role = MRole.getDefault(Env.getCtx(), true);
        setReadOnly( !role.getFormAccess(AD_Form_ID).booleanValue() );
        
        CPanel mainPanel = new CPanel();
        
//        CompiereColor.setBackground(mainPanel);

        employeeListPanel = new VEmployeeListPanel(false);
        
        employeeListPanel.setMinimumSize( new Dimension(150,200) );
        employeeListPanel.setPreferredSize( new Dimension(150,200) );
        employeeListPanel.setMaximumSize( new Dimension(150,200) );
        employeeListPanel.addListSelectionListener( SelectionListener );
        final JComboBox cbFilter = new JComboBox();
        
        
/*
        cbFilter.addItem( "Toutes" );
        cbFilter.addItem( "ApprouvÃ©e" );
        cbFilter.addItem( "CalculÃ©es" );
        cbFilter.addItem( "Ãmises" );
        cbFilter.addItem( "En erreur" );
        cbFilter.addItem( "Initiales" );
        cbFilter.addItem( "TransfÃ©rÃ©es" );
        cbFilter.addItem( "ValidÃ©es" );
        cbFilter.addItem( "AnnulÃ©es" );
*/     
        cbFilter.addItem( Msg.translate(Env.getCtx(), "All TimeSheet") );
        cbFilter.addItem( Msg.translate(Env.getCtx(), "Approved") );
        cbFilter.addItem( Msg.translate(Env.getCtx(), "Calculated") );
        cbFilter.addItem( Msg.translate(Env.getCtx(), "Issued") );
        cbFilter.addItem( Msg.translate(Env.getCtx(), "In Error") );
        cbFilter.addItem( Msg.translate(Env.getCtx(), "Initial") );
        cbFilter.addItem( Msg.translate(Env.getCtx(), "Transferred") );
        cbFilter.addItem( Msg.translate(Env.getCtx(), "Validated") );
        cbFilter.addItem( Msg.translate(Env.getCtx(), "Cancelled") );
        cbFilter.addItem( "----------------------" );
        cbFilter.addItem( Msg.translate(Env.getCtx(), "Regular") );
        cbFilter.addItem( Msg.translate(Env.getCtx(), "Complementary") );
        cbFilter.addItem( Msg.translate(Env.getCtx(), "ExpenseAccount") );
        cbFilter.addItem( Msg.translate(Env.getCtx(), "HolidayPayment") );
        cbFilter.addItem( Msg.translate(Env.getCtx(), "Separation") );
        cbFilter.addItem( Msg.translate(Env.getCtx(), "Adjustement") );
        cbFilter.addItem( Msg.translate(Env.getCtx(), "Advance") );
        cbFilter.addItem( "----------------------" );
        cbFilter.addItem( Msg.translate(Env.getCtx(), "Check") );
        cbFilter.addItem( Msg.translate(Env.getCtx(), "Deposit") );
        
        cbFilter.addItemListener( new ItemListener() { 
            public void itemStateChanged(ItemEvent e) {
                if( e.getStateChange() == ItemEvent.SELECTED ) {
                    String status = null;
                    String type = null;
                    String paymentType = null;
                    switch( cbFilter.getSelectedIndex() ) {
                    	case 1: status = P_Time_Sheet.TIMESHEETSTATUS_Approved; break;
                    	case 2: status = P_Time_Sheet.TIMESHEETSTATUS_Calculated; break;
                    	case 3: status = P_Time_Sheet.TIMESHEETSTATUS_Issued; break;
                    	case 4: status = P_Time_Sheet.TIMESHEETSTATUS_Error; break;
                    	case 5: status = P_Time_Sheet.TIMESHEETSTATUS_Initial; break;
                    	case 6: status = P_Time_Sheet.TIMESHEETSTATUS_Transferred; break;
                    	case 7: status = P_Time_Sheet.TIMESHEETSTATUS_Validated; break;
                    	case 8: status = P_Time_Sheet.TIMESHEETSTATUS_Cancelled; break;
                    	case 10: type  = P_Time_Sheet.SHEETTYPE_Regular; break;
                    	case 11: type  = P_Time_Sheet.SHEETTYPE_Complementary; break;
                    	case 12: type  = P_Time_Sheet.SHEETTYPE_ExpenseAccount; break;
                    	case 13: type  = P_Time_Sheet.SHEETTYPE_HolidayPayment; break;
                    	case 14: type  = P_Time_Sheet.SHEETTYPE_SeparationPay; break;
                    	
                    	case 15: type  = P_Time_Sheet.SHEETTYPE_Adjustment; break;
                    	case 16: type  = P_Time_Sheet.SHEETTYPE_Advance; break;
                    	case 18: paymentType  = P_Time_Sheet.PAYMENTTYPE_Cheque; break;
                    	case 19: paymentType  = P_Time_Sheet.PAYMENTTYPE_Deposit; break;
                    }
                    
                    employeeListPanel.removeListSelectionListener(SelectionListener );

                    if( status != null ) {
	                    employeeListPanel.setWhereClause( 
	                        " EXISTS( SELECT 1 FROM P_Time_Sheet ts WHERE ts.P_Employee_ID = P_Employee.P_Employee_ID " +
	                        "AND TIMESHEETSTATUS = '"+ status +"' " +
	                        "AND P_PERIOD_ID = " + headerPanel.getSelectedPeriodID() + " ) " );
                    }
                    else if( type != null ) 
                    {
	                    employeeListPanel.setWhereClause( 
		                        " EXISTS( SELECT 1 FROM P_Time_Sheet ts WHERE ts.P_Employee_ID = P_Employee.P_Employee_ID " +
		                        "AND SHEETTYPE = '"+ type +"' " +
		                        "AND P_PERIOD_ID = " + headerPanel.getSelectedPeriodID() + " ) " );
                    }
                    else if( paymentType != null ) 
                    {
	                    employeeListPanel.setWhereClause( 
		                        " EXISTS( SELECT 1 FROM P_Time_Sheet ts WHERE ts.P_Employee_ID = P_Employee.P_Employee_ID " +
		                        "AND PAYMENTTYPE = '"+ paymentType +"' " +
		                        "AND P_PERIOD_ID = " + headerPanel.getSelectedPeriodID() + " ) " );
                    }
                    else employeeListPanel.setWhereClause( null );

 //                   employeeListPanel.LoadEmployees();
                    employeeListPanel.addListSelectionListener( SelectionListener );
                    employeeListPanel.selectFirst();
                    reloadInfo();
                }
            }
        } );
        employeeListPanel.addFilter( cbFilter );



        headerPanel = new VTimeSheetHeader(this);

//        cmd_find();

        creditsPanel = new VTimeSheetCredits(headerPanel);
        detailPanel = new VTimeSheetDetail(headerPanel);
        cmd_find();

        mainPanel.setLayout( new GridBagLayout() );
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(employeeListPanel, gbc);
        
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        gbc.weightx = 100.0;

        Box headBox = Box.createVerticalBox();
        headBox.add(headerPanel);
        headBox.add(creditsPanel);
        mainPanel.add(headBox, gbc);
       
        gbc.weightx = 1.0;
        gbc.weighty = 100.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(detailPanel, gbc);

        mainPanel.setPreferredSize( new Dimension(980, 580) );

        // selectFirst() is already invoked within cmd_find() above

        return mainPanel;
    }


	/**
	 *	Attachment
	 */
	public void cmd_attachment()
	{
        if ( headerPanel.getCurrentTimeSheetID() <= 0 )
              return;

		int record_ID = headerPanel.getCurrentTimeSheetID();
		s_log.info("Record_ID=" + record_ID);
		if (record_ID == -1)	//	No Key
		{
			aAttachment.setEnabled(false);
			return;
		}

	//	Attachment va = 
		new Attachment (Env.getFrame(this), getWindowNo(),
				super.getAD_AttachmentID(P_TIMESHEET_AD_Table_ID,record_ID), P_TIMESHEET_AD_Table_ID, record_ID, null);
		//
		super.loadAttachments( P_TIMESHEET_AD_Table_ID);				//	reload
		aAttachment.setPressed(super.hasAttachment(P_TIMESHEET_AD_Table_ID,record_ID));
	}	//	attachment

	/**
	 * Chat
	 */
  
	public void cmd_chat()
	{
        // 2009.03.05 Ne pas permettre de crÃ©er un chat s'il n'y a pas de feuille de temps
        if ( headerPanel.getCurrentTimeSheetID() <= 0 )
              return;
        // 2009.03.05

		int record_ID = headerPanel.getCurrentTimeSheetID();
		int employeeID = employeeListPanel.getSelectedEmployeeID();
	    int periodID = headerPanel.getSelectedPeriodID();
		
		s_log.info("Record_ID=" + record_ID);
		if (record_ID == -1)	// No Key
		{
			aChat.setEnabled(false);
			return;
		}
		// Find display
		P_Period Period = P_Period.get(Env.getCtx(), periodID, null);
        P_Employee Employee = P_Employee.get( Env.getCtx(), employeeID, null);
		
		String description = Period.getName() + " : "  + Employee.getName() ;
		
		new AChat (Env.getFrame(this), getWindowNo(),
		    super.getCM_ChatID(P_TIMESHEET_AD_Table_ID,record_ID),
		    P_TIMESHEET_AD_Table_ID, record_ID, 
			description, null);
		//P_TIMESHEET
		super.loadChats(P_TIMESHEET_AD_Table_ID);				// reload
		aChat.setPressed(super.hasChat(P_TIMESHEET_AD_Table_ID,record_ID));
	}	// chat

	
    protected void cmd_moveFirst() {
        cmd_save(false);
        employeeListPanel.selectFirst();
    }
    
    protected void cmd_movePrevious() {
	    cmd_save(false);
	    employeeListPanel.selectPrev();
	}
    
    protected void cmd_moveNext() {
        cmd_save(false);
        employeeListPanel.selectNext();
    }

    protected void cmd_moveLast() {
        cmd_save(false);
        employeeListPanel.selectLast();
    }

    /**
     * Print Checks and/or Remittance
     */
    protected void cmd_print()
    {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        boolean somethingPrinted = false;
        boolean directPrint = !Ini.isPropertyBool(Ini.P_PRINTPREVIEW);
        boolean ok = true;
        if (!somethingPrinted && ok)
            somethingPrinted = true;

        //	Confirm Print and Update
        if (ADialog.ask(getWindowNo(), this, "VPayPrintPrintRemittance"))
        {
            ReportCtl.startDocumentPrint(ReportEngine.REMITTANCE, 0, directPrint);
        } //	Print

        this.setCursor(Cursor.getDefaultCursor());
        dispose();
    } //  cmd_print

    /*public void valueChanged(ListSelectionEvent e)
    {
        if (e.getValueIsAdjusting() == false)
        {
            String trxName = null;
            int periodID = headerPanel.getSelectedPeriodID();
            P_Period Period = P_Period.get(Env.getCtx(), periodID, trxName);
            headerPanel.refreshEmployeeInformation(employeeListPanel.getSelectedEmployeeID(), Period.getEndDate());
        }
    }*/

    int calendarID = 0;
    int oldEmployeeID = 0;
    /**
     * Active ou desactive le curseur sablier (Wait Cursor) pour indiquer un chargement
     * @param waiting true pour afficher le sablier, false pour restaurer le curseur standard
     */
    private void setWaiting(final boolean waiting)
    {
        if (!SwingUtilities.isEventDispatchThread())
        {
            SwingUtilities.invokeLater(() -> setWaiting(waiting));
            return;
        }

        Cursor cursor = waiting ? Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) : Cursor.getDefaultCursor();
        setCursor(cursor);

        JRootPane rootPane = getRootPane();
        if (rootPane != null)
        {
            rootPane.setCursor(cursor);
        }

        JFrame frame = Env.getFrame(this);
        if (frame == null && getFrame() != null)
        {
            frame = getFrame();
        }
        if (frame != null)
        {
            frame.setCursor(cursor);
        }

        if (headerPanel != null)
        {
            headerPanel.setCursor(cursor);
        }
        if (creditsPanel != null)
        {
            creditsPanel.setCursor(cursor);
        }
        if (detailPanel != null)
        {
            detailPanel.setCursor(cursor);
        }
        if (employeeListPanel != null)
        {
            employeeListPanel.setCursor(cursor);
        }

        if (waiting)
        {
            setStatusLine("Chargement...", false);
        }
        else
        {
            setStatusLine("", false);
        }
    }

    public void reloadInfo() {
        final int employeeID = (employeeListPanel != null) ? employeeListPanel.getSelectedEmployeeID() : 0;
        if (employeeID <= 0 || headerPanel == null) {
            setWaiting(false);
            return;
        }

        final int periodID = headerPanel.getSelectedPeriodID();
        final long token = m_reloadToken.incrementAndGet();

        setWaiting(true);

        s_bgExecutor.submit(() -> {
            boolean scheduledEdt = false;
            try {
                if (token != m_reloadToken.get()) return;

                P_Employee employee = P_Employee.get(Env.getCtx(), employeeID, null);
                if (employee == null || token != m_reloadToken.get()) return;

                P_Payment_Group paymentGroup = (employee.getP_Payment_Group_ID() > 0)
                    ? P_Payment_Group.get(Env.getCtx(), employee.getP_Payment_Group_ID(), null)
                    : null;
                P_Period period = P_Period.get(Env.getCtx(), periodID, null);
                if (period == null || token != m_reloadToken.get()) return;

                // Pre-load lookups in parallel via Virtual Threads (assignment, deduction, credits, taxable benefit)
                if (detailPanel != null && detailPanel.getLookup() != null) {
                    detailPanel.getLookup().setEmployeeId(employeeID, period.getEndDate());
                }

                // Find timeSheetID & paymentID for credits calculation
                int tsID = 0;
                int payID = 0;
                String tsSql = "SELECT P_Time_Sheet_ID, P_Payment_ID FROM P_TIME_SHEET WHERE P_EMPLOYEE_ID = ? AND P_PERIOD_ID = ? ORDER BY SheetType DESC, Value";
                try (java.sql.PreparedStatement pstmt = DB.prepareStatement(tsSql, null)) {
                    pstmt.setInt(1, employeeID);
                    pstmt.setInt(2, periodID);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            tsID = rs.getInt(1);
                            payID = rs.getInt(2);
                        }
                    }
                } catch (Exception e) {
                    s_log.log(Level.SEVERE, "VTimeSheetMain.reloadInfo - find ts", e);
                }

                if (token != m_reloadToken.get()) return;

                // Run heavy DB tasks concurrently via Virtual Threads
                final int fTimeSheetID = tsID;
                final int fPaymentID = payID;
                CreditsModel creditsModel = null;
                boolean hasChat = false;
                boolean hasAttachment = false;

                try (ExecutorService subExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
                    Future<CreditsModel> creditsFuture = (creditsPanel != null)
                        ? subExecutor.submit(() -> creditsPanel.buildBankModel(fTimeSheetID, employeeID, periodID, fPaymentID))
                        : null;
                    Future<Boolean> chatFuture = (aChat != null && fTimeSheetID > 0)
                        ? subExecutor.submit(() -> hasChat(P_TIMESHEET_AD_Table_ID, fTimeSheetID))
                        : null;
                    Future<Boolean> attachFuture = (aAttachment != null && fTimeSheetID > 0)
                        ? subExecutor.submit(() -> hasAttachment(P_TIMESHEET_AD_Table_ID, fTimeSheetID))
                        : null;

                    if (creditsFuture != null) creditsModel = creditsFuture.get();
                    if (chatFuture != null) hasChat = chatFuture.get();
                    if (attachFuture != null) hasAttachment = attachFuture.get();
                } catch (Exception e) {
                    s_log.log(Level.SEVERE, "VTimeSheetMain.reloadInfo - parallel tasks", e);
                }

                if (token != m_reloadToken.get()) return;

                final CreditsModel fCreditsModel = creditsModel;
                final boolean fHasChat = hasChat;
                final boolean fHasAttachment = hasAttachment;
                final P_Period fPeriod = period;
                final P_Payment_Group fPaymentGroup = paymentGroup;

                scheduledEdt = true;
                SwingUtilities.invokeLater(() -> {
                    if (token != m_reloadToken.get()) return;

                    try {
                        int calID = fPeriod.getP_Calendar_ID();
                        calendarID = calID;
                        if (headerPanel.getPayCalendar() == 0) {
                            headerPanel.setPayCalendar(calID);
                        }

                        headerPanel.refreshEmployeeInformation(employeeID, fPeriod.getEndDate());

                        if (creditsPanel != null) {
                            if (fCreditsModel != null) {
                                creditsPanel.applyBankModel(fCreditsModel);
                            } else {
                                creditsPanel.LoadBank(headerPanel.getCurrentTimeSheetID(), employeeID, periodID);
                            }
                        }

                        if (detailPanel != null) {
                            if (fPaymentGroup != null) {
                                detailPanel.setDisplayDetail(fPaymentGroup.getTimeSheetModel());
                            }
                            detailPanel.loadDetailPanel();
                            DataStatusEvent dse = new DataStatusEvent(VTimeSheetMain.this, 0, false, Env.isAutoCommit(Env.getCtx(), getWindowNo()), false);
                            dse.AD_Table_ID = 2000112;
                            P_Time_Sheet ts = headerPanel.getTimeSheet();
                            if (ts != null) {
                                dse.Record_ID = Integer.valueOf(ts.get_ID());
                                dse.CreatedBy = Integer.valueOf(ts.getCreatedBy());
                                dse.Created = ts.getCreated();
                                dse.UpdatedBy = Integer.valueOf(ts.getUpdatedBy());
                                dse.Updated = ts.getUpdated();
                                enableDelete(ts.get_ID() > 0);
                            } else {
                                enableDelete(false);
                            }
                            getStatusBar().setStatusDB(employeeListPanel.getItemPosition() + "/" + (employeeListPanel.getItemCount()), dse);

                            detailPanel.resetEditable();
                            enableSave(false);
                        }

                        if (aChat != null) {
                            aChat.setPressed(fHasChat);
                        }
                        if (aAttachment != null) {
                            aAttachment.setPressed(fHasAttachment);
                        }
                    } finally {
                        if (token == m_reloadToken.get()) {
                            setWaiting(false);
                        }
                    }
                });
            } catch (Throwable t) {
                s_log.log(Level.SEVERE, "VTimeSheetMain.reloadInfo - background", t);
            } finally {
                if (!scheduledEdt && token == m_reloadToken.get()) {
                    SwingUtilities.invokeLater(() -> {
                        if (token == m_reloadToken.get()) {
                            setWaiting(false);
                        }
                    });
                }
            }
        });
    }

    /**
     * Create New Record
     * 
     * @param copy
     *            true if current record is to be copied
     */
    protected void cmd_new(boolean copy)
    {
        if( isReadOnly() ) return;
        
        P_Period Period = P_Period.get(Env.getCtx(), headerPanel.getSelectedPeriodID(), null);
        
        // Impossible de saisir dans une pÃ©riode fermÃ©.
        if ( Period.getPeriodStatus().equals( P_Period.PERIODSTATUS_Closed ) || Period.getPeriodStatus().equals( P_Period.PERIODSTATUS_PermanentlyClosed ) ) return;
        
        cmd_save(false);
        headerPanel.createTimeSheet(null);
        //	m_curTab.getTableModel().setChanged(false);
    } //  cmd_new

    /**
     * Confirm & delete record
     */
    protected void cmd_delete()
    {
        if( isReadOnly() ) return;
        
        // On peu dÃ©truire les feuilles de temps seulement si elle sont a l'Ã©tat initial, ou a l'Ã©tat calculer pour les ajustements.
        if (headerPanel.getTimeSheet().getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Initial) ||
        	( ( headerPanel.getTimeSheet().getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Calculated ) || ( headerPanel.getTimeSheet().getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Error )  ) ) &&
        	  (headerPanel.getTimeSheet().getSheetType().equals(P_Time_Sheet.SHEETTYPE_Adjustment ) )		
        	)
           ) 
        {
    		int response = JOptionPane.showConfirmDialog(new Frame(""),
    				Msg.translate(Env.getCtx(), "DeleteEmployeTimeSheet?" ),
    				Msg.translate(Env.getCtx(), "DeleteRecord?" ),
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE);
            if(response == JOptionPane.YES_OPTION)
            {
            	headerPanel.delete();
            	cmd_refresh();
            }
    	}
        else {
        	JOptionPane.showMessageDialog(this, Msg.translate(Env.getCtx(), "SuppressionSeulementInitial" ));
        }
    } //  cmd_delete

    /**
     * If required ask if you want to save and save it
     * 
     * @param manualCmd
     *            true if invoked manually (i.e. force)
     * @return true if saved
     */
    public boolean cmd_save(boolean manualCmd)
    {
        if( isReadOnly() ) return false;
        enableSave( false );
        boolean ret = detailPanel.save();
        if( ret == true ) cmd_refresh();
        return ret;
    } //  cmd_save

    /**
     * Ignore
     */
    public void cmd_ignore() { cmd_refresh(); } //  cmd_ignore

    /**
     * Refresh
     */
    public void cmd_refresh() { reloadInfo();  } //  cmd_refresh

    /**
     * Print standard Report
     * 
     * @param source
     *            event source
     */
    private void cmd_report(Object source) { cmd_save(false); } //	cmd_report

    /**
     * Zoom Across Menu
     */
    public void cmd_zoomAcross()
    {
    	int record_ID = headerPanel.getCurrentEmployeeID(); 
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
     *	Find - Set Query
     */
    public void cmd_find()
    {
    	//2009.05.13 - corrige problÃ¨me de performance.
    	if ( employeeListPanel != null )
    		employeeListPanel.removeListSelectionListener(SelectionListener );
        //2009.05.13

    	//        System.out.println(" VTimeSheet Find ");
        String trxName = null;
        
        MTable table = new MTable(Env.getCtx(), P_EMPLOYEE_AD_Table_ID, trxName);
        MTab   tab  = new MTab(Env.getCtx(), P_EMPLOYEE_AD_Tab_ID, trxName);
        
        GridField[] findFields = GridField.createFields(Env.getCtx(), 0, 0, P_EMPLOYEE_AD_Tab_ID);

        //2011.10.03 par dÃ©faut count pour les employÃ©s avec feuille de temps
        CustomWindowFind find = new CustomWindowFind (Env.getFrame(this),  P_EMPLOYEE_AD_Window_ID, this.getTitle(),
        		tab.getAD_Tab_ID(), P_EMPLOYEE_AD_Table_ID, "P_Employee", "", findFields, 1, CustomWindowFind.QUERYTYPE_TimeSheet);

//        CustomWindowFind find = new CustomWindowFind (Env.getFrame(this),  P_EMPLOYEE_AD_Window_ID, this.getTitle(),
//        		tab.getAD_Tab_ID(), P_EMPLOYEE_AD_Table_ID, "P_Employee", tab.getWhereClause(), findFields, 1, null, CustomWindowFind.QUERYTYPE_TimeSheet);

        Query query = find.getQuery();
//        find = null;
        if(query != null)
        {
            headerPanel.setPayCalendar( find.getP_Calendar_ID() ) ;
            query.addRestriction(" P_Payment_Group_ID in ( select P_Payment_Group_ID from P_Payment_Group where P_Calendar_ID = " + find.getP_Calendar_ID() + ")");

            headerPanel.loadPayPeriod( headerPanel.getPayCalendar() );

            headerPanel.resetDefaultPeriod();
//        	System.out.println(" VTimeSheet Find Query " + query.getWhereClause());
            if ( employeeListPanel != null)
            {
            	employeeListPanel.setWhereClause( query.getWhereClause() );
            }
        	//2009.05.13 
            //       employeeListPanel.selectFirst();
        }
        

        super.cmd_find();
    	//2009.05.13 - corrige problÃ¨me de performance.
        if ( employeeListPanel != null)
        {
            employeeListPanel.addListSelectionListener(SelectionListener );
            employeeListPanel.selectFirst();
        }
        //2009.05.13
        
   } //	cmd_find

    
	private void setExportCell(org.apache.poi.ss.usermodel.Row row, int colIndex, String value, org.apache.poi.ss.usermodel.CellStyle style) {
		org.apache.poi.ss.usermodel.Cell cell = row.createCell(colIndex);
		if (value != null) {
			cell.setCellValue(value);
		} else {
			cell.setCellValue("");
		}
		cell.setCellStyle(style);
	}

	private void setExportCellNumber(org.apache.poi.ss.usermodel.Row row, int colIndex, double value, org.apache.poi.ss.usermodel.CellStyle style) {
		org.apache.poi.ss.usermodel.Cell cell = row.createCell(colIndex);
		cell.setCellValue(value);
		cell.setCellStyle(style);
	}

	/**
	 * Cette methode sert a exporter le contenue du formulaire dans un fichier Excel (.xlsx)
	 */
	public void cmd_export() 
	{
		File file = org.solstice.util.ExcelExportUtil.chooseFile("Feuille_De_Temps.xlsx", this);
		if (file == null)
			return;

		String fileName = file.getAbsolutePath();
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			try (org.apache.poi.xssf.usermodel.XSSFWorkbook wb = new org.apache.poi.xssf.usermodel.XSSFWorkbook()) {
				org.apache.poi.xssf.usermodel.XSSFSheet sheet = wb.createSheet("Detail");

				org.apache.poi.ss.usermodel.CellStyle cf = wb.createCellStyle();
				org.apache.poi.ss.usermodel.Font wf = wb.createFont();
				wf.setFontName("Arial");
				wf.setFontHeightInPoints((short) 10);
				wf.setBold(true);
				cf.setFont(wf);
				cf.setFillForegroundColor(org.apache.poi.ss.usermodel.IndexedColors.GREY_25_PERCENT.getIndex());
				cf.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
				cf.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
				cf.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
				cf.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
				cf.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);

				org.apache.poi.ss.usermodel.CellStyle cf2 = wb.createCellStyle();
				org.apache.poi.ss.usermodel.Font wf2 = wb.createFont();
				wf2.setFontName("Arial");
				wf2.setFontHeightInPoints((short) 10);
				cf2.setFont(wf2);
				cf2.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
				cf2.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
				cf2.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
				cf2.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);

				org.apache.poi.ss.usermodel.CellStyle nf1 = wb.createCellStyle();
				nf1.cloneStyleFrom(cf2);
				nf1.setDataFormat(wb.createDataFormat().getFormat("#,##0.00"));

				int record_ID = headerPanel.getCurrentTimeSheetID();
				P_Time_Sheet Timesheet = P_Time_Sheet.get(Env.getCtx(), record_ID, null);
				P_Employee Employee = (Timesheet != null) ? P_Employee.get(Env.getCtx(), Timesheet.getP_Employee_ID(), null) : null;
				P_Period Period = (Timesheet != null) ? P_Period.get(Env.getCtx(), Timesheet.getP_Period_ID(), null) : null;

				P_Job_Type Job_Type = (Timesheet != null) ? P_Job_Type.get(Env.getCtx(), Timesheet.getP_Job_Type_ID(), null) : null;
				P_Distribution_Booklet Distribution_Booklet = (Timesheet != null) ? P_Distribution_Booklet.get(Env.getCtx(), Timesheet.getP_Booklet_ID(), null) : null; 
				P_Collective_Labour_Agr Collective_Labour_Agr = (Timesheet != null) ? P_Collective_Labour_Agr.get(Env.getCtx(), Timesheet.getP_Collective_Labour_Agr_ID(), null) : null;

				P_Payment Payment = (Timesheet != null) ? P_Payment.get(Env.getCtx(), Timesheet.getP_Payment_ID(), null) : null;
				
				MOrg Org = (Timesheet != null) ? new MOrg(Env.getCtx(), Timesheet.getAD_Org_ID(), null) : null;
				MRegion Region = (Timesheet != null) ? MRegion.get(Env.getCtx(), Timesheet.getTaxation_Region_ID()) : null;
				MActivity Activity = (Timesheet != null) ? new MActivity(Env.getCtx(), Timesheet.getC_Activity_ID(), null) : null;
				P_Job_Title JobTitle = (Timesheet != null) ? P_Job_Title.get(Env.getCtx(), Timesheet.getP_Job_Title_ID(), null) : null;

				// Row 0
				org.apache.poi.ss.usermodel.Row r0 = sheet.createRow(0);
				setExportCell(r0, 0, Msg.translate(Env.getCtx(), "P_Employee_ID"), cf);
				setExportCell(r0, 1, Employee != null ? Employee.getValue() + " - " + Employee.getName() : "", cf2);

				// Row 1
				org.apache.poi.ss.usermodel.Row r1 = sheet.createRow(1);
				setExportCell(r1, 0, Msg.translate(Env.getCtx(), "PeriodDate"), cf);
				String PeriodDate = "";
				if (Period != null) {
					if (Period.getStartDate() != null)
						PeriodDate += Period.getStartDate().toString().substring(0, 10);
					PeriodDate += Msg.translate(Env.getCtx(), "TimeSheetTo");
					if (Period.getEndDate() != null)
						PeriodDate += Period.getEndDate().toString().substring(0, 10);
				}
				setExportCell(r1, 1, PeriodDate, cf2);
				setExportCell(r1, 2, Msg.translate(Env.getCtx(), "PayDate"), cf);
				setExportCell(r1, 3, (Period != null && Period.getPayDate() != null) ? Period.getPayDate().toString().substring(0, 10) : "", cf2);
				setExportCell(r1, 4, Msg.translate(Env.getCtx(), "P_Period_ID"), cf);
				setExportCell(r1, 5, Period != null ? Period.getName() : "", cf2);

				// Row 2
				org.apache.poi.ss.usermodel.Row r2 = sheet.createRow(2);
				setExportCell(r2, 0, Msg.translate(Env.getCtx(), "DistributionBooklet"), cf);
				setExportCell(r2, 1, Distribution_Booklet != null ? Distribution_Booklet.getValue() + " - " + Distribution_Booklet.getTrlName() : "", cf2);
				setExportCell(r2, 2, Msg.translate(Env.getCtx(), "CollectiveLabourAgr"), cf);
				setExportCell(r2, 3, Collective_Labour_Agr != null ? Collective_Labour_Agr.getValue() + " - " + Collective_Labour_Agr.getTrlName() : "", cf2);
				setExportCell(r2, 4, Msg.translate(Env.getCtx(), "TimeSheetStatus"), cf);
				setExportCell(r2, 5, Timesheet != null ? Timesheet.GetTimeSheetStatusDsc() : "", cf2);

				// Row 3
				org.apache.poi.ss.usermodel.Row r3 = sheet.createRow(3);
				setExportCell(r3, 0, Msg.translate(Env.getCtx(), "P_Time_Sheet_ID"), cf);
				setExportCell(r3, 1, Timesheet != null ? Timesheet.getValue() : "", cf2);

				// Row 4
				org.apache.poi.ss.usermodel.Row r4 = sheet.createRow(4);
				setExportCell(r4, 0, Msg.translate(Env.getCtx(), "AD_Org_ID"), cf);
				setExportCell(r4, 1, Org != null ? Org.getName() : "", cf2);
				setExportCell(r4, 2, Msg.translate(Env.getCtx(), "Taxation_Region_ID"), cf);
				setExportCell(r4, 3, Region != null ? Region.getDescription() : "", cf2);
				setExportCell(r4, 4, Msg.translate(Env.getCtx(), "C_Activity_ID"), cf);
				setExportCell(r4, 5, Activity != null ? Activity.getValue() + " - " + Activity.getName() : "", cf2);

				// Row 5
				org.apache.poi.ss.usermodel.Row r5 = sheet.createRow(5);
				setExportCell(r5, 0, Msg.translate(Env.getCtx(), "TitreEmploi"), cf);
				setExportCell(r5, 1, JobTitle != null ? JobTitle.getValue() + " - " + JobTitle.getName() : "", cf2);
				setExportCell(r5, 2, Msg.translate(Env.getCtx(), "StatutEmploi"), cf);
				setExportCell(r5, 3, Job_Type != null ? Job_Type.getValue() + " - " + Job_Type.getName() : "", cf2);
				setExportCell(r5, 4, Msg.translate(Env.getCtx(), "PayGross"), cf);
				double gross = (Payment != null && Payment.getGrossEarnings() != null) ? Payment.getGrossEarnings().doubleValue() : 0.0;
				setExportCellNumber(r5, 5, gross, nf1);

				// Row 6
				org.apache.poi.ss.usermodel.Row r6 = sheet.createRow(6);
				setExportCell(r6, 0, Msg.translate(Env.getCtx(), "SheetType"), cf);
				setExportCell(r6, 1, Timesheet != null ? Timesheet.getSheetType() : "", cf2);
				setExportCell(r6, 2, Msg.translate(Env.getCtx(), "PaymentType"), cf);
				setExportCell(r6, 3, Timesheet != null ? Timesheet.getPaymentType() : "", cf2);
				setExportCell(r6, 4, Msg.translate(Env.getCtx(), "NetPay"), cf);
				double net = (Payment != null && Payment.getNetPay() != null) ? Payment.getNetPay().doubleValue() : 0.0;
				setExportCellNumber(r6, 5, net, nf1);

				for (int c = 0; c < 6; c++) {
					sheet.autoSizeColumn(c);
					sheet.setColumnWidth(c, Math.max(sheet.getColumnWidth(c) + 1200, 256 * 20));
				}

				try (java.io.FileOutputStream fos = new java.io.FileOutputStream(fileName)) {
					wb.write(fos);
				}

				org.solstice.util.ExcelExportUtil.postExportSuccess(file, this);
			} catch (Exception e) {
				s_log.log(Level.SEVERE, "export", e);
			} finally {
				this.setCursor(Cursor.getDefaultCursor());
			}
	}



}  
