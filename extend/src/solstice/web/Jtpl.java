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
package solstice.web;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.Timestamp;
import java.math.BigDecimal;

import org.compiere.util.Env;

import solstice.utils.PgiUtil;

/**
 * <b>Jtpl: a very simple template engine for Java</b><br>
 * Contact: <a href="mailto:emmanuel.alliel@gmail.com">emmanuel.alliel@gmail.com</a><br>
 * Web: <a href="http://jtpl.sourceforge.net">http://jtpl.sourceforge.net</a><br>
 * 
 * @version 1.2
 * @author Emmanuel ALLIEL
 * 
 * <p>
 * Template syntax:<br>
 * &nbsp;&nbsp;&nbsp;Variables:<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<code>{VARIABLE_NAME}</code><br>
 * &nbsp;&nbsp;&nbsp;Blocks:<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<code>&lt;!-- BEGIN: BlockName --&gt;</code><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<code>&lt;!-- BEGIN: SubBlockName --&gt;</code><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<code>&lt;!-- END: SubBlockName --&gt;</code><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<code>&lt;!-- END: BlockName --&gt;</code><br>
 * <p>
 * License: GPL<br>
*/

public class Jtpl 
{
	private HashMap<String, String> blocks = new HashMap<String, String>();
	private HashMap<String, String> parsedBlocks = new HashMap<String, String>();
	private HashMap<String, String> subBlocks = new HashMap<String, String>();
	private HashMap<String, String> vars = new HashMap<String, String>();
	
	/**
	* Constructs a Jtpl object and reads the template from a file.
	* @param fileName the <code>file name</code> of the template, exemple: "java/folder/index.tpl"
	* @throws IOException when an i/o error occurs while reading the template.
	*/
	public Jtpl(String fileName) throws IOException
	{
		String fileText = readFile(fileName);
		fileText = addHeader(fileText);
		makeTree(fileText);
	}

	public Jtpl(String fileText, boolean file) throws IOException
	{
		fileText = addHeader(fileText);
		makeTree(fileText);
	}

