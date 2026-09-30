/*
 * Created on 26 août 2005
 */
package solstice.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import solstice.model.P_Period;
import solstice.utils.PgiUtil;

/**
 * @author frafor01
 *
 * Procédure de fermeture d'une période. On doit vérifier, dans la table P_Payment, que
 * le statut de la feuille de temps soit TRANSFÉRÉ pour tous les payment de la période
 * de paie courrante. Si c'est le cas, on ferme la période courrante et on ouvre la prochaine.
 */
public class P_Period_Ending extends SvrProcess
{

    P_Period period;
    int P_Calendar_ID;
    
    /**
     * Constructeur
     */
    public P_Period_Ending()
    {
        super();
    }

    /**
     * Méthode de récupération des parametres
     */
    protected void prepare()
    {
		ProcessInfoParameter[] para = getParameter();
		String paramName = "";
		
		//Read the parameters
		for(int i=0; i < para.length; i++){
			paramName = para[i].getParameterName();
			if( paramName.equals("P_Calendar_ID")){
				this.P_Calendar_ID = ((Number)para[i].getParameter()).intValue();
			}
		}
			
    }

    /**
     * Traitement principal de la procédure de fermeture de période
     */
    protected String doIt() throws Exception
    {

    	// Remboursement des comptes de dépense ZOHO Expense.
//        if (PgiUtil.getSolsticeParameter(getCtx(), "Environnement").equals("Production"))
//        {
        	new ImportExportZohoReimburse( true );
//        }

    	
    	// .getOpenPeriod(Env.getCtx(), null);
        period = P_Period.getOpenPeriodWithCalendar(getCtx(), P_Calendar_ID, null); 
        
        // On vérifie d'abord si les feuilles de temps pour la période
        // courrante ont toutes été transférées
        if(this.checkAllTransfered())
        {
        	
            // On doit maintenant récupérer l'id de la période courrante
            int periodId = period.getP_Period_ID();
            if(periodId > 0)
            {
                // On ferme la période courrante
                String sql
                = "update P_Period set PeriodStatus = 'C' where P_Period_ID = " + periodId + " and AD_Client_ID = " + this.getAD_Client_ID();

                DB.executeUpdate(sql, null);
                
                // On ouvre la prochaine période
                // (Mars 2010 - Louise L. - Magnetho 849)
                // Si la période courante=24 ouvrir la periode 1
                // Si la période courante=25 (période d'ajustement) ne rien faire 
                
                if ( !period.isAdjustmentPeriod() )
                {
	                sql = "update P_Period"
	                    + " set PeriodStatus = 'O'"
	                    + " where P_Period_ID = ("
	                    + "   select top 1 P_Period_ID"
	                    + "   from P_Period"
	                    + "   where StartDate > " + DB.TO_DATE( period.getStartDate()) + " and IsAdjustmentPeriod = 'N'"
	                    + "   order by StartDate"
	                    + " )"
	        			+ " AND P_Frequency_ID = "  + period.getP_Frequency_ID()  
	        			+ " AND AD_Client_ID = " + this.getAD_Client_ID()
	                    ;
	                
	                DB.executeUpdate(sql, null);
	                
	
	                // HardCode Statut Delete.  
	                String sqldel = "delete from P_Absence "
	        	          + " Where P_Absence_Status_ID = 1000008 "
	        	          + " and not exists ( Select 1 from P_Absence_Detail Where  P_Absence_Detail.P_Absence_ID = P_Absence.P_Absence_ID and AbsenceDate > " + DB.TO_DATE( period.getEndDate() )+ " )"
	        	          ;
	                DB.executeUpdate( sqldel, null);
	
	
	                // On doit maintenant supprimer tous les enregistrements de la table P_Particular_Sheet
	                // Sauf s'il proviene d'une autorisation d'absence pas encore traité.
	                // Tout les enregistement de la même périodicité.
	                
	                // TODO ajouter le group de paiement dans la table.
	                sqldel = "delete from P_Particular_Sheet "
	                	          + " Where AD_Client_ID = " + this.getAD_Client_ID() 
	                	          + " AND (P_Absence_Detail_ID Is NULL " 
	                	          +	" OR Not Exists( Select 1 from P_Absence_Detail Where P_Absence_Detail.P_Absence_Detail_ID  = P_Particular_Sheet.P_Absence_Detail_ID and Processed = 'N' ))" 
	                	          + " And Exists ( Select 1 from P_Period where P_Period.P_Period_ID = P_Particular_Sheet.P_Period_ID and P_Period.P_Frequency_ID = " + period.getP_Frequency_ID() + ")"
	                	          ;
	                DB.executeUpdate( sqldel, null);
	
	                // TODO ajouter le group de paiement dans la table.
	                sqldel = "delete from P_Particular_Credits "
	                	          + " Where AD_Client_ID = " + this.getAD_Client_ID() 
	                	          + " And Exists ( Select 1 from P_Period where P_Period.P_Period_ID = P_Particular_Credits.P_Period_ID and P_Period.P_Frequency_ID = " + period.getP_Frequency_ID() + ")"
	                	          ;
	                DB.executeUpdate( sqldel, null);
	
	                // Anomalie 1402  
	                // Ménage de la table Calcul net correction.
	                //
	                sqldel = "delete from P_Calcul_Net_Correction "   	          
	                			  + " WHERE AD_Client_ID = " + this.getAD_Client_ID()
	                	          + " And Exists ( Select 1 from P_Period where P_Period.P_Period_ID = P_Calcul_Net_Correction.P_Period_ID and P_Period.P_Frequency_ID = " + period.getP_Frequency_ID() + ")"
	    			;
	                DB.executeUpdate( sqldel, null);

	                
	                
	                // Permet de personnaliser la fermeture de période
	                DB.executeUpdate( "EXEC SP_Period_End", null);
	                
/*	                
	                //2022-06-02 Ménage des données importé.
	                sqldel = "delete from I_Expense_Account  " + " WHERE AD_Client_ID = " + this.getAD_Client_ID() ;
	                DB.executeUpdate( sqldel, null);
	                sqldel = "delete from I_Particular_Sheet " + " WHERE AD_Client_ID = " + this.getAD_Client_ID() ;
	                DB.executeUpdate( sqldel, null);
	                
	                sqldel = "DISABLE TRIGGER  [dbo].[TRG_DEDUCTION_EXCEPTION_D] ON [dbo].[I_DEDUCTION_EXCEPTION]";
	                DB.executeUpdate( sqldel, null);	                
                				
	                sqldel = "delete from I_DEDUCTION_EXCEPTION " + " WHERE AD_Client_ID = " + this.getAD_Client_ID() ;
	                DB.executeUpdate( sqldel, null);	                

	                sqldel = "ENABLE TRIGGER  [dbo].[TRG_DEDUCTION_EXCEPTION_D] ON [dbo].[I_DEDUCTION_EXCEPTION]";
        	        DB.executeUpdate( sqldel, null);	                


	                sqldel = "DISABLE TRIGGER  [dbo].[TRG_EMPLOYEE_DONATION_D] ON [dbo].[I_EMPLOYEE_DONATION]";
	                DB.executeUpdate( sqldel, null);	                
                				
	                sqldel = "delete from I_EMPLOYEE_DONATION " + " WHERE AD_Client_ID = " + this.getAD_Client_ID() ;
	                DB.executeUpdate( sqldel, null);	                

	                sqldel = "ENABLE TRIGGER  [dbo].[TRG_EMPLOYEE_DONATION_D] ON [dbo].[I_EMPLOYEE_DONATION]";
        	        DB.executeUpdate( sqldel, null);	                

        	        
        	        
        	        sqldel = "truncate table P_Punch_Time";
        	        DB.executeUpdate( sqldel, null);
        	        sqldel = "truncate table DB_PLANIFICO_PUNCH_V2";
        	        DB.executeUpdate( sqldel, null);
      */  	        

        	        
        	        //2023-05-16 pépare les avancements echelon
        	        DB.executeUpdate("EXEC SP_EMPLOYEE_SCALE_ADVANCE ", null);

                    if (PgiUtil.getSolsticeParameter(getCtx(), "Environnement").equals("Production"))
                    {
                    	new UpdatePlanifico();
                    }
                    
                    
                }
	                
                // On indique à l'utilisateur que le traitement a réussit avec succès
                return Msg.translate(Env.getCtx(), "Success");
            }
            // Il semble y avoir eu une erreur dans le chargement de la période courrante
            return Msg.translate(Env.getCtx(), "DBExecuteError");
        }
        // Ce n'est pas toutes les périodes qui ont été transférées
        return Msg.translate(Env.getCtx(), "notAllTransfered");
    }

    /**
     * On vérifie si toutes les feuilles de temps pour la période courrante
     * ont été transférées
     */
    private boolean checkAllTransfered()
    {
        
        String sql = "select 1" 
            + " from P_Time_Sheet"
            + " where TimeSheetStatus Not IN ( 'T', 'Z' )"
            + " and P_Period_ID = " + period.getP_Period_ID();
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            boolean notAllTransfered = rs.next();
            
            rs.close();
            stmt.close();
            
            // Si les feuilles n'ont pas toutes été transférées, on retourne FALSE
            if(notAllTransfered)
            {
                return false;
            }
        }
        catch (SQLException e)
        {
            log.log(Level.SEVERE, "P_Period_Ending.doIt()", e);
        }
        
        // Toutes les feuilles ont été transférées
        return true;
    }
    
}
