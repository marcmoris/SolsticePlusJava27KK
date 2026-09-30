/*
 * Created on 2005-09-05
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;

import org.compiere.process.SvrProcess;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.Env;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.sql.Timestamp;

import org.compiere.util.DB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import solstice.model.P_Period;
import solstice.model.P_T_Conciliation;
import solstice.model.P_BankAccount;

/**
 * @author alenav01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_ConciliationFile extends SvrProcess {

	private String _ClassErrorMessage ="Error P_ConciliationFile.";
	private Timestamp Date=null;
	private int InstanceID = 0;
	private int ClientID=0;
	private int OrgID=0;
	private int UserID=0;
	private String m_Format = "PDF";

	
	/**
	 * 
	 */
	public P_ConciliationFile() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		String paramName = "";
		InstanceID = getAD_PInstance_ID();
		
		//Read the parameters
		for(int i=0; i < para.length; i++){ 
			paramName = para[i].getParameterName();
			if( paramName.equals("Date")){
				this.Date = ((Timestamp)para[i].getParameter());
			}
            else if (paramName.equals("CrystalFormat"))
				m_Format = (String)para[i].getParameter();
		}
		
		if ( this.Date == null )
		{
			P_Period Period = P_Period.getOpenPeriod( Env.getCtx(), null );
			this.Date = Period.getPayDate();
		}
		
        ClientID = Env.getAD_Client_ID(getCtx());
        OrgID = Env.getAD_Org_ID(getCtx());
        UserID = Env.getAD_User_ID(getCtx());

		String SQLInsert = "INSERT INTO AD_PInstance_PARA (AD_PInstance_id, seqno, ParameterName, P_String, P_String_To, P_Number, P_Number_To, P_Date, P_Date_To, Info, Info_To, Ad_Client_id, Ad_Org_id, Created, createdby, Updated, UpdatedBy, Isactive) " +
        "Select " + InstanceID + ", " +
        "(Select isnull(Max(seqno) + 1,0) from AD_PInstance_Para where AD_PInstance_id = " + InstanceID + "), " +
        "'AD_PInstance_ID', null, null, " + InstanceID + ", null, null, null, null, null, " + ClientID + ", " + OrgID + ", GetDate(), " + UserID + ", GetDate(), " + UserID + ", 'Y'";

		PreparedStatement pstmp = null;
		
		try
		{
			pstmp = DB.prepareStatement(SQLInsert, null);
			pstmp.execute();
		}
		catch (Exception e)
		{
			//log.log(Level.SEVERE, "LauncherRemunerationState.Insert", e);
			System.err.println(_ClassErrorMessage + " [prepare] " + e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	protected String doIt() throws Exception {
//		P_InOutConciliationFile ConciliationFile=new P_InOutConciliationFile(getCtx(),InstanceID);
		StartProcess(this.Date);
		    
        int AD_Pinstance_ID = this.getAD_PInstance_ID();
        
        String sql = "SELECT WEBAPPURL "
        	+ " FROM P_SYSTEM_PARAMETERS ";
        PreparedStatement pstmt = DB.prepareStatement(sql, null);
        ResultSet rs = pstmt.executeQuery();
        String srvUrl = "";
        if(rs.next())
        	srvUrl = rs.getString(1);
        
        if(srvUrl.length() == 0)
        {
        	//TODO Error Msg avec Logger
        	System.out.println("Error With System Parameters");
        	return "";
        }
        int l_Role = Env.getAD_Role_ID(Env.getCtx());
        String repUrl = srvUrl+"Crystal/reports.jsp?AD_PInstance_ID=" + AD_Pinstance_ID + "&Format=" + m_Format + "&Ad_Role_ID=" + l_Role;
//        String repUrl = srvUrl+"Crystal/reports.jsp?AD_PInstance_ID=" + AD_Pinstance_ID;

        Env.startBrowser(repUrl);
        return "";
	}
	
	private StringBuffer _NewFile;
	private P_T_Conciliation _Conciliation;
	private int _InstanceID=0;

	
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
	
		P_BankAccount BankAccount = P_BankAccount.getByClientID(Env.getCtx(), this.getAD_Client_ID(), null); 

		
		String HeaderLine="00";  			//Start Position =1  
		String ChequeNumber;     			//Start Position =3
		BigDecimal NetPay;					//Start Position =9
		String Month;						//Start Position =19
		String Day;							//Start Position =21
		String Year;						//Start Position =23
//		String FixNumber="0023028"; 		//Start Position =25
		String FixNumber= BankAccount.getAccountNo().replace("-", ""); 		//Start Position =25
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
			_Conciliation=new P_T_Conciliation(getCtx(), -1, null);
			
			_Conciliation.setIsActive(rsConciliation.getBoolean("ISACTIVE"));
			_Conciliation.setAD_PInstance_ID( this.getAD_PInstance_ID() );
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
