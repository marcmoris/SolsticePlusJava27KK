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

import java.sql.*;
import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.compiere.util.DB;

import org.compiere.util.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;



public class ImportExportHandler extends DefaultHandler
{
	/**
	 * 	Import/Export Handler
	 * 	@param AD_Client_ID only certain client if id >= 0
	 */
	public ImportExportHandler (int AD_Client_ID, boolean UpdateRecord)
	{
		m_AD_Client_ID = AD_Client_ID;
	}	//	ImportExportHandler

	boolean m_UpdateRecord = false;
	
	/**	Client							*/
	private int				m_AD_Client_ID = -1;
	/** Language						*/
	private String			m_TableName = null;
	/** Update SQL						*/
	/** Current Value					*/
	private StringBuffer	m_curValue = null;
	/**	SQL								*/
	private StringBuffer	m_sql = null;
	private ArrayList<String> m_columns;
	private ArrayList<String> m_values;
	private Timestamp		m_time = new Timestamp(System.currentTimeMillis());
	private int				m_updateCount = 0;
	private static CLogger	log = CLogger.getCLogger(ImportExportHandler.class);
	private Stack stack;
	
	
	
	public void startDocument () throws SAXException
	{
		stack = new Stack();
		m_curValue = new StringBuffer();
		m_columns = new ArrayList();
		m_values = new ArrayList();
		m_sql = new StringBuffer();
	}
	
	/**************************************************************************
	 * 	Receive notification of the start of an element.
	 *
	 * 	@param uri namespace
	 * 	@param localName simple name
	 * 	@param qName qualified name
	 * 	@param attributes attributes
	 * 	@throws org.xml.sax.SAXException
	 */
	public void startElement (String uri, String localName, String qName, Attributes attributes)
		throws org.xml.sax.SAXException
	{
		
		if(attributes.getLength() > 0)
				m_TableName = qName;
		if((!qName.equals("Created")) && (!qName.equals("Updated")))
			stack.push(qName);
		
		
	//	log.fine( "ImportExportHandler.startElement", qName);	// + " - " + uri + " - " + localName);
	}	//	startElement

	/**
	 *	Receive notification of character data inside an element.
	 *
	 * 	@param ch buffer
	 * 	@param start start
	 * 	@param length length
	 * 	@throws SAXException
	 */
	public void characters (char ch[], int start, int length)
		throws SAXException
	{
		m_curValue.append(ch, start, length);
		
		// look for \n character and remove it
		int i = 0;
		while (i != -1)
		{
			i = m_curValue.indexOf("\n");
			if(i != -1)
				m_curValue.deleteCharAt(i);
		}
	}	//	characters

	/**
	 *	Receive notification of the end of an element.
	 * 	@param uri namespace
	 * 	@param localName simple name
	 * 	@param qName qualified name
	 * 	@throws SAXException
	 */
	public void endElement (String uri, String localName, String qName)
		throws SAXException
	{
		if((!qName.equals("Created")) && (!qName.equals("Updated")))
		{
			String elem = (String)stack.pop();
			if (elem == null || m_TableName == null)
				return;
			
			if (elem.compareTo(m_TableName) != 0)
			{
				m_columns.add(elem);
				m_values.add(m_curValue.toString());
			}
			else
			{
				if ( m_UpdateRecord )
				{
					buildSqlUpdate();
					executeUpdate();
					
				}
				else
				{
					buildSqlInsert();
					executeInsert();
				}
				m_columns.clear();
				m_values.clear();
			}
		}
		m_curValue.delete(0,m_curValue.length());
			
	}	//	endElement

	/**
	 * 	Get Number of updates
	 * 	@return update count
	 */
	public int getUpdateCount()
	{
		return m_updateCount;
	}	//	getUpdateCount

