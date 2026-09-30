package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

public class P_ER_DistributionNoRecall extends X_P_ER_DistributionNoRecall
{

    public P_ER_DistributionNoRecall(Properties ctx,
            int P_ER_DistributionNoRecall_ID, String trxName)
    {
        super(ctx, P_ER_DistributionNoRecall_ID, trxName);
        // TODO Auto-generated constructor stub
    }

    public P_ER_DistributionNoRecall(Properties ctx, ResultSet rs,
            String trxName)
    {
        super(ctx, rs, trxName);
        // TODO Auto-generated constructor stub
    }

}
