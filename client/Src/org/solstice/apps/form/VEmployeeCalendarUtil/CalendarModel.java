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
package org.solstice.apps.form.VEmployeeCalendarUtil;

import java.awt.Color;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.solstice.apps.form.VEmployeeCalendar;
import org.solstice.apps.form.VTimeSheetModel.Lookup.LookupGain;

import solstice.model.P_Employee_Calendar;
import solstice.model.P_Employee_NonBusinessDay;
import solstice.model.P_Employee;
import solstice.model.P_Assignment_Param;

public class CalendarModel extends DefaultTableModel {
    public static final Color COLOR_STANDARD = new Color(200, 255, 200); //Color.GREEN;
    public static final Color COLOR_FERIE = new Color(255, 255, 200); //Color.YELLOW;
    public static final Color COLOR_AUTH_ABSENCE = new Color(200, 200, 255); //Color.BLUE;
    public static final Color COLOR_AUTRE_ABSENCE = new Color(255, 255, 255); //Color.WHITE;
    public static final Color COLOR_MULTI_CODE = new Color(255, 200, 200); //Color.RED;
    public static final Color COLOR_BASE = new Color(200, 200, 200); //Color.RED;
    
	private final String JOURS[] = { 
	    Msg.translate(Env.getCtx(), "Sunday"), 
		Msg.translate(Env.getCtx(), "Monday"), 
		Msg.translate(Env.getCtx(), "Tuesday"), 
		Msg.translate(Env.getCtx(), "Wednesday"), 
		Msg.translate(Env.getCtx(), "Thursday"), 
		Msg.translate(Env.getCtx(), "Friday"), 
		Msg.translate(Env.getCtx(), "Saturday") 
    };
	
	private BigDecimal m_standardTime[]; 
	
	private int m_employeeIdCache;
    private HashMap<Long, DayData> m_mapDataCache;
    private HashSet<Long> m_dateCache;

    private int m_defaultGainId;
    public void setDefaultGainId( int gainId ) { m_defaultGainId = gainId; }
    public int getDefaultGainId() { return m_defaultGainId; }
    private VEmployeeCalendar m_main;
    
    private String trxName = null;
    
    public CalendarModel( VEmployeeCalendar main ) {
        m_main = main;
        setColumnIdentifiers( JOURS );
        setRowCount(6);
        m_mapDataCache = new HashMap<Long, DayData>();
        m_dateCache = new HashSet<Long>();
        m_employeeIdCache = -1;
    }
    
    protected void setupMois( Calendar date ) {
        Calendar calendar = (Calendar)date.clone();
		//first day of the month

		for (int j=0; j<6; j++) {
			for (int i=0; i<7; i++) setValueAt( null, j, i );
		}
		
		for( int jour=1; jour<=calendar.getActualMaximum(Calendar.DAY_OF_MONTH); jour++ ) {
		    calendar.set( Calendar.DAY_OF_MONTH, jour );
		    Long millis = new Long(calendar.getTimeInMillis());
		    DayData data = (DayData)m_mapDataCache.get(millis);
		    if( data == null ) {
			    data = new DayData(millis);
			    data.setStandardTime( m_standardTime[data.getDate().get(Calendar.DAY_OF_WEEK)-1] );
			    m_mapDataCache.put( millis, data );
		    }
		    setValueAt( data, calendar.get(Calendar.WEEK_OF_MONTH)-1, calendar.get(Calendar.DAY_OF_WEEK)-1 );
		}
		
    }

    public void bindTable( final JTable table, LookupGain lookup ) {
        table.setModel( this );
        table.setRowHeight( 50 );
//        table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        table.setCellSelectionEnabled( true );
        DayRenderer renderer = new DayRenderer();
        DayEditor editor = new DayEditor( lookup, this );
        
        TableColumnModel tcm = table.getColumnModel();
        for( int col = 0; col < tcm.getColumnCount(); col++ ) {
            TableColumn column = tcm.getColumn(col);
            column.setCellRenderer( renderer );
            column.setCellEditor( editor );
        }
    }
    
    public void reset() {
        m_employeeIdCache = -1;
        m_mapDataCache.clear();
        m_dateCache.clear();
    }
    
    public void loadData( int employeeId, Calendar date ) {
        // On trim le calendrier à AAAAMMJJ
        Calendar cal = new GregorianCalendar( date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH) );
        Long tick = new Long(cal.getTimeInMillis());
        // Employé différent, on flush la cache...
        if( employeeId != m_employeeIdCache ) {
            m_mapDataCache.clear();
            m_dateCache.clear();
            m_employeeIdCache = employeeId;
        }

		loadStandardTime( cal );
        
        setupMois( cal );

        if( m_dateCache.contains(tick) ) return;

        m_dateCache.add(tick);
        
