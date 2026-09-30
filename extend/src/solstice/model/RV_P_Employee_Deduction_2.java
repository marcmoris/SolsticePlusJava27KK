package solstice.model;

import java.awt.Frame;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
import org.compiere.model.MSequence;

public class RV_P_Employee_Deduction_2 extends X_RV_P_Employee_Deduction_2 
{
	/**
	 * 	Get absence
	 *	@param ctx context
	 * 	@param P_absence_ID id
	 *	@return absence
	 */
	public static RV_P_Employee_Deduction_2 get (Properties ctx, int P_Employee_Deduction_ID, String trxName)
	{
		Integer key = new Integer (P_Employee_Deduction_ID);
		RV_P_Employee_Deduction_2 EmployeeDeduction = (RV_P_Employee_Deduction_2)s_cache.get(key);
		if (EmployeeDeduction != null)
			return EmployeeDeduction;
		EmployeeDeduction = new RV_P_Employee_Deduction_2 (ctx, P_Employee_Deduction_ID, trxName);
		s_cache.put (key, EmployeeDeduction);
		return EmployeeDeduction;
	}	//	get

	/**	Cache						*/
	private static	CCache<Integer, RV_P_Employee_Deduction_2> s_cache = new CCache<Integer,RV_P_Employee_Deduction_2>("RV_P_Employee_Deduction_2", 10);

	/**
     * @param ctx
     * @param P_EmployeeDeduction_ID
     * @param trxName
     */
    public RV_P_Employee_Deduction_2(Properties ctx, int P_EmployeeDeduction_ID, String trxName)
    {
        super(ctx, P_EmployeeDeduction_ID, trxName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param ctx
     * @param rs
     * @param trxName
     */
    public RV_P_Employee_Deduction_2(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
        // TODO Auto-generated constructor stub
    }

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true or false
	 */


    
	protected boolean beforeSave (boolean newRecord)
	{
		if ( newRecord)
			return true;
	    // harcoded
	    
        /*if ( getCreditsContents() != null && getCreditsContents().equals( P_Credits.CREDITSCONTENTS_Amount ))
        {
            this.setP_UOM_ID( 101 );
        }
        
        if ( getCreditsContents() != null && getCreditsContents().equals( P_Credits.CREDITSCONTENTS_Quantity ))
        {
            this.setP_UOM_ID( 105 );
        }*/

		this.setOverride(true);
 
		Timestamp temp = null;
		String strSelect = "Select EffectIn from RV_P_Employee_Deduction_2 "
			+ " WHERE RV_P_Employee_Deduction_2_ID = "+this.getRV_P_Employee_Deduction_2_ID();
		PreparedStatement pstm = DB.prepareStatement(strSelect, null);
		try
		{
			ResultSet rs = pstm.executeQuery();
			if(rs.next())
			{
				temp = rs.getTimestamp(1);
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		//pour vérifier que la page des parametres soit bien gérer (KM SOLSTICE)
		if(temp.equals(this.getEffectIn()))
		{
			String strMessage = "Vous n'avez pas changer la date d'entrée en vigueur. Voulez-vous utiliser la date de début de période courante?";
    		JOptionPane optionPane1 = new JOptionPane(strMessage,
                    JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
            optionPane1.createDialog(new Frame(), null).setVisible(true);

            Object result = optionPane1.getValue();

            if(Integer.parseInt(result.toString()) == JOptionPane.YES_OPTION)
            {
            	P_Period period = P_Period.getOpenPeriod(Env.getCtx(), null);
            	this.setEffectIn(period.getStartDate());
            	return true;
            }
            else
            {
/*            	this.setOverride(false);
 * 
 */
//            	return false;
            }

		}
		else if(temp == null || this.getEffectIn() == null)
			return false;
		else
			return true;

		return true;
	}	//	beforeSave
	
	
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		//on vérifie si il y avait un enregistrement précédent
		//cad meme employé, meme deduction meme EffectIn < que EffectIn courant
		//Si oui, on met comme EffectTo a cet enregistremenr (sil est a nul)
		//EffectIn courant -1 jour
		boolean retValue = true;
		int iP_EmpDed_ID = -1;
		
		String strSql = "SELECT P_Employee_Deduction_ID FROM P_Employee_Deduction "
			+ " WHERE P_Employee_ID = "+this.getP_Employee_ID()
			+ " AND P_Deduction_ID = "+this.getP_Deduction_ID()
			+ " AND EffectIn < ? "
			+ " AND EffectTo is null "
			+ " ORDER BY EffectIn Desc ";
		
		PreparedStatement pstm = DB.prepareStatement(strSql, null);
		
		try
		{
			pstm.setTimestamp(1, this.getEffectIn());
			ResultSet rs = pstm.executeQuery();
			if(rs.next())
			{
				iP_EmpDed_ID = rs.getInt(1);
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		
		if(iP_EmpDed_ID > 0)
		{
			Timestamp temp = (Timestamp)this.getEffectIn().clone();
	    	temp = TimeUtil.addDays( temp, -1);
			
			P_Employee_Deduction empDed = P_Employee_Deduction.get(Env.getCtx(), iP_EmpDed_ID, null);
			if(empDed != null)
			{
				empDed.setEffectTo(temp);
				if(!empDed.save())
				{
					System.out.println("RV_P_Employee_Deduction_2.afterSave : Erreur lors de l'enregistrement de la déduction.");
				}
			}
			else
			{
				System.out.println("RV_P_Employee_Deduction_2.afterSave : Deduction null");
			}
		}
		
		//
		// M.a.j de la Sequence de P_Employee_Deduction.
		//
		MSequence msequence_cur = MSequence.get(Env.getCtx(), "RV_P_Employee_Deduction_2");
		MSequence msequence = MSequence.get(Env.getCtx(), "P_Employee_Deduction");
		if ( msequence_cur.getCurrentNext() > msequence.getCurrentNext())
		{
			msequence.setCurrentNext( msequence_cur.getCurrentNext() );
			msequence.save();
		}
		//
		
		return retValue;
	}
}
