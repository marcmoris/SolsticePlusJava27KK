/*
 * Created on 4-Nov-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;


import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.P_Bank;
import solstice.model.P_Financial_Institution;
import java.util.logging.*;
import org.compiere.model.MPInstancePara;
import org.compiere.model.MRole;


/**
 * @author rejgar01
 *
 * Create file to transfert for the bank
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_InOutCreateFileTransfert extends SvrProcess
{ 
  private int Client_ID = 0;
  private int Org_ID = 0;
  private int Period_ID = 0;
  private int Frequency_ID = 0;
  private int Employee_ID = 0;
  private int Payment_Group_ID = 0;
  private int NoLigne = 0;
  private int NbreTotal = 0;
  private String NoClient;
  private String Banque;
  private BigDecimal MntTotal;
  private String Path = "";
  private String FileName = "";
  private String DateDisponibility = "";
  private String Sort = "";
  private ResultSet rs = null;
  private String m_Format = "PDF";
  private int P_Bank_ID;
  
  private boolean IsRework = false;
  private int numberTransfert = 0;

  private P_Financial_Institution Financial_Institution;

  private String NoTrans= "";
  
  protected void prepare()
  {
	ProcessInfoParameter[] para = getParameter();
	
	for (int i = 0; i < para.length; i++)
	{
	  String name = para[i].getParameterName();
	  if (para[i].getParameter() == null)
		;
	  else if (name.equals("AD_Client_ID"))
		Client_ID = para[i].getParameterAsInt();
	  else if (name.equals("AD_Org_ID"))
		Org_ID = para[i].getParameterAsInt();
	  else if (name.equals("P_Employee_ID"))
  	    Employee_ID = para[i].getParameterAsInt();
	  else if (name.equals("P_Period_ID"))
		Period_ID = para[i].getParameterAsInt();
	  else if (name.equals("P_Frequency_ID"))
		Frequency_ID = para[i].getParameterAsInt();
	  else if (name.equals("P_Payment_Group_ID"))
		Payment_Group_ID = para[i].getParameterAsInt();
	  else if (name.equals("P_Bank_ID"))
		  P_Bank_ID = para[i].getParameterAsInt();
	  else if (name.equals("Rework"))
		IsRework = "Y".equals(para[i].getParameter());
	  else if (name.equals("DisponibilityDate"))
        DateDisponibility = String.valueOf(para[i].getParameter());
	  else if (name.equals("Sort"))
		Sort = String.valueOf(para[i].getParameter());
	  else if (name.equals("CrystalFormat"))
			m_Format = (String)para[i].getParameter();
	  else if (name.equals("TransfertNumberVariable"))
		  numberTransfert = para[i].getParameterAsInt();
	  else
		log.log (Level.INFO,"prepare - Unknown Parameter: " + name);
	}
  }	//	prepare

  
  //public CreateFileTransfertRBC ( String Directory, String periodName )
  protected String doIt() throws Exception
  {
  	String strReturn="";
  	
	String trxName = null; // Trx.createTrxName();

  	if (Period_ID == 0)
  	{
  		log.log (Level.INFO,"UpdatePaymentDistribution-Period not found");
  		return ("*** Error - Period not found *");
  	}

  	P_Bank Bank = P_Bank.get(getCtx(), P_Bank_ID, null);

  	
  	String Sql = "select a.P_Payment_ID "  +
                 "from P_Payment a " +  // , P_Time_Sheet b 
	             "Where a.P_Period_ID = " + Period_ID +
	             "  and a.IsActive = 'Y' " +
				 "  and a.PaymentTypeDoc not in (  'Annulation+', 'Annulation-', 'Adjustement' ) " +
				 "  and isnull( a.NetPay, 0 ) > 0 " +
				 "  and a.PaymentType = 'D' " 
				 ;
				 
    if (Frequency_ID != 0)
       Sql += "  and a.P_Frequency_ID = " + Frequency_ID;
    
    if (Employee_ID != 0)
        Sql += " and a.P_Employee_ID = " + Employee_ID;
    
    if (Payment_Group_ID != 0)
       Sql += "  and a.P_Payment_Group_ID = " + Payment_Group_ID;
    
    if (IsRework)
       Sql += "  and a.TimeSheetStatus in ('E', 'T') ";  // "  and a.IsTransfer = 'Y'" +
    else
       Sql += "  and a.IsTransfer = 'N'" +
              "  and a.TimeSheetStatus = 'E' ";

    // Numéro de la banque est mantenant obligatoire.
    Sql += " and a.P_Payment_Group_ID in ( Select P_Payment_Group.P_Payment_Group_ID FROM P_Payment_Group WHERE P_Payment_Group.P_Bank_ID = " + P_Bank_ID + " )";
  	
    PreparedStatement pstmp = null;
  	try
	{
  		pstmp  = DB.prepareStatement(Sql,ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE, null);
	}
  	catch (Exception e)
	{
  		log.log (Level.SEVERE,"CreateFileTransfertRBC.doIt", e);
	}
  	//log.debug("Juste avant UpdatePaymentDistribution");
  	
	rs = pstmp.executeQuery ();

//    UpdatePaymentDistribution(pstmp, trxName, Sql );
    StringBuffer NewTransfert = new StringBuffer();
    
    String sql2 = "select a.P_Financial_Institution_ID "  +
    "From P_Financial_Institution a, P_BankAccount b " +
	   "where a.P_Financial_Institution_ID = b.P_Financial_Institution_ID" +
      "  and a.IsActive = 'Y' and b.IsActive = 'Y'" +
      " and b.P_Bank_ID = " + Bank.getP_Bank_ID() +
      " and b." + MRole.getDefault().getClientWhere(false) 
     ;
    
    //log.log (Level.SEVERE,sql);
    PreparedStatement psacc = null;
    try
    {
        psacc = DB.prepareStatement(sql2, null);
        ResultSet rsacc = psacc.executeQuery();
        if (rsacc.next())
        {
            Financial_Institution = P_Financial_Institution.get(getCtx(), rsacc.getInt("P_Financial_Institution_ID"), trxName);
        }
        rsacc.close();
        psacc.close();
    }
    catch (SQLException e)
    {
    	log.log (Level.SEVERE,"CreateFileTransfert.DoIt", e);
    }
    Banque = Financial_Institution.getValue();
    int xbq = -1;
    try
    {
        xbq = (new Integer(Banque)).intValue();
    }
    catch(Exception e)
    {
    	log.log (Level.SEVERE,"CreateFileTransfert.DoIt", e); 
    }
    switch (xbq) 
    {
    case 1:
    {
        //  Banque BMO
        P_InOutTransfertBMO TransfertBMO = new P_InOutTransfertBMO( getCtx());
        NoTrans=TransfertBMO.CreateFile(rs, NewTransfert, Period_ID, DateDisponibility);
        break;
    }
      
      case 3:
      {
          //  Banque Royale
          P_InOutTransfertRBC TransfertRBC = new P_InOutTransfertRBC( getCtx() );
          NoTrans=TransfertRBC.CreateFile(rs, NewTransfert, Period_ID, DateDisponibility);
          break;
      }
      case 815:
      {
         // Caisse populaire
         P_InOutTransfertCP TransfertCP = new P_InOutTransfertCP( getCtx() );
         NoTrans=TransfertCP.CreateFile(rs, NewTransfert, Period_ID, DateDisponibility);
         break;

      }
      case 006:
      {
         // Banque National du Canada
         P_InOutTransfertBNC TransfertBNC = new P_InOutTransfertBNC( getCtx(), numberTransfert, P_Bank_ID );
         NoTrans=TransfertBNC.CreateFile(rs, NewTransfert, Period_ID, DateDisponibility);
         break;
      }
      case 30:
      {
         // Canadian Western Bank
         P_InOutTransfertCWB TransfertCWB = new P_InOutTransfertCWB( getCtx(), numberTransfert, P_Bank_ID );
         NoTrans=TransfertCWB.CreateFile(rs, NewTransfert, Period_ID, DateDisponibility);
         break;
      }
      

    }
    rs.close();
    pstmp.close();
    
    MPInstancePara param = new MPInstancePara( getCtx(), getAD_PInstance_ID(), 99);
    param.setAD_PInstance_ID( getAD_PInstance_ID() );
    param.setParameterName("TransfertNumber");
    param.setP_String(NoTrans);
    param.save();

    
	int l_Role = Env.getAD_Role_ID(Env.getCtx());
    ReportLauncher.callReport( this.getAD_PInstance_ID(), l_Role, m_Format, getParameter() );

    //return "Nombre de transaction traité: " + NbreTotal + "<BR>  ** Montant de la transaction: " + MntTotal;
    return strReturn;
  } // doIt

  

    
}
