/*
 * Created on 1 sept. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.solstice.apps.form.VTimeSheetModel.Lookup;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import org.compiere.model.MRole;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import solstice.model.P_Assignment;
import solstice.model.P_Post;

/**
 * @author jeacat01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LookupAssignment extends AbstractKNPModel {
    private HashMap<String, String> m_mapPoste;
    private boolean m_defaultBlank;
    
    public LookupAssignment() {
    	this(false);
    }
    
    public LookupAssignment( boolean defaultBlank ) {
    	m_mapPoste = new HashMap<String, String>();
    	m_defaultBlank = defaultBlank;
    }
    
    public String lookupPoste( String assignment ) { 
    	return (String) m_mapPoste.get(assignment); 
	}
    
    public KeyNamePair lookup( int key ) { 
    	KeyNamePair val = super.lookup(key);
    	if( val == null ) { // Probablement inactif, on va piocher sur la BD
    		P_Assignment obj = P_Assignment.get(Env.getCtx(), key, null);
    		if( obj != null ) {
    		    val = new KeyNamePair( obj.get_ID(), "~" + obj.getValue() + " - " + obj.getName() + "~" );
        		P_Post poste = P_Post.get(Env.getCtx(), obj.getP_Post_ID(), null);
        		if( poste != null )
        		    m_mapPoste.put("~" + obj.getValue() + " - " + obj.getName() + "~", poste.getValue() + " - " + poste.getName() );
    		}
    	}
    	return val;
    }
    
    public void load(int employeeID, Timestamp effectIn ) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = 
		    "SELECT -1, '', '', '' UNION " +
		    "SELECT pa.P_Assignment_ID, pa.Value + ' - ' + isnull( pa.Name, ' ' ), pp.Value + ' - ' + pp.Name, ASSIGNMENTTYPE " + 
		    "FROM P_Assignment pa " +
		    " LEFT OUTER JOIN P_Post pp ON pp.P_Post_ID = pa.P_Post_ID " + 
		    "WHERE pa.IsActive = 'Y' AND pa.P_Employee_ID = ? "	
		    ; 

		sql = sql + " AND pa." + MRole.getDefault().getClientWhere ( false);	// fully qualidfied - RO 

		try {
			Object defaultValue = null;
			ArrayList<KeyNamePair> data = new ArrayList<KeyNamePair>();
			m_mapPoste.clear();

			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, employeeID);
//			pstmt.setTimestamp(2, effectIn );
//			pstmt.setTimestamp(3, effectIn );
			rs = pstmt.executeQuery();
			while (rs.next()) {
			    KeyNamePair knp = new KeyNamePair(rs.getInt(1), rs.getString(2));
				data.add(knp);
				if( "P".equals(rs.getString(4)) ) {
				    defaultValue = knp;
				}
				m_mapPoste.put(rs.getString(2), rs.getString(3));
			}
			if( m_defaultBlank == false ) {
				if( defaultValue == null && data.size() > 0 ) {
				    defaultValue = data.get(0);
				}
			}
			else defaultValue = null;
			setData( data );
			setDefaultValue( defaultValue );
			setSelectedItem( null );
			
			rs.close();
			pstmt.close();

		} 
		catch (Exception e) {
			s_log.log(Level.SEVERE, "VTimeSheetDetail.loadAssignments", e);
		}
	}
    
    
    public void loadAll() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = 
		    "SELECT pa.P_Assignment_ID, pa.Value + ' - ' + isnull( pa.Name, ' ' ) , pp.Value + ' - ' + pp.Name " + 
		    "FROM P_Assignment pa, P_Post pp " + 
		    "WHERE pp.P_Post_ID = pa.P_Post_ID " + 
		    "AND pa.IsActive = 'Y'";


//		System.out.println(" Assignment " + sql + " - All" );

		try {
			ArrayList<KeyNamePair> data = new ArrayList<KeyNamePair>();
			m_mapPoste.clear();

			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				data.add(new KeyNamePair(rs.getInt(1), rs.getString(2)));
				m_mapPoste.put(rs.getString(2), rs.getString(3));
			}

			setData( data );
		} 
		catch (Exception e) {
			s_log.log(Level.SEVERE, "VTimeSheetDetail.loadAssignments", e);
		}
	}
}
