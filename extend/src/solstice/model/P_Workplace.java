package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;
import org.compiere.model.MSalesRegion;

/**
 *  Workplace Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Workplace extends X_P_Workplace
{
	/**
	 * 	Get Workplace
	 *	@param ctx context
	 * 	@param P_Workplace_ID id
	 *	@return Workplace
	 */
	public static P_Workplace get (Properties ctx, int P_Workplace_ID, String trxName)
	{
		Integer key = new Integer (P_Workplace_ID);
		P_Workplace Workplace = (P_Workplace)s_cache.get(key);
		if (Workplace != null)
			return Workplace;
		Workplace = new P_Workplace (ctx, P_Workplace_ID, trxName);
		s_cache.put (key, Workplace);
		return Workplace;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Workplace>	s_cache = new CCache<Integer,P_Workplace>("P_Workplace", 100);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Workplace_ID id
	 */
	public P_Workplace (Properties ctx, int P_Workplace_ID, String trxName)
	{
		super (ctx, P_Workplace_ID, trxName);
		if (P_Workplace_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Workplace

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Workplace (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Workplace (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Workplace_ID"), trxName);
	}	//	P_Workplace

	
	//
	// Personnalisation pour Helicoptère Canadien
	// Création automatique de l'élément du plan comptable "secteur" 
	//
	protected boolean beforeSave (boolean newRecord)
	{
	    
	    // TODO Paramètriser dans une table.
	    if ( this.getAD_Client_ID() != 11)
	    {
	    	MSalesRegion Region;
	    	if (newRecord || this.getC_SalesRegion_ID() == 0 )
	    	{
		    	Region = new MSalesRegion( getCtx(), -1, null);   
	    	}
	    	else
	    	{
		    	Region = new MSalesRegion( getCtx(), this.getC_SalesRegion_ID(), null);   
	    	}
	    	Region.setAD_Org_ID(0);
	    	Region.setName( this.getName());
	    	Region.setDescription(this.getDescription());
	    	Region.setValue( this.getValue());
	    	Region.setIsActive( this.isActive());
	    	Region.setIsSummary(false);
	    	Region.setIsDefault(this.isDefault());
	    	Region.save();
	    	
	    	this.setC_SalesRegion_ID( Region.getC_SalesRegion_ID());
	    }
	    
	    return true;

	
	}

	/**
	 * 	After Delete
	 *	@param success
	 *	@return
	 */
	protected boolean afterDelete (boolean success)
	{
		if (!success )
		{
			return success;
		}
		
	    // TODO Paramètriser dans une table.
	    if ( this.getAD_Client_ID() != 11)
    	{
    		MSalesRegion Region = new MSalesRegion( getCtx(), this.getC_SalesRegion_ID(), null);
    		Region.delete(true);
    	}
		
		return success;
	}
	

}	//	P_Workplace
