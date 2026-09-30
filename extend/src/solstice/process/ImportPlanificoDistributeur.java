package solstice.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.PlanificoDistributeur;

public class ImportPlanificoDistributeur extends SvrProcess{
	
	private int Period_ID = 0;
	private int Payment_Group_ID = 0;
	private int Employee_ID = 0;
	
	protected void prepare()
    {
		
		Payment_Group_ID = 1000012;
		Period_ID = getLastPeriod();
		
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("P_Employee_ID"))
				Employee_ID = para[i].getParameterAsInt();
			else
				log.log (Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}		
    }
	
	protected String doIt() throws Exception
	{
		boolean retValue = true;
		String empList = "";
		String empListValue = "";
		List<String> empListArray = new ArrayList<String>();

		
		String ret = "Importation terminée";
	
        String sql = "Select * from p_employee where CustomFieldYesNo01 = 'Y' and Isactive = 'Y'";
        if ( Employee_ID != 0 )
		{
   			sql = sql + " AND P_Employee_ID =  " + Employee_ID;
		}
//        
		try
		{
			PreparedStatement pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			
			while (rs.next ())
			{
				String emp_id = rs.getString( "P_Employee_ID");
				
				empListArray.add(rs.getString("Value"));
				
				if ( rs.isFirst())
				{
					empList = emp_id;
					empListValue = DB.TO_STRING( rs.getString("Value") );
				}
				else
				{
					empList = empList + "," + emp_id;
					empListValue = empListValue + "," + DB.TO_STRING( rs.getString("Value") );;
				}
			}
			log.log (Level.SEVERE,"emp_listValue - " + empListValue );
			rs.close ();
			pstmt.close ();
			pstmt = null;

		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"Distributeur - " + sql, e);
			throw e;
		}
		
/*		if ( Payment_Group_ID == 1000012)
		{
*/		
	        String sBizRefId = "102";
	        String sBizSubLabel = "Quebec";
			String sql1 = "DELETE FROM DB_PLANIFICO_PUNCH_V2 WHERE BizSubLabel = " + DB.TO_STRING( sBizSubLabel ); 
	        DB.executeUpdate(sql1.toString(), null);
			
	        String result =  PlanificoDistributeur.ImportPlanificoDistributeur( Period_ID, Employee_ID, sBizRefId, sBizSubLabel, empList, empListValue, empListArray);
	        if ( result != null ) retValue = false;
/*	        
		}
		else return "error";
*/
		int l_Role = Env.getAD_Role_ID(Env.getCtx());
        ReportLauncher.callReport( this.getAD_PInstance_ID(), l_Role, "PDF", getParameter() );
		
		
		return retValue == true ? ret : "error";

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
