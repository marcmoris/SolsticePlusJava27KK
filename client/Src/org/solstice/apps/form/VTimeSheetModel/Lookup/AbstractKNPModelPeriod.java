/*
 * Created on 1 sept. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.solstice.apps.form.VTimeSheetModel.Lookup;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.KeyNamePair;

/**
 * @author jeacat01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class AbstractKNPModelPeriod extends AbstractListModel implements ComboBoxModel {
	protected CLogger s_log = CLogger.getCLogger (this.getClass());

	private HashMap m_lookupKey;
	private HashMap m_lookup;
	
	private ArrayList m_data;
	
	private Object m_defaultValue = null;
	public void setDefaultValue( Object obj ) { m_defaultValue = obj; }
	public Object getDefaultValue() { return m_defaultValue; }
	
	private Object m_selection = null;
	public Object getSelectedItem() { return m_selection; }
	public void setSelectedItem(Object anItem) { 
		m_selection = anItem; 
//+ PROGESTION
		int index = m_data.indexOf(anItem);
		fireContentsChanged(this, index, index);
//- PROGESTION		
	}
	
	public Object getElementAt( int index ) { return m_data.get(index); }
	public int getSize() { return m_data.size(); };
	
	public void clear() { m_data.clear(); }
	
	public AbstractKNPModelPeriod() {
		m_data = new ArrayList();
		m_lookupKey = new HashMap();
		m_lookup = new HashMap();
	}
	
	public int lookup( Object obj ) {
    	if( obj == null ) return -1;
    	if( obj instanceof KeyNamePair ) return ((KeyNamePair)obj).getKey();
    	KeyNamePair knp = (KeyNamePair)m_lookup.get(obj);
    	return knp != null ? knp.getKey() : -1;
	}
	
    public KeyNamePair lookup( int key ) {
    	return (KeyNamePair)m_lookupKey.get(new Integer(key));
    }

	public KeyNamePair find( String texte ) {
	    KeyNamePair match = null;
	    for( Iterator iter = m_data.iterator(); iter.hasNext(); ) {
	        KeyNamePair knp = (KeyNamePair)iter.next();
	        if( knp.getName().startsWith(texte) ) {
	            match = knp;
	        }
	        else if( match != null ) break;
	    }
	    return match;
	}
	

	public void setData( ArrayList data ) {
		m_data = data;
		for( int i=0; i<m_data.size(); i++ ) {
			KeyNamePair knp = (KeyNamePair)m_data.get(i);
			m_lookupKey.put( new Integer(knp.getKey()), knp );
			m_lookup.put( knp.getName(), knp );
		}
	}
	
	public void addExceptionData( KeyNamePair knp ) {
	    m_data.add(knp);
		m_lookupKey.put( new Integer(knp.getKey()), knp );
		m_lookup.put( knp.getName(), knp );
	}
	/**	Cache						*/
	private static CCache<String,ArrayList>	s_cache = new CCache<String,ArrayList>("ArrayList", 20);

    protected void genericLoadFromSQL( String sql ) {
    	
    	//2010.01.21 - performance.
		String key = sql;
		ArrayList dataCache = (ArrayList)s_cache.get(key);
		if ( dataCache != null)
		{
			setData(dataCache);
			return;
		}

		PreparedStatement statm = null;
		ResultSet rs = null;
		
		try {
			ArrayList data = new ArrayList();

			statm = DB.prepareStatement(sql, null);
			rs = statm.executeQuery();

			while (rs.next()) {
				data.add(new KeyNamePair(rs.getInt(1), rs.getString(2)));
			}
			
			setData(data);
			
			s_cache.put (key, data);

			rs.close();
			statm.close();

		} 
		catch (Exception e) {
			s_log.log(Level.SEVERE, "AbstractKNPLookup.genericLoadFromSQL", e);
		}
    }
}
