/*
 * Created on 25 juil. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.custom;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class IfTag extends BodyTagSupport
{
    private String condition;
    private boolean check;
    
    public void setCondition(String condition)
    {
        this.condition = condition;
    }
    
    public boolean getCheck()
    {
        return this.check;
    }
    
    public int doStartTag() throws JspException
    {
        String className = this.condition.substring(0, this.condition.lastIndexOf("."));
        String methodName = this.condition.substring(this.condition.lastIndexOf(".") + 1);
  
        check = false;
  
        try
        {
	        Class c = Class.forName(className);
	        Method m = c.getMethod(methodName, new Class[] {PageContext.class});
	        check = ((Boolean)m.invoke(null, new Object[] {pageContext})).booleanValue();
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
        
        return EVAL_BODY_INCLUDE;
    }
}
