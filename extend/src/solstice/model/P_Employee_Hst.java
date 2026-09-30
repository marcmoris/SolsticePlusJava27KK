/*
 * Created on 2005-07-14
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * @author kevmar01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_Employee_Hst extends X_P_Employee_Hst 
{
	/**
	 * 	Get employee_Hst
	 *	@param ctx context
	 * 	@param P_Employee_Hst_ID id
	 *	@return P_Employee_Hst
	 */
	public static P_Employee_Hst get (Properties ctx, int P_Employee_Hst_ID, String trxName)
	{
		Integer key = new Integer (P_Employee_Hst_ID);
		P_Employee_Hst employee_Hst = (P_Employee_Hst)s_cache.get(key);
		if (employee_Hst != null)
			return employee_Hst;
		employee_Hst = new P_Employee_Hst (ctx, P_Employee_Hst_ID, trxName);
		s_cache.put (key, employee_Hst);
		return employee_Hst;
	}	//	get


	/**	Cache						*/
	private static CCache<Integer,P_Employee_Hst>	s_cache = new CCache<Integer,P_Employee_Hst>("P_Employee_Hst", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Employee_Hst_ID id
	 */
	public P_Employee_Hst (Properties ctx, int P_Employee_Hst_ID, String trxName)
	{
		super (ctx, P_Employee_Hst_ID, trxName);
	}	//	P_Assignment_Hst

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Employee_Hst (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Employee_Hst (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Employee_Hst_ID"), trxName);
	}	//	P_Employee_Hst


	public static P_Employee_Hst get (Properties ctx, int P_Employee_ID, Timestamp effectIn, String trxName)
	{
		int P_Employee_Hst_ID = 0;

		String sql = "Select P_Employee_Hst_ID From P_Employee_Hst where P_Employee_ID = ? AND EffectIn = ?";
		//Log.trace( 10, "P_Assignment_Hst.Get - " + sql + " - " + P_Assignment_ID );

		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt( 1, P_Employee_ID );
			pstmt.setTimestamp( 2, effectIn );
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next() )
				P_Employee_Hst_ID = rs.getInt( "P_Employee_Hst_ID");
			rs.close ();
			pstmt.close ();
			pstmt = null;

		}
		catch (Exception e)
		{
			System.out.println ("* Error * P_Employee_Hst.Get - " + sql + " - " + e);

		}
		
		if ( P_Employee_Hst_ID == 0 )
		    return P_Employee_Hst.get( ctx, -1, trxName );
		
		return P_Employee_Hst.get( ctx, P_Employee_Hst_ID, trxName );
	}	//	get

	public static void createHisto ( Properties ctx, P_Employee Employee )
	{
	    P_Employee_Hst EmployeeHst = new P_Employee_Hst(ctx, -1, Employee.get_TrxName() ); 
	    EmployeeHst.setAD_Org_ID( Employee.getAD_Org_ID()  );
	    EmployeeHst.setP_Department_ID ( Employee.getP_Department_ID() );
	    EmployeeHst.setC_Activity_ID( Employee.getC_Activity_ID());
	    EmployeeHst.setP_Collective_Labour_Agr_ID( Employee.getP_Collective_Labour_Agr_ID() );
	    EmployeeHst.setP_Language_ID          ( Employee.getP_Language_ID         () );
	    EmployeeHst.setBirthDate              ( Employee.getBirthDate             () );
	    EmployeeHst.setC_Location_ID		  ( Employee.getC_Location_ID		  () );
	    EmployeeHst.setCivilStatus            ( Employee.getCivilStatus           () );
	    EmployeeHst.setDateHired              ( Employee.getDateHired             () );
	    EmployeeHst.setDateLayoff             ( Employee.getDateLayoff            () );
	    EmployeeHst.setDateProbationary       ( Employee.getDateProbationary      () );
	    EmployeeHst.setDateRehired            ( Employee.getDateRehired           () );
	    EmployeeHst.setDateSeniority          ( Employee.getDateSeniority         () );
	    EmployeeHst.setEmail                  ( Employee.getEMail                 () );
	    EmployeeHst.setFirstLetter            ( Employee.getFirstLetter           () );
	    EmployeeHst.setFirstName			  ( Employee.getFirstName			  () );
	    EmployeeHst.setGender                 ( Employee.getGender                () );
	    EmployeeHst.setIsActive               ( Employee.isActive				  () );
	    if ( Employee.getLayoffCode() != null && Employee.getLayoffCode().length() != 0 )
	    	EmployeeHst.setLayoffCode		  ( Employee.getLayoffCode			  () );
	    EmployeeHst.setName                   ( Employee.getName                  () );
	    EmployeeHst.setP_Distribution_ID      ( Employee.getP_Distribution_ID     () );
	    EmployeeHst.setP_Employee_ID          ( Employee.getP_Employee_ID         () );
	    EmployeeHst.setP_Employer_ID          ( Employee.getP_Employer_ID         () );
	    EmployeeHst.setP_Holidays_Calendar_ID ( Employee.getP_Holidays_Calendar_ID() );
	    EmployeeHst.setP_Job_Title_ID         ( Employee.getP_Job_Title_ID        () );
	    EmployeeHst.setP_Job_Type_ID          ( Employee.getP_Job_Type_ID         () );
	    EmployeeHst.setP_Occupation_Group_ID  ( Employee.getP_Occupation_Group_ID () );
	    EmployeeHst.setP_Payment_Group_ID     ( Employee.getP_Payment_Group_ID    () );
	    EmployeeHst.setP_Workplace_ID         ( Employee.getP_Workplace_ID        () );
	    EmployeeHst.setPaymentType            ( Employee.getPaymentType           () );
	    EmployeeHst.setPhone                  ( Employee.getPhone                 () );
	    EmployeeHst.setPrintStatementEarning  ( Employee.isPrintStatementEarning  () );
	    EmployeeHst.setProcessRoe			  ( Employee.getProcessRoe			  () );
	    EmployeeHst.setProcessRoeList		  ( Employee.getProcessRoeList		  () );
	    EmployeeHst.setRemunerateHoliday	  ( Employee.isRemunerateHoliday	  () );
	    EmployeeHst.setSin                    ( Employee.getSin                   () );
	    EmployeeHst.setSurname				  ( Employee.getSurname				  () );
	    EmployeeHst.setTaxation_Region_ID     ( Employee.getTaxation_Region_ID    () );
	    EmployeeHst.setValue                  ( Employee.getValue                 () );
	    EmployeeHst.setP_Distribution_Booklet_ID (Employee.getP_Distribution_Booklet_ID() );
	    EmployeeHst.setAD_User_ID                (Employee.getAD_User_ID()                );
	    EmployeeHst.setIsCommingBack             (Employee.isCommingBack()                );
	    EmployeeHst.setReactivation              (Employee.getReactivation()              );
	    EmployeeHst.setDateHired2                (Employee.getDateHired2()                );
	    EmployeeHst.setDateHiredComment          (Employee.getDateHiredComment()          );
	    EmployeeHst.setDateRehiredComment        (Employee.getDateRehiredComment()        );
	    EmployeeHst.setJobTypeEffectIn           (Employee.getJobTypeEffectIn()           );
	    EmployeeHst.setLayoffComment             (Employee.getLayoffComment()             );

	    EmployeeHst.save();
		
	}

}
