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

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;


/**
 *  Time_Sheet_Detail Model
 *
 *  @author Marc Morissette
 *  @version $Id: P_Time_Sheet_Detail.java,v 1.1 2007/07/18 14:43:16 marmor01 Exp $
 */
public class P_Time_Sheet_Detail extends X_P_Time_Sheet_Detail implements IBookletTimeSheetDetail
{
	/**
	 * 	Get Time_Sheet_Detail
	 *	@param ctx context
	 * 	@param P_Time_Sheet_Detail_ID id
	 *	@return Time_Sheet_Detail
	 */
	public static P_Time_Sheet_Detail get (Properties ctx, int P_Time_Sheet_Detail_ID)
	{
		Integer key = new Integer (P_Time_Sheet_Detail_ID);
		P_Time_Sheet_Detail Time_Sheet_Detail = (P_Time_Sheet_Detail)s_cache.get(key);
		if (Time_Sheet_Detail != null)
			return Time_Sheet_Detail;
		Time_Sheet_Detail = new P_Time_Sheet_Detail (ctx, P_Time_Sheet_Detail_ID, null);
		s_cache.put (key, Time_Sheet_Detail);
		return Time_Sheet_Detail;
	}	//	get

/*
	public static P_Time_Sheet_Detail get (Properties ctx, P_Time_Sheet_Weekly TimeSheetWeekly, Timestamp Day )
	{
		
		int P_Time_Sheet_Detail_ID = 0;

		String query = "Select P_Time_Sheet_Detail_ID FROM P_Time_Sheet_Detail " 
			 	     + "WHERE P_Time_Sheet_ID = ? " 
					 + "  AND P_Time_Sheet_Weekly_ID = ? "
					 + "  AND Day = " + DB.TO_DATE( Day );

		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (query, null);
			pstmt.setInt(1, TimeSheetWeekly.getP_Time_Sheet_ID() );
			pstmt.setInt(2, TimeSheetWeekly.getP_Time_Sheet_Weekly_ID() );

			ResultSet rs = pstmt.executeQuery ();
			
			if (rs.next ())
			{
				P_Time_Sheet_Detail_ID = rs.getInt(1);
			}
			else
			{
				P_Time_Sheet_Detail_ID = -1;
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println("* ERROR * Read Time_Sheet_Detail error  " + e);
		}

		return P_Time_Sheet_Detail.get( ctx, P_Time_Sheet_Detail_ID );
	}	//	get
*/
/*	
	public static P_Time_Sheet_Detail get (Properties ctx, int TimeSheet_ID, int Period_ID, int Gain_ID, int Assignment_ID, int Schedule_ID, int Activity_ID  )
	{
		
		int P_Time_Sheet_Detail_ID = 0;

		String sqlServerQuery = "Select P_Time_Sheet_Detail_ID FROM P_Time_Sheet_Detail WHERE P_Time_Sheet_ID = ? AND P_Period_ID = ? AND P_Gain_ID = ? AND P_Assignment_ID = ? AND P_Schedule_ID = ? And ( isnull( C_Activity_ID, 0 ) = ? ) ";

		String oraclesQuery   = "Select P_Time_Sheet_Detail_ID FROM P_Time_Sheet_Detail WHERE P_Time_Sheet_ID = ? AND P_Period_ID = ? AND P_Gain_ID = ? AND P_Assignment_ID = ? AND P_Schedule_ID = ? And ( nvl( C_Activity_ID, 0 ) = ? ) ";	


		//Detect on wich server we're connected
		//The default is sql server

        String query = sqlServerQuery;


		System.out.println(" P_Time_Sheet_Detail.get " + query  );
		System.out.println(" TimeSheet_ID = " + TimeSheet_ID );
		System.out.println(" Period_ID = " + Period_ID );
		System.out.println(" Gain_ID  = " + Gain_ID );
		System.out.println(" Assignment_ID = " + Assignment_ID );
		System.out.println(" Schedule_ID = " + Schedule_ID );
		System.out.println(" Activity_ID = " + Activity_ID );

		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (query, null);
			pstmt.setInt(1, TimeSheet_ID );
			pstmt.setInt(2, Period_ID );
			pstmt.setInt(3, Gain_ID );
			pstmt.setInt(4, Assignment_ID );
			pstmt.setInt(5, Schedule_ID );
			pstmt.setInt(6, Activity_ID );

			ResultSet rs = pstmt.executeQuery ();
			
			if (rs.next ())
			{
				P_Time_Sheet_Detail_ID = rs.getInt(1);
			}
			else
			{
				P_Time_Sheet_Detail_ID = -1;
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
//			log.error ("Read tax Parameter RPC ", e);
			System.out.println("* ERROR * Read Time_Sheet_Detail error  " + e);
		}

		return P_Time_Sheet_Detail.get( ctx, P_Time_Sheet_Detail_ID );
	}	//	get

	*/

