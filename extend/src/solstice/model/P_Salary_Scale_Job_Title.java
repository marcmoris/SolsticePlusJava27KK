package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Salary_Scale_Job_Title Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Salary_Scale_Job_Title extends X_P_Salary_Scale_Job_Title
{
	/**
	 * 	Get Salary_Scale_Job_Title
	 *	@param ctx context
	 * 	@param P_Salary_Scale_Job_Title_ID id
	 *	@return Salary_Scale_Job_Title
	 */
	public static P_Salary_Scale_Job_Title get (Properties ctx, int P_Salary_Scale_Job_Title_ID, String trxName)
	{
		Integer key = new Integer (P_Salary_Scale_Job_Title_ID);
		P_Salary_Scale_Job_Title Salary_Scale_Job_Title = (P_Salary_Scale_Job_Title)s_cache.get(key);
		if (Salary_Scale_Job_Title != null)
			return Salary_Scale_Job_Title;
		Salary_Scale_Job_Title = new P_Salary_Scale_Job_Title (ctx, P_Salary_Scale_Job_Title_ID, trxName);
		s_cache.put (key, Salary_Scale_Job_Title);
		return Salary_Scale_Job_Title;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Salary_Scale_Job_Title>	s_cache = new CCache<Integer,P_Salary_Scale_Job_Title>("P_Salary_Scale_Job_Title", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Salary_Scale_Job_Title_ID id
	 */
	public P_Salary_Scale_Job_Title (Properties ctx, int P_Salary_Scale_Job_Title_ID, String trxName)
	{
		super (ctx, P_Salary_Scale_Job_Title_ID, trxName);
		if (P_Salary_Scale_Job_Title_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Salary_Scale_Job_Title

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Salary_Scale_Job_Title (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Salary_Scale_Job_Title (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Salary_Scale_Job_Title_ID"), trxName);
	}	//	P_Salary_Scale_Job_Title


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Salary_Scale_Job_Title[ID=")
			.append(getP_Salary_Scale_Job_Title_ID())
//			.append(",Value=").append(getValue())
//			.append(",Name=").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	P_Salary_Scale_Job_Title
