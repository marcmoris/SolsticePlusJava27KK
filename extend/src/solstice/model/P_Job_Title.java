package solstice.model;

import java.awt.Frame;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;


/**
 *  Job_Title Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Job_Title extends X_P_Job_Title
{
	/**
	 * 	Get Job_Title
	 *	@param ctx context
	 * 	@param P_Job_Title_ID id
	 *	@return Job_Title
	 */
	public static P_Job_Title get (Properties ctx, int P_Job_Title_ID, String trxName)
	{
		Integer key = new Integer (P_Job_Title_ID);
		P_Job_Title Job_Title = (P_Job_Title)s_cache.get(key);
		if (Job_Title != null)
			return Job_Title;
		Job_Title = new P_Job_Title (ctx, P_Job_Title_ID, trxName);
		s_cache.put (key, Job_Title);
		return Job_Title;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Job_Title>	s_cache = new CCache<Integer,P_Job_Title>("P_Job_Title", 20);
	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Job_Title.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Job_Title_ID id
	 */
	public P_Job_Title (Properties ctx, int P_Job_Title_ID, String trxName)
	{
		super (ctx, P_Job_Title_ID, trxName);
		if (P_Job_Title_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Job_Title

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Job_Title (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Job_Title (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Job_Title_ID"), trxName);
	}	//	P_Job_Title


	/**
	 * Procédure de copie. Lorsqu'on copie un titre d'emploi, on doit
	 * l'intégrer à la comptabilité en alimentant les tables P_Gain_Account,
	 * P_Deduction_Account et P_Credits_Account
	 */
	public void afterCopy(int originalId)
	{
	    try
	    {
			String strMessage = "Voulez-vous copier les informations relative a la comptabilité de ce titre d'emploi ?";
    		JOptionPane optionPane1 = new JOptionPane(strMessage,
                    JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
            optionPane1.createDialog(new Frame(), null).setVisible(true);

            Object result = optionPane1.getValue();

            if(Integer.parseInt(result.toString()) == JOptionPane.YES_OPTION)
            {
    	        // Copie des enregistrements
    	        this.copyGainAccount(originalId);
    	        this.copyDeductionAccount(originalId);
    	        this.copyCreditsAccount(originalId);
            	
            }
	    	
	    }
	    catch (SQLException e)
	    {
	        log.log(Level.SEVERE, "afterCopy", e);
	    }
	}
	
	/**
	 * Copie des enregistrements de la table P_Gain_Account
	 */
	private void copyGainAccount(int originalId) throws SQLException
	{
	    // On récupère les données à copier
	    String sql
	    = "select P_Gain_ID, P_Occupation_Group_ID, IsActive, P_Gain_Acct, C_Activity_ID, P_Job_Type_ID"
	        + " from P_Gain_Account"
	        + " where P_Job_Title_ID = " + originalId;
	    
	    PreparedStatement stmt = DB.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE, this.get_TrxName() );
	    ResultSet rs = stmt.executeQuery();
	    
	    while(rs.next())
	    {
	        // Création des enregistrements
	        P_Gain_Account gainAccount = new P_Gain_Account(Env.getCtx(), -1, this.get_TrxName());
	        gainAccount.setP_Job_Title_ID(this.getP_Job_Title_ID());
	        gainAccount.setP_Gain_ID(rs.getInt("P_Gain_ID"));
	        gainAccount.setP_Occupation_Group_ID(rs.getInt("P_Occupation_Group_ID"));
	        gainAccount.setIsActive(rs.getString("IsActive").equals("Y"));
	        gainAccount.setP_Gain_Acct(rs.getInt("P_Gain_Acct"));
	        gainAccount.setC_Activity_ID(rs.getInt("C_Activity_ID"));
	        gainAccount.setP_Job_Type_ID(rs.getInt("P_Job_Type_ID"));
	        gainAccount.save(this.get_TrxName());
	    }
	    
	    rs.close();
	    stmt.close();
	}

	/**
	 * Copie des enregistrements de la table P_Deduction_Account
	 */
	private void copyDeductionAccount(int originalId) throws SQLException
	{
	    // On récupère les données à copier
	    String sql
	    = "select P_Occupation_Group_ID, IsActive, P_Deduction_ID, P_Deduction_Acct, P_Deduction_Account_ID, P_Job_Type_ID"
	        + " from P_Deduction_Account"
	        + " where P_Job_Title_ID = " + originalId;
	    
	    PreparedStatement stmt = DB.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE, this.get_TrxName());
	    ResultSet rs = stmt.executeQuery();
	    
	    while(rs.next())
	    {
	        // Création des enregistrements
	        P_Deduction_Account deductionAccount = new P_Deduction_Account(Env.getCtx(), -1, this.get_TrxName());
	        deductionAccount.setP_Job_Title_ID(this.getP_Job_Title_ID());
	        deductionAccount.setP_Occupation_Group_ID(rs.getInt("P_Occupation_Group_ID"));
	        deductionAccount.setIsActive(rs.getString("IsActive").equals("Y"));
	        deductionAccount.setP_Deduction_ID(rs.getInt("P_Deduction_ID"));
	        deductionAccount.setP_Deduction_Acct(rs.getInt("P_Deduction_Acct"));
	        deductionAccount.setP_Deduction_Account_ID(rs.getInt("P_Deduction_Account_ID"));
	        deductionAccount.setP_Job_Type_ID(rs.getInt("P_Job_Type_ID"));
	        deductionAccount.save(this.get_TrxName());
	    }
	    
	    rs.close();
	    stmt.close();
	}

	/**
	 * Copie des enregistrements de la table P_Credits_Account
	 */
	private void copyCreditsAccount(int originalId) throws SQLException
	{
	    // On récupère les données à copier
	    String sql
	    = "select P_Occupation_Group_ID, IsActive, P_Credits_ID, P_Credits_Acct, P_Job_Type_ID"
	        + " from P_Credits_Account"
	        + " where P_Job_Title_ID = " + originalId;
	    
	    PreparedStatement stmt = DB.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE, this.get_TrxName());
	    ResultSet rs = stmt.executeQuery();
	    
	    while(rs.next())
	    {
	        // Création des enregistrements
	        P_Credits_Account creditsAccount = new P_Credits_Account(Env.getCtx(), -1, this.get_TrxName());
	        creditsAccount.setP_Job_Title_ID(this.getP_Job_Title_ID());
	        creditsAccount.setP_Occupation_Group_ID(rs.getInt("P_Occupation_Group_ID"));
	        creditsAccount.setIsActive(rs.getString("IsActive").equals("Y"));
	        creditsAccount.setP_Credits_ID(rs.getInt("P_Credits_ID"));
	        creditsAccount.setP_Credits_Acct(rs.getInt("P_Credits_Acct"));
	        creditsAccount.setP_Job_Type_ID(rs.getInt("P_Job_Type_ID"));
	        creditsAccount.save(this.get_TrxName());
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


}	//	P_Job_Title
