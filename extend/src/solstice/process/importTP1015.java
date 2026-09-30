package solstice.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.Compiere;
import org.compiere.model.MRegion;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

import solstice.model.P_Employee;
import solstice.model.P_Employee_Form;
import solstice.model.P_Employee_Form_Detail;
import solstice.model.P_Form_Template;
import solstice.model.X_I_Employee;

public class importTP1015 extends SvrProcess {

	/**	Client to be imported to		*/
	private int				m_AD_Client_ID = 1000004;
	/**	Delete old Imported				*/
	private boolean			m_deleteOldImported = false;

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
	}	//	prepare


	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception
	 */
	protected String doIt() 
	{
		StringBuffer sql = null;

		
/*		sql = new StringBuffer ("SELECT * FROM I_Employee "
			+ "WHERE  PAY_EMP_FED_L <> '0.00' and p_employee_id is not null")
				;
*/
		sql = new StringBuffer ("SELECT * FROM I_Employee "
				+ "where p_employee_id is not null and ISACTIVE = 'Y' "
				+ "	and ( PAY_EMP_PRO_E <> '161.43' OR PAY_EMP_FED_E <> '143.98' ) ")
				;

		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				 import_info_compl( rs );
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
			System.err.println( "error : " + e);
		}
		return "";
		
	}

	
	


	public void import_info_compl( ResultSet rs ) throws Exception
	{
		
		X_I_Employee impEmp = new X_I_Employee (Env.getCtx(), rs, null);
		P_Employee emp = P_Employee.get( Env.getCtx(),  impEmp.getP_Employee_ID(), null);

		if ( ! emp.isActive() )
			return;
		
		if ( impEmp.getP_Employee_ID() == 0)
			return;
		
		// Québec
		if ( emp.getTaxation_Region_ID() == 166)
		{
			
			MRegion Region = MRegion.get( Env.getCtx(), impEmp.getTaxation_Region_ID() );
			P_Form_Template FP = P_Form_Template.get( Env.getCtx(), 1000001, null);
			BigDecimal defaultAmount = getDefaultAmount( Region.getC_Region_ID(), Region.getName());
			
			P_Employee_Form TP1015 = P_Employee_Form.getEmployee_Form(emp.getP_Employee_ID(), 1000001, null);
			if ( TP1015 == null )
				TP1015 = new P_Employee_Form( Env.getCtx(), -1, null);
					
			TP1015.setP_Employee_ID( impEmp.getP_Employee_ID());
			TP1015.setP_Form_Template_ID(1000001);
			TP1015.setEffectIn( TimeUtil.getDay(2022, 01, 01) );
			TP1015.setIsActive(true);
			TP1015.save();

			// impot additionnel
			P_Employee_Form_Detail TP1015_Detail = P_Employee_Form_Detail.getEmployee_Form_Detail( TP1015, "1");
			BigDecimal amt =  new BigDecimal( rs.getString( "PAY_EMP_PRO_E") ).multiply(new BigDecimal("100"));
			if ( amt != null ) 
			{
				TP1015_Detail.setFormFieldAmount(  amt );
				TP1015_Detail.save();
			}

			P_Employee_Form TD1 = P_Employee_Form.getEmployee_Form(emp.getP_Employee_ID(), 1000000, null);
			if ( TD1 == null )
				TD1 = new P_Employee_Form( Env.getCtx(), -1, null);
			
			TD1.setP_Employee_ID( impEmp.getP_Employee_ID());
			TD1.setP_Form_Template_ID(1000000);
			TD1.setEffectIn( TimeUtil.getDay(2022, 01, 01) );
			TD1.setIsActive(true);
			TD1.save();

			P_Employee_Form_Detail TD1_Detail = P_Employee_Form_Detail.getEmployee_Form_Detail( TD1, "1");

			BigDecimal amtFed =  new BigDecimal(rs.getString( "PAY_EMP_FED_E") ).multiply(new BigDecimal("100"));

			if ( amtFed != null ) 
			{
				TD1_Detail.setFormFieldAmount( amtFed );
				TD1_Detail.save();
			}	
		}
		
	}

	
	
	public void import_info_compl2( ResultSet rs ) throws Exception
	{
		
		X_I_Employee impEmp = new X_I_Employee (Env.getCtx(), rs, null);
		P_Employee emp = P_Employee.get( Env.getCtx(),  impEmp.getP_Employee_ID(), null);

		if ( ! emp.isActive() )
			return;
		
		if ( impEmp.getP_Employee_ID() == 0)
			return;
		
		// Québec
		if ( emp.getTaxation_Region_ID() == 166)
		{
			
			MRegion Region = MRegion.get( Env.getCtx(), impEmp.getTaxation_Region_ID() );
			P_Form_Template FP = P_Form_Template.get( Env.getCtx(), 1000001, null);
			BigDecimal defaultAmount = getDefaultAmount( Region.getC_Region_ID(), Region.getName());
			
			P_Employee_Form TP1015 = P_Employee_Form.getEmployee_Form(emp.getP_Employee_ID(), 1000001, null);
			if ( TP1015 == null )
				TP1015 = new P_Employee_Form( Env.getCtx(), -1, null);
					
			TP1015.setP_Employee_ID( impEmp.getP_Employee_ID());
			TP1015.setP_Form_Template_ID(1000001);
			TP1015.setEffectIn( TimeUtil.getDay(2022, 01, 01) );
			TP1015.setIsActive(true);
			TP1015.save();

			// impot additionnel
			P_Employee_Form_Detail TP1015_Detail = P_Employee_Form_Detail.getEmployee_Form_Detail( TP1015, "11");
			BigDecimal amt =  new BigDecimal( rs.getString( "PAY_EMP_PRO_L") ).multiply(new BigDecimal("100"));
			TP1015_Detail.setFormFieldAmount(  amt );
			TP1015_Detail.save();

			P_Employee_Form TD1 = new P_Employee_Form( Env.getCtx(), -1, null);
			TD1.setP_Employee_ID( impEmp.getP_Employee_ID());
			TD1.setP_Form_Template_ID(1000000);
			TD1.setEffectIn( TimeUtil.getDay(2022, 01, 01) );
			TD1.setIsActive(true);
			TD1.save();

			P_Employee_Form_Detail TD1_Detail = P_Employee_Form_Detail.getEmployee_Form_Detail( TD1, "L");

			BigDecimal amtFed =  new BigDecimal(rs.getString( "PAY_EMP_FED_L") ).multiply(new BigDecimal("100"));

			TD1_Detail.setFormFieldAmount( amtFed );
			TD1_Detail.save();
			
		}
		
	}

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
	
	public static void main (String[] args)
	{
		Properties ctx = Env.getCtx();

		System.out.println("Import TD1 Tp1015");
		System.out.println("----------------------------------");
		//
		Compiere.startup(true);
		
		Env.setContext( ctx, "#AD_Client_ID", 1000004);
		Env.setContext( ctx, "#AD_Org_ID", 0);
		importTP1015 t = new importTP1015();
		t.doIt();
	}


}
