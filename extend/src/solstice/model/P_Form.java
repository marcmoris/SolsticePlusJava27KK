package solstice.model;


import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

import solstice.model.X_P_Form;

/**
 *  Form Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Form extends X_P_Form
{
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Form
	 *	@param ctx context
	 * 	@param C_Form_ID id
	 *	@return Form
	 */
	public static P_Form get (Properties ctx, int P_Form_ID, String trxName)
	{
		Integer key = new Integer (P_Form_ID);
		P_Form Form = (P_Form)s_cache.get(key);
		if (Form != null)
			return Form;
		Form = new P_Form (ctx, P_Form_ID, trxName);
		s_cache.put (key, Form);
		return Form;
	}	//	get

	/**
	 * 	Get optionally cached Form
	 *	@param ctx context
	 *	@return Form
	 */
//	public static P_Form get (Properties ctx)
//	{
//		return get (ctx, Env.getP_Form_ID(ctx));
//	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Form>	s_cache = new CCache<Integer,P_Form>("P_Form", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Form_ID id
	 */
	public P_Form (Properties ctx, int P_Form_ID, String trxName)
	{
		super (ctx, P_Form_ID, trxName);
		if (P_Form_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Form

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Form (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Form (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Form_ID"), trxName);
	}	//	P_Form



}	//	P_Form
