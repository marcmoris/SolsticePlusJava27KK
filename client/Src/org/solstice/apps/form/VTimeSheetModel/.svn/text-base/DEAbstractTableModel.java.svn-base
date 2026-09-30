/*
 * Created on 26 août 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.solstice.apps.form.VTimeSheetModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.table.AbstractTableModel;

import org.solstice.apps.form.VTimeSheetModel.Lookup.AbstractKNPModel;


/**
 * @author jeacat01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class DEAbstractTableModel extends AbstractTableModel {

	private String[] m_columnNames;
	private boolean[] m_summable = null;
	private int[] m_alignment;
	private AbstractKNPModel[] m_comboData; 
	
	public DEAbstractTableModel( String[] columnNames ) {
		m_columnNames = columnNames;
		
		m_alignment = new int[m_columnNames.length];
		for( int i=0; i<m_alignment.length; i++ ) m_alignment[i] = JLabel.LEFT;
		m_comboData = new AbstractKNPModel[m_columnNames.length]; 
	}

	// Permettre l'affichage des unités à coté de la valeur
	public boolean hasUnits( int col ) { return false; }
	public String getUnits( int row, int col ) { return null; }
	
	// Identifier les colonnes avec des totaux
	public void tagSummable( int[] cols ) {
		m_summable = new boolean[m_columnNames.length];
		for( int i=0; i<cols.length; i++ ) m_summable[cols[i]] = true;
	}
	public boolean isSummable() { return m_summable != null; }
	
	public void setAlignment( int[] cols, int alignment ) {
		for( int i=0; i<cols.length; i++ ) m_alignment[cols[i]] = alignment;
	}
	public int getAlignment( int col ) { return m_alignment[col]; }
	
	public void setComboData( int col, AbstractKNPModel data ) { m_comboData[col] = data; }
	public AbstractKNPModel getComboData( int row, int col ) { return m_comboData[col]; }
	
	// Identifier une colonne avec total
	public boolean isSummable( int col ) { return m_summable != null && m_summable[col]; }
	
	// Identifier une cellule de total
	public abstract boolean isTotal( int row, int col );
	
	// Identifier les lignes contenant des "vraies" données (par opposition à la ligne de total et d'ajout)
	public abstract boolean isDataRow( int row );
	
	// Identifier les lignes pouvant être supprimées
	public abstract boolean isRowDeletable( int row );
	
	// Identifier les lignes supprimées
	public abstract boolean isRowDeleted( int row );

	// Identifier les cellules obligatoires
	public boolean isRequired( int row, int col ) { return false; }

	// Les largeurs de colonnes
    public abstract int[] getColumnWidths();
    
	// Identifier les cellules spéciales
	public abstract boolean isSpecial( int row, int col );
	
    public String getColumnName(int col) 
    {
    	if ( col > m_columnNames.length)
    		return "";
    	return m_columnNames[col]; 
    }

    public int getColumnCount() { return m_columnNames.length; }

    public Date munchDate( int row, int column, String date ) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(date);
    }
    
    // Identifier la colonne pour le checkbox de suppression
	public int indexDeleteColumn() { return -1; }
    
	// Edition automatique lorsque sélectionn.
	public boolean isFastEdit( int row, int col ) { return true; }
	
	public void sort( int col ) {}
	
	public void clear() {}
}
