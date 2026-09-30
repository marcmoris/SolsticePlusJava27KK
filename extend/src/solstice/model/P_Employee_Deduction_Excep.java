package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import java.math.BigDecimal;
/**
*  Employee Model
*
*  @author Marc Morissette
*  @version $Id: P_Employee_Deduction_Excep.java,v 1.1 2007/07/18 14:43:34 marmor01 Exp $
*/
public class P_Employee_Deduction_Excep extends X_P_Employee_Deduction_Excep
{
	/**
	 * 	Get Employee
	 *	@param ctx context
	 * 	@param P_Employee_Deduction_Excep_ID id
	 *	@return Employee
	 */
	public static P_Employee_Deduction_Excep get (Properties ctx, int P_Employee_Deduction_Excep_ID, String trxName)
	{
		Integer key = new Integer (P_Employee_Deduction_Excep_ID);
		P_Employee_Deduction_Excep Employee = (P_Employee_Deduction_Excep)s_cache.get(key);
		if (Employee != null)
			return Employee;
		Employee = new P_Employee_Deduction_Excep (ctx, P_Employee_Deduction_Excep_ID, trxName);
		s_cache.put (key, Employee);
		return Employee;
	}	//	get


	private BigDecimal ExceptionAmountEmployee = Env.ZERO;
	private BigDecimal ExceptionAmountEmployer = Env.ZERO;
	

