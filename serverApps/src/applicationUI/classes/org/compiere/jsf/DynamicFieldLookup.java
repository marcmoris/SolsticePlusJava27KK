/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.jsf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import javax.faces.context.FacesContext;
import javax.faces.el.PropertyNotFoundException;

import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.GridWindow;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.NamePair;

/**
 * this class provides enough of an interface to compiere to get values for a
 * field's label, tooltip text, and value from the compiere model
 */
public class DynamicFieldLookup
{
	private final static CLogger log=CLogger.getCLogger(DynamicFieldLookup.class);

	private FacesContext facesContext;

	private Long tabNumber;

	private String columnName;

	private GridTab gridTab;

	private TabStateManager tabStateManager;

	// this exists so we can always call .value in el
	static public class FieldProxy
	{
		private GridTab gridTab;
		private int rowNum;
		private String columnName;

		public FieldProxy(GridTab gT, int rowNum, String columnName)
		{
			gridTab = gT;
			this.rowNum = rowNum;
			this.columnName = columnName;
		}

		public Object getValue()
		{
			return convertToValue();

		}

		private Integer savedRow = null;

		private GridField doNavigateAndGetField() {
			if (savedRow != null) {
				throw new IllegalStateException("doNavigate Called with row already saved!!!");
			}
			savedRow = new Integer(gridTab.getCurrentRow());
			gridTab.navigate(this.rowNum);
			return gridTab.getField(columnName);

		}

		private void resetNavigation() {
			if (savedRow == null) {
				throw new IllegalStateException("resetNavigate Called with no saved row!!!");
			}
			gridTab.navigate(savedRow.intValue());
			savedRow = null;
		}

		public String getLabel()
		{
			//log.info("returning label "+gridField.getHeader());
			try 
			{
				GridField gridField = doNavigateAndGetField();

				if(gridField.getDisplayType()==28)
				{
					if (gridField.getColumnName().equals("PaymentRule"))
					{
						return readReference(195);
						//this.setForeground(Color.blue);
						//setIcon(Env.getImageIcon("Payment16.gif"));    //  29*14
					}
					else if (gridField.getColumnName().equals("DocAction"))
					{
						return readReference(135);
						//this.setForeground(Color.blue);
						//setIcon(Env.getImageIcon("Process16.gif"));    //  16*16
					}
					else if (gridField.getColumnName().equals("CreateFrom"))
					{
						//setIcon(Env.getImageIcon("Copy16.gif"));       //  16*16
					}
					else if (gridField.getColumnName().equals("Record_ID"))
					{
						//setIcon(Env.getImageIcon("Zoom16.gif"));       //  16*16
						// FIXME: this probably does something that needs to be emulated in our jsf layer
						//this.setText(Msg.getMsg(Env.getCtx(), "ZoomDocument"));
					}
					else if (gridField.getColumnName().equals("Posted"))
					{
						return readReference(234);
						//this.setForeground(Color.magenta);
						//setIcon(Env.getImageIcon("InfoAccount16.gif"));    //  16*16
					}
				}
				return gridField.getHeader();
			} finally {
				resetNavigation();
			}
		}

		public String getTooltip()
		{
			try 
			{
				GridField gridField = doNavigateAndGetField();
				return gridField.getDescription();
			} finally {
				resetNavigation();
			}
		}

		// arg may need to be an Object to handle all cases
		public void setValue(Object o)
		{
			try 
			{
				GridField gridField = doNavigateAndGetField();
				if (o != null) 
				{
					if (gridField.getDisplayType() == 12)
					{
						// gridField.setValueValidate throws exceptions when trying to parse a BigDecimal from a string
						gridField.setValue(o,true);
					}
					else
					{
						String s=o.toString();
						if ((gridField.getDisplayType() == 15 || gridField.getDisplayType() == 16) && s.equals(""))
						{
							// gridField.setValueValidate throws exceptions on empty string for date fields.
							return;
						}
						// some values may not be writable, should catch it in DynamicFieldPropertyResolver.isReadOnly
						if(s.equals("true"))
							s="Y";
						else if(s.equals("false"))
							s="N";

						String ret=gridField.setValueValidate(s,true);
						if(ret!=null)
							throw new PropertyNotFoundException(ret);
						//log.info("got back from setValueValidate (null is success) "+ret);
					}
				}
			} finally {
				resetNavigation();
			}
		}

		// this is for conversion in jsf
		public Class getType()
		{
			// TODO: there probably need to be other types here (Date in the future, for one)
			try 
			{
				GridField gridField = doNavigateAndGetField();
				if(gridField.getDisplayType()==20)
					return java.lang.Boolean.class;
				return java.lang.String.class;
			} finally {
				resetNavigation();
			}
		}

