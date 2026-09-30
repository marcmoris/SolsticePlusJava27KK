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
 *  Assignment_Hst Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Assignment_Hst extends X_P_Assignment_Hst
{
	/**
	 * 	Get Assignment_Hst
	 *	@param ctx context
	 * 	@param P_Assignment_Hst_ID id
	 *	@return Assignment_Hst
	 */
	public static P_Assignment_Hst get (Properties ctx, int P_Assignment_Hst_ID, String trxName)
	{
		Integer key = new Integer (P_Assignment_Hst_ID);
		P_Assignment_Hst Assignment_Hst = (P_Assignment_Hst)s_cache.get(key);
		if (Assignment_Hst != null)
			return Assignment_Hst;
		Assignment_Hst = new P_Assignment_Hst (ctx, P_Assignment_Hst_ID, trxName);
		s_cache.put (key, Assignment_Hst);
		return Assignment_Hst;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Assignment_Hst>	s_cache = new CCache<Integer,P_Assignment_Hst>("P_Assignment_Hst", 20);

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Assignment_Hst.class);

	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Assignment_Hst_ID id
	 */
	public P_Assignment_Hst (Properties ctx, int P_Assignment_Hst_ID, String trxName)
	{
		super (ctx, P_Assignment_Hst_ID, trxName);
		if (P_Assignment_Hst_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Assignment_Hst

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Assignment_Hst (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Assignment_Hst (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Assignment_Hst_ID"), trxName);
	}	//	P_Assignment_Hst


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Assignment_Hst[ID=")
			.append(this.getP_Assignment_Hst_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
	//
	// Vérifier si la dernière ligne d'historique n'est pas déjà celle qu'on veux créer.
	//
	public static boolean getHisto( P_Assignment Assignment)
	{
		String sql = "Select P_Assignment_ID From P_Assignment_Hst " 
			   + " Where P_Employee_ID  = " + Assignment.getP_Employee_ID()
			   + "  And EffectIn = ( Select max( EffectIn ) from P_Assignment_Hst Where P_Employee_ID  = " + Assignment.getP_Employee_ID() + " ) "
			   ;

	PreparedStatement pstmt = null;

	int Assignment_ID = 0;
	try
	{
		pstmt = DB.prepareStatement (sql, null);
		ResultSet rs = pstmt.executeQuery ();
		if ( rs.next () )
			Assignment_ID = rs.getInt(1);
		rs.close ();
		pstmt.close ();
		pstmt = null;
	}
	catch (Exception e)
	{
		s_log.log(Level.SEVERE,"getHisto", e);
	}
		
	if ( Assignment_ID == Assignment.getP_Assignment_ID() )
		return true;
	
	return false;
	}

}	//	P_Assignment_Hst
