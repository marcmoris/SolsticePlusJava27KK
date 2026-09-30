/*
 * Created on 2005-11-17
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * @author alenav01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_GenerateXMLT4 extends SvrProcess {

	private String _ClassErrorMessage="Error Generating XML T4";
	int PInstanceID=0;
	int ClientID = 0;
    int OrgID = 0; 
    int UserID = 0;
	int YearID=0;
	String StatementType;
	int EmployeeID = 0;
	int EmployerID = 0;
	private String m_Format = "PDF";

	/**
	 * 
	 */
	public P_GenerateXMLT4() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#prepare()
	 */
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		String paramName = "";
		
		PInstanceID = this.getAD_PInstance_ID();
		
        for (int i = 0; i < para.length; i++)
  	    {
  	        paramName = para[i].getParameterName();
  	      
  	        if (para[i].getParameter() == null)
  		    ;
  	        else if (paramName.equals("P_Year_ID"))
  	        	this.YearID = ((BigDecimal)para[i].getParameter()).intValue();
  	        else if (paramName.equals("StatementType"))
  	        	this.StatementType = para[i].getParameter().toString().trim();
  	        else if (paramName.equals("P_Employee_ID"))
  	        	this.EmployeeID = ((BigDecimal)para[i].getParameter()).intValue();
  	        else if (paramName.equals("P_Employer_ID"))
  	        	this.EmployerID = ((BigDecimal)para[i].getParameter()).intValue();
            else if (paramName.equals("CrystalFormat"))
				m_Format = (String)para[i].getParameter();
            else 
  		       log.log(Level.SEVERE, "prepare - Unknown Parameter: " + paramName);
  	    }
	}
	
	private void StartProcess()  throws Exception
	{
		PreparedStatement pstmt = null;
		String StoreProcedureStatement;
		try
		{
			StoreProcedureStatement="EXEC SP_GENERATEXMLT4 " + this.ClientID + ',' + this.OrgID + ',' + this.UserID + ',' + this.UserID + ',' + this.PInstanceID + "," + this.YearID + ",'" + this.StatementType + "'," + this.EmployerID + "," + this.EmployeeID;

			pstmt = DB.prepareStatement(StoreProcedureStatement, null);
			pstmt.execute();
			
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, _ClassErrorMessage + " [StartProcess] ", e); 
			throw e;
		}
	} 
	
	private boolean Validate() throws Exception
	{
		int Count=0;
		String SQLString = "SELECT ISNULL(COUNT(*),0) COUNT" +
						   " FROM " + 
						   " P_FORM F "+
						   "INNER JOIN P_FORM_EMPLOYEE FE ON F.P_FORM_ID = FE.P_FORM_ID " +
						   "WHERE " +
						   "F.VALUE='T4' " +
						   " AND FE.P_YEAR_ID=" + this.YearID +
						   " AND FE.FORMTYPE='" + this.StatementType + "'"; 
		
		if (this.EmployeeID!=0)
		{
			SQLString += " AND FE.P_EMPLOYEE_ID = " + this.EmployeeID;
		}
		if (this.EmployerID!=0)
		{
			SQLString += " AND FE.P_EMPLOYER_ID = " + this.EmployerID;
		}
		
	    try
		{       	     		
			PreparedStatement pstmp = null;
			pstmp = DB.prepareStatement(SQLString, null);
            ResultSet rs = pstmp.executeQuery();

            while ( rs.next() )
            {
                Count=rs.getInt("COUNT");
            }
            rs.close();
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, _ClassErrorMessage + " [AddDateParameters] ", e);
			throw e;
		}

		return (Count>0);
	}
	
	private String GetFileName()
	{
		int SubmissionNumber=0;
		String SQLString = "SELECT SUBMISSIONNUMBER FROM P_FORM_PARAM WHERE TYPE='F'";

		try
		{       	     		
			PreparedStatement pstmp = null;
			pstmp = DB.prepareStatement(SQLString, null);
			ResultSet rs = pstmp.executeQuery();
			
			while ( rs.next() )
			{
				SubmissionNumber = rs.getInt("SUBMISSIONNUMBER");
			}
			rs.close();
			pstmp = null;
	
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, _ClassErrorMessage + " [GetFileName] ", e);
		}
		
		return "T4"+GetStringSequence(SubmissionNumber);
	}
	
	private String GetStringSequence(int iSequence)
	{
		return InsertZero(3-String.valueOf(iSequence).length()) + String.valueOf(iSequence); 
	}
	
	private String InsertZero(int Pos)
	{
		String Zeros="";
    	for (int i = 0; i < Pos; i++)
    	{
    		Zeros+="0";
    	}
    	return Zeros;
	}
	
	private String GetYear()
	{
		String Year="00";
		String SQLString = "SELECT SUBSTRING(CONVERT(CHAR(4),YEAR),3,2) YEAR FROM P_YEAR WHERE P_YEAR_ID=" + this.YearID;

		try
		{       	     		
			PreparedStatement pstmp = null;
			pstmp = DB.prepareStatement(SQLString, null);
			ResultSet rs = pstmp.executeQuery();
			
			while ( rs.next() )
			{
				Year = rs.getString("YEAR");
			}
			rs.close();
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, _ClassErrorMessage + " [AddDateParameters] ", e);
		}
		
		return Year;
	}
	
	private void AddDateParameters() throws Exception
	{
		ClientID = this.getAD_Client_ID(); 
        OrgID = Env.getAD_Org_ID(Env.getCtx()); 
        UserID = this.getAD_User_ID();
        
        try
		{        
        	String SQLInsert = "INSERT INTO AD_PINSTANCE_PARA (AD_PINSTANCE_ID, SEQNO, PARAMETERNAME, P_STRING, P_STRING_TO, P_NUMBER, P_NUMBER_TO, P_DATE, P_DATE_TO, INFO, INFO_TO, AD_CLIENT_ID, AD_ORG_ID, CREATED, CREATEDBY, UPDATED, UPDATEDBY, ISACTIVE) " +
	        "SELECT " + this.PInstanceID + ", " +
	        "(SELECT ISNULL(MAX(SEQNO) + 1,0) FROM AD_PINSTANCE_PARA WHERE AD_PINSTANCE_ID = " + this.PInstanceID + "), " +
	        "'AD_PInstance_ID',NULL, NULL," + this.PInstanceID + ", NULL,NULL, NULL, NULL, NULL, " + this.ClientID + ", " + this.OrgID + ", GETDATE(), " + this.UserID + ", GETDATE(), " + UserID + ", 'Y'";
        		
			PreparedStatement pstmp = null;
			
			pstmp = DB.prepareStatement(SQLInsert, null);
			pstmp.execute();
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, _ClassErrorMessage + " [AddDateParameters] ", e);
			throw e;
		}
	}
	
	
	private void CreateFile(StringBuffer Nf) throws Exception
    {
        String SqlEntry = "SELECT * FROM P_T_XMLFILE WHERE AD_PINSTANCE_ID=" + this.PInstanceID + " ORDER BY LINE";
        
        PreparedStatement pstmp = null;
        try
        {
            pstmp = DB.prepareStatement(SqlEntry, null);
            ResultSet rs = pstmp.executeQuery();

            while ( rs.next() )
            {
                Nf.append(rs.getString("TAGXML") + "\n");
            }
            rs.close();
        }
        catch (Exception e)
        {
        	log.log (Level.SEVERE, _ClassErrorMessage + " [CreateFile] ", e);
        	throw e;
        }       
    }
	
    private void WriteToFile(StringBuffer Nf) throws Exception
    {
        String Path = "";
        String FileName = "";
       	String sql = "Select TransfertPath from P_System_Parameters ";
        PreparedStatement psdoc = null;
       try
  	   { 
            psdoc = DB.prepareStatement(sql, null);
            ResultSet rsdoc = psdoc.executeQuery();
            if (rsdoc.next())
            {
                
                Path = rsdoc.getString("TransfertPath");
            }
            rsdoc.close();
           psdoc.close();
        }
        catch (SQLException e)
        {
            log.log(Level.WARNING, "P_GenerateXMLT4.WriteToFile", e);
        }
        log.log(Level.WARNING, "P_GenerateXMLT4.WriteToFile - Path" + Path);
    	//log.debug("CreateFileTransfertRBC.WriteToFile - Start");
    	FileName = GetFileName();
  	    File aFile = null;
  	    aFile = new File(Path + FileName + ".xml");
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
              p = new PrintStream(out, false, "UTF-8");

              //StringBuffer sb = new StringBuffer();
              //sb.insert(0, "lalala");
              //sb.append("This is written to a file333333");
              p.println (Nf.toString().trim());
              //p.println ("asdasd");
              System.out.println(Nf.toString());
              p.close();
        }
        catch (Exception e)
        {
        	log.log (Level.SEVERE, _ClassErrorMessage + " [WriteToFile] ", e);
        }
        
    }
	
			
	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	protected String doIt() throws Exception {
		try
		{
			if (Validate())
			{

				StringBuffer Nf = new StringBuffer();
				AddDateParameters();
				StartProcess();	
				CreateFile(Nf);
				WriteToFile(Nf);
				
			    String sql = "SELECT WEBAPPURL "
			    	+ " FROM P_SYSTEM_PARAMETERS ";
			        PreparedStatement pstmt = DB.prepareStatement(sql, null);
			        ResultSet rs = pstmt.executeQuery();
			        String srvUrl = "";
			    if(rs.next())
			       	srvUrl = rs.getString(1);
			        
			    if(srvUrl.length() == 0)
			    {
			      	//TODO Error Msg avec Logger
			      	System.out.println("Error With System Parameters");
			       	return "Error With System Parameters";
			    }
		        int l_Role = Env.getAD_Role_ID(Env.getCtx());
		        String repUrl = srvUrl+"Crystal/reports.jsp?AD_PInstance_ID=" + this.PInstanceID + "&Format=" + m_Format + "&Ad_Role_ID=" + l_Role;
			    Env.startBrowser(repUrl);
				return "Terminé avec succès";
			}
			else
			{
				return "Aucun enregistrement sélectionné";
			}
		}
		catch (Exception e)
		{
			return "Error: " + e.toString();
		}
	}

}
