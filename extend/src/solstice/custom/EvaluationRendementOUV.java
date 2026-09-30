package solstice.custom;

import java.io.IOException;
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
import solstice.process.PgiUtil;
 
public class EvaluationRendementOUV extends ValidationBase
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
        sql += "    and EvaluationType = 'OUV'";
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
            + "    and EvaluationType = 'OUV'"
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
        int iYear = calendar.get(Calendar.YEAR);
        calendar.add(Calendar.YEAR, 1);
        //+1 an - 1 jour
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
            //+ "        isnull(evaluation.EmployeeExpectencySignoff, '') as EmployeeExpectencySignoff, "
            //+ "        isnull(evaluation.BossExpectencySignoff, '') as BossExpectencySignoff, "
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
            + "        (case evaluation.IsExpectationCompleted when 'Y' then 'checked' else '' end) as isExpectationCompleted,"
            + "        evaluation.GlobalQuotation as globalQuotation,"
            + "        evaluation.GlobalQuotationReason as globalQuotationReason,"
            + "        evaluation.[Year] as \"year\","
            + "        case evaluation.IsProbation when 'Y' then '1' else '0' end as IsProbation,"
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
        
        Hashtable<String,Object>[] competences = new Hashtable[12];
        sql = " select isnull(Commentary, '') as Commentary,"
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
                competences[i] = new Hashtable<String,Object>();
                competences[i].put("commentary", rs.getString("commentary"));
                competences[i].put("quotation", rs.getString("Quotation"));
                i++;
            }
            
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            throw new JspException(e);
        }
        evaluation.put("competences", competences);
        
        return evaluation;
    }
    
    private static Hashtable createER(int employeeId, String startDate, String endDate, String strYear, boolean probation) throws Exception
    {
        Hashtable<String,Object> evaluation = new Hashtable<String,Object>();
        
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        // On doit d'abord obtenir le formulaire approprié
        int formId;
        String sql
        = "select top 1 P_ER_Form_ID"
            + "    from P_ER_Form"
            + "   where FormType = 'OUV'"
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
        evaluation.put("gainId", "");
        evaluation.put("GainName", "");
        evaluation.put("isCompleted", "");
        evaluation.put("recommendation", "");
        evaluation.put("isExpectationCompleted", "");
        evaluation.put("year", strYear);
        evaluation.put("EmployeeEvaluationSignoff", "");
        evaluation.put("BossEvaluationSignoff", "");
        //evaluation.put("EmployeeExpectencySignoff", "");
        //evaluation.put("BossExpectencySignoff", "");
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
        	System.out.println(sql);
        	e.printStackTrace();
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
        
        Hashtable<String,Object>[] competences = new Hashtable[12];
        for(int i = 0; i < 12; i++)
        {
            competences[i] = new Hashtable<String,Object>();
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

    public EvaluationRendementOUV()
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
                response.sendRedirect(webAppUrl+"evaluationRendement/evalout.jsp?evaluationId=" + erEvaluation.getP_ER_Evaluation_ID());
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
                        webUtil.instanciateSimpleTextField("recommendation"),
                        webUtil.instanciateSimpleTextField("year"),
                        webUtil.instanciateSimpleTextField("IsProbation"),
                        webUtil.instanciateSimpleTextField("EmployeeEvaluationSignoff"),
                        webUtil.instanciateSimpleTextField("BossEvaluationSignoff"),
                        webUtil.instanciateSimpleTextField("BossBossEvaluationSignoff"),
                        webUtil.instanciateCheckBoxField("isExpectationCompleted"),
                        webUtil.instanciateCheckBoxField("isClosed"),
                        webUtil.instanciateHashtableArrayField("competences",
                                new WebUtil.IFieldArray[] {
                                webUtil.instanciateIndexedField("quotation", "competence_quotation", 12),
                                webUtil.instanciateSimpleTextField("commentary", "competence_commentary")
                        })
                });

        if(request.getParameter("action") != null
                && request.getParameter("action").equals("save"))
        {
            try
            {
                // L'utilisateur veut sauvegarder, on crée donc un enregistrement dans les tables
                erEvaluation.setEvaluationType("OUV");
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
                if(!((String)evaluation.get("EmployeeEvaluationSignoff")).equals(""))
                {
                	erEvaluation.setEmployeeEvaluationSignoff(new Timestamp(PgiUtil.stringToDate((String)evaluation.get("EmployeeEvaluationSignoff")).getTimeInMillis()));
                }
                if(!((String)evaluation.get("BossEvaluationSignoff")).equals(""))
                {
                	erEvaluation.setBossEvaluationSignoff(new Timestamp(PgiUtil.stringToDate((String)evaluation.get("BossEvaluationSignoff")).getTimeInMillis()));
                }
                //if(!((String)evaluation.get("EmployeeExpectencySignoff")).equals(""))
                //{
                //	erEvaluation.setEmployeeExpectencySignoff(new Timestamp(PgiUtil.stringToDate((String)evaluation.get("EmployeeExpectencySignoff")).getTimeInMillis()));
                //}
                //if(!((String)evaluation.get("BossExpectencySignoff")).equals(""))
                //{
                //	erEvaluation.setBossExpectencySignoff(new Timestamp(PgiUtil.stringToDate((String)evaluation.get("BossExpectencySignoff")).getTimeInMillis()));
                //}
                if(!((String)evaluation.get("BossBossEvaluationSignoff")).equals(""))
                {
                	erEvaluation.setBossBossEvaluationSignoff(new Timestamp(PgiUtil.stringToDate((String)evaluation.get("BossBossEvaluationSignoff")).getTimeInMillis()));
                }
                
                if(erEvaluation.save())
                {
                    DB.executeUpdate("delete from P_ER_Competence where P_ER_Evaluation_ID = " + erEvaluation.getP_ER_Evaluation_ID(), null);
                    
                    boolean ok = true;
                    Hashtable[] competences = (Hashtable[])evaluation.get("competences");
                    for(int i = 0; i < competences.length; i++)
                    {
                        P_ER_Competence competence = new P_ER_Competence(Env.getCtx(), -1, null);
                        competence.setP_ER_Evaluation_ID(erEvaluation.getP_ER_Evaluation_ID());
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

                        	response.sendRedirect(webAppUrl+"evaluationRendement/evalout.jsp?evaluationId=" + erEvaluation.getP_ER_Evaluation_ID());
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
                System.out.println("Le Exception");
                e.printStackTrace(System.out);
            }
        }
        
        this.pageContext.setAttribute("evaluation", evaluation);
        
        return true;
    }

    /**
     * Cette méthode génère le tableau de la section 2
     */
    public static void generateTable(Hashtable evaluation, JspWriter out, boolean isReadOnly) throws SQLException, IOException
    {
        // La manière dont la table est sérialisée dans la base de données, c'est
        // qu'on a quatre enregistrements consécutifs de détail pour un enregistrement
        // d'entête. On doit donc sortir la liste des entêtes.
        String sql;
        sql = "   select PNumber,"
            + "          IsNull(Description, '') as Description "
            + "     from P_ER_Form_Struct"
            + "    where FieldType = 'rendement'"
            + "      and P_ER_Form_ID = " + (String)evaluation.get("formId")
            + " order by PNumber";
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        out.println("<table width=\"100%\" border=\"1\" bordercolor=\"#00000\" cellspacing=\"0\" cellpadding=\"0\">");
        out.println("<tr>");
        
        Hashtable[] competences = (Hashtable[])evaluation.get("competences");
        
        while(rs.next())
        {
            int index = rs.getInt("PNumber");
            
            // On doit dessiner un tableau par section
            out.println("<td width=\"50%\">");
            out.println("<table width=\"100%\" cellspacing=\"0\" cellpadding=\"2\">");
            out.println("<tr>");
            out.println("<td colspan=\"2\" class=\"boldtext\" align=\"center\">");
            out.println(rs.getString("Description"));
            out.println("</td>");
            out.println("</tr>");
            
            // On doit maintenant afficher les 4 lignes de détail
            generateDetail((String)evaluation.get("formId"), competences, index, out, isReadOnly);
            
            String strLock = "";
            
            if(isReadOnly)
            	strLock = "readonly=\"readonly\"";
            out.println("<tr><td colspan=\"2\">");
            out.println("<textarea rows=\"9\" cols=\"43\" name=\"competence_commentary\" " +strLock+ " >" + competences[index].get("commentary") + "</textarea>");
            out.println("</td></tr>");
            out.println("</table>");
            out.println("</td>");
            
            // Si on arrive sur un index imper, on doit ajouter une séparation de
            // ligne dans le tableau
            if(index % 2 == 1)
            {
                out.println("</tr><tr>");
            }
        }
        
        out.println("</tr>");
        out.println("</table>");
        
        rs.close();
        stmt.close();
    }
    
    /**
     * Cette méthode génère le détail du tableau de la section 2
     */
    private static void generateDetail(String formId, Hashtable[] competences, int index, JspWriter out, boolean isReadOnly) throws SQLException, IOException
    {
        // On sait qu'il y a quatre enregistrement de détail pour un enregistrement
        // d'entête. On doit donc faire un top 4 pour l'index passé en paramètre
        String sql;
        sql = "   select top 4"
            + "          ( case when Description is null or Description like '' then ';visibility: hidden'"
            + "                 else                                                 ''"
            + "            end ) as Visibility,"
            + "          PNumber,"
            + "          Description"
            + "     from P_ER_Form_Struct"
            + "    where FieldType = 'rendementDetail'"
            + "      and P_ER_Form_ID = " + formId
            + "      and PNumber >= " + index + " * 4"
            + " order by PNumber";
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            String checked = "";
            if(rs.getString("PNumber").trim().equals(((String)competences[index].get("quotation")).trim()))
            {
                checked = "checked";
            }
            
            String strLock = "";
            if(isReadOnly)
            	strLock = "Disabled=\"disabled\"";
            out.println("<tr><td valign=\"top\">");
            out.println("<div style=\"height: 50px; width: 10px" + rs.getString("Visibility") + "\">");
            out.println("<input type=\"radio\" value=\"" + rs.getInt("PNumber") + "\" name=\"competence_quotation_" + index + "\" " + checked + " " + strLock + " >");
            out.println("</div>");
            out.println("</td><td align=\"left\" width=\"97%\">");
            out.println("<div style=\"height: 50px;text-align" + rs.getString("Visibility") + "\">");
            out.println(rs.getString("Description"));
            out.println("</div>");
            out.println("</td></tr>");
        }
        
        rs.close();
        stmt.close();
    }
    
}
