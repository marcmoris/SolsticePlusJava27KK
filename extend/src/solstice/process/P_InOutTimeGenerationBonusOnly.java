package solstice.process;

import java.util.logging.Level;

//import org.compiere.interfaces.Server;
import org.compiere.process.*;
import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.P_Time_Sheet;


/**
 * @author Progestion Information
 *
 * Time Generation
 * 
 * 
 *  
 */
public class P_InOutTimeGenerationBonusOnly extends SvrProcess {

	private int P_Time_Sheet_ID = 0;
    private boolean IsRework = false;

	private String sheetType;
    
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			if (name.equals("P_Time_Sheet_ID"))
				P_Time_Sheet_ID = para[i].getParameterAsInt();
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
		
		sheetType = P_Time_Sheet.SHEETTYPE_Complementary;

		TimeGeneration TimeGen = new TimeGeneration( Env.getCtx() , 'S', IsRework, false, sheetType);
		
		retValue = TimeGen.GenerationBonusOnly( getCtx(),  P_Time_Sheet_ID, trxName);

		DB.commit( true, trxName );

		return retValue;

	}
	
}
