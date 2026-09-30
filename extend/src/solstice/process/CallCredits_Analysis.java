/*
 * Created on 2005-01-24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Timestamp;

import org.compiere.model.MPInstancePara;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.process.*;

/**
 * @author frabou01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CallCredits_Analysis extends SvrProcess {

	//Stored proc exec parameters
	BigDecimal P_employee_ID = new BigDecimal(-1);
	BigDecimal P_credits_ID = new BigDecimal(-1);
	//Start and End Date
	Timestamp startdate;
	Timestamp enddate;
	

	
	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#prepare()
	 */
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		String paramName = "";
		
		
		//Read the parameters
		for(int i=0; i < para.length; i++){
			paramName = para[i].getParameterName();
			if( paramName.equals("P_EMPLOYEE_ID")){
				P_employee_ID = (BigDecimal)para[i].getParameter();
			}else if (paramName.equals("P_CREDITS_ID")){
				P_credits_ID = (BigDecimal)para[i].getParameter();
			}else if (paramName.equals("CrAnalysisStartDate")){
				startdate = (Timestamp)para[i].getParameter();
			}else if (paramName.equals("CrAnalysisEndDate")) {
				enddate = (Timestamp)para[i].getParameter();
			}
		}	
		
		
	}

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	protected String doIt() throws Exception {
		//Load The stored proc procedure exec parameters
		//LoadInstanceParameter(this.getProcessInfo());
		
		//Call the stored proc
		startProcess( "P_CREDIT_ANALYSIS_LOADER", this.getProcessInfo() );

		String parameterString = BuildParameters();

		Env.startBrowser(PgiUtil.getWebServer( Env.getCtx(),  "pay" ) + "Variation_banques.rpt&rpt=Variation_banques.rpt&param=" + parameterString );


//		Env.startBrowser("http://riesling/reportprompt/reportpromptlight.html?path=C:\\Program%20Files\\Crystal%20Decisions\\Report%20Application%20Server%209\\Reports\\ReportPrompt\\Variation_banques.rpt&rpt=Variation_banques.rpt&param=" + parameterString);
		
		return " ";
	}

	private String BuildParameters()
	{
		return startdate.toLocaleString() + "|" + enddate.toLocaleString() + "|";
	}

	
	private void LoadInstanceParameter(ProcessInfo pi){
		
		String sql = "";
		int no;
		MPInstancePara param ;
		
		//Save the employee Id
		param = new MPInstancePara(Env.getCtx(),pi.getAD_PInstance_ID(),1);
		//param.setAD_PInstance_ID(pi.getAD_PInstance_ID());
		param.setParameterName("P_EMPLOYEE_ID");
		//param.setSeqNo(1);
		param.setP_Number(P_employee_ID);
		param.save();
		/*
		sql = "INSERT INTO AD_PInstance_Para (AD_PInstance_ID,SeqNo,ParameterName, P_NUMBER) "
			+ "VALUES (" + pi.getAD_PInstance_ID() + ",1,'P_EMPLOYEE_ID'," + P_employee_ID.intValue() + ")";
		no = DB.executeUpdate(sql, null);
		*/
		
		//Save the credits Id
		param = new MPInstancePara(Env.getCtx(),pi.getAD_PInstance_ID(),2);
		//param.setAD_PInstance_ID(pi.getAD_PInstance_ID());
		param.setParameterName("P_CREDITS_ID");
		//param.setSeqNo(2);
		param.setP_Number(P_credits_ID);
		param.save();
		
		/*sql = "INSERT INTO AD_PInstance_Para (AD_PInstance_ID,SeqNo,ParameterName, P_NUMBER) "
			+ "VALUES (" + pi.getAD_PInstance_ID() + ",2,'P_CREDITS_ID'," + P_credits_ID.intValue() + ")";
		no = DB.executeUpdate(sql, null);
		*/
		
		//Save the start date
		param = new MPInstancePara(Env.getCtx(),pi.getAD_PInstance_ID(),3);
	//	param.setAD_PInstance_ID(pi.getAD_PInstance_ID());
		param.setParameterName("STARTDATE");
		//param.setSeqNo(3);
		param.setP_Date(startdate);
		param.save();
		
		/*
		sql = "INSERT INTO AD_PInstance_Para (AD_PInstance_ID,SeqNo,ParameterName, P_DATE) "
			+ "VALUES (" + pi.getAD_PInstance_ID() + ",3,'STARTDATE'," + startdate.toString() + ")";
		no = DB.executeUpdate(sql, null);*/
		
		//Save the end date
		param = new MPInstancePara(Env.getCtx(),pi.getAD_PInstance_ID(),4);
	//	param.setAD_PInstance_ID(pi.getAD_PInstance_ID());
		param.setParameterName("ENDDATE");
		//param.setSeqNo(4);
		param.setP_Date(enddate);
		param.save();
		/*
		sql = "INSERT INTO AD_PInstance_Para (AD_PInstance_ID,SeqNo,ParameterName, P_DATE "
			+ "VALUES (" + pi.getAD_PInstance_ID() + ",4,'ENDDATE'," + enddate.toString() + ")";
		no = DB.executeUpdate(sql, null);*/
	}
	
	private static boolean startProcess (String ProcedureName, ProcessInfo pi)
	{
		//  execute on this thread/connection
		try
		{
			String sql = "{call " + ProcedureName + " (?)}";
			CallableStatement cstmt = DB.prepareCall(sql);	//	ro??
			cstmt.setInt(1, pi.getAD_PInstance_ID());
			cstmt.execute();
			cstmt.close();
		}
		catch (Exception e)
		{
			pi.setSummary (Msg.getMsg(Env.getCtx(), "ProcessRunError") + " " + e.getLocalizedMessage());
			pi.setError (true);
			return false;
		}
	//	Log.trace(Log.l4_Data, "ProcessCtl.startProcess - done");
		return true;
	}   //  startProcess
}
