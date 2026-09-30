/*
 * Created on 2005-09-05
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 * @author alenav01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_T_AccidentReport extends X_P_T_AccidentReport
{

	public static P_T_AccidentReport get (Properties ctx, int P_T_ACCIDENTREPORT_ID, String trxName)
	{
		Integer key = new Integer (P_T_ACCIDENTREPORT_ID);
		P_T_AccidentReport ACCIDENTREPORT = (P_T_AccidentReport)s_cache.get(key);
		if (ACCIDENTREPORT != null)
			return ACCIDENTREPORT;
		ACCIDENTREPORT = new P_T_AccidentReport (ctx, P_T_ACCIDENTREPORT_ID, trxName);
		s_cache.put (key, ACCIDENTREPORT);
		return ACCIDENTREPORT;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_T_AccidentReport>	s_cache = new CCache<Integer,P_T_AccidentReport>("P_T_AccidentReport", 2);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_T_ACCIDENTREPORT_ID id
	 */
	public P_T_AccidentReport (Properties ctx, int P_T_ACCIDENTREPORT_ID, String trxName)
	{
		super (ctx, P_T_ACCIDENTREPORT_ID, trxName);
		if (P_T_ACCIDENTREPORT_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_T_ACCIDENTREPORT

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_T_AccidentReport (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_T_AccidentReport (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_T_ACCIDENTREPORT_ID"), trxName);
	}	//	P_T_ACCIDENTREPORT


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_T_ACCIDENTREPORT[ID=")
			.append(this.getP_T_AccidentReport_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString
		
	
}
