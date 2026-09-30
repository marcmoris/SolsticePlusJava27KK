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
import java.util.GregorianCalendar;
import java.util.logging.Level;

import org.compiere.model.MRole;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import solstice.model.P_Assignment;
import solstice.model.P_Employee;
import solstice.model.P_Period;
import solstice.model.P_Year;
import solstice.model.P_Employee_Scale_Advance;

/** 
 * @author frafor01
 * Ce processus sert à remplir la table P_Employee_Scale_Advance
 */
public class P_FillScaleAdvanceTableOld extends SvrProcess
{
    public P_FillScaleAdvanceTableOld()
    {
        super();
    }

    private int Period_ID = 0;
    private P_Period Period = null;
    
    /**
     * Durant le traitement de la procédure, on utilise le champ P_Assignment.ExpectedAdvanceDate. On
     * doit s'assurer ici que ce champ est populé. La première fois que la procédure est lancée, le
     * traitement est plus long, mais à partir du moment qu'une majorité 
     */
    protected void prepare()
    {
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

        // On récupère la liste des affectations dont la date du prochain avancement
        // est encore à null
        //P_Period Period = P_Period.getOpenPeriod( Env.getCtx(), null);
		Period = P_Period.get( Env.getCtx(), Period_ID, null);

		String sql = "Select P_Employee.P_Employee_ID, P_Employee.Value, " 
 		   + "       P_Assignment.P_Assignment_ID, " 
 		   + "       P_Assignment_Param.P_Assignment_Param_ID, " 
 		   + "       P_Employee.P_Job_Type_ID, " 
 		   + "       P_Assignment_Param.P_Collective_Labour_Agr_ID, " 
 		   + "       P_Assignment_Param.P_Salary_Scale_ID, " 
 		   + "       P_Assignment_Param.P_Salary_Scale_Detail_ID, "
 		   + "       scaleDetail.Step, "
 		   + " isnull( P_Assignment.LastAdvanceDate, isnull( P_Employee.reactivation, P_Employee.DateHired)) as LastAdvanceDate, "
  		   + "       ExpectedAdvanceDate, " 
 		   + "       P_Assignment_Param.P_Job_Title_ID "
           + "  From P_Employee, P_Assignment, P_Assignment_Param, P_Salary_Scale_Detail scaleDetail " 
 	       + "  WHERE P_Assignment.IsActive='Y' " 
 	       + "    AND P_Employee.IsActive='Y' "
 	       + "    AND P_Assignment_Param.IsActive='Y' " 
 	       + "    AND P_Assignment_Param.P_Assignment_ID = P_Assignment.P_Assignment_ID "
 	       + "    AND P_Assignment.P_Employee_ID= P_Employee.P_Employee_ID " 
           + "    AND P_Assignment_Param.EffectIn <=  " + DB.TO_DATE( Period.getStartDate() )
           + "    AND ( P_Assignment_Param.EffectTo is null or P_Assignment_Param.EffectTo >= " + DB.TO_DATE( Period.getStartDate() ) + " ) "
 	       + "    AND " + DB.TO_DATE( Period.getStartDate() ) + " between isnull( P_Assignment.startdate, " + DB.TO_DATE( Period.getStartDate() ) + " ) and isnull( P_Assignment.enddate, " + DB.TO_DATE( Period.getEndDate() ) + "  ) "
 	       + "    AND P_Assignment_Param.P_Salary_Scale_Detail_ID = scaleDetail.P_Salary_Scale_Detail_ID "
//Date du prochaine avancement est dans les dates de périodes
           + "    AND ( P_Assignment.ExpectedAdvanceDate is null  "
//// PATCH, On réévalue la date du prochaine avancement si elle est entres les dates de périodes / Mois et Jours dans l'année. 
//	       + "         OR datepart( m,P_Assignment.ExpectedAdvanceDate)*100+datepart( d,P_Assignment.ExpectedAdvanceDate) Between datepart( m," + DB.TO_DATE( Period.getStartDate() ) + ")*100 + datepart( d," + DB.TO_DATE( Period.getStartDate() ) + ") AND datepart( m," + DB.TO_DATE( Period.getEndDate() )+")*100 + datepart( d," + DB.TO_DATE( Period.getEndDate() )+"))"
	       + "         OR P_Assignment.ExpectedAdvanceDate <= " + DB.TO_DATE( Period.getEndDate() )
	       + "         OR P_Assignment.ExpectedAdvanceDate <= P_Assignment.LastAdvanceDate"+")"
	       
//Et qu'il y a un echelon supérieur.        	       
            + "    AND exists( select top 1 P_Salary_Scale_Detail_ID "   
            + "                  from P_Salary_Scale_Detail "
            + "                  where P_Salary_Scale_Detail.P_Salary_Scale_ID = P_Assignment_Param.P_Salary_Scale_ID "   
            + "                     and P_Salary_Scale_Detail.Step > scaleDetail.Step ) "
 	       + " ORDER BY P_Assignment.StartDate Desc, P_Assignment.AssignmentType "
            ;

		sql = MRole.getDefault().addAccessSQL (sql, "P_Employee", true, false);	// fully qualidfied - RO 

        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            // Pour chaque assignation,
            while(rs.next())
            {
            	Timestamp expectedAdvanceDate = getExpectedAdvanceDate( rs , null );
            	//
            	// Si la date du prochaine avancement d'échelon est plus petite que la période on recommance.
            	//
            	if ( expectedAdvanceDate != null )
            	{
                	while ( expectedAdvanceDate.compareTo(Period.getStartDate()) < 0  )
                	{
                		expectedAdvanceDate = getExpectedAdvanceDate( rs, expectedAdvanceDate );
                	}

                	if ( expectedAdvanceDate.compareTo(rs.getTimestamp("LastAdvanceDate")) <= 0  )
                	{
                		expectedAdvanceDate = getExpectedAdvanceDate( rs, expectedAdvanceDate );
                	}
                		
                	
                	// si la date d'avancement d'échelon est plus petit que la date de fin de période.
//                	if ( expectedAdvanceDate.compareTo(Period.getEndDate()) < 0  )
//                	{
/*    //2024-06-25            	
                        // On sauvegarde maintenant la modification
                        P_Assignment assignment = new P_Assignment(Env.getCtx(), rs.getInt("P_Assignment_ID"), null);
                        assignment.setExpectedAdvanceDate(expectedAdvanceDate);
                        assignment.save();
*/
                		P_Employee Employee = P_Employee.get( Env.getCtx(), rs.getInt("Employee_ID"), null);
                		Employee.setExpectedAdvanceDate(expectedAdvanceDate);
                		Employee.save();
//                	}
            	}
            }
            
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            log.log(Level.SEVERE, "prepare", e);
        }
        
    }

    private Timestamp getExpectedAdvanceDate( ResultSet rs, Timestamp _expectedAdvanceDate  ) throws NumberFormatException, SQLException
    {
        // on vérifie si l'employé n'a pas déjà atteint le maximum de son échellon
        if(!this.isOnTop(rs.getInt("P_Salary_Scale_ID"), rs.getString("Step")))
        {
            BigDecimal interval = null;
            String intervalType = null;
            int step = Integer.parseInt(rs.getString("Step"));
            int ruleId = 0;
            int recurrenceCount = 0;
            String specificDate1 = "05/01";
            String specificDate2 = "11/01";
            int P_Credits_ID = 0;
            
            // On sort la liste des règles pouvant s'appliquer prochainement
            String sql = "select P_Scale_Advance_ID, Interval, IntervalType, Recurrence, specificDate1, specificDate2, P_Credits_ID"
                + " from P_Scale_Advance"
                + " where P_Collective_Labour_Agr_ID = " + rs.getInt("P_Collective_Labour_Agr_ID")
                + " and isnull(P_Job_Type_ID, 0) in (0, " + rs.getInt("P_Job_Type_ID") + ")"
                + " order by RuleIndex";
            
            PreparedStatement s = DB.prepareStatement(sql, null);
            ResultSet r = s.executeQuery();
            
            while(r.next() )  // && ruleId == 0
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
                Timestamp expectedAdvanceDate = null;
                
                // On vérifie si on utilise un interval
                // E - 2 fois par année ( Date fixe )
                // F - 2 fois par année ( Date début période )
                if(! intervalType.equals("E") && ! intervalType.equals("F")  )
                {
                    // On utilise la date du dernier avancement pour calculer la nouvelle
                    Calendar calendar = GregorianCalendar.getInstance();
                    if ( _expectedAdvanceDate != null)
                    	calendar.setTimeInMillis( _expectedAdvanceDate.getTime() );
                    else
                    	calendar.setTimeInMillis(rs.getTimestamp("LastAdvanceDate").getTime());
                    P_Year Year = P_Year.get( Env.getCtx(), Period.getP_Year_ID(), null);
                    calendar.set(Calendar.YEAR, Year.getYear() );
                    // Selon nombre de jours dans une banque
                    if(intervalType.equals("D"))
                    {
                        // Dans le cas des jours, on doit aller vérifier dans la
                        // banque des jours travaillés de l'employé
                        int remainingDays = getRemainingDays(rs.getInt("P_Assignment_ID"), interval.intValue(), P_Credits_ID);
//                        if(remainingDays < 14)
//                        {
                            calendar.setTimeInMillis(this.getCurrentPeriodStartDate().getTime());
                            calendar.add(Calendar.DAY_OF_MONTH, remainingDays);
//                        }
                    }
                    // 1 fois par mois	
                    else if(intervalType.equals("M"))
                    {
                        calendar.add(Calendar.MONTH, interval.intValue());
                    }
                    // 1 fois par année ( Date début période )
                    else if(intervalType.equals("Z"))
                    {
                        calendar.setTimeInMillis(this.getCurrentPeriodStartDate().getTime());
                    	calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR)+1);
                    }
                    // 1 fois par année
                    else
                    {
                    	Timestamp curExpectedDate = new Timestamp(calendar.getTimeInMillis());
                    	if ( curExpectedDate.compareTo(Period.getStartDate()) < 0 )
                    		calendar.add(Calendar.YEAR, interval.intValue());
                    }
                    expectedAdvanceDate = new Timestamp(calendar.getTimeInMillis());
                }
                else
                {
                    // On vérifie dans la table P_Scale_Advance_Specific_Date
                    // pour savoir quelle est la prochaine date d'avancement
                	if ( _expectedAdvanceDate != null)
                		expectedAdvanceDate = nextDate(ruleId, _expectedAdvanceDate, specificDate1, specificDate2, intervalType );
                	else	
                		expectedAdvanceDate = nextDate(ruleId, rs.getTimestamp("LastAdvanceDate"), specificDate1, specificDate2, intervalType );
                }
                
                
                // On sauvegarde maintenant la modification
                return  expectedAdvanceDate;
            }
        }
        return null;
    }
    
    /**
     * Cette méthode consulte la banque d'ancienneté d'un employé occasionnel (bnq 8002) et
     * renvoit le nombre de jours qu'il reste avant l'atteinte du minimumDays
     */
    private int getRemainingDays(int assignmentId, int minimumDays, int P_Credits_ID)
    {
        int workedDays = 0;
        String sql
        = "select isnull(sum(PMovementVariation), 0) as Jours"
            + " from P_Credits_Movement"
            + " where P_Credits_ID = " + P_Credits_ID // (select P_Credits_ID from P_Credits where Value = '8002')
            + " and PMovementDate <= (select StartDate from P_Period where P_Period_ID = " + Period.getP_Period_ID() + ")"
            + " and exists ("
            + "   select 1"
            + "   from P_Assignment"
            + "   where P_Assignment_ID = " + assignmentId
            + "   and LastAdvanceDate <= P_Credits_Movement.PMovementDate"
            + "   and P_Employee_ID = P_Credits_Movement.P_Employee_ID"
            + " )";
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
                workedDays = rs.getInt("Jours");
            }
            
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            log.log(Level.SEVERE, "getRemainingDays", e);
        }
        
        return minimumDays - workedDays;
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
     */
    private Timestamp nextDate(int ruleId, Timestamp lastAdvanceDate, String specificDate1, String specificDate2, String intervalType)
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

        if(calendar.getTimeInMillis() > lastCalendar.getTimeInMillis() && calendar.getTimeInMillis() > getCurrentPeriodStartDate().getTime() )
        {
            date = new Timestamp( calendar.getTimeInMillis());
        }
        else
        {
            calendar.set(Calendar.YEAR, Year.getYear() );
            calendar.set(Calendar.MONTH, new Integer( specificDate2.substring(0,2)).intValue()-1 );
            calendar.set(Calendar.DAY_OF_MONTH, new Integer( specificDate2.substring(3,5)).intValue() );

            if(calendar.getTimeInMillis() > lastCalendar.getTimeInMillis() && calendar.getTimeInMillis() > getCurrentPeriodStartDate().getTime())
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
        	return getPeriodStartDate( date );
        else
        	return date;
    }

    /**
     * On doit sortir la liste des employés qui peuvent monter
     * d'échellon et les ajouter dans la table P_Employee_Scale_Advance
     */
    protected String doIt() throws Exception
    {
    	
        DB.executeUpdate("delete from P_Employee_Scale_Advance WHERE IsReady ='Y' ", null);
        
//        P_Period Period = P_Period.getOpenPeriod( Env.getCtx(), null);
		Period = P_Period.get( Env.getCtx(), Period_ID, null);

        // On sort la liste des employés qui sont pret à avancer d'échellon
       
        String sql = "Select P_Assignment.P_Employee_ID, " 
        		   + "       P_Assignment.P_Assignment_ID, " 
        		   + "       P_Assignment_Param.P_Assignment_Param_ID, " 
        		   + "       P_Employee.P_Job_Type_ID, " 
        		   + "       P_Assignment_Param.P_Collective_Labour_Agr_ID, " 
        		   + "       LastAdvanceDate, " 
        		   + "       P_Assignment_Param.P_Salary_Scale_Detail_ID, " 
        		   + "       ExpectedAdvanceDate, " 
        		   + "       P_Assignment_Param.P_Job_Title_ID, "
// CurrentRate         		   
                   + " isnull(scaleDetail.Annual_Salary, scaleDetail.Hourly_Rate) as CurrentRate, "
// ExpectedRate                   
                   + " isnull(( select top 1 isnull(Annual_Salary, Hourly_Rate)"
                   + "   from P_Salary_Scale_Detail"
                   + "   where P_Salary_Scale_ID = P_Assignment_Param.P_Salary_Scale_ID"
                   + "   and Step > scaleDetail.Step"
                   + "   order by Step"
                   + " ), 0) as ExpectedRate, "
// NextScale_ID                   
                   + " ( select top 1 P_Salary_Scale_Detail_ID"
                   + "   from P_Salary_Scale_Detail"
                   + "   where P_Salary_Scale_ID = P_Assignment_Param.P_Salary_Scale_ID"
                   + "   and Step > scaleDetail.Step"
                   + "   order by Step"
                   + " ) as NextScale_ID, "
                   + "   P_Assignment_Param.P_Salary_Class_ID "
                   
                   + "  From P_Employee, P_Assignment, P_Assignment_Param, P_Salary_Scale_Detail scaleDetail " 
        	       + "  WHERE P_Assignment.IsActive='Y' " 
        	       + "    AND P_Employee.IsActive='Y' "
        	       + "    AND P_Assignment_Param.IsActive='Y' " 
        	       + "    AND P_Assignment_Param.P_Assignment_ID = P_Assignment.P_Assignment_ID "
        	       + "    AND P_Assignment.P_Employee_ID= P_Employee.P_Employee_ID " 
                   + "    AND P_Assignment_Param.EffectIn <=  " + DB.TO_DATE( Period.getStartDate() )
                   + "    AND ( P_Assignment_Param.EffectTo is null or P_Assignment_Param.EffectTo >= " + DB.TO_DATE( Period.getStartDate() ) + " ) "
        	       + "    AND " + DB.TO_DATE( Period.getStartDate() ) + " between isnull( P_Assignment.startdate, " + DB.TO_DATE( Period.getStartDate() ) + " ) and isnull( P_Assignment.enddate, " + DB.TO_DATE( Period.getEndDate() ) + "  ) "
        	       + "    AND P_Assignment_Param.P_Salary_Scale_Detail_ID = scaleDetail.P_Salary_Scale_Detail_ID "
// Date du prochaine avancement est entres les dates de périodes / Mois et Jours dans l'année.
        	       + "    AND datepart( m,P_Assignment.ExpectedAdvanceDate)*100+datepart( d,P_Assignment.ExpectedAdvanceDate) Between datepart( m," + DB.TO_DATE( Period.getStartDate() ) + ")*100 + datepart( d," + DB.TO_DATE( Period.getStartDate() ) + ") AND datepart( m," + DB.TO_DATE( Period.getEndDate() )+")*100 + datepart( d," + DB.TO_DATE( Period.getEndDate() )+") "
// Et qu'il y a un echelon supérieur.        	       
                   + "    AND exists( select top 1 P_Salary_Scale_Detail_ID "   
                   + "                  from P_Salary_Scale_Detail "
                   + "                  where P_Salary_Scale_Detail.P_Salary_Scale_ID = P_Assignment_Param.P_Salary_Scale_ID "   
                   + "                     and P_Salary_Scale_Detail.Step > scaleDetail.Step ) "
        	       + " ORDER BY P_Assignment.StartDate Desc, P_Assignment.AssignmentType "
                   ;
       
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next())
            {
            	if ( rs.getTimestamp("ExpectedAdvanceDate").compareTo(Period.getEndDate()) <= 0 )
            	{
                    // Pour chaque enregistrement, on ajoute une dans la
                    // table P_Employee_Scale_Advance
                    P_Employee_Scale_Advance employeeScaleAdvance = new P_Employee_Scale_Advance(Env.getCtx(), -1, null);
                    employeeScaleAdvance.setP_Assignment_ID(rs.getInt("P_Assignment_ID"));
                    employeeScaleAdvance.setP_Employee_ID(rs.getInt("P_Employee_ID"));
                    employeeScaleAdvance.setCurrentRate(rs.getBigDecimal("CurrentRate"));
                    employeeScaleAdvance.setExpectedRate(rs.getBigDecimal("ExpectedRate"));
                    employeeScaleAdvance.setNextScale_ID(rs.getInt("NextScale_ID"));
                    employeeScaleAdvance.setLastAdvanceDate( rs.getTimestamp("LastAdvanceDate"));
                    employeeScaleAdvance.setP_Collective_Labour_Agr_ID( rs.getInt("P_Collective_Labour_Agr_ID"));
                    employeeScaleAdvance.setP_Salary_Class_ID( rs.getInt("P_Salary_Class_ID"));

                    /*
                    Calendar calendar = GregorianCalendar.getInstance();
                    calendar.setTimeInMillis( rs.getTimestamp("ExpectedAdvanceDate").getTime());
                    P_Year Year = P_Year.get( Env.getCtx(), Period.getP_Year_ID(), null);
                    calendar.set(Calendar.YEAR, Year.getYear() );
                    new Timestamp( calendar.getTimeInMillis())
                    */
                    
                    employeeScaleAdvance.setNextAdvanceDate(rs.getTimestamp("ExpectedAdvanceDate"));
                    employeeScaleAdvance.setCurrentScale_ID( rs.getInt("P_Salary_Scale_Detail_ID"));
                    employeeScaleAdvance.setP_Job_Title_ID(rs.getInt("P_Job_Title_ID"));
                    employeeScaleAdvance.setP_Job_Type_ID(rs.getInt("P_Job_Type_ID")); 
                	employeeScaleAdvance.setEffectIn(rs.getTimestamp("ExpectedAdvanceDate"));
                    employeeScaleAdvance.save();
            		
            	}
            }
            
            rs.close();
            stmt.close();
        }
        catch(SQLException e)
        {
            return Msg.translate(Env.getLanguage(Env.getCtx()), "ProcessFailed");
        }
        
        return Msg.translate(Env.getLanguage(Env.getCtx()), "Success");
    }
    
    /**
     * Cette méthode retourne la date de début de la période courrante
     */
    private Timestamp getCurrentPeriodStartDate()
    {
        Timestamp startDate = null;
        String sql = "select StartDate from P_Period where P_Period_ID = " + Period.getP_Period_ID();
        
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

}
