
function FormatHeure(strHeure)//assurer le format HH:MM
{
   var result = "";
   if(strHeure.length > 5 || strHeure.length == 0 || strHeure.length == 3)
   {
      return strHeure;//error
   }
   if(strHeure.length == 1)// 8 heure
   {
      result = "0"+strHeure+":00";//08:00
   }
   if(strHeure.length == 2)// 12 heure
   {
      result = strHeure+":00";
   }
   if(strHeure.length == 4)// 8:00
   {
      result = "0"+strHeure;
   }
   if(strHeure.length == 5)
   {
      result = strHeure;
   }
   return result;
}
function isValidHeure(strHeure)// H, HH, H:MM, HH:MM
{
   var reg1 = /^[1-9]$/;// 8, H
   var reg2 = /^(0[0-9]|1[0-9]|2[0-3])$/;// 12, HH
   var reg3 = /^[1-9]:[0-5]\d$/;// 8:00, H:MM
   var reg4 = /^(0[0-9]|1[0-9]|2[0-3]):[0-5]\d$/;// 8:00, HH:MM
   if ( (reg1.test(strHeure) == false) && (reg2.test(strHeure) == false)  && (reg3.test(strHeure) == false)  &&
   (reg4.test(strHeure) == false)) { return false; }

   return true;
}

function isNumeric(data)
{
   var validChars = "0123456789.-";
   var achar;

   for (c=0; c<data.length; c++)
   {
      achar = data.charAt(c);
      if (validChars.indexOf(achar) == -1)
      {
         return false;
      }
   }
   return true;
}

function IsDate1GreaterThanDate2(date1, date2) //yyyy-mm-dd
{
   var dateElements1 = date1.split("-");
   var dateElements2 = date2.split("-");
   if (parseInt(dateElements1[0],10)>parseInt(dateElements2[0],10))
      return 1;
   if (parseInt(dateElements1[0],10)<parseInt(dateElements2[0],10))
      return -1;

   if (parseInt(dateElements1[0],10)==parseInt(dateElements2[0],10))
   {
      if(parseInt(dateElements1[1],10)>parseInt(dateElements2[1],10))
         return 1;
      if(parseInt(dateElements1[1],10)<parseInt(dateElements2[1],10))
         return -1;
      if(parseInt(dateElements1[1],10)==parseInt(dateElements2[1],10))
      {
         if(parseInt(dateElements1[2],10)>parseInt(dateElements2[2],10))
            return 1;
         if(parseInt(dateElements1[2],10)<parseInt(dateElements2[2],10))
            return -1;
      }
   }
   return 0;
}

function IsHour1GreaterThanHour2(hour1, hour2) //hh:ss
{
   var hourElements1 = hour1.split(":");
   var hourElements2 = hour2.split(":");
   if (parseInt(hourElements1[0],10)>parseInt(hourElements2[0],10))
      return 1;
   if (parseInt(hourElements1[0],10)<parseInt(hourElements2[0],10))
      return -1;

   if (parseInt(hourElements1[0],10)==parseInt(hourElements2[0],10))
   {
      if(parseInt(hourElements1[1],10)>parseInt(hourElements2[1],10))
         return 1;
      if(parseInt(hourElements1[1],10)<parseInt(hourElements2[1],10))
         return -1;
   }
   return 0;
}

function IsHour1GreaterThanHour2Format(hour1, hour2, formatCH) //format C ou H, Centile=H.FF et Heure=HH:SS
{
   var res = 0;
   if (formatCH == "H")
   {
      res = IsHour1GreaterThanHour2(hour1, hour2);
   }
   else
   {
      if (parseFloat(hour1,10)>parseFloat(hour2,10))
      {
         res = 1;   
      }
      else if (parseFloat(hour1,10)<parseFloat(hour2,10))
      {
         res = -1;
      }
      else
      {
         res = 0;   
      }
   }
   return res;
}

function isValidEmail(email,optional)
{
   var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
   if (optional)
   {
      if (email.length > 0)
      {
         return re.test(email);
      }
      else
      {
         return true;
      }
   }
   else
   {
      return re.test(email);
   }
}

//YYYY-MM-JJ
function validDate(strDate)
{
	var bError = 0;
	if(strDate != "")		
	{
		if(strDate.length < 10)
		{
			bError = 1;
		}
		else
		{
			var tempString = strDate.substr(0, 10);
			var strYear = tempString.substr(0, 4);
			if(isNaN(parseInt(strYear)))
			{
				bError = 1;
			}
			else if(parseInt(strYear) < 1900 || parseInt(strYear) > 2100)
			{
				bError = 1;
			}
			else
			{
				if(tempString.substr(4, 1) != "-")
				{
					bError = 1;
				}
				else
				{
					var strMonth = tempString.substr(5, 2);
					if(isNaN(parseInt(strMonth)))
					{
						bError = 1;
					}
					else if(parseInt(strMonth) < 0 || parseInt(strMonth) > 12)
					{
						bError = 1;
					}
					else
					{
						if(tempString.substr(7, 1) != "-")
						{
							bError = 1;
						}
						else
						{
							var strDay = tempString.substr(8, 2);
							if(isNaN(parseInt(strDay)))
							{
								bError = 1;
							}
							else if(parseInt(strDay) < 0 || parseInt(strDay) > 31)
							{
								bError = 1;
							}							
						}
					} 						
				}
			} 
		}
	}
	return bError;
}
