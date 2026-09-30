package solstice.process.report;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.DB;
import solstice.model.P_Form_Employee;
import org.compiere.util.Env;

/**
 * Cette classe sert à générer et à imprimer le rapport passé en paramètre
 */
public class XMLReportEngine implements Printable
{
    
    private int printingAccess = 3;
    private DBNull dbNullInstance = new DBNull();
    private XMLReportHandler reportXml;
    private PreparedStatement stmt = null;
    private ResultSet rs = null;
    private int repetitionCount = 0;
    private int loadedPageIndex = -1;
    private Hashtable[] loadedPage = null;
    private String note = ""; 

    public CLogger			log = CLogger.getCLogger (getClass());
    
    /**
     * Constructeur
     */
    public XMLReportEngine(XMLReportHandler reportXml)
    {
        this.reportXml = reportXml;
    }

    /**
     * set Note
     */
    public void setNote(String note)
    {
        this.note = note;
    }

    /**
     * get Note
     */
    public String getNote()
    {
        return this.note ;
    }

    /**
     * Acces pour l'impression
     */
    public void setPrintingAccess(int value)
    {
        this.printingAccess = value;
    }

    /**
     * Acces pour l'impression
     */
    public int getPrintingAccess()
    {
        return this.printingAccess;
    }
    
    /**
     * Exécute la requête permettant de générer adéquatement le rapport
     */
    public void executeQuery(String additionalCondition) throws SQLException
    {
        // On libère d'abord les anciennes ressources si ce n'est pas déjà fait
        this.closeConnection();
        
        // On exécute la requête provenant du xml
        this.stmt = DB.prepareStatement(this.reportXml.buildQuery(additionalCondition), null);
        this.rs = stmt.executeQuery();

		while (rs.next())
		{
			P_Form_Employee FormEmployee = P_Form_Employee.get( Env.getCtx(), rs.getInt("P_Form_Employee_ID"), null);
			FormEmployee.setIsPrinted(true);
			FormEmployee.save();
		}

		this.rs = stmt.executeQuery();
        
        
    }
    
    /**
     * Exécute la requête permettant de générer adéquatement le rapport
     */
    public void executeQuery() throws SQLException
    {
        this.executeQuery(null);
    }
    
    /**
     * Ferme la connexion ouverte
     */
    public void closeConnection() throws SQLException
    {
        if(this.stmt != null)
            this.stmt.close();
        
        if(this.rs != null)
            this.rs.close();
    }

    /**
     * Impression de chacune des pages
     */
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException
    {
        if(this.rs == null)
            return Printable.NO_SUCH_PAGE;
        
        Graphics2D g = (Graphics2D)graphics;
        g.setFont(this.reportXml.getFont());
        
        try
        {
            // Puisque le gestionnaire d'impression peut passer plusieurs fois par la méthode
            // print(...) au cours du traitement, on doit précharger la page en mémoire pour
            // conserver aussi longtemps que nécessaire les informations à imprimer.
            this.preloadPage(pageIndex);
            if(this.loadedPage != null)
            {
                for(int i = 0; i < this.loadedPage.length; i++)
                {
                    this.printRecord(g, this.loadedPage[i], i);
                }
                return Printable.PAGE_EXISTS;
            }
            return Printable.NO_SUCH_PAGE;
        }
        catch (SQLException e)
        {
        	log.log(Level.SEVERE, e.toString());
            throw new PrinterException(e.getMessage());
        }
    }
    
    /**
     * Cette méthode permet de précharger la page à imprimer. En effet, puisque le gestionnaire
     * d'impression peut passer plusieurs fois dans chacune des pages lors de son traitement, on
     * doit précharger chacune des pages pour être certain d'afficher la bonne information au bon
     * moment. Heureusement, on sait que ces appels se font de manière séquentielle. Lorsqu'on
     * passe à la prochaine page, on peut donc oublier la page précédente
     */
    private void preloadPage(int pageIndex) throws SQLException
    {
        // On vérifie si on doit charger une nouvelle page
        if(this.loadedPageIndex != pageIndex)
        {
            // On charge maintenant tous les enregistrements de la page en prennant en
            // considération les répétitions
            Vector<Object> page = new Vector<Object>();
            for(int i = 0; i < this.reportXml.getRecordPerPage(); i++)
            {
                if(this.moveNext())
                {
                    Hashtable<String,Object> record = new Hashtable<String,Object>();
                    Enumeration fields = this.reportXml.getAllFieldNames();
                    while(fields.hasMoreElements())
                    {
                        String fieldName = (String)fields.nextElement();
                        Object value = this.rs.getObject(fieldName);
                        record.put(fieldName, value == null ? this.dbNullInstance : value);
                    }
                    page.add(record);
                }
            }
            
            // S'il y avait des enregistrements dans la page, on met en mémoire ces informations,
            // sinon, on doit indiquer à l'objet qu'il n'y a aucune page de chargée.
            if(page.size() > 0)
            {
                this.loadedPageIndex = pageIndex;
                this.loadedPage = new Hashtable[page.size()];
                for(int i = 0; i < page.size(); i++)
                {
                    this.loadedPage[i] = (Hashtable)page.get(i);
                }
            }
            else
            {
                this.loadedPageIndex = -1;
                this.loadedPage = null;
            }
        }
    }
    
    /**
     * Passe au prochain enregistrement en contant les répétitions
     */
    private boolean moveNext() throws SQLException
    {
        // Si on avait déjà une répétition de commencée, on reste dans cet
        // enregistrement, sinon, on passe au prochain
        if(this.repetitionCount > 0)
        {
            this.repetitionCount--;
            return true;
        }

        if(this.rs.next())
        {
            this.repetitionCount = this.reportXml.getRecordRepetition() - 1;
            return true;
        }
        return false;
    }
    
    /**
     * Imprime l'enregistrement passé en paramètre
     */
    private void printRecord(Graphics2D g, Hashtable record, int index) throws SQLException
    {
        // On trouve d'abord la position relative du cadre
        int paddingX = this.reportXml.getMarginX();
        int paddingY = this.reportXml.getMarginY() + index * this.reportXml.getDeltaY();
        
        // On passe ensuite chacun des champs pour afficher le résultat au bon endroit
        Enumeration fields = this.reportXml.getFields();
        while(fields.hasMoreElements())
        {
            XMLFieldHandler field = (XMLFieldHandler)fields.nextElement();
            if ( field.getID().equals("Note"))
            {
            	if ( this.getNote() != null )
            		field.print(g, this.getNote(), paddingX, paddingY, this.printingAccess);
            }
            else
            {
                Object value = record.get(field.getID());
            	field.print(g, value, paddingX, paddingY, this.printingAccess);
            }
        }
        
        // Finalement, on passe par les tables de champs variables
        Enumeration fieldTables = this.reportXml.getFieldTables();
        while(fieldTables.hasMoreElements())
        {
            XMLFieldTableHandler fieldTable = (XMLFieldTableHandler)fieldTables.nextElement();
            fieldTable.printRecord(g, record, paddingX, paddingY, this.printingAccess);
        }
    }

}
