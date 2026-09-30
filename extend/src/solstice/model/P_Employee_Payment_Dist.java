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
 *  Employee_Payment_Dist Model
 *
 *  @author Réjean Garant - Progestion Inf
 */
public class P_Employee_Payment_Dist extends X_P_Employee_Payment_Dist
{
	/**
	 * 	Get Employee_Payment_Dist
	 *	@param ctx context
	 * 	@param P_Employee_Payment_Dist_ID id
	 *	@return Employee_Payment_Dist
	 */
	public static P_Employee_Payment_Dist get (Properties ctx, int P_Employee_Payment_Dist_ID, String trxName)
	{
		Integer key = new Integer (P_Employee_Payment_Dist_ID);
		P_Employee_Payment_Dist Employee_Payment_Dist = (P_Employee_Payment_Dist)s_cache.get(key);
		if (Employee_Payment_Dist != null)
			return Employee_Payment_Dist;
		Employee_Payment_Dist = new P_Employee_Payment_Dist (ctx, P_Employee_Payment_Dist_ID, trxName);
		s_cache.put (key, Employee_Payment_Dist);
		return Employee_Payment_Dist;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Employee_Payment_Dist>	s_cache = new CCache<Integer,P_Employee_Payment_Dist>("P_Employee_Payment_Dist", 20);

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Employee_Payment_Dist.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Employee_Payment_Dist_ID id
	 */
	public P_Employee_Payment_Dist (Properties ctx, int P_Employee_Payment_Dist_ID, String trxName)
	{
		super (ctx, P_Employee_Payment_Dist_ID, trxName);
		if (P_Employee_Payment_Dist_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Employee_Payment_Dist

	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Employee_Payment_Dist (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}

	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Employee_Payment_Dist (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Employee_Payment_Dist_ID"), trxName);
	}	//	P_Employee_Payment_Dist


	public static int get_Employee_Payment_Dist( int P_Employee_ID )
	{

		String sql = null;
		sql = "Select P_Employee_Payment_Dist_ID "; 
		sql += " From P_Employee_Payment_Dist";
		sql += " WHERE P_Employee_Payment_Dist.IsActive='Y' ";
		sql += " AND P_Employee_Payment_Dist.P_Employee_ID=" + P_Employee_ID;

		int iID = 0;

		PreparedStatement pstmt = null;

		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				iID = rs.getInt(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Employee_Payment_Dist - get_Employee_Payment_Dist - " + sql, e);
		}
			

		return iID;
		
	}

	

}	//	P_Employee_Payment_Dist
