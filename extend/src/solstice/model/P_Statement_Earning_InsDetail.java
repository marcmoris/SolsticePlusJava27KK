/*
 * Created on 2005-09-23
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
public class P_Statement_Earning_InsDetail extends
        X_P_Statement_Earning_InsDetail
{

    /**
     * @param ctx
     * @param P_Statement_Earning_InsDetail_ID
     * @param trxName
     */
    public P_Statement_Earning_InsDetail(Properties ctx,
            int P_Statement_Earning_InsDetail_ID, String trxName)
    {
        super(ctx, P_Statement_Earning_InsDetail_ID, trxName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param ctx
     * @param rs
     * @param trxName
     */
    public P_Statement_Earning_InsDetail(Properties ctx, ResultSet rs,
            String trxName)
    {
        super(ctx, rs, trxName);
        // TODO Auto-generated constructor stub
    }

}
