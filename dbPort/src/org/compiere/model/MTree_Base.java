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
package org.compiere.model;

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Base Tree Model.
 *	(see also MTree in project base)
 *	
 *  @author Jorg Janke
 *  @version $Id: MTree_Base.java,v 1.1 2007/07/18 15:00:52 marmor01 Exp $
 */
public class MTree_Base extends X_AD_Tree
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Node TableName
	 *	@param treeType tree type
	 *	@return node table name, e.g. AD_TreeNode
	 */
	static String getNodeTableName (String treeType)
	{
		String	nodeTableName = "AD_TreeNode";
		if (TREETYPE_Menu.equals(treeType))
			nodeTableName += "MM";
		else if  (TREETYPE_BPartner.equals(treeType))
			nodeTableName += "BP";
		else if  (TREETYPE_Product.equals(treeType))
			nodeTableName += "PR";
		//
		else if  (TREETYPE_CMContainer.equals(treeType))
			nodeTableName += "CMC";
		else if  (TREETYPE_CMContainerStage.equals(treeType))
			nodeTableName += "CMS";
		else if  (TREETYPE_CMMedia.equals(treeType))
			nodeTableName += "CMM";
		else if  (TREETYPE_CMTemplate.equals(treeType))
			nodeTableName += "CMT";
		//
		else if  (TREETYPE_User1.equals(treeType))
			nodeTableName += "U1";
		else if  (TREETYPE_User2.equals(treeType))
			nodeTableName += "U2";
		else if  (TREETYPE_User3.equals(treeType))
			nodeTableName += "U3";
		else if  (TREETYPE_User4.equals(treeType))
			nodeTableName += "U4";
		return nodeTableName;
	}	//	getNodeTableName

	/**
	 * 	Get Node TableName
	 *	@param AD_Table_ID table
	 *	@return node table name, e.g. AD_TreeNode
	 */
	static String getNodeTableName (int AD_Table_ID)
	{
		String	nodeTableName = "AD_TreeNode";
		if (X_AD_Menu.Table_ID == AD_Table_ID)
			nodeTableName += "MM";
		else if  (X_C_BPartner.Table_ID == AD_Table_ID)
			nodeTableName += "BP";
		else if  (X_M_Product.Table_ID == AD_Table_ID)
			nodeTableName += "PR";
		//
		else if  (X_CM_Container.Table_ID == AD_Table_ID)
			nodeTableName += "CMC";
		else if  (X_CM_CStage.Table_ID == AD_Table_ID)
			nodeTableName += "CMS";
		else if  (X_CM_Media.Table_ID == AD_Table_ID)
			nodeTableName += "CMM";
		else if  (X_CM_Template.Table_ID == AD_Table_ID)
			nodeTableName += "CMT";
		//
		else
		{
			if (s_TableIDs == null)
				fillUserTables(null);
			Integer ii = new Integer(AD_Table_ID);
			if (s_TableIDs.contains(ii))
			{
				if  (s_TableIDs_U1.contains(ii))
					nodeTableName += "U1";
				else if (s_TableIDs_U2.contains(ii))
					nodeTableName += "U2";
				else if (s_TableIDs_U3.contains(ii))
					nodeTableName += "U3";
				else if (s_TableIDs_U4.contains(ii))
					nodeTableName += "U4";
			}
			else	//	no tree
				return null;
		}
		return nodeTableName;
	}	//	getNodeTableName

	/**
	 * 	Table has Tree
	 *	@param AD_Table_ID table
	 *	@return true if table has tree
	 */
	static boolean hasTree (int AD_Table_ID)
	{
		if (s_TableIDs == null)
			fillUserTables(null);
		Integer ii = new Integer(AD_Table_ID);
		return s_TableIDs.contains(ii);
	}	//	hasTree
	
	/**
	 * 	Fill User Tables
	 * 	@param trxName transaction
	 */
	static synchronized void fillUserTables (String trxName)
	{
		s_TableIDs = new ArrayList<Integer>();
		s_TableIDs_U1 = new ArrayList<Integer>();
		s_TableIDs_U2 = new ArrayList<Integer>();
		s_TableIDs_U3 = new ArrayList<Integer>();
		s_TableIDs_U4 = new ArrayList<Integer>();
		//
		String sql = "SELECT DISTINCT TreeType, AD_Table_ID FROM AD_Tree";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				String TreeType = rs.getString(1);
				int AD_Table_ID = rs.getInt(2);
				if (AD_Table_ID == 0)
					continue;
				Integer ii = new Integer(AD_Table_ID);
				s_TableIDs.add(ii);		//	all
				if (TreeType.equals ("U1"))
					s_TableIDs_U1.add(ii);
				else if (TreeType.equals ("U2"))
					s_TableIDs_U2.add(ii);
				else if (TreeType.equals ("U3"))
					s_TableIDs_U3.add(ii);
				else if (TreeType.equals ("U4"))
					s_TableIDs_U4.add(ii);
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		//	Not updated
		if (s_TableIDs.size() < 3)
		{
			MTree_Base xx = get (Env.getCtx(), 10, trxName);
			xx.updateTrees();
			fillUserTables(null);
		}
	}	//	fillUserTables
	
	/** All Table IDs					*/
	private static ArrayList<Integer> s_TableIDs = null;
	/** U1 Table IDs					*/
	private static ArrayList<Integer> s_TableIDs_U1 = null;
	/** U2 Table IDs					*/
	private static ArrayList<Integer> s_TableIDs_U2 = null;
	/** U3 Table IDs					*/
	private static ArrayList<Integer> s_TableIDs_U3 = null;
	/** U4 Table IDs					*/
	private static ArrayList<Integer> s_TableIDs_U4 = null;
	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger (MTree_Base.class);

	
	/**************************************************************************
	 * 	Get MTree_Base from Cache
	 *	@param ctx context
	 *	@param AD_Tree_ID id
	 *	@param trxName transaction
	 *	@return MTree_Base
	 */
	public static MTree_Base get (Properties ctx, int AD_Tree_ID, String trxName)
	{
		Integer key = new Integer (AD_Tree_ID);
		MTree_Base retValue = (MTree_Base) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MTree_Base (ctx, AD_Tree_ID, trxName);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,MTree_Base> s_cache = new CCache<Integer,MTree_Base>("AD_Tree", 10);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Tree_ID id
	 *	@param trxName transaction
	 */
	public MTree_Base (Properties ctx, int AD_Tree_ID, String trxName)
	{
		super(ctx, AD_Tree_ID, trxName);
		if (AD_Tree_ID == 0)
		{
		//	setName (null);
		//	setTreeType (null);
			setIsAllNodes (true);	//	complete tree
			setIsDefault(false);
		}
	}	//	MTree_Base

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MTree_Base (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MTree_Base

	/**
	 * 	Parent Constructor
	 *	@param client client
	 *	@param name name
	 *	@param treeType
	 */
	public MTree_Base (MClient client, String name, String treeType)
	{
		this (client.getCtx(), 0, client.get_TrxName());
		setClientOrg (client);
		setName (name);
		setTreeType (treeType);
		setAD_Table_ID();
	}	//	MTree_Base


	/**
	 * 	Full Constructor
	 *	@param ctx context
	 *	@param Name name
	 *	@param TreeType tree type
	 *	@param trxName transaction
	 */
	public MTree_Base (Properties ctx, String Name, String TreeType,  
		String trxName)
	{
		super(ctx, 0, trxName);
		setName (Name);
		setTreeType (TreeType);
		setAD_Table_ID();
		setIsAllNodes (true);	//	complete tree
		setIsDefault(false);
	}	//	MTree_Base

	
	/**
	 *	Get Node TableName
	 *	@return node table name, e.g. AD_TreeNode
	 */
	public String getNodeTableName()
	{
		return getNodeTableName(getTreeType());
	}	//	getNodeTableName
	
	/**
	 * 	Get Source TableName (i.e. where to get the name and color)
	 * 	@param tableNameOnly if false return From clause (alias = t)
	 *	@return source table name, e.g. AD_Org or null
	 */
	public String getSourceTableName (boolean tableNameOnly)
	{
		int AD_Table_ID = getAD_Table_ID();
		String tableName = MTable.getTableName (getCtx(), AD_Table_ID);
		//
		if (tableNameOnly)
			return tableName;
		if ("M_Product".equals(tableName))
			return "M_Product t INNER JOIN M_Product_Category x ON (t.M_Product_Category_ID=x.M_Product_Category_ID)";
		if ("C_BPartner".equals(tableName))
			return "C_BPartner t INNER JOIN C_BP_Group x ON (t.C_BP_Group_ID=x.C_BP_Group_ID)";
		if ("AD_Org".equals(tableName))
			return "AD_Org t INNER JOIN AD_OrgInfo i ON (t.AD_Org_ID=i.AD_Org_ID) "
				+ "LEFT OUTER JOIN AD_OrgType x ON (i.AD_OrgType_ID=x.AD_OrgType_ID)";
		if ("C_Campaign".equals(tableName))
			return "C_Campaign t LEFT OUTER JOIN C_Channel x ON (t.C_Channel_ID=x.C_Channel_ID)";
		if (tableName != null)
			tableName += " t";
		return tableName;
	}	//	getSourceTableName

	
	/**
	 * 	Get fully qualified Name of Action/Color Column
	 *	@return NULL or Action or Color
	 */
	public String getActionColorName()
	{
		int AD_Table_ID = getAD_Table_ID();
		String tableName = MTable.getTableName (getCtx(), AD_Table_ID);
		//
		if ("AD_Menu".equals(tableName))
			return "t.Action";
		if ("M_Product".equals(tableName) || "C_BPartner".equals(tableName) 
			|| "AD_Org".equals(tableName) || "C_Campaign".equals(tableName))
			return "x.AD_PrintColor_ID";
		return "NULL";
	}	//	getSourceTableName

	/**
	 * 	Get AD_Table_ID
	 *	@return table
	 */
	public int getAD_Table_ID()
	{
		int AD_Table_ID = super.getAD_Table_ID();
		if (AD_Table_ID == 0)
			AD_Table_ID = setAD_Table_ID();
		return AD_Table_ID;
	}	//	getAD_Table_ID

	/**
	 * 	Get AD_Table_ID
	 * 	@param base base info
	 *	@return table
	 */
	public int getAD_Table_ID(boolean base)
	{
		if (base)
			return super.getAD_Table_ID();
		return getAD_Table_ID();
	}	//	getAD_Table_ID

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (!isActive() || !isAllNodes())
			if (isDefault())
				setIsDefault(false);
		//	Table
		if (getAD_Table_ID(true) == 0)
		{
			if (newRecord)
				setAD_Table_ID();
			else
				updateTrees();
			//
			if (getAD_Table_ID(true) == 0)
			{
				log.warning ("No Table for " + toString());
				return false;
			}
		}
		return validate();
	}	//	beforeSave
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		String treeType = getTreeType();
		/**
		if (newRecord)	//	Base Node
		{
			if (TREETYPE_BPartner.equals(treeType))
			{
				MTree_NodeBP ndBP = new MTree_NodeBP(this, 0);
				ndBP.save();
			}
			else if (TREETYPE_Menu.equals(treeType))
			{
				MTree_NodeMM ndMM = new MTree_NodeMM(this, 0);
				ndMM.save();
			}
			else if (TREETYPE_Product.equals(treeType))
			{
				MTree_NodePR ndPR = new MTree_NodePR(this, 0);
				ndPR.save();
			}
			else
			{
				MTree_Node nd = new MTree_Node(this, 0);
				nd.save();
			}
		}
		**/
		if (treeType.startsWith("U"))
			fillUserTables(get_TrxName());
		//
		return success;
	}	//	afterSave
	
	/**************************************************************************
	 * 	Set AD_Table_ID from TreeType
	 *	@return AD_Table_ID
	 */
	private int setAD_Table_ID()
	{
		int AD_Table_ID = 0;
		String type = getTreeType();
		if (type == null
			|| type.startsWith ("U")	//	User
			|| type.equals (TREETYPE_Other))
			return 0;
		for (int i = 0; i < TREETYPES.length; i++)
		{
			if (type.equals (TREETYPES[i]))
			{
				AD_Table_ID = TABLEIDS[i];
				break;
			}
		}
		if (AD_Table_ID != 0)
			setAD_Table_ID (AD_Table_ID);
		if (AD_Table_ID == 0)
			log.warning ("Did not find Table for TreeType=" + type);
		return AD_Table_ID;
	}	//	setAD_Table_ID
	
	/**
	 * 	Validate TreeType and AD_Table_ID
	 *	@return true if Tree Type compatible with AD_Table_ID
	 */
	private boolean validate()
	{
		String type = getTreeType();
		if (type != null
				&& (type.startsWith ("U") || type.equals (TREETYPE_Other)))
			return true;
		//
		int AD_Table_ID = getAD_Table_ID(true);
		for (int i = 0; i < TREETYPES.length; i++)
		{
			if (type == null)
			{
				if (AD_Table_ID == TABLEIDS[i])
				{
					setTreeType (TREETYPES[i]);
					return true;
				}
			}
			else if (AD_Table_ID == TABLEIDS[i])
			{
				if (type.equals(TREETYPES[i]))
					return true;
				else
				{
					setTreeType (TREETYPES[i]);
					return true;
				}
			}
			else if (AD_Table_ID == 0 && type.equals(TREETYPES[i]))
			{
				setAD_Table_ID(TABLEIDS[i]);
				return true;
			}
		}
		//	None found
		if (type == null)
		{
			setTreeType (TREETYPE_Other);
			return true;
		}
		log.warning ("TreeType=" + type + " <> AD_Table_ID=" + AD_Table_ID);
		setTreeType (TREETYPE_Other);
		return false;
	}	//	validate
	
	/** Tree Type Array		*/
	private static final String[]	TREETYPES = new String[] {
		TREETYPE_Activity,
		TREETYPE_BoM,
		TREETYPE_BPartner,
		TREETYPE_CMContainer,
		TREETYPE_CMMedia,
		TREETYPE_CMContainerStage,
		TREETYPE_CMTemplate,
		TREETYPE_ElementValue,
		TREETYPE_Campaign,
		TREETYPE_Menu,
		TREETYPE_Organization,
		TREETYPE_ProductCategory,
		TREETYPE_Project,
		TREETYPE_Product,
		TREETYPE_SalesRegion,
		TREETYPE_User1,
		TREETYPE_User2,
		TREETYPE_User3,
		TREETYPE_User4,
		TREETYPE_Other
	};
	/** Table ID Array				*/
	private static final int[]		TABLEIDS = new int[] {
		X_C_Activity.Table_ID,
		X_M_BOM.Table_ID,
		X_C_BPartner.Table_ID,
		X_CM_Container.Table_ID,
		X_CM_Media.Table_ID,
		X_CM_CStage.Table_ID,
		X_CM_Template.Table_ID,
		X_C_ElementValue.Table_ID,
		X_C_Campaign.Table_ID,
		X_AD_Menu.Table_ID,
		X_AD_Org.Table_ID,
		X_M_Product_Category.Table_ID,
		X_C_Project.Table_ID,
		X_M_Product.Table_ID,
		X_C_SalesRegion.Table_ID,
		0,0,0,0,0
	};
	
	/**
	 * 	Update all Trees with Table_ID
	 */
	public void updateTrees()
	{
		setAD_Table_ID();
		for (int i = 0; i < TREETYPES.length; i++)
			updateTrees (TREETYPES[i], TABLEIDS[i]);
	}	//	updateTrees

	/**
	 * 	Update Trees
	 *	@param treeType tree type
	 *	@param AD_Table_ID table
	 */
	private void updateTrees(String treeType, int AD_Table_ID)
	{
		if (AD_Table_ID == 0)
			return;
		StringBuffer sb = new StringBuffer("UPDATE AD_Tree SET AD_Table_ID=")
			.append (AD_Table_ID)
			.append (" WHERE TreeType='").append (treeType).append ("' AND AD_Table_ID IS NULL");
		int no = DB.executeUpdate(sb.toString(), get_TrxName());
		log.fine (treeType + " #" + no);
	}	//	updateTrees

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("MTree_Base[");
		sb.append (get_ID ()).append ("-")
			.append(getName())
			.append(",Type=").append(getTreeType())
			.append(",AD_Table_ID=").append(getAD_Table_ID(true))
			.append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	MTree_Base
