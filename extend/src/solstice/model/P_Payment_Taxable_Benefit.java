package solstice.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  Payment_Taxable_Benefit Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Payment_Taxable_Benefit extends X_P_Payment_Taxable_Benefit
{
	/**
	 * 	Get Payment_Taxable_Benefit
	 *	@param ctx context
	 * 	@param P_Payment_Taxable_Benefit_ID id
	 *	@return Payment_Taxable_Benefit
	 */
	public static P_Payment_Taxable_Benefit get (Properties ctx, int P_Payment_Taxable_Benefit_ID, String trxName)
	{
		Integer key = new Integer (P_Payment_Taxable_Benefit_ID);
		P_Payment_Taxable_Benefit Payment_Taxable_Benefit = (P_Payment_Taxable_Benefit)s_cache.get(key);
		if (Payment_Taxable_Benefit != null)
			return Payment_Taxable_Benefit;
		Payment_Taxable_Benefit = new P_Payment_Taxable_Benefit (ctx, P_Payment_Taxable_Benefit_ID, trxName);
		s_cache.put (key, Payment_Taxable_Benefit);
		return Payment_Taxable_Benefit;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Payment_Taxable_Benefit>	s_cache = new CCache<Integer,P_Payment_Taxable_Benefit>("P_Payment_Taxable_Benefit", 100);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Payment_Taxable_Benefit_ID id
	 */
	public P_Payment_Taxable_Benefit (Properties ctx, int P_Payment_Taxable_Benefit_ID, String trxName)
	{
		super (ctx, P_Payment_Taxable_Benefit_ID, trxName);
		if (P_Payment_Taxable_Benefit_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Payment_Taxable_Benefit

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Payment_Taxable_Benefit (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Payment_Taxable_Benefit (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Payment_Taxable_Benefit_ID"), trxName);
	}	//	P_Payment_Taxable_Benefit


	public static P_Payment_Taxable_Benefit get (Properties ctx, int paymentID, int Taxable_BenefitID, int Period_ID, String trxName)
	{
	    String sql = "SELECT  P_Payment_Taxable_Benefit_ID FROM P_Payment_Taxable_Benefit "
            + " WHERE P_Payment_ID = " + paymentID
            + "   AND P_Taxable_Benefit_ID  = " + Taxable_BenefitID
            + "   AND P_Period_ID = " + Period_ID
            ;
	    
	    PreparedStatement pstmt = null;
    
	    int P_Payment_Taxable_Benefit_ID = -1;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				P_Payment_Taxable_Benefit_ID = rs.getInt("P_Payment_Taxable_Benefit_ID");
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			
		}
		catch (Exception e)
		{
		    System.out.println( "P_Payment_Taxable_Benefit_ID, get - " + e);
		    return null;
		}
	    
		P_Payment_Taxable_Benefit Payment_Taxable_Benefit;
		if ( P_Payment_Taxable_Benefit_ID == -1 )
			Payment_Taxable_Benefit = new P_Payment_Taxable_Benefit( ctx, -1, trxName );
		else
			Payment_Taxable_Benefit = P_Payment_Taxable_Benefit.get( ctx, P_Payment_Taxable_Benefit_ID, trxName );

		return Payment_Taxable_Benefit;
	}	//	get


	protected boolean beforeSave (boolean newRecord)
	{
		if ( this.getP_Year_ID() == 0 )
		{
			P_Period Period = P_Period.get( Env.getCtx(), this.getP_Period_ID(), this.get_TrxName());
			this.setP_Year_ID( Period.getP_Year_ID());
		}
		return true;
	}


}	//	P_Payment_Taxable_Benefit
