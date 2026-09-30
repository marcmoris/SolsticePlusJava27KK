package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Skill Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Skill extends X_P_Skill
{
	/**
	 * 	Get Skill
	 *	@param ctx context
	 * 	@param P_Skill_ID id
	 *	@return Skill
	 */
	public static P_Skill get (Properties ctx, int P_Skill_ID, String trxName)
	{
		Integer key = new Integer (P_Skill_ID);
		P_Skill Skill = (P_Skill)s_cache.get(key);
		if (Skill != null)
			return Skill;
		Skill = new P_Skill (ctx, P_Skill_ID, trxName);
		s_cache.put (key, Skill);
		return Skill;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Skill>	s_cache = new CCache<Integer,P_Skill>("P_Skill", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Skill_ID id
	 */
	public P_Skill (Properties ctx, int P_Skill_ID, String trxName)
	{
		super (ctx, P_Skill_ID, trxName);
		if (P_Skill_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Skill

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Skill (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Skill (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Skill_ID"), trxName);
	}	//	P_Skill


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Skill[ID=")
			.append(this.getP_Skill_ID())
			.append(",Value=").append(getValue())
			.append(",Name=").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	P_Skill
