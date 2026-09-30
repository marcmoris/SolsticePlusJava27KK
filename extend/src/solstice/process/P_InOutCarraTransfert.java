package solstice.process;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.model.MLocation;
import org.compiere.model.MOrg;
import org.compiere.model.MOrgInfo;
import solstice.model.P_Employee_Carra;


import solstice.model.P_Year;
import java.util.Calendar;


public class P_InOutCarraTransfert extends SvrProcess 
{
	private int m_iP_Year_ID = 0;
    private int m_iNbrTotal = 0;
    private String m_strPath = "";
    private String m_strIntro = "";
    private String m_lastCodeCarra = "";
    private int m_numRecordsLastRCE = 0;
    private BigDecimal m_bdCumulEmployee_Part = new BigDecimal("0"); 
    
      
    // Déclaration des objects
    private CLogger			log = CLogger.getCLogger (getClass());

	P_Year m_year;

    public P_InOutCarraTransfert()
    {
    	super();
    }
    
    protected void prepare()
    {
        ProcessInfoParameter[] para = getParameter();
		String paramName = "";

		//Read the parameters
		for(int i=0; i < para.length; i++)
		{
			paramName = para[i].getParameterName();
			if( paramName.equals("P_Year_ID"))
			{
				this.m_iP_Year_ID = ((Number)para[i].getParameter()).intValue();
			}
		}
		m_year = P_Year.get(getCtx(), m_iP_Year_ID, null);
    }

    /**
     * Traitement principal
     */
    protected String doIt() throws Exception
    {
    	String retValue = "";
    	
    	
    	retValue = CreateFile();
    	return retValue;
    }

    public String CreateFile()
    {
        String trxName = null; //	Trx.createTrxName();
        StringBuffer theFile = new StringBuffer();

        boolean retValue = CreateHeader( theFile, trxName );
        if(retValue == true)
        	retValue = CreateDetail( theFile, trxName);
        else
        	return "Erreur pendant la création de l'entête.";
        
        if(retValue == true)
        	retValue = WriteToFile(theFile, trxName);
        else
        	return "Erreur pendant la création du pied de page.";

        
        if(retValue == true)
        	return "Nombre de transaction traité: " + m_iNbrTotal + ".";
        else
        	return "Erreur pendant la création du fichier.";

    }

    public boolean CreateHeader(StringBuffer NewFile, String trxName)
    {
		// Création du premier enregistrement
	    int orgID = 11;
	    
    	String sql = " SELECT CARRA , AD_Org_ID"
    		+ " FROM P_Equivalence_Factor_Param "
    		+ " WHERE P_Year_ID = "+m_iP_Year_ID;
    	//log.error(sql);
    	String strCARRANumber = "";
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	try
    	{ 
    		pstmt = DB.prepareStatement(sql, null);
    		rs = pstmt.executeQuery();
    		if (rs.next())
    		{
    			orgID = rs.getInt( "AD_Org_ID");
    			strCARRANumber = rs.getString("CARRA");
    		}
    		rs.close();
    		pstmt.close();
    	}
    	catch (SQLException e)
    	{
    		log.log(Level.WARNING, "P_InOutCarraTransfert.CreateHeader", e);
    	}
    	
    	String strYear = "";
    	P_Year theYear = P_Year.get(getCtx(), m_iP_Year_ID, trxName);
    	if(theYear != null)
    		strYear = String.valueOf(theYear.getYear());
    	
    	String strConstante = "00";
    	String strRecordCode = "1";
    	

    	MOrg Org = MOrg.get( getCtx(), orgID);
    	MOrgInfo OrgInfo = MOrgInfo.get( getCtx(),Org.getAD_Org_ID(), null);
    	
    	MLocation Location = MLocation.get( getCtx(), OrgInfo.getC_Location_ID(), null );
    	
    	String strHeader = "";
    	strHeader += carra_filler(carra_format(strCARRANumber), 8, " ");
    	strHeader += strYear;
    	m_strIntro = strHeader;
    	strHeader += strConstante;
    	strHeader += strRecordCode;

    	strHeader += carra_filler(carra_format( Org.getDescription().trim() ), 40, " ");
    	strHeader += carra_filler(carra_format( Location.getAddress1().trim() ), 36, " ");
    	strHeader += carra_filler(carra_format( Location.getCity().trim() +" "+Location.getPostal() ), 36, " ");
    	strHeader += carra_filler("", 64, " ");
    	//log.debug("CreateFileTransfertRBC.CreateHeader" + Protocol_rsn);
    	NewFile.append(strHeader);

//    	NewFile.append("\n");
    	//log.debug("CreateFileTransfertRBC.CreateHeader - Fin");
    	
    	return true;
    }  // CreateHeader
      
