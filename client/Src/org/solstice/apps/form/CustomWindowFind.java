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


import java.awt.*;
import java.awt.event.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import org.compiere.apps.*;
import org.solstice.apps.search.FieldType;
import org.solstice.apps.search.FindValueEditor;
import org.solstice.apps.search.AdvancedRow;
import org.compiere.grid.ed.*;
import org.compiere.model.*;
import org.compiere.plaf.CompierePLAF;
import org.compiere.swing.*;
import org.compiere.util.*;

import solstice.model.P_Payment_Group;
import solstice.utils.DisplayTypeConstants;

/** 
 *  Find/Search Records.

 */
public final class CustomWindowFind extends CDialog
		implements ActionListener, ChangeListener, DataStatusListener, ListSelectionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *	Find Constructor
	 *	@param owner Frame Dialog Onwer
	 *  @param targetWindowNo WindowNo of target window
	 *	@param title 
	 *	@param AD_Table_ID
	 *	@param tableName
	 *	@param whereExtended
	 *	@param findFields
	 *  @param minRecords number of minimum records
	 */
	public CustomWindowFind (Frame owner, int targetWindowNo, String title, int AD_Tab_ID,
		int AD_Table_ID, String tableName, String whereExtended,
		GridField[] findFields, int minRecords, int queryType )
	{
//+ Progestion Solstice - Ajout du trim
		super(owner, Msg.getMsg(Env.getCtx(), "Find").trim() + ": " + title, true);
//- Progestion		
		log.info(title);
		//

		m_targetWindowNo = targetWindowNo;
		m_AD_Tab_ID = AD_Tab_ID;
		m_AD_Table_ID = AD_Table_ID;
		m_tableName = tableName;
		m_whereExtended = whereExtended;
		m_findFields = findFields;
		
		m_QueryType = queryType;
		//
		m_query = new Query (tableName);
		m_query.addRestriction(whereExtended);
		//	Required for Column Validation
		Env.setContext(Env.getCtx(), m_targetWindowNo, "Find_Table_ID", m_AD_Table_ID);
		//  Context for Advanced Search Grid is WINDOW_FIND
		Env.setContext(Env.getCtx(), Env.WINDOW_FIND, "Find_Table_ID", m_AD_Table_ID);
		//
		try
		{
			jbInit();
			initFind();
			
			//2013.05.15
			fillQuery();
			
			if (m_total < minRecords)
			{
				dispose();
				return;
			}
			
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "Find", e);
		}
		//
		this.getRootPane().setDefaultButton(confirmPanelS.getOKButton());
		AEnv.showCenterWindow(owner, this);
	}	//	Find

	public static final int QUERYTYPE_TimeSheet = 0;
	public static final int QUERYTYPE_Earning = 1;
	public static final int QUERYTYPE_Deduction = 2;
	public static final int QUERYTYPE_Taxable_Benefit = 3;
	public static final int QUERYTYPE_AccrualBank = 4;
	public static final int QUERYTYPE_AccrualBankWithMonetaryValue = 5;
	public static final int QUERYTYPE_GeneralLedger = 6;
	public static final int QUERYTYPE_LstAccrualBankInfo = 7;
	public static final int QUERYTYPE_VDeductionMandatoryEstimate = 8;
	public static final int QUERYTYPE_Employee = 9;
	public static final int QUERYTYPE_GeneralLedger360 = 10;
	
	
	/**	Cache						*/
	private static CCache<String,Object>	s_cache = new CCache<String,Object>("CustomWindowFind", 20);

	private Properties				m_ctx = Env.getCtx();
	
	/** Target Window No            */
	private int				m_targetWindowNo;
	/** The Tab						*/
	private int				m_AD_Tab_ID;
	/**	Table ID					*/
	private int				m_AD_Table_ID;
	/** Table Name					*/
	private String			m_tableName;
	/** Where						*/
	private String			m_whereExtended;
	/** Search Fields          		*/
	private GridField[]		m_findFields;
	/** Button Fields				*/
	private ArrayList<Integer> m_buttonFields = new ArrayList<Integer>();

	/** Resulting query             */
	private Query			m_query = null;
	/**	History Days (0=all)		*/
	private int 			m_days = 0;
	/**	History Created Flag		*/
	private boolean			m_created = true;

	private int             m_QueryType = 0;
	
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(CustomWindowFind.class);
	
	/** Number of records			*/
	private int				m_total;
	private PreparedStatement	m_pstmt;
	//
	private	boolean			hasValue = false;
	private boolean			hasDocNo = false;
	private	boolean			hasName = false;
	private	boolean			hasDescription = false;
	/**	Line in Simple Content		*/
	private int				m_sLine = 6;
	
	/**	List of VEditors			*/
	private ArrayList<VEditor>			m_sEditors = new ArrayList<VEditor>();
	private ArrayList<VComboBox>			m_sComboBox = new ArrayList<VComboBox>();
	/** Target Fields with AD_Column_ID as key  */
	private Hashtable<Integer,GridField>	m_targetFields = new Hashtable<Integer,GridField>();

	/**	For Grid Controller			*/
	public static final int		TABNO = 99;
	/** Length of Fields on first tab	*/
	public static final int		FIELDLENGTH = 20;
	
	//
	private CPanel southPanel = new CPanel();
	private BorderLayout southLayout = new BorderLayout();
	private StatusBar statusBar = new StatusBar();
	private CTabbedPane tabbedPane = new CTabbedPane();
//	private CPanel advancedPanel = new CPanel();
//	private BorderLayout advancedLayout = new BorderLayout();
	private ConfirmPanel confirmPanelA = new ConfirmPanel(true, true, false, false, false, false, true);

	
	//2011.07.26
	private CPanel advancedSavedQueries = new CPanel(new GridBagLayout());
	private CComboBox comboQueriesA = new CComboBox();

	private CButton bDeleteQuery = new CButton();//CButton.getSmall("Delete");

	private Vector<AdvancedRow> advancedData = new Vector<AdvancedRow>();
	private CPanel advancedEntry = new CPanel (new GridBagLayout());
	/** Index ColumnName = 0		*/
	public static final int		INDEX_COLUMNNAME = 0;
	/** Index Operator = 1			*/
	public static final int		INDEX_OPERATOR = 1;
	/** Index Value = 2				*/
	public static final int		INDEX_VALUE = 2;
	/** Index Value2 = 3			*/
	public static final int		INDEX_VALUE2 = 3;

	private FindValueEditor	valueEditor = null;
	private FindValueEditor	valueEditor2 = null;
	private CButton bDeleteRow = new CButton();//CButton.getSmall("Delete");
	private CButton bSaveRow = new CButton();// CButton.getSmall("Save");
	private CTextField savedQueryName = new CTextField(15);


	
	private CButton bIgnore = new CButton();
	private JToolBar toolBar = new JToolBar();
	private CTextField fQueryName = new CTextField(20);
	private CButton bSave = new CButton();
	private CButton bDelete = new CButton();
	private ConfirmPanel confirmPanelS = new ConfirmPanel(true);
	private BorderLayout simpleLayout = new BorderLayout();
	
	private CPanel scontentPanel = new CPanel(new GridBagLayout());

//	private CPanel scontentPanel = new CPanel();
	
	private GridBagLayout scontentLayout = new GridBagLayout();
	private CPanel simplePanel = new CPanel(new BorderLayout());

