package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Msg;

public class P_Employee_Distribution_Booklet extends
        X_P_Employee_Distribution_Booklet
{
    /**
     *  Get Employee_Event
     *  @param ctx context
     *  @param P_Employee_Event_ID id
     *  @return Employee_Event
     */
    public static P_Employee_Distribution_Booklet get (Properties ctx, int P_Employee_DIstribution_Booklet_ID, String trxName)
    {
        Integer key = new Integer (P_Employee_DIstribution_Booklet_ID);
        P_Employee_Distribution_Booklet employee_DIstribution_Booklet = (P_Employee_Distribution_Booklet)s_cache.get(key);
        if (employee_DIstribution_Booklet != null)
            return employee_DIstribution_Booklet;
        employee_DIstribution_Booklet = new P_Employee_Distribution_Booklet (ctx, P_Employee_DIstribution_Booklet_ID, trxName);
        s_cache.put (key, employee_DIstribution_Booklet);
        return employee_DIstribution_Booklet;
    }   //  get


    /** Cache                       */
	private static CCache<Integer,P_Employee_Distribution_Booklet>	s_cache = new CCache<Integer,P_Employee_Distribution_Booklet>("P_Employee_Distribution_Booklet", 20);

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Employee_Distribution_Booklet.class);


    public P_Employee_Distribution_Booklet(Properties ctx,
            int P_Employee_Distribution_Booklet_ID, String trxName)
    {
        super(ctx, P_Employee_Distribution_Booklet_ID, trxName);
    }

    public P_Employee_Distribution_Booklet(Properties ctx, ResultSet rs,
            String trxName)
    {
        super(ctx, rs, trxName);
    }

    /**
     * Avant de sauvegarder, on doit vérifier que la période entrée n'entre
     * pas en conflit avec une autre période pour le même employé. On doit
     * également s'assurer que la date de fin soit supérieure à la date de
     * début.
     */
    protected boolean beforeSave(boolean newRecord)
    {
        // On vérifie d'abord la deuxième condition, soit que la date de fin
        // soit supérieure à la date de début (une date de fin à null = infinit+).
        if(this.getEnd_Date() != null && this.getStart_Date().compareTo(this.getEnd_Date()) >= 0)
        {
            // La date de fin n'est pas supérieure, on ne peut donc pas enregistrer
			s_log.saveError("ValidationError",Msg.translate(getCtx(), "DateIntervalError"));
//			s_log.log(Level.SEVERE,"ValidationError", Msg.translate(getCtx(), "DateIntervalError"));
            return false;
        }
        
        boolean canSave;
        
        try
        {
            // Cette requête vérifie si une des bornes de la période entre en conflit
            // avec les bornes d'une autre période pour cet employé. Si c'est le cas,
            // la requête retournera un enregistrement
            PreparedStatement stmt = DB.prepareStatement(
                    "select top 1 1" +
                    "  from P_Employee_Distribution_Booklet" +
                    " where ((Start_Date <= " + DB.TO_DATE(this.getStart_Date(), true) + " and " + DB.TO_DATE(this.getStart_Date(), true) + " between Start_Date and isnull(End_Date, " + DB.TO_DATE(this.getStart_Date(), true) + "))" +
                    ( this.getEnd_Date() == null ? "" : "   or (Start_Date <= " + DB.TO_DATE(this.getEnd_Date(), true) + " and " + DB.TO_DATE(this.getEnd_Date(), true) + " between Start_Date and isnull(End_Date, " + DB.TO_DATE(this.getEnd_Date(), true) + "))" ) +
                    ")   and P_Employee_Distribution_Booklet_ID <> " + this.getP_Employee_Distribution_Booklet_ID() +
                    "   and P_Employee_ID = " + this.getP_Employee_ID() +
                    "   and IsActive = 'Y'", null);
            
            ResultSet rs = stmt.executeQuery();
            
            canSave = !rs.next();
            
            rs.close();
            stmt.close();
            
            if ( ! canSave)
            {
				s_log.saveError("ValidationError",Msg.translate(getCtx(), "IntervalError"));
//				s_log.log(Level.SEVERE,"ValidationError", Msg.translate(getCtx(), "IntervalError"));

            }
            
            return canSave;
        }
        catch (Exception e)
        {
            log.log(Level.SEVERE, "P_Employee_Distribution_Booklet.beforeSave", e);
            return false;
        }
    }
    
    
	public static int get_Employee_Distribution_Booklet( int P_Employee_ID )
	{

		String sql = null;
		sql = "Select P_Employee_Distribution_Booklet_ID "; 
		sql += " From P_Employee_Distribution_Booklet";
		sql += " WHERE P_Employee_Distribution_Booklet.IsActive='Y' ";
		sql += " AND P_Employee_Distribution_Booklet.P_Employee_ID=" + P_Employee_ID;

		int iID = 0;

		PreparedStatement pstmt = null;

		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				iID = rs.getInt(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Employee_Distribution_Booklet - get_Employee_Distribution_Booklet - " + sql, e);
		}
			

		return iID;
		
	}
    

}
