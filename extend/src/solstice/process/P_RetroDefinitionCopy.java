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

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import solstice.model.P_Augmentation;
import solstice.model.P_Augmentation_Employee;
import solstice.model.P_Augmentation_Job_Title;

/**
 * @author frafor01
 *
 * Cette classe représente un processus qui sert à copier
 * la définition de la paramétrisation d'un calcul de rétro
 */
public class P_RetroDefinitionCopy extends SvrProcess
{
    private Timestamp augmentationDate = null;
    private String value = null;
    private String augmentationType = null;
    private int jobTitleId = 0;
    private int employeeId = 0;
    private int occupationGroupId = 0;
    private int collectiveLabourId = 0;
    
    public P_RetroDefinitionCopy()
    {
        super();
    }

    /**
     * Récupération des paramètres
     */
    protected void prepare()
    {
        ProcessInfoParameter[] parameters = this.getParameter();
        for(int i = 0; i < parameters.length; i++)
        {
            String parameterName = parameters[i].getParameterName();
            if(parameterName.equals("AugmentationType"))
                this.augmentationType = parameters[i].getParameter().toString();
            else if(parameterName.equals("AugmentationDate"))
                this.augmentationDate = (Timestamp)parameters[i].getParameter();
            else if(parameterName.equals("P_Job_Title_ID"))
                this.jobTitleId = parameters[i].getParameterAsInt();
            else if(parameterName.equals("P_Employee_ID"))
                this.employeeId = parameters[i].getParameterAsInt();
            else if(parameterName.equals("P_Occupation_Group_ID"))
                this.occupationGroupId = parameters[i].getParameterAsInt();
            else if(parameterName.equals("P_Collective_Labour_Agr_ID"))
                this.collectiveLabourId = parameters[i].getParameterAsInt();
        }
    }

    /**
     * Traitement principal : On doit copier les enregistrement des tables
     * P_Augmentation, 
     * P_Augmentation_Employee, P_Augmentation_Job_Title et
     * P_Augmentation_Correspondence
     */
    protected String doIt() throws Exception
    {
        try
        {
            // On copie les enregistrements des tables
            int augmentationId = this.copyAugmentation();
//            this.copyAugmentationGain(augmentationId);
            this.copyAugmentationEmployee(augmentationId);
            this.copyAugmentationJobTitle(augmentationId);
//            this.copyAugmentationCorrespondence(augmentationId);
            return Msg.translate(Env.getAD_Language(Env.getCtx()), "Success") + "\nNouvel enregistrement : " + this.value;
        }
        catch (SQLException e)
        {
            return Msg.translate(Env.getAD_Language(Env.getCtx()), "ProcessFailed");
        }
    }
    
    /**
     * Copie de la table P_Augmentation
     */
    private int copyAugmentation() throws SQLException
    {
        // On récupère d'abord les informations de l'enregistrement
        // d'augmentation associé au id contenue dans la méthode getRecord_ID()
        String sql;
        sql = " select * "
            + "   from P_Augmentation"
            + "  where P_Augmentation_ID = " + this.getRecord_ID();
        
        int augmentationId = 0;
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next())
        {
            // On crée la copie
            P_Augmentation augmentation = new P_Augmentation(Env.getCtx(), -1, null);
            
            augmentation.setP_Job_Title_ID(this.jobTitleId > 0
                    ? this.jobTitleId : rs.getInt("P_Job_Title_ID"));

            augmentation.setP_Employee_ID(this.employeeId > 0
                    ? this.employeeId : rs.getInt("P_Employee_ID"));

            augmentation.setP_Occupation_Group_ID(this.occupationGroupId > 0
                    ? this.occupationGroupId : rs.getInt("P_Occupation_Group_ID"));
            
            
            //augmentation.setP_Collective_Labour_Agr_ID(this.collectiveLabourId > 0
            //       ? this.collectiveLabourId : rs.getInt("P_Collective_Labour_Agr_ID"));
            
            augmentation.setAugmentationDate(this.augmentationDate != null
                    ? this.augmentationDate : rs.getTimestamp("AugmentationDate"));

            augmentation.setAugmentationType(this.augmentationType != null
                    ? this.augmentationType : rs.getString("AugmentationType"));

            if ( rs.getInt("P_Collective_Labour_Agr_ID") != 0 )
            	augmentation.setP_Collective_Labour_Agr_ID( rs.getInt("P_Collective_Labour_Agr_ID") ); 
            
            augmentation.setAD_Org_ID( rs.getInt("AD_Org_ID"));
            augmentation.setDescription(rs.getString("Description"));
            augmentation.setStartDate(rs.getTimestamp("StartDate"));
            augmentation.setEndDate(rs.getTimestamp("EndDate"));
            augmentation.setScaleStartDate(rs.getTimestamp("ScaleStartDate"));
            augmentation.setScaleEndDate(rs.getTimestamp("ScaleEndDate"));
            augmentation.setAugmentationGroupType(rs.getString("AugmentationGroupType"));
            augmentation.setAugmentationEmployeeType(rs.getString("AugmentationEmployeeType"));
            augmentation.setAugPrcCad(rs.getBigDecimal("AugPrcCad"));
            augmentation.setP_Augmentation_Gain_ID( rs.getInt("P_Augmentation_Gain_ID"));
            
            if(augmentation.save())
            {
                this.value = augmentation.getValue();
                augmentationId = augmentation.getP_Augmentation_ID();
            }
            else
            {
                rs.close();
                stmt.close();
                throw new SQLException("sauvegarde de la table P_Augmentation échouée");
            }
        }
        
