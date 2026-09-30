package solstice.process;

import java.io.*;

import jxl.*;

import java.util.*;
import java.util.logging.Level;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import jxl.write.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

import solstice.model.P_Deduction;
import solstice.model.P_Gain;
import solstice.model.P_Period;
import solstice.utils.PgiUtil;

public class Analyse_Remuneration extends SvrProcess
{
	/** Logger */
	private static CLogger log = CLogger.getCLogger(Analyse_Remuneration.class);
	private String P_Period_Start = "2018-01";
	private String P_Period_End   = "2018-25";
	
	private int P_Period_Start_ID = 1002200;
	private int P_Period_End_ID   = 1002224;
	
	private int P_Employee_ID = 0;
	private int P_Payment_Group_ID = 0;
	private int Org_ID = 0;
    private int P_Distribution_Booklet_ID = 0;
	private int C_Activity_ID = 0;
	private int P_Department_ID = 0;
	private int P_Workplace_ID = 0;
	private int Taxation_Region_ID = 0;
	private int P_LongTermLeave_ID = 0;
	private int P_Post_ID = 0;
	private int P_Job_Type_ID = 0;
	private Timestamp DateHired = null;
	private Timestamp DateLayoff = null;
	
	
//	private int P_Gain_ID = 1;
	private String[] EarningList = new String[] {"17", "18", "19", "29"};
	
/*	
	public Analyse_Remuneration( boolean standalon )
	{
		try
		{ 
			doIt();
        }
        catch (Exception e)
        {
            log.log(Level.WARNING, "Analyse_Remuneration", e);
        }
        
	}
*/
	
	protected void prepare() 
	{
		ProcessInfoParameter[] para = getParameter();
		String paramName = "";
		//Read the parameters
		for(int i=0; i < para.length; i++)
		{ 
			paramName = para[i].getParameterName();
			if (paramName.equals("P_Period_Start_ID"))
				this.P_Period_Start = (String)para[i].getParameter();
			else if (paramName.equals("P_Period_End_ID"))
				this.P_Period_End = (String)para[i].getParameter();
			else if (paramName.equals("P_Payment_Group_ID"))
				P_Payment_Group_ID = para[i].getParameterAsInt();
			else if (paramName.equals("AD_Org_ID"))
				Org_ID = para[i].getParameterAsInt();
			else if (paramName.equals("P_Department_ID"))
				P_Department_ID = para[i].getParameterAsInt();
			else if (paramName.equals("P_Employee_ID"))
				P_Employee_ID = para[i].getParameterAsInt();

			else if (paramName.equals("Workplace_ID"))
				P_Workplace_ID = para[i].getParameterAsInt();

			else if (paramName.equals("Taxation_Region_ID"))
				Taxation_Region_ID = para[i].getParameterAsInt();
			
			else if (paramName.equals("P_LongTermLeave_ID"))
				P_LongTermLeave_ID = para[i].getParameterAsInt();
			
			else if (paramName.equals("DateHired"))
				DateHired = (Timestamp)para[i].getParameter();
			else if (paramName.equals("DateLayoff"))
				DateLayoff = (Timestamp)para[i].getParameter();
			else if (paramName.equals("P_Post_ID"))
				P_Post_ID = para[i].getParameterAsInt();
			
			else if ( paramName.equals("P_Distribution_Booklet_ID"))
				P_Distribution_Booklet_ID = para[i].getParameterAsInt();
			else if (paramName.equals("C_Activity_ID"))
				C_Activity_ID = para[i].getParameterAsInt();
			else if (paramName.equals("P_Job_Type_ID"))
				P_Job_Type_ID = para[i].getParameterAsInt();
			
		}
	}

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	protected String doIt() throws Exception 
	{
		System.out.println("Analyse_Remuneration   $Revision: 1.0 $");
		System.out.println("----------------------------------");
		//
		int exported = 0;
	    try
	    {
     	  String Path = PgiUtil.getSolsticeParameter(getCtx(), "TransfertPath");
	    	
          Calendar toDay = Calendar.getInstance();
	      String filename =  "Analyse_Remuneration" + "_" + String.valueOf(toDay.get(Calendar.YEAR)+ "." 
	              + "00".substring(0, 2 - String.valueOf(toDay.get(Calendar.MONTH) + 1).trim().length()) + String.valueOf(toDay.get(Calendar.MONTH) + 1) + "."
	              +  String.valueOf(toDay.get(Calendar.DAY_OF_MONTH))  );

	      String fullFileName = Path + filename+ ".xls";

	      File file = new File( fullFileName);
	      int no = 0;
	      while ( file.exists() )
	      {
	    	  no = no + 1;
	    	  fullFileName =  Path + filename + "(" + no + ").xls";
		      file = new File( fullFileName );
	      }
		  log.info("Write to : " + fullFileName  );
		  
	      WorkbookSettings ws = new WorkbookSettings();
	      ws.setLocale(new Locale("en", "EN"));
//	      ws.setLocale(new Locale("fr", "FR"));
	      
	      WritableWorkbook workbook = Workbook.createWorkbook(new File(fullFileName), ws);
	      WritableSheet s = workbook.createSheet("Data", 0);
	      exported = writeDataSheet(s);
	      workbook.write();
	      workbook.close();      

			PgiUtil.promptOpenExportedFile(fullFileName);

	    }
	    catch (IOException e)
	    {
	      e.printStackTrace();
	    }
	    catch (WriteException e)
	    {
	      e.printStackTrace();
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }

	    return "@Processed@ " + exported;
	}
	
