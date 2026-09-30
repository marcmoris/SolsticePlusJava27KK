/*
 * Created on 2005-09-22
 */
package solstice.custom;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author frafor01
 *
 * default case (voir SelectCaseTag)
 */
public class DefaultTag extends TagSupport
{
    public DefaultTag()
    {
        super();
    }

    /**
     * On fait l'évaluation
     */
    public int doStartTag() throws JspException
    {
        SelectCaseTag selectCase = (SelectCaseTag)findAncestorWithClass(this, SelectCaseTag.class);
        if(selectCase.checkValue(null))
        {
            return EVAL_BODY_INCLUDE;
        }
        return SKIP_BODY;
    }
}
