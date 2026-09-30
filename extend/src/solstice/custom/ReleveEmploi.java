/******************************************************************************
 * Product: Solstice+ Payroll & Human Resources Management                    *
 * Copyright (C) 2008 ProGestion Informatique, Inc. All Rights Reserved.      *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ProGestion Informatique, 210-5300 Bld des Galerie, Quebec, G2K 2A2 Canada  *
 * or via info@progestion.net or http://www.progestion.net/license.html       *
 ******************************************************************************/
package solstice.custom;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.P_Employee_Roe;
import solstice.utils.PgiUtil;

/**
 * Cette classe définit les fonctionnalités nécessaire pour créer un relevé d'emploi
 */
public class ReleveEmploi extends ValidationBase
{
    /**
     * Cette méthode vérifie si on peut afficher les résultats de la recherche
     */
    public static boolean checkResult(PageContext pageContext)
    {
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        if(request.getParameter("employeeId") != null
                && !request.getParameter("employeeId").equals(""))
        {
            return true;
        }
        return false;
    }
    
    /**
     * Cette méthode prépare les informations du relevé d'emploi
     */
    public static Hashtable<String, String> createReleveEmploi(PageContext pageContext)
    {
        // On doit d'abord vérifier si on est en consultation
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        
        if(pageContext.getAttribute("releveEmploi") != null
                && pageContext.getAttribute("releveEmploi") instanceof Hashtable)
        {
            return (Hashtable<String, String>)pageContext.getAttribute("releveEmploi");
        }
        
        if(request.getParameter("employeeRoeId") != null
                && !request.getParameter("employeeRoeId").equals(""))
        {
            // On doit récupérer les informations de la base de données
            return loadROE(Integer.parseInt(request.getParameter("employeeRoeId")));
        }
        
        if(request.getParameter("employeeId") != null
                && !request.getParameter("employeeId").equals(""))
        {
            // On crée un nouveau relevé
            HttpSession session = request.getSession();
            int userId = ((IUserInfo)session.getAttribute("userInfo")).getUserId();
            return createNew(Integer.parseInt(request.getParameter("employeeId")), userId);
        }
        
        // On renvoit à une page d'erreur
        try
        {
            ((HttpServletResponse)pageContext.getResponse()).sendError(403);
        }
        catch (IOException e)
        {}
        
        return null;
    }
    
