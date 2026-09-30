
/******************************************************************************
 * Product: Solstice+ Payroll & Human Resources Management                    *
 * Copyright (C) 2008 ProGestion Informatique, Inc. All Rights Reserved.      *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ProGestion Informatique, 210-5300 Bld des Galerie, Quebec, G2K 2A2 Canada  *
 * or via info@progestion.net or http://www.progestion.net/license.html       *
 ******************************************************************************/
package solstice.custom;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.xml.parsers.DocumentBuilderFactory;

import org.compiere.util.Env;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import solstice.utils.PgiUtil;

/**
 * Cette classe définit un tag qui crée une page html selon une arborescence
 * de menus contenus dans le fichier /WEB-INF/menu.xml. Les liens sont affiché
 * seulement si la sécurité dans le (IUserInfo)session.get("userInfo") le permet.
 * Cette dernière condition est validée grâce au id du tag
 * 
 * @author frafor01
 */
public class PageTag extends BodyTagSupport
{    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
     * Attributes
     */
    private boolean supportCalendar = false;
    private String validationClass = null;
    private String onLoad = null;
    private Element menuNode = null;
    private boolean displayNavigation = true;
    
    /*
     * Members
     */
    private String[] errors = null;
    
    /*
     * Setters
     */
    public void setSupportCalendar(boolean supportCalendar) {this.supportCalendar = supportCalendar;}
    public void setValidationClass(String validationClass) {this.validationClass = validationClass;}
    public void setOnLoad(String onLoad) {this.onLoad = onLoad;}
    public void setDisplayNavigation(boolean displayNavigation) {this.displayNavigation = displayNavigation;}
    
    public PageTag()
    {
    }
    
