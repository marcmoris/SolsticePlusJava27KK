/*
 * Created on 2004-11-02
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.*;

import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * @author matduf01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_Employee_Calendar extends X_P_Employee_Calendar 
{

	public P_Employee_Calendar(Properties ctx, int P_Employee_Calendar_ID, String trxName)
	{
		super(ctx,P_Employee_Calendar_ID, trxName);
	}

	/**	Static Logger				*/
	private static CLogger		s_log = CLogger.getCLogger (P_Employee_Calendar.class);

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Employee_Calendar (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Employee_Calendar (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Employee_Calendar_ID"), trxName);
	}	//	P_Column_Adm


	
   public static int getEmployeeCalendarId(int employee_id, int year)
   {
		int employee_calendar_id = -1;
		String sql = "SELECT P_Employee_Calendar_ID from P_Employee_Calendar "
			+ "Where P_employee_id = "+ String.valueOf(employee_id)
			+" AND YEAR=" + String.valueOf(year);
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				employee_calendar_id = rs.getInt("P_Employee_Calendar_ID");
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE,"P_Employee_Calendar.getEmployeeCalendarId", e);
		}
   	return employee_calendar_id;
   }
   
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true or false
	 */

	protected boolean beforeSave (boolean newRecord)
	{
		//+ 2011.07.05 + sécurité par compagnie.
	    P_Employee Employee = P_Employee.get( Env.getCtx(), this.getP_Employee_ID(), this.get_TrxName());
		this.setAD_Org_ID( Employee.getAD_Org_ID());
		//- 2011.07.05 + sécurité par compagnie.

		return true;
	}	//	beforeSave
	
}
