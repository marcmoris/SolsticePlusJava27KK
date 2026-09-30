package solstice.process;
import java.awt.*;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.net.ftp.*;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import solstice.model.P_Tax;
import solstice.model.P_Tax_Federal_Rate;
import solstice.model.X_P_Tax_Federal;
import solstice.model.X_P_Tax_Federal_Ei;
import solstice.model.X_P_Tax_Federal_Rpc;
import solstice.model.X_P_Tax_Federal_Td1;
import solstice.model.X_P_Tax_Provincial;
import solstice.model.X_P_Tax_Provincial_Rate;
import solstice.model.X_P_Tax_Provincial_TD1;
import solstice.model.X_P_Tax_RQAP;
import solstice.model.X_P_Tax_RRQ;
import org.compiere.util.TimeUtil;

public class P_TaxImport extends SvrProcess
{

	private String filePath = "C:\\Solstice\\TaxUpdate\\";

	/**	DTD						*/
	public static final String DTD = "<!DOCTYPE compiereTrl PUBLIC \"-//ComPiere, Inc.//DTD Compiere Translation 1.0//EN\" \"http://www.compiere.org/dtd/compiereTrl.dtd\">";
	/**	XML Element Tag			*/
	public static final String	XML_TAG = "P_Tax";

	
	
	private Timestamp EffectIn;
	private int Tax_ID = 0;
	private String trxName = null;
	private P_Tax Tax;
	private int count;
	private String[] tFileName = {"P_Tax","P_Tax_Federal","P_Tax_Federal_Ei","P_Tax_Federal_Rate",
			"P_Tax_Federal_Rpc","P_Tax_Federal_TD1","P_Tax_Provincial", "P_Tax_Provincial_Rate",
			"P_Tax_Provincial_TD1", "P_Tax_RQAP", "P_Tax_RRQ"};
	

	/** Properties					*/
	private Properties		m_ctx = null;

	private ArrayList<String>	m_data = new ArrayList<String>();

