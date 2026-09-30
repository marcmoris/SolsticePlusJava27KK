package solstice.model;

import java.awt.Frame;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.GregorianCalendar;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.TimeUtil;

import java.util.logging.*;

import javax.swing.JOptionPane;

import org.compiere.util.*;
import org.compiere.model.MBPartner;
import org.compiere.model.MLocation;
import org.compiere.model.MRegion;
import org.compiere.model.MRole;
import org.compiere.model.MUser;

import solstice.model.P_Assignment;
import solstice.model.P_Workplace;
import solstice.process.PgiUtil;



/**
*  Employee Model
*
*  @author Marc Morissette
*  @version $Id: P_Employee.java,v 1.32 2008/10/29 14:59:45 marmor01 Exp $
*/
public class P_Employee extends X_P_Employee
{  
	/**
	 * 	Get Employee
	 *	@param ctx context
	 * 	@param P_Employee_ID id
	 *	@return Employee
	 */
//	private int P_Dist_Booklet;
	
	public static P_Employee get (Properties ctx, int P_Employee_ID, String trxName)
	{
		Integer key = new Integer (P_Employee_ID);
		P_Employee Employee = (P_Employee)s_cache.get(key);
		if (Employee != null)
			return Employee;
		Employee = new P_Employee (ctx, P_Employee_ID, trxName);
		s_cache.put (key, Employee);
		
		return Employee;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Employee>	s_cache = new CCache<Integer,P_Employee>("P_Employee", 200);
	private static CCache<Integer,BigDecimal>	s_cache2 = new CCache<Integer,BigDecimal>("PrincipalAssignment_Param", 20);

	private static CCache<String,P_Employee_RWT_Detail>	s_cacheRWT = new CCache<String,P_Employee_RWT_Detail>("P_Employee_RWT_Detail", 20);
	private static CCache<String,P_Employee_RWT_Detail>	s_cacheRWT2 = new CCache<String,P_Employee_RWT_Detail>("P_Employee_RWT_Detail", 20);
	private static CCache<String,P_Employee_RWT_Detail>	s_cacheRWT3 = new CCache<String,P_Employee_RWT_Detail>("P_Employee_RWT_Detail", 20);

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Employee.class);

	private String CurrentUserCode;
	
	private int       CurrentLongTermLeave_ID;
	private Timestamp CurrentLongTermLeaveDate;
	
	private boolean existsRWT = false;

