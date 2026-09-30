package solstice.custom;
 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.P_ER_Competence;
import solstice.model.P_ER_Evaluation;
import solstice.model.P_ER_Expectation;
import solstice.model.P_ER_Meeting;
import solstice.process.PgiUtil;

/**
 * @author Francis
 * Gestion des formulaire d'évaluation de rendement
 */
public class EvaluationRendement extends ValidationBase
{
    /*
     * Static
     */
    String trxName = null;
    
    /**
     * Cette méthode crée la table de hachage contenant les informations
     * du formulaire courant.
     */
    public static Hashtable createMainForm(PageContext pageContext, String formType) throws Exception
    {
        // Un type de formulaire doit avoir été spécifié
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        int gestionnaireId = ((IUserInfo)request.getSession().getAttribute("userInfo")).getEmployeeId();
        if(formType != null && !formType.equals(""))
        {
            
            // Si la page vient d'être traité par la classe de validation,
            // elle contient déjà en attribut l'évaluation courante
            if(pageContext.getAttribute("evaluation") != null
                    && pageContext.getAttribute("evaluation") instanceof Hashtable)
            {
                return (Hashtable)pageContext.getAttribute("evaluation");
            }
            
            // Si la page est en consultation, on doit avoir posté un
            // numéro d'évaluation valide
            if(request.getParameter("evaluationId") != null
                    && !request.getParameter("evaluationId").equals(""))
            {
                pageContext.setAttribute("evaluationId", new Integer(Integer.parseInt(request.getParameter("evaluationId"))));
                return loadER(Integer.parseInt(request.getParameter("evaluationId")));
            }
            
            // Si la page est en création, on doit avoir posté un numéro
            // d'employé. On vérifie alors s'il existe un formulaire qui n'a
            // pas été fermé pour cet employé. Si oui, on le charge, sinon, on en
            // crée un nouveau
            if(request.getParameter("employeeId") != null
                    && !request.getParameter("employeeId").equals(""))
            {
                boolean isAdmin = false;
                isAdmin = ((IUserInfo)request.getSession().getAttribute("userInfo")).hasPolicy("eval_allEmployees");

                int employeeId = Integer.parseInt(request.getParameter("employeeId"));
                int evaluationId = getOpenedForm(employeeId, gestionnaireId, isAdmin);
                if(evaluationId == 0)
                {
                    pageContext.setAttribute("evaluationId", new Integer(-1));

                    // L'employé n'a pas d'évaluation ouverte. On peut donc créer deux
                    // type d'évaluation : une évaluation probatoire et une évaluation
                    // normale. Selon le cas, les dates de référence de l'évaluation seront
                    // calculées différemment.
                    
                    // On tente d'abord de trouver des dates pour une évaluation probatoire
                    String[] dates = EvaluationRendement.getDatesProbation(employeeId);
                    
                    // Si l'employé n'était pas en probation, on n'a pas trouvé de date. On
                    // passe donc au calcul normal des dates selon la date de résolution du
                    // poste
                    boolean probation = dates != null;
                    if(probation == false)
                    {
                        // On calcule les dates de début et de fin normalement
                    	Timestamp EndingDateLastEvaluation = getLastFormEndingDate(employeeId);
                    	if(EndingDateLastEvaluation == null)
                    	{
                    		System.out.println("formulaire pas existant, pas de date");
                    		dates = findDates(employeeId);
                    	}
                    	else
                    	{
                    		dates = setDateFromLast(EndingDateLastEvaluation);
                    		System.out.println("formulaire pas existant, date : "+EndingDateLastEvaluation.toString());
                    	}
                    }
                    
                    return createER(employeeId, formType, dates[0], dates[1], dates[2], probation);
                    
                }
                else
                {
                	System.out.println("formulaire existant");
                    pageContext.setAttribute("evaluationId", new Integer(evaluationId));
                    return loadER(evaluationId);
                }
            }
            
            throw new Exception("Aucun employé et aucune évaluation n'a été spécifié");
        }
        
        throw new Exception("Aucun type de formulaire n'a été définit");
    }
    
    /**
     * Cette méthode sert à vérifier si l'employé est en période de probation.
     * N.B. Si l'employé a déjà reçu une évaluation pour sa période de probation,
     *      on ne considérera pas ici que l'employé est encore en probation.
     *      
     * MODIF Pour un employé dans la convention collective 30 ou 31, on veut permettre 
     *       la saisie de 2 évaluations probatoires lorsque la période de probation de l'employé
     *       dure 1 an. Pour ce faire, lonrsqu'on retrouve un nouvel employé dans les 
     *       conventions susnommées, on vérifie si la période de probation dure 1 an. Si oui, 
     *       l'employé devra être évalué 2 fois : Une fois après 6 mois et une fois à la fin 
     *       de sa période de probation.
     */
    private static boolean isInProbation(Timestamp dateHired, Timestamp dateHired2, 
            Timestamp dateProbationary, Timestamp startingRefPeriodDate, int evaluationCount, 
            boolean canHaveDoubleProbation)
    {
        Calendar today = GregorianCalendar.getInstance();
       
        // Si l'employé n'a pas de date de probation ou si celle-ci n'est pas
        // au delà de la date du jour, l'employé n'est pas en probation.
        if(dateProbationary == null)
            return false;
        
        if(dateProbationary.getTime() < today.getTimeInMillis())
            return false;
        
        // Si l'employé appartient à la convention collective 30 ou 31, le
        // reste des validations sera différent
        if(canHaveDoubleProbation)
        {
            // Si l'employé a déjà eu ses deux évaluations il n'est plus en probation
            if(evaluationCount >= 2)
                return false;
            
            // Si l'employé a déjà reçu une évaluation probatoire, on doit s'assurer
            // qu'il s'agissait d'une évaluation semi-annuelle. Pour que cela soit
            // possible, il faut que la période de probation de l'employé soit de
            // plus d'un an.
            if(startingRefPeriodDate != null)
            {
                
                if(dateHired2 != null)
                {
                    if(dateHired2.getTime() <= startingRefPeriodDate.getTime())
                    {
                        Calendar temp = GregorianCalendar.getInstance();
                        temp.setTimeInMillis(dateHired2.getTime());
                        temp.add(Calendar.YEAR, 1);
                        temp.add(Calendar.DATE, -1);
                        if(temp.getTimeInMillis() > dateProbationary.getTime())
                            return false;
                    }
                }
                else
                {
                    Calendar temp = GregorianCalendar.getInstance();
                    temp.setTimeInMillis(dateHired.getTime());
                    temp.add(Calendar.YEAR, 1);
                    temp.add(Calendar.DATE, -1);
                    if(temp.getTimeInMillis() > dateProbationary.getTime())
                        return false;
                }
            }
        }
        else
        {
            // Si l'employé a déjà reçu une évaluation probatoire, il n'est plus en probation
            if(evaluationCount >= 1)
                return false;
        }
        
        return true;
    }
    
