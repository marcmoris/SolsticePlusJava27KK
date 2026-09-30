/*
 * Created on 12 juil. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.custom;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BlankPageTag extends BodyTagSupport 
{
	private StringBuffer errors = new StringBuffer();
	private boolean postBack = false;
	private boolean supportCalendar = false;
	
	public boolean isPostBack()
	{
		return this.postBack;
	}
	
	public void setSupportCalendar(boolean supportCalendar)
	{
		this.supportCalendar = supportCalendar;
	}
	
	public int doStartTag() throws JspException
	{
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		if(request.getParameter("pageId") != null && request.getParameter("pageId").equals(this.getId()))
		{
		    this.postBack = true;
		}
		else
		{
		    this.postBack = false;
		}


		errors.delete(0, errors.length());
		return EVAL_BODY_INCLUDE;
	}
	
	public void addNewError(String errorMsg)
	{
		this.errors.append("  <input type=\"hidden\" name=\"erreurs\" value=\"" + errorMsg + "\">\n");
	}
	
	public int doEndTag() throws JspException
	{
		String html
		= "<!-- CALENDRIER -->\n"
			+ "  <script language=\"JavaScript\" src=\"scripts/calendarheader.js\" type=text/javascript></script>\n"
			+ "  <script language=\"JavaScript\" src=\"scripts/overlib_mini.js\"></script>\n"
			+ "<!-- FIN CALENDRIER -->\n"
			+ "  </body>\n"
			+ "</html>\n";
		
		if(errors.length() > 0)
		{
			html
				= "<form name=\"errorForm\" target=\"errorWindow\" action=\"error.jsp\" method=\"post\">\n"
				+ this.errors.toString()
				+ "</form>"
				+ "<script language=\"javascript\">\n"
				+ "  window.open(\"error.jsp\", \"errorWindow\", \"left=20,top=20,width=900,height=600,toolbar=0,resizable=0,scrollbars=1\");\n"
				+ "  document.errorForm.submit();\n"
				+ "</script>\n" + html;
		}
		
		JspWriter out = pageContext.getOut();
		try
		{
			out.println(html);
		}
		catch (IOException e)
		{
   		e.printStackTrace(System.out);
		}
		return EVAL_PAGE;
	}
}
