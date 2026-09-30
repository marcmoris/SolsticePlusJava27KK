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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.compiere.apps.ADialog;
import org.compiere.apps.AEnv;
import org.compiere.apps.APanel;
import org.compiere.apps.AWindow;
import org.compiere.apps.AZoomAcross;
import org.compiere.apps.AppsAction;
import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.grid.ed.VButton;
import org.compiere.model.MQuery;
import org.compiere.model.MRole;
import org.compiere.process.ProcessInfo;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogMgt;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Language;
import org.compiere.util.Msg;
import org.compiere.util.Util;

/**
 * @author jeacat01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class CustomWindow extends CPanel implements FormPanel, ActionListener {

    private int m_curWindowNo;
    public int getWindowNo() { return m_curWindowNo; }
    
    private FormFrame m_frame;
    public FormFrame getFrame() { return m_frame; }

    public abstract JComponent getMainPanel();
    
	/** Attachments         */
	private HashMap<Integer,Integer>	m_Attachments = null;

    /** Chats				*/
	private HashMap<Integer,Integer>	m_Chats = null;

    public CustomWindow() {
		m_ctx = Env.getCtx();
    }
    
    public void init( int windowNo, FormFrame frame ) {
        m_curWindowNo = windowNo;
        m_frame = frame;

		setLocale(Language.getLoginLanguage().getLocale());
		setLayout(new BorderLayout(2,2));
		
		CPanel northPanel = new CPanel();
		northPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		northPanel.add(toolBar);

		//2010.01.06		
		createMenu();
		add(getMainPanel(), BorderLayout.CENTER);
		add(statusBar, BorderLayout.SOUTH);
		add(northPanel, BorderLayout.NORTH);

		//2010.01.06		
		// createMenu();

		frame.getContentPane().add( this );
		
		statusBar.setStatusLine("");
    }
    
//	private boolean isActive = true; // Modif Francis
	private boolean m_findHasBeenCancelled = false;
	private boolean m_finding = false;

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(APanel.class);
	
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
	private StatusBar statusBar = new StatusBar();
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

	// --------- PROGESTION - Solstice 2011.11.07 ----------
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
//		aActif = addAction("IsActive", mView, 	null,	true);
	    mView.addSeparator();
	    
	    // *** Remplacement des écrans de recherche pour ceux de la section Paie et RH
		//***************************************************************************
	    AEnv.addMenuItem("InfoEmployee", "Info", null, mView, this);
		AEnv.addMenuItem("InfoAssignment", "Info", null, mView, this);
		mView.addSeparator();
		AEnv.addMenuItem("InfoGain", "Info", null, mView, this);
		AEnv.addMenuItem("InfoDeduction", "Info", null, mView, this);
		AEnv.addMenuItem("InfoCredits", "Info", null, mView, this);
		mView.addSeparator();
		AEnv.addMenuItem("InfoJobTitle", "Info", null, mView, this);
		AEnv.addMenuItem("InfoPost", "Info", null, mView, this);
		AEnv.addMenuItem("InfoPeriod", "Info", null, mView, this);
		AEnv.addMenuItem("InfoSalaryScale", "Info", null, mView, this);
		AEnv.addMenuItem("InfoSalaryScaleDetail", "Info", null, mView, this);
		mView.addSeparator();
		AEnv.addMenuItem("InfoDistribution", "Info", null, mView, this);
		AEnv.addMenuItem("InfoDistributionBooklet", "Info", null, mView, this);
		
		/*aProduct =	addAction("InfoProduct",	mView, 	KeyStroke.getKeyStroke(KeyEvent.VK_I, Event.CTRL_MASK),	false);
		aBPartner =	addAction("InfoBPartner",	mView, 	KeyStroke.getKeyStroke(KeyEvent.VK_I, Event.SHIFT_MASK+Event.CTRL_MASK),	false);
		if (MRole.getDefault().isShowAcct())
			aAccount =  addAction("InfoAccount",	mView, 	KeyStroke.getKeyStroke(KeyEvent.VK_I, Event.ALT_MASK+Event.CTRL_MASK),	false);
		AEnv.addMenuItem("InfoSchedule", null, null, mView, this);
		mView.addSeparator();
		AEnv.addMenuItem("InfoOrder", "Info", null, mView, this);
		AEnv.addMenuItem("InfoInvoice", "Info", null, mView, this);
		AEnv.addMenuItem("InfoInOut", "Info", null, mView, this);
		AEnv.addMenuItem("InfoPayment", "Info", null, mView, this);
		AEnv.addMenuItem("InfoCashLine", "Info", null, mView, this);
		AEnv.addMenuItem("InfoAssignment", "Info", null, mView, this);
		AEnv.addMenuItem("InfoAsset", "Info", null, mView, this);
		***************************************************************************/
		
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
//		 --------- PROGESTION - Solstice 2011.11.08 ----------
		toolBar.add(aExport.getButton());
