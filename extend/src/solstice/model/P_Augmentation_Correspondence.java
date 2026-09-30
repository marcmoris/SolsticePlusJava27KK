/*
 * Created on 2005-09-29
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
public class P_Augmentation_Correspondence extends
        X_P_Augmentation_Correspondence
{

    /**
     * @param ctx
     * @param P_Augmentation_Correspondence_ID
     * @param trxName
     */
    public P_Augmentation_Correspondence(Properties ctx,
            int P_Augmentation_Correspondence_ID, String trxName)
    {
        super(ctx, P_Augmentation_Correspondence_ID, trxName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param ctx
     * @param rs
     * @param trxName
     */
    public P_Augmentation_Correspondence(Properties ctx, ResultSet rs,
            String trxName)
    {
        super(ctx, rs, trxName);
        // TODO Auto-generated constructor stub
    }

}