//	private CPanel simplePanel = new CPanel();
	private CLabel valueLabel = new CLabel();
	private CLabel nameLabel = new CLabel();
	private CLabel descriptionLabel = new CLabel();
	private CTextField valueField = new CTextField();
	private CTextField nameField = new CTextField();
	private CTextField descriptionField = new CTextField();
	private CLabel docNoLabel = new CLabel();
	private CTextField docNoField = new CTextField();
	
	//
	private CPanel  historyPanel = new CPanel();
	private CButton bShowAll = new CButton();
	private CButton bShowMonth = new CButton();
	private CButton bShowWeek = new CButton();
	private CButton bShowDay = new CButton();
	private CButton bShowYear = new CButton();
	private CCheckBox cbCreated = new CCheckBox(Msg.getMsg(m_ctx, "Created"), true);
	private CComboBox comboQueriesS = new CComboBox();

	private CCheckBox cbMonetaryValue = new CCheckBox(Msg.getMsg(m_ctx, "MonetaryValue"), true);

	
	private Component spaceE;
	private Component spaceN;
	private Component spaceW;
	private Component spaceS;
	private JScrollPane advancedScrollPane = new JScrollPane();

	private CPanel advancedPanel = new CPanel(new BorderLayout());


	public CPanel getSouthPanel() { return southPanel; }

	
	protected CTable advancedTable = new CTable()
    {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public boolean isCellEditable(int row, int column)
        {
            boolean editable = column == INDEX_COLUMNNAME;
            if (!editable)
            {
                String columnName = null;
                Object value =
                    getModel().getValueAt(row, INDEX_COLUMNNAME);
                if (value != null)
                    columnName = ((ValueNamePair)value).getValue();
    
                //  Create Editor
                editable = getTargetMField(columnName) != null;
            }
            return editable;
        }
    };

	/**	Advanced Search Column 		*/
	public CComboBox 	columns = null;
	/**	Advanced Search Operators 	*/
	public CComboBox 	operators = null;

	/**
	 *	Static Init.
	 *  <pre>
	 *  tabbedPane
	 *      simplePanel
	 *          scontentPanel
	 *          confirmPanelS
	 *      advancedPanel
	 *          toolBar
	 *          GC
	 *          confirmPanelA
	 *  southPanel
	 *      statusBar
	 *  </pre>
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		spaceE = Box.createHorizontalStrut(8);
		spaceN = Box.createVerticalStrut(8);
		spaceW = Box.createHorizontalStrut(8);
		spaceS = Box.createVerticalStrut(8);
		bIgnore.setIcon(new ImageIcon(org.compiere.Compiere.class.getResource("images/Ignore24.gif")));
		bIgnore.setMargin(new Insets(2, 2, 2, 2));
		bIgnore.setToolTipText(Msg.getMsg(Env.getCtx(),"Ignore"));
		bIgnore.addActionListener(this);
		fQueryName.setToolTipText (Msg.getMsg(Env.getCtx(),"QueryName"));
		bSave.setIcon(new ImageIcon(org.compiere.Compiere.class.getResource("images/Save24.gif")));
		bSave.setMargin(new Insets(2, 2, 2, 2));
		bSave.setToolTipText(Msg.getMsg(Env.getCtx(),"Save"));
		bSave.addActionListener(this);
		bDelete.setIcon(new ImageIcon(org.compiere.Compiere.class.getResource("images/Delete24.gif")));
		bDelete.setMargin(new Insets(2, 2, 2, 2));
		bDelete.setToolTipText(Msg.getMsg(Env.getCtx(),"Delete"));
		bDelete.addActionListener(this);

		bDeleteQuery.setIcon(new ImageIcon(org.compiere.Compiere.class.getResource("images/Delete24.gif")));
		bDeleteQuery.setMargin(new Insets(2, 2, 2, 2));
		bDeleteQuery.setToolTipText(Msg.getMsg(Env.getCtx(),"Delete"));
		bDeleteRow.setIcon(new ImageIcon(org.compiere.Compiere.class.getResource("images/Delete24.gif")));
		bDeleteRow.setMargin(new Insets(2, 2, 2, 2));
		bDeleteRow.setToolTipText(Msg.getMsg(Env.getCtx(),"Delete"));
		bSaveRow.setIcon(new ImageIcon(org.compiere.Compiere.class.getResource("images/Save24.gif")));
		bSaveRow.setMargin(new Insets(2, 2, 2, 2));
		bSaveRow.setToolTipText(Msg.getMsg(Env.getCtx(),"Save"));



		//
		southPanel.setLayout(southLayout);
		valueLabel.setLabelFor(valueField);
		valueLabel.setText(Msg.translate(Env.getCtx(),"Value"));
		nameLabel.setLabelFor(nameField);
		nameLabel.setText(Msg.translate(Env.getCtx(),"Name"));
		descriptionLabel.setLabelFor(descriptionField);
		descriptionLabel.setText(Msg.translate(Env.getCtx(),"Description"));
		valueField.setText("%");
		valueField.setColumns(FIELDLENGTH);
		nameField.setText("%");
		nameField.setColumns(FIELDLENGTH);
		descriptionField.setText("%");
		descriptionField.setColumns(FIELDLENGTH);
		scontentPanel.setToolTipText(Msg.getMsg(Env.getCtx(),"FindTip"));
		docNoLabel.setLabelFor(docNoField);
		docNoLabel.setText(Msg.translate(Env.getCtx(),"DocumentNo"));
		docNoField.setText("%");
		docNoField.setColumns(FIELDLENGTH);
		advancedScrollPane.setPreferredSize(new Dimension(450, 150));
		southPanel.add(statusBar, BorderLayout.SOUTH);
		this.getContentPane().add(southPanel, BorderLayout.SOUTH);
		//
		scontentPanel.setLayout(scontentLayout);
		simplePanel.setLayout(simpleLayout);
		simplePanel.add(confirmPanelS, BorderLayout.SOUTH);
		simplePanel.add(scontentPanel, BorderLayout.CENTER);
		scontentPanel.add(valueLabel,      new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(7, 5, 0, 5), 0, 0));
		scontentPanel.add(nameLabel,    new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(7, 5, 0, 5), 0, 0));
		scontentPanel.add(descriptionLabel,    new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
			,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(7, 5, 5, 5), 0, 0));
		scontentPanel.add(valueField,    new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 5), 0, 0));
		scontentPanel.add(descriptionField,    new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
		scontentPanel.add(docNoLabel,    new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(7, 5, 0, 5), 0, 0));
		scontentPanel.add(nameField,    new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 5), 0, 0));
		scontentPanel.add(docNoField,    new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 5), 0, 0));
		//
		scontentPanel.add(spaceE,    new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
		scontentPanel.add(spaceN,   new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
		scontentPanel.add(spaceW,  new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
		scontentPanel.add(spaceS,  new GridBagConstraints(2, 15, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
		//
	//	tabbedPane.add(simplePanel, Msg.getMsg(Env.getCtx(),"Find"));
		tabbedPane.add(simplePanel, Msg.getMsg(Env.getCtx(),"Find"));
		//
		toolBar.add(bIgnore, null);
		toolBar.addSeparator();
		toolBar.add(fQueryName, null);
		toolBar.add(bSave, null);
		toolBar.add(bDelete, null);
/*		
		advancedPanel.setLayout(advancedLayout);
		advancedPanel.add(toolBar, BorderLayout.NORTH);
		advancedPanel.add(confirmPanelA, BorderLayout.SOUTH);
		advancedPanel.add(advancedScrollPane, BorderLayout.CENTER);
		advancedScrollPane.getViewport().add(advancedTable, null);
		tabbedPane.add(advancedPanel, Msg.getMsg(Env.getCtx(),"Advanced"));
		//
		this.getContentPane().add(tabbedPane, BorderLayout.CENTER);
*/
		//	Advanced
		CLabel lcomboQueriesA = new CLabel(Msg.getMsg(m_ctx, "GetSavedQuery"));
		CLabel lsavedQueryName = new CLabel(Msg.getMsg(m_ctx, "SaveQuery"));
		savedQueryName.setReadWrite(false);
		advancedSavedQueries.add(lcomboQueriesA, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		advancedSavedQueries.add(comboQueriesA, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		bDeleteQuery.setToolTipText(Msg.getMsg(m_ctx, "QueryDelete"));
		bDeleteQuery.addActionListener(this);
		bDeleteQuery.setEnabled(false);
		advancedSavedQueries.add(bDeleteQuery, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		advancedSavedQueries.add(lsavedQueryName, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 10, 2, 2), 0, 0));
		advancedSavedQueries.add(savedQueryName, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		//
		CPanel advancedCenter = new CPanel(new BorderLayout()); 
		advancedScrollPane.setPreferredSize(new Dimension(250, 150));
		advancedCenter.add(advancedScrollPane, BorderLayout.NORTH);
		advancedCenter.add(advancedEntry, BorderLayout.CENTER);
		//
		advancedPanel.add(advancedSavedQueries, BorderLayout.NORTH);
		advancedPanel.add(advancedCenter, BorderLayout.CENTER);
		advancedPanel.add(confirmPanelA, BorderLayout.SOUTH);
		advancedTable.getSelectionModel().addListSelectionListener(this);
		advancedScrollPane.getViewport().add(advancedTable, null);
		tabbedPane.add(advancedPanel, Msg.getMsg(m_ctx,"Advanced"));
		//
		this.getContentPane().add(tabbedPane, BorderLayout.NORTH);
		//
		confirmPanelA.addActionListener(this);
		confirmPanelS.addActionListener(this);
		//

		//
		confirmPanelA.addActionListener(this);
		confirmPanelS.addActionListener(this);
		//
		JButton b = ConfirmPanel.createNewButton(true);
		confirmPanelS.addComponent (b);
		b.addActionListener(this);
		


	}	//	jbInit

	public int m_curWindowNo;
    public CLabel labelYear = new CLabel();
    public CLabel labelPeriodFrom = new CLabel();
    public CLabel labelPeriodTo = new CLabel();

    public CLabel labelPeriodGl = new CLabel();

    
    public CLabel labelDateFrom = new CLabel();
    public CLabel labelDateTo = new CLabel();
    public CLabel labelEvaluationDate = new CLabel();
    public CLabel labelBenefitsRate1 = new CLabel();  // Vacance
    public CLabel labelBenefitsRate2 = new CLabel();  // Maladie
    public CLabel labelBenefitsRate3 = new CLabel();  // Temps Sup
    
    public CLabel labelEmployee = new CLabel();
    public CLabel labelEmployer = new CLabel();
	public CLabel labelTaxable_Benefit = new CLabel();
    public CLabel labelSelectionDateFrom = new CLabel();
    public CLabel labelSelectionDateTo = new CLabel();
    public CLabel labelPaymentGroup = new CLabel();
    public CLabel labelActivity = new CLabel();
    public CLabel labelClient = new CLabel();
    public CLabel labelGroupByOption = new CLabel();
    public CLabel labelOrg = new CLabel();
    public CLabel labelDepartment = new CLabel();
    public CLabel labelOccupationGroup = new CLabel();
    public CLabel labelCalendar = new CLabel();
	public CLabel labelDeduction = new CLabel();
	public CLabel labelDeductionCategory = new CLabel();
	public CLabel labelDeductionFamily = new CLabel();

	public CLabel labelElementValue = new CLabel();
	public CLabel labelEarning = new CLabel();
	public CLabel labelEarningInfo = new CLabel();
	public CLabel labelColumnAdm  = new CLabel();
	public CLabel labelCredits = new CLabel();
	public CLabel labelTaxationRegion  = new CLabel();
	public CLabel labelWorkPlace  = new CLabel();

	public CLabel labelDistributionBooklet  = new CLabel();
	public CLabel labelDistribution  = new CLabel();
	public CLabel labelLongTermLeave  = new CLabel();
	public CLabel labelEmployeeStatus  = new CLabel();
	public CLabel labelJobType  = new CLabel();
	public CLabel labelJobTitle  = new CLabel();

	public CLabel labelMovementType  = new CLabel();
	public CLabel labelMovementOrigin  = new CLabel();
	
	public CLabel labelTimeSheetFilter = new CLabel();
	public CLabel labelCreditsFamily = new CLabel();
	public CLabel labelSheetType = new CLabel();
	
    public CLabel labelCollectiveLabour = new CLabel();


	
//    public VLookup fieldPeriodFrom;
//    public VLookup fieldPeriodTo;
//	public VComboBox m_cbbPaymentGroup = new VComboBox();;
    public VComboBox m_cbbPayYear = new VComboBox();
    public VComboBox m_cbbPayPeriodFrom = new VComboBox();
    public VComboBox m_cbbPayPeriodTo = new VComboBox();
//    public VComboBox m_cbbGroupPayment = new VComboBox();

    public VLookup m_cbbPeriodGl ;

    
    public VComboBox cbbGroupByOption = new VComboBox();

    public VLookup fieldTimeSheetStatus;
    public VLookup fieldSheetType;
    public VLookup fieldActivity;
    public VLookup fieldClient ;
    public VLookup fieldOrg ;
    public VLookup fieldDepartment;
    public VLookup fieldOccupationGroup;
    public VLookup fieldWorkplace;
	public VLookup fieldEmployee;
	public VLookup fieldEmployer;
	public VComboBox fieldCalendar;
	public VLookup fieldTaxable_Benefit;
	public VLookup fieldDeduction;
	public VLookup fieldElementValue;
	public VLookup fieldEarning;
	public VLookup fieldEarningInfo;
	public VLookup fieldColumnAdm;
	public VLookup fieldCredits;
	public VLookup fieldTaxationRegion;
	public VLookup fieldWorkPlace;

	public VLookup fieldDistributionBooklet;
	public VLookup fieldDistribution;
	public VLookup fieldJobType;
	public VLookup fieldJobTitle;
	public VLookup fieldPaymentGroup;
	public VLookup fieldLongTermLeave;
	
	public VLookup fieldCollectiveLabour;
	public VLookup fieldDeductionCategory;
	public VLookup fieldDeductionFamily;


	public VCheckBox checkIsActive = new VCheckBox();
	
	public VCheckBox isSearchPeriod = new VCheckBox();
	public VCheckBox isSearchDate = new VCheckBox();
	public VLookup fieldEmployeeStatus;
	
	public VCheckBox isSearchPeriodPayment = new VCheckBox();

	
	
	public VLookup fieldMovementType ;
	public VLookup fieldMovementOrigin;

    public VDate fieldSelectionDateFrom  = new VDate();
    public VDate fieldSelectionDateTo    = new VDate();

    public VDate fieldEvaluationDate = new VDate();
    public VNumber fieldBenefitsRate1 = new VNumber();  //Vacance 
    public VNumber fieldBenefitsRate2 = new VNumber();  //Maladie
    public VNumber fieldBenefitsRate3 = new VNumber();  //Temps Supp.
	public VCheckBox isRefreshData = new VCheckBox();
	public VComboBox fieldCreditsFamily;

    /**
	 *	Dynamic Init.6
	 *  Set up GridController
	 */
    
	
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
    
	//2013.05.15 add method
    /**
	 * 	Fill Query
	 *	@return true if queries exist
	 */
	private boolean fillQuery()
	{
		MUserQuery[] queries = MUserQuery.get (m_ctx, m_AD_Tab_ID, m_AD_Table_ID);
		if (queries.length == 0)
			return false;
		//
		Vector<ValueNamePair> vectorS = new Vector<ValueNamePair>();
		ValueNamePair pp = new ValueNamePair("", Msg.getMsg (m_ctx, "SelectSavedQuery"));
		vectorS.add(pp);
		for (MUserQuery query : queries) {
			pp = new ValueNamePair(query.getCode(), query.getName());
			vectorS.add(pp);
		}
		ComboBoxModel modelS = new DefaultComboBoxModel(vectorS);
		comboQueriesS.setModel(modelS);
		comboQueriesS.setToolTipText (Msg.getMsg(m_ctx,"QueryName"));
		comboQueriesS.addActionListener(this);
		//
		Vector<MUserQuery> vectorA = new Vector<MUserQuery>();
		vectorA.add(null);
		for (MUserQuery element : queries)
			vectorA.add(element);
		ComboBoxModel modelA = new DefaultComboBoxModel(vectorA);
		comboQueriesA.setModel(modelA);
		comboQueriesA.setToolTipText (Msg.getMsg(m_ctx,"QueryName"));
		comboQueriesA.setEditable(true);
		comboQueriesA.addActionListener(this);
		return true;
	}	//	fillQuery


	private void initFind()
	{
		
		log.config("");
/*		
		//	Get Info from target Tab
		for (GridField mField : m_findFields) {
			if (mField.isEncrypted())
				continue;
			if (!mField.isDisplayed())
				continue;
			String columnName = mField.getColumnName();

			if (columnName.equals("Value"))
				hasValue = true;
			else if (columnName.equals("Name"))
				hasName = true;
			else if (columnName.equals("DocumentNo"))
				hasDocNo = true;
			else if (columnName.equals("Description"))
				hasDescription = true;
			else if (mField.isSelectionColumn())
				addSelectionColumn (mField);
			else if (columnName.indexOf("Name") != -1)
				addSelectionColumn (mField);

			//  TargetFields
			m_targetFields.put (Integer.valueOf(mField.getAD_Column_ID()), mField);
		}   //  for all target tab fields
	*/	

		//	Disable simple query fields
		valueLabel.setVisible(hasValue);
		valueField.setVisible(hasValue);
		if (hasValue)
			valueField.addActionListener(this);
		docNoLabel.setVisible(hasDocNo);
		docNoField.setVisible(hasDocNo);
		if (hasDocNo)
			docNoField.addActionListener(this);
		nameLabel.setVisible(hasName);
		nameField.setVisible(hasName);
		if (hasName)
			nameField.addActionListener(this);
		descriptionLabel.setVisible(hasDescription);
		descriptionField.setVisible(hasDescription);
		if (hasDescription)
			descriptionField.addActionListener(this);

		//	Get Total
		m_total = getNoOfRecords(null, false);
		setStatusDB (m_total);
		statusBar.setStatusLine("");

/*
		VLookup vl;
		CLabel label = new CLabel(); //VEditorFactory.getLabel(mField);
		VEditor editor = null;
*/

		labelSelectionDateFrom.setText(Msg.translate(Env.getCtx(), "DateFrom"));
		labelSelectionDateTo.setText(Msg.translate(Env.getCtx(), "DateTo"));

		fieldSelectionDateFrom.setName( "SelectionDateFrom" );
		
//		fieldSelectionDateFrom.setToolTipText( Msg.translate(Env.getCtx(), "SelectionDateFrom") );
		fieldSelectionDateTo.setName( "SelectionDateTo" );
//		fieldSelectionDateTo.setToolTipText( Msg.translate(Env.getCtx(), "SelectionDateTo") );


		labelYear.setText(Msg.translate(Env.getCtx(), "P_Year_ID"));

		labelPeriodFrom.setText(Msg.translate(Env.getCtx(), "SelectionDateFrom"));
		labelPeriodTo.setText(Msg.translate(Env.getCtx(), "SelectionDateTo"));

		labelPeriodGl.setText(Msg.translate(Env.getCtx(), "C_Period_ID"));

		labelDateFrom.setText(Msg.translate(Env.getCtx(), "DateFrom"));
		labelDateTo.setText(Msg.translate(Env.getCtx(), "DateTo"));
    	
		labelEarning.setText(Msg.translate(Env.getCtx(), "P_Gain_ID"));
		labelEarningInfo.setText(Msg.translate(Env.getCtx(), "P_GainInfo_ID"));
		labelColumnAdm.setText(Msg.translate(Env.getCtx(), "P_Column_Adm_ID"));
    	labelCredits.setText(Msg.translate(Env.getCtx(), "P_Credits_ID"));
       	labelDeduction.setText(Msg.translate(Env.getCtx(), "P_Deduction_ID"));
       	labelElementValue.setText(Msg.translate(Env.getCtx(), "C_ElementValue_ID"));
		labelTaxable_Benefit.setText(Msg.translate(Env.getCtx(), "P_Taxable_Benefit_ID"));
		labelEmployee.setText(Msg.translate(Env.getCtx(), "P_Employee_ID"));
		labelCalendar.setText(Msg.translate(Env.getCtx(), "P_Calendar_ID"));
		labelCreditsFamily.setText(Msg.translate(Env.getCtx(), "P_Credits_Family_ID"));
		labelPaymentGroup.setText(Msg.translate(Env.getCtx(), "P_Payment_Group_ID"));
		
		labelTimeSheetFilter.setText(Msg.translate(Env.getCtx(), "TimeSheetStatus"));
		labelSheetType.setText(Msg.translate(Env.getCtx(), "SheetType"));
		
		labelActivity.setText(Msg.translate(Env.getCtx(), "C_activity_ID"));
		labelClient.setText(Msg.translate(Env.getCtx(), "AD_Client_ID"));
		labelGroupByOption.setText("Type de regroupement");
		labelOrg.setText(Msg.translate(Env.getCtx(), "AD_Org_ID"));
		labelDepartment.setText(Msg.translate(Env.getCtx(), "P_Department_ID"));
		labelTaxationRegion.setText(Msg.translate(Env.getCtx(), "Taxation_Region_ID"));
		labelWorkPlace.setText(Msg.translate(Env.getCtx(), "P_WorkPlace_ID"));
		labelOccupationGroup.setText(Msg.translate(Env.getCtx(), "P_Occupation_Group_ID"));
		labelEmployer.setText(Msg.translate(Env.getCtx(), "P_Employer_ID"));

		labelDistributionBooklet.setText(Msg.translate(Env.getCtx(), "P_Distribution_Booklet_ID"));
		labelDistribution.setText(Msg.translate(Env.getCtx(), "P_Distribution_ID"));
		labelJobType.setText(Msg.translate(Env.getCtx(), "P_Job_Type_ID"));
		labelJobTitle.setText(Msg.translate(Env.getCtx(), "P_Job_Title_ID"));

		if ( m_QueryType == QUERYTYPE_VDeductionMandatoryEstimate )
			if ( s_cache.get(isRefreshData.getName() ) != null)      isRefreshData.setSelected( s_cache.get(isRefreshData.getName() ).toString().equals("true"));

		if ( m_QueryType == QUERYTYPE_LstAccrualBankInfo )
		{
			labelEvaluationDate.setText( Msg.translate( Env.getCtx(), "EvaluationDate"));
			labelBenefitsRate1.setText(  "Taux bénéfits marginaux vacance " );
			labelBenefitsRate2.setText(  "Taux bénéfits marginaux maladie" );
			labelBenefitsRate3.setText(  "Taux bénéfits marginaux temps sup." );
	    	fieldBenefitsRate1.setValue( 29 );
	    	fieldBenefitsRate2.setValue( 18 );
	    	fieldBenefitsRate3.setValue( 29 );
		}
		

		labelLongTermLeave.setText(Msg.translate(Env.getCtx(), "P_LongTermLeave_ID"));
		labelEmployeeStatus.setText(Msg.translate(Env.getCtx(), "EmployeeStatus"));
       	labelDeductionCategory.setText(Msg.translate(Env.getCtx(), "P_Deduction_Category_ID"));
       	labelDeductionFamily.setText(Msg.translate(Env.getCtx(), "P_Deduction_Family_ID"));
		labelCollectiveLabour.setText(Msg.translate(Env.getCtx(), "P_Collective_Labour_Agr_ID"));

		labelMovementType.setText(Msg.translate(Env.getCtx(), "PMovementType"));
		labelMovementOrigin.setText(Msg.translate(Env.getCtx(), "PMovementOrigin"));
		
		MLookup ClientL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, 527, DisplayType.Table);
		fieldClient = new VLookup ("AD_Client_ID", true, false, true, ClientL);
		fieldClient.setBounds(206, 20, 210, 19);

		MLookup OrganisationL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, 528, DisplayType.Table);
		fieldOrg = new VLookup ("AD_Org_ID", false, false, true, OrganisationL);
		fieldOrg.setBounds(206, 20, 210, 19);

		if ( m_QueryType == QUERYTYPE_GeneralLedger || m_QueryType == QUERYTYPE_GeneralLedger360 )
		{
			MLookup CPeriodL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, 2228730, DisplayType.Table);
			m_cbbPeriodGl = new VLookup ("C_Period_ID", false, false, true, CPeriodL);
			m_cbbPeriodGl.setBounds(206, 20, 210, 19);

			
			
		}
		
		if ( m_QueryType == QUERYTYPE_TimeSheet )
		{
			MLookup TimeSheetStatusL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, getAD_Column_ID("TimeSheetStatus"), DisplayType.List);
			fieldTimeSheetStatus = new VLookup ("TimeSheetStatus", false, false, true, TimeSheetStatusL);
			fieldTimeSheetStatus.setBounds(206, 20, 210, 19);

			MLookup SheetTypeL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, getAD_Column_ID("PaymentTypeDoc"), DisplayType.List);
			fieldSheetType = new VLookup ("SheetType", false, false, true, SheetTypeL);
			fieldSheetType.setBounds(206, 20, 210, 19);

		}

		if ( m_QueryType == QUERYTYPE_TimeSheet )
		{
//			MLookup periodL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, 2111061, DisplayType.Table);
//			fieldPeriodFrom = new VLookup ("P_Period_ID", true, false, true, periodL);
//			fieldPeriodFrom.setBounds(206, 20, 210, 19);
			
//			MLookup periodL2 = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, 2111061, DisplayType.Table);
//			fieldPeriodTo = new VLookup ("P_Period_ID", true, false, true, periodL2);
//			fieldPeriodTo.setBounds(206, 20, 210, 19);
			
		}



		MLookup DepartmentL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, getAD_Column_ID("P_Department_ID"), DisplayType.Table);
		fieldDepartment = new VLookup ("P_Department_ID", false, false, true, DepartmentL);
		fieldDepartment.setBounds(206, 20, 210, 19);

		MLookup TaxationRegionL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, getAD_Column_ID("Taxation_Region_ID"), DisplayType.Table);
		fieldTaxationRegion = new VLookup ("Taxation_Region_ID", false, false, true, TaxationRegionL);
		fieldTaxationRegion.setBounds(206, 20, 210, 19);

		MLookup WorkPlaceL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, getAD_Column_ID("P_WorkPlace_ID"), DisplayType.Table);
		fieldWorkPlace = new VLookup ("P_WorkPlace_ID", false, false, true, WorkPlaceL);
		fieldWorkPlace.setBounds(206, 20, 210, 19);

		MLookup JobTitleL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, getAD_Column_ID("P_Job_Title_ID"), DisplayType.Table);
		fieldJobTitle = new VLookup ("P_Job_Title_ID", false, false, true, JobTitleL);
		fieldJobTitle.setBounds(206, 20, 210, 19);

		MLookup JobTypeL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, getAD_Column_ID("P_Job_Type_ID"), DisplayType.Table);
		fieldJobType = new VLookup ("P_Job_Type_ID", false, false, true, JobTypeL);
		fieldJobType.setBounds(206, 20, 210, 19);

		MLookup PaymentGroupL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, getAD_Column_ID("P_Payment_Group_ID"), DisplayType.Table);
		fieldPaymentGroup = new VLookup ("P_Payment_Group_ID", false, false, true, PaymentGroupL);
		fieldPaymentGroup.setBounds(206, 20, 210, 19);

		MLookup CollectiveLabourL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, getAD_Column_ID("P_Collective_Labour_Agr_ID"), DisplayType.Table);
		fieldCollectiveLabour = new VLookup ("P_Collective_Labour_Agr_ID", false, false, true, CollectiveLabourL);
		fieldCollectiveLabour.setBounds(206, 20, 210, 19);
		
		MLookup DistributionL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, getAD_Column_ID("P_Distribution_ID"), DisplayType.Table);
		fieldDistribution = new VLookup ("P_Distribution_ID", false, false, true, DistributionL);
		fieldDistribution.setBounds(206, 20, 210, 19);

		MLookup DistributionBookletL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, getAD_Column_ID("P_Distribution_Booklet_ID"), DisplayType.Table);
		fieldDistributionBooklet = new VLookup ("P_Distribution_Booklet_ID", false, false, true, DistributionBookletL);
		fieldDistributionBooklet.setBounds(206, 20, 210, 19);
		
		MLookup OccupationGroupL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, getAD_Column_ID("P_Occupation_Group_ID"), DisplayType.Table);
		fieldOccupationGroup = new VLookup ("P_Occupation_Group_ID", false, false, true, OccupationGroupL);
		fieldOccupationGroup.setBounds(206, 20, 210, 19);

		MLookup EmployerL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, getAD_Column_ID("P_Employer_ID"), DisplayType.Table);
		fieldEmployer = new VLookup ("P_Employer_ID", false, false, true, EmployerL);
		fieldEmployer.setBounds(206, 20, 210, 19);

		
		MLookup ActivityL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, getAD_Column_ID("C_Activity_ID"), DisplayType.Table);
		fieldActivity = new VLookup ("C_Activity_ID", false, false, true, ActivityL);
		fieldActivity.setBounds(206, 20, 210, 19);

		MLookup Taxable_BenefitL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, 2111822, DisplayType.Table);
		fieldTaxable_Benefit = new VLookup ("P_Taxable_Benefit_ID", false, false, true, Taxable_BenefitL);
		fieldTaxable_Benefit.setBounds(206, 20, 210, 19);

		MLookup EarningL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, 2110846, DisplayType.Table);
		fieldEarning = new VLookup ("P_Gain_ID", false, false, true, EarningL);
		fieldEarning.setBounds(206, 20, 210, 19);

		MLookup EarningLInfo = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, 2229054, DisplayType.Table);
		fieldEarningInfo = new VLookup ("P_GainInfo_ID", false, false, true, EarningLInfo);
		fieldEarningInfo.setBounds(206, 20, 210, 19);

		MLookup ColumnAdmL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, 2110908, DisplayType.Table);
		fieldColumnAdm = new VLookup ("P_Column_Adm_ID", false, false, true, ColumnAdmL);
		fieldColumnAdm.setBounds(206, 20, 210, 19);

		if (  m_QueryType == QUERYTYPE_Deduction || m_QueryType == QUERYTYPE_VDeductionMandatoryEstimate ) // Earning
		{
			MLookup DeductionL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, 2213720, DisplayType.Table);
			fieldDeduction = new VLookup ("P_Deduction_ID", false, false, true, DeductionL);
			fieldDeduction.setBounds(206, 20, 210, 19);

			MLookup DeductionCategoryL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, 2228981, DisplayType.Table);
			fieldDeductionCategory = new VLookup ("P_Deduction_Category_ID", false, false, true, DeductionCategoryL);
			fieldDeductionCategory.setBounds(206, 20, 210, 19);		

			MLookup DeductionFamilyL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, 2213752, DisplayType.Table);
			fieldDeductionFamily = new VLookup ("P_Deduction_Family_ID", false, false, true, DeductionFamilyL);
			fieldDeductionFamily.setBounds(206, 20, 210, 19);		

		}


		MLookup ElementValueL;
		if ( m_QueryType == QUERYTYPE_GeneralLedger360 )
		{
			ElementValueL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, 3345389, 2200494);
			
		}
		else
		{
			ElementValueL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, 2227230, 2200495);
		}
		
		fieldElementValue = new VLookup ("C_ElementValue_ID", false, false, true, ElementValueL);
		fieldElementValue.setBounds(206, 20, 210, 19);

		MLookup CreditsL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, 2111635, DisplayType.Table);
		fieldCredits = new VLookup ("P_Credits_ID", false, false, true, CreditsL);
		fieldCredits.setBounds(206, 20, 210, 19);

		MLookup LongTermLeaveL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, getAD_Column_ID("P_LongTermLeave_ID"), DisplayType.Table);
		fieldLongTermLeave = new VLookup ("P_LongTermLeave_ID", false, false, true, LongTermLeaveL);
		fieldLongTermLeave.setBounds(206, 20, 210, 19);
	

		MLookup EmployeeStatusL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, 2232024, DisplayType.List);
		fieldEmployeeStatus = new VLookup ("EmployeeStatus", false, false, true, EmployeeStatusL);
		fieldEmployeeStatus.setBounds(206, 20, 210, 19);
 
		if ( m_QueryType == QUERYTYPE_AccrualBank )
		{
    		MLookup MovementTypeL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, 2228775, DisplayType.List);
	    	fieldMovementType = new VLookup ("PMovementType", false, false, true, MovementTypeL);
    		fieldMovementType.setBounds(206, 20, 210, 19);
	
	    	MLookup MovementOriginL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, 2213144, DisplayType.List);
    		fieldMovementOrigin = new VLookup ("PMovementOrigin", false, false, true, MovementOriginL);
	    	fieldMovementOrigin.setBounds(206, 20, 210, 19);
		}
		
		checkIsActive.setBackground(CompierePLAF.getInfoBackground());
		checkIsActive.setSelected(false);
		checkIsActive.setText(Msg.translate(Env.getCtx(), "IsActive"));
		
		MLookup empL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, 2111113, DisplayType.Search);
		fieldEmployee = new VLookup ("P_Employee_ID", false, false, true, empL);
		fieldEmployee.setBounds(206, 20, 210, 19);

		fieldPaymentGroup.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
            	fillCalendarComboBox();
            }
        });
		/*
		MLookup calendarL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, 2111015, DisplayType.Table);
		fieldCalendar = new VLookup ("P_Calendar_ID", true, false, true, calendarL);
		*/
		fieldCalendar = new VComboBox();
		fillCalendarComboBox();
		fillComboBox();
		fieldCalendar.setBounds(206, 20, 210, 19);
		
		if ( m_QueryType == QUERYTYPE_LstAccrualBankInfo )
		{
			fieldCreditsFamily = new VComboBox();
			fillCreditsFamily();
			fieldCreditsFamily.setBounds(206, 20, 210, 19);
			
		}
		