        rs.close();
        stmt.close();
        
        return augmentationId;
    }

    /**
     * Copie de la table P_Augmentation_Employee
     */
    private void copyAugmentationEmployee(int augmentationId) throws SQLException
    {
        // On récupère la liste des enregistrements associés à l'augmentation courrante
        String sql
        = "select P_Employee_ID"
            + " from P_Augmentation_Employee"
            + " where P_Augmentation_ID = " + this.getRecord_ID();
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // Création du nouvel enregistrement
            P_Augmentation_Employee augmentationEmployee = new P_Augmentation_Employee(Env.getCtx(), -1, null);
            augmentationEmployee.setP_Augmentation_ID(augmentationId);
            augmentationEmployee.setP_Employee_ID(rs.getInt("P_Employee_ID"));
            
            if(!augmentationEmployee.save())
            {
                rs.close();
                stmt.close();
                throw new SQLException("sauvegarde de la table P_Augmentation_Employee échouée");
            }
        }
        
        rs.close();
        stmt.close();
    }

    /**
     * Copie des informations de la table P_Augmentation_Job_Title
     */
    private void copyAugmentationJobTitle(int augmentationId) throws SQLException
    {
        // On récupère la liste des enregistrements associés à l'augmentation courrante
        String sql
        = "select P_Job_Title_ID"
            + " from P_Augmentation_Job_Title"
            + " where P_Augmentation_ID = " + this.getRecord_ID();
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // Création du nouvel enregistrement
            P_Augmentation_Job_Title augmentationJobTitle = new P_Augmentation_Job_Title(Env.getCtx(), -1, null);
            augmentationJobTitle.setP_Augmentation_ID(augmentationId);
            augmentationJobTitle.setP_Job_Title_ID(rs.getInt("P_Job_Title_ID"));
            
            if(!augmentationJobTitle.save())
            {
                rs.close();
                stmt.close();
                throw new SQLException("sauvegarde de la table P_Augmentation_Job_Title échouée");
            }
        }
        
        rs.close();
        stmt.close();
    }

    /**
     * Copie des informations de la table P_Augmentation_Correspondence
     */
    /*
    private void copyAugmentationCorrespondence(int augmentationId) throws SQLException
    {
        // On récupère la liste des enregistrements associés à l'augmentation courrante
        String sql;
        sql = " select P_Job_Title_ID,"
            + "        P_Salary_Class_ID,"
            + "        P_Salary_Scale_ID,"
            + "        P_Salary_Scale_Detail_ID,"
            + "        P_Job_Title_New_ID,"
            + "        P_Salary_Class_New_ID,"
            + "        P_Salary_Scale_New_ID,"
            + "        P_Salary_Scale_Detail_New_ID,"
            + "        StartDate,"
            + "        EndDate,"
            + "        AugPrcCad "
            + "   from P_Augmentation_Correspondence"
            + "  where P_Augmentation_ID = " + this.getRecord_ID();
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // Création du nouvel enregistrement
            P_Augmentation_Correspondence augmentationCorrespondence = new P_Augmentation_Correspondence(Env.getCtx(), -1, null);
            augmentationCorrespondence.setP_Augmentation_ID(augmentationId);
            augmentationCorrespondence.setP_Job_Title_ID(rs.getInt("P_Job_Title_ID"));
            augmentationCorrespondence.setP_Salary_Class_ID(rs.getInt("P_Salary_Class_ID"));
            augmentationCorrespondence.setP_Salary_Scale_ID(rs.getInt("P_Salary_Scale_ID"));
            augmentationCorrespondence.setP_Salary_Scale_Detail_ID(rs.getInt("P_Salary_Scale_Detail_ID"));
            augmentationCorrespondence.setP_Job_Title_New_ID(rs.getInt("P_Job_Title_New_ID"));
            augmentationCorrespondence.setP_Salary_Class_New_ID(rs.getInt("P_Salary_Class_New_ID"));
            augmentationCorrespondence.setP_Salary_Scale_New_ID(rs.getInt("P_Salary_Scale_New_ID"));
            augmentationCorrespondence.setP_Salary_Scale_Detail_New_ID(rs.getInt("P_Salary_Scale_Detail_New_ID"));
            augmentationCorrespondence.setStartDate(rs.getTimestamp("StartDate"));
            augmentationCorrespondence.setEndDate(rs.getTimestamp("EndDate"));
            augmentationCorrespondence.setAugPrcCad(rs.getBigDecimal("AugPrcCad"));
            augmentationCorrespondence.setIsWithPct(rs.getString("IsWithPct"));
            augmentationCorrespondence.setIsActive( true);
            if(!augmentationCorrespondence.save())
            {
                rs.close();
                stmt.close();
                throw new SQLException("sauvegarde de la table P_Augmentation_Correspondence échouée");
            }
        }
        
        rs.close();
        stmt.close();
    }
    */
}
