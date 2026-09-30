/*
 * Created on 28 juin 2005
 */
package solstice.process;

import java.awt.Frame;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.Level;
import java.math.BigDecimal;
import javax.swing.JFileChooser;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import solstice.model.P_Employee;


/**
 * @author rejgar01
 *
 * Cette classe génère le fichier contenant la liste des relevés d'emploi
 * d'une période selon le format imposé par le gouvernement (XML)
 */
public class P_Employee_Roe_List2005 extends SvrProcess
{
    private int m_periodId    = 0;
    private int m_employeeId  = 0;
    private boolean m_recovery = false;
    private Properties m_ctx;
    private String FileName = "";
    private String trxName = null;
    private BigDecimal ZERO = new java.math.BigDecimal(0.0);
    private int Count = 0;
	// On récupère les paramètres de recherche
	protected void prepare() 
	{
	    ProcessInfoParameter[] para = getParameter();
	    for(int i = 0; i < para.length; i++)
	    {
	    	String name = para[i].getParameterName();
	    	if (para[i].getParameter() == null)
			;
	        else if(para[i].getParameterName().equals("P_Period_ID"))
	        	m_periodId = para[i].getParameterAsInt();
	        else if(para[i].getParameterName().equals("P_Employee_ID"))
	        	m_employeeId = para[i].getParameterAsInt();
	        else if(para[i].getParameterName().equals("Recovery"))
				m_recovery = "Y".equals(para[i].getParameter()); //para[i].getParameter().equals("Y");
            else
	            log.log (Level.SEVERE,"prepare - Unknown Parameter: " + name);
	    }
	}


	protected String doIt() throws Exception 
	{
		// On crée un fichier 'ROEjjmmaaaa.blk' contenant la liste des relevés
		// d'emploi sélectionnés selon le formatage de l'annexe B

		//P_Period Period = P_Period.get(getCtx(), m_periodId, trxName);
    	//FileName = Period.getName();
		
		StringBuffer CrFile = new StringBuffer();
        try
		{
    		CreateHeader(CrFile);
    		CreateDetail(CrFile);
    	    WriteToFile(CrFile);
		}
        catch (Exception e)
		{
        	log.log(Level.WARNING, "DoIt()", e);	
		}
	    return String.valueOf(Count) + " Relevé d'emploi(s) généré(s) avec succes";
	}
	
	private void CreateHeader(StringBuffer XX)
	{
		XX.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\r\n");
		String RoeHeader = "<ROEHEADER Application=\"RoeWeb\" FileVersion=\"1.00\">";
		XX.append(RoeHeader); 
		XX.append("\r\n");
	}
	
