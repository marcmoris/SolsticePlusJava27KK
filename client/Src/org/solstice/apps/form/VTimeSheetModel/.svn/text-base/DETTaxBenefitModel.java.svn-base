/*
 * Created on 1 sept. 2005
 *
 */
package org.solstice.apps.form.VTimeSheetModel;

import java.math.BigDecimal;
import java.util.Vector;

import javax.swing.JLabel;

import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.solstice.apps.form.VTimeSheetModel.Lookup.LookupPeriod;
import org.solstice.apps.form.VTimeSheetModel.Lookup.LookupTaxableBenefit;

public class DETTaxBenefitModel extends DETableModel {
    private static final String[] COLUMN_NAMES = { 
      Msg.translate(Env.getCtx(), "Org" ),
      Msg.translate(Env.getCtx(), "TaxableBenefit" ), 
      Msg.translate(Env.getCtx(), "Amount" ),
      Msg.translate(Env.getCtx(), "Period" ),
	  ""
    };
    
    public static final int COL_ORIGINE = 0;
    public static final int COL_BENEFIT = 1;
    public static final int COL_AMOUNT = 2;
    public static final int COL_PERIOD = 3;
    public static final int COL_DELETE = 4;
    public static final int COL_ID = 5;
    
    public static final int[] COLUMN_WIDTHS = { 30, 325, 325, 325, 20 };

    public int[] getColumnWidths() { return COLUMN_WIDTHS; }
    
    
    public DETTaxBenefitModel( Vector data, LookupTaxableBenefit benefit, LookupPeriod periods ) {
    	super( data, COLUMN_NAMES, true );
    
        setAlignment( new int[] {COL_AMOUNT}, JLabel.RIGHT );
        setAlignment( new int[] {COL_DELETE}, JLabel.CENTER );
        setComboData( COL_BENEFIT, benefit );
        setComboData( COL_PERIOD, periods );
    }
    
	public Object[] newRow(int row, int col) {
		Object[] newRow = new Object[getColumnCount()+3];
		newRow[COL_ORIGINE] = "AJT";
		return newRow;
	}

	public Class getColumnClass(int col) {
		if( col == COL_AMOUNT ) return BigDecimal.class;
		if( col == COL_DELETE ) return Boolean.class;
		return super.getColumnClass(col);
	}
	public int indexDeleteColumn() { return COL_DELETE; }

    public boolean isCellEditable(int row, int col) {
        if( col == COL_ORIGINE ) return false;
        Object val = getValueAt(row, COL_ORIGINE);
        if( indexNewRow() == row ) return super.isCellEditable(row, col);
        if( isDataRow(row) && "AJT".equals(val) ) return super.isCellEditable(row, col);
//        return true;
        return false;
    }
    
    private boolean m_dirty = false;
    public void setValueAt(Object val, int row, int col) {
        super.setValueAt(val, row, col);
        m_dirty = true;
    }
    
    public boolean isDirty() { return m_dirty; }
}
