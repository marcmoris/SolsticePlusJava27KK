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

import java.io.*;

import jxl.*;

import java.util.*;
import java.util.logging.Level;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.Compiere;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

import solstice.model.P_Year;
import solstice.utils.PgiUtil;

public class ExportToAvantaxV2 extends SvrProcess
{
	public int P_Year_ID;
	public int P_Employee_ID;
	public int P_Employer_ID;
	public int P_Form_ID;
    public String FormType;
    
	public P_Year Year;

	private static CLogger		log = CLogger.getCLogger (ExportToAvantaxV2.class);	

	
    String[] ColArrayHeadT4  = {"T4",  "Company.Name1", "Company.CompanyTag", "LastName","FirstName","Initial","Address1","Address2","City","Prov","Postal","Country","SIN","ProvEmp","EmpNum","EmpCode","CPPWeeks","SlipStatus","TaxYear","Income","CPP","QPP","CPP2","QPP2","EI","RPP","Tax","EIEarn","CPPEarn","CPPExempt","EIExempt","PPIPExempt","NoCPPAdjust","NoEIAdjust","NoPPIPAdjust","Union","DentalBenefits","Charitable","PensionNo","Pension","PPIP","PPIPEarn","OtherCode1","OtherAmt1","OtherCode2","OtherAmt2","OtherCode3","OtherAmt3","OtherCode4","OtherAmt4","OtherCode5","OtherAmt5","OtherCode6","OtherAmt6","NetPayOther","TextAtTop","CodeBoxOR1","IDNoQ","IsBusiness","IncomeR1","RPPR1","TaxR1","UnionR1","QPPEarn","HealthR1","TravelR1","OtherTBR1","Commissions","CharitableR1","OtherInc","Insurance","Deferred","IndianInc","Tips","TipsAllocated","PhasedRetire","Housing","AutoR1","FNote1R1","FNote2R1","FNote3R1","NetPayOtherR1","TextAtTopR1","EmailAddress","OkToEmailSlip","Serial","SerialMM","SERIALMMPREVIOUS","SerialOriginal","XBox01","XAmt01","XTxt01","XBox02","XAmt02","XTxt02","XBox03","XAmt03","XTxt03","XBox04","XAmt04","XTxt04","SlipTag", "CustomField",  "CustomPassword"};
    String[] ColArrayHeadR1  = {"R1",  "Company.Name1", "Company.CompanyTag", "LastName", "FirstName", "Initial", "Address1", "Address2", "City", "Prov", "Postal", "Country", "SIN", "ProvEmp", "EmpNum", "CPPWeeks", "SlipStatus", "TaxYear", "Income", "QPP", "QPP2", "EI", "Tax", "EIEarn", "CPPEarn", "CPPExempt", "PPIPExempt", "NoPPIPAdjust", "PPIP", "PPIPEarn", "NetPayOther", "CodeBoxOR1", "IDNoQ", "IsBusiness", "IncomeR1", "RPPR1", "TaxR1", "UnionR1", "QPPEarn", "HealthR1", "TravelR1", "OtherTBR1", "Commissions", "CharitableR1", "OtherInc", "Insurance", "Deferred", "IndianInc", "Tips", "TipsAllocated", "PhasedRetire", "Housing", "AutoR1", "FNote1R1", "FNote2R1", "FNote3R1", "NetPayOtherR1", "TextAtTopR1", "EmailAddress", "OkToEmailSlip", "Serial", "SerialMM", "SERIALMMPREVIOUS", "SerialOriginal", "XBox01", "XAmt01", "XTxt01", "XBox02", "XAmt02", "XTxt02", "XBox03", "XAmt03", "XTxt03", "XBox04", "XAmt04", "XTxt04", "SlipTag", "CustomField", "CustomPassword"};
    String[] ColArrayHeadR2  = {"R2",  "Company.Name1", "Company.CompanyTag", "LastName", "FirstName", "Initial", "Address1", "Address2", "City", "Prov", "Postal", "Country", "Year", "Source1", "BeneficiaryNum", "Annuity", "Benefit", "OtherPayment", "RefundRrspSpouse", "DeathBenefit", "RefundRrspUndeducted", "Revocation", "OtherIncome", "Deduction", "Tax", "IncomeAfterDeath", "LifelongLearning", "TaxPaidAmount", "SIN", "SIN2", "HomeBuyer", "ReportCode", "TextAtTop", "EmailAddress", "OkToEmailSlip", "Serial", "SerialMM", "SerialMMPrevious", "SerialOriginal", "XBox01", "XAmt01", "XTxt01", "XBox02", "XAmt02", "XTxt02", "XBox03", "XAmt03", "XTxt03", "XBox04", "XAmt04", "XTxt04", "SlipTag", "CustomField", "CustomPassword"};
    String[] ColArrayHeadR3  = {"R3",  "Company.Name1", "Company.CompanyTag", "Name1", "Name2", "LastName1", "FirstName1", "Initial1", "FirstIndividual", "LastName2", "FirstName2", "Initial2", "SecondIndividual", "Address1", "Address2", "City", "Prov", "Postal", "Country", "TaxYear", "ReportCodeR3", "RecTypeR3", "SIN", "SIN2", "PayerAssignedID", "Currency", "Transit", "IsInterestSavingsBonds", "AccountNoR3", "TextAtTopR3", "EmailAddress", "OkToEmailSlip", "Serial", "SerialMM", "SerialMMPrevious", "SerialOriginal", "ACTUALR3", "ActualR3_E", "InterestR3", "OtherR3", "ForIncR3", "ForTaxR3", "RoyaltiesR3", "CapGainsR3", "AccruedR3", "XBox01", "XAmt01", "XTxt01", "XBox02", "XAmt02", "XTxt02", "XBox03", "XAmt03", "XTxt03", "XBox04", "XAmt04", "XTxt04", "SlipTag", "CustomField", "CustomPassword"};
    String[] ColArrayHeadT5  = {"T5",  "Company.Name1", "Company.CompanyTag", "RecType", "SecondIndividual", "Name1", "Name2", "LastName1", "FirstName1", "Initial1", "LastName2", "FirstName2", "Initial2", "Address1", "Address2", "City", "Prov", "Postal", "Country", "TaxYear", "ReportCode", "SIN", "ACTUAL", "Actual_E", "Interest", "CapGains", "Other", "ForInc", "ForTax", "Royalties", "Accrued", "Resource", "Currency", "Transit", "AccountNo", "TextAtTop", "EmailAddress", "OkToEmailSlip", "SlipTag", "CustomField", "CustomPassword"};
    String[] ColArrayHeadT4A = {"T4A", "Company.Name1", "Company.CompanyTag", "LastName", "FirstName", "Initial", "Address1", "Address2", "City", "Prov", "Postal", "Country", "Year", "SlipStatus", "SIN", "RecBN", "RecipientNo", "CorpName1", "CorpName2", "Super", "LumpSum", "Self", "Tax", "Annuities", "Fees", "Retiring", "RetiringNon", "Other", "RESPPaymentsOther", "RecipientPaidHealthPlans", "LabourAdjustmentBenefits", "SUBPQualified", "CashAward", "Bankruptcy", "Patronage", "Past", "Pension", "PensionNo", "RespAccum", "RespEd", "Charitable", "TextAtTop", "UnregPen", "SIPension", "LumpSumAcc", "LumpSumSI", "LumpSumRPP", "LumpSumDPSP", "LumpSumNonRes", "LumpSumUnreg", "LumpSumNoTrans", "DPSPAnnuity", "IAACAnnuity", "RetiringSI", "RetiringNonSI", "OtherSI", "OtherDPSP", "BoardSite", "MedTravel", "LoanBenefit", "Research", "Scholarship", "WageLoss", "DeathBenefit", "MedBenefit", "Disability", "GroupTermLife", "VeteransBenefit", "ApprenticeshipIncentive", "TaxDefPatDividends", "RPPPre1990", "RegisteredDisability", "WageEarnerProtection", "VariablePension", "TFSATaxAmount", "ParentsMurderedChildrenGrant", "NonContributorRPPPastService", "PRPPPaymentsTaxable", "PRPPPaymentsExempt", "SaskPensionPlanSpousalContributorInd", "SaskPensionPlanSpousalContributorSIN", "AdultBasicEducation", "EmailAddress", "OkToEmailSlip", "SlipTag", "CustomField", "CustomPassword"};
	