    /**
     * Cette méthode crée un nouveau relevé d'emploi
     */
    private static Hashtable<String, String> createNew(int employeeId, int userId)
    {
        // On récupère les informations pour l'employé
        String sql
        = "select -1 as P_Employee_Roe_ID,"
            + "        employee.P_Employee_ID,"
            + "        null as OldROE,"
            + "        employee.Value,"
            + "        (select AD_Org.Description + '<br>' +"
            + "                isnull(C_Location.Address1, '') + '<br>' +"
            + "                isnull(C_Location.Address2, '') + '<br>' +"
            + "                isnull(C_Location.City, '') + ', ' + isnull(C_Region.Name, '') + ', ' + isnull(C_Country.Name, '')"
            + "           from AD_Org inner join P_Employer inner join (C_Location left join C_Country"
            + "             on C_Location.C_Country_ID = C_Country.C_Country_ID) left join C_Region"
            + "             on C_Location.C_Region_ID = C_Region.C_Region_ID"
            + "             on P_Employer.C_Location_ID = C_Location.C_Location_ID"
            + "             on AD_Org.AD_Org_ID = P_Employer.AD_Org_ID"
            + "          where P_Employer.P_Employer_ID = employer.P_Employer_ID) as EmployerAddress,"
            + "        employer.Value as CanadaRevenueBusinessNumber,"
            + "        ( select PayPeriodType + ' - ' + Name"
            + "            from P_Frequency"
            + "           where exists ( select 1"
            + "                            from P_Payment"
            + "                           where P_Employee_ID = employee.P_Employee_ID"
            + "                             and P_Frequency_ID = P_Frequency.P_Frequency_ID"
            + "                         )"
            + "        ) as Frequency,"
            + "        (select isnull(Postal, '') from C_Location where C_Location_ID = employer.C_Location_ID) as EmployerPostal,"
            + "        employee.SIN,"
            + "        employee.Name + '<br>' +"
            + "        (select isnull(C_Location.Address1, '') + '<br>' +"
            + "                isnull(C_Location.Address2, '') + '<br>' +"
            + "                isnull(C_Location.City, '') + ', ' + isnull(C_Region.Name, '') + ', ' + isnull(C_Country.Name, '') + '<br>' +"
            + "                isnull(C_Location.Postal, '')"
            + "           from (C_Location left join C_Country"
            + "             on C_Location.C_Country_ID = C_Country.C_Country_ID) left join C_Region"
            + "             on C_Location.C_Region_ID = C_Region.C_Region_ID"
            + "          where C_Location.C_Location_ID = employee.C_Location_ID) as EmployeeAddress,"
            + "        (select isnull(min(timeSheetDetail.[Day]), employee.DateHired)"
            + "           from P_Time_Sheet timeSheet inner join P_Time_Sheet_Detail timeSheetDetail"
            + "             on timeSheet.P_Time_Sheet_ID = timeSheetDetail.P_Time_Sheet_ID"
            + "          where timeSheet.P_Employee_ID = employee.P_Employee_ID"
            + "            and exists ( select 1 "
            + "                           from P_Employee_ROE"
            + "                          where P_Employee_ID = timeSheet.P_Employee_ID"
            + "                            having max(FinalPayPeriodEndingDate) < timeSheetDetail.[Day])) as FirstDayWorked,"
            + "        (select max(timeSheetDetail.[Day])"
            + "           from P_Time_Sheet timeSheet inner join P_Time_Sheet_Detail timeSheetDetail inner join P_Gain gain"
            + "             on timeSheetDetail.P_Gain_ID = gain.P_Gain_ID"
            + "             on timeSheet.P_Time_Sheet_ID = timeSheetDetail.P_Time_Sheet_ID"
            + "          where timeSheet.P_Employee_ID = employee.P_Employee_ID" 
            + "            and gain.P_Method_Gain_ID not in (102, 104, 107, 110, 113, 116, 300)) as LastDayForWhichPaid,"
            + "        (select max(period.EndDate)"
            + "           from P_Time_Sheet timeSheet inner join P_Period period"
            + "             on timeSheet.P_Period_ID = period.P_Period_ID"
            + "          where timeSheet.P_Employee_ID = employee.P_Employee_ID"
            + "            and (select max(timeSheetDetail.[Day])"
            + "           from P_Time_Sheet timeSheet inner join P_Time_Sheet_Detail timeSheetDetail inner join P_Gain gain"
            + "             on timeSheetDetail.P_Gain_ID = gain.P_Gain_ID"
            + "             on timeSheet.P_Time_Sheet_ID = timeSheetDetail.P_Time_Sheet_ID"
            + "          where timeSheet.P_Employee_ID = employee.P_Employee_ID"
            + "            and gain.P_Method_Gain_ID not in (102, 104, 107, 110, 113, 116, 300)) Between Period.StartDate and Period.EndDate " 
            + "      ) as FinalPayPeriodEndingDate, "
            + "        (select Name from P_Job_Title where P_Job_Title_ID = employee.P_Job_Title_ID) as JobTitle,"
            + "        null as ExpectedRecallCode,"
            + "        null as ExpectedDateOfRecall,"
            + "        null as TotalInsurableHours,"
            + "        null as TotalInsurableEarnings,"
            + "        null as EarningsForPayPeriod01,"
            + "        null as EarningsForPayPeriod02,"
            + "        null as EarningsForPayPeriod03,"
            + "        null as EarningsForPayPeriod04,"
            + "        null as EarningsForPayPeriod05,"
            + "        null as EarningsForPayPeriod06,"
            + "        null as EarningsForPayPeriod07,"
            + "        null as EarningsForPayPeriod08,"
            + "        null as EarningsForPayPeriod09,"
            + "        null as EarningsForPayPeriod10,"
            + "        null as EarningsForPayPeriod11,"
            + "        null as EarningsForPayPeriod12,"
            + "        null as EarningsForPayPeriod13,"
            + "        null as EarningsForPayPeriod14,"
            + "        null as EarningsForPayPeriod15,"
            + "        null as EarningsForPayPeriod16,"
            + "        null as EarningsForPayPeriod17,"
            + "        null as EarningsForPayPeriod18,"
            + "        null as EarningsForPayPeriod19,"
            + "        null as EarningsForPayPeriod20,"
            + "        null as EarningsForPayPeriod21,"
            + "        null as EarningsForPayPeriod22,"
            + "        null as EarningsForPayPeriod23,"
            + "        null as EarningsForPayPeriod24,"
            + "        null as EarningsForPayPeriod25,"
            + "        null as EarningsForPayPeriod26,"
            + "        null as EarningsForPayPeriod27,"
            + "        null as EarningsForPayPeriod28,"
            + "        null as EarningsForPayPeriod29,"
            + "        null as EarningsForPayPeriod30,"
            + "        null as EarningsForPayPeriod31,"
            + "        null as EarningsForPayPeriod32,"
            + "        null as EarningsForPayPeriod33,"
            + "        null as EarningsForPayPeriod34,"
            + "        null as EarningsForPayPeriod35,"
            + "        null as EarningsForPayPeriod36,"
            + "        null as EarningsForPayPeriod37,"
            + "        null as EarningsForPayPeriod38,"
            + "        null as EarningsForPayPeriod39,"
            + "        null as EarningsForPayPeriod40,"
            + "        null as EarningsForPayPeriod41,"
            + "        null as EarningsForPayPeriod42,"
            + "        null as EarningsForPayPeriod43,"
            + "        null as EarningsForPayPeriod44,"
            + "        null as EarningsForPayPeriod45,"
            + "        null as EarningsForPayPeriod46,"
            + "        null as EarningsForPayPeriod47,"
            + "        null as EarningsForPayPeriod48,"
            + "        null as EarningsForPayPeriod49,"
            + "        null as EarningsForPayPeriod50,"
            + "        null as EarningsForPayPeriod51,"
            + "        null as EarningsForPayPeriod52,"
            + "        null as EarningsForPayPeriod53,"
            + "        null as ReasonForIssuingThisROE,"
            + "        isnull((select Name from P_Employee where AD_User_ID = " + userId + "), '') as Contact,"
            + "        isnull(( select top 1 P_Post.Phone"
            + "                   from P_Post inner join P_Assignment inner join P_Employee"
            + "                     on P_Assignment.P_Employee_ID = P_Employee.P_Employee_ID"
            + "                     on P_Post.P_Post_ID = P_Assignment.P_Post_ID"
            + "                  where P_Assignment.AssignmentType = 'P'"
            + "                    and P_Employee.AD_User_ID = " + userId + " ), '') as ContactPhone,"
            + "        isnull(( select top 1 P_Post.PhoneExt"
            + "                   from P_Post inner join P_Assignment inner join P_Employee"
            + "                     on P_Assignment.P_Employee_ID = P_Employee.P_Employee_ID"
            + "                     on P_Post.P_Post_ID = P_Assignment.P_Post_ID"
            + "                  where P_Assignment.AssignmentType = 'P'"
            + "                    and P_Employee.AD_User_ID = " + userId + " ), '') as ContactPhoneExtension,"
            + "        null as VacationPayAmount,"
            + "        null as StatutoryHolidayPayDate01,"
            + "        null as StatutoryHolidayPayDate02,"
            + "        null as StatutoryHolidayPayDate03,"
            + "        null as StatutoryHolidayPayAmount01,"
            + "        null as StatutoryHolidayPayAmount02,"
            + "        null as StatutoryHolidayPayAmount03,"
            + "        null as OtherMoniesCode01,"
            + "        null as OtherMoniesCode02,"
            + "        null as OtherMoniesCode03,"
            + "        null as OtherMoniesAmount01,"
            + "        null as OtherMoniesAmount02,"
            + "        null as OtherMoniesAmount03,"
            + "        null as CommentsLine01,"
            + "        null as CommentsLine02,"
            + "        null as CommentsLine03,"
            + "        null as CommentsLine04,"
            + "        (select isnull(P_Language_TRL.Name, P_Language.Name)"
            + "           from P_Language left join P_Language_TRL"
            + "             on P_Language.P_Language_ID = P_Language_TRL.P_Language_ID"
            + "          where P_Language.P_Language_ID = employee.P_Language_ID"
            + "        ) as Language,"
            + "        null as PaidSickDate,"
            + "        null as PaidSickAmount,"
            + "        null as PaidSickPeriod"
            + "   from P_Employee employee inner join P_Employer employer"
            + "     on employee.P_Employer_ID = employer.P_Employer_ID"
            + "  where employee.P_Employee_ID = " + employeeId;
        
        try
        {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            Hashtable<String, String> releveEmploi = null;

            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
                releveEmploi = new Hashtable<String, String>();
                releveEmploi.put("SaveButton", "");
                releveEmploi.put("DeleteButton", "disabled");
                ResultSetMetaData rsmd = rs.getMetaData();
                for(int i = 1; i <= rsmd.getColumnCount(); i++)
                {
                    String columnName = rsmd.getColumnName(i);
                    Object value = rs.getObject(columnName);
                    
                    // S'il s'agit d'un null, on doit afficher un blanc
                    if(value == null)
                    {
                        releveEmploi.put(columnName, "&nbsp;");
                    }
                    // S'il s'agit d'une date, on doit la formatter selon jj/mm/aaaa
                    else if(value instanceof Date)
                    {
                        releveEmploi.put(columnName, dateFormatter.format(value));
                    }
                    // Sinon, on affiche la valeur en texte
                    else
                    {
                        releveEmploi.put(columnName, value.toString());
                    }
                }
            }
            
            rs.close();
            stmt.close();
            
            return releveEmploi;
        }
        catch (SQLException e)
        {
            e.printStackTrace(System.out);
        }
        
