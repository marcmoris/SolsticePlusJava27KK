package solstice.custom;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.P_Absence_Status;
import solstice.model.P_Employee;
import solstice.model.P_PunchCard;
import solstice.model.P_System_Parameters;
import solstice.process.PgiUtil;

/**
 * Code derrière l'interface pour remplir une demande de
 * permis horodateur dans le web
 * @author frafor01
 */
public class HorodateurRemplir extends ValidationBase
{

    public HorodateurRemplir()
    {
        super();
    }

    protected String[] doValidate() throws JspException
    {
        return null;
    }

    /**
     * Traitement des actions du post
     */
    public boolean afterValidation(JspWriter out) throws JspException
    {
        String action = this.request.getParameter("action");
        if("enregistrer".equals(action) || "demanderApprobation".equals(action))
        {
            // Enregistrement de la demande de permis
            int id = Integer.parseInt(this.request.getParameter("punchCardId"));
            int reasonId = Integer.parseInt(this.request.getParameter("reasonId"));
            int employeeId = Integer.parseInt(this.request.getParameter("employeeId"));
            Timestamp datePunchCard = dateConverter(this.request.getParameter("datePunchCard"));
            String morningStart = this.request.getParameter("morningStart");
            String morningEnd = this.request.getParameter("morningEnd");
            String afternoonStart = this.request.getParameter("afternoonStart");
            String afternoonEnd = this.request.getParameter("afternoonEnd");
            String reasonOther = this.request.getParameter("reasonOther");
            
            P_PunchCard punchCard = new P_PunchCard(Env.getCtx(), id, null);
            
            if(id < 0)
                punchCard.setP_Absence_Statut_ID(P_Absence_Status.getId("INIT"));
            
            punchCard.setP_Employee_ID(employeeId);
            punchCard.setDatePunchCard(datePunchCard);
            punchCard.setMorningStart(morningStart);
            punchCard.setMorningEnd(morningEnd);
            punchCard.setAfternoonStart(afternoonStart);
            punchCard.setAfternoonEnd(afternoonEnd);
            punchCard.setReason_Other(reasonOther);
            punchCard.setP_PunchCard_Reason_ID(reasonId);

            if("demanderApprobation".equals(action))
            {
                // On demande l'approbation du gestionnaire
                demanderApprobation(punchCard);
            }

            if(!punchCard.save())
                throw new JspException("Erreur dans la sauvegarde d'un P_PunchCard (id=" + punchCard.getP_PunchCard_ID() + ")");
        }
        else if("demanderApprobationEtEnregistrer".equals(action))
        {
            // On fait une demande d'approbation
            int id = Integer.parseInt(this.request.getParameter("punchCardId"));
            P_PunchCard punchCard = new P_PunchCard(Env.getCtx(), id, null);
            
            this.demanderApprobation(punchCard);
            
            if(!punchCard.save())
                throw new JspException("Erreur dans la sauvegarde d'un P_PunchCard (id=" + punchCard.getP_PunchCard_ID() + ")");
        }
        else if("effacer".equals(action))
        {
            // Lorsque l'utilisateur demande de supprimer une demande horodateur, on 
            // doit d'abord vérifier le statut de cette demande. Si celui-ci est à INIT ou ATTE,
            // on change simplement le statut pour DELE. Cependant, si le statut est à
            // APPR, SIGD ou  SIGN, on doit faire passer le statut à DSUP
            if(request.getParameter("punchCardId") != null 
                    && !request.getParameter("punchCardId").equals(""))
            {
                P_PunchCard punchCard = new P_PunchCard(Env.getCtx(), Integer.parseInt(request.getParameter("punchCardId")), null);
                String absenceStatus = P_Absence_Status.getValue(punchCard.getP_Absence_Statut_ID());
                if(absenceStatus.equals("INIT")
                        || absenceStatus.equals("ATTE"))
                {
                    punchCard.setP_Absence_Statut_ID(P_Absence_Status.getId("DELE"));
                }
                else if(absenceStatus.equals("APPR")
                        || absenceStatus.equals("SIGN")
                        || absenceStatus.equals("SIGD"))
                {
                    punchCard.setP_Absence_Statut_ID(P_Absence_Status.getId("DSUP"));
                }
                
                // On sauvegarde le nouveau statut
                if(!punchCard.save())
                    throw new JspException("Erreur dans la sauvegarde d'un P_PunchCard (id=" + punchCard.getP_PunchCard_ID() + ")");
            }
        }
        return true;
    }
    
    /**
     * Envoit un courriel pour une demande d'approbation d'un permis horodateur
     * @param punchCard
     */
    private void demanderApprobation(P_PunchCard punchCard)
    {
        punchCard.setP_Absence_Statut_ID(P_Absence_Status.getId("ATTE"));
        
        String message = this.prepareMessage( punchCard);
        int managerId = this.getManager(punchCard.getP_Employee_ID());
        
        try
        {
            EMailSender sender = new EMailSender(managerId, PgiUtil.getSendEmailAddress( "permis.horodateur"), Env.getCtx());
            sender.sendEMail("Demande de permis horodateur", message);
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
        }
    }

    /**
     * Retourne le gestionnaire de l'employé passé en paramètre
     * @param employeeId
     * @return
     */
    private int getManager(int employeeId)
    {
        int id = 0;
        String sql
        = "select P_Distribution_Booklet.P_Employee_ID"
            + " from P_Employee inner join P_Distribution_Booklet"
            + " on P_Employee.P_Distribution_Booklet_ID = P_Distribution_Booklet.P_Distribution_Booklet_ID"
            + " where P_Employee.P_Employee_ID = " + employeeId;
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
                id = rs.getInt("P_Employee_ID");
            }
            
            rs.close();
            stmt.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace(System.out);
        }
        
        return id;
    }
    
    /**
     * Prépare le e-mail selon les informations de la demande de permis horodateur
     */
    private String prepareMessage(P_PunchCard punchCard)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String employeeName = null;
        String datePunchCard = format.format(punchCard.getDatePunchCard());
        String webAppUrl = null;
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(
                    "select FirstName + ' ' + SurName as Nom" +
                    "  from P_Employee" +
                    " where P_Employee_ID = " + punchCard.getP_Employee_ID(), null);
            
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
                employeeName = rs.getString("Nom");
            }
            
            rs.close();
            stmt.close();
            
            webAppUrl = (String)P_System_Parameters.getParameterValue("WEBAPPURL");
        }
        catch (SQLException e)
        {
            e.printStackTrace(System.out);
        }
        
        P_Employee manager = P_Employee.get( Env.getCtx(), this.getManager(punchCard.getP_Employee_ID()), null );
        
        return (employeeName + " a effectué(e) une demande de permis horodateur. <br><br>"
                + " Date : " + datePunchCard
                + " <br><br>Veuillez accéder à l'application des permis horodateurs pour approuver ou refuser cette demande."
                + " <br><br><a href=\"" + webAppUrl + "login.jsp?user="+manager.getUserCode()+"&redirection=%2FpermisHorodateur%2Findex.jsp\">"
                + " Cliquer ici pour accéder à l'application des permis horodateurs</a>");
    }
}
