package solstice.model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Planifico {

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (Planifico.class);

	public Planifico ()
	{
	}	
	
	public static String ImportPlanifico( int P_Period_ID , int Employee_ID, String sBizRefId, String sBizSubLabel )
	{
		
		String sUrl      = "https://api1.planifico.co/API_Punch/GetEmpAndPunch" ;
		String sUserName = "yumyum1";
		String sPassword = "g34g23ft34t3tgsdeg34";
//		String sBizRefId = "101";
//		String sBizSubLabel = "Warwick";
		
//		String sBizRefId = "102";
//		String sBizSubLabel = "Quebec";

//		String sql1 = "TRUNCATE TABLE DB_PLANIFICO_PUNCH_V2";
		String sql1 = "DELETE FROM DB_PLANIFICO_PUNCH_V2 WHERE BizSubLabel = " + DB.TO_STRING( sBizSubLabel ); 
		
   		if ( Employee_ID != 0 )
		{
   			P_Employee Employee = P_Employee.get( Env.getCtx(), Employee_ID, null);
   			sql1 = sql1 + " AND numEmp =  " + DB.TO_STRING( Employee.getValue());
		}

		
        DB.executeUpdate(sql1.toString(), null);
        

        String sql2 = "DELETE FROM P_Punch_Time WHERE BizSubLabel = " + DB.TO_STRING( sBizSubLabel );

   		if ( Employee_ID != 0 )
		{
   			sql2 = sql2 + " AND P_Employee_ID =  " + Employee_ID;
		}
        
        DB.executeUpdate(sql2, null);

        
		
		
        try {
            // Construct manually a JSON object in Java, for testing purposes an object with an object
            JSONObject data = new JSONObject();
//            JSONObject auth = new JSONObject();
        
            data.put("username", sUserName);
            data.put("pass", sPassword);
            data.put("bizRefId", sBizRefId);
            data.put("bizSubLabel", sBizSubLabel);
            
            P_Period Period;
            
            if ( P_Period_ID != 0 )
            	Period = new P_Period( Env.getCtx(), P_Period_ID, null);
            else
            	Period = P_Period.getOpenPeriod(Env.getCtx(), 0, null);
           
            
			DateFormat df = new SimpleDateFormat("yyyy/MM/dd");

			// Using DateFormat format method we can create a string 
			// representation of a date with the defined format.
			String startDate = df.format( Period.getStartDate());
			String endDate = df.format( Period.getEndDate());
//			String endDate = df.format( TimeUtil.addDays( Period.getEndDate(), 30));

            data.put("startDate", startDate );
            data.put("endDate", endDate );
            
//            data.put("startDate", "2022/03/20");
//            data.put("endDate", "2022/03/26");


            
//            System.out.println( data );

            // URL and parameters for the connection, This particulary returns the information passed
            URL url = new URL(sUrl);
            HttpURLConnection httpConnection  = (HttpURLConnection) url.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setRequestProperty("Accept", "application/json");
            // Not required
            // urlConnection.setRequestProperty("Content-Length", String.valueOf(input.getBytes().length));

            // Writes the JSON parsed as string to the connection
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
            bufferedReader.close();

            // Prints the response
//            System.out.println(content.toString());

            String jsonString = content.toString();
            
            JsonObject jsonObj = JsonParser.parseString(jsonString).getAsJsonObject();

            JsonArray jarray  = jsonObj.getAsJsonArray("listOfUserData");


            for (int i = 0; i < jarray.size(); i++)
            {
            	
	            JsonObject jobject = jarray.get(i).getAsJsonObject();

	            JsonElement numEmp = jobject.get("numEmp");
	            JsonElement firstName = jobject.get("firstName");
	            JsonElement lastName = jobject.get("lastName");
	            
//	            create_employee( jobject, sBizRefId, sBizSubLabel );
	            

	            if ( ! numEmp.toString().replace("\"", "").trim().equals("") )
	            {

//	            	if ( numEmp.toString().replace("\"", "").trim().equals("1971"))
//	            		System.out.println( "Employee :" + numEmp + " " + firstName + " " + lastName );
	            	
//	            	System.out.println( "listOfUserData :" + jobject );
//		            System.out.println( "Employee :" + numEmp + " " + firstName + " " + lastName );
	            	
	            	JsonArray jarray3  = jobject.getAsJsonArray("listOfPayPeriodLinesForThisUser");
		            for (int j = 0; j < jarray3.size(); j++)
		            {

			            JsonObject jobject3 = jarray3.get(j).getAsJsonObject();
//			            System.out.println( "jobject3 :" + jobject3 );
			            
			            JsonElement planificoUserId = jobject.get("planificoUserId");

//			            listOfPayPeriodLinesForThisUser":[{"date":"2022/03/07","startTime":"08:00","endTime":"08:00","wgLabel":"","wgId":null,"qty":"8.00","roleLabel":"ExpÃ©dition/RÃ©ception","rate":"0.00","mgrNote":"","lineId":"tspl_1619727232296_23692_api1_1","lineLabel":"Retraite progressive","lineRefCode":"0076","lineToBePaid":"true"}
			            
			            JsonElement lineId = jobject3.get("lineId");
			            JsonElement lineLabel = jobject3.get("lineLabel");
			            JsonElement isApproved = jobject3.get("lineToBePaid");
			            
//			            JsonElement numEmp = jobject3.get("numEmp");
			            JsonElement date      = jobject3.get("date");
			            JsonElement startTime = jobject3.get("startTime"); 
			            JsonElement endTime   = jobject3.get("endTime");
			            JsonElement wgLabel   = jobject3.get("wgLabel");
			            JsonElement wgId      = jobject3.get("wgId");
			            JsonElement qty       = jobject3.get("qty");
			            JsonElement roleLabel = jobject3.get("roleLabel");
			            JsonElement rate      = jobject3.get("rate");
			            JsonElement mgrNote   = jobject3.get("mgrNote");
			            JsonElement reg       = jobject3.get("reg");
			            JsonElement lineRefCode  = jobject3.get("lineRefCode");
			            JsonElement lineToBePaid = jobject3.get("lineToBePaid");
//			            JsonElement codeFonction = jobject3.get("codeFonction");

			            String Codegain = lineRefCode.getAsString();
			            String s_lineRefCode = lineRefCode.getAsString();
			            
			            if ( lineRefCode.getAsString().equals("0018") &&
			            	 lineLabel.getAsString().equals("Absence non-auto.")	)
			            {
			            	Codegain = "0018N";
			            	s_lineRefCode = "0018N";
			            }
			            	
			            
			        //    jobject3 :{"date":"2022/03/07","startTime":"08:00","endTime":"08:00","wgLabel":"","wgId":null,"qty":"8.00","roleLabel":"ExpÃ©dition/RÃ©ception","rate":"0.00","mgrNote":"","lineId":"tspl_1619727232296_23692_api1_1","lineLabel":"Retraite progressive","lineRefCode":"0076","lineToBePaid":"true"}
//			            if ( ! isApproved.toString().equals("null" ))
			            //2024-07-08 ne pas importé si le wgID est a null.
// 2025-10-14			            if ( ! wgId.isJsonNull() )
			            {
				            StringBuffer sql = new StringBuffer ("" );
				            
				            sql.append( "INSERT INTO [dbo].[DB_PLANIFICO_PUNCH_V2]"
				            		+ "([planificoUserId]"
				            		+ ",[numEmp]"
				            		+ ",[date]"
				            		+ ",[lineId]"
				            		+ ",[lineLabel]"
				            		+ ",[lineRefCode]"
				            		+ ",[roleLabel]"
				            		+ ",[qty]"
				            		+ ",[isApproved]"
				            		+ ",[mgrNote]"
				            		+ ",[rate]"
				            		+ ",[endTime]"
				            		+ ",[startTime]"
				            		+ ",[wgLabel]"
				            		+ ",[wgId]"
				            		+ ",[bizRefId]"
				            		+ ",[bizSubLabel]"
				            		+ ",[lineToBePaid]"
				            		+ ",[codeFonction])"
				            		+ "Values ( " 
				            );
				            
				            sql.append(DB.TO_STRING( planificoUserId.getAsString() ));
				            sql.append(",");
				            sql.append(DB.TO_STRING( numEmp.getAsString() ));
				            sql.append(",");
				            sql.append(DB.TO_STRING( date.getAsString() ));
				            sql.append(",");
				            sql.append(DB.TO_STRING( lineId.getAsString() ));
				            sql.append(",");
				            sql.append(DB.TO_STRING( lineLabel.getAsString() ));
				            sql.append(",");
//				            sql.append(DB.TO_STRING( lineRefCode.toString().replace("\"", "") )  );
				            sql.append(DB.TO_STRING( Codegain )  );
				            sql.append(",");
				            sql.append(DB.TO_STRING( roleLabel.getAsString() ));
				            sql.append(",");
				            sql.append(DB.TO_STRING( qty.getAsString() ));
				            sql.append(",");
				            sql.append( DB.TO_STRING( " "  ) );
				            sql.append(",");
				            sql.append(DB.TO_STRING( mgrNote.getAsString()  ));
				            sql.append(",");
				            sql.append(DB.TO_STRING( rate.getAsString() ));
				            sql.append(",");
				            sql.append(DB.TO_STRING( endTime.getAsString()  )      );
				            sql.append(",");
				            sql.append(DB.TO_STRING( startTime.getAsString()  )    );
				            sql.append(",");
				            sql.append(DB.TO_STRING( wgLabel.getAsString()  )     );
				            sql.append(",");
				            if (  wgId.isJsonNull() )
				            sql.append(DB.TO_STRING( ""  )     );
				            else 
					            sql.append(DB.TO_STRING( wgId.getAsString()  )     );
				            sql.append(",");
				            sql.append(DB.TO_STRING( sBizRefId.replace("\"", "") ) );
				            sql.append(",");
				            sql.append(DB.TO_STRING( sBizSubLabel.replace("\"", "")  ) );
				            sql.append(",");
				            if ( lineToBePaid.isJsonNull() == false)
				            	sql.append(DB.TO_STRING( lineToBePaid.getAsString()  ) );
				            else
				            	sql.append(DB.TO_STRING( ""  ) );
				            sql.append(",");
				            sql.append( DB.TO_STRING( s_lineRefCode ));


				            
				            sql.append(")" );
				            
				            String sNumemp = numEmp.toString().replace("\"", "");
				            P_Employee Employee = P_Employee.getWithValue(Env.getCtx(), sNumemp, null);

				            // Employé période -1 on ne fait rien
				            if ( Employee != null && !  Employee.isCustomFieldYesNo01() )
				            {
					       		if ( Employee_ID != 0 )
					    		{
					       			if ( Employee.getValue().trim().equals( sNumemp) )
					       			{
							            DB.executeUpdate(sql.toString(), null);
					       			}

					    		}
					       		else
					    		{
						            DB.executeUpdate(sql.toString(), null);
					    		}
				            }
				            	

			            	
			            }

//			            System.out.println( "date :" + date + " startTime :" + startTime + " endTime :" + endTime + " qty :" + qty + " lineRefCode :" + lineRefCode);

		            }
	            	
	            }

	            
	            
//	            
            }


       		//2023-10-04 Update Table pour les PowerBi
//       		DB.executeUpdate("dbo.Krispy_Update_Planifico_BI", null);
                

        } catch (Exception e) {
            System.out.println("Error Message " + e);
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
            
			s_log.log(Level.SEVERE, "* Error * import planifico - "   , e );
			
   			return "Erreur importation : " + e.toString();


        }

/*		String sql2 = "DELETE FROM P_Punch_Time WHERE BizSubLabel = " + DB.TO_STRING( sBizSubLabel ); 
        DB.executeUpdate(sql2.toString(), null);

		String sql3 = "select NEXT VALUE FOR P_Punch_TimeSEQ AS P_PUNCH_TIME_ID, * INTO P_Punch_Time FROM RV_Punch_Time WHERE BizSubLabel = " + DB.TO_STRING( sBizSubLabel ); 
        DB.executeUpdate(sql3.toString(), null);
*/
        
 /*       
        String sql3 = "INSERT INTO [dbo].[P_Punch_Time] "
                + "  ([P_Punch_Time_ID] "
                + "  ,[AD_CLIENT_ID] "
                + "  ,[AD_ORG_ID] "
                + "  ,[ISACTIVE] "
  //              + "  ,[Created] "
                + "  ,[createdBy] "
  //              + "  ,[Updated] "
                + "  ,[UpdatedBy] "
                + "  ,[P_Employee_ID] "
                + "  ,[P_Period_ID] "
                + "  ,[Day] "
                + "  ,[StartTime] "
                + "  ,[EndTime] "
                + "  ,[HourlyRate] "
                + "  ,[P_Assignment_ID] "
                + "  ,[P_Distribution_ID] "
                + "  ,[P_Schedule_ID] "
                + "  ,[P_Post_ID] "
                + "  ,[P_Gain_ID] "
                + "  ,[P_Timecode_ID] "
                + "  ,[DayQty] "
                + "  ,[bizSubLabel]) "
                + "  select NEXT value for P_Punch_TimeSeq [P_Punch_Time_ID] "
                + "  ,[AD_CLIENT_ID] "
                + "  ,[AD_ORG_ID] "
                + "  ,'Y' [ISACTIVE] "
//                + "  ,getdate() [Created] "
                + "  ," + Env.getAD_User_ID( Env.getCtx() ) + " [createdBy] "
//                + "  ,getdate()[Updated] "
                + "  ," + Env.getAD_User_ID( Env.getCtx() ) + " [UpdatedBy] "
                + "  ,[P_Employee_ID] "
                + "  ,[P_Period_ID] "
                + "  ,[Day] "
                + "  ,dt_StartTime "
                + "  ,dt_EndTime "
                + "  ,[HourlyRate] "
                + "  ,[P_Assignment_ID] "
                + "  ,(select P_Distribution_ID from P_Distribution where P_Distribution.value = dbo.lpad( PAY_EQT_NO,2,'0' ) ) as  [P_Distribution_ID] "
                + "  ,[P_Schedule_ID] "
                + "  ,[P_Post_ID] "
                + "  ,[P_Gain_ID] "
                + "  ,[P_Timecode_ID] "
                + "  ,[DayQty] "
                + "   ,[bizSubLabel] " 
             	+ " from rv_punch_time "
             	+ "	WHERE rv_punch_time.BizSubLabel = " + DB.TO_STRING( sBizSubLabel );
             		;
             		
         DB.executeUpdate(sql3, null);
*/
        
        String sql3 = " SELECT [P_Employee_ID] "
        		+ "  ,[AD_CLIENT_ID] "
                + "  ,[AD_ORG_ID] "
                + "  ,[P_Period_ID] "
                + "  ,[Day] "
                + "  ,'1900-01-01 ' + dt_StartTime + '.000' dt_StartTime "
                + "  ,'1900-01-01 ' + dt_EndTime + '.000' dt_EndTime "
                + "  ,dt_EndTime "
                + "  ,[HourlyRate] "
                + "  ,[P_Assignment_ID] "
                + "  ,(select P_Distribution_ID from P_Distribution where P_Distribution.value = dbo.lpad( PAY_EQT_NO,2,'0' ) ) as  [P_Distribution_ID] "
                + "  ,[P_Schedule_ID] "
                + "  ,[P_Post_ID] "
                + "  ,[P_Gain_ID] "
                + "  ,[P_Timecode_ID] "
                + "  ,[DayQty] "
                + "   ,[bizSubLabel] " 
             	+ " from rv_punch_time "
             	+ "	WHERE rv_punch_time.BizSubLabel = " + DB.TO_STRING( sBizSubLabel )
                + " AND p_gain_id is NOT NULL"
// 2023-09-08 ne pas include les gains 0001 a 0                
                + " AND not ( DayQty_N = 0 and TimeCode = '0001' ) "
             		;

        		if ( Employee_ID != 0 )
        		{
        			sql3 = sql3 + " AND rv_punch_time.P_Employee_ID =  " + Employee_ID;
        		}
        
                PreparedStatement pstmt = null;
            	pstmt = DB.prepareStatement(sql3, null);

            	try{
            		ResultSet rs = pstmt.executeQuery();
            		while (rs.next())
            		{
            			P_Punch_Time punch_Time = new P_Punch_Time( Env.getCtx(), -1, null);
            			punch_Time.setDayQty( rs.getBigDecimal("DayQty"));

            			punch_Time.setDay( rs.getTimestamp("Day"));
            			punch_Time.setAD_Org_ID( rs.getInt( "AD_Org_ID"));
            			punch_Time.setP_Employee_ID( rs.getInt( "P_Employee_ID"));
            			punch_Time.setHourlyRate( rs.getBigDecimal( "HourlyRate") );
            			punch_Time.setStartTime( rs.getTimestamp("dt_StartTime")  );
            			punch_Time.setEndTime( rs.getTimestamp("dt_EndTime") );
            			punch_Time.setP_Assignment_ID( rs.getInt("P_Assignment_ID") );
           				punch_Time.setP_Schedule_ID( rs.getInt("P_Schedule_ID"));

            			punch_Time.setP_Post_ID(  rs.getInt("P_Post_ID") );
            			punch_Time.setP_Gain_ID(  rs.getInt("P_Gain_ID") );  
            			punch_Time.setP_Timecode_ID( rs.getInt("P_Timecode_ID")); 
            			punch_Time.setP_Distribution_ID(  rs.getInt("P_Distribution_ID") );
            			
            			punch_Time.setP_Period_ID(rs.getInt( "P_Period_ID"));
            			punch_Time.setbizSubLabel( rs.getString( "bizSubLabel") );
            	             		
            			punch_Time.save();
            		}
            	}
           		catch (Exception e)
           		{
           			s_log.log(Level.SEVERE, e.toString());
           			return "Erreur importation : " + e.toString();
           		}

        // Patch pour les employés en pré-retraite 2023-12-11    	
//        ExceptionMaladie();
// Enlever a la demande de Cynthia            	
        
        update_punch_time( P_Period_ID );
        
        return null;
	}
	
	private static void create_employee( JsonObject jobject, String sBizRefId, String sBizSubLabel )
	{
        String numEmp = jobject.get("numEmp").toString().replace("\"", "");

        
		String sql = "INSERT INTO [dbo].[DB_PLANIFICO_EMPLOYE]     "
				+ "           ([planificoUserId]                "
				+ "           ,[numEmp]                         "
				+ "           ,[statut]                         "
				+ "           ,[dateOfFirstDayOfWork]           "
				+ "           ,[dateOfBirth]                    "
				+ "           ,[dateOfHiring]                   "
				+ "           ,[dateOfTermination]              "
				+ "           ,[lang]                           "
				+ "           ,[email]                          "
				+ "           ,[mobilePhoneNum]                 "
				+ "           ,[mobilePhonePrefix]              "
				+ "           ,[mobilePhoneAmericaAreaCode]     "
				+ "           ,[sIN]                            "
				+ "           ,[firstName]                      "
				+ "           ,[lastName]                       "
				+ "           ,[mainRoleLabel]                  "
				+ "           ,[wageType]                       "
				+ "           ,[bizRefId]                       "
				+ "           ,[bizSubLabel])                   "
				+ " SELECT      " + DB.TO_STRING( jobject.get("planificoUserId").toString().replace("\"", "")              )
				+ "           , " + DB.TO_STRING( jobject.get("numEmp").toString().replace("\"", "")                       )
				+ "           , " + DB.TO_STRING( jobject.get("statut").toString().replace("\"", "")                       )
				+ "           , " + DB.TO_STRING( jobject.get("dateOfFirstDayOfWork").toString().replace("\"", "")         )
				+ "           , " + DB.TO_STRING( jobject.get("dateOfBirth").toString().replace("\"", "")                  )
				+ "           , " + DB.TO_STRING( jobject.get("dateOfHiring").toString().replace("\"", "")                 )
				+ "           , " + DB.TO_STRING( jobject.get("dateOfTermination").toString().replace("\"", "")            )
				+ "           , " + DB.TO_STRING( jobject.get("lang").toString().replace("\"", "")                         )
				+ "           , " + DB.TO_STRING( jobject.get("email").toString().replace("\"", "")                        )
				+ "           , " + DB.TO_STRING( jobject.get("mobilePhoneNum").toString().replace("\"", "")               )
				+ "           , " + DB.TO_STRING( jobject.get("mobilePhonePrefix").toString().replace("\"", "")            )
				+ "           , " + DB.TO_STRING( jobject.get("mobilePhoneAmericaAreaCode").toString().replace("\"", "")   )
				+ "           , " + DB.TO_STRING( jobject.get("sIN").toString().replace("\"", "")                          )
				+ "           , " + DB.TO_STRING( jobject.get("firstName").toString().replace("\"", "")                    )
				+ "           , " + DB.TO_STRING( jobject.get("lastName").toString().replace("\"", "")                     )
				+ "           , " + DB.TO_STRING( jobject.get("mainRoleLabel").toString().replace("\"", "")               )
				+ "           , " + DB.TO_STRING( jobject.get("wageType").toString().replace("\"", "")                     )
				+ "           , " + DB.TO_STRING( sBizRefId )
				+ "           , " + DB.TO_STRING( sBizSubLabel )
				+ " WHERE NOT EXISTS ( SELECT 1 FROM DB_PLANIFICO_EMPLOYE A WHERE A.numEmp = " + DB.TO_STRING( numEmp ) + ")";                                                                        
				;
				try {
					if ( numEmp != null && ! numEmp.equals("") )
						DB.executeUpdate(sql.toString(), null);	
				}
				catch (Exception e)
				{
					System.out.println ("* Error * Planifico - create_employee - " + sql + " - " + e);
					s_log.log(Level.SEVERE, "* Error *  Planifico - create_employee - SQL " + sql   , e );
				}
			
				
				sql = " INSERT INTO [svrsql.bureau.krispykernels.com].krispy.[dbo].[DB_PLANIFICO_EMPLOYE]                         "
								+ "            ([planificoUserId]                                                                             "
								+ "            ,[numEmp]                                                                                      "            
								+ "            ,[statut]                                                                                      "   
								+ "            ,[dateOfFirstDayOfWork]                                                                        "    
								+ "            ,[dateOfBirth]                                                                                 "         
								+ "            ,[dateOfHiring]                                                                                "
								+ "            ,[dateOfTermination]                                                                           "
								+ "            ,[lang]                                                                                        "      
								+ "            ,[email]                                                                                       "         
								+ "            ,[mobilePhoneNum]                                                                              "                  
								+ "            ,[mobilePhonePrefix]                                                                           "
								+ "            ,[mobilePhoneAmericaAreaCode]                                                                  " 
								+ "            ,[sIN]                                                                                         "
								+ "            ,[firstName]                                                                                   "     
								+ "            ,[lastName]                                                                                    "
								+ "            ,[mainRoleLabel]                                                                               "
								+ "            ,[wageType]                                                                                    "
								+ "            ,[bizRefId]                                                                                    "
								+ "            ,[bizSubLabel])                                                                                "
								+ " SELECT   [planificoUserId]                                                                                "
								+ "            ,[numEmp]                                                                                      "
								+ "            ,isnull( [statut],'')                                                                          "
								+ "            ,isnull( [dateOfFirstDayOfWork],'')                                                            "
								+ "            ,isnull( [dateOfBirth],'')                                                                     "
								+ "            ,isnull( [dateOfHiring],'')                                                                    "
								+ "            ,isnull( [dateOfTermination],'')                                                               "
								+ "            ,isnull( [lang],'')                                                                            "
								+ "            ,isnull( [email],'')                                                                           "
								+ "            ,isnull( [mobilePhoneNum],'')                                                                  "
								+ "            ,isnull( [mobilePhonePrefix],'')                                                               "
								+ "            ,isnull( [mobilePhoneAmericaAreaCode],'')                                                      "
								+ "            ,isnull( [sIN],'')                                                                             "
								+ "            ,isnull( [firstName],'')                                                                       "
								+ "            ,isnull( [lastName],'')                                                                        "
								+ "            ,isnull( [mainRoleLabel],'')                                                                   "
								+ "            ,isnull( [wageType],'')                                                                        "
								+ "            , [bizRefId]                                                                                   "
								+ "            ,[bizSubLabel]                                                                                 "
								+ " FROM [dbo].[DB_PLANIFICO_EMPLOYE]                                                                         "
								+ " WHERE numEmp is not null AND NOT EXISTS ( select 1 FROM [svrsql.bureau.krispykernels.com].krispy.[dbo].[DB_PLANIFICO_EMPLOYE] A  "
								+ " WHERE A.numEmp = DB_PLANIFICO_EMPLOYE.numEmp)                                                             "
								;
				
				try {
					if ( numEmp != null && ! numEmp.equals("") )
						DB.executeUpdate(sql.toString(), null);	
				}
				catch (Exception e)
				{
					System.out.println ("* Error * Planifico - create_employee - " + sql + " - " + e);
					s_log.log(Level.SEVERE, "* Error *  Planifico - create_employee - SQL " + sql   , e );
				}
			

				
				
	}
	

	private static int getAssignment( P_Punch_Time punchTime )
	{
		int id = 0;
//		String sql = "SELECT P_ASSIGNMENT_ID FROM P_ASSIGNMENT WHERE P_ASSIGNMENT.isActive= 'Y' AND  P_ASSIGNMENT.p_employee_id = " +  punchTime.getP_Employee_ID() + " and P_ASSIGNMENT.p_post_id = " + punchTime.getP_Post_ID() ; 
		String sql = "SELECT P_ASSIGNMENT_ID FROM P_ASSIGNMENT WHERE P_ASSIGNMENT.p_employee_id = " +  punchTime.getP_Employee_ID() + " and P_ASSIGNMENT.p_post_id = " + punchTime.getP_Post_ID() ; 
		PreparedStatement pstmt = null;
		try
		{
				pstmt = DB.prepareStatement (sql, null);
				ResultSet rs = pstmt.executeQuery ();
				if ( rs.next () )
				{
					id = rs.getInt(1);
				}
				
				rs.close ();
				pstmt.close ();
				pstmt = null;
				
		}
		catch (Exception e)
		{
			System.out.println ("* Error * Planifico - update punch_time - " + sql + " - " + e);
			s_log.log(Level.SEVERE, "* Error *  Planifico - update punch_time - SQL " + sql   , e );
		}
		
		return id;

	}

	private static void update_punch_time( int P_Period_ID  )
	{
		
		P_Period Period = new P_Period( Env.getCtx(), P_Period_ID, null);
		
		String sql = "SELECT * FROM P_PUNCH_TIME where P_ASSIGNMENT_ID IS NULL AND P_Post_ID IS NOT NULL "; 
		PreparedStatement pstmt = null;
		try
		{
				pstmt = DB.prepareStatement (sql, null);
				ResultSet rs = pstmt.executeQuery ();
				while ( rs.next () )
				{
					P_Punch_Time punchTime = new P_Punch_Time( Env.getCtx(), rs, null);
					int Assignment_ID = getAssignment( punchTime );
					
					if (  Assignment_ID == 0)
					{
						Assignment_ID = Planifico.create_assignment( Period, punchTime.getP_Employee_ID(), punchTime.getP_Post_ID() );
						punchTime.setP_Assignment_ID(  Assignment_ID );
					}
					else
					{
						punchTime.setP_Assignment_ID(  Assignment_ID );
					}
				
					punchTime.save();
					
				}
				
				rs.close ();
				pstmt.close ();
				pstmt = null;
				
		}
		catch (Exception e)
		{
			System.out.println ("* Error * Planifico - update punch_time - " + sql + " - " + e);
			
			s_log.log(Level.SEVERE, "* Error *  Planifico - update punch_time - SQL " + sql   , e );
			
		}

	}

	public static int getSalaryScale(P_Period Period, int P_Employee_ID, int P_Post_ID )
	{
		P_Employee Employee = P_Employee.get( Env.getCtx(), P_Employee_ID, null);
		
		String sql = "SELECT P_Salary_Scale_ID FROM P_Salary_Scale "
				+ "WHERE P_POST_ID =  " + P_Post_ID
				+ " and P_Salary_Scale.P_SALARY_SCALE_ID = ( SELECT TOP 1 a.P_SALARY_SCALE_ID FROM  P_SALARY_SCALE A WHERE A.P_POST_ID = " + P_Post_ID + " AND a.ScaleDate <= " + DB.TO_DATE( Period.getEndDate()) + " order by ScaleDate DESC ) "
				+ " and P_Salary_Scale.P_COLLECTIVE_LABOUR_AGR_ID = " + Employee.getP_Collective_Labour_Agr_ID()
				+ " and P_Salary_Scale.IsActive = 'Y' "
				;

		int ID = 0; 

		
		PreparedStatement pstmt = null;
		try
		{
				pstmt = DB.prepareStatement (sql, null);
				ResultSet rs = pstmt.executeQuery ();
				if ( rs.next () )
					ID = rs.getInt(1);
				
				rs.close ();
				pstmt.close ();
				pstmt = null;
				
		}
		catch (Exception e)
		{
			System.out.println ("* Error * getSalaryScale - " + sql + " - " + e);
			s_log.log(Level.SEVERE, "* Error * getSalaryScale - " + sql  , e );
			
		}


		return ID;
		
	}

	public static int getStep( P_Employee Employee, P_Period Period , P_Salary_Scale Salary_Scale, Timestamp StartDate )
	{
		int ID = 0;
		
		String sql = "";
		if ( Salary_Scale != null && Salary_Scale.getScale_Type() != null &&  Salary_Scale.getScale_Type().equals( P_Salary_Scale.SCALE_TYPE_AccordingToLimitsCredits )  )
		{
//P_Collective_Labour_Agr_ID=1000009 AND
			int nbrMonth = Employee.getSeniority( StartDate );
			sql = " SELECT P_Salary_Scale_Detail_ID From P_Salary_Scale_Detail Where  P_Salary_Scale_ID = " + Salary_Scale.getP_Salary_Scale_ID() + " AND " + nbrMonth + " between isnull( limit_min,0) and case when isnull( limit_max, 0) = 0 then 9999999999 else limit_max end ";
		}
		else
		{
			sql = " SELECT min( P_Salary_Scale_Detail_ID ) P_Salary_Scale_Detail_ID FROM P_Salary_Scale_Detail where P_Salary_Scale_ID = " + Salary_Scale.getP_Salary_Scale_ID();
		}

		PreparedStatement pstmt = null;
		try
		{
				pstmt = DB.prepareStatement (sql, null);
				ResultSet rs = pstmt.executeQuery ();
				if ( rs.next () )
					ID = rs.getInt(1);
				
				rs.close ();
				pstmt.close ();
				pstmt = null;
				
		}
		catch (Exception e)
		{
			System.out.println ("* Error * getStep - " + sql + " - " + e);
			s_log.log(Level.SEVERE, "* Error * getStep - " + sql  , e );
		}

		return ID;
			
	}
	
	

	
	
	public static int create_assignment(P_Period Period, int P_Employee_ID, int P_Post_ID )
	{
		P_Employee Employee = P_Employee.get( Env.getCtx(), P_Employee_ID, null);
		P_Post Post = P_Post.get( Env.getCtx(), P_Post_ID, null);
		
		P_Assignment Assignment = new P_Assignment( Env.getCtx(), -1 , null);
		
		Assignment.setAD_Org_ID( Employee.getAD_Org_ID() );
		Assignment.setAssignmentType(P_Assignment.ASSIGNMENTTYPE_Other);
		
			
		Assignment.setStartDate( Period.getStartDate() );
		Assignment.setEndDate(null);
		Assignment.setIsActive(true);
		Assignment.setP_Employee_ID(Employee.getP_Employee_ID());
		Assignment.setP_Assignment_State_ID(1000061);
		Assignment.setP_Post_ID( Post.getP_Post_ID() );
		if ( Post == null || Post.getValue() == null || Post.getValue().equals("") )
			Assignment.setValue( "01" );
		else	
			Assignment.setValue( Post.getValue() );
		
		
		Assignment.setName( Post.getName() );
//		Assignment.setP_Workplace_ID( Post.getP_Workplace_ID());
		Assignment.save();
		
		// Valide si l'employé doit changer de paramètre d'affactation
		
		Planifico.Create_Assignment_Detail(Employee, Period, Assignment );

		String sql = "INSERT INTO [dbo].[Lst_Employee_Assignment_Created] "
				+ "           ([AD_CLIENT_ID] "
				+ "           ,[AD_ORG_ID] "
				+ "           ,[ISACTIVE] "
				+ "           ,[CREATEDBY]"
				+ "           ,[UPDATEDBY] "
				+ "           ,[P_Period_ID] "
				+ "           ,[P_Employee_ID] "
				+ "           ,[P_Assignment_ID] "
				+ "           ,[P_Post_ID] "
				+ "           ,[Message]) "
				+ "     VALUES "
				+ "           ( " + Employee.getAD_Client_ID()
				+ "           , " + Employee.getAD_Org_ID()
				+ "           , 'Y' "
                + "           ," + Env.getAD_User_ID( Env.getCtx() ) 
                + "           ," + Env.getAD_User_ID( Env.getCtx() ) 
				+ "           ," + Period.getP_Period_ID()
				+ "           ," + Employee.getP_Employee_ID()
				+ "           ," + Assignment.getP_Assignment_ID()
				+ "           ," + Post.getP_Post_ID()
				+ "           ,'') "
				;
		  DB.executeUpdate(sql, null);
		
		return Assignment.getP_Assignment_ID();
	}

	public static void Create_Assignment_Detail( P_Employee Employee, P_Period Period, P_Assignment Assignment)
	{

		if ( Assignment.getP_Post_ID() == 0)
		{
			s_log.log(Level.SEVERE, "Affectation sans poste : Employé : " +  Employee.toString() + " Affectation " + Assignment.getValue() );
            System.out.println("Affectation sans poste : Employé : " +  Employee.toString() + " Affectation " + Assignment.getValue());
			return;
		}
		
		P_Post Post = P_Post.get( Env.getCtx(), Assignment.getP_Post_ID(), null);

		int Salary_Scale_ID = getSalaryScale( Period, Employee.getP_Employee_ID(), Post.getP_Post_ID() );
		P_Salary_Scale Salary_Scale = P_Salary_Scale.get( Env.getCtx(), Salary_Scale_ID, null);

		if ( Salary_Scale == null )
		{
			s_log.log(Level.SEVERE, "Echelle salarial non trouvé pour le poste : Employé : " +  Employee.toString() + " Poste " + Post.getValue() );
            System.out.println("Echelle salarial non trouvé pour le poste : Employé : " +  Employee.toString() + " Poste " + Post.getValue());
			return;
		}
			
		
		int Salary_Scale_Detail_ID = 0;
		Timestamp StartDate = Employee.getDateSeniority();
		
		if ( Salary_Scale != null && Salary_Scale.getScale_Type() != null && Salary_Scale.getScale_Type().equals( P_Salary_Scale.SCALE_TYPE_AccordingToLimitsCredits )  )
		{

			int maxStep = 5;
			int i = 0;

			
			// Si l'employé a plus de 2 ans
			if ( TimeUtil.addMonth(StartDate, 24).compareTo(Period.getStartDate() ) < 0 )
			{
				StartDate = Period.getStartDate();
				i = maxStep;
			}
			

			while ( ! ( StartDate.compareTo( Period.getStartDate() ) >= 0 &&
					    StartDate.compareTo( Period.getEndDate() ) <= 0 ) 
					&& i < maxStep
						)
			{
				StartDate = TimeUtil.addMonth(StartDate, 6);

				i = i + 1;

				if ( StartDate.compareTo(Period.getEndDate() ) > 0 ) 
				{
					StartDate = TimeUtil.addMonth(StartDate, -6);
					i = maxStep;
				}

			}
			Salary_Scale_Detail_ID = getStep( Employee, Period, Salary_Scale, StartDate );

			
		}
		if ( Salary_Scale_Detail_ID == 0 )
		{
			Salary_Scale_Detail_ID = getStep( Employee, Period, Salary_Scale, Period.getStartDate() );
		}
		

		if ( Assignment.getP_Assignment_ID() != 0 && Salary_Scale_Detail_ID != 0 ) 
		{
			if ( Assignment.getStartDate().compareTo(StartDate) > 0)
			{
				Assignment.setStartDate(StartDate);
				Assignment.save();
			}

			P_Assignment_Param AssignmentParam = new P_Assignment_Param( Env.getCtx(), -1, null);
			P_Salary_Scale_Detail Salary_Scale_Detail = P_Salary_Scale_Detail.get( Env.getCtx(), Salary_Scale_Detail_ID, null);
			P_Occupation_Group Occupation_Group = new P_Occupation_Group( Env.getCtx(), Salary_Scale.getP_Occupation_Group_ID(), null);
				
			AssignmentParam.setIsOut_Of_Range(false);
			AssignmentParam.setWeekly_Hours( Occupation_Group.getWeekly_Hours() );
			AssignmentParam.setP_Salary_Scale_ID( Salary_Scale_Detail.getP_Salary_Scale_ID());
			AssignmentParam.setP_Salary_Scale_Detail_ID( Salary_Scale_Detail.getP_Salary_Scale_Detail_ID());
			AssignmentParam.setP_Collective_Labour_Agr_ID( Salary_Scale.getP_Collective_Labour_Agr_ID() );
			AssignmentParam.setHourly_Rate( Salary_Scale_Detail.getHourly_Rate() );
			AssignmentParam.setDay_Hours( Occupation_Group.getDay_Hours()  );
			AssignmentParam.setEffectIn( Assignment.getStartDate() );
//			AssignmentParam.setEffectIn( Period.getStartDate() );
			AssignmentParam.setP_Occupation_Group_ID(Salary_Scale.getP_Occupation_Group_ID());
			AssignmentParam.setRemuneration_Method(P_Assignment_Param.REMUNERATION_METHOD_Hourly);
			AssignmentParam.setP_Assignment_ID(Assignment.getP_Assignment_ID());
			AssignmentParam.setIsActive(true);
			AssignmentParam.save();
		}
		else {
			s_log.log(Level.SEVERE, "Affectation créer sans paramètre : Employé : " +  Employee.toString() + " Affectation " + Assignment.getValue() );
            System.out.println("Affectation créer sans paramètre : Employé : " +  Employee.toString() + " Affectation " + Assignment.getValue());
	          
		}
	}	

	/*
	private static void ExceptionMaladie ()
	{
		String sql = " SELECT P_PUNCH_TIME.* FROM P_PUNCH_TIME "
				+ " INNER JOIN P_EMPLOYEE ON P_EMPLOYEE.P_EMPLOYEE_ID = P_PUNCH_TIME.P_EMPLOYEE_ID "
				+ " WHERE P_GAIN_ID IN ( SELECT P_GAIN_ID FROM P_GAIN WHERE VALUE IN ( 'GC02','GC05') ) "
				+ " AND StatutoryHolidayGenerationType = " + DB.TO_STRING( P_Employee.STATUTORYHOLIDAYGENERATIONTYPE_MoyenSur37Hours) 
					;

		PreparedStatement pstmt = null;
		try
		{
				ArrayList<BigDecimal> admissiblevalue = new ArrayList<BigDecimal>(2);
				pstmt = DB.prepareStatement (sql, null);
				ResultSet rs = pstmt.executeQuery ();
				while ( rs.next () )
				{
					P_Employee Employee = P_Employee.get( Env.getCtx(), rs.getInt("P_Employee_ID"), null);
					P_Period Period = P_Period.get(Env.getCtx(), rs.getInt("P_Period_ID"), null);
					P_Gain Gain = P_Gain.get(Env.getCtx(), rs.getInt("P_Gain_ID"), null);
					P_Assignment Assignment = P_Assignment.get(Env.getCtx(), rs.getInt("P_Assignment_ID"), null);
					
					P_Assignment_Param AssignmentParam = P_Assignment_Param.get( Env.getCtx(), Assignment.getP_Assignment_ID(), rs.getTimestamp("Day"), null);

					admissiblevalue = Employee.getIndemnityStatutoryHolidays( Period.getStartDate(), rs.getTimestamp("Day"), AssignmentParam.getP_Assignment_Param_ID(), Gain.getP_Column_Adm_ID(), null, null );
					
					BigDecimal Dayqty = (BigDecimal)admissiblevalue.get(0);
					
					String sqlU = "Update P_PUNCH_TIME set Dayqty = " + Dayqty + " WHERE P_PUNCH_TIME_ID = " + rs.getInt( "P_PUNCH_TIME_ID");
					DB.executeUpdate(sqlU, null);
					
				}
				rs.close ();
				pstmt.close ();
				pstmt = null;
				
		}
		catch (Exception e)
		{
			System.out.println ("* Error * getStep - " + sql + " - " + e);
			s_log.log(Level.SEVERE, "* Error * getStep - " + sql  , e );
		}
	
		
	}
	*/
	
	
}
