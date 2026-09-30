/*
 * Created on 24 août 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_TimeSheetWEB extends SvrProcess
{
    public P_TimeSheetWEB()
    {
        super();
    }
    
    protected void prepare()
    {
    }
    
    protected String doIt() throws Exception
    {
	    String sql = "select top 1 WEBAPPURL from P_System_Parameters";
	    PreparedStatement stmt = DB.prepareStatement(sql, null);
	    ResultSet rs = stmt.executeQuery();
	    if(rs.next())
	    {
	        //Env.startBrowser(rs.getString(1) + "livretTemps/index.jsp");
	        Env.startBrowser(rs.getString(1) + "login.jsp?user=" + Env.getContext(Env.getCtx(), "#AD_User_Name") + "&redirection=%2FlivretTemps%2Findex.jsp");
	    }
	    rs.close();
	    stmt.close();
        return "";
    }
}
