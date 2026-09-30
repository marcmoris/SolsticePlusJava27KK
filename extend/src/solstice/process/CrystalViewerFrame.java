/*
 * This sample code is an example of how to use the Business Objects APIs. 
 * Because the sample code is designed for demonstration only, it is 
 * unsupported.  You are free to modify and distribute the sample code as needed.   
 */
package solstice.process;

import java.math.BigDecimal;
import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.compiere.util.CLogger;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
import org.compiere.db.CConnection;
import org.compiere.model.MPInstance;
import org.compiere.model.MProcess;
import org.compiere.model.MProcessPara;
import org.compiere.model.MSystem;
import org.compiere.process.ProcessInfoParameter;

import solstice.model.P_Notice_Bank_Deposit;
import solstice.model.P_Statement_Earning;
import solstice.utils.PgiUtil;




import com.crystaldecisions.ReportViewer.ReportViewerBean;
import com.crystaldecisions.sdk.occa.report.application.OpenReportOptions;
import com.crystaldecisions.sdk.occa.report.application.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.logging.Level;

import org.compiere.util.DB;
/**
 * A sample report viewer class which can load a report and display it in 
 * a ReportViewerBean embedded in a JFrame.
 */
@SuppressWarnings("serial")
public class CrystalViewerFrame extends JFrame
{
        private static void invokeViewerMethod(Object target, String methodName) {
        if (target == null) return;
        try {
            java.lang.reflect.Method m = target.getClass().getMethod(methodName);
            m.invoke(target);
        } catch (Throwable t) {}
    }

    private static void invokeViewerMethodWithArg(Object target, String methodName, Object arg) {
        if (target == null) return;
        try {
            for (java.lang.reflect.Method m : target.getClass().getMethods()) {
                if (m.getName().equals(methodName) && m.getParameterCount() == 1) {
                    m.invoke(target, arg);
                    return;
                }
            }
        } catch (Throwable t) {}
    }

//    public static final String FRAME_TITLE = "Report Viewer";
	public static final String FRAME_TITLE = "Visionneur de rapport";
	
	private static CLogger		log = CLogger.getCLogger (CrystalViewerFrame.class);

    /** The report viewer bean instance. */ 
    private final ReportViewerBean reportViewer;

    /** The ReportClientDocument instance being used. 
     *  Set by loadReport(). */
    private ReportClientDocument reportClientDocument;

    private static ProcessInfoParameter[] processInfoParameter;

    /**
     * Private constructor for this class.
     * Call showViewer() to obtain instances.
     */
    public CrystalViewerFrame() {
        
        setTitle (FRAME_TITLE);
        
        this.reportViewer = new ReportViewerBean();
        invokeViewerMethod(reportViewer, "init");

        // A menu bar can be added here if desired
        
        // Handle closing of the viewer.
        addWindowListener (new WindowAdapter ()
        {
            public void windowClosing (WindowEvent e)
            {
                closeViewer ();
            }
        });
        
        getContentPane ().add ((java.awt.Component) (Object) reportViewer, BorderLayout.CENTER);

        // Set to some default size
        Insets insets = getInsets ();
        setSize (insets.left + 900 + insets.right, insets.top + 500 + insets.bottom);

        // Show in a sensible location for the platform.
        setLocationByPlatform (true);
    }
    
    /**
     * Create a new instance of this viewer class and show it.
     *
     * @return the new instance of this class that was created.
     */
    static CrystalViewerFrame showViewerFrame () 
    {
        CrystalViewerFrame viewerFrame = new CrystalViewerFrame();
        viewerFrame.setVisible (true);
        
        // Start the viewer
        invokeViewerMethod(viewerFrame.reportViewer, "start");
        
        return viewerFrame;
    }

    
    /**
     * Entry point for this class.
     * Create and show the report viewer frame, then bind a report to it so that it can be viewed. 
     */
    public static void showViewer ( ) 
    {
        CrystalViewerFrame viewerFrame = showViewerFrame ();
        boolean success = viewerFrame.showReport ();
        if (!success) {
            viewerFrame.closeViewer ();
        }
    }
    
    //+ Progestion Inf, Solstice 
    /**
     * Entry point for this class.
     * Create and show the report viewer frame, then bind a report to it so that it can be viewed. 
     */
    
    
    public void showViewer ( int AD_PInstance_ID, String m_Format, ProcessInfoParameter[] processParameter)  throws ReportSDKException
    {
        CrystalViewerFrame viewerFrame = showViewerFrame ();
        
        processInfoParameter = processParameter;
        
        
        
        boolean success = viewerFrame.showReport ( AD_PInstance_ID );
        if (!success) {
            viewerFrame.closeViewer ();
        }
    }

