package solstice.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.compiere.model.PO;
import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;

import java.util.logging.*;

import org.compiere.util.*;

import solstice.process.Ledger_Entry;
import solstice.process.Ledger_Entry_360;
import solstice.process.CreateStatementEarning;
import solstice.process.CreateStatementEarningHelico;
import solstice.process.CreateStatementEarningKrispy;

/**
 *  Payment Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Payment extends X_P_Payment
{
    /**
     * Pour distinguer les exceptions "NORMALES"
     */
    public class PaymentException extends Exception
    {
        public PaymentException() {super();}
        public PaymentException(String arg0) {super(arg0);}
        public PaymentException(Throwable arg0) {super(arg0);}
        public PaymentException(String arg0, Throwable arg1) {super(arg0, arg1);}
    }
    
	/**
	 * 	Get Payment
	 *	@param ctx context
	 * 	@param P_Payment_ID id
	 *	@return Payment
	 */
	public static P_Payment get (Properties ctx, int P_Payment_ID, String trxName)
	{
		Integer key = new Integer (P_Payment_ID);
		P_Payment Payment = (P_Payment)s_cache.get(key);
		if (Payment != null)
			return Payment;
		Payment = new P_Payment (ctx, P_Payment_ID, trxName);
		s_cache.put (key, Payment);
		return Payment;
	}	//	get


	/**	Cache						*/
	private static CCache<Integer,P_Payment>	s_cache = new CCache<Integer,P_Payment>("P_Payment", 20);

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Payment.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Payment_ID id
	 */
	public P_Payment (Properties ctx, int P_Payment_ID, String trxName)
	{
		super (ctx, P_Payment_ID, trxName);
		if (P_Payment_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Payment

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Payment (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Payment (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Payment_ID"), trxName);
	}	//	P_Payment

	/* Redéfinition des méthodes getNetPay, getTotalDeduction, getTotalTaxableBenefits et getGrossEarnings
	 *  Car dans la BD se sont des champs calculer.
	 */
	
	/** Set Net pay */
	public void setNetPay (BigDecimal NetPay)
	{
	throw new IllegalArgumentException ("NetPay is virtual column");
	}
	/** Get Net pay */
	public BigDecimal getNetPay() 
	{
		String sql = "Select NetPay From P_Payment Where P_Payment_ID=" + this.getP_Payment_ID() ;

		PreparedStatement pstmt = null;

		BigDecimal bd = Env.ZERO;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				bd = rs.getBigDecimal(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Payment - getNetPay - " + sql, e);
		}
			
		return bd;
	}

	/** Set Total deduction */
	public void setTotalDeduction (BigDecimal TotalDeduction)
	{
	throw new IllegalArgumentException ("TotalDeduction is virtual column");
	}
	/** Get Total deduction */
	public BigDecimal getTotalDeduction() 
	{
		String sql = "Select TotalDeduction From P_Payment Where P_Payment_ID=" + this.getP_Payment_ID() ;

		PreparedStatement pstmt = null;

		BigDecimal bd = Env.ZERO;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				bd = rs.getBigDecimal(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Payment - getTotalDeduction - " + sql, e);
		}
			
		return bd;
	}
	/** Set Total taxable benefit */
	public void setTotalTaxableBenefit (BigDecimal TotalTaxableBenefit)
	{
	throw new IllegalArgumentException ("TotalTaxableBenefit is virtual column");
	}
	/** Get Total taxable benefit */
	public BigDecimal getTotalTaxableBenefit() 
	{
		String sql = "Select TotalTaxableBenefit From P_Payment Where P_Payment_ID=" + this.getP_Payment_ID() ;

		PreparedStatement pstmt = null;

		BigDecimal bd = Env.ZERO;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				bd = rs.getBigDecimal(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Payment - getTotalTaxableBenefit - " + sql, e);
		}
		return bd;
		
	}

	/** Get Total taxable benefit */
	public BigDecimal getTotalTaxableBenefitProv() 
	{
		String sql = "Select TotalTaxableBenefitProv From P_Payment Where P_Payment_ID=" + this.getP_Payment_ID() ;

		PreparedStatement pstmt = null;

		BigDecimal bd = Env.ZERO;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				bd = rs.getBigDecimal(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Payment - getTotalTaxableBenefit - " + sql, e);
		}
		return bd;
		
	}

	/** Set Gross Earnings */
	public void setGrossEarnings (BigDecimal GrossEarnings)
	{
	throw new IllegalArgumentException ("GrossEarnings is virtual column");
	}
	/** Get Gross Earnings */
	public BigDecimal getGrossEarnings() 
	{
		String sql = "Select GrossEarnings From P_Payment Where P_Payment_ID=" + this.getP_Payment_ID() ;

		PreparedStatement pstmt = null;

		BigDecimal bd = Env.ZERO;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				bd = rs.getBigDecimal(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Payment - getGrossEarnings - " + sql, e);
		}
		
		
		if ( bd == null )
			return Env.ZERO;
		
		return bd;

	}

	
	public int GetNextLine ( )
	{
		String sql = "Select max( line ) + 10 From P_Payment_Gain Where P_Payment_ID=" + this.getP_Payment_ID() ;

		PreparedStatement pstmt = null;

		int line = 0;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				line = rs.getInt(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Payment - GetNextLine - " + sql, e);
		}
			
		return line;
		
	}

	public BigDecimal getTotalDebit( String trxName)
	{
		String sql = "Select isnull( sum( AMTACCTDR ),0) From P_Payment_EntryLine Where P_Payment_ID=" + this.getP_Payment_ID() ;

		PreparedStatement pstmt = null;

		BigDecimal amount = new BigDecimal(0);
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				amount = rs.getBigDecimal(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Payment - GetNextLine - " + sql, e);
		}
			
		if ( amount == null )
		    return Env.ZERO;

		amount = amount.setScale( 2 );
		return amount;
		
	}

	public BigDecimal getTotalCredit(String trxName)
	{
		String sql = "Select isnull( sum( AMTACCTCR ),0) From P_Payment_EntryLine Where P_Payment_ID=" + this.getP_Payment_ID() ;

		PreparedStatement pstmt = null;

		BigDecimal amount = new BigDecimal(0);
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				amount = rs.getBigDecimal(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Payment - GetNextLine - " + sql, e);
		}
		
		if ( amount == null )
		    return Env.ZERO;

		amount = amount.setScale( 2 );
		return amount;
		
	}
	
	public void createStatementEarning( )
	{
		if ( this.getAD_Client_ID() == 11 )
		{
			CreateStatementEarning SE = new CreateStatementEarning( );
			SE.createStatementEarning( this );
		}
		else
		{
			CreateStatementEarningKrispy SE = new CreateStatementEarningKrispy( );
			SE.createStatementEarning( this );
		}
			
	}
		
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true or false
	 *  If change the PayDate then update the payDate of the time sheet.
	 */
	
	protected boolean beforeSave (boolean newRecord)
	{
	
		if ( this.getP_Workplace_ID() == 0)
		{
			P_Employee Employee = P_Employee.get( Env.getCtx(), this.getP_Employee_ID(), this.get_TrxName() );
			this.setP_Workplace_ID( Employee.getP_Workplace_ID());
		}
		
/*		if (is_ValueChanged("PayDate"))
		{
  			P_Time_Sheet Timesheet = P_Time_Sheet.GetWithPaymentID(Env.getCtx(), this.getP_Payment_ID(), this.get_TrxName());
  			if ( Timesheet != null )
  			{
  				Timesheet.setPayDate( this.getPayDate());
  				Timesheet.save();
  			}
		}
*/		
		P_Payment_Group Payment_Group = P_Payment_Group.get(Env.getCtx(), this.getP_Payment_Group_ID(), null);
		P_BankAccount BankAccount = P_BankAccount.getByOrgID(Env.getCtx(), this.getAD_Org_ID(), Payment_Group.getP_Bank_ID(),  null);
		this.setP_BankAccount_ID( BankAccount.getP_BankAccount_ID() );

		return true;
	}


	/**
	 * Lorsque le statut du paiment devient E, on doit générer les enregistrements
	 * pour l'avis de dépôt dans les tables suivantes :
	 *   P_Statement_Earning
	 *   P_Statement_Earning_Gain
	 *   P_Statement_Earning_Deduction
	 *   P_Statement_Earning_Taxable_Benefits
	 *   P_Statement_Earning_Credits
	 */
	protected boolean afterSave(boolean newRecord, boolean success)
    {
	    // Si le statut du paiement est E, on doit générer les informations de
	    // l'avis de dépôt
	    if(success && this.getTimeSheetStatus() != null 
	    		   && this.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Issued) 
	    		   && this.getPaymentType().equals( P_Payment.PAYMENTTYPE_Cheque )
	    		   && !this.isTransfer())
	    {
			if ( this.getAD_Client_ID() == 11 )
			{
				CreateStatementEarning SE = new CreateStatementEarning( );
				SE.createStatementEarning( this );
			}
			else
			{
				CreateStatementEarningHelico SE = new CreateStatementEarningHelico( );
				SE.createStatementEarning( this );
			}
	    }
	    
	    //
	    // Si la feuille de temps est au status calculéer on créer ou recréer l'écriture comptable
	    //

        return success;
    } //	afterSave
	
	public void createEntryLine( String trxName )
	{
			Ledger_Entry gb = new Ledger_Entry( this , trxName );

			//Write accounting Entry
			gb.WriteAccountingEntryByPayment();

	}
	
	public void createEntryLine360( String trxName )
	{
			Ledger_Entry_360 gb = new Ledger_Entry_360( this , trxName );

			//Write accounting Entry
			gb.WriteAccountingEntryByPayment();
	}

	
	public void UpdatePaymentDistribution( String trxName) throws SQLException
    {
		if ( this.getNetPay() == null)
		{
			String SqlDel = "Delete from P_Payment_Distribution where P_Payment_ID = " +  this.getP_Payment_ID();
			DB.executeUpdate(SqlDel, null);
			return;
		}
	   	
	  	// reset l'information dans p_Payment a cause de champs calculer 
//	    P_Payment Payment = new P_Payment( Env.getCtx(), this.getP_Payment_ID(), trxName);
	    P_Employee_Payment_Dist Emp_Distribution = null;
	  	P_Payment_Distribution Payment_dist = null; 
		int NoPaiment = 0;
		int Count=0;
		
		String SqlDel = "Delete from P_Payment_Distribution where P_Payment_ID = " +  this.getP_Payment_ID();
		DB.executeUpdate(SqlDel, null);

		NoPaiment++;
		BigDecimal MntNet = this.getNetPay();
  		int DistResidual = 0;
  			                       
  		//
  		// Seulement les paie régulières sont déposés dans plusieurs compte bancaire.
  		//
  		String SqlDist = null;
  		if ( this.getPaymentTypeDoc().equals(P_Payment.PAYMENTTYPEDOC_Regular))
  		{
  			SqlDist = "Select P_Employee_Payment_Dist_ID " +
            " From P_Employee_Payment_Dist " +
            "where IsActive = 'Y' and P_Employee_ID=" + this.getP_Employee_ID() +
			 " order by Priority";
  		}
  		else
  		{
  			SqlDist = "Select P_Employee_Payment_Dist_ID " +
            " From P_Employee_Payment_Dist " +
            "where IsActive = 'Y' and P_Employee_ID=" + this.getP_Employee_ID() +
            " AND IsResidual = 'Y' " +
			 " order by Priority";
  		}
  		PreparedStatement pstmp_dist = null;
		pstmp_dist = DB.prepareStatement(SqlDist, null);
		ResultSet rs_Dist = pstmp_dist.executeQuery();
		boolean found = false;
  		while (rs_Dist.next())
  		{
  			found = true;
//  			log.log (Level.INFO,"Nouvel enregistrement" + Count);
			BigDecimal MntDist = new BigDecimal (0);
				Emp_Distribution = P_Employee_Payment_Dist.get( Env.getCtx(), rs_Dist.getInt("P_Employee_Payment_Dist_ID"), trxName);
  				if (Emp_Distribution.getAmount().compareTo(new BigDecimal(0)) != 0)
  				{
  					if ( MntNet.compareTo(Emp_Distribution.getAmount()) > 0 )
  						MntDist = Emp_Distribution.getAmount();
  					else
  						MntDist = MntNet;
  				}
  				else
  				{
  					BigDecimal PrcRate = Emp_Distribution.getRate().divide(new BigDecimal(100), 2, 2);
  					MntDist = this.getNetPay().multiply(PrcRate); 
  					if ( MntNet.compareTo(MntDist) < 0 )
  					   MntDist = MntNet;
  				}
  				MntNet = MntNet.setScale(2, BigDecimal.ROUND_HALF_UP);
  				MntDist = MntDist.setScale(2, BigDecimal.ROUND_HALF_UP);
  				
  				MntNet = MntNet.subtract(MntDist);
  				Payment_dist = new P_Payment_Distribution(Env.getCtx(), -1, trxName);
  				Payment_dist.setP_Payment_ID(this.getP_Payment_ID());
  				Payment_dist.setP_Financial_Institution_ID(Emp_Distribution.getP_Financial_Institution_ID());
  				Payment_dist.setTransit(Emp_Distribution.getTransit());
  				Payment_dist.setFolio(Emp_Distribution.getFolio());
  				Payment_dist.setAmount(MntDist.setScale(2, BigDecimal.ROUND_HALF_UP));
  				Payment_dist.save();
  				
  				if ( Emp_Distribution.isResidual() )
  					DistResidual = Payment_dist.getP_Payment_Distribution_ID();
  		}
  		if ( Payment_dist != null )
  		{
  	  		if ( DistResidual == 0)
  				DistResidual = Payment_dist.getP_Payment_Distribution_ID();
  		}
  			
  		rs_Dist.close();
  		pstmp_dist.close();
  			
  		if (MntNet.compareTo(new BigDecimal(0)) > 0 && DistResidual != 0)
  		{
  				//log.debug( "Mise à jour distribution: " + DistResidual );
  				Payment_dist = P_Payment_Distribution.get( Env.getCtx(), DistResidual, trxName ); 
  				Payment_dist.setAmount( Payment_dist.getAmount().add( MntNet ) );
  				Payment_dist.save();
  		}
  		
  		if ( ! found )
  		{
  			P_Time_Sheet Timesheet = P_Time_Sheet.GetWithPaymentID(Env.getCtx(), this.getP_Payment_ID(), trxName);
  			P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, "Il y a un erreur dans la distribution des paiments de l'employé", trxName);
  			
  		}
	}
	
	public static P_Payment copyFrom (Properties ctx, int P_Payment_ID, P_Time_Sheet TimeSheet, String trxName)
	{

		P_Payment from = P_Payment.get(ctx, P_Payment_ID, trxName);
		if (from.getP_Payment_ID() == 0)
			throw new IllegalArgumentException ("From Payment not found P_Payment_ID=" + P_Payment_ID);
		
		P_Payment  to = new P_Payment (ctx, -1, trxName);
		PO.copyValues(from, to, from.getAD_Client_ID(), from.getAD_Org_ID());
		to.set_ValueNoCheck ("P_Payment_ID", I_ZERO);

		to.setP_Period_ID( TimeSheet.getP_Period_ID() );

		to.setValue(  TimeSheet.getValue() );
        to.setPayDate( TimeSheet.getPayDate());
        
		if (!to.save())
			throw new IllegalStateException("Could not create TimeSheet");

		TimeSheet.setP_Payment_ID( to.getP_Payment_ID());
		TimeSheet.save();
		
		return to;

	}

	public void deleteAttachment()
	{
		
	}
}	//	P_Payment