	private void buildSqlUpdate()
	{
		int m_TableName_ID = 0;
		m_sql.append("UPDATE ");
		m_sql.append(m_TableName);
		m_sql.append(" SET ");
		for (int i = 0; i < m_columns.size(); i ++)
		{
			int j = i;
//			m_sql.append("[");
			m_sql.append(m_columns.get(i) + " = ");
//			m_sql.append("] ");

			if (m_values.get(j).equals(""))
				m_sql.append("NULL");
			else
			{
				if ( m_columns.get(j).equals( "AD_Client_ID") && ! m_TableName.startsWith("AD"))
				{
					m_values.set(j, "11" );  // Env.getCtx().getAD_Client_ID()
				}
				if ( m_columns.get(j).equals( "AD_Org_ID") )
				{
					m_values.set(j, "0" );
				}
				if ( m_columns.get(j).equals( "CreatedBy") )
				{
					m_values.set(j, "100" );
				}
				if ( m_columns.get(j).equals( "UpdatedBy") )
				{
					m_values.set(j, "100" );
				}
				
				if ( m_columns.get(j).equals( m_TableName +"_ID" ) )
				{
					m_TableName_ID = Integer.parseInt( m_values.get(j) );
				}
				
				Pattern p = Pattern.compile( "([0-9]*)\\.([0-9]*)" );
				Matcher m = p.matcher(m_values.get(j));


				if ( m_values.get(j).contains(".") && m.matches() )
					m_sql.append(m_values.get(j).replaceAll("'", "''") );
				else if ( m_values.get(j).contains("-") && m_values.get(j).contains(":") )
				{
					//.replaceAll(" 00:00:00.0", "" )
					// 2008-01-07 08:26:46.0
					m_sql.append("'");
					m_sql.append(m_values.get(j).replaceAll("'", "''").substring( 0,10) );
					m_sql.append("'");
				}
				else
				{
					m_sql.append("'");
					m_sql.append(m_values.get(j).replaceAll("'", "''") );
					m_sql.append("'");
				}
			}

			if (i < m_columns.size()-1)
				m_sql.append(",");
		}
		m_sql.append(" WHERE " + m_TableName +"_ID = " + m_TableName_ID  );
		
		
		
	}

	
	private void buildSqlInsert()
	{
		m_sql.append("INSERT INTO ");
		m_sql.append(m_TableName);
		m_sql.append("(");
		for (int i = 0; i < m_columns.size(); i ++)
		{
//			m_sql.append("[");
			m_sql.append(m_columns.get(i));
//			m_sql.append("]");
			
			if (i < m_columns.size()-1)
				m_sql.append(",");
		}
		m_sql.append(") values (");
		
		for (int j = 0 ; j < m_values.size(); j++)
		{
			if (m_values.get(j).equals(""))
				m_sql.append("NULL");
			else
			{
				if ( m_columns.get(j).equals( "AD_Client_ID") && ! m_TableName.startsWith("AD"))
				{
					m_values.set(j, "11" );  // Env.getCtx().getAD_Client_ID()
				}
				if ( m_columns.get(j).equals( "AD_Org_ID") )
				{
					m_values.set(j, "0" );
				}
				if ( m_columns.get(j).equals( "CreatedBy") )
				{
					m_values.set(j, "100" );
				}
				if ( m_columns.get(j).equals( "UpdatedBy") )
				{
					m_values.set(j, "100" );
				}

				
				
				Pattern p = Pattern.compile( "([0-9]*)\\.([0-9]*)" );
				Matcher m = p.matcher(m_values.get(j));


				if ( m_values.get(j).contains(".") && m.matches() )
					m_sql.append(m_values.get(j).replaceAll("'", "''") );
				else if ( m_values.get(j).contains("-") && m_values.get(j).contains(":") )
				{
					//.replaceAll(" 00:00:00.0", "" )
					// 2008-01-07 08:26:46.0
					m_sql.append("'");
					m_sql.append(m_values.get(j).replaceAll("'", "''").substring( 0,10) );
					m_sql.append("'");
				}
				else
				{
					m_sql.append("'");
					m_sql.append(m_values.get(j).replaceAll("'", "''") );
					m_sql.append("'");
				}
			}
			
			if (j < m_columns.size()-1)
				m_sql.append(",");
		}
		
		m_sql.append(")");
		
		
	}
	
	private void executeInsert()
	{
		log.info(m_sql.toString());
		DB.executeUpdate(m_sql.toString(),null);
		m_sql.delete(0, m_sql.length());
		
	}

	private void executeUpdate()
	{
		log.info(m_sql.toString());
//		DB.executeUpdate(m_sql.toString(),null);
//		m_sql.delete(0, m_sql.length());
		
	}

}	//	ImportExportHandler
