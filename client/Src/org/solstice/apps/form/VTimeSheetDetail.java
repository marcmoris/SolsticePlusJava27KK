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
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat; 
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.compiere.apps.ADialog;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CButton;
import org.compiere.swing.CComboBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.swing.CScrollPane;
import org.compiere.swing.CTabbedPane;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Language;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;
import org.compiere.util.Trx;
import org.solstice.apps.form.VTimeSheetModel.DETCreditsModel;
import org.solstice.apps.form.VTimeSheetModel.DETDeductionModel;
import org.solstice.apps.form.VTimeSheetModel.DETTaxBenefitModel;
import org.solstice.apps.form.VTimeSheetModel.DETableModel;
import org.solstice.apps.form.VTimeSheetModel.DETableRenderer;
import org.solstice.apps.form.VTimeSheetModel.DetailLigne;
import org.solstice.apps.form.VTimeSheetModel.DetailModel;
import org.solstice.apps.form.VTimeSheetModel.DetailLigneSum;
import org.solstice.apps.form.VTimeSheetModel.DetailModelSum;
import org.solstice.apps.form.VTimeSheetModel.ModelLookup;
import org.solstice.apps.form.VTimeSheetModel.Lookup.LookupCMDescription;
import org.solstice.apps.form.VTimeSheetModel.Lookup.LookupCredits;
import org.solstice.apps.form.VTimeSheetModel.Lookup.LookupDeduction;
import org.solstice.apps.form.VTimeSheetModel.Lookup.LookupPeriod;
import org.solstice.apps.form.VTimeSheetModel.Lookup.LookupSchedule;
import org.solstice.apps.form.VTimeSheetModel.Lookup.LookupTaxableBenefit;
import org.solstice.apps.form.VTimeSheetModel.Lookup.LookupVariationType;

import solstice.model.P_Assignment;
import solstice.model.P_Assignment_Param;
import solstice.model.P_Credits;
import solstice.model.P_Credits_Movement;
import solstice.model.P_Deduction_Param;
import solstice.model.P_Employee;
import solstice.model.P_Employee_Deduction;
import solstice.model.P_Gain;
import solstice.model.P_Payment;
import solstice.model.P_Payment_Group;
import solstice.model.P_Payment_Deduction;
import solstice.model.P_Payment_Taxable_Benefit;
import solstice.model.P_Period;
import solstice.model.P_Frequency;
import solstice.model.P_Time_Sheet;
import solstice.model.P_Time_Sheet_Detail;
import solstice.process.TimeValidation;
import solstice.process.Calcul_Credits;

import solstice.process.CalculNet;
import solstice.utils.PgiUtil;

import org.compiere.grid.ed.*;

/**
 * @author frabou01
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class VTimeSheetDetail extends CPanel implements ChangeListener
{

    /**
     * Comment for <code>serialVersionUID</code>
     */
	
	public int DisplayDetail = Integer.parseInt( PgiUtil.getSolsticeParameter(Env.getCtx(), "DefaultTimeSheetModel"));
    private static final long serialVersionUID = 1L;

    private CTabbedPane m_tabs = null;

    // index colonne sommaire 
    private final int SUMMARY_QTE = 4+1;
    private final int SUMMARY_MNT = 5+1;
    
    //Tab Value
    public final int TIME_TAB = 0;
    public final int SUMMARY_TAB = 1;
    public final int DEDUCTION_TAB = 2;
    public final int ADVANTAGE_TAB = 3;
    public final int BANK_TAB = 4;
    public final int PRIME_TAB = 5;
    public final int ERROR_TAB = 6;
//    public final int ENTRY_TAB = 7;
    public final int ENTRY_TAB_360 = 7;

    private JTable m_timeTable = null;
    private JTable m_summaryTable = null;
    private JTable m_deductionTable = null;
    private JTable m_advantageTable = null;
    private JTable m_bankTable = null;
    private JTable m_primeTable = null;
    private JTable m_errorTable = null;
