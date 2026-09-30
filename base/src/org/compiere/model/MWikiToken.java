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
package org.compiere.model;

import java.sql.*;
import java.util.*;
import java.util.regex.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 * 	Container Stage Model
 *	
 *  @author Yves Sandfort
 *  @version $Id: MWikiToken.java,v 1.1 2007/07/18 14:39:41 marmor01 Exp $
 */
public class MWikiToken extends X_CM_WikiToken
{
	/**	serialVersionUID	*/
	private static final long serialVersionUID = 4980395873969221189L;

	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param CM_WikiToken_ID id
	 *	@param trxName tansaction
	 */
	public MWikiToken (Properties ctx, int CM_WikiToken_ID, String trxName)
	{
		super (ctx, CM_WikiToken_ID, trxName);
	}	//	MWikiToken

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MWikiToken (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MWikiToken
	
	/** Logger								*/
	private static CLogger		s_log = CLogger.getCLogger (MWikiToken.class);
	/** Pattern								*/
	private Pattern pattern = null;
	
	/**
	 * 	getPattern
	 *	@return returns preCompiled RegEx Pattern
	 */
	public Pattern getPattern()
	{
		if (pattern!=null)
			return pattern;
		pattern = Pattern.compile (getName());
		return pattern;
	}
	
	/**
	 *  (re)Load record with m_ID[*]
	 *  @param trxName transaction
	 *  @return true if loaded
	 */
	public boolean load (String trxName)
	{
		pattern = null;
		return super.load (trxName);
	}

	
	/**
	 * 	get all Wiki Tokens on system level (i.e. to preload cache)
	 *  @param ctx 
	 *  @param trxName 
	 *	@return Array of previous DunningLevels
	 */
	public static MWikiToken[] getAllForPreload(Properties ctx, String trxName) 
	{
		ArrayList<MWikiToken> list = new ArrayList<MWikiToken>();
		String sql = "SELECT * FROM CM_WikiToken WHERE Ad_Client_ID=0 AND isActive='Y' ORDER By SeqNo";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trxName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MWikiToken(ctx, rs, trxName));
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}

		MWikiToken[] retValue = new MWikiToken[list.size()];
		list.toArray(retValue);
		return retValue;
	}
	
	/**
	 * 	process any token to a StringBuffer, the different types need to handle the results different.
	 *	@param source
	 *	@param CM_WebProject_ID
	 *	@return converted StringBuffer
	 */
	public StringBuffer processToken(StringBuffer source, int CM_WebProject_ID, String MediaPath)
	{
		if (getTokenType ().equals (X_CM_WikiToken.TOKENTYPE_Style)) {
			Matcher matcher = getPattern ().matcher (source);
			source = new StringBuffer(matcher.replaceAll (getMacro ()));
		} else if (getTokenType ().equals (X_CM_WikiToken.TOKENTYPE_SQLCommand)) {
			
		} else if (getTokenType ().equals (X_CM_WikiToken.TOKENTYPE_ExternalLink)) {
			Matcher matcher = getPattern ().matcher (source);
			source = new StringBuffer(matcher.replaceAll (getMacro ()));
		} else if (getTokenType ().equals (X_CM_WikiToken.TOKENTYPE_InternalLink)) {
			Matcher matcher = getPattern ().matcher (source);
			while(matcher.find ()) {
				if (matcher.group(1).equals ("Media:")) 
				{
					String Name = matcher.group (2);
					MMedia thisMedia = MMedia.getByName (getCtx(), Name, CM_WebProject_ID, null);
					String replaceString = "";
					if (thisMedia != null)
					{
						if (matcher.groupCount ()>2)
							replaceString = "<img src=\"" + MediaPath + thisMedia.getFileName () + "\" alt=\"" + matcher.group (3) + "\"/>";
					}
					source = new StringBuffer(matcher.replaceFirst (replaceString));
					matcher = getPattern ().matcher (source);
				} else {
					String link = matcher.group (1);
					MContainer thisContainer = MContainer.getByName (getCtx(), link, CM_WebProject_ID, null);
					if (thisContainer==null) 
						thisContainer = MContainer.getByTitle(getCtx(), link, CM_WebProject_ID, null);
					String replaceURL = "/index.html";
					if (thisContainer != null)
					{
						if (matcher.groupCount ()>1)
							replaceURL = "<a href=\"" + thisContainer.getRelativeURL () + "\">" + matcher.group (2) + "</a>";
						else
							replaceURL = "<a href=\"" + thisContainer.getRelativeURL () + "\">" + matcher.group (1) + "</a>";
					}
					source = new StringBuffer(matcher.replaceFirst (replaceURL));
					matcher = getPattern ().matcher (source);
				}
			}
			
		} 
		return source;
	}
}	//	MWikiToken
