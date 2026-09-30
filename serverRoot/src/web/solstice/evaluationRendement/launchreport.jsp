<%@ page import="java.sql.*" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="org.compiere.util.DB" %>
<%@ page import="org.compiere.util.Env" %>
<%@ page import="org.compiere.model.MPInstance" %>
<%@ page import="org.compiere.model.MPInstancePara" %>

<%!
   /**
    * Retourne l'id du processus attaché au type d'évaluation passé en paramètre
    */
   private int getProcessId(String evaluationType) throws SQLException
   {
      int processId = 0;
      
      PreparedStatement stmt = DB.prepareStatement("select AD_Process_ID from AD_Process where Name = 'WorkerProcess" + evaluationType + "'");
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
         processId = rs.getInt("AD_Process_ID");
         
      rs.close();
      stmt.close();
      
      return processId;
   }
%>

<%!
   /**
    * Cette méthode retourne le nom du paramètre utilisé par le process
    */
   private String getParameterName(int processId) throws SQLException
   {
      String parameterName = null;
      
      PreparedStatement stmt = DB.prepareStatement("select top 1 ColumnName from AD_Process_Para where AD_Process_ID = " + processId);
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
         parameterName = rs.getString("ColumnName");
         
      rs.close();
      stmt.close();
      
      return parameterName;
   }
%>

<%
   /*
    * Cette page sert uniquement à préparer les rapports d'évaluation de rendement. Elle doit
    * obligatoirement être appelée avec un evaluationId et un evaluationType en paramètre. Dans
    * cette page, on va générer un AD_PInstance_ID à partir d'un des trois workerprocess qui
    * ont été définit dans AD_Process.
    */
    
   String evaluationId = request.getParameter("evaluationId");
   String evaluationType = request.getParameter("evaluationType");
   
   if(evaluationId == null || evaluationType == null)
   {
      response.sendError(403);
   }
   else
   {
      // On récupère d'abord l'id du workerprocess selon le type d'évaluation. Si on ne trouve pas
      // l'id, on lance une exception pour dire que le type n'existe pas.
      int processId = getProcessId(evaluationType);
      if(processId == 0)
         throw new Exception("Il n'existe pas de rapport pour ce type d'évaluation");
         
      // On crée maintenant le nouveau process
      MPInstance newInstance = new MPInstance(Env.getCtx(), -1, null);
      newInstance.setAD_Process_ID(processId);
      newInstance.setRecord_ID(0);
      if(!newInstance.save())
         throw new Exception("Erreur dans l'enregistrement d'un nouveau PInstance");
         
      // On crée ensuite le paramètre
      String parameterName = getParameterName(processId);
      if(parameterName == null)
         throw new Exception("Impossible de trouver le paramètre");
      
      MPInstancePara instancePara = new MPInstancePara(newInstance, 0);
      instancePara.setAD_PInstance_ID(newInstance.getAD_PInstance_ID());
      instancePara.setParameterName(parameterName);
      instancePara.setP_Number(new BigDecimal(Double.parseDouble(evaluationId)));
      if(!instancePara.save())
         throw new Exception("Erreur dans l'enregistrement du PInstance_Para");
      
      response.sendRedirect("/solstice/Crystal/reports.jsp?AD_PInstance_ID=" + newInstance.getAD_PInstance_ID());
   }
%>