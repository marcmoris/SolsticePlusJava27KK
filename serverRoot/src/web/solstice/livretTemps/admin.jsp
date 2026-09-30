
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
    	
		this.setPageId("livradmin");
		this.setSecurityCheck( true );
		String client = PgiUtil.getSolsticeParameter( ctx, "Client").toLowerCase();
		this.setTemplate("index_" + client + ".jtpl");

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

			liste = liste.concat("<li><a href=\"banque.jsp\" class=\"menu\">Liste des banques de temps</a></li>");
			liste = liste.concat("<li><a href=\"sommaire.jsp\" class=\"menu\">Liste des temps sommaires</a></li>");
			liste = liste.concat("<li><a href=\"avancement.jsp\" class=\"menu\">Liste d'état d'avancement des livrets</a></li>");
			liste = liste.concat("<li><a href=\"grpgain.jsp\" class=\"menu\">Gestion des groupes de code de rémuneration</a></li>");
		}

	   tpl.assign("listeApplications", liste);
	   tpl.assign("welcome", "<a href=\"/solstice/index.jsp\" class=\"path\">Menu principal</a>");
	   tpl.assign("welcome2", "");
	   tpl.parse("main");
	   return (tpl.out());
	}
%>


