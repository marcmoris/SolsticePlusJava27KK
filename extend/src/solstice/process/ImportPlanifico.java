package solstice.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.Compiere;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.IBookletTimeSheet;
import solstice.model.P_Assignment;
import solstice.model.P_Employee;
import solstice.model.P_Period;
import solstice.model.P_Punch_Time;
import solstice.model.Planifico;

public class ImportPlanifico extends SvrProcess{
	
	private int Period_ID = 0;
	private int Payment_Group_ID = 0;
	private int Employee_ID = 0;
	
	protected void prepare()
    {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("P_Period_ID"))
				Period_ID = para[i].getParameterAsInt();
			else if (name.equals("P_Payment_Group_ID"))
				Payment_Group_ID = para[i].getParameterAsInt();
			else if (name.equals("P_Employee_ID"))
				Employee_ID = para[i].getParameterAsInt();
			else
				log.log (Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}		
    }
	
	protected String doIt() throws Exception
	{
		boolean retValue = true;
		String ret = "Importation terminée";
		
		if ( Payment_Group_ID == 0 )
		{
			String sql1 = "TRUNCATE TABLE DB_PLANIFICO_PUNCH_V2";
//			String sql1 = "DELETE FROM DB_PLANIFICO_PUNCH_V2 WHERE BizSubLabel = " + DB.TO_STRING( sBizSubLabel ); 
	        DB.executeUpdate(sql1.toString(), null);


			String sBizRefId = "101";
			String sBizSubLabel = "Warwick";
	    	String result = Planifico.ImportPlanifico( Period_ID, Employee_ID, sBizRefId, sBizSubLabel);
	    	if ( result != null ) retValue = false;
			
			sBizRefId = "102";
			sBizSubLabel = "Quebec";
			result =  Planifico.ImportPlanifico( Period_ID, Employee_ID, sBizRefId, sBizSubLabel);
			if ( result != null ) retValue = false;
			
	    	addGainCompensatoire( Employee_ID, null );
			
		}
		else if ( Payment_Group_ID == 1000012)
		{
	        String sBizRefId = "102";
	        String sBizSubLabel = "Quebec";
			String sql1 = "DELETE FROM DB_PLANIFICO_PUNCH_V2 WHERE BizSubLabel = " + DB.TO_STRING( sBizSubLabel ); 
	        DB.executeUpdate(sql1.toString(), null);
			
	        String result =  Planifico.ImportPlanifico( Period_ID, Employee_ID, sBizRefId, sBizSubLabel);
	        if ( result != null ) retValue = false;
/*	        
	        // 2025-07-24 Import les employés en période -1
	        String sql = "Select * from p_employee where CustomFieldYesNo01 = 'Y' and Isactive = 'Y'";
	        if ( Employee_ID != 0 )
			{
	   			sql = sql + " AND P_Employee_ID =  " + Employee_ID;
			}
	        		
	        Period_ID = getLastPeriod();
			try
			{
				PreparedStatement pstmt = DB.prepareStatement (sql, null);
				ResultSet rs = pstmt.executeQuery ();
				String emp_list = "";
				while (rs.next ())
				{
					int emp_id = rs.getInt( "P_Employee_ID");
					
					emp_list = emp_list + "," + emp_id;
				}
				log.log (Level.INFO,"emp_list - " + emp_list );
				rs.close ();
				pstmt.close ();
				pstmt = null;

				PlanificoDistributeur.ImportPlanifico( Period_ID, 0, sBizRefId, sBizSubLabel, emp_list);

			}
			catch (Exception e)
			{
				log.log (Level.SEVERE,"Distributeur - " + sql, e);
				throw e;
			}
*/			
		}
		else if ( Payment_Group_ID == 1000015)
		{
			String sBizRefId = "101";
			String sBizSubLabel = "Warwick";

			String sql1 = "DELETE FROM DB_PLANIFICO_PUNCH_V2 WHERE BizSubLabel = " + DB.TO_STRING( sBizSubLabel ); 
	        DB.executeUpdate(sql1.toString(), null);
			
	        String result =  Planifico.ImportPlanifico( Period_ID, Employee_ID, sBizRefId, sBizSubLabel);
	        if ( result != null ) retValue = false;
	    	addGainCompensatoire( Employee_ID, null );
		}
		else return "error";

		int l_Role = Env.getAD_Role_ID(Env.getCtx());
        ReportLauncher.callReport( this.getAD_PInstance_ID(), l_Role, "PDF", getParameter() );
		
		
		return retValue == true ? ret : "error";

//    	return "";
    }
	  
