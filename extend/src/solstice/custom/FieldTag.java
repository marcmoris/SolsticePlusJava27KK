/*
 * Created on 28 juil. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.custom;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FieldTag extends TagSupport
{
    /**
     * Cette classe sert à contenir la/les spécification(s) de champs et à formatter 
     * les cellules du grid pour une même colonne.
     */
    private class Field implements IField
    {
        /*
         * Attributes
         */
        private String colonneTitle;
        private Vector<SQLField> sqlFields = new Vector<SQLField>();
        
        public void setColonneTitle(String colonneTitle) {this.colonneTitle = colonneTitle;}
        
        public Field()
        {
        }
        
        /**
         * Retourne le &lt;TD&gt; pour l'entête de la colonne 
         */
        public String getTDLabel(String css)
        {
            String cssClass = css != null ? " class=\"" + css + "\"" : "";
            String colspan = sqlFields.size() > 1 ? " colspan=\"" + sqlFields.size() + "\"" : "";
            return "<td" + cssClass + colspan + this.getAlign() +  ">" + this.colonneTitle + "</td>";
        }
        
        /**
         * Si la colonne contient plus d'un champ, on aligne le tire au centre.
         * Si la colonne contient un seul champ de nombre, on aligne le titre à droite.
         */
        private String getAlign()
        {
            if(this.sqlFields.size() > 1) return " align=\"center\"";
            if(this.sqlFields.size() == 1 && ((SQLField)this.sqlFields.elementAt(0)).getType().toLowerCase().equals("number"))
                return " align=\"right\"";
            return "";
        }
        
        /**
         * Retourne la ou les cellules correspondantes à l'enregistrement courrant
         */
        public String getTDCell(String css, ResultSet rs, boolean selected) throws SQLException
        {
            String cssClass = css != null ? " class=\"" + css + "\"" : "";
            Enumeration enumeration = sqlFields.elements();
            String html = "";
            SQLField sqlField;
            while(enumeration.hasMoreElements())
            {
                sqlField = (SQLField)enumeration.nextElement();
                String align = sqlField.getType().toLowerCase().equals("number") ? " align=\"right\"" : "";
                html += "<td" + cssClass + align + ">" + sqlField.toString(rs.getObject(sqlField.getId()), selected) + "</td>";
            }
            return html;
        }
        
        /**
         * Ajout d'un champ à la colonne
         */
        public void addSQLField(SQLField sqlField)
        {
            this.sqlFields.add(sqlField);
        }
    }
    
    /**
     * Représente le contenue des champs tel que redéfinit dans le tag. Cette
     * classe permet également de retourner le formattage final du contenue
     * de la cellule
     */
    private class SQLField
    {
        /*
         * Attributes
         */
        private String id;
        private String type;
        private String format;
        private String style;
        private String styleAlt;
        private String nullValue;
        private boolean group;
        private Object lastValue = null;
        
        public SQLField(String id, String type, String format, String style, String styleAlt, String nullValue, boolean group)
        {
            this.id = id;
            this.type = type;
            this.format = format;
            this.style = style;
            this.styleAlt = styleAlt;
            this.nullValue = nullValue;
            this.group = group;
        }
        
        /*
         * getters
         */
        public String getId() {return this.id;}
        public String getType() {return this.type;}
        
        /**
         * Formattage des nombres et des dates
         */
        private String formatValue(Object value)
        {
            if(this.format != null && !this.format.equals(""))
            {
	            if(type.toLowerCase().equals("number"))
	            {
	                double val = value.getClass().equals(BigDecimal.class) ? ((BigDecimal)value).doubleValue() : ((Double)value).doubleValue();
	                DecimalFormat decimalFormat = new DecimalFormat(this.format);
	                return decimalFormat.format(val);
	            }
	            if(type.toLowerCase().equals("date"))
	            {
	                SimpleDateFormat dateFormat = new SimpleDateFormat(this.format);
	                return dateFormat.format((Date)value);
	            }
            }
            return value.toString();
        }
        
        /**
         * Si un style ou un style alternatif a été défini, on l'ajoute au formattage
         */
        private String styleValue(String value, boolean selected)
        {
            if(selected && this.styleAlt != null)
            {
                return this.styleAlt.replaceAll("@@", value);
            }
            else if(this.style != null)
            {
                return this.style.replaceAll("@@", value);
            }
            return value;
        }
        
        /**
         * Retourne le contenue de la cellule formatté
         */
        public String toString(Object value, boolean selected)
        {
            if(value == null)
                return this.nullValue != null ? this.nullValue : "";
            if(!this.group)
                return this.styleValue(this.formatValue(value), selected);
            if(this.lastValue != null && this.lastValue.equals(value))
                return "";
            this.lastValue = value;
            return this.styleValue(this.formatValue(value), selected);
        }
    }
    
    /*
     * Attributes
     */
    private String type;
    private String name = null;
    private String format = null;
    private String style = null;
    private String styleAlt = null;
    private String nullValue = null;
    private boolean group = false;
    
    /*
     * setters
     */
    public void setName(String name) {this.name = name;}
    public void setType(String type) {this.type = type;}
    public void setFormat(String format) {this.format = format;}
    public void setStyle(String style) {this.style = style;}
    public void setStyleAlt(String styleAlt) {this.styleAlt = styleAlt;}
    public void setNullValue(String nullValue) {this.nullValue = nullValue;}
    public void setGroup(boolean group) {this.group = group;}
    
    public int doStartTag()
    {
        this.style = null;
        this.styleAlt = null;
        return EVAL_BODY_INCLUDE;
    }
    
    public int doEndTag() throws JspException
    {
        SQLField sqlField = new SQLField(this.id, this.type, this.format, this.style, this.styleAlt, this.nullValue, this.group);
        
        // Si on a affaire à un groupe de champs, on récupère le nom
        // du parent et on ajoute un nouveau champ à sa liste
        if(this.getParent().getClass().equals(FieldGroupTag.class))
        {
            Field field = (Field)((FieldGroupTag)this.getParent()).getCurrentField();
            // Si on doit créer une nouvelle liste
            if(field == null)
            {
                field = new Field();
                field.setColonneTitle(((FieldGroupTag)this.getParent()).getName());
            }
            field.addSQLField(sqlField);
            ((FieldGroupTag)this.getParent()).setCurrentField(field);
        }
        else // On est directement rattaché au grid
        {
            Field field = new Field();
            field.setColonneTitle(this.name);
            field.addSQLField(sqlField);
            ((IGrid)this.getParent()).addField(field);
        }
        this.group = false;
        return EVAL_PAGE;
    }
}
