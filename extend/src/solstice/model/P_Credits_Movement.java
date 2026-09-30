package solstice.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.sql.Timestamp;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;
import solstice.model.P_Employee_Credits;

/**
 *  Credits_Movement Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Credits_Movement extends X_P_Credits_Movement
{
	/**
	 * 	Get Credits_Movement
	 *	@param ctx context
	 * 	@param P_Credits_Movement_ID id
	 *	@return Credits_Movement
	 */
	public static P_Credits_Movement get (Properties ctx, int P_Credits_Movement_ID, String trxName)
	{
		//PATCH KM
		//TODO voir pourquoi si on passe -1, il retourne un enregistrement existant
		if(P_Credits_Movement_ID == -1)
			return new P_Credits_Movement(ctx, 0, trxName);
		Integer key = new Integer (P_Credits_Movement_ID);
		P_Credits_Movement Credits_Movement = (P_Credits_Movement)s_cache.get(key);
		if (Credits_Movement != null)
			return Credits_Movement;
		Credits_Movement = new P_Credits_Movement (ctx, P_Credits_Movement_ID, trxName);
		s_cache.put (key, Credits_Movement);
		return Credits_Movement;
	}	//	get

	


	public static P_Credits_Movement get (Properties ctx, int Payment_ID, int Credits_ID, Timestamp MovementDate, String MovementType, String PMovementOrigin, String trxName  ) 
	{

		int Credits_Movement_ID = 0;
		String query = "Select P_Credits_Movement_ID FROM P_Credits_Movement " +
				"WHERE P_Payment_ID = ? AND P_Credits_ID = ? AND PMovementDate = ? AND PMovementType = ? AND PMovementOrigin = ? "; 

		
		String query2 = "Select P_Credits_Movement_ID FROM P_Credits_Movement " +
		"WHERE P_Payment_ID = " + Payment_ID + " AND P_Credits_ID = " + Credits_ID + " AND PMovementDate = " + DB.TO_DATE( MovementDate ) + " AND PMovementType = '" + MovementType + "' AND PMovementOrigin = '" + PMovementOrigin + "'"; 

		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (query2, trxName);
/*			pstmt.setInt(1, Payment_ID );
			pstmt.setInt(2, Credits_ID );
			pstmt.setString(3, DB.TO_DATE( MovementDate ) );
			pstmt.setString(4, MovementType );
			pstmt.setString(5, PMovementOrigin );
*/			
//			System.out.println(" P_Time_Sheet_Weekly.get " + query2 );
			
			ResultSet rs = pstmt.executeQuery ();
			
			if (rs.next ())
			{
				Credits_Movement_ID = rs.getInt(1);
			}
			else
			{
				Credits_Movement_ID = -1;
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
//			log.error ("Read Credits Mouvement ", e);
			System.out.println("* ERROR * Read Credits Mouvement " + e);
		}
		
		return P_Credits_Movement.get(ctx, Credits_Movement_ID, trxName ) ;
	}	//	get

	
	/**	Cache						*/
	private static CCache<Integer,P_Credits_Movement>	s_cache = new CCache<Integer,P_Credits_Movement>("P_Credits_Movement", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Credits_Movement_ID id
	 */
	public P_Credits_Movement (Properties ctx, int P_Credits_Movement_ID, String trxName)
	{
		super (ctx, P_Credits_Movement_ID, trxName);
		if (P_Credits_Movement_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Credits_Movement

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Credits_Movement (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}

	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Credits_Movement (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Credits_Movement_ID"), trxName);
	}	//	P_Credits_Movement


	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true or false
	 */

	protected boolean beforeSave (boolean newRecord)
	{
		if ( this.getP_Employee_Credits_ID() == 0)
		{
			P_Employee_Credits EmployeeCredits = P_Employee_Credits.get( Env.getCtx(), this.getP_Employee_ID(), this.getP_Credits_ID(), this.getPMovementDate(), this.get_TrxName() );
			if (  EmployeeCredits != null )
				this.setP_Employee_Credits_ID(  EmployeeCredits.getP_Employee_Credits_ID() );
				
		}

		P_Employee Employee = P_Employee.get( Env.getCtx(), this.getP_Employee_ID(), null);
    	P_Assignment Assignment = P_Assignment.get( Env.getCtx(), Employee.GetAssignmentPrincipal(), null);
    	P_Assignment_Param Param = P_Assignment_Param.get(Env.getCtx(), Assignment.getP_Assignment_ID(), this.getPMovementDate() , null );
    	 
    	if ( Assignment == null || Param == null )
    	{
    		return true;
    	}
    		
    	BigDecimal hourlyrate = Param.getHourly_Rate();

    	this.setHourly_Rate( hourlyrate );
    	/*    	P_Credits Credits = P_Credits.get( Env.getCtx(), this.getP_Credits_ID(), null );

    	if ( Credits.getP_UOM_ID() == 101  )
    	{
    		this.setAmt_Reserve( this.getPMovementVariation() );
    	}
    	else if ( Credits.getP_UOM_ID() == 102  )
    	{
    		BigDecimal amt = this.getPMovementVariation().multiply( Param.getDay_Hours() );
    		amt	= amt.multiply(hourlyrate);
    		amt = amt.setScale(2,  BigDecimal.ROUND_HALF_UP);
    		this.setAmt_Reserve( amt);
   		
    	}
    	else
    	{
         	this.setAmt_Reserve( (this.getPMovementVariation().multiply(hourlyrate)).setScale(2,  BigDecimal.ROUND_HALF_UP)  );
    		
    	}
*/		
		return true;
	}



	
	
}	//	P_Credits_Movement
