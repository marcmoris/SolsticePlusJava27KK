package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  Bonus Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Benefit_Param extends X_P_Benefit_Param
{
	/**
	 * 	Get Bonus
	 *	@param ctx context
	 * 	@param P_Bonus_ID id
	 *	@return Bonus
	 */
	public static P_Benefit_Param get (Properties ctx, int P_Benefit_ID, String trxName)
	{
		Integer key = new Integer (P_Benefit_ID);
		P_Benefit_Param Bonus = (P_Benefit_Param)s_cache.get(key);
		if (Bonus != null)
			return Bonus;
		Bonus = new P_Benefit_Param (ctx, P_Benefit_ID, trxName);
		s_cache.put (key, Bonus);
		return Bonus;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Benefit_Param>	s_cache = new CCache<Integer,P_Benefit_Param>("P_Bonus", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Bonus_ID id
	 */
	public P_Benefit_Param (Properties ctx, int P_Benefit_ID, String trxName)
	{
		super (ctx, P_Benefit_ID, trxName);
		if (P_Benefit_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Bonus

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Benefit_Param (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Benefit_Param (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Benefit_ID"), trxName);
	}	//	P_Bonus


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
    public void afterCopy(int originalId)
    {
        try
        {
            // On copie la table P_Benefit_Param_Detail,
            this.copyParam(originalId);
        }
        catch (SQLException e)
        {
            log.log(Level.SEVERE, "afterCopy", e);
        }
    }
    
    private void copyParam(int originalId) throws SQLException
    {
        // On sort tous les enregistrements à copier de la table
        String sql
        = "select AD_Org_ID,IsActive,P_Gain_ID "
            + " from P_Benefit_Param_Detail"
            + " where P_Benefit_Param_ID = " + originalId;
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // On crée un nouvel enregistrement pour la déduction copiée
        	P_Benefit_Param_Detail param = new P_Benefit_Param_Detail(Env.getCtx(), -1, null);
        	param.setP_BENEFIT_PARAM_ID(this.getP_BENEFIT_PARAM_ID());
        	param.setAD_Org_ID(rs.getInt("AD_Org_ID"));
            param.setIsActive(rs.getString("IsActive").equals("Y"));
            param.setP_Gain_ID(rs.getInt("P_Gain_ID"));
            param.save();     
        }
        
        rs.close();
        stmt.close();
    }
    
	/*public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Bonus[ID=")
			.append(this.getP_Bonus_ID())
//			.append(",Value=").append(getValue())
			.append(",Name=").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString*/

}	//	P_Bonus