    /**
     * Traitement du tag d'ouverture. On doit afficher le début de la 
     * page et effectuer les validations.
     */
    public int doStartTag() throws JspException
    {
	int retValue = 0;
        try
        {
            // On écrit d'abord le début de la page selon le fichier de ressources. Pour
            // ce faire, on doit d'abord charger le fichier XML pour l'arborescence des
            // menus.
        	this.initializeMenuNode();
            ServletContext servletContext = this.pageContext.getServletContext();
//            String path = servletContext.getRealPath("/WEB-INF/menu.xml");
            
            JspWriter out = this.pageContext.getOut();

            String client = PgiUtil.getSolsticeParameter(Env.getCtx(),"Client").toUpperCase();
            String fileName = servletContext.getRealPath("/WEB-INF/template/pageTag_start_" + client + ".html"); 
            File fileTemplate = new File(fileName);;
            FileInputStream file;
            if (fileTemplate.exists())
            	file = new FileInputStream( fileName );
            else
            	file = new FileInputStream(servletContext.getRealPath("/WEB-INF/template/pageTag_start.html"));

            BufferedReader input = new BufferedReader(new InputStreamReader(file));

	
        if(this.validatePage())
        {
        	retValue = EVAL_BODY_INCLUDE;
        }
        // Les validations ne permettent pas d'afficher le reste de la page
        // normalement, on va donc skipper l'évaluation
        else
        {
            retValue = SKIP_BODY;
        }


            String html = null;
            while((html = input.readLine()) != null)
            {
            	if(html.indexOf("%%HEADING%%") >= 0 )
            	{
            		html = addHeader( html );
            	}

                if(html.indexOf("%%INCLUDESCRIPT%%") >= 0)
                {
                    String autoComplete = "<script type=\"text/javascript\" src=\"scripts/comboBoxAutoComplete.js\"></script>\n";
                    String validationTool = "<script language=\"JavaScript\" src=\"scripts/ValidationTools.js\" type=text/javascript></script>\n";
                    String cal
                        = "<!-- CALENDRIER -->\n"
                            + "  <script language=\"JavaScript\" src=\"scripts/calendarheader.js\" type=text/javascript></script>\n"
                            + "  <script language=\"JavaScript\" src=\"scripts/overlib_mini.js\"></script>\n"
                            + "<!-- FIN CALENDRIER -->\n";

                    String script
                    = " <script language=\"javascript\">\n"
                        + "   var width = 900;\n"
                        + "   var height = 600;\n"
                        + "   var left = (screen.width - 900) / 2;\n"
                        + "   var top = (screen.height - 600) / 2;\n"
                        + "   window.open(\"about:blank\", \"error\", \"left=\" + left + \",top=\" + top + \",width=\" + width + \",height=\" + height + \",toolbar=0,resizable=0,scrollbars=1\");\n"
                        + "</script>\n";

                    html = html.replaceAll("%%INCLUDESCRIPT%%", 
                            autoComplete + validationTool +
                            (this.supportCalendar ? cal : "") +
                            (this.errors != null ? script : ""));
                    
                }
                if(html.indexOf("%%ONLOAD%%") >= 0)
                {
                    if(this.onLoad != null)
                        html = html.replaceAll("%%ONLOAD%%", "onLoad=\"" + this.onLoad + (this.errors != null ? "document.errorForm.submit();" : "") + "\"");
                    else
                        html = html.replaceAll("%%ONLOAD%%", this.errors != null ? "onload=\"document.errorForm.submit();\"" : "");
                }
                if(html.indexOf("%%CALENDAR%%") >= 0)
                {
                    String htmlErreur = this.createErrorList();
                    
                    html = html.replaceAll("%%CALENDAR%%", htmlErreur + (this.supportCalendar ? "<div id=\"overDiv\" style=\"Z-INDEX:1000; VISIBILITY:hidden; POSITION:absolute\"></div>" : ""));
                }
                if(html.indexOf("%%TITLE%%") >= 0)
                {
                    html = html.replaceAll("%%TITLE%%",  this.getPageTitle());
                }
                if(html.indexOf("%%APPNAME%%") >= 0)
                {
                    html = html.replaceAll("%%APPNAME%%", this.getWebAppName());
                }
                if(html.indexOf("%%HTMLPATH%%") >= 0)
                {
                    html = html.replaceAll("%%HTMLPATH%%", this.displayNavigation ? "<a href=\"/solstice/index.jsp\" class=\"path\">Menu principal</a> &middot;&gt;" + this.getHtmlPath() : "");
                }
                if(html.indexOf("%%HTMLMENU%%") >= 0)
                {
                    html = html.replaceAll("%%HTMLMENU%%", this.getHtmlMenu());
                }
                if(html.indexOf("%%PAGETITLE%%") >= 0)
                {
                    html = html.replaceAll("%%PAGETITLE%%", this.getPageTitle());
                }

                out.println(html);
            }
            input.close();
            file.close();
        }
        catch (Exception e)
        {
        	e.printStackTrace(System.out);
            throw new JspException(e);
        }
        
        // On doit ensuite lancer les validations. Si celles-ci le permettent, on 
        // affichera ensuite le début de la page
        return retValue;
    }
    
