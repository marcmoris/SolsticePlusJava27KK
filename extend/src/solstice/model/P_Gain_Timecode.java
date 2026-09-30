

package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Job_Type Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Gain_Timecode extends X_P_Gain_Timecode
{
	/**
	 * 	Get Job_Type
	 *	@param ctx context
	 * 	@param P_Gain_Timecode_ID id
	 *	@return Job_Type
	 */
	public static P_Gain_Timecode get (Properties ctx, int P_Gain_Timecode_ID, String trxName)
	{
		Integer key = new Integer (P_Gain_Timecode_ID);
		P_Gain_Timecode Job_Type = (P_Gain_Timecode)s_cache.get(key);
		if (Job_Type != null)
			return Job_Type;
		Job_Type = new P_Gain_Timecode (ctx, P_Gain_Timecode_ID, trxName);
		s_cache.put (key, Job_Type);
		return Job_Type;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Gain_Timecode>	s_cache = new CCache<Integer,P_Gain_Timecode>("P_Gain_Timecode", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Gain_Timecode_ID id
	 */
	public P_Gain_Timecode (Properties ctx, int P_Gain_Timecode_ID, String trxName)
	{
		super (ctx, P_Gain_Timecode_ID, trxName);
		if (P_Gain_Timecode_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Gain_Timecode

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Gain_Timecode (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Gain_Timecode (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Gain_Timecode_ID"), trxName);
	}	//	P_Gain_Timecode


}	//	P_Gain_Timecode