    public void CreateDetail(StringBuffer CrFile)
    {
	    String sql // On recherche les enregistrements dans l'ordre de l'annexe B
	    = " select "
	    	 + "   Employee.Value,  Employer.CanadaRevenueBusinessNumber,  Frequency.PayPeriodType, "
			 + "   substring(SIN, 1, 3) + substring(SIN, 5, 3) + substring(SIN, 9, 3),"
	    	 + "   Employee.FirstName,  Employee.FirstLetter,  Employee.SurName,  Location.Address1,  Location.City,"
	    	 + "   isnull( Region.Name, '') + ', ' + Country.Name + ', ' + isnull( Location.Postal, '' ), "
	    	 + "   Roe.FirstDayWorked,  Roe.LastDayForWhichPaid,  Roe.FinalPayPeriodEndingDate,  Roe.EmployeeOccupation,"
	    	 + "   Roe.ExpectedRecallCode,  Roe.ExpectedDateOfRecall,  Roe.TotalInsurableHours,  Roe.TotalInsurableEarnings,"
	    	 + "   Roe.EarningsForPayPeriod01,  Roe.EarningsForPayPeriod02,  Roe.EarningsForPayPeriod03,"
	    	 + "   Roe.EarningsForPayPeriod04,  Roe.EarningsForPayPeriod05,  Roe.EarningsForPayPeriod06,"
	    	 + "   Roe.EarningsForPayPeriod07,  Roe.EarningsForPayPeriod08,  Roe.EarningsForPayPeriod09,"
	    	 + "   Roe.EarningsForPayPeriod10,  Roe.EarningsForPayPeriod11,  Roe.EarningsForPayPeriod12,"
	    	 + "   Roe.EarningsForPayPeriod13,  Roe.EarningsForPayPeriod14,  Roe.EarningsForPayPeriod15,"
	    	 + "   Roe.EarningsForPayPeriod16,  Roe.EarningsForPayPeriod17,  Roe.EarningsForPayPeriod18,"
	    	 + "   Roe.EarningsForPayPeriod19,  Roe.EarningsForPayPeriod20,  Roe.EarningsForPayPeriod21,"
	    	 + "   Roe.EarningsForPayPeriod22,  Roe.EarningsForPayPeriod23,  Roe.EarningsForPayPeriod24,"
	    	 + "   Roe.EarningsForPayPeriod25,  Roe.EarningsForPayPeriod26,  Roe.EarningsForPayPeriod27,"
	    	 + "   Roe.EarningsForPayPeriod28,  Roe.EarningsForPayPeriod29,  Roe.EarningsForPayPeriod30,"
	    	 + "   Roe.EarningsForPayPeriod31,  Roe.EarningsForPayPeriod32,  Roe.EarningsForPayPeriod33,"
	    	 + "   Roe.EarningsForPayPeriod34,  Roe.EarningsForPayPeriod35,  Roe.EarningsForPayPeriod36,"
	    	 + "   Roe.EarningsForPayPeriod37,  Roe.EarningsForPayPeriod38,  Roe.EarningsForPayPeriod39,"
	    	 + "   Roe.EarningsForPayPeriod40,  Roe.EarningsForPayPeriod41,  Roe.EarningsForPayPeriod42,"
	    	 + "   Roe.EarningsForPayPeriod43,  Roe.EarningsForPayPeriod44,  Roe.EarningsForPayPeriod45,"
	    	 + "   Roe.EarningsForPayPeriod46,  Roe.EarningsForPayPeriod47,  Roe.EarningsForPayPeriod48,"
	    	 + "   Roe.EarningsForPayPeriod49,  Roe.EarningsForPayPeriod50,  Roe.EarningsForPayPeriod51,"
             + "   Roe.EarningsForPayPeriod52,  Roe.EarningsForPayPeriod53, "			 
	    	 + "   ''," 
	    	 + "   Roe.FirstNameContactPerson, "
	    	 + "   Roe.LastNameContactPerson,  " 
	    	 + "   Roe.PhoneAreaCode,  " 
	    	 + "   Roe.Phone as PhoneNumberContactPerson, "
	    	 + "   Roe.PhoneExt ,"
	    	 + "   Roe.VacationPayAmount,  Roe.StatutoryHolidayPayDate01,"
	    	 + "   Roe.StatutoryHolidayPayAmount01,  Roe.StatutoryHolidayPayDate02,  Roe.StatutoryHolidayPayAmount02,"
	    	 + "   Roe.StatutoryHolidayPayDate03,  Roe.StatutoryHolidayPayAmount03,  Roe.OtherMoniesCode01,"
	    	 + "   Roe.OtherMoniesAmount01,  Roe.OtherMoniesCode02,  Roe.OtherMoniesAmount02,  Roe.OtherMoniesCode03,"
	    	 + "   Roe.OtherMoniesAmount03,  isnull(Roe.CommentsLine01, '') CommentsLine01,  isnull(Roe.CommentsLine02,'') CommentsLine02,  isnull(Roe.CommentsLine03,'') CommentsLine03,"
	    	 + "   isnull(Roe.CommentsLine04,'') CommentsLine04,  Roe.PaidSickDate,  Roe.PaidSickAmount,  Roe.PaidSickPeriod,"
	    	 + "   Employer.CommunicationPreferredIn,  Employer.PrintLanguageRoe,  'D', P_Employee_Roe_ID, Frequency.NumberOfPeriod, ReasonForIssuingThisRoe, Roe.PrintLanguageID"
	    	 + " from P_Employee_Roe Roe, P_Employee Employee "
	    	 + " left join C_Location Location on Location.C_Location_ID = Employee.C_Location_ID"
	    	 + " left join C_Country Country on Country.C_Country_ID = Location.C_Country_ID "
	    	 + " left join C_Region Region on Region.C_Region_ID = Location.C_Region_ID "
	    	 + " left join P_Payment_Group PG on PG.P_Payment_Group_ID = Employee.P_Payment_Group_ID"
	    	 + " left join P_Frequency Frequency  on Frequency.P_Frequency_ID = PG.P_Frequency_ID"
	    	 + " left join P_Employer Employer on Employer.P_Employer_ID = Employee.P_Employer_ID"
	    	 + " where  Roe.P_Employee_ID = Employee.P_Employee_ID " 
	    	 ;
	    if ( m_employeeId != 0 )
	    	sql = sql + " and Employee.P_Employee_ID = " + m_employeeId ;

	    if(!m_recovery)	   sql += " and Roe.Transfered = 'N'";
	    
	    if ( m_periodId != 0 )
	    	sql = sql +  " and exists ("
	    	 + "   select 1"
	    	 + "   from P_Period"
	    	 + "   where P_Period_ID = " + m_periodId
	    	 + "   and StartDate <= Roe.FinalPayPeriodEndingDate"
	    	 + "   and EndDate >= Roe.FInalPayPeriodEndingDate"
	    	 + " )";
	    PreparedStatement stmt = null;
        try 
		{
        	
        	stmt = DB.prepareStatement(sql, null);
    	    ResultSet rs = stmt.executeQuery();
    	    while(rs.next())
    	    {
    	    	//int EmpRoePrintLangID = rs.getInt("P_Employee_Roe_ID");
    	    	int EmpRoePrintLangID = rs.getInt("PrintLanguageID");
    	    	InsertBlank(CrFile,2);
    	    	//CrFile.append("<Roe PrintingLanguage=\"" + convertToString(rs.getObject(99), 1) + "\" " + "Issue=\"S\">" + "\r\n");
    	    	CrFile.append("<Roe PrintingLanguage=\"" + getEmployeeRoeLanguage(EmpRoePrintLangID) + "\" " + "Issue=\"S\">" + "\r\n");
    	    	InsertBlank(CrFile,4);
    	    	CrFile.append("<B3>" + convertToString(rs.getObject(1), 15).trim() + "</B3>" + "\r\n"); // Employee.Value 
    	    	InsertBlank(CrFile,4);
    	    	CrFile.append("<B5>" + convertToString(rs.getObject(2), 15) + "</B5>" + "\r\n"); // Employer.CanadaRevenueBusinessNumber 
    	    	InsertBlank(CrFile,4);
    	    	CrFile.append("<B6>" + convertToString(rs.getObject(3), 1) + "</B6>" + "\r\n");   // Frequency.PayPeriodType
    	    	InsertBlank(CrFile,4);
    	    	CrFile.append("<B8>" + convertToString(rs.getObject(4), 9) + "</B8>" + "\r\n");   // Employee.sin
    	    	InsertBlank(CrFile,4);
    	    	CrFile.append("<B9>" + "\r\n");
    	    	InsertBlank(CrFile,6);
    	    	CrFile.append("<FN>" + convertToString(rs.getObject(5), 20).trim() + "</FN>" + "\r\n");  // Employee.FirstName
/*    	    	if (convertToString(rs.getObject(6), 4).trim().length() > 0)
    	    	{
    		    	InsertBlank(CrFile,6);
    		    	CrFile.append("<MN>" + convertToString(rs.getObject(6), 4).trim() + "</MN>" + "\r\n");  // Employee.FirstLetter
    	    	}
*/    	    	
    	    	InsertBlank(CrFile,6);
    	    	CrFile.append("<LN>" + convertToString(rs.getObject(7), 28).trim() + "</LN>" + "\r\n");  // Employee.SurName
    	    	InsertBlank(CrFile,6);
    	    	CrFile.append("<A1>" + convertToString(rs.getObject(8), 35).trim() + "</A1>" + "\r\n");  // Location.Address1
    	    	if (convertToString(rs.getObject(9), 35).trim().length() > 0)
    	    	{
    		    	InsertBlank(CrFile,6);
    		    	CrFile.append("<A2>" + convertToString(rs.getObject(9), 35).trim() + "</A2>" + "\r\n");  // Location.City
    	    	}
    	    	if (convertToString(rs.getObject(10), 35).trim().length() > 0)
    	    	{
    	    		InsertBlank(CrFile,6);
    		    	CrFile.append("<A3>" + convertToString(rs.getObject(10), 35).trim() + "</A3>" + "\r\n");  // Province pays code postal
    	    	}
    	    	InsertBlank(CrFile,4);
    	    	CrFile.append("</B9>" + "\r\n");
    	    	InsertBlank(CrFile,4);
    	    	CrFile.append("<B10>" + convertToString(rs.getObject(11), 8) + "</B10>" + "\r\n");     // Roe.FirstDayWorked
    	    	InsertBlank(CrFile,4);
    	    	CrFile.append("<B11>" + convertToString(rs.getObject(12), 8) + "</B11>" + "\r\n");     // Roe.LastDayForWhichPaid
    	    	InsertBlank(CrFile,4);
    	    	CrFile.append("<B12>" + convertToString(rs.getObject(13), 8) + "</B12>" + "\r\n");     // Roe.FinalPayPeriodEndingDate
    	    	if (convertToString(rs.getObject(14), 40).trim().length() > 0)
    	    	{
    	    		InsertBlank(CrFile,4);
    		    	CrFile.append("<B13>" + convertToString(rs.getObject(14), 40).trim() + "</B13>" + "\r\n");     // Roe.EmployeeOccupation
    	    	}
//    	    	if (convertToString(rs.getObject(15), 1).equals("Y"))    // Roe.ExpectedRecallCode
//    	    	{
    	    		InsertBlank(CrFile,4);
        	    	CrFile.append("<B14>" + "\r\n");
    		    	InsertBlank(CrFile,6);
    		    	CrFile.append("<CD>" + convertToString(rs.getObject(15), 1) + "</CD>" + "\r\n"); //  Roe.ExpectedRecallCode
    	    		InsertBlank(CrFile,6);
    		    	CrFile.append("<DT>" + convertToString(rs.getObject(16), 10).trim() + "</DT>" + "\r\n"); //  Roe.ExpectedDateOfRecall
    		    	InsertBlank(CrFile,4);
    		    	CrFile.append("</B14>" + "\r\n");
//    	    	}
    	    	BigDecimal HourDecimal = ZERO;
    	    	HourDecimal = rs.getBigDecimal(17).setScale(0,BigDecimal.ROUND_HALF_UP);
    	    	//int Hour = HourDecimal.setScale(0, BigDecimal.ROUND_HALF_UP);
    	    	InsertBlank(CrFile,4);
    	    	CrFile.append("<B15A>" + convertToString(HourDecimal, 4).trim() + "</B15A>" + "\r\n");     // Roe.TotalInsurableHours
    	    	InsertBlank(CrFile,4);
    	    	CrFile.append("<B15B>" + convertToString(rs.getObject(18), 9).trim() + "</B15B>" + "\r\n");     // Roe.TotalInsurableEarnings
    	    	InsertBlank(CrFile,4);
    	    	CrFile.append("<B15C>" + "\r\n");
    	    	String Value = "";
    	    	BigDecimal Amount = ZERO;
    	    	int NbrPeriod = rs.getInt("NumberOfPeriod");
    	    	int XPeriod = 0;
    	    	int Colonne = 19;  // Représente le numéro de l'object de la période 1
    	    	for ( int i = 0;  i < (NbrPeriod + 1); i++ )
    	    	{
    	    		XPeriod = i + 1;
        	    	Value = convertToString(rs.getObject(Colonne), 9);   // Roe.VacationPayAmount
        	    	if  (Value.trim().length() > 0)     // (Value != "null")
    	    		{
    	    			InsertBlank(CrFile,6);
    	    	    	CrFile.append("<PP nbr=\"" + XPeriod + "\">" + "\r\n");
        	    		InsertBlank(CrFile,8);
        	    		CrFile.append("<AMT>" + convertToString(rs.getObject(Colonne), 9).trim() + "</AMT>" + "\r\n");
        	    		Colonne++;
        	    		InsertBlank(CrFile,6);
        	    		CrFile.append("</PP>" + "\r\n");
    	    		}
    	    	}
    	    	InsertBlank(CrFile,4);
    	    	CrFile.append("</B15C>" + "\r\n");
    	    	InsertBlank(CrFile,4);
    	    	CrFile.append("<B16>" + "\r\n");
    	    	InsertBlank(CrFile,6);
    	    	CrFile.append("<CD>" + rs.getString("ReasonForIssuingThisRoe") + "</CD>" + "\r\n");   // Roe.ReasonForIssuingThisRoe
    	    	InsertBlank(CrFile,6);
    	    	CrFile.append("<FN>" + convertToString(rs.getObject("FirstNameContactPerson"), 20).trim() + "</FN>" + "\r\n");   // FirstNameContactPerson
    	    	InsertBlank(CrFile,6);
    	    	CrFile.append("<LN>" + convertToString(rs.getObject("LastNameContactPerson"), 28).trim() + "</LN>" + "\r\n");   // LastNameContactPerson
    	    	InsertBlank(CrFile,6);
    	    	CrFile.append("<AC>" + convertToString(rs.getObject("PhoneAreaCode"), 3) + "</AC>" + "\r\n");   // Employer.PhoneAreaCodeContactPerson
    	    	String PhoneNumber = convertToString(rs.getObject("PhoneNumberContactPerson"), 13);                        // Employer.PhoneNumberContactPerson
    	    	if ( PhoneNumber.startsWith("("))
    	    		PhoneNumber = PhoneNumber.substring(5,8) + PhoneNumber.substring(9, 13);                     
    	    	else
    	    		PhoneNumber = PhoneNumber.replace( "-", "");
//    	    		PhoneNumber = PhoneNumber.substring(0,3) + PhoneNumber.substring(4, 8);                     
    	    	InsertBlank(CrFile,6);
    	    	CrFile.append("<TEL>" + PhoneNumber.trim() + "</TEL>" + "\r\n");   
    	    	if (convertToString(rs.getObject("PhoneExt"), 5).trim().length() > 0)
    	    	{
    	    		InsertBlank(CrFile,6);
    		    	CrFile.append("<EXT>" + convertToString(rs.getObject("PhoneExt"), 5).trim() + "</EXT>" + "\r\n");   // Extention 
    	    	}
    	    	InsertBlank(CrFile,4);
    	    	CrFile.append("</B16>" + "\r\n");
    	    	Value = convertToString(rs.getObject(78), 9);   // Roe.VacationPayAmount
    	    	if (Value.trim().length() > 0)  // (Amount.compareTo(ZERO) != 0)
    	    	{
    		    	InsertBlank(CrFile,4);
    		    	CrFile.append("<B17A>" + Value.trim() + "</B17A>" + "\r\n");   
    	    	}
    	    	int jour = 0;
    	    	Colonne = 0;
    	    	if (convertToString(rs.getObject(79), 9).trim().length() > 0 || convertToString(rs.getObject(81), 9).trim().length() > 0 || convertToString(rs.getObject(83), 9).trim().length() > 0)
    	    	{
    		    	InsertBlank(CrFile,4);
    		    	CrFile.append("<B17B>" + "\r\n");   // Jour Férié
    		    	for (int i = 0; i < 2; i++)
    		    	{
    		    		Colonne = (i * 2) + 79;
    			    	jour = i + 1;
    			    	if (convertToString(rs.getObject(Colonne), 9).trim().length() > 0)
    			    	{
    				    	InsertBlank(CrFile,6);
    				    	CrFile.append("<SH nbr=\"" + jour + "\">" + "\r\n");   // Premier jours
    				    	InsertBlank(CrFile,8);
    				    	CrFile.append("<DT>" + convertToString(rs.getObject(Colonne), 8) + "</DT>" + "\r\n");   // Roe.StatutoryHolidayPayDate
    				    	InsertBlank(CrFile,8);
    				    	CrFile.append("<AMT>" + convertToString(rs.getObject(Colonne + 1), 9).trim() + "</AMT>" + "\r\n"); // Roe.StatutoryHolidayPayAmount
    				    	InsertBlank(CrFile,6);
    				    	CrFile.append("</SH>" + "\r\n");   // Premier jours
    			    	}
    		    	}
    		    	InsertBlank(CrFile,4);
    		    	CrFile.append("</B17B>" + "\r\n");   // 
    	    	}
    	    	if (convertToString(rs.getObject(85), 9).trim().length() > 0 || convertToString(rs.getObject(87), 9).trim().length() > 0 || convertToString(rs.getObject(89), 9).trim().length() > 0)
    	    	{
    		    	InsertBlank(CrFile,4);
    		    	CrFile.append("<B17C>" + "\r\n");   // Jour Férié
    		    	jour = 0;
    		    	Colonne = 0;
    		    	// 2009.07.31
//    		    	for (int i = 0; i < 2; i++)
       		    	for (int i = 0; i <= 2; i++)
    		    	{
    		    		Colonne = (i * 2) + 85;
    			    	jour = i + 1;
    			    	if (convertToString(rs.getObject(Colonne), 1).trim().length() > 0)
    			    	{
    				    	InsertBlank(CrFile,6);
    				    	CrFile.append("<OM nbr=\"" + jour + "\">" + "\r\n");   // Premier jours
    				    	InsertBlank(CrFile,8);
    				    	CrFile.append("<CD>" + convertToString(rs.getObject(Colonne), 1) + "</CD>" + "\r\n");   // Roe.OtherMoniesCode
    				    	InsertBlank(CrFile,8);
    				    	CrFile.append("<AMT>" + convertToString(rs.getObject(Colonne + 1), 9).trim() + "</AMT>" + "\r\n"); // Roe.OtherMoniesAmount
    				    	InsertBlank(CrFile,6);
    				    	CrFile.append("</OM>" + "\r\n");   // Premier jours
    			    	}
    		    	}
    		    	InsertBlank(CrFile,4);
    		    	CrFile.append("</B17C>" + "\r\n");   //
    	    	}
    	    	//if (convertToString(rs.getObject(72), 1) == "K")     // Roe.ReasonForIssuingThisRoe
//    	    	if (convertToString(rs.getObject(72), 1).equals("K"))     // Roe.ReasonForIssuingThisRoe
//    	    	{
    		    	String Comments = convertToString(rs.getObject(91), 40).trim() + " " +  // Roe.CommentsLine01
    		                          convertToString(rs.getObject(92), 40).trim() + " " +  // Roe.CommentsLine02
    		                          convertToString(rs.getObject(93), 40).trim() + " " +  // Roe.CommentsLine03
    		                          convertToString(rs.getObject(94), 40).trim();         // Roe.CommentsLine04
    		    	Comments = Comments.trim();
                    InsertBlank(CrFile,4);
                    CrFile.append("<B18>" + Comments.trim() + "</B18>" + "\r\n"); 
//    	    	}
    	    	Value = convertToString(rs.getObject(96), 9);   // Roe.VacationPayAmount
    	    	if (Value.trim().length() > 0 )   //(Value != null)
	    		{
    	         	InsertBlank(CrFile,4);
    		    	CrFile.append("<B19>" + "\r\n");
    		    	InsertBlank(CrFile,6);
    		    	CrFile.append("<SP cd=\"psl\">" + "\r\n");
    	            InsertBlank(CrFile,8);
    	            CrFile.append("<DT>" + convertToString(rs.getObject(95), 8) + "</DT>" + "\r\n");
    	            InsertBlank(CrFile,8);
    	            CrFile.append("<AMT>" + convertToString(rs.getObject(96), 9).trim() + "</AMT>" + "\r\n");
    	            InsertBlank(CrFile,8);
    	            CrFile.append("<Period>" + convertToString(rs.getObject(97), 1) + "</Period>" + "\r\n");
    	            InsertBlank(CrFile,6);
    	            CrFile.append("</SP>");
    		    	InsertBlank(CrFile,4);
    		    	CrFile.append("</B19>" + "\r\n");
    	    	}
    	    	InsertBlank(CrFile,4);
    	    	CrFile.append("<B20>" + convertToString(rs.getObject(98), 1) + "</B20>" + "\r\n"); // Roe.OtherMoniesAmount
    	    	InsertBlank(CrFile,2);
    	    	CrFile.append("</Roe>" + "\r\n");

    	    	// On doit maintenant mettre à jour la table P_Employee_Roe pour indiquer qu'on
    	        // vient de transférer les relevés d'emploi pour la période demandée
    /*	        sql = "update P_Employee_Roe"
    	            + "   set Transfered + 'Y'"
    	            + " Where P_Employee_Roe_ID = " + rs.getInt("P_Employee_Roe_ID")
    	            ;
    */	            
    			Count++;
    		    
    	    }
    	    rs.close();
    	    stmt.close();
    	    CrFile.append("</ROEHEADER>" + "\r\n");
    	    
    	    // On doit maintenant mettre à jour la table P_Employee_Roe pour indiquer qu'on
            // vient de transférer les relevés d'emploi pour la période demandée
            sql = "update P_Employee_Roe"
                + "   set Transfered + 'Y'"
                + " where exists ("
                + "   select 1"
                + "   from P_Period"
                + "   where P_Period_ID = " + m_periodId
                + "   and StartDate <= P_Employee_Roe.FinalPayPeriodEndingDate"
                + "   and EndDate >= P_Employee_Roe.FInalPayPeriodEndingDate"
                + " )";
    	   
		}
        catch (SQLException e)
		{
        	log.log(Level.WARNING, "DoIt()", e);
		}
    }
	
