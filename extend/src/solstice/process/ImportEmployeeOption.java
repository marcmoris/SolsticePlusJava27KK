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
import org.compiere.util.TimeUtil;

import solstice.model.*;

public class ImportEmployeeOption extends SvrProcess
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


	private String trxName =  null;
	private P_Employee employee;
	private X_I_Employee_Option imp;
	boolean isMax = false;
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
          sql = new StringBuffer ("update p_assignment set p_post_id = null, isincumbent = 'N' where 1=1 " + clientCheck );
			DB.executeUpdate(sql.toString(), null);

			sql = new StringBuffer ("delete from P_Standard_Time where p_gain_id not in ( 1003531, 1003533 ) " + clientCheck );
			DB.executeUpdate(sql.toString(), null);

			sql = new StringBuffer ("delete from P_Employee_Deduction where 1=1 " + clientCheck );
			DB.executeUpdate(sql.toString(), null);

			sql = new StringBuffer ("delete from P_Employee_Taxable_Benefit where 1=1 " + clientCheck );
			DB.executeUpdate(sql.toString(), null);

			sql = new StringBuffer ("UPDATE I_Employee_option set I_IsImported='N'" );
			DB.executeUpdate(sql.toString(), null);

			sql = new StringBuffer ("delete from P_Assignment where value != '01' " + clientCheck );
			DB.executeUpdate(sql.toString(), null);

		}
	
		int noInsert = 0;
		int noUpdate = 0;

		sql = new StringBuffer ("UPDATE I_Employee_option set I_ERRORMSG = null" );
		DB.executeUpdate(sql.toString(), null);

		sql = new StringBuffer ("UPDATE I_Employee_option set Post = dbo.lpad( Post, 3, '0' )" );
		DB.executeUpdate(sql.toString(), null);

		sql = new StringBuffer ("UPDATE i_employee_option SET P_EMPLOYEE_ID = null " );  
		DB.executeUpdate(sql.toString(), null);

		sql = new StringBuffer ("UPDATE i_employee_option SET P_EMPLOYEE_ID = p_employee.p_employee_id from p_employee where i_employee_option.value like '%' + p_employee.value " );  
		DB.executeUpdate(sql.toString(), null);

		sql = new StringBuffer ("UPDATE i_employee_option SET P_Post_id = P_Post.P_Post_ID from P_Post where rtrim(P_Post.value) = rtrim( i_employee_option.post) " );  
		DB.executeUpdate(sql.toString(), null);

		sql = new StringBuffer ("update p_assignment set p_post_id = i_employee_option.P_Post_id, isincumbent = 'Y' "+
							    " from i_employee_option " +
                                " where i_employee_option.P_Post_ID is not null " +
                                " and i_employee_option.p_employee_id = p_assignment.p_employee_id " );  
		noUpdate = DB.executeUpdate(sql.toString(), null);

		sql = new StringBuffer ("UPDATE i_employee_option SET I_ERRORMSG = 'Poste non définie / post not found ' + i_employee_option.post where P_Post_ID is null " );  
		DB.executeUpdate(sql.toString(), null);

		sql = new StringBuffer ("UPDATE i_employee_option SET I_ERRORMSG = 'Employé non définie / employee not found' where P_Employee_ID is null " );  
		DB.executeUpdate(sql.toString(), null);

/*		sql = new StringBuffer ("UPDATE i_employee_option SET gain04 = gain04 / 100 " );  
		DB.executeUpdate(sql.toString(), null);
		sql = new StringBuffer ("UPDATE i_employee_option SET gain05 = gain05 / 100 " );  
		DB.executeUpdate(sql.toString(), null);
*/
		sql = new StringBuffer ("SELECT i_employee_option_ID FROM i_employee_option "
				+ "WHERE I_ERRORMSG is null and I_IsImported='N'  ");
			log.info( "Sql " + sql);
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), trxName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				imp = new X_I_Employee_Option (getCtx(), rs.getInt(1), trxName);
				
				employee = P_Employee.get( getCtx(), imp.getP_Employee_ID(), trxName);
				System.out.println( "Import employée : " + employee.getValue() + " " + employee.getName() );
				
				if ( imp.getlangue() == null || imp.getlangue().equals("1") )
					employee.setP_Language_ID(127);
				else
					employee.setP_Language_ID(155);

				employee.save();
				
				if ( imp.getI_ErrorMsg() == null )
					imp.setI_IsImported(true);

				imp.save();
				

				boolean newRecord = false;
				P_Assignment_Param AssignmentParamRef = P_Assignment_Param.get(getCtx(), employee.GetPrincipalAssignment_Param(), trxName);