    /**
     * Cette méthode retourne la date de probation sous le format yyyy-MM-dd
     * pour l'employé passé en paramètre.
     *      
     * MODIF Pour un employé dans la convention collective 30 ou 31, on veut permettre 
     *       la saisie de 2 évaluations probatoires lorsque la période de probation de l'employé
     *       dure 1 an. Pour ce faire, lonrsqu'on retrouve un nouvel employé dans les 
     *       conventions susnommées, on vérifie si la période de probation dure 1 an. Si oui, 
     *       l'employé devra être évalué 2 fois : Une fois après 6 mois et une fois à la fin 
     *       de sa période de probation.
     */
    public static String[] getDatesProbation(int employeeId) throws SQLException, Exception
    {
        PreparedStatement stmt = DB.prepareStatement(
                "select P_Employee.DateHired," +
                "       P_Employee.DateHired2," +
                "       P_Employee.DateProbationary," +
                "       max( P_ER_Evaluation.StartingRefPeriodDate ) as StartingRefPeriodDate," +
                "       max( P_ER_Evaluation.EndingRefPeriodDate ) as EndingRefPeriodDate," +
                "       count( P_ER_Evaluation.P_ER_Evaluation_ID ) as EvaluationCount," +
                "       P_Collective_Labour_Agr.P_Collective_Labour_Agr_ID" +
                "  from P_Employee" +
                "       inner join P_ER_Evaluation" +
                "               on P_ER_Evaluation.P_Employee_ID = P_Employee.P_Employee_ID" +
                "       inner join P_Collective_Labour_Agr" +
                "               on P_Collective_Labour_Agr.P_Collective_Labour_Agr_ID = P_Employee.P_Collective_Labour_Agr_ID" +
                " where P_Employee.P_Employee_ID = " + employeeId +
                "   and P_ER_Evaluation.IsProbation = 'Y'" +
                "   and isnull( P_Employee.DateHired2, cast( '1900-01-01 00:00:00' as datetime ) ) <= isnull( P_ER_Evaluation.StartingRefPeriodDate, cast( '9999-01-01 00:00:00' as datetime ) )" +
                " group by P_Employee.P_Employee_ID," +
                "          P_Employee.DateHired," +
                "          P_Employee.DateHired2," +
                "          P_Employee.DateProbationary," +
                "          P_Collective_Labour_Agr.P_Collective_Labour_Agr_ID", null);
        
        ResultSet rs = stmt.executeQuery();
        
        if(!rs.next())
            return null;
        
        Timestamp dateHired = rs.getTimestamp("DateHired");
        Timestamp dateHired2 = rs.getTimestamp("DateHired2");
        Timestamp dateProbationary = rs.getTimestamp("DateProbationary");
        Timestamp startingRefPeriodDate = rs.getTimestamp("StartingRefPeriodDate");
        Timestamp endingRefPeriodDate = rs.getTimestamp("EndingRefPeriodDate");
        int evaluationCount = rs.getInt("EvaluationCount");
        int collectiveLabourId = rs.getObject("P_Collective_Labour_Agr_ID") != null ? rs.getInt("P_Collective_Labour_Agr_ID") : 0;
        
        rs.close();
        stmt.close();
        
        boolean canHaveDoubleProbation = EvaluationRendement.canHaveDoubleProbation(collectiveLabourId);
        
        // Si l'employé n'est pas en probation, on ne peut pas calculer de dates
        if(EvaluationRendement.isInProbation(dateHired, dateHired2, dateProbationary, startingRefPeriodDate, evaluationCount, canHaveDoubleProbation) == false)
            return null;
        
        Timestamp startDate = null;
        Timestamp endDate = null;
        
        // Si l'employé est dans la convention collective 30 ou 31, on doit prendre la bonne
        // date de fin de la période de référence pour l'évaluation probatoire de l'employé.
        if(canHaveDoubleProbation)
        {
            // On vérifie si l'employé a une période de probation de plus d'un an
            Timestamp dh = dateHired2 != null ? dateHired2 : dateHired;
            Calendar temp = GregorianCalendar.getInstance();
            temp.setTimeInMillis(dh.getTime());
            temp.add(Calendar.YEAR, 1);
            temp.add(Calendar.DATE, -1);
            if(temp.getTimeInMillis() <= dateProbationary.getTime())
            {
                // Si l'employé est à sa première évaluation, on utilise la date d'engagement
                // jusqu'à la date d'engagnement + 6 mois. Sinon, on utilise la date de fin de
                // la dernière évaluation probation + 1 jour jusqu'à la date de fin de la période
                // de probation.
                if(evaluationCount == 0)
                {
                    startDate = dh;
                    temp.setTimeInMillis(dh.getTime());
                    temp.add(Calendar.MONTH, 6);
                    endDate = new Timestamp(temp.getTimeInMillis());
                }
                else
                {
                    Calendar c = GregorianCalendar.getInstance();
                    c.setTimeInMillis(endingRefPeriodDate.getTime());
                    c.add(Calendar.DATE, 1);
                    startDate = new Timestamp(c.getTimeInMillis());
                    endDate = dateProbationary;
                }
            }
            else
            {
                // La période de probation ne dépasse pas 1an. On utilise donc la date
                // d'engagement et la date de fin de probation comme période de référence
                startDate = dh;
                endDate = dateProbationary;
            }
        }
        else
        {
            // L'employé ne fait pas partie de la convention collective 30 ou 31. On
            // calcule donc les dates de début et de fin selon la date d'engagement jusqu'à
            // la date de fin de probation
            startDate = dateHired2 != null ? dateHired2 : dateHired;
            endDate = dateProbationary;
        }
        
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTimeInMillis(startDate.getTime());
        
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return new String[] {
                simpleDateFormat.format(startDate),
                simpleDateFormat.format(endDate),
                String.valueOf(cal.get(Calendar.YEAR))
                };
    }
    
    /**
     * Indique si la convention collective passée en paramètre permet des doubles évaluation probatoire
     */
    private static boolean canHaveDoubleProbation(int collectiveLabourID) throws SQLException
    {
        boolean canHaveDoubleProbation = false;
        
        PreparedStatement stmt = DB.prepareStatement(
                "select top 1 1" +
                "  from P_Web_Parameters" +
                " where Parameter = 'COLLECT_LABOUR_AGR_DOUBLE_PROBATION'" +
                "   and Value = '" + collectiveLabourID + "'", null);
        
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next())
            canHaveDoubleProbation = true;
        
        rs.close();
        stmt.close();
        
