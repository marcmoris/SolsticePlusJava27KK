/*
 * Created on 13 sept. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.solstice.apps.form.VEmployeeCalendarUtil;

import java.math.BigDecimal;
import java.util.Vector;

import javax.swing.JLabel;

import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.solstice.apps.form.VTimeSheetModel.DETableModel;
import org.solstice.apps.form.VTimeSheetModel.Lookup.LookupGain;

/**
 * @author jeacat01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DETDayModel extends DETableModel {
    
    private static final String[] COLUMN_NAMES = { 
        Msg.translate(Env.getCtx(), "Gain" ), 
        Msg.translate(Env.getCtx(), "Qty" ), 
        ""
    };

    public static final int COL_GAIN = 0;
    public static final int COL_QTE = 1;
    public static final int COL_DELETE = 2;
    public static final int COL_ID = 3;
    public static final int COL_AUTORISE = 4;

    public DETDayModel( Vector data, LookupGain gains ) {
    	super( data, COLUMN_NAMES, true );

    	tagSummable( new int[] {COL_QTE} );
    	setAlignment( new int[] {COL_QTE}, JLabel.RIGHT );
        setAlignment( new int[] {COL_DELETE}, JLabel.CENTER );
        
        setComboData( COL_GAIN, gains );
    }
    
	public Object[] newRow() {
		return new Object[COLUMN_NAMES.length+2];
	}

	public Class getColumnClass(int col) {
		if( col == COL_QTE ) return BigDecimal.class;
		if( col == COL_DELETE ) return Boolean.class;
		return super.getColumnClass(col);
	}
	public int indexDeleteColumn() { return COL_DELETE; }

}
