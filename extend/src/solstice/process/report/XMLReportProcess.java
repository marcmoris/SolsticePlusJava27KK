package solstice.process.report;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.util.logging.Level;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.compiere.model.MProcess;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

import solstice.model.P_System_Parameters;

/**
 * Cette classe définit un processus compiere permettant de lancer un rapport xml.
 */
public class XMLReportProcess extends SvrProcess
{
    
    private String reportName = null;
    private String reportPath = null;
    private int yearId = 0;
    private int employeeId = 0;
    private String typeCopie = "1";
    private String typePiece = "O";
    private int payment_Group_ID;
    private String note = "";

    public XMLReportProcess()
    {
        super();
    }

    /**
     * Cette méthode permet de préparer le processus en récupérant les paramètres de lancement
     */
    protected void prepare()
    {
        try
        {
            // On récupère d'abord le nom du formulaire Xml à charger
            MProcess process = MProcess.get(Env.getCtx(), this.getProcessInfo().getAD_Process_ID());
            this.reportName = process.getName();
            this.reportPath = String.valueOf(P_System_Parameters.getParameterValue("XMLReportPath"));
            
            // On récupère ensuite les paramètres de lancement
            ProcessInfoParameter[] params = this.getParameter();
            for(int i = 0; i < params.length; i++)
            {
                if("P_Year_ID".equals(params[i].getParameterName()))
                    this.yearId = params[i].getParameterAsInt();
                else if("P_Employee_ID".equals(params[i].getParameterName()))
                    this.employeeId = params[i].getParameterAsInt();
                else if("TypeCopie".equals(params[i].getParameterName()))
                    this.typeCopie = String.valueOf(params[i].getParameter());
                else if("TypePiece".equals(params[i].getParameterName()))
                    this.typePiece = String.valueOf(params[i].getParameter());
    			else if ("P_Payment_Group_ID".equals(params[i].getParameterName()))
    				this.payment_Group_ID = params[i].getParameterAsInt();
    			else if ("Note".equals(params[i].getParameterName()))
    				this.note = String.valueOf(params[i].getParameter());
           }
        }
        catch (Exception e)
        {
        	log.log(Level.SEVERE, e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Traitement principal : C'est ici qu'on génère le rapport et qu'on l'imprime
     */
    protected String doIt() throws Exception
    {
        // On s'assure d'abord d'avoir tous les paramètres nécessaires pour lancer le rapport
        if(this.reportName == null || this.reportPath == null)
            return "Le chemin d'accès vers le rapport ne peut pas être null";
        
        // On charge maitnenant le fichier xml du rapport
        File file = new File(this.reportPath + this.reportName);
        if(!file.exists())
            return "Ce fichier n'existe pas : " + this.reportPath + this.reportName;
        
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        SAXParser parser = parserFactory.newSAXParser();
        XMLReportHandler xmlReport = new XMLReportHandler();
        parser.parse(file, xmlReport);
        
        // Si on doit imprimer une copie pour employeur, on doit mettre plusieurs
        // employés par feuille. Sinon, on doit répéter l'employé pour remplir la
        // feuille.
        if("2".equals(this.typeCopie))
            xmlReport.setRecordRepetition(1);
        else
            xmlReport.setRecordRepetition(xmlReport.getRecordPerPage());

        // On prépare les conditions de sélection
        String additionalConditions = "FormType = '" + this.typePiece + "'";
        if(this.employeeId > 0)
            additionalConditions += " and P_Employee_ID = " + this.employeeId;

        if(this.payment_Group_ID > 0)
            additionalConditions += " and P_Payment_Group_ID = " + this.payment_Group_ID;
        
        if(this.yearId > 0)
            additionalConditions += " and P_Year_ID = " + this.yearId;
        
        // On prépare ensuite l'impression
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName(this.reportName);
        PageFormat pf = job.defaultPage();
        Paper p = pf.getPaper();
        p.setSize(xmlReport.getPaperWidth(), xmlReport.getPaperHeight());
        p.setImageableArea(xmlReport.getMarginX(), xmlReport.getMarginY(), p.getWidth() - xmlReport.getMarginX(), p.getHeight() - xmlReport.getMarginY());
        pf.setPaper(p);
        
        // Finalement, on génère le rapport et on l'imprime.
        XMLReportEngine reportEngine = new XMLReportEngine(xmlReport);
        if("2".equals(this.typeCopie))
            reportEngine.setPrintingAccess(XMLFieldHandler.DISPLAYACCESS_EMPLOYER);
        else
            reportEngine.setPrintingAccess(XMLFieldHandler.DISPLAYACCESS_EMPLOYEE);

        if ( this.note != null)
        	reportEngine.setNote( note );

        job.setPrintable(reportEngine, pf);
        
        reportEngine.executeQuery(additionalConditions);
        
        try
        {
            job.print();
            return "Impression terminée";
        }
        catch (PrinterException e)
        {
            e.printStackTrace();
        	log.log(Level.SEVERE, e.toString());
            return "Erreur dans l'impression";
        }
        finally
        {
            reportEngine.closeConnection();
        }
    }

    /**
     * Programme principal.
     * Ce programme sert à faire imprimer un rapport en format text sur plusieurs
     * pages selon un formattage en xml.
     */
    public static void main(String[] args) throws Exception
    {
        org.compiere.Compiere.startupEnvironment(true);
        
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        SAXParser parser = parserFactory.newSAXParser();
        File file = new File("C:\\Eclipse3.1\\workspace\\compiere-all\\xmlreports\\T506.xml");
        XMLReportHandler xmlReport = new XMLReportHandler();
        parser.parse(file, xmlReport);
      
        XMLReportEngine reportEngine = new XMLReportEngine(xmlReport);
        PrinterJob job = PrinterJob.getPrinterJob();
        PageFormat pf = job.defaultPage();
        Paper p = pf.getPaper();
        p.setImageableArea(xmlReport.getMarginX(), xmlReport.getMarginY(), p.getWidth() - xmlReport.getMarginX(), p.getHeight() - xmlReport.getMarginY());
        pf.setPaper(p);
        job.setPrintable(reportEngine, pf);
        reportEngine.executeQuery();

        try
        {
            job.print();
            System.out.println("Impression terminée");
        }
        catch (PrinterException e)
        {
            System.out.println("Erreur dans l'impression");
            System.out.println(e.getMessage());
        }
        
        reportEngine.closeConnection();
    }

}
