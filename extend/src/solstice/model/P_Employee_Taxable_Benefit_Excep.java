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
package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
/**
*  Employee Model
*
*  @author Marc Morissette
*  @version $Id: P_Employee_Taxable_Benefit_Excep.java,v 1.2 2007/12/06 15:00:11 marmor01 Exp $
*/
public class P_Employee_Taxable_Benefit_Excep extends X_P_Employee_Taxable_Benefit_Excep
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Employee
	 *	@param ctx context
	 * 	@param P_Employee_Taxable_Benefit_Excep_ID id
	 *	@return Employee
	 */
	public static P_Employee_Taxable_Benefit_Excep get (Properties ctx, int P_Employee_Taxable_Benefit_Excep_ID, String trxName)
	{
		Integer key = new Integer (P_Employee_Taxable_Benefit_Excep_ID);
		P_Employee_Taxable_Benefit_Excep Employee = (P_Employee_Taxable_Benefit_Excep)s_cache.get(key);
		if (Employee != null)
			return Employee;
		Employee = new P_Employee_Taxable_Benefit_Excep (ctx, P_Employee_Taxable_Benefit_Excep_ID, trxName);
		s_cache.put (key, Employee);
		return Employee;
	}	//	get


	/**	Cache						*/
	private static CCache<Integer,P_Employee_Taxable_Benefit_Excep>	s_cache = new CCache<Integer,P_Employee_Taxable_Benefit_Excep>("P_Employee_Taxable_Benefit_Excep", 20);

	
	/**
	 * 	Default Constructor
	 * 	@param ctx context
	 * 	@param rs ResultSet to load from
	 */
	public P_Employee_Taxable_Benefit_Excep (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	P_Employee_Taxable_Benefit_Excep

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Employee_Taxable_Benefit_Excep_ID id
	 */
	public P_Employee_Taxable_Benefit_Excep (Properties ctx, int P_Employee_Taxable_Benefit_Excep_ID, String trxName)
	{
		super (ctx, P_Employee_Taxable_Benefit_Excep_ID, trxName);
		if (P_Employee_Taxable_Benefit_Excep_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Employee_Taxable_Benefit_Excep


	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Employee_Taxable_Benefit_Excep (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Employee_Taxable_Benefit_Excep_ID"), trxName);
	}	//	P_Employee_Taxable_Benefit_Excep


	public static P_Employee_Taxable_Benefit_Excep get( Properties ctx, int P_Employee_Taxable_Benefit_ID, int P_Period_ID , String trxName)
	{
		int P_Employee_Taxable_Benefit_Excep_ID = 0;
		String sql = "SELECT P_Employee_Taxable_Benefit_Excep_ID FROM P_Employee_Taxable_Benefit_Excep WHERE P_Employee_Taxable_Benefit_ID = ? and P_Period_ID = ?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, P_Employee_Taxable_Benefit_ID);
			pstmt.setInt (2, P_Period_ID);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next ())
			{
				P_Employee_Taxable_Benefit_Excep_ID = rs.getInt( 1 );
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		} catch (Exception e)
		{
			return null;
		}
		if ( P_Employee_Taxable_Benefit_Excep_ID != 0)
		{
			P_Employee_Taxable_Benefit_Excep EmployeeTaxable_BenefitExcep = P_Employee_Taxable_Benefit_Excep.get( ctx, P_Employee_Taxable_Benefit_Excep_ID , trxName);
			return EmployeeTaxable_BenefitExcep;
		}
		else
			return null;
	}


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Employee_Taxable_Benefit_Excep[ID=")
			.append(this.getP_Employee_Taxable_Benefit_Excep_ID())
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
        if (Period.getPeriodStatus().equals(P_Period.PERIODSTATUS_Closed) || Period.getPeriodStatus().equals(P_Period.PERIODSTATUS_PermanentlyClosed) )
        {
//			log.saveError("ValidationError",Msg.translate(getCtx(), "PeriodClosed"));
//    		return false;
        }
        
		if ( this.getP_Employee_Taxable_Benefit_ID() == 0  )  // && this.getP_Employee_ID() != 0
		{
//			P_Period Period = P_Period.get( getCtx(), this.getP_Period_ID(), this.get_TrxName()) ;
			P_Employee_Taxable_Benefit EmployeeTaxable_Benefit = P_Employee_Taxable_Benefit.get( getCtx(),this.getP_Employee_Taxable_Benefit_ID() ,this.get_TrxName());
			if ( EmployeeTaxable_Benefit.getP_Employee_Taxable_Benefit_ID() <= 0 )
			{
				EmployeeTaxable_Benefit = new P_Employee_Taxable_Benefit( Env.getCtx(), -1, this.get_TrxName());
//				EmployeeTaxable_Benefit.setP_Taxable_Benefit_ID( this.getP_Taxable_Benefit_ID());
				EmployeeTaxable_Benefit.setIsActive(true);
//				EmployeeTaxable_Benefit.setP_Employee_ID(this.getP_Employee_ID());
				EmployeeTaxable_Benefit.setEffectIn(Period.getStartDate());
				EmployeeTaxable_Benefit.save();
			}
			// Temporaire 
			else 
			{
				P_Employee_Taxable_Benefit EmployeeTaxableBenefit = P_Employee_Taxable_Benefit.get( getCtx(),this.getP_Employee_Taxable_Benefit_ID() ,this.get_TrxName());
		    	P_Taxable_Benefit Taxable_Benefit = P_Taxable_Benefit.get( Env.getCtx(), EmployeeTaxableBenefit.getP_Taxable_Benefit_ID(), this.get_TrxName());
		    	if ( Taxable_Benefit.getP_Taxable_Benefit_ID() != 0 && this.isActive()  )
		    	{
//			    	P_Employee_Taxable_Benefit EmployeeTaxableBenefit = P_Employee_Taxable_Benefit.get( Env.getCtx(), this.getP_Employee_ID(), Taxable_Benefit.getP_Taxable_Benefit_ID(), this.get_TrxName());
			    	if ( EmployeeTaxableBenefit.getP_Employee_Taxable_Benefit_ID() <= 0)
			    	{
				    	EmployeeTaxableBenefit.setAD_Org_ID(0);
//				    	EmployeeTaxableBenefit.setP_Employee_ID(this.getP_Employee_ID());
				    	EmployeeTaxableBenefit.setP_Taxable_Benefit_ID( Taxable_Benefit.getP_Taxable_Benefit_ID());
				    	if ( EmployeeTaxableBenefit.getEffectIn() == null )
				    		EmployeeTaxableBenefit.setEffectIn( Period.getStartDate());
				    	EmployeeTaxableBenefit.setIsActive( this.isActive());
				    	EmployeeTaxableBenefit.save();
			    		
			    	}
		    	}

			}

			this.setP_Employee_Taxable_Benefit_ID( EmployeeTaxable_Benefit.getP_Employee_Taxable_Benefit_ID() );
		}
			
		return true;
	}


}	//	P_Employee_Taxable_Benefit_Excep
