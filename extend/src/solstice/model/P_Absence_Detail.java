/*
 * Created on 31-Aug-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MClient;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_Absence_Detail extends X_P_Absence_Detail
{
	/**
	 * 	Get absence
	 *	@param ctx context
	 * 	@param P_absence_ID id
	 *	@return absence
	 */
	public static P_Absence_Detail get (Properties ctx, int P_Absence_Detail_ID, String trxName)
	{
		Integer key = new Integer (P_Absence_Detail_ID);
		P_Absence_Detail absence = (P_Absence_Detail)s_cache.get(key);
		if (absence != null)
			return absence;
		absence = new P_Absence_Detail (ctx, P_Absence_Detail_ID, trxName);
		s_cache.put (key, absence);
		return absence;
	}	//	get

	/**	Cache						*/
	private static	CCache<Integer, P_Absence_Detail> s_cache = new CCache<Integer,P_Absence_Detail>("P_Absence_Detail", 100);

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Absence_Detail.class);

    /**
     * @param ctx
     * @param P_Absence_Detail_ID
     * @param trxName
     */
    public P_Absence_Detail(Properties ctx, int P_Absence_Detail_ID,
            String trxName)
    {
        super(ctx, P_Absence_Detail_ID, trxName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param ctx
     * @param rs
     * @param trxName
     */
    public P_Absence_Detail(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
        // TODO Auto-generated constructor stub
    }

    
    //
    // Si tout les détails de l'absence sont traité alors l'absence change de status
    //
	protected boolean afterSave(boolean newRecord, boolean success)
    {
	    // Si le statut du paiement est E, on doit générer les informations de
	    // l'avis de dépôt
	    if(success )
	    {
	    
	        // On supprime d'abord les anciennes valeurs dans Statement Earning s'il y a lieu
	        String sql = "Select 1 From P_Absence_Detail Where P_Absence_ID = " + this.getP_Absence_ID() + " And Processed = 'N' ";
			PreparedStatement pstmt = null;
	        
	        //On remplit les tables
	        try
	        {
				pstmt = DB.prepareStatement (sql, this.get_TrxName());
				ResultSet rs = pstmt.executeQuery ();
				if ( ! rs.next () )
				{
					P_Absence Absence = P_Absence.get( getCtx(), this.getP_Absence_ID(), this.get_TrxName());
					Absence.setP_Absence_Status_ID( 1000006); // Approuvée et transférée au livret de temps
					Absence.save();
				}
				rs.close ();
				pstmt.close ();
				pstmt = null;
					
	        }
	        catch (Exception e)
	        {
	        	s_log.log(Level.SEVERE,"P_Absence_Detail AfterSave",e);
	        }
	    }
	    
	    //
	    // Si la feuille de temps est au status calculéer on créer ou recréer l'écriture comptable
	    //

        return success;
    } //	afterSave

    
}
