/*
 * Created on 2004-11-17
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author marmor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
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
 *  P_Deduction_Param Model
 *
 *  @author Progestion Informatique
 *  @version $Id: P_Deduction_Param.java,v 1.1 2007/07/18 14:43:28 marmor01 Exp $
 */

public class P_Deduction_Param extends X_P_Deduction_Param
{
	/**	Cache						*/
	private static CCache<Integer,P_Deduction_Param>	s_cache = new CCache<Integer,P_Deduction_Param>("P_Deduction_Param", 20);

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Deduction_Param.class);
	
	private String				m_trxName = null;


	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Employee_Deduction_ID id
	 */
	public P_Deduction_Param (Properties ctx, int P_Deduction_Param_ID, String trxName)
	{
		super (ctx, P_Deduction_Param_ID, trxName);
		m_trxName = trxName;
	}	//	P_Employee_Deduction

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Deduction_Param (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
		m_trxName = trxName;
	}
	

	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Deduction_Param (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Deduction_Param_ID"), trxName);
		m_trxName = trxName;
	}	//	P_Deduction_Param

	/**
	 * 	Get
	 * 	@param 
	 */
	
	public static P_Deduction_Param get (Properties ctx, int P_Deduction_Param_ID, String trxName )
	{
		Integer key = new Integer (P_Deduction_Param_ID);
		P_Deduction_Param deductionParam = (P_Deduction_Param)s_cache.get(key);
		if (deductionParam != null)
			return deductionParam;
		deductionParam = new P_Deduction_Param (ctx, P_Deduction_Param_ID, trxName);
		s_cache.put (key, deductionParam);
		if(deductionParam != null)
		{
			deductionParam.setTrxName(trxName);
		}
		return deductionParam;

	}	//	get

	public static P_Deduction_Param get ( Properties ctx, int Deduction_ID, Timestamp EffectIn, String trxName )
	{
		int Deduction_Param_ID = 0;
		String sql = null;
		sql = "Select P_Deduction_Param_ID From P_Deduction_Param ";
		sql +="  Where P_Deduction_Param.IsActive = 'Y' ";
		sql +="    and P_Deduction_ID = " + Deduction_ID;
		sql +="    and EffectIn<= " + DB.TO_DATE( EffectIn )  ;
		sql +="  Order By EffectIn Desc";

		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				Deduction_Param_ID = rs.getInt(1);
			else
				Deduction_Param_ID = 0;
				
			rs.close ();
			pstmt.close ();
			pstmt = null;
						
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"* Error * P_Deduction_Param - get - " + sql + " - " ,e);
			return null;
		}


		if ( Deduction_Param_ID == 0 )
		{
			return null;
		}

		return P_Deduction_Param.get( ctx, Deduction_Param_ID, trxName );

	}	//	get

	
	public static void clear()
	{
		s_cache.clear();
	}

	/**
	 * 	After Save
	 *	@param newRecord
	 *	@param success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
	    if (!success )
		{
			return success;
		}
	    //on va chercher l'objet precedent pour mettre sa date de fin a jour
	    String sql = "SELECT P_Deduction_Param_ID "
	    	+ " FROM P_Deduction_Param "
	    	+ " WHERE P_Deduction_ID = " + this.getP_Deduction_ID()
	    	+ " AND P_Deduction_Param_ID <> " + this.getP_Deduction_Param_ID()
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
		    	P_Deduction_Param deduction_Param = P_Deduction_Param.get(Env.getCtx(), rs.getInt(1), m_trxName);
		    	if(deduction_Param == null || deduction_Param.getP_Deduction_Param_ID() <= 0)
		    	{
		    		return success;
		    	}
		    	Timestamp tsDate = this.getEffectIn();
		    	// - 1 pour la veille
		    	tsDate = TimeUtil.addDays( tsDate, -1);
		    	deduction_Param.setEffectTo(tsDate);
		    	deduction_Param.save();
		    }
	    }
	    catch(SQLException e)
	    {
	    	s_log.log(Level.SEVERE,"P_Assignment_SpecialRate - afterSave - " + sql, e);	    	
	    }
		return success;
	}	//	afterSave
	

	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		P_Deduction deduction = P_Deduction.get( Env.getCtx(), this.getP_Deduction_ID(), this.get_TrxName());
		StringBuffer sb = new StringBuffer ("P_Deduction_Param[ID=")
			.append(this.getP_Deduction_Param_ID())
			.append(",Value=").append(deduction.getValue())
			.append(",Name=").append(deduction.getName())
			.append(",Date=").append(this.getEffectIn())
			.append ("]");
		return sb.toString ();
	}	//	toString

	private void setTrxName ( String trxName)
	{
		m_trxName = trxName;
	}

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
            // On copie les enregistrements de P_Deduction_Insurance et de P_Deduction_Param_Ympe
            // qui sont sous ce paramètre de déduction
            this.copyDeductionInsurance(originalId, this.getP_Deduction_Param_ID());
            this.copyDeductionParamYmpe(originalId, this.getP_Deduction_Param_ID());
        }
        catch (SQLException e)
        {
            log.log(Level.SEVERE, "afterCopy", e);
        }
    }
    
    
    /**
     * Copie les enregistrements de la table P_Deduction_Insurance
     */
    private void copyDeductionInsurance(int originalDeductionParamId, int deductionParamId) throws SQLException
    {
        // On sort tous les enregistrements à copier de la table
        String sql
        = "select P_Deduction_Insurance_ID, IsActive, InsuranceParticipant, InsuranceTypeRate, InsuranceLayer"
            + " from P_Deduction_Insurance"
            + " where P_Deduction_Param_ID = " + originalDeductionParamId;
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // On crée un nouvel enregistrement pour la déduction copiée
            P_Deduction_Insurance deductionInsurance = new P_Deduction_Insurance(Env.getCtx(), -1, null);
            deductionInsurance.setP_Deduction_Param_ID(deductionParamId);
            deductionInsurance.setIsActive(rs.getString("IsActive").equals("Y"));
            deductionInsurance.setInsuranceParticipant(rs.getString("InsuranceParticipant"));
            deductionInsurance.setInsuranceTypeRate(rs.getString("InsuranceTypeRate"));
            deductionInsurance.setInsuranceLayer(rs.getInt("InsuranceLayer"));
            deductionInsurance.save();
            
            // On copie les enregistrements de P_Deduction_Insurance_Bound
            // qui sont sous cet enregistrement
            this.copyDeductionInsuranceBound(rs.getInt("P_Deduction_Insurance_ID"), deductionInsurance.getP_Deduction_Insurance_ID());
        }
        
        rs.close();
        stmt.close();
    }
    
    /**
     * Copie les enregistrements de la table P_Deduction_Insurance
     */
    private void copyDeductionInsuranceBound(int originalDeductionInsuranceId, int deductionInsuranceId) throws SQLException
    {
        // On sort tous les enregistrements à copier de la table
        String sql
        = "select IsActive, SmokerFemal, NoSmokerFemal, SmokerMale, NoSmokerMale, AgeLimitMax, AgeLimitMin"
            + " from P_Deduction_Insurance_Bound"
            + " where P_Deduction_Insurance_ID = " + originalDeductionInsuranceId;
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // On crée un nouvel enregistrement pour la déduction copiée
            P_Deduction_Insurance_Bound deductionInsuranceBound = new P_Deduction_Insurance_Bound(Env.getCtx(), -1, null);
            deductionInsuranceBound.setP_Deduction_Insurance_ID(deductionInsuranceId);
            deductionInsuranceBound.setIsActive(rs.getString("IsActive").equals("Y"));
            deductionInsuranceBound.setSmokerFemal(rs.getBigDecimal("SmokerFemal"));
            deductionInsuranceBound.setNoSmokerFemal(rs.getBigDecimal("NoSmokerFemal"));
            deductionInsuranceBound.setSmokerMale(rs.getBigDecimal("SmokerMale"));
            deductionInsuranceBound.setNoSmokerMale(rs.getBigDecimal("NoSmokerMale"));
            deductionInsuranceBound.setAgeLimitMax(rs.getInt("AgeLimitMax"));
            deductionInsuranceBound.setAgeLimitMin(rs.getInt("AgeLimitMin"));
            deductionInsuranceBound.save();
        }
        
        rs.close();
        stmt.close();
    }
    
    /**
     * Copie les enregistrements de la table P_Deduction_Param_Ympe
     */
    private void copyDeductionParamYmpe(int originalDeductionParamId, int deductionParamId) throws SQLException
    {
        // On sort tous les enregistrements à copier de la table
        String sql
        = "select IsActive, YmpeMin, YmpeRate"
            + " from P_Deduction_Param_Ympe"
            + " where P_Deduction_Param_ID = " + originalDeductionParamId;
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // On crée un nouvel enregistrement pour la déduction copiée
            P_Deduction_Param_Ympe deductionParamYmpe = new P_Deduction_Param_Ympe(Env.getCtx(), -1, null);
            deductionParamYmpe.setP_Deduction_Param_ID(deductionParamId);
            deductionParamYmpe.setIsActive(rs.getString("IsActive").equals("Y"));
            deductionParamYmpe.setYmpeMin(rs.getInt("YmpeMin"));
            deductionParamYmpe.setYmpeRate(rs.getBigDecimal("YmpeRate"));
            deductionParamYmpe.save();
        }
        
        rs.close();
        stmt.close();
    }
    	
	
}	//	P_Deduction_Param
