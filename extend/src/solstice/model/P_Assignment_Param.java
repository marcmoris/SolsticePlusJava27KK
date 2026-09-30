package solstice.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;
import solstice.model.P_Assignment;
import solstice.process.PgiUtil;


public class P_Assignment_Param extends X_P_Assignment_Param 
{
	

    /**
	 * 	Get Credits_Param
	 *	@param ctx context
	 * 	@param P_Credits_Param_ID id
	 *	@return Credits_Param
	 */
	public static P_Assignment_Param get (Properties ctx, int iP_Assignment_Param_ID, String trxName)
	{
		Integer key = new Integer (iP_Assignment_Param_ID);
		P_Assignment_Param assignment_Param = (P_Assignment_Param)s_cache.get(key);
		if (assignment_Param != null)
			return assignment_Param;
		assignment_Param = new P_Assignment_Param (ctx, iP_Assignment_Param_ID, trxName);
		s_cache.put (key, assignment_Param);
		if(assignment_Param != null)
		{
			assignment_Param.setTrxName(trxName);
		}
		
		return assignment_Param;
	}	//	get


	/**	Cache						*/
	private static CCache<Integer,P_Assignment_Param>	s_cache = new CCache<Integer,P_Assignment_Param>("P_Assignment_Param", 20);
	/**	Static Logger				*/
	private static CLogger		s_log = CLogger.getCLogger (P_Assignment_Param.class);

	private String				m_trxName = null;

