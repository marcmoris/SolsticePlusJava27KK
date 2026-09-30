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

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.process.SvrProcess;
import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import solstice.model.P_Augmentation;
import solstice.model.P_Augmentation_Employee;
import solstice.model.P_Augmentation_Payment;
import solstice.model.P_Augmentation_Result;
import solstice.model.P_Payment_Group;
import solstice.model.P_Employee;

/**
 * @author frafor01
 *
 * Cette classe sert à effectuer le calcul de la rétro
 */
public class P_RetroCalculation extends SvrProcess
{
    /*
     * Constantes
     */
    private final BigDecimal CENT = new BigDecimal(100);
    private final BigDecimal ONE = new BigDecimal(1);
    
    
    /*
     * Members
     */
    private P_Augmentation augmentation;
    
    public P_RetroCalculation()
    {
        super();
    }

    /**
     * Cette méthode sert à récupérer les paramètres de lancement pour
     * calculer la rétro. Ces paramètres se retrouve à l'enregistrement de
     * la table P_Augmentation selon le id contenue dans l'objet courrant
     */
    protected void prepare()
    {
        this.augmentation = new P_Augmentation(Env.getCtx(), this.getRecord_ID(), null);
    }

    /**
     * Début du calcul de rétro
     */
    protected String doIt() throws Exception
    {
        // Selon le type d'augmentation, on doit préparer les données du calcul
        PreparedStatement stmt = null;
        switch(this.augmentation.getAugmentationType().charAt(0))
        {
	        case 'E': // Pour un seul employé
	        {
	            stmt = this.prepareEmployee();
	            break;
	        }
	        case 'G': // Pour un groupe d'emploi
	        {
	            stmt = this.prepareGroup();
	            break;
	        }
	        case 'T': // Pour un seul titre d'emploi
	        {
	            stmt = this.prepareJobTitle();
	            break;
	        }
        }
        
        try
        {
	        // Si on a des données pour faire le calcul, on lance la procédure
	        if(stmt != null)
	        {
	            this.executeProcess(stmt);
	            stmt.close();
	        }
	        
	        // On doit maintenant remplir la table des résultats
	        String sql
	        = "select augmentationPayment.P_Augmentation_Payment_ID, payment.P_Employee_ID"
	            + " from P_Payment payment inner join P_Augmentation_Payment augmentationPayment"
	            + " on payment.P_Payment_ID = augmentationPayment.P_Payment_ID"
	            + " where P_Augmentation_ID = " + this.augmentation.getP_Augmentation_ID()
                + " order by payment.P_Employee_ID";
	        
	        stmt = DB.prepareStatement(sql, null);
	        ResultSet rs = stmt.executeQuery();
            
            // Afin de pouvoir créer un seul résult par employé, on utilise une variable pour
            // retenir le numéro d'employé et vérifier à chaque tour de la boucle si on a déjà
            // créé le résultat
            int lastEmployeeId = 0;
            int lastAugmentationResultId = 0;
	        
	        while(rs.next())
	        {
                if(lastEmployeeId != rs.getInt("P_Employee_ID"))
                {
                    lastEmployeeId = rs.getInt("P_Employee_ID");
    	            P_Augmentation_Result augmentationResult = new P_Augmentation_Result(Env.getCtx(), -1, null);
    	            augmentationResult.setP_Augmentation_ID(this.augmentation.getP_Augmentation_ID());
    	            augmentationResult.setP_Employee_ID(rs.getInt("P_Employee_ID"));
//    	            augmentationResult.setIsAugmentationResult(false);
    	            augmentationResult.save();
                    lastAugmentationResultId = augmentationResult.getP_Augmentation_Result_ID();
                }
	            
	            // On doit mettre à jur l'augmentation payment pour indiquer où se retrouve les résultats
	            DB.executeUpdate(
	                    "update P_Augmentation_Payment" +
	                    " set P_Augmentation_Result_ID = " + lastAugmentationResultId +
	                    " where P_Augmentation_Payment_ID = " + rs.getInt("P_Augmentation_Payment_ID"), null);
	        }
	        
	        rs.close();
	        stmt.close();
        }
        catch (Exception e)
        {
            log.log(Level.SEVERE, "doIt", e);
            return Msg.translate(Env.getLanguage(Env.getCtx()), "ProcessFailed");
        }
        
        return Msg.translate(Env.getLanguage(Env.getCtx()), "Success");
    }
    
