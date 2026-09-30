package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.DB;

public class P_Group_Employee extends X_P_Group_Employee
{

    public P_Group_Employee(Properties ctx, int P_Group_Employee_ID,
            String trxName)
    {
        super(ctx, P_Group_Employee_ID, trxName);
    }

    public P_Group_Employee(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
    }



	/**
	 * 	Get Form
	 *	@param ctx context
	 * 	@param C_Form_ID id
	 *	@return Form
	 */
	public static P_Group_Employee get (Properties ctx, int P_Group_Employee_ID, String trxName)
	{
		Integer key = new Integer (P_Group_Employee_ID);
		P_Group_Employee Group_Employee = (P_Group_Employee)s_cache.get(key);
		if (Group_Employee != null)
			return Group_Employee;
		Group_Employee = new P_Group_Employee (ctx, P_Group_Employee_ID, trxName);
		s_cache.put (key, Group_Employee);
		return Group_Employee;
	}	//	get

	private static CCache<Integer,P_Group_Employee>	s_cache = new CCache<Integer,P_Group_Employee>("P_Group_Employee", 2);
    
	
	public static P_Group_Employee get( Properties ctx, int employeeID, int groupID, String trxName)
	{
		int id = 0;
		String sql = "Select P_Group_Employee_ID From P_Group_Employee "
			       + " Where P_Employee_ID = " + employeeID
				   + "   And P_Group_ID = " + groupID
				   ;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				id = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("P_Group_Employee get - " + e);
		}
		
		if ( id == 0 ) 
			return null;
		
		return P_Group_Employee.get( ctx, id , trxName);
		
	}


}
