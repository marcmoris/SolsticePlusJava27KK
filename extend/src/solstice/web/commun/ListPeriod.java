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
package solstice.web.commun;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.compiere.util.DB;

import solstice.web.PageWebSolstice;

public class ListPeriod extends PageWebSolstice
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean firstLoad = true;

	private void GenerateDetail()
	{
		String sql = "select Name, StartDate, EndDate, PayDate, PeriodStatus from P_Period Where 1=1 " ;
		
		if ( request.getParameter("yearId") != null && request.getParameter("yearId").trim().length() != 0 )
			sql += " and P_Year_ID = " + request.getParameter("yearId");
		
		if ( request.getParameter("frequencyId") != null && request.getParameter("frequencyId").trim().length() != 0 )
			sql += " and P_Frequency_ID = " + request.getParameter("frequencyId");

		if ( request.getParameter("yearId") == null || request.getParameter("yearId").trim().length() == 0 )
		{
			sql += " and P_Year_ID in ( select P_Year_ID From P_Year Where IsDefault = 'Y' )";
		}
			
		if ( request.getParameter("frequencyId") == null || request.getParameter("frequencyId").trim().length() == 0 )
		{
			sql += " and P_Frequency_ID in ( select P_Frequency_ID From P_Frequency Where IsDefault = 'Y' )";
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

}
