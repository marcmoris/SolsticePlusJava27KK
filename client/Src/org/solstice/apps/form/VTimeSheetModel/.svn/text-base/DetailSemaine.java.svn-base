/*
 * Created on 23 août 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.solstice.apps.form.VTimeSheetModel;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

import solstice.model.P_Period;
import solstice.model.P_Gain_GainInfo;
/**
 * Abstraction et stockage d'une semaine
 */
public class DetailSemaine {
	String[] m_header = new String[DetailLigne.COLUMN_NAMES.length];
	ArrayList<DetailLigne> m_data = new ArrayList<DetailLigne>();
	DetailLigne m_newLine;
	int m_noSemaine;
	DetailModel m_model;
	boolean m_noNewLine;
	int m_offsetNewLine; 
	int m_offsetTotal; 
	
	public DetailSemaine( int noSemaine, P_Period periodeBase, DetailModel model ) {
	    setNoNewLine( false );
		m_noSemaine = noSemaine;
		m_header[1] = periodeBase.getName() + " #" + noSemaine;
		
		m_newLine = new DetailLigne(model, periodeBase);
		m_model = model;
		Calendar date = Calendar.getInstance();
		date.setTimeInMillis( ((Timestamp)periodeBase.getStartDate()).getTime() );
        //on ajoute 7 jour par semaine (0 pour la premiere, 7 pour la deuxieme...)
		date.add(Calendar.DAY_OF_YEAR, (noSemaine - 1) * 7);
       	
       	//on mets a jour les valeurs de la ligne d'entete
       	for(int idx=0;idx<7;idx++)
       	{
	       	String strDate = "";
	       	String strMonth = String.valueOf(date.get(Calendar.MONTH)+1);
	       	if(strMonth.length() == 1) strMonth = "0"+strMonth;
	       	String strDay = String.valueOf(date.get(Calendar.DAY_OF_MONTH));
	       	if(strDay.length() == 1) strDay = "0"+strDay;
	       	strDate = strMonth + " - " + strDay;
       		m_header[model.recalcJours(DetailLigne.IDX_SEMAINE+idx)] = strDate;
       		
       		Timestamp d = new Timestamp( date.getTimeInMillis());
       		if ( d.compareTo( periodeBase.getEndDate()) > 0 )
           		m_header[model.recalcJours(DetailLigne.IDX_SEMAINE+idx)] = "";
       		
   			date.add(Calendar.DAY_OF_YEAR, 1);
       	}
	}
	
	public int getNoSemaine() { return m_noSemaine; }
	
	// Identifier une ligne de données par opposition aux lignes d'entête/total/ajout
	public boolean isDataRow( int row ) { 
	    return row > 0 && row < m_data.size()+( m_noNewLine == false ? m_offsetNewLine : m_offsetTotal);
    }
	
	// Identifier la ligne d'ajout
	public boolean isNewRow( int row ) { return m_noNewLine == false && ((DetailLigne)m_data.get(row-1)).isNewRow(); }
	
	public int getRowCount() { return m_data.size()+(m_noNewLine ? 2 : 3); }
	
	// Retourne la ligne de données correspondant à la row
	public DetailLigne getLigne( int row ) {
	    if( row == 0 || row > m_data.size()+( m_noNewLine == false ? m_offsetNewLine : m_offsetTotal) ) return null;
	    if( row == m_data.size()+( m_noNewLine == false ? m_offsetNewLine : m_offsetTotal) ) return m_newLine;
        return (DetailLigne)m_data.get(row-1);
	}

