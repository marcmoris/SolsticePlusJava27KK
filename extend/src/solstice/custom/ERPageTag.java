package solstice.custom;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author Francis
 * Une page qui valide après un post, mais qui n'affiche pas
 * d'entête et ne fait pas la gestion du menu dans le menu.xml
 */
public class ERPageTag extends TagSupport
{
    public static final long serialVersionUID = 0;
    
    /*
     * Attributes
     */
    private String validationClass; // Mandatory
    private String title = "&Eacute;valuation de rendement";
    private String styleSheet = "styles/forms.css";

    /*
     * Setters
     */
    public void setValidationClass(String validationClass) {this.validationClass = validationClass;}
    public void setTitle(String title) {this.title = title;}
    public void setStyleSheet(String styleSheet) {this.styleSheet = styleSheet;}
    
    /**
     * Constructeur
     */
    public ERPageTag()
    {
        super();
    }

    /**
     * Dessine les entêtes html standards et un form nommé "mainForm"
     */
    public int doStartTag() throws JspException
    {
        // Si la page revient d'un post vers elle même, on doit
        // d'abord lancer la classe de validation
        String postBack = ((HttpServletRequest)this.pageContext.getRequest()).getParameter(this.id + "_page");
        boolean retValue = true;
        if(postBack != null
                && postBack.equals("ok"))
        {
        	retValue = this.validatePage();
        }
        if(retValue == false)
        	return SKIP_BODY;
        else
        {
	        try
	        {
	            HttpServletRequest request = (HttpServletRequest)this.pageContext.getRequest();
	            JspWriter out = this.pageContext.getOut();
	            
	            out.println("<html>");
	            out.println("   <head>");
	            out.println("      <title>" + this.title + "</title>");
	            out.println("      <link rel=\"stylesheet\" type=\"text/css\" href=\"" + this.styleSheet + "\">");
	            
	            out.println("      <script language=\"JavaScript\" src=\"scripts/ValidationTools.js\" type=text/javascript></script>");
	            
	            out.println("      <script language=\"javascript\">");
	            out.println("         function scrollListener()");
	            out.println("         {");
	            out.println("            if(document.body.scrollTop != null)");
	            out.println("            {");
	            out.println("               document.mainForm.scroll.value = document.body.scrollTop;");
	            out.println("            }");
	            out.println("            else");
	            out.println("            {");
	            out.println("               document.mainForm.scroll.value = window.pageYOffset;");
	            out.println("            }");
	            out.println("         }");
	            
	            out.println("         function scroll()");
	            out.println("         {");
	            out.println("            if(document.body.scrollTop != null)");
	            out.println("            {");
	            out.println("               document.body.scrollTop = " + (request.getParameter("scroll") == null ? "0" : request.getParameter("scroll")) + ";");
	            out.println("            }");
	            out.println("            else");
	            out.println("            {");
	            out.println("               window.pageYOffset = " + (request.getParameter("scroll") == null ? "0" : request.getParameter("scroll")) + ";");
	            out.println("            }");
	            out.println("            setInterval(\"scrollListener()\", 1);");
	            out.println("         }");
	            out.println("      </script>");
	
	            out.println("      <!-- CALENDRIER -->");
	            out.println("      <script language=\"JavaScript\" src=\"scripts/calendarheader.js\" type=text/javascript></script>");
	            out.println("      <script language=\"JavaScript\" src=\"scripts/overlib_mini.js\"></script>");
	            out.println("      <!-- FIN CALENDRIER -->");
	
	            out.println("   </head>");
	            out.println("   <body onLoad=\"scroll();\">");
	            out.println("      <div id=\"overDiv\" style=\"Z-INDEX:1000; VISIBILITY:hidden; POSITION:absolute\"></div>");
	            out.println("      <form name=\"mainForm\" method=\"post\">");
	            out.println("         <input type=\"hidden\" name=\"" + this.id + "_page\" value=\"ok\">");
	            out.println("         <input type=\"hidden\" name=\"scroll\" value=\"0\">");
	            
	            return EVAL_BODY_INCLUDE;
	        }
	        catch (IOException e)
	        {
	            throw new JspException(e);
	        }
        }
    }
    
    /**
     * Dessine les pieds de pages html standards
     */
    public int doEndTag() throws JspException
    {
        String postBack = ((HttpServletRequest)this.pageContext.getRequest()).getParameter(this.id + "_page");
        boolean retValue = true;
        if(postBack != null
                && postBack.equals("ok"))
        {
        	retValue = false;
        }
        if(retValue == false)
        	return SKIP_PAGE;
        else
        {
	
	        try
	        {
	            JspWriter out = this.pageContext.getOut();
	            
	            out.println("      </form>");
	            out.println("   </body>");
	            out.println("</html>");
	            
	            return EVAL_PAGE;
	        }
	        catch (IOException e)
	        {
	            throw new JspException(e);
	        }
        }
    }
    
    /**
     * Instancie la classe de validation
     */
    private boolean validatePage() throws JspException
    {
        try
        {
            Class validationClass = Class.forName(this.validationClass);
            ValidationBase instance = (ValidationBase) validationClass.newInstance();
            instance.init(this.pageContext);
            return instance.afterValidation(null);
        }
        catch (Exception e)
        {
            throw new JspException(e);
        }
    }
}
