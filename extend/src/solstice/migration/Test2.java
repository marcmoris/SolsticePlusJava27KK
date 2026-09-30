package solstice.migration;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Properties;

import org.compiere.Compiere;
import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.util.Env;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.compiere.util.CLogger;
import org.compiere.util.DB;



//import com.mashape.unirest.http.*;
import solstice.model.*;


public class Test2 {

	Properties ctx = Env.getCtx();
	/**	Logger							*/
	protected CLogger			log = CLogger.getCLogger (getClass());

	/** Transaction			*/
	private String m_trxName = null; // Trx.createTrxName();

	/**	Big Decimal 0	 */
	static final public java.math.BigDecimal ZERO = new java.math.BigDecimal(0.0);

	/** Effective						*/
	private Timestamp		m_DateValue = null;

	private X_I_YTD_Deduction imp;


	
	/**
	 * 	Generate PO Class
	 * 	@param AD_Table_ID table id
	 * 	@param directory directory with \ or / at the end.
	 * 	@param packageName package name
	 * @throws SQLException
	 */
	public Test2 () 
	{
		Env.setContext( ctx, "#AD_Client_ID", 1000004);
		Env.setContext( ctx, "#AD_Org_ID", 0);

		String sql = "SELECT * FROM P_Employee where isactive = 'Y'";
		PreparedStatement pstmt = null;
		try
		{
				pstmt = DB.prepareStatement (sql, null);
				ResultSet rs = pstmt.executeQuery ();
				while ( rs.next () )
				{
					
					 P_Employee Employee = new P_Employee(ctx, rs , null);
					 MBPartner BPartner =  MBPartner.get( getCtx(), Employee.getValue() );


					    if ( BPartner == null )
					        BPartner = new MBPartner( getCtx(), -1, null );
					    
				        BPartner.setValue( Employee.getValue());
				        BPartner.setName( Employee.getName());
				        BPartner.setIsSummary( false);
				        // TODO
				        BPartner.setIsEmployee( true );
				        //2014.04.29 set langue a celle de l'employé
				        P_Language language = P_Language.get( Env.getCtx(), Employee.getP_Language_ID(), null );
				        BPartner.setAD_Language( language.getValue() );
				        BPartner.setIsOneTime( false );               
				        BPartner.setIsProspect( false );               
				        BPartner.setIsVendor( false );                
				        BPartner.setIsCustomer( false );
				        BPartner.setC_BP_Group_ID( 105 );
				        BPartner.save();

				        MBPartnerLocation location = new MBPartnerLocation( getCtx(), -1, null );
				        location.setC_Location_ID( Employee.getC_Location_ID());
				        location.setC_BPartner_ID(BPartner.getC_BPartner_ID() );
				        location.setIsActive(true);
				        location.save();
				        
				}
				
				rs.close ();
				pstmt.close ();
				pstmt = null;
				
		}
		catch (Exception e)
		{
			System.out.println ("* Error * update assignment - " + sql + " - " + e);
		}
		
		
	}

	
	

    private Properties getCtx()
    {
    	return Env.getCtx();
    }

	/**************************************************************************
	 * 	Migration des données pour la SIQ
	 */
	public static void main (String[] args)
	{
		System.out.println("Test   $Revision: 1.3 $");
		System.out.println("----------------------------------");
		//
		Compiere.startup(true);
		
		
		int count = 0;
//		new Test2();
		count++;
		System.out.println("Generated = " + count);

	}	//	main
	
}
