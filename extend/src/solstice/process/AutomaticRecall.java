package solstice.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.custom.EMailSender;

/**
 * Procédure de rappel automatique.
 * 
 * On doit vérifier s'il y a des employés qui doivent être réévalués
 * prochainement selon certains critères de catégorisation. Si c'est
 * le cas, on envoit un courriel aux gestionnaires concernés.
 * 
 * @author frafor01
 */
public class AutomaticRecall extends Thread
{
    
    /*
     * members
     */
    private String lastLaunch = null;
    private String newHeader = null;
    private String newFooter = null;
    private String newCol1 = null;
    private String newCol2 = null;
    private String transfertHeader = null;
    private String transfertFooter = null;
    private String transfertCol1 = null;
    private String transfertCol2 = null;
    private String transfertCol3 = null;
    private String noticeHeader = null;
    private String noticeFooter = null;
    private String noticeCol1 = null;
    private String noticeCol2 = null;
    private String noticeCol3 = null;
    private String noticeCol4 = null;
    private String recallHeader = null;
    private String recallFooter = null;
    private String recallCol1 = null;
    private String recallCol2 = null;
    private String recallCol3 = null;
    private String recallCol4 = null;
    
    /**
     * Constructeur
     */
    public AutomaticRecall()
    {
    }
    
