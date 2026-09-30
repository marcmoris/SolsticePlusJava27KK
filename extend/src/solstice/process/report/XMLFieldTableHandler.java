package solstice.process.report;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Cette classe permet de gérer une table de champs variables. Il s'agit en fait
 * d'une table de position de champ dans le formulaire et d'une liste de valeur
 * possible à afficher dedans. La condition pour remplir un champ est simple : la
 * valeur ne doit pas être null. Une fois que toutes les cases sont remplies, on
 * ignore les prochaines valeurs
 */
public class XMLFieldTableHandler
{
    
    /*
     * Constantes utilisées pour définir la position des attributs dans le vecteur des valeurs
     */
    private static final int ID = 0;
    private static final int LABEL = 1;
    
    private XMLReportHandler parentReport;
    private Vector<XMLVariableFieldHandler> variableFields = new Vector<XMLVariableFieldHandler>();
    private Vector<String[]> variableValues = new Vector<String[]>();

    /**
     * Constructeur
     */
    XMLFieldTableHandler(XMLReportHandler parentReport)
    {
        super();
    }
    
    /**
     * Le rapport contenant la table de champs variables
     */
    public XMLReportHandler getParentReport()
    {
        return this.parentReport;
    }
    
    /**
     * Retourne la liste des champs nécessaires pour bâtir la requête
     */
    public String[] getFields()
    {
        String[] fields = new String[this.variableValues.size()];
        for(int i = 0; i < fields.length; i++)
        {
            fields[i] = ((String[])this.variableValues.get(i))[XMLFieldTableHandler.ID];
        }
        return fields;
    }
    
    /**
     * Vide la collection de champs variables
     */
    public void clear()
    {
        this.variableFields.clear();
        this.variableValues.clear();
    }
 
    /**
     * Ajoute un champ variable à la table
     */
    public void addField(int x, int y, int align, int labelX, int labelY, int labelAlign, int width, Font font, String specialMethod)
    {
        XMLVariableFieldHandler newField = new XMLVariableFieldHandler(
                null, x, y, align, labelX, labelY, labelAlign, width, font, specialMethod, this.parentReport, this);
        
        this.variableFields.add(newField);
    }

    /**
     * Ajoute une valeur à la table
     */
    public void addValue(String id, String label)
    {
        this.variableValues.add(new String[] {id, label});
    }
    
    /**
     * Prend en charge impression de l'enregistrement et imprime le contenue des cellules variables
     */
    public void printRecord(Graphics2D g, Hashtable record, int paddingX, int paddingY, int printingAccess)
    {
        // On doit passer au travers de chacune des valeurs variables, vérifier si cette
        // valeur est différente de null et l'afficher séquentiellement dans le vecteur
        // des champs variables.
        int fieldIndex = 0;
        Enumeration values = this.variableValues.elements();
        while(values.hasMoreElements() && fieldIndex < this.variableFields.size())
        {
            String[] currentValue = (String[])values.nextElement();
            Object recordValue = record.get(currentValue[XMLFieldTableHandler.ID]);
            if(recordValue != null && !(recordValue instanceof DBNull))
            {
                // On imprime le champ
                XMLVariableFieldHandler currentField = (XMLVariableFieldHandler)this.variableFields.get(fieldIndex);
                currentField.printLabel(g, currentValue[XMLFieldTableHandler.LABEL], paddingX, paddingY);
                currentField.print(g, recordValue, paddingX, paddingY, printingAccess);
                fieldIndex++;
            }
        }
    }

}
