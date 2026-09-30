/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is             Compiere  ERP & CRM Smart Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2003 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package solstice.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MRole;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Language;
import org.compiere.util.TimeUtil;

import solstice.model.X_P_Credits;
import solstice.utils.TimeUtilSolstice;

/**
 *  Credits Model
 *
 *  @author Marc Morissette
 *  @version $Id: P_Credits.java,v 1.2 2007/07/27 17:07:07 marmor01 Exp $
 */
public class P_Credits extends X_P_Credits
{
	/**
	 * 	Get Credits
	 *	@param ctx context
	 * 	@param P_Credits_ID id
	 *	@return Credits
	 */
	public static P_Credits get (Properties ctx, int P_Credits_ID, String trxName)
	{
		Integer key = new Integer (P_Credits_ID);
		P_Credits Credits = (P_Credits)s_cache.get(key);
		if (Credits != null)
			return Credits;
		Credits = new P_Credits (ctx, P_Credits_ID, trxName);
		s_cache.put (key, Credits);
		return Credits;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Credits>	s_cache = new CCache<Integer,P_Credits>("P_Credits", 20);
	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Credits.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Credits_ID id
	 */
	public P_Credits (Properties ctx, int P_Credits_ID, String trxName)
	{
		super (ctx, P_Credits_ID, trxName);
		if (P_Credits_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Credits

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Credits (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Credits (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Credits_ID"), trxName);
	}	//	P_Credits

	
	/**
	 * 	Load all record - for Performace.
	 *	@param ctx context
	 */
	public static void loadAll (Properties ctx)
	{
		//
		s_cache = new CCache<Integer,P_Credits>("P_Credits", 100);
		String sql = "SELECT P_Credits_ID FROM P_Credits WHERE IsActive='Y'";
		sql = MRole.getDefault().addAccessSQL (sql, "P_Credits", true, false);	// fully qualidfied - RO 

		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				P_Credits Credits = new P_Credits (ctx, rs.getInt("P_Credits_ID"), null);
				s_cache.put( Credits.getP_Credits_ID(), Credits);
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
	}	//	loadAll

	
	public Timestamp getCreditsDate( P_Year Year, P_Employee Employee , String trxName)
	{
	    Timestamp CreditsDate = null;

	    P_Credits Credits = P_Credits.get( Env.getCtx(), this.getP_Credits_ID(), trxName);

		int year   = Year.getYear();
	    int mon    ;
	    int day    ;

	    Calendar utcTime = Calendar.getInstance();
	    Date date;
	    
	    if ( Credits.getCreditsTypeDate().equals("1")  )
	    {
		    
		    mon    = new Integer( Credits.getCreditsMonth() ).intValue();
		    day    = Credits.getCreditsDay();

		    // set calendar to the time
		    utcTime.set( year, mon-1 , day, 0, 0, 0 );
			date = utcTime.getTime();
			
			CreditsDate = new Timestamp( date.getTime() ) ;
	        
	    }
	    if ( Credits.getCreditsTypeDate().equals("2")  )
	    {
	        CreditsDate = Employee.getDateSeniority();
//	        CreditsDate.setYear( year ); 

//		    mon    = CreditsDate.getMonth();
//		    day    = CreditsDate.getDate();

	        mon    = TimeUtil.getMonth(CreditsDate);
		    day    = TimeUtil.getDay_Of_Month( CreditsDate );

		    // set calendar to the time
		    utcTime.set( year, mon-1 , day, 0, 0, 0 );
			date = utcTime.getTime();
			
			CreditsDate = new Timestamp( date.getTime() ) ;

	        
	    }
	    if ( Credits.getCreditsTypeDate().equals("3")  )
	    {
	        CreditsDate = Employee.getDateHired();
//		    mon    = CreditsDate.getMonth();
//		    day    = CreditsDate.getDate();

		    mon    = TimeUtil.getMonth(CreditsDate);
		    day    = TimeUtil.getDay_Of_Month( CreditsDate );

		    // set calendar to the time
		    utcTime.set( year, mon-1 , day, 0, 0, 0 );
			date = utcTime.getTime();
			
			CreditsDate = new Timestamp( date.getTime() ) ;
	    }

	    return CreditsDate;
	}


	public static BigDecimal getEmployeeSolde( P_Credits Credits, P_Employee Employee, Timestamp d, String trxName ) 
	{
		String sql = null;
		if ( Credits.isVirtualAccrualBank()) {
			
			if ( Credits.getValue().equals("BV20B")) {
				int iYear = TimeUtilSolstice.getYear(d);
				P_Year Year = P_Year.getWithValue( Env.getCtx(), iYear, trxName);
				Timestamp CreditsDate = Credits.getCreditsDate( Year, Employee, trxName );
				
				Timestamp startDate = null;
				if ( d.compareTo(CreditsDate) >= 0 )
				{
					 startDate = CreditsDate;
				}
				else
				{
					 startDate = TimeUtilSolstice.addYear( CreditsDate , -1 );
				}

				sql = " SELECT  dbo.Credits_BV20B( " + Employee.getP_Employee_ID() + " , " + DB.TO_DATE( startDate ) + " , " + DB.TO_DATE( d ) + " ) var";
				
			}
			else
				sql = " SELECT 0 as var ";
			
		}
		else {
			sql = "Select sum( PMovementVariation ) var from P_Credits_Movement WHERE IsActive='Y' ";
			sql += " AND P_Credits_ID=" + Credits.getP_Credits_ID();
			sql += " AND P_Employee_ID=" + Employee.getP_Employee_ID();
			sql += " AND PMovementDate <= " + DB.TO_DATE( d );
			
		}
		//
		
//		System.out.println ("P_Credits - Sql - " + sql );
		
		PreparedStatement pstmt = null;

		BigDecimal sld = new BigDecimal(0);
		
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				sld = rs.getBigDecimal(1);

			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println ("P_Credits - get solde - " + sql + " - " + e);
		}

		if ( sld == null )
		    return  new BigDecimal( 0 );
		
		return sld;
	}

	public static BigDecimal getCreditsVariation( P_Credits Credits, P_Employee Employee, Timestamp startDate, Timestamp endDate, String trxName ) 
	{
		String sql = null;
		sql = "Select sum( PMovementVariation ) var from P_Credits_Movement WHERE IsActive='Y' ";
		sql += " AND P_Credits_ID=" + Credits.getP_Credits_ID();
		sql += " AND P_Employee_ID=" + Employee.getP_Employee_ID();
		sql += " AND PMovementDate >= " + DB.TO_DATE( startDate );
		sql += " AND PMovementDate <= " + DB.TO_DATE( endDate );
		//
//		System.out.println ("P_Credits - Sql - " + sql );
		
		PreparedStatement pstmt = null;

		BigDecimal sld = new BigDecimal(0);
		
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				sld = rs.getBigDecimal(1);

			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println ("P_Credits - get solde - " + sql + " - " + e);
		}

		if ( sld == null )
		    return  new BigDecimal( 0 );
		
		return sld;
	}

	public static BigDecimal getCreditsVariation8050( P_Employee Employee, Timestamp startDate, Timestamp endDate, String trxName ) 
	{
		String sql = null;
		sql = "SELECT SUM( nDay ) FROM (\r\n"
				+ "    select 1 as nDay FROM P_PAYMENT\r\n"
				+ "    INNER JOIN P_PAYMENT_GAIN ON P_PAYMENT_GAIN.P_PAYMENT_ID = P_PAYMENT.P_PAYMENT_ID\r\n"
				+ "    WHERE EXISTS ( "
				+ " select 1\r\n"
				+ " from P_Gain_GainInfo \r\n"
				+ " inner join P_GainInfo on P_Gain_GainInfo.P_GainInfo_ID = P_GainInfo.P_GainInfo_ID\r\n"
				+ " where P_GainInfo.Value = 'JourTravMa'\r\n"
				+ "  and P_Gain_GainInfo.P_Gain_ID = P_PAYMENT_GAIN.P_Gain_ID\r\n"
				+ " and To_Consider = 'Y'"
				+ " ) "
//				+ "SELECT P_GAIN_ID FROM P_GAIN WHERE VALUE IN ('GA01', 'GC27','GC03', 'GC01') )\r\n"
				+ "    and P_PAYMENT.p_employee_id = " + Employee.getP_Employee_ID() 
				+ "    and P_PAYMENT_GAIN.day between " + DB.TO_DATE(startDate) + " and " + DB.TO_DATE(endDate)
			 	+ " group by P_PAYMENT_GAIN.day  ) det";
		//
//		System.out.println ("P_Credits - Sql - " + sql );
		
		PreparedStatement pstmt = null;

		BigDecimal sld = new BigDecimal(0);
		
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				sld = rs.getBigDecimal(1);

			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println ("P_Credits - getCreditsVariation8050 - " + sql + " - " + e);
		}

		if ( sld == null )
		    return  new BigDecimal( 0 );
		
		return sld;
	}
	
	
	
	/*
	public static BigDecimal getEmployeeSolde( P_Credits Credits, int P_Employee_ID, Timestamp d, String trxName ) 
	{
		String sql = null;
		sql = "Select sum( PMovementVariation ) var from P_Credits_Movement WHERE IsActive='Y' ";
		sql += " AND P_Credits_ID=" + Credits.getP_Credits_ID();
		sql += " AND P_Employee_ID=" + P_Employee_ID;
		sql += " AND PMovementDate <= " + DB.TO_DATE( d );
		//
//		System.out.println ("P_Credits - Sql - " + sql );
		
		PreparedStatement pstmt = null;

		BigDecimal sld = new BigDecimal(0);
		
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			int index = 1;
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				sld = rs.getBigDecimal(1);

			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println ("P_Credits - get solde - " + sql + " - " + e);
		}

		if ( sld == null )
		    return  new BigDecimal( 0 );
		
		return sld;
	}
    */
/*
	public static BigDecimal getEmployeeOpeningBalanceSolde( int P_Credits_ID, int P_Employee_ID, Timestamp d, String trxName )  
	{
		String sql = null;
		sql = "Select sum( PMovementVariation ) var from P_Credits_Movement WHERE IsActive='Y' ";
		sql += " AND P_Credits_ID=" + P_Credits_ID;
		sql += " AND P_Employee_ID=" + P_Employee_ID;
		sql += " AND PMovementType = 'BO' ";
		sql += " AND PMovementDate <= " + DB.TO_DATE( d );
		//
//		System.out.println ("P_Credits - Sql - " + sql );
		
		PreparedStatement pstmt = null;

		BigDecimal sld = new BigDecimal(0);
		
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				sld = rs.getBigDecimal(1);

			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println ("P_Credits - get solde - " + sql + " - " + e);
		}

		if ( sld == null )
		    return  new BigDecimal( 0 );
		
		return sld;
	}
*/

	public static BigDecimal getEmployeeSolde( int P_Credits_ID, int P_Employee_ID, Timestamp d, String trxName )  
	{
		String sql = null;
		sql = "Select sum( PMovementVariation ) var from P_Credits_Movement WHERE IsActive='Y' ";
		sql += " AND P_Credits_ID=" + P_Credits_ID;
		sql += " AND P_Employee_ID=" + P_Employee_ID;
		sql += " AND PMovementDate <= " + DB.TO_DATE( d );
		//
//		System.out.println ("P_Credits - Sql - " + sql );
		
		PreparedStatement pstmt = null;

		BigDecimal sld = new BigDecimal(0);
		
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				sld = rs.getBigDecimal(1);

			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println ("P_Credits - get solde - " + sql + " - " + e);
		}

		if ( sld == null )
		    return  new BigDecimal( 0 );
		
		return sld;
	}
	
	

