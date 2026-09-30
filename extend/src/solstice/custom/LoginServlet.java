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
package solstice.custom;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.compiere.model.MClient;
import org.compiere.model.MUser;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.SecureEngine;

import solstice.utils.PgiUtil;

/**
 * @author frafor01
 *
 * Cette classe sert à créer une fenêtre de login et renvoit
 * à la page l'ayant appelé.
 */
public class LoginServlet extends HttpServlet
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private class MyUserInfo implements IUserInfo
    {
        
        /*
         * Members
         */
        private int userId;
/*        private int employeeId;
 * 
 */
        private String employeeName;
        private String employeeValue;
        
        private Vector<String> policies;
        private int[] distributionList;
        
        /*
         * Getters
         */
        //+2011.02.16 Return ctx.
        public Properties getCtx()
        {
        	return Env.getCtx();
        }
        //-2011.02.16 Return ctx.
        
        public int getUserId() {return this.userId;}
        
        public void setUserId( int UserId ) 
        {
        	this.userId = UserId;
        	this.policies.clear();
            try
            {
            	//this.findEmployeeId();
            	
                // On charge ensuite les politiques de sécurités applicables aux groupes
                // associé à l'employé trouvé. On utilise des politiques de sécurité
                // optimiste, donc il suffit qu'une permission se retrouve à un seul endroit
                // pour qu'elle soit appliquable.
                try
                {
                    PreparedStatement stmt = DB.prepareStatement(
                            "select distinct P_Security.Name" +
                            "  from P_Security," +
                            "       P_Group_Security" +
                            " where P_Security.P_Security_ID = P_Group_Security.P_Security_ID" +
                            "   and exists ( select 1" +
                            "                  from P_Group_Employee" +
                            "                 where P_Group_ID = P_Group_Security.P_Group_ID" +
                            "                   and P_Group_Employee.isActive = 'Y' " +
                            "                   and P_Employee_ID = " + getEmployeeId() + " )", null);
                    
                    ResultSet rs = stmt.executeQuery();
                    
                    // On ajoute maintenant les politiques appliquables à la liste
                    while(rs.next())
                    {
                        this.policies.add(rs.getString("Name"));
                    }
                    
                    rs.close();
                    stmt.close();
                    loadDistributionList();
                }
                catch (SQLException e)
                {
                    throw new ServletException (e);
                }
            }
            catch (Exception e)
            {
                
            }

        	
        }
        
        public int getEmployeeId() 
        {  
        	int employeeId = 0;
        
            try
            {
            	employeeId = findEmployeeId();
            }
            catch (Exception e)
            {
            	System.out.println( "Enable to read employee id for user " + getUserId());
            }

        	return employeeId;
        }
        public String getEmployeeName() 
        {
        	//2010.03.29 
//        	if ( this.employeeName == null )
//        	{
        		MUser User = MUser.get( Env.getCtx(), this.getUserId() );
        		return User.getName(); 
//        	}
//        	return this.employeeName;
    	}
        public String getEmployeeValue() {return this.employeeValue;}
        public boolean hasPolicy(String key) 
        {
        	 // SuperUser ont tout les privilèges.
        	
        	 if ( getUserId() >= 100 && getUserId() < 1000)
        		 return true;
        	 
        	 return this.policies.contains(key);
        }

        /**
         * Constructuer. On initialise la classe à partir de userId
         */
        public MyUserInfo(int userId) throws ServletException
        {
            this.userId = userId;
            this.policies = new Vector<String>();
            
            // On doit maintenant obtenir le numéro de l'employé correspondant au userId
            if(this.findEmployeeId() > 0)
            {
                // On charge ensuite les politiques de sécurités applicables aux groupes
                // associé à l'employé trouvé. On utilise des politiques de sécurité
                // optimiste, donc il suffit qu'une permission se retrouve à un seul endroit
                // pour qu'elle soit appliquable.
                try
                {
                    PreparedStatement stmt = DB.prepareStatement(
                            "select distinct P_Security.Name" +
                            "  from P_Security," +
                            "       P_Group_Security" +
                            " where P_Security.P_Security_ID = P_Group_Security.P_Security_ID" +
                            "   and exists ( select 1" +
                            "                  from P_Group_Employee" +
                            "                 where P_Group_ID = P_Group_Security.P_Group_ID" +
                            "                   and P_Group_Employee.isActive = 'Y' " +
                            "                   and P_Employee_ID = " + getEmployeeId() + " )", null);
                    
                    ResultSet rs = stmt.executeQuery();
                    
                    // On ajoute maintenant les politiques appliquables à la liste
                    while(rs.next())
                    {
                        this.policies.add(rs.getString("Name"));
                    }
                    
                    rs.close();
                    stmt.close();
                    loadDistributionList();
                }
                catch (SQLException e)
                {
                    throw new ServletException (e);
                }
            }
        }
        
        /**
         * Cette méthode retourne l'id de l'employé associé à l'utilisateur. S'il n'y
         * en a aucun, on retourne 0;
         */
        private int findEmployeeId() throws ServletException
        {
        		
            try
            {
                int employeeId = 0;
            	if ( getUserId() < 1000 )
                {
                    employeeId = Integer.parseInt(PgiUtil.getSolsticeParameter(Env.getCtx(), "SSOESuperUserEmployee"));
                    this.employeeName = "Progestion Information INC";
                    this.employeeValue = "0000";
                    return employeeId;
               }

            	PreparedStatement stmt = DB.prepareStatement(
                        "select P_Employee_ID, Value, Name" +
                        "  from P_Employee" +
                        " where AD_User_ID = " + getUserId(), null);
                ResultSet rs = stmt.executeQuery();
                
                if(rs.next())
                {
                    employeeId = rs.getInt("P_Employee_ID");
                    this.employeeName = rs.getString("Name");
                    this.employeeValue = rs.getString("Value");
               }
                
                rs.close();
                stmt.close();
                
                return employeeId;
            }
            catch (SQLException e)
            {
                throw new ServletException(e);
            }
        }
        
        /*
         * On charge la liste des distributions de l'employé
         */
        private void loadDistributionList() throws ServletException
        {
            
            // On charge les distributions
            String sql = "select P_Distribution_Booklet_ID from P_Distribution_Booklet where ( P_Employee_ID = " + getEmployeeId() 
                + " or P_Employee_Secretary_ID = " + getEmployeeId() + " or P_Employee_Coordinating_ID = " + getEmployeeId() + ")";
            
            // On ajoute les distributions qui peuvent être accedées si l'employé remplace en date d'aujourd'hui
            sql += " union "
            	+ " select P_Distribution_Booklet_ID from P_distribution_booklet_Replacement " 
            	+ " where P_Employee_ID = " + getEmployeeId()
            	+ " and ReplacementType in ('S','N','C') "
            	+ " and startdate <= getDate() " 
            	+ " and (enddate >= getDate() or enddate is null)";

            try
            {
                PreparedStatement stmt = DB.prepareStatement(sql, null);
                ResultSet rs = stmt.executeQuery();
                Vector<Integer> distributions = new Vector<Integer>();
                
                while(rs.next())
                {
                    distributions.add(new Integer(rs.getInt("P_Distribution_Booklet_ID")));
                }
                
                rs.close();
                stmt.close();
                
                this.distributionList = new int[distributions.size()];
                for(int i = 0; i < distributions.size(); i++)
                    this.distributionList[i] = ((Integer)distributions.elementAt(i)).intValue();
                distributions.clear();
            }
            catch (SQLException e)
            {
                throw new ServletException(e);
            }
        }

        
        /**
         * Renvoit un select retournant tous les codes de distribution
         */
        public String getDistributionSQL(String[] selectedFields, boolean orderByValue)
        {
            // On doit d'abord vérifier si l'employé à accès à tous les codes de distribution
            String where = "";
            boolean peutToutVoir = hasPolicy("seeAllDistributionAndEmployees");
            
            if(!peutToutVoir)
            {
            	
            	where = "where ( P_Employee_ID = " + getEmployeeId() 
            	      + "        or P_Employee_Secretary_ID = " + getEmployeeId() 
            	      + "        or P_Employee_Coordinating_ID = " + getEmployeeId() + ")"
            	      + " or   ( P_Distribution_Booklet_id in (select P_Distribution_Booklet_id "
            	      + "           from P_Distribution_Booklet_Replacement "
            	      + "          where P_Employee_ID = " + getEmployeeId()
            	      + "            and ReplacementType in ('S','N','C') "
                      + "            and startdate <= getDate() " 
                      + "            and (enddate >= getDate() or enddate is null)))";
            }

            // On prépare maintenant le select de retour
            return "select " + PgiUtil.join(selectedFields, ", ") + " from P_Distribution_Booklet " + where + (orderByValue ? " order by Value" : "");

        }

        /**
         * Renvoit un select retournant tous les codes de distribution
         */
        public String getDistributionSQLFromER(String[] selectedFields, boolean orderByValue)
        {
            // On doit d'abord vérifier si l'employé à accès à tous les codes de distribution
            String where = "";
            
            boolean peutToutVoir = hasPolicy("seeAllDistributionAndEmployees");
            
            if(!peutToutVoir)
            {
            	where = "where ( P_Employee_Gestionnaire_ID = " + getEmployeeId() + ")"
      	              + " or   ( P_Distribution_Booklet_id in (select P_Distribution_Booklet_id "
      	              + "           from P_Distribution_Booklet_Replacement "
      	              + "          where P_Employee_ID = " + getEmployeeId()
      	              + "            and ReplacementType = 'G' "
                      + "            and startdate <= getDate() " 
                      + "            and (enddate >= getDate() or enddate is null)))";
            }

            // On prépare maintenant le select de retour
            return "select " + PgiUtil.join(selectedFields, ", ") + " from P_Distribution_Booklet " + where + (orderByValue ? " order by Value" : "");
        }

        /**
         * Renvoie un select retournant tous les codes de distribution
         */
        public String getDistributionWebSQL(String[] selectedFields, boolean orderByValue)
        {
            // On doit d'abord vérifier si l'employé à accès à tous les codes de distribution
            String where = "where IsUsingWeb = 'Y' ";
            
            boolean peutToutVoir = hasPolicy("seeAllDistributionAndEmployees");
            
            if(!peutToutVoir)
            {
                
            	where += "and ( ( P_Employee_ID = " + getEmployeeId() 
      	              + "        or P_Employee_Secretary_ID = " + getEmployeeId() 
      	              + "        or P_Employee_Coordinating_ID = " + getEmployeeId() + ")"
      	              + " or   ( P_Distribution_Booklet_id in (select P_Distribution_Booklet_id "
      	              + "           from P_Distribution_Booklet_Replacement "
      	              + "          where P_Employee_ID = " + getEmployeeId()
      	              + "            and ReplacementType in ('S','N','C') "
                      + "            and startdate <= getDate() " 
                      + "            and (enddate >= getDate() or enddate is null))) )";
            }
            
            // On prépare maintenant le select de retour
//			System.out.println("* DEBUG Black Berry * sql " + "select " + PgiUtil.join(selectedFields, ", ") + " from P_Distribution_Booklet " + where + (orderByValue ? " order by Value" : "") );

            return "select " + PgiUtil.join(selectedFields, ", ") + " from P_Distribution_Booklet " + where + (orderByValue ? " order by Value" : "");
        }

        public String getDistributionWebSQLApp(String[] selectedFields, boolean orderByValue)
        {
            // On doit d'abord vérifier si l'employé à accès à tous les codes de distribution
            String where = "where IsUsingWeb = 'Y' ";
            
            boolean peutToutVoir = hasPolicy("seeAllDistributionAndEmployees");
            
            if(!peutToutVoir)
            {
                
            	where += "and ( ( P_Employee_ID = " + getEmployeeId() 
      	              + "        or P_Employee_Secretary_ID = " + getEmployeeId() 
      	              + ")"
      	              + " or   ( P_Distribution_Booklet_id in (select P_Distribution_Booklet_id "
      	              + "           from P_Distribution_Booklet_Replacement "
      	              + "          where P_Employee_ID = " + getEmployeeId()
      	              + "            and ReplacementType in ('S','N') "
                      + "            and startdate <= getDate() " 
                      + "            and (enddate >= getDate() or enddate is null))) )";
            }
            
            // On prépare maintenant le select de retour
//			System.out.println("* DEBUG Black Berry * sql " + "select " + PgiUtil.join(selectedFields, ", ") + " from P_Distribution_Booklet " + where + (orderByValue ? " order by Value" : "") );

            return "select " + PgiUtil.join(selectedFields, ", ") + " from P_Distribution_Booklet " + where + (orderByValue ? " order by Value" : "");
        }

        
        /**
         * Renvoie un select retournant tous les codes de distribution
         */
        public String getDistributionWebSQLAbsence(String[] selectedFields, boolean orderByValue)
        {
            // On doit d'abord vérifier si l'employé à accès à tous les codes de distribution
            String where = "where 1=1 ";
            
            boolean peutToutVoir = hasPolicy("seeAllDistributionAndEmployees");
            
            if(!peutToutVoir)
            {
                
            	where += "and ( ( P_Employee_ID = " + getEmployeeId() 
      	              + "        or P_Employee_Secretary_ID = " + getEmployeeId() 
      	              + "        or P_Employee_Coordinating_ID = " + getEmployeeId() + ")"
      	              + " or   ( P_Distribution_Booklet_id in (select P_Distribution_Booklet_id "
      	              + "           from P_Distribution_Booklet_Replacement "
      	              + "          where P_Employee_ID = " + getEmployeeId()
      	              + "            and ReplacementType in ('S','N','C') "
                      + "            and startdate <= getDate() " 
                      + "            and (enddate >= getDate() or enddate is null))) )";
            }
            
            // On prépare maintenant le select de retour
//			System.out.println("* DEBUG Black Berry * sql " + "select " + PgiUtil.join(selectedFields, ", ") + " from P_Distribution_Booklet " + where + (orderByValue ? " order by Value" : "") );

            return "select " + PgiUtil.join(selectedFields, ", ") + " from P_Distribution_Booklet " + where + (orderByValue ? " order by Value" : "");
        }
        
        /**
         * Cette méthode retourne la liste des employés que l'employé courrant peut consulter.
         * P_Employee_ID, ltrim(rtrim(Name)) + ' (' + ltrim(rtrim(Value)) + ')'
         * 
         * Cette requète est bâtie en fonction du role
         */
        public String getEmployeeSQL()
        {
            String sql = null;
            
            // Si l'utilisateur possède la politique de sécurité nécessaire,
        	//on charge tous employés 
            if(hasPolicy("seeAllDistributionAndEmployees"))
            {
                sql = "select P_Employee_ID, ltrim(rtrim(Name)) + ' (' + ltrim(rtrim(Value)) + ')'"
                + " from P_Employee"
                + " where IsActive = 'Y'"
                + " ";
            }
            // Si il possède une politique un peu plus restrictive,
            //on charge les employés en relation
            else if((hasPolicy("seeRelatedDistributionAndEmployees")) && this.distributionList.length > 0)
            {
                String distributions = "(";
                for(int i = 0; i < this.distributionList.length - 1; i++)
                {
                    distributions += this.distributionList[i] + ", ";
                }
                distributions += this.distributionList[this.distributionList.length - 1] + ")";
                
                sql = "select P_Employee_ID, ltrim(rtrim(Name)) + ' (' + ltrim(rtrim(Value)) + ')'"
                + " from P_Employee"
                // + " where P_Distribution_Booklet_ID in " + distributions
                + " where dbo.fn_ReturnCurrentDistribution( P_Employee.P_Employee_ID, getDate() ) in " + distributions
                + " and IsActive = 'Y'"
                + " ";
            }
            
            else
            {
                // Dans tous les autres cas, on n'affiche que l'employé courrant dans la requète
        		sql = "select P_Employee_ID, ltrim(rtrim(Name)) + ' (' + ltrim(rtrim(Value)) + ')'"
                + " from P_Employee"
                + " where P_Employee_ID = " + getEmployeeId()
                + " and IsActive = 'Y'";
            	
            }

    		MUser User = MUser.get( Env.getCtx(), this.getUserId() );
    		if ( User.isDistributionBookletRestricted() )
    		{
    			sql += " AND P_Employee.P_Distribution_Booklet_ID in ( " 
    			    + " select p_distribution_booklet.p_distribution_booklet_id "  
    			    + " from p_distribution_booklet, p_employee " 
    			    + " where (p_distribution_booklet.p_employee_id = p_employee.p_employee_id " 
    			    + "  or p_distribution_booklet.p_employee_gestionnaire_id = p_employee.p_employee_id  " 
    			    + "  or p_distribution_booklet.p_employee_Coordinating_id = p_employee.p_employee_id ) " 
    			    + " and  p_employee.ad_user_id = " + User.getAD_User_ID() 
    			    + " )" ;
    		}

    		sql += " order by Name ";

            return sql;
        }

        /**
         * Cette méthode retourne la liste des employés que l'employé courrant peut consulter.
         * P_Employee_ID, ltrim(rtrim(Name)) + ' (' + ltrim(rtrim(Value)) + ')'
         * 
         * Cette requète est bâtie en fonction du role
         */
        public String getEmployeeSQL(String additionalCriteria)
        {
        	String sql = null;
        	
            if(additionalCriteria != null && !"".equals(additionalCriteria.trim()))
                additionalCriteria = " and " + additionalCriteria;
            else
                additionalCriteria = "";
            
            // Si l'utilisateur possède la politique de sécurité nécessaire,
            //on charge tous employés 
            if(hasPolicy("seeAllDistributionAndEmployees"))
            {
                sql = "select P_Employee_ID, ltrim(rtrim(Name)) + ' (' + ltrim(rtrim(Value)) + ')'"
                + " from P_Employee"
                + " where IsActive = 'Y'" + additionalCriteria;
            }
            // Si il possède une politique un peu plus restrictive,
            //on charge les employés en relation
            else if((hasPolicy("seeRelatedDistributionAndEmployees")) && this.distributionList.length > 0)
            {
                String distributions = "(";
                for(int i = 0; i < this.distributionList.length - 1; i++)
                {
                    distributions += this.distributionList[i] + ", ";
                }
                distributions += this.distributionList[this.distributionList.length - 1] + ")";
                
                sql = "select P_Employee_ID, ltrim(rtrim(Name)) + ' (' + ltrim(rtrim(Value)) + ')'"
                + " from P_Employee"
                + " where P_Distribution_Booklet_ID in " + distributions
                + " and IsActive = 'Y'" + additionalCriteria;
            }
            
            // Dans tous les autres cas, on n'affiche que l'employé courrant dans la requète
            sql = "select P_Employee_ID, ltrim(rtrim(Name)) + ' (' + ltrim(rtrim(Value)) + ')'"
            + " from P_Employee"
            + " where P_Employee_ID = " + getEmployeeId()
            + " and IsActive = 'Y'" + additionalCriteria;

    		MUser User = MUser.get( Env.getCtx(), this.getUserId() );
    		if ( User.isDistributionBookletRestricted() )
    		{
    			sql += " AND P_Employee.P_Distribution_Booklet_ID in ( " 
    			    + " select p_distribution_booklet.p_distribution_booklet_id "  
    			    + " from p_distribution_booklet, p_employee " 
    			    + " where (p_distribution_booklet.p_employee_id = p_employee.p_employee_id " 
    			    + "  or p_distribution_booklet.p_employee_gestionnaire_id = p_employee.p_employee_id  " 
    			    + "  or p_distribution_booklet.p_employee_Coordinating_id = p_employee.p_employee_id ) " 
    			    + " and  p_employee.ad_user_id = " + User.getAD_User_ID() 
    			    + " )" ;
    		}

    		sql += " order by Name ";

    		return sql;
        }

	}

    private int AD_Client_ID = 0;

    /**
     * Lorsqu'on arrive dans la page en get, on doit vérifier, dans les paramètres web, si on
     * doit afficher la fenêtre de login. Sinon, on renvoit une erreur 403.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // On récupère d'abord le paramètre indiquant si on affiche le login
        String sql;
        boolean showLogin;
        sql = " select AD_Client_ID"
            + "   from P_Web_Parameters"
            + "  where [Parameter] = 'SHOWLOGIN'"
            + "    and [Value] = 'true'";
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            showLogin = rs.next();
            AD_Client_ID = rs.getInt( "AD_Client_ID");
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            throw new ServletException(e);
        }

    	String webAppUrl = PgiUtil.getSolsticeParameter(Env.getCtx(), "WEBAPPURL");


        if ( request.getRemoteUser() != null )
        {
            if ( request.getRequestURL().toString().contains( "8099") )
            {
            	System.out.println( "Connection a l'application par ancienne URL : " + request.getRequestURL() );
            	response.sendRedirect( webAppUrl + "index.jsp");
            }

        	// On récupère d'abord les informations posté pour le login
        	System.out.println( "Connection de l'usager : " + request.getRemoteUser());
        	
        	String user = "";
        	
        	String Domaine = PgiUtil.getSolsticeParameter(Env.getCtx(), "SSODomaine");
        	String SSOSuperUser = PgiUtil.getSolsticeParameter(Env.getCtx(), "SSOSuperUser");
        	String SSOSuperUser2 = PgiUtil.getSolsticeParameter(Env.getCtx(), "SSOSuperUser2");
        	String SSOSuperUser3 = PgiUtil.getSolsticeParameter(Env.getCtx(), "SSOSuperUser3");
        	String SSOSuperUser4 = PgiUtil.getSolsticeParameter(Env.getCtx(), "SSOSuperUser4");
        	String SSOSuperUser5 = PgiUtil.getSolsticeParameter(Env.getCtx(), "SSOSuperUser5");
        	
        	if ( request.getRemoteUser().toUpperCase().contains( Domaine ) )
        	{
        		user = request.getRemoteUser().replace( Domaine + "\\", "");
        		user = user.replace( Domaine.toLowerCase() + "\\", "");
        		user = user.replace( "\\", "");
        	}
        		
        	System.out.println( "Usager sans domaine : " + user );

       	
        	if (   user.equals( SSOSuperUser ) 
        		|| user.equals( SSOSuperUser2 ) 
        		|| user.equals( SSOSuperUser3 ) 
        		|| user.equals( SSOSuperUser4 ) 
        		|| user.equals( SSOSuperUser5  ))
            {
            	user = "SuperUser";
            	System.out.println( "Transfert à usager : " + user);
            }
        	
            // On vérifie s'il s'agit d'un login valide
            int userId = this.validLogin(user);
            if(userId > 0)
            {
                // Le mot de passe est valide, on crée alors les informations sur l'utilisateur
                // et on le renvoit à la page principale de l'application
                IUserInfo userInfo = new MyUserInfo(userId);
                HttpSession session = request.getSession();
                session.setMaxInactiveInterval(2*60*60); // two hours
                session.setAttribute("userInfo", userInfo);
                
                if(request.getParameter("redirection") != null
                        && !request.getParameter("redirection").equals(""))
                {
                    System.out.println(request.getParameter("redirection"));
                    response.sendRedirect(request.getParameter("redirection"));
                }
                else
                {
                    response.sendRedirect( webAppUrl + "index.jsp");
                }
                return;
            }
            else
            {
                // Le mot de passe et/ou le nom d'utilisateur sont mauvais
                response.sendError(401);
            }
        }
        else
        {
            // On affiche le login selon le résultat de la requête
            if(showLogin)
            {
                this.prepareLogin(
                        response.getWriter(),
                        request.getParameter("user"),
                        request.getParameter("redirection"));


            }
            else
            {
                response.sendError(403);
            }
        }
    }
    
    /**
     * Lorsqu'on arrive en post, on doit valider le nom d'usager et le password puis créer les
     * informations d'utilisateur qu'on placera en session sous l'attribute "userInfo"
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // On récupère d'abord les informations posté pour le login
   	
        String user = request.getParameter("user");
        String password = request.getParameter("password");
        
        int userId;
        // On vérifie ensuite s'il s'agit d'un login valide
        userId = this.validLogin(user, password);

        if(userId > 0)
        {
            // Le mot de passe est valide, on crée alors les informations sur l'utilisateur
            // et on le renvoit à la page principale de l'application
            IUserInfo userInfo = new MyUserInfo(userId);
            HttpSession session = request.getSession();
            session.setAttribute("userInfo", userInfo);
            if(request.getParameter("redirection") != null
                    && !request.getParameter("redirection").equals(""))
            {
                System.out.println(request.getParameter("redirection"));
                response.sendRedirect(request.getParameter("redirection"));
            }
            else
            {
                String webAppUrl = PgiUtil.getSolsticeParameter(Env.getCtx(), "WebAppURL");
                response.sendRedirect(webAppUrl + "/index.jsp");
            }
        }
        else
        {
            // Le mot de passe et/ou le nom d'utilisateur sont mauvais
            response.sendError(401);
//            response.sendError(555);
        }
    }
    
    /**
     * Création des textbox pour la saisie du login
     */
    private void prepareLogin(PrintWriter out, String user, String redirection) throws IOException
    {
        this.drawHeader(out, (user == null ? "user" : "password"), redirection);
        // On dessine les textBox d'utilisateur et de mot de passe
        out.println("<tr>");
        out.println("<td class=\"tdlabel\" align=\"right\">Utilisateur</td>");
        out.println("<td class=\"tdfield\"><input type=\"text\" name=\"user\" value=\"" + (user == null ? "" : user) + "\"></td>");
        out.println("<td>&nbsp;</td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<td class=\"tdlabel\" align=\"right\">Mot de passe</td>");
        out.println("<td class=\"tdfield\"><input type=\"password\" name=\"password\"></td>");
        out.println("<td class=\"tdfield\"><input type=\"submit\" value=\"OK\"></td>");
        out.println("</tr>");
        this.drawFooter(out);
    }

    /**
     * Dessine la première partie du document
     */
    private void drawHeader(PrintWriter out, String activeControl, String redirection) throws IOException
    {
		out.println("<html>");

    	String webLogoHeader1 = PgiUtil.getSolsticeParameter(Env.getCtx(), "webLogoHeader1");
		String webLogoHeader2 = PgiUtil.getSolsticeParameter(Env.getCtx(), "webLogoHeader2");
        String client = PgiUtil.getSolsticeParameter(Env.getCtx(),"Client").toUpperCase();
        if ( client.equals("SOQUIJ") )
        {
        
        		out.println( "<head> " );
        			out.println( "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\r\n" );
        			out.println( "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\r\n" );

					out.println( " 	<meta name=\"vs_targetSchema\" content=\"http://schemas.microsoft.com/intellisense/ie5\">" );
					out.println( "   <link rel=\"stylesheet\" type=\"text/css\" media=\"all\" href=\"solstice.css\">" );
					out.println( " 		<link rel=\"stylesheet\" type=\"text/css\" href=\"styles/siq.css\">" );
					out.println( " 		<title>Solstice Menu principal</title>" );
					out.println( "<link rel=\"stylesheet\" type=\"text/css\" media=\"all\" href=\"https://soquij.qc.ca/css/principal.css?v=3\" />     " );
					out.println( "<link rel=\"stylesheet\" type=\"text/css\" media=\"all\" href=\"https://soquij.qc.ca/css/thickbox.css\" />          " );
					out.println( "<link rel=\"stylesheet\" type=\"text/css\" media=\"all\" href=\"https://soquij.qc.ca/css/corpo.css\" />             " );
					out.println(   "                                                                                                                   " );
					out.println( "<!--[if IE]>                                                                                                       " );
					out.println( "	<link rel=\"stylesheet\" type=\"text/css\" media=\"screen\" href=\"https://soquij.qc.ca/css/principal_ie.css\" />  " );
					out.println( "<![endif]-->                                                                                                       " );
					out.println( "                                                                                                                   " );
					out.println( "<!--[if IE 6]>                                                                                                     " );
					out.println( "	<script type=\"text/javascript\" src=\"/js/iepngfix_tilebg.js\" media=\"screen\"></script>                        " );
					out.println( "	<link rel=\"stylesheet\" type=\"text/css\" media=\"screen\" href=\"https://soquij.qc.ca/css/principal_ie6.css\" /> " );
					out.println( "<![endif]-->                                                                                                       " );
					out.println( "                                                                                                                   " );
					out.println( "<!--[if IE 8]>                                                                                                     " );
					out.println( "	<style type=\"text/css\">                                                                                         " );
					out.println( "		#sous_navig_catalogue ul ul li {                                                                              " );
					out.println( "			display:inline-block;                                                                                     " );
					out.println( "		}                                                                                                             " );
					out.println( "						</style>                                                                                      "           );
					out.println( "	<![endif]-->                                                                                                      " );
					out.println( " ");
					out.println( "<link rel=\"stylesheet\" type=\"text/css\" media=\"print\" href=\"https://soquij.qc.ca/css/print.css\" />			  " );
					out.println( "<script type=\"text/javascript\">" );
					out.println( "		var section_id = 1;" );
					out.println( "		var langue = 'fr';" );
					out.println( "		var no_focus_on_load = false;" );
					out.println( "	</script>" );
					out.println( "	<style>" );
					out.println( "		/*li {line-height: 1.2}" );
					out.println( "		label {display:inline;font-size: 12px;}" );
					out.println( "		#login_azimut_rapide input[type=text] {" );
					out.println( "			padding-top: 0px;" );
					out.println( "			padding-bottom: 0px;" );
					out.println( "		}" );
					out.println( "		.contenu_padding .bloc_deroulant {" );
					out.println( "			margin: 0;" );
					out.println( "		}	*/		" );
					out.println( "	</style>" );
					out.println( "" );
					out.println( "		<script src=\"//ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js\"></script>" );
					out.println( "		<script type=\"text/javascript\" src=\"https://soquij.qc.ca/js/bootstrap.min.js\"></script>" );
					out.println( "	<script type=\"text/javascript\" src=\"https://soquij.qc.ca/js/jquery-cookie.js\"></script>" );
					out.println( "	<script type=\"text/javascript\" src=\"https://soquij.qc.ca/js/taille_police.js\"></script>" );
					out.println( "	<script type=\"text/javascript\" src=\"https://soquij.qc.ca/js/swfobject.js\"></script>" );
					out.println( "	<script type=\"text/javascript\" src=\"https://soquij.qc.ca/js/thickbox.js\"></script>" );
					out.println( "	<script type=\"text/javascript\" src=\"https://soquij.qc.ca/js/fonctions.js?v=3\"></script>" );
					out.println( "" );
					out.println( "	" );
					out.println( "	<link rel=\"shortcut icon\" href=\"https://soquij.qc.ca/favicon.ico?v=3\" />" );
					out.println( "" );
					out.println( "			<link rel=\"alternate\" type=\"application/rss+xml\" title=\"SOQUIJ - Nouvelles et communiqués\" href=\"https://soquij.qc.ca/fr/fils-rss/nouvelles-et-communiques.xml\" />" );
					out.println( "" );
					out.println( "<script type=\"text/javascript\">" );
					out.println( "" );
					out.println( "	var _gaq = _gaq || [];" );
					out.println( "	_gaq.push(['_setAccount', 'UA-3760448-1']);" );
					out.println( "	_gaq.push(['_setDomainName', '.soquij.qc.ca']);" );
					out.println( "			_gaq.push(['_trackPageview']);" );
					out.println( "	" );
					out.println( "  (function() {" );
					out.println( "    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;" );
					out.println( "    ga.src = ('https:' == document.location.protocol ? 'https://' : 'http://') + 'stats.g.doubleclick.net/dc.js';" );
					out.println( "    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);" );
					out.println( "  })();" );
					out.println( "" );
					out.println( "" );
					out.println( "</script>" );
					out.println( "		<title>>Authentification</title>" );
					out.println( "	</head>" );
					out.println( "	<body>" );
					out.println( "		<div id=\"fond_site\">" );
					out.println( "			<div id=\"wrapper\">" );
					out.println( "			<div id=\"utilitaires\">" );
					out.println( "						" );
					out.println( "			<ul>" );
					out.println( "					" );
					out.println( "					<li style=\"display:inline;\"><a href=\"https://soquij.qc.ca/fr/nous-joindre\" >Nous joindre</a>&nbsp;<strong>|</strong>&nbsp;</li>" );
					out.println( "					<li style=\"display:inline;\"><a href=\"https://soquij.qc.ca/fr/english\" >English</a>&nbsp;<strong>|</strong>&nbsp;</li>" );
					out.println( "				</ul>" );
					out.println( "	" );
					out.println( "					" );
					out.println( "					<span id=\"taille_police\">" );
					out.println( "						<a href=\"#\" id=\"augmenter\"><img src=\"https://soquij.qc.ca/images/ul/graphiques/police_plus.gif\" width=\"9\" height=\"9\" alt=\"Agrandir la police\" title=\"Agrandir la police\" /></a>&nbsp; A &nbsp;<a href=\"#\" id=\"diminuer\"><img src=\"https://soquij.qc.ca/images/ul/graphiques/police_moins.gif\" width=\"9\" height=\"9\" alt=\"Diminuer la police\" title=\"Diminuer la police\" /></a>" );
					out.println( "					</span>" );
					out.println( "					" );
					out.println( "					<form method=\"get\" class=\"form_recherche\" action=\"https://soquij.qc.ca/fr/recherche\">						<input type=\"text\" id=\"mots_cles\" class=\"mots_cles\" value=\"Recherche\" />" );
					out.println( "						<input type=\"image\" class=\"btn_recherche_rapide\" src=\"https://soquij.qc.ca/images/soquij2013/fleche_recherche_top.gif\" />" );
					out.println( "					</form>" );
					out.println( "				</div>" );
					out.println( "				" );
					out.println( "				<div id=\"entete\">" );
					out.println( "		<div id=\"slogan\"><img src=\"https://soquij.qc.ca/images/fr/titrages/slogan.gif\" width=\"93\" height=\"31\" alt=\"Complice de vos succès\" /></div>" );
					out.println( "<h1><a href=\"https://soquij.qc.ca/fr\"><img src=\"https://soquij.qc.ca/images/soquij2013/logo2013.png\" width=\"352\" height=\"54\" alt=\"Société québécoise d'information juridique\" title=\"Société québécoise d'information juridique\" /></a></h1>											" );
					out.println( "<ul id=\"navig_corpo\">" );
					out.println( "			<li style=\"display:inline;\"><a href=\"https://soquij.qc.ca/fr\">>Authentification</a></li>" );
					out.println( "	</ul>" );
					out.println( "	<!-- Médias sociaux -->" );
					out.println( "	<div id=\"medias_sociaux\">" );
					out.println( "		<a href=\"http://facebook.com/soquij\" name=\"Facebook\" target=\"_blank\"><img src=\"https://soquij.qc.ca/images/ul/icones/facebook.png\" width=\"16\" height=\"16\" alt=\"Facebook. S'ouvrira dans une nouvelle fenêtre.\" title=\"Facebook\" /></a>" );
					out.println( "		<a href=\"http://twitter.com/soquij\" name=\"Twitter\" target=\"_blank\"><img src=\"https://soquij.qc.ca/images/ul/icones/twitter.png\" width=\"16\" height=\"16\" alt=\"Twitter. S'ouvrira dans une nouvelle fenêtre.\" title=\"Twitter\" /></a>" );
					out.println( "		<a href=\"http://www.linkedin.com/company/soquij\" name=\"LinkedIn\" target=\"_blank\"><img src=\"https://soquij.qc.ca/images/ul/icones/linkedin.png\" width=\"16\" height=\"16\" alt=\"LinkedIn. S'ouvrira dans une nouvelle fenêtre.\" title=\"LinkedIn\" /></a>" );
					out.println( "	</div>" );
					out.println( "	<script type=\"text/javascript\">" );
					out.println( "		$('#medias_sociaux a').click(function(){" );
					out.println( "			if (_gaq) {" );
					out.println( "				_gaq.push(['_trackEvent', 'Social', document.title.split(' - ')[0], $(this).attr('name')]);" );
					out.println( "			}" );
					out.println( "		});" );
					out.println( "	</script>" );
					out.println( "" );
					out.println( "</div>"  );
					out.println( "<div id=\"colonne_principale\">" );
					out.println( "<div id=\"conteneur_colonnes\">" );

        }
        else
        {
 		   out.println("   <head>");
           out.println("       <meta name=\"vs_targetSchema\" content=\"http://schemas.microsoft.com/intellisense/ie5\">");
           out.println("       <link rel=\"stylesheet\" type=\"text/css\" href=\"/siq.css\">");
           out.println("       <title>Authentification</title>");
           out.println("   </head>");
           out.println("   <body onLoad=\"document.mainForm." + activeControl + ".focus();\">");
           out.println("       <table width=\"900\" align=\"center\" border=\"0\" cellspacing=\"2\" cellpadding=\"0\">");
           out.println("           <tr>");
           out.println("               <td align=\"center\" valign=\"middle\">");
           out.println("                   <img src=\"" + webLogoHeader1 + "\" width=\"211\" height=\"100\" border=\"0\">");
           out.println("               </td>");
           out.println("               <td align=\"center\" valign=\"middle\">");
           out.println("                   <img src=\"" + webLogoHeader2 + "\" width=\"450\" height=\"57\" border=\"0\">");
           out.println("               </td>");
           out.println("           </tr>");
           out.println("           <tr>");
           out.println("               <td colspan=\"2\" align=\"center\" valign=\"middle\">");
           out.println("                   <div class=\"apptitle\">Authentification</div>");
           out.println("               </td>");
           out.println("           </tr>");
           out.println("           <tr>");
           out.println("               <td colspan=\"2\" class=\"line\"></td>");
           out.println("           </tr>");
           out.println("           <tr>");
           out.println("               <td colspan=\"2\">");
           out.println("                  <br>");
        	
        }
        	
        out.println("                  <form name=\"mainForm\" method=\"post\">");
        out.println("                     <input type=\"hidden\" name=\"redirection\" value=\"" + (redirection == null ? "" : redirection) + "\">");
        out.println("                     <table border=\"0\" align=\"center\" cellspacing=\"1\" cellpadding=\"1\">");

    	

    }
    
    /**
     * Dessine la fin du document
     */
    private void drawFooter(PrintWriter out) throws IOException
    {
    	MClient client = MClient.get(Env.getCtx());
    	
            out.println("                     </table>");
            out.println("                  </form>");
            out.println("               </td>");
            out.println("           </tr>");
            out.println("           <tr>");
            out.println("               <td colspan=\"2\">");
            out.println("                   <div style=\"padding-top: 20px\" class=\"siqname\">" + client.getDescription() +"</div>");
            out.println("               </td>");
            out.println("           </tr>");
            out.println("           <tr>");
            out.println("               <td colspan=\"2\" class=\"line\"> </td>");
            out.println("           </tr>");
            out.println("       </table>");
            
            String s_client = PgiUtil.getSolsticeParameter(Env.getCtx(),"Client").toUpperCase();
            if ( s_client.equals("SOQUIJ") )
            {
            	out.println("       </div></div></div>");
                out.println("       </table>");
            }
            
            out.println("   </body>");
            out.println("</html>");
    		
    }

    /**
     * Valide le nom et le mot de passe de l'utilisateur. Dans le cas où ces parametres sont bons,
     * la méthode retourne le id de l'utilisateur. Sinon, elle retourne -1.
     */
    private int validLogin(String user, String password) throws ServletException
    {
        // On permet de modifier le password
        resetPassword( user, password );
        String sql
        = "select AD_User_ID"
            + " from AD_User"
            + " where Value = ? and Password in ( ?, ? )"
            + " and isActive = 'Y' ";

        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            stmt.setString( 1, user );
            stmt.setString( 2, password );
            stmt.setString( 3, SecureEngine.hashPassword(password) );
            
            ResultSet rs = stmt.executeQuery();
            
            // Si la requète a retourné un enregistrement, le login est valide et on concerve le id
            int userId = -1;
            if ( rs.next() )
            // Si la requète a retourné un enregistrement, le login est valide et on concerve le id
            	userId = rs.getInt("AD_User_ID");
            else
            	userId = -1;
            
            rs.close();
            stmt.close();

            return userId;
        }
        catch(SQLException e)
        {
            throw new ServletException(e);
        }
    }

    /**
     * Valide le nom et le mot de passe de l'utilisateur. Dans le cas où ces parametres sont bons,
     * la méthode retourne le id de l'utilisateur. Sinon, elle retourne -1.
     */
    private int validLogin(String user) throws ServletException
    {
        String sql
        = "select AD_User_ID"
            + " from AD_User"
            + " where Value = ? "
            + " AND IsActive = 'Y' ";
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            stmt.setString( 1, user );
            ResultSet rs = stmt.executeQuery();
            int userId = -1;
            if ( rs.next() )
            // Si la requète a retourné un enregistrement, le login est valide et on concerve le id
            	userId = rs.getInt("AD_User_ID");
            else
            	userId = -1;
            
            rs.close();
            stmt.close();
            
            return userId;
        }
        catch(SQLException e)
        {
            throw new ServletException(e);
        }
    }

    // Tour de passe-passe pour reseter le password... 
    private void resetPassword( String user, String password ) throws ServletException {
        final String PASSWORD_POUR_RESET_AUTOMAGIQUE = "resetpassword123!";
        PreparedStatement statm = null;
        ResultSet rs = null;
        try {
            statm = DB.prepareStatement( "SELECT AD_User_ID FROM AD_User WHERE VALUE = ? AND PASSWORD IN ( ?, ? ) ", null);
            statm.setString( 1, user );
            statm.setString( 2, PASSWORD_POUR_RESET_AUTOMAGIQUE );
            statm.setString( 3, SecureEngine.hashPassword(PASSWORD_POUR_RESET_AUTOMAGIQUE) );
            rs = statm.executeQuery();
            if( rs.next() ) {
                int id = rs.getInt(1);
                statm = DB.prepareStatement( "UPDATE AD_User SET PASSWORD = ? WHERE AD_User_ID = ?" , null);
                statm.setString( 1, SecureEngine.hashPassword(password) );
                statm.setInt( 2, id );
                statm.executeUpdate();
            }
        }
        catch( Exception e ) {
            throw new ServletException(e);
        }
    }

}
