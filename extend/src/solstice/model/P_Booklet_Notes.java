/*
 * Created on 2005-09-01
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
public class P_Booklet_Notes extends X_P_Booklet_Notes
{

    /**
     * @param ctx
     * @param P_Booklet_Notes_ID
     * @param trxName
     */
    public P_Booklet_Notes(Properties ctx, int P_Booklet_Notes_ID,
            String trxName)
    {
        super(ctx, P_Booklet_Notes_ID, trxName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param ctx
     * @param rs
     * @param trxName
     */
    public P_Booklet_Notes(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
        // TODO Auto-generated constructor stub
    }

}
