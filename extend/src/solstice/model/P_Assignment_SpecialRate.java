package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

import java.sql.Timestamp;
import java.sql.SQLException;

/**
 *  Assignment_SpecialRate Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Assignment_SpecialRate extends X_P_Assignment_SpecialRate
{
	/**
	 * 	Get Assignment_SpecialRate
	 *	@param ctx context
	 * 	@param P_Assignment_SpecialRate_ID id
	 *	@return Assignment_SpecialRate
	 */
	public static P_Assignment_SpecialRate get (Properties ctx, int P_Assignment_SpecialRate_ID, String trxName)
	{
		Integer key = new Integer (P_Assignment_SpecialRate_ID);
		P_Assignment_SpecialRate Assignment_SpecialRate = (P_Assignment_SpecialRate)s_cache.get(key);
		if (Assignment_SpecialRate != null)
			return Assignment_SpecialRate;
		Assignment_SpecialRate = new P_Assignment_SpecialRate (ctx, P_Assignment_SpecialRate_ID, trxName);
		s_cache.put (key, Assignment_SpecialRate);
		if(Assignment_SpecialRate != null)
		{
			Assignment_SpecialRate.setTrxName(trxName);
		}
		return Assignment_SpecialRate;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Assignment_SpecialRate>	s_cache = new CCache<Integer,P_Assignment_SpecialRate>("P_Assignment_SpecialRate", 20);

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Employee_LongTermLeave.class);

	private String				m_trxName = null;
	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Assignment_SpecialRate_ID id
	 */
	public P_Assignment_SpecialRate (Properties ctx, int P_Assignment_SpecialRate_ID, String trxName)
	{
		super (ctx, P_Assignment_SpecialRate_ID, trxName);
		if (P_Assignment_SpecialRate_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
		m_trxName = trxName;
	}	//	P_Assignment_SpecialRate

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Assignment_SpecialRate (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
		m_trxName = trxName; 
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Assignment_SpecialRate (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Assignment_SpecialRate_ID"), trxName);
		m_trxName = trxName;
	}	//	P_Assignment_SpecialRate


    public static P_Assignment_SpecialRate get(Properties ctx, int P_Assignment_ID, int P_SpecialRate_ID, Timestamp effectIn, String trxName)
	{
    	
		String sql = "SELECT P_Assignment_SpecialRate_ID FROM P_Assignment_SpecialRate "
			       + " WHERE P_Assignment_ID = " + P_Assignment_ID
				   + " AND P_SpecialRate_ID = " + P_SpecialRate_ID
				   + " AND EffectIn <= "+ DB.TO_DATE( effectIn )   
				   + " ORDER BY EffectIn Desc"
				   ;

		int P_Assignment_SpecialRate_ID = -1;
		
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				P_Assignment_SpecialRate_ID = rs.getInt("P_Assignment_SpecialRate_ID");
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Employee_LongTermLeave - GetP_Credits_ID - " + sql, e);
		}

		if ( P_Assignment_SpecialRate_ID != -1 )
			return P_Assignment_SpecialRate.get( ctx, P_Assignment_SpecialRate_ID, trxName);

		return null;

	}

	
	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Assignment_SpecialRate[ID=")
			.append(getP_Assignment_SpecialRate_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
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

	    //on va chercher l'objet precedent pour mettre sa date de fin a jour
	    String sql = "SELECT P_Assignment_SpecialRate_ID "
	    	+ " FROM P_Assignment_SpecialRate "
	    	+ " WHERE P_Assignment_ID = " + this.getP_Assignment_ID()
	    	+ " AND P_SpecialRate_ID = " + this.getP_SpecialRate_ID()
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
		    	P_Assignment_SpecialRate assignmentRate = P_Assignment_SpecialRate.get(Env.getCtx(), rs.getInt(1), m_trxName);
		    	if(assignmentRate == null || assignmentRate.getP_Assignment_SpecialRate_ID() <= 0)
		    	{
		    		return success;
		    	}
		    	Timestamp tsDate = this.getEffectIn();
		    	// - 1 pour la veille
		    	tsDate = TimeUtil.addDays( tsDate, -1);
		    	assignmentRate.setEffectTo(tsDate);
		    	assignmentRate.save();
		    }
	    }
	    catch(SQLException e)
	    {
	    	s_log.log(Level.SEVERE,"P_Assignment_SpecialRate - afterSave - " + sql, e);	    	
	    }
		return success;
	}	//	afterSave

	private void setTrxName ( String trxName)
	{
		m_trxName = trxName;
	}

}	//	P_Assignment_SpecialRate
