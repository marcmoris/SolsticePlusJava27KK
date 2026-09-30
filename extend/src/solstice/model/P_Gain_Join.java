/*
 * Created on 2005-08-24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

/**
 * @author vivdun01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_Gain_Join extends X_P_Gain_Join
{
	/**
	 * 	Get Gain_Join
	 *	@param ctx context
	 * 	@param P_Gain_Join_ID id
	 *	@return Gain_Join
	 */
	public static P_Gain_Join get (Properties ctx, int P_Gain_Join_ID, String trxName)
	{
		Integer key = new Integer (P_Gain_Join_ID);
		P_Gain_Join Gain_Join = (P_Gain_Join)s_cache.get(key);
		if (Gain_Join != null)
			return Gain_Join;
		Gain_Join = new P_Gain_Join (ctx, P_Gain_Join_ID, trxName);
		s_cache.put (key, Gain_Join);
		return Gain_Join;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Gain_Join>	s_cache = new CCache<Integer,P_Gain_Join>("P_Gain_Join", 20);

	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true or false
	 */

	protected boolean beforeSave (boolean newRecord)
	{
		int p_Gain_id = this.getP_Gain_ID();
		if (p_Gain_id != -1 && this.isNEGATIVE())
		{   
			String whereClause = (newRecord)? "" : " P_Gain_Join_ID <> " + this.getP_Gain_Join_ID() + " and ";
		    String sql = "select P_Gain_ID_AFFECTED from P_Gain_Join where " + whereClause + " P_Gain_ID = " + p_Gain_id + " and ISNEGATIVE = 'Y'";
				
		    try
		    {   	        
		        PreparedStatement stmt = DB.prepareStatement(sql, null);
		        ResultSet rs = stmt.executeQuery();		        
		        if(rs != null && rs.next())
		        {
					log.saveError("ValidationError", "Il y a déjà une coupure négative pour ce code de gain.");
					return false;		        	
		        }
		        
		        rs.close();
		        stmt.close();
		        return true;
		    }
		    catch (SQLException e)
		    {
		        //s_log.log(Level.SEVERE, "P_Employee.GetBooklet()", e);
		        return false;
		    }
		}
        return true;
	}
	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Gain_Join_ID id
	 */
	public P_Gain_Join (Properties ctx, int P_Gain_Join_ID, String trxName)
	{
		super (ctx, P_Gain_Join_ID, trxName);
		if (P_Gain_Join_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Gain_Join

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Gain_Join (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Gain_Join (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Gain_Join_ID"), trxName);
	}	//	P_Gain_Join
	
}	// P_Gain_Join
