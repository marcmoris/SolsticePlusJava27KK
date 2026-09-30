/*
 * Created on 1 sept. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.solstice.apps.form.VTimeSheetModel.Lookup;


import solstice.model.P_Department;
import org.compiere.model.MRole;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

/**
 * @author jeacat01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LookupDept extends AbstractKNPModel {

	public LookupDept() {
		String sql = "SELECT -1, '' UNION SELECT P_Department_ID, Value + ' - ' + Name FROM P_Department WHERE IsActive = 'Y'";
		sql = sql + " AND P_Department." + MRole.getDefault().getClientWhere ( false);	// fully qualidfied - RO 

		genericLoadFromSQL(sql);
    	setDefaultValue( null );
		setSelectedItem( null );
	}
	
    public KeyNamePair lookup( int key ) {  
    	KeyNamePair val = super.lookup(key); 
    	if( val == null ) { // Probablement inactif, on va piocher sur la BD
    		P_Department obj = new P_Department(Env.getCtx(), key, null);
    		if( obj != null && obj.getValue() != null ) 
    			val = new KeyNamePair( obj.get_ID(), "~" + obj.getValue() + " - " + obj.getName() + "~" );
    	}
    	return val;
    }
}

