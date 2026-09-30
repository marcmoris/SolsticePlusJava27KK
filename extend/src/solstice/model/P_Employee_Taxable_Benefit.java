package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

/**
 *  Employee_Taxable_Benefit Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Employee_Taxable_Benefit extends X_P_Employee_Taxable_Benefit
{

	/**
	 * 	Get Employee_Taxable_Benefit
	 *	@param ctx context
	 * 	@param P_Employee_Taxable_Benefit_ID id
	 *	@return Employee_Taxable_Benefit
	 */
	public static P_Employee_Taxable_Benefit get (Properties ctx, int P_Employee_Taxable_Benefit_ID, String trxName)
	{
		Integer key = new Integer (P_Employee_Taxable_Benefit_ID);
		P_Employee_Taxable_Benefit employee_Taxable_Benefit = (P_Employee_Taxable_Benefit)s_cache.get(key);
		if (employee_Taxable_Benefit != null)
			return employee_Taxable_Benefit;
		employee_Taxable_Benefit = new P_Employee_Taxable_Benefit (ctx, P_Employee_Taxable_Benefit_ID, trxName);
		s_cache.put (key, employee_Taxable_Benefit);
		if(employee_Taxable_Benefit != null)
		{
			employee_Taxable_Benefit.setTrxName(trxName);
		}
		return employee_Taxable_Benefit;
	}	//	get

	
	public static P_Employee_Taxable_Benefit get (Properties ctx, int EmployeeID, int Taxable_BenefitID, Timestamp EffectIn, String trxName)
	{
	    String sql = "SELECT  P_Employee_Taxable_Benefit_ID FROM P_Employee_Taxable_Benefit "
            + " WHERE P_Employee_ID = " + EmployeeID
            + "   AND P_Taxable_Benefit_ID  = " + Taxable_BenefitID
			+ "    and EffectIn<= " + DB.TO_DATE( EffectIn )  
			+ "  Order By EffectIn Desc";

	    PreparedStatement pstmt = null;
    
	    int P_Employee_Taxable_Benefit_ID = -1;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
			    P_Employee_Taxable_Benefit_ID = rs.getInt(1);
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			
		}
		catch (Exception e)
		{
		    System.out.println( "P_Employee_Taxable_Benefit, get - " + e);
		    return null;
		}
	    
		P_Employee_Taxable_Benefit Employee_Taxable_Benefit = P_Employee_Taxable_Benefit.get( ctx, P_Employee_Taxable_Benefit_ID, trxName );
		return Employee_Taxable_Benefit;
	}	//	get


	//
	// Pour les déductions, on créer l'avantage imposable s'il n'existe pas, et la date d'entré en vigeur n'est pas prise en considération
	//
	public static P_Employee_Taxable_Benefit get (Properties ctx, int EmployeeID, int Taxable_BenefitID, String trxName)
	{
	    String sql = "SELECT  P_Employee_Taxable_Benefit_ID FROM P_Employee_Taxable_Benefit "
            + " WHERE P_Employee_ID = " + EmployeeID
            + "   AND P_Taxable_Benefit_ID  = " + Taxable_BenefitID
			+ "  Order By EffectIn Desc";

	    PreparedStatement pstmt = null;
    
	    int P_Employee_Taxable_Benefit_ID = -1;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
			    P_Employee_Taxable_Benefit_ID = rs.getInt(1);
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			
		}
		catch (Exception e)
		{
		    System.out.println( "P_Employee_Taxable_Benefit, get - " + e);
		    return null;
		}
		
		P_Employee_Taxable_Benefit Employee_Taxable_Benefit;
		if ( P_Employee_Taxable_Benefit_ID == -1)
			Employee_Taxable_Benefit = new P_Employee_Taxable_Benefit( ctx, -1, trxName );
		else	
			Employee_Taxable_Benefit = P_Employee_Taxable_Benefit.get( ctx, P_Employee_Taxable_Benefit_ID, trxName );
		return Employee_Taxable_Benefit;
	}	//	get


	/**	Cache						*/
	private static CCache<Integer,P_Employee_Taxable_Benefit>	s_cache = new CCache<Integer,P_Employee_Taxable_Benefit>("P_Employee_Taxable_Benefit", 20);

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Employee_LongTermLeave.class);

	private String				m_trxName = null;
	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Employee_Taxable_Benefit_ID id
	 */
	public P_Employee_Taxable_Benefit (Properties ctx, int P_Employee_Taxable_Benefit_ID, String trxName)
	{
		super (ctx, P_Employee_Taxable_Benefit_ID, trxName);
		if (P_Employee_Taxable_Benefit_ID == 0)
		{
			setAD_Org_ID(0);
		}
		m_trxName = trxName;
	}	//	P_Employee_Taxable_Benefit

	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Employee_Taxable_Benefit (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
		m_trxName = trxName;
	}
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Employee_Taxable_Benefit (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Employee_Taxable_Benefit_ID"), trxName);
		m_trxName = trxName;
	}	//	P_Employee_Taxable_Benefit

