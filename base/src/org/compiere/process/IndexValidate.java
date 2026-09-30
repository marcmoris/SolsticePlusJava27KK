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
package org.compiere.process;

import java.sql.*;
import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	Validate Index
 *	
 *  @author Jorg Janke
 *  @version $Id: IndexValidate.java,v 1.1 2007/07/18 14:39:10 marmor01 Exp $
 */
public class IndexValidate extends SvrProcess
{
	private int		p_AD_TableIndex_ID = 0; 
	/**
	 * 	Prepare
	 */
	protected void prepare()
	{
		p_AD_TableIndex_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return summary
	 *	@throws Exception error
	 */
	protected String doIt() throws Exception
	{
		MTableIndex index = new MTableIndex (getCtx(), p_AD_TableIndex_ID, get_TrxName());
		log.info (index.toString ());
		
		DatabaseMetaData md = DB.getConnectionRO().getMetaData();
		//
		String tableName = index.getTableName();
		if (md.storesUpperCaseIdentifiers())
			tableName = tableName.toUpperCase();
		else if (md.storesLowerCaseIdentifiers())
			tableName = tableName.toLowerCase();
		String catalog = "REFERENCE";
		String schema = null;
		
		//
		boolean found = false;
		StringBuffer sqlDB = new StringBuffer();
		ResultSet rs = md.getIndexInfo (catalog, schema, 
			tableName, index.isUnique(), true);
		while (rs.next())
		{
			String dbIndexName = rs.getString ("INDEX_NAME");
			if (dbIndexName.equalsIgnoreCase (index.getName()))
			{
				found = true;
				
			}
		}
		
		if (!found)
		{
			rs = md.getIndexInfo (catalog, schema, 
				tableName, !index.isUnique(), true);
		}
		
		return "NIY";
	}	//	doIt

}
