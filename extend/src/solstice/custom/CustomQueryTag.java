/*
 * Created on 5 juil. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.custom;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;

/**
 * @author frafor01
 *
 * La requête se trouve directement dans le body
 */
public class CustomQueryTag extends QueryBaseTag
{
	/**
	 * Récupération de la requête
	 */
	public int doAfterBody() throws JspTagException
	{
		BodyContent bc = getBodyContent();
		this.sql = bc.getString();
		bc.clearBody();
		return SKIP_BODY;
	}
}