    /**
     * Calcul principal de la rétro
     */
    private void executeProcess(PreparedStatement stmt) throws Exception
    {
        ResultSet rs = stmt.executeQuery();
        s_cache.clear();
        
        int count = 0;
        
        while(rs.next())
        {
        	count++;
        	
        	P_Employee Employee = P_Employee.get( getCtx(), rs.getInt("P_Employee_ID"), null);
        	log.info("Calcul "  + Employee.getValue () + " " + Employee.getName () );

        	// On doit maintenant vérifier s'il y a une correspondance entre le titre d'emploi,
            // la classe salariale et l'échelon grâce à la table P_Augmentation_Correspondence
            int[] ids = this.findCorrespondance(rs.getInt("P_Job_Title_ID"), rs.getInt("P_Salary_Scale_Detail_ID"), rs.getTimestamp("Day"));
            int salaryScaleDetailId = ids[1];

            BigDecimal percent;
            BigDecimal hourlyRate = null;

//1>
/*            percent = findCorrespondancePercent( rs.getTimestamp("Day") );

            if(percent != null )
            {
            	percent = percent.divide(this.CENT,8, BigDecimal.ROUND_HALF_UP);
            	if ( rs.getBigDecimal("Hourly_Rate") != null )
            		hourlyRate = rs.getBigDecimal("Hourly_Rate").multiply(percent.add(this.ONE));
            	else 
            		hourlyRate = Env.ZERO;
            }
//<1
            else
*/            
            if(this.augmentation.getAugPrcCad() != null
                    && this.augmentation.getAugPrcCad().doubleValue() != 0)
            {
                percent = this.augmentation.getAugPrcCad().divide(this.CENT,8, BigDecimal.ROUND_HALF_UP);
                hourlyRate = rs.getBigDecimal("Hourly_Rate").multiply(percent.add(this.ONE));
            }
            else
            {
            	P_Augmentation_Employee Augmentation_Employee = P_Augmentation_Employee.get( getCtx(), Employee.getP_Employee_ID(), augmentation.getP_Augmentation_ID(), null);
            	percent = Augmentation_Employee.getAugPrcCad().divide(this.CENT,8, BigDecimal.ROUND_HALF_UP);
            	hourlyRate = rs.getBigDecimal("Hourly_Rate").multiply(percent.add(this.ONE));            	
            }
            	
            
            if(rs.getString("GainType").equals("1"))
            {
                BigDecimal nouvSalaire = this.CENT.multiply(this.ONE.add(percent));
                BigDecimal nouvPrime = nouvSalaire.multiply(rs.getBigDecimal("NewPercent"));
                BigDecimal ancPrime = this.CENT.multiply(rs.getBigDecimal("OldPercent").divide(this.CENT, 0, BigDecimal.ROUND_HALF_UP));
                BigDecimal diffPrime = nouvPrime.subtract(ancPrime);
                if(ancPrime.doubleValue() != 0)
                    percent = diffPrime.divide(ancPrime, 0);
            }
            
            // On crée maintenant un enregistrement dans la table P_Augmentation_Payment
            // pour contenir les résultats du calcul de rétro
            P_Augmentation_Payment augmentationPayment = new P_Augmentation_Payment(Env.getCtx(), -1, null);
            augmentationPayment.setP_Augmentation_ID(this.augmentation.getP_Augmentation_ID());
            augmentationPayment.setP_Payment_Gain_ID(rs.getInt("P_Payment_Gain_ID"));
            augmentationPayment.setP_Payment_ID(rs.getInt("P_Payment_ID"));
            if(percent != null && rs.getBigDecimal("AmountCalc") != null)
            {
                augmentationPayment.setPercentage(percent.multiply(this.CENT));
                augmentationPayment.setRetroAmount(rs.getBigDecimal("AmountCalc").multiply(percent));
            }
            augmentationPayment.setHourly_Rate(hourlyRate.setScale(4,BigDecimal.ROUND_HALF_UP));
            augmentationPayment.setUserName("SYSTÈME");
            augmentationPayment.save();
        }
        
        rs.close();
    }
    
