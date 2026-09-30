/*
 * Created on 3 août 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.custom;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ListMultiColumnTag extends TagSupport
{   
    /*
     * Attributes 
     */
    private String[] columnsText;
    private int[] columnsWidth;
    private int height;
    private String initializeMethod;
    private String formName;
    private String javascriptMethod = null;
    private String cssLabel = "tdlabel";
    private String cssField = "tdfield";
    private String cssSelected = "tdfieldalt";
    private boolean autoReload = false;
    
    /*
     * Members
     */
    private String selectedValue = null;
    private int width;

    /*
     * Setters
     */
    public void setColumnsText(String[] columnsText) {this.columnsText = columnsText;}
    public void setColumnsWidth(int[] columnsWidth) {this.columnsWidth = columnsWidth;}
    public void setHeight(int height) {this.height= height;}
    public void setInitializeMethod(String initializeMethod) {this.initializeMethod = initializeMethod;}
    public void setFormName(String formName) {this.formName = formName;}
    public void setJavascriptMethod(String javascriptMethod) {this.javascriptMethod = javascriptMethod;}
    public void setCssLabel(String cssLabel) {this.cssLabel = cssLabel;}
    public void setCssField(String cssField) {this.cssField = cssField;}
    public void setCssSelected(String cssSelected) {this.cssSelected = cssSelected;}
    public void setAutoReload(boolean autoReload) {this.autoReload = autoReload;}
    
    /*
     * Noms utilisés
     */
    private String getAction() {return this.id + "_action";}
    private String getList() {return this.id + "_values";}
    private String getSelectedValue() {return this.id + "_selectedValue";}
    private String getSelect(int index) {return this.id + "_select(" + index + ")";}
    private String getRow() {return this.id + "_row";}
    
    private Enumeration retrieveList()
    {
        HttpServletRequest request = (HttpServletRequest)this.pageContext.getRequest();
        String action;
        
        // On vérifie si on doit faire l'initialisation ou non
        if(!this.autoReload && (action = request.getParameter(this.getAction())) != null && !request.getParameter(this.getAction()).equals(""))
        {
            // On retrouve l'élément sélectionné
            if(request.getParameter(this.getSelectedValue()) != null && !request.getParameter(this.getSelectedValue()).equals(""))
                this.selectedValue = request.getParameter(this.getSelectedValue());
            else
                this.selectedValue = null;

            // Dans le cas où on a une action, on ne refait pas l'initialisation et on récupère d'abord
            // la liste du dernier post
            Vector<ListMultiColumnItem> list = new Vector<ListMultiColumnItem>();
            String[] values = request.getParameterValues(this.getList());
            if(values != null)
            {
                for(int i = 0; i < values.length; i++)
                {
                    ListMultiColumnItem item = new ListMultiColumnItem(values[i]);
                    // On vérifie la suppression
                    if(!action.equals("delete") || this.selectedValue == null || !this.selectedValue.equals(item.getKey()))
                        list.add(item);
                }
            }
            
            // On vérifie si on doit ajouter un nouvel élément à la liste
            if(action.indexOf("add:") == 0)
            {
                list.add(new ListMultiColumnItem(action.substring(4)));
            }
            
            return list.elements();
        }
        return this.initializeList();
    }
    
    /**
     * Retourne les éléments de la méthode d'initialisation
     */
    private Enumeration initializeList()
    {
        String className;
        String methodName;

        className = this.initializeMethod.substring(0, this.initializeMethod.lastIndexOf("."));
        methodName = this.initializeMethod.substring(this.initializeMethod.lastIndexOf(".") + 1);
  
        try
        {
	        Class c = Class.forName(className);
	        Method m = c.getMethod(methodName, new Class[] {PageContext.class});
	        return (Enumeration)m.invoke(null, new Object[] {pageContext});
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace(System.out);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace(System.out);
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace(System.out);
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace(System.out);
        }

        return new Vector().elements();
    }
    
    private String join(Object[] inputs)
    {
        if(inputs.length > 0)
        {
            String temp = inputs[0].toString();
            for(int i = 1; i < inputs.length; i++)
            {
                temp += "\n" + inputs[i].toString();
            }
            return temp;
        }
        return "";
    }
    
    private void drawList(Enumeration items) throws JspException
    {
        Vector<String> inputs = new Vector<String>();
        Vector<String> trs = new Vector<String>();
        String indexToValue = "var " + this.id + "_indexToValue = new Array();\n";
        int i = 0;
        
        while(items.hasMoreElements())
        {
            ListMultiColumnItem item = (ListMultiColumnItem)items.nextElement();
            inputs.add(new String("<input type=\"hidden\" name=\"" + this.getList() + "\" value=\"" + item.toString() + "\">"));
            indexToValue += this.id + "_indexToValue[" + i + "] = \"" + item.getKey() + "\";\n";
            String temp = "<tr class=\"" + this.cssField + "\" id=\"" + this.getRow() + i + "\">\n";
            for(int j = 0; j < item.getValueLength(); j++)
            {
                // On ne veut pas mettre la largeur de la dernière colonne pour ne pas
                // qu'il y ait de scroll bar horizontal qui s'affiche en même temps
                // que le vertical
                if(j < item.getValueLength() - 1)
                    temp += "  <td style=\"cursor: pointer;color:"+ item.getProperty("color")+"\" width=\"" + this.columnsWidth[j] + "\" onClick=\"" + this.getSelect(i) + "\">" + item.getValue(j) + "</td>";
                else
                    temp += "  <td style=\"cursor: pointer;color:"+ item.getProperty("color")+"\" onClick=\"" + this.getSelect(i) + "\">" + item.getValue(j) + "</td>";
            }
            i++;
            temp += "</tr>\n";
            trs.add(temp);
        }
        
        String script
        = "<script language=\"javascript\">\n"
         	+ "var " + this.id + "_count = " + inputs.size() + ";\n" + indexToValue
         	+ "function " + this.id + "_select(index)\n"
         	+ "{"
         	+ "   for(var i = 0; i < " + this.id + "_count; i++)\n"
         	+ "   {\n"
         	+ "      if(i != index)\n"
         	+ "      {\n"
         	+ "         document.getElementById(\"" + this.getRow() + "\" + i).className = \"" + this.cssField + "\";\n"
         	+ "      }\n"
         	+ "   }\n"
         	+ "   document.getElementById(\"" + this.getRow() + "\" + index).className = \"" + this.cssSelected + "\";\n"
         	+ "   document." + this.formName + "." + this.getSelectedValue() + ".value = " + this.id + "_indexToValue[index];\n"
         	+ "}\n"
         	+ "</script>\n";
        
        String table
        = "<table width=\"" + this.width + "\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n"
        + "   <tr>\n";
        for(int j = 0; j < this.columnsText.length; j++)
        {
            table += "      <td width=\"" + this.columnsWidth[j] + "\" class=\"" + this.cssLabel + "\">" + this.columnsText[j] + "</td>\n";
        }
        table += "   </tr>\n"
        + "   <tr>\n"
        + "      <td colspan=\"" + this.columnsText.length + "\">\n"
        + "         <div style=\"border: solid 1px #000000; width: 100%; height: " + this.height + "; overflow: auto\">\n"
        + "            <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n"
        + join(trs.toArray())
        + "            </table>\n"
        + "         </div>\n"
        + "      </td>\n"
        + "   </tr>\n"
        + "</table>\n";
        
        JspWriter out = this.pageContext.getOut();
        try
        {
            out.println(script);
            out.println(join(inputs.toArray()));
            out.println("<input type=\"hidden\" name=\"" + this.getAction() + "\" value=\"idle\">");
            out.println("<input type=\"hidden\" name=\"" + this.getSelectedValue() + "\"" + (this.selectedValue != null ? " value=\"" + this.selectedValue + "\"" : "") + ">");
            out.println(table);
        }
        catch (IOException e)
        {
            new JspException(e);
        }
    }
    
    public int doStartTag() throws JspException
    {
        this.width = 0;
        for(int i = 0; i < this.columnsWidth.length; i++)
        {
            this.width += this.columnsWidth[i];
        }
        return SKIP_BODY;
    }
    
    public int doEndTag() throws JspException
    {
        this.drawList(this.retrieveList());
        return EVAL_PAGE;
    }
}
