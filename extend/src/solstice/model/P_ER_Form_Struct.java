/*
 * Created on 13 juil. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_ER_Form_Struct extends X_P_ER_Form_Struct {

	/**
	 * 	Get Payment
	 *	@param ctx context
	 * 	@param P_Payment_ID id
	 *	@return Payment
	 */
	public static P_ER_Form_Struct get (Properties ctx, int P_ER_Form_Struct_ID, String trxName)
	{
		Integer key = new Integer (P_ER_Form_Struct_ID);
		P_ER_Form_Struct Form = (P_ER_Form_Struct)s_cache.get(key);
		if (Form != null)
			return Form;
		Form = new P_ER_Form_Struct (ctx, P_ER_Form_Struct_ID, trxName);
		s_cache.put (key, Form);
		return Form;
	}	//	get
	/**	Cache						*/
	private static CCache<Integer,P_ER_Form_Struct>	s_cache = new CCache<Integer,P_ER_Form_Struct>("P_ER_Form_Struct", 20);

	/**
	 * @param ctx
	 * @param P_ER_Form_Struct_ID
	 */
	public P_ER_Form_Struct(Properties ctx, int P_ER_Form_Struct_ID, String trxName) {
		super(ctx, P_ER_Form_Struct_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param ctx
	 * @param rs
	 */
	public P_ER_Form_Struct(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

}