    /**
     * Cette méthode retourne la liste des enregistrements nécessaire pour effectuer le
     * calcul de rétro sur une liste d'employé
     */
    private PreparedStatement prepareEmployee()
    {
        String sql
        = " select paymentGain.P_Payment_ID, " + "\n"
        + "        P_Payment.P_Employee_ID ," + "\n"
        + "        paymentGain.P_Payment_Gain_ID, " + "\n"
        + "        augmentationGainDetail.P_Gain_ID," + "\n"
        + "        paymentGain.P_Job_Title_ID, " + "\n"
        + "        paymentgain.P_Salary_scale_ID, " + "\n"
        + "        paymentgain.P_Salary_Scale_Detail_ID," + "\n"
        + "        augmentationGainDetail.GainType, " + "\n"
        + "        augmentationGainDetail.NewPercent, " + "\n"
        + "        augmentationGainDetail.IsIgnoringScale," + "\n"
        + "        augmentationGainDetail.ScalePercent, " + "\n"
        + "        augmentationGainDetail.OldPercent, " + "\n"
        + "        paymentGain.AmountCalc," + "\n"
        + "        isnull(paymentGain.Hourly_Rate,0) Hourly_Rate ," + "\n"
        + "        paymentGain.Day" + "\n"
        + "   from P_Payment_Gain paymentGain " + "\n"
        + "     inner join P_Payment on P_Payment.P_Payment_ID = paymentGain.P_Payment_ID and P_Payment.TimeSheetStatus = 'T' " + "\n"
        + "     inner join P_Augmentation on P_Augmentation.P_Augmentation_ID = " + this.augmentation.getP_Augmentation_ID() + "\n"    
        + "     inner join P_Augmentation_Gain augmentationGain on augmentationGain.P_Augmentation_Gain_ID = P_Augmentation.P_Augmentation_Gain_ID " + "\n"    
        + "     inner join P_Augmentation_Gain_Detail augmentationGainDetail on augmentationGainDetail.P_Augmentation_Gain_ID = augmentationGain.P_Augmentation_Gain_ID and augmentationGainDetail.P_Gain_ID = paymentGain.P_Gain_ID " + "\n"
        + "     left outer join P_Salary_Scale_Detail salaryScaleDetail on salaryScaleDetail.P_Salary_Scale_Detail_ID = paymentGain.P_Salary_Scale_Detail_ID " + "\n"
        + "     left outer join P_Salary_Scale salaryScale on salaryScale.P_Salary_Scale_ID = salaryScaleDetail.P_Salary_Scale_ID " + "\n"
        ;

       // + "  where  paymentGain.P_Employee_ID = " + this.augmentation.getP_Employee_ID() + "\n"
       // ;

        if ( this.augmentation.getP_Employee_ID() != 0 )
        {
            sql += "  where  P_Payment.P_Employee_ID = " + this.augmentation.getP_Employee_ID() + "\n";
        }
        else
        {
            sql += " where  P_Payment.P_Employee_ID in (select P_Employee_ID" + "\n"
            + "                                                  from P_Augmentation_Employee" + "\n"
            + "                                                 where P_Augmentation_ID = " + this.augmentation.getP_Augmentation_ID();
            
           if ( augmentation.getEmployeeStatus() != null )
           {
              if ( augmentation.getEmployeeStatus().equals( "A" ))
              		sql += " AND P_Employee.IsActive = 'Y' ";
              if ( augmentation.getEmployeeStatus().equals( "I" ))
              		sql += " AND P_Employee.IsActive = 'N' ";
           }
        	   
           sql += "                                               )" + "\n";
        }

        if ( this.augmentation.getP_Collective_Labour_Agr_ID() != 0 )
        {
            sql += " and  paymentGain.P_Collective_Labour_Agr_ID = " + this.augmentation.getP_Collective_Labour_Agr_ID() + "\n";
        }

        if ( this.augmentation.getScaleStartDate() != null )
        sql += 	
          "	   and (  ( salaryScale.ScaleDate is null or salaryScale.ScaleDate = " + DB.TO_DATE(this.augmentation.getScaleStartDate()) + ")" + "\n"	
        + "			or exists( " + "\n"
        + "				select 1 " + "\n"
        + "					from P_Augmentation_Correspondence " + "\n"
        + "				    where P_Augmentation_ID = " + this.augmentation.getP_Augmentation_ID() + "\n"
        + "					and P_Augmentation_Correspondence.P_Salary_Scale_Detail_ID = salaryScaleDetail.P_Salary_Scale_Detail_ID " + "\n"
        + "			   ) " + "\n"
        + "		   ) " + "\n"
        ;
        
        sql += 	
        "    and (   paymentGain.Day between  " + DB.TO_DATE(this.augmentation.getStartDate()) + " and  " + DB.TO_DATE(this.augmentation.getEndDate()) + ") \n"
//        + "    and (   paymentGain.StartDate between  " + DB.TO_DATE(this.augmentation.getStartDate()) + " and  " + DB.TO_DATE(this.augmentation.getEndDate()) + "\n"
//        + "         or paymentGain.EndDate between  " + DB.TO_DATE(this.augmentation.getStartDate()) + " and  " + DB.TO_DATE(this.augmentation.getEndDate()) + ")" + "\n"
        + "    and (   '" + this.augmentation.getAugmentationGroupType() + "' = 'A'" + "\n"
        + "         or (    '" + this.augmentation.getAugmentationGroupType() + "' = 'E'" + "\n"
        + "             and paymentGain.P_Job_Title_ID not in ( select P_Job_Title_ID" + "\n"
        + "                                                       from P_Augmentation_Job_Title" + "\n"
        + "                                                      where P_Augmentation_ID = " + this.augmentation.getP_Augmentation_ID()
        + "                                                   )" + "\n"
        + "            )" + "\n"
        + "         or (    '" + this.augmentation.getAugmentationGroupType() + "' = 'S'" + "\n"
        + "             and paymentGain.P_Job_Title_ID in (select P_Job_Title_ID" + "\n"
        + "                                                  from P_Augmentation_Job_Title" + "\n"
        + "                                                 where P_Augmentation_ID = " + this.augmentation.getP_Augmentation_ID()
        + "                                               )" + "\n"
        + "            )" + "\n"
        + "        )" 
        + " ORDER BY paymentGain.Day "
        ;
     
        return DB.prepareStatement(sql, null);
    }
    
