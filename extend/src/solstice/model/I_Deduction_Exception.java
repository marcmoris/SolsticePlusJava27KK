package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Deduction_Exception Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class I_Deduction_Exception extends X_I_Deduction_Exception
{
	/**
	 * 	Get Deduction_Exception
	 *	@param ctx context
	 * 	@param I_Deduction_Exception_ID id
	 *	@return Deduction_Exception
	 */
	public static I_Deduction_Exception get (Properties ctx, int I_Deduction_Exception_ID, String trxName)
	{
		Integer key = new Integer (I_Deduction_Exception_ID);
		I_Deduction_Exception Deduction_Exception = (I_Deduction_Exception)s_cache.get(key);
		if (Deduction_Exception != null)
			return Deduction_Exception;
		Deduction_Exception = new I_Deduction_Exception (ctx, I_Deduction_Exception_ID, trxName);
		s_cache.put (key, Deduction_Exception);
		return Deduction_Exception;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,I_Deduction_Exception>	s_cache = new CCache<Integer,I_Deduction_Exception>("I_Deduction_Exception", 20);


	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param I_Deduction_Exception_ID id
	 */
	public I_Deduction_Exception (Properties ctx, int I_Deduction_Exception_ID, String trxName)
	{
		super (ctx, I_Deduction_Exception_ID, trxName);
		if (I_Deduction_Exception_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	I_Deduction_Exception

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public I_Deduction_Exception (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public I_Deduction_Exception (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#I_Deduction_Exception_ID"), trxName);
	}	//	I_Deduction_Exception



}	//	I_Deduction_Exception
