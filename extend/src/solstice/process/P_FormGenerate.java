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
import java.util.logging.Level;

import org.compiere.util.DB;
import org.compiere.process.*;
import solstice.model.P_Employee;
import solstice.model.P_Employer;
import solstice.model.P_Form;
import solstice.model.P_Form_Employee;
import solstice.model.P_Payment;
import solstice.utils.PgiUtil;

/**
 * @author marmor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_FormGenerate extends SvrProcess 
{
	private int Org_ID = 0;
	private int Activity_ID = 0;

	private int Form_ID = 0;
	private int Year_ID = 0;
	private int Distribution_Booklet_ID = 0;
    private int Employee_ID = 0;
    private boolean IsRework = false;
	private int	m_inserted = 0;

	private P_Employee Employee;
	private P_Employer Employer;
	private P_Form Form;

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#prepare()
	 */
	protected void prepare() 
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("P_Employee_ID"))
				Employee_ID = para[i].getParameterAsInt();

			else if (name.equals("AD_Org_ID"))
				Org_ID = para[i].getParameterAsInt();
			else if (name.equals("C_Activity_ID"))
				Activity_ID = para[i].getParameterAsInt();
			
			else if (name.equals("Rework"))
			    IsRework = "Y".equals(para[i].getParameter());
			else if (name.equals("P_Year_ID"))
				Year_ID = para[i].getParameterAsInt();
			else if (name.equals("P_Form_ID"))
				Form_ID = para[i].getParameterAsInt();
			else if (name.equals("P_Distribution_Booklet_ID"))
				Distribution_Booklet_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	protected String doIt() throws Exception 
	{
	    String trxName = null; //Trx.createTrxName();
		log.log(Level.INFO, "doIt - P_Form_ID         =" + Form_ID);
		log.log(Level.INFO, "doIt - P_Year_ID         =" + Year_ID);
		log.log(Level.INFO, "doIt - P_Employee_ID     =" + Employee_ID);

		if (Year_ID == 0 || Form_ID == 0 )
		{
			log.log(Level.WARNING, "doIt - Year Parameter not found");
			return " ** Error - Error with Parameter ";
		}

		Form = P_Form.get( getCtx (), Form_ID , trxName);

		String sql = null;
		
		if (Form.getValue().trim().equals("T4") || Form.getValue().trim().equals("T4A") )
		{
			sql = "SELECT * FROM ( Select AD_ORG_ID, P_Employee_ID, P_Employer_ID, Taxation_Region_ID , min( P_Payment.P_Period_ID ) tri From P_Payment " 
                + " WHERE P_Year_ID = " + Year_ID;
		}
		else if (Form.getValue().trim().equals("R1") || Form.getValue().trim().equals("R2") )
		{
			sql = "Select AD_Org_ID, P_Employee_ID From P_Payment " 
                + " WHERE ( Taxation_Region_ID = 166 " 
/*                + " OR Exists( Select 1 FROM P_Payment_Taxable_Benefit, P_Taxable_Benefit  Where P_Payment_Taxable_Benefit.P_Payment_ID = P_Payment.P_Payment_ID AND P_Taxable_Benefit.P_Taxable_Benefit_ID = P_Payment_Taxable_Benefit.P_Taxable_Benefit_ID and P_Taxable_Benefit.C_Region_ID = 166 ) " 
                + " OR exists( Select 1 From P_Payment_Deduction, P_Deduction " 
        		+ "		WHERE P_Payment_Deduction.P_Deduction_ID = P_Deduction.P_Deduction_ID"
        		+ "		  AND P_Payment_Deduction.P_Payment_ID = P_Payment.P_Payment_ID"
        		+ "		  AND P_Deduction.C_Region_ID = 166 ) 
*/        		 
                + " ) "
                + " AND P_Year_ID = " + Year_ID;
		}
		else
		{
			sql = "Select AD_Org_ID, P_Employee_ID From P_Payment " 
                + " WHERE P_Year_ID = " + Year_ID;
		}

		if (Org_ID > 0)
			sql += "   AND AD_Org_ID = " + Org_ID;
		if (Activity_ID > 0)
			sql +=  "   AND C_Activity_ID = " + Activity_ID;

		if (Employee_ID != 0)
			sql += " AND P_Employee_ID=" + Employee_ID;
		
//		sql += " AND P_Employee_ID IN ( 1002161,1002240,1002316,1002634,1002689,1002777,1002838,1001625,1002846,1001384,1001221,1002395,1002431,1002645,1002678,1002691,1001447,1002780,1002747,1002122,1002413,1002443,1002480,1002806,1002873 ) "; 

		if (Distribution_Booklet_ID != 0)
			sql += " AND P_Employee_ID IN ( SELECT P_EMPLOYEE_ID FROM P_EMPLOYEE WHERE P_Distribution_Booklet_ID=" + Distribution_Booklet_ID + " ) ";
		
//		if (Payment_Group_ID != 0)
//			sql += " AND P_Payment_Group_ID=" + Payment_Group_ID;
		//
//ici
//		sql += "and P_Employee_ID in ( select p_employee_id from p_form_employee where p_form_Id = 1000102 group by p_employee_id having count(*) > 1 )";
		
		if (Form.getValue().trim().equals("T4") || Form.getValue().trim().equals("T4A") )
		{
			sql += " GROUP BY AD_Org_ID, P_Employee_ID, P_Employer_ID, Taxation_Region_ID "; 
		}
		else
		{
			sql += " GROUP BY AD_Org_ID, P_Employee_ID"; 
		}
/*
		//
		// Gestion du second T4 si l'employé à payer du RRQ en étant dans un autre province que Québec.
		//
		if (Form.getValue().trim().equals("T4") )
		{
			sql += " UNION Select P_Employee_ID, P_Employer_ID, 166, min( P_Payment.P_Period_ID ) tri From P_Payment " 
                + " WHERE P_Year_ID = " + Year_ID;

			if (Org_ID > 0)
				sql += "   AND AD_Org_ID = " + Org_ID;
			if (Activity_ID > 0)
				sql +=  "   AND C_Activity_ID = " + Activity_ID;

			if (Employee_ID != 0)
				sql += " AND P_Employee_ID=" + Employee_ID;
//			sql += "and P_Employee_ID in ( select p_employee_id from p_form_employee where p_form_Id = 1000102 group by p_employee_id having count(*) > 1 )";
			
			sql += " AND exists( Select 1 From P_Payment_Deduction, P_Deduction " 
				+ "		WHERE P_Payment_Deduction.P_Deduction_ID = P_Deduction.P_Deduction_ID"
				+ "		  AND P_Payment_Deduction.P_Payment_ID = P_Payment.P_Payment_ID"
				+ "		  AND P_Deduction.C_Region_ID = 166 )"
			    + " Group By P_Employee_ID, P_Employer_ID  ) DETAIL "
			    ;
			
		}
*/		
		if (Form.getValue().trim().equals("T4") || Form.getValue().trim().equals("T4A") )
		{
			sql += " ) DETAIL ";
		}
		
		//2016-11-04 modifier pour toujours genere formulaire du Quebec en premier.
		// Order by P_Period_ID est la pour la gestion du maximum a l'assurance emploie.
		if (Form.getValue().trim().equals("T4") || Form.getValue().trim().equals("T4A") )
		{
//			sql += " Order By P_Employee_ID, CASE WHEN Taxation_Region_ID = 166 THEN 1 ELSE Taxation_Region_ID END, tri ";
			sql += " Order By P_Employee_ID, tri , Taxation_Region_ID";
		}

		else
		{
			sql += " Order By P_Payment.P_Employee_ID, min( P_Payment.P_Period_ID ) ";
		}

		

		
		PreparedStatement pstmt = null;

		int employerID = 0;
		int Taxation_Region_ID = 0;
		try
		{
			boolean r_exist = false; 
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			P_Form_Employee Form_Employee;
			
			int employeeID = 0;
			
			while (rs.next ())
			{
				log.info("******************************************************************"  );
				Employee = P_Employee.get( getCtx (), rs.getInt("P_Employee_ID") , trxName);
				if (Form.getValue().trim().equals("T4") )
				{	
					Employer = P_Employer.get( getCtx (), rs.getInt("P_Employer_ID") , trxName);
					employerID = rs.getInt("P_Employer_ID");
					Taxation_Region_ID = rs.getInt( "Taxation_Region_ID");
				}

				if (Form.getValue().trim().equals("T4A") )
				{	
					Employer = P_Employer.get( getCtx (), rs.getInt("P_Employer_ID") , trxName);
					employerID = rs.getInt("P_Employer_ID");
					Taxation_Region_ID = rs.getInt( "Taxation_Region_ID");
				}

				log.info("FormGenerate - employee - " + Employee.getValue () + " " + Employee.getName () );
				r_exist = P_Form_Employee.exists ( Form.getP_Form_ID(), Year_ID,  Employee.getP_Employee_ID(), employerID, Taxation_Region_ID );
					
				if ( r_exist && IsRework )
				{
					if ( rs.getInt("P_Employee_ID") != employeeID)
					{
						P_Form_Employee.deleteAll( Form.getP_Form_ID(), Year_ID,  Employee.getP_Employee_ID(), employerID, Taxation_Region_ID );
						employeeID = rs.getInt("P_Employee_ID");
					}
					
					P_Form_Employee.delete( Form.getP_Form_ID(), Year_ID,  Employee.getP_Employee_ID(), employerID, Taxation_Region_ID );
//				    r_exist = false;
					// on refait la validation, car si le formulaire est générer, on ne peu pas le détruire et le regénérer.
					r_exist = P_Form_Employee.exists ( Form.getP_Form_ID(), Year_ID,  Employee.getP_Employee_ID(), employerID, Taxation_Region_ID );
				}
				    
				if ( r_exist == false )
				{
					Form_Employee = new P_Form_Employee( getCtx () , -1, trxName);
					Form_Employee.setAD_Org_ID( rs.getInt( "AD_Org_ID") );
					Form_Employee.setP_Employee_ID( Employee.getP_Employee_ID());
					Form_Employee.setP_Form_ID( Form.getP_Form_ID() );
					Form_Employee.setP_Year_ID( Year_ID );
					
					if (Form.getValue().trim().equals("T4") )
					{
						Form_Employee.setP_Employer_ID( employerID );
						Form_Employee.setTaxation_Region_ID( Taxation_Region_ID );
					}

					if (Form.getValue().trim().equals("T4A") )
					{
						Form_Employee.setP_Employer_ID( employerID );
						Form_Employee.setTaxation_Region_ID( Taxation_Region_ID );
					}

					/*
					if (Form.getValue().trim().equals("T4A"))
					{
						Form_Employee.setP_Employer_ID(Employee.getP_Employer_ID());
					}
					
					if (Form.getValue().trim().equals("R1") || Form.getValue().trim().equals("R2") || Form.getValue().trim().equals("R3"))
					{
						Form_Employee.setFormNumber(GetFormEmployeeNumber());
						Form_Employee.setFacsimileNumber(GetFormEmployeeFacSimileNumber());
					}
					
					Form_Employee.setFormType( "O" );
					Form_Employee.setIsGenerated( false );
					Form_Employee.setIsPrinted( false );
*/
					// SM 2007-11-07
					// J'ai ajouté taxTable au save_generate
					Form_Employee.save_generate( );
					
//					calcul_form( Form_Employee , trxName);

					if ( Form_Employee.getTotal().compareTo( new BigDecimal(0)) == 0)
					{
						Form_Employee.delete(true);
					}
					else 
					{
                           // Generate a form number for Releve 1, 2 and 3
						Form_Employee.setFormNumber(Form_Employee.GetFormEmployeeNumber( Form ));
						Form_Employee.save();

						m_inserted++;
					}

				}
				log.info("******************************************************************"  );
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, "doIt - " + sql, e);
		}
		return "@Inserted@ = " + m_inserted;
	}
	
	
}
