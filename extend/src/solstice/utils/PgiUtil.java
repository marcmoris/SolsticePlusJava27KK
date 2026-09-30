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
package solstice.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Language;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;

import org.compiere.model.MClient;
import org.compiere.model.MSystem;
import org.compiere.model.MUser;

import java.io.File;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.mail.internet.InternetAddress;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.compiere.util.DB;


/**
 * @author marmor01
 *
 * 
 */
public class PgiUtil 
{
    /**
     * 
     * @author frafor01
     *
     * Cet interface est utilisé dans la méthode translateQuery. Elle représente un champ général à utiliser
     * dans la requête SQL à générer
     */
    public static interface IQueryField
    {
        public String getSQLColumn();
    }

	private static CLogger		s_log = CLogger.getCLogger (PgiUtil.class);

    /**
     * 
     * @author frafor01
     *
     * Cette classe représente un champ classique dans la requête à générer. Dans le cas où ce champ doit être traduit,
     * la méthode getSQLColumn retournera la version traduite du champ pour la requête
     */
    public static class QueryField implements IQueryField
    {
        private String tableName;
        private String tableAlias;
        private String fieldName;
        private String fieldAlias;


        /**
         * Constructeur lorsque la table ne possède pas d'alias dans le FROM
         */
        public QueryField(String tableName, String fieldName)
        {
            this(tableName, tableName, fieldName, null);
        }
        
        /**
         * Constructeur lorsque la table possède un alias dans le FROM
         */
        public QueryField(String tableName, String tableAlias, String fieldName)
        {
            this(tableName, tableAlias, fieldName, null);
        }
        
        /**
         * Constructeur lorsque la table possède un alias dans le FROM et que le champ possède un alias
         */
        public QueryField(String tableName, String tableAlias, String fieldName, String fieldAlias)
        {
            this.tableName = tableName;
            this.tableAlias = tableAlias;
            this.fieldName = fieldName;
            this.fieldAlias = fieldAlias;
        }
        
        /**
         * Tente une traduction si le cas l'exige
         */
        public String getSQLColumn() 
        {
            // Si la langue courrante est différente de celle de base dans compiere (anglais), on tente une traduction
            Language language = Env.getLanguage(Env.getCtx());
            if(!language.isBaseLanguage())
            {
	            String sql
	            = "select 1"
	                + " from AD_Table inner join AD_Column"
	                + " on AD_Table.AD_Table_ID = AD_Column.AD_Table_ID"
	                + " where AD_Table.TableName like '" + this.tableName + "'"
	                + " and AD_Column.ColumnName like '" + this.fieldName + "'"
	                + " and AD_Column.IsTranslated = 'Y'"
	                + " and exists (select 1 from AD_Table where TableName = '" + this.tableName + "_TRL')";
	            
	            try
	            {
	                PreparedStatement stmt = DB.prepareStatement(sql, null);
	                ResultSet rs = stmt.executeQuery();
	                
	                // Si le champ est traduit, on retourne la bonne traduction
	                if(rs.next())
	                {
	                    rs.close();
	                    stmt.close();
	                    
	                    return
	                    "(select " + this.fieldName
		                    + " from " + this.tableName + "_TRL"
		                    + " where " + this.tableName + "_ID = "+ this.tableAlias +"." + this.tableName + "_ID"
		                    + " and AD_Language = '" + language.getAD_Language() + "')" + getAlias();
	                }
	                
	                rs.close();
	                stmt.close();
	            }
	            catch(SQLException e)
	            {
	    			s_log.log(Level.SEVERE,"PgiUtil", e);
	                System.out.println(e);
	            }
            }
            // Sinon, on retourne le champ en tant que tel
            return this.tableAlias + "." + this.fieldName + getAlias();
        }
        
        /**
         * Crée un alias du type FIELD as "FIELDALIAS" si ce dernier a été spécifié
         */
        private String getAlias()
        {
            if(this.fieldAlias != null)
                return " as \"" + this.fieldAlias + "\"";
            return "";
        }
    }
    
    /**
     * 
     * @author frafor01
     *
     * Cette classe est utilisée pour insérer un champ calculé, une fonction ou une autre chose 
     * qui ne peut pas être traduite
     */
    public static class CustomQueryField implements IQueryField
    {
        private String sqlColumn;
        
        /**
         * Constructeur
         */
        public CustomQueryField(String sqlColumn)
        {
            this.sqlColumn = sqlColumn;
        }
        
        /**
         * Retourne simplement le champ
         */
        public String getSQLColumn() {return this.sqlColumn;}
    }
    