	private int AD_Client_ID;

	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("EffectIn"))
				EffectIn = (Timestamp)para[i].getParameter();
			else if ( name.equals("FilePath") )
				filePath = (String)para[i].getParameter();
			else
				log.log (Level.SEVERE,"prepare - Unknown Parameter: " + name);
		}
		
		if ( EffectIn == null )
			EffectIn = TimeUtil.getDay(2025, 01, 01);
		
		AD_Client_ID = this.getAD_Client_ID();
	}	//	prepare

	
	/**
	 * 	Read each time sheet 
	 *	@return 
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		FileInputStream m_stream = null;

		importTax();
		
		int count = 1;
		
		return "@Inserted " + count;
	}	

	private String getIP_Address()
	{
		return "127.0.0.1";
	}
	
	private String getUserName()
	{
		return "Solstice";
	}
	
	private String getPassword()
	{
		return "Canada01";
	}
	
	private String getFolder()
	{
		return "Solstice";
	}
	
	private String getFileName()
	{
		return "SolsticeTax.dat";
	}
	
	private boolean load_ftp()
	{
		FTPClient ftp = new FTPClient();
		try
		{
			ftp.connect (getIP_Address());
			if (ftp.login (getUserName(), getPassword()))
				log.info("Connected to " + getIP_Address() + " as " + getUserName());
			else
			{
				log.warning("Could NOT connect to " + getIP_Address() + " as " + getUserName());
				return false;
			}
		}
		catch (Exception e)
		{
			log.log(Level.WARNING, "Could NOT connect to " + getIP_Address() + " as " + getUserName(), e);
			return false;
		}

		boolean success = true;
		String cmd = null;
		//	List the files in the directory
/*		try
		{
			cmd = "cwd";
			ftp.changeWorkingDirectory (getFolder());
			//
			cmd = "list";
			String[] fileNames = ftp.listNames();
			log.log(Level.FINE, "Number of files in " + getFolder() + ": " + fileNames.length);
			
			/ *
		*	FTPFile[] files = ftp.listFiles();
		*	log.config("Number of files in " + getFolder() + ": " + files.length);
		*	for (int i = 0; i < files.length; i++)
		*		log.fine(files[i].getTimestamp() + " \t" + files[i].getName());
			* /
			//
			cmd = "bin";
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			//
			String fileName = getFileName();
			cmd = "get " + fileName;
			
			FileOutputStream fos = new FileOutputStream( file );
			ftp.retrieveFile(fileName, fos);
			//.storeFile(fileName, is);
			is.close();
			thisDeployment.setIsDeployed (true);
			thisDeployment.save ();
		}
		catch (Exception e)
		{
			log.log(Level.WARNING, cmd, e);
			success = false;
		}
*/		//	Logout from the FTP Server and disconnect
		try
		{
			cmd = "logout";
			ftp.logout();
			log.log(Level.FINE, " FTP logged out");
			cmd = "disconnect";
			ftp.disconnect();
		}
		catch (Exception e)
		{
			log.log(Level.WARNING, cmd, e);
		}
		ftp = null;
		return success;

	}
	/**************************************************************************
	 *	Load File
	 */
	private String importTax()
	{
		String msg = null;
		int i = 0;
		while ((i < tFileName.length) && (msg == null))
		{
			String fileName = filePath + File.separator + tFileName[i] + ".xml";
			log.info(fileName);
			File in = new File (fileName);
			if (!in.exists())
			{
				msg = "File does not exist: " + fileName;
				log.log(Level.SEVERE, msg);
				
			}
			try
			{
				TaxHandler handler = new TaxHandler(AD_Client_ID);
				SAXParserFactory factory = SAXParserFactory.newInstance();
			//	factory.setValidating(true);
				SAXParser parser = factory.newSAXParser();
				parser.parse(in, handler);
				log.info("Updated=" + handler.getUpdateCount());
				i ++;
				//msg = Msg.getMsg(m_ctx, "Updated") + "=" + handler.getUpdateCount();
				//msg = Msg.getMsg(m_ctx, "Updated");
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, "importTrl", e);
				return e.toString();
			}
		}
		return msg;


	}	//	cmd_loadFile

	 
	private int nbrRecord = 0;
	StringBuffer head = new StringBuffer ("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!--Solstice Release 7.0 -->");
	StringBuffer foot = new StringBuffer ("</P_Tax>");
	
	public String export()
	{
		Export_Tax();
		Export_Tax_Federal();
		Export_Tax_Federal_Ei();
		Export_Tax_Federal_Rate();
		Export_Tax_Federal_Rpc();
		Export_Tax_Rrq();
		Export_Tax_Rqap();
		Export_Tax_Federal_Td1();
		Export_Tax_Provincial();
		Export_Tax_Provincial_Rate( );
		Export_Tax_Provincial_Td1( );

		return "@Inserted@ " + nbrRecord ; 
		
	}
	
	public void setFilePath(String path)
	{
		filePath = path;
	}

	private void Export_Tax()
	{
		nbrRecord++;
//		Tax = P_Tax.get( Env.getCtx(), P_Tax.getCurrentTax(EffectIn), trxName);
		Tax = P_Tax.get( Env.getCtx(), 4000001, trxName);
		
		Tax_ID = Tax.getP_Tax_ID();
		
		WriteToFile( Tax.get_xmlString(head) , "P_Tax");
		
		head = new StringBuffer( "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!--Solstice Release 7.0 --><P_Tax>" );

//		result.append().append(";");
	}
	
	private void Export_Tax_Federal()
	{
		StringBuffer result = new StringBuffer("");
		String sql = "Select * From P_TAX_FEDERAL WHERE P_Tax_Id =  " + Tax_ID;
		PreparedStatement pstmt = null;
		try 
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			X_P_Tax_Federal Tax_Federal;

			result.append( head );
			while (rs.next ()) 
			{
				Tax_Federal = new X_P_Tax_Federal( Env.getCtx(), rs.getInt("P_Tax_Federal_ID"), trxName);
				result.append( Tax_Federal.get_xmlString(new StringBuffer(""))).append("\r\n");
				
			}
			result.append( foot );

			rs.close ();
			pstmt.close ();
			pstmt = null;

			WriteToFile( result , "P_Tax_Federal");

		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"P_TaxExport - " + sql, e);
		}
		
	}

	private void Export_Tax_Federal_Ei()
	{
		StringBuffer result = new StringBuffer("");
		String sql = "Select * From P_Tax_Federal_Ei WHERE P_Tax_Id =  " + Tax_ID;
		PreparedStatement pstmt = null;
		try 
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			X_P_Tax_Federal_Ei Tax_Federal_Ei;

			result.append( head );
			while (rs.next ()) 
			{
				Tax_Federal_Ei = new X_P_Tax_Federal_Ei( Env.getCtx(), rs.getInt("P_Tax_Federal_Ei_ID"), trxName);
				result.append( Tax_Federal_Ei.get_xmlString(new StringBuffer(""))).append("\r\n");
			}
			result.append( foot );
			rs.close ();
			pstmt.close ();
			pstmt = null;

			WriteToFile( result , "P_Tax_Federal_Ei");

		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"P_TaxExport - " + sql, e);
		}
		
		
	}
	
	private void Export_Tax_Federal_Rate()
	{
		StringBuffer result = new StringBuffer("");
		String sql = "Select * From P_Tax_Federal_Rate WHERE P_Tax_Id =  " + Tax_ID;
		PreparedStatement pstmt = null;
		try 
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			P_Tax_Federal_Rate Tax_Federal_Rate;

			result.append( head );
			while (rs.next ()) 
			{
				Tax_Federal_Rate = new P_Tax_Federal_Rate( Env.getCtx(), rs.getInt("P_Tax_Federal_Rate_ID"), trxName);
				result.append( Tax_Federal_Rate.get_xmlString(new StringBuffer(""))).append("\r\n");
			}
			result.append( foot );
			rs.close ();
			pstmt.close ();
			pstmt = null;

			WriteToFile( result , "P_Tax_Federal_Rate");
			
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"P_TaxExport - " + sql, e);
		}
		
	}
	
	private void Export_Tax_Federal_Rpc()
	{
		StringBuffer result = new StringBuffer("");
		String sql = "Select * From P_Tax_Federal_Rpc WHERE P_Tax_Id =  " + Tax_ID;
		PreparedStatement pstmt = null;
		try 
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			X_P_Tax_Federal_Rpc Tax_Federal_Rpc;

			result.append( head );
			while (rs.next ()) 
			{
				Tax_Federal_Rpc = new X_P_Tax_Federal_Rpc( Env.getCtx(), rs.getInt("P_Tax_Federal_Rpc_ID"), trxName);
				result.append( Tax_Federal_Rpc.get_xmlString(new StringBuffer(""))).append("\r\n");
			}
			result.append( foot );
			rs.close ();
			pstmt.close ();
			pstmt = null;

			WriteToFile( result , "P_Tax_Federal_Rpc");

		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"P_TaxExport - " + sql, e);
		}
		
	}
	
	private void Export_Tax_Rrq()
	{
		StringBuffer result = new StringBuffer("");
		String sql = "Select * From P_Tax_RRQ WHERE P_Tax_Id =  " + Tax_ID;
		PreparedStatement pstmt = null;
		try 
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			X_P_Tax_RRQ Tax_RRQ;

			result.append( head );
			while (rs.next ()) 
			{
				Tax_RRQ = new X_P_Tax_RRQ( Env.getCtx(), rs.getInt("P_Tax_RRQ_ID"), trxName);
				result.append( Tax_RRQ.get_xmlString(new StringBuffer(""))).append("\r\n");
			}
			result.append( foot );
			rs.close ();
			pstmt.close ();
			pstmt = null;

			WriteToFile( result , "P_Tax_RRQ");

		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"P_TaxExport - " + sql, e);
		}
		
	}

	private void Export_Tax_Rqap()
	{
		StringBuffer result = new StringBuffer("");
		String sql = "Select * From P_Tax_RQAP WHERE P_Tax_Id =  " + Tax_ID;
		PreparedStatement pstmt = null;
		try 
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			X_P_Tax_RQAP Tax_RQAP;

			result.append( head );
			while (rs.next ()) 
			{
				Tax_RQAP = new X_P_Tax_RQAP( Env.getCtx(), rs.getInt("P_Tax_RQAP_ID"), trxName);
				result.append( Tax_RQAP.get_xmlString(new StringBuffer(""))).append("\r\n");
			}
			result.append( foot );
			rs.close ();
			pstmt.close ();
			pstmt = null;

			WriteToFile( result , "P_Tax_RQAP");

		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"P_TaxExport - " + sql, e);
		}
		
	}

	private void Export_Tax_Federal_Td1()
	{
		StringBuffer result = new StringBuffer("");
		String sql = "Select * From P_Tax_Federal_TD1 WHERE P_Tax_Id =  " + Tax_ID;
		PreparedStatement pstmt = null;
		try 
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			X_P_Tax_Federal_Td1 Tax_Federal_TD1;

			result.append( head );
			while (rs.next ()) 
			{
				Tax_Federal_TD1 = new X_P_Tax_Federal_Td1( Env.getCtx(), rs.getInt("P_Tax_Federal_TD1_ID"), trxName);
				result.append( Tax_Federal_TD1.get_xmlString(new StringBuffer(""))).append("\r\n");
			}
			result.append( foot );
			rs.close ();
			pstmt.close ();
			pstmt = null;

			WriteToFile( result , "P_Tax_Federal_TD1");

		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"P_TaxExport - " + sql, e);
		}
		
	}

	
	private void Export_Tax_Provincial()
	{
		StringBuffer result = new StringBuffer("");
		String sql = "Select * From P_Tax_Provincial WHERE P_Tax_Id =  " + Tax_ID;
		PreparedStatement pstmt = null;
		try 
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			X_P_Tax_Provincial Tax_Provincial;

			result.append( head );
			while (rs.next ()) 
			{
				Tax_Provincial = new X_P_Tax_Provincial( Env.getCtx(), rs.getInt("P_Tax_Provincial_ID"), trxName);
				result.append( Tax_Provincial.get_xmlString(new StringBuffer(""))).append("\r\n");
			}
			result.append( foot );
			rs.close ();
			pstmt.close ();
			pstmt = null;

			WriteToFile( result , "P_Tax_Provincial");

		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"P_TaxExport - " + sql, e);
		}
		
	}

	private void Export_Tax_Provincial_Rate()
	{
		StringBuffer result = new StringBuffer("");
		String sql = "Select * From P_Tax_Provincial_Rate WHERE P_Tax_Provincial_ID in ( Select P_Tax_Provincial_ID From P_Tax_Provincial WHERE P_Tax_Id =  " + Tax_ID + " ) " ;
		PreparedStatement pstmt = null;
		try 
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			X_P_Tax_Provincial_Rate Tax_Provincial;

			result.append( head );
			while (rs.next ()) 
			{
				Tax_Provincial = new X_P_Tax_Provincial_Rate( Env.getCtx(), rs.getInt("P_Tax_Provincial_Rate_ID"), trxName);
				result.append( Tax_Provincial.get_xmlString(new StringBuffer(""))).append("\r\n");
			}
			result.append( foot );
			rs.close ();
			pstmt.close ();
			pstmt = null;

			WriteToFile( result , "P_Tax_Provincial_Rate");

		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"P_TaxExport - " + sql, e);
		}
		
	}
	
	private void Export_Tax_Provincial_Td1( )
	{
		StringBuffer result = new StringBuffer("");
		String sql = "Select * From P_Tax_Provincial_Td1 WHERE P_Tax_Provincial_ID in ( Select P_Tax_Provincial_ID From P_Tax_Provincial WHERE P_Tax_Id =  " + Tax_ID + " ) " ;
		PreparedStatement pstmt = null;
		try 
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			X_P_Tax_Provincial_TD1 Tax_Provincial;

			result.append( head );
			while (rs.next ()) 
			{
				Tax_Provincial = new X_P_Tax_Provincial_TD1( Env.getCtx(), rs.getInt("P_Tax_Provincial_Td1_ID"), trxName);
				result.append( Tax_Provincial.get_xmlString(new StringBuffer(""))).append("\r\n");
			}
			result.append( foot );
			rs.close ();
			pstmt.close ();
			pstmt = null;

			WriteToFile( result , "P_Tax_Provincial_TD1");

		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"P_TaxExport - " + sql, e);
		}
		
	}

    public void WriteToFile(StringBuffer NewFile, String FileName)
    {

    	File aFile = null;
  	    aFile = new File( filePath + FileName + ".xml" ); 
        FileOutputStream out; // declare a file output object
        PrintStream p; // declare a print stream object
        try
        {
              // Create a new file output stream
              out = new FileOutputStream(aFile);

              // Connect print stream to the output stream
              p = new PrintStream( out );

              p.println (NewFile.toString());
//              System.out.println(NewFile.toString());
              p.close();
        }
        catch (Exception e)
        {
        	log.log (Level.SEVERE, "Error writing to file :", e);
        }
   }

    /**
	 * 	Test
	 *	@param args
	 */
	public static void main (String[] args)
	{
		org.compiere.Compiere.startup(true);
		P_TaxImport tax = new P_TaxImport();
//		tax.export();
		tax.importTax();
		
	}	//	main
	

	
}
