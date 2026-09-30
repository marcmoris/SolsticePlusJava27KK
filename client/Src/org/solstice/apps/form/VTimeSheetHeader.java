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
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;

import org.compiere.apps.ADialog;
import org.compiere.apps.AEnv;
import org.compiere.apps.AWindow;
import org.compiere.apps.ProcessCtl;
//import org.compiere.apps.Waiting;
import org.compiere.model.MPInstance;
import org.compiere.model.MProcess;
import org.compiere.model.MQuery;
import org.compiere.model.MRole;
import org.compiere.model.MSession;
import org.compiere.plaf.CompiereColor;
import org.compiere.process.ProcessInfo;
import org.compiere.swing.CButton;
import org.compiere.swing.CComboBox;
import org.compiere.swing.CDialog;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.swing.CTextField;
import org.compiere.util.ASyncProcess;
import org.compiere.util.CLogger;
import org.compiere.util.CacheMgt;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.compiere.util.ValueNamePair;
import org.compiere.util.TimeUtil;

import solstice.model.P_Employee;
import solstice.model.P_Job_Title;
import solstice.model.P_Job_Type;
import solstice.model.P_Collective_Labour_Agr;
import solstice.model.P_Payment;
import solstice.model.P_Payment_Group;
import solstice.model.P_Period;
import solstice.model.P_Time_Sheet;
import solstice.process.P_Payment_Cancel;
import solstice.model.P_Distribution_Booklet;
import solstice.model.P_Occupation_Group;

