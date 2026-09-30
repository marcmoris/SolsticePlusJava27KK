/*
 * Created on 2005-11-10
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * @author alenav01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_WorkAccidentsFollowUpReturn  extends SvrProcess {

	private String _ClassErrorMessage = "Error generating data for this report";
	private Timestamp StartDate=null;
	private Timestamp EndDate=null;
	private int AD_Pinstance_ID=0;
	/**
	 * 
	 */
	public P_WorkAccidentsFollowUpReturn() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		String paramName = "";
		AD_Pinstance_ID = this.getAD_PInstance_ID();
		
		//Read the parameters
		for(int i=0; i < para.length; i++){ 
			paramName = para[i].getParameterName();
			if( paramName.equals("StartDate")){
				this.StartDate = ((Timestamp)para[i].getParameter());
			}else if (paramName.equals("EndDate")){
				this.EndDate = ((Timestamp)para[i].getParameter());
			}
		}
	}
	
	private void AddDateParameters()
	{
		int ClientID = this.getAD_Client_ID(); 
        int OrgID = Env.getAD_Org_ID(Env.getCtx()); 
        int UserID = this.getAD_User_ID();
        
        try
		{        
        	String SQLInsert = "INSERT INTO AD_PINSTANCE_PARA (AD_PINSTANCE_ID, SEQNO, PARAMETERNAME, P_STRING, P_STRING_TO, P_NUMBER, P_NUMBER_TO, P_DATE, P_DATE_TO, INFO, INFO_TO, AD_CLIENT_ID, AD_ORG_ID, CREATED, CREATEDBY, UPDATED, UPDATEDBY, ISACTIVE) " +
	        "SELECT " + AD_Pinstance_ID + ", " +
	        "(SELECT ISNULL(MAX(SEQNO) + 1,0) FROM AD_PINSTANCE_PARA WHERE AD_PINSTANCE_ID = " + AD_Pinstance_ID + "), " +
	        "'AD_PInstance_ID',NULL, NULL," + AD_Pinstance_ID + ", NULL,NULL, NULL, NULL, NULL, " + ClientID + ", " + OrgID + ", GETDATE(), " + UserID + ", GETDATE(), " + UserID + ", 'Y'";
        	
			PreparedStatement pstmp = null;
			
			pstmp = DB.prepareStatement(SQLInsert, null);
			pstmp.execute();

		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, _ClassErrorMessage + " [AddDateParameters] ", e);
		}
	}

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	protected String doIt() throws Exception {
		
		AddDateParameters();
		
        P_InOutWorkAccidentsFollowUpReturn WorkAccidents = new P_InOutWorkAccidentsFollowUpReturn();
        WorkAccidents.StartCalculation(AD_Pinstance_ID);
		
        String sql = "SELECT WEBAPPURL "
        	+ " FROM P_SYSTEM_PARAMETERS ";
        PreparedStatement pstmt = DB.prepareStatement(sql, null);
        ResultSet rs = pstmt.executeQuery();
        String srvUrl = "";
        if(rs.next())
        	srvUrl = rs.getString(1);
        
        if(srvUrl.length() == 0)
        {
        	//TODO Error Msg avec Logger
        	System.out.println("Error With System Parameters");
        	return "";
        }
        String repUrl = srvUrl+"Crystal/reports.jsp?AD_PInstance_ID=" + AD_Pinstance_ID;
        Env.startBrowser(repUrl);
        return "";
	}
}
