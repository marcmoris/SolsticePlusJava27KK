package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Credits_Param Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Credits_Mth_Evaluation extends X_P_Credits_Mth_Evaluation
{
	/**
	 * 	Get Credits_Param
	 *	@param ctx context
	 * 	@param P_Credits_Mth_Evaluation_ID id
	 *	@return Credits_Param
	 */
	public static P_Credits_Mth_Evaluation get (Properties ctx, int P_Credits_Mth_Evaluation_ID, String trxName)
	{
		Integer key = new Integer (P_Credits_Mth_Evaluation_ID);
		P_Credits_Mth_Evaluation Credits_Param = (P_Credits_Mth_Evaluation)s_cache.get(key);
		if (Credits_Param != null)
			return Credits_Param;
		Credits_Param = new P_Credits_Mth_Evaluation (ctx, P_Credits_Mth_Evaluation_ID, trxName);
		s_cache.put (key, Credits_Param);
		return Credits_Param;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Credits_Mth_Evaluation>	s_cache = new CCache<Integer,P_Credits_Mth_Evaluation>("P_Credits_Mth_Evaluation", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Credits_Mth_Evaluation_ID id
	 */
	public P_Credits_Mth_Evaluation (Properties ctx, int P_Credits_Mth_Evaluation_ID, String trxName)
	{
		super (ctx, P_Credits_Mth_Evaluation_ID, trxName);
		if (P_Credits_Mth_Evaluation_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Credits_Mth_Evaluation

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Credits_Mth_Evaluation (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Credits_Mth_Evaluation (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Credits_Mth_Evaluation_ID"), trxName);
	}	//	P_Credits_Mth_Evaluation



}	//	P_Credits_Mth_Evaluation
