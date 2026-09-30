/*
 * Created on 25 juil. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.custom;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class IfTrueTag extends BodyTagSupport
{
    public int doStartTag() throws JspException
    {
        if(((IfTag)this.getParent()).getCheck())
            return EVAL_BODY_INCLUDE;
        return SKIP_BODY;
    }
}
