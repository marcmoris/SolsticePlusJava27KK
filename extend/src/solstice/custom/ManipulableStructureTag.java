package solstice.custom;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * @author Francis
 * <p>Cette classe définit un tag permettant l'ajout et la suppression de ligne. Elle
 * s'assure également de faire le lien entre les différents post pour mettre les
 * bonnes valeurs dans les champs contenus dans le tag</p>
 */
public class ManipulableStructureTag extends BodyTagSupport
{
    public static final long serialVersionUID = 0;

    /*
     * Attributes
     */
    private String formName; // Mandatory
    private String initializationMethod; // Mandatory

    /*
     * Members
     */
    private String[] fieldsArray;
    private Enumeration tables = null;
    private int currentIndex = -1;

    /*
     * Setters
     */
    public void setFormName(String formName) {this.formName = formName;}
    public void setInitializationMethod(String initializationMethod) {this.initializationMethod = initializationMethod;}

    /**
     * Split les champs
     */
    public void setFields(String fields)
    {
        // On compte d'abord le nombre de virgules pour savoir
        // combien de mémoire alouer pour les champs
        int count = 1;
        for(int i = 0; i < fields.length(); i++)
        {
            if(fields.charAt(i) == ',')
                count++;
        }
        
        // Allocation et initialisation
        this.fieldsArray = new String[count];
        for(int i = 0; i < count; i++)
        {
            this.fieldsArray[i] = "";
        }
        
        // On remplit les noms de champs
        int j = 0;
        for(int i = 0; i < fields.length(); i++)
        {
            if(fields.charAt(i) == ',')
            {
                // Lorsqu'on rencontre une virgule, on passe au prochain
                j++;
            }
            else if(fields.charAt(i) != ' ')
            {
                this.fieldsArray[j] += String.valueOf(fields.charAt(i));
            }
        }
    }
    
    /*
     * Getters
     */
    public int getCurrentIndex() {return this.currentIndex;}
    
    public ManipulableStructureTag()
    {
        super();
    }

    /**
     * Dessine les éléments préparatoires (script + input) pour faire
     * la gestion de la structure et récupère l'état du dernier post
     */
    public int doStartTag() throws JspException
    {
        try
        {
            JspWriter out = this.pageContext.getOut();
            
            // On doit d'abord écrire les scripts
            out.println("<script language=\"javascript\">");
            out.println("   function " + this.id + "_add()");
            out.println("   {");
            out.println("      document." + this.formName + "." + this.id + "_action.value = \"add\";");
            out.println("      document." + this.formName + ".submit();");
            out.println("   }");
                 
            out.println("   function " + this.id + "_delete(index)");
            out.println("   {");
            out.println("      document." + this.formName + "." + this.id + "_action.value = \"delete:\" + index;");
            out.println("      document." + this.formName + ".submit();");
            out.println("   }");
             
            out.println("   function " + this.id + "_reset()");
            out.println("   {");
            out.println("      document." + this.formName + "." + this.id + "_action.value = \"\";");
            out.println("      document." + this.formName + ".submit();");
            out.println("   }");
            out.println("</script>");
            
            out.println("<input type=\"hidden\" name=\"" + this.id + "_action\" value=\"idle\">");

            // On doit maintenant passer au premier enregistrement
            if(this.moveFirst())
            {
                return EVAL_BODY_BUFFERED;
            }
            return SKIP_BODY;
        }
        catch (IOException e)
        {
            throw new JspException(e);
        }
    }
    
    /**
     * Passe au prochain enregistrement
     */
    public int doAfterBody() throws JspException
    {
        if(this.moveNext())
        {
            return EVAL_BODY_AGAIN;
        }
        return SKIP_BODY;
    }
    