    public void InsertBlank(StringBuffer Bl, int Pos)
    {
    	//log.debug("CreateFileTransfertRBC.InsertBlank - Début");
    	for (int i = 0; i < Pos; i++)
    	{
    		Bl.append(" ");
    	}
    	//log.debug("CreateFileTransfertRBC.InsertBlank - Fin");
    }

    public void ZeroFill(StringBuffer Bl, int Pos)
    {
    	//log.debug("CreateFileTransfertRBC.ZéroFill - Début");
    	for (int i = 0; i < Pos; i++)
    	{
    		Bl.append("0");
    	}
    	//log.debug("CreateFileTransfertRBC.ZéroFill - Fin");
    }
    
    public boolean CreateDetail (StringBuffer strNewFile, String trxName)
    {
    	boolean retValue = true;
    	int iDeductionTemp_ID = 0;
    	
    	String sql = "SELECT car.NumberOfDay, car.ContributorySalary, car.Employee_Part, car.LeaveOfAbsenceWithoutPay, "
    		+ " car.WorkedTimePercentage, car.EffectIn, car.EffectTo, car.ExoneratedSalary, car.DisabilityDays, isnull( Status, 0) as Status, "
    		+ " car.MotherhoodDays, car.MotherhoodSalary, car.NonContributorySalary, car.BaseSalary, rwt.value as codcar, "
    		+ " emp.SIN as SIN, emp.Surname as Surname, emp.Firstname as Firstname, emp.Gender as Gender, emp.Birthdate as Birthdate, "
    		+ " ded.P_Deduction_ID as P_Deduction_ID, car.P_Employee_Carra_ID "
    		+ " FROM P_Employee_Carra car, P_Employee emp, P_Deduction ded, P_RWT rwt "
    		+ " WHERE car.P_Year_ID = " + m_iP_Year_ID
    		+ " AND ded.P_Deduction_ID = car.P_Deduction_ID "
    		+ " AND emp.P_Employee_ID = car.P_Employee_ID "
    		+ " AND rwt.P_RWT_ID = car.P_RWT_ID "
    		+ " ORDER BY ded.value, emp.name";
    	PreparedStatement pstm = DB.prepareStatement(sql, null);
    	try
    	{
    		ResultSet rs = pstm.executeQuery();
    		String strDetailLine = "";
    		m_numRecordsLastRCE = 0;
			m_bdCumulEmployee_Part = new BigDecimal("0"); 

    		while(rs.next())
    		{
    			P_Employee_Carra EmployeeCarra = P_Employee_Carra.get( getCtx(), rs.getInt("P_Employee_Carra_ID"), trxName);
    			EmployeeCarra.setIsDone( true );
    			EmployeeCarra.save();
    			
    			int currentDeduction = rs.getInt("P_Deduction_ID");
    			//si on change de deduction, on ecrit une ligne de sommaire
    			if(iDeductionTemp_ID != 0 && currentDeduction != iDeductionTemp_ID)
    			{
    	        	boolean retValueFooter = CreateEndLine( strNewFile, trxName );
    	        	if(retValueFooter == false)
    	        		return retValueFooter;
    	    		m_numRecordsLastRCE = 0;
    				m_bdCumulEmployee_Part = new BigDecimal("0"); 
    			}
    			iDeductionTemp_ID = currentDeduction;
    	        
    			strDetailLine = "";
    			int iNumberOfDay = 0;
    			BigDecimal bdContributorySalary = new BigDecimal("0");
    			BigDecimal bdEmployee_Part = new BigDecimal("0");
    			int iLeaveOfAbsenceWithoutPay = 0;
    			BigDecimal bdWorkedTimePercentage = new BigDecimal("0");
    			Timestamp tsEffectIn = null;
    			Timestamp tsEffectTo = null;
    			BigDecimal bdExoneratedSalary = new BigDecimal("0");
    			int iDisabilityDays = 0;
    			String Status = "";
    			int iMotherhoodDays = 0;
    			BigDecimal bdMotherhoodSalary = new BigDecimal("0");
    			BigDecimal bdNonContributorySalary = new BigDecimal("0");
    			BigDecimal bdBaseSalary = new BigDecimal("0");
    			String strCodeCarra = "";
    			String strRecordCode = "2";
    			String strSin = "";
    			String strSurname = "";
    			String strFirstname = "";
    			String strGender = "";
    			Timestamp tsBirthdate = null;
       			String tempMonthIn = "";
    			String tempMonthTo = "";
    			String tempDateIn = "";
    			String tempDateTo = "";

    			
    			iNumberOfDay = rs.getInt("NumberOfDay");
    			bdContributorySalary = rs.getBigDecimal("ContributorySalary");
    			bdEmployee_Part = rs.getBigDecimal("Employee_Part");
    			m_bdCumulEmployee_Part = m_bdCumulEmployee_Part.add(bdEmployee_Part);
    			iLeaveOfAbsenceWithoutPay = rs.getInt("LeaveOfAbsenceWithoutPay");
    			//si 100 on met 0
    			bdWorkedTimePercentage = rs.getBigDecimal("WorkedTimePercentage");
//    			bdWorkedTimePercentage = bdWorkedTimePercentage.multiply(new BigDecimal(100));
    			if(bdWorkedTimePercentage.compareTo(new BigDecimal(100)) >= 0)
    				bdWorkedTimePercentage = new BigDecimal("0");
    			tsEffectIn = rs.getTimestamp("EffectIn");
    			tsEffectTo = rs.getTimestamp("EffectTo");
    			bdExoneratedSalary = rs.getBigDecimal("ExoneratedSalary");
    			iDisabilityDays = rs.getInt("DisabilityDays");
    			Status = rs.getString("Status");
    			iMotherhoodDays = rs.getInt("MotherhoodDays");
    			bdMotherhoodSalary = rs.getBigDecimal("MotherhoodSalary");
    			bdNonContributorySalary = rs.getBigDecimal("NonContributorySalary");
    			bdBaseSalary = rs.getBigDecimal("BaseSalary");
    			strCodeCarra = rs.getString("codcar");
    			m_lastCodeCarra = strCodeCarra;
    			strSin = rs.getString("SIN");
    			strSurname = rs.getString("Surname");
    			strFirstname = rs.getString("Firstname");
    			strGender = (rs.getString("Gender").toUpperCase().equals("M")) ? "1" : "2";
    			tsBirthdate = rs.getTimestamp("Birthdate");
    			
		        Calendar cal = Calendar.getInstance();
		        cal.setTime( (Date)tsBirthdate );
		        String month = String.valueOf( cal.get(Calendar.MONTH)+1);
		        String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		        String strBirthdate = String.valueOf(cal.get(Calendar.YEAR)) + "00".substring(0, 2-month.length()) + month + "00".substring(0, 2-day.length()) + day ;

    			if(tsEffectIn != null)
    			{
    		        cal.setTime( (Date)tsEffectIn );
    		        tempMonthIn = carra_prefiller(String.valueOf( cal.get(Calendar.MONTH)+1), 2, "0");
    		        tempDateIn  = carra_prefiller(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)), 2, "0");

    			
    			}
    			if(tsEffectTo != null)
    			{
    		        cal.setTime( (Date)tsEffectTo );
    		        tempMonthTo = carra_prefiller(String.valueOf( cal.get(Calendar.MONTH)+1), 2, "0");
    		        tempDateTo  = carra_prefiller(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)), 2, "0");

