/*
 * Created on 2005-09-20
 */
package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * @author frafor01
 */
public class P_Absence_Status_Histo extends X_P_Absence_Status_Histo
{

    /**
     * @param ctx
     * @param P_Absence_Status_Histo_ID
     * @param trxName
     */
    public P_Absence_Status_Histo(Properties ctx,
            int P_Absence_Status_Histo_ID, String trxName)
    {
        super(ctx, P_Absence_Status_Histo_ID, trxName);
    }

    /**
     * @param ctx
     * @param rs
     * @param trxName
     */
    public P_Absence_Status_Histo(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
    }

}
