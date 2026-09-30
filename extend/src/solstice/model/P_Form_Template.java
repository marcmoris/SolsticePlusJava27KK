package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  Form_Template Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Form_Template extends X_P_Form_Template
{
	/**
	 * 	Get Form_Template
	 *	@param ctx context
	 * 	@param P_Form_Template_ID id
	 *	@return Form_Template
	 */
	public static P_Form_Template get (Properties ctx, int P_Form_Template_ID, String trxName)
	{
		Integer key = new Integer (P_Form_Template_ID);
		P_Form_Template Form_Template = (P_Form_Template)s_cache.get(key);
		if (Form_Template != null)
			return Form_Template;
		Form_Template = new P_Form_Template (ctx, P_Form_Template_ID, trxName);
		s_cache.put (key, Form_Template);
		return Form_Template;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Form_Template>	s_cache = new CCache<Integer,P_Form_Template>("P_Form_Template", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Form_Template_ID id
	 */
	public P_Form_Template (Properties ctx, int P_Form_Template_ID, String trxName)
	{
		super (ctx, P_Form_Template_ID, trxName);
		if (P_Form_Template_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Form_Template

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Form_Template (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Form_Template (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Form_Template_ID"), trxName);
	}	//	P_Form_Template

	
	public static int getForm_Template_ID( String Form )
	{
		int id = 0;
		String sql = "Select P_Form_Template_ID from P_Form_Template "
			       + " Where Value = '" + Form + "'";
				   ;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				id = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("P_Form_Template getForm_Template_ID - " + e);
		}
		
		return id;
		
	}

	public static P_Form_Template getForm_Template( String Form, String trxName )
	{
		int id = 0;
		String sql = "Select P_Form_Template_ID from P_Form_Template "
			       + " Where Value = '" + Form + "'";
				   ;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				id = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("P_Form_Template getForm_Template - " + e);
		}
		
		return P_Form_Template.get( Env.getCtx(), id , trxName);
		
	}

	
}	//	P_Form_Template
