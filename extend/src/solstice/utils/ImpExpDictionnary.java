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
package solstice.utils;

import java.io.*;
import java.sql.*;
import java.util.Properties;
import java.util.logging.*;


import org.compiere.util.*;

import org.compiere.db.*;


//import org.compiere.install.ConfigurationPanel;
//import org.compiere.install.Setup_Help;

import org.w3c.dom.Element;
import org.w3c.dom.Text;

import org.compiere.model.*;

import solstice.utils.ImportExportHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import java.sql.Timestamp;
/**
 * Import/export data dictionnary Solstice
 * 
 * @author Marc Morissette
 * @version $Id: InstallSolstice.java,v 1.2 2007/07/18 21:09:07 marmor01 Exp $
 */
public class ImpExpDictionnary {

	/**
	 * Constructor
	 */

	private String trx;
	private DatabaseMetaData md;

	private String entityType = "'EXT','U','S'";
	private String fXmlPath = "C:\\Solstice\\Xml\\";
	
	public ImpExpDictionnary( String tableName, int AD_Table_ID, Timestamp startDate ) 
	{

/*		StringBuffer sql = new StringBuffer("select DISTINCT RECORD_ID from AD_CHANGELOG ")
        .append( "inner join AD_TABLE on AD_TABLE.AD_TABLE_ID = AD_CHANGELOG.AD_TABLE_ID " )
        .append( "where AD_TABLE.TABLENAME = '" + tableName + "' " )
        .append( " and AD_CHANGELOG.created > " + DB.TO_DATE( startDate) ) 
        .append( " order by RECORD_ID " );
*/        
		StringBuffer sql = new StringBuffer("select AD_TABLE_ID  from AD_TABLE ")
		        .append( "where AD_TABLE.TABLENAME = '" + tableName + "' " )
		        ;
		PreparedStatement pstmt = null;
		int count = 0;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				exportTable( tableName, AD_Table_ID, rs.getInt( "AD_TABLE_ID" ) );
				count++;
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.severe(e.toString());
		}
		finally
		{
			try
			{
				if (pstmt != null)
					pstmt.close ();
			}
			catch (Exception e)
			{
				
			}
			pstmt = null;
		}

