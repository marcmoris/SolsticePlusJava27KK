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
public class P_Statement_Earning_Credits extends X_P_Statement_Earning_Credits
{
    /**
     * 
     * @param ctx
     * @param P_Statement_Earning_Credits_ID
     * @return
     */
	public static P_Statement_Earning_Credits get (Properties ctx, int P_Statement_Earning_Credits_ID, String trxName)
	{
		Integer key = new Integer (P_Statement_Earning_Credits_ID);
		P_Statement_Earning_Credits statement_Earning_Credits = (P_Statement_Earning_Credits)s_cache.get(key);
		if (statement_Earning_Credits != null)
			return statement_Earning_Credits;
		statement_Earning_Credits = new P_Statement_Earning_Credits (ctx, P_Statement_Earning_Credits_ID, trxName);
		s_cache.put (key, statement_Earning_Credits);
		return statement_Earning_Credits;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Statement_Earning_Credits>	s_cache = new CCache<Integer,P_Statement_Earning_Credits>("P_Statement_Earning_Credits", 20);

    /**
     * @param ctx
     * @param P_Statement_Earning_Credits_ID
     */
    public P_Statement_Earning_Credits(Properties ctx, int P_Statement_Earning_Credits_ID, String trxName)
    {
        super(ctx, P_Statement_Earning_Credits_ID, trxName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param ctx
     * @param rs
     */
    public P_Statement_Earning_Credits(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
        // TODO Auto-generated constructor stub
    }

}
