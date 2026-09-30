/*
 * Created on Sep 6, 2005
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
import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.P_Frequency;
import solstice.model.P_Period;

/**
 * @author rejgar01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LauncherTransfertGeneralLedger360 extends SvrProcess 
{
    private int Client_ID = 0;
    private int AD_Org_ID = 0;
    private int User_ID = 0;
    private int Frequency_ID = 0;
    private int Period_ID = 0;
    private int Instance_ID = 0;
    private boolean IsRework = false;
    private boolean IsOnlyPaymentCanceled = false;
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
  	        else if (name.equals("P_Frequency_ID"))
               Frequency_ID = para[i].getParameterAsInt();
            else if (name.equals("P_Period_ID"))
  	           Period_ID = para[i].getParameterAsInt();
            else if (name.equals("Rework"))
            	IsRework = "Y".equals(para[i].getParameter());
            else if (name.equals("IsOnlyPaymentCanceled"))
            	IsOnlyPaymentCanceled = "Y".equals(para[i].getParameter());
  	        else 
  		       log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
  	        
  	    }
        Client_ID = Env.getAD_Client_ID(getCtx());
        User_ID = Env.getAD_User_ID(getCtx());
        
    }	//	prepare


    protected String doIt() throws Exception
    {
        if (Period_ID == 0)
        {
            log.log(Level.SEVERE, "TransfertGeneralLedger - Period not found");
            return ("*** Error - Start Period not found *");
        }
        if (Frequency_ID == 0)
        {
            log.log(Level.SEVERE, "TransfertGeneralLedger - Frequency not found");
            return ("*** Error - Frequency not found");
        }
        String ret;
         //TODO read PaymentGroup
/*        
        if ( Env.getAD_Client_ID(this.getCtx()) == 11 )
        {
        	P_InOutTransfertGeneralLedger TransfertGL = new P_InOutTransfertGeneralLedger( this.getCtx() );
        	ret = TransfertGL.ExecuteProcess(Client_ID, AD_Org_ID, Frequency_ID, Period_ID, Instance_ID, IsRework );
        }
        else
        {
        	P_InOutTransfertGeneralLedgerKrispy TransfertGL = new P_InOutTransfertGeneralLedgerKrispy( this.getCtx() );
        	ret = TransfertGL.ExecuteProcess(Client_ID, AD_Org_ID, Frequency_ID, Period_ID, Instance_ID, IsRework );
        }

*/
    	P_InOutTransfertGeneralLedger360 TransfertGL = new P_InOutTransfertGeneralLedger360( this.getCtx() );
    	ret = TransfertGL.ExecuteProcess(Client_ID, AD_Org_ID, Frequency_ID, Period_ID, Instance_ID, IsRework );
/*        
        int AD_Pinstance_ID = this.getAD_PInstance_ID();

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
        	return "Error With System Parameters";
        }

		int l_Role = Env.getAD_Role_ID(Env.getCtx());
        ReportLauncher.callReport( AD_Pinstance_ID, l_Role, m_Format, getParameter() );
*/
        return ret;
    }

}
