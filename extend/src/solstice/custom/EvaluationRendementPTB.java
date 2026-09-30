package solstice.custom;
 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.P_ER_Competence;
import solstice.model.P_ER_Evaluation;
import solstice.model.P_ER_Expectation;
import solstice.process.PgiUtil;

public class EvaluationRendementPTB extends ValidationBase
{
    
    /**
     * Cette méthode crée la table de hachage contenant les informations
     * du formulaire courant.
     */
    public static Hashtable createMainForm(PageContext pageContext) throws Exception
    {
        // Un type de formulaire doit avoir été spécifié
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        int gestionnaireId = ((IUserInfo)request.getSession().getAttribute("userInfo")).getEmployeeId();
        
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
                // normale. Selon le cas, la date de fin de l'évaluation sera
                // calculée différemment.
                
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
                
                return createER(employeeId, dates[0], dates[1], dates[2], probation);
                
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
        sql += "    and EvaluationType = 'PTB'";
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
    public static Timestamp getLastFormEndingDate(int employeeId) throws JspException
    {
        String sql;
        sql = " select EndingRefPeriodDate "
            + "   from P_ER_Evaluation"
            + "  where P_Employee_ID = " + employeeId
//            + "    and EvaluationType = 'PTB'"
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
    public static String[] findDates(int employeeId) throws JspException
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
                + "    and getdate() between isnull(P_Assignment.StartDate, getdate())"
                + "                      and isnull(P_Assignment.EndDate, getdate())"
                + "    and P_Post.P_Post_ID = P_Assignment.P_Post_ID" +
                        " and P_Post.Resolution is not null" +
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
            throw new JspException(e);
        }
        
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(dateResolution.getTime());
        calendar.set(Calendar.YEAR, GregorianCalendar.getInstance().get(Calendar.YEAR));
        
        Timestamp startDate = new Timestamp(calendar.getTimeInMillis());
        calendar.add(Calendar.YEAR, 1);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Timestamp endDate = new Timestamp(calendar.getTimeInMillis());

        int iYear = endDate.getYear() + 1900;
        
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        return new String[] {
                simpleDateFormat.format(startDate),
                simpleDateFormat.format(endDate),
                String.valueOf(iYear)};
    }