	private	BigDecimal m_Annual_Increase = null;

	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Credits_Param_ID id
	 */
	public P_Assignment_Param (Properties ctx, int iP_Assignment_Param_ID, String trxName)
	{
		super (ctx, iP_Assignment_Param_ID, trxName);
		if (iP_Assignment_Param_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
		m_trxName = trxName;

		P_Assignment Assignment = P_Assignment.get( ctx, this.getP_Assignment_ID(), trxName);
		m_Annual_Increase = P_Payment_Group.getAnnual_Increase( ctx, Assignment.getP_Employee_ID(), trxName);

	}	//	P_Credits_Param

	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Assignment_Param (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
		m_trxName = trxName;
	}
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Assignment_Param (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Assignment_Param_ID"), trxName);
		m_trxName = trxName;
	}	//	P_Credits_Param


	public static P_Assignment_Param get (Properties ctx, int iP_Assignment_ID, Timestamp EffectIn, String trxName )
	{
		int iP_Assignment_Param_ID = 0;

		String sql = null;
		sql = "Select P_Assignment_Param_ID From P_Assignment_Param ";
		sql +="  Where P_Assignment_Param.IsActive = 'Y' ";
		sql +="    and P_Assignment_ID = " + iP_Assignment_ID; 
		sql +="    and EffectIn <= " + DB.TO_DATE( EffectIn )   ;
		sql +="  Order By EffectIn Desc";



//		System.out.println("P_Credits_Param get sql" + sql );
			
		PreparedStatement pstmt = null;
		try
		{
//			    pstmt.setTimestamp(1, EffectIn);
				pstmt = DB.prepareStatement (sql, null);

				ResultSet rs = pstmt.executeQuery ();
				if ( rs.next () )
					iP_Assignment_Param_ID = rs.getInt(1);
				else
					iP_Assignment_Param_ID = 0;
				
				rs.close ();
				pstmt.close ();
				pstmt = null;
				
		}
		catch (Exception e)
		{
			System.out.println ("* Error * P_Assignment_Param - get - " + sql + " - " + e);
		}

		if ( iP_Assignment_Param_ID == 0)
		{
			return null;
		}
		
		return P_Assignment_Param.get( ctx, iP_Assignment_Param_ID , trxName);
	}

	public BigDecimal getHoursPerPay( )
	{
		BigDecimal HoursPerPay = Env.ZERO;
		String sql = "Select [dbo].[P_Assignment_HoursPerPay]( " + this.getP_Assignment_Param_ID() + " ) as HoursPerPay " ;

		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				HoursPerPay = rs.getBigDecimal(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println ("* Error * P_Assignment_Param - getHoursPerPay - " + sql + " - " + e);
		}

		return HoursPerPay;
	}

	public BigDecimal getSalaryPerPay( )
	{
		BigDecimal SalaryPerPay = Env.ZERO;
		String sql = "Select [dbo].[P_Assignment_SalaryPerPay]( " + this.getP_Assignment_Param_ID() + " ) as SalaryPerPay " ;

		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				SalaryPerPay = rs.getBigDecimal(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println ("* Error * P_Assignment_Param - getHoursPerPay - " + sql + " - " + e);
		}

		return SalaryPerPay;
	}

	public static P_Assignment_Param getWithEffectIn (Properties ctx, int iP_Assignment_ID, Timestamp EffectIn, String trxName )
	{
		int iP_Assignment_Param_ID = 0;

		String sql = null;
		sql = "Select P_Assignment_Param_ID From P_Assignment_Param ";
		sql +="  Where P_Assignment_Param.IsActive = 'Y' ";
		sql +="    and P_Assignment_ID = " + iP_Assignment_ID; 
		sql +="    and EffectIn = " + DB.TO_DATE( EffectIn )   ;
		sql +="  Order By EffectIn Desc";



//		System.out.println("P_Credits_Param get sql" + sql );
			
		PreparedStatement pstmt = null;
		try
		{
//			    pstmt.setTimestamp(1, EffectIn);
				pstmt = DB.prepareStatement (sql, null);

				ResultSet rs = pstmt.executeQuery ();
				if ( rs.next () )
					iP_Assignment_Param_ID = rs.getInt(1);
				else
					iP_Assignment_Param_ID = 0;
				
				rs.close ();
				pstmt.close ();
				pstmt = null;
				
		}
		catch (Exception e)
		{
			System.out.println ("* Error * P_Assignment_Param - get - " + sql + " - " + e);
		}

		if ( iP_Assignment_Param_ID == 0)
		{
			return null;
		}
		
		return P_Assignment_Param.get( ctx, iP_Assignment_Param_ID , trxName);
	}

	//
	// return le taux horaire en tetant compte des ajustements et des cédules ARTT.
	//
	public BigDecimal getHourly_Rate() 
	{
		P_Assignment Assignment = P_Assignment.get( Env.getCtx(), this.getP_Assignment_ID(), this.get_TrxName());
		m_Annual_Increase = P_Payment_Group.getAnnual_Increase( Env.getCtx(), Assignment.getP_Employee_ID(), this.get_TrxName());

		
   		BigDecimal out_Of_Rate_Contractual = Env.ZERO;
 
   		if ( this.getRemuneration_Method() == null )
   			return super.getHourly_Rate();
   		
   		if ( this.getRemuneration_Method().equals( P_Assignment_Param.REMUNERATION_METHOD_Daily))
   			return super.getDaily_Salary();
   		
   		if ( this.getOut_Of_Rate_Contractual() != null )
   	   		out_Of_Rate_Contractual = this.getOut_Of_Rate_Contractual();

   		if ( this.isOut_Of_Rate() == true  && this.getRemuneration_Method().equals(P_Assignment_Param.REMUNERATION_METHOD_Hourly))
   			return this.getOut_Of_Rate_Hourly_Rate();
//   			return this.getOut_Of_Rate_Salary().add( out_Of_Rate_Contractual );
   		else if ( this.isOut_Of_Rate() == true )
   			return (this.getOut_Of_Rate_Salary().add(out_Of_Rate_Contractual)).divide( m_Annual_Increase, 4, BigDecimal.ROUND_HALF_UP ).divide( this.getWeekly_Hours(),4, BigDecimal.ROUND_HALF_UP );
//   			return (this.getOut_Of_Rate_Salary().add(out_Of_Rate_Contractual)).divide( new BigDecimal(52.18 ), 4, BigDecimal.ROUND_HALF_UP ).divide( this.getWeekly_Hours(),4, BigDecimal.ROUND_HALF_UP );
   		else
   			return  super.getHourly_Rate();
	}
	
	public BigDecimal getAnnual_Salary()
	{
   		BigDecimal out_Of_Rate_Contractual = Env.ZERO;
   		
   		if ( this.getOut_Of_Rate_Contractual() != null )
   	   		out_Of_Rate_Contractual = this.getOut_Of_Rate_Contractual();

		if ( this.isOut_Of_Rate() == true  && this.getRemuneration_Method().equals(P_Assignment_Param.REMUNERATION_METHOD_Hourly))
   			return ((this.getOut_Of_Rate_Salary().add(out_Of_Rate_Contractual)).multiply( m_Annual_Increase ).multiply(this.getWeekly_Hours())).setScale(2, BigDecimal.ROUND_HALF_UP) ;
//			return ((this.getOut_Of_Rate_Salary().add(out_Of_Rate_Contractual)).multiply( new BigDecimal(52.18 ) ).multiply(this.getWeekly_Hours())).setScale(2, BigDecimal.ROUND_HALF_UP) ;
   		else if ( this.isOut_Of_Rate() == true )
   			return this.getOut_Of_Rate_Salary().add(out_Of_Rate_Contractual);
   		else
   			return super.getAnnual_Salary();
	}

	
	/**
	 * 	Before Delete.
	 *	@return true 
	 */
	protected boolean beforeDelete ()
	{
		
		// audit trail 
		P_Assignment Assignment = P_Assignment.get( Env.getCtx(), this.getP_Assignment_ID(), this.get_TrxName());
		
		if ( PgiUtil.getSolsticeParameter(Env.getCtx(), "AuditTrailInsurance").equals("True")  )
			P_Audit_Trail_Insurance.setInsurableSalary( Assignment.getP_Employee_ID()  );
		
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
		{
			P_Assignment Assignment = P_Assignment.get( Env.getCtx(), this.getP_Assignment_ID(), this.get_TrxName());
			P_Audit_Trail_Insurance.InsurableSalary_Audit( Assignment.getP_Employee_ID() );

		}
			
		return success;
	}	//	afterDelete
	

	public void update_salary_info()
	{
		if ( this.getHourly_Rate().compareTo(Env.ZERO) == 0 && this.getRemuneration_Method().equals(P_Assignment_Param.REMUNERATION_METHOD_Annual))
		{
			BigDecimal HourlyRate = Env.ZERO;
            P_Assignment Assignment = P_Assignment.get(Env.getCtx(), this.getP_Assignment_ID(), null);
    		P_Employee Employee = P_Employee.get(Env.getCtx(), Assignment.getP_Employee_ID(), null);
    		P_Period Period = P_Period.getOpenPeriod(Env.getCtx(), Employee.getP_Payment_Group_ID(), null);
    		P_Frequency Frequency = P_Frequency.get(Env.getCtx(), Period.getP_Frequency_ID(), null ); 

            // Bimensuel
        	if ( Frequency.getNumberOfPeriod() == 24)
        	{
        		BigDecimal NumberOfPeriod = new BigDecimal( Frequency.getNumberOfPeriod() );
        		BigDecimal HoursPerPay = this.getWeekly_Hours().multiply( m_Annual_Increase ).divide(NumberOfPeriod,2,BigDecimal.ROUND_HALF_UP);
//2008-04-09
        		if ( HoursPerPay.compareTo(Env.ZERO) != 0)
        			HourlyRate = this.getAnnual_Salary().divide(HoursPerPay, 8, BigDecimal.ROUND_HALF_UP).divide( new BigDecimal(24) , 8, BigDecimal.ROUND_HALF_UP);
        	}
        	else
        	{
                HourlyRate = this.getAnnual_Salary().divide( m_Annual_Increase , 8, BigDecimal.ROUND_HALF_UP).divide(this.getWeekly_Hours(), 8, BigDecimal.ROUND_HALF_UP);
        	}

        	HourlyRate = HourlyRate.setScale(4, BigDecimal.ROUND_HALF_UP);
        	this.setHourly_Rate(HourlyRate);

		}
		
		if ( this.getAnnual_Salary().compareTo(Env.ZERO) == 0 && this.getRemuneration_Method().equals(P_Assignment_Param.REMUNERATION_METHOD_Hourly) )
		{
            BigDecimal AnnualSalary = Env.ZERO;
            AnnualSalary = (this.getWeekly_Hours().multiply(this.getHourly_Rate()).multiply( m_Annual_Increase )).setScale(2, BigDecimal.ROUND_HALF_UP);
        	this.setAnnual_Salary(AnnualSalary);
		}

	}
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true or false
	 */

	protected boolean beforeSave (boolean newRecord)
	{
		// Calcul hourly rate oder salary 
		update_salary_info();
		
		if ( ! this.isOut_Of_Rate() )
		{
			this.setOut_Of_Rate_Contractual(null);
			this.setOut_Of_Rate_Salary(null);
		}
		else
		{
			if ( this.getRemuneration_Method().equals( P_Assignment_Param.REMUNERATION_METHOD_Hourly ))
			{
				this.setOut_Of_Rate_Salary( this.getOut_Of_Rate_Hourly_Rate().multiply( this.getWeekly_Hours() ).multiply(new BigDecimal( 52 )).setScale(2, BigDecimal.ROUND_HALF_UP) );
			}
				
		}

		//	Validation des bornes de salaire si l'echelle salarial est par borne.
        if ( ! this.isOut_Of_Range() && ! this.isOut_Of_Rate() )
        {
    		if ( this.getP_Salary_Scale_ID() != 0)
    		{
    			P_Salary_Scale SalaryScale = P_Salary_Scale.get(Env.getCtx(), this.getP_Salary_Scale_ID(), null );
    			P_Salary_Scale_Detail SalaryScaleDetail = P_Salary_Scale_Detail.get(Env.getCtx(), this.getP_Salary_Scale_Detail_ID(), null);

    			if ( SalaryScale.getScale_Type().equals(P_Salary_Scale.SCALE_TYPE_AccordingToLimits))
    			{
    				if ( this.getAnnual_Salary().compareTo( SalaryScaleDetail.getSalary_Limit_Min()) < 0 || 
    					 this.getAnnual_Salary().compareTo( SalaryScaleDetail.getSalary_Limit_Max()) > 0 )
    				{
    					Object[] args = new Object[] { SalaryScaleDetail.getSalary_Limit_Min().toString(),  SalaryScaleDetail.getSalary_Limit_Max().toString()};
    					s_log.saveError("ValidationError", Msg.getMsg( getCtx(), "P_SalaryScaleBorn", args));
//    					s_log.log(Level.SEVERE,"ValidationError", Msg.getMsg( getCtx(), "P_SalaryScaleBorn", args));
    					return false;
    				}
    			}
    				
    		}
        }
		
		// audit trail 
		P_Assignment Assignment = P_Assignment.get( Env.getCtx(), this.getP_Assignment_ID(), this.get_TrxName());
		if ( PgiUtil.getSolsticeParameter(Env.getCtx(), "AuditTrailInsurance").equals("True")  )
			P_Audit_Trail_Insurance.setInsurableSalary( Assignment.getP_Employee_ID() );

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
		P_Assignment Assignment = P_Assignment.get( Env.getCtx(), this.getP_Assignment_ID(), this.get_TrxName());
		P_Audit_Trail_Insurance.InsurableSalary_Audit( Assignment.getP_Employee_ID(), this.getEffectIn(), this.getEffectTo() );

	    //on va chercher l'objet precedent pour mettre sa date de fin a jour
	    String sql = "SELECT P_Assignment_Param_ID "
	    	+ " FROM P_Assignment_Param "
	    	+ " WHERE P_Assignment_ID = " + this.getP_Assignment_ID()
	    	+ " AND P_Assignment_Param_ID <> " + this.getP_Assignment_Param_ID()
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
		    	P_Assignment_Param assignment_Param = P_Assignment_Param.get(Env.getCtx(), rs.getInt(1), m_trxName);
		    	if(assignment_Param == null || assignment_Param.getP_Assignment_Param_ID() <= 0)
		    	{
		    		return success;
		    	}
		    	Timestamp tsDate = this.getEffectIn();
		    	// - 1 pour la veille
		    	tsDate = TimeUtil.addDays( tsDate, -1);
		    	assignment_Param.setEffectTo(tsDate);
		    	assignment_Param.save();
			    log.info("afterSave - New=" + newRecord + ", Success=" + success + " ***");
		    }
	    }
	    catch(SQLException e)
	    {
	    	s_log.log(Level.SEVERE,"P_Assignment_SpecialRate - afterSave - " + sql, e);	    	
	    }


