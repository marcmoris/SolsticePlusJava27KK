package solstice.custom;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

/**
 * Cette classe sert à traiter les paramètres d'un request
 * @author frafor01
 */
public class WebUtil
{
    /**
     * Définition générales des champs pouvant être utilisés dans cette classe.
     * @author frafor01
     */
    public interface IField
    {
        public Object getParameterValue() throws Exception;
        public String getFieldName();
    }
    
    public interface IFieldArray
    {
        public Object[] getParameterValues() throws Exception;
        public String getFieldName();
    }
    
    /**
     * Renvoit un seul paramètre texte
     * @author frafor01
     */
    public class SimpleTextField implements IField, IFieldArray
    {
        /*
         * Member
         */
        private String fieldName;
        private String parameterName;
        
        /**
         * Constructeur par défaut. Il prend le nom du paramètre dans le request
         */
        public SimpleTextField(String fieldName)
        {
            this.fieldName = fieldName;
            this.parameterName = fieldName;
        }
        
        public SimpleTextField(String fieldName, String parameterName)
        {
            this.fieldName = fieldName;
            this.parameterName = parameterName;
        }
        
        /**
         * Retourne un String contenant la valeur texte
         */
        public Object getParameterValue() throws Exception
        {
            try
            {
                return request.getParameter(this.parameterName) == null ? "" : request.getParameter(this.parameterName);
            }
            catch (Exception e)
            {
                throw new Exception("Erreur dans le getParameterValue de " + this.parameterName, e);
            }
        }
        
        /**
         * Retourne un String[] contenant les valeurs textes
         */
        public Object[] getParameterValues() throws Exception
        {
            try
            {
                return request.getParameterValues(this.parameterName);
            }
            catch (Exception e)
            {
                throw new Exception("Erreur dans le getParameterValues de " + this.parameterName, e);
            }
        }
        
        public String getFieldName() {return this.fieldName;}
    }
    
    /**
     * Cette classe retourne la valeur d'un <input type="check">
     * @author frafor01
     */
    public class CheckBoxField implements IField, IFieldArray
    {
        /*
         * Member
         */
        private String fieldName;
        private String parameterName;
        private int dim;
        
        /**
         * Constructeur par défaut. Il prend le nom du paramètre dans le request
         */
        public CheckBoxField(String fieldName)
        {
            this(fieldName, fieldName, 0);
        }
        
        public CheckBoxField(String fieldName, String parameterName)
        {
            this(fieldName, parameterName, 0);
        }
        
        public CheckBoxField(String fieldName, String parameterName, int dim)
        {
            this.fieldName = fieldName;
            this.parameterName = parameterName;
            this.dim = dim;
        }
        
        /**
         * Si le checkbox était coché, cette méthode retourne le String "checked". Sinon
         * elle retourne un String vide ""
         */
        public Object getParameterValue() throws Exception
        {
            try
            {
                return this.getBooleanValue() ? "checked" : "";
            }
            catch (Exception e)
            {
                throw new Exception("Erreur dans le getParameterValue de " + this.parameterName, e);
            }
        }
        
        /**
         * Cette méthode retourne un boolean indiquant si le checkBox était coché ou non
         */
        public boolean getBooleanValue()
        {
            return (request.getParameter(this.fieldName) != null
                    && "on".equals(request.getParameter(this.fieldName)));
        }
        
        /**
         * Pour palier aux contraintes que le html propose, le nom des checkbox utilisant
         * cette méthode doivent suivre la convension suivante : parameterName_index
         */
        public Object[] getParameterValues() throws Exception
        {
            try
            {
                String[] values = new String[this.dim];
                for(int i = 0; i < this.dim; i++)
                {
                    String value = request.getParameter(this.parameterName + "_" + i);
                    values[i] = value != null && "on".equals(value) ? "checked" : "";
                }
                return values;
            }
            catch (Exception e)
            {
                throw new Exception("Erreur dans le getParameterValues de " + this.parameterName, e);
            }
        }
        
        public String getFieldName() {return this.fieldName;}
    }
    
    /**
     * Cette classe construit une hashtable à partir de plusieurs champs.
     * @author frafor01
     */
    public class HashtableField implements IField
    {
        private String fieldName;
        private IField[] fields;

        public HashtableField(String fieldName, IField[] fields)
        {
            this.fieldName = fieldName;
            this.fields = fields;
        }
        
        /**
         * Construit une table de hachage à partir des champs
         */
        public Object getParameterValue() throws Exception
        {
            try
            {
                Hashtable<String,Object> table = new Hashtable<String,Object>();
                for(int i = 0; i < fields.length; i++)
                {
                    table.put(fields[i].getFieldName(), fields[i].getParameterValue());
                }
                return table;
            }
            catch (Exception e)
            {
                throw new Exception("Erreur dans le getParameterValue de " + this.fieldName, e);
            }
        }
        
