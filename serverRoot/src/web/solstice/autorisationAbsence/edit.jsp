<%@ taglib uri="/WEB-INF/taglib.tld" prefix="mt" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="org.compiere.util.DB" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="solstice.custom.IUserInfo" %>
<%@ page import="solstice.utils.PgiUtil" %>

<mt:securityCheck id="remplir">

<%
    /*
     * On prépare le readOnly pour le formulaire. Le formulaire est éditable seulement
     * s'il s'agit d'un nouvel enregistrement (aucun id passé en paramètre) ou si le
     * statut de l'absence est encore à INIT. On doit également préparer les valuers
     * qu'on affichera dans les controles. En effet, puisque ce formulaire est autant
     * utilisé pour la consultation que pour la saisie, on doit initialiser les valeurs
     * selon le contexte.
     */
     
     IUserInfo userInfo = null;
   	 userInfo = (solstice.custom.IUserInfo)session.getAttribute("userInfo");
	 if ( userInfo == null )
	 {
		 response.sendError(403); 
	 }


     int employeeId = ((solstice.custom.IUserInfo)session.getAttribute("userInfo")).getEmployeeId();
     String heures = "00:00";
     String centile = "0.00";
     String motif = "";
     String description = "";
     Timestamp startDate = null;
     Timestamp endDate = null;
     boolean readOnly = false;
     
     // On regarde d'abord s'il y a une absence passée en paramètre
     if(request.getParameter("absenceId") != null
            && !request.getParameter("absenceId").equals(""))
     {
         // On doit maintenant vérifier si le statut est différent de INIT
         String sql
         = "select 1"
            + " from P_Absence inner join P_Absence_Status"
            + " on P_Absence.P_Absence_Status_ID = P_Absence_Status.P_Absence_Status_ID"
            + " where P_Absence_Status.Value <> 'INIT'"
            + " and P_Absence.P_Absence_ID = " + request.getParameter("absenceId");
  /*      
  		  if(! userInfo.hasPolicy("admconsult"))
  		  {
  			  sql = sql + " AND P_Absence.P_Employee = " + ((solstice.custom.IUserInfo)session.getAttribute("userInfo")).getEmployeeId();
  		  }
   */         
         try
         {
             PreparedStatement stmt = DB.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery();
             
             // Si le statut est différent de INIT, on met le readOnly à true
             if(rs.next())
             {
                 readOnly = true;
             }
             
             rs.close();
             stmt.close();
         }
         catch (SQLException e)
         {
             e.printStackTrace(System.out);
         }
         
         // On récupère le reste des informations du formulaire
         sql
         = "select absence.P_Employee_ID, gain.P_Gain_ID as Motif, isnull(absence.Description, '') as Description,"
            + " min(absenceDetail.AbsenceDate) as StartDate,"
            + " max(absenceDetail.AbsenceDate) as EndDate,"
            + " convert(nvarchar(13), cast(isnull(sum(absenceDetail.Duration), 0) as decimal(10,2))) as Centile,"
            + " dbo.timeToHour(isnull(sum(absenceDetail.Duration), 0)) as Heures"
            + " from (P_Absence absence inner join P_Gain gain "
            + " on absence.P_Gain_ID = gain.P_Gain_ID) inner join P_Absence_Detail absenceDetail"
            + " on absence.P_Absence_ID = absenceDetail.P_Absence_ID"
            + " inner join p_Employee employee on employee.P_Employee_ID = absence.P_Employee_ID "
            + " left outer join AD_WF_NODE ON AD_WF_NODE.AD_WF_NODE_ID = absence.AD_WF_NODE_ID "
      		+ " where absence.P_Absence_ID = " + request.getParameter("absenceId")
            ;
    /*     
 		  if( userInfo.hasPolicy("admconsult"))
 		  {
 				sql += " And ( (dbo.fn_ReturnCurrentDistribution( employee.P_Employee_ID, absenceDetail.AbsenceDate ) in (" + ((IUserInfo)session.getAttribute("userInfo")).getDistributionWebSQLAbsence(new String[] {"P_Distribution_Booklet_ID"}, false) + " ) ";
 				
 				//+ 2011.05.18 Ajout autorisation par workflow.
 						if ( ! ((solstice.custom.IUserInfo)session.getAttribute("userInfo")).hasPolicy("avis_allemployee") == true  )
 					    {
 							sql += " AND  absence.AD_WF_NODE_ID IS NULL and gain.ad_workflow_id is null ) "
 				//Gestionnaire 			
 							+ "      OR  ( ( absence.AD_WF_NODE_ID IS NOT NULL AND EMAILRECIPIENT = 'G' AND ((" + ((IUserInfo)session.getAttribute("userInfo")).getUserId() + " = absence.ApprovedBy ) "
 							;

// 							sql += "  OR " + ((IUserInfo)session.getAttribute("userInfo")).getEmployeeId() + " = P_Distribution_Booklet.P_Employee_Secretary_ID  ))"  
 							sql += "  OR " + ((IUserInfo)session.getAttribute("userInfo")).getEmployeeId() + " in ( select P_Distribution_Booklet.P_Employee_Secretary_ID union select P_Employee_ID From P_Distribution_Booklet_Replacement  Where P_Distribution_Booklet_Replacement.P_Distribution_Booklet_ID=P_Distribution_Booklet.P_Distribution_Booklet_ID and ReplacementType = 'S' and StartDate <= getdate() and ( EndDate is null or EndDate >= getdate() ) )))"  
 							; // D.R.H

 					    }

 				//DRH			   
// 				            if(loggedUserHasPolicy(pageContext,"seeAllDistributionAndEmployees"))
 				        	if ( ((solstice.custom.IUserInfo)session.getAttribute("userInfo")).hasPolicy("seeAllDistributionAndEmployees") == true  )
 				        	{
 								sql += "  OR   ( absence.AD_WF_NODE_ID IS NOT NULL AND EMAILRECIPIENT = 'H' AND absence.ApprovedBy = 1000 )  " ; // D.R.H
 				        	}			 
 				//Vice-président			
 						if ( ! ((solstice.custom.IUserInfo)session.getAttribute("userInfo")).hasPolicy("avis_allemployee") == true  )
 					    {
 							sql += " OR ( absence.AD_WF_NODE_ID IS NOT NULL AND EMAILRECIPIENT = 'V' AND " + ((IUserInfo)session.getAttribute("userInfo")).getUserId() + " = absence.ApprovedBy )  "
 				             ;
 				        }			
 				        sql += "  )) ";
 		  }
 		  else
 		  {
 			  sql = sql + " and absence.P_Employee_ID = " + ((solstice.custom.IUserInfo)session.getAttribute("userInfo")).getEmployeeId();
 		  }

*/
            
		 sql = sql + " group by absence.P_Employee_ID, absence.Description, gain.P_Gain_ID";
            
        try
        {
             PreparedStatement stmt = DB.prepareStatement(sql, null);
             ResultSet rs = stmt.executeQuery();
             
             if(rs.next())
             {
                 motif = rs.getString("Motif");
                 description = rs.getString("Description");
                 startDate = rs.getTimestamp("StartDate");
                 endDate = rs.getTimestamp("EndDate");
                 heures = rs.getString("Heures");
                 centile = rs.getString("Centile");
                 employeeId = rs.getInt("P_Employee_ID");
             }
             
             rs.close();
             stmt.close();
        }
        catch (SQLException e)
        {
             e.printStackTrace(System.out);
        }
     }
     
