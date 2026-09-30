/*
 * Created on 2005-10-24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_Job_Title_Annual_Salary extends X_P_Job_Title_Annual_Salary
{

    /**
     * @param ctx
     * @param P_Job_Title_Annual_Salary_ID
     * @param trxName
     */
    public P_Job_Title_Annual_Salary(Properties ctx,
            int P_Job_Title_Annual_Salary_ID, String trxName)
    {
        super(ctx, P_Job_Title_Annual_Salary_ID, trxName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param ctx
     * @param rs
     * @param trxName
     */
    public P_Job_Title_Annual_Salary(Properties ctx, ResultSet rs,
            String trxName)
    {
        super(ctx, rs, trxName);
        // TODO Auto-generated constructor stub
    }

}
