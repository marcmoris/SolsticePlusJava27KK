package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  Form_Template_Detail Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Form_Template_Detail extends X_P_Form_Template_Detail
{
	/**
	 * 	Get Form_Template_Detail
	 *	@param ctx context
	 * 	@param P_Form_Template_Detail_ID id
	 *	@return Form_Template_Detail
	 */
	public static P_Form_Template_Detail get (Properties ctx, int P_Form_Template_Detail_ID, String trxName)
	{
		Integer key = new Integer (P_Form_Template_Detail_ID);
		P_Form_Template_Detail Form_Template_Detail = (P_Form_Template_Detail)s_cache.get(key);
		if (Form_Template_Detail != null)
			return Form_Template_Detail;
		Form_Template_Detail = new P_Form_Template_Detail (ctx, P_Form_Template_Detail_ID, trxName);
		s_cache.put (key, Form_Template_Detail);
		return Form_Template_Detail;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Form_Template_Detail>	s_cache = new CCache<Integer,P_Form_Template_Detail>("P_Form_Template_Detail", 20);
	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Form_Template_Detail.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Form_Template_Detail_ID id
	 */
	public P_Form_Template_Detail (Properties ctx, int P_Form_Template_Detail_ID, String trxName)
	{
		super (ctx, P_Form_Template_Detail_ID, trxName);
		if (P_Form_Template_Detail_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Form_Template_Detail

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Form_Template_Detail (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Form_Template_Detail (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Form_Template_Detail_ID"),trxName);
	}	//	P_Form_Template_Detail


	public static P_Form_Template_Detail getForm_Template_Detail( P_Form_Template FormTemplate, String Field )
	{
		int id = 0;
		String sql = "Select P_Form_Template_Detail_ID from P_Form_Template_Detail "
			       + " Where Value = '" + Field + "'"
				   + "  And P_Form_Template_ID = " + FormTemplate.getP_Form_Template_ID();
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
			System.err.println("P_Form_Template_Detail getForm_Template_Detail - " + e);
		}
		
		return P_Form_Template_Detail.get( Env.getCtx(), id, FormTemplate.get_TrxName() );
		
	}

	protected boolean afterSave (boolean newRecord, boolean success)
	{
	    
	    if (!success )
		{
			return success;
		}
//	    if (  newRecord )
//	    {
		    String sql = "SELECT  * FROM P_Employee_Form  "
	            + " WHERE P_Form_Template_ID = " + this.getP_Form_Template_ID()
	            + "   AND EffectIn = ( Select max( EffectIn ) From P_Employee_Form m Where M.P_Employee_ID = P_Employee_Form.P_Employee_ID AND M.P_Form_Template_ID = P_Employee_Form.P_Form_Template_ID )"
	            + "   AND not Exists( Select 1 From P_Employee_Form_Detail Where P_Employee_Form_Detail.P_Employee_Form_ID = P_Employee_Form.P_Employee_Form_ID And P_Employee_Form_Detail.P_Form_Template_Detail_ID = " + this.getP_Form_Template_Detail_ID() + " )"
	            ;
		    
		    PreparedStatement pstmt = null;
	   
			try
			{
				pstmt = DB.prepareStatement (sql, null);
				ResultSet rs = pstmt.executeQuery ();
				while (rs.next ())
				{
				    
				    P_Employee_Form_Detail EmployeeFormDet = new P_Employee_Form_Detail( Env.getCtx(), -1 , this.get_TrxName());
				    EmployeeFormDet.setP_Employee_Form_ID( rs.getInt("P_Employee_Form_ID"));
				    EmployeeFormDet.setFormTypeField( this.getFormTypeField()  );
				    EmployeeFormDet.setP_Form_Template_Detail_ID( this.getP_Form_Template_Detail_ID() );
				    EmployeeFormDet.save();
				    
				}
				rs.close ();
				pstmt.close ();
				pstmt = null;
				
				
			}
			catch (Exception e)
			{
				s_log.log(Level.SEVERE, "P_Form_Template, afterSave - ", e);
				return false;
			}
	    
	    
//	    }
	    
	    return true;
	}  //	afterSave	


}	//	P_Form_Template_Detail
