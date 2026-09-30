package solstice.process;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import solstice.model.P_Carra;

public class CarraTransfert extends SvrProcess 
{
	int AD_Pinstance_ID = 0;
	int P_Employee_ID = 0;
	int P_Deduction_ID = 0;
	int P_Year_ID = 0;

	protected void prepare() 
	{
		ProcessInfoParameter[] para = getParameter();
		String paramName = "";
		AD_Pinstance_ID = this.getAD_PInstance_ID();
		//Read the parameters
		for(int i=0; i < para.length; i++)
		{ 
			paramName = para[i].getParameterName();
			if (paramName.equals("P_Employee_ID")){
				this.P_Employee_ID = para[i].getParameterAsInt();
			}else if (paramName.equals("P_Deduction_ID")){
				this.P_Deduction_ID = para[i].getParameterAsInt();
			}else if (paramName.equals("P_Year_ID")){
				this.P_Year_ID = para[i].getParameterAsInt();
			}
		}
	}


	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	protected String doIt() throws Exception 
	{
		P_Carra.exportXml(getCtx(), P_Year_ID, P_Employee_ID, P_Deduction_ID, this.get_TrxName());
        return "@Processed@ ";
	}
	

}
