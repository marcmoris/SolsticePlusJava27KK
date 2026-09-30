package solstice.custom;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author Francis
 * Un boutton de suppression pour les structures manipulables
 */
public class ManipulableDeleteButtonTag extends TagSupport
{
    public static final long serialVersionUID = 0;
    
    /*
     * Attributes
     */
    private String type; // Mandatory
    private String src = null;
    
    /*
     * Setters
     */
    public void setType(String type) {this.type = type;}
    public void setSrc(String src) {this.src = src;}

    public ManipulableDeleteButtonTag()
    {
        super();
    }

    /**
     * On dessine le boutton
     */
    public int doEndTag() throws JspException
    {
        ManipulableStructureTag manipulableStructure = (ManipulableStructureTag)findAncestorWithClass(this, ManipulableStructureTag.class);
        if(manipulableStructure != null)
        {
            
            try
            {
                JspWriter out = this.pageContext.getOut();
                
                // Si le type c'est "image", on dessine une un <a href="..."><img src="..."></a>
                if(type.equals("image"))
                {
                    out.println("<a href=\"javascript:" + manipulableStructure.getId() + "_delete(" + manipulableStructure.getCurrentIndex() + ")\">");
                    out.println("<img src=\"" + this.src + "\" alt=\"Supprimer la ligne\" border=\"0\">");
                    out.println("</a>");
                }
                else
                {
                    out.println("<input type=\"button\" value=\"Supprimer\" onClick=\"" + manipulableStructure.getId() + "_delete(" + manipulableStructure.getCurrentIndex() + ")\">");
                }
            }
            catch (IOException e)
            {
                throw new JspException(e);
            }
            
            return EVAL_PAGE;
        }
        throw new JspException("Un ManipulableDeleteButtonTag doit être placé sous un ManipulableStructureTag");
    }
}
