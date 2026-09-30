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
package solstice.model;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.Level;


/*
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
*/
/*
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
*/
import org.compiere.Compiere;
import org.compiere.model.PO;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

// Replaced internal XMLSerializer with standard javax.xml.transform.Transformer

import solstice.utils.PgiUtil;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_Employee_Roe extends X_P_Employee_Roe
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String REASONFORISSUINGTHISROE_Other = "O";
	
	/**
	 * 	Get P_Employee_Roe
	 *	@param ctx context
	 * 	@param P_Employee_Roe_ID id
	 *	@return Employee_Roe
	 */
	public static P_Employee_Roe get (Properties ctx, int P_Employee_Roe_ID, String trxName)
	{
		Integer key = new Integer (P_Employee_Roe_ID);
		P_Employee_Roe Employee_Roe = (P_Employee_Roe)s_cache.get(key);
		if (Employee_Roe != null)
			return Employee_Roe;
		Employee_Roe = new P_Employee_Roe (ctx, P_Employee_Roe_ID, trxName);
		s_cache.put (key, Employee_Roe);
		return Employee_Roe;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Employee_Roe>	s_cache = new CCache<Integer,P_Employee_Roe>("P_Employee_Roe", 20);
	
	private static CLogger log = CLogger.getCLogger(P_Employee_Roe.class);

	private String trxName = null;
	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Employee_Roe_ID id
	 */
	public P_Employee_Roe (Properties ctx, int P_Employee_Roe_ID, String trxName)
	{
		super (ctx, P_Employee_Roe_ID, trxName);
		if (P_Employee_Roe_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(11);
			setAD_Client_ID(11);
		}
	}	//	P_Employee_Roe

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Employee_Roe (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Employee_Roe (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Employee_Roe_ID"), trxName);
	}	//	P_Employee_Roe

	public void setEarningsForPayPeriod(int index, BigDecimal value)
	{
		switch(index)
		{
			case 1: this.setEarningsForPayPeriod01(value); break;
			case 2: this.setEarningsForPayPeriod02(value); break;
			case 3: this.setEarningsForPayPeriod03(value); break;
			case 4: this.setEarningsForPayPeriod04(value); break;
			case 5: this.setEarningsForPayPeriod05(value); break;
			case 6: this.setEarningsForPayPeriod06(value); break;
			case 7: this.setEarningsForPayPeriod07(value); break;
			case 8: this.setEarningsForPayPeriod08(value); break;
			case 9: this.setEarningsForPayPeriod09(value); break;
			case 10: this.setEarningsForPayPeriod10(value); break;
			case 11: this.setEarningsForPayPeriod11(value); break;
			case 12: this.setEarningsForPayPeriod12(value); break;
			case 13: this.setEarningsForPayPeriod13(value); break;
			case 14: this.setEarningsForPayPeriod14(value); break;
			case 15: this.setEarningsForPayPeriod15(value); break;
			case 16: this.setEarningsForPayPeriod16(value); break;
			case 17: this.setEarningsForPayPeriod17(value); break;
			case 18: this.setEarningsForPayPeriod18(value); break;
			case 19: this.setEarningsForPayPeriod19(value); break;
			case 20: this.setEarningsForPayPeriod20(value); break;
			case 21: this.setEarningsForPayPeriod21(value); break;
			case 22: this.setEarningsForPayPeriod22(value); break;
			case 23: this.setEarningsForPayPeriod23(value); break;
			case 24: this.setEarningsForPayPeriod24(value); break;
			case 25: this.setEarningsForPayPeriod25(value); break;
			case 26: this.setEarningsForPayPeriod26(value); break;
			case 27: this.setEarningsForPayPeriod27(value); break;
			case 28: this.setEarningsForPayPeriod28(value); break;
			case 29: this.setEarningsForPayPeriod29(value); break;
			case 30: this.setEarningsForPayPeriod30(value); break;
			case 31: this.setEarningsForPayPeriod31(value); break;
			case 32: this.setEarningsForPayPeriod32(value); break;
			case 33: this.setEarningsForPayPeriod33(value); break;
			case 34: this.setEarningsForPayPeriod34(value); break;
			case 35: this.setEarningsForPayPeriod35(value); break;
			case 36: this.setEarningsForPayPeriod36(value); break;
			case 37: this.setEarningsForPayPeriod37(value); break;
			case 38: this.setEarningsForPayPeriod38(value); break;
			case 39: this.setEarningsForPayPeriod39(value); break;
			case 40: this.setEarningsForPayPeriod40(value); break;
			case 41: this.setEarningsForPayPeriod41(value); break;
			case 42: this.setEarningsForPayPeriod42(value); break;
			case 43: this.setEarningsForPayPeriod43(value); break;
			case 44: this.setEarningsForPayPeriod44(value); break;
			case 45: this.setEarningsForPayPeriod45(value); break;
			case 46: this.setEarningsForPayPeriod46(value); break;
			case 47: this.setEarningsForPayPeriod47(value); break;
			case 48: this.setEarningsForPayPeriod48(value); break;
			case 49: this.setEarningsForPayPeriod49(value); break;
			case 50: this.setEarningsForPayPeriod50(value); break;
			case 51: this.setEarningsForPayPeriod51(value); break;
			case 52: this.setEarningsForPayPeriod52(value); break;
			case 53: this.setEarningsForPayPeriod53(value); break;
		}
	}
	
	public BigDecimal getEarningsForPayPeriod(int index) throws ArrayIndexOutOfBoundsException
	{
		switch(index)
		{
			case 1: return this.getEarningsForPayPeriod01(); 
			case 2: return this.getEarningsForPayPeriod02(); 
			case 3: return this.getEarningsForPayPeriod03(); 
			case 4: return this.getEarningsForPayPeriod04(); 
			case 5: return this.getEarningsForPayPeriod05(); 
			case 6: return this.getEarningsForPayPeriod06(); 
			case 7: return this.getEarningsForPayPeriod07(); 
			case 8: return this.getEarningsForPayPeriod08(); 
			case 9: return this.getEarningsForPayPeriod09(); 
			case 10: return this.getEarningsForPayPeriod10(); 
			case 11: return this.getEarningsForPayPeriod11(); 
			case 12: return this.getEarningsForPayPeriod12(); 
			case 13: return this.getEarningsForPayPeriod13(); 
			case 14: return this.getEarningsForPayPeriod14(); 
			case 15: return this.getEarningsForPayPeriod15(); 
			case 16: return this.getEarningsForPayPeriod16(); 
			case 17: return this.getEarningsForPayPeriod17(); 
			case 18: return this.getEarningsForPayPeriod18(); 
			case 19: return this.getEarningsForPayPeriod19(); 
			case 20: return this.getEarningsForPayPeriod20(); 
			case 21: return this.getEarningsForPayPeriod21(); 
			case 22: return this.getEarningsForPayPeriod22(); 
			case 23: return this.getEarningsForPayPeriod23(); 
			case 24: return this.getEarningsForPayPeriod24(); 
			case 25: return this.getEarningsForPayPeriod25(); 
			case 26: return this.getEarningsForPayPeriod26(); 
			case 27: return this.getEarningsForPayPeriod27();
			case 28: return this.getEarningsForPayPeriod28();
			case 29: return this.getEarningsForPayPeriod29();
			case 30: return this.getEarningsForPayPeriod30();
			case 31: return this.getEarningsForPayPeriod31();
			case 32: return this.getEarningsForPayPeriod32();
			case 33: return this.getEarningsForPayPeriod33();
			case 34: return this.getEarningsForPayPeriod34();
			case 35: return this.getEarningsForPayPeriod35();
			case 36: return this.getEarningsForPayPeriod36();
			case 37: return this.getEarningsForPayPeriod37();
			case 38: return this.getEarningsForPayPeriod38();
			case 39: return this.getEarningsForPayPeriod39();
			case 40: return this.getEarningsForPayPeriod40();
			case 41: return this.getEarningsForPayPeriod41();
			case 42: return this.getEarningsForPayPeriod42();
			case 43: return this.getEarningsForPayPeriod43();
			case 44: return this.getEarningsForPayPeriod44();
			case 45: return this.getEarningsForPayPeriod45();
			case 46: return this.getEarningsForPayPeriod46();
			case 47: return this.getEarningsForPayPeriod47();
			case 48: return this.getEarningsForPayPeriod48();
			case 49: return this.getEarningsForPayPeriod49();
			case 50: return this.getEarningsForPayPeriod50();
			case 51: return this.getEarningsForPayPeriod51();
			case 52: return this.getEarningsForPayPeriod52();
			case 53: return this.getEarningsForPayPeriod53();
		}
		throw (new ArrayIndexOutOfBoundsException());
	}

	//
	// Fill Roe Record with Start and end Date
	//
	public boolean fillRoe()
	{
		P_Employee Employee = P_Employee.get( this.getCtx(), this.getP_Employee_ID(), this.get_TrxName());
		P_Payment_Group PaymentGroup = P_Payment_Group.get( this.getCtx(), Employee.getP_Payment_Group_ID(), this.get_TrxName());
//		P_Frequency Frequency = P_Frequency.get( this.getCtx(), PaymentGroup.getP_Frequency_ID(), this.get_TrxName());
		P_Frequency Frequency = P_Frequency.get( this.getCtx(), this.getP_Frequency_ID(), this.get_TrxName());

//        System.out.println("* DEBUG * fillROE employeeID " + this.getP_Employee_ID() );
//        System.out.println("* DEBUG * fillROE Payment_Group_ID " + Employee.getP_Payment_Group_ID() );
//        System.out.println("* DEBUG * fillROE Frequency_ID " + PaymentGroup.getP_Frequency_ID() );

		this.setP_Employer_ID(Employee.getP_Employer_ID());
		this.setP_Job_Title_ID(Employee.getP_Job_Title_ID());
//		this.setP_Frequency_ID(PaymentGroup.getP_Frequency_ID());


		int topSalary;
		int topSalary15B = 27;
		if ( Frequency.getNumberOfPeriod() == 53)
			topSalary15B = 27;
		if ( Frequency.getNumberOfPeriod() == 27 )
			topSalary15B = 14;
		if ( Frequency.getNumberOfPeriod() == 12 )
			topSalary15B = 7;

		if ( Frequency.getNumberOfPeriod() == 27 )
		{
			topSalary = Frequency.getNumberOfPeriod();
		}
		else
		{
			topSalary = Frequency.getNumberOfPeriod() + 1;
			
		}

		this.setTotalInsurableHours(new BigDecimal (0));
		this.setTotalInsurableEarnings(new BigDecimal(0));
		for(int i = 0; i < 53; i++)
		{
			this.setEarningsForPayPeriod(i+1, null);
		}
		
		//TODO modifier pour ORACLE.
		
		String C_PERIOD
			= " select top " + topSalary + " Period.P_Period_ID"
				+ " from P_Period Period " 
				+ " where Period.EndDate <= ? "
				+ " and Period.StartDate >= isnull(? , Period.StartDate)"
//				2009.01.14 Ne pas inclure la période de régularisation
				+ " and Period.IsAdjustmentPeriod = 'N' "
				+ " and Period.P_Frequency_ID = " + this.getP_Frequency_ID()
				+ " order by Period.Name desc";
		try
		{
			Timestamp actualFirstDayOfFirstPeriod = null;
			String selectFirstDay = " SELECT StartDate FROM P_Period "
				+ " WHERE Startdate <= ? "
				+ " AND EndDate >= ? "
			    + " and P_Period.P_Frequency_ID = " + this.getP_Frequency_ID()
			    ;
			
			PreparedStatement pstmtFirstDay = DB.prepareStatement(selectFirstDay, null);
			pstmtFirstDay.setTimestamp(1, this.getFirstDayWorked());
			pstmtFirstDay.setTimestamp(2, this.getFirstDayWorked());


			ResultSet rsFirstDay = pstmtFirstDay.executeQuery();
			if(rsFirstDay.next())
			{
				actualFirstDayOfFirstPeriod = rsFirstDay.getTimestamp("StartDate");
			}
			rsFirstDay.close();
			pstmtFirstDay.close();
			
			//ici si null, periode existe pas, donc trop loin
			//donc on peut prendre la premiere journee travaillée
			//sans crainte
			if(actualFirstDayOfFirstPeriod == null)
				actualFirstDayOfFirstPeriod = (Timestamp)this.getFirstDayWorked().clone();
			
			PreparedStatement stmt = DB.prepareStatement(C_PERIOD, null);
			stmt.setTimestamp(1, this.getFinalPayPeriodEndingDate());
			//stmt.setTimestamp(2, this.getFirstDayWorked());
			stmt.setTimestamp(2, actualFirstDayOfFirstPeriod);
			ResultSet rs = stmt.executeQuery() ;
			
			int i = 1;
			int index = 0;
			boolean missingPeriod = false;

			
			//2013-10-10 Cette partie de code ne sera bientot plus nécessaire car le calcul de la déduction s'occupe maintenant de ne pas inclure les heures que ne sont pas admissible.
			//+
			String sPeriodUpdateCalculAE = PgiUtil.getSolsticeParameter(getCtx(), "PeriodUpdateCalculAE");
			
			int PeriodUpdateCalculAE;
			if ( sPeriodUpdateCalculAE != null )
				PeriodUpdateCalculAE = Integer.parseInt( sPeriodUpdateCalculAE ) ;
			else
				PeriodUpdateCalculAE = 0;
			
			while(rs.next())
			{
				
				int P_Period_ID = rs.getInt(1);
				
				String C_Cumulatif
				= " select isnull(sum(Salary_Eligible), 0), isnull(sum(Hours_Eligible), 0)"
					+ " from P_Payment_Deduction PD inner join P_Deduction Deduction"
					+ " on PD.P_Deduction_ID = Deduction.P_Deduction_ID"
					+ " where P_Deduction_Family_ID = 1000000" /*assurance emploi*/
					+ " and PD.P_Employee_ID = " + this.getP_Employee_ID()
					+ " and PD.P_Period_ID = " + P_Period_ID;
			
				try
				{
					PreparedStatement stmt2 = DB.prepareStatement(C_Cumulatif, null);
					ResultSet rs2 = stmt2.executeQuery();
					
					while(rs2.next())
					{
						if(index < topSalary)
						{
						    if(rs2.getBigDecimal(1).doubleValue() < 1)
						    {
						        missingPeriod = true;
						    }
						    if ( index < topSalary15B ) 
						    {
						    	this.setTotalInsurableEarnings(this.getTotalInsurableEarnings().add(rs2.getBigDecimal(1)));
						    }
						    
							this.setEarningsForPayPeriod(i++, rs2.getBigDecimal(1));
						}
						this.setTotalInsurableHours(this.getTotalInsurableHours().add(rs2.getBigDecimal(2)) );

			
						if ( PeriodUpdateCalculAE != 0 && P_Period_ID < PeriodUpdateCalculAE )
						{
							RemoveHoursNotEligible( P_Period_ID, topSalary, index );
						}
		
						//2013.10.10 additionner les heures de temps supplémentaire mis en banque durant la période couvert par le ROE.
						// On prend pour aquis que les heures mis en banque sont payé au départ de l'employé.
						if ( this.isOvertimeIncluded())
							addOvertimeHours( P_Period_ID, topSalary, index );

					}
					rs2.close();
					stmt2.close();


				}
				catch (Exception e)
				{
					e.printStackTrace(System.out);
				}

				index++;
			}
			rs.close();
			stmt.close();
			
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
			return false;
		}
		//- 2013-10-10

		

		
//		this.setTotalInsurableHours( this.getTotalInsurableHours().setScale(0,BigDecimal.ROUND_HALF_UP));
		return true;
	}

	// 2013.10.10 +

	private void RemoveHoursNotEligible( int P_Period_ID, int topSalary, int index ) throws Exception
	{
				
		String strHreNonAdm = " SELECT isnull(sum(pg.QuantityCalc), 0) "
				+ " From P_Payment_Gain pg "
				+ " INNER JOIN p_Gain g ON pg.P_Gain_ID = g.P_Gain_ID  "
				+ " INNER JOIN P_Gain_GainInfo ggi on ggi.P_Gain_ID = g.P_Gain_ID "
				+ " INNER JOIN P_GainInfo gi ON ggi.P_GainInfo_ID = gi.P_GainInfo_ID  "
				+ " WHERE pg.P_Period_ID = " + P_Period_ID
				+ " AND pg.P_Employee_ID = "+this.getP_Employee_ID()
				+ " AND gi.Value = 'HreAE' "
				+ " AND ggi.TO_CONSIDER = 'Y' "
		// Path temporaire pour krispy du a la migration des données
				+ " AND G.VALUE in ( 'GA03', 'GA04') ";
			PreparedStatement pstmHNA = DB.prepareStatement(strHreNonAdm, null);
			ResultSet rsHNA = pstmHNA.executeQuery();
			BigDecimal bdHNA = Env.ZERO;
			
//			log.log(Level.WARNING, "*DEBUG* RemoveHoursNotEligible " + strHreNonAdm  );
//			log.log(Level.WARNING, "*DEBUG* RemoveHoursNotEligible this.setTotalInsurableHours : " + this.getTotalInsurableHours()  );
			
			if(rsHNA.next())
			{
				if(rsHNA.getBigDecimal(1) != null)
					bdHNA = rsHNA.getBigDecimal(1);
//					bdHNA = bdHNA.add(rsHNA.getBigDecimal(1));
			}

//			log.log(Level.WARNING, "*DEBUG* RemoveHoursNotEligible this.bdHNA : " + bdHNA  );

			this.setTotalInsurableHours(this.getTotalInsurableHours().subtract(bdHNA));
			
			this.setTotalInsurableHours( this.getTotalInsurableHours().setScale(2, BigDecimal.ROUND_HALF_UP)  );
				
	}

	
	private void addOvertimeHours( int P_Period_ID, int topSalary, int index ) throws Exception
	{
		String strOvertimeHours = " SELECT isnull(sum(pg.QuantityCalc), 0) "
				+ " From P_Payment_Gain pg"
				+ " INNER JOIN p_Gain g ON pg.P_Gain_ID = g.P_Gain_ID"
				+ " INNER JOIN P_Gain_GainInfo ggi ON ggi.P_Gain_ID = g.P_Gain_ID"
				+ " INNER JOIN P_GainInfo gi ON ggi.P_GainInfo_ID = gi.P_GainInfo_ID "
				+ " WHERE  pg.P_Period_ID = "+ P_Period_ID
				+ " AND pg.P_Employee_ID = "+this.getP_Employee_ID()
				+ " AND gi.Value = 'OvertimeAE' "
				+ " AND ggi.TO_CONSIDER = 'Y' ";
			PreparedStatement pstm = DB.prepareStatement(strOvertimeHours, null);
			ResultSet rs = pstm.executeQuery();
			BigDecimal bd = Env.ZERO;
			if(rs.next())
			{
				if(rs.getBigDecimal(1) != null)
					bd = bd.add(rs.getBigDecimal(1));
			}
			this.setTotalInsurableHours(this.getTotalInsurableHours().add(bd));
	}
	
	private String message = "";
	
	// 2013.10.10 -
	public void exportRoe()
	{
		String Path = "";

		Path = PgiUtil.getSolsticeParameter(Env.getCtx(), "TransfertPathROE");
		Calendar DateJour = Calendar.getInstance();
		String Month = String.valueOf(DateJour.get(Calendar.MONTH) + 1);

		P_Employee Employee = P_Employee.get(Env.getCtx(),	this.getP_Employee_ID(), null);

		String FileName = "ROE_"
				+ Employee.getValue().trim()
				+ "_"
				+ Employee.getSurname()
				+ "_"
				+ String.valueOf(DateJour.get(Calendar.YEAR)
			    + "."
				+ "00".substring( 0, 2 - String.valueOf(	DateJour.get(Calendar.MONTH) + 1).trim().length())
				+ String.valueOf(DateJour.get(Calendar.MONTH) + 1)
				+ "." 
				+ "00".substring( 0, 2 - String.valueOf(	DateJour.get(Calendar.DAY_OF_MONTH)).trim().length())
				+ String.valueOf(DateJour.get(Calendar.DAY_OF_MONTH))); 
//				+ "-"
//				+ String.valueOf( this.getP_Period_ID() );

		String fileName = Path + FileName + ".blk";
		System.out.println("* DEBUG * exportROE " + fileName );

		exportXml( Env.getCtx(), trxName, fileName );
	}
	
	public int exportXml(Properties ctx, String trxName, String fileName) {
		StringBuffer xml = new StringBuffer();
		try {

			StringWriter writer = new StringWriter();
			javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(writer);

			Document document = get_xmlDocument( true );

			javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource( document );
			javax.xml.transform.TransformerFactory tFactory = javax.xml.transform.TransformerFactory.newInstance();
			javax.xml.transform.Transformer transformer = tFactory.newTransformer();

			transformer.setOutputProperty(javax.xml.transform.OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			transformer.transform(source, result);
			xml = new StringBuffer( writer.toString() );
			xml = new StringBuffer( PgiUtil.convertAsciiString( xml.toString().replaceAll( "&amp;", "&" )) );

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
			javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory
					.newInstance();
			javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
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

				+ " from P_Employee_Roe Roe"
				+ " INNER JOIN  P_Employee Employee ON Roe.P_Employee_ID = Employee.P_Employee_ID  "
				+ " left join C_Location Location on Location.C_Location_ID = Employee.C_Location_ID"
				+ " left join C_Country Country on Country.C_Country_ID = Location.C_Country_ID "
				+ " left join C_Region Region on Region.C_Region_ID = Location.C_Region_ID "
				+ " left join P_Payment_Group PG on PG.P_Payment_Group_ID = Employee.P_Payment_Group_ID"
				+ " left join P_Frequency Frequency  on Frequency.P_Frequency_ID = PG.P_Frequency_ID"
				+ " left join P_Employer Employer on Employer.P_Employer_ID = Employee.P_Employer_ID"
				+ " left join C_Activity on C_Activity.C_Activity_ID = Employee.C_Activity_ID"
				+ " where Roe.P_Employee_Roe_ID = " + this.getP_Employee_Roe_ID()
				;

		PreparedStatement stmt = null;
		try {
			stmt = DB.prepareStatement(sql, null);
			ResultSet rs = stmt.executeQuery();
			Boolean found = false; 
			while (rs.next()) {
				found = true;
//				exported = exported + 1;

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

				BigDecimal HourDecimal = Env.ZERO;
				HourDecimal = rs.getBigDecimal("TotalInsurableHours").setScale(
						0, BigDecimal.ROUND_HALF_UP);

				createElement(document, roe, "B15A",
						convertToString(HourDecimal, 4));
				// createElement( document, roe ,"B15B",
				// convertToString(rs.getObject("TotalInsurableEarnings"), 9) );

				Element b15c = document.createElement("B15C");

				String Value = "";
				BigDecimal Amount = Env.ZERO;
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
							rs.getString("PhoneExt"));
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
/*
				String Comments = convertToString(rs.getObject("CommentsLine01"), 40).trim()	+ " " + // Roe.CommentsLine01
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
				Comments = Comments.replaceAll(" ", " ");
								
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
			}
			rs.close();
			stmt.close();
			
			if ( found == false ) {
				message = "ROE non trouvé, vérifier le période de paie";
			}
		} catch (SQLException e) {
			log.log(Level.WARNING, "DoIt()", e);
		}

		return document;

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
			javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(writer);
			
			Document doc = get_xmlDocument(xml.length() != 0);
			javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource( doc );
			javax.xml.transform.TransformerFactory tFactory = javax.xml.transform.TransformerFactory.newInstance();
			javax.xml.transform.Transformer transformer = tFactory.newTransformer();
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

	/*
	public void exportRoe()
	{
		System.out.println("* DEBUG * exportROE "  );
		log.warning("export ROE");		
		StringBuffer CrFile = new StringBuffer();
        try
		{
        	System.out.println("* DEBUG * CreateHeader "  );
    		CreateHeader(CrFile);
        	System.out.println("* DEBUG * CreateDetail "  );
    		CreateDetail(CrFile);
        	System.out.println("* DEBUG * WriteToFile "  );
    	    WriteToFile(CrFile);
		}
        catch (Exception e)
		{
        	log.log(Level.WARNING, "DoIt()", e);	
		}
    }
*/

/*	
	private void CreateHeader(StringBuffer XX)
	{
		XX.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\r\n");
		String RoeHeader = "<ROEHEADER Application=\"RoeWeb\" FileVersion=\"1.00\">";
		XX.append(RoeHeader); 
		XX.append("\r\n");
	}

	
    private String trxName = null;
    private BigDecimal ZERO = new java.math.BigDecimal(0.0);
    private int Count = 0;

    private void CreateDetail(StringBuffer CrFile)
    {
		log.info("export ROE - Create detail");		
	    String sql // On recherche les enregistrements dans l'ordre de l'annexe B
	    = " select "
	    	 + "   Employee.Value,  Employer.CanadaRevenueBusinessNumber,  Roe.PayPeriodType, "
			 + "   substring(SIN, 1, 3) + substring(SIN, 5, 3) + substring(SIN, 9, 3),"
	    	 + "   Employee.FirstName,  Employee.FirstLetter,  Employee.SurName,  Location.Address1,  Location.City,"
	    	 + "   Region.Name + ', ' + Country.Name + ', ' + Location.Postal, "
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
	    	 + "   Roe.VacationPayAmount,  null StatutoryHolidayPayDate01, "
	    	 + "   null StatutoryHolidayPayAmount01,  null StatutoryHolidayPayDate02  ,  null StatutoryHolidayPayAmount02,"
	    	 + "   null StatutoryHolidayPayDate03  ,  null StatutoryHolidayPayAmount03,  Roe.OtherMoniesCode01,"
	    	 + "   Roe.OtherMoniesAmount01,  Roe.OtherMoniesCode02,  Roe.OtherMoniesAmount02,  Roe.OtherMoniesCode03,"
	    	 + "   Roe.OtherMoniesAmount03,  isnull(Roe.CommentsLine01, '') CommentsLine01,  isnull(Roe.CommentsLine02,'') CommentsLine02,  isnull(Roe.CommentsLine03,'') CommentsLine03,"
	    	 + "   isnull(Roe.CommentsLine04,'') CommentsLine04,  Roe.PaidSickDate,  Roe.PaidSickAmount,  Roe.PaidSickPeriod,"
	    	 + "   Employer.CommunicationPreferredIn,  Employer.PrintLanguageRoe,  'D', P_Employee_Roe_ID, Frequency.NumberOfPeriod, ReasonForIssuingThisRoe,"
	    	 + "   C_Activity.Value as Activity, Roe.PrintLanguageID, SerialNumber, "
	    	 
				+ "	OtherMoniesStartDate01      , "
				+ "	OtherMoniesEndDate01        , "
				+ "	OtherMoniesStartDate02      , "
				+ "	OtherMoniesEndDate02        , "
				+ "	OtherMoniesStartDate03      , "
				+ "	OtherMoniesEndDate03        , "
				+ "	SpecialPaymentType01        , "
				+ "	SpecialPaymentType02        , "
				+ "	SpecialPaymentType03        , "
				+ "	SpecialPaymentType04        , "
				+ "	SpecialPaymentStartDate01   , "
				+ "	SpecialPaymentStartDate02   , "
				+ "	SpecialPaymentStartDate03   , "
				+ "	SpecialPaymentStartDate04   , "
				+ "	SpecialPaymentEndDate01     , "
				+ "	SpecialPaymentEndDate02     , "
				+ "	SpecialPaymentEndDate03     , "
				+ "	SpecialPaymentEndDate04     , "
				+ "	SpecialPaymentAmount01      , "
				+ "	SpecialPaymentAmount02      , "
				+ "	SpecialPaymentAmount03      , "
				+ "	SpecialPaymentAmount04      , "
				+ "	SpecialPaymentPeriodType01  , "
				+ "	SpecialPaymentPeriodType02  , "
				+ "	SpecialPaymentPeriodType03  , "
				+ "	SpecialPaymentPeriodType04  , "
				+ "	SpecialPaymentPeriod01      , "
				+ "	SpecialPaymentPeriod02      , "
				+ "	SpecialPaymentPeriod03      , "
				+ "	SpecialPaymentPeriod04      , "
				+ "	VacationPayComments         , "
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
	    	 + " right join C_Location Location on Location.C_Location_ID = Employee.C_Location_ID"
	    	 + " right join C_Country Country on Country.C_Country_ID = Location.C_Country_ID "
	    	 + " right join C_Region Region on Region.C_Region_ID = Location.C_Region_ID "
	    	 + " right join P_Payment_Group PG on PG.P_Payment_Group_ID = Employee.P_Payment_Group_ID"
	    	 + " right join P_Frequency Frequency  on Frequency.P_Frequency_ID = PG.P_Frequency_ID"
	    	 + " right join P_Employer Employer on Employer.P_Employer_ID = Employee.P_Employer_ID"
	    	 + " right join C_Activity on C_Activity.C_Activity_ID = Employee.C_Activity_ID"
	    	 + " where  Roe.P_Employee_ID = Employee.P_Employee_ID "
	    	 + "   AND Roe.P_Employee_Roe_ID = " + this.getP_Employee_Roe_ID()
	    	 ;
	    PreparedStatement stmt = null;
        try 
		{
    		log.info("export ROE - Create detail SQL " + sql);		

        	stmt = DB.prepareStatement(sql, null);
    	    ResultSet rs = stmt.executeQuery();
    	    while(rs.next())
    	    {
    	    	int EmpRoePrintLangID = rs.getInt("PrintLanguageID");
    	    	InsertBlank(CrFile,2);
//    	    	CrFile.append("<Roe PrintingLanguage=\"" + convertToString(rs.getObject(99), 1) + "\" " + "Issue=\"S\">" + "\r\n");
    	    	CrFile.append("<Roe PrintingLanguage=\"" + getEmployeeRoeLanguage(EmpRoePrintLangID) + "\" " + "Issue=\"S\">" + "\r\n");

    	    	InsertBlank(CrFile,4);
    	    	if ( rs.getObject("SerialNumber") != null )
    	    		CrFile.append("<B2>" + convertToString(rs.getObject("SerialNumber"), 9) + "</B2>" + "\r\n"); // 

    			if ( PgiUtil.getSolsticeParameter(Env.getCtx(), "ROE_EmployeeNo").equals("2") )
    	    	{
        	    	CrFile.append("<B3>" + convertToString(rs.getObject(1), 15).trim() + '(' + rs.getString("Activity") +')' + "</B3>" + "\r\n"); // Employee.Value 
    	    	}
    	    	else
    	    	{
        	    	CrFile.append("<B3>" + convertToString(rs.getObject(1), 15).trim() + "</B3>" + "\r\n"); // Employee.Value 
    	    	}
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
    		    	CrFile.append("<DT>" + convertToString(rs.getObject(16), 8).trim() + "</DT>" + "\r\n"); //  Roe.ExpectedDateOfRecall
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
    	    	String PhoneNumber = convertToString(rs.getObject("PhoneNumberContactPerson"), 13);   // Employer.PhoneNumberContactPerson
    	    	
    	    	PhoneNumber = PhoneNumber.replace( "-", "");
    	    		                     
    	    	InsertBlank(CrFile,6);
    	    	CrFile.append("<TEL>" + PhoneNumber.trim() + "</TEL>" + "\r\n");   
    	    	if (convertToString(rs.getObject("PhoneExt"), 5).trim().length() > 0)
    	    	{
    	    		InsertBlank(CrFile,6);
    		    	CrFile.append("<EXT>" + convertToString(rs.getObject("PhoneExt"), 5).trim() + "</EXT>" + "\r\n");   // Extention 
    	    	}
    	    	InsertBlank(CrFile,4);
    	    	CrFile.append("</B16>" + "\r\n");
    	    	
    	    	Value = convertToString(rs.getObject("VacationPayAmount"), 9);   // Roe.VacationPayAmount
    	    	if (Value.trim().length() > 0)  // (Amount.compareTo(ZERO) != 0)
    	    	{
    		    	InsertBlank(CrFile,4);
    		    	CrFile.append("<B17A>" + Value.trim() + "</B17A>" + "\r\n");   
    	    	}
    	    	int jour = 0;
    	    	String colonneNo = "";
    	    	if (   convertToString(rs.getObject("StatutoryHolidayAmount01"), 9).trim().length() > 0 
    	    		|| convertToString(rs.getObject("StatutoryHolidayAmount02"), 9).trim().length() > 0 
    	    		|| convertToString(rs.getObject("StatutoryHolidayAmount03"), 9).trim().length() > 0
    	    		|| convertToString(rs.getObject("StatutoryHolidayAmount04"), 9).trim().length() > 0
    	    		|| convertToString(rs.getObject("StatutoryHolidayAmount05"), 9).trim().length() > 0
    	    		|| convertToString(rs.getObject("StatutoryHolidayAmount06"), 9).trim().length() > 0
    	    		|| convertToString(rs.getObject("StatutoryHolidayAmount07"), 9).trim().length() > 0
    	    		|| convertToString(rs.getObject("StatutoryHolidayAmount08"), 9).trim().length() > 0
    	    		|| convertToString(rs.getObject("StatutoryHolidayAmount09"), 9).trim().length() > 0
    	    		|| convertToString(rs.getObject("StatutoryHolidayAmount10"), 9).trim().length() > 0
    	    		)
    	    	{
    		    	InsertBlank(CrFile,4);
    		    	CrFile.append("<B17B>" + "\r\n");   // Jour Férié
    		    	for (int i = 0; i < 9; i++)
    		    	{
    		    		if ( i < 10)
    		    			colonneNo =  "0".concat( String.valueOf( i ) );
    		    		else
    		    			colonneNo =  String.valueOf( i );
    		    		
    			    	jour = i + 1;
    			    	if (convertToString(rs.getObject(Colonne), 9).trim().length() > 0)
    			    	{
    				    	InsertBlank(CrFile,6);
    				    	CrFile.append("<SH nbr=\"" + jour + "\">" + "\r\n");   // Premier jours
    				    	InsertBlank(CrFile,8);
    				    	CrFile.append("<DT>" + convertToString(rs.getObject("StatutoryHolidayStartDate".concat( colonneNo ) ), 8) + "</DT>" + "\r\n");   // Roe.StatutoryHolidayStartDate
    				    	InsertBlank(CrFile,8);
    				    	CrFile.append("<AMT>" + convertToString(rs.getObject("StatutoryHolidayAmount".concat( colonneNo ) ), 9).trim() + "</AMT>" + "\r\n"); // Roe.StatutoryHolidayAmount
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
    		    	CrFile.append("<B17C>" + "\r\n");   // Autre montant
    		    	jour = 0;
    		    	Colonne = 0;
       		    	//2009.07.31
    		    	// for (int i = 0; i < 2; i++)
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
//ici    		    	
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

    	    	P_Employee_Roe Roe = P_Employee_Roe.get(Env.getCtx(), rs.getInt("P_Employee_Roe_ID"), trxName);
    	    	Roe.setTransfered(true);
    	    	Roe.save();
    			Count++;
    		    
    	    }
    	    rs.close();
    	    stmt.close();
    	    CrFile.append("</ROEHEADER>" + "\r\n");
		}
        catch (Exception e)
		{
        	log.log(Level.WARNING, "DoIt()", e);
		}
    }
*/	
	private void InsertBlank(StringBuffer XX, int Pos)
    {
    	for (int i = 0; i < Pos; i++)
    	{
    		XX.append(" ");
    	}
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

	/*
	private void WriteToFile(StringBuffer XX)
    {
    	
      String Path = "";
	  P_Employee Employee = P_Employee.get( Env.getCtx(), this.getP_Employee_ID(), null  );

      Calendar DateJour = Calendar.getInstance();

      String FileName = "ROE_" + Employee.getValue().trim() + "_" + Employee.getSurname() + "_" 
      + String.valueOf(DateJour.get(Calendar.YEAR)+ "." 
      + "00".substring(0, 2 - String.valueOf(DateJour.get(Calendar.MONTH)).trim().length()) + String.valueOf(DateJour.get(Calendar.MONTH)) + "."
      +  String.valueOf(DateJour.get(Calendar.DAY_OF_MONTH))) 
      +  "(" + String.valueOf( this.getP_Employee_Roe_ID() ) + ")"
      ;

	  System.out.println( "export ROE - file " + Path + FileName + ".blk"  );

	  log.warning("export ROE - file " + Path + FileName + ".blk" );		

//      String Month = String.valueOf(DateJour.get(Calendar.MONTH));
      
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
*/
	
	// 
	/**
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if( newRecord && success) // 
		{

			fillRoe();
			
			boolean retValue = this.save(); 
			return retValue;
		}
		return success;
	}
	**/
	
	protected boolean beforeSave (boolean newRecord)
	{
		//+ 2011.07.05 + sécurité par compagnie.
 	    P_Employee Employee = P_Employee.get( Env.getCtx(), this.getP_Employee_ID(), this.get_TrxName());
		this.setAD_Org_ID( Employee.getAD_Org_ID());
		//- 2011.07.05 + sécurité par compagnie.

		P_Payment_Group PaymentGroup = P_Payment_Group.get(Env.getCtx(), Employee.getP_Payment_Group_ID(), this.get_TrxName());
		P_Period Period = P_Period.get(Env.getCtx(), PaymentGroup.getP_Calendar_ID(), this.getFinalPayPeriodEndingDate(), this.get_TrxName());
		this.setP_Period_ID( Period.getP_Period_ID());
		
		//+2012.09.21
		// set de value if is null
		if  ( this.getPayPeriodType() == null )
		{
			P_Frequency Frequency = P_Frequency.get( Env.getCtx(), Period.getP_Frequency_ID(), this.get_TrxName());
			this.setPayPeriodType( Frequency.getPayPeriodType());
		}
		//-2012.09.21

/*
		if ( this.getP_Period_ID() == 0 )
		{
//            P_Employee Employee = P_Employee.get( Env.getCtx(), this.getP_Employee_ID(), this.get_TrxName());
            P_Period Period = P_Period.getOpenPeriodWithPaymentGroup(Env.getCtx(), Employee.getP_Payment_Group_ID(), this.get_TrxName());
			this.setP_Period_ID( Period.getP_Period_ID());
		}
*/		
		
		if ( Employee.getLayoffCode() != this.getReasonForIssuingThisRoe())
		{
			Employee.setLayoffCode( this.getReasonForIssuingThisRoe() );
			Employee.save();
		}
		
		return true;
	}

	// Convertit un objet de la bd en string selon une largeur
	private String convertToString(Object object, int width)
	{
		if ( width ==  0 )
			return "";

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
	    
		String ret = PgiUtil.convertAsciiString( object.toString().trim() );
		return pad( ret, width);
//	    return pad(object.toString().trim(), width);
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

	private String getEmployeeRoeLanguage(int printLangID)
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

	
	   /**
  * Procedure de copie. Lorsqu'on copie une deduction, on doit egalement
  * copier tous les parametres et le contenue des onglets sous celle-ci.
  */
	public static P_Employee_Roe copyFrom (Properties ctx, int P_Employee_Roe_ID, String trxName)
	{

		P_Employee_Roe from = P_Employee_Roe.get(ctx, P_Employee_Roe_ID, trxName);
		if (from.getP_Employee_Roe_ID() == 0)
			throw new IllegalArgumentException ("From ROE not found P_Employee_Roe_ID=" + P_Employee_Roe_ID);
		
		P_Employee_Roe  to = new P_Employee_Roe (ctx, -1, trxName);
		PO.copyValues(from, to, from.getAD_Client_ID(), from.getAD_Org_ID());
		to.set_ValueNoCheck ("P_Employee_Roe_ID", I_ZERO);

		to.setSerialNumber( String.valueOf( from.getP_Employee_Roe_ID()) );

		if (!to.save())
			throw new IllegalStateException("Could not create ROE");

		return to;

	}

	
    public static Timestamp getFirstDayWorked( Properties ctx, int employeeID)
    {
    	P_Employee employee = P_Employee.get(ctx, employeeID, null);

    	Timestamp firstDayWorked = null;
    	String sql;
    	// FirstDayWorked
    	// Retour le permier jour payé de l'employé, on ajout la notion de gaininfo pour éviter d'avoir une
    	// mauvaise date si l'employé a recu une rétro lors qu'il était inactif.
    	sql = "Select min(P_Time_Sheet_Detail.[Day]) as FirstDayWorked "
    		+ " from P_Time_Sheet  "
    		+ " inner join P_Time_Sheet_Detail  on P_Time_Sheet.P_Time_Sheet_ID = P_Time_Sheet_Detail.P_Time_Sheet_ID"
    		+ " where P_Time_Sheet.P_Employee_ID = " + employeeID
    		+ " and dbo.get_GainInfoString( P_Time_Sheet_Detail.P_Gain_ID, 'PYE' ) = 'Y' "
    		+ " and exists ( select 1  "
    		+ "            from P_Employee_ROE "
    		+ "           where P_Employee_ID = P_Time_Sheet.P_Employee_ID "
    		+ "              having max(FinalPayPeriodEndingDate) < P_Time_Sheet_Detail.[Day]) "
    		;
    	
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next() )
            {
            	if ( rs.getTimestamp( "FirstDayWorked" ) != null) 
            		firstDayWorked = rs.getTimestamp( "FirstDayWorked" );
                else
                {
                	firstDayWorked = employee.getDateHired();
                }
            }
            else
            {
            	firstDayWorked = employee.getDateHired();
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace(System.out);
        }
    	

      //+2011.03.15

        if ( employee.getDateHired() != null && employee.getDateHired().compareTo(firstDayWorked) > 0 ) 
        {
          	firstDayWorked = employee.getDateHired();
        }

        if ( employee.getDateRehired() != null && employee.getDateRehired().compareTo(firstDayWorked) > 0 )
        {
           	firstDayWorked = employee.getDateRehired();
        }
      //-2011.03.15
              
        return firstDayWorked;
    }
    
    public static Timestamp  getLastDayForWhichPaid( Properties ctx, int employeeID )
    {
    	P_Employee employee = P_Employee.get(ctx, employeeID, null);

// 2024-05-08    	
//    	if ( employee.getDateLayoff() != null)
//    		return employee.getDateLayoff();
    	
    	Timestamp lastDayForWhichPaid = null;
    	String sql;

    	sql = "Select max(P_Time_Sheet_Detail.[Day]) as FinalPayPeriodEndingDate"
    		+ "  from P_Time_Sheet  " 
    		+ " inner join P_Time_Sheet_Detail on P_Time_Sheet.P_Time_Sheet_ID = P_Time_Sheet_Detail.P_Time_Sheet_ID"
    		+ " inner join P_Gain on P_Time_Sheet_Detail.P_Gain_ID = P_Gain.P_Gain_ID"
    		+ " where P_Time_Sheet.P_Employee_ID = " + employeeID
//+2012.03.07     		
    		+ "  and P_Time_Sheet.SheetType not in ( '" + P_Time_Sheet.SHEETTYPE_Annulation_ + "','" + P_Time_Sheet.SHEETTYPE_AnnulPlus + "')"
//-2012.03.07     		
    		+ "  and P_Gain.P_Method_Gain_ID not in (102, 104, 107, 110, 113, 116) "
    		
    		;

        try
        {
        	PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
            	lastDayForWhichPaid = rs.getTimestamp("FinalPayPeriodEndingDate");
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace(System.out);
        }

        return lastDayForWhichPaid;

    }

    public static Timestamp  getFinalPayPeriodEndingDate( Properties ctx, int employeeID)
    {
    	Timestamp finalPayPeriodEndingDate = null;
    	String sql;
        // FinalPayPeriodEndingDate
    	sql = "Select max(P_Period.EndDate) as FinalPayPeriodEndingDate "
            + "from P_Time_Sheet "
			+ "inner join P_Time_Sheet_Detail On P_Time_Sheet_Detail.P_Time_Sheet_ID = P_Time_Sheet.P_Time_Sheet_ID "
			+ "inner join P_Period on P_Time_Sheet_Detail.P_Period_ID = P_Period.P_Period_ID "
			+ "inner join P_Gain   on P_Time_Sheet_Detail.P_Gain_ID = P_Gain.P_Gain_ID "
            + "where P_Time_Sheet.P_Employee_ID = " + employeeID
  //+2012.03.07     		
    		+ "  and P_Time_Sheet.SheetType not in ( '" + P_Time_Sheet.SHEETTYPE_Annulation_ + "','" + P_Time_Sheet.SHEETTYPE_AnnulPlus + "')"
 //-2012.03.07     		
        		
            + " and P_Gain.P_Method_Gain_ID not in (102, 104, 107, 110, 113, 116) "

            ;
    	
        try
        {
        	PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
            	finalPayPeriodEndingDate = rs.getTimestamp("FinalPayPeriodEndingDate");
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace(System.out);
        }
        return finalPayPeriodEndingDate;
    }
    

}
