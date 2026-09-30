package solstice.model;

import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

import solstice.model.X_P_Form_Employee_Detail;
import java.sql.ResultSet;

/**
 *  Employee_Form Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Form_Employee_Detail extends X_P_Form_Employee_Detail
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
	public static P_Form_Employee_Detail get (Properties ctx, int P_Form_Employee_ID, String trxName)
	{
		Integer key = new Integer (P_Form_Employee_ID);
		P_Form_Employee_Detail Form_Employee = (P_Form_Employee_Detail)s_cache.get(key);
		if (Form_Employee != null)
			return Form_Employee;
		Form_Employee = new P_Form_Employee_Detail (ctx, P_Form_Employee_ID, trxName);
		s_cache.put (key, Form_Employee);
		return Form_Employee;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Form_Employee_Detail>	s_cache = new CCache<Integer,P_Form_Employee_Detail>("P_Form_Employee_Detail", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Form_Employee_ID id
	 */
	public P_Form_Employee_Detail (Properties ctx, int P_Form_Employee_ID, String trxName)
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
	public P_Form_Employee_Detail (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Form_Employee_Detail (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Form_Employee_ID"), trxName);
	}	//	P_Form_Employee



}	//	P_Form_Employee
