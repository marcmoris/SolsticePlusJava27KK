package solstice.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.model.MLocation;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import solstice.model.*;

public class ImportPost extends SvrProcess
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
			sql = new StringBuffer ("delete from p_post where p_post_id in ( select p_post_id from i_post ) ").append(clientCheck);
			no = DB.executeUpdate(sql.toString(), null);
			log.fine("Delete Old Impored =" + no);

			sql = new StringBuffer ("update i_post set I_IsImported = 'N' ");
			no = DB.executeUpdate(sql.toString(), null);

		}

		//	Set Client, Org, IsActive, Created/Updated
		sql = new StringBuffer ("UPDATE i_post "
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

		sql = new StringBuffer ("UPDATE I_Post set value = dbo.lpad( value, 3, '0' ) " );  
		DB.executeUpdate(sql.toString(), null);
		commit();

		
		//	Go through Records
		sql = new StringBuffer ("SELECT i_post_ID FROM i_post "
			+ "WHERE I_IsImported='N'").append(clientCheck);
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{

//				sql = new StringBuffer ("UPDATE i_post set Taxation_Region_ID = 166,  C_Region_ID = 166 WHERE C_Region_ID is null" );  
//				DB.executeUpdate(sql.toString(), null);

				X_I_Post imp = new X_I_Post (getCtx(), rs.getInt(1), null);

				
				boolean isError = false;
				if ( ! isError )
				{
					P_Post Post = new P_Post( getCtx(), -1, null);
					Post.setValue( imp.getValue());
					Post.setName(imp.getname_fr());
					Post.setIsActive(true);
					Post.setAD_Org_ID(0);
					Post.setP_Collective_Labour_Agr_ID(1000060);
					Post.save();

					Post.setName(imp.getname_en());
					Post.save();

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
		sql = new StringBuffer ("UPDATE i_post "
			+ "SET I_IsImported='N', Updated=SysDate "
			+ "WHERE I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(sql.toString(), null);
		addLog (0, null, new BigDecimal (no), "@Errors@");
		addLog (0, null, new BigDecimal (noInsert), "@p_post_ID@: @Inserted@");
		addLog (0, null, new BigDecimal (noUpdate), "@p_post_ID@: @Updated@");


		return "";
		
	}



}
