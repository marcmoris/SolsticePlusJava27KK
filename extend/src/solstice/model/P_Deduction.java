package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MRole;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Language;

/**
 *  Deduction Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Deduction extends X_P_Deduction
{
	/**
	 * 	Get Deduction
	 *	@param ctx context
	 * 	@param P_Deduction_ID id
	 *	@return Deduction
	 */
	public static P_Deduction get (Properties ctx, int P_Deduction_ID, String trxName)
	{
		Integer key = new Integer (P_Deduction_ID);
		P_Deduction Deduction = (P_Deduction)s_cache.get(key);
		if (Deduction != null)
			return Deduction;
		Deduction = new P_Deduction (ctx, P_Deduction_ID, trxName);
		s_cache.put (key, Deduction);
		return Deduction;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Deduction>	s_cache = new CCache<Integer,P_Deduction>("P_Deduction", 20);
	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Deduction.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Deduction_ID id
	 */
	public P_Deduction (Properties ctx, int P_Deduction_ID, String trxName)
	{
		super (ctx, P_Deduction_ID, trxName);
		if (P_Deduction_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Deduction

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Deduction (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Deduction (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Deduction_ID"), trxName);
	}	//	P_Deduction

	//
	// Get Deduction for Advance Recovery
	//
	public static P_Deduction getDeductionRecovery (Properties ctx, int P_Advance_ID, String trxName)
	{
	    String sql = "SELECT  P_Deduction_ID FROM P_Deduction_Param "
            + " WHERE P_Advance_ID   = " + P_Advance_ID;
	    
	    PreparedStatement pstmt = null;
    
	    int P_Deduction_ID = -1;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
			    P_Deduction_ID = rs.getInt("P_Deduction_ID");
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			
		}
		catch (Exception e)
		{
		    System.out.println( "P_Deduction, getDeductionRecovery - " + e);
		    return null;
		}
	    
		if ( P_Deduction_ID == -1 )
			return null;
		
		P_Deduction Deduction = P_Deduction.get( ctx, P_Deduction_ID, trxName );
		return Deduction;
	}	//	get

	/**
	 * 	Load all record - for Performace.
	 *	@param ctx context
	 */
	public static void loadAll (Properties ctx)
	{
		//
		s_cache = new CCache<Integer,P_Deduction>("P_Deduction", 100);
		String sql = "SELECT P_Deduction_ID FROM P_Deduction WHERE IsActive='Y'";
		sql = MRole.getDefault().addAccessSQL (sql, "P_Deduction", true, false);	// fully qualidfied - RO 

		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				P_Deduction deduction = new P_Deduction (ctx, rs.getInt("P_Deduction_ID"), null);
				s_cache.put( deduction.getP_Deduction_ID(), deduction);
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
	}	//	loadAll

	
	/**
	 * 	After Save.
	 * 	Insert
	 * 	- create tree
	 *	@param newRecord insert
	 *	@param success save success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
/*		if (newRecord)
			insert_Tree(MTree_Base.TREETYPE_Deduction);
*/			
		return true;
	}	//	afterSave
	
	/**
	 * 	After Delete
	 *	@param success
	 *	@return deleted
	 */
	protected boolean afterDelete (boolean success)
	{
/*		if (success)
			delete_Tree(MTree_Base.TREETYPE_Deduction);
*/
		return success;
	}	//	afterDelete

	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Deduction[ID=")
			.append(this.getP_Deduction_ID())
			.append(",Value=").append(getValue())
			.append(",Name=").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString

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
            this.copyDeductionParam(originalId);
            this.copyDeductionAccount(originalId);
        }
        catch (SQLException e)
        {
            log.log(Level.SEVERE, "afterCopy", e);
        }
    }
    
    /**
     * Copie les enregistrements de la table P_Deduction_Param
     */
    private void copyDeductionParam(int originalId) throws SQLException
    {
        // On sort tous les enregistrements à copier de la table
        String sql
        = "select P_Deduction_Param_ID, IsActive, P_Column_Adm_ID, "
            + " P_Method_Deduction_ID, P_Advance_Id, EmployeeAmount, "
            + " EmployerAmount, EmployerRate, P_Employer_ID, EmployeeRate, "
            + " MaxEmployer, SalaryType, Method_Rounding, EffectIn, "
            + " AnnualMaximum, MinimumAmountToWithhold, MaximumAmountToWithhold, "
            + " MaximumBackPaymentAccumulated, MaximumBackPayment, ExemptionAmount, "
            + " TaxeRate, P_RWT_ID, MaximumAllowableEarnings, AdministrationFee, "
            + " MaximumNumberPeriod, NonAnnualMaximum"
            + " from P_Deduction_Param"
            + " where P_Deduction_ID = " + originalId;
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // On crée un nouvel enregistrement pour la déduction copiée
            P_Deduction_Param deductionParam = new P_Deduction_Param(Env.getCtx(), -1, null);
            deductionParam.setP_Deduction_ID(this.getP_Deduction_ID());
            deductionParam.setIsActive(rs.getString("IsActive").equals("Y"));
            deductionParam.setP_Column_Adm_ID(rs.getInt("P_Column_Adm_ID"));
//            deductionParam.setP_Deduction_Family_ID(rs.getInt("P_Deduction_Family_ID"));
            deductionParam.setP_Method_Deduction_ID(rs.getInt("P_Method_Deduction_ID"));
            deductionParam.setP_Advance_ID(rs.getInt("P_Advance_Id"));
            deductionParam.setEmployeeAmount(rs.getBigDecimal("EmployeeAmount"));
            deductionParam.setEmployerAmount(rs.getBigDecimal("EmployerAmount"));
            deductionParam.setEmployerRate(rs.getBigDecimal("EmployerRate"));
            deductionParam.setP_Employer_ID(rs.getInt("P_Employer_ID"));
            deductionParam.setEmployeeRate(rs.getBigDecimal("EmployeeRate"));
            deductionParam.setMaxEmployer(rs.getBigDecimal("MaxEmployer"));
            deductionParam.setSalaryType(rs.getString("SalaryType"));
           	deductionParam.setMethod_Rounding(rs.getString("Method_Rounding"));
            	
            deductionParam.setEffectIn(rs.getTimestamp("EffectIn"));
            deductionParam.setAnnualMaximum(rs.getBigDecimal("AnnualMaximum"));
            deductionParam.setMinimumAmountToWithhold(rs.getBigDecimal("MinimumAmountToWithhold"));
            deductionParam.setMaximumAmountToWithhold(rs.getBigDecimal("MaximumAmountToWithhold"));
            deductionParam.setMaximumBackpaymentAccumulated(rs.getBigDecimal("MaximumBackPaymentAccumulated"));
            deductionParam.setMaximumBackpayment(rs.getBigDecimal("MaximumBackPayment"));
            deductionParam.setExemptionAmount(rs.getBigDecimal("ExemptionAmount"));
            deductionParam.setTaxeRate(rs.getBigDecimal("TaxeRate"));
            deductionParam.setP_RWT_ID(rs.getInt("P_RWT_ID"));
            deductionParam.setMaximumAllowableEarnings(rs.getBigDecimal("MaximumAllowableEarnings"));
            deductionParam.setAdministrationFee(rs.getBigDecimal("AdministrationFee"));
            deductionParam.setMaximumNumberPeriod(rs.getInt("MaximumNumberPeriod"));
            deductionParam.setNonAnnualMaximum(rs.getBigDecimal("NonAnnualMaximum"));
            deductionParam.save();
            
            // On copie les enregistrements de P_Deduction_Insurance et de P_Deduction_Param_Ympe
            // qui sont sous ce paramètre de déduction
            this.copyDeductionInsurance(rs.getInt("P_Deduction_Param_ID"), deductionParam.getP_Deduction_Param_ID());
            this.copyDeductionParamYmpe(rs.getInt("P_Deduction_Param_ID"), deductionParam.getP_Deduction_Param_ID());
        }
        
        rs.close();
        stmt.close();
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
    
    /**
     * Copie les enregistrements de la table P_Deduction_Account
     */
    private void copyDeductionAccount(int originalId) throws SQLException
    {
        // On sort tous les enregistrements à copier de la table
        String sql
        = "select P_Occupation_Group_ID, IsActive, P_Deduction_Acct, P_Job_Type_ID, P_Job_Title_ID,"
        	+ " C_Activity_ID, C_Activity_Src_ID, C_SalesRegion_ID, P_Deduction_Account_ID, AD_Org_ID"
            + " from P_Deduction_Account"
            + " where P_Deduction_ID = " + originalId;
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // On crée un nouvel enregistrement pour la déduction copiée
            P_Deduction_Account deductionAccount = new P_Deduction_Account(Env.getCtx(), -1, null);
            deductionAccount.setP_Deduction_ID(this.getP_Deduction_ID());
            if ( rs.getInt("P_Occupation_Group_ID") != 0)
            	deductionAccount.setP_Occupation_Group_ID(rs.getInt("P_Occupation_Group_ID"));
            deductionAccount.setIsActive(rs.getString("IsActive").equals("Y"));
            deductionAccount.setP_Deduction_Acct(rs.getInt("P_Deduction_Acct"));
            if ( rs.getInt("P_Job_Type_ID") != 0)
            deductionAccount.setP_Job_Type_ID(rs.getInt("P_Job_Type_ID"));
            if ( rs.getInt("P_Job_Title_ID") != 0)
            	deductionAccount.setP_Job_Title_ID(rs.getInt("P_Job_Title_ID"));
            if ( rs.getInt("C_Activity_ID") != 0)
            	deductionAccount.setC_Activity_ID(rs.getInt("C_Activity_ID"));
            if ( rs.getInt("C_Activity_Src_ID") != 0)
            	deductionAccount.setC_Activity_Src_ID(rs.getInt("C_Activity_Src_ID"));
            if ( rs.getInt("C_SalesRegion_ID") != 0)
            	deductionAccount.setC_SalesRegion_ID(rs.getInt("C_SalesRegion_ID"));
            deductionAccount.setP_Deduction_Account_ID(rs.getInt("P_Deduction_Account_ID"));
            deductionAccount.setAD_Org_ID(rs.getInt("AD_Org_ID"));
            deductionAccount.save();
        }
        
        rs.close();
        stmt.close();
    }
   	/**
	 * 	Get Translated Name
	 *	@return name
	 */
	public String getTrlName()
	{
		if (Env.isBaseLanguage(Env.getCtx(), "AD_Ref_List"))
			return getName();
		return get_Translation("Name", Env.getAD_Language(Env.getCtx()));
	}	//	getTrlName
	
	//
	// Return Name with Translation
	// 
	public String getTrlName( String language  )
	{
		Language baseLanguage = Language.getBaseLanguage();
		
		if ( language.equals( baseLanguage.getAD_Language()  ) ||  language.equals( "en_CA") )
			return getName();
		return get_Translation("Name", language );
	}	//	getTrlName


	public void updateTranslation( String language, String name, String description)
	{
		StringBuffer sql = new StringBuffer ("UPDATE P_Deduction_TRL set name = '" + name + "', description='" + description + "' Where P_Deduction_ID = " + this.getP_Deduction_ID() + " AND AD_Language='" + language +"'" );  
		DB.executeUpdate(sql.toString(), null);
	}	//	updateTranslation
	
	
	/**
	 * 	Get Translated Name
	 *	@return name
	 */
	public String getTrlDescription()
	{
		if (Env.isBaseLanguage(Env.getCtx(), "AD_Ref_List"))
			return getDescription();
		return get_Translation("Description", Env.getAD_Language(Env.getCtx()));
	}	//	getTrlDescription

	
	public static P_Deduction getWithValue( Properties ctx, String value, String trxName )
	{
		int id = 0;
		String sql = "Select P_Deduction_ID from P_Deduction Where Value = '" + value + "'";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				id = rs.getInt("P_Deduction_ID");
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("P_Gain.get - " + e);
		}
		
		return P_Deduction.get(ctx, id, trxName);
		
	}

	public static P_Deduction getWithMethod( Properties ctx, int Method, String trxName )
	{
		// P_Method_Deduction_ID=103
		int id = 0;
		String sql = "Select P_Deduction.P_Deduction_ID from P_Deduction"
				   + " INNER JOIN P_Deduction_Param ON P_Deduction_Param.P_Deduction_ID = P_Deduction.P_Deduction_ID "
				+ "  Where P_Deduction.isActive = 'Y' AND P_Method_Deduction_ID  = " + Method ;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				id = rs.getInt("P_Deduction_ID");
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("P_Gain.get - " + e);
		}
		
		return P_Deduction.get(ctx, id, trxName);
		
	}

	/** Deduction_Ap_Acct_360 AD_Reference_ID=132 */
	public static final int DEDUCTION_AP_ACCT_360_AD_Reference_ID=132;
	/** Set AP Account 360.
	@param DEDUCTION_AP_ACCT_360 AP Account 360 */
	public void setDeduction_AP_Acct_360 (int Deduction_Ap_Acct_360)
	{
	set_Value ("Deduction_AP_Acct_360", new Integer(Deduction_Ap_Acct_360));
	}
	/** Get AP Account 360.
	@return AP Account 360 */
	public int getDeduction_AP_Acct_360() 
	{
	Integer ii = (Integer)get_Value("Deduction_AP_Acct_360");
	if (ii == null) return 0;
	return ii.intValue();
	}


	
}	//	P_Deduction
