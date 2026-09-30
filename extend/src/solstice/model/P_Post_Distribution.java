/*
 * Created on 2005-06-16
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * @author kevmar01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_Post_Distribution extends X_P_Post_Distribution 
{
	
	private static CLogger		s_log = CLogger.getCLogger (P_Post_Distribution.class);

	/**
	 * 	Get Payment_Deduction
	 *	@param ctx context
	 * 	@param P_Payment_Deduction_ID id
	 *	@return Payment_Deduction
	 */
	public static P_Post_Distribution get (Properties ctx, int P_Post_Distribution_ID, String trxName)
	{
		Integer key = new Integer (P_Post_Distribution_ID);
		P_Post_Distribution post_Distribution = (P_Post_Distribution)s_cache.get(key);
		if (post_Distribution != null)
			return post_Distribution;
		post_Distribution = new P_Post_Distribution (ctx, P_Post_Distribution_ID, trxName);
		s_cache.put (key, post_Distribution);
		return post_Distribution;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Post_Distribution>	s_cache = new CCache<Integer,P_Post_Distribution>("P_Post_Distribution", 20);
	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Payment_Deduction_ID id
	 */
	public P_Post_Distribution (Properties ctx, int P_Post_Distribution_ID, String trxName)
	{
		super (ctx, P_Post_Distribution_ID, trxName);
		if (P_Post_Distribution_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Payment_Deduction

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Post_Distribution (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Post_Distribution (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Post_Distribution_ID"), trxName);
	}	//	P_Payment_Deduction


	public static P_Post_Distribution getWithPostID( Properties ctx, int P_Post_ID, String trxName  ) 
	{
		String sql = null;
		sql = "Select * from P_Post_Distribution WHERE P_Post_ID = " + P_Post_ID;
		//
		
		P_Post_Distribution Post_Distribution = null;
		
		PreparedStatement pstmt = null;
		
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				Post_Distribution = new P_Post_Distribution( ctx, rs, trxName);
			}
			
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Employee - " + sql, e);
		}

		return Post_Distribution;
	}




}
