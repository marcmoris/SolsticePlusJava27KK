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

package solstice.model;


import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.model.CompiereProcessor;
import org.compiere.model.CompiereProcessorLog;
import org.compiere.model.MClient;
import org.compiere.model.MRole;
import org.compiere.util.*;


/**
 *	Accounting Processor Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MPayProcessorV2.java,v 1.1 2007/07/18 14:39:55 marmor01 Exp $
 */
public class MPayProcessorV2 extends X_P_PayProcessorV2
	implements CompiereProcessor
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Active
	 *	@param ctx context
	 *	@return active processors
	 */
	public static MPayProcessorV2[] getActive (Properties ctx)
	{
		ArrayList<MPayProcessorV2> list = new ArrayList<MPayProcessorV2>();
		String sql = "SELECT * FROM P_PayProcessorV2 WHERE IsActive='Y'";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MPayProcessorV2 (ctx, rs, null));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "getActive", e);
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
		MPayProcessorV2[] retValue = new MPayProcessorV2[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getActive

	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MPayProcessorV2.class);

	private Stack<String> stack;
	
	/**
	 * 	Standard Construvtor
	 *	@param ctx context
	 *	@param P_PayProcessor_ID id
	 *	@param trxName transaction
	 */
	public MPayProcessorV2 (Properties ctx, int P_PayProcessorV2_ID, String trxName)
	{
		super (ctx, P_PayProcessorV2_ID, trxName);
		if (P_PayProcessorV2_ID == 0)
		{
		//	setName (null);
		//	setSupervisor_ID (0);
			setFrequencyType (FREQUENCYTYPE_Minute);
			setFrequency (1);
			setKeepLogDays (7);	// 7
			
		}	
//		m_stack = this.getStack();
		
		
	}	//	MPayProcessorV2

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MPayProcessorV2 (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MPayProcessorV2

	/**
	 * 	Parent Constructor
	 *	@param client parent
	 *	@param Supervisor_ID admin
	 */
	public MPayProcessorV2 (MClient client, int Supervisor_ID)
	{
		this (client.getCtx(), 1000003, client.get_TrxName());
		setClientOrg(client);
		setName (client.getName() + " - " 
			+ Msg.translate(getCtx(), "P_PayProcessor_ID"));
		setSupervisor_ID (Supervisor_ID);
	}	//	MPayProcessorV2
	
	
	
	/**
	 * 	Get Server ID
	 *	@return id
	 */
	public String getServerID ()
	{
		return "PayProcessor" + get_ID();
	}	//	getServerID

	/**
	 * 	Get Date Next Run
	 *	@param requery requery
	 *	@return date next run
	 */
	public Timestamp getDateNextRun (boolean requery)
	{
		if (requery)
			load(get_TrxName());
		return getDateNextRun();
	}	//	getDateNextRun

	/**
	 * 	Get Logs
	 *	@return logs
	 */
	public CompiereProcessorLog[] getLogs ()
	{
		ArrayList<MPayProcessorV2Log> list = new ArrayList<MPayProcessorV2Log>();
		String sql = "SELECT * "
			+ "FROM P_PayProcessorV2Log "
			+ "WHERE P_PayProcessorV2_ID=? " 
			+ "ORDER BY Created DESC";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getP_PayProcessorV2_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MPayProcessorV2Log (getCtx(), rs, get_TrxName()));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
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
		MPayProcessorV2Log[] retValue = new MPayProcessorV2Log[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getLogs

	/**
	 * 	Delete old Request Log
	 *	@return number of records
	 */
	public int deleteLog()
	{
		if (getKeepLogDays() < 1)
			return 0;
		String sql = "DELETE FROM P_PayProcessorV2Log "
			+ "WHERE P_PayProcessorV2_ID=" + getP_PayProcessorV2_ID() 
			//jz + " AND (Created+" + getKeepLogDays() + ") < SysDate";
			+ " AND addDays(Created," + getKeepLogDays() + ") < SysDate";
		int no = DB.executeUpdate(sql, get_TrxName());
		return 0;
	}	//	deleteLog

	
	   public Stack<String> m_stack;

	/*
	   public Stack<String> getStack() 
	   {
		    Stack<String> stack = new Stack<String>();
		   
			P_Period Period = P_Period.getOpenPeriod( getCtx(), null);
			
			int Period_ID = Period.getP_Period_ID();
			int count = 0;
		    
			String sql = "SELECT * FROM P_Distribution_Booklet WHERE exists( select 1 from P_Time_Sheet where P_Time_Sheet.P_Distribution_Booklet_ID = P_Distribution_Booklet.P_Distribution_Booklet_ID AND P_Time_Sheet.P_Period_ID = " + Period_ID + " )";
			sql = MRole.getDefault().addAccessSQL (sql, "P_Distribution_Booklet", true, false);	// fully qualidfied - RO 

			try
			{
				Statement stmt = DB.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next())
				{
//					P_Distribution_Booklet Distribution_Booklet = new P_Distribution_Booklet (Env.getCtx(), rs, null);
				
					stack.push( rs.getString("P_Distribution_Booklet_ID"));
					count++;
				}
				rs.close();
				stmt.close();
			}
			catch (SQLException e)
			{
				s_log.log(Level.SEVERE, sql, e);
			}

			System.out.println( "Team count : " + count);

			return stack;
		   
	   }
*/

	   public Stack<String> getStackEmployee( String calculPayroll, int _Payment_Group_ID ) 
	   {
		    Stack<String> stack = new Stack<String>();
		   
			P_Period Period = P_Period.getOpenPeriod( getCtx(), null);
			
			int Period_ID = Period.getP_Period_ID();
			
		    
			String sql = "SELECT * FROM P_Employee WHERE P_Payment_Group_ID = " +  _Payment_Group_ID + " AND  exists( select 1 from P_Time_Sheet where P_Time_Sheet.TimeSheetStatus in ('I','F') AND P_Time_Sheet.P_Employee_ID = P_Employee.P_Employee_ID AND P_Time_Sheet.P_Period_ID = " + Period_ID + " )";

			// Recalcul
			if ( calculPayroll.equals("TrueTrue"))
				sql = "SELECT * FROM P_Employee WHERE P_Payment_Group_ID = " +  _Payment_Group_ID + " AND exists( select 1 from P_Time_Sheet where P_Time_Sheet.TimeSheetStatus in ('I','F','C') AND P_Time_Sheet.P_Employee_ID = P_Employee.P_Employee_ID AND P_Time_Sheet.P_Period_ID = " + Period_ID + " )";
				
// 2023-05-01			
//			sql = MRole.getDefault().addAccessSQL (sql, "P_Employee", true, false);	// fully qualidfied - RO 

			s_log.log(Level.INFO, sql);

			
			try
			{
				Statement stmt = DB.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next())
				{
//					P_Distribution_Booklet Distribution_Booklet = new P_Distribution_Booklet (Env.getCtx(), rs, null);
				
					stack.push( rs.getString("P_Employee_ID"));

				}
				rs.close();
				stmt.close();
			}
			catch (SQLException e)
			{
				s_log.log(Level.SEVERE, sql, e);
			}
			

			
			return stack;
		   
	   }


	   
	   public Stack<String> getStackEmployee( String calculPayroll, int _Org_ID, int _Activity_ID , int _Employee_ID, int _Time_Sheet_ID, int _Payment_Group_ID,
				 int _P_Distribution_Booklet_ID ) 
	   {

		   int AD_Org_ID = _Org_ID;
		   int Activity_ID = _Activity_ID;

		   int Employee_ID = _Employee_ID;
		   int Time_Sheet_ID = _Time_Sheet_ID;
		   int Payment_Group_ID = _Payment_Group_ID;
			
		   int P_Distribution_Booklet_ID = _P_Distribution_Booklet_ID;
		   
		    Stack<String> stack = new Stack<String>();
		   
			P_Period Period = P_Period.getOpenPeriod( getCtx(), null);
			
			int Period_ID = Period.getP_Period_ID();

		    
			String sql = "select 1 from P_Time_Sheet WHERE P_Time_Sheet.TimeSheetStatus in ('I','F') AND P_Time_Sheet.P_Employee_ID = P_Employee.P_Employee_ID ";

			// Recalcul
			if ( calculPayroll.equals("TrueTrue"))
				sql = "select 1 from P_Time_Sheet WHERE P_Time_Sheet.TimeSheetStatus in ('I','F','C') AND P_Time_Sheet.P_Employee_ID = P_Employee.P_Employee_ID ";
				
			
			if (AD_Org_ID > 0)
				sql += "   AND P_Time_Sheet.AD_Org_ID = " + AD_Org_ID;

			if (Activity_ID > 0)
				sql +=  "   AND P_Time_Sheet.C_Activity_ID = " + Activity_ID;
			
			if (Employee_ID > 0)
				sql += " AND P_Employee_ID=" + Employee_ID;
//			if (Period_ID > 0)
//				sql += " AND P_Period_ID=" + Period_ID;
			if (Time_Sheet_ID > 0)
				sql += " AND P_Time_Sheet_ID=" + Time_Sheet_ID;
			if (Payment_Group_ID > 0)
				sql += " AND P_Payment_Group_ID=" + Payment_Group_ID;

			if (P_Distribution_Booklet_ID > 0)
				sql += " AND P_Distribution_Booklet_ID=" + P_Distribution_Booklet_ID;

		
			sql = MRole.getDefault( Env.getCtx(), false).addAccessSQL (sql, "P_Time_Sheet", true, false);	// fully qualidfied - RO 
			
			sql = "SELECT * FROM P_Employee WHERE exists( "  + sql + " AND P_Time_Sheet.P_Period_ID = " + Period_ID + " )";

			try
			{
				Statement stmt = DB.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next())
				{
//					P_Distribution_Booklet Distribution_Booklet = new P_Distribution_Booklet (Env.getCtx(), rs, null);
				
					stack.push( rs.getString("P_Employee_ID"));

				}
				rs.close();
				stmt.close();
			}
			catch (SQLException e)
			{
				s_log.log(Level.SEVERE, sql, e);
			}
			

			
			return stack;
		   
	   }

	   
