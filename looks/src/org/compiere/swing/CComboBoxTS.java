package org.compiere.swing;


import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.plaf.*;

import org.compiere.plaf.*;
import org.compiere.util.*;



public class CComboBoxTS extends JComboBox
implements CEditor, KeyListener 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a <code>JComboBox</code> that takes it's items from an
	 * existing <code>ComboBoxModel</code>.  Since the
	 * <code>ComboBoxModel</code> is provided, a combo box created using
	 * this constructor does not create a default combo box model and
	 * may impact how the insert, remove and add methods behave.
	 *
	 * @param aModel the <code>ComboBoxModel</code> that provides the
	 * 		displayed list of items
	 * @see DefaultComboBoxModel
	 */
	public CComboBoxTS(ComboBoxModel aModel)
	{
		super(aModel);
		init();
	}   //  CComboBox

	/**
	 * Creates a <code>JComboBox</code> that contains the elements
	 * in the specified array.  By default the first item in the array
	 * (and therefore the data model) becomes selected.
	 *
	 * @param items  an array of objects to insert into the combo box
	 * @see DefaultComboBoxModel
	 */
	public CComboBoxTS(final Object items[])
	{
		super(items);
		init();
	}   //  CComboBox

	/**
	 * Creates a <code>JComboBox</code> that contains the elements
	 * in the specified Vector.  By default the first item in the vector
	 * and therefore the data model) becomes selected.
	 *
	 * @param items  an array of vectors to insert into the combo box
	 * @see DefaultComboBoxModel
	 */
	public CComboBoxTS(Vector items)
	{
		super(items);
		init();
	}   //  CComboBox

	/**
	 * Creates a <code>JComboBox</code> with a default data model.
	 * The default data model is an empty list of objects.
	 * Use <code>addItem</code> to add items.  By default the first item
	 * in the data model becomes selected.
	 *
	 * @see DefaultComboBoxModel
	 */
	public CComboBoxTS()
	{
		super();
		init();
	}   //  CComboBox
	
	/** Field Height 				 */
	public static int     		FIELD_HIGHT = 0;

	/**
	 *  Common Init
	 */
	private void init()
	{
		//  overwrite - otherwise Label Font
		setFont(CompierePLAF.getFont_Field());
		setForeground(CompierePLAF.getTextColor_Normal());
		setBackground(false);
		FIELD_HIGHT = getPreferredSize().height;
		
		addKeyListener(this);
	}   //  init


	/*************************************************************************/

	/** Icon        */
	private Icon m_icon = null;

	/**
	 *  Set Icon of arrow button to icon
	 *  @param defaultIcon Icon to be displayed
	 */
	public void setIcon (Icon defaultIcon)
	{
		if (getUI() instanceof CompiereComboBoxUI)
			((CompiereComboBoxUI)getUI()).setIcon(defaultIcon);
		m_icon = defaultIcon;
	}   //  setIcon

	/**
	 *  Set UI and re-set Icon for arrow button
	 *  @param ui
	 */
	public void setUI (ComboBoxUI ui)
	{
		super.setUI(ui);
		if (m_icon != null && ui instanceof CompiereComboBoxUI)
			((CompiereComboBoxUI)getUI()).setIcon(m_icon);
	}   //  setUI

	/**
	 *  Display Popup.
	 *  Called from CompiereComboPopup and allows to implement
	 *  alternative actions than showing the popup
	 *  @return if true, the popup should be displayed
	 */
	public boolean displayPopup()
	{
		return true;
	}   //  displayPopup

	/*************************************************************************/

	/** Mandatory (default false)   */
	private boolean m_mandatory = false;

	/**
	 *	Set Editor Mandatory
	 *  @param mandatory true, if you have to enter data
	 */
	public void setMandatory (boolean mandatory)
	{
		m_mandatory = mandatory;
		setBackground(false);
	}   //  setMandatory

	/**
	 *	Is Field mandatory
	 *  @return true, if mandatory
	 */
	public boolean isMandatory()
	{
		return m_mandatory;
	}   //  isMandatory

	/**
	 *	Enable Editor
	 *  @param rw true, if you can enter/select data
	 */
	public void setReadWrite (boolean rw)
	{
		if (super.isEnabled() != rw)
			super.setEnabled (rw);
		setBackground(false);
	}   //  setReadWrite

	/**
	 *	Is it possible to edit
	 *  @return true, if editable
	 */
	public boolean isReadWrite()
	{
		return super.isEnabled();
	}   //  isReadWrite

	/**
	 *  Set Background based on editable / mandatory / error
	 *  @param error if true, set background to error color, otherwise mandatory/editable
	 */
	public void setBackground (boolean error)
	{
		if (error)
			setBackground(CompierePLAF.getFieldBackground_Error());
		else if (!isReadWrite())
			setBackground(CompierePLAF.getFieldBackground_Inactive());
		else if (m_mandatory)
			setBackground(CompierePLAF.getFieldBackground_Mandatory());
		else
			setBackground(CompierePLAF.getFieldBackground_Normal());
	}   //  setBackground

	/**
	 *  Set Background
	 *  @param bg
	 */
	public void setBackground (Color bg)
	{
		if (bg.equals(getBackground()))
			return;
		super.setBackground(bg);
	}   //  setBackground

	/**
	 *	Set Editor to value
	 *  @param value value of the editor
	 */
	public void setValue (Object value)
	{
		super.setSelectedItem(value);
	}   //  setValue

	/**
	 *	Return Editor value
	 *  @return current value
	 */
	public Object getValue()
	{
		return super.getSelectedItem();
	}   //  getValue

	/**
	 *  Return Display Value
	 *  @return displayed String value
	 */
	public String getDisplay()
	{
		Object o = super.getSelectedItem();
		if (o == null)
			return "";
		return o.toString();
	}   //  getDisplay

	/**
	 *  Add Mouse Listener - 1-4-0 Bug.
	 *  Bug in 1.4.0 Metal: arrowButton gets Mouse Events, so add the JComboBox
	 *  MouseListeners to the arrowButton - No context menu if right-click
	 *  @see also CompiereComboBoxUI#installUI()
	 *  @param ml
	 */
	public void addMouseListener (MouseListener ml)
	{
		super.addMouseListener(ml);
		//  ignore calls from javax.swing.plaf.basic.BasicComboBoxUI.installListeners(BasicComboBoxUI.java:271)
		if (getUI() instanceof CompiereComboBoxUI && !Trace.getCallerClass(1).startsWith("javax"))
		{
			JButton b = ((CompiereComboBoxUI)getUI()).getArrowButton();
			if (b != null)
				b.addMouseListener(ml);
		}
	}   //  addMouseListener

	/**
	 *  Remove Mouse Listener.
	 *  @param ml
	 */
	public void removeMouseListener (MouseListener ml)
	{
		super.removeMouseListener(ml);
		if (getUI() instanceof CompiereComboBoxUI)
		{
			JButton b = ((CompiereComboBoxUI)getUI()).getArrowButton();
			if (b != null)
				b.removeMouseListener(ml);
		}
	}   //  removeMouseListener

	private String m_lookup = "";
	private int m_lastSelection = -1;
	private long m_delai = 0;
	private final int TIMEOUT = 1500; // en ms
	
	public void keyPressed(KeyEvent ev) { }
	public void keyReleased(KeyEvent ev) { }
	public void keyTyped(KeyEvent ev) {
	    long tick = System.currentTimeMillis();
	    
	    char key = ev.getKeyChar();
	    
	    if( key == KeyEvent.VK_ESCAPE || key == KeyEvent.VK_TAB ) return;
	    
	    boolean doLookup = false;
	    if( key == KeyEvent.VK_BACK_SPACE && m_lookup.length() > 0 ) {
	    	m_lookup = m_lookup.substring(0, m_lookup.length() - 1);
	    	doLookup = true;
	    }
	    else {
	    	if( Character.isISOControl(key) == false ) {
    	    	if(m_delai + TIMEOUT < tick) {
    	    		m_lookup = String.valueOf(key);
    	    	}
	    		else {
	    			m_lookup = m_lookup + key;
	    		}
    	    	doLookup = true;
	    	}
	    }
	    
	    if( doLookup ) {
		    m_delai = tick;
	
		    ComboBoxModel model = getModel();
		    for(int i = 0; i < model.getSize(); i++) {
		    	if( model.getElementAt(i).toString().toLowerCase().startsWith(m_lookup) ) {
			    	m_lastSelection = i;
			    	break;
		    	}
		    }
		    setSelectedIndex(m_lastSelection);
	    }
	    ev.consume();
	}

}
