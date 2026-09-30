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
public class P_Job_Type extends X_P_Job_Type
{
	/**
	 * 	Get Job_Type
	 *	@param ctx context
	 * 	@param P_Job_Type_ID id
	 *	@return Job_Type
	 */
	public static P_Job_Type get (Properties ctx, int P_Job_Type_ID, String trxName)
	{
		Integer key = new Integer (P_Job_Type_ID);
		P_Job_Type Job_Type = (P_Job_Type)s_cache.get(key);
		if (Job_Type != null)
			return Job_Type;
		Job_Type = new P_Job_Type (ctx, P_Job_Type_ID, trxName);
		s_cache.put (key, Job_Type);
		return Job_Type;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Job_Type>	s_cache = new CCache<Integer,P_Job_Type>("P_Job_Type", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Job_Type_ID id
	 */
	public P_Job_Type (Properties ctx, int P_Job_Type_ID, String trxName)
	{
		super (ctx, P_Job_Type_ID, trxName);
		if (P_Job_Type_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Job_Type

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Job_Type (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Job_Type (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Job_Type_ID"), trxName);
	}	//	P_Job_Type

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



}	//	P_Job_Type
