package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

public class P_Proposed_Adv_Date extends X_P_Proposed_Adv_Date 
{
	public static P_Proposed_Adv_Date get (Properties ctx, int P_Proposed_Adv_Date_ID, String trxName)
	{
		Integer key = new Integer (P_Proposed_Adv_Date_ID);
		P_Proposed_Adv_Date ProposedDate = (P_Proposed_Adv_Date)s_cache.get(key);
		if (ProposedDate != null)
			return ProposedDate;
		ProposedDate = new P_Proposed_Adv_Date (ctx, P_Proposed_Adv_Date_ID, trxName);
		s_cache.put (key, ProposedDate);
		return ProposedDate;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Proposed_Adv_Date>	s_cache = new CCache<Integer,P_Proposed_Adv_Date>("P_Proposed_Adv_Date", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Bonus_ID id
	 */
	public P_Proposed_Adv_Date (Properties ctx, int P_Proposed_Adv_Date_ID, String trxName)
	{
		super (ctx, P_Proposed_Adv_Date_ID, trxName);
		if (P_Proposed_Adv_Date_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Bonus

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Proposed_Adv_Date (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Proposed_Adv_Date (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Proposed_Adv_Date_ID"), trxName);
	}	//	P_Bonus


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	/*public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Proposed_Adv_Date[ID=")
			.append(this.getP_Proposed_Adv_Date_ID())
			//.append(",Value=").append(getValue())
			//.append(",Name=").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString
*/
}
