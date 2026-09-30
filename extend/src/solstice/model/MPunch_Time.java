package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 *  MPunch_Time Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class MPunch_Time extends X_RV_Punch_Time
{

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MPunch_Time (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	



}	//	MPunch_Time
