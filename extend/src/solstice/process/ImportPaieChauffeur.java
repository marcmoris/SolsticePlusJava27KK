package solstice.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MCharge;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import solstice.model.P_Assignment;
import solstice.model.P_Employee;
import solstice.model.P_Employee_Expensereports;
import solstice.model.P_Employee_Expensereports_Detail;
import solstice.model.P_Gain;
import solstice.model.P_Particular_Sheet;
import solstice.model.P_Period;
import solstice.model.P_Post;
import solstice.model.X_I_Driver_Sheet;
import solstice.model.X_I_Expense_Account;

public class ImportPaieChauffeur extends SvrProcess{

	private int Period_ID = 0;
	private int Payment_Group_ID = 0;
	private int Employee_ID = 0;
	private int m_inserted = 0;
    private boolean IsRework = false;
	
    //Trans_remboursable. = "O" compte de dépense. activite_no = 101
	
	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (ImportPaieChauffeur.class);
	
	OkHttpClient client = new OkHttpClient();
	
	protected void prepare()
    {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("P_Period_ID"))
				Period_ID = para[i].getParameterAsInt();
			else if (name.equals("P_Payment_Group_ID"))
				Payment_Group_ID = para[i].getParameterAsInt();
			else if (name.equals("P_Employee_ID"))
				Employee_ID = para[i].getParameterAsInt();
			else if (name.equals("Rework"))
			    IsRework = "Y".equals(para[i].getParameter());
			
