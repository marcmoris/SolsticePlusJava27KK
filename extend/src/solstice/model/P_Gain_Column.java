/*
 * Created on 2005-09-13
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
public class P_Gain_Column extends X_P_Gain_Column
{

    /**
     * @param ctx
     * @param P_Gain_Column_ID
     * @param trxName
     */
    public P_Gain_Column(Properties ctx, int P_Gain_Column_ID, String trxName)
    {
        super(ctx, P_Gain_Column_ID, trxName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param ctx
     * @param rs
     * @param trxName
     */
    public P_Gain_Column(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
        // TODO Auto-generated constructor stub
    }

}