    		        //
    		        // Si la date de fin d'un employé tombe dans la période entre la date de fin de la dernière paie(26)
    		        // et le 31 décembre, la déclaration doit être considérer dans l'année suivante 
    		        // avec une date de fin 9999
    		        //
    		        if ( cal.get(Calendar.YEAR) < m_year.getYear() )
    		        {
    		        	tempMonthTo = "99";
    		        	tempDateTo = "99";
    		        }
    			
    			}
    			
    			strDetailLine += m_strIntro;
    			strDetailLine += strCodeCarra.substring(0, 2);
    			strDetailLine += strRecordCode;
    			strDetailLine += format_SIN(strSin);
    			strDetailLine += carra_filler(carra_format(strSurname), 30, " ");
    			strDetailLine += carra_filler(carra_format(strFirstname), 20, " ");
    			strDetailLine += carra_filler(carra_format(strSurname), 30, " ");
    			strDetailLine += strBirthdate;
    			strDetailLine += strGender;
    			strDetailLine += carra_prefiller(String.valueOf(iNumberOfDay), 3, "0");
    			strDetailLine += carra_prefiller(format_FloatToString(bdContributorySalary), 8, "0");
    			strDetailLine += carra_prefiller(format_FloatToString(bdEmployee_Part), 7, "0");
    			strDetailLine += carra_prefiller(String.valueOf(iLeaveOfAbsenceWithoutPay), 3, "0");
    			strDetailLine += carra_prefiller(String.valueOf(bdWorkedTimePercentage), 2, "0");
    			strDetailLine += carra_prefiller(tempMonthIn+tempDateIn, 4, "0");
    			strDetailLine += carra_prefiller(tempMonthTo+tempDateTo, 4, "0");
    			strDetailLine += carra_prefiller(format_FloatToString(bdExoneratedSalary), 8, "0");
    			strDetailLine += carra_prefiller(String.valueOf(iDisabilityDays), 3, "0");
    			strDetailLine += carra_prefiller(Status.trim(), 1, "0");
    			strDetailLine += carra_prefiller(String.valueOf(iMotherhoodDays), 3, "0");
    			strDetailLine += carra_prefiller(format_FloatToString(bdMotherhoodSalary), 8, "0");
    			strDetailLine += carra_prefiller(format_FloatToString(bdNonContributorySalary), 8, "0");
    			strDetailLine += carra_prefiller(format_FloatToString(bdBaseSalary), 8, "0");
    			strDetailLine += carra_prefiller("", 3, " ");
    			strDetailLine += carra_prefiller("", 5, " ");
    			
    			//on ajoute cette ligne de detail au fichier et on termine la ligne
    			strNewFile.append(strDetailLine);
