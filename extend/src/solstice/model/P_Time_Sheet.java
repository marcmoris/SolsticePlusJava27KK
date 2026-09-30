package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.model.PO;
import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *  Time_Sheet Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Time_Sheet extends X_P_Time_Sheet implements IBookletTimeSheet
{
	/**
	 * 	Get Time_Sheet
	 *	@param ctx context
	 * 	@param P_Time_Sheet_ID id
	 *	@return Time_Sheet
	 */
	public static P_Time_Sheet get (Properties ctx, int P_Time_Sheet_ID, String trxName)
	{
		Integer key = new Integer (P_Time_Sheet_ID);
		P_Time_Sheet Time_Sheet = (P_Time_Sheet)s_cache.get(key);
		if (Time_Sheet != null)
			return Time_Sheet;
		Time_Sheet = new P_Time_Sheet (ctx, P_Time_Sheet_ID, trxName);
		s_cache.put (key, Time_Sheet);
		return Time_Sheet;
	}	//	get

	/**	Cache					*/
	static private CCache<Integer,P_Time_Sheet> s_cache = new CCache<Integer,P_Time_Sheet>("P_Time_Sheet", 1000, 60);

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Time_Sheet.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Time_Sheet_ID id
	 */
	public P_Time_Sheet (Properties ctx, int P_Time_Sheet_ID, String trxName)
	{
		super (ctx, P_Time_Sheet_ID, trxName);
		if (P_Time_Sheet_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Time_Sheet

	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Time_Sheet (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Time_Sheet_ID"), trxName);
	}	//	P_Time_Sheet

    public void setIsError(boolean iserror) {
        super.setIsError( iserror );
        if( iserror ) {
            setTimeSheetStatus( TIMESHEETSTATUS_Error );
        }
    }

	public static P_Time_Sheet GetWithPaymentID (Properties ctx, int P_Payment_ID, String trxName)
	{
		int TimeSheetID = 0;

		String sql = "Select P_Time_Sheet_ID From P_Time_Sheet where P_Payment_ID = ?";

		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt( 1, P_Payment_ID );
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next() )
				TimeSheetID = rs.getInt( "P_Time_Sheet_ID");

		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"* Error * P_Time_Sheet.GetWithPaymentID - " + sql + " - " , e);

		}
		
		if ( TimeSheetID == 0 )
		    return null;
		
		return P_Time_Sheet.get( ctx, TimeSheetID, trxName );
	}	//	get

	public String GetTimeSheetStatusDsc ()
	{
        String sql = null;
        
		if (Env.isBaseLanguage(Env.getCtx(), "AD_Ref_List"))
			sql = "select arl.value, arl.name from ad_ref_list arl where arl.ad_reference_id  = ? ";
		else
			sql = "select ad_language, arl.value, arlt.name from ad_ref_list_trl arlt inner join ad_ref_list arl on arlt.ad_ref_list_id = arl.ad_ref_list_id where arl.ad_reference_id  = ? ";

        String result = null;
        //Get the description for Time sheet status
        try
        {
            String whereElement = null;
    		if (Env.isBaseLanguage(Env.getCtx(), "AD_Ref_List"))
    			whereElement = " and arl.value = ? ";
    		else
    			whereElement = " and arl.value = ? and AD_Language = ? ";
    			
            PreparedStatement pstmt = DB.prepareStatement(sql + whereElement, get_TrxName());
            //The ad_reference_id for the time sheet status is 2000078
            pstmt.setInt(1, TIMESHEETSTATUS_AD_Reference_ID);
            pstmt.setString(2, getTimeSheetStatus());
    		if ( ! Env.isBaseLanguage(Env.getCtx(), "AD_Ref_List"))
              pstmt.setString(3, Env.getAD_Language(Env.getCtx()));

            ResultSet rs = pstmt.executeQuery();

            if ( rs.next() )
            {
                result = rs.getString("name");
            }
            rs.close();
            pstmt.close();
            pstmt=null;
        }
        catch (Exception e)
        {
        	s_log.log(Level.SEVERE,"VSheetHeaderPanel.LoadTimeSheetInformation", e);
        }
	    
	    return result;
	}	//	P_Time_Sheet

	
	public static void SetTimeSheetInitialPeriod ( int periodID, String trxName )
	{
	    P_Time_Sheet TimeSheet ;
	
        String sql = "Select P_Time_Sheet_ID From P_Time_Sheet WHERE TimeSheetStatus in ( 'V','C' ) and P_Period_ID = ?"; 

        try
        {

            PreparedStatement pstmt;

            pstmt = DB.prepareStatement(sql, trxName);
            pstmt.setInt(1, periodID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next())
            {
                TimeSheet = SetTimeSheetInitial( rs.getInt(1), trxName);
            }

            rs.close();
            pstmt.close();

        }
        catch (Exception e)
        {
        	s_log.log(Level.SEVERE,"SetTimeSheetInitial", e);
        }
	}
	
	//
	// Remet le feuille de temps a l'état initial, mais on conserver les lignes d'ajustement saisie.
	//
	public static P_Time_Sheet SetTimeSheetInitial( int TimeSheetID, String trxName )
	{
	    P_Time_Sheet TimeSheet = new P_Time_Sheet( Env.getCtx(), TimeSheetID, trxName );

	    if ( TimeSheet.getP_Payment_ID() != 0 )
	    {
	    	
	        P_Payment Payment = P_Payment.get(Env.getCtx(), TimeSheet.getP_Payment_ID(), trxName);
		    if (Payment != null)
		    {
		        Payment.delete(true);
/*		    	StringBuffer sql = new StringBuffer( "Delete From P_Payment_Deduction Where Origine <> 'AJT' AND P_Payment_ID = " + TimeSheet.getP_Payment_ID()); 
		  		int no = DB.executeUpdate (sql.toString ());
		  		
		  		sql = new StringBuffer( "Delete From P_Payment_Taxable_Benefit Where Origine <> 'AJT' AND  P_Payment_ID = " + TimeSheet.getP_Payment_ID()); 
		  		no = DB.executeUpdate (sql.toString ());
		  		
		  		sql = new StringBuffer( "Delete From P_Credits_Movement Where PMOVEMENTORIGIN <> 'AJT' AND P_Payment_ID = " + TimeSheet.getP_Payment_ID()); 
		  		no = DB.executeUpdate (sql.toString ());
				
		  		sql = new StringBuffer( "Delete From P_Payment_Distribution Where P_Payment_ID = " + TimeSheet.getP_Payment_ID()); 
		  		no = DB.executeUpdate (sql.toString ());

		  		sql = new StringBuffer( "Delete From P_Payment_Gain Where P_Payment_ID = " + TimeSheet.getP_Payment_ID()); 
		  		no = DB.executeUpdate (sql.toString ());

		  		sql = new StringBuffer( "Delete From P_Employee_Advance_Detail Where P_Payment_ID = " + TimeSheet.getP_Payment_ID()); 
		  		no = DB.executeUpdate (sql.toString ());
		  		
		  		sql = new StringBuffer( "Delete From P_Payment_Deduction_Excep Where P_Payment_ID = " + TimeSheet.getP_Payment_ID()); 
		  		no = DB.executeUpdate (sql.toString ());
		  		
		  		sql = new StringBuffer( "Delete From P_Payment_EntryLine Where P_Payment_ID = " + TimeSheet.getP_Payment_ID()); 
		  		no = DB.executeUpdate (sql.toString ());
*/
		    	
		    }
		    
	    }	    
	    TimeSheet.setTimeSheetStatus(P_Time_Sheet.TIMESHEETSTATUS_Initial);
	    TimeSheet.setP_Payment_ID(0);
	    TimeSheet.setIsError(false);
	    TimeSheet.save();

	    return TimeSheet;
	}
	
	public static int deleteError( int TimeSheetID, String trxName )
	{
		int no;
        String sql = "Delete From P_Time_Sheet_Error WHERE P_Time_Sheet_ID = " + TimeSheetID; 
		no = DB.executeUpdate(sql, null);
		return no;
	}
	public static boolean CheckError( int TimeSheetID, String trxName )
	{
	 
        String sql = "Select 1 From P_Time_Sheet_Error WHERE P_Time_Sheet_ID = ? AND Alert_Severity_Level = 'E'"; 
       
        boolean isError = false;
        try
        {

            PreparedStatement pstmt;

            pstmt = DB.prepareStatement(sql, trxName);
            pstmt.setInt(1, TimeSheetID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next())
            {
                isError = true;
            }

            rs.close();
            pstmt.close();

        }
        catch (Exception e)
        {
        	s_log.log(Level.SEVERE,"CheckError", e);
        }
	    
	    return isError;
	}

	
	public static boolean CheckWarning( int TimeSheetID, String trxName )
	{
	 
        String sql = "Select 1 From P_Time_Sheet_Error WHERE P_Time_Sheet_ID = ? AND Alert_Severity_Level = 'W'"; 

        boolean isWarning = false;
        try
        {

            PreparedStatement pstmt;

            pstmt = DB.prepareStatement(sql, trxName);
            pstmt.setInt(1, TimeSheetID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next())
            {
                isWarning = true;
            }

            rs.close();
            pstmt.close();

        }
        catch (Exception e)
        {
        	s_log.log(Level.SEVERE,"CheckWarning", e);
        }
	    
	    return isWarning;
	}

	public boolean isEmployeeWorkOnWeekly( int day )
	{
        String sql = "Select 1 From P_Time_Sheet_Weekly "
            + " WHERE P_Time_Sheet_ID = ? AND ";
        
		switch ( day ) 
		{
        	case 0: sql = sql + "SUNDAY <> 0";
        	        break;
        	case 1: sql = sql + "MONDAY <> 0";
        		break;
        	case 2: sql = sql + "TUESDAY <> 0";
	        	break;
        	case 3: sql = sql + "WEDNESDAY <> 0";
	        	break;
        	case 4: sql = sql + "THURSDAY <> 0";
	        	break;
        	case 5: sql = sql + "FRIDAY <> 0";
	        	break;
        	case 6: sql = sql + "SATURDAY <> 0";
	        	break;
			default :   sql = sql + "1=0";
		}
        	        

        boolean bReturn = false;
        try
        {

            PreparedStatement pstmt;

            pstmt = DB.prepareStatement(sql, get_TrxName());
            pstmt.setInt(1, this.getP_Time_Sheet_ID());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next())
            {
                bReturn = true;
            }

            rs.close();
            pstmt.close();

        }
        catch (Exception e)
        {
        	s_log.log(Level.SEVERE,"isEmployeeWorkOnWeekly", e);
        }

        return bReturn;

	}

	public boolean isEmployeeWorkOnPost( int PostID  )
	{
        String sql = "Select 1 From P_Time_Sheet_Detail, P_Assignment "
            + " WHERE P_Time_Sheet_Detail.P_Assignment_ID = P_Assignment.P_Assignment_ID " 
            + "   AND P_Time_Sheet_ID = ? AND P_Post_ID = ?"; 

        boolean bReturn = false;
	    try
	    {

	        PreparedStatement pstmt;

	        pstmt = DB.prepareStatement(sql, get_TrxName());
	        pstmt.setInt(1, this.getP_Time_Sheet_ID());
	        pstmt.setInt(2, PostID);
	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next())
	        {
	            bReturn = true;
	        }

	        rs.close();
	        pstmt.close();

	    }
	    catch (Exception e)
	    {
	    	s_log.log(Level.SEVERE,"isEmployeeWorkOnPost", e);
	    }

	    return bReturn;
	}

	public boolean isEmployeeWorkOnLocalization( int id )
	{
        String sql = "Select 1 From P_Time_Sheet_Detail, P_Assignment "
            + " WHERE P_Time_Sheet_Detail.P_Assignment_ID = P_Assignment.P_Assignment_ID " 
            + " AND P_Time_Sheet_ID = ? AND P_Localization_ID = ?"; 

        boolean bReturn = false;
	    try
	    {

	        PreparedStatement pstmt;

	        pstmt = DB.prepareStatement(sql, get_TrxName());
	        pstmt.setInt(1, this.getP_Time_Sheet_ID());
	        pstmt.setInt(2, id);
	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next())
	        {
	            bReturn = true;
	        }

	        rs.close();
	        pstmt.close();

	    }
	    catch (Exception e)
	    {
	    	s_log.log(Level.SEVERE,"isEmployeeWorkOnLocalization", e);
	    }
	    
	    return bReturn;
	    
	}


	/**
	 * Retourne le ID
	 */
	public int getRecord_ID()
	{
	    return this.getP_Time_Sheet_ID();
	}
	
	/**
	 * Affecte le ID
	 */
	public void setRecord_ID(int recordId)
	{
	    this.setP_Time_Sheet_ID(recordId);
	}

	public int getNumberOfGain()
	{
        String sql = "Select Count(*) cnt From P_Time_Sheet_Detail"
            + " WHERE P_Time_Sheet_ID = ? "; 

        int nbr = 0;
	    try
	    {

	        PreparedStatement pstmt;

	        pstmt = DB.prepareStatement(sql, get_TrxName());
	        pstmt.setInt(1, this.getP_Time_Sheet_ID());
	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next())
	        {
	            nbr = rs.getInt("cnt");
	        }

	        rs.close();
	        pstmt.close();

	    }
	    catch (Exception e)
	    {
	    	s_log.log(Level.SEVERE,"getNumberOfGain", e);
	    }
		
		return nbr;
	}
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true or false
	 */

	protected boolean beforeSave (boolean newRecord)
	{
		if ( this.getTaxation_Region_ID() == 0 )
		{
			P_Employee Employee = P_Employee.get( Env.getCtx(), this.getP_Employee_ID(), this.get_TrxName());
			this.setTaxation_Region_ID(Employee.getTaxation_Region_ID());
		}
	
		return true;

	}
	
	//+2011.11.16 Ajout pour s'assuré de l'uniformité des informations
	protected boolean afterSave (boolean newRecord, boolean success)
	{
	    if (!success )
		{
			return success;
		}
	    
		if ( this.getP_Payment_ID() != 0 )
		{
	        P_Payment Payment = P_Payment.get(Env.getCtx(), this.getP_Payment_ID(), this.get_TrxName());
	        Payment.setAD_Org_ID( this.getAD_Org_ID());
	        Payment.setPaymentType( this.getPaymentType() );
	        Payment.setP_Employer_ID( this.getP_Employer_ID() );
		    Payment.setP_Workplace_ID( this.getP_Workplace_ID() );
		    Payment.setP_Department_ID( this.getP_Department_ID());
		    Payment.setC_Activity_ID( this.getC_Activity_ID());
		    Payment.setP_Distribution_ID( this.getP_Distribution_ID());
		    Payment.setP_Distribution_Booklet_ID( this.getP_Distribution_Booklet_ID());
		    Payment.setP_Job_Title_ID( this.getP_Job_Title_ID());
		    Payment.setP_Job_Type_ID( this.getP_Job_Type_ID());
		    Payment.setP_Occupation_Group_ID( this.getP_Occupation_Group_ID());
		    Payment.setP_Payment_Group_ID( this.getP_Payment_Group_ID() );
		    Payment.setTaxation_Region_ID( this.getTaxation_Region_ID());
		    Payment.setDescription( this.getDescription());
			Payment.save();
		}
		
		return true;
	}
	//-2011.11.16		
	
	public static P_Time_Sheet copyFrom (Properties ctx, int P_Time_Sheet_ID, int P_Period_ID, String trxName)
	{

		P_Time_Sheet from = P_Time_Sheet.get(ctx, P_Time_Sheet_ID, trxName);
		if (from.getP_Time_Sheet_ID() == 0)
			throw new IllegalArgumentException ("From Time_Sheet not found P_Time_Sheet_ID=" + P_Time_Sheet_ID);
		
		P_Time_Sheet  to = new P_Time_Sheet (ctx, -1, trxName);
		PO.copyValues(from, to, from.getAD_Client_ID(), from.getAD_Org_ID());
		to.set_ValueNoCheck ("P_Time_Sheet_ID", I_ZERO);

		to.setP_Period_ID( P_Period_ID );

		String value = DB.getDocumentNo (to.getAD_Client_ID() , "P_Time_Sheet", trxName );
		P_Period Period = P_Period.get( Env.getCtx(), to.getP_Period_ID(), trxName);
		to.setValue( Period.getName() + "-" + value.substring( 3, 7 ) );
        to.setPayDate( Period.getPayDate());
        
		if (!to.save())
			throw new IllegalStateException("Could not create TimeSheet");

		return to;

	}
	
	
	public static P_Time_Sheet get (Properties ctx, int P_Period_ID, int P_Employee_ID, String SheetType, String trxName)
	{
		int TimeSheetID = 0;

		String sql = "Select P_Time_Sheet_ID From P_Time_Sheet where P_Period_ID = ? and P_Employee_ID = ? and SheetType = ? ";

		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt( 1, P_Period_ID );
			pstmt.setInt( 2, P_Employee_ID );
			pstmt.setString( 3, SheetType );
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next() )
				TimeSheetID = rs.getInt( "P_Time_Sheet_ID");

		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"* Error * P_Time_Sheet.GetWithPaymentID - " + sql + " - " , e);

		}
		
		if ( TimeSheetID == 0 )
		    return null;
		
		return P_Time_Sheet.get( ctx, TimeSheetID, trxName );
	}	//	get
	
}	//	P_Time_Sheet
