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
package solstice.web;

import java.sql.Timestamp;

import java.text.SimpleDateFormat;

import solstice.utils.PgiUtil;
import solstice.web.PageWebSolstice;
import solstice.custom.IUserInfo;
import org.compiere.model.MUser;
import solstice.model.P_Employee;
import java.util.TimeZone;
import org.compiere.util.TimeUtil;

public class Index extends PageWebSolstice {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String GeneratePage( ) throws Exception 
	{
		String liste = "";
		String welcome = "";
		String welcome2 = "";

		Timestamp LastConnection =  new Timestamp (System.currentTimeMillis());
		this.setPageId("index");
		this.setSecurityCheck( false );
		
		String client = PgiUtil.getSolsticeParameter( ctx, "Client").toLowerCase();
		this.setTemplate("index_" + client + ".jtpl");
		
		IUserInfo userInfo = null;
		if ( this.session != null )
			userInfo = (IUserInfo)session.getAttribute("userInfo");
		
		String webAppUrl = PgiUtil.getSolsticeParameter(ctx, "WEBAPPURL");
		if ( userInfo == null )
		{
			response.sendRedirect( webAppUrl + "login.jsp");
		}
		else
		{
			if ( ! SecurityCheck() )
				response.sendError(403); 

			P_Employee Employee = P_Employee.get( ctx, userInfo.getEmployeeId(), null);
			
			if ( Employee == null )
				  response.sendError(401);
				
			MUser user = MUser.get( ctx, Employee.getAD_User_ID());
			
			welcome = "Bienvenue " + Employee.getFirstName() + " " + Employee.getSurname();

		    TimeZone timeZone = TimeZone.getDefault(); //.getTimeZone("GMT-5");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd" ); // 'à' HH:mm:ss"
            sdf.setTimeZone(timeZone);

//	   		MSession cSession = MSession.get (ctx, request.getRemoteAddr(), request.getRemoteHost(), session.getId());
            
            if ( user.getLastContact() == null)
            {
            	user.setLastContact( new Timestamp (System.currentTimeMillis()) );
            	user.save();
            }
            LastConnection = user.getLastContact();
            
            welcome2 += " Vous avez ouvert une session la dernière fois le : " + sdf.format( LastConnection );

            if ( TimeUtil.getDaysBetween(user.getLastContact(), new Timestamp (System.currentTimeMillis()) ) != 0)
            {
                user.setLastContact(new Timestamp (System.currentTimeMillis()));
                user.save();
            }

//  	      if(userInfo.hasPolicy("EndOfYearIndexAdmin"))
//  	    	  liste = liste.concat("<li><a href=\"/solstice/timesheet/timesheet.jsp\" class=\"menu\">Feuille de temps</a></li>");
            
//	      if(userInfo.hasPolicy("EndOfYearIndexAdmin"))
//	    	  liste = liste.concat("<li><a href=\"/solstice/profile/profile.jsp\" class=\"menu\">Profil employé</a></li>");

	      if(userInfo.hasPolicy("EndOfYearIndexAdmin"))
	    	  liste = liste.concat("<li><a href=\"/solstice/profile/schedule.jsp\" class=\"menu\">Calendrier d'équipe</a></li>");

		  if(userInfo.hasPolicy("autorisationAbsence"))
			  liste = liste.concat("<li><a href=\"/solstice/autorisationAbsence/index.jsp\" class=\"menu\">Autorisation d'absence</a></li>");
		

		  if(userInfo.hasPolicy("AmomaliesManagement"))
			  liste = liste.concat("<li><a href=\"/solstice/AnomaliesManagement/admconsult.jsp\" class=\"menu\">Gestion des anomalies - Approbation des coupures de salaire et reports de temps</a></li>");

	      // L'avis de dépôt
	      if(userInfo.hasPolicy("avisDepot"))
	    	  liste = liste.concat("<li><a href=\"/solstice/avisDepot/index.jsp\" class=\"menu\">Avis de d&eacute;p&ocirc;t</a></li>");

	      // Le livret de temps
	      if(userInfo.hasPolicy("livretTemps"))
	    	  liste = liste.concat("<li><a href=\"/solstice/livretTemps/index.jsp\" class=\"menu\">Livret de temps</a></li>");

	      // Le relevé d'emploi
	      if(userInfo.hasPolicy("releveEmploi"))
	    	  liste = liste.concat("<li><a href=\"/solstice/releveEmploi/index.jsp\" class=\"menu\">Relev&eacute; d'emploi</a></li>");

	      // Les évaluations de rendement
	      if(userInfo.hasPolicy("evaluationRendement"))
	    	  liste = liste.concat("<li><a href=\"/solstice/evaluationRendement/index.jsp\" class=\"menu\">&Eacute;valuation de rendement</a></li>");

	      if(userInfo.hasPolicy("evaluationRendementPre"))
	    	  liste = liste.concat("<li><a href=\"/solstice/evaluationRendement/EvaluationPre.jsp\" class=\"menu\">Appr&eacute;ciation du rendement: Cotes pr&eacute;liminaires</a></li>");

    	  
    	  liste = liste.concat("<li><a href=\"/solstice/listperiod.jsp\" class=\"menu\">Liste des p&eacute;riodes de paie</a></li>");

	      if(userInfo.hasPolicy("ListNonBusinessDay"))
	    	  liste = liste.concat("<li><a href=\"/solstice/ListNonBusinessDay.jsp\" class=\"menu\">Liste des cong&eacute;s f&eacute;ri&eacute;s</a></li>");

	      if(userInfo.hasPolicy("EndOfYearIndex"))
	    	  liste = liste.concat("<li><a href=\"/solstice/endOfYearForm/index.jsp\" class=\"menu\">Relev&eacute;s d'imp&ocirc;t</a></li>");

	      if(userInfo.hasPolicy("EndOfYearIndexAdmin"))
	    	  liste = liste.concat("<li><a href=\"/solstice/endOfYearForm/indexAdmin.jsp\" class=\"menu\">Relev&eacute;s d'imp&ocirc;t (Administrateur)</a></li>");

/*
	      // Permis horodateur
	      if(userInfo.hasPolicy("permisHorodateur"))
	      {
		      // Pour le permis horodateur, la validation se complique un peu. Si l'utilisateur
		      // qui tente de se connecter est un employé, on doit vérifier que sa convention
		      // collective lui permette d'utiliser un horodateur
		      boolean access = true;
		      if(!userInfo.hasPolicy("permisHorodateur_skipCollectiveLabourAgr"))
		      {
			      PreparedStatement stmt = DB.prepareStatement(
			         "select assignmentParam.P_Collective_Labour_Agr_ID" +
	             "  from P_Assignment assignment" +
	             "  join P_Assignment_Param assignmentParam" +
	             "    on assignmentParam.P_Assignment_ID = assignment.P_Assignment_ID" +
	             " where assignment.P_Employee_ID = " + userInfo.getEmployeeId() +
	             "   and assignment.IsActive = 'Y'" +
	             "   and assignmentParam.IsActive = 'Y'" +
	             "   and getdate() between isnull( assignment.StartDate, getdate() ) and isnull( assignment.EndDate, getdate() )" +
	             "   and getdate() between isnull( assignmentParam.EffectIn, getdate() ) and isnull( assignmentParam.EffectTo, getdate() )" +
	             "   order by AssignmentType",null);
			         
			      ResultSet rs = stmt.executeQuery();
			      
			      access = rs.next() ? solstice.model.P_PunchCard_Collective_Labour_Agr.canAccess(rs.getInt("P_Collective_Labour_Agr_ID")) : false;
			      
			      rs.close();
			      stmt.close();
		      }
		      
		      if(access)
		    	  liste = liste.concat("<li><a href=\"/permisHorodateur/\" class=\"menu\">Permis horodateur</a></li>");
    	
	      }
*/
	   }
	   tpl.assign("listeApplications", liste);
	   tpl.assign("welcome", welcome);
	   tpl.assign("welcome2", welcome2);

	   tpl.parse("main");
	   return (tpl.out());
	}
	
}
	
	
	
	

