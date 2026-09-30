package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

import solstice.model.X_P_Form_Field;
import java.sql.PreparedStatement;
import org.compiere.util.DB;
import java.util.logging.*;
import org.compiere.util.CLogger;
/**
 *  Employee_Form Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Form_Field extends X_P_Form_Field
{
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Form_Employee
	 *	@param ctx context
	 * 	@param P_Form_Employee_ID id
	 *	@return Form_Employee
	 */
	public static P_Form_Field get (Properties ctx, int P_Form_Employee_ID, String trxName)
	{
		Integer key = new Integer (P_Form_Employee_ID);
		P_Form_Field Form_Employee = (P_Form_Field)s_cache.get(key);
		if (Form_Employee != null)
			return Form_Employee;
		Form_Employee = new P_Form_Field (ctx, P_Form_Employee_ID, trxName);
		s_cache.put (key, Form_Employee);
		return Form_Employee;
	}	//	get



	/**	Cache						*/
	private static CCache<Integer,P_Form_Field>	s_cache = new CCache<Integer,P_Form_Field>("P_Form_Field", 20);
	/**	Logger							*/
	private static CLogger		log = CLogger.getCLogger (P_Form_Field.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Form_Employee_ID id
	 */
	public P_Form_Field (Properties ctx, int P_Form_Employee_ID, String trxName)
	{
		super (ctx, P_Form_Employee_ID, trxName);
		if (P_Form_Employee_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Form_Employee

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Form_Field (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}
	
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Form_Field (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Form_Employee_ID"), trxName);
	}	//	P_Form_Employee

	public static int getField( String value )
	{
	    String sql = "Select P_Form_Field_ID From P_Form_Field WHERE VALUE = '" + value + "'" ;
		int P_Form_Field_ID = 0;
	    
		PreparedStatement pstmt  = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			
			if (rs.next ())
			{
				P_Form_Field_ID = rs.getInt( "P_Form_Field_ID");
			}
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, "getField - " + sql, e);
		}
		return P_Form_Field_ID;
	}


}	//	P_Form_Field
