/*
 * Created on 14 oct. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.custom;

import java.io.IOException;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.compiere.util.DB;
import org.compiere.util.SecureEngine;

/**
 * @author jeacat01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LoginReset extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String username = request.getParameter("username");
	    String password = request.getParameter("password");

	    if( username == null || "".equals(username) ) return;
	    if( password == null || "".equals(password) ) return;
	    
	    PreparedStatement statm = null;
	    try {
            statm = DB.prepareStatement( "UPDATE AD_User SET PASSWORD = ? WHERE VALUE = ?" , null);
            statm.setString( 1, SecureEngine.hashPassword(password) );
            statm.setString( 2, username );
            statm.executeUpdate();
	    }
	    catch( Exception e ) {
			throw new ServletException(e);
	    }
//	    finally {
//	        DB.closeTarget();
//	    }

	    
	}
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        this.doGet(request, response);
    }
}
