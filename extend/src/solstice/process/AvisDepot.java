/*
 * Created on 11 août 2005
 */
package solstice.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * @author frafor01
 */
public class AvisDepot extends SvrProcess
{
    private int employeeId;
    private int periodId;
	/**	Static Logger				*/

    public AvisDepot()
    {
        super();
    }

    protected void prepare()
    {
        this.employeeId = this.getRecord_ID();
        
	    ProcessInfoParameter[] parameters = this.getParameter();
	    if(!parameters[0].getParameterName().equals("P_Period_ID"))
	    	log.log (Level.SEVERE, "Le paramètre P_Period_ID ne semble pas exister");
	    this.periodId = parameters[0].getParameterAsInt();
    }

    protected String doIt() throws Exception
    {
    	
		int l_Role = Env.getAD_Role_ID(Env.getCtx());
        ReportLauncher.callReport( this.getAD_PInstance_ID(), l_Role, "PDF", getParameter() );
	    
        return null;
    }

}
