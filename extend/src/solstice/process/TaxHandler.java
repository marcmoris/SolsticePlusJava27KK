/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package solstice.process;

import java.sql.*;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;
import org.compiere.util.DB;

import org.compiere.util.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

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
import org.compiere.model.POInfo;
import org.compiere.model.POInfoColumn;


/**
 *	SAX Handler for parsing P_TaxImport
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: TaxHandler.java,v 1.2 2008/03/26 20:48:01 jeaham01 Exp $
 */
public class TaxHandler extends DefaultHandler
{
	/**
	 * 	P_TaxImport Handler
	 * 	@param AD_Client_ID only certain client if id >= 0
	 */
	public TaxHandler (int AD_Client_ID)
	{
		m_AD_Client_ID = AD_Client_ID;
	}	//	TaxHandler

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
	private static CLogger	log = CLogger.getCLogger(TaxHandler.class);
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
		
		
	//	log.fine( "TaxHandler.startElement", qName);	// + " - " + uri + " - " + localName);
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
			if (elem.compareTo(m_TableName) != 0)
			{
				m_columns.add(elem);
				m_values.add(m_curValue.toString());
			}
			else
			{
				buildSqlInsert();
				executeInsert();
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
	
	private void buildSqlInsert()
	{
		m_sql.append("INSERT INTO ");
		m_sql.append(m_TableName);
		m_sql.append("(");
		for (int i = 0; i < m_columns.size(); i ++)
		{
			m_sql.append("[");
			m_sql.append(m_columns.get(i));
			m_sql.append("]");
			
			if (i < m_columns.size()-1)
				m_sql.append(",");
		}
		m_sql.append(") values (");
		
		for (int j = 0 ; j < m_values.size(); j++)
		{
			if (m_values.get(j).equals(""))
				m_sql.append("NULL");
			else if ( m_columns.get(j).equals("AD_Client_ID") )
			{
				m_sql.append("'");
				m_sql.append("1000004");
				m_sql.append("'");
			}
			else if ( m_columns.get(j).equals("AD_Org_ID") )
			{
				m_sql.append("'");
				m_sql.append( 0 );
				m_sql.append("'");
			}
/*			else if ( m_columns.get(j).equals("P_Tax_ID") )
			{
				m_sql.append("'");
				m_sql.append( 2000058 );
				m_sql.append("'");
			}
			else if ( m_columns.get(j).equals("P_Tax_Federal_Ei_ID") )
			{
				m_sql.append("'");
				m_sql.append( Integer.parseInt( m_values.get(j)) + 10000 );
				m_sql.append("'");
			}
*/			
			else
			{
				m_sql.append("'");
				m_sql.append(m_values.get(j));
				m_sql.append("'");
			}
			
			if (j < m_columns.size()-1)
				m_sql.append(",");
		}
		
		m_sql.append(")");
		
	}
	
	private void executeInsert()
	{
		
		DB.executeUpdate(m_sql.toString(),null);
		m_sql.delete(0, m_sql.length());
		
	}

}	//	TaxHandler
