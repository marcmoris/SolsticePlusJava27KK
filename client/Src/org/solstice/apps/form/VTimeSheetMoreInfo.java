package org.solstice.apps.form;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.*;

import org.compiere.apps.ConfirmPanel;
import org.compiere.grid.ed.VEditor;
import org.compiere.grid.ed.VLookup;
import org.compiere.model.*;
import org.compiere.swing.CDialog;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

import solstice.model.P_Employee;
import solstice.model.P_Employer;
import solstice.model.P_Job_Type;
import solstice.model.P_Time_Sheet;
import solstice.utils.PgiUtil;

public class VTimeSheetMoreInfo extends CDialog
	{
	  private JPanel panel1 = new JPanel();
	  private BorderLayout borderLayout1 = new BorderLayout();
//	  private JScrollPane theScrollPane = new JScrollPane();
  	  private ConfirmPanel confirmPanel = new ConfirmPanel(true);

/*	  String[] columnNames = {"Column 1",
	                          "Col 2",
	                          "Col 3",
	                          "Col 4",
	                          "Some Description",
	                          "Column 6",
	                          "Col 7",
	                          "ABS",
	                          "DEF",
	                          "Column xx",
	                          "Override",
	                          "Something",
	                          "Last Col"};
	  String[][] data = {
	      {"Al", "Alexander","Stuff", "ABC","ABC","ABC","ABC","ABC","ABC","ABC","ABC","ABC","ABC","ABC","ABC"},
	      {"Al", "Alexander","Stuff", "ABC","ABC","ABC","ABC","ABC","ABC","ABC","ABC","ABC","ABC","ABC","ABC"},
	      {"Al", "Alexander","Stuff", "ABC","ABC","ABC","ABC","ABC","ABC","ABC","ABC","ABC","ABC","ABC","ABC"},
	      {"Al", "Alexander","Stuff", "ABC","ABC","ABC","ABC","ABC","ABC","ABC","ABC","ABC","ABC","ABC","ABC"},
	    };
	  JTable paperTable = new JTable(data,columnNames);
*/
	  private P_Time_Sheet m_TimeSheet; 
	  
	  public VTimeSheetMoreInfo(Frame frame, String title, boolean modal, P_Time_Sheet TimeSheet)
	  {
	    super(frame, title, modal);
	    
	    m_TimeSheet = TimeSheet;
	    try
	    {
	      jbInit();
	      pack();
	    }
	    catch(Exception ex)
	    {
	      ex.printStackTrace();
	    }
	  }

	  public VTimeSheetMoreInfo()
	  {
	    this(null, "", false, null);
	  }
	  

	  
	  void jbInit() throws Exception
	  {
	    panel1.setLayout(borderLayout1);
	    getContentPane().add(panel1);
	    
	    panel1.add(scontentPanel, BorderLayout.CENTER);
	    panel1.add(confirmPanel, BorderLayout.SOUTH);

//	    panel1.add(theScrollPane, BorderLayout.CENTER);
	    setupInitialColumnWidths();
//	    theScrollPane.getViewport().add(paperTable, null);
	    pack();
	  }
	  
	  
	  private CPanel scontentPanel = new CPanel(new GridBagLayout());
		private GridBagLayout scontentLayout = new GridBagLayout();

		private int				m_sLine = 7;

		public int m_curWindowNo;
		private ArrayList<VEditor>			m_sEditors = new ArrayList<VEditor>();

	    public CLabel labelClient = new CLabel();
	    public CLabel labelEmployee = new CLabel();
	    public CLabel labelEmployer = new CLabel();
	    public CLabel labelPaymentGroup = new CLabel();
	    public CLabel labelActivity = new CLabel();
	    public CLabel labelOrg = new CLabel();
	    public CLabel labelDepartment = new CLabel();
	    public CLabel labelOccupationGroup = new CLabel();
		public CLabel labelTaxationRegion  = new CLabel();
		public CLabel labelWorkPlace  = new CLabel();
		public CLabel labelDistributionBooklet  = new CLabel();
		public CLabel labelDistribution  = new CLabel();
		public CLabel labelJobType  = new CLabel();
		public CLabel labelJobTitle  = new CLabel();

	    public VLookup fieldClient ;
	    public VLookup fieldActivity;
	    public VLookup fieldOrg ;
	    public VLookup fieldDepartment;
	    public VLookup fieldOccupationGroup;
		public VLookup fieldEmployee;
		public VLookup fieldEmployer;
		public VLookup fieldTaxationRegion;
		public VLookup fieldWorkPlace;

		public VLookup fieldDistributionBooklet;
		public VLookup fieldDistribution;
		public VLookup fieldJobType;
		public VLookup fieldJobTitle;
		public VLookup fieldPaymentGroup;

		   private static final Dimension MIN_SIZE_BT = new Dimension(50, 22);
		    private static final Dimension MIN_SIZE = new Dimension(180, 22);
		    private static final Dimension PREF_SIZE = new Dimension(180, 22);
		    private static final Dimension MAX_HEIGHT = new Dimension(Integer.MAX_VALUE, 22);
		    private void setupSize( JComponent component ) {
		    	component.setMinimumSize( MIN_SIZE );
		    	component.setPreferredSize( PREF_SIZE );
		    	component.setMaximumSize( MAX_HEIGHT );
		    }
	  
	  private void setupInitialColumnWidths()
	  {
			scontentPanel.setLayout(scontentLayout);

			labelEmployee.setText(Msg.translate(Env.getCtx(), "P_Employee_ID"));
			labelPaymentGroup.setText(Msg.translate(Env.getCtx(), "P_Payment_Group_ID"));
			labelActivity.setText(Msg.translate(Env.getCtx(), "C_activity_ID"));
			labelClient.setText(Msg.translate(Env.getCtx(), "AD_Client_ID"));
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

	        KeyNamePair oo = null;
	        
			MLookup ClientL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, 527, DisplayType.Table);
			fieldClient = new VLookup ("AD_Client_ID", true, true, false, ClientL);
			//Set la dimention des combobox
//			fieldClient.setPreferredSize( new Dimension(310, 22) );
			setupSize(fieldClient);
			fieldClient.setValue(new Integer(m_TimeSheet.getAD_Client_ID()));
/*
			MClient Client = new MClient( Env.getCtx(), m_TimeSheet.getAD_Client_ID(), null);
			oo = new KeyNamePair( Client.getAD_Client_ID(), Client.getValue());
	        Object newItem = oo; 
			ClientL.setSelectedItem( newItem );
*/
			MLookup OrganisationL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, 528, DisplayType.Table);
			fieldOrg = new VLookup ("AD_Org_ID", false, true, false, OrganisationL);
			//Set la dimention des combobox