    public static String[] setDateFromLast(Timestamp lastDate) throws JspException
    {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(lastDate.getTime());
        //calendar.set(Calendar.YEAR, GregorianCalendar.getInstance().get(Calendar.YEAR));
        calendar.add(Calendar.DATE, 1);
        
        Timestamp startDate = new Timestamp(calendar.getTimeInMillis());
        calendar.add(Calendar.YEAR, 1);
        calendar.add(Calendar.DATE, -1);//on enleve le jour qu'on a ajouté pour la date de depart
        Timestamp endDate = new Timestamp(calendar.getTimeInMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        int iYear = endDate.getYear() + 1900;

        return new String[] {
                simpleDateFormat.format(startDate),
                simpleDateFormat.format(endDate),
                String.valueOf(iYear)};
		    	
    }

    
    /**
     * Cette méthode charge le formulaire passé en paramètre
     * @param evaluationId
     * @return
     */
    public static Hashtable loadER(int evaluationId) throws Exception
    {
        Hashtable<String, Object> evaluation = new Hashtable<String, Object>();
        evaluation.put("evaluationId", String.valueOf(evaluationId));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        String sql;
        sql = " select evaluation.P_Job_Title_ID as jobTitleId,"
            + "        evaluation.P_Job_Type_ID as jobTypeId,"
            + "        evaluation.P_Employee_ID as employeeId,"
            + "        evaluation.P_Employee_Gest_ID as managerId,"
            + "        evaluation.P_ER_Form_ID as formId,"
            + "        evaluation.P_Post_ID as postId,"
            + "        evaluation.EmployeeEvaluationSignoff as EmployeeEvaluationSignoff, "
            + "        evaluation.BossEvaluationSignoff as BossEvaluationSignoff, "
            + "        evaluation.EmployeeExpectencySignoff as EmployeeExpectencySignoff, "
            + "        evaluation.BossExpectencySignoff as BossExpectencySignoff, "
            + "        evaluation.BossBossEvaluationSignoff as BossBossEvaluationSignoff, "
            + "        (select Name from P_Job_Type where P_Job_Type_ID = evaluation.P_Job_Type_ID) as status,"
            + "        employee.Name as employeeName,"
            + "        employee.Value as employeeValue,"
            + "        (select Name from P_Job_Title where P_Job_Title_ID = evaluation.P_Job_Title_ID) as employeeJobTitle,"
            + "        evaluation.CR as cr,"
            + "        evaluation.UNA as administrativeUnit,"
            + "        evaluation.StartingRefPeriodDate as startDate,"
            + "        evaluation.EndingRefPeriodDate as endDate,"
            + "        (select Name from P_Employee where P_Employee_ID = evaluation.P_Employee_Gest_ID) as managerName,"
            + "        (select Name from P_Post where P_Post_ID = evaluation.P_Post_ID) as managerJobTitle,"
            + "        evaluation.StartingWorkDate as hiredDate,"
            + "        evaluation.EvaluationDate as evaluationDate,"
            + "        evaluation.P_Gain_ID as gainId,"
            + "        isnull((select rtrim(Value) + ' - ' + Name from P_Gain where P_Gain_ID = evaluation.P_Gain_ID), '') as GainName,"
            + "        evaluation.[Year] as \"year\","
            + "        case evaluation.IsProbation when 'Y' then '1' else '0' end as IsProbation,"
            + "        (case evaluation.IsExpectationCompleted when 'Y' then 'checked' else '' end) as isExpectationCompleted,"
            + "        evaluation.GlobalQuotation as globalQuotation,"
            + "        evaluation.GlobalQuotationReason as globalQuotationReason,"
            + "        evaluation.Recommendation as recommendation,"
            + "        evaluation.EmployeeCommentary as employeeCommentary,"
            + "        evaluation.OtherEmployeeRealisation as otherEmployeeRealisation,"
            + "        (case evaluation.Closed when 'Y' then 'checked' else '' end) as isClosed"
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
                ResultSetMetaData md = rs.getMetaData();
                for(int i = 1; i <= md.getColumnCount(); i++)
                {
                    if(rs.getObject(i) instanceof Timestamp)
                    {
                    	if(rs.getObject(i).equals(null))
                    	{
                    		evaluation.put(md.getColumnName(i), "");
                    	}
                    	else
                    	{
                    		evaluation.put(md.getColumnName(i), simpleDateFormat.format(rs.getTimestamp(i)));
                    	}
                    }
                    else
                    {
                        evaluation.put(md.getColumnName(i), rs.getString(i) == null ? "" : rs.getString(i));
                    }
                }
            }
            else
            {
                rs.close();
                stmt.close();
                throw new Exception("Il n'existe aucune évaluation pour le id " + evaluationId);
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            throw new Exception(e);
        }
        
        Hashtable<String, String> expectation = new Hashtable<String, String>();
        sql = " select top 1"
            + "        isnull(ExpectedResult, '') as ExpectedResult,"
            + "        isnull(AppreciationElement, '') as AppreciationElement,"
            + "        isnull(AchievementAssessment, '') as AchievementAssessment"
            + "   from P_ER_Expectation"
            + "  where P_ER_Evaluation_ID = " + evaluationId;
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
                expectation.put("expectedResult", rs.getString("ExpectedResult"));
                expectation.put("appreciationElement", rs.getString("AppreciationElement"));
                expectation.put("achievementAssessment", rs.getString("AchievementAssessment"));
            }
            
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            throw new Exception(e);
        }
        evaluation.put("expectation", expectation);
        
        Hashtable<String, String>[] competences = new Hashtable[14];
        sql = " select (case IsActive when 'Y' then 'checked' else '' end) as active,"
            + "        isnull(Commentary, '') as Commentary,"
            + "        isnull(Quotation, '') as Quotation"
            + "   from P_ER_Competence"
            + "  where P_ER_Evaluation_ID = " + evaluationId;
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            int i = 0;
            while(rs.next())
            {
                competences[i] = new Hashtable<String, String>();
                competences[i].put("active", rs.getString("active"));
                competences[i].put("commentary", rs.getString("commentary"));
                competences[i].put("quotation", rs.getString("Quotation"));
                i++;
            }
            
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            throw new Exception(e);
        }
        evaluation.put("competences", competences);
        
        return evaluation;
    }
    
    private static Hashtable createER(int employeeId, String startDate, String endDate, String strYear, boolean probation) throws Exception
    {
        Hashtable<String, Object> evaluation = new Hashtable<String, Object>();
        
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        // On doit d'abord obtenir le formulaire approprié
        int formId;
        String sql
        = "select top 1 P_ER_Form_ID"
            + "    from P_ER_Form"
            + "   where FormType = 'PTB'"
            + "     and FormDate <= getdate() "
            + "order by Version desc";
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
                formId = rs.getInt("P_ER_Form_ID");
            }
            else
            {
                throw new Exception("Il n'existe pas de formulaire valide");
            }
        }
        catch (SQLException e)
        {
            throw new Exception(e);
        }
        
        evaluation.put("evaluationId", "-1");
        evaluation.put("employeeId", String.valueOf(employeeId));
        evaluation.put("formId", String.valueOf(formId));
        evaluation.put("startDate", startDate);
        evaluation.put("endDate", endDate);
        evaluation.put("evaluationDate", getStringDate());
        evaluation.put("globalQuotation", "");
        evaluation.put("globalQuotationReason", "");
        evaluation.put("employeeCommentary", "");
        evaluation.put("otherEmployeeRealisation", "");
        evaluation.put("gainId", "");
        evaluation.put("GainName", "");
        evaluation.put("isCompleted", "");
        evaluation.put("recommendation", "");
        evaluation.put("isExpectationCompleted", "");
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
            throw new Exception("L'employé n'a pas de gestionnaire", e);
        }
        
        Hashtable<String, String> expectation = new Hashtable<String, String>();
        expectation.put("expectedResult", "");
        expectation.put("appreciationElement", "");
        expectation.put("achievementAssessment", "");
        evaluation.put("expectation", expectation);
        
        Hashtable<String, String>[] competences = new Hashtable[14];
        for(int i = 0; i < 14; i++)
        {
            competences[i] = new Hashtable<String, String>();
            competences[i].put("active", "");
            competences[i].put("commentary", "");
            competences[i].put("quotation", "");
        }
        evaluation.put("competences", competences);
        
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
    
    /**
     * Crée le comboBox pour les codes d'habiletés
     * @param formId
     * @return
     */
    public static String createComboBoxHability(String formId, String selectedValue, boolean isReadOnly) throws SQLException
    {
        String sql
        = "       select isnull(Name, '') as Name"
            + "     from P_ER_Form_Struct"
            + "    where P_ER_Form_ID = " + formId
            + "      and FieldType = 'expectationCote'"
            + " order by PNumber";
        
        String select = "<select name=\"competence_quotation\"";
        
        if(isReadOnly)
        	select += "disabled=\"disabled\" >\n";
        else
        	select += ">\n";
        select += "<option value=\" \">&nbsp;</option>\r\n";

        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            String checked = rs.getString("Name").trim().equals(selectedValue.trim()) ? " selected" : "";
            select += "<option value=\"" + rs.getString("Name") + "\"" + checked + ">" + rs.getString("Name") + "</option>\n";
        }
        
        rs.close();
        stmt.close();
        
        return select + "</select>";
    }

    public EvaluationRendementPTB()
    {
        super();
    }

    protected String[] doValidate()
    {
        return null;
    }

    public boolean afterValidation(JspWriter out)
    {
        // Si le formulaire était fermé, la seule chose que l'utilisateur peut faire
        // en sauvegardant, c'est de réouvrir de formulaire. On doit donc vérifier si
        // on est dans cette situation pour ne pas enregistrer le tout pour rien.
        P_ER_Evaluation erEvaluation = new P_ER_Evaluation(Env.getCtx(), Integer.parseInt(request.getParameter("evaluationId")), null);
        if(request.getParameter("action") != null
                && request.getParameter("action").equals("save")
                && erEvaluation.getP_ER_Evaluation_ID() > 0
                && erEvaluation.isClosed())
        {
            if(!"on".equals(request.getParameter("isClosed")))
            {
                erEvaluation.setClosed(false);
                erEvaluation.save();
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


                System.out.println("redirecting");
                response.sendRedirect(webAppUrl+"evaluationRendement/evalptb.jsp?evaluationId=" + erEvaluation.getP_ER_Evaluation_ID());
            }
            catch(Exception e)
            {
                e.printStackTrace(System.out);
            }
            return false;
        }
        
        // On doit d'abord récupérer le résultat du post
        WebUtil webUtil = new WebUtil(request);
        Hashtable evaluation = webUtil.buildHashtable(
                new WebUtil.IField[] {
                        webUtil.instanciateSimpleTextField("jobTitleId"),
                        webUtil.instanciateSimpleTextField("jobTypeId"),
                        webUtil.instanciateSimpleTextField("employeeId"),
                        webUtil.instanciateSimpleTextField("managerId"),
                        webUtil.instanciateSimpleTextField("evaluationId"),
                        webUtil.instanciateSimpleTextField("formId"),
                        webUtil.instanciateSimpleTextField("postId"),
                        webUtil.instanciateSimpleTextField("status"),
                        webUtil.instanciateSimpleTextField("employeeName"),
                        webUtil.instanciateSimpleTextField("employeeValue"),
                        webUtil.instanciateSimpleTextField("employeeJobTitle"),
                        webUtil.instanciateSimpleTextField("cr"),
                        webUtil.instanciateSimpleTextField("administrativeUnit"),
                        webUtil.instanciateSimpleTextField("startDate"),
                        webUtil.instanciateSimpleTextField("endDate"),
                        webUtil.instanciateSimpleTextField("managerName"),
                        webUtil.instanciateSimpleTextField("managerJobTitle"),
                        webUtil.instanciateSimpleTextField("hiredDate"),
                        webUtil.instanciateSimpleTextField("evaluationDate"),
                        webUtil.instanciateSimpleTextField("gainId"),
                        webUtil.instanciateSimpleTextField("globalQuotation"),
                        webUtil.instanciateSimpleTextField("globalQuotationReason"),
                        webUtil.instanciateSimpleTextField("employeeCommentary"),
                        webUtil.instanciateSimpleTextField("year"),
                        webUtil.instanciateSimpleTextField("IsProbation"),
                        webUtil.instanciateSimpleTextField("otherEmployeeRealisation"),
                        webUtil.instanciateSimpleTextField("recommendation"),
                        webUtil.instanciateSimpleTextField("EmployeeEvaluationSignoff"),
                        webUtil.instanciateSimpleTextField("BossEvaluationSignoff"),
                        webUtil.instanciateSimpleTextField("EmployeeExpectencySignoff"),
                        webUtil.instanciateSimpleTextField("BossExpectencySignoff"),
                        webUtil.instanciateSimpleTextField("BossBossEvaluationSignoff"),
                        webUtil.instanciateCheckBoxField("isExpectationCompleted"),
                        webUtil.instanciateCheckBoxField("isClosed"),
                        webUtil.instanciateHashtableField("expectation",
                                new WebUtil.IField[] {
                                webUtil.instanciateSimpleTextField("expectedResult", "expectation_expectedResult"),
                                webUtil.instanciateSimpleTextField("appreciationElement", "expectation_appreciationElement"),
                                webUtil.instanciateSimpleTextField("achievementAssessment", "expectation_achievementAssessment")
                        }),
                        webUtil.instanciateHashtableArrayField("competences",
                                new WebUtil.IFieldArray[] {
                                webUtil.instanciateCheckBoxField("active", "competence_active", 14),
                                webUtil.instanciateSimpleTextField("commentary", "competence_commentary"),
                                webUtil.instanciateSimpleTextField("quotation", "competence_quotation")
                        })
                });
        
        if(request.getParameter("action") != null
                && request.getParameter("action").equals("save"))
        {
            try
            {
                // L'utilisateur veut sauvegarder, on crée donc un enregistrement dans les tables
                erEvaluation.setEvaluationType("PTB");
                erEvaluation.setIsExpectationCompleted(webUtil.instanciateCheckBoxField("isExpectationCompleted").getBooleanValue());
                erEvaluation.setClosed(webUtil.instanciateCheckBoxField("isClosed").getBooleanValue());
                erEvaluation.setRecommendation((String)evaluation.get("recommendation"));
                erEvaluation.setP_Job_Title_ID(Integer.parseInt((String)evaluation.get("jobTitleId")));
                erEvaluation.setP_Job_Type_ID(Integer.parseInt((String)evaluation.get("jobTypeId")));
                erEvaluation.setP_Employee_ID(Integer.parseInt((String)evaluation.get("employeeId")));
                erEvaluation.setP_Employee_Gest_ID(Integer.parseInt((String)evaluation.get("managerId")));
                erEvaluation.setP_ER_Form_ID(Integer.parseInt((String)evaluation.get("formId")));
                erEvaluation.setP_Post_ID(Integer.parseInt((String)evaluation.get("postId")));
                erEvaluation.setCR((String)evaluation.get("cr"));
                erEvaluation.setUNA((String)evaluation.get("administrativeUnit"));
                erEvaluation.setStartingRefPeriodDate(new Timestamp(PgiUtil.stringToDate((String)evaluation.get("startDate")).getTimeInMillis()));
                erEvaluation.setEndingRefPeriodDate(new Timestamp(PgiUtil.stringToDate((String)evaluation.get("endDate")).getTimeInMillis()));
                erEvaluation.setStartingWorkDate(new Timestamp(PgiUtil.stringToDate((String)evaluation.get("hiredDate")).getTimeInMillis()));
                erEvaluation.setEvaluationDate(new Timestamp(PgiUtil.stringToDate((String)evaluation.get("evaluationDate")).getTimeInMillis()));
                erEvaluation.setYear(Integer.parseInt(request.getParameter("year")));
                erEvaluation.setIsProbation("1".equals(evaluation.get("IsProbation")));
                if(((String)evaluation.get("gainId")).length() > 0)
                    erEvaluation.setP_Gain_ID(Integer.parseInt((String)evaluation.get("gainId")));
                erEvaluation.setGlobalQuotation(((String)evaluation.get("globalQuotation")).length() > 0 ? (String)evaluation.get("globalQuotation") : null);
                erEvaluation.setGlobalQuotationReason(((String)evaluation.get("globalQuotationReason")).length() > 0 ? (String)evaluation.get("globalQuotationReason") : null);
                erEvaluation.setEmployeeCommentary(((String)evaluation.get("employeeCommentary")).length() > 0 ? (String)evaluation.get("employeeCommentary") : null);
                erEvaluation.setOtherEmployeeRealisation(((String)evaluation.get("otherEmployeeRealisation")).length() > 0 ? (String)evaluation.get("otherEmployeeRealisation") : null);
                if(!((String)evaluation.get("EmployeeEvaluationSignoff")).equals(""))
                {
                	erEvaluation.setEmployeeEvaluationSignoff(new Timestamp(PgiUtil.stringToDate((String)evaluation.get("EmployeeEvaluationSignoff")).getTimeInMillis()));
                }
                if(!((String)evaluation.get("BossEvaluationSignoff")).equals(""))
                {
                	erEvaluation.setBossEvaluationSignoff(new Timestamp(PgiUtil.stringToDate((String)evaluation.get("BossEvaluationSignoff")).getTimeInMillis()));
                }
                if(!((String)evaluation.get("EmployeeExpectencySignoff")).equals(""))
                {
                	erEvaluation.setEmployeeExpectencySignoff(new Timestamp(PgiUtil.stringToDate((String)evaluation.get("EmployeeExpectencySignoff")).getTimeInMillis()));
                }
                if(!((String)evaluation.get("BossExpectencySignoff")).equals(""))
                {
                	erEvaluation.setBossExpectencySignoff(new Timestamp(PgiUtil.stringToDate((String)evaluation.get("BossExpectencySignoff")).getTimeInMillis()));
                }
                if(!((String)evaluation.get("BossBossEvaluationSignoff")).equals(""))
                {
                	erEvaluation.setBossBossEvaluationSignoff(new Timestamp(PgiUtil.stringToDate((String)evaluation.get("BossBossEvaluationSignoff")).getTimeInMillis()));
                }
                
                if(erEvaluation.save())
                {
                    DB.executeUpdate("delete from P_ER_Expectation where P_ER_Evaluation_ID = " + erEvaluation.getP_ER_Evaluation_ID(), null);
                    DB.executeUpdate("delete from P_ER_Competence where P_ER_Evaluation_ID = " + erEvaluation.getP_ER_Evaluation_ID(), null);
                    Hashtable expectation = (Hashtable)evaluation.get("expectation");
                    P_ER_Expectation erExpectation = new P_ER_Expectation(Env.getCtx(), -1, null);
                    erExpectation.setP_ER_Evaluation_ID(erEvaluation.getP_ER_Evaluation_ID());
                    erExpectation.setPNumber(0);
                    erExpectation.setExpectedresult(((String)expectation.get("expectedResult")).length() > 0 ? (String)expectation.get("expectedResult") : null);
                    erExpectation.setAppreciationElement(((String)expectation.get("appreciationElement")).length() > 0 ? (String)expectation.get("appreciationElement") : null);
                    erExpectation.setAchievementAssessment(((String)expectation.get("achievementAssessment")).length() > 0 ? (String)expectation.get("achievementAssessment") : null);
                    
                    if(!erExpectation.save())
                    {
                        System.out.println("Erreur dans la sauvegarde de l'attente");
                    }
                    
                    boolean ok = true;
                    Hashtable[] competences = (Hashtable[])evaluation.get("competences");
                    for(int i = 0; i < competences.length; i++)
                    {
                        P_ER_Competence competence = new P_ER_Competence(Env.getCtx(), -1, null);
                        competence.setP_ER_Evaluation_ID(erEvaluation.getP_ER_Evaluation_ID());
                        competence.setIsActive("checked".equals(competences[i].get("active")));
                        competence.setCommentary((String)competences[i].get("commentary"));
                        competence.setPNumber(i);
                        competence.setQuotation((String)competences[i].get("quotation"));
                        ok = ok && competence.save();
                    }
                    
                    if(!ok)
                    {
                        System.out.println("Erreur dans la sauvegarde des compétences");
                    }
                    else
                    {
                        /*if(erEvaluation.isClosed())
                        {
                            String dates[] = findDates(erEvaluation.getP_Employee_ID());
                            this.pageContext.setAttribute("evaluation", createER(erEvaluation.getP_Employee_ID(), dates[0], dates[1]));
                        }
                        else
                        {
                            this.pageContext.setAttribute("evaluation", loadER(erEvaluation.getP_ER_Evaluation_ID()));
                        }
                        return true;*/
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


			            	System.out.println("redirecting");
                        	response.sendRedirect(webAppUrl+"evaluationRendement/evalptb.jsp?evaluationId=" + erEvaluation.getP_ER_Evaluation_ID());
                        }
                        catch(Exception e)
                        {
                        	e.printStackTrace(System.out);
                        }
                        return false;
                    }
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
        
        this.pageContext.setAttribute("evaluation", evaluation);
        
        return true;
    }

}
