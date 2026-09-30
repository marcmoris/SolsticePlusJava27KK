/*
 * Created on 1 sept. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.solstice.apps.form.VTimeSheetModel.Lookup;

import java.util.ArrayList;

import org.compiere.util.KeyNamePair;

/**
 * @author jeacat01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LookupWeek extends AbstractKNPModel {
    
    public LookupWeek( int numberOfWeek) {
    	/*
    	 * 
    	 */    	
        ArrayList<KeyNamePair> data = new ArrayList<KeyNamePair>();
        for(int idx=0;idx<numberOfWeek;idx++) {
        	data.add( new KeyNamePair(idx+1, String.valueOf(idx+1)) );
        }
        setData(data);
    }

}
