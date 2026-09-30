/******************************************************************************
 * Product: Solstice+ Payroll & Human Resources Management                    *
 * Copyright (C) 2008 ProGestion Informatique, Inc. All Rights Reserved.      *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ProGestion Informatique, 210-5300 Bld des Galerie, Quebec, G2K 2A2 Canada  *
 * or via info@progestion.net or http://www.progestion.net/license.html       *
 ******************************************************************************/
package solstice.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import solstice.model.P_Augmentation;
import solstice.model.P_Augmentation_Result_TS;
import solstice.model.P_Employee;
import solstice.model.P_Period;
import solstice.model.P_Time_Sheet_Detail;
import solstice.model.P_Time_Sheet;

/**
 * @author frafor01
 *
 * Cette classe définit la procédure qui sert à générer les paiements
 * à partir des résultats du calcul de rétroactivité
 */
public class P_RetroPaymentGeneration extends SvrProcess
{
    private P_Augmentation augmentation;

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_RetroPaymentGeneration.class);

    public P_RetroPaymentGeneration()
    {
        super();
    }

    /**
     * Récupération des paramètres
     */
    protected void prepare()
    {
        // On récupère les paramètres de la rétro
        this.augmentation = new P_Augmentation(Env.getCtx(), this.getRecord_ID(), null);
    }
    
    /**
     * Traitement principal
     */
    protected String doIt() throws Exception
    {
        // On doit d'abord obtenir la liste des employés pour lesquels on doit calculer
        // la rétroactivité
        String sql
        = "select P_Augmentation_Result_ID, P_Employee_ID"
            + " from P_Augmentation_Result"
            + " where P_Augmentation_ID = " + this.augmentation.getP_Augmentation_ID()
        	+ " and IsAugmentationResult = 'N'";
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            int employeeId = rs.getInt("P_Employee_ID");
            
	        // Pour effectuer cette génération, on doit prendre pour acquis qu'il existe
	        // déjà une feuille de temps pour l'employé à la période courrante. On récupère
	        // donc son id pour ensuite pouvoir générer les lignes de la feuille selon les
	        // résultats du calcul de la rétroactivité
	        int timeSheetId = this.findTimeSheet(employeeId);
	        if(timeSheetId > 0)
	        {
	            // Il reste à générer le détail de la feuille de temps
	            this.generateTimeSheetDetail(employeeId, timeSheetId, rs.getInt("P_Augmentation_Result_ID"));
//	            P_Augmentation_Result AugmentationResult = P_Augmentation_Result.get(Env.getCtx(), rs.getInt("P_Augmentation_Result_ID"), null);
//	            AugmentationResult.setIsAugmentationResult(true);
//	            AugmentationResult.save();
	        }
	        else
	        {
		        // Il n'existait pas de feuille de temps pour la période courrante alors
		        // renvoit l'erreur à l'utilisateur
	            rs.close();
	            stmt.close();
		        return Msg.translate(Env.getAD_Language(Env.getCtx()), "FailedNoTimeSheet") + employeeId + ")";
	        }
        }
        return Msg.translate(Env.getAD_Language(Env.getCtx()), "Success");
    }
    
    /**
     * Cette méthode retourne le id de la feuille de temps pour l'employé passé en paramètre
     * à la période courrante. S'il n'existe pas de feuille, le id est égal à 0
     */
    private int findTimeSheet(int employeeId) throws SQLException
    {
    	P_Employee Employee = P_Employee.get( Env.getCtx(), employeeId, null);
        P_Period period = P_Period.getOpenPeriodWithPaymentGroup(Env.getCtx(), Employee.getP_Payment_Group_ID(), null);

        int id = 0;
        String sql
        = "select top 1 P_Time_Sheet_ID"
            + " from P_Time_Sheet"
            + " where P_Employee_ID = " + employeeId
            + " and SheetType = '" + P_Time_Sheet.SHEETTYPE_Regular + "'" 
            + " and TimeSheetStatus = '" + P_Time_Sheet.TIMESHEETSTATUS_Initial + "'"
            + " and P_Period_ID = " + period.getP_Period_ID();
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next())
        {
            id = rs.getInt("P_Time_Sheet_ID");
        }
        
        rs.close();
        stmt.close();
        
        return id;
    }
    
    /**
     * Cette méthode génère le détail de la feuille de temps à partir des résultats
     * de la rétroactivité pour l'employé et la feuille passés en paramètre
     */
    private void generateTimeSheetDetail(int employeeId, int timeSheetId, int augmentationResultId) throws Exception
    {
        // On doit d'abord obtenir les résultats du calcul de la rétroactivité
        // pour l'employé passé en paramètre et l'augmentation courrante
    	
    	// TODO harcoder "SYSTÈME"
    	
        String sql
        = "select P_Payment.P_Year_ID as AnneeTransac, " 
        + "       P_period.P_year_id as AnneePaiement, "
        + "       P_augmentation_gain.P_Gain_Current_ID, "
        + "       P_augmentation_gain.P_Gain_Old_ID, "
        + "       sum( case P_Augmentation_Payment.UserName "
        + "             when 'SYSTÈME' " 
        + "             then P_Augmentation_Payment.RetroAmount"
        + "             else P_Augmentation_Payment.AmountModified"
        + "            end"
        + "          ) as Amount"
        + " from P_Augmentation_Payment  "
        + " inner join P_Augmentation  on P_Augmentation.P_Augmentation_ID = P_Augmentation_Payment.P_Augmentation_ID "
        + " inner join P_Augmentation_Result on P_Augmentation_Result.P_Augmentation_Result_ID = P_Augmentation_Payment.P_Augmentation_Result_ID "
        + " inner join P_Payment_Gain on P_Augmentation_Payment.P_Payment_Gain_ID = P_Payment_Gain.P_Payment_Gain_ID " 
        + " inner join P_Payment on P_Augmentation_Payment.P_Payment_ID = P_Payment.P_Payment_ID  "
        + " inner join P_augmentation_gain  on  P_augmentation_gain.P_augmentation_gain_ID = P_augmentation.P_augmentation_Gain_ID " 
//        + " inner join P_augmentation_gain_detail augmentationgaindetail on augmentationgaindetail.p_augmentation_gain_id = augmentationgain.p_augmentation_gain_id "  
        + " inner join P_period on P_period.periodstatus = 'O' "  
        + " where P_Augmentation_Result.P_Employee_ID = " + employeeId
        + "  and P_Augmentation_Payment.P_Augmentation_Result_ID = " + augmentationResultId
        + "  group by P_Payment.P_Year_ID ,"
        + "           P_period.P_year_id ,"
        + "           P_augmentation_gain.P_Gain_Current_ID,"
        + "           P_augmentation_gain.P_Gain_Old_ID";
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // On doit maintenant créer le paiement
            P_Time_Sheet_Detail timeSheetDetail = createTimeSheetDetail(employeeId, timeSheetId);
            if ((rs.getBigDecimal("AnneeTransac")).compareTo( (rs.getBigDecimal("AnneePaiement"))) < 0)
            {
                timeSheetDetail.setP_Gain_ID(rs.getInt("P_Gain_Old_ID"));                     	
            }
            else
            {
                timeSheetDetail.setP_Gain_ID(rs.getInt("P_Gain_Current_ID"));                     	
            }
       //     timeSheetDetail.setP_Gain_ID(rs.getInt("P_Gain_ID"));
            timeSheetDetail.setDayQty(rs.getBigDecimal("Amount"));
            timeSheetDetail.setWeekIndex(1);
            timeSheetDetail.setOriginTime("STD");
            if(!timeSheetDetail.save())
            {
                rs.close();
                stmt.close();
                throw new SQLException(Msg.translate(Env.getLanguage(Env.getCtx()), "FailedSaveTSDetail") + employeeId + ")");
            }
            
            // On doit créer un lien dans la table P_Augmentation_Result_TS pour indiquer
            // à quel détail de feuille de temps le résultat fait référence
            P_Augmentation_Result_TS augmentationResultTS = new P_Augmentation_Result_TS(Env.getCtx(), -1, null);
            augmentationResultTS.setP_Augmentation_Result_ID(augmentationResultId);
            augmentationResultTS.setP_Time_Sheet_Detail_ID(timeSheetDetail.getP_Time_Sheet_Detail_ID());
            augmentationResultTS.setP_Year_ID( rs.getInt( "AnneeTransac"));
            augmentationResultTS.save();
        }
        
        rs.close();
        stmt.close();
    }
    
    /**
     * Initialisation des principaux champs pour la création d'une feuille de temps
     */
    private P_Time_Sheet_Detail createTimeSheetDetail(int employeeId, int timeSheetId) throws SQLException
    {
        P_Time_Sheet_Detail timeSheetDetail = new P_Time_Sheet_Detail(Env.getCtx(), -1, null);
        
        // On doit récupérer les informations sur la période courante
        String sql
        = "select StartDate,"
            + "        EndDate,"
            + "        P_Period_ID"
            + "   from P_Period"
            + "  where PeriodStatus = 'O'";
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next())
        {
            timeSheetDetail.setP_Time_Sheet_ID(timeSheetId);
            timeSheetDetail.setDay(rs.getTimestamp("StartDate"));
            timeSheetDetail.setP_Period_ID(rs.getInt("P_Period_ID"));
            timeSheetDetail.setStartDate(rs.getTimestamp("StartDate"));
            timeSheetDetail.setEndDate(rs.getTimestamp("StartDate"));
            
            // On doit ensuite trouver l'affectation selon les méthodes
            // suivantes : 1) On tente de trouver une affectation active
            // à la date de paiement de la rétro déjà utilisée sur la feuille.
            // 2) Si la première méthode n'a rien donné, on doit trouver
            // une affectation active à la date de paiement de la rétro.
            int assignmentId = this.assignmentFromTimeSheet(timeSheetId, rs.getTimestamp("EndDate"));
            if(assignmentId == 0)
            {
                assignmentId = this.assignmentFromEmployee(employeeId, rs.getTimestamp("EndDate"));
            }

            if(assignmentId == 0)
            {
                assignmentId = this.assignmentFromEmployeeInactif( employeeId);
            }
            // Finalement, on trouve le P_Schedule_ID à partir du paramètre d'affectation
            int scheduleId = this.findScheduleId(assignmentId, rs.getTimestamp("EndDate"));
            timeSheetDetail.setP_Assignment_ID(assignmentId);
            timeSheetDetail.setP_Schedule_ID(scheduleId);
        }
        
        rs.close();
        stmt.close();
        
        return timeSheetDetail;
    }
    
    /**
     * on trouve le P_Schedule_ID à partir du paramètre d'affectation
     */
    private int findScheduleId(int assignmentId, Timestamp effectIn) throws SQLException
    {
        String sql
        = "select top 1 P_Schedule_ID"
            + "         from P_Assignment_Param"
            + "        where P_Assignment_ID = " + assignmentId
            + "          and EffectIn <= " + DB.TO_DATE(effectIn)
            + "          and IsActive = 'Y'"
            + "     order by EffectIn desc";
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        int scheduleId = 0;
        if(rs.next())
            scheduleId = rs.getInt("P_Schedule_ID");
        
        rs.close();
        stmt.close();
        
        return scheduleId;
    }
    
    /**
     * Si la première méthode n'a rien donné, on doit trouver
     * une affectation active à la date de paiement de la rétro.
     */
    private int assignmentFromEmployee(int employeeId, Timestamp effectIn) throws SQLException
    {
        String sql
        = "select P_Assignment_ID"
            + "   from P_Assignment"
            + "  where P_Employee_ID = " + employeeId
            + "    and IsActive = 'Y'"
            + "    and " + DB.TO_DATE(effectIn) + " between isnull(StartDate, " + DB.TO_DATE(effectIn) + ")"
            + "                      and isnull(EndDate, " + DB.TO_DATE(effectIn) + ")"
            + " order by StartDate desc";
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        int assignmentId = 0;
        if(rs.next())
            assignmentId = rs.getInt("P_Assignment_ID");
        
        rs.close();
        stmt.close();
        
        return assignmentId;
    }
    
    /**
     * Si la première méthode n'a rien donné, on doit trouver
     * une affectation active à la date de paiement de la rétro.
     */
    private int assignmentFromEmployeeInactif(int employeeId) throws SQLException
    {
        String sql
        = "select P_Assignment_ID"
            + "   from P_Assignment"
            + "  where P_Employee_ID = " + employeeId
            + "    and IsActive = 'Y'"
            + " order by StartDate desc";
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        int assignmentId = 0;
        if(rs.next())
            assignmentId = rs.getInt("P_Assignment_ID");
        
        rs.close();
        stmt.close();
        
        return assignmentId;
    }
    
    /**
     * On tente de trouver une affectation active à la date de
     * paiement de la rétro déjà utilisée sur la feuille.
     */
    private int assignmentFromTimeSheet(int timeSheetId, Timestamp effectIn) throws SQLException
    {
        String sql
        = "select max(P_Assignment.P_Assignment_ID) P_Assignment_ID"
            + " from P_Time_Sheet_Detail,"
            + "        P_Assignment"
            + "  where P_Time_Sheet_Detail.P_Time_Sheet_ID = " + timeSheetId
            + "    and P_Assignment.IsActive = 'Y'"
            + "    and P_Time_Sheet_Detail.P_Assignment_ID = P_Assignment.P_Assignment_ID "
//            + "    and P_Assignment.EffectIn <= " + DB.TO_DATE(effectIn)
           ;
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        int assignmentId = 0;
        
        if(rs.next())
            assignmentId = rs.getInt("P_Assignment_ID");
        
        rs.close();
        stmt.close();
        
        return assignmentId;
    }
}
