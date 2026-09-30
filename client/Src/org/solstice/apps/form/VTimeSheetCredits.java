/******************************************************************************
 * Product: Solstice+ Payroll & Human Resources Management                    *
 * Copyright (C) 2008 ProGestion Informatique, Inc. All Rights Reserved.      *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ProGestion Informatique, 210-5300 Bld des Galerie, Quebec, G2K 2A2 Canada  *
 * or via info@progestion.net or http://www.progestion.net/license.html       *
 ******************************************************************************/

package org.solstice.apps.form;

import java.awt.BorderLayout;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.table.TableColumnModel;

import org.compiere.swing.CPanel;
import org.compiere.swing.CTable;

import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;

import org.solstice.apps.form.VTimeSheetModel.CreditsModel;

import solstice.model.P_Credits;
import solstice.model.P_Employee;
import solstice.model.P_Period;
import solstice.model.P_Year;
import solstice.utils.TimeUtilSolstice;

import java.sql.Date;
/**
 * @author frabou01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class VTimeSheetCredits extends CPanel {

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (VTimeSheetCredits.class);

	private CTable tblBank = null;

	private VTimeSheetHeader m_header = null; 

	/**
	 * This is the default constructor
	 */
	public VTimeSheetCredits(VTimeSheetHeader header) 
	{
		super();
		initialize();
        m_header = header;

	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private  void initialize() {
		setLayout( new BorderLayout() );
		setBorder(BorderFactory.createCompoundBorder(
	        BorderFactory.createTitledBorder(Msg.getMsg(Env.getCtx(),"Credits")),
		    BorderFactory.createEmptyBorder(0,5,5,5)));
		add(getTblBank().getTableHeader(), BorderLayout.PAGE_START);
		add(getTblBank(), BorderLayout.CENTER);
		
	}
	/**
	 * This method initializes jTable	
	 * 	
	 * @return javax.swing.JTable	
	 */    
	private CTable getTblBank() {
		if (tblBank == null) {
			tblBank = new CTable();
			tblBank.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			tblBank.setShowGrid(true);
		}
		return tblBank;
	}
	
	public BigDecimal LoadVirtualSoldePayment( P_Credits Credits, int employeeID, int paymentId)
	{
		if ( paymentId <= 0 || employeeID <= 0 ) return Env.ZERO;
		BigDecimal val = Env.ZERO;

		if ( Credits.getValue().equals("BV20B")) {
			String sql = "select dbo.Credits_BV20B_Payment( ?, ? )";
			try {
				PreparedStatement sVirtual = DB.prepareStatement(sql, null);
				sVirtual.setInt(1, employeeID );
				sVirtual.setInt(2, paymentId );
				ResultSet tsRsVirtual = sVirtual.executeQuery();
				if ( tsRsVirtual.next() )
					val = tsRsVirtual.getBigDecimal(1);
				tsRsVirtual.close();
				sVirtual.close();
			} catch (Exception e) {
				s_log.log(Level.SEVERE, "VTimesheetCredits.LoadBank", e);
			}
			
		}

		if ( Credits.getValue().startsWith("BV21")) {
			String sql = "select dbo.Credits_BV21_Payment( ?, ?, ? )";
			try {
				PreparedStatement sVirtual = DB.prepareStatement(sql, null);
				sVirtual.setInt(1, Credits.getP_Credits_ID());
				sVirtual.setInt(2, employeeID );
				sVirtual.setInt(3, paymentId );
				ResultSet tsRsVirtual = sVirtual.executeQuery();
				if ( tsRsVirtual.next() )
					val = tsRsVirtual.getBigDecimal(1);
				tsRsVirtual.close();
				sVirtual.close();
			} catch (Exception e) {
				s_log.log(Level.SEVERE, "VTimesheetCredits.LoadBank", e);
			}
			
		}

		
		return val;
	}
	
	public BigDecimal LoadVirtualSolde(  P_Credits Credits,  int employeeID, P_Period period, String trxName, int paymentId)
	{
		if ( period == null || period.getP_Period_ID() <= 0 ) return null;
		
		BigDecimal val = Env.ZERO;
		P_Employee Employee = P_Employee.get(Env.getCtx(), employeeID, trxName);
		if ( Credits.getValue().equals("BV20B") ){
			int iYear = TimeUtilSolstice.getYear( period.getStartDate() );
			P_Year Year = P_Year.getWithValue( Env.getCtx(), iYear, trxName);
			Timestamp CreditsDate = Credits.getCreditsDate( Year, Employee, trxName );
			
			if ( CreditsDate == null ) return null;
			
			Timestamp startDate = null;

			// Krispy employé distributeur en période -1
			Timestamp PeriodstartDate = period.getStartDate();
			if ( Employee.isCustomFieldYesNo01() )
				PeriodstartDate = TimeUtil.addDays(PeriodstartDate, -7);
			
			if ( PeriodstartDate.compareTo(CreditsDate) >= 0 )
			{
				 startDate = CreditsDate;
			}
			else
			{
				 startDate = TimeUtilSolstice.addYear( CreditsDate , -1 );
			}

			String sql = "select dbo.Credits_BV20B( ?, ?, ?, ? )";
			try {
				PreparedStatement sVirtual = DB.prepareStatement(sql, null);
				sVirtual.setInt(1, employeeID );
				sVirtual.setInt(2, paymentId );
				sVirtual.setDate(3, new Date(startDate.getTime() ) );
				sVirtual.setDate(4, new Date(period.getStartDate().getTime() ) );
				ResultSet tsRsVirtual = sVirtual.executeQuery();
				if ( tsRsVirtual.next() )
					val = tsRsVirtual.getBigDecimal(1);
				tsRsVirtual.close();
				sVirtual.close();
			} catch (Exception e) {
				s_log.log(Level.SEVERE, "VTimesheetCredits.LoadBank", e);
			}
			
		}
		
		if ( Credits.getValue().startsWith("BV21") ){
			int iYear = TimeUtilSolstice.getYear( period.getStartDate() );
			P_Year Year = P_Year.getWithValue( Env.getCtx(), iYear, trxName);
			Timestamp CreditsDate = Credits.getCreditsDate( Year, Employee, trxName );
			
			if ( CreditsDate == null ) return null;
			
			Timestamp startDate = null;
			if ( period.getStartDate().compareTo(CreditsDate) >= 0 )
			{
				 startDate = CreditsDate;
			}
			else
			{
				 startDate = TimeUtilSolstice.addYear( CreditsDate , -1 );
			}

			String sql = "select dbo.Credits_BV21( ?, ?, ?, ?, ? )";
			try {
				PreparedStatement sVirtual = DB.prepareStatement(sql, null);
				sVirtual.setInt(1, Credits.getP_Credits_ID() );
				sVirtual.setInt(2, employeeID );
				sVirtual.setInt(3, paymentId );
				sVirtual.setDate(4, new Date(startDate.getTime() ) );

				sVirtual.setDate(5, new Date(period.getStartDate().getTime() ) );
				ResultSet tsRsVirtual = sVirtual.executeQuery();
				if ( tsRsVirtual.next() )
					val = tsRsVirtual.getBigDecimal(1);
				tsRsVirtual.close();
				sVirtual.close();
			} catch (Exception e) {
				s_log.log(Level.SEVERE, "VTimesheetCredits.LoadBank", e);
			}
			
		}
		
		
		return val;
	}

	public BigDecimal LoadVirtualSolde(  P_Credits Credits,  int employeeID, P_Period period, String trxName)
	{
		int paymentId = (m_header != null && m_header.getTimeSheet() != null) ? m_header.getTimeSheet().getP_Payment_ID() : 0;
		return LoadVirtualSolde(Credits, employeeID, period, trxName, paymentId);
	}
	
	public CreditsModel buildBankModel(int timesheetID, int employeeID, int periodID, int paymentID) {
		CreditsModel model = new CreditsModel();
		if ( employeeID <= 0 || periodID <= 0 ) {
			return model;
		}
		String sqlQuery = null;
		
		if (Env.isBaseLanguage(Env.getCtx(), "AD_Ref_List"))
			sqlQuery =
			"SELECT P_Employee_Credits.P_Credits_ID, pc.Name, dbo.FN_Credits_Sld_Display( isnull( Sum( PMovementVariation ), 0 ), pc.DisplayUom ) Total, pc.Value, pu.VALUE DisplayValue, isnull( pc.DisplayUom, pu.VALUE) DisplayUom,  max(P_Employee_Credits.P_Employee_Credits_ID) as P_Employee_Credits_ID, P_Employee_Credits.P_Employee_ID  " + 
			"FROM 	P_Credits pc, P_UOM pu, P_Employee_Credits  " +
			"LEFT OUTER JOIN P_Credits_Movement ON P_Employee_Credits.P_Employee_ID = P_Credits_Movement.P_Employee_ID " +
			"                                    AND P_Employee_Credits.P_Credits_ID = P_Credits_Movement.P_Credits_ID " +
//			"                                    AND PMovementDate <= ? " +
			"                                    AND PMovementDate < ? " +
	   	    "  AND ( P_Credits_Movement.P_Payment_ID <> ? OR P_Credits_Movement.P_Payment_ID IS NULL )" +
			"WHERE P_Employee_Credits.P_Employee_ID = ? " +
			"AND P_Employee_Credits.P_Credits_ID = pc.P_CREDITS_ID " + 
			"AND P_Employee_Credits.IsActive = 'Y' " +
			"AND pc.IsActive = 'Y' " +            
			"AND pu.P_UOM_ID = pc.P_UOM_ID " +
			"AND pc.IsDisplayedCredits = 'Y' " +
			"AND P_Employee_Credits.EffectIn = ( Select Max( Effectin ) from P_Employee_Credits M Where M.P_Employee_id = P_Employee_Credits.P_Employee_ID and M.P_Credits_ID = P_Employee_Credits.P_Credits_ID ) " +
			"GROUP BY P_Employee_Credits.P_Employee_ID, P_Employee_Credits.P_Credits_ID, pc.Name, pu.VALUE, pc.Value, pc.DisplayUom" +
			" ORDER BY pc.Value "
			;
		else
			sqlQuery =
				"SELECT P_Employee_Credits.P_Credits_ID, trl.Name, dbo.FN_Credits_Sld_Display( isnull( Sum( PMovementVariation ), 0 ), pc.DisplayUom ) Total, pc.Value, pu.VALUE DisplayValue, isnull( pc.DisplayUom, pu.VALUE) DisplayUom, max(P_Employee_Credits.P_Employee_Credits_ID) as P_Employee_Credits_ID, P_Employee_Credits.P_Employee_ID " + 
				"FROM 	P_Credits pc, P_Credits_TRL trl, P_UOM pu, P_Employee_Credits  " +
				"LEFT OUTER JOIN P_Credits_Movement ON P_Employee_Credits.P_Employee_ID = P_Credits_Movement.P_Employee_ID " +
				"                                    AND P_Employee_Credits.P_Credits_ID = P_Credits_Movement.P_Credits_ID " +
				"                                    AND PMovementDate <= ? " +
		   	    "  AND ( P_Credits_Movement.P_Payment_ID <> ? OR P_Credits_Movement.P_Payment_ID IS NULL )" +
				"WHERE P_Employee_Credits.P_Employee_ID = ? " +
				"AND P_Employee_Credits.P_Credits_ID = pc.P_CREDITS_ID " + 
				"AND P_Employee_Credits.IsActive = 'Y' " +
				"AND pc.IsActive = 'Y' " +            
				"AND trl.P_Credits_ID = pc.P_Credits_ID " +
				"AND trl.AD_Language = '" + Env.getAD_Language(Env.getCtx()) + "'" +
				"AND pu.P_UOM_ID = pc.P_UOM_ID " +
				"AND pc.IsDisplayedCredits = 'Y' " +
				"AND P_Employee_Credits.EffectIn = ( Select Max( Effectin ) from P_Employee_Credits M Where M.P_Employee_id = P_Employee_Credits.P_Employee_ID and M.P_Credits_ID = P_Employee_Credits.P_Credits_ID ) " +
				"GROUP BY P_Employee_Credits.P_Employee_ID, P_Employee_Credits.P_Credits_ID, trl.Name, pu.VALUE, pc.Value, pc.DisplayUom" +
				" ORDER BY pc.Value "
				;

		s_log.log(Level.INFO, "sql : " + sqlQuery);

		String sqlLoadForTimesheet = 
			"SELECT pc.VALUE, sum(pcm.PMOVEMENTVARIATION), pc.P_Credits_ID " +
			"FROM 	P_Credits_Movement pcm	inner join P_credits pc 	on pcm.P_CREDITS_ID = pc.P_CREDITS_ID AND pc.IsDisplayedCredits = 'Y'" +
			"WHERE 	pcm.P_PAYMENT_ID = ? AND pcm.IsActive = 'Y' AND pc.IsActive = 'Y' " +
			"GROUP BY pc.VALUE, pc.P_Credits_ID ";
		
		sqlLoadForTimesheet = sqlLoadForTimesheet + " UNION ALL SELECT value, null, P_Credits_ID from P_CREDITS where isVirtualAccrualBank = 'Y' ";	
		ArrayList<BigDecimal> data = new ArrayList<BigDecimal>(16);
		ArrayList<String> codes = new ArrayList<String>(16);
		ArrayList<String> columns = new ArrayList<String>(16);
		//Get the result set count

		HashMap<Integer, String> mapUnits = new HashMap<Integer, String>();
		HashMap<Integer, String> mapUnits2 = new HashMap<Integer, String>();
		HashMap<String, BigDecimal> mapBank = new HashMap<String, BigDecimal>();
		
		try {
			PreparedStatement statm = DB.prepareStatement(sqlLoadForTimesheet, null);
			statm.setInt(1, paymentID);
			ResultSet tsRs = statm.executeQuery();
			while (tsRs.next()) {
				BigDecimal val = tsRs.getBigDecimal(2);
				
				P_Credits Credits = P_Credits.get( Env.getCtx(), tsRs.getInt("P_Credits_ID"), null);
				if ( Credits.isVirtualAccrualBank() )
				{
					val = Env.ZERO;
					if ( paymentID > 0 )
						val = LoadVirtualSoldePayment( Credits, employeeID, paymentID);
				}
				mapBank.put(tsRs.getString(1), val != null ? val.setScale(2, BigDecimal.ROUND_HALF_UP) : null);
			}
			tsRs.close();
			statm.close();
		} catch (Exception e) {
			s_log.log(Level.SEVERE, "VTimesheetCredits.LoadBank", e);
		}

		//Get the bank data
		try {
			P_Period period = P_Period.get( Env.getCtx(), periodID, null );
			
			PreparedStatement pstmt = DB.prepareStatement(sqlQuery, null);
			// 2010.10.12 startdate -1 
			pstmt.setTimestamp(1, TimeUtil.addDays(  period.getStartDate() , -1 ) );
			pstmt.setInt(2, paymentID);
			pstmt.setInt(3, employeeID);

			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				columns.add(rs.getString("Name"));
				codes.add(rs.getString("Value"));
				BigDecimal val = rs.getBigDecimal("Total");

				P_Credits Credits = P_Credits.get( Env.getCtx(), rs.getInt("P_Credits_ID"), null);
				if ( Credits.isVirtualAccrualBank() )
				{
					val = Env.ZERO;
					val = LoadVirtualSolde( Credits, employeeID, period, null, paymentID);
				}

				if ( rs.getString("DisplayUom").equals("A/J"))
					data.add(val != null ? val.setScale(3, BigDecimal.ROUND_HALF_UP) : null);
				else
					data.add(val != null ? val.setScale(2, BigDecimal.ROUND_HALF_UP) : null);


				mapUnits.put(new Integer(rs.getRow() - 1), rs.getString("DisplayUom"));
				mapUnits2.put(new Integer(rs.getRow() - 1), rs.getString("DisplayValue"));

			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			s_log.log(Level.SEVERE, "VEmployeeListPanel.loadPayPeriod", e);
		}

		Object[][] dataArray = new Object[3][data.size()];
		for (int i = 0; i < data.size(); i++) {
			dataArray[0][i] = codes.get(i);
			dataArray[1][i] = data.get(i);
			dataArray[2][i] = mapBank.get(codes.get(i));
		}
		model.setDataVector(dataArray, columns.toArray(new String[columns.size()]));
		model.setMapUnits(mapUnits);
		model.setMapUnits2(mapUnits2);
		return model;
	}

	public void applyBankModel(CreditsModel model) {
		if (model == null) return;
		tblBank.setModel(model);
		tblBank.setAutoResizeMode( CTable.AUTO_RESIZE_ALL_COLUMNS);
		TableColumnModel tcm = tblBank.getColumnModel();
		for (int j = 0; j < tcm.getColumnCount(); j++) {
			tcm.getColumn(j).setCellRenderer(model.new CreditCellRenderer());
		}
	}

	public void LoadBank(int timesheetID, int employeeID, int periodID){
		int paymentID = (m_header != null && m_header.getTimeSheet() != null) ? m_header.getTimeSheet().getP_Payment_ID() : 0;
		CreditsModel model = buildBankModel(timesheetID, employeeID, periodID, paymentID);
		applyBankModel(model);
	}
 
}

