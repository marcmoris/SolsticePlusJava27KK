package solstice.process;

import java.sql.PreparedStatement;
import java.math.BigDecimal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;

import solstice.model.P_Assignment;
import solstice.model.P_Assignment_Param;
import solstice.model.P_Employee;
import solstice.model.P_Employee_LongTermLeave;
import solstice.model.P_Employee_Scale_Advance;
import solstice.model.P_Proposed_Adv_Date;
import solstice.model.P_Period;
import solstice.model.P_Year;

import solstice.model.P_Scale_Advance;

/*
 *  Procédure d'avancement d'échelon automatique.
 *  
 *   Différent paramètre possible :
 *   	Les paramètres sont toujours par convention.
 *   	Il y a 6 types d’intervalles possibles
 *   		-	Selon un nombre de jours dans une banques
 *   		-	1 fois par année, selon la date d’embauche
 *   		-	1 fois par année (date de début de période de paie)
 *   		-	2 fois par année a date fixe : ex  (1 mai et 1 octobre)
 *   		-	2 fois par année (date de début de période de paie)
 *   
 *   Les paramètres sont par échelon, donc 
 *     pour les échelons 1 a  8 le type d’intervalle est « 2 fois par année », 
 *     pour les échelons 9 a 18 le type d’intervalle est une fois par année.
 *   
 *   La procédure ce fait en 2 étapes.
 *   
 *    1iere étapes :
 *    	Sélection de tous les employés qui ont la possibilité d’avoir un avancement d’échelon en fonction soit : de la date du prochain 
 *      avancement d’échelon,  de leurs date d’embauche, ou du solde d’une banque.  (Cette procédure ne met a jours aucune donnée.)
 *    2iem étapes :
 *      Suite à la validation de l’usager de la sélection effectuer. 
 *      Une procédure effectue l’avancement d’échelon, en créant de nouveau paramètre 
 *      d’affectation,  pour tous les employés qui ont été désigner admissible 
 *      a un avancement d’échelon.  
 *      Et elle fait une mise a jours la date du prochaine avancement prévue 
 *      pour cette affectation.
 *      
 *      Replace P_FillScaleAdvanceTable
 *      
 */
public class P_EmployeeStepAdvance2 extends SvrProcess
{
 
	/*
	 *  (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#prepare()
	 */
	P_Period Period;
	P_Employee_Scale_Advance employeeScaleAdvance;
	private String Result = "Success";
	private boolean IsSave = false;
	
