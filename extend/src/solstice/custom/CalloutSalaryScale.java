/*
 * Created on 2005-07-21
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

import org.compiere.model.CalloutEngine;

import org.compiere.util.DB;

/**
 * @author vivdun01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CalloutSalaryScale extends CalloutEngine
{

	/**
	 *  ClearJobTitleList.
	 *	The string in the Callout field is: 
	 *  <code>com.compiere.custom.CalloutSalaryScale.ClearJobTitleList</code>
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

	// Correspondance RETRO. ( P_Augmentation_Correspondence )
	public String ClearSalaryScale (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
        if (value == null)
    		return "";

        if ( mTab.getValue("P_Salary_CLass_ID") == null )
    		return "";
        if ( mTab.getValue("P_Job_Title_ID") == null )
    		return "";

        String sql = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        sql = " SELECT 1 " + " FROM P_Salary_Scale "
                + " WHERE IsActive = 'Y' " 
                + "   AND P_Salary_Class_ID = " + mTab.getValue("P_Salary_CLass_ID")
                + "   AND P_Job_Title_ID = " + mTab.getValue("P_Job_Title_ID")
                ;

        try
        {
            pstmt = DB.prepareStatement(sql, null);
            rs = pstmt.executeQuery();
            if (! rs.next())
            {
        		mTab.setValue("P_Salary_Scale_ID", null);
            }

            rs.close();
            pstmt.close();
            pstmt = null;
        }
        catch (Exception e)
        {
        	System.out.println("CalloutSalaryScale - ClearSalaryScaleDetail" + sql + " - " + e);
        }
		return "";
	}
	
	// Correspondance RETRO. ( P_Augmentation_Correspondence )
	public String ClearSalaryScaleNew (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		
        if (value == null)
    		return "";
        if ( mTab.getValue("P_Salary_CLass_New_ID") == null )
    		return "";
        if ( mTab.getValue("P_Job_Title_New_ID") == null )
    		return "";

        String sql = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        sql = " SELECT 1 " + " FROM P_Salary_Scale "
                + " WHERE IsActive = 'Y' " 
                + "   AND P_Salary_Class_ID = " + mTab.getValue("P_Salary_CLass_New_ID")
                + "   AND P_Job_Title_ID = " + mTab.getValue("P_Job_Title_New_ID")
                ;

        try
        {
            pstmt = DB.prepareStatement(sql, null);
            rs = pstmt.executeQuery();
            if (! rs.next())
            {
        		mTab.setValue("P_Salary_Scale_New_ID", null);
            }

            rs.close();
            pstmt.close();
            pstmt = null;
        }
        catch (Exception e)
        {
        	System.out.println("CalloutSalaryScale - ClearSalaryScaleDetail" + sql + " - " + e);
        }
		return "";
	}

	// Correspondance RETRO. ( P_Augmentation_Correspondence )
	public String ClearSalaryScaleDetail (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{

        if (value == null)
    		return "";
        if ( mTab.getValue("P_Salary_Scale_ID") == null )
    		return "";
		
		String sql = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        sql = " SELECT 1 " + " FROM P_Salary_Scale_Detail "
                + " WHERE IsActive = 'Y' " 
                + "   AND P_Salary_Scale_ID = " + mTab.getValue("P_Salary_Scale_ID")
                ;

        try
        {
            pstmt = DB.prepareStatement(sql, null);
            rs = pstmt.executeQuery();
            if (! rs.next())
            {
        		mTab.setValue("P_Salary_Scale_Detail_ID", null);
            }

            rs.close();
            pstmt.close();
            pstmt = null;
        }
        catch (Exception e)
        {
        	System.out.println("CalloutSalaryScale - ClearSalaryScaleDetail" + sql + " - " + e);
        }
		return "";
	}

	// Correspondance RETRO. ( P_Augmentation_Correspondence )
	public String ClearSalaryScaleDetailNew (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{

        if (value == null)
    		return "";
		if ( mTab.getValue("P_Salary_Scale_New_ID") == null )
    		return "";

		String sql = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        sql = " SELECT 1 " + " FROM P_Salary_Scale_Detail_ID "
                + " WHERE IsActive = 'Y' " 
                + "   AND P_Salary_Scale_ID = " + mTab.getValue("P_Salary_Scale_New_ID")
                ;

        try
        {
            pstmt = DB.prepareStatement(sql, null);
            rs = pstmt.executeQuery();
            if (! rs.next())
            {
        		mTab.setValue("P_Salary_Scale_Detail_New_ID", null);
            }

            rs.close();
            pstmt.close();
            pstmt = null;
        }
        catch (Exception e)
        {
        	System.out.println("CalloutSalaryScale - ClearSalaryScaleDetail" + sql + " - " + e);
        }
		return "";
	}

}
