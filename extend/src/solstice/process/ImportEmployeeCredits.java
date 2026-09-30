package solstice.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.model.MLocation;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

import solstice.model.*;

public class ImportEmployeeCredits extends SvrProcess
{
	/**	Client to be imported to		*/
	private int				m_AD_Client_ID = 0;
	/**	Delete old Imported				*/
	private boolean			m_deleteOldImported = false;

	/** Effective						*/
	private Timestamp		m_DateValue = null;

	P_Credits_Movement Credits_Movement;

	private String trxName = null; 
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
		if (m_deleteOldImported)
		{
			sql = new StringBuffer ("delete from p_credits_movement where 1=1 ").append(clientCheck);
			no = DB.executeUpdate(sql.toString(), null);
			log.fine("Delete Old Impored =" + no);

			sql = new StringBuffer ("update i_employee_credits set I_IsImported = 'N' ");
			no = DB.executeUpdate(sql.toString(), null);

		}

		//	Set Client, Org, IsActive, Created/Updated
		sql = new StringBuffer ("UPDATE I_Employee_Credits "
			+ "SET AD_Client_ID = COALESCE (AD_Client_ID, ").append(m_AD_Client_ID).append("),"
			+ " AD_Org_ID = COALESCE (AD_Org_ID, 0),"
			+ " IsActive = COALESCE (IsActive, 'Y'),"
			+ " Created = COALESCE (Created, SysDate),"
			+ " CreatedBy = COALESCE (CreatedBy, 0),"
			+ " Updated = COALESCE (Updated, SysDate),"
			+ " UpdatedBy = COALESCE (UpdatedBy, 0),"
			+ " I_ErrorMsg = NULL,"
			+ " I_IsImported = 'N' "
			+ "WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
		no = DB.executeUpdate(sql.toString(), null);
		log.fine("Reset=" + no);

//		commit();
		//	-------------------------------------------------------------------
		int noInsert = 0;
		int noUpdate = 0;

		sql = new StringBuffer ("UPDATE i_employee_Credits SET I_ERRORMSG = null " );  
		DB.executeUpdate(sql.toString(), null);

		sql = new StringBuffer ("UPDATE I_Employee_Credits set P_Employee_ID = ( select max(P_Employee_ID) From P_Employee where REPLACE( P_Employee.Sin, '-','') = I_Employee_Credits.Sin )" );  
		DB.executeUpdate(sql.toString(), null);

		sql = new StringBuffer ("UPDATE i_employee_Credits SET I_ERRORMSG = 'Employé non définie / employee not found' where P_Employee_ID is null " );  
		DB.executeUpdate(sql.toString(), null);

		commit();


		//	Go through Records
		sql = new StringBuffer ("SELECT * FROM I_Employee_Credits "
			+ "WHERE I_ERRORMSG is null and I_IsImported='N'").append(clientCheck);
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				
				P_Employee Employee = P_Employee.get( getCtx(), rs.getInt("P_Employee_ID"), trxName );
				P_Employee_Credits EmployeeCredits = P_Employee_Credits.get(getCtx(), Employee.getP_Employee_ID(), 1000001, TimeUtil.getDay(2007, 01, 01), trxName);
				if ( EmployeeCredits == null )
				{
					EmployeeCredits = new P_Employee_Credits(getCtx(), -1, trxName);
					EmployeeCredits.setP_Employee_ID(Employee.getP_Employee_ID());
					EmployeeCredits.setP_Credits_ID(1001132);
					EmployeeCredits.setEffectIn(TimeUtil.getDay(2007, 01, 01));
					EmployeeCredits.setIsActive(true);
					EmployeeCredits.setP_Method_Credits_ID(1000001);
					EmployeeCredits.save();
					
				}
				boolean isError = false;
				Credits_Movement = new P_Credits_Movement( getCtx(), -1, trxName);
				Credits_Movement.setAD_Org_ID(0);
				Credits_Movement.setIsActive(true);
				Credits_Movement.setP_Credits_ID(1001132);
				Credits_Movement.setP_Employee_Credits_ID(EmployeeCredits.getP_Employee_Credits_ID());
				Credits_Movement.setP_Employee_ID( Employee.getP_Employee_ID());
				Credits_Movement.setPMovementDate(TimeUtil.getDay(2007, 01, 01));
				Credits_Movement.setP_Method_Credits_ID(1000001);
//				Credits_Movement.setPMovementHour(PMovementHour);
				Credits_Movement.setPMovementOrigin("INIT");
				Credits_Movement.setPMovementType("V+");
				Credits_Movement.setDescription("Importation");
				Credits_Movement.setPMovementVariation( rs.getBigDecimal("PMOVEMENTVARIATION"));
				
				if (Credits_Movement.save())
				{
					noInsert++;
				}

				
			}
			rs.close();
			pstmt.close();

			sql = new StringBuffer ("UPDATE I_Employee_Credits SET I_IsImported = 'Y' WHERE I_ERRORMSG is null" );  
			DB.executeUpdate(sql.toString(), null);

		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
			System.err.println( "error : " + e);
//			rollback();
		}
		
		
		
		//	Set Error to indicator to not imported
		sql = new StringBuffer ("UPDATE I_Employee_Credits "
			+ "SET I_IsImported='N', Updated=SysDate "
			+ "WHERE I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(sql.toString(), null);
		addLog (0, null, new BigDecimal (no), "@Errors@");
		addLog (0, null, new BigDecimal (noInsert), "@P_Employee_ID@: @Inserted@");
		addLog (0, null, new BigDecimal (noUpdate), "@P_Employee_ID@: @Updated@");


		return "";
		
	}

	
	public int getP_Employee_ID( String value )
	{
		int id = 0;
		String sql = "Select P_Employee_ID from P_Employee where value = rtrim('" + value + "')";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				id = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("ImportEmployee getP_Employee_ID - " + e);
		}
		
		return id;
		
	}

}
