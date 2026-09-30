package solstice.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

public class ExportLabelPerso extends SvrProcess
{
	/**	Client to be imported to		*/
	private int				m_AD_Client_ID = 0;

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
		String clientCheck = " AND AD_LblPerso.AD_Client_ID=" + m_AD_Client_ID;


		sql = new StringBuffer ("UPDATE I_Employee set Taxation_Region_ID = 164  where TaxationregionName = '1'" );  
		DB.executeUpdate(sql.toString(), null);
		commit();


		//	Go through Records
		sql = new StringBuffer ("select AD_LblPerso.ad_element_id, AD_LblPerso.ad_window_id, isnull( AD_LblPerso.name, '' ) as name, isnull( AD_LblPerso.printname, '' ) as printname, isnull(AD_LblPerso.description, '' ) as description, isnull( AD_LblPerso.help, '') as help, "
								+ " isnull( AD_LblPerso_Trl.name, '') as name_trl, isnull(AD_LblPerso_Trl.printname, '') as printname_trl, isnull(AD_LblPerso_Trl.description, '') as description_trl, isnull( AD_LblPerso_Trl.help, '') as help_trl "  
								+ " FROM AD_LblPerso, AD_LblPerso_Trl" 
								+ " where AD_LblPerso.AD_LblPerso_id = AD_LblPerso_Trl.AD_LblPerso_id").append(clientCheck);
	
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				if(rs.getBigDecimal("ad_element_id") != null)
				{
					sql = new StringBuffer 
						("UPDATE  AD_Element"
							+ " SET"
							+ " AD_Element.name = '" + rs.getString("name") + "',"
							+ " AD_Element.Printname =  '" + rs.getString("printname") + "'," 
							+ " AD_Element.description =  '" + rs.getString("description") + "',"
							+ "	AD_Element.help =  '" + rs.getString("help") + "'"
							+ "	where AD_Element.ad_element_id = " + rs.getBigDecimal("ad_element_id"));  
					DB.executeUpdate(sql.toString(), null);
					commit();
					
					sql = new StringBuffer 
					("UPDATE  AD_Element_Trl"
						+ " SET"
						+ " AD_Element_Trl.name = '" + rs.getString("name_trl").replace( "'", "''") + "',"
						+ " AD_Element_Trl.Printname =  '" + rs.getString("printname_trl").replace( "'", "''") + "'," 
						+ " AD_Element_Trl.description =  '" + rs.getString("description_trl").replace( "'", "''") + "',"
						+ "	AD_Element_Trl.help =  '" + rs.getString("help_trl").replace( "'", "''") + "'"
						+ "	where AD_Element_Trl.ad_element_id = " + rs.getBigDecimal("ad_element_id"));  
					DB.executeUpdate(sql.toString(), null);
					commit();
				}
				else if(rs.getBigDecimal("ad_window_id") != null)
				{
					sql = new StringBuffer 
					("UPDATE  AD_Window"
						+ " SET"
						+ " AD_Window.name = '" + rs.getString("name") + "',"
						+ " AD_Window.description =  '" + rs.getString("description") + "',"
						+ "	AD_Window.help =  '" + rs.getString("help") + "'"
						+ "	where AD_Window.AD_Window_id = " + rs.getBigDecimal("ad_window_id"));  
					DB.executeUpdate(sql.toString(), null);
					commit();
					
					sql = new StringBuffer 
					("UPDATE  AD_Window_trl"
						+ " SET"
						+ " AD_Window_trl.name = '" + rs.getString("name_trl") + "',"
						+ " AD_Window_trl.description =  '" + rs.getString("description_trl") + "',"
						+ "	AD_Window_trl.help =  '" + rs.getString("help_trl") + "'" 
						+ "	where AD_Window_trl.AD_Window_id = " + rs.getBigDecimal("ad_window_id"));  
					DB.executeUpdate(sql.toString(), null);
					commit();				
				}
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
		
		return "";
		
	}

	
}
