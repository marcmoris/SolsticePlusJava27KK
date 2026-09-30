package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Deduction_Exception_Employer Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class I_Deduction_Exception_Employer extends X_I_Deduction_Exception_Employer
{
	/**
	 * 	Get Deduction_Exception_Employer
	 *	@param ctx context
	 * 	@param I_Deduction_Exception_Employer_ID id
	 *	@return Deduction_Exception_Employer
	 */
	public static I_Deduction_Exception_Employer get (Properties ctx, int I_Deduction_Exception_Employer_ID, String trxName)
	{
		Integer key = new Integer (I_Deduction_Exception_Employer_ID);
		I_Deduction_Exception_Employer Deduction_Exception_Employer = (I_Deduction_Exception_Employer)s_cache.get(key);
		if (Deduction_Exception_Employer != null)
			return Deduction_Exception_Employer;
		Deduction_Exception_Employer = new I_Deduction_Exception_Employer (ctx, I_Deduction_Exception_Employer_ID, trxName);
		s_cache.put (key, Deduction_Exception_Employer);
		return Deduction_Exception_Employer;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,I_Deduction_Exception_Employer>	s_cache = new CCache<Integer,I_Deduction_Exception_Employer>("I_Deduction_Exception_Employer", 20);


	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param I_Deduction_Exception_Employer_ID id
	 */
	public I_Deduction_Exception_Employer (Properties ctx, int I_Deduction_Exception_Employer_ID, String trxName)
	{
		super (ctx, I_Deduction_Exception_Employer_ID, trxName);
		if (I_Deduction_Exception_Employer_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	I_Deduction_Exception_Employer

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public I_Deduction_Exception_Employer (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public I_Deduction_Exception_Employer (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#I_Deduction_Exception_Employer_ID"), trxName);
	}	//	I_Deduction_Exception_Employer



}	//	I_Deduction_Exception_Employer
