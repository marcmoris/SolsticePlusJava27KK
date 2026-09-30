/*
 * Created on 19 août 2005
 */
package solstice.custom;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Cette classe définit le check de sécurité qui doit être fait dans chacune
 * des pages des applications web. Si l'utilisateur n'est pas connecté ou s'il
 * n'a pas accès à la page, on retourne une erreur 403
 * @author frafor01
 */
public class SecurityCheckTag extends TagSupport
{
   
    /**
     * Le check de sécurité
     */
    public int doStartTag() throws JspException
    {
        IUserInfo userInfo = (IUserInfo)this.pageContext.getSession().getAttribute("userInfo");
        if(userInfo != null && userInfo.hasPolicy(this.id))
        {
            // L'utilisateur est connecté et a accès à la page. On évalue donc son contenue
            return EVAL_BODY_INCLUDE;
        }
        
        // L'utilisateur n'est pas connecté ou n'a pas accès à la page. Erreur 403
        try
        {
            HttpServletResponse response = (HttpServletResponse)this.pageContext.getResponse();
            if(userInfo == null )
            {
            	response.sendRedirect("/login.jsp");
            }
            
            response.sendError(403);
            return SKIP_BODY;
        }
        catch (IOException e)
        {
            throw new JspException(e);
        }
    }
    
}
