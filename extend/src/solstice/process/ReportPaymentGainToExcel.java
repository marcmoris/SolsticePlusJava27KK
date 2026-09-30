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

public class ReportPaymentGainToExcel extends SvrProcess
{
	/** Logger */
	private static CLogger log = CLogger.getCLogger(ReportPaymentGainToExcel.class);
	private int P_Period_Start_ID = 1002200;
	private int P_Period_End_ID   = 1002224;
//	private int P_Gain_ID = 1;
	private String[] EarningList = new String[] {"17", "18", "19", "29"};
	
	public ReportPaymentGainToExcel()
	{
		try
		{ 
			doIt();
        }
        catch (Exception e)
        {
            log.log(Level.WARNING, "ReportPaymentGainToExcel", e);
        }
        
	}

	
	protected void prepare() 
	{
		ProcessInfoParameter[] para = getParameter();
		String paramName = "";
		//Read the parameters
		for(int i=0; i < para.length; i++)
		{ 
			paramName = para[i].getParameterName();
			if (paramName.equals("P_Period_Start_ID")){
				this.P_Period_Start_ID = para[i].getParameterAsInt();
			}else if (paramName.equals("P_Period_End_ID")){
				this.P_Period_End_ID = para[i].getParameterAsInt();
			}
//			else if ( paramName.equals("P_Gain_ID"))
//				this.P_Gain_ID = para[i].getParameterAsInt();
		}
	}

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	protected String doIt() throws Exception 
	{
		System.out.println("ReportPaymentGainToExcel   $Revision: 1.0 $");
		System.out.println("----------------------------------");
		//
		int exported = 0;
	    try
	    {
     	  String Path = PgiUtil.getSolsticeParameter(getCtx(), "TransfertPath");
	    	
          Calendar toDay = Calendar.getInstance();
	      String filename =  getProcessInfo().getTitle() + "_" + String.valueOf(toDay.get(Calendar.YEAR)+ "." 
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
	

	private void createTotalEarning( WritableSheet s, ResultSet rs) throws Exception
	{
    	P_Gain Gain = P_Gain.get( Env.getCtx(), oldGainID, null);

		if ( rs != null)
		{
	    	oldGainID   = rs.getInt("P_Gain_ID");
	    	oldEarning  = rs.getString(6);
		}
    	endGroupEarning = currentRow;
		s.setRowGroup(startGroupEarning, endGroupEarning -1 , true);

		
	    Formula f0 = new Formula(0,currentRow, "A" + endGroupEarning , cft );
	    s.addCell(f0);
	    Formula f1 = new Formula(1,currentRow, "B" + endGroupEarning , cft );
	    s.addCell(f1);

	    Formula f2 = new Formula(2,currentRow, "C" + endGroupEarning , cft );
	    s.addCell(f2);

	    Formula f3 = new Formula(3,currentRow, "D" + endGroupEarning , cft );
	    s.addCell(f3);

	    Label l = new Label(4,currentRow, Gain.getValue() + " " + Gain.getTrlName() + " Total",cft);
	    s.addCell(l);
		// Si le excel doit être en Francais il faut utilisé le formule SOMME		
	    Formula f6 = new Formula(5,currentRow, "SUM(F" + (startGroupEarning + 1) + ":F" + endGroupEarning + ")", i2 );
	    s.addCell(f6);

		// Si le excel doit être en Francais il faut utilisé le formule SOMME		
	    Formula f7 = new Formula(6,currentRow, "SUM(G" + (startGroupEarning + 1) + ":G" + endGroupEarning + ")", i2 );
	    s.addCell(f7);

		// Si le excel doit être en Francais il faut utilisé le formule SOMME		
//	    Formula f8 = new Formula(7,currentRow, "H" + endGroupEarning , cft );
//	    s.addCell(f8);

	    currentRow = currentRow + 1;

	    rowGroupEarning[nbrEarningGroup] = currentRow;
		nbrEarningGroup = nbrEarningGroup + 1;

	    startGroupEarning = currentRow ;

	}
	

	private void createTotalEmployee( WritableSheet s, ResultSet rs) throws Exception
	{
		if ( rs != null)
			oldEmployee  = rs.getString(3);
		
    	endGroup = currentRow;
		s.setRowGroup(startGroup, endGroup -1 , true);

	    Formula f0 = new Formula(0,currentRow, "A" + endGroup , cft );
	    s.addCell(f0);
	    Formula f1 = new Formula(1,currentRow, "B" + endGroup , cft );
	    s.addCell(f1);

	    Formula f2 = new Formula(2,currentRow, "C" + endGroup , cft );
	    s.addCell(f2);

	    Formula f3 = new Formula(3,currentRow, "D" + endGroup , cft );
	    s.addCell(f3);

	    Label l = new Label(4,currentRow,"Total Employé",cft);
	    s.addCell(l);
	    String formula = "";
		for (int j = 0; j < rowGroupEarning.length; j++)
		{
			if ( rowGroupEarning[j] != null)
			{
				formula = formula + "F" + rowGroupEarning[j];
				if ( j != rowGroupEarning.length )
					formula = formula + " + ";
			}
		}
		if ( formula.endsWith( "+ "))
			formula = formula.substring(0, formula.length() - 2);

//	    Formula f6 = new Formula(5,i, "SUM(F" + (startGroup + 1) + ":F" + endGroup + ")", i2 );
	    Formula f6 = new Formula(5,currentRow, formula, i2 );
	    s.addCell(f6);

//	    Formula f7 = new Formula(6,i, "SUM(G" + (startGroup + 1) + ":G" + endGroup + ")", i2 );
	    formula = formula.replace("F", "G");
	    Formula f7 = new Formula(6,currentRow, formula, i2 );
	    s.addCell(f7);

	    Formula f8 = new Formula(7,currentRow ,"F" + (currentRow + 1) + "-G" + (currentRow +1)  ,i2 );
	    s.addCell(f8);


	    currentRow = currentRow + 1;

	    rowGroup[ nbrGroup ] = currentRow;
	    nbrGroup = nbrGroup + 1;
	    rowGroup = (Integer[])PgiUtil.resizeArray( rowGroup, nbrGroup + 1);

	    startGroup = currentRow ;
	    
	    nbrEarningGroup = 0;
	    rowGroupEarning = new Integer[ EarningList.length ];

	    startGroupEarning = currentRow ;

	}

	private void createTotal( WritableSheet s ) throws Exception
	{
		Label l = new Label(0,currentRow,"Grand Total",cft);
	    s.addCell(l);

	    String formula = "";
		for (int j = 0; j < rowGroup.length; j++)
		{
			if ( rowGroup[j] != null)
			{
				formula = formula + "F" + rowGroup[j];
				if ( j != rowGroup.length )
					formula = formula + " + ";
			}
		}
		if ( formula.endsWith( "+ "))
			formula = formula.substring(0, formula.length() - 2);

		
	    Formula f6 = new Formula(5,currentRow, formula, i2 );
	    s.addCell(f6);

	    formula = formula.replace("F", "G");

	    Formula f7 = new Formula(6,currentRow, formula, i2 );
	    s.addCell(f7);
	}

	
	private String oldEmployee = "";
	private String oldEarning = "";
	private int    oldGainID   = 0;

	private int startGroup = 0;
	private int endGroup = 0;
	private int startGroupEarning = 0;
	private int endGroupEarning = 0;
	private int currentRow = 0;
	private WritableCellFormat cft;
	private WritableCellFormat i1;
	private WritableCellFormat i2;
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
	    WritableCellFormat cf = new WritableCellFormat(wf);
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

	  	Label l;
	  	
	    l = new Label(0,currentRow,Msg.translate(Env.getCtx(), "P_Gain_ID" ),cf);
	    s.addCell(l);

	    String sEarningList = "";
		for (int j = 0; j < EarningList.length; j++)
		{
			sEarningList = sEarningList + " - " + EarningList[j];
		}

	    l = new Label(1,currentRow, sEarningList ,cf);
	    s.addCell(l);
	    currentRow = currentRow + 1;


	    l = new Label(0,currentRow,Msg.translate(Env.getCtx(), "P_Period_ID" ),cf);
	    s.addCell(l);
	    l = new Label(1,currentRow, period1.getName() + " - " + period2.getName() ,cf);
	    s.addCell(l);
	    currentRow = currentRow + 3;

	    /* Creates Label and writes date to one cell of sheet*/
	    l = new Label(0,currentRow,Msg.translate(Env.getCtx(), "AD_Org_ID" ),cf);
	    s.addCell(l);
	    l = new Label(1,currentRow,Msg.translate(Env.getCtx(), "C_Activity_ID" ),cf);
	    s.addCell(l);
	    l = new Label(2,currentRow,Msg.translate(Env.getCtx(), "P_Employee_ID" ),cf);
	    s.addCell(l);
	    l = new Label(3,currentRow,Msg.translate(Env.getCtx(), "Name" ),cf);
	    s.addCell(l);
	    l = new Label(4,currentRow,Msg.translate(Env.getCtx(), "P_Period_ID" ),cf);
	    s.addCell(l);
//	    l = new Label(5,i,Msg.translate(Env.getCtx(), "P_Gain_ID" ),cf);
//	    s.addCell(l);
	    l = new Label(5,currentRow,Msg.translate(Env.getCtx(), "AmountCalc" ),cf);
	    s.addCell(l);
	    l = new Label(6,currentRow,Msg.translate(Env.getCtx(), "QuantityCalc" ),cf);
	    s.addCell(l);
	    l = new Label(7,currentRow,Msg.translate(Env.getCtx(), "P_Gain_ID" ),cf);
	    s.addCell(l);

    	s.setColumnView(0, 30);
    	s.setColumnView(1, 30);
    	s.setColumnView(2, 30);
    	s.setColumnView(3, 30);
    	s.setColumnView(4, 30);
    	s.setColumnView(5, 30);
    	s.setColumnView(6, 30);
    	s.setColumnView(7, 30);
    	s.setColumnView(8, 30);

	    WritableFont wf2 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
	  	    WritableCellFormat cf2 = new WritableCellFormat(wf2);
	  	    cf2.setWrap(true);

	  	    
	    String sqlEarningList = "'" + EarningList[0] + "'";
		for (int j = 1; j < EarningList.length; j++)
		{
			sqlEarningList = sqlEarningList + ", '" + EarningList[j] + "'";
		}

	  	
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
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();

			rowGroupEarning = new Integer[ EarningList.length ];
			
			while (rs.next())
			{
				currentRow = currentRow + 1;
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
//			    l = new Label(5,i,rs.getString(6),cf2);
//			    s.addCell(l);
			    Number nI1 = new Number(5,currentRow, rs.getFloat( 7) ,i1);
			    s.addCell(nI1);
			    nI1 = new Number(6,currentRow, rs.getFloat(8) ,i1);
			    s.addCell(nI1);			
				StringBuffer buf = new StringBuffer();
				buf.append( "F" + currentRow+1 + " - G" + currentRow+1 );

				l = new Label(7,currentRow,rs.getString(6),i1);
			    s.addCell(l);
			}

			currentRow = currentRow + 1;
	    	createTotalEarning( s, null );
	    	
//	    	currentRow = currentRow + 1;
	    	createTotalEmployee( s, null );
		    
	    	currentRow = currentRow + 1;

	    	createTotal( s );

			rs.close();
			pstmt.close();
			pstmt = null;

		}
		catch (Exception e)
		{
			System.err.println("ReportPaymentGainToExcel - " + e);
		}


		return currentRow;
	  }

		public static void main (String[] args)
		{
			org.compiere.Compiere.startupEnvironment(true);
			
			log.info("----------------------------------");
			new ReportPaymentGainToExcel();
			
			log.info("----------------------------------");
		}
}
