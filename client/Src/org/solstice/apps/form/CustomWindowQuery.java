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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;

import org.compiere.apps.ADialog;
import org.compiere.apps.AEnv;
import org.compiere.apps.AWindow;
import org.compiere.apps.AZoomAcross;
import org.compiere.apps.AppsAction;
import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.grid.ed.VButton;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MForm;
import org.compiere.model.MQuery;
import org.compiere.model.MRole;
import org.compiere.model.Query;
import org.compiere.process.ProcessInfo;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogMgt;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Language;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import java.text.DecimalFormat;

import org.compiere.swing.CTextField;

import javax.swing.event.TableModelListener;

/**
 * @author Marc Morissette
 *
 * Utiliser pour tout les fenêtres custom en mode query.
 * 
 */
public abstract class CustomWindowQuery extends CPanel implements FormPanel, ActionListener, TableModelListener, ListSelectionListener 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public CPanel mainPanel = new CPanel();
	public BorderLayout mainLayout = new BorderLayout();
	public CPanel parameterPanel = new CPanel();
	public GridBagLayout parameterLayout = new GridBagLayout();
	public String m_infoQuery = "Info sur la recherche";


//	public JLabel dataStatus = new JLabel();
	public JScrollPane dataPanel = new JScrollPane();
	public MiniTable miniTable = new MiniTable();
	public CPanel commandPanel = new CPanel();
	public FlowLayout commandLayout = new FlowLayout();

	public int m_curWindowNo;

	public int getWindowNo() { return m_curWindowNo; }
	public FormFrame m_frame;
    public FormFrame getFrame() { return m_frame; }
    
    public CPanel getMainPanel() { return mainPanel; }

//    public abstract String getWindowTitle();

    public abstract void calculateSelection( int firstRow, int lastRow);
	public abstract void dynInit();
	public abstract void loadTableInfo();
	
    public String m_sql;

	/**	Logger							*/
	public static CLogger		s_log = CLogger.getCLogger (CustomWindowQuery.class);
	/** Format                  */
	public static DecimalFormat   m_format = DisplayType.getNumberFormat(DisplayType.Amount);

    public CustomWindowQuery() 
	{
		m_ctx = Env.getCtx();
	}

    public Query query;
    
    public int mAD_Window_ID; 
    public int mAD_Tab_ID;  
    public int mAD_Table_ID;
    public int P_TIMESHEET_AD_Table_ID;




//	public JButton bExport = ConfirmPanel.createExportButton(true);
//	public JButton bRefresh = ConfirmPanel.createRefreshButton(true);
//	public JButton bCancel = ConfirmPanel.createCancelButton(true);

	public CTextField queryBox = new CTextField();
    

    public void init( int windowNo, FormFrame frame ) 
    {


		queryBox.setText("Select * From P_Deduction");	
        m_frame = frame;
        
		setLocale(Language.getLoginLanguage().getLocale());
		setLayout(new BorderLayout(2,2));
		
		CPanel northPanel = new CPanel();
		northPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		northPanel.add(toolBar);


		//2010.01.06		
		createMenu();
		add(mainPanel, BorderLayout.CENTER);
		add(statusBar, BorderLayout.SOUTH);
		add(northPanel, BorderLayout.NORTH);

//		mainPanel.add(parameterPanel, BorderLayout.NORTH);

		dynInit();

//		mainPanel.add(dataStatus, BorderLayout.SOUTH);
		add(dataPanel, BorderLayout.CENTER);
		dataPanel.getViewport().add(miniTable, null);

		miniTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		miniTable.setColumnSelectionAllowed(false);
		miniTable.setRowSelectionAllowed(true);
		
		miniTable.getTableHeader().addMouseListener(new ColumnChecker(this.miniTable));

		miniTable.getModel().addTableModelListener(this);
		
// 	    SelectionListener listener = new SelectionListener(miniTable);
		miniTable.getSelectionModel().addListSelectionListener(this);
//		miniTable.getColumnModel().getSelectionModel().addListSelectionListener(this);
		
//		bRefresh.addActionListener(this);
//		this.bExport.addActionListener(this);

		mainPanel.setPreferredSize(new Dimension(1024, 750));

//		commandPanel.setLayout(commandLayout);
//		commandLayout.setAlignment(FlowLayout.RIGHT);
//		commandLayout.setHgap(10);
//		commandPanel.add(bCancel, null);

		frame.getContentPane().add( this );
		
		statusBar.setStatusLine("");

/*        try
        {
            Statement statm = DB.createStatement();
            ResultSet rs = statm.executeQuery(
            	"SELECT tab.AD_Window_ID, tab.AD_Tab_ID, tbl.AD_Table_ID " + 
				"FROM AD_Tab tab, AD_Table tbl " + 
				"WHERE tab.AD_Table_ID = tbl.AD_Table_ID " +
				"AND tab.ad_window_id = 2100426 AND tab.Name = 'RV_P_Payment_Analysis'");
            
            if( rs.next() ) {
                mAD_Window_ID = rs.getInt(1); 
                mAD_Tab_ID = rs.getInt(2);
                mAD_Table_ID = rs.getInt(3);
            }
        	else s_log.log(Level.SEVERE,"Window not found");
            
            rs.close();
            statm.close();
        }
        catch (Exception e)
        {
        	s_log.log(Level.SEVERE,"VTimeSheet.init", e);
        }
*/

		cmd_find();

    }
    

    
