/*
 * Created on 28 juin 2005
 */
package solstice.process;

import java.awt.Frame;
import java.io.FileWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.swing.JFileChooser;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 * @author frafor01
 *
 * Cette classe génère le fichier contenant la liste des relevés d'emploi
 * d'une période selon le format imposé par le gouvernement
 */
public class P_Employee_Roe_List extends SvrProcess
{
    private int m_periodId    = 0;
    private int m_employeeId  = 0;
    private boolean m_recovery;
    
	// On récupère les paramètres de recherche
	protected void prepare() 
	{
	    ProcessInfoParameter[] para = getParameter();
	    
	    for(int i = 0; i < para.length; i++)
	    {
	        if(para[i].getParameterName().equals("P_Period_ID"))
	        {
	        	m_periodId = para[i].getParameterAsInt();
	        }
	        else if(para[i].getParameterName().equals("P_Employee_ID"))
	        {
	        	m_employeeId = para[i].getParameterAsInt();
	        }
	        else if(para[i].getParameterName().equals("Recovery"))
			{
				m_recovery = para[i].getParameter().equals("Y");
			}
	    }
	}

	// On crée un fichier 'ROEjjmmaaaa.blk' contenant la liste des relevés
	// d'emploi sélectionnés selon le formatage de l'annexe B
	protected String doIt() throws Exception 
	{
	    String sql // On recherche les enregistrements dans l'ordre de l'annexe B
	    = " select "
	    	 + "   Employee.Value,  Employer.CanadaRevenueBusinessNumber,  Frequency.PayPeriodType, "
			 + "   substring(SIN, 1, 3) + substring(SIN, 5, 3) + substring(SIN, 9, 3),"
	    	 + "   Employee.FirstName,  Employee.FirstLetter,  Employee.SurName,  Location.Address1,  Location.City,"
	    	 + "   Region.Name + ', ' + Country.Name + ', ' + Location.Postal,  '', '', '', '', '',"
	    	 + "   Roe.FirstDayWorked,  Roe.LastDayForWhichPaid,  Roe.FinalPayPeriodEndingDate,  Roe.EmployeeOccupation,"
	    	 + "   Roe.ExpectedRecallCode,  Roe.ExpectedDateOfRecall,  Roe.TotalInsurableHours,  Roe.TotalInsurableEarnings,"
	    	 + "   '',  Roe.EarningsForPayPeriod01,  Roe.EarningsForPayPeriod02,  Roe.EarningsForPayPeriod03,"
	    	 + "   Roe.EarningsForPayPeriod04,  Roe.EarningsForPayPeriod05,  Roe.EarningsForPayPeriod06,"
	    	 + "   Roe.EarningsForPayPeriod07,  Roe.EarningsForPayPeriod08,  Roe.EarningsForPayPeriod09,"
	    	 + "   Roe.EarningsForPayPeriod10,  Roe.EarningsForPayPeriod11,  Roe.EarningsForPayPeriod12,"
	    	 + "   Roe.EarningsForPayPeriod13,  Roe.EarningsForPayPeriod14,  Roe.EarningsForPayPeriod15,"
	    	 + "   Roe.EarningsForPayPeriod16,  Roe.EarningsForPayPeriod17,  Roe.EarningsForPayPeriod18,"
	    	 + "   Roe.EarningsForPayPeriod19,  Roe.EarningsForPayPeriod20,  Roe.EarningsForPayPeriod21,"
	    	 + "   Roe.EarningsForPayPeriod22,  Roe.EarningsForPayPeriod23,  Roe.EarningsForPayPeriod24,"
	    	 + "   Roe.EarningsForPayPeriod25,  Roe.EarningsForPayPeriod26,  Roe.EarningsForPayPeriod27,"
	    	 + "   Roe.ReasonForIssuingThisRoe,  (select FirstName from P_Employee where AD_User_ID = Roe.AD_User_ID) as FirstNameContactPerson,  '',"
	    	 + "   (select SurName from P_Employee where AD_User_ID = Roe.AD_User_ID) as LastNameContactPerson,  Employer.PhoneAreaCodeContactPerson,  Employer.PhoneNumberContactPerson,"
	    	 + "   ( select P_Post.PhoneExt"
             + "       from P_Post inner join P_Assignment inner join P_Employee"
             + "         on P_Assignment.P_Employee_ID = P_Employee.P_Employee_ID"
             + "         on P_Post.P_Post_ID = P_Assignment.P_Post_ID"
             + "      where P_Assignment.AssignmentType = 'P'"
             + "        and P_Employee.AD_User_ID = Roe.AD_User_ID) ,  Roe.VacationPayAmount,  Roe.StatutoryHolidayPayDate01,"
	    	 + "   Roe.StatutoryHolidayPayAmount01,  Roe.StatutoryHolidayPayDate02,  Roe.StatutoryHolidayPayAmount02,"
	    	 + "   Roe.StatutoryHolidayPayDate03,  Roe.StatutoryHolidayPayAmount03,  Roe.OtherMoniesCode01,"
	    	 + "   Roe.OtherMoniesAmount01,  Roe.OtherMoniesCode02,  Roe.OtherMoniesAmount02,  Roe.OtherMoniesCode03,"
	    	 + "   Roe.OtherMoniesAmount03,  Roe.CommentsLine01,  Roe.CommentsLine02,  Roe.CommentsLine03,"
	    	 + "   Roe.CommentsLine04,  Roe.PaidSickDate,  Roe.PaidSickAmount,  Roe.PaidSickPeriod,"
	    	 + "   Employer.CommunicationPreferredIn,  Employer.PrintLanguageRoe,  'D', P_Employee_Roe_ID"

	    	 + " from P_Employee_Roe Roe, P_Employee Employee "
	    	 + " right join C_Location Location on Location.C_Location_ID = Employee.C_Location_ID"
	    	 + " right join C_Country Country on Country.C_Country_ID = Location.C_Country_ID "
	    	 + " right join C_Region Region on Region.C_Region_ID = Location.C_Region_ID "
	    	 + " right join P_Payment_Group PG on PG.P_Payment_Group_ID = Employee.P_Payment_Group_ID"
	    	 + " right join P_Frequency Frequency  on Frequency.P_Frequency_ID = PG.P_Frequency_ID"
	    	 + " right join P_Employer Employer on Employer.P_Employer_ID = Employee.P_Employer_ID"
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
	    

	    PreparedStatement stmt = DB.prepareStatement(sql, null);
	    ResultSet rs = stmt.executeQuery();
	    
	    String record = "";
	    while(rs.next())
	    {
		    record = record
		    	+ convertToString(rs.getObject(1), 15)
		    	+ convertToString(rs.getObject(2), 15)
		    	+ convertToString(rs.getObject(3), 1)
		    	+ convertToString(rs.getObject(4), 9)
		    	+ convertToString(rs.getObject(5), 18)
		    	+ convertToString(rs.getObject(6), 18)
		    	+ convertToString(rs.getObject(7), 50)
		    	+ convertToString(rs.getObject(8), 35)
		    	+ convertToString(rs.getObject(9), 35)
		    	+ convertToString(rs.getObject(10), 35)
		    	+ convertToString(rs.getObject(11), 30)
		    	+ convertToString(rs.getObject(12), 2)
		    	+ convertToString(rs.getObject(13), 2)
		    	+ convertToString(rs.getObject(14), 50)
		    	+ convertToString(rs.getObject(15), 15)
		    	+ convertToString(rs.getObject(16), 10)
		    	+ convertToString(rs.getObject(17), 10)
		    	+ convertToString(rs.getObject(18), 10)
		    	+ convertToString(rs.getObject(19), 100)
		    	+ convertToString(rs.getObject(20), 1)
		    	+ convertToString(rs.getObject(21), 10)
		    	+ convertToString(rs.getString(22), 4) // On prend un string car on ne veut pas le convertir en montant (TotalInsurableHours)
		    	+ convertToString(rs.getObject(23), 10)
		    	+ convertToString(rs.getObject(24), 1)
		    	+ convertToString(rs.getObject(25), 10)
		    	+ convertToString(rs.getObject(26), 10)
		    	+ convertToString(rs.getObject(27), 10)
		    	+ convertToString(rs.getObject(28), 10)
		    	+ convertToString(rs.getObject(29), 10)
		    	+ convertToString(rs.getObject(30), 10)
		    	+ convertToString(rs.getObject(31), 10)
		    	+ convertToString(rs.getObject(32), 10)
		    	+ convertToString(rs.getObject(33), 10)
		    	+ convertToString(rs.getObject(34), 10)
		    	+ convertToString(rs.getObject(35), 10)
		    	+ convertToString(rs.getObject(36), 10)
		    	+ convertToString(rs.getObject(37), 10)
		    	+ convertToString(rs.getObject(38), 10)
		    	+ convertToString(rs.getObject(39), 10)
		    	+ convertToString(rs.getObject(40), 10)
		    	+ convertToString(rs.getObject(41), 10)
		    	+ convertToString(rs.getObject(42), 10)
		    	+ convertToString(rs.getObject(43), 10)
		    	+ convertToString(rs.getObject(44), 10)
		    	+ convertToString(rs.getObject(45), 10)
		    	+ convertToString(rs.getObject(46), 10)
		    	+ convertToString(rs.getObject(47), 10)
		    	+ convertToString(rs.getObject(48), 10)
		    	+ convertToString(rs.getObject(49), 10)
		    	+ convertToString(rs.getObject(50), 10)
		    	+ convertToString(rs.getObject(51), 10)
		    	+ convertToString(rs.getObject(52), 1)
		    	+ convertToString(rs.getObject(53), 18)
		    	+ convertToString(rs.getObject(54), 18)
		    	+ convertToString(rs.getObject(55), 50)
		    	+ convertToString(rs.getObject(56), 3)
		    	+ convertToString(rs.getObject(57), 8)
		    	+ convertToString(rs.getObject(58), 8)
		    	+ convertToString(rs.getObject(59), 10)
		    	+ convertToString(rs.getObject(60), 10)
		    	+ convertToString(rs.getObject(61), 10)
		    	+ convertToString(rs.getObject(62), 10)
		    	+ convertToString(rs.getObject(63), 10)
		    	+ convertToString(rs.getObject(64), 10)
		    	+ convertToString(rs.getObject(65), 10)
		    	+ convertToString(rs.getObject(66), 1)
		    	+ convertToString(rs.getObject(67), 10)
		    	+ convertToString(rs.getObject(68), 1)
		    	+ convertToString(rs.getObject(69), 10)
		    	+ convertToString(rs.getObject(70), 1)
		    	+ convertToString(rs.getObject(71), 10)
		    	+ convertToString(rs.getObject(72), 40)
		    	+ convertToString(rs.getObject(73), 40)
		    	+ convertToString(rs.getObject(74), 40)
		    	+ convertToString(rs.getObject(75), 40)
		    	+ convertToString(rs.getObject(76), 10)
		    	+ convertToString(rs.getObject(77), 10)
		    	+ convertToString(rs.getObject(78), 1)
		    	+ convertToString(rs.getObject(79), 1)
		    	+ convertToString(rs.getObject(80), 1)
		    	+ convertToString(rs.getObject(81), 1) + "\n";
		    
		    // On doit maintenant mettre à jour la table P_Employee_Roe pour indiquer qu'on
	        // vient de transférer les relevés d'emploi pour la période demandée
	        sql = "update P_Employee_Roe"
	            + "   set Transfered + 'Y'"
	            + " Where P_Employee_Roe_ID = " + rs.getInt("P_Employee_Roe_ID")
	            ;
			int count = DB.executeUpdate(sql, null);
		    
	    }
	    rs.close();
	    stmt.close();
/*	    
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
*/	   
	    return save(record);
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
	        SimpleDateFormat f = new SimpleDateFormat ("dd/MM/yyyy");
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

}
