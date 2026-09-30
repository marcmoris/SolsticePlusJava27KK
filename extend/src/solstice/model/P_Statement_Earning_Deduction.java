/*
 * Created on 2 août 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_Statement_Earning_Deduction extends X_P_Statement_Earning_Deduction
{
    /**
     * 
     * @param ctx
     * @param P_Statement_Earning_Deduction_ID
     * @return
     */
	public static P_Statement_Earning_Deduction get (Properties ctx, int P_Statement_Earning_Deduction_ID, String trxName)
	{
		Integer key = new Integer (P_Statement_Earning_Deduction_ID);
		P_Statement_Earning_Deduction Statement_Earning_Deduction = (P_Statement_Earning_Deduction)s_cache.get(key);
		if (Statement_Earning_Deduction != null)
			return Statement_Earning_Deduction;
		Statement_Earning_Deduction = new P_Statement_Earning_Deduction (ctx, P_Statement_Earning_Deduction_ID, trxName);
		s_cache.put (key, Statement_Earning_Deduction);
		return Statement_Earning_Deduction;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Statement_Earning_Deduction>	s_cache = new CCache<Integer,P_Statement_Earning_Deduction>("P_Statement_Earning_Deduction", 20);

    /**
     * @param ctx
     * @param P_Statement_Earning_Deduction_ID
     */
    public P_Statement_Earning_Deduction(Properties ctx, int P_Statement_Earning_Deduction_ID, String trxName)
    {
        super(ctx, P_Statement_Earning_Deduction_ID, trxName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param ctx
     * @param rs
     */
    public P_Statement_Earning_Deduction(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
        // TODO Auto-generated constructor stub
    }

}
