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
 * via info@solsticeplus.com or http://www.solsticeplus.com/                  *
 ******************************************************************************/
package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;
  
/**
 *  ExpenseCorrection Model
 *
 *  @author Marc Morissette - Solstice plus
 */
public class P_TimeCorrection extends X_P_TimeCorrection
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get ExpenseCorrection
	 *	@param ctx context
	 * 	@param P_TimeCorrection_ID id
	 *	@return ExpenseCorrection
	 */
	public static P_TimeCorrection get (Properties ctx, int P_TimeCorrection_ID, String trxName)
	{
		Integer key = new Integer (P_TimeCorrection_ID);
		P_TimeCorrection ExpenseCorrection = (P_TimeCorrection)s_cache.get(key);
		if (ExpenseCorrection != null)
			return ExpenseCorrection;
		ExpenseCorrection = new P_TimeCorrection (ctx, P_TimeCorrection_ID, trxName);
		s_cache.put (key, ExpenseCorrection);
		return ExpenseCorrection;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_TimeCorrection>	s_cache = new CCache<Integer,P_TimeCorrection>("P_TimeCorrection", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_TimeCorrection_ID id
	 */
	public P_TimeCorrection (Properties ctx, int P_TimeCorrection_ID, String trxName)
	{
		super (ctx, P_TimeCorrection_ID, trxName);
		if (P_TimeCorrection_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_TimeCorrection

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_TimeCorrection (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_TimeCorrection (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_TimeCorrection_ID"), trxName);
	}	//	P_TimeCorrection


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_TimeCorrection[ID=")
			.append(this.getP_TimeCorrection_ID())
			.append(",Value=").append(getValue())
			.append(",Description=").append(getDescription())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	P_TimeCorrection
