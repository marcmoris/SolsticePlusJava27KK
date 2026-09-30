/*
 * Created on Feb 13, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;

import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author rejgar01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_FillEmploymentEarning extends SvrProcess
{
	public int Period_Start_ID = 0;
	public int Period_End_ID = 0;
	public int Deduction_ID = 0;
	public int Instance_ID = 0;
	public String Establishment = "";
	public int User_ID = 0;
	public int Client_ID = 0;
	public int Org_ID = 0;
	private Properties m_ctx;
	
/*    public P_FillEmploymentEarning( Properties ctx)
    {
        m_ctx = ctx;
    }*/
	
	protected void prepare() 
	{
	    ProcessInfoParameter[] para = getParameter();
	    Instance_ID = this.getAD_PInstance_ID();
	    for(int i = 0; i < para.length; i++)
	    {
	    	String name = para[i].getParameterName();
	    	if (para[i].getParameter() == null)
			;
	        else if(para[i].getParameterName().equals("P_Period_Start_ID"))
	        	Period_Start_ID = para[i].getParameterAsInt();
	        else if(para[i].getParameterName().equals("P_Period_End_ID"))
	        	Period_End_ID = para[i].getParameterAsInt();
	        else if(para[i].getParameterName().equals("P_Deduction_ID"))
				Deduction_ID = para[i].getParameterAsInt();
	        else if (para[i].getParameterName().equals("EstablishmentNumber"))
	        	Establishment = String.valueOf(para[i].getParameter());
            else
	            log.log (Level.SEVERE,"prepare - Unknown Parameter: " + name);
	    }
	    User_ID = Env.getAD_User_ID(getCtx());
	    Client_ID = Env.getAD_Client_ID(getCtx());
	    Org_ID = Env.getAD_Org_ID(getCtx());
	}

	
	private String m_Format = "PDF";

	protected String doIt() throws Exception 
	{
		//
        // On lance la procédure qui alimente la table P_T_EmploymentEarning
		//
		PreparedStatement pstmt2 = null;
		String ExecSP = "exec sp_EmploymentEarning " + Instance_ID + ", " +
		                Period_Start_ID + ", " + Period_End_ID + ", " + Deduction_ID + ", '" +
		                Establishment + "', " + User_ID + ", " + Client_ID + ", " + Org_ID;

		String sql = "";
		try
		{
			CallableStatement pstmp = DB.prepareCall(ExecSP);
			//pstmp.setInt(1, m_pi.  Instance_ID);
			pstmp.executeUpdate();
			pstmp.close();
/*
	        sql = "SELECT WEBAPPURL FROM P_SYSTEM_PARAMETERS ";
	        pstmt2 = DB.prepareStatement(sql, null);
	        ResultSet rs = pstmt2.executeQuery();
	        String srvUrl = "";

            if(rs.next())
	       	   srvUrl = rs.getString(1);
	        
	        if(srvUrl.length() == 0)
	        {
	        	//TODO Error Msg avec Logger
	        	System.out.println("Error With System Parameters");
	        	return "";
	        }
	        String repUrl = srvUrl+"Crystal/reports.jsp?AD_PInstance_ID=" + Instance_ID;
	        Env.startBrowser(repUrl);
*/
			
			int AD_Pinstance_ID= this.getAD_PInstance_ID();
			

			int l_Role = Env.getAD_Role_ID(Env.getCtx());
	        ReportLauncher.callReport( AD_Pinstance_ID, l_Role, m_Format, getParameter() );
			
		}
        catch (SQLException e)
		{
        	log.log(Level.WARNING, "DoIt()", e);	
		}
        return "Succes";
	    
	}

	/*
	public Properties getCtx()
	{
		return m_ctx;
	}
	*/

}
