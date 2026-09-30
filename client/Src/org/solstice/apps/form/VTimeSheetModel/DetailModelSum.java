/*
 * Created on 19 août 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.solstice.apps.form.VTimeSheetModel;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.TimeUtil;
import org.solstice.apps.form.VTimeSheetModel.Lookup.AbstractKNPModel;

import solstice.model.P_Period;

/**
 * Abstraction et stockage des données de détail d'une feuille de temps
 */
public class DetailModelSum extends DEAbstractTableModel
{
    private TreeMap<String, DetailSemaineSum> m_listeSemaine = new TreeMap<String, DetailSemaineSum>();
    private ModelLookup m_lookup;
    private Calendar m_debutPeriode;
    private P_Period m_periode;
    private int m_premierJour = 0;
    
    public DetailModelSum( ModelLookup lookup, P_Period periode ){
    	super(DetailLigneSum.COLUMN_NAMES);
    	
    	m_periode = periode;
    	
    	m_debutPeriode = new GregorianCalendar();
    	if ( periode != null && periode.getStartDate() != null ) 
    		m_debutPeriode.setTimeInMillis(periode.getStartDate().getTime());
    	else
    		return;

    	m_lookup = lookup;
  //  	m_premierJour = m_debutPeriode.get(Calendar.DAY_OF_WEEK);
    	
    	AbstractKNPModel weeks = m_lookup.getWeekModel();
//    	TODO MOdif 3 lignes
       // for( int i=0; i<weeks.getSize(); i++ ) {
        	KeyNamePair knp = (KeyNamePair)weeks.getElementAt(0);
    		m_listeSemaine.put(String.valueOf(knp.getKey()), new DetailSemaineSum(knp.getKey(), periode, this)); // Semaine de base
       // }
    }
    
    public P_Period getPeriod() { return m_periode; }
    public Calendar getDebutPeriode() { return m_debutPeriode; }
    public ModelLookup getLookup() { return m_lookup; }
    
    // Retourne le poste correspondant à l'assignation pour la sélection automatique lorsque
    // l'assignation est modifiée
    public String getPosteFromAssignment( String assignment ) { 
    	return m_lookup.lookupAssignmentPoste(assignment); 
	}
    
    // Ajoute une ligne à la semaine spécifiée
    public void addLigne( int semaine, DetailLigneSum ligne ) {
    	((DetailSemaineSum)m_listeSemaine.get(String.valueOf(semaine))).add(ligne);
    }
    
    // Recalcule l'index afin que le 1er jour de la période sorte en premier
    // ie: Si la période commence le 20050816, index = 6 retourne la colonne "mardi" plutôt que "dimanche"
    public int recalcJours( int index ) {
    	if( DetailLigneSum.isJour(index) == false ) return index;
    	//int i = index-DetailLigne.IDX_SEMAINE+m_premierJour-1;
		//if( i >= 7 ) i -= 7;
		return DetailLigneSum.IDX_SEMAINE;
    }
    
    public int getPremierJour() { return m_premierJour; }
    
    // Retourne le numéro de la semaine correspondant à la row
    public int getNoSemaine( int row ) { 
    	return findPosition( row ).getSemaine().getNoSemaine(); 
    }

    // Identifier les lignes de données (par opposition aux lignes d'ajout/total/entête)
    public boolean isDataRow(int row) {
    	Position pos = findPosition( row );
    	return pos.getSemaine().isDataRow(row-pos.getOffset());
    }

    // Identifier les lignes d'ajout
    public boolean isNewRow(int row) {
    	Position pos = findPosition( row );
    	return pos.getSemaine().isNewRow(row-pos.getOffset());
    }

    // Retourne la ligne de la semaine correspondant à la row
    public DetailLigneSum getLigne(int row) {
    	Position pos = findPosition( row );
    	return pos.getSemaine().getLigne(row-pos.getOffset());
    }
    
    // Identifier les lignes spéciales (so far: entête)
    public boolean isSpecial( int row, int col ) {
    	Position pos = findPosition( row );
        return pos.getSemaine().isEntete(row-pos.getOffset(), recalcJours(col));
    }
    
    // Identifier les lignes de total
    public boolean isTotal( int row, int col ) {
    	Position pos = findPosition( row );
    	return pos.getSemaine().isTotal(row-pos.getOffset(), recalcJours(col));
    }
    
    // Identifier les lignes supprimables
    public boolean isRowDeletable( int row ) {
    	return isDataRow(row);
    }
    
    // Identifier les lignes supprimées
    public boolean isRowDeleted( int row ) {
    	Position pos = findPosition( row );
    	
		DetailLigneSum ligne = pos.getSemaine().getLigne(row-pos.getOffset());
        if( ligne == null ) return false;
        
        Boolean bool = ligne.getDeleteRow();
        return bool == null ? false : bool.booleanValue();
    }
    
    public String getColumnName(int col) { return super.getColumnName(recalcJours(col)); }
    public int getRowCount() {
    	int compte = 0;
    	for( Iterator iter = m_listeSemaine.values().iterator(); iter.hasNext(); ) {
    		compte += ((DetailSemaineSum)iter.next()).getRowCount();
    	}
    	return compte;
    }
    
    public Class getColumnClass(int columnIndex) {
    	return DetailLigneSum.getColumnClass(columnIndex);
    }

    public Object getValueAt(int row, int col)
    {
    	Position pos = findPosition( row );
    	return pos.getSemaine().getValue(row-pos.getOffset(), recalcJours(col));
    }

