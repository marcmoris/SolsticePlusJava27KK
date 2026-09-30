/*
 * Created on 2005-08-30
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
 * @author vivdun01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_Deduction_Category extends X_P_Deduction_Category 
{

	/**
	 * 	Get Deduction_Category
	 *	@param ctx context
	 * 	@param P_Deduction_Category_ID id
	 *	@return Deduction_Category
	 */
	public static P_Deduction_Category get (Properties ctx, int P_Deduction_Category_ID)
	{
		Integer key = new Integer (P_Deduction_Category_ID);
		P_Deduction_Category Deduction_Category = (P_Deduction_Category)s_cache.get(key);
		if (Deduction_Category != null)
			return Deduction_Category;
		Deduction_Category = new P_Deduction_Category (ctx, P_Deduction_Category_ID, null);
		s_cache.put (key, Deduction_Category);
		return Deduction_Category;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Deduction_Category>	s_cache = new CCache<Integer,P_Deduction_Category>("P_Deduction_Category", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Deduction_Category_ID id
	 */
	public P_Deduction_Category (Properties ctx, int P_Deduction_Category_ID, String trxName)
	{
		super (ctx, P_Deduction_Category_ID, trxName);
		if (P_Deduction_Category_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Deduction_Category

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Deduction_Category (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Deduction_Category (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Deduction_Category_ID"), trxName);
	}	//	P_Deduction_Category
	
}	// P_Deduction_Category