/*	public static void main(final String[] args) throws Exception 
	{
		Compiere.startup(true);
		P_Period Period = P_Period.getOpenPeriod(Env.getCtx(), 0, null);
		
		String sBizRefId = "101";
		String sBizSubLabel = "Warwick";
    	result =  Planifico.ImportPlanifico( Period.getP_Period_ID(), sBizRefId, sBizSubLabel);
		
		sBizRefId = "102";
		sBizSubLabel = "Quebec";
    	result =  Planifico.ImportPlanifico( Period.getP_Period_ID(), sBizRefId, sBizSubLabel);
		

	}	
	*/	
	
	//
	// Ajout le code 0019 pour compenser les heures a 40 h 
	//
	private BigDecimal addGainCompensatoire( int Employee_ID, String trxName ) throws Exception
	{
		// STD = 1000014
		BigDecimal Quantity = Env.ZERO;
		String sql = "SELECT P_Punch_Time.AD_Client_ID, P_Punch_Time.AD_Org_ID, P_Punch_Time.P_Period_ID, P_Punch_Time.P_Employee_ID, bizSubLabel, SUM( ISNULL( DayQty, 0 ) )  Quantity, MAX(P_Assignment_ID ) P_Assignment_ID  "
				+ " FROM P_Punch_Time "
				+ " INNER JOIN P_Employee ON P_Employee.P_Employee_ID = P_Punch_Time.P_Employee_ID "
				+ " WHERE  P_Punch_Time.P_Gain_ID IN ( select P_Gain_ID from P_GAIN_GAININFO where p_gaininfo_id = ( select P_GAININFO_ID from p_gaininfo where p_gaininfo.VALUE = 'Horairecom' ) and P_GAIN_GAININFO.TO_CONSIDER = 'Y' ) "
			    + " AND p_employee.CustomFieldYesNo03 = 'Y' "
//				+ " AND Exists( select 1 from P_STANDARD_TIME WHERE P_STANDARD_TIME.P_EMPLOYEE_ID = P_Punch_Time.P_Employee_ID "
//				+ " AND P_STANDARD_TIME.P_Gain_ID in ( SELECT P_GAIN_ID FROM P_GAIN WHERE VALUE = '0019' ) )"
				+ " AND p_employee.P_DISTRIBUTION_ID not in ( select P_DISTRIBUTION_ID from P_DISTRIBUTION where value = '62' ) "				
				+ " GROUP BY  P_Punch_Time.AD_Client_ID, P_Punch_Time.AD_Org_ID, P_Punch_Time.P_Period_ID, P_Punch_Time.P_Employee_ID, bizSubLabel "
				+ " HAVING SUM( ISNULL( DayQty, 0 ) ) < 37 "
                   ;
		
		if ( Employee_ID != 0 )
		{
			sql = "SELECT P_Punch_Time.AD_Client_ID, P_Punch_Time.AD_Org_ID, P_Punch_Time.P_Period_ID, P_Punch_Time.P_Employee_ID, bizSubLabel, SUM( ISNULL( DayQty, 0 ) )  Quantity, MAX(P_Assignment_ID ) P_Assignment_ID  "
					+ " FROM P_Punch_Time "
					+ " INNER JOIN P_Employee ON P_Employee.P_Employee_ID = P_Punch_Time.P_Employee_ID "
					+ " WHERE  P_Punch_Time.P_Gain_ID IN ( select P_Gain_ID from P_GAIN_GAININFO where p_gaininfo_id = ( select P_GAININFO_ID from p_gaininfo where p_gaininfo.VALUE = 'Horairecom' ) and P_GAIN_GAININFO.TO_CONSIDER = 'Y' ) "
				    + " AND p_employee.CustomFieldYesNo03 = 'Y' "
					+ " AND P_Punch_Time.P_Employee_ID = " + Employee_ID 
					+ " AND p_employee.P_DISTRIBUTION_ID not in ( select P_DISTRIBUTION_ID from P_DISTRIBUTION where value = '62' ) "				
					+ " GROUP BY  P_Punch_Time.AD_Client_ID, P_Punch_Time.AD_Org_ID, P_Punch_Time.P_Period_ID, P_Punch_Time.P_Employee_ID, bizSubLabel "
					+ " HAVING SUM( ISNULL( DayQty, 0 ) ) < 37 "
	                   ;

		}
		
        PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement(sql, trxName);
		
		ResultSet rs = pstmt.executeQuery();
		while (rs.next())
		{
			// modifié 40 pour 37 2024-08-26
			// 40 - xx pour compenser 37h + 3h
			Quantity = new BigDecimal( 37 ).subtract( rs.getBigDecimal("Quantity"));
			P_Period Period = P_Period.get(getCtx(), rs.getInt("P_Period_ID"), trxName);
			
			P_Employee Employee = P_Employee.get(getCtx(), rs.getInt("P_Employee_ID"), trxName);
//			P_Assignment Assignment = P_Assignment.get(getCtx(), Employee.GetAssignmentPrincipal(), trxName);
			P_Assignment Assignment = P_Assignment.get(getCtx(), rs.getInt("P_Assignment_ID"), trxName);
			
			P_Punch_Time punch_Time = new P_Punch_Time( getCtx(), -1, trxName);
			punch_Time.setDayQty(Quantity);
			punch_Time.setDay( Period.getEndDate());
			punch_Time.setAD_Org_ID( rs.getInt( "AD_Org_ID"));
			punch_Time.setP_Employee_ID( rs.getInt( "P_Employee_ID"));
			punch_Time.setHourlyRate(null);
			punch_Time.setStartTime(null);
			punch_Time.setEndTime(null);
			punch_Time.setP_Assignment_ID( Assignment.getP_Assignment_ID() );
			if ( Employee.getP_Schedule_ID( ) == 0)
				punch_Time.setP_Schedule_ID( 100000 );
			else
				punch_Time.setP_Schedule_ID( Employee.getP_Schedule_ID( ));

			punch_Time.setP_Post_ID( Assignment.getP_Post_ID() );
			punch_Time.setP_Gain_ID( 1103808 );  // 0019
			punch_Time.setP_Timecode_ID(1000018); // 0019
			punch_Time.setP_Distribution_ID(Employee.getP_Distribution_ID() );
			
			punch_Time.setP_Period_ID(rs.getInt( "P_Period_ID"));
			punch_Time.setbizSubLabel( rs.getString( "bizSubLabel") );
	             		
			punch_Time.save();
		}
		rs.close();
		pstmt.close();
		pstmt = null;
		
		return Quantity;
		
	}


	private int getLastPeriod( )
	{
		
		String sql = "SELECT MAX(P_PERIOD_ID ) P_PERIOD_ID FROM P_PERIOD WHERE isAdjustmentPeriod = 'N' AND PERIODSTATUS = 'C'";
		int Id = 0;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				Id = rs.getInt( "P_PERIOD_ID");
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"getLastPeriod - " + sql, e);
		}
	
//		if ( Id == 0)
//			return Period.getP_Period_ID();
			
		return Id;	
	}

}
