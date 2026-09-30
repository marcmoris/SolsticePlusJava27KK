///*
// * Created on 4 juil. 2005
// */
//package solstice.custom;
//
//import java.io.BufferedReader;
//import java.io.FileInputStream;
//import java.io.InputStreamReader;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.Stack;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import javax.servlet.jsp.JspTagException;
//import javax.servlet.jsp.JspWriter;
//import javax.servlet.jsp.tagext.BodyTagSupport;
//import javax.xml.parsers.SAXParser;
//import javax.xml.parsers.SAXParserFactory;
//
//import org.compiere.util.DB;
//import org.xml.sax.Attributes;
//import org.xml.sax.helpers.DefaultHandler;
//
///**
// * @author frafor01
// *
// * Template de page pour la SIQ
// */
//public class PageTag extends BodyTagSupport
//{
//    /**
//     * 
//     * @author frafor01
//     *
//     * Cette classe sert à la transformation du fichier menu.xml contenant
//     * la hiérarche des menus avec leur url
//     */
//    private class MyXMLHandler extends DefaultHandler
//    {
//        /**
//         * 
//         * @author frafor01
//         *
//         * Les objets de cette classe seront empilé pour mémoriser
//         * les items du menu
//         */
//        private class MenuItem
//        {
//            public String name;
//            public String href;
//            
//            public MenuItem(String name, String href)
//            {
//                this.name = name;
//                this.href = href;
//            }
//        }
//        
//        private int roleId;
//        private String id;
//        private String pathName = null;
//        private String pageTitle = null;
//        private String htmlPath = null;
//        private String htmlMenu = null;
//        private boolean found = false;
//        private int subMenu = -1;
//        private String appName = "";
//        private Stack path = null;
//        private Stack subItem = null;
//        
//        /**
//         * 
//         * @return le menu de la page
//         */
//        public String getHtmlMenu()
//        {
//            return this.htmlMenu;
//        }
//        
//        /**
//         * 
//         * @return le nom de l'application web
//         */
//        public String getAppName()
//        {
//            return this.appName;
//        }
//        
//        /**
//         * 
//         * @return la séquence de menu menant à la page courrante
//         */
//        public String getHtmlPath()
//        {
//            return this.htmlPath;
//        }
//        
//        /**
//         * 
//         * @return le titre de la page
//         */
//        public String getPageTitle()
//        {
//            return this.pageTitle;
//        }
//        
//        /**
//         * Constructeur
//         * @param id le id de la page dans le fichier menu.xml
//         * @param roleId sert au check de sécurité
//         */
//        public MyXMLHandler(int roleId, String id)
//        {
//            this.roleId = roleId;
//            this.id = id;
//        }
//        
//        /**
//         * On crée les deux piles de traitement
//         */
//        public void startDocument()
//        {
//            this.path = new Stack ();
//            this.subItem = new Stack();
//        }
//        
//        /**
//         * On vide la mémoire
//         */
//        public void endDocument()
//        {
//            convertStack();
//            this.path.clear();
//            this.path = null;
//            this.subItem.clear();
//            this.subItem = null;
//        }
//        
//        /**
//         * Cette méthode est lancée lorsqu'on rencontre une balise ouvrante
//         * dans le fichier XML. Dans ce cas-ci, on cherche à empiler, dans
//         * l'objet path, la séquence d'éléments rencontrés jusqu'à l'item
//         * recherché. Une fois que celui-ci est trouvé, on empile dans l'objet
//         * subItem les éléments de sont sous-menu.
//         */
//        public void startElement(String namespaceUri,
//                String localName,
//                String qualifiedName,
//                Attributes attributes)
//        {
//            
//            if(qualifiedName.equals("webApp"))
//            {
//                this.appName = attributes.getValue("name").replaceAll("#", "&#");
//                this.pathName = attributes.getValue("pathName");
//            }
//            else if(qualifiedName.equals("menu") && !this.found)
//            {
//                String id = attributes.getValue("id");
//                this.path.push(new MenuItem(attributes.getValue("name").replaceAll("#", "&#"), attributes.getValue("href")));
//                if(id.equals(this.id))
//                {
//                    this.found = true;
//                    this.subMenu++;
//                    this.pageTitle = attributes.getValue("name").replaceAll("#", "&#");
//                }
//            }
//            else if(qualifiedName.equals("menu") && this.found && this.subMenu >= 0)
//            {
//                this.subMenu ++;
//                if(this.subMenu == 1)
//                {
//                    // On doit vérifier si l'utilisateur a accès à la page
//                    if(this.checkAccess(attributes.getValue("href")))
//                        this.subItem.push(new MenuItem(attributes.getValue("name").replaceAll("#", "&#"), attributes.getValue("href")));
//                }
//            }
//        }
//        
//        /**
//         * Lorsqu'on rencontre une balise fermant, on retire de la pile
//         * les éléments ne conduisant pas directement à la page recherchée
//         */
//        public void endElement(String namespaceUri,
//                String localName,
//                String qualifiedName)
//        {
//            try
//            {
//                if(!found)
//                {
//                    this.path.pop();
//                }
//                else
//                {
//                    this.subMenu--;
//                }
//            }
//            catch (Exception e)
//            {
//            }
//        }
//        
//        /**
//         * On doit vérifier que le rôle de l'utilisateur courrant a accès à la page. Dans
//         * le cas contraire, on ne veut pas afficher la page dans le menu
//         */
//        private boolean checkAccess(String page)
//        {
//            String sql 
//            = "select 1"
//                + " from AD_Form inner join AD_Form_Access"
//                + " on AD_Form.AD_Form_ID = AD_Form_Access.AD_Form_ID"
//                + " where AD_Form.JSPURL like '" + this.pathName + "/" + page + "'"
//                + " and AD_Form_Access.AD_Role_ID = " + this.roleId
//                + " and AD_Form_Access.IsActive = 'Y'";
//            try
//            {
//                PreparedStatement stmt = DB.prepareStatement(sql, null);
//                ResultSet rs = stmt.executeQuery();
//                
//                boolean access = rs.next();
//                rs.close();
//                stmt.close();
//                return access;
//            }
//            catch (SQLException e)
//            {
//                e.printStackTrace(System.out);
//                return false;
//            }
//        }
//        
//        /**
//         * On convertit le contenue des deux piles en html.
//         *
//         */
//        private void convertStack()
//        {
//            this.htmlPath = "";
//            while(!this.path.isEmpty())
//            {
//                MenuItem item = (MenuItem)this.path.pop();
//                if(!this.htmlPath.equals("")) this.htmlPath = " &middot;&gt; " + this.htmlPath;
//                this.htmlPath = "<a class=\"path\" href=\"" + item.href + "\">" + item.name + "</a>" + this.htmlPath;
//            }
//            
//            this.htmlMenu = "</ul>";
//            
//            while(!this.subItem.isEmpty())
//            {
//                MenuItem item = (MenuItem)this.subItem.pop();
//                this.htmlMenu = "<li><a class=\"menu\" href=\"" + item.href + "\">" + item.name + "</a>" + this.htmlMenu + "</li>\n";
//            }
//            
//            this.htmlMenu = "<ul>" + this.htmlMenu;
//        }
//    }
//    
//    private String onLoad;
//    private String ref = "";
//    private String pageTitle = "";
//    private String htmlPath = "";
//    private String htmlMenu = "";
//    private String appName = "";
//    private boolean supportCalendar = false;
//    private String validationClass = null;
//
//    public void setOnLoad(String onLoad) {this.onLoad = onLoad;}
//    
//    public void setValidationClass (String validationClass)
//    {
//        this.validationClass = validationClass;
//    }
//    
//    public void setSupportCalendar(boolean supportCalendar)
//    {
//        this.supportCalendar = supportCalendar ;
//    }
//    
//    /**
//     * Constructeur
//     *
//     */
//    public PageTag()
//    {
//        super();
//    }
//    
//    /**
//     * 
//     * @param ref le path absolue du dossier WEB-INF contenant menu.xml et le
//     * dossier de template
//     */
//    public void setRef(String ref)
//    {
//        this.ref = ref;
//    }
//    
//    /**
//     * On lit le fichier menu.xml
//     * @throws JspTagException
//     */
//    private void initMenu() throws JspTagException
//    {
//        try
//        {
//            MyXMLHandler handler = new MyXMLHandler(((Integer)this.pageContext.getSession().getAttribute("roleId")).intValue(), this.getId());
//            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
//            parser.parse(this.ref + "\\menu.xml", handler);
//            this.appName = handler.getAppName();
//            this.htmlPath = handler.getHtmlPath();
//            this.pageTitle = handler.getPageTitle();
//            this.htmlMenu = handler.getHtmlMenu();
//        }
//        catch (Exception e)
//        {
//            throw new JspTagException ("initMenu : " + e.toString());
//        }
//    }
//    
//    private String createErrorList(String[] erreurs)
//    {
//        if(erreurs != null)
//        {
//            String htmlErreur = "<form name=\"errorForm\" method=\"post\" action=\"error.jsp\" target=\"error\">";
//            for(int i = 0; i < erreurs.length; i++)
//            {
//                htmlErreur += "  <input type=\"hidden\" name=\"erreurs\" value=\"" + erreurs[i] + "\">\n";
//            }
//            return htmlErreur + "</form>" ;
//        }
//        return "";
//    }
//    
//    /**
//     * On utilise le fichier template\pageTag_start.html pour construire
//     * le html du début de la page. On doit remplacer différents paramètres
//     * de template par les valeurs trouvées dans le fichier menu.xml
//     */
//    public int doStartTag() throws JspTagException
//    {
//        
//        JspWriter out = pageContext.getOut();
//        String [] erreurs = null;
//        if(this.validationClass != null && !this.validationClass.equals(""))
//        {
//            try
//            {
//                ValidationBase validation = (ValidationBase) Class.forName(this.validationClass).newInstance();
//                validation.init(pageContext);
//                erreurs = validation.validate();
//                if(erreurs == null && validation.getValidated())
//                {
//                    boolean validated = validation.afterValidation(out);
//                    if(!validated)
//                    {
//                        return SKIP_PAGE;
//                    }
//                }
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace(System.out);
//            }
//        }
//        
//        this.initMenu();
//        
//        try
//        {
//            FileInputStream file = new FileInputStream(this.ref + "\\template\\pageTag_start.html");
//            BufferedReader input = new BufferedReader(new InputStreamReader(file));
//            String html = null;
//            while((html = input.readLine()) != null)
//            {
//                if(html.indexOf("%%INCLUDESCRIPT%%") >= 0)
//                {
//                    String autoComplete = "<script type=\"text/javascript\" src=\"scripts/comboBoxAutoComplete.js\"></script>\n";
//                    String cal
//                        = "<!-- CALENDRIER -->\n"
//                            + "  <script language=\"JavaScript\" src=\"scripts/calendarheader.js\" type=text/javascript></script>\n"
//                            + "  <script language=\"JavaScript\" src=\"scripts/overlib_mini.js\"></script>\n"
//                            + "<!-- FIN CALENDRIER -->\n";
//
//                    String script
//                    = " <script language=\"javascript\">\n"
//                        + "   var width = 900;\n"
//                        + "   var height = 600;\n"
//                        + "   var left = (screen.width - 900) / 2;\n"
//                        + "   var top = (screen.height - 600) / 2;\n"
//                        + "   window.open(\"error.jsp\", \"error\", \"left=\" + left + \",top=\" + top + \",width=\" + width + \",height=\" + height + \",toolbar=0,resizable=0,scrollbars=1\");\n"
//                        + "</script>\n";
//
//                    html = html.replaceAll("%%INCLUDESCRIPT%%", 
//                            autoComplete +
//                            (this.supportCalendar ? cal : "") +
//                            (erreurs != null ? script : ""));
//                    
//                }
//                if(html.indexOf("%%ONLOAD%%") >= 0)
//                {
//                    if(this.onLoad != null)
//                        html = html.replaceAll("%%ONLOAD%%", "onLoad=\"" + this.onLoad + (erreurs != null ? "document.errorForm.submit();" : "") + "\"");
//                    else
//                        html = html.replaceAll("%%ONLOAD%%", erreurs != null ? "onload=\"document.errorForm.submit();\"" : "");
//                }
//                if(html.indexOf("%%CALENDAR%%") >= 0)
//                {
//                    String htmlErreur = createErrorList(erreurs);
//                    
//                    html = html.replaceAll("%%CALENDAR%%", htmlErreur + (this.supportCalendar ? "<div id=\"overDiv\" style=\"Z-INDEX:1000; VISIBILITY:hidden; POSITION:absolute\"></div>" : ""));
//                }
//                if(html.indexOf("%%TITLE%%") >= 0)
//                {
//                    html = html.replaceAll("%%TITLE%%", "SIQ - " + this.pageTitle);
//                }
//                if(html.indexOf("%%APPNAME%%") >= 0)
//                {
//                    html = html.replaceAll("%%APPNAME%%", this.appName);
//                }
//                if(html.indexOf("%%HTMLPATH%%") >= 0)
//                {
//                    html = html.replaceAll("%%HTMLPATH%%", this.htmlPath);
//                }
//                if(html.indexOf("%%HTMLMENU%%") >= 0)
//                {
//                    html = html.replaceAll("%%HTMLMENU%%", this.htmlMenu);
//                }
//                if(html.indexOf("%%PAGETITLE%%") >= 0)
//                {
//                    html = html.replaceAll("%%PAGETITLE%%", this.pageTitle);
//                }
//                out.println(html);
//            }
//            input.close();
//            file.close();
//        }
//        catch (Exception e)
//        {
//            throw new JspTagException ("doStartTag : " + e.toString());
//        }
//        return EVAL_BODY_INCLUDE;
//    }
//    
//    /**
//     * On crée le html à partir du fichier template\pageTag_end.html
//     */
//    public int doEndTag() throws JspTagException
//    {
//        JspWriter out = pageContext.getOut();
//        
//        try
//        {
//            FileInputStream file = new FileInputStream(this.ref + "\\template\\pageTag_end.html");
//            BufferedReader input = new BufferedReader(new InputStreamReader(file));
//            String html = null;
//            while((html = input.readLine()) != null)
//            {
//                out.println(html);
//            }
//            input.close();
//            file.close();
//        }
//        catch (Exception e)
//        {
//            throw new JspTagException ("doEndTag : " + e.toString());
//        }
//        return EVAL_PAGE;
//    }
//}