/**
 * @author frabou01 
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class VTimeSheetHeader extends CPanel implements ActionListener, ASyncProcess
{	
	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (VTimeSheetHeader.class);

    private int currentPeriodID = -1;
    private int m_defaultPeriodIndex = -1;
    private int mP_Calendar_ID = 1000008;
    
    private Properties m_ctx;

    //
    VTimeSheetMain m_main;

    private int currentTimeID = 0;

    private P_Time_Sheet m_timeSheet = null;
    public P_Time_Sheet getTimeSheet() { return m_timeSheet; }

    private int currentEmployeeID = -1;
    public int getCurrentEmployeeID() { return currentEmployeeID; }

    private int currentTimeSheetIndex = -1;

    private Vector<BigDecimal> timeSheetList = null;

    private VTimeSheetDetail m_detail = null;
    
    public void setDetail( VTimeSheetDetail detail ) { m_detail = detail; }
    public void loadTimeSheet( int timeSheetId ) {
    	String trxName = null;
        m_timeSheet = new P_Time_Sheet(Env.getCtx(), timeSheetId, trxName);
    }
    
    public void createTimeSheet( String trxName ) {
        ValueNamePair pp;

 //TODO
//        pp = (ValueNamePair) m_cbbOrg.getSelectedItem();
//        int orgID = Integer.parseInt(pp.getValue());

        pp = (ValueNamePair) m_cbbPayPeriod.getSelectedItem();
        int periodID = Integer.parseInt(pp.getValue());

        pp = (ValueNamePair) this.m_cbbTaxation_Region.getSelectedItem();
        int taxation_Region_ID = Integer.parseInt(pp.getValue());

        pp = (ValueNamePair) m_cbbPaymentType.getSelectedItem();
        String paymentType = pp.getValue();

//        pp = (ValueNamePair) m_cbbEmployer.getSelectedItem();
//        int Employer_ID = Integer.parseInt(pp.getValue());

        P_Period period = P_Period.get( Env.getCtx(), periodID, trxName );
        P_Employee employee = P_Employee.get( Env.getCtx(), currentEmployeeID, trxName );
        
		m_timeSheet = new P_Time_Sheet( Env.getCtx (), -1, trxName);

		
		String value = DB.getDocumentNo( employee.getAD_Client_ID() , m_timeSheet.get_TableName(), trxName );

		m_timeSheet.setAD_Org_ID( employee.getAD_Org_ID());

//		m_timeSheet.setAD_Org_ID( orgID );
		
		m_timeSheet.setP_Department_ID( employee.getP_Department_ID());
		m_timeSheet.setC_Activity_ID(employee.getC_Activity_ID());
		
		m_timeSheet.setValue( period.getName() + "-" + value.substring( 3, 7 ) );

		m_timeSheet.setIsComplete( false );
		m_timeSheet.setP_Employee_ID( employee.getP_Employee_ID());
		m_timeSheet.setP_Frequency_ID( period.getP_Frequency_ID() );
		m_timeSheet.setP_Year_ID( period.getP_Year_ID());
		m_timeSheet.setP_Period_ID( period.getP_Period_ID());
		m_timeSheet.setPaymentType( paymentType );
		m_timeSheet.setTaxation_Region_ID( taxation_Region_ID );
//		m_timeSheet.setP_Employer_ID( Employer_ID );
		
		if ( timeSheetList.size() > 0 )
		{
			// Les ajustements sont toujours de type depots.
			m_timeSheet.setSheetType( P_Time_Sheet.SHEETTYPE_Adjustment ); // .SHEETTYPE_Complementary
			m_timeSheet.setPaymentType( P_Time_Sheet.PAYMENTTYPE_Deposit );
		}
		else
			m_timeSheet.setSheetType( P_Time_Sheet.SHEETTYPE_Regular );
		m_timeSheet.setTimeSheetStatus( P_Time_Sheet.TIMESHEETSTATUS_Initial );
		m_timeSheet.setIsError( false );
		m_timeSheet.setIsWarning( false );
		m_timeSheet.setP_Payment_Group_ID( employee.getP_Payment_Group_ID()  );
//+ 2011.11.16 ajout numéro employeur et lieu de travail sur la feuille de paie
	    m_timeSheet.setP_Employer_ID( employee.getP_Employer_ID());
	    m_timeSheet.setP_Workplace_ID( employee.getP_Workplace_ID());
//-	2011.11.16 			    

		
		//2010.11.11 
		// m_timeSheet.setP_Distribution_Booklet_ID(employee.getP_Distribution_Booklet_ID());
		m_timeSheet.setP_Distribution_Booklet_ID(employee.getP_Distribution_Booklet_ID(period.getEndDate()));

		m_timeSheet.setP_Distribution_ID(employee.getP_Distribution_ID());
		m_timeSheet.setP_Job_Title_ID(employee.getP_Job_Title_ID());
		m_timeSheet.setP_Job_Type_ID(employee.getP_Job_Type_ID());
		m_timeSheet.setP_Occupation_Group_ID(employee.getP_Occupation_Group_ID());
		m_timeSheet.setP_Collective_Labour_Agr_ID(employee.getP_Collective_Labour_Agr_ID());

		//+2009.05.14 magneto 750 - La feuille de temps régulier reçois la date de l'avant comme date du paiement.
        /*		
        		int year  = Integer.parseInt( this.m_txtPeriodPay.getText().substring(0,4));
                int month = Integer.parseInt( this.m_txtPeriodPay.getText().substring(5,7));
                int day   = Integer.parseInt( this.m_txtPeriodPay.getText().substring(8,10));
                m_timeSheet.setPayDate( TimeUtil.getDay( year, month, day )  );
        */

        m_txtPeriodPay.setText( period.getPayDate().toString().substring(0,10));
        m_timeSheet.setPayDate( period.getPayDate()  );
         //-2009.05.14

		
		m_timeSheet.save( trxName );
		
		setupValidationProcessButton();
		
        timeSheetList.add( new BigDecimal(m_timeSheet.getP_Time_Sheet_ID()) );
        currentTimeSheetIndex = timeSheetList.size()-1;
        enableBtnNext(false);
        enableBtnBefore( timeSheetList.size() > 1 );

        m_main.reloadInfo();
        m_main.enableSave( true );

    }
    
    public VTimeSheetHeader(VTimeSheetMain main)
    {
        super();
        m_ctx = Env.getCtx();
        m_main = main;
        timeSheetList = new Vector<BigDecimal>();

        initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private TitledBorder m_borderTitre;
    
    private CPanel panel;
    
    private void initialize()
    {
        CompiereColor.setBackground( this );
        
        setLayout(new BorderLayout());
        Font font = getFont().deriveFont( Font.BOLD, getFont().getSize2D()+3.0f );
        m_borderTitre = BorderFactory.createTitledBorder(null, Msg.translate(Env.getCtx(), "TimeSheet"), TitledBorder.LEFT, TitledBorder.TOP, font, Color.BLACK );
        setBorder( m_borderTitre );
        
        panel = new CPanel( new GridBagLayout() );
        GridBagConstraints gbc = new GridBagConstraints();
        add( panel, BorderLayout.CENTER );
        panel.setOpaque(false);

        gbc.insets = new Insets(1,2,1,2);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridheight = 1;

// ligne 1        
        gbc.gridwidth = 1;
        CLabel lblPeriodStart = new CLabel(Msg.translate(Env.getCtx(),"PeriodDate" )); // Msg.getMsg(Env.getCtx(),"StartDate" )
        gbc.weightx = 0;
        panel.add( lblPeriodStart, gbc );
        gbc.weightx = 1;
        panel.add( initTxtPeriodStart(), gbc );
        
/*        CLabel lblPeriodEnd = new CLabel(Msg.getMsg(Env.getCtx(),"EndDate" ));
        gbc.weightx = 0;
        panel.add( lblPeriodEnd, gbc );
        gbc.weightx = 1;
        panel.add( initTxtPeriodEnd(), gbc );
*/
        CLabel lblPeriodPay = new CLabel(Msg.translate(Env.getCtx(),"PayDate" ));
        gbc.weightx = 0;
        panel.add( lblPeriodPay, gbc );
        gbc.weightx = 1;
        panel.add( initTxtPeriodPay(), gbc );

        CLabel LbPeriod = new CLabel(Msg.translate(Env.getCtx(), "P_Period_ID"));
        gbc.weightx = 0;
        panel.add( LbPeriod, gbc );
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        panel.add( initCbbPayPeriod(), gbc );


// ligne 2        
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        panel.add( new CLabel(Msg.translate(Env.getCtx(), "DistributionBooklet")), gbc );
        gbc.weightx = 1;
        panel.add( initTxtBooklet(), gbc );
        
        gbc.weightx = 0;
        panel.add( new CLabel(Msg.translate(Env.getCtx(), "CollectiveLabourAgr")), gbc );
        gbc.weightx = 1;
        panel.add( initTxtConvention(), gbc );

        CLabel lblSheetStatus = new CLabel(Msg.translate(Env.getCtx(), "TimeSheetStatus"));
        gbc.weightx = 0;
        panel.add( lblSheetStatus, gbc );
        gbc.weightx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        panel.add( initTxtSheetStatus(), gbc );

// ligne 3
        
//        gbc.gridwidth = 1;
//        CLabel lblSheetNumber = new CLabel(Msg.translate(Env.getCtx(), "P_Time_Sheet_ID"));
//        gbc.weightx = 0;
//        panel.add( lblSheetNumber, gbc );
//        gbc.weightx = 1;
//        panel.add( initTxtSheetNumber(), gbc );

        gbc.gridwidth = 1;
		CLabel lblSheetType = new CLabel(Msg.translate(Env.getCtx(), "SheetType"));
		gbc.weightx = 0;
		panel.add( lblSheetType, gbc );
		gbc.weightx = 1;
		panel.add( initCbbSheetType(), gbc );



        gbc.weightx = 0;
        panel.add( Box.createGlue(), gbc );
        
        Box beforeNextBox = Box.createHorizontalBox(); 
        beforeNextBox.add(initBtnBefore());
        beforeNextBox.add(initBtnNext());
        gbc.weightx = 1;
        panel.add( beforeNextBox, gbc );
        
        gbc.weightx = 0;
        panel.add( Box.createGlue(), gbc );
        
        Box initialProcessBox = Box.createHorizontalBox();
        initialProcessBox.add( initBtnInitial() );
        initialProcessBox.add( initBtnProcess() );
        initialProcessBox.add( initBtnMoreInfo() );

        initialProcessBox.add( initBtnGenBonus() );

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        panel.add( initialProcessBox, gbc );

//      ligne 6
        gbc.gridwidth = 1;
        CLabel lblOrg = new CLabel(Msg.translate(Env.getCtx(), "AD_Org_ID"));
        gbc.weightx = 0;
        panel.add( lblOrg, gbc );
        gbc.weightx = 1;
        panel.add( initCbbOrg(), gbc );

        gbc.gridwidth = 1;
        CLabel lblTaxation_Region = new CLabel(Msg.translate(Env.getCtx(), "Taxation_Region_ID"));
        gbc.weightx = 0;
        panel.add( lblTaxation_Region, gbc );
        gbc.weightx = 1;
        panel.add( initCbbTaxation_Region(), gbc );

        gbc.gridwidth = 1;
        CLabel lblActivity = new CLabel(Msg.translate(Env.getCtx(), "C_Activity_ID"));
        gbc.weightx = 0;
        panel.add( lblActivity, gbc );
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        panel.add( initCbbActivity(), gbc );

        /*
        if ( Env.getAD_Client_ID(Env.getCtx()) == 11 )
        {
        	lblOrg.setVisible( false );
        	lblTaxation_Region.setVisible( false );
        	lblActivity.setVisible( false );
        	this.m_cbbTaxation_Region.setVisible( false );
        	this.m_cbbOrg.setVisible(false);
        	this.m_cbbActivity.setVisible(false);
        }
*/
        
        CLabel lbJobTitle = new CLabel(Msg.translate(Env.getCtx(), "TitreEmploi"));
//        CLabel lbJobTitle = new CLabel(Msg.translate(Env.getCtx(), "JobType"));
        // ligne 4
        gbc.gridwidth = 1;
        if ( Env.getAD_Client_ID(Env.getCtx()) == 11 )
        	panel.add( lbJobTitle, gbc );
        else
        	panel.add( new CLabel(Msg.translate(Env.getCtx(), "P_Occupation_Group_ID")), gbc );

        gbc.weightx = 1;
        panel.add( initTxtJobTitle(), gbc );

        gbc.weightx = 0;
        panel.add( new CLabel(Msg.translate(Env.getCtx(), "StatutEmploi")), gbc );
        gbc.weightx = 1;
        panel.add( initTxtJobType(), gbc );
        
        CLabel lblGrossEarnings = new CLabel(Msg.translate(Env.getCtx(), "PayGross"));
        gbc.weightx = 0;
        panel.add( lblGrossEarnings, gbc );
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        panel.add( initTxtGrossEarnings(), gbc );
        
// ligne 5        
        
        gbc.gridwidth = 1;
        CLabel lblPaymentType = new CLabel(Msg.translate(Env.getCtx(), "PaymentType"));
        gbc.weightx = 0;
        panel.add( lblPaymentType, gbc );
        gbc.weightx = 1;
        panel.add( initCbbPaymentType(), gbc );

        CLabel lblEmployer = new CLabel(Msg.translate(Env.getCtx(), "P_Employer_ID"));
        gbc.weightx = 0;
        panel.add( lblEmployer, gbc );
        gbc.weightx = 1;
        panel.add( initCbbEmployer(), gbc );
        
        CLabel lblNetPay = new CLabel(Msg.translate(Env.getCtx(),"NetPay"));
        gbc.weightx = 0;
        panel.add( lblNetPay, gbc );     
        gbc.weightx = 2;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        panel.add( initTxtNetPay(), gbc );        


     // ligne 6        

        loadOrg();
        loadTaxation_Region();
        loadActivity();
        int P_Calendar_ID = getPayCalendar();
        loadPayPeriod( P_Calendar_ID);
        loadPaymentType();
        loadEmployer();
        loadSheetType();
    }
    
    private static final Dimension MIN_SIZE_BT = new Dimension(50, 22);
    private static final Dimension MIN_SIZE = new Dimension(180, 22);
    private static final Dimension PREF_SIZE = new Dimension(180, 22);
    private static final Dimension MAX_HEIGHT = new Dimension(Integer.MAX_VALUE, 22);
    private void setupSize( JComponent component ) {
    	component.setMinimumSize( MIN_SIZE );
    	component.setPreferredSize( PREF_SIZE );
    	component.setMaximumSize( MAX_HEIGHT );
    }

    private void setupSize2( JComponent component ) {
    	component.setMinimumSize( MIN_SIZE_BT );
    	component.setPreferredSize( PREF_SIZE );
    	component.setMaximumSize( MAX_HEIGHT );
    }

    /**
     * This method initializes CComboBox1
     * 
     * @return javax.swing.CComboBox
     */
    private ActionListener AL_cbbPayPeriod = new java.awt.event.ActionListener()
    {
        public void actionPerformed(java.awt.event.ActionEvent e)
        {
            if (currentEmployeeID != -1) m_main.reloadInfo();
        }
    };
    
    private CComboBox m_cbbPayPeriod = null;
    private CComboBox initCbbPayPeriod() {
        if (m_cbbPayPeriod == null) {
        	m_cbbPayPeriod = new CComboBox();
        	setupSize(m_cbbPayPeriod);
        	m_cbbPayPeriod.addActionListener( AL_cbbPayPeriod );
        	m_cbbPayPeriod.setActionCommand("payPeriod");
        }
        return m_cbbPayPeriod;
    }


    private CComboBox m_cbbOrg = null;
    private CComboBox initCbbOrg() {

        if (m_cbbOrg == null) {
        	m_cbbOrg = new CComboBox();
        	setupSize(m_cbbOrg);
            m_cbbOrg.setEditable(false);
        }
        return m_cbbOrg;
    }

    private CComboBox m_cbbActivity = null;
    private CComboBox initCbbActivity() {

        if (m_cbbActivity == null) {
        	m_cbbActivity = new CComboBox();
        	setupSize(m_cbbActivity);
            m_cbbActivity.setEditable(false);
        }
        return m_cbbActivity;
    }

    private CComboBox m_cbbTaxation_Region = null;
    private CComboBox initCbbTaxation_Region() {

        if (m_cbbTaxation_Region == null) {
        	m_cbbTaxation_Region = new CComboBox();
        	setupSize(m_cbbTaxation_Region);
        	//ici
            m_cbbTaxation_Region.setEditable(true);
            m_cbbTaxation_Region.addItemListener( new ItemListener() {
                public void itemStateChanged(ItemEvent ev) {
                    if( ev.getStateChange() == ItemEvent.SELECTED ) {
                        m_main.enableSave( true );
                    }
                }
            } );
        }
        return m_cbbTaxation_Region;
    }

    
    /**
     * This method initializes CComboBox1
     * 
     * @return javax.swing.CComboBox
     */
    private CComboBox m_cbbPaymentType = null;
    private CComboBox initCbbPaymentType() {
        if (m_cbbPaymentType == null) {
            m_cbbPaymentType = new CComboBox();
        	setupSize(m_cbbPaymentType);
        	m_cbbPaymentType.setEditable(false);
            m_cbbPaymentType.setMaximumSize( MAX_HEIGHT );
            m_cbbPaymentType.addItemListener( new ItemListener() {
                public void itemStateChanged(ItemEvent ev) {
                    if( ev.getStateChange() == ItemEvent.SELECTED ) {
                        m_main.enableSave( true );
                    }
                }
            } );
        }
        return m_cbbPaymentType;
    }

    
    private CComboBox m_cbbEmployer = null;
    private CComboBox initCbbEmployer() {
        if (m_cbbEmployer == null) {
            m_cbbEmployer = new CComboBox();
        	setupSize(m_cbbEmployer);
        	m_cbbEmployer.setEditable(false);
        	m_cbbEmployer.setEnabled(false);
            m_cbbEmployer.setMaximumSize( MAX_HEIGHT );
            m_cbbEmployer.addItemListener( new ItemListener() {
                public void itemStateChanged(ItemEvent ev) {
                    if( ev.getStateChange() == ItemEvent.SELECTED ) {
                        m_main.enableSave( true );
                    }
                }
            } );
        }
        return m_cbbEmployer;
    }

    
    /**
     * This method initializes CTextField2
     * 
     * @return javax.swing.CTextField
     */
/*    private CTextField m_txtSheetNumber = null;
    private CTextField initTxtSheetNumber() {
        if (m_txtSheetNumber == null) {
        	m_txtSheetNumber = new CTextField();
        	setupSize(m_txtSheetNumber);
            m_txtSheetNumber.setEditable(false);
        }
        return m_txtSheetNumber;
    }
*/
    /**
     * This method initializes CTextField3
     * 
     * @return javax.swing.CTextField
     */
    private boolean m_feuilleAjustement = false;
    public boolean isFeuilleAjustement() { return m_feuilleAjustement; }
    private CComboBox m_cbbSheetType = null;
    private CComboBox initCbbSheetType() {
        if (m_cbbSheetType == null) {
            m_cbbSheetType = new CComboBox();
            setupSize(m_cbbSheetType);
            m_cbbSheetType.setEditable(false);
            m_cbbSheetType.addItemListener( new ItemListener() {
                public void itemStateChanged(ItemEvent ev) {
                    if( ev.getStateChange() == ItemEvent.SELECTED ) {
                        ValueNamePair vnp = (ValueNamePair)ev.getItem();
                        m_feuilleAjustement = vnp.getValue().equals("Adjustement");
                        if ( m_feuilleAjustement )
                        {
                	    	// Type Depot par défaut pour un ajustement.
                	    	m_cbbPaymentType.setSelectedIndex(1);
                	    	m_cbbPaymentType.setEnabled(false);
                        }
//                        else m_cbbPaymentType.setEnabled(true);
					    else
					    {
                        	m_cbbPaymentType.setEnabled(true);
                			//2009.05.13 -- magnetho 741 - Si l'on créer un ajustement, la seconde feuille de temps était automatique de type depot meme si l'employé est payé par cheque
                        	if ( m_timeSheet != null && m_timeSheet.getP_Employee_ID() != 0)
                        	{
                            	P_Employee employee = P_Employee.get(Env.getCtx(), m_timeSheet.getP_Employee_ID(), null );
                    			if ( employee.getPaymentType().equals( P_Time_Sheet.PAYMENTTYPE_Cheque) )
                        	    	m_cbbPaymentType.setSelectedIndex(0); 
                    			else
                        	    	m_cbbPaymentType.setSelectedIndex(1);

                        	}
					    }
					    				                       
                        if ( vnp.getValue().equals("Advance"))
                	    	// Type Cheque par défaut pour un avance.
                	    	m_cbbPaymentType.setSelectedIndex(0);
                        	
                        if( m_detail != null ) m_detail.resetEditable();
                        m_main.enableSave( true );
                    }
                }
            } );
        }
        return m_cbbSheetType;
    }

    /**
     * This method initializes CTextField4
     * 
     * @return javax.swing.CTextField
     */
    private CTextField m_txtSheetStatus = null;
    private CTextField initTxtSheetStatus() {
        if (m_txtSheetStatus == null) {
        	m_txtSheetStatus = new CTextField();
        	setupSize(m_txtSheetStatus);
            m_txtSheetStatus.setEditable(false);
        }
        return m_txtSheetStatus;
    }

    /**
     * This method initializes CTextField
     * 
     * @return javax.swing.CTextField
     */
    private CTextField m_txtJobTitle = null;
    private CTextField initTxtJobTitle() {
        if (m_txtJobTitle == null) {
        	m_txtJobTitle = new CTextField();
        	setupSize(m_txtJobTitle);
            m_txtJobTitle.setEditable(false);
            m_txtJobTitle.setHorizontalAlignment(CTextField.LEFT);

        }
        return m_txtJobTitle;
    }
    
    /**
     * This method initializes CTextField
     * 
     * @return javax.swing.CTextField
     */
    private CTextField m_txtJobType = null;
    private CTextField initTxtJobType() {
        if (m_txtJobType == null) {
        	m_txtJobType = new CTextField();
        	setupSize(m_txtJobType);
            m_txtJobType.setEditable(false);
        }
        return m_txtJobType;
    }

    /**
     * This method initializes CTextField
     * 
     * @return javax.swing.CTextField
     */
    private CTextField m_txtBooklet = null;
    private CTextField initTxtBooklet()
    {
        if (m_txtBooklet == null)
        {
        	m_txtBooklet = new CTextField();
        	setupSize(m_txtBooklet);
            m_txtBooklet.setEditable(false);
        }
        return m_txtBooklet;
    }
    
    private CTextField m_txtConvention = null;
    private CTextField initTxtConvention()
    {
        if (m_txtConvention == null)
        {
            m_txtConvention = new CTextField();
        	setupSize(m_txtConvention);
        	m_txtConvention.setEditable(false);
        }
        return m_txtConvention;
    }
    
    /**
     * This method initializes jButton1
     * 
     * @return javax.swing.JButton
     */
    private CButton m_btnInitial = null;
    private CButton initBtnInitial() {
        if (m_btnInitial == null) {
            m_btnInitial = new CButton();
            m_btnInitial.setBorder(BorderFactory.createEtchedBorder());
            setupSize2(m_btnInitial);
            m_btnInitial.setText("Initial");
            m_btnInitial.addActionListener(this);
            m_btnInitial.setActionCommand("Initial");
        }
        return m_btnInitial;
    }

    /**
     * This method initializes jButton
     * 
     * @return javax.swing.JButton
     */
    private CButton m_btnProcess = null;
    private CButton initBtnProcess() {
        if (m_btnProcess == null) {
        	m_btnProcess = new CButton();
            m_btnProcess.setBorder(BorderFactory.createEtchedBorder());
        	setupSize2(m_btnProcess);
        	m_btnProcess.setActionCommand("Process");
        	m_btnProcess.addActionListener(this);
        }
        return m_btnProcess;
    }

    private CButton m_btnMoreInfo = null;

    private CButton m_btnGenBonus = null;

    private CButton initBtnMoreInfo() {
        if (m_btnMoreInfo == null) {
        	m_btnMoreInfo = new CButton();
            m_btnMoreInfo.setBorder(BorderFactory.createEtchedBorder());
        	setupSize2(m_btnMoreInfo);
        	m_btnMoreInfo.setActionCommand("MoreInfo");
        	m_btnMoreInfo.addActionListener(this);
        }
        return m_btnMoreInfo;
    }

    private CButton initBtnGenBonus() {
        if (m_btnGenBonus == null) {
        	m_btnGenBonus = new CButton();
        	m_btnGenBonus.setBorder(BorderFactory.createEtchedBorder());
        	setupSize2(m_btnGenBonus);
        	m_btnGenBonus.setActionCommand("GenBonus");
        	m_btnGenBonus.addActionListener(this);
        }
        return m_btnGenBonus;
    }

    
    /**
     * This method initializes CTextField
     * 
     * @return javax.swing.CTextField
     */
    private CTextField m_txtGrossEarnings = null;
    private CTextField initTxtGrossEarnings() {
        if (m_txtGrossEarnings == null) {
            m_txtGrossEarnings = new CTextField();
            setupSize(m_txtGrossEarnings);
            m_txtGrossEarnings.setEditable(false);
            m_txtGrossEarnings.setHorizontalAlignment(CTextField.RIGHT);
        }
        return m_txtGrossEarnings;
    }

    /**
     * This method initializes CTextField
     * 
     * @return javax.swing.CTextField
     */
    private CTextField m_txtNetPay = null;
    private CTextField initTxtNetPay() {
        if (m_txtNetPay == null) {
        	m_txtNetPay = new CTextField();
        	setupSize(m_txtNetPay);
            m_txtNetPay.setEditable(false);
            m_txtNetPay.setHorizontalAlignment(CTextField.RIGHT);
        }
        return m_txtNetPay;
    }
    
    /**
     * This method initializes jButton
     * 
     * @return javax.swing.JButton
     */
    private CButton m_btnBefore = null;
    private CButton initBtnBefore() {
        if (m_btnBefore == null) {
            m_btnBefore = new CButton();
            m_btnBefore.setBorder(BorderFactory.createEtchedBorder());
            setupSize2(m_btnBefore);
            m_btnBefore.setText("<");
            m_btnBefore.setFont( m_btnBefore.getFont().deriveFont(Font.BOLD) );
            m_btnBefore.setVisible(false);
            m_btnBefore.setActionCommand("Before");
            m_btnBefore.addActionListener(this);
        }
        return m_btnBefore;
    }

    /**
     * This method initializes jButton
     * 
     * @return javax.swing.JButton
     */
    private CButton m_btnNext = null;
    private CButton initBtnNext() {
        if (m_btnNext == null) {
        	m_btnNext = new CButton();
            m_btnNext.setBorder(BorderFactory.createEtchedBorder());
        	setupSize2(m_btnNext);
        	m_btnNext.setText(">");
        	m_btnNext.setFont( m_btnNext.getFont().deriveFont(Font.BOLD) );
        	m_btnNext.setVisible(false);
        	m_btnNext.setActionCommand("Next");
        	m_btnNext.addActionListener(this);
        }
        return m_btnNext;
    }

    /**
     * This method initializes CTextField
     * 
     * @return javax.swing.CTextField
     */
    private CTextField m_txtPeriodStart = null;
    private CTextField initTxtPeriodStart() {
        if (m_txtPeriodStart == null) {
        	m_txtPeriodStart = new CTextField();
        	setupSize(m_txtPeriodStart);
            m_txtPeriodStart.setEditable(false);
        }
        return m_txtPeriodStart;
    }

    /**
     * This method initializes CTextField
     * 
     * @return javax.swing.CTextField
     */
  /*  private CTextField m_txtPeriodEnd = null;
   private CTextField initTxtPeriodEnd() {
        if (m_txtPeriodEnd == null) {
        	m_txtPeriodEnd = new CTextField();
        	setupSize(m_txtPeriodEnd);
            m_txtPeriodEnd.setEditable(false);
        }
        return m_txtPeriodEnd;
    }
*/
    
    private CTextField m_txtPeriodPay = null;
    private CTextField initTxtPeriodPay() {
        if (m_txtPeriodPay == null) {
        	m_txtPeriodPay = new CTextField();
        	setupSize(m_txtPeriodPay);
            m_txtPeriodPay.setEditable(false);
            m_txtPeriodPay.addFocusListener( new java.awt.event.FocusListener() 
                  {
            		public void focusGained(java.awt.event.FocusEvent e)
            		{
//            			m_main.enableSave( true );
            		}
                    public void focusLost(java.awt.event.FocusEvent e)
                        {
                        	m_main.enableSave( true );
                        }
                    });
        }
        return m_txtPeriodPay;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {
//    	System.out.println("ActionPerformed");
        //String value = e.getSource().toString();
        if (e.getActionCommand().equals("Process"))
        { //this the Process
//        	System.out.println("Process");
            if( m_main.isReadOnly() ) return;
            // button, based on the
            // button text we should
            // perform the appropriate
            // aciton
            if (m_timeSheet.get_ID() == 0)
            {
                //Generate Time Sheet
                GenerateTimeSheet();
            }
            else if (m_timeSheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Initial) 
           		 && m_timeSheet.getSheetType().equals(P_Time_Sheet.SHEETTYPE_Regular)
           		 && m_timeSheet.getNumberOfGain() == 0 )
            {
                //Generate Time Sheet
                GenerateTimeSheet();
            }

            else if (m_timeSheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Initial))
            {

                //Validate Time Sheet
                ValidateTimeSheet();
            }
            else if (m_timeSheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Validated) || 
            		m_timeSheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Calculated))
            {
//            	String trxName = null;

//            	m_timeSheet = P_Time_Sheet.SetTimeSheetInitial( m_timeSheet.getP_Time_Sheet_ID(), trxName );
                ValidateTimeSheet();

                //Compute Time Sheet
//                ComputeTimeSheet();

            }
            else if (m_timeSheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Issued) || 
            		m_timeSheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Transferred))
            {
        		int response = JOptionPane.showConfirmDialog(new Frame(""),
        				Msg.translate(Env.getCtx(), "CancelTimeSheet?" ),
        				Msg.translate(Env.getCtx(), "Cancellation?" ),
    					JOptionPane.OK_CANCEL_OPTION,
    					JOptionPane.WARNING_MESSAGE);
                if(response == JOptionPane.YES_OPTION)
                {
                    CancelPayment();
                    m_main.reloadInfo();
                }

            	
            }
        }
        else if (e.getActionCommand() == "Initial")
        {
            if( m_main.isReadOnly() ) return;
    		int AD_Session_ID = Env.getContextAsInt(Env.getCtx(), "#AD_Session_ID");
            CacheMgt.get().reset();
			MSession session = new MSession(Env.getCtx(), AD_Session_ID, null );		//	Start Session
			Env.setContext(Env.getCtx(), "#AD_Session_ID", session.getAD_Session_ID());
        	String trxName = Trx.createTrxName("init_ts");
        	Trx trx = Trx.get(trxName, true);

        	try {
        		trx.start();
        		m_timeSheet = P_Time_Sheet.SetTimeSheetInitial( m_timeSheet.getP_Time_Sheet_ID(), trxName );
        		trx.commit();
        	}
        	catch( Exception ex ) {
        		trx.rollback();
        		s_log.log( Level.SEVERE, "init_ts failed", ex );
        	}
        	finally {
        		trx.close();
//        		CacheMgt.get().reset();
        	}

            m_main.reloadInfo();
        }
        else if (e.getActionCommand() == "Delete")
        {
            if( m_main.isReadOnly() ) return;
            delete();
            CacheMgt.get().reset();
            m_main.reloadInfo();
        }
        else if (e.getActionCommand() == "Next")
        {
            currentTimeSheetIndex = currentTimeSheetIndex + 1;
            enableBtnNext( currentTimeSheetIndex < (timeSheetList.size() - 1) );
            enableBtnBefore( currentTimeSheetIndex >= 1 );
            
            m_main.reloadInfo();
        }
        else if (e.getActionCommand() == "Before")
        {
            currentTimeSheetIndex = currentTimeSheetIndex - 1;

            //Modification pour gerer correctement l'etat des boutons de navigation
            enableBtnBefore(currentTimeSheetIndex != 0);
            enableBtnNext(timeSheetList.size() - 1 != 0);
            
            m_main.reloadInfo();
        }
        else if (e.getActionCommand() == "GB")
        {
            if( m_main.isReadOnly() ) return;
            //Le paiement pour lequelle on génère l'écriture comptable.
//            int paymentID = m_timeSheet.getP_Payment_ID();
            Generate_Ledger_Entry();
        }
        else if(e.getActionCommand().equals("open_employee")) {
			MQuery query = new MQuery("P_Employee");
			query.addRestriction("P_Employee_ID", MQuery.EQUAL, currentEmployeeID );
			
			AWindow frame = new AWindow();
			if (!frame.initWindow(m_main.P_EMPLOYEE_AD_Window_ID, query)) return;
			AEnv.showCenterScreen(frame);
        }

        if (e.getActionCommand().equals("GenBonus"))
        {
        	if (m_timeSheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Initial) 
              		 && m_timeSheet.getSheetType().equals(P_Time_Sheet.SHEETTYPE_Complementary)
//              		 && m_timeSheet.getNumberOfGain() == 0
              		 )
               {
                   GenerateBonus();
               }
        	 m_main.reloadInfo();
        }

        
        if (e.getActionCommand().equals("MoreInfo"))
        {
        	if ( this.currentTimeID <= 0 )
        		return;
        	
			MQuery query = new MQuery("P_Time_Sheet");
			query.addRestriction("P_Time_Sheet_ID", MQuery.EQUAL, this.currentTimeID );
			
//			AWindow frame = new AWindow();
			VTimeSheetMoreInfo Dialog = new VTimeSheetMoreInfo( Env.getFrame( this ), Msg.translate(Env.getCtx(),"More Info" ), true, m_timeSheet );
	
			Dialog.setBounds(20, 200, 1000, 300);
			Dialog.setVisible( true );
			m_main.reloadInfo();
//			if (!frame.initWindow(m_main.P_TIME_SHEET_AD_Window_ID, query)) return;
//			frame.setModalExclusionType( Dialog.ModalExclusionType.NO_EXCLUDE);
//			frame.setAlwaysOnTop(true);
//			AEnv.showCenterScreen(frame);
        }
        //Refresh the screen
