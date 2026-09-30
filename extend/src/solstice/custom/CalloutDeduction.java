/*
 * Created on 2005-05-10
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.custom;

import org.compiere.model.GridField;
import org.compiere.model.GridTab;

import java.math.BigDecimal;
import java.util.Properties;

import org.compiere.model.CalloutEngine;


/**
 * @author marmor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CalloutDeduction extends CalloutEngine
{
	/**
	 *  ClearJobTitleList.
	 *	The string in the Callout field is: 
	 *  <code>com.compiere.custom.CalloutDeduction.ClearJobTitleList</code>
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

	public String SetMultiplyRate (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
 //       String Deduction = (String) mTab.getValue("Value");
		if ( mTab == null )
				return "";
		
		if ( mTab.getValue("P_Deduction_ID") == null )
			return "";
		
        int P_Deduction_ID = (int) mTab.getValue("P_Deduction_ID");
        BigDecimal MultiplyRate = (BigDecimal) mTab.getValue("MultiplyRate");
        
        // DI23
		if ( P_Deduction_ID == 1102795 && (MultiplyRate == null || MultiplyRate.compareTo(new BigDecimal(1.0)) == 0) )
			mTab.setValue("MultiplyRate",  new BigDecimal(2.0));

		// DI25
		if ( P_Deduction_ID == 1102797 && MultiplyRate == null )
			mTab.setValue("MultiplyRate",  new BigDecimal(1.0));

		return "";
	}

	
}
