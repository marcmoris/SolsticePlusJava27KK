package solstice.custom;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.P_PunchCard_Collective_Labour_Agr;
import solstice.model.P_PunchCard_Reason;

/**
 * Code derrière les interfaces d'administration du permis horodateur web
 * @author frafor01
 */
public class HorodateurAdministration extends ValidationBase
{

    public HorodateurAdministration()
    {
        super();
    }

    protected String[] doValidate() throws JspException
    {
        return null;
    }

    /**
     * Traitement des actions envoyé par un post
     */
    public boolean afterValidation(JspWriter out) throws JspException
    {
        String action = this.request.getParameter("action");
        
        if("enregistrerAcces".equals(action))
        {
            
            // On doit enregistrer les accès par convention collective. On
            // récupère donc d'abord la liste des conventions qui n'ont pas
            // accès
            String[] values = this.request.getParameterValues("conventionsL");
            if(values != null)
            {
                String sql = "";
                for(int i = 0; i < values.length; i++)
                {
                    // On vérifie si cette valeur a été modifiée
                    if(values[i].charAt(0) == '1')
                    {
                        String id = values[i].substring(1, values[i].indexOf("|"));
                        sql += "delete from P_PunchCard_Collective_Labour_Agr where P_Collective_Labour_Agr_ID = " + id + "; ";
                    }
                }
                
                // On effectue la mise à jour
                if(!sql.equals(""))
                {
                    DB.executeUpdate(sql, null);
                }
            }
            
            // On récupère maitenant la liste des conventions qui ont accès
            values = this.request.getParameterValues("conventionsR");
            if(values != null)
            {
                for(int i = 0; i < values.length; i++)
                {
                    // On vérifie si cette valeur a été modifiée
                    if(values[i].charAt(0) == '1')
                    {
                        int id = Integer.parseInt(values[i].substring(1, values[i].indexOf("|")));
                        
                        // S'il n'existe pas déjà un accès pour cette convention car,
                        // en effet, si l'utilisateur clique plus d'une fois sur le boutton,
                        // l'enregistrement voudra être créé plus d'une fois.
                        if(!this.existCollectiveLabourAgr(id))
                        {
                            P_PunchCard_Collective_Labour_Agr obj = new P_PunchCard_Collective_Labour_Agr(Env.getCtx(), -1, null);
                            obj.setP_Collective_Labour_Agr_ID(id);
                            obj.save();
                        }
                    }
                }
            }
            
            try
            {
                this.response.sendRedirect("administration.jsp");
                return false;
            }
            catch (IOException e)
            {
                e.printStackTrace();
                throw new JspException(e);
            }
            
        }
        else if("enregistrerMotif".equals(action))
        {
            // On doit enregistrer un motif. On récupère d'abord
            // les informations saisies
            int id = Integer.parseInt(this.request.getParameter("punchCardReasonId"));
            String value = this.request.getParameter("value");
            String name = this.request.getParameter("name");
            boolean active = "on".equals(this.request.getParameter("active"));
            
            P_PunchCard_Reason punchCardReason = new P_PunchCard_Reason(Env.getCtx(), id, null);
            punchCardReason.setValue(value);
            punchCardReason.setName(name);
            punchCardReason.setIsActive(active);
            punchCardReason.save();
        }
        else if("deleteMotif".equals(action))
        {
            // On doit supprimer un motif
            int id = Integer.parseInt(this.request.getParameter("punchCardReasonId"));
            DB.executeUpdate("delete from P_PunchCard_Reason where P_PunchCard_Reason_ID = " + id, null);
        }
        
        return true;
    }
    
    /**
     * Cette méthode vérifie s'il existe un acces pour la
     * convention collective passée en paramètre pour le
     * horodateur
     */
    private boolean existCollectiveLabourAgr(int id)
    {
        boolean exists = false;
        try
        {
            PreparedStatement stmt = DB.prepareStatement(
                    "select top 1 1" +
                    "  from P_PunchCard_Collective_Labour_Agr" +
                    " where P_Collective_Labour_Agr_ID = " + id, null);
            
            ResultSet rs = stmt.executeQuery();
            
            exists = rs.next();

            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return exists;
    }
    
    /**
     * Cette méthode retourne la liste des conventions collectives
     * qui n'ont pas été autorisé à accèder à l'application des
     * permis horodateur (colonne de gauche)
     * @param pageContext
     * @return
     */
    public static Enumeration getNotGrantedConvention(PageContext pageContext)
    {
        Vector<DictionaryEntry> conventions = new Vector<DictionaryEntry>();
        
        try
        {
            // Cette requête récupère la liste des conventions collective
            PreparedStatement stmt = DB.prepareStatement(
                    "select P_Collective_Labour_Agr_ID," +
                    "       Value + ' - ' + Name as Description" +
                    "  from P_Collective_Labour_Agr" +
                    " where IsActive = 'Y'" +
                    "   and not exists ( select 1" +
                    "                      from P_PunchCard_Collective_Labour_Agr" +
                    "                     where P_Collective_Labour_Agr_ID = P_Collective_Labour_Agr.P_Collective_Labour_Agr_ID" +
                    "                       and IsActive = 'Y' )", null);
            
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next())
            {
                // On ajoute une entrée pour chacune des convention
                DictionaryEntry newEntry = SwitchTag.newEntryInstance(
                        rs.getString("P_Collective_Labour_Agr_ID"), 
                        rs.getString("Description"));
                
                conventions.add(newEntry);
            }
            
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        return conventions.elements();
    }
    
    /**
     * Cette méthode retourne la liste des conventions collectives
     * qui ont accès à l'application des permis horodateur
     * @param pageContext
     * @return
     */
    public static Enumeration getGrantedConvention(PageContext pageContext)
    {
        Vector conventions = new Vector();
        
        try
        {
            // Cette requête récupère la liste des conventions collective
            PreparedStatement stmt = DB.prepareStatement(
                    "select P_Collective_Labour_Agr_ID," +
                    "       Value + ' - ' + Name as Description" +
                    "  from P_Collective_Labour_Agr" +
                    " where IsActive = 'Y'" +
                    "   and exists ( select 1" +
                    "                  from P_PunchCard_Collective_Labour_Agr" +
                    "                 where P_Collective_Labour_Agr_ID = P_Collective_Labour_Agr.P_Collective_Labour_Agr_ID" +
                    "                   and IsActive = 'Y' )", null);
            
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next())
            {
                // On ajoute une entrée pour chacune des convention
                DictionaryEntry newEntry = SwitchTag.newEntryInstance(
                        rs.getString("P_Collective_Labour_Agr_ID"), 
                        rs.getString("Description"));
                
                conventions.add(newEntry);
            }
            
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        return conventions.elements();
    }
    
}
