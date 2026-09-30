/******************************************************************************
 * Product: Solstice+ Payroll & Human Resources Management                    *
 * Copyright (C) 2008 ProGestion Informatique, Inc. All Rights Reserved.      *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ProGestion Informatique, 210-5300 Bld des Galerie, Quebec, G2K 2A2 Canada  *
 * or via info@progestion.net or http://www.progestion.net/license.html       *
 ******************************************************************************/
package solstice.custom;

import java.math.BigDecimal;
import java.util.Properties;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;

import solstice.model.P_Employee;
import solstice.model.P_Gain;
import solstice.utils.PgiUtil;

public class CalloutRegularTime extends CalloutEngine
{

	/**
	 *  set isUseExternalTimeSystem.
	 *	The string in the Callout field is: 
	 *  <code>solstice.custom.CalloutRegularTime.setIsUseExternalTimeSystem</code>
	 *  
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 * @param oldValue
	 * @return
	 */
/*
	public String setIsUseExternalTimeSystem (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);

        if ( mTab.getValue("P_Employee_ID") != null )
        	mTab.setValue("isUseExternalTimeSystem", P_Employee.get(ctx, (Integer)mTab.getValue("P_Employee_ID")  , null).isUseExternalTimeSystem());
		
		setCalloutActive(false);

		return "";


	}
*/
	public String setP_Method_Gain_ID (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);

        if ( mTab.getValue("P_Gain_ID") != null )
        {
    		P_Gain Gain = P_Gain.get( ctx, Integer.parseInt(mTab.getValue("P_Gain_ID").toString()), null	);
        	mTab.setValue("P_Method_Gain_ID", Gain.getP_Method_Gain_ID());
        	mTab.setValue("GainUnit", Gain.getGainUnit() );
        }
        	
		
		setCalloutActive(false);

		return "";


	}
	
	private BigDecimal getSunday(  Properties ctx, GridTab mTab, Boolean virtual)
    {
   		return PgiUtil.getPandora_QuantityVitual( Integer.parseInt(mTab.getValue("Pandora_CategoriesAtlasMaitre_ID").toString()), "Sunday" );
    };
    
    private BigDecimal getMonday( Properties ctx, GridTab mTab, Boolean virtual)
    {
   		return PgiUtil.getPandora_QuantityVitual( Integer.parseInt(mTab.getValue("Pandora_CategoriesAtlasMaitre_ID").toString()),  "Monday");
    };
     
    private BigDecimal getTuesday( Properties ctx, GridTab mTab,Boolean virtual)
    {
   		return PgiUtil.getPandora_QuantityVitual( Integer.parseInt(mTab.getValue("Pandora_CategoriesAtlasMaitre_ID").toString()),  "Tuesday");
    };
     
    private BigDecimal getWednesday( Properties ctx, GridTab mTab,Boolean virtual)
    {
   		return PgiUtil.getPandora_QuantityVitual( Integer.parseInt(mTab.getValue("Pandora_CategoriesAtlasMaitre_ID").toString()),  "Wednesday");
    };
     
    private BigDecimal getThursday( Properties ctx, GridTab mTab,Boolean virtual)
    {
   		return PgiUtil.getPandora_QuantityVitual( Integer.parseInt(mTab.getValue("Pandora_CategoriesAtlasMaitre_ID").toString()),  "Thursday");
    };
     
    private BigDecimal getFriday( Properties ctx, GridTab mTab,Boolean virtual)
    {
   		return PgiUtil.getPandora_QuantityVitual( Integer.parseInt(mTab.getValue("Pandora_CategoriesAtlasMaitre_ID").toString()),  "Friday");
    };
     
    private BigDecimal getSaturday( Properties ctx, GridTab mTab,Boolean virtual)
    {
   		return PgiUtil.getPandora_QuantityVitual( Integer.parseInt(mTab.getValue("Pandora_CategoriesAtlasMaitre_ID").toString()),  "Saturday");
    };
    
	
	public String setTimeFromCategories( Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "" ;
		
		setCalloutActive(true);


    	if ( mTab.getValue("Pandora_CategoriesAtlasMaitre_ID") != null  )
		{
    	    
    	    if ( mTab.getValue("Monday") == null ||  ((BigDecimal)mTab.getValue("Monday")).intValue() == 0 )
    	    {
    	    	mTab.setValue("Monday", this.getMonday(ctx, mTab, true));
    	    	mTab.setValue("Monday_V", this.getMonday(ctx, mTab, true));
    	    	
    	    }
    	    
    	    if ( mTab.getValue("Thursday") == null || ((BigDecimal)mTab.getValue("Thursday")).intValue() == 0 )
    	    {
    	    	mTab.setValue("Thursday", this.getThursday(ctx, mTab, true));
    	    	mTab.setValue("Thursday_V", this.getThursday(ctx, mTab, true));
    	    	
    	    }
    	    
    	    if ( mTab.getValue("Wednesday") == null ||  ((BigDecimal)mTab.getValue("Wednesday")).intValue() == 0 )
    	    {
    	    	mTab.setValue("Wednesday", this.getWednesday(ctx, mTab, true));
    	    	mTab.setValue("Wednesday_V", this.getWednesday(ctx, mTab, true));
    	    	
    	    }
    	    
    	    if ( mTab.getValue("Tuesday") == null ||  ((BigDecimal)mTab.getValue("Tuesday")).intValue() == 0 )
    	    {
    	    	mTab.setValue("Tuesday", this.getTuesday(ctx, mTab, true));
    	    	mTab.setValue("Tuesday_V", this.getTuesday(ctx, mTab, true));
    	    	
    	    }
    	    
    	    if ( mTab.getValue("Friday") == null || ((BigDecimal)mTab.getValue("Friday")).intValue() == 0 )
    	    {
    	    	mTab.setValue("Friday", this.getFriday(ctx, mTab, true));
    	    	mTab.setValue("Friday_V", this.getFriday(ctx, mTab, true));
    	    	
    	    }

    	    if ( mTab.getValue("Saturday") == null || ((BigDecimal)mTab.getValue("Saturday")).intValue() == 0 )
    	    {
    	    	mTab.setValue("Saturday", this.getSaturday(ctx, mTab, true));
    	    	mTab.setValue("Saturday_V", this.getSaturday(ctx, mTab, true));
    	    	
    	    }

    	    if ( mTab.getValue("Sunday") == null || ((BigDecimal)mTab.getValue("Sunday")).intValue() == 0 )
    	    {
    	    	mTab.setValue("Sunday", this.getSunday(ctx, mTab, true));
    	    	mTab.setValue("Sunday_V", this.getSunday(ctx, mTab, true));
    	    	
    	    }

		}
		
		setCalloutActive(false);
		return "" ;
		
	}
}
