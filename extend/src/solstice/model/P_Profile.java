package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  Profile Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Profile extends X_P_Profile
{
	/**
	 * 	Get Profile
	 *	@param ctx context
	 * 	@param P_Profile_ID id
	 *	@return Profile
	 */
	public static P_Profile get (Properties ctx, int P_Profile_ID, String trxName)
	{
		Integer key = new Integer (P_Profile_ID);
		P_Profile Profile = (P_Profile)s_cache.get(key);
		if (Profile != null)
			return Profile;
		Profile = new P_Profile (ctx, P_Profile_ID, trxName);
		s_cache.put (key, Profile);
		return Profile;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Profile>	s_cache = new CCache<Integer,P_Profile>("P_Profile", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Profile_ID id
	 */
	public P_Profile (Properties ctx, int P_Profile_ID, String trxName)
	{
		super (ctx, P_Profile_ID, trxName);
		if (P_Profile_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Profile

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Profile (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Profile (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Profile_ID"), trxName);
	}	//	P_Profile

	   /**
     * Procédure de copie. Lorsqu'on copie un code de regroupement, on doit également
     * copier tous les paramètres et le contenue des onglets sous le gain.
     */
    public void afterCopy(int originalId)
    {
        try
        {
            this.copyCredits(originalId);
            this.copyDeduction(originalId);
            this.copyTaxableBenefit(originalId);
            this.copyBonus(originalId);
        }
        catch (SQLException e)
        {
            log.log(Level.SEVERE, "afterCopy", e);
        }
    }

    /**
     * Copie de la table P_Gain_Credits
     */
    private void copyCredits(int originalId) throws SQLException
    {
        // On sort tous les enregistrements à copier de la table
        String sql = "select * from P_Profile_Credits "
                   + " where isActive = 'Y' AND  P_Profile_ID = " + originalId;
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // On crée un nouvel enregistrement pour le gain copié
            P_Profile_Credits tab = new P_Profile_Credits(Env.getCtx(), -1, this.get_TrxName());
            tab.setP_Credits_ID( rs.getInt("P_Credits_ID") );
            tab.setP_Method_Credits_ID( rs.getInt("P_Method_Credits_ID") );
            tab.setP_Profile_ID( this.getP_Profile_ID() );
            tab.setEffectIn( rs.getTimestamp("EffectIn") );
            tab.setEffectTo( rs.getTimestamp("EffectTo") );
            tab.setIsActive( rs.getString("IsActive").equals("Y"));
            tab.save();
        }
        
        rs.close();
        stmt.close();
    }
    
    private void copyDeduction(int originalId) throws SQLException
    {
        // On sort tous les enregistrements à copier de la table
        String sql = "select * from P_Profile_Deduction "
                   + " where isActive = 'Y' AND  P_Profile_ID = " + originalId;
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // On crée un nouvel enregistrement pour le gain copié
            P_Profile_Deduction tab = new P_Profile_Deduction(Env.getCtx(), -1, this.get_TrxName());
            tab.setP_Deduction_ID( rs.getInt("P_Deduction_ID") );
            tab.setP_Profile_ID( this.getP_Profile_ID() );
            tab.setEffectIn( rs.getTimestamp("EffectIn") );
            tab.setEffectTo( rs.getTimestamp("EffectTo") );
            tab.setIsActive( rs.getString("IsActive").equals("Y"));
            tab.save();
        }
        
        rs.close();
        stmt.close();
    }

    private void copyBonus(int originalId) throws SQLException
    {
        // On sort tous les enregistrements à copier de la table
        String sql = "select * from P_Profile_Bonus "
                   + " where isActive = 'Y' AND P_Profile_ID = " + originalId;
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // On crée un nouvel enregistrement pour le gain copié
            P_Profile_Bonus tab = new P_Profile_Bonus(Env.getCtx(), -1, this.get_TrxName());
            tab.setP_Bonus_ID( rs.getInt("P_Bonus_ID") );
            tab.setP_Profile_ID( this.getP_Profile_ID() );
            tab.setEffectIn( rs.getTimestamp("EffectIn") );
            tab.setEffectTo( rs.getTimestamp("EffectTo") );
            tab.setIsActive( rs.getString("IsActive").equals("Y"));
            tab.save();
        }
        
        rs.close();
        stmt.close();
    }

    private void copyTaxableBenefit(int originalId) throws SQLException
    {
        // On sort tous les enregistrements à copier de la table
        String sql = "select * from P_Profile_Taxable_Benefit "
                   + " where isActive = 'Y' AND P_Profile_ID = " + originalId;
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // On crée un nouvel enregistrement pour le gain copié
            P_Profile_Taxable_Benefit tab = new P_Profile_Taxable_Benefit(Env.getCtx(), -1, this.get_TrxName());
            tab.setP_Taxable_Benefit_ID( rs.getInt("P_Taxable_Benefit_ID") );
            tab.setP_Profile_ID( this.getP_Profile_ID() );
            tab.setEffectIn( rs.getTimestamp("EffectIn") );
            tab.setEffectTo( rs.getTimestamp("EffectTo") );
            tab.setIsActive( rs.getString("IsActive").equals("Y"));
            tab.save();
        }
        
        rs.close();
        stmt.close();
    }


}	//	P_Profile
