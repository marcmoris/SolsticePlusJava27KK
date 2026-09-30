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

public class InfoGainParameter extends Info 
{
    /*
     * Création des contrôles
     */
	private CLabel labelValue = new CLabel();
	private CTextField fieldValue = new CTextField(10);
	private CLabel labelName = new CLabel();
	private CTextField fieldName = new CTextField(10);
	
	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (InfoEmployee.class);
    
	/*
	 * Constantes
	 */
	/** From Clause             */
	private static String s_partnerFROM = "P_Gain_Parameter, P_Gain";

	/**  Array of Column Info    */
	private static Info_Column[] s_Layout = {
		new Info_Column(" ", "P_Gain_Parameter.P_Gain_Parameter_ID", IDColumn.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Value"), "P_Gain.Value", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Name"), "P_Gain.Name", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Description"), "P_Gain.Description", String.class)
	};

	/**
	 *	Get Layout
	 *	@return array of Column_Info
	 */
	protected Info_Column[] getInfoColumns()
	{
		return s_Layout;
	}	//	getInfoColumns


	
	/**
	 * Constructeur
	 */
    public InfoGainParameter(Frame frame, boolean modal, int WindowNo, String value, boolean multiSelection, String whereClause)
    {
        super(frame, modal, WindowNo, "P_Gain_Parameter", "P_Gain_Parameter_ID", multiSelection, whereClause);
		setTitle("test");
		//
        initWindow();
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
    }

    /**
     * On retourne le where SQL
     */
    String getSQLWhere()
    {
    	StringBuffer where = new StringBuffer();

    	where.append(" AND P_Gain_Parameter.P_Gain_ID = P_Gain.P_Gain_ID ");
		//	  => Value
		String value = fieldValue.getText().toUpperCase();
		if (!(value.equals("") || value.equals("%")))
			where.append(" AND UPPER(P_Gain.Value) LIKE ?");

		//  => Name
		String name = fieldName.getText().toUpperCase();
		if (!(name.equals("") || name.equals("%")))
			where.append(" AND UPPER(P_Gain.Name) LIKE ?");
				
		return where.toString();
		
        /*if(this.fieldName.getText() != null && !this.fieldName.getText().equals("")
                && this.fieldValue.getText() != null && !this.fieldValue.getText().equals(""))
            return " and (P_Gain.Value like '" + this.fieldValue.getText() + "' " 
            + (this.checkAND.isSelected() ? "and" : "or") + " P_Gain.Name like '" + this.fieldName.getText() + "')";
        if(this.fieldName.getText() != null && !this.fieldName.getText().equals(""))
            return " and P_Gain.Name like '" + this.fieldName.getText() + "'";
        if(this.fieldValue.getText() != null && !this.fieldValue.getText().equals(""))
            return " and P_Gain.Value like '" + this.fieldValue.getText() + "'";
        return "";*/
    }

    void setParameters(PreparedStatement pstmt, boolean forCount) throws SQLException
    {
    	int index = 1;

		//  => Value
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
    }
    
    /**
     * On positionne les champs dans la fenêtre
     */
    private void initWindow()
    {
		this.labelValue.setText(Msg.getMsg(Env.getCtx(), "Value"));
		this.fieldValue.setBackground(CompierePLAF.getInfoBackground());
		this.fieldValue.addActionListener(this);

		this.labelName.setText(Msg.getMsg(Env.getCtx(), "Name"));
		this.fieldName.setBackground(CompierePLAF.getInfoBackground());
		this.fieldName.addActionListener(this);

		//
		this.parameterPanel.setLayout(new ALayout());
		// Line 1
		this.parameterPanel.add(this.labelValue, new ALayoutConstraint(0,0));
		this.parameterPanel.add(this.fieldValue, null);
		this.parameterPanel.add(this.labelName, null);
		this.parameterPanel.add(this.fieldName, null);
	}

	/**
	 *	Dynamic Init
	 *  @param value value
	 *  @param whereClause where clause
	 */
	private void initInfo(String value, String whereClause)
	{
		//	Create Grid
		StringBuffer where = new StringBuffer();
		where.append("P_Gain.IsActive='Y'");
		if (whereClause != null && whereClause.length() > 0)
			where.append(" AND ").append(whereClause);
		//
		prepareTable(s_partnerFROM,
			where.toString(),
			"P_Gain.Value");

		//  Set Value
		if (value == null)
			value = "%";
		if (!value.endsWith("%"))
			value += "%";

		fieldValue.setText(value);
	}	//	initInfo
	
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
		Env.setContext(Env.getCtx(), Env.WINDOW_INFO, Env.TAB_INFO, "P_Gain_ID", ID == null ? "0" : ID.toString());
	}   //  saveSelectionDetail

	/**
	 *	Zoom
	 */
	void zoom()
	{
		Integer P_Gain_Parameter_ID = getSelectedRowKey();
		if (P_Gain_Parameter_ID == null)
			return;
		MQuery query = new MQuery("P_Gain_Parameter");
		query.addRestriction("P_Gain_Parameter_ID", MQuery.EQUAL, P_Gain_Parameter_ID);
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