    /**
     * 
     * @author frafor01
     *
     * Cette classe affiche le text lié à une référence dans la table AD_Ref_List ou 
     * dans la table AD_Ref_List_TRL. La traduction est donc aussi faite
     */
    public static class ReferencedQueryField implements IQueryField
    {
        private String refListName;
        private String tableAlias;
        private String fieldName;
        private String fieldAlias;
        
        /**
         * Constructeur lorsque le champ ne possède pas d'alias
         */
        public ReferencedQueryField(String refListName, String tableAlias, String fieldName)
        {
            this(refListName, tableAlias, fieldName, null);
        }
        
        /**
         * Constructeur lorsque le champ possède un alias
         */
        public ReferencedQueryField(String refListName, String tableAlias, String fieldName, String fieldAlias)
        {
            this.refListName = refListName;
            this.tableAlias = tableAlias;
            this.fieldName = fieldName;
            this.fieldAlias = fieldAlias;
        }
        
        /**
         * Cette méthode vérifie s'il y a une référence pour cette valeur. Si oui,
         * il retourne la valeur dans la bonne langue
         */
        public String getSQLColumn()
        {
            Language language = Env.getLanguage(Env.getCtx());
            String sql
            = "select 1"
                + " from AD_Reference"
                + " where Name like '" + this.refListName + "'"
                + " and ValidationType = 'L'";
            
            try
            {
                PreparedStatement stmt = DB.prepareStatement(sql, null);
                ResultSet rs = stmt.executeQuery();
                
                // S'il existe une valeur de référence pour ce champ
                if(rs.next())
                {
                    rs.close();
                    stmt.close();

                    // Si la langue courrante est celle de base, vérifie la référence
                    if(language.isBaseLanguage())
                    {
                        return "(select AD_Ref_List.Name"
                        + " from AD_Ref_List"
                        + " where AD_Ref_List.Value = " + this.tableAlias + "." + this.fieldName
                        + " and exists ("
                        + "   select 1"
                        + "   from AD_Reference"
                        + "   where AD_Reference_ID = AD_Ref_List.AD_Reference_ID"
                        + "   and AD_Reference.Name like '" + this.refListName + "'"
                        + " ))" + this.getAlias();
                    }
                    // Sinon, on vérifie s'il y a une référence pour la traduction
                    return "(select AD_Ref_List_TRL.Name"
                    + " from AD_Ref_List inner join AD_Ref_List_TRL"
                    + " on AD_Ref_List.AD_Ref_List_ID = AD_Ref_List_TRL.AD_Ref_List_ID"
                    + " where AD_Ref_List.Value = " + this.tableAlias + "." + this.fieldName
                    + " and exists ("
                    + "   select 1"
                    + "   from AD_Reference"
                    + "   where AD_Reference_ID = AD_Ref_List.AD_Reference_ID"
                    + "   and AD_Reference.Name like '" + this.refListName + "'"
                    + " ))" + this.getAlias();
                }
                
                rs.next();
                stmt.close();
            }
            catch (SQLException e)
            {
    			s_log.log(Level.SEVERE,"PgiUtil", e);
                System.out.println(e);
            }
            // La référence n'existe pas
            return this.tableAlias + "." + this.fieldName + this.getAlias();
        }

        /**
         * Crée un alias du type FIELD as "FIELDALIAS" si ce dernier a été spécifié
         */
        private String getAlias()
        {
            if(this.fieldAlias != null)
                return " as \"" + this.fieldAlias + "\"";
            return "";
        }
    }

    // TODO deprecated
	public static String getWebServer (Properties ctx, String module )  
	{
		String url = PgiUtil.getSolsticeParameter(Env.getCtx(), "WebAppURL");
		return url;
		
	}
	
	
	public static String getWebServerLogin (Properties ctx, String user )
	{
		String url ;
		    	// Env.getContext(Env.getCtx(), "#AD_User_Name")
        url = PgiUtil.getSolsticeParameter(Env.getCtx(), "WebAppURL") + "login.jsp?user=" + user + "&redirection=%2F";	    return url;
	}

	
	private static final java.util.concurrent.ConcurrentHashMap<String, String> s_paramCache = new java.util.concurrent.ConcurrentHashMap<String, String>();

	public static void clearSolsticeParameterCache() {
		s_paramCache.clear();
	}

	public static String getSolsticeParameter (Properties ctx, String ParameterName )
	{
		if (ParameterName == null)
			return null;
		String cached = s_paramCache.get(ParameterName);
		if (cached != null)
			return cached;

		String Parameter = null;
	    String sql = "select top 1 Parameter from P_Solstice_Parameters Where Value = '" + ParameterName + "'";
        PreparedStatement stmt = DB.prepareStatement(sql, null);
		try
		{
		    ResultSet rs = stmt.executeQuery();
		    if(rs.next())
		    {
		    	// Env.getContext(Env.getCtx(), "#AD_User_Name")
		    	Parameter = rs.getString(1);
		    }
		    rs.close();
		    stmt.close();
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"PgiUtil", e);
			return null;
		}
		
