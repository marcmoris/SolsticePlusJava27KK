/*
 * Created on 1 sept. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.solstice.apps.form.VTimeSheetModel.Lookup;

import java.sql.ResultSet;
import java.sql.Statement;

import org.compiere.model.MRole;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import solstice.model.P_Schedule;

/**
 * @author jeacat01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LookupSchedule extends AbstractKNPModel {

    private int m_defaultId = -1;
    public int getDefaultId() { return m_defaultId; }
    
	public LookupSchedule() {
        String sql = "SELECT P_Schedule_ID, Name FROM P_Schedule WHERE IsActive = 'Y'";
 
		sql = MRole.getDefault().addAccessSQL (sql, "P_Schedule", true, false);	// fully qualidfied - RO 
		
        genericLoadFromSQL(sql);
        
        sql = sql + " AND IsDefault = 'Y'";
        Statement statm = null;
        ResultSet rs = null;
        try {
            statm = DB.createStatement();
            rs = statm.executeQuery( sql );
            if( rs.next() ) {
                m_defaultId = rs.getInt(1);
            }
            rs.close();
			statm.close();
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
	}
	
    public KeyNamePair lookup( int key ) { 
    	KeyNamePair val = super.lookup(key); 
    	if( val == null ) { // Probablement inactif, on va piocher sur la BD
    		P_Schedule obj = P_Schedule.get(Env.getCtx(), key, null);
    		if( obj != null ) val = new KeyNamePair( obj.get_ID(), "~" + obj.getName() + "~" );
    	}
    	return val;
	}
}