        return null;
        
    }
    
    /**
     * Cette méthode crée une table de hachage pour le relevé d'emploi passé en paramètre
     */
    private static Hashtable<String, String> loadROE(int employeeRoeId)
    {
        // On récupère les informations pour le relevé d'emploi
        String sql
        = "select employeeRoe.P_Employee_Roe_ID,"
            + "        employee.P_Employee_ID,"
            + "        null as OldROE,"
            + "        employee.Value,"
            + "        (select AD_Org.Description + '<br>' +"
            + "                isnull(C_Location.Address1, '') + '<br>' +"
            + "                isnull(C_Location.Address2, '') + '<br>' +"
            + "                isnull(C_Location.City, '') + ', ' + isnull(C_Region.Name, '') + ', ' + isnull(C_Country.Name, '')"
            + "           from AD_Org inner join P_Employer inner join (C_Location left join C_Country"
            + "             on C_Location.C_Country_ID = C_Country.C_Country_ID) left join C_Region"
            + "             on C_Location.C_Region_ID = C_Region.C_Region_ID"
            + "             on P_Employer.C_Location_ID = C_Location.C_Location_ID"
            + "             on AD_Org.AD_Org_ID = P_Employer.AD_Org_ID"
            + "          where P_Employer.P_Employer_ID = employer.P_Employer_ID) as EmployerAddress,"
            + "        employer.Value as CanadaRevenueBusinessNumber,"
            + "        ( select PayPeriodType + ' - ' + Name"
            + "            from P_Frequency"
            + "           where exists ( select 1"
            + "                            from P_Payment"
            + "                           where P_Employee_ID = employee.P_Employee_ID"
            + "                             and P_Frequency_ID = P_Frequency.P_Frequency_ID"
            + "                         )"
            + "        ) as Frequency,"
            + "        (select isnull(Postal, '') from C_Location where C_Location_ID = employer.C_Location_ID) as EmployerPostal,"
            + "        employee.SIN,"
            + "        employee.Name + '<br>' +"
            + "        (select isnull(C_Location.Address1, '') + '<br>' +"
            + "                isnull(C_Location.Address2, '') + '<br>' +"
            + "                isnull(C_Location.City, '') + ', ' + isnull(C_Region.Name, '') + ', ' + isnull(C_Country.Name, '') + '<br>' +"
            + "                isnull(C_Location.Postal, '')"
            + "           from (C_Location left join C_Country"
            + "             on C_Location.C_Country_ID = C_Country.C_Country_ID) left join C_Region"
            + "             on C_Location.C_Region_ID = C_Region.C_Region_ID"
            + "          where C_Location.C_Location_ID = employee.C_Location_ID) as EmployeeAddress,"
            + "        employeeRoe.FirstDayWorked,"
            + "        employeeRoe.LastDayForWhichPaid,"
            + "        employeeRoe.FinalPayPeriodEndingDate,"
            + "        (select Name from P_Job_Title where P_Job_Title_ID = employee.P_Job_Title_ID) as JobTitle,"
            + "        employeeRoe.ExpectedRecallCode,"
            + "        employeeRoe.ExpectedDateOfRecall,"
            + "        employeeRoe.TotalInsurableHours,"
            + "        employeeRoe.TotalInsurableEarnings,"
            + "        employeeRoe.EarningsForPayPeriod01,"
            + "        employeeRoe.EarningsForPayPeriod02,"
            + "        employeeRoe.EarningsForPayPeriod03,"
            + "        employeeRoe.EarningsForPayPeriod04,"
            + "        employeeRoe.EarningsForPayPeriod05,"
            + "        employeeRoe.EarningsForPayPeriod06,"
            + "        employeeRoe.EarningsForPayPeriod07,"
            + "        employeeRoe.EarningsForPayPeriod08,"
            + "        employeeRoe.EarningsForPayPeriod09,"
            + "        employeeRoe.EarningsForPayPeriod10,"
            + "        employeeRoe.EarningsForPayPeriod11,"
            + "        employeeRoe.EarningsForPayPeriod12,"
            + "        employeeRoe.EarningsForPayPeriod13,"
            + "        employeeRoe.EarningsForPayPeriod14,"
            + "        employeeRoe.EarningsForPayPeriod15,"
            + "        employeeRoe.EarningsForPayPeriod16,"
            + "        employeeRoe.EarningsForPayPeriod17,"
            + "        employeeRoe.EarningsForPayPeriod18,"
            + "        employeeRoe.EarningsForPayPeriod19,"
            + "        employeeRoe.EarningsForPayPeriod20,"
            + "        employeeRoe.EarningsForPayPeriod21,"
            + "        employeeRoe.EarningsForPayPeriod22,"
            + "        employeeRoe.EarningsForPayPeriod23,"
            + "        employeeRoe.EarningsForPayPeriod24,"
            + "        employeeRoe.EarningsForPayPeriod25,"
            + "        employeeRoe.EarningsForPayPeriod26,"
            + "        employeeRoe.EarningsForPayPeriod27,"
            + "        employeeRoe.EarningsForPayPeriod28,"
            + "        employeeRoe.EarningsForPayPeriod29,"
            + "        employeeRoe.EarningsForPayPeriod30,"
            + "        employeeRoe.EarningsForPayPeriod31,"
            + "        employeeRoe.EarningsForPayPeriod32,"
            + "        employeeRoe.EarningsForPayPeriod33,"
            + "        employeeRoe.EarningsForPayPeriod34,"
            + "        employeeRoe.EarningsForPayPeriod35,"
            + "        employeeRoe.EarningsForPayPeriod36,"
            + "        employeeRoe.EarningsForPayPeriod37,"
            + "        employeeRoe.EarningsForPayPeriod38,"
            + "        employeeRoe.EarningsForPayPeriod39,"
            + "        employeeRoe.EarningsForPayPeriod40,"
            + "        employeeRoe.EarningsForPayPeriod41,"
            + "        employeeRoe.EarningsForPayPeriod42,"
            + "        employeeRoe.EarningsForPayPeriod43,"
            + "        employeeRoe.EarningsForPayPeriod44,"
            + "        employeeRoe.EarningsForPayPeriod45,"
            + "        employeeRoe.EarningsForPayPeriod46,"
            + "        employeeRoe.EarningsForPayPeriod47,"
            + "        employeeRoe.EarningsForPayPeriod48,"
            + "        employeeRoe.EarningsForPayPeriod49,"
            + "        employeeRoe.EarningsForPayPeriod50,"
            + "        employeeRoe.EarningsForPayPeriod51,"
            + "        employeeRoe.EarningsForPayPeriod52,"
            + "        employeeRoe.EarningsForPayPeriod53,"
            + "        employeeRoe.ReasonForIssuingThisROE,"
            + "        isnull((select Name from P_Employee where AD_User_ID = isnull(employeeRoe.AD_User_ID, 0)), '') as Contact,"
            + "        isnull(( select P_Post.Phone"
            + "                   from P_Post inner join P_Assignment inner join P_Employee"
            + "                     on P_Assignment.P_Employee_ID = P_Employee.P_Employee_ID"
            + "                     on P_Post.P_Post_ID = P_Assignment.P_Post_ID"
            + "                  where P_Assignment.AssignmentType = 'P'"
            + "                    and P_Employee.AD_User_ID = isnull(employeeRoe.AD_User_ID, 0) ), '') as ContactPhone,"
            + "        isnull(( select P_Post.PhoneExt"
            + "                   from P_Post inner join P_Assignment inner join P_Employee"
            + "                     on P_Assignment.P_Employee_ID = P_Employee.P_Employee_ID"
            + "                     on P_Post.P_Post_ID = P_Assignment.P_Post_ID"
            + "                  where P_Assignment.AssignmentType = 'P'"
            + "                    and P_Employee.AD_User_ID = isnull(employeeRoe.AD_User_ID, 0) ), '') as ContactPhoneExtension,"
            + "        employeeRoe.VacationPayAmount,"
            + "        employeeRoe.StatutoryHolidayPayDate01,"
            + "        employeeRoe.StatutoryHolidayPayDate02,"
            + "        employeeRoe.StatutoryHolidayPayDate03,"
            + "        employeeRoe.StatutoryHolidayPayAmount01,"
            + "        employeeRoe.StatutoryHolidayPayAmount02,"
            + "        employeeRoe.StatutoryHolidayPayAmount03,"
            + "        employeeRoe.OtherMoniesCode01,"
            + "        employeeRoe.OtherMoniesCode02,"
            + "        employeeRoe.OtherMoniesCode03,"
            + "        employeeRoe.OtherMoniesAmount01,"
            + "        employeeRoe.OtherMoniesAmount02,"
            + "        employeeRoe.OtherMoniesAmount03,"
            + "        employeeRoe.CommentsLine01,"
            + "        employeeRoe.CommentsLine02,"
            + "        employeeRoe.CommentsLine03,"
            + "        employeeRoe.CommentsLine04,"
            + "        (select isnull(P_Language_TRL.Name, P_Language.Name)"
            + "           from P_Language left join P_Language_TRL"
            + "             on P_Language.P_Language_ID = P_Language_TRL.P_Language_ID"
            + "          where P_Language.P_Language_ID = employee.P_Language_ID"
            + "        ) as Language,"
            + "        employeeRoe.PaidSickDate,"
            + "        employeeRoe.PaidSickAmount,"
            + "        employeeRoe.PaidSickPeriod"
            + "   from P_Employee_Roe employeeRoe inner join P_Employee employee inner join P_Employer employer"
            + "     on employee.P_Employer_ID = employer.P_Employer_ID"
            + "     on employeeRoe.P_Employee_ID = employee.P_Employee_ID "
            + "  where employeeRoe.P_Employee_Roe_ID = " + employeeRoeId;
        
        
        try
        {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            Hashtable<String, String> releveEmploi = null;

            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
                releveEmploi = new Hashtable<String, String>();
                releveEmploi.put("SaveButton", "");
                releveEmploi.put("DeleteButton", "");
                ResultSetMetaData rsmd = rs.getMetaData();
                for(int i = 1; i <= rsmd.getColumnCount(); i++)
                {
                    String columnName = rsmd.getColumnName(i);
                    Object value = rs.getObject(columnName);
                    
                    // S'il s'agit d'un null, on doit afficher un blanc
                    if(value == null)
                    {
                        releveEmploi.put(columnName, "&nbsp;");
                    }
                    // S'il s'agit d'une date, on doit la formatter selon jj/mm/aaaa
                    else if(value instanceof Date)
                    {
                        releveEmploi.put(columnName, dateFormatter.format(value));
                    }
                    // Sinon, on affiche la valeur en texte
                    else
                    {
                        releveEmploi.put(columnName, value.toString());
                    }
                }
            }
            
            rs.close();
            stmt.close();
            
            
            return releveEmploi;
        }
        catch (SQLException e)
        {
            e.printStackTrace(System.out);
        }
        
        return null;
        
    }

    public ReleveEmploi()
    {
        super();
    }

    protected String[] doValidate()
    {
        return null;
    }

    /**
     * Traitement du dernier post
     */
    public boolean afterValidation(JspWriter out)
    {
        String action = this.request.getParameter("action");
        if(action != null)
        {
            if(action.equals("save"))
            {
                // L'utilisateur veut sauvegarder. On récupère donc les informations du dernier
                // post et on crée un nouvel enregistrement dans la table P_Employee_ROE ou
                // on le met à jour dépendemment du employeeRoeId passé en post
                P_Employee_Roe employeeRoe = new P_Employee_Roe(
                        Env.getCtx(),
                        Integer.parseInt(this.request.getParameter("employeeRoeId")), null);
                
                employeeRoe.setP_Employee_ID(Integer.parseInt(this.request.getParameter("employeeId")));
                employeeRoe.setTransfered(false);
                employeeRoe.setFirstDayWorked(this.getDate(this.request.getParameter("FirstDayWorked")));
                employeeRoe.setLastDayForWhichPaid(this.getDate(this.request.getParameter("LastDayForWhichPaid")));
                employeeRoe.setFinalPayPeriodEndingDate(this.getDate(this.request.getParameter("FinalPayPeriodEndingDate")));
                employeeRoe.setCommentsLine01(this.getString(this.request.getParameter("CommentsLine01")));
                employeeRoe.setCommentsLine02(this.getString(this.request.getParameter("CommentsLine02")));
                employeeRoe.setCommentsLine03(this.getString(this.request.getParameter("CommentsLine03")));
                employeeRoe.setCommentsLine04(this.getString(this.request.getParameter("CommentsLine04")));
                employeeRoe.setExpectedDateOfRecall(this.getDate(request.getParameter("ExpectedDateOfRecall")));
                employeeRoe.setExpectedRecallCode(this.getString(request.getParameter("ExpectedRecallCode")));
                employeeRoe.setOtherMoniesAmount01(this.getBigDecimal(request.getParameter("OtherMoniesAmount01")));
                employeeRoe.setOtherMoniesAmount02(this.getBigDecimal(request.getParameter("OtherMoniesAmount02")));
                employeeRoe.setOtherMoniesAmount03(this.getBigDecimal(request.getParameter("OtherMoniesAmount03")));
                employeeRoe.setOtherMoniesCode01(this.getString(request.getParameter("OtherMoniesCode01")));
                employeeRoe.setOtherMoniesCode02(this.getString(request.getParameter("OtherMoniesCode02")));
                employeeRoe.setOtherMoniesCode03(this.getString(request.getParameter("OtherMoniesCode03")));
                employeeRoe.setPaidSickAmount(this.getBigDecimal(request.getParameter("PaidSickAmount")));
                employeeRoe.setPaidSickDate(this.getDate(request.getParameter("PaidSickDate")));
                employeeRoe.setPaidSickPeriod(this.getString(request.getParameter("PaidSickPeriod")));
                employeeRoe.setReasonForIssuingThisRoe(this.getString(request.getParameter("ReasonForIssuingThisROE")));
                employeeRoe.setStatutoryHolidayPayAmount01(this.getBigDecimal(request.getParameter("StatutoryHolidayPayAmount01")));
                employeeRoe.setStatutoryHolidayPayAmount02(this.getBigDecimal(request.getParameter("StatutoryHolidayPayAmount02")));
                employeeRoe.setStatutoryHolidayPayAmount03(this.getBigDecimal(request.getParameter("StatutoryHolidayPayAmount03")));
                employeeRoe.setStatutoryHolidayPaydate01(this.getDate(request.getParameter("StatutoryHolidayPayDate01")));
                employeeRoe.setStatutoryHolidayPaydate02(this.getDate(request.getParameter("StatutoryHolidayPayDate02")));
                employeeRoe.setStatutoryHolidayPaydate03(this.getDate(request.getParameter("StatutoryHolidayPayDate03")));
                employeeRoe.setVacationPayAmount(this.getBigDecimal(request.getParameter("VacationPayAmount")));
                employeeRoe.setAD_User_ID(((IUserInfo)this.session.getAttribute("userInfo")).getUserId());
                
                this.findExtra(employeeRoe);
                
                if(employeeRoe.save())
                {
                    // Une fois la sauvegarde complétée, on doit rechargé le relevé d'emploi
                    Hashtable<String, String> releveEmploi = loadROE(employeeRoe.getP_Employee_Roe_ID());
                    this.pageContext.setAttribute("releveEmploi", releveEmploi, PageContext.PAGE_SCOPE);
                }
                else
                {
                    System.out.println("Erreur dans la sauvegarde de P_Employee_Roe_ID = " + this.request.getParameter("employeeRoeId"));
                }
            }
            else if(action.equals("delete"))
            {
                // L'utilisateur veut supprimer le relevé d'emploi. On récupère donc le numéro
                // du relevé et on le supprime. Par la suite, on redirige l'utilisateur vers
                // le formulaire de recherche pour l'employé courant
                int employeeRoeId = Integer.parseInt(request.getParameter("employeeRoeId"));
                DB.executeUpdate("delete from P_Employee_Roe where P_Employee_Roe_ID = " + employeeRoeId, null);
                try
                {
                    this.response.sendRedirect("index.jsp?employeeId=" + request.getParameter("employeeId"));
                }
                catch (IOException e)
                {
                    e.printStackTrace(System.out);
                }
            }
        }
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
    
    private String getString(String string)
    {
        if(string == null || string.trim().length() == 0)
            return null;
        return string.trim();
    }
    
    private BigDecimal getBigDecimal(String value)
    {
        try
        {
            // On doit supprimer les blanc
            double val = Double.parseDouble(value);
            BigDecimal amount = new BigDecimal(val);
            amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
            return amount;
        }
        catch(Exception e)
        {
            System.out.println("Erreur dans la conversion vers un BigDecimal. Valeur d'entrée : " + value);
            return null;
        }
    }

    private void findExtra(P_Employee_Roe employeeRoe)
    {
        String sql
        = "select jobTitle.Name as EmployeeOccupation,"
            + "        employer.Value as CanadaRevenueBusinessNumber"
            + "   from P_Employee employee inner join P_Employer employer"
            + "     on employee.P_Employer_ID = employer.P_Employer_ID inner join P_Job_Title jobTitle"
            + "     on employee.P_Job_Title_ID = jobTitle.P_Job_Title_ID"
            + "  where employee.P_Employee_ID = " + employeeRoe.getP_Employee_ID();
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            rs.next();
            
            employeeRoe.setCanadaRevenueBusinessNumber(rs.getString("CanadaRevenueBusinessNumber"));
            employeeRoe.setEmployeeOccupation(rs.getString("EmployeeOccupation"));
            
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace(System.out);
        }
    }
}
