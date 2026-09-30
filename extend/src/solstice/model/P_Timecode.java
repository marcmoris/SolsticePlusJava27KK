package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

public class P_Timecode extends X_P_Timecode
{	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


/**
	 * 	Get SpecialRate
	 *	@param ctx context
	 * 	@param P_SpecialRate_ID id
	 *	@return SpecialRate
	 */
	public static P_Timecode get (Properties ctx, int P_Timecode_ID, String trxName)
	{
		Integer key = new Integer (P_Timecode_ID);
		P_Timecode p_Timecode = (P_Timecode)s_cache.get(key);
		if (p_Timecode != null)
			return p_Timecode;
		p_Timecode = new P_Timecode (ctx, P_Timecode_ID, trxName);
		s_cache.put (key, p_Timecode);
		return p_Timecode;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Timecode>	s_cache = new CCache<Integer,P_Timecode>("P_Timecode", 50);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_SpecialRate_ID id
	 */
	public P_Timecode (Properties ctx, int P_Timecode_ID, String trxName)
	{
		super (ctx, P_Timecode_ID, trxName);
		if (P_Timecode_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
		//	setAD_Org_ID(0);
		}
	}	//	P_SpecialRate

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Timecode (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Timecode (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Timecode_ID"), trxName);
	}	//	P_SpecialRate

	


}