//	private boolean isActive = true; // Modif Francis
	private boolean m_findHasBeenCancelled = false;
	private boolean m_finding = false;

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(CustomWindowQuery.class);
	
	/**
	 *	Dispose
	 */
	public void dispose()
	{
		log.info("");
		//  ignore changes
		m_disposing = true;
		//  MenuBar
		if (menuBar != null)
			menuBar.removeAll();
		menuBar = null;
		//  ToolBar
		if (toolBar != null)
			toolBar.removeAll();
		toolBar = null;
		//  Prepare GC
		this.removeAll();
	}	//	dispose

	/**
	 * The Layout.
	 */
	public StatusBar statusBar = new StatusBar();
	public StatusBar getStatusBar() { return statusBar; }
	

	private JToolBar toolBar = new JToolBar();
	private JMenuBar menuBar = new JMenuBar();


	protected AppsAction 	aPrevious, aNext, aParent, aDetail, aFirst, aLast,
							aNew, aCopy, aDelete, aIgnore, aPrint,
							aRefresh, aHistory, aAttachment, aChat, aMulti, aFind,
							aWorkflow, aZoomAcross, aRequest, aWinSize, aArchive;   // , /*Francis*/aActif
	protected AppsAction	aSave, aLock;
	//	Local (added to toolbar)
	protected AppsAction	aReport, aEnd, aHome, aHelp, aProduct,
							aAccount, aCalculator, aCalendar, aEditor, aPreference, aScript,
							aOnline, aMailSupport, aAbout, aPrintScr, aScrShot, aExit, aBPartner;

	private AppsAction 		aExport;

	
	/**************************************************************************
	 *	Create Menu and Toolbar and registers keyboard actions.
	 *  - started from constructor
	 */
	private void createMenu()
	{
		/**
		 *	Menu
		 */
	//	menuBar.setHelpMenu();
		//								File
		JMenu mFile = AEnv.getMenu("File");
		menuBar.add(mFile);
		aPrintScr =	addAction("PrintScreen",	mFile,	KeyStroke.getKeyStroke(KeyEvent.VK_PRINTSCREEN, 0), 	false);
		aScrShot =	addAction("ScreenShot",		mFile,	KeyStroke.getKeyStroke(KeyEvent.VK_PRINTSCREEN, Event.SHIFT_MASK), 	false);
		aReport = 	addAction("Report",			mFile, 	KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.ALT_MASK),	false);
		aPrint = 	addAction("Print",			mFile, 	KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK),	false);
		mFile.addSeparator();
		aEnd =	 	addAction("End",			mFile, 	KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.ALT_MASK),	false);
		aExit =		addAction("Exit",			mFile, 	KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.SHIFT_MASK+Event.ALT_MASK),	false);
		//								Edit
		JMenu mEdit = AEnv.getMenu("Edit");
		menuBar.add(mEdit);
		aNew = 		addAction("New", 			mEdit, 	KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK), false);
		aSave = 	addAction("Save",			mEdit, 	KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK),	false);
		mEdit.addSeparator();
		aCopy =		addAction("Copy", 			mEdit, 	KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK),	false);
		aDelete = 	addAction("Delete",			mEdit, 	KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK),	false);
		aIgnore = 	addAction("Ignore",			mEdit, 	KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),	false);
		aRefresh = 	addAction("Refresh",		mEdit, 	KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0),	false);
		aExport =   addAction("Export", mEdit, null, false);

		mEdit.addSeparator();
		aFind = 	addAction("Find",			mEdit, 	KeyStroke.getKeyStroke(KeyEvent.VK_F, Event.CTRL_MASK), true);	//	toggle
		if (m_isPersonalLock)			
			aLock = addAction("Lock",			mEdit, 	null,	true);		//	toggle
		//								View
		JMenu mView = AEnv.getMenu("View");
		menuBar.add(mView);
		/*
		 * Modif à Francis pour l'ajout du boutton d'enregistrement actif
		 */
	    mView.addSeparator();
	    
		
		mView.addSeparator();
		aAttachment = addAction("Attachment",	mView, 	KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0),	true);		//	toggle
		aChat = addAction("Chat",				mView, 	KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0),	true);		//	toggle
		aHistory = 	addAction("History",		mView, 	KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0),	true);		//	toggle
		mView.addSeparator();
		aMulti =	addAction("Multi",			mView, 	KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0),	true);		//	toggle
		
		
		//								Go
		JMenu mGo = AEnv.getMenu("Go");
		menuBar.add(mGo);
		aFirst =	addAction("First", 			mGo, 	KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, Event.ALT_MASK),	false);
		aPrevious = addAction("Previous", 		mGo, 	KeyStroke.getKeyStroke(KeyEvent.VK_UP, Event.ALT_MASK),	false);
		aNext = 	addAction("Next", 			mGo, 	KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, Event.ALT_MASK),	false);
		aLast =		addAction("Last",	 		mGo, 	KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, Event.ALT_MASK),	false);
		mGo.addSeparator();
		aParent =	addAction("Parent", 		mGo, 	KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, Event.ALT_MASK),	false);
		aDetail =	addAction("Detail", 		mGo,	KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, Event.ALT_MASK),	false);
		mGo.addSeparator();
		aZoomAcross = addAction("ZoomAcross",	mGo, 	null,	false);
		aRequest =  addAction("Request",		mGo, 	null,	false);
		aArchive =  addAction("Archive",		mGo, 	null,	false);
		aHome =		addAction("Home", 			mGo,	null,	false);
		//								Tools
		JMenu mTools = AEnv.getMenu("Tools");
		menuBar.add(mTools);
		aCalculator = addAction("Calculator",	mTools, 	null,	false);
		aCalendar = addAction("Calendar",		mTools, 	null,	false);
		aEditor =	addAction("Editor",			mTools, 	null,	false);
		aScript = addAction("Script",	        mTools, 	null,	false);
		if ("Y".equals(Env.getContext(m_ctx, "#SysAdmin")))	//	set in DB.loginDB
			aWinSize = addAction("WinSize",     mTools, 	null,	false);
		if (AEnv.isWorkflowProcess())
			aWorkflow = addAction("WorkFlow",	mTools,		null,	false);
		if (MRole.getDefault().isShowPreference())
		{
			mTools.addSeparator();
			aPreference = addAction("Preference",	mTools, 	null,	false);
		}
		//								Help
		JMenu mHelp = AEnv.getMenu("Help");
		menuBar.add(mHelp);
		aHelp = 	addAction("Help",			mHelp, 	KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0),	false);
		aOnline =	addAction("Online",			mHelp, 	null,	false);
		aMailSupport = addAction("EMailSupport",	mHelp,	null,	false);
		aAbout = 	addAction("About",			mHelp, 	null,	false);

		/**
		 *  KeyBoard Actions
		 *
		 * 	Function Keys (if not defined above)
		 */
		int c = CPanel.WHEN_IN_FOCUSED_WINDOW;	//	default condition = WHEN_FOCUSED
		//	ESC = Ignore
		getInputMap(c).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), aIgnore.getName());
		getActionMap().put(aIgnore.getName(), aIgnore);
		//	F1 = Help
		getInputMap(c).put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), aHelp.getName());
		getActionMap().put(aHelp.getName(), aHelp);
		//	F2 = New
		getInputMap(c).put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), aNew.getName());
		getActionMap().put(aNew.getName(), aNew);
		//	F3 = Delete
		getInputMap(c).put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), aDelete.getName());
		getActionMap().put(aDelete.getName(), aDelete);
		//	F4 = Save
		getInputMap(c).put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0), aSave.getName());
		getActionMap().put(aSave.getName(), aSave);
		//	F5 = Refresh
		getInputMap(c).put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), aRefresh.getName());
		getActionMap().put(aRefresh.getName(), aRefresh);
		//	F6 = Find
		getInputMap(c).put(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0), aFind.getName());
		getActionMap().put(aFind.getName(), aFind);
		//	F7 = Arrachment
		getInputMap(c).put(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0), aAttachment.getName());
		getActionMap().put(aAttachment.getName(), aAttachment);
		//	F8 = Multi
		getInputMap(c).put(KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0), aMulti.getName());
		getActionMap().put(aMulti.getName(), aMulti);
		//	F9 = History
		getInputMap(c).put(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0), aHistory.getName());
		getActionMap().put(aHistory.getName(), aHistory);

		/**
		 *	ToolBar
		 */
		toolBar.add(aIgnore.getButton());		//	ESC
		toolBar.addSeparator();
		toolBar.add(aHelp.getButton());			//	F1
		toolBar.add(aNew.getButton());
		toolBar.add(aDelete.getButton());
		toolBar.add(aSave.getButton());
		toolBar.addSeparator();
		toolBar.add(aRefresh.getButton());      //  F5
		toolBar.add(aFind.getButton());
		toolBar.add(aAttachment.getButton());
		toolBar.add(aChat.getButton());
		toolBar.add(aMulti.getButton());
		toolBar.addSeparator();