    public boolean isCellEditable(int row, int col)
    {
    	Position pos = findPosition( row );
    	return pos.getSemaine().isEditable(row-pos.getOffset(), recalcJours(col));
    }

    public void setValueAt(Object value, int row, int col)
    {
    	Position pos = findPosition( row );

		int offset = pos.getOffset();
		DetailSemaineSum semaine = pos.getSemaine();
		
		semaine.setValue(value, row-offset, recalcJours(col));
		
		// Déclancher le refresh du total
        if( DetailLigneSum.isSummable(recalcJours(col)) ) {
        	fireTableCellUpdated(offset+semaine.getRowCount(), recalcJours(col));
        	//fireTableCellUpdated(offset+semaine.getRowCount(), DetailLigneSum.COL_TOTAL );
        }
        
        // Déclancher le refresh de la table a cause de la suppression de la ligne
        DetailLigneSum ligne = getLigne(row);
        if( ligne != null && ligne.getDeleteRow() != null ) fireTableDataChanged();
        
        fireTableDataChanged();
    }
    
    public void reorgAntecedant() {
        // Scan pour pseudo-semaine
        HashMap<String, DetailSemaineSum> pseudoSemaine = null;
    	for( Iterator iter = m_listeSemaine.values().iterator(); iter.hasNext(); ) {
    		DetailSemaineSum semaine = (DetailSemaineSum)iter.next();
//    		List liste = semaine.extraitAntecedant(m_periode.getName());
    		List liste = semaine.extraitAntecedant(m_periode.getSynonymName());
    		
    		
    		if( liste != null ) {
    		    for( Iterator iterLigne = liste.iterator(); iterLigne.hasNext(); ) {
    		        DetailLigneSum ligne = (DetailLigneSum)iterLigne.next();
	    		    DetailSemaineSum target = (DetailSemaineSum)m_listeSemaine.get("A_"+ligne.getPeriod()+"_"+ligne.getWeek());
	    		    if( target == null ) {
	    		        if( pseudoSemaine != null ) {
	    		            target = (DetailSemaineSum)pseudoSemaine.get( "A_"+ligne.getPeriod()+"_"+ligne.getWeek() );
	    		        }
	    		        if( target == null ) {
		    		        P_Period period = P_Period.get(Env.getCtx(), m_lookup.lookupPeriod(ligne.getPeriod()), null);
		    		        target = new DetailSemaineSum(semaine.getNoSemaine(), period, this);
		    		        if( pseudoSemaine == null ) pseudoSemaine = new HashMap<String, DetailSemaineSum>();
		    		        pseudoSemaine.put( "A_"+ligne.getPeriod()+"_"+ligne.getWeek(), target );
	    		        }
	    		    }
    		        target.add( ligne );
    		    }
    		}
    	}
    	if( pseudoSemaine != null ) m_listeSemaine.putAll( pseudoSemaine );
    }
    
    public void addPeriodeCorrection( KeyNamePair knp ) {
        P_Period period = P_Period.get(Env.getCtx(), m_lookup.lookupPeriodOrigin(knp), null);
        if( m_periode.equals(period) ) return;
	    
//      + new m.m
        int numberOfWeek = new BigDecimal( TimeUtil.getDaysBetween(period.getStartDate(), period.getEndDate()) +1 ) .divide(new BigDecimal(7), 0, BigDecimal.ROUND_UP).intValue();
        m_lookup.setNumberOfWeek(numberOfWeek);
//   - new m.m
//TODO MOdif 3 lignes
    	AbstractKNPModel weeks = m_lookup.getWeekModel();
       // for( int i=0; i<weeks.getSize(); i++ ) {
        	KeyNamePair week = (KeyNamePair)weeks.getElementAt(0);
        	String antKey = "A_" + knp + "_" + week.getKey();
            if( m_listeSemaine.containsKey(antKey) == false ) {
	    		m_listeSemaine.put(antKey, new DetailSemaineSum(week.getKey(), period, this));
            }
        //}
        
        fireTableDataChanged();
        
    }
    
    // Trouve une semaine à partir de la row
    private Position findPosition( int row ) {
    	int position = 0;
    	for( Iterator iter = m_listeSemaine.values().iterator(); iter.hasNext(); ) {
    		DetailSemaineSum semaine = (DetailSemaineSum)iter.next();
    		if( position+semaine.getRowCount() > row ) 
    			return new Position( position, semaine );
    		position += semaine.getRowCount();
    	}
    	
    	// Row inexistante... normal que ça explose...
    	throw new InvalidParameterException( "Row inexistante:" + row );
    }
    
    public boolean isEmpty() {
        for( int i=0; i<getRowCount(); i++ ) {
            if( isDataRow(i) ) return false;
        }
        return true;
    }
    
    // Encapsule la position d'une semaine dans la table
    private class Position {
    	private int m_offset;
    	private DetailSemaineSum m_semaine;
    	public Position( int offset, DetailSemaineSum semaine ) {
    		m_offset = offset;
    		m_semaine = semaine;
    	}
    	public int getOffset() { return m_offset; }
    	public DetailSemaineSum getSemaine() { return m_semaine; }
    }
 
    public int[] getColumnWidths() { return DetailLigneSum.COLUMN_WIDTHS; }

    public int indexDeleteColumn() { return DetailLigneSum.COL_DELETE_ROW; }
}

