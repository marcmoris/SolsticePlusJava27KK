package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.Vector;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;



/**
 *  Payment_Registry Model
 *
 *  @author alenav01
 */
public class P_Payment_Registry extends X_P_Payment_Registry
{
	
	private String ClassErrorMessage ="ERROR FILLING TABLE P_PAYMENT_REGISTRY";
	private int  LineNumber=0;
	private Vector PaymentRegistryIDs;
	private Vector TotalPaymentGroupRegistryIDs;
	private Vector TotalPaymentRegistryIDs;
	private boolean ThereAreRecords=false;
	private boolean IsFisrt=true;
	
	private int PaymentId=0;
	private int PeriodID=0;
	private int PaymentGroupID=0;
	private int EmployeeID=0;
	private int OccupationGroup=0;
	private int CurrentPaymentGroupID=0;
	private int OrgID=0;
	private int ClientID=0;
	private int YearID=0;
	private int PeriodNo;
	
	/**
	 * 	Get Payment_Registry
	 *	@param ctx context
	 * 	@param P_Payment_Registry_ID id
	 *	@return Payment_Registry
	 */
	public static P_Payment_Registry get (Properties ctx, int P_Payment_Registry_ID, String trxName)
	{
		Integer key = new Integer (P_Payment_Registry_ID);
		P_Payment_Registry Payment_Registry = (P_Payment_Registry)s_cache.get(key);
		if (Payment_Registry != null)
			return Payment_Registry;
		Payment_Registry = new P_Payment_Registry (ctx, P_Payment_Registry_ID, trxName);
		s_cache.put (key, Payment_Registry);
		return Payment_Registry;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Payment_Registry>	s_cache = new CCache<Integer,P_Payment_Registry>("P_Payment_Registry", 100);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Payment_Registry_ID id
	 */
	public P_Payment_Registry (Properties ctx, int P_Payment_Registry_ID, String trxName)
	{
		super (ctx, P_Payment_Registry_ID, trxName);
		if (P_Payment_Registry_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Payment_Registry

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Payment_Registry (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Payment_Registry (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Payment_Registry_ID"), trxName);
	}	//	P_Payment_Registry


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Payment_Registry[ID=")
			.append(this.getP_Payment_Registry_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString
		
	/*Start*/
	public void StartToFillCumulativesTables(int P_Period_ID)
	{
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement("EXEC SP_P_PAYMENT_REGISTRY_CUMULATIVE " + P_Period_ID, null);
			pstmt.execute();
			
		}
		catch (Exception e)
		{
			System.err.println(ClassErrorMessage + " [StartToFillCumulativesTables] " + e);
		}
	} 

	public void StartToFillEmployeesTable(int P_Period_ID,int ADPInstanceID)
	{
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement("EXEC SP_P_PAYMENT_REGISTRY_EMPLOYEES " + ADPInstanceID + "," + P_Period_ID, null);
			pstmt.execute();
		}
		catch (Exception e)
		{
			System.err.println(ClassErrorMessage + " [StartToFillEmployeesTable] " + e);
		}
	} 
}//	P_Payment_Registry
