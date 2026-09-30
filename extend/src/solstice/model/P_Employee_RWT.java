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
package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Assignment_RWT Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Employee_RWT extends X_P_Employee_RWT
{
	/**
	 * 	Get Employee_RWT
	 *	@param ctx context
	 * 	@param P_Employee_RWT_ID id
	 *	@return Employee_RWT
	 */
	public static P_Employee_RWT get (Properties ctx, int P_Employee_RWT_ID, String trxName)
	{
		Integer key = new Integer (P_Employee_RWT_ID);
		P_Employee_RWT Employee_RWT = (P_Employee_RWT)s_cache.get(key);
		if (Employee_RWT != null)
			return Employee_RWT;
		Employee_RWT = new P_Employee_RWT (ctx, P_Employee_RWT_ID, trxName);
		s_cache.put (key, Employee_RWT);
		return Employee_RWT;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Employee_RWT>	s_cache = new CCache<Integer,P_Employee_RWT>("P_Employee_RWT", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Employee_RWT_ID id
	 */
	public P_Employee_RWT (Properties ctx, int P_Employee_RWT_ID, String trxName)
	{
		super (ctx, P_Employee_RWT_ID, trxName);
		if (P_Employee_RWT_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Employee_RWT

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Employee_RWT (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Employee_RWT (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Employee_RWT_ID"), trxName);
	}	//	P_Employee_RWT


	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true or false
	 */

	protected boolean beforeSave (boolean newRecord)
	{
		//+ 2011.07.05 + sécurité par compagnie.
 	    P_Employee Employee = P_Employee.get( Env.getCtx(), this.getP_Employee_ID(), this.get_TrxName());
		this.setAD_Org_ID( Employee.getAD_Org_ID());
		//- 2011.07.05 + sécurité par compagnie.

		return true;
	}	//	beforeSave
	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Employee_RWT[ID=")
			.append(getP_Employee_RWT_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	P_Employee_RWT
