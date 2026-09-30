package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  Insurance_Dental Model
 *
 *  @author Marc Morissette - Solstice Plus
 */
public class P_Insurance_Dental extends X_P_Insurance_Dental
{
	/**
	 * 	Get Insurance_Dental
	 *	@param ctx context
	 * 	@param P_Insurance_Dental_ID id
	 *	@return Insurance_Dental
	 */
	public static P_Insurance_Dental get (Properties ctx, int P_Insurance_Dental_ID, String trxName)
	{
		Integer key = new Integer (P_Insurance_Dental_ID);
		P_Insurance_Dental Insurance_Dental = (P_Insurance_Dental)s_cache.get(key);
		if (Insurance_Dental != null)
			return Insurance_Dental;
		Insurance_Dental = new P_Insurance_Dental (ctx, P_Insurance_Dental_ID, trxName);
		s_cache.put (key, Insurance_Dental);
		return Insurance_Dental;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Insurance_Dental>	s_cache = new CCache<Integer,P_Insurance_Dental>("P_Insurance_Dental", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Insurance_Dental_ID id
	 */
	public P_Insurance_Dental (Properties ctx, int P_Insurance_Dental_ID, String trxName)
	{
		super (ctx, P_Insurance_Dental_ID, trxName);
		if (P_Insurance_Dental_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Insurance_Dental

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Insurance_Dental (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Insurance_Dental (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Insurance_Dental_ID"), trxName);
	}	//	P_Insurance_Dental


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Insurance_Dental[ID=")
			.append(this.getP_Insurance_Dental_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
	
	public static P_Insurance_Dental getByProfile (Properties ctx, int P_Profile_ID, int P_Period_ID, String trxName)
	{
		P_Period Period = P_Period.get(ctx, P_Period_ID, trxName);
		
	    String sql = "SELECT  P_Insurance_Dental_ID FROM P_Insurance_Dental "
            + " WHERE P_Profile_ID  = " + P_Profile_ID
            + "  AND " + DB.TO_DATE(Period.getStartDate()) + " Between EffectIn and Isnull( EffectTo, " + DB.TO_DATE(Period.getStartDate()) + "+1)"
            ;
	    
	    PreparedStatement pstmt = null;
    
	    int P_Insurance_Dental_ID = 0;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				P_Insurance_Dental_ID = rs.getInt(1);
			}
			    
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			
		}
		catch (Exception e)
		{
		    System.out.println( "P_Insurance_Dental, get - " + e);
		    return null;
		}
	    
		if ( P_Insurance_Dental_ID == 0 )
		   return null;
		
		P_Insurance_Dental Insurance_Dental = P_Insurance_Dental.get( ctx, P_Insurance_Dental_ID, trxName );
		return Insurance_Dental;
	}	//	get


}	//	P_Insurance_Dental
