package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
import java.util.logging.*;

import org.compiere.util.*;
  
/**
 *  Employee_Form Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Employee_Form extends X_P_Employee_Form
{
	/**
	 * 	Get Employee_Form
	 *	@param ctx context
	 * 	@param P_Employee_Form_ID id
	 *	@return Employee_Form
	 */
	public static P_Employee_Form get (Properties ctx, int P_Employee_Form_ID, String trxName)
	{
		Integer key = new Integer (P_Employee_Form_ID);
		P_Employee_Form Employee_Form = (P_Employee_Form)s_cache.get(key);
		if (Employee_Form != null)
			return Employee_Form;
		Employee_Form = new P_Employee_Form (ctx, P_Employee_Form_ID, trxName);
		s_cache.put (key, Employee_Form);
		return Employee_Form;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Employee_Form>	s_cache = new CCache<Integer,P_Employee_Form>("P_Employee_Form", 20);
	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Employee_Form.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Employee_Form_ID id
	 */
	public P_Employee_Form (Properties ctx, int P_Employee_Form_ID, String trxName)
	{
		super (ctx, P_Employee_Form_ID, trxName);
		if (P_Employee_Form_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Employee_Form

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Employee_Form (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	public static int getEmployee_Form_ID( int EmployeeID, String Form)
	{
		int id = 0;
		String sql = "Select P_Employee_Form_ID from P_Employee_Form "
			       + " Where P_Employee_ID = " + EmployeeID
				   + "   And P_Form_Template_ID = " + P_Form_Template.getForm_Template_ID( Form );
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
			System.err.println("P_Employee_Form getForm - " + e);
		}
		
		return id;
		
	}

	
	public static P_Employee_Form getEmployee_Form( int EmployeeID, Timestamp EffectIn, String Form, String trxName)
	{
		int id = 0;
		String sql = "Select P_Employee_Form_ID from P_Employee_Form "
			       + " Where P_Employee_ID = " + EmployeeID
				   + "   AND EffectIn = " + DB.TO_DATE( EffectIn)
				   + "   And P_Form_Template_ID = " + P_Form_Template.getForm_Template_ID( Form );
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
			System.err.println("P_Employee_Form getForm - " + e);
		}
		
		if ( id == 0 ) 
			return null;
		
		return P_Employee_Form.get( Env.getCtx(), id , trxName);
		
	}

	
	public static P_Employee_Form getEmployee_Form( int EmployeeID, int P_Form_Template_ID, String trxName)
	{
		int id = 0;
		String sql = "Select P_Employee_Form_ID from P_Employee_Form "
			       + " Where P_Employee_ID = " + EmployeeID
//				   + "   AND EffectIn = " + DB.TO_DATE( EffectIn)
				   + "   And P_Form_Template_ID = " + P_Form_Template_ID;
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
			System.err.println("P_Employee_Form getForm - " + e);
		}
		
		if ( id == 0 ) 
			return null;
		
		return P_Employee_Form.get( Env.getCtx(), id , trxName);
		
	}

	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Employee_Form (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Employee_Form_ID"), trxName);
	}	//	P_Employee_Form

	
	/**
	 * 	Before Delete
	 *	@return true if it can be deleted
	 */
	protected boolean beforeDelete ()
	{
		return true;
	}
	
	/**
	 * 	After Delete
	 *	@param success
	 *	@return
	 */
	protected boolean afterDelete (boolean success)
	{
		return success;
	}
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true or false
	 */

	protected boolean beforeSave (boolean newRecord)
	{
		//+ 2011.07.05 + sécurité par compagnie.
 	    P_Employee Employee = P_Employee.get( Env.getCtx(), this.getP_Employee_ID(), this.get_TrxName());
		this.setAD_Org_ID( Employee.getAD_Org_ID());
		//- 2011.07.05 + sécurité par compagnie.
		return true;
	}	//	beforeSave

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
	    
	    if (!success )
		{
			return success;
		}

	    if (newRecord)
		{
		    String sql = "SELECT  P_Form_Template_Detail_ID, FormTypeField FROM P_Form_Template_Detail  "
	            + " WHERE P_Form_Template_ID = " + this.getP_Form_Template_ID();
		    
		    PreparedStatement pstmt = null;
	   
			try
			{
				pstmt = DB.prepareStatement (sql, null);
				ResultSet rs = pstmt.executeQuery ();
				while (rs.next ())
				{
				    
				    P_Employee_Form_Detail EmployeeFormDet = new P_Employee_Form_Detail( Env.getCtx(), -1 , this.get_TrxName());
				    EmployeeFormDet.setP_Employee_Form_ID( this.getP_Employee_Form_ID());
				    EmployeeFormDet.setFormTypeField( rs.getString("FormTypeField") );
				    EmployeeFormDet.setP_Form_Template_Detail_ID( rs.getInt("P_Form_Template_Detail_ID") );

				    // Harcode pour simplifier la saise
				    
//				    P_Employee Employee = P_Employee.get( Env.getCtx(), this.getP_Employee_ID(), this.get_TrxName());
//				    if ( rs.getInt("P_Form_Template_Detail_ID") == 999999 )
//				    	EmployeeFormDet.setFormFieldAmount( P_Tax.getTD1_info( P_Tax.getCurrentTax(), "TD1.01"));
//				    if ( rs.getInt("P_Form_Template_Detail_ID") == 1000000 )
//				    	EmployeeFormDet.setFormFieldAmount( P_Tax.getTD1Provincial_info( P_Tax.getCurrentTax(), Employee.getTaxation_Region_ID(), "TP1015.01") );
				    EmployeeFormDet.save();
				}
				rs.close ();
				pstmt.close ();
				pstmt = null;
				
				
			}
			catch (Exception e)
			{
				s_log.log(Level.SEVERE, "P_Employee_Form, afterSave - ", e);
				return false;
			}
	        
		}
	    
	    //on va chercher l'objet precedent pour mettre sa date de fin a jour
	    String sql = "SELECT P_Employee_Form_ID "
	    	+ " FROM P_Employee_Form "
	    	+ " WHERE P_Employee_ID = " + this.getP_Employee_ID()
	    	+ " AND P_Form_Template_ID = " + this.getP_Form_Template_ID()
	    	+ " AND EffectIn < " + DB.TO_DATE( this.getEffectIn() )
	    	+ " AND EffectTo IS NULL "
	    	+ " ORDER BY EffectIn Desc ";
	    PreparedStatement pstmt = DB.prepareStatement(sql, null);

	    try
	    {
		    ResultSet rs = pstmt.executeQuery();
		    if(rs.next())
		    {
		    	//si on a un objet, on set sa date de fin a la veille de la date de debut du nouvelle objet
		    	P_Employee_Form employee_form = P_Employee_Form.get(Env.getCtx(), rs.getInt(1), this.get_TrxName() );
		    	if(employee_form == null || employee_form.getP_Employee_Form_ID() <= 0)
		    	{
		    		return success;
		    	}
		    	Timestamp tsDate = this.getEffectIn();
		    	// - 1 pour la veille
		    	tsDate = TimeUtil.addDays( tsDate, -1);
		    	employee_form.setEffectTo(tsDate);
		    	employee_form.save();
		    }
	    }
	    catch(SQLException e)
	    {
	    	s_log.log(Level.SEVERE,"employee_form - afterSave - " + sql, e);	    	
	    }

		return true;
	}  //	afterSave	

    /**
     * Procédure de copie. Lorsqu'on copie une déduction, on doit également
     * copier tous les paramètres et le contenue des onglets sous celle-ci.
     */
    public void afterCopy(int originalId)
    {
        try
        {
            // On copie les tables P_Deduction_Param, P_Deduction_Insurance, P_Deduction_Insurance_Bound, 
            // P_Deduction_Param_Ympe, P_Deduction_Account et P_Employee_Deduction
            this.copyEmployeeFormDetail(originalId);
        }
        catch (SQLException e)
        {
            log.log(Level.SEVERE, "afterCopy", e);
        }
    }
    
    /**
     * Copie les enregistrements de la table P_Employee_Form_Detail
     */
    private void copyEmployeeFormDetail(int originalId) throws SQLException
    {
        // On sort tous les enregistrements à copier de la table
        String sql
        = "select * from P_Employee_Form_Detail "
            + " where P_Employee_Form_ID = " + originalId;
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // On crée un nouvel enregistrement pour la déduction copiée
        	P_Employee_Form_Detail detail = P_Employee_Form_Detail.getEmployee_Form_Detail( this.getP_Employee_Form_ID(), rs.getInt("P_Form_Template_Detail_ID") );
        	detail.setFormFieldAmount( rs.getBigDecimal("FormFieldAmount"));
        	detail.setFormFieldText( rs.getString("FormFieldText"));
            detail.save();
            
        }
        
        rs.close();
        stmt.close();
    }



}	//	P_Employee_Form
