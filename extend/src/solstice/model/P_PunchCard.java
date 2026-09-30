/**
 * 
 */
package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * @author frafor01
 *
 */
public class P_PunchCard extends X_P_PunchCard
{

    /**
     * @param ctx
     * @param P_PunchCard_ID
     * @param trxName
     */
    public P_PunchCard(Properties ctx, int P_PunchCard_ID, String trxName)
    {
        super(ctx, P_PunchCard_ID, trxName);
    }

    /**
     * @param ctx
     * @param rs
     * @param trxName
     */
    public P_PunchCard(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
    }

}
