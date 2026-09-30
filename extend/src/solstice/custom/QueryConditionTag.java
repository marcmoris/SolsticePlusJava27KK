/*
 * Created on 20 juil. 2005
 */
package solstice.custom;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * @author frafor01
 */
public class QueryConditionTag extends BodyTagSupport
{
    public int doStartTag() throws JspException
    {
        IVariableCondition builder = (IVariableCondition)findAncestorWithClass(this, IVariableCondition.class);
        if(builder.useCondition(this.getId()))
            return super.doStartTag();
        return SKIP_BODY;
    }
    
    public int doAfterBody() throws JspException
    {
		BodyContent bc = getBodyContent();
		String sql = bc.getString();
		bc.clearBody();
		IVariableCondition builder = (IVariableCondition)findAncestorWithClass(this, IVariableCondition.class);
        builder.addCondition(sql);
		return SKIP_BODY;
    }
}
