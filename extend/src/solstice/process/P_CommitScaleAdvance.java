/*
 * Created on 2005-09-14
 */
package solstice.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import solstice.model.P_Assignment;
import solstice.model.P_Assignment_Param;
import solstice.model.P_Employee;
import solstice.model.P_Payment_Group;
import solstice.model.P_Period;
import solstice.model.P_Year;


/**
 * @author frafor01
 *
 * Ce processus lit à jour les enregistrements de la table P_Employee_Scale_Advance
 * et effectue les avancements sélectionnés
 */
public class P_CommitScaleAdvance extends SvrProcess
{

    private P_Period Period = null;

	public P_CommitScaleAdvance()
    {
        super();
    }

    /**
     * Récupération des paramètres de lancement
     */
    protected void prepare()
    {
        // Il n'y a aucun paramètre à récupérer

    	Period = P_Period.getOpenPeriod( Env.getCtx(), null);

    }

    /**
     * Méthode principale du processus d'avancement
     */
    protected String doIt() throws Exception
    {
//		Env.setContext( Env.getCtx(), "#AD_Client_ID", 11);
//		Env.setContext( Env.getCtx(), "#AD_Org_ID", 11);

        // On lit les enregistrements de la table P_Employee_Scale_Advance
        String sql
        = "SELECT employeeScaleAdvance.P_Assignment_ID,"
        	 + " salaryScaleDetail.Annual_Salary,"
        	 + " salaryScaleDetail.Hourly_Rate, "
        	 + " P_Salary_Scale.Remuneration_Method, "
        	 + " employeeScaleAdvance.NextScale_ID, NextAdvanceDate, employeeScaleAdvance.EffectIn"
        	 + " FROM P_Employee_Scale_Advance employeeScaleAdvance," 
        	 + "      P_Salary_Scale_Detail salaryScaleDetail,"
        	 + "      P_Salary_Scale,"
        	 + "      P_Assignment assignment,"
        	 + "      P_Assignment_Param  assignmentParam, employeeScaleAdvance.P_Employee_ID"
        	 + " WHERE employeeScaleAdvance.IsReady = 'Y'"
        	 + "   AND assignmentParam.EffectIn = DBO.P_AssignmentEffectInDate( assignment.P_Assignment_ID)"
        	 + "   AND employeeScaleAdvance.NextScale_ID = salaryScaleDetail.P_Salary_Scale_Detail_ID"
        	 + "   AND salaryScaleDetail.P_Salary_Scale_ID = P_Salary_Scale.P_Salary_Scale_ID" 
        	 + "   AND employeeScaleAdvance.P_Assignment_ID = assignment.P_Assignment_ID" 
        	 + "   AND assignment.P_Assignment_ID = assignmentParam.P_Assignment_ID"
        	 + " ";
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                // On doit d'abord vérifier s'il existe une ou plusieurs affectations
                // futures à la période courrante.
                Enumeration futureAssignment;
                if((futureAssignment = getFutureAssignment(rs.getInt("P_Assignment_ID"))) != null)
                {
                    // Dans ce cas, on doit modifier le champ P_Salary_Scale_ID pour
                    // chacune de ces assignation. 
                    while(futureAssignment.hasMoreElements())
                    {
                        // On récupère l'assignation à modifier et on les modifie
                        int assignmentParamId = ((Integer)futureAssignment.nextElement()).intValue();
                        P_Assignment_Param assignmentParam = new P_Assignment_Param(Env.getCtx(), assignmentParamId, null);
                        assignmentParam.setP_Salary_Scale_Detail_ID(rs.getInt("NextScale_ID"));
                        assignmentParam.save();
                    }
                }
                
                // On crée ensuite une affectation_param pour l'avancement d'échellon qui prend
                // effet maintenant
                P_Assignment_Param newAssignmentParam = new P_Assignment_Param(Env.getCtx(), -1, null);
                P_Assignment_Param currentAssignmentParam = getCurrentAssignmentParam(rs.getInt("P_Assignment_ID"));
                newAssignmentParam.setIsActive(true);
                newAssignmentParam.setP_Assignment_ID(rs.getInt("P_Assignment_ID"));
                newAssignmentParam.setP_Salary_Scale_Detail_ID(rs.getInt("NextScale_ID"));
                newAssignmentParam.setEffectIn( rs.getTimestamp("EffectIn"));


                newAssignmentParam.setRemuneration_Method(rs.getString("Remuneration_Method"));
                newAssignmentParam.setIsOut_Of_Range(currentAssignmentParam.isOut_Of_Range());
                newAssignmentParam.setIsOut_Of_Rate(currentAssignmentParam.isOut_Of_Rate());
                newAssignmentParam.setCumulatedHours(currentAssignmentParam.getCumulatedHours());
                newAssignmentParam.setDay_Hours(currentAssignmentParam.getDay_Hours());
                newAssignmentParam.setOut_Of_Rate_Contractual(currentAssignmentParam.getOut_Of_Rate_Contractual());
                newAssignmentParam.setOut_Of_Rate_Salary(currentAssignmentParam.getOut_Of_Rate_Salary());

                newAssignmentParam.setP_Collective_Labour_Agr_ID( currentAssignmentParam.getP_Collective_Labour_Agr_ID());
//                if(currentAssignmentParam.getP_Collective_Labour_Agr_ID() > 0) newAssignmentParam.setP_Collective_Labour_Agr_ID(currentAssignmentParam.getP_Collective_Labour_Agr_ID());
                if(currentAssignmentParam.getP_Job_Title_ID() > 0) newAssignmentParam.setP_Job_Title_ID(currentAssignmentParam.getP_Job_Title_ID());
                if(currentAssignmentParam.getP_Occupation_Group_ID() > 0) newAssignmentParam.setP_Occupation_Group_ID(currentAssignmentParam.getP_Occupation_Group_ID());
                if(currentAssignmentParam.getP_Salary_Class_ID() > 0) newAssignmentParam.setP_Salary_Class_ID(currentAssignmentParam.getP_Salary_Class_ID());
                if(currentAssignmentParam.getP_Salary_Scale_ID() > 0) newAssignmentParam.setP_Salary_Scale_ID(currentAssignmentParam.getP_Salary_Scale_ID());
                if(currentAssignmentParam.getP_Schedule_ID() > 0) newAssignmentParam.setP_Schedule_ID(currentAssignmentParam.getP_Schedule_ID());
                newAssignmentParam.setWeekly_Hours(currentAssignmentParam.getWeekly_Hours());


                if ( rs.getBigDecimal("Annual_Salary") != null )
                	newAssignmentParam.setAnnual_Salary(rs.getBigDecimal("Annual_Salary"));
                else
                	newAssignmentParam.setAnnual_Salary(rs.getBigDecimal("Hourly_Rate").multiply( newAssignmentParam.getWeekly_Hours()).multiply( new BigDecimal(52.18)).setScale(2,BigDecimal.ROUND_HALF_UP)  );

                P_Assignment Assignment = P_Assignment.get( Env.getCtx(), currentAssignmentParam.getP_Assignment_ID(), null);
            	BigDecimal Annual_Increase = null;
        		Annual_Increase = P_Payment_Group.getAnnual_Increase( Env.getCtx(), Assignment.getP_Employee_ID(), null);

                if ( rs.getBigDecimal("Hourly_Rate") != null )
                	newAssignmentParam.setHourly_Rate(rs.getBigDecimal("Hourly_Rate"));
                else
                	newAssignmentParam.setHourly_Rate(rs.getBigDecimal("Annual_Salary").divide( Annual_Increase ,8,BigDecimal.ROUND_HALF_UP).divide(newAssignmentParam.getWeekly_Hours(),8,BigDecimal.ROUND_HALF_UP ));
//                	newAssignmentParam.setHourly_Rate(rs.getBigDecimal("Annual_Salary").divide(new BigDecimal( 52.18 ),8,BigDecimal.ROUND_HALF_UP).divide(newAssignmentParam.getWeekly_Hours(),8,BigDecimal.ROUND_HALF_UP ));
                
                newAssignmentParam.save();
                
                // On effectue d'abord l'avancement pour chacune des affectations
                P_Assignment assignment = new P_Assignment(Env.getCtx(), rs.getInt("P_Assignment_ID"), null);
                assignment.setLastAdvanceDate(rs.getTimestamp("NextAdvanceDate"));
                
                // On calcule maintenant la date du prochain avancement
//                assignment.setExpectedAdvanceDate(this.findExpectedAdvanceDate(rs.getInt("P_Assignment_ID")));
                assignment.save();
                
        		P_Employee Employee = P_Employee.get( Env.getCtx(), rs.getInt("P_Employee_ID"), null);
        		Employee.setExpectedAdvanceDate(rs.getTimestamp("NextAdvanceDate"));
        		Employee.save();
                
            }
            
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            log.log(Level.SEVERE, "doIt", e);
            return Msg.translate(Env.getLanguage(Env.getCtx()), "ProcessFailed");
        }
        
        DB.executeUpdate("delete from P_Employee_Scale_Advance where IsReady = 'Y'", null);
        
        return Msg.translate(Env.getLanguage(Env.getCtx()), "Success");
    }
    
    /**
     * Cette méthode retourne l'affectation courrante
     */
    private P_Assignment_Param getCurrentAssignmentParam(int assignmentId)
    {

        int id = -1;
        String sql
        = "select top 1 P_Assignment_Param_ID"
            + " from P_Assignment_Param"
            + " where P_Assignment_ID = " + assignmentId
            + " and IsActive = 'Y'"
            + " and EffectIn <= " + DB.TO_DATE(Period.getStartDate())
//            + " and EffectIn <= (select StartDate from P_Period where PeriodStatus = 'O')"
            + " order by EffectIn desc";
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
                id = rs.getInt("P_Assignment_Param_ID");
            }
            
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            log.log(Level.SEVERE, "getCurrentAssignmentParam", e);
        }
        
        return new P_Assignment_Param(Env.getCtx(), id, null);
    }
    
    /**
     * Cette méthode retourne les P_Assignment_Param_ID futures selon le P_Assignment_Param.EffectIn
     * et la date de début de la période courrante
     */
    private Enumeration getFutureAssignment(int assignmentId)
    {
        Enumeration assignmentParamIds = null;
        String sql
        = "select P_Assignment_Param_ID"
            + " from P_Assignment_Param"
            + " where P_Assignment_ID = " + assignmentId
            + " and IsActive = 'Y'"
            + " and EffectIn > " + DB.TO_DATE(Period.getStartDate());
//            + " and EffectIn > (select StartDate from P_Period where PeriodStatus = 'O')";
        
        try
        {
            Vector<Integer> vector = new Vector<Integer>();
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next())
            {
                vector.add(new Integer(rs.getInt("P_Assignment_Param_ID")));
            }
            
            rs.close();
            stmt.close();
            
            if(vector.size() > 0)
                assignmentParamIds = vector.elements();
        }
        catch (SQLException e)
        {
            log.log(Level.SEVERE, "getFutureAssignment", e);
        }
        
        return assignmentParamIds;
    }
    
    /**
     * Trouve la date de début de la période courrante
     */
    private Timestamp currentPeriodStartDate() throws SQLException
    {
        Timestamp startDate = null;
        startDate = Period.getStartDate();
        return startDate;
    }
    
    /**
     * Calcule la prochaine date d'avancement
     */
    private Timestamp findExpectedAdvanceDate(int assignmentId)
    {
        Timestamp expectedAdvanceDate = null;
        
        // On récupère la liste des affectations dont la date du prochain avancement
        // est encore à null
        String sql
        = "select top 1 assignment.P_Assignment_ID, assignmentParam.P_Collective_Labour_Agr_ID, assignmentParam.P_Salary_Scale_ID,"
        	+ " employee.P_Job_Type_ID, salaryScaleDetail.Step, assignment.LastAdvanceDate"
        	+ " from ((P_Assignment assignment inner join P_Assignment_Param assignmentParam"
        	+ " on assignment.P_Assignment_ID = assignmentParam.P_Assignment_ID) inner join P_Employee employee"
        	+ " on assignment.P_Employee_ID = employee.P_Employee_ID) inner join P_Salary_Scale_Detail salaryScaleDetail"
        	+ " on assignmentParam.P_Salary_Scale_Detail_ID = salaryScaleDetail.P_Salary_Scale_Detail_ID"
        	+ " where assignment.P_Assignment_ID = " + assignmentId
        	+ " and assignment.IsActive = 'Y'"
        	+ " and employee.IsActive = 'Y'"
        	+ " and assignmentParam.IsActive = 'Y'"
        	+ " and assignmentParam.EffectIn <= ?"
        	+ " order by assignmentParam.EffectIn desc";
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            stmt.setTimestamp(1, this.currentPeriodStartDate());
            ResultSet rs = stmt.executeQuery();
            String specificDate1 = "05/01";
            String specificDate2 = "11/01";

            if(rs.next())
            {
                // on vérifie si l'employé n'a pas déjà atteint le maximum de son échellon
                if(!this.isOnTop(rs.getInt("P_Salary_Scale_ID"), rs.getString("Step")))
                {
                    BigDecimal interval = null;
                    String intervalType = null;
                    int step = Integer.parseInt(rs.getString("Step"));
                    int ruleId = 0;
                    int recurrenceCount = 0;
                    int P_Credits_ID = 0;

                    
                    // On sort la liste des règles pouvant s'appliquer prochainement
/*                    
                    sql = "select P_Scale_Advance_ID, Interval, IntervalType, Recurrence"
                        + " from P_Scale_Advance"
                        + " where P_Collective_Labour_Agr_ID = " + rs.getInt("P_Collective_Labour_Agr_ID")
                        + " and P_Job_Type_ID = " + rs.getInt("P_Job_Type_ID")
                        + " order by RuleIndex";
*/
                    sql = "select P_Scale_Advance_ID, Interval, IntervalType, Recurrence, specificDate1, specificDate2, P_Credits_ID"
                        + " from P_Scale_Advance"
                        + " where P_Collective_Labour_Agr_ID = " + rs.getInt("P_Collective_Labour_Agr_ID")
                        + " and isnull(P_Job_Type_ID, 0) in (0, " + rs.getInt("P_Job_Type_ID") + ")"
                        + " order by RuleIndex";

                    
                    PreparedStatement s = DB.prepareStatement(sql, null);
                    ResultSet r = s.executeQuery();
                    
                    while(r.next() && ruleId == 0)
                    {
                        // On cherche maintenant la règle qui s'applique en observant
                        // le nombre de récurrence par rapport au numéro de l'échellon
                        if(r.getInt("Recurrence") == 0 || (recurrenceCount += r.getInt("Recurrence")) > step)
                        {
                            ruleId = r.getInt("P_Scale_Advance_ID");
                            interval = r.getBigDecimal("Interval");
                            intervalType = r.getString("IntervalType");
                            specificDate1 = r.getString("specificDate1");
                            specificDate2 = r.getString("specificDate2");
                            P_Credits_ID  = r.getInt("P_Credits_ID");
                        }
                    }
                    
                    r.close();
                    s.close();
                    
                    // Si on a trouvé une règle appliquable, on prépare le calcul
                    // de la date du prochain avancement
                    if(ruleId > 0)
                    {
                        // On vérifie si on utilise un interval
                        if(interval != null)
                        {
                            // On utilise la date du dernier avancement pour calculer la nouvelle
                            Calendar calendar = GregorianCalendar.getInstance();
                            if ( rs.getTimestamp("LastAdvanceDate") != null )
                            	calendar.setTimeInMillis(rs.getTimestamp("LastAdvanceDate").getTime());
                            else calendar.setTimeInMillis( Period.getStartDate().getTime());
                            if(intervalType.equals("D"))
                            {
                                calendar = null;
//                                calendar.add(Calendar.DAY_OF_MONTH, interval.intValue());
                            }
                            else if(intervalType.equals("M"))
                            {
                                calendar.add(Calendar.MONTH, interval.intValue());
                            }
                            // 1 fois par année ( Date début période )
                            else if(intervalType.equals("Z"))
                            {
                                calendar.setTimeInMillis(this.currentPeriodStartDate().getTime());
                            	calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR)+1);
                            }
                            else
                            {
                                calendar.add(Calendar.YEAR, interval.intValue());
                            }
                            expectedAdvanceDate = calendar == null ? null : new Timestamp(calendar.getTimeInMillis());
                        }
                        else
                        {
                            // On vérifie dans la table P_Scale_Advance_Specific_Date
                            // pour savoir quelle est la prochaine date d'avancement
                            // On vérifie dans la table P_Scale_Advance_Specific_Date
                            // pour savoir quelle est la prochaine date d'avancement
//                        	if ( _expectedAdvanceDate != null)
//                        		expectedAdvanceDate = nextDate(ruleId, _expectedAdvanceDate, specificDate1, specificDate2, intervalType );
//                        	else	
                        		expectedAdvanceDate = nextDate(ruleId, rs.getTimestamp("LastAdvanceDate"), specificDate1, specificDate2, intervalType );
//                            expectedAdvanceDate = nextDate(ruleId, rs.getTimestamp("LastAdvanceDate"));
                        }
                    }
                }
            }
            
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            log.log(Level.SEVERE, "findExpectedAdvanceDate", e);
        }
        return expectedAdvanceDate;
    }
 
    /**
     * Indique s'il s'agit du maximum de l'échelle
     */
    private boolean isOnTop(int salaryScaleId, String step)
    {
        boolean isOnTop = false;
        String sql
        = "select 1"
            + " from P_Scale_Advance_MaxByClass"
            + " where exists ("
            + "   select 1"
            + "   from P_Salary_Scale"
            + "   where P_Salary_Scale_ID = " + salaryScaleId
            + "   and P_Collective_Labour_Agr_ID = P_Scale_Advance_MaxByClass.P_Collective_Labour_Agr_ID"
            + "   and P_Salary_Class_ID = isnull(P_Scale_Advance_MaxByClass.P_Salary_Class_ID, P_Salary_Class_ID)"
            + " )"
            + " and MaximumStep > '" + step + "'";
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            isOnTop = !rs.next();
            
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            log.log(Level.SEVERE, "isOnTop", e);
        }
        
        return isOnTop;
    }
    

    /**
     * Vérifie dans la table P_Scale_Advance_Specific_Date quelle est
     * la date du prochain avancement
     * @throws SQLException 
     */
    private Timestamp nextDate(int ruleId, Timestamp lastAdvanceDate, String specificDate1, String specificDate2, String intervalType) throws SQLException
    {
        Timestamp date = null;
        Calendar lastCalendar = GregorianCalendar.getInstance();
        lastCalendar.setTimeInMillis(lastAdvanceDate.getTime());
        Timestamp first = null;
        
        // Si la date récupérée et transportée dans l'année de la
        // date du dernier avancement est plus grande que la date
        // du dernier avancement, on a trouvé la date du prochain
        // avancement
        Calendar calendar = GregorianCalendar.getInstance();
        P_Year Year = P_Year.get( Env.getCtx(), Period.getP_Year_ID(), null);
        calendar.set(Calendar.YEAR, Year.getYear()  );
        calendar.set(Calendar.MONTH, new Integer( specificDate1.substring(0,2)).intValue()-1 );
        calendar.set(Calendar.DAY_OF_MONTH, new Integer( specificDate1.substring(3,5)).intValue() );

        first = new Timestamp( calendar.getTimeInMillis());

        if(calendar.getTimeInMillis() > lastCalendar.getTimeInMillis() && calendar.getTimeInMillis() > this.currentPeriodStartDate().getTime() )
        {
            date = new Timestamp( calendar.getTimeInMillis());
        }
        else
        {
            calendar.set(Calendar.YEAR, Year.getYear() );
            calendar.set(Calendar.MONTH, new Integer( specificDate2.substring(0,2)).intValue()-1 );
            calendar.set(Calendar.DAY_OF_MONTH, new Integer( specificDate2.substring(3,5)).intValue() );

            if(calendar.getTimeInMillis() > lastCalendar.getTimeInMillis() && calendar.getTimeInMillis() > this.currentPeriodStartDate().getTime())
                date = new Timestamp( calendar.getTimeInMillis());
        }
 
        
        // Si aucune date était plus grande que la date du dernier
        // avancement dans l'année de celle-ci, on utilise
        // la première date de l'année pour l'an prochain
        if(date == null && first != null)
        {
        	calendar = GregorianCalendar.getInstance();
        	calendar.setTimeInMillis(first.getTime());
        	calendar.set(Calendar.YEAR, lastCalendar.get(Calendar.YEAR) +1 );
        	date = new Timestamp( calendar.getTimeInMillis());
        }
        
        if ( intervalType.equals("F") )
        	return this.getPeriodStartDate( date );
        else
        	return date;
    }
    
    // 
    // retourne la date de début de période en fonction de la date d'avancement sélectionner
    private Timestamp getPeriodStartDate(  Timestamp dateAdvance )
    {
        Timestamp startDate = null;
        String sql = "select Max(StartDate) StartDate from P_Period where Startdate <= " + DB.TO_DATE( dateAdvance )
                   + " and P_Frequency_ID = " + Period.getP_Frequency_ID() ;
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
                startDate = rs.getTimestamp("StartDate");
            }
            
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            log.log(Level.SEVERE, "getCurrentPeriodStartDate", e);
        }
        
        return startDate;
    }


