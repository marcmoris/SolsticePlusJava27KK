/*
 * Created on 2005-06-28
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;


import java.util.Hashtable;
import java.util.logging.Level;

//import org.compiere.interfaces.Server;
import org.compiere.model.MSession;
import org.compiere.process.*;
import org.compiere.util.CacheMgt;
import org.compiere.util.Env;
import solstice.model.P_Employee;

import solstice.model.P_Period;
import solstice.model.P_Time_Sheet;

/**
 * @author kevmar01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_inOutTimeValidation extends SvrProcess 
{
	private int Org_ID = 0;
	private int Activity_ID = 0;

	private int Employee_ID = 0;
	private int Time_Sheet_ID = 0;
	private int Frequency_ID = 0;
	private int Period_ID = 0;
	private int Payment_Group_ID = 0;
    private int P_Distribution_Booklet_ID = 0;
    private int P_Collective_Labour_Agr_ID = 0;
    
    private int	m_inserted = 0;
 	
	private P_Period Period;
	private P_Employee Employee;
//	private P_Assignment Assignment;
	private P_Time_Sheet TimeSheet;
    private boolean IsRework = false;

	private Hashtable 	s_GenTime = new Hashtable();	//	

	/**	Big Decimal 0	 */
	static final public java.math.BigDecimal ZERO = new java.math.BigDecimal(0.0);

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
				P_Distribution_Booklet_ID = para[i].getParameterAsInt();
			else if ( name.equals("P_Collective_Labour_Agr_ID"))
				P_Collective_Labour_Agr_ID = para[i].getParameterAsInt();
			
						
			else if (name.equals("Rework"))
			    IsRework = "Y".equals(para[i].getParameter());
			else
				log.log (Level.SEVERE,"prepare - Unknown Parameter: " + name);
		}
	}	//	prepare

	
	/**
	 * 	Read each time sheet 
	 *	@return 
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		boolean retValue = true;
		String ret = "";
		
		int AD_Session_ID = Env.getContextAsInt(Env.getCtx(), "#AD_Session_ID");

		/** Transaction			*/
/*		CacheMgt.get().reset();
		
		MSession session = new MSession(Env.getCtx(), AD_Session_ID, this.get_TrxName() );		//	Start Session
		Env.setContext(Env.getCtx(), "#AD_Session_ID", session.getAD_Session_ID());
*/
		try {
			
			TimeValidation tmp = new TimeValidation( getCtx(), this.getAD_Client_ID(), Org_ID, Activity_ID, Employee_ID, Time_Sheet_ID, Payment_Group_ID, Frequency_ID, Period_ID, P_Distribution_Booklet_ID, P_Collective_Labour_Agr_ID, IsRework, null);
			ret = tmp.Validation();
		}
		catch( Exception e ) {
			retValue = false;
		}
/*		finally {
			CacheMgt.get().reset();
		}
*/	
		return retValue == true ? ret : "error";
		
	}	
}