	public String addHeader(String fileText)
	{
		String head = "<head>";
		
        String client = PgiUtil.getSolsticeParameter(Env.getCtx(),"Client").toUpperCase();
        if ( client.equals("SIQ") )
        {
        	head = 	"<head>"
        		+ "<meta name=\"vs_targetSchema\" content=\"http://schemas.microsoft.com/intellisense/ie5\">"
        		+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"styles/siq.css\">"
        		+ "<title>Solstice</title> "
        		
/*        		
        		+ "%%INCLUDESCRIPT%%"  
        		+ "</head>"                       
        		+ "<body %%ONLOAD%%>"             
        		+ "%%CALENDAR%% "                 
        		+ "<table width=\"900\" align=\"center\" border=\"0\" cellspacing=\"2\" cellpadding=\"0\">"
        		+ "<tr> "                         
        		+ "<td align=\"center\" valign=\"middle\">"
        		+ "<img src=\"images/SIQwBAN.GIF\" width=\"211\" height=\"100\" border=\"0\"> "
        		+ "</td>"                         
        		+ "<td align=\"center\" valign=\"middle\">"
        		+ "<img src=\"images/SIQBAN.GIF\" width=\"450\" height=\"57\" border=\"0\"> "
        		+ "</td>"                         
        		+ "</tr>"                         
        		+ "<tr> "                         
        		+ "<td colspan=\"2\" align=\"center\" valign=\"middle\">"
        		+ "<div class=\"apptitle\">%%APPNAME%%</div>"
        		+ "</td>"                         
        		+ "</tr>"                         
        		+ "<tr> "                         
        		+ "<td colspan=\"2\" class=\"line\"></td> "
        		+ "</tr>"                         
        		+ "<tr> "                         
        		+ "<td colspan=\"2\"> "           
        		+ " "                             
        		+ "%%HTMLPATH%% "                 
        		+ "</td>"                         
        		+ "</tr>"                         
        		+ "<tr> "                         
        		+ "<td colspan=\"2\"> "           
        		+ "<div class=\"pagetitle\">%%PAGETITLE%%</div> "
        		+ "</td>"                         
        		+ "</tr>"                         
        		+ "<tr> "                         
        		+ "<td colspan=\"2\"> "           
        		+ " %%HTMLMENU%%"                 
        		+ "</td>"                         
        		+ "</tr>"                         
        		+ "<tr> "                         
        		+ "<td colspan=\"2\"> "           
*/        		;       
        		           
        		           


        }
        if ( client.equals("SOQUIJ") )
        {
			head = "<head> \r\n"
				+ " 	<meta name=\"vs_targetSchema\" content=\"http://schemas.microsoft.com/intellisense/ie5\"> \r\n"
				+ "   <link rel=\"stylesheet\" type=\"text/css\" media=\"all\" href=\"../solstice.css\"> \r\n"
				+ " 		<link rel=\"stylesheet\" type=\"text/css\" href=\"styles/siq.css\"> \r\n"
				+ " 		<title>Menu principal</title> \r\n"
				+ "<link rel=\"stylesheet\" type=\"text/css\" media=\"all\" href=\"https://soquij.qc.ca/css/principal.css?v=3\" />     \r\n"
				+ "<link rel=\"stylesheet\" type=\"text/css\" media=\"all\" href=\"https://soquij.qc.ca/css/thickbox.css\" />          \r\n"
				+ "<link rel=\"stylesheet\" type=\"text/css\" media=\"all\" href=\"https://soquij.qc.ca/css/corpo.css\" />             \r\n"
                + "                                                                                                                   \r\n"
				+ "<!--[if IE]>                                                                                                       \r\n"
				+ "	<link rel=\"stylesheet\" type=\"text/css\" media=\"screen\" href=\"https://soquij.qc.ca/css/principal_ie.css\" />  \r\n"
				+ "<![endif]-->                                                                                                      \r\n "
				+ "                                                                                                                   \r\n"
				+ "<!--[if IE 6]>                                                                                                     \r\n"
				+ "	<script type=\"text/javascript\" src=\"/js/iepngfix_tilebg.js\" media=\"screen\"></script>                        \r\n"
				+ "	<link rel=\"stylesheet\" type=\"text/css\" media=\"screen\" href=\"https://soquij.qc.ca/css/principal_ie6.css\" /> \r\n"
				+ "<![endif]-->                                                                                                       \r\n"
                + "                                                                                                                   \r\n"
				+ "<!--[if IE 8]>                                                                                                     \r\n"
				+ "	<style type=\"text/css\">                                                                                         \r\n"
				+ "		#sous_navig_catalogue ul ul li {                                                                              \r\n"
				+ "			display:inline-block;                                                                                     \r\n"
				+ "		}                                                                                                             \r\n"
				+ "						</style>                                                                                      \r\n"          
				+ "	<![endif]-->                                                                                                      \r\n"

				+ "<link rel=\"stylesheet\" type=\"text/css\" media=\"print\" href=\"https://soquij.qc.ca/css/print.css\" />			  \r\n"
				+ "<script type=\"text/javascript\">\r\n"
				+ "		var section_id = 1;\r\n"
				+ "		var langue = 'fr';\r\n"
				+ "		var no_focus_on_load = false;\r\n"
				+ "	</script>\r\n"
				+ "	<style>\r\n"
				+ "		/*li {line-height: 1.2}\r\n"
				+ "		label {display:inline;font-size: 12px;}\r\n"
				+ "		#login_azimut_rapide input[type=text] {\r\n"
				+ "			padding-top: 0px;\r\n"
				+ "			padding-bottom: 0px;\r\n"
				+ "		}\r\n"
				+ "		.contenu_padding .bloc_deroulant {\r\n"
				+ "			margin: 0;\r\n"
				+ "		}	*/		\r\n"
				+ "	</style>\r\n"
				+ "\r\n"
				+ "		<script src=\"//ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js\"></script>\r\n"
				+ "		<script type=\"text/javascript\" src=\"/js/bootstrap.min.js\"></script>\r\n"
				+ "	<script type=\"text/javascript\" src=\"/js/jquery-cookie.js\"></script>\r\n"
				+ "	<script type=\"text/javascript\" src=\"/js/taille_police.js\"></script>\r\n"
				+ "	<script type=\"text/javascript\" src=\"/js/swfobject.js\"></script>\r\n"
				+ "	<script type=\"text/javascript\" src=\"/js/thickbox.js\"></script>\r\n"
				+ "	<script type=\"text/javascript\" src=\"/js/fonctions.js?v=3\"></script>\r\n"
				+ "\r\n"
				+ "	\r\n"
				+ "	<link rel=\"shortcut icon\" href=\"/favicon.ico?v=3\" />\r\n"
				+ "\r\n"
				+ "			<link rel=\"alternate\" type=\"application/rss+xml\" title=\"SOQUIJ - Nouvelles et communiqués\" href=\"/fr/fils-rss/nouvelles-et-communiques.xml\" />\r\n"
				+ "\r\n"
				+ "<script type=\"text/javascript\">\r\n"
				+ "\r\n"
				+ "	var _gaq = _gaq || [];\r\n"
				+ "	_gaq.push(['_setAccount', 'UA-3760448-1']);\r\n"
				+ "	_gaq.push(['_setDomainName', '.soquij.qc.ca']);\r\n"
				+ "			_gaq.push(['_trackPageview']);\r\n"
				+ "	\r\n"
				+ "  (function() {\r\n"
				+ "    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;\r\n"
				+ "    ga.src = ('https:' == document.location.protocol ? 'https://' : 'http://') + 'stats.g.doubleclick.net/dc.js';\r\n"
				+ "    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);\r\n"
				+ "  })();\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "</script>\r\n"
				+ "		<title>Menu principal</title>\r\n"
				+ "	</head>\r\n"
				+ "	<body>\r\n"
				+ "		<div id=\"fond_site\">\r\n"
				+ "			<div id=\"wrapper\">\r\n"
				+ "			<div id=\"utilitaires\">\r\n"
				+ "						\r\n"
				+ "			<ul>\r\n"
				+ "					\r\n"
				+ "					<li style=\"display:inline;\"><a href=\"https://soquij.qc.ca/fr/nous-joindre\" >Nous joindre</a>&nbsp;<strong>|</strong>&nbsp;</li>\r\n"
				+ "					<li style=\"display:inline;\"><a href=\"https://soquij.qc.ca/fr/english\" >English</a>&nbsp;<strong>|</strong>&nbsp;</li>\r\n"
				+ "				</ul>\r\n"
				+ "	\r\n"
				+ "					\r\n"
				+ "					<span id=\"taille_police\">\r\n"
				+ "						<a href=\"#\" id=\"augmenter\"><img src=\"https://soquij.qc.ca/images/ul/graphiques/police_plus.gif\" width=\"9\" height=\"9\" alt=\"Agrandir la police\" title=\"Agrandir la police\" /></a>&nbsp; A &nbsp;<a href=\"#\" id=\"diminuer\"><img src=\"https://soquij.qc.ca/images/ul/graphiques/police_moins.gif\" width=\"9\" height=\"9\" alt=\"Diminuer la police\" title=\"Diminuer la police\" /></a>\r\n"
				+ "					</span>\r\n"
				+ "					\r\n"
				+ "					<form method=\"get\" class=\"form_recherche\" action=\"https://soquij.qc.ca/fr/recherche\">						<input type=\"text\" id=\"mots_cles\" class=\"mots_cles\" value=\"Recherche\" />\r\n"
				+ "						<input type=\"image\" class=\"btn_recherche_rapide\" src=\"https://soquij.qc.ca/images/soquij2013/fleche_recherche_top.gif\" />\r\n"
				+ "					</form>\r\n"
				+ "				</div>\r\n"
				+ "				\r\n"
				+ "				<div id=\"entete\">\r\n"
				+ "		<div id=\"slogan\"><img src=\"https://soquij.qc.ca/images/fr/titrages/slogan.gif\" width=\"93\" height=\"31\" alt=\"Complice de vos succès\" /></div>\r\n"
				+ "<h1><a href=\"https://soquij.qc.ca/fr\"><img src=\"https://soquij.qc.ca/images/soquij2013/logo2013.png\" width=\"352\" height=\"54\" alt=\"Société québécoise d'information juridique\" title=\"Société québécoise d'information juridique\" /></a></h1>											\r\n"
				+ "<ul id=\"navig_corpo\">\r\n"
				+ "			<li style=\"display:inline;\"><a href=\"https://soquij.qc.ca/fr\">{CIE_NAME}</a></li>\r\n"
				+ "	</ul>\r\n"
				+ "	<!-- Médias sociaux -->\r\n"
				+ "	<div id=\"medias_sociaux\">\r\n"
				+ "		<a href=\"http://facebook.com/soquij\" name=\"Facebook\" target=\"_blank\"><img src=\"https://soquij.qc.ca/images/ul/icones/facebook.png\" width=\"16\" height=\"16\" alt=\"Facebook. S'ouvrira dans une nouvelle fenêtre.\" title=\"Facebook\" /></a>\r\n"
				+ "		<a href=\"http://twitter.com/soquij\" name=\"Twitter\" target=\"_blank\"><img src=\"https://soquij.qc.ca/images/ul/icones/twitter.png\" width=\"16\" height=\"16\" alt=\"Twitter. S'ouvrira dans une nouvelle fenêtre.\" title=\"Twitter\" /></a>\r\n"
				+ "		<a href=\"http://www.linkedin.com/company/soquij\" name=\"LinkedIn\" target=\"_blank\"><img src=\"https://soquij.qc.ca/images/ul/icones/linkedin.png\" width=\"16\" height=\"16\" alt=\"LinkedIn. S'ouvrira dans une nouvelle fenêtre.\" title=\"LinkedIn\" /></a>\r\n"
				+ "	</div>\r\n"
				+ "	<script type=\"text/javascript\">\r\n"
				+ "		$('#medias_sociaux a').click(function(){\r\n"
				+ "			if (_gaq) {\r\n"
				+ "				_gaq.push(['_trackEvent', 'Social', document.title.split(' - ')[0], $(this).attr('name')]);\r\n"
				+ "			}\r\n"
				+ "		});\r\n"
				+ "	</script>\r\n"
				+ "\r\n"
				+ "</div>\r\n"
				+ "<div id=\"colonne_principale\">\r\n"
				+ "<div id=\"conteneur_colonnes\">\r\n"
				;
        }
       		
			
		return fileText.replace("%%HEADING%%", head);
	}

