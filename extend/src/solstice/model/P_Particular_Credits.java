package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Particular_Credits Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Particular_Credits extends X_P_Particular_Credits
{
	/**
	 * 	Get Particular_Credits
	 *	@param ctx context
	 * 	@param P_Particular_Credits_ID id
	 *	@return Particular_Credits
	 */
	public static P_Particular_Credits get (Properties ctx, int P_Particular_Credits_ID, String trxName)
	{
		Integer key = new Integer (P_Particular_Credits_ID);
		P_Particular_Credits Particular_Credits = (P_Particular_Credits)s_cache.get(key);
		if (Particular_Credits != null)
			return Particular_Credits;
		Particular_Credits = new P_Particular_Credits (ctx, P_Particular_Credits_ID, trxName);
		s_cache.put (key, Particular_Credits);
		return Particular_Credits;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Particular_Credits>	s_cache = new CCache<Integer,P_Particular_Credits>("P_Particular_Credits", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Particular_Credits_ID id
	 */
	public P_Particular_Credits (Properties ctx, int P_Particular_Credits_ID, String trxName)
	{
		super (ctx, P_Particular_Credits_ID, trxName);
		if (P_Particular_Credits_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Particular_Credits

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Particular_Credits (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Particular_Credits (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Particular_Credits_ID"), trxName);
	}	//	P_Particular_Credits


	protected boolean beforeSave (boolean newRecord)
	{
		//+ 2011.07.05 + sécurité par compagnie.
		P_Employee Employee = P_Employee.get(getCtx(), getP_Employee_ID(), this.get_TrxName());
		this.setAD_Org_ID( Employee.getAD_Org_ID());
		//- 2011.07.05 + sécurité par compagnie.
		return true;
	}
		

	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Particular_Credits[ID=")
			.append(this.getP_Particular_Credits_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	P_Particular_Credits
