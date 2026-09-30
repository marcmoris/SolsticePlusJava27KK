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
package org.compiere.util;

import java.util.ListResourceBundle;

/**
 *  License Dialog Translation
 *
 *  @author     Bui Chi Trung
 *  @version    $Id: IniRes_vi.java,v 1.1 2007/07/18 14:40:59 marmor01 Exp $
 */
public class IniRes_vi extends ListResourceBundle
{
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Compiere_License",   "Gi\u1EA5y phép s\u1EED d\u1EE5ng" },
	{ "Do_you_accept",      "B\u1EA1n có ch\u1EA5p nh\u1EADn gi\u1EA5y phép này không?" },
	{ "No",                 "Không" },
	{ "Yes_I_Understand",   "Vâng, tôi hi\u1EC3u và ch\u1EA5p nh\u1EADn gi\u1EA5p phép này" },
	{ "license_htm",        "org/compiere/license.htm" },
	{ "License_rejected",   "Gi\u1EA5y phép b\u1ECB kh\u01B0\u1EDBc t\u1EEB ho\u1EB7c h\u1EBFt h\u1EA1n" }
	};

	/**
	 *  Get Content
	 *  @return Content
	 */
	public Object[][] getContents()
	{
		return contents;
	}   //  getContent
}   //  IniRes
