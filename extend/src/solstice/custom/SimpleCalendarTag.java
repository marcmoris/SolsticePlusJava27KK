/*
 * Created on 18 juil. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.custom;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import solstice.process.PgiUtil;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SimpleCalendarTag extends TagSupport
{
    private String formName;
    private boolean readOnly = false;
    private Date value = null;
    private boolean getPostValue = false;
    private String defaultDate = null;
    
    public void setGetPostValue(boolean getPostValue)
    {
        this.getPostValue = getPostValue;
    }
    
    public void setFormName(String formName)
    {
        this.formName = formName;
    }
    
    public void setReadOnly(boolean readOnly)
    {
        this.readOnly = readOnly;
    }
    
    public void setValue(Date value)
    {
        this.value = value;
    }
    
    public void setDefaultDate(String _DefaultDate)
    {
        this.defaultDate = _DefaultDate;
    }

    public void setValue(String value)
    {
        Calendar calendar = PgiUtil.stringToDateV2(value);
        if(calendar == null)
            this.value = null;
        else
            this.value = new Timestamp(calendar.getTimeInMillis());
    }
    
    public int doEndTag() throws JspException
    {
        JspWriter out = pageContext.getOut();
        try
        {
            out.println(this.createTag());
        }
        catch (IOException e)
        {
            e.printStackTrace(System.out);
        }
        this.getPostValue = false;
        this.defaultDate = null;
        return EVAL_PAGE;
    }

    protected String createTag()
	{
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd");
        String value;
        if(this.getPostValue && ((HttpServletRequest)this.pageContext.getRequest()).getParameter(this.id) != null)
        {
            value = "value=\"" + ((HttpServletRequest)this.pageContext.getRequest()).getParameter(this.id) + "\"";
        }
        else
        {
            value
        	= this.value == null
        	? "" : "value=\"" + fdate.format(this.value) + "\"";
        }
        
        String strYear = null;
        String strMonth = null;
        int iMonth = -1;
        String strDefaultDateParam = "";
        if(this.defaultDate != null)
        {
        	if(this.defaultDate.length() > 3)
        	{
        		strYear = this.defaultDate.substring(0, 4);
        	}
        	if(this.defaultDate.length() > 6)
        	{
        		strMonth = this.defaultDate.substring(5, 7);//2006-07
        		if(strMonth.substring(0, 1).equals("0"))//si 03 genre
        		{
        			iMonth = Integer.parseInt(strMonth.substring(1, 2));
        		}
        		else
        		{
        			iMonth = Integer.parseInt(strMonth);
        		}
        		iMonth -= 1;//Javscrip months are 0-11
        	}
        	if(strYear != null && iMonth != -1)
        	{
        		strDefaultDateParam = ", '"+iMonth+"', '"+strYear+"'";
        		System.out.println("KM TEST defaultDate SimpleCalendar : "+strDefaultDateParam);
        	}
        }
		String calendar = "<input type=\"text\" name=\"" + this.getId() + "\" id=\"" + this.getId() + "\" size=\"10\" "+ value + " readonly>";
		if(!this.readOnly)
		{
			calendar += "<a href=\"javascript:show_calendar('" + this.formName + "." + this.getId() + "'"+strDefaultDateParam+");\" onMouseOver=\"window.status='Date Picker'; overlib('Cliquez ici pour choisir une date.'); return true;\"";
			calendar += "onMouseOut=\"window.status=''; nd(); return true;\">";
		}
		calendar += "<img src=\"images/finddate.gif\" width=\"19\" height=\"17\" border=\"0\">";
		if(!this.readOnly)
		    calendar += "</a>";
		return calendar;
	}
}