//        _main.reloadInfo();

    }
    
    
    private void GenerateBonus()
    {


        ProcessInfo pi = new ProcessInfo("P_TimeGenerationBonusOnly", 2100252); // HARDCODED
		MProcess process = MProcess.get(Env.getCtx(), pi.getAD_Process_ID());

        MPInstance pInstance = new MPInstance(process, -1);
		pi.setAD_PInstance_ID(pInstance.getAD_PInstance_ID());

        // P_TimeGeneration
        if (pi.getAD_PInstance_ID() == 0)
        {
            s_log.severe(Msg.getMsg(Env.getCtx(), "ProcessNoInstance"));
            return;
        }

        //		Create a new Time Sheet
        int frequencyID = -1;
        int payPeriodID = -1;
        int employeeID = -1;

        ValueNamePair pp;

        pp = (ValueNamePair) m_cbbPayPeriod.getSelectedItem();
        payPeriodID = Integer.parseInt(pp.getValue());

        employeeID = currentEmployeeID;

        String sql = new String("");

        if (m_timeSheet.get_ID() != 0)
        {
            sql = "INSERT INTO AD_PInstance_Para (AD_PInstance_ID,SeqNo,ParameterName, P_NUMBER) " + "VALUES (" + pi.getAD_PInstance_ID() + ",6,'P_Time_Sheet_ID'," + this.m_timeSheet.get_ID() + ")";
            DB.executeUpdate(sql, null);
        }


        //
        // Sécurité pour évité que les feuilles de temps soit générer pour tout les employées.
        //
        if ( employeeID <= 0 )
        	return;
        
        ProcessCtl worker = new ProcessCtl(this, pi, null); 
        worker.start(); //  complete tasks in unlockUI /
    }

    
    //Génerer une feuille de temps pour l'employé courant et la période
    // courante
    private void GenerateTimeSheet()
    {

//		Trx trx = null; //Trx.get( Trx.createTrxName(), true);

        ProcessInfo pi = new ProcessInfo("P_TimeGeneration", 2000010); // HARDCODED
		MProcess process = MProcess.get(Env.getCtx(), pi.getAD_Process_ID());

        MPInstance pInstance = new MPInstance(process, -1);
		pi.setAD_PInstance_ID(pInstance.getAD_PInstance_ID());

        // P_TimeGeneration
//        pi.setAD_PInstance_ID(ProcessCtl.getInstanceID(m_WindowNo, pi.getAD_Process_ID(), pi.getRecord_ID()));
        if (pi.getAD_PInstance_ID() == 0)
        {
            s_log.severe(Msg.getMsg(Env.getCtx(), "ProcessNoInstance"));
            return;
        }

        //		Create a new Time Sheet
        int frequencyID = -1;
        int payPeriodID = -1;
  //      int PaymentGroupID = -1;
        int employeeID = -1;

        ValueNamePair pp;/* = (ValueNamePair) m_cbbFrequency.getSelectedItem();
        frequencyID = Integer.parseInt(pp.getValue());
*/

        pp = (ValueNamePair) m_cbbPayPeriod.getSelectedItem();
        payPeriodID = Integer.parseInt(pp.getValue());

        employeeID = currentEmployeeID;

        String sql = new String("");

        sql = "INSERT INTO AD_PInstance_Para (AD_PInstance_ID,SeqNo,ParameterName, P_NUMBER) " + "VALUES (" + pi.getAD_PInstance_ID() + ",2,'P_Employee_ID'," + employeeID + ")";
        DB.executeUpdate(sql, null);

        sql = "INSERT INTO AD_PInstance_Para (AD_PInstance_ID,SeqNo,ParameterName, P_NUMBER) " + "VALUES (" + pi.getAD_PInstance_ID() + ",4,'P_Period_ID'," + payPeriodID + ")";
        DB.executeUpdate(sql, null);

        sql = "INSERT INTO AD_PInstance_Para (AD_PInstance_ID,SeqNo,ParameterName, P_NUMBER) " + "VALUES (" + pi.getAD_PInstance_ID() + ",5,'P_Frequency_ID'," + frequencyID + ")";
        DB.executeUpdate(sql, null);

        if (m_timeSheet.get_ID() != 0)
        {
            sql = "INSERT INTO AD_PInstance_Para (AD_PInstance_ID,SeqNo,ParameterName, P_NUMBER) " + "VALUES (" + pi.getAD_PInstance_ID() + ",6,'P_Time_Sheet_ID'," + this.m_timeSheet.get_ID() + ")";
            DB.executeUpdate(sql, null);
        }
        	

        //
        // Sécurité pour évité que les feuilles de temps soit générer pour tout les employées.
        //
        if ( employeeID <= 0 )
        	return;
        
        ProcessCtl worker = new ProcessCtl(this, pi, null); 
        worker.start(); //  complete tasks in unlockUI /
    }

    private void Generate_Ledger_Entry()
    {
        ProcessInfo pi = new ProcessInfo("P_Generate_Ledger_Entry", 2000054); // HARDCODED
		MProcess process = MProcess.get(Env.getCtx(), pi.getAD_Process_ID());
        MPInstance pInstance = new MPInstance(process, -1);
		pi.setAD_PInstance_ID(pInstance.getAD_PInstance_ID());

        // P_TimeGeneration
//        pi.setAD_PInstance_ID(ProcessCtl.getInstanceID(m_WindowNo, pi.getAD_Process_ID(), TimeSheet.getP_Payment_ID()));

        if (pi.getAD_PInstance_ID() == 0)
        {
            s_log.severe(Msg.getMsg(Env.getCtx(), "ProcessNoInstance"));
            return;
        }

        String sql = new String("");
        sql = "INSERT INTO AD_PInstance_Para (AD_PInstance_ID,SeqNo,ParameterName, P_NUMBER) " + "VALUES (" + pi.getAD_PInstance_ID() + ",1,'P_Payment_ID'," + m_timeSheet.getP_Payment_ID() + ")";
        DB.executeUpdate(sql, null);

        //	Execute Process
		Trx trx = null; // Trx.get( Trx.createTrxName(), true);

        ProcessCtl worker = new ProcessCtl(this, pi,trx);
        worker.start();

    }

    //Cancel Payment Process
    private void CancelPayment()
    {
//    	System.out.println("CancelPayment");
        P_Payment_Cancel paymentCancel = new P_Payment_Cancel(m_timeSheet);
        String adMessage = paymentCancel.execute();
        if(adMessage != null)
            ADialog.error(0, m_main, adMessage);
    }

    //Valider la feuille de temps courante
    private void ValidateTimeSheet() 
    {
        if( m_main.cmd_save(false) == false ) return;
        
        ProcessInfo pi = new ProcessInfo("P_TimeValidation", 2000011); // HARDCODED
		MProcess process = MProcess.get(Env.getCtx(), pi.getAD_Process_ID());
        MPInstance pInstance = new MPInstance(process, -1);
		pi.setAD_PInstance_ID(pInstance.getAD_PInstance_ID());

        if (pi.getAD_PInstance_ID() == 0)
        {
            s_log.severe(Msg.getMsg(Env.getCtx(), "ProcessNoInstance"));
            return;
        }

        //		Create a new Time Sheet
        int payPeriodID = -1;
 //       int PaymentGroupID = -1;
        int employeeID = -1;

        ValueNamePair pp;
        
        pp = (ValueNamePair) m_cbbPayPeriod.getSelectedItem();
        payPeriodID = Integer.parseInt(pp.getValue());
        employeeID = currentEmployeeID;
        String sql = new String("");

        sql = "INSERT INTO AD_PInstance_Para (AD_PInstance_ID,SeqNo,ParameterName, P_NUMBER) " + "VALUES (" + pi.getAD_PInstance_ID() + ",1,'P_Time_Sheet_ID'," + m_timeSheet.get_ID() + ")";
        DB.executeUpdate(sql, null);

        sql = "INSERT INTO AD_PInstance_Para (AD_PInstance_ID,SeqNo,ParameterName, P_NUMBER) " + "VALUES (" + pi.getAD_PInstance_ID() + ",2,'P_Employee_ID'," + employeeID + ")";
        DB.executeUpdate(sql, null);

        sql = "INSERT INTO AD_PInstance_Para (AD_PInstance_ID,SeqNo,ParameterName, P_NUMBER) " + "VALUES (" + pi.getAD_PInstance_ID() + ",4,'P_Period_ID'," + payPeriodID + ")";
        DB.executeUpdate(sql, null);

        sql = "INSERT INTO AD_PInstance_Para (AD_PInstance_ID,SeqNo,ParameterName, P_String ) " + 
        	  "VALUES (" + pi.getAD_PInstance_ID() + ",5,'Rework', 'Y')";
        DB.executeUpdate(sql, null);
        
        ProcessCtl worker = new ProcessCtl(this, pi, null); 
        worker.start(); //  complete tasks in unlockUI /
    }