	/**	Cache						*/
	private static CCache<Integer,P_Time_Sheet_Detail>	s_cache = new CCache<Integer,P_Time_Sheet_Detail>("P_Time_Sheet_Detail", 500);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Time_Sheet_Detail_ID id
	 */
	public P_Time_Sheet_Detail (Properties ctx, int P_Time_Sheet_Detail_ID, String trxName)
	{
		super (ctx, P_Time_Sheet_Detail_ID, trxName);
		if (P_Time_Sheet_Detail_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Time_Sheet_Detail

	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Time_Sheet_Detail (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}
	
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Time_Sheet_Detail (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Time_Sheet_Detail_ID"), trxName);
	}	//	P_Time_Sheet_Detail

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

	private P_Time_Sheet TimeSheet = null;
	private P_Period Period = null;
	private P_Assignment Assignment = null;
	
	protected boolean beforeSave (boolean newRecord)
	{
		if ( this.getP_Assignment_ID() != 0)
		{
			if ( Assignment == null || Assignment.getP_Assignment_ID() != this.getP_Assignment_ID())
				Assignment = P_Assignment.get(Env.getCtx(), this.getP_Assignment_ID(), this.get_TrxName());

			if ( TimeSheet == null || this.getP_Time_Sheet_ID() != TimeSheet.getP_Time_Sheet_ID())
				TimeSheet = P_Time_Sheet.get(Env.getCtx(), this.getP_Time_Sheet_ID(), this.get_TrxName());
			
			if ( Assignment.getP_Employee_ID() != TimeSheet.getP_Employee_ID())
			{
				log.saveError("ValidationError", "Affectation n'est pas en relation avec cet employé");
				return false;
			}
		}

		if ( Period == null || Period.getP_Period_ID() != this.getP_Period_ID())
			Period = P_Period.get( Env.getCtx(), this.getP_Period_ID(), this.get_TrxName());
		
		if ( this.getStartDate().compareTo(Period.getEndDate()) > 0)
		{
			this.setStartDate(Period.getStartDate());
			this.setEndDate(Period.getEndDate());
		}
			

        return true;
	}	//	beforeSave

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 */
/*	protected boolean afterSave (boolean newRecord, boolean success)
	{
	    
	    if (!success )
		{
			return success;
		}

	    if ( newRecord )
	    {
			String sql = null;

			sql = " SELECT P_Time_Sheet_Weekly_ID "
		        + " FROM    P_Time_Sheet_Weekly "
		        + " WHERE ";
ici
			PreparedStatement pstmt = null;
			
			int Time_Sheet_Weekly_ID = -1;
			
			try
			{
				pstmt = DB.prepareStatement (sql);
				int index = 1;
				ResultSet rs = pstmt.executeQuery ();
				if ( rs.next () )
				{
					Time_Sheet_Weekly_ID = rs.getInt(1);
				}

				rs.close ();
				pstmt.close ();
				pstmt = null;
			}
			catch (Exception e)
			{
				System.out.println ("P_Time_Sheet_Detail - afterSave - " + sql + " - " + e);
			}

			P_Time_Sheet_Weekly TimeSheetWeekly = P_Time_Sheet_Weekly.get( getCtx(), Time_Sheet_Weekly_ID );
			TimeSheetWeekly.setC_Activity_ID( this.getC_Activity_ID());
			TimeSheetWeekly.setC_Campaign_ID( this.getC_Campaign_ID());
			TimeSheetWeekly.setC_Project_ID( this.getC_Project_ID());
			TimeSheetWeekly.setP_AccidentReport_ID( this.getP_AccidentReport_ID());
			TimeSheetWeekly.setP_Assignment_ID( this.getP_Assignment_ID());
			TimeSheetWeekly.setP_Gain_ID( this.getP_Gain_ID());
			TimeSheetWeekly.setP_Period_ID( this.getP_Period_ID());
			TimeSheetWeekly.setP_Schedule_ID( this.getP_Schedule_ID());
			TimeSheetWeekly.setP_Time_Sheet_ID( this.getP_Time_Sheet_ID());
			
	        GregorianCalendar cal = new GregorianCalendar();
	        cal.setTime( (Date)this.getDay() );

			switch ( cal.get(Calendar.DAY_OF_WEEK) ) 
			{
			case Calendar.SUNDAY : 	
				TimeSheetWeekly.setSunday( this.getDayQty() ) ;
				break;
			case Calendar.MONDAY : 	
				TimeSheetWeekly.setMonday( this.getDayQty());
				break;
			case Calendar.TUESDAY : 	
				TimeSheetWeekly.setTuesday( this.getDayQty());
				break;
			case Calendar.WEDNESDAY : 	
				TimeSheetWeekly.setWednesday( this.getDayQty());
				break;
			case Calendar.THURSDAY : 	
				TimeSheetWeekly.setThursday( this.getDayQty());
				break;
			case Calendar.FRIDAY : 	
				TimeSheetWeekly.setFriday( this.getDayQty());
				break;
			case Calendar.SATURDAY : 	
				TimeSheetWeekly.setSaturday( this.getDayQty());
				break;

			default :   
				TimeSheetWeekly.setDayQty( this.getDayQty());
			}
			
			TimeSheetWeekly.setWeekIndex( 1 );

			TimeSheetWeekly.save();
			
	    }
		return true;
	}  //	afterSave	
*/
	

    public void setRecord_ID(int record_ID) {this.setP_Time_Sheet_Detail_ID(record_ID);}
    public void setParent_ID(int parent_ID) {this.setP_Time_Sheet_ID(parent_ID);}
    public int getRecord_ID() {return this.getP_Time_Sheet_Detail_ID();}
    public int getParent_ID() {return this.getP_Time_Sheet_ID();}

}	//	P_Time_Sheet_Detail
