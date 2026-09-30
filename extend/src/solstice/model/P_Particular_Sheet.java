package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.Env;

/**
 *  Particular_Sheet Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Particular_Sheet extends X_P_Particular_Sheet
{
	/**
	 * 	Get Particular_Sheet
	 *	@param ctx context
	 * 	@param P_Particular_Sheet_ID id
	 *	@return Particular_Sheet
	 */
	public static P_Particular_Sheet get (Properties ctx, int P_Particular_Sheet_ID, String trxName)
	{
		Integer key = new Integer (P_Particular_Sheet_ID);
		P_Particular_Sheet Particular_Sheet = (P_Particular_Sheet)s_cache.get(key);
		if (Particular_Sheet != null)
			return Particular_Sheet;
		Particular_Sheet = new P_Particular_Sheet (ctx, P_Particular_Sheet_ID, trxName);
		s_cache.put (key, Particular_Sheet);
		return Particular_Sheet;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Particular_Sheet>	s_cache = new CCache<Integer,P_Particular_Sheet>("P_Particular_Sheet", 20);
	/**	Logger						*/
	private static CLogger		s_log = CLogger.getCLogger (P_Particular_Sheet.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Particular_Sheet_ID id
	 */
	public P_Particular_Sheet (Properties ctx, int P_Particular_Sheet_ID, String trxName)
	{
		super (ctx, P_Particular_Sheet_ID, trxName);
		if (P_Particular_Sheet_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Particular_Sheet

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Particular_Sheet (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Particular_Sheet (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Particular_Sheet_ID"), trxName);
	}	//	P_Particular_Sheet


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Particular_Sheet[ID=")
			.append(this.getP_Particular_Sheet_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true or false
	 */

	protected boolean beforeSave (boolean newRecord)
	{
		//+ 2011.07.05 + sécurité par compagnie.
		P_Employee Employee = P_Employee.get(getCtx(), getP_Employee_ID(), this.get_TrxName());
		this.setAD_Org_ID( Employee.getAD_Org_ID());
		//- 2011.07.05 + sécurité par compagnie.
		
		if ( this.getP_Assignment_ID() != 0)
		{
			P_Assignment Assignment = P_Assignment.get(Env.getCtx(), this.getP_Assignment_ID(), this.get_TrxName());
			
			if ( Assignment.getP_Employee_ID() != this.getP_Employee_ID())
			{
				log.saveError("ValidationError", "Affectation n'est pas en relation avec cet employé");
				return false;
			}
		}

		P_Period Period = P_Period.get( Env.getCtx(), this.getP_Period_ID(), null);
		if ( this.getDay().before( Period.getStartDate()) || this.getDay().after( Period.getEndDate()) )
		{
			log.saveError("ValidationError", "Date non inclus dans dans la période de paie ");
			return false;
		}
			

		return true;
	}
	
}	//	P_Particular_Sheet
