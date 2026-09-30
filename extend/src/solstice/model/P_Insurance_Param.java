package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  Insurance_Param Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Insurance_Param extends X_P_Insurance_Param
{
	/**
	 * 	Get Insurance_Param
	 *	@param ctx context
	 * 	@param P_Insurance_Param_ID id
	 *	@return Insurance_Param
	 */
	public static P_Insurance_Param get (Properties ctx, int P_Insurance_Param_ID, String trxName)
	{
		Integer key = new Integer (P_Insurance_Param_ID);
		P_Insurance_Param Insurance_Param = (P_Insurance_Param)s_cache.get(key);
		if (Insurance_Param != null)
			return Insurance_Param;
		Insurance_Param = new P_Insurance_Param (ctx, P_Insurance_Param_ID, trxName);
		s_cache.put (key, Insurance_Param);
		return Insurance_Param;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Insurance_Param>	s_cache = new CCache<Integer,P_Insurance_Param>("P_Insurance_Param", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Insurance_Param_ID id
	 */
	public P_Insurance_Param (Properties ctx, int P_Insurance_Param_ID, String trxName)
	{
		super (ctx, P_Insurance_Param_ID, trxName);
		if (P_Insurance_Param_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Insurance_Param

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Insurance_Param (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Insurance_Param (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Insurance_Param_ID"), trxName);
	}	//	P_Insurance_Param

	
	
	public static P_Insurance_Param  get (Properties ctx, int P_Department_ID, Timestamp EffectIn, String trxName)
	{
	    String sql = "SELECT  P_insurance_Param_ID FROM P_Insurance_Param "
            + " WHERE P_Department_ID = " + P_Department_ID
//			+ "    and EffectIn<= " + DB.TO_DATE( EffectIn )  
//			+ "  Order By EffectIn Desc";
	    ;
	    
	    PreparedStatement pstmt = null;
    
	    int P_insurance_Param_ID = 0;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				P_insurance_Param_ID = rs.getInt(1);
			}
			    
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			
		}
		catch (Exception e)
		{
		    System.out.println( "P_insurance_Param, get - " + e);
		    return null;
		}
	    
		if ( P_insurance_Param_ID == 0 )
		   return null;
		
		P_Insurance_Param  insurance_Param = P_Insurance_Param.get( ctx, P_insurance_Param_ID, trxName );
		return insurance_Param;
	}	//	get


	public static P_Insurance_Param  get (Properties ctx, int P_Insurance_Program_ID, int P_Deduction_ID, Timestamp EffectIn, String trxName)
	{
	    String sql = "SELECT TOP 1  P_insurance_Param_ID FROM P_Insurance_Param "
            + " WHERE P_Insurance_Program_ID = " + P_Insurance_Program_ID
            + " AND   P_deduction_id = " + P_Deduction_ID
            + " AND Isactive = 'Y' "
//			+ "    and EffectIn <= " + DB.TO_DATE( EffectIn )  
//			+ "  Order By EffectIn";
	    ;
	    
	    PreparedStatement pstmt = null;
    
	    int P_insurance_Param_ID = 0;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				P_insurance_Param_ID = rs.getInt(1);
			}
			    
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			
		}
		catch (Exception e)
		{
		    System.out.println( "P_insurance_Param, get - " + e);
		    return null;
		}
	    
		if ( P_insurance_Param_ID == 0 )
		   return null;
		
		P_Insurance_Param  insurance_Param = P_Insurance_Param.get( ctx, P_insurance_Param_ID, trxName );
		return insurance_Param;
	}	//	get


	

	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Insurance_Param[ID=")
			.append(this.getP_Insurance_Param_ID())
//			.append(",Value=").append(getValue())
//			.append(",Name=").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	P_Insurance_Param
