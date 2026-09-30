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

import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import solstice.model.P_Employee;
import solstice.utils.PgiUtil;

import org.compiere.model.MUser;
import org.compiere.model.MClient;
import org.compiere.util.ByteArrayDataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.compiere.util.DB;
import java.sql.Timestamp;
import org.compiere.util.Env;

/**
 * @author frafor01
 *
 * Cette classe simplifie l'envoit de e-mail à un employé
 */
public class EMailSender
{
    
    //private int employeeId;
    private P_Employee employee;
    private Address address;
    private Address emailTo;
    private ArrayList<DataSource> attachments = new ArrayList<DataSource>();
    
    
    public Address getAddress() {
		return address;
	}

	public void setAddress(String adresse) {
		
		
		try {
			this.address = new InternetAddress("adresse");
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setAddress(Address address)
	{
		this.address = address;
	}

	/**
     * Constructeur
     * @param employeeId L'employé à qui le e-mail doit être envoyé
     */
    public EMailSender(int employeeId, Properties ctx) throws Exception
    {
        this(employeeId, null, ctx);
        
    }
    
    public EMailSender(Properties ctx) throws Exception
    {
        
    }

    
    
    /**
     * Constructeur
     * @param employeeId L'employé à qui le e-mail doit être envoyé
     */
    public EMailSender(int employeeId, Address address, Properties ctx ) throws Exception
    {
        super();
        this.employee = P_Employee.get(ctx, employeeId, null);
        String EmailAbsenceModule  = PgiUtil.getSolsticeParameter(ctx, "EmailAbsenceModule");   // "autorisation.absence@siq.gouv.qc.ca" 
        this.address = address == null ? new InternetAddress(EmailAbsenceModule, "Formulaire d'absence autorisée") : address;
    }
    
    /**
     * Cette méthode permet d'ajouter un fichier join au courriel
     * @param attachment Un fichier join chargé en mémoire
     */
    public void addAttachment(DataSource attachment)
    {
    	this.attachments.add(attachment);
    }
    
    
    /**
     * Envoit le e-mail
     */
    public void sendEMail(String subject, String body) throws Exception
    {
        String EmailAbsenceModule  = PgiUtil.getSolsticeParameter(Env.getCtx(), "EmailAbsenceModule");   // "autorisation.absence@siq.gouv.qc.ca" 
        this.address = address == null ? new InternetAddress(EmailAbsenceModule, "Formulaire d'absence autorisée") : address;
        String email = this.employee.getEMail();
        
        // debug
//    	subject = "(envoyé à " + employee.getName() +")" + subject ;


        System.out.println("Email:"+email);
        if(email != null)
        {
	        Properties props = getProperties();
	
	        Session mailSession = Session.getDefaultInstance(props, null);
	        Transport transport = mailSession.getTransport();
	
	        MimeMessage message = new MimeMessage(mailSession);
	        message.setFrom(this.address);
	        message.setSubject(subject);
	        message.setContent(body, "text/html");
	        message.addRecipient(Message.RecipientType.TO,
	             new InternetAddress(email));
	        transport.connect();
	        transport.sendMessage(message,
	            message.getRecipients(Message.RecipientType.TO));
	        transport.close();
        }
    }
    
    public void sendEMail(String email, String subject, String body) throws Exception
    {
        String EmailAbsenceModule  = PgiUtil.getSolsticeParameter(Env.getCtx(), "EmailAbsenceModule");   // "autorisation.absence@siq.gouv.qc.ca" 
        this.address = address == null ? new InternetAddress(EmailAbsenceModule, "Formulaire d'absence autorisée") : address;
        System.out.println("Email:"+email);
        // debug
//    	subject = "(envoyé à email fixe : " + email +")" + subject ;

        if(email != null)
        {
	        Properties props = getProperties();
	
	        Session mailSession = Session.getDefaultInstance(props, null);
	        Transport transport = mailSession.getTransport();
	
	        MimeMessage message = new MimeMessage(mailSession);
	        message.setFrom(this.address);
	        message.setSubject(subject);
	        message.setContent(body, "text/html");
	        message.addRecipient(Message.RecipientType.TO,
	             new InternetAddress(email));
	        transport.connect();
	        transport.sendMessage(message,
	            message.getRecipients(Message.RecipientType.TO));
	        transport.close();
        }
    }

    
    /**
     * Envoie le e-mail
     */
    public void sendEMailWithAttachment(String subject, String body) throws Exception
    {
        String email = this.employee.getEMail();
        
        if(email != null)
        {
	        Properties props = getProperties();
	
	        Session mailSession = Session.getDefaultInstance(props, null);
	        Transport transport = mailSession.getTransport();
	
	        MimeMessage message = new MimeMessage(mailSession);
	        
	        message.setFrom(this.address);
	        message.setSubject(subject);
	        
	        Multipart multiPart = new MimeMultipart();
	        MimeBodyPart bodyPart = new MimeBodyPart();
	        bodyPart.setText(body);
	        bodyPart.addHeaderLine("Content-Type: text/html; charset=\"iso-8859-1\"");
	        bodyPart.addHeaderLine("Content-Transfer-Encoding: quoted-printable");
	        multiPart.addBodyPart(bodyPart);
	        
	        for(int i = 0; i < this.attachments.size(); i++)
	        {
	        	MimeBodyPart attachmentPart = new MimeBodyPart();
	        	DataHandler d = new DataHandler(this.attachments.get(i));
	        	attachmentPart.setFileName(this.attachments.get(i).getName());
	        	attachmentPart.setDataHandler(d);
	        	multiPart.addBodyPart(attachmentPart);
	        }
	        
	        message.setContent(multiPart);
	        message.addRecipient(Message.RecipientType.TO,
	             new InternetAddress(email));
	        transport.connect();
	        transport.sendMessage(message,
	            message.getRecipients(Message.RecipientType.TO));
	        transport.close();
	        
        }
    }
    
    public void sendEMailWithAttachment(String subject, String body, String sendTo ) throws Exception
    {
        String EmailAbsenceModule  = PgiUtil.getSolsticeParameter(Env.getCtx(), "EmailAbsenceModule");   // "autorisation.absence@siq.gouv.qc.ca" 

        this.address = address == null ? new InternetAddress(EmailAbsenceModule, "Formulaire d'absence autorisée") : address;
        String email = sendTo;
        if(email != null)
        {
	        Properties props = getProperties();
	
	        Session mailSession = Session.getDefaultInstance(props, null);
	        Transport transport = mailSession.getTransport();
	
	        MimeMessage message = new MimeMessage(mailSession);
	        
	        message.setFrom(this.address);
	        message.setSubject(subject);
	        
	        Multipart multiPart = new MimeMultipart();
	        MimeBodyPart bodyPart = new MimeBodyPart();
	        bodyPart.setText(body);
	        bodyPart.addHeaderLine("Content-Type: text/html; charset=\"iso-8859-1\"");
	        bodyPart.addHeaderLine("Content-Transfer-Encoding: quoted-printable");
	        multiPart.addBodyPart(bodyPart);
	        
	        for(int i = 0; i < this.attachments.size(); i++)
	        {
	        	MimeBodyPart attachmentPart = new MimeBodyPart();
	        	DataHandler d = new DataHandler(this.attachments.get(i));
	        	attachmentPart.setFileName(this.attachments.get(i).getName());
	        	attachmentPart.setDataHandler(d);
	        	multiPart.addBodyPart(attachmentPart);
	        }
	        
	        message.setContent(multiPart);

	        message.addRecipient(Message.RecipientType.TO,
	             new InternetAddress(email));
	        transport.connect();
	        transport.sendMessage(message,
	            message.getRecipients(Message.RecipientType.TO));
	        transport.close();
	        
        }
    }

    
    /**
     * Crée les propriétés de connection selon les informations
     * de la table AD_Client relatif à l'employé
     */
    private Properties getProperties( ) throws SQLException
    {
//    	MClient Client = MClient.get( this.employee.getCtx(), this.employee.getAD_Client_ID());
    	MClient Client = MClient.get( Env.getCtx(), Env.getAD_Client_ID( Env.getCtx() ));
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", Client.getSmtpHost() );
        return props;
    }


    // 2009.04.17
    P_Employee Manager ;
    
    public void sendEMailSubstituteManager(Properties ctx, String subject, String body, int P_Distribution_Booklet_ID, Timestamp StartDate, Timestamp EndDate )
    {
    	Manager = this.employee;

    	//+ 2010.11.05
    	// Envoie un email au signataire et coordonnateur
    	String sql = "SELECT P_Employee_ID FROM P_DISTRIBUTION_BOOKLET_REPLACEMENT " 
    			   + " WHERE ReplacementType IN ( 'N', 'C' ) "   
    			   + " and StartDate <= "  + DB.TO_DATE( StartDate)
    			   + " and ( EndDate is null or EndDate >= " +  DB.TO_DATE( EndDate ) + " )"
    			   + " and P_Distribution_Booklet_ID =  " + P_Distribution_Booklet_ID
    			   ;
    	
	    try
		{

			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			MUser user = null;
			String message = body;
			while (rs.next ())
			{	

		    	this.employee = P_Employee.get(ctx, rs.getInt( "P_Employee_ID" ), null);
		        // debug
		        // 2010.02.25 - correction du email envoyé au remplacant.
		    	user = MUser.get( ctx, employee.getAD_User_ID());
		    	message = body.replace( "user=" + Manager.getUserCode() , "user=" + user.getValue() );
		    	if ( user.getValue() != null )
		    		sendEMail( subject + " (" + Manager.getName() + " )" , message);
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println("* ERROR * Read P_Absence_Detail sql " + sql + "Exception :" + e);
		}
    	
    }


}

