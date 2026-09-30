package solstice.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.Compiere;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import solstice.model.P_Employee;

public class ImportExportZoho extends SvrProcess{

	protected CLogger			log = CLogger.getCLogger (getClass());
//	
//	private int Period_ID = 0;
	private int Payment_Group_ID = 0;
	private int Employee_ID = 0;
	private int m_inserted = 0;
    private boolean IsRework = false;
	

	private String trxName = null;

	protected void prepare()
    {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("P_Payment_Group_ID"))
				Payment_Group_ID = para[i].getParameterAsInt();
			else if (name.equals("P_Employee_ID"))
				Employee_ID = para[i].getParameterAsInt();
			else if (name.equals("Rework"))
			    IsRework = "Y".equals(para[i].getParameter());
			
			else
				log.log (Level.SEVERE, "prepare - Unknown Parameter: " + name);			
		}
    }
	
	protected String doIt() throws Exception
	{
		boolean retValue = true;
		
	    try {

	    	//Liste des comptes de dépenses.
	        JsonArray jarray2 = ReadListExpenseReport();
	        
		}
	 	catch (Exception e)
	 	{
	 			log.log(Level.SEVERE,"ImportExportZoho " , e);
	 	}
	    
        DB.executeUpdate( "EXEC KRISPY_IMPORT_EXPENSE_WEB", null);

		String ret = "Importation terminée";
		return retValue == true ? ret : "error";

    }
	
	Properties ctx;
	OkHttpClient client = new OkHttpClient();
	
	public ImportExportZoho ( ) 
	{
		
	}
	
	public ImportExportZoho ( Boolean standalone ) 
	{
		Env.setContext( ctx, "#AD_Client_ID", 1000004);
		Env.setContext( ctx, "#AD_Org_ID", 0);
	    try {

	    	//Liste des comptes de dépenses.
	        JsonArray jarray2 = ReadListExpenseReport();
	        
	        
/*	    	
	        JsonArray jarray = ReadExpenseCategories();
			// JsonArray jarray = ReadListOfUsers();
			
	        for (int i = 0; i < jarray.size(); i++)
	        {
	            JsonObject jobject = jarray.get(i).getAsJsonObject();
	            
	            log.log(Level.INFO,  " object : " + jobject );	
	        }	
*/	        
 
	        
	        
	        
	    }
 		catch (Exception e)
 		{
 	 		log.log(Level.SEVERE,"ImportExportZoho " , e);
 		}	 	
	    
        DB.executeUpdate( "EXEC KRISPY_IMPORT_EXPENSE_WEB", null);

		
	}
		
	public String getAuthentication( String scope ) throws Exception
	{
	
//		 String clientId = "1000.0B1TIBCHJKHDH46Y5VQWZV6DAOQ62P";
//		 String client_secret= "8641d59a76c5ee0324831f0470681497119231a752";

// Usager Générique Admin
		
		String clientId = "1000.LEHQGZ4J5ODKNV6IZTI5Y2YOM74NER";
		String client_secret= "8e7ecb00424af024312f9f3d507680fc3d602c7784";
		
		
// Marc		
//		 String clientId = "1000.SGV60PZOWAVRXMXXOANR5ZNF1FIP3E";
//		 String client_secret= "d58efe4f61b22411e9c4046c075e8c7bce176ec8ad";
		 
// Yves		 
//		 String clientId = "1000.X0WYG82RVR4Z1PGYFTFABLXIHR2N8K";
//		 String client_secret= "6ec26f3c94b9bd2430de8e33d12db6a3670c9a042c";

//Solstice Plus		 
//		 String clientId = "1004.RP4SF4LRVJC7WFLC1EAYO2YQD43E0X";
//		 String client_secret= "6435598e456786a98d8bcc396113e82ea8020853ce";
		 // 07f60bd07413bffc5d40b4f7fd38582e9ec8608d68
		 
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd_hh");

		String date = df.format( new Date() );
	    String token = "";

		
		String fileName = "C:\\Solstice\\Transferts\\Temp\\Token_" + date + "_" + clientId; 
        File file = new File(fileName);;
        if (file.exists())
        {
        	FileReader fr=new FileReader(file);   //reads the file  
        	BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream  
        	token = br.readLine();
        	fr.close();
        }
        else
        {
    		//
    		//  obtain an OAuth 2.0 token for running the API
    		//
       	
    		 String grant_type = "client_credentials";
    		 
    		 okhttp3.RequestBody formBody = new okhttp3.FormBody.Builder()
    				 	.add("grant_type", grant_type)
    	       	        .add("client_secret", client_secret)
    	                .add("client_id", clientId)
    	                .add("scope", scope)
    	                .build();

/*
   		 String grant_type = "authorization_code";

        	okhttp3.RequestBody formBody = new okhttp3.FormBody.Builder()
 	                .add("grant_type", grant_type)
 	       	        .add("client_secret", client_secret)
 	                .add("client_id", clientId)
 	                .add("scope", scope)
 	                .add("code", "07f60bd07413bffc5d40b4f7fd38582e9ec8608d68")
 	                .add( "redirect_uri", "https://www.zylker.com/oauthredirect" )
 	                .build();
*/
    		 

    		 
    		 String url = "https://accounts.zoho.com/oauth/v2/token";
    		 
    //		 url = "https://accounts.zohocloud.ca/oauth/v2/token";
    		 
    		 Request request = new Request.Builder()
    	    		    .url(url)
    	    		    .post(formBody)
    	                .addHeader("Content-Type", "application/json")
    	                .build();
    	                
    	     okhttp3.Response respnse = null;
    		 
    	     try {
    	            
    	            respnse = client.newCall(request).execute();
    	            
    	            String resp = respnse.body().string();
    	            log.log(Level.INFO,"resp : " + resp);
    	            
    	            JsonElement jsonElement = JsonParser.parseString(resp);
    	            JsonObject jsonObject = jsonElement.getAsJsonObject();
    	            
    	            token = jsonObject.get("access_token").getAsString();
    	            
    	            log.log(Level.INFO,"token : " + token);
    	            
    	     }
    		 catch (Exception e)
    		 {
     			log.log(Level.SEVERE,"ImportExportZoho " , e);
    		 }	     
    	     
    	     FileWriter myWriter = new FileWriter( fileName );
    	     myWriter.write( token );
    	     myWriter.close();

        	
        }
        
	     return token;
	}
	
	
	public JsonArray ReadListOfUsers() throws Exception
	{
		 String token = getAuthentication("ZohoExpense.users.READ");
	 
//	     MediaType mediaType = MediaType.parse( "application/json" );
	     
	     okhttp3.RequestBody formBody2 = new okhttp3.FormBody.Builder()
	                .add("grant_type", "Bearer")
	    		 
	                .build();
		 
		 Request request2 = new Request.Builder()
	    		 	.addHeader("Authorization", "Bearer " + token)
	    		 	.addHeader("Content-Type", "application/json;")
//				    .url("https://www.zohoapis.com/expense/v1/expensereports/")
				    .url("https://www.zohoapis.com/expense/v1/users")
	    		    .post(formBody2)
	    		    .get()
				    .build();
	
	     okhttp3.Response respnse2 = null;

	     JsonArray jarray = null;
	     try {
	            
	            respnse2 = client.newCall(request2).execute();
	            
	            String resp = respnse2.body().string();
	            JsonElement jsonElement = JsonParser.parseString(resp);
	            JsonObject jsonObject = jsonElement.getAsJsonObject();

	            
	            jarray  = jsonObject.getAsJsonArray("users");
	            
	            //jarray  = jsonObject.getAsJsonArray("expense_reports");
	            
	            
	     }
		 catch (Exception e)
		 {
	 			log.log(Level.SEVERE,"ImportExportZoho " , e);
		 }	     
		 
         return jarray;
	}

	public JsonArray CreateUsers() throws Exception
	{
		 String token = getAuthentication("ZohoExpense.users.WRITE");
	 
//	     MediaType mediaType = MediaType.parse( "application/json" );
	     
	     okhttp3.RequestBody formBody2 = new okhttp3.FormBody.Builder()
	                .add("grant_type", "Bearer")
	    		    .add( "name", "Caouette, Cynthia")
	    		    .add( "email", "ccaouette@krispykernels.com")	    		    
	    		    .add( "role", "submitter")	    		    
	    		    .add("default_approver_email", "will.smith@yahoo.in" )
	    		    .add("approves_to_email", "will.smith@yahoo.in" )
	    		    .add("submission_amount_limit", "5000" )
	    		    .add("approval_amount_limit", "1000" )
	    		    .add("employee_number", "E001")
	    		    .add("department_name", "Finance" )
	    		    .add("designation_name", "Manager")
	    		    .add("date_of_birth", "1996-07-25")
	    		    .add("date_of_joining", "2017-01-04")
	    		    .add("mobile", "9500881944")
	    		    .add("gender", "male")
	    		    .add("policy_name", "Expense Saver")
/*	    		    .add("custom_fields", [{
	    		            "customfield_id": "16367000000093005",
	    		            "value": "Tric"
	    		        }
	    		    ]	)*/            
	                .build();
		 
		 Request request2 = new Request.Builder()
	    		 	.addHeader("Authorization", "Bearer " + token)
	    		 	.addHeader("Content-Type", "application/json;")
//				    .url("https://www.zohoapis.com/expense/v1/expensereports/")
				    .url("https://www.zohoapis.com/expense/v1/users")
	    		    .post(formBody2)
	    		    .get()
				    .build();
	
	     okhttp3.Response respnse2 = null;

	     JsonArray jarray = null;
	     try {
	            
	            respnse2 = client.newCall(request2).execute();
	            // {"code":57,"message":"You are not authorized to perform this operation"}
	            String resp = respnse2.body().string();
	            JsonElement jsonElement = JsonParser.parseString(resp);
	            JsonObject jsonObject = jsonElement.getAsJsonObject();

	            
	            jarray  = jsonObject.getAsJsonArray("users");
	            
	            //jarray  = jsonObject.getAsJsonArray("expense_reports");
	            
	            
	     }
		 catch (Exception e)
		 {
	 			log.log(Level.SEVERE,"ImportExportZoho " , e);
		 }	     
		 
         return jarray;
		 
		 
	}

