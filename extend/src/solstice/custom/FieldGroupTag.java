/*
 * Created on 28 juil. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.custom;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FieldGroupTag extends TagSupport
{
    /*
     * Attributes
     */
    private String name;
    
    /*
     * members
     */
    private IField currentField = null;
    
    /*
     * setters
     */
    public void setName(String name) {this.name = name;}
    public void setCurrentField(IField field) {this.currentField = field;}

    /*
     * getters
     */
    public String getName() {return this.name;}
    public IField getCurrentField() {return this.currentField;}
   
    /**
     * Initialisation
     */
    public int doStartTag() throws JspException
    {
        this.currentField = null;
        return EVAL_BODY_INCLUDE;
    }
    
    /**
     * On renvoit le field au grid parent
     */
    public int doEndTag() throws JspException
    {
        if(this.currentField != null)
        {
            IGrid grid = (IGrid)this.getParent();
	        grid.addField(this.currentField);
        }
        return EVAL_PAGE;
    }
}