    public void showViewer ( int AD_PInstance_ID, String m_Format, ProcessInfoParameter[] processParameter, String ReportName)  throws ReportSDKException
    {
        CrystalViewerFrame viewerFrame = showViewerFrame ();
        
        processInfoParameter = processParameter;
        
        boolean success = viewerFrame.showReport ( AD_PInstance_ID, ReportName );
        if (!success) {
            viewerFrame.closeViewer ();
        }
    }

    //- Progestion Inf, Solstice 

    
    /**
     * Close the viewer.
     */
    private void closeViewer ()
    {
        if (reportViewer != null)
        {
            invokeViewerMethod(reportViewer, "stop");
            invokeViewerMethod(reportViewer, "destroy");
        }
        
        removeAll ();
        dispose ();
    }

    
    /**
     * Load a report and show it in the viewer.
     * @return whether a report was successfully displayed.
     */
    private boolean showReport () 
    {
        try
        {
            loadReport ();

            if (reportClientDocument != null) {
                setDatabaseLogon ();
                setParameterFieldValues ();
                setReportSource ();
                
                return true;
            }
        }
        catch (ReportSDKException e)
        {
            String localizedMessage = e.getLocalizedMessage ();
            int errorCode = e.errorCode ();
            
            String title = "Problem showing report";
            String message = localizedMessage + "\nError code: " + errorCode;
//            JOptionPane.showMessageDialog (CrystalViewerFrame.this, message, title, JOptionPane.WARNING_MESSAGE);
            log.log (Level.SEVERE,title + " - " + message , e);
        }
        return false;
    }
    /**
     * Load a report and show it in the viewer.
     * @return whether a report was successfully displayed.
     */
    private boolean showReport ( int AD_PInstance_ID ) 
    {
        try
        {
            loadReport ( AD_PInstance_ID, null );

            if (reportClientDocument != null) {
                setDatabaseLogon ();
                setParameterFieldValues ();
                setReportSource ();
                
                return true;
            }
        }
        catch (ReportSDKException e)
        {
            String localizedMessage = e.getLocalizedMessage ();
            int errorCode = e.errorCode ();
            
            String title = "Problem showing report";
            String message = localizedMessage + "\nError code: " + errorCode;
//            JOptionPane.showMessageDialog (CrystalViewerFrame.this, message, title, JOptionPane.WARNING_MESSAGE);
            log.log (Level.SEVERE,title + " - " + message , e);
        }
        return false;
    }

    private boolean showReport ( int AD_PInstance_ID, String ReportName ) 
    {
        try
        {
            loadReport ( AD_PInstance_ID, ReportName );

            if (reportClientDocument != null) {
                setDatabaseLogon ();
                setParameterFieldValues ();
                setReportSource ();
                
                return true;
            }
        }
        catch (ReportSDKException e)
        {
            String localizedMessage = e.getLocalizedMessage ();
            int errorCode = e.errorCode ();
            
            String title = "Problem showing report";
            String message = localizedMessage + "\nError code: " + errorCode;
//            JOptionPane.showMessageDialog (CrystalViewerFrame.this, message, title, JOptionPane.WARNING_MESSAGE);
            log.log (Level.SEVERE,title + " - " + message , e);
        }
        return false;
    }

    
    private MPInstance instance;
    private MProcess process;
    
