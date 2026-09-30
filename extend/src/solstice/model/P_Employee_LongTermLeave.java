package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  Employee_LongTermLeave Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Employee_LongTermLeave extends X_P_Employee_LongTermLeave
{
	/**
	 * 	Get Employee_LongTermLeave
	 *	@param ctx context
	 * 	@param P_Employee_LongTermLeave_ID id
	 *	@return Employee_LongTermLeave
	 */
	public static P_Employee_LongTermLeave get (Properties ctx, int P_Employee_LongTermLeave_ID, String trxName)
	{
		Integer key = new Integer (P_Employee_LongTermLeave_ID);
		P_Employee_LongTermLeave Employee_LongTermLeave = (P_Employee_LongTermLeave)s_cache.get(key);
		if (Employee_LongTermLeave != null)
			return Employee_LongTermLeave;
		Employee_LongTermLeave = new P_Employee_LongTermLeave (ctx, P_Employee_LongTermLeave_ID, trxName);
		s_cache.put (key, Employee_LongTermLeave);
		return Employee_LongTermLeave;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Employee_LongTermLeave>	s_cache = new CCache<Integer,P_Employee_LongTermLeave>("P_Employee_LongTermLeave", 20);


	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Employee_LongTermLeave.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Employee_LongTermLeave_ID id
	 */
	public P_Employee_LongTermLeave (Properties ctx, int P_Employee_LongTermLeave_ID, String trxName)
	{
		super (ctx, P_Employee_LongTermLeave_ID, trxName);
		if (P_Employee_LongTermLeave_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Employee_LongTermLeave

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Employee_LongTermLeave (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Employee_LongTermLeave (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Employee_LongTermLeave_ID"), trxName);
	}	//	P_Employee_LongTermLeave

	
	//
	// Return Gain ID en fonction du nombres de jours que l'employé est en ALD.
	//
    public int getP_Gain_ID( int nbrDay)
	{
    	int GainID = 0;
		String sql = "SELECT P_Gain_ID FROM P_LongTermLeave_Gain "
			       + " WHERE P_LongTermLeave_ID = " + this.getP_LongTermLeave_ID()
				   + " AND P_Method_LongTermLeave_ID = " + this.getP_Method_LongTermLeave_ID() 
				   + " AND Day <= " + nbrDay
		           + " ORDER BY Day DESC "
				   ;

		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				GainID = rs.getInt("P_Gain_ID");
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Employee_LongTermLeave - GetP_Gain_ID - " + sql, e);
		}

    	return GainID;
	}

    
	//
	//
	public static P_Employee_LongTermLeave get (  Properties ctx, int P_Employee_ID, int P_LongTermLeave_ID, String trxName )
	{
		P_Employee_LongTermLeave Employee_LongTermLeave = null;
	    String sql = "Select P_Employee_LongTermLeave_ID From P_Employee_LongTermLeave Where P_Employee_ID = " + P_Employee_ID + " and P_LongTermLeave_ID = " + P_LongTermLeave_ID;

	    int Employee_LongTermLeave_ID = 0;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				Employee_LongTermLeave_ID = rs.getInt("P_Employee_LongTermLeave_ID"); 
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Form_Employee, - " + sql, e);
		}

		if ( Employee_LongTermLeave_ID != 0)
			Employee_LongTermLeave = P_Employee_LongTermLeave.get( ctx, Employee_LongTermLeave_ID, trxName);
		else
			Employee_LongTermLeave = new P_Employee_LongTermLeave( ctx, -1, trxName); 

		return Employee_LongTermLeave;
		
	}	//	


	//
	//
	public static P_Employee_LongTermLeave get (  Properties ctx, int P_Employee_ID, int P_LongTermLeave_ID, Timestamp LongTermLeaveDate, String trxName )
	{
		P_Employee_LongTermLeave Employee_LongTermLeave = null;
	    String sql = "Select P_Employee_LongTermLeave_ID From P_Employee_LongTermLeave "
	    	       + " Where P_Employee_ID = " + P_Employee_ID 
	               + " and P_LongTermLeave_ID = " + P_LongTermLeave_ID
	    		   + " and StartDate = " + DB.TO_DATE( LongTermLeaveDate )
	    		   ;

	    int Employee_LongTermLeave_ID = 0;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				Employee_LongTermLeave_ID = rs.getInt("P_Employee_LongTermLeave_ID"); 
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Form_Employee, - " + sql, e);
		}

		if ( Employee_LongTermLeave_ID != 0)
			Employee_LongTermLeave = P_Employee_LongTermLeave.get( ctx, Employee_LongTermLeave_ID, trxName);
		else
			Employee_LongTermLeave = new P_Employee_LongTermLeave( ctx, -1, trxName); 

		return Employee_LongTermLeave;
		
	}	//	
	
	private Timestamp CurrentLongTermLeaveDate;

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true or false
	 */

	protected boolean beforeSave (boolean newRecord)
	{
	   //+ 2011.07.05 + sécurité par compagnie.
	   P_Employee Employee = P_Employee.get( Env.getCtx(), this.getP_Employee_ID(), this.get_TrxName());
	   this.setAD_Org_ID( Employee.getAD_Org_ID());
  	   //- 2011.07.05 + sécurité par compagnie.

	   CurrentLongTermLeaveDate = getCurrentLongTermLeaveDate();
	   return true;
	}

	
	protected boolean afterSave (boolean newRecord, boolean success)
	{
	    
	    if (!success )
		{
			return success;
		}
	    
    	P_Employee Employee = P_Employee.get( Env.getCtx(), this.getP_Employee_ID(), this.get_TrxName());
	    if ( is_ValueChanged("StartDate") && Employee.getP_LongTermLeave_ID() == this.getP_LongTermLeave_ID() && Employee.getLongTermLeaveDate().compareTo( CurrentLongTermLeaveDate ) == 0 )
	    {
	    	Employee.setLongTermLeaveDate(this.getStartDate());
	    	Employee.save();
	    }

	    
	    return true;
	}

	public Timestamp getCurrentLongTermLeaveDate()
	{
		String sql = "SELECT StartDate FROM P_Employee_LongTermLeave WHERE P_Employee_LongTermLeave_ID = " + this.getP_Employee_LongTermLeave_ID();

		Timestamp date = null;
		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				date = rs.getTimestamp( 1 );
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		
		return date;
		
	}

}	//	P_Employee_LongTermLeave
