
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
    	
		this.setPageId("evaluationRendement_index");
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
		  liste = liste.concat("<li><a href=\"creation.jsp\" class=\"menu\">&Eacute;valuation d'un employ&eacute;</a></li>");
		  liste = liste.concat("<li><a href=\"recherche.jsp\" class=\"menu\">Rechercher une &eacute;valuation</a></li>");
		  liste = liste.concat("<li><a href=\"modification.jsp\" class=\"menu\">Modification du formulaire</a></li>");
		  liste = liste.concat("<li><a href=\"moneval.jsp\" class=\"menu\">Mes &eacute;valuations</a></li>");
		  liste = liste.concat("<li><a href=\"automaticrecall.jsp\" class=\"menu\">Param&egrave;tres</a></li>");
		}

	   tpl.assign("listeApplications", liste);
	   tpl.assign("welcome", "<a href=\"/solstice/index.jsp\" class=\"path\">Menu principal</a>");
	   tpl.assign("welcome2", "");
	   tpl.parse("main");
	   return (tpl.out());
	}
%>


