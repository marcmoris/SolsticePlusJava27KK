/*
 * Created on 2005-12-19
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
public class P_FormReleve3 extends SvrProcess{
	private int InstanceID = 0;

	private int P_Year_ID=0;
	private int P_Employee_ID=0;
	private String TypeCopie = "";
	private String m_Format = "PDF";
		
	/**
	 * 
	 */
	public P_FormReleve3() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		String paramName = "";
		InstanceID = getAD_PInstance_ID();
		
		//Read the parameters
		for(int i=0; i < para.length; i++){
			paramName = para[i].getParameterName();
			if( paramName.equals("P_Year_ID")){
				this.P_Year_ID = ((Number)para[i].getParameter()).intValue();
			}else if (paramName.equals("P_Employee_ID")){
				this.P_Employee_ID = ((Number)para[i].getParameter()).intValue();
			}else if (paramName.equals("TypeCopie")){
				this.TypeCopie = (para[i].getParameter()).toString();
			}
		}
	}
	
	private void SetPrinted()
	{
		String StringSql="UPDATE P_FORM_EMPLOYEE " + 
		 				"SET ISPRINTED='Y' " +
						"WHERE " +
						"P_FORM_ID = (SELECT P_FORM_ID FROM P_FORM WHERE VALUE = 'R3') " +
						"AND P_YEAR_ID = " + this.P_Year_ID;
		PreparedStatement pstmt = null;
		try
		{
			if (this.P_Employee_ID!=0)
				StringSql += " AND P_EMPLOYEE_ID =" + this.P_Employee_ID;
			
			pstmt = DB.prepareStatement(StringSql, null);
			pstmt.execute();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"SetPrinted, - " + StringSql, e);
		}
	}
	
	public boolean Validate()
	{
		int CountSelection=0;
		String CountSQL="SELECT COUNT(*) COUNT FROM " + 
						" P_FORM_EMPLOYEE FE " +
						" INNER JOIN P_FORM F ON F.P_FORM_ID = FE.P_FORM_ID " +
						" WHERE " +
						" F.VALUE = 'R3' " +
						" AND FE.P_YEAR_ID =" + this.P_Year_ID;
		PreparedStatement pstmp = null;
		try
		{
			if (this.P_Employee_ID!=0)
				CountSQL += " AND P_EMPLOYEE_ID =" + this.P_Employee_ID;
			
			pstmp = DB.prepareStatement(CountSQL, null);
			ResultSet rsCount = pstmp.executeQuery(); 
			
			while ( rsCount.next() )
			{
				CountSelection = rsCount.getInt("COUNT");
			}

		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"SetPrinted, - " + CountSQL, e);
		}
		return (CountSelection>0);
	}
		
	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	protected String doIt() throws Exception {    
		try
		{
			if (Validate())
			{
				if (this.TypeCopie.equals("1")) //Copy Employé
					SetPrinted();
				
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
		        String repUrl = srvUrl+"Crystal/reports.jsp?AD_PInstance_ID=" +  this.InstanceID + "&Format=" + m_Format + "&Ad_Role_ID=" + l_Role;
//			    String repUrl = srvUrl+"Crystal/reports.jsp?AD_PInstance_ID=" + this.InstanceID;
			    Env.startBrowser(repUrl);
			    return "Terminé avec succès";
			}
			else
			{
				return "Aucun enregistrement sélectionné";
			}
		}
		catch (Exception e)
		{
			return "Error: " + e.toString();
		}

	}
}
