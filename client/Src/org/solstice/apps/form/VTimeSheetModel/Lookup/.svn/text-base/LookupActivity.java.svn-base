/*
 * Created on 1 sept. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.solstice.apps.form.VTimeSheetModel.Lookup;

import org.compiere.model.MActivity;
import org.compiere.model.MRole;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

/**
 * @author jeacat01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LookupActivity extends AbstractKNPModel {

	public LookupActivity() {
		String sql = "SELECT -1, '' UNION SELECT C_Activity_ID, Value + ' - ' + Name FROM C_Activity WHERE IsActive = 'Y'";
		sql = sql + " AND C_Activity." + MRole.getDefault().getClientWhere ( false);	// fully qualidfied - RO 

		genericLoadFromSQL(sql);
    	setDefaultValue( null );
		setSelectedItem( null );
	}
	
    public KeyNamePair lookup( int key ) {  
    	KeyNamePair val = super.lookup(key); 
    	if( val == null ) { // Probablement inactif, on va piocher sur la BD
    		MActivity obj = new MActivity(Env.getCtx(), key, null);
    		if( obj != null && obj.getValue() != null ) 
    			val = new KeyNamePair( obj.get_ID(), "~" + obj.getValue() + " - " + obj.getName() + "~" );
    	}
    	return val;
    }
}

