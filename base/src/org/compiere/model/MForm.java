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
import org.compiere.util.*;


/**
 *	Form Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MForm.java,v 1.1 2007/07/18 14:39:16 marmor01 Exp $
 */
public class MForm extends X_AD_Form
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param AD_Form_ID id
	 *	@param trxName transaction
	 */
	public MForm (Properties ctx, int AD_Form_ID, String trxName)
	{
		super (ctx, AD_Form_ID, trxName);
	}	//	MForm

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MForm (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MForm
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (newRecord)
		{
			int AD_Role_ID = Env.getAD_Role_ID(getCtx());
			MFormAccess pa = new MFormAccess(this, AD_Role_ID);
			pa.save();
		}
		return success;
	}	//	afterSave

	//+ Progestion Solstice
	//
	// Return Name with Translation
	// 
	public String getTrlName( String language )
	{
		Language baseLanguage = Language.getBaseLanguage();
		
		if ( language.equals( baseLanguage.getAD_Language()  )||  language.equals( "en_CA") )
			return getName();
		return get_Translation("Name", language );
	}	//	getTrlName
	
	//- Progestion Solstice

}	//	MForm
