package solstice.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.P_Employee;
import solstice.model.P_Form_Employee_Detail;


public class P_TransfertFE extends SvrProcess 
{

	private int m_iP_Year_ID = 0;
	private int m_iP_Employee_ID = 0;
	protected void prepare() 
	{
		// 1 seul paramètre : l'année
		ProcessInfoParameter[] para = getParameter();

		//Read the parameters
		for(int i=0; i < para.length; i++)
		{ 
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("P_Employee_ID"))
				this.m_iP_Employee_ID = para[i].getParameterAsInt();
			else if( name.equals("P_Year_ID"))
				this.m_iP_Year_ID = para[i].getParameterAsInt();

		}
	}

	protected String doIt() throws Exception 
	{
		String retValue = "";
		String msg = "";
		
		//requete principale qui va chercher les enregistrements dans p_employee_carra
		//on cumul pour chaque employee, au cas ou l'employe cotise sur plusieurs regimes 
		String sql = " SELECT SUM(AdjustmentFactorAmount) as Amount, P_Employee_ID "
			+ " FROM P_Employee_Carra "
			+ " WHERE P_Year_ID = "+m_iP_Year_ID;

			if (m_iP_Employee_ID > 0)
				sql += " AND P_Employee_ID=" + m_iP_Employee_ID;

			sql += " GROUP BY P_Employee_ID";
		
		//requete pour aller chercher le bon formulaire (T4)
		String strSelectForm = "SELECT P_Form_ID FROM P_Form "
			+ " WHERE Value = 'T4'";
		
		int numSave = 0;
		BigDecimal bdAmount = Env.ZERO;
		int iP_Employee_ID = 0;
		int iP_Form_ID = 0;
		int iP_Form_Field_ID = 0;
		int iP_Form_Employee_ID = 0;
		int iP_Form_Employee_Detail_ID = 0;

		PreparedStatement pstm = null;
		ResultSet rs = null;
		PreparedStatement pstmIntern = null;
		ResultSet rsIntern = null;
		try
		{
			//on va chercher le formulaire (T4)
			pstm = DB.prepareStatement(strSelectForm, null);
			rs = pstm.executeQuery();
			if(rs.next())
			{
				iP_Form_ID = rs.getInt("P_Form_ID");
			}
			else
			{
				log.log(Level.SEVERE, "doIt - no form_id");
				return "Error - No form ID";
			}
			rs.close();
			pstm.close();
			
			//on va chercher le champ du formulaire (case 52)
			String strSelectField = "SELECT P_Form_Field_ID "
				+ " FROM P_Form_Field "
				+ " WHERE P_Form_ID = "+iP_Form_ID
				+ " AND Value = '52'";

			pstm = DB.prepareStatement(strSelectField, null);
			rs = pstm.executeQuery();
			if(rs.next())
			{
				iP_Form_Field_ID = rs.getInt("P_Form_Field_ID");
			}
			else
			{
				log.log(Level.SEVERE, "doIt - no form_field_id");
				return "Error - No form field ID";
			}
			rs.close();
			pstm.close();
			
			pstm = DB.prepareStatement(sql, null);
			rs = pstm.executeQuery();
			
			//on boulce pour tous les employés
			while(rs.next())
			{
				bdAmount = rs.getBigDecimal("Amount");
				iP_Employee_ID = rs.getInt("P_Employee_ID");
				
				//patch temporaire pour test, enleve les employé inactif
				P_Employee emp = P_Employee.get(Env.getCtx(), iP_Employee_ID, null);
				if(emp == null ) // || emp.isActive() == false
					continue;//on skip cet employé aprce que pas valide
				//on va chercher le formulaire de l'employé
				String strSelectFEmp = "SELECT P_Form_Employee_ID "
					+ " FROM P_Form_Employee "
					+ " WHERE P_Year_ID = "+m_iP_Year_ID
					+ " AND P_Form_ID = "+iP_Form_ID
					+ " AND P_Employee_ID = "+iP_Employee_ID;

				pstmIntern = DB.prepareStatement(strSelectFEmp, null);
				rsIntern = pstmIntern.executeQuery();
				if(rsIntern.next())
				{
					iP_Form_Employee_ID = rsIntern.getInt("P_Form_Employee_ID");
				}
				else
				{
					msg = msg + " <br> Employé " + emp.getValue() + " " + emp.getName() + " n'a pas de formulaire " ;
					log.log(Level.SEVERE, "doIt - no form_employee_id " + emp.getValue() + " " + emp.getName());
//					return "Error - No form employee ID";					
				}
				rsIntern.close();
				pstmIntern.close();
				
				//on va chercher le detail du formulaire de l'employe
				String strSelectFinal = " SELECT P_Form_Employee_Detail_ID "
					+ " FROM P_Form_Employee_Detail "
					+ " WHERE P_Form_Employee_ID ="+iP_Form_Employee_ID
					+ " AND P_Form_Field_ID ="+iP_Form_Field_ID;

				pstmIntern = DB.prepareStatement(strSelectFinal, null);
				rsIntern = pstmIntern.executeQuery();
				if(rsIntern.next())
				{
					iP_Form_Employee_Detail_ID = rsIntern.getInt("P_Form_Employee_Detail_ID");
				}
				else
				{
					log.log(Level.SEVERE, "doIt - no form_employee_detail_id " + emp.getValue() + " " + emp.getName());
//					return "Error - No form employee detail ID";					
				}
				rsIntern.close();
				pstmIntern.close();
				
				//on genere l'objet detail
				P_Form_Employee_Detail formEmpDetail = P_Form_Employee_Detail.get(Env.getCtx(), iP_Form_Employee_Detail_ID, null);
				if(formEmpDetail != null)
				{
					//on instancie le champ et on sauvegarde
					formEmpDetail.setFormFieldAmount(bdAmount);
					
					if(!formEmpDetail.save())
					{
						log.log(Level.SEVERE, "doIt - save error.");
						return "Error - Save error.";																
					}
					else
					{
						numSave++;
					}
				}
				
			}
			rs.close();
			pstm.close();
			
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "doIt - SQL ", e.toString());
		}

		retValue = numSave+" formulaire(s) mis a jour. " + msg;
		return retValue;
	}

}
