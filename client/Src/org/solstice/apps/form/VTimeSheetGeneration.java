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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JFrame;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.compiere.util.ASyncProcess;
import org.compiere.util.DB;
import org.compiere.util.Trx;
import org.compiere.util.ValueNamePair;
import org.compiere.swing.CComboBox;
import org.compiere.process.ProcessInfo;
import org.compiere.apps.ProcessCtl;
import org.compiere.apps.form.FormFrame;

import java.util.logging.*;
import org.compiere.util.*;

/**
 * @author frabou01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class VTimeSheetGeneration extends JFrame implements ActionListener,  ASyncProcess
{

	private javax.swing.JPanel jContentPane = null;

	private JPanel timeSheetPanel = null;
	private JButton btnCreate = null;
	private JButton btnClose = null;
	private JLabel lblEmployeeNumber = null;
	private JLabel lblName = null;

	private JLabel lblFrequency = null;
	private JLabel lblPaymentPeriod = null;
	private JLabel lblPayGroup = null;
	private JTextField txtEmployeeNumber = null;
	private CComboBox CbbEmployee = null;
	private CComboBox cbbFrequency = null;
	private CComboBox cbbPayPeriod = null;
	private CComboBox cbbPaymentGroup = null;
	
	ProcessInfo _pi;
	
	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (VTimeSheetGeneration.class);

	/**
	 * This is the default constructor
	 */
	public VTimeSheetGeneration(int WindowNo, FormFrame frame)
	{
		super();
		m_WindowNo = WindowNo;
		m_frame = frame; 
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		 _pi = new ProcessInfo ("", 2000010);  // HARDCODED    P_TimeGeneration
//		_pi.setAD_PInstance_ID (ProcessCtl.getInstanceID (m_WindowNo, _pi.getAD_Process_ID(), _pi.getRecord_ID()));
		if (_pi.getAD_PInstance_ID() == 0)
		{
//			info.setText(Msg.getMsg(Env.getCtx(), "ProcessNoInstance"));
			return;
		}
		this.setBounds(100, 100, 514, 261);
		this.setName("timeSheetGenFrame");
		this.setContentPane(getJContentPane());
		this.setTitle("Générer Feuille de Temps");
		
		//Load data
		loadFrequency();
		loadPaymentGroup();
		loadEmployee();
    }
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if(jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getTimeSheetPanel(), null);
		}
		return jContentPane;
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getTimeSheetPanel() {
		if (timeSheetPanel == null) {
			lblPayGroup = new JLabel();
			lblEmployeeNumber = new JLabel();
			lblName  = new JLabel();
			lblFrequency = new JLabel();
			lblPaymentPeriod = new JLabel();
			timeSheetPanel = new JPanel();
			timeSheetPanel.setLayout(null);
			timeSheetPanel.setBounds(3, 1, 504, 232);
			lblEmployeeNumber.setBounds(12, 12, 167, 18);
			lblEmployeeNumber.setText("Numéro d'identification");	
			lblFrequency.setBounds(12, 79, 167, 18);
			lblFrequency.setText("Périodicité");
			lblPaymentPeriod.setBounds(12, 114, 167, 18);
			lblName.setText("Nom,Prénom");
			lblName.setBounds(12, 43, 167, 18);			
			lblPaymentPeriod.setText("Période de Paie");
			lblPayGroup.setText("Groupement de paiement");
			lblPayGroup.setBounds(12, 150, 167, 18);
			lblName.setBounds(11, 46, 167, 16);
			timeSheetPanel.add(getBtnCreate(), null);
			timeSheetPanel.add(getBtnClose(), null);
			timeSheetPanel.add(lblEmployeeNumber, null);
			timeSheetPanel.add(lblName, null);
			timeSheetPanel.add(lblFrequency, null);
			timeSheetPanel.add(lblPaymentPeriod, null);
			timeSheetPanel.add(lblPayGroup, null);
			timeSheetPanel.add(getTxtEmployeeNumber(), null);
			timeSheetPanel.add(getCbbEmployee(), null);
			timeSheetPanel.add(getCbbFrequency(), null);
			timeSheetPanel.add(getCbbPayPeriod(), null);
			timeSheetPanel.add(getCbbGroupPay(), null);
		}
		return timeSheetPanel;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getBtnCreate() {
		if (btnCreate == null) {
			btnCreate = new JButton();
			btnCreate.setBounds(87, 188, 173, 33);
			btnCreate.setText("Créer");
			btnCreate.setActionCommand("Create");
			btnCreate.addActionListener(this);
		}
		return btnCreate;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getBtnClose() {
		if (btnClose == null) {
			btnClose = new JButton();
			btnClose.setBounds(267, 189, 173, 33);
			btnClose.setText("Fermer");
			btnClose.setActionCommand("Close");
			btnClose.addActionListener(this);
		}
		return btnClose;
	}
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getTxtEmployeeNumber() {
		if (txtEmployeeNumber == null) {
			txtEmployeeNumber = new JTextField();
			txtEmployeeNumber.setBounds(219, 9, 210, 20);
			txtEmployeeNumber.addActionListener(new java.awt.event.ActionListener() 
		    { 
				   public void actionPerformed(java.awt.event.ActionEvent e) 
				   {    

         				CbbEmployee.setSelectedIndex( 0 );

			    		String employeeQuery = "Select P_Employee_ID From P_Employee Where Value like '%" + txtEmployeeNumber.getText() +"' and IsActive = 'Y' Order by value " ;
		                PreparedStatement pstmt = DB.prepareStatement(employeeQuery, null);
		        		try
				        {
				       		int size = CbbEmployee.getItemCount();

				       		ResultSet rs = pstmt.executeQuery();
			                if(rs.next())
			                {
			                	// i = 0 of CbbEmployee = null 
			            		for (int i = 1; i < size; i++)
			            		{

			            			ValueNamePair pp = (ValueNamePair)CbbEmployee.getItemAt(i);
			            			
//							   	    System.out.println("actionPerformed() - getTxtEmployeeNumber cmp " + Integer.parseInt(pp.getValue()) + " == " + rs.getInt(1) ); 
			            			if (Integer.parseInt(pp.getValue()) == rs.getInt(1) )
			            			{
			            				CbbEmployee.setSelectedIndex( i );
			            				continue;
			            			}
			            		}
		            		}
			                rs.close();
			                pstmt.close();
				        }
				        catch (Exception ex)
				        {
				                s_log.log(Level.SEVERE,"TimeSheetGeneration.refreshEmployeeInformation", ex);
				        }
				   	}
		    });
		}
		return txtEmployeeNumber;
	}
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private CComboBox getCbbEmployee() {
		if (CbbEmployee == null) {
			CbbEmployee = new org.compiere.swing.CComboBox();
			CbbEmployee.setBounds(219, 43, 210, 20);
			CbbEmployee.addActionListener(new java.awt.event.ActionListener() 
				    { 
					   public void actionPerformed(java.awt.event.ActionEvent e) 
					   {    
//					   		System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()

				       		ValueNamePair pemp = (ValueNamePair)CbbEmployee.getSelectedItem();
				       		if ( pemp.getValue().length() == 0 )
				       		{
				       			cbbPaymentGroup.setEnabled( true );
				       			return;
				       		}

				    		String employeeQuery = "Select P_Payment_Group_ID From P_Employee Where P_Employee_ID = " + pemp.getValue();
			                PreparedStatement pstmt = DB.prepareStatement(employeeQuery, null);
			        		try
					        {
					       		int size = cbbPaymentGroup.getItemCount();

					       		ResultSet rs = pstmt.executeQuery();
				                if(rs.next())
				                {
				            		for (int i = 0; i < size; i++)
				            		{

				            			ValueNamePair pp = (ValueNamePair)cbbPaymentGroup.getItemAt(i);
				            			
				            			if (Integer.parseInt(pp.getValue()) == rs.getInt(1) )
				            			{
							       			cbbPaymentGroup.setEnabled( true );
				            				cbbPaymentGroup.setSelectedIndex( i );
							       			cbbPaymentGroup.setEnabled( false );
				            				continue;
				            			}
				            		}
			            		}
				                rs.close();
				                pstmt.close();
					        }
					        catch (Exception ex)
					        {
					                s_log.log(Level.SEVERE,"TimeSheetGeneration.refreshEmployeeInformation", ex);
					        }
					   	}
				    });
		}
		return CbbEmployee;
	}
	/**
	 * This method initializes CComboBox	
	 * 	
	 * @return javax.swing.CComboBox	
	 */    
	private CComboBox getCbbFrequency() {
		if (cbbFrequency == null) {
			cbbFrequency = new org.compiere.swing.CComboBox();
			cbbFrequency.setBounds(219, 81, 210, 20);
			cbbFrequency.addActionListener(new java.awt.event.ActionListener() 
				    { 
					   public void actionPerformed(java.awt.event.ActionEvent e) 
					   {    
					   		System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
					   		loadPayPeriod();
					   	}
				    });
		}
		return cbbFrequency;
	}
	/**
	 * This method initializes CComboBox	
	 * 	
	 * @return javax.swing.CComboBox	
	 */    
	private CComboBox getCbbPayPeriod() {
		if (cbbPayPeriod == null) {
			cbbPayPeriod = new org.compiere.swing.CComboBox();
			cbbPayPeriod.setBounds(219, 114, 210, 20);
			cbbPayPeriod.addActionListener(this);
			cbbPayPeriod.setActionCommand("PayPeriod");
		}
		return cbbPayPeriod;
	}
	/**
	 * This method initializes CComboBox	
	 * 	
	 * @return javax.swing.CComboBox	
	 */    
	private CComboBox getCbbGroupPay() {
		if (cbbPaymentGroup == null) {
			cbbPaymentGroup = new org.compiere.swing.CComboBox();
			cbbPaymentGroup.setBounds(217, 150, 210, 20);
			cbbPaymentGroup.addActionListener(this);
			cbbPaymentGroup.setActionCommand("GroupPay");
		}
		return cbbPaymentGroup;
	}
	/**
	 * Response to differents Kind of actions
	 */
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
/*		if(e.getActionCommand() == "EmployeeNumber"){
//			LoadEmployeeName();
			
		}else if (e.getActionCommand() == "Frequency"){ // A new Frenqueny is chosen
			
			
		}else if (e.getActionCommand() == "GroupPay"){ // A new Group Pay is chosen
			
			
		}else if (e.getActionCommand() == "PayPeriod"){ // A new Pay period is chosen
			
			
		}else*/
		 if(e.getActionCommand() == "Create"){

			//Call time sheet genration proc
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			generate(_pi);
			this.setCursor(Cursor.getDefaultCursor());

		
		}else if ( e.getActionCommand() == "Close"){
			//Close the current Window - Frame
			this.dispose();
		}
		
	}
	
	

	private void generate( ProcessInfo pi )
	{
		//Create a new Time Sheet
		String value = txtEmployeeNumber.getText();
		int frequencyID = -1;
		int payPeriodID = -1;
		int PaymentGroupID = -1;
		int employeeID = -1;
		
		ValueNamePair pp = (ValueNamePair)cbbFrequency.getSelectedItem();
		frequencyID = Integer.parseInt(pp.getValue());

		pp = (ValueNamePair)cbbPayPeriod.getSelectedItem();
		payPeriodID = Integer.parseInt(pp.getValue());

		pp = (ValueNamePair)cbbPaymentGroup.getSelectedItem();
		PaymentGroupID = Integer.parseInt(pp.getValue());

		pp = (ValueNamePair)CbbEmployee.getSelectedItem();
		employeeID = Integer.parseInt(pp.getValue());
		

		String sql = new String("");

//		statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "InOutGenerateGen"));
//		statusBar.setStatusDB(String.valueOf(no));

//		int gen = InOutTimeGeneration.TimeGeneration( Env.getCtx(), employeeID, payPeriodID, PaymentGroupID );
		//	Prepare Process

		//	Add Parameter - Selection=Y
		sql = "INSERT INTO AD_PInstance_Para (AD_PInstance_ID,SeqNo,ParameterName, P_STRING) "
			+ "VALUES (" + pi.getAD_PInstance_ID() + ",1,'Selection', 'Y')";
		int no = DB.executeUpdate(sql, null);
		if (no == 0)
		{
			String msg = "No Parameter added";  //  not translated
//			info.setText(msg);
			s_log.log(Level.SEVERE,"VInOutGen.generateShipments - " + msg);
			return;
		}
		sql = "INSERT INTO AD_PInstance_Para (AD_PInstance_ID,SeqNo,ParameterName, P_NUMBER) "
			+ "VALUES (" + pi.getAD_PInstance_ID() + ",2,'P_Employee_ID'," + employeeID + ")";
		no = DB.executeUpdate(sql, null);

		sql = "INSERT INTO AD_PInstance_Para (AD_PInstance_ID,SeqNo,ParameterName, P_NUMBER) "
			+ "VALUES (" + pi.getAD_PInstance_ID() + ",3,'P_Payment_Group_ID'," + PaymentGroupID + ")";
		no = DB.executeUpdate(sql, null);

		sql = "INSERT INTO AD_PInstance_Para (AD_PInstance_ID,SeqNo,ParameterName, P_NUMBER) "
			+ "VALUES (" + pi.getAD_PInstance_ID() + ",4,'P_Period_ID'," + payPeriodID + ")";
		no = DB.executeUpdate(sql, null);

		
		
		//	Execute Process
//		Trx trx = Trx.get( Trx.createTrxName(), true);
		Trx trx = null;

		ProcessCtl worker = new ProcessCtl( this, pi, trx );  // (this, pi);
		worker.start();     //  complete tasks in unlockUI / generateShipments_complete
	}
	

	
	/**
	 * Load  the frequency 
	 *
	 */
	private void loadFrequency(){
		String sql = "SELECT P_Frequency_ID, Name, IsDefault FROM P_Frequency Where IsActive = 'Y' ORDER BY Name";
		System.out.println(" loadPayPeriod " + sql ); 

		int selectedIndex = 0;
	  	try
		{
	  		PreparedStatement pstmt = DB.prepareStatement(sql, null);
	  		ResultSet rs = pstmt.executeQuery();
	  		int index = 0;

	  		ValueNamePair vp = null;
	    	while (rs.next())
	    	{
	    		vp = new ValueNamePair(rs.getString(1), rs.getString(2));   //  returns also not active
	    		cbbFrequency.addItem(vp);
	            if ( rs.getString(3).compareTo("Y") == 0 )
	            {
	               	selectedIndex = index;
	            }
	    	    index++;
	    	}
	    	cbbFrequency.setSelectedIndex( selectedIndex );
		}
	  	catch (Exception e)
		{
	  		s_log.log(Level.SEVERE,"TimeSheetGeneration.loadFrequencies", e);
		}
	}
	/**
	 * Load the pay Period 
	 *
	 */
	private void loadPayPeriod(){

		ValueNamePair pp = (ValueNamePair)cbbFrequency.getSelectedItem();
		String sql = "SELECT P_Period_ID, Name, IsDefault FROM P_Period WHERE PeriodStatus IN ('N','O') AND P_Frequency_ID = " + pp.getValue() + " ORDER BY Name DESC";

		System.out.println(" loadPayPeriod " + sql ); 

		int selectedIndex = 0;
	  	try
		{
	  		PreparedStatement pstmt = DB.prepareStatement(sql, null);
	  		ResultSet rs = pstmt.executeQuery();
	  		int index = 0;

	  		cbbPayPeriod.removeAllItems();
	  		
	  		ValueNamePair vp = null;
	    	while (rs.next())
	    	{
	    		vp = new ValueNamePair(rs.getString(1), rs.getString(2));   //  returns also not active
	    		cbbPayPeriod.addItem(vp);
	            if ( rs.getString(3).compareTo("Y") == 0 )
	            {
	               	selectedIndex = index;
	            }
	    	    index++;
	    	}
	    	cbbPayPeriod.setSelectedIndex( selectedIndex );
		}
	  	catch (Exception e)
		{
	  		s_log.log(Level.SEVERE,"VEmployeeListPanel.loadPayPeriod", e);
		}
	}

	/**
	 * Load Payment Group
	 *
	 */
	private void loadPaymentGroup(){
		String sql = "SELECT P_Payment_Group_ID, Name FROM P_Payment_Group WHERE IsActive = 'Y' ORDER BY Name";
		System.out.println(" loadPaymentGroup " + sql ); 

		int selectedIndex = 0;
	  	try
		{
	  		PreparedStatement pstmt = DB.prepareStatement(sql, null);
	  		ResultSet rs = pstmt.executeQuery();
	  		int index = 0;

	  		ValueNamePair vp = null;
	    	while (rs.next())
	    	{
	    		vp = new ValueNamePair(rs.getString(1), rs.getString(2));   //  returns also not active
	    		cbbPaymentGroup.addItem(vp);
	    	    index++;
	    	}
	    	cbbPaymentGroup.setSelectedIndex( 0 );
		}
	  	catch (Exception e)
		{
	  		s_log.log(Level.SEVERE,"TimeSheetGeneration.loadPaymentGroup", e);
		}
	}
	
	/**
	 * Load Payment Group
	 *
	 */
	private void loadEmployee(){
		String sql = "SELECT P_Employee_ID, Name FROM P_Employee WHERE IsActive = 'Y' ORDER BY Name";
		System.out.println(" loadEmployee " + sql ); 

		int selectedIndex = 0;
	  	try
		{
	  		PreparedStatement pstmt = DB.prepareStatement(sql, null);
	  		ResultSet rs = pstmt.executeQuery();
	  		int index = 0;

	  		ValueNamePair vp = null;

    		vp = new ValueNamePair(null, new String("") );   //  returns also not active
    		CbbEmployee.addItem(vp);
	  		
	  		while (rs.next())
	    	{
	    		vp = new ValueNamePair(rs.getString(1), rs.getString(2));   //  returns also not active
	    		CbbEmployee.addItem(vp);
	    	    index++;
	    	}
	    	CbbEmployee.setSelectedIndex( 0 );
		}
	  	catch (Exception e)
		{
	  		s_log.log(Level.SEVERE,"TimeSheetGeneration.loadEmployee", e);
		}
	}
	
	/**
	 *  Lock User Interface.
	 *  Called from the Worker before processing
	 *  @param pi process info
	 */
	public void lockUI (ProcessInfo pi)
	{
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		this.setEnabled(false);
	}   //  lockUI

	/**
	 *  Unlock User Interface.
	 *  Called from the Worker when processing is done
	 *  @param pi result of execute ASync call
	 */
	public void unlockUI (ProcessInfo pi)
	{
		this.setEnabled(true);
		this.setCursor(Cursor.getDefaultCursor());
		//
		generate(pi);
	}   //  unlockUI

	/**
	 *  Is the UI locked (Internal method)
	 *  @return true, if UI is locked
	 */
	public boolean isUILocked()
	{
		return this.isEnabled();
	}   //  isUILocked

	/**
	 *  Method to be executed async.
	 *  Called from the Worker
	 *  @param pi ProcessInfo
	 */
	public void executeASync (ProcessInfo pi)
	{
	}   //  executeASync

	
 }  //  @jve:decl-index=0:visual-constraint="10,10"
