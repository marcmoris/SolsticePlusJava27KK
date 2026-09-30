package solstice.process;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
//import org.compiere.server.CompiereServerMgr;
import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.utils.PgiUtil;

public class CalculPayrollV2 extends SvrProcess {

	private boolean IsRework = false;
	private int Payment_Group_ID = 0;

	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("P_Payment_Group_ID"))
				Payment_Group_ID = para[i].getParameterAsInt();
			else if (name.equals("Rework"))
			    IsRework = "Y".equals(para[i].getParameter());
		
		}		
	
	}	//	prepare

	
	/**
	 */
	
	/**	The Server			*/
//	private CompiereServerMgr	m_serverMgr = null;

	protected String doIt () throws Exception
	{
		String trxName = "CalculPayroll";
		
		String calculPayroll = PgiUtil.getSolsticeParameter(Env.getCtx(), "CalculPayrollV2");
		if ( ! calculPayroll.equals("False")) 
		{
			return "*ATTENTION* Calcul en cours, refaire une demande de calcul dans quelques minutes. ";
		}

		DB.executeUpdate("UPDATE P_Solstice_Parameters set Parameter = " + Payment_Group_ID + " where value = 'CalculPayrollPayment_Group_ID'", null);

		if ( IsRework ) 
		{
			DB.executeUpdate("UPDATE P_Solstice_Parameters set Parameter = 'TrueTrue' where value = 'CalculPayrollV2'", null);
		}
		else
		{
			DB.executeUpdate("UPDATE P_Solstice_Parameters set Parameter = 'True' where value = 'CalculPayrollV2'", null);
		}
		

//		PgiUtil.setSolsticeParameter(Env.getCtx(), "CalculPayrollUser", Env.getAD_User_ID( Env.getCtx()));

		DB.executeUpdate("UPDATE P_Solstice_Parameters set Parameter = " + Env.getAD_User_ID( Env.getCtx()) + " where value = 'CalculPayrollUser'", null);

		    
		
		return "Vous allez recevoir un courriel lorsque le calcul sera terminé";
	}
	
}
