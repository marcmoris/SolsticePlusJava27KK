/*
 * Created on 3 août 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.custom;

import java.util.Vector;
import java.util.Hashtable;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ListMultiColumnItem
{
    private String key;
    private String[] values;
    private Hashtable<String, String> properties;
    
    public String getKey() {return this.key;}
    public String getValue(int index) {return this.values[index];}
    public int getValueLength() {return this.values.length;}
    public String getProperty(String propertyName)
    {
    	if(properties == null)
    	{
    		return null;
    	}
    	return (String)this.properties.get(propertyName);
    }
    public void setProperty(String propertyName, String propertyValue)
    {
    	if(properties == null)
    	{
    		properties = new Hashtable<String, String>();
    	}
    	this.properties.put(propertyName, propertyValue);
    }
    
    public ListMultiColumnItem(String key, String[] values)
    {
        this.key = key;
        this.values = values;
    }
    
    public ListMultiColumnItem(String input)
    {
        int index;
        this.key = input.substring(0, (index = input.indexOf("|")));
        this.values = split(input.substring(index+1));
    }
    
    private String[] split(String input)
    {
        Vector<String> temp = new Vector<String>();
        int index;
        while((index = input.indexOf("|")) >= 0)
        {
            temp.add(input.substring(0, index));
            input = input.substring(index+1);
        }
        temp.add(input);
        String[] retour = new String[temp.size()];
        for(int i = 0; i < retour.length; i++)
            retour[i] = temp.elementAt(i).toString();
        return retour;
    }
    
    private String join(String[] input)
    {
        if(input.length > 0)
        {
            String temp = input[0];
            for(int i = 1; i < input.length; i++)
            {
                temp += "|" + input[i];
            }
            return temp;
            
        }
        return "";
    }
    
    public String toString()
    {
        return this.key + "|" + join(this.values);
    }
}
