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
<%@ page import="org.compiere.util.Env" %>4
<%@ page import="org.compiere.util.TimeUtil" %>
<%@ page import="solstice.custom.IUserInfo" %>
<%@ page import="solstice.web.PageWebSolstice" %>
<%@ page import="solstice.model.P_Employee" %>
<%@ page import="solstice.model.P_Group_Automatic_Progression" %>
<%@ page import="solstice.model.P_ER_Evaluation_Pre" %>
<%@ page import="solstice.model.P_ER_Evaluation_Year" %>
<%@ page import="solstice.utils.PgiUtil" %>
<%@ page import="java.io.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="java.util.SimpleTimeZone" %>
<%@ page import="org.compiere.util.Msg" %>
<%@ page import="org.compiere.model.MActivity" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page extends="solstice.web.PageWebSolstice" %>

<%!

private String P_Manager_ID = "0";
private String P_Group_Automatic_Progression_ID = "0";
private String Language = "fr_CA"; 
private int P_ER_Evaluation_Year_ID = 1000101;

public String GeneratePage()  throws Exception  
{
    this.setPageId( "evaluationRendementPre" );
    this.setSecurityCheck( true );
    this.setTemplate("EvaluationPre.jtpl");

	if ( ! SecurityCheck() )
		response.sendError(403); 
	else
	{
		String action = request.getParameter("action");
	    if(action != null )
	    {
	        if(action.equals("save") )
	        {
	    		Enumeration NomsParam = request.getParameterNames();

	    		//
	    		//
	    		//
	    		String debug = "";
	    		while(NomsParam.hasMoreElements()) 
	    		{
	    		
	    			String NomParam = (String)NomsParam.nextElement();
	    			String[] ValeursParam = request.getParameterValues(NomParam);
	    			if ( NomParam != null && ValeursParam != null && NomParam.startsWith("Cote")  ) 
	    			{
	    				String sql = "Update P_ER_Evaluation_Pre set GlobalQuotation = '" + ValeursParam[0].trim().replace("P","+"  ) + "' WHERE P_ER_Evaluation_Pre_ID = " + NomParam.substring( NomParam.indexOf('[') + 1, NomParam.indexOf(']')) ;
//	    				System.out.println("* DEBUG * SQL\n" + sql );
	    				DB.executeUpdate(sql, null);
	    			}
	    			if ( NomParam != null && ValeursParam != null && NomParam.startsWith("Comment")  )
	    			{
	    				String sql = "Update P_ER_Evaluation_Pre set Comment = '" + PgiUtil.convertHTMLString( ValeursParam[0].trim()) + "' WHERE P_ER_Evaluation_Pre_ID = " + NomParam.substring( NomParam.indexOf('[') + 1, NomParam.indexOf(']')) ;
//	    				System.out.println("* DEBUG * SQL\n" + sql );
	    				DB.executeUpdate(sql, null);
	    			}

	    			if ( NomParam != null && ValeursParam != null && NomParam.startsWith("EvaluationStatus")  )
	    			{
	    				String sql;

    					System.out.println("* DEBUG * EvaluationStatus  :" + ValeursParam[0]);

	    				boolean completed = false;
	    				try
	    				{
	    					completed = ValeursParam[0].equals("on");
	    				}
	    				catch (Exception e)
	    				{
	    					System.out.println("* ERROR * EvaluationStatus Exception :" + e);
	    				}

	    				if ( completed ) 
	    					 sql = "Update P_ER_Evaluation_Pre set EvaluationStatus = '" + P_ER_Evaluation_Pre.EVALUATIONSTATUS_Complete + "' WHERE P_ER_Evaluation_Pre_ID = " + NomParam.substring( NomParam.indexOf('[') + 1, NomParam.indexOf(']')) ;
	    				else
	       					 sql = "Update P_ER_Evaluation_Pre set EvaluationStatus = '" + P_ER_Evaluation_Pre.EVALUATIONSTATUS_EnCours + "' WHERE P_ER_Evaluation_Pre_ID = " + NomParam.substring( NomParam.indexOf('[') + 1, NomParam.indexOf(']')) ;
	    					
	    				System.out.println("* DEBUG * SQL\n" + sql );
	    				DB.executeUpdate(sql, null);


	    			}

	    		}

	        }
	    	
	    }

		P_ER_Evaluation_Year Evaluation_Year = P_ER_Evaluation_Year.get( Env.getCtx(), P_ER_Evaluation_Year_ID, null);

		tpl.assign("TITLE", "Appréciation du rendement: Cotes préliminaires " + String.valueOf( Evaluation_Year.getYear() - 1 )  + " - "+ Evaluation_Year.getYear() );
		
	    SimpleDateFormat sdf = new SimpleDateFormat();
	    sdf.setTimeZone(new SimpleTimeZone(0, "GMT"));
	    sdf.applyPattern("dd MMMM yyyy");
	    

	    String StartDateStr = sdf.format(new java.sql.Date( Evaluation_Year.getStartDate().getTime()));
	    
	    String EndDateStr = sdf.format(new java.sql.Date( Evaluation_Year.getEndDate().getTime()));


		tpl.assign("TITLE2", "Appréciation du rendement : Cotes préliminaires du "  +  StartDateStr + " au " +  EndDateStr );
		tpl.assign("Save", Msg.getMsg(Language, "Save", true));
		this.tpl.assign( "LabelManager", "Gestionnaire" );
		this.tpl.assign( "LabelGroup", "Groupe" );
		this.tpl.assign( "CoteA", "A" );
		this.tpl.assign( "CoteB", "B" );
		this.tpl.assign( "CoteBPLUS", "B+" );
		this.tpl.assign( "CoteC", "C" );
		this.tpl.assign( "Total", "Total" );

		this.tpl.assign( "NbrCoteA", 0 );
		this.tpl.assign( "NbrCoteBPLUS", 0 );
		this.tpl.assign( "NbrCoteB", 0 );
		this.tpl.assign( "NbrCoteC", 0 );
		this.tpl.assign( "NbrTotal", 0 );
		this.tpl.assign( "PrcCoteA", 0 );
		this.tpl.assign( "PrcCoteBPLUS", 0 );
		this.tpl.assign( "PrcCoteB", 0 );
		this.tpl.assign( "PrcCoteC", 0 );

		if ( request.getParameter("ManagerId") != null && request.getParameter("ManagerId").trim().length() != 0 )
		{
			P_Manager_ID = request.getParameter("ManagerId");
		}
		else
		{
			P_Manager_ID = String.valueOf( ((IUserInfo)session.getAttribute("userInfo")).getEmployeeId());
		}

		P_Employee Manager = P_Employee.get( Env.getCtx(), Integer.parseInt( P_Manager_ID ), null );
		MActivity Activity = new MActivity( Env.getCtx(), Manager.getC_Activity_ID(), null );
		if ( Activity != null)
			tpl.assign("ACTIVITY", Activity.getValue() + " " + Activity.getName() );

	    if(((IUserInfo)session.getAttribute("userInfo")).hasPolicy("evaluationRendementPreGest"))
			this.tpl.assign( "ComboBoxManager", ComboBoxManager() );
		else
			this.tpl.assign( "ComboBoxManager", ((IUserInfo)session.getAttribute("userInfo")).getEmployeeName() );
			
		this.tpl.assign( "ComboBoxGroup", ComboBoxGroup() );

		P_Group_Automatic_Progression Group = P_Group_Automatic_Progression.get( Env.getCtx(), Integer.parseInt( P_Group_Automatic_Progression_ID ), null);

		if ( Group.getCompensationPolicy() != null && Group.getCompensationPolicy().equals( P_Group_Automatic_Progression.COMPENSATIONPOLICY_CompaRatio))
		{
			this.tpl.assign( "LabelComparatio", "Comparatio" );
			this.tpl.assign( "LabelMid", "Point milieu" );
			
		}
		else
		{
			this.tpl.assign( "LabelComparatio", "Position relative" );
			this.tpl.assign( "LabelMid", "" );
			
		}

		
		GenerateDetail();
		
		tpl.parse("main");
	}
	return (this.tpl.out());
		
}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean firstLoad = true;

	private void GenerateDetail()
	{ 

		int NbrCoteA = 0;
		int NbrCoteBPLUS = 0;
		int NbrCoteB = 0;
		int NbrCoteC = 0;
		int NbrTotal = 0;

		P_Group_Automatic_Progression Group = P_Group_Automatic_Progression.get( Env.getCtx(), Integer.parseInt( P_Group_Automatic_Progression_ID ), null);

		String sql = "SELECT P_ER_Evaluation_Pre_ID, P_Employee.value, P_Employee.Name, case when isnull(P_Employee.DATEREHIRED, P_Employee.DATEHIRED) > P_Employee.DATEHIRED then P_Employee.DATEREHIRED else P_Employee.DATEHIRED end as DATEHIRED , "
				   + " P_Job_Type.Name JobType, "
				   + " P_Group_Automatic_Progression.Name as Group_Labour_Agr, "
				   + " P_Job_Title.LongName JobTitle, "
				   + " isToEvaluate, "
				   + " P_Assignment_Param.CompaRatio,"
				   + " dbo.get_salary_scale_detail_mid( P_Assignment_Param.P_Salary_Scale_Detail_ID ) as MID, "
				   + " dbo.P_Assignment_Annual_Salary( P_Assignment_Param.P_Assignment_Param_ID ) as Annual_Salary, " 
				   + " NumberOfDays, "
				   + " GlobalQuotation, "
				   + " Comment, "
   				   + " P_Salary_Scale_Detail.SALARY_LIMIT_MAX, "
   				   + " P_Salary_Scale_Detail.LIMIT_MAX, "
   				   + " EvaluationStatus, "
   				   + " DBO.GET_OldGlobalQuotation( P_ER_Evaluation_Pre.P_ER_Evaluation_Pre_ID ) AS OldCote "
				   + " FROM [dbo].[P_ER_Evaluation_Pre] "
				   + " Inner JOIN P_Employee on P_Employee.P_Employee_ID = P_ER_Evaluation_Pre.P_Employee_ID "
//				   + " Left Outer join P_Employee_Gestionnaire on P_Employee_Gestionnaire.P_Employee_Gestionnaire_ID = P_ER_Evaluation_Pre.P_Employee_Gestionnaire_ID "
				   + " Left Outer join P_Assignment on P_Assignment.P_Assignment_ID = P_ER_Evaluation_Pre.P_Assignment_ID "
				   + " Left Outer join P_Assignment_Param on P_Assignment_Param.P_Assignment_Param_ID = P_ER_Evaluation_Pre.P_Assignment_Param_ID "
				   + " Left Outer Join P_Job_Type on P_Job_Type.P_Job_Type_ID = P_Employee.P_Job_Type_ID "
				   + " Left Outer Join P_Job_Title on P_Job_Title.P_Job_Title_ID = P_Assignment_Param.P_Job_Title_ID "
				   + " Left Outer Join P_Salary_Scale_Detail on P_Salary_Scale_Detail.P_Salary_Scale_Detail_ID = P_Assignment_Param.P_Salary_Scale_Detail_ID "
				   + " Left Outer join P_Group_Automatic_Progression on P_Group_Automatic_Progression.P_Group_Automatic_Progression_ID = P_ER_Evaluation_Pre.P_Group_Automatic_Progression_ID "
				   + " Where EvaluationStatus <> 'I' " ;
		
		if ( request.getParameter("ManagerId") != null && request.getParameter("ManagerId").trim().length() != 0 )
		{
			P_Manager_ID = request.getParameter("ManagerId");
		}
		else
		{
			P_Manager_ID = String.valueOf( ((IUserInfo)session.getAttribute("userInfo")).getEmployeeId());
		}

		if ( request.getParameter("GroupId") != null && request.getParameter("GroupId").trim().length() != 0 )
		{
			P_Group_Automatic_Progression_ID = request.getParameter("GroupId");
		}

		sql += " and P_ER_Evaluation_Pre.P_Employee_Gestionnaire_ID = " + P_Manager_ID;
		sql += " and P_ER_Evaluation_Pre.P_Group_Automatic_Progression_ID = " + P_Group_Automatic_Progression_ID;

		sql += " order by P_Employee.Name " ; 

		try
		{

			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			boolean alt = false;

			while (rs.next ())
			{
				tpl.assign("P_ER_Evaluation_Pre_ID", rs.getInt("P_ER_Evaluation_Pre_ID"));
				tpl.assign("NAME", rs.getString("Name"));
				tpl.assign("VALUE", rs.getString("Value"));
				tpl.assign("HIREDDATE", rs.getTimestamp("DateHired"));
				tpl.assign("JOBTYPE", rs.getString("JobType"));
				tpl.assign("JOBTITLE", rs.getString("JobTitle"));
				
				if ( rs.getString("isToEvaluate").equals("Y") )
					tpl.assign("IsToEvaluate", "Oui");
				else
					tpl.assign("IsToEvaluate", "Non");
				
				tpl.assign("NBRDAY", rs.getInt("NumberOfDays"));
				tpl.assign("COMPARATIO", rs.getBigDecimal("Comparatio"));
				tpl.assign("ANNUALSALARY", rs.getBigDecimal("Annual_Salary"));
//				tpl.assign("MAXSALARY", rs.getBigDecimal("Salary_Limit_Max"));
				tpl.assign("MAXSALARY", rs.getBigDecimal("Limit_Max"));
				if ( rs.getString("OldCote") == null || rs.getString("OldCote").equals( "-"))
					tpl.assign("OLDCOTE", "N/A");
				else
					tpl.assign("OLDCOTE", rs.getString("OldCote"));
				
				tpl.assign("COTE", ComboBoxCote( rs.getString( "GlobalQuotation"), rs.getInt("P_ER_Evaluation_Pre_ID"), rs.getString("EvaluationStatus"), rs.getString("isToEvaluate").equals("Y") ) );
				tpl.assign("COMMENT", rs.getString("Comment"));
				
				
				if ( rs.getString("EvaluationStatus").equals( P_ER_Evaluation_Pre.EVALUATIONSTATUS_EnCours ) == false )
//		        if( rs.getString("EvaluationStatus").equals( P_ER_Evaluation_Pre.EVALUATIONSTATUS_Complete ) )
		        	tpl.assign("EvaluationStatus", "");
		        else
		        	tpl.assign("EvaluationStatus", " checked");
				
		        
				if ( rs.getString("EvaluationStatus").equals( P_ER_Evaluation_Pre.EVALUATIONSTATUS_EnCours ) == false )
					tpl.assign("DISABLE",  "disabled=\"disabled\"");
				else
					tpl.assign("DISABLE", "");

				if ( rs.getString("EvaluationStatus").equals( P_ER_Evaluation_Pre.EVALUATIONSTATUS_Terminated ) )
					tpl.assign("DISABLE2",  "disabled=\"disabled\"");

				if ( Group.getCompensationPolicy().equals( P_Group_Automatic_Progression.COMPENSATIONPOLICY_CompaRatio))
				{
					tpl.assign("MID", rs.getBigDecimal("MID"));
				}
					

		        if ( alt)
				    tpl.assign("CELLCLASS", "tdfieldalt");
		        else
		            tpl.assign("CELLCLASS", "tdfield");

				alt = !alt;

				tpl.parse("main.line");

				if ( rs.getString( "GlobalQuotation") != null && rs.getString( "GlobalQuotation").equals( "-"  ) == false)
				{
					if ( rs.getString( "GlobalQuotation").equals( "A"  ))
						NbrCoteA++;
					if ( rs.getString( "GlobalQuotation").equals( "B+"  ))
						NbrCoteBPLUS++;
					if ( rs.getString( "GlobalQuotation").equals( "B"  ))
						NbrCoteB++;
					if ( rs.getString( "GlobalQuotation").equals( "C"  ))
						NbrCoteC++;
					NbrTotal++;
				}
			}

			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println("* ERROR * Read EvaluationPre sql " + sql + "Exception :" + e);
		}


		this.tpl.assign( "NbrCoteA", NbrCoteA );
		this.tpl.assign( "NbrCoteBPLUS", NbrCoteBPLUS );
		this.tpl.assign( "NbrCoteB", NbrCoteB );
		this.tpl.assign( "NbrCoteC", NbrCoteC );
		this.tpl.assign( "NbrTotal", NbrTotal );
		BigDecimal BTotal = new BigDecimal( NbrTotal );
		BigDecimal Cent = new BigDecimal( 100 ) ;

		if ( BTotal.compareTo( Env.ZERO) != 0)
		{
			this.tpl.assign( "PrcCoteA", (new BigDecimal( NbrCoteA )).divide( BTotal, 4, BigDecimal.ROUND_HALF_UP ).multiply( Cent).setScale(2, BigDecimal.ROUND_HALF_UP).toString() );
			this.tpl.assign( "PrcCoteBPLUS", (new BigDecimal( NbrCoteBPLUS )).divide( BTotal, 4, BigDecimal.ROUND_HALF_UP ).multiply( Cent ).setScale(2, BigDecimal.ROUND_HALF_UP).toString()  );
			this.tpl.assign( "PrcCoteB", (new BigDecimal( NbrCoteB )).divide( BTotal, 4, BigDecimal.ROUND_HALF_UP ).multiply( Cent ).setScale(2, BigDecimal.ROUND_HALF_UP).toString() );
			this.tpl.assign( "PrcCoteC", (new BigDecimal( NbrCoteC )).divide( BTotal, 4, BigDecimal.ROUND_HALF_UP ).multiply( Cent ).setScale(2, BigDecimal.ROUND_HALF_UP).toString() );
		}



	}
	
	private String ComboBoxCote( String Cote, int Id, String EvaluationStatus, boolean isToEvaluate )  
	{
		String selected = "";
		if ( Cote != null)
			selected = Cote;
		
		String result  = null;

		String sql = "select ad_ref_list.Value, ad_ref_list.Name from ad_reference  "
		           + " inner join ad_ref_list on ad_ref_list.AD_REFERENCE_ID = AD_REFERENCE.AD_REFERENCE_ID "
		           + " where ad_reference.name like 'P_GlobalQuotation' and ad_ref_list.isactive = 'Y'" 
		           + " order by ad_ref_list.Value "
		           ;
		if ( isToEvaluate == false )
			return "";
		
		try
		{

			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();

			if ( EvaluationStatus.equals( P_ER_Evaluation_Pre.EVALUATIONSTATUS_EnCours ) && isToEvaluate )
				result  = "<select name=\"Cote[" + Id + "]\" id=\"Cote[" + Id + "]\"  onBlur=\"javascript:changeData('{P_ER_Evaluation_Pre_ID}','Cote')\" onchange=\"javascript:changeData('{P_ER_Evaluation_Pre_ID}','Cote')\"> ";
			else
				result  = "<select disabled=\"disabled\" name=\"Cote[" + Id + "]\" id=\"Cote[" + Id + "]\" ,'Cote')\"> ";

			while (rs.next ())
			{
				if ( rs.getString(1).equals(selected )  )
					result += "<option value=\"" + rs.getString(1).replace("+", "P" ) + "\" selected > " + rs.getString(2) + "</option> \n";
				else
					result += "<option value=\"" + rs.getString(1).replace("+", "P" ) + "\" > " + rs.getString(2) + "</option> \n";
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;

			result += "</select>";

		}
		catch (Exception e)
		{
			System.out.println("* ERROR * Read ComboBox" + sql + "Exception :" + e);
		}

		return result;

	}

	

	
	public String ComboBoxManager() 
	{
			String selected = ""; 
			if ( request != null && request.getParameter("ManagerId") != null)
				selected = request.getParameter("ManagerId").trim();
			
			String result  = null;

			String sql = "select Distinct p_employee_gestionnaire.p_employee_gestionnaire_ID, p_employee_gestionnaire.name from p_employee_gestionnaire "
					   + " inner join p_gestionnaire on p_employee_gestionnaire.p_employee_gestionnaire_id = p_gestionnaire.p_employee_gestionnaire_id"
					   + " Order by p_employee_gestionnaire.name "
					   ;
			
			try
			{

				PreparedStatement pstmt = null;
				pstmt = DB.prepareStatement (sql, null);
				ResultSet rs = pstmt.executeQuery ();

				result  = "<select name=\"ManagerId\" id=\"ManagerId\" onChange=\"search();\" onKeyPress=\"autoSelect(this);\">";
//				result += "<option></option> \n";

				while (rs.next ())
				{
					if ( rs.getString(1).equals(selected ) || ( selected.equals("")  ) )
					{
						P_Manager_ID = rs.getString(1); 
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
				System.out.println("* ERROR * Read Manager " + sql + "Exception :" + e);
			}

			return result;

	}


	public String ComboBoxGroup() 
	{
			String selected = ""; 
			if ( request != null && request.getParameter("GroupId") != null)
				selected = request.getParameter("GroupId").trim();
			
			String result  = null;

			String sql = "select P_Group_Automatic_Progression.P_Group_Automatic_Progression_ID, P_Group_Automatic_Progression.name from P_Group_Automatic_Progression "
					   + " Where IsActive = 'Y' "
					   + " Order by P_Group_Automatic_Progression.name "
					   ;
			
			try
			{

				PreparedStatement pstmt = null;
				pstmt = DB.prepareStatement (sql, null);
				ResultSet rs = pstmt.executeQuery ();

				result  = "<select name=\"GroupId\" id=\"GroupId\" onChange=\"search();\" onKeyPress=\"autoSelect(this);\">";
//				result += "<option></option> \n";

				while (rs.next ())
				{
					if ( rs.getString(1).equals(selected ) || ( selected.equals("")  ) )
					{
						P_Group_Automatic_Progression_ID = rs.getString(1); 
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
				System.out.println("* ERROR * Read Group " + sql + "Exception :" + e);
			}

			return result;

	}

	
%>