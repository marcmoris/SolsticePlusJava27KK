/*
 * Created on 2005-09-26
 */
package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * @author frafor01
 */
public class P_Equivalence_Factor_Param extends X_P_Equivalence_Factor_Param
{

    /**
     * @param ctx
     * @param P_Equivalence_Factor_Param_ID
     * @param trxName
     */
    public P_Equivalence_Factor_Param(Properties ctx,
            int P_Equivalence_Factor_Param_ID, String trxName)
    {
        super(ctx, P_Equivalence_Factor_Param_ID, trxName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param ctx
     * @param rs
     * @param trxName
     */
    public P_Equivalence_Factor_Param(Properties ctx, ResultSet rs,
            String trxName)
    {
        super(ctx, rs, trxName);
        // TODO Auto-generated constructor stub
    }

}
