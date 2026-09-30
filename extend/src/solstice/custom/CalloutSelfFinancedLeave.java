/*
 * Created on 2005-06-07
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.custom;

import org.compiere.model.GridField;
import org.compiere.model.GridTab;

import java.awt.Frame;
import java.math.BigDecimal;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.compiere.model.CalloutEngine;
import org.compiere.util.Env;
import org.compiere.util.Msg;


/**
 * @author vivdun01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CalloutSelfFinancedLeave extends CalloutEngine
{
    private static boolean SalaryPercentageChangeActive = false;

	public String SalaryPercentageChange (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
	    BigDecimal SalaryPercentage = Env.ZERO;
	    BigDecimal ReductionPercentage = Env.ZERO;
	    
	    if (SalaryPercentageChangeActive == true)
	    {
	        SalaryPercentageChangeActive = false;
	        return "";
	    }
	        
	    if (value == null)
	        return "";
	    
	    setCalloutActive(true);
	    
	    try
	    {
	        SalaryPercentage = (BigDecimal)value;
	        if (SalaryPercentage.compareTo(new BigDecimal(100)) > 0)
	        {
	        	JOptionPane optionPane1 = new JOptionPane(Msg.getMsg(Env.getCtx(), "ValueBetween0And100"),
	                    JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
	            optionPane1.createDialog(new Frame(), null).show( );
	            
	            SalaryPercentage = new BigDecimal(100);
	            SalaryPercentageChangeActive = true;
	            mTab.setValue("SalaryPercentage", SalaryPercentage);
	        }
	        
	        if (SalaryPercentage.compareTo(new BigDecimal(0)) < 0)
	        {
	            JOptionPane optionPane1 = new JOptionPane(Msg.getMsg(Env.getCtx(), "ValueBetween0And100"),
	                    JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
	            optionPane1.createDialog(new Frame(), null).show();
	            
	            SalaryPercentage = Env.ZERO;
	            SalaryPercentageChangeActive = true;
	            mTab.setValue("SalaryPercentage", SalaryPercentage);
	        }
	        
	        ReductionPercentage = (new BigDecimal(100)).subtract(SalaryPercentage);
	    }
	    catch (Exception e)
	    {
           e.printStackTrace(System.out);
	    }
	    
	    mTab.setValue("ReductionPercentage", ReductionPercentage);
	    
	    setCalloutActive(false);
	    	    
	    return "";
	}	//
	
}