package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

public class P_Notice_Bank_Deposit extends X_P_Notice_Bank_Deposit
{
	
	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Notice_Bank_Deposit.class);


	public static P_Notice_Bank_Deposit get (Properties ctx, int P_Period_ID, int AD_Org_ID,  int P_Department_ID, int C_Activity_ID, int P_Employee_ID, String trxName)
	{
		int P_Notice_Bank_Deposit_ID = 0;
		P_Notice_Bank_Deposit Notice_Bank_Deposit;
		
		String sql = "Select * From P_Notice_Bank_Deposit where P_Period_ID = " + P_Period_ID;
		
		sql = sql + " AND ( P_Notice_Bank_Deposit.Employee_AD_Org_id = " + AD_Org_ID + " OR P_Notice_Bank_Deposit.Employee_AD_Org_id IS NULL ) ";
		sql = sql + " AND ( P_Notice_Bank_Deposit.P_Department_ID = " + P_Department_ID + " OR P_Notice_Bank_Deposit.P_Department_ID IS NULL ) ";
		sql = sql + " AND ( P_Notice_Bank_Deposit.C_Activity_ID = " + C_Activity_ID + " OR P_Notice_Bank_Deposit.C_Activity_ID IS NULL ) ";
		sql = sql + " AND ( P_Notice_Bank_Deposit.P_Employee_ID = " + P_Employee_ID + " OR P_Notice_Bank_Deposit.P_Employee_ID IS NULL ) ";

		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next() )
				P_Notice_Bank_Deposit_ID = rs.getInt( "P_Notice_Bank_Deposit_ID");

		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"* Error * P_Time_Sheet.GetWithPaymentID - " + sql + " - " , e);

		}
		
		if ( P_Notice_Bank_Deposit_ID == 0 ) return null;
		
		Notice_Bank_Deposit = new P_Notice_Bank_Deposit(ctx, P_Notice_Bank_Deposit_ID, trxName);
		
		return Notice_Bank_Deposit;
	}


	
	/**
	 * 	Get P_Notice_Bank_Deposit
	 *	@param ctx context
	 * 	@param P_Notice_Bank_Deposit_ID id
	 *	@return P_Notice_Bank_Deposit
	 */
	public static P_Notice_Bank_Deposit get (Properties ctx, int P_Notice_Bank_Deposit_ID, String trxName)
	{
		Integer key = new Integer (P_Notice_Bank_Deposit_ID);
		P_Notice_Bank_Deposit Notice_Bank_Deposit = s_cache.get(key);
		if (Notice_Bank_Deposit != null)
			return Notice_Bank_Deposit;
		Notice_Bank_Deposit = new P_Notice_Bank_Deposit(ctx, P_Notice_Bank_Deposit_ID, trxName);
		s_cache.put (key, Notice_Bank_Deposit);
		return Notice_Bank_Deposit;
	}	//	get

	/**	Cache						*/
	
	private static CCache<Integer,P_Notice_Bank_Deposit> s_cache = new CCache<Integer,P_Notice_Bank_Deposit>("P_Notice_Bank_Deposit", 20);

	public P_Notice_Bank_Deposit(Properties ctx, int P_Notice_Bank_Deposit_ID,
			String trxName)
	{
		super(ctx, P_Notice_Bank_Deposit_ID, trxName);
	}

	public P_Notice_Bank_Deposit(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}
	
}
