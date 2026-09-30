package solstice.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.model.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 *  Audit_Trail_Insurance Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Audit_Trail_Insurance extends X_P_Audit_Trail_Insurance
{
	/**
	 * 	Get Audit_Trail_Insurance
	 *	@param ctx context
	 * 	@param P_Audit_Trail_Insurance_ID id
	 *	@return Audit_Trail_Insurance
	 */
	public static P_Audit_Trail_Insurance get (Properties ctx, int P_Audit_Trail_Insurance_ID, String trxName)
	{
		Integer key = new Integer (P_Audit_Trail_Insurance_ID);
		P_Audit_Trail_Insurance Audit_Trail_Insurance = (P_Audit_Trail_Insurance)s_cache.get(key);
		if (Audit_Trail_Insurance != null)
			return Audit_Trail_Insurance;
		Audit_Trail_Insurance = new P_Audit_Trail_Insurance (ctx, P_Audit_Trail_Insurance_ID, trxName);
		s_cache.put (key, Audit_Trail_Insurance);
		return Audit_Trail_Insurance;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Audit_Trail_Insurance>	s_cache = new CCache<Integer,P_Audit_Trail_Insurance>("P_Audit_Trail_Insurance", 20);

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Audit_Trail_Insurance.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Audit_Trail_Insurance_ID id
	 */
	public P_Audit_Trail_Insurance (Properties ctx, int P_Audit_Trail_Insurance_ID, String trxName)
	{
		super (ctx, P_Audit_Trail_Insurance_ID, trxName);
		if (P_Audit_Trail_Insurance_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Audit_Trail_Insurance

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Audit_Trail_Insurance (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Audit_Trail_Insurance (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Audit_Trail_Insurance_ID"), trxName);
	}	//	P_Audit_Trail_Insurance


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Audit_Trail_Insurance[ID=")
			.append(this.getP_Audit_Trail_Insurance_ID())
//			.append(",Value=").append(getValue())
			.append(",Name=").append(getDescription())
			.append ("]");
		return sb.toString ();
	}	//	toString

	
	public static void insert_audit( int P_Employee_ID, String Description, String OldValue, String NewValue, Timestamp EffectIn, Timestamp EffectTo  )
	{
		P_Audit_Trail_Insurance rec = new P_Audit_Trail_Insurance( Env.getCtx(), -1, null);
		rec.setOldValue(OldValue);
		rec.setNewValue(NewValue);
		rec.setEffectIn(EffectIn);
		rec.setEffectTo(EffectTo);
		rec.setDescription(Description);
		rec.setP_Employee_ID( P_Employee_ID );
		rec.save();
		
	}

	public static void insert_audit( int P_Employee_ID, String Description, BigDecimal OldValue, BigDecimal NewValue, Timestamp EffectIn, Timestamp EffectTo  )
	{
		P_Audit_Trail_Insurance rec = new P_Audit_Trail_Insurance( Env.getCtx(), -1, null);
		if ( OldValue != null )
			rec.setOldValue(OldValue.toString());
		if ( NewValue != null )
			rec.setNewValue(NewValue.toString());
		
		rec.setEffectIn(EffectIn);
		rec.setEffectTo(EffectTo);
		rec.setDescription(Description);
		rec.setP_Employee_ID( P_Employee_ID );
		rec.save();
		
	}

	
	public static void insert_audit( int P_Employee_ID, String Description, String OldValue, String NewValue )
	{
		insert_audit( P_Employee_ID, Description, OldValue, NewValue, null, null);
	}

	public static void insert_audit( int P_Employee_ID, String Description, int NewValue )
	{
		insert_audit( P_Employee_ID, Description, null, String.valueOf(NewValue));
	}

	public static void insert_audit( int P_Employee_ID, String Description, int OldValue, int NewValue )
	{
		insert_audit( P_Employee_ID, Description, String.valueOf(OldValue), String.valueOf(NewValue));
	}

	/************
	 *  Gestion des différents audits 
	 *  call in the beforeSave method
	 */
	public static void Employee_Audit( P_Employee Employee, boolean newRecord)
	{
		
		String oldValue = null;
		String newValue = null;

		if ( newRecord )
		{

			insert_audit(Employee.getP_Employee_ID(), "Value", null, Employee.getValue() );
			//- Nom et Prénom
			insert_audit(Employee.getP_Employee_ID(), "Name", null, Employee.getName() );
			insert_audit(Employee.getP_Employee_ID(), "DateHired", null, Employee.getDateHired().toString() );

			{
				oldValue = null;
				MOrg o = new MOrg(Env.getCtx(), Employee.getAD_Org_ID(), null);
				newValue = o.getValue();
				insert_audit(Employee.getP_Employee_ID(), "AD_Org_ID", oldValue, newValue );
				
			}

			{
				oldValue = null;
				P_LongTermLeave o;
				if ( Employee.getP_LongTermLeave_ID() == 0 )
				{
					newValue = "01 Active";
				}
				else
				{
					o = P_LongTermLeave.get(Env.getCtx(), Employee.getP_LongTermLeave_ID(), null);
					newValue = o.getValue() + ' ' + o.getName();
					
				}
				insert_audit(Employee.getP_Employee_ID(), "P_LongTermLeave_ID", oldValue, newValue );
			}
			{
				oldValue = null;
				P_Department o = P_Department.get(Env.getCtx(), Employee.getP_Department_ID(), null);
				newValue = o.getValue();
				insert_audit(Employee.getP_Employee_ID(), "P_Department_ID", oldValue, newValue );
			}
			//- Base
			{
				oldValue = null;
				P_Workplace o = P_Workplace.get(Env.getCtx(), Employee.getP_Workplace_ID(), null);
				newValue = o.getValue();
				insert_audit(Employee.getP_Employee_ID(), "P_Workplace_ID", oldValue, newValue  );
				
			}
			//- Crew cost
			{
				oldValue = null;
				MActivity o = new MActivity(Env.getCtx(), Employee.getC_Activity_ID(), null);
				newValue = o.getValue();
				insert_audit(Employee.getP_Employee_ID(), "C_Activity_ID", oldValue, newValue );
				
			}

			return;
		}

		String sql = "Select * From P_Employee Where P_Employee_ID = " + Employee.getP_Employee_ID();
		
		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next() )
			{
				//-Date embauche
				if ( Employee.is_ValueChanged("DateHired")  )
				{
					if ( Employee.getDateHired() == null)
						insert_audit(Employee.getP_Employee_ID(), "DateHired", rs.getString("DateHired"), " " );
					else
						insert_audit(Employee.getP_Employee_ID(), "DateHired", rs.getString("DateHired"), Employee.getDateHired().toString() );
				}
				//- Date de départ
				if ( Employee.is_ValueChanged("DateLayoff") )
				{
					if ( Employee.getDateLayoff() == null)
						insert_audit(Employee.getP_Employee_ID(), "DateLayoff", rs.getString("DateLayoff"), " " );
					else
						insert_audit(Employee.getP_Employee_ID(), "DateLayoff", rs.getString("DateLayoff"), Employee.getDateLayoff().toString() );
				}
				
				if ( Employee.is_ValueChanged("LayoffComment") )
				{
					if ( Employee.getLayoffComment() == null)
						insert_audit(Employee.getP_Employee_ID(), "LayoffComment", rs.getString("LayoffComment"), " " );
					else
						insert_audit(Employee.getP_Employee_ID(), "LayoffComment", rs.getString("LayoffComment"), Employee.getLayoffComment() );
				}

				if ( Employee.is_ValueChanged("LayoffCode")  )
				{
					int AD_Reference_ID = 2100332;
					
					oldValue = null;
					
					if ( rs.getString("LayoffCode") != null)
					{
						MRefList o = MRefList.get(Env.getCtx(), AD_Reference_ID, rs.getString("LayoffCode"), null);
						oldValue = o.getValue() + " " + o.getName();
					}
					
					newValue = null;
					if ( Employee.getLayoffCode() != null && Employee.getLayoffCode().trim().length() != 0 )
					{
						MRefList o = MRefList.get(Env.getCtx(), AD_Reference_ID, Employee.getLayoffCode(), null);
						newValue = o.getValue() + " " + o.getName();
					}
					
					if ( Employee.getLayoffCode() == null)
						insert_audit(Employee.getP_Employee_ID(), "LayoffCode", oldValue, " " );
					else
						insert_audit(Employee.getP_Employee_ID(), "LayoffCode", oldValue, newValue );
				}
				
				//- Statut d’emploi
				
				if ( Employee.is_ValueChanged("P_Job_Title_ID") || rs.getInt("P_Job_Title_ID") != Employee.getP_Job_Title_ID() )
				{
					P_Job_Title o = P_Job_Title.get(Env.getCtx(), rs.getInt("P_Job_Title_ID"), null);
					oldValue = o.getValue();
					o = P_Job_Title.get(Env.getCtx(), Employee.getP_Job_Title_ID(), null);
					newValue = o.getValue();
					insert_audit(Employee.getP_Employee_ID(), "P_Job_Title_ID", oldValue, newValue );
				}
					
				//- Statut d’activité
				if ( Employee.is_ValueChanged("P_LongTermLeave_ID")  || rs.getInt("P_LongTermLeave_ID") != Employee.getP_LongTermLeave_ID() )
				{
					P_LongTermLeave o;
					if ( rs.getInt("P_LongTermLeave_ID") == 0 )
					{
						oldValue = "01 Active";
					}
					else
					{
						o = P_LongTermLeave.get( Env.getCtx(), rs.getInt("P_LongTermLeave_ID"), null);
						oldValue = o.getValue() + ' ' + o.getName();
					}
					
					if ( Employee.getP_LongTermLeave_ID() == 0 )
					{
						newValue = "01 Active";
					}
					else
					{
						o = P_LongTermLeave.get(Env.getCtx(), Employee.getP_LongTermLeave_ID(), null);
						newValue = o.getValue() + ' ' + o.getName();
						
					}
					insert_audit(Employee.getP_Employee_ID(), "P_LongTermLeave_ID", oldValue, newValue );
					
				}
				//- Salaire assurable*
				if ( Employee.is_ValueChanged("InsurableSalary")  )
					insert_audit(Employee.getP_Employee_ID(), "InsurableSalary", rs.getString("InsurableSalary"), Employee.getInsurableSalary().toString() );

				//- Salaire assurable fix *
				if ( Employee.is_ValueChanged("InsurableSalaryFix") )
					insert_audit(Employee.getP_Employee_ID(), "InsurableSalary", rs.getString("InsurableSalaryFix"), Employee.getInsurableSalaryFix().toString() );

				//- Salaire assurable fix *
				if ( Employee.is_ValueChanged("IsInsurableSalaryFix") )
					insert_audit(Employee.getP_Employee_ID(), "IsInsurableSalary", rs.getString("IsInsurableSalaryFix"), String.valueOf( Employee.isInsurableSalaryFix() ) );

				//- Département
				if ( Employee.is_ValueChanged("P_Department_ID") || rs.getInt("P_Department_ID") != Employee.getP_Department_ID())
				{
					P_Department o = P_Department.get( Env.getCtx(), rs.getInt("P_Department_ID"), null);
					oldValue = o.getValue();
					o = P_Department.get(Env.getCtx(), Employee.getP_Department_ID(), null);
					newValue = o.getValue();
					insert_audit(Employee.getP_Employee_ID(), "P_Department_ID", oldValue, newValue );
				}
				//- Base
				if ( Employee.is_ValueChanged("P_Workplace_ID") || rs.getInt("P_Workplace_ID") != Employee.getP_Workplace_ID() )
				{
					P_Workplace o = P_Workplace.get( Env.getCtx(), rs.getInt("P_Workplace_ID"), null);
					oldValue = o.getValue();
					o = P_Workplace.get(Env.getCtx(), Employee.getP_Workplace_ID(), null);
					newValue = o.getValue();
					insert_audit(Employee.getP_Employee_ID(), "P_Workplace_ID", oldValue, newValue  );
					
				}
				//- Crew cost
				if ( Employee.is_ValueChanged("C_Activity_ID") || rs.getInt("C_Activity_ID") != Employee.getC_Activity_ID() )
				{
					MActivity o = new MActivity( Env.getCtx(), rs.getInt("C_Activity_ID"), null);
					oldValue = o.getValue();
					o = new MActivity(Env.getCtx(), Employee.getC_Activity_ID(), null);
					newValue = o.getValue();
					
					insert_audit(Employee.getP_Employee_ID(), "C_Activity_ID", oldValue, newValue );
					
				}
				//- Division
				if ( Employee.is_ValueChanged("AD_Org_ID") || rs.getInt("AD_Org_ID") != Employee.getAD_Org_ID() )
				{
					MOrg o = new MOrg( Env.getCtx(),rs.getInt("AD_Org_ID"), null );
					oldValue = o.getValue();
					o = new MOrg(Env.getCtx(), Employee.getAD_Org_ID(), null);
					newValue = o.getValue();
					
					insert_audit(Employee.getP_Employee_ID(), "AD_Org_ID", oldValue, newValue );
				}
				//- Numéro d’employé
				if ( Employee.is_ValueChanged("Value") )
					insert_audit(Employee.getP_Employee_ID(), "Value", rs.getString("Value"), Employee.getValue() );
				//- Nom et Prénom
				if ( Employee.is_ValueChanged("Name") || Employee.is_ValueChanged("FirstName") || Employee.is_ValueChanged("Surname") )
					insert_audit(Employee.getP_Employee_ID(), "Name", rs.getString("Name"), Employee.getName() );
				
			}
			else
			{
				//-Date embauche
				if ( Employee.is_ValueChanged("DateHired") )
					insert_audit(Employee.getP_Employee_ID(), "DateHired", null, Employee.getDateHired().toString() );
				//- Date de départ
				if ( Employee.is_ValueChanged("DateLayoff") )
					insert_audit(Employee.getP_Employee_ID(), "DateLayoff", null, Employee.getDateLayoff().toString() );
				//- Statut d’emploi
				if ( Employee.is_ValueChanged("P_Job_Title_ID") )
				{
					oldValue = null;
					P_Job_Title o = P_Job_Title.get(Env.getCtx(), Employee.getP_Job_Title_ID(), null);
					newValue = o.getValue();
					insert_audit(Employee.getP_Employee_ID(), "P_Job_Title_ID", oldValue, newValue );
				}
					
				//- Statut d’activité
				if ( Employee.is_ValueChanged("P_LongTermLeave_ID") )
				{
					oldValue = null;
					P_LongTermLeave o;
					if ( Employee.getP_LongTermLeave_ID() == 0 )
					{
						newValue = "01 Active";
					}
					else
					{
						o = P_LongTermLeave.get(Env.getCtx(), Employee.getP_LongTermLeave_ID(), null);
						newValue = o.getValue() + ' ' + o.getName();
						
					}
					insert_audit(Employee.getP_Employee_ID(), "P_LongTermLeave_ID", oldValue, newValue );
					
				}
				//- Salaire assurable*
				if ( Employee.is_ValueChanged("InsurableSalary") )
					insert_audit(Employee.getP_Employee_ID(), "InsurableSalary", null, Employee.getInsurableSalary().toString() );

				//- Salaire assurable fix *
				if ( Employee.is_ValueChanged("InsurableSalaryFix") )
					insert_audit(Employee.getP_Employee_ID(), "InsurableSalary", null, Employee.getInsurableSalaryFix().toString() );

				//- Salaire assurable fix *
				if ( Employee.is_ValueChanged("IsInsurableSalaryFix") )
					insert_audit(Employee.getP_Employee_ID(), "IsInsurableSalary", null, String.valueOf( Employee.isInsurableSalaryFix() ) );

				//- Département
				if ( Employee.is_ValueChanged("P_Department_ID") )
				{
					oldValue = null;
					P_Department o = P_Department.get(Env.getCtx(), Employee.getP_Department_ID(), null);
					newValue = o.getValue();
					insert_audit(Employee.getP_Employee_ID(), "P_Department_ID", oldValue, newValue );
				}
				//- Base
				if ( Employee.is_ValueChanged("P_Workplace_ID") )
				{
					oldValue = null;
					P_Workplace o = P_Workplace.get(Env.getCtx(), Employee.getP_Workplace_ID(), null);
					newValue = o.getValue();
					insert_audit(Employee.getP_Employee_ID(), "P_Workplace_ID", oldValue, newValue  );
					
				}
				//- Crew cost
				if ( Employee.is_ValueChanged("C_Activity_ID") )
				{
					oldValue = null;
					MActivity o = new MActivity(Env.getCtx(), Employee.getC_Activity_ID(), null);
					newValue = o.getValue();
					
					insert_audit(Employee.getP_Employee_ID(), "C_Activity_ID", oldValue, newValue );
					
				}
				//- Division
				if ( Employee.is_ValueChanged("AD_Org_ID") )
				{
					oldValue = null;
					MOrg o = new MOrg(Env.getCtx(), Employee.getAD_Org_ID(), null);
					newValue = o.getValue();
					
					insert_audit(Employee.getP_Employee_ID(), "AD_Org_ID", oldValue, newValue );
				}
				//- Numéro d’employé
				if ( Employee.is_ValueChanged("Value") )
					insert_audit(Employee.getP_Employee_ID(), "Value", null, Employee.getValue() );
				//- Nom et Prénom
				if ( Employee.is_ValueChanged("Name") || Employee.is_ValueChanged("FirstName") || Employee.is_ValueChanged("Surname") )
					insert_audit(Employee.getP_Employee_ID(), "Name", null, Employee.getName() );
			}
				
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}

	}

	private static BigDecimal InsurableSalary = Env.ZERO;

	public static void setInsurableSalary ( BigDecimal amount)
	{
		InsurableSalary = amount;
	}

	public static void setInsurableSalary ( int P_Employee_ID )
	{
		P_Employee Employee = P_Employee.get( Env.getCtx(), P_Employee_ID, null);
		InsurableSalary = Employee.getInsurableSalary();
	}


	public static BigDecimal getInsurableSalary ()
	{
		return InsurableSalary;
	}

	//
	// Salaire assurable.
	//
	public static void InsurableSalary_Audit( int P_Employee_ID )
	{
		try
		{
			P_Employee Employee = P_Employee.get( Env.getCtx(), P_Employee_ID, null);
			if ( getInsurableSalary() == null || Employee.getInsurableSalary() == null || getInsurableSalary().compareTo( Employee.getInsurableSalary() ) != 0 )
				insert_audit(Employee.getP_Employee_ID(), "InsurableSalary", getInsurableSalary().toString(), Employee.getInsurableSalary().toString() );
			
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "", e);
		}
	}

	public static void InsurableSalary_Audit( int P_Employee_ID, Timestamp EffectIn, Timestamp EffectTo )
	{
		try
		{
			P_Employee Employee = P_Employee.get( Env.getCtx(), P_Employee_ID, null);
			if ( getInsurableSalary() == null || Employee.getInsurableSalary() == null || getInsurableSalary().compareTo( Employee.getInsurableSalary() ) != 0 )
			{
				insert_audit(Employee.getP_Employee_ID(), "InsurableSalary", getInsurableSalary(), Employee.getInsurableSalary(), EffectIn, EffectTo );
				
			}
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "", e);
		}
	}

	public static void LocationAudit( MLocation Location, String trxName )
	{
		String sql = "Select P_Employee_ID, C_Location.C_Region_ID from P_Employee Left Outer Join C_Location on C_Location.C_Location_ID = P_Employee.C_Location_ID where C_Location.C_Location_ID = " + Location.getC_Location_ID();

		System.out.println("* DEBUG * Employee qui change de province. " );
		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			P_Employee Employee;
			MRegion Region;
			while (rs.next() )
			{
				Region = MRegion.get( Env.getCtx(), rs.getInt("C_Region_ID"));
				String oldValue = Region.getName();
				Employee = P_Employee.get( Env.getCtx(), rs.getInt( "P_Employee_ID"), trxName);
				Region = MRegion.get( Env.getCtx(), Location.getC_Region_ID());
				String newValue =  Region.getName();
				insert_audit(Employee.getP_Employee_ID(), "Province", oldValue, newValue );
				
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
	}
	
}	//	P_Audit_Trail_Insurance
