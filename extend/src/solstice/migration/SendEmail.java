package solstice.migration;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class SendEmail {
public static void main(String[] args) 
    {    
	/**
	   Outgoing Mail (SMTP) Server
	   requires TLS or SSL: smtp.gmail.com (use authentication)
	   Use Authentication: Yes
	   Port for TLS/STARTTLS: 587
	 */
		final String fromEmail = "support@solsticeplus.com"; //requires valid gmail id
//		final String username = "support@solsticeplus.com"; //requires valid gmail id
//		final String password = "S0lsMx001"; // correct password for gmail id
		final String toEmail = "marc.morissette@solsticeplus.com"; // can be any email id 

		final String from = "support@solsticeplus.com"; //requires valid gmail id
		final String to   = "marc.morissette@solsticeplus.com"; // can be any email id 

//		final String username = "marc.mxsystem@gmail.com";    
//        final String password = "MxAdm2009!"; 
		final String username = "solstice";    
        final String password = "ZXjvdzFsNnpoZzAw"; 
		
		System.out.println("Start");
/*
		Properties props = new Properties();
		props.put("mail.smtp.host", "mail.solsticeplus.com"); //SMTP Host
		props.put("mail.smtp.port", "465"); //TLS Port
		props.put("mail.smtp.auth", "true"); //enable authentication
		props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
		*/
		Properties props = new Properties();
		
//		final String host = "10.0.80.4";
//		props.put("mail.smtp.auth", host);
/*		
		props.put("mail.smtp.host", "mail.solsticeplus.com"); //SMTP Host
		props.put("mail.smtp.port", "465"); //TLS Port
		props.put("mail.smtp.auth", "true"); //enable authentication
		props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
*/		
		props.put("mail.smtp.host", "mail.smtp2go.com"); //SMTP Host
		props.put("mail.smtp.port", "443"); //TLS Port
//		props.put("mail.smtp.port", "587"); //TLS Port
		props.put("mail.smtp.auth", "true"); //enable authentication
		props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
		
		
//		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		

/*		
		//create Authenticator object to pass in Session.getInstance argument
		Authenticator auth = new Authenticator() {
			//override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};
*/		
//		Session session = Session.getInstance(props, auth);

		Session session = Session.getInstance(props, 
		          new javax.mail.Authenticator() { 
		             
		            //override the getPasswordAuthentication method 
		            protected PasswordAuthentication  
		                           getPasswordAuthentication() { 
		                                         
		                return new PasswordAuthentication(username,  
		                                                 password); 
		            } 
		          }); 
		
		  
		EmailUtil.sendEmail(session, toEmail,"Marc M Subject", "TLSEmail Testing Body");
	/*	
		 //compose the message 
		try { 
		    // javax.mail.internet.MimeMessage class is mostly  
		    // used for abstraction. 
		    MimeMessage message = new MimeMessage(session);  
		      
		    // header field of the header. 
		    message.setFrom(new InternetAddress(from)); 
		      
		    message.addRecipient(Message.RecipientType.TO,  
		                          new InternetAddress(to)); 
		    message.setSubject("subject"); 
		    message.setText("Hello, aas is sending email "); 
		  
		    // Send message 
		    Transport.send(message); 
		    System.out.println("Yo it has been sent.."); 
		} 
		catch (MessagingException mex) { 
		    mex.printStackTrace(); 
		} 
*/
	

		
    }
}