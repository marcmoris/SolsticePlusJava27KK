package solstice.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;
import org.compiere.model.MColumn;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  Salary_Scale_Detail Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Salary_Scale_Detail extends X_P_Salary_Scale_Detail
{
	/**
	 * 	Get Salary_Scale_Detail
	 *	@param ctx context
	 * 	@param P_Salary_Scale_Detail_ID id
	 *	@return Salary_Scale_Detail
	 */
	public static P_Salary_Scale_Detail get (Properties ctx, int P_Salary_Scale_Detail_ID, String trxName)
	{
		Integer key = new Integer (P_Salary_Scale_Detail_ID);
		P_Salary_Scale_Detail Salary_Scale_Detail = (P_Salary_Scale_Detail)s_cache.get(key);
		if (Salary_Scale_Detail != null)
			return Salary_Scale_Detail;
		Salary_Scale_Detail = new P_Salary_Scale_Detail (ctx, P_Salary_Scale_Detail_ID, trxName);
		s_cache.put (key, Salary_Scale_Detail);
		return Salary_Scale_Detail;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Salary_Scale_Detail>	s_cache = new CCache<Integer,P_Salary_Scale_Detail>("P_Salary_Scale_Detail", 20);


	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Salary_Scale_Detail.class);
	
	private String				m_trxName = null;

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Salary_Scale_Detail_ID id
	 */
	public P_Salary_Scale_Detail (Properties ctx, int P_Salary_Scale_Detail_ID, String trxName)
	{
		super (ctx, P_Salary_Scale_Detail_ID, trxName);
		if (P_Salary_Scale_Detail_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Salary_Scale_Detail

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Salary_Scale_Detail (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Salary_Scale_Detail (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Salary_Scale_Detail_ID"), trxName);
	}	//	P_Salary_Scale_Detail

	public static P_Salary_Scale_Detail getWithStep (Properties ctx, int P_Salary_Scale_ID, String Step, String trxName)
	{
		// *** Find current new salary scale detail ID
        String sql = "SELECT P_Salary_Scale_Detail_ID " + 
              	" FROM P_Salary_Scale_Detail " +  
				" WHERE P_Salary_Scale_ID = " + P_Salary_Scale_ID + 
                " AND Step = '" + Step + "'" 
                ;
//				" AND IsActive = 'Y'";

        int SalaryScaleDetailID = 0;
        
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();

			if (rs.next())
				SalaryScaleDetailID = rs.getInt("P_Salary_Scale_Detail_ID");
			rs.close();
			pstmt.close();
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"* Error * P_Salary_Scale - getWithStep  - " + sql + " - " ,e);
			return null;
		}
		return P_Salary_Scale_Detail.get( Env.getCtx(), SalaryScaleDetailID, null );
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

	    String sql = "SELECT P_Assignment_Param_ID FROM P_Assignment_Param WHERE P_Salary_Scale_Detail_ID = ?";
	    
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt( 1, this.getP_Salary_Scale_Detail_ID() );
			ResultSet rs = pstmt.executeQuery ();
			while ( rs.next() )
			{
			    P_Assignment_Param assignment_Param = P_Assignment_Param.get( Env.getCtx() ,rs.getInt( "P_Assignment_Param_ID"), this.get_TrxName());
			    if ( assignment_Param.getRemuneration_Method().equals( P_Assignment_Param.REMUNERATION_METHOD_Annual))
			    {
				    assignment_Param.setAnnual_Salary( this.getAnnual_Salary());
				    assignment_Param.setDaily_Salary(null);
			    }

			    if ( assignment_Param.getRemuneration_Method().equals( P_Assignment_Param.REMUNERATION_METHOD_Daily))
			    {
				    assignment_Param.setAnnual_Salary( null );
				    assignment_Param.setDaily_Salary( this.getAnnual_Salary() );
			    }
			    assignment_Param.setHourly_Rate( this.getHourly_Rate());
			    assignment_Param.save();
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;

		}
		catch (Exception e)
		{
			System.out.println ("* Error * P_Assignment_Hst.Get - " + sql + " - " + e);

		}
		
			
//	    log.info("afterSave - New=" + newRecord + ", Success=" + success + " ***");
		return success;
	}	//	afterSave
	

	/**
	 * 	Called before Save for Pre-Save Operation
	 * 	@param newRecord new record
	 *	@return true if record can be saved
	 */
	protected boolean beforeSave(boolean newRecord)
	{
		//2011.05.06 changé l'appel de la fonction par un calcul car la ligne n'existe pas en création.
		//HARCODE column Salary_Mid
		MColumn Column = MColumn.get(getCtx(), 2230778);
		if ( ! Column.isVirtualColumn() && ( this.getSalary_Mid() == null || this.getSalary_Mid().compareTo( Env.ZERO) == 0))
			this.setSalary_Mid( (this.getSalary_Limit_Max().subtract( this.getSalary_Limit_Min() )).divide( new BigDecimal(2) ).add( this.getSalary_Limit_Min())  );

		/*String sql = "select dbo.get_salary_scale_detail_mid( ? ) as SalaryMid ";
	    
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt( 1, this.getP_Salary_Scale_Detail_ID() );
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next() )
			{
				if ( this.getSalary_Mid() == null || this.getSalary_Mid().compareTo( Env.ZERO) == 0)
					this.setSalary_Mid( rs.getBigDecimal( "SalaryMid"));
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;

		}
		catch (Exception e)
		{
			System.out.println ("* Error * beforeSave - " + sql + " - " + e);
		}
*/
		return true;
	}	//	beforeSave

	
	
	//2011.05.06 override model
	public void setSalary_Mid (BigDecimal Salary_Mid)
	{
		MColumn Column = MColumn.get(getCtx(), 2230778);
		if (  Column.isVirtualColumn() )
			throw new IllegalArgumentException ("Salary_Mid is virtual column");
		set_Value ("Salary_Mid", Salary_Mid);

	}

}	//	P_Salary_Scale_Detail
