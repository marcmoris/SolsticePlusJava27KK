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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.P_Gain_Group;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LivretTempsGroupeCode extends ValidationBase
{
    public static Enumeration nonAssignes(PageContext pageContext)
    {
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        
        Vector<DictionaryEntry> values = new Vector<DictionaryEntry>();
        if(request.getParameter("groupId") != null && !request.getParameter("groupId").equals(""))
	{
		if(request.getParameter("action") != null)
		{
			if(!request.getParameter("action").equals("detruire"))
	        	{
	        	    String id = request.getParameter("groupId");
		            String sql
        		    = "SELECT P_Gain_ID, Value, Name"
                		+ " FROM P_Gain"
		                + " WHERE IsActive = 'Y'"
        		        + " AND P_Gain_Group_ID is null"
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
        		}
		}
	}
        return values.elements();
    }

    public static Enumeration assignes(PageContext pageContext)
    {
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        
        Vector<DictionaryEntry> values = new Vector<DictionaryEntry>();
        if(request.getParameter("groupId") != null && !request.getParameter("groupId").equals(""))
        {
	        if(request.getParameter("action") != null)
		{
			if(!request.getParameter("action").equals("detruire"))
	        	{

		            String id = request.getParameter("groupId");
            		    String sql
            			= "SELECT P_Gain_ID, Value, Name"
                		+ " FROM P_Gain"
                		+ " WHERE IsActive = 'Y'"
                		+ " AND IsNull(P_Gain_Group_ID, 0) = " + id
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
			}
		}
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
    
    private String fourFirst(String value)
    {
        while(value.length() <= 4)
            value += "_";
        String temp = value.substring(0, 4);
        return temp.replaceAll(" ", "_");
    }
    
    private String completeValue(String value)
    {
        String sql
        = "select count(1)"
            + " from P_Gain_Group"
            + " where Value like '" + value + "%'";
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
                int i = rs.getInt(1);
                rs.close();
                stmt.close();
                
                if(i < 10)
                    return value + "0" + i;
                return value + i;
            }
            else
            {
                System.out.println("Erreur dans la completion de la value");
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace(System.out);
        }
        return value;
    }
    
    private boolean canBeDeleted(String id)
    {
        String sql
        = "select count(1)"
            + " from P_Gain"
            + " where isnull(P_Gain_Group_ID, 0) = " + id;
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
                int i = rs.getInt(1);
                rs.close();
                stmt.close();
                return i == 0;
            }
            else
            {
                System.out.println("Erreur dans le canBeDelete de groupe de gain");
            }
            rs.close();
            stmt.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace(System.out);
        }
        return false;
    }

    /* (non-Javadoc)
     * @see com.compiere.custom.ValidationBase#afterValidation(javax.servlet.jsp.JspWriter)
     */
    public boolean afterValidation(JspWriter out)
    {
        String action;
        if((action = request.getParameter("action")) != null)
        {
            if(action.equals("ajouter"))
            {
                String name = request.getParameter("newGroup");
                String value = fourFirst(name);
                value = completeValue(value);
                
                P_Gain_Group gainGroup = new P_Gain_Group(Env.getCtx(), -1, null);
                gainGroup.setValue(value);
                gainGroup.setName(name);
                gainGroup.save();
            }
            else if(action.equals("detruire"))
            {
                String id = request.getParameter("groupId");
                if(id != null && canBeDeleted(id))
                {
                    String sql = "delete from P_Gain_Group where P_Gain_Group_ID = " + id;
                    DB.executeUpdate(sql, null);
                }
            }
            else if(action.equals("save"))
            {
                String id = request.getParameter("groupId");
                String[] values = request.getParameterValues("groupOui");
                if(id != null && !id.equals(""))
                {
                    String sql = "update P_Gain set P_Gain_Group_ID = null where isnull(P_Gain_Group_ID, 0) = " + id;
                    DB.executeUpdate(sql, null);
                    if(values != null)
                    {
	                    sql = "";
	                    for(int i = 0; i < values.length; i++)
	                    {
	                        String gainId = values[i].substring(1, values[i].indexOf("|"));
	                        sql += "update P_Gain set P_Gain_Group_ID = " + id + " where P_Gain_ID = " + gainId + ";";
	                    }
	                    DB.executeUpdate(sql, null);
                    }
                }
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
