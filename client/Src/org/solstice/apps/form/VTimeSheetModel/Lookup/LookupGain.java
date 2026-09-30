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
package org.solstice.apps.form.VTimeSheetModel.Lookup;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;

import org.compiere.model.MRole;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

import solstice.model.P_Gain;
import solstice.model.P_Method_Gain;

/**
 * @author jeacat01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LookupGain extends AbstractKNPModel {
	private HashSet<Object> m_lookup_gainMonetaire;
	private HashSet<Object> m_lookup_gainSansValeur;
	private HashMap<KeyNamePair, String> m_lookup_unite;
	private HashSet<Object> m_lookup_HourlyRateSansValeur;
	private HashSet<Object> m_lookup_quantitySansValeur;
	
	public LookupGain() {
    	m_lookup_gainMonetaire = new HashSet<Object>();
    	m_lookup_gainSansValeur = new HashSet<Object>();
    	m_lookup_unite = new HashMap<KeyNamePair, String>();
    	m_lookup_HourlyRateSansValeur = new HashSet<Object>();
    	m_lookup_quantitySansValeur = new HashSet<Object>();
    	
    	load();
	}
	
	public boolean isMonetaire( Object obj ) { return m_lookup_gainMonetaire.contains(obj); }
    public boolean isSansValeur( Object obj ) { return m_lookup_gainSansValeur.contains(obj); }
    public String getUnite( Object knp ) { return (String)m_lookup_unite.get(knp); }
    public boolean isHourlyRateSansValeur(Object obj) {return m_lookup_HourlyRateSansValeur.contains(obj);}
    public boolean isQuantitySansValeur (Object obj) {return m_lookup_quantitySansValeur.contains(obj);}
    
    public KeyNamePair lookup( int key ) { 
    	KeyNamePair val = super.lookup(key); 
    	if( val == null ) { // Probablement inactif, on va piocher sur la BD
    		P_Gain obj = P_Gain.get(Env.getCtx(), key, null);
    		if( obj != null ) val = new KeyNamePair( obj.get_ID(), "~" + obj.getValue() + " - " + obj.getName() + "~" );
    	}
    	return val;
	}

    
    protected void load() {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql;
        
        if (Env.isBaseLanguage(Env.getCtx(), "AD_Ref_List"))
          sql = 
        	"SELECT P_Gain.P_Gain_ID, P_Gain.Value + '-' + P_Gain.Name, u.Value, P_Gain.P_Method_Gain_ID, u.P_UOM_ID " +
			"FROM P_Gain , P_UOM u " +
			"WHERE u.P_UOM_ID = P_Gain.P_UOM_ID AND P_Gain.IsActive = 'Y' ";
        else
          sql = 
           	"SELECT P_Gain.P_Gain_ID, P_Gain.Value + '-' + trl.Name, utrl.Value, P_Gain.P_Method_Gain_ID, u.P_UOM_ID " +
    		"FROM P_Gain , P_Gain_Trl trl, P_UOM u, P_UOM_TRL utrl  " +
    		"WHERE u.P_UOM_ID = P_Gain.P_UOM_ID " +
    		" AND u.P_UOM_ID = utrl.P_UOM_ID " +
		    " AND utrl.AD_Language = '" + Env.getAD_Language(Env.getCtx()) + "'" + 
    		" AND P_Gain.IsActive = 'Y'" +
        	" AND trl.P_Gain_ID = P_Gain.P_Gain_ID " +
		    " AND trl.AD_Language = '" + Env.getAD_Language(Env.getCtx()) + "' "  ;

		sql = sql + " AND P_Gain." + MRole.getDefault().getClientWhere ( false);	// fully qualidfied - RO 

		sql = sql + " ORDER BY P_Gain.value ";

        try {
        	ArrayList<KeyNamePair> data = new ArrayList<KeyNamePair>();
        	m_lookup_gainMonetaire.clear();
        	m_lookup_gainSansValeur.clear();
        	m_lookup_HourlyRateSansValeur.clear();
        	
        	
            pstmt = DB.prepareStatement(sql, null);
            rs = pstmt.executeQuery();

            while (rs.next()) {
            	P_Method_Gain methodGain = P_Method_Gain.get(Env.getCtx(), rs.getInt(4), null);
            	
            	KeyNamePair knp = new KeyNamePair(rs.getInt(1), rs.getString(2));
            	data.add( knp );
				if( "M".equals(rs.getString(3)) || "Q".equals(rs.getString(3)) )
//				if ( 101 == rs.getInt( "P_UOM_ID"))
				{
					m_lookup_gainMonetaire.add( rs.getString(2) );
					m_lookup_gainMonetaire.add( knp );
				}
				
				//107 == rs.getInt(4) ||
				//TODO ajouté un paramètre dans la table des méthodes.
				
					//if(  107 == rs.getInt(4) || 113 == rs.getInt(4) || 116 == rs.getInt(4) ) // || 118 == rs.getInt(4)
					if (!methodGain.isEditHourlyRate())
					{
						m_lookup_HourlyRateSansValeur.add(rs.getString(2));
						m_lookup_HourlyRateSansValeur.add(knp);	
					}

					// 2022-08-24
					if (!methodGain.isEditQuantity() )
					{
						m_lookup_gainSansValeur.add( rs.getString(2) );
						m_lookup_gainSansValeur.add( knp );
					}
				
				
					// 107 == rs.getInt(4) ||
					/*if(   113 == rs.getInt(4) || 116 == rs.getInt(4) ) // || 118 == rs.getInt(4) 
					{
						m_lookup_gainSansValeur.add( rs.getString(2) );
						m_lookup_gainSansValeur.add( knp );
					}*/
					
				
				m_lookup_unite.put( knp, rs.getString(3) );
            }
            
            setData(data);
            
            rs.close();
			pstmt.close();
			
        }
        catch (Exception e) {
            s_log.log(Level.SEVERE,"VTimeSheetDetail.loadGainCodes", e);
        }
    }

}
