/*
 * Created on 2005-08-26
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import solstice.model.*;

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
public class P_Payment_Registry_Fill_Table extends SvrProcess {

	private int P_Frequency_ID=0;
	private int P_Period_ID=0;
	private int P_Payment_Group_ID=0;
	private int P_Employee_ID=0;
	private int P_Occupation_Group_ID=0;
	private String _ClassErrorMessage="Error P_Payment_Registry_Fill_Table";
	private String m_Format = "PDF";
		
	/**
	 * 
	 */
	public P_Payment_Registry_Fill_Table() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#prepare()
	 */
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		String paramName = "";
		
		//Read the parameters
		for(int i=0; i < para.length; i++){
			paramName = para[i].getParameterName();
			if( paramName.equals("P_Frequency_ID")){
				this.P_Frequency_ID = ((Number)para[i].getParameter()).intValue();
			}else if (paramName.equals("P_Period_ID")){
				this.P_Period_ID = ((Number)para[i].getParameter()).intValue();
			}else if (paramName.equals("P_Payment_Group_ID")){
				this.P_Payment_Group_ID = ((Number)para[i].getParameter()).intValue();
			}else if (paramName.equals("P_Employee_ID")){
				this.P_Employee_ID = ((Number)para[i].getParameter()).intValue();
			}else if (paramName.equals("P_Occupation_Group_ID")){
				this.P_Occupation_Group_ID = ((Number)para[i].getParameter()).intValue();
			}
            else if (paramName.equals("CrystalFormat"))
				m_Format = (String)para[i].getParameter();
		}	
	}
	
	private void AddInstanceParameters(int AD_Pinstance_ID)
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
			System.err.println(_ClassErrorMessage + " [AddParameters] " + e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	protected String doIt() throws Exception {
		if ((this.P_Frequency_ID > 0) && (this.P_Period_ID>0))
		{
			P_Payment_Registry PaymentRegistry = new P_Payment_Registry( getCtx(), -1, null);
			//PaymentRegistry.StartToFillTable(this.P_Frequency_ID,this.P_Period_ID,this.P_Payment_Group_ID,this.P_Occupation_Group_ID,this.P_Employee_ID);

	        int AD_Pinstance_ID = this.getAD_PInstance_ID();
	        
	        AddInstanceParameters(AD_Pinstance_ID);
	        
	        PaymentRegistry.StartToFillEmployeesTable(this.P_Period_ID,AD_Pinstance_ID);
	        
//			if (this.P_Employee_ID==0)
//				PaymentRegistry.StartToFillCumulativesTables(this.P_Period_ID);
	        
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

	        int l_Role = Env.getAD_Role_ID(Env.getCtx());
	        String repUrl = srvUrl+"Crystal/reports.jsp?AD_PInstance_ID=" + AD_Pinstance_ID + "&Format=" + m_Format + "&Ad_Role_ID=" + l_Role;

	        Env.startBrowser(repUrl);
	        return "";
		}
		return "";
	}
	


}
