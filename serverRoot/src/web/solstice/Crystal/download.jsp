<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="javax.servlet.jsp.PageContext" %>
<%@ page import="org.compiere.util.DB" %>
<%@ page import="org.compiere.util.Env" %>
<%@ page import="solstice.custom.IUserInfo" %>
<%@ page import="solstice.web.PageWebSolstice" %>
<%@ page import="java.io.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*" %>
<%@ page import="solstice.utils.PgiUtil" %>

<%@ page extends="solstice.web.DownloadServlet" %>

<%!
    String trxName = null;
    
	public void DownloadFile( )
	{
		String webAppUrl = PgiUtil.getSolsticeParameter(ctx, "WEBAPPURL");

		this.setPageId("index");
		this.setSecurityCheck( false );

/*		if ( ! SecurityCheck() )
		{
			System.out.println("* ERROR * Security Check" );
	        try 
			{
			   		response.sendRedirect(webAppUrl + "/MessageBox/MessageBox.jsp?error=400");
		    }
	    	catch (IOException ex)
	    	{
		    	System.out.println("* ERROR * File not found "   );
	    	}   
		
			return ;
		}
*/ 

		IUserInfo userInfo = (IUserInfo)pageContext.getSession().getAttribute("userInfo");

    	String ReportOutputPath = PgiUtil.getSolsticeParameter(Env.getCtx(), "ReportOutputPath"); 

    	if ( ! ReportOutputPath.endsWith( File.separator ))
    		ReportOutputPath = ReportOutputPath + File.separator;

		String AD_Pinstance_ID  = request.getParameter("AD_Pinstance_ID");

//    	String fileName = ReportOutputPath  +  AD_Pinstance_ID + ".pdf" ;
    	
    	String fileName = PgiUtil.getReportFileName( Integer.parseInt( AD_Pinstance_ID ), ".pdf");

    	
	    File file = new File(fileName);
	  	if (!file.exists() || !file.isFile() )    
	   	{
	   		System.out.println("* ERROR * File not found " + fileName );
	        
	        try 
			{
		   		response.sendRedirect( webAppUrl + "/error_401.jsp");
		    }
	    	catch (IOException ex)
	    	{
		    	System.out.println("* ERROR * File not found "   );
	    	}   
	        
			return ;
	   		
	   	}
	
	   	System.out.println("* ERROR * View File : " + fileName );
	
	    //    Send PDF
		try
		{
	        int bufferSize = 8192;
	        int fileLength = (int)file.length();
	        
	        System.out.println("* DEBUG * fileLenght : " + fileLength );
	            //

	        response.setContentType("application/pdf");
	        response.setBufferSize(bufferSize);
	        response.setContentLength(fileLength);
//	        String formName = getFormName( UserMailID );
//	        response.setHeader("Content-Disposition","attachment; filename=\"" + formName + ".pdf\"");
	        response.setHeader("Cache-Control", "no-cache");
	        //
	        //
	        System.out.println("* DEBUG *  Set Header" );
        
	        response.reset();
			response.resetBuffer();	
	        byte[] buffer = new byte[bufferSize];
			FileInputStream inStream = new FileInputStream(file);
			int sizeRead = 0;
	        System.out.println("* DEBUG *  before while" );

	        OutputStream outStream = response.getOutputStream();
			try 
			{

				while ((sizeRead = inStream.read(buffer, 0, buffer.length)) > 0) 
				{
		//  		System.out.println("size:"+sizeRead);
		    		outStream.write(buffer, 0, sizeRead);
				}
			} catch (Exception e) 
			{
				System.out.println("* ERROR * Streaming error "  );
			}

			inStream.close();
			outStream.flush();
			outStream.close();
			
	    }
	    catch (IOException ex)
	    {
	    	System.out.println("* ERROR * Streaming error "  );
	    }
	}
	
	
	
%>