    /**
     * Fin du tag, réinitialisation des members
     */
    public int doEndTag() throws JspException
    {
        try
        {
            if(this.bodyContent != null)
            {
                String content = this.bodyContent.getString();
                this.bodyContent.clearBody();
                this.pageContext.getOut().println(content);
            }
        }
        catch (IOException e)
        {
            throw new JspException(e);
        }
        
        this.pageContext.removeAttribute(this.id);
        this.tables = null;
        this.currentIndex = -1;
        return EVAL_PAGE;
    }
    
    /**
     * Cette méthode prépare la liste de tables et se positionne
     * au premier élément.
     * @return Indique s'il y a un premier élément
     */
    private boolean moveFirst() throws JspException
    {
        System.out.println("debut moveFirst()");
        // On doit d'abord vérifier s'il s'agit de la première fois
        // qu'on entre dans la page. Dans ce cas là, il faut utiliser
        // la méthode d'initialisation pour récupérer les tables
        HttpServletRequest request = (HttpServletRequest)this.pageContext.getRequest();
        String action = request.getParameter(this.id + "_action");
        if(action == null || action.equals(""))
        {
            System.out.println("this.tables = this.callInitializationMethod()");
            this.tables = this.callInitializationMethod();
        }
        else
        {
            // La page revient d'un post, on doit donc récupérer l'information
            // nécessaire pour rebâtir les tables. On doit également traiter
            // les actions d'ajout et de suppression
            int removedIndex;
            if(action.startsWith("delete:"))
            {
                removedIndex = Integer.parseInt(action.substring(7));
            }
            else
            {
                removedIndex = -1;
            }
            
            Vector<Hashtable> vector = new Vector<Hashtable>();
            for(int i = 0; i < this.fieldsArray.length; i++)
            {
                String[] values = request.getParameterValues(this.fieldsArray[i]);
                System.out.println(this.fieldsArray[i] + (values == null ? " est null" : " a " + values.length + " valeur(s)"));
                if(values != null)
                {
                    int k = 0;
                    for(int j = 0; j < values.length; j++)
                    {
                        if(j != removedIndex)
                        {
                            Hashtable<String, String> table;
                            if(vector.size() > k)
                            {
                                table = (Hashtable<String, String>)vector.elementAt(k);
                            }
                            else
                            {
                                table = new Hashtable<String, String>();
                                vector.add(table);
                            }
                            k++;
                            
                            table.put(this.fieldsArray[i], values[j]);
                        }
                    }
                }
            }
            
            if(action.equals("add"))
            {
                Hashtable<String, String> table = new Hashtable<String, String>();
                for(int i = 0; i < this.fieldsArray.length; i++)
                {
                    table.put(this.fieldsArray[i], "");
                }
                vector.add(table);
            }
            this.tables = vector.elements();
        }
        
        System.out.println("fin moveFirst()");
        return this.moveNext();
    }
    
    /**
     * Cette méthode passe au prochain élément
     * @return Indique s'il y a un prochain élément
     */
    private boolean moveNext() throws JspException
    {
        System.out.println("compiere.custom.ManipulableStructureTag.moveNext()");
        if(this.tables.hasMoreElements())
        {
            this.currentIndex++;
            Hashtable currentTable = (Hashtable)this.tables.nextElement();
            this.pageContext.setAttribute(this.id, currentTable, PageContext.PAGE_SCOPE);
            return true;
        }
        return false;
    }
    
    /**
     * Lance la méthode d'initialisation
     */
    private Enumeration callInitializationMethod() throws JspException
    {
        try
        {
            Class initializationClass = Class.forName(this.initializationMethod.substring(0, this.initializationMethod.lastIndexOf(".")));
            Method initializationMethod = initializationClass.getMethod(this.initializationMethod.substring(this.initializationMethod.lastIndexOf(".") + 1), new Class[] {PageContext.class});
            return (Enumeration)initializationMethod.invoke(null, new Object[] {this.pageContext});
        }
        catch (Exception e)
        {
            throw new JspException("Erreur dans l'appel de la méthode d'initialisation", e);
        }
    }
}