//    private JTable m_paymentEntryTable = null;
    private JTable m_paymentEntryTable360 = null;
    
    private ModelLookup m_lookup;
    public ModelLookup getLookup() { return m_lookup; }
    
    /**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (VTimeSheetDetail.class);
	
	Language language = Env.getLanguage(Env.getCtx());

    //
    // Buttons
    //
    private VTimeSheetHeader m_header = null; 

    /**
     * This is the default constructor
     */
    public VTimeSheetDetail( VTimeSheetHeader header )
    {
        super();
        CompiereColor.setBackground(this);
        m_header = header;
        m_header.setDetail(this);
        initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize()
    {
    	String trxName = null;
    	
        P_Period period = P_Period.get( Env.getCtx(), m_header.getSelectedPeriodID(), trxName);
        P_Frequency frequency = P_Frequency.get( Env.getCtx(), period.getP_Frequency_ID(), trxName);
        
//        if ( frequency.getNumberOfPeriod() == 24 )
        //2011.08.17 Lecture du paramêtre dans le groupe de payment 
        if ( m_header.getCurrentEmployeeID() > 0 )
        {
            P_Employee Employee = P_Employee.get(Env.getCtx(), m_header.getCurrentEmployeeID(), trxName);
            P_Payment_Group PaymentGroup = P_Payment_Group.get(Env.getCtx(), Employee.getP_Payment_Group_ID(), trxName);
            if ( PaymentGroup != null && PaymentGroup.getTimeSheetModel() != null && PaymentGroup.getTimeSheetModel().equals( P_Payment_Group.TIMESHEETMODEL_Summary) )
            	DisplayDetail = 1;	
            else
            	DisplayDetail = 0;
        }
        else
        	DisplayDetail = Integer.parseInt( PgiUtil.getSolsticeParameter(Env.getCtx(), "DefaultTimeSheetModel"));

        
        //TODO changer de place ou on change d'employer 
     /*   if ( m_header.getCurrentEmployeeID() > 0 )
        {
            P_Employee employee = P_Employee.get( Env.getCtx(), m_header.getCurrentEmployeeID(), trxName);
            P_Payment_Group paymentGroup = P_Payment_Group.get( Env.getCtx(), employee.getP_Payment_Group_ID(), trxName);
            DisplayDetail = Integer.parseInt( paymentGroup.getTimeSheetModel() );
        }*/
        
        	
//      + new m.m
             int numberOfWeek = new BigDecimal( TimeUtil.getDaysBetween(period.getStartDate(), period.getEndDate()) +1 ) .divide(new BigDecimal(7), 0, BigDecimal.ROUND_UP).intValue();
             m_lookup = new ModelLookup( numberOfWeek );
//      - new m.m
//       m_lookup = new ModelLookup();

        setLayout( new BorderLayout() );
        add(getPaymentEntryTab());
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Msg.translate(Env.getCtx(),"TimeSheetDetail" )), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        
        m_lookup.setPeriod(period);
        m_lookup.setPeriodOrigin(period);
        m_lookup.setPeriodSubsequent(period);
    }
    
    /**
     * This method initializes jTabbedPane
     * 
     * @return javax.swing.JTabbedPane
     */
    private VComboBox m_cbCorrection;
    private VComboBox m_cbCorrection2;
    private CButton m_btnCorrection;
    private CButton m_btnCorrection2;
    private CPanel initTimePanel() {
    	CPanel timePanel = new CPanel( new GridBagLayout() );

    	GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridheight = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
    	
    	timePanel.add( new JScrollPane(getTimeTable()), gbc );
    	
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.gridwidth = 1;
        m_cbCorrection = new VComboBox(m_lookup.getPeriodModelOrigin());
        m_cbCorrection.setMinimumSize( new Dimension(100, 20) );
        m_cbCorrection.setPreferredSize( new Dimension(100, 20) );
        m_cbCorrection.setMaximumSize( new Dimension(100, 20) );
        m_cbCorrection.setFocusable(false);
        m_cbCorrection.setEditable(false);
    	timePanel.add( m_cbCorrection, gbc );
    	
    	m_btnCorrection = new CButton(Msg.translate(Env.getCtx(), "AjouterPeriodeCorrection"));
    	if (DisplayDetail == 0)
    	{
        	m_btnCorrection.addActionListener( new ActionListener() { 
                    public void actionPerformed(ActionEvent ev) {
        		        DetailModel model = (DetailModel)m_timeTable.getModel();
        		        KeyNamePair knp = (KeyNamePair)m_cbCorrection.getSelectedItem();
        		        if( knp != null ) model.addPeriodeCorrection( knp );
                    }
            	} );
    	}
    	else
    	{
        	m_btnCorrection.addActionListener( new ActionListener() { 
                    public void actionPerformed(ActionEvent ev) {
        		        DetailModelSum model = (DetailModelSum)m_timeTable.getModel();
        		        KeyNamePair knp = (KeyNamePair)m_cbCorrection.getSelectedItem();
        		        if( knp != null ) model.addPeriodeCorrection( knp );
                    }
            	} );
    	}    	
    	m_btnCorrection.setMinimumSize( new Dimension(200, 20) );
    	m_btnCorrection.setPreferredSize( new Dimension(200, 20) );
    	m_btnCorrection.setMaximumSize( new Dimension(Integer.MAX_VALUE, 20) );
    	m_btnCorrection.setFocusable(false);
    	
    	timePanel.add( m_btnCorrection, gbc );


        m_cbCorrection2 = new VComboBox(m_lookup.getPeriodModelSubsequent());
        m_cbCorrection2.setMinimumSize( new Dimension(100, 20) );
        m_cbCorrection2.setPreferredSize( new Dimension(100, 20) );
        m_cbCorrection2.setMaximumSize( new Dimension(100, 20) );

        m_cbCorrection2.setFocusable(false);
        m_cbCorrection2.setEditable(false);
    	timePanel.add( m_cbCorrection2, gbc );
  	
    	m_btnCorrection2 = new CButton(Msg.translate(Env.getCtx(), "AjouterPeriodeSubsequentes"));
    	if (DisplayDetail == 0)
    	{
			m_btnCorrection2.addActionListener( new ActionListener() {
	    		
	            public void actionPerformed(ActionEvent ev) {
			        DetailModel model = (DetailModel)m_timeTable.getModel();
			        KeyNamePair knp = (KeyNamePair)m_cbCorrection2.getSelectedItem();
			        if( knp != null ) model.addPeriodeCorrection( knp );
	            }
	    	} );
    	}
    	else
    	{
			m_btnCorrection2.addActionListener( new ActionListener() {
	    		
	            public void actionPerformed(ActionEvent ev) {
			        DetailModelSum model = (DetailModelSum)m_timeTable.getModel();
			        KeyNamePair knp = (KeyNamePair)m_cbCorrection2.getSelectedItem();
			        if( knp != null ) model.addPeriodeCorrection( knp );
	            }
	    	} );   		
    	}
    	m_btnCorrection2.setMinimumSize( new Dimension(200, 20) );
    	m_btnCorrection2.setPreferredSize( new Dimension(200, 20) );
    	m_btnCorrection2.setMaximumSize( new Dimension(Integer.MAX_VALUE, 20) );
    	
    	m_btnCorrection2.setFocusable(false);
    	timePanel.add( m_btnCorrection2, gbc );
        gbc.gridwidth = GridBagConstraints.REMAINDER;
    	timePanel.add( Box.createHorizontalGlue(), gbc );
    		
     	return timePanel;
    }
    
	private CLabel m_qteValue;
	private CLabel m_mntValue;
    private CPanel initSummaryPanel() {
    	CPanel summaryPanel = new CPanel( new GridBagLayout() );

    	GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridheight = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
    	
        summaryPanel.add( new JScrollPane(getSummaryTable()), gbc );
    	
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.gridwidth = 1;
        
        CLabel qteLabel = new CLabel( "Total quantité:" );
        qteLabel.setMinimumSize( new Dimension(100, 20) );
        qteLabel.setPreferredSize( new Dimension(100, 20) );
        qteLabel.setMaximumSize( new Dimension(100, 20) );
    	summaryPanel.add( qteLabel, gbc );
    	
    	m_qteValue = new CLabel();
    	m_qteValue.setMinimumSize( new Dimension(100, 20) );
        m_qteValue.setPreferredSize( new Dimension(100, 20) );
        m_qteValue.setMaximumSize( new Dimension(100, 20) );
    	summaryPanel.add( m_qteValue, gbc );
    	
        CLabel mntLabel = new CLabel( "Total montant:" );
        mntLabel.setMinimumSize( new Dimension(100, 20) );
        mntLabel.setPreferredSize( new Dimension(100, 20) );
        mntLabel.setMaximumSize( new Dimension(100, 20) );
    	summaryPanel.add( mntLabel, gbc );
    	
        m_mntValue = new CLabel();
        m_mntValue.setMinimumSize( new Dimension(100, 20) );
        m_mntValue.setPreferredSize( new Dimension(100, 20) );
        m_mntValue.setMaximumSize( new Dimension(100, 20) );
    	summaryPanel.add( m_mntValue, gbc );

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        summaryPanel.add( Box.createHorizontalGlue(), gbc );

        return summaryPanel;
    }
    
    private CTabbedPane getPaymentEntryTab()
    {
        if (m_tabs == null)
        {
        	m_tabs = new CTabbedPane();
        	m_tabs.addTab(Msg.translate(Env.getCtx(), "Time"), null, initTimePanel());
        	m_tabs.addTab(Msg.translate(Env.getCtx(), "Summary"), null, initSummaryPanel());
        	m_tabs.addTab(Msg.translate(Env.getCtx(), "Deduction"), null, new CScrollPane(getDeductionTable()));
        	m_tabs.addTab(Msg.translate(Env.getCtx(), "TaxableBenefit"), null, new CScrollPane(getAdvantageTable()));
        	m_tabs.addTab(Msg.translate(Env.getCtx(), "Credits"), null, new CScrollPane(getBankTable()));
        	m_tabs.addTab(Msg.translate(Env.getCtx(), "Prime"), null, new CScrollPane(getPrimeTable()));
        	m_tabs.addTab(Msg.translate(Env.getCtx(), "Controles"), null, new CScrollPane(getErrorTable()));
//        	m_tabs.addTab(Msg.translate(Env.getCtx(), "Accounting"), null, new CScrollPane(getPaymentEntryTable()));
        	m_tabs.addTab(Msg.translate(Env.getCtx(), "Accounting"), null, new CScrollPane(getPaymentEntryTable360()));
        	m_tabs.addChangeListener(this);
        }
        return m_tabs;
    }

    /**
     * This method initializes jTable
     * 
     * @return javax.swing.JTable
     */
    private JTable getTimeTable()
    {
        if (m_timeTable == null) m_timeTable = new JTable();
        m_timeTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        return m_timeTable;
    }


    /**
     * This method initializes jButton
     * 
     * @return javax.swing.JButton
     */
    public boolean save() {
    	String trxName = null;
    	//2023-09-05
	//String trxName = Trx.createTrxName("save_ts");
// 	Trx trx = Trx.get( trxName, true );
//		trx.start();

        boolean ret = save_impl(trxName);
/*        
     if( ret ) trx.commit();
    	else trx.rollback();
    	trx.close();
 */   	
    	return ret;
    }
    
    public boolean save_impl( String trxName )
    {
	    boolean recalcul = false;
    	try {
    		
	    	if( m_header.getTimeSheet() == null || m_header.getTimeSheetID() == 0 ) 
	    	{
	    		return false;//m_header.createTimeSheet( trxName );
			}
	
	        m_header.save(trxName);
	        
	    	String status = m_header.getTimeSheet().getTimeSheetStatus();

	    	if( status.equals(P_Time_Sheet.TIMESHEETSTATUS_Error) ) {
	    	    TableModel model = m_errorTable.getModel();
	    	    boolean override = true;
	    	    for( int i=0; i<model.getRowCount(); i++ ) {
	    	        String level = (String)model.getValueAt(i, 0);
	    	        Boolean obj = (Boolean)model.getValueAt(i, 2);
	    	        if( "E".equals(level) ) { override = false; break; }
    	            if( "W".equals(level) && ((Boolean)obj).booleanValue() == false ) {
    	                override = false; break;
	    	        }
	    	    }
	    	    
	    	    if( override ) {
	    	        m_header.getTimeSheet().setTimeSheetStatus(P_Time_Sheet.TIMESHEETSTATUS_Calculated);
	    	    }
	    	}
	    	
	    	if( status.equals(P_Time_Sheet.TIMESHEETSTATUS_Initial) || ( m_header.isFeuilleAjustement() && m_header.getTimeSheet().getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Calculated)) ) 
	    	{
	        	if( m_loaded[TIME_TAB] ) 
	        	{
			    	if (DisplayDetail == 0)
			    	{
				        DetailModel model = (DetailModel)m_timeTable.getModel();
				        for(int i=0; i<model.getRowCount(); i++) 
				        {
				        	if( model.isDataRow(i) ) 
				        	{
				                DetailLigne ligne = ((DetailModel)m_timeTable.getModel()).getLigne(i);
				        		if( model.isRowDeleted(i) ) DeleteDetail(model, ligne, trxName);
				        		else SaveDetail(model, ligne, trxName);
				        	}
				        }
			    	}
			    	else
			    	{	
				        DetailModelSum model = (DetailModelSum)m_timeTable.getModel();
				        for(int i=0; i<model.getRowCount(); i++) 
				        {
				        	if( model.isDataRow(i) ) 
				        	{
				                DetailLigneSum ligne = ((DetailModelSum)m_timeTable.getModel()).getLigne(i);
				        		if( model.isRowDeleted(i) ) DeleteDetailSum(model, ligne, trxName);
				        		else SaveDetailSum(model, ligne, trxName);
				        	}
				        }
			    	}    	        		
	    	    }
	    	}
    			    
			// Si les tables n'ont pas encore été initialisé, elles sont avec le model par défaut
			TableModel model = m_deductionTable.getModel();
			if( model instanceof DETDeductionModel && m_loaded[DEDUCTION_TAB] ) {
			    DETDeductionModel mdl = (DETDeductionModel)model;
			    recalcul |= mdl.isDirty();
				SaveDeduction( mdl, trxName );
			}
			
			model = m_advantageTable.getModel();
			if( model instanceof DETTaxBenefitModel && m_loaded[ADVANTAGE_TAB] ) {
			    DETTaxBenefitModel mdl = (DETTaxBenefitModel)model;
			    recalcul |= mdl.isDirty();
				SaveAvantage( mdl, trxName );
			}
			

	    	if( status.equals(P_Time_Sheet.TIMESHEETSTATUS_Error) ) {
	    	    boolean allClear = true;
	    	    for( int i=0; i<m_errorTable.getRowCount(); i++ ) {
	    	        Object obj = m_errorTable.getValueAt(i,2);
	    	        if( obj instanceof Boolean ) {
	    	            allClear &= ((Boolean)obj).booleanValue();
	    	        }
	    	        else allClear = false;
	    	    }
	    	    if( allClear ) {
	    	        P_Time_Sheet ts = m_header.getTimeSheet();
	    	        ts.setTimeSheetStatus( P_Time_Sheet.TIMESHEETSTATUS_Calculated );
	    	        ts.save();
	    	    }
	    	}
			else 
			{
			}

	    	P_Time_Sheet ts = m_header.getTimeSheet();

    	    if( m_header.isFeuilleAjustement() )
    	    {
 				//-	    	        
    	        // créer un payment au besoin
    	        P_Period period = P_Period.get( Env.getCtx(), ts.getP_Period_ID(), trxName );
    	        P_Employee employee = P_Employee.get( Env.getCtx(), ts.getP_Employee_ID(), trxName );
    	        if( ts.getP_Payment_ID() <= 0 ) {
					P_Payment payment = new P_Payment( Env.getCtx (), -1, trxName);
					payment.setAD_Org_ID( employee.getAD_Org_ID());
					payment.setC_Activity_ID( employee.getC_Activity_ID());
					payment.setP_Department_ID(employee.getP_Department_ID());
					payment.setTaxation_Region_ID( employee.getTaxation_Region_ID());
					payment.setValue( payment.getP_Payment_ID() + ""  );
					payment.setP_Employee_ID( ts.getP_Employee_ID ());
					payment.setIsActive( true );
					payment.setP_Period_ID( period.getP_Period_ID () );
					payment.setP_Year_ID( period.getP_Year_ID() );
					payment.setP_Frequency_ID ( period.getP_Frequency_ID() );
					payment.setPayDate( period.getPayDate() );
					payment.setPaymentType( ts.getPaymentType() );
//+ 2011.11.16 Le numéro d'employeur est maintenant modifiable sur la feuille de paie						
//					payment.setP_Employer_ID( employee.getP_Employer_ID() );
					payment.setP_Employer_ID( ts.getP_Employer_ID() );
					payment.setP_Workplace_ID( ts.getP_Workplace_ID() );
//- 2011.11.16					
					payment.setP_Payment_Group_ID( ts.getP_Payment_Group_ID() );
					
					payment.setPaymentTypeDoc( ts.getSheetType() );
					payment.setAnnualSalary( employee.getAnnualSalary( period.getStartDate() ));
					payment.setP_Job_Title_ID( ts.getP_Job_Title_ID());
					payment.setP_Job_Type_ID(ts.getP_Job_Type_ID());
					payment.setP_Occupation_Group_ID( ts.getP_Occupation_Group_ID());
					payment.setP_Distribution_Booklet_ID( ts.getP_Distribution_Booklet_ID());
					payment.setP_Distribution_ID( ts.getP_Distribution_ID());
					payment.setValue( ts.getValue() );
	                payment.save();
	                ts.setP_Payment_ID( payment.get_ID() );
	                ts.setTimeSheetStatus(P_Time_Sheet.TIMESHEETSTATUS_Calculated); // P_Time_Sheet.TIMESHEETSTATUS_Validated);
	                ts.save();

    	        }
//    	      m.m+
				P_Payment payment = new P_Payment( Env.getCtx (), ts.getP_Payment_ID(), trxName);
				if ( payment != null ) // && payment.getNetPay() != null
				{
					P_Time_Sheet.deleteError( m_header.getTimeSheet().getP_Time_Sheet_ID() , trxName);
	                TimeValidation timeValidation = new TimeValidation( m_header.getTimeSheet(), trxName);
	                timeValidation.deleteDetail();
    				timeValidation.gen_time_sheet_detail( payment, trxName );
    				timeValidation.calcul_gain_credits( payment, trxName );
//    				timeValidation.Create_PaymentGainDistribution(payment, 0);
    				recalcul = true;

				}
//				m.m-

    	    }

//	    }
			model = m_bankTable.getModel();
			if( model instanceof DETCreditsModel && m_loaded[BANK_TAB] )  
				if( SaveBanque( (DETCreditsModel)model, trxName ) == false ) return false;

	    	
			if( recalcul ) 
			{
	            P_Payment payment = new P_Payment( Env.getCtx(), m_header.getTimeSheet().getP_Payment_ID(), trxName);
	            if ( payment != null && m_header.getTimeSheet().getP_Payment_ID() != 0 && payment.getTimeSheetStatus().equals( P_Payment.TIMESHEETSTATUS_Calculated) )
	            {
	                //2008-06-26 M.a.j Distribution
	            	//2019-07-08
//					payment.UpdatePaymentDistribution(trxName);

	            	P_Time_Sheet.deleteError( m_header.getTimeSheet().getP_Time_Sheet_ID() , trxName);
					
	            	TimeValidation timeValidation = new TimeValidation( ts, trxName);
/*	                
	                DB.executeUpdate("Delete from P_Payment_Gain_Distribution where P_Payment_Gain_ID in ( select P_Payment_Gain_ID From P_Payment_Gain where P_Payment_ID = " + payment.getP_Payment_ID() + ")", trxName);
	
					timeValidation.Create_PaymentGainDistribution(payment, 0);
	            	//2019-07-08

					payment.createEntryLine( trxName );
*/					
	                DB.executeUpdate("Delete from P_Payment_Gain_Distribution_360 where P_Payment_Gain_ID in ( select P_Payment_Gain_ID From P_Payment_Gain where P_Payment_ID = " + payment.getP_Payment_ID() + ")", trxName);
					timeValidation.Create_PaymentGainDistribution_360(payment, 0);
	        		payment.createEntryLine360( trxName );
					
	        		payment.save( trxName );
					
	        		
	      			boolean IsError   = P_Time_Sheet.CheckError( m_header.getTimeSheet().getP_Time_Sheet_ID(), trxName );
	    			boolean IsWarning = P_Time_Sheet.CheckWarning( m_header.getTimeSheet().getP_Time_Sheet_ID(), trxName );
	    			
	    			if ( IsError ) 
	    				m_header.getTimeSheet().setTimeSheetStatus(P_Time_Sheet.TIMESHEETSTATUS_Error);

	    			m_header.getTimeSheet().setIsError( IsError );
	    			m_header.getTimeSheet().setIsWarning( IsWarning );
	    			m_header.getTimeSheet().save( );
	        		  
	            }
			}
    	}
    	catch( Exception e ) {
    		s_log.log( Level.SEVERE, "echec sauvegarde", e );
    		return false;
    	}
    	return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */

    //Save a detail Row
    private void SaveDetail(DetailModel model, DetailLigne ligne, String trxName)
    {
        int periodID = m_lookup.lookupPeriod( ligne.getPeriod() );
        if (periodID == -1) return;

        //2023-07-10
        if ( m_header.getTimeSheet().getSheetType().equals( P_Time_Sheet.SHEETTYPE_HolidayPayment ))
        {
        	if ( periodID <= m_header.getTimeSheet().getP_Period_ID() )
            {
//    			JOptionPane.showMessageDialog(null, ""," warning",JOptionPane.WARNING_MESSAGE);
    			periodID = periodID +2; 
    			P_Period l_p = P_Period.get( Env.getCtx(), periodID, trxName);
        		int response = JOptionPane.showConfirmDialog(new Frame(""),
        				"La période de paie doit être dans le futur pour une paie de vacance, la période va être modifiée pour " + l_p.getName(),
        				"Paie de vacances",
    					JOptionPane.OK_CANCEL_OPTION,
    					JOptionPane.WARNING_MESSAGE);
        		
                if( response != JOptionPane.YES_OPTION)
                {
                	return;
                }

    			
            }

        }

        int assignmentID = m_lookup.lookupAssignment( ligne.getAssignment() );

        int gainID = m_lookup.lookupGain( ligne.getGain() );
        if( gainID == -1 ) return;
        
        int scheduleID = m_lookup.lookupSchedule( ligne.getSchedule() );
        //2023-08-23
        if( scheduleID == -1 ) scheduleID = 100000;
        	//        	return;
        
//        int scheduleID = ((LookupSchedule)m_lookup.getScheduleModel()).getDefaultId();
        
        
//        int scheduleID = ligne.getSchedule();
        int activityID = m_lookup.lookupActivity( ligne.getDistribution() );
        
        GregorianCalendar cal = new GregorianCalendar();
        P_Period period = P_Period.get( Env.getCtx(), periodID, trxName );
        long dateInitiale = period.getStartDate().getTime();

        if( assignmentID == -1 ) 
        {
	        cal.setTimeInMillis( dateInitiale );
	        //cal.add( Calendar.DAY_OF_YEAR, (ligne.getWeek()-1)*7 );
	        cal.add( Calendar.DAY_OF_YEAR, ((ligne.getWeek()-1)*7)+6 );
	        //int lastDayOfWeek = ((ligne.getWeek()-1)*7)+7;
	        // --------- Pgi Solstice 14-04-1008
//	        String param = PgiUtil.getSolsticeParameter(Env.getCtx(), "LastDayOfWeek");
//			cal.set( Calendar.DAY_OF_WEEK, Calendar.SATURDAY );
	        //cal.set( Calendar.DAY_OF_WEEK, Integer.parseInt(param) );
	        //cal.set( Calendar.DAY_OF_WEEK, lastDayOfWeek );
	        // -------------------------------------
			Timestamp ts = new Timestamp(cal.getTimeInMillis());
        	assignmentID = getEmployeeAssignment(m_header.getTimeSheet().getP_Employee_ID(), ts , trxName);
        	
        }
        if( assignmentID == -1 ) return;

		// Ligne a 0 pour methodGainID 107 ou 113
		if( m_lookup.isGainSansValeur(ligne.getGain())  ) 
		{
			// On supprime toutes les quantitées de temps
	    	for( Iterator iter = ligne.getAllIdQty().iterator(); iter.hasNext(); ) 
	    	{
				DetailLigne.IdQty idQty = (DetailLigne.IdQty)iter.next();
				if( idQty.getId() != -1 ) 
				{
					P_Time_Sheet_Detail detail = new P_Time_Sheet_Detail( Env.getCtx(), idQty.getId(), trxName );
					detail.delete(true);
				}
	    	}
	    	
			// On supprime la quantité monétaire
			if( ligne.getQuantityId() != -1 ) 
			{
		        P_Time_Sheet_Detail detail = new P_Time_Sheet_Detail( Env.getCtx(), ligne.getQuantityId(), trxName );
		        detail.delete(true);
			}
	    	
	        P_Time_Sheet_Detail detail = new P_Time_Sheet_Detail( Env.getCtx(), ligne.getSansValeurId(), trxName );
	        detail.setC_Activity_ID(activityID);

	        P_Assignment assignment = P_Assignment.get( Env.getCtx(), assignmentID, trxName );

	        detail.setP_Assignment_ID( assignmentID );
	        detail.setP_Gain_ID( gainID );
	        detail.setP_Period_ID( periodID );
	        if ( scheduleID != 0)
	        	detail.setP_Schedule_ID( scheduleID );
	        else
	        	detail.setP_Schedule_ID( 100000 ); // valeur par default
	        detail.setP_Time_Sheet_ID( m_header.getTimeSheetID() );
	        detail.setWeekIndex( ligne.getWeek() );
        	detail.setDayQty( Env.ZERO );
			
	        cal.setTimeInMillis( dateInitiale );
	       // cal.add( Calendar.DAY_OF_YEAR, (ligne.getWeek()-1)*7 );
	        cal.add( Calendar.DAY_OF_YEAR, ((ligne.getWeek()-1)*7)+6 );
	        //int lastDayOfWeek = ((ligne.getWeek()-1)*7)+7;
	     // --------- Pgi Solstice 14-04-1008
	       // String param = PgiUtil.getSolsticeParameter(Env.getCtx(), "LastDayOfWeek");
			//cal.set( Calendar.DAY_OF_WEEK, Calendar.SATURDAY );
	        //cal.set( Calendar.DAY_OF_WEEK, Integer.parseInt(param) );
	        //cal.set( Calendar.DAY_OF_WEEK, lastDayOfWeek );
	        // -------------------------------------

			Timestamp ts = new Timestamp(cal.getTimeInMillis());
			// Détecter si l'assignation commence après le samedi...
			if( ts.before(assignment.getStartDate()) ) ts = assignment.getStartDate();
			
			detail.setDay( ts );
			P_Period Period = P_Period.get( Env.getCtx(), periodID, trxName );
			
//+ 2009.09.20

			if ( ligne.getStartDate() != null )
			{
				String date = (String)ligne.getStartDate();
				int year  = Integer.parseInt( date.substring(0,4) );
				int month = Integer.parseInt( date.substring(5,7) );
				int day   = Integer.parseInt( date.substring(8,10) );
				detail.setStartDate(  TimeUtil.getDay(year, month, day));

				date = (String)ligne.getEndDate();
				year  = Integer.parseInt( date.substring(0,4) );
				month = Integer.parseInt( date.substring(5,7) );
				day   = Integer.parseInt( date.substring(8,10) );

				detail.setEndDate( TimeUtil.getDay(year, month, day) );
			}
/*			if ( detail.getStartDate() != null )
			{
				detail.setStartDate(  detail.getStartDate() );
				detail.setEndDate(  detail.getEndDate() );
			}
*/
	//- 2009.09.20-
			else
			{
				//TODO faire une formule mathématique...
				if (ligne.getWeek() == 1)
				{
					detail.setStartDate( Period.getStartDate());
					detail.setEndDate( TimeUtil.addDays( Period.getEndDate(), -7)  );
				}
				else
				{
					detail.setStartDate(  TimeUtil.addDays( Period.getStartDate(), +7) );
					detail.setEndDate( Period.getEndDate() );
				}
				
			}
			
			//2008-04-30
			if( ligne.getOrg() == null ) 
				detail.setOriginTime( "PER" );
			else 
				detail.setOriginTime( ligne.getOrg() );

			detail.save();
		}
		else {
			if( m_lookup.isGainMonetaire(ligne.getGain()) ) {
				// On supprime la quantité sans valeur
				P_Gain Gain = P_Gain.get( Env.getCtx(), gainID, null);
				if( ligne.getSansValeurId() != -1 &&  Gain.getP_Method_Gain_ID () != 300 ) {
			        P_Time_Sheet_Detail detail = new P_Time_Sheet_Detail( Env.getCtx(), ligne.getSansValeurId(), trxName );
			        detail.delete(true);
				}
				
				// On supprime toutes les quantitées de temps
		    	for( Iterator iter = ligne.getAllIdQty().iterator(); iter.hasNext(); ) {
					DetailLigne.IdQty idQty = (DetailLigne.IdQty)iter.next();
					if( idQty.getId() != -1 &&  Gain.getP_Method_Gain_ID () != 300 ) {
						P_Time_Sheet_Detail detail = new P_Time_Sheet_Detail( Env.getCtx(), idQty.getId(), trxName );
						detail.delete(true);
					}
		    	}

		    	if( (ligne.getQuantity() != null &&  Env.ZERO.compareTo(ligne.getQuantity()) != 0 ) ||  Gain.getP_Method_Gain_ID () == 300 ) {
			    	// On sauvegarde la quantité monétaire
			        P_Time_Sheet_Detail detail = new P_Time_Sheet_Detail( Env.getCtx(), ligne.getQuantityId(), trxName );
			        detail.setC_Activity_ID(activityID);
			        
			        detail.setP_Assignment_ID( assignmentID );
			        detail.setP_Gain_ID( gainID );
			        detail.setP_Period_ID( periodID );
			        if ( scheduleID != 0)
			        	detail.setP_Schedule_ID( scheduleID );
			        else
			        	detail.setP_Schedule_ID( 100000 ); // valeur par default
			        detail.setP_Time_Sheet_ID( m_header.getTimeSheetID() );
			        detail.setWeekIndex( ligne.getWeek() );
	
					detail.setDayQty( ligne.getQuantity() );
			        cal.setTimeInMillis( dateInitiale );
			        //int lastDayOfWeek = ((ligne.getWeek()-1)*7)+7;
			        //cal.add( Calendar.DAY_OF_YEAR, (ligne.getWeek())*7 );
			        cal.add( Calendar.DAY_OF_YEAR, ((ligne.getWeek()-1)*7)+6 );
			        // TODO déterminer le bon denier jour de la semaine
			        // --- Pgi Solstice 14-04-2008
			        //String param = PgiUtil.getSolsticeParameter(Env.getCtx(), "LastDayOfWeek");
					//cal.set( Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY ); // Calendar.SATURDAY
					//cal.set( Calendar.DAY_OF_WEEK, Integer.parseInt(param) ); // Calendar.SATURDAY
					//cal.set( Calendar.DAY_OF_WEEK, lastDayOfWeek); 
					// ------------------------------
			        if ( ligne.getStartDate() != null)
			        {
						String date = (String)ligne.getStartDate();
						int year  = Integer.parseInt( date.substring(0,4) );
						int month = Integer.parseInt( date.substring(5,7) );
						int day   = Integer.parseInt( date.substring(8,10) );
			        	detail.setDay(  TimeUtil.getDay(year, month, day) );
			        	
			        }
			        else
			        	detail.setDay( new Timestamp(cal.getTimeInMillis()) );

			        detail.setStartDate(detail.getDay());
					detail.setEndDate(detail.getDay());
					
					if( ligne.getQuantityId() == -1) detail.setOriginTime( "PER" );
					
					detail.save();
		    	}
		    	else if( ligne.getQuantityId() != -1 ) {
		    		// On supprime la quantité à 0
			        P_Time_Sheet_Detail detail = new P_Time_Sheet_Detail( Env.getCtx(), ligne.getQuantityId(), trxName );
			        detail.delete(true);
		    	}
			}
			else {
				P_Gain Gain = P_Gain.get( Env.getCtx(), gainID, null);

				// On supprime la quantité sans valeur
				if( ligne.getSansValeurId() != -1 &&  Gain.getP_Method_Gain_ID () != 300 ) {
			        P_Time_Sheet_Detail detail = new P_Time_Sheet_Detail( Env.getCtx(), ligne.getSansValeurId(), trxName );
			        detail.delete(true);
				}
				
				// On supprime la quantité monétaire
				if( ligne.getQuantityId() != -1 &&  Gain.getP_Method_Gain_ID () != 300 ) {
			        P_Time_Sheet_Detail detail = new P_Time_Sheet_Detail( Env.getCtx(), ligne.getQuantityId(), trxName );
			        detail.delete(true);
				}
				
				// On sauvegarde toutes les quantités de temps
		    	for( Iterator iter = ligne.getAllIdQty().iterator(); iter.hasNext(); ) {
					DetailLigne.IdQty idQty = (DetailLigne.IdQty)iter.next();
			        
					if( ( ( idQty.getQty() != null && Env.ZERO.compareTo(idQty.getQty()) != 0 ) || Gain.getP_Method_Gain_ID () == 300 ) ) {
				        P_Time_Sheet_Detail detail = new P_Time_Sheet_Detail( Env.getCtx(), idQty.getId(), trxName );
				        detail.setC_Activity_ID(activityID);
				        
				        detail.setP_Assignment_ID( assignmentID );
				        detail.setP_Gain_ID( gainID );
				        detail.setP_Period_ID( periodID );
				        if ( scheduleID != 0)
				        	detail.setP_Schedule_ID( scheduleID );
				        else
				        	detail.setP_Schedule_ID( 100000 ); // valeur par default
				        detail.setP_Time_Sheet_ID( m_header.getTimeSheetID() );
				        detail.setWeekIndex( ligne.getWeek() );
	
				        cal.setTimeInMillis( dateInitiale );
				        cal.add( Calendar.DAY_OF_YEAR, (ligne.getWeek()-1)*7 );

				        int dayOfWeek = idQty.getDayOfWeek();
						cal.set( Calendar.DAY_OF_WEEK, dayOfWeek );
						
				        if( dayOfWeek < model.getPremierJour() ) {
				        	// Journée dans la semaine suivante... on offset...
				        	cal.add( Calendar.DAY_OF_MONTH, 7 );
				        }
				        
						detail.setDay( new Timestamp(cal.getTimeInMillis()) );
						detail.setStartDate(detail.getDay());
						detail.setEndDate(detail.getDay());

						detail.setDayQty( idQty.getQty() );
						
						if( idQty.getId() == -1) detail.setOriginTime( "PER" );
	
						detail.save();
					}
					else if( idQty.getId() != -1 ) {
						// On supprime les quantités a 0
				        P_Time_Sheet_Detail detail = new P_Time_Sheet_Detail( Env.getCtx(), idQty.getId(), trxName );
				        detail.delete(true);
					}
				}
			}
		}

    }

    private void SaveDetailSum(DetailModelSum model, DetailLigneSum ligne, String trxName)
    {
        int periodID = m_lookup.lookupPeriod( ligne.getPeriod() );
        if (periodID == -1) return;


        int assignmentID = m_lookup.lookupAssignment( ligne.getAssignment() );

        int gainID = m_lookup.lookupGain( ligne.getGain() );
        if( gainID == -1 ) return;
        
        int activityID = m_lookup.lookupActivity( ligne.getDistribution() );
        int orgID = m_lookup.lookupOrg( ligne.getOrganization());
       
        GregorianCalendar cal = new GregorianCalendar();
        P_Period period = P_Period.get( Env.getCtx(), periodID, trxName );
        long dateInitiale = period.getStartDate().getTime();

        if( assignmentID == -1 ) 
        {
	        cal.setTimeInMillis( dateInitiale );
	        //cal.add( Calendar.DAY_OF_YEAR, (ligne.getWeek()-1)*7 );
	        cal.add( Calendar.DAY_OF_YEAR, ((ligne.getWeek()-1)*7)+6 );
	        //int lastDayOfWeek = ((ligne.getWeek()-1)*7)+7;
	     // --------- Pgi Solstice 14-04-1008
	        //String param = PgiUtil.getSolsticeParameter(Env.getCtx(), "LastDayOfWeek");
			//cal.set( Calendar.DAY_OF_WEEK, Calendar.SATURDAY );
	        //cal.set( Calendar.DAY_OF_WEEK, Integer.parseInt(param) );
	        //cal.set( Calendar.DAY_OF_WEEK, lastDayOfWeek );
	        // -------------------------------------
			Timestamp ts = new Timestamp(cal.getTimeInMillis());
        	assignmentID = getEmployeeAssignment(m_header.getTimeSheet().getP_Employee_ID(), ts , trxName);
        	
        }
        if( assignmentID == -1 ) return;

		// Ligne a 0 pour methodGainID 107 ou 113
//		if( m_lookup.isGainSansValeur(ligne.getGain()) ) 
//		{

			// On supprime toutes les quantitées de temps
	    	for( Iterator iter = ligne.getAllIdQty().iterator(); iter.hasNext(); ) 
	    	{
				DetailLigneSum.IdQty idQty = (DetailLigneSum.IdQty)iter.next();
				if( idQty.getId() != -1 ) 
				{
					P_Time_Sheet_Detail detail = new P_Time_Sheet_Detail( Env.getCtx(), idQty.getId(), trxName );
					detail.delete(true);
				}
	    	}
	    	
			// On supprime la quantité monétaire
			if( ligne.getQuantityId() != -1 ) 
			{
		        P_Time_Sheet_Detail detail = new P_Time_Sheet_Detail( Env.getCtx(), ligne.getQuantityId(), trxName );
		        detail.delete(true);
			}
	    	
	        P_Time_Sheet_Detail detail = new P_Time_Sheet_Detail( Env.getCtx(), ligne.getSansValeurId(), trxName );
	        detail.setC_Activity_ID(activityID);

	        P_Assignment assignment = P_Assignment.get( Env.getCtx(), assignmentID, trxName );

	        detail.setP_Assignment_ID( assignmentID );
	        detail.setP_Gain_ID( gainID );
	        detail.setP_Period_ID( periodID );
	        detail.setP_Time_Sheet_ID( m_header.getTimeSheetID() );
	        detail.setWeekIndex( ligne.getWeek() );
	        // ------------ Pgi Solstice 14-04-2008
	        if (m_lookup.isHourlyRateSansValeur(ligne.getGain()))
	        	detail.setHourly_Rate(null);
	        else
	        	detail.setHourly_Rate(this.safeBigDecimal(ligne.getHourlyRate()));
	        // ------------------------------------

//	        detail.setAD_Org_ID(m_lookup.lookupOrg(ligne.getOrganization()));
//	        detail.setC_SalesRegion_ID(m_lookup.lookupSalesRegion(ligne.getSalesRegion()));

	        //	      Ajout			      

			try
			{			        
				BigDecimal total = this.safeBigDecimal(ligne.getHourlyRate());
				detail.setHourly_Rate(total);
			}
			catch (Exception e)
			{
				s_log.log( Level.SEVERE, "echec sauvegarde", e );
			}

     
			detail.setDayQty( Env.ZERO );
			
	        cal.setTimeInMillis( dateInitiale );
	        //cal.add( Calendar.DAY_OF_YEAR, (ligne.getWeek()-1)*7 );
	        cal.add( Calendar.DAY_OF_YEAR, ((ligne.getWeek()-1)*7)+6 );
	        //int lastDayOfWeek = ((ligne.getWeek()-1)*7)+7;
	     // --------- Pgi Solstice 14-04-1008
	     //   String param = PgiUtil.getSolsticeParameter(Env.getCtx(), "LastDayOfWeek");
			//cal.set( Calendar.DAY_OF_WEEK, Calendar.SATURDAY );
	        //cal.set( Calendar.DAY_OF_WEEK, Integer.parseInt(param) );
	        //cal.set( Calendar.DAY_OF_WEEK, lastDayOfWeek );
	        // -------------------------------------			
			Timestamp ts = new Timestamp(cal.getTimeInMillis());
			// Détecter si l'assignation commence après le samedi...
			
			if( ts.before(assignment.getStartDate()) ) ts = assignment.getStartDate();
			
			// Stephane Morin 17 septembre 2007
			// Pour tout de suite ça me cause des problèmes avec Helico
			//  On va dans un prmier temps conserver ce qui est prévu à la feuille de temps
			
			// detail.setDay( ts );
			
			P_Period Period = P_Period.get( Env.getCtx(), periodID, trxName );
			
			
			if ( detail.getStartDate() == null )
			{
				detail.setDay( Period.getStartDate());
			}	
			

			// Stephane Morin 17 septembre 2007
			
			if ( ligne.getStartDate() != null )
			{
				String date = (String)ligne.getStartDate();
				int year  = Integer.parseInt( date.substring(0,4) );
				int month = Integer.parseInt( date.substring(5,7) );
				int day   = Integer.parseInt( date.substring(8,10) );
				detail.setStartDate(  TimeUtil.getDay(year, month, day));

				date = (String)ligne.getEndDate();
				year  = Integer.parseInt( date.substring(0,4) );
				month = Integer.parseInt( date.substring(5,7) );
				day   = Integer.parseInt( date.substring(8,10) );

				detail.setEndDate( TimeUtil.getDay(year, month, day) );
			}
			else
			{
				detail.setStartDate( Period.getStartDate());
				detail.setEndDate( Period.getEndDate() );
			}	

//			if ( detail.getDay() == null)
			detail.setDay( detail.getStartDate());

			detail.setDayQty(ligne.getQuantity());
	        BigDecimal total = this.safeBigDecimal(ligne.getHourlyRate());
	        detail.setHourly_Rate(total);
			
//			if( ligne.getQuantityId() == -1) 
			if(	ligne.getOrg() == null )
				detail.setOriginTime( "PER" );
			else
				detail.setOriginTime( ligne.getOrg() );

			detail.setOrg_ID( orgID );
			detail.setP_Employee_LongTermLeave_ID(ligne.getP_Employee_LongTermLeave_ID());

			detail.save();
    }
   //Cette méthode retourne un object en BigDecimal utiliser par SaveSum 
	    private BigDecimal safeBigDecimal( Object value ) {
	    	if( value instanceof BigDecimal ) return (BigDecimal)value;
	    	if( value instanceof String ) {
	    		if( "".equals(value) ) return new BigDecimal(0);
	    		return new BigDecimal((String)value);
	    	}
	    	return null;
	    }
	    
	//
	// Cette méthode retourne l'affectation active a une date, la plus récente.
	// Possible qu'un employé est 2 ou 3 affectation active en même temps, 
	// mais la plus récente sera généré sur la feuille de temps.
	//
	private int getEmployeeAssignment(int Employee_ID, Timestamp effectIn, String trxName)
	{
		String sql = null;
		sql = "Select P_Assignment.P_Assignment_ID "; 
		sql += " From P_Assignment, P_Assignment_Param ";
		sql += " WHERE P_Assignment.IsActive='Y' ";
		sql += " AND P_Assignment_Param.IsActive='Y' ";
		sql += " AND P_Assignment_Param.P_Assignment_ID = P_Assignment.P_Assignment_ID";
		sql += " AND P_Assignment.P_Employee_ID=" + Employee_ID;
//		sql += " AND P_Assignment.AssignmentType = '" + P_Assignment.ASSIGNMENTTYPE_Other + "'";
		sql += " AND P_Assignment_Param.EffectIn <= " + DB.TO_DATE( effectIn );
		sql += " AND " + DB.TO_DATE( effectIn ) + " between isnull( P_Assignment.startdate, " + DB.TO_DATE( effectIn ) + ") and isnull( P_Assignment.enddate, "+ DB.TO_DATE( effectIn ) + ")";
		sql += " ORDER BY P_Assignment.StartDate Desc, P_Assignment.AssignmentType ";


		PreparedStatement pstmt = null;

		int iAssignment_ID = -1;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				iAssignment_ID = rs.getInt(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
//			Log.log(Level.SEVERE," getEmployeeAssignment - " + sql, e);
		}
			
		
		return iAssignment_ID;		
	}

    private void DeleteDetail(DetailModel model, DetailLigne ligne, String trxName )
    {
    	// On supprime la quantité monétaire
    	if( ligne.getQuantityId() != -1 ) {
    		P_Time_Sheet_Detail detail = new P_Time_Sheet_Detail( Env.getCtx(), ligne.getQuantityId(), trxName);
			detail.delete(true);
    	}
		// On supprime la quantité sans valeur
		if( ligne.getSansValeurId() != -1 ) {
	        P_Time_Sheet_Detail detail = new P_Time_Sheet_Detail( Env.getCtx(), ligne.getSansValeurId(), trxName );
	        detail.delete(true);
		}
		// On supprime les quantités de temps
    	for( Iterator iter = ligne.getAllIdQty().iterator(); iter.hasNext(); ) {
			DetailLigne.IdQty idQty = (DetailLigne.IdQty)iter.next();
			if( idQty.getId() != -1 ) {
	    		P_Time_Sheet_Detail detail = new P_Time_Sheet_Detail( Env.getCtx(), idQty.getId(), trxName);
				detail.delete(true);
			}
    	}
    }

    private void DeleteDetailSum(DetailModelSum model, DetailLigneSum ligne, String trxName )
    {
    	// On supprime la quantité monétaire
    	if( ligne.getQuantityId() != -1 ) {
    		P_Time_Sheet_Detail detail = new P_Time_Sheet_Detail( Env.getCtx(), ligne.getQuantityId(), trxName);
			detail.delete(true);
    	}
		// On supprime la quantité sans valeur
		if( ligne.getSansValeurId() != -1 ) {
	        P_Time_Sheet_Detail detail = new P_Time_Sheet_Detail( Env.getCtx(), ligne.getSansValeurId(), trxName );
	        detail.delete(true);
		}
		// On supprime les quantités de temps
    	for( Iterator iter = ligne.getAllIdQty().iterator(); iter.hasNext(); ) {
			DetailLigneSum.IdQty idQty = (DetailLigneSum.IdQty)iter.next();
			if( idQty.getId() != -1 ) {
	    		P_Time_Sheet_Detail detail = new P_Time_Sheet_Detail( Env.getCtx(), idQty.getId(), trxName);
				detail.delete(true);
			}
    	}
    }    
    
    public void SaveDeduction( DETDeductionModel model, String trxName ) throws Exception
    {
    	if( model == null ) return; // Rien a faire
		int id = -1;
    	for( int i=0; i<model.getRowCount(); i++ ) {
    		if( model.isDataRow(i) ) {
				if( model.getValueAt(i, DETDeductionModel.COL_ID) != null ) {
					id = ((Integer)model.getValueAt(i, DETDeductionModel.COL_ID)).intValue();
				}
				else id = -1;
				
    			if( model.isRowDeleted(i) ) {
    				if( id != -1 ) {
						P_Payment_Deduction pd = new P_Payment_Deduction( Env.getCtx(), id, trxName );
						if ( pd != null)
							pd.delete(true);
    				}
    			}
    			else {
    				if ( m_lookup.lookupDeduction(model.getValueAt(i, DETDeductionModel.COL_DEDUCTION)) > 0)
    				{
    					P_Payment_Deduction pd = new P_Payment_Deduction( Env.getCtx(), id, trxName );
    					pd.setP_Employee_ID( m_header.getCurrentEmployeeID() );
    					pd.setP_Payment_ID( m_header.getTimeSheet().getP_Payment_ID() );
    					pd.setP_Deduction_ID( m_lookup.lookupDeduction(model.getValueAt(i, DETDeductionModel.COL_DEDUCTION)) );
    					pd.setSalary_Eligible( (BigDecimal)model.getValueAt(i, DETDeductionModel.COL_SALARY_ADM) );
    					pd.setHours_Eligible( (BigDecimal)model.getValueAt(i, DETDeductionModel.COL_HOUR_ADM) );
    					pd.setEmployee_Part( (BigDecimal)model.getValueAt(i, DETDeductionModel.COL_EMPLOYEE_PART) );
    					pd.setEmployer_Part( (BigDecimal)model.getValueAt(i, DETDeductionModel.COL_EMPLOYER_PART) );
    					pd.setAccumulationAmount( (BigDecimal)model.getValueAt(i, DETDeductionModel.COL_ACCUMULATION) );
    					pd.setRetrievalAmount( (BigDecimal)model.getValueAt(i, DETDeductionModel.COL_REMBOURSEMENT) );
    					pd.setP_Period_ID( m_lookup.lookupPeriod(model.getValueAt(i, DETDeductionModel.COL_PERIOD)) );
    					pd.setOrigine( (String)model.getValueAt(i, DETDeductionModel.COL_ORIGINE) );

    					if ( pd.getP_Period_ID() == 0 || pd.getP_Period_ID() == -1)
    						pd.setP_Period_ID( m_header.getTimeSheet().getP_Period_ID());

    					P_Period Period = P_Period.get(Env.getCtx(), pd.getP_Period_ID(), trxName);
    					pd.setP_Year_ID(Period.getP_Year_ID());
    					P_Deduction_Param Deduction_Param = P_Deduction_Param.get(Env.getCtx(), pd.getP_Deduction_ID(), Period.getStartDate(), trxName);
    					pd.setP_Deduction_Param_ID(Deduction_Param.getP_Deduction_Param_ID());
    					
    					//+ 2013.04.02
    					//+ 2025-08-19 La déduction pour être en milieux de période.
//    					P_Employee_Deduction EmployeeDeduction = P_Employee_Deduction.get(Env.getCtx(), pd.getP_Employee_ID(), pd.getP_Deduction_ID(), Period.getStartDate(), trxName);
    					P_Employee_Deduction EmployeeDeduction = P_Employee_Deduction.get(Env.getCtx(), pd.getP_Employee_ID(), pd.getP_Deduction_ID(), Period.getEndDate(), trxName);
    					pd.setP_Employee_Deduction_ID( EmployeeDeduction.getP_Employee_Deduction_ID() );
    					//- 2013.04.02 
    					
    					
    					if ( pd.getSalary_Eligible() == null ) //|| pd.getSalary_Eligible().compareTo(Env.ZERO) == 0)
    					{
    						CalculNet cal = new CalculNet( Env.getCtx(), Period.getPayDate() , null );

    						cal.calculDeduction( pd, Deduction_Param, trxName);
//    						CalculNet.calculDeduction( pd, Deduction_Param, trxName);
    					}

    					pd.save();
    				}
    			}
    		}
    	}
    }
    
    public void SaveAvantage( DETTaxBenefitModel model, String trxName ) {
    	if( model == null ) return; // Rien a faire
		int id = -1;
    	for( int i=0; i<model.getRowCount(); i++ ) {
    		if( model.isDataRow(i) ) {
				if( model.getValueAt(i, DETTaxBenefitModel.COL_ID) != null ) {
					id = ((Integer)model.getValueAt(i, DETTaxBenefitModel.COL_ID)).intValue();
				}
				else id = -1;
				
    			if( model.isRowDeleted(i) ) {
    				if( id != -1 ) {
    					P_Payment_Taxable_Benefit tb = new P_Payment_Taxable_Benefit( Env.getCtx(), id, trxName );
    					tb.delete(true);
    				}
    			}
    			else {
    				if ( m_lookup.lookupTaxableBenefit(model.getValueAt(i, DETTaxBenefitModel.COL_BENEFIT)) > 0)
    				{
    					P_Payment_Taxable_Benefit tb = new P_Payment_Taxable_Benefit( Env.getCtx(), id, trxName );
    					tb.setP_Employee_ID( m_header.getCurrentEmployeeID() );
    					tb.setP_Payment_ID( m_header.getTimeSheet().getP_Payment_ID() );
    					tb.setP_Taxable_Benefit_ID( m_lookup.lookupTaxableBenefit(model.getValueAt(i, DETTaxBenefitModel.COL_BENEFIT)) );
    					tb.setP_Period_ID( m_lookup.lookupPeriod(model.getValueAt(i, DETTaxBenefitModel.COL_PERIOD)) );
    					tb.setTaxable_Benefit_Amount( (BigDecimal)model.getValueAt(i, DETTaxBenefitModel.COL_AMOUNT) );
    					tb.setOrigine( (String)model.getValueAt(i, DETTaxBenefitModel.COL_ORIGINE) );
    					if ( tb.getP_Period_ID() == 0 || tb.getP_Period_ID() == -1)
    						tb.setP_Period_ID( m_header.getTimeSheet().getP_Period_ID());
    					P_Period Period = P_Period.get(Env.getCtx(), tb.getP_Period_ID(), trxName);
    					tb.setP_Year_ID(Period.getP_Year_ID());
    					tb.save();
    				}
    			}
    		}
    	}
    }
    
    private boolean validationBanque( P_Credits_Movement movement, String trxName ) {
        P_Credits pc = P_Credits.get(Env.getCtx(), movement.getP_Credits_ID(), trxName);
        boolean ret = new Calcul_Credits(m_header.getTimeSheet(), trxName).CreditsAlert( movement, pc, 
												                movement.getPMovementDate(), 
												                movement.getPMovementVariation(), false );
        if( ret == true ) {
            ADialog.error( 0, this, "Le mouvement de banque " + pc.getValue() + " (" + movement.getPMovementVariation() + ") au " + movement.getPMovementDate() + " est invalide." );
        }
        return !ret;
    }
    
    public boolean SaveBanque( DETCreditsModel model, String trxName ) {
    	if( model == null ) return true; // Rien a faire
		int id = -1;
    	for( int i=0; i<model.getRowCount(); i++ ) {
    		if( model.isDataRow(i) ) {
				if( model.getValueAt(i, DETCreditsModel.COL_ID) != null ) {
					id = ((Integer)model.getValueAt(i, DETCreditsModel.COL_ID)).intValue();
				}
				else id = -1;
				
    			if( model.isRowDeleted(i) ) {
    				if( id != -1 ) {
    					P_Credits_Movement cm = new P_Credits_Movement( Env.getCtx(), id, trxName );
    					if ( cm.getPMovementOrigin().equals( "FTE") == false )
    						cm.delete(true);
    				}
    			}
    			else {
    				if ( model.getValueAt(i, DETCreditsModel.COL_DATE) != null )
    				{
    					if ( m_lookup.lookupCredits(model.getValueAt(i, DETCreditsModel.COL_CREDITS)) > 0)
    					{
//2011.11.08 Ne pas sauvegardé les mouvements qui provient de pandora.
    						if ( ((String)model.getValueAt(i, DETCreditsModel.COL_ORIGINE)) == null || ((String)model.getValueAt(i, DETCreditsModel.COL_ORIGINE)).equals( "FTE") == false )
    						{
            					P_Credits_Movement cm = new P_Credits_Movement( Env.getCtx(), id, trxName );
            					cm.setP_Employee_ID( m_header.getCurrentEmployeeID() );
            					cm.setP_Payment_ID( m_header.getTimeSheet().getP_Payment_ID() );
            					
            					cm.setP_Credits_ID( m_lookup.lookupCredits(model.getValueAt(i, DETCreditsModel.COL_CREDITS)) );
            					cm.setPMovementDate( new Timestamp(((Date)model.getValueAt(i, DETCreditsModel.COL_DATE)).getTime()) );
            					cm.setPMovementType( ((KeyNamePair)model.getValueAt(i, DETCreditsModel.COL_TYPE)).getName() );
            					cm.setDescription( (String)model.getValueAt(i, DETCreditsModel.COL_DESCRIPTION) );
            					if( id == -1 )
            					{
            						cm.setPMovementOrigin( "AJT" );
//            						cm.setPMovementType( "AJ" );
            					}
            					BigDecimal valInit = (BigDecimal)model.getValueAt(i, DETCreditsModel.COL_VALEUR_INIT);
            					BigDecimal valDelta = (BigDecimal)model.getValueAt(i, DETCreditsModel.COL_VARIATION);
            					if( valInit != null ) valDelta = valDelta.subtract(valInit);
            					if( valDelta.compareTo(BigDecimal.valueOf(0)) != 0 ) {
            						cm.setPMovementVariation( valDelta );
            						
            	    			    if( validationBanque(cm, trxName) == false ) return false;
            					}
            	    			    
            					cm.setPMovementVariation( ((BigDecimal)model.getValueAt(i, DETCreditsModel.COL_VARIATION)) );

             					cm.save();
    							
    						}
    						
    					}
    				}
    			}
    		}
    	}
    	return true;
    }
    
    //Set if the time detail is editable
    public void resetEditable() {
        if( m_header.m_main.isReadOnly() ) {
        	m_timeTable.setEnabled(false);
	    	m_advantageTable.setEnabled(false);
	    	m_bankTable.setEnabled(false);
	    	m_deductionTable.setEnabled(false);
        }
        else {
        	//2010.01.21 null pointeur problem
	    	String status = null; 
	    	if ( m_header.getTimeSheet() != null )	
	    		status = m_header.getTimeSheet().getTimeSheetStatus();
	    	if ( m_header.getTimeSheetID() <= 0)
	    	{
	        	m_timeTable.setEnabled(false);
		    	m_advantageTable.setEnabled(false);
		    	m_bankTable.setEnabled(false);
		    	m_deductionTable.setEnabled(false);
	    	}
	    	else if( status.equals(P_Time_Sheet.TIMESHEETSTATUS_Initial) ) {
//	        	m_timeTable.setEnabled(!m_header.isFeuilleAjustement());
	        	m_timeTable.setEnabled(true);
		    	m_advantageTable.setEnabled(false);
		    	m_bankTable.setEnabled(m_header.isFeuilleAjustement());
		    	m_deductionTable.setEnabled(false);
	        }
	        else if( status.equals(P_Time_Sheet.TIMESHEETSTATUS_Calculated) || status.equals(P_Time_Sheet.TIMESHEETSTATUS_Validated) ) 
	        {
	        	if ( m_header.isFeuilleAjustement() )
	        		m_timeTable.setEnabled(true);
	        	else
	        		m_timeTable.setEnabled(false);
	        		
		    	m_advantageTable.setEnabled(true);
		    	m_bankTable.setEnabled(true);
		    	m_deductionTable.setEnabled(true);
	        }
	        else if ( status.equals(P_Time_Sheet.TIMESHEETSTATUS_Error) && m_header.isFeuilleAjustement() )
	        {
        		m_timeTable.setEnabled(true);
		    	m_advantageTable.setEnabled(true);
		    	m_bankTable.setEnabled(true);
		    	m_deductionTable.setEnabled(true);
	        }
	        else {
	        	m_timeTable.setEnabled(false);
		    	m_advantageTable.setEnabled(false);
		    	m_bankTable.setEnabled(false);
		    	m_deductionTable.setEnabled(false);
	        }
        }
        
        m_cbCorrection.setVisible( m_timeTable.isEnabled() );
        m_btnCorrection.setVisible( m_timeTable.isEnabled() );
        m_cbCorrection2.setVisible( m_timeTable.isEnabled() );
        m_btnCorrection2.setVisible( m_timeTable.isEnabled() );
//        m_cbCorrection.setEnabled(true);
//        m_cbCorrection2.setEnabled(true);
    }

    /**
     * This method initializes jTable
     * 
     * @return javax.swing.JTable
     */
    private JTable getSummaryTable() {
        if (m_summaryTable == null) m_summaryTable = new JTable();
        return m_summaryTable;
    }

    /**
     * This method initializes jTable
     * 
     * @return javax.swing.JTable
     */
    private JTable getDeductionTable() {
        if (m_deductionTable == null) {
        	m_deductionTable = new JTable();
	        m_deductionTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        }
        return m_deductionTable;
    }

    /**
     * This method initializes jTable
     * 
     * @return javax.swing.JTable
     */
    private JTable getAdvantageTable() {
        if (m_advantageTable == null) {
        	m_advantageTable = new JTable();
	        m_advantageTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        }
        return m_advantageTable;
    }

    /**
     * This method initializes jTable
     * 
     * @return javax.swing.JTable
     */
    private JTable getBankTable() {
        if (m_bankTable == null) {
        	m_bankTable = new JTable();
        	m_bankTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        }
        return m_bankTable;
    }

    /**
     * This method initializes jTable
     * 
     * @return javax.swing.JTable
     */
    private JTable getPrimeTable() {
        if (m_primeTable == null) m_primeTable = new JTable();
        return m_primeTable;
    }

    /**
     * This method initializes jTable
     * 
     * @return javax.swing.JTable
     */
    private JTable getErrorTable() {
        if (m_errorTable == null) m_errorTable = new JTable();
        return m_errorTable;
    }

    private boolean[] m_loaded;
    private void loadOnglet( int index ) {
    	if( m_loaded[index] == false ) {
    		m_loaded[index] = true;
	        switch( index ) {
		    	case TIME_TAB : 
		    	if (DisplayDetail == 0)
		    	{
		    		loadTimeSheetDetail(); 
		    	}
		    	else
		    	{	
		    		loadTimeSheetDetailSum(); 
		    	}
		    	break;

		    	case SUMMARY_TAB : loadSummary(); break;
		    	case DEDUCTION_TAB : loadPaymentDeduction(); break;
				case ADVANTAGE_TAB : loadTaxableBenefit(); break;
				case BANK_TAB : loadCreditsMovement(); break;
				case PRIME_TAB : loadPaymentGain106(); break;
				case ERROR_TAB : loadMessages(); break;
/*				case ENTRY_TAB : 
			    	if (DisplayDetail == 0)
			    	{
			    		 loadPaymentEntry(); 
			    	}
			    	else
			    	{	
			    		loadPaymentEntryHelico();
			    	}
			    	break;
*/			    					
				case ENTRY_TAB_360 : 
		    		 loadPaymentEntry360(); 
			    	break;				
	        }
	    }
    }  
    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    public void stateChanged(ChangeEvent e)
    {
    	loadOnglet( ((CTabbedPane)e.getSource()).getSelectedIndex() );
    }

    private int m_lastLoadedPeriodID = -1;

    public void loadDetailPanel()
    {
        int periodID = m_header.getSelectedPeriodID();
        P_Period period = P_Period.get(Env.getCtx(), periodID, null);

        if (periodID != m_lastLoadedPeriodID && period != null && period.getStartDate() != null && period.getEndDate() != null)
        {
            m_lookup.setPeriod(period);
            
            int numberOfWeek = new BigDecimal( TimeUtil.getDaysBetween(period.getStartDate(), period.getEndDate()) +1 ) .divide(new BigDecimal(7), 0, BigDecimal.ROUND_UP).intValue();
            m_lookup.setNumberOfWeek(numberOfWeek);
            
            m_lookup.setPeriodOrigin(period);
            m_lookup.setPeriodSubsequent(period);
            m_lastLoadedPeriodID = periodID;
        }

    	m_loaded = new boolean[m_tabs.getTabCount()];
    	for( int i=0; i<m_loaded.length; i++ ) m_loaded[i] = false;
        loadOnglet( m_tabs.getSelectedIndex() );
    }

    private void loadTimeSheetDetail()
    {
    	String trxName = null;
    	String sql =
    		"SELECT d.P_Time_Sheet_Detail_ID, d.P_Period_ID, d.WeekIndex, d.P_Assignment_ID, d.P_Gain_ID, " +
				   "d.P_Schedule_ID, d.Day, d.DayQty, d.C_Activity_ID, g.P_Method_Gain_ID, u.VALUE, OriginTime " +
				   //2009.08.20
				   " ,StartDate, EndDate, d.P_Employee_LongTermLeave_ID " +
			"FROM P_Time_Sheet_Detail d " +
			"INNER JOIN P_Gain g ON g.P_GAIN_ID = d.P_GAIN_ID " +
			"INNER JOIN P_UOM u ON u.P_UOM_ID = g.P_UOM_ID " +
			"WHERE P_Time_Sheet_ID = ? " +
			"ORDER BY d.P_Period_ID, d.WeekIndex, OriginTime, d.P_Assignment_ID, g.value, d.P_Schedule_ID, d.Day ";
//			"ORDER BY d.P_Period_ID, d.WeekIndex, d.P_Assignment_ID, d.P_Gain_ID, d.P_Schedule_ID, d.Day ";
        
		P_Period period = P_Period.get( Env.getCtx(), m_header.getSelectedPeriodID(), trxName);
//		DetailModel model = null;
//		if ( period != null)
//			model = new DetailModel(m_lookup, period);
        DetailModel model = new DetailModel(m_lookup, period);
        

        //Les champs quantité et total sont aligner a droit.
        model.setAlignment( new int[] { 13, 14 }, CLabel.RIGHT );

        PreparedStatement pstmt = null;
        ResultSet rs = null;
       try
        {
            KeyNamePair idNamePair = null;
            pstmt = DB.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, null);
            pstmt.setInt(1, m_header.getTimeSheetID());
            rs = pstmt.executeQuery();

            
            int periodGroup = -1;
            int weekGroup = -1;
            int assignmentGroup = -1;
            int gainGroup = -1;
            int activityGroup = -1;
            int scheduleGroup = -1;
            String origine = "";
          
         /**/   ArrayList<DetailLigne> aggLigne = new ArrayList<DetailLigne>();
            DetailLigne data = null;
            while (rs.next())
            {
            	idNamePair = m_lookup.lookupGain(rs.getInt(5));
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTimeInMillis( rs.getTimestamp(7).getTime() );

                // On réutilise la même ligne si periode, week, assignment, gain et schedule sont identiques
            	if( periodGroup != rs.getInt(2) || weekGroup != rs.getInt(3) || assignmentGroup != rs.getInt(4) || 
            		gainGroup != rs.getInt(5) || origine.compareTo( rs.getString(12)) != 0
            		|| activityGroup != rs.getInt("C_Activity_ID")
            		|| scheduleGroup != rs.getInt("P_Schedule_ID")
            		) 
            	{
            		
					aggLigne.clear();
					
            		data = new DetailLigne(model);
            		aggLigne.add( data );
                    model.addLigne(rs.getInt(3), data);
                    
                    periodGroup = rs.getInt(2);
            		weekGroup = rs.getInt(3);
					assignmentGroup = rs.getInt(4);
					gainGroup = rs.getInt(5);
					scheduleGroup = rs.getInt("P_Schedule_ID");
					origine = rs.getString(12);
					activityGroup = rs.getInt("C_Activity_ID");
            	}
            	else data = (DetailLigne)aggLigne.get(0);
            	
                int methodGainId = rs.getInt(10);
                String gainUnit = rs.getString(11);
                	
                // methodGainId != 107 && 
                if( methodGainId != 113 && methodGainId != 116  ) {
                	BigDecimal qte = null;
                	
                	if (m_lookup.isGainSansValeur(idNamePair))
                	{
                		if( rs.getBigDecimal(8) != null && rs.getBigDecimal(8).intValue() != 0)	
                			qte = rs.getBigDecimal(8).setScale(2, BigDecimal.ROUND_HALF_UP);
                	}
                	else {
                		if ( rs.getBigDecimal(8) != null)
                			qte = rs.getBigDecimal(8).setScale(2, BigDecimal.ROUND_HALF_UP);
                		else
                			qte = Env.ZERO;
                	}
	            	// Gain monétaire à mettre dans la colonne quantité
	                if( "M".equals(gainUnit) || "Q".equals(gainUnit) ) 
	                { 
	                	// Magie pour aggréger les lignes "identiques"
	                	// ie: Si on a plus qu'une valeur pour un jour de la semaine avec period, week, assignment, 
	                	// gain et schedule identique, on doit créer une nouvelle ligne pour ces données là
	                	if( data.getQuantityId() != -1 ) {
	                		boolean needNew = true;
	                		for( Iterator iter = aggLigne.iterator(); iter.hasNext(); ) {
	                			data = (DetailLigne)iter.next();
	                			if( data.getQuantityId() == -1 ) { needNew = false; break; }
	                		}
	                		if( needNew ) {
	                            model.addLigne(rs.getInt(3), data = new DetailLigne(model));
	                            aggLigne.add(data);
	                		}
	                	}
	                	
	                	data.setQuantity( qte );
	                	data.setQuantityId( rs.getInt(1) );
	                }
	                // Gain en temps à mettre dans la bonne journée
	                else {
	                	// Magie pour aggréger les lignes "identiques"
	                	// ie: Si on a plus qu'une valeur pour un jour de la semaine avec period, week, assignment, 
	                	// gain et schedule identique, on doit créer une nouvelle ligne pour ces données là
	                	if( data.getDayId(cal.get(Calendar.DAY_OF_WEEK)) != -1 ) {
	                		boolean needNew = true;
	                		for( Iterator iter = aggLigne.iterator(); iter.hasNext(); ) {
	                			data = (DetailLigne)iter.next();
	                			if( data.getDayId(cal.get(Calendar.DAY_OF_WEEK)) == -1 ) { needNew = false; break; }
	                		}
	                		if( needNew ) {
	                            model.addLigne(rs.getInt(3), data = new DetailLigne(model));
	                            aggLigne.add(data);
	                		}
	                	}
	                	
	                	data.setDayIdQty(cal.get(Calendar.DAY_OF_WEEK), rs.getInt(1), qte );
	                }
                }
                // methodGainID == 116 ou 113, on fait juste conserver le ID du détail
                else {
                	// Magie pour aggréger les lignes "identiques"
                	// ie: Si on a plus qu'une valeur pour un jour de la semaine avec period, week, assignment, 
                	// gain et schedule identique, on doit créer une nouvelle ligne pour ces données là
                	if( data.getSansValeurId() != -1 ) {
                		boolean needNew = true;
                		for( Iterator iter = aggLigne.iterator(); iter.hasNext(); ) {
                			data = (DetailLigne)iter.next();
                			if( data.getSansValeurId() == -1 ) { needNew = false; break; }
                		}
                		if( needNew ) {
                            model.addLigne(rs.getInt(3), data = new DetailLigne(model));
                            aggLigne.add(data);
                		}
                	}
                	
                	data.setSansValeurId( rs.getInt(1) );
                }
                
                data.setPeriod( m_lookup.lookupPeriod(rs.getInt(2)) );
                data.setWeek( rs.getInt(3) );
                data.setAssignment( m_lookup.lookupAssignment(rs.getInt(4)) );
                data.setGain( m_lookup.lookupGain(rs.getInt(5)) );
                data.setSchedule( m_lookup.lookupSchedule(rs.getInt(6)) );
                data.setDistribution( m_lookup.lookupActivity(rs.getInt(9)) );
                data.setOrg( rs.getString("OriginTime") );
                data.setP_Employee_LongTermLeave_ID( rs.getInt("P_Employee_LongTermLeave_ID"));

                //2009.09.20
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                if ( rs.getDate("StartDate") != null)
                	data.setStartDate(sdf.format(rs.getDate("StartDate")));
                if ( rs.getDate("EndDate") != null)
                	data.setEndDate(sdf.format(rs.getDate("EndDate")));      
                //2009.09.20

            }/**/
        }
        catch (Exception e)
        {
        	s_log.log(Level.SEVERE,"VEmployeeListPanel.loadTimeSheetDetail", e);
        }
        finally {
//        	DB.DB_CLOSE(rs);
//        	DB.DB_CLOSE(pstmt);
        }
   
        model.reorgAntecedant();
        
	    model.addTableModelListener( new TableModelListener() { 
            public void tableChanged(TableModelEvent e) {
                if( e.getType() == TableModelEvent.UPDATE ) m_header.m_main.enableSave( true ); 
            }
	    } );

        m_timeTable.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        
       m_timeTable.setModel(model);
        new DETableRenderer().bindTable( m_timeTable, model);
        
       model.setComboData(DetailLigne.COL_PERIOD, m_lookup.getPeriodModel() );
       model.setComboData(DetailLigne.COL_ASSIGNMENT, m_lookup.getAssignmentModel() );
       model.setComboData(DetailLigne.COL_GAIN, m_lookup.getGainModel() );
       model.setComboData(DetailLigne.COL_SCHEDULE, m_lookup.getScheduleModel() );
       model.setComboData(DetailLigne.COL_DISTRIBUTION, m_lookup.getActivityModel() );

    }

    private void loadTimeSheetDetailSum()
    {
    	String trxName = null;
    	String sql =
    		"SELECT d.P_Time_Sheet_Detail_ID, d.P_Period_ID, d.WeekIndex, d.P_Assignment_ID, d.P_Gain_ID, " +
				   "d.P_Schedule_ID, d.Day, d.DayQty, d.C_Activity_ID, g.P_Method_Gain_ID, u.VALUE, OriginTime, " +
			"d.StartDate, d.EndDate, d.Hourly_Rate,d.Org_ID, d.P_Employee_LongTermLeave_ID" + // , d.C_SalesRegion_ID 
		    " FROM P_Time_Sheet_Detail d " +
			"INNER JOIN P_Gain g ON g.P_GAIN_ID = d.P_GAIN_ID " +
			"INNER JOIN P_UOM u ON u.P_UOM_ID = g.P_UOM_ID " +
			"WHERE P_Time_Sheet_ID = ? " +
			"ORDER BY d.P_Period_ID, d.WeekIndex, d.P_Assignment_ID, g.value, d.P_Schedule_ID, d.Day ";
//			"ORDER BY d.P_Period_ID, d.WeekIndex, d.P_Assignment_ID, d.P_Gain_ID, d.P_Schedule_ID, d.Day ";
        
		P_Period periode = P_Period.get( Env.getCtx(), m_header.getSelectedPeriodID(), trxName);
        DetailModelSum model = new DetailModelSum(m_lookup, periode);
        

        // Les champs unité et  quantité  sont aligner a droit.
        model.setAlignment( new int[] {7, 8, 9}, CLabel.RIGHT );

        PreparedStatement pstmt = null;
        ResultSet rs = null;
       try
        {
            KeyNamePair idNamePair = null;
            pstmt = DB.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, trxName);
            pstmt.setInt(1, m_header.getTimeSheetID());
            rs = pstmt.executeQuery();

            int periodGroup = -1;
            int weekGroup = -1;
            int assignmentGroup = -1;
            int gainGroup = -1;
            int scheduleGroup = -1;
          
            ArrayList<DetailLigneSum> aggLigne = new ArrayList<DetailLigneSum>();
            DetailLigneSum data = null;
            while (rs.next())
            {
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTimeInMillis( rs.getTimestamp(7).getTime() );

                // On réutilise la même ligne si periode, week, assignment, gain et schedule sont identiques
            	/*if( periodGroup != rs.getInt(2) || weekGroup != rs.getInt(3) || assignmentGroup != rs.getInt(4) || 
            		gainGroup != rs.getInt(5) || scheduleGroup != rs.getInt(6) ) {*/
            		
					aggLigne.clear();
					
            		data = new DetailLigneSum(model);
            		aggLigne.add( data );
                    model.addLigne(rs.getInt(3), data);
                    
                    periodGroup = rs.getInt(2);
            		weekGroup = rs.getInt(3);
					assignmentGroup = rs.getInt(4);
					gainGroup = rs.getInt(5);
					scheduleGroup = rs.getInt(6);
            	/*}
            	else data = (DetailLigneSum)aggLigne.get(0);*/
            	
                int methodGainId = rs.getInt(10);
                String gainUnit = rs.getString(11);
                idNamePair = m_lookup.lookupGain(rs.getInt(5));

                // methodGainId != 107 &&
                
                if(  methodGainId != 113 && methodGainId != 116  )  // && methodGainId != 118 
                {
                	BigDecimal qte = null;
                	if (m_lookup.isGainSansValeur(idNamePair))
                	{	
                    	if ( rs.getBigDecimal(8) != null)
                    	{
                    		if (rs.getBigDecimal(8).intValue() != 0)
                    			qte = rs.getBigDecimal(8).setScale(2, BigDecimal.ROUND_HALF_UP);
                    	}
                	}
                	else
//+2012.01.09                		
                		qte = PgiUtil.nvl( rs.getBigDecimal(8)).setScale(2, BigDecimal.ROUND_HALF_UP);
//                		qte = rs.getBigDecimal(8).setScale(2, BigDecimal.ROUND_HALF_UP);
//-2012.01.09                		
                	//BigDecimal qte = Env.ZERO;
                	
	                   	
                	
	            	// Gain monétaire à mettre dans la colonne quantité
	                if( "M".equals(gainUnit) || "Q".equals(gainUnit) ) 
	                { 
	                	// Magie pour aggréger les lignes "identiques"
	                	// ie: Si on a plus qu'une valeur pour un jour de la semaine avec period, week, assignment, 
	                	// gain et schedule identique, on doit créer une nouvelle ligne pour ces données là
	                	if( data.getQuantityId() != -1 ) {
	                		boolean needNew = true;
	                		for( Iterator iter = aggLigne.iterator(); iter.hasNext(); ) {
	                			data = (DetailLigneSum)iter.next();
	                			if( data.getQuantityId() == -1 ) { needNew = false; break; }
	                		}
	                		if( needNew ) {
	                            model.addLigne(rs.getInt(3), data = new DetailLigneSum(model));
	                            aggLigne.add(data);
	                		}
	                	}
	                	
	                	data.setQuantity( qte );
	                	data.setQuantityId( rs.getInt(1) );
	                }
	                // Gain en temps à mettre dans la bonne journée
	                else {
	                	// Magie pour aggréger les lignes "identiques"
	                	// ie: Si on a plus qu'une valeur pour un jour de la semaine avec period, week, assignment, 
	                	// gain et schedule identique, on doit créer une nouvelle ligne pour ces données là
	                	data.setQuantity( qte );
	                	if( data.getDayId(cal.get(Calendar.DAY_OF_WEEK)) != -1 ) {
	                		boolean needNew = true;
	                		for( Iterator iter = aggLigne.iterator(); iter.hasNext(); ) {
	                			data = (DetailLigneSum)iter.next();
	                			if( data.getDayId(cal.get(Calendar.DAY_OF_WEEK)) == -1 ) { needNew = false; break; }
	                		}
	                		if( needNew ) {
	                            model.addLigne(rs.getInt(3), data = new DetailLigneSum(model));
	                            aggLigne.add(data);
	                		}
	                	}
	                	
	                	data.setDayIdQty(cal.get(Calendar.DAY_OF_WEEK), rs.getInt(1), qte );
	                }
                }
                // methodGainID == 107 ou 113, on fait juste conserver le ID du détail
                else {
                	// Magie pour aggréger les lignes "identiques"
                	// ie: Si on a plus qu'une valeur pour un jour de la semaine avec period, week, assignment, 
                	// gain et schedule identique, on doit créer une nouvelle ligne pour ces données là
                	if( data.getSansValeurId() != -1 ) {
                		boolean needNew = true;
                		for( Iterator iter = aggLigne.iterator(); iter.hasNext(); ) {
                			data = (DetailLigneSum)iter.next();
                			if( data.getSansValeurId() == -1 ) { needNew = false; break; }
                		}
                		if( needNew ) {
                            model.addLigne(rs.getInt(3), data = new DetailLigneSum(model));
                            aggLigne.add(data);
                		}
                	}
                	
                	data.setSansValeurId( rs.getInt(1) );
                }
                data.setPeriod( m_lookup.lookupPeriod(rs.getInt(2)) );
                data.setWeek( rs.getInt(3) );
                data.setAssignment( m_lookup.lookupAssignment(rs.getInt(4)) );
                data.setGain( m_lookup.lookupGain(rs.getInt(5)) );
                data.setDistribution( m_lookup.lookupActivity(rs.getInt(9)) );
                data.setOrg( rs.getString("OriginTime") );
                data.setP_Employee_LongTermLeave_ID( rs.getInt("P_Employee_LongTermLeave_ID"));

                data.setOrganization(m_lookup.lookupOrg(rs.getInt(16)));
//                data.setSalesRegion(m_lookup.lookupSalesRegion(rs.getInt(17)));

                //Afin d'éviter un bug il est important de faire le set de Hourly_Rate avant la date
              //  if (data.get)
                // ------------- Pgi Solstice 14-04-2008
                
                if (m_lookup.isGainSansValeur(data.getGain()))
                {
                	data.setHourlyRate(null);
                	data.setM_isSansValeur(true);
                }
                else	{
                	P_Gain Gain = P_Gain.get( Env.getCtx(), rs.getInt(5), null);
            		if ( Gain != null && Gain.getType_Rate() != null && Gain.getType_Rate().equals(  P_Gain.TYPE_RATE_FixedRate ) )
                    	data.setHourlyRate( Gain.getHourly_Rate() );
            		else
            			data.setHourlyRate(rs.getObject(15));

                }
                // ----------------------------------
               SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
               data.setStartDate(sdf.format(rs.getDate(13)));
               data.setEndDate(sdf.format(rs.getDate(14)));      
            }
        }
        catch (Exception e)
        {
        	s_log.log(Level.SEVERE,"VEmployeeListPanel.loadTimeSheetDetail", e);
        }
        finally {
//        	DB.DB_CLOSE(rs);
//        	DB.DB_CLOSE(pstmt);
        }
   
        model.reorgAntecedant();
        
	    model.addTableModelListener( new TableModelListener() { 
            public void tableChanged(TableModelEvent e) {
                if( e.getType() == TableModelEvent.UPDATE ) m_header.m_main.enableSave( true ); 
            }
	    } );

        m_timeTable.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        
        m_timeTable.setModel(model);
        new DETableRenderer().bindTable( m_timeTable, model);
        
        model.setComboData(DetailLigneSum.COL_PERIOD, m_lookup.getPeriodModel() );
        model.setComboData(DetailLigneSum.COL_ASSIGNMENT, m_lookup.getAssignmentModel() );
        model.setComboData(DetailLigneSum.COL_GAIN, m_lookup.getGainModel() );
        model.setComboData(DetailLigne.COL_SCHEDULE, m_lookup.getScheduleModel() );
        model.setComboData(DetailLigneSum.COL_DISTRIBUTION, m_lookup.getActivityModel() );

        model.setComboData(DetailLigneSum.COL_ORGANIZATION, m_lookup.getOrgModel());
