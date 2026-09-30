/*
 * Created on 1 nov. 2005
 *
 */
package org.solstice.apps.form.VEmployeeCalendarUtil;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JLabel;

import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.solstice.apps.form.VTimeSheetModel.DETableModel;
import org.solstice.apps.form.VTimeSheetModel.Lookup.LookupGain;

import solstice.model.P_Employee_Calendar;
import solstice.model.P_Employee_NonBusinessDay;

public class CalendarGridModel extends DETableModel {
    private final static String[] COLUMN_NAMES = new String[] {
        Msg.translate(Env.getCtx(), "Date"), 
        Msg.translate(Env.getCtx(), "Date"), 
        "D", "L", "M", "M", "J", "V", "S",
        Msg.translate(Env.getCtx(), "GainCode"), 
        Msg.translate(Env.getCtx(), "Qty" ), 
        Msg.translate(Env.getCtx(), "calAutorisationAbsences"),
        ""
    };
    public static final int COL_DATE_DEBUT = 0;
    public static final int COL_DATE_FIN = 1;
    public static final int COL_DIMANCHE = 2;
    public static final int COL_LUNDI = 3;
    public static final int COL_MARDI = 4;
    public static final int COL_MERCREDI = 5;
    public static final int COL_JEUDI = 6;
    public static final int COL_VENDREDI = 7;
    public static final int COL_SAMEDI = 8;
    public static final int COL_GAIN = 9;
    public static final int COL_QTY = 10;
    public static final int COL_AUTORISE = 11;
    public static final int COL_DELETE = 12;
    public static final int COL_ID = 13;
    public static final int COL_DIRTY_FLAG = 14;
    public static final int COL_LISTE_INTERNE = 15;
    
    public static final int[] COLUMN_WIDTHS = { 
        120, 120, 18, 18, 18, 18, 18, 18, 18, 120, 120+120, 120+120, 20 };

    public int[] getColumnWidths() { return COLUMN_WIDTHS; }

    private LookupGain m_gains;
    private int m_employeeIdCache;
    private int m_calendarIdCache;
    
	public CalendarGridModel( Vector data, 
							  LookupGain gains) {
		super(data, COLUMN_NAMES, true);
		
	    setAlignment( new int[] {COL_QTY}, JLabel.RIGHT );
	    setAlignment( new int[] {COL_DIMANCHE, COL_LUNDI, COL_MARDI, 
	            				 COL_MERCREDI, COL_JEUDI, COL_VENDREDI,
	            				 COL_SAMEDI, COL_DELETE}, JLabel.CENTER );
	    setComboData( COL_GAIN, gains );
	    
	    m_gains = gains;
    };

	public Class getColumnClass(int col) {
		if( col == COL_QTY ) return BigDecimal.class;
		if( col >= COL_DIMANCHE && col <= COL_SAMEDI ) return Boolean.class;
		if( col == COL_DATE_DEBUT ) return Date.class;
		if( col == COL_DATE_FIN ) return Date.class;
		if( col == COL_DELETE ) return Boolean.class;
		return super.getColumnClass(col);
	}
	
	public int indexDeleteColumn() { return COL_DELETE; }

	public Object[] newRow(int row, int col) {
		Object[] objs = new Object[getColumnCount()+3];
		objs[COL_DIMANCHE] = Boolean.FALSE;
        objs[COL_LUNDI] = Boolean.TRUE;
        objs[COL_MARDI] = Boolean.TRUE;
        objs[COL_MERCREDI] = Boolean.TRUE;
        objs[COL_JEUDI] = Boolean.TRUE;
        objs[COL_VENDREDI] = Boolean.TRUE;
        objs[COL_SAMEDI] = Boolean.FALSE;
		return objs;
	}

    public boolean isCellEditable(int row, int col) {
        if( col == COL_AUTORISE ) return false;
        if( indexNewRow() == row ) return super.isCellEditable(row, col);
        if( isDataRow(row) ) return super.isCellEditable(row, col);
        return false;
    }
    
    public void setValueAt(Object val, int row, int col) {
        super.setValueAt( Boolean.TRUE, row, COL_DIRTY_FLAG );
        super.setValueAt(val, row, col);
    }
    
