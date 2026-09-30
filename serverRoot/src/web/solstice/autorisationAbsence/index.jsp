
<%@ page import="java.io.*" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>


<%@ page import="org.compiere.util.DB" %>
<%@ page import="org.compiere.util.Env" %>
<%@ page import="org.compiere.util.Msg" %>

<%@ page import="solstice.custom.IUserInfo" %>

<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.util.Calendar" %>

<%@ page import="solstice.utils.PgiUtil" %>


<%@ page extends="solstice.web.PageWebSolstice" %>

<%!
    public String GeneratePage( ) throws Exception 
	{

		IUserInfo userInfo = null;
		String liste = "";
    	
		this.setPageId("mainAutorisation");
		this.setSecurityCheck( true );
		String client = PgiUtil.getSolsticeParameter( ctx, "Client").toLowerCase();
		this.setTemplate("index_level2_" + client + ".jtpl");

		if ( ! SecurityCheck() )
		{
			response.sendError(403); 

		}

		if ( this.session != null )
			userInfo = (IUserInfo)session.getAttribute("userInfo");
		
		String webAppUrl = PgiUtil.getSolsticeParameter(ctx, "WEBAPPURL");
		if ( userInfo == null )
		{
			response.sendRedirect( webAppUrl + "login.jsp");
		}
		else
		{

		  if(userInfo.hasPolicy("remplir"))
		  	  liste = liste.concat("<li><a href=\"remplir.jsp\" class=\"menu\">Remplir une demande d'autorisation d'absence</a></li>");
		  
		  if(userInfo.hasPolicy("consult"))
			  liste = liste.concat("<li><a href=\"consult.jsp\" class=\"menu\">Consulter la liste de vos absences</a></li>");
		  if(userInfo.hasPolicy("admconsult"))
		  	  liste = liste.concat("<li><a href=\"admconsult.jsp\" class=\"menu\">Consulter la liste des absences de vos employés à approuver</a></li>");
//		  liste = liste.concat("<li><a href=\"admabssig.jsp\" class=\"menu\">Accéder au menu des absences signifiées</a></li>");
	      if(userInfo.hasPolicy("admabssig"))
			  liste = liste.concat("<li><a href=\"listeabssig.jsp\" class=\"menu\">Liste des absences signifiées</a></li>");
	      if(userInfo.hasPolicy("admabssig"))
		      liste = liste.concat("<li><a href=\"addabssig.jsp\" class=\"menu\">Ajouter une absence signifiée</a></li>");
		}

	   tpl.assign("listeApplications", liste);
	   tpl.assign("firstMenu", "<a href=\"/solstice/index.jsp\" class=\"path\">Menu principal</a>");
	   tpl.assign("welcome2", "");
	   tpl.parse("main");
	   return (tpl.out());
	}
%>