//	 String token = getAuthentication("ZohoExpense.expensereport.READ");
//	 String token = getAuthentication("ZohoBooks.fullaccess.ALL");
//    MediaType mediaType = MediaType.parse( "application/json" );
//    .addHeader( "Filter_by", "Type.Approval,Status.Approved")
// 	.addHeader("Filter_by", "Type.Approval,Status.Submitted")
// 	.addHeader("X-com-zoho-expense-organizationid", "851698770")

// 	.url("https://expense.zoho.com/api/v1/expensereports?page=1&per_page=50&filter_by=Type.Approval%2CStatus.All&sort_column=&sort_order=D&search_text=&customview_id=&organization_id=851698770&build_label=Jun_14_2024_1_25374")
//    .url("https://www.zohoapis.com/expense/v1/expensereports?filter_by=Status.Approved")
//	
//    .add( "Filter_by", "Type.Approval,Status.Submitted")
	 // 				    .url("https://www.zohoapis.com/expense/v1/expensereports/")

	/*
	 * 
	 * 
	 * Read Expense Report from ZOHO Expense
	 * 
	 * 
	 * 
	 */
	
	public JsonArray ReadListExpenseReport() throws Exception
	{
		 String token = getAuthentication("ZohoExpense.fullaccess.ALL");
		 
	     
	     okhttp3.RequestBody formBody2 = new okhttp3.FormBody.Builder()
	                .add("grant_type", "Bearer")
	                .add("X-com-zoho-expense-organizationid", "851698770")
	                .build();
		 
	     

		 Request request2 = new Request.Builder()
	    		 	.addHeader("Authorization", "Bearer " + token)
	    		 	.addHeader("Content-Type", "application/json;")
//			    .url("https://www.zohoapis.com/expense/v1/expensereports")

//Retour tout les dépense mais doit être authetifier	    		 	
//	    		 	.url("https://expense.zoho.com/api/v1/expensereports?filter_by=Type.Approval%2CStatus.All&sort_column=&sort_order=D&search_text=&customview_id=&organization_id=851698770")
//				    .url("https://www.zohoapis.com/expense/v1/expensereports?filter_by=Type.Approval")

// OK			    
	    		 	.url("https://www.zohoapis.com/expense/v1/expensereports?filter_by=Type.Approval,Status.Approved")
//	    		 	.url("https://www.zohoapis.com/expense/v1/expensereports?filter_by=Type.Approval,Status.Reimbursed")
	    		    .post(formBody2)
	    		    .get()
				    .build();
	
	     okhttp3.Response respnse2 = null;

	     JsonArray jarray = null;
	     try {
	            
	            respnse2 = client.newCall(request2).execute();
	            
	            String resp = respnse2.body().string();
	            JsonElement jsonElement = JsonParser.parseString(resp);
	            JsonObject jsonObject = jsonElement.getAsJsonObject();
	            
	            jarray  = jsonObject.getAsJsonArray("expense_reports");
	            
	            
	     }
		 catch (Exception e)
		 {
	 			log.log(Level.SEVERE,"ImportExportZoho " , e);
		 }	     
		 
	     // DEBUG Display JSON receive.
        if ( jarray != null)
        {
	        for (int i = 0; i < jarray.size(); i++)
	        {
	            JsonObject jobject = jarray.get(i).getAsJsonObject();
	            JsonElement submitter_name = jobject.get("submitter_name");
	            log.log(Level.INFO,  " report_id : " + jobject.get("report_id").getAsString() + "  - object : submitter_name " + submitter_name.getAsString() + " : " + jobject );

	            P_Employee Employee = null;
	            if ( Employee_ID != 0 )
	            	Employee = P_Employee.get( Env.getCtx(), Employee_ID, null );
	            
	            if ( Employee_ID == 0 || ( Employee_ID != 0 && Employee.getValue().equals( jobject.get("employee_number").getAsString( ) )) )
	            {
	            	
		            String sqlDel = "DELETE FROM [dbo].[zoho_expensereports_Detail] WHERE [report_id] = " + DB.TO_STRING( jobject.get("report_id").getAsString() );
		            int noDel = DB.executeUpdate(sqlDel, trxName);

		            // Important d'inséré le détail avant l'entête pour que le trigger fonctionne correctement.
		            ReadListExpenseReportDetail( jobject.get("report_id").getAsString() );

		            String sqlDel2 = "DELETE FROM [dbo].[zoho_expensereports] WHERE [report_id] = " + DB.TO_STRING( jobject.get("report_id").getAsString() );
		            int noDel2 = DB.executeUpdate(sqlDel2, trxName);
		            
		            
		            String sql = "INSERT INTO [dbo].[zoho_expensereports]            " 
		            		+ "           ([report_id]                            " 
		            		+ "           ,[report_name]                          " 
		            		+ "           ,[description]                          " 
		            		+ "           ,[report_number]                        " 
		            		+ "           ,[start_date]                           " 
		            		+ "           ,[end_date]                             " 
		            		+ "           ,[status]                               " 
		            		+ "           ,[is_ach_reimbursement]                 " 
		            		+ "           ,[is_archived]                          " 
		            		+ "           ,[due_date]                             " 
		            		+ "           ,[submitted_date]                       " 
		            		+ "           ,[approved_date]                        " 
		            		+ "           ,[last_submitted_date]                  " 
		            		+ "           ,[currency_id]                          " 
		            		+ "           ,[currency_code]                        " 
		            		+ "           ,[approver_id]                          " 
		            		+ "           ,[payrun_name]                          " 
		            		+ "           ,[claim_type_id]                        " 
		            		+ "           ,[claim_type_name]                      " 
		            		+ "           ,[approver_name]                        " 
		            		+ "           ,[audit_status]                         " 
		            		+ "           ,[approver_email]                       " 
		            		+ "           ,[approver_photo_url]                   " 
		            		+ "           ,[approver_employee_no]                 " 
		            		+ "           ,[approver_zuid]                        " 
		            		+ "           ,[submitted_to_id]                      " 
		            		+ "           ,[submitted_to_zuid]                    " 
		            		+ "           ,[submitted_to_name]                    " 
		            		+ "           ,[submitted_to_email]                   " 
		            		+ "           ,[submitted_to_employee_no]             " 
		            		+ "           ,[submitter_email]                      " 
		            		+ "           ,[submitted_by]                         " 
		            		+ "           ,[submitter_name]                       " 
		            		+ "           ,[submitter_zuid]                       " 
		            		+ "           ,[total]                                " 
		            		+ "           ,[reimbursable_total]                   " 
		            		+ "           ,[amount_to_be_reimbursed]              " 
		            		+ "           ,[non_reimbursable_total]               " 
		            		+ "           ,[reimbursement_date]                   " 
		            		+ "           ,[rcy_currency_id]                      " 
		            		+ "           ,[rcy_currency_code]                    " 
		            		+ "           ,[rcy_exchange_rate]                    " 
		            		+ "           ,[rcy_price_precision]                  " 
		            		+ "           ,[rcy_currency_symbol]                  " 
		            		+ "           ,[rcy_total]                            " 
		            		+ "           ,[rcy_reimbursable_total]               " 
		            		+ "           ,[rcy_non_reimbursable_total]           " 
		            		+ "           ,[created_time]                         " 
		            		+ "           ,[last_modified_time]                   " 
		            		+ "           ,[created_date]                         " 
		            		+ "           ,[last_modified_date]                   " 
		            		+ "           ,[created_by_id]                        " 
		            		+ "           ,[created_by_zuid]                      " 
		            		+ "           ,[created_by_name]                      " 
		            		+ "           ,[creator_photo_url]                    " 
		            		+ "           ,[creator_employee_no]                  " 
		            		+ "           ,[user_id]                              " 
		            		+ "           ,[user_email]                           " 
		            		+ "           ,[user_name]                            " 
		            		+ "           ,[user_photo_url]                       " 
		            		+ "           ,[is_shared_report]                     " 
		            		+ "           ,[comments_count]                       " 
		            		+ "           ,[policy_violated]                      " 
		            		+ "           ,[receipt_policy_violation_count]       " 
		            		+ "           ,[description_policy_violation_count]   " 
		            		+ "           ,[amount_policy_violation_count]        " 
		            		+ "           ,[total_policy_violation_count]         " 
		            		+ "           ,[custom_policy_violation_count]        " 
		            		+ "           ,[uncategorized_expense_count]          " 
		            		+ "           ,[customer_id]                          " 
		            		+ "           ,[zcrm_potential_id]"
		            		+ "			,project_id                                 "
		            		+ "			,submitter_photo_url                        "
		            		+ "			,submitter_employee_no                      "
		            		+ "			,customer_name                              "
		            		+ "			,project_name                               "
		            		+ "			,can_push_to_accounting_app                 "
		            		+ "			,accounting_app_name                        "
		            		+ "			,accounting_reference_id                    "
		            		+ "			,accounting_sync_time                       "
		            		+ "			,employee_number                            "
		            		+ "			,department_id                              "
		            		+ "			,department_name                            "
		            		+ "			,trip_id                                    "
		            		+ "			,trip_number                                "
		            		+ "			,trip_name                                  "
		            		+ "			,trip_status                                "
		            		+ "			,is_international_trip                      "
		            		+ "			,policy_id                                  "
		            		+ "			,trip_destination_country                   "
		            		+ "			,trip_destination_city                      "
		            		+ "			,trip_departure                             "
		            		+ "			,trip_budget_amount                         "
		            		+ "			,trip_budget_amount_currency_id             "
		            		+ "			,trip_budget_amount_currency_code           "
		            		+ "			,trip_start_date                            "
		            		+ "			,trip_end_date                              "
		            		+ "			,policy_name                                "
		            		+ "			,policy_display_name                        "
		            		+ "			,due_days                                   "
		            		+ "			,sub_status                                 "
		            		+ "			,expense_count                              "
		            		+ ")                   " 
		            		+ "     VALUES                                        " 
		            		+ "           (                                       " 
		            		+ "			   "  + DB.TO_STRING( jobject.get("report_id").getAsString() )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("report_name").getAsString()                        )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("description").getAsString()                        )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("report_number").getAsString()                      )
		            		+ "           ,CONVERT( DATETIME, "  + DB.TO_STRING( jobject.get("start_date").getAsString() ) +  ", 126 )"
		            		+ "           ,CONVERT( DATETIME, "  + DB.TO_STRING( jobject.get("end_date").getAsString()  ) +  ", 126 )"
		            		+ "           ,"  + DB.TO_STRING( jobject.get("status").getAsString()                             )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("is_ach_reimbursement").getAsString()               )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("is_archived").getAsString()                        )
		            		+ "           ,CONVERT( DATETIME, "  + DB.TO_STRING( jobject.get("due_date").getAsString()                           )+  ", 126 )"
		            		+ "           ,CONVERT( DATETIME, "  + DB.TO_STRING( jobject.get("submitted_date").getAsString()                     )+  ", 126 )"
		            		+ "           ,CONVERT( DATETIME, "  + DB.TO_STRING( jobject.get("approved_date").getAsString()                      )+  ", 126 )"
		            		+ "           ,CONVERT( DATETIME, "  + DB.TO_STRING( jobject.get("last_submitted_date").getAsString()                )+  ", 126 )"
		            		+ "           ,"  + DB.TO_STRING( jobject.get("currency_id").getAsString()                        )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("currency_code").getAsString()                      )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("approver_id").getAsString()                        )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("payrun_name").getAsString()                        )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("claim_type_id").getAsString()                      )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("claim_type_name").getAsString()                    )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("approver_name").getAsString()                      )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("audit_status").getAsString()                       )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("approver_email").getAsString()                     )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("approver_photo_url").getAsString()                 )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("approver_employee_no").getAsString()               )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("approver_zuid").getAsString()                      )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("submitted_to_id").getAsString()                    )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("submitted_to_zuid").getAsString()                  )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("submitted_to_name").getAsString()                  )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("submitted_to_email").getAsString()                 )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("submitted_to_employee_no").getAsString()           )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("submitter_email").getAsString()                    )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("submitted_by").getAsString()                       )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("submitter_name").getAsString()                     )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("submitter_zuid").getAsString()                     )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("total").getAsString()                              )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("reimbursable_total").getAsString()                 )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("amount_to_be_reimbursed").getAsString()            )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("non_reimbursable_total").getAsString()             )
		            		;
