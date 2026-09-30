/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is             Compiere  ERP & CRM Smart Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2003 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package solstice.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  P_Credits_Bound Model
 *
 *  @author Marc Morissette
 *  @version $Id: P_Credits_Bound.java,v 1.1 2007/07/18 14:43:40 marmor01 Exp $
 */
public class P_Credits_Bound extends X_P_Credits_Bound
{
	/**
	 * 	Get P_Credits_Bound
	 *	@param ctx context
	 * 	@param P_Credits_Bound_ID id
	 *	@return P_Credits_Bound
	 */
	public static P_Credits_Bound get (Properties ctx, int P_Credits_Bound_ID, String trxName)
	{
		Integer key = new Integer (P_Credits_Bound_ID);
		P_Credits_Bound P_Credits_Bound = (P_Credits_Bound)s_cache.get(key);
		if (P_Credits_Bound != null)
			return P_Credits_Bound;
		P_Credits_Bound = new P_Credits_Bound (ctx, P_Credits_Bound_ID, trxName);
		s_cache.put (key, P_Credits_Bound);
		return P_Credits_Bound;
	}	//	get


	/**	Cache						*/
	private static CCache<Integer,P_Credits_Bound>	s_cache = new CCache<Integer,P_Credits_Bound>("P_Credits_Bound", 20);
	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Credits_Bound_ID id
	 */
	public P_Credits_Bound (Properties ctx, int P_Credits_Bound_ID, String trxName)
	{
		super (ctx, P_Credits_Bound_ID, trxName);

		if (P_Credits_Bound_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
		
	}	//	P_Credits_Bound

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Credits_Bound (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}

	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Credits_Bound (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Credits_Bound_ID"),  trxName);
	}	//	P_Credits_Bound

	public static P_Credits_Bound get ( Properties ctx, int CreditID, int Method_Credits_ID, Timestamp EffectIn,  BigDecimal sld, String trxName )
	{

		int CreditsBoundID = 0;
		P_Credits_Param creditsparam = P_Credits_Param.get( ctx, CreditID, Method_Credits_ID, EffectIn, trxName);
		
		String sql = null;
		sql = "Select P_Credits_Bound_ID From P_Credits_Bound ";
		sql +="  Where P_Credits_Bound.IsActive = 'Y' ";
		sql +="    and " + sld + " >=P_Credits_Bound.LimitMin ";
		sql +="    and P_Credits_Param_ID = " + creditsparam.getP_Credits_Param_ID(); 
		sql +=" Order By LimitMin Desc ";
			
		//System.out.println("P_Credits_Bound get sql" + sql );
			
		PreparedStatement pstmt = null;

		try
		{
				pstmt = DB.prepareStatement (sql, null);
				int index = 1;
				ResultSet rs = pstmt.executeQuery ();
				if ( rs.next () )
				  CreditsBoundID = rs.getInt(1);
				else
				  CreditsBoundID = 0;
				
				rs.close ();
				pstmt.close ();
				pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println ("P_Credits_Bound - get - " + sql + " - " + e);
		}

		P_Credits_Bound Bound = new P_Credits_Bound( ctx, CreditsBoundID, trxName);
		return Bound;
	}	//	P_Credits_Bound


	/**
	 * 	After Save
	 *	@param newRecord
	 *	@param success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
	    if (!success )
		{
			return success;
		}

	    log.info("afterSave - New=" + newRecord + ", Success=" + success + " ***");
		return success;
	}	//	afterSave
	

	
}	//	P_Credits_Bound
