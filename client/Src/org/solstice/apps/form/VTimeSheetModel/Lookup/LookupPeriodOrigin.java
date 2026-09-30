/*
 * Created on 1 sept. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.solstice.apps.form.VTimeSheetModel.Lookup;

import org.compiere.model.MRole;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import solstice.model.P_Period;
 
/**
 * @author jeacat01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LookupPeriodOrigin extends AbstractKNPModelPeriod {

    public KeyNamePair lookupPeriodOrigin( int key ) { 
    	KeyNamePair val = super.lookup(key);
    	if( val == null ) { // Probablement inactif, on va piocher sur la BD
    		P_Period obj = P_Period.get(Env.getCtx(), key, null);
    		if( obj != null ) val = new KeyNamePair( obj.get_ID(), "~" + obj.getName() + "~" );
    	}
    	return val;
	}

    public void load( P_Period period ) {
    	
       	if ( period == null || period.getP_Calendar_ID() == 0 )
    		period = P_Period.getOpenPeriod(Env.getCtx(), null);

        String sql = "SELECT P_Period.P_Period_ID, P_Frequency.PayPeriodType + '-' + P_Period.Name FROM P_Period " +
                     " inner join P_YEAR on P_YEAR.P_YEAR_ID = P_PERIOD.P_YEAR_ID " +
                     " inner join P_Frequency on P_Frequency.P_Frequency_ID = P_PERIOD.P_Frequency_ID " +
					 "WHERE  P_Period.IsActive = 'Y'  AND P_Period.StartDate <= " + DB.TO_DATE(period.getStartDate()) +
					 " AND   P_YEAR.P_CALENDAR_ID = "  + period.getP_Calendar_ID() + 
					 " ORDER BY P_Period.StartDate DESC";

        sql = MRole.getDefault().addAccessSQL (sql, "P_Period", true, false);	// fully qualidfied - RO 

        genericLoadFromSQL(sql);
        
    	setDefaultValue( lookup(period.get_ID()) );
		setSelectedItem( null );
    }

    public void load( int Default_Period_ID ) {
       	P_Period def = P_Period.get(Env.getCtx(), Default_Period_ID, null);
        P_Period p = P_Period.getOpenPeriodWithCalendar(Env.getCtx(), def.getP_Calendar_ID(), null);
        
        load(p);
    }

}
