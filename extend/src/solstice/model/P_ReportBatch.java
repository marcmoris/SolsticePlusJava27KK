package solstice.model;

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

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  ReportBatch Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_ReportBatch extends X_P_ReportBatch
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get ReportBatch
	 *	@param ctx context
	 * 	@param P_ReportBatch_ID id
	 *	@return ReportBatch
	 */
	public static P_ReportBatch get (Properties ctx, int P_ReportBatch_ID, String trxName)
	{
		Integer key = new Integer (P_ReportBatch_ID);
		P_ReportBatch ReportBatch = (P_ReportBatch)s_cache.get(key);
		if (ReportBatch != null)
			return ReportBatch;
		ReportBatch = new P_ReportBatch (ctx, P_ReportBatch_ID, trxName);
		s_cache.put (key, ReportBatch);
		return ReportBatch;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_ReportBatch>	s_cache = new CCache<Integer,P_ReportBatch>("P_ReportBatch", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_ReportBatch_ID id
	 */
	public P_ReportBatch (Properties ctx, int P_ReportBatch_ID, String trxName)
	{
		super (ctx, P_ReportBatch_ID, trxName);
		if (P_ReportBatch_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_ReportBatch

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_ReportBatch (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_ReportBatch (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_ReportBatch_ID"), trxName);
	}	//	P_ReportBatch


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_ReportBatch[ID=")
			.append(this.getP_ReportBatch_ID())
//			.append(",Value=").append(getValue())
			.append(",Name=").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	P_ReportBatch