    /**
     * Cette méthode retourne la liste des enregistrements nécessaire pour effectuer le
     * calcul de rétro sur une liste de groupe d'emploi
     */
    private PreparedStatement prepareGroup()
    {
        String sql
        = " select paymentGain.P_Payment_ID, " + "\n"
        + "        P_Payment.P_Employee_ID ," + "\n"
        + "        paymentGain.P_Payment_Gain_ID, " + "\n"
        + "        augmentationGainDetail.P_Gain_ID," + "\n"
        + "        paymentGain.P_Job_Title_ID, " + "\n"
        + "        paymentgain.P_Salary_scale_ID, " + "\n"
        + "        paymentgain.P_Salary_Scale_Detail_ID," + "\n"
        + "        augmentationGainDetail.GainType, " + "\n"
        + "        augmentationGainDetail.NewPercent, " + "\n"
        + "        augmentationGainDetail.IsIgnoringScale," + "\n"
        + "        augmentationGainDetail.ScalePercent, " + "\n"
        + "        augmentationGainDetail.OldPercent, " + "\n"
        + "        paymentGain.AmountCalc," + "\n"
        + "        isnull(paymentGain.Hourly_Rate,0) Hourly_Rate ," + "\n"
        + "        paymentGain.Day" + "\n"
        + "   from P_Payment_Gain paymentGain " + "\n"
        + "     inner join P_Payment on P_Payment.P_Payment_ID = paymentGain.P_Payment_ID and P_Payment.TimeSheetStatus = 'T' " + "\n"
        + "     inner join P_Augmentation on P_Augmentation.P_Augmentation_ID = " + this.augmentation.getP_Augmentation_ID() + "\n"    
        + "     inner join P_Augmentation_Gain augmentationGain on augmentationGain.P_Augmentation_Gain_ID = P_Augmentation.P_Augmentation_Gain_ID " + "\n"    
        + "     inner join P_Augmentation_Gain_Detail augmentationGainDetail on augmentationGainDetail.P_Augmentation_Gain_ID = augmentationGain.P_Augmentation_Gain_ID and augmentationGainDetail.P_Gain_ID = paymentGain.P_Gain_ID " + "\n"
        + "     left outer join P_Salary_Scale_Detail salaryScaleDetail on salaryScaleDetail.P_Salary_Scale_Detail_ID = paymentGain.P_Salary_Scale_Detail_ID " + "\n"
        + "     left outer join P_Salary_Scale salaryScale on salaryScale.P_Salary_Scale_ID = salaryScaleDetail.P_Salary_Scale_ID " + "\n"
        + "    where 1 = 1 "
/*        + "  where paymentGain.P_Job_Title_ID in ( select P_Job_Title_ID"
        + "                                          from P_Job_Title"
        + "                                         where P_Occupation_Group_ID = " + this.augmentation.getP_Occupation_Group_ID()
        + "                                      )"
*/        
        ;
        if ( this.augmentation.getP_Collective_Labour_Agr_ID() != 0 )
        {
            sql += " and  paymentGain.P_Collective_Labour_Agr_ID = " + this.augmentation.getP_Collective_Labour_Agr_ID() + "\n";
        }

        if ( augmentation.getEmployeeStatus() != null )
        {
           if ( augmentation.getEmployeeStatus().equals( "A" ))
           		sql += " AND P_Payment.P_Employee_ID IN ( Select P_Employee_ID FROM P_Employee WHERE P_Employee.IsActive = 'Y' ) ";
           if ( augmentation.getEmployeeStatus().equals( "I" ))
          		sql += " AND P_Payment.P_Employee_ID IN ( Select P_Employee_ID FROM P_Employee WHERE P_Employee.IsActive = 'N' ) ";
        }

        if ( augmentation.getP_Occupation_Group_ID() != 0 )
        {
      		sql += " AND P_Payment.P_Employee_ID IN ( Select P_Employee_ID FROM P_Employee WHERE P_Employee.P_Occupation_Group_ID = " +  augmentation.getP_Occupation_Group_ID() + " ) ";
        	
        }

        
        if ( this.augmentation.getScaleStartDate() != null )
        sql += 	"	   and (  ( salaryScale.ScaleDate is null or salaryScale.ScaleDate = " + DB.TO_DATE(this.augmentation.getScaleStartDate()) + ") \n"	
        + "			or exists( " + "\n"
        + "				select 1 " + "\n"
        + "					from P_Augmentation_Correspondence " + "\n"
        + "				    where P_Augmentation_ID = " + this.augmentation.getP_Augmentation_ID() + "\n"
        + "					and P_Augmentation_Correspondence.P_Salary_Scale_Detail_ID = salaryScaleDetail.P_Salary_Scale_Detail_ID " + "\n"
        + "			   ) " + "\n"
        + "		   ) " + "\n"
        ;
        sql += "    and (   paymentGain.Day between  " + DB.TO_DATE(this.augmentation.getStartDate()) + " and  " + DB.TO_DATE(this.augmentation.getEndDate()) + ") \n"
//      + "    and (   paymentGain.StartDate between  " + DB.TO_DATE(this.augmentation.getStartDate()) + " and  " + DB.TO_DATE(this.augmentation.getEndDate()) + "\n"
//      + "         or paymentGain.EndDate between  " + DB.TO_DATE(this.augmentation.getStartDate()) + " and  " + DB.TO_DATE(this.augmentation.getEndDate()) + ")" + "\n"
        + "    and (   '" + this.augmentation.getAugmentationGroupType() + "' = 'A'" + "\n"
        + "         or (    '" + this.augmentation.getAugmentationGroupType() + "' = 'E'" + "\n"
        + "             and paymentGain.P_Job_Title_ID not in ( select P_Job_Title_ID" + "\n"
        + "                                                       from P_Augmentation_Job_Title" + "\n"
        + "                                                      where P_Augmentation_ID = " + this.augmentation.getP_Augmentation_ID()
        + "                                                   )" + "\n"
        + "            )" + "\n"
        + "         or (    '" + this.augmentation.getAugmentationGroupType() + "' = 'S'" + "\n"
        + "             and paymentGain.P_Job_Title_ID in (select P_Job_Title_ID" + "\n"
        + "                                                  from P_Augmentation_Job_Title" + "\n"
        + "                                                 where P_Augmentation_ID = " + this.augmentation.getP_Augmentation_ID()
        + "                                               )" + "\n"
        + "            )" + "\n"
        + "        )" 
        + "    and (   '" + this.augmentation.getAugmentationEmployeeType() + "' = 'A'" + "\n"
        + "         or (    '" + this.augmentation.getAugmentationEmployeeType() + "' = 'E'" + "\n"
        + "             and P_Payment.P_Employee_ID not in ( select P_Employee_ID" + "\n"
        + "                                                       from P_Augmentation_Employee" + "\n"
        + "                                                      where P_Augmentation_ID = " + this.augmentation.getP_Augmentation_ID()
        + "                                                   )" + "\n"
        + "            )" + "\n"
        + "         or (    '" + this.augmentation.getAugmentationEmployeeType() + "' = 'S'" + "\n"
        + "             and P_Payment.P_Employee_ID in (select P_Employee_ID" + "\n"
        + "                                                  from P_Augmentation_Employee" + "\n"
        + "                                                 where P_Augmentation_ID = " + this.augmentation.getP_Augmentation_ID()
        + "                                               )" + "\n"
        + "            )" + "\n"
        + "        )" ;
        
        return DB.prepareStatement(sql, null);
    }
    
