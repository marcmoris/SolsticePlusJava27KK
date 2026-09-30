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
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

/**
 *  Employee_Credits Model
 *
 *  @author Marc Morissette
 *  @version $Id: P_Employee_Credits.java,v 1.4 2007/09/19 21:40:14 marmor01 Exp $
 */
public class P_Employee_Credits extends X_P_Employee_Credits
{

	/**
	 * 	Get Employee_Credits
	 *	@param ctx context
	 * 	@param P_Employee_Credits_ID id
	 *	@return Employee_Credits
	 */
	public static P_Employee_Credits get (Properties ctx, int P_Employee_Credits_ID, String trxName)
	{
		Integer key = new Integer (P_Employee_Credits_ID);
		P_Employee_Credits employee_Credits = (P_Employee_Credits)s_cache.get(key);
		if (employee_Credits != null)
			return employee_Credits;
		employee_Credits = new P_Employee_Credits (ctx, P_Employee_Credits_ID, trxName);
		s_cache.put (key, employee_Credits);
		if(employee_Credits != null)
		{
			employee_Credits.setTrxName(trxName);
		}
		return employee_Credits;
	}	//	get


	
	public static P_Employee_Credits get (Properties ctx, int EmployeeID, int CreditsID, Timestamp EffectIn, String trxName)
	{
	    String sql = "SELECT  P_Employee_Credits_ID FROM P_Employee_Credits "
            + " WHERE P_Employee_ID = " + EmployeeID
            + "   AND P_Credits_ID  = " + CreditsID
			+ "    and EffectIn<= " + DB.TO_DATE( EffectIn )  
			+ "  Order By EffectIn Desc";
	    
	    PreparedStatement pstmt = null;
    
	    int P_Employee_Credits_ID = 0;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
			    P_Employee_Credits_ID = rs.getInt(1);
			}
			    
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			 
		}
		catch (Exception e)
		{
		    System.out.println( "P_Employee_Credits, get - " + e);
		    return null;
		}
	    
		if ( P_Employee_Credits_ID == 0 )
		   return null;
		
		P_Employee_Credits Employee_Credits = P_Employee_Credits.get( ctx, P_Employee_Credits_ID, trxName );
		return Employee_Credits;
	}	//	get


	
	/**	Cache						*/
	private static CCache<Integer,P_Employee_Credits>	s_cache = new CCache<Integer,P_Employee_Credits>("P_Employee_Credits", 20);

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Employee_LongTermLeave.class);

	private String				m_trxName = null;
	
