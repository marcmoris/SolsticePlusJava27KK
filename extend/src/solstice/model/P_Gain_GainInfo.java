/*
 * Created on 2005-08-31
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * @author vivdun01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_Gain_GainInfo extends X_P_Gain_GainInfo 
{
	/**
	 * 	Get Gain_GainInfo
	 *	@param ctx context
	 * 	@param P_Gain_GainInfo_ID id
	 *	@return Gain_GainInfo
	 */
	public static P_Gain_GainInfo get (Properties ctx, int P_Gain_GainInfo_ID)
	{
		Integer key = new Integer (P_Gain_GainInfo_ID);
		P_Gain_GainInfo Gain_GainInfo = (P_Gain_GainInfo)s_cache.get(key);
		if (Gain_GainInfo != null)
			return Gain_GainInfo;
		Gain_GainInfo = new P_Gain_GainInfo (ctx, P_Gain_GainInfo_ID, null);
		s_cache.put (key, Gain_GainInfo);
		return Gain_GainInfo;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Gain_GainInfo>	s_cache = new CCache<Integer,P_Gain_GainInfo>("P_Gain_GainInfo", 20);
	private static CCache<String,P_Gain_GainInfo>	s_cache2 = new CCache<String,P_Gain_GainInfo>("P_Gain_GainInfo", 20);


	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Gain_GainInfo_ID id
	 */
	public P_Gain_GainInfo (Properties ctx, int P_Gain_GainInfo_ID, String trxName)
	{
		super (ctx, P_Gain_GainInfo_ID, trxName);
		if (P_Gain_GainInfo_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Gain_GainInfo

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Gain_GainInfo (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Gain_GainInfo (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Gain_GainInfo_ID"), trxName);
	}	//	P_Gain_GainInfo
	
	public static P_Gain_GainInfo get( Properties ctx, int P_Gain_ID, int P_GainInfo_ID )
	{
		String key = String.valueOf(P_Gain_ID) + '-' + String.valueOf(P_GainInfo_ID);
		P_Gain_GainInfo Gain_GainInfo = (P_Gain_GainInfo)s_cache2.get(key);
		if (Gain_GainInfo != null)
			return Gain_GainInfo;

		int id = 0;
		String sql = "Select P_Gain_GainInfo_ID from P_Gain_GainInfo Where P_Gain_ID = " + P_Gain_ID + " And P_GainInfo_ID = " + P_GainInfo_ID;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				id = rs.getInt("P_Gain_GainInfo_ID");
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("P_Gain_GainInfo.get - " + e);
		}

		Gain_GainInfo = P_Gain_GainInfo.get(ctx, id);
		s_cache2.put (key, Gain_GainInfo);

		return Gain_GainInfo;
		
	}


}
