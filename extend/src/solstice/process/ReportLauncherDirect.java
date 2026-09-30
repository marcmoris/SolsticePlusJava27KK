/*
 * Created on 2005-08-09
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.DB;

/**
 * @author kevmar01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ReportLauncherDirect extends SvrProcess 
{
    protected void prepare()
    {
        
    }	//	prepare


    protected String doIt() throws Exception
    {
        int AD_Pinstance_ID = this.getAD_PInstance_ID();
        
        String sql = "SELECT WEBAPPURL "
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
        String repUrl = srvUrl+"Crystal/reportsDirect.jsp?AD_PInstance_ID=" + AD_Pinstance_ID;
        Env.startBrowser(repUrl);
        return "";
    }
}
