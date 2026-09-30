package solstice.custom;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.P_Absence_Status;
import solstice.model.P_PunchCard;
import solstice.model.P_System_Parameters;

/**
 * Class de validation pour l'interface de consultation des permis horodateur
 * pour la formule administration. Cette class permet donc d'appliquer les
 * autorisation selon les permissions des utilisateurs connectés.
 * @author frafor01
 */
public class HorodateurConsulter extends ValidationBase
{

    public HorodateurConsulter()
    {
        super();
    }

    protected String[] doValidate() throws JspException
    {
        return null;
    }

    public boolean afterValidation(JspWriter out) throws JspException
    {
        String action = this.request.getParameter("action");
        // Si l'utilisateur a choisi d'accepter une demande d'autorisation
        // d'absence, on doit d'abord vérifier le rôle de l'utilisateur. Dans
        // le cas d'un signataire, on doit faire passer le statut à SIGN. Dans
        // le cas d'un délégué, on le fait passer à SIGD. Lorsqu'il s'agit
        // d'un approbateur, on le fait passer à APPR ou SIGD dépendemment
        // du niveau d'approbation
        if("accept".equals(action))
        {
            int punchCardId = Integer.parseInt(request.getParameter("punchCardId"));
            int statusId = 0;
            P_PunchCard punchCard = new P_PunchCard(Env.getCtx(), punchCardId, null);
            
            // S'il s'agit d'un administrateur ou d'un gestionnaire
            if(loggedUserHasPolicy(pageContext,"ph_approve"))
            {
                statusId = P_Absence_Status.getId("SIGN");
            }
            // S'il s'agit d'une secrétaire ou d'un délégué
            else if(loggedUserHasPolicy(pageContext,"ph_approveByDelegate"))
            {
                statusId = P_Absence_Status.getId("SIGD");
            }
            // Sinon, il s'agit d'un approbateur coordonnateur
            else if(loggedUserHasPolicy(pageContext,"ph_approvePartially"))
            {
                statusId = P_Absence_Status.getId("APPR");
            }
            
            punchCard.setP_Absence_Statut_ID(statusId);
            if(!punchCard.save())
                throw new JspException("Erreur dans la sauvegarde d'un P_PunchCard (id=" + punchCard.getP_PunchCard_ID() + ")");
            
            envoyerMessage(punchCard, "approuvé");
        }
        // Si l'utilisateur a choisi de refuser une demande d'autorisation
        // d'absence, on doit faire passer le statut à REFU
        else if("refus".equals(action))
        {
            int punchCardId = Integer.parseInt(request.getParameter("punchCardId"));
            P_PunchCard punchCard = new P_PunchCard(Env.getCtx(), punchCardId, null);
            punchCard.setP_Absence_Statut_ID(P_Absence_Status.getId("REFU"));
            if(!punchCard.save())
                throw new JspException("Erreur dans la sauvegarde d'un P_PunchCard (id=" + punchCard.getP_PunchCard_ID() + ")");

            envoyerMessage(punchCard, "refusé");
        }
        // Si l'utilisateur a choisi de supprimer, on doit faire passer
        // le statut de DSUP à DELE
        else if("delete".equals(action))
        {
            int punchCardId = Integer.parseInt(request.getParameter("punchCardId"));
            P_PunchCard punchCard = new P_PunchCard(Env.getCtx(), punchCardId, null);
            punchCard.setP_Absence_Statut_ID(P_Absence_Status.getId("DELE"));
            if(!punchCard.save())
                throw new JspException("Erreur dans la sauvegarde d'un P_PunchCard (id=" + punchCard.getP_PunchCard_ID() + ")");
            
            envoyerMessage(punchCard, "supprimé");
        }
        return true;
    }