/*    
    //Calculer la feuille de temps.
    private void ComputeTimeSheet()
    {
        ProcessInfo pi = new ProcessInfo("P_Calcul_Net", 2000007); // HARDCODED
		MProcess process = MProcess.get(Env.getCtx(), pi.getAD_Process_ID());
        MPInstance pInstance = new MPInstance(process, -1);
		pi.setAD_PInstance_ID(pInstance.getAD_PInstance_ID());

        if (pi.getAD_PInstance_ID() == 0)
        {
            s_log.severe(Msg.getMsg(Env.getCtx(), "ProcessNoInstance"));
            return;
        }

        //		Create a new Time Sheet
        int frequencyID = -1;
        int payPeriodID = -1;
 //       int PaymentGroupID = -1;
        int employeeID = -1;

        ValueNamePair pp;
        
        pp = (ValueNamePair) m_cbbPayPeriod.getSelectedItem();
        payPeriodID = Integer.parseInt(pp.getValue());
        employeeID = currentEmployeeID;

        String sql = new String("");
        sql = "INSERT INTO AD_PInstance_Para (AD_PInstance_ID,SeqNo,ParameterName, P_NUMBER) " + "VALUES (" + pi.getAD_PInstance_ID() + ",1,'P_Payment_ID'," + m_timeSheet.getP_Payment_ID() + ")";
        DB.executeUpdate(sql, null);

        sql = "INSERT INTO AD_PInstance_Para (AD_PInstance_ID,SeqNo,ParameterName, P_NUMBER) " + "VALUES (" + pi.getAD_PInstance_ID() + ",2,'P_Employee_ID'," + employeeID + ")";
        DB.executeUpdate(sql, null);

        sql = "INSERT INTO AD_PInstance_Para (AD_PInstance_ID,SeqNo,ParameterName, P_NUMBER) " + "VALUES (" + pi.getAD_PInstance_ID() + ",4,'P_Period_ID'," + payPeriodID + ")";
        DB.executeUpdate(sql, null);

        sql = "INSERT INTO AD_PInstance_Para (AD_PInstance_ID,SeqNo,ParameterName, P_NUMBER) " + "VALUES (" + pi.getAD_PInstance_ID() + ",5,'P_Frequency_ID'," + frequencyID + ")";
        DB.executeUpdate(sql, null);

        sql = "INSERT INTO AD_PInstance_Para (AD_PInstance_ID,SeqNo,ParameterName, P_STRING) " + "VALUES (" + pi.getAD_PInstance_ID() + ",6,'Rework'," + "'Y'" + ")";
        DB.executeUpdate(sql, null);

        ProcessCtl worker = new ProcessCtl(this, pi, null); 
        worker.start(); //  complete tasks in unlockUI /
    }
*/
    /**
     * Set the employee information Any calls to this method refresh the screen
     */
    public void refreshEmployeeInformation(int employeeID, Timestamp EffectIn)
    {
   	
        String employeeQuery = "Select P_Employee.Value,P_Employee.Name,P_Employee.PaymentType, P_Payment_Group.Name From P_Employee, P_Payment_Group Where P_Employee.P_Payment_Group_ID = P_Payment_Group.P_Payment_Group_ID and P_Employee_ID = ?";

        //Initialise the employee timesheet counter to zero
        //I guess this function is only called when a new employee is selected
        // otherwise we call houston.

        if (currentEmployeeID != employeeID || currentPeriodID != getSelectedPeriodID())
        {
            //Reset every Thing
            currentTimeSheetIndex = -1;
            enableBtnNext(false);
            enableBtnBefore(false);

        }

        currentEmployeeID = employeeID;

        
        //Get the employee Details
        try
        {
            PreparedStatement pstmt = DB.prepareStatement(employeeQuery, null);
            pstmt.setInt(1, employeeID);
            ResultSet rs = pstmt.executeQuery();

//            int index = 0;
            if (rs.next())
            {
                //Set the id number
            	m_borderTitre.setTitle(rs.getString(1) + " - " + rs.getString(2));
            }
            else
            {
            	m_borderTitre.setTitle(Msg.translate(Env.getCtx(), "TimeSheet"));
            }

            this.repaint();
            
            rs.close();
            pstmt.close();

        }
        catch (Exception e)
        {
            s_log.log(Level.SEVERE,"VSheetHeaderPanel.refreshEmployeeInformation", e);
        }

        if ( m_detail != null)
        	m_detail.getLookup().setEmployeeId(employeeID, EffectIn);

        /** Load time sheet info */
        loadTimeSheetInformation(employeeID);

    }
    
    /**
     * Load the pay Period
     *  
     */
    public void loadPayPeriod( int P_Calendar_ID )
    {

//        P_Period period = P_Period.getOpenPeriodWithCalendar(Env.getCtx(), P_Calendar_ID, null);
        String sql = "SELECT P_Period.P_Period_ID, P_Frequency.PayPeriodType + '-' + P_Period.Name as Name, P_Period.IsDefault, P_Period.Name as PeriodName  " +
        		"FROM P_Period  " +
                " inner join P_YEAR on P_YEAR.P_YEAR_ID = P_PERIOD.P_YEAR_ID " +
                " inner join P_Frequency on P_Frequency.P_Frequency_ID = P_PERIOD.P_Frequency_ID " +
				 "WHERE  P_Period.IsActive = 'Y' " ;
        
        if ( P_Calendar_ID != 0 )
               sql += 			 " AND   P_YEAR.P_CALENDAR_ID = "  + P_Calendar_ID ;

        /*
        if ( getCurrentEmployeeID() > 0 )
        {
	        sql += " UNION " 
	             + " SELECT P_Period.P_Period_ID, P_Frequency.PayPeriodType + '-' + P_Period.Name as Name, P_Period.IsDefault, P_Period.Name as PeriodName " 
	             + " FROM P_Period  "
	             + " inner join P_YEAR on P_YEAR.P_YEAR_ID = P_PERIOD.P_YEAR_ID "
	             + " inner join P_Frequency on P_Frequency.P_Frequency_ID = P_PERIOD.P_Frequency_ID " 
	             + " inner join P_Payment on P_Payment.P_PERIOD_ID = P_Period.P_PERIOD_ID and P_Payment.P_EMPLOYEE_ID = " + getCurrentEmployeeID()
	             ; 
        }
*/        
        sql += 	 "ORDER BY PeriodName DESC";
        int selectedIndex = 0;
        try
        {
            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            ResultSet rs = pstmt.executeQuery();
            int index = 0;

            try
            {
            	if ( m_cbbPayPeriod != null && m_cbbPayPeriod.getItemCount() != 0)
            	{
            		m_cbbPayPeriod.removeActionListener( AL_cbbPayPeriod );
            		m_cbbPayPeriod.setSelectedIndex(-1);

            		m_cbbPayPeriod.removeAllItems();
            	}
            }
            catch (Exception e)
            {
                s_log.log(Level.SEVERE,"VEmployeeListPanel.loadPayPeriod", e);
            }
            	

            ValueNamePair vp = null;
            while (rs.next())
            {
                vp = new ValueNamePair(rs.getString(1), rs.getString(2)); //  returns
                // also
                // not
                // active
                m_cbbPayPeriod.addItem(vp);
                if (rs.getString(3).compareTo("Y") == 0)
                {
                    selectedIndex = index;
                }
                index++;
            }

            m_cbbPayPeriod.addActionListener( AL_cbbPayPeriod );

            if ( index != 0)
            {
                m_defaultPeriodIndex = selectedIndex;
                m_cbbPayPeriod.setSelectedIndex(selectedIndex);
            }
        }
        catch (Exception e)
        {
            s_log.log(Level.SEVERE,"VEmployeeListPanel.loadPayPeriod", e);
        }
        

    }

    /**
     * Load the Organisation
     *  
     */
    private void loadOrg()
    {
       
    	if ( m_cbbOrg == null)
    		return;
    	
        String sql = "SELECT AD_Org_ID, Name FROM AD_Org WHERE IsActive = 'Y' AND AD_ORG_ID <> 0 ";
	    
        sql = sql + " And AD_ORG." + MRole.getDefault().getClientWhere(false) + " ORDER BY Value";

        
        int selectedIndex = 0;
        try
        {
            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            ResultSet rs = pstmt.executeQuery();

            m_cbbOrg.removeAllItems();

            ValueNamePair vp = null;
            while (rs.next())
            {
                vp = new ValueNamePair(rs.getString(1), rs.getString(2)); //  returns
                m_cbbOrg.addItem(vp);
            }
            m_cbbOrg.setSelectedIndex(0);
        }
        catch (Exception e)
        {
            s_log.log(Level.SEVERE,"VEmployeeListPanel.loadOrg", e);
        }
    }

    /**
     * Load Region
     *  
     */
    private void loadTaxation_Region()
    {
       
    	if ( m_cbbTaxation_Region == null)
    		return;
    	
        String sql = "SELECT C_Region_ID, Description FROM C_Region WHERE IsActive = 'Y' AND C_Country_ID = 109 ";
	    
//        sql = sql + " And AD_ORG." + MRole.getDefault().getClientWhere(false) + " ORDER BY Value";

        
        int selectedIndex = 0;
        try
        {
            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            ResultSet rs = pstmt.executeQuery();

            m_cbbTaxation_Region.removeAllItems();

            ValueNamePair vp = null;
            while (rs.next())
            {
                vp = new ValueNamePair(rs.getString(1), rs.getString(2)); //  returns
                m_cbbTaxation_Region.addItem(vp);
            }
            m_cbbTaxation_Region.setSelectedIndex(0);
        }
        catch (Exception e)
        {
            s_log.log(Level.SEVERE,"VEmployeeListPanel.loadTaxation_Region", e);
        }
    }
    
   public int getPayCalendar()
    {
/*	   if ( mP_Calendar_ID == 0)
	   {
	        P_Employee Employee = P_Employee.get( Env.getCtx(), getCurrentEmployeeID(), null);
	        P_Payment_Group PaymentGroup = P_Payment_Group.get( Env.getCtx(), Employee.getP_Payment_Group_ID(), null); 
	        mP_Calendar_ID = PaymentGroup.getP_Calendar_ID(); 
	   }
	   if ( mP_Calendar_ID == 0)
	   {
		   return 1000008;
	   }
*/
	    
	    if ( mP_Calendar_ID == 0)
	    	mP_Calendar_ID = Env.getContextAsInt(Env.getCtx(), "#P_Calendar_ID");
	    
    	return mP_Calendar_ID;
    }

   public void setPayCalendar( int P_Calendar_ID )
   {
	   mP_Calendar_ID = P_Calendar_ID;
   }

    public void loadPayCalendar( int P_Calendar_ID, int Employee_ID )
    {
    	mP_Calendar_ID = P_Calendar_ID;
    	currentEmployeeID = Employee_ID;
        loadPayPeriod( P_Calendar_ID);
    }


    /**
     * Load the Organisation
     *  
     */
    private void loadActivity()
    {
       
    	if ( m_cbbActivity == null)
    		return;
    	
        String sql = "SELECT C_Activity_ID, Value + ' - ' + Name FROM C_Activity WHERE IsActive = 'Y' ";
	    
        sql = sql + " And C_Activity." + MRole.getDefault().getClientWhere(false) + " ORDER BY Value";

        
        int selectedIndex = 0;
        try
        {
            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            ResultSet rs = pstmt.executeQuery();

            m_cbbActivity.removeAllItems();

            ValueNamePair vp = null;
            while (rs.next())
            {
                vp = new ValueNamePair(rs.getString(1), rs.getString(2)); //  returns
                m_cbbActivity.addItem(vp);
            }
            m_cbbActivity.setSelectedIndex(0);
        }
        catch (Exception e)
        {
            s_log.log(Level.SEVERE,"VEmployeeListPanel.loadActivity", e);
        }
    }


    /**
     * Load the Employer
     *  
     */
    private void loadEmployer()
    {
       
    	if ( m_cbbEmployer == null)
    		return;
    	
        String sql = "SELECT P_Employer_ID, Name FROM P_Employer WHERE Type = 'F' and IsActive = 'Y' ";
	    
        sql = sql + " And P_Employer." + MRole.getDefault().getClientWhere(false) + " ORDER BY Value";

        
        int selectedIndex = 0;
        try
        {
            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            ResultSet rs = pstmt.executeQuery();

            m_cbbEmployer.removeAllItems();

            ValueNamePair vp = null;
            while (rs.next())
            {
                vp = new ValueNamePair(rs.getString(1), rs.getString(2)); //  returns
                m_cbbEmployer.addItem(vp);
            }
            m_cbbEmployer.setSelectedIndex(0);
        }
        catch (Exception e)
        {
            s_log.log(Level.SEVERE,"VEmployeeListPanel.loadEmployer", e);
        }
    }
    
    /**
     * Load the paymentType
     *  
     */
    private void loadPaymentType()
    {
        try
        {
        	String Sql = null;
    		if (Env.isBaseLanguage(Env.getCtx(), "AD_Ref_List"))
    			Sql = "SELECT arl.value, arl.name " 
    				+ "FROM ad_ref_list arl " 
    				+ "WHERE arl.ad_reference_id = 2000055 ";
	        else
		       Sql = "SELECT arl.value, arlt.name  " +
		             "FROM ad_ref_list_trl arlt INNER JOIN ad_ref_list arl ON arlt.ad_ref_list_id = arl.ad_ref_list_id " +
		             "WHERE arl.ad_reference_id = 2000055 and AD_Language = ? "; 

		    PreparedStatement pstmt = DB.prepareStatement( Sql, null );
    		if ( ! Env.isBaseLanguage(Env.getCtx(), "AD_Ref_List"))
                pstmt.setString(1, Env.getAD_Language(Env.getCtx()));

            ResultSet rs = pstmt.executeQuery();
//            String lang = Env.getLanguage(Env.getCtx()).getLanguageCode();
            while( rs.next() ) {
                m_cbbPaymentType.addItem( new ValueNamePair(rs.getString(1), rs.getString("name")) );
            }
            m_cbbPaymentType.setSelectedIndex(0);
        }

        catch (Exception e)
        {
            s_log.log(Level.SEVERE,"VSheetHeaderPanel.loadPaymentType", e);
        }
    }
    
    /**
     * Load sheetType
     *  
     */
    private void loadSheetType()
    {
    	//Permet de ne pas déclancher le rafrachissement des boutons si c'est 
    	//un load qui est effectué et non un changement de sheetType
        try
        {
        	String Sql = null;
        	
    		if (Env.isBaseLanguage(Env.getCtx(), "AD_Ref_List"))
    			Sql = "SELECT arl.value, arl.name " 
    				+ "FROM ad_ref_list arl " 
    				+ "WHERE arl.ad_reference_id = 2000058 ";
	        else 
	        	Sql =  "SELECT arl.value, arlt.name " +
	            "FROM ad_ref_list_trl arlt INNER JOIN ad_ref_list arl ON arlt.ad_ref_list_id = arl.ad_ref_list_id " +
	            "WHERE arl.ad_reference_id = 2000058 AND arl.value not like 'Annulation%' and AD_Language = ? " +
// 2008-04-18 change order by
	            "ORDER BY arlt.name DESC ";

            PreparedStatement pstmt = DB.prepareStatement(Sql, null);
    		if ( ! Env.isBaseLanguage(Env.getCtx(), "AD_Ref_List"))
                pstmt.setString(1, Env.getAD_Language(Env.getCtx()));

            ResultSet rs = pstmt.executeQuery();
            while( rs.next() ) {
                m_cbbSheetType.addItem( new ValueNamePair(rs.getString(1), rs.getString("name")) );
            }
            m_cbbSheetType.setSelectedIndex(0);
        }

        catch (Exception e)
        {
            s_log.log(Level.SEVERE,"VSheetHeaderPanel.loadSheetType", e);
        }
    }

    /**
     * Load time sheet related information
     *  
     */
    private void loadTimeSheetInformation(int employeeID)
    {

        String employeeQuery = "Select P_Time_Sheet_ID, Value, SheetType, TimesheetStatus FROM P_TIME_SHEET WHERE P_EMPLOYEE_ID = ? AND P_PERIOD_ID = ? order by SheetType DESC, value";


        //Get the employee Details
        int periodID = getSelectedPeriodID();
        currentPeriodID = getSelectedPeriodID();

        
        //We don't need to load the employee TimeSheet Id every time this
        // method gets called.
        if (currentTimeSheetIndex == -1)
        {

            try
            {
                PreparedStatement pstmt = DB.prepareStatement(employeeQuery, null);
                pstmt.setInt(1, employeeID);
                pstmt.setInt(2, periodID);
                ResultSet rs = pstmt.executeQuery();

                //Clear the current TimeSheet Ids List
                timeSheetList.clear();

                while (rs.next())
                {
                    timeSheetList.add(rs.getBigDecimal(1));
                }
                rs.close();
                pstmt.close();
            }
            catch (Exception e)
            {
                s_log.log(Level.SEVERE,"VSheetHeaderPanel.refreshEmployeeInformation", e);
            }

            //				here we only need to take the first timesheet id if we have
            // more than one
            if (timeSheetList.size() != 0)
            {
                //Set the id number
                currentTimeID = ((BigDecimal) timeSheetList.get(0)).intValue();
                loadTimeSheet( currentTimeID );

                if ( timeSheetList.size() == 1)
                {
                    enableBtnNext(false);
                    enableBtnBefore(false);
                }


                currentTimeSheetIndex = currentTimeSheetIndex + 1;
                if (timeSheetList.size() > 1)
                    enableBtnNext(true);

            }
            else
            {
                //There is no data, reset the field
                currentTimeID = -1;
//                m_txtSheetNumber.setText("");
                loadTimeSheet( currentTimeID );
            }


            if (m_timeSheet.get_ID() == 0)
            {
            	m_timeSheet.setTimeSheetStatus(P_Time_Sheet.TIMESHEETSTATUS_Initial);
                m_btnInitial.setVisible(false);
            }

            else if (m_timeSheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Initial))
            {
            	m_btnInitial.setVisible(false);
            }
            else if (m_timeSheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Issued) || 
            		m_timeSheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Transferred))
            {
            	m_btnInitial.setVisible(false);
            }
            else
            {
            	m_btnInitial.setVisible(true);
            }
            

            	

            //Call the validation process
            setupValidationProcessButton();

        }
        else
        { //Get the next timeSheet for the current Employee

            //Set the id number
            currentTimeID = ((BigDecimal) timeSheetList.get(currentTimeSheetIndex)).intValue();
            loadTimeSheet( currentTimeID );
            //only activated next if were sure there is more then 1 timesheet
            if (currentTimeSheetIndex == 0 && timeSheetList.size() > 1)
                enableBtnNext(true);

        }

        loadTimeSheetInformation(); 
        
    }

    private void loadSupplement( int employeeId, int bookletId, long time,  
            					 CTextField jobTitle, CTextField jobType, 
            					 CTextField booklet, CTextField convention ) {
    	String paymentType = m_timeSheet.getPaymentType();
    	
    	P_Employee Employee = P_Employee.get( Env.getCtx(), employeeId, null );
    	
        if ( m_timeSheet.get_ID() != 0 )
        {
//        	P_Job_Title Job_Title = P_Job_Title.get( Env.getCtx(), Employee.getP_Job_Title_ID(), null);  
//        	P_Job_Type Job_Type   = P_Job_Type.get( Env.getCtx(), Employee.getP_Job_Type_ID(), null);

        	P_Job_Title Job_Title = null; 
        	P_Occupation_Group Occupation_Group = null; 
       		Job_Title = P_Job_Title.get( Env.getCtx(), m_timeSheet.getP_Job_Title_ID(), null);
       		Occupation_Group = P_Occupation_Group.get( Env.getCtx(), m_timeSheet.getP_Occupation_Group_ID(), null);
        	
        	P_Job_Type Job_Type   = P_Job_Type.get( Env.getCtx(), m_timeSheet.getP_Job_Type_ID(), null);
        	P_Distribution_Booklet Distribution_Booklet = P_Distribution_Booklet.get( Env.getCtx(), bookletId, null); 
        	P_Collective_Labour_Agr Collective_Labour_Agr = P_Collective_Labour_Agr.get( Env.getCtx(), m_timeSheet.getP_Collective_Labour_Agr_ID(), null);

        	
//    		jobTitle.setText( Job_Type.getValue() + " - " + Job_Type.getTrlName() );
    		jobTitle.setText( Occupation_Group.getValue() + " - " + Occupation_Group.getTrlName() );
    		jobTitle.setSelectionStart(0);
    		jobTitle.setSelectionEnd(0);

        	/*
        	if ( Job_Title.getValue() != null )
        	{
        		jobTitle.setText( Job_Title.getValue() + " - " + Job_Title.getTrlName() );
        		jobTitle.setSelectionStart(0);
        		jobTitle.setSelectionEnd(0);
        	}
        	else
        	{
        		jobTitle.setText( Occupation_Group.getValue() + " - " + Occupation_Group.getTrlName() );
                jobTitle.setSelectionStart(0);
                jobTitle.setSelectionEnd(0);
        		
        	}
  */      		


            jobType.setText(  Job_Type.getValue() + " - " + Job_Type.getTrlName() );

            if ( Distribution_Booklet.getValue() != null )
            	booklet.setText( Distribution_Booklet.getValue() + " - " + Distribution_Booklet.getTrlName() );
            else 
            	booklet.setText( " " );
            
        	booklet.setSelectionStart(0);
            booklet.setSelectionEnd(0);
            
            convention.setText( Collective_Labour_Agr.getValue() + " - " + Collective_Labour_Agr.getTrlName() );
        	convention.setSelectionStart(0);
            convention.setSelectionEnd(0);
            		
        }
        else
        {
        	jobTitle.setText("");
            jobType.setText( "");
            booklet.setText( "" );
            convention.setText( "" );
        }

       
        if( paymentType == null ) paymentType = Employee.getPaymentType();

    	
    	for( int i=0; i<m_cbbPaymentType.getItemCount(); i++ ) 
    	{
    	    ValueNamePair vp = (ValueNamePair)m_cbbPaymentType.getItemAt(i);
    	    if( vp.getValue().equals(paymentType) ) 
    	    {
    	        m_cbbPaymentType.setSelectedIndex(i);
    	        break;
    	    }
    	}

    
    	int Org_ID = m_timeSheet.getAD_Org_ID();

        if ( m_timeSheet.get_ID() == 0 ) 
        	Org_ID = Employee.getAD_Org_ID();

    	for( int i=0; i<m_cbbOrg.getItemCount(); i++ ) 
    	{
    	    ValueNamePair vp = (ValueNamePair)m_cbbOrg.getItemAt(i);
    	    if( vp.getID().equals( String.valueOf( Org_ID )  ) ) 
    	    {
    	        m_cbbOrg.setSelectedIndex(i);
    	        break;
    	    }
    	}

//+2011.11.16 -- modification, ajout employer dans la feuille de paie.    	
    	int Activity_ID =  m_timeSheet.getC_Activity_ID();
    	int Taxation_Region_ID =  m_timeSheet.getTaxation_Region_ID();
    	int Employer_ID =  m_timeSheet.getP_Employer_ID();

        if ( m_timeSheet.get_ID() == 0 )
        {
        	Activity_ID =  Employee.getC_Activity_ID();
        	Taxation_Region_ID = Employee.getTaxation_Region_ID();
        	Employer_ID = Employee.getP_Employer_ID();
        }

//        m_cbbActivity.setValue(new Integer( Activity_ID ));
//        m_cbbTaxation_Region.setValue(new Integer( Taxation_Region_ID ));
//        m_cbbEmployer.setValue(new Integer( Employer_ID ));
        
    	for( int i=0; i<m_cbbActivity.getItemCount(); i++ ) 
    	{
    	    ValueNamePair vp = (ValueNamePair)m_cbbActivity.getItemAt(i);
    	    if( vp.getID().equals( String.valueOf( Activity_ID )  ) ) 
    	    {
    	        m_cbbActivity.setSelectedIndex(i);
    	        break;
    	    }
    	}

    	for( int i=0; i<m_cbbTaxation_Region.getItemCount(); i++ ) 
    	{
    	    ValueNamePair vp = (ValueNamePair)m_cbbTaxation_Region.getItemAt(i);
    	    if( vp.getID().equals( String.valueOf( Taxation_Region_ID )  ) ) 
    	    {
    	        m_cbbTaxation_Region.setSelectedIndex(i);
    	        break;
    	    }
    	}

    	for( int i=0; i<m_cbbEmployer.getItemCount(); i++ ) 
    	{
    	    ValueNamePair vp = (ValueNamePair)m_cbbEmployer.getItemAt(i);
    	    if( vp.getID().equals( String.valueOf( Employer_ID )  ) ) 
    	    {
    	    	m_cbbEmployer.setSelectedIndex(i);
    	        break;
    	    }
    	}

 //-2011.11.16
    }
    
    private void loadTimeSheetInformation()
    {
    	P_Payment Payment;
		
        if (m_timeSheet.get_ID() == 0)
        {
        	m_txtSheetStatus.setText("");
        }
        else
        {
        	m_txtSheetStatus.setText( m_timeSheet.GetTimeSheetStatusDsc() );
        }

//        m_txtSheetNumber.setText(m_timeSheet.getValue());

        if ( m_timeSheet.getSheetType() != null ) {
            String sheetType = m_timeSheet.getSheetType();
        	for( int i=0; i<m_cbbSheetType.getItemCount(); i++ ) {
        	    ValueNamePair vp = (ValueNamePair)m_cbbSheetType.getItemAt(i);
        	    if( vp.getValue().equals(sheetType) ) {
        	        m_cbbSheetType.setSelectedIndex(i);
        	        break;
        	    }
        	}
        }


        if ( m_timeSheet.getP_Payment_ID() != 0 )
        {
        	String trxName = null;
            Payment = new P_Payment( Env.getCtx(), m_timeSheet.getP_Payment_ID(), trxName );
            
            if ( Payment.getGrossEarnings() != null)
            	m_txtGrossEarnings.setText( Payment.getGrossEarnings().setScale(2,BigDecimal.ROUND_HALF_UP).toString());
            else
            	m_txtGrossEarnings.setText( "" );
            if ( Payment.getNetPay() != null )
            	m_txtNetPay.setText( Payment.getNetPay().setScale(2,BigDecimal.ROUND_HALF_UP).toString());
            else
            	m_txtNetPay.setText( "" );
        }
        else
        {
        	m_txtGrossEarnings.setText( "" );
        	m_txtNetPay.setText( "" );
        }

        String trxName = null;
        P_Period period = null;
        if ( getSelectedPeriodID() != 0)
        {
            period = P_Period.get( Env.getCtx(), getSelectedPeriodID(), trxName );
            m_txtPeriodStart.setText( period.getStartDate().toString().substring(0,10) 
            + Msg.translate(Env.getCtx(),"TimeSheetTo" )//" au " 
            + period.getEndDate().toString().substring(0,10) );
        }

        

        if (m_timeSheet.get_ID() == 0)
        {
        	m_timeSheet.setTimeSheetStatus(P_Time_Sheet.TIMESHEETSTATUS_Initial);
//        	m_txtSheetNumber.setText("");
        	m_cbbPaymentType.setSelectedIndex(0);
        	m_cbbSheetType.setSelectedIndex(0);
        	m_txtSheetStatus.setText("");
        }

        if ( period != null)
        {
            if ( m_timeSheet == null || m_timeSheet.getPayDate() == null  )
            	m_txtPeriodPay.setText( period.getPayDate().toString().substring(0,10));
            else
            	m_txtPeriodPay.setText( m_timeSheet.getPayDate().toString().substring(0,10) );
        	
        }

        setupValidationProcessButton();

        if ( period != null )
        loadSupplement( currentEmployeeID, m_timeSheet.getP_Distribution_Booklet_ID(), period.getStartDate().getTime(),  
                		m_txtJobTitle, m_txtJobType, m_txtBooklet, m_txtConvention );
    }
    
    private void setupValidationProcessButton()
    {
//    	boolean isSheetNull = m_timeSheet == null;
//    	System.out.println("isSheetNull:"+isSheetNull);
    	boolean sheetTypeIsAdjustment = m_timeSheet.getSheetType() != null 
    	? m_timeSheet.getSheetType().equals(P_Time_Sheet.SHEETTYPE_Adjustment)
    	: false;

    	boolean initialVisible =
            m_timeSheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Initial) ||
            m_timeSheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Issued) || 
    		m_timeSheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Transferred) ||
    		m_timeSheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Cancelled) ||
    		sheetTypeIsAdjustment;
    		
