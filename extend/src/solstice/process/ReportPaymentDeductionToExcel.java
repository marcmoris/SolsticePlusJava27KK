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

import org.compiere.model.MProcess;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

import solstice.model.P_Deduction;
import solstice.model.P_Period;
import solstice.utils.PgiUtil;

public class ReportPaymentDeductionToExcel extends SvrProcess
{
	/** Logger */
	private static CLogger log = CLogger.getCLogger(ReportPaymentDeductionToExcel.class);
	private int P_Period_Start_ID = 1002103;
	private int P_Period_End_ID   = 1002104;
	private int P_Deduction_ID = 1002451;
	

	public ReportPaymentDeductionToExcel( )
	{
		
	}

	public ReportPaymentDeductionToExcel( boolean standalone)
	{
		try
		{ 
			doIt();
        }
        catch (Exception e)
        {
            log.log(Level.WARNING, "ReportPaymentDeductionToExcel", e);
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
			else if ( paramName.equals("P_Deduction_ID"))
				this.P_Deduction_ID = para[i].getParameterAsInt();
		}
	}

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	protected String doIt() throws Exception 
	{
		System.out.println("ReportPaymentDeductionToExcel   $Revision: 1.0 $");
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
	




	  private int writeDataSheet(WritableSheet s) 
	    throws WriteException
	  {
		  int i = 0;
	    /* Format the Font */
	    WritableFont wf = new WritableFont(WritableFont.ARIAL, 
	      10, WritableFont.BOLD);
	    WritableCellFormat cf = new WritableCellFormat(wf);
	    cf.setWrap(true);
	    Colour c = Colour.GREY_25_PERCENT;
	    cf.setBackground(c);
	    cf.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);

	    WritableCellFormat i1 = new WritableCellFormat(NumberFormats.FLOAT);
	    WritableCellFormat i2 = new WritableCellFormat(NumberFormats.FLOAT);
//	    i2.setBackground(c);
	    i2.setBorder(jxl.format.Border.TOP, jxl.format.BorderLineStyle.THIN);

	    WritableCellFormat cft = new WritableCellFormat(wf);
	    cft.setBorder(jxl.format.Border.TOP, jxl.format.BorderLineStyle.THIN);

	  	P_Deduction Deduction = P_Deduction.get( Env.getCtx(), P_Deduction_ID, null);
	  	P_Period period1 = P_Period.get( Env.getCtx(), P_Period_Start_ID, null );
	  	P_Period period2 = P_Period.get( Env.getCtx(), P_Period_End_ID, null );

	  	Label l;
	  	
	    l = new Label(0,i,Msg.translate(Env.getCtx(), "P_Deduction_ID" ),cf);
	    s.addCell(l);
	    l = new Label(1,i, Deduction.getValue() + " " + Deduction.getName() ,cf);
	    s.addCell(l);
	    i = i + 1;


	    l = new Label(0,i,Msg.translate(Env.getCtx(), "P_Period_ID" ),cf);
	    s.addCell(l);
	    l = new Label(1,i, period1.getName() + " - " + period2.getName() ,cf);
	    s.addCell(l);
	    i = i + 3;

	    /* Creates Label and writes date to one cell of sheet*/
	    l = new Label(0,i,Msg.translate(Env.getCtx(), "AD_Org_ID" ),cf);
	    s.addCell(l);
	    l = new Label(1,i,Msg.translate(Env.getCtx(), "C_Activity_ID" ),cf);
	    s.addCell(l);
	    l = new Label(2,i,Msg.translate(Env.getCtx(), "P_Employee_ID" ),cf);
	    s.addCell(l);
	    l = new Label(3,i,Msg.translate(Env.getCtx(), "Name" ),cf);
	    s.addCell(l);
	    l = new Label(4,i,Msg.translate(Env.getCtx(), "P_Period_ID" ),cf);
	    s.addCell(l);
//	    l = new Label(5,i,Msg.translate(Env.getCtx(), "P_Deduction_ID" ),cf);
//	    s.addCell(l);
	    l = new Label(5,i,Msg.translate(Env.getCtx(), "Employee_Part" ),cf);
	    s.addCell(l);
	    l = new Label(6,i,Msg.translate(Env.getCtx(), "Employer_Part" ),cf);
	    s.addCell(l);
	    l = new Label(7,i,Msg.translate(Env.getCtx(), "Difference" ),cf);
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

	  	    
	  	
		String sql = "SELECT AD_ORG.NAME "
				   + ", C_Activity.NAME "
				   + ", P_Employee.VALUE "
				   + ", P_Employee.NAME "
				   + ", P_Period.NAME "
				   + ", P_Deduction.NAME "
				   + ", SUM( EMPLOYEE_PART ) "
				   + ", SUM( EMPLOYER_PART ) "
				   + " FROM dbo.P_PAYMENT "
				   + "INNER JOIN dbo.P_PAYMENT_DEDUCTION ON P_PAYMENT_DEDUCTION.P_PAYMENT_ID = dbo.P_PAYMENT.P_PAYMENT_ID "
				   + "INNER JOIN dbo.AD_ORG ON AD_ORG.AD_ORG_ID = P_PAYMENT.AD_ORG_ID "
				   + "INNER JOIN dbo.P_EMPLOYEE ON P_EMPLOYEE.P_EMPLOYEE_ID = P_PAYMENT.P_EMPLOYEE_ID "
				   + "INNER JOIN dbo.P_DEDUCTION ON dbo.P_DEDUCTION.P_DEDUCTION_ID = P_PAYMENT_DEDUCTION.P_DEDUCTION_ID "
				   + "INNER JOIN dbo.C_ACTIVITY ON dbo.C_ACTIVITY.C_ACTIVITY_ID = P_PAYMENT.C_Activity_ID "
				   + "INNER JOIN dbo.AD_ORG empOrg ON EmpOrg.AD_ORG_ID = P_Employee.AD_ORG_ID "
				   + "INNER JOIN dbo.C_ACTIVITY empAct ON empAct.C_ACTIVITY_ID = P_Employee.C_Activity_ID "
				   + "INNER JOIN dbo.P_PERIOD ON P_PERIOD.P_PERIOD_ID = P_Payment.P_PERIOD_ID "
				   + " WHERE P_DEDUCTION.P_DEDUCTION_ID = " + P_Deduction_ID
				   + "   AND P_Payment.P_PERIOD_ID BETWEEN " + P_Period_Start_ID + " AND " + P_Period_End_ID
				   + "GROUP BY "
				   + "AD_ORG.NAME "
				   + ", C_Activity.NAME "
				   + ", P_Employee.VALUE "
				   + ", P_Employee.NAME "
				   + ", P_Period.NAME "
				   + ", P_Deduction.NAME "
				   + ",  empOrg.NAME "
				   + ", empAct.NAME "
				   + " ORDER BY "
				   + "  empOrg.NAME "
				   + ", empAct.NAME "
				   + ", P_Employee.VALUE "
				   + ", P_Employee.NAME "
				   + ", P_Period.NAME "
				   + ", P_Deduction.NAME "
				   ;
		PreparedStatement pstmt = null;
		try
		{
			String oldEmployee = "";
			int startGroup = 0;
			int endGroup = 0;
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();


			while (rs.next())
			{
			    i = i + 1;
			    if ( oldEmployee.length() == 0)
			    {
			    	oldEmployee = rs.getString(3);
			    	startGroup = i  ;
			    }
			    if ( oldEmployee.equals( rs.getString(3) ) == false  )
			    {
			    	oldEmployee  = rs.getString(3);
			    	endGroup = i;
		    		s.setRowGroup(startGroup, endGroup -1 , true);

				    Formula f0 = new Formula(0,i, "A" + endGroup , cft );
				    s.addCell(f0);
				    Formula f1 = new Formula(1,i, "B" + endGroup , cft );
				    s.addCell(f1);

				    Formula f2 = new Formula(2,i, "C" + endGroup , cft );
				    s.addCell(f2);

				    Formula f3 = new Formula(3,i, "D" + endGroup , cft );
				    s.addCell(f3);

				    l = new Label(4,i,"Total",cft);
				    s.addCell(l);
				    Formula f6 = new Formula(5,i, "SUM(F" + String.valueOf(startGroup + 1) + ":F" + endGroup + ")", i2 );
				    s.addCell(f6);

				    Formula f7 = new Formula(6,i, "SUM(G" + String.valueOf(startGroup + 1) + ":G" + endGroup + ")", i2 );
				    s.addCell(f7);

				    
				    Formula f8 = new Formula(7,i, "SUM(H" + String.valueOf(startGroup + 1) + ":H" + endGroup + ")", i2 );
//				    Formula f8 = new Formula(7,i, "IF(F" + String.valueOf(i + 1) + ">250,250-ROUND(G" + String.valueOf(i + 1) + ",2),ROUND(F" + String.valueOf(i + 1) + "-G" + String.valueOf(i + 1) + ",2))", i2 );
				    s.addCell(f8);

				    Formula f9 = new Formula(8,i, "IF(F" + String.valueOf(i + 1) + ">250,250-ROUND(G" + String.valueOf(i + 1) + ",2),ROUND(F" + String.valueOf(i + 1) + "-G" + String.valueOf(i + 1) + ",2))", i2 );
				    s.addCell(f9);

				    i = i + 1;
				    startGroup = i ;
			    	
			    }
			    
			    l = new Label(0,i,rs.getString(1),cf2);
			    s.addCell(l);
			    l = new Label(1,i,rs.getString(2),cf2);
			    s.addCell(l);
			    l = new Label(2,i,rs.getString(3),cf2);
			    s.addCell(l);
			    l = new Label(3,i,rs.getString(4),cf2);
			    s.addCell(l);
			    l = new Label(4,i,rs.getString(5),cf2);
			    s.addCell(l);
//			    l = new Label(5,i,rs.getString(6),cf2);
//			    s.addCell(l);
			    Number nI1 = new Number(5,i, rs.getFloat( 7) ,i1);
			    s.addCell(nI1);
			    nI1 = new Number(6,i, rs.getFloat(8) ,i1);
			    s.addCell(nI1);			
				StringBuffer buf = new StringBuffer();
				buf.append( "F" + String.valueOf( i+1 ) + " - G" + String.valueOf( i+1 ) );
				// Si le excel doit être en Francais il faut utilisé le formule SOMME		
			    Formula f = new Formula(7,i, buf.toString(), i1 );
			    s.addCell(f);
			}
			rs.close();
			pstmt.close();
			pstmt = null;

		    i = i + 1;

		    endGroup = i;
    		s.setRowGroup(startGroup, endGroup -1 , true);

		    Formula f0 = new Formula(0,i, "A" + endGroup , cft );
		    s.addCell(f0);
		    Formula f1 = new Formula(1,i, "B" + endGroup , cft );
		    s.addCell(f1);

		    Formula f2 = new Formula(2,i, "C" + endGroup , cft );
		    s.addCell(f2);

		    Formula f3 = new Formula(3,i, "D" + endGroup , cft );
		    s.addCell(f3);

		    l = new Label(4,i,"Total",cft);
		    s.addCell(l);
			// Si le excel doit être en Francais il faut utilisé le formule SOMME		
		    Formula f6 = new Formula(5,i, "SUM(F" + String.valueOf(startGroup + 1) + ":F" + endGroup + ")", i2 );
		    s.addCell(f6);

			// Si le excel doit être en Francais il faut utilisé le formule SOMME		
		    Formula f7 = new Formula(6,i, "SUM(G" + String.valueOf(startGroup + 1) + ":G" + endGroup + ")", i2 );
		    s.addCell(f7);

			// Si le excel doit être en Francais il faut utilisé le formule SOMME		
		    Formula f8 = new Formula(7,i, "SUM(H" + String.valueOf(startGroup + 1) + ":H" + endGroup + ")", i2 );
		    s.addCell(f8);
		    Formula f9 = new Formula(8,i, "IF(F" + String.valueOf(i + 1) + ">250,250-ROUND(G" + String.valueOf(i + 1) + ",2),ROUND(F" + String.valueOf(i + 1) + "-G" + String.valueOf(i + 1) + ",2))", i2 );
		    s.addCell(f9);

		    i = i + 1;

		    l = new Label(0,i,"Grand Total",cft);
		    s.addCell(l);
		    f6 = new Formula(5,i, "SUM(F3"  + ":F" + endGroup + ")", i2 );
		    s.addCell(f6);

		    f7 = new Formula(6,i, "SUM(G3" + ":G" + endGroup + ")", i2 );
		    s.addCell(f7);

		    f8 = new Formula(7,i, "SUM(H3" + ":H" + endGroup + ")", i2 );
		    s.addCell(f8);

		}
		catch (Exception e)
		{
			System.err.println("ReportPaymentDeductionToExcel - " + e);
		}


		return i;
	  }

		public static void main (String[] args)
		{
			org.compiere.Compiere.startupEnvironment(true);
			
			log.info("----------------------------------");
			new ReportPaymentDeductionToExcel( true );
			
			log.info("----------------------------------");
		}
}
