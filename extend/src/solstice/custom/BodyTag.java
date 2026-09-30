/*
 * Created on 8 août 2005
 */
package solstice.custom;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author frafor01
 */
public class BodyTag extends TagSupport
{
    public int doStartTag() throws JspException
    {
        if(((IBodyOwner)this.getParent()).evaluateBody())
            return EVAL_BODY_INCLUDE;
        return SKIP_BODY;
    }
}
