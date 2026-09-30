package solstice.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

import solstice.model.P_Employee;
import solstice.model.P_Employee_Deduction;
import solstice.model.P_Employee_Taxable_Benefit;
import solstice.model.P_Standard_Time;
import solstice.model.X_I_Employee_Option;

public class ImportEmployeeEmail extends SvrProcess
{
	/**	Client to be imported to		*/
	private int				m_AD_Client_ID = 0;
	/**	Delete old Imported				*/
	private boolean			m_deleteOldImported = false;

	/** Effective						*/
	private Timestamp		m_DateValue = null;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("AD_Client_ID"))
				m_AD_Client_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("DeleteOldImported"))
				m_deleteOldImported = "Y".equals(para[i].getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		if (m_DateValue == null)
			m_DateValue = new Timestamp (System.currentTimeMillis());
	}	//	prepare


	private String trxName =  null;
	private P_Employee employee;
//	private X_I_Employee_Option imp;
	boolean isMax = false;
	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception
	 */
	protected String doIt() throws java.lang.Exception
	{
		StringBuffer sql = null;
		int no = 0;
		String clientCheck = " AND AD_Client_ID=" + m_AD_Client_ID;

		//	Delete Old Imported
//		if (m_deleteOldImported)
//		{

		sql = new StringBuffer ("UPDATE i_employee_email SET I_ERRORMSG = null " );  
		DB.executeUpdate(sql.toString(), null);

		sql = new StringBuffer ("UPDATE i_employee_email SET P_EMPLOYEE_ID = null " );  
		DB.executeUpdate(sql.toString(), null);

		sql = new StringBuffer ("UPDATE i_employee_email SET P_EMPLOYEE_ID = p_employee.p_employee_id from p_employee where upper( rtrim(i_employee_email.FIRSTNAME)) = upper( rtrim(p_employee.FIRSTNAME)) AND  upper( rtrim(i_employee_email.lastNAME)) = upper( rtrim( p_employee.SURNAME ))" );
		DB.executeUpdate(sql.toString(), null);

		sql = new StringBuffer ("UPDATE i_employee_email SET I_ERRORMSG = 'Employé non définie / employee not found' where P_Employee_ID is null " );  
		DB.executeUpdate(sql.toString(), null);


		sql = new StringBuffer ("SELECT * FROM i_employee_email "
				+ "WHERE I_ERRORMSG is null and I_IsImported='N' ");
			log.info( "Sql " + sql);
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), trxName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
//				imp = new X_I_Employee_Option (getCtx(), rs.getInt(1), trxName);
				
				employee = P_Employee.get( getCtx(), rs.getInt("P_Employee_ID"), trxName);
				employee.setEMail(rs.getString("Email"));
				employee.save();
				
				
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
			System.err.println( "error : " + e);
		}
		return "";
	}


}
