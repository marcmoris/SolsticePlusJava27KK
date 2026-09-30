package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.compiere.util.DB;

public class P_PunchCard_Collective_Labour_Agr extends
        X_P_PunchCard_Collective_Labour_Agr
{

    public P_PunchCard_Collective_Labour_Agr(Properties ctx,
            int P_PunchCard_Collective_Labour_Agr_ID, String trxName)
    {
        super(ctx, P_PunchCard_Collective_Labour_Agr_ID, trxName);
    }

    public P_PunchCard_Collective_Labour_Agr(Properties ctx, ResultSet rs,
            String trxName)
    {
        super(ctx, rs, trxName);
    }

    /**
     * Cette méthode sert à vérifier si la convention collective
     * passé en paramètre peut accéder au permis horodateur
     */
    public static boolean canAccess(int id) throws SQLException
    {
        PreparedStatement stmt = DB.prepareStatement(
                "select 1" +
                "  from P_PunchCard_Collective_Labour_Agr" +
                " where P_Collective_Labour_Agr_ID = " + id, null);
        
        ResultSet rs = stmt.executeQuery();
        
        boolean access = rs.next();
        
        rs.close();
        stmt.close();
        
        return access;
    }
}
