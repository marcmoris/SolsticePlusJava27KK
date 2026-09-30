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
package org.compiere.cm.cache;

import java.util.*;

import org.compiere.model.*;

/**
 * 	Wiki Token Cache
 *	@author Yves Sandfort
 *	@version $Id: WikiToken.java,v 1.2 2007/07/19 21:49:52 marmor01 Exp $
 */
public class WikiToken extends CO
{
	/**
	 * 	init Globals
	 *  @param ctx 
	 */
	public void init(Properties ctx)
	{
		setCtx(ctx);
		if (cache.isEmpty ()) {
			MWikiToken[] theseTokens = MWikiToken.getAllForPreload(ctx, null);
			for (int i=0; i<theseTokens.length; i++) 
			{
				String seq = "" + i;
				while (seq.length ()<(theseTokens.length))
					seq = "0" + seq;
				String AD_Client = "" + theseTokens[i].getAD_Client_ID ();
				while (AD_Client.length ()<7)
					AD_Client = "0" + AD_Client;
				cache.put (AD_Client + "-" + seq, theseTokens[i]);
			}
		}
	}	//	initGlobals
	
	public MWikiToken get(String thisID, boolean reload)  
	{
		MWikiToken thisToken = null;
		if (cache.containsKey (thisID))
			thisToken = (MWikiToken) cache.get (thisID);
		else 
			thisToken = null;
		if (reload)
		{
			thisToken.load (null);
			cache.put (thisID, thisToken);
		}
		return thisToken;
	}
}	//	WikiToken
