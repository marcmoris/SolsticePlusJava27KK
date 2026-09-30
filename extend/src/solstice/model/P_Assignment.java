
package solstice.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.sql.Timestamp;

import org.compiere.model.MRole;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import solstice.model.P_Assignment_SpecialRate;
import solstice.model.P_Assignment_Hst;
import solstice.model.P_Payment_Group;
import solstice.model.P_Workplace;
import solstice.process.PgiUtil;

/**
 *  Assignment Model
 *
 *  @author Marc Morissette - Progestion Informatique
 *  @version $Id: P_Assignment.java,v 1.5 2007/07/25 21:56:28 marmor01 Exp $
 */
public class P_Assignment extends X_P_Assignment
{
	/**
	 * 	Get Assignment
	 *	@param ctx context
	 * 	@param P_Assignment_ID id
	 *	@return Assignment
	 */
	public static P_Assignment get (Properties ctx, int P_Assignment_ID, String trxName)
	{
		Integer key = new Integer (P_Assignment_ID);
		P_Assignment Assignment = (P_Assignment)s_cache.get(key);
		if (Assignment != null)
			return Assignment;
		Assignment = new P_Assignment (ctx, P_Assignment_ID, trxName);
		s_cache.put (key, Assignment);
		return Assignment;
	}	//	get

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Assignment (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}

	 
	/**	Cache						*/
	private static CCache<Integer,P_Assignment>	s_cache = new CCache<Integer,P_Assignment>("P_Assignment", 20);

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Assignment.class);

	private boolean existsRWT = false;

	private	BigDecimal m_Annual_Increase = null;

	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Assignment_ID id
	 */
	public P_Assignment (Properties ctx, int P_Assignment_ID, String trxName)
	{
		super (ctx, P_Assignment_ID, trxName);
		if (P_Assignment_ID == 0)
		{
		//	setValue (null);
			setAD_Org_ID(0);
		}

		existsRWT = CheckRWT();
		m_Annual_Increase = P_Payment_Group.getAnnual_Increase( ctx, this.getP_Employee_ID(), trxName);
		
	}	//	P_Assignment

	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Assignment (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Assignment_ID"), trxName);
	}	//	P_Assignment

	
	public BigDecimal getWeekly_Hours_Base(Timestamp effectIn, String trxName )
	{
		P_Assignment_Param assignment_Param = P_Assignment_Param.get(Env.getCtx(), this.getP_Assignment_ID(), effectIn, trxName);
		if(assignment_Param == null || assignment_Param.getWeekly_Hours().compareTo( Env.ZERO) == 0)
		{
			s_log.log(Level.SEVERE,"getWeekly_Hours_Base - Invalid P_Assignment_Param " + this.toString());
			return null;
		}
		
		return assignment_Param.getWeekly_Hours();
	}

	
	/**
	 * 	Load all record - for Performace.
	 *	@param ctx context
	 */
	public static void loadAll (Properties ctx)
	{
		//
		s_cache = new CCache<Integer,P_Assignment>("P_Assignment", 250);
		P_Period Period = P_Period.getOpenPeriod(ctx, null);
		String sql = "SELECT P_Assignment_ID FROM P_Assignment WHERE IsActive='Y' and ( EndDate is null OR EndDate >= " + DB.TO_DATE(Period.getStartDate()) + " ) ";
		sql = MRole.getDefault().addAccessSQL (sql, "P_Assignment", true, false);	// fully qualidfied - RO 

		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				P_Assignment Assignment = new P_Assignment (ctx, rs.getInt("P_Assignment_ID"), null);
				s_cache.put( Assignment.getP_Assignment_ID(), Assignment);
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
	}	//	loadAll


	
	
	//
	// Pour des raisons de performance, on vérifier s'il exists un information RWT pour l'affectation. 
	// 
	//
	public boolean CheckRWT(  )
	{
		String sql = null;
		sql = "Select P_Assignment_RWT_ID FROM P_Assignment_RWT WHERE P_Assignment_RWT.IsActive='Y' ";
		sql += " AND P_Assignment_RWT.P_Assignment_ID=" + this.getP_Assignment_ID();
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
			return false;
		
		return true;
	}

	
	public P_Assignment_RWT GetAssignment_RWT( Timestamp EffectIn )
	{
		if ( ! existsRWT )
			return null;
		
		String sql = null;
		sql = "Select P_Assignment_RWT_ID FROM P_Assignment_RWT WHERE P_Assignment_RWT.IsActive='Y' ";
		sql += " AND P_Assignment_RWT.P_Assignment_ID=" + this.getP_Assignment_ID();
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

	//
	// Indique si l'affectation a une cédule artt ou non.
	//
	public boolean isRWT( Timestamp EffectIn )
	{
	    boolean isRWT = false;
	    
	    P_Assignment_RWT Assignment_RWT = GetAssignment_RWT( EffectIn );

	    if	( Assignment_RWT != null )
			isRWT = true;
		
		return isRWT;
	}
	
	// 
	
	public int dayRWT( Timestamp EffectIn )
	{
	    P_Assignment_RWT Assignment_RWT = GetAssignment_RWT( EffectIn );

	    if	( Assignment_RWT != null )
			return Integer.parseInt( Assignment_RWT.getWeekDay()) +1;

	    return 0;
	}

	public BigDecimal getDay_HoursRWT( Timestamp EffectIn )
	{
	    P_Assignment_RWT Assignment_RWT = GetAssignment_RWT( EffectIn );

	    if	( Assignment_RWT != null )
			return Assignment_RWT.getDay_Hours();
	    
	    return null;
	}

	public BigDecimal getWeekly_HoursRWT( Timestamp EffectIn )
	{
	    P_Assignment_RWT Assignment_RWT = GetAssignment_RWT( EffectIn );

	    if	( Assignment_RWT != null )
			return Assignment_RWT.getWeekly_Hours();
	    
	    return null;
	}

	
	//
	// return le nombre d'heure travailler par semaine, en tenant compte des cédules ARTT.
	public BigDecimal getWeekly_Hours( Timestamp EffectIn, String trxName )
	{
	
		BigDecimal  Weekly_Hours = getWeekly_Hours_NoDate(EffectIn, trxName);

		String sql = null;
		sql = "Select * FROM P_Assignment_RWT WHERE P_Assignment_RWT.IsActive='Y' ";
		sql += " AND P_Assignment_RWT.P_Assignment_ID=" + this.getP_Assignment_ID();
		sql += " AND " + DB.TO_DATE( EffectIn) + " BETWEEN startdate AND isnull( enddate, " + DB.TO_DATE( EffectIn) + ") "
		;
		//
		
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			int index = 1;
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				Weekly_Hours = rs.getBigDecimal( "Weekly_Hours");
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"getWeekly_Hours - " + sql, e);
		}
		
		return Weekly_Hours;
	}

	//
	// return le nombre d'heure travailler par semaine, en tenant compte des cédules ARTT.
	public BigDecimal getWeekly_Hours_Cumulated( Timestamp effectIn, String trxName )
	{
	
		P_Assignment_Param assignment_Param = P_Assignment_Param.get(Env.getCtx(), this.getP_Assignment_ID(), effectIn, trxName);
		if(assignment_Param == null)
		{
			s_log.log(Level.SEVERE,"getWeekly_Hours_Cumulated - Invalid P_Assignment_Param");
			return null;
		}
		

		BigDecimal  Weekly_Hours = assignment_Param.getWeekly_Hours();
		
		if ( assignment_Param.getCumulatedHours() != null && assignment_Param.getCumulatedHours().compareTo(Env.ZERO) != 0)
		{
			Weekly_Hours = assignment_Param.getCumulatedHours().multiply( new BigDecimal(5));
		}

		
		String sql = null;
		sql = "Select * FROM P_Assignment_RWT WHERE P_Assignment_RWT.IsActive='Y' ";
		sql += " AND P_Assignment_RWT.P_Assignment_ID=" + this.getP_Assignment_ID();
		sql += " AND " + DB.TO_DATE( effectIn) + " BETWEEN startdate AND isnull( enddate, " + DB.TO_DATE( effectIn) + ") "
		;
		//
		
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				Weekly_Hours = rs.getBigDecimal( "Weekly_Hours");
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"getWeekly_Hours - " + sql, e);
		}
		
		return Weekly_Hours;
	}

	
	//
	// return le nombre d'heure travailler par semaine, en tenant compte des cédules ARTT.
	public BigDecimal getWeekly_Hours_NoDate(Timestamp effectIn, String trxName )
	{
	
		BigDecimal  Weekly_Hours = getWeekly_Hours_Base(effectIn, trxName);

		String sql = null;
		sql = "Select * FROM P_Assignment_RWT WHERE P_Assignment_RWT.IsActive='Y' ";
		sql += " AND P_Assignment_RWT.P_Assignment_ID=" + this.getP_Assignment_ID();
		sql += " AND getdate() BETWEEN startdate AND isnull( enddate, getdate()) "
		;
		//
		
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);

			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				Weekly_Hours = rs.getBigDecimal( "Weekly_Hours");
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"getWeekly_Hours - " + sql, e);
		}
		
		return Weekly_Hours;
	}
	
	
	//
	// return le salaire annuel en tetant compte des ajustements et des cédules ARTT.
	//
	public BigDecimal getAnnual_Salary(Timestamp effectIn, String trxName)
	{
		P_Assignment_Param assignment_Param = P_Assignment_Param.get(Env.getCtx(), this.getP_Assignment_ID(), effectIn, trxName);
		if(assignment_Param == null)
		{
			s_log.log(Level.SEVERE,"setAnnual_Salary - Invalid P_Assignment_Param");
			return null;
		}

   		if ( assignment_Param.isOut_Of_Rate() == true  && assignment_Param.getRemuneration_Method().equals(P_Assignment_Param.REMUNERATION_METHOD_Hourly))
 			return assignment_Param.getOut_Of_Rate_Hourly_Rate().multiply( getWeekly_Hours(effectIn, trxName) ).multiply( m_Annual_Increase ).setScale(2, BigDecimal.ROUND_HALF_UP);
//			return assignment_Param.getOut_Of_Rate_Salary().multiply( getWeekly_Hours(effectIn, trxName) ).multiply( m_Annual_Increase ).setScale(2, BigDecimal.ROUND_HALF_UP);
//			return assignment_Param.getOut_Of_Rate_Salary().multiply( getWeekly_Hours(effectIn, trxName) ).multiply( new BigDecimal( 52.18)).setScale(2, BigDecimal.ROUND_HALF_UP);
   		else if ( assignment_Param.isOut_Of_Rate() == true )
   			return assignment_Param.getOut_Of_Rate_Salary();
   		else
   			return  assignment_Param.getAnnual_Salary();
	}
	
	//
	// return le taux horaire en tetant compte des ajustements et des cédules ARTT.
	//
	public BigDecimal getHourly_Rate(Timestamp effectIn, String trxName) 
	{
  		//TODO
   		//annualite
		P_Assignment_Param assignment_Param = P_Assignment_Param.get(Env.getCtx(), this.getP_Assignment_ID(), effectIn, trxName);
		if(assignment_Param == null)
		{
			s_log.log(Level.SEVERE,"getHourly_Rate - Invalid P_Assignment_Param");
			return null;
		}

   		
   		if ( assignment_Param.isOut_Of_Rate() == true  && assignment_Param.getRemuneration_Method().equals(P_Assignment_Param.REMUNERATION_METHOD_Hourly))
   			return assignment_Param.getOut_Of_Rate_Salary();
   		else if ( assignment_Param.isOut_Of_Rate() == true )
   			return assignment_Param.getOut_Of_Rate_Salary().divide( m_Annual_Increase, 4, BigDecimal.ROUND_HALF_UP ).divide( getWeekly_Hours( effectIn, trxName),4, BigDecimal.ROUND_HALF_UP );
//   			return assignment_Param.getOut_Of_Rate_Salary().divide( new BigDecimal(52.18 ), 4, BigDecimal.ROUND_HALF_UP ).divide( getWeekly_Hours( effectIn, trxName),4, BigDecimal.ROUND_HALF_UP );
   		else
   			return  assignment_Param.getHourly_Rate();
	}

	public BigDecimal getSpecialRate( Properties ctx, int P_SpecialRate_ID, Timestamp effectIn, String trxName )
	{
		P_Assignment_SpecialRate Assignment_SpecialRate = P_Assignment_SpecialRate.get( ctx, this.getP_Assignment_ID(), P_SpecialRate_ID, effectIn, trxName );
		
		if ( Assignment_SpecialRate != null && Assignment_SpecialRate.getWeekly_Hours().compareTo( Env.ZERO) != 0 )
			return Assignment_SpecialRate.getWeekly_Amount().divide( Assignment_SpecialRate.getWeekly_Hours(), 4, BigDecimal.ROUND_HALF_UP);
		else if ( Assignment_SpecialRate != null  )
			return Env.ZERO;
		else
			return null;
	}
	
	/**
	 * 	Before Delete.
	 *	@return true 
	 */
	protected boolean beforeDelete ()
	{
		
		// audit trail 
		if ( PgiUtil.getSolsticeParameter(Env.getCtx(), "AuditTrailInsurance").equals("True")  )
			P_Audit_Trail_Insurance.setInsurableSalary( this.getP_Employee_ID() );
		
		return true;
	}	//	beforeDelete

	/**
	 * 	After Delete
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterDelete (boolean success)
	{
		// audit trail 
		if (success)
			if ( PgiUtil.getSolsticeParameter(Env.getCtx(), "AuditTrailInsurance").equals("True")  )
				P_Audit_Trail_Insurance.InsurableSalary_Audit( this.getP_Employee_ID() );
			
		return success;
	}	//	afterDelete
	

	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true or false
	 */

	protected boolean beforeSave (boolean newRecord)
	{
		// audit trail
		if ( PgiUtil.getSolsticeParameter(Env.getCtx(), "AuditTrailInsurance").equals("True")  )
			P_Audit_Trail_Insurance.setInsurableSalary( this.getP_Employee_ID() );

		//+ 2011.07.05 + sécurité par compagnie.
		P_Employee Employee = P_Employee.get(getCtx(), getP_Employee_ID(), this.get_TrxName());
		this.setAD_Org_ID( Employee.getAD_Org_ID());
		//- 2011.07.05 + sécurité par compagnie.

		//	Affectation, validation qu'il n'y a pas 2 affectation principal en même temps.
		int Assignment_ID = 0;

		if ( this.getAssignmentType().equals( P_Assignment.ASSIGNMENTTYPE_Primary))
		{

			String sql = "Select P_Assignment_ID From P_Assignment " 
					   + " Where AssignmentType = 'P' " 
					   + " AND P_Employee_ID=" + this.getP_Employee_ID()
					   + " AND P_Assignment_ID <> " + this.getP_Assignment_ID();

			PreparedStatement pstmt = null;

			try
			{
				pstmt = DB.prepareStatement (sql, null);
				ResultSet rs = pstmt.executeQuery ();
				if ( rs.next () )
					Assignment_ID = rs.getInt(1);
				rs.close ();
				pstmt.close ();
				pstmt = null;
			}
			catch (Exception e)
			{
				s_log.saveError("ValidationError",Msg.translate(getCtx(), "DuplicateAssignmentPrimary"));
//				s_log.log(Level.SEVERE,"ValidationError", Msg.translate(getCtx(), "DuplicateAssignmentPrimary"));
			}

			if ( Assignment_ID != 0 )
			{
				s_log.saveError("ValidationError",Msg.translate(getCtx(), "DuplicateAssignmentPrimary"));
//				s_log.log(Level.SEVERE,"ValidationError", Msg.translate(getCtx(), "DuplicateAssignmentPrimary"));
				return false;
			}
				
		}

		if ( this.getP_Post_ID() != 0 && (this.getEquityCodeCNP() == null || this.getEquityCodeCNP().equals("" ) ) )
		{
			P_Post Post = P_Post.get(Env.getCtx(), this.getP_Post_ID(), null);
			this.setEquityCodeCNP( Post.getEquityCodeCNP() );
		}
			
		
		
		return true;
	}	//	beforeSave
	

	/**
	 * 	After Save
	 *	@param newRecord
	 *	@param success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
	    if (!success )
		{
			return success;
		}

		// audit trail 
		if ( PgiUtil.getSolsticeParameter(Env.getCtx(), "AuditTrailInsurance").equals("True")  )
			P_Audit_Trail_Insurance.InsurableSalary_Audit( this.getP_Employee_ID(), this.getStartDate(), this.getEndDate() );

		
	    // on garde une historique des affectations principal 
	    // pour des rapports comme vérification des écarts salarailes
	    if ( this.getAssignmentType().equals( P_Assignment.ASSIGNMENTTYPE_Primary ))
	    {
	    	boolean HistoExist = P_Assignment_Hst.getHisto( this);
	    	if ( ! HistoExist )
	    	{
			    P_Assignment_Hst Histo = new P_Assignment_Hst( Env.getCtx(), -1, this.get_TrxName());
			    Histo.setAssignmentType( this.getAssignmentType());
			    Histo.setIsActive( this.isActive());
			    Histo.setP_Assignment_ID( this.getP_Assignment_ID());
			    Histo.setP_Assignment_State_ID( this.getP_Assignment_State_ID());
			    Histo.setP_Employee_ID( this.getP_Employee_ID());
		    	Histo.save();
	    	}
	    	
	    }
/*
	    P_Employee employee = P_Employee.get( Env.getCtx(), this.getP_Employee_ID(), this.get_TrxName());
	    
	    if ( newRecord && employee.getC_Activity_ID() != 0 || (is_ValueChanged("P_Workplace_ID")) )
	    {
	    	P_Post post = P_Post.get( Env.getCtx(), this.getP_Post_ID(), this.get_TrxName());
//	    	P_Workplace workplace = P_Workplace.get( Env.getCtx(), employee.getP_Workplace_ID(),  this.get_TrxName());
	    	P_Workplace workplace = P_Workplace.get( Env.getCtx(), post.getP_Workplace_ID(),  this.get_TrxName());

	    	
	    	P_Assignment_Distribution distribution = null;
	    	distribution = P_Assignment_Distribution.getWithAssignmentID( Env.getCtx(), this.getP_Assignment_ID(), this.get_TrxName());
	    	if ( distribution == null )
	    		distribution = new P_Assignment_Distribution( Env.getCtx(), -1, this.get_TrxName());
	    	distribution.setP_Assignment_ID( this.getP_Assignment_ID());
	    	distribution.setC_Activity_ID( employee.getC_Activity_ID() );
	    	distribution.setOrg_ID( employee.getAD_Org_ID());
	    	distribution.setC_SalesRegion_ID( workplace.getC_SalesRegion_ID() );
	    	distribution.setIsActive(true);
	    	distribution.setLine( Env.ONE);
	    	distribution.setRatio(new BigDecimal( 100 ) );
	    	distribution.save( );	    	
	    }
*/

	    
		return success;
	}	//	afterSave
	
	
	public static void UpdateIndexation(int assignmentID, int newSalaryScaleID, int newSalaryScaleDetailID, Timestamp newEffectIn, String trxName)
	{
		P_Assignment_Param assignment_Param = P_Assignment_Param.get(Env.getCtx(), assignmentID, newEffectIn, trxName);
		if(assignment_Param == null)
		{
			s_log.log(Level.SEVERE,"setAnnual_Salary - Invalid P_Assignment_Param");
			return;
		}
		
		assignment_Param.setP_Salary_Scale_ID(newSalaryScaleID);
		assignment_Param.setP_Salary_Scale_Detail_ID(newSalaryScaleDetailID);
		assignment_Param.setEffectIn(newEffectIn);
		assignment_Param.save();
	}	// 	UpdateIndexation

	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Assignment[ID=")
			.append(this.getP_Assignment_ID())
			.append(",Value=").append(getValue())
			.append(",Name=").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString

	
	
	
	

	
}	//	P_Assignment
