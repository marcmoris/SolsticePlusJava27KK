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
import org.compiere.util.Env;
import org.compiere.util.DB;

/**
 * @author vivdun01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_GainInfo extends X_P_GainInfo
{
	/**
	 * 	Get GainInfo
	 *	@param ctx context
	 * 	@param P_GainInfo_ID id
	 *	@return GainInfo
	 */
	public static P_GainInfo get (Properties ctx, int P_GainInfo_ID)
	{
		Integer key = new Integer (P_GainInfo_ID);
		P_GainInfo GainInfo = (P_GainInfo)s_cache.get(key);
		if (GainInfo != null)
			return GainInfo;
		GainInfo = new P_GainInfo (ctx, P_GainInfo_ID, null);
		s_cache.put (key, GainInfo);
		return GainInfo;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_GainInfo>	s_cache = new CCache<Integer,P_GainInfo>("P_GainInfo", 20);


	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_GainInfo_ID id
	 */
	public P_GainInfo (Properties ctx, int P_GainInfo_ID, String trxName)
	{
		super (ctx, P_GainInfo_ID, trxName);
		if (P_GainInfo_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_GainInfo

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_GainInfo (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_GainInfo (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_GainInfo_ID"), trxName);
	}	//	P_GainInfo
	
	
	public static P_GainInfo get( Properties ctx, String value )
	{
		int id = 0;
		String sql = "Select P_GainInfo_ID from P_GainInfo Where Value = '" + value + "'";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				id = rs.getInt("P_GainInfo_ID");
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("P_GainInfo.get - " + e);
		}
		
		return P_GainInfo.get(ctx, id);
		
	}

	
	protected boolean afterSave (boolean newRecord, boolean success)
	{
//		if (newRecord)
//		{
			String sql = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			sql = "SELECT P_Gain_ID FROM P_Gain Where AD_Client_ID = " + this.getAD_Client_ID()  	
			    + " And not exists ( select 1 From P_Gain_GainInfo Where P_Gain_GainInfo.P_Gain_ID = P_Gain.P_Gain_ID and P_Gain_GainInfo.P_GainInfo_ID = " + this.getP_GainInfo_ID() + " ) ";
			
			try
			{
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();
				
				// *** On ajoute à tous les gains la nouvelle info complémentaire
				while (rs.next())
				{
					P_Gain_GainInfo Gain_GainInfo = new P_Gain_GainInfo(Env.getCtx(), -1, this.get_TrxName());
					
					Gain_GainInfo.setP_Gain_ID			( rs.getInt(1)				);
					Gain_GainInfo.setP_GainInfo_ID		( this.getP_GainInfo_ID()	);
					Gain_GainInfo.setTo_Consider		( false						);
					
					Gain_GainInfo.save();
				}
				pstmt.close();
				rs.close();
			}
			catch (Exception e)
			{
				System.out.println ("P_GainInfo - afterSave - " + sql + " - " + e);
			}
			
//		}
		
		return true;
	}

}
