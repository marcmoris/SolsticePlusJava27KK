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
<%@ page import="solstice.utils.PgiUtil" %>
<%@ page import="java.io.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*" %>

<%@ page extends="solstice.web.PageWebSolstice" %>

<%!	

		private void GenerateDetail() throws Exception 
		{
			String employeeId = request.getParameter("employeeId");
			String trxName = null;
			IUserInfo userInfo = PageWebSolstice.getUserInfo(pageContext);

			String sql = "select max(P_Form_Employee.P_Form_Employee_ID) as P_Form_Employee_ID, p_form.name Form, P_Form_Employee.FormType, P_Employee.Value, P_Employee.Name, count(*) as nbrform,  p_form_employee.taxation_region_id, c_Region.Name as Province from p_form_employee "
					   + " inner join p_form on p_form.p_form_id = p_form_employee.p_form_id "
					   + " inner join p_employee on p_employee.p_employee_id = p_form_employee.p_employee_id "
//					   + " left outer join p_employer on p_employer.p_employer_id = p_form_employee.p_employer_id "
					   + " left outer join c_region on c_region.c_region_id = p_form_employee.taxation_region_id "
					   + " where P_Form_Employee.isprinted = 'Y' "
				       ;
			
		    	sql += " and P_Form_Employee.P_Employee_ID = " + employeeId ;
		
			sql += " and FormType in ( 'O','M' )";
			
			if ( request.getParameter("yearId") != null && request.getParameter("yearId").trim().length() != 0 )
				sql += " and P_Form_Employee.P_Year_ID = " + request.getParameter("yearId");
			
			if ( request.getParameter("yearId") == null || request.getParameter("yearId").trim().length() == 0 )
			{
				sql += " and P_Year_ID in ( select P_Year_ID -1 From P_Year Where IsDefault = 'Y' )";
			}

			sql += " group by p_form.name , P_Form_Employee.FormType, P_Employee.Value, P_Employee.Name, p_form_employee.taxation_region_id, c_Region.Name "; 
				
			sql += " order by P_Employee.Value, p_form.name " ; 
			try
			{

				PreparedStatement pstmt = null;
				pstmt = DB.prepareStatement (sql, null);
				ResultSet rs = pstmt.executeQuery ();
				boolean alt = false;

				int count = 0;
				while (rs.next ())
				{
					count = count + 1;
/*					if ( (userInfo.hasPolicy("show_allemployee"))
					{
						tpl.assign("FORM", rs.getString( "Value") + " - " + rs.getString( "Name") + " - " + rs.getString("Form"));
					}
					else
*/					
						tpl.assign("FORM", rs.getString("Form"));
					
					if ( rs.getString("FormType").equals("O"))
						tpl.assign("TYPE", "Original");
					else
					{
//						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//						tpl.assign("TYPE", "Modifié ( " + sdf.format(rs.getTimestamp( "Created").getTime()).toString() + ")" );
						tpl.assign("TYPE", "Modifié");
					}


					
			  		P_Employee Employee = P_Employee.get(ctx, Integer.parseInt(employeeId), trxName);
					String sfile = String.valueOf(Employee.getP_Employee_ID()) + rs.getString( "P_Form_Employee_ID");

					String EXPORT_LOC =  PgiUtil.getSolsticeParameter(ctx, "TaxFormPDFPath");
	   				String fileName = EXPORT_LOC + sfile + ".pdf";
//	   				setFileName( fileName );
	    			File file = new File(fileName);
					MAttachment attachment = xxx.getAttachment();
				   	if ( attachment != null)
				   	{
						tpl.assign("VIEW", "<input type=\"button\" target=\"_blank\" value=\"Consulter\" onClick=\"sendAction('view', '" + sfile + "');\">");
				   	}
				   	else if (file.exists() && file.isFile() )    
						tpl.assign("VIEW", "<input type=\"button\" target=\"_blank\" value=\"Consulter\" onClick=\"sendAction('view', '" + sfile + "');\">");
					else 
						tpl.assign("VIEW", "&nbsp;");

//					if ( rs.getInt( "P_Employer_ID") != 0)
//						tpl.assign("EMPLOYER", rs.getString("Employer"));

					if ( rs.getInt( "nbrform") > 1 )
						tpl.assign("EMPLOYER",
						"Attention ! Vos relevés d'impôt comprennent 2 relevés T4 dû à votre changement de statut en cours d'année." + "<BR />" + 
						"Veuillez additionner les cases de chacun des relevés au fédéral pour obtenir le total de l'année." );

					if ( rs.getInt( "Taxation_Region_ID") != 0)
						tpl.assign("PROVINCE", rs.getString("Province"));

			        if ( alt)
				        tpl.assign("CELLCLASS", "tdfieldalt");
			        else
			            tpl.assign("CELLCLASS", "tdfield");
					alt = !alt;

					tpl.parse("main.line");
				}
				if ( count == 0 )
				{
					tpl.assign("VIEW", "Il n'y a pas de relevé disponible actuellement pour l'année sélectionnée.");
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
    private String employeeId = "";

    public String GeneratePage( ) throws Exception 
	{

	
		String trxName = null;
		String liste = "";
		this.setPageId("EndOfYearIndexAdmin");
		this.setSecurityCheck( true );
		this.setTemplate("ListFormByEmployeeAdmin.jtpl");

		
		if ( SecurityCheck() )
		{
			IUserInfo userInfo = PageWebSolstice.getUserInfo(pageContext);
			 // On doit vérifier si l'utilisateur a accès à tous les employés
			if(userInfo.hasPolicy("avis_allemployee"))
			{
				this.tpl.assign( "ComboBoxEmployee", ComboBoxEmployee());
	         	}
			this.tpl.assign( "LabelEmployee", "Employés");
	
			employeeId = request.getParameter("employeeId");
	
			P_Employee Employee;
			if ( employeeId != null)
				Employee = P_Employee.get(ctx, Integer.parseInt(employeeId), trxName);
			else
				Employee = P_Employee.get(ctx, userInfo.getEmployeeId(), trxName);

			System.out.println("* DEBUG * Employee " + Employee.getValue() + " - " + Employee.getName()  );
	   		System.out.println("* ERROR * UserInfo " + userInfo.getUserId() );
	
	
			String action = request.getParameter("action");
			String formID  = request.getParameter("formID");
			
			String error = request.getParameter("error");
			if ( error != null )
			{
				this.setTemplate("Error.jtpl");
				tpl.assign("ERRORMSG", "Erreur : le fichier demandé n'existe pas...");
				tpl.parse("main");
				return (tpl.out());
			
			}
				
			if(action != null)
			{
				System.out.println("* DEBUG * action " + action );
				System.out.println("* DEBUG * formID " + formID );

			String webAppUrl = PgiUtil.getSolsticeParameter(ctx, "WEBAPPURL");

		        if(action.equals("view"))
		        {
					response.sendRedirect(webAppUrl + "/endOfYearForm/download.jsp?fileName=" +formID );
		        }
			
			}
	
			System.out.println("* DEBUG 2 * Employee " + Employee.getValue() + " - " + Employee.getName()   );
					this.tpl.assign("TITLE", "Liste des relev&eacute;s d'imp&ocirc;t");
					this.tpl.assign( "LabelYear", "Année" );
					this.tpl.assign( "LabelForm", "Relevés" );
					this.tpl.assign( "LabelType", "Type" );
					this.tpl.assign( "LabelView", " " );
					this.tpl.assign( "LabelEmployer", "Message" );
					this.tpl.assign( "LabelProvince", "Province" );
					this.tpl.assign( "ComboBoxYear", ComboBoxYear() );
	
					GenerateDetail();
					
			
			tpl.parse("main");
			return (tpl.out());
		}
		return "";
	}
	
	public String ComboBoxYear() 
	{
			String selected = ""; 
			if ( request != null && request.getParameter("yearId") != null)
				selected = request.getParameter("yearId").trim();
			
			String result  = null;

			String sql = "select distinct P_Year_ID, Year, IsDefault "
				   + " from P_Year"
				   + " where Year >= '2009'" 
				  // + " and IsDefault = 'N' "  // current year not included 
				   + " order by Year Desc ";
			try
			{

				PreparedStatement pstmt = null;
				pstmt = DB.prepareStatement (sql, null);
				ResultSet rs = pstmt.executeQuery ();

				result  = "<select name=\"yearId\" id=\"yearId\"  onChange=\"search();\" onKeyPress=\"autoSelect(this);\">";
//				result += "<option></option> \n";

				while (rs.next ())
				{
					if ( rs.getString(1).equals(selected ) || ( selected.equals("") && rs.isFirst()) )
						result += "<option value=\"" + rs.getString(1) + "\" selected > " + rs.getString(2) + "</option> \n";
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


	public String ComboBoxEmployee() 
	{
			String selected = ""; 
			if ( request != null && request.getParameter("employeeId") != null)
				selected = request.getParameter("employeeId").trim();
			
			String result  = null;

			String sql = "select P_Employee_ID, value + ' ' + Name "
				   + " from P_Employee"
				   + " where IsEndOfYearFormWebConfirmation = 'Y'"
				   + " order by value  ";
			try
			{

				PreparedStatement pstmt = null;
				pstmt = DB.prepareStatement (sql, null);
				ResultSet rs = pstmt.executeQuery ();

				result  = "<select name=\"employeeId\" id=\"employeeId\"  onChange=\"search();\" onKeyPress=\"autoSelect(this);\">";
//				result += "<option></option> \n";

				while (rs.next ())
				{
					if ( rs.getString(1).equals(selected )   )
						result += "<option value=\"" + rs.getString(1) + "\" selected > " + rs.getString(2) + "</option> \n";
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