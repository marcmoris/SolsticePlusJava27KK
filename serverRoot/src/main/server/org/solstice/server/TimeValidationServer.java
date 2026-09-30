package org.solstice.server;

import java.util.Properties;
import java.util.Stack;
import java.util.logging.Level;

import org.compiere.model.MClient;
import org.compiere.util.CLogMgt;
import org.compiere.util.CLogger;
import org.compiere.util.Env;

import solstice.model.MPayProcessorV2;
import solstice.model.P_Employee;
import solstice.model.P_Payment_Group;
import solstice.model.P_Period;
import solstice.process.TimeValidation;
import solstice.utils.PgiUtil;

public class TimeValidationServer extends Thread {

	public Stack<String> m_stack;
	
	
    private Thread t;
    private String threadName;
    private String m_Distribution_Booklet_ID;
    
    
	/**	The Concrete Model			*/
	private MPayProcessorV2		m_model = null;
	/** Client info					*/
	private MClient 			m_client = null;
    
	private static CLogger	s_log	= CLogger.getCLogger (TimeValidationServer.class);

	Properties ctx = Env.getCtx();

	
    public void run() {
        System.out.println("Run");
		
    	CLogMgt.setLevel(Level.INFO);

		m_client = MClient.get(Env.getCtx(), m_model.getAD_Client_ID());

		
		
		int Org_ID = 0;
		int Activity_ID = 0;

		int Employee_ID = 0;
//		int Payment_Group_ID = 0;
		int Payment_Group_ID = Integer.parseInt( PgiUtil.getSolsticeParameter(Env.getCtx(), "CalculPayrollPayment_Group_ID") );
		
		P_Payment_Group Payment_Group = P_Payment_Group.get(Env.getCtx(),Payment_Group_ID, null );
		Env.setContext( ctx, "#AD_Client_ID", Payment_Group.getAD_Client_ID());
		Env.setContext( ctx, "#AD_Org_ID", Payment_Group.getAD_Org_ID());
		Env.setContext( ctx, "#AD_Role_ID", 1000027);  //Superviser
		
		Org_ID = Payment_Group.getAD_Org_ID();
		
		P_Period Period = P_Period.getOpenPeriod( Env.getCtx(), null);
		
		int Period_ID = Period.getP_Period_ID();
		
		int Frequency_ID = Period.getP_Frequency_ID();

		int Time_Sheet_ID = 0; //rs.getInt("P_Time_Sheet_ID");

//		boolean IsRework = false;
		boolean IsRework = true;
		
		while ( ! m_model.m_stack.empty() )
		{

			// Traitement d'un livret de temps a la fois.
			String s_Employee_ID = m_model.m_stack.pop();
			
			if ( s_Employee_ID != null)
			{
					int Team_ID = 0;
					int P_Collective_Labour_Agr_ID = 0;
					Employee_ID = Integer.parseInt( s_Employee_ID );

					
		            System.out.println("DEBUG TimeValidationServer Thread " +  threadName  ) ;
		            P_Employee Employee = P_Employee.get( Env.getCtx(), Employee_ID, null);
					System.out.println("DEBUG TimeValidationServer Employee " + Employee.toString()  );

					TimeValidation tmp = new TimeValidation( Env.getCtx(), m_model.getAD_Client_ID(), Org_ID, Activity_ID, Employee_ID, Time_Sheet_ID, Payment_Group_ID, Frequency_ID, Period_ID, Team_ID, P_Collective_Labour_Agr_ID, IsRework, null);

					try
					{
						
						String ret = tmp.Validation();
						
					} catch (Exception e) {
				        System.out.println("Thread " +  threadName + " interrupted.");
				         
 						String description = "Calcul de la rémunération interrompu. ";
						//1001140

						m_client.createEMail("support@solsticeplus.com", "Calcul de la rémunération interrompu", description);

			        }				
			}


		}
		

		System.out.println("TimeValidationServer Thread " +  threadName + " exiting.");
	   
    }
	
	
   
	public TimeValidationServer (MPayProcessorV2 model,  String name, int ID)
	{
//		super (model, 30);	//	30 seconds delay
		threadName = name;
		m_model = model;
			
      	System.out.println("TimeValidationServer Creating " +  threadName );
	      	
			
	}	//	PayProcessor
	  

	
	public void start () {
	     System.out.println("TimeValidationServer Starting " +  threadName );
	     if (t == null) {
	    	 
	        t = new Thread (this, threadName);
	        t.start ();
	     }
	}
	
	
}