//				Ajout si l'employé a un echelle et un gain 4 ou 5 creer une 2iem affectation aussi.
				if ( AssignmentParamRef.isOut_Of_Range() == false )
					newRecord = true;

				// Si l'employé a 2 salaire
				int noAss = 1;
				if ( newRecord )
				{
					if ( imp.getGain04() != null && imp.getGain04().compareTo(Env.ZERO) != 0 )
					{
						noAss = 2;

						P_Assignment Assignment = new P_Assignment( getCtx(), -1 , null);
						Assignment.setAD_Org_ID( employee.getAD_Org_ID() );
						Assignment.setAssignmentType(P_Assignment.ASSIGNMENTTYPE_Other);
						Assignment.setStartDate( employee.getDateHired() );
						Assignment.setEndDate(null);
						Assignment.setIsActive(true);
						Assignment.setP_Employee_ID(employee.getP_Employee_ID());
						Assignment.setP_Assignment_State_ID(1000061);
//						Assignment.setValue("02");
						Assignment.save();

						P_Assignment_Param AssignmentParam = new P_Assignment_Param(getCtx(), -1, trxName);

						AssignmentParam.setIsOut_Of_Range(true);
						AssignmentParam.setIsOut_Of_Rate(false);
						AssignmentParam.setEffectIn(Assignment.getStartDate());
						AssignmentParam.setP_Collective_Labour_Agr_ID( employee.getP_Collective_Labour_Agr_ID());
						AssignmentParam.setP_Occupation_Group_ID(employee.getP_Occupation_Group_ID());
						AssignmentParam.setRemuneration_Method(P_Assignment_Param.REMUNERATION_METHOD_Daily);
						AssignmentParam.setP_Assignment_ID(Assignment.getP_Assignment_ID());
						AssignmentParam.setWeekly_Hours( AssignmentParamRef.getWeekly_Hours() );
						AssignmentParam.setDay_Hours( AssignmentParam.getWeekly_Hours().divide( new BigDecimal(5), 2, BigDecimal.ROUND_HALF_UP) );
						AssignmentParam.setIsActive(true);

						AssignmentParam.setAnnual_Salary( Env.ZERO );
						AssignmentParam.setHourly_Rate( Env.ZERO );
						AssignmentParam.setDaily_Salary( imp.getGain04().divide(new BigDecimal(10000),6,BigDecimal.ROUND_HALF_UP) );
						
						AssignmentParam.save();
					}
				}

				if ( (imp.getGain04() != null && imp.getGain04().compareTo(Env.ZERO) != 0 ) && ( imp.getGain05() != null && imp.getGain05().compareTo(Env.ZERO) != 0) )
					newRecord = true;

				if ( newRecord )
				{
					if ( imp.getGain05() != null && imp.getGain05().compareTo(Env.ZERO) != 0 )
					{
						P_Assignment Assignment = new P_Assignment( getCtx(), -1 , null);
						Assignment.setAD_Org_ID( employee.getAD_Org_ID() );
						Assignment.setAssignmentType(P_Assignment.ASSIGNMENTTYPE_Other);
						Assignment.setStartDate( employee.getDateHired() );
						Assignment.setEndDate(null);
						Assignment.setIsActive(true);
						Assignment.setP_Employee_ID(employee.getP_Employee_ID());
						Assignment.setP_Assignment_State_ID(1000061);
//						Assignment.setP_Assignment_State_ID(P_Assignment_State_ID)
						
/*						if ( noAss == 1)
							Assignment.setValue("02");
						else
							Assignment.setValue("03");
*/							
						Assignment.save();

						P_Assignment_Param AssignmentParam = new P_Assignment_Param(getCtx(), -1, trxName);

						AssignmentParam.setRemuneration_Method( P_Assignment_Param.REMUNERATION_METHOD_Hourly);

						AssignmentParam.setIsOut_Of_Range(true);
						AssignmentParam.setIsOut_Of_Rate(false);
						AssignmentParam.setEffectIn(Assignment.getStartDate());
						AssignmentParam.setP_Collective_Labour_Agr_ID( employee.getP_Collective_Labour_Agr_ID());
						AssignmentParam.setP_Occupation_Group_ID(employee.getP_Occupation_Group_ID());
						AssignmentParam.setP_Assignment_ID(Assignment.getP_Assignment_ID());
						AssignmentParam.setDay_Hours( AssignmentParam.getWeekly_Hours().divide( new BigDecimal(5), 2, BigDecimal.ROUND_HALF_UP) );
						AssignmentParam.setIsActive(true);
						AssignmentParam.setWeekly_Hours( AssignmentParamRef.getWeekly_Hours() );
						AssignmentParam.setDaily_Salary( Env.ZERO);
						AssignmentParam.setHourly_Rate( imp.getGain05().divide(new BigDecimal(10000),6,BigDecimal.ROUND_HALF_UP) );
						AssignmentParam.setAnnual_Salary( AssignmentParam.getHourly_Rate().multiply(new BigDecimal(52)).multiply( AssignmentParam.getWeekly_Hours()));
						AssignmentParam.save();
					}
				}
				else
				{

					if ( imp.getGain04() != null && imp.getGain04().compareTo(Env.ZERO) != 0 )
					{
						P_Assignment_Param AssignmentParam = P_Assignment_Param.get(getCtx(), employee.GetPrincipalAssignment_Param(), trxName);

						AssignmentParam.setRemuneration_Method( P_Assignment_Param.REMUNERATION_METHOD_Daily);
						AssignmentParam.setDaily_Salary( imp.getGain04().divide(new BigDecimal(10000),6,BigDecimal.ROUND_HALF_UP) );
						AssignmentParam.setAnnual_Salary( Env.ZERO );
						AssignmentParam.setHourly_Rate( Env.ZERO );
						AssignmentParam.save();
						
					}
					if ( imp.getGain05() != null && imp.getGain05().compareTo(Env.ZERO) != 0 )
					{
						P_Assignment_Param AssignmentParam = P_Assignment_Param.get(getCtx(), employee.GetPrincipalAssignment_Param(), trxName);
						AssignmentParam.setRemuneration_Method( P_Assignment_Param.REMUNERATION_METHOD_Hourly);
						AssignmentParam.setHourly_Rate( imp.getGain05().divide(new BigDecimal(10000),6,BigDecimal.ROUND_HALF_UP) );
						AssignmentParam.setAnnual_Salary( AssignmentParam.getHourly_Rate().multiply(new BigDecimal(52)).multiply( AssignmentParam.getWeekly_Hours()));
						AssignmentParam.save();
					}
				}
					

				
				isMax = false;
				if ( imp.getDedn01() != null) add_deduction( getP_Deduction_ID("01"), imp.getDedn01(), "01" );
				if ( imp.getDedn02() != null) add_deduction( getP_Deduction_ID("02"), imp.getDedn02(), "02" );
				if ( imp.getDedn03() != null) add_deduction( getP_Deduction_ID("03"), imp.getDedn03(), "03" );
				if ( imp.getDedn04() != null) add_deduction( getP_Deduction_ID("04"), imp.getDedn04(), "04" );
				if ( imp.getDedn05() != null) add_deduction( getP_Deduction_ID("05"), imp.getDedn05(), "05" );
				if ( imp.getDedn06() != null) add_deduction( getP_Deduction_ID("06"), imp.getDedn06(), "06" );
				if ( imp.getDedn07() != null) add_deduction( getP_Deduction_ID("07"), imp.getDedn07(), "07" );
				if ( imp.getDedn08() != null) add_deduction( getP_Deduction_ID("08"), imp.getDedn08(), "08" );
				if ( imp.getDedn09() != null) add_deduction( getP_Deduction_ID("09"), imp.getDedn09(), "09" );
				if ( imp.getDedn10() != null) add_deduction( getP_Deduction_ID("10"), imp.getDedn10(), "10" );
				if ( imp.getDedn11() != null) add_deduction( getP_Deduction_ID("11"), imp.getDedn11(), "11" );
				if ( imp.getDedn12() != null) add_deduction( getP_Deduction_ID("12"), imp.getDedn12(), "12" );
				if ( imp.getDedn13() != null) add_deduction( getP_Deduction_ID("13"), imp.getDedn13(), "13" );
				if ( imp.getDedn14() != null) add_deduction( getP_Deduction_ID("14"), imp.getDedn14(), "14" );
				if ( imp.getDedn15() != null) add_deduction( getP_Deduction_ID("15"), imp.getDedn15(), "15" );
				if ( imp.getDedn16() != null) add_deduction( getP_Deduction_ID("16"), imp.getDedn16(), "16" );
				if ( imp.getDedn17() != null) add_deduction( getP_Deduction_ID("17"), imp.getDedn17(), "17" );
				if ( imp.getDedn18() != null) add_deduction( getP_Deduction_ID("18"), imp.getDedn18(), "18" );
				if ( imp.getDedn19() != null) add_deduction( getP_Deduction_ID("19"), imp.getDedn19(), "19" );
				if ( imp.getDedn20() != null) add_deduction( getP_Deduction_ID("20"), imp.getDedn20(), "20" );
				if ( imp.getDedn21() != null) add_deduction( getP_Deduction_ID("21"), imp.getDedn21(), "21" );
				if ( imp.getDedn22() != null) add_deduction( getP_Deduction_ID("22"), imp.getDedn22(), "22" );
				if ( imp.getDedn23() != null) add_deduction( getP_Deduction_ID("23"), imp.getDedn23(), "23" );
				if ( imp.getDedn24() != null) add_deduction( getP_Deduction_ID("24"), imp.getDedn24(), "24" );
				if ( imp.getDedn25() != null) add_deduction( getP_Deduction_ID("25"), imp.getDedn25(), "25" );
				if ( imp.getDedn26() != null) add_deduction( getP_Deduction_ID("26"), imp.getDedn26(), "26" );
				if ( imp.getDedn27() != null) add_deduction( getP_Deduction_ID("27"), imp.getDedn27(), "27" );
				if ( imp.getDedn28() != null) add_deduction( getP_Deduction_ID("28"), imp.getDedn28(), "28" );
				if ( imp.getDedn29() != null) add_deduction( getP_Deduction_ID("29"), imp.getDedn29(), "29" );
				if ( imp.getDedn30() != null) add_deduction( getP_Deduction_ID("30"), imp.getDedn30(), "30" );
				if ( imp.getDedn31() != null) add_deduction( getP_Deduction_ID("31"), imp.getDedn31(), "31" );
				if ( imp.getDedn32() != null) add_deduction( getP_Deduction_ID("32"), imp.getDedn32(), "32" );
				if ( imp.getDedn33() != null) add_deduction( getP_Deduction_ID("33"), imp.getDedn33(), "33" );
				if ( imp.getDedn34() != null) add_deduction( getP_Deduction_ID("34"), imp.getDedn34(), "34" );
				if ( imp.getDedn35() != null) add_deduction( getP_Deduction_ID("35"), imp.getDedn35(), "35" );
				if ( imp.getDedn36() != null) add_deduction( getP_Deduction_ID("36"), imp.getDedn36(), "36" );
				if ( imp.getDedn37() != null) add_deduction( getP_Deduction_ID("37"), imp.getDedn37(), "37" );
				if ( imp.getDedn38() != null) add_deduction( getP_Deduction_ID("38"), imp.getDedn38(), "38" );
				if ( imp.getDedn39() != null) add_deduction( getP_Deduction_ID("39"), imp.getDedn39(), "39" );
				if ( imp.getDedn40() != null) add_deduction( getP_Deduction_ID("40"), imp.getDedn40(), "40" );
				if ( imp.getDedn41() != null) add_deduction( getP_Deduction_ID("41"), imp.getDedn41(), "41" );
				if ( imp.getDedn42() != null) add_deduction( getP_Deduction_ID("42"), imp.getDedn42(), "42" );
				if ( imp.getDedn43() != null) add_deduction( getP_Deduction_ID("43"), imp.getDedn43(), "43" );
				if ( imp.getDedn44() != null) add_deduction( getP_Deduction_ID("44"), imp.getDedn44(), "44" );
				if ( imp.getDedn45() != null) add_deduction( getP_Deduction_ID("45"), imp.getDedn45(), "45" );
				if ( imp.getDedn46() != null) add_deduction( getP_Deduction_ID("46"), imp.getDedn46(), "46" );
				if ( imp.getDedn47() != null) add_deduction( getP_Deduction_ID("47"), imp.getDedn47(), "47" );
				if ( imp.getDedn48() != null) add_deduction( getP_Deduction_ID("48"), imp.getDedn48(), "48" );
				if ( imp.getDedn49() != null) add_deduction( getP_Deduction_ID("49"), imp.getDedn49(), "49" );
				if ( imp.getDedn50() != null) add_deduction( getP_Deduction_ID("50"), imp.getDedn50(), "50" );
				if ( imp.getDedn51() != null) add_deduction( getP_Deduction_ID("51"), imp.getDedn51(), "51" );
				if ( imp.getDedn52() != null) add_deduction( getP_Deduction_ID("52"), imp.getDedn52(), "52" );
				if ( imp.getDedn53() != null) add_deduction( getP_Deduction_ID("53"), imp.getDedn53(), "53" );
				if ( imp.getDedn54() != null) add_deduction( getP_Deduction_ID("54"), imp.getDedn54(), "54" );
				if ( imp.getDedn55() != null) add_deduction( getP_Deduction_ID("55"), imp.getDedn55(), "55" );
				if ( imp.getDedn56() != null) add_deduction( getP_Deduction_ID("56"), imp.getDedn56(), "56" );
				if ( imp.getDedn57() != null) add_deduction( getP_Deduction_ID("57"), imp.getDedn57(), "57" );
				if ( imp.getDedn58() != null) add_deduction( getP_Deduction_ID("58"), imp.getDedn58(), "58" );
				if ( imp.getDedn59() != null) add_deduction( getP_Deduction_ID("59"), imp.getDedn59(), "59" );
				if ( imp.getDedn60() != null) add_deduction( getP_Deduction_ID("60"), imp.getDedn60(), "60" );
				if ( imp.getDedn61() != null) add_deduction( getP_Deduction_ID("61"), imp.getDedn61(), "61" );
				if ( imp.getDedn62() != null) add_deduction( getP_Deduction_ID("62"), imp.getDedn62(), "62" );
				if ( imp.getDedn63() != null) add_deduction( getP_Deduction_ID("63"), imp.getDedn63(), "63" );
				if ( imp.getDedn64() != null) add_deduction( getP_Deduction_ID("64"), imp.getDedn64(), "64" );
