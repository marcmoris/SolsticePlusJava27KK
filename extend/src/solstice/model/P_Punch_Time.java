package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.DB;
import org.compiere.util.Env;

public class P_Punch_Time extends X_P_Punch_Time
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public P_Punch_Time(Properties ctx, int P_Group_ID, String trxName)
    {
        super(ctx, P_Group_ID, trxName);
    }

    public P_Punch_Time(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
    }

    protected boolean beforeSave (boolean newRecord)
	{

    	P_Period Period = P_Period.get( Env.getCtx(), this.getP_Period_ID(), null);
		if ( this.getDay().before( Period.getStartDate()) || this.getDay().after( Period.getEndDate()) )
		{
			log.saveError("ValidationError", "Date non inclus dans dans la période de paie ");
			return false;
		}
    	
		
  		String sql = "Select P_Schedule_ID FROM P_Distribution_Schedule WHERE P_Post_ID =  " + this.getP_Post_ID()
  		           + " AND P_Distribution_ID = " + this.getP_Distribution_ID()
  		           ;
        PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement(sql, null);

		try{
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				this.setP_Schedule_ID( rs.getInt("P_Schedule_ID") );
			}
		}
		catch (Exception e)
		{
			 log.log(Level.SEVERE, e.toString());
		}
  		
		if ( this.getP_Schedule_ID() == 0 || this.getP_Schedule_ID() == 100000)
		{
	    	P_Distribution Distribution = P_Distribution.get( Env.getCtx(), this.getP_Distribution_ID(), null);
	    	this.setP_Schedule_ID( Distribution.getP_Schedule_ID() );
		}
		
    	return true;
	}

    
}
