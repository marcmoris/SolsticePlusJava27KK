/*
 * Created on 5 juil. 2005
 */
package solstice.custom;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.compiere.util.DB;

/**
 * @author frafor01
 *
 * Ce tag crée un comboBox à partir d'une requète SQL. La requète en question
 * doit être placée dans le body du tag :
 * <td:comboBoxTag id="yo">select P_Employee_ID, Name from P_Employee</td:comboBoxTag>
 * 
 * Le premier champ représente la value et le deuxième représente le display
 */
public class ComboBoxTag extends BodyTagSupport
{
    private String onFocus = "";
    private String onBlur = "";
    private String style = "";
    private String size = "";
    private Method formatMethodInstance = null;
    private String formatMethod = null;
    private String onChange = "";
    private String sql = "";
    private String className = null;
    private String selectedValue = null;
    private boolean allowNull = true;
    private boolean getPostValue = false;
    private String readOnly = "";

    public void setSize(int size) {this.size = " size=\"" + size + "\" ";}
    public void setStyle(String style) {this.style = " style=\"" + style + "\" ";}
    public void setOnFocus(String onFocus) {this.onFocus = " onFocus=\"" + onFocus + "\" ";}
    public void setOnBlur(String onBlur) {this.onBlur = " onBlur=\"" + onBlur + "\" ";}
    public void setReadOnly(boolean readOnly) {this.readOnly = readOnly ? " disabled" : "";}
    
    public void setFormatMethod(String formatMethod)
    {
        this.formatMethod = formatMethod;
    }
    
    public void setOnChange(String onChange)
    {
        this.onChange = " onChange=\"" + onChange + "\" ";
    }
    
	/**
	 * Le nom de la class CSS (optionnel)
	 * @param className
	 */
	public void setClassName(String className)
	{
		this.className = className;
	}
	
	/**
	 * La valeur sélectionnée (optionnel)
	 * @param selectedValue
	 */
	public void setSelectedValue(String selectedValue)
	{
		this.selectedValue = selectedValue;
	}
	
	/**
	 * Indique si on doit ajouter un champ null
	 * @param allowNull
	 */
	public void setAllowNull(boolean allowNull)
	{
		this.allowNull = allowNull;
	}
	
	public void setGetPostValue(boolean getPostValue) {this.getPostValue = getPostValue;}
	
	/**
	 * On récupère le contenue du body i.e. la requète
	 */
	public int doAfterBody() throws JspTagException
	{
		try
		{
			BodyContent bc = getBodyContent();
			this.sql = bc.getString();
			bc.clearBody();
		}
		catch (Exception e)
		{
			throw new JspTagException ("doAfterBody : " + e.toString());
		}
		
		return SKIP_BODY;
	}
	
	/**
	 * On lance la requète et on crée le comboBox. Veuillez notter que le
	 * id du tag devient le name du contrôle dans le formulaire
	 */
	public int doEndTag() throws JspTagException
	{
		JspWriter out = pageContext.getOut();
		
		if(this.getPostValue && ((HttpServletRequest) this.pageContext.getRequest()).getParameter(this.id) != null)
		{
		    this.selectedValue = ((HttpServletRequest) this.pageContext.getRequest()).getParameter(this.id);
		}
		try
		{
		    String autoComplete = " onKeyPress=\"autoSelect(this);\"";
			//autoComplete = "";//BUG AVEC AUTOCOMPLETE' ON DISABLE POUR LINSTANT
			out.println("<select name=\"" + getId() + "\" id=\"" + getId() + "\" " + (className != null && !className.equals("") ? "class=\"" + className + "\"" : "") + this.onFocus + this.onBlur + this.style + this.size + this.onChange + autoComplete + this.readOnly + ">");
			
			PreparedStatement stmt = DB.prepareStatement(sql, null);
			ResultSet rs = stmt.executeQuery();
			
			if(allowNull)
			{
				out.println("  <option " + (selectedValue != null && !selectedValue.equals("") ? "" : "selected") + "></option>");
			}
			
			while(rs.next())
			{
			    if(this.formatMethod == null)
			    {
			        out.println("  <option value=\"" + rs.getString(1) + "\" " + (selectedValue != null && selectedValue.equals(rs.getString(1)) ? "selected" : "") + ">" + rs.getString(2) + "</option>");
			    }
			    else
			    {
			        out.println("  <option value=\"" + rs.getString(1) + "\" " + (selectedValue != null && selectedValue.equals(rs.getString(1)) ? "selected" : "") + ">" + format(rs) + "</option>");
			    }
			}
			
			rs.close();
			stmt.close();
			
			out.println("</select>");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new JspTagException("doEndTag : " + e.toString());
		}
		
		this.style = "";
		this.size = "";
		this.onBlur = "";
		this.onFocus = "";
		this.onChange = "";
		this.readOnly = "";
		this.formatMethod = null;
		this.formatMethodInstance = null;
		this.selectedValue = null;
		getPostValue = false;
		return EVAL_PAGE;
	}
	
	private String format(ResultSet rs) throws SQLException
	{
	    if(this.formatMethodInstance == null)
	    {
	        String className = this.formatMethod.substring(0, this.formatMethod.lastIndexOf("."));
	        String methodName = this.formatMethod.substring(this.formatMethod.lastIndexOf(".") + 1);
	  
	        boolean check = false;
	  
	        try
	        {
		        Class c = Class.forName(className);
		        this.formatMethodInstance = c.getMethod(methodName, new Class[] {ResultSet.class});
	        }
	        catch (NoSuchMethodException e)
	        {
              e.printStackTrace(System.out);
	        }
	        catch (ClassNotFoundException e)
	        {
              e.printStackTrace(System.out);
	        }
	    }
	    
	    try
	    {
	        return (String)this.formatMethodInstance.invoke(null, new Object[] {rs});
	    }
        catch (InvocationTargetException e)
        {
              e.printStackTrace(System.out);
        }
        catch (IllegalAccessException e)
        {
              e.printStackTrace(System.out);
        }
        return rs.getString(2);
	}
}
