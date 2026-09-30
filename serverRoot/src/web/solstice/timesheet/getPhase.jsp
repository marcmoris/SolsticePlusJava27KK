<%@ page import="java.text.SimpleDateFormat" %>                                                    
<%@ page import="java.util.Enumeration" %>                                                         
<%@ page import="java.math.BigDecimal" %>                                                          
<%@ page import="java.sql.PreparedStatement" %>                                                    
<%@ page import="java.sql.ResultSet" %>                                                            
<%@ page import="java.sql.SQLException" %>                                                         
<%@ page import="java.sql.Timestamp" %>                                                            
<%@ page import="javax.servlet.jsp.PageContext" %>                                                 
<%@ page import="org.compiere.util.DB" %>                                                          
<%@ page import="org.compiere.util.Env" %>                                                         
<%@ page import="solstice.custom.IUserInfo" %>                                                     
<%@ page import="solstice.utils.PgiUtil" %>                                                      
<%@ page import="solstice.web.PageWebSolstice" %>                                                  
<!%@ page extends="solstice.web.PageWebSolstice" %>       
<%                                                                                    
                                                                                      
String project=request.getParameter("project");                                         
response.setContentType("text/html");                                                 
response.setHeader("Cache-Control","no-cache");                                       
try                                                                                   
{String buffer="<select name='phase'><option value='-1'>Pick One</option>";           

String query = "Select c_project_id, name from C_PROJECTPHASE where c_project_id=isnull("+project+",c_project_id) ";                         
System.out.println(query);                                                            

PreparedStatement pstmt = null;
pstmt = DB.prepareStatement (query, null);
ResultSet rs = pstmt.executeQuery ();

  while(rs.next())                                                                    
  {buffer=buffer+"<option value='"+rs.getString(1)+"'>"+rs.getString(2)+"</option>";  
  }                                                                                   
 buffer=buffer+"</select>";                                                           
response.getWriter().println(buffer);                                                 
}                                                                                     
catch(Exception e)                                                                    
{response.getWriter().println(e);                                                     
}                                                                                     
%>                                                                                    