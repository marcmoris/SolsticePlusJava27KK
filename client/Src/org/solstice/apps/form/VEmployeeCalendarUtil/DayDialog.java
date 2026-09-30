/*
 * Created on 12 sept. 2005
 *
 */
package org.solstice.apps.form.VEmployeeCalendarUtil;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.solstice.apps.form.VTimeSheetModel.DETableRenderer;
import org.solstice.apps.form.VTimeSheetModel.Lookup.LookupGain;

public class DayDialog extends JDialog implements ActionListener {
    private DayData m_data;
    private DETDayModel m_model;
    private LookupGain m_lookup;

    public DayDialog( JComponent parent, DayData data, LookupGain lookup ) {
        super( JOptionPane.getFrameForComponent(parent), DateFormat.getDateInstance().format(data.getDate().getTime()), true );

        m_lookup = lookup;
        m_data = data;
        
        setLocationRelativeTo(parent.getTopLevelAncestor());
        
        init();
    }

    private void init() {
        Container pane = getContentPane();
        Vector<Object[]> data = new Vector<Object[]>(); 
        List liste = m_data.getListData();
        for( Iterator iter = liste.iterator(); iter.hasNext(); ) {
            DayData.Payload payload = (DayData.Payload)iter.next();
            Object[] ligne = new Object[5];
            
            ligne[DETDayModel.COL_GAIN] = m_lookup.lookup(payload.getGainId());
            ligne[DETDayModel.COL_QTE] = payload.getQte();
            ligne[DETDayModel.COL_ID] = new Integer(payload.getId());
            ligne[DETDayModel.COL_DELETE] = new Boolean(false);
            ligne[DETDayModel.COL_AUTORISE] = new Boolean(payload.getAutorise());
            data.add(ligne);
        }
        
        m_model = new DETDayModel(data, m_lookup);
        
        JTable table = new JTable();
        
        new DETableRenderer().bindTable(table, m_model);
        table.getColumnModel().getColumn(m_model.indexDeleteColumn()).setMaxWidth(20);
        table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        JScrollPane scrollpane = new JScrollPane(table);
        scrollpane.setPreferredSize( new Dimension(300,200) );
        pane.add( scrollpane, BorderLayout.CENTER );
        
        Box btns = Box.createHorizontalBox();
        ((JButton)btns.add( new JButton("Ok") )).addActionListener(this);
        ((JButton)btns.add( new JButton("Cancel") )).addActionListener(this);
        pane.add( btns, BorderLayout.SOUTH );
        pack();
        
        Point pos = getLocation();
        pos.translate( -getWidth()/2, -getHeight()/2 );
        setLocation( pos );
    }
    
    public void actionPerformed(ActionEvent ev) {
        String actn = ev.getActionCommand();
        if( "Ok".equals(actn) ) {
            m_data.resetLists();
            for( int i=0; i<m_model.getRowCount(); i++ ) {
                if( m_model.isDataRow(i) ) {
                    Integer id = (Integer)m_model.getValueAt(i, DETDayModel.COL_ID);
                    if( m_model.isRowDeleted(i) ) {
                        if( id != null ) m_data.remove( id.intValue() );
                    }
                    else {
                        Boolean autorise = (Boolean)m_model.getValueAt(i, DETDayModel.COL_AUTORISE);
                        m_data.add( id != null ? id.intValue() : -1, 
                                	(BigDecimal)m_model.getValueAt(i, DETDayModel.COL_QTE),
                                	m_lookup.lookup(m_model.getValueAt(i, DETDayModel.COL_GAIN)),
                                	autorise != null && autorise.booleanValue());
                    }
                }
            }
            m_data.setDirty( true );
        }
        dispose();
    }
}
