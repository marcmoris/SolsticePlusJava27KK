/*
 * Created on 26 août 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.solstice.apps.form.VTimeSheetModel;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.util.Date;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.compiere.swing.CCheckBox;
//import org.compiere.swing.CComboBox;
import org.compiere.swing.CComboBoxTS;

import org.compiere.swing.CTextField;
import org.solstice.apps.form.VTimeSheetModel.Lookup.AbstractKNPModel;



public class DETableEditor extends DefaultCellEditor {
	CCheckBox m_checkBox;
	CComboBoxTS m_comboBox;
	boolean m_comboBoxMode = true;
	
	class VKEnterNextLine implements KeyListener {
        public void keyPressed(KeyEvent e) {
            if( e.getKeyCode() == KeyEvent.VK_ENTER ) {
                int row = m_currentTable.getSelectedRow();
                if( row+1 < m_currentTable.getRowCount() ) {
	                stopCellEditing();
	                int col = 0;
	                for( int i=0; i<m_currentTable.getColumnCount(); i++ ) {
	                    if( m_currentTable.isCellEditable(row+1, i) ) { col = i; break; }
	                }
	                m_currentTable.changeSelection( row+1, col, false, false );
                }
            }
        }
        public void keyReleased(KeyEvent e) {}
        public void keyTyped(KeyEvent e) {}
	}
	
	public DETableEditor() {
		super( new CTextField() );
		
		setClickCountToStart(1);
		m_checkBox = new CCheckBox();
		//m_checkBox.setBorder( BorderFactory.createLineBorder(Color.GRAY) );
		m_checkBox.setBackground( Color.LIGHT_GRAY );
		m_checkBox.setOpaque(true);
		m_checkBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent ev) { stopCellEditing(); }
		} );

		m_comboBox = new CComboBoxTS();
    	m_comboBox.setBorder(null);

        ((JComponent) m_comboBox.getEditor().getEditorComponent()).setBorder(null);
        
		VKEnterNextLine keyListener = new VKEnterNextLine();
		
		m_comboBox.addKeyListener( keyListener );
		m_checkBox.addKeyListener( keyListener );
        getComponent().addKeyListener( keyListener );
	}

	private Class m_dataType;
	private JTable m_currentTable;
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		m_dataType = table.getColumnClass(column);
		DEAbstractTableModel model = (DEAbstractTableModel)table.getModel();
		m_currentTable = table;
		m_comboBoxMode = false;
		
		// Deal avec les checkbox
		if( Boolean.class.equals(m_dataType) ) {
			if( model.isDataRow(row) ) {
				boolean etat = false;
				if( value != null ) {
					if( value instanceof String ) etat = Boolean.valueOf((String)value).booleanValue();
					else if( value instanceof Boolean ) etat = ((Boolean)value).booleanValue();
				}
			 	m_checkBox.setSelected( etat );
				m_checkBox.setHorizontalAlignment( ((DEAbstractTableModel)table.getModel()).getAlignment(column) );
			 	return m_checkBox;
			}
		}
		else {
			AbstractKNPModel comboModel = (AbstractKNPModel)model.getComboData(row, column);

			// Deal avec les combobox
			if( comboModel != null ) {
				m_comboBoxMode = true;
				m_comboBox.setModel( comboModel );
				if( value != null ) m_comboBox.setSelectedItem(value);
				else {
				    Object item = comboModel.getSelectedItem();
				    if( item == null ) item = comboModel.getDefaultValue();
				    if( item != null ) m_comboBox.setSelectedItem( item );
				    else if( m_comboBox.getItemCount() > 0 ) m_comboBox.setSelectedIndex( 0 );
				}
				return m_comboBox;
			}
			// Deal avec les champs texte
			else {
			    if( model.getColumnClass(column).equals(Date.class) && value instanceof Date ) {
				    value = new SimpleDateFormat("yyyy-MM-dd").format(value);
				}
			    JTextField field = (JTextField)super.getTableCellEditorComponent(table, value, true, row, column);
				field.setHorizontalAlignment( ((DEAbstractTableModel)table.getModel()).getAlignment(column) );
				field.setBorder( BorderFactory.createLineBorder(Color.black) );
				field.setCaretPosition(0);
				field.moveCaretPosition(field.getText().length());
				return field;
			}
		}
		return null;
	}
	
	public Object getCellEditorValue() {
		if( m_comboBoxMode ) return m_comboBox.getSelectedItem();

		if( Boolean.class.equals(m_dataType) ) return new Boolean(m_checkBox.isSelected());

		String val = (String)super.getCellEditorValue();
		try {
			if( BigDecimal.class.equals(m_dataType) ) {
			    if( val == null || "".equals(val) ) return (BigDecimal)null;
			    return new BigDecimal(val);
			}
			if( Date.class.equals(m_dataType) ) {
			    if( val == null || "".equals(val) ) return (Date)null;
			    return ((DEAbstractTableModel)m_currentTable.getModel()).munchDate(m_currentTable.getEditingRow(), m_currentTable.getEditingColumn(), val);
			}
		}
		catch( Exception e ) { /* Ssshhhh */ }
		return val;
	}
	
	public boolean stopCellEditing() {
		if( m_comboBoxMode ) {
	        fireEditingStopped();
	        return true;
		}

		String val = (String)super.getCellEditorValue();
	    DEAbstractTableModel model = (DEAbstractTableModel)m_currentTable.getModel();
	    boolean failed = false;
		if( val != null && "".equals(val) == false ) {
			try {
				if( BigDecimal.class.equals(m_dataType) ) new BigDecimal(val);
				if( Date.class.equals(m_dataType) ) {
				    if( model.munchDate(m_currentTable.getEditingRow(), m_currentTable.getEditingColumn(), val) == null ) failed = true;
				}
			}
			catch( Exception e ) {
			    failed = true;
			}
		}
	    else if( model.isRequired(m_currentTable.getEditingRow(), m_currentTable.getEditingColumn()) ) failed = true; 
		if( failed ) {
			JTextField field = (JTextField)getComponent();
			field.setBorder( BorderFactory.createLineBorder(Color.red));
			field.setCaretPosition(0);
			field.moveCaretPosition(field.getText().length());
			return false;
		}
		fireEditingStopped();
		return true;
	}				
	
}
