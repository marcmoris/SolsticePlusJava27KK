/*
 * Created on 24 août 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.compiere.apps.search;

import java.awt.Frame;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.compiere.apps.AEnv;
import org.compiere.apps.ALayout;
import org.compiere.apps.ALayoutConstraint;
import org.compiere.minigrid.IDColumn;
import org.compiere.model.Info_Column;
import org.compiere.model.MQuery;
import org.compiere.plaf.CompierePLAF;
import org.compiere.swing.CLabel;
import org.compiere.swing.CTextField;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InfoDistributionBooklet extends Info
{
	/**
	 *	Standard Constructor
	 *  @param frame frame
	 *  @param modal modal
	 *  @param WindowNo WindowNo
	 *  @param  value   Query value Name or Value if contains numbers
	 *  @param isSOTrx  if false, query vendors only
	 *  @param multiSelection multiple selection
	 *  @param whereClause where clause
	 */
	public InfoDistributionBooklet(Frame frame, boolean modal, int WindowNo, String value, boolean isSOTrx, boolean multiSelection, String whereClause)
	{
		super (frame, modal, WindowNo, "P_Distribution_Booklet", "P_Distribution_Booklet_ID", multiSelection, whereClause);
		setTitle(Msg.getMsg(Env.getCtx(), "InfoDistributionBooklet"));
		m_isSOTrx = isSOTrx;
		//
		statInit();
		initInfo (value, whereClause);
		//
		int no = p_table.getRowCount();
		setStatusLine(Integer.toString(no) + " " + Msg.getMsg(Env.getCtx(), "SearchRows_EnterQuery"), false);
		setStatusDB(Integer.toString(no));
		//	AutoQuery
		if (value != null && value.length() > 0)
			executeQuery();
		p_loadedOK = true;
		//	Focus
		fieldValue.requestFocus();

		AEnv.positionCenterWindow(frame, this);
	}	//	InfoDistribution_Booklet

	/** SalesOrder Trx          */
	private boolean 		m_isSOTrx = false;


	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (InfoDistributionBooklet.class);

	/** From Clause             */
	private static String s_partnerFROM = "P_Distribution_Booklet";

	/**  Array of Column Info    */
	private static Info_Column[] s_Layout = {
		new Info_Column(" ", "P_Distribution_Booklet.P_Distribution_Booklet_ID", IDColumn.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Value"), "P_Distribution_Booklet.Value", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Name"), "P_Distribution_Booklet.Name", String.class)
	};
	
	/**
	 *	Get Layout
	 *	@return array of Column_Info
	 */
	protected Info_Column[] getInfoColumns()
	{
		return s_Layout;
	}	//	getInfoColumns


	//
	private CLabel labelValue = new CLabel();
	private CTextField fieldValue = new CTextField(10);
	private CLabel labelName = new CLabel();
	private CTextField fieldName = new CTextField(10);
	
	/**
	 *	Static Setup - add fields to parameterPanel
	 */
	private void statInit()
	{
		labelValue.setText(Msg.getMsg(Env.getCtx(), "Value"));
		fieldValue.setBackground(CompierePLAF.getInfoBackground());
		fieldValue.addActionListener(this);

		labelName.setText(Msg.getMsg(Env.getCtx(), "Name"));
		fieldName.setBackground(CompierePLAF.getInfoBackground());
		fieldName.addActionListener(this);

		//
		parameterPanel.setLayout(new ALayout());
		// Line 1
		parameterPanel.add(labelValue, new ALayoutConstraint(0,0));
		parameterPanel.add(fieldValue, null);
		parameterPanel.add(labelName, null);
		parameterPanel.add(fieldName, null);
	}	//	statInit

	/**
	 *	Dynamic Init
	 *  @param value value
	 *  @param whereClause where clause
	 */
	private void initInfo(String value, String whereClause)
	{

		//	Create Grid
		StringBuffer where = new StringBuffer();
		where.append("P_Distribution_Booklet.IsActive='Y'");
		if (whereClause != null && whereClause.length() > 0)
			where.append(" AND ").append(whereClause);
		//
		prepareTable(s_partnerFROM,
			where.toString(),
			"P_Distribution_Booklet.Value");

		//  Set Value
		if (value == null)
			fieldValue.setText("%");
		if (!value.endsWith("%"))
			fieldValue.setText(value + "%");
		
	}	//	initInfo

	/*************************************************************************/

	/**
	 *	Construct SQL Where Clause and define parameters.
	 *  (setParameters needs to set parameters)
	 *  Includes first AND
	 *  @return WHERE clause
	 */
	String getSQLWhere()
	{
		StringBuffer where = new StringBuffer();

		//	=> Value
		String value = fieldValue.getText().toUpperCase();
		if (!(value.equals("") || value.equals("%")))
			where.append("and UPPER(P_Distribution_Booklet.Value) LIKE ?");

		//  => Name
		String name = fieldName.getText().toUpperCase();
		if (!(name.equals("") || name.equals("%")))
			where.append("and UPPER(P_Distribution_Booklet.Name) LIKE ?");
				
		return where.toString();
	}	//	getSQLWhere

	/**
	 *  Set Parameters for Query.
	 *  (as defined in getSQLWhere)
	 *  @param pstmt pstmt
	 *  @throws SQLException
	 */
	void setParameters(PreparedStatement pstmt, boolean forCount) throws SQLException
	{
		int index = 1;
		//	=> Value
		String value = fieldValue.getText().toUpperCase();
		if (!(value.equals("") || value.equals("%")))
		{
			if (!value.endsWith("%"))
				value += "%";
			pstmt.setString(index++, value);
		}
		//	=> Name
		String name = fieldName.getText().toUpperCase();
		if (!(name.equals("") || name.equals("%")))
		{
			if (!name.endsWith("%"))
				name += "%";
			pstmt.setString(index++, name);
		}
	}   //  setParameters

	/*************************************************************************/

	/**
	 *  Save Selection Details
	 *  Get Location/Partner Info
	 */
	public void saveSelectionDetail()
	{
		int row = p_table.getSelectedRow();
		if (row == -1)
			return;

		//  publish for Callout to read
		Integer ID = getSelectedRowKey();
		Env.setContext(Env.getCtx(), Env.WINDOW_INFO, Env.TAB_INFO, "P_Distribution_Booklet_ID", ID == null ? "0" : ID.toString());
	}   //  saveSelectionDetail


	/*************************************************************************/
	/**
	 *	Zoom
	 */
	void zoom()
	{
		Integer P_Distribution_Booklet_ID = getSelectedRowKey();
		if (P_Distribution_Booklet_ID == null)
			return;
		MQuery query = new MQuery("P_Distribution_Booklet");
		query.addRestriction("P_Distribution_Booklet_ID", MQuery.EQUAL, P_Distribution_Booklet_ID);
		zoom (123, query);
	}	//	zoom

	/**
	 *	Has Zoom
	 *  @return true
	 */
	boolean hasZoom()
	{
		return true;
	}	//	hasZoom

	/**
	 *	Customize
	 */
	void customize()
	{
	}	//	customize

	/**
	 *	Has Customize
	 *  @return false
	 */
	boolean hasCustomize()
	{
		return false;	//	for now
	}	//	hasCustomize
}
