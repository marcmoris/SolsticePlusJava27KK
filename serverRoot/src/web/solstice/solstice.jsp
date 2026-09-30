<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="javax.servlet.jsp.PageContext" %>
<%@ page import="org.compiere.util.DB" %>
<%@ page import="org.compiere.util.Env" %>
<%@ page import="org.compiere.model.MActivity" %>
<%@ page import="solstice.custom.EMailSender" %>
<%@ page import="solstice.custom.IUserInfo" %>
<%@ page import="solstice.utils.PgiUtil" %>
<%@ page import="solstice.web.PageWebSolstice" %>

<%@ page import="org.compiere.util.TimeUtil" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="solstice.utils.TimeUtilSolstice" %>

<%@ page extends="solstice.web.PageWebSolstice" %>

<%!public String GeneratePage( ) throws Exception 
{
   String PC_BROWSER_TEMPLATE =  "Solstice.jtpl";
   this.setTemplate(PC_BROWSER_TEMPLATE);
   
   
	this.setPageId("index");
	this.setSecurityCheck( false );

   //on set les labels static
   tpl.assign("TITLE", "Solstice +");
   
   if ( PgiUtil.getSolsticeParameter(Env.getCtx(), "Environnement").equals( "Production") )
	   tpl.assign("JNLP_FILE" , "solstice.jnlp");
   else 
	   tpl.assign("JNLP_FILE" , "solsticeTest.jnlp");
   tpl.parse("main");
   return (tpl.out());

}
%>
