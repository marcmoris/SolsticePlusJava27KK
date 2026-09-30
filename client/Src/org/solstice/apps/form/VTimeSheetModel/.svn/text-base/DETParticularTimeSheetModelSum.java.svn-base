/******************************************************************************
* Product: Solstice+ Payroll & Human Resources Management                    *
* Copyright (C) 2008 ProGestion Informatique, Inc. All Rights Reserved.      *
* This program is free software, you can redistribute it and/or modify it    *
* under the terms version 2 of the GNU General Public License as published   *
* by the Free Software Foundation. This program is distributed in the hope   *
* that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
* warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
* See the GNU General Public License for more details.                       *
* You should have received a copy of the GNU General Public License along    *
* with this program, if not, write to the Free Software Foundation, Inc.,    *
* 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
* For the text or an alternative of this public license, you may reach us    *
* ProGestion Informatique, 210-5300 Bld des Galerie, Quebec, G2K 2A2 Canada  *
* or via info@progestion.net or http://www.progestion.net/license.html       *
******************************************************************************/
package org.solstice.apps.form.VTimeSheetModel;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.JLabel;

import org.compiere.model.MRole;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.solstice.apps.form.VTimeSheetModel.Lookup.AbstractKNPModel;
import org.solstice.apps.form.VTimeSheetModel.Lookup.LookupAssignment;
import org.solstice.apps.form.VTimeSheetModel.Lookup.LookupEmployee;
import org.solstice.apps.form.VTimeSheetModel.Lookup.LookupGain;
import org.solstice.apps.form.VTimeSheetModel.Lookup.LookupPeriod;
import org.solstice.apps.form.VTimeSheetModel.Lookup.LookupActivity;
import org.solstice.apps.form.VTimeSheetModel.Lookup.LookupDept;

import solstice.model.P_Assignment;
import solstice.model.P_Assignment_Param;
import solstice.model.P_Employee;
import solstice.model.P_Gain;
import solstice.model.P_Particular_Sheet;
import solstice.model.P_Period;

