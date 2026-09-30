/*
 * Created on 8 juil. 2005
 */
package solstice.custom;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author frafor01
 */
public class DynamicValueTag extends TagSupport 
{
    private String parentId = null;
    private String formatMethod = null;
    private String nullValue = null;
    
    public void setNullValue(String nullValue) {this.nullValue = nullValue;}
    
    /** (optionnel)
     * Nom d'une méthode statique du type Object -> String
     */
    public void setFormatMethod(String formatMethod)
    {
        this.formatMethod = formatMethod;
    }
    
    /** (optionnel)
     * Nom du dynamicStructure associé
     */
    public void setParentId(String parentId)
    {
        this.parentId = parentId;
    }
    
    /**
     * Retourne le dynamicStructure associé
     */
    public DynamicStructureTag findAncestor()
    {
        if(this.parentId != null)
        {
            DynamicStructureTag current = (DynamicStructureTag)findAncestorWithClass(this, DynamicStructureTag.class);
            while(current.getId() == null || !current.getId().equals(this.parentId))
            {
                current = (DynamicStructureTag)findAncestorWithClass(current, DynamicStructureTag.class);
            }
            return current;
        }
        return (DynamicStructureTag)findAncestorWithClass(this, DynamicStructureTag.class);
    }
    
    /**
     * Retrouve, formatte et affiche la valeur
     */
	public int doEndTag() throws JspTagException
	{
		try
		{
			DynamicStructureTag struct = findAncestor();
			pageContext.getOut().print(this.format(struct.getRSValue(this.getId())));
		}
		catch (Exception e)
		{
			throw new JspTagException (e.toString());
		}
		this.parentId = null;
		this.formatMethod = null;
	    this.nullValue = null;
		return EVAL_PAGE;
	}
	
	/**
	 * S'il y a une méthode de formatage d'associée, on l'utilise, sinon on fait
	 * simplement un toString sur la valeur 
	 */
	private String format(Object value)
	{
	    if(this.formatMethod != null)
	    {
	        String className = this.formatMethod.substring(0, this.formatMethod.lastIndexOf("."));
	        String methodName = this.formatMethod.substring(this.formatMethod.lastIndexOf(".") + 1);
	  
	        try
	        {
		        Class c = Class.forName(className);
		        Method m = c.getMethod(methodName, new Class[] {Object.class});
		        return (String)m.invoke(null, new Object[] {value});
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
	    }
	    if(value == null)
	    {
	        if(this.nullValue != null)
	        {
	            return this.nullValue;
	        }
	        return "";
	    }
	    return value.toString();
	}
}
