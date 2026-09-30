/*
 * Created on 2005-09-05
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Timestamp;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.compiere.util.DB;
import solstice.model.P_T_Conciliation;
import java.util.Properties;




/**
 * @author alenav01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_InOutConciliationFile {

	  private String _ClassErrorMessage ="Error creating conciliation file.";
	  private StringBuffer _NewFile;
	  private P_T_Conciliation _Conciliation;
	  private Properties _ctx;
	  private int _InstanceID=0;
	
	/**
	 * 
	 */
	public P_InOutConciliationFile(Properties ictx,int iInstanceID) {
		super();
		_ctx=ictx;
		_InstanceID=iInstanceID;
		// TODO Auto-generated constructor stub
	}
	
	public void StartProcess(Timestamp iConciliationDate)
	{
		_NewFile=new StringBuffer();
		DeleteRecords();
		CreateFile(GetRecords(iConciliationDate));
		WriteToFile(_NewFile, iConciliationDate);
//		if (iConciliationDate==null)
		UpdateConciliationDate();
	}
		
	private void DeleteRecords()
	{
		String DeleteSQLString="DELETE FROM P_T_CONCILIATION WHERE CREATED<=GETDATE()-1";

		PreparedStatement pstmt;

		try
		{
			pstmt = DB.prepareStatement(DeleteSQLString, null);
			pstmt.execute();
		}
		catch (Exception e)
		{
			System.err.println(_ClassErrorMessage + " [DeleteRecords] " + e);
		}	
	}
	
	private void UpdateConciliationDate()
	{
		String UpdateSQLString="UPDATE P_PAYMENT SET RECONCILIATIONDATE=GETDATE() WHERE RECONCILIATIONDATE IS NULL " +
				          " AND TIMESHEETSTATUS IN ('E')" + // ,'T'
						  " AND PAYMENTTYPEDOC IN ('Regular','Advance', 'HolidayPayment', 'ExpenseAccount', 'Complementary')" +
						  " AND PAYMENTTYPE = 'C'";

		PreparedStatement pstmt;

		try
		{
			pstmt = DB.prepareStatement(UpdateSQLString, null);
			pstmt.execute();
		}
		catch (Exception e)
		{
			System.err.println(_ClassErrorMessage + " [UpdateConciliationDate] " + e);
		}	
	}
	
	private ResultSet GetRecords(Timestamp ConciliationDate)
	{
		String PaymentSQL="SELECT *,ISNULL(NETPAY,0) AS FILENETPAY,DAY(PAYDATE) AS DAY,MONTH(PAYDATE) AS MONTH,YEAR(PAYDATE) AS YEAR" +
						  " FROM P_PAYMENT" +
						  " WHERE TIMESHEETSTATUS IN ('E')" + // ,'T'
						  " AND PAYMENTTYPEDOC IN ('Regular','Advance', 'HolidayPayment', 'ExpenseAccount', 'Complementary')" +
						  " AND PAYMENTTYPE = 'C'";
		ResultSet rsPayment=null;
       	PreparedStatement pstmtPeriod = null;
       	
    	try
		{
    		if (ConciliationDate==null) 
    			PaymentSQL+= " AND RECONCILIATIONDATE IS NULL";
    		else
    			PaymentSQL+= " AND ( RECONCILIATIONDATE >=" + DB.TO_DATE(ConciliationDate) + " OR RECONCILIATIONDATE IS NULL ) ";
    		
    		pstmtPeriod= DB.prepareStatement(PaymentSQL, null);
			rsPayment= pstmtPeriod.executeQuery();			
		}
        catch (Exception e)
		{
        	System.err.println(_ClassErrorMessage + " [GetRecords] " + e);
		}
		return rsPayment;
	}
	
	private void CreateFile(ResultSet rsConciliation)
	{
		
		String HeaderLine="00";  			//Start Position =1  
		String ChequeNumber;     			//Start Position =3
		BigDecimal NetPay;					//Start Position =9
		String Month;						//Start Position =19
		String Day;							//Start Position =21
		String Year;						//Start Position =23
		String FixNumber="0023028"; 		//Start Position =25
		//Nine(9) blanksspaces				//Start Position =32
		String FixNumber2="593";			//Start Position =41
		
		String strNetPay;
		
				try
		{
			while(rsConciliation.next())
			{
				//To Create File
				ChequeNumber=rsConciliation.getString("PREPRINTEDNO");
				NetPay=rsConciliation.getBigDecimal("FILENETPAY").setScale(2, BigDecimal.ROUND_HALF_UP);
				Month=rsConciliation.getString("MONTH");
				Day=rsConciliation.getString("DAY");
				Year=rsConciliation.getString("YEAR");
				
				if (Month.length()<2) 
					Month="0" + Month;
				if (Day.length()<2) 
					Day="0" + Day;
				if (Year.length()>2) 
					Year=Year.substring(Year.length()-2);
								
				strNetPay=RemovePoint(NetPay.toString());
						
				_NewFile.append(HeaderLine);
				_NewFile.append(ChequeNumber.substring(1));
				_NewFile.append(InsertZero(10-strNetPay.length()) + strNetPay);
				_NewFile.append(Month);
				_NewFile.append(Day);
				_NewFile.append(Year);
				_NewFile.append(FixNumber);
				InsertBlank(_NewFile,9);
				_NewFile.append(FixNumber2);
				InsertBlank(_NewFile,1);
				InsertCarriageReturn(_NewFile);
				
				//To Insert Register in Temporal Table
				InsertToConciliationTemporalTable(rsConciliation);
			}
		}
		catch (Exception e)
		{
			System.err.println(_ClassErrorMessage + " [CreateFile] "  + e);
		}
	}
	
	private void InsertToConciliationTemporalTable(ResultSet rsConciliation)
	{
		try
		{
			_Conciliation=new P_T_Conciliation(_ctx, -1, null);
			
			_Conciliation.setIsActive(rsConciliation.getBoolean("ISACTIVE"));
			_Conciliation.setAD_PInstance_ID(_InstanceID);
			_Conciliation.setP_Payment_ID(rsConciliation.getInt("P_PAYMENT_ID"));
			_Conciliation.setP_Payment_Group_ID(rsConciliation.getInt("P_PAYMENT_GROUP_ID"));
			_Conciliation.setP_Employee_ID(rsConciliation.getInt("P_EMPLOYEE_ID"));
			_Conciliation.setP_Period_ID(rsConciliation.getInt("P_PERIOD_ID"));
			_Conciliation.setP_Frequency_ID(rsConciliation.getInt("P_FREQUENCY_ID"));
			_Conciliation.setPayDate(rsConciliation.getTimestamp("PAYDATE"));
			_Conciliation.setPrePrintedNo(rsConciliation.getString("PREPRINTEDNO"));
			_Conciliation.setNetPay(rsConciliation.getBigDecimal("NETPAY"));
			
			_Conciliation.save();	
		}
		catch (Exception e)
		{
			System.err.println(_ClassErrorMessage + " [InsertToConciliationTemporalTable] "  + e);
		}
		
	}
	
	private String RemovePoint(String strNetPay)
	{
		char[] ch=strNetPay.toCharArray();
		String strReturn="";
		for(int i = 0; i < ch.length ; i++)
		{
			if (ch[i]!='.')
				strReturn+=ch[i];
		}
    	return strReturn;
	}
	
	private String InsertZero(int Pos)
	{
		String Zeros="";
    	for (int i = 0; i < Pos; i++)
    	{
    		Zeros+="0";
    	}
    	return Zeros;
	}
	
    private void InsertBlank(StringBuffer Bl, int Pos)
    {
    	for (int i = 0; i < Pos; i++)
    	{
    		Bl.append(" ");
    	}
    }
    
    private void InsertCarriageReturn(StringBuffer Bl)
    {
    	Bl.append("\n");
    }
	
    public void WriteToFile(StringBuffer NewFile, Timestamp iConciliationDate)
    {
  	  String Path = "";
	  String FileName = "";
	  
	  String sql = "SELECT TRANSFERTPATH FROM P_SYSTEM_PARAMETERS";
      PreparedStatement psdoc = null;
      try
  	  { 
      	psdoc = DB.prepareStatement(sql, null);
        ResultSet rsdoc = psdoc.executeQuery();
        if (rsdoc.next())
        {        
        	Path = rsdoc.getString("TransfertPath");
        }
        rsdoc.close();
        psdoc.close();
      }
      catch (Exception e)
      {
      	System.out.println(_ClassErrorMessage + " [WriteToFile] " + e);
      }
        
      FileName = "Paie_Conciliation_" + iConciliationDate.toString().substring(0,10);
  	  File aFile = null;
  	  aFile = new File(Path + FileName + ".txt");
      FileOutputStream out; // declare a file output object
      PrintStream p; // declare a print stream object
      try
      {
      	out = new FileOutputStream(aFile);
        // Connect print stream to the output stream
        p = new PrintStream( out );
        p.println (NewFile.toString());
        p.close();
      }
      catch (Exception e)
      {
      	System.out.println ("Error writing to file :" + e.toString());
      }
   }

}
