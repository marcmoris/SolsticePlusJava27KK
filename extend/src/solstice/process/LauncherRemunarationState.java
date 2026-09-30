/*
 * Created on Aug 31, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;


/**
 * @author rejgar01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LauncherRemunarationState extends SvrProcess 
{
    private int Client_ID = 0;
    private int Org_ID = 0;
    private int User_ID = 0;
    private int StartPeriod = 0;
    private int EndPeriod = 0;
    private int Period_ID = 0;
    private int Instance_ID = 0;
	private String m_Format = "PDF";
	
    protected void prepare()
    {
        ProcessInfoParameter[] para = getParameter();
        Instance_ID = getAD_PInstance_ID();
        for (int i = 0; i < para.length; i++)
  	    {
  	        String name = para[i].getParameterName();
  	      
  	        if (para[i].getParameter() == null)
  		    ;
  	        else if (name.equals("AD_Client_ID"))
  		       Client_ID = ((BigDecimal)para[i].getParameter()).intValue();
  	        else if (name.equals("AD_Org_ID"))
  		       Org_ID = ((BigDecimal)para[i].getParameter()).intValue();
  	        else if (name.equals("P_Period_Start_ID"))
               StartPeriod = ((BigDecimal)para[i].getParameter()).intValue();
            else if (name.equals("P_Period_End_ID"))
  	           EndPeriod = ((BigDecimal)para[i].getParameter()).intValue();
            else if (name.equals("CrystalFormat"))
				m_Format = (String)para[i].getParameter();
  	        else 
  		       log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
  	        
  	    }
        Client_ID = Env.getAD_Client_ID(getCtx());
        Org_ID = Env.getAD_Org_ID(getCtx());
        User_ID = Env.getAD_User_ID(getCtx());
        
        String sql = "INSERT INTO AD_PInstance_PARA (AD_PInstance_id, seqno, ParameterName, P_String, P_String_To, P_Number, P_Number_To, P_Date, P_Date_To, Info, Info_To, Ad_Client_id, Ad_Org_id, Created, createdby, Updated, UpdatedBy, Isactive) " +
                     "Select " + Instance_ID + ", " +
                     "(Select Max(seqno) + 1 from AD_PInstance_Para where AD_PInstance_id = " + Instance_ID + "), " +
                     "'AD_Pinstance_ID', null, null, " + Instance_ID + ", null, null, null, null, null, " + Client_ID + ", " + Org_ID + ", GetDate(), " + User_ID + ", GetDate(), " + User_ID + ", 'Y'";
        
        PreparedStatement pstmp = null;
        try
        {
            pstmp = DB.prepareStatement(sql, null);
            pstmp.execute();
            //pstmp = DB.
        }
  	    catch (Exception e)
	    {
     		log.log(Level.SEVERE, "LauncherRemunerationState.Insert", e);
	    }
    }	//	prepare


    protected String doIt() throws Exception
    {
        if (StartPeriod == 0)
        {
            log.log(Level.SEVERE, "P_RemunerationState-Period not found");
            return ("*** Error - Start Period not found *");
        }
        if (EndPeriod == 0)
        {
            log.log(Level.SEVERE, "P_RemunerationState-Period not found");
            return ("*** Error - End Period not found");
        }
        
        P_InOutRemunerationState RemunerationState = new P_InOutRemunerationState( getCtx() );
        RemunerationState.ExecuteProcess(Client_ID, Org_ID, StartPeriod, EndPeriod, Instance_ID);

        int AD_Pinstance_ID = this.getAD_PInstance_ID();
        
/*        String sql = "SELECT WEBAPPURL "
        	+ " FROM P_SYSTEM_PARAMETERS ";
        PreparedStatement pstmt = DB.prepareStatement(sql, null);
        ResultSet rs = pstmt.executeQuery();
        String srvUrl = "";
        if(rs.next())
        	srvUrl = rs.getString(1);
        
        if(srvUrl.length() == 0)
        {
        	//TODO Error Msg avec Logger
        	System.out.println("Error With System Parameters");
        	return "";
        }
        int l_Role = Env.getAD_Role_ID(Env.getCtx());
        String repUrl = srvUrl+"Crystal/reports.jsp?AD_PInstance_ID=" + AD_Pinstance_ID + "&Format=" + m_Format + "&Ad_Role_ID=" + l_Role;
//        String repUrl = srvUrl+"Crystal/reports.jsp?AD_PInstance_ID=" + AD_Pinstance_ID;
        Env.startBrowser(repUrl);
        
*/
		int l_Role = Env.getAD_Role_ID(Env.getCtx());
        ReportLauncher.callReport( AD_Pinstance_ID, l_Role, m_Format, getParameter() );


        return "";
    }

}

