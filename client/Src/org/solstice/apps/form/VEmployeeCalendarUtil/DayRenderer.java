/*
 * Created on 12 sept. 2005
 *
 */
package org.solstice.apps.form.VEmployeeCalendarUtil;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class DayRenderer implements TableCellRenderer {
    private DayPanel m_panel;
    
    public DayRenderer() {
        m_panel = new DayPanel();
    }
    
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if( isSelected && table.getSelectedColumn() == column && table.hasFocus() && table.isCellEditable(row, column) ) {
			table.editCellAt(row, column);
			Component editor = table.getEditorComponent();
			if( editor != null ) {
			    editor.requestFocusInWindow();
			}
			return null;
		}
		
        if( value == null ) return m_panel.getVide();
        
        DayData data = (DayData)value;
        m_panel.getText().setEditable( false );
        m_panel.presentData( data );
        return m_panel; 
    }
}
