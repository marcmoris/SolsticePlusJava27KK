package solstice.process;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.logging.Level;

import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import solstice.model.P_Period;
import solstice.model.Planifico;

public class UpdatePlanifico extends SvrProcess{


	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (UpdatePlanifico.class);

	
	public UpdatePlanifico ()
	{
		try {
			doIt();
	    } catch (Exception e) {
	        System.out.println("Error Message " + e);
	    }
	}	
	
	private JsonObject postDateJson( JSONObject data )
	{
		String sUrl      = "https://api1.planifico.co/API_V2/UpdateUsersHourBanks" ;

		JsonObject jsonObj = null;
		try {
			
		    URL url = new URL(sUrl);
		    HttpURLConnection httpConnection  = (HttpURLConnection) url.openConnection();
		    httpConnection.setDoOutput(true);
		    httpConnection.setRequestMethod("POST");
		    httpConnection.setRequestProperty("Content-Type", "application/json");
		    httpConnection.setRequestProperty("Accept", "application/json");
	
		    DataOutputStream wr = new DataOutputStream(httpConnection.getOutputStream());
		    wr.write(data.toString().getBytes());
		    Integer responseCode = httpConnection.getResponseCode();
	
		    
		    BufferedReader bufferedReader;
		    // Creates a reader buffer
		    if (responseCode > 199 && responseCode < 300) {
		        bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream(), "UTF-8"));
		    } else {
		        bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getErrorStream(), "UTF-8"));
		    }
	
		    // To receive the response
		    StringBuilder content = new StringBuilder();
		    String line;
		    while ((line = bufferedReader.readLine()) != null) {
		        content.append(line).append("\n");
		    }
		    
		    System.out.println( "content : " + content );
		    
		    bufferedReader.close();
		    String jsonString = content.toString();
		    
		    jsonObj = JsonParser.parseString(jsonString).getAsJsonObject();

		}
		catch (Exception e)
		{
			System.out.println ("* Error * Planifico - postDateJson - " + " - " + e);
			s_log.log(Level.SEVERE, "* Error *  Planifico - postDateJson - SQL "  , e );
		}

	    return jsonObj;
	
	}

	
	private void UpdateBanque( String bizRefId, String userId, BigDecimal Sick_Leave, BigDecimal timeoff, BigDecimal General )
	{
		String sUserName = "yumyum1";
		String sPassword = "g34g23ft34t3tgsdeg34";
		
//		String s_Sick_Leave = "40000"; //(Sick_Leave.multiply( new BigDecimal(1000) )).toString().replace(".00", "");
//		String s_timeoff  = "00000"; //(timeoff.multiply( new BigDecimal(1000) )).toString().replace(".00", "");
//		String s_General  = "20000";//(General.multiply( new BigDecimal(1000) )).toString().replace(".00", "");

		String s_Sick_Leave = (Sick_Leave.multiply( new BigDecimal(10) )).setScale(0,BigDecimal.ROUND_HALF_UP) .toString().replace(".00", "");
		String s_timeoff  = (timeoff.multiply( new BigDecimal(10) )).toString().replace(".00", "");
		String s_General  = (General.multiply( new BigDecimal(1000) )).toString().replace(".00", "");
		
		P_Period Period = P_Period.getOpenPeriod( Env.getCtx(), null );
		
		String pattern = "yyyy/MM/dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		String s_Date = simpleDateFormat.format( Period.getStartDate() );
		
		
        JSONObject data = new JSONObject();
        try {
	        data.put("username", sUserName);
	        data.put("pass", sPassword);
	        data.put("bizRefId", bizRefId ); //use "101" for Warwick and use "102" for Quebec

	        JSONObject tmp = new JSONObject( "{listOfUsers : [{ "
	        		+ "			userId : \" " + userId + "\","
	        		+ "			listOfHourBanksInfo : ["
	        		+ "				{ id : \"general\", amountX1000 : " + s_General + " , fromDate : \"2023/09/11\"},"
	        		+ "				{ id : \"sick_leave\", amountX1000 : " + s_Sick_Leave + ", fromDate : \"2023/09/11\"},"
	        		+ "				{ id : \"timeoff\", amountX1000 : " + s_timeoff + " , fromDate : \"2023/09/11\"}"
	        		+ "			]"
	        		+ "		}]}"
	        		+ "");

/*
	        JSONObject listOfHourBanksInfo = new JSONObject( "{listOfUsers : [{ "
	        		+ "			userId : \" " + userId + "\","
	        		+ "			listOfHourBanksInfo : ["
	        		+ "				{ id : \"general\", amountX1000 : " + s_General + " , fromDate : \"2023/09/11\"},"
	        		+ "				{ id : \"sick_leave\", amountX1000 : " + s_Sick_Leave + ", fromDate : \"2023/09/11\"},"
	        		+ "				{ id : \"timeoff\", amountX1000 : " + s_timeoff + " , fromDate : \"2023/09/11\"}"
	        		+ "			]"
	        		+ "		}]}"
	        		+ "");
*/	        		
/*
{"pass":"g34g23ft34t3tgsdeg34","bizRefId":102,"listOfUsers":[{"listOfHourBanksInfo":[{"fromDate":"2023/09/11","id":"general","amountX1000":"0.00"},{"fromDate":"2023/09/11","id":"sick_leave","amountX1000":"0.00"},{"fromDate":"2023/09/11","id":"timeoff","amountX1000":"0.00"}],"userId":"64fa1e0943683624d8292342"}],"username":"yumyum1"}
*/
	        JSONArray listOfUsers = new JSONArray();  
//	        listOfUsers.put("listOfUsers");
        
	        JSONObject bq1 = new JSONObject( );
//	        bq1.put("fromDate", "2023/09/11");
	        bq1.put("fromDate", s_Date );
	        bq1.put("amountX1000", s_General);
	        bq1.put("id", "general");


	        JSONObject obj2 = new JSONObject( );
	        obj2.put("userId", userId);

	        
	        JSONArray listOfHourBanksInfo = new JSONArray();  
//	        listOfHourBanksInfo.add("listOfHourBanksInfo");
	        listOfHourBanksInfo.put( bq1 );

	        JSONObject bq2 = new JSONObject( );
	        bq2.put("fromDate", s_Date);
	        bq2.put("amountX1000", s_Sick_Leave);
	        bq2.put("id", "sick_leave");
	        listOfHourBanksInfo.put( bq2 );

	        JSONObject bq3 = new JSONObject( );
	        bq3.put("fromDate", s_Date);
	        bq3.put("amountX1000", s_timeoff);
	        bq3.put("id", "timeoff");
	        listOfHourBanksInfo.put( bq3 );

	        
//	        listOfHourBanksInfo.add("userId", userId);

//	        listOfUsers.put("listOfHourBanksInfo", listOfHourBanksInfo );
	        
	        
	        
//	        JSONObject obj3 = new JSONObject( );
//	        obj3.put("listOfHourBanksInfo", listOfHourBanksInfo);

	        obj2.put("listOfHourBanksInfo", listOfHourBanksInfo );

	        listOfUsers.put( obj2 );

	        JSONObject data2 = new JSONObject();
	        data2.put("listOfUsers", listOfUsers);
	        
	        data.put( "data", data2);
	        
	        System.out.println ( "data : " + data );

	        postDateJson( data );
	        
    	}
		catch (Exception e)
		{
			System.out.println ("* Error * Planifico - UpdateBanque - " + " - " + e);
			s_log.log(Level.SEVERE, "* Error *  Planifico - UpdateBanque - SQL "  , e );
		}


	}
	
	protected void prepare()
    {
		
    }
	protected String doIt() throws Exception
	{
		boolean retValue = true;
		String ret = "Mise a jour terminée";

		String sql = "SELECT CASE WHEN P_Employee.AD_ORG_ID is null THEN '102' WHEN P_Employee.AD_ORG_ID = 1000001  THEN '102' ELSE '101' END as bizRefId, DB_PLANIFICO_EMPLOYE.planificoUserId, P_EMPLOYEE.VALUE, P_EMPLOYEE.name "
				 // Sick_Leave Maladie
				+ ",(isnull(( SELECT SUM( P_EMPLOYEE_CREDITS.TotalCredits) FROM P_EMPLOYEE_CREDITS WHERE P_EMPLOYEE_CREDITS.ISACTIVE = 'Y' AND P_EMPLOYEE_CREDITS.P_EMPLOYEE_ID = P_EMPLOYEE.P_EMPLOYEE_ID AND P_CREDITS_ID in ( select p_credits_ID from p_credits where VALUE in ( 'BM01', 'BM04', 'BM06'))),0) * 100) + (isnull(( SELECT SUM( P_EMPLOYEE_CREDITS.TotalCredits) FROM P_EMPLOYEE_CREDITS WHERE P_EMPLOYEE_CREDITS.ISACTIVE = 'Y' AND P_EMPLOYEE_CREDITS.P_EMPLOYEE_ID = P_EMPLOYEE.P_EMPLOYEE_ID AND P_CREDITS_ID in ( select p_credits_ID from p_credits where VALUE in ( 'BM05'))),0) * isnull( Day_hours,8) * 100) Sick_Leave"
				// timeoff Vacances
				+ ",isnull(( SELECT SUM( P_EMPLOYEE_CREDITS.TotalCredits) FROM P_EMPLOYEE_CREDITS WHERE P_EMPLOYEE_CREDITS.ISACTIVE = 'Y' AND P_EMPLOYEE_CREDITS.P_EMPLOYEE_ID = P_EMPLOYEE.P_EMPLOYEE_ID AND P_CREDITS_ID in ( select p_credits_ID from p_credits where P_UOM_ID = '102' and VALUE LIKE 'BV%')),0) * isnull( Day_hours ,8) timeoff "
				// General - Accumulation du temps.
				+ ",isnull(( SELECT SUM( P_EMPLOYEE_CREDITS.TotalCredits) FROM P_EMPLOYEE_CREDITS WHERE P_EMPLOYEE_CREDITS.ISACTIVE = 'Y' AND P_EMPLOYEE_CREDITS.P_EMPLOYEE_ID = P_EMPLOYEE.P_EMPLOYEE_ID AND P_CREDITS_ID in ( select p_credits_ID from p_credits where VALUE in ( 'BT01', 'BT02', 'BT03'))),0) General "
				+ " From DB_PLANIFICO_EMPLOYE " 
				+ " LEFT OUTER JOIN P_EMPLOYEE ON P_EMPLOYEE.value = DB_PLANIFICO_EMPLOYE.numEmp "
				+ " LEFT OUTER JOIN P_ASSIGNMENT on P_ASSIGNMENT.P_EMPLOYEE_ID = P_EMPLOYEE.P_EMPLOYEE_ID AND   AssignmentType = 'P' "
				+ " LEFT OUTER JOIN P_Assignment_Param ON P_Assignment_Param.P_ASSIGNMENT_ID = P_ASSIGNMENT.P_ASSIGNMENT_ID  AND ( EFFECTTO is null OR EFFECTTO > GETDATE() ) "
				
				+ " WHERE P_EMPLOYEE.ISACTIVE = 'Y' "
//				P_Employee.AD_ORG_ID = 1000002 
//				
				;
		
        PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement(sql, null);
		
		ResultSet rs = pstmt.executeQuery();
		while (rs.next())
		{
			UpdateBanque( rs.getString( "bizRefId"), rs.getString("planificoUserId"), rs.getBigDecimal("Sick_Leave"), rs.getBigDecimal( "timeoff"), rs.getBigDecimal( "General"));
		}
		rs.close();
		pstmt.close();
		pstmt = null;
        
		return retValue == true ? ret : "error";
	}

	public static void main(final String[] args) throws Exception 
	{
		org.compiere.Compiere.startupEnvironment(true);
		new UpdatePlanifico();
 
		

	}	

	
}
