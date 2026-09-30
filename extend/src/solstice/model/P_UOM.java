package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

public class P_UOM extends X_P_UOM
{	/**
	 * 	Get SpecialRate
	 *	@param ctx context
	 * 	@param P_SpecialRate_ID id
	 *	@return SpecialRate
	 */
	public static P_UOM get (Properties ctx, int P_UOM_ID, String trxName)
	{
		Integer key = new Integer (P_UOM_ID);
		P_UOM p_UOM = (P_UOM)s_cache.get(key);
		if (p_UOM != null)
			return p_UOM;
		p_UOM = new P_UOM (ctx, P_UOM_ID, trxName);
		s_cache.put (key, p_UOM);
		return p_UOM;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_UOM>	s_cache = new CCache<Integer,P_UOM>("P_UOM", 50);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_SpecialRate_ID id
	 */
	public P_UOM (Properties ctx, int P_UOM_ID, String trxName)
	{
		super (ctx, P_UOM_ID, trxName);
		if (P_UOM_ID == 0)
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
	public P_UOM (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_UOM (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_UOM_ID"), trxName);
	}	//	P_SpecialRate

	
	/**
	 * 	Get Translated Name
	 *	@return name
	 */
	public String getTrlName()
	{
		if (Env.isBaseLanguage(Env.getCtx(), "AD_Ref_List"))
			return getName();
		return get_Translation("Name", Env.getAD_Language(Env.getCtx()));
	}	//	getTrlName
	

}
