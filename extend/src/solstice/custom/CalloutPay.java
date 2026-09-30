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
package solstice.custom;

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;

import org.compiere.model.CalloutEngine;

import solstice.model.P_Job_Title;
import solstice.model.P_Occupation_Group;
import solstice.model.P_Payment_Group;
import solstice.model.P_Assignment_Param;
import solstice.model.P_Salary_Scale;
import solstice.model.P_Employee;
import solstice.model.P_Assignment;
import solstice.model.P_Frequency;
import solstice.model.P_Period;
import javax.swing.JOptionPane;
import java.awt.Frame;

/**
 * Paie Callouts
 * 
 * @author M.Dufour
 */
public class CalloutPay extends CalloutEngine
{
	private void setInfoFromOccupationGroup( Properties ctx, GridTab mTab, int P_Occupation_Group_ID )
	{
		P_Occupation_Group OccupationGroup = P_Occupation_Group.get( ctx, P_Occupation_Group_ID, null);
		if ( OccupationGroup != null )
		{
			if ( OccupationGroup.getWeekly_Hours() != null)
				mTab.setValue("Weekly_Hours", OccupationGroup.getWeekly_Hours());
			if ( OccupationGroup.getDay_Hours() != null && OccupationGroup.getDay_Hours().compareTo(Env.ZERO) != 0 )
				mTab.setValue("Day_Hours", OccupationGroup.getDay_Hours());
	        if ( OccupationGroup.getRemuneration_Method() != null)
	        	mTab.setValue("Remuneration_Method", OccupationGroup.getRemuneration_Method());
		}
		
	}

	private void setInfoFromJobTitle( Properties ctx, GridTab mTab, int P_Job_Title_ID )
	{
		P_Job_Title Job_Title = P_Job_Title.get( ctx, P_Job_Title_ID, null);
		if ( Job_Title != null )
		{
			if ( Job_Title.getWeekly_Hours() != null )
				mTab.setValue("Weekly_Hours", Job_Title.getWeekly_Hours());
			if ( Job_Title.getWeekly_Hours() != null)
				mTab.setValue("Day_Hours", Job_Title.getWeekly_Hours().divide( new BigDecimal(5)));
	        if ( Job_Title.getRemuneration_Method() != null)
		        mTab.setValue("Remuneration_Method", Job_Title.getRemuneration_Method());
		}
		
	}

