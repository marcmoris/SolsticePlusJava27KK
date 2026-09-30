package solstice.model;

import java.sql.*;
import java.util.*;
import org.compiere.util.*;
import org.compiere.model.MLocation;

import java.util.logging.*;

/**
 *  Bonus Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
 
public class P_Profile_Taxable_Benefit extends X_P_Profile_Taxable_Benefit
{
	/**
	 * 	Constructor
	 *	@param ctx context
	 *	@param P_Profile_Taxable_Benefit_ID
	 */
	public P_Profile_Taxable_Benefit(Properties ctx, int P_Profile_Taxable_Benefit_ID, String trxName)
	{
		super (ctx, P_Profile_Taxable_Benefit_ID, trxName);
	}	//	P_Profile_Taxable_Benefit

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Profile_Taxable_Benefit.class);

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Profile_Taxable_Benefit(Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	P_Profile_Taxable_Benefit

	
	/**
	 * 	Before Delete
	 *	@return true if it can be deleted
	 */
	protected boolean beforeDelete ()
	{

		String sql = "Delete P_Employee_Taxable_Benefit Where P_Taxable_Benefit_ID = " + this.getP_Taxable_Benefit_ID() 
        + " AND EXISTS( Select 1 FROM P_Employee_Profile "
        + "     WHERE IsActive = 'Y' "
        + "       AND P_Employee_Profile.P_Employee_ID = P_Employee_Bonus.P_Employee_ID " 
        + "       AND P_PROFILE_ID = " + getP_Profile_ID() + " ) "; 

        int no = DB.executeUpdate (sql, null);

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
			    MLocation Location = MLocation.get( Env.getCtx(), Employee.getC_Location_ID(), this.get_TrxName());
			    P_Employee_Taxable_Benefit EmpTaxable_Benefit = P_Employee_Taxable_Benefit.get( Env.getCtx(), employeeID, getP_Taxable_Benefit_ID(), getEffectIn(), this.get_TableName() );

			    P_Taxable_Benefit tb = new P_Taxable_Benefit(Env.getCtx(), getP_Taxable_Benefit_ID(), this.get_TrxName());
	            if ( tb.getC_Region_ID() == 0 || tb.getC_Region_ID() == Location.getC_Region_ID() )
	            {
//				    if ( newRecord )
//				    {
				    	EmpTaxable_Benefit.setAD_Org_ID( 0 );
			    	if ( EmpTaxable_Benefit.getP_Taxable_Benefit_ID() == 0 ) 
				        EmpTaxable_Benefit.setP_Taxable_Benefit_ID( getP_Taxable_Benefit_ID() );

				    if ( EmpTaxable_Benefit.getP_Employee_ID() == 0 )    
				    	EmpTaxable_Benefit.setP_Employee_ID( employeeID );
//				    }
				    
				    if ( this.getTaxB_Fixed_Amount() != null )
				    	EmpTaxable_Benefit.setTaxB_Fixed_Amount( this.getTaxB_Fixed_Amount() );

				    EmpTaxable_Benefit.setIsActive( isActive() );
				    if ( getEffectIn() != null )
	    			    EmpTaxable_Benefit.setEffectIn( getEffectIn() );
				    EmpTaxable_Benefit.save();
	            }

			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "P_Profile_Taxable_Benefit, afterSave - ", e);
		    return false;
		}
		return success;
	}	//	afterSave

	
	
}	//	P_Profile_Taxable_Benefit
