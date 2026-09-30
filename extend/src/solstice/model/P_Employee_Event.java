/*
 * Created on 2005-06-21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;


/**
 * @author vivdun01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_Employee_Event extends X_P_Employee_Event
{
	/**
	 * 	Get Employee_Event
	 *	@param ctx context
	 * 	@param P_Employee_Event_ID id
	 *	@return Employee_Event
	 */
	public static P_Employee_Event get (Properties ctx, int P_Employee_Event_ID, String trxName)
	{
		Integer key = new Integer (P_Employee_Event_ID);
		P_Employee_Event Employee_Event = (P_Employee_Event)s_cache.get(key);
		if (Employee_Event != null)
			return Employee_Event;
		Employee_Event = new P_Employee_Event (ctx, P_Employee_Event_ID, trxName);
		s_cache.put (key, Employee_Event);
		return Employee_Event;
	}	//	get


	/**	Cache						*/
	private static CCache<Integer,P_Employee_Event>	s_cache = new CCache<Integer,P_Employee_Event>("P_Employee_Event", 20);
	
	public P_Employee_Event (Properties ctx, int P_Employee_Event_ID, String trxName)
	{
		super (ctx, P_Employee_Event_ID, trxName);
		if (P_Employee_Event_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Employee_Event

	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Employee_Event (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Employee_Event_ID"), trxName);
	}	//	P_Employee_Event

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Employee_Event (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true or false
	 */

	protected boolean beforeSave (boolean newRecord)
	{

		P_Employee_Event Employee_Event = P_Employee_Event.get( Env.getCtx(), getP_Employee_Event_ID(), this.get_TrxName()); 
		
		// Retourne 1 s'il est impossible d'ajouter le nouvel enregistrement
		// (Retournera 1 seulement si c'est un type d'événement non multiple et que celui-ci est déjà créé)
		String sql = "SELECT count(*) " +  
					" FROM P_Employee_Event " + 
					" WHERE P_Employee_ID = " + this.getP_Employee_ID() + 
					" AND P_EventType_ID = " + this.getP_EventType_ID() + 
					" AND 'N' IN (SELECT IsMultipleEvent FROM P_EventType WHERE P_EventType_ID = " + this.getP_EventType_ID() + ")"; 
		
		PreparedStatement pstmt = null;
		
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			rs.first();
			if (rs.getInt(1) == 1)
			{
//				Log.saveError("NewError", Msg.translate(getCtx(), "MultipleEvent"));
				return false;
			}
		}
		catch (Exception e)
		{
			System.out.println ("P_Employee_Event - beforeSave - " + sql + " - " + e);
		}
		return true;

	}	//	beforeSave


}