//        System.out.println("initial Visible:"+!initialVisible);
    	m_btnInitial.setVisible(!initialVisible);
//        System.out.println("process, getNextStateValue():"+getNextStateValue());
    	m_btnProcess.setText(getNextStateValue());
    	m_btnMoreInfo.setText( Msg.translate(Env.getCtx(),"More Info" ) );
    	m_btnMoreInfo.setToolTipText( Msg.translate(Env.getCtx(),"More Info Time Sheet" ));

//        P_Period period = P_Period.get( Env.getCtx(), getSelectedPeriodID(), null );
    	boolean processVisible = 
    	    m_timeSheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Issued) ||
    		m_timeSheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Error) ||
     		m_timeSheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Cancelled) ||
    		m_timeSheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Transferred) ||
     		sheetTypeIsAdjustment;

//    		||
//    		"C".equals(period.getPeriodStatus()); 
//    	System.out.println("process visible:"+!processVisible);
    	m_btnProcess.setVisible(!processVisible);

    	boolean moreInfoVisible = 
    		m_timeSheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Initial) ||
    		sheetTypeIsAdjustment;

    	m_btnMoreInfo.setVisible( moreInfoVisible );

    	m_btnGenBonus.setText( "Prime" );

    	if (m_timeSheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Initial) 
    			&& m_timeSheet.getSheetType() != null && m_timeSheet.getSheetType().equals(P_Time_Sheet.SHEETTYPE_Complementary)
    			)
    	{
        	m_btnGenBonus.setVisible( true);
    		
    	}
    	else
    	{
        	m_btnGenBonus.setVisible( false);
    	}
    	
	    m_cbbOrg.setEnabled(false);
	    if ( sheetTypeIsAdjustment &&  
	    	( m_timeSheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Initial) ||
	    	  m_timeSheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Calculated ) ||
	          m_timeSheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Error		  ) )  
           )
	    {
		    m_cbbTaxation_Region.setEnabled(true);
	    }
	    else
	    {
		    m_cbbTaxation_Region.setEnabled(false);
	    }

	    m_cbbActivity.setEnabled(false);

    	if( m_main.isReadOnly() == false ) {
    	    m_cbbSheetType.setEnabled( m_timeSheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Initial) );
    	    m_cbbPaymentType.setEnabled(true);
            if ( m_timeSheet != null &&  m_timeSheet.getSheetType() != null && m_timeSheet.getSheetType().equals(P_Time_Sheet.SHEETTYPE_Advance) && m_timeSheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Initial))
            {
    	    	this.m_txtPeriodPay.setEditable(true);
            }
            else
            {
        	    this.m_txtPeriodPay.setEditable(false);
        	    if ( m_timeSheet != null &&  m_timeSheet.getSheetType() != null && m_timeSheet.getSheetType().equals(P_Time_Sheet.SHEETTYPE_Adjustment))
        	    {
        	    	// Les ajustement sont toujours de type dépot. 
        	    	// Car seulement la procédure d'émission des dépots change le status d'un ajustement a émis.
        	    	this.m_cbbPaymentType.setEnabled(false);
        	    }
        	    else
        	    {
        	    	this.m_cbbPaymentType.setEnabled(true);
        	    }
            }
    	}
    	else {
    	    m_cbbSheetType.setEnabled(false);
    	    m_cbbPaymentType.setEnabled(false);
    	    this.m_txtPeriodPay.setEditable(false);
    	}

    }

    private String getNextStateValue()
    {

        String state = "";

        if (m_timeSheet.get_ID() == 0)
        {
            //		else if(currentState.equals("") || currentState == null)
            state = Msg.translate(Env.getCtx(), "TimeSheetGenerate");  //"Générer";
        }
        else if (m_timeSheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Initial) 
        		 && m_timeSheet.getSheetType().equals(P_Time_Sheet.SHEETTYPE_Regular)
        		 && m_timeSheet.getNumberOfGain() == 0 )
        {
            state = Msg.translate(Env.getCtx(), "TimeSheetGenerate"); //"Générer";
        }
        else if (m_timeSheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Initial))
        {
            state = Msg.translate(Env.getCtx(), "TimeSheetValidate");//"Valider";
        }
        else if (m_timeSheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Validated))
        {
            state = Msg.translate(Env.getCtx(), "TimeSheetCalculate");//"Calculer";
        }
        else if (m_timeSheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Calculated))
        {
            state = Msg.translate(Env.getCtx(), "TimeSheetRecalculate");//"Recalculer";
        }
        else if (m_timeSheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Issued) || 
        		m_timeSheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Transferred))
        {
            state = Msg.translate(Env.getCtx(), "Cancel");//"Annuler";
        }

        return state;
    }

    public int getSelectedPeriodID()
    {
        ValueNamePair pp = (ValueNamePair) m_cbbPayPeriod.getSelectedItem();
        if ( pp == null || pp.getValue() == null )
        	return 0;
        
        return Integer.parseInt(pp.getValue());
    }


    public int getCurrentTimeSheetID()
    {
        return m_timeSheet.get_ID();
    }

