/******************************************************************************
 * Product: Solstice+ Payroll & Human Resources Management                    *
 * Copyright (C) 2008 ProGestion Informatique, Inc. All Rights Reserved.      *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ProGestion Informatique, 210-5300 Bld des Galerie, Quebec, G2K 2A2 Canada  *
 * or via info@progestion.net or http://www.progestion.net/license.html       *
 ******************************************************************************/
package solstice.process;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.compiere.Compiere;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

// Replaced internal XMLSerializer with standard javax.xml.transform.Transformer

import solstice.model.P_Employee;
import solstice.model.P_Employee_Roe;
import solstice.utils.PgiUtil;

/**
 * @author Marc Morissette
 *
 *         Cette classe génère le fichier contenant la liste des relevés
 *         d'emploi d'une période selon le format imposé par le gouvernement
 *         (XML)
 */
public class ExportROE extends SvrProcess {

	/** Logger */
	private static CLogger log = CLogger.getCLogger(ExportROE.class);

	private int exported = 0;
	private int m_periodId = 0;
	private int m_employeeId = 0;
	private boolean m_recovery = false;
	private Properties m_ctx;
	private String FileName = "";
	private String trxName = null;
	private BigDecimal ZERO = new java.math.BigDecimal(0.0);
	private int Count = 0;

