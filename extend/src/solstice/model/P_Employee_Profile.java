package solstice.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MLocation;
import org.compiere.model.MRegion;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

import solstice.process.PgiUtil;

/**
 *  Bonus Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Employee_Profile extends X_P_Employee_Profile
{
	
	/**
	 * 	Get Bonus
	 *	@param ctx context
	 * 	@param P_Employee_Profile_ID id
	 *	@return Bonus
	 */
	public static P_Employee_Profile get (Properties ctx, int P_Employee_Profile_ID, String trxName)
	{
		Integer key = new Integer (P_Employee_Profile_ID);
		P_Employee_Profile employee_Profile = (P_Employee_Profile)s_cache.get(key);
		if (employee_Profile != null)
			return employee_Profile;
		employee_Profile = new P_Employee_Profile (ctx, P_Employee_Profile_ID, trxName);
		s_cache.put (key, employee_Profile);
		if(employee_Profile != null)
		{
			employee_Profile.setTrxName(trxName);
		}
		return employee_Profile;
	}	//	get

	public static P_Employee_Profile get (Properties ctx, int EmployeeID, int profileID, Timestamp EffectIn, String trxName)
	{
	    String sql = "SELECT  P_Employee_Profile_ID FROM P_Employee_Profile "
            + " WHERE P_Employee_ID = " + EmployeeID
            + "   AND P_Profile_ID  = " + profileID
			+ "    and EffectIn<= " + DB.TO_DATE( EffectIn )  
			+ "  Order By EffectIn Desc";
	    
	    PreparedStatement pstmt = null;
    
	    int P_Employee_Profile_ID = 0;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				P_Employee_Profile_ID = rs.getInt(1);
			}
			    
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			
		}
		catch (Exception e)
		{
		    System.out.println( "P_Employee_Profile, get - " + e);
		    return null;
		}
	    
		if ( P_Employee_Profile_ID == 0 )
		   return null;
		
		P_Employee_Profile employee_Profile = P_Employee_Profile.get( ctx, P_Employee_Profile_ID, trxName );
		return employee_Profile;
	}	//	get

	
	/**	Cache						*/
	private static CCache<Integer,P_Employee_Profile>	s_cache = new CCache<Integer,P_Employee_Profile>("P_Employee_Profile", 20);

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Employee_LongTermLeave.class);

	private String				m_trxName = null;
	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Employee_Profile_ID id
	 */
	public P_Employee_Profile (Properties ctx, int P_Employee_Profile_ID, String trxName)
	{
		super (ctx, P_Employee_Profile_ID, trxName);
		if (P_Employee_Profile_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
		m_trxName = trxName;
	}	//	P_Employee_Profile

	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Employee_Profile (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
		m_trxName = trxName;
	}
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Employee_Profile (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Employee_Profile_ID"), trxName);
		m_trxName = trxName;
	}	//	P_Employee_Profile

	
	public static P_Employee_Profile getProfileInsurance (Properties ctx, int EmployeeID, Timestamp EffectIn, String trxName)
	{
	    String sql = "SELECT  P_Employee_Profile_ID FROM P_Employee_Profile "
            + " WHERE P_Employee_ID = " + EmployeeID
            + "   AND P_Profile_ID  in (  select P_Profile_ID from P_Profile where IsInsuranceProfile = 'Y' ) "
			+ "    and EffectIn<= " + DB.TO_DATE( EffectIn )  
			+ "  Order By EffectIn Desc";
	    
	    PreparedStatement pstmt = null;
    
	    int P_Employee_Profile_ID = 0;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				P_Employee_Profile_ID = rs.getInt(1);
			}
			    
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			
		}
		catch (Exception e)
		{
		    System.out.println( "P_Employee_Profile, get - " + e);
		    return null;
		}
	    
		if ( P_Employee_Profile_ID == 0 )
		   return null;
		
		P_Employee_Profile employee_Profile = P_Employee_Profile.get( ctx, P_Employee_Profile_ID, trxName );
		return employee_Profile;
	}	//	get


    //
    // Vérifie qu'il n'y a pas un autre profil actif pour l'employé qui contient déjà cette déduction.
    //
    // type : 1 = Deduction
    //        2 = Credits
    //        3 = Taxable Benefit
    //        4 = Bonus
    //
    private boolean activeOtherProfile( int Profile_ID, int Employee_ID, int record_ID, int type )
    {
    	boolean ret = false;
    	String sql = null;
    	
    	
    	if ( type == 1 )
    	{
        	sql = "SELECT  1 FROM P_Profile_Deduction , P_Employee_Profile"
    	        + " 		WHERE P_Employee_Profile.P_Profile_ID <> " + Profile_ID
    	        + "           AND P_Employee_Profile.P_Profile_ID = P_Profile_Deduction.P_Profile_ID"
    	        + "           AND P_Employee_Profile.P_Employee_ID = " + Employee_ID
    	        + "       	  AND P_Profile_Deduction.P_Deduction_ID = " + record_ID
                + "  AND P_Employee_Profile.EffectTo is null "
                + "  AND P_Employee_Profile.IsActive = 'Y' "
                + "  AND P_Profile_Deduction.IsActive = 'Y' "
            ;
    		
    	}

    	if ( type == 2 )
    	{
        	sql = "SELECT  1 FROM P_Profile_Credits , P_Employee_Profile"
    	        + " 		WHERE P_Employee_Profile.P_Profile_ID <> " + Profile_ID
    	        + "           AND P_Employee_Profile.P_Profile_ID = P_Profile_Credits.P_Profile_ID"
    	        + "           AND P_Employee_Profile.P_Employee_ID = " + Employee_ID
    	        + "       	  AND P_Profile_Credits.P_Credits_ID = " + record_ID
                + "  AND P_Employee_Profile.EffectTo is null "
                + "  AND P_Employee_Profile.IsActive = 'Y' "
                + "  AND P_Profile_Credits.IsActive = 'Y' "
            ;
    		
    	}

    	if ( type == 3 )
    	{
        	sql = "SELECT  1 FROM P_Profile_Taxable_Benefit , P_Employee_Profile"
    	        + " 		WHERE P_Employee_Profile.P_Profile_ID <> " + Profile_ID
    	        + "           AND P_Employee_Profile.P_Profile_ID = P_Profile_Taxable_Benefit.P_Profile_ID"
    	        + "           AND P_Employee_Profile.P_Employee_ID = " + Employee_ID
    	        + "       	  AND P_Profile_Taxable_Benefit.P_Taxable_Benefit_ID = " + record_ID
                + "  AND P_Employee_Profile.EffectTo is null "
                + "  AND P_Employee_Profile.IsActive = 'Y' "
                + "  AND P_Profile_Taxable_Benefit.IsActive = 'Y' "
            ;
    		
    	}


    	if ( type == 4 )
    	{
        	sql = "SELECT  1 FROM P_Profile_Bonus , P_Employee_Profile"
    	        + " 		WHERE P_Employee_Profile.P_Profile_ID <> " + Profile_ID
    	        + "           AND P_Employee_Profile.P_Profile_ID = P_Profile_Bonus.P_Profile_ID"
    	        + "           AND P_Employee_Profile.P_Employee_ID = " + Employee_ID
    	        + "       	  AND P_Profile_Bonus.P_Bonus_ID = " + record_ID
                + "  AND P_Employee_Profile.EffectTo is null "
                + "  AND P_Employee_Profile.IsActive = 'Y' "
                + "  AND P_Profile_Bonus.IsActive = 'Y' "
            ;
    		
    	}

		PreparedStatement pstmt = null;
	    try
	    {
	    	pstmt = DB.prepareStatement (sql, null);
	    	ResultSet rs = pstmt.executeQuery ();
	    	if (rs.next ())
	    	{
	    		ret = true;
	    	}
	    	rs.close ();
	    	pstmt.close ();
	    	pstmt = null;
	    }
	    catch (Exception e)
	    {
		    System.out.println( "P_Employee_Profile, update - " + e);
	    	return false;
	    }

	    return ret;
    }

	
    public boolean UpdateProfileCredits( int Profile_ID, int Employee_ID,  Timestamp EffectIn, Timestamp EffectTo, boolean active,  String trxName )
    {
	    String sql = "SELECT  P_Employee_Credits_ID, P_Employee_Credits.P_Credits_ID FROM P_PROFILE_CREDITS , P_Employee_Credits "
	        + " 		WHERE P_PROFILE_ID = " + Profile_ID
	        + "           AND P_Employee_ID = " + Employee_ID
	        + "       	  AND P_Employee_Credits.P_Credits_ID = P_Profile_Credits.P_Credits_ID   "
	        + "  AND P_Employee_Credits.EffectIn = ( Select Max( EffectIn ) From P_Employee_Credits m where m.P_Employee_ID = P_Employee_Credits.P_Employee_ID AND m.P_Credits_ID = P_Employee_Credits.P_Credits_ID )"
	        + "  AND P_Profile_Credits.IsActive = 'Y'"
	        ;

		PreparedStatement pstmt = null;
	    try
	    {
	    	pstmt = DB.prepareStatement (sql, null);
	    	ResultSet rs = pstmt.executeQuery ();
	    	while (rs.next ())
	    	{
	    		if ( activeOtherProfile( Profile_ID, Employee_ID, rs.getInt("P_Credits_ID"), 2) == false)
	    		{
		    		P_Employee_Credits EmployeeCredits = new P_Employee_Credits( Env.getCtx(), rs.getInt("P_Employee_Credits_ID") ,trxName);
//		    	    EmployeeCredits.setEffectIn( EffectIn );

//		    		if ( EffectTo != null )
		    			EmployeeCredits.setEffectTo(EffectTo);
		    		
		    	    EmployeeCredits.setIsActive(active);
		    	    EmployeeCredits.save();
	    			
	    		}
	    	    
	    	}
	    	rs.close ();
	    	pstmt.close ();
	    	pstmt = null;
	    }
	    catch (Exception e)
	    {
		    System.out.println( "P_Employee_Profile, update - " + e);
		    return false;
	    }

    	return true;
    }

    
    
    public boolean UpdateProfileDeduction     ( int Profile_ID, int Employee_ID,  Timestamp EffectIn, Timestamp EffectTo, boolean active,  String trxName)
    {
		P_Employee Employee = P_Employee.get( Env.getCtx(), Employee_ID, null );
    	
	    String sql = "SELECT  P_Employee_Deduction_ID, P_Employee_Deduction.P_Deduction_ID "
	    		+ " FROM P_PROFILE_DEDUCTION "
	    		+ " INNER JOIN P_Employee_Deduction on P_Employee_Deduction.P_Deduction_ID = P_Profile_Deduction.P_Deduction_ID  "
	        + " 		WHERE P_PROFILE_ID = " + Profile_ID
	        + "           AND P_Employee_ID = " + Employee_ID
	        + "  AND P_Employee_Deduction.EffectIn = ( Select Max( EffectIn ) From P_Employee_Deduction m where m.P_Employee_ID = P_Employee_Deduction.P_Employee_ID AND m.P_Deduction_ID = P_Employee_Deduction.P_Deduction_ID )"
	        + " AND P_PROFILE_DEDUCTION.ad_org_id in ( 0, " + Employee.getAD_Org_ID() + " )"
 	        + "  AND ( P_PROFILE_DEDUCTION.P_COLLECTIVE_LABOUR_AGR_ID IS NULL OR P_PROFILE_DEDUCTION.P_COLLECTIVE_LABOUR_AGR_ID = " + Employee.getP_Collective_Labour_Agr_ID() + " ) "

            + "  AND P_Profile_Deduction.IsActive = 'Y'"
	                ;

		PreparedStatement pstmt = null;
	    try
	    {
	    	pstmt = DB.prepareStatement (sql, null);
	    	ResultSet rs = pstmt.executeQuery ();
	    	while (rs.next ())
	    	{
	    		
	    		if ( activeOtherProfile( Profile_ID, Employee_ID, rs.getInt("P_Deduction_ID"), 1) == false)
	    		{
		    		P_Employee_Deduction EmployeeDeduction = new P_Employee_Deduction( Env.getCtx(), rs.getInt("P_Employee_Deduction_ID") ,trxName);
//		    	    EmployeeDeduction.setEffectIn( EffectIn );
//		    		if ( EffectTo != null )
		    			EmployeeDeduction.setEffectTo(EffectTo);
		    	    EmployeeDeduction.setIsActive(active);
		    	    
		    	    P_Deduction Deduction = P_Deduction.get( Env.getCtx(), EmployeeDeduction.getP_Deduction_ID(), trxName);
		    	    if ( Deduction.getValue().equals( "DI23" ) )
		    	    	EmployeeDeduction.setMultiplyRate( new BigDecimal(2.0));

		    	    if ( Deduction.getValue().equals( "DI25"))
		    	    	EmployeeDeduction.setMultiplyRate( new BigDecimal(1.0));

		    	    EmployeeDeduction.save();
	    			
	    		}
	    	    
	    	}
	    	rs.close ();
	    	pstmt.close ();
	    	pstmt = null;
	    }
	    catch (Exception e)
	    {
		    System.out.println( "P_Employee_Profile, update - " + e);
	    	return false;
	    }

    	return true;
    }

    
    public boolean UpdateProfileTaxableBenefit( int Profile_ID, int Employee_ID,  Timestamp EffectIn, Timestamp EffectTo, boolean active,  String trxName)
    {
	    String sql = "SELECT  P_Employee_Taxable_Benefit_ID, P_Employee_Taxable_Benefit.P_Taxable_Benefit_ID FROM P_PROFILE_Taxable_Benefit , P_Employee_Taxable_Benefit "
	        + " 		WHERE P_PROFILE_ID = " + Profile_ID
	        + "           AND P_Employee_ID = " + Employee_ID
	        + "       	  AND P_Employee_Taxable_Benefit.P_Taxable_Benefit_ID = P_Profile_Taxable_Benefit.P_Taxable_Benefit_ID   "
            + "  AND P_Employee_Taxable_Benefit.EffectIn = ( Select Max( EffectIn ) From P_Employee_Taxable_Benefit m where m.P_Employee_ID = P_Employee_Taxable_Benefit.P_Employee_ID AND m.P_Taxable_Benefit_ID = P_Employee_Taxable_Benefit.P_Taxable_Benefit_ID )"
//            + "  AND P_Profile_Taxable_Benefit.IsActive = 'Y'"
            ;
		PreparedStatement pstmt = null;
	    try
	    {
	    	pstmt = DB.prepareStatement (sql, null);
	    	ResultSet rs = pstmt.executeQuery ();
	    	while (rs.next ())
	    	{
	    		if ( activeOtherProfile( Profile_ID, Employee_ID, rs.getInt("P_Taxable_Benefit_ID"), 3) == false)
	    		{
		    		P_Employee_Taxable_Benefit EmployeeTaxable_Benefit = new P_Employee_Taxable_Benefit( Env.getCtx(), rs.getInt("P_Employee_Taxable_Benefit_ID") ,trxName);
//		    	    EmployeeTaxable_Benefit.setEffectIn( EffectIn );
//		    		if ( EffectTo != null )
		    			EmployeeTaxable_Benefit.setEffectTo(EffectTo);
		    	    EmployeeTaxable_Benefit.setIsActive(active);
		    	    EmployeeTaxable_Benefit.save();
	    			
	    		}
	    	    
	    	}
	    	rs.close ();
	    	pstmt.close ();
	    	pstmt = null;
	    }
	    catch (Exception e)
	    {
		    System.out.println( "P_Employee_Profile, update - " + e);
	    	return false;
	    }

    	return true;
    }
    public boolean UpdateProfileBonus		 ( int Profile_ID, int Employee_ID,  Timestamp EffectIn, Timestamp EffectTo, boolean active,  String trxName)
    {
	    String sql = "SELECT  P_Employee_Bonus_ID, P_Employee_Bonus.P_Bonus_ID FROM P_PROFILE_Bonus , P_Employee_Bonus "
	        + " 		WHERE P_PROFILE_ID = " + Profile_ID
	        + "           AND P_Employee_ID = " + Employee_ID
	        + "       	  AND P_Employee_Bonus.P_Bonus_ID = P_Profile_Bonus.P_Bonus_ID   "
        	+ "  AND P_Employee_Bonus.EffectIn = ( Select Max( EffectIn ) From P_Employee_Bonus m where m.P_Employee_ID = P_Employee_Bonus.P_Employee_ID AND m.P_Bonus_ID = P_Employee_Bonus.P_Bonus_ID )"
            + "  AND P_Profile_Bonus.IsActive = 'Y'"
        	;
		PreparedStatement pstmt = null;
	    try
	    {
	    	pstmt = DB.prepareStatement (sql, null);
	    	ResultSet rs = pstmt.executeQuery ();
	    	while (rs.next ())
	    	{
	    		if ( activeOtherProfile( Profile_ID, Employee_ID, rs.getInt("P_Bonus_ID"), 4) == false)
	    		{
		    		P_Employee_Bonus EmployeeBonus = new P_Employee_Bonus( Env.getCtx(), rs.getInt("P_Employee_Bonus_ID") ,trxName);
//		    	    EmployeeBonus.setEffectIn( EffectIn );
//		    		if ( EffectTo != null )
		    			EmployeeBonus.setEffectTo(EffectTo);
		    	    EmployeeBonus.setIsActive(active);
		    	    EmployeeBonus.save();
	    			
	    		}
	    	    
	    	}
	    	rs.close ();
	    	pstmt.close ();
	    	pstmt = null;
	    }
	    catch (Exception e)
	    {
		    System.out.println( "P_Employee_Profile, update - " + e);
	    	return false;
	    }

    	return true;
		
    }

	
	public static boolean CreateProfileDeduction( int Profile_ID, int Employee_ID, Timestamp EffectIn, Timestamp EffectTo, String trxName )
	{
		P_Employee Employee = P_Employee.get( Env.getCtx(), Employee_ID, null );
		
	    String sql = "SELECT  P_Deduction_ID FROM P_PROFILE_DEDUCTION pd "
	        + " 		WHERE P_PROFILE_ID = " + Profile_ID
	        + " 		AND NOT EXISTS ( SELECT 1 FROM P_EMPLOYEE_DEDUCTION de " 
	        + "       						WHERE de.P_Employee_ID = " + Employee_ID 
	        + "         					AND de.P_Deduction_ID = pd.P_Deduction_ID  ) "
	        + " AND pd.ad_org_id in ( 0, " + Employee.getAD_Org_ID() + " )"
	     	+ " AND ( pd.P_COLLECTIVE_LABOUR_AGR_ID IS NULL OR pd.P_COLLECTIVE_LABOUR_AGR_ID = " + Employee.getP_Collective_Labour_Agr_ID() + " ) "

            + "  AND pd.IsActive = 'Y'"
	        
	        ;

		PreparedStatement pstmt = null;
	    try
	    {
	    	pstmt = DB.prepareStatement (sql, null);
	    	ResultSet rs = pstmt.executeQuery ();
	    	while (rs.next ())
	    	{
	    		P_Employee_Deduction EmployeeDeduction = new P_Employee_Deduction( Env.getCtx(), -1 ,trxName);
	    	    EmployeeDeduction.setP_Employee_ID( Employee_ID );
	    	    EmployeeDeduction.setP_Deduction_ID( rs.getInt(1));
	    	    EmployeeDeduction.setEffectIn( EffectIn );
	    		if ( EffectTo != null )
	    			EmployeeDeduction.setEffectTo(EffectTo);
	    	    EmployeeDeduction.save();
	    	    
	    	}
	    	rs.close ();
	    	pstmt.close ();
	    	pstmt = null;
	    }
	    catch (Exception e)
	    {
		    System.out.println( "P_Employee_Profile, Create - " + e);
	    	return false;
	    }
	    return true;
	}



	
	public static boolean CreateProfileCredits( int Profile_ID, int Employee_ID, Timestamp EffectIn, Timestamp EffectTo, String trxName )
	{
	    String sql = "SELECT P_Credits_ID, P_Method_Credits_ID FROM P_PROFILE_CREDITS pc "
	           + " 		WHERE P_PROFILE_ID = " + Profile_ID 
	           + " 		AND NOT EXISTS ( SELECT 1 FROM P_EMPLOYEE_CREDITS cr " 
	           + "      					WHERE Cr.P_Employee_ID = " + Employee_ID 
	           + "        					AND Cr.P_Credits_ID = pc.P_Credits_ID  ) "
	           + "  AND pc.IsActive = 'Y'"
	           ;

		PreparedStatement pstmt = null;

		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
			    P_Employee_Credits EmployeeCredits = new P_Employee_Credits( Env.getCtx(), -1 , trxName);
			    EmployeeCredits.setP_Employee_ID( Employee_ID );
			    EmployeeCredits.setP_Credits_ID( rs.getInt("P_Credits_ID"));
			    EmployeeCredits.setEffectIn( EffectIn );
	    		if ( EffectTo != null )
	    			EmployeeCredits.setEffectTo(EffectTo);
			    EmployeeCredits.setP_Method_Credits_ID( rs.getInt("P_Method_Credits_ID"));
			    EmployeeCredits.save();
			    
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
		    System.out.println( "P_Employee_Profile, Create - " + e);
			return false;
		}
	    return true;
	}

	
	public static boolean CreateProfileTaxableBenefit( int Profile_ID, int Employee_ID, Timestamp EffectIn, Timestamp EffectTo, String trxName )
	{
	    String sql = "SELECT  P_Taxable_Benefit_ID, Taxb_Fixed_Amount FROM P_PROFILE_TAXABLE_BENEFIT pd "
            + " 		WHERE P_PROFILE_ID = " + Profile_ID 
            + " 		AND NOT EXISTS ( SELECT 1 FROM P_EMPLOYEE_TAXABLE_BENEFIT de " 
            + "      						WHERE de.P_Employee_ID = " + Employee_ID
            + "        						AND de.P_Taxable_Benefit_ID = pd.P_Taxable_Benefit_ID  ) "
	        + "  AND pd.IsActive = 'Y'"
            ;
	    
	    PreparedStatement pstmt = null;
   
		try
		{
			P_Employee Employee = P_Employee.get( Env.getCtx(), Employee_ID, trxName);
				
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				// Créer seulement les avantages imposables sans région ou dans la même région que l'employé
				P_Taxable_Benefit TaxableBenefit = P_Taxable_Benefit.get( Env.getCtx(), rs.getInt(1), trxName);
				if ( TaxableBenefit.getC_Region_ID() == 0 || TaxableBenefit.getC_Region_ID() == Employee.getRegionResidence().getC_Region_ID() )
				{
				    P_Employee_Taxable_Benefit EmployeeTaxableBenefit = new P_Employee_Taxable_Benefit( Env.getCtx(), -1 , trxName );
				    EmployeeTaxableBenefit.setP_Employee_ID( Employee_ID );
				    EmployeeTaxableBenefit.setP_Taxable_Benefit_ID( rs.getInt(1));
				    EmployeeTaxableBenefit.setEffectIn( EffectIn );
		    		if ( EffectTo != null )
		    			EmployeeTaxableBenefit.setEffectTo(EffectTo);
				    EmployeeTaxableBenefit.setTaxB_Fixed_Amount( rs.getBigDecimal("Taxb_Fixed_Amount") );
				    EmployeeTaxableBenefit.save();
				}
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
		    System.out.println( "P_Employee_Profile, Create - " + e);
			return false;
		}
	    return true;
	}
	
	
	public static boolean CreateProfileBonus( int Profile_ID, int Employee_ID, Timestamp EffectIn, Timestamp EffectTo, String trxName )
	{
	    String sql = "SELECT P_Bonus_ID FROM P_Profile_Bonus b "
            + " 		WHERE P_PROFILE_ID = " + Profile_ID 
            + " 		AND NOT EXISTS ( SELECT 1 FROM P_Employee_Bonus eb " 
            + "      						WHERE eb.P_Employee_ID = " + Employee_ID
            + "        						AND eb.P_Bonus_ID = b.P_Bonus_ID  ) "
	        + "  AND b.IsActive = 'Y'"
            ;
	    
	    PreparedStatement pstmt = null;
   
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
			    P_Employee_Bonus EmployeeBonus = new P_Employee_Bonus( Env.getCtx(), -1 , trxName );
			    EmployeeBonus.setP_Employee_ID( Employee_ID );
			    EmployeeBonus.setP_Bonus_ID( rs.getInt(1));
			    EmployeeBonus.setEffectIn( EffectIn );
	    		if ( EffectTo != null )
	    			EmployeeBonus.setEffectTo(EffectTo);
			    EmployeeBonus.save();
			    
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;		
		}
		catch (Exception e)
		{
		    System.out.println( "P_Employee_Profile, Create - " + e);
			return false;
		}
	    return true;
	}
	
	/**
	 * 	Before Delete.
	 *	@return true 
	 */
	protected boolean beforeDelete ()
	{
		
		// audit trail 
		P_Profile Profile = P_Profile.get( Env.getCtx(), this.getP_Profile_ID(), this.get_TrxName());
		if ( Profile.isInsuranceProfile())
			if ( PgiUtil.getSolsticeParameter(Env.getCtx(), "AuditTrailInsurance").equals("True")  )
				P_Audit_Trail_Insurance.insert_audit(this.getP_Employee_ID(), "P_Profile", Profile.getName(), null  );
		
		return true;
	}	//	beforeDelete



	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true or false
	 */

	protected boolean beforeSave (boolean newRecord)
	{

		// audit trail 
		if ( is_ValueChanged("P_Profile_ID") || newRecord )
		{
			P_Profile Profile = P_Profile.get( Env.getCtx(), this.getP_Profile_ID(), this.get_TrxName());
			if ( Profile.isInsuranceProfile())
				if ( PgiUtil.getSolsticeParameter(Env.getCtx(), "AuditTrailInsurance").equals("True")  )
					P_Audit_Trail_Insurance.insert_audit(this.getP_Employee_ID(), "P_Profile_ID", null, Profile.getName(), this.getEffectIn(), this.getEffectTo() );
		}


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

	        success = CreateProfileCredits       ( this.getP_Profile_ID(),  this.getP_Employee_ID(), this.getEffectIn(), this.getEffectTo(), this.get_TrxName());
	        success = CreateProfileDeduction     ( this.getP_Profile_ID(),  this.getP_Employee_ID(), this.getEffectIn(), this.getEffectTo(), this.get_TrxName());
	        success = CreateProfileTaxableBenefit( this.getP_Profile_ID(),  this.getP_Employee_ID(), this.getEffectIn(), this.getEffectTo(), this.get_TrxName());
	        success = CreateProfileBonus		 ( this.getP_Profile_ID(),  this.getP_Employee_ID(), this.getEffectIn(), this.getEffectTo(), this.get_TrxName());
		}

