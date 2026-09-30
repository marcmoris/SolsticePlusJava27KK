package solstice.process.report;

import java.awt.Font;
import java.util.Enumeration;
import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Cette classe sert à interpréter un xml pour charger les paramètres d'impression d'un rapport
 */
public class XMLReportHandler extends DefaultHandler
{
    
    /*
     * Members
     */
    
    private String view = null;
    private String where = null;
    private Font font = new Font("Serif", Font.PLAIN, 10);
    private int recordRepetition = 1;
    private int recordPerPage = 1;
    private int marginX = 0;
    private int marginY = 0;
    private int deltaY = 0;
    private double paperWidth = 612;
    private double paperHeight = 792;
    private Vector<XMLFieldHandler> fields = new Vector<XMLFieldHandler>();
    private Vector<XMLFieldTableHandler> fieldTables = new Vector<XMLFieldTableHandler>();
    private XMLFieldTableHandler currentFieldTable = null;
    
    /*
     * Constructor
     */
    
    /**
     * Constructeur par défaut
     */
    public XMLReportHandler()
    {
    }
    
    /*
     * Getters
     */
    
    /**
     * Le nom de la vue associée au rapport
     */
    public String getView()
    {
        return this.view;
    }
    
    public Font getFont()
    {
        return this.font;
    }
    
    /**
     * Largeur du papier en point à 72dpi
     */
    public double getPaperWidth()
    {
        return this.paperWidth;
    }
    
    /**
     * Hauteur du papier en point à 72dpi
     */
    public double getPaperHeight()
    {
        return this.paperHeight;
    }
    
    /**
     * Le nombre de fois que chaque enregistrement doit être répété (1 ou plus)
     */
    public int getRecordRepetition()
    {
        return this.recordRepetition;
    }
    
    /**
     * Le nombre d'enregistrement par page (1 ou plus)
     */
    public int getRecordPerPage()
    {
        return this.recordPerPage;
    }
    
    /**
     * Le nombre de fois que chaque enregistrement doit être répété (1 ou plus)
     */
    public void setRecordRepetition(int value)
    {
        if(value < 1)
            this.recordRepetition = 1;
        else
            this.recordRepetition = value;
    }
    
    /**
     * La marge en X (0 ou plus)
     */
    public int getMarginX()
    {
        return this.marginX;
    }

    /**
     * La marge en Y (0 ou plus)
     */
    public int getMarginY()
    {
        return this.marginY;
    }

    /**
     * Décallage entre chaque enregistrement sur une page
     */
    public int getDeltaY()
    {
        return this.deltaY;
    }
    
    /**
     * Retourne la liste des champs
     */
    public Enumeration getFields()
    {
        return this.fields.elements();
    }
    
    /**
     * Retourne la liste des tables de champs variables
     */
    public Enumeration getFieldTables()
    {
        return this.fieldTables.elements();
    }
    
    /*
     * Methods
     */
    
    /**
     * Construit une requête à partir de la liste des champs et du nom de la vue
     */
    public String buildQuery(String additionalCondition)
    {
        if(this.view != null)
        {
            // On récupère d'abord la liste des noms des champs à inclure dans la requête
            Enumeration fieldNames = this.getAllFieldNames();
            if(fieldNames.hasMoreElements())
            {
                String buffer = "SELECT " + fieldNames.nextElement();
                while(fieldNames.hasMoreElements())
                {
                    buffer += ", " + fieldNames.nextElement();
                }
                
                buffer += " FROM " + this.view;
                
                // Ajout des conditions (s'il y a lieu)
                if(additionalCondition != null && "".equals(additionalCondition) == false
                        && this.where != null && "".equals(this.where) == false)
                {
                    buffer += " WHERE ( " + this.where + " ) AND ( " + additionalCondition + " )";
                }
                else if(additionalCondition != null && "".equals(additionalCondition) == false)
                {
                    buffer += " WHERE " + additionalCondition;
                }
                else if(this.where != null && "".equals(this.where) == false)
                {
                    buffer += " WHERE " + this.where;
                }
                return buffer;
            }
        }
        return null;
    }
    
    /**
     * Construit une requête à partir de la liste des champs et du nom de la vue
     */
    public String buildQuery()
    {
        return this.buildQuery(null);
    }
    