	private static Timestamp MaxAssignmentParamEffectIN = null;
	/**
	 * 	Default Constructor
	 * 	@param ctx context
	 * 	@param rs ResultSet to load from
	 */
	public P_Employee (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);

	}	//	P_Employee


	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Employee_ID id
	 */
	public P_Employee (Properties ctx, int P_Employee_ID, String trxName)
	{
		super (ctx, P_Employee_ID, trxName);
		
		if (P_Employee_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(11);
			setAD_User_ID(0);
			CurrentUserCode = null;
		}
		else
		{
			CurrentUserCode = getCurrentUserCode();
		}

		MaxAssignmentParamEffectIN = _getMaxAssignmentParamEffectIN( P_Employee_ID ); 
		
	}	//	P_Employee


	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Employee (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Employee_ID"), trxName);
	}	//	P_Employee

	
	/**
	 * 	Load all record - for Performace.
	 *	@param ctx context
	 */
	public static void loadAll (Properties ctx)
	{
		//
		s_cache = new CCache<Integer,P_Employee>("P_Employee", 250);
		String sql = "SELECT P_Employee_ID FROM P_Employee WHERE IsActive='Y'";
		sql = MRole.getDefault(Env.getCtx(), false).addAccessSQL (sql, "P_Employee", true, false);	// fully qualidfied - RO 

		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				P_Employee Employee = new P_Employee (ctx, rs.getInt("P_Employee_ID"), null);
				s_cache.put( Employee.getP_Employee_ID(), Employee);
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
	}	//	loadAll


	
	
	public int getAge( Timestamp d ) 
	{
		Timestamp tt2 = (Timestamp)get_Value("BirthDate");

		String t1 = d.toString();
		String t2 = tt2.toString();

		t1 = t1.substring(0,4) + t1.substring(5,7) + t1.substring(8,10);
		t2 = t2.substring(0,4) + t2.substring(5,7) + t2.substring(8,10);
			
		BigInteger endDate   = new BigInteger( t1 );
		BigInteger birthDate = new BigInteger( t2 );

		return ( endDate.subtract( birthDate ).divide( new BigInteger( "10000" )).intValue() );
	}

	public BigDecimal getAgeWithMonth( Timestamp d ) 
	{
		Timestamp birthDate = (Timestamp)get_Value("BirthDate");
		int nbrYear = TimeUtil.getDaysBetween(birthDate, d) / 365 ;
		int nbrMonth = nbrYear * 12;

		BigDecimal rest = new BigDecimal( TimeUtil.getDaysBetween(birthDate, d)).divide(new BigDecimal(365), 5, BigDecimal.ROUND_HALF_UP) .multiply(new BigDecimal( 12 )) .subtract( new BigDecimal( nbrMonth )) ;
	
		return new BigDecimal ( nbrYear  ).add(  rest.divide( new BigDecimal(100))  ).setScale(2, BigDecimal.ROUND_HALF_UP);
/*		
		Timestamp tt2 = (Timestamp)get_Value("BirthDate");

		String t1 = d.toString();
		String t2 = tt2.toString();

		t1 = t1.substring(0,4) + t1.substring(5,7) + t1.substring(8,10);
		t2 = t2.substring(0,4) + t2.substring(5,7) + t2.substring(8,10);
			
		BigDecimal endDate   = new BigDecimal( t1 );
		BigDecimal birthDate = new BigDecimal( t2 );

	   return ( endDate.subtract( birthDate ).divide( new BigDecimal( "10000" ) ,2 , BigDecimal.ROUND_HALF_UP) );
*/
	}

	
	public BigDecimal getAnnualSalary( Timestamp effectIn ) 
	{
		String sql = null;
		sql = "Select P_Assignment.P_Assignment_ID from P_Assignment WHERE P_Assignment.IsActive='Y' ";
		sql += " AND P_Assignment.P_Employee_ID=" + this.getP_Employee_ID();
		sql += " AND P_Assignment.AssignmentType = 'P' ";
		sql += " AND " + DB.TO_DATE( effectIn ) + " between isnull( P_Assignment.startdate, " + DB.TO_DATE( effectIn ) + ") and isnull( P_Assignment.enddate, "+ DB.TO_DATE( effectIn ) + ")";
		//
		
		P_Assignment_Param assignment_Param = null;
		
		PreparedStatement pstmt = null;
		
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			int index = 1;
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				assignment_Param = P_Assignment_Param.get(Env.getCtx(), rs.getInt("P_Assignment_ID"), effectIn, null);
			}
			else
			{
				int P_Assignment_Param_ID = GetPrincipalAssignment_Param ( ); 
				if ( P_Assignment_Param_ID != 0 )
					assignment_Param = P_Assignment_Param.get( getCtx (), P_Assignment_Param_ID,  null ); 
			}
			
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Employee - " + sql, e);
		}

		if(assignment_Param == null || assignment_Param.getP_Assignment_Param_ID() == 0)
		{
			s_log.log(Level.SEVERE,"setAnnual_Salary - Invalid P_Assignment_Param - Employee " + this.toString());
			return null;
		}

		return assignment_Param.getAnnual_Salary();
	}


	public static P_Employee getWithUserID( Properties ctx, int AD_User_ID, String trxName  ) 
	{
		String sql = null;
		sql = "Select P_Employee.P_Employee_ID from P_Employee WHERE AD_User_ID = " + AD_User_ID;
		//
		
		P_Employee Employee = null;
		
		PreparedStatement pstmt = null;
		
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				Employee = P_Employee.get( ctx, rs.getInt( "P_Employee_ID"), trxName);
			}
			
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Employee - " + sql, e);
		}

		return Employee;
	}



	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Employee[ID=")
			.append(this.getP_Employee_ID())
			.append(",Value=").append(getValue())
			.append(",Name=").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString


	


	private static Timestamp _getMaxAssignmentParamEffectIN( int Employee_ID )
	{
		
		Timestamp ParamEffectIN = null;
		
		String sql = null;
		sql = "Select max( P_Assignment_Param.effectIn ) "; 
		sql += " From P_Assignment, P_Assignment_Param ";
		sql += " WHERE P_Assignment.IsActive='Y' ";
		sql += " AND P_Assignment_Param.IsActive='Y' ";
		sql += " AND P_Assignment_Param.P_Assignment_ID = P_Assignment.P_Assignment_ID";
		sql += " AND P_Assignment.P_Employee_ID=" + Employee_ID;


		PreparedStatement pstmt = null;

		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				ParamEffectIN = rs.getTimestamp(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE," getEmployeeAssignment - " + sql, e);
		}
		
		return ParamEffectIN;
	}

	public Timestamp getMaxAssignmentParamEffectIN()
	{
		return MaxAssignmentParamEffectIN;
	}

	public P_Assignment_RWT GetAssignment_RWT( Timestamp EffectIn )
	{
		
		String sql = null;
		sql = "Select P_Assignment_RWT_ID FROM P_Assignment_RWT WHERE P_Assignment_RWT.IsActive='Y' ";
		sql += " AND P_Assignment_RWT.P_Assignment_ID=" + GetAssignmentPrincipal( );
		sql += " AND " + DB.TO_DATE( EffectIn) + " BETWEEN startdate AND isnull( enddate," + DB.TO_DATE( EffectIn) + ") "
		;
		//
		
		int P_Assignment_RWT_ID = 0;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				P_Assignment_RWT_ID = rs.getInt("P_Assignment_RWT_ID");	
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"IsRWT - " + sql, e);
		}

		if ( P_Assignment_RWT_ID == 0)
			return null;
		
		return P_Assignment_RWT.get( Env.getCtx(), P_Assignment_RWT_ID, this.get_TrxName());
	}

	public int isDayRWT( Timestamp EffectIn )
	{
	    P_Assignment_RWT Assignment_RWT = GetAssignment_RWT( EffectIn );
	    if	( Assignment_RWT != null )
	    {
	    	//System.out.println("* DEBUG * Read P_Absence_Detail sql " + Integer.parseInt( Assignment_RWT.getWeekDay()) +1 );
			return Integer.parseInt( Assignment_RWT.getWeekDay()) +1;
	    }

	    return 0;
	}
	
	public int dayRWT( Timestamp EffectIn )
	{
		P_Assignment Assignment = P_Assignment.get( Env.getCtx(), GetAssignmentPrincipal( ), this.get_TrxName() );
	    P_Assignment_RWT Assignment_RWT = Assignment.GetAssignment_RWT( EffectIn );

	    if	( Assignment_RWT != null )
			return Integer.parseInt( Assignment_RWT.getWeekDay()) +1;

	    return 0;
	}
	
	public int GetAssignmentPrincipal(  )
	{
		Integer key = new Integer (this.getP_Employee_ID());

		String sql = null;
		sql = "Select P_Assignment.P_Assignment_ID "; 
		sql += " From P_Assignment";
		sql += " WHERE P_Assignment.IsActive='Y' ";
		sql += " AND P_Assignment.P_Employee_ID=" + this.getP_Employee_ID();
		sql += " AND P_Assignment.AssignmentType = 'P' ";
		sql += " ORDER BY P_Assignment.StartDate Desc";

		int iAssignment_ID = 0;

		PreparedStatement pstmt = null;

		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				iAssignment_ID = rs.getInt(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Employee - GetPrincipalAssignment - " + sql, e);
		}
			

		return iAssignment_ID;
		
	}


	//Maintenant
	//1 - plus besoin de date pour aller chercher l'affectation principale
	//2 - il ne devrait pas y avoir plus d'une affectation principale pour l'employe
	public int GetPrincipalAssignment_Param (  )
	{


		Integer key = new Integer (this.getP_Employee_ID());

		BigDecimal bAssignment_Param_ID = (BigDecimal)s_cache2.get(key);
		if (bAssignment_Param_ID != null)
			return bAssignment_Param_ID.intValue();

		String sql = null;
		sql = "Select P_Assignment_Param.P_Assignment_Param_ID "; 
		sql += " From P_Assignment, P_Assignment_Param ";
		sql += " WHERE P_Assignment.IsActive='Y' ";
		sql += " AND P_Assignment_Param.IsActive='Y' ";
		sql += " AND P_Assignment_Param.P_Assignment_ID = P_Assignment.P_Assignment_ID";
		sql += " AND P_Assignment.P_Employee_ID=" + this.getP_Employee_ID();
		sql += " AND P_Assignment.AssignmentType = 'P' ";
		sql += " ORDER BY P_Assignment_Param.EffectIn Desc";

		int iAssignment_Param_ID = 0;

		PreparedStatement pstmt = null;

		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				iAssignment_Param_ID = rs.getInt(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Employee - GetPrincipalAssignment - " + sql, e);
		}
			
		s_cache2.put (key, new BigDecimal(iAssignment_Param_ID));

		return iAssignment_Param_ID;
		
	}

	
	//Maintenant
	//1 - plus besoin de date pour aller chercher l'affectation principale
	//2 - il ne devrait pas y avoir plus d'une affectation principale pour l'employe
	public int GetPrincipalAssignment_Param ( Timestamp effectIn )
	{


/*		Integer key = new Integer (this.getP_Employee_ID() + effectIn.toString());

		BigDecimal bAssignment_Param_ID = (BigDecimal)s_cache2.get(key);
		if (bAssignment_Param_ID != null)
			return bAssignment_Param_ID.intValue();
*/
		String sql = null;
		sql = "Select P_Assignment_Param.P_Assignment_Param_ID "; 
		sql += " From P_Assignment, P_Assignment_Param ";
		sql += " WHERE P_Assignment.IsActive='Y' ";
		sql += " AND P_Assignment_Param.IsActive='Y' ";
		sql += " AND P_Assignment_Param.P_Assignment_ID = P_Assignment.P_Assignment_ID";
		sql += " AND P_Assignment.P_Employee_ID=" + this.getP_Employee_ID();
		sql += " AND P_Assignment.AssignmentType = 'P' ";
		sql += " AND P_Assignment_Param.EffectIn <= " + DB.TO_DATE( effectIn );
		sql += " ORDER BY P_Assignment_Param.EffectIn Desc";

		int iAssignment_Param_ID = 0;

		PreparedStatement pstmt = null;

		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				iAssignment_Param_ID = rs.getInt(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Employee - GetPrincipalAssignment - " + sql, e);
		}
			
	//	s_cache2.put (key, new BigDecimal(iAssignment_Param_ID));

		return iAssignment_Param_ID;
		
	}

	// TODO faire le get avec un having...
	public int GetBaseAssignment_Param ( Timestamp effectIn )
	{
		String sql = null;
		sql = "Select P_Assignment_Param.P_Assignment_Param_ID "; 
		sql += " From P_Assignment, P_Assignment_Param ";
		sql += " WHERE P_Assignment.IsActive='Y' ";
		sql += " AND P_Assignment_Param.IsActive='Y' ";
		sql += " AND P_Assignment_Param.P_Assignment_ID = P_Assignment.P_Assignment_ID";
		sql += " AND P_Assignment.P_Employee_ID=" + this.getP_Employee_ID();
		sql += " AND P_Assignment.AssignmentType = 'B' ";
		sql += " AND P_Assignment_Param.EffectIn <= " + DB.TO_DATE( effectIn );
		sql += " AND " + DB.TO_DATE( effectIn ) + " between isnull( P_Assignment.startdate, " + DB.TO_DATE( effectIn ) + ") and isnull( P_Assignment.enddate, "+ DB.TO_DATE( effectIn ) + ")";
		sql += " ORDER BY P_Assignment_Param.EffectIn Desc";


		PreparedStatement pstmt = null;

		int iAssignment_Param_ID = 0;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				iAssignment_Param_ID = rs.getInt(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Employee - GetBaseAssignment - " + sql, e);
		}
			
		return iAssignment_Param_ID;
		
	}

	public int getNbrAssignment (int P_Employee_ID, int P_Assignment_ID, Timestamp StartDate, Timestamp EndDate, String trxName)
	{

		int NbrAssignment = 0 ;
        // Retourne le nombre d'affectation active dans une intervalle donnée
		String sql = null;
		sql = "Select count( distinct P_Assignment_Param.P_Assignment_Param_ID) as NbrAssignment "; 
		sql += " From P_Assignment, P_Assignment_Param ";
		sql += " WHERE P_Assignment.IsActive='Y' ";
		sql += " AND P_Assignment_Param.IsActive='Y' ";
		sql += " AND P_Assignment_Param.P_Assignment_ID = P_Assignment.P_Assignment_ID";
		sql += " AND P_Assignment.P_Employee_ID=" + P_Employee_ID;
		sql += " AND P_Assignment.P_Assignment_ID =  " + P_Assignment_ID;
		sql += " AND ( isnull(P_Assignment_Param.EffectTO," + DB.TO_DATE( EndDate ) + ") between " + DB.TO_DATE( StartDate ) + " AND " + DB.TO_DATE( EndDate ) + " OR  P_Assignment_Param.EffectTO >= " + DB.TO_DATE( EndDate ) + ")";
		sql += " AND P_Assignment_Param.EffectIn <= " + DB.TO_DATE( EndDate );
		sql += " AND isnull( P_Assignment.startdate, " + DB.TO_DATE( StartDate ) + ") <= " + DB.TO_DATE( EndDate ) ;
		sql += " AND isnull( P_Assignment.enddate, "+ DB.TO_DATE( EndDate ) + ") >= " + DB.TO_DATE( StartDate );


		PreparedStatement pstmt = null;

		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				NbrAssignment = rs.getInt("NbrAssignment");
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE," P_Employee - getNbrAssignment - " + sql, e);
		}

		return NbrAssignment  ;		
	}

	public int getLastEmployeeAssignment( int P_Payment_ID  )
	{
		String sql = null;
		sql = "Select TOP 1 P_Payment_Gain.P_Assignment_ID, sum( quantitycalc) quantitycalc "; 
		sql += " From P_Payment_Gain";
		sql += " WHERE P_Payment_ID = " + P_Payment_ID ;
		sql += " GROUP BY P_Payment_Gain.P_Assignment_ID  ";
		sql += " ORDER BY sum( quantitycalc) desc ";

		int iAssignment_ID = 0;

		PreparedStatement pstmt = null;

		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				iAssignment_ID = rs.getInt(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"Ledger_Entry - GetAssignment - " + sql, e);
		}
			

		return iAssignment_ID;
		
	}

	public int getLastEmployeeAssignmentTime( int P_Timesheet_ID  )
	{
		String sql = null;
		sql = "Select TOP 1 P_TIME_SHEET_DETAIL.P_Assignment_ID, sum( P_TIME_SHEET_DETAIL.DAYQTY) quantitycalc "; 
		sql += " From P_TIME_SHEET_DETAIL";
		sql += " WHERE P_TIME_SHEET_ID = " + P_Timesheet_ID ;
		sql += " GROUP BY P_TIME_SHEET_DETAIL.P_Assignment_ID  ";
		sql += " ORDER BY sum( P_TIME_SHEET_DETAIL.DAYQTY) desc ";

		int iAssignment_ID = 0;

		PreparedStatement pstmt = null;

		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				iAssignment_ID = rs.getInt(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"Ledger_Entry - GetAssignment - " + sql, e);
		}
			

		return iAssignment_ID;
		
	}

	
	public P_Assignment_Param getLastEmployeeAssignment (int P_Employee_ID, Timestamp StartDate, String trxName)
	{
        
		
		P_Assignment_Param Assignment_Param = null;
		
		

		//TODO Ajouter un paramètre global qui détermine si le client par défaut génère l'affectation principal ou la plus récente.
		if ( Env.getAD_Client_ID( Env.getCtx() ) != 11 )
		{
			Assignment_Param = new P_Assignment_Param( Env.getCtx(), GetPrincipalAssignment_Param( StartDate ), trxName);
			P_Assignment Assignment = P_Assignment.get( Env.getCtx(),Assignment_Param.getP_Assignment_ID(), trxName); 
			if ( Assignment.getEndDate() == null || Assignment.getEndDate().compareTo( StartDate ) > 0 )
				return Assignment_Param  ;		
		}

        // Retourne les paramètres d'affectation en ayant du plus récent au plus vieux, en fonction de la date reçue en paramètre
		String sql = null;
		sql = "Select P_Assignment_Param.P_Assignment_Param_ID "; 
		sql += " From P_Assignment, P_Assignment_Param ";
		sql += " WHERE P_Assignment.IsActive='Y' ";
		sql += " AND P_Assignment_Param.IsActive='Y' ";
		sql += " AND P_Assignment_Param.P_Assignment_ID = P_Assignment.P_Assignment_ID";
		sql += " AND P_Assignment.P_Employee_ID=" + P_Employee_ID;
		sql += " AND " + DB.TO_DATE( StartDate ) + " between isnull( P_Assignment.startdate, " + DB.TO_DATE( StartDate ) + ") and isnull( P_Assignment.enddate, "+ DB.TO_DATE( StartDate ) + ")";
		sql += " ORDER BY P_Assignment.StartDate Desc, P_Assignment.AssignmentType, P_Assignment_Param.EffectIn DESC  ";


		PreparedStatement pstmt = null;

		int iAssignment_Param_ID = 0;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				iAssignment_Param_ID = rs.getInt(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE," P_Employee - getLastEmployeeAssignment - " + sql, e);
		}

		Assignment_Param = new P_Assignment_Param( Env.getCtx(), iAssignment_Param_ID, trxName);

		return Assignment_Param  ;		
	}


	public int GetOthersAssignment_Param ( Timestamp effectIn )
	{
		String sql = null;
		sql = "Select P_Assignment_Param.P_Assignment_Param_ID "; 
		sql += " From P_Assignment, P_Assignment_Param ";
		sql += " WHERE P_Assignment.IsActive='Y' ";
		sql += " AND P_Assignment_Param.IsActive='Y' ";
		sql += " AND P_Assignment_Param.P_Assignment_ID = P_Assignment.P_Assignment_ID";
		sql += " AND P_Assignment.P_Employee_ID=" + this.getP_Employee_ID();
		sql += " AND P_Assignment.AssignmentType = '" + P_Assignment.ASSIGNMENTTYPE_Other + "'";
		sql += " AND P_Assignment_Param.EffectIn <= " + DB.TO_DATE( effectIn );
		sql += " AND " + DB.TO_DATE( effectIn ) + " between isnull( P_Assignment.startdate, " + DB.TO_DATE( effectIn ) + ") and isnull( P_Assignment.enddate, "+ DB.TO_DATE( effectIn ) + ")";
		sql += " ORDER BY P_Assignment_Param.EffectIn Desc";


		PreparedStatement pstmt = null;

		int iAssignment_Param_ID = 0;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				iAssignment_Param_ID = rs.getInt(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Employee - GetOthersAssignment - " + sql, e);
		}
			
		return iAssignment_Param_ID;
		
	}

	
	public P_Assignment_Param getAssignment_Param( Timestamp effectIn, String trxName )
	{
		int iAssigment_Param_ID = 0;
		
		iAssigment_Param_ID = GetPrincipalAssignment_Param(  );
		if ( iAssigment_Param_ID == 0 )
			iAssigment_Param_ID = GetBaseAssignment_Param( effectIn );

		if ( iAssigment_Param_ID == 0 )
			iAssigment_Param_ID = GetOthersAssignment_Param( effectIn );
			
		if ( iAssigment_Param_ID == 0 )
			return null;
		
		return P_Assignment_Param.get( Env.getCtx(), iAssigment_Param_ID, trxName);
	}

	
	//
	// Get the current Booklet ID
	//
	public int getP_Distribution_Booklet_ID() 
	{
		String sql = null;
		sql = "Select P_Distribution_Booklet_ID "; 
		sql += " From P_Employee_Distribution_Booklet";
		sql += " WHERE P_Employee_ID=" + this.getP_Employee_ID();
		sql += " ORDER BY Start_Date Desc";

		int ID = 0;

		PreparedStatement pstmt = null;

		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				ID = rs.getInt(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Employee - getP_Distribution_Booklet_ID - " + sql, e);
		}
			

		return ID;
		
	}

	
	//
	// Get the current Booklet ID
	//
	public int getP_Distribution_Booklet_ID( Timestamp effectIn ) 
	{
		String sql = null;
		sql = "Select P_Distribution_Booklet_ID "; 
		sql += " From P_Employee_Distribution_Booklet";
		sql += " WHERE P_Employee_ID=" + this.getP_Employee_ID();
		sql += "  AND Start_Date <= " + DB.TO_DATE(effectIn);
		sql += " ORDER BY Start_Date Desc";

		int ID = 0;

		PreparedStatement pstmt = null;

		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				ID = rs.getInt(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Employee - getP_Distribution_Booklet_ID - " + sql, e);
		}
			

		return ID;
		
	}

		
	/*
	 * 
	 * Replacer par le méthode de calcul dans employee_credits. 
	 * 
	public int getP_Profile_ID() 
	{
	    int profile_ID = 0;
	    
		String sql = null;
		sql = "Select P_Profile_ID "; 
		sql += " From P_Employee_Profile WHERE P_Employee_ID = " + this.getP_Employee_ID();
		sql += " AND IsDefault='Y'" ;


		PreparedStatement pstmt = null;

		try
		{
			pstmt = DB.prepareStatement (sql);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
			    profile_ID = rs.getInt(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Employee - GetProfile_ID - " + sql, e);
		}
		
	   return profile_ID;
	}
*/

	/**
	 * 	e Delete
	 *	@return true if it can be deleted
	 */
	protected boolean eDelete ()
	{
        MBPartner BPartner = MBPartner.get( getCtx(), this.getValue() );
        if ( BPartner != null ) 
        	BPartner.delete( false );

		boolean retValue = true;
		if(this.getAD_Client_ID() != 0 && this.getAD_User_ID() != 0)
		{
			MUser user = new MUser(Env.getCtx(), this.getAD_User_ID(), null);
			if(user != null && user.getAD_User_ID() > 100)
				retValue = user.delete(false);
			
		}

	    // 
	    // Ajout audit trail 
	    //
		if ( PgiUtil.getSolsticeParameter(Env.getCtx(), "AuditTrailInsurance").equals("True")  )
			P_Audit_Trail_Insurance.Employee_Audit( this, false );
		

		
		return retValue;
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
	

	public boolean GetNumberInsurance()
	{	
		String noEmpl = this.getValue();
		if (noEmpl == null)
		{
			return true;
		}
		int lLength = noEmpl.length();
		int lmin;
		int lmax;

		if (lLength < 5)
		{
			for (int i = lLength; i < 5; i++)
			{
				noEmpl = "0" + noEmpl;
			}
			lmin = 0;
			lmax = 5;
		}
		else
		{
			lmin = lLength - 5;
			lmax = lLength;
		}
		
		String noInsur =  noEmpl.substring(lmin,lmax);
				
		String sql = null;
		sql = "select * from P_employee where isActive='Y' and cast(substring(value,len(value) - 4,len(value) + 1) as int ) = " + noInsur + "";
				
		PreparedStatement pstmt = null;

		boolean iResult = true;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
			{
				log.saveError("ValidationError", Msg.translate(getCtx(), "Insurance - Invalide"));
				iResult = false;
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Employee - GetNumberInsurance - " + sql, e);
		}
		
		return iResult;
	}
	
	
	//TODO - Inclure Sécurité. MROLE...
	private KeyNamePair[] GetBooklet()
	{
		String sqlCount = "select count(*) as card from p_distribution_booklet where ad_client_id <>  11";
		int nb_rep = 0;
	    try
	    {       
	        PreparedStatement stmt = DB.prepareStatement(sqlCount, null);
	        ResultSet rs = stmt.executeQuery();
	       
	        while(rs.next())
	        {
	        	nb_rep = rs.getInt("card");
	        }
	        rs.close();
	        stmt.close();
	    }
	    catch (SQLException e)
	    {
	        s_log.log(Level.SEVERE, "P_Employee.GetBooklet()", e);
	    }     
	    
		String sql = "select p_distribution_booklet_id, value + '-' + Name as Name from p_distribution_booklet where ad_client_id <>  11 order by value";
		KeyNamePair[] element = new KeyNamePair[nb_rep]; 
		
	    try
	    {
	        int index = 0;      	        
	        PreparedStatement stmt = DB.prepareStatement(sql, null);
	        ResultSet rs = stmt.executeQuery();
	        
	        while(rs.next())
	        {
	        	element[index] = new KeyNamePair(rs.getInt("p_distribution_booklet_id"), rs.getString("Name"));
	        	index++;
	        }
	        rs.close();
	        stmt.close();
	    }
	    catch (SQLException e)
	    {
	        s_log.log(Level.SEVERE, "P_Employee.GetBooklet()", e);
	    }
	    return element;
	}

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true or false
	 */

	protected boolean beforeSave (boolean newRecord)
	{

		
		if ( this.isActive() && Env.getAD_Client_ID(Env.getCtx()) == 11)
		{
			if (this.getUserCode() == null && this.getAD_User_ID() == 0)
			{
				log.saveError("ValidationError","Le code utilisateur est obligatoire");
				return false;
			}
		}

		if ( this.isActive() )
		{
			if ( this.getDistribution_Booklet_ID() == 0 && this.getP_Distribution_Booklet_ID() == 0 )
			{
				log.saveError("ValidationError","Le code de distribution est obligatoire");
				return false;
			}
			
		}

		if ( this.isRefundEIActive()   )
		{
			P_Employer Employer = P_Employer.get( Env.getCtx(), this.getP_Employer_ID(), null);
			if ( Employer.isReducedEI() == false )
			{
				log.saveError("ValidationError","Un employé sur le taux plein assurance emploi ne peux pas avoir de remboursement assurance emploi");
				return false;
				
			}
			
		}

		//2025-01-29
		if ( this.getUserCode() == null && this.getAD_User_ID() == Env.getAD_User_ID(Env.getCtx()) )
		  this.setAD_User_ID(( 0 ));
		
		CurrentLongTermLeave_ID  = getCurrentLongTermLeave_ID();
		CurrentLongTermLeaveDate = getCurrentLongTermLeaveDate();

		if ((Env.getAD_Client_ID(Env.getCtx()) != 11) && this.isActive() && ((newRecord == true) || (is_ValueChanged("Value"))))
		{
			boolean rep = GetNumberInsurance();
			if (rep == false)
			{
				return false;
			}
		}
/*
		//	Employee
		//TODO Ajout Dialogue
		P_Dist_Booklet = 0;

		
		if (newRecord == true  && this.getP_Distribution_Booklet_ID() == 0 )
		{
			KeyNamePair[] list = GetBooklet();
			KeyNamePair value = (KeyNamePair) JOptionPane.showInputDialog(null, "Choisissez un feuillet: ", "Solstice - Feuillet", JOptionPane.INFORMATION_MESSAGE, null, list, null);
			if (value != null)
			{
				P_Dist_Booklet = value.getKey();
			}
			else
			{
				P_Dist_Booklet = 0;
			}
		}
*/
		
		
    	try
		{

			// *** Concatenate FirstName and Surname to create Name
			setName(getSurname() + ", " + getFirstName());
			
			// *** Validation SIN Number
			if (getSin().length() != 0 )
			{
				if (getSin().length() != 11 )
				{
					log.saveError("ValidationError",Msg.translate(getCtx(), "SIN"));
//					log.log(Level.SEVERE,"ValidationError", Msg.translate(getCtx(), "SIN"));
					return false;
				}
	
				try
				{
					String sin  = getSin();
					sin = sin.replaceAll( "-", "");
					
					// System.out.println( "N.A.S : " + sin );
					Integer val1 = new Integer( sin.substring(1,2));
					Integer val2 = new Integer( sin.substring(3,4)); 
					Integer val3 = new Integer( sin.substring(5,6)); 
					Integer val4 = new Integer( sin.substring(7,8));
					
					val1 = new Integer( val1.intValue() + val1.intValue());
					val2 = new Integer( val2.intValue() + val2.intValue());
					val3 = new Integer( val3.intValue() + val3.intValue());
					val4 = new Integer( val4.intValue() + val4.intValue());
					
					String val5 = val1.toString() + val2.toString() + val3.toString() + val4.toString() ;
	
					int val6 = 0;
					
					for (int i = 0; i < val5.length(); i++)
					{
					  val6 = val6 + new Integer( val5.substring(i, i+1) ).intValue();    
					}
	
					val1 = new Integer( sin.substring(0,1));
					val2 = new Integer( sin.substring(2,3)); 
					val3 = new Integer( sin.substring(4,5)); 
					val4 = new Integer( sin.substring(6,7));
	
					int val7 = val1.intValue() + val2.intValue() + val3.intValue() + val4.intValue();
					
					int val8 = val6 + val7;
	
					BigDecimal val9 = new BigDecimal( val8 ) ;
					val9 = val9.divide( new BigDecimal( 10 ), BigDecimal.ROUND_CEILING);
					val9 = val9.multiply( new BigDecimal(10));
					int val10 = val9.intValue() - val8;
	
					if ( val10 != new Integer( sin.substring(8,9)).intValue() )
					{
						log.saveError("ValidationError", Msg.translate(getCtx(), "SINInvalide"));
//						log.log(Level.SEVERE,"ValidationError", Msg.parseTranslation(getCtx(), "SINInvalide"));
						return false;
					}
				}
				catch (Exception e)
				{
					log.saveError("ValidationError",Msg.translate(getCtx(), "SINInvalide"));
//					s_log.log(Level.SEVERE,"ValidationError", Msg.translate(getCtx(), "SIN"));
					return false;
				}
			}	
		}
		catch (Exception e)
		{
//			Log.saveError("ValidationError", Msg.translate(getCtx(), "SIN"));
			return true;
		}
		
		//
		// Valide numéro d'employeur
		//
		if ( Env.getAD_Client_ID( Env.getCtx() ) != 11 )
		{
			if ( this.getAD_Org_ID() == 1000011 || this.getAD_Org_ID() == 1000012 ) {
			}
			else {
				if ( this.getP_Job_Type_ID() != 0 && this.getP_Employer_ID() != 0 )
				{
					P_Job_Type JobType = P_Job_Type.get( getCtx(), this.getP_Job_Type_ID(), this.get_TrxName());
					P_Employer Employer = P_Employer.get( getCtx(), this.getP_Employer_ID(), this.get_TrxName());
					
/*					if ( JobType.isReducedEI() != Employer.isReducedEI() )
					{
						log.saveError("ValidationError", "Le numéro d'employeur n'est pas valide avec ce statut d'emploi ");
						return false;
					}
*/					
				}
				
			}
		}
		
		
		// Valide la province d'imposation avec la province de la base
    	P_Workplace workplace = P_Workplace.get( Env.getCtx(), this.getP_Workplace_ID(),  this.get_TrxName());
		if ( workplace != null && this.getTaxation_Region_ID() != workplace.getC_Region_ID() && workplace.getC_Region_ID() != 0)
		{

			log.saveError("ValidationError","La province d'imposition est différent de la province du lieu de travail");
			return false;

		}
		
	    if ( this.getEMail() == null && this.getUserCode() != null )
	    	this.setEMail( this.getUserCode()+ PgiUtil.getEmailDomain() );

	    //2014.04.29
//	    if ( PgiUtil.getSolsticeParameter(Env.getCtx(), "PayStubByEmail").toLowerCase().equals("true") && this.getPassword() == null  )
//	    	setNewPassword();

	    
	    if ( ! newRecord && is_ValueChanged("P_LongTermLeave_ID")   )
	    	// && ! is_ValueChanged("LongTermLeaveDate")
	    {
	    	//TODO traduction
	    	if ( this.getLongTermLeaveDate() == null )
	    	{
				log.saveError("ValidationError", "Vous ne pouvez pas modifier le statut d'activité sans identifier la date de début du statut d'activité ");
				return false;
	    	}

	    	//TODO traduction
	    	if ( this.CurrentLongTermLeaveDate != null && this.getLongTermLeaveDate().compareTo( this.CurrentLongTermLeaveDate ) < 0 )
	    	{
				log.saveError("ValidationError", "Vous ne pouvez pas modifier le statut d'activité pour une date inférieur à la date de l'ancien statut ");
				return false;
	    	}

	    }

	    //
        // Modification pour hélicoptère, mais cela ne devrait pas avoir d'impacte chez d'autre client vue que les champs seronts invisible.
	    //	    
		if (PgiUtil.getSolsticeParameter(Env.getCtx(), "Client").equals("CHL"))
		{
		    if ( !newRecord && ( is_ValueChanged("C_Activity_ID") || is_ValueChanged("AD_Org_ID") || is_ValueChanged("P_Workplace_ID")))
		    {
		    	String sql = "Select P_Assignment.P_Assignment_ID, P_Assignment_Distribution.P_Assignment_Distribution_ID, P_Assignment.P_WorkPlace_ID "
		    			   + " From P_Assignment, P_Assignment_Distribution " 
		    			   + " Where P_Assignment.P_Employee_ID = " + this.getP_Employee_ID()
		    			   + "   and P_Assignment_Distribution.P_Assignment_ID = P_Assignment.P_Assignment_ID "
	  	    			   + "   and P_Assignment.IsActive = 'Y' "
		    			   + "   and ( P_Assignment.EndDate is null or P_Assignment.EndDate > sysdate ) "
		    			   ;

				PreparedStatement pstmt = null;

				try
				{
					pstmt = DB.prepareStatement (sql, null);
					ResultSet rs = pstmt.executeQuery ();
//			    	P_Workplace workplace = P_Workplace.get( Env.getCtx(), this.getP_Workplace_ID(),  this.get_TrxName());
			    	//2014.04.17 Modification pour Helico.
			    	if ( rs.getInt("P_Workplace_ID") != 0)
			    		workplace = P_Workplace.get( Env.getCtx(), rs.getInt("P_Workplace_ID"),  this.get_TrxName());

					while (rs.next ())
					{
				    	P_Assignment_Distribution distribution = P_Assignment_Distribution.get( Env.getCtx(), rs.getInt("P_Assignment_Distribution_ID"), this.get_TrxName());
				    	distribution.setC_Activity_ID( this.getC_Activity_ID() );
				    	distribution.setOrg_ID( this.getAD_Org_ID());
				    	distribution.setC_SalesRegion_ID( workplace.getC_SalesRegion_ID() );
				    	distribution.save( );
						
					}
					rs.close ();
					pstmt.close ();
					pstmt = null;
				}
				catch (Exception e)
				{
					log.log (Level.SEVERE,"doIt - " + sql, e);
				}


		    }
		}

	    
	    // 
	    // Ajout audit trail 
	    //
	    if ( ! newRecord )
			if ( PgiUtil.getSolsticeParameter(Env.getCtx(), "AuditTrailInsurance").equals("True")  )
				P_Audit_Trail_Insurance.Employee_Audit( this, newRecord );
		
		return true;
	}	//	beforeSave
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 */
	
	static boolean preventRecursiveAfterSave = false;
	protected boolean afterSave (boolean newRecord, boolean success)
	{

	    if (!success )
		{
			return success;
		}
	    if(preventRecursiveAfterSave == true)
	    {
	    	preventRecursiveAfterSave = false;
	    	return true;
	    }


//	    if ((P_Dist_Booklet != 0) && (newRecord == true))
	    if (this.getP_Distribution_Booklet_ID() == 0 && this.getDistribution_Booklet_ID() != 0)
	    {
		    P_Employee_Distribution_Booklet Booklet = new P_Employee_Distribution_Booklet( getCtx(), -1, get_TrxName());
		    P_Period Period = P_Period.getOpenPeriod(getCtx(), this.getP_Payment_Group_ID(), get_TrxName());
		    
		    Booklet.setP_Employee_ID(this.getP_Employee_ID());
//		    Booklet.setP_Distribution_Booklet_ID(P_Dist_Booklet);
		    Booklet.setP_Distribution_Booklet_ID(this.getDistribution_Booklet_ID());
		    Booklet.setStart_Date(Period.getStartDate());
		    Booklet.save();
	    }
	    
	    P_Employee_Date_Hist.createHisto( Env.getCtx(), this, newRecord );
//	    P_Employee_Hst.createHisto( Env.getCtx(), this); 

	    //
	    // Profile d'absence
	    //
	    if ( is_ValueChanged("P_LongTermLeave_ID") || is_ValueChanged("LongTermLeaveDate")  )
	    {
	    	// Actif = ID 0
		    if ( this.getP_LongTermLeave_ID() != 0 )
		    {
		    	if ( CurrentLongTermLeave_ID != this.getP_LongTermLeave_ID() )
		    	{
		    		
		    		// Modify the old record		    		
			    	P_Employee_LongTermLeave Employee_LongTermLeave = P_Employee_LongTermLeave.get ( Env.getCtx(), this.getP_Employee_ID(), CurrentLongTermLeave_ID, CurrentLongTermLeaveDate, this.get_TrxName());
			    	Employee_LongTermLeave.setEndDate( TimeUtil.addDays( this.getLongTermLeaveDate(), -1) );
			    	if ( Employee_LongTermLeave.getP_Employee_ID() != 0)
			    		Employee_LongTermLeave.save();
			    	
			    	// Create new record
		    		Employee_LongTermLeave = P_Employee_LongTermLeave.get ( Env.getCtx(), this.getP_Employee_ID(), -1, this.get_TrxName());
			    	Employee_LongTermLeave.setAD_Org_ID(this.getAD_Org_ID());
			    	Employee_LongTermLeave.setP_Employee_ID( this.getP_Employee_ID());
			    	Employee_LongTermLeave.setP_LongTermLeave_ID(this.getP_LongTermLeave_ID() );
			    	Employee_LongTermLeave.setStartDate( this.getLongTermLeaveDate() );
			    	Employee_LongTermLeave.setLongtermLeaveMth("01");

			    	Employee_LongTermLeave.save();

			    	P_Assignment Assignment = P_Assignment.get( Env.getCtx(), this.GetAssignmentPrincipal(), null);
			    	P_Assignment_Param Param = P_Assignment_Param.get(Env.getCtx(), Assignment.getP_Assignment_ID(), this.getLongTermLeaveDate() , null ); 
			    	BigDecimal nbrHre = Param.getDay_Hours();
			    	Employee_LongTermLeave.setMonday( nbrHre );
			    	Employee_LongTermLeave.setThursday(nbrHre);
			    	Employee_LongTermLeave.setWednesday(nbrHre);;
			    	Employee_LongTermLeave.setTuesday(nbrHre);
			    	Employee_LongTermLeave.setFriday(nbrHre);
			    	Employee_LongTermLeave.save();
		    		
		    	}
		    	else
		    	{
			    	// update current record
			    	P_Employee_LongTermLeave Employee_LongTermLeave = P_Employee_LongTermLeave.get ( Env.getCtx(), this.getP_Employee_ID(), CurrentLongTermLeave_ID, CurrentLongTermLeaveDate, this.get_TrxName());
			    	Employee_LongTermLeave.setAD_Org_ID(this.getAD_Org_ID());
			    	Employee_LongTermLeave.setP_Employee_ID( this.getP_Employee_ID());
			    	Employee_LongTermLeave.setP_LongTermLeave_ID(this.getP_LongTermLeave_ID() );
			    	Employee_LongTermLeave.setStartDate( this.getLongTermLeaveDate() );
			    	Employee_LongTermLeave.setLongtermLeaveMth("01");
			    	Employee_LongTermLeave.save();
		    		
		    	}
		    }
		    else
		    {
	    		// Modify the old record		    		
		    	P_Employee_LongTermLeave Employee_LongTermLeave = P_Employee_LongTermLeave.get ( Env.getCtx(), this.getP_Employee_ID(), CurrentLongTermLeave_ID, CurrentLongTermLeaveDate, this.get_TrxName());
		    	Employee_LongTermLeave.setEndDate( TimeUtil.addDays( this.getLongTermLeaveDate(), -1) );
		    	Employee_LongTermLeave.save();
		    }
			CurrentLongTermLeave_ID  = this.getP_LongTermLeave_ID();
			CurrentLongTermLeaveDate = this.getLongTermLeaveDate();
		    
	    }

	    

	    

	    
	    //
	    // Si l'on change le code d'usager alors l'on créer un nouvel usager avec la sécurité d'access au avis de dépot. 
	    // TODO avoir un table de paramétrisation pour déterminer les modules de sécurité a donne access.

    	MUser user = null;

    	boolean newuser = false;
    	boolean validation = false;
    	if ( this.getAD_User_ID() != 0 )
    	{
		    user = new MUser(Env.getCtx(), this.getAD_User_ID(), this.get_TrxName());
		    if ( user != null && this.getUserCode() != null && ! user.getValue().trim().equals(this.getUserCode().trim()) )
		    {
			    user = new MUser(Env.getCtx(), -1, this.get_TrxName());
			    newuser = true;
		    }
    	}
    	else if ( this.getUserCode() != null )
    	{
		    user = new MUser(Env.getCtx(), -1, this.get_TrxName());
		    newuser = true;
		    validation = true;
    	}

	    if ( this.getUserCode() != null )
	    {
		    //Creation d'un enregistrement dans AD_User
		    user.setAD_Org_ID(this.getAD_Org_ID());
		    	    
		    user.setIsActive(true);

		    user.setValue(this.getUserCode() );
		    user.setName( this.getName());
		    user.setDescription( "" );
//			CurrentUserCode = getCurrentUserCode();
//		    if( CurrentUserCode == null || this.getUserCode().compareTo( CurrentUserCode ) != 0 || user.getValue() != this.getUserCode() )
		    if ( newuser )
		    {
		    	user.setPassword("resetpassword123!");
		    }
		    //
		    if ( this.getEMail() != null )
		    	user.setEMail( this.getEMail() );
		    else
		    	user.setEMail( this.getUserCode()+ PgiUtil.getEmailDomain() );

    		if ( newuser && validation )
    		{
			    if ( validUserCode(this.getUserCode()) == false )
			    {
					String message = "Le code d'usager '"+this.getUserCode()+"' n'est pas disponible. Veuillez en saisir un nouveau.";
			   		JOptionPane optionPane1 = new JOptionPane(message,
		                    JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION);
		            optionPane1.createDialog(new Frame(), null).setVisible(true);
		            return false;
			    }
    		}

		    if ( ! user.save() )
			{	
		    	s_log.log(Level.SEVERE,"afterSave - no AD_User Created.");
				String message = "Le code d'usager '"+this.getUserCode()+"' n'a pas été créé ";
		   		JOptionPane optionPane1 = new JOptionPane(message, JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION);
	            optionPane1.createDialog(new Frame(), null).setVisible(true);
		    	return false;
			}

	    	this.setAD_User_ID(user.getAD_User_ID());
	    	preventRecursiveAfterSave = true;
	    	this.save();
		    
    		if ( newuser )
		    {
		    	
		    	//Insertion automatique dans les groupes de sécurité
		    	
		    	String sql = "Select P_Group_ID, InsertOnNewEmp From P_Group";
		        try
		        {
		            PreparedStatement stmt = DB.prepareStatement(sql, null);
		            ResultSet rs = stmt.executeQuery();
		            
		            while(rs.next())
		            {
		            	if(rs.getString("InsertOnNewEmp").toUpperCase().equals("Y"))
		            	{
					    	X_P_Group_Employee GroupEmployee = new X_P_Group_Employee(Env.getCtx(), -1, this.get_TrxName());
					    	GroupEmployee.setP_Employee_ID( this.getP_Employee_ID());			    	
					    	GroupEmployee.setP_Group_ID( rs.getInt("P_Group_ID") );
					    	GroupEmployee.save();
		            	}
		            }
		            rs.close();
		            stmt.close();
		        }
		        catch (SQLException e)
		        {
			    	s_log.log(Level.SEVERE,"afterSave - "+e.toString());
					String message = "Erreur lors de l'insertion dans un groupe de sécurité.";
			   		JOptionPane optionPane1 = new JOptionPane(message, JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION);
		            optionPane1.createDialog(new Frame(), null).setVisible(true);
			    	return false;
		        }
		    	
		    }
	    }

/*	    MBPartner BPartner =  MBPartner.get( getCtx(), this.getValue() );


	    if ( BPartner == null )
	        BPartner = new MBPartner( getCtx(), -1, this.get_TrxName() );
	    
        BPartner.setValue( this.getValue());
        BPartner.setName( this.getName());
        BPartner.setIsSummary( false);
        // TODO
        BPartner.setIsEmployee( true );
        //2014.04.29 set langue a celle de l'employé
        P_Language language = P_Language.get( Env.getCtx(), this.getP_Language_ID(), this.get_TrxName() );
        BPartner.setAD_Language( language.getValue() );
        BPartner.setIsOneTime( false );               
        BPartner.setIsProspect( false );               
        BPartner.setIsVendor( false );                
        BPartner.setIsCustomer( false );
        BPartner.setC_BP_Group_ID( 105 );
        BPartner.save();
*/

	    if ( this.getUserCode() != null )
	    {
	        
	//	    user.setC_BPartner_ID(BPartner.getC_BPartner_ID());
			user.setEMail( this	.getEMail());
			user.setBirthday(this.getBirthDate() );
			user.setDescription(this.getName());
			user.setPhone(this.getPhone());
		    user.save();
	    }

	    if ( newRecord )
			if ( PgiUtil.getSolsticeParameter(Env.getCtx(), "AuditTrailInsurance").equals("True")  )
				P_Audit_Trail_Insurance.Employee_Audit( this, newRecord );

	    return true;
	}  //	afterSave	


	/*
	 * getStandardTimeInMonth(int iYear, int iMonth)
	 * 
	 * Permet d'aller chercher le nombre d'heure qu'un employé aurait
	 * du travailler dans un mois donné pour une année donnée
	 * 
	 * param : iYear - l'année pour lequel on veut connaitre le nombre d'heure
	 * param : iMonth - le mois pour lequel on veut connaitre le nombre d'heure
	 * return : le nombre d'heure qu,aurait du être travaillé
	 */
	public int getWorkDayInMonth(int iYear, int iMonth)
	{
		//on veut calculer le nombre de jour de travail pour un employe
		//pour un mois donne
		
		//on commence par aller chercher la date de debut du mois
		//et la date de fin de mois
		
		//calendar/date/timestamp debut
		Calendar start = Calendar.getInstance();
		start.set( iYear, iMonth -1, 1 );
		Date startDate = start.getTime();
		Timestamp monthStartDate = new Timestamp( startDate.getTime() ) ;
		
		//calendar/date/timestamp fin
		Calendar end = Calendar.getInstance();
		end.set( iYear, iMonth-1, start.getActualMaximum( Calendar.DAY_OF_MONTH ) );
		Date endDate = end.getTime();
		Timestamp monthEndDate = new Timestamp( endDate.getTime() ) ;

		
		//on va chercher le nombre de jour total du mois
		// cad : nombre de jour entre date de debut et date de fin
		int iNumDayMonth = TimeUtil.getDaysBetween(monthStartDate, monthEndDate);
		iNumDayMonth ++;//on ajoute une journée puisque pour octobre (01-31) ca donne 30 jours
		//mais ca devrait donner 31
		
		//var pour le nombre de jour travaillés du mois
		int iNumWorkDayMonth = 0;
		
		//on boucle au travers toutes les journees
		//on verifies si :
		//Ce n'est pas un samedi ni un dimanche.
		//Bon, c'est peut etre pas la meilleure facon de faire, mais
		//c'est comme ca que c'etait fait (voir sp_pr042.sql)
		for(int idx = 0; idx < iNumDayMonth; idx++)
		{
			//pour chaque journée du mois (debut)
			Calendar added = (Calendar)start.clone();
			added.add(Calendar.DATE, idx);
			//pour chaque journée du mois (fin)
			
			//on verifie que c'est pas ni un dimanche (0) ni un samedi (6)
//			Date day = added.getTime();
//			if(day.getDay() != 0 && day.getDay() != 6)
			
			if (  added.get( Calendar.DAY_OF_WEEK) != Calendar.SATURDAY &&
				  added.get( Calendar.DAY_OF_WEEK) != Calendar.SUNDAY
			   )
			{
				iNumWorkDayMonth++;
			}
		}
		
		
		return iNumWorkDayMonth;
	}

	private boolean validUserCode(String strUserCode)
	{
		boolean retValue = true;
		String sql = " SELECT 1 FROM AD_User "
			+ " WHERE Value = '"+strUserCode+"'";
		
		PreparedStatement pstm = DB.prepareStatement(sql, null);
		try
		{
			ResultSet rs = pstm.executeQuery();
			if(rs.next())
			{
				retValue = false;
			}
		}
		catch(Exception e)
		{
			s_log.log(Level.SEVERE,"lookUpUserCode - sql error : "+sql);
		}
		return retValue;
	}

	
	public BigDecimal getInsurableSalary(  ) 
	{	// Modif jeaham01 29/10/07
		String sql = "Select dbo.fn_getInsurableSalary( " + this.getP_Employee_ID() + " ) as InsurableSalary" ;
		BigDecimal bd = Env.ZERO;
		
//ici... age de l'employé au ???		
//		Date date = new Date();
//		Timestamp t = new Timestamp(date.getTime());
		P_Period Period = P_Period.getOpenPeriod(Env.getCtx(), this.getP_Payment_Group_ID(), this.get_TrxName());
		int age = this.getAge( Period.getPayDate() );
		if (age > 70)
		{
			return Env.ZERO;
		}
		
		PreparedStatement pstm = DB.prepareStatement(sql, null);
		try
		{
			ResultSet rs = pstm.executeQuery();
			if ( rs.next() )
			{
				bd = rs.getBigDecimal("InsurableSalary");		
			}
		
		}
		catch(Exception e)
		{
			s_log.log(Level.SEVERE,"getInsurableSalary - sql error : "+sql);
		}	

		if (age > 65)
		{
			BigDecimal pourcent = new BigDecimal(0.5);
			bd = bd.multiply(pourcent) ;
		}

		return bd;
	}

	// Add 2008-08-07 retour le salaire assurable pour une période de paie
	public BigDecimal getInsurableSalary( int P_Period_ID, int P_Deduction_ID ) 
	{	
		String sql = "Select dbo.fn_getInsurableSalaryPeriod( " + this.getP_Employee_ID() + ", " + P_Period_ID +  ", " + P_Deduction_ID + " ) as InsurableSalary" ;
		BigDecimal bd = Env.ZERO;
		
		P_Period Period = P_Period.get(Env.getCtx(), P_Period_ID, this.get_TrxName());
		int age = this.getAge( Period.getPayDate() );
		if (age >= 70)
		{
			return Env.ZERO;
		}
		
		PreparedStatement pstm = DB.prepareStatement(sql, null);
		try
		{
			ResultSet rs = pstm.executeQuery();
			if ( rs.next() )
			{
				bd = rs.getBigDecimal("InsurableSalary");		
			}
		
		}
		catch(Exception e)
		{
			s_log.log(Level.SEVERE,"getInsurableSalary - sql error : "+sql);
		}	


		return bd;
	}

	public BigDecimal getVacationRate() 
	{
		if ( this.isVacationFixeRate() )
		{
			return this.getVacationFixeRate();
		}
		
		String sql = "Select dbo.P_Employee_VacationRate( " + this.getP_Employee_ID() + " ) as VacationRate" ;
		BigDecimal bd = Env.ZERO;
		
		PreparedStatement pstm = DB.prepareStatement(sql, null);
		try
		{
			ResultSet rs = pstm.executeQuery();
			if(rs.next())
			{
				bd = rs.getBigDecimal("VacationRate");
			}
		}
		catch(Exception e)
		{
			s_log.log(Level.SEVERE,"getVacationRate - sql error : "+sql);
		}

		
		return bd;
	}

	public BigDecimal getPrevVacationRate() 
	{
		if ( this.isVacationFixeRate() )
		{
			return this.getVacationFixeRate();
		}
		
		String sql = "Select dbo.P_Employee_PrevVacationRate( " + this.getP_Employee_ID() + " ) as VacationRate" ;
		BigDecimal bd = Env.ZERO;
		
		PreparedStatement pstm = DB.prepareStatement(sql, null);
		try
		{
			ResultSet rs = pstm.executeQuery();
			if(rs.next())
			{
				bd = rs.getBigDecimal("VacationRate");
			}
		}
		catch(Exception e)
		{
			s_log.log(Level.SEVERE,"getVacationRate - sql error : "+sql);
		}

		
		return bd;
	}

	public BigDecimal getOverTimeRate() 
	{
		String sql = "Select dbo.fn_getOverTimeRate( " + this.getP_Employee_ID() + " ) as OverTimeRate" ;
		BigDecimal bd = Env.ZERO;
		
		PreparedStatement pstm = DB.prepareStatement(sql, null);
		try
		{
			ResultSet rs = pstm.executeQuery();
			if(rs.next())
			{
				bd = rs.getBigDecimal("OverTimeRate");
			}
		}
		catch(Exception e)
		{
			s_log.log(Level.SEVERE,"getOverTimeRate - sql error : "+sql);
		}

		
		return bd;
	}

	public BigDecimal getAccumulatedDaysRate() 
	{
		String sql = "Select dbo.fn_getAccumulatedDaysRate( " + this.getP_Employee_ID() + " ) as Rate" ;
		BigDecimal bd = Env.ZERO;
		
		PreparedStatement pstm = DB.prepareStatement(sql, null);
		try
		{
			ResultSet rs = pstm.executeQuery();
			if(rs.next())
			{
				bd = rs.getBigDecimal("Rate");
			}
		}
		catch(Exception e)
		{
			s_log.log(Level.SEVERE,"getAccumulatedDaysRate - sql error : "+sql);
		}

		
		return bd;
	}

	public BigDecimal getShortTermDisabilityRate() 
	{
		String sql = "Select dbo.fn_getShortTermDisabilityRate( " + this.getP_Employee_ID() + " ) as Rate" ;
		BigDecimal bd = Env.ZERO;
		
		PreparedStatement pstm = DB.prepareStatement(sql, null);
		try
		{
			ResultSet rs = pstm.executeQuery();
			if(rs.next())
			{
				bd = rs.getBigDecimal("Rate");
			}
		}
		catch(Exception e)
		{
			s_log.log(Level.SEVERE,"getShortTermDisabilityRate - sql error : "+sql);
		}

		
		return bd;
	}

	public BigDecimal getCsstRate() 
	{
		String sql = "Select dbo.fn_getCsstRate( " + this.getP_Employee_ID() + " ) as Rate" ;
		BigDecimal bd = Env.ZERO;
		
		PreparedStatement pstm = DB.prepareStatement(sql, null);
		try
		{
			ResultSet rs = pstm.executeQuery();
			if(rs.next())
			{
				bd = rs.getBigDecimal("Rate");
			}
		}
		catch(Exception e)
		{
			s_log.log(Level.SEVERE,"getCsstRate - sql error : "+sql);
		}

		
		return bd;
	}

	//
	// Retour Region Residence
	//
	public MRegion getRegionResidence()
	{
		MLocation Location = MLocation.get( Env.getCtx(), this.getC_Location_ID(), this.get_TrxName());
		MRegion RegionResidence = null;
		
		if ( Location != null )
			RegionResidence = new MRegion( Env.getCtx(), Location.getC_Region_ID(), this.get_TrxName());
		
		if ( RegionResidence == null || Location.getC_Region_ID() == 0	)
		{
			RegionResidence = new MRegion( Env.getCtx(), this.getTaxation_Region_ID(), this.get_TrxName());
		}

		return RegionResidence;
	}


	private String getCurrentUserCode()
	{
		String sql = "SELECT UserCode FROM P_Employee WHERE P_Employee_ID = " + this.getP_Employee_ID(); 

		String code = null;
		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				code = rs.getString(1);
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		
		return code;
	}

	private int getCurrentLongTermLeave_ID()
	{
		String sql = "SELECT P_LongTermLeave_ID FROM P_Employee WHERE P_Employee_ID = " + this.getP_Employee_ID(); 

		int id = 0;
		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				id = rs.getInt(1);
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		
		return id;
		
	}

	public Timestamp getCurrentLongTermLeaveDate()
	{
		String sql = "SELECT LongTermLeaveDate FROM P_Employee WHERE P_Employee_ID = " + this.getP_Employee_ID();

		Timestamp date = null;
		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				date = rs.getTimestamp( 1 );
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		
		return date;
		
	}

	public boolean isLongTermLeave( P_Period Period, String trxName)
	{
		String sql = null;
		sql = "Select P_Employee_LongTermLeave_ID "                                                                          
			+ " From P_Employee_LongTermLeave "
            + " WHERE P_Employee_LongTermLeave.P_Employee_ID=" + this.getP_Employee_ID()
			+ " AND IsActive = 'Y' "
			+ " AND StartDate <= " + DB.TO_DATE( Period.getStartDate() )
			+ " AND ( EndDate is null OR EndDate >= " + DB.TO_DATE( Period.getEndDate() )+ ") "
			+ " AND P_LongTermLeave_ID <> 0 "  // 0 = actif.
			;

		Boolean result = false;
		
		PreparedStatement pstmt  = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				result = true;
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;

		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"isLongTermLeave - " + sql, e);
		}
	
		
		return result;
	}

	public BigDecimal getDay_HoursRWT( Timestamp EffectIn )
	{
	    P_Employee_RWT_Detail RWT = GetRWTDetail( EffectIn, true );

	    if	( RWT != null )
			return RWT.getDay_Hours();
	    
	    return null;
	}

	public BigDecimal getWeekly_HoursRWT( Timestamp EffectIn )
	{
		BigDecimal Weekly_Hours = null;
		Weekly_Hours = this.getWeekly_Hours_Total(EffectIn, this.get_TrxName());
		// todo read Frequency for the number of week
//		Weekly_Hours = Weekly_Hours.divide(new BigDecimal(2), 2, BigDecimal.ROUND_HALF_UP);
		return Weekly_Hours;
		
	}

	public BigDecimal getWeekly_Hours_Total( Timestamp EffectIn, String trxName )
	{
		String sql = null;
		sql = "Select sum( RwtQuantity ) RwtQuantity, sum(  WEEKNUMBER )  WEEKNUMBER FROM P_Employee_RWT, P_Employee_RWT_Detail ";
		sql += " WHERE P_Employee_RWT_Detail.P_Employee_RWT_ID = P_Employee_RWT.P_Employee_RWT_ID AND P_Employee_RWT.IsActive='Y' ";
		sql += " AND P_Employee_RWT.P_Employee_ID=" + this.getP_Employee_ID();
		sql += " AND " + DB.TO_DATE( EffectIn) + " BETWEEN startdate AND isnull( enddate," + DB.TO_DATE( EffectIn) + ") ";
		//
		
		BigDecimal RwtQuantity = null;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				RwtQuantity = rs.getBigDecimal("RwtQuantity");	
				if ( rs.getInt( "WEEKNUMBER" ) == 0)
					RwtQuantity = RwtQuantity.multiply(new BigDecimal(2));

			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"IsRWT - " + sql, e);
		}

		
		P_Assignment_Param AssignmentParamBase = P_Assignment_Param.get( Env.getCtx(), this.GetPrincipalAssignment_Param(), trxName );
		BigDecimal Weekly_Hours_Base = AssignmentParamBase.getWeekly_Hours().multiply(new BigDecimal(2));

		return Weekly_Hours_Base.add( RwtQuantity );
	}

	
	public P_Employee_RWT_Detail GetRWTDetail( Timestamp EffectIn, boolean global )
	{
		if ( ! existsRWT )
			return null;
		
		int weekNumber = getWeekNumber(  EffectIn );
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime( (Date)EffectIn );

		int dayOfWeek  = cal.get(Calendar.DAY_OF_WEEK) -1;
		
		String key = this.getP_Employee_ID() + "|" + weekNumber + "|" + EffectIn.toString().substring(0, 10)+ "|" + dayOfWeek;
		P_Employee_RWT_Detail RWT = (P_Employee_RWT_Detail)s_cacheRWT.get(key);
		if (RWT != null)
			return RWT;
			

		String sql = null;
		sql = "Select P_Employee_RWT_Detail.P_Employee_RWT_Detail_ID FROM P_Employee_RWT, P_Employee_RWT_Detail ";
		sql += " WHERE P_Employee_RWT_Detail.P_Employee_RWT_ID = P_Employee_RWT.P_Employee_RWT_ID AND P_Employee_RWT.IsActive='Y' ";
		sql += " AND P_Employee_RWT.P_Employee_ID=" + this.getP_Employee_ID();
		sql += " AND P_Employee_RWT_Detail.Weeknumber in ( 0," + weekNumber + " )";
		sql += " AND " + DB.TO_DATE( EffectIn) + " BETWEEN startdate AND isnull( enddate," + DB.TO_DATE( EffectIn) + ") ";
		if ( ! global )
			sql += " AND P_Employee_RWT_Detail.WeekDay = " + dayOfWeek	;
		//
		
		int P_Employee_RWT_Detail_ID = 0;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				isRWT = true;
				P_Employee_RWT_Detail_ID = rs.getInt("P_Employee_RWT_Detail_ID");	
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"IsRWT - " + sql, e);
		}

		if ( P_Employee_RWT_Detail_ID == 0)
			return null;

		RWT = P_Employee_RWT_Detail.get( Env.getCtx(), P_Employee_RWT_Detail_ID, this.get_TrxName());
		s_cacheRWT.put (key, RWT);

		return RWT;
	}

	public int getWeekNumber( Timestamp EffectIn )
	{
		int weekNumber = 0;
		
		P_Period Period = P_Period.get(Env.getCtx(), EffectIn, this.get_TrxName());
		weekNumber = new BigDecimal( TimeUtil.getDaysBetween(Period.getStartDate(), EffectIn) +1 ) .divide(new BigDecimal(7), 0, BigDecimal.ROUND_UP).intValue();
		return weekNumber;
	}
	
	private boolean isRWT = false;

	public P_Employee_RWT_Detail GetRWT( Timestamp EffectIn )
	{
		if ( ! existsRWT )
			return null;
		
		int weekNumber = getWeekNumber(  EffectIn );
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime( (Date)EffectIn );

		int dayOfWeek  = cal.get(Calendar.DAY_OF_WEEK) -1;
		
		String key = this.getP_Employee_ID() + "|" + weekNumber + "|" + EffectIn.toString().substring(0, 10)+ "|" + dayOfWeek;
		P_Employee_RWT_Detail RWT = (P_Employee_RWT_Detail)s_cacheRWT2.get(key);
		if (RWT != null)
			return RWT;

		String sql = null;
		sql = "Select P_Employee_RWT_Detail.P_Employee_RWT_Detail_ID FROM P_Employee_RWT, P_Employee_RWT_Detail ";
		sql += " WHERE P_Employee_RWT_Detail.P_Employee_RWT_ID = P_Employee_RWT.P_Employee_RWT_ID AND P_Employee_RWT.IsActive='Y' ";
		sql += " AND P_Employee_RWT.P_Employee_ID=" + this.getP_Employee_ID();
		sql += " AND P_Employee_RWT_Detail.Weeknumber in ( 0," + weekNumber + " )";
		sql += " AND " + DB.TO_DATE( EffectIn) + " BETWEEN startdate AND isnull( enddate," + DB.TO_DATE( EffectIn) + ") ";
//		sql += " AND P_Employee_RWT_Detail.WeekDay = " + dayOfWeek	;
		//
		
		int P_Employee_RWT_Detail_ID = 0;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				isRWT = true;
				P_Employee_RWT_Detail_ID = rs.getInt("P_Employee_RWT_Detail_ID");	
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"IsRWT - " + sql, e);
		}

		if ( P_Employee_RWT_Detail_ID == 0)
			return null;

		RWT = P_Employee_RWT_Detail.get( Env.getCtx(), P_Employee_RWT_Detail_ID, this.get_TrxName());
		s_cacheRWT2.put (key, RWT);

		return RWT;
	}

	//
	// Indique si l'affectation a une cédule artt ou non.
	//
	public boolean isRWT( Timestamp EffectIn )
	{
	    boolean isRWT = false;
	    
	    P_Employee_RWT_Detail RWT = GetRWT( EffectIn );

	    if	( RWT != null && RWT.isWorkingShortTime() == false )
			isRWT = true;
		
		return isRWT;
	}

	//
	// Indique si l'affectation a une cédule artt ou non.
	//
	public boolean isRWTForCredits( Timestamp EffectIn )
	{
	    boolean isRWT = false;
	    
	    P_Employee_RWT_Detail RWT = GetRWTForCredits( EffectIn );

	    if	( RWT != null && RWT.isWorkingShortTime() == false )
			isRWT = true;
		
		return isRWT;
	}

	
	// Travail à temps réduit ( Working short time )
	// Temps de travail aménagé afin de réduire temporairement la durée de la semaine normale de travail d'un salarié en raison d'une situation particulière.
	
	public boolean isWorkingShortTime( Timestamp EffectIn )
	{
	    boolean isWorkingShortTime = false;
	    
	    P_Employee_RWT_Detail RWT = GetRWT( EffectIn );

	    if	( RWT != null  && RWT.isWorkingShortTime())
	    	isWorkingShortTime = true;
		
		return isWorkingShortTime;
	}



	public P_Employee_RWT_Detail GetRWTForCredits( Timestamp EffectIn )
	{
		if ( ! existsRWT )
			return null;
		
		int weekNumber = getWeekNumber(  EffectIn );
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime( (Date)EffectIn );

		int dayOfWeek  = cal.get(Calendar.DAY_OF_WEEK) -1;
		
		String key = this.getP_Employee_ID() + EffectIn.toString().substring(0, 10)+ "|" + dayOfWeek;
		P_Employee_RWT_Detail RWT = (P_Employee_RWT_Detail)s_cacheRWT3.get(key);
		if (RWT != null)
			return RWT;

		String sql = null;
		sql = "Select P_Employee_RWT_Detail.P_Employee_RWT_Detail_ID FROM P_Employee_RWT, P_Employee_RWT_Detail ";
		sql += " WHERE P_Employee_RWT_Detail.P_Employee_RWT_ID = P_Employee_RWT.P_Employee_RWT_ID AND P_Employee_RWT.IsActive='Y' ";
		sql += " AND P_Employee_RWT.P_Employee_ID=" + this.getP_Employee_ID();
//		sql += " AND P_Employee_RWT_Detail.Weeknumber in ( 0," + weekNumber + " )";
		sql += " AND " + DB.TO_DATE( EffectIn) + " BETWEEN startdate AND isnull( enddate," + DB.TO_DATE( EffectIn) + ") ";
//		sql += " AND P_Employee_RWT_Detail.WeekDay = " + dayOfWeek	;
		//
		
		int P_Employee_RWT_Detail_ID = 0;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				isRWT = true;
				P_Employee_RWT_Detail_ID = rs.getInt("P_Employee_RWT_Detail_ID");	
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"IsRWT - " + sql, e);
		}

		if ( P_Employee_RWT_Detail_ID == 0)
			return null;

		RWT = P_Employee_RWT_Detail.get( Env.getCtx(), P_Employee_RWT_Detail_ID, this.get_TrxName());
		s_cacheRWT3.put (key, RWT);

		return RWT;
	}

	
	public void setNewPassword() 
	{
    	try
		{
 //2022   		this.setPassword( RandomStringUtils.randomAlphanumeric(8) );

    		/*
        	InternetAddress addressFrom = PgiUtil.getSendEmailAddress("drh");
			addressFrom =  new InternetAddress( "canadianhelicopters@solsticeplus.com" , "Information paie et ressources humaines / Payroll and Human Resources Information ");
	
	    	int R_MailText_ID = 1000001;
	    	MMailText infoMail = new MMailText (getCtx(), R_MailText_ID, get_TrxName());
	
	
	    	MBPartner BPartner = MBPartner.get( getCtx(), this.getValue() );
	    	infoMail.setBPartner( BPartner );
	
			EMailSender emailSender = new EMailSender( getCtx());
	    	emailSender.setAddress( addressFrom );
        	String addressTo = this.getPersonalEmail() != null ? this.getPersonalEmail() : this.getEMail(); 
	    	emailSender.sendEMail(  addressTo, infoMail.getMailHeader(), infoMail.getMailText().replace( "[Password]" , this.getPassword() ) );
*/	    	
		}
		catch(Exception e)
		{
			s_log.log(Level.SEVERE,"setNewPassword  : ", e);
		}

	}

	public void setP_LongTermLeave_ID (int P_LongTermLeave_ID)
	{
	//if (P_LongTermLeave_ID < 1) throw new IllegalArgumentException ("P_LongTermLeave_ID is mandatory.");
		set_Value ("P_LongTermLeave_ID", new Integer(P_LongTermLeave_ID));
	}

	
	public int getSeniority( Timestamp d ) 
	{
		Timestamp DateSeniority = (Timestamp)get_Value("DateSeniority");
		
	    LocalDate dateBefore = LocalDate.parse(DateSeniority.toString().substring(0,10));
	    LocalDate dateAfter= LocalDate.parse(d.toString().substring(0,10) );
		
	    long monthsBetween = ChronoUnit.MONTHS.between( dateBefore, dateAfter);
	    
	    return (int)monthsBetween ;
	}

	
	public ArrayList<BigDecimal> getIndemnityStatutoryHolidays( Timestamp PeriodStartDate, Timestamp HolidayDate, int AssignmentParam_ID, int GainColumn_ID, BigDecimal GainMultiplyRate, String TrxName )
	{
	
		// SM 2012-11-28 Va me permettre dans la méthode 121 de connaître la semaine où ma date se situe
        int nbrWeek = 0;
        int nbrWeekBack = 0;
		BigDecimal []quantity = new BigDecimal[2];
		BigDecimal quantityCalc = Env.ZERO;
		BigDecimal amountCalc = Env.ZERO;
		String statutoryHolidayGenerationType = new String ();
   		statutoryHolidayGenerationType = this.getStatutoryHolidayGenerationType();
		//BigDecimal amount = Env.ZERO;
		ArrayList<BigDecimal> admissiblevalue = new ArrayList<BigDecimal>();
        
		// On calcule les dates de début et de fin afin de cibler les bonnes périodes
		// de calcul
		
		// J'identifie ma semaine
        nbrWeek = (new BigDecimal( TimeUtil.getDaysBetween(PeriodStartDate, HolidayDate) +1 ) .divide(new BigDecimal(7), 0, BigDecimal.ROUND_UP).intValue());
		if (   		statutoryHolidayGenerationType.equals(this.STATUTORYHOLIDAYGENERATIONTYPE_120thOfHours)  ||
				statutoryHolidayGenerationType.equals(this.STATUTORYHOLIDAYGENERATIONTYPE_120thOfHoursAndAmount))
		{
			nbrWeekBack = -4;
		}
		else if ( statutoryHolidayGenerationType.equals(this.STATUTORYHOLIDAYGENERATIONTYPE_160thOfHoursAndAmount))
		{
			nbrWeekBack = -12;
		}

		else if ( statutoryHolidayGenerationType.equals(STATUTORYHOLIDAYGENERATIONTYPE_110thOfHoursAndAmount) )
		{
			nbrWeekBack = -2;
		}
		else if ( statutoryHolidayGenerationType.equals(STATUTORYHOLIDAYGENERATIONTYPE_MoyenSur37Hours) )
		{
			nbrWeekBack = -1;
		}
		else
		{
			nbrWeekBack = -3;
		}
	    // Je calcule la date de début de ma semaine courante pour mon gain
        Timestamp NewStartDate = TimeUtil.addDays( PeriodStartDate, ((nbrWeek-1) * 7));
	    // Je calcule la date de début de ma semaine courante -3 ou -4 pour mon gain, pour reculer de 3 semaines
        Timestamp NewStartDateBack = TimeUtil.addDays( NewStartDate, nbrWeekBack * 7);
	    // Je calcule la date de fin de ma semaine précédente pour mon gain
        Timestamp NewEndDate = TimeUtil.addDays( NewStartDate, -1);
        
		String sql = "SELECT isnull( SUM( QuantityCalc ), 0) QuantityCalc, isnull( SUM( AmountCalc ), 0) AmountCalc "
		       + " FROM P_Payment_Gain "
			   + " INNER JOIN P_Gain_Column  ON P_Payment_Gain.P_Gain_Parameter_ID = P_Gain_Column.P_Gain_Parameter_ID "  
		       + " INNER JOIN P_Payment ON  P_Payment.P_Payment_ID =  P_Payment_Gain.P_Payment_ID "
		       + " WHERE P_Payment_Gain.P_Employee_ID = " + this.getP_Employee_ID()
               + " AND P_Column_Adm_ID = " + GainColumn_ID
               + " AND P_Payment.PAYMENTTYPEDOC = 'Regular' "
			   + " AND [Day] Between " + DB.TO_DATE( NewStartDateBack ) + " AND " + DB.TO_DATE( NewEndDate )
               + " AND IsAdmissible = 'Y' " ;

     	PreparedStatement pstmt = null;
    	pstmt = DB.prepareStatement(sql, TrxName);
    	try
    	{
        	//pstmt.setInt(1, this.getP_Employee_ID());
        	//pstmt.setInt(2, GainColumn_ID);
    	    
        	ResultSet rs = pstmt.executeQuery();
    	    while (rs.next())
        	{
    	    	quantityCalc = quantityCalc.add( rs.getBigDecimal("QuantityCalc"));
    	    	amountCalc = amountCalc.add( rs.getBigDecimal("AmountCalc"));
    	    	
    	    }
        	rs.close();
    	    pstmt.close();
        	pstmt = null;    		
    	}
		catch(Exception e)
		{
			s_log.log(Level.SEVERE,"getIndemnityStatutoryHolidays - sql error : "+sql);
		}
        

   		P_Assignment_Param AssignmentParam = P_Assignment_Param.get( Env.getCtx(),AssignmentParam_ID , TrxName );     
   		
		// Férié à nombre d'heures fixe on prends les heures définies sur l'affectation
   		
		if (statutoryHolidayGenerationType.equals(P_Employee.STATUTORYHOLIDAYGENERATIONTYPE_FixedNbrOfHours))
		{
			quantityCalc = AssignmentParam.getDay_Hours().setScale(4,BigDecimal.ROUND_HALF_UP);				
			amountCalc = new BigDecimal(0) ; //quantity.multiply( AssignmentParam.getHourly_Rate().multiply( GainMultiplyRate ) ).setScale(2,BigDecimal.ROUND_HALF_UP); 
		}
	    // Férié à 1/16e des heures
		if (statutoryHolidayGenerationType.equals(P_Employee.STATUTORYHOLIDAYGENERATIONTYPE_116thOfHours))
		{
			quantityCalc = quantityCalc.divide(new BigDecimal(16),2, BigDecimal.ROUND_HALF_UP);
			amountCalc = new BigDecimal(0) ; //quantity.multiply( AssignmentParam.getHourly_Rate().multiply( GainMultiplyRate ) ).setScale(2,BigDecimal.ROUND_HALF_UP); 
		}
	    // Férié à 1/15e des heures (Pour SAM seulement pour l'instant)
		if (statutoryHolidayGenerationType.equals(P_Employee.STATUTORYHOLIDAYGENERATIONTYPE_115thOfHours))
		{
			quantityCalc = quantityCalc.divide(new BigDecimal(15),2, BigDecimal.ROUND_HALF_UP);
			amountCalc = new BigDecimal(0) ; //quantity.multiply( AssignmentParam.getHourly_Rate().multiply( GainMultiplyRate ) ).setScale(2,BigDecimal.ROUND_HALF_UP);
			// Maximum 8 heures payées 
			if (quantityCalc.compareTo(new BigDecimal(8)) > 0)
			{
				quantityCalc = new BigDecimal(8);
			}
		}
		if (statutoryHolidayGenerationType.equals(P_Employee.STATUTORYHOLIDAYGENERATIONTYPE_116thOfHoursAndAmount))
		{
			quantityCalc = quantityCalc.divide(new BigDecimal(16),2, BigDecimal.ROUND_HALF_UP);
			amountCalc = amountCalc.divide(new BigDecimal(16),2, BigDecimal.ROUND_HALF_UP); 
		}
		if (statutoryHolidayGenerationType.equals(P_Employee.STATUTORYHOLIDAYGENERATIONTYPE_120thOfHours))
		{
			quantityCalc = quantityCalc.divide(new BigDecimal(20),2, BigDecimal.ROUND_HALF_UP);
			amountCalc = new BigDecimal(0) ; //quantity.multiply( AssignmentParam.getHourly_Rate().multiply( GainMultiplyRate ) ).setScale(2,BigDecimal.ROUND_HALF_UP); 
		}
		if (statutoryHolidayGenerationType.equals(P_Employee.STATUTORYHOLIDAYGENERATIONTYPE_120thOfHoursAndAmount))
		{
			quantityCalc= quantityCalc.divide(new BigDecimal(20),2, BigDecimal.ROUND_HALF_UP);
			amountCalc = amountCalc.divide(new BigDecimal(20),2, BigDecimal.ROUND_HALF_UP); 
		}
		if (statutoryHolidayGenerationType.equals(P_Employee.STATUTORYHOLIDAYGENERATIONTYPE_160thOfHoursAndAmount))
		{
			quantityCalc= quantityCalc.divide(new BigDecimal(60),2, BigDecimal.ROUND_HALF_UP);
			amountCalc = amountCalc.divide(new BigDecimal(60),2, BigDecimal.ROUND_HALF_UP); 
		}
		if (statutoryHolidayGenerationType.equals(P_Employee.STATUTORYHOLIDAYGENERATIONTYPE_NbrOfHoursAsDefinedInTheGenericSchedule))
		{
			// TODO
		}
		
		if ( statutoryHolidayGenerationType.equals(P_Employee.STATUTORYHOLIDAYGENERATIONTYPE_110thOfHoursAndAmount))
		{
			quantityCalc= quantityCalc.divide(new BigDecimal(10),2, BigDecimal.ROUND_HALF_UP);
			amountCalc = amountCalc.divide(new BigDecimal(10),2, BigDecimal.ROUND_HALF_UP); 
		}
		if ( statutoryHolidayGenerationType.equals(P_Employee.STATUTORYHOLIDAYGENERATIONTYPE_MoyenSur37Hours))
		{
//			quantityCalc= quantityCalc.divide(new BigDecimal(37),2, BigDecimal.ROUND_HALF_UP);
//			amountCalc = amountCalc.divide(new BigDecimal(37),2, BigDecimal.ROUND_HALF_UP);
			
			quantityCalc = this.getProRataHoliday();
			amountCalc = new BigDecimal(0);
		}
        
		quantity[0] = quantityCalc;
		quantity[1] = amountCalc;
		
		admissiblevalue.add(quantity[0]);
		admissiblevalue.add(quantity[1]);		
	
		return admissiblevalue;
		
	
	}

    /**
     * Procédure de copie. Lorsqu'on copie un code de regroupement, on doit également
     * copier tous les paramètres et le contenue des onglets sous le gain.
     */
    public void afterCopy(int originalId)
    {
    	
  /*  	MLocation cp = new MLocation(getCtx(), this.getC_Location_ID(), null);
    	
    	this.setC_Location_ID( 0 );
    	
		MLocation location = new MLocation(getCtx(), -1, null);
		location.setAD_Org_ID(0);
		location.setC_Country_ID(cp.getC_Country_ID());
		location.setC_Region_ID(cp.getC_Region_ID());
	*/	
/*		location.setCity(cp.getCity());
		location.setAddress1(cp.getAddress1());
		location.setAddress2(cp.getAddress2());
		location.setAddress3(cp.getAddress3());
		location.setAddress4(cp.getAddress4());
		location.setPostal(cp.getPostal() );
//		location.setPostal_Add(impEmp.getPostal_Add());
 * 
 */
    	/*
		if (location.save() )
	    	this.setC_Location_ID( location.getC_Location_ID() );
*/
    }

	public static P_Employee getWithValue( Properties ctx, String Value, String trxName  ) 
	{
		String sql = null;
		sql = "Select P_Employee.P_Employee_ID from P_Employee WHERE Value = " + DB.TO_STRING( Value );
		//
		
		P_Employee Employee = null;
		
		PreparedStatement pstmt = null;
		
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				Employee = P_Employee.get( ctx, rs.getInt( "P_Employee_ID"), trxName);
			}
			
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Employee - " + sql, e);
		}

		return Employee;
	}

    
    
}	//	P_Employee
