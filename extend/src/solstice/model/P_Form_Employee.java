/******************************************************************************
 * Product: Solstice+ Payroll & Human Resources Management                    *
 * Copyright (C) 2008 ProGestion Informatique, Inc. All Rights Reserved.      *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ProGestion Informatique, 210-5300 Bld des Galerie, Quebec, G2K 2A2 Canada  *
 * or via info@progestion.net or http://www.progestion.net/license.html       *
 ******************************************************************************/
package solstice.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.model.PO;
import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;
import solstice.model.P_Form_Param;

import solstice.model.X_P_Form_Employee;
import solstice.process.Calcul_federal_tax;
import solstice.utils.PgiUtil;
import solstice.model.P_Region;

import java.util.logging.*;

import org.compiere.util.*;
import java.util.Hashtable;

/**
 *  Employee_Form Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Form_Employee extends X_P_Form_Employee
{
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;
	private static Hashtable<?, ?> 	taxTable = null;
	
	private String TypeDeductionSelect;


	/**
	 * 	Get Employee_Form
	 *	@param ctx context
	 * 	@param C_Employee_Form_ID id
	 *	@return Employee_Form
	 */
	public static P_Form_Employee get (Properties ctx, int P_Form_Employee_ID, String trxName)
	{
		Integer key = new Integer (P_Form_Employee_ID);
		P_Form_Employee Employee_Form = (P_Form_Employee)s_cache.get(key);
		if (Employee_Form != null)
			return Employee_Form;
		Employee_Form = new P_Form_Employee (ctx, P_Form_Employee_ID, trxName);
		s_cache.put (key, Employee_Form);
		return Employee_Form;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Form_Employee>	s_cache = new CCache<Integer,P_Form_Employee>("P_Form_Employee", 20);
	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Form_Employee.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param C_Employee_Form_ID id
	 */
	public P_Form_Employee (Properties ctx, int P_Form_Employee_ID, String trxName)
	{
		super (ctx, P_Form_Employee_ID, trxName);
		if (P_Form_Employee_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Employee_Form

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Form_Employee (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}
	
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Form_Employee (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Form_Employee_ID"), trxName);
	}	//	P_Form_Employee

	//
	//
	public static boolean exists ( int P_Form_ID, int P_Year_ID, int P_Employee_ID, int P_Employer_ID, int Taxation_Region_ID )
	{
		boolean r_exist = false; 
	    String sql = "Select 1 From P_Form_Employee Where P_Form_ID = " + P_Form_ID + " and P_Year_ID = " + P_Year_ID + " and P_Employee_ID = " + P_Employee_ID;
	    if ( P_Employer_ID != 0 )
	    	sql =  sql + " and P_Employer_ID = " + P_Employer_ID;
	    
	    if ( Taxation_Region_ID != 0 )
	    	sql =  sql + " and Taxation_Region_ID = " + Taxation_Region_ID;
	    	
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
			    r_exist = true; 
			}
			else
			{
			    r_exist = false; 
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Form_Employee, - " + sql, e);
		}

		return r_exist;
	}	//	P_Form_Employee

	public static void delete ( int P_Form_ID, int P_Year_ID, int P_Employee_ID, int P_Employer_ID, int Taxation_Region_ID )
	{
		String sql = "Delete From P_Form_Employee " 
				   + "Where IsGenerated = 'N' and P_Form_ID = " + P_Form_ID + " and P_Year_ID = " + P_Year_ID + " and P_Employee_ID = " + P_Employee_ID
				   ;
		
// On détruit tout les formulaier t4 ou r1 de l'année		
		if ( P_Employer_ID != 0 )
			sql = sql + " and P_Employer_id = " + P_Employer_ID ;

		if ( Taxation_Region_ID != 0 )
			sql = sql + " and Taxation_Region_ID = " + Taxation_Region_ID ;

		
		DB.executeUpdate (sql.toString (), null);
	}

    //	 Détruit tout les formulaier t4 ou r1 de l'année		

	public static void deleteAll ( int P_Form_ID, int P_Year_ID, int P_Employee_ID, int P_Employer_ID, int Taxation_Region_ID )
	{
		String sql = "Delete From P_Form_Employee " 
				   + "Where IsGenerated = 'N' and P_Form_ID = " + P_Form_ID + " and P_Year_ID = " + P_Year_ID + " and P_Employee_ID = " + P_Employee_ID
				   ;
		
		DB.executeUpdate (sql.toString (), null);
	}

	//
	// Empeche la destruction si le formulaire a été imprimer ou générer
	//
	public boolean beforeDelete()
	{
		if ( this.isGenerated() || this.isPrinted())
			return false;
		
		return true;
	}
	
    /**
     * Procédure de copie. Lorsqu'on copie une déduction, on doit également
     * copier tous les paramètres et le contenue des onglets sous celle-ci.
     */
    public static int copyFrom(Properties ctx, int P_Form_Employee_ID, String trxName)
    {
    	P_Form_Employee from = P_Form_Employee.get(ctx, P_Form_Employee_ID, trxName);
		if (from.getP_Form_Employee_ID() == 0)
			throw new IllegalArgumentException ("From P_Form_Employee not found P_Form_Employee_ID=" + P_Form_Employee_ID);
		
		P_Form_Employee  to = new P_Form_Employee (ctx, -1, trxName);
		PO.copyValues(from, to, from.getAD_Client_ID(), from.getAD_Org_ID());

    	to.setFormType( P_Form_Employee.FORMTYPE_Modified);
    	to.setIsPrinted(false);
    	to.setIsGenerated(false);

		if (!to.save())
			throw new IllegalStateException("Could not create P_Form_Employee");

/*    	this.setFormType( P_Form_Employee.FORMTYPE_Modified);
    	this.setIsPrinted(false);
    	this.setIsGenerated(false);
    	this.save();
*/    	
    	
        return 1;
/*        try
        {
            // On copie les tables P_Deduction_Param, P_Deduction_Insurance, P_Deduction_Insurance_Bound, 
            // P_Deduction_Param_Ympe, P_Deduction_Acct et P_Employee_Deduction
            this.copyFormEmployeeDetail(originalId);
        }
        catch (SQLException e)
        {
            log.log(Level.SEVERE, "afterCopy", e);
        }
*/        
    }
    
    /**
     * Copie les enregistrements de la table P_Form_Employee_Detail
     */
    private void copyFormEmployeeDetail(int originalId) throws SQLException
    {
        // On sort tous les enregistrements à copier de la table
        String sql
        = "select * from P_Form_Employee_Detail "
            + " where P_Form_Employee_ID = " + originalId;
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        P_Form_Employee_Detail detail;
        while(rs.next())
        {
            // On crée un nouvel enregistrement pour la déduction copiée
        	detail = new P_Form_Employee_Detail( Env.getCtx(), -1, this.get_TrxName());
        	detail.setP_Form_Employee_ID( this.getP_Form_Employee_ID());
        	detail.setP_Form_Field_ID(rs.getInt("P_Form_Field_ID"));
        	detail.setFormFieldAmount( rs.getBigDecimal("FormFieldAmount"));
        	detail.setFormFieldText( rs.getString("FormFieldText"));
            detail.save();
            
        }
        
        rs.close();
        stmt.close();
    }


    /**
     * return the total of the form
     */
    public BigDecimal getTotal( ) throws SQLException
    {
    	BigDecimal total = new BigDecimal(0);
        String sql
        = "SELECT SUM(ISNULL( FormFieldAmount ,0)) Amount FROM P_Form_Employee_Detail WHERE P_Form_Employee_ID = " + this.getP_Form_Employee_ID();
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next())
        {
            total = rs.getBigDecimal( "Amount" );
        }
        
        rs.close();
        stmt.close();
        return total;
    }

    boolean isGenerate = false;
    public void save_generate( )
    {
    	// SM 2007-11-07
    	// Initialise la variable globale au paramètre reçu
    	
    	isGenerate = true;
    	this.save();
    }
    
	private P_Employer Employer;
	private P_Employee Employee;
	private P_Form Form;
	private P_Region Taxation_Region;
	private Timestamp LastDate;

	protected boolean beforeSave (boolean newRecord)
	{

		if ( taxTable == null )
			taxTable = getTaxTable( this.getP_Year_ID() );

		
		if ( this.getP_Form_ID() == 0 )
		{
			//TODO Traduction
			s_log.saveError("ValidationError","Le champs formulaire est obligatoire");
//			s_log.log(Level.SEVERE,"ValidationError", Msg.translate(getCtx(), "Le champs formulaire est obligatoire"));
			return false;
		}
		
		LastDate = getLastDate(  this.getP_Year_ID() );

		Form = P_Form.get( getCtx (), this.getP_Form_ID() , this.get_TrxName());
		Employee = P_Employee.get( Env.getCtx(), this.getP_Employee_ID(), this.get_TrxName());
		
		if (Form.getValue().trim().equals("T4") )
		{
			Employer = P_Employer.get( Env.getCtx(), this.getP_Employer_ID(), this.get_TrxName());
			Taxation_Region = P_Region.get( Env.getCtx(), this.getTaxation_Region_ID(), this.get_TrxName());
		}

		if (Form.getValue().trim().equals("T4A") )
		{
			Employer = P_Employer.get( Env.getCtx(), this.getP_Employer_ID(), this.get_TrxName());
			Taxation_Region = P_Region.get( Env.getCtx(), this.getTaxation_Region_ID(), this.get_TrxName());
		}

		if (Form.getValue().trim().equals("R1") )
		{
			Taxation_Region = P_Region.get( Env.getCtx(), 166, this.get_TrxName());
		}


		if (newRecord)
		{
		//	Form.setAD_Org_ID( this.getAD_Org_ID());
    		if (Form.getValue().trim().equals("T4") && this.getP_Employer_ID() == 0)
			{
				this.setP_Employer_ID( Employee.getP_Employer_ID() );
			}

    		if (Form.getValue().trim().equals("T4") && this.getTaxation_Region_ID() == 0)
			{
				this.setTaxation_Region_ID( Employee.getTaxation_Region_ID() );
			}

    		// Si l'employé a travailler dans plusieurs seulement les gain du Québec vont sur le T4
    		if (Form.getValue().trim().equals("R1"))
			{
				this.setTaxation_Region_ID( 166 );
			}

    		
    		if (Form.getValue().trim().equals("T4A")  && this.getP_Employer_ID() == 0 )
			{
				this.setP_Employer_ID( Employee.getP_Employer_ID() );
				this.setTaxation_Region_ID( Employee.getTaxation_Region_ID() );
			}

    		if ( this.getFormType() == null )
    			this.setFormType( "O" );
			this.setIsGenerated( false );
			this.setIsPrinted( false );

			if ( ! isGenerate )
			{
				this.setFormNumber(GetFormEmployeeNumber( Form ));


//				if (Form.getValue().trim().equals("R1") || Form.getValue().trim().equals("R2") || Form.getValue().trim().equals("R3"))
//				{
//					this.setFacsimileNumber(GetFormEmployeeFacSimileNumber());
//				}
			}


	    	
		}
		return true;
	}	//	beforeSave

	// Code 57: Revenus d'emploi - 15 mars au 9 mai
	// Code 58: Revenus d'emploi - 10 mai au 4 juillet
	// Code 59: Revenus d'emploi - 5 juillet au 29 aout
	// Code 60: Revenus d'emploi - 30 aout au 26 septembre
/*	
	private BigDecimal getAmountPCU( String Case )
	{
		String sqlSumCase14 = "";
		BigDecimal amount = Env.ZERO;

		String DateFrom = "";
		String DateTo = "";

		if ( Case.equals("57") )
		{
			DateFrom = "2020/03/15";
			DateTo = "2020/05/09";
		}
		if ( Case.equals("58") )
		{
			DateFrom = "2020/05/10";
			DateTo = "2020/07/04";
			
		}
		if ( Case.equals("59") )
		{
			DateFrom = "2020/07/05";
			DateTo = "2020/08/29";
		}
		if ( Case.equals("60") )
		{
			DateFrom = "2020/08/30";
			DateTo = "2020/09/26";
		}
				

		sqlSumCase14 = " Select Sum(GTX.Total) From (	(	Select sum( AmountCalc ) Total From P_Payment_Gain , P_Payment Where P_Payment_Gain.P_Payment_ID = P_Payment.P_Payment_ID "
					     + " AND P_Payment.P_Employee_ID =  " + this.getP_Employee_ID()   
					     + " And P_Payment.P_Year_ID = " + this.getP_Year_ID()
					     + " And P_Gain_ID In ( Select P_Gain_ID FROM P_Form_Field_Gain Where P_Form_Field_Gain.isActive = 'Y' "
					     + " and P_Form_Field_ID IN ( SELECT P_Form_Field_ID FROM P_Form_Field WHERE P_Form_Field.value = '14' ) " 
					     + " ) "
					     + " And P_Payment.P_Employer_ID = " + this.getP_Employer_ID()
					     + " And P_Payment.Taxation_Region_ID = " + this.getTaxation_Region_ID() 	
					     + " AND P_Payment.P_Period_ID in ( Select P_Period_ID From P_Period where P_Period.PAYDATE Between '" + DateFrom + "' and '" + DateTo + "'  ) " 
					     + " )  "  
					     + " Union All	(	Select sum( Taxable_Benefit_Amount ) Total From P_Payment_Taxable_Benefit , P_Payment Where P_Payment_Taxable_Benefit.P_Payment_ID = P_Payment.P_Payment_ID "
					     + " AND P_Payment.P_Employee_ID =  " + this.getP_Employee_ID() 
					     + " And P_Payment.P_Year_ID = " + this.getP_Year_ID()
					     + " And P_Payment_Taxable_Benefit.P_Taxable_Benefit_ID IN ( Select P_Taxable_Benefit_ID From P_Form_Field_Taxable_Benefit Where P_Form_Field_Taxable_Benefit.isactive = 'Y' "
					     + " and P_Form_Field_ID IN ( SELECT P_Form_Field_ID FROM P_Form_Field WHERE P_Form_Field.value = '14' ) " 
					     + " ) "
					     + " And P_Payment.P_Employer_ID = " + this.getP_Employer_ID()
					     + " And P_Payment.Taxation_Region_ID =  "  + this.getTaxation_Region_ID() 
					     + " AND P_Payment.P_Period_ID in ( Select P_Period_ID From P_Period where P_Period.PAYDATE Between '" + DateFrom + "' and '" + DateTo + "'  ) " 
					     + " )) GTX  " ;
					
			
		
	    PreparedStatement pstmtSum  = null;
		try
		{

			log.info("FormGenerate - sqlSum - Open - Field: " + Case + " - "+  sqlSumCase14 );

			pstmtSum = DB.prepareStatement (sqlSumCase14, null);
			ResultSet rsSum = pstmtSum.executeQuery ();
			if ( rsSum.next() )
			{
				amount = rsSum.getBigDecimal(1);
				if ( amount == null )
					amount = Env.ZERO;
			}
			rsSum.close ();
			pstmtSum.close ();
			pstmtSum = null;

			log.info("FormGenerate - sqlSum - close "  );

		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, "FormGenerate Sum - " + sqlSumCase14, e);
		}
	

		return amount;
		
	}
*/
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
    	BigDecimal AmountForStatusIndian = Env.ZERO;

		int PeriodID = 0; 

	    if (!success )
		{
			return success;
		}

	    if (newRecord  )
		{

    		String sql = "Select P_Form_Field_ID From P_Form_Field WHERE P_Form_ID = " + this.getP_Form_ID() + " And IsActive = 'Y' ORDER BY VALUE ";
			
			PreparedStatement pstmt  = null;
			try
			{
				pstmt = DB.prepareStatement (sql, null);
				ResultSet rs = pstmt.executeQuery ();
				P_Form_Field Form_Field;
				P_Form_Employee_Detail Form_Employee_Detail;
				
				while (rs.next ())
				{
				    Form_Field = P_Form_Field.get( getCtx(), rs.getInt(1), this.get_TrxName());
				    Form_Employee_Detail = new P_Form_Employee_Detail( getCtx(), -1, this.get_TrxName());
				    
				    Form_Employee_Detail.setP_Form_Employee_ID( this.getP_Form_Employee_ID() );
				    Form_Employee_Detail.setP_Form_Field_ID( rs.getInt(1) );
				    if ( Form_Field.getFormFieldType() == null  )
				    {
				    	Form_Employee_Detail.setFormFieldText( null );
				    }
				    else if ( Form_Field.getFormFieldType().equals("4"))
				    {
				        if ( Form_Field.getFormFieldTypeVar().equals("1"))
				            Form_Employee_Detail.setFormFieldText( Employee.getValue() );
				        if ( Form_Field.getFormFieldTypeVar().equals("2"))
				            Form_Employee_Detail.setFormFieldText( Employee.getSin() );
				        if ( Form_Field.getFormFieldTypeVar().equals("3"))
				            Form_Employee_Detail.setFormFieldText( Employee.getBirthDate().toString() );
				        if ( Form_Field.getFormFieldTypeVar().equals("4"))
				        {
				        	 P_Region Form_Taxation_Region;
				        	if ( Taxation_Region != null)
				        	{
					            Form_Taxation_Region = P_Region.get( getCtx(), Taxation_Region.getC_Region_ID(), this.get_TrxName() );
				        	}
				        	else
				        	{
				        		Form_Taxation_Region = P_Region.get( getCtx(), Employee.getTaxation_Region_ID(), this.get_TrxName() );
				        	}

				            Form_Employee_Detail.setFormFieldText( Form_Taxation_Region.getName() );
				        }
				        if ( Form_Field.getFormFieldTypeVar().equals("5"))
				           	Form_Employee_Detail.setFormFieldText(GetEmployeePensionNumber(Form_Field.getP_Form_Field_ID(),Employee.getP_Employee_ID()));
				    }
				    else if ( Form_Field.getFormFieldType().equals("5")
					 && (Form.getValue().trim().equals("T4") 
							&& (   Form_Field.getValue().equals("45")
								)
						))
					{
						String text = "3";
						if ( Employer.isReducedEI() == false )
							text = "1" ;

						if ( Employee.getDateLayoff() != null && Employee.getDateLayoff().compareTo( LastDate ) <= 0 )
							text = "1" ;
						
						// groupe 8 pas d'assurance dentaire. 
						P_Employee_Profile pf = P_Employee_Profile.getProfileInsurance(Env.getCtx(), Employee.getP_Employee_ID(), LastDate ,null);
						if ( pf.getP_Profile_ID() == 1100068 )
							text = "1" ;
							
						Form_Employee_Detail.setFormFieldText(text); 
						
					}
				    
				    else if (!Form_Field.getFormFieldType().equals("5") && !Form_Field.getFormFieldType().equals("9"))
//				    else if (!Form_Field.getFormFieldType().equals("5"))
				    {
						String sqlSum = null; 
						String DeductionType="";
						if ( Form_Field.getFormFieldType().trim().equals("1") || Form_Field.getFormFieldType().trim().equals("7"))
						{
							DeductionType = GetDeductionType(Form_Field.getP_Form_Field_ID());
							if (DeductionType.trim().equals("1"))
							{
								TypeDeductionSelect = "Salary_Eligible";
							}
							else if (DeductionType.trim().equals("2"))
							{
								TypeDeductionSelect = "Employee_Part";
							}
							else if (DeductionType.trim().equals("3"))
							{
								TypeDeductionSelect = "Employer_Part";
							}
							else
							{
								TypeDeductionSelect = "Employee_Part + Employer_Part";
							}
						}

				        // Deduction
					    if ( Form_Field.getFormFieldType().trim().equals("1"))
					    {
					        sqlSum = "Select sum( " + TypeDeductionSelect + " ) From P_Payment_Deduction, P_Payment Where P_Payment_Deduction.P_Payment_ID = P_Payment.P_Payment_ID AND P_Payment.P_Employee_ID = " + this.getP_Employee_ID() 
							   		+ " And P_Payment.P_Year_ID = " + this.getP_Year_ID()
									+ " And P_Deduction_ID In ( Select P_Form_Field_Deduction.P_Deduction_ID From P_Form_Field_Deduction, P_Deduction Where P_Form_Field_Deduction.isactive = 'Y' and P_Form_Field_Deduction.P_Deduction_ID = P_Deduction.P_Deduction_ID AND ISNULL( P_Deduction.C_Region_ID," + Taxation_Region.getC_Region_ID() + " ) = " + Taxation_Region.getC_Region_ID() + " AND P_Form_Field_ID = " + Form_Field.getP_Form_Field_ID() + ")";
							if ( Employer != null )
								sqlSum = sqlSum + " And P_Payment.P_Employer_ID =  " + Employer.getP_Employer_ID();	    

							if ( Taxation_Region != null )
								sqlSum = sqlSum + " And P_Payment.Taxation_Region_ID =  " + Taxation_Region.getC_Region_ID() ;	    

							
							if ( PeriodID != 0)
								sqlSum = sqlSum + " and P_Payment.P_Period_ID <= " +  PeriodID; 

						    //2008.10.28
/*							
				    		if (Form.getValue().trim().equals("T4A") )
							{
				    			sqlSum = sqlSum + " And P_Payment.PaymentTypeDoc  = '" + P_Payment.PAYMENTTYPEDOC_SeparationPay + "'";  
							}
				    		else if (Form.getValue().trim().equals("T4") )
				    		{
				    			sqlSum = sqlSum + " And P_Payment.PaymentTypeDoc  <> '" + P_Payment.PAYMENTTYPEDOC_SeparationPay + "'";  
				    		}
*/
					    }

					    String sqlSum2 = null;
				        // Deduction du Québec... pour employé hors Québec.
					    if ( Form_Field.getFormFieldType().trim().equals("1") && Taxation_Region.getC_Region_ID() == 166 )
					    {
					        sqlSum2 = "Select  sum( " + TypeDeductionSelect + " ) From P_Payment_Deduction, P_Payment Where P_Payment_Deduction.P_Payment_ID = P_Payment.P_Payment_ID AND P_Payment.P_Employee_ID = " + this.getP_Employee_ID() 
							   		+ " And P_Payment.P_Year_ID = " + this.getP_Year_ID()
									+ " And P_Deduction_ID In ( Select P_Form_Field_Deduction.P_Deduction_ID From P_Form_Field_Deduction, P_Deduction Where P_Form_Field_Deduction.isactive = 'Y' and P_Form_Field_Deduction.P_Deduction_ID = P_Deduction.P_Deduction_ID AND P_Deduction.C_Region_ID = 166 AND P_Form_Field_ID = " + Form_Field.getP_Form_Field_ID() + ")";
							if ( Employer != null )
								sqlSum2 = sqlSum2 + " And P_Payment.P_Employer_ID =  " + Employer.getP_Employer_ID();	    

                            // payment avec province d'imposition différente de Québec.
							sqlSum2 = sqlSum2 + " And P_Payment.Taxation_Region_ID !=  " + Taxation_Region.getC_Region_ID() ;	    
							
							if ( PeriodID != 0)
								sqlSum2 = sqlSum2 + " and P_Payment.P_Period_ID <= " +  PeriodID; 

						    //2008.10.28
/*
							if (Form.getValue().trim().equals("T4A") )
							{
				    			sqlSum = sqlSum + " And P_Payment.PaymentTypeDoc  = '" + P_Payment.PAYMENTTYPEDOC_SeparationPay + "'";  
							}
				    		else if (Form.getValue().trim().equals("T4") )
				    		{
				    			sqlSum = sqlSum + " And P_Payment.PaymentTypeDoc  <> '" + P_Payment.PAYMENTTYPEDOC_SeparationPay + "'";  
				    		}
*/
					    }


					    // Gain
					    if ( Form_Field.getFormFieldType().trim().equals("2"))
					    {
					        sqlSum = "Select sum( AmountCalc ) From P_Payment_Gain , P_Payment Where P_Payment_Gain.P_Payment_ID = P_Payment.P_Payment_ID AND P_Payment.P_Employee_ID = " + this.getP_Employee_ID() 
					               + " And P_Payment.P_Year_ID = " + this.getP_Year_ID()
					               + " And P_Gain_ID In ( Select P_Gain_ID From P_Form_Field_Gain Where P_Form_Field_Gain.IsActive = 'Y' and P_Form_Field_ID = " + Form_Field.getP_Form_Field_ID() + ")";
							if ( Employer != null )
								sqlSum = sqlSum + " And P_Payment.P_Employer_ID =  " + Employer.getP_Employer_ID();	    

							if ( Taxation_Region != null )
								sqlSum = sqlSum + " And P_Payment.Taxation_Region_ID =  " + Taxation_Region.getC_Region_ID() ;	    

							if ( PeriodID != 0)
								sqlSum = sqlSum + " and P_Payment.P_Period_ID <= " +  PeriodID; 

						    //2008.10.28
/*
							if (Form.getValue().trim().equals("T4A") )
							{
				    			sqlSum = sqlSum + " And P_Payment.PaymentTypeDoc  = '" + P_Payment.PAYMENTTYPEDOC_SeparationPay + "'";  
							}
				    		else if (Form.getValue().trim().equals("T4") )
				    		{
				    			sqlSum = sqlSum + " And P_Payment.PaymentTypeDoc  <> '" + P_Payment.PAYMENTTYPEDOC_SeparationPay + "'";  
				    		}
*/
					    }
					    // Taxable Benefits
					    if ( Form_Field.getFormFieldType().trim().equals("3"))
					    {
					        sqlSum = "Select sum( Taxable_Benefit_Amount ) From P_Payment_Taxable_Benefit , P_Payment Where P_Payment_Taxable_Benefit.P_Payment_ID = P_Payment.P_Payment_ID AND P_Payment.P_Employee_ID = " + this.getP_Employee_ID() 
				               + " And P_Payment.P_Year_ID = " + this.getP_Year_ID()
				               + " And P_Taxable_Benefit_ID In ( Select P_Taxable_Benefit_ID From P_Form_Field_Taxable_Benefit Where P_Form_Field_Taxable_Benefit.isactive = 'Y' and P_Form_Field_ID = " + Form_Field.getP_Form_Field_ID() + ")";
							if ( Employer != null )
								sqlSum = sqlSum + " And P_Payment.P_Employer_ID =  " + Employer.getP_Employer_ID();	    

							if ( Taxation_Region != null )
								sqlSum = sqlSum + " And ( P_Payment.Taxation_Region_ID =  " + Taxation_Region.getC_Region_ID(); 

							// Québec.
//							if (Taxation_Region != null && Form.getValue().trim().equals("R1"))
//								sqlSum = sqlSum + "  OR P_Taxable_Benefit_ID In ( SELECT P_Taxable_Benefit_ID FROM P_Taxable_Benefit WHERE P_Taxable_Benefit.P_Taxable_Benefit_ID = P_Payment_Taxable_Benefit.P_Taxable_Benefit_ID AND P_Taxable_Benefit.C_Region_ID = 166 ) ";	    

							if ( Taxation_Region != null )
								sqlSum = sqlSum + ")"; 

							if ( PeriodID != 0)
								sqlSum = sqlSum + " and P_Payment.P_Period_ID <= " +  PeriodID; 

						    //2008.10.28
/*
							if (Form.getValue().trim().equals("T4A") )
							{
				    			sqlSum = sqlSum + " And P_Payment.PaymentTypeDoc  = '" + P_Payment.PAYMENTTYPEDOC_SeparationPay + "'";  
							}
				    		else if (Form.getValue().trim().equals("T4") )
				    		{
				    			sqlSum = sqlSum + " And P_Payment.PaymentTypeDoc  <> '" + P_Payment.PAYMENTTYPEDOC_SeparationPay + "'";  
				    		}
*/
					    }
					    // Gain / Taxable Benefits
					    if ( Form_Field.getFormFieldType().trim().equals("6"))
					    {
					    	sqlSum = "Select Sum(GTX.Total) From " +
							 "(" +
							 "	(" +
							 "	Select sum( AmountCalc ) Total From P_Payment_Gain , P_Payment Where P_Payment_Gain.P_Payment_ID = P_Payment.P_Payment_ID AND P_Payment.P_Employee_ID = " + this.getP_Employee_ID() + 
				             " 	And P_Payment.P_Year_ID = " + this.getP_Year_ID()+
				             " 	And P_Gain_ID In ( Select P_Gain_ID FROM P_Form_Field_Gain Where P_Form_Field_Gain.isActive = 'Y' and P_Form_Field_ID = " + Form_Field.getP_Form_Field_ID() + ")" ;
							if ( Employer != null )
								sqlSum = sqlSum + " And P_Payment.P_Employer_ID =  " + Employer.getP_Employer_ID();	    

							if ( Taxation_Region != null )
								sqlSum = sqlSum + " And P_Payment.Taxation_Region_ID =  " + Taxation_Region.getC_Region_ID() ;	    

							if ( PeriodID != 0)
								sqlSum = sqlSum + " and P_Payment.P_Period_ID <= " +  PeriodID; 

						    //2008.10.28
/*
							if (Form.getValue().trim().equals("T4A") )
							{
				    			sqlSum = sqlSum + " And P_Payment.PaymentTypeDoc  = '" + P_Payment.PAYMENTTYPEDOC_SeparationPay + "'";  
							}
				    		else if (Form.getValue().trim().equals("T4") )
				    		{
				    			sqlSum = sqlSum + " And P_Payment.PaymentTypeDoc  <> '" + P_Payment.PAYMENTTYPEDOC_SeparationPay + "'";  
				    		}
*/
							sqlSum = sqlSum + "	)" +
							 "Union All" + 
							 "	(" +
							 "	Select sum( Taxable_Benefit_Amount ) Total From P_Payment_Taxable_Benefit , P_Payment Where P_Payment_Taxable_Benefit.P_Payment_ID = P_Payment.P_Payment_ID AND P_Payment.P_Employee_ID = " + this.getP_Employee_ID() +
							 "  And P_Payment.P_Year_ID = " + this.getP_Year_ID() +
							 "  And P_Payment_Taxable_Benefit.P_Taxable_Benefit_ID IN ( Select P_Taxable_Benefit_ID From P_Form_Field_Taxable_Benefit Where P_Form_Field_Taxable_Benefit.isactive = 'Y' and P_Form_Field_ID = " + Form_Field.getP_Form_Field_ID() + ")";
							
							if ( Employer != null )
								sqlSum = sqlSum + " And P_Payment.P_Employer_ID =  " + Employer.getP_Employer_ID();

							if ( Taxation_Region != null )
								sqlSum = sqlSum + " And ( P_Payment.Taxation_Region_ID =  " + Taxation_Region.getC_Region_ID(); 

							// Québec.
//							if ( Taxation_Region != null && Form.getValue().trim().equals("R1"))
//								sqlSum = sqlSum + "  OR P_Taxable_Benefit_ID In ( SELECT P_Taxable_Benefit_ID FROM P_Taxable_Benefit WHERE P_Taxable_Benefit.P_Taxable_Benefit_ID = P_Payment_Taxable_Benefit.P_Taxable_Benefit_ID AND P_Taxable_Benefit.C_Region_ID = 166 ) ";	    
							
							if ( Taxation_Region != null )
								sqlSum = sqlSum + ")"; 
							
							if ( PeriodID != 0)
								sqlSum = sqlSum + " and P_Payment.P_Period_ID <= " +  PeriodID; 

						    //2008.10.28
/*
							if (Form.getValue().trim().equals("T4A") )
							{
				    			sqlSum = sqlSum + " And P_Payment.PaymentTypeDoc  = '" + P_Payment.PAYMENTTYPEDOC_SeparationPay + "'";  
							}
				    		else if (Form.getValue().trim().equals("T4") )
				    		{
				    			sqlSum = sqlSum + " And P_Payment.PaymentTypeDoc  <> '" + P_Payment.PAYMENTTYPEDOC_SeparationPay + "'";  
				    		}
*/
							sqlSum = sqlSum + "	)" +
							 ") GTX"; 
					    }
					    // Deduction / Taxable Benefits
					    if ( Form_Field.getFormFieldType().trim().equals("7"))
					    {
					    	sqlSum = "Select Sum(DTX.Total) From " +
									 "(" +
									 "	(" +
									 "	Select sum( " + TypeDeductionSelect + " ) Total From P_Payment_Deduction , P_Payment Where P_Payment_Deduction.P_Payment_ID = P_Payment.P_Payment_ID AND P_Payment.P_Employee_ID = " + this.getP_Employee_ID() + 
						             " 	And P_Payment.P_Year_ID = " + this.getP_Year_ID() +
						             " 	And P_Payment_Deduction.P_Deduction_ID IN ( Select P_Deduction_ID From P_Form_Field_Deduction Where P_Form_Field_Deduction.isactive = 'Y' and P_Form_Field_ID = " + Form_Field.getP_Form_Field_ID() + ")" ;
					    			 if ( Employer != null )
										sqlSum = sqlSum + " And P_Payment.P_Employer_ID =  " + Employer.getP_Employer_ID();	    
  									 if ( Taxation_Region != null )
										sqlSum = sqlSum + " And P_Payment.Taxation_Region_ID =  " + Taxation_Region.getC_Region_ID() ;	    

									 if ( PeriodID != 0)
										sqlSum = sqlSum + " and P_Payment.P_Period_ID <= " +  PeriodID; 

									    //2008.10.28
/*
									 if (Form.getValue().trim().equals("T4A") )
										{
							    			sqlSum = sqlSum + " And P_Payment.PaymentTypeDoc  = '" + P_Payment.PAYMENTTYPEDOC_SeparationPay + "'";  
										}
							    		else if (Form.getValue().trim().equals("T4") )
							    		{
							    			sqlSum = sqlSum + " And P_Payment.PaymentTypeDoc  <> '" + P_Payment.PAYMENTTYPEDOC_SeparationPay + "'";  
							    		}
*/
									sqlSum = sqlSum + "	)" +
									 "Union All" + 
									 "	(" +
									 "	Select sum( Taxable_Benefit_Amount ) Total From P_Payment_Taxable_Benefit , P_Payment Where P_Payment_Taxable_Benefit.P_Payment_ID = P_Payment.P_Payment_ID AND P_Payment.P_Employee_ID = " + this.getP_Employee_ID() +
									 "  And P_Payment.P_Year_ID = " + this.getP_Year_ID() +
									 "  And P_Payment_Taxable_Benefit.P_Taxable_Benefit_ID IN ( Select P_Taxable_Benefit_ID From P_Form_Field_Taxable_Benefit Where P_Form_Field_Taxable_Benefit.isactive = 'Y' and P_Form_Field_ID = " + Form_Field.getP_Form_Field_ID() + ")";
									if ( Employer != null )
										sqlSum = sqlSum + " And P_Payment.P_Employer_ID =  " + Employer.getP_Employer_ID();
									if ( Taxation_Region != null )
										sqlSum = sqlSum + " And P_Payment.Taxation_Region_ID =  " + Taxation_Region.getC_Region_ID() ;	    

									if ( PeriodID != 0)
										sqlSum = sqlSum + " and P_Payment.P_Period_ID <= " +  PeriodID; 

								    //2008.10.28
/*
 						    		if (Form.getValue().trim().equals("T4A") )
 
									{
						    			sqlSum = sqlSum + " And P_Payment.PaymentTypeDoc  = '" + P_Payment.PAYMENTTYPEDOC_SeparationPay + "'";  
									}
						    		else if (Form.getValue().trim().equals("T4") )
						    		{
						    			sqlSum = sqlSum + " And P_Payment.PaymentTypeDoc  <> '" + P_Payment.PAYMENTTYPEDOC_SeparationPay + "'";  
						    		}
*/
									sqlSum = sqlSum + "	)" +
									 ") DTX"; 
					    }

					    if ( Form_Field.getFormFieldType().trim().equals("8"))
					    {
					        sqlSum = "Select sum( QuantityCalc ) From P_Payment_Gain , P_Payment Where P_Payment_Gain.P_Payment_ID = P_Payment.P_Payment_ID AND P_Payment.P_Employee_ID = " + this.getP_Employee_ID() 
					               + " And P_Payment.P_Year_ID = " + this.getP_Year_ID()
					               + " And P_Gain_ID In ( Select P_Gain_ID From P_Form_Field_Gain Where P_Form_Field_Gain.isactive = 'Y' and P_Form_Field_ID = " + Form_Field.getP_Form_Field_ID() + ")";
							if ( Employer != null )
								sqlSum = sqlSum + " And P_Payment.P_Employer_ID =  " + Employer.getP_Employer_ID();	    
							if ( Taxation_Region != null )
								sqlSum = sqlSum + " And P_Payment.Taxation_Region_ID =  " + Taxation_Region.getC_Region_ID() ;	    

							if ( PeriodID != 0)
								sqlSum = sqlSum + " and P_Payment.P_Period_ID <= " +  PeriodID; 

						    //2008.10.28
/*				    		if (Form.getValue().trim().equals("T4A") )
							{
				    			sqlSum = sqlSum + " And P_Payment.PaymentTypeDoc  = '" + P_Payment.PAYMENTTYPEDOC_SeparationPay + "'";  
							}
				    		else if (Form.getValue().trim().equals("T4") )
				    		{
				    			sqlSum = sqlSum + " And P_Payment.PaymentTypeDoc  <> '" + P_Payment.PAYMENTTYPEDOC_SeparationPay + "'";  
				    		}
*/
					    }


					    
					    PreparedStatement pstmtSum  = null;
						try
						{

							log.info("FormGenerate - sqlSum - Open - Field: " + Form_Field.getValue() + " - "+  sqlSum );

							pstmtSum = DB.prepareStatement (sqlSum, null);
							ResultSet rsSum = pstmtSum.executeQuery ();
							if ( rsSum.next() )
							{
								BigDecimal amount = rsSum.getBigDecimal(1);
								if ( amount == null )
									amount = Env.ZERO;
								
								// SM 2007-11-07
								// Enlever les plug des montants admissibles, remplacé par les get sur taxTable
								// Trouver maximum des gains admissibles au RQAP
								if (Form.getValue().trim().equals("R1") && Form_Field.getValue().equals("I"))
								{
									if ( amount != null && amount.compareTo( (BigDecimal)taxTable.get( "RQAP.1")) > 0 )
										amount = (BigDecimal)taxTable.get( "RQAP.1");
								}

								if (Form.getValue().trim().equals("R1") && Form_Field.getValue().trim().equals("G"))
								{
									if ( amount != null && amount.compareTo( (BigDecimal)taxTable.get( "RRQ2.2")) > 0 )
										amount = (BigDecimal)taxTable.get( "RRQ2.2");
								}

								
								// Trouver maximum des gains admissibles au RQAP
								if (Form.getValue().trim().equals("T4") && Form_Field.getValue().equals("56") )
								{
									if ( amount != null && amount.compareTo( (BigDecimal)taxTable.get( "RQAP.1")) > 0 )
										amount = (BigDecimal)taxTable.get( "RQAP.1");
									
								}
								// Trouver maximum des gains admissibles à l'assurance emploi
								if (Form.getValue().trim().equals("T4") && Form_Field.getValue().equals("24") )
								{
									if ( amount != null && amount.compareTo( (BigDecimal)taxTable.get( "AE.1")) > 0 )
										amount = (BigDecimal)taxTable.get( "AE.1");
									
									BigDecimal Cum24 = getCumulatif( Form_Field );
									if ( amount != null && ( Cum24.add( amount )).compareTo( (BigDecimal)taxTable.get( "AE.1")) > 0  )
										amount = ((BigDecimal)taxTable.get( "AE.1")).subtract( Cum24 );
									
									
								}
								
								// Trouver maximum des gains admissibles à RRQ et RPC
								if (Form.getValue().trim().equals("T4") && Form_Field.getValue().equals("26") )
								{
									if ( amount != null && amount.compareTo( (BigDecimal)taxTable.get( "RPC2.2")) > 0 )
										amount = (BigDecimal)taxTable.get( "RPC2.2");

									BigDecimal Cum26 = getCumulatif( Form_Field );
									if ( amount != null && ( Cum26.add( amount )).compareTo( (BigDecimal)taxTable.get( "RPC2.2")) > 0  )
										amount = ((BigDecimal)taxTable.get( "RPC2.2")).subtract( Cum26 );

								}

								// Case 52 du T4 devrait avoir le montant arrondit. 
								if (Form.getValue().trim().equals("T4") && Form_Field.getValue().equals("52") )
								{
									amount = amount.setScale(0, BigDecimal.ROUND_HALF_UP);
								}

								// Exception pour les indiens incrit, la case 14 dans être inscrit dans la cade "71"
								if ( Form.getValue().trim().equals("T4") && Form_Field.getValue().equals("14") && Employee.isStatusIndian() )
								{
									AmountForStatusIndian = amount;
									amount = Env.ZERO;
//									Form_Employee_Detail.setP_Form_Field_ID( P_Form_Field.getField("71"));
								}

								if ( Form.getValue().trim().equals("T4") && Form_Field.getValue().equals("71") && Employee.isStatusIndian() )
								{
									amount = AmountForStatusIndian;
								}
/*
								// Code 57: Revenus d'emploi - 15 mars au 9 mai
								// Code 58: Revenus d'emploi - 10 mai au 4 juillet
								// Code 59: Revenus d'emploi - 5 juillet au 29 aout
								// Code 60: Revenus d'emploi - 30 aout au 26 septembre
								if (Form.getValue().trim().equals("T4") 
										&& (   Form_Field.getValue().equals("57")
											|| Form_Field.getValue().equals("58")	
											|| Form_Field.getValue().equals("59")	
											|| Form_Field.getValue().equals("60")	
											)
									)
								{
										amount = getAmountPCU( Form_Field.getValue() );
									
								}
*/
								
								Form_Employee_Detail.setFormFieldAmount( amount);
								
								
								
							}
							rsSum.close ();
							pstmtSum.close ();
							pstmtSum = null;

							log.info("FormGenerate - sqlSum - close "  );

						}
						catch (Exception e)
						{
							log.log (Level.SEVERE, "FormGenerate Sum - " + sqlSum, e);
						}

						// 
						// 2iem select pour la même case
						//
						if ( sqlSum2 != null)
						{
							try
							{
							pstmtSum = DB.prepareStatement (sqlSum2, null);
							ResultSet rsSum = pstmtSum.executeQuery ();
							if ( rsSum.next() )
							{
								BigDecimal amount = rsSum.getBigDecimal(1);

								if ( amount == null )
									amount = Env.ZERO;
								
								if ( Form_Employee_Detail.getFormFieldAmount() != null )
									amount = Form_Employee_Detail.getFormFieldAmount().add( amount );
								
								// SM 2007-11-07
								// Enlever les plug des montants admissibles, remplacé par les get sur taxTable
								// Trouver maximum des gains admissibles au RQAP
								if (Form.getValue().trim().equals("R1") && Form_Field.getValue().equals("I"))
								{
									if ( amount != null && amount.compareTo( (BigDecimal)taxTable.get( "RQAP.1")) > 0 )
										amount = (BigDecimal)taxTable.get( "RQAP.1");
								}

								if (Form.getValue().trim().equals("R1") && Form_Field.getValue().trim().equals("G"))
								{
									if ( amount != null && amount.compareTo( (BigDecimal)taxTable.get( "RRQ2.2")) > 0 )
										amount = (BigDecimal)taxTable.get( "RRQ2.2");
								}

								
								// Trouver maximum des gains admissibles au RQAP
								if (Form.getValue().trim().equals("T4") && Form_Field.getValue().equals("56") )
								{
									if ( amount != null && amount.compareTo( (BigDecimal)taxTable.get( "RQAP.1")) > 0 )
										amount = (BigDecimal)taxTable.get( "RQAP.1");
									
								}
								// Trouver maximum des gains admissibles à l'assurance emploi
								if (Form.getValue().trim().equals("T4") && Form_Field.getValue().equals("24") )
								{
									if ( amount != null && amount.compareTo( (BigDecimal)taxTable.get( "AE.1")) > 0 )
										amount = (BigDecimal)taxTable.get( "AE.1");
									
									BigDecimal Cum24 = getCumulatif( Form_Field );
									if ( amount != null && ( Cum24.add( amount )).compareTo( (BigDecimal)taxTable.get( "AE.1")) > 0  )
										amount = ((BigDecimal)taxTable.get( "AE.1")).subtract( Cum24 );
									
									
								}
								
								// Trouver maximum des gains admissibles à RRQ et RPC
								if (Form.getValue().trim().equals("T4") && Form_Field.getValue().equals("26") )
								{
									if ( amount != null && amount.compareTo( (BigDecimal)taxTable.get( "RPC2.2")) > 0 )
										amount = (BigDecimal)taxTable.get( "RPC2.2");

									BigDecimal Cum26 = getCumulatif( Form_Field );
									if ( amount != null && ( Cum26.add( amount )).compareTo( (BigDecimal)taxTable.get( "RPC2.2")) > 0  )
										amount = ((BigDecimal)taxTable.get( "RPC2.2")).subtract( Cum26 );

								}

								// Case 52 du T4 devrait avoir le montant arrondit. 
								if (Form.getValue().trim().equals("T4") && Form_Field.getValue().equals("52") )
								{
									amount = amount.setScale(0, BigDecimal.ROUND_HALF_UP);
								}

								// Exception pour les indiens incrit, la case 14 dans être inscrit dans la cade "71"
								if ( Form.getValue().trim().equals("T4") && Form_Field.getValue().equals("14") && Employee.isStatusIndian() )
								{
									AmountForStatusIndian = amount;
									amount = Env.ZERO;
//									Form_Employee_Detail.setP_Form_Field_ID( P_Form_Field.getField("71"));
								}

								if ( Form.getValue().trim().equals("T4") && Form_Field.getValue().equals("71") && Employee.isStatusIndian() )
								{
									amount = AmountForStatusIndian;
								}
								
								Form_Employee_Detail.setFormFieldAmount( amount );
									
							}
							}
							catch (Exception e)
							{
								log.log (Level.SEVERE, "FormGenerate Sum - " + sqlSum, e);
							}
						}

				    
				    }
				    else if (Form_Field.getFormFieldType().equals("5"))
				    {
			    		Form_Employee_Detail.setFormFieldText(Form_Field.getFormFieldTypeText());
				    }
				    else if ( Form_Field.getFormFieldType().trim().equals("9"))
				    {
				    	if ( isException( Form_Field.getP_Form_Field_ID(),Employee.getP_Employee_ID() ) )
				    		Form_Employee_Detail.setFormFieldText("X" );
				    }

					if (Form.getValue().trim().equals("R1") && Form_Field.getValue().trim().equals("NO"))
					{
			    		Form_Employee_Detail.setFormFieldText( GetOriginalNumber( ) );
					}

				    Form_Employee_Detail.save();
				}
				rs.close ();
				pstmt.close ();
				pstmt = null;
					
			}
			catch (Exception e)
			{
				log.log (Level.SEVERE, "FormGenerate - " + sql, e);
			}
	        
		}

	    return true;
	}  //	afterSave	
    
	
	private BigDecimal getCumulatif( P_Form_Field Form_Field )
	{
		BigDecimal amount = Env.ZERO;
		String sql = "Select isnull(sum( FormFieldAmount ),0) FROM P_Form_Employee_Detail, P_Form_Employee " 
				   + " WHERE P_Form_Employee_Detail.P_Form_Employee_ID = P_Form_Employee.P_Form_Employee_ID "
				   + "  AND P_Form_Field_ID = " + Form_Field.getP_Form_Field_ID()
				   + "  AND P_Form_Employee.P_Form_ID = " + Form_Field.getP_Form_ID()
				   + "  AND P_Form_Employee.P_Employee_ID = " + this.getP_Employee_ID()
//2008-09-16
				   + "  AND P_Form_Employee.P_Year_ID = " + this.getP_Year_ID()
				   + "  AND P_Form_Employee.P_Form_Employee_ID != " + this.getP_Form_Employee_ID()
				   ;
		
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				amount = rs.getBigDecimal(1); 
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"getCumulatif, - " + sql, e);
		}
		return amount;
	}


	private String GetDeductionType(int iP_Form_Field_ID)
	{
		String DeductionType="2";
		String sql = "Select Isnull(Max(Isnull(DeductionType,'2')),'2') From P_Form_Field_Deduction where P_Form_Field_id =" + iP_Form_Field_ID; 
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				DeductionType = rs.getString(1); 
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"GetDeductionType, - " + sql, e);
		}
		return DeductionType;
	}
	
	private String GetEmployeePensionNumber(int iP_Form_Field_ID, int iP_Employee_ID)
	{
		String PensionNumber="";
		String sql = "Select Isnull(RRPNumber,'') From P_Deduction D " +
					 " inner join P_Payment_Deduction PD On D.P_Deduction_ID = PD.P_Deduction_ID " +
					 " inner join P_Payment P On PD.P_Payment_ID = P.P_Payment_ID " +
					 " Where " + 
					 " D.P_Deduction_ID In ( Select P_Deduction_ID From P_Form_Field_Deduction where P_Form_Field_Deduction.IsActive = 'Y' and P_Form_Field_id =" + iP_Form_Field_ID + ") " +
					 " And P.P_Year_ID = " + this.getP_Year_ID() +
					 " And P.P_Employee_ID = " + iP_Employee_ID +
					 " Group by RRPNumber";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				PensionNumber = rs.getString(1);
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"GetEmployeePensionNumber, - " + sql, e);
		}
		return PensionNumber.trim();
	}

	
	public String GetFormEmployeeNumber( P_Form Form )
	{
		String StringFormNumber="";
		int FormNumber = 0;

		if ( ! ( Form.getValue().trim().equals("R1") ||  Form.getValue().trim().equals("R2") || Form.getValue().trim().equals("R3")) )
		  return null;
	
		String sql = " Select P_Form_Param_ID From P_Form_Param Where Type = 'P'";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				P_Form_Param FormParam = P_Form_Param.get( Env.getCtx(), rs.getInt( "P_Form_Param_ID"), null);

				if ( Form.getValue().trim().equals("R1") )
				{
					FormNumber =  FormParam.getFormNumber_RL1();
					FormParam.setFormNumber_RL1(FormNumber + 1 );
				}

				if ( Form.getValue().trim().equals("R2") )
				{
					FormNumber = FormParam.getFormNumber_RL2();
					FormParam.setFormNumber_RL2(FormNumber + 1);
				}
				
				if ( Form.getValue().trim().equals("R3") )
				{
					FormNumber = FormParam.getFormNumber_RL3();
					FormParam.setFormNumber_RL3(FormNumber + 1);
				}
				FormParam.save();
				
				int NumberControl= 0 ;
				NumberControl = 7 * ( FormNumber / 7 );
				NumberControl = FormNumber -  NumberControl ;
				
				StringFormNumber = String.valueOf( FormNumber ) + String.valueOf( NumberControl ).trim();
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"GetFormEmployeeNumber, - " + sql, e);
		}
		return StringFormNumber;
	}

	public String GetFormEmployeeFacSimileNumber()
	{
		String StringFormNumber="";
		BigDecimal FormNumber =new BigDecimal(0);
		String sql = " Select FacSimileNumber From P_Form_Param Where Type = 'P'";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				BigDecimal NumberControl=new BigDecimal(-1);
				StringFormNumber = rs.getString(1);
				FormNumber = new BigDecimal(StringFormNumber);
				
				NumberControl = new BigDecimal(7).multiply(FormNumber.divide(new BigDecimal(7),0,BigDecimal.ROUND_FLOOR));
				NumberControl = FormNumber.subtract(NumberControl);

				sql = "Update P_Form_Param set FacSimileNumber= str(convert(int," + StringFormNumber + ") + 1,8) Where  Type = 'P'";
				pstmt = DB.prepareStatement (sql, null);
				pstmt.execute();
				
				StringFormNumber += NumberControl.toString().trim();
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"GetFormEmployeeFacSimileNumber, - " + sql, e);
		}
		return StringFormNumber;
	}

	private Hashtable<?, ?> getTaxTable( int Year_ID )
	{
		Hashtable<?, ?> 	taxTable = new Hashtable<Object, Object>(); 

		String sql = null;
		taxTable.clear();
		Timestamp CurrentDate = null;
		int taxID = 0;
		// Va chercher la dernière date de paiement sur les prériodes de paie
		// de l'année à générer
		sql = "Select max(paydate) Paydate from p_period where P_Year_ID = " + Year_ID ;
		PreparedStatement pstmt = null;

		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				CurrentDate = rs.getTimestamp("Paydate") ;
			}

			// Va chercher les tables de taxes pour avoir les maximums admissibles
			taxID = Calcul_federal_tax.get_federal_tax_id( CurrentDate );
			taxTable = Calcul_federal_tax.get_federal_tax_para(  taxID, CurrentDate);

		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, "doIt PAYDATE - " + sql, e);
		}
		