/**
 * @author jeacat01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DETParticularTimeSheetModelSum extends DETableModel {
    private static final String[] COLUMN_NAMES = { 
        Msg.translate(Env.getCtx(), "Period" ), 
        Msg.translate(Env.getCtx(), "P_Employee_ID"), 
        Msg.translate(Env.getCtx(), "Day"), 
        Msg.translate(Env.getCtx(), "GainCode"), 
        Msg.translate(Env.getCtx(), "Assignment"), 
        Msg.translate(Env.getCtx(), "Hourly_Rate"), 
        Msg.translate(Env.getCtx(), "DayQty"), 
        Msg.translate(Env.getCtx(), "Department"), 
        Msg.translate(Env.getCtx(), "Distribution" ),
        ""
    };

    public static final int COL_PERIOD = 0;
    public static final int COL_EMPLOYEE = 1;
    public static final int COL_DAY = 2;
    public static final int COL_GAIN = 3;
    public static final int COL_ASSIGNMENT = 4;
    public static final int COL_HOURLY_RATE = 5;
    public static final int COL_DAY_QTY = 6;
    public static final int COL_DEPARTMENT = 7;
    public static final int COL_DISTRIBUTION = 8;
    public static final int COL_DELETE = 9;
    public static final int COL_ID = 10;
    public static final int COL_DIRTY_FLAG = 11;

    public static final int[] COLUMN_WIDTHS = { 100, 270, 100, 220, 100,100, 100,220,220, 20 };

	private LookupPeriod m_periods;
	private LookupEmployee m_employees;
	private LookupGain m_gains;
    private LookupAssignment m_assignments;
    private LookupActivity m_activity;
    private LookupDept p_dept;
    
    
    public CLogger			log = CLogger.getCLogger (getClass());
    
	public DETParticularTimeSheetModelSum( Vector data,
	        							LookupEmployee employees,
	        							LookupGain gains,
									    LookupAssignment assignments,
									    LookupPeriod periods,
									    LookupActivity activity,
									    LookupDept dept) 
	{
		super(data, COLUMN_NAMES, true);
		
		m_periods = periods;
		m_employees = employees;
		m_gains = gains;
		m_assignments = assignments;
		m_activity = activity;
		p_dept = dept;
		
		setAlignment( new int[] {COL_HOURLY_RATE}, JLabel.RIGHT );
		setAlignment( new int[] {COL_DAY_QTY}, JLabel.RIGHT );
		setAlignment( new int[] {COL_DELETE}, JLabel.CENTER );
//		setComboData( COL_EMPLOYEE, employees );
		setComboData( COL_ASSIGNMENT, assignments );
		setComboData( COL_GAIN, gains );
		setComboData( COL_DEPARTMENT, dept);
		setComboData( COL_DISTRIBUTION, activity);
//		setComboData( COL_PERIOD, periods );
	};

    public int[] getColumnWidths() { return COLUMN_WIDTHS; }
	
	public Class getColumnClass(int col) {
		if( col == COL_DAY ) return Date.class;
		if( col == COL_HOURLY_RATE ) return BigDecimal.class;
		if( col == COL_DAY_QTY ) return BigDecimal.class;
		if( col == COL_DELETE ) return Boolean.class;
		return super.getColumnClass(col);
	}
	
	public int indexDeleteColumn() { return COL_DELETE; }
	
	public Object[] newRow(int row, int col) {
	    Object[] row1 = new Object[getColumnCount()+2];
        row1[COL_DELETE] = Boolean.FALSE;
        row1[COL_DIRTY_FLAG] = Boolean.TRUE;
        //si on est pas la premiere ligne, on set l'Employé selon la ligne precedente
        if(row > 0) {
            row1[COL_EMPLOYEE] = getValueAt(row-1, COL_EMPLOYEE);
            row1[COL_PERIOD] = getValueAt(row-1, COL_PERIOD);
        }
        else if( row == 0 ) {
            row1[COL_PERIOD] = m_periods.getDefaultValue();
        }
	    return row1;
	}

	public AbstractKNPModel getComboData( int row, int col ) {
	    AbstractKNPModel model = super.getComboData(row, col);
	    if( col == COL_ASSIGNMENT ) {
	        KeyNamePair emp = (KeyNamePair)getValueAt(row, COL_EMPLOYEE);
	        if( emp != null ) {
		        P_Period period = P_Period.get( Env.getCtx(), ((KeyNamePair)getValueAt(row, COL_PERIOD)).getKey(), null );
		        ((LookupAssignment)model).load(emp.getKey(), period.getEndDate());
	        }
	        else {
	            ((LookupAssignment)model).clear();
	        }
	    }	    
	    return model;
	}
	
    public void setValueAt(Object val, int row, int col) {
        super.setValueAt( Boolean.TRUE, row, COL_DIRTY_FLAG );
        if( col == COL_EMPLOYEE ) {
            if( val instanceof String ) {
                if( "".equals(val) == false ) super.setValueAt( m_employees.find((String)val), row, col ); 
            }            	
            else if( val != null ) super.setValueAt( val, row, col ); 
        }
        else if( col == COL_PERIOD ) {
            if( val instanceof String ) {
                if( "".equals(val) == false ) super.setValueAt( m_periods.find((String)val), row, col ); 
            }            	
            else if( val != null ) super.setValueAt( val, row, col ); 
        }
        else {
            super.setValueAt( val, row, col );
        }
                
        Object old = getValueAt(row, col);
        if( col == COL_GAIN && val != null && (old == null || val.equals(old) == false || getValueAt(row, COL_DAY_QTY) == null) ) {
            BigDecimal qte = getDefaultQte( getValueAt(row, COL_EMPLOYEE), (Date)getValueAt(row, COL_DAY), getValueAt(row, COL_GAIN) );
            if( qte != null ) {
                super.setValueAt( qte, row, COL_DAY_QTY );
                fireTableCellUpdated(row, COL_DAY_QTY);
            }
        }
    }
    
    private BigDecimal getDefaultQte( Object employeeVal, Date date, Object gainVal ) {
        if( employeeVal == null || date == null || gainVal == null ) return null;
        P_Gain gain = P_Gain.get( Env.getCtx(), m_gains.lookup(gainVal), null );
        if( "Q".equals(gain.getGainUnit()) == false ) return null;

        Timestamp ts = new Timestamp( date.getTime() );
        P_Employee employee = P_Employee.get( Env.getCtx(), m_employees.lookup(employeeVal), null );
        
        BigDecimal qte = null;
        PreparedStatement statm = null;
        ResultSet rs = null;
        try {
            statm = DB.prepareStatement( 
                "Select P_Assignment.P_Assignment_ID " + 
                "From P_Assignment, P_Assignment_Param " + 
                "WHERE P_Assignment.IsActive='Y' AND P_Assignment_Param.IsActive='Y' " + 
                "AND P_Assignment_Param.P_Assignment_ID = P_Assignment.P_Assignment_ID " +
                "AND P_Assignment.P_Employee_ID = ? " +
                "AND P_Assignment_Param.EffectIn <= ? " +
                "AND ? between isnull( P_Assignment.startdate, ?) and isnull( P_Assignment.enddate, ?) " , null);
            statm.setInt( 1, employee.getP_Employee_ID() );
            statm.setTimestamp( 2, ts );
            statm.setTimestamp( 3, ts );
            statm.setTimestamp( 4, ts );
            statm.setTimestamp( 5, ts );
            rs = statm.executeQuery();
            if( rs.next() ) {
                P_Assignment assignment = P_Assignment.get( Env.getCtx(), rs.getInt(1), null );
                P_Assignment_Param assignment_param = P_Assignment_Param.get(Env.getCtx(), assignment.getP_Assignment_ID(), ts, null);
                if( employee.isRWT(ts) || employee.isWorkingShortTime(ts)) 
                	qte = employee.getDay_HoursRWT(ts);
                else if(assignment_param != null && assignment_param.getP_Assignment_Param_ID() != 0)
                	qte = assignment_param.getDay_Hours();
                else
                {
                	qte = Env.ZERO;
                	log.log( Level.SEVERE, "getDefaultQte - Pas de paramètre d'affectation.");
                }
            }
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
        finally { 
//            DB.DB_CLOSE(statm);
//            DB.DB_CLOSE(rs);
        }
        return qte;
    }
	
    public Date munchDate( int row, int column, String value ) throws ParseException {
    	if (value == null ) return null;

		P_Period period = P_Period.get( Env.getCtx(), m_periods.lookup(getValueAt(row, COL_PERIOD)), null );
 		
		Calendar startDate = Calendar.getInstance();
	 	startDate.setTimeInMillis( period.getStartDate().getTime() );
	 	
 		Calendar endDate = Calendar.getInstance();
	 	endDate.setTimeInMillis( period.getEndDate().getTime() );

		int length = value.toString().length();
		if( length == 10 ) {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(value);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(date.getTime());
    		if( cal.before(startDate) || cal.after(endDate) ) return null; 
            return date;
        }
		if( length != 1 && length != 2 ) return null;
		
		int jour = Integer.parseInt(value);
	 	
	 	Calendar date;
	 	// si le jour est inférieur au début de la période, on suppose qu'on arrive à la fin du mois
	 	// donc la fin de la période est probablement dans le mois suivant
	 	if( jour < startDate.get(Calendar.DAY_OF_MONTH) )
	 	    date = (Calendar)endDate.clone();
	 	else
	 	    date = (Calendar)startDate.clone();
	 	
	 	date.set(Calendar.DAY_OF_MONTH, jour);
	 	
	 	// Extérieur de la période
		if( date.before(startDate) || date.after(endDate) ) return null; 

        return date.getTime();
    }

    public void load() { load( null ); }
    public void load( String where ) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        Vector<Object> data = new Vector<Object>();
        try {
            m_assignments.loadAll();
            
            String sql = "SELECT P_Particular_Sheet_ID, P_Employee.P_Employee_ID, DAY, P_GAIN_ID, P_Particular_Sheet.P_ASSIGNMENT_ID, " +
 		   				"isnull(DAYQTY,0) as DAYQTY, Hourly_Rate, P_SCHEDULE_ID, P_PERIOD_ID, P_Particular_Sheet.P_DEPARTMENT_ID, P_Particular_Sheet.C_ACTIVITY_ID " +
	   					"FROM P_Employee INNER JOIN P_Particular_Sheet ON P_Particular_Sheet.P_Employee_ID = P_Employee.P_Employee_ID  " 
 		   				;

    		sql = sql + " AND P_Employee." + MRole.getDefault().getClientWhere ( false);	// fully qualidfied - RO 

            if ( Env.getContextAsInt(Env.getCtx(), "#P_Payment_Group_ID") > 0 )
            	sql += " AND P_Payment_Group_ID = " + Env.getContext(Env.getCtx(), "#P_Payment_Group_ID"); 

            if( where != null && where.length() > 0 ) sql += " AND " + where;
            
            ps = DB.prepareStatement( sql , null); 
            
            rs = ps.executeQuery();
            
            while( rs.next() ) {
                Object[] ligne = new Object[getColumnCount()+3];
                ligne[COL_PERIOD] = m_periods.lookup(rs.getInt("P_PERIOD_ID"));
                ligne[COL_EMPLOYEE] = m_employees.lookup(rs.getInt("P_EMPLOYEE_ID"));
                ligne[COL_GAIN] = m_gains.lookup(rs.getInt("P_GAIN_ID"));
                int assignmentId = rs.getInt("P_ASSIGNMENT_ID");
                if( rs.wasNull() == false )
                    ligne[COL_ASSIGNMENT] = m_assignments.lookup(assignmentId);
                ligne[COL_DAY] = new Date(rs.getTimestamp("DAY").getTime());

                if ( rs.getBigDecimal("Hourly_Rate") != null )
                	ligne[COL_HOURLY_RATE] = rs.getBigDecimal("Hourly_Rate").setScale(4, BigDecimal.ROUND_HALF_UP);
                
                ligne[COL_DAY_QTY] = rs.getBigDecimal("DAYQTY").setScale(2, BigDecimal.ROUND_HALF_UP);
                ligne[COL_DEPARTMENT]  = p_dept.lookup(rs.getInt("P_DEPARTMENT_ID"));
                ligne[COL_DISTRIBUTION]  = m_activity.lookup(rs.getInt("C_ACTIVITY_ID"));
                ligne[COL_ID] = new Integer(rs.getInt("P_PARTICULAR_SHEET_ID"));
                ligne[COL_DELETE] = Boolean.FALSE;
                ligne[COL_DIRTY_FLAG] = Boolean.FALSE;
                data.add(ligne);
            }
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
        finally {
//            DB.DB_CLOSE( rs );
//            DB.DB_CLOSE( ps );
        }
        setData( data );
        resetSort( COL_EMPLOYEE );
    }
    
    public boolean save( String trxName ) {
        for( int i=0; i<getRowCount(); i++ ) {
            if( isDataRow(i) ) {
	            if( ((Boolean)getValueAt(i, COL_DIRTY_FLAG)).booleanValue() == true ) {
		            Integer integer = (Integer)getValueAt(i, COL_ID);
		            int id = integer != null ? integer.intValue() : -1;
	
		            if( isRowDeleted(i) == false ) {
		                if( (BigDecimal)getValueAt(i, COL_DAY_QTY) != null ) { 
				            P_Particular_Sheet ps = new P_Particular_Sheet( Env.getCtx(), id, trxName );
//				            ps.setP_Schedule_ID( 1000000 ); 
				            ps.setP_Period_ID( m_periods.lookup(getValueAt(i, COL_PERIOD)) );
				            ps.setP_Employee_ID( m_employees.lookup(getValueAt(i, COL_EMPLOYEE)) );
				            ps.setP_Gain_ID( m_gains.lookup(getValueAt(i, COL_GAIN)) );
				            ps.setP_Assignment_ID( m_assignments.lookup(getValueAt(i, COL_ASSIGNMENT)) );
				            ps.setDay( new Timestamp( ((Date)getValueAt(i, COL_DAY)).getTime()) );
				            ps.setHourly_Rate( (BigDecimal)getValueAt(i, COL_HOURLY_RATE)  );
				            ps.setDayQty( (BigDecimal)getValueAt(i, COL_DAY_QTY) );
				            ps.setC_Activity_ID(m_activity.lookup(getValueAt(i, COL_DISTRIBUTION)));
				            ps.setP_Department_ID(p_dept.lookup(getValueAt(i, COL_DEPARTMENT)));
				            ps.save();
		                }
		            }
		            else if( id != -1 ){
			            P_Particular_Sheet ps = new P_Particular_Sheet( Env.getCtx(), id, trxName );
			            ps.delete(true);
		            }
	            }
            }
        }
        return true;
    }
    
	public boolean isRequired( int row, int col ) { return col == COL_DAY; }
	
	public boolean hasUnits( int col ) { return col == COL_DAY_QTY; }
	public String getUnits( int row, int col ) { return m_gains.getUnite(getValueAt(row, COL_GAIN)); }

    public boolean isFastEdit(int row, int col) {
        return col == COL_PERIOD || col == COL_ASSIGNMENT || col == COL_GAIN;
    }
}