    /**
     * Retourne la liste des noms de tous les champs utilisés dans l'impression du rapport
     */
    public Enumeration getAllFieldNames()
    {
        // On commence par les champs statiques
        Vector<String> fieldNames = new Vector<String>();
        
        for(int i = 0; i < this.fields.size(); i++)
        {
            XMLFieldHandler field = (XMLFieldHandler)this.fields.get(i);
            if ( ! field.getID().equals("Note"))
              if(!fieldNames.contains(field.getID()))
                fieldNames.add(field.getID());
        }
        
        // On récupère ensuite la liste des champs variables
        for(int i = 0; i < this.fieldTables.size(); i++)
        {
            XMLFieldTableHandler fieldTable = (XMLFieldTableHandler)this.fieldTables.get(i);
            String[] f = fieldTable.getFields();
            for(int j = 0; j < f.length; j++)
            {
                if(!fieldNames.contains(f[j]))
                    fieldNames.add(f[j]);
            }
        }

        // add the key for update the information.
        fieldNames.add("P_FORM_EMPLOYEE_ID");

        return fieldNames.elements();
    }

    /*
     * DefaultHandler methods
     */
    
    /**
     * Début du document, on réinitialise les champs
     */
    public void startDocument()
    {
        this.view = null;
        this.recordRepetition = 1;
        this.recordPerPage = 1;
        this.marginX = 0;
        this.marginY = 0;
        this.deltaY = 0;
        this.fields.clear();
        this.fieldTables.clear();
        this.currentFieldTable = null;
    }
    
