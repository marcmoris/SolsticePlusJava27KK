/*
 * Created on 2005-09-15
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.P_Payment;
import solstice.model.P_Time_Sheet;
import solstice.model.P_BankAccountDoc;

/**
 * @author alenav01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_InOutChequesPrinting {
	
	
	private CLogger	log = CLogger.getCLogger (getClass());
	private String _ClassErrorMessage ="Error Printing Check.";
	private P_Payment _Payment;
	private Properties _ctx;
	private int _InstanceID=0;
	private int BankAccountDocID=0;
	  
	/**
	 * 
	 */
	public P_InOutChequesPrinting(Properties ictx,int iInstanceID) {
		super();
		_ctx=ictx;
		_InstanceID=iInstanceID;
		// TODO Auto-generated constructor stub
	}
	
	public void StartProcess(int iFrequencyID,int iPeriodID,int iPaymentGroupID,int iDistributionID,int iOccupationGroupID,int iEmployeeID, String iTypeDocument, int iToBeginAfterThisNumeration, boolean iReprint) throws Exception
	{
		if (iReprint)
			RePrintCheque(iFrequencyID,iPeriodID,iPaymentGroupID,iDistributionID,iOccupationGroupID,iEmployeeID,iTypeDocument,iToBeginAfterThisNumeration);
		else	
			PrintCheque(iFrequencyID,iPeriodID,iPaymentGroupID,iDistributionID,iOccupationGroupID,iEmployeeID,iTypeDocument,iToBeginAfterThisNumeration);
	}

	private void PrintCheque(int iFrequencyID,int iPeriodID,int iPaymentGroupID,int iDistributionID,int iOccupationGroupID,int iEmployeeID, String iTypeDocument, int iToBeginAfterThisNumeration)
	{
		int FirstChequeNumber=GetChequeNumber(iToBeginAfterThisNumeration);
		
		if (FirstChequeNumber!=0)
			ProcessCheque(ChequesToPrint(iFrequencyID,iPeriodID,iPaymentGroupID,iDistributionID,iOccupationGroupID,iEmployeeID, iTypeDocument),FirstChequeNumber);
		else
			log.log (Level.SEVERE, _ClassErrorMessage + " There are not cheque numeration. Please check table P_ACCOUNTBANKACCOUNT");
	}	
	
	private void RePrintCheque(int iFrequencyID,int iPeriodID,int iPaymentGroupID,int iDistributionID,int iOccupationGroupID,int iEmployeeID, String iTypeDocument, int iToBeginAfterThisNumeration)
	{
		int FirstChequeNumber=GetChequeNumber(iToBeginAfterThisNumeration);
		
		if (FirstChequeNumber!=0)
			RegenerateNumeration(ChequesToRePrint(iFrequencyID,iPeriodID,iPaymentGroupID,iDistributionID,iOccupationGroupID,iEmployeeID, iTypeDocument),FirstChequeNumber);
		else
			log.log (Level.SEVERE, _ClassErrorMessage + " There are not cheque numeration. Please check table P_ACCOUNTBANKACCOUNT");

	}
	
	private ResultSet ChequesToRePrint(int iFrequencyID,int iPeriodID,int iPaymentGroupID,int iDistributionID,int iOccupationGroupID,int iEmployeeID, String iTypeDocument)
	{
		String SelectSQL="SELECT P.P_PAYMENT_ID, SE.* " +
						 " FROM P_STATEMENT_EARNING SE " +
		  				 " INNER JOIN P_PAYMENT P ON P.P_EMPLOYEE_ID = SE.P_EMPLOYEE_ID AND P.P_PERIOD_ID = SE.P_PERIOD_ID" +
						 " WHERE" +
						 " P.PAYMENTTYPE = 'C'" +
						 " AND P.P_FREQUENCY_ID=" + iFrequencyID +
						 " AND P.P_PERIOD_ID="+ iPeriodID +
						 " AND P.PAYMENTTYPEDOC='" + iTypeDocument + "'";
		
		if (iPaymentGroupID!=0)
			SelectSQL+=" AND P.P_PAYMENT_GROUP_ID="+iPaymentGroupID;
		if (iDistributionID!=0)
			SelectSQL+=" AND P.P_DISTRIBUTION_BOOKLET_ID="+iDistributionID;
		if (iOccupationGroupID!=0)
			SelectSQL+=" AND P.P_OCCUPATION_GROUP_ID="+iOccupationGroupID;
		if (iEmployeeID!=0)
			SelectSQL+=" AND P.P_EMPLOYEE_ID="+iEmployeeID;
		
		ResultSet rs=null;
		PreparedStatement pstmt = null;
		
		try
		{
			pstmt= DB.prepareStatement(SelectSQL, null);
			rs= pstmt.executeQuery();		
			
			
		}
		catch (Exception e)
		{
			System.err.println(_ClassErrorMessage + " [ChequesToRePrint] " + e);
		}
		return rs;
	}
	
	private ResultSet ChequesToPrint(int iFrequencyID,int iPeriodID,int iPaymentGroupID,int iDistributionID,int iOccupationGroupID,int iEmployeeID, String iTypeDocument)
	{
		String SelectSQL="SELECT TS.P_TIME_SHEET_ID,P.*" +
						  " FROM P_PAYMENT P,P_TIME_SHEET TS" +
						  " WHERE P.P_PAYMENT_ID=TS.P_PAYMENT_ID" +
						  " AND P.PAYMENTTYPE = 'C'" +
						  " AND P.TIMESHEETSTATUS='C'" +
						  " AND P.PAYMENTTYPEDOC ='" + iTypeDocument + "'" +
						  " AND P.PREPRINTEDNO IS NULL" +
						  " AND P.P_FREQUENCY_ID="+ iFrequencyID +
						  " AND P.P_PERIOD_ID="+ iPeriodID;

		ResultSet rs=null;
		PreparedStatement pstmt = null;
			
		try
		{
			if (iPaymentGroupID!=0)
				SelectSQL+=" AND P.P_PAYMENT_GROUP_ID="+iPaymentGroupID;
			if (iDistributionID!=0)
				SelectSQL+=" AND P.P_DISTRIBUTION_BOOKLET_ID="+iDistributionID;
			if (iOccupationGroupID!=0)
				SelectSQL+=" AND P.P_OCCUPATION_GROUP_ID="+iOccupationGroupID;
			if (iEmployeeID!=0)
				SelectSQL+=" AND P.P_EMPLOYEE_ID="+iEmployeeID;
			
			pstmt= DB.prepareStatement(SelectSQL, null);
			rs= pstmt.executeQuery();			
		}
		catch (Exception e)
		{
			System.err.println(_ClassErrorMessage + " [ChequesToPrint] " + e);
		}
		return rs;
	}
	
	private int GetChequeNumber(int iToBeginAfterThisNumeration)
	{
		int CheckNumber=GetCurrentBankChequeNumber();
		if (CheckNumber!=0)
		{
			if (iToBeginAfterThisNumeration==0)
				CheckNumber++;
			else
				CheckNumber=iToBeginAfterThisNumeration;
		}
		return CheckNumber;
	}
	
	private void ProcessCheque(ResultSet rsPayment,int iFirstChequeNumber)
	{
		int CurrentNumeration=iFirstChequeNumber;
		try
		{
			if (rsPayment.isBeforeFirst())
			{
				while (rsPayment.next())
				{
					UpdatePayment(rsPayment.getInt("P_PAYMENT_ID"),rsPayment.getInt("P_TIME_SHEET_ID"),MakeNumeration(CurrentNumeration));
					CurrentNumeration++;
				}
				SetLastBankAccountDoc(CurrentNumeration-1);
				AddParameters(iFirstChequeNumber,CurrentNumeration-1);
			}
			else
			{
				AddParameters(0,0);
			}
			
		}
		catch (Exception e)
		{
			System.err.println(_ClassErrorMessage + " [ProcessCheque] " + e);
		}		
	}
	
	private void RegenerateNumeration(ResultSet rs,int iFirstChequeNumber)
	{
		int CurrentNumeration=iFirstChequeNumber;
		try
		{
			if (rs.isBeforeFirst())
			{
				while (rs.next())
				{
					P_Payment Payment = new P_Payment(Env.getCtx(),rs.getInt("P_PAYMENT_ID"), null);
					Payment.setPrePrintedNo(MakeNumeration(CurrentNumeration));
					Payment.save();
					
					CurrentNumeration++;
				}
				SetLastBankAccountDoc(CurrentNumeration-1);
				AddParameters(iFirstChequeNumber,CurrentNumeration-1);
			}
			else
			{
				AddParameters(0,0);
			}
			
		}
		catch (Exception e)
		{
			System.err.println(_ClassErrorMessage + " [ProcessCheque] " + e);
		}		
	}
	
	private String MakeNumeration(int CurrentNumeration)
	{
		String  PrePrintedNo=String.valueOf(CurrentNumeration);
		int NumberOfChar=PrePrintedNo.length();
		PrePrintedNo="C" + InsertZero(6-NumberOfChar) + PrePrintedNo;
		return PrePrintedNo;
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
		
	private void UpdatePayment(int iPaymentID,int iTimeSheetID,String iPrePrintedNo) 
	{
		P_Time_Sheet TimeSheet=new P_Time_Sheet(Env.getCtx(),iTimeSheetID, null);
		TimeSheet.setTimeSheetStatus("E");
		TimeSheet.save();
		
		P_Payment Payment=new P_Payment(Env.getCtx(),iPaymentID, null);
		Payment.setPrePrintedNo(iPrePrintedNo);
		Payment.save();
	}
	
	private int GetCurrentBankChequeNumber()
	{
		int ChequeNumber=0;
		String SqlBankAccountDoc="SELECT P_BANKACCOUNTDOC_ID,CURRENTNEXT FROM P_BANKACCOUNTDOC WHERE PAYMENTRULE='S'"; 
		
		ResultSet rsBankAccountDoc=null;
		PreparedStatement pstmt = null;
		
		try
		{
			pstmt= DB.prepareStatement(SqlBankAccountDoc, null);
			rsBankAccountDoc= pstmt.executeQuery();
			
			if (rsBankAccountDoc.isBeforeFirst())
			{
				rsBankAccountDoc.next();
				BankAccountDocID=rsBankAccountDoc.getInt("P_BANKACCOUNTDOC_ID");
				ChequeNumber=rsBankAccountDoc.getInt("CURRENTNEXT");
			}
			
		}
		catch (Exception e)
		{
			System.err.println(_ClassErrorMessage + " [GetNextChequeNumber] " + e);
		}
		
		return ChequeNumber;
	}
	
	private void SetLastBankAccountDoc(int iLastChequeNumber)
	{
		P_BankAccountDoc BankAccountDoc=new P_BankAccountDoc(Env.getCtx(), BankAccountDocID, null);
		BankAccountDoc.setCurrentNext(iLastChequeNumber);
		BankAccountDoc.save();
	}
	
	private void AddParameters(int iFirstChequeNumber,int iLastChequeNumber)
	{
		int ClientID = Env.getAD_Client_ID(_ctx);
        int OrgID = Env.getAD_Org_ID(_ctx);
        int UserID = Env.getAD_User_ID(_ctx);
		
		String SQLInsert = "INSERT INTO AD_PINSTANCE_PARA (AD_PINSTANCE_ID, SEQNO, PARAMETERNAME, P_STRING, P_STRING_TO, P_NUMBER, P_NUMBER_TO, P_DATE, P_DATE_TO, INFO, INFO_TO, AD_CLIENT_ID, AD_ORG_ID, CREATED, CREATEDBY, UPDATED, UPDATEDBY, ISACTIVE) " +
	        "SELECT " + _InstanceID + ", " +
	        "(SELECT ISNULL(MAX(SEQNO) + 1,0) FROM AD_PINSTANCE_PARA WHERE AD_PINSTANCE_ID = " + _InstanceID + "), " +
	        "'FirstCheque','" + MakeNumeration(iFirstChequeNumber) +"', NULL, NULL, NULL, NULL, NULL, NULL, NULL, " + ClientID + ", " + OrgID + ", GETDATE(), " + UserID + ", GETDATE(), " + UserID + ", 'Y'";

		String SQLInsert2 = "INSERT INTO AD_PINSTANCE_PARA (AD_PINSTANCE_ID, SEQNO, PARAMETERNAME, P_STRING, P_STRING_TO, P_NUMBER, P_NUMBER_TO, P_DATE, P_DATE_TO, INFO, INFO_TO, AD_CLIENT_ID, AD_ORG_ID, CREATED, CREATEDBY, UPDATED, UPDATEDBY, ISACTIVE) " +
        "SELECT " + _InstanceID + ", " +
        "(SELECT ISNULL(MAX(SEQNO) + 1,0) FROM AD_PINSTANCE_PARA WHERE AD_PINSTANCE_ID = " + _InstanceID + "), " +
        "'LastCheque','" + MakeNumeration(iLastChequeNumber) +"', NULL, NULL, NULL, NULL, NULL, NULL, NULL, " + ClientID + ", " + OrgID + ", GETDATE(), " + UserID + ", GETDATE(), " + UserID + ", 'Y'";

		
		PreparedStatement pstmp = null;
		
		try
		{
			pstmp = DB.prepareStatement(SQLInsert, null);
			pstmp.execute();
			
			pstmp = DB.prepareStatement(SQLInsert2, null);
			pstmp.execute();
		}
		catch (Exception e)
		{
			System.err.println(_ClassErrorMessage + " [AddParameters] " + e);
		}
	}
	
	public boolean Validate(int iFrequencyID,int iPeriodID,int iPaymentGroupID,int iDistributionID,int iOccupationGroupID,int iEmployeeID, String ITypeDocument, int iToBeginAfterThisNumeration, boolean iReprint) throws Exception
	{
		int CountSelection=0;
		String CountSQL="SELECT COUNT(*) COUNT ";
	
		if (iReprint)
		{
			CountSQL += " FROM P_STATEMENT_EARNING SE" +
			  			" INNER JOIN P_PAYMENT P ON P.P_EMPLOYEE_ID = SE.P_EMPLOYEE_ID AND P.P_PERIOD_ID = SE.P_PERIOD_ID" +
						" WHERE" +
						" P.P_FREQUENCY_ID=" + iFrequencyID +
						" AND P.P_PERIOD_ID="+ iPeriodID;
		}
		else
		{
			CountSQL += " FROM P_PAYMENT P,P_TIME_SHEET TS" +
			  			" WHERE P.P_PAYMENT_ID=TS.P_PAYMENT_ID" +
						" AND P.PAYMENTTYPE = 'C'" +
						" AND P.TIMESHEETSTATUS='C'" +
						" AND P.PAYMENTTYPEDOC = '" + ITypeDocument + "'" + //IN ('Regular','Advance')" +
						" AND P.PREPRINTEDNO IS NULL" +
						" AND P.P_FREQUENCY_ID="+ iFrequencyID +
						" AND P.P_PERIOD_ID="+ iPeriodID;
		}
			
		if (iPaymentGroupID!=0)
			CountSQL+=" AND P.P_PAYMENT_GROUP_ID="+iPaymentGroupID;
		if (iDistributionID!=0)
			CountSQL+=" AND P.P_DISTRIBUTION_BOOKLET_ID="+iDistributionID;
		if (iOccupationGroupID!=0)
			CountSQL+=" AND P.P_OCCUPATION_GROUP_ID="+iOccupationGroupID;
		if (iEmployeeID!=0)
			CountSQL+=" AND P.P_EMPLOYEE_ID="+iEmployeeID;

		PreparedStatement pstmp = null;
		
		try
		{
			pstmp = DB.prepareStatement(CountSQL, null);
			ResultSet rsCount = pstmp.executeQuery(); 
			
			while ( rsCount.next() )
			{
				CountSelection = rsCount.getInt("COUNT");
			}
		}
		catch (Exception e)
		{
			System.err.println(_ClassErrorMessage + " [AddParameters] " + e);
		}
		return (CountSelection>0);
	}
	
}
