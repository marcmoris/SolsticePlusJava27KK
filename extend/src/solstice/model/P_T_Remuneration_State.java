/*
 * Created on Jul 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;


/**
 * @author rejgar01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_T_Remuneration_State extends X_P_T_Remuneration_State 
{  
	/**
	 * 	Get Gain
	 *	@param ctx context
	 * 	@param P_Gain_ID id
	 *	@return remuneration_State
	 */
	public static P_T_Remuneration_State get (Properties ctx, int P_T_Remuneration_State_ID, String trxName)
	{
		Integer key = new Integer (P_T_Remuneration_State_ID);
		P_T_Remuneration_State remuneration_State = (P_T_Remuneration_State)s_cache.get(key);
		if (remuneration_State != null)
			return remuneration_State;
		remuneration_State = new P_T_Remuneration_State (ctx, P_T_Remuneration_State_ID, trxName);
		s_cache.put (key, remuneration_State);
		return remuneration_State;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_T_Remuneration_State>	s_cache = new CCache<Integer,P_T_Remuneration_State>("P_T_Remuneration_State", 2);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Gain_ID id
	 */
	public P_T_Remuneration_State (Properties ctx, int P_T_Remuneration_State_ID, String trxName)
	{
		super (ctx, P_T_Remuneration_State_ID, trxName);
		if (P_T_Remuneration_State_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Gain

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_T_Remuneration_State (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_T_Remuneration_State (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_T_Remuneration_State_ID"), trxName);
	}	//	P_Gain


    public static void generation( Properties ctx, int P_Period_ID, String trxName )
	{
/*
    	//  	ici....
    	
		String sql = "select * from siq..prftp_ect  where ftpcle like '05%' order by ftpcle";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				P_T_Remuneration_State tab = new P_T_Remuneration_State( ctx, -1, m_trxName);
//				tab.setC_ValidCombination_ID();
               	tab.setIsActive( true);
               	tab.save();
				count++;
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("Migration P_payment_entryline - " + e);
		}
  */  

	}
    
}
