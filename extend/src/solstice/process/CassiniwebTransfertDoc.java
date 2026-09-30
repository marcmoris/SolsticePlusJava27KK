package solstice.process;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.PrintStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.compiere.Compiere;
import org.compiere.model.MActivity;
import org.compiere.model.MCountry;
import org.compiere.model.MLocation;
import org.compiere.model.MOrg;
import org.compiere.model.MPInstance;
import org.compiere.model.MRegion;
import org.compiere.model.MUser;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/*
import com.sshtools.net.SocketTransport;
import com.sshtools.sftp.SftpClient;
import com.sshtools.ssh.PasswordAuthentication;
import com.sshtools.ssh.SshAuthentication;
import com.sshtools.ssh.SshClient;
import com.sshtools.ssh.SshConnector;
import com.sshtools.ssh2.Ssh2Client;
import com.sshtools.ssh2.Ssh2Context;
*/

import solstice.model.P_Department;
import solstice.model.P_Employee;

import solstice.model.P_Year;
import solstice.utils.PgiUtil;

public class CassiniwebTransfertDoc extends SvrProcess {

	private String trxName = null;

//	private String EXPORT_LOC = "\\\\192.168.3.194\\humanRess$\\Payroll\\Avantax eForms 2018\\PDF Output\\Web\\";
	private String EXPORT_LOC = "";
	private String scriptFileName;
	private File scriptFile;

	private int P_Year_ID = 1000126
			;

	private static CLogger log = CLogger.getCLogger(CassiniwebTransfertDoc.class);

	public CassiniwebTransfertDoc() {

	}

	public CassiniwebTransfertDoc(boolean Standalone) {
		try {
			this.P_Year_ID = 1000126;  // 1000126 = 2020
			
			String path = PgiUtil.getSolsticeParameter(Env.getCtx(), "AvantaxPath");
			if ( path.endsWith( File.separator ) == false )
				path = path + File.separator;

		   	path = "S:\\Avantax eForms 2020\\PDF Output\\Cassiniweb\\";

			EXPORT_LOC = path; //+ "Export" + File.separator;


			
			
			this.doIt();
		} catch (Exception e) {
			log.log(Level.SEVERE, "Error :", e);
		}

	}

	protected void prepare() {
		MPInstance instance = new MPInstance(Env.getCtx(), this.getAD_PInstance_ID(), null);
		int process_id = instance.getAD_Process_ID();

		String paramName = "";
		ProcessInfoParameter[] para = getParameter();

		for (int i = 0; i < para.length; i++) {
			paramName = para[i].getParameterName();
			if (paramName.equals("P_Year_ID")) {
				this.P_Year_ID = para[i].getParameterAsInt();
			}
		}
		String path = PgiUtil.getSolsticeParameter(Env.getCtx(), "AvantaxPath");
		if ( path.endsWith( File.separator ) == false )
			path = path + File.separator;

		EXPORT_LOC = path; //+ "Export" + File.separator;

	}

	static final int BUFFER = 2048;

	private MessageDigest m_md = null;
	private P_Employee Employee;
	private P_Year Year;

	StringBuffer xmlStr;
	String zipFileName;
	FileInputStream fi;

	BufferedInputStream origin = null;
	CheckedOutputStream checksum = null;
	ZipOutputStream out = null;
	
	File f ;
	