		int calendarID = P_Employee_Calendar.getEmployeeCalendarId( m_employeeIdCache, date.get(Calendar.YEAR));
		cal.set( Calendar.DAY_OF_MONTH, 1 );
		Timestamp tsDebut = new Timestamp( cal.getTimeInMillis() );
		cal.add( Calendar.MONTH, 1 );
		cal.add( Calendar.DAY_OF_MONTH, -1 );
		Timestamp tsFin = new Timestamp( cal.getTimeInMillis() );
		
		String sql = 
		    "SELECT E.P_EMPLOYEE_NONBUSINESSDAY_ID, E.NONBUSINESSDATE, E.P_GAIN_ID, E.DAYQTY, " +
		    	    "CASE WHEN EXISTS( SELECT 1 FROM P_Absence_Detail ad WHERE SOURCE = 'C' AND ad.SOURCE_ID = E.P_EMPLOYEE_NONBUSINESSDAY_ID ) THEN 1 ELSE 0 END AUTH " +
			"FROM P_EMPLOYEE_NONBUSINESSDAY E " +
			"WHERE P_EMPLOYEE_CALENDAR_ID = ? " +
			"AND NONBUSINESSDATE BETWEEN ? AND ? ";
		
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt( 1, calendarID );
			pstmt.setTimestamp( 2, tsDebut );
			pstmt.setTimestamp( 3, tsFin );
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
			    Long millis = new Long(rs.getTimestamp("NONBUSINESSDATE").getTime());
			    DayData data = (DayData)m_mapDataCache.get( millis );

