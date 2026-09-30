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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  Form_Param Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Form_Param extends X_P_Form_Param
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get Form_Param
	 *	@param ctx context
	 * 	@param P_Form_Param_ID id
	 *	@return Form_Param
	 */
	public static P_Form_Param get (Properties ctx, int P_Form_Param_ID, String trxName)
	{
		Integer key = new Integer (P_Form_Param_ID);
		P_Form_Param Form_Param = (P_Form_Param)s_cache.get(key);
		if (Form_Param != null)
			return Form_Param;
		Form_Param = new P_Form_Param (ctx, P_Form_Param_ID, trxName);
		s_cache.put (key, Form_Param);
		return Form_Param;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Form_Param>	s_cache = new CCache<Integer,P_Form_Param>("P_Form_Param", 20);

	private static CLogger		s_log = CLogger.getCLogger (P_Form_Param.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Form_Param_ID id
	 */
	public P_Form_Param (Properties ctx, int P_Form_Param_ID, String trxName)
	{
		super (ctx, P_Form_Param_ID, trxName);
		if (P_Form_Param_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Form_Param

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Form_Param (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Form_Param (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Form_Param_ID"), trxName);
	}	//	P_Form_Param


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Form_Param[ID=")
	//		.append(this.getP_Form_Param_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString

	
	
	public static P_Form_Param get( Properties ctx, String Typ, String trxName  ) 
	{
		String sql = null;
		sql = "Select P_Form_Param_ID from P_Form_Param WHERE Type = '" + Typ + "'" ;
		//
		
		P_Form_Param FormParam = null;
		
		PreparedStatement pstmt = null;
		
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				FormParam = P_Form_Param.get( ctx, rs.getInt( "P_Form_Param_ID"), trxName);
			}
			
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Form_Param - " + sql, e);
		}

		return FormParam;
	}

	
}	//	P_Form_Param