//			fieldOrg.setPreferredSize( new Dimension(310, 22) );
			setupSize(fieldOrg);

			fieldOrg.setValue(new Integer(m_TimeSheet.getAD_Org_ID()));
			
			MLookup empL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, 2111113, DisplayType.Search);
			fieldEmployee = new VLookup ("P_Employee_ID", false, true, false, empL);
			fieldEmployee.setValue(new Integer(m_TimeSheet.getP_Employee_ID()));


			MLookup DepartmentL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, getAD_Column_ID("P_Department_ID"), DisplayType.Table);
			fieldDepartment = new VLookup ("P_Department_ID", false, false, true, DepartmentL);
			//			fieldDepartment.setBounds(206, 20, 610, 19);
			fieldDepartment.setValue(new Integer(m_TimeSheet.getP_Department_ID()));

			MLookup TaxationRegionL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, getAD_Column_ID("Taxation_Region_ID"), DisplayType.Table);
			fieldTaxationRegion = new VLookup ("Taxation_Region_ID", false, false, true, TaxationRegionL);
			//			fieldTaxationRegion.setBounds(206, 20, 610, 19);
			fieldTaxationRegion.setValue(new Integer( m_TimeSheet.getTaxation_Region_ID()));

			MLookup WorkPlaceL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, getAD_Column_ID("P_WorkPlace_ID"), DisplayType.Table);
			fieldWorkPlace = new VLookup ("P_WorkPlace_ID", false, false, true, WorkPlaceL);
			//			fieldWorkPlace.setBounds(206, 20, 610, 19);
			fieldWorkPlace.setValue(new Integer( m_TimeSheet.getP_Workplace_ID()));

			MLookup JobTitleL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, getAD_Column_ID("P_Job_Title_ID"), DisplayType.Table);
			fieldJobTitle = new VLookup ("P_Job_Title_ID", false, false, true, JobTitleL);
			//			fieldJobTitle.setBounds(206, 20, 610, 19);
			fieldJobTitle.setValue(new Integer( m_TimeSheet.getP_Job_Title_ID()));

			MLookup JobTypeL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, getAD_Column_ID("P_Job_Type_ID"), DisplayType.Table);
			fieldJobType = new VLookup ("P_Job_Type_ID", false, false, true, JobTypeL);
			//			fieldJobType.setBounds(206, 20, 610, 19);
			fieldJobType.setValue(new Integer( m_TimeSheet.getP_Job_Type_ID()));

			MLookup PaymentGroupL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, getAD_Column_ID("P_Payment_Group_ID"), DisplayType.Table);
			fieldPaymentGroup = new VLookup ("P_Payment_Group_ID", false, false, true, PaymentGroupL);
			//			fieldPaymentGroup.setBounds(206, 20, 610, 19);
			fieldPaymentGroup.setValue(new Integer( m_TimeSheet.getP_Payment_Group_ID()));

			
			MLookup DistributionL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, getAD_Column_ID("P_Distribution_ID"), DisplayType.Table);
			fieldDistribution = new VLookup ("P_Distribution_ID", false, false, true, DistributionL);
			//			fieldDistribution.setBounds(206, 20, 610, 19);
			fieldDistribution.setValue(new Integer( m_TimeSheet.getP_Distribution_ID()));

			MLookup DistributionBookletL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, getAD_Column_ID("P_Distribution_Booklet_ID"), DisplayType.Table);
			fieldDistributionBooklet = new VLookup ("P_Distribution_Booklet_ID", false, false, true, DistributionBookletL);
			//			fieldDistributionBooklet.setBounds(206, 20, 610, 19);
			fieldDistributionBooklet.setValue(new Integer( m_TimeSheet.getP_Distribution_Booklet_ID()));
			
			MLookup OccupationGroupL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, getAD_Column_ID("P_Occupation_Group_ID"), DisplayType.Table);
			fieldOccupationGroup = new VLookup ("P_Occupation_Group_ID", false, false, true, OccupationGroupL);
			//			fieldOccupationGroup.setBounds(206, 20, 610, 19);
			fieldOccupationGroup.setValue(new Integer( m_TimeSheet.getP_Occupation_Group_ID()));

			MLookup EmployerL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, getAD_Column_ID("P_Employer_ID"), DisplayType.Table);
			fieldEmployer = new VLookup ("P_Employer_ID", false, false, true, EmployerL);
			//			fieldEmployer.setBounds(206, 20, 610, 19);
			fieldEmployer.setValue(new Integer( m_TimeSheet.getP_Employer_ID()));

			
			MLookup ActivityL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, getAD_Column_ID("C_Activity_ID"), DisplayType.Table);
			fieldActivity = new VLookup ("C_Activity_ID", false, false, true, ActivityL);
			//			fieldActivity.setBounds(206, 20, 610, 19);
			fieldActivity.setValue(new Integer( m_TimeSheet.getC_Activity_ID()));

			addSelectionColumn( labelClient, fieldClient, false );
			addSelectionColumn( labelOrg, fieldOrg, true );
			m_sLine = m_sLine +1;
		

			addSelectionColumn( labelEmployee, fieldEmployee, false );
			addSelectionColumn( labelPaymentGroup, fieldPaymentGroup, true );
			m_sLine = m_sLine +1;

			addSelectionColumn( labelActivity, fieldActivity, false );
			addSelectionColumn( labelDepartment, fieldDepartment, true );
			m_sLine = m_sLine +1;

			addSelectionColumn( labelEmployer, fieldEmployer, false );
			addSelectionColumn( labelTaxationRegion, fieldTaxationRegion, true );
			m_sLine = m_sLine +1;

			addSelectionColumn( labelWorkPlace, fieldWorkPlace, false );
			addSelectionColumn( labelOccupationGroup, fieldOccupationGroup, true );
			m_sLine = m_sLine +1;
			
			addSelectionColumn( labelDistributionBooklet, fieldDistributionBooklet, false );
			addSelectionColumn( labelDistribution  , fieldDistribution  , true );
			m_sLine = m_sLine +1;

			addSelectionColumn( labelJobType, fieldJobType, false );
			addSelectionColumn( labelJobTitle  , fieldJobTitle  , true );
			m_sLine = m_sLine +1;

			confirmPanel.addActionListener(this);

		  
