package solstice.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  Gain_Bound Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Gain_Bound extends X_P_Gain_Bound
{
	/**
	 * 	Get Gain_Bound
	 *	@param ctx context
	 * 	@param P_Gain_Bound_ID id
	 *	@return Gain_Bound
	 */
	public static P_Gain_Bound get (Properties ctx, int P_Gain_Bound_ID, String trxName)
	{
		Integer key = new Integer (P_Gain_Bound_ID);
		P_Gain_Bound Gain_Bound = (P_Gain_Bound)s_cache.get(key);
		if (Gain_Bound != null)
			return Gain_Bound;
		Gain_Bound = new P_Gain_Bound (ctx, P_Gain_Bound_ID, trxName);
		s_cache.put (key, Gain_Bound);
		return Gain_Bound;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Gain_Bound>	s_cache = new CCache<Integer,P_Gain_Bound>("P_Gain_Bound", 20);


	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Employee.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Gain_Bound_ID id
	 */
	public P_Gain_Bound (Properties ctx, int P_Gain_Bound_ID, String trxName)
	{
		super (ctx, P_Gain_Bound_ID, trxName);
		if (P_Gain_Bound_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Gain_Bound

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Gain_Bound (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Gain_Bound (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Gain_Bound_ID"), trxName);
	}	//	P_Gain_Bound


	public static P_Gain_Bound get ( Properties ctx, int P_Employee_ID, int P_Gain_ID, int P_Job_Type, Timestamp d, String trxName )
	{
		String sql = "Select * From P_Gain_Bound Where P_Gain_ID = ? AND P_Job_Type_ID = ? AND IsActive='Y'";

		PreparedStatement pstmt = null;
		
		int P_Gain_Bound_ID = 0;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt(1, P_Gain_ID);
			pstmt.setInt(2, P_Job_Type);

			ResultSet rs = pstmt.executeQuery ();
			while ( rs.next () )
			{
				P_Credits Credits = P_Credits.get( Env.getCtx (), rs.getInt("P_Credits_ID"), trxName ) ;
				BigDecimal CreditsSoldes = P_Credits.getEmployeeSolde( Credits.getP_Credits_ID(), P_Employee_ID, d, trxName).divide(new BigDecimal( 365),2,BigDecimal.ROUND_HALF_UP);

				if ( rs.getBigDecimal("LIMITMIN").compareTo(CreditsSoldes) <= 0 &&
					 rs.getBigDecimal("LIMITMAX").compareTo(CreditsSoldes) >= 0	)
					P_Gain_Bound_ID = rs.getInt("P_Gain_Bound_ID");
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE," P_Gain_Bound - get - " + sql, e);
		}
			
		if ( P_Gain_Bound_ID != 0 )
			return P_Gain_Bound.get( Env.getCtx(), P_Gain_Bound_ID, trxName);
		else
			return null;
		
	}

	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Gain_Bound[ID=")
			.append(this.getP_Gain_Bound_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	P_Gain_Bound
