/*
 * Created on 3 août 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.custom;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IVariableCondition
{
    public boolean useCondition(String id);
    public void addCondition(String condition);
    public void addHavingCondition(String havingCondition);
}
