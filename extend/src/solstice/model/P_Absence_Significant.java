/*
 * Created on 31-Aug-2005
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
public class P_Absence_Significant extends X_P_Absence_Significant
{
	/**
	 * 	Get absence
	 *	@param ctx context
	 * 	@param P_absence_ID id
	 *	@return absence
	 */
	public static P_Absence_Significant get (Properties ctx, int P_Absence_Significant_ID, String trxName)
	{
		Integer key = new Integer (P_Absence_Significant_ID);
		P_Absence_Significant absence = (P_Absence_Significant)s_cache.get(key);
		if (absence != null)
			return absence;
		absence = new P_Absence_Significant (ctx, P_Absence_Significant_ID, trxName);
		s_cache.put (key, absence);
		return absence;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Absence_Significant>	s_cache = new CCache<Integer,P_Absence_Significant>("P_Absence_Significant", 100);

    /**
     * @param ctx
     * @param P_Absence_Significant_ID
     * @param trxName
     */
    public P_Absence_Significant(Properties ctx, int P_Absence_Significant_ID,
            String trxName)
    {
        super(ctx, P_Absence_Significant_ID, trxName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param ctx
     * @param rs
     * @param trxName
     */
    public P_Absence_Significant(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
        // TODO Auto-generated constructor stub
    }

}
