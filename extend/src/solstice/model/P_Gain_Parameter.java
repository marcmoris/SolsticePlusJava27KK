/*
 * Created on 10 août 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_Gain_Parameter extends X_P_Gain_Parameter
{
	/**
	 * 	Get P_Gain_Parameter
	 *	@param ctx context
	 * 	@param P_Gain_Parameter_ID id
	 *	@return P_Gain_Parameter
	 */
	public static P_Gain_Parameter get (Properties ctx, int P_Gain_Parameter_ID, String trxName)
	{
		Integer key = new Integer (P_Gain_Parameter_ID);
		P_Gain_Parameter gain_Parameter = (P_Gain_Parameter)s_cache.get(key);
		if (gain_Parameter != null)
			return gain_Parameter;
		gain_Parameter = new P_Gain_Parameter (ctx, P_Gain_Parameter_ID, trxName);
		s_cache.put (key, gain_Parameter);
		return gain_Parameter;
	}	//	get


	public static P_Gain_Parameter get (Properties ctx, int P_Gain_ID, int P_Collective_Labour_Agr_ID, String trxName)
	{
		int id = 0;
		String sql = "Select P_Gain_Parameter_ID From P_Gain_Parameter "
			       + " Where P_Gain_ID = " + P_Gain_ID
				   + "  AND ( P_Collective_Labour_Agr_ID IS NULL OR P_Collective_Labour_Agr_ID = " + P_Collective_Labour_Agr_ID + " ) "
				   + "  AND P_Gain_Parameter.IsActive = 'Y' "
				   + "  ORDER BY P_Collective_Labour_Agr_ID DESC"
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
			System.err.println("P_Gain_Parameter get - " + e);
		}
		
		if ( id == 0 )
			return null;
		
		return P_Gain_Parameter.get( ctx, id , trxName);
	}	//	get

	public static P_Gain_Parameter getWithCollective_Labour_Agr (Properties ctx, int P_Gain_ID, int P_Collective_Labour_Agr_ID, String trxName)
	{
		int id = -1;
		String sql = "Select P_Gain_Parameter_ID From P_Gain_Parameter "
			       + " Where P_Gain_ID = " + P_Gain_ID
				   + "  AND P_Collective_Labour_Agr_ID = " + P_Collective_Labour_Agr_ID   
				   + "  ORDER BY P_Collective_Labour_Agr_ID "
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
			System.err.println("P_Gain_Parameter get - " + e);
		}
		
		if ( id == -1)
			return null;
		
		return P_Gain_Parameter.get( ctx, id , trxName);
	}	//	get

	/**	Cache						*/
	
	private static CCache<Integer,P_Gain_Parameter>	s_cache = new CCache<Integer,P_Gain_Parameter>("P_Gain_Parameter", 20);

    /**
     * @param ctx
     * @param P_Gain_Parameter_ID
     */
    public P_Gain_Parameter(Properties ctx, int P_Gain_Parameter_ID, String trxName)
    {
        super(ctx, P_Gain_Parameter_ID, trxName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param ctx
     * @param rs
     */
    public P_Gain_Parameter(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
        // TODO Auto-generated constructor stub
    }

    private boolean isCopyAction = false;
    
    public void setCopyAction( boolean copyAction )
    {
    	this.isCopyAction = copyAction; 
    }
    
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

	    if ( newRecord & ! isCopyAction )
	    {
			String sql = null;
			sql = " SELECT col.P_COLUMN_ADM_ID "
		        + " FROM    P_COLUMN_ADM col "
		        + " WHERE col.IsActive = 'Y' "
		        + "   AND col.AD_Client_ID in ( " + getAD_Client_ID() + ", 0 )"; 
//		        + "   AND col.AD_Org_ID    = " + getAD_Org_ID() ;

			//
			
			PreparedStatement pstmt = null;

			try
			{
				pstmt = DB.prepareStatement (sql, null);
				ResultSet rs = pstmt.executeQuery ();
				while ( rs.next () )
				{
				    X_P_Gain_Column GainColumn = new X_P_Gain_Column( Env.getCtx(), -1, this.get_TrxName());
				    GainColumn.setP_Gain_Parameter_ID( this.getP_Gain_Parameter_ID() );
				    GainColumn.setP_Column_Adm_ID( rs.getInt(1));
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
	    }
	    isCopyAction = false;
	    
		return true;
	}  //	afterSave	

}
