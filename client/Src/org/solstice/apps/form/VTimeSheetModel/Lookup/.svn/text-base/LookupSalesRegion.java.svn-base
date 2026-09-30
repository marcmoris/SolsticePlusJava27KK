package org.solstice.apps.form.VTimeSheetModel.Lookup;


import org.compiere.model.MSalesRegion;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

public class LookupSalesRegion extends AbstractKNPModel {
	
	public LookupSalesRegion ()
	{
		String sql = "SELECT -1, '' UNION SELECT C_SalesRegion_ID, Value + ' - ' + Name FROM C_SalesRegion WHERE IsActive = 'Y'";
		 
		genericLoadFromSQL(sql);
    	setDefaultValue( null );
		setSelectedItem( null );
	}
	
	public KeyNamePair lookup( int key ) {  
    	KeyNamePair val = super.lookup(key); 
    	if( val == null ) { // Probablement inactif, on va piocher sur la BD
    		MSalesRegion obj = new MSalesRegion(Env.getCtx(), key, null);
    		if( obj != null && obj.getValue() != null ) 
    			val = new KeyNamePair( obj.get_ID(), "~" + obj.getValue() + " - " + obj.getName() + "~" );
    	}
    	return val;
    }

}
