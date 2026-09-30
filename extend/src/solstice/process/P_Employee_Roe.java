/*
 * Created on 28 juin 2005
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
public class P_Employee_Roe extends SvrProcess
{
    private int m_employeeId;
    
	// On récupère le ID de la session
	protected void prepare() 
	{
	    m_employeeId = getRecord_ID();
	}

	// On lance la page web des relevés d'emploi pour cet employé
	protected String doIt() throws Exception 
	{
	    String sql = "select top 1 WEBAPPURL from P_System_Parameters";
	    PreparedStatement stmt = DB.prepareStatement(sql, null);
	    ResultSet rs = stmt.executeQuery();
	    if(rs.next())
	    {
	        Env.startBrowser(rs.getString(1) + "login.jsp?user=" + Env.getContext(Env.getCtx(), "#AD_User_Name") + "&redirection=%2FreleveEmploi%2Findex.jsp%3FemployeeId=" + m_employeeId);
	    }
	    rs.close();
	    stmt.close();
	    return null;
	}
}
