package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  Employee_Advance_Summary Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Employee_Advance_Summary extends X_P_Employee_Advance_Summary
{
	/**
	 * 	Get Employee_Advance_Summary
	 *	@param ctx context
	 * 	@param P_Employee_Advance_Summary_ID id
	 *	@return Employee_Advance_Summary
	 */
	public static P_Employee_Advance_Summary get (Properties ctx, int P_Employee_Advance_Summary_ID, String trxName)
	{
		Integer key = new Integer (P_Employee_Advance_Summary_ID);
		P_Employee_Advance_Summary Employee_Advance_Summary = (P_Employee_Advance_Summary)s_cache.get(key);
		if (Employee_Advance_Summary != null)
			return Employee_Advance_Summary;
		Employee_Advance_Summary = new P_Employee_Advance_Summary (ctx, P_Employee_Advance_Summary_ID, trxName);
		s_cache.put (key, Employee_Advance_Summary);
		return Employee_Advance_Summary;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Employee_Advance_Summary>	s_cache = new CCache<Integer,P_Employee_Advance_Summary>("P_Employee_Advance_Summary", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Employee_Advance_Summary_ID id
	 */
	public P_Employee_Advance_Summary (Properties ctx, int P_Employee_Advance_Summary_ID, String trxName)
	{
		super (ctx, P_Employee_Advance_Summary_ID, trxName);
		if (P_Employee_Advance_Summary_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Employee_Advance_Summary

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Employee_Advance_Summary (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Employee_Advance_Summary (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Employee_Advance_Summary_ID"), trxName);
	}	//	P_Employee_Advance_Summary

	
	//
	// Get Employee_Advance_Summary with Employee ID and Advance ID.
	//
	public static P_Employee_Advance_Summary get(Properties ctx, int P_Employee_ID, int P_Advance_ID, int P_SpecialRate_ID, String trxName)
	{
		String sql;
		if ( P_SpecialRate_ID == 0 )
		{
		    sql = "SELECT  P_Employee_Advance_Summary_ID FROM P_Employee_Advance_Summary "
	            + " WHERE P_Advance_ID   = " + P_Advance_ID
				+ "   AND P_Employee_ID  = " + P_Employee_ID
				+ " Order by Created Desc "
			;
		}
		else
		{
		    sql = "SELECT  P_Employee_Advance_Summary_ID FROM P_Employee_Advance_Summary "
	            + " WHERE P_Advance_ID   = " + P_Advance_ID
				+ "   AND P_Employee_ID  = " + P_Employee_ID
				+ "   AND isnull( P_SpecialRate_ID, 0 ) = " + P_SpecialRate_ID
				+ " Order by P_SpecialRate_ID Desc "
				;
		}
	    
	    PreparedStatement pstmt = null;
    
	    int P_Employee_Advance_Summary_ID = -1;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				P_Employee_Advance_Summary_ID = rs.getInt("P_Employee_Advance_Summary_ID");
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			
		}
		catch (Exception e)
		{
		    System.out.println( "P_Employee_Advance_Summary, get - " + e);
		    return null;
		}
	    
		return P_Employee_Advance_Summary.get( ctx, P_Employee_Advance_Summary_ID, trxName);
	}	//	get



}	//	P_Employee_Advance_Summary
