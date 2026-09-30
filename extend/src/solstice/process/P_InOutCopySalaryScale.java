/*
 * Created on 19-Jun-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;

import org.compiere.process.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import solstice.model.P_Salary_Scale;
import java.util.logging.*;
/**
 * @author Propriétaire
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_InOutCopySalaryScale extends SvrProcess 
{
	int Salary_Scale_ID; 
	Timestamp EffectIn;
	Timestamp ScaleDate;
	BigDecimal MultiplyRate;
	
	protected void prepare()
	{
		Salary_Scale_ID = this.getRecord_ID();

		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("EffectIn"))
				EffectIn = (Timestamp)para[i].getParameter();
			else if (name.equals("ScaleDate"))
				ScaleDate = (Timestamp)para[i].getParameter();
			else if (name.equals("MultiplyRate"))
				MultiplyRate = (BigDecimal)para[i].getParameter();
			else
				log.log (Level.SEVERE,"prepare - Unknown Parameter: " + name);
		}
	}	//	prepare

	protected String doIt () throws Exception
	{
		String trxName = null; //Trx.createTrxName();
		P_Salary_Scale SalaryScale = P_Salary_Scale.get( getCtx(), Salary_Scale_ID, trxName );
		SalaryScale.copy( getCtx(), ScaleDate, MultiplyRate, EffectIn, trxName);
		return "Copie effectuer.";
	}
		
		
}
