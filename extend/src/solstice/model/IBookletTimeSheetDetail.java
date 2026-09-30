/*
 * Created on 2005-09-15
 */
package solstice.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author frafor01
 *
 * Cet interface a été définit pour représenter les méthodes communes
 * de P_Booklet_Detail et de P_Time_Sheet_Detail
 */
public interface IBookletTimeSheetDetail
{
    /*
     * Setters
     */
    public void setC_Activity_ID(int c_activity_id);
    public void setC_Campaign_ID(int c_campaign_id);
    public void setC_Project_ID(int c_project_id);
    public void setDay(Timestamp day);
    public void setDayQty(BigDecimal dayqty);
    public void setIsActive(boolean isactive);
    public void setOriginTime(String origintime);
    public void setP_Assignment_ID(int p_assignment_id);
    public void setRecord_ID(int record_ID);
    public void setParent_ID(int parent_ID);
    public void setP_Employee_LongTermLeave_ID(int p_employee_longtermleave_id);
    public void setP_Gain_ID(int p_gain_id);
    public void setP_Period_ID(int p_period_id);
    public void setP_Schedule_ID(int p_schedule_id);
    public void setSalaryPercentage(BigDecimal salarypercentage);
    public void setWeekIndex(int weekindex);
    public void setStartDate(Timestamp startDate);
    public void setEndDate(Timestamp endDate);
    public void setHourly_Rate( BigDecimal Hourly_Rate);
    public void setP_Timecode_ID (int P_Timecode_ID);
    public void setStartTime (String StartTime);
    public void setEndTime (String EndTime);
    
    /*
     * Getters
     */
    public int getAD_Client_ID();
    public int getAD_Org_ID();
    public int getC_Activity_ID();
    public int getC_Campaign_ID();
    public int getC_Project_ID();
    public Timestamp getCreated();
    public int getCreatedBy();
    public Timestamp getDay();
    public BigDecimal getDayQty();
    public boolean isActive();
    public String getOriginTime();
    public int getP_Assignment_ID();
    public int getRecord_ID();
    public int getParent_ID();
    public int getP_Employee_LongTermLeave_ID();
    public int getP_Gain_ID();
    public int getP_Period_ID();
    public int getP_Schedule_ID();
    public BigDecimal getSalaryPercentage();
    public Timestamp getUpdated();
    public int getUpdatedBy();
    public int getWeekIndex();
    public Timestamp getStartDate();
    public Timestamp getEndDate();
    public BigDecimal getHourly_Rate();
    public int getP_Timecode_ID();
	public String getStartTime(); 
	public String getEndTime(); 

    
    /*
     * PO
     */
    
    public boolean save();
    public boolean delete (boolean force, String trxName);
    public String get_TableName();
}
