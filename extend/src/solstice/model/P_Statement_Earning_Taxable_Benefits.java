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

import solstice.process.P_RetroCalculation;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_Statement_Earning_Taxable_Benefits extends X_P_Statement_Earning_Taxable_Benefits
{
    /**
     * 
     * @param ctx
     * @param P_Statement_Earning_Taxable_Benefits_ID
     * @return
     */
	public static P_Statement_Earning_Taxable_Benefits get (Properties ctx, int P_Statement_Earning_Taxable_Benefits_ID, String trxName)
	{
		Integer key = new Integer (P_Statement_Earning_Taxable_Benefits_ID);
		P_Statement_Earning_Taxable_Benefits Statement_Earning_Taxable_Benefits = (P_Statement_Earning_Taxable_Benefits)s_cache.get(key);
		if (Statement_Earning_Taxable_Benefits != null)
			return Statement_Earning_Taxable_Benefits;
		Statement_Earning_Taxable_Benefits = new P_Statement_Earning_Taxable_Benefits (ctx, P_Statement_Earning_Taxable_Benefits_ID, trxName);
		s_cache.put (key, Statement_Earning_Taxable_Benefits);
		return Statement_Earning_Taxable_Benefits;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Statement_Earning_Taxable_Benefits>	s_cache = new CCache<Integer,P_Statement_Earning_Taxable_Benefits>("P_Statement_Earning_Taxable_Benefits", 20);

    /**
     * @param ctx
     * @param P_Statement_Earning_Taxable_Benefits_ID
     */
    public P_Statement_Earning_Taxable_Benefits(Properties ctx, int P_Statement_Earning_Taxable_Benefits_ID, String trxName)
    {
        super(ctx, P_Statement_Earning_Taxable_Benefits_ID, trxName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param ctx
     * @param rs
     */
    public P_Statement_Earning_Taxable_Benefits(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
        // TODO Auto-generated constructor stub
    }

}
