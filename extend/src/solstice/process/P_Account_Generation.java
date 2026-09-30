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
import solstice.model.P_Credits_Account;
import solstice.model.P_Deduction_Account;
import solstice.model.P_Gain_Account;

/**
 * @author frafor01
 * 
 * Cette classe sert à générer les enregistrements dans la table P_Gain_Account
 * pour la déduction d'où provient l'appel.
 */
public class P_Account_Generation extends SvrProcess
{
    /*
     * Members
     */
    private int jobTypeId = 0;
    private int jobTitleId = 0;
    private int occupationGroupId = 0;
    private int accountId;
    private int orgId;
    private int ActivitySrcId;
    private int ActivityId;
    private int SalesRegionId;
    private String className;
   
    
    /**
     * Constructeur
     */
    public P_Account_Generation()
    {
        super();
    }

    /**
     * On récupère les paramètres de lancement
     */
    protected void prepare()
    {
		ProcessInfoParameter[] para = getParameter();
		if (Env.getAD_Client_ID(Env.getCtx()) == 11)
		{
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
		else
		{
			for(int i = 0; i < para.length; i++)
			{
				if(para[i].getParameterName().equals("AD_Org_ID"))
			    {
			        this.orgId = para[i].getParameterAsInt();
			    } 
				else if (para[i].getParameterName().equals("C_ElementValue_ID"))
			    {
			    	this.accountId = para[i].getParameterAsInt();
			    }
				else if (para[i].getParameterName().equals("C_Activity_Src_ID"))
				{
					this.ActivitySrcId = para[i].getParameterAsInt();
				}
				else if (para[i].getParameterName().equals("C_Activity_ID"))
				{
					this.ActivityId = para[i].getParameterAsInt();
				}
				else if (para[i].getParameterName().equals("C_Sales_Region_ID"))
				{
					this.SalesRegionId = para[i].getParameterAsInt();
				}
			}
			
		}
		
			//get the class names from the Table ID
		if(this.getTable_ID() == 2000153)
			className = "P_Deduction_Account";
		else if (this.getTable_ID() == 2000005)
			className = "P_Gain_Account";
		else if (this.getTable_ID() == 2000056)
			className = "P_Credits_Account";

    }

    /**
     * On doit générer les enregistrements selon les paramètre de lancement
     */
    protected String doIt() throws Exception
    {
    	
        // On génère d'abord la liste des enregistrements à créer
        // à partir des paramètres
    	
    	// SIQ
    	if (Env.getAD_Client_ID(Env.getCtx()) == 11)
    	{
    		String sql
            = "select P_Occupation_Group.P_Occupation_Group_ID, P_Job_Title.P_Job_Title_ID, P_Job_Type.P_Job_Type_ID"
                + " from P_Occupation_Group , P_Job_Title,  P_Job_Type"
                + this.getWhere();
            
            try
            {
                PreparedStatement stmt = DB.prepareStatement(sql, null);
                ResultSet rs = stmt.executeQuery();
                
                while(rs.next())
                {
                    // On crée les enregistrements
                	if (className.equals("P_Deduction_Account"))
                	{
                		P_Deduction_Account deductionAccount = null;
                		deductionAccount = P_Deduction_Account.get(Env.getCtx(), this.getRecord_ID(), rs.getInt("P_Job_Title_ID"), rs.getInt("P_Job_Type_ID"), null);
                		if ( deductionAccount == null)
                			deductionAccount = new P_Deduction_Account(Env.getCtx(), -1, null);
                		deductionAccount.setP_Deduction_ID(this.getRecord_ID());
                        deductionAccount.setP_Deduction_Acct(this.accountId);
                        deductionAccount.setP_Occupation_Group_ID(rs.getInt("P_Occupation_Group_ID"));
                        deductionAccount.setP_Job_Title_ID(rs.getInt("P_Job_Title_ID"));
                        deductionAccount.setP_Job_Type_ID(rs.getInt("P_Job_Type_ID"));
                        deductionAccount.save();
                	}
                	else if (className.equals("P_Gain_Account"))
                	{
                		P_Gain_Account gainAccount = null;
                		
                		gainAccount = P_Gain_Account.get(Env.getCtx(), this.getAD_Client_ID(), this.getRecord_ID(), rs.getInt("P_Occupation_Group_ID"), rs.getInt("P_Job_Type_ID"), rs.getInt("P_Job_Title_ID"), null); 
                		if ( gainAccount == null)
                			gainAccount = new P_Gain_Account(Env.getCtx(), -1, null);
                        gainAccount.setP_Gain_ID(this.getRecord_ID());
                        gainAccount.setC_Activity_ID(this.ActivityId);
                        gainAccount.setP_Gain_Acct(this.accountId);
                        gainAccount.setP_Occupation_Group_ID(rs.getInt("P_Occupation_Group_ID"));
                        gainAccount.setP_Job_Title_ID(rs.getInt("P_Job_Title_ID"));
                        gainAccount.setP_Job_Type_ID(rs.getInt("P_Job_Type_ID"));
                        gainAccount.save();
                	}
                	else if(className.equals("P_Credits_Account"))
                	{
                		P_Credits_Account creditsAccount = null;
                		
                		creditsAccount = P_Credits_Account.get(Env.getCtx(), this.getRecord_ID(), rs.getInt("P_Occupation_Group_ID"), rs.getInt("P_Job_Title_ID"), rs.getInt("P_Job_Type_ID"), null);
                		if ( creditsAccount == null )
                			creditsAccount = new P_Credits_Account(Env.getCtx(), -1, null);
                        creditsAccount.setP_Credits_ID(this.getRecord_ID());
                        creditsAccount.setP_Credits_Acct(this.accountId);
                        creditsAccount.setP_Occupation_Group_ID(rs.getInt("P_Occupation_Group_ID"));
                        creditsAccount.setP_Job_Title_ID(rs.getInt("P_Job_Title_ID"));
                        creditsAccount.setP_Job_Type_ID(rs.getInt("P_Job_Type_ID"));
                        creditsAccount.save();
                	}
                    
                }
                
                rs.close();
                stmt.close();
            }
            catch (SQLException e)
            {
                log.log(Level.SEVERE, "doIt", e);
            }
    	}
    	// Helico
    	else
    	{
    		
    		String sql  = "SELECT AD_Org.AD_Org_ID, C_Activity.C_Activity_ID  FROM AD_Org, C_Activity "
    			+ this.getWhere();

    		try
            {
                PreparedStatement stmt = DB.prepareStatement(sql, null);
                ResultSet rs = stmt.executeQuery();
                
                while(rs.next())
                {
                    // On crée les enregistrements
                	if (className.equals("P_Deduction_Account"))
                	{
                		P_Deduction_Account deductionAccount = null;
                		
                		deductionAccount = P_Deduction_Account.get(Env.getCtx(), this.getAD_Client_ID(), rs.getInt("Ad_Org_ID"), rs.getInt("C_Activity_ID"), this.getRecord_ID(), null);
                		if ( deductionAccount == null)
                			deductionAccount = new P_Deduction_Account(Env.getCtx(), -1, null);
                        deductionAccount.setP_Deduction_ID(this.getRecord_ID());
                        deductionAccount.setP_Deduction_Acct(this.accountId);
                        deductionAccount.setAD_Org_ID(rs.getInt("Ad_Org_ID"));
                        deductionAccount.setC_Activity_Src_ID(rs.getInt("C_Activity_ID"));
                        deductionAccount.setC_SalesRegion_ID(this.SalesRegionId);
                        deductionAccount.setC_Activity_ID(this.ActivityId);
                        deductionAccount.save();
                	}
                	else if(className.equals("P_Gain_Account"))
                	{
                		P_Gain_Account gainAccount = null;
                		gainAccount = P_Gain_Account.get(Env.getCtx(), this.getAD_Client_ID(), rs.getInt("Ad_Org_ID"), rs.getInt("C_Activity_ID"), this.getRecord_ID(), null); 
                		if ( gainAccount == null)
                			gainAccount = new P_Gain_Account(Env.getCtx(), -1, null);
                		gainAccount.setP_Gain_ID(this.getRecord_ID());
                		gainAccount.setP_Gain_Acct(this.accountId);
                		gainAccount.setAD_Org_ID(rs.getInt("Ad_Org_ID"));
                		gainAccount.setC_Activity_Src_ID(rs.getInt("C_Activity_ID"));
                		gainAccount.setC_SalesRegion_ID(this.SalesRegionId);
                		gainAccount.setC_Activity_ID(this.ActivityId);
                		gainAccount.save();
                	}
                	else if (className.equals("P_Credits_Account"))
                	{
                		P_Credits_Account creditsAccount = null;
                		
                		creditsAccount = P_Credits_Account.get(Env.getCtx(), this.getRecord_ID(), this.getAD_Client_ID(), rs.getInt("Ad_Org_ID"), rs.getInt("C_Activity_ID"), null);
                		if ( creditsAccount == null )
                			creditsAccount = new P_Credits_Account(Env.getCtx(), -1, null);

                		creditsAccount.setP_Credits_ID(this.getRecord_ID());
                		creditsAccount.setP_Credits_Acct(this.accountId);
                		creditsAccount.setAD_Org_ID(rs.getInt("Ad_Org_ID"));
                		creditsAccount.setC_Activity_Src_ID(rs.getInt("C_Activity_ID"));
                		creditsAccount.setC_SalesRegion_ID(this.SalesRegionId);
                		creditsAccount.setC_Activity_ID(this.ActivityId);
                		creditsAccount.save();
                	}
                    
                }
                
                rs.close();
                stmt.close();
            }
                catch (SQLException e)
                {
                    log.log(Level.SEVERE, "doIt", e);
                }
    	}
        
        
        return Msg.translate(Env.getLanguage(Env.getCtx()), "Success");
    }
    
    /**
     * Construit le where dépendemment des paramètres de lancement
     */
    private String getWhere()
    {
    	String where = " Where 1=1 ";
    	if (Env.getAD_Client_ID(Env.getCtx()) == 11)
    	{
    		
            if(this.jobTitleId > 0)
            {
                where += " and P_Job_Title.P_Job_Title_ID = " + this.jobTitleId;
            }
            if(this.jobTypeId > 0)
            {
                where += " and ";
                where += "P_Job_Type.P_Job_Type_ID = " + this.jobTypeId;
            }
            if(this.occupationGroupId > 0)
            {
                where += " and ";
                where += "P_Occupation_Group.P_Occupation_Group_ID = " + this.occupationGroupId;
            }
    	}
    	
    	else
    	{
    		where += " and Ad_Org.AD_Org_ID != 0";
    		if(this.ActivitySrcId > 0)
            {
                where += " and ";
                where += " C_Activity.C_Activity_ID = " + this.ActivitySrcId;
            }
            if(this.orgId > 0)
            {
                where += " and ";
                where += "Ad_Org.AD_Org_ID = " + this.orgId;
            }
    	}
        
        return where;
    }
}
