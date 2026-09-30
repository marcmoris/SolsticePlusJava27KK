/*
 * Created on 2004-10-04
 *
 */
package solstice.process;

import java.util.logging.Level;

//import org.compiere.interfaces.Server;
import org.compiere.process.*;
import org.compiere.util.DB;
import org.compiere.util.Env;


/**
 * @author Progestion Information
 *
 * Time Generation
 * 
 * 
 *  
 */
public class P_InOutTimeGeneration extends SvrProcess {

	private int Org_ID = 0;
	private int Activity_ID = 0;
	
	private int Employee_ID = 0;
	private int Payment_Group_ID = 0;
	private int Period_ID = 0;
	private int Frequency_ID = 0;
	private int P_Distribution_Booklet_ID = 0;
	private int P_Time_Sheet_ID = 0;
	private int P_Collective_Labour_Agr_ID = 0;
    private boolean IsRework = false;
    private boolean IsPlanifico = false;
    private int threadCount = 0;
    private boolean multiThread = true;
    
	private String sheetType;
    
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("P_Employee_ID"))
				Employee_ID = para[i].getParameterAsInt();
			else if (name.equals("P_Payment_Group_ID"))
				Payment_Group_ID = para[i].getParameterAsInt();
			else if (name.equals("P_Period_ID"))
				Period_ID = para[i].getParameterAsInt();
			else if (name.equals("AD_Org_ID"))
				Org_ID = para[i].getParameterAsInt();
			else if (name.equals("C_Activity_ID"))
				Activity_ID = para[i].getParameterAsInt();
			else if ( name.equals("P_Distribution_Booklet_ID"))
				P_Distribution_Booklet_ID = para[i].getParameterAsInt();
			else if ( name.equals("P_Frequency_ID"))
				Frequency_ID = para[i].getParameterAsInt();
			else if (name.equals("P_Time_Sheet_ID"))
				P_Time_Sheet_ID = para[i].getParameterAsInt();
			else if (name.equals("P_Collective_Labour_Agr_ID"))
				P_Collective_Labour_Agr_ID = para[i].getParameterAsInt();
			else if (name.equals("Rework"))
			    IsRework = "Y".equals(para[i].getParameter());
			else if (name.equals("Planifico"))
				IsPlanifico = "Y".equals(para[i].getParameter());
			else if (name.equals("SheetType"))
				sheetType = (String)para[i].getParameter();
			else if (name.equalsIgnoreCase("ThreadCount") || name.equalsIgnoreCase("nbrThread"))
				threadCount = para[i].getParameterAsInt();
			else if (name.equalsIgnoreCase("MultiThread"))
				multiThread = "Y".equals(para[i].getParameter());
			else
				log.log (Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 * 	Read each time sheet 
	 *	@return 
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		
		String retValue;
/*		
		boolean retValue = false;
		Server server = CConnection.get().getServer();
		if (server != null)
		{
			//on a surchargé la fonction postimmediate pour lui ajouter un boolean
			//a la fin. Vrai = Provient de la paie.
			//on prend le premier parametre, si on provient de la paie pis on s'en sert 
			//pour indiquer quel fonction appelé. 1 - TimeGeneration
			retValue = server.postImmediate(1, 1, getAD_PInstance_ID(), false, true);
		}

		return retValue == true ? "" : "error";
		*/
		
		/** Transaction			*/
		String trxName = null; // Trx.createTrxName();

		TimeGeneration TimeGen = new TimeGeneration( Env.getCtx() , 'S', IsRework, IsPlanifico, sheetType);
		TimeGen.setMultiThread(multiThread);
		if (threadCount > 0)
			TimeGen.setThreadCount(threadCount);
		
		retValue = TimeGen.Generation( getCtx(), this.getAD_Client_ID(), Org_ID, Activity_ID, Employee_ID, Payment_Group_ID,  Period_ID, P_Distribution_Booklet_ID, P_Collective_Labour_Agr_ID, P_Time_Sheet_ID, trxName);

		DB.commit( true, trxName );

		if (Period_ID > 0)
		{
			int errCount = DB.getSQLValue(null, "SELECT COUNT(1) FROM P_Time_Sheet_Error err INNER JOIN P_Time_Sheet ts ON (err.P_Time_Sheet_ID = ts.P_Time_Sheet_ID) WHERE ts.P_Period_ID = ?", Period_ID);
			if (errCount > 0)
			{
				retValue += " (" + errCount + " messages / erreurs d\u00e9tect\u00e9s)";
			}
		}

		return retValue;

	}
	
}
