/*
 * Created on 2005-09-22
 */
package solstice.custom;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author frafor01
 *
 * le case d'un switch (voir SelectCaseTag)
 */
public class CaseTag extends TagSupport
{
    /*
     * Attribute
     */
    private Object value;
    
    /*
     * Setters
     */
    public void setValue(Object value) {this.value = value;}
    
    public CaseTag()
    {
        super();
    }

    /**
     * On fait l'évaluation
     */
    public int doStartTag() throws JspException
    {
        SelectCaseTag selectCase = (SelectCaseTag)findAncestorWithClass(this, SelectCaseTag.class);
        if(selectCase.checkValue(this.value))
        {
            return EVAL_BODY_INCLUDE;
        }
        return SKIP_BODY;
    }
}
