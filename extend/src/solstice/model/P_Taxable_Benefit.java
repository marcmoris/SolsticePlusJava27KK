package solstice.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Language;

/**
 *  Taxable_Benefit Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Taxable_Benefit extends X_P_Taxable_Benefit
{
	/**
	 * 	Get Taxable_Benefit
	 *	@param ctx context
	 * 	@param P_Taxable_Benefit_ID id
	 *	@return Taxable_Benefit
	 */
	public static P_Taxable_Benefit get (Properties ctx, int P_Taxable_Benefit_ID, String trxName)
	{
		Integer key = new Integer (P_Taxable_Benefit_ID);
		P_Taxable_Benefit Taxable_Benefit = (P_Taxable_Benefit)s_cache.get(key);
		if (Taxable_Benefit != null)
			return Taxable_Benefit;
		Taxable_Benefit = new P_Taxable_Benefit (ctx, P_Taxable_Benefit_ID, trxName);
		s_cache.put (key, Taxable_Benefit);
		return Taxable_Benefit;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Taxable_Benefit>	s_cache = new CCache<Integer,P_Taxable_Benefit>("P_Taxable_Benefit", 10);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Taxable_Benefit_ID id
	 */
	public P_Taxable_Benefit (Properties ctx, int P_Taxable_Benefit_ID, String trxName)
	{
		super (ctx, P_Taxable_Benefit_ID, trxName);
		if (P_Taxable_Benefit_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Taxable_Benefit

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Taxable_Benefit (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}
	
	
	protected boolean afterSave (boolean newRecord, boolean success)
	{
	    
	    if (!success )
		{
			return success;
		}

	    if ( newRecord )
	    {
			String sql = null;
			sql = " SELECT col.P_COLUMN_ADM_ID "
		        + " FROM    P_COLUMN_ADM col "
		        + " WHERE col.IsActive = 'Y' "
		        + "   AND col.AD_Client_ID in ( " + getAD_Client_ID() + ", 0 )"; 
//		        + "   AND col.AD_Org_ID    = " + getAD_Org_ID() ;

			//
			
			PreparedStatement pstmt = null;

			BigDecimal sld = new BigDecimal(0);
			
			try
			{
				pstmt = DB.prepareStatement (sql, null);
				int index = 1;
				ResultSet rs = pstmt.executeQuery ();
				while ( rs.next () )
				{
				    X_P_Taxable_Benefit_Column Taxable_BenefitColumn = new X_P_Taxable_Benefit_Column( Env.getCtx(), -1, this.get_TrxName());
				    Taxable_BenefitColumn.setP_Taxable_Benefit_ID( this.getP_Taxable_Benefit_ID() );
				    Taxable_BenefitColumn.setP_Column_Adm_ID( rs.getInt(1));
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
	        
	    }
		return true;
	}  //	afterSave	

	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Taxable_Benefit[ID=")
			.append(this.getP_Taxable_Benefit_ID())
			.append(",Value=").append(getValue())
			.append(",Name=").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString


	/**
	 * 	Get Translated Name
	 *	@return name
	 */
	public String getTrlName()
	{
		String ret = get_Translation("Name", Env.getAD_Language(Env.getCtx()));	
		if ( ret == null )
		  return " ";
		return ret;
	}	//	getTrlName
	
	//
	// Return Name with Translation
	// 
	public String getTrlName( String language )
	{
		Language baseLanguage = Language.getBaseLanguage();
		
		if ( language.equals( baseLanguage.getAD_Language()  )  || language.equals( "en_CA") )
			return getName();
		return get_Translation("Name", language );
	}	//	getTrlName

	/**
	 * 	Get Translated Name
	 *	@return name
	 */
	public String getTrlDescription()
	{
			
		return get_Translation("Description", Env.getAD_Language(Env.getCtx()));
	}	//	getTrlDescription


}	//	P_Taxable_Benefit