//ICI
		            		if ( jobject.get("reimbursement_date").getAsString() != "" && jobject.get("reimbursement_date").getAsString() != null)
		            			sql = sql + " ,CONVERT( DATETIME, "  + DB.TO_STRING( jobject.get("reimbursement_date").getAsString() ) +  ", 126 )";
	            			else
	            				sql = sql + "           ,"  + DB.TO_STRING( jobject.get("reimbursement_date").getAsString()                 );

	            			sql = sql +  "           ,"  + DB.TO_STRING( jobject.get("rcy_currency_id").getAsString()                    )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("rcy_currency_code").getAsString()                  )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("rcy_exchange_rate").getAsString()                  )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("rcy_price_precision").getAsString()                )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("rcy_currency_symbol").getAsString()                )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("rcy_total").getAsString()                          )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("rcy_reimbursable_total").getAsString()             )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("rcy_non_reimbursable_total").getAsString()         )
		            		+ "           ,CONVERT( DATETIME, "  + DB.TO_STRING( jobject.get("created_time").getAsString().substring(0,19)                       )+  ", 126 )"
		            		+ "           ,CONVERT( DATETIME, "  + DB.TO_STRING( jobject.get("last_modified_time").getAsString().substring(0,19)                 )+  ", 126 )"
		            		+ "           ,CONVERT( DATETIME, "  + DB.TO_STRING( jobject.get("created_date").getAsString()                       )+  ", 126 )"
		            		+ "           ,CONVERT( DATETIME, "  + DB.TO_STRING( jobject.get("last_modified_date").getAsString()                 )+  ", 126 )"
		            		+ "           ,"  + DB.TO_STRING( jobject.get("created_by_id").getAsString()                      )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("created_by_zuid").getAsString()                    )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("created_by_name").getAsString()                    )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("creator_photo_url").getAsString()                  )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("creator_employee_no").getAsString()                )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("user_id").getAsString()                            )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("user_email").getAsString()                         )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("user_name").getAsString()                          )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("user_photo_url").getAsString()                     )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("is_shared_report").getAsString()                   )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("comments_count").getAsString()                     )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("policy_violated").getAsString()                    )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("receipt_policy_violation_count").getAsString()     )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("description_policy_violation_count").getAsString() )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("amount_policy_violation_count").getAsString()      )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("total_policy_violation_count").getAsString()       )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("custom_policy_violation_count").getAsString()      )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("uncategorized_expense_count").getAsString()        )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("customer_id").getAsString()                        )
		            		+ "           ,"  + DB.TO_STRING( jobject.get("zcrm_potential_id").getAsString()                  )
		            		+ "			      ," + DB.TO_STRING( jobject.get("project_id").getAsString() )
		            		+ "			      ," + DB.TO_STRING( jobject.get("submitter_photo_url").getAsString() )
		            		+ "			      ," + DB.TO_STRING( jobject.get("submitter_employee_no").getAsString() )
		            		+ "			      ," + DB.TO_STRING( jobject.get("customer_name").getAsString() )
		            		+ "			      ," + DB.TO_STRING( jobject.get("project_name").getAsString() )
		            		+ "			      ," + DB.TO_STRING( jobject.get("can_push_to_accounting_app").getAsString() )
		            		+ "			      ," + DB.TO_STRING( jobject.get("accounting_app_name").getAsString() )
		            		+ "			      ," + DB.TO_STRING( jobject.get("accounting_reference_id").getAsString() )
		            		+ "			      ," + DB.TO_STRING( jobject.get("accounting_sync_time").getAsString() )
		            		+ "			      ," + DB.TO_STRING( jobject.get("employee_number").getAsString() )
		            		+ "			      ," + DB.TO_STRING( jobject.get("department_id").getAsString() )
		            		+ "			      ," + DB.TO_STRING( jobject.get("department_name").getAsString() )
		            		+ "			      ," + DB.TO_STRING( jobject.get("trip_id").getAsString() )
		            		+ "			      ," + DB.TO_STRING( jobject.get("trip_number").getAsString() )
		            		+ "			      ," + DB.TO_STRING( jobject.get("trip_name").getAsString() )
		            		+ "			      ," + DB.TO_STRING( jobject.get("trip_status").getAsString() )
		            		+ "			      ," + DB.TO_STRING( jobject.get("is_international_trip").getAsString() )
		            		+ "			      ," + DB.TO_STRING( jobject.get("policy_id").getAsString() )
		            		+ "			      ," + DB.TO_STRING( jobject.get("trip_destination_country").getAsString() )
		            		+ "			      ," + DB.TO_STRING( jobject.get("trip_destination_city").getAsString() )
		            		+ "			      ," + DB.TO_STRING( jobject.get("trip_departure").getAsString() )
		            		+ "			      ," + DB.TO_STRING( jobject.get("trip_budget_amount").getAsString() )
		            		+ "			      ," + DB.TO_STRING( jobject.get("trip_budget_amount_currency_id").getAsString() )
		            		+ "			      ," + DB.TO_STRING( jobject.get("trip_budget_amount_currency_code").getAsString() )
		            		+ "  	          ,CONVERT( DATETIME, "  + DB.TO_STRING( jobject.get("trip_start_date").getAsString()                     )+  ", 126 )"
	                        + "               ,CONVERT( DATETIME, "  + DB.TO_STRING( jobject.get("trip_end_date").getAsString()                     )+  ", 126 )"
		            		
		            		+ "			      ," + DB.TO_STRING( jobject.get("policy_name").getAsString() )
		            		+ "			      ," + DB.TO_STRING( jobject.get("policy_display_name").getAsString() )
		            		+ "			      ," + DB.TO_STRING( jobject.get("due_days").getAsString() )
		            		+ "			      ," + DB.TO_STRING( jobject.get("sub_status").getAsString() )
		            		+ "			      ," + DB.TO_STRING( jobject.get("expense_count").getAsString() )
	         
		            		+ "           )"  
		            		;
		           
