/*
 * Created on 2005-09-22
 */
package solstice.custom;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author frafor01
 *
 * Représente un switch {case} dans une page
 */
public class SelectCaseTag extends TagSupport
{
    /*
     * Attributes
     */
    private Object value = null;
    private String dynamicValue = null;
    
    /*
     * Members
     */
    private boolean used = false;
    
    /*
     * Setters
     */
    public void setValue(Object value) {this.value = value;}
    public void setDynamicValue(String dynamicValue) {this.dynamicValue = dynamicValue;}

    public SelectCaseTag()
    {
        super();
    }
    
    /**
     * Retourne l'évaluation du check (null = default case)
     */
    public boolean checkValue(Object value)
    {   
        // Si la correspondance n'a pas déjà été trouvée pour
        // cette structure...
        if(!this.used)
        {
            // ...on evalue la valeur et on ajuste le résultat
            // de la correspondance
            if(this.value == null)
                this.used = (value == null);
            else
                this.used = (value == null ? true : this.value.equals(value));
            return this.used;
        }
        return false;
    }

    /**
     * On trouve la bonne valeur dépendamment de la source
     */
    public int doStartTag() throws JspException
    {
        // Si on doit prendre la valeur d'une dynamicStructure...
        if(this.dynamicValue != null)
        {
            // ...on récupère la structure et la valeur
            DynamicStructureTag dynamicStructure = (DynamicStructureTag)findAncestorWithClass(this, DynamicStructureTag.class);
            this.value = dynamicStructure.getRSValue(this.dynamicValue);
        }
        return EVAL_BODY_INCLUDE;
    }
    
    /**
     * Réinitialisation des variables
     */
    public int doEndTag() throws JspException
    {
        this.dynamicValue = null;
        this.value = null;
        this.used = false;
        return EVAL_PAGE;
    }
}
