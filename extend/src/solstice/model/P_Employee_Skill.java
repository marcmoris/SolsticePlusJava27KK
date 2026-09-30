package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Employee_Skill Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Employee_Skill extends X_P_Employee_Skill
{
	/**
	 * 	Get Employee_Skill
	 *	@param ctx context
	 * 	@param P_Employee_Skill_ID id
	 *	@return Employee_Skill
	 */
	public static P_Employee_Skill get (Properties ctx, int P_Employee_Skill_ID, String trxName)
	{
		Integer key = new Integer (P_Employee_Skill_ID);
		P_Employee_Skill Employee_Skill = (P_Employee_Skill)s_cache.get(key);
		if (Employee_Skill != null)
			return Employee_Skill;
		Employee_Skill = new P_Employee_Skill (ctx, P_Employee_Skill_ID, trxName);
		s_cache.put (key, Employee_Skill);
		return Employee_Skill;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Employee_Skill>	s_cache = new CCache<Integer,P_Employee_Skill>("P_Employee_Skill", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Employee_Skill_ID id
	 */
	public P_Employee_Skill (Properties ctx, int P_Employee_Skill_ID, String trxName)
	{
		super (ctx, P_Employee_Skill_ID, trxName);
		if (P_Employee_Skill_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Employee_Skill

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Employee_Skill (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Employee_Skill (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Employee_Skill_ID"), trxName);
	}	//	P_Employee_Skill


	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true or false
	 */

	protected boolean beforeSave (boolean newRecord)
	{
		//+ 2011.07.05 + sécurité par compagnie.
 	    P_Employee Employee = P_Employee.get( Env.getCtx(), this.getP_Employee_ID(), this.get_TrxName());
		this.setAD_Org_ID( Employee.getAD_Org_ID());
		//- 2011.07.05 + sécurité par compagnie.

		return true;
	}	//	beforeSave
	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Employee_Skill[ID=")
			.append(this.getP_Employee_Skill_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	P_Employee_Skill
