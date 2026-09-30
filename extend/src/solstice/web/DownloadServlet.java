/******************************************************************************
 * Product: Solstice+ Payroll & Human Resources Management                    *
 * Copyright (C) 2008 ProGestion Informatique, Inc. All Rights Reserved.      *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ProGestion Informatique, 210-5300 Bld des Galerie, Quebec, G2K 2A2 Canada  *
 * or via info@progestion.net or http://www.progestion.net/license.html       *
 ******************************************************************************/
package solstice.web;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.PageContext;

import org.compiere.util.Env;

import solstice.custom.IUserInfo;
import solstice.model.P_Employee;
//import solstice.model.P_Language;

public class DownloadServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pageId = null;
	boolean securityCheck = true;
	
	protected HttpSession session;
	protected HttpServletResponse response;
	protected HttpServletRequest request;
	protected PageContext pageContext;
	/** Session Context 			*/
	public Properties		ctx = new Properties();
	private String trxName = null;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{

		this.request = request;
		this.session = request.getSession();
		this.response = response;

		JspFactory _jspxFactory = null;
		_jspxFactory = JspFactory.getDefaultFactory();
		this.pageContext =  _jspxFactory.getPageContext(this, request, response, null, true, 8192, true);

		IUserInfo userInfo = null;
		if ( this.session != null )
          userInfo = (IUserInfo)session.getAttribute("userInfo");

		if ( this.session != null )
          userInfo = (IUserInfo)session.getAttribute("userInfo");

  		DownloadFile( );

	}

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	IUserInfo userInfo = null;
    	
		this.request = request;
		this.session = request.getSession();
		this.response = response;

		JspFactory _jspxFactory = null;
		_jspxFactory = JspFactory.getDefaultFactory();
		this.pageContext =  _jspxFactory.getPageContext(this, request, response, null, true, 8192, true);

		if ( this.session != null )
          userInfo = (IUserInfo)session.getAttribute("userInfo");

		if ( this.session != null )
          userInfo = (IUserInfo)session.getAttribute("userInfo");
   	
		DownloadFile( );
    }

	public void setSecurityCheck( boolean securityCheck  )
	{
		this.securityCheck = securityCheck;
	}

	public boolean getSecurityCheck(  )
	{
		return this.securityCheck;
	}

    
	public void setPageId( String id )
	{
		this.pageId = id;
	}

	public String getPageId( )
	{
		return this.pageId;
	}
	
	public boolean SecurityCheck()
	{
		IUserInfo userInfo = null;
        userInfo = (IUserInfo)session.getAttribute("userInfo");

        // L'utilisateur n'est pas connecté ou n'a pas accès à la page. Erreur 403
        try
        {
            if(userInfo == null )
            {
            	response.sendRedirect("/login.jsp");
            }
            
        }
        catch (IOException e)
        {
			System.out.println("* ERROR * SecurityCheck " + e);
        }
	
        
		Env.setContext(ctx, "#AD_User_ID",  userInfo.getUserId());

		P_Employee EmployeeConnected = P_Employee.get( Env.getCtx(), userInfo.getEmployeeId(), null);
        if ( EmployeeConnected != null )
        {
//        	MUser User = MUser.get( Env.getCtx(), userInfo.getUserId() );
//        	P_Language Lang = P_Language.get( Env.getCtx(), EmployeeConnected.getP_Language_ID(), null);
			ctx.setProperty("#AD_Client_ID", String.valueOf(EmployeeConnected.getAD_Client_ID()));
			ctx.setProperty("#AD_Org_ID"   , String.valueOf(EmployeeConnected.getAD_Org_ID()));
			Env.setContext(ctx, "#AD_Client_ID", String.valueOf(EmployeeConnected.getAD_Client_ID()));
			Env.setContext(ctx, "#AD_Org_ID"   , String.valueOf(EmployeeConnected.getAD_Org_ID()));
        }

		int AD_Role_ID = 0;		//	HARDCODED - System
		Env.setContext(ctx, "#AD_Role_ID", AD_Role_ID);
		//ctx.setProperty("#AD_Language", language.getLanguageCode() );
		ctx.setProperty("#AD_Role_ID", String.valueOf(AD_Role_ID));
		ctx.setProperty("#AD_User_ID",  String.valueOf( userInfo.getUserId()) );
		//Env.setContext(ctx, "#AD_Language", language.getLanguageCode() );
        
//		&& userInfo.hasPolicy(this.pageId)
        if(userInfo != null && userInfo.hasPolicy(this.pageId) )
        {
            // L'utilisateur est connecté et a accès à la page. On évalue donc son contenue
            return true;
        }

        try
        {
        	response.sendError(403);
        }
        catch (IOException e)
        {
			System.out.println("* ERROR * SecurityCheck " + e);
        }
        return false;

	}

	String fileName = "";
	public void setFileName( String name )
	{
		fileName = name;
	}
	public void DownloadFile(  )
	{
		if ( ! SecurityCheck() )
		{
			return ;
		}

		setFileName( "xyz");

		File file = new File(fileName);
	  	if (!file.exists() || !file.isFile() )    
	   	{
	   		System.out.println("* ERROR * File not found " + fileName );
	   	}
	
	   	System.out.println("* ERROR * View File : " + fileName );


	}

}