//		toolBar.add(aHistory.getButton());		//	F9
		toolBar.add(aHome.getButton());			//	F10 is Windows Menu Key
//		toolBar.add(aParent.getButton());
//		toolBar.add(aDetail.getButton());
		toolBar.addSeparator();
		toolBar.add(aFirst.getButton());
		toolBar.add(aPrevious.getButton());
		toolBar.add(aNext.getButton());
		toolBar.add(aLast.getButton());
		toolBar.addSeparator();
		toolBar.add(aReport.getButton());
//		toolBar.add(aArchive.getButton());
		toolBar.add(aPrint.getButton());
		toolBar.add(aExport.getButton());
		toolBar.addSeparator();
		if (m_isPersonalLock)
			toolBar.add(aLock.getButton());
		toolBar.add(aZoomAcross.getButton());
		if (aWorkflow != null)
			toolBar.add(aWorkflow.getButton());
		toolBar.add(aRequest.getButton());
		if (aProduct != null)
			toolBar.add(aProduct.getButton());
		toolBar.addSeparator();
		toolBar.add(aEnd.getButton());
		/*
		 * Modif à Francis pour ajouter le boutton d'enregistrement actif
		 */
//		toolBar.addSeparator();
//		toolBar.add(aActif.getButton());
		//
		if (CLogMgt.isLevelFinest())
			Util.printActionInputMap(this);
		
	    aDelete.setEnabled( true );
	    aSave.setEnabled( false );
	}	//	createMenu


	/**
	 *	Add (Toggle) Action to Toolbar and Menu
	 *  @param action action
	 *  @param menu manu
	 *  @param accelerator accelerator
	 *  @param toggle toggle button
	 *  @return AppsAction
	 */
	private AppsAction addAction (String action, JMenu menu, KeyStroke accelerator, boolean toggle)
	{
		AppsAction act = new AppsAction(action, accelerator, toggle);
		if (menu != null)
			menu.add(act.getMenuItem());
		act.setDelegate(this);
		return act;
	}	//	addAction

	/**
	 *	Return MenuBar
	 *  @return JMenuBar
	 */
	public JMenuBar getMenuBar()
	{
		return menuBar;
	}	//	getMenuBar


	/**
	 *	Get Title of Window
	 *  @return String with Title
	 */
	
	public String getTitle( String ClassName ) {
    	int AD_Form_ID = 0;
        try
        {
            Statement statm = DB.createStatement();
            ResultSet rs = statm.executeQuery(
            		  " SELECT AD_Form_ID " 
            		+ " FROM AD_Form"
            		+ " WHERE ClassName = '"+ ClassName + "'" 
            		);
            
            if( rs.next() ) {
               	AD_Form_ID = rs.getInt("AD_Form_ID");
            }
        	else s_log.log(Level.SEVERE,"getWindowTitle");
            
            rs.close();
            statm.close();
        }
        catch (Exception e)
        {
        	s_log.log(Level.SEVERE,"getWindowTitle", e);
        }
    	
        MForm Form = new MForm( Env.getCtx(), AD_Form_ID, null);
		
        Language language = Env.getLanguage( Env.getCtx());
		
		return Form.getTrlName( language.getAD_Language() );
	        
	}	//	getTitle


	public Properties      m_ctx;

	/** Dispose active                                  */
	public boolean         m_disposing = false;
	/** Save Error Message indicator                    */
	public boolean         m_errorDisplayed = false;
	/** Only current row flag                           */
	public boolean			m_onlyCurrentRows = true;
	/** Number of days to show	0=all					*/
	public int				m_onlyCurrentDays = 0;
	/** Process Info                                    */
	public boolean         m_isLocked = false;
	/** Show Personal Lock								*/
	public boolean 		m_isPersonalLock = MRole.getDefault().isPersonalLock();
	/**	Last Modifier of Action Event					*/
	public int 			m_lastModifiers;

	/**
	 *	Set Status Line to text
	 *  @param text clear text
	 *  @param error error flag
	 */
	public void setStatusLine (String text, boolean error)
	{
		log.fine(text);
		statusBar.setStatusLine(text, error);
	}	//	setStatusLine

	/**
	 *	Indicate Busy
	 *  @param busy busy
	 */
	public void setBusy (boolean busy)
	{
		m_isLocked = busy;
		//
		JFrame frame = Env.getFrame(this);
		if (frame == null)  //  during init
			return;
		if (frame instanceof AWindow)
			((AWindow)frame).setBusy(busy);
	//	String processing = Msg.getMsg(m_ctx, "Processing");
		if (busy)
		{
	//		setStatusLine(processing);
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		}
		else
		{
			this.setCursor(Cursor.getDefaultCursor());
			frame.setCursor(Cursor.getDefaultCursor());
	//		if (statusBar.getStatusLine().equals(processing))
	//			statusBar.setStatusLine("");
		}
	}	//	set Busy


	
	/**************************************************************************
	 *	Action Listener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
/*		//  
		if (e.getSource() == isSearchPeriod)
		{
			if ( isSearchPeriod.isSelected())
			{
				m_cbbPayPeriodFrom.setEnabled(true);
				m_cbbPayPeriodTo.setEnabled(true);
			}
			else
			{
				m_cbbPayPeriodFrom.setEnabled(false);
				m_cbbPayPeriodTo.setEnabled(false);
			}
		}
		
		if (e.getSource() == isSearchDate)
		{
			if ( isSearchDate.isSelected())
			{
				fieldSelectionDateFrom.setEnabled(true);
				fieldSelectionDateTo.setEnabled(true);
			}
			else
			{
				fieldSelectionDateFrom.setEnabled(false);
				fieldSelectionDateTo.setEnabled(false);
			}
		}
		if (e.getSource() == bCancel)
			dispose();

		//  Update Open Invoices
		else if (e.getSource() == bRefresh)
		{
			setBusy( true );
			loadTableInfo();
			setBusy( false );
		}

		else if(e.getSource() == this.bExport)
		{
			setBusy( true );
			try
			{
			    exportToXml();
			}
			catch (Exception ex)
			{
			    s_log.log(Level.SEVERE, "exportToCSV", ex);
			}
			setBusy( false );
		}
*/
		log.info(e.getActionCommand() + " - " + e.getModifiers());
		//	+ " - " + new Timestamp(e.getWhen()) + " " + isUILocked());
		if (m_disposing || isUILocked())
			return;
			
		m_lastModifiers = e.getModifiers();
		String cmd = e.getActionCommand();
		//	Do ScreenShot w/o busy
		if (cmd.equals("ScreenShot"))
		{
			AEnv.actionPerformed (e.getActionCommand(), m_curWindowNo, this);
			return;
		}

		//  Problem: doubleClick detection - can't disable button as clicking button may change button status
		setBusy (true);
		//  Command Buttons
		if (e.getSource() instanceof VButton)
		{
			actionButton((VButton)e.getSource());
			setBusy(false);
			return;
		}

		try
		{
			//	File
			if (cmd.equals(aReport.getName()))
				cmd_report();
			else if (cmd.equals(aPrint.getName()))
				cmd_print();
			else if (cmd.equals(aEnd.getName()))
				cmd_end(false);
			else if (cmd.equals(aExit.getName()))
				cmd_end(true);
			//	Edit
			else if (cmd.equals(aNew.getName()))
				cmd_new(false);
			else if (cmd.equals(aSave.getName()))
				cmd_save(true);
			else if (cmd.equals(aCopy.getName()))
				cmd_new(true);
			else if (cmd.equals(aDelete.getName()))
				cmd_delete();
			else if (cmd.equals(aIgnore.getName()))
				cmd_ignore();
			else if (cmd.equals(aRefresh.getName()))
				cmd_refresh();

			else if (cmd.equals(aExport.getName()))
				cmd_export();	
			
			else if (cmd.equals(aFind.getName()))
				cmd_find();
			else if (m_isPersonalLock && cmd.equals(aLock.getName()))
				cmd_lock();
			//	View
			else if (cmd.equals(aChat.getName()))
				cmd_chat();
			else if (cmd.equals(aAttachment.getName()))
				cmd_attachment();
			else if (cmd.equals(aMulti.getName()))
				cmd_multi();
			else if (cmd.equals(aHistory.getName()))
				cmd_history();
			//	Go
			else if (cmd.equals(aFirst.getName()))
				cmd_moveFirst();
			else if (cmd.equals(aPrevious.getName()))
				cmd_movePrevious();
			else if (cmd.equals(aNext.getName()))
			    cmd_moveNext();
			else if (cmd.equals(aLast.getName()))
			    cmd_moveLast();
			else if (cmd.equals(aParent.getName()))
				cmd_parent();
			else if (cmd.equals(aDetail.getName()))
				cmd_detail();
			else if (cmd.equals(aZoomAcross.getName()))
				cmd_zoomAcross();
			else if (cmd.equals(aRequest.getName()))
				cmd_request();
			else if (cmd.equals(aArchive.getName()))
				cmd_archive();
			/*
			 * Modif à Francis
			 */
//			else if (cmd.equals(aActif.getName()))
//				cmd_actif();
			//	Tools
			else if (aWinSize != null && cmd.equals(aWinSize.getName()))
				cmd_winSize();
			//	Help
			else if (cmd.equals(aHelp.getName()))
				cmd_help();
			//  General Commands (Environment)
			else if (!AEnv.actionPerformed (e.getActionCommand(), m_curWindowNo, this))
				log.log(Level.SEVERE, "No action for: " + cmd);
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, "ex", ex);
			String msg = ex.getMessage();
			if (msg == null || msg.length() == 0)
				msg = ex.toString();
			msg = Msg.parseTranslation(m_ctx, msg);
			ADialog.error(m_curWindowNo, this, "Error", msg);
		}
		//
		setBusy(false);
	}	//	actionPerformed

	
	/**************************************************************************
	 *	Start Button Process
	 *  @param vButton button
	 */
	private void actionButton (VButton vButton)
	{
		log.info(vButton.toString());
	}	//	actionButton


	/**
	 *	Navigate to Detail Tab			->
	 */
	private void cmd_detail() {}

	/**
	 *	Navigate to Parent Tab			<-
	 */
	private void cmd_parent() {}
	
	/**
	 *  Create New Record
	 *  @param copy true if current record is to be copied
	 */
	protected void cmd_new (boolean copy)	{}

	protected void cmd_moveFirst() {}
	protected void cmd_movePrevious() {}
	protected void cmd_moveNext() {}
	protected void cmd_moveLast() {}

	/**
	 *  Confirm & delete record
	 */
	protected void cmd_delete() { }

	/**
	 *  If required ask if you want to save and save it
	 *  @param manualCmd true if invoked manually (i.e. force)
	 *  @return true if saved
	 */
	protected boolean cmd_save (boolean manualCmd) { return false; }

	/**
	 *  Ignore
	 */
	protected void cmd_ignore() {}

	/**
	 *  Refresh
	 */
	protected void cmd_refresh() {}

	/**
	 *  Export
	 */
	protected void cmd_export() {}


	/**
	 *	Print standard Report
	 */
	protected void cmd_report () {}
	
	/**
	 * 	Zoom Across Menu
	 */
	protected void cmd_zoomAcross() {}
	protected void doZoomAcross( String field, MQuery query) {
		new AZoomAcross (aZoomAcross.getButton(), field, query);
	}
	/**
	 * 	Open/View Request
	 */
	protected void cmd_request() {}

	/**
	 * 	Open/View Archive
	 */
	protected void cmd_archive() {}
	
	/**
	 *	Print specific Report - or start default Report
	 */
	protected void cmd_print() {}

	/**
	 *	Find - Set Query
	 */
	protected void cmd_find() {         
	    aFind.setPressed( true );
	}


	/**
	 *	Attachment
	 */
	protected void cmd_attachment() {}

	protected void cmd_multi() {}

	/**
	 *	Chat
	 */
	protected void cmd_chat() {};

	/**
	 *	Lock
	 */
	protected void cmd_lock() {}

	/**
	 *	Toggle History
	 */
	protected void cmd_history() {}

	/**
	 *	Help
	 */
	protected void cmd_help() {}

	/**
	 *  Close this screen - after save
	 *  @param exit ask if user wants to exit application
	 */
	protected void cmd_end (boolean exit)
	{
		boolean exitSystem = false;
		cmd_save(false);

		if (exit && ADialog.ask(m_curWindowNo, this, "ExitApplication?"))
			exitSystem = true;

		Env.getFrame(this).dispose();		//	calls this dispose

		if (exitSystem)
			AEnv.exit(0);
	}   //  cmd_end

	/**
	 * 	Set Window Size
	 */
	protected void cmd_winSize() {
        Dimension size = getSize();
        if (!ADialog.ask(m_curWindowNo, this, "WinSizeSet", "x=" + size.width + " - y=" + size.height))
        {
            setPreferredSize(null);
            SwingUtilities.getWindowAncestor(this).pack();
            size = new Dimension(0, 0);
        }
	}