		private Object convertToValue()
		{
			try 
			{
				GridField gridField = doNavigateAndGetField();

				if (gridField.isDisplayed(true))
				{

					int displayType=gridField.getDisplayType();
					// TODO: this only has like... 3 or 4 unique cases, refactor for reuse check display type to genereate jsf code for each specific component

					switch (displayType)
					{
					case 10:
					case 11:
					case 13:
					case 14:
					case 22:
					case 29:
					case 33:
					case 34:
					case 35:
					case 36:
					case 37:
						// text box
						return gridField.getValue();
					case 12:
						// Amount
						if (gridField.getValue() != null) 
						{
							return gridField.getValue();
						}
						return null;
					case 15:
						// may want to convert this to a java.util.Date, same for case 16 date. have to chop timestamp off of gridField value
						String dateString;
						if(gridField.getValue()!=null)
						{
							String[] dateArray=gridField.getValue().toString().split(" ");
							dateString=dateArray[0];
							return dateString;
						}
						return null;
					case 16:
						// probably unused so far
						// date + time
						return gridField.getValue();
					case 17:
					case 18:
					case 19:
						// lookup
						ArrayList tmp=gridField.getLookup().getData(true, true, true, true);
						ArrayList<NamePair> valueNamePairList=tmp;
						// ArrayList<SelectItem> selectItems=new ArrayList<SelectItem>();
						// for(int i=0; i<valueNamePairList.size(); i++)
						//  selectItems.add(new SelectItem(valueNamePairList.get(i).getID(),valueNamePairList.get(i).getName()));
						// return selectItems;

						HashMap<String,String> lookupKeyValue = new HashMap<String,String>();
						for (int i = 0; i < valueNamePairList.size(); i++)
						{
							lookupKeyValue.put(valueNamePairList.get(i).getName(), valueNamePairList.get(i).getName());
						}
						return lookupKeyValue;
					case 20:
						// Check box
						return gridField.getValue();
					case 21:
						// Address
						return gridField.getValue();
					case 23:
					case 24:
					case 25:
					case 27:
						// these last are unimplemented and tricky at best
					case 28:
						// custom component
						// break;
					case 30:
						// search
						// TODO this needs to be fixed.  
						return (new HashMap());
					case 31:
						// locator
						// break;
					case 32:
						// image
						// break;
					default:
						log.warning("Display Type not handled for "+gridField.getColumnName()+": "
								+gridField.getDisplayType());
					return "";
					}
				}
				// TODO this is not an effective fix.  needs to be figured out and fixed
				return new HashMap();
			} finally {
				resetNavigation();
			}
		}

		// stolen from compiere, this should eventually be abstracted from this layer
		private String readReference(int AD_Reference_ID)
		{
			//m_values = new HashMap<String,String>();
			String SQL;
			if (Env.isBaseLanguage(Env.getCtx(), "AD_Ref_List"))
				SQL = "SELECT Name FROM AD_Ref_List WHERE AD_Reference_ID=?";
			else
				SQL = "SELECT t.Name FROM AD_Ref_List l, AD_Ref_List_Trl t "
					+ "WHERE l.AD_Ref_List_ID=t.AD_Ref_List_ID"
					+ " AND t.AD_Language='" + Env.getAD_Language(Env.getCtx()) + "'"
					+ " AND l.AD_Reference_ID=?";

			try
			{
				PreparedStatement pstmt = DB.prepareStatement(SQL, null);
				pstmt.setInt(1, AD_Reference_ID);
				ResultSet rs = pstmt.executeQuery();
				rs.next();
				String name = rs.getString(1);
				rs.close();
				pstmt.close();
				return name;
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, SQL, e);
			}
			return "ERROR";

		}   //  readReference
	} // end FieldProxy

	public DynamicFieldLookup(FacesContext ctx)
	{
		facesContext=ctx;
		tabStateManager=new TabStateManager(facesContext);
	}

	public FieldProxy getProxy()
	{
		return new FieldProxy(gridTab, gridTab.getCurrentRow(), columnName);
	}

	/**
	 * sets the GridTab for this field
	 * @param l
	 */
	public void setTabNumber(Long l)
	{
		//log.info("setting tab number");
		tabNumber=l;
		// window # is available as a hidden field named windowNo
		// GridWindows are stored in a session Map called grid
		HashMap<Integer,GridWindow> grids=(HashMap<Integer,GridWindow>)facesContext.getExternalContext().getSessionMap().get("grids");
		//log.info("grids is "+grids+" getting windowNo "+tabStateManager.getUIState().getWindowNo());
		GridWindow gridWindow=grids.get(new Integer(tabStateManager.getUIState().getWindowNo()));
		//log.info("gridWindow is "+gridWindow);
		gridTab=gridWindow.getTab((int)tabNumber.longValue());
		//log.info("gridTab open status "+gridTab.isOpen());
	}

	/**
	 * 
	 */
	public void setColumnName(String s)
	{
		// error checking for testing in development, all of this but the final assignment can be commented out in release
		boolean b=false;
		GridField[] gridFields=gridTab.getFields();
		for(int i=0;i<gridFields.length;i++)
			if(gridFields[i].getColumnName().equals(s))
			{
				b=true;
				break;
			}
		if(!b)
			throw new PropertyNotFoundException("could not find column name: "+s+" on tab "+tabNumber+".  Check your el");
		columnName=s;
	}

	String getColumnName()
	{
		return columnName;
	}

	/**
	 * @return a list that can be iterated through in a jsf looping construct
	 */
	public List<List<FieldProxy>> getProxies()
	{
		gridTab.getTableModel().open(0);
		List<List<FieldProxy>> tmp=new ArrayList<List<FieldProxy>>();
		//I hope there is a better way to do this, but we will just save off the 
		//current row and then reset it at the end.
		int currentRow = gridTab.getCurrentRow();
		for (int i=0; i < gridTab.getRowCount(); i++) 
		{
			gridTab.navigate(i);
			GridField[] gridFields=gridTab.getFields();
			ArrayList<FieldProxy> fieldProxies=new ArrayList<FieldProxy>();
			log.info("getProxies: got "+gridFields.length+" fields in row "+i);
			for (int j=0;j<gridFields.length;j++)
				fieldProxies.add(new FieldProxy(gridTab, i, gridFields[j].getColumnName()));//gridFields[j]));
			tmp.add(fieldProxies);
		}
		gridTab.navigate(currentRow);
		return tmp;
	}

	/**
	 * @return the selected row in the current gridTab object
	 */
	public Integer getCurrentlySelectedRow() 
	{
		gridTab.getTableModel().open(0);
		return gridTab.getCurrentRow();
	}

	/**
	 * @return the number of rows in the gridTab object
	 */
	public Integer getRowCount() 
	{
		return gridTab.getRowCount();
	}


}
