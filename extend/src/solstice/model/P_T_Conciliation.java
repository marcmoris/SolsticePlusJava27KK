/*
 * Created on 2005-09-05
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 * @author alenav01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_T_Conciliation extends X_P_T_Conciliation
{

	public static P_T_Conciliation get (Properties ctx, int P_T_Conciliation_ID, String trxName)
	{
		Integer key = new Integer (P_T_Conciliation_ID);
		P_T_Conciliation Payment_Registry = (P_T_Conciliation)s_cache.get(key);
		if (Payment_Registry != null)
			return Payment_Registry;
		Payment_Registry = new P_T_Conciliation (ctx, P_T_Conciliation_ID, trxName);
		s_cache.put (key, Payment_Registry);
		return Payment_Registry;
	}	//	get

	/**	Cache						*/

	private static CCache<Integer,P_T_Conciliation>	s_cache = new CCache<Integer,P_T_Conciliation>("P_T_Conciliation", 2);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_T_Conciliation_ID id
	 */
	public P_T_Conciliation (Properties ctx, int P_T_Conciliation_ID, String trxName)
	{
		super (ctx, P_T_Conciliation_ID, trxName);
		if (P_T_Conciliation_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_T_Conciliation

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_T_Conciliation (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_T_Conciliation (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_T_Conciliation_ID"), trxName);
	}	//	P_T_Conciliation


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_T_Conciliation[ID=")
			.append(getP_T_Conciliation_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString
		
	
}