//	protected PreparedStatement prepareStatement() throws Exception
//	{
//		return null;
//	}
	
	protected ColumnInfo[] getColumn()
	{
	   return null;	
	}
	
	/**************************************************************************
	 *  Lock User Interface.
	 *  Called from the Worker before processing
	 *  @param pi process info
	 */
	public void lockUI (ProcessInfo pi)
	{
	//	log.fine("" + pi);
		setBusy(true);
	}   //  lockUI

	/**
	 *  Unlock User Interface.
	 *  Called from the Worker when processing is done
	 *  @param pi of execute ASync call
	 */
	public void unlockUI (ProcessInfo pi) {}

	/**
	 *  Is the UI locked (Internal method)
	 *  @return true, if UI is locked
	 */
	public boolean isUILocked()
	{
		return m_isLocked;
	}   //  isLoacked

	/**
	 *  Method to be executed async.
	 *  Called from the ASyncProcess worker
	 *  @param pi process info
	 */
	public void executeASync (ProcessInfo pi)
	{
		log.config("-");
	}   //  executeASync
	
	/*
	 * Relance la query avec le IsActive
	 */
/*	protected void cmd_actif()
	{
	    isActive = !isActive;
	    aActif.setPressed(isActive);
	}
*/
    public void enableSave( boolean etat ) { if( aSave != null ) aSave.setEnabled( etat ); }
    public void enableDelete( boolean etat ) { if( aDelete != null ) aDelete.setEnabled( etat ); }


	
	/**
	 * Cette méthode sert à exporter le contenue du grid dans un fichier XML Excel
	 */