    /**
     * Interprète un élément dans le xml
     */
    public void startElement(String uri, String localName, String qName, Attributes attributes)
    {
        if("report".equals(qName))
        {
            // Paramètres généraux d'impression du rapport
            this.view = attributes.getValue("view");
            this.where = attributes.getValue("where");
            
            String fontFamily = attributes.getValue("fontFamily") == null ? "Serif" : attributes.getValue("fontFamily");
            int fontSize = attributes.getValue("fontSize") == null ? 10 : Integer.parseInt(attributes.getValue("fontSize"));
            
            String fontStyle = attributes.getValue("fontStyle");
            int fs = Font.PLAIN;
            if("bold".equals(fontStyle))
                fs = Font.BOLD;
            else if("italic".equals(fontStyle))
                fs = Font.ITALIC;
            
            this.font = new Font(fontFamily, fs, fontSize);
            
            this.recordRepetition = attributes.getValue("recordRepetition") == null ? 1 : Integer.parseInt(attributes.getValue("recordRepetition"));
            this.recordPerPage = attributes.getValue("recordPerPage") == null ? 1 : Integer.parseInt(attributes.getValue("recordPerPage"));
            this.marginX = attributes.getValue("marginX") == null ? 0 : Integer.parseInt(attributes.getValue("marginX"));
            this.marginY = attributes.getValue("marginY") == null ? 0 : Integer.parseInt(attributes.getValue("marginY"));
            this.deltaY = attributes.getValue("deltaY") == null ? 0 : Integer.parseInt(attributes.getValue("deltaY"));
            this.paperWidth = attributes.getValue("paperWidth") == null ? 612 : Double.parseDouble(attributes.getValue("paperWidth")) * 72.0d;
            this.paperHeight = attributes.getValue("paperHeight") == null ? 792 : Double.parseDouble(attributes.getValue("paperHeight")) * 72.0d;
        }
        else if("field".equals(qName))
        {
            // Paramétrage d'un champ
            String id = attributes.getValue("id");
            int x = attributes.getValue("x") == null ? 0 : Integer.parseInt(attributes.getValue("x"));
            int y = attributes.getValue("y") == null ? 0 : Integer.parseInt(attributes.getValue("y"));
            int width = attributes.getValue("width") == null ? -1 : Integer.parseInt(attributes.getValue("width"));
            String specialMethod = attributes.getValue("specialMethod");

            String fontFamily = attributes.getValue("fontFamily") == null ? this.font.getFamily() : attributes.getValue("fontFamily");
            int fontSize = attributes.getValue("fontSize") == null ? this.font.getSize() : Integer.parseInt(attributes.getValue("fontSize"));
            
            String fontStyle = attributes.getValue("fontStyle");
            int fs;
            if("bold".equals(fontStyle))
                fs = Font.BOLD;
            else if("italic".equals(fontStyle))
                fs = Font.ITALIC;
            else if("plain".equals(fontStyle))
                fs = Font.PLAIN;
            else
                fs = this.font.getStyle();
            
            int displayAccess = attributes.getValue("displayAccess") == null ? 3 : Integer.parseInt(attributes.getValue("displayAccess"));
            
            String align = attributes.getValue("align");
            int alignment;
            if("center".equals(align))
                alignment = XMLFieldHandler.ALIGN_CENTER;
            else if("right".equals(align))
                alignment = XMLFieldHandler.ALIGN_RIGHT;
            else if("currency".equals(align))
                alignment = XMLFieldHandler.ALIGN_CURRENCY;
            else
                alignment = XMLFieldHandler.ALIGN_LEFT;
            
            if(id != null)
            {
                // On instancie un nouveau champ
                Font font = new Font(fontFamily, fs, fontSize);
                XMLFieldHandler field = new XMLFieldHandler(id, x, y, alignment, width, font, displayAccess, specialMethod, this);
                
                this.fields.add(field);
            }
        }
        else if("fieldTable".equals(qName))
        {
            if(this.currentFieldTable == null)
            {
                // On doit créer une nouvelle table de champs variables
                XMLFieldTableHandler fieldTable = new XMLFieldTableHandler(this);
                this.fieldTables.add(fieldTable);
                this.currentFieldTable = fieldTable;
            }
        }
        else if("addField".equals(qName))
        {
            if(this.currentFieldTable != null)
            {
                // On doit ajouter un champ variable à la table courante
                int labelX = attributes.getValue("labelX") == null ? 0 : Integer.parseInt(attributes.getValue("labelX"));
                int labelY = attributes.getValue("labelY") == null ? 0 : Integer.parseInt(attributes.getValue("labelY"));
                int x = attributes.getValue("x") == null ? 0 : Integer.parseInt(attributes.getValue("x"));
                int y = attributes.getValue("y") == null ? 0 : Integer.parseInt(attributes.getValue("y"));
                int width = attributes.getValue("width") == null ? -1 : Integer.parseInt(attributes.getValue("width"));
                String specialMethod = attributes.getValue("specialMethod");

                String fontFamily = attributes.getValue("fontFamily") == null ? this.font.getFamily() : attributes.getValue("fontFamily");
                int fontSize = attributes.getValue("fontSize") == null ? this.font.getSize() : Integer.parseInt(attributes.getValue("fontSize"));
                
                String fontStyle = attributes.getValue("fontStyle");
                int fs;
                if("bold".equals(fontStyle))
                    fs = Font.BOLD;
                else if("italic".equals(fontStyle))
                    fs = Font.ITALIC;
                else if("plain".equals(fontStyle))
                    fs = Font.PLAIN;
                else
                    fs = this.font.getStyle();
                
                String align = attributes.getValue("align");
                int alignment;
                if("center".equals(align))
                    alignment = XMLFieldHandler.ALIGN_CENTER;
                else if("right".equals(align))
                    alignment = XMLFieldHandler.ALIGN_RIGHT;
                else if("currency".equals(align))
                    alignment = XMLFieldHandler.ALIGN_CURRENCY;
                else
                    alignment = XMLFieldHandler.ALIGN_LEFT;
                
                align = attributes.getValue("labelAlign");
                int labelAlignment;
                if("center".equals(align))
                    labelAlignment = XMLFieldHandler.ALIGN_CENTER;
                else if("right".equals(align))
                    labelAlignment = XMLFieldHandler.ALIGN_RIGHT;
                else
                    labelAlignment = XMLFieldHandler.ALIGN_LEFT;
                
                // On instancie un nouveau champ
                Font font = new Font(fontFamily, fs, fontSize);
                this.currentFieldTable.addField(x, y, alignment, labelX, labelY, labelAlignment, width, font, specialMethod);
            }
        }
        else if("addValue".equals(qName))
        {
            if(this.currentFieldTable != null)
            {
                // On doit ajouter une valeur à la table courante
                String id = attributes.getValue("id");
                String label = attributes.getValue("label");
                if(id != null)
                {
                    this.currentFieldTable.addValue(id, label == null ? id : label);
                }
            }
        }
    }

    /**
     * Cette méthode est lancée lors de la fermeture d'un élément.
     */
    public void endElement(String uri, String localName, String qName)
    {
        // Si on vient de fermer une table de champs variables, on doit
        // remettre à null la table courante. Cette étape est importante
        // pour s'assurer qu'il n'y a pas de problème lors de la lecture
        // du xml.
        if("fieldTable".equals(qName))
        {
            this.currentFieldTable = null;
        }
    }
}
