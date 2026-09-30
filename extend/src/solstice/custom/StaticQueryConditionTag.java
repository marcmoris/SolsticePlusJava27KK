/*
 * Created on Aug 30, 2005
 */
package solstice.custom;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * @author frafor01
 *
 * Ajoute une condition sans aucune vérification
 */
public class StaticQueryConditionTag extends BodyTagSupport
{
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