// cumulatif 63 + gain 89 - 				if ( imp.getDedn65() != null) add_deduction( getP_Deduction_ID("65"), imp.getDedn65(), "65" );
				if ( imp.getDedn66() != null) add_deduction( getP_Deduction_ID("66"), imp.getDedn66(), "66" );
				if ( imp.getDedn67() != null) add_deduction( getP_Deduction_ID("67"), imp.getDedn67(), "67" );
				if ( imp.getDedn68() != null) add_deduction( getP_Deduction_ID("68"), imp.getDedn68(), "68" );
				if ( imp.getDedn69() != null) add_deduction( getP_Deduction_ID("69"), imp.getDedn69(), "69" );
				if ( imp.getDedn70() != null) add_deduction( getP_Deduction_ID("70"), imp.getDedn70(), "70" );
				if ( imp.getDedn71() != null) add_deduction( getP_Deduction_ID("71"), imp.getDedn71(), "71" );
				if ( imp.getDedn72() != null) add_deduction( getP_Deduction_ID("72"), imp.getDedn72(), "72" );
				if ( imp.getDedn73() != null) add_deduction( getP_Deduction_ID("73"), imp.getDedn73(), "73" );
				if ( imp.getDedn74() != null) add_deduction( getP_Deduction_ID("74"), imp.getDedn74(), "74" );
				if ( imp.getDedn75() != null) add_deduction( getP_Deduction_ID("75"), imp.getDedn75(), "75" );
				if ( imp.getDedn76() != null) add_deduction( getP_Deduction_ID("76"), imp.getDedn76(), "76" );
				if ( imp.getDedn77() != null) add_deduction( getP_Deduction_ID("77"), imp.getDedn77(), "77" );
				if ( imp.getDedn78() != null) add_deduction( getP_Deduction_ID("78"), imp.getDedn78(), "78" );
				if ( imp.getDedn79() != null) add_deduction( getP_Deduction_ID("79"), imp.getDedn79(), "79" );
				if ( imp.getDedn80() != null) add_deduction( getP_Deduction_ID("80"), imp.getDedn80(), "80" );
				if ( imp.getDedn81() != null) add_deduction( getP_Deduction_ID("81"), imp.getDedn81(), "81" );
				if ( imp.getDedn82() != null) add_deduction( getP_Deduction_ID("82"), imp.getDedn82(), "82" );
				if ( imp.getDedn83() != null) add_deduction( getP_Deduction_ID("83"), imp.getDedn83(), "83" );
				if ( imp.getDedn84() != null) add_deduction( getP_Deduction_ID("84"), imp.getDedn84(), "84" );
				if ( imp.getDedn85() != null) add_deduction( getP_Deduction_ID("85"), imp.getDedn85(), "85" );
				if ( imp.getDedn86() != null) add_deduction( getP_Deduction_ID("86"), imp.getDedn86(), "86" );
				if ( imp.getDedn87() != null) add_deduction( getP_Deduction_ID("87"), imp.getDedn87(), "87" );
				if ( imp.getDedn88() != null) add_deduction( getP_Deduction_ID("88"), imp.getDedn88(), "88" );
				if ( imp.getDedn89() != null) add_deduction( getP_Deduction_ID("89"), imp.getDedn89(), "89" );
				if ( imp.getDedn90() != null) add_deduction( getP_Deduction_ID("90"), imp.getDedn90(), "90" );
				if ( imp.getDedn91() != null) add_deduction( getP_Deduction_ID("91"), imp.getDedn91(), "91" );
				if ( imp.getDedn92() != null) add_deduction( getP_Deduction_ID("92"), imp.getDedn92(), "92" );
				if ( imp.getDedn93() != null) add_deduction( getP_Deduction_ID("93"), imp.getDedn93(), "93" );
				if ( imp.getDedn94() != null) add_deduction( getP_Deduction_ID("94"), imp.getDedn94(), "94" );
				if ( imp.getDedn95() != null) add_deduction( getP_Deduction_ID("95"), imp.getDedn95(), "95" );
				if ( imp.getDedn96() != null) add_deduction( getP_Deduction_ID("96"), imp.getDedn96(), "96" );
				isMax = true;
				if ( imp.getMaxDedn07() != null) add_deduction( getP_Deduction_ID("07"), imp.getMaxDedn07(), "07" );
				if ( imp.getMaxDedn09() != null) add_deduction( getP_Deduction_ID("09"), imp.getMaxDedn09(), "09" );
				if ( imp.getMaxDedn10() != null) add_deduction( getP_Deduction_ID("10"), imp.getMaxDedn10(), "10" );
				if ( imp.getMaxDedn11() != null) add_deduction( getP_Deduction_ID("11"), imp.getMaxDedn11(), "11" );
				if ( imp.getMaxDedn22() != null) add_deduction( getP_Deduction_ID("22"), imp.getMaxDedn22(), "22" );
				if ( imp.getMaxDedn27() != null) add_deduction( getP_Deduction_ID("27"), imp.getMaxDedn27(), "27" );
				if ( imp.getMaxDedn34() != null) add_deduction( getP_Deduction_ID("34"), imp.getMaxDedn34(), "34" );
				if ( imp.getMaxDedn49() != null) add_deduction( getP_Deduction_ID("49"), imp.getMaxDedn49(), "49" );
				if ( imp.getMaxDedn54() != null) add_deduction( getP_Deduction_ID("54"), imp.getMaxDedn54(), "54" );
				if ( imp.getMaxDedn55() != null) add_deduction( getP_Deduction_ID("55"), imp.getMaxDedn55(), "55" );
				if ( imp.getMaxDedn57() != null) add_deduction( getP_Deduction_ID("57"), imp.getMaxDedn57(), "57" );
				if ( imp.getMaxDedn62() != null) add_deduction( getP_Deduction_ID("62"), imp.getMaxDedn62(), "62" );
				if ( imp.getMaxDedn66() != null) add_deduction( getP_Deduction_ID("66"), imp.getMaxDedn66(), "66" );
				if ( imp.getMaxDedn69() != null) add_deduction( getP_Deduction_ID("69"), imp.getMaxDedn69(), "69" );
				if ( imp.getMaxDedn80() != null) add_deduction( getP_Deduction_ID("80"), imp.getMaxDedn80(), "80" );

				
				if ( imp.getGain01() != null ) add_gain( getP_Gain_ID("01"), imp.getGain01(),  "01" );
				if ( imp.getGain02() != null ) add_gain( getP_Gain_ID("02"), imp.getGain02(),  "02" );
				if ( imp.getGain03() != null ) add_gain( getP_Gain_ID("03"), imp.getGain03(),  "03" );