	private void createEarning( WritableSheet s, int currentRow, int p_employee_id ) throws Exception
	{
		String sql  = " SELECT P_Gain_ID, P_Gain.Value + ' - ' + P_Gain.Name as Name FROM [dbo].[P_Gain] "
				+ " WHERE Exists( SELECT 1 FROM P_Payment_Gain Where P_Payment_Gain.P_Gain_ID = P_Gain.P_Gain_ID AND P_Payment_Gain.P_Period_ID Between "  + P_Period_Start_ID + " AND " + P_Period_End_ID + " ) "
				+ " Order By CASE WHEN P_Gain.Value in ( '01', '04', '05', '06', '08', '10', '101', '105', '11', '117', '12', '13', '14', '15', '16', '17', '79', '21', '23', '24', '25', '26','27','29','65' ) THEN 0 ELSE 1 END,  Value ";

		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();

		    int col = 16;

			while (rs.next())
			{
//				P_Gain Gain = P_Gain.get( Env.getCtx(), rs.getInt("P_Gain_ID"), null);
				
				Label l = new Label(col,3, rs.getString("Name"),cf);
			    s.addCell(l);

			    createTotalEarning( s, currentRow, col, p_employee_id, rs.getInt( "P_Gain_ID" ) );
				col = col + 1;
				
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("ERROR - " + e);
		}


	}


	private void createTotalEarning( WritableSheet s, int currentRow, int col, int p_employee_id, int p_gain_id ) throws Exception
	{

		String sql  = "select isnull(sum(AmountCalc),0) as AmountCalc  from p_payment, p_payment_gain"
				    + " where P_Payment.P_Period_ID between " + P_Period_Start_ID + " AND " + P_Period_End_ID + " AND  p_payment.p_employee_id = " + p_employee_id + " and p_payment_gain.p_payment_id = p_payment.p_payment_id and p_payment_gain.p_gain_id = " + p_gain_id ;

		sql += " AND p_payment.AD_Org_ID in ( SELECT AD_Org_ID FROM AD_Role_OrgAccess ra WHERE ra.AD_Role_ID= " + Env.getAD_Role_ID( Env.getCtx() ) + " AND ra.IsActive='Y')";

		
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
//				P_Gain Gain = P_Gain.get( Env.getCtx(), rs.getInt("P_Gain_ID"), null);
				
			    Number nI1 = new Number(col,currentRow, rs.getFloat("AmountCalc"),i1);
			    s.addCell(nI1);
			    
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("ERROR - " + e);
		}


 
		
	}
	
	private int startGroup = 0;
	private int endGroup = 0;
	private int startGroupEarning = 0;
	private int endGroupEarning = 0;
	private int currentRow = 0;
	private WritableCellFormat cft;
	private WritableCellFormat i1;
	private WritableCellFormat i2;
	private WritableCellFormat cf;
	private int nbrEarningGroup = 0;
	private Integer[] rowGroupEarning;
	private Integer[] rowGroup = new Integer[2];
	private int nbrGroup = 0;
	
	
	private int writeDataSheet(WritableSheet s) 
	   throws WriteException
	  {
	    /* Format the Font */
	    WritableFont wf = new WritableFont(WritableFont.ARIAL, 
	      10, WritableFont.BOLD);
	    cf = new WritableCellFormat(wf);
	    cf.setWrap(true);
	    Colour c = Colour.GREY_25_PERCENT;
	    cf.setBackground(c);
	    cf.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);

	    i1 = new WritableCellFormat(NumberFormats.FLOAT);
	    i2 = new WritableCellFormat(NumberFormats.FLOAT);
