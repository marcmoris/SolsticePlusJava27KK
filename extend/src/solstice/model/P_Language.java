/*
 * Created on 17 août 2005
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
public class P_Language extends X_P_Language
{
	/**
	 * 	Get Employee
	 *	@param ctx context
	 * 	@param P_Employee_ID id
	 *	@return Employee
	 */
	public static P_Language get (Properties ctx, int P_Language_ID, String trxName)
	{
		Integer key = new Integer (P_Language_ID);
		P_Language language = (P_Language)s_cache.get(key);
		if (language != null)
			return language;
		language = new P_Language (ctx, P_Language_ID, trxName);
		s_cache.put (key, language);
		return language;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Language>	s_cache = new CCache<Integer,P_Language>("P_Language", 20);

    /**
     * @param ctx
     * @param P_Language_ID
     * @param trxName
     */
    public P_Language(Properties ctx, int P_Language_ID, String trxName)
    {
        super(ctx, P_Language_ID, trxName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param ctx
     * @param rs
     * @param trxName
     */
    public P_Language(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
        // TODO Auto-generated constructor stub
    }

    public String getName( String lang)
    {
    	return get_Translation("Name", lang);
    }
 
}