	/**
     * occupation_group modified Set Attribute Set Instance
     * 
     * @param ctx
     *            Context
     * @param WindowNo
     *            current Window No
     * @param mTab
     *            Model Tab
     * @param mField
     *            Model Field
     * @param value
     *            The new value
     * @return Error message or ""
     */
    public String occupation_group(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
    {
    	if ( mTab.getValue("P_Occupation_Group_ID") != null )
    		setInfoFromOccupationGroup( ctx, mTab, Integer.parseInt( mTab.getValue("P_Occupation_Group_ID").toString()) );

    	//2012.07.18 reinitialise le titre d'emploi
    	//2013.02.06 seulement si le groupe d'emploi est différent.
    	if ( mTab.getValue("P_Job_Title_ID") != null )
    	{
        	P_Job_Title JobTitle = P_Job_Title.get(ctx, Integer.parseInt( mTab.getValue("P_Job_Title_ID").toString()), null);
        	if ( JobTitle.getP_Occupation_Group_ID() != Integer.parseInt( mTab.getValue("P_Occupation_Group_ID").toString()) )
        		mTab.setValue("P_Job_Title_ID", null);
    	}

        return "";
    } //  occupation_group

    public String payment_employee(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
    {

        if (value != null)
        {
            //on monte le select
            String sql = "SELECT P_PAYMENT_GROUP_ID, PAYMENTTYPE, P_FINANCIAL_INSTITUTION_ID, TRANSIT, FOLIO, P_Employer_ID " +
            			" FROM P_Employee " +
            			" WHERE P_Employee_ID = " + value.toString();

            // on execute le select
            try
            {
                //on ajoute les paramètres
                PreparedStatement pstmt = DB.prepareStatement(sql, null);

                //on execute
                ResultSet rs = pstmt.executeQuery();
                //on boucle dans les résultats
                if (rs.next())
                {
                    mTab.setValue("P_Payment_Group_ID", rs.getBigDecimal(1));
                    mTab.setValue("P_Employer_ID", rs.getBigDecimal(6));
                    //               mTab.setValue("PaymentType", rs.getBigDecimal(2));
                    //               mTab.setValue("P_Financial_Institution_ID",
                    // rs.getString(3));
                    //               mTab.setValue("Transit", rs.getString(4));
                    //               mTab.setValue("Folio", rs.getString(5));
                }
                rs.close();
                pstmt.close();
            }
            catch (SQLException e)
            {
                log.log(Level.SEVERE, "payment_employee", e);
                return e.getLocalizedMessage();
            }
        }

        return "";
    } //  payment_employee

    /**
     * salary_scale modified Set Attribute Set Instance
     * 
     * @param ctx
     *            Context
     * @param WindowNo
     *            current Window No
     * @param mTab
     *            Model Tab
     * @param mField
     *            Model Field
     * @param value
     *            The new value
     * @return Error message or ""
     */
    public String salary_scale(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
    {
    	
        if (value != null)
        {
            //on monte le select
            String sql = "Select P_Salary_Scale.P_Occupation_Group_Id, P_Occupation_Group.Weekly_Hours, " +
            				" P_Occupation_Group.Day_Hours, P_Salary_Scale.Remuneration_Method, P_Job_Title_Id, " +
            				" P_Salary_Class_ID, P_Collective_Labour_Agr_ID, ScaleDate, P_Salary_Scale.P_Salary_Scale_ID, DBO.FN_P_SalaryScaleWithBorn(P_Salary_Scale.P_Salary_Scale_ID) CheckBorn " +
                			" From P_Salary_Scale" +
                			"  left outer join P_Occupation_Group ON P_Occupation_Group.P_Occupation_Group_ID = P_Salary_Scale.P_Occupation_Group_ID " + 
    						" Where  P_Salary_Scale.P_Salary_Scale_Id = " + value.toString();
            
            //on execute le select
            try
            {
                //on ajoute les paramètres
                PreparedStatement pstmt = DB.prepareStatement(sql, null);

                //on execute
                ResultSet rs = pstmt.executeQuery();
                //on boucle dans les résultats
                if (rs.next())
                {
                	if ( rs.getObject("P_Occupation_Group_ID") != null )
                		mTab.setValue("P_Occupation_Group_ID", rs.getObject("P_Occupation_Group_ID"));
                    if ( rs.getObject("P_Job_Title_ID") != null )
                    	mTab.setValue("P_Job_Title_ID", rs.getObject("P_Job_Title_ID"));
                    mTab.setValue("P_Salary_Class_ID", rs.getObject("P_Salary_Class_ID"));
                    if ( rs.getInt("P_Collective_Labour_Agr_ID") != 0)
                    	mTab.setValue("P_Collective_Labour_Agr_ID", rs.getObject("P_Collective_Labour_Agr_ID"));
//                    mTab.setValue("Weekly_Hours", rs.getObject("Weekly_Hours"));
//                    mTab.setValue("Day_Hours", rs.getObject("Day_Hours"));
//                    mTab.setValue("Remuneration_Method", rs.getObject("Remuneration_Method"));
                    mTab.setValue("CheckBorn", rs.getObject("CheckBorn"));
                    //                mTab.setValue("ScaleDate", rs.getDate(8));
                    //                mTab.setValue("P_Salary_Scale_ID", rs.getBigDecimal(9));
//                    mTab.setValue("P_Salary_Scale_Detail_ID", null);
                }
                rs.close();
                pstmt.close();
            }
            catch (SQLException e)
            {
                log.log(Level.SEVERE, "p_salary_scale", e);
                return e.getLocalizedMessage();
            }
        }

        //2012.07.18 initialise avec le 1er echelon qu'on trouve.
 //       if (  Integer.parseInt( mTab.getValue(  "P_Salary_Scale_ID").toString()) != CurrentP_Salary_Scale_ID)
 //       {

          //2012.08.17  
          if ( mTab.getValue(  "P_Salary_Scale_ID") == null)
              return "";

          //2012.10.26 dans le cas d'une copie d'enregistrement.
          if ( mTab.getValue(  "P_Salary_Scale_Detail_ID") != null)
              return "";

          String sql = "Select top 1 P_Salary_Scale_Detail_ID From P_Salary_Scale_Detail Where P_Salary_Scale_ID = " + mTab.getValue(  "P_Salary_Scale_ID").toString() + " Order BY STEP ";
          try
          {
              PreparedStatement pstmt = DB.prepareStatement(sql, null);
              ResultSet rs = pstmt.executeQuery();
              if (rs.next())
              {
          		mTab.setValue("P_Salary_Scale_Detail_ID", rs.getInt("P_Salary_Scale_Detail_ID"));
              }
              rs.close();
              pstmt.close();
          }
          catch (SQLException e)
          {
              log.log(Level.SEVERE, "p_salary_scale", e);
              return e.getLocalizedMessage();
          }
        	
  //      }
        	


        return "";
    } //  salary_scale

    /**
     * salary_scale_detail modified Set Attribute Set Instance
     * 
     * @param ctx
     *            Context
     * @param WindowNo
     *            current Window No
     * @param mTab
     *            Model Tab
     * @param mField
     *            Model Field
     * @param value
     *            The new value
     * @return Error message or ""
     */
    public String salary_scale_detail(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
    {
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);

		if (value != null)
        {
            //on monte le select
        	// 
            String sql = "Select P_Salary_Scale.P_Occupation_Group_Id, P_Salary_Scale.P_Salary_Scale_ID, P_Occupation_Group.Weekly_Hours, " +
            				" P_Occupation_Group.Day_Hours, P_Salary_Scale.Remuneration_Method, P_Salary_Class_ID, P_Collective_Labour_Agr_ID, " +
							" Annual_Salary, Hourly_Rate, P_Salary_Scale.P_Job_Title_Id " + 
							" From P_Salary_Scale_Detail" +
							"  inner join P_Salary_Scale ON P_Salary_Scale.P_Salary_Scale_ID = P_Salary_Scale_Detail.P_Salary_Scale_ID  " +
							"  left outer join P_Occupation_Group ON P_Occupation_Group.P_Occupation_Group_ID = P_Salary_Scale.P_Occupation_Group_ID " + 
							" Where P_Salary_Scale_Detail.P_Salary_Scale_Detail_Id = " + value.toString();
            
            //on execute le select
            try
            {
                //on ajoute les paramètres
                PreparedStatement pstmt = DB.prepareStatement(sql, null);

//                String[] fields = new String[]
//                {  "P_Occupation_Group_ID","P_Salary_Scale_ID",  "Weekly_Hours", "Day_Hours", "Remuneration_Method",  "P_Salary_Class_ID", "P_Collective_Labour_Agr_ID",
//                        "Annual_Salary", "Hourly_Rate" };

                // , "P_Salary_Scale_ID", "P_Job_Title_ID",

                //on execute
                ResultSet rs = pstmt.executeQuery();
                //on boucle dans les résultats
                if (rs.next())
                {
                	
                	if ( rs.getObject("P_Occupation_Group_ID") != null )
                		mTab.setValue("P_Occupation_Group_ID", rs.getObject( "P_Occupation_Group_ID" ));
                	if ( rs.getObject( "P_Job_Title_ID" ) != null)
                    	mTab.setValue("P_Job_Title_ID", rs.getObject( "P_Job_Title_ID" ));
               		
                    if ( rs.getInt("P_Collective_Labour_Agr_ID") != 0)
                    	mTab.setValue("P_Collective_Labour_Agr_ID", rs.getObject( "P_Collective_Labour_Agr_ID" ));

                    //2012.07.18
                    if ( ! rs.getObject( "P_Salary_Scale_ID" ).equals( mTab.getValue("P_Salary_Scale_ID")) )
                    	mTab.setValue("P_Salary_Scale_ID"    , rs.getObject( "P_Salary_Scale_ID" ));
                    
                	mTab.setValue("P_Salary_Class_ID"    , rs.getObject( "P_Salary_Class_ID" ));
                	mTab.setValue("Remuneration_Method"  , rs.getObject( "Remuneration_Method" ));

                	if ( rs.getString( "Remuneration_Method").equals( P_Salary_Scale.REMUNERATION_METHOD_Annual) && rs.getObject( "Annual_Salary" ) != null && rs.getBigDecimal( "Annual_Salary" ).compareTo(Env.ZERO) != 0)
                		mTab.setValue("Annual_Salary"        , rs.getObject( "Annual_Salary" ));

                    if ( rs.getString( "Remuneration_Method").equals( P_Salary_Scale.REMUNERATION_METHOD_Hourly) && rs.getObject( "Hourly_Rate" ) != null && rs.getBigDecimal( "Hourly_Rate" ).compareTo(Env.ZERO) != 0)
                		mTab.setValue("Hourly_Rate"         , rs.getObject( "Hourly_Rate" ));

                    //TODO analyser s'il ne serait pas mieux d'avoir un champs 
                   	if ( rs.getString( "Remuneration_Method").equals( P_Salary_Scale.REMUNERATION_METHOD_Daily) && rs.getObject( "Annual_Salary" ) != null && rs.getBigDecimal( "Annual_Salary" ).compareTo(Env.ZERO) != 0)
                		mTab.setValue("Daily_Salary"         , rs.getObject( "Annual_Salary" ));

                }
                rs.close();
                pstmt.close();
            }
            catch (SQLException e)
            {
                log.log(Level.SEVERE, "p_salary_scale_detail", e);
                return e.getLocalizedMessage();
            }
            
            setCalloutActive(false);
            
            calculSalary(ctx, WindowNo,  mTab,  mField, value);
        }

        return "";
    } //  salary_scale_detail

    /**
     * salary_scale_occupation_group modified Set Attribute Set Instance From
     * window salary_scale, field occupation group
     * 
     * @param ctx
     *            Context
     * @param WindowNo
     *            current Window No
     * @param mTab
     *            Model Tab
     * @param mField
     *            Model Field
     * @param value
     *            The new value
     * @return Error message or ""
     */
    public String salary_scale_occupation_group(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
    {
        if (value != null)
        {
            //on monte le select
            String sql = "SELECT Name FROM P_Job_Title WHERE P_Occupation_Group_ID = " + value.toString();
            log.log(Level.INFO, "********" + sql);
            //on execute le select

            try
            {
                //on ajoute les paramètres
                PreparedStatement pstmt = DB.prepareStatement(sql, null);

                //on execute
                ResultSet rs = pstmt.executeQuery();
                //on boucle dans les résultats
                if (rs != null)
                    while (rs.next())
                    {
                        mTab.setValue("Job_Title", rs.getString(1));
                        log.log(Level.INFO, "!!!!!!" + rs.getString(1));
                    }
                rs.close();
                pstmt.close();
            }
            catch (SQLException e)
            {
                log.log(Level.SEVERE, "p_salary_scale_occupation_group", e);
                return e.getLocalizedMessage();
            }

        }

        return "";
    } //  salary_scale_occupation_group

	public String calculHourlyRate(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
	    BigDecimal HourlyRate;
	    BigDecimal AnnualSalary;
	    BigDecimal WeeklyHours;
	    String RemunerationMethod;

	   
        WeeklyHours = (BigDecimal) mTab.getValue("Weekly_Hours");
        if ( WeeklyHours.compareTo( Env.ZERO) == 0 )
        {
        	if ( mTab.getValue("P_Job_Title_ID") != null)
        	{
        		setInfoFromJobTitle( ctx, mTab, Integer.parseInt( mTab.getValue("P_Job_Title_ID").toString()) );
            	WeeklyHours = (BigDecimal) mTab.getValue("Weekly_Hours");
        	}

            if ( WeeklyHours.compareTo( Env.ZERO) == 0 && mTab.getValue("P_Occupation_Group_ID") != null )
            {
        		setInfoFromOccupationGroup( ctx, mTab, Integer.parseInt( mTab.getValue("P_Occupation_Group_ID").toString()) );
            	WeeklyHours = (BigDecimal) mTab.getValue("Weekly_Hours");
            }
            if ( WeeklyHours.compareTo( Env.ZERO) == 0 )
            {
            	WeeklyHours = new BigDecimal(35) ;
    			mTab.setValue("Weekly_Hours", WeeklyHours );
    	        mTab.setValue("Day_Hours", WeeklyHours.divide( new BigDecimal(5)));
            }
        	
        }
        	
        
        RemunerationMethod = (String) mTab.getValue("Remuneration_Method");

        // ***
        if (RemunerationMethod == null)
            return "";
        
        
        if (RemunerationMethod.equals(P_Assignment_Param.REMUNERATION_METHOD_Annual) )
        {
            AnnualSalary = (BigDecimal) mTab.getValue("Annual_Salary");
            
            P_Assignment Assignment = P_Assignment.get(ctx,  ((Integer)mTab.getValue("P_Assignment_ID")).intValue(), null);
        	BigDecimal Annual_Increase = null;
    		Annual_Increase = P_Payment_Group.getAnnual_Increase( Env.getCtx(), Assignment.getP_Employee_ID(), null);

    		P_Employee Employee = P_Employee.get(ctx, Assignment.getP_Employee_ID(), null);
    		P_Period Period = P_Period.getOpenPeriodWithPaymentGroup(ctx, Employee.getP_Payment_Group_ID(), null);
    		P_Frequency Frequency = P_Frequency.get(ctx, Period.getP_Frequency_ID(), null ); 

            // Bimensuel
        	if ( Frequency.getNumberOfPeriod() == 24)
        	{
        		BigDecimal NumberOfPeriod = new BigDecimal( Frequency.getNumberOfPeriod() );
        		BigDecimal HoursPerPay = WeeklyHours.multiply( Annual_Increase ).divide(NumberOfPeriod,2,BigDecimal.ROUND_HALF_UP); 
                HourlyRate = AnnualSalary.divide(HoursPerPay, 8, BigDecimal.ROUND_HALF_UP).divide( new BigDecimal(24) , 8, BigDecimal.ROUND_HALF_UP);
                mTab.setValue("HoursPerPay", HoursPerPay);
                mTab.setValue("SalaryPerPay", AnnualSalary.divide(NumberOfPeriod,2,BigDecimal.ROUND_HALF_UP));
        	}
        	else
        	{
        		if ( AnnualSalary != null && WeeklyHours != null && WeeklyHours.compareTo(Env.ZERO) != 0 ) 
        			HourlyRate = AnnualSalary.divide( Annual_Increase , 8, BigDecimal.ROUND_HALF_UP).divide(WeeklyHours, 8, BigDecimal.ROUND_HALF_UP);
        		else
        			HourlyRate = Env.ZERO;
        	}

        	HourlyRate = HourlyRate.setScale(4, BigDecimal.ROUND_HALF_UP);
            mTab.setValue("Hourly_Rate", HourlyRate);
        }

        return "";
    }

	//
	// Appeler dans l'écran d'affectation
	//
    public String calculSalary(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
    {
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);
        if (mTab.getValue("Weekly_Hours") != null && mTab.getValue("Remuneration_Method") != null)
        {
            BigDecimal HourlyRate;
            BigDecimal AnnualSalary = Env.ZERO;
            BigDecimal WeeklyHours;
            String RemunerationMethod;

            //TODO
            //annualite
            P_Assignment Assignment = P_Assignment.get(ctx,  ((Integer)mTab.getValue("P_Assignment_ID")).intValue(), null);
        	BigDecimal Annual_Increase = null;
    		Annual_Increase = P_Payment_Group.getAnnual_Increase( Env.getCtx(), Assignment.getP_Employee_ID(), null);
    		P_Employee Employee = P_Employee.get(ctx, Assignment.getP_Employee_ID(), null);
    		P_Period Period = P_Period.getOpenPeriodWithPaymentGroup(ctx, Employee.getP_Payment_Group_ID(), null);
    		P_Frequency Frequency = P_Frequency.get(ctx, Period.getP_Frequency_ID(), null ); 
    		BigDecimal NumberOfPeriod = new BigDecimal( Frequency.getNumberOfPeriod() );
    		
            WeeklyHours = (BigDecimal) mTab.getValue("Weekly_Hours");
            if (( WeeklyHours == null || WeeklyHours.compareTo(Env.ZERO) == 0 ) && mTab.getValue("Day_Hours") != null )
            {
            	WeeklyHours = ((BigDecimal) mTab.getValue("Day_Hours")).multiply(new BigDecimal(5));
            	mTab.setValue("Weekly_Hours", WeeklyHours);
            }
            if ( WeeklyHours == null || WeeklyHours.compareTo(Env.ZERO) == 0)
            {
                setCalloutActive(false);
            	return "";
            }

            RemunerationMethod = (String) mTab.getValue("Remuneration_Method");

            if (RemunerationMethod == null)
            {
                setCalloutActive(false);
            	return "";
            }

            if (RemunerationMethod.equals(P_Assignment_Param.REMUNERATION_METHOD_Annual) )
            {
                // Bimensuel
            	if ( Frequency.getNumberOfPeriod() == 24)
            	{
            		BigDecimal HoursPerPay = WeeklyHours.multiply( Annual_Increase ).divide(NumberOfPeriod,2,BigDecimal.ROUND_HALF_UP); 
                    AnnualSalary = (BigDecimal) mTab.getValue("Annual_Salary");
                    HourlyRate = AnnualSalary.divide(HoursPerPay, 8, BigDecimal.ROUND_HALF_UP).divide( new BigDecimal(24) , 8, BigDecimal.ROUND_HALF_UP);
                    mTab.setValue("HoursPerPay", HoursPerPay);
                    mTab.setValue("SalaryPerPay", AnnualSalary.divide(NumberOfPeriod,2,BigDecimal.ROUND_HALF_UP));
            	}
            	else
            	{
                    AnnualSalary = (BigDecimal) mTab.getValue("Annual_Salary");
                    HourlyRate = AnnualSalary.divide(WeeklyHours, 8, BigDecimal.ROUND_HALF_UP).divide( Annual_Increase , 8, BigDecimal.ROUND_HALF_UP);
//                  HourlyRate = AnnualSalary.divide(WeeklyHours, 4, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal(52.18), 4, BigDecimal.ROUND_HALF_UP);
            	}
            	HourlyRate = HourlyRate.setScale(4, BigDecimal.ROUND_HALF_UP);
                mTab.setValue("Hourly_Rate", HourlyRate);
            }

            if (RemunerationMethod.equals(P_Assignment_Param.REMUNERATION_METHOD_Hourly ) )
            {
                HourlyRate = (BigDecimal) mTab.getValue("Hourly_Rate");
                AnnualSalary = (WeeklyHours.multiply(HourlyRate).multiply( Annual_Increase )).setScale(2, BigDecimal.ROUND_HALF_UP);
                mTab.setValue("Annual_Salary", AnnualSalary);
                mTab.setValue("SalaryPerPay", AnnualSalary.divide(NumberOfPeriod,2,BigDecimal.ROUND_HALF_UP));
            }

            if (RemunerationMethod.equals(P_Assignment_Param.REMUNERATION_METHOD_Daily ) )
            {
                mTab.setValue("Annual_Salary", Env.ZERO);
                mTab.setValue("Hourly_Rate", Env.ZERO);
            }

        
        }
        setCalloutActive(false);

        return "";
    }

    public String calculAnnualSalary(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
    {
        BigDecimal HourlyRate;
        BigDecimal AnnualSalary = Env.ZERO;
        BigDecimal WeeklyHours;
        String RemunerationMethod;
                
        P_Assignment Assignment = P_Assignment.get(ctx,  ((Integer)mTab.getValue("P_Assignment_ID")).intValue(), null);
    	BigDecimal Annual_Increase = null;
		Annual_Increase = P_Payment_Group.getAnnual_Increase( Env.getCtx(), Assignment.getP_Employee_ID(), null);

		
        WeeklyHours = (BigDecimal) mTab.getValue("Weekly_Hours");
        RemunerationMethod = (String) mTab.getValue("Remuneration_Method");
        if (RemunerationMethod == null  || mTab.getValue("IsOut_Of_Range").toString().equals("false") )
            return "";

		P_Employee Employee = P_Employee.get(ctx, Assignment.getP_Employee_ID(), null);
		P_Period Period = P_Period.getOpenPeriodWithPaymentGroup(ctx, Employee.getP_Payment_Group_ID(), null);
		P_Frequency Frequency = P_Frequency.get(ctx, Period.getP_Frequency_ID(), null ); 
		BigDecimal NumberOfPeriod = new BigDecimal( Frequency.getNumberOfPeriod() );

        
        if (RemunerationMethod.equals(P_Assignment_Param.REMUNERATION_METHOD_Hourly) )
        {
            HourlyRate = (BigDecimal) mTab.getValue("Hourly_Rate");
            AnnualSalary = (WeeklyHours.multiply(HourlyRate).multiply( Annual_Increase )).setScale(2, BigDecimal.ROUND_HALF_UP);
            mTab.setValue("Annual_Salary", AnnualSalary);
            mTab.setValue("SalaryPerPay", AnnualSalary.divide(NumberOfPeriod,2,BigDecimal.ROUND_HALF_UP));
        }
        
        return "";
    }

    /**
     * Cette méthode est lancée lors de la modification du champ P_Employee_ID
     * de la table P_Assignment. On ajuste les valeurs par défaut pour le groupe
     * d'emploi, le titre de l'emploi et la convention
     */
    public String calloutAssignmentEmployee(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
    {
    	// TODO Parametriser HARCODE
    	
    	if ( Env.getAD_Client_ID( Env.getCtx()) != 11)
    	{
        	return "";
    	}
    	
    	// On vérifie si la convention du poste est la meme que celle de l'employe
    	if (value != null && mTab.getValue("P_Post_ID") != null && 
    			PostEmployeeHaveSameConvention(ctx,WindowNo, mTab, mField, value) == false)
    	{
    		JOptionPane optionPane1 = new JOptionPane(Msg.getMsg(Env.getCtx(), "EmployeePostDifferentCollectiveLabour"),
                    JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION);
            optionPane1.createDialog(new Frame(), null).setVisible(true);
    	}
    	
    	// return calloutDefaultAssignmentName(ctx, WindowNo, mTab, mField, value);
    	
    	return "";
    }

    /**
     * Permet de valider si la convention collective du poste est la meme que 
     * celle de l'employe
     * @param ctx
     * @param WindowNo
     * @param mTab
     * @param mField
     * @param value
     * @return true si les conventions sont égales, false sinon
     */
    private boolean PostEmployeeHaveSameConvention(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
    {
    	// On verifie si la convention du poste est la meme que celle de l'employe
    	String sql = "";
    	PreparedStatement pstmt = null; 	
        ResultSet rs = null; 		
        
        sql = "Select P_Employee.P_Collective_Labour_Agr_ID As EmployeeCollective, P_Post.P_Collective_Labour_Agr_ID As PostCollective " +
        		" From P_Employee, P_Post" +
        		" Where P_Employee.P_Employee_ID = " + mTab.getValue("P_Employee_ID") + 
        		" And P_Post.P_Post_ID = " + mTab.getValue("P_Post_ID");
        
        try
        {
        	pstmt = DB.prepareStatement(sql, null);
        	rs = pstmt.executeQuery();
        	
        	if (rs.next())
        	{
        		int EmployeeCollectiveID = rs.getInt(1);
        		int PostCollectiveID = rs.getInt(2);
        		
        		sql = null;
        		pstmt.close();
        		rs.close(); 
        		//2010.12.14 ajout de la condition, validation s'il y a une convention sur le poste.
        		if (PostCollectiveID != 0 && EmployeeCollectiveID != PostCollectiveID && mTab.getValue("IsActive").equals("Y" ) )
        			return false;
        		else
        			return true;	
        	}	
        }
        catch (Exception e)
        {
        	log.log(Level.SEVERE, "CalloutPay - PostEmployeeHaveSameConvention - " + sql, e);
        }
        
        return false;
    }

    /**
     * Cette méthode est lancée lors de la modification du champ P_Post_ID de la
     * table P_Assignment. On ajuste la valeur par défaut pour le lieu de
     * travail
     */
    public String calloutAssignmentPost(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
    {
    	
// TODO Parametriser HARCODE.    	
    	if ( Env.getAD_Client_ID( ctx ) == 11)
    	{
        	// On vérifie si la convention du poste est la meme que celle de l'employe
        	if (value != null && mTab.getValue("P_Employee_ID") != null && 
        			PostEmployeeHaveSameConvention(ctx,  WindowNo, mTab, mField, value) == false)
        	{
        		JOptionPane optionPane1 = new JOptionPane(Msg.getMsg(Env.getCtx(), "EmployeePostDifferentCollectiveLabour"),
                        JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION);
                optionPane1.createDialog(new Frame(), null).setVisible(true);
        	}
    	}
    	
    	
    	// **** On affiche les infos relatives au poste (les champs ColumnSQL 
    	// **** ds compiere): PostJobTitle, PostActivity, JobTitleWeeklyHours 
    	String sql = null;
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	
    	// Afficher le titre d'emploi relié au poste de l'affectation 
    	try 
    	{
    		sql = "Select P_Job_Title_ID " +
					" From P_Post " +
					" Where P_Post_ID = " + mTab.getValue("P_Post_ID");
    		
    		pstmt = DB.prepareStatement(sql, null);
    		rs = pstmt.executeQuery();
    		
    		if (rs.next())
    			mTab.setValue("PostJobTitle", rs.getObject(1));
    		
    		rs.close();
        	pstmt.close();
    	}
    	catch (Exception e)
    	{
    		log.log(Level.SEVERE, "CalloutPay - PostEmployeeHaveSameConvention - " + sql, e);
    	}
    	
    	// Afficher l'unité administrative reliée au poste de l'affectation
    	try
    	{	
    		sql = "Select C_Activity_ID " +
	    			" From P_Post " +
	    			" Where P_Post_ID = " + mTab.getValue("P_Post_ID");
    		
    		pstmt = DB.prepareStatement(sql, null);
    		rs = pstmt.executeQuery();
    		
    		if (rs.next())
    			mTab.setValue("PostActivity", rs.getObject(1));
    		
    		rs.close();
        	pstmt.close();
    	}
    	catch (Exception e)
    	{
    		log.log(Level.SEVERE, "CalloutPay - PostEmployeeHaveSameConvention - " + sql, e);
    	}
    	    	
    	// Afficher les heures hebdomadaires reliée au titre d'emploi du poste
    	try
    	{	
    		sql = "Select Weekly_Hours " +
	    			" From P_Job_Title " +
	    			" Where P_Job_Title_ID = " + mTab.getValue("PostJobTitle");
    		
    		pstmt = DB.prepareStatement(sql, null);
    		rs = pstmt.executeQuery();
    		
    		if (rs.next())
    			mTab.setValue("JobTitleWeeklyHours", rs.getObject(1));
    		
    		rs.close();
        	pstmt.close();
    	}
    	catch (Exception e)
    	{
    		log.log(Level.SEVERE, "CalloutPay - PostEmployeeHaveSameConvention - " + sql, e);
    	}
    	// ************
    	
    	// return calloutDefaultAssignmentName(ctx, WindowNo, mTab, mField, value);
    	
    	return "";
    }
    

    /**
     * Cette méthode est lancée lors de la modification du champ P_Job_Title_ID
     * de la table P_Assignment. On ajuste les valeurs concernants le nombre
     * d'heures de travail par jour et par semaine. On ajuste également
     * l'échelle salariale.
     */
    public String calloutJobTitle(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
    {
    	if ( mTab.getValue("P_Job_Title_ID") != null)
    		setInfoFromJobTitle( ctx, mTab, Integer.parseInt( mTab.getValue("P_Job_Title_ID").toString()) );
    	
        return "";
    }
    
    
    /**
     * Cette méthode est lancée lors de la modification du champ P_Employee_ID ou P_Post_ID
     * dans la table P_Assignment. Ces deux valeurs ont été saisie et que le nom de l'affectation
     * est toujours à null, on met comme valeur par défaut NOM DU POSTE - NOM DE L'EMPLOYÉ comme
     * nom d'affectation.
     */
    /*public String calloutDefaultAssignmentName(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
    {
        if(mTab.getValue("P_Employee_ID") != null
                && mTab.getValue("P_Post_ID") != null
                && mTab.getValue("Name") == null)
        {
            String sql
            = "SELECT rtrim(post.Name) + ' - ' + rtrim(employee.Name)"
                + " FROM P_Post post, P_Employee employee"
                + " WHERE post.P_Post_ID = " + mTab.getValue("P_Post_ID")
                + " AND employee.P_Employee_ID = " + mTab.getValue("P_Employee_ID");
            
            try
            {
                PreparedStatement stmt = DB.prepareStatement(sql, null);
                ResultSet rs = stmt.executeQuery();
                
                if(rs.next())
                {
                    mTab.setValue("Name", rs.getString(1));
                }
                
                rs.close();
                stmt.close();
            }
            catch(SQLException e)
            {
                log.log(Level.SEVERE, "calloutDefaultAssignmentName", e);
            }
        }
        return "";
    }
    */

    

    public String outOfRange(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
    {

        if (value != null)
        {
           if ( value.toString().equals("true") )
        	   mTab.setValue("CheckBorn", "Y");
           else
           {
        	   mTab.setValue("CheckBorn", "N");
        	   salary_scale_detail(ctx, WindowNo, mTab, mField, mTab.getValue("P_Salary_Scale_Detail_ID"));
           }
        }

        return "";
    } //  outOfRange

    
} // CalloutPay
