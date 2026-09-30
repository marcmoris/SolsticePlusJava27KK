/*
 * Created on 2005-09-30
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_Augmentation_Payment extends X_P_Augmentation_Payment
{

    /**
     * @param ctx
     * @param P_Augmentation_Payment_ID
     * @param trxName
     */
    public P_Augmentation_Payment(Properties ctx,
            int P_Augmentation_Payment_ID, String trxName)
    {
        super(ctx, P_Augmentation_Payment_ID, trxName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param ctx
     * @param rs
     * @param trxName
     */
    public P_Augmentation_Payment(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
        // TODO Auto-generated constructor stub
    }

    /**
     * Cette méthode sert à s'assurer que le nom d'utilisateur
     * corresponde au montant modifié
     */
    protected boolean beforeSave(boolean newRecord)
    {
        // S'il ne s'agit pas d'un nouvel enregistrement, c'est
        // que cette sauvegarde a été faite par l'utilisateur
        // et non par la procédure de calcul de la rétroactivité
        if(!newRecord)
        {
            // On doit vérifier si le montant a changé
            String sql
            = "select 1"
                + " from P_Augmentation_Payment"
                + " where P_Augmentation_Payment_ID = " + this.getP_Augmentation_Payment_ID()
                + " and (AmountModified is null"
                + "      or AmountModified <> " + this.getAmountModified().toString() + ")";
            
            try
            {
                PreparedStatement stmt = DB.prepareStatement(sql, null);
                ResultSet rs = stmt.executeQuery();
                
                // Si le montant a été modifié
                if(rs.next())
                {
                    // On change le nom de l'utilisateur
                    this.setUserName(this.userName());
                }
            }
            catch(SQLException e)
            {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Cette méthode retourne le nom de l'utilisateur connecté
     */
    private String userName() throws SQLException
    {
        String sql = "select Name from AD_User where AD_User_ID = " + Env.getAD_User_ID(Env.getCtx());
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        String user = rs.getString("Name");
        rs.close();
        stmt.close();
        return user;
    }
}
