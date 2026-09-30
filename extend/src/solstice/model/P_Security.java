package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

public class P_Security extends X_P_Security
{

    public P_Security(Properties ctx, int P_Security_ID, String trxName)
    {
        super(ctx, P_Security_ID, trxName);
    }

    public P_Security(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
    }

}
