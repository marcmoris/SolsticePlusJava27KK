/*
 * Created on 27 juil. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.custom;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author frafor01
 *
 * Crée deux listBox en html permettant d'échanger des enregistrements d'une liste à une autre
 */
public class SwitchTag extends TagSupport
{
    /**
     * La classe qui doit obligatoirement être contenue dans les énumérations pour
     * garder un historique sur les transferts
     */
    private static class MyDictionaryEntry extends DictionaryEntry
    {
        private boolean changed;
        
        public void setChanged(boolean changed) {this.changed = changed;}
        public boolean getChanged() {return this.changed;}
        public void invert() {this.changed = !this.changed;}
        
        public MyDictionaryEntry(Object key, Object value, boolean changed)
        {
            super(key, value);
            this.changed = changed;
        }
        
        public MyDictionaryEntry(String input)
        {
            super(input.substring(1, input.indexOf("|")), input.substring(input.indexOf("|") + 1));
            this.changed = input.substring(0, 1).equals("1");
        }
        
        public String toString()
        {
            return (changed ? "1" : "0") + this.getKey().toString() + "|" + this.getValue().toString();
        }
    }
    
    /**
     * Retourne une nouvelle instance des entrées qui doivent obligatoirement être
     * utilisées dans les énumérations des initialisateurs
     */
    public static DictionaryEntry newEntryInstance(Object key, Object value)
    {
        return new MyDictionaryEntry(key, value, false);
    }
    
    /*
     * attributs
     */
    private String leftSufix;
    private String rightSufix;
    private String leftInitialize;
    private String rightInitialize;
    private String leftTitle;
    private String rightTitle;
    private String labelCss = "tdlabel";
    private String fieldCss = "tdfield";
    
    /*
     * setters
     */
    public void setLeftSufix(String leftSufix) {this.leftSufix = leftSufix;}
    public void setRightSufix(String rightSufix) {this.rightSufix = rightSufix;}
    public void setLeftInitialize(String leftInitialize) {this.leftInitialize = leftInitialize;}
    public void setRightInitialize(String rightInitialize) {this.rightInitialize = rightInitialize;}
    public void setLeftTitle(String leftTitle) {this.leftTitle = leftTitle;}
    public void setRightTitle(String rightTitle) {this.rightTitle = rightTitle;}
    public void setLabelCss(String labelCss) {this.labelCss = labelCss;}
    public void setFieldCss(String fieldCss) {this.fieldCss = fieldCss;}
    
    /**
     * Constructeur
     */
    public SwitchTag()
    {
        super();
    }
    
    /*
     * Noms utilisés
     */
    private String getHiddenName(boolean left) {return this.id + (left ? this.leftSufix : this.rightSufix);}
    private String getSelectName(boolean left) {return this.id + (left ? this.leftSufix : this.rightSufix) + "_list";}
    private String getButtonName(boolean left) {return this.id + (left ? this.leftSufix : this.rightSufix) + "_button";}
    private String getActionName() {return this.id + "_action";}
    
    private String[] split(String value)
    {
        String id = value.substring(0, value.indexOf("|"));
        String text = value.substring(value.indexOf("|") + 1);
        return new String[] {id, text};
    }
    
    /**
     * Si la page revient d'un post, on traite l'action et on retourne la nouvelle liste. Sinon, on retourne la liste
     * d'initialisation d'après les méthodes statiques passées en paramètres
     */
    private Enumeration retrieveList(boolean left)
    {
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        String action;
        
        // Si on revient d'un post, on traite l'action
        if(request.getParameter(this.getActionName()) != null && !((String)(action = request.getParameter(this.getActionName()))).equals(""))
        {
            // Si on doit supprimer un item de la liste
            if((action.equals("toLeft") && !left) || (action.equals("toRight") && left))
            {
                String id = request.getParameter(this.getSelectName(left));
                String[] values = request.getParameterValues(this.getHiddenName(left));
                
                Vector<MyDictionaryEntry> list = new Vector<MyDictionaryEntry>();
                
                for(int i = 0; i < values.length; i++)
                {
                    // On ne fait que pas ajouter le id à enlever
                    MyDictionaryEntry entry = new MyDictionaryEntry(values[i]);
                    if(!id.equals(entry.getKey()))
                    {
                        list.add(entry);
                    }
                }
                return list.elements();
            }
            // Si on doit ajouter à la liste, on doit rechercher l'élément dans l'autre
            else if((action.equals("toRight") && !left) || (action.equals("toLeft") && left))
            {
	            String id = request.getParameter(this.getSelectName(!left));
	            String[] values = request.getParameterValues(this.getHiddenName(!left));
	            
	            Vector<MyDictionaryEntry> list = new Vector<MyDictionaryEntry>();
	            
	            for(int i = 0; i < values.length; i++)
	            {
	                // On recherche l'élément
	                MyDictionaryEntry entry = new MyDictionaryEntry(values[i]);
	                if(id.equals(entry.getKey()))
	                {
	                    // Élément trouvé, on note le changement
	                    entry.invert();
	                    list.add(entry);
	                    break;
	                }
	            }
	            
	            // On récupère ensuite la liste originale
	            values = request.getParameterValues(this.getHiddenName(left));
	            if(values != null) // Dans le cas où la liste est vide
	            {
		            for(int i = 0; i < values.length; i++)
		            {
		                MyDictionaryEntry entry = new MyDictionaryEntry(values[i]);
		                list.add(entry);
		            }
	            }
	            
	            return list.elements();
            }
            // Sinon, aucun changement dans les listes
            // On récupère ensuite la liste originale
            Vector<MyDictionaryEntry> list = new Vector<MyDictionaryEntry>();
            String[] values = request.getParameterValues(this.getHiddenName(left));
            if(values != null) // Dans le cas où la liste est vide
            {
	            for(int i = 0; i < values.length; i++)
	            {
	                MyDictionaryEntry entry = new MyDictionaryEntry(values[i]);
	                list.add(entry);
	            }
            }
            return list.elements();
        }
        
        // C'est la première fois qu'on arrive dans le formulaire, donc on initialise la liste
        return this.getInitializeList(left);
    }
    
