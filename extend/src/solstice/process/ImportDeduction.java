package solstice.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import solstice.model.P_Deduction;
import solstice.model.X_I_Deduction;

public class ImportDeduction extends SvrProcess
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
/*			sql = new StringBuffer ("delete from p_Deduction where p_Deduction_id in ( select p_Deduction_id from i_Deduction ) ").append(clientCheck);
			no = DB.executeUpdate(sql.toString(), null);
			log.fine("Delete Old Impored =" + no);
*/
			sql = new StringBuffer ("update i_Deduction set I_IsImported = 'N' ");
			no = DB.executeUpdate(sql.toString(), null);

		}

		//	Set Client, Org, IsActive, Created/Updated
		sql = new StringBuffer ("UPDATE i_Deduction "
			+ "SET AD_Client_ID = COALESCE (AD_Client_ID, ").append(m_AD_Client_ID).append("),"
			+ " AD_Org_ID = COALESCE (AD_Org_ID, 0),"
			+ " IsActive = COALESCE (IsActive, 'Y'),"
			+ " Created = COALESCE (Created, SysDate),"
			+ " CreatedBy = COALESCE (CreatedBy, 0),"
			+ " Updated = COALESCE (Updated, SysDate),"
			+ " UpdatedBy = COALESCE (UpdatedBy, 0),"
			+ " I_ErrorMsg = NULL,"
			+ " I_IsImported = 'N' "
			+ "WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
		no = DB.executeUpdate(sql.toString(), null);
		log.fine("Reset=" + no);

//		commit();
		//	-------------------------------------------------------------------
		int noInsert = 0;
		int noUpdate = 0;

		sql = new StringBuffer ("UPDATE I_Deduction set value = dbo.lpad( value, 2, '0' ) " );  
		DB.executeUpdate(sql.toString(), null);
		commit();

		sql = new StringBuffer ("UPDATE I_Deduction set name_en = dbo.initcap( name_en  )" );  
		DB.executeUpdate(sql.toString(), null);
		commit();

		sql = new StringBuffer ("UPDATE I_Deduction set name_fr = dbo.initcap( name_fr  )" );  
		DB.executeUpdate(sql.toString(), null);
		commit();
		


		sql = new StringBuffer ("UPDATE I_Deduction set P_Deduction_ID = ( Select max(P_Deduction_ID) from p_Deduction where P_Deduction.value = I_Deduction.value  and ad_client_id <> 11 )" );  
		DB.executeUpdate(sql.toString(), null);
		commit();

		
		//	Go through Records
		sql = new StringBuffer ("SELECT i_Deduction_ID FROM i_Deduction "
			+ "WHERE P_Deduction_ID is not null and  I_IsImported='N'").append(clientCheck);
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{

//				sql = new StringBuffer ("UPDATE i_Deduction set Taxation_Region_ID = 166,  C_Region_ID = 166 WHERE C_Region_ID is null" );  
//				DB.executeUpdate(sql.toString(), null);

				X_I_Deduction imp = new X_I_Deduction (getCtx(), rs.getInt(1), null);

				
				boolean isError = false;
				if ( ! isError )
				{
					P_Deduction Deduction = new P_Deduction( getCtx(), imp.getP_Deduction_ID(), null);
					
					Deduction.updateTranslation("fr_CA", imp.getname_fr(), "");

					Deduction.setName(imp.getname_en());
					Deduction.save();

				}
				commit();
				
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
			System.err.println( "error : " + e);
//			rollback();
		}
		
		//	Set Error to indicator to not imported
		sql = new StringBuffer ("UPDATE i_Deduction "
			+ "SET I_IsImported='N', Updated=SysDate "
			+ "WHERE I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(sql.toString(), null);
		addLog (0, null, new BigDecimal (no), "@Errors@");
		addLog (0, null, new BigDecimal (noInsert), "@p_Deduction_ID@: @Inserted@");
		addLog (0, null, new BigDecimal (noUpdate), "@p_Deduction_ID@: @Updated@");


		return "";
		
	}


}