			    BigDecimal val = rs.getBigDecimal("DAYQTY"); 
			    data.add( rs.getInt("P_EMPLOYEE_NONBUSINESSDAY_ID"),
					      val != null ? val.setScale(2) : null,
					      rs.getInt("P_GAIN_ID"),
					      rs.getInt("AUTH") == 1);
			}
			rs.close();
			pstmt.close();

			pstmt = DB.prepareStatement(
			        "SELECT NONBUSINESSDATE FROM P_NonBusinessDay " +
			        "WHERE P_Holidays_Year_ID IN (SELECT P_Holidays_Year_ID " +
			        							 "FROM P_Employee e, P_Holidays_Year hy " +
			        						 	 "WHERE e.P_Employee_ID = ? " +
			        							 "AND e.P_Holidays_Calendar_ID = hy.P_Holidays_Calendar_ID ) " +
					"AND NonBusinessDate BETWEEN ? AND ? " , null);

			pstmt.setInt( 1, employeeId );
			pstmt.setTimestamp( 2, tsDebut );
			pstmt.setTimestamp( 3, tsFin );
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
			    Long millis = new Long(rs.getTimestamp("NONBUSINESSDATE").getTime());
			    DayData data = (DayData)m_mapDataCache.get( millis );
			    data.setFlagFerie( true );
			}
			rs.close();
			pstmt.close();
			
			
			fireTableDataChanged();
		} 
		catch (Exception e) {
		    System.out.println( e.toString() );
		    e.printStackTrace();
		    throw new RuntimeException( e );
//			s_log.log(Level.SEVERE, "PCalendar.load", e);
		}

    }
    
    private void loadStandardTime( Calendar cal ) {
        PreparedStatement statm = null;
        ResultSet rs = null;
        try {
            statm = DB.prepareStatement(
                "SELECT Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday " + 
                "FROM P_Standard_Time " +
                "WHERE P_Gain_ID = (SELECT P_Gain_ID FROM P_Gain WHERE Value = '100') " +
                "AND P_Employee_ID = ? " +
                "AND ? BETWEEN StartDate AND IsNull(EndDate,?) " , null);
            statm.setInt( 1, m_employeeIdCache );
            Timestamp ts = new Timestamp( cal.getTimeInMillis() );
            statm.setTimestamp( 2, ts );
            statm.setTimestamp( 3, ts );

        	P_Employee Employee = P_Employee.get( Env.getCtx(), m_employeeIdCache , trxName);
        	P_Assignment_Param Assignment_Param = Employee.getAssignment_Param(ts, trxName); 
        	if ( Assignment_Param == null)
        		return;
        	
            rs = statm.executeQuery();
            
            if( rs.next() ) {
                m_standardTime = new BigDecimal[7];
                for( int i=0; i<7; i++ )
                {
                	m_standardTime[i] = rs.getBigDecimal(i+1);
                	// 2008.08.08 
                	if ( m_standardTime[i].compareTo(Env.ZERO) == 0 )
                	{
                		m_standardTime[i] = Assignment_Param.getDay_Hours();
                	}
                	
                }
            }
            else {
                BigDecimal val = BigDecimal.valueOf(0, 2);

            	// 2008.08.08 
                BigDecimal val2 = Assignment_Param.getDay_Hours();

                m_standardTime = new BigDecimal[] { val,val2,val2,val2,val2,val2,val };
            }
        }
        catch( Exception e ) {
		    System.out.println( e.toString() );
		    e.printStackTrace();
		    throw new RuntimeException( e );
        }
        finally {
//            DB.DB_CLOSE( rs );
//            DB.DB_CLOSE( statm );
        }
    }
    
    public void save() {
        String trxName = null;
        int prevYear = -1;
        int calId = -1;
        for( Iterator iter = m_mapDataCache.values().iterator(); iter.hasNext(); ) {
            DayData data = (DayData)iter.next();
            if( data.isDirty() ) {
	            for( Iterator iterP = data.getListDeleted().iterator(); iterP.hasNext(); ) {
	                Integer id = (Integer)iterP.next(); 
	                P_Employee_NonBusinessDay empNBD = new P_Employee_NonBusinessDay(Env.getCtx(), id.intValue(), trxName);
	                empNBD.delete(true);
	            }
	            data.flushListDeleted();
	            
				int year = data.getDate().get(Calendar.YEAR);
				if( prevYear != year ) {
				    prevYear = year;
				    calId = P_Employee_Calendar.getEmployeeCalendarId(m_employeeIdCache, year);
				    if( calId == -1) {
						P_Employee_Calendar empCal = new P_Employee_Calendar(Env.getCtx(), -1, trxName); 
						empCal.setP_Employee_ID(m_employeeIdCache);
						empCal.setYear(String.valueOf(year));
						empCal.save();
					    calId = P_Employee_Calendar.getEmployeeCalendarId(m_employeeIdCache, year);
					}
				}
				for( Iterator iterP = data.getListData().iterator(); iterP.hasNext(); ) {
				    DayData.Payload payload = (DayData.Payload)iterP.next();
				    
					P_Employee_NonBusinessDay empNBD = new P_Employee_NonBusinessDay(Env.getCtx(), payload.getId(), trxName);
					empNBD.setNonBusinessDate(new Timestamp(data.getDate().getTimeInMillis()));
					empNBD.setP_Employee_Calendar_ID(calId);
					empNBD.setP_Gain_ID(payload.getGainId());
					empNBD.setDayQty(payload.getQte());
					empNBD.save();
				}
            }
        }
    }
    
    /*
     * Appliquer le gain pour la plage de temps spécifiée aux jours spécifiées
     */
    public void genererCalendrier( int gainId, Timestamp dateDebut, Timestamp dateFin, 
            boolean lundi, boolean mardi, boolean mercredi, boolean jeudi, 
            boolean vendredi, boolean samedi, boolean dimanche,
            BigDecimal qte, boolean remove ) {
        Calendar debut = Calendar.getInstance();
        debut.setTimeInMillis( dateDebut.getTime() );
        Calendar fin = Calendar.getInstance();
        fin.setTimeInMillis( dateFin.getTime() );
        
        if( debut.after(fin) ) return;
        
        // On trim le calendrier à AAAAMMJJ
        Calendar cal = new GregorianCalendar( debut.get(Calendar.YEAR), debut.get(Calendar.MONTH), debut.get(Calendar.DAY_OF_MONTH) );
        Long tick = new Long(cal.getTimeInMillis());

        if( m_dateCache.contains(tick) == false ) {
            loadStandardTime( cal );
            m_dateCache.add(tick);
        }

        //on ajoute 1 a la date de fin parce que le before inclus pas la date de fin
        fin.add(Calendar.DAY_OF_MONTH, 1);
        while( debut.before(fin) ) {
            boolean setDate = false;
            switch( debut.get(Calendar.DAY_OF_WEEK) ) {
            	case Calendar.MONDAY: setDate = lundi; break;
            	case Calendar.TUESDAY: setDate = mardi; break;
            	case Calendar.WEDNESDAY: setDate = mercredi; break;
            	case Calendar.THURSDAY: setDate = jeudi; break;
            	case Calendar.FRIDAY: setDate = vendredi; break;
            	case Calendar.SATURDAY: setDate = samedi; break;
            	case Calendar.SUNDAY: setDate = dimanche; break;
            }
            if( setDate ) {
                BigDecimal val = qte != null ? qte : m_standardTime[debut.get(Calendar.DAY_OF_WEEK)-1];
			    DayData data = (DayData)m_mapDataCache.get( new Long(debut.getTimeInMillis()) );
			    if( remove == false ) { 
			    	if( data == null ) {
			    		data = new DayData(new Long(debut.getTimeInMillis()));
			    		data.setStandardTime( m_standardTime[debut.get(Calendar.DAY_OF_WEEK)-1] );
			    		m_mapDataCache.put(new Long(debut.getTimeInMillis()), data);
			    	}
		    		data.setQte( val, gainId );
			    }	
			    else if( data != null ) data.remove( val, gainId, false );
            }
		    debut.add( Calendar.DAY_OF_MONTH, 1 );
        }

		fireTableDataChanged();
        m_main.cmd_save(false);
    }


}
