/*
 * Created on 2005-12-06
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

import solstice.model.X_P_Form_Field_Deduction;

/**
 *  Employee_Form Model
 *
 *  * @author alenav01
 */
public class P_Form_Field_Deduction extends X_P_Form_Field_Deduction
{
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Form_Employee
	 *	@param ctx context
	 * 	@param P_Form_Field_Deduction_ID id
	 *	@return Form_Field_Deduction
	 */
	public static P_Form_Field_Deduction get (Properties ctx, int P_Form_Field_Deduction_ID, String trxName)
	{
		Integer key = new Integer (P_Form_Field_Deduction_ID);
		P_Form_Field_Deduction Form_Field_Deduction = (P_Form_Field_Deduction)s_cache.get(key);
		if (Form_Field_Deduction != null)
			return Form_Field_Deduction;
		Form_Field_Deduction = new P_Form_Field_Deduction (ctx, P_Form_Field_Deduction_ID, trxName);
		s_cache.put (key, Form_Field_Deduction);
		return Form_Field_Deduction;
	}	//	get



	/**	Cache						*/
	private static CCache<Integer,P_Form_Field_Deduction>	s_cache = new CCache<Integer,P_Form_Field_Deduction>("P_Form_Field_Deduction", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Form_Field_Deduction_ID id
	 */
	public P_Form_Field_Deduction (Properties ctx, int P_Form_Field_Deduction_ID, String trxName)
	{
		super (ctx, P_Form_Field_Deduction_ID, trxName);
		if (P_Form_Field_Deduction_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Form_Field_Deduction

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Form_Field_Deduction (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}
	
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Form_Field_Deduction (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Form_Field_Deduction_ID"), trxName);
	}	//	P_Form_Field_Deduction



}	//	P_Form_Field_Deduction

