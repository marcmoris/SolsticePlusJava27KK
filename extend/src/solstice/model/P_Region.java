package solstice.model;

import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;
import org.compiere.model.X_C_Region;
import java.sql.ResultSet;

/**
 *  Region Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Region extends X_C_Region
{
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Region
	 *	@param ctx context
	 * 	@param C_Region_ID id
	 *	@return Region
	 */
	public static P_Region get (Properties ctx, int C_Region_ID, String trxName)
	{
		Integer key = new Integer (C_Region_ID);
		P_Region Region = (P_Region)s_cache.get(key);
		if (Region != null)
			return Region;
		Region = new P_Region (ctx, C_Region_ID, trxName);
		s_cache.put (key, Region);
		return Region;
	}	//	get


	/**	Cache						*/
	private static CCache<Integer,P_Region>	s_cache = new CCache<Integer,P_Region>("P_Region", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param C_Region_ID id
	 */
	public P_Region (Properties ctx, int C_Region_ID, String trxName)
	{
		super (ctx, C_Region_ID, trxName);
		if (C_Region_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	C_Region

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Region (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Region (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#C_Region_ID"), trxName);
	}	//	C_Region



}	//	C_Region
