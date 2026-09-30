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
package org.compiere.dbPort;

import java.util.*;

import org.compiere.util.Env;

/**
 *  Database Syntax Conversion Map.
 *
 *
 *  @author     Jorg Janke & Victor Perez
 *  @version    $Id: ConvertMap.java,v 1.2 2007/08/06 13:58:15 marmor01 Exp $
 */
public class ConvertMap
{
	/**
	 *  Return Map for PostgreSQL
	 *  @return TreeMap with pattern as key and the replacement as value
	 */
	public static TreeMap<String, String> getPGMap()
	{
		if (s_pg.size() == 0)
			initPG();
		return s_pg;
	}   //  getPGMap

	/**
	 *  Return Map for DB/2
	 *  @return TreeMap with pattern as key and the replacement as value
	 */
	public static TreeMap<String, String> getDB2Map()
	{
		if (s_db2.size() == 0)
			initDB2();
		return s_db2;
	}   //  getDB2Map

	//+ Progestion
	/**
	 *  Return Map for SqlServer
	 *  @return TreeMap with pattern as key and the replacement as value
	 */
	public static TreeMap<String, String> getSqlSerSQLMap()
	{
		if (s_mssql.size() == 0)
			initSqlServerSQL();
		return s_mssql;
	}   //  getPostgreSQLMap
	
	//- Progestion
	
	/** Tree Map for PostgreSQL			*/
	private static TreeMap<String,String>  s_pg = new TreeMap<String,String>();
	/** Tree Map for DB/2			     */
	private static TreeMap<String,String>  s_db2 = new TreeMap<String,String>();

	//+ Progestion
	private static TreeMap<String,String>  s_mssql = new TreeMap<String,String>();
	//- Progestion
	
	/**
	 *  PostgreSQL Init
	 */
	static private void initPG()
	{
		//      Oracle Pattern                  Replacement

		s_pg.put("\\b::bpchar\\b",              ""); //jz gabage from EDB

		//  Data Types
		//s_pg.put("\\bNUMBER\\b",                "NUMERIC");
		//s_pg.put("\\bDATE\\b",                  "TIMESTAMP");
		s_pg.put("\\bNVARCHAR\\b",                 "VARCHAR");
		s_pg.put("\\bNVARCHAR2\\b",                 "VARCHAR");
		s_pg.put("\\bVARCHAR2\\b",              "VARCHAR");
		s_pg.put("\\bbpchar\\b",              "CHAR");
		s_pg.put("\\bNCHAR\\b",                 "CHAR");
	//	s_pg.put("\\bBLOB\\b",                  "OID");         //  BLOB not directly supported
	//	s_pg.put("\\bCLOB\\b",                  "TEXT");        //  CLOB not directly supported
		s_pg.put("\\bMODIFY\\b",                "ALTER");		//	MODIFY COLUMN	

		//  Storage
		s_pg.put("\\bCACHE\\b",                 "");
		s_pg.put("\\bUSING INDEX\\b",           "");
		s_pg.put("\\bTABLESPACE\\s\\w+\\b",     "");
		s_pg.put("\\bSTORAGE\\([\\w\\s]+\\)",   "");
		//
		s_pg.put("\\bBITMAP INDEX\\b",          "INDEX");

		//  Functions
		s_pg.put("\\bSysDate\\b",               "CURRENT_TIMESTAMP");
		s_pg.put("\\bSYSDATE\\b",               "CURRENT_TIMESTAMP");   //  alternative: NOW()
		s_pg.put("\\bNVL\\b",                   "COALESCE");
		s_pg.put("\\bTO_DATE\\b",               "TO_TIMESTAMP");
		s_pg.put("\\bTO_NCHAR\\b",                 "TO_CHAR");
		//
		s_pg.put("\\bDBMS_OUTPUT.PUT_LINE\\b",  "RAISE NOTICE");

		//  Temporary
		s_pg.put("\\bGLOBAL TEMPORARY\\b",      "TEMPORARY");
		s_pg.put("\\bON COMMIT DELETE ROWS\\b", "");
		s_pg.put("\\bON COMMIT PRESERVE ROWS\\b",   "");

		//  DROP TABLE x CASCADE CONSTRAINTS
		s_pg.put("\\bCASCADE CONSTRAINTS\\b",   "");

		//  Select
		//jz it exists, otherwise create one.
		/* s_pg.put("\\sFROM\\s+DUAL\\b",          "");
		s_db2.put(",NULL\\b",				",'' ");  //jz there are a few statements not with DB.NULL() yet
		s_db2.put(", NULL\\b",				",'' ");
		s_db2.put(",NULL,",				",'',");
		s_db2.put("SELECT NULL,",				"SELECT '',");	
		*/	

		//  Statements
		s_pg.put("\\bELSIF\\b",                 "ELSE IF");
		s_pg.put("\\bEND CASE\\b",                 "END");

		//  Sequences
		s_pg.put("\\bSTART WITH\\b",            "START");
		s_pg.put("\\bINCREMENT BY\\b",          "INCREMENT");

	}   //  initPG