			else
				log.log (Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}		
    }
	
	public JsonArray runApi( Timestamp StartDate, Timestamp EndDate) throws Exception
	{

		//
		//  obtain an OAuth 2.0 token for running the API
		//
		 String grant_type = "client_credentials";
		 String clientId = "e8e66c0a-28fe-4edb-8e77-cbecc3e22e35";
		 String client_secret= "72b8Q~A5fw7Nt69TgZSXTxFqAOxRFoTCe_L5_dBG";
		 String scope = "https://orgd2519c1d.crm3.dynamics.com/.default";

		 okhttp3.RequestBody formBody = new okhttp3.FormBody.Builder()
	                .add("grant_type", grant_type)
	       	        .add("client_secret", client_secret)
	                .add("client_id", clientId)
	                .add("scope", scope)
	                .build();
		 
		 String ur = "https://login.microsoftonline.com/eb58acc3-69a7-4abd-988c-acd12198c58b/oauth2/v2.0/token";
		 String token = "";
		 
		 Request request = new Request.Builder()
	    		    .url(ur)
	    		    .post(formBody)
	                .addHeader("Content-Type", "application/x-www-form-urlencoded")
	                .build();
	                
	     okhttp3.Response respnse = null;
		 
	     try {
	            
	            respnse = client.newCall(request).execute();
	            
	            String resp = respnse.body().string();
	            JsonElement jsonElement = JsonParser.parseString(resp);
	            JsonObject jsonObject = jsonElement.getAsJsonObject();
	            
	            token = jsonObject.get("access_token").getAsString();    
	     }
		 catch (Exception e)
		 {
 			log.log(Level.SEVERE,"ImportPaieChauffeur " , e);
		 }	     
	
	 
//	     MediaType mediaType = MediaType.parse( "application/json" );
	     
	     okhttp3.RequestBody formBody2 = new okhttp3.FormBody.Builder()
	                .add("grant_type", "Bearer")
	    		 
	                .build();

	     DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	     String s_startdate = df.format( StartDate );

	     
		 Request request2 = new Request.Builder()
	    		 	.addHeader("Authorization", "Bearer " + token)
	    		 	.addHeader("Content-Type", "application/json;")
				    .url("https://orgd2519c1d.crm3.dynamics.com/api/data/v9.2/crf06_transport_transaction_detail2s?$filter=crf06_trans_date ge " + s_startdate ) 
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

	            jarray  = jsonObject.getAsJsonArray("value");
	            
	            
	     }
		 catch (Exception e)
		 {
	 			log.log(Level.SEVERE,"ImportPaieChauffeur " , e);
		 }	     
		 
         return jarray;
		 
		 
	}
	 
    String message = "";

	
	protected String doIt() throws Exception
	{
		boolean retValue = true;
		String ret = "Importation terminée ";
		message = "";
		
	    P_Period Period;
	        
	    if ( Period_ID != 0 )
	       	Period = new P_Period( Env.getCtx(), Period_ID, null);
	    else
	      	Period = P_Period.getOpenPeriod(Env.getCtx(), 0, null);
	   
	    if ( IsRework )
	    {
	    	String sql = "delete from I_Driver_Sheet WHERE 1=1 ";
            if ( Employee_ID != 0 )
            {
            	sql = sql + " and P_Employee_ID = " + Employee_ID;
            }
            else if ( Payment_Group_ID != 0  )
            {
            	sql = sql + " and P_Employee_ID IN ( SELECT P_Employee_ID From P_Employee where P_Payment_Group_ID = " + Payment_Group_ID + " )";            	
            }
	    	
		    DB.executeUpdate(sql , null);
	    }
    
	    try {
			JsonArray jarray = runApi( Period.getStartDate(), Period.getEndDate());
			
//	        for (int i = 0; i < jarray.size(); i++)
			jarray.forEach(item -> 
	        {
	            JsonObject jobject = (JsonObject) item; //jarray.get(i).getAsJsonObject();

	            JsonElement numEmp = jobject.get("crf06_emp_no");
	            JsonElement crf06_paye_soumis = jobject.get("crf06_paye_soumis");
	            JsonElement crf06_trans_remboursable = jobject.get("crf06_trans_remboursable");
          
	            if ( numEmp.isJsonNull() == false)
	            {
//	              System.out.println( jobject );

	            	String sPaye_soumis = "N";
	            	if ( crf06_paye_soumis.isJsonNull()  == false)
	            		sPaye_soumis = crf06_paye_soumis.getAsString();
	            	
//	            	String sTrans_remboursable = "N";
	            	String sTrans_remboursable = "-";
	            	if ( crf06_trans_remboursable.isJsonNull() == false )
	            		sTrans_remboursable = crf06_trans_remboursable.getAsString();
	            	
	            	
		            String sNumEmp = numEmp.getAsString();
		            
//		            crf06_trans_date_create
		            String crf06_trans_date = jobject.get("crf06_trans_date").getAsString();
		            String crf06_trans_supp = "";
		            if ( jobject.get("crf06_trans_supp").isJsonNull() == false )
		            	crf06_trans_supp = jobject.get("crf06_trans_supp").getAsString();
		            else 
		            	crf06_trans_supp = "O";
		            
		            int year = Integer.parseInt( crf06_trans_date.substring(0,4) );
		            int month = Integer.parseInt( crf06_trans_date.substring(5,7) );
		            int day = Integer.parseInt( crf06_trans_date.substring(8,10) );
		            Timestamp transdate = TimeUtil.getDay( year, month, day);

		            if ( transdate.compareTo( Period.getStartDate() ) >= 0 &&
	            		 transdate.compareTo( Period.getEndDate() ) <= 0 &&
	            		 sNumEmp.compareTo("99999") != 0 && 
	            		 ( sPaye_soumis.equals("O"))  &&  // sPaye_soumis.equals("P") ||
	            		 sTrans_remboursable.equals("N") &&
	            		 crf06_trans_supp.equals( "N" )
		            	)
		            {
			            JsonElement crf06_trans_montant = jobject.get("crf06_trans_montant");
			            JsonElement crf06_poste_paye_no = jobject.get("crf06_poste_paye_no");
//			            JsonElement TRANS_SUPP = jobject.get("crf06_poste_paye_no");

			            String Earning = "GA06";
			            P_Employee Employee = this.EmployeeGetWithValue( Env.getCtx(), sNumEmp, null );
			            P_Gain Gain = P_Gain.getWithValue( Env.getCtx(), Earning, null);

//			            log.log(Level.INFO,"ImportPaieChauffeur Import : " + sNumEmp );
//			 			log.log(Level.INFO,"ImportPaieChauffeur Import : " + Employee.getValue() + " " + Employee.getName() );
			            
			            String sPost = "";
			            P_Post Post = null;
			            P_Assignment Assignment = null;
			            
			            if ( crf06_poste_paye_no.isJsonNull() == false )	
			            {
//				 			log.log(Level.INFO,"ImportPaieChauffeur Import Post : " + Employee.getValue() + " " + Employee.getName() + " Poste : "  + sPost);

				 			sPost = crf06_poste_paye_no.getAsString();
				            Post = PostGetWithValue(  Env.getCtx(), sPost, null );
				            if ( Post == null)
				            {
				            	message = message + " Probleme avec : " + Employee.getValue() + " " + Employee.getName();
				            	log.log(Level.SEVERE,"ImportPaieChauffeur Import Post a null : " + Employee.getValue() + " " + Employee.getName() + " " + crf06_poste_paye_no.getAsString() );
				            }
				            else	
				            Assignment = AssignmentGetWithValue(  Env.getCtx(), Employee, Post, null);	
			            }
			            else
			            {
				 			log.log(Level.INFO,"ImportPaieChauffeur Import Post non trouvé : " + Employee.getValue() + " " + Employee.getName() );
			            	
			            }
			            	

			            BigDecimal amt = Env.ZERO;

			            if ( crf06_trans_montant.isJsonNull() == false)
			            	amt = crf06_trans_montant.getAsBigDecimal() ;
			            
			            
			            if ( Assignment == null)
			            {
			            	log.log(Level.SEVERE,"ImportPaieChauffeur Import Affectation non trouvé : " + sNumEmp + " Post : " + sPost );
			            }
			            	
			            else if ( Employee_ID != 0 && Employee.getP_Employee_ID() != Employee_ID)
			            {
			            	// ne rien faire
			            }
			            else if ( Payment_Group_ID != 0 && Employee.getP_Payment_Group_ID() != Payment_Group_ID )
			            {
			            	// ne rien faire
			            }
			            else
			            {
			            	
//				 			log.log(Level.INFO,"ImportPaieChauffeur Import : " + jobject );

			            	
//				            X_I_Driver_Sheet Result = new X_I_Driver_Sheet(  Env.getCtx(), -1, null );
				            X_I_Driver_Sheet Result = getDriver_Sheet(  Env.getCtx(), Employee, Post, null);
				            Result.setAD_Org_ID( Employee.getAD_Org_ID());
							Result.setP_Employee_ID( Employee.getP_Employee_ID() );
							Result.setP_Assignment_ID( Assignment.getP_Assignment_ID() );
							Result.setP_Gain_ID( Gain.getP_Gain_ID());
							Result.setEmployé( Employee.getValue() );
							Result.setPosition( Post.getValue() );
							Result.setP_Post_ID( Post.getP_Post_ID());
							Result.setEarning( Earning);
//							Result.setP_Period_ID( rs.getInt("P_Period_ID") );
//							Result.setDay( rs.getTimestamp("Trans_Date") );
							Result.setMontant( Result.getAmt().add( amt ) );
							Result.setAmt( Result.getAmt().add( amt ) );
							Result.setIsActive(true);
//							Result.setC_Activity_ID(rs.getInt("C_Activity_ID"));
//							Result.setP_Department_ID(rs.getInt("P_Department_ID"));
//							Result.setDay( transdate );
							if ( Result.getAmt().compareTo( Env.ZERO) != 0)
								Result.save();
							
							m_inserted++;
			            	
							log.log(Level.INFO,"ImportPaieChauffeur Import : " + Employee.getValue() + ' ' + jobject);
							
// 			                System.out.println( jobject );

							
			            }
		            }
	            }
	        });
	    	
	    }
		 catch (Exception e)
		 {
	 			log.log(Level.SEVERE,"ImportPaieChauffeur " , e);
		 }	    
		
		// Déclache le trigger
 	    DB.executeUpdate("update I_Driver_Sheet set updated=updated", null);
		
 	    Import_Expense();
 	    
		return retValue == true ? ret + " " + message : "error";

//    	return "";
    }
	
	
	private String Import_Expense() throws Exception
	{
		boolean retValue = true;
		String ret = "Importation compte de dépense terminée";

	    P_Period Period2;
        
	    if ( Period_ID != 0 )
	       	Period2 = new P_Period( Env.getCtx(), Period_ID, null);
	    else
	      	Period2 = P_Period.getOpenPeriod(Env.getCtx(), 0, null);
	   
//	    P_Period PeriodDebut;
	    P_Period PeriodFin;
	    PeriodFin = getLastPeriod( Env.getCtx(), Period2, null );
//	    PeriodDebut = getLastPeriod( Env.getCtx(), PeriodFin, null );
	    
	    if ( IsRework )
	    {
	    	String sql = "delete from I_Expense_Account WHERE nom='Chauffeur Expense' ";
            if ( Employee_ID != 0 )
            {
            	sql = sql + " and P_Employee_ID = " + Employee_ID;
            }
            else if ( Payment_Group_ID != 0  )
            {
            	sql = sql + " and P_Employee_ID IN ( SELECT P_Employee_ID From P_Employee where P_Payment_Group_ID = " + Payment_Group_ID + " )";            	
            }
	    	
		    DB.executeUpdate(sql , null);
	    }
	 
	     try {
	 		JsonArray jarray = runApi( PeriodFin.getStartDate() , PeriodFin.getEndDate() );
			
	        for (int i = 0; i < jarray.size(); i++)
	        {
	        	
	            JsonObject jobject = jarray.get(i).getAsJsonObject();

	            JsonElement numEmp = jobject.get("crf06_emp_no");
	            JsonElement crf06_paye_soumis = jobject.get("crf06_paye_soumis");
	            JsonElement crf06_trans_remboursable = jobject.get("crf06_trans_remboursable");
	            
	            if ( numEmp.isJsonNull() == false)
	            {
	            	String sPaye_soumis = crf06_paye_soumis.getAsString();
	            	
	            	String sTrans_remboursable = "N";
	            	if ( crf06_trans_remboursable.isJsonNull() == false )
	            		sTrans_remboursable = crf06_trans_remboursable.getAsString();
	            	
	            	
		            String sNumEmp = numEmp.getAsString();
		            
		            String crf06_trans_date = jobject.get("crf06_trans_date").getAsString();

		            int year = Integer.parseInt( crf06_trans_date.substring(0,4) );
		            int month = Integer.parseInt( crf06_trans_date.substring(5,7) );
		            int day = Integer.parseInt( crf06_trans_date.substring(8,10) );
		            Timestamp transdate = TimeUtil.getDay( year, month, day);

		            String crf06_trans_supp = "";
		            if ( jobject.get("crf06_trans_supp").isJsonNull() == false )
		            	crf06_trans_supp = jobject.get("crf06_trans_supp").getAsString();
		            else 
		            	crf06_trans_supp = "O";

		            if ( transdate.compareTo( PeriodFin.getStartDate() ) >= 0 &&
	            		 transdate.compareTo( PeriodFin.getEndDate() ) <= 0 &&
	            		 sNumEmp.compareTo("99999") != 0 && 
	            		 ( sPaye_soumis.equals("O"))  &&  // sPaye_soumis.equals("P") ||
	            		 crf06_trans_supp.equals( "N" ) &&
	            		 sTrans_remboursable.equals("O")
		            	)
		            {
			            JsonElement crf06_trans_montant = jobject.get("crf06_trans_montant");
			            JsonElement crf06_poste_paye_no = jobject.get("crf06_poste_paye_no");
//			            JsonElement TRANS_SUPP = jobject.get("crf06_poste_paye_no");

			            String Earning = "GL02";
			            P_Employee Employee = this.EmployeeGetWithValue( Env.getCtx(), sNumEmp, null );
			            P_Gain Gain = P_Gain.getWithValue( Env.getCtx(), Earning, null);

//			 			log.log(Level.INFO,"ImportPaieChauffeur Expense Import : " + Employee.getValue() + ' ' + Employee.getName() );
			            
			            String sPost = crf06_poste_paye_no.getAsString();
			            P_Post Post = PostGetWithValue(  Env.getCtx(), sPost, null );
//			            P_Assignment Assignment = AssignmentGetWithValue(  Env.getCtx(), Employee, Post, null);
			            BigDecimal amt = crf06_trans_montant.getAsBigDecimal() ;
			            
			            if ( Employee_ID != 0 && Employee.getP_Employee_ID() != Employee_ID)
			            {
			            	// ne rien faire
			            }
			            else if ( Payment_Group_ID != 0 && Employee.getP_Payment_Group_ID() != Payment_Group_ID )
			            {
			            	// ne rien faire
			            }
			            else
			            {
			            	JsonElement crf06_activite_no = jobject.get("crf06_activite_no");
			            	String sActivity = null;
			            	MCharge Charge = null;
			            	if ( crf06_activite_no.isJsonNull() == false)
			            	{
			            		sActivity = crf06_activite_no.getAsString();
			            		Charge = ChargeGetWithActivity( Env.getCtx(), sActivity, Employee.getTaxation_Region_ID(), null);
			            	}
			            		
							X_I_Expense_Account Result = getExpense_Account( Env.getCtx(), Employee, null );
//							X_I_Expense_Account Result = new X_I_Expense_Account( Env.getCtx(), -1, null ) ;
				            Result.setAD_Org_ID( Employee.getAD_Org_ID());
							Result.setP_Employee_ID( Employee.getP_Employee_ID() );
							Result.setP_Gain_ID( Gain.getP_Gain_ID());
							Result.setEmployé( Employee.getValue() );
							Result.setEarning( Earning);
							Result.setnom("Chauffeur Expense");
							Result.setI_IsImported(true);

							Result.setMontant( Result.getAmt().add( amt ) );
							Result.setAmt( Result.getAmt().add( amt ) );

//							Result.setAmt( Result.getAmt().add( amt ) );

							Result.setactivity_no( sActivity );
							if ( Charge != null && Charge.getTPS_Rate() != null && Charge.getTPS_Rate().compareTo( Env.ZERO) != 0  )
							Result.settps_amt( Result.getAmt().multiply( Charge.getTPS_Rate().divide(new BigDecimal(100)) ) );
							if ( Charge != null && Charge.getTVQ_Rate() != null && Charge.getTVQ_Rate().compareTo( Env.ZERO) != 0  )
							Result.settvq_amt( Result.getAmt().multiply( Charge.getTVQ_Rate().divide(new BigDecimal(100)) ) );
							if ( Charge != null && Charge.getTVH_Rate() != null && Charge.getTVH_Rate().compareTo( Env.ZERO) != 0  )
							Result.settvh_amt( Result.getAmt().multiply( Charge.getTVH_Rate().divide(new BigDecimal(100)) ) );

							Result.setIsActive(true);
							
							Result.setTransdate( transdate );
							
							if ( Charge == null)
								Result.setI_ErrorMsg( "Information de TPS TVQ non trouvé ");
							
							if ( Result.getAmt().compareTo( Env.ZERO) != 0)
								Result.save();
							
							// update table référence
//							String sql2 ="INSERT INTO I_Expense_Account_Processed VALUE ( " + rs.getString("CLE_DEBCPTDEPE") + " )"; 
//							DB.executeUpdate(sql2, null);
							
							m_inserted++;

							log.log(Level.INFO,"ImportPaieChauffeur Expense Import : " + Employee.getValue() + ' ' + Employee.getName() + ' ' + jobject);
// 			                System.out.println( jobject );

			            }
		            }
	            }
	        }
	     }
		 catch (Exception e)
		 {
	 			log.log(Level.SEVERE,"ImportPaieChauffeur " , e);
		 }	     
	    
	    

		
		// Déclache le trigger
 	    DB.executeUpdate("update I_Expense_Account set updated=updated", null);
		
		
		return retValue == true ? ret : "error";

//    	return "";
    }	

	
	private String Import_ExpenseV2() throws Exception
	{
		boolean retValue = true;
		String ret = "Importation compte de dépense terminée";

	    P_Period Period2;
        
	    if ( Period_ID != 0 )
	       	Period2 = new P_Period( Env.getCtx(), Period_ID, null);
	    else
	      	Period2 = P_Period.getOpenPeriod(Env.getCtx(), 0, null);
	   
//	    P_Period PeriodDebut;
	    P_Period PeriodFin;
	    PeriodFin = getLastPeriod( Env.getCtx(), Period2, null );
//	    PeriodDebut = getLastPeriod( Env.getCtx(), PeriodFin, null );
	    
	    if ( IsRework )
	    {
	    	String sql = "delete from P_Employee_Expensereports WHERE ExpensereportsType='Chauffeur Expense' ";
            if ( Employee_ID != 0 )
            {
            	sql = sql + " and P_Employee_ID = " + Employee_ID;
            }
            else if ( Payment_Group_ID != 0  )
            {
            	sql = sql + " and P_Employee_ID IN ( SELECT P_Employee_ID From P_Employee where P_Payment_Group_ID = " + Payment_Group_ID + " )";            	
            }
            
            sql = sql + " and P_Period_ID = " + Period2.getP_Period_ID();
	    	
		    DB.executeUpdate(sql , null);
	    }
	 
	     try {
	 		JsonArray jarray = runApi( PeriodFin.getStartDate() , PeriodFin.getEndDate() );
			
	        for (int i = 0; i < jarray.size(); i++)
	        {
	        	
	            JsonObject jobject = jarray.get(i).getAsJsonObject();

	            JsonElement numEmp = jobject.get("crf06_emp_no");
	            JsonElement crf06_paye_soumis = jobject.get("crf06_paye_soumis");
	            JsonElement crf06_trans_remboursable = jobject.get("crf06_trans_remboursable");
	            
	            if ( numEmp.isJsonNull() == false)
	            {
	            	String sPaye_soumis = crf06_paye_soumis.getAsString();
	            	
	            	String sTrans_remboursable = "N";
	            	if ( crf06_trans_remboursable.isJsonNull() == false )
	            		sTrans_remboursable = crf06_trans_remboursable.getAsString();
	            	
	            	
		            String sNumEmp = numEmp.getAsString();
		            
		            String crf06_trans_date = jobject.get("crf06_trans_date").getAsString();

		            int year = Integer.parseInt( crf06_trans_date.substring(0,4) );
		            int month = Integer.parseInt( crf06_trans_date.substring(5,7) );
		            int day = Integer.parseInt( crf06_trans_date.substring(8,10) );
		            Timestamp transdate = TimeUtil.getDay( year, month, day);

		            String crf06_trans_supp = "";
		            if ( jobject.get("crf06_trans_supp").isJsonNull() == false )
		            	crf06_trans_supp = jobject.get("crf06_trans_supp").getAsString();
		            else 
		            	crf06_trans_supp = "O";

		            if ( transdate.compareTo( PeriodFin.getStartDate() ) >= 0 &&
	            		 transdate.compareTo( PeriodFin.getEndDate() ) <= 0 &&
	            		 sNumEmp.compareTo("99999") != 0 && 
	            		 ( sPaye_soumis.equals("O"))  &&  // sPaye_soumis.equals("P") ||
	            		 crf06_trans_supp.equals( "N" ) &&
	            		 sTrans_remboursable.equals("O")
		            	)
		            {
			            JsonElement crf06_trans_montant = jobject.get("crf06_trans_montant");
			            JsonElement crf06_poste_paye_no = jobject.get("crf06_poste_paye_no");
//			            JsonElement TRANS_SUPP = jobject.get("crf06_poste_paye_no");

			            String Earning = "GL02";
			            P_Employee Employee = this.EmployeeGetWithValue( Env.getCtx(), sNumEmp, null );
			            P_Gain Gain = P_Gain.getWithValue( Env.getCtx(), Earning, null);

//			 			log.log(Level.INFO,"ImportPaieChauffeur Expense Import : " + Employee.getValue() + ' ' + Employee.getName() );
			            
			            String sPost = crf06_poste_paye_no.getAsString();
			            P_Post Post = PostGetWithValue(  Env.getCtx(), sPost, null );
//			            P_Assignment Assignment = AssignmentGetWithValue(  Env.getCtx(), Employee, Post, null);
			            BigDecimal amt = crf06_trans_montant.getAsBigDecimal() ;
			            
			            if ( Employee_ID != 0 && Employee.getP_Employee_ID() != Employee_ID)
			            {
			            	// ne rien faire
			            }
			            else if ( Payment_Group_ID != 0 && Employee.getP_Payment_Group_ID() != Payment_Group_ID )
			            {
			            	// ne rien faire
			            }
			            else
			            {
			            	JsonElement crf06_activite_no = jobject.get("crf06_activite_no");
			            	String sActivity = null;
			            	MCharge Charge = null;
			            	if ( crf06_activite_no.isJsonNull() == false)
			            	{
			            		sActivity = crf06_activite_no.getAsString();
			            		Charge = ChargeGetWithActivity( Env.getCtx(), sActivity, Employee.getTaxation_Region_ID(), null);
			            	}
			            		
			            	P_Employee_Expensereports Result = getExpense_AccountV2( Env.getCtx(), Employee, Period2.getP_Period_ID(),  null );
				            Result.setAD_Org_ID( Employee.getAD_Org_ID());
							Result.setP_Employee_ID( Employee.getP_Employee_ID() );
//							Result.setP_Gain_ID( Gain.getP_Gain_ID());
							Result.setExpensereportsType("Chauffeur Expense");
							Result.setStatus("Initial");
//							Result.setI_IsImported(true);

							Result.setRcy_Total( Result.getRcy_Total().add( amt ) );
//							Result.setAmount_To_Be_Reimbused( Result.getAmount_To_Be_Reimbused().add( amt ) );
//							Result.setReimbused_Total( Result.getReimbused_Total().add( amt ) );
							Result.setTotal( Result.getTotal().add( amt ) );
//							Result.setAmt( Result.getAmt().add( amt ) );

//							Result.setactivity_no( sActivity );
							if ( Charge != null && Charge.getTPS_Rate() != null && Charge.getTPS_Rate().compareTo( Env.ZERO) != 0  )
							Result.settps_amt( Result.getTotal().multiply( Charge.getTPS_Rate().divide(new BigDecimal(100)) ) );
							if ( Charge != null && Charge.getTVQ_Rate() != null && Charge.getTVQ_Rate().compareTo( Env.ZERO) != 0  )
							Result.settvq_amt( Result.getTotal().multiply( Charge.getTVQ_Rate().divide(new BigDecimal(100)) ) );
							if ( Charge != null && Charge.getTVH_Rate() != null && Charge.getTVH_Rate().compareTo( Env.ZERO) != 0  )
							Result.settvh_amt( Result.getTotal().multiply( Charge.getTVH_Rate().divide(new BigDecimal(100)) ) );

							Result.setIsActive(true);
							
							Result.setCreated_Date( transdate );
							
							if ( Charge == null)
								Result.setI_ErrorMsg( "Information de TPS TVQ non trouvé ");
							
							if ( Result.getTotal().compareTo( Env.ZERO) != 0)
								Result.save();
							
							// update table référence
//							String sql2 ="INSERT INTO I_Expense_Account_Processed VALUE ( " + rs.getString("CLE_DEBCPTDEPE") + " )"; 
//							DB.executeUpdate(sql2, null);
							
							m_inserted++;
							
							P_Employee_Expensereports_Detail detail = new P_Employee_Expensereports_Detail( getCtx(), -1, null);
							detail.setP_Employee_Expensereports_ID( Result.getP_Employee_Expensereports_ID());

							BigDecimal taxAmt = Env.ZERO;
							if ( Charge != null && Charge.getTPS_Rate() != null && Charge.getTPS_Rate().compareTo( Env.ZERO) != 0  )
								taxAmt = taxAmt.add( amt.multiply( Charge.getTPS_Rate().divide(new BigDecimal(100)) ) );
							if ( Charge != null && Charge.getTVQ_Rate() != null && Charge.getTVQ_Rate().compareTo( Env.ZERO) != 0  )
								taxAmt = taxAmt.add( amt.multiply( Charge.getTVQ_Rate().divide(new BigDecimal(100)) ) );
							if ( Charge != null && Charge.getTVH_Rate() != null && Charge.getTVH_Rate().compareTo( Env.ZERO) != 0  )
								taxAmt = taxAmt.add( amt.multiply( Charge.getTVH_Rate().divide(new BigDecimal(100)) ) );

							
							detail.setTax_Amount( amt  );
							detail.setAmount(amt);
							detail.setClaimed_Bcy_Total(amt);
							detail.setClaimed_Fcy_Total(amt);
							detail.setCategory_code(sActivity );
							detail.setCategory_Name("");
//							detail.setGl_Code( );
							

							detail.save();

							log.log(Level.INFO,"ImportPaieChauffeur Expense Import : " + Employee.getValue() + ' ' + Employee.getName() + ' ' + jobject);
// 			                System.out.println( jobject );

			            }
		            }
	            }
	        }
	     }
		 catch (Exception e)
		 {
	 			log.log(Level.SEVERE,"ImportPaieChauffeur " , e);
		 }	     
	    
	    

		
		// Déclache le trigger
 	    DB.executeUpdate("update I_Expense_Account set updated=updated", null);
		
		
		return retValue == true ? ret : "error";

//    	return "";
    }	



    	private P_Employee EmployeeGetWithValue( Properties ctx, String Value, String trxName  ) 
    	{
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
    				Employee = P_Employee.get( ctx, rs.getInt( "P_Employee_ID"), trxName);
    			}
    			
    			rs.close ();
    			pstmt.close ();
    			pstmt = null;
    		}
    		catch (Exception e)
    		{
     			log.log(Level.SEVERE,"ImportPaieChauffeur " + sql  , e);
    		}

    		return Employee;
    	}

    	private MCharge ChargeGetWithActivity( Properties ctx, String Activity, int C_Region_ID, String trxName  )
    	{
    		String sql = null;
    		sql = "Select C_Charge_ID from P_Expense_Account_Activity INNER JOIN C_CHARGE ON C_CHARGE.VALUE = P_Expense_Account_Activity.DEP_TYPE_TRANS"
    		    + " WHERE activite_no = " + DB.TO_STRING( Activity )
    		    + " AND P_Expense_Account_Activity.C_Region_ID = " + C_Region_ID
    		    + " and C_CHARGE.C_REGION_id = " + C_Region_ID
    		;
    		//
    		
    		MCharge Charge = null;
    		
    		PreparedStatement pstmt = null;
    		
    		try
    		{
    			pstmt = DB.prepareStatement (sql, null);
    			ResultSet rs = pstmt.executeQuery ();
    			if (rs.next ())
    			{
    				Charge = new MCharge( ctx, rs.getInt( "C_Charge_ID"), trxName);
    			}
    			
    			rs.close ();
    			pstmt.close ();
    			pstmt = null;
    		}
    		catch (Exception e)
    		{
     			log.log(Level.SEVERE,"ImportPaieChauffeur " + sql , e);
    		}

    		return Charge;
    		
    	}

    	
    	
    	private P_Post PostGetWithValue( Properties ctx, String Value, String trxName  ) 
    	{
    		String sql = null;
    		sql = "Select P_Post.P_Post_ID from P_Post WHERE ISACTIVE = 'Y' AND Value = " + DB.TO_STRING( Value );
    		//
    		
    		P_Post Post = null;
    		
    		PreparedStatement pstmt = null;
    		
    		try
    		{
    			pstmt = DB.prepareStatement (sql, null);
    			ResultSet rs = pstmt.executeQuery ();
    			if (rs.next ())
    			{
    				Post = new P_Post( ctx, rs.getInt( "P_Post_ID"), trxName);
    			}
    			
    			rs.close ();
    			pstmt.close ();
    			pstmt = null;
    		}
    		catch (Exception e)
    		{
     			log.log(Level.SEVERE,"ImportPaieChauffeur " + sql , e);
    		}

    		return Post;
    	}

    	
    	private P_Assignment AssignmentGetWithValue( Properties ctx, P_Employee Employee, P_Post Post, String trxName  ) 
    	{
    		String sql = null;
    		sql = "Select P_Assignment.P_Assignment_ID FROM P_Assignment "
    				+ " WHERE ISACTIVE = 'Y' AND P_Assignment.P_Employee_ID = " + Employee.getP_Employee_ID() 
    				+ " AND P_Assignment.P_post_Id = " + Post.getP_Post_ID()
    				;
    		//
    		
    		P_Assignment Assignment = null;
    		
    		PreparedStatement pstmt = null;
    		
    		try
    		{
    			pstmt = DB.prepareStatement (sql, null);
    			ResultSet rs = pstmt.executeQuery ();
    			if (rs.next ())
    			{
    				Assignment = new P_Assignment( ctx, rs.getInt( "P_Assignment_ID"), trxName);
    			}
    			
    			rs.close ();
    			pstmt.close ();
    			pstmt = null;
    		}
    		catch (Exception e)
    		{
     			log.log(Level.SEVERE,"ImportPaieChauffeur " + sql , e);
    		}

    		return Assignment;
    	}


    	private X_I_Driver_Sheet getDriver_Sheet( Properties ctx, P_Employee Employee, P_Post Post, String trxName  ) 
    	{
    		String sql = null;
    		sql = "Select I_Driver_Sheet.I_Driver_Sheet_ID FROM I_Driver_Sheet "
    				+ " WHERE I_Driver_Sheet.P_Employee_ID = " + Employee.getP_Employee_ID() 
    				+ " AND I_Driver_Sheet.P_post_Id = " + Post.getP_Post_ID()
    				;
    		//
    		
    		X_I_Driver_Sheet I_Driver_Sheet = null;
    		
    		PreparedStatement pstmt = null;
    		
    		try
    		{
    			pstmt = DB.prepareStatement (sql, null);
    			ResultSet rs = pstmt.executeQuery ();
    			if (rs.next ())
    			{
    				I_Driver_Sheet = new X_I_Driver_Sheet( ctx, rs.getInt( "I_Driver_Sheet_ID"), trxName);
    			}
    			else
    				I_Driver_Sheet = new X_I_Driver_Sheet( ctx, -1, null );
    			
    			rs.close ();
    			pstmt.close ();
    			pstmt = null;
    		}
    		catch (Exception e)
    		{
    			log.log(Level.SEVERE,"I_Driver_Sheet - " + sql, e);
    		}

    		return I_Driver_Sheet;
    	}

    	private P_Employee_Expensereports getExpense_AccountV2( Properties ctx, P_Employee Employee, int P_Period_ID,  String trxName  ) 
    	{
    		String sql = null;
    		sql = "Select P_Employee_Expensereports.P_Employee_Expensereports_ID FROM P_Employee_Expensereports "
    				+ " WHERE P_Employee_Expensereports.P_Employee_ID = " + Employee.getP_Employee_ID() 
    				+ " AND P_Employee_Expensereports.P_Period_ID = " + P_Period_ID
    				;
    		//
    		
    		P_Employee_Expensereports Expensereports = null;
    		
    		PreparedStatement pstmt = null;
    		
    		try
    		{
    			pstmt = DB.prepareStatement (sql, null);
    			ResultSet rs = pstmt.executeQuery ();
    			if (rs.next ())
    			{
    				Expensereports = new P_Employee_Expensereports( ctx, rs.getInt( "P_Employee_Expensereports_ID"), trxName);
    			}
    			else
    			{
    				Expensereports = new P_Employee_Expensereports( ctx, -1, trxName );
    				Expensereports.setP_Period_ID (P_Period_ID );
    				Expensereports.setP_Employee_ID(Employee.getP_Employee_ID());
    			}
    			
    			rs.close ();
    			pstmt.close ();
    			pstmt = null;
    		}
    		catch (Exception e)
    		{
    			log.log(Level.SEVERE,"I_Expense_Account - " + sql, e);
    		}

    		return Expensereports;
    	}

    	
    	private X_I_Expense_Account getExpense_Account( Properties ctx, P_Employee Employee, String trxName  ) 
    	{
    		String sql = null;
    		sql = "Select I_Expense_Account.I_Expense_Account_ID FROM I_Expense_Account "
    				+ " WHERE I_Expense_Account.P_Employee_ID = " + Employee.getP_Employee_ID() 
//    				+ " AND I_Expense_Account.P_post_Id = " + Post.getP_Post_ID()
    				;
    		//
    		
    		X_I_Expense_Account I_Expense_Account = null;
    		
    		PreparedStatement pstmt = null;
    		
    		try
    		{
    			pstmt = DB.prepareStatement (sql, null);
    			ResultSet rs = pstmt.executeQuery ();
    			if (rs.next ())
    			{
    				I_Expense_Account = new X_I_Expense_Account( ctx, rs.getInt( "I_Expense_Account_ID"), trxName);
    			}
    			else
    				I_Expense_Account = new X_I_Expense_Account( ctx, -1, null );
    			
    			rs.close ();
    			pstmt.close ();
    			pstmt = null;
    		}
    		catch (Exception e)
    		{
    			log.log(Level.SEVERE,"I_Expense_Account - " + sql, e);
    		}

    		return I_Expense_Account;
    	}

    	// Période - 1
    	private P_Period getLastPeriod( Properties ctx, P_Period _Period, String trxName  ) 
    	{
    		String sql = null;
    		sql = "Select max(P_Period.P_Period_ID) P_Period_ID from P_Period WHERE IsadjustmentPeriod = 'N' AND name < " + DB.TO_STRING( _Period.getName() );
    		//
    		
    		P_Period Period = null;
    		
    		PreparedStatement pstmt = null;
    		
    		try
    		{
    			pstmt = DB.prepareStatement (sql, null);
    			ResultSet rs = pstmt.executeQuery ();
    			if (rs.next ())
    			{
    				Period = P_Period.get( ctx, rs.getInt( "P_Period_ID"), trxName);
    			}
    			
    			rs.close ();
    			pstmt.close ();
    			pstmt = null;
    		}
    		catch (Exception e)
    		{
     			log.log(Level.SEVERE,"ImportPaieChauffeur " + sql , e);
    		}

    		return Period;
    	}
    	
}

