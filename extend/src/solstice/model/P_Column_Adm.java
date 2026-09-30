package solstice.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  Column_Adm Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Column_Adm extends X_P_Column_Adm
{
	/**
	 * 	Get Column_Adm
	 *	@param ctx context
	 * 	@param P_Column_Adm_ID id
	 *	@return Column_Adm
	 */
	public static P_Column_Adm get (Properties ctx, int P_Column_Adm_ID, String trxName)
	{
		Integer key = new Integer (P_Column_Adm_ID);
		P_Column_Adm Column_Adm = (P_Column_Adm)s_cache.get(key);
		if (Column_Adm != null)
			return Column_Adm;
		Column_Adm = new P_Column_Adm (ctx, P_Column_Adm_ID, trxName);
		s_cache.put (key, Column_Adm);
		return Column_Adm;
	}	//	get

	/**	Cache						*/
	private static	CCache<Integer, P_Column_Adm> s_cache = new CCache<Integer,P_Column_Adm>("P_Column_Adm", 10);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Column_Adm_ID id
	 */
	public P_Column_Adm (Properties ctx, int P_Column_Adm_ID, String trxName)
	{
		super (ctx, P_Column_Adm_ID, trxName);
		if (P_Column_Adm_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Column_Adm

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Column_Adm (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Column_Adm (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Column_Adm_ID"), trxName);
	}	//	P_Column_Adm

   /**
     * Procédure de copie. Lorsqu'on copie un code de regroupement, on doit également
     * copier tous les paramètres et le contenue des onglets sous le gain.
     */
    public void afterCopy(int originalId)
    {
        try
        {
//        	copyColumnEarning (originalId);
 //       	copyColumnTaxableBenefit ( originalId);
        }
        catch (Exception e)
        {
            log.log(Level.SEVERE, "afterCopy", e);
        }
    }

    
    /**
     * Copy Earning Column Adm. 
     */
    private void copyColumnEarning (int originalId) throws Exception
    {
        // On sort tous les enregistrements à copier de la table
        String sql
        = "select *"
            + " from P_Gain_Column"
            + " where P_Gain_Column.P_Column_Adm_ID  = " + originalId;
        
        PreparedStatement stmt = DB.prepareStatement(sql, this.get_TrxName());
        ResultSet rs = stmt.executeQuery();
        
		while ( rs.next () )
		{
		    P_Gain_Column GainColumn = new P_Gain_Column( Env.getCtx(), -1, this.get_TrxName());
		    GainColumn.setP_Gain_Parameter_ID( rs.getInt("P_Gain_Parameter_ID" ) );
		    GainColumn.setP_Column_Adm_ID( this.getP_Column_Adm_ID() );
		    GainColumn.setIsAdmissible( rs.getString("IsAdmissible").equals("Y") );
		    GainColumn.save();
		}
        
        rs.close();
        stmt.close();
    }

    /**
     * Copy Taxable Benefit Column Adm
     */
	
    private void copyColumnTaxableBenefit (int originalId) throws Exception
    {
        // On sort tous les enregistrements à copier de la table
        String sql
        = "select *"
            + " from P_Taxable_Benefit_Column"
            + " where P_Taxable_Benefit_Column.P_Column_Adm_ID  = " + originalId;
        
        PreparedStatement stmt = DB.prepareStatement(sql, this.get_TrxName());
        ResultSet rs = stmt.executeQuery();
        
		while ( rs.next () )
		{
		    P_Taxable_Benefit_Column Taxable_BenefitColumn = new P_Taxable_Benefit_Column( Env.getCtx(), -1, this.get_TrxName());
		    Taxable_BenefitColumn.setP_Taxable_Benefit_ID( rs.getInt("P_Taxable_Benefit_ID"));
		    Taxable_BenefitColumn.setP_Column_Adm_ID( this.getP_Column_Adm_ID());
		    Taxable_BenefitColumn.setIsAdmissible( rs.getString("IsAdmissible").equals("Y") );
		    Taxable_BenefitColumn.save();
		}
        
        rs.close();
        stmt.close();
    }

    
	protected boolean afterSave (boolean newRecord, boolean success)
	{
	    if (!success )
		{
			return success;
		}
	    

//	    if ( newRecord )
//	    {
			String sql = null;
			sql = " SELECT P_Gain_Parameter_ID "
		        + " FROM    P_Gain_Parameter "
		        + " WHERE P_Gain_Parameter.IsActive = 'Y' "
		        + "   AND P_Gain_Parameter.AD_Client_ID in ( " + getAD_Client_ID() + ", 0 )"
			    + "   AND NOT EXISTS ( Select 1 from P_Gain_Column Where P_Gain_Parameter.P_Gain_Parameter_ID = P_Gain_Column.P_Gain_Parameter_ID " 
			    + "          AND P_Gain_Column.P_Column_Adm_ID = " + this.getP_Column_Adm_ID() + ")"
			    ;
			//
			PreparedStatement pstmt = null;

			try
			{
				pstmt = DB.prepareStatement (sql, null);
				ResultSet rs = pstmt.executeQuery ();
				while ( rs.next () )
				{
				    P_Gain_Column GainColumn = new P_Gain_Column( Env.getCtx(), -1, this.get_TrxName());
				    GainColumn.setP_Gain_Parameter_ID( rs.getInt("P_Gain_Parameter_ID" ) );
				    GainColumn.setP_Column_Adm_ID( this.getP_Column_Adm_ID() );
				    GainColumn.setIsAdmissible( false );
				    GainColumn.save();
				}

				rs.close ();
				pstmt.close ();
				pstmt = null;
			}
			catch (Exception e)
			{
				System.out.println ("P_Gain_Parameter - afterSave - " + sql + " - " + e);
			}
			
			pstmt = null;

			sql = " SELECT P_Taxable_Benefit_ID "
		        + " FROM    P_Taxable_Benefit "
		        + " WHERE P_Taxable_Benefit.IsActive = 'Y' "
		        + "   AND P_Taxable_Benefit.AD_Client_ID in ( " + getAD_Client_ID() + ", 0 )" 
				+ "   AND NOT EXISTS ( Select 1 from P_Taxable_Benefit_Column Where P_Taxable_Benefit_Column.P_Taxable_Benefit_ID = P_Taxable_Benefit.P_Taxable_Benefit_ID " 
				+ "          AND P_Taxable_Benefit_Column.P_Column_Adm_ID = " + this.getP_Column_Adm_ID()  + ")"
				;

			
			try
			{
				pstmt = DB.prepareStatement (sql, null);
				int index = 1;
				ResultSet rs = pstmt.executeQuery ();
				while ( rs.next () )
				{
				    P_Taxable_Benefit_Column Taxable_BenefitColumn = new P_Taxable_Benefit_Column( Env.getCtx(), -1, this.get_TrxName());
				    Taxable_BenefitColumn.setP_Taxable_Benefit_ID( rs.getInt("P_Taxable_Benefit_ID"));
				    Taxable_BenefitColumn.setP_Column_Adm_ID( this.getP_Column_Adm_ID());
				    Taxable_BenefitColumn.setIsAdmissible( false );
				    Taxable_BenefitColumn.save();

				}

				rs.close ();
				pstmt.close ();
				pstmt = null;
			}
			catch (Exception e)
			{
				System.out.println ("P_Taxable_Benefit - afterSave - " + sql + " - " + e);
			}
        
	//    }
		return true;
	}  //	afterSave	



}	//	P_Column_Adm