//		            log.log(Level.INFO,  " SQL : " + sql);
		            
		            int no = DB.executeUpdate(sql.toString(), trxName);
		            m_inserted = m_inserted + 1;

	            }
	            
	            
	        }	
        }
	     
         return jarray;
		 
		 
	}

	
	public JsonArray ReadListExpenseReportDetail( String report_id ) throws Exception
	{
		 String token = getAuthentication("ZohoExpense.fullaccess.ALL");
		 
	     
	     okhttp3.RequestBody formBody2 = new okhttp3.FormBody.Builder()
	                .add("grant_type", "Bearer")
	                .add("X-com-zoho-expense-organizationid", "851698770")
	                .build();
		 
	     

		 Request request2 = new Request.Builder()
	    		 	.addHeader("Authorization", "Bearer " + token)
	    		 	.addHeader("Content-Type", "application/json;")
				    .url("https://www.zohoapis.com/expense/v1/expensereports/" + report_id + "/approvalhistory")
	    		    .post(formBody2)
	    		    .get()
				    .build();
	
	     okhttp3.Response respnse2 = null;

	     JsonObject jExpense_report = null;
	     JsonObject joExpenses = null;
	     JsonArray jExpenses = null;
	     
	     JsonArray jarray = null;
	     try {
	            
	            respnse2 = client.newCall(request2).execute();
	            
	            String resp = respnse2.body().string();
	            JsonElement jsonElement = JsonParser.parseString(resp);
	            JsonObject jsonObject = jsonElement.getAsJsonObject();
	            jExpense_report = jsonObject.getAsJsonObject("expense_report");
	            jExpenses  = jExpense_report.getAsJsonArray("expenses");
	            
	            ArrayList<Object>  listdata = new ArrayList<Object>();  
	            if (jExpenses != null) {   
	                  
	                //Iterating JSON array  
	                for (int i=0;i<jExpenses.size();i++){   
	                      
	                	listdata = new ArrayList<Object>();  
	                	listdata.add(jExpenses.get(i) );
	                	
	                	for (Object dataObj: listdata) {
	                		
	                	        System.out.println("String Data : " + dataObj );
	                	        
	                	        JsonElement jElement = JsonParser.parseString( dataObj.toString() );
	            	            JsonObject jObject = jElement.getAsJsonObject();
	            	            JsonArray  jarray2 = jObject.getAsJsonArray("line_items");
	            	            
	            	            JsonObject jobject = jarray2.get(0).getAsJsonObject();
	            	            if ( ! jobject.isJsonNull() ) 
	            	            {
		            	            String sql = "INSERT INTO [dbo].[zoho_expensereports_detail]\r\n"
		            	            		+ "           ([report_id]\r\n"
		            	            		+ "           ,[tax_amount]\r\n"
		            	            		+ "           ,[claimed_bcy_total]\r\n"
		            	            		+ "           ,[amount]\r\n"
		            	            		+ "           ,[line_item_id]\r\n"
		            	            		+ "           ,[category_name]\r\n"
		            	            		+ "           ,[item_date]\r\n"
		            	            		+ "           ,[user_name]\r\n"
		            	            		+ "           ,[tax_name]\r\n"
		            	            		+ "           ,[description]\r\n"
		            	            		+ "           ,[item_total]\r\n"
		            	            		+ "           ,[item_total_inclusive_of_tax]\r\n"
		            	            		+ "           ,[tax_id]\r\n"
		            	            		+ "           ,[item_order]\r\n"
		            	            		+ "           ,[gl_code]\r\n"
		            	            		+ "           ,[bcy_total]\r\n"
		            	            		+ "           ,[category_id]\r\n"
		            	            		+ "           ,[tax_type]\r\n"
		            	            		+ "           ,[user_id]\r\n"
		            	            		+ "           ,[claimed_fcy_total]\r\n"
		            	            		+ "           ,[tax_percentage]"
		            	            		+ ")                   " 
		            	            		+ "     VALUES                                        " 
		            	            		+ "           (                                       " 
		            	            		+ "			   "  + DB.TO_STRING( report_id )
		            	            		+ "           ,"  + DB.TO_STRING( jobject.get("tax_amount").getAsString()                        )
		            	            		+ "           ,"  + DB.TO_STRING( jobject.get("claimed_bcy_total").getAsString()                        )
		            	            		+ "           ,"  + DB.TO_STRING( jobject.get("amount").getAsString()                      )
		            	            		+ "           ,"  + DB.TO_STRING( jobject.get("line_item_id").getAsString()                      )
		            	            		+ "           ,"  + DB.TO_STRING( jobject.get("category_name").getAsString()                      )
		            	            		+ "  	          ,CONVERT( DATETIME, "  + DB.TO_STRING( jobject.get("item_date").getAsString()                     )+  ", 126 )"
		            	            		+ "           ,"  + DB.TO_STRING( jobject.get("user_name").getAsString()                      )
		            	            		+ "           ,"  + DB.TO_STRING( jobject.get("tax_name").getAsString()                      )
		            	            		+ "           ,"  + DB.TO_STRING( jobject.get("description").getAsString()                      )
		            	            		+ "           ,"  + DB.TO_STRING( jobject.get("item_total").getAsString()                      )
		            	            		+ "           ,"  + DB.TO_STRING( jobject.get("item_total_inclusive_of_tax").getAsString()                      )
		            	            		+ "           ,"  + DB.TO_STRING( jobject.get("tax_id").getAsString()                      )
		            	            		+ "           ,"  + DB.TO_STRING( jobject.get("item_order").getAsString()                      )
		            	            		+ "           ,"  + DB.TO_STRING( jobject.get("gl_code").getAsString()                      )
		            	            		+ "           ,"  + DB.TO_STRING( jobject.get("bcy_total").getAsString()                      )
		            	            		+ "           ,"  + DB.TO_STRING( jobject.get("category_id").getAsString()                      )
		            	            		+ "           ,"  + DB.TO_STRING( jobject.get("tax_type").getAsString()                      )
		            	            		+ "           ,"  + DB.TO_STRING( jobject.get("user_id").getAsString()                      )
		            	            		+ "           ,"  + DB.TO_STRING( jobject.get("claimed_fcy_total").getAsString()                      )
		            	            		+ "           ,"  + DB.TO_STRING( jobject.get("tax_percentage").getAsString()                      )
		                     
		            	            		+ "           )"  
		            	            		;
		            	         
//		            	            log.log(Level.INFO,  " SQL : " + sql.toString());
		            	            
		            	            int no = DB.executeUpdate(sql.toString(), trxName);
	            	            	
	            	            }
	                	}	                	
	                }   
	                
	            }  	            
	            
	     }
		 catch (Exception e)
		 {
	 			log.log(Level.SEVERE,"ImportExportZoho " , e);
		 }	     
		 
	     
         return jarray;
		 
		 
	}

	
	public void ReimburseExpenseReport( String id, String sDate, String sAmount ) throws Exception
	{
		 String token = getAuthentication("ZohoExpense.fullaccess.ALL");
//		 String token = getAuthentication("ZohoExpense.expensereport.UPDATE");		 
	     
	     okhttp3.RequestBody formBody = new okhttp3.FormBody.Builder()
//	                .add("grant_type", "Bearer")
//	                .add("X-com-zoho-expense-organizationid", "851698770")
/*	                .add( "notes", "Payé par la paie")
	                .add( "date", sDate)
	                .add( "amount", sAmount)
	                .add( "reference_number", id)
	                .add( "report_ids", id)
	                
	                .add( "currency_id", "4951942000000000101")
*/	                
	    		 	.add( "report_ids", id)
//	                .add( "amount", sAmount)	    		 	
//	                .add( "data", "{\"reference_number\": \"" + id  +  "\", \"notes\", \"Payé par la paie\",  \"amount\", sAmount  }" )
	                .build();
		 
	     

		 Request request2 = new Request.Builder()
	    		 	.addHeader("Authorization", "Bearer " + token)
	    		 	.addHeader("X-com-zoho-expense-organizationid", "851698770")	    		 	
	    		 	.addHeader("Content-Type", "application/json;")
				    .url("https://www.zohoapis.com/expense/v1/expensereports/reimburse")
	    		    .post(formBody)
				    .build();
	
	     okhttp3.Response respnse2 = null;

	     JsonArray jarray = null;
	     try {
	            
	            respnse2 = client.newCall(request2).execute();
	            
	            String resp = respnse2.body().string();
	            JsonElement jsonElement = JsonParser.parseString(resp);
	            JsonObject jsonObject = null;
	            if ( ! jsonElement.isJsonNull() )
	            	jsonObject = jsonElement.getAsJsonObject();
	            
//	            jarray  = jsonObject.getAsJsonArray("expense_reports");
	            
	            
	     }
		 catch (Exception e)
		 {
	 			log.log(Level.SEVERE,"Reimburse an expense report " , e);
		 }	     
		
		
	}

	
	public JsonArray ReadExpenseCategories() throws Exception
	{
		 String token = getAuthentication("ZohoExpense.fullaccess.ALL");

	     okhttp3.RequestBody formBody2 = new okhttp3.FormBody.Builder()
	                .add("grant_type", "Bearer")
	    		 
	                .build();
		 
		 Request request2 = new Request.Builder()
	    		 	.addHeader("Authorization", "Bearer " + token)
	    		 	.addHeader("Content-Type", "application/json;")
				    .url("https://www.zohoapis.com/expense/v1/expensecategories")
	    		    .post(formBody2)
	    		    .get()
				    .build();
	
	     okhttp3.Response respnse2 = null;

	     JsonArray jarray = null;
	     try {
	            
	            respnse2 = client.newCall(request2).execute();
	            
	            String resp = respnse2.body().string();
	            JsonElement jsonElement = JsonParser.parseString(resp);
	            JsonObject jsonObject = jsonElement.getAsJsonObject();

	            
	            jarray  = jsonObject.getAsJsonArray("expense_accounts");
	            
	            //jarray  = jsonObject.getAsJsonArray("expense_reports");
	            
	            
	     }
		 catch (Exception e)
		 {
	 			log.log(Level.SEVERE,"ImportExportZoho " , e);
		 }	     
		 
         return jarray;
		 
		 
	}

	
	public static void main (String[] args)
	{
		System.out.println("ImportExportZoho   $Revision: 1.0 $");
		System.out.println("----------------------------------");
		//
		Compiere.startup(true);
		
		
		int count = 0;

		new ImportExportZoho( true );
		count++;
		System.out.println("Generated = " + count);

  //      DB.executeUpdate( "EXEC KRISPY_IMPORT_EXPENSE_WEB", null);
		
		
	}	//	main
	
}
