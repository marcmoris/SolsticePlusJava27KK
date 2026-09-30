package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Carra_Retro Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Carra_Retro extends X_P_Carra_Retro
{
	/**
	 * 	Get Carra_Retro
	 *	@param ctx context
	 * 	@param P_Carra_Retro_ID id
	 *	@return Carra_Retro
	 */
	public static P_Carra_Retro get (Properties ctx, int P_Carra_Retro_ID, String trxName)
	{
		Integer key = new Integer (P_Carra_Retro_ID);
		P_Carra_Retro Carra_Retro = (P_Carra_Retro)s_cache.get(key);
		if (Carra_Retro != null)
			return Carra_Retro;
		Carra_Retro = new P_Carra_Retro (ctx, P_Carra_Retro_ID, trxName);
		s_cache.put (key, Carra_Retro);
		return Carra_Retro;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Carra_Retro>	s_cache = new CCache<Integer,P_Carra_Retro>("P_Carra_Retro", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Carra_Retro_ID id
	 */
	public P_Carra_Retro (Properties ctx, int P_Carra_Retro_ID, String trxName)
	{
		super (ctx, P_Carra_Retro_ID, trxName);
		if (P_Carra_Retro_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Carra_Retro

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Carra_Retro (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Carra_Retro (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Carra_Retro_ID"), trxName);
	}	//	P_Carra_Retro


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Carra_Retro[ID=")
			.append(this.getP_Carra_Retro_ID())
//			.append(",Value=").append(getValue())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	P_Carra_Retro