	protected String doIt() throws Exception {

		f = new File(EXPORT_LOC);

		Year = P_Year.get(Env.getCtx(), P_Year_ID, trxName);

//		createEmloyeeFile();
		
		// P_Employee_ID not in( 1001285, 1001114, 1001316 ) and
		//taxation_region_id = 166 and
		String sql = "Select Distinct AD_Org_ID, Value, Name FROM AD_Org WHERE Isactive = 'Y' AND ad_org_id > 0";
//		String sql = "Select Distinct AD_Org_ID, Value, Name FROM AD_Org WHERE Isactive = 'Y' AND ad_org_id = 0";
		
//		String sql = "Select Distinct AD_Org_ID, Value, Name FROM AD_Org WHERE Isactive = 'Y' AND ad_org_id IN ( 1000011,1000012 ) ";
		
//		String sql = "Select Distinct AD_Org_ID, Value, Name FROM AD_Org WHERE Isactive = 'Y' AND VALUE = '2C0E' ";
		
		PreparedStatement stmt = DB.prepareStatement(sql, null);
		ResultSet rs = stmt.executeQuery();
		try {

			while (rs.next()) {

				//
				// ZIP FILE
				//
				zipFileName = EXPORT_LOC + "D95AE66B4AC0F6105B0B8ECDB9892D44_employee_document" + "_" + rs.getString( "Value" ) + "_" + Year.getYear() + "_T4.zip";
				FileOutputStream dest = new FileOutputStream(zipFileName);
				origin = null;
				checksum = new CheckedOutputStream(dest, new Adler32());
				out = new ZipOutputStream(new BufferedOutputStream(checksum));

				out.setMethod(ZipOutputStream.DEFLATED);
				out.setLevel(Deflater.BEST_COMPRESSION);

				byte data[] = new byte[BUFFER];

				
				xmlStr = new StringBuffer("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
				xmlStr = xmlStr.append("<data>");
				xmlStr = xmlStr.append("<sat.employee_document>");
		
				
				addEmployeeFile( rs.getInt( "AD_Org_ID" ));

				xmlStr = xmlStr.append("</sat.employee_document>");
				xmlStr = xmlStr.append("</data>");
				
				// Create XML FILE
				String xmlFileName = "D95AE66B4AC0F6105B0B8ECDB9892D44_employee_document" + "_" + rs.getString( "Value" ) + "_"  + Year.getYear() + ".xml";
				File xmlFile = new File(EXPORT_LOC + xmlFileName);
				FileOutputStream outxml = new FileOutputStream(xmlFile, false);
				PrintStream p = new PrintStream(outxml);
				p.println(xmlStr);
				p.close();
				// Close XML File.

				// ADD Xml file to zip file
				System.out.println("Adding: " + xmlFileName);
				fi = new FileInputStream(EXPORT_LOC + xmlFileName);
				origin = new BufferedInputStream(fi, BUFFER);
				ZipEntry entry = new ZipEntry(xmlFileName);
				out.putNextEntry(entry);
				int count;
				while ((count = origin.read(data, 0, BUFFER)) != -1) {
					out.write(data, 0, count);
				}
				origin.close();

				out.close();

				//FTP 
//		        SendFileSFTP( zipFileName );

			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "getFormEmployeeInfo", e);
		}

		rs.close();
		stmt.close();

		String msg = "";
		return "@Processed@ " + msg;

	}
	
	protected String addEmployeeFile( int ad_org_id ) throws Exception {
		
		String pdfFiles[] = f.list();

		try {
			m_md = MessageDigest.getInstance("MD5");
			// m_md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		}


		// P_Employee_ID not in( 1001285, 1001114, 1001316 ) and
		//taxation_region_id = 166 and
		String sql = "Select Distinct P_Employee_ID from P_Employee Where AD_Org_ID = " + ad_org_id + " AND EXISTS( select 1 FROM p_form_employee WHERE  p_form_employee.p_employee_id = p_employee.p_employee_id and  p_form_employee.P_Year_ID = "
				+ P_Year_ID  + " ) order by p_employee_id";

//		String sql = "Select Distinct P_Employee_ID, Value from P_Employee Where AD_Org_ID = " + ad_org_id + " order by Value";
//		String sql = "Select Distinct P_Employee_ID, Value from P_Employee Where AD_Org_ID = " + ad_org_id + " order by Value";
/*
		String sql = "Select Distinct P_Employee_ID, Value from P_Employee Where AD_Org_ID NOT IN ( 1000011,1000012 ) "
				+ " AND exists( select 1 from p_form_employee where p_form_employee.p_employee_id = p_employee.p_employee_id and p_year_id = 1000116 ) "
				+ " AND P_Employee.value in ( '001866' )"
				+ "order by Value";
	*/	

	/*	
		sql = "Select Distinct P_Employee_ID from P_Employee "
				+ " Where sin in ( '273-261-511','260-185-681', '123-571-580','265-225-011') "
				+ " AND AD_Org_ID = " + ad_org_id 
				+ " AND EXISTS( select 1 FROM p_form_employee WHERE  p_form_employee.p_employee_id = p_employee.p_employee_id and  p_form_employee.P_Year_ID = "
				+ P_Year_ID  + " ) "
				+ " order by p_employee_id";
		*/
		PreparedStatement stmt = DB.prepareStatement(sql, null);
		ResultSet rs = stmt.executeQuery();

		try {

			// EXPORT_LOC = PgiUtil.getSolsticeParameter(getCtx(),
			// "CassiniwebPDFPath");
//			EXPORT_LOC = "C:\\SolsticeTest\\PDF\\";
			// EXPORT_LOC = EXPORT_LOC + Year.getYear() + "\\";
/*
			File dir = new File(EXPORT_LOC);
			if (!dir.exists())
				dir.mkdir();
			dir = new File(EXPORT_LOC);
			if (!dir.exists()) {
				System.out.println("Cannot create directory " + EXPORT_LOC);
				System.exit(1);
			}
*/

			while (rs.next()) {


				Employee = P_Employee.get(Env.getCtx(), rs.getInt("P_Employee_ID"), null);
				// Form = P_Form.get( Env.getCtx(), rs.getInt("P_Form_ID"), null);
				if ( fileExists( Employee, pdfFiles  ))
				{

					// get a list of files from current directory

					// XML


					// xmlStr = xmlStr.append( getFormEmployeeInfo(
					// Employee.getP_Employee_ID() ) );
					String desc1 = "RELEVE T4";
					String desc2 = "RELEVE T4";
					String fileType = "can";

					//
					//
					//

					// Add Pdf file to zip file.
					for (int i = 0; i < pdfFiles.length; i++) {
						if (pdfFiles[i].endsWith(".pdf")
								&& pdfFiles[i].contains(Employee.getSin().replace("-", ""))) {

							if (pdfFiles[i].startsWith("T4") || pdfFiles[i].startsWith("TR") || pdfFiles[i].startsWith("TP") || pdfFiles[i].startsWith("TA")  ) {
								desc1 = "RELEVE T4";
								desc2 = "RELEVE T4";
								fileType = "can";
							}
							if (pdfFiles[i].startsWith("R1")) {
								fileType = "que";
								desc1 = "RELEVE R1";
								desc2 = "RELEVE R1";
							}

							// boucle sur employés
							xmlStr = xmlStr.append("<employee_document>");
							xmlStr = xmlStr.append("<employee_id>" + Employee.getP_Employee_ID() + "</employee_id>");
							xmlStr = xmlStr.append("<employee_code>" + Employee.getValue() + "</employee_code>");
//							xmlStr = xmlStr.append( "<unique_guid>" + Employee.getUnique_GUID() + "</unique_guid>" );
							xmlStr = xmlStr.append("<desc1>" + Year.getYear() + "</desc1>");
							xmlStr = xmlStr.append("<desc2>" + Year.getYear() + "</desc2>");
							xmlStr = xmlStr.append("<filename/>");
							xmlStr = xmlStr.append("<filetype>dirpay</filetype>");
							xmlStr = xmlStr.append("<owner_id/>");
							xmlStr = xmlStr.append("<permission>rwd</permission>");

							// boucle sur les formulaire
							xmlStr = xmlStr.append("<employee_document>");
							xmlStr = xmlStr.append("<employee_id>" + Employee.getP_Employee_ID() + "</employee_id>");
							xmlStr = xmlStr.append("<employee_code>" + Employee.getValue() + "</employee_code>");
							xmlStr = xmlStr.append("<desc1>" + desc1 + "</desc1>");
							xmlStr = xmlStr.append("<desc2>" + desc2 + "</desc2>");

							xmlStr = xmlStr.append("<filename>" + pdfFiles[i] + "</filename>");
							xmlStr = xmlStr.append("<filetype>" + fileType + "</filetype>");
							xmlStr = xmlStr.append("<permission>rwd</permission>");
							xmlStr = xmlStr.append("</employee_document>");

							xmlStr = xmlStr.append("</employee_document>");

							byte data[] = new byte[BUFFER];

							//
							//
							//
							log.log( Level.WARNING , "Adding: " + pdfFiles[i] );
							// it is a .pdf file!
							FileInputStream fi = new FileInputStream(EXPORT_LOC + pdfFiles[i]);
							origin = new BufferedInputStream(fi, BUFFER);
							ZipEntry entry = new ZipEntry(pdfFiles[i]);
							try {
								out.putNextEntry(entry);
							} catch (Exception e) {
								log.log(Level.SEVERE, "getFormEmployeeInfo", e);
							}
							int count;
							while ((count = origin.read(data, 0, BUFFER)) != -1) {
								try {
									out.write(data, 0, count);
								} catch (Exception e) {
									log.log(Level.SEVERE, "getFormEmployeeInfo -2 ", e);
								}
							}
							origin.close();

						}
					}



				} // isPrintEndOfYearForm() == false

			}

			rs.close();
			stmt.close();

		} catch (Exception e) {
			log.log(Level.SEVERE, "getFormEmployeeInfo", e);
		}

		String msg = "";
		return "@Processed@ " + msg;

	}

	private boolean fileExists( P_Employee Employee, String pdfFiles[] ) {
		
		boolean flg = false; 
		for (int i = 0; i < pdfFiles.length; i++) {
			if (pdfFiles[i].endsWith(".pdf") 
					&& pdfFiles[i].contains(Employee.getSin().replace("-", ""))) {
				flg = true;
			}
		}
		return flg;
		
	}

	
	protected String createEmloyeeFile(  String pdfFiles[] ) throws Exception
	{
		
    	StringBuffer xmlStr = new StringBuffer( "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");

    	xmlStr = new StringBuffer( "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");

    	
		String sql = "Select Distinct P_Employee_ID, Value from P_Employee order by Value";
    	
    	PreparedStatement stmt = DB.prepareStatement(sql, null);
        try
        {

			xmlStr = xmlStr.append( "<titan>");
			xmlStr = xmlStr.append( getCompanyBranchDeptInfo() ); 
					

	        ResultSet rs = stmt.executeQuery();
	        while(rs.next())
	        {
					P_Employee Employee = P_Employee.get( Env.getCtx(), rs.getInt("P_Employee_ID"), null);

					if ( fileExists( Employee, pdfFiles ) ) {
						MUser User = null;
						if (  Employee.getAD_User_ID() != 0 )
							User = new MUser( Env.getCtx(), Employee.getAD_User_ID(), null);
						
						MLocation Location = MLocation.get( Env.getCtx(), Employee.getC_Location_ID(), null);
						MRegion Region = new MRegion( Env.getCtx(), Location.getC_Region_ID(), null);
						MCountry Country = new MCountry( Env.getCtx(), Location.getC_Country_ID(), null);
						
						MActivity Activity = new MActivity( Env.getCtx(), Employee.getC_Activity_ID(), null);
						P_Department Department = P_Department.get( Env.getCtx(), Employee.getP_Department_ID(), null);
						MOrg Org = new MOrg( Env.getCtx(), Employee.getAD_Org_ID(), null);
						
						xmlStr = xmlStr.append( "<employee code=\"" + Employee.getValue() + "\">");
						addElement( xmlStr, "employee_id", rs.getString("P_Employee_ID") );
						addElement( xmlStr, "employee_code" , Employee.getValue() ) ;
						if ( Employee.isActive() )
							addElement( xmlStr, "employee_is_active" , "1" ) ;
						else
							addElement( xmlStr, "employee_is_active" , "0" ) ;
						
						
						addElement( xmlStr, "employee_first_name" , Employee.getFirstName() );
						addElement( xmlStr, "employee_last_name"  , Employee.getSurname() );
						addElement( xmlStr, "employee_sex" ,  Employee.getGender() );
						if ( Employee.getP_Language_ID() == 127) {
							addElement( xmlStr, "employee_language" , "1" );
						}
						else {
							addElement( xmlStr, "employee_language" , "2" );
						}	
						addElement( xmlStr, "employee_prefix" ,  null );
						addElement( xmlStr, "company_id" , String.valueOf(Employee.getAD_Org_ID()) );
						addElement( xmlStr, "company_code" , Org.getValue() );
						addElement( xmlStr, "branch_id" , String.valueOf(Employee.getC_Activity_ID()) );
						addElement( xmlStr, "branch_code" , Org.getValue() + '-' + Activity.getValue() );
						if ( Employee.getP_Department_ID() == 1000000 )
						{
							String dept_id = String.valueOf( Org.getAD_Org_ID() + ( 23 * 1000 ) );
							addElement( xmlStr, "dept_id" ,  dept_id );
							
						}
						else {
							String dept_id = String.valueOf( Org.getAD_Org_ID() + ( Employee.getP_Department_ID() * 1000 ) );
							addElement( xmlStr, "dept_id" ,  dept_id );
						}

//						addElement( xmlStr, "dept_id" ,  String.valueOf(Employee.getP_Department_ID()) );
						addElement( xmlStr, "dept_code" , Org.getValue() + '-' + Department.getValue()   );
						{
							addElement( xmlStr, "employee_is_admin" , "0" ) ;
							addElement( xmlStr, "employee_email" ,  Employee.getEMail() );
						}
						addElement( xmlStr, "company_name" ,  Org.getValue() + " " + Org.getName() );
						addElement( xmlStr, "last3_nas" ,  Employee.getSin().substring(8) );
//						addElement( xmlStr, "unique_guid",  Employee.getUnique_GUID() );
						addElement( xmlStr, "employee_personalemail" , Employee.getPersonalEmail() );
		        		

		        		addElement( xmlStr, "username" , Employee.getValue()  );

		        		
		    	        // Create an instance of SimpleDateFormat used for formatting 
		    			// the string representation of date (month/day/year)
		    			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		    			// Using DateFormat format method we can create a string 
		    			// representation of a date with the defined format.
		        		
						xmlStr = xmlStr.append( "</employee>");
						
					}
				
				}
//	        }

			xmlStr = xmlStr.append( "</titan>");

	        rs.close();
	        stmt.close();

	        // Create an instance of SimpleDateFormat used for formatting 
			// the string representation of date (month/day/year)
			DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

			// Get the date today using Calendar object.
			Date today = Calendar.getInstance().getTime();        
			// Using DateFormat format method we can create a string 
			// representation of a date with the defined format.
			String reportDate = df.format(today);
			
			String xmlFileName =  "D95AE66B4AC0F6105B0B8ECDB9892D44_Employee" + "_" + Year.getYear() + ".xml" ;
    		File xmlFile = new File( EXPORT_LOC + xmlFileName );
    		FileOutputStream out = new FileOutputStream(xmlFile, false);
    		PrintStream p = new PrintStream(out); 
    		p.println(xmlStr);
    		p.close();

    		String ftpFileName = "D95AE66B4AC0F6105B0B8ECDB9892D44_Employee" +  "_" + ".zip";
    		String employeeFileName = EXPORT_LOC + "D95AE66B4AC0F6105B0B8ECDB9892D44_Employee" +  "_" + Year.getYear() + ".zip";
    		try {
    			BufferedInputStream origin = null;
    			FileOutputStream dest = new FileOutputStream( employeeFileName );
    			CheckedOutputStream checksum = new CheckedOutputStream(dest, new Adler32());
    			ZipOutputStream outZip = new ZipOutputStream(new BufferedOutputStream(checksum));
    			
    			outZip.setMethod(ZipOutputStream.DEFLATED);
    			outZip.setLevel(Deflater.BEST_COMPRESSION);
    			         
    			byte data[] = new byte[BUFFER];
    	        // get a list of files from current directory
  	        
   	            System.out.println("Adding: "+xmlFileName);
   	            FileInputStream fi = new FileInputStream( EXPORT_LOC + xmlFileName );
   	            origin = new BufferedInputStream(fi, BUFFER);
   	            ZipEntry entry = new ZipEntry( xmlFileName );
   	            outZip.putNextEntry(entry);
   	            int count;
   	            while((count = origin.read(data, 0, BUFFER)) != -1) {
   	            	outZip.write(data, 0, count);
   	            }
   	            origin.close();
   	            outZip.close();

   	            
    		} catch(Exception e) {
    	         e.printStackTrace();
    	    }
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
        }
   	
//        SendFileSFTP( );

        return "";

    }
    
    private void addElement( StringBuffer xmlStr, String Element, String value)
    {
    	//PgiUtil.convertHTMLString( 
    	if ( value != null)
    		xmlStr = xmlStr.append(  new StringBuffer( "<" + Element + ">" + value  + "</" + Element + ">" ));
    	else
    		xmlStr = xmlStr.append(  new StringBuffer( "<" + Element + ">" + "</" + Element + ">" ));
    }
    

	
	
	private String getIP_Address() {
		return PgiUtil.getSolsticeParameter(Env.getCtx(), "CassiniwebFtpServer");
		// return "canadianhelicopters.cassiniweb.com";
	}

	private String getUserName() {
		return PgiUtil.getSolsticeParameter(Env.getCtx(), "CassiniwebFtpUser");
		// return "canadianhelicopters";
	}

	private String getPassword() {
		return PgiUtil.getSolsticeParameter(Env.getCtx(), "CassiniwebFtpPwd");
		// return "caHe2798!gT67sR3";
	}
/*
	private boolean SendFileSFTP( String fileName) throws Exception {

		boolean success = true;

		String hostname = getIP_Address();
		String username = getUserName();
		String password = getPassword();
		int port = 1122;

		String cmd = null;

		
		 // Create an SshConnector instance
		 
		SshConnector con = SshConnector.createInstance();

		// Lets do some host key verification

		// con.getContext().setHostKeyVerification( new
		// ConsoleKnownHostsKeyVerification());
		con.getContext().setPreferredPublicKey(Ssh2Context.PUBLIC_KEY_SSHDSS);

		
		// Connect to the host
		 
		SocketTransport t = new SocketTransport(hostname, port);
		t.setTcpNoDelay(true);

		SshClient ssh = con.connect(t, username, true);

		Ssh2Client ssh2 = (Ssh2Client) ssh;
		
		// Authenticate the user using password authentication
		 
		PasswordAuthentication pwd = new PasswordAuthentication();

		do {
			System.out.print("Password: ");
			pwd.setPassword(password);
		} while (ssh2.authenticate(pwd) != SshAuthentication.COMPLETE && ssh.isConnected());

		
		 // Start a session and do basic IO
		 
		if (ssh.isAuthenticated()) {

			SftpClient sftp = new SftpClient(ssh2);

			
			 // Perform some text mode operations
			 
			sftp.setTransferMode(SftpClient.MODE_BINARY);

//ICI			sftp.put( fileName);

			sftp.quit();
		}

		ssh.disconnect();
		return success;

	}
*/
	public String getDigest(String value) {
		if (m_md == null) {
			try {
				m_md = MessageDigest.getInstance("MD5");
				// m_md = MessageDigest.getInstance("SHA-1");
			} catch (NoSuchAlgorithmException nsae) {
				nsae.printStackTrace();
			}
		}
		// Reset MessageDigest object
		m_md.reset();
		// Convert String to array of bytes
		byte[] input = value.getBytes();
		// feed this array of bytes to the MessageDigest object
		m_md.update(input);
		// Get the resulting bytes after the encryption process
		byte[] output = m_md.digest();
		m_md.reset();
		//
		return convertToHexString(output);
	} // getDigest

	/**************************************************************************
	 * Convert Byte Array to Hex String
	 * 
	 * @param bytes
	 *            bytes
	 * @return HexString
	 */
	public static String convertToHexString(byte[] bytes) {
		// see also Util.toHex
		int size = bytes.length;
		StringBuffer buffer = new StringBuffer(size * 2);
		for (int i = 0; i < size; i++) {
			// convert byte to an int
			int x = bytes[i];
			// account for int being a signed type and byte being unsigned
			if (x < 0)
				x += 256;
			String tmp = Integer.toHexString(x);
			// pad out "1" to "01" etc.
			if (tmp.length() == 1)
				buffer.append("0");
			buffer.append(tmp);
		}
		return buffer.toString();
	} // convertToHexString

	
	private StringBuffer getCompanyBranchDeptInfo(){
		StringBuffer xmlStr = new StringBuffer( "");
	
    	String sql = "Select P_Employee.AD_Org_ID,  max(AD_Org.Value) Company_code "
			   		+ "	, max( AD_Org.Name ) as Company_Desc1 "
 			   		+ " , max( AD_Org.Description ) as Company_Desc2 "
					+ "From P_Employee  "
					+ "Inner Join AD_Org ON AD_Org.AD_Org_ID = P_Employee.AD_Org_ID "
//					+ "Where P_Employee.P_Employee_ID IN ( Select P_Employee_ID From P_Statement_Earning Where grossEarningsPeriod <> 0 AND P_Period_ID = " + P_Period_ID + ") "
					+ "Group BY P_Employee.AD_Org_ID "
					+ "ORDER BY P_Employee.AD_Org_ID" ;
		
		
    	PreparedStatement stmt = DB.prepareStatement(sql, null);
        try
        {
	        ResultSet rs = stmt.executeQuery();
	        while(rs.next())
	        {
	        	xmlStr = xmlStr.append( "<company code=\"" + rs.getString("Company_Code") + "\">");

				addElement( xmlStr, "company_id" , rs.getString("AD_Org_ID") );
		        addElement( xmlStr, "company_code" , rs.getString("Company_Code" ) ) ;
				addElement( xmlStr, "company_name" , rs.getString("Company_Desc1" ) ) ;
				addElement( xmlStr, "company_desc1" , rs.getString("Company_Desc1" ) ) ;
				addElement( xmlStr, "company_desc2" , rs.getString("Company_Desc2" ) ) ;
				addElement( xmlStr, "contact_name" , "Maryse Lafond" ) ;
				addElement( xmlStr, "contact_email" , "mlafond@canadianhelicopters.com" ) ;
				
				xmlStr = xmlStr.append( getBranchDeptInfo( rs.getInt("AD_Org_ID") ) );
		        xmlStr = xmlStr.append( "</company>");
	        }


	        rs.close();
	        stmt.close();
        }
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getCompanyBranchDeptInfo", e);
		}
        return xmlStr;
	}

	private StringBuffer getBranchDeptInfo( int AD_Org_ID ){
		StringBuffer xmlStr = new StringBuffer( "");
	
    	String sql = "Select P_Employee.C_Activity_ID as Branch_ID "
					+ ", max(AD_Org.Value + '-' + C_Activity.Value ) as branch_code "
					+ ", max(C_Activity_TRL.Name )  as branch_desc1 "
					+ ", max(C_Activity.Name )  as branch_desc2 "
					+ "From P_Employee  "
					+ "Inner Join AD_Org ON AD_Org.AD_Org_ID = P_Employee.AD_Org_ID "
					+ "Inner Join C_Activity ON C_Activity.C_Activity_ID = P_Employee.C_Activity_ID "
					+ "Inner Join C_Activity_TRL ON C_Activity_Trl.C_Activity_ID = P_Employee.C_Activity_ID  AND AD_Language = 'fr_CA'"
					+ "Where P_Employee.AD_Org_ID = " + AD_Org_ID 
					+ "Group BY P_Employee.C_Activity_ID "
					+ "ORDER BY P_Employee.C_Activity_ID" ;
		
		
    	PreparedStatement stmt = DB.prepareStatement(sql, null);
        try
        {
	        ResultSet rs = stmt.executeQuery();
	        while(rs.next())
	        {
				xmlStr = xmlStr.append("<branch code=\"" + rs.getString("branch_code") + "\">" ) ;
				addElement( xmlStr, "branch_id" , rs.getString( "branch_id")) ;
				addElement( xmlStr, "branch_code" , rs.getString( "branch_code") ) ;
				addElement( xmlStr, "branch_desc1" , rs.getString( "branch_desc1") ) ;
				addElement( xmlStr, "branch_desc2" , rs.getString( "branch_desc2") ) ;

				xmlStr = xmlStr.append( getDeptInfo( AD_Org_ID, rs.getInt("Branch_ID") ) );

				xmlStr = xmlStr.append( "</branch>");

	        }

	        rs.close();
	        stmt.close();
        }
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getBranchDeptInfo", e);
		}
        return xmlStr;
	}

