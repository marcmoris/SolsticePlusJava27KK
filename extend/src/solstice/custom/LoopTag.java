/*
 * Created on 11 juil. 2005
 */
package solstice.custom;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * @author frafor01
 *
 * Boucle le body
 */
public class LoopTag extends BodyTagSupport
{
    /*
     * Attributes
     */
    private int iterations = 10;
    
    /*
     * Setters
     */
    public void setIterations(int iterations) {this.iterations = iterations;}

    public LoopTag()    
    {        
        super();    
    }

    /**
     * On boucle pour la première fois si l'itération est plus grande que 0
     */
    public int doStartTag() throws JspTagException    
    {        
        if(iterations>0)        
        {            
            return EVAL_BODY_BUFFERED;        
        }        
        else        
        {            
            return SKIP_BODY;        
        }    
    }

    /**
     * Boucle encore si l'itération le permet
     */
    public int doAfterBody() throws JspTagException    
    {        
        if(iterations > 1)        
        {            
            iterations--;            
            return EVAL_BODY_AGAIN;        
        }        
        else        
        {            
            return SKIP_BODY;        
        }    
    }

    /**
     * Print le résultat
     */
    public int doEndTag() throws JspTagException    
    {        
        try        
        {            
            if(bodyContent != null)            
            {
                bodyContent.writeOut(bodyContent.getEnclosingWriter());
                bodyContent.clearBody();
            }
        }
        catch(java.io.IOException e)        
        {            
            throw new JspTagException("IO Error: " + e.getMessage());        
        }
        return EVAL_PAGE;    
    }
}