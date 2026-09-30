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

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

/**
 * @author alenav01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_ChequesPrinting extends SvrProcess{

	private String _ClassErrorMessage ="Error P_Cheques Printing";
	private int InstanceID = 0;
	private int ClientID=0;
	private int OrgID=0;
	private int UserID=0;
	
	private int P_Frequency_ID=0;
	private int P_Period_ID=0;
	private int P_Payment_Group_ID=0;
	private int P_Occupation_Group_ID=0;
	private int P_Distribution_ID=0;
	private int P_Employee_ID=0;
	private String DocumentType = "";
	private int ToBeginAfterThisNumeration=0;
	private boolean Reprint=false;

	private String m_Format = "PDF";

	/**
	 * 
	 */
	public P_ChequesPrinting() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		String paramName = "";
		InstanceID = getAD_PInstance_ID();
		
		//Read the parameters
		for(int i=0; i < para.length; i++){
			paramName = para[i].getParameterName();
			if( paramName.equals("P_Frequency_ID")){
				this.P_Frequency_ID = ((Number)para[i].getParameter()).intValue();
			}else if (paramName.equals("P_Period_ID")){
				this.P_Period_ID = ((Number)para[i].getParameter()).intValue();
			}else if (paramName.equals("P_Payment_Group_ID")){
				this.P_Payment_Group_ID = ((Number)para[i].getParameter()).intValue();
			}else if (paramName.equals("P_Occupation_Group_ID")){
				this.P_Occupation_Group_ID = ((Number)para[i].getParameter()).intValue();
			}else if (paramName.equals("P_Distribution_Booklet_ID")){
				this.P_Distribution_ID = ((Number)para[i].getParameter()).intValue();
			}else if (paramName.equals("P_Employee_ID")){
				this.P_Employee_ID = ((Number)para[i].getParameter()).intValue();
			}else if (paramName.equals("PaymentTypeDoc")){
				this.DocumentType = (para[i].getParameter().toString());
			}else if (paramName.equals("ToBeginAfterThisNumeration")){
				this.ToBeginAfterThisNumeration = ((Number)para[i].getParameter()).intValue();
			}else if (paramName.equals("Reprint")){
				this.Reprint = StringToBoolean((char)para[i].getParameter().toString().charAt(0));
			}
		}
	}
	
	private boolean StringToBoolean(char StringToConvert)
	{
		boolean boolReturn=false;
		
		if (StringToConvert=='Y')
			boolReturn=true;
		
		return boolReturn;
	}
	
	
	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	protected String doIt() throws Exception {    
		try
		{
			P_InOutChequesPrinting ChequesPrinting=new P_InOutChequesPrinting (getCtx(),InstanceID);
			
			if (ChequesPrinting.Validate(this.P_Frequency_ID,this.P_Period_ID,this.P_Payment_Group_ID,this.P_Distribution_ID,this.P_Occupation_Group_ID,this.P_Employee_ID, this.DocumentType, this.ToBeginAfterThisNumeration,this.Reprint))
			{
				
				ChequesPrinting.StartProcess(this.P_Frequency_ID,this.P_Period_ID,this.P_Payment_Group_ID,this.P_Distribution_ID,this.P_Occupation_Group_ID,this.P_Employee_ID,  this.DocumentType, this.ToBeginAfterThisNumeration,this.Reprint);	
/*				
				String srvUrl = PgiUtil.getSolsticeParameter(Env.getCtx(), "WebAppURL");
				String repUrl = srvUrl+"Crystal/reports.jsp?AD_PInstance_ID=" + this.InstanceID;
			    Env.startBrowser(repUrl);
*/			    
				int l_Role = Env.getAD_Role_ID(Env.getCtx());
		        ReportLauncher.callReport( this.InstanceID, l_Role, m_Format, getParameter() );

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
