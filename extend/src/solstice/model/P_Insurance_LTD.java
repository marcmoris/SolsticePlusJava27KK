package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  Insurance_LTD Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Insurance_LTD extends X_P_Insurance_LTD
{
	/**
	 * 	Get Insurance_LTD
	 *	@param ctx context
	 * 	@param P_Insurance_LTD_ID id
	 *	@return Insurance_LTD
	 */
	public static P_Insurance_LTD get (Properties ctx, int P_Insurance_LTD_ID, String trxName)
	{
		Integer key = new Integer (P_Insurance_LTD_ID);
		P_Insurance_LTD Insurance_LTD = (P_Insurance_LTD)s_cache.get(key);
		if (Insurance_LTD != null)
			return Insurance_LTD;
		Insurance_LTD = new P_Insurance_LTD (ctx, P_Insurance_LTD_ID, trxName);
		s_cache.put (key, Insurance_LTD);
		return Insurance_LTD;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Insurance_LTD>	s_cache = new CCache<Integer,P_Insurance_LTD>("P_Insurance_LTD", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Insurance_LTD_ID id
	 */
	public P_Insurance_LTD (Properties ctx, int P_Insurance_LTD_ID, String trxName)
	{
		super (ctx, P_Insurance_LTD_ID, trxName);
		if (P_Insurance_LTD_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Insurance_LTD

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Insurance_LTD (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Insurance_LTD (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Insurance_LTD_ID"), trxName);
	}	//	P_Insurance_LTD

	public static P_Insurance_LTD getByProfile (Properties ctx, int P_Profile_ID, Timestamp EffectIn, String trxName)
	{
	    String sql = "SELECT  P_Insurance_LTD_ID FROM P_Insurance_LTD "
            + " WHERE P_Profile_ID = " + P_Profile_ID
			+ "    and EffectIn<= " + DB.TO_DATE( EffectIn )  
			+ "  Order By EffectIn Desc";
	    
	    PreparedStatement pstmt = null;
    
	    int ID = 0;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				ID = rs.getInt(1);
			}
			    
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			
		}
		catch (Exception e)
		{
		    System.out.println( "P_Employee_Profile, get - " + e);
		    return null;
		}
	    
		if ( ID == 0 )
		   return null;
		
		P_Insurance_LTD Insurance_LTD = P_Insurance_LTD.get( ctx, ID, trxName );
		return Insurance_LTD;
	}	//	get



	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Insurance_LTD[ID=")
			.append(this.getP_Insurance_LTD_ID())
//			.append(",Value=").append(getValue())
//			.append(",Name=").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	P_Insurance_LTD
