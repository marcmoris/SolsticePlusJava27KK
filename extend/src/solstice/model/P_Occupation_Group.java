package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;


import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.Env;


/**
 *  Occupation_Group Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Occupation_Group extends X_P_Occupation_Group
{
	/**
	 * 	Get Occupation_Group
	 *	@param ctx context
	 * 	@param P_Occupation_Group_ID id
	 *	@return Occupation_Group
	 */
	public static P_Occupation_Group get (Properties ctx, int P_Occupation_Group_ID, String trxName)
	{
		Integer key = new Integer (P_Occupation_Group_ID);
		P_Occupation_Group Occupation_Group = (P_Occupation_Group)s_cache.get(key);
		if (Occupation_Group != null)
			return Occupation_Group;
		Occupation_Group = new P_Occupation_Group (ctx, P_Occupation_Group_ID, trxName);
		s_cache.put (key, Occupation_Group);
		return Occupation_Group;
	}	//	get


	/**	Cache						*/
	private static CCache<Integer,P_Occupation_Group>	s_cache = new CCache<Integer,P_Occupation_Group>("P_Occupation_Group", 20);
	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Occupation_Group.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Occupation_Group_ID id
	 */
	public P_Occupation_Group (Properties ctx, int P_Occupation_Group_ID, String trxName)
	{
		super (ctx, P_Occupation_Group_ID, trxName);
		if (P_Occupation_Group_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Occupation_Group

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Occupation_Group (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Occupation_Group (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Occupation_Group_ID"), trxName);
	}	//	P_Occupation_Group


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


}	//	P_Occupation_Group
