/*
 * Created on 1 sept. 2005
 *
 */
package org.solstice.apps.form.VTimeSheetModel.Lookup;

import org.compiere.model.MRole;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

import solstice.model.P_Credits;

public class LookupCredits extends AbstractKNPModel {

	public LookupCredits() {}
	
    public KeyNamePair lookup( int key ) { 
    	KeyNamePair val = super.lookup(key); 
    	if( val == null ) { // Probablement inactif, on va piocher sur la BD
    		P_Credits obj = P_Credits.get(Env.getCtx(), key, null);
    		if( obj != null ) val = new KeyNamePair( obj.get_ID(), "~" + obj.getValue() + " - " + obj.getName() + "~" );
    	}
    	return val;
	}
    
    public void load( int employeeId ) {
        String sql; 

        if (Env.isBaseLanguage(Env.getCtx(), "AD_Ref_List"))
        	sql = "SELECT c.P_Credits_ID, c.Value + ' - ' + c.Name " +
			"FROM P_Credits c, P_Employee_Credits  " +
			"WHERE c.IsActive = 'Y' AND P_Employee_Credits.IsActive = 'Y' " +
			"AND c.P_Credits_ID = P_Employee_Credits.P_Credits_ID AND P_Employee_Credits.P_Employee_ID = " + employeeId ;
        else
        	sql = "SELECT c.P_Credits_ID, c.Value + ' - ' + trl.Name " +
			"FROM P_Credits c, P_Credits_Trl trl, P_Employee_Credits  " +
			"WHERE c.IsActive = 'Y' AND P_Employee_Credits.IsActive = 'Y' " +
			"AND c.P_Credits_ID = P_Employee_Credits.P_Credits_ID AND P_Employee_Credits.P_Employee_ID = " + employeeId +
            "AND trl.P_Credits_ID = c.P_Credits_ID " +
			"AND trl.AD_Language = '" + Env.getAD_Language(Env.getCtx()) + "'" ;
  
		sql = sql + " AND P_Employee_Credits." + MRole.getDefault().getClientWhere ( false);	// fully qualidfied - RO 

		sql = sql + " Order by c.value ";
        genericLoadFromSQL(sql);

    }
}
    