/*
 * Created on 5 juil. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.custom;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * @author frafor01
 *
 * Retourne une requête SQL
 */
public abstract class QueryBaseTag extends BodyTagSupport
{
	private String orderBy;
	protected String sql;
	
	public String getQuery()
	{
	    if(this.orderBy != null && !this.orderBy.equals(""))
	        return sql + " order by " + this.orderBy;
	    
	    return sql;
	}
	
	public void setOrderBy(String orderBy)
	{
		this.orderBy = orderBy;
	}
	
	public QueryBaseTag()
	{
		super();
	}
	
	public int doEndTag() throws JspException
	{
		((IQueryUser)getParent()).setQuery(this);
		return EVAL_PAGE;
	}
}
