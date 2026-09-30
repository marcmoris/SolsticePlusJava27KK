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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Vector;

/** 
 * Model avec édition et totaux
 */
public class DETableModel extends DEAbstractTableModel {
	private Vector<Object> m_data;
	private Vector<Integer> m_ids;
	private boolean m_editable;
	
	public DETableModel( Vector data, String[] columnNames, boolean editable ) {
		super(columnNames);
		m_data = data;
		m_editable = editable;
	}

	public void setData( Vector data ) {
	    m_data = data;
	    fireTableDataChanged();
	}
	
	// Permettre l'affichage des unités à coté de la valeur
	public boolean hasUnits( int col ) { return false; }
	public String getUnits( int row, int col ) { return null; }
	
    // Identifier la ligne d'ajout de données (si active)
    protected int indexNewRow() { return m_editable == true ? m_data.size() : -1; }
    
    // Identifier la ligne de total (si active)
    private int indexTotalRow() {
    	if( isSummable() ) return m_editable == true ? m_data.size() + 1 : m_data.size();
    	return -1;
    }
    
	// Identifier une cellule de total
	public boolean isTotal( int row, int col ) { return isSummable(col) && indexTotalRow() == row; }
	
	// Identifier les lignes contenant des "vraies" données (par opposition à la ligne de total et d'ajout)
	public boolean isDataRow( int row ) { return row != indexNewRow() && row != indexTotalRow(); }
	
	// Identifier les lignes pouvant être supprimées
	public boolean isRowDeletable( int row ) { return row != indexNewRow() && row != indexTotalRow(); }
	
	// Identifier les lignes supprimées
	public boolean isRowDeleted( int row ) {
		int deleteCol = indexDeleteColumn();
		if( deleteCol != -1 ) return Boolean.TRUE.equals(getValueAt(row, deleteCol)); 
		return false; 
	}
	
	// Identifier les cellules spéciales (y'en a pas)
	public boolean isSpecial( int row, int col ) { return false; }
	
    public int getRowCount() {
    	int compte = m_data.size();
    	if( m_editable == true ) compte++;
    	if( isSummable() ) compte++;
    	return compte;
    }

	public boolean isCellEditable(int row, int col) { 
		if( m_editable ) {
			if( indexTotalRow() == row ) return false;
			if( indexNewRow() == row && indexDeleteColumn() == col ) return false;
			return true;
		}
		return false;
	}
	
	public Object getValueAt(int row, int col) {
		// La ligne d'ajout est toujours vide
		if( indexNewRow() == row ) {
			Class classe = getColumnClass(col); 
			if( classe.equals(BigDecimal.class) ) return (BigDecimal)null;
			else if( classe.equals(Boolean.class) ) return (Boolean)null;
			else return (String)null;
		}
		
		// Calculer le total au besoin
		if( indexTotalRow() == row ) {
			if( isSummable(col) ) { 
				BigDecimal total = new BigDecimal(0);
				for( int i=0; i<m_data.size(); i++ ) {
					Object val = ((Object[])m_data.get(i))[col];
					if( val != null && val instanceof BigDecimal && isRowDeleted(i) == false ) 
						total = total.add( (BigDecimal)val );
				}
				return total;
			}
			return null;
		}
		
		// Données ordinaires
		Object[] data = (Object[])m_data.get(row);
		return data != null && col < data.length ? data[col] : null;
	}
	
	public Object[] newRow(int row, int col) {
		return new Object[getColumnCount()];
	}
	
	public void setValueAt( Object val, int row, int col ) { 
		// Une modification sur la ligne d'ajout entraine la création d'une nouvelle ligne d'ajout
		if( indexNewRow() == row ) m_data.add(newRow( row,  col));
		
		// M.M ajout 2007.11.08
		// seulement affecter par la colonne dans l'onglet summary.
		if ( this.getColumnName(col).equals(""))
			((Object[])m_data.get(row))[col] = val;
		if (col != 0)
			((Object[])m_data.get(row))[col] = val;

		// pour la feuille de temps particulière
		if ( col == 0 & this.getColumnName(col).equals("Période") )
			((Object[])m_data.get(row))[col] = val;

		//2008-08-11
		//Calendrier Employé
		if ( col == 0 & this.getColumnName(col).trim().equals("Date") )
			((Object[])m_data.get(row))[col] = val;
		if ( col == 0 & this.getColumnName(col).trim().equals("GainCode") )
			((Object[])m_data.get(row))[col] = val;
		if ( col == 0 & this.getColumnName(col).trim().equals("Qty") )
			((Object[])m_data.get(row))[col] = val;
		//---

		// Déclancher le refresh du total
		if( isSummable() ) fireTableCellUpdated(indexTotalRow(), col);
		
		// Déclancher le refresh de la ligne supprimée et du total
		if( col == indexDeleteColumn() ) {
			fireTableDataChanged();
			fireTableRowsUpdated(row, row);
			int totalRow = indexTotalRow();
			if( totalRow != -1 ) fireTableRowsUpdated( totalRow, totalRow );
		}
		
		fireTableCellUpdated( row, col );
	}
	
    public int[] getColumnWidths() { return null; }

    private int m_lastSortedCol = -1;
    private boolean m_sortOrder = true;
    public void sort( final int col ) {
        if( m_lastSortedCol == col ) { m_sortOrder = !m_sortOrder; }
        else m_sortOrder = true;
        m_lastSortedCol = col;

        if( getColumnClass(col) == BigDecimal.class ) {
	        Collections.sort( m_data, new Comparator() {
	            public int compare(Object arg0, Object arg1) {
	                BigDecimal val1 = (BigDecimal)((Object[])arg0)[col];
	                BigDecimal val2 = (BigDecimal)((Object[])arg1)[col];
	                if( val1 == null || val2 == null ) return 0;
	                int order = val1.compareTo(val2); 
	                if( m_sortOrder == false ) order = -1*order;
	                return order; 
	            }
	        });
        }
        else if( getColumnClass(col) == Date.class ) {
	        Collections.sort( m_data, new Comparator() {
	            public int compare(Object arg0, Object arg1) {
	                Date val1 = (Date)((Object[])arg0)[col];
	                Date val2 = (Date)((Object[])arg1)[col];
	                if( val1 == null || val2 == null ) return 0;
	                int order = val1.compareTo(val2); 
	                if( m_sortOrder == false ) order = -1*order;
	                return order; 
	            }
	        });
        }
        else {
	        Collections.sort( m_data, new Comparator() {
	            public int compare(Object arg0, Object arg1) {
	                Object val1 = ((Object[])arg0)[col];
	                Object val2 = ((Object[])arg1)[col];
	                if( val1 == null || val2 == null ) return 0;
	                int order = val1.toString().compareTo(val2.toString()); 
	                if( m_sortOrder == false ) order = -1*order;
	                return order; 
	            }
	        });
        }
        
        fireTableDataChanged();
    }
    public void resetSort( int col ) {
        m_lastSortedCol = -1;
        m_sortOrder = true;
        sort( col );
    }
    
    public void clear() {
        m_data.clear();
        fireTableDataChanged();
    }
}