//	    if (!newRecord)
//		{
	        // If IsActive change, we change the IsActive value for each records defined by the profile (credits, deduction
//	        if (is_ValueChanged("IsActive"))
//	        {
		        Boolean Active = true;
		        if ( this.isActive() ) 
		          Active = true;
		        else
		          Active = false;

		        success = UpdateProfileCredits       ( this.getP_Profile_ID(),  this.getP_Employee_ID(), this.getEffectIn(), this.getEffectTo(), Active, this.get_TrxName());
		        success = UpdateProfileDeduction     ( this.getP_Profile_ID(),  this.getP_Employee_ID(), this.getEffectIn(), this.getEffectTo(), Active, this.get_TrxName());
		        success = UpdateProfileTaxableBenefit( this.getP_Profile_ID(),  this.getP_Employee_ID(), this.getEffectIn(), this.getEffectTo(), Active, this.get_TrxName());
		        success = UpdateProfileBonus		 ( this.getP_Profile_ID(),  this.getP_Employee_ID(), this.getEffectIn(), this.getEffectTo(), Active, this.get_TrxName());

//	        }
//		}
   
//	  on va chercher l'objet precedent pour mettre sa date de fin a jour
	    String sql = "SELECT P_Employee_Profile_ID "
	    	+ " FROM P_Employee_Profile "
	    	+ " WHERE P_Employee_ID = " + this.getP_Employee_ID()
	    	+ " AND P_Profile_ID = " + this.getP_Profile_ID()
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
		    	P_Employee_Profile employee_Profile = P_Employee_Profile.get(Env.getCtx(), rs.getInt(1), m_trxName);
		    	if(employee_Profile == null || employee_Profile.getP_Employee_Profile_ID() <= 0)
		    	{
		    		return success;
		    	}
		    	Timestamp tsDate = this.getEffectIn();
		    	// - 1 pour la veille
		    	tsDate = TimeUtil.addDays( tsDate, -1);
		    	employee_Profile.setEffectTo(tsDate);
		    	employee_Profile.save();
		    }
	    }
	    catch(Exception e)
	    {
	    	s_log.log(Level.SEVERE,"P_Assignment_SpecialRate - afterSave - " + sql, e);	    	
	    }
		//	Propagate Description changes
//		if (is_ValueChanged("Description") || is_ValueChanged("POReference"))
		return true;
	}	//	afterSave

	private void setTrxName ( String trxName)
	{
		m_trxName = trxName;
	}
	
	
    
	public static int get_Employee_Profile( int P_Employee_ID )
	{

		String sql = null;
		sql = "Select P_Employee_Profile_ID "; 
		sql += " From P_Employee_Profile";
		sql += " WHERE P_Employee_Profile.IsActive='Y' ";
		sql += " AND P_Employee_Profile.P_Employee_ID=" + P_Employee_ID;
		sql += " AND P_Employee_Profile.P_Profile_ID=1000041" ;
		

		int iID = 0;

		PreparedStatement pstmt = null;

		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				iID = rs.getInt(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Employee_Profile - get_Employee_Profile - " + sql, e);
		}
			

		return iID;
		
	}

	
}	//	P_Employee_Profile