	public static BigDecimal getEmployeeSoldeTimeSheet( int P_Credits_ID, int P_Employee_ID, Timestamp d, String trxName )  
	{
		String sql = null;
		sql = "Select sum( PMovementVariation ) var from P_Credits_Movement WHERE IsActive='Y' ";
		sql += " AND P_Credits_ID=" + P_Credits_ID;
		sql += " AND P_Employee_ID=" + P_Employee_ID;
		sql += " AND PMovementDate <= " + DB.TO_DATE( d );
		sql += " AND NOT ( PMovementType = 'BF' and PMovementDate = " + DB.TO_DATE( d ) + ")";
		//
//		System.out.println ("P_Credits - Sql - " + sql );
		
		PreparedStatement pstmt = null;

		BigDecimal sld = new BigDecimal(0);
		
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				sld = rs.getBigDecimal(1);

			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println ("P_Credits - get solde - " + sql + " - " + e);
		}

		if ( sld == null )
		    return  new BigDecimal( 0 );
		
		return sld;
	}
	

	public static BigDecimal getEmployeeSoldeConv( int P_Credits_ID, int P_Employee_ID, Timestamp d, String trxName )  
	{
		
		String sql = "Select [dbo].[FN_Credits_Sld_Conv]( " + P_Employee_ID + ", " + P_Credits_ID + ", " + DB.TO_DATE( d ) + " ) " ;
		//
//		System.out.println ("P_Credits - Sql - " + sql );
		
		PreparedStatement pstmt = null;

		BigDecimal sld = new BigDecimal(0);
		
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				sld = rs.getBigDecimal(1);

			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println ("P_Credits - get solde - " + sql + " - " + e);
		}

		if ( sld == null )
		    return  new BigDecimal( 0 );
		
		return sld;
	}
	
	
	
	public static BigDecimal getEmployeeAccumulatedBalanceConv( Properties ctx, int P_Employee_Credits_ID, int P_Credits_ID, int P_Employee_ID, Timestamp d, String trxName )  
	{
	
		P_Employee_Credits Employee_Credits = P_Employee_Credits.get( ctx, P_Employee_Credits_ID, trxName);
		// EMS
//		if ( Employee_Credits.getP_Method_Credits_ID() == 1000003)
//			return P_Employee_Credits.getEmployeeOpeningBalance(P_Credits_ID, P_Employee_ID, d, trxName);
		
		String sql = "Select [dbo].[FN_Credits_AccumulatedBalance_Conv]( " + P_Employee_ID + ", " + P_Credits_ID + ", " + DB.TO_DATE( d ) + " ) " ;
		//
//		System.out.println ("P_Credits - Sql - " + sql );
		
		PreparedStatement pstmt = null;

		BigDecimal sld = new BigDecimal(0);
		
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				sld = rs.getBigDecimal(1);

			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println ("P_Credits - get solde - " + sql + " - " + e);
		}

		if ( sld == null )
		    return  new BigDecimal( 0 );
		
		return sld;
	}

	
	public static BigDecimal getEmployeeOpeningBalanceConv( Properties ctx, int P_Employee_Credits_ID, int P_Credits_ID, int P_Employee_ID, Timestamp d, String trxName )  
	{
	
		P_Employee_Credits Employee_Credits = P_Employee_Credits.get( ctx, P_Employee_Credits_ID, trxName);
		// EMS
//		if ( Employee_Credits.getP_Method_Credits_ID() == 1000003)
//			return P_Employee_Credits.getEmployeeOpeningBalance(P_Credits_ID, P_Employee_ID, d, trxName);
		
		String sql = "Select [dbo].[FN_Credits_OpeningBalance_Conv]( " + P_Employee_ID + ", " + P_Credits_ID + ", " + DB.TO_DATE( d ) + " ) " ;
		//
//		System.out.println ("P_Credits - Sql - " + sql );
		
		PreparedStatement pstmt = null;

		BigDecimal sld = new BigDecimal(0);
		
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				sld = rs.getBigDecimal(1);

			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println ("P_Credits - get solde - " + sql + " - " + e);
		}

		if ( sld == null )
		    return  new BigDecimal( 0 );
		
		return sld;
	}


	public static BigDecimal getEmployeeSoldeConv( Properties ctx, int P_Employee_Credits_ID, int P_Credits_ID, int P_Employee_ID, Timestamp d, String trxName )  
	{
	
		P_Employee_Credits Employee_Credits = P_Employee_Credits.get( ctx, P_Employee_Credits_ID, trxName);
		// EMS
		if ( Employee_Credits.getP_Method_Credits_ID() == 1000003)
			return P_Employee_Credits.getEmployeeSolde(P_Credits_ID, P_Employee_ID, d, trxName);
		
		String sql = "Select [dbo].[FN_Credits_Sld_Conv]( " + P_Employee_ID + ", " + P_Credits_ID + ", " + DB.TO_DATE( d ) + " ) " ;
		//
//		System.out.println ("P_Credits - Sql - " + sql );
		
		PreparedStatement pstmt = null;

		BigDecimal sld = new BigDecimal(0);
		
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				sld = rs.getBigDecimal(1);

			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println ("P_Credits - get solde - " + sql + " - " + e);
		}

		if ( sld == null )
		    return  new BigDecimal( 0 );
		
		return sld;
	}

	
	public void setEmployeeSolde( X_P_Payment Payment, X_P_Period Period, Timestamp d, BigDecimal var, String trxName ) 
	{
		X_P_Credits_Movement Credits_Movement = new X_P_Credits_Movement(getCtx (), -1, trxName);
		Credits_Movement.setP_Payment_ID( Payment.getP_Payment_ID() );
		Credits_Movement.setP_Employee_ID( Payment.getP_Employee_ID());
		Credits_Movement.setP_Credits_ID( this.getP_Credits_ID() );
//		PaymentCredits.setYear( Payment.)
		Credits_Movement.setPMovementDate( d );
//		Credits_Movement.setPMovementDescription( "" );
//		PaymentCredits.setPMovementOrigin(  );
		Credits_Movement.setPMovementType( "V-");
		Credits_Movement.setPMovementOrigin("FTP");
		Credits_Movement.setPMovementVariation(  var );
//		Credits_Movement.setP_Period_ID( Period.getID());
		Credits_Movement.save();
	}
	

	
	/**
	 * 	Before Delete
	 *	@return true if it can be deleted
	 */
	protected boolean beforeDelete ()
	{
		return true;
	}
	
	/**
	 * 	After Delete
	 *	@param success
	 *	@return
	 */
	protected boolean afterDelete (boolean success)
	{
		return success;
	}
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true or false
	 */

	protected boolean beforeSave (boolean newRecord)
	{

	    // harcoded
	    
        /*if ( getCreditsContents() != null && getCreditsContents().equals( P_Credits.CREDITSCONTENTS_Amount ))
        {
            this.setP_UOM_ID( 101 );
        }
        
        if ( getCreditsContents() != null && getCreditsContents().equals( P_Credits.CREDITSCONTENTS_Quantity ))
        {
            this.setP_UOM_ID( 105 );
        }*/

        return true;
	}	//	beforeSave

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
	    
	    if (!success )
		{
			return success;
		}

		return true;
	}  //	afterSave	


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Credits[ID=")
			.append(this.getP_Credits_ID())
			.append(",Value=").append(getValue())
			.append(",Name=").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
	/**
	 * Procédure de copie. Lorsqu'on copie une banque, on doit copier
	 * les enregistrements correspondants dans les tables P_Credits_Param,
	 * P_Credits_Bound, P_Credits_Alert et P_Credits_Account
	 */
	public void afterCopy(int originalId)
	{
	    try
	    {
	        // Copie des enregistrements
	        this.copyCreditsParam(originalId);
	        this.copyCreditsAlert(originalId);
	        this.copyCreditsAccount(originalId);
	    }
	    catch (SQLException e)
	    {
	        log.log(Level.SEVERE, "afterCopy", e);
	    }
	}

	/**
	 * Copie des enregistrements de la table P_Credits_Param
	 */
	private void copyCreditsParam(int originalId) throws SQLException
	{
	    // On récupère d'abord les enregistrements de la banque d'origine
	    String sql
	    = "select P_Credits_Param_ID, IsActive, P_Method_Credits_ID, Method_Annual, Method_Parting,"
	        + " CreditsInitial, CreditsAnnual, GainAnnual, P_Credits_Target, Description, P_Credits_Join,"
	        + " EffectIn, P_Credits_Family_MTH_Eval_ID, P_Credits_Family_MTH_Var_ID, CreditsVariation,"
	        + " GainParting, P_Credits_MTH_Variation_ID, AffectedByRWT, CumulatedHours"
	        + " from P_Credits_Param"
	        + " where P_Credits_ID = " + originalId;
	    
	    PreparedStatement stmt = DB.prepareStatement(sql, get_TrxName());
	    ResultSet rs = stmt.executeQuery();
	    
	    while(rs.next())
	    {
	        // On crée un nouvel enregistrement pour la banque courrante
	        P_Credits_Param creditsParam = new P_Credits_Param(Env.getCtx(), -1, null);
	        creditsParam.setP_Credits_ID(this.getP_Credits_ID());
	        creditsParam.setIsActive(rs.getString("IsActive").equals("Y"));
	        creditsParam.setP_Method_Credits_ID(rs.getInt("P_Method_Credits_ID"));
	        creditsParam.setMethod_Annual(rs.getString("Method_Annual"));
	        creditsParam.setMethod_Parting(rs.getString("Method_Parting"));
	        creditsParam.setCreditsInitial(rs.getBigDecimal("CreditsInitial"));
	        creditsParam.setCreditsAnnual(rs.getBigDecimal("CreditsAnnual"));
	        creditsParam.setGainAnnual(rs.getInt("GainAnnual"));
	        creditsParam.setP_Credits_Target(rs.getInt("P_Credits_Target"));
	        creditsParam.setDescription(rs.getString("Description"));
	        //not in use anymore
	        //creditsParam.setP_Credits_Join(rs.getInt("P_Credits_Join"));
	        creditsParam.setEffectIn(rs.getTimestamp("EffectIn"));
	        creditsParam.setP_Credits_Family_Mth_Eval_ID(rs.getInt("P_Credits_Family_MTH_Eval_ID"));
	        creditsParam.setP_Credits_Family_Mth_Var_ID(rs.getInt("P_Credits_Family_MTH_Var_ID"));
	        creditsParam.setCreditsVariation(rs.getBigDecimal("CreditsVariation"));
	        creditsParam.setGainParting(rs.getInt("GainParting"));
	        creditsParam.setP_Credits_Mth_Variation_ID(rs.getInt("P_Credits_MTH_Variation_ID"));
	        creditsParam.setAffectedByRWT(rs.getBoolean("AffectedByRWT"));
	        creditsParam.setCumulatedHours(rs.getBoolean("CumulatedHours"));
	        creditsParam.save();
	        
	        // On crée les enregistrements de P_Credits_Bound qui dépendent de ce paramètre
	        this.copyCreditsBound(rs.getInt("P_Credits_Param_ID"), creditsParam.getP_Credits_Param_ID());
	        this.copyCreditsParamCredits(rs.getInt("P_Credits_Param_ID"), creditsParam.getP_Credits_Param_ID());
	    }
	    
	    rs.close();
	    stmt.close();
	}
	
	/**
	 * Copie des enregistrements de la table P_Credits_Bound
	 */
	public void copyCreditsBound(int originalCreditsParamId, int creditsParamId) throws SQLException
	{
	    // On récupère d'abord les enregistrements de la banque d'origine
	    String sql
	    = "select IsActive, LimitMin, Variation, CreditsUnit"
	        + " from P_Credits_Bound"
	        + " where P_Credits_Param_ID = " + originalCreditsParamId;
	    
	    PreparedStatement stmt = DB.prepareStatement(sql, get_TrxName());
	    ResultSet rs = stmt.executeQuery();
	    
	    while(rs.next())
	    {
	        // On crée un nouvel enregistrement pour la banque courrante
	        P_Credits_Bound creditsBound = new P_Credits_Bound(Env.getCtx(), -1, null);
	        creditsBound.setP_Credits_Param_ID(creditsParamId);
	        creditsBound.setIsActive(rs.getString("IsActive").equals("Y"));
	        creditsBound.setLimitMin(rs.getBigDecimal("LimitMin"));
	        creditsBound.setVariation(rs.getBigDecimal("Variation"));
	        creditsBound.setCreditsUnit(rs.getString("CreditsUnit"));
	        creditsBound.save();
	    }
	    
	    rs.close();
	    stmt.close();
	}
	/**
	 * Copie des enregistrements de la table P_Credits_Bound
	 */
	public void copyCreditsParamCredits(int originalCreditsParamId, int creditsParamId) throws SQLException
	{
	    // On récupère d'abord les enregistrements de la banque d'origine
	    String sql
	    = "select IsActive, P_Credits_Join "
	        + " from P_Credits_Param_Credits "
	        + " where P_Credits_Param_ID = " + originalCreditsParamId;
	    
	    PreparedStatement stmt = DB.prepareStatement(sql, get_TrxName());
	    ResultSet rs = stmt.executeQuery();
	    
	    while(rs.next())
	    {
	        // On crée un nouvel enregistrement pour la banque courrante
	        X_P_Credits_Param_Credits creditsParamCredits = new X_P_Credits_Param_Credits(Env.getCtx(), -1, null);
	        creditsParamCredits.setP_Credits_Param_ID(creditsParamId);
	        creditsParamCredits.setP_Credits_Join(rs.getInt("P_Credits_Join"));
	        creditsParamCredits.save();
	    }
	    
	    rs.close();
	    stmt.close();
	}
	
	/**
	 * Copie des enregistrements de la table P_Credits_Alert
	 */
	private void copyCreditsAlert(int originalId) throws SQLException
	{
	    // On récupère d'abord les enregistrements de la banque d'origine
	    String sql
	    = "select IsActive, P_Method_Credits_ID, Alert_Limit, Alert_Condition,"
	        + " Alert_Message, Alert_Severity_Level, P_Occupation_Group_ID, P_Job_Title_ID, "
	        + " P_WorkPlace_ID, P_Job_Type_ID, P_Payment_Group_ID, Alert_Option, P_Credits_Join_ID"
	        + " from P_Credits_Alert"
	        + " where P_Credits_ID = " + originalId;
	    
	    PreparedStatement stmt = DB.prepareStatement(sql, get_TrxName());
	    ResultSet rs = stmt.executeQuery();
	    
	    while(rs.next())
	    {
	        // On crée un nouvel enregistrement pour la banque courrante
	        P_Credits_Alert creditsAlert = new P_Credits_Alert(Env.getCtx(), -1, null);
	        creditsAlert.setP_Credits_ID(this.getP_Credits_ID());
	        creditsAlert.setIsActive(rs.getString("IsActive").equals("Y"));
	        creditsAlert.setP_Method_Credits_ID(rs.getInt("P_Method_Credits_ID"));
	        creditsAlert.setAlert_Limit(rs.getBigDecimal("Alert_Limit"));
	        creditsAlert.setAlert_Condition(rs.getString("Alert_Condition"));
	        creditsAlert.setAlert_Message(rs.getString("Alert_Message"));
	        creditsAlert.setAlert_Severity_Level(rs.getString("Alert_Severity_Level"));
	        creditsAlert.setP_Occupation_Group_ID(rs.getInt("P_Occupation_Group_ID"));
	        creditsAlert.setP_Job_Title_ID(rs.getInt("P_Job_Title_ID"));
	        creditsAlert.setP_Workplace_ID(rs.getInt("P_WorkPlace_ID"));
	        creditsAlert.setP_Job_Type_ID(rs.getInt("P_Job_Type_ID"));
	        creditsAlert.setP_Payment_Group_ID(rs.getInt("P_Payment_Group_ID"));
	        creditsAlert.setAlert_Option(rs.getString("Alert_Option"));
	        creditsAlert.setP_Credits_Join_ID(rs.getInt("P_Credits_Join_ID"));
	        creditsAlert.save();
	    }
	    
	    rs.close();
	    stmt.close();
	}
	
	/**
	 * Copie des enregistrements de la table P_Credits_Account
	 */
	private void copyCreditsAccount(int originalId) throws SQLException
	{
	    // On récupère d'abord les enregistrements de la banque d'origine
	    String sql
	    = "select P_Occupation_Group_ID, IsActive, P_Credits_Acct, P_Job_Type_ID, P_Job_Title_ID"
	        + " from P_Credits_Account"
	        + " where P_Credits_ID = " + originalId;
	    
	    PreparedStatement stmt = DB.prepareStatement(sql, get_TrxName());
	    ResultSet rs = stmt.executeQuery();
	    
	    while(rs.next())
	    {
	        // On crée un nouvel enregistrement pour la banque courrante
	        P_Credits_Account creditsAccount = new P_Credits_Account(Env.getCtx(), -1, null);
	        creditsAccount.setP_Credits_ID(this.getP_Credits_ID());
	        creditsAccount.setP_Occupation_Group_ID(rs.getInt("P_Occupation_Group_ID"));
	        creditsAccount.setIsActive(rs.getString("IsActive").equals("Y"));
	        creditsAccount.setP_Credits_Acct(rs.getInt("P_Credits_Acct"));
	        creditsAccount.setP_Job_Type_ID(rs.getInt("P_Job_Type_ID"));
	        creditsAccount.setP_Job_Title_ID(rs.getInt("P_Job_Title_ID"));
	        creditsAccount.save();
	    }
	    
	    rs.close();
	    stmt.close();
	}

	/**
	 * 	Get Translated Name
	 *	@return name
	 */
	public String getTrlName()
	{
		if (Env.isBaseLanguage(Env.getCtx(), "AD_Ref_List"))
			return getName();
		return get_Translation("Name", Env.getAD_Language(Env.getCtx()));
	}	//	getTrlName
	
	//
	// Return Name with Translation
	// 
	public String getTrlName( String language )
	{
		Language baseLanguage = Language.getBaseLanguage();
		
		if ( language.equals( baseLanguage.getAD_Language()  )||  language.equals( "en_CA") )
			return getName();
		return get_Translation("Name", language );
	}	//	getTrlName
	

	
	/**
	 * 	Get Translated Name
	 *	@return name
	 */
	public String getTrlDescription()
	{
		if (Env.isBaseLanguage(Env.getCtx(), "AD_Ref_List"))
			return getDescription();
		return get_Translation("Description", Env.getAD_Language(Env.getCtx()));
	}	//	getTrlDescription

	
	public static P_Credits getWithValue( Properties ctx, String value, String trxName )
	{
		int id = 0;
		String sql = "Select P_Credits_ID from P_Credits Where Value = '" + value + "'";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				id = rs.getInt("P_Credits_ID");
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("P_Gain.get - " + e);
		}
		
		return P_Credits.get(ctx, id, trxName);
		
	}

	
}	//	P_Credits
