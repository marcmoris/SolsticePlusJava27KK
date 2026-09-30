package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Carra_Absence Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Carra_Absence extends X_P_Carra_Absence
{
	/**
	 * 	Get Carra_Absence
	 *	@param ctx context
	 * 	@param P_Carra_Absence_ID id
	 *	@return Carra_Absence
	 */
	public static P_Carra_Absence get (Properties ctx, int P_Carra_Absence_ID, String trxName)
	{
		Integer key = new Integer (P_Carra_Absence_ID);
		P_Carra_Absence Carra_Absence = (P_Carra_Absence)s_cache.get(key);
		if (Carra_Absence != null)
			return Carra_Absence;
		Carra_Absence = new P_Carra_Absence (ctx, P_Carra_Absence_ID, trxName);
		s_cache.put (key, Carra_Absence);
		return Carra_Absence;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Carra_Absence>	s_cache = new CCache<Integer,P_Carra_Absence>("P_Carra_Absence", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Carra_Absence_ID id
	 */
	public P_Carra_Absence (Properties ctx, int P_Carra_Absence_ID, String trxName)
	{
		super (ctx, P_Carra_Absence_ID, trxName);
		if (P_Carra_Absence_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Carra_Absence

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Carra_Absence (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Carra_Absence (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Carra_Absence_ID"), trxName);
	}	//	P_Carra_Absence


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Carra_Absence[ID=")
			.append(this.getP_Carra_Absence_ID())
			.append(",Name=").append(getAbsenceCode())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	P_Carra_Absence