//	    i2.setBackground(c);
	    i2.setBorder(jxl.format.Border.TOP, jxl.format.BorderLineStyle.THIN);

	    cft = new WritableCellFormat(wf);
	    cft.setBorder(jxl.format.Border.TOP, jxl.format.BorderLineStyle.THIN);

//	  	P_Gain Gain = P_Gain.get( Env.getCtx(), P_Gain_ID, null);
	  	P_Period period1 = P_Period.get( Env.getCtx(), P_Period_Start_ID, null );
	  	P_Period period2 = P_Period.get( Env.getCtx(), P_Period_End_ID, null );

	  	P_Period_Start = period1.getName();
	  	P_Period_End = period2.getName();
	  	
	  	Label l;
	  	
	    l = new Label(0,currentRow,Msg.translate(Env.getCtx(), "P_Gain_ID" ),cf);
	    s.addCell(l);

	    String sEarningList = "";
		for (int j = 0; j < EarningList.length; j++)
		{
			sEarningList = sEarningList + " - " + EarningList[j];
		}

/*	    l = new Label(1,currentRow, sEarningList ,cf);
	    s.addCell(l);
	    currentRow = currentRow + 1;
*/

	    l = new Label(0,currentRow,Msg.translate(Env.getCtx(), "P_Period_ID" ),cf);
	    s.addCell(l);
	    l = new Label(1,currentRow, period1.getName() + " - " + period2.getName() ,cf);
	    s.addCell(l);
	    currentRow = currentRow + 3;

	    /* Creates Label and writes date to one cell of sheet*/
	    l = new Label(0,currentRow, "Division" ,cf);
	    s.addCell(l);
	    l = new Label(1,currentRow,"Departement",cf);
	    s.addCell(l);
	    l = new Label(2,currentRow,"Crew cost",cf);
	    s.addCell(l);
	    l = new Label(3,currentRow,"Date Layoff",cf);
	    s.addCell(l);
	    l = new Label(4,currentRow,"Cost center",cf);
	    s.addCell(l);
	    l = new Label(5,currentRow,"EE Number",cf);
	    s.addCell(l);
	    l = new Label(6,currentRow,"First Name",cf);
	    s.addCell(l);
	    l = new Label(7,currentRow,"Last Name",cf);
	    s.addCell(l);
	    l = new Label(8,currentRow,"Status",cf);
	    s.addCell(l);
	    l = new Label(9,currentRow,"Employment Status",cf);
	    s.addCell(l);
	    l = new Label(10,currentRow,"Job Title",cf);
	    s.addCell(l);
	    l = new Label(11,currentRow,"Activity",cf);
	    s.addCell(l);
	    l = new Label(12,currentRow,"Status",cf);
	    s.addCell(l);
	    l = new Label(13,currentRow,"Base Salary",cf);
	    s.addCell(l);
	    l = new Label(14,currentRow,"Fix Earning Amt",cf);
	    s.addCell(l);
	    l = new Label(15,currentRow,"Fix Earning Hours",cf);
	    s.addCell(l);
