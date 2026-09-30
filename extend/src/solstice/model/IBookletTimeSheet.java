/*
 * Created on 2005-09-15
 */
package solstice.model;

import java.sql.Timestamp;

/**
 * @author frafor01
 *
 * Cet interface a été définit pour représenter les méthodes communes
 * de P_Booklet et de P_Time_Sheet
 */
public interface IBookletTimeSheet
{ 
    /*
     * Setters
     */
    public void setAD_Org_ID(int ad_org_id);
    public void setP_Department_ID(int p_department_id);
    public void setC_Activity_ID(int c_activity_id);
    public void setDescription(String description);
    public void setIsActive(boolean isactive);
    public void setIsComplete(boolean iscomplete);
    public void setIsError(boolean iserror);
    public void setIsWarning(boolean iswarning);
    public void setRecord_ID(int recordId);
    public void setP_Distribution_Booklet_ID(int p_distribution_booklet_id);
    public void setP_Distribution_ID(int p_distribution_id);
    public void setP_Employee_ID(int p_employee_id);
    public void setP_Frequency_ID(int p_frequency_id);
    public void setP_Job_Title_ID(int p_job_title_id);
    public void setP_Job_Type_ID(int p_job_type_id);
    public void setP_Occupation_Group_ID(int p_occupation_group_id);
    public void setP_Payment_Group_ID(int p_payment_group_id);
    
    public void setP_Employer_ID(int p_employer_id);
    public void setP_Workplace_ID(int p_workplace_id);

    public void setP_Collective_Labour_Agr_ID( int p_collective_labour_agr );
    public void setP_Payment_ID(int p_payment_id);
    public void setP_Period_ID(int p_period_id);
    public void setP_Year_ID(int p_year_id);
    public void setPaymentType(String paymenttype);
    public void setSheetType(String sheettype);
    public void setTimeSheetStatus(String timesheetstatus);
    public void setValue(String value);
    public void setPayDate( Timestamp PayDate);
    /*
     * Getters
     */
    public int getAD_Client_ID();
    public int getAD_Org_ID();
    public int getP_Department_ID();
    public int getC_Activity_ID();

    public Timestamp getCreated();
    public int getCreatedBy();
    public String getDescription();
    public boolean isActive();
    public boolean isComplete();
    public boolean isError();
    public boolean isWarning();
    public int getRecord_ID();
    public int getP_Distribution_Booklet_ID();
    public int getP_Distribution_ID();
    public int getP_Employee_ID();
    public int getP_Frequency_ID();
    public int getP_Job_Title_ID();
    public int getP_Job_Type_ID();
    public int getP_Occupation_Group_ID();
    public int getP_Payment_Group_ID();
    public int getP_Payment_ID();
    public int getP_Period_ID();
    public int getP_Year_ID();
    public String getPaymentType();
    public String getSheetType();
    public String getTimeSheetStatus();
    public Timestamp getUpdated();
    public int getUpdatedBy();
    public String getValue();
    
    /*
     * PO
     */
    
    public String get_TableName();
    public String get_TrxName();
    public boolean save();
    
    public boolean delete (boolean force);
}