    private void loadReport ( int AD_PInstance_ID, String ReportName ) throws ReportSDKException
    {
        // String reportFilePath = "C:\\Solstice\\Sources\\crystalReports\\";
        
       // String reportFilePath = "\\\\solstice\\Rapport\\"; 
		String client = PgiUtil.getSolsticeParameter( Env.getCtx(), "Client").toUpperCase();

		//Path 1
        String reportFilePath = PgiUtil.getSolsticeParameter(Env.getCtx(), "CrystalReportShareDirectory01");
        String fileName = null;
        
        instance = new MPInstance( Env.getCtx(), AD_PInstance_ID, null );
        process = new MProcess( Env.getCtx(), instance.getAD_Process_ID(), null);
        
        if ( ! reportFilePath.endsWith("\\"))
        	reportFilePath = reportFilePath + "\\";
        
        String processValue = null;
        if ( ReportName != null )
        	processValue = ReportName;
        else
        	processValue = process.getValue();
        
		//search report file custom for the client on the first path
        fileName = reportFilePath + processValue + "_" + client + ".rpt";
		File file = new File(fileName);

		if (! file.exists() )
		{
			//search file generic report
			fileName = reportFilePath + processValue + ".rpt";
			file = new File(fileName);
		}

		//Path 2
		//search report file custom for the client on the second path
		if (! file.exists() )
		{
	       reportFilePath = PgiUtil.getSolsticeParameter(Env.getCtx(), "CrystalReportShareDirectory02");
	        if ( ! reportFilePath.endsWith("\\"))
	        	reportFilePath = reportFilePath + "\\";

	        fileName = reportFilePath + processValue + "_" + client + ".rpt";
			file = new File(fileName);

			if (! file.exists() )
			{
				fileName = reportFilePath + processValue + ".rpt";
				file = new File(fileName);
			}

			// Path 3
			//search report file custom for the client on the path 3
			if (! file.exists() )
			{
		       reportFilePath = PgiUtil.getSolsticeParameter(Env.getCtx(), "CrystalReportShareDirectory03");
		        if ( ! reportFilePath.endsWith("\\"))
		        	reportFilePath = reportFilePath + "\\";

		        fileName = reportFilePath + processValue + "_" + client + ".rpt";
				file = new File(fileName);

				if (! file.exists() )
				{
					fileName = reportFilePath + processValue + ".rpt";
					file = new File(fileName);
				}
			}
		}
				
        
//        reportFilePath = reportFilePath + "LstFeuillesManquantesV2008.rpt";
//        reportFilePath = reportFilePath + "LstFeuillesManquantesVXI.rpt";
        
		log.info(" Call report : " + fileName);
        

        // Create a new client document and use it to open the desired report.
        reportClientDocument = new ReportClientDocument ();
        reportClientDocument.setReportAppServer(ReportClientDocument.inprocConnectionString);
        reportClientDocument.open (fileName, OpenReportOptions._openAsReadOnly);

        if (reportClientDocument == null) 
        {
            // Determine the report to display using a file chooser
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle ("Select a report to display");
            fileChooser.setFileFilter(new FileFilter() {

                @Override
                public boolean accept (File f)
                {
                    if (f != null) {
                        if (f.isDirectory()) {
                            return true;
                        }
                        return f.getName ().endsWith (".rpt");
                    }
                    return false;
                }

                @Override
                public String getDescription ()
                {
                    return "Crystal Reports (*.rpt)";
                }
            });

            int returnVal = fileChooser.showOpenDialog(CrystalViewerFrame.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                reportFilePath = fileChooser.getSelectedFile().getAbsolutePath ();
               
               // Create a new client document and use it to open the desired report.
               reportClientDocument = new ReportClientDocument ();
               reportClientDocument.setReportAppServer(ReportClientDocument.inprocConnectionString);
               reportClientDocument.open (reportFilePath, OpenReportOptions._openAsReadOnly);
            }
        }
    	
    }

    /**
     * Determine which report to display, and use this to set the reportClientDocument field.
     * @throws ReportSDKException if there is a problem opening the specified document. 
     */
    private void loadReport () throws ReportSDKException
    {
        if (reportClientDocument == null) 
        {
            // Determine the report to display using a file chooser
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle ("Select a report to display");
            fileChooser.setFileFilter(new FileFilter() {

                @Override
                public boolean accept (File f)
                {
                    if (f != null) {
                        if (f.isDirectory()) {
                            return true;
                        }
                        return f.getName ().endsWith (".rpt");
                    }
                    return false;
                }

                @Override
                public String getDescription ()
                {
                    return "Crystal Reports (*.rpt)";
                }
            });

            int returnVal = fileChooser.showOpenDialog(CrystalViewerFrame.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String reportFilePath = fileChooser.getSelectedFile().getAbsolutePath ();
               
               // Create a new client document and use it to open the desired report.
               reportClientDocument = new ReportClientDocument ();
               reportClientDocument.setReportAppServer(ReportClientDocument.inprocConnectionString);
               reportClientDocument.open (reportFilePath, OpenReportOptions._openAsReadOnly);
            }
        }
    }
    
    
    /**
     * Set the database logon associated with the report document.
     * @throws ReportSDKException if there is a problem setting the database logon. 
     */
    private void setDatabaseLogon () throws ReportSDKException
    {
        // TODO Set up database logon here to have the report log onto the
    	// data sources defined in the report automatically, without prompting the
    	// user.  For more information about this feature, refer to the documentation.
    	// For example:
    	//  CRJavaHelper.logonDataSource (reportClientDocument, "username", "password");
    	// will log onto the data sources defined in the report with username "username"
    	// and password "password".

        // jdbc:sqlserver://PGI0133\SQLEXPRESS:1142;databaseName=solstice
        DB.getDatabase();
/*        
        String connectionURL = "jdbc:sqlserver://PGI0133\\SQLEXPRESS:1142;databaseName=solstice";
        String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String jndiName = "";
*/        

		MSystem system = MSystem.get(Env.getCtx());
//		DB.getDatabase().getDriver().getClass()
		CConnection cc = CConnection.get();
//        String connectionURL = "jdbc:jtds:sqlserver://Solstice:1433/solstice;instance=SQLEXPRESS" ; //system.getDBAddress(true);
        String driverName = "net.sourceforge.jtds.jdbc.Driver";
		String connectionURL = cc.getConnectionURL();
		try
        {
			driverName = DB.getDatabase().getDriver().getClass().getName();
        }
        catch (Exception e)
        {
			log.log (Level.SEVERE, "setDatabaseLogon", e);
        }

        String jndiName = "";

//        DB_SqlServer[jdbc:jtds:sqlserver://Solstice:1433/solstice]
        	
		String userName = CConnection.get().getDbUid(); 
		String password = CConnection.get().getDbPwd();
        
        CRJavaHelper.changeDataSource(reportClientDocument, userName, password, connectionURL, driverName, jndiName);
    	CRJavaHelper.logonDataSource (reportClientDocument, userName, password);
    }
    
