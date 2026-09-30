/*
 * Created on 2005-09-13
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

import solstice.model.P_Deduction_Account;

/**
 * @author frafor01
 * 
 * Cette classe sert à générer les enregistrements dans la table P_Gain_Account
 * pour la déduction d'où provient l'appel.
 */
public class P_Deduction_Account_Generation extends SvrProcess
{
    /*
     * Members
     */
    private int jobTypeId = 0;
    private int jobTitleId = 0;
    private int occupationGroupId = 0;
    private int accountId;
    
    /**
     * Constructeur
     */
    public P_Deduction_Account_Generation()
    {
        super();
    }

    /**
     * On récupère les paramètres de lancement
     */
    protected void prepare()
    {
		ProcessInfoParameter[] para = getParameter();
		for(int i = 0; i < para.length; i++)
		{
		    if(para[i].getParameterName().equals("P_Job_Title_ID"))
		    {
		        this.jobTitleId = para[i].getParameterAsInt();
		    }
		    else if(para[i].getParameterName().equals("P_Job_Type_ID"))
		    {
		        this.jobTypeId = para[i].getParameterAsInt();
		    }
		    else if(para[i].getParameterName().equals("P_Occupation_Group_ID"))
		    {
		        this.occupationGroupId = para[i].getParameterAsInt();
		    }
		    else if(para[i].getParameterName().equals("C_ElementValue_ID"))
		    {
		        this.accountId = para[i].getParameterAsInt();
		    }
		}
    }

    /**
     * On doit générer les enregistrements selon les paramètre de lancement
     */
    protected String doIt() throws Exception
    {
        // On génère d'abord la liste des enregistrements à créer
        // à partir des paramètres
        String sql
        = "select occupationGroup.P_Occupation_Group_ID, jobTitle.P_Job_Title_ID, jobType.P_Job_Type_ID"
            + " from P_Occupation_Group occupationGroup, P_Job_Title jobTitle, P_Job_Type jobType"
            + this.getWhere();
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next())
            {
                // On crée les enregistrements
                P_Deduction_Account deductionAccount = new P_Deduction_Account(Env.getCtx(), -1, null);
                deductionAccount.setP_Deduction_ID(this.getRecord_ID());
                deductionAccount.setP_Deduction_Acct(this.accountId);
                deductionAccount.setP_Occupation_Group_ID(rs.getInt("P_Occupation_Group_ID"));
                deductionAccount.setP_Job_Title_ID(rs.getInt("P_Job_Title_ID"));
                deductionAccount.setP_Job_Type_ID(rs.getInt("P_Job_Type_ID"));
                deductionAccount.save();
            }
            
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            log.log(Level.SEVERE, "doIt", e);
        }
        
        return Msg.translate(Env.getLanguage(Env.getCtx()), "Success");
    }
    
    /**
     * Construit le where dépendemment des paramètres de lancement
     */
    private String getWhere()
    {
        String where = "";
        if(this.jobTitleId > 0)
        {
            where = " where jobTitle.P_Job_Title_ID = " + this.jobTitleId;
        }
        if(this.jobTypeId > 0)
        {
            if(where.length() == 0)
                where += " where ";
            else
                where += " and ";
            where += "jobType.P_Job_Type_ID = " + this.jobTypeId;
        }
        if(this.occupationGroupId > 0)
        {
            if(where.length() == 0)
                where += " where ";
            else
                where += " and ";
            where += "occupationGroup.P_Occupation_Group_ID = " + this.occupationGroupId;
        }
        return where;
    }
}
