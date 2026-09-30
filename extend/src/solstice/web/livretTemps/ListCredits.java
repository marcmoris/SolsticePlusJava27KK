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
package solstice.web.livretTemps;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.util.DB;

import java.sql.Timestamp;

import solstice.utils.PgiUtil;
import solstice.web.PageWebSolstice;

public class ListCredits extends PageWebSolstice //HttpServlet implements SingleThreadModel 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String p_employee_ID = null;
	String p_credits_ID = null;
	String p_date = null;


	private void GenerateDetail()
	{
		String sql = "select rtrim(P_Employee.Name) as Employee_Name," 
			   + " rtrim(P_Employee.Value) as Employee_No," 
	           + " rtrim(P_Credits.Name) as Credits_Name," 
	           + " rtrim(P_Credits.Value) as Credits_No," 
	           + " SUM( PMOVEMENTVARIATION ) Solde, "
	           + " P_UOM.Value as Unit "
	           + " from P_Employee " 
	           + " inner join P_Credits_Movement on ( P_Employee.P_Employee_ID = P_Credits_Movement.P_Employee_ID )  "
	           + " inner join P_Credits on ( P_Credits_Movement.P_Credits_ID = P_Credits.P_Credits_ID) " 
	           + " inner join P_UOM on ( P_Credits.P_UOM_ID = P_UOM.P_UOM_ID)"
	           + " WHERE P_Employee.isActive='Y' ";
		try
		{

			if ( this.p_credits_ID != null && this.p_credits_ID.length() != 0  )
				sql += " And P_Credits.P_Credits_ID = " + this.p_credits_ID;

			if ( this.p_employee_ID != null && this.p_employee_ID.length() != 0)	
				sql += " And P_Employee.P_Employee_ID = " + this.p_employee_ID;

			if ( this.p_date != null && this.p_date.length() != 0 )
				sql += " And P_Credits_Movement.PMovementDate <= " + DB.TO_DATE(new Timestamp(PgiUtil.stringToDateV2( p_date ).getTimeInMillis())); 

			
		    sql += " group By P_Employee.Value, P_Employee.Name, P_Credits.Value, P_Credits.Name, P_UOM.Value"
		           + " order By P_Employee.Name, P_Credits.Name"
            ;
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			boolean alt = false;

			while (rs.next ())
			{
				tpl.assign("EMPLOYEE_NO", rs.getString("Employee_No"));
				tpl.assign("EMPLOYEE_NAME", rs.getString("Employee_Name"));
				tpl.assign("CREDITS_NO", rs.getString("Credits_No"));
				tpl.assign("CREDITS_NAME", rs.getString("Credits_Name"));
				tpl.assign("SOLDE", rs.getBigDecimal("Solde").toString());
				tpl.assign("UNIT", rs.getString("Unit"));
		        if ( alt)
			        tpl.assign("CELLCLASS", "tdfieldalt");
		        else
		            tpl.assign("CELLCLASS", "tdfield");
				alt = !alt;

				tpl.parse("main.line");
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println("* ERROR * Read Credits Mouvement sql " + sql + "Exception :" + e);
		}
	}
	

	public String GeneratePage()  throws Exception  
	{
	    this.setPageId( "livrbanklist" );
		this.setSecurityCheck( true );
		this.setTemplate("ListCredits.jtpl");

		if ( request != null )
	    {
	        if ( request.getParameter("employeeId") != null)
	        	p_employee_ID = request.getParameter("employeeId").trim();
	        else p_employee_ID = null;
	        
	        if ( request.getParameter("creditsId") != null)
	        	p_credits_ID  = request.getParameter("creditsId").trim();
	        else p_credits_ID  = null;
	        
	        if ( request.getParameter("date") != null)
	        	p_date        = request.getParameter("date");
	        else p_date = null;
	    }

        
		if ( SecurityCheck() )
		{
			this.tpl.assign("TITLE", "Liste des banques de temps");

			this.tpl.assign( "ComboBoxEmployee", ComboBoxEmployee() );
			this.tpl.assign( "ComboBoxCredits", ComboBoxCredits() );
			GenerateDetail();
			
			this.tpl.parse("main");
			return (this.tpl.out());
			
		}
		return null;
	}

}
