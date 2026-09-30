/*
 * Created on 2005-09-07
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.compiere.util.DB;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_System_Parameters extends X_P_System_Parameters
{
    public static Object getParameterValue(String parameterName) throws SQLException
    {
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	
    	try
    	{
	        String sql = "select top 1 " + parameterName + " from P_System_Parameters";
	        stmt = DB.prepareStatement(sql, null);
	        rs = stmt.executeQuery();
	        if(rs.next())
	        {
	            return rs.getObject(1);
	        }
	        return null;
    	}
    	finally
    	{
    		if(rs != null)
    			rs.close();
    		
    		if(stmt != null)
    			stmt.close();
    	}
    }

    /**
     * @param ctx
     * @param P_System_Parameters_ID
     * @param trxName
     */
    public P_System_Parameters(Properties ctx, int P_System_Parameters_ID,
            String trxName)
    {
        super(ctx, P_System_Parameters_ID, trxName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param ctx
     * @param rs
     * @param trxName
     */
    public P_System_Parameters(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
        // TODO Auto-generated constructor stub
    }

}
