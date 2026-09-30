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
public class P_Statement_Earning extends X_P_Statement_Earning
{
	public static P_Statement_Earning get (Properties ctx, int P_Statement_Earning_ID, String trxName)
	{
		Integer key = new Integer (P_Statement_Earning_ID);
		P_Statement_Earning Statement_Earning = (P_Statement_Earning)s_cache.get(key);
		if (Statement_Earning != null)
			return Statement_Earning;
		Statement_Earning = new P_Statement_Earning (ctx, P_Statement_Earning_ID, trxName);
		s_cache.put (key, Statement_Earning);
		return Statement_Earning;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Statement_Earning>	s_cache = new CCache<Integer,P_Statement_Earning>("P_Statement_Earning", 20);

    /**
     * @param ctx
     * @param P_Statement_Earning_ID
     */
    public P_Statement_Earning(Properties ctx, int P_Statement_Earning_ID, String trxName)
    {
        super(ctx, P_Statement_Earning_ID, trxName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param ctx
     * @param rs
     */
    public P_Statement_Earning(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
        // TODO Auto-generated constructor stub
    }

}
