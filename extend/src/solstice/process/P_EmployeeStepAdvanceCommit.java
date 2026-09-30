/*
 * Created on Oct 23, 2006
 *
 * Mise à jour de la table de Paramètre de l'assignation.
 * Window - Preferences - Java - Code Style - Code Templates
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

import org.bouncycastle.cms.CMSEnvelopedDataGenerator;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import com.crystaldecisions.reports.common.engine.Engine;

import solstice.model.P_Assignment;
import solstice.model.P_Assignment_Param;
import solstice.model.P_Employee;
import solstice.model.P_Payment_Group;
import solstice.model.P_Period;
import solstice.model.P_Year;
import solstice.model.P_Scale_Advance;
import solstice.model.P_Salary_Scale_Detail;

/**
 * @author rejgar01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_EmployeeStepAdvanceCommit extends SvrProcess
{
    private P_Period Period = null;
    
	public P_EmployeeStepAdvanceCommit()
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
        // On lit les enregistrements de la table P_Employee_Scale_Advance
        String sql
        = "SELECT employeeScaleAdvance.P_Assignment_ID,"
        	 + " salaryScaleDetail.Annual_Salary,"
        	 + " salaryScaleDetail.Hourly_Rate, "
        	 + " P_Salary_Scale.Remuneration_Method, "
        	 + " employeeScaleAdvance.NextScale_ID, employeeScaleAdvance.LastAdvanceDate, employeeScaleAdvance.NextAdvanceDate, employeeScaleAdvance.EffectIn"
        	 + ", employeeScaleAdvance.P_Employee_ID "
        	 + " FROM P_Employee_Scale_Advance employeeScaleAdvance," 
        	 + "      P_Salary_Scale_Detail salaryScaleDetail,"
        	 + "      P_Salary_Scale,"
        	 + "      P_Assignment assignment,"
        	 + "      P_Assignment_Param  assignmentParam"
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
/*                // On doit d'abord vérifier s'il existe une ou plusieurs affectations
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
*/                
                // On crée ensuite une affectation_param pour l'avancement d'échellon qui prend
                // effet maintenant
                P_Assignment_Param newAssignmentParam = new P_Assignment_Param(Env.getCtx(), -1, null);
                P_Assignment_Param currentAssignmentParam = getCurrentAssignmentParam(rs.getInt("P_Assignment_ID"));
                
//                currentAssignmentParam.setEffectTo( Period.getStartDate());
//                currentAssignmentParam.save();
                
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
                if ( currentAssignmentParam.getP_Collective_Labour_Agr_ID() != 0) 
                	newAssignmentParam.setP_Collective_Labour_Agr_ID( currentAssignmentParam.getP_Collective_Labour_Agr_ID());
                else
                {
                	P_Employee Employee = P_Employee.get( Env.getCtx(), rs.getInt("P_Employee_ID"), null);
                	newAssignmentParam.setP_Collective_Labour_Agr_ID( Employee.getP_Collective_Labour_Agr_ID());
                	
                }
                
                if(currentAssignmentParam.getP_Job_Title_ID() > 0) newAssignmentParam.setP_Job_Title_ID(currentAssignmentParam.getP_Job_Title_ID());
                if(currentAssignmentParam.getP_Occupation_Group_ID() > 0) newAssignmentParam.setP_Occupation_Group_ID(currentAssignmentParam.getP_Occupation_Group_ID());
                if(currentAssignmentParam.getP_Salary_Class_ID() > 0) newAssignmentParam.setP_Salary_Class_ID(currentAssignmentParam.getP_Salary_Class_ID());
                if(currentAssignmentParam.getP_Salary_Scale_ID() > 0) newAssignmentParam.setP_Salary_Scale_ID(currentAssignmentParam.getP_Salary_Scale_ID());
                if(currentAssignmentParam.getP_Schedule_ID() > 0) newAssignmentParam.setP_Schedule_ID(currentAssignmentParam.getP_Schedule_ID());
                newAssignmentParam.setWeekly_Hours(currentAssignmentParam.getWeekly_Hours());


                P_Assignment Assignment = P_Assignment.get( Env.getCtx(), currentAssignmentParam.getP_Assignment_ID(), null);
            	BigDecimal Annual_Increase = null;
        		Annual_Increase = P_Payment_Group.getAnnual_Increase( Env.getCtx(), Assignment.getP_Employee_ID(), null);

                if ( rs.getBigDecimal("Annual_Salary") != null )
                	newAssignmentParam.setAnnual_Salary(rs.getBigDecimal("Annual_Salary"));
                else
                	newAssignmentParam.setAnnual_Salary(rs.getBigDecimal("Hourly_Rate").multiply( newAssignmentParam.getWeekly_Hours()).multiply( Annual_Increase ).setScale(2,BigDecimal.ROUND_HALF_UP)  );


                if ( rs.getBigDecimal("Hourly_Rate") != null )
                	newAssignmentParam.setHourly_Rate(rs.getBigDecimal("Hourly_Rate"));
                else
                	newAssignmentParam.setHourly_Rate(rs.getBigDecimal("Annual_Salary").divide( Annual_Increase ,8,BigDecimal.ROUND_HALF_UP).divide(newAssignmentParam.getWeekly_Hours(),8,BigDecimal.ROUND_HALF_UP ));
//	            	newAssignmentParam.setHourly_Rate(rs.getBigDecimal("Annual_Salary").divide(new BigDecimal( 52.18 ),8,BigDecimal.ROUND_HALF_UP).divide(newAssignmentParam.getWeekly_Hours(),8,BigDecimal.ROUND_HALF_UP ));
                
                newAssignmentParam.save();
                // On effectue d'abord l'avancement pour chacune des affectations
                P_Assignment assignment = new P_Assignment(Env.getCtx(), rs.getInt("P_Assignment_ID"), null);
                assignment.setLastAdvanceDate(rs.getTimestamp("EffectIn"));
                
                // On calcule maintenant la date du prochain avancement
                //assignment.setExpectedAdvanceDate(this.findExpectedAdvanceDate(rs.getInt("P_Assignment_ID"), rs.getTimestamp("NextAdvanceDate"), rs.getInt("NextScale_ID")));
//2024-06-25                assignment.setExpectedAdvanceDate(rs.getTimestamp("NextAdvanceDate"));
                assignment.save();
                
        		P_Employee Employee = P_Employee.get( Env.getCtx(), rs.getInt("P_Employee_ID"), null);
        		Employee.setExpectedAdvanceDate(rs.getTimestamp("NextAdvanceDate"));
        		Employee.save();
                
            }
            
            rs.close();
            stmt.close();
        }
        catch ( Exception e)
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
            + " and ( EffectTo is null or EffectTo >= " + DB.TO_DATE(Period.getStartDate()) + ") "
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
/*    
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
   */ 

}
