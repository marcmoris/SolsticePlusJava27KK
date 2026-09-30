package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.model.MClient;
import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  Year Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Year extends X_P_Year
{
	/**
	 * 	Get Year
	 *	@param ctx context
	 * 	@param P_Year_ID id
	 *	@return Year
	 */
	public static P_Year get (Properties ctx, int P_Year_ID, String trxName)
	{
		Integer key = new Integer (P_Year_ID);
		P_Year Year = (P_Year)s_cache.get(key);
		if (Year != null)
			return Year;
		Year = new P_Year (ctx, P_Year_ID, trxName);
		s_cache.put (key, Year);
		return Year;
	}	//	get


	/**	Cache						*/
	private static CCache<Integer,P_Year>	s_cache = new CCache<Integer,P_Year>("P_Year", 10);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Year_ID id
	 */
	public P_Year (Properties ctx, int P_Year_ID, String trxName)
	{
		super (ctx, P_Year_ID, trxName);
		if (P_Year_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Year

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Year (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}
	
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Year (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Year_ID"), trxName);
	}	//	P_Year


	public static P_Year getWithValue( Properties ctx, int year, String trxName )
	{
		int id = 0;
		String sql = "Select P_Year_ID from P_Year Where Year = '" + year + "'";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				id = rs.getInt("P_Year_ID");
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("P_Gain.get - " + e);
		}
		
		return P_Year.get(ctx, id, trxName);
		
	}


}	//	P_Year
