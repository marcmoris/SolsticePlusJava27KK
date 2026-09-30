/*
 * Created on 2005-07-07
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.custom;

import org.compiere.model.GridField;
import org.compiere.model.GridTab;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.CalloutEngine;
import org.compiere.util.DB;
/**
 * @author vivdun01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CalloutEmployee extends CalloutEngine
{
	int oldValueOccGroup = -1;
	int oldValueJobTitle = -1;
	
	
	/**
	 *  ClearJobTitleList.
	 *	The string in the Callout field is: 
	 *  <code>com.compiere.custom.CalloutEmployee.ClearJobTitleList</code>
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
		
	
/*		if (mTab.getValue("P_Job_Title_ID") == null)
			return "";
		
		mTab.setValue("P_Job_Title_ID", null);
*/		
		return "";
	}
	
	public String SetOccupationGroupList (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		/*
		
		if (value == null)
			return "";
		
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		sql = "SELECT P_Occupation_Group_ID " +
				" FROM P_Job_Title " +
				" WHERE P_Job_Title_ID = " + value;	
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			if (rs.next())
			{
				// si le groupe doit changer
				if (rs.getString("P_Occupation_Group_ID").compareTo(mTab.get_ValueAsString("P_Occupation_Group_ID")) != 0)
					mTab.setValue("P_Occupation_Group_ID", rs.getObject("P_Occupation_Group_ID"));
				else
					return "";
			}
			else
			{
				mTab.setValue("P_Occupation_Group_ID", null);
			}
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, "CalloutEmployee - SetOccupationGroupList - " + sql, e);
		}*/
				
		return "";
	}
	
	
	public String DeductionChange (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		int deductionID = -1;
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		if (mTab.getValue("P_Deduction_ID") == null)
			return "";
		
		deductionID = ((Integer)mTab.getValue("P_Deduction_ID")).intValue();
				
		sql = "SELECT P_Taxable_Benefit_ID " +
				" FROM P_Deduction " + 
				" WHERE P_Deduction_ID = " + deductionID;
		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
		
			// Set the associated taxable benefit 
			if (rs.next())
				mTab.setValue("P_Taxable_Benefit_ID", rs.getObject(1));
			else
				// Ne fonctionne pas toujours et cause parfois une exception... a revoir. 
				mTab.setValue("P_Taxable_Benefit_ID", null);
						
			rs.close();
			pstmt.close();
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, "CalloutEmployee - DeductionChange - " + sql, e);
		}
		
		return "";
	}
}