/*	    paperTable.getColumnModel().getColumn(0).setMinWidth(60);
	    paperTable.getColumnModel().getColumn(1).setMinWidth(80);
	    paperTable.getColumnModel().getColumn(2).setMinWidth(80);
	    paperTable.getColumnModel().getColumn(3).setMinWidth(100);
	    paperTable.getColumnModel().getColumn(4).setMinWidth(60);
	    paperTable.getColumnModel().getColumn(5).setMinWidth(60);
	    paperTable.getColumnModel().getColumn(6).setMinWidth(60);
	    paperTable.getColumnModel().getColumn(7).setMinWidth(60);
	    paperTable.getColumnModel().getColumn(8).setMinWidth(60);
	    paperTable.getColumnModel().getColumn(9).setMinWidth(60);
	    paperTable.getColumnModel().getColumn(10).setMinWidth(100);
	    paperTable.getColumnModel().getColumn(11).setMinWidth(60);
	    paperTable.getColumnModel().getColumn(12).setMinWidth(60);

	    //paperTable.setPreferredSize(new Dimension(900,400));
	    paperTable.setPreferredScrollableViewportSize(new Dimension(900,400));
*/	    
	  }
	  
	    private int getAD_Column_ID( String ColumnName )
		{
			int id = 0;
			String sql = "select AD_COLUMN_ID from AD_COLUMN where columnNAME = '" +ColumnName+ "'   and AD_TABLE_ID = 2000092";
			PreparedStatement pstmt = null;
			try
			{
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
			
			return id;
			
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
					,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 55), 0, 0));
			
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

		/**************************************************************************
		 *	Action Listener
		 *  @param e event
		 */
		public void actionPerformed (ActionEvent e)
		{
			if (e.getActionCommand().equals(ConfirmPanel.A_OK))
			{

				
				int C_Activity_ID             = 0;
				int AD_Org_ID                 = 0;
				int P_Department_ID           = 0;
				int P_OccupationGroup_ID      = 0;
				int P_WorkPlace_ID            = 0;
				int P_Employer_ID             = 0;
				int P_Distribution_Booklet_ID = 0;	
				int P_Distribution_ID         = 0;
				int P_Job_Type_ID             = 0;
				int P_Job_Title_ID            = 0;
				int P_Payment_Group_id        = 0;
				int Taxation_Region_ID        = 0;

				if ( fieldActivity.getValue() != null )
					C_Activity_ID             = Integer.parseInt( fieldActivity.getValue().toString());	
					
				if ( fieldOrg.getValue() != null )
				AD_Org_ID                 = Integer.parseInt( fieldOrg.getValue().toString());	 

				if ( fieldDepartment.getValue() != null )
				P_Department_ID           = Integer.parseInt( fieldDepartment.getValue().toString());	

				if ( fieldOccupationGroup.getValue() != null )
				P_OccupationGroup_ID      = Integer.parseInt( fieldOccupationGroup.getValue().toString());	

				if ( fieldWorkPlace.getValue() != null )
				P_WorkPlace_ID            = Integer.parseInt( fieldWorkPlace.getValue().toString());	

				if ( fieldEmployer.getValue() != null )
				P_Employer_ID             = Integer.parseInt( fieldEmployer.getValue().toString());	

				if ( fieldDistributionBooklet.getValue() != null )
				P_Distribution_Booklet_ID = Integer.parseInt( fieldDistributionBooklet.getValue().toString());	

				if ( fieldDistribution.getValue() != null )
				P_Distribution_ID         = Integer.parseInt( fieldDistribution.getValue().toString());	

				if ( fieldJobType.getValue() != null )
				P_Job_Type_ID             = Integer.parseInt( fieldJobType.getValue().toString());	

				if ( fieldJobTitle.getValue() != null )
				P_Job_Title_ID            = Integer.parseInt( fieldJobTitle.getValue().toString());	

				if ( fieldPaymentGroup.getValue() != null )
				P_Payment_Group_id          = Integer.parseInt(fieldPaymentGroup.getValue().toString());	

				if ( fieldTaxationRegion.getValue() != null )
				Taxation_Region_ID        = Integer.parseInt(fieldTaxationRegion.getValue().toString());

				
				
				m_TimeSheet.setC_Activity_ID             ( C_Activity_ID             );
				m_TimeSheet.setAD_Org_ID                 ( AD_Org_ID                 );
				m_TimeSheet.setP_Department_ID           ( P_Department_ID           );
				m_TimeSheet.setP_Occupation_Group_ID     ( P_OccupationGroup_ID      );
				m_TimeSheet.setP_Employer_ID             ( P_Employer_ID             );
				m_TimeSheet.setP_Workplace_ID            ( P_WorkPlace_ID            );
				m_TimeSheet.setP_Distribution_Booklet_ID ( P_Distribution_Booklet_ID );	
				m_TimeSheet.setP_Distribution_ID         ( P_Distribution_ID         );
				m_TimeSheet.setP_Job_Type_ID             ( P_Job_Type_ID             );
				m_TimeSheet.setP_Job_Title_ID            ( P_Job_Title_ID            );
				m_TimeSheet.setP_Payment_Group_ID        ( P_Payment_Group_id        );
				m_TimeSheet.setTaxation_Region_ID        ( Taxation_Region_ID      );
				m_TimeSheet.save();
				dispose();
			}
			else if (e.getActionCommand().equals(ConfirmPanel.A_CANCEL))
			{
				dispose();
			}

			confirmPanel.getOKButton().setEnabled(true);


		}	//	actionPerformed

	}