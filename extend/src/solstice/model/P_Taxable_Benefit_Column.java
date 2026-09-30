package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Taxable_Benefit_Column Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Taxable_Benefit_Column extends X_P_Taxable_Benefit_Column
{
	/**
	 * 	Get Taxable_Benefit_Column
	 *	@param ctx context
	 * 	@param P_Taxable_Benefit_Column_ID id
	 *	@return Taxable_Benefit_Column
	 */
	public static P_Taxable_Benefit_Column get (Properties ctx, int P_Taxable_Benefit_Column_ID, String trxName)
	{
		Integer key = new Integer (P_Taxable_Benefit_Column_ID);
		P_Taxable_Benefit_Column Taxable_Benefit_Column = (P_Taxable_Benefit_Column)s_cache.get(key);
		if (Taxable_Benefit_Column != null)
			return Taxable_Benefit_Column;
		Taxable_Benefit_Column = new P_Taxable_Benefit_Column (ctx, P_Taxable_Benefit_Column_ID, trxName);
		s_cache.put (key, Taxable_Benefit_Column);
		return Taxable_Benefit_Column;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Taxable_Benefit_Column>	s_cache = new CCache<Integer,P_Taxable_Benefit_Column>("P_Taxable_Benefit_Column", 100);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Taxable_Benefit_Column_ID id
	 */
	public P_Taxable_Benefit_Column (Properties ctx, int P_Taxable_Benefit_Column_ID, String trxName)
	{
		super (ctx, P_Taxable_Benefit_Column_ID, trxName);
		if (P_Taxable_Benefit_Column_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Taxable_Benefit_Column

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Taxable_Benefit_Column (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Taxable_Benefit_Column (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Taxable_Benefit_Column_ID"), trxName);
	}	//	P_Taxable_Benefit_Column


	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true or false
	 */

	protected boolean beforeSave (boolean newRecord)
	{
		return true;
	}

}	//	P_Taxable_Benefit_Column
