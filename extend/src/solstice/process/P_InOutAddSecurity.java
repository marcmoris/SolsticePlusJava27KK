package solstice.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import solstice.model.P_Group_Employee;


public class P_InOutAddSecurity extends SvrProcess
{

	private int	m_inserted = 0;
	private int DistributionBooklet_ID = 0;
	private int Group_ID = 0;

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#prepare()
	 */
	protected void prepare() 
	{
        this.DistributionBooklet_ID = this.getRecord_ID();
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("P_Group_ID")) 
				Group_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	protected String doIt() throws Exception 
	{
		String sql = "Select P_Employee_ID FROM P_Employee "
			       + " Where P_Distribution_Booklet_ID = " + DistributionBooklet_ID
			       + " And IsActive = 'Y' "
			       ;

		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{

				P_Group_Employee GroupEmployee = P_Group_Employee.get( getCtx(), rs.getInt("P_Employee_ID"), Group_ID , null );
				if ( GroupEmployee == null )
				{
					GroupEmployee = new P_Group_Employee( getCtx(), -1 , null );
					GroupEmployee.setIsActive(true);
					GroupEmployee.setP_Employee_ID(rs.getInt("P_Employee_ID"));
					GroupEmployee.setP_Group_ID(Group_ID);
					GroupEmployee.save();
					m_inserted++;
					
				}
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, "doIt - " + sql, e);
		}

		return "@Inserted@ = " + m_inserted;

	}

	 
}
