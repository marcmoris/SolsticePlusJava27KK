package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MRole;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

import solstice.process.PgiUtil;

/**
 *  Standard_Time Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Standard_Time extends X_P_Standard_Time
{
	/**
	 * 	Get Standard_Time
	 *	@param ctx context
	 * 	@param P_Standard_Time_ID id
	 *	@return Standard_Time
	 */
	public static P_Standard_Time get (Properties ctx, int P_Standard_Time_ID, String trxName)
	{
		Integer key = new Integer (P_Standard_Time_ID);
		P_Standard_Time Standard_Time = (P_Standard_Time)s_cache.get(key);
		if (Standard_Time != null)
			return Standard_Time;
		Standard_Time = new P_Standard_Time (ctx, P_Standard_Time_ID, trxName);
		s_cache.put (key, Standard_Time);
		return Standard_Time;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Standard_Time>	s_cache = new CCache<Integer,P_Standard_Time>("P_Standard_Time", 20);
	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Standard_Time.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Standard_Time_ID id
	 */
	public P_Standard_Time (Properties ctx, int P_Standard_Time_ID, String trxName)
	{
		super (ctx, P_Standard_Time_ID, trxName);
		if (P_Standard_Time_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Standard_Time

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Standard_Time (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Standard_Time (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Standard_Time_ID"), trxName);
	}	//	P_Standard_Time

	/**
	 * 	Load all record - for Performace.
	 *	@param ctx context
	 */
	public static void loadAll (Properties ctx)
	{
		//
		s_cache = new CCache<Integer,P_Standard_Time>("P_Standard_Time", 250);
		String sql = "SELECT P_Standard_Time_ID FROM P_Standard_Time WHERE IsActive='Y'";
		sql = MRole.getDefault().addAccessSQL (sql, "P_Standard_Time", true, false);	// fully qualidfied - RO 

		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				P_Standard_Time Standard_Time = new P_Standard_Time (ctx, rs.getInt("P_Standard_Time_ID"), null);
				s_cache.put( Standard_Time.getP_Standard_Time_ID(), Standard_Time);
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
	}	//	loadAll

	
	/**
	 * 	Before Delete.
	 *	@return true 
	 */
	protected boolean beforeDelete ()
	{
		
		// audit trail 
		if ( PgiUtil.getSolsticeParameter(Env.getCtx(), "AuditTrailInsurance").equals("True")  )
			P_Audit_Trail_Insurance.setInsurableSalary( this.getP_Employee_ID() );
		
		return true;
	}	//	beforeDelete

	/**
	 * 	After Delete
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterDelete (boolean success)
	{
		// audit trail 
		if (success)
			if ( PgiUtil.getSolsticeParameter(Env.getCtx(), "AuditTrailInsurance").equals("True")  )
				P_Audit_Trail_Insurance.InsurableSalary_Audit( this.getP_Employee_ID() );
			
		return success;
	}	//	afterDelete
	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true or false
	 */

	protected boolean beforeSave (boolean newRecord)
	{
		if ( this.getP_Assignment_ID() != 0)
		{
			P_Assignment Assignment = P_Assignment.get(Env.getCtx(), this.getP_Assignment_ID(), this.get_TrxName());
			
			if ( Assignment.getP_Employee_ID() != this.getP_Employee_ID())
			{
				log.saveError("ValidationError", "Affectation n'est pas en relation avec cet employé");
				return false;
			}
		}

		// audit trail 
		if ( PgiUtil.getSolsticeParameter(Env.getCtx(), "AuditTrailInsurance").equals("True")  )
			P_Audit_Trail_Insurance.setInsurableSalary( this.getP_Employee_ID() );


		return true;
	}

	/**
	 * 	After Save
	 *	@param newRecord
	 *	@param success
	 */

	protected boolean afterSave (boolean newRecord, boolean success)
	{
	    
	    if (!success )
		{
			return success;
		}


		// audit trail 
		if ( PgiUtil.getSolsticeParameter(Env.getCtx(), "AuditTrailInsurance").equals("True")  )
			P_Audit_Trail_Insurance.InsurableSalary_Audit( this.getP_Employee_ID() );

		
	    //on va chercher l'objet precedent pour mettre sa date de fin a jour
	    String sql = "SELECT P_Standard_Time_ID "
	    	+ " FROM P_Standard_Time "
	    	+ " WHERE P_Employee_ID = " + this.getP_Employee_ID()
	    	+ " AND P_Standard_Time_ID = " + this.getP_Standard_Time_ID()
	    	+ " AND StartDate < " + DB.TO_DATE( this.getStartDate() )
	    	+ " AND EndDate IS NULL "
	    	+ " ORDER BY StartDate Desc ";
	    PreparedStatement pstmt = DB.prepareStatement(sql, null);

	    try
	    {
		    ResultSet rs = pstmt.executeQuery();
		    if(rs.next())
		    {
		    	//si on a un objet, on set sa date de fin a la veille de la date de debut du nouvelle objet
		    	P_Standard_Time employee_Standard_Time = P_Standard_Time.get(Env.getCtx(), rs.getInt(1), this.get_TrxName());
		    	if(employee_Standard_Time == null || employee_Standard_Time.getP_Standard_Time_ID() <= 0)
		    	{
		    		return success;
		    	}
		    	Timestamp tsDate = this.getStartDate();
		    	// - 1 pour la veille
		    	tsDate = TimeUtil.addDays( tsDate, -1);
		    	employee_Standard_Time.setEndDate(tsDate);
		    	employee_Standard_Time.save();
		    }
	    }
	    catch(SQLException e)
	    {
	    	s_log.log(Level.SEVERE,"P_Standard_Time - afterSave - " + sql, e);	    	
	    }
	    

	    
		return true;
	}  //	afterSave	



}	//	P_Standard_Time