//				if ( imp.getGain04() != null ) add_gain( getP_Gain_ID("04"), imp.getGain04().divide(new BigDecimal(10000),2,BigDecimal.ROUND_HALF_UP),  "04" );
//				if ( imp.getGain05() != null ) add_gain( getP_Gain_ID("05"), imp.getGain05().divide(new BigDecimal(10000),2,BigDecimal.ROUND_HALF_UP),  "05" );
				if ( imp.getGain06() != null ) add_gain( getP_Gain_ID("06"), imp.getGain06(),  "06" );
				if ( imp.getGain07() != null ) add_gain( getP_Gain_ID("07"), imp.getGain07(),  "07" );
				if ( imp.getGain08() != null ) add_gain( getP_Gain_ID("08"), imp.getGain08(),  "08" );
				if ( imp.getGain09() != null ) add_gain( getP_Gain_ID("09"), imp.getGain09(),  "09" );
				if ( imp.getGain10() != null ) add_gain( getP_Gain_ID("10"), imp.getGain10(),  "10" );
				if ( imp.getGain11() != null ) add_gain( getP_Gain_ID("11"), imp.getGain11(),  "11" );
				if ( imp.getGain12() != null ) add_gain( getP_Gain_ID("12"), imp.getGain12(),  "12" );
				if ( imp.getGain13() != null ) add_gain( getP_Gain_ID("13"), imp.getGain13(),  "13" );
				if ( imp.getGain14() != null ) add_gain( getP_Gain_ID("14"), imp.getGain14(),  "14" );
				if ( imp.getGain15() != null ) add_gain( getP_Gain_ID("15"), imp.getGain15(),  "15" );
				if ( imp.getGain16() != null ) add_gain( getP_Gain_ID("16"), imp.getGain16(),  "16" );
				if ( imp.getGain17() != null ) add_gain( getP_Gain_ID("17"), imp.getGain17(),  "17" );
				if ( imp.getGain18() != null ) add_gain( getP_Gain_ID("18"), imp.getGain18(),  "18" );
				if ( imp.getGain19() != null ) add_gain( getP_Gain_ID("19"), imp.getGain19(),  "19" );
				if ( imp.getGain20() != null ) add_gain( getP_Gain_ID("20"), imp.getGain20(),  "20" );
				if ( imp.getGain21() != null ) add_gain( getP_Gain_ID("21"), imp.getGain21(),  "21" );
				if ( imp.getGain22() != null ) add_gain( getP_Gain_ID("22"), imp.getGain22(),  "22" );
				if ( imp.getGain23() != null ) add_gain( getP_Gain_ID("23"), imp.getGain23(),  "23" );
				if ( imp.getGain24() != null ) add_gain( getP_Gain_ID("24"), imp.getGain24(),  "24" );
				if ( imp.getGain25() != null ) add_gain( getP_Gain_ID("25"), imp.getGain25(),  "25" );
				if ( imp.getGain26() != null ) add_gain( getP_Gain_ID("26"), imp.getGain26(),  "26" );
				if ( imp.getGain27() != null ) add_gain( getP_Gain_ID("27"), imp.getGain27(),  "27" );
				if ( imp.getGain28() != null ) add_gain( getP_Gain_ID("28"), imp.getGain28(),  "28" );
				if ( imp.getGain29() != null ) add_gain( getP_Gain_ID("29"), imp.getGain29(),  "29" );
				if ( imp.getGain30() != null ) add_gain( getP_Gain_ID("30"), imp.getGain30(),  "30" );
				if ( imp.getGain31() != null ) add_gain( getP_Gain_ID("31"), imp.getGain31(),  "31" );
				if ( imp.getGain32() != null ) add_gain( getP_Gain_ID("32"), imp.getGain32(),  "32" );
				if ( imp.getGain33() != null ) add_gain( getP_Gain_ID("33"), imp.getGain33(),  "33" );
				if ( imp.getGain34() != null ) add_gain( getP_Gain_ID("34"), imp.getGain34(),  "34" );
				if ( imp.getGain35() != null ) add_gain( getP_Gain_ID("35"), imp.getGain35(),  "35" );
				if ( imp.getGain36() != null ) add_gain( getP_Gain_ID("36"), imp.getGain36(),  "36" );
				if ( imp.getGain37() != null ) add_gain( getP_Gain_ID("37"), imp.getGain37(),  "37" );
				if ( imp.getGain38() != null ) add_gain( getP_Gain_ID("38"), imp.getGain38(),  "38" );
				if ( imp.getGain39() != null ) add_gain( getP_Gain_ID("39"), imp.getGain39(),  "39" );
				if ( imp.getGain40() != null ) add_gain( getP_Gain_ID("40"), imp.getGain40(),  "40" );
				if ( imp.getGain41() != null ) add_gain( getP_Gain_ID("41"), imp.getGain41(),  "41" );
				if ( imp.getGain42() != null ) add_gain( getP_Gain_ID("42"), imp.getGain42(),  "42" );
				if ( imp.getGain43() != null ) add_gain( getP_Gain_ID("43"), imp.getGain43(),  "43" );
				if ( imp.getGain44() != null ) add_gain( getP_Gain_ID("44"), imp.getGain44(),  "44" );
				if ( imp.getGain45() != null ) add_gain( getP_Gain_ID("45"), imp.getGain45(),  "45" );
				if ( imp.getGain46() != null ) add_gain( getP_Gain_ID("46"), imp.getGain46(),  "46" );
				if ( imp.getGain47() != null ) add_gain( getP_Gain_ID("47"), imp.getGain47(),  "47" );
				if ( imp.getGain48() != null ) add_gain( getP_Gain_ID("48"), imp.getGain48(),  "48" );
				if ( imp.getGain49() != null ) add_gain( getP_Gain_ID("49"), imp.getGain49(),  "49" );
				if ( imp.getGain50() != null ) add_gain( getP_Gain_ID("50"), imp.getGain50(),  "50" );
				if ( imp.getGain51() != null ) add_gain( getP_Gain_ID("51"), imp.getGain51(),  "51" );
				if ( imp.getGain52() != null ) add_gain( getP_Gain_ID("52"), imp.getGain52(),  "52" );
				if ( imp.getGain53() != null ) add_gain( getP_Gain_ID("53"), imp.getGain53(),  "53" );
				if ( imp.getGain54() != null ) add_gain( getP_Gain_ID("54"), imp.getGain54(),  "54" );
				if ( imp.getGain55() != null ) add_gain( getP_Gain_ID("55"), imp.getGain55(),  "55" );
				if ( imp.getGain56() != null ) add_gain( getP_Gain_ID("56"), imp.getGain56(),  "56" );
				if ( imp.getGain57() != null ) add_gain( getP_Gain_ID("57"), imp.getGain57(),  "57" );
				if ( imp.getGain58() != null ) add_gain( getP_Gain_ID("58"), imp.getGain58(),  "58" );
				if ( imp.getGain59() != null ) add_gain( getP_Gain_ID("59"), imp.getGain59(),  "59" );
				if ( imp.getGain60() != null ) add_gain( getP_Gain_ID("60"), imp.getGain60(),  "60" );
				if ( imp.getGain61() != null ) add_gain( getP_Gain_ID("61"), imp.getGain61(),  "61" );
				if ( imp.getGain62() != null ) add_gain( getP_Gain_ID("62"), imp.getGain62(),  "62" );
				if ( imp.getGain63() != null ) add_gain( getP_Gain_ID("63"), imp.getGain63(),  "63" );
				if ( imp.getGain64() != null ) add_gain( getP_Gain_ID("64"), imp.getGain64(),  "64" );
				if ( imp.getGain65() != null ) add_gain( getP_Gain_ID("65"), imp.getGain65(),  "65" );
				if ( imp.getGain66() != null ) add_gain( getP_Gain_ID("66"), imp.getGain66(),  "66" );
				if ( imp.getGain67() != null ) add_gain( getP_Gain_ID("67"), imp.getGain67(),  "67" );
				if ( imp.getGain68() != null ) add_gain( getP_Gain_ID("68"), imp.getGain68(),  "68" );
				if ( imp.getGain69() != null ) add_gain( getP_Gain_ID("69"), imp.getGain69(),  "69" );
				if ( imp.getGain70() != null ) add_gain( getP_Gain_ID("70"), imp.getGain70(),  "70" );
				if ( imp.getGain71() != null ) add_gain( getP_Gain_ID("71"), imp.getGain71(),  "71" );
				if ( imp.getGain72() != null ) add_gain( getP_Gain_ID("72"), imp.getGain72(),  "72" );
				if ( imp.getGain73() != null ) add_gain( getP_Gain_ID("73"), imp.getGain73(),  "73" );
				if ( imp.getGain74() != null ) add_gain( getP_Gain_ID("74"), imp.getGain74(),  "74" );
				if ( imp.getGain75() != null ) add_gain( getP_Gain_ID("75"), imp.getGain75(),  "75" );
				if ( imp.getGain76() != null ) add_gain( getP_Gain_ID("76"), imp.getGain76(),  "76" );
				if ( imp.getGain77() != null ) add_gain( getP_Gain_ID("77"), imp.getGain77(),  "77" );
				if ( imp.getGain78() != null ) add_gain( getP_Gain_ID("78"), imp.getGain78(),  "78" );
				if ( imp.getGain79() != null ) add_gain( getP_Gain_ID("79"), imp.getGain79(),  "79" );
				if ( imp.getGain80() != null ) add_gain( getP_Gain_ID("80"), imp.getGain80(),  "80" );
				if ( imp.getGain81() != null ) add_gain( getP_Gain_ID("81"), imp.getGain81(),  "81" );
				if ( imp.getGain82() != null ) add_gain( getP_Gain_ID("82"), imp.getGain82(),  "82" );
				if ( imp.getGain83() != null ) add_gain( getP_Gain_ID("83"), imp.getGain83(),  "83" );
				if ( imp.getGain84() != null ) add_gain( getP_Gain_ID("84"), imp.getGain84(),  "84" );
				if ( imp.getGain85() != null ) add_gain( getP_Gain_ID("85"), imp.getGain85(),  "85" );
				if ( imp.getGain86() != null ) add_gain( getP_Gain_ID("86"), imp.getGain86(),  "86" );
				if ( imp.getGain87() != null ) add_gain( getP_Gain_ID("87"), imp.getGain87(),  "87" );
				if ( imp.getGain88() != null ) add_gain( getP_Gain_ID("88"), imp.getGain88(),  "88" );
				if ( imp.getGain89() != null ) add_gain( getP_Gain_ID("89"), imp.getGain89(),  "89" );
				if ( imp.getGain90() != null ) add_gain( getP_Gain_ID("90"), imp.getGain90(),  "90" );
				if ( imp.getGain91() != null ) add_gain( getP_Gain_ID("91"), imp.getGain91(),  "91" );
				if ( imp.getGain92() != null ) add_gain( getP_Gain_ID("92"), imp.getGain92(),  "92" );
				if ( imp.getGain93() != null ) add_gain( getP_Gain_ID("93"), imp.getGain93(),  "93" );
				if ( imp.getGain94() != null ) add_gain( getP_Gain_ID("94"), imp.getGain94(),  "94" );
				if ( imp.getGain95() != null ) add_gain( getP_Gain_ID("95"), imp.getGain95(),  "95" );
				if ( imp.getGain96() != null ) add_gain( getP_Gain_ID("96"), imp.getGain96(),  "96" );
				
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
			System.err.println( "error : " + e);
		}
		return "";
	}

	
	public int getP_Deduction_ID( String value )
	{
		int id = 0;
		String sql = "Select P_Deduction_ID from P_Deduction where value = rtrim('" + value + "')";
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
			System.err.println("Import getP_Deduction_ID - " + e);
		}
		
		return id;
		
	}

	public int getP_Gain_ID( String value )
	{
		int id = 0;
		String sql = "Select P_Gain_ID from P_Gain where value = rtrim('" + value + "')";
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
			System.err.println("ImportYTD getP_Gain_ID - " + e);
		}
		
		return id;
		
	}

	public int getP_Taxable_Benefit_ID( String value )
	{
		int id = 0;
		String sql = "Select P_Taxable_Benefit_ID from P_Taxable_Benefit where value = rtrim('" + value + "')";
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
			System.err.println("ImportYTD getP_Taxable_Benefit_ID - " + e);
		}
		
		return id;
		
	}

	private void add_deduction( int P_Deduction_ID, BigDecimal employeePart, String deduction )
	{
		if ( P_Deduction_ID == 0 && employeePart.compareTo(Env.ZERO) != 0 )
		{
			if ( imp.getI_ErrorMsg() == null)
				imp.setI_ErrorMsg("");
			
			imp.setI_ErrorMsg( imp.getI_ErrorMsg() + " Déduction non trouvé -  " + deduction  + "\n");
			return;
		}

		if ( employeePart.compareTo(Env.ZERO) == 0 )
			return;

		// Créer les déductions que ne sont pas présent dans le dossier employé.
		P_Employee_Deduction employeeDeduction = P_Employee_Deduction.get(getCtx(), employee.getP_Employee_ID(), P_Deduction_ID, TimeUtil.getDay(2007, 01, 01), trxName);
		employeeDeduction.setAD_Org_ID( 0);
		employeeDeduction.setEffectIn(TimeUtil.getDay(2007, 01, 01));
//			employeeDeduction.setEffectTo(period.getEndDate());
		employeeDeduction.setIsActive(true);
		employeeDeduction.setP_Deduction_ID(P_Deduction_ID);
		employeeDeduction.setP_Employee_ID(employee.getP_Employee_ID());
		if ( isMax )
			employeeDeduction.setNonAnnualMaximum( employeePart );
		else
			// deduction 66 Kicker Company.
			if ( P_Deduction_ID == 1002630)
				employeeDeduction.setEmployeeAmount( employeePart );
		    // deduction 50
			else if ( P_Deduction_ID == 1002615 )
				employeeDeduction.setEmployeeRate( employeePart.divide(new BigDecimal(100),2, BigDecimal.ROUND_HALF_UP) );
			else
				employeeDeduction.setEmployerAmount( employeePart );
		employeeDeduction.save();
		
		
	}

	private void add_gain( int P_Gain_ID, BigDecimal Hours, String gain )
	{
		boolean taxablebenefit = false;
		int P_Taxable_Benefit_ID = 0;
		if ( P_Gain_ID == 0 )
		{
			P_Gain_ID = this.getP_Taxable_Benefit_ID(gain);
			if ( P_Gain_ID != 0 )
			{
				P_Taxable_Benefit_ID = P_Gain_ID;
				taxablebenefit = true;
			}
		}
			
		
		if ( P_Gain_ID == 0 && ( Hours.compareTo(Env.ZERO) != 0 ) )
		{
			if ( imp.getI_ErrorMsg() == null)
				imp.setI_ErrorMsg("");
			
			imp.setI_ErrorMsg( imp.getI_ErrorMsg() + " Gain non trouvé -  " + gain  + "\n");
			return;
		}

		if ( ( Hours.compareTo(Env.ZERO) == 0 ) )
			return;
		
		if ( ! taxablebenefit )
		{
			P_Standard_Time st = new P_Standard_Time( getCtx(), -1, trxName );
			st.setP_Employee_ID( employee.getP_Employee_ID() );
			st.setStartDate( TimeUtil.getDay(2007, 01, 01));
			st.setP_Gain_ID(P_Gain_ID);
			st.setStandard_Time_Amount(Hours);
			st.save();
		}
		else
		{
			P_Employee_Taxable_Benefit tb = P_Employee_Taxable_Benefit.get( getCtx(), employee.getP_Employee_ID(), P_Taxable_Benefit_ID, trxName);
			tb.setP_Employee_ID(employee.getP_Employee_ID());
			tb.setP_Taxable_Benefit_ID(P_Taxable_Benefit_ID);
			tb.setEffectIn(TimeUtil.getDay(2007, 01, 01));
			tb.setTaxB_Fixed_Amount( Hours );
			tb.save();
		}
	}

}
