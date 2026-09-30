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
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="javax.servlet.jsp.PageContext" %>
<%@ page import="org.compiere.util.DB" %>
<%@ page import="org.compiere.util.Env" %>
<%@ page import="org.compiere.util.TimeUtil" %>
<%@ page import="solstice.custom.IUserInfo" %>
<%@ page import="solstice.web.PageWebSolstice" %>
<%@ page import="solstice.model.P_Employee" %>
<%@ page import="org.compiere.model.MUser" %>
<%@ page import="org.compiere.model.MUserMail" %>
<%@ page import="java.util.TimeZone" %>
<%@ page import="solstice.utils.PgiUtil" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>  
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*" %>
<%@ page import="java.util.SimpleTimeZone" %>
<%@ page import="org.compiere.model.MAttachment" %>

<%@ page extends="solstice.web.PageWebSolstice" %>

<%!
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String GeneratePage( ) throws Exception 
	{
		String trxName = null;

		this.setPageId( "Message" );
	    this.setSecurityCheck( true );
	    this.setTemplate("MessageBox.jtpl");

		if ( ! SecurityCheck() )
			response.sendError(403); 

		
		IUserInfo userInfo = PageWebSolstice.getUserInfo(pageContext);
		
		P_Employee Employee = P_Employee.get(ctx, userInfo.getEmployeeId(), trxName);

		System.out.println("* DEBUG * Employee " + Employee.getValue() + " - " + Employee.getName() );
   		System.out.println("* DEBUG * UserInfo " + userInfo.getUserId() );

		MUserMail UserMail = null;


		String action = request.getParameter("action");

		
		String UserMailID  = request.getParameter("UserMailID");


		if ( UserMailID != null &&  UserMailID != "" )
		{


	    	UserMail = new MUserMail( ctx, Integer.parseInt( UserMailID ), trxName);
	    	if ( UserMail.getAD_User_ID() != Employee.getAD_User_ID()) //((IUserInfo)session.getAttribute("userInfo")).getUserId() )
			{
				this.setTemplate("Error.jtpl");
				tpl.assign("ERRORMSG", "Erreur : Vous n'avez pas accès a cet information...");
				tpl.parse("main");
				return (tpl.out());
			}
	    	else
			{
		    	UserMail.setIsDelivered( "Y" );
		    	UserMail.save();
			}

			
		}

		String error = request.getParameter("error");
		if ( error != null )
		{
			this.setTemplate("Error.jtpl");
			tpl.assign("ERRORMSG", "Erreur : le fichier demandé n'existe pas...");
			tpl.parse("main");
			return (tpl.out());
		
		}
			
		if(action != null)
		{

			String webAppUrl = PgiUtil.getSolsticeParameter(ctx, "WEBAPPURL");

	        if(action.equals("delete"))
	        {

			System.out.println("* DEBUG * action delete ");
	    		Enumeration NomsParam = request.getParameterNames();

	    		while(NomsParam.hasMoreElements()) 
	    		{
	    			String NomParam = (String)NomsParam.nextElement();
	    			String[] ValeursParam = request.getParameterValues(NomParam);



	    			if ( NomParam.startsWith("selectedMessages")  ) // && ValeursParam.equals("on")
	    			{
		    		    	UserMail = new MUserMail( ctx, Integer.parseInt(  ValeursParam[0] ), trxName);
		    	        	UserMail.delete( true );
	    			}
	    		}

			}

			
			if(action.equals("view"))
	        {
/*	        	if ( UserMail.getMailPDF() != null )
					response.sendRedirect(webAppUrl + "/MessageBox/download.jsp?UserMailID=" +UserMailID );
	        	else
*/	        		
	        	{
	        		
		    	    this.setTemplate("MessageBoxView.jtpl");
		    		tpl.assign("TITLE", "Liste des Messages à consulter ");
					tpl.assign("MSG", UserMail.getMailText()) ;
					
					MAttachment attachment = UserMail.getAttachment();
				   	if ( attachment != null)
				   	{
						tpl.assign("AttachmentMsg", "<a href=\"download.jsp?UserMailID=" +UserMailID + "&p=1&target=\"_blank\">Cliquer ici pour voir pièce jointe&nbsp;&nbsp;</a>") ;
						tpl.assign("Attachment", "<a href=\"download.jsp?UserMailID=" +UserMailID + "&p=1&target=\"_blank\"><img src=\"attachment.png\" WIDTH=\"40\" HEIGHT=\"40\" border=\"0\" /></a>") ;
				   	}

				   	else if ( UserMail.getMailPDF() != null )
					{
						tpl.assign("AttachmentMsg", "<a href=\"download.jsp?UserMailID=" +UserMailID + "&p=1&target=\"_blank\">Cliquer ici pour voir pièce jointe&nbsp;&nbsp;</a>") ;
						tpl.assign("Attachment", "<a href=\"download.jsp?UserMailID=" +UserMailID + "&p=1&target=\"_blank\"><img src=\"attachment.png\" WIDTH=\"40\" HEIGHT=\"40\" border=\"0\" /></a>") ;
					}
					else
					{
						tpl.assign("AttachmentMsg", "&nbsp;" ) ;
						tpl.assign("Attachment", "&nbsp;" ) ;
					}

					if ( UserMail.getMailPDF2() != null )
//					if ( UserMail.getR_MailText_ID() == 1000029)
					{
						tpl.assign("AttachmentMsg2", "<a href=\"download.jsp?UserMailID=" +UserMailID + "&p=2&target=\"_blank\">Cliquer ici pour voir le formulaire&nbsp;&nbsp;</a>") ;
						tpl.assign("Attachment2", "<a href=\"download.jsp?UserMailID=" +UserMailID + "&p=2&target=\"_blank\"><img src=\"attachment.png\" WIDTH=\"40\" HEIGHT=\"40\" border=\"0\" /></a>") ;
					}
					else
					{
						tpl.assign("AttachmentMsg2", "&nbsp;" ) ;
						tpl.assign("Attachment2", "&nbsp;" ) ;
					}

					
					tpl.parse("main");
					return (tpl.out());
	        	}

	        	//				response.sendRedirect(webAppUrl + "/MessageBox/download.jsp?UserMailID=" +UserMail_ID );
	        }
		
		}

		
	   tpl.assign("TITLE", "Liste des messages &agrave; consulter");
 	   tpl.assign("LabelSubject", "Objet");
	   tpl.assign("LabelName", "Name");	
	   tpl.assign("LabelDate", "Date");	
	   tpl.assign("LabelAttachment", "Pi&egrave;ce jointe");	
	   tpl.assign("LabelDelete", "Supprimer les messages s&eacute;lectionn&eacute;s.");
	  
  	   GenerateDetail();

		
	   tpl.parse("main");
	   return (tpl.out());
	}

	private void GenerateDetail()
	{ 

		IUserInfo userInfo = PageWebSolstice.getUserInfo(pageContext);
		
		P_Employee Employee = P_Employee.get(ctx, userInfo.getEmployeeId(), null);

		String sql = "SELECT AD_UserMail_ID, Subject, isnull( IsDelivered, 'N') IsDelivered, AD_User.Name, AD_UserMail.Created, MailPdf, MailPdf2  FROM AD_UserMail " 
				   + " INNER JOIN AD_User on AD_User.AD_User_ID = AD_UserMail.CreatedBy " 
				   + " WHERE MailText is not null AND AD_UserMail.AD_USER_ID = " + Employee.getAD_User_ID(); 
				   //((IUserInfo)session.getAttribute("userInfo")).getUserId();
		try
		{

			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			boolean alt = false;

			SimpleDateFormat sdf = new SimpleDateFormat();
		    sdf.setTimeZone(new SimpleTimeZone(0, "GMT"));
		    sdf.applyPattern("dd MMMM yyyy");
		    
			while (rs.next ())
			{
				
				tpl.assign("MailID", rs.getInt("AD_UserMail_ID"));
				tpl.assign("Subject", rs.getString("Subject"));
				tpl.assign("Name", rs.getString("Name"));	
			    String DateStr = sdf.format(new java.sql.Date( rs.getTimestamp("Created").getTime()));

				tpl.assign("Date", DateStr);	

				

				if ( rs.getString("MailPDF") != null )
					tpl.assign("ImagePdf", "<a href=\"download.jsp?UserMailID=" +rs.getInt("AD_UserMail_ID") + "&p=1target=\"_blank\"><img src=\"attachment.png\" WIDTH=\"22\" HEIGHT=\"22\" border=\"0\" /></a>");
				else
					tpl.assign("ImagePdf", "");

				if ( rs.getString("MailPDF2") != null )
					tpl.assign("ImagePdf2", "<a href=\"download.jsp?UserMailID=" +rs.getInt("AD_UserMail_ID") + "&p=2&target=\"_blank\"><img src=\"attachment.png\" WIDTH=\"22\" HEIGHT=\"22\" border=\"0\" /></a>");
				else
					tpl.assign("ImagePdf2", "");

				
				if ( rs.getString("IsDelivered").equals("N"))
				{
					tpl.assign("ImageRead", "<a href=./MessageBox.jsp?action=view&UserMailID="+ rs.getString( "AD_UserMail_ID") + " TARGET=\"_blank\"  onClick=\"sendAction('view', '" + rs.getString( "AD_UserMail_ID") + "');\"> <img src=\"envfp.gif\" WIDTH=\"25\" HEIGHT=\"22\" border=\"0\" /></a>");
					tpl.assign("Read1", "<B>");	
					tpl.assign("Read2", "</B>");
				}
				else	
				{
					tpl.assign("ImageRead", "<a href=./MessageBox.jsp?action=view&UserMailID="+ rs.getString( "AD_UserMail_ID") + " TARGET=\"_blank\"  onClick=\"sendAction('view', '" + rs.getString( "AD_UserMail_ID") + "');\"> <img src=\"envop.gif\" WIDTH=\"25\" HEIGHT=\"22\" border=\"0\" /></a>");
					tpl.assign("Read1", "");	
					tpl.assign("Read2", "");
				}
					
				
		        if ( alt)
				    tpl.assign("CELLCLASS", "tdfieldalt");
		        else
		            tpl.assign("CELLCLASS", "tdfield");
		        
//		        pageContext.setAttribute("UserMailID", rs.getString( "AD_UserMail_ID"));
				tpl.assign("VIEW", "<a href=./MessageBox.jsp?action=view&UserMailID="+ rs.getString( "AD_UserMail_ID") + " TARGET=\"_blank\"  onClick=\"sendAction('view', '" + rs.getString( "AD_UserMail_ID") + "');\"> " + rs.getString("Subject") + "</a>");
//				tpl.assign("VIEW", "<a href=./MessageBox.jsp?action=view" + "TARGET=\"_blank\"  onClick=\"sendAction('view', '" + rs.getString( "AD_UserMail_ID") + "');\"> " + rs.getString("Subject") + "</a>");


				alt = !alt;

				tpl.parse("main.line");


			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println("* ERROR * Read messages sql " + sql + "Exception :" + e);
		}
	}


%>	