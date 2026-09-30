package solstice.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;

import solstice.migration.Test;
import solstice.model.*;

import org.compiere.Compiere;
import org.compiere.model.*;
import org.compiere.util.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

public class ImportAssignment //extends SvrProcess
{
	/**	Client to be imported to		*/
	private int				m_AD_Client_ID = 1000004;
	/**	Delete old Imported				*/
	private boolean			m_deleteOldImported = false;

	/** Effective						*/
	private Timestamp		m_DateValue = null;
	/**	Logger							*/
	protected CLogger			log = CLogger.getCLogger (getClass());

	P_Employee Employee;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
/*	protected void prepare()
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

*/
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

		//	-------------------------------------------------------------------
		int noInsert = 0;
		int noUpdate = 0;

		//	Go through Records
		sql = new StringBuffer ("select I_PAYMENT_GAIN.p_employee_id, pay_pos_no, max(p_post.p_post_id) as p_post_id "
				+ " from I_PAYMENT_GAIN "
				+ " inner join p_employee on p_employee.p_employee_id = i_payment_gain.p_employee_id "
				+ " inner join P_Post on p_post.value = pay_pos_no and p_post.ad_org_id = p_employee.ad_org_id "
				+ " group by I_PAYMENT_GAIN.p_employee_id, pay_pos_no "
				+ " order by I_PAYMENT_GAIN.p_employee_id" ); 
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				P_Assignment Assignment ;
				int id = GetAssignmentPost( rs.getInt("P_Employee_ID"), rs.getInt("P_Post_ID"));
				if ( id == 0)
				{
					P_Employee Employee = P_Employee.get( getCtx() , rs.getInt("P_Employee_ID"), null );
					P_Post Post = P_Post.get(getCtx(), rs.getInt("P_Post_ID"), null);
					Assignment = new P_Assignment( getCtx(), -1 , null);
					Assignment.setAD_Org_ID( Employee.getAD_Org_ID() );
					Assignment.setAssignmentType(P_Assignment.ASSIGNMENTTYPE_Other);
					Assignment.setStartDate( Employee.getDateHired() );
					Assignment.setEndDate(null);
					Assignment.setIsActive(true);
					Assignment.setP_Employee_ID(Employee.getP_Employee_ID());
					Assignment.setP_Assignment_State_ID(1000061);
					Assignment.setP_Post_ID( rs.getInt("P_Post_ID") );
//					Assignment.setP_Assignment_State_ID(P_Assignment_State_ID)
					Assignment.setValue( Post.getValue() );
					Assignment.save();
					
					if ( Assignment.getP_Assignment_ID() != 0 ) 
					{
						P_Assignment_Param AssignmentParam;
						
/*						if ( Employee.getAssignment_Param(Assignment.getStartDate(), null ) != null )
							AssignmentParam  = Employee.getAssignment_Param(Assignment.getStartDate(), null );
						else
							AssignmentParam  = new P_Assignment_Param( getCtx(), -1, null);
*/
						AssignmentParam  = new P_Assignment_Param( getCtx(), -1, null);
						P_Job_Title jobType = P_Job_Title.get( getCtx(), Employee.getP_Job_Title_ID(), null);
						BigDecimal Hourly_Rate = getHourlyRate( rs.getInt("P_Post_ID") , jobType.getValue() );
						AssignmentParam.setHourly_Rate( Hourly_Rate );
						AssignmentParam.setAnnual_Salary( Hourly_Rate.multiply(new BigDecimal(40)).multiply( new BigDecimal(52)) );
						AssignmentParam.setIsOut_Of_Range(true);
						AssignmentParam.setWeekly_Hours( new BigDecimal(40) );
						AssignmentParam.setDay_Hours(AssignmentParam.getWeekly_Hours().divide(new BigDecimal(5)).setScale(2, BigDecimal.ROUND_HALF_UP )  );
						AssignmentParam.setEffectIn(Assignment.getStartDate());
						AssignmentParam.setP_Collective_Labour_Agr_ID( Employee.getP_Collective_Labour_Agr_ID());
						AssignmentParam.setP_Occupation_Group_ID(Employee.getP_Occupation_Group_ID());
						AssignmentParam.setRemuneration_Method(P_Assignment_Param.REMUNERATION_METHOD_Hourly);
						AssignmentParam.setP_Assignment_ID(Assignment.getP_Assignment_ID());
//						AssignmentParam.setSalaryPerPay(impEmp.getSalaryPerPay());
//						AssignmentParam.setHoursPerPay(impEmp.getNormalHoursPerPay());

		// AssignmentParam.setWeekly_Hours( (impEmp.getNormalHoursPerPay().multiply( new BigDecimal( 24 )).divide( new BigDecimal( 52 ),2,BigDecimal.ROUND_HALF_UP  )));
						AssignmentParam.setDay_Hours( AssignmentParam.getWeekly_Hours().divide( new BigDecimal(5), 2, BigDecimal.ROUND_HALF_UP) );
						AssignmentParam.setIsActive(true);
						AssignmentParam.save();
					
					}
						
				}
						

					
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
		


		return "";
		
	}
	
    private Properties getCtx()
    {
    	return Env.getCtx();
    }

	
	Properties ctx = Env.getCtx();

/*	private Properties getCtx()
    {
    	return Env.getCtx();
    }
  */  
	public ImportAssignment() 
	{
		Env.setContext( ctx, "#AD_Client_ID", 1000004);
		Env.setContext( ctx, "#AD_Org_ID", 0);

		try
		{
			doIt();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE," import assignment " , e);
		}
	}
	
	public int GetAssignmentPost( int P_Employee_id, int P_Post_id  )
	{

		String sql = null;
		sql = "Select P_Assignment.P_Assignment_ID "; 
		sql += " From P_Assignment";
		sql += " WHERE P_Assignment.IsActive='Y' ";
		sql += " AND P_Assignment.P_Employee_ID=" + P_Employee_id;
		sql += " AND P_Assignment.P_Post_ID = " + P_Post_id
				;

		int iAssignment_ID = 0;

		PreparedStatement pstmt = null;

		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				iAssignment_ID = rs.getInt(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"P_Employee - GetAssignmentPost - " + sql, e);
		}
			

		return iAssignment_ID;
		
	}

	public BigDecimal getHourlyRate( int P_Post_ID, String PaySteCode)
	{		
		String sql = null;
	    if ( PaySteCode.equals("REG"))
	    	PaySteCode = "PER";
	    else
	    	PaySteCode = "PRO";
	    
		sql = "select * from RV_PAYECHEL where P_Post_ID =  " + P_Post_ID + " AND PAY_STE_CODE = '" + PaySteCode + "'"; 
				;
	
		BigDecimal mnt = Env.ZERO;
	
		PreparedStatement pstmt = null;
	
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				mnt = rs.getBigDecimal("PAY_ECH_SAL_HORAIRE");
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"P_Employee - GetAssignmentPost - " + sql, e);
		}
			
	
		return mnt;
			
	}

	/**************************************************************************
	 * 	Migration des données pour la SIQ
	 */
	public static void main (String[] args)
	{
		System.out.println("Test   $Revision: 1.3 $");
		System.out.println("----------------------------------");
		//
		Compiere.startup(true);
		
		
		int count = 0;
		new ImportAssignment();
		count++;
		System.out.println("Generated = " + count);

	}	//	main

	
}
