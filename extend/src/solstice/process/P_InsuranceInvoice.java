/*
 * Created on 2005-01-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;

import java.math.BigDecimal;
import org.compiere.util.Env;
import org.compiere.process.*;

/**
 * @author frabou01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_InsuranceInvoice extends SvrProcess {

//	Stored proc exec parameters
	BigDecimal P_DEDUCTION_FAMILY_ID = new BigDecimal(-1);
	BigDecimal P_PERIOD_ID = new BigDecimal(-1);
	
	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#prepare()
	 */
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		String paramName = "";
		
/*		//Read the parameters
		for(int i=0; i < para.length; i++){
			paramName = para[i].getParameterName();
			if( paramName.equals("P_DEDUCTION_FAMILY_ID")){
				P_DEDUCTION_FAMILY_ID = (BigDecimal)para[i].getParameter();
			}else if (paramName.equals("P_PERIOD_ID")){
				P_PERIOD_ID = (BigDecimal)para[i].getParameter();
			}
		}
*/			
	}

/*	private String BuildParameters(){
		
		return P_DEDUCTION_FAMILY_ID.intValue() + "|" + P_PERIOD_ID.intValue() + "|";
	}
*/
	private String BuildParameters()
	{
		
		return this.getAD_PInstance_ID() + " ";
	}

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	protected String doIt() throws Exception {
//		Build the parameter string for the report
		String parameterString = BuildParameters();
		
		System.out.println(" Call : " + PgiUtil.getWebServer( Env.getCtx() , "pay" ) + "Auto_Facturation_Ass.rpt&rpt=Auto_Facturation_Ass.rpt&param=" + parameterString ); 

		Env.startBrowser(PgiUtil.getWebServer( Env.getCtx(),  "pay" ) + "Auto_Facturation_Ass.rpt&rpt=Auto_Facturation_Ass.rpt&param=" + parameterString);
//		Env.startBrowser("http://riesling/reportprompt/reportpromptlight.html?path=C:\\Program%20Files\\Crystal%20Decisions\\Report%20Application%20Server%209\\Reports\\ReportPrompt\\Auto_Facturation_Ass.rpt&rpt=Auto_Facturation_Ass.rpt&param=" + parameterString);
		
		return " ";
	}

}
