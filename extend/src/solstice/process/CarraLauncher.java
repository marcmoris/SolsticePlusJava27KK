package solstice.process;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;


public class CarraLauncher extends SvrProcess 
{
	int AD_Pinstance_ID = 0;
	int period_ID = 0;
	int employeeId = 0;
	int deductionId = 0;
	int yearId = 0;
	boolean flagMAJ = false;
	int nbrPeriod = 0;
	String recordType = "P";

	protected void prepare() 
	{
		ProcessInfoParameter[] para = getParameter();
		String paramName = "";
		AD_Pinstance_ID = this.getAD_PInstance_ID();
		String tempFlagMAJ = "";
		//Read the parameters
		for(int i=0; i < para.length; i++)
		{ 
			paramName = para[i].getParameterName();
			if( paramName.equals("P_Period_ID")){
				this.period_ID = ((Number)para[i].getParameter()).intValue();
			}else if (paramName.equals("P_Employee_ID")){
				this.employeeId = ((Number)para[i].getParameter()).intValue();
			}else if (paramName.equals("P_Deduction_ID")){
				this.deductionId = ((Number)para[i].getParameter()).intValue();
			}else if (paramName.equals("P_Year_ID")){
				this.yearId = ((Number)para[i].getParameter()).intValue();
			}else if (paramName.equals("Update")){
				tempFlagMAJ = ((String)para[i].getParameter());
			}else if (paramName.equals("NumberOfPeriod")){
				this.nbrPeriod = ((Number)para[i].getParameter()).intValue();
			}else if (paramName.equals("RecordType")){
				this.recordType = ((String)para[i].getParameter());
			}
		}
		if(tempFlagMAJ.equals("N"))
		{
			this.flagMAJ = false;
		}
		else
		{
			this.flagMAJ = true;
		}
	}


	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	protected String doIt() throws Exception 
	{
		P_Carra carra = new P_Carra(this.period_ID,
									this.employeeId,
									this.deductionId,
									this.yearId,
									this.flagMAJ,
									this.nbrPeriod,
									this.recordType);
		String retValue = carra.doIt();
        return retValue;
	}
	

}
