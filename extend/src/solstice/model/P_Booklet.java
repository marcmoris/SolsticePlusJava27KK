/*
 * Created on 25 août 2005
 */
package solstice.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;


import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;

import solstice.process.Calcul_Credits;


/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_Booklet extends X_P_Booklet implements IBookletTimeSheet
{

    private int distributionBookletId = -1;
    
    public int getDistributionBookletId() {return this.distributionBookletId;}
    public void setDistributionBookletId(int distributionBookletId) {this.distributionBookletId = distributionBookletId;}
    
    /**
     * @param ctx
     * @param P_Booklet_ID
     * @param trxName
     */
    public P_Booklet(Properties ctx, int P_Booklet_ID, String trxName)
    {
        super(ctx, P_Booklet_ID, trxName);
    }

    /**
     * @param ctx
     * @param rs
     * @param trxName
     */
    public P_Booklet(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
    }

    public int getRecord_ID() {return this.getP_Booklet_ID();}
    public void setRecord_ID (int record_ID) {this.setP_Booklet_ID(record_ID);}

    
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true or false
	 */

	protected boolean beforeSave (boolean newRecord)
	{
		if ( this.getTaxation_Region_ID() == 0 )
		{
			P_Employee Employee = P_Employee.get( Env.getCtx(), this.getP_Employee_ID(), this.get_TrxName());
			this.setTaxation_Region_ID(Employee.getTaxation_Region_ID());
			//+ 2011.07.05 + sécurité par compagnie.
			this.setAD_Org_ID( Employee.getAD_Org_ID());
			//- 2011.07.05 + sécurité par compagnie.
		}
		return true;

	}


    
    /**
     * Lorsqu'on sauvegarde un booklet, on doit s'assurer de concerver un historique
     * du TimeSheetStatus. Pour ce faire, on utilise la table P_Booklet_Status_Histo
     */
    protected boolean afterSave(boolean newRecord, boolean success)
    {
        // On doit d'abord vérifier si on a spécifié, avant la sauvegarde, un code
        // de distribution pour le livret de temps. Dans ce cas, on devra effectuer
        // le changement du statut seulement pour ce code de distribution. Sinon, on
        // devra changer le statut du livret pour toutes les distributions touchant
        // l'employé pour la période du livret
        if(this.distributionBookletId > 0)
        {
            try
            {
                return this.changeStatus(this.distributionBookletId);
            }
            catch (SQLException e)
            {
                this.log.log(Level.SEVERE, "P_Booklet.afterSave (premier try)", e.getMessage());
                return false;
            }
        }

        try
        {
            // On doit sortir la liste des distributions associées à l'employé pour
            // la période du livret de temps
            PreparedStatement stmt = DB.prepareStatement(
                    "select employeeDistributionBooklet.P_Distribution_Booklet_ID" +
                    "  from P_Employee_Distribution_Booklet employeeDistributionBooklet," +
                    "       P_Period period" +
                    " where employeeDistributionBooklet.P_Employee_ID = " + this.getP_Employee_ID() +
                    "   and period.P_Period_ID = " + this.getP_Period_ID() +
                    "   and ( employeeDistributionBooklet.Start_Date between period.StartDate and period.EndDate" +
                    "    or   isnull( employeeDistributionBooklet.End_Date, period.EndDate ) between period.StartDate and period.EndDate" +
                    "    or   period.EndDate between employeeDistributionBooklet.Start_Date and isnull(employeeDistributionBooklet.End_Date, period.EndDate) )", null);
            ResultSet rs = stmt.executeQuery();
            
            boolean ok = true;
            
            while(rs.next())
            {
                // On change le statut pour chacune des distributions
                ok = ok && this.changeStatus(rs.getInt(1));
            }
            
            rs.close();
            stmt.close();
            
            return ok;
        }
        catch (SQLException e)
        {
            this.log.log(Level.SEVERE, "P_Booklet.afterSave (deuxième try)", e.getMessage());
            return false;
        }
    }
    
    private boolean changeStatus(int distributionBookletId) throws SQLException
    {
        // On vérifie d'abord si le statut a changé en sortant le dernier
        // statut enregistré pour ce livret
        boolean logChange = true;
        String sql
        = "select top 1 TimeSheetStatus"
            + " from P_Booklet_Status_Histo"
            + " where P_Booklet_ID = " + this.getP_Booklet_ID()
            + " and P_Distribution_Booklet_ID = " + distributionBookletId
            + " order by Created desc";
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next())
        {
            logChange = !rs.getString("TimeSheetStatus").equals(this.getTimeSheetStatus());
        }
        
        rs.close();
        stmt.close();
        
        // Si on doit concerver un historique, on crée un nouvel enregistrement dans la
        // bd. La date de création compte comme la date de l'historique
        if(logChange)
        {
            P_Booklet_Status_Histo bookletStatusHisto = new P_Booklet_Status_Histo(Env.getCtx(), -1, null);
            bookletStatusHisto.setP_Booklet_ID(this.getP_Booklet_ID());
            bookletStatusHisto.setP_Distribution_Booklet_ID(distributionBookletId);
            bookletStatusHisto.setTimeSheetStatus(this.getTimeSheetStatus());
            return bookletStatusHisto.save();
        }
        return true;
    }
    
    public String getTimeSheetStatusHisto( int P_Distribution_Booklet_ID ) 
    {
    	String status = "";
        String sql
        = "select TimeSheetStatus"
            + " from P_Booklet_Status_Histo"
            + " where P_Booklet_ID = " + this.getP_Booklet_ID()
            + " and P_Distribution_Booklet_ID = " + P_Distribution_Booklet_ID
            + " order by Created desc";
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
                status = rs.getString("TimeSheetStatus");
            }
            
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            log.log(Level.SEVERE, "P_Booklet.getTimeSheetStatusHisto", e);
            return null;
        }
    
        return status;
    }
    
    /*
      Integer.parseInt(request.getParameter("employeeId")), 
      this.parseQty(request.getParameter("dayHrs")), 
      Integer.parseInt(request.getParameter("dayCode")),
      Integer.parseInt(request.getParameter("bookletId")),
      request.getParameter("dayName"), 
      new Timestamp(PgiUtil.stringToDateV2(request.getParameter(dayName + "_date")).getTimeInMillis(), 
      trx.getTrxName()
    */
    public String validate( int P_Employee_ID, BigDecimal var, int P_Gain_ID, int P_Booklet_ID, String dayName, Timestamp ts, String trxName)
    {


        // ...on doit procéder à la validation des banques. On doit donc récupérer
        // les paramètres pour le numéro de l'employé, la journée du mouvement de banque,
        // le code de rémunération et la variation ajoutés.
          Calcul_Credits calculCredits = new Calcul_Credits(trxName);
          String erreurs;
        
          try
          {
             erreurs = calculCredits.CreditsVariationFromWebV2(
                    P_Employee_ID,
                    ts,
                    var,
                    P_Gain_ID,
                    P_Booklet_ID);
          }
          catch (Exception e)
          {
            log.log(Level.SEVERE, "P_Booklet.validate", e);
            return "Internal Error ";
          }

    	
    	return erreurs;
    }
    
    
	//
	// Valide les banques pour l'ensemble du livret de temps. 
	// Car il est possible que la génération est générer des banques négatives.
	//
    // isVisible = !LivretTempsPrincipal.loggedUserHasPolicy(pageContext,"viewNonVisibleGain")
	public String validateCredits(int P_Employee_ID, int P_Period_ID, int P_Booklet_ID, boolean isVisible) 
	{
        Trx trx = Trx.get(Trx.createTrxName(), true);

        String sql
        = "select P_Booklet_Detail.P_Booklet_Detail_ID, gain.P_Gain_ID, gain.Value, P_Booklet_Detail.DayQty, P_Booklet_Detail.day"
            + " from P_Booklet " 
            + " inner join P_Booklet_Detail on P_Booklet.P_Booklet_ID = P_Booklet_Detail.P_Booklet_ID"
            + " inner join P_Gain on P_Booklet_Detail.P_Gain_ID = P_Gain.P_Gain_ID "
//            + " and P_Booklet.P_Period_ID = P_Booklet_Detail.P_Period_ID"
            + " where P_Booklet.P_Period_ID = " + P_Period_ID
            + " and P_Booklet.P_Employee_ID = " + P_Employee_ID
            ;
        
        if( isVisible)
        {
            sql += " and Visible = 'Y'";
        }

        sql += " order by P_Booklet_Detail.day" ;

        System.out.println("validateCredits SQL " + sql );

        
        String erreurs = null;

        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            Calcul_Credits calculCredits = new Calcul_Credits(trx.getTrxName());
            
            // On ajoute ensuite chaque élément du select au vecteur de ListMultiColumnItem
            while(rs.next())
            {
                if ( rs.getBigDecimal("DayQty") != null )
                   erreurs = calculCredits.CreditsVariationFromWebV2(
                			 P_Employee_ID,
	                        rs.getTimestamp("day"),
	                        rs.getBigDecimal("DayQty"),
	                        rs.getInt("P_Gain_ID"),
	                        P_Booklet_ID);

                System.out.println(" ValidateCredits : "+ erreurs );
//				recursiveCheckBanksAlerts(rsGain.getInt("P_Method_Credits_ID"), employeeId, false);

            }
            
            rs.close();
            stmt.close();
            trx.commit();
            trx.close();

        }
        catch (Exception e)
        {
            trx.rollback();
            trx.close();
            e.printStackTrace(System.out);
        }
		

		return erreurs;
	}		
	

}