//    			strNewFile.append("\n");
    			
    			m_numRecordsLastRCE++;
    			m_iNbrTotal++;
    			
    		}
    		rs.close();
    		pstm.close();
    	}
    	catch(Exception e)
    	{
    		log.log(Level.WARNING, "P_InOutCarraTransfert.CreateDetail", e);
    		return false;
    	}
    	retValue = CreateEndLine( strNewFile, trxName );
    	return retValue;
    }
    
    public boolean CreateEndLine (StringBuffer NewFile, String trxName)
    {
		// Création du premier enregistrement
    	
    	String sql = " SELECT CARRA "
    		+ " FROM P_Equivalence_Factor_Param "
    		+ " WHERE P_Year_ID = "+m_iP_Year_ID;
    	//log.error(sql);
    	String strCARRANumber = "";
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	try
    	{ 
    		pstmt = DB.prepareStatement(sql, null);
    		rs = pstmt.executeQuery();
    		if (rs.next())
    		{
    			strCARRANumber = rs.getString("CARRA");
    		}
    		rs.close();
    		pstmt.close();
    	}
    	catch (SQLException e)
    	{
    		log.log(Level.WARNING, "P_InOutCarraTransfert.CreateHeader", e);
    	}
    	
    	String strYear = "";
    	P_Year theYear = P_Year.get(getCtx(), m_iP_Year_ID, trxName);
    	if(theYear != null)
    		strYear = String.valueOf(theYear.getYear());
    	
    	String strCodeRegime = m_lastCodeCarra;
    	
    	String strConstante = "3";
    	
     	
    	
    	String strFooter = "";
    	strFooter += carra_filler(carra_format(strCARRANumber), 8, " ");
    	strFooter += strYear;
    	strFooter += strCodeRegime.substring(0, 2);
    	strFooter += strConstante;
    	strFooter += carra_prefiller(String.valueOf(m_numRecordsLastRCE), 7, "0");
    	strFooter += carra_prefiller(format_FloatToString(m_bdCumulEmployee_Part), 14, "0");
    	strFooter += carra_filler( "", 155, " ");

    	//log.debug("CreateFileTransfertRBC.CreateHeader" + Protocol_rsn);
    	NewFile.append(strFooter);

//    	NewFile.append("\n");
    	//log.debug("CreateFileTransfertRBC.CreateHeader - Fin");
    	
    	return true;
    }
   
	public boolean WriteToFile(StringBuffer NewFile, String trxName)
	{
		String sql = "Select TransfertPath from P_System_Parameters ";
		PreparedStatement psdoc = null;
		try
		{
			psdoc = DB.prepareStatement(sql, null);
			ResultSet rsdoc = psdoc.executeQuery();
			if (rsdoc.next())
			{
				m_strPath = rsdoc.getString("TransfertPath");
	        }
			rsdoc.close();
			psdoc.close();
		}
		catch (SQLException e)
		{
			log.log(Level.WARNING, "P_InOutCarraTransfert.WriteToFile", e);
		}
		//log.debug("CreateFileTransfertRBC.WriteToFile - Start");
		String strFileName = "carra.txt";
		File aFile = null;
		aFile = new File(m_strPath + strFileName);
		//aFile.createNewFile();
		//log.debug("CreateFileTransfert.WriteToFile - StringBufferCapacity " + NewFile.capacity());
		FileOutputStream out; // declare a file output object
		PrintStream p; // declare a print stream object
		try
		{
			// Create a new file output stream
			// connected to "myfile.txt"
			out = new FileOutputStream(aFile);
	
			// Connect print stream to the output stream
			p = new PrintStream( out );
	
			//StringBuffer sb = new StringBuffer();
			//sb.insert(0, "lalala");
			//sb.append("This is written to a file333333");
			p.println (NewFile.toString());
			//p.println ("asdasd");
			System.out.println(NewFile.toString());
			p.close();
		}
		catch (Exception e)
		{
			System.out.println ("Error writing to file :" + e.toString());
		}
		//log.debug("CreateFileTransfertRBC.WriteToFile - End");
		
		return true;
	}

	
	private String carra_format(String theInput)
	{
		theInput = theInput.toUpperCase();
		theInput = theInput.replace('É', 'E');
		theInput = theInput.replace('È', 'E');
		theInput = theInput.replace('Ê', 'E');
		theInput = theInput.replace('À', 'A');
		theInput = theInput.replace('À', 'A');
		theInput = theInput.replace('Â', 'A');
		theInput = theInput.replace('Î', 'I');
		theInput = theInput.replace('Ï', 'I');
		theInput = theInput.replace('Ì', 'I');
		theInput = theInput.replace('Ö', 'O');
		theInput = theInput.replace('Ç', 'C');
		
		return theInput;
	}
	
	private String carra_filler(String theInput, int lengthDesired, String cFilledWith)
	{
		String retValue = "";
		//on verifie si on depasse pas la longueur permis, si oui, on cut
		if(theInput.length() > lengthDesired)
		{
			return theInput.substring(0, lengthDesired);
		}
		else if(theInput.length() == lengthDesired)
		{
			return theInput;
		}
		else//theInput.length() < lengthDesired
		{
			int toFill = lengthDesired - theInput.length();
			retValue = theInput;
			for(int idx=0;idx<toFill;idx++)
			{
				retValue += cFilledWith;
			}
		}
		
		return retValue;
	}
	//pour les nombres
	//donc on fill avant plutot qu'apres
	private String carra_prefiller(String theInput, int lengthDesired, String cFilledWith)
	{
		String retValue = "";
		//on verifie si on depasse pas la longueur permis, si oui, on lance une erreur (a cause que cEst un nombre)
		if(theInput.length() > lengthDesired)
		{
			log.log(Level.SEVERE, "P_InOutCarraTransfert.carra_prefiller - truncating number");
			return theInput.substring(0, lengthDesired);
		}
		else if(theInput.length() == lengthDesired)
		{
			return theInput;
		}
		else//theInput.length() < lengthDesired
		{
			int toFill = lengthDesired - theInput.length();
			retValue = "";
			for(int idx=0;idx<toFill;idx++)
			{
				retValue += cFilledWith;
			}
			retValue += theInput;
		}
		
		return retValue;
	}
	
	//pour les nombre a virgules
	private String format_FloatToString(BigDecimal bdValue)
	{
		String retValue = "";
		String temp = bdValue.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
		//on s'en va supprimmer la virgule (mais on laisse 2 chiffres apres)
		for(int idx = 0;idx<temp.length();idx++)
		{
			if(!temp.substring(idx, idx+1).equals(",") && !temp.substring(idx, idx+1).equals("."))
				retValue += temp.substring(idx, idx+1);
		}
		return retValue;
	}
	
	//pour les NAS
	private String format_SIN(String strInput)
	{
		String retValue = "";
		
		for(int idx = 0;idx < strInput.length();idx++)
		{
			if(!strInput.substring(idx, idx+1).equals("-"))
			{
				retValue += strInput.substring(idx, idx+1);
			}
		}
		
		return retValue;
	}
}