//	    l = new Label(5,i,Msg.translate(Env.getCtx(), "P_Gain_ID" ),cf);
//	    s.addCell(l);
//	    l = new Label(5,currentRow,Msg.translate(Env.getCtx(), "AmountCalc" ),cf);
//	    s.addCell(l);
	    
	    		 		 	
	    
/*	    l = new Label(6,currentRow,Msg.translate(Env.getCtx(), "QuantityCalc" ),cf);
	    s.addCell(l);
	    l = new Label(7,currentRow,Msg.translate(Env.getCtx(), "P_Gain_ID" ),cf);
	    s.addCell(l);
*/
    	s.setColumnView(0, 30);
    	s.setColumnView(1, 30);
    	s.setColumnView(2, 30);
    	s.setColumnView(3, 30);
    	s.setColumnView(4, 30);
    	s.setColumnView(5, 30);
    	s.setColumnView(6, 30);
    	s.setColumnView(7, 30);
    	s.setColumnView(8, 30);
    	s.setColumnView(9, 30);
    	s.setColumnView(10, 30);
    	s.setColumnView(11, 30);
    	s.setColumnView(12, 30);
    	s.setColumnView(13, 30);
    	s.setColumnView(14, 30);
    	s.setColumnView(15, 30);

	    WritableFont wf2 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
	  	    WritableCellFormat cf2 = new WritableCellFormat(wf2);
	  	    cf2.setWrap(true);

	  	    
	    String sqlEarningList = "'" + EarningList[0] + "'";
		for (int j = 1; j < EarningList.length; j++)
		{
			sqlEarningList = sqlEarningList + ", '" + EarningList[j] + "'";
		}
		
		String sql = " ";
	  	/*
		String sql = "SELECT AD_ORG.NAME "
				   + ", C_Activity.NAME "
				   + ", P_Employee.VALUE "
				   + ", P_Employee.NAME "
				   + ", P_Period.NAME "
				   + ", P_Gain.Value " //  P_Gain.NAME "
				   + ", SUM( AmountCalc ) "
				   + ", SUM( QuantityCalc ) "
				   + ", MAX( P_Gain.P_Gain_ID ) as P_Gain_ID"
				   + " FROM dbo.P_PAYMENT "
				   + "INNER JOIN dbo.P_PAYMENT_Gain ON P_PAYMENT_Gain.P_PAYMENT_ID = dbo.P_PAYMENT.P_PAYMENT_ID "
				   + "INNER JOIN dbo.AD_ORG ON AD_ORG.AD_ORG_ID = P_PAYMENT.AD_ORG_ID "
				   + "INNER JOIN dbo.P_EMPLOYEE ON P_EMPLOYEE.P_EMPLOYEE_ID = P_PAYMENT.P_EMPLOYEE_ID "
				   + "INNER JOIN dbo.P_Gain ON dbo.P_Gain.P_Gain_ID = dbo.P_PAYMENT_Gain.P_Gain_ID "
				   + "INNER JOIN dbo.C_ACTIVITY ON dbo.C_ACTIVITY.C_ACTIVITY_ID = dbo.P_PAYMENT.C_Activity_ID "
				   + "INNER JOIN dbo.P_PERIOD ON P_PERIOD.P_PERIOD_ID = P_Payment.P_PERIOD_ID "
				   + " WHERE P_Gain.value in ( " + sqlEarningList + ")" 
				   + "   AND P_Payment.P_PERIOD_ID BETWEEN " + P_Period_Start_ID + " AND " + P_Period_End_ID
				   + "GROUP BY "
				   + "AD_ORG.NAME "
				   + ", C_Activity.NAME "
				   + ", P_Employee.VALUE "
				   + ", P_Employee.NAME "
				   + ", P_Gain.VALUE "
				   + ", P_Gain.NAME "
				   + ", P_Period.NAME "
				   + " ORDER BY "
				   + "AD_ORG.NAME "
				   + ", C_Activity.NAME "
				   + ", P_Employee.VALUE "
				   + ", P_Employee.NAME "
				   + ", P_Gain.VALUE "
				   + ", P_Period.NAME "
				   ;
		*/
		sql = "SELECT Ad_Org.Value  AS Division, "
				+ "P_Department.Value AS Department,  "
				+ "C_Activity.VALUE AS Crew_Cost,  "
			+ "P_Employee.DATELAYOFF,  "
	       + "ISNULL( dbo.P_Employee_Cost_Center( P_Employee.P_Employee_ID ), ' ') AS Cost_Center,  "
			+ "P_Workplace.Value + ' - ' + P_Workplace.Name as Base,  "
	       + "P_Employee.Value AS EmpNo,  "
	      + " P_Employee.FirstName,  "
	      + " P_Employee.SurName As LastName,  "
	      + " P_Employee.isactive,  "
		  + " P_Job_Type.Value Job_Type, "  
		  + " P_Post.Value + ' - ' + P_Post.Name Post,  "
		  + " P_LongTermLeave.Value + ' - ' + P_LongTermLeave.Name P_LongTermLeave,  "
		  + "   P_Assignment_Param.annual_salary,	  "

	      + " (  select isnull(sum(AmountCalc)  ,0)  from p_payment, p_payment_gain, p_gain where  P_Payment.P_Period_ID between period_start.P_Period_ID AND period_end.P_Period_ID AND  p_payment.p_employee_id = p_employee.p_employee_id and p_payment_gain.p_payment_id = p_payment.p_payment_id and p_payment_gain.p_gain_id = p_gain.p_gain_id and p_gain.value IN ( '01', '04', '05', '06', '08', '10', '101', '105', '11', '117', '12', '13', '14', '15', '16', '17', '79', '21', '23', '24', '25', '26','27','29','65') )  as FixEarnAmt,  "
	      + " (  select isnull(sum(QuantityCalc),0)  from p_payment, p_payment_gain, p_gain where P_Payment.P_Period_ID between period_start.P_Period_ID AND period_end.P_Period_ID AND  p_payment.p_employee_id = p_employee.p_employee_id and p_payment_gain.p_payment_id = p_payment.p_payment_id and p_payment_gain.p_gain_id = p_gain.p_gain_id and p_gain.value IN ( '01', '04', '05', '06', '08', '10', '101', '105', '11', '117', '12', '13', '14', '15', '16', '17', '79', '21', '23', '24', '25', '26','27','29','65' ))  as FixEarn , "