//        model.setComboData(DetailLigneSum.COL_REGION, m_lookup.getSalesRegionModel());

    }
    
    
    private String lookupErrorLevel( String level ) {
        if( "W".equals(level) ) return Msg.translate(Env.getCtx(), "TIMESHEET_WARNING");
        if( "E".equals(level) ) return Msg.translate(Env.getCtx(), "TIMESHEET_ERROR");
        if( "I".equals(level) ) return Msg.translate(Env.getCtx(), "TIMESHEET_INFORMATION");
        return level;
    }
    private void loadMessages()
    {
        String sql = 
            "SELECT Alert_Severity_Level, CASE ALERT_MESSAGE WHEN NULL THEN adm.MSGTEXT ELSE ALERT_MESSAGE END " +
            "FROM P_time_Sheet_Error ptse LEFT OUTER JOIN AD_Message adm ON ptse.AD_MESSAGE_ID = adm.AD_MESSAGE_ID " + 
            "WHERE ptse.P_TIME_SHEET_ID = ? ";

        Vector<Object[]> data = new Vector<Object[]>();
        try
        {

            PreparedStatement pstmt = DB.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, null);
            pstmt.setInt(1, m_header.getTimeSheetID());
            ResultSet rs = pstmt.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount()+1;

            while (rs.next())
            {
                Object[] ligne = new Object[columnCount+1];
                ligne[0] = lookupErrorLevel(rs.getString(1));
                ligne[1] = rs.getString(2);
                ligne[2] = new Boolean(false);
                ligne[3] = rs.getString(1);

                data.add( ligne );
            }
            rs.close();
            pstmt.close();

        }
        catch (Exception e)
        {
            s_log.log(Level.SEVERE,"VTimeSheetDetail.loadMessages", e);
        }

        String[] columnNames = { 
			Msg.translate(Env.getCtx(), "Level" ),
			Msg.translate(Env.getCtx(), "Messages" ),
			""
        };
        
        DETableModel model = new DETableModel(data, columnNames, false) {
			public Class getColumnClass(int col) {
			    if( col == 2 ) return Boolean.class;
			    return super.getColumnClass(col);
			}
            public boolean isCellEditable(int row, int col) {
                if( col == 2 && "W".equals(getValueAt(row, 3)) ) return true;
                return super.isCellEditable(row, col);
            }
            public boolean isRowDeletable(int row) {
                if( "W".equals((String)super.getValueAt(row, 3)) == false ) return false;
                return super.isRowDeletable(row);
            }
		};
		
	    model.addTableModelListener( new TableModelListener() { 
            public void tableChanged(TableModelEvent e) {
                if( e.getType() == TableModelEvent.UPDATE ) m_header.m_main.enableSave( true ); 
            }
	    } );

        new DETableRenderer().bindTable(m_errorTable, model);
        m_errorTable.getColumnModel().getColumn(0).setMaxWidth(150);
        m_errorTable.getColumnModel().getColumn(2).setMaxWidth(20);
    }

    private void loadTaxableBenefit()
    {
        String sql = 
        	"select ptb.P_Taxable_Benefit_ID, isnull( pptb.TAXABLE_BENEFIT_AMOUNT, 0), pptb.P_Period_ID, " +
        			"pptb.P_Payment_Taxable_Benefit_ID, pptb.origine " +
			"from P_payment_taxable_benefit pptb " +
			"inner join P_taxable_benefit ptb on pptb.P_TAXABLE_BENEFIT_ID = ptb.P_TAXABLE_BENEFIT_ID " +
			"where P_payment_ID = ? Order By pptb.P_Period_ID, ptb.Value ";

        Vector<Object[]> data = new Vector<Object[]>();

        try
        {
            PreparedStatement pstmt = DB.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, null);
            pstmt.setInt(1, m_header.getTimeSheet().getP_Payment_ID());
            ResultSet rs = pstmt.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();

            while (rs.next())
            {
            	Object[] ligne = new Object[columnCount+2];
                ligne[DETTaxBenefitModel.COL_BENEFIT] = m_lookup.lookupTaxableBenefit(rs.getInt(1));
                ligne[DETTaxBenefitModel.COL_AMOUNT] = rs.getBigDecimal(2).setScale(2, BigDecimal.ROUND_HALF_UP);
                ligne[DETTaxBenefitModel.COL_PERIOD] = m_lookup.lookupPeriod(rs.getInt(3));
                ligne[DETTaxBenefitModel.COL_ID] = new Integer(rs.getInt(4));
                ligne[DETTaxBenefitModel.COL_ORIGINE] = rs.getString(5);

                data.add(ligne);
            }
            rs.close();
            pstmt.close();

        }
        catch (Exception e)
        {
            s_log.log(Level.SEVERE,"VTimeSheetDetail.loadTaxableBenefit", e);
        }

        DETTaxBenefitModel model = new DETTaxBenefitModel( 
        		data, 
        		(LookupTaxableBenefit)m_lookup.getTaxableBenefitModel(),
				(LookupPeriod)m_lookup.getPeriodModel() );
        
	    model.addTableModelListener( new TableModelListener() { 
            public void tableChanged(TableModelEvent e) {
                if( e.getType() == TableModelEvent.UPDATE ) m_header.m_main.enableSave( true ); 
            }
	    } );
	    
        new DETableRenderer().bindTable(m_advantageTable, model);
        
        TableColumnModel tcm = m_advantageTable.getColumnModel();
        tcm.getColumn(model.indexDeleteColumn()).setMaxWidth(20);
    }
    
    private void loadCreditsMovement()
    {

        String sql = 
        	"select pc.P_Credits_ID, pcm.PMOVEMENTDATE, pcm.PMOVEMENTTYPE,	pcm.PMOVEMENTVARIATION,	" +
        	"pcm.DESCRIPTION, pcm.P_Credits_Movement_ID, pu.VALUE, pcm.PMOVEMENTORIGIN, isnull( pcm.Amt_Reserve,0) Amt_Reserve "  
		  + "from 	P_Credits_Movement pcm	inner join P_credits pc 	on pcm.P_CREDITS_ID = pc.P_CREDITS_ID " 
		  + "INNER JOIN P_UOM pu ON pu.P_UOM_ID = pc.P_UOM_ID " 
		  + " where 	pcm.P_PAYMENT_ID = ? AND pcm.IsActive = 'Y' AND pc.IsActive = 'Y' " 
		  + "Order By pc.VALUE, pcm.PMOVEMENTDATE ";

        Vector<Object[]> data = new Vector<Object[]>();
        try
        {

            PreparedStatement pstmt = DB.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, null);
            pstmt.setInt(1, m_header.getTimeSheet().getP_Payment_ID());
            ResultSet rs = pstmt.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();

            while (rs.next())
            {
                Object[] ligne = new Object[columnCount+4];
                ligne[DETCreditsModel.COL_ORIGINE] = rs.getString(8);
            	ligne[DETCreditsModel.COL_CREDITS] = m_lookup.lookupCredits(rs.getInt(1));
            	ligne[DETCreditsModel.COL_DATE] = new Date(rs.getDate(2).getTime());
            	ligne[DETCreditsModel.COL_TYPE] = m_lookup.lookupVariationType(rs.getString(3));
            	ligne[DETCreditsModel.COL_VARIATION] = rs.getBigDecimal(4).setScale(2, BigDecimal.ROUND_HALF_UP);
            	ligne[DETCreditsModel.COL_VALEUR_INIT] = rs.getBigDecimal(4).setScale(2, BigDecimal.ROUND_HALF_UP);
            	ligne[DETCreditsModel.COL_DESCRIPTION] = rs.getString(5);
            	ligne[DETCreditsModel.COL_AMTRESERVE] = rs.getBigDecimal("Amt_Reserve").setScale(2, BigDecimal.ROUND_HALF_UP);
            	// ligne 6 = checkbox pour delete
            	ligne[DETCreditsModel.COL_ID] = new Integer(rs.getInt(6));
            	ligne[DETCreditsModel.COL_UNITS] = rs.getString(7);
                data.add(ligne);
            }
            rs.close();
            pstmt.close();

        }
        catch (Exception e)
        {
            s_log.log(Level.SEVERE,"VTimeSheetDetail.loadCreditsMovement", e);
        }
        //	}

        DETCreditsModel model = new DETCreditsModel(data, 
                (LookupCredits)m_lookup.getCreditsModel(),
//                (LookupCMDescription)m_lookup.getCMDescriptionModel(),
                (LookupVariationType)m_lookup.getVarationTypeModel() );
        
	    model.addTableModelListener( new TableModelListener() { 
            public void tableChanged(TableModelEvent e) {
                if( e.getType() == TableModelEvent.UPDATE ) m_header.m_main.enableSave( true ); 
            }
	    } );

        new DETableRenderer().bindTable(m_bankTable, model);
        m_bankTable.getColumnModel().getColumn(model.indexDeleteColumn()).setMaxWidth(20);
    }

    private void loadPaymentDeduction()
    {
        String sql = 
        	"select pd.P_Deduction_ID, 	ppd.SALARY_ELIGIBLE, 	" + 
			"		ppd.HOURS_ELIGIBLE,	ppd.EMPLOYEE_PART, 	ppd.EMPLOYER_PART, " +
			" 		isnull( ppd.AccumulationAmount, 0), isnull( ppd.RetrievalAmount,0), ppd.P_Period_ID, " +
			"		ppd.P_Payment_Deduction_ID, ppd.origine " +
			"from 	p_payment_deduction ppd 	" + 
			"inner join p_deduction pd 	on ppd.P_DEDUCTION_ID = pd.P_deduction_id " + 
			"where ppd.p_payment_id = ? Order By ppd.P_Period_ID , pd.Value";
        if(! language.isBaseLanguage())
        {
        	sql = 
            	"select pd.P_Deduction_ID, 	ppd.SALARY_ELIGIBLE, 	" + 
    			"		ppd.HOURS_ELIGIBLE,	ppd.EMPLOYEE_PART, 	ppd.EMPLOYER_PART, " +
    			" 		isnull( ppd.AccumulationAmount, 0), isnull( ppd.RetrievalAmount,0), ppd.P_Period_ID, " +
    			"		ppd.P_Payment_Deduction_ID, ppd.origine " +
    			"from 	p_payment_deduction ppd 	" + 
    			"inner join p_deduction pd 	on ppd.P_DEDUCTION_ID = pd.P_deduction_id " + 
    			"where ppd.p_payment_id = ? Order By ppd.P_Period_ID , pd.Value";
        }
        Vector<Object[]> data = new Vector<Object[]>();

        try
        {
            PreparedStatement pstmt = DB.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, null);
            pstmt.setInt(1,m_header.getTimeSheet().getP_Payment_ID());
            ResultSet rs = pstmt.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();

            while (rs.next())
            {
                BigDecimal val;
                Object[] ligne = new Object[columnCount+2];
                ligne[DETDeductionModel.COL_DEDUCTION] = m_lookup.lookupDeduction(rs.getInt(1));
                val = rs.getBigDecimal(2);
                ligne[DETDeductionModel.COL_SALARY_ADM] = val != null ? val.setScale(2, BigDecimal.ROUND_HALF_UP) : null;
                val = rs.getBigDecimal(3);
                ligne[DETDeductionModel.COL_HOUR_ADM] = val != null ? val.setScale(2, BigDecimal.ROUND_HALF_UP) : null;
                val = rs.getBigDecimal(4);
                ligne[DETDeductionModel.COL_EMPLOYEE_PART] = val != null ? val.setScale(2, BigDecimal.ROUND_HALF_UP) : null;
                val = rs.getBigDecimal(5);
                ligne[DETDeductionModel.COL_EMPLOYER_PART] = val != null ? val.setScale(2, BigDecimal.ROUND_HALF_UP) : null;
                val = rs.getBigDecimal(6);
                ligne[DETDeductionModel.COL_ACCUMULATION] = val != null ? val.setScale(2, BigDecimal.ROUND_HALF_UP) : null;
                val = rs.getBigDecimal(7);
                ligne[DETDeductionModel.COL_REMBOURSEMENT] = val != null ? val.setScale(2, BigDecimal.ROUND_HALF_UP) : null;
                ligne[DETDeductionModel.COL_PERIOD] = m_lookup.lookupPeriod(rs.getInt(8));
                ligne[DETDeductionModel.COL_ID] = new Integer(rs.getInt(9));
                ligne[DETDeductionModel.COL_ORIGINE] = rs.getString(10);
                
                data.add(ligne);
            }
            rs.close();
            pstmt.close();
        }
        catch (Exception e)
        {
            s_log.log(Level.SEVERE,"VTimeSheetDetail.loadPaymentDeduction", e);
        }
        
        DETDeductionModel model = new DETDeductionModel(
        		data, 
        		(LookupDeduction)m_lookup.getDeductionModel(), 
        		(LookupPeriod)m_lookup.getPeriodModel() );

        model.addTableModelListener( new TableModelListener() { 
            public void tableChanged(TableModelEvent e) {
                if( e.getType() == TableModelEvent.UPDATE ) m_header.m_main.enableSave( true ); 
            }
	    } );

        new DETableRenderer().bindTable(m_deductionTable, model);
        
        TableColumnModel tcm = m_deductionTable.getColumnModel();
        tcm.getColumn(model.indexDeleteColumn()).setMaxWidth(20);

    }

    private void loadPaymentGain106()
    {
    	String trxName = null;
        String sql = 
        	"select p_gain.Value + ' - ' + isnull( max( p_gain_trl.name), max( p_gain.NAME) ) Gain, isnull( p_payment_gain.hourly_rate, 0 ) hourly_rate, isnull( p_payment_gain.multiplyrate, 0 ) multiplyrate, max( p_uom.name ) uom, isnull( sum( p_payment_gain.quantitycalc ),0) quantitycalc, isnull( Sum( p_payment_gain.amountcalc ),0) amountcalc, P_Period_ID " +
			"from p_payment_gain  " +
			" inner join p_gain on p_payment_gain.P_GAIN_ID = p_gain.P_GAIN_ID " +
			" inner join p_gain_trl on p_gain_trl.P_GAIN_ID = p_gain.P_GAIN_ID " +
		    " inner join p_uom  on  p_gain.p_uom_id = p_uom.p_uom_id " +
			"where p_payment_gain.p_payment_id = ? " + 
			"and p_gain.P_Gain_Family_ID = 106 " +
			"Group By P_Period_ID, p_gain.Value, p_payment_gain.hourly_rate, p_payment_gain.multiplyrate " +
			"Order By P_Period_ID DESC, p_gain.Value" ;

//        System.out.println(" LoadPrimeDetail sql " + sql + " timeSheetID " + m_header.getTimeSheetID());
        
        Vector<Object[]> data = new Vector<Object[]>();

        try
        {

            PreparedStatement pstmt = DB.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, null);
            pstmt.setInt(1, m_header.getTimeSheet().getP_Payment_ID());
            ResultSet rs = pstmt.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();

            while (rs.next())
            {
                Object[] ligne = new Object[columnCount];
                ligne[0] = rs.getString(1);
                ligne[1] = rs.getBigDecimal(2).setScale(2, BigDecimal.ROUND_HALF_UP);
                ligne[2] = rs.getBigDecimal(3).setScale(2, BigDecimal.ROUND_HALF_UP);
                ligne[3] = rs.getString(4);
                ligne[4] = rs.getBigDecimal(5).setScale(2, BigDecimal.ROUND_HALF_UP);
                ligne[5] = rs.getBigDecimal(6).setScale(2, BigDecimal.ROUND_HALF_UP);
                P_Period period = P_Period.get( Env.getCtx(), rs.getInt(7), trxName);
                ligne[6] = period.getName();

                data.add(ligne);
            }
            rs.close();
            pstmt.close();

        }
        catch (Exception e)
        {
            s_log.log(Level.SEVERE,"VTimeSheetDetail.loadPaymentGain106", e);
        }

        String[] columnNames =
        { 
          Msg.translate(Env.getCtx(), "GainCode" ), 
          Msg.translate(Env.getCtx(), "HourlyRate"), 
          Msg.translate(Env.getCtx(), "Factor" ), 
          Msg.translate(Env.getCtx(), "Unit"), 
          Msg.translate(Env.getCtx(), "QuantityComputed"), 
          Msg.translate(Env.getCtx(), "AmountComputed"), 
          Msg.translate(Env.getCtx(), "Period") 
        };
        
		DETableModel model = new DETableModel(data, columnNames, false);
		model.setAlignment( new int[] {1, 2, 3, 4}, CLabel.RIGHT );
		new DETableRenderer().bindTable(m_primeTable, model);
    }

    private void loadSummary()
    {
        m_qteValue.setText( Env.ZERO.toString() );
        m_mntValue.setText( Env.ZERO.toString() );

    	String trxName = null;
		 String sql = 
		     "select pg.Value + ' - ' + pg.NAME Gain, isnull(ppg.hourly_rate,0),isnull(ppg.multiplyrate,0), uom.name, sum( isnull( ppg.quantitycalc, 0 ) ) quantitycalc, Sum( isnull( ppg.amountcalc, 0) ) amountcalc, P_Period_ID " +
		     "from p_payment_gain ppg , p_gain pg , p_uom uom  " +
		 	"where  ppg.P_GAIN_ID = pg.P_GAIN_ID and pg.p_uom_id = uom.p_uom_id and ppg.p_payment_id = ? " +
			    "Group By P_Period_ID, pg.Value, pg.NAME, ppg.hourly_rate,ppg.multiplyrate, uom.name " +
			    "Order By P_Period_ID , pg.Value";   	
        if(! language.isBaseLanguage())
        {
	            sql = 
	            "select pg.Value + ' - ' + pgt.NAME Gain, isnull(ppg.hourly_rate,0),isnull(ppg.multiplyrate,0), uom.name, sum( isnull( ppg.quantitycalc, 0 ) ) quantitycalc, Sum( isnull( ppg.amountcalc, 0) ) amountcalc, P_Period_ID " +
	            "from p_payment_gain ppg , p_gain_trl pgt , p_gain pg , p_uom uom  " +
	        	"where  ppg.P_GAIN_ID = pg.P_GAIN_ID and ppg.P_GAIN_ID = pgt.P_GAIN_ID  and pg.p_uom_id = uom.p_uom_id and ppg.p_payment_id = ? " +
	       	    "Group By P_Period_ID, pg.Value, pgt.NAME, ppg.hourly_rate,ppg.multiplyrate, uom.name " +
	       	    "Order By P_Period_ID , pg.Value";	
        }
	        
//    	"AND EXISTS( SELECT 1 FROM P_GAIN_GAININFO gg " +
//   	    "WHERE gg.P_GAIN_ID = ppg.P_GAIN_ID " +
//   	    "AND gg.P_GAININFO_ID = (SELECT P_GAININFO_ID FROM P_GAININFO WHERE VALUE = 'STD') ) " +

//        System.out.println(" LoadSummaryDetail sql " + sql + " timeSheetID " + m_header.getTimeSheetID());

        Vector<Object[]> data = new Vector<Object[]>();
 
        try
        {
            PreparedStatement pstmt = DB.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, null);
            pstmt.setInt(1, m_header.getTimeSheet().getP_Payment_ID());
            ResultSet rs = pstmt.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();

            while (rs.next())
            {
            	Object[] ligne = new Object[columnCount+1];
            	ligne[0+1] = rs.getString(1);
            	ligne[1+1] = rs.getBigDecimal(2).setScale(6, BigDecimal.ROUND_HALF_UP);
            	ligne[2+1] = rs.getBigDecimal(3).setScale(6, BigDecimal.ROUND_HALF_UP);
            	ligne[3+1] = Msg.translate(Env.getCtx(), rs.getString(4));
            	ligne[SUMMARY_QTE] = rs.getBigDecimal(5).setScale(2, BigDecimal.ROUND_HALF_UP);
            	ligne[SUMMARY_MNT] = rs.getBigDecimal(6).setScale(2, BigDecimal.ROUND_HALF_UP);
                P_Period period = P_Period.get( Env.getCtx(), rs.getInt(7), trxName);
                ligne[6+1] = period.getName();
                
                data.add(ligne);
            }
            rs.close();
            pstmt.close();

        }
        catch (Exception e)
        {
            s_log.log(Level.SEVERE,"VTimeSheetDetail.loadSummary", e);
        }

        String[] columnNames = { 
            "",
            Msg.translate(Env.getCtx(), "GainCode" ), 
            Msg.translate(Env.getCtx(), "HourlyRate"), 
            Msg.translate(Env.getCtx(), "Factor" ), 
            Msg.translate(Env.getCtx(), "Unit"), 
            Msg.translate(Env.getCtx(), "QuantityComputed"), 
            Msg.translate(Env.getCtx(), "AmountComputed"), 
            Msg.translate(Env.getCtx(), "Period") 
        };
        
        final DETableModel model = new DETableModel(data, columnNames, false) {
            public Class getColumnClass(int col) {
                if( col == 0 ) return Boolean.class;
                return super.getColumnClass(col);
            }
            public boolean isCellEditable(int row, int col) {
                if( col == 0 ) return true;
                return super.isCellEditable(row, col);
            }
            final int[] COL_WIDTHS = new int[] {20, 150, 150, 150, 150, 150, 150, 150 };
            public int[] getColumnWidths() { return COL_WIDTHS; }
            public boolean isDataRow(int row) { return true; }
            
            public void setValueAt(Object val, int row, int col) {
                super.setValueAt(val, row, col);
                if( col == 0 ) {
                    BigDecimal sumQte = new BigDecimal(0);
                    BigDecimal sumMnt = new BigDecimal(0);
                    for( int i=0; i<getRowCount(); i++ ) {
                        Boolean bool = (Boolean)getValueAt(i, 0);
                        if( bool != null && bool.booleanValue() ) {
                            sumQte = sumQte.add( (BigDecimal)getValueAt(i, SUMMARY_QTE) );
                            sumMnt = sumMnt.add( (BigDecimal)getValueAt(i, SUMMARY_MNT) );
                        }
                    }
                    m_qteValue.setText( sumQte.toString() );
                    m_mntValue.setText( sumMnt.toString() );
                }
            }
        };
        model.setAlignment( new int[] {2, 3, SUMMARY_QTE, SUMMARY_MNT}, CLabel.RIGHT );
        new DETableRenderer().bindTable(m_summaryTable, model);
        
        final TableCellRenderer defaultRenderer = m_summaryTable.getTableHeader().getDefaultRenderer();
        final JCheckBox cbHeader = new JCheckBox();
        m_summaryTable.getTableHeader().setDefaultRenderer( new TableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if( column == 0 ) return cbHeader;
                return defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        } );
        
        m_summaryTable.getTableHeader().addMouseListener( new MouseListener() { 
            public void mouseClicked(MouseEvent ev) {
                int col = m_summaryTable.getTableHeader().columnAtPoint(ev.getPoint());
                if( col == 0 ) {
                    cbHeader.setSelected(!cbHeader.isSelected());
                    Boolean bool = new Boolean(cbHeader.isSelected());
                    for( int i=0; i<model.getRowCount(); i++ ) {
                        model.setValueAt(bool,i,0);
                    }
                }
            }
            public void mouseEntered(MouseEvent ev) {}
            public void mouseExited(MouseEvent ev) {}
            public void mousePressed(MouseEvent ev) {}
            public void mouseReleased(MouseEvent ev) {}
        } );
    }