    /**
     * Cette méthode retourne la liste des enregistrements nécessaire pour effectuer le
     * calcul de rétro sur une liste de titre d'emploi
     */
    private PreparedStatement prepareJobTitle()
    {
        String sql
        = " select paymentGain.P_Payment_ID, " + "\n"
        	+ "        P_Payment.P_Employee_ID ," + "\n"
            + "        paymentGain.P_Payment_Gain_ID, " + "\n"
            + "        augmentationGainDetail.P_Gain_ID," + "\n"
            + "        paymentGain.P_Job_Title_ID, " + "\n"
            + "        paymentgain.P_Salary_scale_ID, " + "\n"
            + "        paymentgain.P_Salary_Scale_Detail_ID," + "\n"
            + "        augmentationGainDetail.GainType, " + "\n"
            + "        augmentationGainDetail.NewPercent, " + "\n"
            + "        augmentationGainDetail.IsIgnoringScale," + "\n"
            + "        augmentationGainDetail.ScalePercent, " + "\n"
            + "        augmentationGainDetail.OldPercent, " + "\n"
            + "        paymentGain.AmountCalc," + "\n"
            + "        isnull(paymentGain.Hourly_Rate,0) Hourly_Rate ," + "\n"
            + "        paymentGain.Day" + "\n"
            + "   from P_Payment_Gain paymentGain " + "\n"
            + "     inner join P_Payment on P_Payment.P_Payment_ID = paymentGain.P_Payment_ID and P_Payment.TimeSheetStatus = 'T' " + "\n"
            + "     inner join P_Augmentation on P_Augmentation.P_Augmentation_ID = " + this.augmentation.getP_Augmentation_ID() + "\n"    
            + "     inner join P_Augmentation_Gain augmentationGain on augmentationGain.P_Augmentation_Gain_ID = P_Augmentation.P_Augmentation_Gain_ID " + "\n"    
            + "     inner join P_Augmentation_Gain_Detail augmentationGainDetail on augmentationGainDetail.P_Augmentation_Gain_ID = augmentationGain.P_Augmentation_Gain_ID and augmentationGainDetail.P_Gain_ID = paymentGain.P_Gain_ID " + "\n"
            + "     left outer join P_Salary_Scale_Detail salaryScaleDetail on salaryScaleDetail.P_Salary_Scale_Detail_ID = paymentGain.P_Salary_Scale_Detail_ID " + "\n"
            + "     left outer join P_Salary_Scale salaryScale on salaryScale.P_Salary_Scale_ID = salaryScaleDetail.P_Salary_Scale_ID " + "\n"
            + "  where paymentGain.P_Job_Title_ID = " + this.augmentation.getP_Job_Title_ID() + "\n"
            ;

         	if ( this.augmentation.getP_Collective_Labour_Agr_ID() != 0 )
        	{
        		sql += " and  paymentGain.P_Collective_Labour_Agr_ID = " + this.augmentation.getP_Collective_Labour_Agr_ID() + "\n";
        	}

            if ( augmentation.getEmployeeStatus() != null )
            {
               if ( augmentation.getEmployeeStatus().equals( "A" ))
               		sql += " AND P_Payment.P_Employee_ID IN ( Select P_Employee_ID FROM P_Employee WHERE P_Employee.IsActive = 'Y' ) ";
               if ( augmentation.getEmployeeStatus().equals( "I" ))
              		sql += " AND P_Payment.P_Employee_ID IN ( Select P_Employee_ID FROM P_Employee WHERE P_Employee.IsActive = 'N' ) ";
            }

        
            if ( this.augmentation.getScaleStartDate() != null )
                sql += "	   and (  ( salaryScale.ScaleDate is null or salaryScale.ScaleDate = " + DB.TO_DATE(this.augmentation.getScaleStartDate()) + ") \n"	
            + "			or exists( " + "\n"
            + "				select 1 " + "\n"
            + "					from P_Augmentation_Correspondence " + "\n"
            + "				    where P_Augmentation_ID = " + this.augmentation.getP_Augmentation_ID() + "\n"
            + "					and P_Augmentation_Correspondence.P_Salary_Scale_Detail_ID = salaryScaleDetail.P_Salary_Scale_Detail_ID " + "\n"
            + "			   ) " + "\n"
            + "		   ) " + "\n"
            ;
            sql += "    and (   paymentGain.Day between  " + DB.TO_DATE(this.augmentation.getStartDate()) + " and  " + DB.TO_DATE(this.augmentation.getEndDate()) + ") \n"
//          + "    and (   paymentGain.StartDate between  " + DB.TO_DATE(this.augmentation.getStartDate()) + " and  " + DB.TO_DATE(this.augmentation.getEndDate()) + "\n"
//          + "         or paymentGain.EndDate between  " + DB.TO_DATE(this.augmentation.getStartDate()) + " and  " + DB.TO_DATE(this.augmentation.getEndDate()) + ")" + "\n"
            + "    and (   '" + this.augmentation.getAugmentationEmployeeType() + "' = 'A'" + "\n"
            + "         or (    '" + this.augmentation.getAugmentationEmployeeType() + "' = 'E'" + "\n"
            + "             and P_Payment.P_Employee_ID not in ( select P_Employee_ID" + "\n"
            + "                                                       from P_Augmentation_Employee" + "\n"
            + "                                                      where P_Augmentation_ID = " + this.augmentation.getP_Augmentation_ID()
            + "                                                   )" + "\n"
            + "            )" + "\n"
            + "         or (    '" + this.augmentation.getAugmentationEmployeeType() + "' = 'S'" + "\n"
            + "             and P_Payment.P_Employee_ID in (select P_Employee_ID" + "\n"
            + "                                                  from P_Augmentation_Employee" + "\n"
            + "                                                 where P_Augmentation_ID = " + this.augmentation.getP_Augmentation_ID()
            + "                                               )" + "\n"
            + "            )" + "\n"
            + "        )" ;
        return DB.prepareStatement(sql, null);
    }
    