    protected void prepare()
    {
    	int Period_ID = 0;
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("P_Period_ID"))
				Period_ID = para[i].getParameterAsInt();
			else
				log.log (Level.SEVERE,"prepare - Unknown Parameter: " + name);
		}

		if ( Period_ID != 0 )
			Period = P_Period.get( Env.getCtx(), Period_ID, null);
		else
			Period = P_Period.getOpenPeriod( Env.getCtx(), null);

    }
    
    /**
     * Méthode principale du processus 
     */
    protected String doIt() throws Exception
    {
        /*DB.executeUpdate("delete from P_Employee_Scale_Advance WHERE IsReady ='Y' ", null);

    	String sql = "Select * FROM P_Scale_Advance where (Ad_Org_ID = 1000001 and Ad_Client_ID = 1000004)  ORDER BY RuleIndex";
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        // Pour chaque assignation,
        while(rs.next())
        {
        	P_Scale_Advance Scale_Advance = P_Scale_Advance.get(Env.getCtx(), rs.getInt( "P_Scale_Advance_ID"), null); 
            try
            {
            	selectEmployeeAdmissible( Scale_Advance);
            }
            catch(SQLException e)
            {
                log.log(Level.SEVERE, "Process Failed", e);
                return Msg.translate(Env.getLanguage(Env.getCtx()), "ProcessFailed");
            };

        }
        rs.close();
        stmt.close();*/
    
		try {
			proposeAdvancementDate();
			selectEmployeeAdmissible();
		} catch (Exception e) {
			log.log(Level.SEVERE, "Process Failed", e);
            return Msg.translate(Env.getLanguage(Env.getCtx()), "ProcessFailed");
		}    	
		return Msg.translate(Env.getLanguage(Env.getCtx()), Result);
    }

    private void proposeAdvancementDate() throws Exception
    {
    	Calendar debutPeriode;
    	Calendar finPeriode;
   	
    	String sql = " select  P_Employee_LongTermLeave.P_Employee_LongTermLeave_ID, P_Employee_LongTermLeave.P_Employee_ID,  P_Employee_LongTermLeave.EndDate,P_Employee_LongTermLeave.StartDate, " 
    		+ " P_Assignment.P_Assignment_ID, P_Assignment.ExpectedAdvanceDate " 	
    		+ "  from P_Employee_LongTermLeave, P_Assignment, P_Employee, P_LongTermLeave  "
    		+ " where P_Employee_LongTermLeave.P_Employee_ID = P_Assignment.P_Employee_ID "
		   + " and P_Employee_LongTermLeave.P_Employee_ID = P_Employee.P_Employee_ID "
		   + " and P_Employee_LongTermLeave.P_LongTermLeave_ID = P_LongTermLeave.P_LongTermLeave_ID "
		   + " and P_LongTermLeave.isDateReportable = 'Y' "
		   + " and P_Employee.IsActive = 'Y' "
		   + " and P_Assignment.IsActive = 'Y' "
 	       + " and P_Assignment.LastAdvanceDate is not null "
 	       + " and P_Assignment.ExpectedAdvanceDate is not null"
 	       + " and P_Assignment.StartDate <= " + DB.TO_DATE(Period.getEndDate())
    	   + " and ( P_Assignment.EndDate is null OR P_Assignment.EndDate > " + DB.TO_DATE(Period.getStartDate()) + " )"  
 	      ;
    	
    	PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        // Convertion des dates de debut et fin de periode en objet Calendar
        debutPeriode = GregorianCalendar.getInstance();
        finPeriode = GregorianCalendar.getInstance();
        debutPeriode.setTimeInMillis(Period.getStartDate().getTime());
        finPeriode.setTimeInMillis(Period.getEndDate().getTime());
        
        while (rs.next())
        { 
        	int nbrJours = 0;
        	Calendar debutAbsence = GregorianCalendar.getInstance();
        	Calendar finAbsence = GregorianCalendar.getInstance();
        	debutAbsence.setTimeInMillis(rs.getTimestamp("StartDate").getTime());
        	if ( rs.getTimestamp("EndDate") != null)
        		finAbsence.setTimeInMillis(rs.getTimestamp("EndDate").getTime());
        	else
        		finAbsence = finPeriode;
        	
        	Timestamp StartDate = rs.getTimestamp("StartDate");
        	Timestamp EndDate = rs.getTimestamp("EndDate");

        	if ( EndDate == null )
        		EndDate = Period.getEndDate();
        	
        	//Verifier si la date de debut d'absence et la date de 
        	//fin d'absence est comprise dans la periode courante
        	if (Period.getStartDate().compareTo(StartDate) < 0 &&
        		Period.getEndDate().compareTo(EndDate) > 0)
        	 // La date de debut et de fin d'absence sont incluse dans la periode
        		nbrJours = finAbsence.get(Calendar.DAY_OF_MONTH) - debutAbsence.get(Calendar.DAY_OF_MONTH);
        	else
        	{
        		//Verifier si la date de debut d'absence commence dans la periode et se termine
    			//dans un periode subsequente
        		if(	Period.getStartDate().compareTo(StartDate) < 0 &&
        			Period.getEndDate().compareTo(EndDate) < 0)
        			nbrJours = finPeriode.get(Calendar.DAY_OF_MONTH) - debutAbsence.get(Calendar.DAY_OF_MONTH);
        		
        		else
        		{
        			// Verifier si la date de debut d'absence commence dans un periode precedente
        			// et la date de fin d'absence se termine dans la periode courante
        			if(	Period.getStartDate().compareTo(StartDate) > 0 &&
        				Period.getEndDate().compareTo(EndDate) > 0)
        				nbrJours = finAbsence.get(Calendar.DAY_OF_MONTH) - debutPeriode.get(Calendar.DAY_OF_MONTH);
        			
        			else
        			{
        				if (rs.getTimestamp("StartDate").compareTo(Period.getStartDate())< 0 &&
        						Period.getEndDate().compareTo(EndDate) < 0)
        				//la periode d'absence couvre entirement la periode courante
            				nbrJours = 15;
        			}
        				
        			
        		}
        	}
        		
        		P_Proposed_Adv_Date dateProposee = new P_Proposed_Adv_Date(Env.getCtx(), -1, null);
        		P_Assignment Assignment = P_Assignment.get( Env.getCtx(), rs.getInt("P_Assignment_ID"), null);
        		P_Employee   Employee = P_Employee.get( Env.getCtx(), rs.getInt("P_Employee_ID"), null);
        		P_Employee_LongTermLeave longTermLeave = P_Employee_LongTermLeave.get(Env.getCtx(), rs.getInt("P_Employee_LongTermLeave_ID"), null);
        		
        		Calendar newExpectedDate = GregorianCalendar.getInstance();
        		newExpectedDate.setTimeInMillis(rs.getTimestamp("ExpectedAdvanceDate").getTime());
        		int jourDuMois = newExpectedDate.get(Calendar.DAY_OF_MONTH);
        		int nouveauJourMois = jourDuMois + nbrJours;
        		newExpectedDate.set(Calendar.DAY_OF_MONTH,nouveauJourMois);
        		dateProposee.setnewExpectedAdvanceDate(new Timestamp(newExpectedDate.getTime().getTime()));
        		dateProposee.setexcpectedAdvanceDate(rs.getTimestamp("ExpectedAdvanceDate"));
        		dateProposee.setLastAdvanceDate(Assignment.getLastAdvanceDate());
        		dateProposee.setP_Assignment_ID(rs.getInt("P_Assignment_ID"));
        		dateProposee.setP_Employee_ID(rs.getInt("P_Employee_ID"));
        		dateProposee.setP_Occupation_Group_ID(Employee.getP_Occupation_Group_ID());
        		dateProposee.setP_LongTermLeave_ID(longTermLeave.getP_LongTermLeave_ID());
        		dateProposee.save();
        }
        	
    }
    private void selectEmployeeAdmissible() throws Exception
    {
    	P_Scale_Advance Scale_Advance;
    	
//    	 Aller chercher les échelons de chaque classe 
        try
        {
      	String sql = "Select P_Assignment_Param.P_Assignment_Param_ID, P_Assignment_Param.P_Salary_Scale_ID, P_Salary_Scale_Detail.Step "
    		       + " FROM P_Employee, P_Assignment, P_Assignment_Param, P_Salary_Scale_Detail, P_Salary_Scale "
    		       + " WHERE P_Assignment.IsActive='Y' " 
        	       + "   AND P_Employee.IsActive='Y' "
        	       + "   AND P_Assignment_Param.IsActive='Y' "
        	       + "   AND P_Assignment_Param.P_Assignment_ID = P_Assignment.P_Assignment_ID "
        	       + "   AND P_Assignment.P_Employee_ID= P_Employee.P_Employee_ID "
        	       + "   AND P_Assignment_Param.P_Salary_Scale_ID = P_Salary_Scale.P_Salary_Scale_ID "
        	       + "   AND P_Assignment_Param.P_Salary_Scale_Detail_ID = P_Salary_Scale_Detail.P_Salary_Scale_Detail_ID "
        	       + "   AND " + DB.TO_DATE( Period.getStartDate() ) + " Between P_Assignment_Param.EffectIn AND isnull( P_Assignment_Param.EffectTo, " + DB.TO_DATE( Period.getStartDate() ) + " )"
        	       + "   AND " + DB.TO_DATE( Period.getStartDate() ) + " Between isnull( P_Assignment.startdate, " + DB.TO_DATE( Period.getStartDate() ) + " ) and isnull( P_Assignment.enddate, " + DB.TO_DATE( Period.getEndDate() ) + "  ) ";
  
    	// On vérifie seulement le mois et le jours, pas l'année.
    	sql = sql + "  AND (( P_Assignment.ExpectedAdvanceDate IS NOT NULL AND ( datepart( m,P_Assignment.ExpectedAdvanceDate)*100+datepart( d,P_Assignment.ExpectedAdvanceDate) Between datepart( m," + DB.TO_DATE( Period.getStartDate() ) + ")*100 + datepart( d," + DB.TO_DATE( Period.getStartDate() ) + ") AND datepart( m," + DB.TO_DATE( Period.getEndDate() )+")*100 + datepart( d," + DB.TO_DATE( Period.getEndDate() )+") ) )";
    	sql = sql + "  OR ( P_Assignment.ExpectedAdvanceDate IS NULL AND ( datepart( m,P_Employee.datehired)*100+datepart( d,P_Employee.datehired) Between datepart( m," + DB.TO_DATE( Period.getStartDate() ) + ")*100 + datepart( d," + DB.TO_DATE( Period.getStartDate() ) + ") AND datepart( m," + DB.TO_DATE( Period.getEndDate() )+")*100 + datepart( d," + DB.TO_DATE( Period.getEndDate() )+") ) ) )";
    	
    	sql = sql + "  AND P_Salary_Scale.Scale_Type <> 'B' ";
    	
    	sql = sql + " ORDER BY P_Assignment.StartDate Desc, P_Assignment.AssignmentType ";
    	

        PreparedStatement stmt = DB.prepareStatement(sql, null);
      	ResultSet rs = stmt.executeQuery();
      	
	        
        // Pour chaque assignation,
        while(rs.next())
        {
        	int nextStepSalary_Scale_ID = 0;
        	//Timestamp proposedDate = null;
        	P_Assignment_Param Assignment_Param = P_Assignment_Param.get( Env.getCtx(), rs.getInt("P_Assignment_Param_ID"), null);
        	P_Assignment Assignment             = P_Assignment.get( Env.getCtx(), Assignment_Param.getP_Assignment_ID(), null);
        	P_Employee   Employee               = P_Employee.get( Env.getCtx(), Assignment.getP_Employee_ID(), null);
        	// Verifier si l'employe a un echelon superieur
        	nextStepSalary_Scale_ID = getNextStep( Assignment_Param.getP_Salary_Scale_ID(), rs.getString("Step"));
        	if (nextStepSalary_Scale_ID > 0)
        			{	
        				 
		        		employeeScaleAdvance = new P_Employee_Scale_Advance(Env.getCtx(), -1, null);
		                employeeScaleAdvance.setP_Assignment_ID(Assignment.getP_Assignment_ID());
		                employeeScaleAdvance.setP_Employee_ID(Assignment.getP_Employee_ID());
		                employeeScaleAdvance.setP_Occupation_Group_ID(Employee.getP_Occupation_Group_ID());
		                if ( Assignment_Param.getRemuneration_Method().equals( P_Assignment_Param.REMUNERATION_METHOD_Annual))
		                	employeeScaleAdvance.setCurrentRate(Assignment_Param.getAnnual_Salary());
		                else
		                	employeeScaleAdvance.setCurrentRate(Assignment_Param.getHourly_Rate() );
		                
		                employeeScaleAdvance.setLastAdvanceDate( Assignment.getLastAdvanceDate() );
		                employeeScaleAdvance.setP_Salary_Class_ID( Assignment_Param.getP_Salary_Class_ID() );
		                employeeScaleAdvance.setCurrentScale_ID( Assignment_Param.getP_Salary_Scale_Detail_ID() );
		                employeeScaleAdvance.setP_Job_Title_ID( Assignment_Param.getP_Job_Title_ID() );
		                employeeScaleAdvance.setP_Job_Type_ID( Employee.getP_Job_Type_ID() );
		                employeeScaleAdvance.setNextScale_ID(nextStepSalary_Scale_ID);
		                employeeScaleAdvance.setExpectedRate(getExpectedRate(Assignment_Param.getP_Salary_Scale_ID(), rs.getString("Step") ));
		                //Aller chercher les parametres d'avancement d'echelle en fonction du groupe d'occupation de l'employe
		                Scale_Advance = getCurrentScaleAdvance(Employee.getP_Occupation_Group_ID(),nextStepSalary_Scale_ID);
		                if ( Scale_Advance != null)
		                {
			                //proposedDate = getProposedDate(Employee.getP_Employee_ID(),Period);
			                //if (proposedDate != null)
			                //	employeeScaleAdvance.setProposedDate(proposedDate);
			                if ( 
			                        Scale_Advance.getIntervalType().equals( P_Scale_Advance.INTERVALTYPE_1FoisParAnnéeDateDébutPériode ) ||
			                        Scale_Advance.getIntervalType().equals( P_Scale_Advance.INTERVALTYPE_2FoisParAnnéeDateDébutPériode ) 
			                     )
			                  {
			                  	employeeScaleAdvance.setEffectIn( Period.getStartDate() );
			                  }
			                else
			                {
			                	if ( Assignment.getExpectedAdvanceDate() != null )
			                		//employeeScaleAdvance.setEffectIn( getDateDebutPeriod(Scale_Advance, Assignment.getExpectedAdvanceDate()) );
			                		employeeScaleAdvance.setEffectIn(Assignment.getExpectedAdvanceDate());
			                	else
			                	{
			                		if ( Employee.getReactivation() != null )
			                			employeeScaleAdvance.setEffectIn( getNextAdvanceDate( Scale_Advance,  Employee.getReactivation(), false ));
			                		else 
			                			employeeScaleAdvance.setEffectIn( getNextAdvanceDate( Scale_Advance,  Employee.getDateHired(), false ));
			                	}
			                }
		                	employeeScaleAdvance.setNextAdvanceDate( getNextAdvanceDate( Scale_Advance,  employeeScaleAdvance.getEffectIn(), true  ) );
		                    if ( 
		                    		employeeScaleAdvance.getEffectIn().compareTo( Period.getStartDate()) >= 0 &&
		                    		employeeScaleAdvance.getEffectIn().compareTo( Period.getEndDate()) <= 0 	
		                    	)
		                    {
		                        employeeScaleAdvance.save();

		                        // Il faut s'assurer que le message de sortie soit valide parce que l'on fait tout
		                    	// les calculs de date et que l'on valide ensuite si on peut enregistrer l'information
		                    	// et que le message de sortie est mis à jour durant les calculs.
		                        if (Result.compareTo("Success") != 0)
		                        	IsSave = true;
		                    }
		                    else
		                    {
		                    	if (! IsSave)
		                    		Result = "Success";
		                    }
		                	
		                }
        			}
        }
        rs.close();
        stmt.close();
        }
        catch(Exception e)
        {
            log.log(Level.SEVERE, "Process Failed - P_EmployeeStepAdvance.selectEmployeeAdmissible", e);
            return ;
        }   
    }
    
    /**
     * getCurrentScaleAdvance
     * @param occupationGroupId
     * @param nextScale_ID
     * @return P_Scale_Advance
     * @throws SQLException
     */
    private P_Scale_Advance getCurrentScaleAdvance (int occupationGroupId, int nextScale_ID) throws Exception
    {
    	/*
    	 * Cette fonction sert a determiner dans quel intervalle notre prochain echelon se trouve en rapport
    	 * avec la table P_Scale_Advance. 
    	 * 4 types d'intervalle sont possible :
    	 * 	 -2 lettres : X - Y (StepFrom = X, StepTo = Y)
    	 *   -1 lettre et un chiffre : X - 00 (StepFrom = X, StepTo = 00)
    	 *   -2 chiffres: 00 - 00 (StepFrom = 00, StepTo = 00)
    	 *   -1 chiffre et null : 00 - null (StepFrom = 00, StepTo = null)
    	 * */
    	 
    	String nextScale = null;
    	String newNextScale = null;
    	boolean isLetter = false;
    	boolean returnObject = false;
    	int intNextScale = 0;
    	P_Scale_Advance scale_Advance = null;
    	
    	//Aller chercher le prochain echelon (determine par nextScale_ID)
    	String sql1 = "select Step from P_Salary_Scale_Detail "
    		+ " where P_Salary_Scale_Detail_ID = " + nextScale_ID + " ";
    	PreparedStatement stmt1 = DB.prepareStatement(sql1, null);
    	ResultSet rs1 = stmt1.executeQuery();
    	
    	while(rs1.next())
    	{
    		 nextScale = rs1.getString("Step");
    	}
    	if (nextScale.startsWith("0"))
    		//on verifie si c'est un chiffre de 0 a 9
    		 newNextScale = nextScale.substring(1);
    	
    	if (nextScale.startsWith(" "))
    		//On verifie si c'est une lettre
    	{
    		isLetter = true;
    		newNextScale = nextScale.trim();
    	}
    	
    	// On va chercher nos intervalle 
    	String sql = " select * from P_Scale_Advance where P_Occupation_Group_ID = " + occupationGroupId
        + " order by RuleIndex";
        PreparedStatement stmt = DB.prepareStatement(sql, null);
      	ResultSet rs = stmt.executeQuery();
      	
      while(!returnObject && rs.next())
      {
    	  if (isLetter)
    		  // si c'est une lettre on a 2 possibilites: on compare 2 lettres ou on compare 1 lettre et un chiffre
    	  {
    		  if (Character.isLetter(rs.getString("StepTo").charAt(0)))
    		  //on compare 2 lettres
    		  {
    			  if (newNextScale.compareTo(rs.getString("StepFrom")) > 0 && newNextScale.compareTo(rs.getString("StepTo")) < 0)
    				  returnObject = true; 
    		  } else //On compare une lettre et un chiffre
    		  {
    			  if (Character.isLetter(rs.getString("StepFrom").charAt(0)))
    			  {
    				  if (newNextScale.compareTo(rs.getString("StepFrom")) == 0)
        				  returnObject = true;
    			  }
    			  
    		  }
    	  } else
    	  {
    		  // c'est un chiffre
    		  intNextScale = Integer.parseInt(newNextScale);
    		  if (rs.getString("StepTo") != null)
    		  {
    			  if (Character.isDigit(rs.getString("StepTo").charAt(0)))
    			  {// le stepTo est un chiffre
    				  if (Character.isDigit(rs.getString("StepFrom").charAt(0)))
        				  // le StepFrom est un chiffre donc intervalle = 00 -00
        			  {
        				  if(intNextScale >= Integer.parseInt(rs.getString("StepFrom")) && intNextScale <= Integer.parseInt(rs.getString("StepTo")))
            				  returnObject = true;
        			  }
        			  else
        			  {
        				  if (Character.isLetter(rs.getString("StepFrom").charAt(0)))
        				  {	//Le stepFrom est une lettre donc intervalle = XX - 00
        					  if (intNextScale <= Integer.parseInt(rs.getString("StepTo")))
        						  returnObject = true;
        				  }
        			  }
    			  }  
    		  } else
    		  { // Le StepTo est a null
    			  if (Character.isDigit(rs.getString("StepFrom").charAt(0)))
    			  { // Le stepFrom est un chiffre donc intervalle 00 - null
    				  if (intNextScale == Integer.parseInt(rs.getString("StepFrom")))
        				  returnObject = true;
    			  }  	  
    		  }	  
    	  }
      }
       if (returnObject)
    	   scale_Advance = P_Scale_Advance.get(Env.getCtx(), rs.getInt( "P_Scale_Advance_ID"), null);
    		
    return scale_Advance;
    	
    }
    
    private Timestamp getProposedDate (int employee_ID, P_Period period) throws Exception
    {
    	Timestamp result = null;
    	String sql = " select P_Employee_LongTermLeave_ID, StartDate, EndDate "
    		+ "from P_Employee_LongTermLeave "
    		+ " where P_Employee_ID = " + employee_ID
    		;
    	
    	PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next())
        {
        	if (period.getEndDate().compareTo(rs.getTimestamp("EndDate")) < 0)
        	{ // La date de fin d'absence est plus grande que la date de la periode
        		result = rs.getTimestamp("EndDate");
        		
        	}
        }
    	
    	return result;
    }
    private int getNextStep( int P_Salary_Scale_ID, String Step  ) throws Exception
    {
    	/*String sql = " Select TOP 1 P_Salary_Scale_Detail_ID"
                   + "   From P_Salary_Scale_Detail"
                   + "   Where P_Salary_Scale_Detail.P_Salary_Scale_ID = " + P_Salary_Scale_ID
                   + "   and P_Salary_Scale_Detail.Step > " + Step
                   + "   order by Step"
                   ;*/
    	String sql = " Select P_Salary_Scale_Detail_ID, P_Salary_Scale_Detail.Step "
    		+ " From P_Salary_Scale_Detail"
    		+" Where P_Salary_Scale_Detail.P_Salary_Scale_ID = " + P_Salary_Scale_ID 
    		+ " order by P_Salary_Scale_Detail_ID, P_Salary_Scale_Detail.Step ";
    	int id = 0;
    	
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        // On boucle dans le recordSet pour trouver le prochain echelon
        boolean stop = false; 
        
        while(rs.next() && !stop)
        {
        	if (rs.getString("Step").equals(Step))
        	{
        		stop = true;
        		rs.next();
        		if(!rs.isAfterLast())
        			id = rs.getInt("P_Salary_Scale_Detail_ID");		
        	}
        }
        rs.close();
        stmt.close();

        return id;
        
    }

    private BigDecimal getExpectedRate( int P_Salary_Scale_ID, String Step ) throws Exception
    {
    	/*String sql = " Select TOP 1 isnull(Annual_Salary, Hourly_Rate) ExpectedRate "
                   + "   From P_Salary_Scale_Detail"
                   + "   Where P_Salary_Scale_Detail.P_Salary_Scale_ID = " + P_Salary_Scale_ID
                   + "     and P_Salary_Scale_Detail.Step > " + Step
                   + "  order by Step"
                   ;*/
    	String sql = "Select isnull(Annual_Salary, Hourly_Rate) ExpectedRate, Step "
    		+ " From P_Salary_Scale_Detail " 
    		+ " Where P_Salary_Scale_Detail.P_Salary_Scale_ID = " + P_Salary_Scale_ID
    		+ " order by Step";
    	
    	BigDecimal expectedRate= Env.ZERO;
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();

        boolean stop = false; 
        
        while(rs.next() && !stop)
        {
        	if (rs.getString("Step").equals(Step))
        	{
        		stop = true;
        		rs.next();
        		if(!rs.isAfterLast())
        			expectedRate = rs.getBigDecimal( "ExpectedRate");	
        	}
        }
        
        rs.close();
        stmt.close();

        return expectedRate;
    }

    
    private Timestamp getDateDebutPeriod( P_Scale_Advance Scale_Advance, Timestamp ExpectedDate ) throws NumberFormatException, Exception
    {
    	Calendar DateExpected = null;
        if (Scale_Advance.getIntervalType().equals( P_Scale_Advance.INTERVALTYPE_1FoisParAnnéeDateDébutPériode ) ||
        	Scale_Advance.getIntervalType().equals( P_Scale_Advance.INTERVALTYPE_2FoisParAnnéeDateDébutPériode )	)
        {
    		Calendar NextDate = GregorianCalendar.getInstance();
    		NextDate.set(new Integer(ExpectedDate.toString().substring(0, 4)).intValue(),
    				    new Integer(ExpectedDate.toString().substring(5, 7)).intValue() - 1,
						new Integer(ExpectedDate.toString().substring(8, 10)).intValue());
    		DateExpected = PeriodNextYear(NextDate);
        }
        if ( DateExpected == null )
        	return ExpectedDate;
        else
    	    return TimeUtil.getDay( DateExpected.getTimeInMillis());
    }
	
	
    private Calendar PeriodNextYear (Calendar NextDate) throws Exception
    {
    	Timestamp NextAdvanceDate = null;
       	Calendar DateNext = null;
    	String sql = "Select StartDate from p_period where '" + TimeUtil.getDay(NextDate.getTimeInMillis()) + "' Between StartDate and EndDate";
    	PreparedStatement smtp = DB.prepareStatement(sql, null);
    	ResultSet rs = smtp.executeQuery();
    	if ( rs.next() )
    	{
    		NextAdvanceDate = rs.getTimestamp("StartDate");
    		DateNext = GregorianCalendar.getInstance();
        	DateNext.set(new Integer(NextAdvanceDate.toString().substring(0, 4)).intValue(), 
        			     new Integer(NextAdvanceDate.toString().substring(5, 7)).intValue() - 1, 
    					 new Integer(NextAdvanceDate.toString().substring(8,10)).intValue());
    	}
    	
    	if (NextAdvanceDate == null)
    		Result = "Période pour le prochain avancement d'échelon inexistante";
    	
    	return DateNext;
    }
    private Timestamp getNextAdvanceDate(  P_Scale_Advance Scale_Advance, Timestamp lastAdvanceDate, boolean nextYear ) throws NumberFormatException, Exception
    {
    	
        // Si la date récupérée et transportée dans l'année de la
        // date du dernier avancement est plus grande que la date
        // du dernier avancement, on a trouvé la date du prochain
        // avancement
        Calendar NextAdvanceDate = GregorianCalendar.getInstance();
        P_Year Year = P_Year.get( Env.getCtx(), Period.getP_Year_ID(), null);

        NextAdvanceDate.setTimeInMillis(lastAdvanceDate.getTime());
        NextAdvanceDate.set(Calendar.YEAR, Year.getYear()  );

/*
    	if ( Scale_Advance.getIntervalType().equals( P_Scale_Advance.INTERVALTYPE_1FoisParAnnée ))
    	{
            NextAdvanceDate.set(Calendar.YEAR, NextAdvanceDate.get(Calendar.YEAR) + 1  );
    	}
    	
    	if ( Scale_Advance.getIntervalType().equals( P_Scale_Advance.INTERVALTYPE_1FoisParAnnéeDateDébutPériode ))
    	{
            NextAdvanceDate.set(Calendar.YEAR, NextAdvanceDate.get(Calendar.YEAR) + 1  );
    	}
*/
    	if ( Scale_Advance.getIntervalType().equals( P_Scale_Advance.INTERVALTYPE_1FoisParMois ))
    	{
            NextAdvanceDate.set(Calendar.MONTH, NextAdvanceDate.get(Calendar.MONTH) + 1  );
    	}

    	if ( Scale_Advance.getIntervalType().equals( P_Scale_Advance.INTERVALTYPE_SelonNombreDeJoursDansUneBanque ))
    	{
            NextAdvanceDate.setTimeInMillis(Period.getStartDate().getTime());
    	}

    	// Pour l'option 2 fois par année date de début de période
    	// on ne mais pas la date de début de période, car il y a des chance que la périod n'existe pas encore.
    	if ( Scale_Advance.getIntervalType().equals( P_Scale_Advance.INTERVALTYPE_2FoisParAnnéeDateDébutPériode ) || 
    	     Scale_Advance.getIntervalType().equals( P_Scale_Advance.INTERVALTYPE_2FoisParAnnéeDateFixe ))
    	{
            NextAdvanceDate.set(Calendar.YEAR, Year.getYear()  );
            NextAdvanceDate.set(Calendar.MONTH, new Integer( Scale_Advance.getSpecificDate1().substring(0,2)).intValue()-1 );
            NextAdvanceDate.set(Calendar.DAY_OF_MONTH, new Integer( Scale_Advance.getSpecificDate1().substring(3,5)).intValue() );
            
    		if ( Period.getStartDate().compareTo( TimeUtil.getDay( NextAdvanceDate.getTimeInMillis())) > 0)
    		{
                NextAdvanceDate.set(Calendar.YEAR, Year.getYear()  );
                NextAdvanceDate.set(Calendar.MONTH, new Integer( Scale_Advance.getSpecificDate2().substring(0,2)).intValue()-1 );
                NextAdvanceDate.set(Calendar.DAY_OF_MONTH, new Integer( Scale_Advance.getSpecificDate2().substring(3,5)).intValue() );

        		if ( Period.getStartDate().compareTo( TimeUtil.getDay( NextAdvanceDate.getTimeInMillis())) > 0)
        		{
                    NextAdvanceDate.set(Calendar.YEAR, Year.getYear() + 1  );
                    NextAdvanceDate.set(Calendar.MONTH, new Integer( Scale_Advance.getSpecificDate1().substring(0,2)).intValue()-1 );
                    NextAdvanceDate.set(Calendar.DAY_OF_MONTH, new Integer( Scale_Advance.getSpecificDate1().substring(3,5)).intValue() );
        		}
        	}
    			
    	}
    	
    	//
    	// determine la date du prochaine avancement d'échelon. année + 1
    	//
    	if ( nextYear ) 
    	{
    		Calendar NextDate = GregorianCalendar.getInstance();
    		if ( employeeScaleAdvance.getEffectIn().compareTo( TimeUtil.getDay( NextAdvanceDate.getTimeInMillis())) == 0 )
    			NextDate.set(NextAdvanceDate.get( Calendar.YEAR ) + 1, NextAdvanceDate.get( Calendar.MONTH ), NextAdvanceDate.get( Calendar.DAY_OF_MONTH));
    		else
    			NextDate = NextAdvanceDate;

    	// On prend la date de début de la période si elle existes.
        	if ( Scale_Advance.getIntervalType().equals( P_Scale_Advance.INTERVALTYPE_2FoisParAnnéeDateDébutPériode ) )
        	{
        		NextAdvanceDate = PeriodNextYear(NextDate);
        	}        		
        	else
        		NextAdvanceDate = NextDate;
        	if (NextAdvanceDate == null)
        		NextAdvanceDate = NextDate;
    	}
    		
		return TimeUtil.getDay( NextAdvanceDate.getTimeInMillis()); 
    }

}

