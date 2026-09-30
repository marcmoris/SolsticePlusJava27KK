package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *  Profile_Bonus Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Profile_Bonus extends X_P_Profile_Bonus
{

	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Profile_Bonus_ID id
	 */
	public P_Profile_Bonus (Properties ctx, int P_Profile_Bonus_ID, String trxName)
	{
		super (ctx, P_Profile_Bonus_ID, trxName);
		if (P_Profile_Bonus_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Profile_Bonus

	
	/**
	 * 	Load Cosntructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Profile_Bonus (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	P_Profile_Bonus

	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Profile_Bonus (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Profile_Bonus_ID"), trxName);
	}	//	P_Profile_Bonus


	
    /**
	 * 	Get Profile_Bonus
	 *	@param ctx context
	 * 	@param P_Profile_Bonus_ID id
	 *	@return Profile_Bonus
	 */
	public static P_Profile_Bonus get (Properties ctx, int P_Profile_Bonus_ID, String trxName)
	{
		Integer key = new Integer (P_Profile_Bonus_ID);
		P_Profile_Bonus Profile_Bonus = (P_Profile_Bonus)s_cache.get(key);
		if (Profile_Bonus != null)
			return Profile_Bonus;
		Profile_Bonus = new P_Profile_Bonus (ctx, P_Profile_Bonus_ID, trxName);
		s_cache.put (key, Profile_Bonus);
		return Profile_Bonus;
	}	//	get


	/**	Cache						*/
	
	private static CCache<Integer,P_Profile_Bonus>	s_cache = new CCache<Integer,P_Profile_Bonus>("P_Profile_Bonus", 20);

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Profile_Bonus.class);

	

	/**
	 * 	Before Delete
	 *	@return true if it can be deleted
	 */
	protected boolean beforeDelete ()
	{
		log.info("beforeDelete ***");

		String sql = "Delete P_Employee_Bonus Where P_Bonus_ID = " + this.getP_Bonus_ID() 
		           + " AND EXISTS( Select 1 FROM P_Employee_Profile "
                   + "     WHERE IsActive = 'Y' "
                   + "       AND P_Employee_Profile.P_Employee_ID = P_Employee_Bonus.P_Employee_ID " 
                   + "       AND P_PROFILE_ID = " + getP_Profile_ID() + " ) "; 

		int no = DB.executeUpdate (sql.toString (), null);

		return true;
	}
	
	/**
	 * 	After Delete
	 *	@param success
	 *	@return
	 */
	protected boolean afterDelete (boolean success)
	{
		log.info("afterDelete *** Success=" + success);
		return success;
	}
	
	/**
	 * 	Before Save
	 *	@param newRecord
	 *	@return
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		log.info("beforeSave - New=" + newRecord + " ***");
		return true;
	}
	
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

	    String sql = "SELECT  P_Employee_ID FROM P_Employee "
            + " WHERE IsActive = 'Y'" 
            + "   AND EXISTS( Select 1 FROM P_Employee_Profile "
            + "                 WHERE IsActive = 'Y' "
            + "                   AND P_Employee_Profile.P_Employee_ID = P_Employee.P_Employee_ID " 
            + "                   AND P_PROFILE_ID = " + getP_Profile_ID() + " ) "; 
	    
	    PreparedStatement pstmt = null;
   
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
			    int employeeID = rs.getInt(1);
			    P_Employee Employee = P_Employee.get( Env.getCtx(), employeeID , this.get_TrxName());
			    P_Employee_Bonus EmpBonus = P_Employee_Bonus.get( Env.getCtx(), employeeID, getP_Bonus_ID(), getEffectIn(), this.get_TrxName() );
			    if ( newRecord )
			    {
			       EmpBonus.setAD_Org_ID(0);
			       EmpBonus.setP_Bonus_ID( getP_Bonus_ID() );
				   EmpBonus.setP_Employee_ID( employeeID );
			    }
			    
			    EmpBonus.setIsActive( this.isActive() );
			    if ( getEffectIn() != null )
			      EmpBonus.setEffectIn( getEffectIn() );
			    EmpBonus.save();
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Profile_Bonus, afterSave - ", e);
			return false;
		}
	    
	    
		return success;
	}	//	afterSave



}	//	P_Profile_Bonus