    /**
     * Traitement du tag de fermeture. On doit afficher la fin de la page
     */
    public int doEndTag() throws JspException
    {
        JspWriter out = pageContext.getOut();
        
        try
        {
            ServletContext servletContext = this.pageContext.getServletContext();
            FileInputStream file = new FileInputStream(servletContext.getRealPath("/WEB-INF/template/pageTag_end.html"));
            BufferedReader input = new BufferedReader(new InputStreamReader(file));
            String html = null;
            while((html = input.readLine()) != null)
            {
                if(html.indexOf("%%Organisation%%") >= 0)
                {
                    html = html.replaceAll("%%Organisation%%", "Solstice");
                }

            	out.println(html);
            }
            input.close();
            file.close();
            return EVAL_PAGE;
        }
        catch (Exception e)
        {
            throw new JspException (e);
        }
    }
    
    /**
     * Réinitialisation de l'état du tag
     */
    public void release()
    {
        this.supportCalendar = false;
        this.validationClass = null;
        this.errors = null;
        this.onLoad = null;
        this.displayNavigation = true;
    }
    
    /**
     * Cette méthode effectue les validations grâce à la classe de validation
     * et retourne un boolean indiquant si on doit évaluer le contenue de la
     * page ou non.
     */
    private boolean validatePage() throws JspException
    {
        // Si on a définit une classe de validation
        if(this.validationClass != null && !this.validationClass.equals(""))
        {
            try
            {
                // On tente d'abord d'instancier la classe qui doit être de type ValidationBase
                ValidationBase validationInstance = (ValidationBase)Class.forName(this.validationClass).newInstance();
                
                // On effectue maintenant les validations
                validationInstance.init(this.pageContext);
                this.errors = validationInstance.validate();
                
                // Si les validations on passée sans erreur, on vérifie si on doit évaluer
                // le reste de la page. 
                if(this.errors == null && validationInstance.getValidated())
                {
                    return validationInstance.afterValidation(this.pageContext.getOut());
                }
            }
            catch (Exception e)
            {
		e.printStackTrace();
                throw new JspException(e);
            }
        }
        
        // On évalue le reste de la page
        return true;
    }

