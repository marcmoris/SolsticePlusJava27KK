package org.compiere.cm.invoice;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.compiere.cm.HttpServletCM;
import org.compiere.model.MInvoice;
import org.compiere.util.WebUser;
import org.compiere.util.WebUtil;

public class Invoice extends HttpServletCM{

	/**
	 * 	Stream invoice
	 * 	@param request request
	 * 	@param response response
	 * 	@return "" or error message
	 */
	public static String streamInvoice (HttpServletRequest request, HttpServletResponse response, Properties ctx) {
			int MIN_SIZE = 2000; 	//	if not created size is 1015
			
			//	Get Invoice ID
			int C_Invoice_ID = WebUtil.getParameterAsInt (request, "Invoice_ID");
			if (C_Invoice_ID == 0)
			{
				return "No Invoice ID";
			}

			//	Get Invoice
			MInvoice invoice = new MInvoice (ctx, C_Invoice_ID, null);
			if (invoice.getC_Invoice_ID() != C_Invoice_ID)
			{
				return "Invoice not found";
			}
			//	Get WebUser & Compare with invoice
			HttpSession session = request.getSession(true);
			WebUser wu = (WebUser)session.getAttribute(WebUser.NAME);
			if (wu.getC_BPartner_ID() != invoice.getC_BPartner_ID())
			{
				return "Your invoice not found";
			}

			//	Check Directory
			String dirName = ctx.getProperty("documentDir", ".");
			try
			{
				File dir = new File (dirName);
				if (!dir.exists ())
					dir.mkdir ();
			}
			catch (Exception ex)
			{
				return "Streaming error - directory";
			}
			//	Check if Invoice already created
			String fileName = invoice.getPDFFileName (dirName);
			File file = new File(fileName);
			if (!file.exists() || !file.isFile() || file.length() < MIN_SIZE)	
			{
				file = invoice.createPDF (file);
				if (file != null)
				{
					invoice.setDatePrinted (new Timestamp(System.currentTimeMillis()));
					invoice.save();
				}
			}
			//	Issue Error
			if (file == null || !file.exists() || file.length() < MIN_SIZE) 
			{
				return "Streaming error - file";
			}

			//	Send PDF
			try
			{
				int bufferSize = 2048; //	2k Buffer
				int fileLength = (int)file.length();
				//
				response.setContentType("application/pdf");
				response.setBufferSize(bufferSize);
				response.setContentLength(fileLength);
				//
				long time = System.currentTimeMillis();		//	timer start
				//
				FileInputStream in = new FileInputStream (file);
				ServletOutputStream out = response.getOutputStream ();
				byte[] buffer = new byte[bufferSize];
				double totalSize = 0;
				int count = 0;
				do
				{
					count = in.read(buffer, 0, bufferSize);
					if (count > 0)
					{
						totalSize += count;
						out.write (buffer, 0, count);
					}
				} while (count != -1);
				out.flush();
				out.close();
				//
				in.close();
				time = System.currentTimeMillis() - time;
				double speed = (totalSize/1024) / ((double)time/1000);
			}
			catch (IOException ex)
			{
				return "Streaming error";
			}

			return null;
		}	//	streamInvoice

}	//	InvoiceServlet

