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

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 * @author frafor01
 * 
 * Cette classe définit un processus pour supprimer les résultats
 * de la rétro pour l'enregistrement de P_Augmentation passé en paramètre
 * dans le record_ID
 */
public class P_RetroResultDestruction extends SvrProcess
{
    public P_RetroResultDestruction()
    {
        super();
    }

    /**
     * Récupération des paramètres
     */
    protected void prepare()
    {
        // Il n'y a aucun paramètre à récupérer
    }

    /**
     * Procédure principale : On supprime les résultats et la table de calcul
     * de la rétroactivité
     */
    protected String doIt() throws Exception
    {
    	
        String sql = "Select isAugmentationResult From P_Augmentation_Result WHERE P_Augmentation_ID = " + this.getRecord_ID();
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        while(rs.next())
        {
           // Si le résultat a été générer sur la feuille de temps, il n'est plus possible de détruire.
           // Si l'usager veux vraiment détruire les résultats il doit aller indiquer dans le résultat que l'information n'est plus sur une feuille de temps.
           // Record not deleted - dependent record found
           if ( rs.getString("isAugmentationResult").equals("Y"))
           {
	            rs.close();
	            stmt.close();
        	    return Msg.translate(Env.getAD_Language(Env.getCtx()), "DeleteErrorDependent");
           }
        }
        rs.close();
        stmt.close();

        // Suppression de la table P_Augmentation_Result
        DB.executeUpdate(
                "delete from P_Augmentation_Result" +
        		" where P_Augmentation_ID = " + this.getRecord_ID(), null);
        // Suppression de la table P_Augmentation_Payment
        DB.executeUpdate(
                "delete from P_Augmentation_Payment" +
                " where P_Augmentation_ID = " + this.getRecord_ID(), null);
        
        return Msg.translate(Env.getAD_Language(Env.getCtx()), "Success");
    }
}
