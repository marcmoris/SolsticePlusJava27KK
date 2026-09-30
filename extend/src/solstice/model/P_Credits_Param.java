package solstice.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;

import java.util.logging.*;

/**
 *  Credits_Param Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Credits_Param extends X_P_Credits_Param
{
	static final public java.math.BigDecimal ZERO = new java.math.BigDecimal(0.0);

    /**
	 * 	Get Credits_Param
	 *	@param ctx context
	 * 	@param P_Credits_Param_ID id
	 *	@return Credits_Param
	 */
	public static P_Credits_Param get (Properties ctx, int P_Credits_Param_ID, String trxName)
	{
		Integer key = new Integer (P_Credits_Param_ID);
		P_Credits_Param credits_Param = (P_Credits_Param)s_cache.get(key);
		if (credits_Param != null)
			return credits_Param;
		credits_Param = new P_Credits_Param (ctx, P_Credits_Param_ID, trxName);
		s_cache.put (key, credits_Param);
		if(credits_Param != null)
		{
			credits_Param.setTrxName(trxName);
		}
		return credits_Param;
	}	//	get


	/**	Cache						*/
	private static CCache<Integer,P_Credits_Param>	s_cache = new CCache<Integer,P_Credits_Param>("P_Credits_Param", 20);

	/**	Static Logger				*/
	private static CLogger		s_log = CLogger.getCLogger (P_Credits_Param.class);

	private String				m_trxName = null;
	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Credits_Param_ID id
	 */
	public P_Credits_Param (Properties ctx, int P_Credits_Param_ID, String trxName)
	{
		super (ctx, P_Credits_Param_ID, trxName);
		if (P_Credits_Param_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
		m_trxName = trxName;
	}	//	P_Credits_Param

	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Credits_Param (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
		m_trxName = trxName;
	}
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Credits_Param (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Credits_Param_ID"), trxName);
		m_trxName = trxName;
	}	//	P_Credits_Param


	public static P_Credits_Param get (Properties ctx, int Credits_ID, int Method_Credits_ID, Timestamp EffectIn, String trxName )
	{
		int Credits_Param_ID = 0;

		String sql = null;
		sql = "Select P_Credits_Param_ID From P_Credits_Param ";
		sql +="  Where P_Credits_Param.IsActive = 'Y' ";
		sql +="    and P_Credits_ID = " + Credits_ID;
		sql +="    and P_Method_Credits_ID = " + Method_Credits_ID; 
		sql +="    and EffectIn <= " + DB.TO_DATE( EffectIn )   ;
		sql +="  Order By EffectIn Desc";

		//WTF avec le P_Profile_ID = 100????????????????
		String sql2 = null;
		sql2 = "Select P_Credits_Param_ID From P_Credits_Param ";
		sql2 +="  Where P_Credits_Param.IsActive = 'Y' ";
		sql2 +="    and P_Credits_ID = " + Credits_ID;
		sql2 +="    and P_Method_Credits_ID = 1000001 " ;  // Profile * 
//		sql2 +="    and P_Method_Credits_ID = 100 " ;  // Profile * 
		sql2 +="    and EffectIn <= " + DB.TO_DATE( EffectIn )   ;
		sql2 +="  Order By EffectIn Desc";

//		System.out.println("P_Credits_Param get sql" + sql );
			
		PreparedStatement pstmt = null;
		try
		{
//			    pstmt.setTimestamp(1, EffectIn);
				pstmt = DB.prepareStatement (sql, null);
				int index = 1;
				ResultSet rs = pstmt.executeQuery ();
				if ( rs.next () )
					Credits_Param_ID = rs.getInt(1);
				else
					Credits_Param_ID = 0;
				
				rs.close ();
				pstmt.close ();
				pstmt = null;
				
		}
		catch (Exception e)
		{
			System.out.println ("* Error * P_Credits_Param - get - " + sql + " - " + e);
		}

		if ( Credits_Param_ID == 0)
		{
			pstmt = null;
			try
			{
//				    pstmt.setTimestamp(1, EffectIn);
					pstmt = DB.prepareStatement (sql2, null);
					int index = 1;
					ResultSet rs = pstmt.executeQuery ();
					if ( rs.next () )
						Credits_Param_ID = rs.getInt(1);
					else
						Credits_Param_ID = 0;
					
					rs.close ();
					pstmt.close ();
					pstmt = null;
					
			}
			catch (Exception e)
			{
				System.out.println ("* Error * P_Credits_Param - get - " + sql2 + " - " + e);
			}
		}

		if ( Credits_Param_ID == 0)
		{
			return null;
		}
		
		return P_Credits_Param.get( ctx, Credits_Param_ID , trxName);
	}
	

	public Timestamp getCreditsDate( P_Period Period, P_Employee Employee , String trxName)
	{
	    Timestamp CreditsDate = null;

	    P_Year Year = P_Year.get( Env.getCtx(), Period.getP_Year_ID(), trxName );
	    P_Credits Credits = P_Credits.get( Env.getCtx(), this.getP_Credits_ID(), trxName);

		int year   = Year.getYear();
	    int mon    ;
	    int day    ;

	    Calendar utcTime = Calendar.getInstance();
	    Date date;
	    
	    if ( Credits.getCreditsTypeDate().equals("1")  )
	    {
		    
		    mon    = new Integer( Credits.getCreditsMonth() ).intValue();
		    day    = Credits.getCreditsDay();

		    // set calendar to the time
		    utcTime.set( year, mon-1 , day, 0, 0, 0 );
			date = utcTime.getTime();
			
			CreditsDate = new Timestamp( date.getTime() ) ;
	        
	    }
	    if ( Credits.getCreditsTypeDate().equals("2")  )
	    {
	        CreditsDate = Employee.getDateSeniority();
//	        CreditsDate.setYear( year ); 

//		    mon    = CreditsDate.getMonth();
//		    day    = CreditsDate.getDate();

	        mon    = TimeUtil.getMonth(CreditsDate);
		    day    = TimeUtil.getDay_Of_Month( CreditsDate );

		    // set calendar to the time
		    utcTime.set( year, mon-1 , day, 0, 0, 0 );
			date = utcTime.getTime();
			
			CreditsDate = new Timestamp( date.getTime() ) ;

	        
	    }
	    if ( Credits.getCreditsTypeDate().equals("3")  )
	    {
	        CreditsDate = Employee.getDateHired();
//		    mon    = CreditsDate.getMonth();
//		    day    = CreditsDate.getDate();

		    mon    = TimeUtil.getMonth(CreditsDate);
		    day    = TimeUtil.getDay_Of_Month( CreditsDate );

		    // set calendar to the time
		    utcTime.set( year, mon-1 , day, 0, 0, 0 );
			date = utcTime.getTime();
			
			CreditsDate = new Timestamp( date.getTime() ) ;
	    }

	    return CreditsDate;
	}
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true or false
	 */

	protected boolean beforeSave (boolean newRecord)
	{

        P_Credits Credits       = P_Credits.get( Env.getCtx(), getP_Credits_ID(), this.get_TrxName());

//        if ( this.getMethod_Annual().equals( P_Credits_Param.METHOD_ANNUAL_SoldIsPay ) )
        if ( this.getMethod_Annual().equals( P_Credits_Param.METHOD_ANNUAL_SoldIsTransferredInAOthersAccumulatedCredits ))
        {
            if ( this.getP_Credits_Target() != 0  )
            {
                P_Credits CreditsTarget = P_Credits.get( Env.getCtx(), getP_Credits_Target(), this.get_TrxName());
                if (  Credits.getP_UOM_ID() != CreditsTarget.getP_UOM_ID() )
                {
                	s_log.saveError("ValidationError", Msg.translate(getCtx(), "CreditsTargetValidation"));
    			    return false;
                }
            }
        }

        if ( this.getMethod_Annual().equals( P_Credits_Param.METHOD_ANNUAL_SoldIsPay ) )
        {
            if ( this.getGainAnnual() != 0 )
            {
                P_Gain Gain = P_Gain.get( Env.getCtx(), getGainAnnual(), this.get_TrxName() );
                    
                if (  Credits.getP_UOM_ID() != Gain.getP_UOM_ID() )
                {
                	s_log.saveError("ValidationError", Msg.translate(getCtx(), "CreditsGainValidation"));
    			    return false;
                }
            }
        }

        if ( this.getMethod_Parting().equals( P_Credits_Param.METHOD_PARTING_SoldIsPay  ) )
        {
            if ( this.getGainParting() != 0 ) 
            {
                P_Gain Gain = P_Gain.get( Env.getCtx(), getGainParting(), this.get_TrxName() );
                    
                if (  Credits.getP_UOM_ID() != Gain.getP_UOM_ID() )
                {
                	s_log.saveError("ValidationError", Msg.translate(getCtx(), "CreditsGainValidation"));
    			    return false;
                }
            }
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
	    //on va chercher l'objet precedent pour mettre sa date de fin a jour
	    String sql = "SELECT P_Credits_Param_ID "
	    	+ " FROM P_Credits_Param "
	    	+ " WHERE P_Credits_ID = " + this.getP_Credits_ID()
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
		    	P_Credits_Param credits_Param = P_Credits_Param.get(Env.getCtx(), rs.getInt(1), m_trxName);
		    	if(credits_Param == null || credits_Param.getP_Credits_Param_ID() <= 0)
		    	{
		    		return success;
		    	}
		    	Timestamp tsDate = this.getEffectIn();
		    	// - 1 pour la veille
		    	tsDate = TimeUtil.addDays( tsDate, -1);
		    	credits_Param.setEffectTo(tsDate);
		    	credits_Param.save();
		    }
	    }
	    catch(SQLException e)
	    {
	    	s_log.log(Level.SEVERE,"P_Credits_Param - afterSave - " + sql, e);	    	
	    }
	    log.info("afterSave - New=" + newRecord + ", Success=" + success + " ***");
		return success;
	}	//	afterSave

	private BigDecimal nvl ( BigDecimal amount )
	{
	    if ( amount == null )
	        return ZERO;
	    
	    return amount;
	}
	private void setTrxName ( String trxName)
	{
		m_trxName = trxName;
	}
	
	//bon maintenant qu'on a une table pour les banques liés, on redefinie la fonction
	//pour un minimum d'impact de changement
	public P_Credits[] getP_Credits_Join(int iP_Employee_ID)
	{
		
		Vector<P_Credits> creditsVect = new Vector<P_Credits>(3);
		
		String sql = " SELECT cpc.P_Credits_Join "
			+ " FROM P_Credits_Param_Credits  cpc, P_Employee_Credits ec "
			+ " WHERE cpc.IsActive = 'Y' "
			+ " AND ec.IsActive = 'Y' "
			+ " AND cpc.P_Credits_Param_ID = "+this.getP_Credits_Param_ID()
			+ " AND ec.P_Employee_ID = "+iP_Employee_ID
			+ " AND ec.P_Credits_ID = cpc.P_Credits_Join ";
		
		PreparedStatement pstm = DB.prepareStatement(sql, null);
		
		try
		{
			ResultSet rs = pstm.executeQuery();
			while(rs.next())
			{
				int iP_Credits_ID = rs.getInt("P_Credits_Join");
				if(iP_Credits_ID != 0)
				{
					P_Credits credits = P_Credits.get(Env.getCtx(), iP_Credits_ID, null);
					if(credits != null)
					{
						creditsVect.add(credits);
					}
				}
			}
		}
		catch(Exception e)
		{
			s_log.log(Level.SEVERE,"P_Credits_Param - getP_Credits_Join - " + sql, e);
		}
		
		P_Credits[] retValue = new P_Credits[creditsVect.size()];
		for(int idx=0;idx<creditsVect.size();idx++)
		{
			retValue[idx] = (P_Credits)creditsVect.get(idx);
		}
		return retValue;
	}
}	//	P_Credits_Param