/*
    private Timestamp nextDate(int ruleId, Timestamp lastAdvanceDate)
    {
        Timestamp date = null;
        Calendar lastCalendar = GregorianCalendar.getInstance();
        lastCalendar.setTimeInMillis(lastAdvanceDate.getTime());
        Timestamp first = null;
        
        // On récupère d'abord la liste de date en ordre
        String sql
        = "select SpecificDate"
            + " from P_Scale_Advance_Specific_Date"
            + " where P_Scale_Advance_ID = " + ruleId
            + " order by SpecificDate";
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next() && date == null)
            {
                if(first == null)
                    first = rs.getTimestamp("SpecificDate");
                
                // Si la date récupérée et transportée dans l'année de la
                // date du dernier avancement est plus grande que la date
                // du dernier avancement, on a trouvé la date du prochain
                // avancement
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.setTimeInMillis(rs.getTimestamp("SpecificDate").getTime());
                calendar.set(Calendar.YEAR, lastCalendar.get(Calendar.YEAR));
                if(calendar.getTimeInMillis() > lastCalendar.getTimeInMillis())
                    date = rs.getTimestamp("SpecificDate");
            }
            
            stmt.close();
            rs.close();
            
            // Si aucune date était plus grande que la date du dernier
            // avancement dans l'année de celle-ci, on utilise
            // la première date de l'année pour l'an prochain
            if(date == null && first != null)
            {
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.setTimeInMillis(first.getTime());
                calendar.set(Calendar.YEAR, lastCalendar.get(Calendar.YEAR) + 1);
            }
        }
        catch (SQLException e)
        {
            log.log(Level.SEVERE, "nextDate", e);
        }
        return date;
    }
    */
}