    /**
     * Cette méthode trouve le pourcentage d'écart entre le nouvel échelon salarial et
     * l'ancien. Elle trouve également le nouveau taux horaire.
     * @return new BigDecimal[] {percent, hourlyRate}
     */
    
    /** Cache */
	private static CCache<String,BigDecimal[]>	s_cache = new CCache<String,BigDecimal[]>("P_RetroCalculation", 20);


    private BigDecimal[] findPercentage(int P_Employee_ID, int jobTitleId, int salaryScaleDetailId, int newSalaryScaleDetailId) throws Exception
    {
        String key = jobTitleId + "-" + salaryScaleDetailId + "-" + newSalaryScaleDetailId;
        if ( s_cache.containsKey( key ) )
        	return (BigDecimal[]) s_cache.get( key );
        
    	BigDecimal percent = null;
        BigDecimal hourlyRate = null;
        
        String sql
        = "select jobTitle.Remuneration_Method, jobTitle.Weekly_Hours,"
            + " isnull(oldScale.Annual_Salary, 0) as oldAnnualSalary, isnull(oldScale.Hourly_Rate, 0) as oldHourlyRate,"
            + " isnull(newScale.Annual_Salary, 0) as newAnnualSalary, isnull(newScale.Hourly_Rate, 0) as newHourlyRate"
            + " from P_Job_Title jobTitle, P_Salary_Scale_Detail oldScale, P_Salary_Scale_Detail newScale"
            + " where jobTitle.P_Job_Title_ID = " + jobTitleId
            + " and oldScale.P_Salary_Scale_Detail_ID = " + salaryScaleDetailId
            + " and newScale.P_Salary_Scale_Detail_ID = " + newSalaryScaleDetailId;
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next())
        {
            // On calcul les écarts et le taux horaire dépendemment du mode
            // de rémunération
            try
            {
            	BigDecimal Annual_Increase = null;
        		Annual_Increase = P_Payment_Group.getAnnual_Increase( Env.getCtx(), P_Employee_ID, null);

                if(rs.getString("Remuneration_Method").equals("annual"))
                {
                    percent = (rs.getBigDecimal("newAnnualSalary").divide(rs.getBigDecimal("oldAnnualSalary"), 8,BigDecimal.ROUND_HALF_UP).subtract(this.ONE)).setScale(4,BigDecimal.ROUND_HALF_UP);
                    hourlyRate = (rs.getBigDecimal("newAnnualSalary").divide( Annual_Increase, 8, BigDecimal.ROUND_HALF_UP ).divide(rs.getBigDecimal("Weekly_Hours"), 8, BigDecimal.ROUND_HALF_UP)).setScale(4,BigDecimal.ROUND_HALF_UP);
                }
                else
                {
                    percent = rs.getBigDecimal("newHourlyRate").divide(rs.getBigDecimal("oldHourlyRate"), 4).subtract(this.ONE);
                    hourlyRate = rs.getBigDecimal("newHourlyRate");
                }
            }
            catch (Exception e) // Division par zéro
            {
                log.log(Level.SEVERE, "findPercentage ", e);
                return new BigDecimal[] {Env.ZERO, Env.ZERO};
            }
        }
        else
        {
            throw new SQLException("Aucun enregistrement : " + sql);
        }
        
