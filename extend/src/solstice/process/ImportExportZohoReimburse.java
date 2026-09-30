package solstice.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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


public class ImportExportZohoReimburse extends SvrProcess{

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

            Date today = Calendar.getInstance().getTime();      
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

			String sql = "SELECT * FROM P_Employee_Expensereports ";
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{ 
		        // Mise a jour du compte de dépense comme quoi il est réemboursé 
				// A faire a la fermeture de la paie.
		        ReimburseExpenseReport( rs.getString("Report_ID"), df.format(today), rs.getString("reimbursable_total") );

			}
			rs.close();
			pstmt.close();
	        
		}
	 	catch (Exception e)
	 	{
	 			log.log(Level.SEVERE,"ImportExportZohoReimburse " , e);
	 	}
		String ret = "Importation terminée";
		return retValue == true ? ret : "error";

    }
	
	Properties ctx;
	OkHttpClient client = new OkHttpClient();
	
	
	public ImportExportZohoReimburse ( Boolean standalone ) 
	{
		Env.setContext( ctx, "#AD_Client_ID", 1000004);
		Env.setContext( ctx, "#AD_Org_ID", 0);

	    try {
	    	doIt();
		}
	 	catch (Exception e)
	 	{
	 			log.log(Level.SEVERE,"ImportExportZohoReimburse " , e);
	 	}
    	        
	        
		
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

		
		String fileName = "C:\\Solstice\\Transferts\\Temp\\Token2_" + date + "_" + clientId; 
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
	

	
	public void ReimburseExpenseReport( String report_id, String sDate, String sAmount ) throws Exception
	{
		 String token = getAuthentication("ZohoExpense.fullaccess.ALL");
//		 String token = getAuthentication("ZohoExpense.expensereport.UPDATE");		 
	     
	     okhttp3.RequestBody formBody = new okhttp3.FormBody.Builder()
/*	                .add( "notes", "Payé par la paie")
	                .add( "date", sDate)
*/	                
//	                .add( "amount", sAmount)
	                
//	                .add( "reference_number", id)
//	                .add( "currency_id", "4951942000000000101")
	                
//	    		 	.add( "report_ids", report_id)
	    		    .add( "JSONString", "{\"amount\":" + sAmount + "}" )
	                .build();
		 
	     

		 Request request2 = new Request.Builder()
	    		 	.addHeader("Authorization", "Bearer " + token)
	    		 	.addHeader("X-com-zoho-expense-organizationid", "851698770")	    		 	
	    		 	.addHeader("Content-Type", "application/json;")
				    .url("https://www.zohoapis.com/expense/v1/expensereports/" + report_id + "/reimburse")
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

	
	public static void main (String[] args)
	{
		System.out.println("ImportExportZohoReimburse   $Revision: 1.0 $");
		System.out.println("----------------------------------");
		//
		Compiere.startup(true);
		
		
		int count = 0;

		new ImportExportZohoReimburse( true );
		count++;
		System.out.println("Generated = " + count);

	}	//	main
	
}
