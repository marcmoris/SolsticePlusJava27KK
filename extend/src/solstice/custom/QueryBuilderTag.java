/*
 * Created on 20 juil. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.custom;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class QueryBuilderTag extends QueryBaseTag implements IVariableCondition
{
    /*
     * Attributes
     */
    private String checkMethod;
    private String groupBy = null;
    private Vector<String> conditions = new Vector<String>();
    private Vector<String> havingConditions = new Vector<String>();
    
    /*
     * Setters
     */
    public void setCheckMethod(String checkMethod) {this.checkMethod = checkMethod;}
    public void setGroupBy(String groupBy) {this.groupBy = groupBy;}
    
    /**
     * On vérifie si une condition doit être utilisée
     */
    public boolean useCondition(String id)
    {
        String className = this.checkMethod.substring(0, this.checkMethod.lastIndexOf("."));
        String methodName = this.checkMethod.substring(this.checkMethod.lastIndexOf(".") + 1);
  
        boolean check = false;
  
        try
        {
	        Class c = Class.forName(className);
	        Method m = c.getMethod(methodName, new Class[] {PageContext.class, String.class});
	        check = ((Boolean)m.invoke(null, new Object[] {pageContext, id})).booleanValue();
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
        return check;
    }
    
    public void addCondition(String condition)
    {
        this.conditions.add(condition);
    }
    
    public void addHavingCondition(String havingCondition)
    {
        this.havingConditions.add(havingCondition);
    }
    
    public int doStartTag() throws JspException
    {
        this.conditions.clear();
        this.havingConditions.clear();
        return super.doStartTag();
    }
    
    public int doAfterBody()
    {
		BodyContent bc = getBodyContent();
		this.sql = bc.getString();
		if(this.conditions.size() > 0)
		{
		    this.sql += " where " + (String)this.conditions.elementAt(0);
		    for(int i = 1; i < this.conditions.size(); i++)
		    {
		        this.sql += " and " + (String)this.conditions.elementAt(i);
		    }
		}
		bc.clearBody();
		if(this.groupBy != null)
		{
		    this.sql += " group by " + this.groupBy;
		}
		this.groupBy = null;
		
		if(this.havingConditions.size() > 0)
		{
		    this.sql += " having " + (String)this.havingConditions.elementAt(0);
		    for(int i = 1; i < this.havingConditions.size(); i++)
		    {
		        this.sql += " and " + (String)this.havingConditions.elementAt(i);
		    }
		}
		System.out.println("QueryBuilderTag : "+ this.sql);

		return SKIP_BODY;
    }
}
