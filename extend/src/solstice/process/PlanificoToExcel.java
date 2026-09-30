package solstice.process;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.Compiere;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.write.DateFormats;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import solstice.model.P_Distribution;
import solstice.model.P_Employee;
import solstice.model.P_Gain;
import solstice.model.P_Period;
import solstice.model.P_Timecode;
import solstice.model.Planifico;
import solstice.utils.PgiUtil;
import solstice.utils.TimeUtilSolstice;

public class PlanificoToExcel extends SvrProcess{

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (PlanificoToExcel.class);

	protected void prepare()
	{
		
	}
	protected String doIt() throws Exception
	{
		boolean retValue = true;
		String ret = "Importation terminée";
		
		String sql1 = "TRUNCATE TABLE KrispyPlanificoExcel";
        DB.executeUpdate(sql1, null);
		
		PlanificoToExcel.CreateExcelFile( );
		
		return retValue == true ? ret : "error";
	}
	
	public PlanificoToExcel ()
	{
	}	

	private static void writeDataSheetHeader(WritableSheet s, String sBizRefId, String sBizSubLabel) 
			    throws WriteException
	{
	    /* Format the Font */
	    WritableFont wf = new WritableFont(WritableFont.ARIAL, 
	      10, WritableFont.BOLD);
	    WritableCellFormat cf = new WritableCellFormat(wf);
	    cf.setWrap(true);
	    Colour c = Colour.GREY_25_PERCENT;
	    cf.setBackground(c);
	    cf.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);

	    
	    /* Creates Label and writes date to one cell of sheet*/
	    Label l = new Label(0,0,"Société",cf);
	    s.addCell(l);
	    l = new Label(1,0,"Division",cf);
	    s.addCell(l);
	    l = new Label(2,0,"Période",cf);
	    s.addCell(l);
	    l = new Label(3,0,"Employé",cf);
	    s.addCell(l);
	    l = new Label(4,0,"Jour",cf);
	    s.addCell(l);
	    l = new Label(5,0,"Heure début",cf);
	    s.addCell(l);
	    l = new Label(6,0,"Heure fin",cf);
	    s.addCell(l);
	    l = new Label(7,0,"Poste",cf);
	    s.addCell(l);
	    l = new Label(8,0,"Distribution",cf);
	    s.addCell(l);
	    l = new Label(9,0,"Horaire",cf);
	    s.addCell(l);
	    l = new Label(10,0,"Quantité",cf);
	    s.addCell(l);
	    l = new Label(11,0,"Code de temps",cf);
	    s.addCell(l);
	    l = new Label(12,0,"Code de rémunération",cf);
	    s.addCell(l);
		 
	}

	 private static int writeDataSheet(WritableSheet s, String sBizRefId, String sBizSubLabel) 
			    throws WriteException
	{
		int cnt = 0;
		String sUrl      = "https://api1.planifico.co/API_Punch/GetEmpAndPunch" ;
		String sUserName = "yumyum1";
		String sPassword = "g34g23ft34t3tgsdeg34";

	    WritableFont wf2 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
	  	WritableCellFormat cf2 = new WritableCellFormat(wf2);
	  	cf2.setWrap(true);
	    WritableCellFormat nf1 = new WritableCellFormat(NumberFormats.FLOAT);
	    WritableCellFormat nf2 = new WritableCellFormat(NumberFormats.INTEGER);
	    WritableCellFormat df1 = new WritableCellFormat(DateFormats.DEFAULT);
		
	    
		try {
			
            JSONObject data = new JSONObject();
            // Construct manually a JSON object in Java, for testing purposes an object with an object
            data.put("username", sUserName);
            data.put("pass", sPassword);
            data.put("bizRefId", sBizRefId);
            data.put("bizSubLabel", sBizSubLabel);

            Timestamp ts=new Timestamp(new Date().getTime());  
            
			DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
			String startDate = df.format( TimeUtilSolstice.addMonth( ts, -3) );
			String endDate = df.format( new Date() );

//            data.put("startDate", "2023/11/01");
//            data.put("startDate", "2024/01/01");
            data.put("startDate", startDate );
            data.put("endDate", endDate );
			            
//            data.put("endDate", "2023/12/26");

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
            String jsonString = content.toString();
			            
            JsonObject jsonObj = JsonParser.parseString(jsonString).getAsJsonObject();
            JsonArray jarray  = jsonObj.getAsJsonArray("listOfUserData");

            for (int i = 0; i < jarray.size(); i++)
			{

			   JsonObject jobject = jarray.get(i).getAsJsonObject();
	           JsonElement numEmp = jobject.get("numEmp");
	           JsonElement firstName = jobject.get("firstName");
	           JsonElement lastName = jobject.get("lastName");
	            
	           JsonArray jarray3  = jobject.getAsJsonArray("listOfPayPeriodLinesForThisUser");
	           if ( ! numEmp.toString().replace("\"", "").trim().equals("") )
	           {

	        	   P_Employee Employee = getEmployeeValue( Env.getCtx(), numEmp.toString().replace("\"", ""), null );
	        	   P_Distribution Distribution = P_Distribution.get( Env.getCtx(), Employee.getP_Distribution_ID(), null);
	        	   
	        	   for (int j = 0; j < jarray3.size(); j++)
		           {
					   cnt = cnt + 1;
//			            System.out.println("Info :" + cnt + " " + numEmp.toString().replace("\"", "") );

			            JsonObject jobject3 = jarray3.get(j).getAsJsonObject();
			            JsonElement planificoUserId = jobject.get("planificoUserId");

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
//				            JsonElement codeFonction = jobject3.get("codeFonction");
			            
			            String day = date.toString().replace("\"", "");
			            int iyear = Integer.parseInt( day.substring(0,4) );
			            int imonth = Integer.parseInt( day.substring(5,7) );
			            int iday = Integer.parseInt( day.substring(8,10) );
			            
			            P_Period Period = P_Period.get(Env.getCtx(), TimeUtil.getDay( iyear, imonth, iday ), null);
			            
			            Label l0 = new Label(0,cnt,"ALIMENTS KRISPY" ,cf2);
					    s.addCell(l0);
			            Label l1 = new Label(1,cnt,sBizSubLabel ,cf2);
					    s.addCell(l1);
			            Label l2 = new Label(2,cnt,Period.getName() ,cf2);
					    s.addCell(l2);
			            Label l3 = new Label(3,cnt,numEmp.getAsString() + " " + Employee.getName() ,cf2);
					    s.addCell(l3);
					    Label l4 = new Label(4,cnt,date.getAsString(),cf2);
					    s.addCell(l4);
					    Label l5 = new Label(5,cnt,startTime.getAsString(),cf2);
					    s.addCell(l5);
					    Label l6 = new Label(6,cnt,endTime.getAsString(),cf2);
					    s.addCell(l6);
					    Label l7 = new Label(7,cnt,roleLabel.getAsString(),cf2);
					    s.addCell(l7);
					    Label l8 = new Label(8,cnt,Distribution.getValue() + "-" + Distribution.getName(),cf2);
					    s.addCell(l8);
					    Label l9 = new Label(9,cnt,wgLabel.getAsString(),cf2);
					    s.addCell(l9);
//					    Label l6 = new Label(6,cnt,qty.toString().replace("\"", ""),cf2);
					    Number nI2 = new Number(10,cnt, Double.parseDouble( qty.getAsString() ) ,nf1);
					    s.addCell(nI2);
					    
			        	P_Timecode Timecode = getTimecodeValue( Env.getCtx(), lineRefCode.getAsString(), null );
			        	P_Gain Gain = getGaincode( Env.getCtx(), Employee.getP_Employee_ID(), Timecode.getP_Timecode_ID(), null );

					    Label l11 = new Label(11,cnt,Timecode.getValue() + "-" + Timecode.getName() ,cf2);
					    s.addCell(l11);
					    Label l12 = new Label(12,cnt,lineLabel.getAsString(),cf2);
					    s.addCell(l12);
					    
					    String sql = "INSERT INTO [dbo].[KrispyPlanificoExcel]\r\n"
					    		+ "           ([Société]\r\n"
					    		+ "           ,[Division]\r\n"
					    		+ "           ,[Période]\r\n"
					    		+ "           ,[Employé]\r\n"
					    		+ "           ,[Jour]\r\n"
					    		+ "           ,[Heure debut]\r\n"
					    		+ "           ,[Heure fin]\r\n"
					    		+ "           ,[Poste]\r\n"
					    		+ "           ,[Distribution]\r\n"
					    		+ "           ,[Horaire]\r\n"
					    		+ "           ,[Quantité]\r\n"
					    		+ "           ,[Code de temps]\r\n"
					    		+ "           ,[Code de rémunération]\r\n"
					    		+ "           ,[CodeTemps]"
					    		+ "           ,[CodeGain]"
					    		+ "           ,[codeEmploye]"
					    		+ ")\r\n"
					    		+ " VALUES ( "
					    		+ DB.TO_STRING( "ALIMENTS KRISPY " ) + ","
					    		+ DB.TO_STRING( sBizSubLabel ) + ","
					            + DB.TO_STRING( Period.getName() ) + ","
					            + DB.TO_STRING( numEmp.getAsString() + " " + Employee.getName() ) + "," 
					            + DB.TO_STRING( date.getAsString() ) + ","
					            + DB.TO_STRING(startTime.getAsString() ) + ","
					            + DB.TO_STRING(endTime.getAsString() ) + ","
					            + DB.TO_STRING(roleLabel.getAsString() ) + ","
					            + DB.TO_STRING(Distribution.getValue() + "-" + Distribution.getName() ) + ","
					            + DB.TO_STRING(wgLabel.getAsString() ) + ","
					            + DB.TO_STRING( qty.getAsString() ) + ","
							    
								+ DB.TO_STRING(Timecode.getValue() + "-" + Timecode.getName() ) + ","
								+ DB.TO_STRING( lineLabel.getAsString() )  + ","
								+ DB.TO_STRING(Timecode.getValue() ) + ","
								+ DB.TO_STRING( Gain.getValue() ) + "," 
					            + DB.TO_STRING( numEmp.getAsString()  ) 
								
								+ ") "
							    ;
					    
					    DB.executeUpdate(sql, null);

		           }
	           }
	        	   
            }

       } catch (Exception e) {
           System.out.println("Error Message " + e);
           s_log.log(Level.SEVERE, "* Error * PlanificoToExcel - "   , e );
			
       }
		
 	   String sql = "update KrispyPlanificoExcel set CodeConfig = ( select top 1 CodeConfig from DBO.KrispyPlanificoConfig Where [Remunération] = CodeGain )\r\n"
				+ ", [Code de rémunération] = ( select top 1 Description from P_GAIN where P_GAIN.VALUE = CodeGain)"
 			    + ", ConfigPoste = ( SELECT TOP 1 Analyse FROM KrispyPlanificoConfigPoste Where KrispyPlanificoExcel.Poste = [Nom Planifico] ) "
				;
	   DB.executeUpdate(sql, null);
	/*	
 	   sql = "update KrispyPlanificoExcel set post = ( SELECT TOP 1 P_POST.VALUE FROM P_POST INNER JOIN P_EMPLOYEE ON P_EMPLOYEE.AD_ORG_ID = P_POST.AD_ORG_ID AND P_EMPLOYEE.value = codeEmploye " 
				+ "  inner join DB_PLANIFICO_POSTE on DB_PLANIFICO_POSTE.name = rtrim([Poste]) "
				+ " ) "
				;
	   DB.executeUpdate(sql, null);
	  */ 
	   sql = "update KrispyPlanificoExcel set post = ( SELECT DB_PLANIFICO_POSTE.Code FROM  DB_PLANIFICO_POSTE WHERE DB_PLANIFICO_POSTE.name = KrispyPlanificoExcel.[Poste]) where post is null ";
	   DB.executeUpdate(sql, null);
	   
	   sql = "update KrispyPlanificoExcel set codeEmploye = rtrim(substring([Employé],1,5)) where codeEmploye is null";
	   DB.executeUpdate(sql, null);
	   
	   sql = "update KrispyPlanificoExcel set hourly_rate "
	   		+ "	   = ( select TOP 1 hourly_rate from P_ASSIGNMENT "
	   		+ "	   			inner join P_ASSIGNMENT_PARAM on P_ASSIGNMENT_PARAM.P_ASSIGNMENT_id = P_ASSIGNMENT.P_ASSIGNMENT_id "
	   		+ "	   			and to_date(replace(jour, '/',''), '') between P_ASSIGNMENT_PARAM.effectin and isnull( P_ASSIGNMENT_PARAM.effectto, to_date(replace(jour, '/',''), '') ) "
	   		+ "	   			where P_ASSIGNMENT.p_post_id in ( select p_post_id from p_post where p_post.value = KrispyPlanificoExcel.post ) "
	   		+"	  		    and P_ASSIGNMENT.P_Employee_Id = ( select p_employee_id from p_employee where p_employee.value = KrispyPlanificoExcel.codeEmploye ) "
	   		+ "	   			and P_ASSIGNMENT.ISACTIVE = 'Y' "
	   		+ "	   			)";

	   DB.executeUpdate(sql, null);
	   
	   sql = "update KrispyPlanificoExcel set hourly_rate "
	   		+ "= ( select TOP 1 max(hourly_rate) from P_ASSIGNMENT "
	   		+ "			inner join P_ASSIGNMENT_PARAM on P_ASSIGNMENT_PARAM.P_ASSIGNMENT_id = P_ASSIGNMENT.P_ASSIGNMENT_id "
	   		+ "			and P_ASSIGNMENT_PARAM.effectin < to_date(replace(jour, '/',''), '') "
	   		+ "			where P_ASSIGNMENT.p_post_id in ( select p_post_id from p_post where p_post.value = KrispyPlanificoExcel.post ) "
	   		+ "			and P_ASSIGNMENT.P_Employee_Id = ( select p_employee_id from p_employee where p_employee.value = KrispyPlanificoExcel.codeEmploye ) "
	   		+ "			and P_ASSIGNMENT.ISACTIVE = 'Y' "
	   		+ "			) "
	   		+ "where hourly_rate is null"
	   ;
	   DB.executeUpdate(sql, null);
	   
	   return cnt;
	}


	
        
    	private static void CreateExcelFile(  )
    	{
    	    DB.executeUpdate("truncate table KrispyPlanificoExcel", null);

    	    
    		int exported = 0;
    	    try
    	    {
         	  String Path = "\\\\svrpplay.bureau.krispykernels.com\\Utilisateurs\\Publique\\Martin L\\";
    	    	if ( ! Path.endsWith("\\") )
    	    		Path = Path + "\\";

    	      String filename =  Path + "Planifico" + ".xls";

    	      WorkbookSettings ws = new WorkbookSettings();
    	      ws.setLocale(new Locale("en", "EN"));
    	      WritableWorkbook workbook = 
    	      Workbook.createWorkbook(new File(filename), ws);
    	      WritableSheet s = workbook.createSheet("Yum Yum", 1);
    	      WritableSheet s1 = workbook.createSheet("Krispy Kernel", 2);
    	      // set la largeur des colonnes
    	      s.setColumnView(0, 20);
    	      s.setColumnView(1, 10);
    	      s.setColumnView(2, 10);
    	      s.setColumnView(3, 10);
    	      s.setColumnView(4, 30);
    	      s.setColumnView(5, 30);
    	      s.setColumnView(6, 10);
    	      s.setColumnView(7, 10);
    	      s.setColumnView(8, 30);
    	      s.setColumnView(9, 30);
    	      s.setColumnView(11, 30);
    	      s1.setColumnView(0, 20);
    	      s1.setColumnView(1, 10);
    	      s1.setColumnView(2, 10);
    	      s1.setColumnView(3, 10);
    	      s1.setColumnView(4, 30);
    	      s1.setColumnView(5, 30);
    	      s1.setColumnView(6, 10);
    	      s1.setColumnView(7, 10);
    	      s1.setColumnView(8, 30);
    	      s1.setColumnView(9, 30);
    	      s1.setColumnView(11, 30);


    	      String sBizRefId = "101";
    		  String sBizSubLabel = "Warwick";

    		  writeDataSheetHeader(s, sBizRefId, sBizSubLabel);
    	      exported = writeDataSheet(s, sBizRefId, sBizSubLabel);

    		  sBizRefId = "102";
   			  sBizSubLabel = "Quebec";

    		  writeDataSheetHeader(s1, sBizRefId, sBizSubLabel);
    	      exported += writeDataSheet(s1, sBizRefId, sBizSubLabel);

    	      workbook.write();
    	      workbook.close();      

    	    }
    	    catch (IOException e)
    	    {
    	      e.printStackTrace();
    	    }
    	    catch (WriteException e)
    	    {
    	      e.printStackTrace();
    	    }
    	    catch (Exception e)
    	    {
    	      e.printStackTrace();
    	    }
    	}
    	
    	
    	public static P_Employee getEmployeeValue( Properties ctx, String value, String trxName )
    	{
    		int id = 0;
    		String sql = "Select P_Employee_ID from P_Employee Where Value = '" + value + "'";
    		PreparedStatement pstmt = null;
    		try
    		{
    			pstmt = DB.prepareStatement(sql, null);
    			ResultSet rs = pstmt.executeQuery();
    			if (rs.next())
    			{
    				id = rs.getInt("P_Employee_ID");
    			}
    			rs.close();
    			pstmt.close();
    			pstmt = null;
    		}
    		catch (Exception e)
    		{
    			System.err.println("P_Employee.get - " + e);
    		}
    		
    		return P_Employee.get(ctx, id, trxName);
    		
    	}
    	
    	public static P_Gain getGaincode( Properties ctx, int P_Employee_ID, int P_Timecode_ID, String trxName )
    	{
    		int id = 0;
    		P_Employee Employee = P_Employee.get( ctx, P_Employee_ID, null );
    		String sql = "SELECT [dbo].[GetTimecode_Gain_V2] ( " + Employee.getP_Payment_Group_ID() + ", " + Employee.getP_Occupation_Group_ID() + " , " + P_Timecode_ID +" ) AS P_Gain_ID ";
    		PreparedStatement pstmt = null;
    		try
    		{
    			pstmt = DB.prepareStatement(sql, null);
    			ResultSet rs = pstmt.executeQuery();
    			if (rs.next())
    			{
    				id = rs.getInt("P_Gain_ID");
    			}
    			rs.close();
    			pstmt.close();
    			pstmt = null;
    		}
    		catch (Exception e)
    		{
    			System.err.println("P_Gain.get - " + e);
    		}
    		
    		return P_Gain.get(ctx, id, trxName);
    		
    	}

    	public static P_Timecode getTimecodeValue( Properties ctx, String value, String trxName )
    	{
    		int id = 0;
    		String sql = "Select P_Timecode_ID from P_Timecode Where Value = '" + value + "'";
    		PreparedStatement pstmt = null;
    		try
    		{
    			pstmt = DB.prepareStatement(sql, null);
    			ResultSet rs = pstmt.executeQuery();
    			if (rs.next())
    			{
    				id = rs.getInt("P_Timecode_ID");
    			}
    			rs.close();
    			pstmt.close();
    			pstmt = null;
    		}
    		catch (Exception e)
    		{
    			System.err.println("P_Timecode.get - " + e);
    		}
    		
    		return P_Timecode.get(ctx, id, trxName);
    		
    	}

        
	
	public static void main(final String[] args) throws Exception 
	{
		Compiere.startup(true);
		
    	PlanificoToExcel.CreateExcelFile( );
		
		

	}	

	
	
}
