/*
 * Created on 28 juil. 2005
 */
package solstice.custom;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author frafor01
 *
 * Interface pour la spécification et le formattage de colonne
 */
public interface IField
{
    /**
     * Retourne le &lt;TD&gt; pour l'entête de la colonne 
     */
    public String getTDLabel(String css);

    /**
     * Retourne la ou les cellules correspondantes à l'enregistrement courrant
     */
    public String getTDCell(String css, ResultSet rs, boolean selected) throws SQLException;
}