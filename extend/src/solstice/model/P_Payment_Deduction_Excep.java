package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.CLogger;
import java.util.logging.Level;

/**
 *  Payment_Deduction_Excep Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Payment_Deduction_Excep extends X_P_Payment_Deduction_Excep
{
	/**
	 * 	Get Payment_Deduction_Excep
	 *	@param ctx context
	 * 	@param P_Payment_Deduction_Excep_ID id
	 *	@return Payment_Deduction_Excep
	 */
	public static P_Payment_Deduction_Excep get (Properties ctx, int P_Payment_Deduction_Excep_ID, String trxName)
	{
		Integer key = new Integer (P_Payment_Deduction_Excep_ID);
		P_Payment_Deduction_Excep Payment_Deduction_Excep = (P_Payment_Deduction_Excep)s_cache.get(key);
		if (Payment_Deduction_Excep != null)
			return Payment_Deduction_Excep;
		Payment_Deduction_Excep = new P_Payment_Deduction_Excep (ctx, P_Payment_Deduction_Excep_ID, trxName);
		s_cache.put (key, Payment_Deduction_Excep);
		return Payment_Deduction_Excep;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Payment_Deduction_Excep>	s_cache = new CCache<Integer,P_Payment_Deduction_Excep>("P_Payment_Deduction_Excep", 20);

	private static CLogger		log = CLogger.getCLogger (P_Payment_Deduction_Excep.class);


	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Payment_Deduction_Excep_ID id
	 */
	public P_Payment_Deduction_Excep (Properties ctx, int P_Payment_Deduction_Excep_ID, String trxName)
	{
		super (ctx, P_Payment_Deduction_Excep_ID, trxName);
		if (P_Payment_Deduction_Excep_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Payment_Deduction_Excep

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Payment_Deduction_Excep (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Payment_Deduction_Excep (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Payment_Deduction_Excep_ID"), trxName);
	}	//	P_Payment_Deduction_Excep

	public static P_Payment_Deduction_Excep[] get( Properties ctx, int P_Payment_ID, int P_Employee_Deduction_ID, int P_Period_ID, int P_Employee_ID, String trxName)
	{
		P_Payment_Deduction_Excep[] Payment_Deduction_Excep = {null,null,null,null,null};
		P_Payment Payment = P_Payment.get( ctx, P_Payment_ID, trxName);
		String sql = "SELECT P_Payment_Deduction_Excep_ID FROM P_Payment_Deduction_Excep WHERE P_Employee_Deduction_ID = ? and P_Payment_ID in ( select P_Payment_ID from P_Payment where P_Payment.P_Employee_ID = ? and  ( P_Payment.P_Period_ID = ? or P_Payment.P_Period_ID = ? ) )  and P_Period_ID = ? ";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, P_Employee_Deduction_ID);
			pstmt.setInt (2, P_Employee_ID);
			pstmt.setInt (3, P_Period_ID);
			pstmt.setInt (4, Payment.getP_Period_ID());
			pstmt.setInt (5, P_Period_ID);
			ResultSet rs = pstmt.executeQuery ();
			
			int i = 0;
			while ( rs.next ())
			{
				Payment_Deduction_Excep[i] = P_Payment_Deduction_Excep.get( ctx, rs.getInt( 1 ) , trxName);
				i = i+1;
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		} catch (Exception e)
		{
			log.log (Level.SEVERE,"calcul - " + sql, e);
		}
		return Payment_Deduction_Excep;
	}
	
	public static P_Payment_Deduction_Excep get( Properties ctx, int P_Payment_ID, int P_Employee_Deduction_ID, int P_Period_ID, String trxName)
	{
		int P_Payment_Deduction_Excep_ID = 0;
		String sql = "SELECT P_Payment_Deduction_Excep_ID FROM P_Payment_Deduction_Excep WHERE P_Employee_Deduction_ID = ? and P_Payment_ID = ? and P_Period_ID = ? ";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, P_Employee_Deduction_ID);
			pstmt.setInt (2, P_Payment_ID);
			pstmt.setInt (3, P_Period_ID);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next ())
				P_Payment_Deduction_Excep_ID = rs.getInt( 1 );
			rs.close ();
			pstmt.close ();
			pstmt = null;
		} catch (Exception e)
		{
//			log.error ("getLines", e);
			return null;
		}
		if ( P_Payment_Deduction_Excep_ID != 0)
			return P_Payment_Deduction_Excep.get( ctx, P_Payment_Deduction_Excep_ID , trxName);
		else
			return null;
	}
/*
	
	public static P_Payment_Deduction_Excep get( Properties ctx, int P_Payment_ID, int P_Employee_Deduction_ID, int P_Period_ID, int P_Employee_ID, String trxName)
	{
		int P_Payment_Deduction_Excep_ID = 0;
		P_Payment Payment = P_Payment.get( ctx, P_Payment_ID, trxName);
//		String sql = "SELECT P_Payment_Deduction_Excep_ID FROM P_Payment_Deduction_Excep WHERE P_Employee_Deduction_ID = ? and P_Payment_ID = ? and P_Period_ID = ? ";
		String sql = "SELECT P_Payment_Deduction_Excep_ID FROM P_Payment_Deduction_Excep WHERE P_Employee_Deduction_ID = ? and P_Payment_ID in ( select P_Payment_ID from P_Payment where P_Payment.P_Employee_ID = ? and  P_Payment.P_Period_ID = ? )  and P_Period_ID = ? ";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, P_Employee_Deduction_ID);
//			pstmt.setInt (2, P_Payment_ID);
			pstmt.setInt (2, P_Employee_ID);
// 2010.03.19 -- une exception pour avoir lieux sur plusieurs période			
//			pstmt.setInt (3, P_Period_ID);
			pstmt.setInt (3, Payment.getP_Period_ID());
			pstmt.setInt (4, P_Period_ID);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next ())
				P_Payment_Deduction_Excep_ID = rs.getInt( 1 );
			rs.close ();
			pstmt.close ();
			pstmt = null;
		} catch (Exception e)
		{
//			log.error ("getLines", e);
			return null;
		}
		if ( P_Payment_Deduction_Excep_ID != 0)
			return P_Payment_Deduction_Excep.get( ctx, P_Payment_Deduction_Excep_ID , trxName);
		else
			return null;
	}
*/
	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Payment_Deduction_Excep[ID=")
			.append(this.getP_Payment_Deduction_Excep_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	P_Payment_Deduction_Excep
