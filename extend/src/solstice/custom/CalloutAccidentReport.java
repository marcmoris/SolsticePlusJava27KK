/*
 * Created on 13 juin 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.custom;

import org.compiere.model.GridField;
import org.compiere.model.GridTab;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.sql.Timestamp;

import org.compiere.model.CalloutEngine;
import org.compiere.util.DB;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CalloutAccidentReport extends CalloutEngine
{
    /**
     * Lorsque l'assignation change, on doit affecter les valeurs par défaut
     * dans les champs Post, Job Title, Occupation Group et JobType
     */
    public String assignment_changed (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
    {

       if(value!=null)
       {
           setCalloutActive(true);
           int id = ((Integer)value).intValue();
           Timestamp dateAccident = (Timestamp)mTab.getValue("AccidentReport");
           
          String sql 
          	= " select P_ASSIGNMENT.P_POST_ID, P_ASSIGNMENT_PARAM.P_JOB_TITLE_ID, "
            + " P_ASSIGNMENT_PARAM.P_OCCUPATION_GROUP_ID, P_EMPLOYEE.P_JOB_TYPE_ID"
            + " from P_ASSIGNMENT inner join P_EMPLOYEE"
            + " on P_ASSIGNMENT.P_EMPLOYEE_ID = P_EMPLOYEE.P_EMPLOYEE_ID"
            + " inner join P_ASSIGNMENT_PARAM on P_ASSIGNMENT.P_ASSIGNMENT_ID = P_ASSIGNMENT_PARAM.P_ASSIGNMENT_ID "
            + " where P_ASSIGNMENT.P_ASSIGNMENT_ID = " + id
            + " AND P_ASSIGNMENT_PARAM.EffectIn <= ?";
          
          try
          {
             PreparedStatement pstmt = DB.prepareStatement(sql, null);

             pstmt.setTimestamp(1, dateAccident);
             ResultSet rs = pstmt.executeQuery();
             if (rs.next())
             {
                mTab.setValue("P_POST_ID", rs.getBigDecimal(1));
                mTab.setValue("P_JOB_TITLE_ID", rs.getBigDecimal(2));
                mTab.setValue("P_OCCUPATION_GROUP_ID", rs.getBigDecimal(3));
                mTab.setValue("P_JOB_TYPE_ID", rs.getBigDecimal(4));
             }
             rs.close();
             pstmt.close();
          }
          catch (SQLException e)
          {
             log.log(Level.SEVERE, "occupation group", e);
             return e.getLocalizedMessage();
          }
       }

       return "";
    } 
    
    /**
     * Lorsqu'on change d'employé, on remet la liste des assignations à null
     */
    public String employee_changed (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
    {
        mTab.setValue("P_Assignment_ID", null);
        return "";
    }
}
