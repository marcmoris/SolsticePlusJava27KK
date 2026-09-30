package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  Employee_Advance Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Employee_Advance extends X_P_Employee_Advance
{
	/**
	 * 	Get Employee_Advance
	 *	@param ctx context
	 * 	@param P_Employee_Advance_ID id
	 *	@return Employee_Advance
	 */
	public static P_Employee_Advance get (Properties ctx, int P_Employee_Advance_ID, String trxName)
	{
		Integer key = new Integer (P_Employee_Advance_ID);
		P_Employee_Advance Employee_Advance = (P_Employee_Advance)s_cache.get(key);
		if (Employee_Advance != null)
			return Employee_Advance;
		Employee_Advance = new P_Employee_Advance (ctx, P_Employee_Advance_ID, trxName);
		s_cache.put (key, Employee_Advance);
		return Employee_Advance;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Employee_Advance>	s_cache = new CCache<Integer,P_Employee_Advance>("P_Employee_Advance", 20);


	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Employee_Advance_ID id
	 */
	public P_Employee_Advance (Properties ctx, int P_Employee_Advance_ID, String trxName)
	{
		super (ctx, P_Employee_Advance_ID, trxName);
		if (P_Employee_Advance_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Employee_Advance

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Employee_Advance (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Employee_Advance (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Employee_Advance_ID"), trxName);
	}	//	P_Employee_Advance

	
	//
	// Get Employee_Advance with Employee ID and Advance ID.
	//
	public static P_Employee_Advance get(Properties ctx, int P_Employee_ID, int P_Advance_ID, String trxName)
	{
	    String sql = "SELECT  P_Employee_Advance_ID FROM P_Employee_Advance "
            + " WHERE P_Advance_ID   = " + P_Advance_ID
			+ "   AND P_Employee_ID  = " + P_Employee_ID
			;
	    
	    PreparedStatement pstmt = null;
    
	    int P_Employee_Advance_ID = -1;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				P_Employee_Advance_ID = rs.getInt("P_Employee_Advance_ID");
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			
		}
		catch (Exception e)
		{
		    System.out.println( "P_Employee_Advance, get - " + e);
		    return null;
		}
	    
		return P_Employee_Advance.get( ctx, P_Employee_Advance_ID, trxName);
	}	//	get

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true or false
	 */

	protected boolean beforeSave (boolean newRecord)
	{
		//+ 2011.07.05 + sécurité par compagnie.
 	    P_Employee Employee = P_Employee.get( Env.getCtx(), this.getP_Employee_ID(), this.get_TrxName());
		this.setAD_Org_ID( Employee.getAD_Org_ID());
		//- 2011.07.05 + sécurité par compagnie.

		return true;
	}	//	beforeSave
	

}	//	P_Employee_Advance
