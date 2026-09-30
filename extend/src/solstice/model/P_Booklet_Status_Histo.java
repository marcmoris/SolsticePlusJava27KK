/*
 * Created on 2005-09-20
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
public class P_Booklet_Status_Histo extends X_P_Booklet_Status_Histo
{

    /**
     * @param ctx
     * @param P_Booklet_Status_Histo_ID
     * @param trxName
     */
    public P_Booklet_Status_Histo(Properties ctx,
            int P_Booklet_Status_Histo_ID, String trxName)
    {
        super(ctx, P_Booklet_Status_Histo_ID, trxName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param ctx
     * @param rs
     * @param trxName
     */
    public P_Booklet_Status_Histo(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
        // TODO Auto-generated constructor stub
    }

}