        return canHaveDoubleProbation;
    }
    
    /**
     * Cette méthode vérifie s'il existe un formulaire qui n'a pas été fermé
     * pour l'employé passé en paramètre. Elle retourne soit le id de cette
     * évaluation ou, dans le cas échéant, 0
     */
    private static int getOpenedForm(int employeeId, int gestionnaireId, boolean isAdmin) throws JspException
    {
        String sql;
        sql = " select P_ER_Evaluation_ID"
            + "   from P_ER_Evaluation"
            + "  where P_Employee_ID = " + employeeId;
        if(isAdmin == false)
        	sql += "    and P_Employee_Gest_ID = " + gestionnaireId;
        sql += "    and EvaluationType = 'GES'";
        sql += "    and Closed = 'N'";
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            int evaluationId;
            if(rs.next())
            {
                evaluationId = rs.getInt("P_ER_Evaluation_ID");
            }
            else
            {
                evaluationId = 0;
            }
            
            rs.close();
            stmt.close();
            
            return evaluationId;
        }
        catch (SQLException e)
        {
            throw new JspException(e);
        }
    }
    
    /**
     * Cette méthode va chercher le dernier formulaire de l'employé
     * passé en paramètre. Elle retourne soit le id de cette
     * évaluation ou, dans le cas échéant, 0.
     * Ceci dans le but d'avoir les dates pour le nouveau formulaire
     */
    private static Timestamp getLastFormEndingDate(int employeeId) throws JspException
    {
        String sql;
        sql = " select EndingRefPeriodDate "
            + "   from P_ER_Evaluation"
            + "  where P_Employee_ID = " + employeeId
            + "    and EvaluationType = 'GES'"
            + "  Order by StartingRefPeriodDate desc ";
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            Timestamp endRefDate;
            if(rs.next())
            {
            	endRefDate = rs.getTimestamp("EndingRefPeriodDate");
            }
            else
            {
            	endRefDate = null;
            }
            
            rs.close();
            stmt.close();
            
            return endRefDate;
        }
        catch (SQLException e)
        {
            throw new JspException(e);
        }
    }
    
    /**
     * Cette méthode trouve la date de début et de fin de la période de référence de l'évaluation
     * pour l'employé passé en paramètre
     */
    private static String[] findDates(int employeeId) throws JspException
    {
        // Pour trouvé la date de début, on utilise le mois et le jour de la date
        // de résolution du poste si elle est définie sinon le mois et le jour de la
        // date d'embauche de l'employé qu'on met dans l'année courante. Pour la
        // date de fin, on utilise la date début + 1 an
        Timestamp dateResolution = null;
        try
        {
            String sql
            = "select isnull(dbo.TO_DATE(substring(P_Post.Resolution, 3, len(P_Post.Resolution)), null), P_Employee.DateHired) as Date"
                + "   from P_Employee,"
                + "        P_Assignment,"
                + "        P_Post"
                + "  where P_Employee.P_Employee_ID = P_Assignment.P_Employee_ID"
                + "    and P_Employee.P_Employee_ID = " + employeeId
                + "    and P_Assignment.AssignmentType = 'P'"
                + "    and P_Assignment.IsActive = 'Y'"
                + "    and getdate() between isnull(P_Assignment.StartDate, getdate())"
                + "                      and isnull(P_Assignment.EndDate, getdate())"
                + "    and P_Post.P_Post_ID = P_Assignment.P_Post_ID" +
                        " and P_POST.RESOLUTION is not null" +
                        " and dbo.TO_DATE(substring(P_Post.Resolution, 3, len(P_Post.Resolution)), null) <> convert(datetime, '1900-01-01T00:00:00.000', 126 )";

            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
                dateResolution = rs.getTimestamp("Date");
                rs.close();
                stmt.close();
            }
            else
            {
                rs.close();
                stmt.close();
                stmt = DB.prepareStatement("select DateHired from P_Employee where P_Employee_ID = " + employeeId, null);
                rs = stmt.executeQuery();
                rs.next();
                dateResolution = rs.getTimestamp("DateHired");
                rs.close();
                stmt.close();
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            throw new JspException(e);
        }
        
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(dateResolution.getTime());
        calendar.set(Calendar.YEAR, GregorianCalendar.getInstance().get(Calendar.YEAR));
        
        Timestamp startDate = new Timestamp(calendar.getTimeInMillis());
        int iYear = calendar.get(Calendar.YEAR);
        calendar.add(Calendar.YEAR, 1);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Timestamp endDate = new Timestamp(calendar.getTimeInMillis());
        
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        return new String[] {
                simpleDateFormat.format(startDate),
                simpleDateFormat.format(endDate),
                String.valueOf(iYear)};
    }

    private static String[] setDateFromLast(Timestamp lastDate) throws JspException
    {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(lastDate.getTime());
        //calendar.set(Calendar.YEAR, GregorianCalendar.getInstance().get(Calendar.YEAR));
        calendar.add(Calendar.DATE, 1);
        
        Timestamp startDate = new Timestamp(calendar.getTimeInMillis());
        int iYear = calendar.get(Calendar.YEAR);
        calendar.add(Calendar.YEAR, 1);
        calendar.add(Calendar.DATE, -1);//on enleve le jour qu'on a ajouté pour la date de depart
        Timestamp endDate = new Timestamp(calendar.getTimeInMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        return new String[] {
                simpleDateFormat.format(startDate),
                simpleDateFormat.format(endDate),
                String.valueOf(iYear)};
		    	
    }
    /**
     * Charge l'évaluation à partir de la bd
     */
    private static Hashtable loadER(int evaluationId) throws JspException
    {
        Hashtable<String, Object> evaluation = new Hashtable<String, Object>();
//        Hashtable[] expectations;
        Hashtable<String, String>[] competences;
        Hashtable<String, String>[] meetings;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        String sql;
        sql = " select evaluation.P_ER_Evaluation_ID,"
            + "        isnull(evaluation.P_Post_ID, 0) as P_Post_ID,"
            + "        evaluation.P_Employee_ID,"
            + "        evaluation.P_ER_Form_ID,"
            + "        evaluation.P_Employee_Gest_ID,"
            + "        evaluation.StartingRefPeriodDate,"
            + "        evaluation.EndingRefPeriodDate,"
            + "        evaluation.EvaluationDate,"
            + "        evaluation.EmployeeEvaluationSignoff as EmployeeEvaluationSignoff, "
            + "        evaluation.BossEvaluationSignoff as BossEvaluationSignoff, "
            + "        evaluation.EmployeeExpectencySignoff as EmployeeExpectencySignoff, "
            + "        evaluation.BossExpectencySignoff as BossExpectencySignoff, "
            + "        evaluation.BossBossEvaluationSignoff as BossBossEvaluationSignoff, "
            + "        isnull(evaluation.GlobalQuotation, '') as GlobalQuotation,"
            + "        isnull(evaluation.GlobalQuotationReason, '') as GlobalQuotationReason,"
            + "        isnull(evaluation.EmployeeCommentary, '') as EmployeeCommentary,"
            + "        isnull(evaluation.OtherEmployeeRealisation, '') as OtherEmployeeRealisation,"
            + "        isnull(evaluation.P_Gain_ID, 0) as P_Gain_ID,"
            + "        isnull((select rtrim(Value) + ' - ' + Name from P_Gain where P_Gain_ID = evaluation.P_Gain_ID), '') as GainName,"
            + "        isnull(evaluation.Closed, '') as Closed,"
            + "        isnull(evaluation.IsExpectationCompleted, '') as IsExpectationCompleted,"
            + "        isnull(evaluation.P_Job_Title_ID, 0) as P_Job_Title_ID, "
            + "        (select isnull(Name, '') from P_Job_Title where P_Job_Title_ID = evaluation.P_Job_Title_ID) as EmployeeJobTitle,"
            + "        isnull(employee.Value, '') as EmployeeValue,"
            + "        isnull(employee.Name, '') as EmployeeName,"
            + "        isnull(evaluation.UNA, '') as UNA,"
            + "        isnull(evaluation.[year], '') as \"year\","
            + "        case evaluation.IsProbation when 'Y' then '1' else '0' end as IsProbation,"
            + "        evaluation.StartingWorkDate,"
            + "        (select isnull(Name, '') from P_Job_Type where P_Job_Type_ID = evaluation.P_Job_Type_ID) as Status,"
            + "        isnull(evaluation.P_Job_Type_ID, 0) as P_Job_Type_ID, "
            + "        (select isnull(Name, '') from P_Employee where P_Employee_ID = evaluation.P_Employee_Gest_ID) as ManagerName,"
            + "        (select isnull(Name, '') from P_Post where P_Post_ID = evaluation.P_Post_ID) as ManagerJobTitle,"
            + "        isnull(evaluation.CR, '') as CR,"
//            + "        (select count(1) from P_ER_Expectation where P_ER_Evaluation_ID = evaluation.P_ER_Evaluation_ID) as ExpectationCount,"
            + "        (select count(1) from P_ER_Competence where P_ER_Evaluation_ID = evaluation.P_ER_Evaluation_ID) as CompetenceCount,"
            + "        (select count(1) from P_ER_Meeting where P_ER_Evaluation_ID = evaluation.P_ER_Evaluation_ID) as MeetingCount "	
            + "   from P_ER_Evaluation evaluation,"
            + "        P_Employee employee"
            + "  where evaluation.P_Employee_ID = employee.P_Employee_ID"
            + "    and evaluation.P_ER_Evaluation_ID = " + evaluationId;
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
                evaluation.put("evaluationId", rs.getString("P_ER_Evaluation_ID"));
                evaluation.put("employeeId", rs.getString("P_Employee_ID"));
                evaluation.put("formId", rs.getString("P_ER_Form_ID"));
                evaluation.put("managerId", rs.getString("P_Employee_Gest_ID"));
                evaluation.put("startDate", simpleDateFormat.format(rs.getTimestamp("StartingRefPeriodDate")));
                evaluation.put("endDate", simpleDateFormat.format(rs.getTimestamp("EndingRefPeriodDate")));
                evaluation.put("evaluationDate", simpleDateFormat.format(rs.getTimestamp("EvaluationDate")));
                evaluation.put("globalCote", rs.getString("GlobalQuotation"));
                evaluation.put("globalReason", rs.getString("GlobalQuotationReason"));
                evaluation.put("employeeCommentary", rs.getString("EmployeeCommentary"));
                evaluation.put("otherAchievement", rs.getString("OtherEmployeeRealisation"));
                evaluation.put("gainId", rs.getString("P_Gain_ID"));
                evaluation.put("gainName", rs.getString("GainName"));
                evaluation.put("isClosed", "Y".equals(rs.getString("Closed")) ? "true" : "false");
                evaluation.put("isExpectationCompleted", "Y".equals(rs.getString("IsExpectationCompleted")) ? "true" : "false");
                evaluation.put("jobTitleId", rs.getString("P_Job_Title_ID"));
                evaluation.put("employeeJobTitle", rs.getString("EmployeeJobTitle"));
                evaluation.put("employeeValue", rs.getString("EmployeeValue"));
                evaluation.put("employeeName", rs.getString("EmployeeName"));
                evaluation.put("administrativeUnit", rs.getString("UNA"));
                evaluation.put("hiredDate", simpleDateFormat.format(rs.getTimestamp("StartingWorkDate")));
                evaluation.put("status", rs.getString("Status"));
                evaluation.put("jobTypeId", rs.getString("P_Job_Type_ID"));
                evaluation.put("postId", rs.getString("P_Post_ID"));
                evaluation.put("managerJobTitle", rs.getString("ManagerJobTitle"));
                evaluation.put("managerName", rs.getString("ManagerName"));
                evaluation.put("cr", rs.getString("CR"));
                evaluation.put("year", rs.getString("year")); 
                evaluation.put("IsProbation", rs.getString("IsProbation")); 
                if(rs.getObject("EmployeeEvaluationSignoff") == null)
                {
                	evaluation.put("EmployeeEvaluationSignoff", "");
                }
                else
                {
                	evaluation.put("EmployeeEvaluationSignoff", simpleDateFormat.format(rs.getTimestamp("EmployeeEvaluationSignoff")));
                }
                if(rs.getObject("BossEvaluationSignoff") == null)
                {
                	evaluation.put("BossEvaluationSignoff", "");
                }
                else
                {
                	evaluation.put("BossEvaluationSignoff", simpleDateFormat.format(rs.getTimestamp("BossEvaluationSignoff")));
                }
                if(rs.getObject("EmployeeExpectencySignoff") == null)
                {
                	evaluation.put("EmployeeExpectencySignoff", "");
                }
                else
                {
                	evaluation.put("EmployeeExpectencySignoff", simpleDateFormat.format(rs.getTimestamp("EmployeeExpectencySignoff")));
                }
                if(rs.getObject("BossExpectencySignoff") == null)
                {
                	evaluation.put("BossExpectencySignoff", "");
                }
                else
                {
                	evaluation.put("BossExpectencySignoff", simpleDateFormat.format(rs.getTimestamp("BossExpectencySignoff")));
                }
                if(rs.getObject("BossBossEvaluationSignoff") == null)
                {
                	evaluation.put("BossBossEvaluationSignoff", "");
                }
                else
                {
                	evaluation.put("BossBossEvaluationSignoff", simpleDateFormat.format(rs.getTimestamp("BossBossEvaluationSignoff")));
                }
               
        		System.out.println("EvaluationRendement CompetenceCount : " + rs.getInt("CompetenceCount") );
                
//                expectations = new Hashtable[rs.getInt("ExpectationCount")];
                competences = new Hashtable[rs.getInt("CompetenceCount")];
                meetings = new Hashtable[2];
            }
            else
            {
                throw new JspException("Il n'existe pas d'évaluation pour le id " + evaluationId);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new JspException(e);
        }
        
        /*
        sql = "   select P_ER_Expectation_ID,"
            + "          isnull(ExpectedResult, '') as ExpectedResult,"
            + "          isnull(AppreciationElement, '') as AppreciationElement,"
            + "          isnull(AchievementAssessment, '') as AchievementAssessment,"
            + "          isnull(Commentary, '') as Commentary,"
            + "          isnull(ExpectationQuotation, '') as ExpectationQuotation"
            + "     from P_ER_Expectation"
            + "    where P_ER_Evaluation_ID = " + evaluationId
            + " order by PNumber";

        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            int i = 0;
            while(rs.next())
            {
                expectations[i] = new Hashtable();
                expectations[i].put("expectationId", rs.getString("P_ER_Expectation_ID"));
                expectations[i].put("expectedResult", rs.getString("ExpectedResult"));
                expectations[i].put("appreciationElement", rs.getString("AppreciationElement"));
                expectations[i].put("achievementAssessment", rs.getString("AchievementAssessment"));
                expectations[i].put("commentary", rs.getString("Commentary"));
                expectations[i].put("cote", rs.getString("ExpectationQuotation"));
                i++;
            }
            evaluation.put("expectations", expectations);
            
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            throw new JspException(e);
        } */
        
        sql = "   select P_ER_Competence_ID,"
            + "          isnull(Commentary, '') as Commentary,"
            + "          isnull(Quotation, '') as Quotation"
            + "     from P_ER_Competence"
            + "    where P_ER_Evaluation_ID = " + evaluationId
            + " order by PNumber";
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            int i = 0;
            while(rs.next())
            {
                competences[i] = new Hashtable<String, String>();
                competences[i].put("competenceId", rs.getString("P_ER_Competence_ID"));
                competences[i].put("commentary", rs.getString("Commentary"));
                competences[i].put("cote", rs.getString("Quotation"));
                i++;
            }
            evaluation.put("competences", competences);
            
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            throw new JspException(e);
        }
        
        sql = "   select P_ER_Meeting_ID,"
            + "          MeetingDate"
            + "     from P_ER_Meeting"
            + "    where P_ER_Evaluation_ID = " + evaluationId
            + " order by PNumber";
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            int i = 0;
            while(rs.next())
            {
                meetings[i] = new Hashtable<String, String>();
                meetings[i].put("meetingId", rs.getString("P_ER_Meeting_ID"));
                meetings[i].put("meetingDate", rs.getTimestamp("MeetingDate") == null ? "" : simpleDateFormat.format(rs.getTimestamp("MeetingDate")));
                i++;
            }
            
            for(int j = i; j < 2; j++)
            {
                meetings[j] = new Hashtable<String, String>();
                meetings[j].put("meetingId", "-1");
                meetings[j].put("meetingDate", "");
            }
            evaluation.put("meetings", meetings);
            
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            throw new JspException(e);
        }
        
        return evaluation;
    }
    
    /**
     * Crée le comboBox de code d'attente
     */
    public static String createExpectationComboBox(int formId, String selectedValue, boolean isReadOnly) throws SQLException
    {
        String sql
        = "       select isnull(Name, '') as Name"
            + "     from P_ER_Form_Struct"
            + "    where P_ER_Form_ID = " + formId
            + "      and FieldType = 'expectation'"
            + " order by PNumber";
        
        String select = "<select name=\"expectation_cote\"";
        if(isReadOnly)
        	select += "disabled=\"disabled\" >\n";
        else
        	select += ">\n";
        select += "<option value=\" \">&nbsp;</option>\r\n";
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            String checked;
            if(selectedValue != null
                    && selectedValue.trim().equals(rs.getString("Name")))
            {
                checked = " selected";
            }
            else
            {
                checked = "";
            }
            
            select += "<option value=\"" + rs.getString("Name") + "\"" + checked + ">" + rs.getString("Name") + "</option>\n";
        }
        
        rs.close();
        stmt.close();
        
        return select + "</select>";
    }

    /**
     * Crée le comboBox de code de compétence
     */
    public static String createCompetenceComboBox(int formId, String selectedValue, boolean isReadonly) throws SQLException
    {
        String sql;
        sql = "   select isnull(Name, '') as Name"
            + "     from P_ER_Form_Struct"
            + "    where P_ER_Form_ID = " + formId
            + "      and FieldType = 'compCote'"
            + " order by PNumber";
        
        String select = "<select name=\"competence_cote\"";
        if(isReadonly)
        	select += "disabled=\"disabled\" >\n";
        else
        	select += ">\n";
        select += "<option value=\" \">&nbsp;</option>\r\n";
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            String checked;
            if(selectedValue != null
                    && selectedValue.trim().equals(rs.getString("Name")))
            {
                checked = " selected";
            }
            else
            {
                checked = "";
            }
            
            select += "<option value=\"" + rs.getString("Name") + "\"" + checked + ">" + rs.getString("Name") + "</option>\n";
        }
        
        rs.close();
        stmt.close();
        
        return select + "</select>";
    }
    
    /**
     * Crée une nouvelle évaluation selon la structure de formulaire demandée
     */
    private static Hashtable createER(int employeeId, String formType, String startDate, String endDate, String strYear, boolean probation) throws Exception
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        // On doit d'abord obtenir le formulaire approprié
        int formId;
        int competenceCount;
        String sql
        = "select top 1 P_ER_Form_ID,"
            + "         (select count(1)"
            + "            from P_ER_Form_Struct"
            + "           where P_ER_Form_ID = P_ER_Form.P_ER_Form_ID"
            + "             and FieldType = 'competence') as CompetenceCount"
            + "    from P_ER_Form"
            + "   where FormType = '" + formType + "'"
            + "     and FormDate <= getdate() "
            + "order by Version desc";
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
                formId = rs.getInt("P_ER_Form_ID");
                competenceCount = rs.getInt("CompetenceCount");
            }
            else
            {
                throw new Exception("Il n'existe pas de formulaire valide");
            }
        }
        catch (SQLException e)
        {
            throw new JspException(e);
        }
        
        // On construit maintenant le formulaire à partir de la
        // structure des tables.
        Hashtable<String, Object> evaluation = new Hashtable<String, Object>();
        
        // P_ER_Evaluation
        evaluation.put("evaluationId", "-1");
        evaluation.put("employeeId", String.valueOf(employeeId));
        evaluation.put("formId", String.valueOf(formId));
        evaluation.put("startDate", startDate);
        evaluation.put("endDate", endDate);
        evaluation.put("evaluationDate", getStringDate());
        evaluation.put("globalCote", "");
        evaluation.put("globalReason", "");
        evaluation.put("employeeCommentary", "");
        evaluation.put("otherAchievement", "");
        evaluation.put("gainId", "");
        evaluation.put("gainName", "");
        evaluation.put("isClosed", "false");
        evaluation.put("isExpectationCompleted", "false");
        evaluation.put("year", strYear);
        evaluation.put("EmployeeEvaluationSignoff", "");
        evaluation.put("BossEvaluationSignoff", "");
        evaluation.put("EmployeeExpectencySignoff", "");
        evaluation.put("BossExpectencySignoff", "");
        evaluation.put("BossBossEvaluationSignoff", "");
        evaluation.put("IsProbation", probation ? "1" : "0");
        
        // On doit maintenant trouver le titre d'emploi de l'employé et du gestionnaire
        sql = "select jobTitle.P_Job_Title_ID,"
            + "       jobTitle.Name as Job_Title_Name,"
            + "       employee.Value as No_Employe,"
            + "       employee.Name as Emp_Name,"
            + "       employee.DateHired,"
            + "       jobType.P_Job_Type_ID,"
            + "       jobType.Name as Status,"
            + "       activity.Value as CR,"
            + "       activity.Name as Una_Name"
            + "  from P_Employee employee,"
            + "       P_Assignment assignment,"
            + "       P_Post post,"
            + "       C_Activity  activity,"
            + "       P_Job_Title jobTitle,"
            + "       P_Job_Type jobType"
            + " where employee.p_employee_id = assignment.p_employee_id   "
            + "   and assignment.startdate <= getdate()   "
            + "   and (    isnull(assignment.enddate,0) = 0    "
            + "           or (      isnull(assignment.enddate,0) <> 0    "
            + "                and isnull(assignment.enddate,0) >= getdate()   "
            + "               )   "
            + "         )    "
            + "   and assignment.p_post_id = post.p_post_id   "
            + "   and post.c_activity_id = activity.c_activity_id"
            + "   and post.p_job_title_id = jobTitle.p_job_title_id"
            + "   and employee.P_Job_Type_ID = jobType.P_Job_Type_ID"
            + "   and employee.P_Employee_ID = " + employeeId;
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            rs.next();
            
            evaluation.put("jobTitleId", rs.getString("P_Job_Title_ID"));
            evaluation.put("employeeJobTitle", rs.getString("Job_Title_Name"));
            evaluation.put("employeeValue", rs.getString("No_Employe"));
            evaluation.put("employeeName", rs.getString("Emp_Name"));
            evaluation.put("administrativeUnit", rs.getString("Una_Name"));
            evaluation.put("hiredDate", simpleDateFormat.format(rs.getTimestamp("DateHired")));
            evaluation.put("status", rs.getString("Status"));
            evaluation.put("jobTypeId", rs.getString("P_Job_Type_ID"));
            evaluation.put("cr", rs.getString("CR"));
            
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace(System.out);
            throw new Exception("Erreur lors de la récupération de l'information de l'employé.", e);
        }
        
        sql = "select    emp.P_Employee_ID,"
            + "          emp.Name,   "
            + "          aff.P_Post_ID ,  "
            + "          pos.name   as PosName"
            + "  from p_employee emp,   "
            + "       p_assignment aff ,  "
            + "       p_post  pos , ad_user usr "
            + " where emp.p_employeE_id = aff.p_employee_id "
            + "    and aff.startdate  <= getdate()"
            + "    and (    isnull(aff.enddate,0) = 0    "
            + "            or (      isnull(aff.enddate,0) <> 0    "
            + "                 and isnull(aff.enddate,0) >= getdate()"
            + "                )   "
            + "          )    "
