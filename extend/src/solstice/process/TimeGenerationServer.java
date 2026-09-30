package solstice.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.model.MClient;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.ProcessInfoUtil;
import org.compiere.util.CLogMgt;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.MPayProcessorV2;
import solstice.model.P_Period;
import solstice.model.P_Time_Sheet;
import solstice.model.X_P_PayProcessorV2_Para;
import solstice.process.TimeGeneration;

public class TimeGenerationServer extends Thread {

	private int Org_ID = 0;
	private int Activity_ID = 0;
	
	private int Employee_ID = 0;
	private int Payment_Group_ID = 0;
	private int Period_ID = 0;
	private int Frequency_ID = 0;
	private int Distribution_Booklet_ID = 0;
	private int P_Collective_Labour_Agr_ID = 0;
	private int Time_Sheet_ID = 0;
    private boolean IsRework = false;
	
    private Thread t;
    private String threadName;
    
    
	/**	The Concrete Model			*/
	private MPayProcessorV2		m_model = null;
	/** Client info					*/
	private MClient 			m_client = null;
    
	private static CLogger	s_log	= CLogger.getCLogger (TimeGenerationServer.class);

	private ProcessInfo			m_pi;
	
    public void run() {
		
    	CLogMgt.setLevel(Level.INFO);
    	
		Env.setContext( Env.getCtx(), "#AD_Client_ID", 1000004);
		Env.setContext( Env.getCtx(), "#AD_Org_ID", 0);

//		m_client = MClient.get(Env.getCtx(), m_model.getAD_Client_ID());
		m_client = MClient.get(Env.getCtx(), 1000004);
		
/*		int Org_ID = 0;
		int Activity_ID = 0;

		int Employee_ID = 0;
		int Payment_Group_ID = 0;

		P_Period Period = P_Period.getOpenPeriod( Env.getCtx(), null);
		int Period_ID = Period.getP_Period_ID();
		
		s_log.log(Level.SEVERE, "DEBUG TimeValidationServer.RUN Period_ID "  +  Period_ID );

		
		int Frequency_ID = Period.getP_Frequency_ID();

		int Time_Sheet_ID = 0; //rs.getInt("P_Time_Sheet_ID");
		
		int Team_ID = 0;
		
		boolean IsRework = true;
*/		
/*		
		String sql = "SELECT * FROM P_PayProcessorV2_Para ";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next())
			{
				
				X_P_PayProcessorV2_Para Param = new X_P_PayProcessorV2_Para( Env.getCtx(), rs, null);

				m_pi = new ProcessInfo (Param.getTitle(), Param.getAD_Process_ID() );
				m_pi.setAD_PInstance_ID( Param.getAD_PInstance_ID() );
				
			
				Param.delete(true);
				

				ProcessInfoParameter[] para = getParameter();
				for (int i = 0; i < para.length; i++)
				{
					String name = para[i].getParameterName();
					
					s_log.log(Level.SEVERE, "DEBUG TimeValidationServer.RUN Read Param "  +  name );

					
					if (para[i].getParameter() == null)
						;
					else if (name.equals("P_Employee_ID"))
						Employee_ID = para[i].getParameterAsInt();
					else if (name.equals("P_Time_Sheet_ID"))
						Time_Sheet_ID = para[i].getParameterAsInt();
					else if (name.equals("P_Payment_Group_ID"))
						Payment_Group_ID = para[i].getParameterAsInt();
					else if (name.equals("AD_Org_ID"))
						Org_ID = para[i].getParameterAsInt();
					else if (name.equals("C_Activity_ID"))
						Activity_ID = para[i].getParameterAsInt();
					else if (name.equals("P_Frequency_ID"))
						Frequency_ID = para[i].getParameterAsInt();
					else if (name.equals("P_Period_ID"))
						Period_ID = para[i].getParameterAsInt();
					else if ( name.equals("P_Distribution_Booklet_ID"))
						Team_ID = para[i].getParameterAsInt();
					else if (name.equals("Rework"))
					    IsRework = "Y".equals(para[i].getParameter());
//					else
//						log.log (Level.SEVERE,"prepare - Unknown Parameter: " + name);
				}

			}
			
			s_log.log(Level.SEVERE, "DEBUG TimeValidationServer.RUN Period_ID 2 "  +  Period_ID );
			
			rs.close();
			pstmt.close();
		} catch (Exception e) {
		    System.out.println(" Read AD_PInstance_ID : " + e);
		}	
*/		         
		
		while ( ! m_model.m_stack.empty() )
		{

			// Traitement d'un livret de temps a la fois.
			String s_Employee_ID = m_model.m_stack.pop();
			
//			s_log.log(Level.INFO, "DEBUG TimeValidationServer.RUN s_Employee_ID "  +  s_Employee_ID );

			
			if ( s_Employee_ID != null)
			{
	            System.out.println("DEBUG TimeValidationServer.RUN s_Employee_ID "  +  s_Employee_ID );
	            
					Employee_ID = Integer.parseInt( s_Employee_ID );
//		            System.out.println("Thread " +  threadName + " employee = " + Employee_ID);
					
		            TimeGeneration TimeGen = new TimeGeneration( Env.getCtx() , 'S', IsRework, false, P_Time_Sheet.SHEETTYPE_Regular);
					try
					{
						
						String ret = TimeGen.Generation( Env.getCtx(), m_model.getAD_Client_ID(), Org_ID, Activity_ID, Employee_ID, Payment_Group_ID,  Period_ID, Distribution_Booklet_ID, P_Collective_Labour_Agr_ID, Time_Sheet_ID, null);
						
					} catch (Exception e) {
				        System.out.println("Thread " +  threadName + " interrupted.");
 						String description = "Génération du temps interrompu. ";
 						m_client.createEMail("support@solsticeplus.com", "Génération du temps interrompu", description);
			        }				
			}
		}
		System.out.println("Thread " +  threadName + " exiting.");
	   
    }
	
	protected ProcessInfoParameter[] getParameter()
	{
		ProcessInfoParameter[] retValue = m_pi.getParameter();
		if (retValue == null)
		{
			ProcessInfoUtil.setParameterFromDB(m_pi);
			retValue = m_pi.getParameter();
		}
		return retValue;
	}	//	getParameter

    
	

	   
	public TimeGenerationServer (MPayProcessorV2 model,  String name, int ID,  int _Org_ID, int _Activity_ID, int _Employee_ID, int _Payment_Group_ID, int _Period_ID, int _Frequency_ID, int _P_Distribution_Booklet_ID, int _P_Time_Sheet_ID, boolean _IsRework )
	{
//		super (model, 30);	//	30 seconds delay
		threadName = name;
		m_model = model;
			

		Org_ID = _Org_ID;
		Activity_ID = _Activity_ID;
		
		Employee_ID = _Employee_ID;
		Payment_Group_ID = _Payment_Group_ID;
		Period_ID = _Period_ID;
		Frequency_ID = _Frequency_ID;
		Distribution_Booklet_ID = _P_Distribution_Booklet_ID;
		Time_Sheet_ID = _P_Time_Sheet_ID;
	    IsRework = _IsRework;
		
      	System.out.println("Creating " +  threadName );
	      	
			
	}	//	PayProcessor
	  

	
	public void start () {
	     System.out.println("Starting " +  threadName );
	     if (t == null) {
	    	 
	        t = new Thread (this, threadName);
	        t.start ();
	     }
	}
	
	
}