	/**	Cache						*/
	private static CCache<Integer,P_Employee_Deduction_Excep>	s_cache = new CCache<Integer,P_Employee_Deduction_Excep>("P_Employee_Deduction_Excep", 20);

	
	/**
	 * 	Default Constructor
	 * 	@param ctx context
	 * 	@param rs ResultSet to load from
	 */
	public P_Employee_Deduction_Excep (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	P_Employee_Deduction_Excep

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Employee_Deduction_Excep_ID id
	 */
	public P_Employee_Deduction_Excep (Properties ctx, int P_Employee_Deduction_Excep_ID, String trxName)
	{
		super (ctx, P_Employee_Deduction_Excep_ID, trxName);
		if (P_Employee_Deduction_Excep_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Employee_Deduction_Excep


	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Employee_Deduction_Excep (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Employee_Deduction_Excep_ID"), trxName);
	}	//	P_Employee_Deduction_Excep


	public static P_Employee_Deduction_Excep get( Properties ctx, int P_Employee_Deduction_ID, int P_Period_ID , String trxName)
	{
		BigDecimal ExceptionAmountEmployee = null;
		BigDecimal ExceptionAmountEmployer = null; 

		int P_Employee_Deduction_Excep_ID = 0;
		String sql = "SELECT min(P_Employee_Deduction_Excep_ID) P_Employee_Deduction_Excep_ID, isnull(sum( ExceptionAmountEmployee ),0) ExceptionAmountEmployee, isnull(sum( ExceptionAmountEmployer ),0) ExceptionAmountEmployer FROM P_Employee_Deduction_Excep WHERE P_Employee_Deduction_ID = ? and P_Period_ID = ?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, P_Employee_Deduction_ID);
			pstmt.setInt (2, P_Period_ID);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next ())
			{
				P_Employee_Deduction_Excep_ID = rs.getInt( 1 );
				ExceptionAmountEmployee = rs.getBigDecimal( "ExceptionAmountEmployee" );
				ExceptionAmountEmployer = rs.getBigDecimal( "ExceptionAmountEmployer" );
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		} catch (Exception e)
		{
//			log.error ("getLines", e);
			return null;
		}
		if ( P_Employee_Deduction_Excep_ID != 0)
		{
			P_Employee_Deduction_Excep EmployeeDeductionExcep = P_Employee_Deduction_Excep.get( ctx, P_Employee_Deduction_Excep_ID , trxName);
			EmployeeDeductionExcep.setExceptionAmountEmployee( ExceptionAmountEmployee );
			EmployeeDeductionExcep.setExceptionAmountEmployer( ExceptionAmountEmployer );
			return EmployeeDeductionExcep;
			
		}
		else
			return null;
	}

	public BigDecimal getExceptionAmountEmployee()
	{
		return ExceptionAmountEmployee;
	}

	public BigDecimal getExceptionAmountEmployer( )
	{
		return ExceptionAmountEmployer;
	}

	public void setExceptionAmountEmployee( BigDecimal amount)
	{
		ExceptionAmountEmployee = amount; 
		super.setExceptionAmountEmployee( amount);
	}

	public void setExceptionAmountEmployer( BigDecimal amount )
	{
		ExceptionAmountEmployer = amount; 
		super.setExceptionAmountEmployer( amount);
	}

	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Employee_Deduction_Excep[ID=")
			.append(this.getP_Employee_Deduction_Excep_ID())
//			.append(",Value=").append(getValue())
//			.append(",Name=").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true or false
	 */

	protected boolean beforeSave (boolean newRecord)
	{
		//+2012.05.15 stop save procedure if the period is closed
        P_Period Period = P_Period.get( getCtx(), this.getP_Period_ID(), this.get_TrxName());
    /*    if (Period.getPeriodStatus().equals(P_Period.PERIODSTATUS_Closed) || Period.getPeriodStatus().equals(P_Period.PERIODSTATUS_PermanentlyClosed) )
        {
			log.saveError("ValidationError",Msg.translate(getCtx(), "PeriodClosed"));
    		return false;

        }
*/

		if ( this.getP_Employee_Deduction_ID() == 0 && this.getP_Employee_ID() != 0 )
		{
//			P_Period Period = P_Period.get( getCtx(), this.getP_Period_ID(), this.get_TrxName()) ;
			P_Employee_Deduction EmployeeDeduction = P_Employee_Deduction.get( getCtx(),this.getP_Employee_ID(), this.getP_Deduction_ID(), Period.getStartDate() ,this.get_TrxName());
			if ( EmployeeDeduction.getP_Employee_Deduction_ID() <= 0 )
			{
				EmployeeDeduction = new P_Employee_Deduction( Env.getCtx(), -1, this.get_TrxName());
				EmployeeDeduction.setP_Deduction_ID( this.getP_Deduction_ID());
				EmployeeDeduction.setIsActive(true);
				EmployeeDeduction.setP_Employee_ID(this.getP_Employee_ID());
				EmployeeDeduction.setEffectIn(Period.getStartDate());
				EmployeeDeduction.save();
			}
			// Temporaire 
			else 
			{
		    	P_Deduction Deduction = P_Deduction.get( Env.getCtx(), this.getP_Deduction_ID(), this.get_TrxName());
		    	if ( Deduction.getP_Taxable_Benefit_ID() != 0 && this.isActive()  )
		    	{
			    	P_Employee_Taxable_Benefit EmployeeTaxableBenefit = P_Employee_Taxable_Benefit.get( Env.getCtx(), this.getP_Employee_ID(), Deduction.getP_Taxable_Benefit_ID(), this.get_TrxName());
			    	if ( EmployeeTaxableBenefit.getP_Employee_Taxable_Benefit_ID() <= 0)
			    	{
				    	EmployeeTaxableBenefit.setAD_Org_ID(0);
				    	EmployeeTaxableBenefit.setP_Employee_ID(this.getP_Employee_ID());
				    	EmployeeTaxableBenefit.setP_Taxable_Benefit_ID( Deduction.getP_Taxable_Benefit_ID());
				    	if ( EmployeeTaxableBenefit.getEffectIn() == null )
				    		EmployeeTaxableBenefit.setEffectIn( Period.getStartDate());
				    	EmployeeTaxableBenefit.setIsActive( this.isActive());
				    	EmployeeTaxableBenefit.save();
			    		
			    	}
		    	}

			}
			this.setP_Employee_Deduction_ID( EmployeeDeduction.getP_Employee_Deduction_ID() );
		}
			
		return true;
	}

}	//	P_Employee_Deduction_Excep
