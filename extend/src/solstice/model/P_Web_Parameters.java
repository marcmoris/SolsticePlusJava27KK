package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

public class P_Web_Parameters extends X_P_Web_Parameters
{

    public P_Web_Parameters(Properties ctx, int P_Web_Parameters_ID,
            String trxName)
    {
        super(ctx, P_Web_Parameters_ID, trxName);
        // TODO Auto-generated constructor stub
    }

    public P_Web_Parameters(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
        // TODO Auto-generated constructor stub
    }

}
