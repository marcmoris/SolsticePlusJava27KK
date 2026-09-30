/*
 * Created on 13 juil. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.logging.Level;


import javax.servlet.jsp.JspException;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.custom.EMailSender;
import solstice.process.PgiUtil;
import solstice.model.P_Employee;
import solstice.model.P_ER_Expectation;
import solstice.custom.EvaluationRendementPTB;
import org.compiere.model.MUser;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_ER_Evaluation extends X_P_ER_Evaluation {

	/**
	 * 	Get Payment
	 *	@param ctx context
	 * 	@param P_Payment_ID id
	 *	@return Payment
	 */
	public static P_ER_Evaluation get (Properties ctx, int P_ER_Evaluation_ID, String trxName)
	{
		Integer key = new Integer (P_ER_Evaluation_ID);
		P_ER_Evaluation Form = (P_ER_Evaluation)s_cache.get(key);
		if (Form != null)
			return Form;
		Form = new P_ER_Evaluation (ctx, P_ER_Evaluation_ID, trxName);
		s_cache.put (key, Form);
		return Form;
	}	//	get
	/**	Cache						*/
	private static CCache<Integer,P_ER_Evaluation>	s_cache = new CCache<Integer,P_ER_Evaluation>("P_ER_Evaluation", 20);

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Employee.class);


	/**
	 * @param ctx
	 * @param P_ER_Evaluation_ID
	 */
	public P_ER_Evaluation(Properties ctx, int P_ER_Evaluation_ID, String trxName) {
		super(ctx, P_ER_Evaluation_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param ctx
	 * @param rs
	 */
	public P_ER_Evaluation(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	
    /**
     * Cette méthode envoie le courriel au gestionnaire
     */
    private void sendEMail(int EmployeeId, StringBuffer message)
    {
        try
        {
        	//TODO Paramétrer le email...
            EMailSender sender = new EMailSender(EmployeeId, PgiUtil.getSendEmailAddress( "drh"), Env.getCtx() );
            sender.sendEMail("Avis", message.toString());
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    /**
     * Avant d'enregistrer, on doit mettre à jour la date de l'évaluation
     */
    protected boolean beforeSave (boolean newRecord)
    {
        Calendar cal = GregorianCalendar.getInstance();
        Timestamp evalDate = new Timestamp(cal.getTimeInMillis());
        this.setEvaluationDate(evalDate);
        return true;
    }
    
    
    
    private String noticeHeader = null;
    private String noticeFooter = null;
    private String notice1 = null;
    private String notice2 = null;

	protected boolean afterSave (boolean newRecord, boolean success)
	{
	    if (!success )
		{
			return success;
		}

	    
		s_log.log(Level.INFO,"P_ER_Evaluation - isCompleted : " + this.isClosed() );
	    if ( this.isClosed() )
	    {
	    	P_Employee Employee = P_Employee.get( Env.getCtx(), this.getP_Employee_ID(), null);
	    	MUser User = MUser.get( Env.getCtx(), Employee.getAD_User_ID());
	    	
			StringBuffer message = new StringBuffer(" ");
			
			noticeHeader = "Avis";
			//TODO traduction

	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	        String StartDate;
	        String EndDate;

	        String Url = PgiUtil.getWebServerLogin( Env.getCtx(), User.getName()  ) + "evaluationRendement%2Fmoneval.jsp" ;
	        
	        StartDate = formatter.format(this.getStartingRefPeriodDate());
	        EndDate = formatter.format(this.getEndingRefPeriodDate());

			notice1 = "Votre évaluation de rendement couvrant la période du " + StartDate + " au " + EndDate + " et vos attentes " + this.getYear() + " sont maintenant complétées."; 
			notice2 = "Vous pouvez consulter le document à l'adresse suivante : " ;
			noticeFooter = "Pour toutes questions veuillez communiquer avec votre gestionnaire immédiat." ;

			
			
	        // Avis header
	        message.append(
	                "<div style=\"border: 2px solid #000000\"><br />" +
	                "<p align=\"center\" style=\"font-weight: bold; text-decoration: underline\"><br />" +
	                "Avis<br />" +
	                "</p><br />" +
	                "<p>" + "&nbsp;" + "</p><br />" +
	                "<p>" + this.notice1 + "</p><br />" +
	                "<p>" + this.notice2 + "<a href=\"" + Url + "\"> votre évaluation de rendement </a></p><br />" +
	                
	                "<br />");

	        message.append( "<p>" + this.noticeFooter  + "</p><br />" +
	                "</div><br /><br />");

	        this.sendEMail(this.getP_Employee_ID(), message);
			s_log.log(Level.INFO,"P_ER_Evaluation - Send Email to : " + Employee.getValue() + " "  + Employee.getName() );

	    }

		return true;
    }

		// validate the security
	public boolean beforeCopy( int OriginalId )
	{
/*		P_ER_Evaluation original = P_ER_Evaluation.get( Env.getCtx(), OriginalId, null);  
		if ( this.getP_Employee_Gest_ID() != original.getP_Employee_Gest_ID() )
		{
			
			return false;
		}
*/		
		return true;
	}
	
	public static P_ER_Evaluation getLastEvaluation( Properties ctx, int EmployeeID )
	{
		String sql = null;
		sql = "Select max( P_ER_Evaluation_ID ) P_ER_Evaluation_ID from P_ER_Evaluation where P_Employee_ID = " + EmployeeID ;
    	System.out.println( "Copy sql = " + sql );
		
		P_ER_Evaluation Evaluation = null;
		
		PreparedStatement pstmt = null;
		
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ()  )
			{
				if ( rs.getBigDecimal("P_ER_Evaluation_ID") != null )
					Evaluation  = P_ER_Evaluation.get(Env.getCtx(), rs.getInt("P_ER_Evaluation_ID"), null);
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Employee - " + sql, e);
		}

		return Evaluation;
	}
	
	public static P_ER_Evaluation copy( P_ER_Evaluation original, int EmployeeID )
	{
		// Valide que le gestion ne copie pas les évaluations d'un autre gestionnaire.
	
		if ( original == null || original.getP_ER_Evaluation_ID() <= 0 )
			return null;
		
		P_ER_Evaluation Evaluation = new P_ER_Evaluation( original.getCtx() , -1 , null);
		Evaluation.setStandardDefaults(  );
//		Evaluation.setBossBossEvaluationSignoff(original.getBossBossEvaluationSignoff());
//		Evaluation.setBossEvaluationSignoff(original.getBossEvaluationSignoff());
//		Evaluation.setBossExpectencySignoff(original.getBossExpectencySignoff());
		Evaluation.setClosed(false);
		Evaluation.setCR(original.getCR());
		Evaluation.setEmployeeCommentary(original.getEmployeeCommentary());
//		Evaluation.setEmployeeEvaluationSignoff(original.getEmployeeEvaluationSignoff());
//		Evaluation.setEmployeeExpectencySignoff(original.getEmployeeExpectencySignoff());
		
		Evaluation.setEvaluationType(original.getEvaluationType());
		Evaluation.setIsExpectationCompleted(false );
		Evaluation.setIsProbation( false );
		Evaluation.setOtherEmployeeRealisation(original.getOtherEmployeeRealisation());
		Evaluation.setP_Employee_Gest_ID(original.getP_Employee_Gest_ID());
		Evaluation.setP_Employee_ID(original.getP_Employee_ID());
		Evaluation.setP_ER_Form_ID(original.getP_ER_Form_ID());
		Evaluation.setP_Gain_ID(original.getP_Gain_ID());
		Evaluation.setP_Job_Title_ID(original.getP_Job_Title_ID());
		Evaluation.setP_Job_Type_ID(original.getP_Job_Type_ID());
		Evaluation.setP_Post_ID(original.getP_Post_ID());
		Evaluation.setRecommendation(original.getRecommendation());
		Evaluation.setStartingWorkDate(original.getStartingWorkDate());
		Evaluation.setUNA(original.getUNA());
		Evaluation.setGlobalQuotation(original.getGlobalQuotation());
		Evaluation.setGlobalQuotationReason(original.getGlobalQuotationReason());

		
		String[] dates = null;
        try
        {
        	Timestamp EndingDateLastEvaluation = EvaluationRendementPTB.getLastFormEndingDate(EmployeeID);
            if(EndingDateLastEvaluation == null)
            {
                dates = EvaluationRendementPTB.findDates(EmployeeID);
            }
            else
            {
            	dates = EvaluationRendementPTB.setDateFromLast(EndingDateLastEvaluation);
            }
        }
        catch (JspException e)
        {
        	System.out.println( "Error Copy procedure " + e);
        }
        
		Evaluation.setStartingRefPeriodDate( new Timestamp( PgiUtil.stringToDate( dates[0] ).getTimeInMillis() ));
		Evaluation.setEndingRefPeriodDate( new Timestamp( PgiUtil.stringToDate( dates[1] ).getTimeInMillis() ));
		Evaluation.setYear( Integer.parseInt( dates[2] ));

		Evaluation.save();

		
        try
        {
        	Evaluation.copyExpectation(original.getP_ER_Evaluation_ID());
			Evaluation.copyCompetence(original.getP_ER_Evaluation_ID());
			Evaluation.copyDeveloppement(original.getP_ER_Evaluation_ID());
            Evaluation.copyMeeting( original.getP_ER_Evaluation_ID() );
        }
        catch (SQLException e)
        {
        	System.out.println( "Error Copy procedure " + e);
        }

		return Evaluation;
	}
	
    /**
     * Procédure de copie. 
     */
    public void afterCopy(int originalId)
    { 
        try
        {
            this.copyExpectation(originalId);
            this.copyCompetence(originalId);
            this.copyDeveloppement(originalId);
//            this.copyMeeting( originalId);
        }
        catch (SQLException e)
        {
            log.log(Level.SEVERE, "afterCopy", e);
        }
    }
    	
    /**
     * Copie de la table P_ER_Expectation
     */
    private void copyExpectation(int originalId) throws SQLException
    {
        // On sort tous les enregistrements à copier de la table
        String sql
        = "select * from P_ER_Expectation"
            + " where P_ER_Evaluation_ID = " + originalId;
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // On crée un nouvel enregistrement pour le gain copié
        	P_ER_Expectation Expectation = new P_ER_Expectation(Env.getCtx(), -1, null);
        	Expectation.setP_ER_Evaluation_ID( this.getP_ER_Evaluation_ID() );
        	Expectation.setIsActive(rs.getString("IsActive").equals("Y"));
        	Expectation.setAchievementAssessment(rs.getString( "AchievementAssessment"));
        	Expectation.setAppreciationElement(rs.getString( "AppreciationElement"));
        	Expectation.setCommentary(rs.getString( "Commentary"));
        	Expectation.setExpectationQuotation(rs.getString( "ExpectationQuotation"));
        	Expectation.setExpectedresult(rs.getString( "Expectedresult"));
        	Expectation.setPNumber(rs.getInt( "PNumber"));
        	Expectation.save();
        }
        
        rs.close();
        stmt.close();
    }

    /**
     * Copie de la table P_ER_Competence
     */
    private void copyCompetence(int originalId) throws SQLException
    {
        // On sort tous les enregistrements à copier de la table
        String sql
        = "select * from P_ER_Competence"
            + " where P_ER_Evaluation_ID = " + originalId;
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // On crée un nouvel enregistrement
        	P_ER_Competence Competence = new P_ER_Competence(Env.getCtx(), -1, null);
        	Competence.setP_ER_Evaluation_ID( this.getP_ER_Evaluation_ID() );
        	Competence.setCommentary(rs.getString("Commentary"));
        	Competence.setIsActive(rs.getString("IsActive").equals("Y"));
        	Competence.setPNumber(rs.getInt("PNumber"));
        	Competence.setQuotation(rs.getString("Quotation"));
        	Competence.save();
        }
        
        rs.close();
        stmt.close();
    }


    /**
     * Copie de la table P_ER_Developpement
     */
    private void copyDeveloppement(int originalId) throws SQLException
    {
        // On sort tous les enregistrements à copier de la table
        String sql
        = "select * from P_ER_Developpement"
            + " where P_ER_Evaluation_ID = " + originalId;
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // On crée un nouvel enregistrement 
        	P_ER_Developpement Developpement = new P_ER_Developpement(Env.getCtx(), -1, null);
        	Developpement.setP_ER_Evaluation_ID( this.getP_ER_Evaluation_ID() );
        	Developpement.setIsActive(rs.getString("IsActive").equals("Y"));
        	Developpement.setAction(rs.getString("Action"));
//        	Developpement.setDueDate(rs.getTimestamp("DueDate"));
        	Developpement.setImprovement(rs.getString("Improvement"));
        	Developpement.setPNumber(rs.getInt("PNumber"));
        	Developpement.save();
        }
        
        rs.close();
        stmt.close();
    }

    /**
     * Copie de la table P_ER_Developpement
     */
    private void copyMeeting(int originalId) throws SQLException
    {
        // On sort tous les enregistrements à copier de la table
        String sql
        = "select * from P_ER_Meeting"
            + " where P_ER_Evaluation_ID = " + originalId;
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // On crée un nouvel enregistrement 
        	P_ER_Meeting Meeting = new P_ER_Meeting(Env.getCtx(), -1, null);
        	Meeting.setP_ER_Evaluation_ID( this.getP_ER_Evaluation_ID() );
        	Meeting.setIsActive(rs.getString("IsActive").equals("Y"));
        	Meeting.setMeetingDate(rs.getTimestamp("MeetingDate"));
        	Meeting.setPNumber(rs.getInt("PNumber"));
        	Meeting.save();
        }
        
        rs.close();
        stmt.close();
    }


}