/*
	public void exporToXml() 
	{
	    // On doit d'abord créer le fichier *.csv. Pour ce faire, on utilise d'abord
	    // un dialogue de sauvegarde pour obtenir le nom du fichier
        String fileName = null;
		JFileChooser dialog = new JFileChooser();
		if(dialog.showSaveDialog(new Frame()) == JFileChooser.APPROVE_OPTION)
		{
		    // L'utilisateur a choisi un nom de ficher, on doit vérifier si celui-ci
		    // est valide et se termine bien par .csv
		    File file = dialog.getSelectedFile();
		    try
		    {
			    if(file.getPath().length() > 0)
			    {
			        FileWriter fileWriter;
			        if(file.getPath().length() > 4 && file.getPath().endsWith(".xml"))
			        {
			        	fileName = file.getPath();
			            fileWriter = new FileWriter(file);
			        }
			        else
			        {
			        	fileName = file.getPath() + ".xml";
			            fileWriter = new FileWriter(file.getPath() + ".xml");
			        }
			        // On charge maintenant la liste des informations du grid
			        // en réexécutant la requête
			        PreparedStatement stmt = this.prepareStatement();
			        ResultSet rs = stmt.executeQuery();
			        ResultSetMetaData rsMetaData = rs.getMetaData();

			        fileWriter.write("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n");
			        fileWriter.write("<?mso-application progid=\"Excel.Sheet\"?>\n");
			        fileWriter.write("<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\"\n");
			        fileWriter.write("xmlns:o=\"urn:schemas-microsoft-com:office:office\"\n");
			        fileWriter.write(" xmlns:x=\"urn:schemas-microsoft-com:office:excel\"\n");
			        fileWriter.write(" xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\"\n");
			        fileWriter.write(" xmlns:html=\"http://www.w3.org/TR/REC-html40\">\n");
			        fileWriter.write(" <DocumentProperties xmlns=\"urn:schemas-microsoft-com:office:office\">\n");
			        fileWriter.write(" </DocumentProperties>\n");
			        fileWriter.write(" <OfficeDocumentSettings xmlns=\"urn:schemas-microsoft-com:office:office\">\n");
			        fileWriter.write("  <RelyOnVML/>\n");
			        fileWriter.write(" </OfficeDocumentSettings>\n");
			        fileWriter.write(" <Worksheet ss:Name=\"Export \">\n");
			        fileWriter.write("  <Table ss:ExpandedColumnCount=\"" + rsMetaData.getColumnCount() + "\">\n");


//				      -------------PGI Solstice 07/04/2008-----------------
		            // generation des entêtes de colonnes
			        fileWriter.write("<Row>\n");
			       ColumnInfo[] colInfo = this.getColumn();
			        for(int i = 1; i <= rsMetaData.getColumnCount() ; i++)
			        {
			        	String colName = colInfo[i-1].getColHeader();
			        	if(!colName.endsWith("_ID") && !colName.equals(" "))
			        		fileWriter.write("<Cell><Data ss:Type=\"String\">" + colName + "</Data></Cell>\n");
			        }
			        
			        fileWriter.write("</Row>\n");
			       // ----------------------------------------------------- 
		            while(rs.next())
			        {
			            // On crée chaque ligne dans le fichier selon le format d'un .csv
			            String buffer = "";
			            fileWriter.write("<Row>\n");
			            for(int i = 1; i <= rsMetaData.getColumnCount() ; i++)
			            {
			                // On ne doit pas inclure les colonnes ID
			                if(!rsMetaData.getColumnName(i).endsWith("_ID"))
			                {
			                	if ( rsMetaData.getColumnClassName(i).equals( "java.lang.String") )
			                		if (rs.getString(i) != null)
			                		{
			                			fileWriter.write("<Cell><Data ss:Type=\"String\">" + PgiUtil.convertHTMLString(rs.getString(i)) + "</Data></Cell>\n");
			                		}
			                		else
			                		{
			                			fileWriter.write("<Cell><Data ss:Type=\"String\">" + PgiUtil.convertHTMLString("") + "</Data></Cell>\n");
			                		}
			                	else if ( rsMetaData.getColumnClassName(i).equals("java.math.BigDecimal") )
			                		fileWriter.write("<Cell><Data ss:Type=\"Number\">" + rs.getString(i) + "</Data></Cell>\n");
			                	else if ( rsMetaData.getColumnClassName(i).equals("java.math.Double") )
			                		fileWriter.write("<Cell><Data ss:Type=\"Number\">" + rs.getString(i) + "</Data></Cell>\n");
			                	else if ( rsMetaData.getColumnClassName(i).equals("java.sql.Timestamp") )
			                		fileWriter.write("<Cell><Data ss:Type=\"String\">" + rs.getTimestamp(i).toString().substring(0,9) + "</Data></Cell>\n");
			                	else
			                		fileWriter.write("<Cell><Data ss:Type=\"String\">" + rs.getString(i) + "</Data></Cell>\n");
			                }
//			                    buffer += "\"" + rs.getString(i).replaceAll("\"", "\"\"") + "\";";
			            }
//			            buffer += "\"" + rs.getString(rsMetaData.getColumnCount()) + "\"";

				            fileWriter.write("</Row>\n");

//			            fileWriter.write(buffer + "\n");
			        }
			        
			        rs.close();
			        stmt.close();

			        fileWriter.write("  </Table>\n");
			        fileWriter.write(" </Worksheet>\n");
			        fileWriter.write("</Workbook>\n");
				    fileWriter.close();
				    org.solstice.util.ExcelExportUtil.postExportSuccess(new File(fileName), this);
			    }
		    }
		    catch (Exception e)
		    {
		        s_log.log(Level.SEVERE, "exportToXML", e);
		    }
		    
		}
		
	}
*/
	/**
	 * Cette méthode sert à exporter le contenue du grid dans un fichier
	 * séparé par des virgules (*.csv) qui peut être lu dans excel
	 */
 /*   
	public void exportToCSV() throws Exception
	{
	    // On doit d'abord créer le fichier *.csv. Pour ce faire, on utilise d'abord
	    // un dialogue de sauvegarde pour obtenir le nom du fichier
		JFileChooser dialog = new JFileChooser();
		if(dialog.showSaveDialog(new Frame()) == JFileChooser.APPROVE_OPTION)
		{
		    // L'utilisateur a choisi un nom de ficher, on doit vérifier si celui-ci
		    // est valide et se termine bien par .csv
		    File file = dialog.getSelectedFile();
		    if(file.getPath().length() > 0)
		    {
		        FileWriter fileWriter;
		        if(file.getPath().length() > 4 && file.getPath().endsWith(".csv"))
		        {
		            fileWriter = new FileWriter(file);
		        }
		        else
		        {
		            fileWriter = new FileWriter(file.getPath() + ".csv");
		        }

			    try
			    {
			        // On charge maintenant la liste des informations du grid
			        // en réexécutant la requête
			        PreparedStatement stmt = this.prepareStatement();
			        ResultSet rs = stmt.executeQuery();
			        ResultSetMetaData rsMetaData = rs.getMetaData();
			        
			        while(rs.next())
			        {
			            // On crée chaque ligne dans le fichier selon le format d'un .csv
			            String buffer = "";
			            for(int i = 1; i <= rsMetaData.getColumnCount() - 1; i++)
			            {
			                // On ne doit pas inclure les colonnes ID
			                if(!rsMetaData.getColumnName(i).endsWith("_ID"))
			                    buffer += "\"" + rs.getString(i).replaceAll("\"", "\"\"") + "\";";
			            }
			            buffer += "\"" + rs.getString(rsMetaData.getColumnCount()) + "\"";
			            fileWriter.write(buffer + "\n");
			        }
			        
			        rs.close();
			        stmt.close();
			    }
			    catch (SQLException e)
			    {
			        s_log.log(Level.SEVERE, "exportToCSV", e);
			    }
			    
			    fileWriter.close();
		    }
		}
	}
*/

	/**************************************************************************
	 *  List Selection Listener (VTable) - row changed
	 *  @param e event
	 */
	public void valueChanged(ListSelectionEvent e)
	{
		//	nothing or initiated by mouse (wait for "real" one)
		if (e.getValueIsAdjusting())
		{
			System.out.println("The mouse button has not yet been released");
			return;
		}
		//  no rows
		int first = -1;
		int last = -1;
		
	    if (e.getSource() == miniTable.getSelectionModel() && miniTable.getRowSelectionAllowed()) 
	    {
	    		first = e.getFirstIndex();
	    		last = e.getLastIndex();
		} 
	    else if (e.getSource() == miniTable.getColumnModel().getSelectionModel()
		        && miniTable.getColumnSelectionAllowed()) 
		{
		    	first = e.getFirstIndex();
		    	last = e.getLastIndex();
		}

		System.out.println("first " + first + " last " + last);

	    calculateSelection( first, last );
		
	}   //  valueChanged

    
	/**
	 *  Table Model Listener
	 *  @param e event
	 */
	public void tableChanged(TableModelEvent e)
	{
		if (m_disposing || isUILocked())
			return;
		
//		if (e.getColumn() == 0)
//			calculateSelection( e.getFirstRow(), e.getLastRow() );
	}   //  valueChanged


	public void getConstance( String tableName, int AD_Window_ID)
	{
        try
        {
            Statement statm = DB.createStatement();
            ResultSet rs = statm.executeQuery(
            		  " SELECT tab.AD_Window_ID, tab.AD_Tab_ID, tbl.AD_Table_ID " 
            		+ " FROM AD_Tab tab"
            		+ " inner join AD_Table tbl on tab.AD_Table_ID = tbl.AD_Table_ID" 
            		+ " WHERE tbl.Name = '"+ tableName + "'" 
            		+ "  AND Tab.AD_WINDOW_ID = " + AD_Window_ID);
//            		+ "  AND Tab.AD_WINDOW_ID = 2000042");
            
            if( rs.next() ) {
               	mAD_Window_ID = rs.getInt(1);
            	mAD_Tab_ID   = rs.getInt(2);
            	mAD_Table_ID =  rs.getInt(3);
            }
        	else s_log.log(Level.SEVERE,"getConstance");
            
            rs.close();
            statm.close();
        }
        catch (Exception e)
        {
        	s_log.log(Level.SEVERE,"getConstance", e);
        }
	}

	public static String getSolsticeParameter (Properties ctx, String ParameterName )
	{
		String Parameter = null;
	    String sql = "select top 1 Parameter from P_Solstice_Parameters Where Value = '" + ParameterName + "'";
        PreparedStatement stmt = DB.prepareStatement(sql, null);
		try
		{
		    ResultSet rs = stmt.executeQuery();
		    if(rs.next())
		    {
		    	// Env.getContext(Env.getCtx(), "#AD_User_Name")
		    	Parameter = rs.getString(1);
		    }
		    rs.close();
		    stmt.close();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"VfileImport", e);
			return null;
		}
		
	    return Parameter;
	}


	public String getPath()
	{
		String directory = "";
		//Path path = Paths.get("S:");
		Path path = Paths.get(getSolsticeParameter( Env.getCtx(), "ImportPath" ));

		if ( Files.exists( path ))
			directory = getSolsticeParameter( Env.getCtx(), "ImportPath" );
		else {
			Path path2 = Paths.get(getSolsticeParameter( Env.getCtx(), "ImportPath2" ));

			if ( Files.exists( path2 ))
				directory = getSolsticeParameter( Env.getCtx(), "ImportPath2" );
			else directory = "";
		}

		return directory;
	}

	
}