	private StringBuffer getDeptInfo( int AD_Org_ID,  int C_Activity_ID ){
		StringBuffer xmlStr = new StringBuffer( "");
	
    	String sql = "Select AD_Org.AD_Org_ID + ( CASE WHEN P_Employee.P_Department_ID = 1000000 THEN 23 else P_Employee.P_Department_ID END * 1000 ) as Dept_ID "
					+ ", max(AD_Org.Value + '-' + P_Department.Value ) as dept_code "
					+ ", max(P_Department_Trl.Name ) as dept_desc1 "
					+ ", max(P_Department.Name ) as dept_desc2 "
					+ "From P_Employee  "
					+ "Inner Join AD_Org ON AD_Org.AD_Org_ID = P_Employee.AD_Org_ID "
					+ "Inner Join P_Department ON P_Department.P_Department_ID = P_Employee.P_Department_ID "
					+ "Inner Join P_Department_Trl ON P_Department_Trl.P_Department_ID = P_Department.P_Department_ID AND AD_Language = 'fr_CA'"
					+ "Where P_Employee.C_Activity_ID = " + C_Activity_ID
					+ "Group BY AD_Org.AD_Org_ID, P_Employee.P_Department_ID "
					+ "ORDER BY AD_Org.AD_Org_ID, P_Employee.P_Department_ID" ;
		
		
    	PreparedStatement stmt = DB.prepareStatement(sql, null);
        try
        {
	        ResultSet rs = stmt.executeQuery();
	        while(rs.next())
	        {
				xmlStr = xmlStr.append("<department code=\"" + rs.getString("dept_code") + "\">" ) ;
				addElement( xmlStr, "dept_id" , rs.getString( "dept_id")) ;
				addElement( xmlStr, "dept_code" , rs.getString( "dept_code") ) ;
				
				addElement( xmlStr, "dept_desc1" , rs.getString( "dept_desc1") ) ;
				addElement( xmlStr, "dept_desc2" , rs.getString( "dept_desc2") ) ;

				xmlStr = xmlStr.append( "</department>");

	        }

	        rs.close();
	        stmt.close();
        }
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getDeptInfo", e);
		}
        return xmlStr;
	}

	
	public static void main(String[] args) {
		System.out.println("CassiniwebTransfertDoc");
		System.out.println("----------------------------------");
		//
		int count = 0;
		try {
			Compiere.startup(true);


			new CassiniwebTransfertDoc(true);
			count++;
		} catch (Exception e) {
			log.log(Level.SEVERE, "*ERROR EndYearImportAvantaxPdf Fail", e);
		}

		System.out.println("CassiniwebTransfertDoc = " + count);

	} // main

}
