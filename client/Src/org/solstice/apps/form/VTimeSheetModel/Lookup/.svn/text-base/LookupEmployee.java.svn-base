/*
 * Created on 3 oct. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.solstice.apps.form.VTimeSheetModel.Lookup;

import org.compiere.model.MRole;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

import solstice.model.P_Employee;

/**
 * @author jeacat01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LookupEmployee extends AbstractKNPModel {
	public LookupEmployee() {
        String sql = "SELECT P_Employee_ID, Value + ' - ' + Name FROM P_Employee WHERE IsActive = 'Y'";

        if ( Env.getContextAsInt(Env.getCtx(), "#P_Payment_Group_ID") > 0 )
        	sql += " AND P_Payment_Group_ID = " + Env.getContext(Env.getCtx(), "#P_Payment_Group_ID"); 

		sql = sql + " AND P_Employee." + MRole.getDefault().getClientWhere ( false);	// fully qualidfied - RO 

        genericLoadFromSQL(sql);
	}
	
    public KeyNamePair lookup( int key ) { 
    	KeyNamePair val = super.lookup(key); 
    	if( val == null ) { // Probablement inactif, on va piocher sur la BD
    		P_Employee obj = P_Employee.get(Env.getCtx(), key, null);
    		if( obj != null ) val = new KeyNamePair( obj.get_ID(), "~" + obj.getName() + "~" );
    	}
    	return val;
	}
}