    String[] ColArrayT4      = {"T4" , "Company", "CompanyTag",               "LastName","FirstName","Initial","Address1","Address2","City","Prov","Postal","Country","SIN","ProvEmp","EmpNum","EmpCode","CPPWeeks","SlipStatus","TaxYear","Income","CPP","QPP","CPP2","QPP2","EI","RPP","Tax","EIEarn","CPPEarn","CPPExempt","EIExempt","PPIPExempt","NoCPPAdjust","NoEIAdjust","NoPPIPAdjust","Union","DentalBenefits","Charitable","PensionNo","Pension","PPIP","PPIPEarn","OtherCode1","OtherAmt1","OtherCode2","OtherAmt2","OtherCode3","OtherAmt3","OtherCode4","OtherAmt4","OtherCode5","OtherAmt5","OtherCode6","OtherAmt6","NetPayOther","TextAtTop","CodeBoxOR1","IDNoQ","IsBusiness","IncomeR1","RPPR1","TaxR1","UnionR1","QPPEarn","HealthR1","TravelR1","OtherTBR1","Commissions","CharitableR1","OtherInc","Insurance","Deferred","IndianInc","Tips","TipsAllocated","PhasedRetire","Housing","AutoR1","FNote1R1","FNote2R1","FNote3R1","NetPayOtherR1","TextAtTopR1","EmailAddress","OkToEmailSlip","Serial","SerialMM","SERIALMMPREVIOUS","SerialOriginal","XBox01","XAmt01","XTxt01","XBox02","XAmt02","XTxt02","XBox03","XAmt03","XTxt03","XBox04","XAmt04","XTxt04","SlipTag", "CustomField", "CustomPassword"};
    String[] ColArrayR1      = {"R1",  "Company", "CompanyTag",               "LastName", "FirstName", "Initial", "Address1", "Address2", "City", "Prov", "Postal", "Country", "SIN", "ProvEmp", "EmpNum", "CPPWeeks", "SlipStatus", "TaxYear", "Income", "QPP", "QPP2", "EI", "Tax", "EIEarn", "CPPEarn", "CPPExempt", "PPIPExempt", "NoPPIPAdjust", "PPIP", "PPIPEarn", "NetPayOther", "CodeBoxOR1", "IDNoQ", "IsBusiness", "IncomeR1", "RPPR1", "TaxR1", "UnionR1", "QPPEarn", "HealthR1", "TravelR1", "OtherTBR1", "Commissions", "CharitableR1", "OtherInc", "Insurance", "Deferred", "IndianInc", "Tips", "TipsAllocated", "PhasedRetire", "Housing", "AutoR1", "FNote1R1", "FNote2R1", "FNote3R1", "NetPayOtherR1", "TextAtTopR1", "EmailAddress", "OkToEmailSlip", "Serial", "SerialMM", "SERIALMMPREVIOUS", "SerialOriginal", "XBox01", "XAmt01", "XTxt01", "XBox02", "XAmt02", "XTxt02", "XBox03", "XAmt03", "XTxt03", "XBox04", "XAmt04", "XTxt04", "SlipTag", "CustomField", "CustomPassword"};
    String[] ColArrayR2      = {"R2",  "Company", "CompanyTag",               "LastName", "FirstName", "Initial", "Address1", "Address2", "City", "Prov", "Postal", "Country", "Year", "Source1", "BeneficiaryNum", "Annuity", "Benefit", "OtherPayment", "RefundRrspSpouse", "DeathBenefit", "RefundRrspUndeducted", "Revocation", "OtherIncome", "Deduction", "Tax", "IncomeAfterDeath", "LifelongLearning", "TaxPaidAmount", "SIN", "SIN2", "HomeBuyer", "ReportCode", "TextAtTop", "EmailAddress", "OkToEmailSlip", "Serial", "SerialMM", "SerialMMPrevious", "SerialOriginal", "XBox01", "XAmt01", "XTxt01", "XBox02", "XAmt02", "XTxt02", "XBox03", "XAmt03", "XTxt03", "XBox04", "XAmt04", "XTxt04", "SlipTag", "CustomField", "CustomPassword"};
    String[] ColArrayR3      = {"R3",  "Company", "CompanyTag",               "Name1", "Name2", "LastName1", "FirstName1", "Initial1", "FirstIndividual", "LastName2", "FirstName2", "Initial2", "SecondIndividual", "Address1", "Address2", "City", "Prov", "Postal", "Country", "TaxYear", "ReportCodeR3", "RecTypeR3", "SIN", "SIN2", "PayerAssignedID", "Currency", "Transit", "IsInterestSavingsBonds", "AccountNoR3", "TextAtTopR3", "EmailAddress", "OkToEmailSlip", "Serial", "SerialMM", "SerialMMPrevious", "SerialOriginal", "ACTUALR3", "ActualR3_E", "InterestR3", "OtherR3", "ForIncR3", "ForTaxR3", "RoyaltiesR3", "CapGainsR3", "AccruedR3", "XBox01", "XAmt01", "XTxt01", "XBox02", "XAmt02", "XTxt02", "XBox03", "XAmt03", "XTxt03", "XBox04", "XAmt04", "XTxt04", "SlipTag", "CustomField", "CustomPassword"};
    String[] ColArrayT5      = {"T5",  "Company", "CompanyTag",               "RecType", "SecondIndividual", "Name1", "Name2", "LastName1", "FirstName1", "Initial1", "LastName2", "FirstName2", "Initial2", "Address1", "Address2", "City", "Prov", "Postal", "Country", "TaxYear", "ReportCode", "SIN", "ACTUAL", "Actual_E", "Interest", "CapGains", "Other", "ForInc", "ForTax", "Royalties", "Accrued", "Resource", "Currency", "Transit", "AccountNo", "TextAtTop", "EmailAddress", "OkToEmailSlip", "SlipTag", "CustomField", "CustomPassword"};
    String[] ColArrayT4A     = {"T4A", "Company", "CompanyTag",               "LastName", "FirstName", "Initial", "Address1", "Address2", "City", "Prov", "Postal", "Country", "Year", "SlipStatus", "SIN", "RecBN", "RecipientNo", "CorpName1", "CorpName2", "Super", "LumpSum", "Self", "Tax", "Annuities", "Fees", "Retiring", "RetiringNon", "Other", "RESPPaymentsOther", "RecipientPaidHealthPlans", "LabourAdjustmentBenefits", "SUBPQualified", "CashAward", "Bankruptcy", "Patronage", "Past", "Pension", "PensionNo", "RespAccum", "RespEd", "Charitable", "TextAtTop", "UnregPen", "SIPension", "LumpSumAcc", "LumpSumSI", "LumpSumRPP", "LumpSumDPSP", "LumpSumNonRes", "LumpSumUnreg", "LumpSumNoTrans", "DPSPAnnuity", "IAACAnnuity", "RetiringSI", "RetiringNonSI", "OtherSI", "OtherDPSP", "BoardSite", "MedTravel", "LoanBenefit", "Research", "Scholarship", "WageLoss", "DeathBenefit", "MedBenefit", "Disability", "GroupTermLife", "VeteransBenefit", "ApprenticeshipIncentive", "TaxDefPatDividends", "RPPPre1990", "RegisteredDisability", "WageEarnerProtection", "VariablePension", "TFSATaxAmount", "ParentsMurderedChildrenGrant", "NonContributorRPPPastService", "PRPPPaymentsTaxable", "PRPPPaymentsExempt", "SaskPensionPlanSpousalContributorInd", "SaskPensionPlanSpousalContributorSIN", "AdultBasicEducation", "EmailAddress", "OkToEmailSlip", "SlipTag", "CustomField", "CustomPassword"};

    
	protected void prepare() 
	{
		ProcessInfoParameter[] para = getParameter();
		String paramName = "";
		//Read the parameters
		for(int i=0; i < para.length; i++)
		{ 
			paramName = para[i].getParameterName();
			if (paramName.equals("P_Year_ID")){
				this.P_Year_ID = para[i].getParameterAsInt();
			}
			if (paramName.equals("P_Employee_ID")){
				this.P_Employee_ID = para[i].getParameterAsInt();
			}
			if (paramName.equals("P_Employer_ID")){
				this.P_Employer_ID = para[i].getParameterAsInt();
			}
			if (paramName.equals("P_Form_ID")){
				this.P_Form_ID = para[i].getParameterAsInt();
			}
			if (paramName.equals("P_Form_ID")){
				this.P_Form_ID = para[i].getParameterAsInt();
			}
/*
			if (paramName.equals("FormType")){
				this.FormType = (String)para[i].getParameter();
			}
*/
		}
	}
	
	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	protected String doIt() throws Exception 
	{
		System.out.println("ExportToAvantaxV2   $Revision: 1.0 $");
		System.out.println("----------------------------------");

	  	Year = P_Year.get( ctx, P_Year_ID, null );
	  	
		String Path = PgiUtil.getSolsticeParameter(ctx, "TransfertPath");
//		Path = "\\\\192.168.3.194\\hr$\\Payroll\\Avantax eForms 2017\\Data\\";
		String path = PgiUtil.getSolsticeParameter(Env.getCtx(), "AvantaxPath");
		
//		path = "C:\\PGI\\Transfert\\";
		if ( path.endsWith( File.separator ) == false )
			path = path + File.separator;

		Path = path + "Export" + File.separator;
		
		String filename =  Path + "Avantax_" + Year.getYear() + "-V22.xls";

		System.out.println("filename : " + filename );

		//
		int exported = 0;
		
	    try
	    {
	      WorkbookSettings ws = new WorkbookSettings();
	      ws.setLocale(new Locale("fr", "FR"));
	      WritableWorkbook workbook = 
          Workbook.createWorkbook(new File(filename), ws);
		  System.out.println("Company");
	      WritableSheet s1 = workbook.createSheet("Company", 0);
		  System.out.println("T4 & RL1");
	      WritableSheet s2 = workbook.createSheet("T4 & RL1", 1);
		  System.out.println("T5");
	      WritableSheet s3 = workbook.createSheet("T5", 1);
		  System.out.println("T4A");
	      WritableSheet s4 = workbook.createSheet("T4A", 1);
		  System.out.println("R1");
	      WritableSheet s5 = workbook.createSheet("R1", 1);
		  System.out.println("R2");
	      WritableSheet s6 = workbook.createSheet("R2", 1);
		  System.out.println("R3");
	      WritableSheet s7 = workbook.createSheet("R3", 1);
		  System.out.println("Company");

		  System.out.println("T5");
	      exported = writeDataSheetT5(s3 );

		  exported = writeDataSheetCie( s1 );
		  System.out.println("T4 & RL1");
	      exported = writeDataSheet(s2 );
		  System.out.println("T4A");
	      exported = writeDataSheetT4A(s4 );
		  System.out.println("R1");
	      exported = writeDataSheetR1(s5 );
		  System.out.println("R2");
	      exported = writeDataSheetR2(s6 );
		  System.out.println("R3");
	      exported = writeDataSheetR3(s7 );
	      workbook.write();
	      workbook.close();      
	    }
	    catch (IOException e)
	    {
	      e.printStackTrace();
	    }
	    catch (WriteException e)
	    {
	      e.printStackTrace();
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }

        PgiUtil.promptOpenExportedFile( filename );

/*        
		String filenameR1 =  Path + "Avantax_R1" + Year.getYear() + ".xls";

	    try
	    {
	      WorkbookSettings ws = new WorkbookSettings();
	      ws.setLocale(new Locale("fr", "FR"));
	      WritableWorkbook workbook = 
          Workbook.createWorkbook(new File(filenameR1), ws);
	      WritableSheet s1 = workbook.createSheet("Company", 0);
	      WritableSheet s2 = workbook.createSheet("T4 & RL1", 1);
	      exported = writeDataSheetCie( s1 );
	      exported = writeDataSheetR1(s2 );
	      workbook.write();
	      workbook.close();
	      
	      
	      
	    }
	    catch (IOException e)
	    {
	      e.printStackTrace();
	    }
	    catch (WriteException e)
	    {
	      e.printStackTrace();
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }

        PgiUtil.promptOpenExportedFile( filenameR1 );
*/
	    return "@Processed@ " + exported;
	}
	


