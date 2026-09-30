package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Training Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Training extends X_P_Training
{
	/**
	 * 	Get Training
	 *	@param ctx context
	 * 	@param P_Training_ID id
	 *	@return Training
	 */
	public static P_Training get (Properties ctx, int P_Training_ID, String trxName)
	{
		Integer key = new Integer (P_Training_ID);
		P_Training Training = (P_Training)s_cache.get(key);
		if (Training != null)
			return Training;
		Training = new P_Training (ctx, P_Training_ID, trxName);
		s_cache.put (key, Training);
		return Training;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Training>	s_cache = new CCache<Integer,P_Training>("P_Training", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Training_ID id
	 */
	public P_Training (Properties ctx, int P_Training_ID, String trxName)
	{
		super (ctx, P_Training_ID, trxName);
		if (P_Training_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Training

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Training (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Training (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Training_ID"), trxName);
	}	//	P_Training


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Training[ID=")
			.append(this.getP_Training_ID())
			.append(",Value=").append(getValue())
			.append(",Name=").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	P_Training
