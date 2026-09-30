package solstice.model;

import java.sql.*;
import java.util.*;
import org.compiere.util.*;
import java.util.logging.*;

/**
 *  Bonus Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
 
public class P_Profile_Credits extends X_P_Profile_Credits
{
	/**
	 * 	Constructor
	 *	@param ctx context
	 *	@param P_Profile_Credits_ID
	 */
	public P_Profile_Credits(Properties ctx, int P_Profile_Credits_ID, String trxName)
	{
		super (ctx, P_Profile_Credits_ID, trxName);
	}	//	P_Profile_Credits

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Profile_Credits.class);
	
		
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Profile_Credits(Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	P_Profile_Credits

	
	/**
	 * 	Before Delete
	 *	@return true if it can be deleted
	 */
	protected boolean beforeDelete ()
	{
		String sql = "Delete P_Employee_Credits Where P_Credits_ID = " + this.getP_Credits_ID() 
        + " AND EXISTS( Select 1 FROM P_Employee_Profile "
        + "     WHERE IsActive = 'Y' "
        + "       AND P_Employee_Profile.P_Employee_ID = P_Employee_Bonus.P_Employee_ID " 
        + "       AND P_Profile_ID = " + getP_Profile_ID() + " ) "; 

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
		return success;
	}
	
	/**
	 * 	Before Save
	 *	@param newRecord
	 *	@return
	 */
	protected boolean beforeSave (boolean newRecord)
	{
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
			    P_Employee Employee = P_Employee.get( Env.getCtx(), employeeID, this.get_TrxName() );
			    P_Employee_Credits EmpCredits = P_Employee_Credits.get( Env.getCtx(), employeeID, getP_Credits_ID() , getEffectIn(), this.get_TrxName());

			    if ( EmpCredits == null)
			    	EmpCredits = new P_Employee_Credits( Env.getCtx(), -1, this.get_TrxName());
			    
//			    if ( newRecord )
//			    {
			       EmpCredits.setAD_Org_ID(0);
			       EmpCredits.setP_Credits_ID( this.getP_Credits_ID() );
				   EmpCredits.setP_Employee_ID( employeeID );
				   EmpCredits.setP_Method_Credits_ID( this.getP_Method_Credits_ID() );
//			    }
			    
                EmpCredits.setP_Method_Credits_ID( this.getP_Method_Credits_ID());
			    EmpCredits.setIsActive( this.isActive() );
			    
			    if ( getEffectIn() != null )
			      EmpCredits.setEffectIn( getEffectIn() );
			    EmpCredits.save();
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "P_Profile_Credits, afterSave - ", e);
			return false;
		}
	    
	    
		return success;
	}	//	afterSave

	
	
}	//	P_Profile_Credits
