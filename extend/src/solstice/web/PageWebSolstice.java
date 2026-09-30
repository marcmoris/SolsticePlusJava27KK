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

import java.io.*;
import java.util.*; 
import java.util.logging.Level;
import java.text.*;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.PageContext;

import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Language;

import solstice.custom.IUserInfo;
import solstice.model.P_Employee;
import solstice.model.P_Language;
import solstice.model.P_Frequency;
import solstice.utils.PgiUtil;

import org.compiere.model.MClient;
import java.net.*;

public class PageWebSolstice extends HttpServlet implements SingleThreadModel 
	{
	
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		boolean isThreadSafe=true;
	
		private URL       searchedURL;       // URL de départ
		
		protected Jtpl tpl;

		public final static String		CTX_SERVER_CONTEXT = "context";
		public final static String		CTX_DOCUMENT_DIR = "documentDir";

		/**	Logger							*/
		public static CLogger		log = CLogger.getCLogger (PageWebSolstice.class);

		protected HttpSession session;
		protected HttpServletResponse response;
		protected HttpServletRequest request;
		protected PageContext pageContext;
//		protected IUserInfo userInfo = null;
        private String template = null;
		private String pageId = null;

		/** Session Context 			*/
		public Properties		ctx = new Properties();
		/**	Session Language			*/
		public Language			language = null;
		
		/** Localized Date format       */
		public SimpleDateFormat dateFormat = null;
		/** Localized Timestamp format  */
		public SimpleDateFormat dateTimeFormat = null;

		/** Localized Amount format    */
		public DecimalFormat 	amountFormat = null;
		/** Localized Integer format    */
		public DecimalFormat 	integerFormat = null;
		/** Localized Number format     */
		public DecimalFormat 	numberFormat = null;
		/** Localized Quantity format   */
		public DecimalFormat 	quantityFormat = null;

		

		public boolean firstLoad = true;

		boolean securityCheck = true;

		public MClient Client;

		public P_Employee getEmployeeConnected()
		{
			if ( ! securityCheck )
				return null;
	
			IUserInfo userInfo = null;
			userInfo = (IUserInfo)session.getAttribute("userInfo");
			P_Employee EmployeeConnected = P_Employee.get( Env.getCtx(), userInfo.getEmployeeId(), null);


			return EmployeeConnected;
		}
		
		public void setSecurityCheck( boolean securityCheck  )
		{
			this.securityCheck = securityCheck;
		}

		public boolean getSecurityCheck(  )
		{
			return this.securityCheck;
		}

		public void setPageId( String id )
		{
			this.pageId = id;
		}
	
		public String getPageId( )
		{
			return this.pageId;
		}
		
		public void setTemplate( String template ) throws Exception
		{

			this.template = template;
	
//			this.tpl = new Jtpl("C:\\Solstice\\Sources\\serverRoot\\src\\web\\solstice\\template\\" + this.getTemplate());

			URLConnection urlConnection;
			HttpsURLConnection httpsUrlConnection;
			
			InputStream httpStream;
			BufferedReader urlReader = null;
			try
			{
//		        System.out.println(" get WebAppURL");
			    String webAppUrl = PgiUtil.getSolsticeParameter(Env.getCtx(), "WebAppURL");
//				URL fileURL = new URL( webAppUrl + "/template/" + this.getTemplate()); 
				URL fileURL = new URL( "http://10.10.10.14:8099/solstice/template/" + this.getTemplate()); 

//		        System.out.println( "WebAppURl" + webAppUrl + "template/" + this.getTemplate() );
		        
//		        System.out.println( " fileURL : " + fileURL.getFile() );

				//				URL fileURL = new URL("http://pgi0133:8099/template/" + this.getTemplate());
		        if ( webAppUrl.startsWith("https"))
		        {
		        	String webAppUrl2 = webAppUrl.replace("https", "http"); 
		        	webAppUrl2 = webAppUrl2.replace("8443", "8099"); 

		        	webAppUrl2 = webAppUrl2.replace("rh-essai/", "rh-essai:8099/"); 

		        	//https://rh-essai/solstice/index.jsp
		        	fileURL = new URL( webAppUrl2 + "template/" + this.getTemplate());

//					System.out.println( " fileURL : " + fileURL.getFile() );
//					System.out.println( " webAppUrl2 : " + webAppUrl2 + "template/" + this.getTemplate() );

					urlConnection = fileURL.openConnection();     // open URL (HTTP query)
//					httpsUrlConnection = (HttpsURLConnection)fileURL.openConnection();     // open URL (HTTP query)
					httpStream = urlConnection.getInputStream();  // Open data stream
		  	        urlReader = new BufferedReader (new InputStreamReader (httpStream));
		        }
		        else
		        {
					urlConnection = fileURL.openConnection();     // open URL (HTTP query)
					httpStream = urlConnection.getInputStream();  // Open data stream
		  	        urlReader = new BufferedReader (new InputStreamReader (httpStream));
		        }
			}
		    catch (Exception e)
		    {
		        System.out.println("No Content Available : " + e.toString() );
		        log.log (Level.SEVERE, "PageWebSolstice", e);
		        return;
		    }


			String line;
			StringBuffer text = new StringBuffer();;
			while ((line=urlReader.readLine())!=null)
		    {
		    	text.append( line + "\n" );
		    }
			
			urlReader.close();
			
//			System.out.println( "Read Template : " + text.toString()  );
			
 	        this.tpl = new Jtpl( text.toString() , false);

			tpl.assign("STYLES", "styles/siq.css");
			
			getInfo();
			String webLogoHeader1 = PgiUtil.getSolsticeParameter(ctx, "webLogoHeader1");
			String webLogoHeader2 = PgiUtil.getSolsticeParameter(ctx, "webLogoHeader2");
//			tpl.assign("HEADING1", "<img src=\"images/LogoBAN.jpg\" width=\"211\" height=\"100\" border=\"0\">" );
//			tpl.assign("HEADING2","<img src=\"images/SIQBAN.GIF\" width=\"450\" height=\"57\" border=\"0\"></td>" );
			tpl.assign("HEADING1", "<img src=\""+ webLogoHeader1 + "\" width=\"211\" height=\"100\" border=\"0\">" );
			tpl.assign("HEADING2","<img src=\""+ webLogoHeader2 + "\" width=\"450\" height=\"57\" border=\"0\"></td>" );
			
			tpl.assign("FOOTING","<tr><td colspan=\"2\"><div style=\"padding-top: 20px\" class=\"siqname\">" + Client.getDescription() + "</div></td></tr><tr><td colspan=\"2\" class=\"line\"> </td></tr>");
				

	      	tpl.assign("CIE_NAME", Client.getDescription());

		}
	
		public String getTemplate( )
		{
			return this.template;
		}

		public String getLanguage( )
		{
			return this.language.getAD_Language();
		}

		/**
		 * 	Set Language and init formats
		 *	@param lang language
		 */
		private void setLanguage (Language lang)
		{
			language = lang;
			//
			dateFormat = DisplayType.getDateFormat(DisplayType.Date, language);
			dateTimeFormat = DisplayType.getDateFormat(DisplayType.DateTime, language);
			//
			amountFormat = DisplayType.getNumberFormat(DisplayType.Amount, language);
			integerFormat = DisplayType.getNumberFormat(DisplayType.Integer, language);
			numberFormat = DisplayType.getNumberFormat(DisplayType.Number, language);
			quantityFormat = DisplayType.getNumberFormat(DisplayType.Quantity, language);
		
		}	//	setLanguage

		
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
		{

			firstLoad = true;

			this.request = request;
			this.session = request.getSession();
			this.response = response;

			JspFactory _jspxFactory = null;
			_jspxFactory = JspFactory.getDefaultFactory();
			this.pageContext =  _jspxFactory.getPageContext(this, request, response, null, true, 8192, true);

			IUserInfo userInfo = null;
			if ( this.session != null )
	          userInfo = (IUserInfo)session.getAttribute("userInfo");
	
			if ( this.session != null )
	          userInfo = (IUserInfo)session.getAttribute("userInfo");

	        PrintWriter out = response.getWriter();
			try 
			{
				String info = this.GeneratePage();
				if ( info != null )
					out.print( info );
			} catch (Exception e) {
				e.printStackTrace(out);
			}
			out.flush();
			out.close();
//			response.getWriter().close();
//			response.reset();
//			response.resetBuffer();
			
		}
	    /**
	     * Lorsqu'on arrive en post, on doit valider le nom d'usager et le password puis créer les
	     * informations d'utilisateur qu'on placera en session sous l'attribute "userInfo"
	     */
	    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	    {
	    	IUserInfo userInfo = null;
	    	
			firstLoad = false;

			this.request = request;
			this.session = request.getSession();
			this.response = response;

			JspFactory _jspxFactory = null;
			_jspxFactory = JspFactory.getDefaultFactory();
			this.pageContext =  _jspxFactory.getPageContext(this, request, response, null, true, 8192, true);

			if ( this.session != null )
	          userInfo = (IUserInfo)session.getAttribute("userInfo");
	
			if ( this.session != null )
	          userInfo = (IUserInfo)session.getAttribute("userInfo");
			
			response.setContentType("text/html");

			PrintWriter out = response.getWriter();
				
			try 
			{
				out.print(this.GeneratePage());
			} catch (Exception e) {
				e.printStackTrace(out);
			}
			out.flush();
			out.close();
//			response.getWriter().close();
//			response.reset();
//			response.resetBuffer();

	        
	    }

		public boolean SecurityCheck()
		{
			IUserInfo userInfo = null;
	        userInfo = (IUserInfo)session.getAttribute("userInfo");

//	    	System.out.println( "SecurityCheck userInfo  " + userInfo.getUserId() ); 

			if ( ! this.securityCheck )
			{
	        	language = Language.getLanguage( request.getLocale().getDisplayLanguage() );
	        	if ( language == null )
	        		language = Language.getBaseLanguage();
				setLanguage(  language );
				return true;
			}

	        // L'utilisateur n'est pas connecté ou n'a pas accès à la page. Erreur 403
	        try
	        {
	            if(userInfo == null )
	            {
	            	String webAppUrl = PgiUtil.getSolsticeParameter(ctx, "WEBAPPURL");
	            	response.sendRedirect(webAppUrl + "login.jsp");
	            }
	            
	        }
	        catch (IOException e)
	        {
				System.out.println("* ERROR * SecurityCheck " + e);
	        }
		
            
			// TODO changer le client ID.
			
			Env.setContext(ctx, "#AD_User_ID",  userInfo.getUserId());

			P_Employee EmployeeConnected = P_Employee.get( Env.getCtx(), userInfo.getEmployeeId(), null);
	        if ( EmployeeConnected != null )
	        {
//	        	MUser User = MUser.get( Env.getCtx(), userInfo.getUserId() );
	        	P_Language Lang = P_Language.get( Env.getCtx(), EmployeeConnected.getP_Language_ID(), null);
	        	language = Language.getLanguage( Lang.getValue() );
	        	if ( language == null )
	        		language = Language.getBaseLanguage();
				setLanguage(  language );
				ctx.setProperty("#AD_Client_ID", String.valueOf(EmployeeConnected.getAD_Client_ID()));
				ctx.setProperty("#AD_Org_ID"   , String.valueOf(EmployeeConnected.getAD_Org_ID()));
				Env.setContext(ctx, "#AD_Client_ID", String.valueOf(EmployeeConnected.getAD_Client_ID()));
				Env.setContext(ctx, "#AD_Org_ID"   , String.valueOf(EmployeeConnected.getAD_Org_ID()));
	        }

			int AD_Role_ID = 0;		//	HARDCODED - System
			Env.setContext(ctx, "#AD_Role_ID", AD_Role_ID);
			//ctx.setProperty("#AD_Language", language.getLanguageCode() );
			ctx.setProperty("#AD_Language", language.getAD_Language() );
			ctx.setProperty("#AD_Role_ID", String.valueOf(AD_Role_ID));
			ctx.setProperty("#AD_User_ID",  String.valueOf( userInfo.getUserId()) );
			//Env.setContext(ctx, "#AD_Language", language.getLanguageCode() );
			Env.setContext(ctx, "#AD_Language", language.getAD_Language() );
/*			
			if (ctx.getProperty("#AD_Language") == null)
				Env.setContext(ctx, "#AD_Language", "en_US");
*/
	        
	        if(userInfo != null && userInfo.hasPolicy(this.pageId))
	        {
	            // L'utilisateur est connecté et a accès à la page. On évalue donc son contenue
	            return true;
	        }

	        try
	        {
	        	response.sendError(403);
	        }
	        catch (IOException e)
	        {
				System.out.println("* ERROR * SecurityCheck " + e);
	        }
            return false;

		}

		public String GeneratePage()  throws Exception 
		{
//	    	System.out.println( "GeneratePage SecurityCheck  " ); 

	    	if ( tpl == null )
	    		System.out.println( "Templace non trouvé." );
	    	
			if ( SecurityCheck() )
			{
//		    	System.out.println( "GeneratePage SecurityCheck OK  " ); 
				tpl.parse("main");
				return (tpl.out());
			}
			else
			{
		    	System.out.println( "Vous n'avez pas accès à cette page" ); 
			}
			return "Vous n'avez pas accès à cette page";
//			return null;
		}

		/**
		 * Permet d'obtenir si l'utilisateur loggé à accès à la politique de sécurité
		 * @param pageContext Le contexte de la page
		 * @param policy la politique de sécurité
		 * @return
		 */
		protected static boolean loggedUserHasPolicy(PageContext pageContext, String policy)
		{
			IUserInfo user = getUserInfo(pageContext);
			return user.hasPolicy(policy);
		}

		/**
		 * Permet d'obtenir l'utilisateur
		 * @param pageContext le contexte de la page
		 * @return l'utilisateur
		 */
		protected static IUserInfo getUserInfo (PageContext pageContext)
		{
			return (IUserInfo)pageContext.getSession().getAttribute("userInfo");
		}

	   public static String getDistributionSQL(PageContext pageContext,
				   String[] selectedFields, boolean orderByValue)
	   {
		   return getUserInfo(pageContext).getDistributionSQL(selectedFields,orderByValue);
	   }

	   public static String getDistributionWebSQL(PageContext pageContext,
				   String[] selectedFields, boolean orderByValue)
	   {
		   return getUserInfo(pageContext).getDistributionWebSQL(selectedFields,orderByValue);
	   }
	   
	   public static String getDistributionWebSQLAbsence(PageContext pageContext,
			   String[] selectedFields, boolean orderByValue)
	   {
		   return getUserInfo(pageContext).getDistributionWebSQLAbsence(selectedFields,orderByValue);
	   }

	   public static String getEmployeeSQL(PageContext pageContext)
	   {
		   return getUserInfo(pageContext).getEmployeeSQL();
	   }

	   
		public String ComboBoxDistributionBooklet( )  
		{
			String selected = ""; 
			if ( request != null && request.getParameter("distributionId") != null)
				selected = request.getParameter("distributionId").trim();
			
			String result  = null;
			String sql = getDistributionWebSQL(pageContext, new String[] {"P_Distribution_Booklet_ID, Value, Name"},true);
			try
			{

				PreparedStatement pstmt = null;
				pstmt = DB.prepareStatement (sql, null);
				ResultSet rs = pstmt.executeQuery ();

				result  = "<select width=\"300\" style=\"width: 300px\" name=\"distributionId\" id=\"distributionId\"  onKeyPress=\"autoSelect(this);\">";
				result += "<option ></option> \n";

				while (rs.next ())
				{
					if ( rs.getString(1).equals(selected ) )
						result += "<option value=\"" + rs.getString(1) + "\" selected > " + rs.getString(2) + " - " + rs.getString(3) + "</option> \n";
					else
						result += "<option value=\"" + rs.getString(1) + "\" > " + rs.getString(2) + " - " + rs.getString(3) + "</option> \n";
				}
				rs.close ();
				pstmt.close ();
				pstmt = null;

				result += "</select>";

			}
			catch (Exception e)
			{
				System.out.println("* ERROR * Read DistributionBooklet " + sql + "Exception :" + e);
			}
			
			return result;
		}

		
		public String ComboBoxEmployee()  
		{
			String selected = ""; 
			if ( request != null && request.getParameter("employeeId") != null)
				selected = request.getParameter("employeeId").trim();
			
			String result  = null;
			String sql = getEmployeeSQL(pageContext);
			try
			{

				PreparedStatement pstmt = null;
				pstmt = DB.prepareStatement (sql, null);
				ResultSet rs = pstmt.executeQuery ();

				result  = "<select name=\"employeeId\" id=\"employeeId\"  onKeyPress=\"autoSelect(this);\">";
				result += "<option></option> \n";

				while (rs.next ())
				{
					if ( rs.getString(1).equals(selected ) )
						result += "<option value=\"" + rs.getString(1) + "\" selected > " + rs.getString(2) + "</option> \n";
					else
						result += "<option value=\"" + rs.getString(1) + "\" > " + rs.getString(2) + "</option> \n";
				}
				rs.close ();
				pstmt.close ();
				pstmt = null;

				result += "</select>";

			}
			catch (Exception e)
			{
				System.out.println("* ERROR * Read Employee " + sql + "Exception :" + e);
			}
		
			return result;
		}


		public String ComboBoxCredits() 
		{
			String selected = ""; 
			if ( request != null && request.getParameter("creditsId") != null)
				selected = request.getParameter("creditsId").trim();
			
			String result  = null;

			String sql = "select P_Credits_ID, ltrim(rtrim(Name)) + ' (' + ltrim(rtrim(Value)) + ')' AS Name "
				   + " from P_Credits"
				   + " where IsActive = 'Y'"
				   + " order by Name ";
			try
			{

				PreparedStatement pstmt = null;
				pstmt = DB.prepareStatement (sql, null);
				ResultSet rs = pstmt.executeQuery ();

				result  = "<select name=\"creditsId\" id=\"creditsId\"  onKeyPress=\"autoSelect(this);\">";
				result += "<option></option> \n";

				while (rs.next ())
				{
					if ( rs.getString(1).equals(selected ) )
						result += "<option value=\"" + rs.getString(1) + "\" selected > " + rs.getString(2) + "</option> \n";
					else
						result += "<option value=\"" + rs.getString(1) + "\" > " + rs.getString(2) + "</option> \n";
				}
				rs.close ();
				pstmt.close ();
				pstmt = null;

				result += "</select>";

			}
			catch (Exception e)
			{
				System.out.println("* ERROR * Read Credits " + sql + "Exception :" + e);
			}

			return result;

		}

		public String ComboBoxFrequency()
		{
			String selected = ""; 
			if ( request != null && request.getParameter("frequencyId") != null)
				selected = request.getParameter("frequencyId").trim();
			
			String result  = null;

			String sql = "select distinct P_Frequency_ID, name, IsDefault "
				   + " from P_Frequency"
				   + " order by Name Desc ";
			try
			{

				PreparedStatement pstmt = null;
				pstmt = DB.prepareStatement (sql, null);
				ResultSet rs = pstmt.executeQuery ();

				result  = "<select name=\"frequencyId\" id=\"frequencyId\"  onKeyPress=\"autoSelect(this);\">";
				while (rs.next ())
				{
					if ( rs.getString(1).equals(selected ) || ( selected.equals("") && rs.getString("IsDefault").equals("Y") ) )
						result += "<option value=\"" + rs.getString(1) + "\" selected > " + rs.getString(2) + "</option> \n";
					else
						result += "<option value=\"" + rs.getString(1) + "\" > " + rs.getString(2) + "</option> \n";
				}
				rs.close ();
				pstmt.close ();
				pstmt = null;

				result += "</select>";

			}
			catch (Exception e)
			{
				System.out.println("* ERROR * Read Year " + sql + "Exception :" + e);
			}

			return result;

		}
		
		public String ComboBoxYear() 
		{
			String selected = ""; 
			if ( request != null && request.getParameter("yearId") != null)
				selected = request.getParameter("yearId").trim();
			
			String result  = null;

			String sql = "select distinct P_Year_ID, Year, IsDefault "
				   + " from P_Year"
				   + " order by Year Desc ";
			try
			{

				PreparedStatement pstmt = null;
				pstmt = DB.prepareStatement (sql, null);
				ResultSet rs = pstmt.executeQuery ();

				result  = "<select name=\"yearId\" id=\"yearId\"  onKeyPress=\"autoSelect(this);\">";
				result += "<option></option> \n";

				while (rs.next ())
				{
					if ( rs.getString(1).equals(selected ) || ( selected.equals("") && rs.getString("IsDefault").equals("Y") ) )
						result += "<option value=\"" + rs.getString(1) + "\" selected > " + rs.getString(2) + "</option> \n";
					else
						result += "<option value=\"" + rs.getString(1) + "\" > " + rs.getString(2) + "</option> \n";
				}
				rs.close ();
				pstmt.close ();
				pstmt = null;

				result += "</select>";

			}
			catch (Exception e)
			{
				System.out.println("* ERROR * Read Year " + sql + "Exception :" + e);
			}

			return result;

		}

		public String ComboBoxPeriod() 
		{
			String selected = ""; 
			if ( request != null && request.getParameter("periodId") != null)
				selected = request.getParameter("periodId").trim();
			
			String result  = null;

//			P_Period period = P_Period.getOpenPeriod(Env.getCtx(), null);
			// TODO - Deteminer le périodicité
			
			String sqlgrp = "select P_Frequency_ID From P_Frequency order by name ";
			
			String sql = "";
			try
			{

				PreparedStatement pstmtgrp = null;
				pstmtgrp = DB.prepareStatement (sqlgrp, null);
				ResultSet rsgrp = pstmtgrp.executeQuery ();


				result  = "<select name=\"periodId\" id=\"periodId\"  onKeyPress=\"autoSelect(this);\">";
				result += "<option></option> \n";

				while ( rsgrp.next ())
				{

					 sql = "select distinct P_Period_ID, Name, StartDate, EndDate, IsDefault, P_Frequency_ID "
						   + " from P_Period"
						   + " Where P_Frequency_ID = " + rsgrp.getInt("P_Frequency_ID")
						   + " order by P_Frequency_ID, StartDate Desc ";

					P_Frequency Frequency = P_Frequency.get( Env.getCtx(), rsgrp.getInt("P_Frequency_ID"), null);
					result += "<option value=\" \" > >>-- " + Frequency.getName() +  " --<< </option> \n";

					PreparedStatement pstmt = null;
					pstmt = DB.prepareStatement (sql, null);
					ResultSet rs = pstmt.executeQuery ();

					while (rs.next ())
					{
						if ( rs.getString(1).equals(selected ) || ( selected.equals("") && rs.getString("IsDefault").equals("Y") ) )
							result += "<option value=\"" + rs.getString(1) + "\" selected > " + rs.getString(2) + " - " + " Du " + dateFormat.format(rs.getTimestamp(3)) + " au " + dateFormat.format(rs.getTimestamp(4)) + "</option> \n";
						else
							result += "<option value=\"" + rs.getString(1) + "\" > " + rs.getString(2) + " - " + " Du " + dateFormat.format(rs.getTimestamp(3)) + " au " + dateFormat.format(rs.getTimestamp(4)) + "</option> \n";
					}
					rs.close ();
					pstmt.close ();
					pstmt = null;

				}
				rsgrp.close ();
				pstmtgrp.close ();
				pstmtgrp = null;

				result += "</select>";

			}
			catch (Exception e)
			{
				System.out.println("* ERROR * Read P_Period " + sql + "Exception :" + e);
			}

			return result;

		}

		private void getInfo()
		{
			int AD_Client_ID = 0;
			String sql = "SELECT * FROM AD_CLIENT WHERE AD_CLIENT_ID != 0 ";
			try
			{

				PreparedStatement pstmt = null;
				pstmt = DB.prepareStatement (sql, null);
				ResultSet rs = pstmt.executeQuery ();
				if (rs.next ())
				{
					AD_Client_ID = rs.getInt( "AD_Client_ID");
					Env.setContext(ctx, "#AD_Client_ID", AD_Client_ID);
					Env.setContext(ctx, "#AD_Org_ID", AD_Client_ID);
					ctx.setProperty("#AD_Client_ID", String.valueOf(AD_Client_ID));
					ctx.setProperty("#AD_Org_ID", String.valueOf(AD_Client_ID));
					Client = new MClient( ctx, AD_Client_ID, null  );
				}

				rs.close ();
				pstmt.close ();
				pstmt = null;

			}
			catch (Exception e)
			{
				System.out.println("* ERROR * Read getInfo " + sql + "Exception :" + e);
			}

		}

		
}
