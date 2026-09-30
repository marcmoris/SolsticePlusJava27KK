/*
 * Created on 7 juil. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.custom;

import java.sql.Timestamp;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import solstice.process.PgiUtil;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class ValidationBase 
{
	private boolean validated = false;
	protected HttpServletResponse response;
	protected HttpServletRequest request;
	protected HttpSession session;
	protected PageContext pageContext;
	
	public boolean getValidated()
	{
		return this.validated;
	}
	
	public ValidationBase()
	{
	}
	
	public void init(PageContext pageContext)
	{
		this.pageContext = pageContext;
		this.response = (HttpServletResponse)pageContext.getResponse();
		this.request = (HttpServletRequest)pageContext.getRequest();
		this.session = (HttpSession)pageContext.getSession();
	}
	
	public String[] validate() throws JspException
	{
		if(request != null 
				&& request.getMethod().toUpperCase().equals("POST") )
				//&& request.getRequestURL().indexOf(this.getPageName()) >= 0)
		{
			validated = true;
			return doValidate();
		}
		validated = false;
		return null;
	}
	
	protected abstract String[] doValidate() throws JspException;
	
	public abstract boolean afterValidation(JspWriter out) throws JspException;
	
	/**
	 * Convertir une date sortant normalement d'un Calendar (yyyy-MM-dd)
	 * en Timestamp
	 * @param value
	 * @return
	 */
	protected Timestamp dateConverter(String value)
	{
		try
		{
            Calendar calendar = PgiUtil.stringToDate(value);
	        return new Timestamp(calendar.getTimeInMillis());
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	/**
	 * Permet d'obtenir si l'utilisateur loggé à accès à la politique de sécurité
	 * @param pageContext Le contexte de la page
	 * @param policy la politique de sécurité
	 * @return
	 */
	protected static boolean loggedUserHasPolicy(PageContext pageContext, String policy)
	{
		IUserInfo user = getUserInfo(pageContext);
		return user.hasPolicy(policy);
	}
	
	/**
	 * Permet d'obtenir l'utilisateur
	 * @param pageContext le contexte de la page
	 * @return l'utilisateur
	 */
	protected static IUserInfo getUserInfo (PageContext pageContext)
	{
		return (IUserInfo)pageContext.getSession().getAttribute("userInfo");
	}
}