%>

	<mt:pageTag id="remplir" supportCalendar="true" validationClass="solstice.custom.AutorisationAbsence" displayNavigation="false">
	
	    <script language="javascript">
	        var durations = new Array();
	        var times = new Array();
			var threadInUse = false;
	    
	        function showCalendar()
	        {
    	        if(document.mainForm.startDate.value != ""
    	            && document.mainForm.endDate.value != "")
    	        {
        	        document.mainForm.showAbsenceCalendar.value = "true";
        	        document.mainForm.submit();
    	        }
    	        else
    	        {
        	        alert("Vous devez obligatoirement saisir une date de début et une date de fin.");
    	        }
	        }

		function refreshTimeFormat(value)
		{
			var indexMinuteSeparator = value.indexOf(":");
			document.getElementById('totalHeures').innerHTML = value;

			if(document.mainForm.dateJournee != null)
			{

				if((typeof document.mainForm.dateJournee.length != 'undefined') == true)
				{
					var cmpt = document.mainForm.dateJournee.length;
					var idx = 0;
					for(idx = 0;idx < cmpt; idx++)
					{
						var nameValue = document.mainForm.dateJournee[idx].value;
						var nameValueCent = nameValue + "_cent";
						eval("var tempValue = document.mainForm."+nameValue+".value;");
						eval("var tempValueCent = document.mainForm."+nameValueCent+".value;");
						//var tempValue = document.mainForm.dureeJournee[idx].value;
						if(indexMinuteSeparator == -1)//minutes vers centile
						{	
							var indexOfvalue = tempValue.indexOf(":");
							if(indexOfvalue == -1)
							{
								alert("Format incorrect");
							}
							else
							{
								/*var firstPart = tempValue.substring(0, indexOfvalue);
								var secondPart = tempValue.substring(indexOfvalue + 1, tempValue.length);
								if(firstPart.indexOf("0") == 0)
								{
									firstPart = firstPart.substring(1, firstPart.length);
								}
								if(secondPart/60*100 != Number.NaN)
								{
									secondPart = secondPart/60*100;
									secondPart = Math.round(secondPart);
									secondPart = secondPart.toString();
									if(secondPart.indexOf(".") != -1)
									{
										secondPart = secondPart.substring(0, secondPart.indexOf("."));
									}
									if(secondPart.length == 0)
									{
										secondPart = "00";
									}
									else if(secondPart.length == 1)
									{
										secondPart = "0" + secondPart;
									}
									else if(secondPart.length > 2)
									{
										secondPart = secondPart.substring(0,2)
									}
	
								}*/
								//eval("document.mainForm."+nameValue+".value = firstPart + \".\" +secondPart;");
								eval("document.mainForm."+nameValue+".value = tempValueCent;");
								//document.mainForm.dureeJournee[idx].value = firstPart + "." +secondPart;
							}
						}
						else//centile vers minutes
						{
							var indexOfvalue = tempValue.indexOf(".");
							if(indexOfvalue == -1)
							{
								alert("Format incorrect");
							}
							else
							{
								var firstPart = tempValue.substring(0, indexOfvalue);
								var secondPart = tempValue.substring(indexOfvalue + 1, tempValue.length);
								if(firstPart.length == 1)
								{
									firstPart = "0" + firstPart;
								}
								if(secondPart/100*60 != Number.NaN)
								{
									secondPart = secondPart/100*60;
									secondPart = Math.round(secondPart);
									secondPart = secondPart.toString();
									if(secondPart.indexOf(".") != -1)
									{
										secondPart = secondPart.substring(0, secondPart.indexOf("."));
									}
									if(secondPart.length == 0)
									{
										secondPart = "00";
									}
									else if(secondPart.length == 1)
									{
										secondPart = "0" + secondPart;
									}
									else if(secondPart.length > 2)
									{
										secondPart = secondPart.substring(0,2)
									}
								}
								eval("document.mainForm."+nameValue+".value = firstPart + \":\" +secondPart;");
								//document.mainForm.dureeJournee[idx].value = firstPart + ":" +secondPart;
							}
						}
					}
				}
				else
				{
					var nameValue = document.mainForm.dateJournee.value;
					var nameValueCent = nameValue + "_cent";
					eval("var tempValue = document.mainForm."+nameValue+".value;");
					eval("var tempValueCent = document.mainForm."+nameValueCent+".value;");
					//var tempValue = document.mainForm.dureeJournee.value;
					if(indexMinuteSeparator == -1)//minutes vers centile
					{	
						var indexOfvalue = tempValue.indexOf(":");
						if(indexOfvalue == -1)
						{
							alert("Format incorrect");
						}
						else
						{
							/*var firstPart = tempValue.substring(0, indexOfvalue);
							var secondPart = tempValue.substring(indexOfvalue + 1, tempValue.length);
							if(firstPart.indexOf("0") == 0)
							{
								firstPart = firstPart.substring(1, firstPart.length);
							}
							if(secondPart/60*100 != Number.NaN)
							{
								secondPart = secondPart/60*100;
							}
							secondPart = Math.round(secondPart);
							secondPart = secondPart.toString();
							if(secondPart.indexOf(".") != -1)
							{
								secondPart = secondPart.substring(0, secondPart.indexOf("."));
							}
							if(secondPart.length == 0)
							{
								secondPart = "00";
							}
							else if(secondPart.length == 1)
							{
								secondPart = "0" + secondPart;
							}
							else if(secondPart.length > 2)
							{
								secondPart = secondPart.substring(0,2)
							}*/
							//eval("document.mainForm."+nameValue+".value = firstPart + \".\" +secondPart;");
							eval("document.mainForm."+nameValue+".value = tempValueCent;");
							//document.mainForm.dureeJournee.value = firstPart + "." +secondPart;
						}
					}
					else//centile vers minutes
					{
						var indexOfvalue = tempValue.indexOf(".");
						if(indexOfvalue == -1)
						{
							alert("Format incorrect");
						}
						else
						{
							var firstPart = tempValue.substring(0, indexOfvalue);
							var secondPart = tempValue.substring(indexOfvalue + 1, tempValue.length);
							if(firstPart.length == 1)
							{
								firstPart = "0" + firstPart;
							}
							if(secondPart/100*60 != Number.NaN)
							{
								secondPart = secondPart/100*60;
								secondPart = Math.round(secondPart);
								secondPart = secondPart.toString();
								if(secondPart.indexOf(".") != -1)
								{
									secondPart = secondPart.substring(0, secondPart.indexOf("."));
								}
								if(secondPart.length == 0)
								{
									secondPart = "00";
								}
								else if(secondPart.length == 1)
								{
									secondPart = "0" + secondPart;
								}
								else if(secondPart.length > 2)
								{
									secondPart = secondPart.substring(0,2)
								}

							}
							eval("document.mainForm."+nameValue+".value = firstPart + \":\" +secondPart;");
							//document.mainForm.dureeJournee.value = firstPart + ":" +secondPart;
						}
					}
					
				}
			}
		}
	        function validAllDuration()
	        {
	        if(document.mainForm.dateJournee != null)
	        {
		        var result = 1;
	        	var cmpt = 0;
		        if((typeof document.mainForm.dateJournee.length != 'undefined') == true)
		        {
	        		cmpt = document.mainForm.dateJournee.length;
	        		for(var idx = 0;idx < cmpt; idx++)
					{
						var nameValue = document.mainForm.dateJournee[idx].value;
						var nameValueCent = nameValue + "_cent";
						eval("var backGround = document.mainForm."+nameValue+".style.background;");
						if(backGround == 'red')
						{
							result = 0;
						}
					}
	        	}
	        	else
	        	{
					var nameValue = document.mainForm.dateJournee.value;
					var nameValueCent = nameValue + "_cent";
					eval("var backGround = document.mainForm."+nameValue+".style.background;");
					if(backGround == 'red')
					{
						result = 0;
					}
	        	}

	        	if(result == 0)
	        		return false;
	        	else
	        		return true;
	        }
	        return true;
	        }
	        
	        
	        function save()
	        {
    	        if(document.mainForm.gainId.selectedIndex > 0
    	            && document.mainForm.startDate.value != ""
    	            && document.mainForm.endDate.value != "")
    	        {
    	        	if(validAllDuration())
    	        	{
	        	        if(allValid(durations))
	        	        {
	            	        if(allValid(times))
	            	        {
	            	        	if(absenceCalendar_validerTempsStandard())
	            	        	{
		                	        document.mainForm.action.value = "save";
		                	        document.mainForm.submit();
		            	        }
	                	        else
	                	        {
	                	        	alert("Vous devez saisir des heures de début et des heures de fin pour toutes les journées où le temps saisi est plus petit que le temps standard");
	                	        }
	            	        }
	            	        else
	            	        {
	                	        alert("Vérifier le calendrier. Les heures de début et les heures de fin doivent suivre le format 00:00");
	            	        }
	        	        }
	        	        else
	        	        {
	            	        alert("Les durées entrées ne doivent pas dépasser le temps standard et doivent être différent de 0.");
	        	        }
        	        }
        	        else
        	        {
        	        	alert("Une des durées saisies n'est pas valide.\nSoit le format n'est pas valide, soit la durée dépasse le temps standard\nou soit la durée est de 0.");
        	        }
    	        }
    	        else
    	        {
        	        alert("Vous devez obligatoirement choisir un motif et des dates");
        	    }
	        }
	        
	        function allValid(array)
	        {
    	        for(var i = 0; i < array.length; i++)
    	        {
        	        if(array[i] == 0)
        	            return false;
    	        }
    	        return true;
	        }
	        
	        function isNumeric(v)
	        {
                if(v.length == 0) return false;
                var validChars = "0123456789.";
                var isNumber=true;
                var c;

                for (i = 0; i < v.length && isNumber == true; i++) 
                { 
                    c = v.charAt(i); 
                    if (validChars.indexOf(c) == -1) 
                    {
                        isNumber = false;
                    }  
                }
                return isNumber;
	        }
	        
	        function validHour(value)
	        {
              var reg1 = /^[1-9]$/;// 8, H
              var reg2 = /^(0[0-9]|1[0-9]|2[0-3])$/;// 12, HH
              var reg3 = /^[1-9]:[0-5]\d$/;// 8:00, H:MM
              var reg4 = /^(0[0-9]|1[0-9]|2[0-3]):[0-5]\d$/;// 8:00, HH:MM
              if ( (reg1.test(value) == false) && 
                   (reg2.test(value) == false) && 
                   (reg3.test(value) == false) &&
                   (reg4.test(value) == false)) 
              { 
                 return false; 
              }
              return true;
	        }
	        
	        
        function validDuration(value, standardTime, i)
        {
		if ((value.toString().indexOf(".") == -1) && (value.toString().indexOf(":") == -1))
	        {
			// aucun '.' ni ':' dans la durée qui a été entrée
			return false;
	        }

		var strTimeFormat = document.mainForm.timeFormat.value;
		var isInMinute = false;

		if(strTimeFormat.toString().indexOf(":") != -1)
			isInMinute = true;
		var indexOfDot = value.toString().indexOf(".");

		if(indexOfDot == -1)//conversion minute -> centile
		{
			if(isInMinute == false)
				return false;
			var indexOfColon = value.toString().indexOf(":");
			var firstPart = value.substring(0, indexOfColon);
			var secondPart = value.substring(indexOfColon + 1, value.length);

			if(firstPart.indexOf("0") == 0)
			{
				firstPart = firstPart.substring(1, firstPart.length);
			}
			if(secondPart/60*100 != Number.NaN)
			{
				secondPart = secondPart/60*100;
			}
			secondPart = secondPart + "";
			if(secondPart.indexOf(".") > -1)
			{

				secondPart = secondPart.substring(0, secondPart.indexOf("."));
			}

			secondPart = secondPart.toString();
			if(secondPart.length > 2)
			{
				secondPart = secondPart.substring(0,2)
			}
			value = firstPart + "." +secondPart;

		}
		else
		{
			if(isInMinute == true)
				return false;
		}
    	        if(isNumeric(value))
    	        {
        	        if(parseFloat(value) <= parseFloat(standardTime))
        	        {
            	        durations[i] = 1;
			
            	        return true;
        	        }
    	        }
    	        durations[i] = 0;
    	        return false;
	        }
	        
	        function validTime(value, i)
	        {
    	        if(value == "" || validHour(value))
    	        {
        	        times[i] == 1;
        	        return true;
    	        }
    	        times[i] == 0;
    	        return false;
	        }

	        function askApprob()
	        {
    	        document.mainForm.action.value = 'askApprob';
    	        document.mainForm.submit();
	        }
			
			function askApprobAndSave()
			{
    	        if(document.mainForm.gainId.selectedIndex > 0
    	            && document.mainForm.startDate.value != ""
    	            && document.mainForm.endDate.value != "")
    	        {
    	        	if(validAllDuration())
    	        	{
	        	        if(allValid(durations))
	        	        {
	            	        if(allValid(times))
	            	        {
	            	        	if(absenceCalendar_validerTempsStandard())
	            	        	{
		                	        document.mainForm.action.value = "askApprobAndSave";
		                	        document.mainForm.submit();
		            	        }
	                	        else
	                	        {
	                	        	alert("Vous devez saisir des heures de début et des heures de fin pour toutes les journées où le temps saisi est plus petit que le temps standard");
	                	        }
	            	        }
	            	        else
	            	        {
	                	        alert("Vérifier le calendrier. Les heures de début et les heures de fin doivent suivre le format 00:00");
	            	        }
	        	        }
	        	        else
	        	        {
	            	        alert("Les durées entrées ne doivent pas dépasser le temps standard et doivent être différent de 0.");
	        	        }
        	        }
        	        else
        	        {
        	        	alert("Une des durées saisies n'est pas valide.\nSoit le format n'est pas valide, soit la durée dépasse le temps standard\nou soit la durée est de 0.");
        	        }
    	        }
    	        else
    	        {
        	        alert("Vous devez obligatoirement choisir un motif et des dates");
        	    }
	        }
			
	   		function dateListener()
    		{
			    
				if (!threadInUse)
				{
					threadInUse = true;
					if(document.mainForm.endDate.value != ""
					      && endDate != document.mainForm.endDate.value 
					      && document.mainForm.startDate.value != "")
					{
   					if(compareDate())
   					{
   						refreshCalendar();
   					}
   					else
   					{
      					document.mainForm.endDate.value = "";
      					alert("La date de fin doit être plus grande ou égale à la date de début");
   					}
					}
					else if(document.mainForm.startDate.value != ""
					      && startDate != document.mainForm.startDate.value 
					      && document.mainForm.endDate.value != "")
					{
   					if(compareDate())
   					{
   						refreshCalendar();
   					}
   					else
   					{
      					document.mainForm.startDate.value = "";
      					alert("La date de début doit être plus petite ou égale à la date de fin");
   					}
					}
					threadInUse = false;
				}
    		}
    		
    		/**
    		 * Cette fonction sert à vérifier si la date de fin saisie est plus
    		 * grande ou égale à la date de début saisie
    		 */
    		function compareDate()
    		{
       		if(document.mainForm.endDate.value != ""
       		      && document.mainForm.startDate.value != "")
       		{
          		// Puisque la date est dans le format yyyy-MM-dd, on n'a qu'à
          		// la transformer sous le format yyyyMMdd et à la convertir en
          		// entier pour faire la comparaison
          		var sd = "" + document.mainForm.startDate.value.charAt(0)
          		   + document.mainForm.startDate.value.charAt(1)
          		   + document.mainForm.startDate.value.charAt(2)
          		   + document.mainForm.startDate.value.charAt(3)
          		   + document.mainForm.startDate.value.charAt(5)
          		   + document.mainForm.startDate.value.charAt(6)
          		   + document.mainForm.startDate.value.charAt(8)
          		   + document.mainForm.startDate.value.charAt(9);
          		   
          		var ed = "" + document.mainForm.endDate.value.charAt(0)
          		   + document.mainForm.endDate.value.charAt(1)
          		   + document.mainForm.endDate.value.charAt(2)
          		   + document.mainForm.endDate.value.charAt(3)
          		   + document.mainForm.endDate.value.charAt(5)
          		   + document.mainForm.endDate.value.charAt(6)
          		   + document.mainForm.endDate.value.charAt(8)
          		   + document.mainForm.endDate.value.charAt(9);
          		   
          		return (parseInt(sd) <= parseInt(ed));
       		}
       		return false;
    		}
			
			function refreshCalendar()
			{
    	        if(document.mainForm.startDate.value != ""
   	            && document.mainForm.endDate.value != "")
    	        {
					clearInterval(intervalId);
					showCalendar();
				}

			}

			function updateCentile(name, value)
			{
				var realName = name + "_cent";
				//alert(realName);
				if(value.indexOf(":") > -1)
				{
					var newValue = hr_minToHr_Cent(value);
					//alert("document.mainForm."+realName+".value = newValue;"+newValue);
					eval("document.mainForm."+realName+".value = newValue;");
				}
				else
				{
					//alert("document.mainForm."+realName+".value = value;"+value);
					eval("document.mainForm."+realName+".value = value;");
				}

			}
			
			function hr_minToHr_Cent(value)
			{
				var indexOfColon = value.toString().indexOf(":");
				var firstPart = value.substring(0, indexOfColon);
				var secondPart = value.substring(indexOfColon + 1, value.length);

				if(firstPart.indexOf("0") == 0)
				{
					firstPart = firstPart.substring(1, firstPart.length);
				}
				if(secondPart/60*100 != Number.NaN)
				{
					secondPart = secondPart/60*100;
				}
				secondPart = secondPart + "";
				if(secondPart.indexOf(".") > -1)
				{
					secondPart = secondPart.substring(0, secondPart.indexOf("."));
				}

				secondPart = secondPart.toString();
				if(secondPart.length > 2)
				{
					secondPart = secondPart.substring(0,2)
				}
				if(secondPart.length == 1)
				{
					secondPart = "0"+secondPart;
				}
				return firstPart + "." +secondPart;
			}
	
			function trim(string)
			{return string.replace(/(^\s*)|(\s*$)/g,'');} 	        
	      
			function chatWindow(tab,rec)
		   {
		   	window.open("chat_window.jsp?tableId="+tab+"&recordId="+rec, "chatWindow", "left=20,top=20,width=990,height=600,toolbar=0,resizable=1,scrollbars=1");
		   }
			
	    </script>
	
	    <form name="mainForm" method="post">
	    
	        <input type="hidden" name="action">
	        <input type="hidden" name="absenceId" value="<%= request.getParameter("absenceId") != null ? request.getParameter("absenceId") : "-1" %>">
	        
	        <p class="sectitle">Section 1 - Identification</p>
	        
	        <mt:dynamicStructure>
	            <mt:customQuery>
                    select top 1 employee.Name, employee.Value, isnull(ltrim(rtrim(activity.Value)) + ' - ' + activity.Name, 'N/A') as Activity,
                    isnull(occupationGroup.Name, 'N/A') as OccupationGroup, isnull(jobType.Name, 'N/A') as JobType,
                    jobType.P_Job_Type_ID, occupationGroup.P_Occupation_Group_ID, activity.C_Activity_ID
                    from ((P_Employee employee left join P_Job_Type jobType
                    on employee.P_Job_Type_ID = jobType.P_Job_Type_ID) left join P_Occupation_Group occupationGroup
                    on employee.P_Occupation_Group_ID = occupationGroup.P_Occupation_Group_ID) 
                    left join (P_Assignment assignment inner join (P_Post post inner join C_Activity activity
                    on post.C_Activity_ID = activity.C_Activity_ID)
                    on assignment.P_Post_ID = post.P_Post_ID)
                    on employee.P_Employee_ID = assignment.P_Employee_ID
                    where isnull(assignment.AssignmentType, 'P') = 'P'
                    and employee.P_Employee_ID = <%= employeeId %>
	            </mt:customQuery>
	            
	            <mt:body>
        	        <table width="100%" border="0" cellspacing="1" cellpadding="1">
        	            <tr>
        	                <td class="tdlabel">Nom de l'employ&eacute;</td>
        	                <td class="tdlabel">No. d'employ&eacute;</td>
        	            </tr>
        	            <tr>
        	                <td class="tdfield"><mt:dynamicValue id="Name"/></td>
        	                <td class="tdfield"><mt:dynamicValue id="Value"/></td>
        	            </tr>
        	            <tr>
        	                <td class="tdlabel" colspan="2">Unit&eacute; administrative</td>
        	            </tr>
        	            <tr>
        	                <td class="tdfield" colspan="2">
        	                    <mt:dynamicValue id="Activity"/>
        	                    <input type="hidden" name="activityId" value="<mt:dynamicValue id="C_Activity_ID"/>">
        	                </td>
        	            </tr>
        	            <tr>
        	                <td class="tdlabel">Cat&eacute;gorie de personnel</td>
        	                <td class="tdlabel">Statut</td>
        	            </tr>
        	            <tr>
        	                <td class="tdfield">
        	                    <mt:dynamicValue id="OccupationGroup"/>
        	                    <input type="hidden" name="occupationGroupId" value="<mt:dynamicValue id="P_Occupation_Group_ID"/>">
        	                </td>
        	                <td class="tdfield">
        	                    <mt:dynamicValue id="JobType"/>
        	                    <input type="hidden" name="jobTypeId" value="<mt:dynamicValue id="P_Job_Type_ID"/>">
        	                </td>
        	            </tr>
        	        </table>
	            </mt:body>
	        </mt:dynamicStructure>
	        
	        <p class="sectitle">Section 2 - Demande de l'employ&eacute;</p>
	    
	        <table width="100%" cellspacing="1" cellpadding="1" border="0">
	            <tr>
	                <td class="tdlabel" colspan="5">Motif</td>
	            </tr>
	            <tr>
	                <td class="tdfield" colspan="5">
	                    <mt:comboBoxTag id="gainId" getPostValue="true" selectedValue="<%= motif %>" readOnly="<%= readOnly %>">
                            select P_Gain_ID, ltrim(rtrim(Name)) + ' (' + ltrim(rtrim(Value)) + ')' as description
                            from P_Gain
                            where IsActive = 'Y'
                            and exists (
                              select *
                              from P_Gain_GainInfo inner join P_GainInfo
                              on P_Gain_GainInfo.P_GainInfo_ID = P_GainInfo.P_GainInfo_ID
                              where P_GainInfo.Value = 'MotifAbsen'
                              and P_Gain_GainInfo.P_Gain_ID = P_Gain.P_Gain_ID
                              and P_Gain_GainInfo.To_Consider = 'Y'
                              )
             				  order by  description asc
	                    </mt:comboBoxTag>
	                </td>
	            </tr>
	            <tr>
	                <td class="tdlabel" colspan="5">D&eacute;tail de la demande (s'il y a lieu)</td>
	            </tr>
	            <tr>
	                <td class="tdfield" colspan="5">
	                    <textarea name="detailDemande" rows="4" style="width: 100%"<%= readOnly ? " readonly" : "" %>><% if (!description.equals("") && description != null){%><%=description%><%}else if (request.getParameter("detailDemande") != null){%><%=	request.getParameter("detailDemande")%><%}%></textarea>
	                </td>
	            </tr>
	            <tr>
	                <td rowspan="2" class="tdlabel">Dates</td>
	                <td class="tdlabel">Date de d&eacute;but</td>
	                <td class="tdlabel">Date de fin</td>
	                <td class="tdlabel">D&eacute;tailler les heures</td>
	                <td class="tdlabel">Total (heures)</td>
	            </tr>
	            <tr>
	                <td class="tdfield"><mt:simpleCalendar value="<%= startDate %>" readOnly="<%= readOnly %>" getPostValue="true" formName="mainForm" id="startDate"/></td>
	                <td class="tdfield"><mt:simpleCalendar value="<%= endDate %>" readOnly="<%= readOnly %>" getPostValue="true" formName="mainForm" id="endDate"/></td>
	                <td class="tdfield">
	                    <div>
	                        <input type="button" value="Afficher le calendrier" onClick="showCalendar();">
				<%
	String strTimeFormat = "";
	boolean isMinute = true;
	if(request.getParameter("timeFormat") != null
            && !request.getParameter("timeFormat").equals(""))
	{
		strTimeFormat = request.getParameter("timeFormat");
		int indexOfDot = strTimeFormat.indexOf(".");
		
		if(indexOfDot != -1)
			isMinute = false;
	}
				%>

	                        <select name="timeFormat" onChange="refreshTimeFormat(this.value);">
	                            <option value="<%= heures %>" <%=(strTimeFormat.equals("")||strTimeFormat.equals(heures)) ? "selected" : ""%>>Heures:Minutes</option>
	                            <option value="<%= centile %>" <%=strTimeFormat.equals(centile) ? "selected" : ""%>>Centile</option>
	                        </select>
	                    </div>
	                </td>
	                <td class="tdfield" align="right"><div id="totalHeures"><%= isMinute ? heures : centile %></div></td>
	            </tr>
	        </table>
	        
	        <input type="hidden" name="showAbsenceCalendar" value="<%= request.getParameter("showAbsenceCalendar") %>">
	        
	        <mt:checkVisible checkMethod="solstice.custom.AutorisationAbsence.showAbsenceCalendar">
	        <input type="hidden" name="calendarShown" value="true" />
	        <table width="100%" cellspacing="1" cellpadding="1" border="0">
	            <tr>
	                <td class="tdlabel">Calendrier des jours et des heures d'absence</td>
	            </tr>
	            <tr>
	                <td class="tdfield" align="center"><br><br>
	                    <mt:absenceCalendar 
	                        employeeId="<%= ((solstice.custom.IUserInfo)session.getAttribute("userInfo")).getEmployeeId() %>"
	                        startDate="<%= request.getParameter("startDate") %>"
	                        endDate="<%= request.getParameter("endDate") %>"/><br><br>
            	        <table border="0" cellspacing="1" cellpadding="1" width="90%">
            	            <tr>
            	                <td class="tdfield" width="25%"><b>L&eacute;gende :</b></td>
            	                <td width="25%">
            	                    <table width="100%" cellspacing="0" cellpadding="1">
            	                        <tr>
            	                            <td class="tdfield">
                        	                    <div class="tdcal" style="width: 25px">&nbsp;</div>
            	                            </td>
            	                            <td class="tdfield" width="100%">
                        	                    Journ&eacute;e travaill&eacute;e
            	                            </td>
            	                        </tr>
            	                    </table>
            	                </td>
            	                <td width="25%">
            	                    <table width="100%" cellspacing="0" cellpadding="1">
            	                        <tr>
            	                            <td class="tdfield">
                        	                    <div class="tdselectcal" style="width: 25px">&nbsp;</div>
            	                            </td>
            	                            <td class="tdfield" width="100%">
                        	                    Journ&eacute;e non travaill&eacute;e
            	                            </td>
            	                        </tr>
            	                    </table>
            	                </td>
            	                <td width="25%">
            	                    <table width="100%" cellspacing="0" cellpadding="1">
            	                        <tr>
            	                            <td class="tdfield">
                        	                    <div class="tdfullselectcal" style="width: 25px">&nbsp;</div>
            	                            </td>
            	                            <td class="tdfield" width="100%">
                        	                    Journ&eacute;e avec absence
            	                            </td>
            	                        </tr>
            	                    </table>
            	                </td>
            	            </tr>
            	            <tr>
            	                <td colspan="2">&nbsp;</td>
            	                <td colspan="2">
            	                    <table width="100%" cellspacing="0" cellpadding="1">
            	                        <tr>
            	                            <td class="tdfield">
                        	                    <div class="tdautreselectcal" style="width: 25px">&nbsp;</div>
            	                            </td>
            	                            <td class="tdfield" width="100%">
                        	                    Journ&eacute;e avec autre absence
            	                            </td>
            	                        </tr>
            	                    </table>
            	                </td>
            	            </tr>
            	        </table><br>
					    Les heures de d&eacute;but et de fin ne sont pas obligatoires lorsque l'absence
	                    est pour une journ&eacute;e complète.
	                </td>
	            </tr>
	        </table>
	        
	        </mt:checkVisible>
	        
	        
	        
	        <table width="100%">
	        <tr>
	          <td valign="top" width="50%"><br>
	            <table width="100%"><tr><td class="sectitle">Notes</td><td align="right"><input type="button" value="Gestion des notes" onClick="chatWindow('2100758','<%=request.getParameter("absenceId")%>');">&nbsp;</td></tr></table>
					<iframe name="iframechat" id="iframechat" src="chat_window.jsp?tableId=2100758&recordId=<%=request.getParameter("absenceId")%>&compact=true" width="100%" frameborder="0" scrolling="auto"></iframe>
				</td>
            <td valign="top" width="50%">
	        <input type="hidden" name="source" value="edition" />
	        <p align="right"><input type="button" value="Demander approbation" onClick="askApprobAndSave();"<%= readOnly ? " disabled" : "" %>></p>
	        <p align="right">
	            <input type="button" value="Sauvegarder" onClick="save();"<%= readOnly ? " disabled" : "" %>>
	            <!--input type="button" value="Liste des absences" onClick="document.location = 'consult.jsp';"-->
	            <!--input type="button" value="Nouveau" onClick="document.location = 'remplir.jsp'"-->
	        </p>
           </td></tr></table>
	    </form>
        <script language="javascript">
			var startDate = document.mainForm.startDate.value;
			var endDate = document.mainForm.endDate.value;
			var intervalId = null;
            intervalId = setInterval("dateListener()", 1);
        </script>

	</mt:pageTag>
</mt:securityCheck>