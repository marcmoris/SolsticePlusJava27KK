package solstice.process;

import org.compiere.process.SvrProcess;
//import org.compiere.server.CompiereServerMgr;
import org.compiere.util.DB;

public class CalculPayroll extends SvrProcess {

	protected void prepare()
	{
	
	}	//	prepare

	
	/**
	 */
	
	/**	The Server			*/
//	private CompiereServerMgr	m_serverMgr = null;

	protected String doIt () throws Exception
	{
		String trxName = "CalculPayroll";
		
		DB.executeUpdate("UPDATE P_Solstice_Parameters set Parameter = 'True' where value = 'CalculPayroll'", null);
/*
		String serverID = "1000005";
		if (serverID == null || serverID.length() == 0)
	 		return "Server not found: ";
		
		log.info ("ServerID=" + serverID);
		CompiereServer server = m_serverMgr.getServer(serverID);
		if (server == null)
		{
			return "Server not found: " + serverID;
		}
		//
		server.runNow();

*/		
		return "Vous allez recevoir un courriel lorsque le calcul sera terminé";
	}
	
}
