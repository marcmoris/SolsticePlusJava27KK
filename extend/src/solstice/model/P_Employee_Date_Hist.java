package solstice.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MRole;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import java.sql.Timestamp;

public class P_Employee_Date_Hist extends X_P_Employee_Date_Hist {
	
	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Employee.class);
	
	/**
	 * 	Get Employee_Date_Hist
	 *	@param ctx context
	 * 	@param P_Employee_Date_Hist_ID id
	 *	@return Employee_Date_Hist
	 */
	public static P_Employee_Date_Hist get (Properties ctx, int P_Employee_Date_Hist_ID, String trxName)
	{
		Integer key = new Integer (P_Employee_Date_Hist_ID);
		P_Employee_Date_Hist EmplDateHist = (P_Employee_Date_Hist)s_cache.get(key);
		if (EmplDateHist != null)
			return EmplDateHist;
		EmplDateHist = new P_Employee_Date_Hist (ctx, P_Employee_Date_Hist_ID, trxName);
		s_cache.put (key, EmplDateHist);
		return EmplDateHist;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Employee_Date_Hist>	s_cache = new CCache<Integer,P_Employee_Date_Hist>("EmplDateHist", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Employee_Date_Hist_ID id
	 */
	public P_Employee_Date_Hist (Properties ctx, int P_Employee_Date_Hist_ID, String trxName)
	{
		super (ctx, P_Employee_Date_Hist_ID, trxName);
		if (P_Employee_Date_Hist_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Employee_Date_Hist

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Employee_Date_Hist (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Employee_Date_Hist (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Employee_Date_Hist_ID"), trxName);
	}	//	P_Employee_Date_Hist


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Employee_Date_Hist[ID=")
			.append(this.getP_Employee_Date_Hist_ID())
//			.append(",Value=").append(getValue())
//			.append(",Name=").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString


	public static P_Employee_Date_Hist get( Properties ctx, Timestamp Date, String Type_Date_Hist, String trxName )
	{
		int id = -1;
		
		String sql = "SELECT P_Employee_Date_Hist_ID FROM P_Employee_Date_Hist Where Date_Hist = " + DB.TO_DATE( Date ) + " AND Type_Date_Hist = '" + Type_Date_Hist + "'";
		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				id = rs.getInt( "P_Employee_Date_Hist_ID");
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		if ( id == -1)
			return new P_Employee_Date_Hist( ctx, -1, trxName );
		
		return P_Employee_Date_Hist.get( ctx, id, trxName); 
	}
	
	public static void createHisto ( Properties ctx, P_Employee Employee, boolean newRecord )
	{
	    if (newRecord || Employee.is_ValueChanged("DateHired") || Employee.is_ValueChanged("DateHiredComment"))
	    {
	    	if ( Employee.getDateHired() != null )
	    	{
		    	//  creer un nouvel enregistrement dans la table P_EMPLOYEE_DATE_HIST
		    	P_Employee_Date_Hist EmpDateHist = P_Employee_Date_Hist.get( ctx,Employee.getDateHired(), "DateHired"  , Employee.get_TrxName());
		    	EmpDateHist.setAD_Org_ID(Employee.getAD_Org_ID());
		    	EmpDateHist.setDate_Hist(Employee.getDateHired());
		    	EmpDateHist.setComment(Employee.getDateHiredComment());
		    	EmpDateHist.setP_Employee_ID(Employee.getP_Employee_ID());
		    	EmpDateHist.setType_Date_Hist("DateHired");
		    	EmpDateHist.save();
	    		
	    	}
	    }

	    if (newRecord || Employee.is_ValueChanged("DateHired2"))
	    {
	    	if ( Employee.getDateHired2() != null )
	    	{
		    	//  creer un nouvel enregistrement dans la table P_EMPLOYEE_DATE_HIST
		    	P_Employee_Date_Hist EmpDateHist = P_Employee_Date_Hist.get( ctx,Employee.getDateHired2(), "DateHired2"  , Employee.get_TrxName());
		    	EmpDateHist.setAD_Org_ID(Employee.getAD_Org_ID());
		    	EmpDateHist.setDate_Hist(Employee.getDateHired2());
		    	EmpDateHist.setComment("");
		    	EmpDateHist.setP_Employee_ID(Employee.getP_Employee_ID());
		    	EmpDateHist.setType_Date_Hist("DateHired2");
		    	EmpDateHist.save();
	    		
	    	}
	    }

	    if (newRecord || Employee.is_ValueChanged("DateSeniority"))
	    {
	    	if ( Employee.getDateSeniority() != null )
	    	{
		    	//  creer un nouvel enregistrement dans la table P_EMPLOYEE_DATE_HIST
		    	P_Employee_Date_Hist EmpDateHist = P_Employee_Date_Hist.get( ctx,Employee.getDateSeniority(), "DateSeniority"  , Employee.get_TrxName());
		    	EmpDateHist.setAD_Org_ID(Employee.getAD_Org_ID());
		    	EmpDateHist.setDate_Hist(Employee.getDateSeniority());
		    	EmpDateHist.setComment("");
		    	EmpDateHist.setP_Employee_ID(Employee.getP_Employee_ID());
		    	EmpDateHist.setType_Date_Hist("DateSeniority");
		    	EmpDateHist.save();
	    		
	    	}
	    }

	    if (newRecord || Employee.is_ValueChanged("DateProbationary") )
	    {
	    	if ( Employee.getDateProbationary() != null )
	    	{
		    	//  creer un nouvel enregistrement dans la table P_EMPLOYEE_DATE_HIST
		    	P_Employee_Date_Hist EmpDateHist = P_Employee_Date_Hist.get( ctx,Employee.getDateProbationary(), "DateProbationary"  , Employee.get_TrxName());
		    	EmpDateHist.setAD_Org_ID(Employee.getAD_Org_ID());
		    	EmpDateHist.setDate_Hist(Employee.getDateProbationary());
		    	EmpDateHist.setComment("");
		    	EmpDateHist.setP_Employee_ID(Employee.getP_Employee_ID());
		    	EmpDateHist.setType_Date_Hist("DateProbationary");
		    	EmpDateHist.save();
	    		
	    	}
	    }

	    if (newRecord || Employee.is_ValueChanged("DateRehired") || Employee.is_ValueChanged("DateRehiredComment"))
	    {
	    	if ( Employee.getDateRehired() != null )
	    	{
		    	//  creer un nouvel enregistrement dans la table P_EMPLOYEE_DATE_HIST
		    	P_Employee_Date_Hist EmpDateHist = P_Employee_Date_Hist.get( ctx,Employee.getDateRehired(), "DateRehired"  , Employee.get_TrxName());
		    	EmpDateHist.setAD_Org_ID(Employee.getAD_Org_ID());
		    	EmpDateHist.setDate_Hist(Employee.getDateRehired());
		    	EmpDateHist.setComment(Employee.getDateRehiredComment());
		    	EmpDateHist.setP_Employee_ID(Employee.getP_Employee_ID());
		    	EmpDateHist.setType_Date_Hist("DateRehired");
		    	EmpDateHist.save();
	    		
	    	}
	    }

	    if (newRecord || Employee.is_ValueChanged("DateLayoff") || Employee.is_ValueChanged("LayoffComment"))
	    {
	    	if ( Employee.getDateLayoff() != null )
	    	{
		    	//  creer un nouvel enregistrement dans la table P_EMPLOYEE_DATE_HIST
		    	P_Employee_Date_Hist EmpDateHist = P_Employee_Date_Hist.get( ctx,Employee.getDateLayoff(), "DateLayoff"  , Employee.get_TrxName());
		    	EmpDateHist.setAD_Org_ID(Employee.getAD_Org_ID());
		    	EmpDateHist.setDate_Hist(Employee.getDateLayoff());
		    	EmpDateHist.setComment(Employee.getLayoffComment());
		    	EmpDateHist.setP_Employee_ID(Employee.getP_Employee_ID());
		    	EmpDateHist.setType_Date_Hist("DateLayoff");
		    	EmpDateHist.save();
	    		
	    	}
	    }
		
	}
}