	/**
	 *  DB/2 Init
	 */
	static private void initDB2()
	{
		//      Oracle Pattern                  Replacement

		s_db2.put("\\bTO_NCHAR\\b",                 "CHAR");

		//  Data Types
		/* 
		s_db2.put("DECIMAL(10,0)",                "INTEGER"); //jz numeric int
		s_db2.put("DECIMAL(22,0)",                "BIGINT"); //jz numeric int
		s_db2.put("\\bNUMBER(10,0)\\b",                "INTEGER"); //jz numeric int
		s_db2.put("\\bNUMBER(22,0)\\b",                "BIGINT"); //jz numeric int
		s_db2.put("\\bNUMBER(10)\\b",                "INTEGER"); //jz numeric int
		s_db2.put("\\bNUMBER(22)\\b",                "BIGINT"); //jz numeric int
		*/
		s_db2.put("\\bNUMBER\\b",                "DECIMAL(31,6)"); //jz numeric de
		s_db2.put("\\bDATE\\b",                  "TIMESTAMP");
		s_db2.put("\\bVARCHAR2\\b",              "VARCHAR");
		s_db2.put("\\bNVARCHAR2\\b",             "VARCHAR");
		s_db2.put("\\bNCHAR\\b",                 "CHAR");
		//jz s_db2.put("\\bBLOB\\b",                  "OID");                 //  BLOB not directly supported
		// s_db2.put("\\bCLOB\\b",                  "TEXT");                //  CLOB not directly supported

		//  Storage
		s_db2.put("\\bCACHE\\b",                 "");
		s_db2.put("\\bUSING INDEX\\b",           "");
		s_db2.put("\\bTABLESPACE\\s\\w+\\b",     "");
		s_db2.put("\\bSTORAGE\\([\\w\\s]+\\)",   "");
		//
		s_db2.put("\\bBITMAP INDEX\\b",          "INDEX");
		
		//  Functions
		//jz it needs () matching in pattern     s_db2.put("currencyBase(invoiceOpen",               "currencyBaseD(invoiceOpen");   //  alternative: NOW()
		s_db2.put("\\bSysDate\\b",               "CURRENT_TIMESTAMP");
		s_db2.put("\\bSYSDATE\\b",               "CURRENT_TIMESTAMP");   //  alternative: NOW()
		s_db2.put("\\bNVL\\b",                   "COALESCE");
		//jz    s_db2.put("\\bTO_DATE\\b",               "TO_TIMESTAMP");
		s_db2.put("\\bTO_DATE\\b",               "TIMESTAMP");
		//
		s_db2.put("\\bDBMS_OUTPUT.PUT_LINE\\b",  "RAISE NOTICE");

		//  Temporary
		s_db2.put("\\bGLOBAL TEMPORARY\\b",      "TEMPORARY");
		s_db2.put("\\bON COMMIT DELETE ROWS\\b", "");
		s_db2.put("\\bON COMMIT PRESERVE ROWS\\b",   "");


		//  DROP TABLE x CASCADE CONSTRAINTS
		s_db2.put("\\bCASCADE CONSTRAINTS\\b",   "");

		//  Select
		//jz add nullif -> '' since derby is not stable with null value
		/* jz () problem in matching
		s_db2.put(",NULL\\b",				",nullif('a','a') ");
		s_db2.put(", NULL\\b",				",nullif('a','a') ");
		s_db2.put(",NULL,",				",nullif('a','a'),");
		s_db2.put("SELECT NULL,",				"SELECT nullif('a','a'),");		
		*/
		s_db2.put(",NULL\\b",				",'' ");
		s_db2.put(", NULL\\b",				",'' ");
		s_db2.put(",NULL,",				",'',");
		s_db2.put("SELECT NULL,",				"SELECT '',");		
		 
		s_db2.put("\\sFROM\\s+DUAL\\b",          " FROM SYSIBM.SYSDUMMY1 ");
		

		//  Statements
		s_db2.put("\\bELSIF\\b",                 "ELSE IF");

		//  Sequences
		s_db2.put("\\bSTART WITH\\b",            "START");
		s_db2.put("\\bINCREMENT BY\\b",          "INCREMENT");

	}   //  initPostgreSQL
	
	
	//+ Progestion

