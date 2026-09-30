package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *  Employee_Form_Detail Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Employee_Form_Detail extends X_P_Employee_Form_Detail
{
	/**
	 * 	Get Employee_Form_Detail
	 *	@param ctx context
	 * 	@param P_Employee_Form_Detail_ID id
	 *	@return Employee_Form_Detail
	 */
	public static P_Employee_Form_Detail get (Properties ctx, int P_Employee_Form_Detail_ID, String trxName)
	{
		Integer key = new Integer (P_Employee_Form_Detail_ID);
		P_Employee_Form_Detail Employee_Form_Detail = (P_Employee_Form_Detail)s_cache.get(key);
		if (Employee_Form_Detail != null)
			return Employee_Form_Detail;
		Employee_Form_Detail = new P_Employee_Form_Detail (ctx, P_Employee_Form_Detail_ID, trxName);
		s_cache.put (key, Employee_Form_Detail);
		return Employee_Form_Detail;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Employee_Form_Detail>	s_cache = new CCache<Integer,P_Employee_Form_Detail>("P_Employee_Form_Detail", 20);
	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Employee_Form_Detail.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Employee_Form_Detail_ID id
	 */
	public P_Employee_Form_Detail (Properties ctx, int P_Employee_Form_Detail_ID, String trxName)
	{
		super (ctx, P_Employee_Form_Detail_ID, trxName);
		if (P_Employee_Form_Detail_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Employee_Form_Detail

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Employee_Form_Detail (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Employee_Form_Detail (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Employee_Form_Detail_ID"), trxName);
	}	//	P_Employee_Form_Detail


	public static P_Employee_Form_Detail getEmployee_Form_Detail( P_Employee_Form EmployeeForm, String Field)
	{
		int id = 0;

		P_Form_Template FormTemplate = P_Form_Template.get( Env.getCtx(), EmployeeForm.getP_Form_Template_ID(), EmployeeForm.get_TrxName());
		P_Form_Template_Detail FormTemplateDetail = P_Form_Template_Detail.getForm_Template_Detail( FormTemplate, Field );
		
		String sql = "Select P_Employee_Form_Detail_ID from P_Employee_Form_Detail "
			       + " Where P_Employee_Form_ID = " + EmployeeForm.getP_Employee_Form_ID()
				   + "   And P_Form_Template_Detail_ID = " + FormTemplateDetail.getP_Form_Template_Detail_ID()
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
			s_log.log(Level.SEVERE,"P_Employee_Form_Detail getEmployee_Form_Detail - " ,e);
		}
		
		if ( id != 0)
			return P_Employee_Form_Detail.get( Env.getCtx(), id , EmployeeForm.get_TrxName());
		else 
			return null;
		
	}
	
	public static P_Employee_Form_Detail getEmployee_Form_Detail( int P_Employee_Form_ID, int P_Form_Template_Detail_ID)
	{
		int id = 0;

		String sql = "Select P_Employee_Form_Detail_ID from P_Employee_Form_Detail "
			       + " Where P_Employee_Form_ID = " + P_Employee_Form_ID
				   + "   And P_Form_Template_Detail_ID = " + P_Form_Template_Detail_ID
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
			s_log.log(Level.SEVERE,"P_Employee_Form_Detail getEmployee_Form_Detail - " ,e);
		}
		
		if ( id != 0)
			return P_Employee_Form_Detail.get( Env.getCtx(), id , null);
		else 
			return null;
		
	}

	
}	//	P_Employee_Form_Detail
