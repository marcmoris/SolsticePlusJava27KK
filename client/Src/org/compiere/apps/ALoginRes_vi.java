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
package org.compiere.apps;

import java.util.*;

/**
 *  Vietnamese Resource Bundle
 *
 * 	@author 	Bui Chi Trung
 * 	@version 	$Id: ALoginRes_vi.java,v 1.2 2007/07/18 21:09:20 marmor01 Exp $
 */
public final class ALoginRes_vi extends ListResourceBundle
{
	// TODO Run native2ascii to convert everything to plain ASCII !! 
	
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Connection",         "K\u1EBFt n\u1ED1i" },
	{ "Defaults",           "M\u1EB7c nhiên" },
	{ "Login",              "\u0110\u0103ng nh\u1EADp" },
	{ "File",               "H\u1EC7 th\u1ED1ng" },
	{ "Exit",               "Thoát" },
	{ "Help",               "Giúp \u0111\u1EE1" },
	{ "About",              "Gi\u1EDBi thi\u1EC7u" },
	{ "Host",               "Máy ch\u1EE7" },
	{ "Database",           "C\u01A1 s\u1EDF d\u1EEF li\u1EC7u" },
	{ "User",               "Tên ng\u01B0\u1EDDi dùng" },
	{ "EnterUser",          "Hãy nh\u1EADp tên ng\u01B0\u1EDDi dùng" },
	{ "Password",           "M\u1EADt kh\u1EA9u" },
	{ "EnterPassword",      "Hãy nh\u1EADp m\u1EADt kh\u1EA9u" },
	{ "Language",           "Ngôn ng\u1EEF" },
	{ "SelectLanguage",     "Hãy ch\u1ECDn ngôn ng\u1EEF" },
	{ "Role",               "Vai trò" },
	{ "Client",             "Công ty" },
	{ "Organization",       "\u0110\u01A1n v\u1ECB" },
	{ "Date",               "Ngày" },
	{ "Warehouse",          "Kho hàng" },
	{ "Printer",            "Máy in" },
	{ "Connected",          "\u0110ã k\u1EBFt n\u1ED1i" },
	{ "NotConnected",       "Ch\u01B0a k\u1EBFt n\u1ED1i \u0111\u01B0\u1EE3c" },
	{ "DatabaseNotFound",   "Không tìm th\u1EA5y CSDL" },
	{ "UserPwdError",       "Ng\u01B0\u1EDDi dùng và m\u1EADt kh\u1EA9u không kh\u1EDBp nhau" },
	{ "RoleNotFound",       "Không tìm th\u1EA5y vai trò này" },
	{ "Authorized",         "\u0110ã \u0111\u01B0\u1EE3c phép" },
	{ "Ok",                 "\u0110\u1ED3ng ý" },
	{ "Cancel",             "H\u1EE7y" },
	{ "VersionConflict",    "X\u1EA3y ra tranh ch\u1EA5p phiên b\u1EA3n:" },
	{ "VersionInfo",        "Thông tin v\u1EC1 phiên b\u1EA3n" },
	{ "PleaseUpgrade",      "Vui lòng nâng c\u1EA5p ch\u01B0\u01A1ng trình" }
	};

	/**
	 *  Get Contents
	 *  @return context
	 */
	public Object[][] getContents()
	{
		return contents;
	}   //  getContents
}   //  ALoginRes