//	      + " (  select isnull(sum(AmountCalc)  ,0)  from p_payment, p_payment_gain, p_gain where P_Payment.P_Period_ID between period_start.P_Period_ID AND period_end.P_Period_ID AND  p_payment.p_employee_id = p_employee.p_employee_id and p_payment_gain.p_payment_id = p_payment.p_payment_id and p_payment_gain.p_gain_id = p_gain.p_gain_id and p_gain.value IN ( '02', '03', '20', '32', '33', '34', '35', '36', '37', '39', '40', '41', '45', '46', '47', '50', '53', '54', '56', '73', '78', '82', '87', '90', '93', '99', '100'      ) )  as VarEarnAmt, " 
//	      + " (  select isnull(sum(QuantityCalc),0)  from p_payment, p_payment_gain, p_gain where P_Payment.P_Period_ID between period_start.P_Period_ID AND period_end.P_Period_ID AND  p_payment.p_employee_id = p_employee.p_employee_id and p_payment_gain.p_payment_id = p_payment.p_payment_id and p_payment_gain.p_gain_id = p_gain.p_gain_id and p_gain.value IN ( '02', '03', '20', '32', '33', '34', '35', '36', '37', '39', '40', '41', '45', '46', '47', '50', '53', '54', '56', '73', '78', '82', '87', '90', '93', '99', '100' ))  as VarEarn, "
	      
	      + " P_Employee.P_Employee_ID "
	    
	   + " FROM AD_ORG "
	   + " INNER JOIN P_Employee ON AD_ORG.AD_Org_ID = P_Employee.AD_Org_ID "
	   + " INNER JOIN P_Workplace ON P_Workplace.P_Workplace_ID = P_Employee.P_Workplace_ID " 
	   + " INNER JOIN P_Job_Type ON P_Job_Type.P_Job_Type_ID = P_Employee.P_Job_Type_ID "
	   + " INNER JOIN P_PERIOD period_start ON PERIOD_start.P_Period_ID =  " + P_Period_Start_ID
	   + " INNER JOIN P_PERIOD period_end ON PERIOD_end.P_Period_ID =  " + P_Period_End_ID
	   + " LEFT OUTER JOIN P_Assignment ON P_Assignment.P_Employee_ID = P_Employee.P_Employee_ID and P_Assignment.isactive = 'Y' AND AssignmentType = 'P' "
	   + " LEFT OUTER jOIN P_POST on P_Post.P_Post_ID = P_Assignment.P_Post_ID "
	   + " LEFT OUTER JOIN P_Assignment_Param ON P_Assignment_Param.P_Assignment_ID = P_Assignment.P_Assignment_ID " 
	   + " LEFT OUTER JOIN C_Activity ON C_Activity.C_Activity_ID = P_Employee.C_Activity_ID "
	   + " LEFT OUTER JOIN P_Department ON P_Department.P_Department_ID = P_Employee.P_Department_ID "
	   + " LEFT OUTER JOIN P_LongTermLeave ON P_LongTermLeave.P_LongTermLeave_ID = P_Employee.P_LongTermLeave_ID "       
	   + " WHERE P_Assignment_Param.Effectin = ( Select Max( m.Effectin ) From P_Assignment_Param m Where m.P_Assignment_ID = P_Assignment.P_Assignment_ID  )  "
	   + " AND exists( Select 1 from P_Payment Where P_Payment.P_Employee_ID = P_Employee.P_Employee_ID And P_Payment.P_Period_ID between period_start.P_Period_ID AND period_end.P_Period_ID ) "
	   
	   ;

		
		if (P_Employee_ID > 0)
			sql += " AND P_Employee.P_Employee_ID = " + P_Employee_ID;
		
		if (P_Payment_Group_ID > 0)
			sql += " AND P_Employee.P_Payment_Group_ID = " + P_Payment_Group_ID;
		
		if (Org_ID > 0)
			sql += " AND P_Employee.AD_Org_ID = " + Org_ID;

		if (P_Distribution_Booklet_ID > 0)
			sql += " AND P_Employee.P_Distribution_Booklet_ID = " + P_Distribution_Booklet_ID;

		if (C_Activity_ID > 0)
			sql += " AND P_Employee.C_Activity_ID = " + C_Activity_ID;

		if (P_Department_ID > 0)
			sql += " AND P_Employee.P_Department_ID = " + P_Department_ID;

		if (P_Workplace_ID > 0)
			sql += " AND P_Employee.P_Workplace_ID = " + P_Workplace_ID;

		if (Taxation_Region_ID > 0)
			sql += " AND P_Employee.Taxation_Region_ID = " + Taxation_Region_ID;

		if (P_LongTermLeave_ID > 0)
			sql += " AND P_Employee.P_LongTermLeave_ID = " + P_LongTermLeave_ID;


		if (DateHired != null)
			sql += " AND P_Employee.DateHired >= " + DB.TO_DATE( DateHired );


		if (DateLayoff != null)
			sql += " AND P_Employee.DateLayoff >= " + DB.TO_DATE( DateLayoff );

		if (P_Job_Type_ID > 0)
			sql += " AND P_Employee.P_Job_Type_ID = " + P_Job_Type_ID;	
		
		if (P_Post_ID > 0)
			sql += " AND P_Assignment.P_Post_ID = " + P_Post_ID;
		
		//2019-05-08 ajout sécurité organisation
