package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

public class P_Group extends X_P_Group
{

    public P_Group(Properties ctx, int P_Group_ID, String trxName)
    {
        super(ctx, P_Group_ID, trxName);
    }

    public P_Group(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
    }

}
