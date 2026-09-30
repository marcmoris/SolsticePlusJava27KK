/**
 * 
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
 */
public class P_PunchCard_Reason extends X_P_PunchCard_Reason
{

    /**
     * @param ctx
     * @param P_PunchCard_Reason_ID
     * @param trxName
     */
    public P_PunchCard_Reason(Properties ctx, int P_PunchCard_Reason_ID,
            String trxName)
    {
        super(ctx, P_PunchCard_Reason_ID, trxName);
    }

    /**
     * @param ctx
     * @param rs
     * @param trxName
     */
    public P_PunchCard_Reason(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
    }
    
    /**
     * Retourne l'id correspondant à la valeur dans la table P_PunchCard_Reason
     */
    public static int getID(String value) throws SQLException
    {
        int id = -1;
        
        PreparedStatement stmt = DB.prepareStatement(
                "select P_PunchCard_Reason_ID" +
                "  from P_PunchCard_Reason" +
                " where Value = '" + value + "'", null);
        
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next())
            id = rs.getInt("P_PunchCard_Reason_ID");
        
        rs.close();
        stmt.close();
        
        return id;
    }

}
