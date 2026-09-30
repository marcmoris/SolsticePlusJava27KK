<%@ taglib uri="/WEB-INF/taglib.tld" prefix="mt" %>
<%@ page import="java.sql.*" %>
<%@ page import="org.compiere.util.DB" %>

<mt:securityCheck id="evaluationRendement_automaticRecall">
   <mt:pageTag id="evaluationRendement_automaticRecall" onLoad="scroll();" validationClass="solstice.custom.ER_AutomaticRecall">
   
      <%
         // On doit charger les paramètres qu'il y a présentement dans la base de données
         int parameterId = -1;
         String dayOfMonth = "";
         String assignmentState = "";
         String minimumDuration = "";
         String newHeader = "";
         String newFooter = "";
         String newCol1 = "";
         String newCol2 = "";
         String transfertHeader = "";
         String transfertFooter = "";
         String transfertCol1 = "";
         String transfertCol2 = "";
         String transfertCol3 = "";
         String noticeHeader = "";
         String noticeFooter = "";
         String noticeCol1 = "";
         String noticeCol2 = "";
         String noticeCol3 = "";
         String noticeCol4 = "";
         String recallHeader = "";
         String recallFooter = "";
         String recallCol1 = "";
         String recallCol2 = "";
         String recallCol3 = "";
         String recallCol4 = "";
         
         if("loaded".equals(request.getParameter("action")))
         {
            parameterId = Integer.parseInt(request.getParameter("parameterId"));
            dayOfMonth = request.getParameter("dayOfMonth");
            assignmentState = request.getParameter("assignmentState");
            minimumDuration = request.getParameter("minimumDuration");
            newHeader = request.getParameter("newHeader");
            newFooter = request.getParameter("newFooter");
            newCol1 = request.getParameter("newCol1");
            newCol2 = request.getParameter("newCol2");
            transfertHeader = request.getParameter("transfertHeader");
            transfertFooter = request.getParameter("transfertFooter");
            transfertCol1 = request.getParameter("transfertCol1");
            transfertCol2 = request.getParameter("transfertCol2");
            transfertCol3 = request.getParameter("transfertCol3");
            noticeHeader = request.getParameter("noticeHeader");
            noticeFooter = request.getParameter("noticeFooter");
            noticeCol1 = request.getParameter("noticeCol1");
            noticeCol2 = request.getParameter("noticeCol2");
            noticeCol3 = request.getParameter("noticeCol3");
            noticeCol4 = request.getParameter("noticeCol4");
            recallHeader = request.getParameter("recallHeader");
            recallFooter = request.getParameter("recallFooter");
            recallCol1 = request.getParameter("recallCol1");
            recallCol2 = request.getParameter("recallCol2");
            recallCol3 = request.getParameter("recallCol3");
            recallCol4 = request.getParameter("recallCol4");
         }
         else
         {
         
            PreparedStatement stmt = DB.prepareStatement(
               "select P_ER_Parameter_ID," +
               "       DayOfMonth," +
               "	     P_Assignment_State_ID," +
               "       MinimumDuration," +
               "       isnull(NewHeader, '') as NewHeader," +
               "       isnull(NewFooter, '') as NewFooter," +
               "       isnull(NewCol1, '') as NewCol1," +
               "       isnull(NewCol2, '') as NewCol2," +
               "       isnull(TransfertHeader, '') as TransfertHeader," +
               "       isnull(TransfertFooter, '') as TransfertFooter," +
               "       isnull(TransfertCol1, '') as TransfertCol1," +
               "       isnull(TransfertCol2, '') as TransfertCol2," +
               "       isnull(TransfertCol3, '') as TransfertCol3," +
               "       isnull(NoticeHeader, '') as NoticeHeader," +
               "       isnull(NoticeFooter, '') as NoticeFooter," +
               "       isnull(NoticeCol1, '') as NoticeCol1," +
               "       isnull(NoticeCol2, '') as NoticeCol2," +
               "       isnull(NoticeCol3, '') as NoticeCol3," +
               "       isnull(NoticeCol4, '') as NoticeCol4," +
               "       isnull(RecallHeader, '') as RecallHeader," +
               "       isnull(RecallFooter, '') as RecallFooter," +
               "       isnull(RecallCol1, '') as RecallCol1," +
               "       isnull(RecallCol2, '') as RecallCol2," +
               "       isnull(RecallCol3, '') as RecallCol3," +
               "       isnull(RecallCol4, '') as RecallCol4" +
               "  from P_ER_Parameter");
               
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
               parameterId = rs.getInt("P_ER_Parameter_ID");
               dayOfMonth = rs.getString("DayOfMonth");
               assignmentState = rs.getString("P_Assignment_State_ID");
               minimumDuration = rs.getString("MinimumDuration");
               newHeader = rs.getString("NewHeader");
               newFooter = rs.getString("NewFooter");
               newCol1 = rs.getString("NewCol1");
               newCol2 = rs.getString("NewCol2");
               transfertHeader = rs.getString("TransfertHeader");
               transfertFooter = rs.getString("TransfertFooter");
               transfertCol1 = rs.getString("TransfertCol1");
               transfertCol2 = rs.getString("TransfertCol2");
               transfertCol3 = rs.getString("TransfertCol3");
               noticeHeader = rs.getString("NoticeHeader");
               noticeFooter = rs.getString("NoticeFooter");
               noticeCol1 = rs.getString("NoticeCol1");
               noticeCol2 = rs.getString("NoticeCol2");
               noticeCol3 = rs.getString("NoticeCol3");
               noticeCol4 = rs.getString("NoticeCol4");
               recallHeader = rs.getString("RecallHeader");
               recallFooter = rs.getString("RecallFooter");
               recallCol1 = rs.getString("RecallCol1");
               recallCol2 = rs.getString("RecallCol2");
               recallCol3 = rs.getString("RecallCol3");
               recallCol4 = rs.getString("RecallCol4");
            }
            
            rs.close();
            stmt.close();
            
         }
      %>
   
      <script>
   		// On ajuste la valeur du scroll à chaque interval
   		function scrollListener()
   		{
   			// On vérifie si on utilise ie ou un autre navigateur
   			if(document.body.scrollTop != null)
   				document.getElementById("scroll").value = document.body.scrollTop;
   			else
   				document.getElementById("scroll").value = window.pageYOffset;
   		}
   		
   		// On récupère la position de l'ancien post et on part le listener
   		function scroll()
   		{
   			// On vérifie si on utilise ie ou un autre navigateur
   			if(document.body.scrollTop != null)
   				document.body.scrollTop = <%= request.getParameter("scroll") != null && !request.getParameter("scroll").equals("") ? request.getParameter("scroll") : "0" %>;
   			else
   				window.pageYOffset = <%= request.getParameter("scroll") != null && !request.getParameter("scroll").equals("") ? request.getParameter("scroll") : "0" %>;
   			setInterval("scrollListener()", 1);
   		}
   		
         function validateForm()
         {
            // On doit s'assurer que la durée minimum soit un chiffre non null
            var v = document.getElementById("minimumDuration").value;
            if(v == "")
            {
               alert("La durée minimum ne peut pas être null");
               return false;
            }
            
            for(var i = 0; i < v.length; i++)
            {
               if(v.charAt(i) < '0' || v.charAt(i) > '9')
               {
                  alert("La durée minimum doit être un chiffre valide");
                  return false;
               }
            }
            
            // On doit également vérifier que le nom des rapports et leur paramètre
            // ne sont pas null
            if(document.getElementById("workerProcessGES").value == ""
               || document.getElementById("workerProcessOUV").value == ""
               || document.getElementById("workerProcessPTB").value == ""
               || document.getElementById("workerProcessParaGES").value == ""
               || document.getElementById("workerProcessParaOUV").value == ""
               || document.getElementById("workerProcessParaPTB").value == "")
            {
               alert("Vous devez obligatoirement saisir un nom pour les rapports et leur paramètre");
               return false;
            }
            
            return true;
         }
         
         function enregistrerParametres()
         {
            if(validateForm())
            {
               document.getElementById("action").value = "enregistrer";
               document.mainForm.submit();
            }
         }
      </script>
      <form name="mainForm" id="mainForm" method="post">
   		<input type="hidden" name="scroll" />
   	   <p class="sectitle">Rappel automatique</p>
         <input type="hidden" name="parameterId" id="parameterId" value="<%= parameterId %>" />
         <input type="hidden" name="action" id="action" value="loaded" />
         <table border="0" cellspacing="1" cellpadding="1" width="100%">
            <tr>
               <td class="tdlabel">Jour du lancement</td>
               <td class="tdfield"> 
                  <select name="dayOfMonth" id="dayOfMonth">
                     <option value="1"<%= "1".equals(dayOfMonth) ? " selected=\"selected\"" : "" %>>1</option>
                     <option value="2"<%= "2".equals(dayOfMonth) ? " selected=\"selected\"" : "" %>>2</option>
                     <option value="3"<%= "3".equals(dayOfMonth) ? " selected=\"selected\"" : "" %>>3</option>
                     <option value="4"<%= "4".equals(dayOfMonth) ? " selected=\"selected\"" : "" %>>4</option>
                     <option value="5"<%= "5".equals(dayOfMonth) ? " selected=\"selected\"" : "" %>>5</option>
                     <option value="6"<%= "6".equals(dayOfMonth) ? " selected=\"selected\"" : "" %>>6</option>
                     <option value="7"<%= "7".equals(dayOfMonth) ? " selected=\"selected\"" : "" %>>7</option>
                     <option value="8"<%= "8".equals(dayOfMonth) ? " selected=\"selected\"" : "" %>>8</option>
                     <option value="9"<%= "9".equals(dayOfMonth) ? " selected=\"selected\"" : "" %>>9</option>
                     <option value="10"<%= "10".equals(dayOfMonth) ? " selected=\"selected\"" : "" %>>10</option>
                     <option value="11"<%= "11".equals(dayOfMonth) ? " selected=\"selected\"" : "" %>>11</option>
                     <option value="12"<%= "12".equals(dayOfMonth) ? " selected=\"selected\"" : "" %>>12</option>
                     <option value="13"<%= "13".equals(dayOfMonth) ? " selected=\"selected\"" : "" %>>13</option>
                     <option value="14"<%= "14".equals(dayOfMonth) ? " selected=\"selected\"" : "" %>>14</option>
                     <option value="15"<%= "15".equals(dayOfMonth) ? " selected=\"selected\"" : "" %>>15</option>
                     <option value="16"<%= "16".equals(dayOfMonth) ? " selected=\"selected\"" : "" %>>16</option>
                     <option value="17"<%= "17".equals(dayOfMonth) ? " selected=\"selected\"" : "" %>>17</option>
                     <option value="18"<%= "18".equals(dayOfMonth) ? " selected=\"selected\"" : "" %>>18</option>
                     <option value="19"<%= "19".equals(dayOfMonth) ? " selected=\"selected\"" : "" %>>19</option>
                     <option value="20"<%= "20".equals(dayOfMonth) ? " selected=\"selected\"" : "" %>>20</option>
                     <option value="21"<%= "21".equals(dayOfMonth) ? " selected=\"selected\"" : "" %>>21</option>
                     <option value="22"<%= "22".equals(dayOfMonth) ? " selected=\"selected\"" : "" %>>22</option>
                     <option value="23"<%= "23".equals(dayOfMonth) ? " selected=\"selected\"" : "" %>>23</option>
                     <option value="24"<%= "24".equals(dayOfMonth) ? " selected=\"selected\"" : "" %>>24</option>
                     <option value="25"<%= "25".equals(dayOfMonth) ? " selected=\"selected\"" : "" %>>25</option>
                     <option value="26"<%= "26".equals(dayOfMonth) ? " selected=\"selected\"" : "" %>>26</option>
                     <option value="27"<%= "27".equals(dayOfMonth) ? " selected=\"selected\"" : "" %>>27</option>
                     <option value="0"<%= "0".equals(dayOfMonth) ? " selected=\"selected\"" : "" %>>Dernier jour du mois</option>
                  </select>
               </td>
            </tr>
            <tr>
               <td class="tdlabel">État de l'affectation</td>
               <td class="tdfield"> 
                  	<mt:comboBoxTag id="assignmentState" selectedValue="<%= assignmentState %>" >
						select P_Assignment_State.P_Assignment_State_ID,
                                  rtrim(P_Assignment_State.Value) + ' ' + rtrim(P_Assignment_State.Name) as Name
                    	from P_Assignment_State
                      	--Order by P_Employee.Name				    
                      	</mt:comboBoxTag>
               </td>
            </tr>
            <tr>
               <td class="tdlabel">Durée minimum de l'affectation temporaire</td>
               <td class="tdfield">
                  <input type="text" name="minimumDuration" id="minimumDuration" value="<%= minimumDuration %>" />
               </td>
            </tr>
         </table>
         <br />
   	   <mt:switch id="distribution" leftSufix="L" rightSufix="R"
   	      leftInitialize="solstice.custom.ER_AutomaticRecall.getDistributionBookletToInclude"
   	      rightInitialize="solstice.custom.ER_AutomaticRecall.getDistributionBookletToExclude"
   	      leftTitle="Code de distribution de livret"
   	      rightTitle="Code de distribution de livret à exclure du rappel automatique" />
   	   <br />
   	   <table border="0" cellspacing="1" cellpadding="1" width="100%">
   	      <tr>
   	         <td class="tdlabel">Nouvel employé :</td>
   	         <td class="tdlabel">Entête</td>
   	         <td class="tdfield"><input type="text" name="newHeader" id="newHeader" maxlength="500" size="100" value="<%= newHeader %>" /></td>
   	      </tr>
   	      <tr>
   	         <td class="tdlabel">&nbsp;</td>
   	         <td class="tdlabel"><nobr>Pied de page</nobr></td>
   	         <td class="tdfield"><textarea name="newFooter" id="newFooter" maxlength="500" cols="100" rows="3"><%= newFooter %></textarea></td>
   	      </tr>
   	      <tr>
   	         <td class="tdlabel">&nbsp;</td>
   	         <td class="tdlabel">Colonne 1</td>
   	         <td class="tdfield"><input type="text" name="newCol1" id="newCol1" maxlength="50" size="100" value="<%= newCol1 %>" /></td>
   	      </tr>
   	      <tr>
   	         <td class="tdlabel">&nbsp;</td>
   	         <td class="tdlabel">Colonne 2</td>
   	         <td class="tdfield"><input type="text" name="newCol2" id="newCol2" maxlength="50" size="100" value="<%= newCol2 %>" /></td>
   	      </tr>
   	      <tr>
   	         <td colspan="3">&nbsp;</td>
   	      </tr>
   	      <tr>
   	         <td class="tdlabel"><nobr>Transfert d'employé :</nobr></td>
   	         <td class="tdlabel">Entête</td>
   	         <td class="tdfield"><input type="text" name="transfertHeader" id="transfertHeader" maxlength="500" size="100" value="<%= transfertHeader %>" /></td>
   	      </tr>
   	      <tr>
   	         <td class="tdlabel">&nbsp;</td>
   	         <td class="tdlabel">Pied de page</td>
   	         <td class="tdfield"><textarea name="transfertFooter" id="transfertFooter" maxlength="500" cols="100" rows="3"><%= transfertFooter %></textarea></td>
   	      </tr>
   	      <tr>
   	         <td class="tdlabel">&nbsp;</td>
   	         <td class="tdlabel">Colonne 1</td>
   	         <td class="tdfield"><input type="text" name="transfertCol1" id="transfertCol1" maxlength="50" size="100" value="<%= transfertCol1 %>" /></td>
   	      </tr>
   	      <tr>
   	         <td class="tdlabel">&nbsp;</td>
   	         <td class="tdlabel">Colonne 2</td>
   	         <td class="tdfield"><input type="text" name="transfertCol2" id="transfertCol2" maxlength="50" size="100" value="<%= transfertCol2 %>" /></td>
   	      </tr>
   	      <tr>
   	         <td class="tdlabel">&nbsp;</td>
   	         <td class="tdlabel">Colonne 3</td>
   	         <td class="tdfield"><input type="text" name="transfertCol3" id="transfertCol3" maxlength="50" size="100" value="<%= transfertCol3 %>" /></td>
   	      </tr>
   	      <tr>
   	         <td colspan="3">&nbsp;</td>
   	      </tr>
   	      <tr>
   	         <td class="tdlabel">Avis :</td>
   	         <td class="tdlabel">Entête</td>
   	         <td class="tdfield"><input type="text" name="noticeHeader" id="noticeHeader" maxlength="500" size="100" value="<%= noticeHeader %>" /></td>
   	      </tr>
   	      <tr>
   	         <td class="tdlabel">&nbsp;</td>
   	         <td class="tdlabel">Pied de page</td>
   	         <td class="tdfield"><textarea name="noticeFooter" id="noticeFooter" maxlength="500" cols="100" rows="3"><%= noticeFooter %></textarea></td>
   	      </tr>
   	      <tr>
   	         <td class="tdlabel">&nbsp;</td>
   	         <td class="tdlabel">Colonne 1</td>
   	         <td class="tdfield"><input type="text" name="noticeCol1" id="noticeCol1" maxlength="50" size="100" value="<%= noticeCol1 %>" /></td>
   	      </tr>
   	      <tr>
   	         <td class="tdlabel">&nbsp;</td>
   	         <td class="tdlabel">Colonne 2</td>
   	         <td class="tdfield"><input type="text" name="noticeCol2" id="noticeCol2" maxlength="50" size="100" value="<%= noticeCol2 %>" /></td>
   	      </tr>
   	      <tr>
   	         <td class="tdlabel">&nbsp;</td>
   	         <td class="tdlabel">Colonne 3</td>
   	         <td class="tdfield"><input type="text" name="noticeCol3" id="noticeCol3" maxlength="50" size="100" value="<%= noticeCol3 %>" /></td>
   	      </tr>
   	      <tr>
   	         <td class="tdlabel">&nbsp;</td>
   	         <td class="tdlabel">Colonne 4</td>
   	         <td class="tdfield"><input type="text" name="noticeCol4" id="noticeCol4" maxlength="50" size="100" value="<%= noticeCol4 %>" /></td>
   	      </tr>
   	      <tr>
   	         <td colspan="3">&nbsp;</td>
   	      </tr>
   	      <tr>
   	         <td class="tdlabel">Rappel :</td>
   	         <td class="tdlabel">Entête</td>
   	         <td class="tdfield"><input type="text" name="recallHeader" id="recallHeader" maxlength="500" size="100" value="<%= recallHeader %>" /></td>
   	      </tr>
   	      <tr>
   	         <td class="tdlabel">&nbsp;</td>
   	         <td class="tdlabel">Pied de page</td>
   	         <td class="tdfield"><textarea name="recallFooter" id="recallFooter" maxlength="500" cols="100" rows="3"><%= recallFooter %></textarea></td>
   	      </tr>
   	      <tr>
   	         <td class="tdlabel">&nbsp;</td>
   	         <td class="tdlabel">Colonne 1</td>
   	         <td class="tdfield"><input type="text" name="recallCol1" id="recallCol1" maxlength="50" size="100" value="<%= recallCol1 %>" /></td>
   	      </tr>
   	      <tr>
   	         <td class="tdlabel">&nbsp;</td>
   	         <td class="tdlabel">Colonne 2</td>
   	         <td class="tdfield"><input type="text" name="recallCol2" id="recallCol2" maxlength="50" size="100" value="<%= recallCol2 %>" /></td>
   	      </tr>
   	      <tr>
   	         <td class="tdlabel">&nbsp;</td>
   	         <td class="tdlabel">Colonne 3</td>
   	         <td class="tdfield"><input type="text" name="recallCol3" id="recallCol3" maxlength="50" size="100" value="<%= recallCol3 %>" /></td>
   	      </tr>
   	      <tr>
   	         <td class="tdlabel">&nbsp;</td>
   	         <td class="tdlabel">Colonne 4</td>
   	         <td class="tdfield"><input type="text" name="recallCol4" id="recallCol4" maxlength="50" size="100" value="<%= recallCol4 %>" /></td>
   	      </tr>
   	   </table>
   	   <%
   	      // Ajout d'une section spéciale pour saisir le nom des rapports ainsi
   	      // que le nom du paramètre qui leur est associé pour imprimer les
   	      // évaluation de rendement. Ces informations proviennent de trois
   	      // workerprocess se trouvant dans la table AD_Process et AD_Process_Para.
   	      String workerProcessGES = "";
   	      String workerProcessOUV = "";
   	      String workerProcessPTB = "";
   	      String workerProcessParaGES = "";
   	      String workerProcessParaOUV = "";
   	      String workerProcessParaPTB = "";
   	      
   	      if("loaded".equals(request.getParameter("action")))
   	      {
      	      workerProcessGES = request.getParameter("workerProcessGES");
      	      workerProcessOUV = request.getParameter("workerProcessOUV");
      	      workerProcessPTB = request.getParameter("workerProcessPTB");
      	      workerProcessParaGES = request.getParameter("workerProcessParaGES");
      	      workerProcessParaOUV = request.getParameter("workerProcessParaOUV");
      	      workerProcessParaPTB = request.getParameter("workerProcessParaPTB");
   	      }
   	      else
   	      {
      	      PreparedStatement stmt = DB.prepareStatement(
      	         "select isnull( ( select top 1 Value from AD_Process where Name = 'WorkerProcessGES' ), '' ) as workerProcessGES," +
                  "       isnull( ( select top 1 Value from AD_Process where Name = 'WorkerProcessOUV' ), '' ) as workerProcessOUV," +
                  "       isnull( ( select top 1 Value from AD_Process where Name = 'WorkerProcessPTB' ), '' ) as workerProcessPTB," +
                  "       isnull( ( select top 1 AD_Process_Para.ColumnName" +
                  "                   from AD_Process_Para" +
                  "                        inner join AD_Process" +
                  "                           on AD_Process.AD_Process_ID = AD_Process_Para.AD_Process_ID" +
                  "                  where AD_Process.Name = 'WorkerProcessGES'" +
                  "                  order by SeqNo ), '' ) as workerProcessParaGES," +
                  "       isnull( ( select top 1 AD_Process_Para.ColumnName" +
                  "                   from AD_Process_Para" +
                  "                        inner join AD_Process" +
                  "                           on AD_Process.AD_Process_ID = AD_Process_Para.AD_Process_ID" +
                  "                  where AD_Process.Name = 'WorkerProcessOUV'" +
                  "                  order by SeqNo ), '' ) as workerProcessParaOUV," +
                  "       isnull( ( select top 1 AD_Process_Para.ColumnName" +
                  "                   from AD_Process_Para" +
                  "                        inner join AD_Process" +
                  "                           on AD_Process.AD_Process_ID = AD_Process_Para.AD_Process_ID" +
                  "                  where AD_Process.Name = 'WorkerProcessPTB'" +
                  "                  order by SeqNo ), '' ) as workerProcessParaPTB");
      	         
      	      ResultSet rs = stmt.executeQuery();
      	      
      	      if(rs.next())
      	      {
         	      workerProcessGES = rs.getString("workerProcessGES");
         	      workerProcessOUV = rs.getString("workerProcessOUV");
         	      workerProcessPTB = rs.getString("workerProcessPTB");
         	      workerProcessParaGES = rs.getString("workerProcessParaGES");
         	      workerProcessParaOUV = rs.getString("workerProcessParaOUV");
         	      workerProcessParaPTB = rs.getString("workerProcessParaPTB");
      	      }
      	      
      	      rs.close();
      	      stmt.close();
   	      }
   	   %>
   	   <p class="sectitle">Rapports d'évaluation du rendement</p>
   	   <table width="100%" cellspacing="1" cellpadding="1" border="0">
   	      <tr>
   	         <td class="tdlabel">&nbsp;</td>
   	         <td class="tdlabel">Nom du rapport</td>
   	         <td class="tdlabel">Nom du paramètre</td>
   	      </tr>
   	      <tr>
   	         <td class="tdlabel">Évaluation du rendement des gestionnaires</td>
   	         <td class="tdfield"><input type="text" name="workerProcessGES" id="workerProcessGES" value="<%= workerProcessGES %>" maxlength="80" /></td>
   	         <td class="tdfield"><input type="text" name="workerProcessParaGES" id="workerProcessParaGES" value="<%= workerProcessParaGES %>" maxlength="40" /></td>
   	      </tr>
   	      <tr>
   	         <td class="tdlabel">Évaluation du rendement du personnel professionnel technique de bureau</td>
   	         <td class="tdfield"><input type="text" name="workerProcessPTB" id="workerProcessPTB" value="<%= workerProcessPTB %>" maxlength="80" /></td>
   	         <td class="tdfield"><input type="text" name="workerProcessParaPTB" id="workerProcessParaPTB" value="<%= workerProcessParaPTB %>" maxlength="40" /></td>
   	      </tr>
   	      <tr>
   	         <td class="tdlabel">Évaluation du rendement du personnel ouvrier</td>
   	         <td class="tdfield"><input type="text" name="workerProcessOUV" id="workerProcessOUV" value="<%= workerProcessOUV %>" maxlength="80" /></td>
   	         <td class="tdfield"><input type="text" name="workerProcessParaOUV" id="workerProcessParaOUV" value="<%= workerProcessParaOUV %>" maxlength="40" /></td>
   	      </tr>
   	   </table>
   	   <p class="sectitle">Conventions collectives pouvant avoir deux évaluations probatoires</p>
   	   <mt:switch id="collectiveLabour" leftSufix="L" rightSufix="R"
   	      leftInitialize="solstice.custom.ER_AutomaticRecall.getCollectiveLabourSingleER"
   	      rightInitialize="solstice.custom.ER_AutomaticRecall.getCollectiveLabourDoubleER"
   	      leftTitle="Évaluation probatoire simple"
   	      rightTitle="Évaluation probatoire double" />
   	   <p align="right">
   	      <input type="button" value="Enregistrer les paramètres" onclick="enregistrerParametres();" />
   	   </p>
      </form>
   </mt:pageTag>
</mt:securityCheck>