	  private int writeDataSheet(WritableSheet s ) 
			    throws WriteException
	  {
		  int i = 5;
		    /* Format the Font */
		    WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
		    WritableCellFormat cf = new WritableCellFormat(wf);
		    cf.setWrap(false);
		    
		    WritableCellFormat cft = new WritableCellFormat(wf);
		    cft.setBorder(jxl.format.Border.TOP, jxl.format.BorderLineStyle.THIN);

		    WritableCellFormat NF = new WritableCellFormat(NumberFormats.FLOAT);
		    WritableFont wf2 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
	  	    WritableCellFormat cf2 = new WritableCellFormat(wf2);
	  	    cf2.setWrap(true);

//		    T4	Company.Name1	Company.CompanyTag	LastName	FirstName	Initial	Address1	Address2	City	Prov	Postal	Country	SIN	ProvEmp	EmpNum	EmpCode	CPPWeeks	SlipStatus	TaxYear	Income	CPP	QPP	EI	RPP	Tax	EIEarn	CPPEarn	CPPExempt	EIExempt	PPIPExempt	NoCPPAdjust	NoEIAdjust	NoPPIPAdjust	Union	Charitable	PensionNo	Pension	PPIP	PPIPEarn	OtherCode1	OtherAmt1	OtherCode2	OtherAmt2	OtherCode3	OtherAmt3	OtherCode4	OtherAmt4	OtherCode5	OtherAmt5	OtherCode6	OtherAmt6	NetPayOther	TextAtTop	CodeBoxOR1	IDNoQ	IsBusiness	IncomeR1	RPPR1	TaxR1	UnionR1	QPPEarn	HealthR1	TravelR1	OtherTBR1	Commissions	CharitableR1	OtherInc	Insurance	Deferred	IndianInc	Tips	TipsAllocated	PhasedRetire	Housing	AutoR1	FNote1R1	FNote2R1	FNote3R1	NetPayOtherR1	TextAtTopR1	EmailAddress	OkToEmailSlip	Serial	SerialMM	SERIALMMPREVIOUS	SerialOriginal	XBox01	XAmt01	XTxt01	XBox02	XAmt02	XTxt02	XBox03	XAmt03	XTxt03	XBox04	XAmt04	XTxt04	SlipTag

		    /* Creates Label and writes date to one cell of sheet*/
		    Label l;
		    int line = 0;
		    
		    
		    
		    
		    for (int col=0; col<(ColArrayHeadT4.length); col++ ) {

			    l = new Label( col, line, ColArrayHeadT4[ col ], cf );
			    s.addCell(l);
		    }

		  	
			String sql  = " SELECT * FROM [dbo].[SolsExportToAvantax] "
					+ " WHERE P_Year_ID = " + Year.getP_Year_ID()
//					+ " AND Form = 'T4'"
					;
					
			if ( P_Employee_ID != 0)
				sql += " AND P_Employee_ID = " + P_Employee_ID ;

			if ( P_Employer_ID != 0)
				sql += " AND P_Employer_ID = " + P_Employer_ID ;

			if ( P_Form_ID != 0)
				sql += " AND P_Form_ID = " + P_Form_ID ;

			if ( FormType  != null  )
				sql += " AND FormType = '" + FormType + "'" ;

			sql += " ORDER BY EmpNum, Form Desc";


			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();


				while (rs.next())
				{
					line++;
//					int currentCol =0;
				    for (int col=0; col<(ColArrayT4.length); col++ ) {
						l = new Label( col, line, rs.getString( ColArrayT4[ col ]) , cf );
							
					    s.addCell(l);
//					    currentCol = col;
				    }

//				    	l = new Label( 98, line, "99" , cf );
//				    s.addCell(l);

				}

				
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				System.err.println("ExportToAvantax - " + e);
			}
			return i;
	  }


	  private int writeDataSheetR1(WritableSheet s ) 
			    throws WriteException
	  {
		  int i = 5;
		    /* Format the Font */
		    WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
		    WritableCellFormat cf = new WritableCellFormat(wf);
		    cf.setWrap(false);
		    
		    WritableCellFormat cft = new WritableCellFormat(wf);
		    cft.setBorder(jxl.format.Border.TOP, jxl.format.BorderLineStyle.THIN);

		    WritableCellFormat NF = new WritableCellFormat(NumberFormats.FLOAT);
		    WritableFont wf2 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
	  	    WritableCellFormat cf2 = new WritableCellFormat(wf2);
	  	    cf2.setWrap(true);


		    /* Creates Label and writes date to one cell of sheet*/
		    Label l;
		    int line = 0;
		    
		    
		    
		    String[] ColArrayHead =  ColArrayHeadR1;
		    String[] ColArray =  ColArrayR1;
		    
		    for (int col=0; col<(ColArrayHead.length); col++ ) {

			    l = new Label( col, line, ColArrayHead[ col ], cf );
			    s.addCell(l);
		    }

			String sql  = " SELECT * FROM [dbo].[SolsExportToAvantax_R1] "
					+ " WHERE P_Year_ID = " + Year.getP_Year_ID()
					;
//					+ " AND Form = 'R1'";
					
			if ( P_Employee_ID != 0)
				sql += " AND P_Employee_ID = " + P_Employee_ID ;

			if ( P_Employer_ID != 0)
				sql += " AND P_Employer_ID = " + P_Employer_ID ;

			if ( P_Form_ID != 0)
				sql += " AND P_Form_ID = " + P_Form_ID ;

			if ( FormType  != null  )
				sql += " AND FormType = '" + FormType + "'" ;

			sql += " ORDER BY EmpNum, Form Desc";


			System.out.println("DEBUG SQL : " + sql);

			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();


				while (rs.next())
				{
					line++;
					int currentCol =0;
				    for (int col=0; col<(ColArray.length); col++ ) {
						l = new Label( col, line, rs.getString( ColArray[ col ]) , cf );
							
					    s.addCell(l);
					    currentCol = col;
				    }

//			    	l = new Label( 98, line, rs.getString( "SlipTag" ) , cf );

				}

				
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				System.err.println("ExportToAvantax R1 - " + e);
			}
			return i;
	  }

	  private int writeDataSheetR2(WritableSheet s ) 
			    throws WriteException
	  {
		  int i = 5;
		    /* Format the Font */
		    WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
		    WritableCellFormat cf = new WritableCellFormat(wf);
		    cf.setWrap(false);
		    
		    WritableCellFormat cft = new WritableCellFormat(wf);
		    cft.setBorder(jxl.format.Border.TOP, jxl.format.BorderLineStyle.THIN);

		    WritableCellFormat NF = new WritableCellFormat(NumberFormats.FLOAT);
		    WritableFont wf2 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
	  	    WritableCellFormat cf2 = new WritableCellFormat(wf2);
	  	    cf2.setWrap(true);


		    /* Creates Label and writes date to one cell of sheet*/
		    Label l;
		    int line = 0;
		    
		    
		    
		    String[] ColArrayHead =  ColArrayHeadR2;
		    String[] ColArray =  ColArrayR2;
		    
		    for (int col=0; col<(ColArrayHead.length); col++ ) {

			    l = new Label( col, line, ColArrayHead[ col ], cf );
			    s.addCell(l);
		    }

			String sql  = " SELECT * FROM [dbo].[SolsExportToAvantax_R2] "
					+ " WHERE P_Year_ID = " + Year.getP_Year_ID()
					+ " AND Form = 'R2'";
					
			if ( P_Employee_ID != 0)
				sql += " AND P_Employee_ID = " + P_Employee_ID ;

			if ( P_Employer_ID != 0)
				sql += " AND P_Employer_ID = " + P_Employer_ID ;

			if ( P_Form_ID != 0)
				sql += " AND P_Form_ID = " + P_Form_ID ;

			if ( FormType  != null  )
				sql += " AND FormType = '" + FormType + "'" ;

			sql += " ORDER BY EmpNum, Form Desc";


			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();


				while (rs.next())
				{
					line++;
					int currentCol =0;
				    for (int col=0; col<(ColArray.length); col++ ) {
						l = new Label( col, line, rs.getString( ColArray[ col ]) , cf );
							
					    s.addCell(l);
					    currentCol = col;
				    }

//			    	l = new Label( 98, line, rs.getString( "SlipTag" ) , cf );

				}

				
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				System.err.println("ExportToAvantaxV2 R2 - " + e);
			}
			return i;
	  }

	  private int writeDataSheetR3(WritableSheet s ) 
			    throws WriteException
	  {
		  int i = 5;
		    /* Format the Font */
		    WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
		    WritableCellFormat cf = new WritableCellFormat(wf);
		    cf.setWrap(false);
		    
		    WritableCellFormat cft = new WritableCellFormat(wf);
		    cft.setBorder(jxl.format.Border.TOP, jxl.format.BorderLineStyle.THIN);

		    WritableCellFormat NF = new WritableCellFormat(NumberFormats.FLOAT);
		    WritableFont wf2 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
	  	    WritableCellFormat cf2 = new WritableCellFormat(wf2);
	  	    cf2.setWrap(true);


		    /* Creates Label and writes date to one cell of sheet*/
		    Label l;
		    int line = 0;
		    
		    
		    
		    String[] ColArrayHead =  ColArrayHeadR3;
		    String[] ColArray =  ColArrayR3;
		    
		    for (int col=0; col<(ColArrayHead.length); col++ ) {

			    l = new Label( col, line, ColArrayHead[ col ], cf );
			    s.addCell(l);
		    }

			String sql  = " SELECT * FROM [dbo].[SolsExportToAvantax_R3] "
					+ " WHERE P_Year_ID = " + Year.getP_Year_ID()
					+ " AND Form = 'R3'";
					
			if ( P_Employee_ID != 0)
				sql += " AND P_Employee_ID = " + P_Employee_ID ;

			if ( P_Employer_ID != 0)
				sql += " AND P_Employer_ID = " + P_Employer_ID ;

			if ( P_Form_ID != 0)
				sql += " AND P_Form_ID = " + P_Form_ID ;

			if ( FormType  != null  )
				sql += " AND FormType = '" + FormType + "'" ;

			sql += " ORDER BY EmpNum, Form Desc";


			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();


				while (rs.next())
				{
					line++;
					int currentCol =0;
				    for (int col=0; col<(ColArray.length); col++ ) {
						l = new Label( col, line, rs.getString( ColArray[ col ]) , cf );
							
					    s.addCell(l);
					    currentCol = col;
				    }

//			    	l = new Label( 98, line, rs.getString( "SlipTag" ) , cf );

				}

				
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				System.err.println("ExportToAvantaxV2 - R3 " + e);
			}
			return i;
	  }

	  private int writeDataSheetT5(WritableSheet s ) 
			    throws WriteException
	  {
		  int i = 5;
		    /* Format the Font */
		    WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
		    WritableCellFormat cf = new WritableCellFormat(wf);
		    cf.setWrap(false);
		    
		    WritableCellFormat cft = new WritableCellFormat(wf);
		    cft.setBorder(jxl.format.Border.TOP, jxl.format.BorderLineStyle.THIN);

		    WritableCellFormat NF = new WritableCellFormat(NumberFormats.FLOAT);
		    WritableFont wf2 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
	  	    WritableCellFormat cf2 = new WritableCellFormat(wf2);
	  	    cf2.setWrap(true);


		    /* Creates Label and writes date to one cell of sheet*/
		    Label l;
		    int line = 0;
		    
		    
		    
		    String[] ColArrayHead =  ColArrayHeadT5;
		    String[] ColArray =  ColArrayT5;
		    
		    for (int col=0; col<(ColArrayHead.length); col++ ) {

			    l = new Label( col, line, ColArrayHead[ col ], cf );
			    s.addCell(l);
		    }

			String sql  = " SELECT * FROM [dbo].[SolsExportToAvantax_T5] "
					+ " WHERE P_Year_ID = " + Year.getP_Year_ID()
					+ " AND Form = 'T5'";
					
			if ( P_Employee_ID != 0)
				sql += " AND P_Employee_ID = " + P_Employee_ID ;

			if ( P_Employer_ID != 0)
				sql += " AND P_Employer_ID = " + P_Employer_ID ;

			if ( P_Form_ID != 0)
				sql += " AND P_Form_ID = " + P_Form_ID ;

			if ( FormType  != null  )
				sql += " AND FormType = '" + FormType + "'" ;

			sql += " ORDER BY EmpNum, Form Desc";


			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();


				while (rs.next())
				{
					line++;
					int currentCol =0;
				    for (int col=0; col<(ColArray.length); col++ ) {
						l = new Label( col, line, rs.getString( ColArray[ col ]) , cf );
							
					    s.addCell(l);
					    currentCol = col;
				    }

//			    	l = new Label( 98, line, rs.getString( "SlipTag" ) , cf );

				}

				
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				System.err.println("ExportToAvantaxV2 - T5 " + e);
			}
			return i;
	  }

	  private int writeDataSheetT4A(WritableSheet s ) 
			    throws WriteException
	  {
		  int i = 5;
		    /* Format the Font */
		    WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
		    WritableCellFormat cf = new WritableCellFormat(wf);
		    cf.setWrap(false);
		    
		    WritableCellFormat cft = new WritableCellFormat(wf);
		    cft.setBorder(jxl.format.Border.TOP, jxl.format.BorderLineStyle.THIN);

		    WritableCellFormat NF = new WritableCellFormat(NumberFormats.FLOAT);
		    WritableFont wf2 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
	  	    WritableCellFormat cf2 = new WritableCellFormat(wf2);
	  	    cf2.setWrap(true);


		    /* Creates Label and writes date to one cell of sheet*/
		    Label l;
		    int line = 0;
		    
		    
		    
		    String[] ColArrayHead =  ColArrayHeadT4A;
		    String[] ColArray =  ColArrayT4A;
		    
		    for (int col=0; col<(ColArrayHead.length); col++ ) {

			    l = new Label( col, line, ColArrayHead[ col ], cf );
			    s.addCell(l);
		    }

			String sql  = " SELECT * FROM [dbo].[SolsExportToAvantax_T4A] "
					+ " WHERE P_Year_ID = " + Year.getP_Year_ID()
					+ " AND Form = 'T4A'";
					
			if ( P_Employee_ID != 0)
				sql += " AND P_Employee_ID = " + P_Employee_ID ;

			if ( P_Employer_ID != 0)
				sql += " AND P_Employer_ID = " + P_Employer_ID ;

			if ( P_Form_ID != 0)
				sql += " AND P_Form_ID = " + P_Form_ID ;

			if ( FormType  != null  )
				sql += " AND FormType = '" + FormType + "'" ;

			sql += " ORDER BY EmpNum, Form Desc";


			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();


				while (rs.next())
				{
					line++;
					int currentCol =0;
				    for (int col=0; col<(ColArray.length); col++ ) {
						l = new Label( col, line, rs.getString( ColArray[ col ]) , cf );
							
					    s.addCell(l);
					    currentCol = col;
				    }

//			    	l = new Label( 98, line, rs.getString( "SlipTag" ) , cf );

				}

				
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				System.err.println("ExportToAvantaxV2 - " + e);
			}
			return i;
	  }

	

		Properties ctx = Env.getCtx();

		public ExportToAvantaxV2 ( )
		{
			
		}

		public ExportToAvantaxV2 ( boolean Standalone )
		{
			Env.setContext( ctx, "#AD_Client_ID", 11);
			Env.setContext( ctx, "#AD_Org_ID", 0);

			P_Year_ID = 1000112;
			try 
			{
				this.doIt();
			}
			catch (Exception e) 
			{
				log.log(Level.SEVERE, "Error :", e);
			}
		}

		private int writeDataSheetCie(WritableSheet s ) 
				    throws WriteException
				  {
					  int i = 5;
				    /* Format the Font */
				    WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
				    WritableCellFormat cf = new WritableCellFormat(wf);
				    cf.setWrap(false);
				    
				    WritableCellFormat cft = new WritableCellFormat(wf);
				    cft.setBorder(jxl.format.Border.TOP, jxl.format.BorderLineStyle.THIN);

				    WritableCellFormat NF = new WritableCellFormat(NumberFormats.FLOAT);
				    WritableFont wf2 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
			  	    WritableCellFormat cf2 = new WritableCellFormat(wf2);
			  	    cf2.setWrap(true);

				    /* Creates Label and writes date to one cell of sheet*/
				    Label l;
				    int line = 0;
				    
				    String[] ColArray =  {"COMPANY",	"CompanyTag",	"Name1",	"Name2",	"CareOf",	"Address1",	"Address2",	"City",	"Prov",	"Postal",	"Country",	"Category",	"AccountNo",	"AccountNoRZ",	"AccountNoRZ_T5013",	"AccountNoRZ_T5018",	"AccountNoRZ_TFSA",	"AccountNoT4RSP" , "AccountNoNR",	"IDNOQ",	"NEQ",	"WebCode",	"DefProvEmp", "EIFactor",	"AdjCPPOU",	"AdjXfrSin",	"AdjCPPWks",	"AdjMaxPenWks",	"AdjxfrIncr",	"AdjEIOverMax",	"AdjMin",	"AdjEIONEarn",	"AdjBdo",	"AdjEIEarn",	"AdjBDOMax",	"AdjPPIPOverMax",	"AdjPPIPonEarn",	"AdjPPIPEarn",	"UNLINKT5RL3" };
				    		
				    for (int col=0; col<(ColArray.length); col++ ) {

					    l = new Label( col, line, ColArray[ col ], cf );
					    s.addCell(l);
				    }
				    
					String sql  = " SELECT * FROM [dbo].[SolsExportToAvantaxCie] ";

					PreparedStatement pstmt = null;
					try
					{
						pstmt = DB.prepareStatement(sql, null);
						ResultSet rs = pstmt.executeQuery();

//					    int col = 0;

						while (rs.next())
						{
							line++;
						    for (int col=0; col<(ColArray.length); col++ ) {
						    	l = new Label( col, line, rs.getString( ColArray[ col ] ) , cf );
						    	s.addCell(l);
							}
						}

						rs.close();
						pstmt.close();
						pstmt = null;
					}
					catch (Exception e)
					{
						System.err.println("ExportToAvantaxV2 - " + e);
					}
					return i;
				  }

		public static void main (String[] args)
		{
			System.out.println("ExportToAvantaxV2");
			System.out.println("----------------------------------");
			//
			int count = 0;
			try
			{
				Compiere.startup(true);
			
				new ExportToAvantaxV2 ( true );
				count++;
			}
			catch (Exception e) {
				log.log(Level.SEVERE, "*ERROR ExportToAvantax Fail", e);
			}

			System.out.println("ExportToAvantaxV2 = " + count);

		}	//	main

	
}
