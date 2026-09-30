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
package org.compiere.cm.wiki;

import org.compiere.cm.cache.*;

/**
 * @author YS
 * @version $Id: wikiProcessor.java,v 1.2 2007/07/19 21:49:53 marmor01 Exp $
 */
public class wikiProcessor
{
	public static StringBuffer run (String source, int CM_WebProject_ID, String mediaPath, WikiToken wikiCache) throws Exception
	{
		return run (new StringBuffer(source), CM_WebProject_ID, mediaPath, wikiCache);
	}
	
	/**
	 * 	Run
	 *	@param source
	 *  @param wikiCache 
	 *	@return xml
	 *	@throws Exception
	 */
	public static StringBuffer run (StringBuffer source, int CM_WebProject_ID, String mediaPath, WikiToken wikiCache) throws Exception
	{
		if (wikiCache.getSize ()>0) 
		{
			String [] wikiKeys = wikiCache.getSortedKeys ();
			for (int i = 0; i<wikiKeys.length; i++)
				source =  wikiCache.get (wikiKeys[i], true).processToken (source, CM_WebProject_ID, mediaPath);
		}
		return source;
	}
}	//	wikiProcessor
