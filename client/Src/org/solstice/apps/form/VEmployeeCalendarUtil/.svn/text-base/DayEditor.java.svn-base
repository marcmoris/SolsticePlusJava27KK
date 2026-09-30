/*
 * Created on 12 sept. 2005
 *
 */
package org.solstice.apps.form.VEmployeeCalendarUtil;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import org.solstice.apps.form.VTimeSheetModel.Lookup.LookupGain;

public class DayEditor extends DefaultCellEditor {
    private DayData m_data;
    private DayPanel m_panel;
    private CalendarModel m_model;
    private LookupGain m_lookup;
    private boolean m_fromBouton = false;
    
    public DayEditor( final LookupGain lookup, CalendarModel model ) {
        super(new JTextField());
        setClickCountToStart( 1 );
    
        m_lookup = lookup;
        m_model = model;

        final DayEditor editor = this;
        m_panel = new DayPanel();
        m_panel.getButton().addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                DayData data = (DayData)getCellEditorValue();
                DayDialog dlg = new DayDialog( m_panel.getButton(), data, lookup );
              //2011.11.16 // show is deprecated			
                dlg.setVisible( true);
//                dlg.show();
                m_fromBouton = true;
                editor.stopCellEditing();
                m_fromBouton = false;
            }
        } );
        
        m_panel.getBtnStdTime().addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                m_panel.getText().setText( String.valueOf(m_data.getStandardTime()) );
            }
        } );
        
        m_panel.getText().addMouseListener( new MouseListener() {
            public void mouseClicked(MouseEvent ev) { /* nyet */ }
            public void mouseEntered(MouseEvent ev) { 
                DayData data = (DayData)getCellEditorValue();
	            if( data.isMultiValue() ) {
	                DayDialog dlg = new DayDialog( m_panel.getButton(), data, lookup );
	                //2011.11.16 // show is deprecated			
	                dlg.setVisible( true);
//	                dlg.show();
	                editor.stopCellEditing();
	            }
            }
            public void mouseExited(MouseEvent ev) { /* nyet */ }
            public void mousePressed(MouseEvent ev) { /* nyet */ }
            public void mouseReleased(MouseEvent ev) { /* nyet */ }
        } );
        
        m_panel.setBorder( BorderFactory.createBevelBorder(BevelBorder.LOWERED) );
        
    }
    
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
	    m_data = null;
        if( value == null ) return null;
        m_data = (DayData)value;
        
        int gainId = m_data.getGainIdUnique();
        if( gainId != -1 ) m_lookup.setSelectedItem(m_lookup.lookup(gainId));
        
        m_panel.getText().setEditable( true );
        m_panel.presentData( m_data );
	    return m_panel;
	}
	
	public Object getCellEditorValue() {
	    try {
	        if( m_fromBouton == false )
	            m_data.setQte( new BigDecimal(m_panel.getText().getText()), m_model.getDefaultGainId() );
	    }
	    catch( Exception e ) {
	        // Ssshh...
	    }
		return m_data;
	}
	
	public boolean stopCellEditing() {
		try {
		    String data = m_panel.getText().getText();
		    if( data != null && "".equals(data) == false ) new BigDecimal( m_panel.getText().getText() );
		}
		catch( Exception e ) { 
			JTextField field = m_panel.getText();
			field.setBorder( BorderFactory.createLineBorder(Color.red));
			field.setCaretPosition(0);
			field.moveCaretPosition(field.getText().length());
			return false;
		}
		fireEditingStopped();
		return true;
	}				


}
