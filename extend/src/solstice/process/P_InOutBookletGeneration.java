/*
 * Created on 25 août 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;

import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

import solstice.model.P_Time_Sheet;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_InOutBookletGeneration extends SvrProcess
{
	private int Org_ID = 0;
	private int Activity_ID = 0;

	private int employee_ID = 0;
	private int payment_Group_ID = 0;
	private int period_ID = 0;
	private int frequency_ID = 0;
	private int P_Distribution_Booklet_ID = 0;
	private int P_Collective_Labour_Agr_ID = 0;
    private boolean IsRework = false;
    private int threadCount = 0;
    private boolean multiThread = true;


	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("P_Employee_ID"))
				employee_ID = para[i].getParameterAsInt();			
			else if (name.equals("P_Payment_Group_ID"))
				payment_Group_ID = para[i].getParameterAsInt();
			else if (name.equals("AD_Org_ID"))
				Org_ID = para[i].getParameterAsInt();
			else if (name.equals("C_Activity_ID"))
				Activity_ID = para[i].getParameterAsInt();
			else if (name.equals("P_Frequency_ID"))
				frequency_ID = para[i].getParameterAsInt();
			else if (name.equals("P_Period_ID"))
				period_ID = para[i].getParameterAsInt();
			else if ( name.equals("P_Distribution_Booklet_ID"))
				P_Distribution_Booklet_ID = para[i].getParameterAsInt();
			else if (name.equals("Rework"))
			    IsRework = "Y".equals(para[i].getParameter());
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
		/** Transaction			*/
		String trxName = null; // Trx.createTrxName();
		String ret = null;
		
		
		TimeGeneration bookletGeneration = new TimeGeneration( Env.getCtx() , 'B', IsRework, false, P_Time_Sheet.SHEETTYPE_Regular);
		bookletGeneration.setMultiThread(multiThread);
		if (threadCount > 0)
			bookletGeneration.setThreadCount(threadCount);
		
		ret = bookletGeneration.Generation( getCtx(), this.getAD_Client_ID(), Org_ID, Activity_ID, this.employee_ID, this.payment_Group_ID, this.period_ID, P_Distribution_Booklet_ID, P_Collective_Labour_Agr_ID, 0, trxName);

//		DB.commit( true, trxName );

		return ret;

	}
}