    /**
     * Set values for parameter fields in the report document.
     * @throws ReportSDKException if there is a problem setting parameter field values. 
     */
    private void setParameterFieldValues () throws ReportSDKException
    {
    	// TODO Populate the parameter fields in the report document here, to
    	// view the report without prompting the user for parameter values.  For
    	// more information about this feature, refer to the documentation.
    	// For example:
        //  CRJavaHelper.addDiscreteParameterValue(reportClientDocument, "subreportName", 
    	//			"parameterName", newValue);
    	// will populate the discrete parameter "parameterName" under the subreport
    	// "subreportName" with a new value (newValue).  To set a parameter in the main
    	// report, use an empty string ("") instead of "subreportName".
    	
//		boolean existOrg = false;
    	if ( processInfoParameter == null)	return;
    	boolean existOrg = false;

		CRJavaHelper.addDiscreteParameterValue( reportClientDocument, "", "AD_PInstance_ID", instance.getAD_PInstance_ID() );
		CRJavaHelper.addDiscreteParameterValue( reportClientDocument, "", "AfficherParametre", "Y" );

		for (int i = 0; i < processInfoParameter.length; i++)
		{
			String parameterName = processInfoParameter[i].getParameterName();

			if (parameterName.trim().equals("AD_Org_ID"))
			{
				System.out.println( processInfoParameter[i].getParameterAsInt() );
				existOrg = true;
			}

			//processInfoParameter[i].getParameter().getClass() == Timestamp.class 
			if ( processInfoParameter[i].getParameter() != null  )
			{
					CRJavaHelper.addDiscreteParameterValue( reportClientDocument, "", parameterName, processInfoParameter[i].getParameter() );
			}
		}

		if ( existOrg == false )
		{
	        int l_Role = Env.getAD_Role_ID(Env.getCtx());

			String rep = this.getOrgId( l_Role );
			String[] array = rep.split(":");
			for(int q = 0; q < array.length; q++)
			{
				CRJavaHelper.addDiscreteParameterValue( reportClientDocument, "", "AD_Org_ID", new BigDecimal(array[q] ) );
			}
		}
		
		String sql
				= "select *"
					 + " from AD_Process_Para"
					 + " where AD_Process_ID = " + process.getAD_Process_ID()
					 + " and Not exists( select 1 from AD_PINSTANCE_PARA WHERE AD_PINSTANCE_ID = " + instance.getAD_PInstance_ID() + " and AD_PINSTANCE_PARA.Parametername = AD_Process_Para.ColumnName ) "
					 + "  and AD_PROCESS_PARA.COLUMNNAME not in ( 'CrystalFormat', 'AD_PInstance_ID', 'AfficherParametre' ) "
					 + " order by seqNo";
			try 
			{	

				PreparedStatement stmt = DB.prepareStatement(sql, null);
   				ResultSet rs = stmt.executeQuery();
   				while(rs.next())
   				{
					MProcessPara para = MProcessPara.get(Env.getCtx(), rs.getInt("AD_Process_Para_ID"));
					if ( para.getAD_Reference_ID() ==  DisplayType.TableDir || 
						 para.getAD_Reference_ID() ==  DisplayType.Table    ||
						 para.getAD_Reference_ID() ==  DisplayType.Search   ||
						 para.getAD_Reference_ID() ==  DisplayType.Amount   || 
						 para.getAD_Reference_ID() ==  DisplayType.Integer  ||  
						 para.getAD_Reference_ID() ==  DisplayType.ID  )
						CRJavaHelper.addDiscreteParameterValue( reportClientDocument, "", rs.getString("ColumnName"), 0 );
					else if ( para.getAD_Reference_ID() ==  DisplayType.List || 
							  para.getAD_Reference_ID() ==  DisplayType.String ||
							  para.getAD_Reference_ID() ==  DisplayType.Text
							)
						CRJavaHelper.addDiscreteParameterValue( reportClientDocument, "", rs.getString("ColumnName"), " " );
					else if ( para.getAD_Reference_ID() ==  DisplayType.Date ||
							  para.getAD_Reference_ID() ==  DisplayType.DateTime
							)
					{
						Date dateParamVal = TimeUtil.getCalendar( TimeUtil.getDay(1900, 01, 01)).getTime();
						CRJavaHelper.addDiscreteParameterValue( reportClientDocument, "", rs.getString("ColumnName"), dateParamVal );
					}
					else
						CRJavaHelper.addDiscreteParameterValue( reportClientDocument, "", rs.getString("ColumnName"), new Object() );

				}
			} 
			catch (Exception e) 
			{
				System.out.println(e);
	            log.log (Level.SEVERE, "Problem showing report "  , e);
			}

		// 2012.03.29
		// Ajoute les param�tres qui serait ajout� par un autre processus et non demand� � Usager.
		// Ex Transfere bancaire.
			
		sql	= "select *"
				 + " from AD_PINSTANCE_PARA"
				 + " WHERE AD_PINSTANCE_ID = " + instance.getAD_PInstance_ID()
				 + " and Not exists( select 1 from AD_Process_Para WHERE AD_Process_ID = " + process.getAD_Process_ID() + " and AD_PINSTANCE_PARA.Parametername = AD_Process_Para.ColumnName ) "
				 ;
		try 
		{	

			PreparedStatement stmt = DB.prepareStatement(sql, null);
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				if ( rs.getString( "P_String") != null )
					CRJavaHelper.addDiscreteParameterValue( reportClientDocument, "", rs.getString("Parametername"), rs.getString( "P_String") );
				else if ( rs.getObject( "P_Number") != null )
					CRJavaHelper.addDiscreteParameterValue( reportClientDocument, "", rs.getString("Parametername"), rs.getInt( "P_Number") );
				else if ( rs.getObject( "P_Date") != null )
					CRJavaHelper.addDiscreteParameterValue( reportClientDocument, "", rs.getString("Parametername"), rs.getObject( "P_Date") );
			}
		} 
		catch (Exception e) 
		{
			System.out.println(e);
            log.log (Level.SEVERE, "Problem showing report "  , e);
		}
		
/*		//2023-06-26 ajout la gestion des notes.
        process = new MProcess( Env.getCtx(), instance.getAD_Process_ID(), null);
        if ( process.getValue().startsWith("lstAvisDepotBancaire")   )
        {
        	String s_note = "";
        	P_Statement_Earning Statement_Earning = P_Statement_Earning.get(Env.getCtx(),  )
        	P_Notice_Bank_Deposit note = P_Notice_Bank_Deposit.get(Env.getCtx(), Statement_Earning.getP_Period_ID(), Statement_Earning.getAD_Org_ID(), Statement_Earning.getP_Department_ID(), Statement_Earning.getC_Activity_ID(), Statement_Earning.getP_Employee_ID(), trxName);
        	if ( note != null )
        		s_note = note.getNotice();
        	if ( s_note == null) s_note =  "";
          	
        	Object [] Note = { s_note };
			CRJavaHelper.addDiscreteParameterValue( reportClientDocument, "", "Note", Note );

        }
*/			

    }
    
    /**
     * Bind the report document to the report viewer so that the report is displayed.
     */
    private void setReportSource () throws ReportSDKException
    {
        invokeViewerMethodWithArg(reportViewer, "setReportSource", reportClientDocument.getReportSource ());
    }

    private String getOrgId(int ADRoleID)
	{
		String Chaine = "";
		try 
		{
    		
			String sql
				= "select AD_ORG_ID from AD_Role_OrgAccess"
					 + " where AD_Role_ID = " + ADRoleID
					 + " and isActive = 'Y'"
					 ;
			
			PreparedStatement stmt = DB.prepareStatement(sql, null);
				ResultSet rs = stmt.executeQuery();
				while(rs.next())
				{
					Chaine += rs.getBigDecimal("AD_ORG_ID").toString() + ":";
				}
			}
		catch (Exception e) 
		{
			System.out.println(e);
            log.log (Level.SEVERE, "Problem showing report "  , e);

		}
			return Chaine;		
	}

}