	private Boolean found = false; 
	/*
	public ExportROE(boolean Standalone) {
		try {
			m_recovery = true;
			// m_periodId = 1002147;
			String msg = this.doIt();

			System.out.println("Export ROE = " + msg);

		} catch (Exception e) {
			log.log(Level.SEVERE, "Error :", e);
		}
	}
*/
	// On récupère les paramètres de recherche
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (para[i].getParameterName().equals("P_Period_ID"))
				m_periodId = para[i].getParameterAsInt();
			else if (para[i].getParameterName().equals("P_Employee_ID"))
				m_employeeId = para[i].getParameterAsInt();
			else if (para[i].getParameterName().equals("Recovery"))
				m_recovery = "Y".equals(para[i].getParameter()); // para[i].getParameter().equals("Y");
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
	}

	private String message = "";
	
	protected String doIt() throws Exception {
		String Path = "";
		Calendar DateJour = Calendar.getInstance();
		String Month = String.valueOf(DateJour.get(Calendar.MONTH) + 1);
		if (this.m_employeeId != 0) {
			P_Employee Employee = P_Employee.get(Env.getCtx(),this.m_employeeId, null);

			FileName = "ROE_"
					+ Employee.getValue().trim()
					+ "_"
					+ Employee.getSurname()
					+ "_"
					+ String.valueOf(DateJour.get(Calendar.YEAR)
				    + "."
					+ "00".substring( 0, 2 - String.valueOf( DateJour.get(Calendar.MONTH) + 1).trim().length())
					+ String.valueOf(DateJour.get(Calendar.MONTH) + 1)
					+ "." 
					+ "00".substring( 0, 2 - String.valueOf( DateJour.get(Calendar.DAY_OF_MONTH) ).trim().length())
					+ String.valueOf(DateJour.get(Calendar.DAY_OF_MONTH)));

		} else {
			FileName = "ROE_"
					+ String.valueOf(DateJour.get(Calendar.YEAR)
					+ "."
					+ "00".substring( 0, 2 - String.valueOf(DateJour.get(Calendar.MONTH) + 1).trim().length())
					+ String.valueOf(DateJour.get(Calendar.MONTH) + 1)
					+ "."
					+ "00".substring( 0, 2 - String.valueOf(	DateJour.get(Calendar.DAY_OF_MONTH) ).trim().length())
					+ String.valueOf(DateJour.get(Calendar.DAY_OF_MONTH)));

		}

		Path = PgiUtil.getSolsticeParameter(Env.getCtx(), "TransfertPathROE");
//		Path = "D:\\PGI\\Transfert\\";

		/*
		 * JFileChooser fileChooser = new JFileChooser();
		 * fileChooser.setDialogTitle ("Transfert de RE extrait de la Paie");
		 * fileChooser.setFileFilter(new FileFilter() { int returnVal =
		 * fileChooser.showOpenDialog( ); if (returnVal ==
		 * JFileChooser.APPROVE_OPTION) { path =
		 * fileChooser.getSelectedFile().getAbsolutePath (); }
		 */

		String fileName = Path + FileName + ".blk";

		try {
			int exported = exportXml(getCtx(), m_periodId, m_employeeId,
					this.get_TrxName(), fileName);
		} catch (Exception e) {
			log.log(Level.WARNING, "DoIt()", e);
		}
		// return String.valueOf(Count) +
		// " Relevé d'emploi(s) généré(s) avec succes";

		 String path = PgiUtil.getSolsticeParameter(getCtx(), "XmlValidationPath"); 

//		String msg = PgiUtil.validateWithXsd(fileName,	"U:\\XML_Shema_Validation\\Roe.xsd");
		String msg = PgiUtil.validateWithXsd(fileName,	path + "Roe.xsd");

		
		if ( found == false ) {
			msg = "ROE non trouvé, vérifier le période de paie";
		}

		
		return "@Processed@ " + exported + " " + msg;

	}

	public int exportXml(Properties ctx, int P_Period_ID, int P_Employee_ID,
			String trxName, String fileName) {
		StringBuffer xml = new StringBuffer();
		try {

			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);

			Document document = get_xmlDocument( true );

			DOMSource source = new DOMSource( document );
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			transformer.setOutputProperty(javax.xml.transform.OutputKeys.ENCODING, "ISO-8859-1");
			transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			transformer.transform(source, result);
			xml = new StringBuffer( writer.toString() );
/*			
			StringBuffer newXML = writer.getBuffer();

			//
			if (xml.length() != 0) { // // <?xml version="1.0"
										// encoding="UTF-8"?>
				int tagIndex = newXML.indexOf("?>");
				if (tagIndex != -1)
					xml.append(newXML.substring(tagIndex + 2));
				else
					xml.append(newXML);
			} else
				xml.append(newXML);
*/
			
			xml = new StringBuffer( PgiUtil.convertAsciiString( xml.toString().replaceAll( "&amp;", "&" )) );
//			xml = new StringBuffer( xml.toString().replaceAll( "&amp;", "&" ) );

			WriteToFile(xml, fileName);

		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}

		return 1;
	}

	/**
	 * Get XML Document representation
	 * 
	 * @param noComment
	 *            do not add comment
	 * @return XML document
	 */
	public Document get_xmlDocument(boolean noComment) {
		Document document = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.newDocument();
			if (!noComment)
				document.appendChild(document.createComment(Compiere
						.getSummaryAscii()));
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
		// Root
		Element root = document.createElement("ROEHEADER");
		// root.setAttribute("Application", "RoeWeb");
		root.setAttribute("FileVersion", "W-2.0");
		root.setAttribute("SoftwareVendor", "Solstice Plus");
		root.setAttribute("ProductName", "Solstice Plus");
		root.setAttribute("ProductVersion", "7.1");
		document.appendChild(root);

		// ICI

		String sql // On recherche les enregistrements dans l'ordre de l'annexe
					// B
		= " select "
				+ "   Employee.Value,  Employer.CanadaRevenueBusinessNumber,  Roe.PayPeriodType, "
				+ "   substring(SIN, 1, 3) + substring(SIN, 5, 3) + substring(SIN, 9, 3) as SIN,"
				+ "   Employee.FirstName,  Employee.FirstLetter,  Employee.SurName,  Location.Address1,  Location.City,"
				+ "   isnull(Region.Name, '' ) + ' ' + Country.Name as countryInfo,  "
				+ "   Roe.FirstDayWorked,  Roe.LastDayForWhichPaid,  Roe.FinalPayPeriodEndingDate,  Roe.EmployeeOccupation,"
				+ "   isnull( Roe.ExpectedRecallCode, 'U' ) as ExpectedRecallCode,  Roe.ExpectedDateOfRecall,  Roe.TotalInsurableHours,  Roe.TotalInsurableEarnings,"
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
				+ "   Roe.VacationPayAmount, Roe.VacationPayComments,  "
				+ "   Roe.OtherMoniesCode01,"
				+ "   Roe.OtherMoniesAmount01,  Roe.OtherMoniesCode02,  Roe.OtherMoniesAmount02,  Roe.OtherMoniesCode03,"
				+ "   Roe.OtherMoniesAmount03,  isnull(Roe.CommentsLine01, '') CommentsLine01,  isnull(Roe.CommentsLine02,'') CommentsLine02,  isnull(Roe.CommentsLine03,'') CommentsLine03,"
				+ "   isnull(Roe.CommentsLine04,'') CommentsLine04,  Roe.PaidSickDate,  Roe.PaidSickAmount,  Roe.PaidSickPeriod,"
				+ "   Employer.CommunicationPreferredIn,  Employer.PrintLanguageRoe,  'D', P_Employee_Roe_ID, Frequency.NumberOfPeriod, ReasonForIssuingThisRoe,"
				+ "   C_Activity.Value as Activity, isnull( Roe.PrintLanguageID, 155) PrintLanguageID, SerialNumber, Location.Postal, Roe.VacationPayStartDate, Roe.VacationPayEndDate, Roe.VacationPayCode, Roe.OtherMoniesStartDate01, Roe.OtherMoniesEndDate01, Roe.OtherMoniesStartDate02, Roe.OtherMoniesEndDate02, Roe.OtherMoniesStartDate03, Roe.OtherMoniesEndDate03 "
				+ "  , SpecialPaymentType01, SpecialPaymentType02, SpecialPaymentType03, SpecialPaymentType04, SpecialPaymentStartDate01, SpecialPaymentStartDate02, SpecialPaymentStartDate03, SpecialPaymentStartDate04, SpecialPaymentEndDate01, SpecialPaymentEndDate02, SpecialPaymentEndDate03, SpecialPaymentEndDate04, SpecialPaymentAmount01, SpecialPaymentAmount02, SpecialPaymentAmount03, SpecialPaymentAmount04, SpecialPaymentPeriodType01, SpecialPaymentPeriodType02, SpecialPaymentPeriodType03, SpecialPaymentPeriodType04 "
				+ "  , SpecialPaymentPeriod01, SpecialPaymentPeriod02, SpecialPaymentPeriod03, SpecialPaymentPeriod04,"
				+ "	StatutoryHolidayStartDate01 , "
				+ "	StatutoryHolidayAmount01    , "
				+ "	StatutoryHolidayStartDate02 , "
				+ "	StatutoryHolidayAmount02    , "
				+ "	StatutoryHolidayStartDate03 , "
				+ "	StatutoryHolidayAmount03    , "
				+ "	StatutoryHolidayStartDate04 , "
				+ "	StatutoryHolidayAmount04    , "
				+ "	StatutoryHolidayStartDate05 , "
				+ "	StatutoryHolidayAmount05    , "
				+ "	StatutoryHolidayStartDate06 , "
				+ "	StatutoryHolidayAmount06    , "
				+ "	StatutoryHolidayStartDate07 , "
				+ "	StatutoryHolidayAmount07    , "
				+ "	StatutoryHolidayStartDate08 , "
				+ "	StatutoryHolidayAmount08    , "
				+ "	StatutoryHolidayStartDate09 , "
				+ "	StatutoryHolidayAmount09    , "
				+ "	StatutoryHolidayStartDate10 , "
				+ "	StatutoryHolidayAmount10      "

				+ " from P_Employee_Roe Roe, P_Employee Employee "
				+ " left join C_Location Location on Location.C_Location_ID = Employee.C_Location_ID"
				+ " left join C_Country Country on Country.C_Country_ID = Location.C_Country_ID "
				+ " left join C_Region Region on Region.C_Region_ID = Location.C_Region_ID "
				+ " left join P_Payment_Group PG on PG.P_Payment_Group_ID = Employee.P_Payment_Group_ID"
				+ " left join P_Frequency Frequency  on Frequency.P_Frequency_ID = PG.P_Frequency_ID"
				+ " left join P_Employer Employer on Employer.P_Employer_ID = Employee.P_Employer_ID"
				+ " left join C_Activity on C_Activity.C_Activity_ID = Employee.C_Activity_ID"
				+ " where  Roe.P_Employee_ID = Employee.P_Employee_ID ";
		if (m_employeeId != 0)
			sql = sql + " and Employee.P_Employee_ID = " + m_employeeId;

		if (!m_recovery)
			sql += " and Roe.Transfered = 'N'";

		if (m_periodId != 0)
			sql = sql + " and Roe.P_Period_ID = " + m_periodId;

//		sql = sql + " and Roe.P_Period_ID >= 1002130";

		PreparedStatement stmt = null;
		try {
			stmt = DB.prepareStatement(sql, null);
			ResultSet rs = stmt.executeQuery();
			found = false;
			while (rs.next()) {

				found = true;
				exported = exported + 1;

				Element roe = document.createElement("ROE");
				roe.setAttribute("PrintingLanguage",
						getEmployeeRoeLanguage(rs.getInt("PrintLanguageID")));
				roe.setAttribute("Issue", "S"); // S – Submit / D – Draft

				root.appendChild(roe);

				createElement(document, roe, "B2", rs.getString("SerialNumber"));

				if (PgiUtil
						.getSolsticeParameter(Env.getCtx(), "ROE_EmployeeNo")
						.equals("2")) {
					createElement(document, roe, "B3", rs.getString("Value")
							.trim()
							+ '('
							+ rs.getString("Activity").trim()
							+ ')');
				} else {
					createElement(document, roe, "B3", rs.getString("Value"));
				}

				createElement(document, roe, "B5",
						rs.getString("CanadaRevenueBusinessNumber"));
				createElement(document, roe, "B6",
						rs.getString("PayPeriodType"));

				createElement(document, roe, "B8", rs.getString("Sin"));

				Element b9 = document.createElement("B9");

				createElement(document, b9, "FN", rs.getString("FirstName"));
				createElement(document, b9, "LN", rs.getString("SurName"));
				if ( rs.getString("Address1").length() > 35 )
					createElement(document, b9, "A1", rs.getString("Address1").substring(0,34) );
				else
					createElement(document, b9, "A1", rs.getString("Address1") );
					
				createElement(document, b9, "A2", rs.getString("City"));
				createElement(document, b9, "A3", rs.getString("countryInfo")); // Province
																				// pays

				createElement(document, b9, "PC", rs.getString("Postal")); // Code
																			// postal

				roe.appendChild(b9);

				createElement(document, roe, "B10",
						convertToString(rs.getObject("FirstDayWorked"), 8));
				createElement(document, roe, "B11",
						convertToString(rs.getObject("LastDayForWhichPaid"), 8));
				createElement(
						document,
						roe,
						"B12",
						convertToString(
								rs.getObject("FinalPayPeriodEndingDate"), 8));
				createElement(document, roe, "B13",
						convertToString(rs.getObject("EmployeeOccupation"), 40)
								.trim());

				Element b14 = document.createElement("B14");
				roe.appendChild(b14);
				createElement(document, b14, "CD",
						rs.getString("ExpectedRecallCode"));
				createElement(document, b14, "DT",
						convertToString(rs.getString("ExpectedDateOfRecall"), 10));

				roe.appendChild(b14);

				BigDecimal HourDecimal = ZERO;
				HourDecimal = rs.getBigDecimal("TotalInsurableHours").setScale(
						0, BigDecimal.ROUND_HALF_UP);

				createElement(document, roe, "B15A",
						convertToString(HourDecimal, 4));
				// createElement( document, roe ,"B15B",
				// convertToString(rs.getObject("TotalInsurableEarnings"), 9) );

				Element b15c = document.createElement("B15C");

				String Value = "";
				BigDecimal Amount = ZERO;
				int NbrPeriod = rs.getInt("NumberOfPeriod");
				int XPeriod = 0;
				int Colonne = 19; // Représente le numéro de l'object de la
									// période 1
				for (int i = 0; i < (NbrPeriod + 1); i++) {
					XPeriod = i + 1;

					Value = convertToString(rs.getObject(Colonne), 9);
					if (Value.trim().length() > 0) // (Value != "null")
					{

						Element pp = document.createElement("PP");
						pp.setAttribute("nbr", String.valueOf(XPeriod));
						b15c.appendChild(pp);

						String amt = convertToString(rs.getObject(Colonne), 9);
						if (amt.startsWith("-")) {
							log.log(Level.WARNING,
									"Montant negatif pour le ROE employé : "
											+ rs.getString("Value"));
							amt = "0.00";
						}
						createElement(document, pp, "AMT", amt);

						Colonne++;
					}
				}
				roe.appendChild(b15c);

				Element b16 = document.createElement("B16");
				createElement(document, b16, "CD",
						rs.getString("ReasonForIssuingThisRoe"));
				createElement(document, b16, "FN",
						rs.getString("FirstNameContactPerson"));
				createElement(document, b16, "LN",
						rs.getString("LastNameContactPerson"));
				createElement(document, b16, "AC",
						rs.getString("PhoneAreaCode"));
				String PhoneNumber = convertToString(
						rs.getObject("PhoneNumberContactPerson"), 13); // Employer.PhoneNumberContactPerson

				PhoneNumber = PhoneNumber.replace("-", "");

				createElement(document, b16, "TEL", PhoneNumber);

/*				if (rs.getString("PhoneExt").trim().length() > 0) {
					createElement(document, b16, "EXT",
							rs.getString("PhoneExt").trim() );
				}
*/
				roe.appendChild(b16);

				Value = convertToString(rs.getObject("VacationPayAmount"), 9); // Roe.VacationPayAmount
				{
					Element B17A = document.createElement("B17A");
					// createElement( document, roe ,"B17A", Value );

					//2023-10-25
					if ( Value.trim().length() > 0 && rs.getBigDecimal("VacationPayAmount").compareTo(Env.ZERO) != 0)
					{
						Element vp = document.createElement("VP");
						vp.setAttribute("nbr", "1");

						if ( rs.getString("VacationPayComments") == null )
							createElement(document, vp, "CD", "1");
						else
							createElement(document, vp, "CD", rs.getString("VacationPayComments"));

//						createElement(document, vp, "CD", "1"); // 1 Included with
																// each pay, 2 Paid
																// because no longer
																// working, 3 Paid
																// for a vacation
																// leave period, 4
																// Anniversary (Paid
																// on a specific
																// date each year)

						if (convertToString(rs.getObject( "VacationPayStartDate" ), 9).trim().length() > 0) {

							createElement(
									document,
									vp,
									"SDT",
									convertToString(
											rs.getObject("VacationPayStartDate"), 8)); // Vacation
																						// Pay
																						// Start
																						// Date
							createElement(
									document,
									vp,
									"EDT",
									convertToString(rs.getObject("VacationPayEndDate"),
											8)); // Vacation Pay End Date
						}
						if ( Value.equals("0.00"))
							createElement(document, vp, "AMT", "");
						else
							createElement(document, vp, "AMT", Value);

						B17A.appendChild(vp);

						roe.appendChild(B17A);
						
					}

				}
				int jour = 0;
				Colonne = 0;
				String colonneNo = "";
				
				if (convertToString(rs.getObject(79), 9).trim().length() > 0
						|| convertToString(rs.getObject(81), 9).trim().length() > 0
						|| convertToString(rs.getObject(83), 9).trim().length() > 0) {
					// createElement( document, roe ,"B17B", Value.trim() );
					Element B17B = document.createElement("B17B");
					for (int i = 1; i < 9; i++) {
						if ( i < 10)
    		    			colonneNo =  "0".concat( String.valueOf( i ) );
    		    		else
    		    			colonneNo =  String.valueOf( i );
						
						jour = jour + 1;
						if (convertToString(rs.getObject( "StatutoryHolidayStartDate".concat( colonneNo ) ), 9).trim()
								.length() > 0) {
							Element sh = document.createElement("SH");
							sh.setAttribute("nbr", String.valueOf(jour));
							B17B.appendChild(sh);

							createElement(document, sh, "DT",
									convertToString(rs.getObject("StatutoryHolidayStartDate".concat( colonneNo )) , 8));
							createElement(
									document,
									sh,
									"AMT",
									convertToString(rs.getObject("StatutoryHolidayAmount".concat( colonneNo )),
											9));
						}
					}
					roe.appendChild(B17B);

				}
				if (convertToString(rs.getObject(85), 9).trim().length() > 0
						|| convertToString(rs.getObject(87), 9).trim().length() > 0
						|| convertToString(rs.getObject(89), 9).trim().length() > 0) {
					Element B17C = document.createElement("B17C"); // Jour Férié
					jour = 0;
					Colonne = 0;
					String ColonneName = "";
					String ColonneNameAmt = "";
					String ColonneNameStartDate = "";
					String ColonneNameEndDate = "";
					// 2009.07.31
					// for (int i = 0; i < 2; i++)
					for (int i = 1; i <= 3; i++) {
						ColonneName = "OtherMoniesCode0" + i;
						ColonneNameAmt = "OtherMoniesAmount0" + i;
						ColonneNameStartDate = "OtherMoniesStartDate0" + i;
						ColonneNameEndDate = "OtherMoniesEndDate0" + i;
						jour = jour + 1;

						if (convertToString(rs.getObject(ColonneName), 1)
								.trim().length() > 0) {
							Element om = document.createElement("OM");
							om.setAttribute("nbr", String.valueOf(jour));
							// createElement( document, om ,"CD",
							// convertToString(rs.getObject(ColonneName), 3 )) ;
							createElement(document, om, "CD", rs.getString( ColonneName ));

							if (  convertToString( rs.getObject(ColonneNameAmt),1).trim().length() > 0) {
								createElement(
										document,
										om,
										"SDT",
										convertToString(
												rs.getObject(ColonneNameStartDate),
												8));
								createElement(
										document,
										om,
										"EDT",
										convertToString(
												rs.getObject(ColonneNameEndDate), 8));
								createElement(
										document,
										om,
										"AMT",
										convertToString(
												rs.getObject(ColonneNameAmt), 9));
								
							}
							B17C.appendChild(om);

						}
					}
					roe.appendChild(B17C);
				}

/*				String Comments = convertToString(rs.getObject("CommentsLine01"), 40).trim()	+ " " + // Roe.CommentsLine01
						convertToString(rs.getObject("CommentsLine02"), 40).trim() + " " + // Roe.CommentsLine02
						convertToString(rs.getObject("CommentsLine03"), 40).trim() + " " + // Roe.CommentsLine03
						convertToString(rs.getObject("CommentsLine04"), 40).trim(); // Roe.CommentsLine04
*/
				String Comments = rs.getString("CommentsLine01") + " " + // Roe.CommentsLine01
						rs.getString("CommentsLine02") + " " + // Roe.CommentsLine02
						rs.getString("CommentsLine03") + " " + // Roe.CommentsLine03
						rs.getString("CommentsLine04"); // Roe.CommentsLine04

				Comments = Comments.replaceAll("\\t", "");
				Comments = Comments.replaceAll("\\n", "");
				Comments = Comments.replaceAll(" ", " "); // Elever un charactère spécial.
				

				
				// 2013.01.21 add PgiUtil.convertAsciiString
				//Comments = PgiUtil.convertAsciiString(Comments);

				createElement(document, roe, "B18", Comments);
				
				// 19 – Special Payments Information

				Element b19 = document.createElement("B19");
				roe.appendChild(b19);


				String ColonneNameType = "";
				String ColonneNamePeriod = "";
				String ColonneNameAmt = "";
				String ColonneNameStartDate = "";
				String ColonneNameEndDate = "";
				int cdnbr = 0;
				for (int i = 1; i <= 4; i++) {
					ColonneNameType = "SpecialPaymentType0" + i;
					ColonneNameAmt = "SpecialPaymentAmount0" + i;
					ColonneNameStartDate = "SpecialPaymentStartDate0" + i;
					ColonneNameEndDate = "SpecialPaymentEndDate0" + i;
					ColonneNamePeriod = "SpecialPaymentPeriod0" + i;

					cdnbr = cdnbr + 1;
					Value = convertToString(rs.getObject(ColonneNameAmt), 9); //
					if (Value.trim().length() > 0) // (Value != null)
					{
						Element sp = document.createElement("SP");
						//sp.setAttribute("cd", convertToString(rs.getObject(ColonneNameType), 5));
						if	( ColonneNameType.equals("SpecialPaymentType01"))	
							sp.setAttribute("cd", "PLS01" );
						if	( ColonneNameType.equals("SpecialPaymentType02"))	
							sp.setAttribute("cd", "WLI01" );
						if	( ColonneNameType.equals("SpecialPaymentType03"))	
							sp.setAttribute("cd", "WLI02" );
						if	( ColonneNameType.equals("SpecialPaymentType04"))	
							sp.setAttribute("cd", "MAT01" );
						
						if (  convertToString( rs.getObject(ColonneNameStartDate),8).trim().length() > 0) {
							createElement(
									document,
									sp,
									"SDT",
									convertToString(
											rs.getObject(ColonneNameStartDate), 8));
							createElement(
									document,
									sp,
									"EDT",
									convertToString(
											rs.getObject(ColonneNameEndDate), 8));
							createElement(
									document,
									sp,
									"AMT",
									convertToString(rs.getObject(ColonneNameAmt), 8));
							createElement(
									document,
									sp,
									"PRD",
									convertToString(
											rs.getObject(ColonneNamePeriod), 1));
							b19.appendChild(sp);

						}


					}
					/*
					 * createElement( document, sp ,"SDT",
					 * convertToString(rs.getObject("PaidSickDate"), 8) );
					 * createElement( document, sp ,"EDT", "" ); createElement(
					 * document, sp ,"AMT",
					 * convertToString(rs.getObject("PaidSickAmount"), 8) );
					 * createElement( document, sp ,"PRD",
					 * convertToString(rs.getObject("PaidSickPeriod"), 1) );
					 */
				}

				createElement(document, roe, "B20",
						rs.getString("CommunicationPreferredIn"));

				P_Employee_Roe Roe = P_Employee_Roe.get(Env.getCtx(),
						rs.getInt("P_Employee_Roe_ID"), trxName);
				Roe.setTransfered(true);
				Roe.save();
				Count++;
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			log.log(Level.WARNING, "DoIt()", e);
		}

		return document;

	}

	public void InsertBlank(StringBuffer XX, int Pos) {
		// log.debug("CreateFileTransfertRBC.InsertBlank - Début");
		for (int i = 0; i < Pos; i++) {
			XX.append(" ");
		}
		// log.debug("CreateFileTransfertRBC.InsertBlank - Fin");
	}

	private static void WriteToFile(StringBuffer NewFile, String FileName) {

		File aFile = null;
		aFile = new File(FileName);
		FileOutputStream out; // declare a file output object
		PrintStream p; // declare a print stream object
		try {
			// Create a new file output stream
			out = new FileOutputStream(aFile);

			// Connect print stream to the output stream
			p = new PrintStream(out);

			p.println(NewFile.toString());
			// System.out.println(NewFile.toString());
			p.close();
		} catch (Exception e) {
			log.log(Level.SEVERE, "Error writing to file :", e);
		}
	}

	// Convertit un objet de la bd en string selon une largeur
	private String convertToString(Object object, int width) {
		if (object == null) {
			return pad("", width);
		}
		if (object.getClass().equals(Timestamp.class)) // On veut un format
														// jj/mm/aaaa pour
														// toutes les dates
		{
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
			return f.format((Timestamp) object);
		}
		if (object.getClass().equals(Double.class)) {
			DecimalFormat f = new DecimalFormat("#0.00");
			String s = f.format(((Double) object).doubleValue());
			s = s.replace('.', ',');
			return pad(s, width);
		}
		
		String ret = PgiUtil.convertAsciiString( object.toString().trim() );
		return pad(ret , width);
		//return pad(object.toString().trim(), width);
	}

	// Ajoute des espaces au bout de la string
	private String pad(String input, int width) {
		if (input.length() > width) {
			return input.substring(0, width);
		}
		String buffer = input;
		while (buffer.length() < width) {
			buffer += " ";
		}
		return buffer;
	}

	String getEmployeeRoeLanguage(int printLangID) {
		String ret = null;
		String value = null;
		String sql = "select value from P_Language " + "where P_Language_ID ="
				+ printLangID;

		PreparedStatement stmt = null;
		try {
			stmt = DB.prepareStatement(sql, null);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				value = rs.getString(1);
			}
			if (value.substring(0, 2).equals("en"))
				ret = "E";
			if (value.substring(0, 2).equals("fr"))
				ret = "F";
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			log.log(Level.WARNING, "getEmployeeRoeLanguage", e);
		}
		return ret;
	}
