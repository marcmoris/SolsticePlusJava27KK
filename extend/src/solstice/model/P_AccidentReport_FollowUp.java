/*
 * Created on 2005-10-14
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_AccidentReport_FollowUp extends X_P_AccidentReport_FollowUp
{

    /**
     * @param ctx
     * @param P_AccidentReport_FollowUp_ID
     * @param trxName
     */
    public P_AccidentReport_FollowUp(Properties ctx,
            int P_AccidentReport_FollowUp_ID, String trxName)
    {
        super(ctx, P_AccidentReport_FollowUp_ID, trxName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param ctx
     * @param rs
     * @param trxName
     */
    public P_AccidentReport_FollowUp(Properties ctx, ResultSet rs,
            String trxName)
    {
        super(ctx, rs, trxName);
        // TODO Auto-generated constructor stub
    }

}
