package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  Time_Sheet_Error Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Time_Sheet_Error extends X_P_Time_Sheet_Error
{
	/**
	 * 	Get Time_Sheet_Error
	 *	@param ctx context
	 * 	@param P_Time_Sheet_Error_ID id
	 *	@return Time_Sheet_Error
	 */
	public static P_Time_Sheet_Error get (Properties ctx, int P_Time_Sheet_Error_ID)
	{
		Integer key = new Integer (P_Time_Sheet_Error_ID);
		P_Time_Sheet_Error Time_Sheet_Error = (P_Time_Sheet_Error)s_cache.get(key);
		if (Time_Sheet_Error != null)
			return Time_Sheet_Error;
		Time_Sheet_Error = new P_Time_Sheet_Error (ctx, P_Time_Sheet_Error_ID, null);
		s_cache.put (key, Time_Sheet_Error);
		return Time_Sheet_Error;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Time_Sheet_Error>	s_cache = new CCache<Integer,P_Time_Sheet_Error>("P_Time_Sheet_Error", 50);
	public CLogger			log = CLogger.getCLogger (getClass());

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Time_Sheet_Error_ID id
	 */
	public P_Time_Sheet_Error (Properties ctx, int P_Time_Sheet_Error_ID, String trxName)
	{
		super (ctx, P_Time_Sheet_Error_ID, trxName);
		if (P_Time_Sheet_Error_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Time_Sheet_Error

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Time_Sheet_Error (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Time_Sheet_Error (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Time_Sheet_Error_ID"), trxName);
	}	//	P_Time_Sheet_Error

    private static boolean MessageExist( int p_time_sheet_id, int p_time_sheet_detail_id, String alert_message, String alert_severity_level, int p_credits_id, String trxName )
    {
    	
		String sql = null;
		String exp1 = "'";
		String exp2 = "''";
		
		sql = "Select 1 From P_Time_Sheet_Error " 
			+ " WHERE p_time_sheet_id = " + p_time_sheet_id
			+ " AND alert_message = '" + alert_message.replaceAll(exp1, exp2) + "'" 
			+ " AND alert_severity_level = '" + alert_severity_level + "'";
		
		if ( p_time_sheet_detail_id != 0 )
			sql += " AND p_time_sheet_detail_id = " + p_time_sheet_detail_id;
		
		if ( p_credits_id != 0 )
			sql += " AND p_credits_id = " + p_credits_id;
		
		boolean exist = false;
		
		PreparedStatement pstmt  = null;
		try 
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
				
			if (rs.next ())
			{
	           exist = true;
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
		}
		catch (Exception e)
		{
//            log.log( Level.SEVERE, "P_Time_Sheet_Error - " + sql, e);
			return false;
		}
    	
    	return exist;
    }
	
	public static void NewMessage( P_Credits Credits, P_Time_Sheet TimeSheet, String Severity_Level, String message, String trxName )
	{

    	P_Employee Employee = P_Employee.get( Env.getCtx(), TimeSheet.getP_Employee_ID() , trxName);
	    
		P_Time_Sheet_Error TimeSheetError = new P_Time_Sheet_Error( Env.getCtx (), -1, trxName );
		
		TimeSheetError.setP_Time_Sheet_ID( TimeSheet.getP_Time_Sheet_ID());
//		TimeSheetError.setP_Time_Sheet_Detail_ID( TimeSheetDetailID);
		
		message = message.replaceAll( "%Emp%", Employee.getValue() + " - " + Employee.getName() );

		message = message.replaceAll( "%Credits%", Credits.getValue() + " - " + Credits.getName() );
		
		P_Time_Sheet time_Sheet = P_Time_Sheet.get(Env.getCtx(), TimeSheetError.getP_Time_Sheet_ID(), trxName);
		P_Period period = P_Period.get(Env.getCtx(), time_Sheet.getP_Period_ID(), trxName);

		P_Employee_Credits Employee_Credits = P_Employee_Credits.get( Env.getCtx(), Employee.getP_Employee_ID(), Credits.getP_Credits_ID(), period.getStartDate() , trxName);
		if(Employee_Credits == null)
		{
			String error_message = "Pas d'enregistrement dans P_Employee_Credits pour l'employé "+Employee.getP_Employee_ID()
				+ " et pour la banque "+Credits.getP_Credits_ID()+" en date du "+period.getStartDate().toString();
			System.out.println(error_message);
		}
		if ( Employee_Credits != null )
		{
			P_Method_Credits Method_Credits = P_Method_Credits.get( Env.getCtx(), Employee_Credits.getP_Method_Credits_ID(), trxName );
			message = message.replaceAll( "%Method%", Method_Credits.getName() );
		}
		
		TimeSheetError.setAlert_Message( message );
		TimeSheetError.setAlert_Severity_Level( Severity_Level );
		TimeSheetError.setP_Credits_ID( Credits.getP_Credits_ID() );

	    if ( ! MessageExist( TimeSheetError.getP_Time_Sheet_ID(), TimeSheetError.getP_Time_Sheet_Detail_ID(), TimeSheetError.getAlert_Message(), TimeSheetError.getAlert_Severity_Level(), TimeSheetError.getP_Credits_ID(), trxName) )
	    {
	    	TimeSheetError.save();
	    }
	    	
	    
	    
	}

	public static void NewMessage( P_Time_Sheet TimeSheet, String Severity_Level, String message, String trxName )
	{
	    P_Employee Employee = P_Employee.get( Env.getCtx(), TimeSheet.getP_Employee_ID(), trxName );

		P_Time_Sheet_Error TimeSheetError = new P_Time_Sheet_Error( Env.getCtx (), -1, trxName );
		
		TimeSheetError.setP_Time_Sheet_ID( TimeSheet.getP_Time_Sheet_ID());
//		TimeSheetError.setP_Time_Sheet_Detail_ID( TimeSheetDetailID);
		
		message = message.replaceAll( "%Emp%", Employee.getValue() + " - " + Employee.getName() );

		TimeSheetError.setAlert_Message( message );
		TimeSheetError.setAlert_Severity_Level( Severity_Level );

		if ( ! MessageExist( TimeSheetError.getP_Time_Sheet_ID(), 0, TimeSheetError.getAlert_Message(), TimeSheetError.getAlert_Severity_Level(), 0, trxName ) )
	    {
			TimeSheetError.save();
	    	
	    }
	    
	}

}	//	P_Time_Sheet_Error
