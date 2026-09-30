/*
 * Created on 19 juil. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.custom;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.Tag;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MemoryStructureTag implements BodyTag
{
    private int totalIteration = 0;
    private int toRemove = -1;
    private String initializeHandler;
    private int minValue = 0;
    private int iterations = 0;
    private String id;
    private Tag parent;
    private BodyContent bodyContent;
    private PageContext pageContext;
    private boolean nouveau = false;
    
    public void setInitializeHandler(String initializeHandler)
    {
        this.initializeHandler = initializeHandler;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public void setMinValue(int minValue)
    {
        this.minValue = minValue;
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.BodyTag#setBodyContent(javax.servlet.jsp.tagext.BodyContent)
     */
    public void setBodyContent(BodyContent bodyContent)
    {
        this.bodyContent = bodyContent;
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.BodyTag#doInitBody()
     */
    public void doInitBody() throws JspException
    {
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.IterationTag#doAfterBody()
     */
    public int doAfterBody() throws JspException
    {
	 	if(this.iterations > 1)        
	 	{            
	 	    if(this.iterations == this.toRemove)
	 	        this.iterations --;
	 	    this.iterations--;            
	 		return EVAL_BODY_AGAIN;        
	 	}        
	 	else        
	 	{            
	 		return SKIP_BODY;        
	 	}    
    }
    
    public boolean isNew()
    {
        if(this.nouveau && this.iterations == 1)
            return true;
        return false;
    }
    
    public int getCurrentNumber()
    {
        return this.totalIteration - this.iterations + 1;
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.Tag#setPageContext(javax.servlet.jsp.PageContext)
     */
    public void setPageContext(PageContext pageContext)
    {
        this.pageContext = pageContext;
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.Tag#setParent(javax.servlet.jsp.tagext.Tag)
     */
    public void setParent(Tag parent)
    {
        this.parent = parent;
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.Tag#getParent()
     */
    public Tag getParent()
    {
        return this.parent;
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.Tag#doStartTag()
     */
    public int doStartTag() throws JspException
    {
        System.out.println("MemoryStructure::doStartTag()");
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        
        if(request.getParameter(this.id + "_iterations") == null
                || (request.getParameter(this.id + "_method") != null 
                        && request.getParameter(this.id + "_method").equals("reset")))
        {
            int i = this.initializeHandler.lastIndexOf(".");
            String className = this.initializeHandler.substring(0, i);
            String methodName = this.initializeHandler.substring(i+1);
            try
            {
                this.iterations = ((Integer)Class.forName(className).getMethod(methodName, null).invoke(null, null)).intValue();
            }
            catch (ClassNotFoundException e)
            {
                this.iterations = this.minValue;
                e.printStackTrace(System.out);
            }
            catch (InvocationTargetException e)
            {
                this.iterations = this.minValue;
                e.printStackTrace(System.out);
            }
            catch (IllegalAccessException e)
            {
                this.iterations = this.minValue;
                e.printStackTrace(System.out);
            }
            catch (NoSuchMethodException e)
            {
                this.iterations = this.minValue;
                e.printStackTrace(System.out);
            }
        }
        else
        {
	        try
	        {
	            this.iterations = Integer.parseInt(request.getParameter(this.id + "_iterations"));
	            if(this.iterations < this.minValue)
	                this.iterations = this.minValue;
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace(System.out);
	            this.iterations = this.minValue;
	        }
        }
        System.out.println("iterations = " + this.iterations);
        
        if(request.getParameter(this.id + "_method") != null 
                && request.getParameter(this.id + "_method").equals("newRow"))
        {
            this.nouveau = true;
            this.iterations ++;
        }
        else
        {
            this.nouveau = false;
        }

        if(this.iterations > this.minValue && request.getParameter(this.id + "_method") != null 
                && request.getParameter(this.id + "_method").indexOf("deleteRow:") == 0)
        {
            this.toRemove = Integer.parseInt(request.getParameter(this.id + "_method").split(":")[1]);
            this.toRemove = this.iterations - this.toRemove + 2;
        }
        else
        {
            this.toRemove = -1;
        }
        
        this.totalIteration = this.iterations;

        try
        {
            JspWriter out = pageContext.getOut();
            out.println("<input type=\"hidden\" name=\"" + this.id + "_iterations\" value=\"" + (this.toRemove != -1 ? this.iterations - 1 : this.iterations) + "\">");
            out.println("<input type=\"hidden\" name=\"" + this.id + "_method\">");
        }
        catch (IOException e)
        {
            e.printStackTrace(System.out);
        }

        if(this.iterations > 0)        
	 	{            
	 		return EVAL_BODY_BUFFERED;        
	 	}        
	 	else        
	 	{            
	 		return SKIP_BODY;        
	 	}    
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    public int doEndTag() throws JspException
    {
	 	try        
		{            
	 		if(bodyContent != null)            
	 			bodyContent.writeOut(bodyContent.getEnclosingWriter());        
	 	}        
	 	catch(java.io.IOException e)        
		{            
	 		throw new JspTagException("IO Error: " + e.getMessage());        
	 	}        
	 	return EVAL_PAGE;    
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.Tag#release()
     */
    public void release()
    {
    }

}
