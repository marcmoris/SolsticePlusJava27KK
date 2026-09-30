package solstice.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;

import solstice.model.*;
import org.compiere.model.*;
import org.compiere.util.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

public class ImportEmployee extends SvrProcess
{
	/**	Client to be imported to		*/
	private int				m_AD_Client_ID = 1000004;
	/**	Delete old Imported				*/
	private boolean			m_deleteOldImported = false;

	/** Effective						*/
	private Timestamp		m_DateValue = null;

	P_Employee Employee;
	
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
			sql = new StringBuffer ("delete from p_employee where p_employee_id in ( select p_employee_id from I_Employee ) ").append(clientCheck);
			no = DB.executeUpdate(sql.toString(), null);
			log.fine("Delete Old Impored =" + no);

			sql = new StringBuffer ("update i_employee set I_IsImported = 'N' ");
			no = DB.executeUpdate(sql.toString(), null);

		}

		//	Set Client, Org, IsActive, Created/Updated
		sql = new StringBuffer ("UPDATE I_Employee "
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

/*		sql = new StringBuffer ("UPDATE I_Employee set P_WorkPlace_ID = ( select P_WorkPlace_ID From P_WorkPlace where value = dbo.trim( WORKPLACE ) and ad_client_id = 1000004 )" );  
		DB.executeUpdate(sql.toString(), null);

		sql = new StringBuffer ("UPDATE I_Employee set AD_Org_ID = ( select AD_Org_ID From AD_Org where value = Org )" );  
		DB.executeUpdate(sql.toString(), null);

//		sql = new StringBuffer ("UPDATE I_Employee set Taxation_Region_ID = ( select C_Region_ID From C_Region where name = regionName and c_country_id = 109)" );  
//		DB.executeUpdate(sql.toString(), null);

		/ *
		 	164 1 - Ontario 
			166	2 - québec 
			157 3 - BC 
			156 4 - Alberta 
			167 5 - Saskatchewan 
			158 6 - Manitoba 
			161 7 -  Nova Scotia 
			159 8 - New Brunswick 
			160 9 - New Foundland 
			165 10 - Prince Edwards Island 
			162 11 - NWT 
			168 12 - Yt 
			163 13 - Nunavut 
		 * /

		sql = new StringBuffer ("UPDATE I_Employee set Taxation_Region_ID = 164  where TaxationregionName = '1'" );  
		DB.executeUpdate(sql.toString(), null);
		sql = new StringBuffer ("UPDATE I_Employee set Taxation_Region_ID = 166  where TaxationregionName = '2'" );  
		DB.executeUpdate(sql.toString(), null);
		sql = new StringBuffer ("UPDATE I_Employee set Taxation_Region_ID = 157  where TaxationregionName = '3'" );  
		DB.executeUpdate(sql.toString(), null);
		sql = new StringBuffer ("UPDATE I_Employee set Taxation_Region_ID = 156  where TaxationregionName = '4'" );  
		DB.executeUpdate(sql.toString(), null);
		sql = new StringBuffer ("UPDATE I_Employee set Taxation_Region_ID = 167  where TaxationregionName = '5'" );  
		DB.executeUpdate(sql.toString(), null);
		sql = new StringBuffer ("UPDATE I_Employee set Taxation_Region_ID = 158  where TaxationregionName = '6'" );  
		DB.executeUpdate(sql.toString(), null);
		sql = new StringBuffer ("UPDATE I_Employee set Taxation_Region_ID = 161  where TaxationregionName = '7'" );  
		DB.executeUpdate(sql.toString(), null);
		sql = new StringBuffer ("UPDATE I_Employee set Taxation_Region_ID = 159  where TaxationregionName = '8'" );  
		DB.executeUpdate(sql.toString(), null);
		sql = new StringBuffer ("UPDATE I_Employee set Taxation_Region_ID = 160  where TaxationregionName = '9'" );  
		DB.executeUpdate(sql.toString(), null);
		sql = new StringBuffer ("UPDATE I_Employee set Taxation_Region_ID = 165  where TaxationregionName = '10'" );  
		DB.executeUpdate(sql.toString(), null);
		sql = new StringBuffer ("UPDATE I_Employee set Taxation_Region_ID = 162  where TaxationregionName = '11'" );  
		DB.executeUpdate(sql.toString(), null);
		sql = new StringBuffer ("UPDATE I_Employee set Taxation_Region_ID = 168  where TaxationregionName = '12'" );  
		DB.executeUpdate(sql.toString(), null);
		sql = new StringBuffer ("UPDATE I_Employee set Taxation_Region_ID = 163  where TaxationregionName = '13'" );  
		DB.executeUpdate(sql.toString(), null);

		sql = new StringBuffer ("UPDATE I_Employee set C_Region_ID = ( select C_Region_ID From C_Region where name = regionName and c_country_id = 109)" );  
		DB.executeUpdate(sql.toString(), null);
		commit();

		sql = new StringBuffer ("UPDATE I_Employee set C_Region_ID = ( select C_Region_ID From C_Region where name = regionName and c_country_id != 109) where I_Employee.C_Region_ID is null" );  
		DB.executeUpdate(sql.toString(), null);
		commit();

		sql = new StringBuffer ("UPDATE I_Employee set C_Activity_ID = ( select C_Activity_ID From C_Activity where value = activity)" );  
		DB.executeUpdate(sql.toString(), null);
		commit();

		sql = new StringBuffer ("UPDATE I_Employee set P_Financial_Institution_ID = ( select P_Financial_Institution_ID From P_Financial_Institution where cast( value as int ) =  financial_institution01)" );  
		DB.executeUpdate(sql.toString(), null);
		commit();
*/

		//	Go through Records
		sql = new StringBuffer ("SELECT * FROM I_Employee "
			+ "WHERE I_IsImported='N'").append(clientCheck);
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{

//				sql = new StringBuffer ("UPDATE I_Employee set Taxation_Region_ID = 166,  C_Region_ID = 166 WHERE C_Region_ID is null" );  
//				DB.executeUpdate(sql.toString(), null);

				X_I_Employee impEmp = new X_I_Employee (getCtx(), rs, null);

				
//				 A valider
//ici 
//ALTER TABLE P_Employee ADD SupplierNumber nvarchar(30)
/*
				if ( impEmp.getBusinessNumber() != null && impEmp.getBusinessNumber().equals("1"))
				{
					impEmp.setP_Employer_ID( 1000046 );
					impEmp.setP_Job_Type_ID( 1000077 );
				}
				else
				{
					impEmp.setP_Employer_ID( 1000045 );
					impEmp.setP_Job_Type_ID( 1000079 );
				}

				if ( impEmp.getACTIVITY().substring(2,4).equals("04"))
					impEmp.setP_Employer_ID(1000045);
				else
					if ( impEmp.getORG().equals("FL76"))
						impEmp.setP_Employer_ID(1000051);
					else
						impEmp.setP_Employer_ID(1000046);

				
// Temporaire load échelle salarial par la suite.				
				impEmp.setP_Occupation_Group_ID( 1000230 );
				if ( impEmp.getORG().equals("FL76"))
					impEmp.setP_Payment_Group_ID( 1000014 );
				else
					impEmp.setP_Payment_Group_ID( 1000012 );
				
				//Québec
				if ( impEmp.getTaxation_Region_ID() != 0 && impEmp.getTaxation_Region_ID() == 166)
					impEmp.setP_Holidays_Calendar_ID(1000021);
				else
					impEmp.setP_Holidays_Calendar_ID(1000018);

				if ( impEmp.getCivilStatus() != null && impEmp.getCivilStatus().equals("M"))
					impEmp.setCivilStatus(P_Employee.CIVILSTATUS_Married);
				else
					impEmp.setCivilStatus(P_Employee.CIVILSTATUS_Single);

				if ( impEmp.getisEmployeeActive().equals("A"))
					impEmp.setIsActive(true);
				else
					impEmp.setIsActive(false);

				if ( impEmp.getLanguageCode() != null && impEmp.getLanguageCode().equals("EN"))
					impEmp.setP_Language_ID(127);
				else
					impEmp.setP_Language_ID(155);

				
				impEmp.setI_IsImported(true);

				// Hardcoded
//				ici SI Département N et organisation 2AWW = syndiqué.
//				if ( impEmp.getACTIVITY().equals("3301") || impEmp.getACTIVITY().equals("3304"))
				if ( impEmp.getDepartment().equals("N") && impEmp.getORG().equals("2AWW"))
					impEmp.setP_Collective_Labour_Agr_ID(1000060);
				else
					impEmp.setP_Collective_Labour_Agr_ID(1000061);

				impEmp.setP_Distribution_ID( 1000509 );

				impEmp.setC_Country_ID(109);

				impEmp.save();
				commit();
*/				
				
				boolean isError = false;

				if ( impEmp.getGender() == null)
				{
					impEmp.setI_ErrorMsg("Ne peux pas déterminer le sexe de l'employé");
					impEmp.setI_IsImported(false);
					impEmp.save();
					isError = true;
				}

				if ( impEmp.getC_Activity_ID() == 0 )
				{
					impEmp.setI_ErrorMsg("Ne peux pas déterminer le unité administrative");
					impEmp.setI_IsImported(false);
					impEmp.save();
					isError = true;
				}

				if ( impEmp.getTaxation_Region_ID() == 0 )
				{
					impEmp.setI_ErrorMsg("Ne peux pas déterminer la province de l'employé");
					impEmp.setI_IsImported(false);
					impEmp.save();
					isError = true;
					commit();
				}
/*				
				if (  getP_Employee_ID( impEmp.getValue() ) != 0 )
				{
					impEmp.setI_ErrorMsg("Ce numéro d'employé existe déjà");
					impEmp.setI_IsImported(false);
					impEmp.save();
					isError = true;
					commit();
				}
*/
				if ( impEmp.getP_Workplace_ID() == 0 )
				{
					impEmp.setI_ErrorMsg("Ne peux pas déterminer la base l'employé");
					impEmp.setI_IsImported(false);
					impEmp.save();
					isError = true;
					commit();
				}
				if ( impEmp.getP_Collective_Labour_Agr_ID() == 0 )
				{
					impEmp.setI_ErrorMsg("Ne peux pas déterminer la convention collective de l'employé");
					impEmp.setI_IsImported(false);
					impEmp.save();
					isError = true;
					commit();
				}
/*				if ( impEmp.getP_Financial_Institution_ID() == 0 )
				{
					impEmp.setI_ErrorMsg("Ne peux pas déterminer la institution financiere de l'employé");
					impEmp.setI_IsImported(false);
					impEmp.save();
					isError = true;
					commit();
				}				
	*/			
				if ( impEmp.getBirthDate() == null )
				{
					impEmp.setI_ErrorMsg("Ne peux pas déterminer la BirthDate de l'employé");
					impEmp.setI_IsImported(false);
					impEmp.save();
					isError = true;
					commit();
				}				
				
				

				if ( ! isError )
				{
					if ( impEmp.getP_Employee_ID() != 0 )
						Employee = new P_Employee( getCtx(), impEmp.getP_Employee_ID(), null);
					else
						Employee = new P_Employee( getCtx(), -1, null);
					
					Employee.setValue( impEmp.getValue() );
					
					Employee.setAD_Org_ID( impEmp.getAD_Org_ID());
					Employee.setBirthDate( impEmp.getBirthDate() );
					
					Employee.setCivilStatus( impEmp.getCivilStatus());
						
//					Employee.setCondition(Condition);
					Employee.setDateHired( impEmp.getDateHired());
					Employee.setDateLayoff( impEmp.getDateLayoff());
					Employee.setDateProbationary(impEmp.getDateProbationary());
					Employee.setDateRehired( impEmp.getDateRehired() );
					Employee.setDateSeniority(impEmp.getDateSeniority());

//					Employee.setFirstLetter(impEmp.getFirstLetter());
					Employee.setFirstName(impEmp.getFirstName());
					Employee.setSurname(impEmp.getSurname());
					Employee.setGender(impEmp.getGender());
//	 Statut T
					Employee.setIsActive(impEmp.isActive());
						
					Employee.setIsCommingBack(false);
					Employee.setLayoffCode(impEmp.getLayoffCode());
//					Employee.setLayoffComment(impEmp.getCondition())
				
					Employee.setP_Collective_Labour_Agr_ID(impEmp.getP_Collective_Labour_Agr_ID());
					Employee.setP_Distribution_ID( impEmp.getP_Distribution_ID() );

					Employee.setP_Employer_ID( impEmp.getP_Employer_ID() );
						
					Employee.setP_Holidays_Calendar_ID( impEmp.getP_Holidays_Calendar_ID());

					Employee.setP_Holidays_Calendar_ID( impEmp.getP_Holidays_Calendar_ID());

					Employee.setP_Job_Title_ID(impEmp.getP_Job_Title_ID());
					Employee.setP_Job_Type_ID(impEmp.getP_Job_Type_ID());
					Employee.setP_Language_ID(impEmp.getP_Language_ID());
					Employee.setP_Occupation_Group_ID(impEmp.getP_Occupation_Group_ID());
					Employee.setP_Payment_Group_ID(impEmp.getP_Payment_Group_ID());
					Employee.setP_Workplace_ID(impEmp.getP_Workplace_ID());
					Employee.setPaymentType(impEmp.getPaymentType());
					if ( impEmp.getPAY_EMP_TEL1() != null ) 
						Employee.setPhone(  impEmp.getPAY_EMP_TEL1() );
					
				
					Employee.setDistribution_Booklet_ID( 1000000 );
						
					Employee.setPrintStatementEarning(true);
					Employee.setSin(impEmp.getSin() );
					Employee.setTaxation_Region_ID(impEmp.getTaxation_Region_ID());
					Employee.setC_Activity_ID( impEmp.getC_Activity_ID());
//					Employee.setP_LongTermLeave_ID( 0);
				
					if ( impEmp.getP_Employee_ID() == 0 )
					{
						Employee.setP_Employee_Manager_ID( 1103204 );
						Employee.setP_LongTermLeave_ID( 0 );
//						Employee.setP_Department_ID(getP_Department_ID( impEmp.getDepartment() ));
						
					}
			//		Employee.setP_Employee_Manager_ID( 1103203 );
						
//					Employee.setP_Department_ID( impEmp.getP_Department_ID());
					
//					Employee.setP_Department_ID(getP_Department_ID( impEmp.getDepartment() ));
//					Employee.setCostCenter(impEmp.getCOST_CENTRE());

//					Employee.setSupplierNumber( impEmp.getSupplierNumber() );
					if (Employee.save())
					{
						
/*						if ( ! Employee.isActive() && Employee.getDateLayoff() != null )
						{
							Employee.setP_LongTermLeave_ID( 1000156 );
							Employee.setLongTermLeaveDate( Employee.getDateLayoff() );
							Employee.save();
						}
*/						
						noInsert++;
					    impEmp.setP_Employee_ID( Employee.getP_Employee_ID());
					    impEmp.setI_IsImported(true);
					    impEmp.setProcessed(true);
					    impEmp.save();
					}
					else
					{
						log.warning("Employee save");
//						rollback();
//						noInsert--;
						impEmp.setI_ErrorMsg("Cannot Insert Employee");
						impEmp.setI_IsImported(false);
						impEmp.save();
						continue;
					}


					MLocation location;
					if (  Employee.getC_Location_ID() != 0 )
						location = new MLocation(getCtx(), Employee.getC_Location_ID(), null);
					else
						location = new MLocation(getCtx(), -1, null);
					location.setAD_Org_ID(0);
					location.setC_Country_ID(impEmp.getC_Country_ID());
					location.setC_Region_ID(impEmp.getC_Region_ID());
					
					location.setCity(impEmp.getPAY_EMP_VILLE().trim());
					location.setAddress1(impEmp.getPAY_EMP_RUE().trim());
					location.setAddress2(impEmp.getPAY_EMP_ADR1().trim());
					location.setAddress3(impEmp.getPAY_EMP_ADR2().trim());
					location.setAddress4(impEmp.getPAY_EMP_ADR3().trim());
					location.setPostal( impEmp.getPAY_EMP_CPOSTAL().trim() );
//					location.setPostal_Add(impEmp.getPostal_Add());
					if (!location.save() )
					{
						log.warning("Location not updated");
//						rollback();
						noInsert--;
						impEmp.setI_ErrorMsg("Cannot insert Location");
						impEmp.setI_IsImported(false);
						impEmp.save();
						commit();
					}

					Employee.setC_Location_ID(location.getC_Location_ID());
					
	//				Employee.setUserCode( impEmp.getValue() );
					
					Employee.save();
					

					P_Assignment Assignment ;
					if ( Employee.GetAssignmentPrincipal() == 0)
					{

//						if ( Employee.GetAssignmentPrincipal() != 0)
//							Assignment = new P_Assignment( getCtx(), Employee.GetAssignmentPrincipal() , null);
//						else
							Assignment = new P_Assignment( getCtx(), -1 , null);
						
						Assignment.setAD_Org_ID( Employee.getAD_Org_ID() );
						Assignment.setAssignmentType(P_Assignment.ASSIGNMENTTYPE_Primary);
						Assignment.setStartDate( Employee.getDateHired() );
						Assignment.setEndDate(null);
						Assignment.setIsActive(true);
						Assignment.setP_Employee_ID(Employee.getP_Employee_ID());
						Assignment.setP_Assignment_State_ID(1000061);
						Assignment.setP_Post_ID( impEmp.getP_Post_ID() );
//						Assignment.setP_Assignment_State_ID(P_Assignment_State_ID)
						Assignment.setValue("01");
						Assignment.save();
						
						if ( Assignment.getP_Assignment_ID() != 0 ) 
						{
							P_Assignment_Param AssignmentParam;
							
							if ( Employee.getAssignment_Param(Assignment.getStartDate(), null ) != null )
								AssignmentParam  = Employee.getAssignment_Param(Assignment.getStartDate(), null );
							else
								AssignmentParam  = new P_Assignment_Param( getCtx(), -1, null);

							AssignmentParam.setAnnual_Salary( impEmp.getAnnual_Salary() );
							AssignmentParam.setHourly_Rate( impEmp.getHourly_Rate());
							AssignmentParam.setIsOut_Of_Range(true);
							AssignmentParam.setWeekly_Hours( impEmp.getWeekly_Hours() );
							AssignmentParam.setDay_Hours(AssignmentParam.getWeekly_Hours().divide(new BigDecimal(5)).setScale(2, BigDecimal.ROUND_HALF_UP )  );
							AssignmentParam.setEffectIn(Assignment.getStartDate());
							AssignmentParam.setP_Collective_Labour_Agr_ID( Employee.getP_Collective_Labour_Agr_ID());
							AssignmentParam.setP_Occupation_Group_ID(Employee.getP_Occupation_Group_ID());
							AssignmentParam.setRemuneration_Method(P_Assignment_Param.REMUNERATION_METHOD_Hourly);
							AssignmentParam.setP_Assignment_ID(Assignment.getP_Assignment_ID());
//							AssignmentParam.setSalaryPerPay(impEmp.getSalaryPerPay());
//							AssignmentParam.setHoursPerPay(impEmp.getNormalHoursPerPay());

			// AssignmentParam.setWeekly_Hours( (impEmp.getNormalHoursPerPay().multiply( new BigDecimal( 24 )).divide( new BigDecimal( 52 ),2,BigDecimal.ROUND_HALF_UP  )));
							AssignmentParam.setDay_Hours( AssignmentParam.getWeekly_Hours().divide( new BigDecimal(5), 2, BigDecimal.ROUND_HALF_UP) );
							AssignmentParam.setIsActive(true);
							AssignmentParam.save();
							
						}

						
					}

					

//TODO folio2.					
					if ( Employee.getPaymentType().equals( P_Employee.PAYMENTTYPE_Deposit) && impEmp.getP_Financial_Institution_ID() != 0)
					{
						P_Employee_Payment_Dist PaymentDist;
						if ( P_Employee_Payment_Dist.get_Employee_Payment_Dist( Employee.getP_Employee_ID() ) != 0 )
							PaymentDist = new P_Employee_Payment_Dist( getCtx(), P_Employee_Payment_Dist.get_Employee_Payment_Dist( Employee.getP_Employee_ID() ) , null);
						else
							PaymentDist = new P_Employee_Payment_Dist( getCtx(), -1 , null);
						PaymentDist.setP_Employee_ID(Employee.getP_Employee_ID());
						PaymentDist.setFolio( impEmp.getPAY_EMP_CPT_BQ() );
						PaymentDist.setP_Financial_Institution_ID(impEmp.getP_Financial_Institution_ID());
						PaymentDist.setTransit(impEmp.getPAY_EMP_TRANSIT_BQ() );
						PaymentDist.setRate( new BigDecimal(100));
						PaymentDist.setIsActive(true);
						PaymentDist.setPriority(1);
						PaymentDist.setIsResidual(true);
						PaymentDist.save();
						
					}
					else {
						Employee.setPaymentType( P_Employee.PAYMENTTYPE_Check );
					}
					
					P_Employee_Distribution_Booklet DBooklet;
					if ( P_Employee_Distribution_Booklet.get_Employee_Distribution_Booklet(Employee.getP_Employee_ID()) != 0 )
						DBooklet = new P_Employee_Distribution_Booklet( getCtx(), P_Employee_Distribution_Booklet.get_Employee_Distribution_Booklet(Employee.getP_Employee_ID()), null);
					else
						DBooklet = new P_Employee_Distribution_Booklet( getCtx(), -1, null);
					DBooklet.setP_Employee_ID(Employee.getP_Employee_ID());
					DBooklet.setIsActive(true);
					DBooklet.setStart_Date( TimeUtil.getDay(2007, 01, 01) );
					
//TODO		
/*					
					int dist = 1000549;
					if ( impEmp.getCoordonnatrice() != null)
					{
						if ( impEmp.getCoordonnatrice().trim().equals("Fraincine Briand"))
							dist = 1000549;
						if ( impEmp.getCoordonnatrice().trim().equals("Francine Briand"))
							dist = 1000549;
						if ( impEmp.getCoordonnatrice().trim().equals("Lisette Vachon"))
							dist = 1000550;
						if ( impEmp.getCoordonnatrice().trim().equals("Nicole Dubois"))
							dist = 1000551;
						if ( impEmp.getCoordonnatrice().trim().equals("Monique Dubé"))
							dist = 1000552;
						if ( impEmp.getCoordonnatrice().trim().equals("Maryse Chateauvert")) 
							dist = 1000554;

						if ( impEmp.getCoordonnatrice().trim().equals("Caroline Levesque"))
							dist = 1000555;
						if ( impEmp.getCoordonnatrice().trim().equals("Maryse Lafond"))
							dist = 1000556;
						if ( impEmp.getCoordonnatrice().trim().equals("Claude Payette"))
							dist = 1000557;
					}
					
					DBooklet.setP_Distribution_Booklet_ID(dist);
					DBooklet.save();
*/					
/*					
					if ( impEmp.getContactName() != null )
					{
						P_Employee_Contact Contact = new P_Employee_Contact( getCtx(), -1 , null );
						Contact.setP_Employee_ID(Employee.getP_Employee_ID());
						Contact.setName( impEmp.getContactName());
//TODO phone 2 et 3						
						if ( impEmp.getCONTACTPHONE01() != null && impEmp.getCONTACTPHONE01().length() == 10)
							Contact.setPhone( "(" + impEmp.getCONTACTPHONE01().substring(0,3) + ")" + impEmp.getCONTACTPHONE01().substring(3,6) + "-" + impEmp.getCONTACTPHONE01().substring(6,10));

						Contact.setDescription( impEmp.getContactDescription());
						Contact.save();
						
					}
*/

					if ( Employee.isActive() )
					{
						
						P_Employee_Profile profile;
						
						if (  P_Employee_Profile.get_Employee_Profile( Employee.getP_Employee_ID()) != 0 )
							profile = new P_Employee_Profile( getCtx(), P_Employee_Profile.get_Employee_Profile( Employee.getP_Employee_ID()), null);
						else
							profile = new P_Employee_Profile( getCtx(), -1, null);
							
						profile.setP_Employee_ID( Employee.getP_Employee_ID());
						profile.setP_Profile_ID(1000041);
						profile.setEffectIn( Employee.getDateHired());
						profile.save();
					}
					
					
				}
				commit();
				
//				import_info_compl( impEmp);
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
		sql = new StringBuffer ("UPDATE I_Employee "
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

	public int getP_Department_ID( String value )
	{
		int id = 0;
		String sql = "Select P_Department_ID from P_Department where value = rtrim('" + value + "')";
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
			System.err.println("ImportEmployee getP_Department_ID - " + e);
		}
		
		return id;
		
	}


	/*
	public void import_info_compl( X_I_Employee impEmp)
	{

		if ( ! impEmp.isActive() )
			return;
		
		if ( impEmp.getP_Employee_ID() == 0)
			return;
		
		// Québec
//		if ( impEmp.getTaxation_Region_ID() == 166)
//		{
			
			MRegion Region = MRegion.get( getCtx(), impEmp.getTaxation_Region_ID() );
			P_Form_Template FP = P_Form_Template.getForm_Template("TD1" + Region.getName(), null);
			BigDecimal defaultAmount = getDefaultAmount( Region.getC_Region_ID(), Region.getName());
			
			if (( impEmp.getQuebecAddTax() != null && impEmp.getQuebecAddTax().compareTo(Env.ZERO) != 0 ) ||
				(impEmp.getAmountProvAnnualExemption() != null && impEmp.getAmountProvAnnualExemption().compareTo( defaultAmount ) != 0 )
				)
			{
				P_Employee_Form TP1015 = new P_Employee_Form( getCtx(), -1, null);
				TP1015.setP_Employee_ID( impEmp.getP_Employee_ID());
				// Québec.
				if ( impEmp.getTaxationRegionName().equals("2")  )
					TP1015.setP_Form_Template_ID(1000001);
				else
				{
					TP1015.setP_Form_Template_ID( FP.getP_Form_Template_ID() );
					System.out.println("TD1 Provincial - " + impEmp.getValue() );
				}
				TP1015.setEffectIn( TimeUtil.getDay(2007, 01, 01) );
				TP1015.setIsActive(true);
				TP1015.save();

				if (( impEmp.getQuebecAddTax() != null && impEmp.getQuebecAddTax().compareTo(Env.ZERO) != 0 ) )
				{
					P_Employee_Form_Detail TP1015_Detail = P_Employee_Form_Detail.getEmployee_Form_Detail( TP1015, "11");
					TP1015_Detail.setFormFieldAmount( impEmp.getQuebecAddTax() );
					TP1015_Detail.save();
				}

				if (impEmp.getAmountProvAnnualExemption() != null && impEmp.getAmountProvAnnualExemption().compareTo( Env.ZERO ) != 0 && impEmp.getAmountProvAnnualExemption().compareTo( defaultAmount ) != 0 )
				{
					P_Employee_Form_Detail TP1015_Detail = P_Employee_Form_Detail.getEmployee_Form_Detail( TP1015, "1");
					TP1015_Detail.setFormFieldAmount( impEmp.getAmountProvAnnualExemption() );
					TP1015_Detail.save();
				}
			}
//		}
		
		if ( ( impEmp.getAmountFedTax() != null && impEmp.getAmountFedTax().compareTo(Env.ZERO) != 0 )|| 
			 ( impEmp.getAmountFedAnnualExemption() != null && impEmp.getAmountFedAnnualExemption().compareTo( new BigDecimal( 8929) ) != 0 ) || 
		     ( impEmp.getAmountFedOtherTaxCredit() != null && impEmp.getAmountFedOtherTaxCredit().compareTo( Env.ZERO ) != 0 ) ||
			 ( impEmp.getAmountFedPrescAreaExmpt() != null && impEmp.getAmountFedPrescAreaExmpt().compareTo( Env.ZERO ) != 0 )
   		   )
		
		{
			P_Employee_Form TD1 = new P_Employee_Form( getCtx(), -1, null);
			TD1.setP_Employee_ID( impEmp.getP_Employee_ID());
			TD1.setP_Form_Template_ID(1000000);
			TD1.setEffectIn( TimeUtil.getDay(2007, 01, 01) );
			TD1.setIsActive(true);
			TD1.save();

			if ( impEmp.getAmountFedTax() != null && impEmp.getAmountFedTax().compareTo(Env.ZERO) != 0)
			{
				P_Employee_Form_Detail TD1_Detail = P_Employee_Form_Detail.getEmployee_Form_Detail( TD1, "L");
				TD1_Detail.setFormFieldAmount( impEmp.getAmountFedTax() );
				TD1_Detail.save();
			}
			
			if ( impEmp.getAmountFedAnnualExemption() != null && impEmp.getAmountFedAnnualExemption().compareTo( Env.ZERO ) != 0 && impEmp.getAmountFedAnnualExemption().compareTo( new BigDecimal( 8929) ) != 0 )
			{
				P_Employee_Form_Detail TD1_Detail = P_Employee_Form_Detail.getEmployee_Form_Detail( TD1, "1");
				TD1_Detail.setFormFieldAmount( impEmp.getAmountFedAnnualExemption() );
				TD1_Detail.save();
			}

			if ( impEmp.getAmountFedOtherTaxCredit() != null && impEmp.getAmountFedOtherTaxCredit().compareTo( Env.ZERO ) != 0 )
			{
				P_Employee_Form_Detail TD1_Detail = P_Employee_Form_Detail.getEmployee_Form_Detail( TD1, "A");
				TD1_Detail.setFormFieldAmount( impEmp.getAmountFedOtherTaxCredit() );
				TD1_Detail.save();
			}
				
			if ( impEmp.getAmountFedPrescAreaExmpt() != null && impEmp.getAmountFedPrescAreaExmpt().compareTo( Env.ZERO ) != 0 );
			{
				P_Employee_Form_Detail TD1_Detail = P_Employee_Form_Detail.getEmployee_Form_Detail( TD1, "A");
				TD1_Detail.setFormFieldAmount( impEmp.getAmountFedPrescAreaExmpt() );
				TD1_Detail.save();
			}
		}
		
	}
*/
	private BigDecimal getDefaultAmount( int C_Region_ID,  String C_Region_Name )
	{
		BigDecimal amount = Env.ZERO;
		
		String sql = "Select P_Tax_Provincial_TD1.Amount "
				   + " from P_Tax_Provincial_TD1, P_Tax_Provincial, P_Tax "
				   + " where P_Tax_Provincial_TD1.Value = CASE  '" + C_Region_Name + "'"
                   + "                                                 WHEN 'QC' THEN 'TP1015.01' " 
                   + "                                                   ELSE 'TD1" + C_Region_Name + ".01' "
                   + "                                                 END "
                   + "   and P_Tax_Provincial_TD1.P_Tax_Provincial_ID = P_Tax_Provincial.P_Tax_Provincial_ID "
                   + " and P_Tax_Provincial.P_Tax_ID = P_Tax.P_Tax_ID "
                   + " and P_Tax.EffectIn <= ( select max( EffectIn ) from P_Tax ) "
                   + " and P_Tax_Provincial.C_Region_ID =  " + C_Region_ID
                   ;
		
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				amount = rs.getBigDecimal(1);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("getDefaultAmount - " + e);
		}
		
		return amount;
		
	}
	
}
