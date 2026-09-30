/******************************************************************************
 * Product: Solstice+ Payroll & Human Resources Management                    *
 * Copyright (C) 2008 ProGestion Informatique, Inc. All Rights Reserved.      *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ProGestion Informatique, 210-5300 Bld des Galerie, Quebec, G2K 2A2 Canada  *
 * or via info@progestion.net or http://www.progestion.net/license.html       *
 ******************************************************************************/
package solstice.process;


import java.util.Calendar;

import org.compiere.model.MClient;
import org.compiere.process.*;
import org.compiere.util.Env;

import solstice.model.MPayProcessorV2;
import solstice.model.MPayProcessorV2Log;
import solstice.model.P_Employee;

import solstice.model.P_Period;
import solstice.model.P_Time_Sheet;
import solstice.process.TimeValidation;
import solstice.utils.PgiUtil;

/**
 * @author kevmar01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_inOutTimeValidationV2 extends SvrProcess 
{
	private int Org_ID = 0;
	private int Activity_ID = 0;

	private int Employee_ID = 0;
	private int Time_Sheet_ID = 0;
	private int Frequency_ID = 0;
	private int Period_ID = 0;
	private int Payment_Group_ID = 0;
    private int P_Distribution_Booklet_ID = 0;
    private int P_Collective_Labour_Agr_ID = 0;

 	
	private P_Period Period;
	private P_Employee Employee;
//	private P_Assignment Assignment;
	private P_Time_Sheet TimeSheet;
    private boolean IsRework = false;
    private boolean runBackGround = false;


	/**	Big Decimal 0	 */
	static final public java.math.BigDecimal ZERO = new java.math.BigDecimal(0.0);

	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("P_Employee_ID"))
				Employee_ID = para[i].getParameterAsInt();
			else if (name.equals("P_Time_Sheet_ID"))
				Time_Sheet_ID = para[i].getParameterAsInt();
			else if (name.equals("P_Payment_Group_ID"))
				Payment_Group_ID = para[i].getParameterAsInt();
			else if (name.equals("AD_Org_ID"))
				Org_ID = para[i].getParameterAsInt();
			else if (name.equals("C_Activity_ID"))
				Activity_ID = para[i].getParameterAsInt();
			else if (name.equals("P_Frequency_ID"))
				Frequency_ID = para[i].getParameterAsInt();
			else if (name.equals("P_Period_ID"))
				Period_ID = para[i].getParameterAsInt();
			else if ( name.equals("P_Distribution_Booklet_ID"))
				P_Distribution_Booklet_ID = para[i].getParameterAsInt();
			else if (name.equals("Rework"))
			    IsRework = "Y".equals(para[i].getParameter());
			else if (name.equals("runBackGround"))
				runBackGround = "Y".equals(para[i].getParameter());
//			else
//				log.log (Level.SEVERE,"prepare - Unknown Parameter: " + name);
		}
	}	//	prepare

	
	/**
	 * 	Read each time sheet 
	 *	@return 
	 *	@throws Exception
	 */
	
	private Calendar calendar = Calendar.getInstance();
	private java.util.Date now = calendar.getTime();


	protected String doIt () throws Exception
	{
		
 	    ThreadGroup tg1 = new ThreadGroup("Group PayProcessor");   

 	    runBackGround = true;
 	    
		boolean retValue = true;
		String ret = "";
		try {

			if ( runBackGround )
			{
  			    MPayProcessorV2 m_model = new MPayProcessorV2 (Env.getCtx(), 1000003, "Calcul");
				
				if ( IsRework ) 
				{
//					DB.executeUpdate("UPDATE P_Solstice_Parameters set Parameter = 'TrueTrue' where value = 'CalculPayrollV2'", null);
					  m_model.m_stack = m_model.getStackEmployee( "TrueTrue", Org_ID, Activity_ID, Employee_ID, Time_Sheet_ID, Payment_Group_ID, P_Distribution_Booklet_ID);
				}
				else
				{
//					DB.executeUpdate("UPDATE P_Solstice_Parameters set Parameter = 'True' where value = 'CalculPayrollV2'", null);
					  m_model.m_stack = m_model.getStackEmployee( "True", Org_ID, Activity_ID, Employee_ID, Time_Sheet_ID, Payment_Group_ID, P_Distribution_Booklet_ID);
				}
				

//				TimeValidation tmp = new TimeValidation( getCtx(), this.getAD_Client_ID(), Org_ID, Activity_ID, Employee_ID, Time_Sheet_ID, Payment_Group_ID, Frequency_ID, Period_ID, P_Distribution_Booklet_ID, IsRework, null);
//				ret = tmp.ValidationBackground();
	
				   // DU=1000499		   
				  
//				  m_model.m_stack = m_model.getStackEmployeeTest( "TrueTrue");
				  
				  MClient m_client = MClient.get(m_model.getCtx(), m_model.getAD_Client_ID());
				  
			      int nbrThread = 500;
				  try {
					  String pThread = PgiUtil.getSolsticeParameter(Env.getCtx(), "CalculPayrollThread");
					  if (pThread != null && !pThread.trim().isEmpty()) {
						  nbrThread = Integer.parseInt(pThread.trim());
					  }
				  } catch (Exception e) {}
					
				  if ( nbrThread > m_model.m_stack.size() )  nbrThread = m_model.m_stack.size();
				  
				  
				  for(int i = 0;i<nbrThread;i++){
						  
					  Thread mr = new Thread(tg1, new TimeValidationServerV2( m_model, "Thread-" + i, i));
		   		        mr.start();
				  }
				  
					while ( tg1.activeCount() != 0 )
					{
			            // On fait attendre le processus pour 10 minute
			            try
			            {
			        		Thread.sleep(1000);
			            }
			            catch (InterruptedException e)
			            {
			                e.printStackTrace();
			            }
					}
					
			}
			else
			{
				TimeValidation tmp = new TimeValidation( Env.getCtx(), this.getAD_Client_ID(), Org_ID, Activity_ID, Employee_ID, Time_Sheet_ID, Payment_Group_ID, Frequency_ID, Period_ID, P_Distribution_Booklet_ID, P_Collective_Labour_Agr_ID, IsRework, null);
				ret = tmp.Validation();
				
			}
		
		}
		catch( Exception e ) {
			retValue = false;
		}
		return retValue == true ? ret : "error";
		
	}
	
	

}
