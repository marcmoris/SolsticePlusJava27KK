/*
 * Created on 2005-09-20
 */
package solstice.custom;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.jsp.JspWriter;

import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.P_Booklet_Status_Histo;
import solstice.process.PgiUtil;

/**
 * @author frafor01
 * Cette classe effectue les validations pour la page /livretTemps/approblist.jsp
 */
public class LivretTempsApprobationList extends ValidationBase
{
    
    /**
     * Cette classe sert à concerver l'id d'un livert de temps et le code de distribution
     */
    private class BookletDistribution
    {
        private int bookletId;
        private String distributionBooklet;
        private String value;
        
        /**
         * Constructeur
         * @param value concaténation de l'id avec le code de distribution séparé par |
         */
        public BookletDistribution(String value)
        {
            String[] temp = PgiUtil.split(value, '|');
            this.bookletId = Integer.parseInt(temp[0]);
            this.distributionBooklet = temp[1];
            this.value = value;
        }
        
        public int getBookletId() {return this.bookletId;}
        public String getDistributionBooklet() {return this.distributionBooklet;}
        
        public boolean equals(Object obj)
        {
            if(obj instanceof String)
            {
                return this.value.equals((String)obj);
            }
            else if(obj instanceof BookletDistribution)
            {
                return ( this.bookletId == ((BookletDistribution)obj).bookletId
                        && this.distributionBooklet.equals(((BookletDistribution)obj).distributionBooklet));
            }
            return false;
        }
    }
    
    public LivretTempsApprobationList()
    {
        super();
    }

    protected String[] doValidate() {return null;}

    /**
     * Gestion des résultats du post
     */
    public boolean afterValidation(JspWriter out)
    {
        String action;
        if((action = request.getParameter("action_save")) != null)
        {
            // Si l'utilisateur a choisi de sauvegarder...
            if(action.equals("save"))
            {
                // On récupère la checklist qui est de la forme suivante :
                // +000000-000000+000011+111222-111222
                // Chaque action des checkBox ont été compilées séquentiellement. Lorsque
                // l'utilisateur a sélectionné un checkBox, l'id du livret est précédé d'un
                // '+', sinon, d'un '-'. Il suffit donc simplement de parcourir cette séquence
                // pour savoir si en bout de ligne le livret a été approuvé.
                String checkList = request.getParameter("checkList");
                if(checkList.length() > 0)
                {
	                int index = 0;
	                Vector<BookletDistribution> vector = new Vector<BookletDistribution>();
	                while(index >= 0)
	                {
	                    // On obtient la prochaine coupure
	                    int next = realMin(
	                            checkList.indexOf("+", index+1),
	                            checkList.indexOf("-", index+1));
	                    
	                    BookletDistribution id;
	                    // Si on n'est pas rendu à la fin de la liste...
	                    if(next >= 0)
	                    {
	                        // ...on effectue un substring à deux bornes pour obtenir l'id
	                        id = new BookletDistribution(checkList.substring(index + 1, next));
	                    }
	                    else
	                    {
	                        // sinon, on effectue un substring jusqu'à la fin pour obtenir l'id
	                        id = new BookletDistribution(checkList.substring(index + 1));
	                    }
	                    
	                    // Si on veut ajouter le livret...
	                    if(checkList.charAt(index) == '+')
	                    {
	                        // ...on l'ajoute simplement au vector
	                        vector.add(id);
	                    }
	                    else
	                    {
	                        // sinon, on doit retirer l'élément du vector
	                        vector.remove(id);
	                    }
	                    
	                    index = next;
	                }
	                
	                // On doit maintenant repasser le vector qui contient les approbations finales
	                for(int i = 0; i < vector.size(); i++)
	                {
                        // On doit créer un enregistrement dans la table d'historique des statuts
                        // pour approuver le livret demandé
                        BookletDistribution id = (BookletDistribution)vector.elementAt(i);
                        P_Booklet_Status_Histo statusHisto = new P_Booklet_Status_Histo(Env.getCtx(), -1, null);
                        statusHisto.setP_Booklet_ID(id.bookletId);
                        statusHisto.setP_Distribution_Booklet_ID(getDistributionIdFromValue(id.distributionBooklet));
                        statusHisto.setTimeSheetStatus("A");
                        statusHisto.save();
	                }
                }
            }
        }
        return true;
    }
    
    /**
     * Cette méthode retourne le id du code de distribution des 
     * livrets associé à la valeur passée en paramètre
     */
    private int getDistributionIdFromValue(String value)
    {
        try
        {
            int id = 0;
            PreparedStatement stmt = DB.prepareStatement("select P_Distribution_Booklet_ID from P_Distribution_Booklet where Value = '" + value + "'", null);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
                id = rs.getInt(1);
            
            rs.close();
            stmt.close();
            return id;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * Cette méthode retourne le minimum entre a et b qui ne soit pas -1. Dans
     * le cas où les deux variables sont à -1, on retourne -1.
     */
    private int realMin(int a, int b)
    {
        if(a >= 0 && b >= 0)
            return a < b ? a : b;
        if((a < 0 && b >= 0) || (a >= 0 && b < 0))
            return a < 0 ? b : a;
        return -1;
    }

}
