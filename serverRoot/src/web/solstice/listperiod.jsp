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
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="javax.servlet.jsp.PageContext" %>
<%@ page import="org.compiere.util.DB" %>
<%@ page import="org.compiere.util.Env" %>
<%@ page import="org.compiere.util.TimeUtil" %>
<%@ page import="solstice.custom.IUserInfo" %>
<%@ page import="solstice.web.PageWebSolstice" %>
<%@ page import="solstice.model.P_Employee" %>
<%@ page import="solstice.model.P_Workplace" %>
<%@ page import="solstice.utils.PgiUtil" %>
<%@ page import="java.io.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*" %>
<%@ page import="org.compiere.model.MLocation" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.SimpleTimeZone" %>
<%@ page extends="solstice.web.PageWebSolstice" %>

<%!

private String P_Year_ID = "0";

public String GeneratePage()  throws Exception  
{
    this.setPageId( "ListPeriod" );
    this.setSecurityCheck( false );
    this.setTemplate("ListPeriod.jtpl");

	if ( SecurityCheck() )
	{
		this.tpl.assign("TITLE", "Liste des périodes de paie");
		this.tpl.assign( "LabelYear", "Année" );
		this.tpl.assign( "LabelPeriod", "Période" );
		this.tpl.assign( "LabelStartDate", "Date début" );
		this.tpl.assign( "LabelEndDate", "Date fin" );
		this.tpl.assign( "LabelPayDate", "Date paiement" );
		this.tpl.assign( "LabelFrequency", "Périodicité" );
		this.tpl.assign( "ComboBoxYear", ComboBoxYear() );
		this.tpl.assign( "ComboBoxFrequency", ComboBoxFrequency() );

		GenerateDetail();
		
		this.tpl.parse("main");
		return (this.tpl.out());
		
	}
	return null;
}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean firstLoad = true;

	private void GenerateDetail()
	{
		String sql = "select Name, StartDate, EndDate, PayDate, PeriodStatus from P_Period Where 1=1 " ;
		
		if ( request.getParameter("yearId") != null && request.getParameter("yearId").trim().length() != 0 )
		{
			P_Year_ID = request.getParameter("yearId");
		}

		sql += " and P_Year_ID = " + P_Year_ID;
		
		if ( request.getParameter("frequencyId") != null && request.getParameter("frequencyId").trim().length() != 0 )
			sql += " and P_Frequency_ID = " + request.getParameter("frequencyId");

		if ( request.getParameter("frequencyId") == null || request.getParameter("frequencyId").trim().length() == 0 )
		{
			sql += " and P_Frequency_ID in ( select TOP 1 P_Frequency_ID From P_Frequency Where IsDefault = 'Y' )";
		}

		sql += " order by StartDate " ; 
		try
		{

			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			boolean alt = false;

			while (rs.next ())
			{
				tpl.assign("NAME", rs.getString("Name"));
				tpl.assign("STARTDATE", rs.getTimestamp("StartDate"));
				tpl.assign("ENDDATE", rs.getTimestamp("EndDate"));
				tpl.assign("PAYDATE", rs.getTimestamp("PayDate"));

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
			System.out.println("* ERROR * Read Period sql " + sql + "Exception :" + e);
		}
	}
	
	public String ComboBoxYear() 
	{
			String selected = ""; 
			if ( request != null && request.getParameter("yearId") != null)
				selected = request.getParameter("yearId").trim();
			
			String result  = null;

			String sql = "select top 10 P_Year_ID, Year, IsDefault "
				   + " from P_Year"
				   + " order by Year Desc ";
			try
			{

				PreparedStatement pstmt = null;
				pstmt = DB.prepareStatement (sql, null);
				ResultSet rs = pstmt.executeQuery ();

				result  = "<select name=\"yearId\" id=\"yearId\" onChange=\"search();\" onKeyPress=\"autoSelect(this);\">";
//				result += "<option></option> \n";

				while (rs.next ())
				{
					if ( rs.getString(1).equals(selected ) || ( selected.equals("") && rs.getString("IsDefault").equals("Y") ) )
//					if ( rs.getString(1).equals(selected ) || ( selected.equals("") && rs.isFirst() ) )
					{
						P_Year_ID = rs.getString(1); 
						result += "<option value=\"" + rs.getString(1) + "\" selected > " + rs.getString(2) + "</option> \n";
					}	
					else
						result += "<option value=\"" + rs.getString(1) + "\" > " + rs.getString(2) + "</option> \n";
				}
				rs.close ();
				pstmt.close ();
				pstmt = null;

				result += "</select>";

			}
			catch (Exception e)
			{
				System.out.println("* ERROR * Read Year " + sql + "Exception :" + e);
			}

			return result;

	}

%>