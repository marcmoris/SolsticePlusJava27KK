/*
 * Created on 2005-10-07
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * @author alenav01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_FillGainToExcludeTable extends SvrProcess {

	private int P_Frequency_ID=0;
	private int P_Period_ID=0;
	private int P_Distribution_Booklet_ID=0;
	private int P_Gain_ID=0;
	private int P_GainInfo_ID=0;
	private String ClassErrorMessage ="ERROR FILLING TABLE P_T_GAIN_TO_EXCLUDE";
		
	private String m_Format = "PDF";

	/**
	 * 
	 */
	public P_FillGainToExcludeTable() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#prepare()
	 */
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		String paramName = "";
		
		//Read the parameters
		for(int i=0; i < para.length; i++){
			paramName = para[i].getParameterName();
			if( paramName.equals("P_Frequency_ID")){
				this.P_Frequency_ID = ((Number)para[i].getParameter()).intValue();
			}else if (paramName.equals("P_Period_ID")){
				this.P_Period_ID = ((Number)para[i].getParameter()).intValue();
			}else if (paramName.equals("P_Distribution_Booklet_ID")){
				this.P_Distribution_Booklet_ID = ((Number)para[i].getParameter()).intValue();
			}else if (paramName.equals("P_Gain_ID")){
				this.P_Gain_ID = ((Number)para[i].getParameter()).intValue();
			}else if (paramName.equals("P_GainInfo_ID")){
				this.P_GainInfo_ID = ((Number)para[i].getParameter()).intValue();
			}
		}	
	}
	
	private void StartToFillTable(int P_Period_ID)
	{
		PreparedStatement pstmt = null;
		try
		{
		
			pstmt = DB.prepareStatement("EXEC SP_P_FILL_TABLE_P_T_GAIN_TO_EXCLUDE " + this.P_Period_ID + "," + this.P_Gain_ID, null);
			pstmt.execute();
			
		}
		catch (Exception e)
		{
			System.err.println(ClassErrorMessage + " [StartToFillTable] " + e);
		}
	} 
	
	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	protected String doIt() throws Exception {
		
		StartToFillTable(this.P_Period_ID);
		int AD_Pinstance_ID = this.getAD_PInstance_ID();
	        
		int l_Role = Env.getAD_Role_ID(Env.getCtx());
        ReportLauncher.callReport( AD_Pinstance_ID, l_Role, m_Format, getParameter() );


	    return "";
	}
	

}