	/**
	 *  SqlServerSQL Init
	 */
	static private void initSqlServerSQL()
	{
		if ( Env.getContext( Env.getCtx(), "FromImpExpDictionnary") != null && Env.getContext( Env.getCtx(), "FromImpExpDictionnary").equals( "true"))
			return;
		//      Oracle Pattern                  Replacement

		//  Data Types
		s_mssql.put("\\bNUMBER\\b",                "NUMERIC"); 
//		s_mssql.put("\\bNUMBER\\b",                "NUMERIC");
//		s_mssql.put("\\bDATE\\b",                  "DATETIME");
		s_mssql.put("\\bVARCHAR2\\b",              "VARCHAR");
		s_mssql.put("\\bNVARCHAR2\\b",             "NVARCHAR");
		s_mssql.put("\\bNCHAR\\b",                 "NCHAR");
		s_mssql.put("\\bBLOB\\b",                  "IMAGE");
		s_mssql.put("\\bCLOB\\b",                  "TEXT");

		//  Storage
		s_mssql.put("\\bCACHE\\b",                 "");
		s_mssql.put("\\bUSING INDEX\\b",           "");
		s_mssql.put("\\bTABLESPACE\\s\\w+\\b",     "");
		s_mssql.put("\\bSTORAGE\\([\\w\\s]+\\)",   "");
		//
		s_mssql.put("\\bBITMAP INDEX\\b",          "INDEX");
		
		//	Select
		s_mssql.put("\\bFOR UPDATE\\b",		   "FOR UPDATE");
		s_mssql.put("\\bTRUNC\\(",		   "dbo.trunc(");
		s_mssql.put("\\bEND CASE\\b", 		   "END");
		
		// Replace || (oracle concatenation) by +
		
		s_mssql.put("\\|\\|", 		   " + ");
		s_mssql.put("\\bLineNo\\b",    "[LineNo]");
		s_mssql.put("\\bPercent\\b",    "[Percent]");
		
		//  Functions
		s_mssql.put("\\bSysDate\\b",               "getdate()");
		s_mssql.put("\\bSYSDATE\\b",               "getdate()");
		s_mssql.put("\\bNVL\\b",                   "isNull");
		s_mssql.put("\\bTO_DATE\\b",               "dbo.TO_DATE");
		s_mssql.put("\\baddDays\\(",               "dbo.addDays(");
		
		//
		s_mssql.put("\\bDBMS_OUTPUT.PUT_LINE\\b",  "RAISE NOTICE");

		//  Temporary
		s_mssql.put("\\bGLOBAL TEMPORARY\\b",      "TEMPORARY");
		s_mssql.put("\\bON COMMIT DELETE ROWS\\b", "");
		s_mssql.put("\\bON COMMIT PRESERVE ROWS\\b",   "");


		//  DROP TABLE x CASCADE CONSTRAINTS
		s_mssql.put("\\bCASCADE CONSTRAINTS\\b",   "");

		//  Select
		s_mssql.put("\\sFROM\\s+DUAL\\b",          "");

		//  Statements
		s_mssql.put("\\bELSIF\\b",                 "ELSE IF");

		//  Sequences
		s_mssql.put("\\bSTART WITH\\b",            "START");
		s_mssql.put("\\bINCREMENT BY\\b",          "INCREMENT");

	}   //  initSqlServerSQL

	//- Progestion
	
}   //  ConvertMap