//	private static P_Credits Credits;
//	private static X_P_Credits_Param CreditsParam = null;
	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Employee_Credits_ID id
	 */
	public P_Employee_Credits (Properties ctx, int P_Employee_Credits_ID, String trxName )
	{
		super (ctx, P_Employee_Credits_ID, trxName);

		if (P_Employee_Credits_ID == 0)
		{
			setAD_Org_ID(0);
		}
		m_trxName = trxName;
	}	//	P_Employee_Credits

	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Employee_Credits (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
		m_trxName = trxName;
	}
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Employee_Credits (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Employee_Credits_ID"), trxName);
		m_trxName = trxName;
	}	//	P_Employee_Credits

	
	
	
	/*
	 * Return le solde de la banque d'un employé.
	 */
	public static BigDecimal getEmployeeSolde( int P_Credits_ID , int P_Employee_ID, Timestamp d, String trxName ) 
	{
		return P_Credits.getEmployeeSolde(P_Credits_ID, P_Employee_ID, d, trxName);
	}

	//2009.05.07
	public BigDecimal getEmployeeSoldeConvert( P_Credits Credits, P_UOM Uom, Timestamp d)
	{
		BigDecimal sld = P_Credits.getEmployeeSoldeTimeSheet(this.getP_Credits_ID(), this.getP_Employee_ID(), d, this.get_TrxName());
		
		// EMS
		if ( this.getP_Method_Credits_ID() == 1000003)
			return sld;

		
		if ( Credits.getDisplayUOM() != null && Credits.getDisplayUOM().equals( P_Credits.DISPLAYUOM_Days))
		{
			P_Employee Employee = P_Employee.get( Env.getCtx(), this.getP_Employee_ID(), this.get_TrxName());
			P_Assignment_Param AssignmentParam = Employee.getAssignment_Param(d, this.get_TrxName());
			if ( AssignmentParam.getDay_Hours().compareTo(Env.ZERO) == 0)
				sld = null;
			else
				sld = sld.divide( AssignmentParam.getDay_Hours());
			
		}

		/*		
		if ( Credits.getDisplayUOM().equals( P_Credits.DISPLAYUOM_YearDays))
		{
			BigDecimal nbrYear = sld.divide( new BigDecimal( 365), 0, BigDecimal.ROUND_DOWN);
			BigDecimal nbrDay  = sld.subtract( (nbrYear.multiply( new BigDecimal( 365) )) ).setScale(0);  

			BigDecimal sTemp =   cast(@nbrYear as varchar(10)) + '.' + dbo.lpad( cast( @nbrDay as varchar(10)), 3, '0' )

			sld = BigDecimal.valueOf( Double.valueOf( nbrYear.toString() + "." + nbrDay.toString() ));
		}
*/			
		return sld;
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
		//+ 2011.07.05 + sécurité par compagnie.
 	    P_Employee Employee = P_Employee.get( Env.getCtx(), this.getP_Employee_ID(), this.get_TrxName());
		this.setAD_Org_ID( Employee.getAD_Org_ID());
		//- 2011.07.05 + sécurité par compagnie.

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
    
	    if (newRecord)
		{
		    //P_Employee Employee = P_Employee.get( Env.getCtx(), getP_Employee_ID(), this.get_TrxName());
		    Timestamp CreditsDate = this.getEffectIn();
		    
		    P_Credits_Param CreditsParam = P_Credits_Param.get( Env.getCtx(), getP_Credits_ID(), this.getP_Method_Credits_ID(), CreditsDate, this.get_TrxName() );

		    if ( CreditsParam == null )
		        return true;
		    
		    if ( CreditsParam.getCreditsInitial() != null  && CreditsParam.getCreditsInitial().compareTo( Env.ZERO) != 0 )
		    {
				P_Credits_Movement CreditsMovement = P_Credits_Movement.get(Env.getCtx (), 0, CreditsParam.getP_Credits_Target(), CreditsDate, "BO", "" , this.get_TrxName()) ;
				CreditsMovement.setP_Credits_ID( getP_Credits_ID() );
				CreditsMovement.setP_Employee_ID( getP_Employee_ID());
				CreditsMovement.setPMovementDate( CreditsDate );
				CreditsMovement.setPMovementOrigin( "INIT");
				CreditsMovement.setPMovementType( "BO" );
				CreditsMovement.setPMovementVariation( CreditsParam.getCreditsInitial() );
				
				CreditsMovement.save();
		    }
			
	        
		}
	    
//	  on va chercher l'objet precedent pour mettre sa date de fin a jour
	    String sql = "SELECT P_Employee_Credits_ID "
	    	+ " FROM P_Employee_Credits "
	    	+ " WHERE P_Employee_ID = " + this.getP_Employee_ID()
	    	+ " AND P_Credits_ID = " + this.getP_Credits_ID()
	    	+ " AND EffectIn < " + DB.TO_DATE( this.getEffectIn() )
	    	+ " AND EffectTo IS NULL "
	    	+ " ORDER BY EffectIn Desc ";
	    PreparedStatement pstmt = DB.prepareStatement(sql, null);

	    try
	    {
		    ResultSet rs = pstmt.executeQuery();
		    if(rs.next())
		    {
		    	//si on a un objet, on set sa date de fin a la veille de la date de debut du nouvelle objet
		    	P_Employee_Credits employee_Credits = P_Employee_Credits.get(Env.getCtx(), rs.getInt(1), m_trxName);
		    	if(employee_Credits == null || employee_Credits.getP_Employee_Credits_ID() <= 0)
		    	{
		    		return success;
		    	}
		    	Timestamp tsDate = this.getEffectIn();
		    	// - 1 pour la veille
		    	tsDate = TimeUtil.addDays( tsDate, -1);
		    	employee_Credits.setEffectTo(tsDate);
		    	employee_Credits.save();
		    }
	    }
	    catch(SQLException e)
	    {
	    	s_log.log(Level.SEVERE,"P_Assignment_SpecialRate - afterSave - " + sql, e);	    	
	    }
	    
		return true;
	}  //	afterSave	
	
	private void setTrxName ( String trxName)
	{
		m_trxName = trxName;
	}

	/** Get Total.
	@return Total */
	public BigDecimal getTotalCredits( Timestamp EffectIn ) 
	{
		String sql = "SELECT [dbo].[P_Credits_Total]( " + this.getP_Credits_ID() + ", " + this.getP_Employee_ID() + " , " + DB.TO_DATE( EffectIn ) + " )"; 
		BigDecimal sld = Env.ZERO;

		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				sld = rs.getBigDecimal(1);
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}

		return sld;

	}

	public static P_Employee_Credits getAnnul (Properties ctx, int EmployeeID, int CreditsID, Timestamp EffectIn, String trxName)
	{
	    String sql = "SELECT  P_Employee_Credits_ID FROM P_Employee_Credits "
            + " WHERE P_Employee_ID = " + EmployeeID
            + "   AND P_Credits_ID  = " + CreditsID
//			+ "    and EffectIn<= " + DB.TO_DATE( EffectIn )  
			+ "  Order By EffectIn Desc";
	    
	    PreparedStatement pstmt = null;
    
	    int P_Employee_Credits_ID = 0;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
			    P_Employee_Credits_ID = rs.getInt(1);
			}
			    
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			 
		}
		catch (Exception e)
		{
		    System.out.println( "P_Employee_Credits, get - " + e);
		    return null;
		}
	    
		if ( P_Employee_Credits_ID == 0 )
		   return null;
		
		P_Employee_Credits Employee_Credits = P_Employee_Credits.get( ctx, P_Employee_Credits_ID, trxName );
		return Employee_Credits;
	}	//	get

	
	
	
}	//	P_Employee_Credits



