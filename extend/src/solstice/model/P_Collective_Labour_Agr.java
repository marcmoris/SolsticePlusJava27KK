package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.Env;

/**
 *  Collective_Labour_Agr Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Collective_Labour_Agr extends X_P_Collective_Labour_Agr
{
	/**
	 * 	Get Collective_Labour_Agr
	 *	@param ctx context
	 * 	@param P_Collective_Labour_Agr_ID id
	 *	@return Collective_Labour_Agr
	 */
	public static P_Collective_Labour_Agr get (Properties ctx, int P_Collective_Labour_Agr_ID, String trxName)
	{
		Integer key = new Integer (P_Collective_Labour_Agr_ID);
		P_Collective_Labour_Agr Collective_Labour_Agr = (P_Collective_Labour_Agr)s_cache.get(key);
		if (Collective_Labour_Agr != null)
			return Collective_Labour_Agr;
		Collective_Labour_Agr = new P_Collective_Labour_Agr (ctx, P_Collective_Labour_Agr_ID, trxName);
		s_cache.put (key, Collective_Labour_Agr);
		return Collective_Labour_Agr;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Collective_Labour_Agr>	s_cache = new CCache<Integer,P_Collective_Labour_Agr>("P_Collective_Labour_Agr", 10);

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Collective_Labour_Agr.class);
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Collective_Labour_Agr_ID id
	 */
	public P_Collective_Labour_Agr (Properties ctx, int P_Collective_Labour_Agr_ID, String trxName)
	{
		super (ctx, P_Collective_Labour_Agr_ID, trxName);
		if (P_Collective_Labour_Agr_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Collective_Labour_Agr

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Collective_Labour_Agr (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Collective_Labour_Agr (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Collective_Labour_Agr_ID"), trxName);
	}	//	P_Collective_Labour_Agr

	
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
	
	/**
	 * 	Get Translated Name
	 *	@return name
	 */
	public String getTrlDescription()
	{
		if (Env.isBaseLanguage(Env.getCtx(), "AD_Ref_List"))
			return getDescription();
		return get_Translation("Description", Env.getAD_Language(Env.getCtx()));
	}	//	getTrlDescription


}	//	P_Collective_Labour_Agr