	public void InsertBlank(StringBuffer XX, int Pos)
    {
    	//log.debug("CreateFileTransfertRBC.InsertBlank - Début");
    	for (int i = 0; i < Pos; i++)
    	{
    		XX.append(" ");
    	}
    	//log.debug("CreateFileTransfertRBC.InsertBlank - Fin");
    }
    
    public void WriteToFile(StringBuffer XX)
    {
      String Path = "";
      Calendar DateJour = Calendar.getInstance();
      String Month = String.valueOf(DateJour.get(Calendar.MONTH));
      if ( this.m_employeeId != 0)
      {
    	  P_Employee Employee = P_Employee.get( Env.getCtx(), this.m_employeeId, null  );
    	  
          FileName = "ROE_" + Employee.getValue().trim() + "_" + Employee.getSurname() + "_" 
          + String.valueOf(DateJour.get(Calendar.YEAR)+ "." 
          + "00".substring(0, 2 - String.valueOf(DateJour.get(Calendar.MONTH)).trim().length()) + String.valueOf(DateJour.get(Calendar.MONTH)) + "."
          +  String.valueOf(DateJour.get(Calendar.DAY_OF_MONTH))  );
    	  
      }
      else
      {
          FileName = "ROE_" 
        	  	+ String.valueOf(DateJour.get(Calendar.YEAR)+ "." 
        	  	+ "00".substring(0, 2 - String.valueOf(DateJour.get(Calendar.MONTH)).trim().length()) + String.valueOf(DateJour.get(Calendar.MONTH)) + "."
                +  String.valueOf(DateJour.get(Calendar.DAY_OF_MONTH))  );
      
      }
            
      Path = PgiUtil.getSolsticeParameter(Env.getCtx(), "TransfertPathROE");
        log.log(Level.WARNING, "WriteToFile - Path" + Path);
    	File aFile = null;
  	    aFile = new File(Path + FileName + ".blk");
        FileOutputStream out; // declare a file output object
        PrintStream p;        // declare a print stream object
        try
        {
              out = new FileOutputStream(aFile);

              p = new PrintStream( out );

              p.println (XX.toString());
              System.out.println(XX.toString());
              p.close();
        }
        catch (Exception e)
        {
              System.out.println ("Error writing to file :" + e.toString());
        }
    }
 
	
	private String save(String s)
	{
		JFileChooser dialog = new JFileChooser();
		if(dialog.showSaveDialog(new Frame()) == JFileChooser.APPROVE_OPTION)
		{
			try
			{
				FileWriter out = new FileWriter ( dialog.getSelectedFile() );
				out.write(s);
				out.close();
			}
			catch (Exception e)
			{
				return e.toString();
			}
		}
		return Msg.translate(Env.getLanguage(Env.getCtx()), "Success");
	}
	
