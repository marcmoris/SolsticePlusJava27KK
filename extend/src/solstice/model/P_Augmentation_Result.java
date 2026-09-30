/*
 * Created on 2005-09-29
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_Augmentation_Result extends X_P_Augmentation_Result
{

	/**	Cache						*/
	private static CCache<Integer,P_Augmentation_Result>	s_cache = new CCache<Integer,P_Augmentation_Result>("P_Augmentation_Result", 20);

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Augmentation_Result.class);


    /**
     * @param ctx
     * @param P_Augmentation_Result_ID
     * @param trxName
     */
    public P_Augmentation_Result(Properties ctx, int P_Augmentation_Result_ID,
            String trxName)
    {
        super(ctx, P_Augmentation_Result_ID, trxName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param ctx
     * @param rs
     * @param trxName
     */
    public P_Augmentation_Result(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
        // TODO Auto-generated constructor stub
    }

    
	/**
	 * 	Get Assignment
	 *	@param ctx context
	 * 	@param P_Assignment_ID id
	 *	@return Assignment
	 */
	public static P_Augmentation_Result get (Properties ctx, int P_Augmentation_Result_ID, String trxName)
	{
		Integer key = new Integer (P_Augmentation_Result_ID);
		P_Augmentation_Result Augmentation_Result = (P_Augmentation_Result)s_cache.get(key);
		if (Augmentation_Result != null)
			return Augmentation_Result;
		Augmentation_Result = new P_Augmentation_Result (ctx, P_Augmentation_Result_ID, trxName);
		s_cache.put (key, Augmentation_Result);
		return Augmentation_Result;
	}	//	get

}
