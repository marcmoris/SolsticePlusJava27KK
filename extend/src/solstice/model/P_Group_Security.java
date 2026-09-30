package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

public class P_Group_Security extends X_P_Group_Security
{

    public P_Group_Security(Properties ctx, int P_Group_Security_ID,
            String trxName)
    {
        super(ctx, P_Group_Security_ID, trxName);
    }

    public P_Group_Security(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
    }

}
