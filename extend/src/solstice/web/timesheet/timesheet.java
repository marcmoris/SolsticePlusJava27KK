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
package solstice.web.timesheet;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.compiere.util.DB;

import solstice.web.PageWebSolstice;


public class timesheet  extends PageWebSolstice
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private void loadRecord()
    {
    	
    }

    private void Process ( String action, int timesheetId )
    {
    	
    }
 
    public String GeneratePage( ) throws Exception 
	{
		System.out.println("* generatePage *");
		this.setPageId("timesheet");
		this.setSecurityCheck( false );
		
/*		if ( ! SecurityCheck() )
		{
			response.sendError(403); 
		}
*/		
		System.out.println("set template");
		this.setTemplate("timesheet.jtpl");

		tpl.assign("TITLE","Feuille de temps" );
		tpl.assign("PAGETITLE", "Feuille de temps" );

		tpl.assign("LabelEmployeeName", "Nom de l'employé");
		tpl.assign("LabelEmployeeNo", "No. de l'employé");
		tpl.assign("LabelEmployee", "Employé");
		tpl.assign("LabelActivity", "Unité administrative");

		tpl.assign( "LabelPeriod", "Période" 	);
		tpl.assign( "SelectPeriod" ,ComboBoxPeriod() );

		tpl.assign( "LabelProject", "Projet" 	);
		tpl.assign( "SelectProject", this.ComboBoxProject() );

		
		tpl.assign( "SelectEarning", this.ComboBoxEarning() );
        tpl.assign("StartDate", "");
        tpl.assign("EndDate", "");

		System.out.println("parse");
       	tpl.parse("main");

		System.out.println("return");

       	return (tpl.out());

	}
    
	public String ComboBoxEarning( )  
	{
		String selected = ""; 
		if ( request != null && request.getParameter("earningId") != null)
			selected = request.getParameter("earningId").trim();
		
		String result  = null;
		String sql =  " select P_Gain_ID, ltrim(rtrim(Name)) + ' (' + ltrim(rtrim(Value)) + ')' as description  from P_Gain "
                   + " where IsActive = 'Y' "
                   + " and exists ( "
                   + " select * "
                   + " from P_Gain_GainInfo inner join P_GainInfo "
                   + " on P_Gain_GainInfo.P_GainInfo_ID = P_GainInfo.P_GainInfo_ID "
                   + " where P_GainInfo.Value = 'MotifAbsen' "
                   + " and P_Gain_GainInfo.P_Gain_ID = P_Gain.P_Gain_ID "
                   + " and P_Gain_GainInfo.To_Consider = 'Y' "
                   + " ) "
                   + " order by  2 asc ";
;
		try
		{

			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();

			result  = "<select name=\"earningId\" id=\"earningId\"  onKeyPress=\"autoSelect(this);\">";
			result += "<option ></option> \n";

			while (rs.next ())
			{
				if ( rs.getString(1).equals(selected ) )
					result += "<option value=\"" + rs.getString(1) + "\" selected > " + rs.getString(2)  + "</option> \n";
				else
					result += "<option value=\"" + rs.getString(1) + "\" > " + rs.getString(2)  + "</option> \n";
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;

			result += "</select>";

		}
		catch (Exception e)
		{
			System.out.println("* ERROR * Read earning " + sql + "Exception :" + e);
		}
		
		return result;
	}

	public String ComboBoxProject( )  
	{
		String selected = ""; 
		if ( request != null && request.getParameter("ProjectId") != null)
			selected = request.getParameter("ProjectId").trim();
		
		String result  = null;
		String sql =  " select C_Project_ID, ltrim(rtrim(Name)) + ' (' + ltrim(rtrim(Value)) + ')' as description  from C_Project "
                   + " where IsActive = 'Y' "
                   + " order by  2 asc ";
;
		try
		{

			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();

			result  = "<select name=\"ProjectId\" id=\"ProjectId\"  onKeyPress=\"autoSelect(this);\">";
			result += "<option ></option> \n";

			while (rs.next ())
			{
				if ( rs.getString(1).equals(selected ) )
					result += "<option value=\"" + rs.getString(1) + "\" selected > " + rs.getString(2)  + "</option> \n";
				else
					result += "<option value=\"" + rs.getString(1) + "\" > " + rs.getString(2)  + "</option> \n";
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;

			result += "</select>";

		}
		catch (Exception e)
		{
			System.out.println("* ERROR * Read earning " + sql + "Exception :" + e);
		}
		
		return result;
	}


}
