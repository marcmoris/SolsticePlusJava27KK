package solstice.model;

import java.awt.Frame;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;
import java.util.logging.*;

import javax.swing.JOptionPane;

import org.compiere.util.*;

/**
 *  Salary_Scale Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Salary_Scale extends X_P_Salary_Scale
{
	/**
	 * 	Get Salary_Scale
	 *	@param ctx context
	 * 	@param P_Salary_Scale_ID id
	 *	@return Salary_Scale
	 */
	public static P_Salary_Scale get (Properties ctx, int P_Salary_Scale_ID, String trxName)
	{
		Integer key = new Integer (P_Salary_Scale_ID);
		P_Salary_Scale Salary_Scale = (P_Salary_Scale)s_cache.get(key);
		if (Salary_Scale != null)
			return Salary_Scale;
		Salary_Scale = new P_Salary_Scale (ctx, P_Salary_Scale_ID, trxName);
		s_cache.put (key, Salary_Scale);
		return Salary_Scale;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Salary_Scale>	s_cache = new CCache<Integer,P_Salary_Scale>("P_Salary_Scale", 20);
	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Salary_Scale.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Salary_Scale_ID id
	 */
	public P_Salary_Scale (Properties ctx, int P_Salary_Scale_ID, String trxName)
	{
		super (ctx, P_Salary_Scale_ID, trxName);
		if (P_Salary_Scale_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Salary_Scale

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Salary_Scale (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Salary_Scale (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Salary_Scale_ID"), trxName);
	}	//	P_Salary_Scale


	public void copy( Properties ctx, Timestamp NewScaleDate, BigDecimal MultiplyRate, Timestamp EffectIn, String trxName )
	{
		int iP_Existing_New_Salary_Scale_ID = 0;
		//on commence par verifier si une echelle existe deja pour les parametres fournis
		String sql = " SELECT P_Salary_Scale_ID FROM P_Salary_Scale "
			+ " WHERE ScaleDate = ?"
			+ " AND P_Occupation_Group_ID="+this.getP_Occupation_Group_ID()
			+ " AND ( P_Job_Title_ID="+this.getP_Job_Title_ID() + " OR P_Job_Title_ID is null )"
		    + " AND P_Salary_Class_ID="+this.getP_Salary_Class_ID();
		
		PreparedStatement pstm = DB.prepareStatement(sql, null);
		try
		{
			pstm.setTimestamp(1, NewScaleDate);
			ResultSet rs = pstm.executeQuery();
			if(rs.next())
			{
				iP_Existing_New_Salary_Scale_ID = rs.getInt("P_Salary_Scale_ID");
			}
		}
		catch(Exception e)
		{
		  	s_log.log(Level.SEVERE,"P_Salary_Scale - copy - " + sql, e);
		}
		
		//si une echelle existait deja pour les parametres fournis, on tente de la detruire
		if(iP_Existing_New_Salary_Scale_ID > 0)
		{
//			P_Salary_Scale existingNewSalaryScale = P_Salary_Scale.get(Env.getCtx(), iP_Existing_New_Salary_Scale_ID, trxName);
//			if(existingNewSalaryScale.delete(false) == false)
//			{
				String message = "Impossible de copier l'échelle courante avec la date saisie puisqu'une nouvelle échelle existe déjà et ne peut être supprimer.";
		   		JOptionPane optionPane1 = new JOptionPane(message,
	                    JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION);
	            optionPane1.createDialog(new Frame(), null).setVisible(true);
	            return;
//			}
		}
		  P_Salary_Scale SalaryScale = new P_Salary_Scale( ctx, -1 , trxName);
		  SalaryScale.setScaleDate( NewScaleDate );
		  SalaryScale.setP_Collective_Labour_Agr_ID( this.getP_Collective_Labour_Agr_ID() );
		  SalaryScale.setP_Occupation_Group_ID( this.getP_Occupation_Group_ID());
		  SalaryScale.setP_Credits_ID( this.getP_Credits_ID());
		  SalaryScale.setDescription( this.getDescription() );
		  SalaryScale.setEffectIn( EffectIn );
		  SalaryScale.setP_Job_Title_ID( this.getP_Job_Title_ID());
		  SalaryScale.setP_Salary_Class_ID( this.getP_Salary_Class_ID());
		  SalaryScale.setRemuneration_Method( this.getRemuneration_Method());
		  SalaryScale.setIsActive( this.isActive());
		  SalaryScale.setScale_Type( this.getScale_Type());
		  SalaryScale.setAD_Org_ID( this.getAD_Org_ID() );
		  SalaryScale.setP_Post_ID( this.getP_Post_ID() );
		  SalaryScale.setName( this.getName() );
		  SalaryScale.setValue( this.getValue() );
		  
		  SalaryScale.save();

		  sql = "SELECT P_Salary_Scale_Detail_ID  FROM P_Salary_Scale_Detail WHERE P_Salary_Scale_ID = ? ORDER BY STEP ";

		  PreparedStatement pstmt = null;
		  int Salary_Scale_Detail_ID = 0;
		  try
		  {
		  	pstmt = DB.prepareStatement (sql, null);
            pstmt.setInt(1, this.getP_Salary_Scale_ID());
		  	ResultSet rs = pstmt.executeQuery ();
		  	while ( rs.next () )
		  	{
		  		BigDecimal rate = new BigDecimal(1.0000).add( MultiplyRate.divide(new BigDecimal( 100.0000 ), 4,BigDecimal.ROUND_HALF_UP)); 
		  		Salary_Scale_Detail_ID = rs.getInt(1);
		  		P_Salary_Scale_Detail SalCopy = P_Salary_Scale_Detail.get( ctx, Salary_Scale_Detail_ID, trxName);
		  		P_Salary_Scale_Detail Salary_Scale_Detail = new P_Salary_Scale_Detail( ctx, -1, trxName);
		  		if ( SalCopy.getAnnual_Salary().compareTo(Env.ZERO) != 0)
		  			Salary_Scale_Detail.setAnnual_Salary( SalCopy.getAnnual_Salary().multiply( rate ).setScale(0,BigDecimal.ROUND_HALF_UP));
		  		if ( SalCopy.getHourly_Rate().compareTo(Env.ZERO) != 0)
		  			Salary_Scale_Detail.setHourly_Rate( SalCopy.getHourly_Rate().multiply( rate ).setScale(2,BigDecimal.ROUND_HALF_UP));
				if ( SalCopy.getLimit_Max().compareTo(Env.ZERO) != 0 )
					Salary_Scale_Detail.setLimit_Max( SalCopy.getLimit_Max().multiply( rate ).setScale(2,BigDecimal.ROUND_HALF_UP));
				if ( SalCopy.getLimit_Min().compareTo(Env.ZERO) != 0 )
					Salary_Scale_Detail.setLimit_Min( SalCopy.getLimit_Min().multiply( rate ).setScale(2,BigDecimal.ROUND_HALF_UP));
	
				if (SalCopy.getSalary_Limit_Max().compareTo(Env.ZERO) != 0)
					Salary_Scale_Detail.setSalary_Limit_Max( SalCopy.getSalary_Limit_Max().multiply( rate ).setScale(0,BigDecimal.ROUND_UP));
				if (SalCopy.getSalary_Limit_Min().compareTo(Env.ZERO) != 0)
					Salary_Scale_Detail.setSalary_Limit_Min( SalCopy.getSalary_Limit_Min().multiply( rate ).setScale(0,BigDecimal.ROUND_DOWN));
				Salary_Scale_Detail.setP_Salary_Scale_ID( SalaryScale.getP_Salary_Scale_ID() );
				Salary_Scale_Detail.setStep( SalCopy.getStep());
                Salary_Scale_Detail.setIsActive( SalCopy.isActive());
                Salary_Scale_Detail.setAD_Org_ID( SalCopy.getAD_Org_ID() );
		  		Salary_Scale_Detail.save();
		  	}
	  		rs.close ();
	  		pstmt.close ();
	  		pstmt = null;
		    
		  }
		  catch (Exception e)
		  {
		  	s_log.log(Level.SEVERE,"P_Salary_Scale - copy - " + sql, e);
		  }

		  sql = "SELECT P_Salary_Scale_Job_Title_ID  FROM P_Salary_Scale_Job_Title WHERE P_Salary_Scale_ID = ?";

		  pstmt = null;
		  int Salary_Scale_Job_Title_ID = 0;
		  try
		  {
		  	pstmt = DB.prepareStatement (sql, null);
            pstmt.setInt(1, this.getP_Salary_Scale_ID());
		  	ResultSet rs = pstmt.executeQuery ();
		  	while ( rs.next () )
		  	{
		  		Salary_Scale_Job_Title_ID = rs.getInt(1);
		  		P_Salary_Scale_Job_Title SalCopy = P_Salary_Scale_Job_Title.get( ctx, Salary_Scale_Job_Title_ID, trxName);
		  		P_Salary_Scale_Job_Title Salary_Scale_Job_Title = new P_Salary_Scale_Job_Title( ctx, -1, trxName);
				Salary_Scale_Job_Title.setP_Salary_Scale_ID( SalaryScale.getP_Salary_Scale_ID() );
				Salary_Scale_Job_Title.setP_Job_Title_ID( SalCopy.getP_Job_Title_ID() );
                Salary_Scale_Job_Title.setIsActive( SalCopy.isActive());
		  		Salary_Scale_Job_Title.save();
		  	}
	  		rs.close ();
	  		pstmt.close ();
	  		pstmt = null;
		    
		  }
		  catch (Exception e)
		  {
		  	s_log.log(Level.SEVERE,"P_Salary_Scale - copy - " + sql, e);
		  }

	}
	
	/**
	 * Procédure de copie. Lorsqu'on copie un titre d'emploi, on doit
	 * l'intégrer à la comptabilité en alimentant les tables Salary scale,
	 * P_Deduction_Account et P_Credits_Account
	 */
	public void afterCopy(int originalId)
	{
		  String sql = "SELECT P_Salary_Scale_Detail_ID  FROM P_Salary_Scale_Detail WHERE P_Salary_Scale_ID = " + originalId;

		  PreparedStatement pstmt = null;
		  int Salary_Scale_Detail_ID = 0;
		  try
		  {
		  	pstmt = DB.prepareStatement (sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE, this.get_TrxName());
		  	ResultSet rs = pstmt.executeQuery ();
		  	while ( rs.next () )
		  	{
		  		Salary_Scale_Detail_ID = rs.getInt("P_Salary_Scale_Detail_ID");
		  		P_Salary_Scale_Detail SalCopy = P_Salary_Scale_Detail.get( Env.getCtx(), Salary_Scale_Detail_ID, this.get_TrxName());
		  		P_Salary_Scale_Detail Salary_Scale_Detail = new P_Salary_Scale_Detail( Env.getCtx(), -1, this.get_TrxName());
		  		
		  		if ( SalCopy.getAnnual_Salary().compareTo(Env.ZERO) != 0)
		  			Salary_Scale_Detail.setAnnual_Salary( SalCopy.getAnnual_Salary());
		  		if ( SalCopy.getHourly_Rate().compareTo(Env.ZERO) != 0)
		  			Salary_Scale_Detail.setHourly_Rate( SalCopy.getHourly_Rate());

                
                if ( SalCopy.getLimit_Max().compareTo(Env.ZERO) != 0)
                	Salary_Scale_Detail.setLimit_Max( SalCopy.getLimit_Max());
                
                if (  SalCopy.getLimit_Min().compareTo(Env.ZERO) != 0 )
                	Salary_Scale_Detail.setLimit_Min( SalCopy.getLimit_Min());

                if ( SalCopy.getSalary_Limit_Max().compareTo(Env.ZERO) != 0 )
                	Salary_Scale_Detail.setSalary_Limit_Max( SalCopy.getSalary_Limit_Max());
                
                if ( SalCopy.getSalary_Limit_Min().compareTo(Env.ZERO) != 0 )
                	Salary_Scale_Detail.setSalary_Limit_Min( SalCopy.getSalary_Limit_Min());

		  		Salary_Scale_Detail.setIsActive( SalCopy.isActive());
				Salary_Scale_Detail.setP_Salary_Scale_ID( this.getP_Salary_Scale_ID() );
				Salary_Scale_Detail.setStep( SalCopy.getStep());
		  		Salary_Scale_Detail.save(this.get_TrxName());
		  	}
	  		rs.close ();
	  		pstmt.close ();
	  		pstmt = null;
		    
		  }
	    catch (SQLException e)
	    {
	        log.log(Level.SEVERE, "afterCopy", e);
	    }
	}
	
	

}	//	P_Salary_Scale
