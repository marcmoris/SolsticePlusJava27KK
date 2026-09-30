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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.Properties;

import org.compiere.Compiere;
import org.compiere.model.MTable;
import org.compiere.util.CLogMgt;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

public class CreateSolsticeTable {

	private Properties		m_ctx = new Properties();
	boolean dropFirst = false;

	/**	Logger	*/
	private static CLogger	log	= CLogger.getCLogger (CreateSolsticeTable.class);
	
	public CreateSolsticeTable()
	{
		String sql = "SELECT * FROM AD_Table WHERE  isactive = 'Y' "; // EntityType = 'EXT' AND
		
//		sql += " and not exists ( select 1 from user_tables where table_name = upper(tablename) ) ";

		sql += " ORDER BY TableName";
		
		String cmd = null;
		//
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next() )
			{
				MTable table = new MTable (m_ctx, rs, null);
				if (table.isView())
					continue;

				if (dropFirst)
				{
					cmd = "DROP TABLE " + table.getTableName();
					int no = DB.executeUpdate(cmd, null);
				}
				
				cmd = table.getSQLCreate(false);
				
				cmd = cmd.replace ( "CREATE TABLE ", "CREATE TABLE Release33.dbo.");
				log.log(Level.INFO, cmd);
				
				int no = DB.executeUpdate(cmd, null);
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
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
		
	}

	
	
	/**************************************************************************
	 * 	Create DB
	 *	@param args
	 */
	public static void main (String[] args)
	{
//		Compiere.startup(true, false, "CreateCompiere");
		org.compiere.Compiere.startupEnvironment(true);
		
		CLogMgt.setLevel(Level.FINE);
		CLogMgt.setLoggerLevel(Level.FINE,null);

		CreateSolsticeTable cc = new CreateSolsticeTable ();
	}	//	main
	

}