    public void load( int employeeId, int year) {
        PreparedStatement statm = null;
        ResultSet rs = null;
        m_employeeIdCache = employeeId;
        m_calendarIdCache = P_Employee_Calendar.getEmployeeCalendarId(employeeId, year);
        try {
            statm = DB.prepareStatement( 
                "SELECT E.NONBUSINESSDATE, E.P_GAIN_ID, E.DAYQTY, " +
                	"CASE WHEN EXISTS( SELECT 1 FROM P_Absence_Detail ad WHERE SOURCE = 'C' AND ad.SOURCE_ID = E.P_EMPLOYEE_NONBUSINESSDAY_ID ) THEN 'Oui' ELSE 'Non' END AUTH, " +
                	"e.P_EMPLOYEE_NONBUSINESSDAY_ID " +
                "FROM P_EMPLOYEE_NONBUSINESSDAY E " +
                "WHERE P_EMPLOYEE_CALENDAR_ID = ? " +
                "ORDER BY E.P_GAIN_ID, E.NONBUSINESSDATE ", null);
            statm.setInt( 1, m_calendarIdCache );
            rs = statm.executeQuery();
            
            Vector<Object> data = new Vector<Object>();
            Object[] prev = null;
            Object[] baseLine = null;
            Calendar nextDate = null;
            Date oldDate = null;
            Object gain = null;
            Object qty = null;
            Object autorise = null;
            Calendar date = Calendar.getInstance();
            while( rs.next() ) {
                date.setTimeInMillis(rs.getDate(1).getTime());
                int day = date.get(Calendar.DAY_OF_WEEK);
                Object[] ligne = new Object[getColumnCount()+3];
                ligne[COL_DATE_DEBUT] = rs.getDate(1);
                ligne[COL_DATE_FIN] = rs.getDate(1);
                ligne[COL_DIMANCHE] = new Boolean(day == Calendar.SUNDAY);
                ligne[COL_LUNDI] = new Boolean(day == Calendar.MONDAY);
                ligne[COL_MARDI] = new Boolean(day == Calendar.TUESDAY);
                ligne[COL_MERCREDI] = new Boolean(day == Calendar.WEDNESDAY);
                ligne[COL_JEUDI] = new Boolean(day == Calendar.THURSDAY);
                ligne[COL_VENDREDI] = new Boolean(day == Calendar.FRIDAY);
                ligne[COL_SAMEDI] = new Boolean(day == Calendar.SATURDAY);
                ligne[COL_GAIN] = m_gains.lookup( rs.getInt(2) );
                ligne[COL_QTY] = rs.getBigDecimal(3);
                ligne[COL_AUTORISE] = rs.getString(4);
                ligne[COL_ID] = new Integer(rs.getInt(5));
                ligne[COL_DELETE] = Boolean.FALSE;
                ligne[COL_DIRTY_FLAG] = Boolean.FALSE;
                ligne[COL_LISTE_INTERNE] = null;
                if( ( (nextDate == null || rs.getDate(1).compareTo(nextDate.getTime()) != 0)
                        && (oldDate == null || rs.getDate(1).compareTo(oldDate) != 0) )
                	|| ligne[COL_GAIN].equals(gain) == false
                	|| ligne[COL_QTY].equals(qty) == false
                	|| ligne[COL_AUTORISE].equals(autorise) == false  
                	) {
                    data.add(ligne);
                    oldDate = rs.getDate(1);
                    nextDate = Calendar.getInstance();
                    nextDate.setTimeInMillis( rs.getDate(1).getTime() );
                    nextDate.add( Calendar.DATE, 1 );
                    gain = ligne[COL_GAIN];
                    qty = ligne[COL_QTY];
                    autorise = ligne[COL_AUTORISE];
                    prev = ligne;
                }
                else {
                    prev[COL_DATE_FIN] = rs.getDate(1); 
                    prev[COL_DIMANCHE] = new Boolean(((Boolean)prev[COL_DIMANCHE]).booleanValue() || ((Boolean)ligne[COL_DIMANCHE]).booleanValue());
                    prev[COL_LUNDI] = new Boolean(((Boolean)prev[COL_LUNDI]).booleanValue() || ((Boolean)ligne[COL_LUNDI]).booleanValue());
                    prev[COL_MARDI] = new Boolean(((Boolean)prev[COL_MARDI]).booleanValue() || ((Boolean)ligne[COL_MARDI]).booleanValue());
                    prev[COL_MERCREDI] = new Boolean(((Boolean)prev[COL_MERCREDI]).booleanValue() || ((Boolean)ligne[COL_MERCREDI]).booleanValue());
                    prev[COL_JEUDI] = new Boolean(((Boolean)prev[COL_JEUDI]).booleanValue() || ((Boolean)ligne[COL_JEUDI]).booleanValue());
                    prev[COL_VENDREDI] = new Boolean(((Boolean)prev[COL_VENDREDI]).booleanValue() || ((Boolean)ligne[COL_VENDREDI]).booleanValue());
                    prev[COL_SAMEDI] = new Boolean(((Boolean)prev[COL_SAMEDI]).booleanValue() || ((Boolean)ligne[COL_SAMEDI]).booleanValue());
                    if( prev[COL_LISTE_INTERNE] == null ) {
                        prev[COL_LISTE_INTERNE] = new ArrayList();
                        Object[] original = new Object[getColumnCount()+3];
                        original[COL_DATE_DEBUT] = ((Date)prev[COL_DATE_DEBUT]).clone();
                        original[COL_DATE_FIN] = ((Date)prev[COL_DATE_FIN]).clone();
                        original[COL_GAIN] = prev[COL_GAIN];
                        original[COL_QTY] = prev[COL_QTY];
                        original[COL_AUTORISE] = prev[COL_AUTORISE];
                        original[COL_ID] = prev[COL_ID];
                        original[COL_DELETE] = prev[COL_DELETE];
                        original[COL_DIRTY_FLAG] = prev[COL_DIRTY_FLAG];
                        original[COL_LISTE_INTERNE] = null;
                        ((ArrayList)prev[COL_LISTE_INTERNE]).add( original );
                    }
                    ((ArrayList)prev[COL_LISTE_INTERNE]).add(ligne);
                    oldDate = rs.getDate(1);
                    nextDate = Calendar.getInstance();
                    nextDate.setTimeInMillis( rs.getDate(1).getTime() );
                    nextDate.add( Calendar.DATE, 1 );
                }
            }
            setData(data);
            
            resetSort( COL_DATE_DEBUT );
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
        finally {
//            DB.DB_CLOSE( rs );
//            DB.DB_CLOSE( statm );
        }
    }
    
    private final long MILLISEC_PAR_JOUR = 24 * 60 * 60 * 1000;
    public void save() {
        int prevYear = -1;
        for( int i=0; i<getRowCount(); i++ ) {
            if( isDataRow(i) && ((Boolean)getValueAt(i, COL_DIRTY_FLAG)).booleanValue() == true ) {
                if( getValueAt(i, COL_LISTE_INTERNE) == null ) {
		            if( isRowDeleted(i) == false ) { 
		                if( getValueAt(i,COL_DATE_DEBUT) != null ) {
		                    Date debut = (Date)getValueAt(i,COL_DATE_DEBUT);
		                    Date fin = (Date)getValueAt(i,COL_DATE_FIN);
	                        long msDebut = debut.getTime();
	                        long msFin = fin.getTime();
		                    if( debut.compareTo(fin) == 0 || msDebut > msFin ) {
				                doSave( (Integer)getValueAt(i, COL_ID), ((Date)getValueAt(i,COL_DATE_DEBUT)).getTime(),
				                        m_calendarIdCache, m_gains.lookup(getValueAt(i, COL_GAIN)),
			                        	(BigDecimal)getValueAt(i, COL_QTY) );
		                    }
		                    else {
				                doDelete( (Integer)getValueAt(i, COL_ID) );
		                        long nbreJour = (msFin - msDebut) / MILLISEC_PAR_JOUR; 
		                        creerPlageDate( nbreJour+1, debut, 
										((Boolean)getValueAt(i,COL_DIMANCHE)).booleanValue(),
										((Boolean)getValueAt(i,COL_LUNDI)).booleanValue(),
										((Boolean)getValueAt(i,COL_MARDI)).booleanValue(),
										((Boolean)getValueAt(i,COL_MERCREDI)).booleanValue(),
										((Boolean)getValueAt(i,COL_JEUDI)).booleanValue(),
										((Boolean)getValueAt(i,COL_VENDREDI)).booleanValue(),
										((Boolean)getValueAt(i,COL_SAMEDI)).booleanValue(),
		                                m_calendarIdCache, 
		                                m_gains.lookup(getValueAt(i, COL_GAIN)),
			                        	(BigDecimal)getValueAt(i, COL_QTY) );
		                    }
		                }
		            }
		            else { 
		                doDelete( (Integer)getValueAt(i, COL_ID) );
		            }
                }
                else {
                    if( isRowDeleted(i) == false ) {
	                    processListe( (Date)getValueAt(i,COL_DATE_DEBUT),
	                            	  (Date)getValueAt(i,COL_DATE_FIN),
	                            	  ((Boolean)getValueAt(i,COL_DIMANCHE)).booleanValue(),
	                            	  ((Boolean)getValueAt(i,COL_LUNDI)).booleanValue(),
	                            	  ((Boolean)getValueAt(i,COL_MARDI)).booleanValue(),
	                            	  ((Boolean)getValueAt(i,COL_MERCREDI)).booleanValue(),
	                            	  ((Boolean)getValueAt(i,COL_JEUDI)).booleanValue(),
	                            	  ((Boolean)getValueAt(i,COL_VENDREDI)).booleanValue(),
	                            	  ((Boolean)getValueAt(i,COL_SAMEDI)).booleanValue(),
	                            	  m_calendarIdCache, 
	                            	  m_gains.lookup(getValueAt(i, COL_GAIN)),
	                            	  (BigDecimal)getValueAt(i, COL_QTY),
	                            	  (List)getValueAt(i, COL_LISTE_INTERNE) );
                    }
                    else {
                        for( Iterator iter = ((List)getValueAt(i, COL_LISTE_INTERNE)).iterator(); iter.hasNext(); ) {
                            Object[] data = (Object[])iter.next();
                            doDelete( (Integer)data[COL_ID] );
                        }
                    }
                }
            }
        }
    }
    
    private void processListe( Date debut, Date fin,
            				   boolean dimanche, boolean lundi, boolean mardi,
            				   boolean mercredi, boolean jeudi, boolean vendredi, boolean samedi,
            				   int calendarId, int gainId, BigDecimal qty, List liste ) {
        Calendar date = Calendar.getInstance();
        // On sauvegarde/supprime les données selon l'intervalle debut/fin
        for( Iterator iter = liste.iterator(); iter.hasNext(); ) {
            Object[] data = (Object[])iter.next();
            date.setTimeInMillis(((Date)data[COL_DATE_DEBUT]).getTime());
            int day = date.get(Calendar.DAY_OF_WEEK);
            if( debut.compareTo((Date)data[COL_DATE_DEBUT]) <= 0 
                && fin.compareTo((Date)data[COL_DATE_FIN]) >= 0
                && ( (day == Calendar.SUNDAY && dimanche) 
                        || (day == Calendar.MONDAY && lundi) 
                        || (day == Calendar.TUESDAY && mardi) 
                        || (day == Calendar.WEDNESDAY && mercredi) 
                        || (day == Calendar.THURSDAY && jeudi) 
                        || (day == Calendar.FRIDAY && vendredi)
                        || (day == Calendar.SATURDAY && samedi) ) ) {
                doSave( (Integer)data[COL_ID], date.getTimeInMillis(), calendarId, gainId, qty );
            }
            else {
                doDelete( (Integer)data[COL_ID] );
            }
        }
        if( debut.equals(fin) ) {
            doSave( new Integer(-1), debut.getTime(), calendarId, gainId, qty );
        }
        else {
	        // On crée les nouvelles données qui sont à l'extérieure de l'intervalle
	        Object[] dataDebut = (Object[])liste.get(0);
	        Object[] dataFin = (Object[])liste.get(liste.size()-1);
	        
	        if( debut.compareTo((Date)dataFin[COL_DATE_DEBUT]) > 0 || fin.compareTo((Date)dataDebut[COL_DATE_DEBUT]) < 0 ) {
	            long nbreJour = (fin.getTime()-debut.getTime()) / MILLISEC_PAR_JOUR;
	            creerPlageDate( nbreJour+1, debut, dimanche, lundi, mardi, mercredi, jeudi, vendredi, samedi, calendarId, gainId, qty );
	        }
	        else { 
		        if( debut.compareTo((Date)dataDebut[COL_DATE_DEBUT]) < 0 ) {
		            long nbreJour = (((Date)dataDebut[COL_DATE_DEBUT]).getTime()-debut.getTime()) / MILLISEC_PAR_JOUR;
		            creerPlageDate( nbreJour, debut, dimanche, lundi, mardi, mercredi, jeudi, vendredi, samedi, calendarId, gainId, qty );
		        }
		        
		        if( fin.compareTo((Date)dataFin[COL_DATE_DEBUT]) > 0 ) {
		            long nbreJour = (fin.getTime()-((Date)dataFin[COL_DATE_DEBUT]).getTime()) / MILLISEC_PAR_JOUR;
		            long msDebut = ((Date)dataFin[COL_DATE_DEBUT]).getTime() + MILLISEC_PAR_JOUR;
		            creerPlageDate( nbreJour, new Date(msDebut), dimanche, lundi, mardi, mercredi, jeudi, vendredi, samedi, calendarId, gainId, qty );
		        }
	        }
        }
    }
    
    private void creerPlageDate( long nbreJour, Date date, 
				            	 boolean dimanche, boolean lundi, boolean mardi,
				            	 boolean mercredi, boolean jeudi, boolean vendredi, boolean samedi,
				            	 int calendarId, int gainId, BigDecimal qty ) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis( date.getTime() );
        for( int i=0; i<nbreJour; i++ ) {
            int day = cal.get(Calendar.DAY_OF_WEEK);
            if( (day == Calendar.SUNDAY && dimanche) 
                    || (day == Calendar.MONDAY && lundi) 
                    || (day == Calendar.TUESDAY && mardi) 
                    || (day == Calendar.WEDNESDAY && mercredi) 
                    || (day == Calendar.THURSDAY && jeudi) 
                    || (day == Calendar.FRIDAY && vendredi)
                    || (day == Calendar.SATURDAY && samedi) ) {
                doSave( null, cal.getTimeInMillis(), calendarId, gainId, qty );
            }
            cal.add( Calendar.DATE, 1 );
        }
    }
    
