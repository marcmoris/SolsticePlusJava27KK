package solstice.process;

import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.utils.PgiUtil;

public class Call_LstTransactionVerification extends SvrProcess 
{
    private int AD_Client_ID = 0;
    private int AD_Org_ID = 0;
    private int P_Booklet_Distribution_ID = 0;
    private int P_Period_ID = 0;
    private int Instance_ID = 0;
	private String m_Format = "PDF";

    protected void prepare()
    {
        ProcessInfoParameter[] para = getParameter();
        Instance_ID = getAD_PInstance_ID();
//        Org_ID = Env.getAD_Org_ID(getCtx());

        for (int i = 0; i < para.length; i++)
  	    {
  	        String name = para[i].getParameterName();
  	      
  	        if (para[i].getParameter() == null)
  		    ;
  	        else if (name.equals("AD_Org_ID"))
                AD_Org_ID = para[i].getParameterAsInt();
            else if (name.equals("P_Period_ID"))
  	           P_Period_ID = para[i].getParameterAsInt();
            else if (name.equals("P_Booklet_Distribution_ID"))
            	P_Booklet_Distribution_ID = para[i].getParameterAsInt();
  	        
  	    }
        AD_Client_ID = Env.getAD_Client_ID(getCtx());
    }	//	prepare

    private String trxName;
    
    protected String doIt() throws Exception
    {
    	String ret = "";
    	  
        int AD_Pinstance_ID = this.getAD_PInstance_ID();

    	DB.executeUpdate("Delete from P_LstTransactionVerification", null);
    	DB.executeUpdate("Exec [dbo].[SP_LstTransactionVerification] " + AD_Client_ID + ", " + AD_Org_ID + ", " + P_Period_ID + ", " + P_Booklet_Distribution_ID , null);

        String srvUrl = PgiUtil.getSolsticeParameter(Env.getCtx(), "WebAppURL");

        int l_Role = Env.getAD_Role_ID(Env.getCtx());
        ReportLauncher.callReport( AD_Pinstance_ID, l_Role, m_Format, getParameter() );

        
        return ret;
    	
    }


}
