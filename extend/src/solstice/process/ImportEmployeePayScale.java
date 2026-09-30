package solstice.process;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

public class ImportEmployeePayScale extends SvrProcess
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
//			sql = new StringBuffer ("update p_assignment set p_post_id = null, isincumbent = 'N' where 1=1 " + clientCheck );
//			DB.executeUpdate(sql.toString(), null);
			
		}
	
		int noInsert = 0;
		int noUpdate = 0;

		sql = new StringBuffer ("UPDATE i_employee_Payscale SET I_ERRORMSG = null, I_IsImported = 'N' " );  
		DB.executeUpdate(sql.toString(), null);

		sql = new StringBuffer ("UPDATE i_employee_PayScale SET P_EMPLOYEE_ID = null " );
		DB.executeUpdate(sql.toString(), null);

		sql = new StringBuffer ("UPDATE i_employee_PayScale SET P_EMPLOYEE_ID = p_employee.p_employee_id from p_employee where i_employee_payscale.employee = p_employee.value " );
		DB.executeUpdate(sql.toString(), null);

//		sql = new StringBuffer ("UPDATE i_employee_PayScale SET P_EMPLOYEE_ID = p_employee.p_employee_id from p_employee where lpad( i_employee_payscale.employee, 6, '0' ) = p_employee.value " );
//		DB.executeUpdate(sql.toString(), null);

		// avec le nom... vérifier pourquoi avec chantal les numéros ne sont pas identique.
		sql = new StringBuffer ("UPDATE i_employee_PayScale SET P_EMPLOYEE_ID = p_employee.p_employee_id from p_employee where  i_employee_payscale.firstname =  p_employee.firstname and  i_employee_payscale.lastname =  p_employee.surname and i_employee_PayScale.P_Employee_ID IS NULL");
		DB.executeUpdate(sql.toString(), null);
		
		
		sql = new StringBuffer ("UPDATE i_employee_PayScale SET P_EMPLOYEE_ID = p_employee.p_employee_id from p_employee where substring( i_employee_payscale.employee, 2,5 ) = substring( p_employee.value , 2,5 ) and i_employee_PayScale.P_Employee_ID IS NULL " );
		DB.executeUpdate(sql.toString(), null);

		sql = new StringBuffer ("UPDATE P_assignment set expectedadvancedate = Anniversary from i_employee_payscale where P_assignment.p_employee_id = i_employee_payscale.p_employee_id" );  
		DB.executeUpdate(sql.toString(), null);

		sql = new StringBuffer ("UPDATE i_employee_PayScale SET P_Occupation_Group_ID = 1000226 where i_employee_payscale.payscale = 'AME' ");
		DB.executeUpdate(sql.toString(), null);
		
		sql = new StringBuffer ("UPDATE i_employee_PayScale SET P_Occupation_Group_ID = 1000234 where i_employee_payscale.payscale = 'AVIONIC'" );
		DB.executeUpdate(sql.toString(), null);

		sql = new StringBuffer ("UPDATE i_employee_PayScale SET P_Occupation_Group_ID = 1000234 where i_employee_payscale.payscale = 'AVIONICS'" );
		DB.executeUpdate(sql.toString(), null);

		sql = new StringBuffer ("UPDATE i_employee_PayScale SET P_Occupation_Group_ID = 1000231 where i_employee_payscale.payscale = 'COMPONENT SHOP & SHEET METAL T' ");
		DB.executeUpdate(sql.toString(), null);
		sql = new StringBuffer ("UPDATE i_employee_PayScale SET P_Occupation_Group_ID = 1000243 where i_employee_payscale.payscale = 'EASTERN VFR CONTRACTUAL PILOT' ");  
		DB.executeUpdate(sql.toString(), null);
		sql = new StringBuffer ("UPDATE i_employee_PayScale SET P_Occupation_Group_ID = 1000223 where i_employee_payscale.payscale = 'EASTERN VFR PILOT' ");
		DB.executeUpdate(sql.toString(), null);
		sql = new StringBuffer ("UPDATE i_employee_PayScale SET P_Occupation_Group_ID = 1000245 where i_employee_payscale.payscale = 'EMS CAPTAIN' ");
		DB.executeUpdate(sql.toString(), null);
		                                                                                                                              
		sql = new StringBuffer ("UPDATE i_employee_PayScale SET P_Occupation_Group_ID = 1000247 where i_employee_payscale.payscale = 'EMS FIRST OFFICER'");
		DB.executeUpdate(sql.toString(), null);
		
		sql = new StringBuffer ("UPDATE i_employee_PayScale SET P_Occupation_Group_ID = 1000246 where i_employee_payscale.payscale = 'EMS AME' ");
		DB.executeUpdate(sql.toString(), null);
		
		sql = new StringBuffer ("UPDATE i_employee_PayScale SET P_Occupation_Group_ID = 1000232 where i_employee_payscale.payscale = 'PAINT SHOP' ");
		DB.executeUpdate(sql.toString(), null);
		sql = new StringBuffer ("UPDATE i_employee_PayScale SET P_Occupation_Group_ID = 1000244 where i_employee_payscale.payscale = 'WESTERN VFR CONTRACTUAL PILOT' ");
		DB.executeUpdate(sql.toString(), null);
		sql = new StringBuffer ("UPDATE i_employee_PayScale SET P_Occupation_Group_ID = 1000239 where i_employee_payscale.payscale = 'WESTERN VFR PILOT' ");
		DB.executeUpdate(sql.toString(), null);

		sql = new StringBuffer ("UPDATE i_employee_PayScale SET P_Occupation_Group_ID = 1000230 where i_employee_payscale.payscale = 'Administration' ");
		DB.executeUpdate(sql.toString(), null);

		sql = new StringBuffer ("UPDATE i_employee_PayScale SET P_Occupation_Group_ID = 1000241 where i_employee_payscale.payscale = 'Edmonton Administration' ");
		DB.executeUpdate(sql.toString(), null);


		sql = new StringBuffer ("UPDATE i_employee_PayScale SET P_Salary_Scale_ID = P_Salary_Scale.P_Salary_Scale_ID " +
 		                        "FROM P_Salary_Scale WHERE P_Salary_Scale.P_Occupation_Group_ID = i_Employee_PayScale.P_Occupation_Group_ID" );
		DB.executeUpdate(sql.toString(), null);

		sql = new StringBuffer ("UPDATE i_employee_PayScale SET P_Salary_Scale_Detail_ID = P_Salary_Scale_Detail.P_Salary_Scale_Detail_ID " +
				"FROM P_Salary_Scale_Detail " +
				"WHERE P_Salary_Scale_Detail.P_Salary_Scale_ID = i_Employee_PayScale.P_Salary_Scale_ID " +
				" and rtrim(P_Salary_Scale_Detail.step) = + rtrim(i_Employee_Payscale.level)   " ) 
				; 
		DB.executeUpdate(sql.toString(), null);

		
		sql = new StringBuffer ("UPDATE i_employee_PayScale SET P_Salary_Scale_Detail_ID = P_Salary_Scale_Detail.P_Salary_Scale_Detail_ID " + 
		    " FROM P_Salary_Scale_Detail " +
		    " WHERE P_Salary_Scale_Detail.P_Salary_Scale_ID = i_Employee_PayScale.P_Salary_Scale_ID " + 
		    " and rtrim(P_Salary_Scale_Detail.step) =  '0' +rtrim(i_Employee_Payscale.level)" ); 
		 		
		DB.executeUpdate(sql.toString(), null);

		sql = new StringBuffer ("UPDATE i_employee_PayScale SET P_Salary_Scale_Detail_ID = P_Salary_Scale_Detail.P_Salary_Scale_Detail_ID " + 
			    " FROM P_Salary_Scale_Detail " +
			    " WHERE P_Salary_Scale_Detail.P_Salary_Scale_ID = i_Employee_PayScale.P_Salary_Scale_ID " + 
			    " and rtrim(P_Salary_Scale_Detail.step) =  ' ' +rtrim(i_Employee_Payscale.level)" ); 
			 		
			DB.executeUpdate(sql.toString(), null);

		/*		
		sql = new StringBuffer ("UPDATE i_employee_PayScale SET P_Salary_Scale_Detail_ID = P_Salary_Scale_Detail.P_Salary_Scale_Detail_ID " +
		"FROM P_Salary_Scale_Detail " +
		"WHERE P_Salary_Scale_Detail.P_Salary_Scale_ID = i_Employee_PayScale.P_Salary_Scale_ID " +
		" and rtrim(P_Salary_Scale_Detail.step) like  '%' + rtrim(i_Employee_Payscale.level) + '%'  " ) 
		; 
		DB.executeUpdate(sql.toString(), null);
*/

		sql = new StringBuffer ("update p_assignment_param " +
		"set isout_of_range = 'N', " +
		"  p_occupation_group_id = i_employee_payscale.p_occupation_group_id, " +
		"  p_salary_scale_id = i_employee_payscale.p_salary_scale_id,  " +
		"  p_salary_scale_detail_id = i_employee_payscale.p_salary_scale_detail_id " +
		"from i_employee_payscale " +
		"where p_assignment_param.p_assignment_id in (select p_assignment_id from p_assignment where p_employee_id = i_employee_payscale.p_employee_id  ) " +
		"and i_employee_payscale.p_salary_scale_detail_id is not null " );
		DB.executeUpdate(sql.toString(), null);


		sql = new StringBuffer ("update p_assignment_param " +
				"set remuneration_method = p_salary_scale.remuneration_method " +
				"from p_salary_scale " +
				"where p_assignment_param.p_salary_scale_id = p_salary_scale.p_salary_scale_id and p_assignment_param.ad_client_id <> 11" );
		
		DB.executeUpdate(sql.toString(), null);


		sql = new StringBuffer ("update p_assignment_param " +
				"set weekly_hours = 40, day_hours = 8, annual_salary = 0, daily_salary = p_salary_scale_detail.annual_salary  " +
				"from p_salary_scale, p_salary_scale_detail " +
				"where p_assignment_param.p_salary_scale_id = p_salary_scale.p_salary_scale_id and p_assignment_param.ad_client_id <> 11" +
				" and p_salary_scale.remuneration_method = 'daily'" + 
				" and p_salary_scale_detail.p_salary_scale_detail_id = p_assignment_param.p_salary_scale_detail_id ");
		
		DB.executeUpdate(sql.toString(), null);

		
		sql = new StringBuffer ("UPDATE i_employee_Payscale SET I_ERRORMSG = 'Employé non définie / employee not found' where P_Employee_ID is null " );  
		DB.executeUpdate(sql.toString(), null);

		sql = new StringBuffer ("UPDATE i_employee_Payscale SET I_ERRORMSG = 'Groupe d''emploi non définie / occupation group not found' where P_Occupation_Group_ID is null " );  
		DB.executeUpdate(sql.toString(), null);

		sql = new StringBuffer ("UPDATE i_employee_Payscale SET I_ERRORMSG = 'Echelle salarial définie / payscale not found' where P_Salary_Scale_ID is null OR P_Salary_Scale_Detail_ID IS null " );  
		DB.executeUpdate(sql.toString(), null);

		sql = new StringBuffer ("UPDATE i_employee_Payscale SET I_ERRORMSG = 'Salaire annuel employé différent du salaire échelle ' " + 
			" from p_salary_scale_detail, p_assignment, p_assignment_param, p_employee " +
			" where p_salary_scale_detail.p_salary_scale_detail_id = i_employee_payscale.p_salary_scale_detail_id " +
			" and p_assignment.p_employee_id = i_employee_Payscale.p_employee_id " +
			" and p_assignment_param.p_assignment_id = p_assignment.p_assignment_id " +
			" and round(p_salary_scale_detail.annual_salary,-2) <> round(p_assignment_param.annual_salary,-2) " +
			" and I_ERRORMSG is null " +
		    " and isnull( p_salary_scale_detail.annual_salary, 0 ) <> 0" +
			" and p_employee.p_employee_id = p_assignment.p_employee_id " +
			" and p_employee.isactive = 'Y' "
		);
		DB.executeUpdate(sql.toString(), null);

		sql = new StringBuffer ("update p_assignment_param " +
				"set annual_salary = p_salary_scale_detail.annual_salary  " +
				"from p_salary_scale, p_salary_scale_detail " +
				"where p_assignment_param.p_salary_scale_id = p_salary_scale.p_salary_scale_id and p_assignment_param.ad_client_id <> 11" +
				" and p_salary_scale_detail.p_salary_scale_detail_id = p_assignment_param.p_salary_scale_detail_id  and scale_type <> 'B'"
				);
		
		DB.executeUpdate(sql.toString(), null);


		sql = new StringBuffer ("UPDATE i_employee_Payscale SET I_IsImported = 'Y' WHERE I_ERRORMSG is null" );  
		DB.executeUpdate(sql.toString(), null);

		sql = new StringBuffer ("update p_Employee set  	P_Collective_Labour_Agr_ID = 1000060 where P_Occupation_Group_ID in ( 1000245, 1000247 )" );
		DB.executeUpdate(sql.toString(), null);
		
		
		
		return "";
	}

}
