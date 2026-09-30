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
<%@ page import="solstice.model.P_Absence" %>
<%@ page import="solstice.model.P_Absence_Detail" %>
<%@ page import="solstice.model.P_Absence_Status" %>
<%@ page import="solstice.model.P_Employee" %>
<%@ page import="solstice.model.P_Assignment" %>

<%@ page import="solstice.model.P_Post" %>
<%@ page import="solstice.model.P_Period" %>
<%@ page import="org.compiere.util.TimeUtil" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="solstice.utils.TimeUtilSolstice" %>

<%@ page extends="solstice.web.PageWebSolstice" %>

<%!public String GeneratePage( ) throws Exception 
{
	String filtreDistribution = null;
   	String PC_BROWSER_TEMPLATE =  "Schedule.jtpl";
    this.setTemplate(PC_BROWSER_TEMPLATE);
   
	if ( request.getParameter("distributionId") != null && request.getParameter("distributionId").trim().length() != 0 )
		filtreDistribution = request.getParameter("distributionId");

   //on set les labels static
   tpl.assign("TITLE", "Horaire des employés");
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

   //On crée la requête, qui va chercher les codes de gain	   
   String sql = " select P_Employee_ID, " +
                " value, " +
                " name ," +
                " P_Holidays_Calendar_ID " +
                " from P_Employee " +
                " where " +
                "     IsActive = 'Y' " +
                " and P_Distribution_Booklet_ID = " + 
                this.getEmployeeConnected().getP_Distribution_Booklet_ID()
                ; 
 /*  
	if ( filtreDistribution != null && filtreDistribution.trim().length() != 0)
	{
		
		sql += " and dbo.fn_ReturnCurrentDistribution( employee.P_Employee_ID, sysdate ) = " + filtreDistribution + " ";
	}
 */ 
      sql += " order by name asc ";
  
   //ON exécute la requête et on set les var qui serviront aux templates pour la construction du tableau dynamique
   try
   {
	   	PreparedStatement pstmt = null;
	   	pstmt = DB.prepareStatement (sql, null);
	   	ResultSet rs = pstmt.executeQuery();
	   	boolean alt = false;
	   	Timestamp now = new Timestamp( TimeUtil.getToday().getTimeInMillis() ) ;
	   	int month = TimeUtil.getMonth( now );
	   	int year = TimeUtil.getYear( now );
		if (request.getParameter("month") != null  )
  	    {
			month = Integer.parseInt( request.getParameter("month"));
  	    }

		if (request.getParameter("year") != null  )
  	    {
			year = Integer.parseInt( request.getParameter("year"));
  	    }

		int PreviousYear = year;
		int PreviousMonth = month -1;
		int NextYear = year;
		
		int NextMonth = month +1;
		if ( PreviousMonth == 0)
		{	
			PreviousMonth = 12;
			PreviousYear  = year - 1;
		}
		if ( NextMonth == 13)
		{
			NextMonth = 1;
			NextYear = year + 1;
		}
//		Timestamp firstDay = new Timestamp( TimeUtil.getToday().getTimeInMillis() );
		Timestamp firstDay = TimeUtil.getDay( year, month, 1);
		int MonthLastDay = TimeUtil.getDay_Of_Month( TimeUtil.getMonthLastDay( firstDay));

      	

		tpl.assign("LabelDistribution","Distribution");
		tpl.assign( "SelectDistribution" ,ComboBoxDistributionBooklet() );
//		tpl.assign("Manager", ((solstice.custom.IUserInfo)session.getAttribute("userInfo")).getEmployeeName() );
		tpl.assign("LabelManager", "Gestionnaire :" );
		

      	tpl.assign("CurrentMonth" , TimeUtilSolstice.monthName( month -1 ) + " " + TimeUtil.getYear( firstDay ));
      	tpl.assign("PreviousMonth" , TimeUtilSolstice.monthName( PreviousMonth -1  ) );
      	tpl.assign("NextMonth" , TimeUtilSolstice.monthName( NextMonth -1 ) );

      	tpl.assign("PreviousMonthLink" , "schedule.jsp?month=" + String.valueOf( PreviousMonth  ) +"&year=" + String.valueOf( PreviousYear ));
      	tpl.assign("NextMonthLink" , "schedule.jsp?month=" + String.valueOf( NextMonth ) +"&year=" + String.valueOf( NextYear ));
      	
		for ( int i = 1; i <= MonthLastDay ; i++)
		{
	      	tpl.assign("DAY" , String.valueOf( i ) );
	      	tpl.parse("main.head");
		}

		String sqlAbsence = "Select P_ABSENCE_DETAIL.DURATION, "
						+ "       P_ABSENCE_DETAIL.BEGINING_HOUR, " 
						+ "       P_ABSENCE_DETAIL.ENDING_HOUR,  "
						+ "       P_Gain.Name "
						+ " from P_ABSENCE "
						+ " inner join P_ABSENCE_DETAIL on P_ABSENCE.P_ABSENCE_ID = P_ABSENCE_DETAIL.P_ABSENCE_ID "
						+ " inner join P_Gain on P_Gain.P_Gain_ID = P_ABSENCE.P_Gain_ID "
						+ " where P_ABSENCE.P_EMPLOYEE_ID = ? "
						+ " and P_ABSENCE_DETAIL.ABSENCEDATE = ? ";

		String sqlNonBusinessDay = "Select P_NONBUSINESSDAY.name from  P_Holidays_Calendar "
			+ " inner join P_Holidays_Year on P_Holidays_Calendar.P_Holidays_Calendar_id = P_Holidays_Year.P_Holidays_Calendar_ID " 
			+ " inner join P_NONBUSINESSDAY on P_Holidays_Year.P_Holidays_Year_ID = P_NONBUSINESSDAY.P_Holidays_Year_ID "
			+ " where P_Holidays_Calendar.P_Holidays_Calendar_ID = ? " 
			+ "   AND NonBusinessDate = ? "  
			;

		
		while (rs != null && rs.next())
	   	{
			Timestamp currentDay = firstDay;
	      	tpl.assign("EMPLOYEE_NAME",rs.getString("name"));
			   //Important pour créer un array des paramètres set si-dessus  	
			
		   	PreparedStatement pstmtAbs = null;
		   	PreparedStatement pstmtNbd = null;
			
			for ( int i=1; i <= MonthLastDay ; i++)
			{
			   	pstmtAbs = DB.prepareStatement (sqlAbsence, null);
			   	pstmtAbs.setInt( 1, rs.getInt("P_Employee_ID"));
			   	pstmtAbs.setTimestamp(2, currentDay );

			   	pstmtNbd = DB.prepareStatement (sqlNonBusinessDay, null);
			   	pstmtNbd.setInt( 1, rs.getInt("P_Holidays_Calendar_ID"));
			   	pstmtNbd.setTimestamp(2, currentDay );

			   	ResultSet rsAbs = pstmtAbs.executeQuery();
			   	ResultSet rsNbd = pstmtNbd.executeQuery();

		      	tpl.assign("Msg_AM" + i,  "" );
		      	tpl.assign("Msg_PM" + i,  "" );
		      	
		   		if ( rsAbs.next() )
				{
			      	tpl.assign("CLASS_AM" + i,"AM_V");
			      	tpl.assign("CLASS_PM" + i,"PM_V");
			      	tpl.assign("Msg_AM" + i,  rsAbs.getString("Name") );
			      	tpl.assign("Msg_PM" + i,  rsAbs.getString("Name") );
				}
		   		else if ( rsNbd.next())
				{
			      	tpl.assign("CLASS_AM" + i,"AM_E");
			      	tpl.assign("CLASS_PM" + i,"PM_E");
			      	tpl.assign("Msg_AM" + i,  rsNbd.getString("Name") );
			      	tpl.assign("Msg_PM" + i,  rsNbd.getString("Name") );
				}
				else
				{
			      	tpl.assign("CLASS_AM" + i,"AM_");
			      	tpl.assign("CLASS_PM" + i,"PM_");
				}
				
				
				rsAbs.close();
				pstmtAbs.close();
				
				if ( TimeUtil.getDay_Of_Week( currentDay ) == Calendar.SUNDAY ||
						TimeUtil.getDay_Of_Week( currentDay ) == Calendar.SATURDAY )
				{
			      	tpl.assign("CLASS_AM" + i,"AM_F");
			      	tpl.assign("CLASS_PM" + i,"PM_F");
				}

				currentDay = TimeUtil.addDays( currentDay, 1);
			}
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
