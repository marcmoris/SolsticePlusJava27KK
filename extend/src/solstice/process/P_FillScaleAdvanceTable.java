/*
 * Created on 2023-05-16
 */
package solstice.process;



import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;


/** 
 * @author Marc Morissette
 * Ce processus sert à remplir la table P_Employee_Scale_Advance
 */
public class P_FillScaleAdvanceTable extends SvrProcess
{
    public P_FillScaleAdvanceTable()
    {
        super();
    }

  //  private int Period_ID = 0;
  //  private P_Period Period = null;
    
    /**
     * Durant le traitement de la procédure, on utilise le champ P_Assignment.ExpectedAdvanceDate. On
     * doit s'assurer ici que ce champ est populé. La première fois que la procédure est lancée, le
     * traitement est plus long, mais à partir du moment qu'une majorité 
     */
    protected void prepare()
    {
    	
/*
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("P_Period_ID"))
				Period_ID = para[i].getParameterAsInt();
			else
				log.log (Level.SEVERE,"prepare - Unknown Parameter: " + name);
*/				
		}

     /**
     * On doit sortir la liste des employés qui peuvent monter
     * d'échellon et les ajouter dans la table P_Employee_Scale_Advance
     */
    protected String doIt() throws Exception
    {
    	
        DB.executeUpdate("EXEC SP_EMPLOYEE_SCALE_ADVANCE ", null);
            
        return Msg.translate(Env.getLanguage(Env.getCtx()), "Success");
    }
    
    
}
