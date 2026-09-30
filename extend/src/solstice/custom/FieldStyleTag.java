/*
 * Created on 29 juil. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.custom;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FieldStyleTag extends BodyTagSupport
{
    /*
     * Attribute
     */
    private boolean alt;
    
    public void setAlt(boolean alt) {this.alt = alt;}
    
	/**
	 * On récupère le contenue du body i.e. la requète
	 */
	public int doAfterBody() throws JspTagException
	{
		try
		{
			BodyContent bc = getBodyContent();
			String style = bc.getString();
			if(alt)
			    ((FieldTag)this.getParent()).setStyleAlt(style);
			else
			    ((FieldTag)this.getParent()).setStyle(style);
			bc.clearBody();
		}
		catch (Exception e)
		{
			throw new JspTagException ("doAfterBody : " + e.toString());
		}
		
		return SKIP_BODY;
	}
}
