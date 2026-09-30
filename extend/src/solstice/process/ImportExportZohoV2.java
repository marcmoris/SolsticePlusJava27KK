package solstice.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.Compiere;
import org.compiere.model.MCharge;
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
import solstice.model.P_Employee_Expensereports;
import solstice.model.P_Employee_Expensereports_Detail;
import solstice.model.P_Period;
import solstice.utils.TimeUtilSolstice;

public class ImportExportZohoV2 extends SvrProcess{

	protected CLogger			log = CLogger.getCLogger(getClass());
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
		for(int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if(name.equals("P_Payment_Group_ID"))
				Payment_Group_ID = para[i].getParameterAsInt();
			else if(name.equals("P_Employee_ID"))
				Employee_ID = para[i].getParameterAsInt();
			else if(name.equals("Rework"))
			    IsRework = "Y".equals(para[i].getParameter());
			
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);			
		}
    }
	
	protected String doIt() throws Exception
	{
		boolean retValue = true;
		
	    try {

	    	//Liste des comptes de dépenses.
	        JsonArray jarray2 = ReadListExpenseReport();
	        
		}
	 	catch(Exception e)
	 	{
	 			log.log(Level.SEVERE,"ImportExportZohoV2 " , e);
	 	}
	    
//        DB.executeUpdate( "EXEC KRISPY_IMPORT_EXPENSE_WEB_V2", null);

		String ret = "Importation terminée";
		return retValue == true ? ret : "error";

    }
	
	Properties ctx;
	OkHttpClient client = new OkHttpClient();
	
	public ImportExportZohoV2() 
	{
		
	}
	
	public ImportExportZohoV2( Boolean standalone) 
	{
		Env.setContext( ctx, "#AD_Client_ID", 1000004);
		Env.setContext( ctx, "#AD_Org_ID", 0);

		Env.setContext( Env.getCtx(), "#AD_Client_ID", 1000004);
		Env.setContext( Env.getCtx(), "#AD_Org_ID", 0);

		try {

	    	//Liste des comptes de dépenses.
	        JsonArray jarray2 = ReadListExpenseReport();
	        
	    }
 		catch(Exception e)
 		{
 	 		log.log(Level.SEVERE,"ImportExportZohoV2 " , e);
 		}	 	
	    
 //       DB.executeUpdate( "EXEC KRISPY_IMPORT_EXPENSE_WEB_V2", null);

		
	}
		
	public String getAuthentication( String scope) throws Exception
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

		String date = df.format( new Date());
	    String token = "";

		
		String fileName = "C:\\Solstice\\Transferts\\Temp\\TokenV2_" + date + "_" + clientId; 
        File file = new File(fileName);;
        if(file.exists())
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

    		 String url = "https://accounts.zoho.com/oauth/v2/token";
    		 
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
    		 catch(Exception e)
    		 {
     			log.log(Level.SEVERE,"ImportExportZohoV2 " , e);
    		 }	     
    	     
    	     FileWriter myWriter = new FileWriter( fileName);
    	     myWriter.write( token);
    	     myWriter.close();

        	
        }
        
	     return token;
	}
	
	
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
		 catch(Exception e)
		 {
	 			log.log(Level.SEVERE,"ImportExportZohoV2 " , e);
		 }	     
		 
	     // DEBUG Display JSON receive.
        if( jarray != null)
        {
	        for(int i = 0; i < jarray.size(); i++)
	        {
	            JsonObject jobject = jarray.get(i).getAsJsonObject();
	            JsonElement submitter_name = jobject.get("submitter_name");
	            log.log(Level.INFO,  " report_id : " + jobject.get("report_id").getAsString() + "  - object : submitter_name " + submitter_name.getAsString() + " : " + jobject);

	            P_Employee Employee = null;
	            if( Employee_ID != 0)
	            	Employee = P_Employee.get( Env.getCtx(), Employee_ID, null);
	            
	            if ( Employee_ID == 0 || ( Employee_ID != 0 && Employee.getValue().equals( jobject.get("employee_number").getAsString( ) )) )
	            {
	            	
		            String sqlDel = "DELETE FROM P_Employee_Expensereports_Detail WHERE report_id = " + DB.TO_STRING( jobject.get("report_id").getAsString());
		            int noDel = DB.executeUpdate(sqlDel, trxName);

		            String sqlDel2 = "DELETE FROM P_Employee_Expensereports WHERE report_id = " + DB.TO_STRING( jobject.get("report_id").getAsString());
		            int noDel2 = DB.executeUpdate(sqlDel2, trxName);
		            

		            P_Employee_Expensereports ExpenseReports = new P_Employee_Expensereports( Env.getCtx(), -1, null);
		            ExpenseReports.setExpensereportsType("ZohoExpense");
		            ExpenseReports.setReport_Id( jobject.get("report_id").getAsString());
		            ExpenseReports.setReport_Name( jobject.get("report_name").getAsString());
		    		ExpenseReports.setDescription( jobject.get("description").getAsString());
		            ExpenseReports.setReport_Number( jobject.get("report_number").getAsString());
		            ExpenseReports.setStart_Date( TimeUtilSolstice.StringToTimestamp( jobject.get("start_date").getAsString()));
				    ExpenseReports.setEnd_Date( TimeUtilSolstice.StringToTimestamp(jobject.get("end_date").getAsString()));
				    ExpenseReports.setStatus  ( jobject.get("status").getAsString());
				    
				    ExpenseReports.setIs_Ach_Reimbursement( jobject.get("is_ach_reimbursement").getAsBoolean());
				    
				    ExpenseReports.setIs_Archived( jobject.get("is_archived").getAsBoolean());
				    ExpenseReports.setDue_Date( TimeUtilSolstice.StringToTimestamp(jobject.get("due_date").getAsString()));
				    ExpenseReports.setSubmitted_Date( TimeUtilSolstice.StringToTimestamp(jobject.get("submitted_date").getAsString()));
				    ExpenseReports.setApproved_Date( TimeUtilSolstice.StringToTimestamp(jobject.get("approved_date").getAsString()));
				    ExpenseReports.setLast_Submitted_Date( TimeUtilSolstice.StringToTimestamp(jobject.get("last_submitted_date").getAsString()));
				    ExpenseReports.setCurrency_Id( jobject.get("currency_id").getAsString());
				    ExpenseReports.setCurrency_Code( jobject.get("currency_code").getAsString());
//				    ExpenseReports.setApprover_ID( jobject.get("approver_id").getAsString());
				    ExpenseReports.setPayrun_Name( jobject.get("payrun_name").getAsString());
//				    ExpenseReports.setClaim_Type_ID( jobject.get("claim_type_id").getAsString());
				    ExpenseReports.setClaim_Type_Name( jobject.get("claim_type_name").getAsString());
				    ExpenseReports.setApprover_Name( jobject.get("approver_name").getAsString());
				    ExpenseReports.setAudit_Status( jobject.get("audit_status").getAsString());
				    ExpenseReports.setApprover_Email( jobject.get("approver_email").getAsString());
				    ExpenseReports.setApprover_Photo_Url( jobject.get("approver_photo_url").getAsString());
				    ExpenseReports.setApprover_Employee_No( jobject.get("approver_employee_no").getAsString());
				    ExpenseReports.setApprover_Zuid( jobject.get("approver_zuid").getAsString());
				    ExpenseReports.setSubmitted_To_Id( jobject.get("submitted_to_id").getAsString());
				    ExpenseReports.setSubmitted_To_Zuid( jobject.get("submitted_to_zuid").getAsString());
				    ExpenseReports.setSubmitted_To_Name( jobject.get("submitted_to_name").getAsString());
				    ExpenseReports.setSubmitted_To_Email( jobject.get("submitted_to_email").getAsString());
				    ExpenseReports.setSubmitted_To_Employee_No( jobject.get("submitted_to_employee_no").getAsString());
				    ExpenseReports.setSubmitter_Email( jobject.get("submitter_email").getAsString());
				    ExpenseReports.setSubmitted_By( jobject.get("submitted_by").getAsString());
				    ExpenseReports.setSubmitter_Name( jobject.get("submitter_name").getAsString());
				    ExpenseReports.setSubmitter_Zuid( jobject.get("submitter_zuid").getAsString());
//				    ExpenseReports.setTotal   ( jobject.get("total").getAsBigDecimal());
				    ExpenseReports.setTotal   ( jobject.get("reimbursable_total").getAsBigDecimal() );
				    ExpenseReports.setReimbursable_Total( jobject.get("reimbursable_total").getAsBigDecimal());
				    ExpenseReports.setAmount_To_Be_Reimbursed( jobject.get("amount_to_be_reimbursed").getAsBigDecimal());
				    ExpenseReports.setNon_Reimbursable_Total( jobject.get("non_reimbursable_total").getAsBigDecimal());
				    if( jobject.get("reimbursement_date").getAsString() != "" && jobject.get("reimbursement_date").getAsString() != null)
				    	ExpenseReports.setReimbursement_Date( TimeUtilSolstice.StringToTimestamp(jobject.get("reimbursement_date").getAsString()));
				    else 	
				    	ExpenseReports.setReimbursement_Date(null);

				    ExpenseReports.setCurrency_Id(jobject.get("rcy_currency_id").getAsString());
				    ExpenseReports.setCurrency_Code(jobject.get("rcy_currency_code").getAsString());
				    ExpenseReports.setRcy_Exchange_Rate(jobject.get("rcy_exchange_rate").getAsBigDecimal());
				    ExpenseReports.setRcy_Price_Precision(jobject.get("rcy_price_precision").getAsString());
				    ExpenseReports.setRcy_Currency_Code(jobject.get("rcy_currency_symbol").getAsString());
				    ExpenseReports.setRcy_Total(jobject.get("rcy_total").getAsBigDecimal());
				    ExpenseReports.setNon_Reimbursable_Total(jobject.get("rcy_reimbursable_total").getAsBigDecimal());
				    ExpenseReports.setRcy_Non_Reimbursable_Total(jobject.get("rcy_non_reimbursable_total").getAsBigDecimal());
				    ExpenseReports.setCreated_Time(TimeUtilSolstice.StringToTimestamp(jobject.get("created_time").getAsString().substring(0,19)));
				    ExpenseReports.setLast_Modified_Time(TimeUtilSolstice.StringToTimestamp(jobject.get("last_modified_time").getAsString().substring(0,19)));
				    ExpenseReports.setCreated_Date(TimeUtilSolstice.StringToTimestamp(jobject.get("created_date").getAsString()));
				    ExpenseReports.setLast_Modified_Date(TimeUtilSolstice.StringToTimestamp(jobject.get("last_modified_date").getAsString()));
//				    ExpenseReports.setCreated_By_Id(jobject.get("created_by_id").getAsString() );
//				    ExpenseReports.setCreated_By_Zuid(jobject.get("created_by_zuid").getAsString());
				    ExpenseReports.setCreated_By_Name(jobject.get("created_by_name").getAsString());
					ExpenseReports.setCreator_Photo_Url(jobject.get("creator_photo_url").getAsString() );
					ExpenseReports.setCreator_Employee_No(jobject.get("creator_employee_no").getAsString());
//					ExpenseReports.setUser_ID (jobject.get("user_id").getAsString());
					ExpenseReports.setUser_Email(jobject.get("user_email").getAsString());
					ExpenseReports.setUser_Name(jobject.get("user_name").getAsString());
					ExpenseReports.setUser_Photo_Url(jobject.get("user_photo_url").getAsString());
					ExpenseReports.setIs_Shared_Report(jobject.get("is_shared_report").getAsBoolean());
					ExpenseReports.setComments_Count(jobject.get("comments_count").getAsInt());
//					ExpenseReports.setPolicy_Violated(jobject.get("policy_violated").getAsString());
					ExpenseReports.setReceipt_Policy_Violation_Count(jobject.get("receipt_policy_violation_count").getAsInt());
					ExpenseReports.setDescription_Policy_Violation_Count(jobject.get("description_policy_violation_count").getAsInt());
					ExpenseReports.setAmount_Policy_Violation_Count(jobject.get("amount_policy_violation_count").getAsInt());
					ExpenseReports.setTotal_Policy_Violation_Count(jobject.get("total_policy_violation_count").getAsInt());
					ExpenseReports.setCustom_Policy_Violation_Count(jobject.get("custom_policy_violation_count").getAsInt());
					ExpenseReports.setUncategorized_Expense_Count(jobject.get("uncategorized_expense_count").getAsInt());
//					ExpenseReports.setCustomer_Id(jobject.get("customer_id").getAsString());
//					ExpenseReports.setZcrm_Potential_ID(jobject.get("zcrm_potential_id").getAsString());
//					ExpenseReports.setProject_ID(jobject.get("project_id").getAsString());
					ExpenseReports.setSubmitter_Photo_Url(jobject.get("submitter_photo_url").getAsString());
					ExpenseReports.setSubmitter_Employee_No(jobject.get("submitter_employee_no").getAsString());
					ExpenseReports.setCustomer_Name(jobject.get("customer_name").getAsString());
					ExpenseReports.setProject_Name(jobject.get("project_name").getAsString());
					ExpenseReports.setCan_Push_To_Accounting_App(jobject.get("can_push_to_accounting_app").getAsString());
					ExpenseReports.setAccounting_App_Name(jobject.get("accounting_app_name").getAsString());
//					ExpenseReports.setAccounting_Reference_ID(jobject.get("accounting_reference_id").getAsString());
					ExpenseReports.setAccounting_Sync_Time(jobject.get("accounting_sync_time").getAsString());
					ExpenseReports.setEmployee_Number(jobject.get("employee_number").getAsString());
//					ExpenseReports.setDepartment_Id(jobject.get("department_id").getAsString());
					ExpenseReports.setDepartment_Name(jobject.get("department_name").getAsString());
//					ExpenseReports.setTrip_Id (jobject.get("trip_id").getAsString());
					ExpenseReports.setTrip_Number(jobject.get("trip_number").getAsString());
					ExpenseReports.setTrip_Name(jobject.get("trip_name").getAsString());
					ExpenseReports.setTrip_Status(jobject.get("trip_status").getAsString());
					ExpenseReports.setIs_International_Trip(jobject.get("is_international_trip").getAsString());
//					ExpenseReports.setPolicy_ID(jobject.get("policy_id").getAsString());
					ExpenseReports.setTrip_Destination_Country(jobject.get("trip_destination_country").getAsString());
					ExpenseReports.setTrip_Destination_City(jobject.get("trip_destination_city").getAsString());
					ExpenseReports.setTrip_Departure(jobject.get("trip_departure").getAsString());
					ExpenseReports.setTrip_Budget_Amount(jobject.get("trip_budget_amount").getAsBigDecimal());
//					ExpenseReports.setTrip_Budget_Amount_Currency_ID(jobject.get("trip_budget_amount_currency_id").getAsString());
					ExpenseReports.setTrip_Budget_Amount_Currency_Code(jobject.get("trip_budget_amount_currency_code").getAsString());
					ExpenseReports.setTrip_Start_Date(  TimeUtilSolstice.StringToTimestamp(jobject.get("trip_start_date").getAsString()));
					ExpenseReports.setTrip_End_Date( TimeUtilSolstice.StringToTimestamp(jobject.get("trip_end_date").getAsString()));
					ExpenseReports.setPolicy_Name(jobject.get("policy_name").getAsString());
					ExpenseReports.setPolicy_Display_Name(jobject.get("policy_display_name").getAsString());
					ExpenseReports.setDue_Days(jobject.get("due_days").getAsString());
					ExpenseReports.setSub_Status(jobject.get("sub_status").getAsString());
					ExpenseReports.setExpense_Count(jobject.get("expense_count").getAsString());
					ExpenseReports.setP_Employee_ID( getWithValue( Env.getCtx(), ExpenseReports.getEmployee_Number(), null  ));

					ExpenseReports.setP_Period_ID( P_Period.getOpenPeriod(Env.getCtx(), null).getP_Period_ID() );
		            P_Employee Emp = P_Employee.get( Env.getCtx(), ExpenseReports.getP_Employee_ID(), null);
		            ExpenseReports.setAD_Org_ID( Emp.getAD_Org_ID() );
					
				    ExpenseReports.save();

				    // Import détail.
		            ReadListExpenseReportDetail( jobject.get("report_id").getAsString(), Emp );

				    
				    m_inserted = m_inserted + 1;

	            }
	        }	
        }
	     
         return jarray;
		 
		 
	}

	
	public JsonArray ReadListExpenseReportDetail( String report_id, P_Employee Employee ) throws Exception
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
	            if(jExpenses != null) {   
	                  
	                //Iterating JSON array  
	                for(int i=0;i<jExpenses.size();i++){   
	                      
	                	listdata = new ArrayList<Object>();  
	                	listdata.add(jExpenses.get(i));
	                	
	                	for(Object dataObj: listdata) {
	                		
//	                	        System.out.println("String Data : " + dataObj);
	                	        
	                	        JsonElement jElement = JsonParser.parseString( dataObj.toString());
	            	            JsonObject jObject = jElement.getAsJsonObject();
	            	            JsonArray  jarray2 = jObject.getAsJsonArray("line_items");
// 2026-09-08
	            	            for ( int t=0;t<jarray2.size();t++ )
	            	            {
		            	            JsonObject jobject = jarray2.get(t).getAsJsonObject();
		            	            if( ! jobject.isJsonNull()) 
		            	            {
		            	            	int chargeId = getChargeID(Env.getCtx(), Employee.getTaxation_Region_ID(),  jobject.get("gl_code").getAsString().trim(), null); 
		            	            	MCharge charge = null;
		            	            	if ( chargeId != 0 )
		            	            		charge = new MCharge( Env.getCtx(), chargeId , null);
		            	            	
		            	            	BigDecimal tps_rate = null ;
		            	            	BigDecimal tvq_rate = null ;
		            	            	BigDecimal tvh_rate = null ;

		            	            	if ( charge != null )
		            	            	{
			            	            	tps_rate = charge.getTPS_Rate();
			            	            	tvq_rate = charge.getTVQ_Rate();
			            	            	tvh_rate = charge.getTVH_Rate();
		            	            	}

		            	            	if ( tps_rate == null ) tps_rate = Env.ZERO;
		            	            	if ( tvq_rate == null ) tvq_rate = Env.ZERO;
		            	            	if ( tvh_rate == null ) tvh_rate = Env.ZERO;
		            	            	
		            	            	tps_rate = tps_rate.divide(new BigDecimal(100));
		            	            	tvq_rate = tvq_rate.divide(new BigDecimal(100));
		            	            	tvh_rate = tvh_rate.divide(new BigDecimal(100));

		            	            	BigDecimal amount = jobject.get("claimed_bcy_total").getAsBigDecimal();
/*		            	            	
		            	            	BigDecimal tps_amt = (amount).multiply(tps_rate);
		            	            	BigDecimal tvq_amt = (amount).multiply(tvq_rate);
		            	            	BigDecimal tvh_amt = (amount).multiply(tvh_rate);
*/
		            	            	BigDecimal tps_amt = Env.ZERO;
		            	            	BigDecimal tvq_amt = Env.ZERO;
		            	            	BigDecimal tvh_amt = Env.ZERO;

		            	            	// TVH jobject.get("tax_id").getAsString().equals("4951942000000845225") || 
		            	            	String tax_Name = jobject.get("tax_name").getAsString();
		            	            	if ( tax_Name.startsWith( "TVH" ) )  
		            	            	{
		            	            		tps_amt = Env.ZERO;
		            	            		tvq_amt = Env.ZERO;
		            	            		tvh_amt = jobject.get("tax_amount").getAsBigDecimal();
		            	            	}
		            	            	
		            	            	// TPS TVQ jobject.get("tax_id").getAsString().equals("4951942000000997188")  ||
		            	            	/*
		            	            	 * Formules de calculTPS (5 %) :$$\text{TPS} = \text{Total des 2 taxes} \times \frac{5}{14{,}975} \approx \text{Total des 2 taxes} \times 0{,}33389$$TVQ (9,975 %) :$$\text{TVQ} = \text{Total des 2 taxes} \times \frac{9{,}975}{14{,}975} \approx \text{Total des 2 taxes} \times 0{,}66611$$
		            	            	 */
		            	            	else if ( tax_Name.startsWith( "TPS/TVQ" )    ) 
	            	            	    {
		            	            		tvh_amt = Env.ZERO;
		            	            		BigDecimal totaltax = jobject.get("tax_amount").getAsBigDecimal();
		            	            		tvq_amt = totaltax.multiply( new BigDecimal( 0.66611 )).setScale(2, BigDecimal.ROUND_HALF_UP);
		            	            		tps_amt = totaltax.multiply( new BigDecimal( 0.33389 )).setScale(2, BigDecimal.ROUND_HALF_UP);
	            	            		
		            	            		tps_amt = totaltax.subtract(tvq_amt);
		            	            		
		            	            	}
		            	            	else if ( tax_Name.startsWith( "TVQ" )    ) 
	            	            	    {
		            	            		tvh_amt = Env.ZERO;
		            	            		BigDecimal totaltax = jobject.get("tax_amount").getAsBigDecimal();
		            	            		tvq_amt = totaltax;
		            	            		tps_amt = Env.ZERO;
		            	            	}
		            	            	else if ( tax_Name.startsWith( "TPS" )    ) 
	            	            	    {
		            	            		tvh_amt = Env.ZERO;
		            	            		BigDecimal totaltax = jobject.get("tax_amount").getAsBigDecimal();
		            	            		tvq_amt = Env.ZERO;
		            	            		tps_amt = totaltax;
		            	            	}
		            	            	else if ( tax_Name.startsWith( "NS HST" )    ) 
	            	            	    {
		            	            		BigDecimal totaltax = jobject.get("tax_amount").getAsBigDecimal();
		            	            		tvq_amt = Env.ZERO;
		            	            		tps_amt = Env.ZERO;
		            	            		tvh_amt = totaltax;
		            	            	}
		            	            	else	{
		            	            		tvh_amt = Env.ZERO;
		            	            		tvq_amt = Env.ZERO;
		            	            		tps_amt = Env.ZERO;
		            	            	}
		            	            		
		            	            	
		            	            	BigDecimal tax_amt = tps_amt.add( tvq_amt ).add( tvh_amt );
		            	            	
		            		            P_Employee_Expensereports_Detail ExpenseReportsDet = new P_Employee_Expensereports_Detail( Env.getCtx(), -1, null);
		            		            ExpenseReportsDet.setP_Employee_Expensereports_ID(  getExpensereports_ID( Env.getCtx(), report_id, null));
		            		            ExpenseReportsDet.setreport_id(report_id);
		            		            ExpenseReportsDet.setTax_Amount( jobject.get("tax_amount").getAsBigDecimal() );
		            		            ExpenseReportsDet.setClaimed_Bcy_Total( jobject.get("claimed_bcy_total").getAsBigDecimal());
		            		        	ExpenseReportsDet.setAmount(  amount );
		            		        	ExpenseReportsDet.setLine_Item_Code(jobject.get("line_item_id").getAsString());
		            		        	ExpenseReportsDet.setCategory_Name(jobject.get("category_name").getAsString());
		            		        	ExpenseReportsDet.setItem_Date(TimeUtilSolstice.StringToTimestamp(jobject.get("item_date").getAsString()));
		            		        	ExpenseReportsDet.setUser_Name(jobject.get("user_name").getAsString());
		            		        	ExpenseReportsDet.setTax_Name(jobject.get("tax_name").getAsString());
		            		        	ExpenseReportsDet.setDescription(jobject.get("description").getAsString());
		            		        	ExpenseReportsDet.setItem_Total(jobject.get("item_total").getAsBigDecimal());
		            		        	ExpenseReportsDet.setItem_Total_Inclusive_Of_Tax(jobject.get("item_total_inclusive_of_tax").getAsBigDecimal());
		            		        	ExpenseReportsDet.setTax_Code(jobject.get("tax_id").getAsString());
		            		        	ExpenseReportsDet.setItem_Order(jobject.get("item_order").getAsInt());
		            		        	ExpenseReportsDet.setGl_Code(jobject.get("gl_code").getAsString());
		            		        	ExpenseReportsDet.setBcy_Total(jobject.get("bcy_total").getAsBigDecimal());
		            		        	ExpenseReportsDet.setCategory_code(jobject.get("category_id").getAsString());
		            		        	ExpenseReportsDet.setTax_Type(jobject.get("tax_type").getAsString() );
//		            		        	ExpenseReportsDet.setUser_Code(jobject.get("user_id").getAsString());
		            		        	ExpenseReportsDet.setClaimed_Fcy_Total(jobject.get("claimed_fcy_total").getAsBigDecimal());
		            		        	ExpenseReportsDet.setTax_Percentage(jobject.get("tax_percentage").getAsBigDecimal());
		            		        	ExpenseReportsDet.setIs_reimbursable( jObject.get("is_reimbursable").getAsString().equals("true") );
		            		        	
		            		        	ExpenseReportsDet.setC_ElementValue_ID( getElementValue( Env.getCtx(), jobject.get("gl_code").getAsString(), null ));
		            		        	ExpenseReportsDet.setTax_Amount( tax_amt );
		            		        	ExpenseReportsDet.settps_amt(tps_amt);
		            		        	ExpenseReportsDet.settvq_amt(tvq_amt);
		            		        	ExpenseReportsDet.settvh_amt( tvh_amt );
		            		        	
		            		        	if ( jObject.get("is_reimbursable").getAsString().equals("true") )
		            		        		ExpenseReportsDet.save();
		            	            	
		            	            }
	            	            	
	            	            }
	            	            
	                	}	                	
	                }   
	                
	            }  	            
	            
	     }
		 catch(Exception e)
		 {
	 			log.log(Level.SEVERE,"ImportExportZohoV2 " , e);
		 }	     
		 
	     
         return jarray;
		 
		 
	}

	

	public int getWithValue( Properties ctx, String Value, String trxName  ) 
	{
		int ID = 0;
		String sql = null;
		sql = "Select P_Employee.P_Employee_ID from P_Employee WHERE Value = " + DB.TO_STRING( Value );
		//
		
		P_Employee Employee = null;
		
		PreparedStatement pstmt = null;
		
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
//				Employee = P_Employee.get( ctx, rs.getInt( "P_Employee_ID"), trxName);
				ID = rs.getInt( "P_Employee_ID");
			}
			
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"P_Employee - " + sql, e);
		}

		return ID;
	}

	public int getElementValue( Properties ctx, String Value, String trxName  ) 
	{
		int ID = 0;
		String sql = null;
		sql = "select  C_ELEMENTVALUE_ID FROM C_ELEMENTVALUE WHERE C_ELEMENT_ID = 1100010 and C_ELEMENTVALUE.value = " + DB.TO_STRING( Value );
		//
		
		
		PreparedStatement pstmt = null;
		
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				ID = rs.getInt( "C_ELEMENTVALUE_ID");
			}
			
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"C_ELEMENTVALUE_ID - " + sql, e);
		}

		return ID;
	}

	public int getChargeID( Properties ctx, int Region_ID, String Value, String trxName  ) 
	{
		int ID = 0;
		String sql = null;
		sql = "select  C_Charge_ID FROM C_Charge WHERE C_Region_ID = " + Region_ID + " and C_Charge.value = " + DB.TO_STRING( Value );
		//
		
		
		PreparedStatement pstmt = null;
		
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				ID = rs.getInt( "C_Charge_ID");
			}
			
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"C_Charge_ID - " + sql, e);
		}

		return ID;
	}

	
	
	public int getExpensereports_ID( Properties ctx, String Value, String trxName  ) 
	{
		int ID = 0;
		String sql = null;
		sql = "Select P_Employee_Expensereports_ID from P_Employee_Expensereports WHERE report_id = " + DB.TO_STRING( Value );
		//
		
		PreparedStatement pstmt = null;
		
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
//				Employee = P_Employee.get( ctx, rs.getInt( "P_Employee_ID"), trxName);
				ID = rs.getInt( 1 );
			}
			
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"P_Employee - " + sql, e);
		}

		return ID;
	}
	
	
	public static void main(String[] args)
	{
		System.out.println("ImportExportZohoV2   $Revision: 1.0 $");
		System.out.println("----------------------------------");
		//
		Compiere.startup(true);
		
		
		int count = 0;

		new ImportExportZohoV2( true);
		count++;
		System.out.println("Generated = " + count);

		
		
	}	//	main
	
}
