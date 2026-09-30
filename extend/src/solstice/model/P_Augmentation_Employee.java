/*
 * Created on 2005-09-29
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.DB;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_Augmentation_Employee extends X_P_Augmentation_Employee
{

	private static CLogger		s_log = CLogger.getCLogger (P_Augmentation_Employee.class);

    /**
     * @param ctx
     * @param P_Augmentation_Employee_ID
     * @param trxName
     */
    public P_Augmentation_Employee(Properties ctx,
            int P_Augmentation_Employee_ID, String trxName)
    {
        super(ctx, P_Augmentation_Employee_ID, trxName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param ctx
     * @param rs
     * @param trxName
     */
    public P_Augmentation_Employee(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
        // TODO Auto-generated constructor stub
    }

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true or false
	 */

	protected boolean beforeSave (boolean newRecord)
	{
		//+ 2011.07.05 + sécurité par compagnie.
 	    P_Employee Employee = P_Employee.get( getCtx(), this.getP_Employee_ID(), this.get_TrxName());
		this.setAD_Org_ID( Employee.getAD_Org_ID());
		//- 2011.07.05 + sécurité par compagnie.

		return true;
	}	//	beforeSave

	

	public static P_Augmentation_Employee get (  Properties ctx, int P_Employee_ID, int P_Augmentation_ID, String trxName )
	{
		P_Augmentation_Employee Augmentation_Employee = null;
	    String sql = "Select P_Augmentation_Employee_ID From P_Augmentation_Employee "
	    	       + " Where P_Employee_ID = " + P_Employee_ID 
	               + " and P_Augmentation_ID = " + P_Augmentation_ID
	    		   ;

	    int Augmentation_Employee_ID = 0;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				Augmentation_Employee_ID = rs.getInt("P_Augmentation_Employee_ID"); 
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Form_Employee, - " + sql, e);
		}

		if ( Augmentation_Employee_ID != 0)
			Augmentation_Employee = new P_Augmentation_Employee( ctx, Augmentation_Employee_ID, trxName);
		else
			return null; 

		return Augmentation_Employee;
		
	}	//		
}
