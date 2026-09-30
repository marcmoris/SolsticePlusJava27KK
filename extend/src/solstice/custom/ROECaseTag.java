package solstice.custom;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author frafor01
 * Cette classe définit une case dans le relevé d'emploi
 */
public class ROECaseTag extends TagSupport
{
    /*
     * Attribute
     */
    private String caption; // Mandatory
    private boolean stared = false;
    
    /*
     * Setters
     */
    public void setCaption(String caption) {this.caption = caption;}
    public void setStared(boolean stared) {this.stared = stared;}

    public ROECaseTag()
    {
        super();
    }

    /**
     * Dessine le début de la case
     */
    public int doStartTag() throws JspException
    {
        try
        {
            JspWriter out = this.pageContext.getOut();

            String star = this.stared ? "<br><img src=\"img/star.gif\" border=\"0\">" : "";
            out.println("      <table height=\"100%\" width=\"100%\" cellspacing=\"0\" cellpadding=\"1\" style=\"border: 1px solid #000000\">");
            out.println("         <tr>");
            out.println("            <td class=\"tdlabel\" rowspan=\"2\" valign=\"top\" align=\"center\"><img src=\"img/" + this.id + ".gif\" border=\"0\">" + star + "</td>");
            out.println("            <td class=\"tdlabel\" width=\"100%\">" + this.caption + "</td>");
            out.println("         </tr>");
            out.println("         <tr>");
            out.println("            <td class=\"tdfield\">");
            
            this.stared = false;
            return EVAL_BODY_INCLUDE;
        }
        catch (IOException e)
        {
            throw new JspException(e);
        }
    }
    
    /**
     * Dessine la fin de la case
     */
    public int doEndTag() throws JspException
    {
        try
        {
            JspWriter out = this.pageContext.getOut();

            out.println("            </td>");
            out.println("         </tr>");
            out.println("      </table>");
            
            return EVAL_PAGE;
        }
        catch (IOException e)
        {
            throw new JspException(e);
        }
    }
}