    /**
     * Permet d'obtenir le cas du switch case sql qui permet d'approuver
     * @return le sql
     */
    public static String peutApprouver (PageContext pageContext)
    {
        String sql = "";
        if (loggedUserHasPolicy(pageContext,"ph_approve"))
        {
            sql += " when 'ATTE' then punchCard.P_PunchCard_ID ";
        }
        else if (loggedUserHasPolicy(pageContext,"ph_approvePartially"))
        {
            sql += " when 'ATTE' then punchCard.P_PunchCard_ID ";
        }
        else if (loggedUserHasPolicy(pageContext,"ph_approveByDelegate"))
        {
            sql += " when 'ATTE' then punchCard.P_PunchCard_ID ";
        }
        if (loggedUserHasPolicy(pageContext,"ph_approveApprovedByDelegate"))
        {
            sql += " when 'SIGD' then punchCard.P_PunchCard_ID ";
        }
        if (loggedUserHasPolicy(pageContext,"ph_approveApprovedPartially"))
        {
            sql += " when 'APPR' then punchCard.P_PunchCard_ID ";
        }
        if(sql.equals(""))
            sql += " when 'ZZZZ' then '' ";
        return sql;
    }
    
    /**
     * Permet d'obtenir le cas du switch case sql qui permet de refuser
     * @return le sql
     */
    public static String peutRefuser (PageContext pageContext)
    {
        String sql = "";
        if (loggedUserHasPolicy(pageContext,"ph_refuseAbsence"))
        {
            sql += " when 'ATTE' then punchCard.P_PunchCard_ID";
        }
        if (loggedUserHasPolicy(pageContext,"ph_refuseApprovedPartially"))
        {
            sql += " when 'APPR' then punchCard.P_PunchCard_ID";
        }
        if(sql.equals(""))
            sql += " when 'ZZZZ' then '' ";

        return sql;
   }
    
    /**
     * Permet d'obtenir le cas du switch case sql qui permet de supprimer
     * @return le sql
     */
    public static String peutSupprimer (PageContext pageContext)
    {
        String sql = "";
        if (loggedUserHasPolicy(pageContext,"ph_deleteAbsence"))
        {
            sql += " when 'ATTE' then punchCard.P_PunchCard_ID";
        }
        if (loggedUserHasPolicy(pageContext,"ph_askApprovedPartiallyDeletion"))
        {
            sql += " when 'APPR' then punchCard.P_PunchCard_ID";
        }
        if (loggedUserHasPolicy(pageContext,"ph_askApprovedDeletion"))
        {
            sql += " when 'SIGN' then punchCard.P_PunchCard_ID";
        }
        if (loggedUserHasPolicy(pageContext,"ph_deleteAskDeletion"))
        {
            sql += " when 'DSUP' then punchCard.P_PunchCard_ID";
        }
        if(sql.equals(""))
            sql += " when 'ZZZZ' then '' ";

        return sql;
   }
    
    /**
     * Envoie le e-mail et prépare le message
     */
    private void envoyerMessage (P_PunchCard punchCard, String qualificatif)
    {
        try
        {
            String message = this.prepareMessage(punchCard.getDatePunchCard(),qualificatif);
            EMailSender sender = new EMailSender(punchCard.getP_Employee_ID(), Env.getCtx());
            sender.sendEMail("Permis horodateur", message);
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
        }
    }
    /**
     * Prépare le e-mail selon les informations de la demande d'autorisation d'absence
     * en qualifiant le message d'approuvé, refusé ou supprimé
     */
    private String prepareMessage(Timestamp date, String qualificatif) throws SQLException
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String punchDate = format.format(date);
        String strUserCode = "";
        String webAppUrl = (String)P_System_Parameters.getParameterValue("WEBAPPURL");

        return (getEmployeeName(((IUserInfo)session.getAttribute("userInfo")).getEmployeeId())+ " a "+qualificatif+" la demande de permis horodateur. <br><br>"
                + " Date : " + punchDate
                + " <br><br><a href=\"" + webAppUrl + "login.jsp?user="+strUserCode+"&redirection=%2FautorisationAbsence%2Findex.jsp\">"
                + " Cliquer ici pour accéder à l'application d'autorisation d'absence</a>");

    }
    
    private String getEmployeeName (int noEmploye)
    {
        // On récupère le nom de l'employé
        String sql
        = "select employee.FirstName, SurName"
            + " from P_Employee employee"
            + " where employee.p_employee_id = "+noEmploye;
        String employeeName = null;
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            {
                employeeName = rs.getString("FirstName")+" "+rs.getString("SurName");
            }
            
            rs.close();
            stmt.close();
            
        }
        catch (SQLException e)
        {
            e.printStackTrace(System.out);
        }
        return employeeName;
    }

}