        public String getFieldName() {return this.fieldName;}
    }
    
    /**
     * Cette classe construit une vecteur de table de hachage à partir de plusieurs champs
     * @author frafor01
     */
    public class HashtableArrayField implements IField
    {
        private String fieldName;
        private IFieldArray[] fields;
        
        public HashtableArrayField(String fieldName, IFieldArray[] fields)
        {
            this.fieldName = fieldName;
            this.fields = fields;
        }
        
        public Object getParameterValue() throws Exception
        {
            try
            {
                Vector<Object> tablesVector = new Vector<Object>();
                for(int i = 0; i < this.fields.length; i++)
                {
                    Object[] values = this.fields[i].getParameterValues();
                    for(int j = 0; j < values.length; j++)
                    {
                        Hashtable<String,Object> currentTable;
                        if(j < tablesVector.size())
                        {
                            currentTable = (Hashtable<String,Object>)tablesVector.elementAt(j);
                        }
                        else
                        {
                            currentTable = new Hashtable<String,Object>();
                            tablesVector.add(currentTable);
                        }
                        if(values[j] == null)
                        {
                            System.out.println("la valeur de " + this.fields[i].getFieldName() + " à l'index " + j + "est null");
                        }
                        currentTable.put(this.fields[i].getFieldName(), values[j]);
                    }
                }
                
                Hashtable[] tables = new Hashtable[tablesVector.size()];
                for(int i = 0; i < tables.length; i++)
                {
                    tables[i] = (Hashtable)tablesVector.elementAt(i);
                }
                
                return tables;
            }
            catch (Exception e)
            {
                throw new Exception("Erreur dans le getParameterValues de " + this.fieldName, e);
            }
        }
        
        public String getFieldName() {return this.fieldName;}
    }
    
    /**
     * Cette classe représente les champs nommés parameterName_index
     */
    public class IndexedField implements IFieldArray
    {
        private String parameterName;
        private String fieldName;
        private int dim;
        
        public IndexedField(String fieldName, String parameterName, int dim)
        {
            this.fieldName = fieldName;
            this.parameterName = parameterName;
            this.dim = dim;
        }
        
        public String getFieldName() {return this.fieldName;}
        
        public Object[] getParameterValues() throws Exception
        {
            try
            {
                String[] values = new String[this.dim];
                for(int i = 0; i < this.dim; i++)
                {
                    values[i] = request.getParameter(this.parameterName + "_" + i) == null ? "" : request.getParameter(this.parameterName + "_" + i);
                }
                return values;
            }
            catch (Exception e)
            {
                e.printStackTrace(System.out);
                throw new Exception("Erreur dans le getParameterValues de " + this.fieldName, e);
            }
        }
    }
    
    /*
     * Member
     */
    private HttpServletRequest request;

    public WebUtil(HttpServletRequest request)
    {
        super();
        this.request = request;
    }
    
    public SimpleTextField instanciateSimpleTextField(String fieldName) {return new SimpleTextField(fieldName);}
    public SimpleTextField instanciateSimpleTextField(String fieldName, String parameterName) {return new SimpleTextField(fieldName, parameterName);}
    public CheckBoxField instanciateCheckBoxField(String fieldName) {return new CheckBoxField(fieldName);}
    public CheckBoxField instanciateCheckBoxField(String fieldName, String parameterName) {return new CheckBoxField(fieldName, parameterName);}
    public CheckBoxField instanciateCheckBoxField(String fieldName, String parameterName, int dim) {return new CheckBoxField(fieldName, parameterName, dim);}
    public HashtableField instanciateHashtableField(String fieldName, IField[] fields) {return new HashtableField(fieldName, fields); }
    public HashtableArrayField instanciateHashtableArrayField(String fieldName, IFieldArray[] fields) {return new HashtableArrayField(fieldName, fields); }
    public IndexedField instanciateIndexedField(String fieldName, String parameterName, int dim) {return new IndexedField(fieldName, parameterName, dim);}

    /**
     * Cette méthode retourne une table de hachage construite à partir des paramètres de la requête
     */
    public Hashtable buildHashtable(IField[] fields)
    {
        try
        {
            Hashtable<String,Object> table = new Hashtable<String,Object>();
            for(int i = 0; i < fields.length; i++)
            {
                Object value = fields[i].getParameterValue();
                if(value == null)
                    System.out.println("Le champ " + fields[i].getFieldName() + " est null");
                else
                    table.put(fields[i].getFieldName(), fields[i].getParameterValue());
            }
            return table;
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
            return null;
        }
    }
}
