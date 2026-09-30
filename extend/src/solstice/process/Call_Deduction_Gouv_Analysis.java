package solstice.process;

import java.sql.PreparedStatement;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

public class Call_Deduction_Gouv_Analysis extends SvrProcess 
{
	int AD_Pinstance_ID = 0;
	int PeriodFrom = 0;
	int PeriodTo  = 0;
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
			if (paramName.equals("PeriodFrom")){
				this.PeriodFrom = para[i].getParameterAsInt();
			}else if (paramName.equals("PeriodTo")){
				this.PeriodTo = para[i].getParameterAsInt();
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
		PreparedStatement pstmt = null;
		String StoreProcedureStatement;

		StoreProcedureStatement="EXEC P_Deduction_Gouv_Analysis " +  this.P_Year_ID + ", " + this.PeriodFrom + ", " + this.PeriodTo ;

	    log.log(Level.INFO, "call: " + StoreProcedureStatement);

		pstmt = DB.prepareStatement(StoreProcedureStatement, null);
		pstmt.execute();
        return "@Processed@ ";
	}
	
}
