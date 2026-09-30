/*
 * Created on 2005-09-29
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

/**
 * @author frafor01
 */
public class P_Augmentation extends X_P_Augmentation
{

    /**
     * @param ctx
     * @param P_Augmentation_ID
     * @param trxName
     */
    public P_Augmentation(Properties ctx, int P_Augmentation_ID, String trxName)
    {
        super(ctx, P_Augmentation_ID, trxName);
    }

    /**
     * @param ctx
     * @param rs
     * @param trxName
     */
    public P_Augmentation(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
    }

    /**
     * Avant de sauvegarder l'enregistrement, on doit obtenir la prochaine clé
     * de recherche dans la bd. Il s'agit d'un numéro basé sur le maximum des
     * code existant.
     */
    protected boolean beforeSave(boolean newRecord)
    {
        if(newRecord)
        {
	        // On recherche le prochain numéro dans la table
	        String sql = "select isnull(max(convert(decimal(10,0), Value)), 0) + 1 as Value from P_Augmentation";
	        int nextValue = 1;
	        
	        try
	        {
	            PreparedStatement stmt = DB.prepareStatement(sql, null);
	            ResultSet rs = stmt.executeQuery();
	            
	            if(rs.next())
	            {
	                nextValue = rs.getInt("Value");
	            }
	            
	            rs.close();
	            stmt.close();
	        }
	        catch (SQLException e)
	        {
	            return false;
	        }
	        
	        // On doit maintenant mettre ce code sur 6 chiffres en ajoutant des 0
	        // à gauche de celui-ci
	        String value = String.valueOf(nextValue);
	        while(value.length() < 6)
	            value = "0" + value;
	        
	        // Finalement, on affecte le nouveau code à l'enregistrement et on
	        // continue la sauvegarde
	        this.setValue(value);
        }
        
        return true;
    }
}
