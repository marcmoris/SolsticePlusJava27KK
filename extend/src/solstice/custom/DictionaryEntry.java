/*
 * Created on 11 juil. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.custom;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DictionaryEntry 
{
	private Object key;
	private Object value;
	
	public DictionaryEntry(Object key, Object value)
	{
		this.key = key;
		this.value = value;
	}
	
	public void setValue(Object value)
	{
		this.value = value;
	}
	
	public Object getKey()
	{
		return this.key;
	}
	
	public Object getValue()
	{
		return this.value;
	}
	
	public String toString()
	{
	    if(this.value != null)
	        return this.value.toString();
	    return "";
	}
}
