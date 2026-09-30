/*
 * Created on 27 juil. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.custom;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.compiere.util.DB;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LivretTempsCodeSecurite extends ValidationBase
{
    public static Enumeration leftInitialize (PageContext pageContext)
    {
        Vector<DictionaryEntry> values = new Vector<DictionaryEntry>();
        
        String sql
        = "SELECT P_Gain_ID, Value, Name"
            + " FROM P_Gain"
            + " WHERE Accessible='N' and IsActive = 'Y' "
            + " ORDER BY Name";
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next())
            {
                String text = rs.getString("Name") + " (" + rs.getString("Value") + ")";
                values.add(SwitchTag.newEntryInstance(rs.getString("P_Gain_ID"), text));
            }
            
            rs.close();
            stmt.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace(System.out);
        }
        return values.elements();
    }
    
    public static Enumeration rightInitialize (PageContext pageContext)
    {
        Vector<DictionaryEntry> values = new Vector<DictionaryEntry>();
        
        String sql
        = "SELECT P_Gain_ID, Value, Name"
            + " FROM P_Gain"
            + " WHERE Accessible='Y' and IsActive = 'Y' "
            + " ORDER BY Name";
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next())
            {
                String text = rs.getString("Name") + " (" + rs.getString("Value") + ")";
                values.add(SwitchTag.newEntryInstance(rs.getString("P_Gain_ID"), text));
            }
            
            rs.close();
            stmt.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace(System.out);
        }
        return values.elements();
    }
    
    /* (non-Javadoc)
     * @see com.compiere.custom.ValidationBase#doValidate()
     */
    protected String[] doValidate()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.compiere.custom.ValidationBase#afterValidation(javax.servlet.jsp.JspWriter)
     */
    public boolean afterValidation(JspWriter out)
    {
        if(this.request.getParameter("action") != null && this.request.getParameter("action").equals("save"))
        {
            String sql = "";
            // On prépare la mise à jour des gain à enlever l'accessibilité
            String[] values = this.request.getParameterValues("accessNon");
            if(values != null)
            {
	            for(int i = 0; i < values.length; i++)
	            {
	                // On vérifie si cette valeur a été modifiée
	                if(values[i].charAt(0) == '1')
	                {
	                    String id = values[i].substring(1, values[i].indexOf("|"));
	                    sql += "update P_Gain set Accessible = 'N' where P_Gain_ID = " + id + "; ";
	                }
	            }
            }
            
            // On prépare la mise à jour des gain à ajouter l'accessibilité
            values = this.request.getParameterValues("accessOui");
            if(values != null)
            {
	            for(int i = 0; i < values.length; i++)
	            {
	                // On vérifie si cette valeur a été modifiée
	                if(values[i].charAt(0) == '1')
	                {
	                    String id = values[i].substring(1, values[i].indexOf("|"));
	                    sql += "update P_Gain set Accessible = 'Y' where P_Gain_ID = " + id + "; ";
	                }
	            }
            }

            // On effectue la mise à jour
            if(!sql.equals(""))
            {
                DB.executeUpdate(sql, null);
            }
        }
        return true;
    }

    /* (non-Javadoc)
     * @see com.compiere.custom.ValidationBase#getPageName()
     */
    protected String getPageName()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