//	 
//	 
	
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

		return true;
	}	//	beforeSave
	
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
//	  on va chercher l'objet precedent pour mettre sa date de fin a jour
	    String sql = "SELECT P_Employee_Taxable_Benefit_ID "
	    	+ " FROM P_Employee_Taxable_Benefit "
	    	+ " WHERE P_Employee_ID = " + this.getP_Employee_ID()
	    	+ " AND P_Taxable_Benefit_ID = " + this.getP_Taxable_Benefit_ID()
	    	+ " AND EffectIn < " + DB.TO_DATE( this.getEffectIn() )
	    	+ " AND EffectTo IS NULL "
	    	+ " ORDER BY EffectIn Desc ";
	    PreparedStatement pstmt = DB.prepareStatement(sql, null);

	    try
	    {
		    ResultSet rs = pstmt.executeQuery();
		    if(rs.next())
		    {
		    	//si on a un objet, on set sa date de fin a la veille de la date de debut du nouvelle objet
		    	P_Employee_Taxable_Benefit employee_Taxable_Benefit = P_Employee_Taxable_Benefit.get(Env.getCtx(), rs.getInt(1), m_trxName);
		    	if(employee_Taxable_Benefit == null || employee_Taxable_Benefit.getP_Employee_Taxable_Benefit_ID() <= 0)
		    	{
		    		return success;
		    	}
		    	Timestamp tsDate = this.getEffectIn();
		    	// - 1 pour la veille
		    	tsDate = TimeUtil.addDays( tsDate, -1);
		    	employee_Taxable_Benefit.setEffectTo(tsDate);
		    	employee_Taxable_Benefit.save();
		    }
	    }
	    catch(SQLException e)
	    {
	    	s_log.log(Level.SEVERE,"P_Assignment_SpecialRate - afterSave - " + sql, e);	    	
	    }
		return success;
	}	//	afterSave

	
	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		P_Employee Employee = P_Employee.get( getCtx(), this.getP_Employee_ID(), this.get_TrxName() ); 
		P_Taxable_Benefit Taxable_Benefit = P_Taxable_Benefit.get( getCtx(), this.getP_Taxable_Benefit_ID(), this.get_TrxName() ); 

		StringBuffer sb = new StringBuffer ("P_Employee_Taxable_Bemefit[ID=")
			.append( this.getP_Employee_Taxable_Benefit_ID())
			.append(",Employee Value=").append(Employee.getValue())
			.append(",Name=").append(Employee.getName())
			.append(",Taxable_Benefit Value=").append(Taxable_Benefit.getValue())
			.append(",Name=").append(Taxable_Benefit.getName())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
	private void setTrxName ( String trxName)
	{
		m_trxName = trxName;
	}

}	//	P_Employee_Taxable_Benefit