//		-----------------------------------------		
		
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
	public String getTitle() {
		return Env.getHeader(m_ctx, m_curWindowNo);
	}	//	getTitle


	private Properties      m_ctx;

	/** Dispose active                                  */
	private boolean         m_disposing = false;
	/** Save Error Message indicator                    */
	private boolean         m_errorDisplayed = false;
	/** Only current row flag                           */
	private boolean			m_onlyCurrentRows = true;
	/** Number of days to show	0=all					*/
	private int				m_onlyCurrentDays = 0;
	/** Process Info                                    */
	private boolean         m_isLocked = false;
	/** Show Personal Lock								*/
	private boolean 		m_isPersonalLock = MRole.getDefault().isPersonalLock();
	/**	Last Modifier of Action Event					*/
	private int 			m_lastModifiers;

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
	private void setBusy (boolean busy)
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
/*
		boolean startWOasking = false;
		String col = vButton.getColumnName();

		//  Zoom
		if (col.equals("Record_ID"))
		{
			int AD_Table_ID = Env.getContextAsInt (m_ctx, m_curWindowNo, "AD_Table_ID");
			int Record_ID = Env.getContextAsInt (m_ctx, m_curWindowNo, "Record_ID");
			AEnv.zoom(AD_Table_ID, Record_ID);
			return;
		}   //  Zoom

		//  save first	---------------
		if (m_curTab.needSave(true, false))
			if (!cmd_save(true))
				return;
		//
		int table_ID = m_curTab.getAD_Table_ID();
		//	Record_ID
		int record_ID = m_curTab.getRecord_ID();
		//	Record_ID - Language Handling
		if (record_ID == -1 && m_curTab.getKeyColumnName().equals("AD_Language"))
			record_ID = Env.getContextAsInt (m_ctx, m_curWindowNo, "AD_Language_ID");
		//	Record_ID - Change Log ID
		if (record_ID == -1 && vButton.getProcess_ID() == 306)
		{
			Integer id = (Integer)m_curTab.getValue("AD_ChangeLog_ID");
			record_ID = id.intValue();
		}
		//	Ensure it's saved
		if (record_ID == -1 && m_curTab.getKeyColumnName().endsWith("_ID"))
		{
			ADialog.error(m_curWindowNo, this, "SaveErrorRowNotFound");
			return;
		}

		//	Pop up Payment Rules
		if (col.equals("PaymentRule"))
		{
			VPayment vp = new VPayment(m_curWindowNo, m_curTab, vButton);
			if (vp.isInitOK())		//	may not be allowed
				vp.setVisible(true);
			vp.dispose();
			if (vp.needSave())
			{
				cmd_save(false);
				cmd_refresh();
			}
		}	//	PaymentRule

		//	Pop up Document Action (Workflow)
		else if (col.equals("DocAction"))
		{
			VDocAction vda = new VDocAction(m_curWindowNo, m_curTab, vButton, record_ID);
			//	Something to select from?
			if (vda.getNumberOfOptions() == 0)
			{
				vda.dispose ();
				log.info("DocAction - No Options");
				return;
			}
			else
			{
				vda.setVisible(true);
				if (!vda.getStartProcess())
					return;
				startWOasking = true;
				vda.dispose();
			}
		}	//	DocAction

		//  Pop up Create From
		else if (col.equals("CreateFrom"))
		{
			//  m_curWindowNo
			VCreateFrom vcf = VCreateFrom.create (m_curTab);
			if (vcf != null)
			{
				if (vcf.isInitOK())
				{
					vcf.setVisible(true);
					vcf.dispose();
					m_curTab.dataRefresh();
				}
				else
					vcf.dispose();
				return;
			}
			//	else may start process
		}	//	CreateFrom

		//  Posting -----
		else if (col.equals("Posted") && MRole.getDefault().isShowAcct())
		{
			//  Check Doc Status
			String processed = Env.getContext(m_ctx, m_curWindowNo, "Processed");
			if (!processed.equals("Y"))
			{
				ADialog.error(m_curWindowNo, this, "PostDocNotComplete");
				return;
			}

			//  Check Post Status
			Object ps = m_curTab.getValue("Posted");
			if (ps != null && ps.equals("Y"))
			{
				new org.compiere.acct.AcctViewer (Env.getContextAsInt (m_ctx, m_curWindowNo, "AD_Client_ID"),
					m_curTab.getAD_Table_ID(), m_curTab.getRecord_ID());
			}
			else
			{
				if (ADialog.ask(m_curWindowNo, this, "PostImmediate?"))
				{
					AEnv.postImmediate (m_curWindowNo, Env.getContextAsInt (m_ctx, m_curWindowNo, "AD_Client_ID"),
						m_curTab.getAD_Table_ID(), m_curTab.getRecord_ID(), true);
					m_curTab.dataRefresh();
				}
			}
			return;
		}   //  Posted

		//
		 //  Start Process ----
		 //

		log.config("Process_ID=" + vButton.getProcess_ID() + ", Record_ID=" + record_ID);
		if (vButton.getProcess_ID() == 0)
			return;
		//	Save item changed
		if (m_curTab.needSave(true, false))
			if (!cmd_save(true))
				return;

		//	Ask user to start process, if Description and Help is not empty
		if (!startWOasking && !(vButton.getDescription().equals("") && vButton.getHelp().equals("")))
			if (!ADialog.ask(m_curWindowNo, this, "StartProcess?", 
				//	"<b><i>" + vButton.getText() + "</i></b><br>" +
				vButton.getDescription() + "\n" + vButton.getHelp()))
				return;
		//
		String title = vButton.getDescription();
		if (title == null || title.length() == 0)
			title = vButton.getName();
		ProcessInfo pi = new ProcessInfo (title, vButton.getProcess_ID(), table_ID, record_ID);
		pi.setAD_User_ID (Env.getAD_User_ID(m_ctx));
		pi.setAD_Client_ID (Env.getAD_Client_ID(m_ctx));

	//	Trx trx = Trx.get(Trx.createTrxName("AppsPanel"), true);
		ProcessCtl.process(this, m_curWindowNo, pi, null); //  calls lockUI, unlockUI
		*/
	}	//	actionButton

	/**************************************************************************
	 *	Load Attachments for this table
	 */
	public void loadAttachments( int AD_Table_ID)
	{
		if (!canHaveAttachment())
			return;

		String SQL = "SELECT AD_Attachment_ID, Record_ID FROM AD_Attachment "
			+ "WHERE AD_Table_ID=?";
		try
		{
			if (m_Attachments == null)
				m_Attachments = new HashMap<Integer,Integer>();
			else
				m_Attachments.clear();
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			pstmt.setInt(1, AD_Table_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				Integer key = new Integer(rs.getInt(2));
				Integer value = new Integer(rs.getInt(1));
				m_Attachments.put(key, value);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "loadAttachments", e);
		}
		log.config("#" + m_Attachments.size());
	}	//	loadAttachment

	/**
	 *	Can this tab have Attachments?.
	 *  <p>
	 *  It can have an attachment if it has a key column ending with _ID.
	 *  The key column is empty, if there is no single identifying key.
	 *  @return true if record can have attachment
	 */
	public boolean canHaveAttachment()
	{
		return true;
	}   //	canHaveAttachment

	/**
	 *	Returns true, if current row has an Attachment
	 *  @return true if record has attchment
	 */
	public boolean hasAttachment( int AD_Table_ID, int record_ID)
	{
		if (m_Attachments == null)
			loadAttachments( AD_Table_ID);
		if (m_Attachments == null || m_Attachments.isEmpty())
			return false;
		//
		Integer key = new Integer( record_ID );
		return m_Attachments.containsKey(key);
	}	//	hasAttachment

	/**
	 *	Get Attachment_ID for current record.
	 *	@return ID or 0, if not found
	 */
	public int getAD_AttachmentID( int AD_Table_ID, int record_ID)
	{
		if (m_Attachments == null)
			loadAttachments( AD_Table_ID);
		if (m_Attachments.isEmpty())
			return 0;
		//
		Integer key = new Integer( record_ID );
		Integer value = (Integer)m_Attachments.get(key);
		if (value == null)
			return 0;
		else
			return value.intValue();
	}	//	getAttachmentID


	
	/**************************************************************************
	 *	Load Chats for this table
	 */
	public void loadChats(int AD_Table_ID)
	{
		//if (!canHaveAttachment())
		//	return;

		String sql = "SELECT CM_Chat_ID, Record_ID FROM CM_Chat "
			+ "WHERE AD_Table_ID=?";
		try
		{
			if (m_Chats == null)
				m_Chats = new HashMap<Integer,Integer>();
			else
				m_Chats.clear();
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Table_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				Integer key = new Integer(rs.getInt(2));	//	Record_ID
				Integer value = new Integer(rs.getInt(1));	//	CM_Chat_ID
				m_Chats.put(key, value);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			
			log.log(Level.SEVERE, sql, e);
		}
		log.config("#" + m_Chats.size());
	}	//	loadChats

	/**
	 *	Returns true, if current row has a Chat
	 *  @return true if record has chat
	 */
	public boolean hasChat(int AD_Table_ID, int record_ID)
	{
		if (m_Chats == null)
			loadChats(AD_Table_ID);
		if (m_Chats == null || m_Chats.isEmpty())
			return false;
		//Integer key = new Integer(m_mTable.getKeyID (m_currentRow));
		Integer key = new Integer(record_ID);
		return m_Chats.containsKey(key);
	}	//	hasChat

	/**
	 *	Get Chat_ID for this record.
	 *	@return ID or 0, if not found
	 */
	public int getCM_ChatID(int AD_Table_ID, int record_ID)
	{
		if (m_Chats == null)
			loadChats(AD_Table_ID);
		if (m_Chats.isEmpty())
			return 0;
		
		//Integer key = new Integer(m_mTable.getKeyID (m_currentRow));
		Integer key = new Integer(record_ID);
		Integer value = (Integer)m_Chats.get(key);
		if (value == null)
			return 0;
		else
			return value.intValue();
	}	//	getCM_ChatID
	

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
}
