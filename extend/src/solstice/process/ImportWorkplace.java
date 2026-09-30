package solstice.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import solstice.model.P_Workplace;
import solstice.model.X_I_Workplace;

public class ImportWorkplace extends SvrProcess
{
	/**	Client to be imported to		*/
	private int				m_AD_Client_ID = 0;
	/**	Delete old Imported				*/
	private boolean			m_deleteOldImported = false;

	/** Effective						*/
	private Timestamp		m_DateValue = null;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("AD_Client_ID"))
				m_AD_Client_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("DeleteOldImported"))
				m_deleteOldImported = "Y".equals(para[i].getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		if (m_DateValue == null)
			m_DateValue = new Timestamp (System.currentTimeMillis());
	}	//	prepare


	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception
	 */
	protected String doIt() throws java.lang.Exception
	{
		StringBuffer sql = null;
		int no = 0;
		String clientCheck = " AND AD_Client_ID=" + m_AD_Client_ID;

		//	Delete Old Imported
		if (m_deleteOldImported)
		{
		}
	
		int noInsert = 0;
		int noUpdate = 0;

		sql = new StringBuffer ("UPDATE i_workplace SET I_ISIMPORTED = 'Y' where exists ( select 1 from P_WorkPlace where P_WorkPlace.value = i_WorkPlace.value )" );  
		DB.executeUpdate(sql.toString(), null);

		//	Go through Records
		sql = new StringBuffer ("SELECT I_Workplace_ID FROM I_Workplace "
			+ "WHERE I_IsImported='N'").append(clientCheck);
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				X_I_Workplace imp = new X_I_Workplace (getCtx(), rs.getInt(1), null);

				P_Workplace Workplace = new P_Workplace( getCtx(), -1, null);
				Workplace.setValue( imp.getValue());
				Workplace.setName( imp.getName());
				Workplace.setIsActive(true);
				Workplace.setIsDefault(false);
				Workplace.setAD_Org_ID(0);
				Workplace.save();
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
			System.err.println( "error : " + e);
//			rollback();
		}

		//	Set Error to indicator to not imported
		sql = new StringBuffer ("UPDATE i_workplace "
			+ "SET I_IsImported='N', Updated=SysDate "
			+ "WHERE I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(sql.toString(), null);
		addLog (0, null, new BigDecimal (no), "@Errors@");
		addLog (0, null, new BigDecimal (noInsert), "@p_post_ID@: @Inserted@");
		addLog (0, null, new BigDecimal (noUpdate), "@p_post_ID@: @Updated@");


		return "";
	}

}