    /**
     * Traitement principal.
     * 
     * Cette méthode sert à lancer la procédure de rappel automatique
     * si on est rendu à la journée du mois spécifiée dans le paramètre
     * web.
     */
    public void run()
    {
        while(true)
        {
            // Si on n'a pas déjà lancé la procédure aujourd'hui
            if(!this.alreadyLaunchToday())
            {
                // Si on est rendu à la journée du mois où on doit
                // exécuter la procédure
                if(this.isExecutionDay())
                {
                    // On lance le traitement et on mémorise la date
                    // du dernier lancement pour éviter de faire le
                    // traitement plus d'une fois dans la journée
                    try
                    {
                        this.initLabels();
                        this.processRecall();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                        Calendar cal = GregorianCalendar.getInstance();
                        Timestamp now = new Timestamp(cal.getTimeInMillis());
                        this.lastLaunch = formatter.format(now);
                    }
                    catch (SQLException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            
            // On fait attendre le processus pour 10 minute
            try
            {
                Thread.sleep(600000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Cette méthode vérifie si la procédure a déjà été exécutée aujourd'hui
     * @return
     */
    private boolean alreadyLaunchToday()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = GregorianCalendar.getInstance();
        Timestamp now = new Timestamp(cal.getTimeInMillis());
        String launchingDay = formatter.format(now);
        return launchingDay.equals(this.lastLaunch);
    }
    
    /**
     * Cette méthode vérifie si c'est le jour de l'exécution de la procédure
     * @return
     */
    private boolean isExecutionDay()
    {
        boolean executionDay = false;
        try
        {
            // Cette requête sort la journée du mois où il faut lancer la
            // procédure. Si la journée retournée est 0, on considère qu'il
            // s'agit de la dernière journée du mois. Si la requête ne sort
            // aucun enregistrement, c'est que le paramètre n'a pas été
            // défini et donc on ne peut pas lancer la procédure.
            PreparedStatement stmt = DB.prepareStatement("select DayOfMonth from P_ER_Parameter", null);
            
            ResultSet rs = stmt.executeQuery();

            if(rs.next())
            {
                int dayOfMonth = rs.getInt("DayOfMonth");
                Calendar today = GregorianCalendar.getInstance();
                if(dayOfMonth == 0)
                {
                    int lastDayOfMonth;
                    switch(today.get(Calendar.MONTH))
                    {
                        case Calendar.FEBRUARY:
                        {
                            lastDayOfMonth = today.get(Calendar.YEAR) % 4 == 0 ? 29 : 28;
                            break;
                        }
                        case Calendar.APRIL: case Calendar.JUNE: 
                        case Calendar.SEPTEMBER: case Calendar.NOVEMBER:
                        {
                            lastDayOfMonth = 30;
                            break;
                        }
                        default:
                        {
                            lastDayOfMonth = 31;
                            break;
                        }
                    }
                    executionDay = lastDayOfMonth == today.get(Calendar.DAY_OF_MONTH);
                }
                else
                {
                    executionDay = today.get(Calendar.DAY_OF_MONTH) == dayOfMonth;
                }
            }
            
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return executionDay;
    }
    
    /**
     * Procédure de rappel automatique.
     *
     * On doit sortir la liste des employés qui doivent être évalués
     * au cours du prochain mois. On les classes par gestionnaires et
     * ensuite par catégories. Finalement on envoit un courriel aux
     * gestionnaires concernés.
     */
    private void processRecall() throws SQLException
    {
        // Cette requête sort la liste de tous les employés à réévalués.
        // La liste est triée par gestionnaire et par catégorie de rappel.
        PreparedStatement stmt = DB.prepareStatement(
                "select P_Employee.Surname," +
                "       P_Employee.Firstname," +
                "       P_Employee.Value," +
                "       RV_Automatic_Recall.Gestionnaire_ID," +
                "       RV_Automatic_Recall.TypeRappel," +
                "       RV_Automatic_Recall.Colonne1," +
                "       RV_Automatic_Recall.Colonne2," +
                "       RV_Automatic_Recall.Colonne3," +
                "       RV_Automatic_Recall.Colonne4" +
                "  from RV_Automatic_Recall" +
                "       inner join P_Employee" +
                "               on P_Employee.P_Employee_ID = RV_Automatic_Recall.P_Employee_ID" +
                " order by RV_Automatic_Recall.Gestionnaire_ID," +
                "          RV_Automatic_Recall.TypeRappel", null);
        
        ResultSet rs = stmt.executeQuery();
        
        int currentManagerId = 0;
        String currentType = "";
        StringBuffer message = new StringBuffer();
        
        while(rs.next())
        {
            // Si on a changé de gestionnaire depuis le dernier tour de boucle,
            // on envoit le message dans le buffer dernier gestionnaire et
            // on change de gestionnaire courant.
            if(currentManagerId != rs.getInt("Gestionnaire_ID"))
            {
               if(currentManagerId != 0)
               {
                   this.writeFooter(currentType, message);
                   this.sendEMail(currentManagerId, message);
                   message.delete(0, message.length());
               }
               currentManagerId = rs.getInt("Gestionnaire_ID");
               currentType = "";
            }
            
            // Si le type a changé depuis de dernier tour de boucle, on
            // doit écrire le pied de page du dernier type et écrire
            // l'entête du nouveau type.
            if(!currentType.equals(rs.getString("TypeRappel")))
            {
                if(!currentType.equals(""))
                    this.writeFooter(currentType, message);
                currentType = rs.getString("TypeRappel");
                this.writeHeader(currentType, message);
            }
            
            // On ajoute maitenant l'employé dans le tableau
            this.writeLine(rs, message);
        }
        
        // En sortant de la boucle, on doit envoyer le dernier courriel
        // qui doit être encore en mémoire (s'il y a lieu)
        if(currentManagerId != 0)
        {
            this.writeFooter(currentType, message);
            this.sendEMail(currentManagerId, message);
        }
        
    }
    
    /**
     * Cette méthode envoie le courriel au gestionnaire
     */
    private void sendEMail(int managerId, StringBuffer message)
    {
        try
        {
            EMailSender sender = new EMailSender(managerId, PgiUtil.getSendEmailAddress( "drh"), Env.getCtx() );
            sender.sendEMail("Évaluations du rendement de votre personnel", message.toString());
            
            // Pour être sûr que chaque envoi de courriel se fasse correctement et
            // ne bloque pas le smpt, on ajoute une pause de deux secondes.
            Thread.sleep(2000);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Cette méthode écrit l'entête de la catégorie de rappel spécifiée
     */
    private void writeHeader(String type, StringBuffer message)
    {
        if("A".equals(type))
        {
            // Nouvel(le) employé(e)
            message.append(
                    "<div style=\"border: 2px solid #000000\">\r\n" +
                    "<p align=\"center\" style=\"font-weight: bold; text-decoration: underline\">\r\n" +
                    "Nouvel(le) employé(e)\r\n" +
                    "</p>\r\n" +
                    "<p>" + this.newHeader + "</p>\r\n" +
                    "<table width=\"95%\" border=\"0\" cellspacing=\"0\" cellpadding=\"2\">\r\n" +
                    "<tr>\r\n" +
                    "<th width=\"50%\">&nbsp;</th>\r\n" +
                    "<th>" + this.newCol1 + "</th>\r\n" +
                    "<th>" + this.newCol2 + "</th>\r\n" +
                    "</tr>\r\n");
        }
        else if("B".equals(type))
        {
            // Transfert d'unité administrative
            message.append(
                    "<div style=\"border: 2px solid #000000\">\r\n" +
                    "<p align=\"center\" style=\"font-weight: bold; text-decoration: underline\">\r\n" +
                    "Transfert d'unité administrative\r\n" +
                    "</p>\r\n" +
                    "<p>" + this.transfertHeader + "</p>\r\n" +
                    "<table width=\"95%\" border=\"0\" cellspacing=\"0\" cellpadding=\"2\">\r\n" +
                    "<tr>\r\n" +
                    "<th width=\"50%\">&nbsp;</th>\r\n" +
                    "<th>" + this.transfertCol1 + "</th>\r\n" +
                    "<th>" + this.transfertCol2 + "</th>\r\n" +
                    "<th>" + this.transfertCol3 + "</th>\r\n" +
                    "</tr>\r\n");
        }
        else if("C".equals(type))
        {
            // Avis
            message.append(
                    "<div style=\"border: 2px solid #000000\">\r\n" +
                    "<p align=\"center\" style=\"font-weight: bold; text-decoration: underline\">\r\n" +
                    "Avis\r\n" +
                    "</p>\r\n" +
                    "<p>" + this.noticeHeader + "</p>\r\n" +
                    "<table width=\"95%\" border=\"0\" cellspacing=\"0\" cellpadding=\"2\">\r\n" +
                    "<tr>\r\n" +
                    "<th width=\"50%\">&nbsp;</th>\r\n" +
                    "<th>" + this.noticeCol1 + "</th>\r\n" +
                    "<th>" + this.noticeCol2 + "</th>\r\n" +
                    "<th>" + this.noticeCol3 + "</th>\r\n" +
                    "<th>" + this.noticeCol4 + "</th>\r\n" +
                    "</tr>\r\n");
        }
        else
        {
            // Rappel
            message.append(
                    "<div style=\"border: 2px solid #000000\">\r\n" +
                    "<p align=\"center\" style=\"font-weight: bold; text-decoration: underline\">\r\n" +
                    "Rappel\r\n" +
                    "</p>\r\n" +
                    "<p>" + this.recallHeader + "</p>\r\n" +
                    "<table width=\"95%\" border=\"0\" cellspacing=\"0\" cellpadding=\"2\">\r\n" +
                    "<tr>\r\n" +
                    "<th width=\"50%\">&nbsp;</th>\r\n" +
                    "<th>" + this.recallCol1 + "</th>\r\n" +
                    "<th>" + this.recallCol2 + "</th>\r\n" +
                    "<th>" + this.recallCol3 + "</th>\r\n" +
                    "<th>" + this.recallCol4 + "</th>\r\n" +
                    "</tr>\r\n");
        }
    }
    
    /**
     * Cette méthode écrit le pied de page de la catégorie de rappel spécifiée
     */
    private void writeFooter(String type, StringBuffer message)
    {
        if("A".equals(type))
        {
            // Nouvel(le) employé(e)
            message.append(
                    "</table>\r\n" +
                    "<p>" + this.newFooter.replaceAll("\r\n", "<br />") + "</p>\r\n" +
                    "</div><br />\r\n");
        }
        else if("B".equals(type))
        {
            // Transfert d'unité administrative
            message.append(
                    "</table>\r\n" +
                    "<p>" + this.transfertFooter.replaceAll("\r\n", "<br />") + "</p>\r\n" +
                    "</div><br />\r\n");
        }
        else if("C".equals(type))
        {
            // Avis
            message.append(
                    "</table>\r\n" +
                    "<p>" + this.noticeFooter.replaceAll("\r\n", "<br />") + "</p>\r\n" +
                    "</div><br />\r\n");
        }
        else
        {
            // Rappel
            message.append(
                    "</table>\r\n" +
                    "<p>" + this.recallFooter.replaceAll("\r\n", "<br />") + "</p>\r\n" +
                    "</div><br />\r\n");
        }
    }

    /**
     * Cette méthode ajoute une ligne dans le tableau des employés selon
     * la catégorie de rappel utilisée.
     */
    private void writeLine(ResultSet rs, StringBuffer message) throws SQLException
    {
        message.append("<tr>\r\n");
        message.append("<td>" + rs.getString("Surname") + ", " + rs.getString("Firstname") + " (" + rs.getString("Value") + ")" + "</td>\r\n");
        
        String type = rs.getString("TypeRappel");
        if("A".equals(type))
        {
            // Nouvel(le) employé(e)
            message.append("<td align=\"center\">" + rs.getString("Colonne1") + "</td>\r\n");
            message.append("<td align=\"center\">" + rs.getString("Colonne2") + "</td>\r\n");
        }
        else if("B".equals(type))
        {
            // Transfert d'unité administrative
            message.append("<td align=\"center\">" + rs.getString("Colonne1") + "</td>\r\n");
            message.append("<td align=\"center\">" + rs.getString("Colonne2") + "</td>\r\n");
            message.append("<td align=\"center\">" + rs.getString("Colonne3") + "</td>\r\n");
        }
        else if("C".equals(type))
        {
            // Avis
            message.append("<td align=\"center\">" + rs.getString("Colonne1") + "</td>\r\n");
            message.append("<td align=\"center\">" + rs.getString("Colonne2") + "</td>\r\n");
            message.append("<td align=\"center\">" + rs.getString("Colonne3") + "</td>\r\n");
            message.append("<td align=\"center\">" + rs.getString("Colonne4") + "</td>\r\n");
        }
        else
        {
            // Rappel
            message.append("<td align=\"center\">" + rs.getString("Colonne1") + "</td>\r\n");
            message.append("<td align=\"center\">" + rs.getString("Colonne2") + "</td>\r\n");
            message.append("<td align=\"center\">" + rs.getString("Colonne3") + "</td>\r\n");
            message.append("<td align=\"center\">" + rs.getString("Colonne4") + "</td>\r\n");
        }
        
        message.append("</tr>\r\n");
    }
    
    /**
     * Cette méthode initialise les labels pour le courriel selon
     * ce qui se trouve dans la table P_ER_PARAMETER
     * @throws SQLException
     */
    private void initLabels() throws SQLException
    {
        PreparedStatement stmt = DB.prepareStatement(
                "select top 1 isnull( NewHeader, 'N/A' ) as NewHeader," +
                "             isnull( NewFooter, 'N/A' ) as NewFooter," +
                "             isnull( NewCol1, 'N/A' ) as NewCol1," +
                "             isnull( NewCol2, 'N/A' ) as NewCol2," +
                "             isnull( TransfertHeader, 'N/A' ) as TransfertHeader," +
                "             isnull( TransfertFooter, 'N/A' ) as TransfertFooter," +
                "             isnull( TransfertCol1, 'N/A' ) as TransfertCol1," +
                "             isnull( TransfertCol2, 'N/A' ) as TransfertCol2," +
                "             isnull( TransfertCol3, 'N/A' ) as TransfertCol3," +
                "             isnull( NoticeHeader, 'N/A' ) as NoticeHeader," +
                "             isnull( NoticeFooter, 'N/A' ) as NoticeFooter," +
                "             isnull( NoticeCol1, 'N/A' ) as NoticeCol1," +
                "             isnull( NoticeCol2, 'N/A' ) as NoticeCol2," +
                "             isnull( NoticeCol3, 'N/A' ) as NoticeCol3," +
                "             isnull( NoticeCol4, 'N/A' ) as NoticeCol4," +
                "             isnull( RecallHeader, 'N/A' ) as RecallHeader," +
                "             isnull( RecallFooter, 'N/A' ) as RecallFooter," +
                "             isnull( RecallCol1, 'N/A' ) as RecallCol1," +
                "             isnull( RecallCol2, 'N/A' ) as RecallCol2," +
                "             isnull( RecallCol3, 'N/A' ) as RecallCol3," +
                "             isnull( RecallCol4, 'N/A' ) as RecallCol4" +
                "  from P_ER_PARAMETER" +
                " where ISACTIVE = 'Y'", null);
        
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next())
        {
            this.newHeader = rs.getString("NewHeader");
            this.newFooter = rs.getString("NewFooter");
            this.newCol1 = rs.getString("NewCol1");
            this.newCol2 = rs.getString("NewCol2");
            this.transfertHeader = rs.getString("TransfertHeader");
            this.transfertFooter = rs.getString("TransfertFooter");
            this.transfertCol1 = rs.getString("TransfertCol1");
            this.transfertCol2 = rs.getString("TransfertCol2");
            this.transfertCol3 = rs.getString("TransfertCol3");
            this.noticeHeader = rs.getString("NoticeHeader");
            this.noticeFooter = rs.getString("NoticeFooter");
            this.noticeCol1 = rs.getString("NoticeCol1");
            this.noticeCol2 = rs.getString("NoticeCol2");
            this.noticeCol3 = rs.getString("NoticeCol3");
            this.noticeCol4 = rs.getString("NoticeCol4");
            this.recallHeader = rs.getString("RecallHeader");
            this.recallFooter = rs.getString("RecallFooter");
            this.recallCol1 = rs.getString("RecallCol1");
            this.recallCol2 = rs.getString("RecallCol2");
            this.recallCol3 = rs.getString("RecallCol3");
            this.recallCol4 = rs.getString("RecallCol4");
        }
        
        rs.close();
        stmt.close();
    }

}
