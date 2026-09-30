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
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import solstice.model.P_Employee;
import solstice.model.P_Assignment_Param;
import solstice.model.P_Frequency;
import solstice.model.P_Payment_Group;
import solstice.model.P_Salary_Scale;
import solstice.model.P_Salary_Scale_Detail;
import solstice.model.X_P_Assignment_Param;
import solstice.model.P_Period;
import solstice.utils.PgiUtil;


/**
 * @author vivdun01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_InOutAssignment_Indexation extends SvrProcess 
{
	
	private int iP_Collective_Labour_Agr_ID = 0;
	private int iP_Payment_Group_ID = 0;
	private int iP_Occupation_Group_ID = 0;
	private int iP_Post_ID = 0;
	private int iP_Job_Title_ID = 0;
	private int iAD_PInstance_ID = 0;
	private int iP_Salary_Class_ID = 0;
	private Timestamp Old_Scale_Date ;
	private Timestamp New_Scale_Date ;
	private Timestamp Effect_In;

	private P_Salary_Scale OldSalaryScale; 
	private P_Salary_Scale NewSalaryScale; 
    private P_Period Period ;
    private int numInserted = 0;
	private String m_Format = "PDF";
	private BigDecimal percent = new BigDecimal(2.5);
		
	protected void prepare()
	{
		iAD_PInstance_ID=getAD_PInstance_ID();
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("P_Collective_Labour_Agr_ID"))
				iP_Collective_Labour_Agr_ID = para[i].getParameterAsInt();
			else if (name.equals("P_Payment_Group_ID"))
				iP_Payment_Group_ID = para[i].getParameterAsInt();
			else if (name.equals("P_Occupation_Group_ID"))
				iP_Occupation_Group_ID = para[i].getParameterAsInt();
			else if (name.equals("P_Job_Title_ID"))
				iP_Job_Title_ID = para[i].getParameterAsInt();
			else if (name.equals("P_Post_ID"))
				iP_Post_ID = para[i].getParameterAsInt();
			else if (name.equals("P_Salary_Class_ID"))
				iP_Salary_Class_ID = para[i].getParameterAsInt();
			else if (name.equals("OldScaleDate"))
				Old_Scale_Date = (Timestamp) para[i].getParameter();
			else if (name.equals("NewScaleDate"))
				New_Scale_Date = (Timestamp) para[i].getParameter();
			else if (name.equals("EffectIn"))
				Effect_In = (Timestamp) para[i].getParameter();
            else if (name.equals("CrystalFormat"))
				m_Format = (String)para[i].getParameter();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		
		//ensuite, on insere un nouveau parametre, AD_PInstance_ID
		
        int Client_ID = Env.getAD_Client_ID(getCtx());
        int Org_ID = Env.getAD_Org_ID(getCtx());
        int User_ID = Env.getAD_User_ID(getCtx());

        String sql = "INSERT INTO AD_PInstance_PARA (AD_PInstance_id, seqno, ParameterName, P_String, P_String_To, P_Number, P_Number_To, P_Date, P_Date_To, Info, Info_To, Ad_Client_id, Ad_Org_id, Created, createdby, Updated, UpdatedBy, Isactive) " +
        "Select " + iAD_PInstance_ID + ", " +
        "(Select Max(seqno) + 1 from AD_PInstance_Para where AD_PInstance_id = " + iAD_PInstance_ID + "), " +
        "'AD_PInstance_ID', null, null, " + iAD_PInstance_ID + ", null, null, null, null, null, " + Client_ID + ", " + Org_ID + ", GetDate(), " + User_ID + ", GetDate(), " + User_ID + ", 'Y'";

		PreparedStatement pstmp = null;
		try
		{
		pstmp = DB.prepareStatement( sql, null);
		pstmp.execute();
		//pstmp = DB.
		}
		catch (Exception e)
		{
		log.log(Level.SEVERE, "LauncherRemunerationState.Insert", e);
		}
		// prepare
	}
	
    protected String Indexation()
    {
		
		String Step = null;
		
	    String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String msg = "";
		
		//recherche les affectations selon les paramètres recu; 
		// *** Select the concerned assignments
		sql = "SELECT P_Assignment_Param.P_Assignment_Param_ID, " 
				+ " P_Assignment_Param.P_Assignment_ID, " 
				+ " P_Assignment_Param.P_Salary_Scale_Detail_ID,  "
				+ " P_Employee.P_Employee_ID "
				+ " FROM P_Assignment, P_Assignment_Param, P_Employee "  
				+ " WHERE P_Assignment_Param.IsActive = 'Y' "
//				2008-04-03 M.M
				+ " AND P_Assignment_Param.IsOut_Of_Range = 'N' "
//
				+ " AND P_Assignment.IsActive = 'Y' "
				+ " AND P_Employee.IsActive = 'Y' "
				+ " AND P_Employee.P_Employee_ID = P_Assignment.P_Employee_ID"

				//2008-04-26 M.M L'affectation doit être active selon les date de début et de fin.
				+ " AND ( P_Assignment.EndDate IS NULL OR P_Assignment.EndDate >= " + DB.TO_DATE( Period.getStartDate()) + "  )"

				+ " AND P_Assignment_Param.EffectIn <= ? "
				+ " AND (P_Assignment_Param.EffectTo is null OR P_Assignment_Param.EffectTo >= ?) "
				+ " AND P_Assignment.P_Assignment_ID = P_Assignment_Param.P_Assignment_ID "
				+ " AND P_Assignment_Param.P_Salary_Scale_ID = " + OldSalaryScale.getP_Salary_Scale_ID()
				;
		
				//Multiple calendrier de paie
		        sql += " AND P_Employee.P_Payment_Group_ID = " + iP_Payment_Group_ID;
		

//				if (PgiUtil.getSolsticeParameter(Env.getCtx(), "Client").equals("CHL") == false )
//				{
//					sql += " AND P_Employee.P_Collective_Labour_Agr_ID = " + OldSalaryScale.getP_Collective_Labour_Agr_ID();
//				}
				

		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setTimestamp(1, Period.getEndDate());
			pstmt.setTimestamp(2, Period.getEndDate());
			rs = pstmt.executeQuery();
			
		  	// Loop in all assignments		
			while (rs.next())
			{
				P_Employee Employee = P_Employee.get( Env.getCtx(), rs.getInt("P_Employee_ID"), null);

				log.log(Level.INFO, "Employee : " + Employee.getValue() + " " + Employee.getName());

				P_Salary_Scale_Detail OldSalaryScaleDetail = P_Salary_Scale_Detail.get( Env.getCtx(), rs.getInt("P_Salary_Scale_Detail_ID"), null );
				P_Salary_Scale_Detail NewSalaryScaleDetail = P_Salary_Scale_Detail.getWithStep( Env.getCtx(), NewSalaryScale.getP_Salary_Scale_ID(), OldSalaryScaleDetail.getStep(), null  );

				
				String trxName = null; // Trx.createTrxName();
				
				//on va cherche l'echelon salariale pour mettre le nouveau salaire
				if(NewSalaryScaleDetail == null)
				{
					msg = msg + " Nouvel échelon inexistant pour l'employé : " + Employee.getValue() + " " + Employee.getName() + "\n";
				}
				else
				{
					// Update current assignment
					P_Assignment_Param assignment_Param = P_Assignment_Param.get(getCtx(), rs.getInt("P_Assignment_Param_ID"), trxName);

					
					P_Assignment_Param newAssignment_Param = P_Assignment_Param.getWithEffectIn(getCtx(), rs.getInt("P_Assignment_ID"), Effect_In, trxName);
					if ( newAssignment_Param == null )
					{

						newAssignment_Param = new P_Assignment_Param(getCtx(), -1, trxName);
						newAssignment_Param.setAD_Org_ID(assignment_Param.getAD_Org_ID());
						
						newAssignment_Param.setCumulatedHours(assignment_Param.getCumulatedHours());
						newAssignment_Param.setDay_Hours(assignment_Param.getDay_Hours());
						
						newAssignment_Param.setEffectIn(Effect_In);
						
						newAssignment_Param.setRemuneration_Method(assignment_Param.getRemuneration_Method());
						newAssignment_Param.setWeekly_Hours(assignment_Param.getWeekly_Hours());
						
						//variable pour sauvegarder le salaire, puisque getAnnualSalary() n'est pas fiable (dependant de hors taux, hors echelle, etc)
						X_P_Assignment_Param baseAssParam = new X_P_Assignment_Param(Env.getCtx(), assignment_Param.getP_Assignment_Param_ID(), trxName);
						BigDecimal oldAnnualSalary;

		            	BigDecimal Annual_Increase = null;
		        		Annual_Increase = P_Payment_Group.getAnnual_Increase( Env.getCtx(), Employee.getP_Employee_ID(), null);

						if(newAssignment_Param.getRemuneration_Method().equals(P_Assignment_Param.REMUNERATION_METHOD_Annual) == true)
						{
							oldAnnualSalary = baseAssParam.getAnnual_Salary();
							if ( NewSalaryScale.getScale_Type().equals( P_Salary_Scale.SCALE_TYPE_AccordingToLimits))
							{
								BigDecimal newSalary = oldAnnualSalary.add( oldAnnualSalary.multiply( percent.divide(new BigDecimal(100), 8, BigDecimal.ROUND_HALF_UP) ) ).setScale(2, BigDecimal.ROUND_HALF_UP);
								
								if ( newSalary.compareTo( NewSalaryScaleDetail.getSalary_Limit_Max() ) > 0)
									newAssignment_Param.setAnnual_Salary( NewSalaryScaleDetail.getSalary_Limit_Max() );
								else
									newAssignment_Param.setAnnual_Salary( newSalary );
							}
							else
							{
								newAssignment_Param.setAnnual_Salary(NewSalaryScaleDetail.getAnnual_Salary());
							}

							BigDecimal HourlyRate = Env.ZERO;

							//+2011.05.09 - 1372 Indexation Salarial ne calcul pas le bon taux horaire pour une paie avec une périodicité de 24 période.
							P_Period Period = P_Period.getOpenPeriodWithPaymentGroup(Env.getCtx(), Employee.getP_Payment_Group_ID(), null);
				    		P_Frequency Frequency = P_Frequency.get(Env.getCtx(), Period.getP_Frequency_ID(), null ); 

				            // Bimensuel
				        	if ( Frequency.getNumberOfPeriod() == 24)
				        	{
				        		BigDecimal NumberOfPeriod = new BigDecimal( Frequency.getNumberOfPeriod() );
				        		BigDecimal HoursPerPay = newAssignment_Param.getWeekly_Hours().multiply( Annual_Increase ).divide(NumberOfPeriod,2,BigDecimal.ROUND_HALF_UP);
				        		if ( HoursPerPay.compareTo(Env.ZERO) != 0)
				        			HourlyRate = newAssignment_Param.getAnnual_Salary().divide(HoursPerPay, 8, BigDecimal.ROUND_HALF_UP).divide( new BigDecimal(24) , 8, BigDecimal.ROUND_HALF_UP);
				        	}
				        	else
				        	{
				                HourlyRate = newAssignment_Param.getAnnual_Salary().divide( Annual_Increase , 8, BigDecimal.ROUND_HALF_UP).divide(newAssignment_Param.getWeekly_Hours(), 8, BigDecimal.ROUND_HALF_UP);
				        	}
				        	
				        	HourlyRate = HourlyRate.setScale(4, BigDecimal.ROUND_HALF_UP);
				        	//-2011.05.09
							newAssignment_Param.setHourly_Rate( HourlyRate );
						}
						else
						{
							oldAnnualSalary = baseAssParam.getHourly_Rate();
							if ( NewSalaryScale.getScale_Type().equals( P_Salary_Scale.SCALE_TYPE_AccordingToLimits))
							{
								BigDecimal newHourlyRate = baseAssParam.getHourly_Rate().add( baseAssParam.getHourly_Rate().multiply( percent.divide(new BigDecimal(100), 8, BigDecimal.ROUND_HALF_UP) ) ).setScale(2, BigDecimal.ROUND_HALF_UP);
								
								if ( newHourlyRate.compareTo( NewSalaryScaleDetail.getSalary_Limit_Max() ) > 0)
									newAssignment_Param.setHourly_Rate( NewSalaryScaleDetail.getSalary_Limit_Max());
								else
									newAssignment_Param.setHourly_Rate( newHourlyRate );
							}
							else
							{
								newAssignment_Param.setHourly_Rate(NewSalaryScaleDetail.getHourly_Rate());
							}

							newAssignment_Param.setAnnual_Salary(NewSalaryScaleDetail.getHourly_Rate().multiply(newAssignment_Param.getWeekly_Hours().multiply( Annual_Increase )).setScale(4, BigDecimal.ROUND_HALF_UP));
//							newAssignment_Param.setAnnual_Salary(NewSalaryScaleDetail.getHourly_Rate().multiply(newAssignment_Param.getWeekly_Hours().multiply(new BigDecimal(52.18))).setScale(4, BigDecimal.ROUND_HALF_UP));
						}
						
						newAssignment_Param.setIsActive(true);
						newAssignment_Param.setIsOut_Of_Range(assignment_Param.isOut_Of_Range());
						newAssignment_Param.setIsOut_Of_Rate(assignment_Param.isOut_Of_Rate());
						newAssignment_Param.setOut_Of_Rate_Contractual(assignment_Param.getOut_Of_Rate_Contractual());
						newAssignment_Param.setOut_Of_Rate_Salary(assignment_Param.getOut_Of_Rate_Salary());
						newAssignment_Param.setP_Assignment_ID(assignment_Param.getP_Assignment_ID());
						newAssignment_Param.setP_Collective_Labour_Agr_ID(assignment_Param.getP_Collective_Labour_Agr_ID());
						newAssignment_Param.setP_Job_Title_ID(assignment_Param.getP_Job_Title_ID());
						newAssignment_Param.setP_Occupation_Group_ID(assignment_Param.getP_Occupation_Group_ID());
						newAssignment_Param.setP_Salary_Class_ID(assignment_Param.getP_Salary_Class_ID());
						if ( NewSalaryScaleDetail.getP_Salary_Scale_Detail_ID() != 0)
							newAssignment_Param.setP_Salary_Scale_Detail_ID(NewSalaryScaleDetail.getP_Salary_Scale_Detail_ID());
						else 
							log.log(Level.SEVERE, "Échelle salarial non trouvé : " + Employee.getValue() + " " + Employee.getName() );
						
						newAssignment_Param.setP_Salary_Scale_ID(NewSalaryScaleDetail.getP_Salary_Scale_ID());
						newAssignment_Param.setP_Schedule_ID(assignment_Param.getP_Schedule_ID());

						newAssignment_Param.setDaily_Salary(assignment_Param.getDaily_Salary());
						newAssignment_Param.setDay_Hours(assignment_Param.getDay_Hours());

						//avant de sauvegarder le nouveau parametre d'affeectation, on verifie pour savoir 
						//si l'employee est hors-taux
						if (PgiUtil.getSolsticeParameter(Env.getCtx(), "Client").equals("krispy") == false )
						{
							if(newAssignment_Param.isOut_Of_Rate() == true)
							{
								//on fait un produit croisée pour aller chercher le % d'augmentation
								// vieux salaire / nouveau salaire => 100% / 100% + % d'augmentation
								BigDecimal percentage = NewSalaryScaleDetail.getAnnual_Salary().setScale(4).multiply(new BigDecimal(100.0000));
								percentage = percentage.setScale(4).divide(oldAnnualSalary, 4, BigDecimal.ROUND_HALF_UP);
								percentage = percentage.setScale(4).subtract(new BigDecimal(100.0000));
								//on applique le % sur le salaire hors taux
								BigDecimal amount = newAssignment_Param.getOut_Of_Rate_Salary().setScale(4).multiply(percentage);
								amount = amount.divide(new BigDecimal(100.0000), 4, BigDecimal.ROUND_HALF_UP);
								//on divise le montant par 2 (50% dans un champ, 50% dans l'autre)
								amount = amount.divide(new BigDecimal(2.0000), 4, BigDecimal.ROUND_HALF_UP);
								//de ce montant, on en donne la moitié au salaire hors-taux, 
								//et l'Autre moitié au salaire forfaitaire hors-taux
								newAssignment_Param.setOut_Of_Rate_Salary(newAssignment_Param.getOut_Of_Rate_Salary().setScale(4).add(amount));
								newAssignment_Param.setOut_Of_Rate_Contractual(newAssignment_Param.getOut_Of_Rate_Contractual().setScale(4).add(amount));
							}
							else
							{
								newAssignment_Param.setOut_Of_Rate_Salary(assignment_Param.getOut_Of_Rate_Salary());
								newAssignment_Param.setOut_Of_Rate_Contractual(assignment_Param.getOut_Of_Rate_Contractual());
							}
						}
						
						if(newAssignment_Param.save() == false)
						{
							log.log(Level.SEVERE,"Erreur lors de la sauvegarde du paramètre d'affectation (id de l'affectation = '"+assignment_Param.getP_Assignment_ID()+"')." ); 
							return "Erreur lors de la sauvegarde du paramètre d'affectation (id de l'affectation = '"+assignment_Param.getP_Assignment_ID()+"').";
						}
						else
						{
							numInserted++;
							//apres insertion dans affectation param,
							//on insere dans table temporaire pour rapport d'Execution
							
							//Pour le solde (CreditsSld), le champ necessaire a ce calcul (P_Salary_Scale.P_Credits_ID est toujours a null dans la BD)
							String sqlInsert = "INSERT INTO P_T_Assignment_Pay_Increment ( AD_PINSTANCE_ID, P_Assignment_ID, OLD_Salary_Scale_Detail_ID , NEW_Salary_Scale_Detail_ID , CreditsSld )  VALUES ("+iAD_PInstance_ID+", "+newAssignment_Param.getP_Assignment_ID()+" , "+rs.getInt("P_Salary_Scale_Detail_ID")+", "+NewSalaryScaleDetail.getP_Salary_Scale_Detail_ID() +", 0 )";
							DB.executeUpdate(sqlInsert, null);
						}

					}
					
				}
				
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, "P_InOutAssignment_Indexation - doIt - " + sql, e);
		}

		return msg;
    }


	protected String doIt() throws Exception
	{

		String sqlDelete = "DELETE FROM P_T_Assignment_Pay_Increment ";
		DB.executeUpdate(sqlDelete, null);

	    String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

	    String sql2 = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs2 = null;

		String msg = "";
		Period = P_Period.getOpenPeriodWithPaymentGroup( Env.getCtx(),  iP_Payment_Group_ID, null);

		//recherche les affectations selon les paramètres recu; 
		// *** Select the concerned assignments
		sql = "SELECT P_Salary_Scale_ID "
				+ " FROM P_Salary_Scale "  
				+ " WHERE P_Salary_Scale.IsActive = 'Y' "
				+ " AND P_Salary_Scale.ScaleDate = " + DB.TO_DATE( this.Old_Scale_Date );

		
//		if (PgiUtil.getSolsticeParameter(Env.getCtx(), "Client").equals("CHL") == false )
		sql += " AND P_Salary_Scale.P_Collective_Labour_Agr_ID = " + iP_Collective_Labour_Agr_ID;

		if(iP_Occupation_Group_ID != 0)
			sql += " AND P_Salary_Scale.P_Occupation_Group_ID = " + iP_Occupation_Group_ID;
		if(iP_Post_ID != 0)
			sql += " AND  P_Salary_Scale.P_Post_ID = " + iP_Post_ID ;
		if(iP_Job_Title_ID != 0)
			sql += " AND ( P_Salary_Scale.P_Job_Title_ID = " + iP_Job_Title_ID + " OR exists( select 1 from P_Salary_Scale_Job_Title e Where e.P_Job_Title_ID=" + iP_Job_Title_ID + " and e.P_Salary_Scale_ID = P_Salary_Scale.P_Salary_Scale_ID )) ";
		if(iP_Salary_Class_ID != 0)
			sql += " AND ( P_Salary_Scale.P_Salary_Class_ID = " + iP_Salary_Class_ID ;
				
						


		try
		{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
		  	// Loop in all assignments		
			while (rs.next())
			{
				OldSalaryScale = P_Salary_Scale.get( Env.getCtx(), rs.getInt("P_Salary_Scale_ID"), null);

				sql2 = "SELECT P_Salary_Scale_ID "
					+ " FROM P_Salary_Scale "  
					+ " WHERE P_Salary_Scale.IsActive = 'Y' "
					+ " AND P_Salary_Scale.ScaleDate = " + DB.TO_DATE( this.New_Scale_Date )
				    + " AND P_Salary_Scale.P_Occupation_Group_ID = " + OldSalaryScale.getP_Occupation_Group_ID()
//KRISPY
				    + " AND P_Salary_Scale.P_Post_ID = " + OldSalaryScale.getP_Post_ID() 
				    + " AND P_Salary_Scale.P_Collective_Labour_Agr_ID = " + OldSalaryScale.getP_Collective_Labour_Agr_ID()
				    ;

/*				if (PgiUtil.getSolsticeParameter(Env.getCtx(), "Client").equals("CHL") == false )
					sql2 += " AND P_Salary_Scale.P_Collective_Labour_Agr_ID = " + OldSalaryScale.getP_Collective_Labour_Agr_ID()
						 + " AND ( P_Salary_Scale.P_Job_Title_ID = " + OldSalaryScale.getP_Job_Title_ID() + " OR P_Salary_Scale.P_Job_Title_ID IS NULL ) "
						 + " AND ( P_Salary_Scale.P_Salary_Class_ID = " + OldSalaryScale.getP_Salary_Class_ID() + " OR P_Salary_Scale.P_Salary_Class_ID IS NULL )";
*/
				pstmt2 = DB.prepareStatement(sql2, null);
				rs2 = pstmt2.executeQuery();

				if ( rs2.next())
				{
					NewSalaryScale = P_Salary_Scale.get( Env.getCtx(), rs2.getInt("P_Salary_Scale_ID"), null);
					msg = Indexation();
				}
			}
			
			rs.close();
			pstmt.close();
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, "P_InOutAssignment_Indexation - doIt - " + sql, e);
		}
			
		
		
		
		//Ne reste plus qu'a lancer le rapport
/*		
		String srvUrl = PgiUtil.getSolsticeParameter(Env.getCtx(), "WebAppURL");
		int l_Role = Env.getAD_Role_ID(Env.getCtx());
        String repUrl = srvUrl+"Crystal/reports.jsp?AD_PInstance_ID=" + iAD_PInstance_ID + "&Format=" + m_Format + "&Ad_Role_ID=" + l_Role;
	    Env.startBrowser(repUrl);
*/		
		int l_Role = Env.getAD_Role_ID(Env.getCtx());
        ReportLauncher.callReport( iAD_PInstance_ID, l_Role, m_Format, getParameter() );

		return "Procédure exécutée avec succès, "+numInserted+" nouvels enregistrements. \n" + msg;
	}
}
