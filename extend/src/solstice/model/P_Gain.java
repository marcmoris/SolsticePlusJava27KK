package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MRole;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Language;

import java.sql.*;

/**
 * Gain Model
 * 
 * @author Marc Morissette - Progestion Inf
 */
public class P_Gain extends X_P_Gain
{
    /**
     * Get Gain
     * 
     * @param ctx
     *            context
     * @param P_Gain_ID
     *            id
     * @return Gain
     */
    public static P_Gain get(Properties ctx, int P_Gain_ID, String trxName)
    {
        Integer key = new Integer(P_Gain_ID);
        P_Gain Gain = (P_Gain) s_cache.get(key);
        if (Gain != null)
            return Gain;
        Gain = new P_Gain(ctx, P_Gain_ID, trxName);
        s_cache.put(key, Gain);
        return Gain;
    } //	get

    /** Cache */
	private static CCache<Integer,P_Gain>	s_cache = new CCache<Integer,P_Gain>("P_Gain", 20);
	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Gain.class);

	static boolean isCopyAction = false;

    /***************************************************************************
     * Standard Constructor
     * 
     * @param ctx
     *            context
     * @param P_Gain_ID
     *            id
     */
    public P_Gain(Properties ctx, int P_Gain_ID, String trxName)
    {
        super(ctx, P_Gain_ID, trxName);
        if (P_Gain_ID == 0)
        {
            setAD_Org_ID(0);
        }
    } //	P_Gain

    /**
     * Load Constructor
     * 
     * @param ctx
     *            context
     * @param rs
     *            result set
     */
    public P_Gain(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
    }

    /**
     * Simplified Constructor
     * 
     * @param ctx
     *            context
     */
    public P_Gain(Properties ctx, String trxName)
    {
        this(ctx, Env.getContextAsInt(ctx, "#P_Gain_ID"), trxName);
    } //	P_Gain

    
	/**
	 * 	Load all record - for Performace.
	 *	@param ctx context
	 */
	public static void loadAll (Properties ctx)
	{
		//
		s_cache = new CCache<Integer,P_Gain>("P_Gain", 250);
		String sql = "SELECT P_Gain_ID FROM P_Gain WHERE IsActive='Y'";
		sql = MRole.getDefault().addAccessSQL (sql, "P_Gain", true, false);	// fully qualidfied - RO 

		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				P_Gain gain = new P_Gain (ctx, rs.getInt("P_Gain_ID"), null);
				s_cache.put( gain.getP_Gain_ID(), gain);
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
     * Before Delete
     * 
     * @return true if it can be deleted
     */
    protected boolean beforeDelete()
    {
        return true;
    }

    /**
     * Before Save
     * 
     * @param newRecord
     *            new
     * @return true or false
     */

    protected boolean beforeSave(boolean newRecord)
    {

        return true;
    } //	beforeSave

    /**
     * After Save
     * 
     * @param newRecord
     *            new
     * @param success
     *            success
     */
    protected boolean afterSave(boolean newRecord, boolean success)
    {

        if (!success)
        {
            return success;
        }

        if (newRecord)
        {
  //          insert_Tree(MTree_Base.TREETYPE_Gain);
        }


        // *** Create records for Complementary Information

        if ( this.isSummary())
        {
            DB.executeUpdate("delete from P_Gain_GainInfo WHERE P_Gain_ID = " + this.getP_Gain_ID() + " and To_Consider = 'N'" , null);
            return success;
        }
        
        if ( isCopyAction == false)
        {
            String sql = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;

            sql = " SELECT P_GainInfo_ID " + " FROM P_GainInfo "
                    + " WHERE IsActive = 'Y' " + "   AND AD_Client_ID = " + getAD_Client_ID()
                    + " AND NOT Exists ( Select 1 FROM P_Gain_GainInfo WHERE P_Gain_GainInfo.P_Gain_ID = " + this.getP_Gain_ID()
                    +   "AND P_Gain_GainInfo.P_GainInfo_ID = P_GainInfo.P_GainInfo_ID ) "     
                    ;
            //		        + " AND col.AD_Org_ID = " + getAD_Org_ID() ;

            try
            {
                pstmt = DB.prepareStatement(sql, null);
                rs = pstmt.executeQuery();
                while (rs.next())
                {
                    X_P_Gain_GainInfo Gain_GainInfo = new X_P_Gain_GainInfo(Env.getCtx(), -1, this.get_TrxName());
                    Gain_GainInfo.setP_Gain_ID(this.getP_Gain_ID());
                    Gain_GainInfo.setP_GainInfo_ID(rs.getInt(1));
                    Gain_GainInfo.setTo_Consider(false);
                    Gain_GainInfo.save();
                }

                rs.close();
                pstmt.close();
                pstmt = null;
            }
            catch (Exception e)
            {
                System.out.println("P_Gain - afterSave - " + sql + " - " + e);
            }
        	
        }

            // ************************************************
        /*
         * P_Gain_Hst gain_Hst = P_Gain_Hst.get( Env.getCtx(),
         * this.getP_Gain_ID(), this.getEffectIn(), this.get_TrxName());
         * 
         * gain_Hst.setBonus_Amount(this.getBonus_Amount());
         * gain_Hst.setColor(this.getColor());
         * gain_Hst.setDescription(this.getDescription());
         * gain_Hst.setEffectIn(this.getEffectIn());
         * gain_Hst.setFixed_Amount(this.getFixed_Amount());
         * gain_Hst.setGainUnit(this.getGainUnit());
         * gain_Hst.setIsAbsence(this.isAbsence());
         * gain_Hst.setIsActive(this.isActive());
         * gain_Hst.setIsProductive(this.isProductive());
         * gain_Hst.setIsSummary(this.isSummary());
         * gain_Hst.setName(this.getName());
         * gain_Hst.setP_Advance_ID(this.getP_Advance_ID());
         * gain_Hst.setP_Column_Adm_ID(this.getP_Column_Adm_ID());
         * gain_Hst.setP_Credits_ID(this.getP_Credits_ID());
         * gain_Hst.setP_Gain_Family_ID(this.getP_Gain_Family_ID());
         * gain_Hst.setP_Gain_ID(this.getP_Gain_ID());
         * gain_Hst.setP_Method_Gain_ID(this.getP_Method_Gain_ID());
         * gain_Hst.setP_UOM_ID(this.getP_UOM_ID());
         * gain_Hst.setType_Rate(this.getType_Rate());
         * gain_Hst.setValue(this.getValue());
         * gain_Hst.setAccessible(this.isAccessible());
         * gain_Hst.setVisible(this.isVisible());
         * gain_Hst.setP_Gain_Group_ID(this.getP_Gain_Group_ID());
         * gain_Hst.setP_Gain_Category_ID(this.getP_Gain_Category_ID());
         * 
         * gain_Hst.save();
         */

        return true;
    } //	afterSave

    /**
     * After Delete
     * 
     * @param success
     * @return deleted
     */
    protected boolean afterDelete(boolean success)
    {
        
/*        if (success) 
           delete_Tree(MTree_Base.TREETYPE_Gain);
*/        
        return success;
    } //	afterDelete

    /***************************************************************************
     * String Representation
     * 
     * @return info
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer("P_Gain[ID=").append(this.getP_Gain_ID())
                .append(",Value=").append(getValue()).append(",Name=").append(
                        getName()).append("]");
        return sb.toString();
    } //	toString
    
    /**
     * Procédure de copie. Lorsqu'on copie un code de regroupement, on doit également
     * copier tous les paramètres et le contenue des onglets sous le gain.
     */
    public void afterCopy(int originalId)
    {
    	isCopyAction = true;
        try
        {
            // On copie les tables P_Gain_Parameter, P_Gain_Credits, P_Gain_Bound, P_Gain_Account,
            // P_Gain_Join et P_Gain_Column. On doit également mettre à jour la table P_Gain_GainInfo.
            this.copyGainParameter(originalId);
            this.copyGainBound(originalId);
            this.copyGainAccount(originalId);
            this.copyGainJoin(originalId);
            this.updateGainGainInfo(originalId);
            this.copyGainAccount360(originalId);
        }
        catch (Exception e)
        {
            log.log(Level.SEVERE, "afterCopy", e);
        }
    	isCopyAction = false;

    }
    
    /**
     * Copie de la table P_Gain_Parameter
     */
    private void copyGainParameter(int originalId) throws SQLException
    {
        // On sort tous les enregistrements à copier de la table
        String sql
        = "select P_Gain_Parameter_ID, IsActive, P_Collective_Labour_Agr_ID,"
            + " MultiplyRate, Fixed_Amount, Bonus_Amount"
            + " from P_Gain_Parameter"
            + " where P_Gain_ID = " + originalId;
        
        PreparedStatement stmt = DB.prepareStatement(sql, this.get_TrxName());
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // On crée un nouvel enregistrement pour le gain copié
            P_Gain_Parameter gainParameter = new P_Gain_Parameter(Env.getCtx(), -1, this.get_TrxName());
            gainParameter.setP_Gain_ID(this.getP_Gain_ID());
            gainParameter.setIsActive(rs.getString("IsActive").equals("Y"));
            gainParameter.setP_Collective_Labour_Agr_ID(rs.getInt("P_Collective_Labour_Agr_ID"));
            gainParameter.setMultiplyRate(rs.getBigDecimal("MultiplyRate"));
            gainParameter.setFixed_Amount(rs.getBigDecimal("Fixed_Amount"));
            gainParameter.setBonus_Amount(rs.getBigDecimal("Bonus_Amount"));
            
            gainParameter.setCopyAction( true );
            
            gainParameter.save();


            // On copie les enregistrements de P_Gain_Credits et de P_Gain_Column
            // qui sont sous ce paramètre de rémunération
            this.copyGainCredits(rs.getInt("P_Gain_Parameter_ID"), gainParameter.getP_Gain_Parameter_ID());
            this.copyGainColumn(rs.getInt("P_Gain_Parameter_ID"), gainParameter.getP_Gain_Parameter_ID());
        }
        
        rs.close();
        stmt.close();
    }

    /**
     * Copie de la table P_Gain_Credits
     */
    private void copyGainCredits(int originalGainParameterId, int gainParameterId) throws SQLException
    {
        // On sort tous les enregistrements à copier de la table
        String sql
        = "select IsActive, Credits_Function, MultiplyRate, P_Credits_ID, P_Collective_Labour_Agr_ID, P_Credits_ID "
            + " from P_Gain_Credits"
            + " where P_Gain_Parameter_ID = " + originalGainParameterId;
        
        PreparedStatement stmt = DB.prepareStatement(sql, this.get_TrxName());
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // On crée un nouvel enregistrement pour le gain copié
            P_Gain_Credits gainCredits = new P_Gain_Credits(Env.getCtx(), -1, this.get_TrxName());
            gainCredits.setP_Gain_Parameter_ID(gainParameterId);
            gainCredits.setIsActive(rs.getString("IsActive").equals("Y"));
            gainCredits.setCredits_Function(rs.getString("Credits_Function"));
            gainCredits.setMultiplyRate(rs.getBigDecimal("MultiplyRate"));
            gainCredits.setP_Credits_ID(rs.getInt("P_Credits_ID"));
            gainCredits.setP_Collective_Labour_Agr_ID(rs.getInt("P_Collective_Labour_Agr_ID"));
            gainCredits.save();
        }
        
        rs.close();
        stmt.close();
    }

    /**
     * Copie de la table P_Gain_Bound
     */
    private void copyGainBound(int originalId) throws SQLException
    {
        // On sort tous les enregistrements à copier de la table
        String sql
        = "select IsActive, P_Credits_ID, LimitMin, LimitMax, P_Job_Type_ID, MultiplyRate"
            + " from P_Gain_Bound"
            + " where P_Gain_ID = " + originalId;
        
        PreparedStatement stmt = DB.prepareStatement(sql, this.get_TrxName());
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // On crée un nouvel enregistrement pour le gain copié
            P_Gain_Bound gainBound = new P_Gain_Bound(Env.getCtx(), -1, this.get_TrxName());
            gainBound.setP_Gain_ID(this.getP_Gain_ID());
            gainBound.setIsActive(rs.getString("IsActive").equals("Y"));
            gainBound.setP_Credits_ID(rs.getInt("P_Credits_ID"));
            gainBound.setLimitMin(rs.getBigDecimal("LimitMin"));
            gainBound.setLimitMax(rs.getBigDecimal("LimitMax"));
            gainBound.setP_Job_Type_ID(rs.getInt("P_Job_Type_ID"));
            gainBound.setMultiplyRate(rs.getBigDecimal("MultiplyRate"));
            gainBound.save();
        }
        
        rs.close();
        stmt.close();
    }

    /**
     * Copie de la table P_Gain_Account
     */
    private void copyGainAccount(int originalId) throws Exception
    {
        // On sort tous les enregistrements à copier de la table
        String sql
        = "select P_Occupation_Group_ID, IsActive, P_Gain_Acct,"
            + "C_Activity_ID, P_Job_Type_ID, P_Job_Title_ID, C_Activity_Src_ID,"
            + " AD_Org_ID, C_SalesRegion_ID"
            + " from P_Gain_Account"
            + " where P_Gain_ID = " + originalId;
        
        PreparedStatement stmt = DB.prepareStatement(sql, this.get_TrxName());
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // On crée un nouvel enregistrement pour le gain copié
            P_Gain_Account gainAccount = new P_Gain_Account(Env.getCtx(), -1, this.get_TrxName());
            gainAccount.setP_Gain_ID(this.getP_Gain_ID());
            gainAccount.setP_Occupation_Group_ID(rs.getInt("P_Occupation_Group_ID"));
            gainAccount.setIsActive(rs.getString("IsActive").equals("Y"));
            gainAccount.setP_Gain_Acct(rs.getInt("P_Gain_Acct"));
            gainAccount.setC_Activity_ID(rs.getInt("C_Activity_ID"));
            gainAccount.setC_Activity_Src_ID(rs.getInt("C_Activity_Src_ID"));
            gainAccount.setP_Job_Type_ID(rs.getInt("P_Job_Type_ID"));
            gainAccount.setP_Job_Title_ID(rs.getInt("P_Job_Title_ID"));
            gainAccount.setAD_Org_ID(rs.getInt("AD_ORG_ID"));
            gainAccount.setC_SalesRegion_ID(rs.getInt("C_SalesRegion_ID"));
            
            gainAccount.save();
        }
        
        rs.close();
        stmt.close();
    }

    private void copyGainAccount360(int originalId) throws Exception
    {
        // On sort tous les enregistrements à copier de la table
        String sql
        = "select  P_Occupation_Group_ID, IsActive, P_Gain_Acct, C_Activity_ID, AD_Org_ID, C_SalesRegion_ID "
            + " from P_Gain_Account_360"
            + " where P_Gain_ID = " + originalId;
        
        PreparedStatement stmt = DB.prepareStatement(sql, this.get_TrxName());
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // On crée un nouvel enregistrement pour le gain copié
            P_Gain_Account_360 gainAccount = new P_Gain_Account_360(Env.getCtx(), -1, this.get_TrxName());
            gainAccount.setP_Gain_ID(this.getP_Gain_ID());
            gainAccount.setP_Occupation_Group_ID(rs.getInt("P_Occupation_Group_ID"));
            gainAccount.setIsActive(rs.getString("IsActive").equals("Y"));
            gainAccount.setP_Gain_Acct(rs.getInt("P_Gain_Acct"));
            gainAccount.setC_Activity_ID(rs.getInt("C_Activity_ID"));
            gainAccount.setAD_Org_ID(rs.getInt("AD_ORG_ID"));
            gainAccount.setC_SalesRegion_ID(rs.getInt("C_SalesRegion_ID"));
            
            gainAccount.save();
        }
        
        rs.close();
        stmt.close();
    }

    /**
     * Copie de la table P_Gain_Join
     */
    private void copyGainJoin(int originalId) throws SQLException
    {
        // On sort tous les enregistrements à copier de la table
        String sql
        = "select IsActive, P_Gain_ID_Affected"
            + " from P_Gain_Join"
            + " where P_Gain_ID = " + originalId;
        
        PreparedStatement stmt = DB.prepareStatement(sql, this.get_TrxName());
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // On crée un nouvel enregistrement pour le gain copié
            P_Gain_Join gainJoin = new P_Gain_Join(Env.getCtx(), -1, this.get_TrxName());
            gainJoin.setP_Gain_ID(this.getP_Gain_ID());
            gainJoin.setIsActive(rs.getString("IsActive").equals("Y"));
            gainJoin.setP_Gain_ID_Affected(rs.getInt("P_Gain_ID_Affected"));
            gainJoin.save();
        }
        
        rs.close();
        stmt.close();
    }

    /**
     * Copie de la table P_Gain_Column
     */
    private void copyGainColumn(int originalGainParameterId, int gainParameterId) throws SQLException
    {
        // On sort tous les enregistrements à copier de la table
        String sql
        = "select P_Column_Adm_ID, IsActive, IsAdmissible"
            + " from P_Gain_Column"
            + " where P_Gain_Parameter_ID = " + originalGainParameterId;
        
        PreparedStatement stmt = DB.prepareStatement(sql, this.get_TrxName());
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // On crée un nouvel enregistrement pour le gain copié
            P_Gain_Column gainColumn = new P_Gain_Column(Env.getCtx(), -1, this.get_TrxName());
            gainColumn.setP_Gain_Parameter_ID(gainParameterId);
            gainColumn.setP_Column_Adm_ID(rs.getInt("P_Column_Adm_ID"));
            gainColumn.setIsActive(rs.getString("IsActive").equals("Y"));
            gainColumn.setIsAdmissible(rs.getBoolean("IsAdmissible"));
            gainColumn.save();
        }
        
        rs.close();
        stmt.close();
    }
    
    private void updateGainGainInfo(int originalId) throws SQLException
    {
        // On sort tous les enregistrements à copier de la table
        String sql
        = "select P_GainInfo_ID, To_Consider"
            + " from P_Gain_GainInfo"
            + " where P_Gain_ID = " + originalId;
        
        PreparedStatement stmt = DB.prepareStatement(sql, this.get_TrxName());
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // On met à jour l'enregistrement correspondant au gain copié
            DB.executeUpdate(
                    "update P_Gain_GainInfo" +
                    " set To_Consider = '" + rs.getString("To_Consider") + "'" +
                    " where P_Gain_ID = " + this.getP_Gain_ID() +
                    " and P_Gain_GainInfo_ID = " + rs.getInt("P_GainInfo_ID"), this.get_TrxName());
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
	

	public void updateTranslation( String language, String name, String description)
	{
		StringBuffer sql = new StringBuffer ("UPDATE P_Gain_TRL set name = '" + name + "', description='" + description + "' Where P_Gain_ID = " + this.getP_Gain_ID() + " AND AD_Language='" + language +"'" );  
		DB.executeUpdate(sql.toString(), null);
	}	//	updateTranslation

	//
	// Return Name with Translation
	// 
	public String getTrlName( String language )
	{
		Language baseLanguage = Language.getBaseLanguage();
		
		if ( language.equals( baseLanguage.getAD_Language()  )||  language.equals( "en_CA") )
			return getName();
		return get_Translation("Name", language );
	}	//	getTrlName
	

	
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

	//
	// is HNR Heure non rémunérer
	public boolean isHNR()
	{
		if ( this.getP_Method_Gain_ID() == 104 )
			return true;
		
		return false;
		
	}

	public static P_Gain getWithValue( Properties ctx, String value, String trxName )
	{
		int id = 0;
		String sql = "Select P_Gain_ID from P_Gain Where Value = '" + value + "'";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				id = rs.getInt("P_Gain_ID");
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("P_Gain.get - " + e);
		}
		
		return P_Gain.get(ctx, id, trxName);
		
	}

	
} //	P_Gain
