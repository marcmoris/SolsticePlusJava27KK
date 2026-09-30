package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  Insurance_Program Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Insurance_Program extends X_P_Insurance_Program
{
	/**
	 * 	Get Insurance_Program
	 *	@param ctx context
	 * 	@param P_Insurance_Program_ID id
	 *	@return Insurance_Program
	 */
	public static P_Insurance_Program get (Properties ctx, int P_Insurance_Program_ID, String trxName)
	{
		Integer key = new Integer (P_Insurance_Program_ID);
		P_Insurance_Program Insurance_Program = (P_Insurance_Program)s_cache.get(key);
		if (Insurance_Program != null)
			return Insurance_Program;
		Insurance_Program = new P_Insurance_Program (ctx, P_Insurance_Program_ID, trxName);
		s_cache.put (key, Insurance_Program);
		return Insurance_Program;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Insurance_Program>	s_cache = new CCache<Integer,P_Insurance_Program>("P_Insurance_Program", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Insurance_Program_ID id
	 */
	public P_Insurance_Program (Properties ctx, int P_Insurance_Program_ID, String trxName)
	{
		super (ctx, P_Insurance_Program_ID, trxName);
		if (P_Insurance_Program_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Insurance_Program

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Insurance_Program (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Insurance_Program (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Insurance_Program_ID"), trxName);
	}	//	P_Insurance_Program


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Insurance_Program[ID=")
			.append(this.getP_Insurance_Program_ID())
			.append(",Value=").append(getValue())
			.append(",Name=").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
	
	public static P_Insurance_Program getByProfile (Properties ctx, int P_Profile_ID, String trxName)
	{
	    String sql = "SELECT  P_Insurance_Program_ID FROM P_Insurance_Program "
            + " WHERE P_Profile_ID  = " + P_Profile_ID
            + " AND isactive = 'Y' "
            ;
	    
	    PreparedStatement pstmt = null;
    
	    int P_Insurance_Program_ID = 0;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				P_Insurance_Program_ID = rs.getInt(1);
			}
			    
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			
		}
		catch (Exception e)
		{
		    System.out.println( "P_Insurance_Program, get - " + e);
		    return null;
		}
	    
		if ( P_Insurance_Program_ID == 0 )
		   return null;
		
		P_Insurance_Program Insurance_Program = P_Insurance_Program.get( ctx, P_Insurance_Program_ID, trxName );
		return Insurance_Program;
	}	//	get


}	//	P_Insurance_Program
