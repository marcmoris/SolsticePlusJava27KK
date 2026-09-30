/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.
 * This program is free software; you can redistribute it and/or modify it
 * under the terms version 2 of the GNU General Public License as published
 * by the Free Software Foundation. This program is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 * You may reach us at: ComPiere, Inc. - http://www.compiere.org/license.html
 * 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA or info@compiere.org 
 *****************************************************************************/
package org.compiere.cm.extend;

import javax.servlet.http.*;
import org.compiere.model.*;
import java.util.*;

/**
 *	Search the index for results
 *	
 *  @author Yves Sandfort
 *  @version $Id: indexSearch.java,v 1.2 2007/07/19 21:49:51 marmor01 Exp $
 */
public class indexSearch extends org.compiere.cm.Extend
{
	public indexSearch (HttpServletRequest request, Properties ctx)
	{
		super (request, ctx);
	}
	
	public boolean doIt()
	{
		if (e_request.getParameter ("query")!=null) {
			appendXML("<searchResults>\n");
			appendXML("<query><![CDATA[" + e_request.getParameter ("query") + "]]></query>\n");
			MIndex [] searchResults = MIndex.getResults (e_request.getParameter("query"), getCtx(), null);
			if (searchResults!=null && searchResults.length>0) {
				appendXML("<results>" + searchResults.length + "</results>\n");
			} else {
				appendXML("<results>0</results>\n");
			}
			for (int i=0;i<searchResults.length;i++) 
			{
				appendXML(searchResults[i].get_xmlString (new StringBuffer("")));
			}
			appendXML("</searchResults>\n");
		}
		return true;
	}
}
