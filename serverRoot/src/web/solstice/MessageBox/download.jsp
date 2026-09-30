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
<%@ page import="solstice.model.P_Employee" %>
<%@ page import="org.compiere.model.MUserMail" %>
<%@ page import="java.io.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*" %>
<%@ page import="solstice.utils.PgiUtil" %>
<%@ page import="org.compiere.model.MAttachment" %>

<%@ page extends="solstice.web.DownloadServlet" %>

<%!
    String trxName = null;
    
	public void DownloadFile( )
	{
		String webAppUrl = PgiUtil.getSolsticeParameter(ctx, "WEBAPPURL");

		this.setPageId("Message");
		this.setSecurityCheck( true );

		if ( ! SecurityCheck() )
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
		
		IUserInfo userInfo = (IUserInfo)pageContext.getSession().getAttribute("userInfo");

  		P_Employee Employee = P_Employee.get(ctx, userInfo.getEmployeeId(), trxName);
		String UserMailID  = request.getParameter("UserMailID");

		MUserMail UserMail = new MUserMail( ctx, Integer.parseInt( UserMailID ), trxName);
    	if ( UserMail.getAD_User_ID() != Employee.getAD_User_ID()) // ((IUserInfo)session.getAttribute("userInfo")).getUserId() )
		{
	        try 
			{
		   		response.sendRedirect(webAppUrl + "/MessageBox/MessageBox.jsp?error=400");
		    }
	    	catch (IOException ex)
	    	{
		    	System.out.println("* ERROR * File not found "   );
	    	}   
		}
/*
		String sfile = String.valueOf(Employee.getP_Employee_ID()) + UserMailID;

		if ( request.getParameter("fileName") != null && request.getParameter("fileName").length() != 0);
			sfile = request.getParameter("fileName");

		if ( sfile == null || sfile.equals("null") )
			sfile = String.valueOf(Employee.getP_Employee_ID()) + UserMailID;


		String EXPORT_LOC =  PgiUtil.getSolsticeParameter(ctx, "TaxFormPDFPath");
	   	String fileName = sfile;
	   	*/

	   	String fileName = null;

		MAttachment attachment = UserMail.getAttachment();
	   	if ( attachment != null)
	   	{
			byte[] info = UserMail.getAttachmentData( ".pdf");

			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(info);
			
			String filePath = PgiUtil.getSolsticeParameter(ctx, "ReportOutputPath");
			if ( filePath.endsWith( File.separator ) == false)
				filePath = filePath + File.separator;
			
			fileName = filePath + "tmp_" + UserMail.getAD_UserMail_ID() + ".pdf"; 
			File newFile=new File("fileName");

	        try 
			{

				FileOutputStream fos = new FileOutputStream(newFile);
				int data; 
				while((data=byteArrayInputStream.read())!=-1)
				{ 
					char ch = (char)data;
					fos.write(ch);
				}
				fos.flush();
				fos.close();
		    }
	    	catch (Exception ex)
	    	{
		    	System.out.println("* ERROR * File not found "  + ex.toString() );
	    	}   

	   	}
	   	else
	   	{
	   		fileName = UserMail.getMailPDF();
//		   		fileName = UserMail.getMailPDF2();
	   	}

	   	if ( request.getParameter("p" ).equals("2"))
	   		fileName = "\\\\NTAP2\\PGI\\Lettres\\CE-01-C(08-2013)_Prepress1.pdf";

	    File file = new File(fileName);
	  	if (!file.exists() || !file.isFile() )    
	   	{
	   		System.out.println("* ERROR * File not found " + fileName );
	        
	        try 
			{
		   		response.sendRedirect( webAppUrl + "/MessageBox/MessageBox.jsp?error=401");
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