/*
 * Created on 25 août 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.solstice.apps.form.VTimeSheetModel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CCheckBox;
import org.compiere.swing.CLabel;

/**
 * Renderer pour DETableModel
 */
public class DETableRenderer implements TableCellRenderer {
	private CompiereColor m_backgroundColor = new CompiereColor();
	private DEAbstractTableModel m_model;
	private Font m_normal;
	private Font m_bold;
	private CCheckBox m_checkBox;
	private CLabel m_label;
	
	public DETableRenderer()
	{
		m_checkBox = new CCheckBox();
		m_label = new CLabel();
		m_label.setOpaque(true);
		
		m_normal = m_label.getFont();
		m_bold = new Font( m_normal.getName(), Font.BOLD, m_normal.getSize() );
	} 

	
	// Attacher les morceaux ensemble... model, table et renderer...
	public void bindTable( JTable table, DEAbstractTableModel model ) {
		bindTable( table, model, new DETableEditor() );	
	}
	
	public void bindTable( JTable table, DEAbstractTableModel model, final DETableEditor dte ) {
	    final JTableHeader header = table.getTableHeader();
	    header.setReorderingAllowed( false );
	    header.addMouseListener( new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                dte.stopCellEditing();
                m_model.sort( header.columnAtPoint( e.getPoint() ) );
            }
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
	    } );
		table.setModel(model);
		m_model = model;
		TableColumnModel tcm = table.getColumnModel();
		
		int[] widths = model.getColumnWidths();
		for( int i=0; i<tcm.getColumnCount(); i++ ) {
		    TableColumn tc = tcm.getColumn(i);
		    tc.setCellRenderer(this);
			tc.setCellEditor(dte);
			if( widths != null ) tc.setPreferredWidth(widths[i]);
		}
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		// Active automagiquement l'édition si la colonne est éditable
		if( isSelected && table.getSelectedColumn() == column 
		        && table.hasFocus() 
		        && table.isCellEditable(row, column) 
		        && m_model.isFastEdit(row, column)) {
			table.editCellAt(row, column);
			table.getEditorComponent().requestFocusInWindow();
			return null;
		}
		
		// Check pour la colonne de suppression et faire afficher le checkbox
		if( m_model.indexDeleteColumn() == column ) {
			if( m_model.isRowDeletable(row) ) {
				m_checkBox.setHorizontalAlignment(m_model.getAlignment(column));
				m_checkBox.setSelected( m_model.isRowDeleted(row) );
			 	return m_checkBox;
			}
			if( m_model.isSpecial(row, column) == false ) return null;
		}
		else if( m_model.getColumnClass(column).equals(Boolean.class) ) {
			m_checkBox.setHorizontalAlignment(m_model.getAlignment(column));
			m_checkBox.setSelected( value != null && ((Boolean)value).booleanValue() );
		 	return m_checkBox;
		}
		if( m_model.getColumnClass(column).equals(Date.class) && value != null && value.getClass().isAssignableFrom(Date.class)) {
		    value = new SimpleDateFormat("yyyy-MM-dd").format(value);
		}
		// Données ordinaires
		m_label.setHorizontalAlignment(m_model.getAlignment(column));
		
		// Le total est en gras
		m_label.setFont( m_model.isTotal(row, column) ? m_bold : m_normal );
		
		// Les entêtes en gris
		m_label.setBackground(m_model.isSpecial(row, column) ? m_backgroundColor.getFlatColor() : Color.white );
		
		// Une ligne supprimée est en rouge
		m_label.setForeground( m_model.isRowDeleted(row) ? Color.red : Color.black );
		
		String texte; 
		if( value != null ) {
			texte = String.valueOf(value);
			// Afficher les unités au besoin
			if( m_model.hasUnits(column) ) {
				if( value != null ) {
					String units = m_model.getUnits(row, column);
					if( units != null ) texte = texte + " " + units;
				}
			}
		}
		else texte = "";
		
		m_label.setText(texte);

		// Encadrer les cellules non éditable et sélectionnée
		if( table.hasFocus() && isSelected && column == table.getSelectedColumn() ) {
			m_label.setBorder( BorderFactory.createLineBorder(Color.lightGray) );
		}
		else m_label.setBorder( null );
		
		return m_label;
	}

	

} 