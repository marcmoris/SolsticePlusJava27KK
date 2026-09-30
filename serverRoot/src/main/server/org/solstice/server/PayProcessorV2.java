/******************************************************************************
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
package org.solstice.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;

import solstice.model.MPayProcessorV2;
import solstice.model.MPayProcessorV2Log;
import solstice.model.P_Payment_Group;
import solstice.utils.PgiUtil;

//import org.compiere.apps.ADialog;
import org.compiere.model.MClient;
import org.compiere.model.MNote;
import org.compiere.model.MPInstance;
import org.compiere.model.MProcess;
import org.compiere.model.MTable;
import org.compiere.model.MUser;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.ProcessInfoUtil;
import org.compiere.server.CompiereServer;
import org.compiere.util.DB;
//import org.compiere.util.EMail;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

public class PayProcessorV2 extends CompiereServer{

	/**
	 * 	Accounting Processor
	 *	@param model model 
	 */
	public PayProcessorV2 (MPayProcessorV2 model)
	{
		super (model, 30);	//	30 seconds delay
		m_model = model;
		m_client = MClient.get(model.getCtx(), model.getAD_Client_ID());
	}	//	PayProcessorV2

	/**	The Concrete Model			*/
	private MPayProcessorV2		m_model = null;
	/**	Last Summary				*/
	private StringBuffer 		m_summary = new StringBuffer();
	/** Client info					*/
	private MClient 			m_client = null;

	
	/**
	 * 	Work
	 * 
	 * La scédule est définie dans la table P_PayProcessorV2 
	 * 
	 */
	protected void doWork ()
	{
		return;
		
	}
	
	protected void OlddoWork ()
	{
		
		Env.setContext( getCtx(), "#AD_Client_ID", 1000004);
		Env.setContext( getCtx(), "#AD_Org_ID", 0);

		Env.setContext( Env.getCtx(), "#AD_Client_ID", 1000004);
		Env.setContext( Env.getCtx(), "#AD_Org_ID", 0);

 	    ThreadGroup tg1 = new ThreadGroup("Group PayProcessor");   

		String calculPayroll = PgiUtil.getSolsticeParameter(Env.getCtx(), "CalculPayrollV2");
		if ( calculPayroll.equals("False")) {
			return;  // Do noting
		}

		int User_ID = Integer.parseInt( PgiUtil.getSolsticeParameter(Env.getCtx(), "CalculPayrollUser") );
		int Payment_Group_ID = Integer.parseInt( PgiUtil.getSolsticeParameter(Env.getCtx(), "CalculPayrollPayment_Group_ID") );

		PgiUtil.setSolsticeParameter(Env.getCtx(), "CalculPayrollV2", "False");
		
		m_model.m_stack = m_model.getStackEmployee(  calculPayroll , Payment_Group_ID );
		m_summary = new StringBuffer();
		String TableName = "P_Time_Sheet";
		
		if (m_model.getAD_Table_ID() != 0)
		{
			MTable Table = MTable.get(getCtx(), m_model.getAD_Table_ID());
			TableName = Table.getTableName();
		}

		
		//  SELECT * FROM table
		StringBuffer sql = new StringBuffer ("Select * from AD_Client where AD_Client_ID=? " ); 

		if ( TableName == null )
			return;
		
		//
		int count = 0;
		int countError = 0;
		PreparedStatement pstmt = null;
		
		
		
		
//		int nbrThread = 500;
		
		int nbrThread = Integer.parseInt( PgiUtil.getSolsticeParameter(getCtx(), "CalculPayrollThread") ); 
		
		
		if ( nbrThread > m_model.m_stack.size() )  nbrThread = m_model.m_stack.size();
		
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(1, m_model.getAD_Client_ID());
			ResultSet rs = pstmt.executeQuery();
			while (  rs.next() )  // 
			{
				
				if (this.isInterrupted())
				{
					log.fine("Interrupted");
					rs.close();
					pstmt.close();
					pstmt = null;
					return;
				}

				count++;
				boolean ok = true;
				try
				{
					if ( TableName.equals( "P_Time_Sheet" )) {


						  log.log (Level.INFO,"Calculation - start");
						  for(int i = 0;i<nbrThread;i++){
							  
							  Thread mr = new Thread(tg1, new TimeValidationServer( m_model, "Thread-" + i, i));
				   		      mr.start();
				   		      
				   		        
						  }
						  
					}

				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, getName() + ": " + TableName, e);
					ok = false;
				}
				if (!ok)
					countError++;
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		if (pstmt != null)
		{
			try
			{
				pstmt.close();
			}
			catch (Exception e)
			{
			}
		}

		//
		
//		ici Thread.isAlive()
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
			
		
		String description = "Calcul de la rémunération terminé : " + getSecondsAlive() / 60 / 60 + " minutes";
		System.out.println("PayProcessor " + description + " " + m_model.getAD_Client_ID());

		
		int AD_Message_ID = 2200460;		//	HARDCODED Calcul de la rémunération terminé

		P_Payment_Group Group = P_Payment_Group.get( Env.getCtx(), Payment_Group_ID, null);

		
		MNote note = new MNote(getCtx(), AD_Message_ID, 100 , null );
		note.setClientOrg(m_model.getAD_Client_ID(), m_model.getAD_Org_ID());
		note.setTextMsg(getName());
		note.setAD_Org_ID( Group.getAD_Org_ID() );
		note.setDescription( description  );  //getDescription()
		note.setRecord(m_model.getAD_Table_ID(), 0);
		note.save();

		note = new MNote(getCtx(), AD_Message_ID, Env.getAD_User_ID(getCtx()) , null );
		note.setClientOrg(m_model.getAD_Client_ID(), m_model.getAD_Org_ID());
		note.setAD_Org_ID( Group.getAD_Org_ID() );
		note.setTextMsg(getName());
		note.setDescription( description  );  //getDescription()
		note.setRecord(m_model.getAD_Table_ID(), 0);
		note.save();

		

		m_client = new MClient(Env.getCtx(), 1000004, null);

//		m_client.sendEMail("support@solsticeplus.com", "Calcul de la rémunération terminé", description, null);
		MPayProcessorV2Log pLog = new MPayProcessorV2Log(m_model, m_summary.toString());
		pLog.setP_PayProcessorV2_ID( m_model.getP_PayProcessorV2_ID() );
		pLog.setReference("#" + String.valueOf(p_runCount) 
			+ " - " + TimeUtil.formatElapsed(new Timestamp(p_startWork)));
		pLog.save();
		
//		P_Payment_Group Group = P_Payment_Group.get( Env.getCtx(), Payment_Group_ID, null);
		MUser user = new MUser( Env.getCtx(), User_ID, null);
		m_client.sendEMail(user.getEMail(), "Calcul de la rémunération terminé " + Group.getName() , description, null);
		
		
		


	}  //	doWork
	private MPInstance m_instance;
	private MProcess m_process;
	private ProcessInfo m_pi;
	
	
	/**************************************************************************
	 * 	Get Parameter
	 *	@return parameter
	 */
	protected ProcessInfoParameter[] getParameter( int AD_PInstance_ID)
	{
		ProcessInfoUtil.setParameterFromDB(m_pi);
		
		ProcessInfoParameter[] retValue = m_pi.getParameter();
		if (retValue == null)
		{
			ProcessInfoUtil.setParameterFromDB(m_pi);
			retValue = m_pi.getParameter();
		}
		return retValue;
	}	//	getParameter

	
	/**
	 * 	Get Server Info
	 *	@return info
	 */
	public String getServerInfo()
	{
		return "#" + p_runCount + " - Last=" + m_summary.toString();
	}	//	getServerInfo

	
	
}