/*
    public void loadPaymentEntry()
    {

        String sql = "select CASE WHEN ppe.ORG_ID = 1000001 THEN '0100' ELSE '0200' END + ' ' + ce.value + ' - ' + isnull(ce.name , '')  as name, ca.value + ' - ' + ca.name, isnull(cast(sum (ppe.amtacctdr) as numeric(10,2)),0), isnull(cast(sum(ppe.amtacctcr) as numeric(10,2)),0), P_ExpenseCorrection_ID " + 
		"from p_payment_entryline ppe"
		+ " left outer join  c_elementvalue ce on ppe.c_elementvalue_id = ce.c_elementvalue_id"
		+ " left outer join c_activity ca on ppe.c_activity_id = ca.c_activity_id " 
		+ "where ppe.p_payment_id = ?  " 
		+ "group by ppe.ORG_ID, ce.value, ce.name, ca.value, ca.name, P_ExpenseCorrection_ID";

        Vector<Object[]> data = new Vector<Object[]>();
        try
        {

            PreparedStatement pstmt = DB.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, null);
            pstmt.setInt(1, m_header.getTimeSheet().getP_Payment_ID());
            ResultSet rs = pstmt.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();

            while (rs.next())
            {
                Object[] ligne = new Object[columnCount];
                ligne[0] = rs.getString(1);
                ligne[1] = rs.getString(2);
                ligne[2] = rs.getBigDecimal(3).setScale(2, BigDecimal.ROUND_HALF_UP);
                ligne[3] = rs.getBigDecimal(4).setScale(2, BigDecimal.ROUND_HALF_UP);
                if ( rs.getBigDecimal("P_ExpenseCorrection_ID") != null)
                    ligne[4] = "*";
                else
                    ligne[4] = " ";
                	
                
                data.add(ligne);
            }
            rs.close();
            pstmt.close();

        }
        catch (Exception e)
        {
            s_log.log(Level.SEVERE,"VTimeSheetDetail.loadPaymentEntry", e);
        }

        String[] columnNames =
        { 
          Msg.translate(Env.getCtx(), "Account" ), 
          Msg.translate(Env.getCtx(), "Activity" ), 
          Msg.translate(Env.getCtx(), "Debit" ), 
          Msg.translate(Env.getCtx(), "Credit" ),
          Msg.translate(Env.getCtx(), "*" )
        };
        
        DETableModel model = new DETableModel(data, columnNames, false);
        model.tagSummable( new int[] {2, 3} );
        model.setAlignment( new int[] {2, 3}, CLabel.RIGHT );
        new DETableRenderer().bindTable(m_paymentEntryTable, model);

        m_paymentEntryTable.getColumnModel().getColumn(4).setMaxWidth(5);

    }
*/
    public void loadPaymentEntry360()
    {

        String sql = "select ce.value + ' - ' + isnull(ce.name , '')  as name, ca.value360 + ' - ' + ca.name, isnull(cast(sum (ppe.amtacctdr) as numeric(10,2)),0), isnull(cast(sum(ppe.amtacctcr) as numeric(10,2)),0), P_ExpenseCorrection_ID , C_SalesRegion.value + ' - ' + C_SalesRegion.name as SalesRegion " + 
		" from p_payment_entryline_360 ppe"
		+ " left outer join  c_elementvalue ce on ppe.c_elementvalue_id = ce.c_elementvalue_id"
		+ " left outer join c_activity ca on ppe.c_activity_id = ca.c_activity_id " 
		+ " left outer join C_SalesRegion on C_SalesRegion.C_SalesRegion_ID = ppe.C_SalesRegion_ID "
		+ "where ppe.p_payment_id = ?  " 
		+ "group by ppe.ORG_ID, ce.value, ce.name, ca.value360, ca.name, P_ExpenseCorrection_ID, C_SalesRegion.value,  C_SalesRegion.name";

        Vector<Object[]> data = new Vector<Object[]>();
        try
        {

            PreparedStatement pstmt = DB.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, null);
            pstmt.setInt(1, m_header.getTimeSheet().getP_Payment_ID());
            ResultSet rs = pstmt.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();

            while (rs.next())
            {
                Object[] ligne = new Object[columnCount];
                ligne[0] = rs.getString("SalesRegion");
                ligne[1] = rs.getString(1);
                ligne[2] = rs.getString(2);
                ligne[3] = rs.getBigDecimal(3).setScale(2, BigDecimal.ROUND_HALF_UP);
                ligne[4] = rs.getBigDecimal(4).setScale(2, BigDecimal.ROUND_HALF_UP);
                if ( rs.getBigDecimal("P_ExpenseCorrection_ID") != null)
                    ligne[5] = "*";
                else
                    ligne[5] = " ";
                	
                
                data.add(ligne);
            }
            rs.close();
            pstmt.close();

        }
        catch (Exception e)
        {
            s_log.log(Level.SEVERE,"VTimeSheetDetail.loadPaymentEntry360", e);
        }

        String[] columnNames =
        { 
          Msg.translate(Env.getCtx(), "C_SalesRegion_ID" ), 
          Msg.translate(Env.getCtx(), "Account" ), 
          Msg.translate(Env.getCtx(), "Activity" ), 
          Msg.translate(Env.getCtx(), "Debit" ), 
          Msg.translate(Env.getCtx(), "Credit" ),
          Msg.translate(Env.getCtx(), "*" )
        };
        
        DETableModel model = new DETableModel(data, columnNames, false);
        model.tagSummable( new int[] {3, 4} );
        model.setAlignment( new int[] {3, 4}, CLabel.RIGHT );
        new DETableRenderer().bindTable(m_paymentEntryTable360, model);

        m_paymentEntryTable360.getColumnModel().getColumn(5).setMaxWidth(5);

    }

   

    /**
     * This method initializes jTable
     * 
     * @return javax.swing.JTable
     */
/*    
    private JTable getPaymentEntryTable()
    {
        if (m_paymentEntryTable == null) m_paymentEntryTable = new JTable();
        return m_paymentEntryTable;
    }
*/
    
    private JTable getPaymentEntryTable360()
    {
        if (m_paymentEntryTable360 == null) m_paymentEntryTable360 = new JTable();
        return m_paymentEntryTable360;
    }

    /**
     * Cette methode supprime une ligne de detail ou retourne FALSE
     * 
     * @return boolean
     */
    public int EntryTab_getSelectedTab()
    {
    	int iResult = -1;
    	iResult = getPaymentEntryTab().getSelectedIndex();
    	return iResult;
    }
    public boolean new_cmd()
    {
    	boolean bResult = false;
//    	System.out.println("New New New");
    	return bResult;
    }
    
    public void setDisplayDetail( String model)
    {
 //   	DisplayDetail = Integer.valueOf( model );
    }
    
} //  @jve:decl-index=0:visual-constraint="10,10"
