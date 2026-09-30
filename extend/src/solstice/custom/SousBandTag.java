/*
 * Created on 28 juil. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.custom;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.compiere.util.DB;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SousBandTag extends GridBaseTag
{
    /**
     * Cette classe sera envoyée au grid parent. Elle est à concerver les informations
     * du taglib jusqu'à leur utilisation. Puisqu'elle implémente l'interface ISousBand,
     * les seules méthodes que le parent devra connaître sont les méthodes open(...),
     * getFieldId() et drawGrid(...)
     */
    private class SousBand implements ISousBand
    {
        /*
         * members
         */
        private String fieldId;
        private Method checkMethod;
        private int colSpan;
    	private String labelClassName;
    	private String cellClassName;
    	private String cellClassAltName;
    	private String selectedClassName;
    	private Object currentId = null;
    	private String query;
    	private Vector fields;
        
        public String getFieldId() {return this.fieldId;}
        
        /**
         * Ce constructeur récupère les informations du tag
         */
        public SousBand(String fieldId, Method checkMethod, int colSpan, String labelClassName,
                String cellClassName, String cellClassAltName, String selectedClassName, 
                String query, Vector fields)
        {
            this.fieldId = fieldId;
            this.checkMethod = checkMethod;
            this.colSpan = colSpan;
            this.labelClassName = labelClassName;
            this.cellClassName = cellClassName;
            this.cellClassAltName = cellClassAltName;
            this.selectedClassName = selectedClassName;
            this.query = query;
            this.fields = fields;
        }
        
        /**
         * Pour ouvrir le sous-band, on doit lancer la méthode de vérification
         * pour savoir si l'enregistrement courrant (selon l'id) a été sélectionné.
         * Si oui, on concerve l'id courrant et on retourne VRAI. Sinon, l'id
         * courrant est mis à null et on retourne FAUX.
         */
        public boolean open(PageContext pageContext, Object id) throws JspException
        {
            boolean check = false;
            try
            {
                check = ((Boolean)this.checkMethod.invoke(null, new Object[] {pageContext, id})).booleanValue();
            }
            catch (InvocationTargetException e)
            {
                throw new JspException(e);
            }
            catch (IllegalAccessException e)
            {
                throw new JspException(e);
            }
            if(check)
                this.currentId = id;
            else
                this.currentId = null;
            return check;
        }
        
        /**
         * On formatte le string pour qu'il soit correctement lu par SQL
         */
        private String idToString()
        {
            if(this.currentId.getClass().equals(String.class) || this.currentId.getClass().equals(Date.class))
                return "'" + this.currentId.toString() + "'";
            return this.currentId.toString();
        }
        
        /**
         * Création du grid
         */
        public void drawGrid(JspWriter out, String selectedCss) throws JspException
        {
            // Si le sous-band a été ouvert
            if(this.currentId != null)
            {
                // On ajuste la requête avec le bon id
                String sql = this.query.replaceAll("@@", this.idToString());
                try
                {
                    PreparedStatement stmt = DB.prepareStatement(sql, null);
                    ResultSet rs = stmt.executeQuery();
                    
                    // Création de l'entête
                    out.println("<td colspan=\"" + this.colSpan + "\">");
                    out.println("<table class=\"" + selectedCss + "\" width=\"100%\">");
                    out.println("<tr>");
                    for(int i = 0; i < this.fields.size(); i++)
                    {
                        IField field = (IField)this.fields.elementAt(i);
                        out.println(field.getTDLabel(this.labelClassName));
                    }
                    out.println("</tr>");

                    boolean alt = false;
                    // Pour chaque enregistrement, on sélectionne la bonne feuille de style
                    // et on l'ajoute à la fin du grid
                    while(rs.next())
                    {
                        out.println("<tr>");
                        for(int i = 0; i < this.fields.size(); i++)
                        {
                            IField field = (IField)this.fields.elementAt(i);
                            if(alt && this.cellClassAltName != null)
                                out.println(field.getTDCell(this.cellClassAltName, rs, false));
                            else
                                out.println(field.getTDCell(this.cellClassName, rs, false));
                        }
                        out.println("</tr>");
	                    alt = !alt;
                    }
                    
                    out.println("</table>");
                    out.println("</td>");

                    rs.close();
                    stmt.close();
                }
                catch (SQLException e)
                {
                    throw new JspException(e);
                }
                catch (IOException e)
                {
                    throw new JspException(e);
                }
            }
            else
            {
                throw new JspException("Le sous band n'est pas ouvert");
            }
        }
    }
    
    /*
     * Attributs
     */
    private String fieldId;
    private String checkMethod;
    private int colSpan;

	/*
	 * setters
	 */
	public void setFieldId (String fieldId) {this.fieldId = fieldId;}
	public void setCheckMethod (String checkMethod) {this.checkMethod = checkMethod;}
	public void setColSpan (int colSpan) {this.colSpan = colSpan;}
	
	/**
	 * Création de la méthode d'après le nom passé en attribut
	 */
	private Method getMethod() throws JspException
	{
        String className = this.checkMethod.substring(0, this.checkMethod.lastIndexOf("."));
        String methodName = this.checkMethod.substring(this.checkMethod.lastIndexOf(".") + 1);
  
        try
        {
	        Class c = Class.forName(className);
	        return c.getMethod(methodName, new Class[] {PageContext.class, Object.class});
        }
        catch (NoSuchMethodException e)
        {
            throw new JspException(e);
        }
        catch (ClassNotFoundException e)
        {
            throw new JspException(e);
        }
	}
	
	/**
	 * Initialisation
	 */
	public int doStartTag() throws JspException
	{
	    this.fields.clear();
	    return EVAL_BODY_INCLUDE;
	}
	
	/**
	 * On envoit les informations du sous-band au grid parent
	 */
	public int doEndTag() throws JspException
	{
	    SousBand sb = new SousBand(this.fieldId, this.getMethod(), this.colSpan,
	            this.labelClassName, this.cellClassName, this.cellClassAltName,
	            this.selectedClassName, this.query, this.fields);
	    ((GridTag)this.getParent()).setSousBand(sb);
	    return EVAL_PAGE;
	}
}
