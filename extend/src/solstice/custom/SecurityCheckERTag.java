///*
// * Created on 2005-11-02
// */
//package solstice.custom;
//
//import java.io.IOException;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import javax.servlet.jsp.JspException;
//import javax.servlet.jsp.tagext.TagSupport;
//
//import org.compiere.util.DB;
//
///**
// * @author frafor01
// *
// * Check de sécurité pour les formulaires d'évaluation de rendement
// */
//public class SecurityCheckERTag extends TagSupport
//{
//    public SecurityCheckERTag()
//    {
//        super();
//    }
//
//    /**
//     * On doit vérifier si l'utilisateur a accès à la page
//     */
//    public int doStartTag() throws JspException
//    {
//        // On vérifie d'abord si l'utilisateur est logué
//        HttpServletResponse response = (HttpServletResponse)this.pageContext.getResponse();
//        HttpSession session = this.pageContext.getSession();
//        IUserInfo userInfo = (IUserInfo)session.getAttribute("userInfo");
//        if(userInfo != null)
//        {
//            // On doit vérifier s'il s'agit d'un administrateur ou
//            // d'un gestionnaire grâce à la vue
//            if(userInfo.getRoleId() == 1000000
//                    || this.checkGestionnaire(userInfo.getEmployeeId()))
//            {
//                // L'utilisateur a accès à la page
//                return EVAL_BODY_INCLUDE;
//            }
//            // L'utilisateur n'a pas accès à la page
//            try
//            {
//                response.sendError(403);
//            }
//            catch (IOException e)
//            {
//                throw new JspException(e);
//            }
//            return SKIP_BODY;
//        }
//        
//        // L'utilisateur n'est pas encore logué, on le renvoit donc à la page de login
//        try
//        {
//            HttpServletRequest request = (HttpServletRequest)this.pageContext.getRequest();
//            String url = request.getRequestURI();
//            int i = url.indexOf("?");
//            if(i < 0) i = url.length();
//            url = url.substring(0, url.lastIndexOf("/")) + "/login" + url.substring(url.lastIndexOf("/"), i);
//            response.sendRedirect(url + this.getQueryString());
//        }
//        catch (IOException e)
//        {
//            throw new JspException(e);
//        }
//        return SKIP_BODY;
//    }
//    
//    /**
//     * Vérifie si l'employé est un gestionnaire
//     */
//    private boolean checkGestionnaire(int employeeId) throws JspException
//    {
//        String sql
//        = "select 1"
//            + " from RV_Emp_Gestionnaire"
//            + " where P_Employee_ID = " + employeeId;
//        
//        try
//        {
//            PreparedStatement stmt = DB.prepareStatement(sql, null);
//            ResultSet rs = stmt.executeQuery();
//            
//            boolean gestionnaire = rs.next();
//            
//            rs.close();
//            stmt.close();
//            
//            return gestionnaire;
//        }
//        catch(SQLException e)
//        {
//            throw new JspException(e);
//        }
//    }
//    
//    /**
//     * Transforme la query string pour ne pas qu'elle dérange le login
//     */
//    private String getQueryString()
//    {
//        HttpServletRequest request = (HttpServletRequest)this.pageContext.getRequest();
//        if(request.getQueryString() != null)
//        {
//            return "%3F" + request.getQueryString().replaceAll("=", "%3D").replaceAll("&", "%26");
//        }
//        return "";
//    }
//}