    private void doSave( Integer id, long date, int calendarId, int gainId, BigDecimal qty ) 
    {
        if( qty == null ) 
        	return;
        
        //MODIF KM
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis( date );
        int currentYear = cal.get(Calendar.YEAR);
        P_Employee_Calendar empCal = null;
        if(calendarId == -1)
        {
        	calendarId = P_Employee_Calendar.getEmployeeCalendarId(m_employeeIdCache, currentYear);

        	if(calendarId == -1)
        	{
        		empCal = new P_Employee_Calendar(Env.getCtx(), -1, null);
        		empCal.setYear(String.valueOf(currentYear));
        		empCal.setP_Employee_ID(m_employeeIdCache);
            	if(!empCal.save())
            	{
            		//log erreur
            		System.out.println("KM Can't save Calendar");
            	}
            	else
            	{
            		calendarId = empCal.getP_Employee_Calendar_ID();
            	}
        	}
        	else
        	{
            	empCal = new P_Employee_Calendar(Env.getCtx(), calendarId, null);
        	}
        }
        else
        {
        	calendarId = P_Employee_Calendar.getEmployeeCalendarId(m_employeeIdCache, currentYear);
        	empCal = new P_Employee_Calendar(Env.getCtx(), calendarId, null);
        }
        
        if(calendarId == -1 || empCal == null)
        {
        	System.out.println("KM Can't save NonBusinessDay - invalid calendar");
        	return;
        }
        
        int calendarYear = Integer.parseInt(empCal.getYear());
        
        if(calendarYear != currentYear)
        {
        	calendarId = P_Employee_Calendar.getEmployeeCalendarId(m_employeeIdCache, currentYear);
        	empCal = new P_Employee_Calendar(Env.getCtx(), calendarId, null);
        	if(calendarId == -1)
        	{
            	calendarId = P_Employee_Calendar.getEmployeeCalendarId(m_employeeIdCache, currentYear);
            	empCal = new P_Employee_Calendar(Env.getCtx(), calendarId, null);        		
        	}
        }
        //FIN MODIF KM
        
        P_Employee_NonBusinessDay en = new P_Employee_NonBusinessDay(Env.getCtx(), id == null ? -1 : id.intValue(), null);
        en.setNonBusinessDate( new Timestamp(date) );
        en.setP_Employee_Calendar_ID(calendarId);
        en.setP_Gain_ID(gainId);
        en.setDayQty(qty);
		en.save();
    }
    
    private void doDelete( Integer id ) {
        if( id == null ) return;
        P_Employee_NonBusinessDay en = new P_Employee_NonBusinessDay( Env.getCtx(), id.intValue(), null );
        en.delete(true);
    }
}
