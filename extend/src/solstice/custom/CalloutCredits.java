/*
 * Created on 2005-05-10
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.custom;

import org.compiere.model.GridField;
import org.compiere.model.GridTab;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.CalloutEngine;
import org.compiere.util.DB;

/**
 * @author marmor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CalloutCredits extends CalloutEngine
{
    
	/**
	 *	Family Gain.
	 *	The string in the Callout field is: 
	 *  <code>com.compiere.custom.CalloutGain.familyGain</code> 
	 *
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 *  @param oldValue The old value
	 *	@return error message or "" if OK
	 */
    
	public String CreditsFamilyMthEvalChange (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		log.info("CallOutCredits.CreditsFamilyMthEvalChange");

		if ( value == null )
		    return "";
		
        String sql = "SELECT P_CREDITS_MTH_EVALUATION_ID FROM P_CREDITS_FAMILY_MTH_EVAL WHERE P_CREDITS_FAMILY_MTH_EVAL_ID = " + value.toString();
            //on execute le select
   	
        try
        {
            //on ajoute les paramètres
            PreparedStatement pstmt = DB.prepareStatement(sql, null);

            //on execute
            ResultSet rs = pstmt.executeQuery();
            //on boucle dans les résultats
            if (rs.next())
            {
            	    mTab.setValue("P_CREDITS_MTH_EVALUATION_ID", rs.getString(1) );
            }
            rs.close();
            pstmt.close();
        }
        catch (SQLException e)
        {
           log.log(Level.SEVERE, "CallOutCredits.CreditsFamilyMthEvalChange", e);
        }
		

		return "";
	}	//	
	
	public String CreditsFamilyMthVarChange (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
		{
			log.info("CallOutCredits.CreditsFamilyMthVarChange");

			if ( value == null )
			    return "";

	        String sql = "SELECT P_CREDITS_MTH_VARIATION_ID FROM P_CREDITS_FAMILY_MTH_VAR WHERE P_CREDITS_FAMILY_MTH_VAR_ID = " + value.toString();
	            //on execute le select
	   	
	        try
	        {
	            //on ajoute les paramètres
	            PreparedStatement pstmt = DB.prepareStatement(sql, null);

	            //on execute
	            ResultSet rs = pstmt.executeQuery();
	            //on boucle dans les résultats
	            if (rs.next())
	            {
	            	    mTab.setValue("P_CREDITS_MTH_VARIATION_ID", rs.getString(1) );
	            }
	            rs.close();
	            pstmt.close();
	        }
	        catch (SQLException e)
	        {
	           log.log(Level.SEVERE, "CallOutCredits.CreditsFamilyMthVarChange", e);
	        }
			

			return "";
		}	//	

	/**
	 *  ClearJobTitleList.
	 *	The string in the Callout field is: 
	 *  <code>com.compiere.custom.CalloutCredits.ClearJobTitleList</code>
	 *  
	 *   Delete all items of the JobTitle list
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 * @param oldValue
	 * @return
	 */
	public String ClearJobTitleList (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		mTab.setValue("P_Job_Title_ID", null);
		
		return "";
	}

}
