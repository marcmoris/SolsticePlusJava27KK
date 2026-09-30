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



import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;


import solstice.utils.PgiUtil;

/**
 * Execute Crystal Report 
  */
public class ReportLauncher extends SvrProcess 
{
	private String m_Format = "PDF";
	
	private ProcessInfoParameter[] para;
	
    protected void prepare()
    {
    	para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("CrystalFormat"))
				m_Format = (String)para[i].getParameter();
		}
        
    }	//	prepare


    protected String doIt() throws Exception
    {
        int AD_Pinstance_ID = this.getAD_PInstance_ID();
        int l_Role = Env.getAD_Role_ID(Env.getCtx());

        if ( PgiUtil.getSolsticeParameter(Env.getCtx(), "CrystalRas").equals("false"))
        {
        	CrystalViewerFrame mm = new CrystalViewerFrame();
        	mm.showViewer ( this.getAD_PInstance_ID(), m_Format, para);
        	
        }
        else
		{
		 	
	        String srvUrl = PgiUtil.getSolsticeParameter(Env.getCtx(), "WebAppURL");
	        String repUrl = srvUrl+"Crystal/reports.jsp?AD_PInstance_ID=" + AD_Pinstance_ID + "&Format=" + m_Format + "&Ad_Role_ID=" + l_Role;
	        Env.startBrowser(repUrl);
		}        
    	return "";
    }
    
    public static String callReport ( int AD_Pinstance_ID,  int l_Role, String Format, ProcessInfoParameter[] para ) throws Exception
    {
        if ( PgiUtil.getSolsticeParameter(Env.getCtx(), "CrystalRas").equals("false"))
        {
        	CrystalViewerFrame mm = new CrystalViewerFrame();
        	mm.showViewer ( AD_Pinstance_ID, Format, para);
        	
        }
        else
		{
		 	
	        String srvUrl = PgiUtil.getSolsticeParameter(Env.getCtx(), "WebAppURL");
	        String repUrl = srvUrl+"Crystal/reports.jsp?AD_PInstance_ID=" + AD_Pinstance_ID + "&Format=" + Format + "&Ad_Role_ID=" + l_Role;
	        Env.startBrowser(repUrl);
		}        
    	return "";

    }

    public static String callReport ( int AD_Pinstance_ID,  int l_Role, String Format, ProcessInfoParameter[] para, String ReportName ) throws Exception
    {
        if ( PgiUtil.getSolsticeParameter(Env.getCtx(), "CrystalRas").equals("false"))
        {
        	CrystalViewerFrame mm = new CrystalViewerFrame();
        	mm.showViewer ( AD_Pinstance_ID, Format, para, ReportName);
        }
        else
		{
		 	
	        String srvUrl = PgiUtil.getSolsticeParameter(Env.getCtx(), "WebAppURL");
	        String repUrl = srvUrl+"Crystal/reports.jsp?ReportName=" + ReportName + "AD_PInstance_ID=" + AD_Pinstance_ID + "&Format=" + Format + "&Ad_Role_ID=" + l_Role;
	        Env.startBrowser(repUrl);
		}        
    	return "";

    }

}