//            + "    and exists (select '1'    "
//            + "                           from rv_emp_evalue vem   "
//            + "                         where vem.p_post_id_1 = aff.p_post_id )"
            + "    and aff.p_post_id = pos.p_post_id      "
            + "    and emp.ad_user_id = usr.ad_user_id"
            + "    and exists ( select 1"
            + "                   from P_Distribution_Booklet"
            + "                   join P_Employee_Dist_Booklet"
            + "                     on P_Employee_Dist_Booklet.P_Distribution_Booklet_ID = P_Distribution_Booklet.P_Distribution_Booklet_ID"
            + "                  where getdate() between P_Employee_Dist_Booklet.Start_Date and isnull( P_Employee_Dist_Booklet.End_Date, getdate() )"
            + "                    and getdate() >= P_Employee_Dist_Booklet.Start_Date"
            + "                    and P_Employee_Dist_Booklet.P_Employee_ID = " + employeeId
            + "                    and P_Distribution_Booklet.P_Employee_Gestionnaire_ID = emp.P_Employee_ID )";
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            rs.next();

            evaluation.put("managerId", rs.getString("P_Employee_ID"));
            evaluation.put("postId", rs.getString("P_Post_ID"));
            evaluation.put("managerJobTitle", rs.getString("PosName"));
            evaluation.put("managerName", rs.getString("Name"));
            
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            throw new Exception("L'employé n'a pas de gestionnaire.", e);
        }
        
        /*
        // P_ER_Expectation
        Hashtable[] expectations = new Hashtable[6];
        for(int i = 0; i < 6; i++)
        {
            expectations[i] = new Hashtable();
            expectations[i].put("expectationId", "-1");
            expectations[i].put("expectedResult", "");
            expectations[i].put("appreciationElement", "");
            expectations[i].put("achievementAssessment", "");
            expectations[i].put("commentary", "");
            expectations[i].put("cote", "");
        }
        evaluation.put("expectations", expectations); */
        
        // P_ER_Competence
        Hashtable<String, String>[] competences = new Hashtable[competenceCount];
        for(int i = 0; i < competenceCount; i++)
        {
            competences[i] = new Hashtable<String, String>();
            competences[i].put("competenceId", "-1");
            competences[i].put("commentary", "");
            competences[i].put("cote", "");
        }
        evaluation.put("competences", competences);
        
        Hashtable<String, String>[] meetings = new Hashtable[2];
        for(int i = 0; i < 2; i++)
        {
            meetings[i] = new Hashtable<String, String>();
            meetings[i].put("meetingId", "-1");
            meetings[i].put("meetingDate", "");
        }
        evaluation.put("meetings", meetings);
        
        return evaluation;
    }
    
    /**
     * Retourne la date courante selon le format yyyy-MM-dd
     */
    private static String getStringDate()
    {
        Calendar calendar = GregorianCalendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Timestamp(calendar.getTimeInMillis()));
    }
    
    public EvaluationRendement()
    {
        super();
    }

    public String[] doValidate()
    {
        return null;
    }

    /**
     * Traitement principal du dernier post
     */
    public boolean afterValidation(JspWriter out)
    {
        String action = this.request.getParameter("action");
        if(action != null)
        {
            if(action.equals("save"))
            {
                // Si le formulaire était fermé, la seule chose que l'utilisateur peut faire
                // en sauvegardant, c'est de réouvrir de formulaire. On doit donc vérifier si
                // on est dans cette situation pour ne pas enregistrer le tout pour rien.
                P_ER_Evaluation evaluation = new P_ER_Evaluation(Env.getCtx(), Integer.parseInt(request.getParameter("evaluationId")), null);
                if(evaluation.getP_ER_Evaluation_ID() > 0 &&  evaluation.isClosed())
                {
                    if(!"on".equals(request.getParameter("isClosed")))
                    {
                        evaluation.setClosed(false);
                        evaluation.save();
                    }
                    try
                    {
                        String webAppUrl = "";
                        String strSelectWebAppUrl = "Select WebAppUrl FROM P_System_Parameters ";
                        PreparedStatement pstm = DB.prepareStatement(strSelectWebAppUrl, null);
                        try
                        {
                            ResultSet rs = pstm.executeQuery();
                            if(rs.next())
                            {
                                webAppUrl = rs.getString(1);
                            }
                            rs.close();
                            pstm.close();
                        }
                        catch(SQLException e)
                        {
                            e.printStackTrace();
                        }
                         
                        response.sendRedirect(webAppUrl+"evaluationRendement/evalges.jsp?evaluationId=" + evaluation.getP_ER_Evaluation_ID());
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace(System.out);
                    }
                    return false;
                }
                else
                {
                    try
                    {
                        // L'utilisateur a choisi de sauvegarder, on doit donc
                        // créer des enregistrements dans les table P_ER_Evaluation,
                        // P_ER_Expectation, P_ER_Competence et P_ER_Meeting
                        evaluation.setEvaluationType(this.getFormType());
                        evaluation.setP_Employee_ID(Integer.parseInt(request.getParameter("employeeId")));
                        evaluation.setP_ER_Form_ID(Integer.parseInt(request.getParameter("formId")));
                        evaluation.setP_Employee_Gest_ID(Integer.parseInt(request.getParameter("managerId")));
                        evaluation.setStartingRefPeriodDate(this.getDate(request.getParameter("startDate")));
                        evaluation.setEndingRefPeriodDate(this.getDate(request.getParameter("endDate")));
                        evaluation.setEvaluationDate(this.getDate(request.getParameter("evaluationDate")));
                        evaluation.setYear(Integer.parseInt(request.getParameter("year")));
                        evaluation.setGlobalQuotation(request.getParameter("globalCote"));
                        evaluation.setGlobalQuotationReason(request.getParameter("globalReason"));
                        evaluation.setEmployeeCommentary(request.getParameter("employeeCommentary"));
                        evaluation.setOtherEmployeeRealisation(request.getParameter("otherAchievement"));
                        
                        System.out.println("==> request.getParameter(\"IsProbation\")=\"" + request.getParameter("IsProbation") + "\"");
                        
                        evaluation.setIsProbation("1".equals(request.getParameter("IsProbation")));
                        if(request.getParameter("jobTitleId") != null && !request.getParameter("jobTitleId").equals(""))
                        	evaluation.setP_Job_Title_ID(Integer.parseInt(request.getParameter("jobTitleId")));
                        if(request.getParameter("jobTypeId") != null && !request.getParameter("jobTypeId").equals(""))
                        	evaluation.setP_Job_Type_ID(Integer.parseInt(request.getParameter("jobTypeId")));
                        if(request.getParameter("postId") != null && !request.getParameter("postId").equals(""))
                        	evaluation.setP_Post_ID(Integer.parseInt(request.getParameter("postId")));
                        evaluation.setClosed("on".equals(request.getParameter("isClosed")));
                        evaluation.setIsExpectationCompleted("on".equals(request.getParameter("isExpectationCompleted")));
                        if(request.getParameter("gainId") != null && !request.getParameter("gainId").equals(""))
                            evaluation.setP_Gain_ID(Integer.parseInt(request.getParameter("gainId")));
                        evaluation.setUNA(request.getParameter("administrativeUnit"));
                        evaluation.setCR(request.getParameter("cr"));
                        evaluation.setStartingWorkDate(this.getDate(request.getParameter("hiredDate")));
                        if(!((String)request.getParameter("EmployeeEvaluationSignoff")).equals(""))
                        {
                        	evaluation.setEmployeeEvaluationSignoff(this.getDate(request.getParameter("EmployeeEvaluationSignoff")));
                        }
                        if(!((String)request.getParameter("BossEvaluationSignoff")).equals(""))
                        {
                        	evaluation.setBossEvaluationSignoff(this.getDate(request.getParameter("BossEvaluationSignoff")));
                        }
                        if(!((String)request.getParameter("EmployeeExpectencySignoff")).equals(""))
                        {
                        	evaluation.setEmployeeExpectencySignoff(this.getDate(request.getParameter("EmployeeExpectencySignoff")));
                        }
                        if(!((String)request.getParameter("BossExpectencySignoff")).equals(""))
                        {
                        	evaluation.setBossExpectencySignoff(this.getDate(request.getParameter("BossExpectencySignoff")));
                        }
                        if(!((String)request.getParameter("BossBossEvaluationSignoff")).equals(""))
                        {
                        	evaluation.setBossBossEvaluationSignoff(this.getDate(request.getParameter("BossBossEvaluationSignoff")));
                        }
                        
                        if(evaluation.save())
                        {
                            boolean ok = true;
                            
                            // Attentes
                            String[] expectation_expectationIds = request.getParameterValues("expectation_expectationId");
                            String[] expectation_expectedResults = request.getParameterValues("expectation_expectedResult");
                            String[] expectation_appreciationElements = request.getParameterValues("expectation_appreciationElement");
                            String[] expectation_achievementAssessments = request.getParameterValues("expectation_achievementAssessment");
                            String[] expectation_commentarys = request.getParameterValues("expectation_commentary");
                            String[] expectation_cotes = request.getParameterValues("expectation_cote");
                            for(int i = 0; i < expectation_expectationIds.length; i++)
                            {
                                int temp = (expectation_expectationIds[i] == null || expectation_expectationIds[i].equals("") 
                                        ? -1 : Integer.parseInt(expectation_expectationIds[i]));
                                
                                P_ER_Expectation expectation = new P_ER_Expectation(Env.getCtx(), temp, null);
                                expectation.setP_ER_Evaluation_ID(evaluation.getP_ER_Evaluation_ID());
                                expectation.setPNumber(i);
                                expectation.setExpectedresult(expectation_expectedResults[i]);
                                expectation.setAppreciationElement(expectation_appreciationElements[i]);
                                expectation.setAchievementAssessment(expectation_achievementAssessments[i]);
                                expectation.setCommentary(expectation_commentarys[i]);
                                expectation.setExpectationQuotation(expectation_cotes[i]);
                                ok = ok && expectation.save();
                            }
                            
                            
                            String[] competence_competenceIds = request.getParameterValues("competence_competenceId");
                            String[] competence_commentarys = request.getParameterValues("competence_commentary");
                            String[] competence_cotes = request.getParameterValues("competence_cote");
                            for(int i = 0; i < competence_competenceIds.length; i++)
                            {
                                P_ER_Competence competence = new P_ER_Competence(Env.getCtx(), Integer.parseInt(competence_competenceIds[i]), null);
                                competence.setCommentary(competence_commentarys[i]);
                                competence.setQuotation(competence_cotes[i]);
                                competence.setPNumber(i);
                                competence.setP_ER_Evaluation_ID(evaluation.getP_ER_Evaluation_ID());
                                ok = ok && competence.save();
                            }
                            
                            if(request.getParameter("meeting_meetingDate0") != null
                                    && !request.getParameter("meeting_meetingDate0").equals(""))
                            {
                                P_ER_Meeting meeting = new P_ER_Meeting(Env.getCtx(), Integer.parseInt(request.getParameter("meeting_meetingId0")), null);
                                meeting.setMeetingDate(this.getDate(request.getParameter("meeting_meetingDate0")));
                                meeting.setPNumber(0);
                                meeting.setP_ER_Evaluation_ID(evaluation.getP_ER_Evaluation_ID());
                                ok = ok && meeting.save();
                            }
    
                            if(request.getParameter("meeting_meetingDate1") != null
                                    && !request.getParameter("meeting_meetingDate1").equals(""))
                            {
                                P_ER_Meeting meeting = new P_ER_Meeting(Env.getCtx(), Integer.parseInt(request.getParameter("meeting_meetingId1")), null);
                                meeting.setMeetingDate(this.getDate(request.getParameter("meeting_meetingDate1")));
                                meeting.setPNumber(1);
                                meeting.setP_ER_Evaluation_ID(evaluation.getP_ER_Evaluation_ID());
                                ok = ok && meeting.save();
                            }
                            
                            if(!ok)
                            {
                                System.out.println("Erreur dans la sauvegarde d'une partie du formulaire");
                            }
    
                            System.out.println("Evaluation sauvegardée : " + evaluation.getP_ER_Evaluation_ID());
                            
                            // Une fois la sauvegarde complétée, si l'évaluation a été fermée,
                            // on doit créer un nouveau formulaire d'évaluation pour la prochaine
                            /*if(evaluation.isClosed())
                            {
                                String dates[] = findDates(evaluation.getP_Employee_ID());
                                this.pageContext.setAttribute("evaluation", createER(evaluation.getP_Employee_ID(), "GES", dates[0], dates[1]));
                            }
                            else
                            {
                                this.pageContext.setAttribute("evaluation", loadER(evaluation.getP_ER_Evaluation_ID()));
                            }*/
                            try
                            {
                            	String webAppUrl = "";
                            	String strSelectWebAppUrl = "Select WebAppUrl FROM P_System_Parameters ";
                            	PreparedStatement pstm = DB.prepareStatement(strSelectWebAppUrl, null);
                            	try
                            	{
                            		ResultSet rs = pstm.executeQuery();
                            		if(rs.next())
                            		{
                            			webAppUrl = rs.getString(1);
                            		}
                            		rs.close();
                            		pstm.close();
                            	}
                            	catch(SQLException e)
                            	{
                            		e.printStackTrace();
                            	}
                            	 
                            	response.sendRedirect(webAppUrl+"evaluationRendement/evalges.jsp?evaluationId=" + evaluation.getP_ER_Evaluation_ID());
                            }
                            catch(Exception e)
                            {
                            	e.printStackTrace(System.out);
                            }
                            return false;
                        }
                        else
                        {
                            System.out.println("Erreur dans la sauvegarde de l'évaluation");
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace(System.out);
                    }
                }
            }
        }
        this.reloadPost();
 
        return true;
    }
    
    private Timestamp getDate(String date)
    {
        try
        {
            Calendar calendar = PgiUtil.stringToDate(date);
            return new Timestamp(calendar.getTimeInMillis());
        }
        catch (Exception e)
        {
            System.out.println("Erreur dans la conversion vers un Timestamp. Valeur d'entrée : " + date);
            return null;
        }
    }
    
    /**
     * Retourne le type du formulaire
     */
    private String getFormType() throws SQLException
    {
        String sql = "select formType from P_ER_Form where P_ER_Form_ID = " + request.getParameter("formId");
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        if(!rs.next())
            System.out.println("wtf solstice.custom.EvaluationRendement.getFormType:" + request.getParameter("formId"));
        String formType = rs.getString("formType");
        rs.close();
        stmt.close();
        return formType;
    }

    private String getParameter(String parameterName)
    {
        String value = request.getParameter(parameterName);
        if(value == null || value.equals(""))
        {
            System.out.println(parameterName + " is empty");
            return "";
        }
        return value;
    }
    
    /**
     * Cette méthode charge les informations du dernier post
     * et crée une table de hachage avec celles-ci
     */
    private void reloadPost()
    {
        System.out.println("debut reloadPost()");
        // On construit maintenant le formulaire à partir de la
        // structure des tables.
        Hashtable<String, Object> evaluation = new Hashtable<String, Object>();
        
        System.out.println("reloadPost evaluation");
        // P_ER_Evaluation
        this.pageContext.setAttribute("evaluationId", new Integer(Integer.parseInt(this.getParameter("evaluationId"))));
        evaluation.put("evaluationId", this.getParameter("evaluationId"));
        evaluation.put("employeeId", this.getParameter("employeeId"));
        evaluation.put("formId", this.getParameter("formId"));
        evaluation.put("managerId", this.getParameter("managerId"));
        evaluation.put("startDate", this.getParameter("startDate"));
        evaluation.put("endDate", this.getParameter("endDate"));
        evaluation.put("evaluationDate", this.getParameter("evaluationDate"));
        evaluation.put("globalCote", this.getParameter("globalCote"));
        evaluation.put("globalReason", this.getParameter("globalReason"));
        evaluation.put("employeeCommentary", this.getParameter("employeeCommentary"));
        evaluation.put("otherAchievement", this.getParameter("otherAchievement"));
        evaluation.put("IsProbation", this.getParameter("IsProbation"));
        evaluation.put("jobTitleId", this.getParameter("jobTitleId"));
        evaluation.put("jobTypeId", this.getParameter("jobTypeId"));
        evaluation.put("postId", this.getParameter("postId"));
        evaluation.put("status", this.getParameter("status"));
        evaluation.put("isClosed", "on".equals(this.getParameter("isClosed")) ? "true" : "false");
        evaluation.put("isExpectationCompleted", "on".equals(this.getParameter("isExpectationCompleted")) ? "true" : "false");
        evaluation.put("gainId", this.getParameter("gainId"));
        evaluation.put("year", this.getParameter("year"));
        
        // On doit maintenant trouver le titre d'emploi de l'employé et du gestionnaire
        evaluation.put("employeeJobTitle", this.getParameter("employeeJobTitle"));
        evaluation.put("employeeValue", this.getParameter("employeeValue"));
        evaluation.put("employeeName", this.getParameter("employeeName"));
        evaluation.put("managerJobTitle", this.getParameter("managerJobTitle"));
        evaluation.put("managerName", this.getParameter("managerName"));
        evaluation.put("administrativeUnit", this.getParameter("administrativeUnit"));
        evaluation.put("cr", this.getParameter("cr"));
        evaluation.put("hiredDate", this.getParameter("hiredDate"));
        
        evaluation.put("EmployeeEvaluationSignoff", this.getParameter("EmployeeEvaluationSignoff"));
        evaluation.put("BossEvaluationSignoff", this.getParameter("BossEvaluationSignoff"));
        evaluation.put("EmployeeExpectencySignoff", this.getParameter("EmployeeExpectencySignoff"));
        evaluation.put("BossExpectencySignoff", this.getParameter("BossExpectencySignoff"));
        evaluation.put("BossBossEvaluationSignoff", this.getParameter("BossBossEvaluationSignoff"));

        /*
        // P_ER_Expectation
        String[] expectationIds = request.getParameterValues("expectation_expectationId");
        String[] expectedResults = request.getParameterValues("expectation_expectedResult");
        String[] appreciationElements = request.getParameterValues("expectation_appreciationElement");
        String[] achievementAssessments = request.getParameterValues("expectation_achievementAssessment");
        String[] commentarys = request.getParameterValues("expectation_commentary");
        String[] cotes = request.getParameterValues("expectation_cote");
        
        Hashtable[] expectations = new Hashtable[6];
        for(int i = 0; i < 6; i++)
        {
            expectations[i] = new Hashtable();
            expectations[i].put("expectationId", expectationIds[i]);
            expectations[i].put("expectedResult", expectedResults[i]);
            expectations[i].put("appreciationElement", appreciationElements[i]);
            expectations[i].put("achievementAssessment", achievementAssessments[i]);
            expectations[i].put("commentary", commentarys[i]);
            expectations[i].put("cote", cotes[i]);
        }
        evaluation.put("expectations", expectations); */
        
        System.out.println("reloadPost competence");
        // P_ER_Competence
        String[] competenceIds = request.getParameterValues("competence_competenceId");
        String[] commentarys = request.getParameterValues("competence_commentary");
        String[] cotes = request.getParameterValues("competence_cote");
        
        Hashtable<String, String>[] competences = new Hashtable[competenceIds.length];
        for(int i = 0; i < competenceIds.length; i++)
        {
            competences[i] = new Hashtable<String, String>();
            competences[i].put("competenceId", competenceIds[i]);
            competences[i].put("commentary", commentarys[i]);
            competences[i].put("cote", cotes[i]);
        }
        evaluation.put("competences", competences);
        
        System.out.println("reloadPost meeting");
        Hashtable<String, String>[] meetings = new Hashtable[2];
        meetings[0] = new Hashtable<String, String>();
        meetings[0].put("meetingId", this.getParameter("meeting_meetingId0"));
        meetings[0].put("meetingDate", this.getParameter("meeting_meetingDate0"));
        meetings[1] = new Hashtable<String, String>();
        meetings[1].put("meetingId", this.getParameter("meeting_meetingId1"));
        meetings[1].put("meetingDate", this.getParameter("meeting_meetingDate1"));
        evaluation.put("meetings", meetings);
        
        this.pageContext.setAttribute("evaluation", evaluation);
        System.out.println("fin reloadPost()");
    }
    
    public static String formatPNumber(Object o)
    {
        if(o != null && o instanceof Number)
        {
            return String.valueOf(((Number)o).intValue());
        }
        return "";
    }
    
    /**
     * Cette méthode sert à initialiser la liste des attentes
     */
    public static Enumeration initializeExpectation(PageContext pageContext) throws Exception
    {
        System.out.println("execution de solstice.custom.EvaluationRendement.initializeExpectation");
        Vector<Hashtable> expectations = new Vector<Hashtable>();
        int evaluationId = ((Integer)pageContext.getAttribute("evaluationId")).intValue();
        
        // S'il s'agit d'un nouvel enregistrement, on crée une attente vide. Sinon,
        // on charge les attentes provenant de la base de données
        if(evaluationId <= 0)
        {
            Hashtable<String, String> ht = new Hashtable<String, String>();
            ht.put("expectation_expectationId", "");
            ht.put("expectation_expectedResult", "");
            ht.put("expectation_appreciationElement", "");
            ht.put("expectation_achievementAssessment", "");
            ht.put("expectation_commentary", "");
            ht.put("expectation_cote", "");
            expectations.add(ht);
        }
        else
        {
            String sql = "select P_ER_Expectation_ID,"
                + "          isnull(ExpectedResult, '') as ExpectedResult,"
                + "          isnull(AppreciationElement, '') as AppreciationElement,"
                + "          isnull(AchievementAssessment, '') as AchievementAssessment,"
                + "          isnull(Commentary, '') as Commentary,"
                + "          isnull(ExpectationQuotation, '') as ExpectationQuotation"
                + "     from P_ER_Expectation"
                + "    where P_ER_Evaluation_ID = " + evaluationId
                + " order by PNumber";
    
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next())
            {
                Hashtable<String, String> ht = new Hashtable<String, String>();
                ht.put("expectation_expectationId", rs.getString("P_ER_Expectation_ID"));
                ht.put("expectation_expectedResult", rs.getString("ExpectedResult"));
                ht.put("expectation_appreciationElement", rs.getString("AppreciationElement"));
                ht.put("expectation_achievementAssessment", rs.getString("AchievementAssessment"));
                ht.put("expectation_commentary", rs.getString("Commentary"));
                ht.put("expectation_cote", rs.getString("ExpectationQuotation"));
                expectations.add(ht);
            }
            
            rs.close();
            stmt.close();
        }
        return expectations.elements();
    }
    
}