		log.info("Generated Table = " + tableName + " " + count);
	} 


	/** Logger */
	private static CLogger log = CLogger.getCLogger(ImpExpDictionnary.class);
	//


	public String updateCustomizeData( String filePath)
	{

		filePath = fXmlPath;

		String msg = null;
		int i = 0;
		while ((i < tFileName.length) && (msg == null)) {
			String fileName = filePath + File.separator + tFileName[i] + ".xml";
			log.info(fileName);
			File in = new File(fileName);
			if (!in.exists()) {
				msg = "File does not exist: " + fileName;
				log.log(Level.SEVERE, msg);

			}
			try {
				
				log.info("Update Solstice Data from : " + tFileName[i]);

				int AD_Client_ID = 0;

				ImportExportHandler handler = new ImportExportHandler( AD_Client_ID, true);
				SAXParserFactory factory = SAXParserFactory.newInstance();
				// factory.setValidating(true);
				SAXParser parser = factory.newSAXParser();
				parser.parse(in, handler);
				log.info("Updated=" + handler.getUpdateCount());
				i++;
			} catch (Exception e) {
				log.log(Level.SEVERE, "importFile", e);
				return e.toString();
			}
		}

		return msg;
		
	}
	
	public String updateClientDictionnary()
	{
		// Backup Data dictionnary of the client.
//		this.export( fXmlPathBak.getText() );
		
		// Import nouvelle donnée
//		this.importFile( fXmlPath.getText() );
		
		this.updateCustomizeData( fXmlPath);
		
		return "";
	}
	
	private String[] tFileName = { "AD_Element", "AD_Window", "AD_Table",
			"AD_Tab", "AD_Reference", "AD_Ref_Table",
			"AD_Ref_List", "AD_Form", "AD_Menu", "AD_Process",
			"AD_Process_Para", "AD_Val_Rule", "AD_Message", "AD_FieldGroup",
			"AD_Form_Access", "AD_Process_Access", "AD_Window_Access",
			"AD_PrintFormat", "AD_PrintFormatItem", "AD_Tree" , "AD_Column", "AD_Field", 
			"AD_TableIndex", "AD_Element_TRL",
			"AD_Window_TRL",
			"AD_Table_TRL",
			"AD_Tab_TRL",
			"AD_Reference_TRL",
			"AD_Ref_List_TRL",
			"AD_Form_TRL",
			"AD_Menu_TRL",
			"AD_Process_TRL",
			"AD_Process_Para_TRL",
			"AD_Message_TRL",
			"AD_FieldGroup_TRL"
 };

	// "AD_IndexColumn"
	// "AD_TreeNodeMM",
	// "AD_Image"
	String filePath = "C:\\Solstice\\Xml\\"; 
	
	public String importFile( String filePath ) {
		log.info("Disable Constraint");

		String sql;
		if ( DB.isMSSQLServer())
		{
	  		   // SQL disable all triggers - disable all triggers sql server - t sql disable trigger
			   sql = "EXEC sp_MSforeachtable @command1=\"ALTER TABLE ? DISABLE TRIGGER ALL\"";
			   DB.executeUpdate(sql, null);
	  		   // SQL disable all constraints - disable all constraints sql server
			   sql =  "EXEC sp_MSforeachtable @command1=\"ALTER TABLE ? NOCHECK CONSTRAINT ALL\"";
			   DB.executeUpdate(sql, null);
		}

		if ( DB.isOracle() || DB.isOracleXE())
		{
			sql = "begin "
				+ " for cur in (select fk.owner, fk.constraint_name , fk.table_name "
				+ " from all_constraints fk, all_constraints pk "
				+ "where fk.CONSTRAINT_TYPE = 'R' and "
				+ "	pk.owner = '"
				+ CConnection.get().getDbUid().toString().toUpperCase()
				+ "' and "
				+ "	fk.R_CONSTRAINT_NAME = pk.CONSTRAINT_NAME and  "
				+ "	pk.TABLE_NAME in ( 'AD_ELEMENT','AD_TABLE', 'AD_COLUMN', 'AD_WINDOW','AD_FIELD',	'AD_TAB','AD_REFERENCE','AD_REFTABLE', 'AD_REFLIST',	'AD_FORM', 'AD_MENU', 'AD_PROCESS', 'AD_PROCESS_PARA', 'AD_TABLEINDEX', 'AD_INDEXCOLUMN', 'AD_VAL_RULE', 'AD_MESSAGE',		'AD_IMAGE', 'AD_FIELDGROUP', 'AD_PRINTFORMAT', 'AD_PRINTFORMATITEM', 'AD_TREE', 'AD_TREENODEMM' )) loop "
				+ "  execute immediate 'ALTER TABLE '||cur.owner||'.'||cur.table_name||' MODIFY CONSTRAINT '||cur.constraint_name||' DISABLE'; "
				+ " end loop; " + " end;";
			DB.executeUpdate(sql, null);
		}
		

		String msg = null;
		int i = 0;
		while ((i < tFileName.length) && (msg == null)) {
			String fileName = filePath + File.separator + tFileName[i] + ".xml";
			log.info(fileName);
			File in = new File(fileName);
			if (!in.exists()) {
				msg = "File does not exist: " + fileName;
				log.log(Level.SEVERE, msg);

			}
			try {
				
				 
				log.info("Insert Solstice Data from : " + tFileName[i]);

				int AD_Client_ID = 0;

				ImportExportHandler handler = new ImportExportHandler( AD_Client_ID, false);
				SAXParserFactory factory = SAXParserFactory.newInstance();
				// factory.setValidating(true);
				SAXParser parser = factory.newSAXParser();
				parser.parse(in, handler);
				log.info("Updated=" + handler.getUpdateCount());
				i++;
				// msg = Msg.getMsg(m_ctx, "Updated") + "=" +
				// handler.getUpdateCount();
				// msg = Msg.getMsg(m_ctx, "Updated");
			} catch (Exception e) {
				log.log(Level.SEVERE, "importFile", e);
				return e.toString();
			}
		}

		log.info("Enable Constraint");

		if ( DB.isMSSQLServer())
		{
			// SQL enable all triggers - disable all triggers sql server - t sql disable trigger
			sql = "EXEC sp_MSforeachtable @command1=\"ALTER TABLE ? ENABLE TRIGGER ALL\"";
			DB.executeUpdate(sql, null);
			// SQL enable all constraints - disable all constraints sql server
			sql = "EXEC sp_MSforeachtable @command1=\"ALTER TABLE ? CHECK CONSTRAINT ALL\"";
			DB.executeUpdate(sql, null);
			
		}
			
		if ( DB.isOracle() || DB.isOracleXE())
		{
			sql = "begin "
				+ " for cur in (select fk.owner, fk.constraint_name , fk.table_name "
				+ " from all_constraints fk, all_constraints pk "
				+ "where fk.CONSTRAINT_TYPE = 'R' and "
				+ "	pk.owner = '"
				+ CConnection.get().getDbUid().toString().toUpperCase()
				+ "' and "
				+ "	fk.R_CONSTRAINT_NAME = pk.CONSTRAINT_NAME and  "
				+ "	pk.TABLE_NAME in ( 'AD_ELEMENT','AD_TABLE', 'AD_COLUMN', 'AD_WINDOW','AD_FIELD',	'AD_TAB','AD_REFERENCE','AD_REFTABLE', 'AD_REFLIST',	'AD_FORM', 'AD_MENU', 'AD_PROCESS', 'AD_PROCESS_PARA', 'AD_TABLEINDEX', 'AD_INDEXCOLUMN', 'AD_VAL_RULE', 'AD_MESSAGE',		'AD_IMAGE', 'AD_FIELDGROUP', 'AD_PRINTFORMAT', 'AD_PRINTFORMATITEM', 'AD_TREE', 'AD_TREENODEMM' )) loop "
				+ "  execute immediate 'ALTER TABLE '||cur.owner||'.'||cur.table_name||' MODIFY CONSTRAINT '||cur.constraint_name||' ENABLE'; "
				+ " end loop; " + " end;";
			DB.executeUpdate(sql, null);
		}


		return msg;

	}


	StringBuffer head = new StringBuffer(
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?><!--Solstice Release 7.0 --><Solstice>");
	StringBuffer foot = new StringBuffer("</Solstice>");


	private String exportTable( String tableName, int AD_Table_ID, int Record_ID) 
	{
		StringBuffer result = new StringBuffer("");
		
		MTable Table = new MTable(Env.getCtx(), AD_Table_ID, null);
		Class clazz = MTable.getClass(tableName);
		if (clazz == null)
		{
			log.log(Level.WARNING, "(id) - Class not found for " + tableName);
			return null;
		}

		try
		{
			Constructor constructor = null;
			try
			{
				constructor = clazz.getDeclaredConstructor(new Class[]{Properties.class, int.class, String.class});
			}
			catch (Exception e)
			{
				String msg = e.getMessage();
				if (msg == null)
					msg = e.toString();
				log.warning("No transaction Constructor for " + clazz + " (" + msg + ")");
			}
			if (constructor != null)
			{
				PO po = (PO)constructor.newInstance(new Object[] {Env.getCtx(), new Integer(Record_ID), null});
				result.append( po.get_xmlString(new StringBuffer("")) );
			}
			else
				throw new Exception("No Std Constructor");
		}
		catch (Exception e)
		{
			if (e.getCause() != null)
			{
				Throwable t = e.getCause();
				log.log(Level.SEVERE, "(id) - Table=" + tableName + ",Class=" + clazz, t);
				if (t instanceof Exception)
					log.saveError("Error", (Exception)e.getCause());
				else
					log.saveError("Error", "Table=" + tableName + ",Class=" + clazz);
			}
			else
			{
				log.log(Level.SEVERE, "(id) - Table=" + tableName + ",Class=" + clazz, e);
				log.saveError("Error", "Table=" + tableName + ",Class=" + clazz);
			}
		}

		WriteToFile(result, tableName);
		
		return "";
	}


	private void WriteToFile(StringBuffer NewFile, String FileName) {

		log.log(Level.INFO, "Write file :" + filePath + File.separator
				+ FileName + ".xml");
		File aFile = null;
		aFile = new File(filePath + File.separator + FileName + ".xml");
		FileOutputStream out; // declare a file output object
		PrintStream p; // declare a print stream object
		try {
			// Create a new file output stream
			out = new FileOutputStream(aFile, true);

			// Connect print stream to the output stream
			p = new PrintStream(out);

			p.println(NewFile.toString());
			// System.out.println(NewFile.toString());
			p.close();
		} catch (Exception e) {
			log.log(Level.SEVERE, "Error writing to file :", e);
		}
	}

	public static void createElement(org.w3c.dom.Document doc, Element parent,
			String nodeName, String nodeValue) {
		if (nodeValue != null) {
			Element elem = doc.createElement(nodeName);
			Text text = doc.createTextNode(nodeValue);
			elem.appendChild(text);
			parent.appendChild(elem);
		} else {
			Element elem = doc.createElement(nodeName);
			elem.setAttribute("xsi:nil", "true");
			parent.appendChild(elem);

		}
	}

	private void exportToXML( String path, String Table )  
	{
		this.filePath = path;

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (Exception e) {
			log.log(Level.SEVERE, "newDocumentBuilder", e);
		}
	    Document doc = builder.newDocument();
	    Element results = doc.createElement("Results");
	    doc.appendChild(results);

    
	    String sql = "select * from " + Table;
	    PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement(sql, trx);
		try {

		ResultSet rs = pstmt.executeQuery();

	    ResultSetMetaData rsmd = rs.getMetaData();
	    int colCount = rsmd.getColumnCount();

		    while (rs.next()) {
		      Element row = doc.createElement("Row");
		      results.appendChild(row);
		      for (int i = 1; i <= colCount; i++) {
		        String columnName = rsmd.getColumnName(i);
		        Object value = rs.getObject(i);
		        Element node = doc.createElement(columnName);
		        if ( value != null)
		        	node.appendChild(doc.createTextNode(value.toString()));
		        else
		        	node.appendChild(doc.createTextNode("NULL"));
		        row.appendChild(node);
		      }
		    }
		    DOMSource domSource = new DOMSource(doc);
		    TransformerFactory tf = TransformerFactory.newInstance();
		    Transformer transformer = tf.newTransformer();
		    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		    transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
		    StringWriter sw = new StringWriter();
		    StreamResult sr = new StreamResult(sw);
		    transformer.transform(domSource, sr);
	
		    //System.out.println(sw.toString());
			WriteToFile( new StringBuffer( sw.toString()), Table );
	
		    rs.close();
		    pstmt.close();
		} catch (Exception e) {
			log.log(Level.SEVERE, sql, e);
		}
		
	}

	public void export( String path )  {

		int i = 0;
		while (i < tFileName.length) 
		{
			log.info("Update Solstice Data from : " + tFileName[i]);
			exportToXML( path, tFileName[i]);
			i++;
		}



	}


	public static void main (String[] args)
	{
		Timestamp StartDate = TimeUtil.getDay(2011, 7, 1);
		org.compiere.Compiere.startupEnvironment(true);
		CLogMgt.setLevel(Level.FINE);
		log.info("ImpExpDictionnary   $Revision: 1.0 $");
		log.info("----------------------------------");
		int count = 0;
/*		
		StringBuffer sql = new StringBuffer("select DISTINCT AD_TABLE.TABLENAME, AD_TABLE.AD_TABLE_ID from AD_CHANGELOG ")
		                 .append( "inner join AD_TABLE on AD_TABLE.AD_TABLE_ID = AD_CHANGELOG.AD_TABLE_ID " )
				         .append( "where AD_TABLE.TABLENAME like 'AD%' " )
						 .append( " and AD_TABLE.TABLENAME in ( 	'AD_Column','AD_Element','AD_Field','AD_Form','AD_Menu','AD_Message','AD_PrintFormat','AD_PrintFormatItem',	'AD_Process','AD_Process_Para','AD_Ref_List','AD_Reference','AD_ReportView','AD_Tab','AD_Table','AD_Val_Rule','AD_Window'" + 
						 		                               "'AD_Column_TRL','AD_Element_TRL','AD_Field_TRL','AD_Form_TRL','AD_Menu_TRL','AD_Message_TRL','AD_PrintFormat_TRL','AD_PrintFormatItem_TRL',	'AD_Process_TRL','AD_Process_Para_TRL','AD_Ref_List_TRL','AD_Reference_TRL','AD_ReportView_TRL','AD_Tab_TRL','AD_Table_TRL','AD_Val_Rule_TRL','AD_Window_TRL') " )
				         .append( " and AD_CHANGELOG.created > " + DB.TO_DATE(StartDate) ) 
				         .append( " order by AD_TABLE.TABLENAME " );
*/		
		StringBuffer sql = new StringBuffer("select DISTINCT AD_TABLE.TABLENAME, AD_TABLE.AD_TABLE_ID from AD_TABLE ")
		         .append( "where AD_TABLE.TABLENAME like 'AD%' " )
				 .append( " and AD_TABLE.TABLENAME in ( 	'AD_Column','AD_Element','AD_Field','AD_Form','AD_Menu','AD_Message','AD_PrintFormat','AD_PrintFormatItem',	'AD_Process','AD_Process_Para','AD_Ref_List','AD_Reference','AD_ReportView','AD_Tab','AD_Table','AD_Val_Rule','AD_Window'" + 
				 		                               "'AD_Column_TRL','AD_Element_TRL','AD_Field_TRL','AD_Form_TRL','AD_Menu_TRL','AD_Message_TRL','AD_PrintFormat_TRL','AD_PrintFormatItem_TRL',	'AD_Process_TRL','AD_Process_Para_TRL','AD_Ref_List_TRL','AD_Reference_TRL','AD_ReportView_TRL','AD_Tab_TRL','AD_Table_TRL','AD_Val_Rule_TRL','AD_Window_TRL') " )
		         .append( " order by AD_TABLE.TABLENAME " );
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				new ImpExpDictionnary( rs.getString("TABLENAME"), rs.getInt("AD_TABLE_ID"), StartDate );
				count++;
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.severe(e.toString());
		}
		finally
		{
			try
			{
				if (pstmt != null)
					pstmt.close ();
			}
			catch (Exception e)
			{}
			pstmt = null;
		}

		log.info("Generated = " + count);

	}

} // ImpExpDictionnary
	
