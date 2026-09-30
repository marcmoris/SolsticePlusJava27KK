/*
 * Created on 28 juil. 2005
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
public class LivretTempsRecherche
{
    public static boolean searchBanque (PageContext pageContext)
    {
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        return request.getParameter("action") != null && request.getParameter("action").equals("search");
    }
 
    public static boolean conditionBanque (PageContext pageContext, String condition)
    {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        return request.getParameter(condition) != null && !request.getParameter(condition).equals("");
    }
    
    public static boolean conditionEmployeeDistribution(PageContext pageContext, String id)
    {
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        return request.getParameter(id) != null && !request.getParameter(id).equals("");
    }
    
    public static boolean conditionEmployeeBanque (PageContext pageContext, String id)
    {
        //HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        return (id != null);
    }
}
