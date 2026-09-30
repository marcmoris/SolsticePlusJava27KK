/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.server;

import java.sql.*;
import java.util.logging.*;
import org.compiere.*;
import org.compiere.model.*;
import org.compiere.util.*;

import solstice.utils.PgiUtil;


/**
 *	Alert Processor
 *	
 *  @author Jorg Janke
 *  @version $Id: AlertProcessor.java,v 1.1 2007/07/18 14:44:06 marmor01 Exp $
 */
public class AlertProcessor extends CompiereServer
{
	/**
	 * 	Alert Processor
	 *	@param model model
	 */
	public AlertProcessor (MAlertProcessor model)
	{
		super (model, 180);		//	3 monute delay 
		m_model = model;
		m_client = MClient.get(model.getCtx(), model.getAD_Client_ID());
	}	//	AlertProcessor

	/**	The Concrete Model			*/
	private MAlertProcessor		m_model = null;
	/**	Last Summary				*/
	private StringBuffer 		m_summary = new StringBuffer();
	/**	Last Error Msg				*/
	private StringBuffer 		m_errors = new StringBuffer();
	/** Client onfo					*/
	private MClient 			m_client = null;

	/**
	 * 	Work
	 */
	protected void doWork ()
	{
		m_summary = new StringBuffer();
		m_errors = new StringBuffer();
		//
		int count = 0;
		int countError = 0;
		MAlert[] alerts = m_model.getAlerts(false);
		for (int i = 0; i < alerts.length; i++)
		{
			if (!processAlert(alerts[i]))
				countError++;
			count++;
		}
		//
		String summary = "Total=" + count;
		if (countError > 0)
			summary += ", Not processed=" + countError;
		summary += " - ";
		m_summary.insert(0, summary);
		//
		int no = m_model.deleteLog();
		m_summary.append("Logs deleted=").append(no);
		//
		MAlertProcessorLog pLog = new MAlertProcessorLog(m_model, m_summary.toString());
		pLog.setReference("#" + String.valueOf(p_runCount) 
			+ " - " + TimeUtil.formatElapsed(new Timestamp(p_startWork)));
		pLog.setTextMsg(m_errors.toString());
		pLog.save();
	}	//	doWork

	/**
	 * 	Process Alert
	 *	@param alert alert
	 *	@return true if processed
	 */
	private boolean processAlert (MAlert alert)
	{
		if (!alert.isValid())
			return false;
		log.info("" + alert);

		StringBuffer message = new StringBuffer(alert.getAlertMessage())
			.append(Env.NL);
		//
		boolean valid = true;
		boolean processed = false;
		MAlertRule[] rules = alert.getRules(false);
		for (int i = 0; i < rules.length; i++)
		{
			if (i > 0)
				message.append(Env.NL).append("================================").append(Env.NL);
			String trxName = null;		//	assume r/o
			
			MAlertRule rule = rules[i];
			if (!rule.isValid())
				continue;
			log.fine("" + rule);
			
			//	Pre
			String sql = rule.getPreProcessing();
			if (sql != null && sql.length() > 0)
			{
				int no = DB.executeUpdate(sql, false, trxName);
				if (no == -1)
				{
					ValueNamePair error = CLogger.retrieveError();
					rule.setErrorMsg("Pre=" + error.getName());
					m_errors.append("Pre=" + error.getName());
					rule.setIsValid(false);
					rule.save();
					valid = false;
					break;
				}
			}	//	Pre
			
			//	The processing
			sql = rule.getSql();
			if (alert.isEnforceRoleSecurity()
				|| alert.isEnforceClientSecurity())
			{
				int AD_Role_ID = alert.getFirstAD_Role_ID();
				if (AD_Role_ID == -1)
					AD_Role_ID = alert.getFirstUserAD_Role_ID();
				if (AD_Role_ID != -1)
				{
					MRole role = MRole.get(getCtx(), AD_Role_ID);
					sql = role.addAccessSQL(sql, null, true, false);
				}
			}
			
			try
			{
				String text = listSqlSelect(sql, trxName);
				if (text != null && text.length() > 0)
				{
					message.append(text);
					processed = true;
				}
				//2013-08-05
				else
				{
					processed = false;
				}
			}
			catch (Exception e)
			{
				rule.setErrorMsg("Select=" + e.getLocalizedMessage());
				m_errors.append("Select=" + e.getLocalizedMessage());
				rule.setIsValid(false);
				rule.save();
				valid = false;
				break;
			}

			//	Post
			sql = rule.getPostProcessing();
			if (sql != null && sql.length() > 0)
			{
				int no = DB.executeUpdate(sql, false, trxName);
				if (no == -1)
				{
					ValueNamePair error = CLogger.retrieveError();
					rule.setErrorMsg("Post=" + error.getName());
					m_errors.append("Post=" + error.getName());
					rule.setIsValid(false);
					rule.save();
					valid = false;
					break;
				}
			}	//	Post
			
			/**	Trx				*/
			if (trxName != null)
			{
				Trx trx = Trx.get(trxName, false);
				if (trx != null)
				{
					trx.commit();
					trx.close();
				}
			}
		}	//	 for all rules
		
		//	Update header if error
		if (!valid)
		{
			alert.setIsValid(false);
			alert.save();
			return false;
		}
		
		//	Nothing to report
		if (!processed)
		{
			m_summary.append(alert.getName()).append("=No Result - ");

			return true;
		}
		
		//	Send Message
		int countMail = 0;
		MAlertRecipient[] recipients = alert.getRecipients(false);
		for (int i = 0; i < recipients.length; i++)
		{
			MAlertRecipient recipient = recipients[i];
			if (recipient.getAD_User_ID() >= 0)		//	System == 0
//PROGESTION Solstice 2013.03.21 Change to html email				
//				if (m_client.sendEMail(recipient.getAD_User_ID(), 
				if (m_client.sendEMailHtml(recipient.getAD_User_ID(), 
						convertHTMLString( alert.getAlertSubject() ), message.toString(), null))
					countMail++;
			if (recipient.getAD_Role_ID() >= 0)		//	SystemAdministrator == 0
			{
				MUserRoles[] urs = MUserRoles.getOfRole(getCtx(), recipient.getAD_Role_ID());
				for (int j = 0; j < urs.length; j++)
				{
					MUserRoles ur = urs[j];
					if (!ur.isActive())
						continue;
//PROGESTION Solstice 2013.03.21 Change to html email					
//					if (m_client.sendEMail (ur.getAD_User_ID(), 
					if (m_client.sendEMailHtml (ur.getAD_User_ID(), 
							convertHTMLString( alert.getAlertSubject() ), message.toString(), null))
						countMail++;
				}
			}
		}
		
		m_summary.append(alert.getName()).append(" (EMails=").append(countMail).append(") - ");
		return valid;
	}	//	processAlert
	