	// Convertit un objet de la bd en string selon une largeur
	private String convertToString(Object object, int width)
	{
	    if(object == null)
	    {
	        return pad("", width);
	    }
	    if( object.getClass().equals(Timestamp.class) ) // On veut un format jj/mm/aaaa pour toutes les dates
	    {
	        SimpleDateFormat f = new SimpleDateFormat ("ddMMyyyy");
	        return f.format((Timestamp)object);
	    }
	    if( object.getClass().equals(Double.class) )
	    {
	        DecimalFormat f = new DecimalFormat ("#0.00");
	        String s = f.format(((Double)object).doubleValue());
	        s = s.replace('.', ',');
	        return pad(s, width);
	    }
	    return pad(object.toString().trim(), width);
	}
	
	// Ajoute des espaces au bout de la string
	private String pad (String input, int width)
	{
	    if(input.length() > width)
	    {
	        return input.substring(0, width);
	    }
        String buffer = input;
        while(buffer.length() < width)
        {
            buffer += " ";
        }
        return buffer;
	}
	
	String getEmployeeRoeLanguage(int printLangID)
	{
		String ret = null;
		String value = null;
		String sql = "select value from P_Language "
			+ "where P_Language_ID =" + printLangID;
		
		
		PreparedStatement stmt = null;
        try 
		{
        	stmt = DB.prepareStatement(sql, null);
    	    ResultSet rs = stmt.executeQuery();
    	    while(rs.next())
    	    {
    	    	value = rs.getString(1);
    	    }
    	    if (value.substring(0, 2).equals("en"))
    	    	ret = "E";
    	    if (value.substring(0, 2).equals("fr"))
    	    	ret = "F";
    	    rs.close();
    	    stmt.close();
		} catch (SQLException e)
		{
			log.log(Level.WARNING, "getEmployeeRoeLanguage", e);
		}
		return ret;
	}
}
