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
<%@ page import="solstice.model.P_Employee_Calendar" %>
<%@ page import="solstice.model.P_Post" %>
<%@ page import="solstice.model.P_Period" %>
<%@ page extends="solstice.web.PageWebSolstice" %>


<%!public String GeneratePage( ) throws Exception 
{
   String result = " ";
   
   // on s'assure des droits.
   if ( ! SecurityCheck() )
   {
//      response.sendError(403); 
   }
   
   
   // Si une demande d'eneregistrement ajax est faite
   String action = request.getParameter("action");
   
   if(action !=null && action.equals("saveAbsence"))
   {
      // Détermine si la requête d'enregistrement est sur HV ou CA
      String HvCaType = request.getParameter("HvCaType");
      
      // Détermine l'identifiant de l'absence à enregistrer
      String absenceID = request.getParameter("absenceId");
      
      // Détermine la valeur à atribuer au flag
      String val = request.getParameter("val").toLowerCase();
      
      // Si la requête de la page web est ok 
      if (HvCaType != null && absenceID != null && val !=null)
      {
         
         // Un objet d'absence est instancié
         P_Absence absence = new P_Absence(ctx, Integer.parseInt(absenceID), null);
         
         // On attribue la valeur à la bonne propriétée
         if(HvCaType.equals("HV"))
            absence.setIsFlexibleHours(Boolean.valueOf(val).booleanValue()); 
         else if (HvCaType.equals("CA"))
            absence.setisCallCenter(Boolean.valueOf(val).booleanValue());
 
         // L'absence est enregistrée
         absence.save();
      }
   }
   
   return result;
}
%>