		return success;
	}	//	afterSave

	private BigDecimal nvl ( BigDecimal amount )
	{
	    if ( amount == null )
	        return Env.ZERO;
	    
	    return amount;
	}
	private void setTrxName ( String trxName)
	{
		m_trxName = trxName;
	}
	
	public BigDecimal getHourlyRate( P_Gain Gain, Timestamp StartDate )
	{
		BigDecimal HourlyRate = null;
		
		if ( Gain == null )
			return Env.ZERO;
			
		if ( Gain.getType_Rate() != null )
		{
			if ( Gain.getType_Rate().endsWith( P_Gain.TYPE_RATE_EmployeeAffectation))
			{
		    	HourlyRate = this.getHourly_Rate();
			}

			if ( Gain.getType_Rate().endsWith( P_Gain.TYPE_RATE_FixedRate))
			{
		    	HourlyRate = Gain.getHourly_Rate();
			}
				
			
			if ( Gain.getType_Rate().endsWith( P_Gain.TYPE_RATE_Overtime))
			{
				P_Assignment Assignment = P_Assignment.get( Env.getCtx(), this.getP_Assignment_ID() ,null);
				P_Employee Employee = P_Employee.get( Env.getCtx(), Assignment.getP_Employee_ID(), null);
				HourlyRate = Employee.getOverTimeRate();
			}
			
			if ( Gain.getType_Rate().endsWith( P_Gain.TYPE_RATE_VacanceTauxEmploye))
			{
				P_Assignment Assignment = P_Assignment.get( Env.getCtx(), this.getP_Assignment_ID() ,null);
				P_Employee Employee = P_Employee.get( Env.getCtx(), Assignment.getP_Employee_ID(), null);
				HourlyRate = Employee.getvacationHourlyRate();

				//2024-03-20
		    	if ( HourlyRate == null || HourlyRate.compareTo(Env.ZERO) == 0)
		    		HourlyRate = this.getHourly_Rate();

				
			}
			
			
			
			

			if ( Gain.getType_Rate().endsWith( P_Gain.TYPE_RATE_AccumulatedDays))
			{
				P_Assignment Assignment = P_Assignment.get( Env.getCtx(), this.getP_Assignment_ID() ,null);
				P_Employee Employee = P_Employee.get( Env.getCtx(), Assignment.getP_Employee_ID(), null);
				HourlyRate = Employee.getAccumulatedDaysRate();
			}

			if ( Gain.getType_Rate().endsWith( P_Gain.TYPE_RATE_ShortTermDisability))
			{
				P_Assignment Assignment = P_Assignment.get( Env.getCtx(), this.getP_Assignment_ID() ,null);
				P_Employee Employee = P_Employee.get( Env.getCtx(), Assignment.getP_Employee_ID(), null);
				HourlyRate = Employee.getShortTermDisabilityRate();
			}

			if ( Gain.getType_Rate().endsWith( P_Gain.TYPE_RATE_CSSTWSIB))
			{
				P_Assignment Assignment = P_Assignment.get( Env.getCtx(), this.getP_Assignment_ID() ,null);
				P_Employee Employee = P_Employee.get( Env.getCtx(), Assignment.getP_Employee_ID(), null);
				HourlyRate = Employee.getCsstRate();
			}

		}
			
		P_Gain_Parameter GainParameter = P_Gain_Parameter.get( Env.getCtx (), Gain.getP_Gain_ID(), this.getP_Collective_Labour_Agr_ID(), null );
		if ( HourlyRate != null)
			HourlyRate = ((BigDecimal)HourlyRate).multiply( GainParameter.getMultiplyRate()).setScale(6,BigDecimal.ROUND_HALF_UP);
				
		switch ( Gain.getP_Method_Gain_ID() ) {
		case 102: 	
			HourlyRate = Env.ZERO;
			break;
		case 103: 	
			// Avance net data entry
			HourlyRate = Env.ZERO;
			break;
		case 104: 	
			// gain no remunerate
			HourlyRate = Env.ZERO;
			break;
		case 105: 	
			// Bonus of Quantity
			HourlyRate = Env.ZERO;
			break;
		case 106: 	
			// Bonus fixe amount
			HourlyRate = Env.ZERO;
			break;
		case 108:   
			// Sld of the accumulated credit ( Money )
			HourlyRate = Env.ZERO;
			break;
		case 109:   
			// avance net caculated
			HourlyRate = Env.ZERO;
			break;
		case 110:   
			// Gain in money
			HourlyRate = Env.ZERO;
			break;
		case 114:   
			// Gain avec taux spéciaux.
			P_Assignment Assignment = new P_Assignment( Env.getCtx (), this.getP_Assignment_ID(), null );	
			BigDecimal SpecialRate = Assignment.getSpecialRate( Env.getCtx(), Gain.getP_SpecialRate_ID(), StartDate, null ); 
			HourlyRate = SpecialRate;
			break;
		case 107:   
			HourlyRate = Env.ZERO;
			break;
		case 113:   
			// Traitement différé.
			HourlyRate = Env.ZERO;
			break;
		case 116:   
			HourlyRate = Env.ZERO;
			break;
		}
		
		return HourlyRate;
		
	}

	//2009.01.22
	public int getP_Collective_Labour_Agr_ID() 
	{
		//TODO add Parameter
		if ( Env.getAD_Client_ID(Env.getCtx()) == 11)
			return super.getP_Collective_Labour_Agr_ID();
		else
		{
            P_Assignment Assignment = P_Assignment.get(Env.getCtx(), this.getP_Assignment_ID(), null);
    		P_Employee Employee = P_Employee.get(Env.getCtx(), Assignment.getP_Employee_ID(), null);
			return Employee.getP_Collective_Labour_Agr_ID();
		}
	}


}