/*		fieldCalendar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
            	fillComboBox();
            }
        });
*/


		
		fieldEvaluationDate.setValue(new Timestamp(System.currentTimeMillis()));

		//	fieldBenefitsRate.setValue( );

		isSearchPeriodPayment.setSelected(true);
		isSearchPeriodPayment.setText("Période paiement");
		isSearchPeriodPayment.setName("isSearchPeriodPayment");

		isSearchPeriod.setSelected(true);
		isSearchPeriod.setText("Inclure");
		isSearchPeriod.setName("isSearchPeriod");
//		isSearchPeriod.addActionListener(this);

		isSearchDate.setSelected(false);
		isSearchDate.setText("Inclure");
		isSearchDate.setName("isSearchDate");
		
		isRefreshData.setSelected( false );
		isRefreshData.setText( "Actualiser les données");
		isRefreshData.setName("isRefreshData");

//		isSearchDate.addActionListener(this);

		//+2013.03.28
		fieldSelectionDateFrom.addActionListener( new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
            	isSearchDate.setSelected(true);
            }
        });
		fieldSelectionDateTo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
            	isSearchDate.setSelected(true);
            }
        });
		//-2013.03.28

		

//		if ( getGroupByOption() != 0 )
//		{
//			addColumnNoQuery( labelGroupByOption, cbbGroupByOption, false );
//			m_sLine = m_sLine +1;
//		}

		//2012.12.03
		// pas de cache pour la feuille de temps.
		if ( m_QueryType == QUERYTYPE_TimeSheet )
		{
			s_cache.clear();
		}

		if ( s_cache.get(fieldClient.getName() ) != null)   		fieldClient.setValue( s_cache.get(fieldClient.getName()) );
		if ( s_cache.get(fieldOrg.getName() ) != null)	    		fieldOrg.setValue( s_cache.get(fieldOrg.getName()) );
		if ( s_cache.get(fieldActivity.getName() ) != null) 		fieldActivity.setValue( s_cache.get( fieldActivity.getName()) );            
		if ( s_cache.get(fieldDepartment.getName() ) != null) 		fieldDepartment.setValue( s_cache.get( fieldDepartment.getName()) );          
		if ( fieldOccupationGroup != null && s_cache.get(fieldOccupationGroup.getName() ) != null) 	fieldOccupationGroup.setValue( s_cache.get( fieldOccupationGroup.getName()) );     
		if ( fieldWorkplace != null && s_cache.get(fieldWorkplace.getName() ) != null) 		fieldWorkplace.setValue( s_cache.get( fieldWorkplace.getName()) );           
		if ( s_cache.get(fieldEmployee.getName() ) != null) 		fieldEmployee.setValue( s_cache.get( fieldEmployee.getName()) );            
		if ( s_cache.get(fieldEmployer.getName() ) != null) 		fieldEmployer.setValue( s_cache.get( fieldEmployer.getName()) );            
		if ( s_cache.get(fieldCalendar.getName() ) != null) 		fieldCalendar.setValue( s_cache.get( fieldCalendar.getName()) );            
		if ( fieldTaxable_Benefit != null && s_cache.get(fieldTaxable_Benefit.getName() ) != null) 	fieldTaxable_Benefit.setValue( s_cache.get( fieldTaxable_Benefit.getName()) );     
		if ( fieldDeduction != null && s_cache.get(fieldDeduction.getName() ) != null) 		fieldDeduction.setValue( s_cache.get( fieldDeduction.getName()) );           
		if ( fieldElementValue != null && s_cache.get(fieldElementValue.getName() ) != null) 		fieldElementValue.setValue( s_cache.get( fieldElementValue.getName()) );           
		if ( fieldEarning != null && s_cache.get(fieldEarning.getName() ) != null) 			fieldEarning.setValue( s_cache.get( fieldEarning.getName()) );             
		if ( fieldEarningInfo != null && s_cache.get(fieldEarningInfo.getName() ) != null) 	fieldEarningInfo.setValue( s_cache.get( fieldEarningInfo.getName()) );             
		if ( fieldColumnAdm != null && s_cache.get(fieldColumnAdm.getName() ) != null) 	fieldColumnAdm.setValue( s_cache.get( fieldColumnAdm.getName()) );             
		if ( fieldCredits != null && s_cache.get(fieldCredits.getName() ) != null) 			fieldCredits.setValue( s_cache.get( fieldCredits.getName()) );             
		if ( s_cache.get(fieldTaxationRegion.getName() ) != null) 	fieldTaxationRegion.setValue( s_cache.get( fieldTaxationRegion.getName()) );      
		if ( s_cache.get(fieldDistributionBooklet.getName() ) != null) 	fieldDistributionBooklet.setValue( s_cache.get( fieldDistributionBooklet.getName()) ); 
		if ( s_cache.get(fieldDistribution.getName() ) != null) 	fieldDistribution.setValue( s_cache.get( fieldDistribution.getName()) );        
		if ( s_cache.get(fieldJobType.getName() ) != null) 			fieldJobType.setValue( s_cache.get( fieldJobType.getName()) );             
		if ( s_cache.get(fieldJobTitle.getName() ) != null) 		fieldJobTitle.setValue( s_cache.get( fieldJobTitle.getName()) );            
		if ( s_cache.get(fieldPaymentGroup.getName() ) != null) 	fieldPaymentGroup.setValue( s_cache.get( fieldPaymentGroup.getName()) );        
		
		if ( s_cache.get(fieldCollectiveLabour.getName() ) != null) 	fieldCollectiveLabour.setValue( s_cache.get( fieldCollectiveLabour.getName()) );
		
		if ( m_QueryType == QUERYTYPE_LstAccrualBankInfo )
		{
			if ( s_cache.get(isRefreshData.getName() ) != null)      isRefreshData.setSelected( s_cache.get(isRefreshData.getName() ).toString().equals("true"));
			if ( s_cache.get(fieldBenefitsRate1.getName() ) != null) 	fieldBenefitsRate1.setValue( s_cache.get( fieldBenefitsRate1.getName()) );        
			if ( s_cache.get(fieldBenefitsRate2.getName() ) != null) 	fieldBenefitsRate2.setValue( s_cache.get( fieldBenefitsRate2.getName()) );        
			if ( s_cache.get(fieldBenefitsRate3.getName() ) != null) 	fieldBenefitsRate3.setValue( s_cache.get( fieldBenefitsRate3.getName()) );        
			if ( s_cache.get(fieldCreditsFamily.getName() ) != null) 	fieldCreditsFamily.setValue( s_cache.get( fieldCreditsFamily.getName()) );
			
		}
		if ( s_cache.get(m_cbbPayYear.getName() ) != null)     m_cbbPayYear.setValue( s_cache.get(m_cbbPayYear.getName() ));
		if ( s_cache.get(m_cbbPayPeriodFrom.getName() ) != null)     m_cbbPayPeriodFrom.setValue( s_cache.get(m_cbbPayPeriodFrom.getName() ));
		if ( s_cache.get(m_cbbPayPeriodTo.getName() ) != null)       m_cbbPayPeriodTo.setValue( s_cache.get(m_cbbPayPeriodTo.getName() ));

		if ( m_QueryType == QUERYTYPE_VDeductionMandatoryEstimate )
			if ( s_cache.get(isRefreshData.getName() ) != null)      isRefreshData.setSelected( s_cache.get(isRefreshData.getName() ).toString().equals("true"));

		if ( m_QueryType == QUERYTYPE_GeneralLedger || m_QueryType == QUERYTYPE_GeneralLedger360 )
		{
			if ( s_cache.get(m_cbbPeriodGl.getName() ) != null)     m_cbbPeriodGl.setValue( s_cache.get(m_cbbPeriodGl.getName() ));
		}

		if ( s_cache.get(fieldSelectionDateFrom.getName() ) != null)     fieldSelectionDateFrom.setValue( s_cache.get(fieldSelectionDateFrom.getName() ));
		if ( s_cache.get(fieldSelectionDateTo.getName() ) != null)      fieldSelectionDateTo.setValue( s_cache.get(fieldSelectionDateTo.getName() ));
		if ( s_cache.get(isSearchDate.getName() ) != null)      isSearchDate.setSelected( s_cache.get(isSearchDate.getName() ).toString().equals("true"));
		if ( s_cache.get(isSearchPeriod.getName() ) != null)       isSearchPeriod.setSelected( s_cache.get(isSearchPeriod.getName() ).toString().equals("true"));
		if ( s_cache.get(isSearchPeriodPayment.getName() ) != null)       isSearchPeriodPayment.setSelected( s_cache.get(isSearchPeriodPayment.getName() ).toString().equals("true"));

		if ( s_cache.get(fieldEmployeeStatus.getName() ) != null)      fieldEmployeeStatus.setValue( s_cache.get(fieldEmployeeStatus.getName() ));

		if ( s_cache.get(fieldLongTermLeave.getName() ) != null) 	fieldLongTermLeave.setValue( s_cache.get( fieldLongTermLeave.getName()) );        

		

		if ( m_QueryType == QUERYTYPE_TimeSheet )
		{
			if ( s_cache.get(fieldTimeSheetStatus.getName() ) != null)       fieldTimeSheetStatus.setValue( s_cache.get(fieldTimeSheetStatus.getName() ));
			if ( s_cache.get(fieldSheetType.getName() ) != null)       fieldSheetType.setValue( s_cache.get(fieldSheetType.getName() ));
		}
		
		if (m_QueryType == QUERYTYPE_AccrualBank)
		{
			if ( s_cache.get(fieldMovementType.getName() ) != null)      fieldMovementType.setValue( s_cache.get(fieldMovementType.getName() ));
			if ( s_cache.get(fieldMovementOrigin.getName() ) != null)      fieldMovementOrigin.setValue( s_cache.get(fieldMovementOrigin.getName() ));
		}
		addSelectionColumn( labelClient, fieldClient, false );
		addSelectionColumn( labelOrg, fieldOrg, true );
		m_sLine = m_sLine +1;
	

		addSelectionColumn( labelEmployee, fieldEmployee, false );
		if ( m_QueryType == QUERYTYPE_Earning ) // Earning
		{
			addSelectionColumn( labelEarning, fieldEarning, true );
		}

		if ( m_QueryType == QUERYTYPE_Deduction ) // Deduction
		{
			addSelectionColumn( labelDeduction, fieldDeduction, true );
		}

		if ( m_QueryType == QUERYTYPE_VDeductionMandatoryEstimate )
		{
			addSelectionColumn( labelDeduction, fieldDeduction, true );
		}

		if ( m_QueryType == QUERYTYPE_VDeductionMandatoryEstimate )
		{
			scontentPanel.add( isRefreshData,   new GridBagConstraints(5, m_sLine, 1, 1, 0.0, 0.0
					,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));

			m_sLine = m_sLine +1;
			
		}

		
		if ( m_QueryType == QUERYTYPE_GeneralLedger || m_QueryType == QUERYTYPE_GeneralLedger360 ) // ElementValue
		{
			addSelectionColumn( labelElementValue, fieldElementValue, true );
		}

		if ( m_QueryType == QUERYTYPE_Taxable_Benefit ) // Taxable Benefit
		{
			addSelectionColumn( labelTaxable_Benefit, fieldTaxable_Benefit, true );
		}

		if ( m_QueryType == QUERYTYPE_AccrualBank || m_QueryType == QUERYTYPE_AccrualBankWithMonetaryValue ) // Accrual Bank
		{
			addSelectionColumn( labelCredits, fieldCredits, true );
		}
		
		if ( m_QueryType == QUERYTYPE_LstAccrualBankInfo )
		{
			addSelectionColumn( labelCredits, fieldCredits, true );
		}

		
		m_sLine = m_sLine +1;

		addSelectionColumn( labelPaymentGroup, fieldPaymentGroup, false );

		if ( m_QueryType == QUERYTYPE_LstAccrualBankInfo )
		{
			
			addSelectionColumn( labelCreditsFamily, fieldCreditsFamily, true );
		}

		if ( m_QueryType == QUERYTYPE_TimeSheet)
		{
			addColumnNoQuery( labelCalendar, fieldCalendar, true );
		}
		else if ( m_QueryType != QUERYTYPE_LstAccrualBankInfo )
		{
			addSelectionColumn( labelCalendar, fieldCalendar, true );
		}
		
		m_sLine = m_sLine +1;

		if ( m_QueryType == QUERYTYPE_Earning ) // Earning
		{
			addColumnNoQuery( labelEarningInfo, fieldEarningInfo, false );
			addColumnNoQuery( labelColumnAdm, fieldColumnAdm, true );
		}

		if ( m_QueryType == QUERYTYPE_Taxable_Benefit ) 
		{
			addColumnNoQuery( labelColumnAdm, fieldColumnAdm, false );
		}

		if ( m_QueryType == QUERYTYPE_Deduction ) // Deduction
		{
			addColumnNoQuery( labelColumnAdm, fieldColumnAdm, false );
		}

		

		m_sLine = m_sLine +1;
		
		if ( m_QueryType == QUERYTYPE_TimeSheet)
		{
			addColumnNoQuery( labelTimeSheetFilter, fieldTimeSheetStatus, false );
			addColumnNoQuery( labelSheetType, fieldSheetType, true );
			
		}
		m_sLine = m_sLine +1;
		

		addSelectionColumn( labelActivity, fieldActivity, false );

		if ( m_QueryType != QUERYTYPE_LstAccrualBankInfo )
		{
			
			addSelectionColumn( labelDepartment, fieldDepartment, true );
			m_sLine = m_sLine +1;
		}

		if ( m_QueryType == QUERYTYPE_LstAccrualBankInfo )
		{
			m_sLine = m_sLine +1;
		}
		else
		{
			addSelectionColumn( labelEmployer, fieldEmployer, false );
			addSelectionColumn( labelTaxationRegion, fieldTaxationRegion, true );
			m_sLine = m_sLine +1;

			addSelectionColumn( labelWorkPlace, fieldWorkPlace, false );
			addSelectionColumn( labelOccupationGroup, fieldOccupationGroup, true );
			m_sLine = m_sLine +1;

		}

		if (m_QueryType != QUERYTYPE_LstAccrualBankInfo && m_QueryType != QUERYTYPE_TimeSheet && m_QueryType != QUERYTYPE_AccrualBank && m_QueryType != QUERYTYPE_AccrualBankWithMonetaryValue && m_QueryType != QUERYTYPE_Employee)
		{
			addSelectionColumn( labelYear, m_cbbPayYear, false );
			m_sLine = m_sLine +1;
		}	


		if (m_QueryType != QUERYTYPE_LstAccrualBankInfo && m_QueryType != QUERYTYPE_TimeSheet && m_QueryType != QUERYTYPE_AccrualBank && m_QueryType != QUERYTYPE_AccrualBankWithMonetaryValue && m_QueryType != QUERYTYPE_Employee)
		{
			addColumnNoQuery( labelPeriodFrom, m_cbbPayPeriodFrom, false );
			addColumnNoQuery( labelPeriodTo  , m_cbbPayPeriodTo  , true );
			if ( m_QueryType == QUERYTYPE_Earning || m_QueryType == QUERYTYPE_Deduction || m_QueryType == QUERYTYPE_Taxable_Benefit )
			{
				scontentPanel.add( isSearchPeriodPayment,   new GridBagConstraints(5, m_sLine, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));

				
			}


			
			if ( m_QueryType == QUERYTYPE_Earning )
			{
				scontentPanel.add( isSearchPeriod,   new GridBagConstraints(6, m_sLine, 1, 1, 0.0, 0.0
					,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));

			}
			

			m_sLine = m_sLine +1;
		}


		if ( m_QueryType  == this.QUERYTYPE_GeneralLedger || m_QueryType == QUERYTYPE_GeneralLedger360 )
		{
			addColumnNoQuery( labelPeriodGl, m_cbbPeriodGl, false );
			m_sLine = m_sLine +1;
		}	

		
		if ( m_QueryType == QUERYTYPE_AccrualBank || m_QueryType == QUERYTYPE_AccrualBankWithMonetaryValue)
		{
			addColumnNoQuery( labelDateFrom, fieldSelectionDateFrom, false );
			addColumnNoQuery( labelDateTo  , fieldSelectionDateTo  , true );

			scontentPanel.add( isSearchDate,   new GridBagConstraints(5, m_sLine, 1, 1, 0.0, 0.0
					,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));

			m_sLine = m_sLine +1;
		}

		if ( m_QueryType == QUERYTYPE_Earning )
		{
			fieldEvaluationDate.setValue( null );
			addColumnNoQuery( labelDateFrom, fieldSelectionDateFrom, false );
			addColumnNoQuery( labelDateTo  , fieldSelectionDateTo  , true );

			scontentPanel.add( isSearchDate,   new GridBagConstraints(5, m_sLine, 1, 1, 0.0, 0.0
					,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));

			m_sLine = m_sLine +1;
		}


		addSelectionColumn( labelDistributionBooklet, fieldDistributionBooklet, false );
		addSelectionColumn( labelDistribution  , fieldDistribution  , true );
		m_sLine = m_sLine +1;

		addSelectionColumn( labelJobType, fieldJobType, false );
		addSelectionColumn( labelJobTitle  , fieldJobTitle  , true );
		m_sLine = m_sLine +1;

		addColumnNoQuery( labelLongTermLeave, fieldLongTermLeave, false );
		addColumnNoQuery( labelEmployeeStatus, fieldEmployeeStatus, true );
		m_sLine = m_sLine +1;

		if ( m_QueryType == QUERYTYPE_AccrualBank)
		{
			addSelectionColumn( labelMovementType, fieldMovementType, false );
			addSelectionColumn( labelMovementOrigin, fieldMovementOrigin, true );
			m_sLine = m_sLine +1;
		}

		if ( m_QueryType == QUERYTYPE_LstAccrualBankInfo )
		{
			addColumnNoQuery( labelEvaluationDate, fieldEvaluationDate, false );
			addColumnNoQuery( labelBenefitsRate1, fieldBenefitsRate1, true );
			m_sLine = m_sLine +1;
			addColumnNoQuery( labelBenefitsRate2, fieldBenefitsRate2, false );
			addColumnNoQuery( labelBenefitsRate3, fieldBenefitsRate3, true );

			scontentPanel.add( isRefreshData,   new GridBagConstraints(5, m_sLine, 1, 1, 0.0, 0.0
					,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));

			m_sLine = m_sLine +1;
		}
		

		if ( m_QueryType == QUERYTYPE_AccrualBankWithMonetaryValue)
		{
//			addColumnNoQuery( cbMonetaryValue, false );
			addColumnNoQuery( labelEvaluationDate, fieldEvaluationDate, true );
			m_sLine = m_sLine +1;
		}

		if (  m_QueryType == QUERYTYPE_Deduction ) // Earning
		{
			addColumnNoQuery( labelDeductionCategory, fieldDeductionCategory, false );
			addColumnNoQuery( labelDeductionFamily, fieldDeductionFamily, true );
			m_sLine = m_sLine +1;
		}

		if ( m_QueryType == QUERYTYPE_Earning || m_QueryType == QUERYTYPE_Deduction  || m_QueryType == QUERYTYPE_TimeSheet || m_QueryType == QUERYTYPE_Taxable_Benefit) // Earning
		{
			addSelectionColumn( labelCollectiveLabour, fieldCollectiveLabour, false );
		}
		
		
