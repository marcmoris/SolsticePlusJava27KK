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
<%@ page import="javax.servlet.http.*" %>
<%@ page import="java.util.SimpleTimeZone" %>
<%@ page import="org.compiere.util.Msg" %>
<%@ page import="java.util.Enumeration" %>

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

	if ( SecurityCheck() )
	{

        String action = request.getParameter("action");
        if(action != null )
        {
            if(action.equals("save") )
            {
            	
                String Type = request.getParameter("Type");
                
                // Détermine l'identifiant de l'absence à enregistrer
                String ER_Evaluation_Pre_ID = request.getParameter("ER_Evaluation_Pre_ID");

                String val = PgiUtil.convertHTMLString( request.getParameter("val").trim().replace("P","+"  ));


		System.out.println("* DEBUG * EvaluationStatus Ajax Val :" + val );

		System.out.println("* DEBUG * EvaluationStatus Ajax Type :" + Type );
		System.out.println("* DEBUG * EvaluationStatus Ajax ID :" + ER_Evaluation_Pre_ID );

                
    			if ( Type.equals("Cote")  ) 
    			{
    				String sql = "Update P_ER_Evaluation_Pre set GlobalQuotation = '" + val + "' WHERE P_ER_Evaluation_Pre_ID = " + ER_Evaluation_Pre_ID ;
    				DB.executeUpdate(sql, null);
    			}
    			if ( Type.equals("Comment")  )
    			{
    				String sql = "Update P_ER_Evaluation_Pre set Comment = '" + val + "' WHERE P_ER_Evaluation_Pre_ID = " + ER_Evaluation_Pre_ID ;
    				DB.executeUpdate(sql, null);
    			}

    			if ( Type.equals("EvaluationStatus") && val.equals( "true" ) )
    			{
    				String sql = "Update P_ER_Evaluation_Pre set EvaluationStatus = '" + P_ER_Evaluation_Pre.EVALUATIONSTATUS_Complete + "' WHERE P_ER_Evaluation_Pre_ID = " + ER_Evaluation_Pre_ID ;
    				DB.executeUpdate(sql, null);
    			}

    			if ( Type.equals("EvaluationStatus") && val.equals( "false" ) )
    			{
    				String sql = "Update P_ER_Evaluation_Pre set EvaluationStatus = '" + P_ER_Evaluation_Pre.EVALUATIONSTATUS_EnCours + "' WHERE P_ER_Evaluation_Pre_ID = " + ER_Evaluation_Pre_ID ;
    				DB.executeUpdate(sql, null);
    			}

    			
            }
        	
        }

		
	}

	return null;
}
	
%>