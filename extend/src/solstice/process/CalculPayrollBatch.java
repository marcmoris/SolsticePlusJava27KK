
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


import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.logging.Level;

import org.compiere.model.MPInstance;
import org.compiere.model.MPInstancePara;
import org.compiere.model.MScheduler;
import org.compiere.model.MSchedulerPara;
import org.compiere.process.*;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

import solstice.model.P_Employee;

import solstice.model.P_Period;
import solstice.model.P_Time_Sheet;
import solstice.utils.PgiUtil;

/**
 * @author kevmar01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CalculPayrollBatch extends SvrProcess 
{
	private int Org_ID = 0;
	private int Activity_ID = 0;

	private int Employee_ID = 0;
	private int Time_Sheet_ID = 0;
	private int Frequency_ID = 0;
	private int Period_ID = 0;
	private int Payment_Group_ID = 0;
    private int P_Distribution_Booklet_ID = 0;
    private int P_Collective_Labour_Agr_ID = 0;
 	
	private P_Period Period;
	private P_Employee Employee;
//	private P_Assignment Assignment;
	private P_Time_Sheet TimeSheet;
    private boolean IsRework = false;
    private boolean runBackGround = false;


	/**	Big Decimal 0	 */
	static final public java.math.BigDecimal ZERO = new java.math.BigDecimal(0.0);

	protected void prepare()
	{
		P_Period Period = P_Period.getOpenPeriod( getCtx(), null);
		
		Period_ID = Period.getP_Period_ID();
		Frequency_ID = Period.getP_Frequency_ID();
		runBackGround = false;
		IsRework = false;
		
	}	//	prepare

	
	/**
	 * 	Read each time sheet 
	 *	@return 
	 *	@throws Exception
	 */
	
	private Calendar calendar = Calendar.getInstance();
	private java.util.Date now = calendar.getTime();


	protected String doIt () throws Exception
	{
		boolean retValue = true;
		String ret = "";

		String calculPayroll = PgiUtil.getSolsticeParameter(Env.getCtx(), "CalculPayroll");
		if ( calculPayroll.equals("True")) {
			
			DB.executeUpdate("UPDATE P_Solstice_Parameters set Parameter = 'False' where value = 'CalculPayroll'", null);		

			log.log (Level.INFO,"Calculation - start");

			TimeValidation tmp = new TimeValidation( getCtx(), this.getAD_Client_ID(), Org_ID, Activity_ID, Employee_ID, Time_Sheet_ID, Payment_Group_ID, Frequency_ID, Period_ID, P_Distribution_Booklet_ID, P_Collective_Labour_Agr_ID, IsRework, null);
			ret = tmp.Validation();
		}

/*
		int Occupation_Group_ID = 0;

		String sql = "SELECT P_Occupation_Group_ID FROM P_Time_Sheet WHERE p_period_id = " + Period_ID + " GROUP BY p_occupation_group_id";
		PreparedStatement pstmt = null;

		try {
			
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			
			while (rs.next ()) 
			{
				Occupation_Group_ID = rs.getInt("P_Occupation_Group_ID");
				TimeValidation tmp = new TimeValidation( getCtx(), this.getAD_Client_ID(), Org_ID, Activity_ID, Employee_ID, Time_Sheet_ID, Payment_Group_ID, Frequency_ID, Period_ID, P_Distribution_Booklet_ID, IsRework, null);
				ret = tmp.Validation();

			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch( Exception e ) {
			retValue = false;
		}

*/
		log.log (Level.INFO,"Calculation - end");
		return retValue == true ? ret : "error";
		
	}
	
	

}
