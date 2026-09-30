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
import org.solstice.apps.form.VTimeSheetModel.Lookup.LookupDeduction;
import org.solstice.apps.form.VTimeSheetModel.Lookup.LookupPeriod;

public class DETDeductionModel extends DETableModel {
    private static final String[] COLUMN_NAMES = { 
      Msg.translate(Env.getCtx(), "Org" ),
      Msg.translate(Env.getCtx(), "Deduction" ), 
      Msg.translate(Env.getCtx(), "SalaryAdmissible"), 
      Msg.translate(Env.getCtx(), "HourAdmissible"), 
      Msg.translate(Env.getCtx(), "EmployeePart"), 
      Msg.translate(Env.getCtx(), "EmployerPart"), 
      Msg.translate(Env.getCtx(), "AccumulationAmount"), 
      Msg.translate(Env.getCtx(), "Remboursement"),
      Msg.translate(Env.getCtx(), "Period" ),
	  ""
    };

    public static final int COL_ORIGINE = 0;
    public static final int COL_DEDUCTION = 1;
    public static final int COL_SALARY_ADM = 2;
    public static final int COL_HOUR_ADM = 3;
    public static final int COL_EMPLOYEE_PART = 4;
    public static final int COL_EMPLOYER_PART = 5;
    public static final int COL_ACCUMULATION = 6;
    public static final int COL_REMBOURSEMENT = 7;
    public static final int COL_PERIOD = 8;
    public static final int COL_DELETE = 9;
    public static final int COL_ID = 10;
    
    public static final int[] COLUMN_WIDTHS = { 
        30, 120, 120, 120, 120, 120, 120, 120, 120, 20 };

    public int[] getColumnWidths() { return COLUMN_WIDTHS; }

    
	public DETDeductionModel( Vector data, 
							  LookupDeduction deductions,
							  LookupPeriod periods) {
		super(data, COLUMN_NAMES, true);
		
	    tagSummable( new int[] {COL_EMPLOYEE_PART, COL_EMPLOYER_PART, COL_ACCUMULATION, COL_REMBOURSEMENT} );
	    setAlignment( new int[] {COL_SALARY_ADM, COL_HOUR_ADM, COL_EMPLOYEE_PART, 
	    						 COL_EMPLOYER_PART, COL_ACCUMULATION, COL_REMBOURSEMENT}, JLabel.RIGHT );
	    setAlignment( new int[] {COL_DELETE}, JLabel.CENTER );
	    setComboData( COL_DEDUCTION, deductions );
	    setComboData( COL_PERIOD, periods );
    };

	public Class getColumnClass(int col) {
		if( col >= COL_SALARY_ADM && col <= COL_REMBOURSEMENT ) return BigDecimal.class;
		if( col == COL_DELETE ) return Boolean.class;
		return super.getColumnClass(col);
	}
	
	public int indexDeleteColumn() { return COL_DELETE; }

	public Object[] newRow(int row, int col) {
		Object[] newRow = new Object[getColumnCount()+3];
		newRow[COL_ORIGINE] = "AJT";
		return newRow;
	}

    public boolean isCellEditable(int row, int col) {
  //      if( col == COL_ORIGINE ) return false;
        Object val = getValueAt(row, COL_ORIGINE);
        if( indexNewRow() == row ) return super.isCellEditable(row, col);
        if( isDataRow(row) && "AJT".equals(val) ) return super.isCellEditable(row, col);
        //2008-04-16 - permettre la destruction de tout type de ligne.
        if ( col == COL_DELETE ) return true;
     
        return false;
//        return true;
    }

    private boolean m_dirty = false;
    public void setValueAt(Object val, int row, int col) {
        super.setValueAt(val, row, col);
        m_dirty = true;
    }
    
    public boolean isDirty() { return m_dirty; }
}
