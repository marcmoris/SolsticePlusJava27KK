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
<%@ page import="solstice.custom.IUserInfo" %>
<%@ page import="solstice.utils.PgiUtil" %>
<%@ page import="solstice.web.PageWebSolstice" %>
<%@ page import="solstice.model.P_Absence" %>
<%@ page import="solstice.model.P_Absence_Detail" %>
<%@ page import="solstice.model.P_Absence_Status" %>
<%@ page import="solstice.model.P_Employee" %>
<%@ page import="solstice.model.P_Assignment" %>
<%@ page import="solstice.model.P_Post" %>
<%@ page import="solstice.model.P_Period" %>
<%@ page extends="solstice.web.PageWebSolstice" %>

<%!public String GeneratePage( ) throws Exception 
{
   String PC_BROWSER_TEMPLATE =  "ListGain.jtpl";
   this.setTemplate(PC_BROWSER_TEMPLATE);
   
   //on set les labels static
   tpl.assign("TITLE", "SIQ - Descriptif des codes de gain");
   tpl.assign("LabelCode", "Code");
   tpl.assign("LabelNom", "Nom");
   tpl.assign("LabelDesc", "Description");
   tpl.assign("LabelFilter", "Filtres");
   tpl.assign("LabelChamp", "Filtrer par &nbsp;&nbsp;");
   tpl.assign("LabelParam", "Recherche");
   
   String sqlFiltre = "";
   String filtreValue = "";
   String searchValue = "";
   //On utilise cet array pour determiner quel item est sélectionner dans le dropdown
   String[] arraySelected = {"","","",""};
  //On ajoute un where s'il y a un filtre demander par l'utilisateur
	if (request.getParameter("ddlFiltre") != null && request.getParameter("txbRecherche") != null )
   {
      filtreValue = request.getParameter("ddlFiltre");
      searchValue = request.getParameter("txbRecherche");
      if (filtreValue.equals("code"))
      {
         sqlFiltre += " And value like '%" + searchValue + "%' ";
         arraySelected[1] = "Selected";
      }
      else if (filtreValue.equals("name"))
      {
         sqlFiltre += " And name like '%" + searchValue + "%' ";
         arraySelected[2]  = "Selected";
      }
      else if (filtreValue.equals("desc"))
      {
         sqlFiltre += " And description like '%" + searchValue + "%' ";
         arraySelected[3] = "Selected";
      }
      else
      {
         searchValue = "";
         sqlFiltre = "";
         arraySelected[0] = "Selected";         
      }
   }
   else
   {
      sqlFiltre = "";
      arraySelected[0] = "Selected";
      
   }
   
  //on set le dropdown du filtre
   String ddlfiltre = "<select name=\"ddlFiltre\" id=\"ddlFiltre\" style='width: 180px;'  onKeyPress=\"autoSelect(this);\">";
          ddlfiltre += "<option " + arraySelected[0] + ">Sélectionner le filtre</option> \n";
			 ddlfiltre += "<option " + arraySelected[1] + " value='code'>Code</option> \n";
			 ddlfiltre += "<option " + arraySelected[2] + " value='name'>Nom</option> \n";
			 ddlfiltre += "<option " + arraySelected[3] + " value='desc'>Description</option> \n";
		    ddlfiltre += "</select>";
		   
   tpl.assign("ddlfiltre", ddlfiltre);	
   
   //On crée le textbox 
   String txbRecherche = "<input name=\"txbRecherche\" id=\"txbRecherche\" value='" + searchValue + "' style='width: 180px;'/>";
   tpl.assign("txbRecherche", txbRecherche);	
	
   //On crée la requête, qui va chercher les codes de gain	   
   String sql = " select " +
                " value, " +
                " (rtrim(name) + ' (' + rtrim(value) + ')') as name, " +
                " ltrim(description) as description " +
                " from P_Gain " +
                " where " +
                "     IsActive = 'Y' " +
                " And Visible = 'Y' "; 

  //   if(!((solstice.custom.IUserInfo)session.getAttribute("userInfo")).hasPolicy("livradmingainlist")) 
  //{ 
  //		sql += " And Accessible = 'Y' ";
  //	}
	
   sql += (! sqlFiltre.equals(""))? sqlFiltre : "";
	
   //On sélection le bon order by selon le choix de l'utilisateur
	if (request.getParameter("tri") != null)
	{
      if (request.getParameter("tri").equals("c"))
      {
         sql += " order by value asc ";
      }
      else if (request.getParameter("tri").equals("d"))
      {
         sql += " order by description asc ";
      }
      else
      {
         sql += " order by name asc ";
      }	
   }
   else
   {
      sql += " order by name asc ";
   }
  
   //ON exécute la requête et on set les var qui serviront aux templates pour la construction du tableau dynamique
   try
   {
   	PreparedStatement pstmt = null;
   	pstmt = DB.prepareStatement (sql, null);
   	ResultSet rs = pstmt.executeQuery();
   	boolean alt = false;
   	while (rs != null && rs.next())
   	{
      	tpl.assign("ValueCode",rs.getString("value"));
      	tpl.assign("ValueName",rs.getString("name"));
      	tpl.assign("ValueDesc",rs.getString("description"));
		   tpl.assign("CELLCLASS",((alt)? "tdfieldalt" : "tdfield"));
		   alt = !alt;    
		   //Important pour créer un array des paramètres set si-dessus  	
      	tpl.parse("main.line");
      }	
		rs.close ();
		pstmt.close ();
		pstmt = null;      
   }
	catch (Exception e)
	{
		System.out.println("* ERROR * Read P_Gain sql " + sql + "Exception :" + e);
	}   
   
   tpl.parse("main");
   return (tpl.out());

}
%>
