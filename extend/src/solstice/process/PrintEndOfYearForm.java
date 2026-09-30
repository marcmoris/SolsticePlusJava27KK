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
package solstice.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import solstice.model.P_Year;
import solstice.model.P_Form;
import solstice.utils.PgiUtil;

public class PrintEndOfYearForm extends SvrProcess 
{
	private int InstanceID = 0;

	private int P_Year_ID=0;
	private int P_Employee_ID=0;
	private String TypeCopie = "";
	private String m_Format = "PDF";
	private int P_Form_ID=0;
	
	/**
	 * 
	 */
	public PrintEndOfYearForm() 
	{
		super();
	}
	
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		String paramName = "";
		InstanceID = getAD_PInstance_ID();
		
		//Read the parameters
		for(int i=0; i < para.length; i++){
			paramName = para[i].getParameterName();
			if( paramName.equals("P_Year_ID"))
			{
				this.P_Year_ID = para[i].getParameterAsInt();
			}
			else if (paramName.equals("P_Employee_ID"))
			{
				this.P_Employee_ID = para[i].getParameterAsInt();
			}
			else if (paramName.equals("TypeCopie"))
			{
				this.TypeCopie = (para[i].getParameter()).toString();
			}
			else if (paramName.equals("P_Form_ID"))
			{
				this.P_Form_ID = para[i].getParameterAsInt();
			}
            else if (paramName.equals("CrystalFormat"))
				m_Format = (String)para[i].getParameter();
		}
	}
	
	private void SetPrinted()
	{
		String StringSql="UPDATE P_FORM_EMPLOYEE " + 
		 				"SET ISPRINTED='Y' " +
						"WHERE P_FORM_ID = " + this.P_Form_ID  +
						" AND P_YEAR_ID = " + this.P_Year_ID;
		PreparedStatement pstmt = null;
		try
		{
			if (this.P_Employee_ID!=0)
				StringSql += " AND P_EMPLOYEE_ID =" + this.P_Employee_ID;
			
			pstmt = DB.prepareStatement(StringSql, null);
			pstmt.execute();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"SetPrinted, - " + StringSql, e);
		}
	}
	
	public boolean Validate()
	{
		int CountSelection=0;
		String CountSQL="SELECT COUNT(*) COUNT FROM " + 
						" P_FORM_EMPLOYEE FE " +
						" WHERE " +
						" P_FORM_ID = " + this.P_Form_ID +
						" AND FE.P_YEAR_ID =" + this.P_Year_ID;
		PreparedStatement pstmp = null;
		try
		{
			if (this.P_Employee_ID!=0)
				CountSQL += " AND P_EMPLOYEE_ID =" + this.P_Employee_ID;
			
			pstmp = DB.prepareStatement(CountSQL, null);
			ResultSet rsCount = pstmp.executeQuery(); 
			
			while ( rsCount.next() )
			{
				CountSelection = rsCount.getInt("COUNT");
			}

		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"SetPrinted, - " + CountSQL, e);
		}
		return (CountSelection>0);
	}
		
	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	protected String doIt() throws Exception {    
		try
		{
			if (Validate())
			{
				if (this.TypeCopie.equals("1")) //Copy Employé
					SetPrinted();
				
		       	String srvUrl = PgiUtil.getSolsticeParameter(Env.getCtx(), "WebAppURL");

		       	P_Year Year = P_Year.get( Env.getCtx(), this.P_Year_ID, null);
		       	P_Form Form = P_Form.get( Env.getCtx(), this.P_Form_ID, null);
		       	
		       	String ReportName = "LstFormulaire" + Form.getValue().trim() + "_" + String.valueOf( Year.getYear()).replace( " ", "").trim();
/*		       	
		       	int l_Role = Env.getAD_Role_ID(Env.getCtx());
		        String repUrl = srvUrl+"Crystal/reports.jsp?ReportName=" + ReportName + "&AD_PInstance_ID=" +  this.InstanceID + "&Format=" + m_Format + "&Ad_Role_ID=" + l_Role;
			    
			    Env.startBrowser(repUrl);
*/			    
		    	int l_Role = Env.getAD_Role_ID(Env.getCtx());
		        ReportLauncher.callReport( this.getAD_PInstance_ID(), l_Role, m_Format, getParameter(), ReportName );

			    return "Terminé avec succès";
			}
			else
			{
				return "Aucun enregistrement sélectionné";
			}
		}
		catch (Exception e)
		{
			return "Error: " + e.toString();
		}

	}
}
