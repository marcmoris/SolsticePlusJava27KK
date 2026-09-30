/*
 * Created on 2005-11-29
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;


import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * @author alenav01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_Carra_PensionPlansFollowUp extends SvrProcess 
{
	
	private static CLogger		s_log = CLogger.getCLogger (P_Carra_PensionPlansFollowUp.class);

	private String _ClassErrorMessage ="Error Generating les données pour le rapport";
	private int AD_Pinstance_ID=0;
	private int YearID=0;
	private int DeductionID=0;
	private String ReportType="";
 
	/**
	 * 
	 */
	public P_Carra_PensionPlansFollowUp() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	private void StartProcess()
	{
		if (this.DeductionID==0)
			CaculateCarraForAllDeduction();
		else
			CalculateCarraByDeduction(this.DeductionID);
	}
	
	private void CaculateCarraForAllDeduction()
	{
		String SQLString = "SELECT P_DEDUCTION_ID FROM P_EQUIVALENCE_FACTOR_PARAM WHERE P_YEAR_ID=" + this.YearID;
		
	    try
		{       	     		
			PreparedStatement pstmp = DB.prepareStatement(SQLString, null);
	        ResultSet rs = pstmp.executeQuery();
	
	        while ( rs.next() )
	        {
	        	CalculateCarraByDeduction(rs.getInt("P_DEDUCTION_ID"));
	        }
	        rs.close();
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, _ClassErrorMessage + " [CaculateCarraForAllDeduction] ", e);
		}	

	}
	
	private void CalculateCarraByDeduction(int iDeductionID)
	{
    	String SQLString = "SELECT COUNT(*) COUNT FROM P_EMPLOYEE_CARRA WHERE P_YEAR_ID=" + this.YearID + " AND P_DEDUCTION_ID=" + iDeductionID;
    	
    	try
		{
        	PreparedStatement pstmp = DB.prepareStatement(SQLString, null);
            ResultSet rs = pstmp.executeQuery();
        	
            while ( rs.next() )
            {
            	if (rs.getInt("COUNT")>0)
            		LauchCarraCalculation(iDeductionID,"Y");
            	else
            		LauchCarraCalculation(iDeductionID,"N");
            }
            rs.close();
    		
		}
    	catch (Exception e)
		{
    		log.log (Level.SEVERE, _ClassErrorMessage + " [CalculateCarraByDeduction] ", e);
		}
	}
	
	private void LauchCarraCalculation(int iDeductionID,String iUpdate)
	{
		/*Trx trx = null; 

        ProcessInfo pi = new ProcessInfo("Carra", 2000146); // HARDCODED
		MProcess process = MProcess.get(Env.getCtx(), pi.getAD_Process_ID());

        MPInstance pInstance = new MPInstance(process, -1);
		pi.setAD_PInstance_ID(pInstance.getAD_PInstance_ID());

        if (pi.getAD_PInstance_ID() == 0)
        {
            s_log.severe(Msg.getMsg(Env.getCtx(), "ProcessNoInstance"));
            return;
        }

        String sqlString = new String("");

        sqlString = "INSERT INTO AD_PInstance_Para (AD_PInstance_ID,SeqNo,ParameterName, P_NUMBER) " + "VALUES (" + pi.getAD_PInstance_ID() + ",2,'P_Deduction_ID'," + iDeductionID + ")";
        int no = DB.executeUpdate(sqlString);

        sqlString = "INSERT INTO AD_PInstance_Para (AD_PInstance_ID,SeqNo,ParameterName, P_NUMBER) " + "VALUES (" + pi.getAD_PInstance_ID() + ",3,'P_Year_ID'," + this.YearID + ")";
        no = DB.executeUpdate(sqlString);

        sqlString = "INSERT INTO AD_PInstance_Para (AD_PInstance_ID,SeqNo,ParameterName, P_STRING) " + "VALUES (" + pi.getAD_PInstance_ID() + ",4,'Update','" + iUpdate + "')";
        no = DB.executeUpdate(sqlString);
        
        sqlString = "INSERT INTO AD_PInstance_Para (AD_PInstance_ID,SeqNo,ParameterName, P_NUMBER) " + "VALUES (" + pi.getAD_PInstance_ID() + ",5,'NumberOfPeriod',26)";
        no = DB.executeUpdate(sqlString);

        sqlString = "INSERT INTO AD_PInstance_Para (AD_PInstance_ID,SeqNo,ParameterName, P_STRING) " + "VALUES (" + pi.getAD_PInstance_ID() + ",6,'RecordType','R')";
        no = DB.executeUpdate(sqlString);

        ProcessCtl worker = new ProcessCtl(this,pi, null);
        worker.start();*/ 
	
        boolean isUpdate = false;
        if(iUpdate.substring(0, 1).toUpperCase().endsWith("Y"))
        	isUpdate = true;
        P_Carra carra = new P_Carra(0, 0, iDeductionID, this.YearID, isUpdate, 26, "R");
        try
        {
        	String retValue = carra.doIt();
        }
        catch(Exception e)
        {
        	log.log (Level.SEVERE, _ClassErrorMessage + " LauchCarraCalculation ", e);
        }
	}
	
	protected void prepare() 
	{
		ProcessInfoParameter[] para = getParameter();
		String paramName = "";
		AD_Pinstance_ID = this.getAD_PInstance_ID();
		
		//Read the parameters
		for(int i=0; i < para.length; i++)
		{ 
			paramName = para[i].getParameterName();
  	        if (paramName.equals("P_Year_ID"))
  	        	this.YearID = ((BigDecimal)para[i].getParameter()).intValue();
  	        else if (paramName.equals("P_Deduction_ID"))
  	        	this.DeductionID = ((BigDecimal)para[i].getParameter()).intValue();
  	        else if (paramName.equals("TypeRapport"))
  	        	this.ReportType = para[i].getParameter().toString();
		}
	}


	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	protected String doIt() throws Exception {
		if (ReportType.compareTo("1")==0) /*'R'*/
			StartProcess();
             
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
