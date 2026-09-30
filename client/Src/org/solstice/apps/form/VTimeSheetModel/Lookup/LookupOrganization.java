package org.solstice.apps.form.VTimeSheetModel.Lookup;


import org.compiere.model.MOrg;
import org.compiere.model.MRole;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

	public class LookupOrganization extends AbstractKNPModel {
		
		public LookupOrganization ()
		{
			String sql = "SELECT -1, '' UNION SELECT ad_org_id, Value + ' - ' + Name  from ad_org where IsActive = 'Y' and AD_Org_ID <> 0 ";
			 	sql = sql +"AND ad_org." + MRole.getDefault().getClientWhere ( false);
			genericLoadFromSQL(sql);
	    	setDefaultValue( null );
			setSelectedItem( null );
		}
		
		public KeyNamePair lookup( int key ) {  
	    	KeyNamePair val = super.lookup(key); 
	    	if( val == null ) { // Probablement inactif, on va piocher sur la BD
	    		MOrg obj = new MOrg(Env.getCtx(), key, null);
	    		if( obj != null && obj.getValue() != null ) 
	    			val = new KeyNamePair( obj.get_ID(), "~" + obj.getValue() + " - " + obj.getName() + "~" );
	    	}
	    	return val;
	    }

	}


	