/*	   public Stack<String> getStackEmployeeTest( String calculPayroll ) 
	   {
		    Stack<String> stack = new Stack<String>();
		   
			P_Period Period = P_Period.getOpenPeriod( getCtx(), null);
			
			int Period_ID = Period.getP_Period_ID();

		    
			String sql = "SELECT Top 10 * FROM P_Employee WHERE exists( select 1 from P_Time_Sheet where P_Time_Sheet.TimeSheetStatus in ('I','F') AND P_Time_Sheet.P_Employee_ID = P_Employee.P_Employee_ID AND P_Time_Sheet.P_Period_ID = " + Period_ID + " )";

			// Recalcul
			if ( calculPayroll.equals("TrueTrue"))
				sql = "SELECT Top 10  * FROM P_Employee WHERE exists( select 1 from P_Time_Sheet where P_Time_Sheet.TimeSheetStatus in ('I','F','C') AND P_Time_Sheet.P_Employee_ID = P_Employee.P_Employee_ID AND P_Time_Sheet.P_Period_ID = " + Period_ID + " )";
				
			sql = MRole.getDefault().addAccessSQL (sql, "P_Employee", true, false);	// fully qualidfied - RO 

			try
			{
				Statement stmt = DB.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next())
				{
//					P_Distribution_Booklet Distribution_Booklet = new P_Distribution_Booklet (Env.getCtx(), rs, null);
				
					stack.push( rs.getString("P_Employee_ID"));

				}
				rs.close();
				stmt.close();
			}
			catch (SQLException e)
			{
				s_log.log(Level.SEVERE, sql, e);
			}
			

			
			return stack;
		   
	   }
*/	   

/*	
	public Stack<String> getStack()
	{
		return stack;
	}
*/	

}	//	MPayProcessorV2
