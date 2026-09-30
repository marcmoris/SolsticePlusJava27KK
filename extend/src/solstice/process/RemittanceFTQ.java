package solstice.process;

import java.io.*;
import java.math.BigDecimal;

import jxl.*;
import java.util.*;
import jxl.Workbook;
import jxl.write.Number;

import jxl.write.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.util.DB;
import org.compiere.util.Env;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import solstice.model.P_Period;
import solstice.utils.PgiUtil;

public class RemittanceFTQ extends SvrProcess
{
	int P_Period_Start_ID;
	int P_Period_End_ID ;
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
		}
	}
	
	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	protected String doIt() throws Exception 
	{
		System.out.println("RemittanceFTQ   $Revision: 1.0 $");
		System.out.println("----------------------------------");

		String Path = PgiUtil.getSolsticeParameter(getCtx(), "TransfertPath");
		String filename =  Path + "\\RemiseFTQ.xls";
		//
		int exported = 0;
	    try
	    {
	      WorkbookSettings ws = new WorkbookSettings();
	      ws.setLocale(new Locale("fr", "FR"));
	      WritableWorkbook workbook = 
	        Workbook.createWorkbook(new File(filename), ws);
	      WritableSheet s = workbook.createSheet("Remise", 0);
//	      WritableSheet s1 = workbook.createSheet("Sheet1", 0);
	      exported = writeDataSheet(s);
//	      writeImageSheet(s1);
	      workbook.write();
	      workbook.close();      
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

        PgiUtil.promptOpenExportedFile( filename );
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
	    cf.setWrap(false);

	    // NAS
	    s.setColumnView(7, 15);
	    s.setColumnView(13, 30);
	    
	    /* Creates Label and writes date to one cell of sheet*/
	    Label l = new Label(0,0,"A1",cf);
	    s.addCell(l);
	    l = new Label(1,0,"B1",cf);
	    s.addCell(l);
	    l = new Label(2,0,"C1",cf);
	    s.addCell(l);
	    l = new Label(3,0,"D1",cf);
	    s.addCell(l);
	    l = new Label(4,0,"E1",cf);
	    s.addCell(l);
	    l = new Label(5,0,"F1",cf);
	    s.addCell(l);
	    l = new Label(6,0,"G1",cf);
	    s.addCell(l);
	    l = new Label(7,0,"H1",cf);
	    s.addCell(l);
	    l = new Label(8,0,"I1",cf);
	    s.addCell(l);
	    l = new Label(9,0,"J1",cf);
	    s.addCell(l);
	    l = new Label(10,0,"K1",cf);
	    s.addCell(l);
	    l = new Label(11,0,"L1",cf);
	    s.addCell(l);
	    l = new Label(12,0,"M1",cf);
	    s.addCell(l);
	    l = new Label(13,0,"N1",cf);
	    s.addCell(l);

	    WritableFont wf2 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
	  	    WritableCellFormat cf2 = new WritableCellFormat(wf2);
	  	    cf2.setWrap(true);

	  	P_Period period1 = P_Period.get( getCtx(), P_Period_Start_ID, null );
	  	P_Period period2 = P_Period.get( getCtx(), P_Period_End_ID, null );
	  	
		String sql = "solstice.dbo.P_Remise_FTQ '" + period1.getName() + "' , '" + period2.getName() + "'";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
			    l = new Label(0,i,rs.getString("A1"),cf2);
			    s.addCell(l);
			    l = new Label(1,i,rs.getString("B1"),cf2);
			    s.addCell(l);
			    l = new Label(2,i,rs.getString("C1"),cf2);
			    s.addCell(l);
			    l = new Label(3,i,rs.getString("D1"),cf2);
			    s.addCell(l);
			    l = new Label(4,i,rs.getString("E1"),cf2);
			    s.addCell(l);
			    l = new Label(5,i,rs.getString("F1"),cf2);
			    s.addCell(l);
			    l = new Label(6,i,rs.getString("G1"),cf2);
			    s.addCell(l);
			    l = new Label(7,i,rs.getString("H1"),cf2);
			    s.addCell(l);
			    WritableCellFormat i1 = new WritableCellFormat(new NumberFormat("#0.00"));
			    
//			    Float tmp = rs.getFloat("I1");
			    Double tmp2 = rs.getDouble("I1");
			    Number nI1 = new Number(8,i, tmp2 ,i1);
			    s.addCell(nI1);
			    l = new Label(9,i,rs.getString("J1"),cf2);
			    s.addCell(l);
			    l = new Label(10,i,rs.getString("K1"),cf2);
			    s.addCell(l);
			    l = new Label(11,i,rs.getString("L1"),cf2);
			    s.addCell(l);
			    l = new Label(12,i,rs.getString("M1"),cf2);
			    s.addCell(l);
			    l = new Label(13,i,rs.getString("N1"),cf2);
			    s.addCell(l);

			    i = i + 1;


			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("RemittanceFTQ - " + e);
		}
/*	    
	    WritableCellFormat cf1 = 
	      new WritableCellFormat(DateFormats.FORMAT9);

	    DateTime dt = 
	      new DateTime(0,1,new Date(), cf1, DateTime.GMT);

	    s.addCell(dt);
	    
	    /* Creates Label and writes float number to one cell of sheet*/
/*
		WritableCellFormat cf2 = new WritableCellFormat(NumberFormats.FLOAT);
	    Number n = new Number(2,1,3.1415926535,cf2);
	    s.addCell(n);

	    n = new Number(2,2,-3.1415926535, cf2);
	    s.addCell(n);
*/
	    /* Creates Label and writes float number upto 3 
	       decimal to one cell of sheet */
/*	    
		NumberFormat dp3 = new NumberFormat("#.###");
	    WritableCellFormat dp3cell = new WritableCellFormat(dp3);
	    n = new Number(3,1,3.1415926535,dp3cell);
	    s.addCell(n);
*/
	    /* Creates Label and adds 2 cells of sheet*/
/*		
	    n = new Number(4,1,10);
	    s.addCell(n);
	    n = new Number(4,2,16);
	    s.addCell(n);
	    Formula f = new Formula(4,3, "E2+E3");
	    s.addCell(f);
*/
	    /* Creates Label and multipies value of one cell of sheet by 2*/
/*		
	    n = new Number(5,1,10);
	    s.addCell(n);
	    f = new Formula(5,2, "F2 * 3");
	    s.addCell(f);
*/
	    /* Creates Label and divide value of one cell of sheet by 2.5 */
/*		
	    n = new Number(6,1, 12);
	    s.addCell(n);
	    f = new Formula(6,2, "F2/4");
	    s.addCell(f);
*/	    
		return i;
	  }

	  private static void writeImageSheet(WritableSheet s) 
	    throws WriteException
	  {
	    /* Creates Label and writes image to one cell of sheet*/    
	    Label l = new Label(0, 0, "Image");
	    s.addCell(l);
	    WritableImage wi = new WritableImage(0, 3, 5, 7, new File("image.png"));
	    s.addImage(wi);

	    /* Creates Label and writes hyperlink to one cell of sheet*/
	    l = new Label(0,15, "HYPERLINK");
	    s.addCell(l);
	    Formula f = new Formula(1, 15, 
	      "HYPERLINK(\"http://www.andykhan.com/jexcelapi\", "+
	      "\"JExcelApi Home Page\")");
	    s.addCell(f);
	    
	    }

/*
		public static void main(String[] args) 
		  {
			System.out.println("RemittanceFTQ   $Revision: 1.0 $");
			System.out.println("----------------------------------");
			//
			Compiere.startup(true);

		    try
		    {
		      String filename = "D:\\PGI\\RemiseFTQ.xls";
		      WorkbookSettings ws = new WorkbookSettings();
		      ws.setLocale(new Locale("fr", "FR"));
		      WritableWorkbook workbook = 
		        Workbook.createWorkbook(new File(filename), ws);
		      WritableSheet s = workbook.createSheet("Remise", 0);
//		      WritableSheet s1 = workbook.createSheet("Sheet1", 0);
		      int nbr = writeDataSheet(s);
//		      writeImageSheet(s1);
		      workbook.write();
		      workbook.close();      
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
		  }
*/

}


