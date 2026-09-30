package solstice.process;

import org.compiere.util.Env;
import org.compiere.process.*;
import solstice.model.P_Employee_Carra;

public class P_InOutCallCalculFE extends SvrProcess
{

	int P_Employee_Carra_ID = 0;
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		
		P_Employee_Carra_ID = this.getRecord_ID();

	}	//	prepare

	
	/**
	 * 	Read each time sheet 
	 *	@return 
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		P_Employee_Carra calcul = P_Employee_Carra.get( Env.getCtx(), P_Employee_Carra_ID, null );
		calcul.calcul_fe( );
		calcul.save();
	    return "Calcul terminer.";
	}	
}

