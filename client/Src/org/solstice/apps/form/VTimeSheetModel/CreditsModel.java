/*
 * Created on 22 août 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.solstice.apps.form.VTimeSheetModel;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.compiere.plaf.CompiereColor;

public class CreditsModel extends DefaultTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public boolean isCellEditable(int arg0, int arg1) { return false; }
	
	private HashMap<?, ?> m_mapUnits;
	private HashMap<?, ?> m_mapUnits2;
	public void setMapUnits( HashMap<?, ?> map ) { m_mapUnits = map; }
	public String getUnits( int col ) { return (String)m_mapUnits.get(new Integer(col)); }

	public void setMapUnits2( HashMap<?, ?> map ) { m_mapUnits2 = map; }
	public String getUnits2( int col ) { return (String)m_mapUnits2.get(new Integer(col)); }

	public class CreditCellRenderer extends DefaultTableCellRenderer {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private CompiereColor m_backgroundColor = new CompiereColor();
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			CreditsModel model = (CreditsModel)table.getModel();
			JLabel component = (JLabel)super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column) ;
			if( row > 0 ) {
			    setBackground( Color.WHITE );
				if( value != null ) {
					setHorizontalAlignment(JTextField.RIGHT);
					if ( row == 2)
					{
						String units = model.getUnits2(column);
						if( units != null ) setText(value + " " + units );
					}
					else
					{
						String units = model.getUnits(column);
						if( units != null ) setText(value + " " + units );
					}
				}
			}
			else {
			    setBackground( m_backgroundColor.getFlatColor() );
				setHorizontalAlignment(JTextField.LEFT);
			    setText(String.valueOf(value));
			}
			return component;
		}
	}
};