//		addSelectionColumn( null , checkIsActive  , true );
//		m_sLine = m_sLine +1;
		
		//	Disable simple query fields
		valueLabel.setVisible(hasValue);
		valueField.setVisible(hasValue);
		if (hasValue)
			valueField.addActionListener(this);
		docNoLabel.setVisible(hasDocNo);
		docNoField.setVisible(hasDocNo);
		if (hasDocNo)
			docNoField.addActionListener(this);
		nameLabel.setVisible(hasName);
		nameField.setVisible(hasName);
		if (hasName)
			nameField.addActionListener(this);
		descriptionLabel.setVisible(hasDescription);
		descriptionField.setVisible(hasDescription);
		if (hasDescription)
			descriptionField.addActionListener(this);

		//	Get Total
		m_total = getNoOfRecords(null, false);
		setStatusDB (m_total);
		statusBar.setStatusLine("");

		tabbedPane.addChangeListener(this);

		//	Better Labels for OK/Cancel
		confirmPanelA.getOKButton().setToolTipText(Msg.getMsg(Env.getCtx(),"QueryEnter"));
		confirmPanelA.getCancelButton().setToolTipText(Msg.getMsg(Env.getCtx(),"QueryCancel"));
		confirmPanelS.getOKButton().setToolTipText(Msg.getMsg(Env.getCtx(),"QueryEnter"));
		confirmPanelS.getCancelButton().setToolTipText(Msg.getMsg(Env.getCtx(),"QueryCancel"));
		
		bDeleteRow.addActionListener(this);
		bDeleteRow.setToolTipText(Msg.getMsg(m_ctx, "QueryDeleteRow"));
		bSaveRow.addActionListener(this);
		bSaveRow.setToolTipText(Msg.getMsg(m_ctx, "QuerySaveRow"));

	}	//	initFind

	
	

	/**
	 * 	Add Selection Column 
	 */
	private void addSelectionColumn( CLabel label, VComboBox cb, Boolean sameLine )
	{
		if ( ! sameLine )
		{
			if ( label != null )
				scontentPanel.add(label,   new GridBagConstraints(1, m_sLine, 1, 1, 0.0, 0.0
					,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(7, 5, 5, 5), 0, 0));
			scontentPanel.add((Component)cb,   new GridBagConstraints(2, m_sLine, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
		
		}
		else
		{
			if ( label != null )
				scontentPanel.add(label,   new GridBagConstraints(3, m_sLine, 1, 1, 0.0, 0.0
					,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(7, 5, 5, 5), 0, 0));
			scontentPanel.add((Component)cb,   new GridBagConstraints(4, m_sLine, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
			
		}
		m_sComboBox.add(cb);

	}
	
	private void addColumnNoQuery( CLabel label, VComboBox cb, Boolean sameLine )
	{
		if ( ! sameLine )
		{
			if ( label != null )
				scontentPanel.add(label,   new GridBagConstraints(1, m_sLine, 1, 1, 0.0, 0.0
					,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(7, 5, 5, 5), 0, 0));
			scontentPanel.add((Component)cb,   new GridBagConstraints(2, m_sLine, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
		
		}
		else
		{
			if ( label != null )
				scontentPanel.add(label,   new GridBagConstraints(3, m_sLine, 1, 1, 0.0, 0.0
					,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(7, 5, 5, 5), 0, 0));
			scontentPanel.add((Component)cb,   new GridBagConstraints(4, m_sLine, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
			
		}
	}

	private void addColumnNoQuery( CCheckBox cb, Boolean sameLine )
	{
		if ( ! sameLine )
		{
			scontentPanel.add((Component)cb,   new GridBagConstraints(2, m_sLine, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
		
		}
		else
		{
			scontentPanel.add((Component)cb,   new GridBagConstraints(4, m_sLine, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
			
		}
	}


	private void addColumnNoQuery( CLabel label,VEditor editor, Boolean sameLine )
	{
		if ( ! sameLine )
		{
			if ( label != null )
				scontentPanel.add(label,   new GridBagConstraints(1, m_sLine, 1, 1, 0.0, 0.0
					,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(7, 5, 5, 5), 0, 0));
			scontentPanel.add((Component)editor,   new GridBagConstraints(2, m_sLine, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
		
		}
		else
		{
			if ( label != null )
				scontentPanel.add(label,   new GridBagConstraints(3, m_sLine, 1, 1, 0.0, 0.0
					,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(7, 5, 5, 5), 0, 0));
			scontentPanel.add((Component)editor,   new GridBagConstraints(4, m_sLine, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
			
		}
	}

	/**
	 * 	Add Selection Column 
	 */
	private void addSelectionColumn( CLabel label, VEditor editor, Boolean sameLine )
	{
		if ( ! sameLine )
		{
			if ( label != null )
				scontentPanel.add(label,   new GridBagConstraints(1, m_sLine, 1, 1, 0.0, 0.0
					,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(7, 5, 5, 5), 0, 0));
			scontentPanel.add((Component)editor,   new GridBagConstraints(2, m_sLine, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
		
		}
		else
		{
			if ( label != null )
				scontentPanel.add(label,   new GridBagConstraints(3, m_sLine, 1, 1, 0.0, 0.0
					,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(7, 5, 5, 5), 0, 0));
			scontentPanel.add((Component)editor,   new GridBagConstraints(4, m_sLine, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
			
		}
		m_sEditors.add(editor);

	}
	
	/**
	 * 	Add Selection Column to first Tab
	 * 	@param mField field
	 */
	private void addSelectionColumn (GridField mField)
	{
		log.config(mField.getHeader());
		int displayLength = mField.getDisplayLength();
		if (displayLength > FIELDLENGTH)
			mField.setDisplayLength(FIELDLENGTH);
		else
			displayLength = 0;
		
		//	Editor
		VEditor editor = null;
		if (mField.isLookup())
		{
			VLookup vl = new VLookup(mField.getColumnName(), false, false, true,
				mField.getLookup());
			vl.setName(mField.getColumnName());
			editor = vl;
		}
		else
		{
			editor = VEditorFactory.getEditor(mField, false);
			editor.setMandatory(false);
			editor.setReadWrite(true);
		}
		CLabel label = VEditorFactory.getLabel(mField);
		//
		if (displayLength > 0)		//	set it back
			mField.setDisplayLength(displayLength);
		//
		m_sLine++;
		if (label != null)	//	may be null for Y/N
			scontentPanel.add(label,   new GridBagConstraints(1, m_sLine, 1, 1, 0.0, 0.0
				,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(7, 5, 5, 5), 0, 0));
		scontentPanel.add((Component)editor,   new GridBagConstraints(2, m_sLine, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
		m_sEditors.add(editor);

	}	//	addSelectionColumn


	/**
	 *  Init Find GridController
	 */
/*
	private void initFindAdvanced()
	{
		
		log.config("");
		Object[][] data =
		{
			new Object[] {null, MQuery.OPERATORS[MQuery.EQUAL_INDEX], null, null},
			new Object[] {null, MQuery.OPERATORS[MQuery.EQUAL_INDEX], null, null},
			new Object[] {null, MQuery.OPERATORS[MQuery.EQUAL_INDEX], null, null},
			new Object[] {null, MQuery.OPERATORS[MQuery.EQUAL_INDEX], null, null},
			new Object[] {null, MQuery.OPERATORS[MQuery.EQUAL_INDEX], null, null}
		};
		String[] columnNames = {"AD_Column_ID", "Operator", "QueryValue", "QueryValue2"};
		advancedTable.setModel(new DefaultTableModel(data, columnNames));
		advancedTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		advancedTable.putClientProperty("terminateEditOnFocusLost",Boolean.TRUE);
		
        advancedTable.addHierarchyListener(new HierarchyListener()
        {
            public void hierarchyChanged(HierarchyEvent he)
            {
                if ((he.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) ==
                    HierarchyEvent.SHOWING_CHANGED)
                {
                    SwingUtilities.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                        	if (advancedTable == null)
                        		return;
                            int row = advancedTable.getSelectedRow();
                            int col = advancedTable.getSelectedColumn();
                            if (advancedTable.isCellEditable(row, col))
                            {
                                advancedTable.editCellAt(row, col);
                                Component cc = advancedTable.getEditorComponent();
                                if (cc != null)
                                	cc.requestFocusInWindow();
                            }
                            else
                                advancedTable.requestFocusInWindow();
                        }
                    });
                }
            }
        });
        advancedTable.setRowSelectionInterval(0, 0);
        advancedTable.setColumnSelectionInterval(0, 0);
        advancedTable.setSurrendersFocusOnKeystroke(true);
        TableCellRenderer renderer = new ProxyRenderer(advancedTable.getDefaultRenderer(Object.class));
        advancedTable.setDefaultRenderer(Object.class, renderer);

		//	0 = Columns
		ArrayList<ValueNamePair> items = new ArrayList<ValueNamePair>();
		for (int c = 0; c < m_findFields.length; c++)
		{
			GridField field = m_findFields[c];
			String columnName = field.getColumnName();
			String header = field.getHeader();
			if (header == null || header.length() == 0)
			{
				header = Msg.translate(Env.getCtx(), columnName);
				if (header == null || header.length() == 0)
					continue;
			}
			if (field.isKey())
				header += (" (ID)");
			ValueNamePair pp = new ValueNamePair(columnName, header);

			items.add(pp);
		}
		ValueNamePair[] cols = new ValueNamePair[items.size() + 1];
		items.toArray(cols);
		Arrays.sort(cols);		//	sort alpha
		columns = new CComboBox(cols);
		columns.addActionListener(this);
		columns.setAutoReducible(false);
		columns.setEditable(false);
		TableColumn tc = advancedTable.getColumnModel().getColumn(INDEX_COLUMNNAME);
		tc.setPreferredWidth(150);
		final DefaultCellEditor dce = new DefaultCellEditor(columns);
		dce.addCellEditorListener(new CellEditorListener()
		{
			public void editingCanceled(ChangeEvent ce)
			{
			}

			public void editingStopped(ChangeEvent ce)
			{
				int row = advancedTable.getSelectedRow();
				if (row == INDEX_COLUMNNAME)
				{
					advancedTable.setValueAt(null, row, INDEX_VALUE);
					advancedTable.setValueAt(null, row, INDEX_VALUE2);
				}
			}
		});
		tc.setCellEditor(dce);
		tc.setHeaderValue(Msg.translate(Env.getCtx(), columnNames[0]));

		//	1 = Operators
		operators = new CComboBox(MQuery.OPERATORS);
		operators.setAutoReducible(false);
		operators.setEditable(false);
		tc = advancedTable.getColumnModel().getColumn(INDEX_OPERATOR);
		tc.setPreferredWidth(40);
		tc.setCellEditor(new DefaultCellEditor(operators));
		tc.setHeaderValue(Msg.getMsg(Env.getCtx(), columnNames[1]));

		// 	2 = QueryValue
		tc = advancedTable.getColumnModel().getColumn(INDEX_VALUE);
        FindValueEditor fve = new FindValueEditor(this, false);
        CellEditorListener cel = new CellEditorListener()
        {
            public void editingCanceled(ChangeEvent ce)
            {
            }

            public void editingStopped(ChangeEvent ce)
            {
                advancedTable.requestFocusInWindow();
            }
        };
        fve.addCellEditorListener(cel);
		tc.setCellEditor(fve);
        renderer = new ProxyRenderer(new FindValueRenderer(this, false));
		tc.setCellRenderer(renderer);
		tc.setHeaderValue(Msg.getMsg(Env.getCtx(), columnNames[2]));

		// 	3 = QueryValue2
		tc = advancedTable.getColumnModel().getColumn(INDEX_VALUE2);
		tc.setPreferredWidth(50);
        fve = new FindValueEditor(this, true);
        fve.addCellEditorListener(cel);
		tc.setCellEditor(fve);
        renderer = new ProxyRenderer(new FindValueRenderer(this, true));
        tc.setCellRenderer(renderer);
		tc.setHeaderValue(Msg.getMsg(Env.getCtx(), columnNames[3]));
		
	}   //  initFindAdvanced
*/
	
	/**
	 *  Init Advanced Tab (called when tab changed)
	 */
	private void initFindAdvanced()
	{
		log.config("");
		
		advancedEntry.removeAll();
		advancedData = new Vector<AdvancedRow>();
		Vector<String> columnNames = new Vector<String>(); 
		columnNames.add(Msg.translate(m_ctx,"AD_Column_ID"));
		columnNames.add(Msg.translate(m_ctx,"Operator"));
		columnNames.add(Msg.translate(m_ctx,"QueryValue"));
		columnNames.add(Msg.translate(m_ctx,"QueryValue2"));
		//
		DefaultTableModel model = new DefaultTableModel(advancedData, columnNames)
		{
		    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {return false;}
		};
		advancedTable.setModel(model);
//		advancedTable.setSortEnabled(false);
		advancedTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        advancedTable.setRowSelectionAllowed(true);
        advancedTable.setColumnSelectionAllowed(false);

        m_buttonFields.clear();
        
		//	0 = Columns
		ArrayList<ValueNamePair> items = new ArrayList<ValueNamePair>();
		for (int c = 0; c < m_findFields.length; c++)
		{
			GridField field = m_findFields[c];
			if (field.isEncrypted())
				continue;
			String columnName = field.getColumnName();
			if (field.getDisplayType() == DisplayTypeConstants.Button) 
			{
				if (field.getAD_Reference_Value_ID() == 0)
					continue;
				if (columnName.endsWith("_ID"))
					field.setDisplayType(DisplayTypeConstants.Table);
				else
					field.setDisplayType(DisplayTypeConstants.List);
					
				field.loadLookup();
				m_buttonFields.add(c);
			}

			String header = field.getHeader();
			if (header == null || header.length() == 0)
			{
				header = Msg.getElement(m_ctx, columnName);
				if (header == null || header.length() == 0)
					continue;
			}
			if (field.isKey())
				header += (" (ID)");
			ValueNamePair pp = new ValueNamePair(columnName, header);
			items.add(pp);
		}
		ValueNamePair[] cols = new ValueNamePair[items.size() + 1];
		items.toArray(cols);
		try
		{
			Arrays.sort(cols);		//	sort alpha
		}
		catch (Exception e)
		{
			log.log( Level.SEVERE, "Sort Error ", e);
		}
		columns = new CComboBox(cols);
		columns.addActionListener(this);
		columns.setAutoReducible(false);
		columns.setEditable(false);
		Dimension size = columns.getPreferredSize();
		size.width = 180;
		columns.setPreferredSize(size);
		//
		String name = columnNames.get(INDEX_COLUMNNAME);
		CLabel lcolumn = new CLabel(name);
		lcolumn.setLabelFor(columns);
		advancedEntry.add(lcolumn, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		advancedEntry.add(columns, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		//
		TableColumn tc = advancedTable.getColumnModel().getColumn(INDEX_COLUMNNAME);
		tc.setPreferredWidth(150);
		
		//	1 = Operators
		operators = new CComboBox(Query.OPERATORS);
		operators.addActionListener(this);
		operators.setAutoReducible(false);
		operators.setEnabled(false);
		size = operators.getPreferredSize();
		size.width = 50;
		operators.setPreferredSize(size);
		//
		name = columnNames.get(INDEX_OPERATOR);
		CLabel loperator = new CLabel(name);
		loperator.setLabelFor(operators);
		advancedEntry.add(loperator, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		advancedEntry.add(operators, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		//
		tc = advancedTable.getColumnModel().getColumn(INDEX_OPERATOR);
		tc.setPreferredWidth(50);

		// 	2 = QueryValue
		valueEditor = new FindValueEditor();
		valueEditor.setEnabled(false);
		size = valueEditor.getPreferredSize();
		size.width = 120;
		valueEditor.setPreferredSize(size);
		name = columnNames.get(INDEX_VALUE);
		CLabel lvalue = new CLabel(name);
		lvalue.setLabelFor(valueEditor);
		advancedEntry.add(lvalue, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		advancedEntry.add(valueEditor, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		//
		tc = advancedTable.getColumnModel().getColumn(INDEX_VALUE);
		tc.setPreferredWidth(120);

		// 	3 = QueryValue2
		valueEditor2 = new FindValueEditor();
		valueEditor2.setEnabled(false);
		name = columnNames.get(INDEX_VALUE2);
		CLabel lvalue2 = new CLabel(name);
		lvalue2.setLabelFor(valueEditor2);
		advancedEntry.add(lvalue2, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		advancedEntry.add(valueEditor2, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		//
		tc = advancedTable.getColumnModel().getColumn(INDEX_VALUE2);
		tc.setPreferredWidth(50);
		//
		advancedEntry.add(bDeleteRow, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 10, 0, 5), 0, 0));
		advancedEntry.add(bSaveRow, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 10, 5, 5), 0, 0));
		bSaveRow.setEnabled(false);
		bDeleteRow.setEnabled(false);
		//
		pack();
	}   //  initFindAdvanced

	/**
	 *	Dispose window
	 */
	public void dispose()
	{
		log.fine("");

		//  Find SQL
		if (m_pstmt != null)
		{
			try 
			{
				m_pstmt.close();
			} 
			catch (SQLException e)
			{}
		}
		m_pstmt = null;

		//  TargetFields
		if (m_targetFields != null)
			m_targetFields.clear();
		m_targetFields = null;
		//
		if (m_sEditors != null)
		{
			for (int i = 0; i < m_sEditors.size(); i++)
				m_sEditors.get(i).removeActionListener(this);
			m_sEditors.clear();
		}
		m_sEditors = null;
		
		removeAll();
		super.dispose();
	}	//	dispose

	
	/**************************************************************************
	 *	Action Listener
	 *  @param e ActionEvent
	 */
	
	/**************************************************************************
	 *	Action Listener
	 *  @param e ActionEvent
	 */
	@Override
	public void actionPerformed (ActionEvent e)
	{
		log.fine(e.getActionCommand());
		Object source = e.getSource();
		m_days = 0;
		m_query = null;
/*		
		if ( source == isSearchDate  )
		{
			if ( isSearchDate.isSelected()  ) 
			{
				isSearchPeriod.setSelected(true);
				isSearchDate.setSelected(false);
			}
			else 
			{
				isSearchDate.setSelected(true);
				isSearchPeriod.setSelected(false);
			}
		}

		else if ( source  == isSearchPeriod  )
		{
			if ( isSearchPeriod.isSelected() ) 
			{
				isSearchDate.setSelected(true);
				isSearchPeriod.setSelected(false);
			}
			else 
			{
				isSearchPeriod.setSelected(true);
				isSearchDate.setSelected(false);
			}
		}

		//	Cancel - Simple & Advances
		else 
*/			
		if (e.getActionCommand() == ConfirmPanel.A_CANCEL)
			cmd_cancel();
		//	New - Simple
		else if (e.getActionCommand() == ConfirmPanel.A_NEW)
		{
			m_query = Query.getNoRecordQuery(m_tableName, true);
			m_total = 0;
			dispose();
		}

		//	Refresh - Advanced
		else if (e.getActionCommand() == ConfirmPanel.A_REFRESH)
			cmd_refresh();
		//	Saved Query Retrieve - Advanced 
		else if (source == comboQueriesA)
			cmd_savedQueryLoad();
		//	Delete Query
		else if (source == bDeleteQuery)
			cmd_savedQueryDelete();
		
		//	Set Advanced Column
		else if (source == columns)
		{
			ValueNamePair column = (ValueNamePair)columns.getSelectedItem();
			if (column != null)
			{
				String columnName = column.getValue();
				log.config("Column=" + columnName);
				if (columnName.endsWith("_ID") || columnName.endsWith("_Acct"))
					operators.setModel(new DefaultComboBoxModel(Query.OPERATORS_ID));
				else if (columnName.startsWith("Is"))
					operators.setModel(new DefaultComboBoxModel(Query.OPERATORS_YN));
				else
					operators.setModel(new DefaultComboBoxModel(Query.OPERATORS));
				GridField field = getTargetMField(columnName);
				valueEditor.setEditor(field);
				bSaveRow.setEnabled(true);
			}
			operators.setEnabled(true);
			valueEditor.setEnabled(true);
			valueEditor2.setEnabled(false);
			advancedTable.getSelectionModel().clearSelection();
		}
		//	Set Advanced Operator
		else if (source == operators)
		{
			ValueNamePair operator = (ValueNamePair)operators.getSelectedItem();
			if (operators != null)
			{
				String op = operator.getValue();
				log.config("Operator: " + op);
				if (Query.BETWEEN.equals(op))
				{
					ValueNamePair column = (ValueNamePair)columns.getSelectedItem();
					String columnName = column.getValue();
					GridField field = getTargetMField(columnName);
					valueEditor2.setEditor(field);
					valueEditor2.setEnabled(true);
				}
			}
			else
			{
				bSaveRow.setEnabled(false);
				valueEditor2.setEnabled(false);
			}
			advancedTable.getSelectionModel().clearSelection();
		}
		else if (source == bSaveRow)
			cmd_saveRow();
		else if (source == bDeleteRow)
			cmd_deleteRow();
		
		//	History
		else if (source == comboQueriesS || source == bShowDay || source == bShowWeek 
			|| source == bShowMonth || source == bShowYear || source == bShowAll)
		{
			if (e.getSource() == bShowDay)
				m_days = 1;
			else if (e.getSource() == bShowWeek)
				m_days = 7;
			else if (e.getSource() == bShowMonth)
				m_days = 31;
			else if (e.getSource() == bShowYear)
				m_days = 356;
			else
				m_days = 0;		//	all
			m_created = cbCreated.isSelected();
			//
			Object xx = comboQueriesS.getSelectedItem();
			if (xx instanceof ValueNamePair)
			{
				ValueNamePair pp = (ValueNamePair)xx;
				m_query = new Query(m_tableName);
				m_query.addRestriction (pp.getValue());
				int no = getNoOfRecords(m_query, true);
				m_query.setRecordCount(no);
				if (no != 0)
					dispose();
			}
			else
				dispose();
		}
		
		else    // ConfirmPanel.A_OK and enter in fields - Simple & Advanced
		{
			if (source == confirmPanelA.getOKButton())
				cmd_okAdvanced();
			else
			{
				if(source == confirmPanelS.getOKButton())
					cmd_okSimple();
				//	Close when hitting enter in text field on first tab
				Component[] simpleComponents = scontentPanel.getComponents();
				for (Component component : simpleComponents) 
				{
					if (component == source)
					{
						cmd_okSimple();
						break;
					}
				}
			}
		}
	}	//	actionPerformed

	
/*	
	public void actionPerformed (ActionEvent e)
	{		
		log.fine(e.getActionCommand());
		Object source = e.getSource();
		m_query = null;

		log.info(e.getActionCommand());
		//
		if (e.getActionCommand() == ConfirmPanel.A_CANCEL)
			cmd_cancel();
		else if (e.getActionCommand() == ConfirmPanel.A_REFRESH)
			cmd_refresh();
		//
		else if (e.getActionCommand() == ConfirmPanel.A_NEW)
		{
			m_query = MQuery.getNoRecordQuery(m_tableName, true);
			m_total = 0;
			dispose();
		}
		//
		//	Saved Query Retrieve - Advanced 
		else if (source == comboQueriesA)
			cmd_savedQueryLoad();
		//	Delete Query
		else if (source == bDeleteQuery)
			cmd_savedQueryDelete();
		
		//	Set Advanced Column
		else if (source == columns)
		{
			ValueNamePair column = (ValueNamePair)columns.getSelectedItem();
			if (column != null)
			{
				String columnName = column.getValue();
				log.config("Column=" + columnName);
				if (columnName.endsWith("_ID") || columnName.endsWith("_Acct"))
					operators.setModel(new DefaultComboBoxModel(Query.OPERATORS_ID));
				else if (columnName.startsWith("Is"))
					operators.setModel(new DefaultComboBoxModel(Query.OPERATORS_YN));
				else
					operators.setModel(new DefaultComboBoxModel(Query.OPERATORS));
				GridField field = getTargetMField(columnName);
				valueEditor.setEditor(field);
				bSaveRow.setEnabled(true);
			}
			operators.setEnabled(true);
			valueEditor.setEnabled(true);
			valueEditor2.setEnabled(false);
			advancedTable.getSelectionModel().clearSelection();
		}
		//	Set Advanced Operator
		else if (source == operators)
		{
			ValueNamePair operator = (ValueNamePair)operators.getSelectedItem();
			if (operators != null)
			{
				String op = operator.getValue();
				log.config("Operator: " + op);
				if (Query.BETWEEN.equals(op))
				{
					ValueNamePair column = (ValueNamePair)columns.getSelectedItem();
					String columnName = column.getValue();
					GridField field = getTargetMField(columnName);
					valueEditor2.setEditor(field);
					valueEditor2.setEnabled(true);
				}
			}
			else
			{
				bSaveRow.setEnabled(false);
				valueEditor2.setEnabled(false);
			}
			advancedTable.getSelectionModel().clearSelection();
		}
		else if (source == bSaveRow)
			cmd_saveRow();
		else if (source == bDeleteRow)
			cmd_deleteRow();
		
		//
		
		else if (e.getSource() == bIgnore)
			cmd_ignore();
		else if (e.getSource() == bSave)
			cmd_save();
		else if (e.getSource() == bDelete)
			cmd_delete();
		//
		else if (e.getSource() == columns)
		{
			ValueNamePair column = (ValueNamePair)columns.getSelectedItem();
			if (column != null)
			{
				String columnName = column.getValue();
				log.config("Column: " + columnName);
				if (columnName.endsWith("_ID") || columnName.endsWith("_Acct"))
					operators.setModel(new DefaultComboBoxModel(MQuery.OPERATORS_ID));
				else if (columnName.startsWith("Is"))
					operators.setModel(new DefaultComboBoxModel(MQuery.OPERATORS_YN));
				else
					operators.setModel(new DefaultComboBoxModel(MQuery.OPERATORS));
			}
		}
		else    // ConfirmPanel.A_OK and enter in fields
		{
			if (e.getSource() == confirmPanelA.getOKButton())
				cmd_ok_Advanced();
			else
				cmd_ok_Simple();
		}
	}	//	actionPerformed
*/
	/**
	 *  Change Listener (tab change)
	 *  @param e ChangeEbent
	 */
	public void stateChanged(ChangeEvent e)
	{
	//	log.info( "Find.stateChanged");
		if (tabbedPane.getSelectedIndex() == 0)
			this.getRootPane().setDefaultButton(confirmPanelS.getOKButton());
		else
		{
			initFindAdvanced();
			this.getRootPane().setDefaultButton(confirmPanelA.getOKButton());
		}
	}	//  stateChanged

	String infoQuery = "Recherche : ";

	public String getInfoQuery ()
	{
		return infoQuery;
	}

	/**
	 *	Simple OK Button pressed
	 */
	private void cmd_okSimple()
	{
		s_cache.put(fieldActivity.getName() ,fieldActivity.getValue());
		s_cache.put(fieldClient.getName(), fieldClient.getValue());
		s_cache.put(fieldOrg.getName(), fieldOrg.getValue());
		s_cache.put(fieldDepartment.getName(), fieldDepartment.getValue());
		s_cache.put(fieldOccupationGroup.getName(), fieldOccupationGroup.getValue());
		if ( fieldWorkplace != null ) s_cache.put(fieldWorkplace.getName(), fieldWorkplace.getValue());
		s_cache.put(fieldEmployee.getName(), fieldEmployee.getValue());
		s_cache.put(fieldEmployer.getName(), fieldEmployer.getValue());
		s_cache.put(fieldCalendar.getName(), fieldCalendar.getValue());
		if ( fieldTaxable_Benefit != null ) s_cache.put(fieldTaxable_Benefit.getName(), fieldTaxable_Benefit.getValue());
		if ( fieldDeduction != null ) s_cache.put(fieldDeduction.getName(), fieldDeduction.getValue());
		if ( fieldElementValue != null ) s_cache.put(fieldElementValue.getName(), fieldElementValue.getValue());
		if ( fieldEarning != null ) s_cache.put(fieldEarning.getName(), fieldEarning.getValue());
		if ( fieldEarningInfo != null ) s_cache.put(fieldEarningInfo.getName(), fieldEarningInfo.getValue());
		if ( fieldColumnAdm != null ) s_cache.put(fieldColumnAdm.getName(), fieldColumnAdm.getValue());
		if ( fieldCredits != null ) s_cache.put(fieldCredits.getName(), fieldCredits.getValue());
		s_cache.put(fieldTaxationRegion.getName(), fieldTaxationRegion.getValue());
		s_cache.put(fieldWorkPlace.getName(), fieldWorkPlace.getValue());
		s_cache.put(fieldDistributionBooklet.getName(), fieldDistributionBooklet.getValue());
		s_cache.put(fieldDistribution.getName(), fieldDistribution.getValue());
		s_cache.put(fieldJobType.getName(), fieldJobType.getValue());
		s_cache.put(fieldJobTitle.getName(), fieldJobTitle.getValue());
		s_cache.put(fieldPaymentGroup.getName(), fieldPaymentGroup.getValue());
		s_cache.put(m_cbbPayYear.getName(), m_cbbPayYear.getValue()); 
		s_cache.put(m_cbbPayPeriodFrom.getName(), m_cbbPayPeriodFrom.getValue()); 
		s_cache.put(m_cbbPayPeriodTo.getName(), m_cbbPayPeriodTo.getValue()); 

		if ( m_QueryType == QUERYTYPE_VDeductionMandatoryEstimate )
		{
			s_cache.put( isRefreshData.getName(), isRefreshData.isSelected());
		}

		if ( m_QueryType == QUERYTYPE_GeneralLedger || m_QueryType == QUERYTYPE_GeneralLedger360 )
		{
			s_cache.put(m_cbbPeriodGl.getName(), m_cbbPeriodGl.getValue()); 
			
		}

		s_cache.put( fieldSelectionDateFrom.getName(), fieldSelectionDateFrom.getValue());
		s_cache.put( fieldSelectionDateTo.getName(), fieldSelectionDateTo.getValue());
		s_cache.put( isSearchDate.getName(), isSearchDate.isSelected());
		s_cache.put( isSearchPeriod.getName(), isSearchPeriod.isSelected());
		s_cache.put( isSearchPeriodPayment.getName(), isSearchPeriodPayment.isSelected());

		s_cache.put( fieldEmployeeStatus.getName(), fieldEmployeeStatus.getValue());

		s_cache.put(fieldLongTermLeave.getName(), fieldLongTermLeave.getValue());

		if ( m_QueryType == QUERYTYPE_LstAccrualBankInfo )
		{
			s_cache.put( fieldBenefitsRate1.getName(), fieldBenefitsRate1.getValue());
			s_cache.put( fieldBenefitsRate2.getName(), fieldBenefitsRate2.getValue());
			s_cache.put( fieldBenefitsRate3.getName(), fieldBenefitsRate3.getValue());
			s_cache.put( isRefreshData.getName(), isRefreshData.isSelected());
			s_cache.put(fieldCreditsFamily.getName(), fieldCreditsFamily.getValue());
		}
		
		if (m_QueryType == QUERYTYPE_AccrualBank)
		{
			s_cache.put(fieldMovementType.getName(), fieldMovementType.getValue()); 
			s_cache.put(fieldMovementOrigin.getName(), fieldMovementOrigin.getValue()); 
		}

		
		if ( m_QueryType == QUERYTYPE_TimeSheet )
		{
			s_cache.put(fieldTimeSheetStatus.getName(), fieldTimeSheetStatus.getValue()); 
			s_cache.put(fieldSheetType.getName(), fieldSheetType.getValue()); 
		}
		
		//	Create Query String
		m_query = new Query(m_tableName);
		if (hasValue && !valueField.getText().equals("%") && valueField.getText().length() != 0)
		{
			String value = valueField.getText().toUpperCase();
			if (!value.endsWith("%"))
				value += "%";
			m_query.addRestriction("UPPER(Value)", Query.LIKE, value, valueLabel.getText(), value);
			infoQuery += " - Clé de recherche = " + value;
		}
		//
		if (hasDocNo && !docNoField.getText().equals("%") && docNoField.getText().length() != 0)
		{
			String value = docNoField.getText().toUpperCase();
			if (!value.endsWith("%"))
				value += "%";
			m_query.addRestriction("UPPER(DocumentNo)", Query.LIKE, value, docNoLabel.getText(), value);
			infoQuery += " - No document = " + value;
		}
		//
		if ((hasName) && !nameField.getText().equals("%") && nameField.getText().length() != 0)
		{
			String value = nameField.getText().toUpperCase();
			if (!value.endsWith("%"))
				value += "%";
			m_query.addRestriction("UPPER(Name)", Query.LIKE, value, nameLabel.getText(), value);
			infoQuery += " - Nom = " + value;
		}
		//
		if (hasDescription && !descriptionField.getText().equals("%") && descriptionField.getText().length() != 0)
		{
			String value = descriptionField.getText().toUpperCase();
			if (!value.endsWith("%"))
				value += "%";
			m_query.addRestriction("UPPER(Description)", Query.LIKE, value, descriptionLabel.getText(), value);
			infoQuery += " - Description = " + value;
		}
		//	Special Editors
		for (int i = 0; i < m_sEditors.size(); i++)
		{
			VEditor ved = m_sEditors.get(i);
			Object value = ved.getValue();
			if (value != null && value.toString().length() > 0)
			{
				String ColumnName = ved.getName();
				String sqlQuery = ColumnName;
				GridField field = ved.getField();
				if (field != null)
				{
					String sql = field.getColumnSQL(false);	//	virtual column
					if (sql != null && sql.length() > 0)
						sqlQuery = sql;
				}
				log.fine(ColumnName + "=" + value);
				infoQuery += " - " + Msg.translate(Env.getCtx(), ved.getName()) + " = " + ved.getDisplay();
				if (value.toString().indexOf("%") != -1)
					m_query.addRestriction(sqlQuery, Query.LIKE, value, ColumnName, ved.getDisplay());
				else
					m_query.addRestriction(sqlQuery, Query.EQUAL, value, ColumnName, ved.getDisplay());


			}
		}	//	editors


		//	Test for no records
		if (getNoOfRecords(m_query, true) != 0)
			dispose();
	}	//	cmd_ok_Simple


	
	/**
	 *	Advanced OK Button pressed
	 */
	private void cmd_ok_Advanced()
	{
		//	save pending
		if (bSave.isEnabled())
			cmd_save();
		if (getNoOfRecords(m_query, true) != 0)
			dispose();
	}	//	cmd_ok_Advanced

	
	/**
	 *	Advanced OK Button pressed
	 */
	private void cmd_okAdvanced()
	{
		//	save pending
		cmd_saveAdvanced();
		
		/* nnayak : Reset display type for buttons */
		for (int c = 0; c < m_buttonFields.size(); c++)
		{
			if (m_findFields[m_buttonFields.get( c )] != null)
				m_findFields[m_buttonFields.get( c )].setDisplayType( DisplayTypeConstants.Button );
		}
		
		if (getNoOfRecords(m_query, true) != 0)
			dispose();
	}	//	cmd_ok_Advanced

	/**
	 *	Cancel Button pressed
	 */
	
	
	
//+ Progestion
	private boolean m_hasBeenCancelled = false;
	public boolean hasBeenCancelled() { return m_hasBeenCancelled; }
//- Progestion	
	private void cmd_cancel()
	{
		log.info("");
		m_query = null;
		m_total = 999999;
		m_hasBeenCancelled = true;
		dispose();
	}	//	cmd_ok

	/**
	 *	Ignore
	 */
	private void cmd_ignore()
	{
		log.info("");
	}	//	cmd_ignore

	/**
	 *	Save (Advanced)
	 */
	private void cmd_save()
	{
		log.info("");
		advancedTable.stopEditor(true);
		//
		m_query = new Query(m_tableName);
		for (int row = 0; row < advancedTable.getRowCount(); row++)
		{
			//	Column
			Object column = advancedTable.getValueAt(row, INDEX_COLUMNNAME);
			if (column == null)
				continue;
			String ColumnName = ((ValueNamePair)column).getValue();
			String infoName = column.toString();
			//
			GridField field = getTargetMField(ColumnName);
			String ColumnSQL = field.getColumnSQL(false);
			//	Op
			Object op = advancedTable.getValueAt(row, INDEX_OPERATOR);
			if (op == null)
				continue;
			String Operator = ((ValueNamePair)op).getValue();
			
			//	Value	******
			Object value = advancedTable.getValueAt(row, INDEX_VALUE);
			if (value == null)
				continue;
			Object parsedValue = parseValue(field, value);
			if (parsedValue == null)
				continue;
			String infoDisplay = value.toString();
			if (field.isLookup())
				infoDisplay = field.getLookup().getDisplay(value);
			else if (field.getDisplayType() == DisplayType.YesNo)
				infoDisplay = Msg.getMsg(Env.getCtx(), infoDisplay);
			//	Value2	******
			if (MQuery.OPERATORS[MQuery.BETWEEN_INDEX].equals(op))
			{
				Object value2 = advancedTable.getValueAt(row, INDEX_VALUE2);
				if (value2 == null)
					continue;
				Object parsedValue2 = parseValue(field, value2);
				String infoDisplay_to = value2.toString();
				if (parsedValue2 == null)
					continue;
				m_query.addRangeRestriction(ColumnSQL, parsedValue, parsedValue2,
					infoName, infoDisplay, infoDisplay_to);
			}
			else
				m_query.addRestriction(ColumnSQL, Operator, parsedValue,
					infoName, infoDisplay);
		}
		
		//
		if (m_query.getRestrictionCount() == 0)
			return;
		String query = m_query.getWhereClause();
		String name = fQueryName.getText();
		if (name != null && name.length() > 0)
		{
			String where = m_query.getWhereClause();
			MUserQuery uq = MUserQuery.get(Env.getCtx(), m_AD_Tab_ID, name);
			if (uq == null)
			{
				uq = new MUserQuery (Env.getCtx(), 0, null);
				uq.setAD_Tab_ID(m_AD_Tab_ID);
			}
			uq.setName (name);
			uq.setCode (query);
			uq.setAD_Table_ID (m_AD_Table_ID);
			//
			if (uq.save())
				ADialog.info (m_targetWindowNo, this, "Saved", name);
			else
				ADialog.warn (m_targetWindowNo, this, "SaveError", name);
		}
	}	//	cmd_save


	/**
	 *	Save (Advanced)
	 */
	private void cmd_saveAdvanced()
	{
		log.fine("");
		//
		cmd_saveRow();	//	unsaved 
		m_query = getQueryAdvanced();
		if (m_query.getRestrictionCount() == 0)
			return;
		String where = m_query.getWhereClause();

		String name = savedQueryName.getText();
		if (name != null && name.length() == 0)
			name = null;
		
		//	Update Existing Query
		MUserQuery uq = (MUserQuery)comboQueriesA.getSelectedItem();
		if (uq != null)
		{
			if (name != null)
				uq.setName(name);
			uq.setCode (where);
			uq.setAD_Tab_ID	(m_AD_Tab_ID);
			uq.setAD_Table_ID (m_AD_Table_ID);
			//
			if (uq.save())
			{
				AdvancedRow.store(uq, advancedData);
				ADialog.info (m_targetWindowNo, this, "Updated", uq.getName());
			}
			else
				ADialog.warn (m_targetWindowNo, this, "SaveError", uq.getName());
		}
		else if (name != null)
		{
			uq = new MUserQuery (m_ctx, 0, null);
			uq.setName(name);
			uq.setCode (where);
			uq.setAD_Tab_ID	(m_AD_Tab_ID);
			uq.setAD_Table_ID (m_AD_Table_ID);
			//
			if (uq.save())
			{
				AdvancedRow.store(uq, advancedData);
				ADialog.info (m_targetWindowNo, this, "Saved", uq.getName());
			}
			else
				ADialog.warn (m_targetWindowNo, this, "SaveError", uq.getName());
		}
	}	//	cmd_saveAdvanced


	/**
	 * 	Get Query from Advanced Tab
	 *	@return query
	 */
	private Query getQueryAdvanced()
	{
		Query query = new Query(m_tableName);
		for (int i = 0; i < advancedData.size(); i++)
		{
			AdvancedRow row = advancedData.get(i);
			//	Column
			ValueNamePair column = row.getColumn();
			String infoName = column.getName();
			String columnName = column.getValue();
			GridField field = getTargetMField(columnName);
			String columnSQL = field.getColumnSQL(false);
			//	Op
			String operator = row.getOperator().getValue();

			//	Value	******
			ValueNamePair value = row.getValue();
			Object parsedValue = parseValue(field, value);
			String infoDisplay = null;
			if (value == null)
			{
				if (Query.BETWEEN.equals(operator))
					continue;	//	no null in between
				parsedValue = Null.NULLString;
				infoDisplay = "NULL";
			}
			else
				infoDisplay = value.getName();
			//	Value2	******
			if (Query.BETWEEN.equals(operator))
			{
				ValueNamePair value2 = row.getValue2();
				if (value2 == null)
					continue;
				Object parsedValue2 = parseValue(field, value2);
				String infoDisplay_to = value2.getName();
				if (parsedValue2 == null)
					continue;
				query.addRangeRestriction(columnSQL, parsedValue, parsedValue2,
					infoName, infoDisplay, infoDisplay_to);
			}
			else
				query.addRestriction(columnSQL, operator, parsedValue,
					infoName, infoDisplay);
		}
		return query;
	}	//	getQueryAdvanced

	/**
	 * 	Parse Value
	 * 	@param field column
	 * 	@param in value
	 * 	@return data type corected value
	 */
	private Object parseValue (GridField field, Object in)
	{
		if (in == null)
			return null;
		int dt = field.getDisplayType();
		try
		{
			//	Return Integer
			if (dt == DisplayType.Integer
				|| (DisplayType.isID(dt) && field.getColumnName().endsWith("_ID")))
			{
				if (in instanceof Integer)
					return in;
				int i = Integer.parseInt(in.toString());
				return new Integer(i);
			}
			//	Return BigDecimal
			else if (DisplayType.isNumeric(dt))
			{
				if (in instanceof BigDecimal)
					return in;
				return DisplayType.getNumberFormat(dt).parse(in.toString());
			}
			//	Return Timestamp
			else if (DisplayType.isDate(dt))
			{
				if (in instanceof Timestamp)
					return in;
				long time = 0;
				try
				{
					time = DisplayType.getDateFormat_JDBC().parse(in.toString()).getTime();
					return new Timestamp(time);
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, in + "(" + in.getClass() + ")" + e);
					time = DisplayType.getDateFormat(dt).parse(in.toString()).getTime();
				}
				return new Timestamp(time);
			}
			//	Return Y/N for Boolean
			else if (in instanceof Boolean)
				return ((Boolean)in).booleanValue() ? "Y" : "N";
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, "Object=" + in, ex);
			String error = ex.getLocalizedMessage();
			if (error == null || error.length() == 0)
				error = ex.toString();
			StringBuffer errMsg = new StringBuffer();
			errMsg.append(field.getColumnName()).append(" = ").append(in).append(" - ").append(error);
			//
			ADialog.error(0, this, "ValidationError", errMsg.toString());
			return null;
		}

		return in;
	}	//	parseValue

	/**
	 * 	Parse Value
	 * 	@param field column
	 * 	@param in value
	 * 	@return data type corrected value
	 */
	private Object parseValue (GridField field, ValueNamePair pp)
	{
		if (pp == null)
			return null;
		int dt = field.getDisplayType();
		String in = pp.getValue();
		if (in == null || in.equals(Null.NULLString))
			return null;
		try
		{
			//	Return Integer
			if (dt == DisplayTypeConstants.Integer
				|| (FieldType.isID(dt) && field.getColumnName().endsWith("_ID")))
			{
				int i = Integer.parseInt(in);
				return Integer.valueOf(i);
			}
			//	Return BigDecimal
			else if (FieldType.isNumeric(dt))
			{
				return DisplayType.getNumberFormat(dt).parse(in);
			}
			//	Return Timestamp
			else if (FieldType.isDate(dt))
			{
				long time = 0;
				try
				{
					return Timestamp.valueOf(in);
				}
				catch (Exception e)
				{
					log.log(Level.WARNING, in + "(" + in.getClass() + ")" + e);
					time = DisplayType.getDateFormat(dt).parse(in).getTime();
				}
				return new Timestamp(time);
			}
		}
		catch (Exception ex)
		{
			log.log(Level.WARNING, "Object=" + in, ex);
			String error = ex.getLocalizedMessage();
			if (error == null || error.length() == 0)
				error = ex.toString();
			StringBuffer errMsg = new StringBuffer();
			errMsg.append(field.getColumnName()).append(" = ").append(in).append(" - ").append(error);
			//
			ADialog.error(0, this, "ValidationError", errMsg.toString());
			return null;
		}

		return in;
	}	//	parseValue

	/**
	 *	Delete
	 */
	private void cmd_delete()
	{
		log.info("");
		/**
		DefaultTableModel model = (DefaultTableModel)advancedTable.getModel();
		int row = advancedTable.getSelectedRow();
		if (row >= 0)
			model.removeRow(row);
		cmd_refresh();
		**/
		String name = fQueryName.getText();
		MUserQuery uq = MUserQuery.get(Env.getCtx(), m_AD_Tab_ID, name);
		if (uq == null)
			ADialog.warn (m_targetWindowNo, this, "NotFound", name);
		else
		{
			if (uq.delete(true))
				ADialog.info (m_targetWindowNo, this, "Deleted", name);
			else
				ADialog.warn (m_targetWindowNo, this, "DeleteError");
		}
	}	//	cmd_delete

	/**
	 *	Refresh
	 */
	private void cmd_refresh()
	{
		log.info("");
		int records = getNoOfRecords(m_query, true);
		setStatusDB (records);
		statusBar.setStatusLine("");
	}	//	cmd_refresh

	
	public int getGroupByOption()
	{
		if ( cbbGroupByOption == null)
			return 0;
		
		if ( cbbGroupByOption.getItemCount() == 0 )
			return 0;
		
		return Integer.parseInt( cbbGroupByOption.getValue().toString());
	}
	
	public String getTimeSheetStatus()
	{
		if ( fieldTimeSheetStatus.getValue() == null || fieldTimeSheetStatus.getValue().toString().length() == 0 )
			return null;
		
		return fieldTimeSheetStatus.getValue().toString();
	}

	public String getEarningInfo()
	{
		if ( fieldEarningInfo.getValue() == null || fieldEarningInfo.getValue().toString().length() == 0 )
			return null;
		
		return fieldEarningInfo.getValue().toString();
	}

	public String getColumnAdm()
	{
		if ( fieldColumnAdm.getValue() == null || fieldColumnAdm.getValue().toString().length() == 0 )
			return null;
		
		return fieldColumnAdm.getValue().toString();
	}

	
	public String getSheetType()
	{
		if ( fieldSheetType.getValue() == null || fieldSheetType.getValue().toString().length() == 0)
			return null;
		
		return fieldSheetType.getValue().toString();
	}
	
	
	

	public int getP_Calendar_ID()
	{
		return Integer.parseInt( this.fieldCalendar.getValue().toString());
	}

	public int getP_Credits_Family_ID()
	{
		return Integer.parseInt( this.fieldCreditsFamily.getValue().toString());
	}

	public int getP_Year_ID()
	{
		return Integer.parseInt( this.m_cbbPayYear.getValue().toString());
	}

	
	public int getP_Period_From_ID()
	{
		return Integer.parseInt( this.m_cbbPayPeriodFrom.getValue().toString());
	}

	public int getC_Period_ID()
	{
		return Integer.parseInt( this.m_cbbPeriodGl.getValue().toString());
	}

	public int getP_Period_To_ID()
	{
		return Integer.parseInt( this.m_cbbPayPeriodTo.getValue().toString());
	}

	public Timestamp getDate_From()
	{
		return (Timestamp)fieldSelectionDateFrom.getValue();
	}


	public boolean isSearchPeriod()
	{
		return isSearchPeriod.isSelected();
	}

	public boolean isSearchPeriodPayment()
	{
		return isSearchPeriodPayment.isSelected();
	}

	public boolean isSearchDate()
	{
		return isSearchDate.isSelected();
	}

	public boolean isRefreshData()
	{
		return isRefreshData.isSelected();
	}
	
	public int getLongTermLeave_ID()
	{
		if ( fieldLongTermLeave.getValue() == null || fieldLongTermLeave.getValue().toString().length() == 0 )
			return -1;
		
		return Integer.parseInt( fieldLongTermLeave.getValue().toString());
	}

	
	public String getEmployeeStatus()
	{
		if ( fieldEmployeeStatus.getValue() == null || fieldEmployeeStatus.getValue().toString().length() == 0 )
			return null;

		return fieldEmployeeStatus.getValue().toString();

	}

	
	public String getDeductionCategory()
	{
		if ( fieldDeductionCategory.getValue() == null || fieldDeductionCategory.getValue().toString().length() == 0 )
			return null;
		
		return fieldDeductionCategory.getValue().toString();
	}
	
	public String getDeductionFamily()
	{
		if ( fieldDeductionFamily.getValue() == null || fieldDeductionFamily.getValue().toString().length() == 0 )
			return null;
		
		return fieldDeductionFamily.getValue().toString();
	}
	
	public String getMovementType()
	{
		if ( fieldMovementType.getValue() == null || fieldMovementType.getValue().toString().length() == 0 )
			return null;

		return fieldMovementType.getValue().toString();

	}

	public String getMovementOrigin()
	{
		if ( fieldMovementOrigin.getValue() == null || fieldMovementOrigin.getValue().toString().length() == 0 )
			return null;

		return fieldMovementOrigin.getValue().toString();

	}

	// Vacance
	public BigDecimal getBenefitsRate1()  
	{
		return (BigDecimal)fieldBenefitsRate1.getValue();
	}

	// Maladie
	public BigDecimal getBenefitsRate2()
	{
		return (BigDecimal)fieldBenefitsRate2.getValue();
	}

	// Temps Suplémentaire
	public BigDecimal getBenefitsRate3()
	{
		return (BigDecimal)fieldBenefitsRate3.getValue();
	}

	
	public Timestamp getEvaluationDate()
	{
		return (Timestamp)fieldEvaluationDate.getValue();
	}

	public boolean getIsMonetaryValue()
	{
		return cbMonetaryValue.isSelected();
	}
	
	public Timestamp getDate_To()
	{
		return (Timestamp)fieldSelectionDateTo.getValue();
	}

	
	
	/**************************************************************************
	 *	Get Query - Retrieve result
	 *  @return String representation of query
	 */
	public Query getQuery()
	{
		//TODO Modifier
		if (m_hasBeenCancelled)
		{
			return null;
		}
		MRole role = MRole.getDefault();
		if (role.isQueryMax(getTotalRecords()))
		{
			m_query = Query.getNoRecordQuery (m_tableName, false);
			m_total = 0;
			log.warning("Query - over max");
		}
		else
			log.info("Query=" + m_query);
		return m_query;
	}	//	getQuery

	/**
	 * 	Get selected number of days
	 * 	@return days or -1 for all
	 */
	public int getCurrentDays()
	{
		return m_days;
	}	//	getCurrentDays

	/**
	 * 	Get created or updated
	 * 	@return true if created
	 */
	public boolean getIsCreated()
	{
		return m_created;
	}	//	getIsCreated

	/**
	 *	Get the number of records of target tab
	 *  @param query where clause for target tab
	 * 	@param alertZeroRecords show dialog if there are no records
	 *  @return number of selected records
	 */
	private int getNoOfRecords (Query query, boolean alertZeroRecords)
	{
/*		log.config(query == null ? "" : query.toString());
		StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM ");
		sql.append(m_tableName);
		boolean hasWhere = false;
		if (m_whereExtended != null && m_whereExtended.length() > 0)
		{
			sql.append(" WHERE ").append(m_whereExtended);
			hasWhere = true;
		}
		if (query != null && query.isActive())
		{
			if (hasWhere)
				sql.append(" AND ");
			else
				sql.append(" WHERE ");
			sql.append(query.getWhereClause());
		}
		//	Add Access
		String finalSQL = MRole.getDefault().addAccessSQL(sql.toString(), 
			m_tableName, MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
		finalSQL = Env.parseContext(m_ctx, m_targetWindowNo, finalSQL, false);
//		m_ctx.setContext(m_targetWindowNo, TABNO, "FindSQL", finalSQL);

		//  Execute Qusery
		m_total = 999999;
		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(finalSQL);
			if (rs.next())
				m_total = rs.getInt(1);
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, finalSQL, e);
		}
*/		
		m_total = 999;
		MRole role = MRole.getDefault();
		 
		//	No Records
		if (m_total == 0 && alertZeroRecords)
			ADialog.info(m_targetWindowNo, this, "FindZeroRecords");
		//	More then allowed
		else if (query != null && role.isQueryMax(m_total))
			ADialog.error(m_targetWindowNo, this, "FindOverMax", 
				m_total + " > " + role.getMaxQueryRecords());
		else
			log.config("#" + m_total);
		//
		if (query != null)
			statusBar.setStatusToolTip (query.getWhereClause());
			
		return m_total;
	}	//	getNoOfRecords

	/**
	 * 	Get Total Records
	 *	@return no of records
	 */
	public int getTotalRecords()
	{
		return m_total;
	}	//	getTotalRecords

	/**
	 *	Display current count
	 *  @param currentCount String representation of current/total
	 */
	private void setStatusDB (int currentCount)
	{
		String text = " " + currentCount + " / " + m_total + " ";
		statusBar.setStatusDB(text);
	}	//	setDtatusDB

	/**************************************************************************
	 *	Grid Status Changed.
	 *  @param e DataStatueEvent
	 */
	public void dataStatusChanged (DataStatusEvent e)
	{
		log.config(e.getMessage());
		//	Action control
		boolean changed = e.isChanged();
		bIgnore.setEnabled(changed);
	}	//	statusChanged

	/**
	 * 	Get Target MField
	 * 	@param columnName column name
	 * 	@return MField
	 */
	public GridField getTargetMField (String columnName)
	{
		if (columnName == null)
			return null;
		for (int c = 0; c < m_findFields.length; c++)
		{
			GridField field = m_findFields[c];
			if (columnName.equals(field.getColumnName()))
				return field;
		}
		return null;
	}	//	getTargetMField
    
    /**
     * Renderer to help tabbing work for editing a cell.
     */
    private class ProxyRenderer implements TableCellRenderer
    {
        /**
         * Creates a Find.ProxyRenderer.
         */
        public ProxyRenderer(TableCellRenderer renderer)
        {
            this.m_renderer = renderer;
        }
        
        /** The renderer. */
        private TableCellRenderer m_renderer;
        
        /**
         * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
         */
        public Component getTableCellRendererComponent(final JTable table,
        	Object value, boolean isSelected, boolean hasFocus, final int row, final int col)
        {
            Component comp = m_renderer.getTableCellRendererComponent(table,
            	value, isSelected, hasFocus, row, col);
            if (hasFocus && table.isCellEditable(row, col))
                table.editCellAt(row, col);
            return comp;
        }
    }	// ProxyRenderer

    
	private void fillCreditsFamily()
	{
		String sql = "select P_CREDITS_FAMILY_ID, case when P_CREDITS_FAMILY_ID = 105 then 'Temps Supplémentaire' else Name end as name from P_CREDITS_FAMILY  where P_CREDITS_FAMILY_ID in ( 102,104,105)";
        try
        {
	        PreparedStatement stmt = DB.prepareStatement(sql, null);
	        ResultSet rs = stmt.executeQuery();

	        int i = 0;
	        while(rs.next())
	        {
	        	fieldCreditsFamily.addItem(new KeyNamePair(rs.getInt("P_Credits_Family_ID"), rs.getString("Name")));
	        	i = i + 1;
	        }
	        
	        rs.close();
	        stmt.close();
	    }
	    catch (SQLException e)
	    {
	    	log.log(Level.SEVERE, "loadComboBox", e);
	    }

	}

	/**
	 * On remplit les combobox de périodes en leur affectant la période ouverte comme valeur par défaut
	 */

	private void fillCalendarComboBox()
	{
        // Calendar
        String sql = "SELECT P_Calendar.P_Calendar_ID, P_Calendar.NAME, isDefault "
            + " From P_Calendar " ;
        
        if ( fieldPaymentGroup.getDisplay() != null && fieldPaymentGroup.getValue() != null )
        {
        	int P_Payment_Group_ID = Integer.parseInt( fieldPaymentGroup.getValue().toString() );
        	P_Payment_Group PaymentGroup = P_Payment_Group.get(Env.getCtx(), P_Payment_Group_ID, null);
        	sql += " where P_Calendar_ID =" + PaymentGroup.getP_Calendar_ID();
        }
        sql += " ORDER BY P_Calendar.NAME";
        try
        {
        	if ( fieldCalendar != null && fieldCalendar.getItemCount() != 0)
        	{
        		fieldCalendar.setSelectedIndex(0);
            	fieldCalendar.removeAllItems();
        	}
        }
        catch (Exception e)
        {
            log.log(Level.SEVERE,"loadComboBox", e);
        }
	    try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sql, null);
	        ResultSet rs = stmt.executeQuery();
	        fieldCalendar.setName("P_Calendar_ID");
//	        fieldCalendar.addItem(new KeyNamePair(0, ""));

	        int i = 0;
	        while(rs.next())
	        {
	        	fieldCalendar.addItem(new KeyNamePair(rs.getInt("P_Calendar_ID"), rs.getString("Name")));
	        	if ( rs.getString("IsDefault").equals("Y"))
	        		fieldCalendar.setSelectedIndex(i);
	        	i = i + 1;
	        }
	        
//        	if ( fieldCalendar != null && fieldCalendar.getItemCount() != 0)
//        		fieldCalendar.setSelectedIndex(0);
	        rs.close();
	        stmt.close();
	    }
	    catch (SQLException e)
	    {
	    	log.log(Level.SEVERE, "loadComboBox", e);
	    }
	}

	private void fillComboBox()
	{

        String sql = null;


        int P_Calendar_ID =  Integer.parseInt( fieldCalendar.getValue().toString() );

	    // Period
		sql = "select P_Year.P_Year_ID, P_Year.Year AS Name, P_Year.IsDefault "
				   + " From P_Year " 
				   + " WHERE P_Year.IsActive = 'Y' " 
				   + " AND P_Year.P_Calendar_ID = "  + P_Calendar_ID  
	     	   + " ORDER BY P_Year.Year DESC";
	        try
	        {
	        	if ( m_cbbPayYear != null && m_cbbPayYear.getItemCount() != 0)
	        		m_cbbPayYear.setSelectedIndex(0);
	        	m_cbbPayYear.removeAllItems();
	        }
	        catch (Exception e)
	        {
	            log.log(Level.SEVERE,"loadComboBox", e);
	        }
		    try
		    {

		        int index = 0;
		        PreparedStatement stmt = DB.prepareStatement(sql, null);
		        ResultSet rs = stmt.executeQuery();
		        m_cbbPayYear.setName("P_Year");

		        m_cbbPayYear.addItem(new KeyNamePair(0, ""));

		        while(rs.next())
		        {
		            this.m_cbbPayYear.addItem(new KeyNamePair(rs.getInt("P_Year_ID"), rs.getString("Name")));
		            if(rs.getString("IsDefault").toUpperCase().equals("Y"))
		            {
		                this.m_cbbPayYear.setSelectedIndex(index + 1);
		            }
		            index++;
		        }
		        rs.close();
		        stmt.close();
		    }
		    catch (SQLException e)
		    {
		    	log.log(Level.SEVERE, "loadComboBox", e);
		    }
    
	        
		sql = "select P_Period.P_Period_ID, P_Period.SynonymName AS Name, P_Period.PeriodStatus "
			   + " From P_Period " 
		       + " Inner Join P_Year ON P_Year.P_Year_ID = P_Period.P_Year_ID "
               + " inner join P_Frequency on P_Frequency.P_Frequency_ID = P_PERIOD.P_Frequency_ID " 
			   + " WHERE P_Period.IsActive = 'Y' " 
			   + " AND P_Year.P_Calendar_ID = "  + P_Calendar_ID  
     	   + " ORDER BY P_Period.Name DESC";
        try
        {
        	if ( m_cbbPayPeriodFrom != null && m_cbbPayPeriodFrom.getItemCount() != 0)
        		m_cbbPayPeriodFrom.setSelectedIndex(0);
        	m_cbbPayPeriodFrom.removeAllItems();

        	if ( m_cbbPayPeriodTo != null && m_cbbPayPeriodTo.getItemCount() != 0)
        		m_cbbPayPeriodTo.setSelectedIndex(0);
        	m_cbbPayPeriodTo.removeAllItems();
        }
        catch (Exception e)
        {
            log.log(Level.SEVERE,"loadComboBox", e);
        }
	    try
	    {

	        int index = 0;
	        PreparedStatement stmt = DB.prepareStatement(sql, null);
	        ResultSet rs = stmt.executeQuery();
	        m_cbbPayPeriodFrom.setName("PeriodFrom");
	        m_cbbPayPeriodTo.setName("PeriodTo");
	        
	        while(rs.next())
	        {
	            this.m_cbbPayPeriodFrom.addItem(new KeyNamePair(rs.getInt("P_Period_ID"), rs.getString("Name")));
	            this.m_cbbPayPeriodTo.addItem(new KeyNamePair(rs.getInt("P_Period_ID"), rs.getString("Name")));
	            if(rs.getString("PeriodStatus").toUpperCase().equals("O"))
	            {
	                this.m_cbbPayPeriodFrom.setSelectedIndex(index);
	                this.m_cbbPayPeriodTo.setSelectedIndex(index);
	            }
	            index++;
	        }
	        rs.close();
	        stmt.close();
	    }
	    catch (SQLException e)
	    {
	    	log.log(Level.SEVERE, "loadComboBox", e);
	    }
	    
    }

	public void setGroupByOption ( VComboBox GroupByOption )
	{
		cbbGroupByOption = GroupByOption;
	}

	public void setGroupByOption ( int key, String description)
	{
/*	    m_TypeQuery.addItem(new KeyNamePair(1, "Sommaire"));
	    m_TypeQuery.addItem(new KeyNamePair(2, "Détaillé"));
	    m_TypeQuery.addItem(new KeyNamePair(3, "Très Détaillé"));
*/
		cbbGroupByOption.addItem( new KeyNamePair(key, description) );
	}


	public void setQueryType ( int Type)
	{
		m_QueryType = Type;
	}

	/**
	 * 	Value Changed - Advanced Table selection changed
	 *	@param e event
	 */
	public void valueChanged(ListSelectionEvent e)
    {
		if (e.getValueIsAdjusting())
			return;
		int index = advancedTable.getSelectedRow();
		bDeleteRow.setEnabled(index != -1);
		log.fine("#" + index);
    }	//	valueChanged


	/**
	 * 	Load saved Query - Advanced
	 */
	private void cmd_savedQueryLoad()
	{
		MUserQuery uq = (MUserQuery)comboQueriesA.getSelectedItem();
		if (uq == null)
		{
			savedQueryName.setText(null);
			savedQueryName.setReadWrite(false);
			return;
		}
//		log.info(uq.toStringX());
		advancedData.removeAllElements();
		
		//	Load Info Table
		Vector<AdvancedRow> ad = AdvancedRow.load(uq);
		if (ad != null)
		{
			for (int i = 0; i < ad.size(); i++)
            {
	            AdvancedRow row = ad.get(i);
				advancedData.add(row);
            }
		}
		((DefaultTableModel)advancedTable.getModel())
			.fireTableDataChanged();
		advancedTable.getSelectionModel().clearSelection();
		bDeleteQuery.setEnabled(true);
		savedQueryName.setText(uq.getName());
		savedQueryName.setReadWrite(true);
	}	//	cmd_savedQueryLoad

	/**
	 * 	Save Advanced Row
	 * 	@return true if row saved
	 */
	private boolean cmd_saveRow()
	{
		ValueNamePair column = (ValueNamePair)columns.getSelectedItem();
		if (column == null || column.getValue().length() == 0)
			return false;
		//
		ValueNamePair operator = (ValueNamePair)operators.getSelectedItem();
		String op = operator.getValue();
		//	Value
		Object value = valueEditor.getValue();
		String valueDisplay = valueEditor.getDisplay();  
		/**	List - (NOT) IN
		Object[] values = null;
		String[] valuesDisplay = null;
		if (Query.EQUAL.equals(op) || Query.NOT_EQUAL.equals(op)
			|| Query.IN.equals(op) || Query.NOT_IN.equals(op))
		{
			values = valueEditor.getValues();
			if (values == null || values.length == 1)
			{
				if (Query.IN.equals(op))
					op = Query.EQUAL;
				if (Query.NOT_IN.equals(op))
					op = Query.NOT_EQUAL;
			}
			else
			{
				valuesDisplay = valueEditor.getDisplays();
				if (Query.EQUAL.equals(op))
					op = Query.IN;
				if (Query.NOT_EQUAL.equals(op))
					op = Query.NOT_IN;
			}
		}	*/
		ValueNamePair v = null;
		if (value == null)
		{
			if (Query.EQUAL.equals(op) || Query.NOT_EQUAL.equals(op))
				v = new ValueNamePair(Null.NULLString, "NULL");
			else
			{
				operators.setSelectedIndex(0);
				return false;
			}
		}
		else
		{
			if (value instanceof Boolean)
				value = ((Boolean)value).booleanValue() ? "Y" : "N";
			v = new ValueNamePair(value.toString(), valueDisplay);
		}
		
		//	Value 2
		ValueNamePair v2 = null;
		if (Query.BETWEEN.equals(op))
		{
			Object value2 = valueEditor2.getValue();
			String value2Display = valueEditor2.getDisplay();
			if (value2 == null)
				return false;
			else
			{
				if (value2 instanceof Boolean)
					value2 = ((Boolean)value).booleanValue() ? "Y" : "N";
				v2 = new ValueNamePair(value2.toString(), value2Display);
			}
		}
		
		AdvancedRow row = new AdvancedRow(column, operator, v, v2);
		log.info(row.toString());
		int index = advancedData.size();
		advancedData.add(row);
		((DefaultTableModel)advancedTable.getModel())
			.fireTableRowsInserted(index, index);
		advancedTable.getSelectionModel().clearSelection();
		columns.setSelectedIndex(0);
		operators.setSelectedIndex(0);
		valueEditor.setValue(null);
		valueEditor2.setValue(null);
		savedQueryName.setReadWrite(true);
		return true;
	}	//	cmd_saveRow
	
	/**
	 * 	Delete selected Advanced Row
	 */
	private void cmd_deleteRow()
	{
		int index = advancedTable.getSelectedRow();
		log.info("#" + index);
		if (index == -1)
			return;
		advancedData.remove(index);
		((DefaultTableModel)advancedTable.getModel())
			.fireTableRowsDeleted(index, index);
		advancedTable.getSelectionModel().clearSelection();
	}	//	cmd_deleteRow

	/**
	 *	Delete User Query
	 */
	private void cmd_savedQueryDelete()
	{
		MUserQuery uq = (MUserQuery)comboQueriesA.getSelectedItem();
		if (uq == null)
			return;
//		log.info(uq.toStringX());
		String name = uq.getName(); 
		if (uq.delete(true))
		{
			ADialog.info (m_targetWindowNo, this, "Deleted", name);
			MutableComboBoxModel modelA = (MutableComboBoxModel)comboQueriesA.getModel();
			modelA.removeElement(uq);
			modelA.setSelectedItem(null);
			savedQueryName.setText(null);
			savedQueryName.setReadWrite(false);
		}
		else
			ADialog.warn (m_targetWindowNo, this, "DeleteError");
	}	//	cmd_delete

}	//	Find
