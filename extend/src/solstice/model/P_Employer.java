package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  Employer Model
 *
 *  @author Marc Morissette
 *  @version $Id: P_Employer.java,v 1.1 2007/07/18 14:43:15 marmor01 Exp $
 */
public class P_Employer extends X_P_Employer
{
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Employer
	 *	@param ctx context
	 * 	@param P_Employer_ID id
	 *	@return Employer
	 */
	public static P_Employer get (Properties ctx, int P_Employer_ID, String trxName)
	{
		Integer key = new Integer (P_Employer_ID);
		P_Employer Employer = (P_Employer)s_cache.get(key);
		if (Employer != null)
			return Employer;
		Employer = new P_Employer (ctx, P_Employer_ID, trxName);
		s_cache.put (key, Employer);
		return Employer;
	}	//	get


	/**	Cache						*/
	private static CCache<Integer,P_Employer>	s_cache = new CCache<Integer,P_Employer>("P_Employer", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Employer_ID id
	 */
	public P_Employer (Properties ctx, int P_Employer_ID, String trxName)
	{
		super (ctx, P_Employer_ID, trxName);
		if (P_Employer_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Employer

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Employer (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Employer (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Employer_ID"), trxName);
	}	//	P_Employer

	public  static P_Employer getProvincialWithOrg ( Properties ctx, int AD_Org_ID, String trxName)
	{
		String sql = "Select top 1 P_Employer.P_Employer_ID From P_Employer"
			+ " Where P_Employer.TYPE =  'P'"
			+ " AND P_Employer.AD_Org_ID in ( 0, " + AD_Org_ID + " ) "
			+ " ORDER BY AD_ORG_ID DESC ";

		PreparedStatement pstmt = null;
		
		int P_Employer_ID = -1;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				P_Employer_ID = rs.getInt(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("P_Employer - getProvincialWithOrg - " + e);
		}
			
		return P_Employer.get( ctx, P_Employer_ID , trxName);

	}


}	//	P_Employer
