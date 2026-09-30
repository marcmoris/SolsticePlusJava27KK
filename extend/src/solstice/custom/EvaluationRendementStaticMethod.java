/*
 * Created on 20 juil. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.custom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class EvaluationRendementStaticMethod
{
    public static boolean showGrid(PageContext pageContext)
    {
        return ((HttpServletRequest)pageContext.getRequest()).getParameter("action") != null
    		&& ((HttpServletRequest)pageContext.getRequest()).getParameter("action").equals("recherche");
    }
    
    public static boolean useCondition(PageContext pageContext, String condition)
    {
        return ((HttpServletRequest)pageContext.getRequest()).getParameter(condition) != null
        	&& ((HttpServletRequest)pageContext.getRequest()).getParameter(condition).equals("on");
    }
}
