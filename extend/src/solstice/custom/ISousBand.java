/*
 * Created on 28 juil. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.custom;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface ISousBand
{
    /**
     * Retourne le nom du champ dont on veut l'id de la rangée
     */
    public String getFieldId();
    
    /**
     * Prépare l'ouverture du band. Retourne VRAI si le band doit
     * s'ouvrir et FAUX sinon
     */
    public boolean open(PageContext pageContext, Object id) throws JspException;
    
    /**
     * Affiche le grid
     */
    public void drawGrid(JspWriter out, String selectedCss) throws JspException;
}