    /**
     * Exécution des méthodes d'initialisation
     */
    private Enumeration getInitializeList(boolean left)
    {
        String className;
        String methodName;

        if(left)
        {
            className = this.leftInitialize.substring(0, this.leftInitialize.lastIndexOf("."));
            methodName = this.leftInitialize.substring(this.leftInitialize.lastIndexOf(".") + 1);
        }
        else
        {
            className = this.rightInitialize.substring(0, this.rightInitialize.lastIndexOf("."));
            methodName = this.rightInitialize.substring(this.rightInitialize.lastIndexOf(".") + 1);
        }
  
        try
        {
	        Class c = Class.forName(className);
	        Method m = c.getMethod(methodName, new Class[] {PageContext.class});
	        return (Enumeration)m.invoke(null, new Object[] {pageContext});
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace(System.out);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace(System.out);
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace(System.out);
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace(System.out);
        }

        return new Vector().elements();
    }
    
    /**
     * Crée les hidden et le select
     */
    private String getList (boolean left)
    {
        String inputs = "";
        String options = "";
        
        Enumeration entries = this.retrieveList(left);
        
        while(entries.hasMoreElements())
        {
            DictionaryEntry entry = (DictionaryEntry) entries.nextElement();
            inputs += "<input type=\"hidden\" name=\"" + this.getHiddenName(left) +  "\" value=\"" + entry.toString() + "\">\n";
            options += "  <option value=\"" + entry.getKey() + "\">" + entry.getValue() + "</option>\n";
        }
        
        String html = inputs;
        html += "<select size=\"10\" name=\"" + this.getSelectName(left) + "\" onChange=\"this.form." + this.getButtonName(left) + ".disabled = false;\" style=\"width: 350px\">\n";
        html += options;
        html += "</select>";
        
        return html;
    }
    
    /**
     * Crée deux listBox en html permettant d'échanger des enregistrements d'une liste à une autre
     */
    public int doEndTag() throws JspException
    {
        JspWriter out = pageContext.getOut();
        
        try
        {
            // Début du tableau complet
            out.println("<input type=\"hidden\" name=\"" + this.getActionName() + "\" value=\"idle\">");
            out.println("<table width=\"100%\">");
            out.println("  <tr>");
            out.println("    <td width=\"50%\">");

            // Début du tableau de gauche
            out.println("      <table width=\"100%\" border=\"0\" cellspacing=\"1\" cellpadding=\"1\">");
            out.println("        <tr>");
            out.println("          <td class=\"" + this.labelCss + "\" align=\"center\">" + this.leftTitle + "</td>");
            out.println("        </tr>");
            out.println("        <tr>");
            out.println("          <td class=\"" + this.fieldCss + "\">");
            
            // Ajout de la liste de input et création du select
            out.println(this.getList(true));
            
            // Ajout des bouttons de transfert
            out.println("          </td>");
            out.println("        </tr>");
            out.println("      </table>");
            out.println("    </td>");
            out.println("    <td>");
            out.println("      <input type=\"button\" name=\"" + this.getButtonName(false) + "\" onClick=\"this.form." + this.getActionName() + ".value = 'toLeft'; this.form.submit();\" value=\"<<\" style=\"width: 40px;\" disabled><br>");
            out.println("      <input type=\"button\" name=\"" + this.getButtonName(true) + "\" onClick=\"this.form." + this.getActionName() + ".value = 'toRight'; this.form.submit();\" value=\">>\" style=\"width: 40px;\" disabled>");
            out.println("    </td>");
            out.println("    <td width=\"50%\">");

            // Début du tableau de droite
            out.println("      <table width=\"100%\" border=\"0\" cellspacing=\"1\" cellpadding=\"1\">");
            out.println("        <tr>");
            out.println("          <td class=\"" + this.labelCss + "\" align=\"center\">" + this.rightTitle + "</td>");
            out.println("        </tr>");
            out.println("        <tr>");
            out.println("          <td class=\"" + this.fieldCss + "\" align=\"right\">");
            
            // Ajout de la liste de input et création du select
            out.println(this.getList(false));
            
            // Fin du tableau principal
			out.println("          </td>");
			out.println("        </tr>");
			out.println("      </table>");
			out.println("    </td>");
			out.println("  </tr>");
			out.println("</table>");
        }
        catch (IOException e)
        {
            e.printStackTrace(System.out);
        }
        
        return EVAL_PAGE;
    }
}
