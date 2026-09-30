package solstice.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.TimeUtil;

import solstice.model.P_Employee_Deduction;
import solstice.model.X_I_Employee_Insurance_Fact;
import solstice.model.P_Deduction;

public class ImportEmployeeInsurance extends SvrProcess
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
		int noInsert = 0;
		int noUpdate = 0;

		StringBuffer sql = null;
		int no = 0;
		String clientCheck = " AND AD_Client_ID=" + m_AD_Client_ID;
		//	Go through Records
/*		sql = new StringBuffer ("SELECT I_Employee_Insurance_Fact_ID FROM I_Employee_Insurance_Fact "
			+ "WHERE I_IsImported='N'").append(clientCheck)
			 .append( " and not exists ( select 1 from p_employee_deduction where p_employee_deduction.p_employee_id = I_Employee_Insurance_Fact.P_employee_id and p_employee_deduction.p_Deduction_id = I_Employee_Insurance_Fact.P_Deduction_id")
			;
*/		
		sql = new StringBuffer ( "SELECT I_Employee_Insurance_Fact_ID FROM I_Employee_Insurance_Fact " + 
									" WHERE not exists ( select 1 from p_employee_deduction " +
									" where p_employee_deduction.p_employee_id = I_Employee_Insurance_Fact.P_employee_id and p_employee_deduction.p_Deduction_id = I_Employee_Insurance_Fact.P_Deduction_id ) " 
							  );
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
			ResultSet rs = pstmt.executeQuery();


			while (rs.next())
			{
				X_I_Employee_Insurance_Fact imp = new X_I_Employee_Insurance_Fact (getCtx(), rs.getInt(1), null);
				
				P_Employee_Deduction emd = new P_Employee_Deduction( getCtx(), -1, null);
//				P_Deduction Deduction = P_Deduction.get( getCtx(), imp.getP_Deduction_ID(), null);
				
				emd.setP_Deduction_ID(imp.getP_Deduction_ID());
				emd.setP_Employee_ID(imp.getP_Employee_ID());
				if ( imp.getLastName() != null)
				{
					emd.setParticipant_Name(imp.getLastName() + ", " + imp.getFirstName() );
					emd.setInsuranceParticipant(P_Employee_Deduction.INSURANCEPARTICIPANT_Conjoint);
				}
				else
				{
					emd.setInsuranceParticipant(P_Employee_Deduction.INSURANCEPARTICIPANT_Salaried);
				}
				emd.setEffectIn(TimeUtil.getDay(2007, 01, 02));
				if ( imp.getGender() != null )
				{
					if ( imp.getGender().equals("Femme"))
						emd.setGender( P_Employee_Deduction.GENDER_Woman);
					else
						emd.setGender( P_Employee_Deduction.GENDER_Man);
				}
				
				else
				{
					if ( imp.getGenderParticipant() != null)
					{
						if ( imp.getGenderParticipant().equals("Femme"))
							emd.setGender( P_Employee_Deduction.GENDER_Woman);
						else
							emd.setGender( P_Employee_Deduction.GENDER_Man);
					}
				}
					
				emd.setProtectionAmount(imp.getProtectionAmount());
				emd.setIsSmoker( false);
				if ( imp.getSmoker() != null)
					emd.setIsSmoker(imp.getSmoker().equals("Oui"));
				else if ( imp.getSmokerParticipant() != null)
					emd.setIsSmoker(imp.getSmokerParticipant().equals("Oui"));
				
				emd.setBirthDate(imp.getBirthDate());
				
				emd.save();
				 
				imp.setI_IsImported(true);
				imp.save();
				noInsert = noInsert++;
				
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
			System.err.println( "error : " + e);
		}


		
		/*
		//	Delete Old Imported
		if (m_deleteOldImported)
		{
			sql = new StringBuffer ("update I_Employee_Insurance set I_IsImported = 'N' ");
			no = DB.executeUpdate(sql.toString(), null);
		}

		//	Set Client, Org, IsActive, Created/Updated
		sql = new StringBuffer ("UPDATE i_Employee_Insurance "
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

		sql = new StringBuffer ("UPDATE I_Employee_Insurance set P_Employee_ID = ( Select P_Employee_ID from P_Employee where substring( Value, 2,5) = substring( Employee, 2,5))" );  
		DB.executeUpdate(sql.toString(), null);

		sql = new StringBuffer ("UPDATE I_Employee_Insurance set P_Profile_ID = ( Select P_Profile_ID from P_Profile where Name like '%' + Insurance and ad_client_id <> 11)" );  
		DB.executeUpdate(sql.toString(), null);


		//	Go through Records
		sql = new StringBuffer ("SELECT I_Employee_Insurance_ID FROM I_Employee_Insurance "
			+ "WHERE I_IsImported='N'").append(clientCheck);
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
			ResultSet rs = pstmt.executeQuery();


			while (rs.next())
			{

				X_I_Employee_Insurance imp = new X_I_Employee_Insurance (getCtx(), rs.getInt(1), null);

				P_Employee_Profile EmployeeProfile;

				if (m_deleteOldImported)
				{
					EmployeeProfile = P_Employee_Profile.get(getCtx(), imp.getP_Employee_ID(), imp.getP_Profile_ID(), TimeUtil.getDay(2007, 01, 01), null);
					if ( EmployeeProfile != null )
						EmployeeProfile.delete(true);
				   	
				}

				
				
				boolean isError = false;
				
				if ( imp.getP_Employee_ID() == 0)
				{
					imp.setI_ErrorMsg("Employé non trouvé");
					imp.setI_IsImported(false);
					imp.save();
					isError = true;
					commit();
				}

				if ( imp.getP_Profile_ID() == 0)
				{
					imp.setI_ErrorMsg("Profil non trouvé");
					imp.setI_IsImported(false);
					imp.save();
					isError = true;
					commit();
				}

				if ( ! isError )
				{
					EmployeeProfile = new P_Employee_Profile(getCtx(), -1, null);
					EmployeeProfile.setP_Employee_ID(imp.getP_Employee_ID());
					EmployeeProfile.setP_Profile_ID( imp.getP_Profile_ID());
					EmployeeProfile.setEffectIn(TimeUtil.getDay(2007, 01, 01));
					EmployeeProfile.setIsActive(true);
					EmployeeProfile.setIsDefault(false);
					EmployeeProfile.save();
					
					imp.setP_Employee_Profile_ID(EmployeeProfile.getP_Employee_Profile_ID());
					imp.save();

				}
				commit();
				
				imp.setI_IsImported(true);

				
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
			System.err.println( "error : " + e);
//			rollback();
		}
		
		//	Set Error to indicator to not imported
		sql = new StringBuffer ("UPDATE i_post "
			+ "SET I_IsImported='N', Updated=SysDate "
			+ "WHERE I_IsImported<>'Y'").append(clientCheck);
		
		
		no = DB.executeUpdate(sql.toString(), null);
*/
		addLog (0, null, new BigDecimal (no), "@Errors@");
		addLog (0, null, new BigDecimal (noInsert), "@Inserted@");
		addLog (0, null, new BigDecimal (noUpdate), "@Updated@");


		return "";
		
	}




}