/*
	public StringBuffer get_xmlString(StringBuffer xml) {
		if (xml == null)
			xml = new StringBuffer();
		else
			xml.append(Env.NL);
		//
		try {
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			DOMSource source = new DOMSource(get_xmlDocument(xml.length() != 0));
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			transformer.transform(source, result);
			StringBuffer newXML = writer.getBuffer();
			//
			if (xml.length() != 0) { // // <?xml version="1.0"
										// encoding="UTF-8"?>
				int tagIndex = newXML.indexOf("?>");
				if (tagIndex != -1)
					xml.append(newXML.substring(tagIndex + 2));
				else
					xml.append(newXML);
			} else
				xml.append(newXML);
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
		return xml;
	} // get_xmlString
*/
	public static void createElement(org.w3c.dom.Document doc, Element parent,
			String nodeName, String nodeValue) {
		if (nodeValue != null) {
			Element elem = doc.createElement(nodeName);
			Text text = doc.createTextNode(nodeValue.trim());
			elem.appendChild(text);
			parent.appendChild(elem);
		}
		/*
		 * else { Element elem = doc.createElement(nodeName);
		 * elem.setAttribute("xsi:nil", "true"); parent.appendChild(elem);
		 * 
		 * }
		 */
	}

	/**************************************************************************
	 * Migration des données pour la SIQ
	 */
	/*
	public static void main(String[] args) {
		System.out.println("Export ROE");
		System.out.println("----------------------------------");
		//
		try {
			Compiere.startup(true);

			new ExportROE(true);
		} catch (Exception e) {
			log.log(Level.SEVERE, "*ERROR export ROE Fail", e);
		}

	} // main
*/
}
