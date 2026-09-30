/*
 * Created on 28 juil. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.custom;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.compiere.util.DB;

/**
 * @author frafor01
 *
 * Crée un grid pouvant contenir des sous-bands
 */
public class GridTag extends GridBaseTag
{
    /*
     * Member
     */
    private ISousBand sousBand = null;
    
    /**
     * Ajout d'un sous-band
     */
    public void setSousBand(ISousBand sousBand) {this.sousBand = sousBand;}
    
    /**
     * Initialisation
     */
    public int doStartTag() throws JspException
    {
        this.fields.clear();
        this.sousBand = null;
        return EVAL_BODY_INCLUDE;
    }
    
    /**
     * On affiche le grid
     */
    public int doEndTag() throws JspException
    {
        JspWriter out = pageContext.getOut();
        try
        {
            // On lance d'abord la requête et on bâtit l'entête des colonnes
            PreparedStatement stmt = DB.prepareStatement(this.query, null);
            ResultSet rs = stmt.executeQuery();
            
            out.println("<table border=\"0\" width=\"100%\">");
            out.println("<tr>");
            for(int i = 0; i < this.fields.size(); i++)
            {
                IField field = (IField)this.fields.elementAt(i);
                out.println(field.getTDLabel(this.labelClassName));
            }
            out.println("</tr>");

            // Pour chaque enregistrement dans le résultat
            boolean alt = false;
            while(rs.next())
            {
                // On doit d'abord vérifier si on doit afficher un sous-band
                boolean hasSousBand = this.sousBand == null ? false : this.sousBand.open(this.pageContext, rs.getObject(this.sousBand.getFieldId()));
                out.println("<tr>");
                for(int i = 0; i < this.fields.size(); i++)
                {
                    // On récupère le formattage des colonnes et on choisit la
                    // bonne feuille de style pour les cellules du grid
                    IField field = (IField)this.fields.elementAt(i);
                    if(hasSousBand)
                    {
                        out.println(field.getTDCell(this.selectedClassName, rs, true));
                    }
                    else
                    {
	                    if(alt && this.cellClassAltName != null)
	                        out.println(field.getTDCell(this.cellClassAltName, rs, false));
	                    else
	                        out.println(field.getTDCell(this.cellClassName, rs, false));
                    }
                }
                out.println("</tr>");
                
                alt = !alt;
                // Si le sous-band a été ouvert, on l'affiche à la suite de la rangée
                if(hasSousBand)
                {
                    out.println("<tr>");
                    this.sousBand.drawGrid(out, this.selectedClassName);
                    out.println("</tr>");
                }
            }
            
            out.println("</table>");

            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            throw new JspException(e);
        }
        catch (IOException e)
        {
            throw new JspException(e);
        }
        return EVAL_PAGE;
    }
}