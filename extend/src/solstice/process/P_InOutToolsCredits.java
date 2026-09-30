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
package solstice.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;


import solstice.model.P_Tools_Credits;
import solstice.model.P_Tools_Credits_Result;


public class P_InOutToolsCredits extends SvrProcess
{
	int m_inserted;
	int Record_ID;

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_InOutToolsCredits.class);
	private P_Tools_Credits ToolsCredits;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		Record_ID = this.getRecord_ID();

	}	//	prepare

	private int deleteOldData()
	{
		String sql = " Delete from P_Tools_Credits_Result Where P_Tools_Credits_ID = " + ToolsCredits.getP_Tools_Credits_ID();	
		int no = DB.executeUpdate(sql, get_TrxName());
		return no;
	}
	
	/**
	 * 	Read each time sheet 
	 *	@return 
	 *	@throws Exception
	 */
	
	protected String doIt () throws Exception
	{
		ToolsCredits = new P_Tools_Credits( getCtx(), Record_ID, null );
		
		deleteOldData();
		
		String sql = " Select P_Employee.P_Employee_ID, P_Employee.P_Distribution_Booklet_ID, * From P_Employee_Credits, P_Employee  "
			+ " WHERE P_Employee_Credits.P_Employee_ID = P_Employee.P_Employee_ID "
			+ " AND P_Employee.IsActive = 'Y' "
			+ " AND P_Employee_Credits.P_Credits_ID = " + ToolsCredits.getCredits_Src_ID()
			+ " AND P_Employee_Credits.IsActive = 'Y' "
			;
		
		PreparedStatement pstmt = DB.prepareStatement(sql, null);
		try
		{
			ResultSet rs = pstmt.executeQuery();
			while(rs.next())
			{
				P_Tools_Credits_Result Result = new P_Tools_Credits_Result( getCtx(), -1, null );
				Result.setP_Distribution_Booklet_ID( rs.getInt( "P_Distribution_Booklet_ID"));
				Result.setP_Employee_ID( rs.getInt("P_Employee_ID"));
				Result.setP_Tools_Credits_ID( ToolsCredits.getP_Tools_Credits_ID());
				Result.setCredits_Dst_ID( ToolsCredits.getCredits_Dst_ID());
				Result.setCredits_Src_ID( ToolsCredits.getCredits_Src_ID());
				Result.setIsActive(true);
				Result.setTransfertDate( ToolsCredits.getTransfertDate());
				Result.save();
				
				m_inserted++;
			}
	  		rs.close ();
	  		pstmt.close ();
	  		pstmt = null;

		}
		catch(Exception e)
		{
		  	s_log.log(Level.SEVERE,"P_InOutToolsCredits- " + sql, e);
		}

		return  "@inserted@ " + m_inserted;
	}

}