	public static boolean isNumeric(String strNum) {
		
		if ( strNum == null ) return false;
		
	    boolean ret = true;
	    try {

	        Double.parseDouble(strNum);

	    }catch (NumberFormatException e) {
	        ret = false;
	    }
	    return ret;
	}	
	
	/**
	 * 	List Sql Select
	 *	@param sql sql select
	 *	@param trxName transaction
	 *	@return list of rows & values
	 *	@throws Exception
	 */
	private String listSqlSelect (String sql, String trxName) throws Exception
	{
		StringBuffer result = new StringBuffer();
		PreparedStatement pstmt = null;
		Exception error = null;
		boolean isFound = false; 
		
		try
		{
//			log.log(Level.SEVERE, "Alert debug " + sql);

			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			ResultSetMetaData meta = rs.getMetaData();
			
//			result.append( "<table width=\"100%\" align=\"center\" border=\"0\" cellspacing=\"2\" cellpadding=\"0\">");
			while (rs.next ())
			{
				isFound = true;
//2011.03.22 Solstice / Progestion				
				result.append( "<tr>");
//				result.append("------------------").append(Env.NL);
				for (int col = 1; col <= meta.getColumnCount(); col++)
				{
					
					if ( rs.getString(col).contains( ".") && isNumeric( rs.getString(col) ) )
						result.append( "<td align=\"right\">");
					else
						result.append( "<td>");
//					result.append(meta.getColumnLabel(col)).append(" = ");
					result.append(rs.getString(col));
					result.append( "</td>");
//					result.append(Env.NL);
				}	//	for all columns
				result.append( "</tr>");
			}
			result.append( "</table>");
			if (result.length() == 0)
				log.fine("No rows selected");
			
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e) 
		{
			log.log(Level.SEVERE, sql, e);
			error = e;
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		
		//	Error occured
		if (error != null)
			throw new Exception ("(" + sql + ") " + Env.NL 
				+ error.getLocalizedMessage());

		//2013.08.05
		if ( isFound == false )
		{
			return null;
		}

		
		return result.toString();
	}	//	listSqlSelect
	

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

        return "" + c;
	 }    

	
	/**
	 * 	Get Server Info
	 *	@return info
	 */
	public String getServerInfo()
	{
		return "#" + p_runCount + " - Last=" + m_summary.toString();
	}	//	getServerInfo

	
	/***************************************************************************
	 * 	Test
	 *	@param args ignored
	 */
	public static void main (String[] args)
	{
		Compiere.startup(true);
		MAlertProcessor model = new MAlertProcessor (Env.getCtx(), 1000007, null);
		AlertProcessor ap = new AlertProcessor(model);
		ap.start();
		
		
	}	//	main
	
}	//	AlertProcessor
