/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is Compiere ERP & CRM Smart Business Solution. The Initial
 * Developer of the Original Code is Jorg Janke. Portions created by Jorg Janke
 * are Copyright (C) 1999-2005 Jorg Janke.
 * All parts are Copyright (C) 1999-2005 ComPiere, Inc.  All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package solstice.process;

import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/**
 *	Create Periods of year
 *	
 *  @author Jorg Janke
 *  @version $Id: YearCreatePeriods.java,v 1.1 2007/07/18 14:43:44 marmor01 Exp $
 */
public class YearCreatePeriods extends SvrProcess
{
	private int	p_C_Year_ID = 0;
	private int AD_Pinstance_ID=0;
	private String FirstMonth = ""; 
	
	/**
	 * 	Prepare
	 */
	//protected void prepare ()
	//{
	//	p_C_Year_ID = getRecord_ID();
	//}	//	prepare
	protected void prepare() 
	{
		ProcessInfoParameter[] para = getParameter();
		String paramName = "";
		AD_Pinstance_ID = this.getAD_PInstance_ID();
		p_C_Year_ID = getRecord_ID();
		//Read the parameters
		for(int i=0; i < para.length; i++)
		{ 
			paramName = para[i].getParameterName();
			if( paramName.equals("StartMonth"))
				this.FirstMonth = String.valueOf(para[i].getParameter()); 
		}
	}

	/**
	 * 	Process
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt ()
		throws Exception
	{
		MYear year = new MYear (getCtx(), p_C_Year_ID, get_TrxName());
		if (p_C_Year_ID == 0 || year.getC_Year_ID() != p_C_Year_ID)
			throw new CompiereUserError ("@NotFound@: @C_Year_ID@ - " + p_C_Year_ID);
		log.info(year.toString());
		//
/*		if (year.createStdPeriods(null, FirstMonth))
			return "@OK@";
*/			
		return "@Error@";
	}	//	doIt
	
}	//	YearCreatePeriods
