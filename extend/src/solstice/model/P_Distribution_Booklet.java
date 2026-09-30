package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Distribution Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Distribution_Booklet extends X_P_Distribution_Booklet
{
	/**
	 * 	Get Distribution
	 *	@param ctx context
	 * 	@param P_Distribution_Booklet_ID id
	 *	@return Distribution
	 */
	public static P_Distribution_Booklet get (Properties ctx, int P_Distribution_Booklet_ID, String trxName)
	{
		Integer key = new Integer (P_Distribution_Booklet_ID);
		P_Distribution_Booklet Distribution = (P_Distribution_Booklet)s_cache.get(key);
		if (Distribution != null)
			return Distribution;
		Distribution = new P_Distribution_Booklet (ctx, P_Distribution_Booklet_ID, trxName);
		s_cache.put (key, Distribution);
		return Distribution;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Distribution_Booklet>	s_cache = new CCache<Integer,P_Distribution_Booklet>("P_Distribution_Booklet", 20);
	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Distribution_Booklet_ID id
	 */
	public P_Distribution_Booklet (Properties ctx, int P_Distribution_Booklet_ID, String trxName)
	{
		super (ctx, P_Distribution_Booklet_ID, trxName);
		if (P_Distribution_Booklet_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Distribution_Booklet

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Distribution_Booklet (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Distribution_Booklet (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Distribution_Booklet_ID"), trxName);
	}	//	P_Distribution_Booklet


	/**
	 * 	Get Translated Name
	 *	@return name
	 */
	public String getTrlName()
	{
		String name = "";
		if (Env.isBaseLanguage(Env.getCtx(), "AD_Ref_List"))
			name = getName();
		else
			name = get_Translation("Name", Env.getAD_Language(Env.getCtx()));
		if ( name == null) 
			return "";
		
		return name;
		
	}	//	getTrlName
	


}	//	P_Distribution_Booklet
