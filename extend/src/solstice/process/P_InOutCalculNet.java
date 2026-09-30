/******************************************************************************
 * 
 *  A FAIRE.
 *     Calcul des avances au Net.
 *     calcul par périod d'origine. 
 *     Prime automatique 
 *     revoir tout les ici avec les formule 
 *
 *****************************************************************************/
package solstice.process;

import solstice.model.P_Employee;
import solstice.model.P_Payment;
import solstice.model.P_Period;

//import org.compiere.interfaces.Server;
import org.compiere.process.*;

import java.util.logging.*;
 
/**
 *  Calcul of the Net
 *	
 *  @author Progestion Informatique
 *
 */
public class P_InOutCalculNet extends SvrProcess
{
	
	/**	Payment					*/
	private int Payment_ID = 0;
	private int Employee_ID = 0;
	private int Payment_Group_ID = 0;
	private int Frequency_ID = 0;
	private int Period_ID = 0;
    private boolean IsRework = false;

	private P_Period    TimeSheetPeriod;
	private P_Employee  Employee;
	private P_Payment   Payment;


	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("P_Payment_ID"))
				Payment_ID = para[i].getParameterAsInt();
			else if (name.equals("P_Employee_ID"))
				Employee_ID = para[i].getParameterAsInt();
			else if (name.equals("P_Payment_Group_ID"))
				Payment_Group_ID = para[i].getParameterAsInt();
			else if (name.equals("P_Frequency_ID"))
				Frequency_ID = para[i].getParameterAsInt();
			else if (name.equals("P_Period_ID"))
				Period_ID = para[i].getParameterAsInt();
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
		
		//
		
		boolean retValue = false;
		//Server server = CConnection.get().getServer();
		//if (server != null)
		{
			//on a surchargé la fonction postimmediate pour lui ajouter un boolean
			//a la fin. Vrai = Provient de la paie.
			//on prend le premier parametre, si on provient de la paie pis on s'en sert 
			//pour indiquer quel fonction appelé. 3 - CalculNet
// ICI			retValue = server.postImmediate(3, 1, getAD_PInstance_ID(), false, true);
		}

		return retValue == true ? "" : "error";
		
	}	//	InOutCalculNet
	

}	//	InOutCalculNet