/*
    Waiting m_waiting = null;
	private void lockUIAndWait(ProcessInfo pi)
	{
		m_waiting = new Waiting (Env.getFrame(this), Msg.getMsg(Env.getCtx(), "Processing"), false, 0);
		m_waiting.toFront();
		m_waiting.setVisible(true);
		lockUI(pi);
	}   //  lock
*/
	/**
	 *  Unlock & dispose Waiting.
	 * 	Called from run()
	 */
/*	private void unlockUIAndWait(ProcessInfo pi)
	{
		if (m_waiting != null) m_waiting.dispose();
		m_waiting = null;
		unlockUI(pi);
	}   //  unlock
*/
    
    /**
     * Lock User Interface. Called from the Worker before processing
     * 
     * @param pi
     *            process info
     */
    public void lockUI(ProcessInfo pi)
    {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        this.setEnabled(false);
    } //  lockUI

    /**
     * Unlock User Interface. Called from the Worker when processing is done
     * 
     * @param pi
     *            result of execute ASync call
     */
    public void unlockUI(ProcessInfo pi)
    {
        this.setEnabled(true);
        this.setCursor(Cursor.getDefaultCursor());
        
        MPInstance processInfo = new MPInstance(Env.getCtx(), pi.getAD_PInstance_ID(), null);
        s_log.info( "Message " + processInfo.getErrorMsg() );
//        ADialog.error(0, m_main, processInfo.getErrorMsg());

        m_main.reloadInfo();
    } //  unlockUI

    /**
     * Is the UI locked (Internal method)
     * 
     * @return true, if UI is locked
     */
    public boolean isUILocked()
    {
        return this.isEnabled();
    } //  isUILocked

    /**
     * Method to be executed async. Called from the Worker
     * 
     * @param pi
     *            ProcessInfo
     */
    public void executeASync(ProcessInfo pi)
    {
    } //  executeASync

    //
    // Delete current record.
    // 
    public void delete()
    {
    	m_timeSheet.delete(true);
        currentTimeSheetIndex = -1;
    }

    public int getTimeSheetID()
    {
        //We should return the current timeSheetID
    	if ( m_timeSheet == null )
    		return -1;
        return m_timeSheet.getP_Time_Sheet_ID();
    }

    public boolean addTimeSheet()
    {
    	boolean retValue = true;
    	return retValue;
    }
    
    public void resetDefaultPeriod() {
        m_cbbPayPeriod.setSelectedIndex(m_defaultPeriodIndex);
    }
    
    public void save( String trxName ) {
        ValueNamePair pp = (ValueNamePair) m_cbbPaymentType.getSelectedItem();
        m_timeSheet.setPaymentType( pp.getValue() );
        pp = (ValueNamePair) m_cbbSheetType.getSelectedItem();
        m_timeSheet.setSheetType( pp.getValue() );
        
        int year  = Integer.parseInt( this.m_txtPeriodPay.getText().substring(0,4));
        int month = Integer.parseInt( this.m_txtPeriodPay.getText().substring(5,7));
        int day   = Integer.parseInt( this.m_txtPeriodPay.getText().substring(8,10));
        m_timeSheet.setPayDate( TimeUtil.getDay( year, month, day )  );
        
        pp = (ValueNamePair) this.m_cbbTaxation_Region.getSelectedItem();
        int Taxation_Region_ID = Integer.parseInt(pp.getValue());

        m_timeSheet.setTaxation_Region_ID(Taxation_Region_ID);
        m_timeSheet.save(trxName);
        if ( m_timeSheet.getP_Payment_ID() != 0)
        {
        	P_Payment Payment = P_Payment.get( Env.getCtx(), m_timeSheet.getP_Payment_ID(), trxName);
        	if ( m_timeSheet.getTaxation_Region_ID() != Payment.getTaxation_Region_ID())
        	{
            	Payment.setTaxation_Region_ID(m_timeSheet.getTaxation_Region_ID());
            	Payment.save();
        	}
        }
    }
    
    private void enableBtnBefore( boolean etat ) {
        m_btnBefore.setEnabled( etat );
        boolean visible = etat || m_btnNext.isEnabled();
        m_btnBefore.setVisible( visible );
        m_btnNext.setVisible( visible );
    }
    
    private void enableBtnNext( boolean etat ) {
        m_btnNext.setEnabled( etat );
        boolean visible = etat || m_btnNext.isEnabled();
        m_btnBefore.setVisible( visible );
        m_btnNext.setVisible( visible );
    }


} //  @jve:decl-index=0:visual-constraint="10,10"
