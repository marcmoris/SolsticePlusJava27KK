/*
 * Created on 13 sept. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.solstice.apps.form.VEmployeeCalendarUtil;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import org.compiere.plaf.CompiereColor;
import org.compiere.util.Env;

/**
 * @author jeacat01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DayPanel extends JPanel {
    private JTextField m_text;
    private JButton m_btnStdTime;
    private JButton m_button;
    private JLabel m_vide;

    private Font m_fontNormal;
    private Font m_fontBold;    
    
    public DayPanel() {
        m_vide = new JLabel();
        m_vide.setOpaque(true);
        CompiereColor.setBackground(m_vide);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0,1,0,1);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        
        m_button = new JButton();
        Dimension taille = new Dimension(70, 20);
        m_button.setMinimumSize( taille );
        m_button.setPreferredSize( taille );
        m_button.setMaximumSize( taille );
        m_button.setBorder( BorderFactory.createEtchedBorder() );
        
        m_btnStdTime = new JButton();
        taille = new Dimension(40, 20);
        m_btnStdTime.setMinimumSize( taille );
        m_btnStdTime.setPreferredSize( taille );
        m_btnStdTime.setMaximumSize( taille );
        m_btnStdTime.setBorder( BorderFactory.createEtchedBorder() );
        
        m_text = new JTextField();
        m_text.setHorizontalAlignment( JTextField.RIGHT );
        
        setLayout( new GridBagLayout() );
        setBorder( BorderFactory.createBevelBorder(BevelBorder.RAISED) );
        
        add( m_button, gbc );
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add( m_btnStdTime, gbc );
        
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add( m_text, gbc );
        
        m_fontNormal = m_button.getFont();
        m_fontBold = new Font(m_fontNormal.getName(), Font.BOLD, m_fontNormal.getSize() );
    }
    
    public JTextField getText() { return m_text; }
    public JButton getButton() { return m_button; }
    public JLabel getVide() { return m_vide; }
    public JButton getBtnStdTime() { return m_btnStdTime; }
    
    public void presentData( DayData data ) {
        Color background = CalendarModel.COLOR_BASE;
    	if( data.getFlagFerie() ) background = CalendarModel.COLOR_FERIE;
		else if( data.getStandardTime().compareTo(Env.ZERO) > 0 ) background = CalendarModel.COLOR_STANDARD;

        setBackground( background );
	    
	    m_btnStdTime.setText( String.valueOf(data.getStandardTime().setScale(0, BigDecimal.ROUND_HALF_UP)) );

	    if( data.isMultiValue() || data.isEmpty() ) m_text.setBackground(background);
	    else m_text.setBackground( data.isAuthAbsence() ? CalendarModel.COLOR_AUTH_ABSENCE : CalendarModel.COLOR_AUTRE_ABSENCE ); 

	    m_text.setText( data.getQteUnique() != null ? String.valueOf(data.getQteUnique()) : null );
	    m_text.setEditable( data.isMultiValue() == false );
	    
	    m_button.setText( String.valueOf(data.getDate().get(Calendar.DAY_OF_MONTH)) );
	    m_button.setFont( m_fontBold );
	    m_button.setBackground( data.isMultiValue() ? CalendarModel.COLOR_MULTI_CODE: background );
	    m_button.setOpaque(true);
    }


}
