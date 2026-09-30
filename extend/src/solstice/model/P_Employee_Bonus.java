package solstice.model;

import java.util.Properties;
import java.util.logging.Level;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

import java.sql.ResultSet;

/**
 *  Employee_Bonus Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Employee_Bonus extends X_P_Employee_Bonus
{
	/**
	 * 	Get Employee_Bonus
	 *	@param ctx context
	 * 	@param P_Employee_Bonus_ID id
	 *	@return Employee_Bonus
	 */
	public static P_Employee_Bonus get (Properties ctx, int P_Employee_Bonus_ID, String trxName)
	{
		Integer key = new Integer (P_Employee_Bonus_ID);
		P_Employee_Bonus employee_Bonus = (P_Employee_Bonus)s_cache.get(key);
		if (employee_Bonus != null)
			return employee_Bonus;
		employee_Bonus = new P_Employee_Bonus (ctx, P_Employee_Bonus_ID, trxName);
		s_cache.put (key, employee_Bonus);
		if(employee_Bonus != null)
		{
			employee_Bonus.setTrxName(trxName);
		}
		return employee_Bonus;
	}	//	get

	
	public static P_Employee_Bonus get (Properties ctx, int EmployeeID, int BonusID, Timestamp EffectIn, String trxName)
	{
	    String sql = "SELECT  P_Employee_Bonus_ID FROM P_Employee_Bonus "
            + " WHERE P_Employee_ID = " + EmployeeID
            + "   AND P_Bonus_ID  = " + BonusID
	     	+ "   AND EffectIn<= " + DB.TO_DATE( EffectIn )  
	     	+ "  Order By EffectIn Desc";
	    
	    PreparedStatement pstmt = null;
    
	    int P_Employee_Bonus_ID = -1;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
			    P_Employee_Bonus_ID = rs.getInt(1);
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			
		}
		catch (Exception e)
		{
		    System.out.println( "P_Employee_Deduction, afterSave - " + e);
		    return null;
		}
	    
		P_Employee_Bonus Employee_Bonus = P_Employee_Bonus.get( ctx, P_Employee_Bonus_ID , trxName);
		return Employee_Bonus;
	}	//	get

	/**
	 * 	Get optionally cached Employee_Bonus
	 *	@param ctx context
	 *	@return Employee_Bonus
	 */
//	public static P_Employee_Bonus get (Properties ctx)
//	{
//		return get (ctx, Env.getP_Employee_Bonus_ID(ctx));
//	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Employee_Bonus>	s_cache = new CCache<Integer,P_Employee_Bonus>("P_Employee_Bonus", 20);

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Employee_LongTermLeave.class);

	private String				m_trxName = null;
	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Employee_Bonus_ID id
	 */
	public P_Employee_Bonus (Properties ctx, int P_Employee_Bonus_ID, String trxName)
	{
		super (ctx, P_Employee_Bonus_ID, trxName);
		if (P_Employee_Bonus_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
		m_trxName = trxName; 
	}	//	P_Employee_Bonus

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Employee_Bonus (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
		m_trxName = trxName; 
	}	

	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Employee_Bonus (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Employee_Bonus_ID"), trxName);
		m_trxName = trxName; 
	}	//	P_Employee_Bonus

	/**
	 * 	After Save
	 *	@param newRecord
	 *	@param success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
	    if (!success )
		{
			return success;
		}

	    //on va chercher l'objet precedent pour mettre sa date de fin a jour
	    String sql = "SELECT P_Employee_Bonus_ID "
	    	+ " FROM P_Employee_Bonus "
	    	+ " WHERE P_Employee_ID = " + this.getP_Employee_ID()
	    	+ " AND P_Bonus_ID = " + this.getP_Bonus_ID()
	    	+ " AND EffectIn < " + DB.TO_DATE( this.getEffectIn() )
	    	+ " AND EffectTo IS NULL "
	    	+ " ORDER BY EffectIn Desc ";
	    PreparedStatement pstmt = DB.prepareStatement(sql, null);

	    try
	    {
		    ResultSet rs = pstmt.executeQuery();
		    if(rs.next())
		    {
		    	//si on a un objet, on set sa date de fin a la veille de la date de debut du nouvelle objet
		    	P_Employee_Bonus employee_Bonus = P_Employee_Bonus.get(Env.getCtx(), rs.getInt(1), m_trxName);
		    	if(employee_Bonus == null || employee_Bonus.getP_Employee_Bonus_ID() <= 0)
		    	{
		    		return success;
		    	}
		    	Timestamp tsDate = this.getEffectIn();
		    	// - 1 pour la veille
		    	tsDate = TimeUtil.addDays( tsDate, -1);
		    	employee_Bonus.setEffectTo(tsDate);
		    	employee_Bonus.save();
		    }
	    }
	    catch(SQLException e)
	    {
	    	s_log.log(Level.SEVERE,"P_Assignment_SpecialRate - afterSave - " + sql, e);	    	
	    }
		return success;
	}	//	afterSave

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true or false
	 */

	protected boolean beforeSave (boolean newRecord)
	{
		//+ 2011.07.05 + sécurité par compagnie.
 	    P_Employee Employee = P_Employee.get( Env.getCtx(), this.getP_Employee_ID(), this.get_TrxName());
		this.setAD_Org_ID( Employee.getAD_Org_ID());
		//- 2011.07.05 + sécurité par compagnie.

		return true;
	}	//	beforeSave
	
	private void setTrxName ( String trxName)
	{
		m_trxName = trxName;
	}

}	//	P_Employee_Bonus