//		taxTable = CalculNet.getTaxTable();
//		Hashtable 	taxTable = CalculNet.getTaxTable();
		
		return taxTable;
	}

	private boolean isException( int iP_Form_Field_ID, int iP_Employee_ID ) 
	{
		boolean isException = false; 
		String sql = "SELECT IsExonerated from P_employee_deduction Where IsExonerated = 'Y' "
			       + " And P_Deduction_ID In ( Select P_Deduction_ID From P_Form_Field_Deduction where P_Form_Field_Deduction.isactive = 'Y' and P_Form_Field_id =" + iP_Form_Field_ID + ") " 
			       + " And P_Employee_ID = " + iP_Employee_ID
			       ;

		PreparedStatement pstmt = null;
		try
		{

			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next() )
			{ 
				isException = true;
			}
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, "getException - " + sql, e);
		}

		return isException;
	}


	public String GetOriginalNumber(  )
	{
//		if ( this.getFormType().equals(P_Form_Employee.FORMTYPE_Original)  )
//			return "";
		
		String sql = " Select FormNumber From P_Form_Employee Where P_Employee_ID = " + this.getP_Employee_ID() 
		           + " and P_Year_ID = " + this.getP_Year_ID()
		           + " and P_Form_ID = " + this.getP_Form_ID()
				   + " and FormType = '" + P_Form_Employee.FORMTYPE_Original + "'"
				   ;

		String StringFormNumber = "";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				StringFormNumber = rs.getString( "FormNumber");
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"GetFormEmployeeNumber, - " + sql, e);
		}
		return StringFormNumber;
		
	}
	
	private Timestamp getLastDate( int Year_ID)
	{
		String sql = null;
		Timestamp CurrentDate = null;
		int taxID = 0;
		// Va chercher la dernière date de paiement sur les prériodes de paie
		// de l'année à générer
		sql = "Select max(enddate) EndDate from p_period where P_Year_ID = " + Year_ID ;
		PreparedStatement pstmt = null;

		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				CurrentDate = rs.getTimestamp("EndDate") ;
			}


		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, "doIt PAYDATE - " + sql, e);
		}
		
		return CurrentDate;
		
	}
	
}	//	P_Form_Employee
