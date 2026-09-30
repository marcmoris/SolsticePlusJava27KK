/*
 * Created on 2005-01-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;

import java.sql.CallableStatement;
import java.sql.Timestamp;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.CLogger;
import org.compiere.util.Msg;
import org.compiere.process.*;

/**
 * @author frabou01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_AssignementPayIncrement extends SvrProcess {

	Timestamp EffectIn;
	
	CLogger log = CLogger.getCLogger (getClass());
	
	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#prepare()
	 */
	protected void prepare() {
		
		ProcessInfoParameter[] para = getParameter();
		String paramName = "";
		//Read the parameters
		for(int i=0; i < para.length; i++){
			paramName = para[i].getParameterName();
			if( paramName.equals("EffectIn")){
				EffectIn = (Timestamp)para[i].getParameter();
			}
		}

	}

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	protected String doIt() throws Exception {
		//Call the stored proc
		startProcess( "P_Assigment_pay_increment", this.getProcessInfo() );
		
		//Build the parameter string for the report
	//	String parameterString = BuildParameters();
		
		Env.startBrowser(PgiUtil.getWebServer( Env.getCtx(), "pay" ) + "Avancement_Echelon.rpt&rpt=Avancement_Echelon.rpt");
//		Env.startBrowser("http://riesling/reportprompt/reportpromptlight.html?path=C:\\Program%20Files\\Crystal%20Decisions\\Report%20Application%20Server%209\\Reports\\ReportPrompt\\Avancement_Echelon.rpt&rpt=Avancement_Echelon.rpt" );
		
		return " ";
	}
	
	private static boolean startProcess (String ProcedureName, ProcessInfo pi)
	{
		//  execute on this thread/connection
		//Log.trace(Log.l4_Data, "ProcessCtl.startProcess", ProcedureName + "(" + pi.getAD_PInstance_ID() + ")");
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
			//log.log(Level.SEVERE, "ProcessCtl.startProcess", e);
			pi.setSummary (Msg.getMsg(Env.getCtx(), "ProcessRunError") + " " + e.getLocalizedMessage());
			pi.setError (true);
			return false;
		}
	//	Log.trace(Log.l4_Data, "ProcessCtl.startProcess - done");
		return true;
	}   //  startProcess

}
