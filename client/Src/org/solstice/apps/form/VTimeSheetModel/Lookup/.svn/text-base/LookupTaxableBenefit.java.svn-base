/*
 * Created on 1 sept. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.solstice.apps.form.VTimeSheetModel.Lookup;

import org.compiere.model.MRole;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

import solstice.model.P_Taxable_Benefit;

/**
 * @author jeacat01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LookupTaxableBenefit extends AbstractKNPModel {

	public LookupTaxableBenefit() {}
	
    public KeyNamePair lookup( int key ) { 
    	KeyNamePair val = super.lookup(key); 
    	if( val == null ) { // Probablement inactif, on va piocher sur la BD
    		P_Taxable_Benefit obj = P_Taxable_Benefit.get(Env.getCtx(), key, null);
    		if( obj != null ) val = new KeyNamePair( obj.get_ID(), "~" + obj.getValue() + " - " + obj.getName() + "~" );
    	}
    	return val;
	}
    
    public void load( int employeeId ) {
        String sql; 
        
        if (Env.isBaseLanguage(Env.getCtx(), "AD_Ref_List"))
           sql = 
        	"SELECT tb.P_Taxable_Benefit_ID, tb.Value + ' - ' + tb.Name " +
        	"FROM P_Taxable_Benefit tb, P_Employee_Taxable_Benefit " +
        	"WHERE tb.IsActive = 'Y' AND P_Employee_Taxable_Benefit.IsActive = 'Y' " +
        	"AND tb.P_Taxable_Benefit_ID = P_Employee_Taxable_Benefit.P_Taxable_Benefit_ID AND P_Employee_Taxable_Benefit.P_Employee_ID = " + employeeId ;
        else
            sql = 
            	"SELECT tb.P_Taxable_Benefit_ID, tb.Value + ' - ' + trl.Name " +
            	"FROM P_Taxable_Benefit tb, P_Taxable_Benefit_Trl Trl, P_Employee_Taxable_Benefit " +
            	"WHERE tb.IsActive = 'Y' AND P_Employee_Taxable_Benefit.IsActive = 'Y' " +
            	" AND tb.P_Taxable_Benefit_ID = P_Employee_Taxable_Benefit.P_Taxable_Benefit_ID " +
            	" AND P_Employee_Taxable_Benefit.P_Employee_ID = " + employeeId +
            	" AND trl.P_Taxable_Benefit_ID = tb.P_Taxable_Benefit_ID " +
    		    " AND trl.AD_Language = '" + Env.getAD_Language(Env.getCtx()) + "'" 
    		    ;

		sql = sql + " AND P_Employee_Taxable_Benefit." + MRole.getDefault().getClientWhere ( false);	// fully qualidfied - RO 
        	
		sql = sql + " Order by tb.Value";
        genericLoadFromSQL(sql);

    }
	
}