	public Object getValue( int row, int col) {
		// Ligne d'entête
		if( row == 0 ) return m_header[col];
		
		// Ligne d'ajout
		if( m_noNewLine == false && row == m_data.size()+m_offsetNewLine ) return m_newLine.getColumn(col);
		
		// Ligne de total avec calcul du total
		if( row == m_data.size()+m_offsetTotal ) {
    		if( DetailLigne.isSummable(col) ) {
	    		BigDecimal total = new BigDecimal(0);
	    		for( Iterator iter = m_data.iterator(); iter.hasNext(); ) {
	    			DetailLigne data = (DetailLigne)iter.next();
	    			if( Boolean.TRUE.equals(data.getDeleteRow()) == false ) {
	    				BigDecimal val = (BigDecimal)data.getColumn(col);
	    			    ModelLookup m_lookup = new ModelLookup();
	    		        int gainID = m_lookup.lookupGain( data.getGain() );
	    				P_Gain_GainInfo GainInfo = P_Gain_GainInfo.get( Env.getCtx(), gainID, 1000014); // 1000014 = GainInfo STD.
	    				if ( GainInfo.isTo_Consider() )
	    					if( val != null ) total = total.add(val);
	    			}
	    		}
	    		return total;
    		}
    		return null;
		}
		
		DetailLigne ligne = (DetailLigne)m_data.get(row-1);
		
		if( col == DetailLigne.COL_TOTAL ) {
    		BigDecimal total = new BigDecimal(0);
			for( int i=DetailLigne.IDX_SEMAINE; i<DetailLigne.IDX_SEMAINE+7; i++ ) {
				BigDecimal val = (BigDecimal)ligne.getColumn(i);
				if( val != null ) total = total.add(val);
    		}
    		return total;
		}
		
		// Ligne de données ordinaire
		return ligne.getColumn(col); 
	}
	
	public void setValue( Object val, int row, int col ) {
		// On touche pas au ligne d'entête/total
		if( row == 0 || row == m_data.size()+m_offsetTotal ) return;
		
		// Ajouter une nouvelle ligne lorsqu'on modifie la ligne d'ajout
		if( m_noNewLine == false && row == m_data.size()+m_offsetNewLine ) {
			DetailLigne newLine = new DetailLigne(m_model, (KeyNamePair)m_newLine.getPeriod());
			newLine.tagNewRow();
			newLine.setColumn(col, val);
			newLine.setWeek( m_noSemaine );
			m_data.add(newLine);
		}
		// Modifier une ligne ordinaire
		else ((DetailLigne)m_data.get(row-1)).setColumn(col, val);
	}
	
	// Identifier les cellules modifiables
	public boolean isEditable( int row, int col ) { 
		if( row == 0 || row == m_data.size()+m_offsetTotal ) return false;
		if( col == DetailLigne.COL_DELETE_ROW && row == m_data.size()+m_offsetNewLine && m_noNewLine == false ) return false;
		if( col == DetailLigne.COL_POSTE ) return false;
		if( col == DetailLigne.COL_TOTAL ) return false;
		DetailLigne ligne = getLigne(row);
		if( ligne != null ) return ligne.isEditable(col);
		return false;
	}

	// Identifier les cellules d'entête
	public boolean isEntete( int row, int col ) { return row == 0; }
	
	// Identifier les cellules de total
	public boolean isTotal( int row, int col ) { return row == m_data.size()+m_offsetTotal; }
	
	// Ajouter une ligne de données
	public void add( DetailLigne ligne ) { m_data.add(ligne); }
	
	// Extraire les lignes de période antécédante
	public List extraitAntecedant( String periode ) {
	    ArrayList<DetailLigne> liste = null;
	    for( Iterator iter = m_data.iterator(); iter.hasNext(); ) {
	        DetailLigne ligne = (DetailLigne)iter.next();
	        if( ligne.getPeriod() != null && ligne.getPeriod().toString().equals(periode) == false ) {
	            if( liste == null ) liste = new ArrayList<DetailLigne>();
	            liste.add(ligne);
	            iter.remove();
	        }
	    }
	    return liste;
	}
	
	// Flag les semaines sans ligne d'ajout
	public void setNoNewLine( boolean noNewLine ) { 
	    m_noNewLine = noNewLine;
	    if( m_noNewLine ) {
	        m_offsetNewLine = 0;
	        m_offsetTotal = 1;
	    }
	    else {

	    	m_offsetNewLine = 1;
	        m_offsetTotal = 2;
	    }
	}
}


