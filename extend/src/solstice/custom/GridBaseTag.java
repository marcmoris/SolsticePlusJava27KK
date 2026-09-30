/*
 * Created on 28 juil. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.custom;

import java.util.Vector;

import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class GridBaseTag extends TagSupport implements IGrid, IQueryUser
{
    /*
     * Attributs
     */
	protected String labelClassName = "tdlabel";
	protected String cellClassName = "tdfield";
	protected String cellClassAltName = "tdfieldalt";
	protected String selectedClassName = "tdfullselectcal";
	
	protected String query;
	protected Vector<IField> fields = new Vector<IField>();

	/*
	 * Setters
	 */
	public void setLabelClassName(String labelClassName) {this.labelClassName = labelClassName;}
	public void setCellClassName(String cellClassName) {this.cellClassName = cellClassName;}
	public void setCellClassAltName(String cellClassAltName) {this.cellClassAltName = cellClassAltName;}
	public void setSelectedClassName(String selectedClassName) {this.selectedClassName = selectedClassName;}
	
	/*
	 * Méthodes utilisées par les enfants
	 */
	public void setQuery(QueryBaseTag query) {this.query = query.getQuery();}
	public void addField(IField field) {this.fields.add(field);}
	
	
}
