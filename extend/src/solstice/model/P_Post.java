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
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.model.MOrg;
import org.compiere.model.MOrgInfo;
import org.compiere.util.CCache;
import org.compiere.util.Env;


/**
 *  Post Model
 *
 *  @author Jorg Janke
 *  @version $Id: P_Post.java,v 1.1 2007/07/18 14:43:29 marmor01 Exp $
 */
public class P_Post extends X_P_Post
{
	/**
	 * 	Get Post
	 *	@param ctx context
	 * 	@param P_Post_ID id
	 *	@return Post
	 */
	public static P_Post get (Properties ctx, int P_Post_ID, String trxName)
	{
		Integer key = new Integer (P_Post_ID);
		P_Post Post = (P_Post)s_cache.get(key);
		if (Post != null)
			return Post;
		Post = new P_Post (ctx, P_Post_ID, trxName);
		s_cache.put (key, Post);
		return Post;
	}	//	get


	/**	Cache						*/
	private static CCache<Integer,P_Post>	s_cache = new CCache<Integer,P_Post>("P_Post", 100);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Post_ID id
	 */
	public P_Post (Properties ctx, int P_Post_ID, String trxName)
	{
		super (ctx, P_Post_ID, trxName);
		if (P_Post_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Post

	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Post (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Post_ID"),trxName);
	}	//	P_Post

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Post (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	


	/** Get Hourly Rate.
	  */
	/*public BigDecimal getHourly_Rate() 
	{
	
	  		//TODO
   		//annualite
   		BigDecimal bdAnnuality = Env.ZERO;

		X_P_Post_Remuneration PostRemuneration = new X_P_Post_Remuneration(getCtx (), getP_Post_ID() ) ;
		
		if ( PostRemuneration.isOut_Of_Range() )
			return  PostRemuneration.getHourly_Rate();
		else
		{
			X_P_Salary_Scale        Salary_Scale        = new X_P_Salary_Scale       ( getCtx (),	PostRemuneration.getP_Salary_Scale_ID() );
			X_P_Salary_Scale_Detail Salary_Scale_Detail = new X_P_Salary_Scale_Detail( getCtx (),	PostRemuneration.getP_Salary_Scale_Detail_ID() );
			if ( Salary_Scale.getRemuneration_Method().compareTo( "hourly" ) == 0 )
				return Salary_Scale_Detail.getHourly_Rate();
			else
			{
				return Salary_Scale_Detail.getAnnual_Salary().divide( PostRemuneration.getWeekly_Hours(), BigDecimal.ROUND_UNNECESSARY ).divide( new BigDecimal(52), 2, BigDecimal.ROUND_HALF_UP);	
			}
		}
		
	}*/
	
	
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
	    
/*		if (newRecord)
			insert_Tree(MTree_Base.TREETYPE_Post);
*/
	    
	    if ( newRecord && this.getC_Activity_ID() != 0 || (is_ValueChanged("P_Workplace_ID")) )
	    {
	    	P_Workplace workplace = P_Workplace.get( Env.getCtx(), this.getP_Workplace_ID(),  this.get_TrxName());
	    	
			P_Post_Distribution distribution = null;
			distribution = P_Post_Distribution.getWithPostID( Env.getCtx(), this.getP_Post_ID(), this.get_TrxName());
			if ( distribution == null )
			{
				distribution = new P_Post_Distribution( Env.getCtx(), -1, this.get_TrxName());
				distribution.setP_Post_ID( this.getP_Post_ID());
				distribution.setC_Activity_ID( this.getC_Activity_ID() );
				distribution.setOrg_ID( this.getAD_Org_ID());
				distribution.setC_SalesRegion_ID( workplace.getC_SalesRegion_ID() );
				distribution.setIsActive(true);
				distribution.setLine( Env.ONE);
				distribution.setRatio( new BigDecimal( 100 ) );
				distribution.save( );
			}
	    	
	    }
	    
		return success;
		
	}	//	afterSave


	/**
	 * 	After Delete
	 *	@param success
	 *	@return deleted
	 */
	protected boolean afterDelete (boolean success)
	{
/*		if (success)
			delete_Tree(MTree_Base.TREETYPE_Post);
*/			
		return success;
	}	//	afterDelete

	/**
	 * 	Get Translated Name
	 *	@return name
	 */
	public String getTrlName()
	{
		if (Env.isBaseLanguage(Env.getCtx(), "AD_Ref_List"))
			return getName();
		return get_Translation("Name", Env.getAD_Language(Env.getCtx()));
	}	//	getTrlName
	
	/**
	 * 	Get Translated Name
	 *	@return name
	 */
	public String getTrlDescription()
	{
		if (Env.isBaseLanguage(Env.getCtx(), "AD_Ref_List"))
			return getDescription();
		return get_Translation("Description", Env.getAD_Language(Env.getCtx()));
	}	//	getTrlDescription


	public String getPhone() 
	{
		String phone = (String)get_Value("Phone");
		
    	MOrgInfo OrgInfo = MOrgInfo.get( getCtx(),Env.getAD_Org_ID( getCtx() ), null);
    	
		if ( phone == null )
			phone = OrgInfo.getPhone();
		
		return phone ;
	}


	
}	//	P_Post