//		if ( Org_ID == 0 )
		sql += " AND AD_Org.AD_Org_ID in ( SELECT AD_Org_ID FROM AD_Role_OrgAccess ra WHERE ra.AD_Role_ID= " + Env.getAD_Role_ID( Env.getCtx() ) + " AND ra.IsActive='Y')";

		
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();

			rowGroupEarning = new Integer[ EarningList.length ];
			
			while (rs.next())
			{
				currentRow = currentRow + 1;
/*				
			    if ( oldEmployee.length() == 0)
			    {
			    	oldEmployee = rs.getString(3);
			    	startGroup = currentRow  ;
			    }
			    if ( oldEarning.length() == 0)
			    {
			    	oldEarning = rs.getString(6);
			    	oldGainID   = rs.getInt("P_Gain_ID");
			    	startGroupEarning = currentRow  ;
			    }

			    
			    if ( oldEarning.equals( rs.getString(6) ) == false || oldEmployee.equals( rs.getString(3) ) == false )
			    {
			    	log.info( "   Total Gain " + rs.getString(6) ); 
			    	createTotalEarning( s, rs );
			    }

			    if ( oldEmployee.equals( rs.getString(3) ) == false  )
			    {
			    	log.info( "Total employee " + rs.getString(3) ); 
//			    	createTotalEarning( s, rs );
			    	createTotalEmployee( s, rs );
			    }

*/
			    
			    l = new Label(0,currentRow,rs.getString(1),cf2);
			    s.addCell(l);
			    l = new Label(1,currentRow,rs.getString(2),cf2);
			    s.addCell(l);
			    l = new Label(2,currentRow,rs.getString(3),cf2);
			    s.addCell(l);
			    l = new Label(3,currentRow,rs.getString(4),cf2);
			    s.addCell(l);
			    l = new Label(4,currentRow,rs.getString(5),cf2);
			    s.addCell(l);
			    l = new Label(5,currentRow,rs.getString(6),cf2);
			    s.addCell(l);
			    l = new Label(6,currentRow,rs.getString(7),cf2);
			    s.addCell(l);
			    l = new Label(7,currentRow,rs.getString(8),cf2);
			    s.addCell(l);
			    l = new Label(8,currentRow,rs.getString(9),cf2);
			    s.addCell(l);
			    l = new Label(9,currentRow,rs.getString(10),cf2);
			    s.addCell(l);
			    l = new Label(10,currentRow,rs.getString(11),cf2);
			    s.addCell(l);
			    l = new Label(11,currentRow,rs.getString(12),cf2);
			    s.addCell(l);
			    l = new Label(12,currentRow,rs.getString(13),cf2);
			    s.addCell(l);
			    l = new Label(13,currentRow,rs.getString(14),cf2);
			    s.addCell(l);
			    Number nI1 = new Number(14,currentRow, rs.getFloat(15),i1);
			    s.addCell(nI1);
			    Number nI2 = new Number(15,currentRow, rs.getFloat(16),i1);
			    s.addCell(nI2);
			    
			    createEarning( s , currentRow, rs.getInt("P_Employee_ID") );
			    
//			    l = new Label(5,i,rs.getString(6),cf2);
//			    s.addCell(l);
/*			    
			    Number nI1 = new Number(5,currentRow, rs.getFloat( 7) ,i1);
			    s.addCell(nI1);
			    nI1 = new Number(6,currentRow, rs.getFloat(8) ,i1);
			    s.addCell(nI1);			
				StringBuffer buf = new StringBuffer();
				buf.append( "F" + currentRow+1 + " - G" + currentRow+1 );

				l = new Label(7,currentRow,rs.getString(6),i1);
			    s.addCell(l);
*/			    
			}
/*
			currentRow = currentRow + 1;
	    	createTotalEarning( s, null );
	    	
//	    	currentRow = currentRow + 1;
	    	createTotalEmployee( s, null );
		    
	    	currentRow = currentRow + 1;

	    	createTotal( s );
*/
			rs.close();
			pstmt.close();
			pstmt = null;

		}
		catch (Exception e)
		{
			System.err.println("Analyse_Remuneration - " + e);
		}


		return currentRow;
	  }
/*
		public static void main (String[] args)
		{
			org.compiere.Compiere.startupEnvironment(true);
			
			log.info("----------------------------------");
			new Analyse_Remuneration( true );
			
			log.info("----------------------------------");
		}
*/		
}