		if (Parameter != null)
			s_paramCache.put(ParameterName, Parameter);
	    return Parameter;
	}

	
	public static boolean PasswordMustChange( Properties ctx, int AD_User_ID)
	{
		
		MUser User = MUser.get( ctx, AD_User_ID );
		
		if ( User.isPasswdUnexpirable())
			return false;
		
		if ( User.isMustChangePassword())
			return true;
		
		int NbrDayWarning = Integer.parseInt( getSolsticeParameter(Env.getCtx(), "PWD_DAYSWARNING"));
    		
        Timestamp today = TimeUtil.getDay(TimeUtil.getToday().getTimeInMillis() );

        if ( User.getPasswordFinished() == null )
           return true;
        
		if (  User.getPasswordFinished().compareTo( today ) <= 0)
			   return true;

        if (  User.getPasswordFinished().compareTo( TimeUtil.addDays(today, NbrDayWarning )) <= 0)
		{
    		//Your password will expire in %1 day(s). Do you want to change it now?
            String Message = Msg.getMsg(Env.getCtx(), "STR_PWDEXPIRESOON");
        		Message = Message.replace("%1", String.valueOf( TimeUtil.getDaysBetween(today, TimeUtil.addDays(today, NbrDayWarning ))-1) );

        	
        	int ask = JOptionPane.showConfirmDialog(null,
					Message,									//	message
					Msg.getMsg(Env.getCtx(), "STR_PWDMANAGEMENT"),	//	title
					 JOptionPane.YES_NO_OPTION	);

			if ( ask == JOptionPane.YES_OPTION  )
				return true;
		}

		
		return false;
	}

	// TODO Paramètre la lecture de cette information;
    public static InternetAddress getSendEmailAddress( String type )
    {
//    	String email = "drh@siq.gouv.qc.ca";
    	String email = getSolsticeParameter( Env.getCtx(), "EmailDRH");
    	String sender = "Direction Ressources humaines";
    	if ( type.equals("drh"))
    	{
    		email = getSolsticeParameter( Env.getCtx(), "EmailDRH");
    		//email = "drh@siq.gouv.qc.ca";
    		sender = "Direction Ressources humaines";
    		
    	}
/*
    	if ( type.equals("permis.horodateur"))
    	{
    		email = "permis.horodateur@siq.gouv.qc.ca";
    		sender = "Permis horodateur";
    		
    	}
  */  		
    	
    	InternetAddress internetAddress = null;
        try
        {
        	internetAddress = new InternetAddress( email , sender);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return internetAddress;
    	
    }

    /**
     * Crée les propriétés de connection selon les informations
     * de la table AD_Client relatif à l'employé
     */
    private static Properties getProperties( Properties ctx, int AD_Client_ID ) throws SQLException
    {
    	MClient Client = MClient.get( ctx, AD_Client_ID );
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", Client.getSmtpHost() );

        System.out.println("mail.transport.protocol smtp" +  Client.getSmtpHost() );

        return props;
    }

    /**
     * Envoit le e-mail
     */
    public static void sendEMail(Properties ctx,  String emailFrom, String emailTo, String subject, String body) throws Exception
    {
        System.out.println("Email From:"+emailFrom);
        System.out.println("Email To :"+emailTo);
        if(emailTo != null)
        {
	        Properties props = getProperties( ctx, Env.getAD_Client_ID(ctx));
	
	        Session mailSession = Session.getDefaultInstance(props, null);
	        Transport transport = mailSession.getTransport();
	
	        MimeMessage message = new MimeMessage(mailSession);
	        message.setFrom( new InternetAddress( emailFrom ) );
	        message.setSubject(subject);
	        message.setContent(body, "text/html");
	        message.addRecipient(Message.RecipientType.TO, new InternetAddress( emailTo ));
	        transport.connect();
	        transport.sendMessage(message,
	            message.getRecipients(Message.RecipientType.TO));
	        transport.close();
        }
    }
    

    public static String getReportAppServer()
    {
    	return getSolsticeParameter( Env.getCtx(), "ReportServer");
    }
    
	// TODO Paramètre la lecture de cette information;
    public static String getEmailDomain(  )
    {
    	return getSolsticeParameter( Env.getCtx(), "EmailDomain");
//        return "@siq.gouv.qc.ca"; 
    }

	/**
	 * Cette méthode génère une requête SQL en traduisant les champs passés en paramètre lorsque c'est 
	 * nécessaire. Les trois implémentations possibles de l'interface IQueryField lors de la création de
	 * cette méthode, sont QueryField, ReferencedQueryField et CustomQueryField.
	 */
	public static String translateQuery(IQueryField[] fields, String remainingQuery)
	{
	    if(fields.length == 0) return null;
	    
	    String sql = "select ";
	    for(int i = 0; i < fields.length - 1; i++)
	    {
	        sql += fields[i].getSQLColumn() + ", ";
	    }
	    
	    return sql + fields[fields.length - 1].getSQLColumn() + " " + remainingQuery;
	}

	public static String charToHex(char c)
	{
	    int i = (int)c;
	    int x = (int)Math.floor(i / 16.0d);
	    int y = (int)(i % 16.0d);
	    
	    return String.valueOf(x >= 10 ? (char)(x - 10 + 'A') : (char)(x + '0')) 
	    + String.valueOf(y >= 10 ? (char)(y - 10 + 'A') : (char)(y + '0'));
	}
	
	public static String[] split(String value, char c)
    {
	    Vector<String> v = new Vector<String>();
	    String buffer = "";
	    for(int i = 0; i < value.length(); i++)
	    {
	        if(value.charAt(i) != c)
	        {
	            buffer = buffer + value.charAt(i);
	        }
	        else
	        {
	            v.add(buffer);
	            buffer = "";
	        }
	    }
	    v.add(buffer);
	    
	    String[] out = new String[v.size()];
	    for(int i = 0; i < v.size(); i++)
	    {
	        out[i] = (String)v.elementAt(i);
	    }
	    
        return out;
    }
	
	public static String join (String[] values, String jointure)
	{
	    if(values != null)
	    {
	        if(values.length > 0)
	        {
	            String buffer = "";
	            for(int i = 0; i < values.length - 1; i++)
	            {
	                buffer += values[i] + jointure;
	            }
	            buffer += values[values.length-1];
	            return buffer;
	        }
	        return "";
	    }
	    return null;
	}

	/**
	 * Convertit une date du format yyyy-MM-dd en Calendar
	 */
	public static Calendar stringToDate(String value)
	{	
		//+2012.08.24 ne transfort pas ce qui n'est pas une date 
		if ( value == null )
			return null;
		
		if ( value.length() < 10 )
			return null;
		//-2012.08.24 
		
		try
		{
			int yyyy = Integer.parseInt(value.substring(0, 4));
			int mm = Integer.parseInt(value.substring(5, 7));
			int dd = Integer.parseInt(value.substring(8));
			
			Calendar calendar = GregorianCalendar.getInstance();
			calendar.set(Calendar.MILLISECOND, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.HOUR, 0);
			calendar.set(Calendar.YEAR, yyyy);
			calendar.set(Calendar.MONDAY, mm-1);
			calendar.set(Calendar.DAY_OF_MONTH, dd);
	        return calendar;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"PgiUtil", e);
			return null;
		}
	}
	
	/**
	 * Convertit une date du format yyyy-MM-dd en Calendar
	 * Deuxième version pour ne pas mettre le bordel dans le relevé
	 * d'emploi.
	 */
	public static Calendar stringToDateV2(String value)
	{
		//+2012.08.24 ne transfort pas ce qui n'est pas une date 
		if ( value == null )
			return null;
		
		if ( value.length() < 10 )
			return null;
		//-2012.08.24 

		try
		{
			int yyyy = Integer.parseInt(value.substring(0, 4));
			int mm = Integer.parseInt(value.substring(5, 7));
			int dd = Integer.parseInt(value.substring(8));
			
			Calendar calendar = GregorianCalendar.getInstance();
			calendar.set(Calendar.MILLISECOND, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.HOUR, 0);
			calendar.set(Calendar.YEAR, yyyy);
			calendar.set(Calendar.MONDAY, mm-1);
			calendar.set(Calendar.DAY_OF_MONTH, dd);
			calendar.set(Calendar.AM_PM,Calendar.AM);
	        return calendar;
		}
		catch (Exception e)
		{
//			s_log.log(Level.SEVERE,"PgiUtil", e);
			return null;
		}
	}


	public static String convertAsciiString( String s )
	{
	    if ( s == null )
	    	return "";

	    s = s.replaceAll("&aacute;", "á");
	    s = s.replaceAll("&Aacute;", "Á");
	    s = s.replaceAll("&aacute;", "á");
	    s = s.replaceAll("&Agrave;", "À");
	    s = s.replaceAll("&Acirc;", "Â");
	    s = s.replaceAll("&agrave;", "à");
	    s = s.replaceAll("&Acirc;", "Â");
	    s = s.replaceAll("&acirc;", "â");
	    s = s.replaceAll("&Auml;", "Ä");
	    s = s.replaceAll("&auml;", "ä");
	    s = s.replaceAll("&Atilde;", "Ã");
	    s = s.replaceAll("&atilde;", "ã");
	    s = s.replaceAll("&Aring;", "Å");
	    s = s.replaceAll("&aring;", "å");
	    s = s.replaceAll("&Aelig;", "Æ");
	    s = s.replaceAll("&aelig;", "æ");
	    s = s.replaceAll("&Ccedil;", "Ç");
	    s = s.replaceAll("&ccedil;", "ç");
	    s = s.replaceAll("&Eth;", "Ð");
	    s = s.replaceAll("&eth;", "ð");
	    s = s.replaceAll("&Eacute;", "É");
	    s = s.replaceAll("&eacute;", "é");
	    s = s.replaceAll("&Egrave;", "È");
	    s = s.replaceAll("&egrave;", "è");
	    s = s.replaceAll("&Ecirc;", "Ê");
	    s = s.replaceAll("&ecirc;", "ê");
	    s = s.replaceAll("&Euml;", "Ë");
	    s = s.replaceAll("&euml;", "ë");
	    s = s.replaceAll("&Iacute;", "Í");
	    s = s.replaceAll("&iacute;", "í");
	    s = s.replaceAll("&Igrave;", "Ì");
	    s = s.replaceAll("&igrave;", "ì");
	    s = s.replaceAll("&Icirc;", "Î");
	    s = s.replaceAll("&icirc;", "î");
	    s = s.replaceAll("&Iuml;", "Ï");
	    s = s.replaceAll("&iuml;", "ï");
	    s = s.replaceAll("&Ntilde;", "Ñ");
	    s = s.replaceAll("&ntilde;", "ñ");
	    s = s.replaceAll("&Oacute;", "Ó");
	    s = s.replaceAll("&oacute;", "ó");
	    s = s.replaceAll("&Ograve;", "Ò");
	    s = s.replaceAll("&ograve;", "ò");
	    s = s.replaceAll("&Ocirc;", "Ô");
	    s = s.replaceAll("&ocirc;", "ô");
	    s = s.replaceAll("&Ouml;", "Ö");
	    s = s.replaceAll("&ouml;", "ö");
	    s = s.replaceAll("&Otilde;", "Õ");
	    s = s.replaceAll("&otilde;", "õ");
	    s = s.replaceAll("&Oslash;", "Ø");
	    s = s.replaceAll("&oslash;", "ø");
	    s = s.replaceAll("&szlig;", "ß");
	    s = s.replaceAll("&Thorn;", "Þ");
	    s = s.replaceAll("&thorn;", "þ");
	    s = s.replaceAll("&Uacute;", "Ú");
	    s = s.replaceAll("&uacute;", "ú");
	    s = s.replaceAll("&Ugrave;", "Ù");
	    s = s.replaceAll("&ugrave;", "ù");
	    s = s.replaceAll("&Ucirc;", "Û");
	    s = s.replaceAll("&ucirc;", "û");
	    s = s.replaceAll("&Uuml;", "Ü");
	    s = s.replaceAll("&uuml;", "ü");
	    s = s.replaceAll("&Yacute;", "Ý");
	    s = s.replaceAll("&yacute;", "ý");
	    s = s.replaceAll("&yuml;", "ÿ");
	    s = s.replaceAll("&copy;", "©");
	    s = s.replaceAll("&reg;", "®");
	    s = s.replaceAll("&trade;", "™");
	    s = s.replaceAll("&amp;", "&");
	    s = s.replaceAll("&euro;", "€");
	    s = s.replaceAll("&cent;", "¢" );
	    s = s.replaceAll("&pound;", "£" );
	    s = s.replaceAll("&lsquo;", "‘" );
	    s = s.replaceAll("&quot;", "\"");
	    s = s.replaceAll("&rsquo;", "’" );
	    s = s.replaceAll("&ldquo;", "“" );
	    s = s.replaceAll("&rdquo;", "”" );
	    s = s.replaceAll("&laquo;", "«" );
	    s = s.replaceAll("&raquo;", "»" );
	    s = s.replaceAll("&mdash;", "—" );
	    s = s.replaceAll("&ndash;", "–" );
	    s = s.replaceAll("&deg;", "°");
	    s = s.replaceAll("&plusmn;", "±" );
	    s = s.replaceAll("&frac14;", "¼" );
	    s = s.replaceAll("&frac12;", "½" );
	    s = s.replaceAll("&frac34;", "¾" );
	    s = s.replaceAll("&times;", "×" );
	    s = s.replaceAll("&divide;", "÷" );
	    s = s.replaceAll("&#37;", "%");
//	    s = s.replaceAll("&#36;", "$");
	    s = s.replaceAll("&#35;", "#");
	    s = s.replaceAll("&#34;", "\"");
	    s = s.replaceAll("&#33;", "!");
	    s = s.replaceAll("&lt;" , "<");
	    s = s.replaceAll("&gt;" , ">");
	    s = s.replaceAll("&eac" , "");
	    
	    return s;
	}


	public static String convertHTMLString( String s )
	{
	    String output = "";

	    if ( s == null )
	    	return "";

	    for (int i = 0; i < s.length(); i++) {
	      output += removeAccent(s.charAt(i));
	    }
		return output;
	}

    private static String removeAccent(char c) 
    {
	    if (c == 'á') return "&aacute;";
        if (c == 'Á') return "&Aacute;";
        if (c == 'á') return "&aacute;";
        if (c == 'À') return "&Agrave;";
        if (c == 'Â') return "&Acirc;";
        if (c == 'à') return "&agrave;";
        if (c == 'Â') return "&Acirc;";
        if (c == 'â') return "&acirc;";
        if (c == 'Ä') return "&Auml;";
        if (c == 'ä') return "&auml;";
        if (c == 'Ã') return "&Atilde;";
        if (c == 'ã') return "&atilde;";
        if (c == 'Å') return "&Aring;";
        if (c == 'å') return "&aring;";
        if (c == 'Æ') return "&Aelig;";
        if (c == 'æ') return "&aelig;";
        if (c == 'Ç') return "&Ccedil;";
        if (c == 'ç') return "&ccedil;";
        if (c == 'Ð') return "&Eth;";
        if (c == 'ð') return "&eth;";
        if (c == 'É') return "&Eacute;";
        if (c == 'é') return "&eacute;";
        if (c == 'È') return "&Egrave;";
        if (c == 'è') return "&egrave;";
        if (c == 'Ê') return "&Ecirc;";
        if (c == 'ê') return "&ecirc;";
        if (c == 'Ë') return "&Euml;";
        if (c == 'ë') return "&euml;";
        if (c == 'Í') return "&Iacute;";
        if (c == 'í') return "&iacute;";
        if (c == 'Ì') return "&Igrave;";
        if (c == 'ì') return "&igrave;";
        if (c == 'Î') return "&Icirc;";
        if (c == 'î') return "&icirc;";
        if (c == 'Ï') return "&Iuml;";
        if (c == 'ï') return "&iuml;";
        if (c == 'Ñ') return "&Ntilde;";
        if (c == 'ñ') return "&ntilde;";
        if (c == 'Ó') return "&Oacute;";
        if (c == 'ó') return "&oacute;";
        if (c == 'Ò') return "&Ograve;";
        if (c == 'ò') return "&ograve;";
        if (c == 'Ô') return "&Ocirc;";
        if (c == 'ô') return "&ocirc;";
        if (c == 'Ö') return "&Ouml;";
        if (c == 'ö') return "&ouml;";
        if (c == 'Õ') return "&Otilde;";
        if (c == 'õ') return "&otilde;";
        if (c == 'Ø') return "&Oslash;";
        if (c == 'ø') return "&oslash;";
        if (c == 'ß') return "&szlig;";
        if (c == 'Þ') return "&Thorn;";
        if (c == 'þ') return "&thorn;";
        if (c == 'Ú') return "&Uacute;";
        if (c == 'ú') return "&uacute;";
        if (c == 'Ù') return "&Ugrave;";
        if (c == 'ù') return "&ugrave;";
        if (c == 'Û') return "&Ucirc;";
        if (c == 'û') return "&ucirc;";
        if (c == 'Ü') return "&Uuml;";
        if (c == 'ü') return "&uuml;";
        if (c == 'Ý') return "&Yacute;";
        if (c == 'ý') return "&yacute;";
        if (c == 'ÿ') return "&yuml;";
        if (c == '©') return "&copy;";
        if (c == '®') return "&reg;";
        if (c == '™') return "&trade;";
        if (c == '&') return "&amp;";
        if (c == '<') return "&lt;";
        if (c == '>') return "&gt;";
        if (c == '€') return "&euro;";
        if (c == '¢') return "&cent;"; 
        if (c == '£') return "&pound;"; 
        if (c == '"') return "&quot;";
        if (c == '‘') return "&lsquo;"; 
        if (c == '’') return "&rsquo;"; 
        if (c == '“') return "&ldquo;"; 
        if (c == '”') return "&rdquo;"; 
        if (c == '«') return "&laquo;"; 
        if (c == '»') return "&raquo;"; 
        if (c == '—') return "&mdash;"; 
        if (c == '–') return "&ndash;"; 
        if (c == '°') return "&deg;";
        if (c == '±') return "&plusmn;"; 
        if (c == '¼') return "&frac14;"; 
        if (c == '½') return "&frac12;"; 
        if (c == '¾') return "&frac34;"; 
        if (c == '×') return "&times;"; 
        if (c == '÷') return "&divide;"; 
        if (c == '%') return "&#37;";
        if (c == '$') return "&#36;";
        if (c == '#') return "&#35;";
        if (c == '"') return "&#34;";
        if (c == '!') return "&#33;";
        if (c == '\'') return "&#39;";
        
/*        		if (c == 'á') return "&aacute;";
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
		    if (c == '€') return "&euro;";
		    if (c == '¢') return "&cent;";
		    if (c == '$') return "&#36;";
*/		    
		    return "" + c;
		  }    

    public static String getPhoneArea( String phone)
    {
    	// format xxx-xxxx
    	if ( phone.length() <= 8)
        	return "000";

    	// format (xxx)xxx-xxxx

    	if ( phone.startsWith("("))
    		return phone.substring(1,4) ;                     

    	// format xxx-xxx-xxxx
    	if ( phone.length() >= 12)
    		return phone.substring(0,3);                     

    	return phone;
    }

    public static String getPhoneNoArea( String phone)
    {
    	if ( phone == null )
    		return "";
    				
    	// format xxx-xxxx
    	if ( phone.length() <= 8)
        	return phone;

    	// format (xxx)xxx-xxxx

    	if ( phone.startsWith("("))
    		return phone.substring(5, 13);                     

    	// format xxx-xxx-xxxx
    	if ( phone.length() >= 12)
    		return phone.substring(4, 12);                     

    	return phone;
    }
    
    
	public static BigDecimal nvl ( BigDecimal amount )
	{
	    if ( amount == null )
	        return Env.ZERO;
	    
	    return amount;
	}

	public static String nvl ( String string )
	{
	    if ( string == null )
	        return "";
	    
	    return string;
	}

	public static String nvl ( String string , String ret)
	{
	    if ( string == null )
	        return ret;
	    
	    return string;
	}

	public static Object resizeArray (Object oldArray, int newSize) 
	{
	   int oldSize = java.lang.reflect.Array.getLength(oldArray);
	   Class<?> elementType = oldArray.getClass().getComponentType();
	   Object newArray = java.lang.reflect.Array.newInstance(
		         elementType,newSize);
	   int preserveLength = Math.min(oldSize,newSize);
	   if (preserveLength > 0)
	      System.arraycopy (oldArray,0,newArray,0,preserveLength);
	   return newArray; }

	
	//+ Progestion
	public static String hashPassword( String password ) {
	    String ret = null;
	    try {
		    MessageDigest md = MessageDigest.getInstance("SHA1");
	        md.update("Sels4l+5aLz5413".getBytes()); // Salt :)
	        md.update(password.getBytes());
	        md.update("Poivrep3Pp3RPfeFf3rp1m3N+4".getBytes());
	        byte[] hash = md.digest();
		    if( hash == null || hash.length == 0 ) return null;

		    final char val[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

		    StringBuffer out = new StringBuffer(hash.length * 2);

		    for( int i=0; i<hash.length; i++ ) {
		        out.append(val[((hash[i] & 0xF0) >>> 4) & 0x0F]); 
		        out.append(val[hash[i] & 0x0F]); 
		    }
		    ret = out.toString();
	    }
	    catch( Exception e ) {
	        s_log.log(Level.SEVERE, "hashPassword", e );
	        ret = password;
	    }
	    return ret;
	}


	public static void CheckAtlasLinkedServer()
	{

		//+ Solstice Progestion syncronise with Pandora 2012.04.03
		if ( getSolsticeParameter(Env.getCtx(), "SyncroniseWithAtlas").equals("True")  )
		{
			
			if ( getSolsticeParameter(Env.getCtx(), "SyncroniseWithAtlasFail").equals("True")  )
			{
				return;
			}

			String sql = "SELECT * FROM openquery( HORODATEUR, 'select state from master.sys.databases where name = ''sat0100'' and state = 0' )";

			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
					s_log.log(Level.INFO,"Atlas Linked Server ok");
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (SQLException e)
			{
				DB.executeUpdate("UPDATE P_Solstice_Parameters set Parameter = 'True' where value = 'SyncroniseWithAtlasFail'", null);
	
				s_log.log(Level.SEVERE,"Atlas Linked Server not found", e);
			}
		}
	}

	public static BigDecimal getPandora_QuantityVitual ( int Pandora_CategoriesAtlasMaitre_ID,  String day)
	{
		String sql = "Select " + day + " from [dbo].[Pandora_CategoriesAtlasMaitre]  where Pandora_CategoriesAtlasMaitre.Pandora_CategoriesAtlasMaitre_ID = " + Pandora_CategoriesAtlasMaitre_ID 
     		;
	
		PreparedStatement pstmt = null;
	
		BigDecimal Quantity = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				Quantity = rs.getBigDecimal(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
			
		return Quantity;
		
	}
	
	//+2012.12.12 Date du permier cycle ouvert dans pandora
	public static Timestamp getCycle_Begin_Date()
	{
		if ( PgiUtil.getSolsticeParameter(Env.getCtx(), "SyncroniseWithPandora").equals("False") && PgiUtil.getSolsticeParameter(Env.getCtx(), "SyncroniseWithAtlas").equals("False"))
			return null;

		String sql = "select MIN( Cycle_Begin_Date ) from dbo.semaine where Fermer <> 1";	

		if ( PgiUtil.getSolsticeParameter(Env.getCtx(), "SyncroniseWithAtlas").toLowerCase().equals("true") ) 
				sql = "select MIN( Cycle_Begin_Date )  from horodateur.sat0100.dbo.semaine where Fermer <> 1 ";
		
		PreparedStatement pstmt = null;
		
		Timestamp startDate = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				startDate = rs.getTimestamp(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
			
		return startDate;

	
	}
	

	public static void deleteOldReportFile(final int daysBack, final String dirWay) 
	{

		final File directory = new File(dirWay);
		if(directory.exists())
		{
		  System.out.println(" Directory Exists");
		  final File[] listFiles = directory.listFiles();          
		  final long purgeTime = System.currentTimeMillis() - (daysBack * 24 * 60 * 60 * 1000);
		  System.out.println("System.currentTimeMillis " + System.currentTimeMillis());
		  System.out.println("purgeTime " + purgeTime);

		  for(File listFile : listFiles) {
		      System.out.println("Length : "+ listFiles.length);
		      System.out.println("listFile.getName() : " +listFile.getName());
		      System.out.println("listFile.lastModified() :"+listFile.lastModified());
		      if(listFile.lastModified() < purgeTime) 
		      {
		        if(!listFile.delete()) {
		        	s_log.log(Level.SEVERE, "Unable to delete file: " + listFile);
		        }
		         System.out.println("Inside File Delete");
		      }
		  }
		} 
		else 
		{
        	s_log.log(Level.SEVERE, "directory not found : " + dirWay.toString());
		    
		}
	}

	
	
	public static String validateWithXsd( String fileName, String schemaFileName  )
	{
		 Source schemaFile = new StreamSource(new File( schemaFileName));
	     Source xmlFile = new StreamSource(new File( fileName ));

	     try{

		     SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		     Schema schema = schemaFactory.newSchema(schemaFile);
		     Validator validator = schema.newValidator();

	         validator.validate(xmlFile);
	         s_log.log(Level.INFO, xmlFile.getSystemId() + " is valid");
	         return xmlFile.getSystemId() + " est valide" ;
	     }
	     catch (Exception e) 
	     {
	    	 s_log.log(Level.SEVERE, xmlFile.getSystemId() + " is NOT valid");
	         s_log.log(Level.SEVERE,"Reason: " + e.getLocalizedMessage());

	         return xmlFile.getSystemId() + " est non valide : raison " + e.getLocalizedMessage() ;

	     }
		
	}

	
	//- Progestion
	
	public static void setSolsticeParameter (Properties ctx, String ParameterName, String value )
	{
  	    DB.executeUpdate("UPDATE P_Solstice_Parameters set Parameter = '" + value + "' where value = '" + ParameterName + "'", null);
		if (ParameterName != null) {
			if (value != null)
				s_paramCache.put(ParameterName, value);
			else
				s_paramCache.remove(ParameterName);
		}
	}


	/**
	 * Demande confirmation avant d'ouvrir un fichier exporté
	 * @param fullFileName chemin complet du fichier
	 */
	public static void promptOpenExportedFile(String fullFileName)
	{
		if (java.awt.GraphicsEnvironment.isHeadless())
			return;
		int choice = JOptionPane.showConfirmDialog(
			null,
			"Fichier généré avec succès :\n" + fullFileName + "\n\nVoulez-vous l'ouvrir maintenant ?",
			"Exportation Excel terminée",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE
		);
		if (choice == JOptionPane.YES_OPTION)
		{
			try
			{
				java.io.File file = new java.io.File(fullFileName);
				if (java.awt.Desktop.isDesktopSupported() && java.awt.Desktop.getDesktop().isSupported(java.awt.Desktop.Action.OPEN))
					java.awt.Desktop.getDesktop().open(file);
				else
					Env.startBrowser(fullFileName);
			}
			catch (Exception ex)
			{
				Env.startBrowser(fullFileName);
			}
		}
	}
}