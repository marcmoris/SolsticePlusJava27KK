/*
 * Created on 24 août 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.compiere.apps.search;

import java.awt.Frame;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.apps.AEnv;
import org.compiere.apps.ALayout;
import org.compiere.apps.ALayoutConstraint;
import org.compiere.grid.ed.VComboBox;
import org.compiere.grid.ed.VDate;
import org.compiere.minigrid.IDColumn;
import org.compiere.model.Info_Column;
import org.compiere.model.MQuery;
import org.compiere.model.MRole;
import org.compiere.plaf.CompierePLAF;
import org.compiere.swing.CLabel;
import org.compiere.swing.CTextField;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Language;
import org.compiere.util.Msg;

import solstice.custom.DictionaryEntry;
import solstice.process.PgiUtil;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InfoPeriod extends Info
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
	public InfoPeriod(Frame frame, boolean modal, int WindowNo, String value, boolean isSOTrx, boolean multiSelection, String whereClause)
	{
		super (frame, modal, WindowNo, "P_Period", "P_Period_ID", multiSelection, whereClause);
		setTitle(Msg.getMsg(Env.getCtx(), "InfoPeriod"));
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
		fieldName.requestFocus();

		AEnv.positionCenterWindow(frame, this);
	}	//	InfoDistribution_Booklet

	/** Trx          */
	private boolean 		m_isSOTrx = false;


	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (InfoPeriod.class);

	/** From Clause             */
	private static String s_periodFROM = "P_Period "
		                               + " LEFT OUTER JOIN P_Year ON ( P_Year.P_Year_ID = P_Period.P_Year_ID ) ";

    private static Language language = Env.getLanguage(Env.getCtx());

	
	/**  Array of Column Info    */
	private static Info_Column[] s_periodLayout = {
		new Info_Column(" ", "P_Period.P_Period_ID", IDColumn.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Name"), "P_Period.Name", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "StartDate"), "P_Period.StartDate", Timestamp.class),
		new Info_Column(Msg.translate(Env.getCtx(), "EndDate"), "P_Period.EndDate", Timestamp.class),
		new Info_Column(Msg.translate(Env.getCtx(), "PeriodStatus"), new PgiUtil.ReferencedQueryField("P_PeriodControl Status", "P_Period", "PeriodStatus").getSQLColumn() , String.class)
	};

	/**
	 *	Get Layout
	 *	@return array of Column_Info
	 */
	protected Info_Column[] getInfoColumns()
	{
		return s_periodLayout;
	}	//	getInfoColumns

	//
	private CLabel labelCalendar = new CLabel();
	private VComboBox fieldCalendar = new VComboBox();
	private CLabel labelFrequency = new CLabel();
	private VComboBox fieldFrequency = new VComboBox();
	private CLabel labelYear = new CLabel();
	private VComboBox fieldYear = new VComboBox();
	private CLabel labelName = new CLabel();
	private CTextField fieldName = new CTextField(10);
	private CLabel labelStartDate = new CLabel();
	private VDate fieldStartDate = new VDate();
	private CLabel labelEndDate = new CLabel();
	private VDate fieldEndDate = new VDate();
	private CLabel labelPeriodStatus = new CLabel();
	private VComboBox fieldPeriodStatus = new VComboBox();
	
	/**
	 *	Static Setup - add fields to parameterPanel
	 */
	private void statInit()
	{
		labelCalendar.setText(Msg.translate(Env.getCtx(), "P_Calendar_ID"));
		fieldCalendar.setBackground(CompierePLAF.getInfoBackground());
		fillCalendar();
		fieldCalendar.addActionListener(this);

		labelFrequency.setText(Msg.translate(Env.getCtx(), "P_Frequency_ID"));
		fieldFrequency.setBackground(CompierePLAF.getInfoBackground());
		fillFrequency();
		fieldFrequency.addActionListener(this);

		labelYear.setText(Msg.translate(Env.getCtx(), "P_Year_ID"));
		fieldYear.setBackground(CompierePLAF.getInfoBackground());
		fillYear();
		fieldYear.addActionListener(this);

		labelStartDate.setText(Msg.getMsg(Env.getCtx(), "StartDate"));
		fieldStartDate.setBackground(CompierePLAF.getInfoBackground());
		fieldStartDate.addActionListener(this);

		labelEndDate.setText(Msg.getMsg(Env.getCtx(), "EndDate"));
		fieldEndDate.setBackground(CompierePLAF.getInfoBackground());
		fieldEndDate.addActionListener(this);

		labelPeriodStatus.setText(Msg.getMsg(Env.getCtx(), "PeriodStatus"));
		fieldPeriodStatus.setBackground(CompierePLAF.getInfoBackground());
		fillPeriodStatus();
		fieldPeriodStatus.addActionListener(this);

		labelName.setText(Msg.getMsg(Env.getCtx(), "Name"));
		fieldName.setBackground(CompierePLAF.getInfoBackground());
		fieldName.addActionListener(this);

		//
		parameterPanel.setLayout(new ALayout());

		// Line 1
		parameterPanel.add(labelCalendar, new ALayoutConstraint(0,0));
		parameterPanel.add(fieldCalendar, null);
		parameterPanel.add(labelYear, null);
		parameterPanel.add(fieldYear, null);
		// Line 2
		parameterPanel.add(labelFrequency, new ALayoutConstraint(1,0));
		parameterPanel.add(fieldFrequency, null);

		// Line 3
		parameterPanel.add(labelName, new ALayoutConstraint(2,0));
		parameterPanel.add(fieldName, null);
		parameterPanel.add(labelPeriodStatus, null);
		parameterPanel.add(fieldPeriodStatus, null);

		// Line 4		
		parameterPanel.add(labelStartDate, new ALayoutConstraint(3,0));
		parameterPanel.add(fieldStartDate, null);
		parameterPanel.add(labelEndDate, null);
		parameterPanel.add(fieldEndDate, null);
	}	//	statInit


	private void fillCalendar()
	{
	    String sql = null;

	    if(language.isBaseLanguage())
	    {
	    	sql = "Select P_Calendar.P_Calendar_ID, Name "
	    		+ " From P_Calendar "
	    		;
	    }
	    else
	    {
	    	sql = "Select P_Calendar.P_Calendar_ID, isnull( P_Calendar_Trl.Name, P_Calendar.Name  ) AS Name "
	    		+ " From P_Calendar"
	    		+ " LEFT OUTER JOIN P_Calendar_Trl ON P_Calendar.P_Calendar_ID = P_Calendar_Trl.P_Calendar_ID AND P_Calendar_Trl.AD_Language = '" + language.getLocale()+  "'"
	    		;
	    }

   		sql = sql + " Where P_Calendar." + MRole.getDefault().getClientWhere(false) + " Order By Name ";// fully qualidfied - RO 

	    this.fieldCalendar.removeAllItems();
//        this.fieldCalendar.addItem(new DictionaryEntry("", ""));
	    try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sql, null);
	        ResultSet rs = stmt.executeQuery();
	        
		    int index = 0;
	        while(rs.next())
	        {
			    index++;
				KeyNamePair kn = new KeyNamePair (rs.getInt(1), rs.getString(2));
	            this.fieldCalendar.addItem(kn);
	        }
	        this.fieldCalendar.setSelectedIndex(0);
	        
	        rs.close();
	        stmt.close();
	    }
	    catch (SQLException e)
	    {
	        s_log.log(Level.SEVERE, "fillCalendar", e);
	    }
	}

	private void fillFrequency()
	{
	    String sql = null;

	    if(language.isBaseLanguage())
	    {
    		sql = "Select P_Frequency.P_Frequency_ID, Name, isDefault FROM P_Frequency ";
	    		;
	    }
	    else
	    {
    		sql = "Select P_Frequency.P_Frequency_ID, isnull( P_Frequency_Trl.Name, P_Frequency.Name  ) AS Name, isDefault " 
    			+ " FROM P_Frequency "
    		    + " LEFT OUTER JOIN P_Frequency_Trl ON P_Frequency.P_Frequency_ID = P_Frequency_Trl.P_Frequency_ID AND P_Frequency_Trl.AD_Language = '" + language.getLocale() + "'"
    		    ;
	    	
	    }

   		sql = sql + " Where P_Frequency." + MRole.getDefault().getClientWhere(false) + " Order By Name ";// fully qualidfied - RO 

   		this.fieldFrequency.removeAllItems();
//        this.fieldFrequency.addItem(new DictionaryEntry("", ""));
	    try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sql, null);
	        ResultSet rs = stmt.executeQuery();
	        
		    int index = 0;
	        while(rs.next())
	        {
				KeyNamePair kn = new KeyNamePair (rs.getInt(1), rs.getString(2));
	            this.fieldFrequency.addItem(kn);
				if(rs.getString("IsDefault").equals("Y"))
					this.fieldFrequency.setSelectedIndex(index);
	            else
	    	        this.fieldFrequency.setSelectedIndex(0);
			    index++;
	        }
	        
	        rs.close();
	        stmt.close();
	    }
	    catch (SQLException e)
	    {
	        s_log.log(Level.SEVERE, "fillFrequency", e);
	    }
	}

	
	private void fillYear()
	{
	    String sql = null;
        sql = "Select P_Year_ID, Year "
	        + " FROM P_Year ";
	   	sql = sql + " Where P_Year." + MRole.getDefault().getClientWhere(false)	// fully qualidfied - RO 
	        + " Order By Year Desc "
	        ;
	    this.fieldYear.removeAllItems();
        this.fieldYear.addItem(new DictionaryEntry("", ""));
	    try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sql, null);
	        ResultSet rs = stmt.executeQuery();
	        
		    int index = 0;
	        while(rs.next())
	        {
			    index++;
				KeyNamePair kn = new KeyNamePair (rs.getInt(1), rs.getString(2));
	            this.fieldYear.addItem(kn);
	            if ( Env.getContextAsInt(Env.getCtx(), "#P_Year_ID") > 0 )
	    	    {
					if(rs.getInt(1) == Env.getContextAsInt(Env.getCtx(), "#P_Year_ID"))
						this.fieldYear.setSelectedIndex(index);
	            	
	    	    }
	            else
					this.fieldYear.setSelectedIndex(1);

	        }
	        
	        rs.close();
	        stmt.close();
	    }
	    catch (SQLException e)
	    {
	        s_log.log(Level.SEVERE, "fillYear", e);
	    }
	}

	private void fillPeriodStatus()
	{
	    String sql = null;
	    if(language.isBaseLanguage())
	    {
	        sql = "Select AD_Ref_List.Value, AD_Ref_List.Name"
	            + " FROM AD_Reference Inner join AD_Ref_List"
	            + " On AD_Reference.AD_Reference_ID = AD_Ref_List.AD_Reference_ID"
	            + " Where AD_Reference.name Like 'P_PeriodControl Status'";
	    }
	    else
	    {
	        sql = "Select AD_Ref_List.Value, AD_Ref_List_TRL.Name"
	            + " FROM AD_Reference inner join (AD_Ref_List inner join AD_Ref_List_TRL"
	            + " On AD_Ref_List.AD_Ref_List_ID = AD_Ref_List_TRL.AD_Ref_List_ID)"
	            + " On AD_Reference.AD_Reference_ID = AD_Ref_List.AD_Reference_ID"
	            + " Where AD_Reference.name Like 'P_PeriodControl Status'"
	            + " And AD_Ref_List_TRL.AD_Language = '" + language.getAD_Language() + "'";
	    }
	    
	    this.fieldPeriodStatus.removeAllItems();
        this.fieldPeriodStatus.addItem(new DictionaryEntry("", ""));
	    try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sql, null);
	        ResultSet rs = stmt.executeQuery();
	        
	        while(rs.next())
	        {
	            this.fieldPeriodStatus.addItem(new DictionaryEntry(rs.getString("Value"), rs.getString("Name")));
	        }
	        
	        rs.close();
	        stmt.close();
	    }
	    catch (SQLException e)
	    {
	        s_log.log(Level.SEVERE, "fillPeriodStatus", e);
	    }

	    this.fieldPeriodStatus.setSelectedIndex(0);

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
		where.append("P_Period.IsActive='Y'");
		if (whereClause != null && whereClause.length() > 0)
			where.append(" AND ").append(whereClause);
		//
		prepareTable( s_periodFROM,
			where.toString(),
			"P_Period.StartDate DESC");

		//  Set Value
		if (value == null)
			value = "%";
		if (!value.endsWith("%"))
			value += "%";

		fieldName.setText(value);
		
		executeQuery();
		
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
		
		//	=> Name
		String name = fieldName.getText().toUpperCase();
		if (!(name.equals("") || name.equals("%")))
			//list.add ("substring(P_Period.Name, 0, 5) + substring(P_Period.Name, 6, 8) LIKE ?");
		    where.append(" And P_Period.Name LIKE ?");

		//	=> StartDate
		if(this.fieldStartDate.getValue() != null)
			where.append(" And P_Period.StartDate = ?");
		
		//	=> EndDate
		if(this.fieldEndDate.getValue() != null)
			where.append(" And P_Period.EndDate = ?");
		
		//	=> PeriodStatus
		if(this.fieldPeriodStatus.getSelectedIndex() > 0)
			where.append(" And P_Period.PeriodStatus = ?");

		//	=> Calendar
		if(this.fieldCalendar.getSelectedIndex() >= 0)
			where.append(" And P_Year.P_Calendar_ID = ?");

		//	=> Year
		if(this.fieldYear.getSelectedIndex() >= 0)
			where.append(" And P_Period.P_Year_ID = ?");

		// => Frequency 
		if(this.fieldFrequency.getSelectedIndex() >= 0)
			where.append(" And P_Period.P_Frequency_ID = ?");

