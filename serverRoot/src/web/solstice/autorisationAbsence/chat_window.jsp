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
<%@ page import="org.compiere.model.MChat" %>
<%@ page import="org.compiere.model.MChatEntry" %>
<%@ page import="org.compiere.model.MUser" %>
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
   this.setTemplate(((request.getParameter("compact") != null && request.getParameter("compact").equals("true")) ? "chat_window_compact.jtpl" : "chat_window.jtpl"));
   
   
   this.setPageId("remplir");
   this.setSecurityCheck( true );
   
   
   // on s'assure des droits.
   if ( ! SecurityCheck() )
   {
      response.sendError(403); 
   }
   
   
   String action = "";
   int selectedEntry = -1;
   int userId = ((solstice.custom.IUserInfo)session.getAttribute("userInfo")).getUserId();
   int chatId = -1;
   int tableId = -1;
   int recordId = -1;
 
   // si le chat id est défini on le prend
   if(request.getParameter("chatId") != null && !request.getParameter("chatId").equals("-1"))
   {
      chatId = Integer.parseInt(request.getParameter("chatId"));
   }
   // sinon on va le chercher ailleurs
   else
   {
      // si on a un tableId et un recordId il est possible d'aller chercher le chatID
      if(request.getParameter("tableId") != null && request.getParameter("recordId")!=null)
      {
         tableId = Integer.parseInt(request.getParameter("tableId"));
         recordId = Integer.parseInt(request.getParameter("recordId"));
         
         String sql = "SELECT  CM_CHAT_ID FROM CM_CHAT " +
                      " WHERE AD_TABLE_ID = ?"  + 
                      "   AND RECORD_ID = ?" ;
                      
         PreparedStatement pstmt = null;
         try
         {
            pstmt = DB.prepareStatement (sql, null);
            pstmt.setInt (1, tableId);
            pstmt.setInt (2, recordId);
            ResultSet rs = pstmt.executeQuery ();
            while (rs.next ())
            {
               chatId = rs.getInt("CM_CHAT_ID");
            }
            rs.close ();
            pstmt.close ();
            pstmt = null;
         }
         catch (Exception e)
         {
            System.out.println("Chat_window SQL ERROR : " +  sql + e);
         }
      }
      // sinon, pas d'identifiant, on lance une erreur
      else
      {
         response.sendError(403); 
      }
   }
   
   
   // Traitement des actions en postback
   if(request.getMethod().equals("POST"))
   {
      if(request.getParameter("action")!=null)
      {
         action = request.getParameter("action");   
      }  
      if(request.getParameter("entryid") != null && !request.getParameter("entryid").equals(""))
      {
         selectedEntry = Integer.parseInt(request.getParameter("entryid"));
      }
      // Enregistrement
      if(action.equals("save"))
      {
         String comment = request.getParameter("comment");
         // si nouvelle entrée on la crée et l'enregistre
         if(selectedEntry == -1)
         {
            // avant d'enregistrer l'entrée on s'assure que le chat a été créé
            if(chatId == -1)
            {
               try
               {
                  MChat chat = new MChat(ctx, -1 , null);
                  chat.setAD_Table_ID (tableId);
                  chat.setRecord_ID (recordId);
                  chat.setDescription ("Commentaire au livret de temps");
                  chat.setConfidentialType(MChat.CONFIDENTIALTYPE_PublicInformation);
                  chat.setModerationType(MChat.MODERATIONTYPE_NotModerated);
                  chat.save();
                  chatId = chat.getCM_Chat_ID();
               }
               catch (Exception e)
               {
                  System.out.println("Erreur de save : " +  e);   
               }
            }
            
            MChatEntry entry = new MChatEntry(new MChat(ctx, chatId , null), comment);
            entry.save();
         }
         // sinon on la met à jour
         else
         {
            MChatEntry entry = new MChatEntry(ctx, selectedEntry, null);
            entry.setCharacterData( PgiUtil.convertHTMLString( comment ));
            entry.save();
         }
         
         // on remet le selectedEntry à -1   
         selectedEntry = -1;
      }
      else if (action.equals("delete"))
      {
         // on supprime l'entrée
         MChatEntry entry = new MChatEntry(ctx, selectedEntry, null);
         entry.delete(false);
         
         // on remet le selectedEntry à -1   
         selectedEntry = -1;
      }
   }
   
   //on définis les libellés statiques
   tpl.assign("TITLE", "SIQ - Notes");
   tpl.assign("LabelUserDate", "Utilisateur et Date");
   tpl.assign("LabelComment", "Note");
   tpl.assign("LabelAction", "Action");
   tpl.assign("LabelClose", "Fermer");
   tpl.assign("MsgCommentMandatory", "La note est obligatoire à l'enregistrement.");
   tpl.assign("MsgConfirmDelete", "Désirez-vous supprimer cette note?");
   tpl.assign("chatId", chatId);
   tpl.assign("tableId", tableId);
   tpl.assign("recordId", recordId);
   
   tpl.assign("CurrentUser", ((solstice.custom.IUserInfo)session.getAttribute("userInfo")).getEmployeeName());
   tpl.assign("CurrentDate", "Nouveau commentaire");
   if(selectedEntry == -1)
   {
      tpl.assign("CurrentComment", "<textarea rows=\"5\" style=\"width:100%\" name=\"comment\"></textarea>");
      tpl.assign("CurrentAction", "<input type=\"button\" value=\"Enregistrer\" onclick=\"javascript:save('-1');\">");
   }
   else
   {
      tpl.assign("CurrentComment", "<a href=\"javascript:modify('-1')\"><i>Cliquer ici pour éditer une nouvelle note.</i></a>");
   }
   
   
   try
   {
      MChat chat = new MChat(ctx, chatId, null); 
      MChatEntry[] entries = chat.getEntries(true);
      
      for(int i=0; i<entries.length ; i++)
      {
         MUser user = new MUser(ctx, entries[i].getUpdatedBy(), null);
         
         tpl.assign("User", user.getName());
         SimpleDateFormat f = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss" );
         
         tpl.assign("Date", f.format(entries[i].getUpdated()));
         
         if(selectedEntry == entries[i].getCM_ChatEntry_ID())
         {
            tpl.assign("Comment", "<textarea rows=\"5\" style=\"width:100%\" name=\"comment\">" + PgiUtil.convertAsciiString(entries[i].getCharacterData()) +"</textarea>");
            tpl.assign("Button", "<input type=\"button\" value=\"Enregistrer\" onclick=\"javascript:save('"+ entries[i].getCM_ChatEntry_ID() +"');\">");
         }
         else
         {
            tpl.assign("Comment", entries[i].getCharacterData());
            // si l'usager est l'auteur de l'entrée ou superuser, il peut la modifier
            if(entries[i].getUpdatedBy() == userId || userId == 0 || userId == 100)
               tpl.assign("Button", "<a href=\"javascript:modify('"+ entries[i].getCM_ChatEntry_ID() +"');\"><img src=\"images/edit.gif\" border=\"0\" alt=\"Éditer\"></a>&nbsp;<a href=\"javascript:deleteE('"+ entries[i].getCM_ChatEntry_ID() +"');\"><img src=\"images/delete.gif\" alt=\"Supprimer\" border=\"0\"></a>");
            else
               tpl.assign("Button",""); 
         }
         
         tpl.assign("EntryID", entries[i].getCM_Chat_ID());
         tpl.assign("CELLCLASS",((i%2==0)? "tdfieldalt" : "tdfield"));
         tpl.parse("main.line");
      }
   }
   catch (Exception e)
   {
      System.out.println("* ERROR * fenetre de chat web "  + "Exception :" + e);
   }   
   
   tpl.parse("main");
   return (tpl.out());

}
%>
