/*
 * Created on 8 juil. 2005
 */
package solstice.custom;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.Tag;

import org.compiere.util.DB;

/**
 * @author frafor01
 */
public class DynamicStructureTag implements BodyTag, IQueryUser, IBodyOwner
{
    private String id = null;
    private boolean evaluated = false;
    private ResultSet rs;
    private PreparedStatement stmt;
    private String registerKey = null;
    private String registerValue = null;
    private String registerPrefix = null;
    private boolean evalBody = false;

    private Tag parent;    
    private BodyContent bodyContent;    
    private PageContext pageContext;    
    
    public boolean evaluateBody() {return this.evalBody;}
     
    public DynamicStructureTag()    
    {        
        super();    
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public String getId()
    {
        return this.id;
    }
    
    public void setRegisterKey(String registerKey)
    {
        this.registerKey = registerKey;
    }
    
    public void setRegisterValue(String registerValue)
    {
        this.registerValue = registerValue;
    }
    
    public void setRegisterPrefix(String registerPrefix)
    {
        this.registerPrefix = registerPrefix;
    }
    
    public void setPageContext(PageContext pageContext)    
    {        
        this.pageContext = pageContext;    
    }
    
    public void setParent(final javax.servlet.jsp.tagext.Tag parent)    
    {        
        this.parent = parent;    
    }
    
    public int doStartTag() throws JspTagException    
    {        
        this.evaluated = false;
        return EVAL_BODY_BUFFERED;        
    }
    
    public void setBodyContent(BodyContent bodyContent)    
    {        
        this.bodyContent = bodyContent;    
    }
    
    public void doInitBody() throws JspTagException {}
    
    public int doAfterBody() throws JspTagException    
    {        
        try
        {
            if(this.evalBody && rs.next())
            {
                if(this.registerValue != null && !this.registerValue.equals(""))
                {
                    DictionaryEntry entry
                        = this.registerKey != null && !this.registerKey.equals("")
                        ? new DictionaryEntry(rs.getObject(this.registerKey), rs.getObject(this.registerValue))
                        : new DictionaryEntry(rs.getObject(this.registerValue), rs.getObject(this.registerValue));
                
                    Vector v = (Vector)this.pageContext.getAttribute(
                            (this.registerPrefix != null && !this.registerPrefix.equals("") 
                            ? this.registerPrefix : "") + this.registerValue);
                    
                    v.add(entry);
                }
                return EVAL_BODY_AGAIN;
            }
            else        
            {            
                return SKIP_BODY;        
            }
        }
        catch (Exception e)
        {
            throw new JspTagException (e.getMessage());
        }
    }
    
    public int doEndTag() throws JspTagException    
    {        
        try        
        {            
            if(this.evalBody)
            {
                rs.close();
                stmt.close();
            }
            if(bodyContent != null)            
                bodyContent.writeOut(bodyContent.getEnclosingWriter());        
        }        
        catch(SQLException e)
        {
            e.printStackTrace(System.out);
        }
        catch(java.io.IOException e)        
        {            
            this.evaluated = false;
            throw new JspTagException("IO Error: " + e.getMessage());        
        }        
        this.evaluated = false;
        return EVAL_PAGE;    
    }
    
    public void release() {}
    
    public javax.servlet.jsp.tagext.Tag getParent()    
    {        
        return parent;    
    }
    
    public void setQuery(QueryBaseTag query)
    {
        String sql = query.getQuery();
        if(!evaluated)
        {
            try
            {
                stmt = DB.prepareStatement(sql, null);
                rs = stmt.executeQuery();
                this.evalBody = rs.next();
                if(this.evalBody && this.registerValue != null && !this.registerValue.equals(""))
                {
                    DictionaryEntry entry
                        = this.registerKey != null && !this.registerKey.equals("")
                        ? new DictionaryEntry(rs.getObject(this.registerKey), rs.getObject(this.registerValue))
                        : new DictionaryEntry(rs.getObject(this.registerValue), rs.getObject(this.registerValue));
                    
                    Vector v = new Vector();
                    v.add(entry);
                    this.pageContext.setAttribute(
                            (this.registerPrefix != null && !this.registerPrefix.equals("") 
                                    ? this.registerPrefix : "") + this.registerValue, v);
                }
            }
            catch(Exception e)
            {
                e.printStackTrace(System.out);
            }
            evaluated = true;
        }
    }

    /**
     * Retourne la valuer du champ passé en paramètre
     */
	public Object getRSValue(String field) throws JspException
	{
		try
		{
            Object value = rs.getObject(field);
            if(!(value instanceof String)
                    && !(value instanceof Date)
                    && !(value instanceof Number))
            {
                return rs.getString(field);
            }
			return value;
		}
		catch (Exception e)
		{
			throw new JspException(e);
		}
	}
	
}