//        P_Period period = P_Period.getOpenPeriod(Env.getCtx(), null);
//		where.append( " And P_Frequency_ID = "  + period.getP_Frequency_ID()); 
        
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
		//  => Name
		String name = fieldName.getText().toUpperCase();
		if (!(name.equals("") || name.equals("%")))
		{
			if (!name.endsWith("%"))
				name += "%";
			pstmt.setString(index++, name);
		}
		//	=> StartDate
		if(this.fieldStartDate.getValue() != null)
		{
		    pstmt.setTimestamp(index++, this.fieldStartDate.getTimestamp());
		}
		//	=> EndDate
		if(this.fieldEndDate.getValue() != null)
		{
		    pstmt.setTimestamp(index++, this.fieldEndDate.getTimestamp());
		}
		//	=> PeriodStatus
		if(this.fieldPeriodStatus.getSelectedIndex() > 0)
		{
		    pstmt.setString(index++, (String)((DictionaryEntry)this.fieldPeriodStatus.getSelectedItem()).getKey());
		}

		//	=> Calendar
		if(this.fieldCalendar.getSelectedIndex() >= 0)
		{
			KeyNamePair p2 = (KeyNamePair)fieldCalendar.getSelectedItem();
		    pstmt.setInt(index++, p2.getKey());
		}

		//	=> Year
		if(this.fieldYear.getSelectedIndex() >= 0)
		{
			KeyNamePair p2 = (KeyNamePair)fieldYear.getSelectedItem();
		    pstmt.setInt(index++, p2.getKey());
		}

		//	=> Frequency
		if(this.fieldFrequency.getSelectedIndex() >= 0)
		{
			KeyNamePair p2 = (KeyNamePair)fieldFrequency.getSelectedItem();
		    pstmt.setInt(index++, p2.getKey());
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
		Env.setContext(Env.getCtx(), Env.WINDOW_INFO, Env.TAB_INFO, "P_Period_ID", ID == null ? "0" : ID.toString());
	}   //  saveSelectionDetail



	/**
	 *	Zoom
	 */
	void zoom()
	{
		Integer P_Period_ID = getSelectedRowKey();
		if (P_Period_ID == null)
			return;
		MQuery query = new MQuery("P_Period");
		query.addRestriction("P_Period_ID", MQuery.EQUAL, P_Period_ID);
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
