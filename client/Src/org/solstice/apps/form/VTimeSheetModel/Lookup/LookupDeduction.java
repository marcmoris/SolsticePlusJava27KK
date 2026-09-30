/*
 * Created on 1 sept. 2005
 *
 */
package org.solstice.apps.form.VTimeSheetModel.Lookup;

import java.sql.Timestamp;

import org.compiere.model.MRole;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

import solstice.model.P_Deduction;

public class LookupDeduction extends AbstractKNPModel {

	public LookupDeduction() {}
	
    public KeyNamePair lookup( int key ) { 
    	KeyNamePair val = super.lookup(key); 
    	if( val == null ) { // Probablement inactif, on va piocher sur la BD
    		P_Deduction obj = P_Deduction.get(Env.getCtx(), key, null);
    		
    		if( obj != null ) val = new KeyNamePair( obj.get_ID(), "~" + obj.getValue() + " - " + obj.getTrlName() + "~" );
    	}
    	return val;
	}
    
    public void load( int employeeId, Timestamp EffectIn ) {
        String sql;

        if (Env.isBaseLanguage(Env.getCtx(), "AD_Ref_List"))
          sql = 
         	"SELECT d.P_Deduction_ID, Value + ' - ' + Name " +
			"FROM P_Deduction d, P_Employee_Deduction " +
			"WHERE d.IsActive = 'Y' AND P_Employee_Deduction.IsActive = 'Y' " +
			"AND d.P_Deduction_ID = P_Employee_Deduction.P_Deduction_ID AND P_Employee_Deduction.P_Employee_ID = " + employeeId +
			" AND P_Employee_Deduction.EffectIn in ( Select Max( EffectIn) FROM P_Employee_Deduction dat WHERE dat.P_Deduction_ID = d.P_Deduction_ID and dat.P_Employee_ID = P_Employee_Deduction.P_Employee_ID and dat.EffectIn <= " + DB.TO_DATE( EffectIn ) + ")"
			;
        else
            sql = 
             	"SELECT d.P_Deduction_ID, d.Value + ' - ' + Trl.Name " +
    			"FROM P_Deduction d, P_Deduction_Trl trl, P_Employee_Deduction " +
    			"WHERE d.IsActive = 'Y' AND P_Employee_Deduction.IsActive = 'Y' " +
    			"AND d.P_Deduction_ID = P_Employee_Deduction.P_Deduction_ID AND P_Employee_Deduction.P_Employee_ID = " + employeeId +
    			" AND P_Employee_Deduction.EffectIn in ( Select Max( EffectIn) FROM P_Employee_Deduction dat WHERE dat.P_Deduction_ID = d.P_Deduction_ID and dat.P_Employee_ID = P_Employee_Deduction.P_Employee_ID and dat.EffectIn <= " + DB.TO_DATE( EffectIn ) + ")" +
    			" AND trl.P_Deduction_ID = d.P_Deduction_ID " +
    			" AND trl.AD_Language = '" + Env.getAD_Language(Env.getCtx()) + "'"  ;
   
		sql = sql + " AND P_Employee_Deduction." + MRole.getDefault().getClientWhere ( false);	// fully qualidfied - RO 

		sql = sql + " order by d.value";
        genericLoadFromSQL(sql);

    }
}