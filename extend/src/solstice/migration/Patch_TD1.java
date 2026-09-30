package solstice.migration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.compiere.Compiere;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

import solstice.model.P_Employee_Form;
import solstice.model.P_Form_Template;
import solstice.model.P_Region;
import solstice.model.P_Employee;

public class Patch_TD1 {

	Properties ctx = Env.getCtx();

	/** Transaction			*/
	private String trxName = null; // Trx.createTrxName();

	
	/**
	 * 	Generate PO Class
	 * 	@param AD_Table_ID table id
	 * 	@param directory directory with \ or / at the end.
	 * 	@param packageName package name
	 * @throws SQLException
	 */
	public Patch_TD1 () 
	{
		Env.setContext( ctx, "#AD_Client_ID", 1000004);
		Env.setContext( ctx, "#AD_Org_ID", 0);

		int count = p_employee_form_fed();
		System.out.println("p_employee_form migrated = " + count);

		count = p_employee_form_prov();
		System.out.println("p_employee_form migrated = " + count);

		try 
		{
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
		
	public int p_employee_form_fed()
	{
		int count = 0;
		
		String sql = "select * from p_employee where ad_client_id = 1000004 and isactive = 'Y' and not exists ( select 1 from P_Employee_Form where p_form_template_id = 1000000 and P_Employee.P_Employee_ID = P_Employee_Form.P_Employee_ID ) ";
		
		PreparedStatement pstmt = null;
		
		try 
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				P_Employee Employee = P_Employee.get( ctx, rs.getInt( "P_Employee_ID"), trxName );
				System.out.println( " Employé : " + Employee.getValue() + " " + Employee.getName());

				P_Employee_Form tabtd1 = new P_Employee_Form( ctx, -1, trxName);
				tabtd1.setP_Employee_ID( rs.getInt( "P_Employee_ID"));
				tabtd1.setP_Form_Template_ID(1000000);
				tabtd1.setEffectIn( TimeUtil.getDay(2007, 01, 01) );		
               	tabtd1.setIsActive( true);
				tabtd1.save();

				count++;
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("Migration p_employee_form - " + e);
		}

		return count;

	}
		
	public int p_employee_form_prov()
	{
		int count = 0;

		String sql = "select * from p_employee where ad_client_id = 1000004 and isactive = 'Y' ";
		
		PreparedStatement pstmt = null;
		
		try 
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{

				P_Employee Employee = P_Employee.get( ctx, rs.getInt( "P_Employee_ID"), trxName );
				System.out.println( " Employé : " + Employee.getValue() + " " + Employee.getName());
				P_Region Region = P_Region.get( ctx, Employee.getTaxation_Region_ID(), trxName);

				 int Form_Template_ID = 0;
				if ( Region.getName().equals("QC"))
				{
					Form_Template_ID = 1000001;
				}
				else
				{
			        Form_Template_ID = P_Form_Template.getForm_Template_ID("TD1"+Region.getName()) ;
				}
		        
		        

		        P_Employee_Form form = P_Employee_Form.getEmployee_Form(rs.getInt( "P_Employee_ID"), Form_Template_ID, trxName);
		        if ( Form_Template_ID != 0 &&  form == null )
		        {
					form = new P_Employee_Form( ctx, -1, trxName);
					form.setP_Employee_ID( rs.getInt( "P_Employee_ID"));
					form.setP_Form_Template_ID( Form_Template_ID );
					form.setEffectIn( TimeUtil.getDay(2007, 01, 01) );
	               	form.setIsActive( true);
					form.save();
		        }

				count++;
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("Migration p_employee_form - " + e);
		}

		return count;
		
	}




	/**************************************************************************
	 * 	Migration des données pour la HLC création TD1 pour tout les employés
	 */
	public static void main (String[] args)
	{
		System.out.println("Patch_TD1   $Revision: 1.1 $");
		System.out.println("----------------------------------");
		//
		Compiere.startup(true);
		
		
		int count = 0;
		new Patch_TD1();
		count++;
		System.out.println("Generated = " + count);

	}	//	main
	
}
