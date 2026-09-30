/*
 * Created on 8 juil. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.custom;

import javax.servlet.jsp.JspException;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IQueryUser 
{
    /**
     * Recoit un tag pour en extraire la requête
     */
	public void setQuery(QueryBaseTag query) throws JspException;
}