	public String addHeader(String fileText)
	{
		String client = PgiUtil.getSolsticeParameter( Env.getCtx(), "Client").toLowerCase();
		String head;
		String webLogoHeader1 = PgiUtil.getSolsticeParameter(Env.getCtx(), "webLogoHeader1");
		String webLogoHeader2 = PgiUtil.getSolsticeParameter(Env.getCtx(), "webLogoHeader2");

		if ( client.equals( "soquij")  )
			head = "<head> \r\n"
					+ "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\r\n"
					+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\r\n"
					+ "<meta name=\"vs_targetSchema\" content=\"http://schemas.microsoft.com/intellisense/ie5\"> \r\n"
					+ "   <link rel=\"stylesheet\" type=\"text/css\" media=\"all\" href=\"../solstice.css\"> \r\n"
					+ " 		<link rel=\"stylesheet\" type=\"text/css\" href=\"styles/siq.css\"> \r\n"
					+ " 		<title>Menu principal</title> \r\n"
					+ "<link rel=\"stylesheet\" type=\"text/css\" media=\"all\" href=\"/solstice/soquij/css/principal.css?v=3\" />     \r\n"
					+ "<link rel=\"stylesheet\" type=\"text/css\" media=\"all\" href=\"/solstice/soquij/css/thickbox.css\" />          \r\n"
					+ "<link rel=\"stylesheet\" type=\"text/css\" media=\"all\" href=\"/solstice/soquij/css/corpo.css\" />             \r\n"
	                + "                                                                                                                   \r\n"
					+ "<!--[if IE]>                                                                                                       \r\n"
					+ "	<link rel=\"stylesheet\" type=\"text/css\" media=\"screen\" href=\"/solstice/soquij/css/principal_ie.css\" />  \r\n"
					+ "<![endif]-->                                                                                                      \r\n "
					+ "                                                                                                                   \r\n"
					+ "<!--[if IE 6]>                                                                                                     \r\n"
					+ "	<script type=\"text/javascript\" src=\"/js/iepngfix_tilebg.js\" media=\"screen\"></script>                        \r\n"
					+ "	<link rel=\"stylesheet\" type=\"text/css\" media=\"screen\" href=\"/solstice/soquij/css/principal_ie6.css\" /> \r\n"
					+ "<![endif]-->                                                                                                       \r\n"
	                + "                                                                                                                   \r\n"
					+ "<!--[if IE 8]>                                                                                                     \r\n"
					+ "	<style type=\"text/css\">                                                                                         \r\n"
					+ "		#sous_navig_catalogue ul ul li {                                                                              \r\n"
					+ "			display:inline-block;                                                                                     \r\n"
					+ "		}                                                                                                             \r\n"
					+ "						</style>                                                                                      \r\n"          
					+ "	<![endif]-->                                                                                                      \r\n"

					+ "<link rel=\"stylesheet\" type=\"text/css\" media=\"print\" href=\"/solstice/soquij/css/print.css\" />			  \r\n"
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
					+ "		<script type=\"text/javascript\" src=\"https://soquij.qc.ca/js/bootstrap.min.js\"></script>\r\n"
					+ "	<script type=\"text/javascript\" src=\"https://soquij.qc.ca/js/jquery-cookie.js\"></script>\r\n"
					+ "	<script type=\"text/javascript\" src=\"https://soquij.qc.ca/js/taille_police.js\"></script>\r\n"
					+ "	<script type=\"text/javascript\" src=\"https://soquij.qc.ca/js/swfobject.js\"></script>\r\n"
					+ "	<script type=\"text/javascript\" src=\"https://soquij.qc.ca/js/thickbox.js\"></script>\r\n"
					+ "	<script type=\"text/javascript\" src=\"https://soquij.qc.ca/js/fonctions.js?v=3\"></script>\r\n"
					+ "\r\n"
					+ "	\r\n"
					+ "	<link rel=\"shortcut icon\" href=\"https://soquij.qc.ca/favicon.ico?v=3\" />\r\n"
					+ "\r\n"
					+ "			<link rel=\"alternate\" type=\"application/rss+xml\" title=\"SOQUIJ - Nouvelles et communiqués\" href=\"https://soquij.qc.ca/fr/fils-rss/nouvelles-et-communiques.xml\" />\r\n"
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
		else
			head = "<head>"
				 + "<meta name=\"vs_targetSchema\" content=\"http://schemas.microsoft.com/intellisense/ie5\">"
			     + "<link rel=\"stylesheet\" type=\"text/css\" href=\"styles/siq.css\">"
			     + "<title>Menu principal</title>"
			     + "</head>"
			     + "<body>"
			     + "<table width=\"900\" align=\"center\" border=\"0\" cellspacing=\"2\" cellpadding=\"0\">"
			     + "<tr>"
			     + "<td align=\"center\" valign=\"middle\"><img src=\""+ webLogoHeader1 + "\" width=\"211\" height=\"100\" border=\"0\"></td>" 
			     + "<td align=\"center\" valign=\"middle\"><img src=\""+ webLogoHeader2 + "\" width=\"450\" height=\"57\" border=\"0\"></td></td>"
			     + "</tr>"
			     + "</table>"
			     ;
	
		return fileText.replace("<!-- HEADING: -->", head);
	}

	
	/**
	* Assign a template variable.
	* For variables that are used in blocks, the variable value
	* must be set before <code>parse</code> is called.
	* @param varName the name of the variable to be set.
	* @param varData the new value of the variable.
	*/
	public void assign(String varName, String varData)
	{
		vars.put(varName, convertHTMLString(varData));
	}

	public void assign(String varName, int varData)
	{
		String tmp = String.valueOf(varData);
		vars.put(varName, nvl( tmp ));
	}

	public void assign(String varName, Timestamp varData)
	{
	    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	    if ( varData != null )
	    {
			String tmp = dateFormatter.format(varData);
			vars.put(varName, tmp );
	    }
	    else
	    	vars.put(varName, nvl(null));
	}

	public void assign(String varName, BigDecimal varData)
	{
		if ( varData != null)
		{
			String tmp = String.valueOf(varData);
			vars.put(varName, tmp);
		}
		else
	    	vars.put(varName, nvl(null));
	}


	/**
	* Generates the HTML page and return it into a String.
	*/
	public String out()
	{
		return(parsedBlocks.get("main").toString());
	}
	
	/**
	* Parse a template block.
	* If the block contains variables, these variables must be set
	* before the block is added.
	* If the block contains subblocks, the subblocks
	* must be parsed before this block.
	* @param blockName the name of the block to be parsed.
	*/
	public void parse(String blockName)
	{
		String copy = "";
		try {
			copy = blocks.get(blockName).toString();
		} catch (NullPointerException e) {
			
		}
		Pattern pattern = Pattern.compile("\\{([\\w\\.]+)\\}");
		Matcher matcher = pattern.matcher(copy);
		pattern = Pattern.compile("_BLOCK_\\.(.+)");
		for (Matcher matcher2; matcher.find();)
		{
			String match = matcher.group(1);
			matcher2 = pattern.matcher(match);
			if (matcher2.find())
			{
				if (parsedBlocks.containsKey(matcher2.group(1)))
				{
					copy = copy.replaceFirst("\\{"+match+"\\}", parsedBlocks.get(matcher2.group(1)).toString());
				}
				else
				{
					copy = copy.replaceFirst("\\{"+match+"\\}", "");
				}
			}
			else
			{
				if (vars.containsKey(match))
				{
					copy = copy.replaceFirst("\\{"+match+"\\}", vars.get(match).toString());
				}
				else
				{
					copy = copy.replaceFirst("\\{"+match+"\\}", "");
				}
			}
		}
		if (parsedBlocks.containsKey(blockName))
		{
			parsedBlocks.put(blockName, parsedBlocks.get(blockName) + copy);
		}
		else
		{
			parsedBlocks.put(blockName, copy);
		}
		if (subBlocks.containsKey(blockName))
		{
			parsedBlocks.put(subBlocks.get(blockName), "");
		}
	}
	
