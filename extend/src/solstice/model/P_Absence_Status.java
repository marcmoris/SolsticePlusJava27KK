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
public class P_Absence_Status extends X_P_Absence_Status
{

    /**
     * @param ctx
     * @param P_Absence_Status_ID
     * @param trxName
     */
    public P_Absence_Status(Properties ctx, int P_Absence_Status_ID,
            String trxName)
    {
        super(ctx, P_Absence_Status_ID, trxName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param ctx
     * @param rs
     * @param trxName
     */
    public P_Absence_Status(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
        // TODO Auto-generated constructor stub
    }

    /**
     * Retourne le id correspondant au statut (char(4))
     */
    public static int getId(String status)
    {
        int id = 0;
        String sql = "select P_Absence_Status_ID from P_Absence_Status where Value = '" + status + "'";
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
                id = rs.getInt("P_Absence_Status_ID");
            }
            
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace(System.out);
        }
        
        return id;
    }
    
    /**
     * Retourne le code de statut à partir du id
     */
    public static String getValue(int id)
    {
        String value = null;
        try
        {
            PreparedStatement stmt = DB.prepareStatement("select Value from P_Absence_Status where P_Absence_Status_ID = " + id, null);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
                value = rs.getString("Value");
            }
            
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace(System.out);
        }
        return value;
    }
}
