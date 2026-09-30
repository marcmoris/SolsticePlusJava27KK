package solstice.model;

import java.sql.*;
import java.util.*;

import org.compiere.model.MLocation;
import org.compiere.util.*;

import java.util.logging.*;

/**
 *  Bonus Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
 
public class P_Profile_Deduction extends X_P_Profile_Deduction
{
	/**
	 * 	Constructor
	 *	@param ctx context
	 *	@param P_Profile_Deduction_ID
	 */
	public P_Profile_Deduction(Properties ctx, int P_Profile_Deduction_ID, String trxName)
	{
		super (ctx, P_Profile_Deduction_ID, trxName);
	}	//	P_Profile_Deduction

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Profile_Deduction(Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	P_Profile_Deduction

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Profile_Deduction.class);

	/**
	 * 	Before Delete
	 *	@return true if it can be deleted
	 */
	protected boolean beforeDelete ()
	{
		
		String sql = "Delete P_Employee_Deduction Where P_Deduction_ID = " + this.getP_Deduction_ID() 
        + " AND EXISTS( Select 1 FROM P_Employee_Profile "
        + "     WHERE IsActive = 'Y' "
        + "       AND P_Employee_Profile.P_Employee_ID = P_Employee_Deduction.P_Employee_ID " 
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
	    
	    P_Period Period = P_Period.getOpenPeriod(Env.getCtx(), null);


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
			    MLocation Location = MLocation.get( Env.getCtx(), Employee.getC_Location_ID(), this.get_TrxName());
			    P_Employee_Deduction EmpDeduction = P_Employee_Deduction.get( Env.getCtx(), employeeID, getP_Deduction_ID(), Period.getEndDate(), this.get_TrxName() );
			    P_Deduction deduction = new P_Deduction(Env.getCtx(), getP_Deduction_ID(), this.get_TrxName());
			    
			    if (this.getAD_Org_ID() == 0 || this.getAD_Org_ID() == Employee.getAD_Org_ID() )
			    {
				    if (this.getP_Collective_Labour_Agr_ID() == 0 || this.getP_Collective_Labour_Agr_ID() == Employee.getP_Collective_Labour_Agr_ID() )
				    {
			            if ( deduction.getC_Region_ID() == 0 || deduction.getC_Region_ID() == Location.getC_Region_ID() )
			            {
//						    if ( newRecord )
//						    {
						    	EmpDeduction.setAD_Org_ID(0);
					    	if ( EmpDeduction.getP_Deduction_ID() == 0 ) 
						    	EmpDeduction.setP_Deduction_ID( getP_Deduction_ID() );
						    if ( EmpDeduction.getP_Employee_ID() == 0 )    
						    	EmpDeduction.setP_Employee_ID( employeeID );
//						    }
						    
						    EmpDeduction.setIsActive( isActive() );
						    if ( getEffectIn() != null )
			  			      EmpDeduction.setEffectIn( getEffectIn() );

						    if ( getEffectTo() != null )
				  			      EmpDeduction.setEffectTo( getEffectTo() );
						    
						    EmpDeduction.save();
			            }
				    	
				    }
			    	
			    }
			    
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "P_Profile_Deduction, afterSave - ", e);
			return false;
		}
	    
	    
	    log.info("afterSave - New=" + newRecord + ", Success=" + success + " ***");
		return success;
	}	//	afterSave

	
	
}	//	P_Profile_Deduction