    /**
     * Cette méthode crée la liste d'erreurs
     */
    private String createErrorList()
    {
        if(this.errors != null)
        {
            String htmlErreur = "<form name=\"errorForm\" method=\"post\" action=\"error.jsp\" target=\"error\">";
            for(int i = 0; i < this.errors.length; i++)
            {
                htmlErreur += "  <input type=\"hidden\" name=\"erreurs\" value=\"" + this.errors[i] + "\">\n";
            }
            return htmlErreur + "</form>" ;
        }
        return "";
    }
    
    /**
     * Permet d'initialiser le noeud de menu pour la page
     * @throws Exception
     */
    private void initializeMenuNode () throws Exception
    {
    	try
    	{
    		
        ServletContext servletContext = this.pageContext.getServletContext();

//	  	System.out.println( "Debug " + servletContext.getRealPath("/WEB-INF/menu.xml") );

        Document d = DocumentBuilderFactory.newInstance()
    	.newDocumentBuilder()
    	.parse(servletContext.getRealPath("/WEB-INF/menu.xml"));

//	  	System.out.println( "Debug " + d.getDocumentElement() );

    	this.menuNode = this.findMenuNode(d.getDocumentElement(),this.id);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    /**
     * Permet de retrouver un noeud avec un id unique dans à partir d'un noeud XML
     * @param node Le noeud
     * @param id l'identifiant unique
     * @return le noeud à utiliser
     */
    private Element findMenuNode (Element node, String id)
    {
    	Element returnNode = null;
//	  	System.out.println("Debug id = \"" + node.getAttribute("id") + "\" == \"" + id + "\"");
//	  	System.out.println("node.getTagName() = \"" + node.getTagName() + "\"");
	  	
    	//Vérifier si c'est bien un noeud de type menu
    	if (node.getTagName().equals("menu"))
    	{
	    	if (id.equals(node.getAttribute("id")))
	    	{
	    		return node;
	    	}
    	}
    	//Si on a pas trouvé le noeud correspondant à la recherche,
    	//on continue la recherche plus creux dans les enfants
		boolean found = false;
		Element child;
		for (Node n = node.getFirstChild() ;
		n != null && !found;
		n = n.getNextSibling())
		{
			if ( n == null)
				return null;
			child = (Element)n;
			returnNode = this.findMenuNode(child, id);
			if (returnNode != null)
			{
				found = true;
			}
		}
    	return returnNode;
    }
    
    /**
     * Permet de trouver le noeud webApp en remontant les parents
     * @return le noeud webApp
     */
/*    private Element findWebAppNodeFromMenuNode ()
    {
    	boolean found = false;
    	Element returnNode = null;
    	
    	//Fouiller les parents jusqu'à temps qu'on trouve le webApp correspondant 
    	for (Element parentNode = (Element)this.menuNode.getParentNode();
    	parentNode != null && !found;
    	parentNode = (Element)parentNode.getParentNode())
    	{
    		if (parentNode.getTagName() == "webApp")
    		{
    			found = true;
    			returnNode = parentNode;
    		}
    	}
    	return returnNode;
    	*/
/*
    	String parentName = null;
        Node parentNode = getParentNode();
        // Hack to bypass the references group node, but then this entire method is a hack.
        if(parentNode instanceof ReferencesNode) {
            parentNode = parentNode.getParentNode();
        }
        if(parentNode instanceof NamedBeanNode) {
            DDBinding binding = ((NamedBeanNode) parentNode).getBinding();
            if(binding != null) {
                parentName = binding.isBound() ? binding.getBindingName() : binding.getBeanName();
                if(parentName.length() == 0) {
                    parentName = null;
                }
            }
        }
        return parentName;
*/
//    }
    
    /**
     * Permet d'obtenir le titre de la page
     * @return titre
     */
    private String getPageTitle()
    {
    	if ( this.menuNode == null )
    		return "Not Define";
   		return this.menuNode.getAttribute("name");
    }
    
    /**
     * Permet d'obtenir le nom de l'application web
     * @return nom
     */
    private String getWebAppName()
    {
 /*   	Element webAppNode = this.findWebAppNodeFromMenuNode();
    	return webAppNode.getAttribute("name");
  */
    	return "";
    }
    
    /**
     * Permet d'obtenir le path html par rapport aux autres pages
     * @return path
     */
    private String getHtmlPath()
    {
    	String htmlPath = "";
 /*   	Element webAppNode = this.findWebAppNodeFromMenuNode();
    	
    	for (Element currentNode = this.menuNode;
    	currentNode != webAppNode;
    	currentNode = (Element)currentNode.getParentNode())
    	{
          if(!htmlPath.equals(""))
          {
        	  htmlPath = " &middot;&gt; " + htmlPath;
          }
          htmlPath = "<a class=\"path\" href=\"" +
          currentNode.getAttribute("href") +
          "\">" + currentNode.getAttribute("name") +
          "</a>" + htmlPath;
    	}
*/    	
    	return htmlPath;
    }
    
    /**
     * Permet d'obtenir le menu disponible à partir de cette page
     * Seulement les premiers enfants sont disponibles
     * @return menu
     */
    private String getHtmlMenu()
    {
    	String htmlMenu = "";
    	if ( this.menuNode == null)
    		return "";
    	
    	NodeList childs = this.menuNode.getChildNodes();
    	Element child;
    	IUserInfo userInfo = (IUserInfo)pageContext.getSession().getAttribute("userInfo");
    	
    	for (int i = 0; i < childs.getLength(); i++)
    	{
    		child = (Element)childs.item(i);
    		//Vérifier si l'utilisateur à accès
			if(userInfo.hasPolicy(child.getAttribute("id")))
			{
			    htmlMenu += "<li><a href=\"" + child.getAttribute("href") +
			    "\" class=\"menu\">" + child.getAttribute("name") +
			    "</a></li>";
			}
    	}
    	
    	if (!htmlMenu.equals(""))
    	{
    		htmlMenu = "<ul>" + htmlMenu + "</ul>";
    	}
    	return htmlMenu;
    }
   
}