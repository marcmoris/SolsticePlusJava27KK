/*
 * Created on 25 juil. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.custom;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspWriter;

/**
 * @author frafor01
 *
 * Transfert des informations vers le formulaire d'avis de dépot
 */
public class AvisDepot extends ValidationBase
{

    /* (non-Javadoc)
     * @see com.compiere.custom.ValidationBase#doValidate()
     */
    protected String[] doValidate()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.compiere.custom.ValidationBase#afterValidation(javax.servlet.jsp.JspWriter)
     */
    public boolean afterValidation(JspWriter out)
    {
        try
        {
 		      if(((IUserInfo)session.getAttribute("userInfo")).hasPolicy("avis_allemployee"))
 		      {
				   pageContext.forward("avis_admin.jsp");
			  }
			  else
			  {
				   pageContext.forward("avis.jsp");
			  }
				   
        }
        catch (ServletException e)
        {
            e.printStackTrace(System.out);
        }
        catch (IOException e)
        {
            e.printStackTrace(System.out);
        }
        return false;
    }

    /* (non-Javadoc)
     * @see com.compiere.custom.ValidationBase#getPageName()
     */
    protected String getPageName()
    {
        return "index.jsp";
    }

}
