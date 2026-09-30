package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  Assignment_Distribution Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Assignment_Distribution extends X_P_Assignment_Distribution
{
	/**
	 * 	Get Assignment_Distribution
	 *	@param ctx context
	 * 	@param P_Assignment_Distribution_ID id
	 *	@return Assignment_Distribution
	 */
	public static P_Assignment_Distribution get (Properties ctx, int P_Assignment_Distribution_ID, String trxName)
	{
		Integer key = new Integer (P_Assignment_Distribution_ID);
		P_Assignment_Distribution Assignment_Distribution = (P_Assignment_Distribution)s_cache.get(key);
		if (Assignment_Distribution != null)
			return Assignment_Distribution;
		Assignment_Distribution = new P_Assignment_Distribution (ctx, P_Assignment_Distribution_ID, trxName);
		s_cache.put (key, Assignment_Distribution);
		return Assignment_Distribution;
	}	//	get

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Assignment_Distribution (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Assignment_Distribution (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Assignment_Distribution_ID"), trxName);
	}	//	P_Assignment_Distribution


	/**	Cache						*/
	private static CCache<Integer,P_Assignment_Distribution>	s_cache = new CCache<Integer,P_Assignment_Distribution>("P_Assignment_Distribution", 20);
	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Assignment_Distribution.class);


	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Assignment_Distribution_ID id
	 */
	public P_Assignment_Distribution (Properties ctx, int P_Assignment_Distribution_ID, String trxName)
	{
		super (ctx, P_Assignment_Distribution_ID, trxName);
		if (P_Assignment_Distribution_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Assignment_Distribution

	// 2014.04.17 ajout pour la m.a.j d'un affectation - lieu de travail
	public static P_Assignment_Distribution getWithAssignmentID( Properties ctx, int P_Assignment_ID, String trxName  ) 
	{
		String sql = null;
		sql = "Select * from P_Assignment_Distribution WHERE P_Assignment_ID = " + P_Assignment_ID;
		//
		
		P_Assignment_Distribution Assignment_Distribution = null;
		
		PreparedStatement pstmt = null;
		
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				Assignment_Distribution = new P_Assignment_Distribution( ctx, rs, trxName);
			}
			
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Employee - " + sql, e);
		}

		return Assignment_Distribution;
	}



}	//	P_Assignment_Distribution