	private String readFile(String fileName) throws IOException
	{
		File f = new File(fileName);
		FileReader fr = new FileReader(f);
		StringBuffer content = new StringBuffer();
		for (int c; (c = fr.read()) != -1; content.append((char)c));
		fr.close();
		return content.toString();
	}
	
	private void makeTree(String fileText)
	{
		Pattern pattern = Pattern.compile("<!--\\s*(BEGIN|END)\\s*:\\s*(\\w+)\\s*-->(.*?)(?=(?:<!--\\s*(?:BEGIN|END)\\s*:\\s*\\w+\\s*-->)|(?:\\s*$))", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(fileText);
		ArrayList<String> blockNames = new ArrayList<String>();
		String parentName = "";
		while (matcher.find())
		{
			if (matcher.group(1).toUpperCase().equals("BEGIN"))
			{
				parentName = implode(blockNames);
				blockNames.add(matcher.group(2));
				String currentBlockName = implode(blockNames);
				if (blocks.containsKey(currentBlockName))
				{
					blocks.put(currentBlockName, blocks.get(currentBlockName) + matcher.group(3));
				}
				else
				{
					blocks.put(currentBlockName, matcher.group(3));
				}
				if (blocks.containsKey(parentName))
				{
					blocks.put(parentName, blocks.get(parentName) + "{_BLOCK_." + currentBlockName + "}");
				}
				else
				{
					blocks.put(parentName, "{_BLOCK_." + currentBlockName + "}");
				}
				subBlocks.put(parentName, currentBlockName);
				subBlocks.put(currentBlockName, "");
			}
			else if (matcher.group(1).toUpperCase().equals("END"))
			{
				blockNames.remove(blockNames.size()-1);
				parentName = implode(blockNames);
				if (blocks.containsKey(parentName))
				{
					blocks.put(parentName, blocks.get(parentName) + matcher.group(3));
				}
				else
				{
					blocks.put(parentName, matcher.group(3));
				}
			}
		}
	}
	
	private String implode(ArrayList<String> al)
	{
		String ret = "";
		for (int i = 0; al.size() > i; i++)
		{
			if (i != 0)
			{
				ret += ".";
			}
			ret += al.get(i);
		}
		return (ret);
	}
	
    private String nvl( String value )
    {
    	if ( value == null )
    	  return "&nbsp;";
    	
    	return value;
    }

	public static String convertHTMLString( String s )
	{
		if ( s == null )
			return "&nbsp;";
		
	    String output = "";
	    for (int i = 0; i < s.length(); i++) {
	      output += removeAccent(s.charAt(i));
	    }
		return output;
	}
 	
    private static String removeAccent(char c) 
    {
		    if (c == 'á') return "&aacute;";
		    if (c == 'â') return "&acirc;";
		    if (c == 'æ') return "&aelig;";
		    if (c == 'à') return "&agrave;";
		    if (c == 'å') return "&aring;";       
		    if (c == 'ã') return "&atilde;";
		    if (c == 'ä') return "&auml;";
		    if (c == 'ç') return "&ccedil;";
		    if (c == 'é') return "&eacute;";
		    if (c == 'ê') return "&ecirc;";
		    if (c == 'è') return "&egrave;";
		    if (c == 'ë') return "&euml;";
		    if (c == 'í') return "&iacute;";
		    if (c == 'î') return "&icirc;";
		    if (c == 'ì') return "&igrave;";
		    if (c == 'ï') return "&iuml;";
		    if (c == 'ñ') return "&ntilde;";
		    if (c == 'ó') return "&oacute;";
		    if (c == 'ô') return "&ocirc;";
		    if (c == 'ò') return "&ograve;";
		    if (c == 'ø') return "&oslash;"; 
		    if (c == 'õ') return "&otilde;";
		    if (c == 'ö') return "&ouml;";
		    if (c == 'ß') return "&szlig;";
		    if (c == 'þ') return "&thorn;"; 
		    if (c == 'ú') return "&uacute;";
		    if (c == 'û') return "&ucirc;";
		    if (c == 'ù') return "&ugrave;";
		    if (c == 'ü') return "&uuml;";
		    if (c == 'ý') return "&yacute;";
		    if (c == 'ÿ') return "&yuml;";
		    return "" + c;
		  }  
}