        rs.close();
        stmt.close();
        
        s_cache.put(key, new BigDecimal[] {percent, hourlyRate});

        
        return new BigDecimal[] {percent, hourlyRate};
    }
    
    /**
     * Cette méthode sert à trouver la correspondance pour le titre d'emploi, la classe salariale
     * et l'échelon selon les paramètres d'augmentation courrant. S'il n'y a aucune correspondance
     * de définie dans la table, on retourne les mêmes valeurs que celles passées en paramètres.
     * @return new int[] {jobTitleId, salaryClassId, salaryScaleDetailId}
     */
    private int[] findCorrespondance(int jobTitleId, int salaryScaleDetailId, Timestamp transactionDate) throws Exception
    {
        String sql
        = "select P_Job_Title_New_ID, P_Salary_Scale_Detail_New_ID"
            + " from P_Augmentation_Correspondence"
            + " where P_Augmentation_ID = " + this.augmentation.getP_Augmentation_ID()
            + " and P_Job_Title_ID = " + jobTitleId
            + " and P_Salary_Scale_Detail_ID = " + salaryScaleDetailId
            + " and StartDate <= " + DB.TO_DATE(transactionDate)
            + " and EndDate >= " + DB.TO_DATE(transactionDate)
            + " and IsWithPct = 'N'";
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        // Si on a trouvé une correspondance, on se prépare à retourner ces valeurs. Sinon
        // on ne passe pas dans ce if et les valeurs qui seront retournées sont celles passées
        // en paramètres.
        if(rs.next())
        {
            jobTitleId = rs.getInt("P_Job_Title_New_ID");
            salaryScaleDetailId = rs.getInt("P_Salary_Scale_Detail_New_ID");
        }
        else
        {
            // Si on n'a pas trouvé de correspondence, on doit en faire une avec la date
            // de fin des paramètres d'augmentation
            sql = " select salaryScaleDetail.P_Salary_Scale_Detail_ID"
                + "   from P_Salary_Scale salaryScale inner join P_Salary_Scale_Detail salaryScaleDetail"
                + "     on salaryScale.P_Salary_Scale_ID = salaryScaleDetail.P_Salary_Scale_ID"
                + "  where salaryScale.ScaleDate = " + DB.TO_DATE(this.augmentation.getScaleEndDate())
                + "    and exists ( select 1"
                + "                   from P_Salary_Scale inner join P_Salary_Scale_Detail"
                + "                     on P_Salary_Scale.P_Salary_Scale_ID = P_Salary_Scale_Detail.P_Salary_Scale_ID"
                + "                  where P_Salary_Scale_Detail.P_Salary_Scale_Detail_ID = " + salaryScaleDetailId
                + "                    and P_Salary_Scale.P_Salary_Class_ID = salaryScale.P_Salary_Class_ID"
                + "                    and P_Salary_Scale.P_Job_Title_ID = salaryScale.P_Job_Title_ID"
                + "                    and P_Salary_Scale_Detail.Step = salaryScaleDetail.Step"
                + "        )";
            
            PreparedStatement stmt2 = DB.prepareStatement(sql, null);
            ResultSet rs2 = stmt2.executeQuery();
            
            if(rs2.next())
            {
                salaryScaleDetailId = rs2.getInt("P_Salary_Scale_Detail_ID");
            }
            
            rs2.close();
            stmt2.close();
        }
        	        
        rs.close();
        stmt.close();
        
        return new int[] {jobTitleId, salaryScaleDetailId};
    }

//1>
    private BigDecimal findCorrespondancePercent( Timestamp transactionDate) throws Exception
    {
        BigDecimal percent = null;

        String sql
        = "select AUGPRCCAD "
            + " from P_Augmentation_Correspondence"
            + " where P_Augmentation_ID = " + this.augmentation.getP_Augmentation_ID()
            + " and StartDate <= " + DB.TO_DATE(transactionDate)
            + " and EndDate >= " + DB.TO_DATE(transactionDate)
            + " and IsWithPct = 'Y'";
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next()  )
        {
           percent = rs.getBigDecimal("AUGPRCCAD");   
        }
        	        
        rs.close();
        stmt.close();
        
        return percent;
    }
//1<

}
