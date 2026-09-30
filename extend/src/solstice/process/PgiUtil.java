/*
 * Created on 2005-02-01
 *
 * Different class from PGI
 */
package solstice.process;

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
import java.sql.Timestamp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.mail.internet.InternetAddress;

import org.compiere.util.DB;

import solstice.model.P_System_Parameters;
import org.compiere.model.MUser;

import javax.swing.JOptionPane;

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
		String url = null;
		try
		{
			url = (String)P_System_Parameters.getParameterValue("WEBAPPURL");
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"PgiUtil getWebServer ", e);
		}	
		return url;
		
	/*	
		String sql2 = "";
		if ( ctx == null || Env.getAD_Client_ID(ctx) == 0 )
			sql2 = "SELECT WebParam6, WebParam4 FROM AD_Client WHERE AD_Client_ID=11";
		else
			sql2 = "SELECT WebParam6, WebParam4 FROM AD_Client WHERE AD_Client_ID="+String.valueOf(Env.getAD_Client_ID(ctx));
		PreparedStatement pstmt2 = null;
		String urlBegin = "";
		try
		{
			pstmt2 = DB.prepareStatement(sql2);
			ResultSet rs = pstmt2.executeQuery();
			if(rs.next())
			{
				if ( module.compareTo( "pay" ) == 0 )
					urlBegin = rs.getString("WebParam4").trim() ;
				else
					urlBegin = rs.getString("WebParam6").trim() ;
			}
			rs.close();
			pstmt2.close();
			pstmt2 = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"PgiUtil", e);

//			log.error("doIt", e);
		}	

//		System.out.println( "GetWebServer : AD_Client_ID : " + Env.getAD_Client_ID( ctx ) );
//		X_AD_Client client = new X_AD_Client( ctx, Env.getAD_Client_ID( ctx ));
//		return Env.getContext(ctx, "webParam6" );
//		return Env.getContext(ctx, client.getWebParam6() );
		return urlBegin;
	*/
	}
	
	
	public static String getWebServerLogin (Properties ctx, String user )
	{
		String url = null;
	    String sql = "select top 1 WEBAPPURL from P_System_Parameters";
        PreparedStatement stmt = DB.prepareStatement(sql, null);
		try
		{
		    ResultSet rs = stmt.executeQuery();
		    if(rs.next())
		    {
		    	// Env.getContext(Env.getCtx(), "#AD_User_Name")
		        url = rs.getString(1) + "login.jsp?user=" + user + "&redirection=%2F";
		    }
		    rs.close();
		    stmt.close();
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"PgiUtil", e);
			return null;
		}
		
	    return url;
	}

	
	public static String getSolsticeParameter (Properties ctx, String ParameterName )
	{
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
	    Vector v = new Vector();
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
			s_log.log(Level.SEVERE,"PgiUtil", e);
			return null;
		}
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