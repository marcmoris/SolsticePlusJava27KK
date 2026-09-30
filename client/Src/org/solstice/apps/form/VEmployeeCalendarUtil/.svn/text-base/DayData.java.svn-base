/*
 * Created on 12 sept. 2005
 *
 */
package org.solstice.apps.form.VEmployeeCalendarUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class DayData {
    private Calendar m_date;
    
    private ArrayList<Payload> m_listData;
    private ArrayList<Integer> m_listDeleted;
    
    private boolean m_flagFerie;
    private String m_title;
    private BigDecimal m_standardTime;
    
    private boolean m_dirty = false;
    public void setDirty( boolean dirty ) { m_dirty = dirty; }
    public boolean isDirty() { return m_dirty; }
    
    public DayData( Long dateInMillis ) {
        m_date = Calendar.getInstance();
        m_date.setTimeInMillis(dateInMillis.longValue());
        m_listData = new ArrayList<Payload>();
        m_listDeleted = new ArrayList<Integer>();
        m_flagFerie = false;
        m_title = String.valueOf(m_date.get(Calendar.DAY_OF_MONTH));
    }

    public class Payload {
        int m_id;
        BigDecimal m_qte;
        int m_gainId;
        boolean m_autorise;
        public Payload( int id, BigDecimal qte, int gainId, boolean autorise ) {
            m_id = id;
            m_qte = qte;
            m_gainId = gainId;
            m_autorise = autorise;
        }
        public int getId() { return m_id; }
        public BigDecimal getQte() { return m_qte; }
        public void setQte( BigDecimal qte ) { m_qte = qte; }
        public int getGainId() { return m_gainId; }
        public void setGainId( int gainId ) { m_gainId = gainId; }
        public boolean getAutorise() { return m_autorise; }
    }
    
    public Calendar getDate() { return m_date; }
    
    public void resetLists() {
        m_listData.clear();
        m_listDeleted.clear();
        setDirty( false );
    }
    
    public void add( int id, BigDecimal qte, int gainId, boolean autorise ) {
        m_listData.add( new Payload(id, qte, gainId, autorise) );
    }
    
    public void remove( int id ) {
        m_listDeleted.add( new Integer(id) );
        setDirty( true );
    }
    
    public void remove( BigDecimal qte, int gainId, boolean autorise ) {
        for( Iterator iter = m_listData.iterator(); iter.hasNext(); ) {
            Payload data = (Payload)iter.next();
            if( data.getQte() != null && qte.compareTo(data.getQte()) == 0 && gainId == data.getGainId() && autorise == data.getAutorise() ) {
                int id = data.getId();
                iter.remove();
                m_listDeleted.add( new Integer(id) );
                setDirty( true );
                break;
            }
        }
    }
    
    public void setStandardTime( BigDecimal standardTime ) { m_standardTime = standardTime; }
    
    public List getListData() { return m_listData; }
    public List getListDeleted() { return m_listDeleted; }
    public void flushListDeleted() { m_listDeleted.clear(); }
    
    public BigDecimal getQteUnique() {
        if( m_listData.size() == 1 ) {
            return ((Payload)m_listData.get(0)).getQte();
        }
        return null;
    }
    
    public int getGainIdUnique() {
        if( m_listData.size() == 1 ) {
            return ((Payload)m_listData.get(0)).getGainId();
        }
        return -1;
    }
    
    public void setQte( BigDecimal qte, int gainId ) {
        if( m_listData.size() == 1 ) {
	        Payload p = (Payload)m_listData.get(0);
	        p.setQte(qte);
	        p.setGainId(gainId);
        }
        else {
            add(-1, qte, gainId, false ); 
        }
        setDirty( true );
    }
    
    public boolean isMultiValue() { return m_listData.size() > 1; }
    public boolean isAuthAbsence() {
        for( Iterator iter=m_listData.iterator(); iter.hasNext(); ) {
            if( ((Payload)iter.next()).getAutorise() ) return true; 
        }
        return false;
    }
    public boolean isEmpty() { return m_listData.size() == 0; }
    
    public void setFlagFerie( boolean flagFerie ) { m_flagFerie = flagFerie; }
    public boolean getFlagFerie() { return m_flagFerie; }
    
    public String getTitle() { return m_title; }
    public BigDecimal getStandardTime() { return m_standardTime; }
}