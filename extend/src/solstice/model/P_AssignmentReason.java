/*
 * Created on 2005-09-06
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
public class P_AssignmentReason extends X_P_AssignmentReason
{

    /**
     * @param ctx
     * @param P_AssignmentReason_ID
     * @param trxName
     */
    public P_AssignmentReason(Properties ctx, int P_AssignmentReason_ID,
            String trxName)
    {
        super(ctx, P_AssignmentReason_ID, trxName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param ctx
     * @param rs
     * @param trxName
     */
    public P_AssignmentReason(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
        // TODO Auto-generated constructor stub
    }

}
