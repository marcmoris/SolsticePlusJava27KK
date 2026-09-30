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

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.model.MLocation;
import org.compiere.model.MClient;
import org.compiere.model.MOrg;
import org.compiere.model.MOrgInfo;
import org.compiere.model.MRegion;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.P_Employee;
import solstice.model.P_Employer;
import solstice.model.P_Form;
import solstice.model.P_Form_Employee;
import solstice.model.P_Form_Employee_Detail;
import solstice.model.P_Form_Param;
import solstice.model.P_Year;
import solstice.utils.PgiUtil;


public class XmlProvincial extends SvrProcess  {

	/** Logger */
	private static CLogger log = CLogger.getCLogger(XmlProvincial.class);
	//

	private Vector<P_Form_Employee> FormEmployeeVect =  new Vector<P_Form_Employee>(2000);
	int StartInformation = 0;

	private int P_Year_ID = 1000095;
	private int P_Form_ID = 1000000;
	private int P_Employee_ID = 0;
	private String trxName = this.get_TrxName();
	private String FormType = "O";
	private String m_Format = "PDF";
	
	public XmlProvincial( )
	{
	}
	public XmlProvincial( boolean standalon )
	{	   
		try
		{ 
			doIt();
        }
        catch (Exception e)
        {
            log.log(Level.WARNING, "XML_Provincial", e);
        }
	}
	
	protected void prepare() 
	{
		ProcessInfoParameter[] para = getParameter();
		String paramName = "";
		for (int i = 0; i < para.length; i++)
	  	{
  	        paramName = para[i].getParameterName();
	  	      
  	        if (para[i].getParameter() == null)
  		    ;
  	        else if (paramName.equals("P_Year_ID"))
  	        	this.P_Year_ID = para[i].getParameterAsInt();
  	        else if (paramName.equals("P_Form_ID"))
  	        	this.P_Form_ID = para[i].getParameterAsInt();
  	        else if (paramName.equals("P_Employee_ID"))
  	        	this.P_Employee_ID = para[i].getParameterAsInt();
  	        else if (paramName.equals("FormType"))
  	        	this.FormType = (String)para[i].getParameter();
            else if (paramName.equals("CrystalFormat"))
				m_Format = (String)para[i].getParameter();
  	      
	  	}
  	
	}

	
	private StringBuffer generateCaseRensCompl( Properties ctx, int P_Form_Employee_ID, String trxName )
	{
		StringBuffer result = new StringBuffer("");
		
		String sql = "Select P_FORM_EMPLOYEE_DETAIL.P_FORM_EMPLOYEE_DETAIL_ID, XmlTag"
			       + " FROM P_FORM_EMPLOYEE_DETAIL  "
			       + "  INNER JOIN P_FORM_FIELD ON P_FORM_FIELD.P_FORM_FIELD_ID = P_FORM_EMPLOYEE_DETAIL.P_FORM_FIELD_ID  and P_FORM_FIELD.isActive = 'Y'"
			       + " WHERE P_FORM_EMPLOYEE_DETAIL.P_FORM_EMPLOYEE_ID = " + P_Form_Employee_ID
			       + " AND IsOtherInformation = 'Y'"
			       + " AND isnull( P_Form_Field.XmlTag, '') <> '' "
			       + " Order BY P_Form_Field.XmlTag  "
			       ;
//		sql = MRole.getDefault().addAccessSQL (sql, "P_Form_Employee", true, false);	// fully qualidfied - RO 
		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next())
			{
				String xmlTag = rs.getString("xmlTag");
				P_Form_Employee_Detail Detail = P_Form_Employee_Detail.get( ctx, rs.getInt("P_Form_Employee_Detail_ID"), trxName );

				if ( PgiUtil.nvl(Detail.getFormFieldAmount() ).compareTo( Env.ZERO) != 0 || PgiUtil.nvl( Detail.getFormFieldText() ).trim().length() != 0 )
				{
					result.append("<CaseRensCompl>").append( "\r\n");

					result.append( "<CodeRensCompl>" + xmlTag + "</CodeRensCompl>" ).append( "\r\n");

					result.append( "<DonneeRensCompl>" );
//					result.append( xmlTag );
					if ( Detail.getFormFieldAmount() != null  )
						result.append( String.valueOf( Detail.getFormFieldAmount()) );
					else if ( Detail.getFormFieldText() != null )
						result.append( Detail.getFormFieldText() );
					 
					result.append( "</DonneeRensCompl>" ).append( "\r\n");
					result.append("</CaseRensCompl>").append( "\r\n");

				}
				
			}
			rs.close();
			stmt.close();

		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		
		return result;
	}
	
	
	private StringBuffer generateAmountDetail( Properties ctx, int P_Form_Employee_ID, String trxName )
	{
		StringBuffer result = new StringBuffer("");
		
		String sql = "Select P_FORM_EMPLOYEE_DETAIL.P_FORM_EMPLOYEE_DETAIL_ID, XmlTag"
			       + " FROM P_FORM_EMPLOYEE_DETAIL  "
			       + "  INNER JOIN P_FORM_FIELD ON P_FORM_FIELD.P_FORM_FIELD_ID = P_FORM_EMPLOYEE_DETAIL.P_FORM_FIELD_ID and P_FORM_FIELD.isActive = 'Y' "
			       + " WHERE P_FORM_EMPLOYEE_DETAIL.P_FORM_EMPLOYEE_ID = " + P_Form_Employee_ID
			       + " AND IsOtherInformation = 'N'"
			       + " AND isnull( P_Form_Field.XmlTag, '') <> '' "
			       + " Order BY P_Form_Field.XmlTag  "
			       ;
//		sql = MRole.getDefault().addAccessSQL (sql, "P_Form_Employee", true, false);	// fully qualidfied - RO 
		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			BigDecimal MontantCaseO = null;
			String    SourceCaseO = "";
			
			while(rs.next())
			{
				String xmlTag = rs.getString("xmlTag");
				P_Form_Employee_Detail Detail = P_Form_Employee_Detail.get( ctx, rs.getInt("P_Form_Employee_Detail_ID"), trxName );
				
				if ( xmlTag.trim().equals( "<MontantCaseO>"))
					MontantCaseO = Detail.getFormFieldAmount();
				else if ( xmlTag.trim().equals( "<SourceCaseO>"))
					SourceCaseO = Detail.getFormFieldText();
				else
				{
					if ( PgiUtil.nvl(Detail.getFormFieldAmount() ).compareTo( Env.ZERO) != 0 || PgiUtil.nvl( Detail.getFormFieldText() ).trim().length() != 0 )
					{
						result.append( xmlTag );
						if ( Detail.getFormFieldAmount() != null  )
							result.append( String.valueOf( Detail.getFormFieldAmount()) );
						else if ( Detail.getFormFieldText() != null )
							result.append( Detail.getFormFieldText() );
						 

						result.append( xmlTag.replace( "<", "</") ).append( "\r\n");
					}
				}

				if ( xmlTag.trim().equals( "<SourceCaseO>"))
				{
					if ( MontantCaseO != null  && MontantCaseO.compareTo( Env.ZERO) != 0 )
					{
						result.append("<O_AutreRevenu>" ).append( "\r\n");
						result.append("<MontantCaseO>" + String.valueOf( MontantCaseO ) + "</MontantCaseO>" ).append( "\r\n");
						result.append("<SourceCaseO>" + SourceCaseO + "</SourceCaseO>" ).append( "\r\n");
						result.append("</O_AutreRevenu>" ).append( "\r\n");
					}
				}
			}
			rs.close();
			stmt.close();

		

		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		
		return result;
	}

	int NbReleves = 0;
	private String FormTypeXml = "R";
	
	private StringBuffer generateEmployeeDetail( Properties ctx, String FormType, P_Form_Param FormParam, String trxName )
	{
		
		
		//		sql = MRole.getDefault().addAccessSQL (sql, "P_Form_Employee", true, false);	// fully qualidfied - RO

		if ( FormType.equals("O"))
			FormTypeXml = "R";
		if ( FormType.equals("M"))
			FormTypeXml = "A";
		if ( FormType.equals("C"))
			FormTypeXml = "D";
		
		StringBuffer result = new StringBuffer("");
		
		NbReleves = 0;
		
		// StartInformation doit valoir 0 ou 1000 ou 2000
		for ( int i=StartInformation; i< FormEmployeeVect.size() && NbReleves < 1000; i++)
		{
			P_Form_Employee FormEmployee = FormEmployeeVect.get(i);
			MOrgInfo OrgInfo = MOrgInfo.get(ctx, FormEmployee.getAD_Org_ID(), trxName);
			if ( OrgInfo.getParent_Org_ID() == Org.getAD_Org_ID()   )
			{
				NbReleves = NbReleves + 1;
				P_Employee Employee  = P_Employee.get( ctx, FormEmployee.getP_Employee_ID(), trxName);
				MLocation Location = MLocation.get(ctx, Employee.getC_Location_ID(), trxName);
				MRegion InfoRegion = MRegion.get( ctx, Location.getC_Region_ID());

				result.append("<" + FormTypeXml + ">").append( "\r\n");
				result.append("<Annee>" + Year.getYear() + "</Annee>").append( "\r\n");
				result.append("<NoReleve>" + FormEmployee.getFormNumber() + "</NoReleve>").append( "\r\n");

				
				
				
				if ( Form.getValue().trim().equals("R1"))
				{
					result.append("<Identification>").append( "\r\n");
					result.append("<Employe>").append( "\r\n");
				}
				if ( Form.getValue().trim().equals("R2") )
				{
					result.append("<Beneficiaire>").append( "\r\n");
				}

				if ( Form.getValue().trim().equals("R3") )
				{
					result.append("<Beneficiaire>").append( "\r\n");
					result.append("<Type>1</Type>").append( "\r\n");
					result.append("<Personne>").append( "\r\n");
				}

				if ( Employee != null)
				{
					result.append("<NAS>" + Employee.getSin().replace( "-", "") +"</NAS>").append( "\r\n");
					if ( Form.getValue().trim().equals("R1") || Form.getValue().trim().equals("R2"))
					{
						if ( FormTypeXml.equals("D") == false )
							result.append("<No>" + Employee.getValue() + "</No>").append( "\r\n");
					}	
					
	       			if ( Employee.getSurname().trim().length() > 30)
	       				result.append("<NomFamille>" + Employee.getSurname().trim().substring(0,29) + "</NomFamille>").append( "\r\n");
	       			else
	       				result.append("<NomFamille>" + Employee.getSurname().trim() + "</NomFamille>").append( "\r\n");
	       				
	       			if ( Employee.getFirstName().trim().length() > 30)
	       				result.append("<Prenom>" + Employee.getFirstName().trim().substring(0,29) + "</Prenom>").append( "\r\n");
	       			else
	       				result.append("<Prenom>" + Employee.getFirstName().trim() + "</Prenom>").append( "\r\n");

					if ( FormTypeXml.equals("D") == false && Employee.getFirstLetter() != null )
						result.append("<Initiale>" + Employee.getFirstLetter().substring(0,1) + "</Initiale>").append( "\r\n");
					
				}
				

				if ( Form.getValue().trim().equals("R1"))
				{
					result.append("</Employe>").append( "\r\n");
					result.append("</Identification>").append( "\r\n");
				}

				
				if ( Form.getValue().trim().equals("R3") )
				{
					result.append("</Personne>").append( "\r\n");
				}

				if ( FormTypeXml.equals("D") == false )
				{
					result.append("<Adresse>").append( "\r\n");
					if ( PgiUtil.nvl(Location.getAddress1() , " ").length() > 30 )
						result.append("<Ligne1>" + nvl(Location.getAddress1() , " ").substring(0,29) + "</Ligne1>").append( "\r\n");
					else
						result.append("<Ligne1>" + nvl(Location.getAddress1() , " ") + "</Ligne1>").append( "\r\n");
					if ( PgiUtil.nvl(Location.getAddress2() , " ").trim().length() != 0 )
						result.append("<Ligne2>" + nvl(Location.getAddress2() , " ") + "</Ligne2>").append( "\r\n");
					if ( Location.getCity().length() >= 30)
						result.append("<Ville>" + nvl(Location.getCity().substring(0,29)) + "</Ville>").append( "\r\n");
					else
						result.append("<Ville>" + Location.getCity() + "</Ville>").append( "\r\n");
					
					result.append("<Province>" + PgiUtil.nvl(InfoRegion.getDescription()) +"</Province>").append( "\r\n");
					result.append("<CodePostal>" + Location.getPostal().replace(" ", "").replace("-", "")  + "</CodePostal>").append( "\r\n");
					result.append("</Adresse>").append( "\r\n");
				}

				if ( Form.getValue().trim().equals("R2") )
				{
					result.append("</Beneficiaire>").append( "\r\n");
				}

				if ( Form.getValue().trim().equals("R3") )
				{
					result.append("</Beneficiaire>").append( "\r\n");
				}

				if ( FormTypeXml.equals("D") == false  )
				{
					result.append("<Montants>").append( "\r\n");
					result.append( generateAmountDetail( ctx, FormEmployee.getP_Form_Employee_ID(), trxName ) );
					result.append("</Montants>").append( "\r\n");

					result.append( generateCaseRensCompl( ctx, FormEmployee.getP_Form_Employee_ID(), trxName ) );

				}

				if ( FormTypeXml.equals("A")   )
				{
					result.append("<NoReleveDerniereTrans>" + getBox(FormEmployee.getP_Form_Employee_ID(), "NO") + "</NoReleveDerniereTrans>").append( "\r\n");
				}

				result.append("</" + FormTypeXml + ">").append( "\r\n");

				FormEmployee.setIsGenerated( true );
				FormEmployee.save();
				
			}
			
		}
		
		return result;
	}

	private StringBuffer generateTotal( Properties ctx, String trxName )
	{
		StringBuffer result = new StringBuffer("<T>").append( "\r\n");
		result.append("<Annee>" + Year.getYear() + "</Annee>").append( "\r\n");
		result.append("<NbReleves>" + String.valueOf( NbReleves ) + "</NbReleves>").append( "\r\n");
		NbReleves = 0 ;
		
		if ( Form.getValue().trim().equals("R1"))
		{
			result.append("<Employeur>").append( "\r\n");
		}
		if ( Form.getValue().trim().equals("R2"))
		{
			result.append("<PayeurEmetteur>").append( "\r\n");
		}

		if ( Form.getValue().trim().equals("R3"))
		{
			result.append("<PayeurMandataire>").append( "\r\n");
		}

		result.append("<NoId>" + EmployerInfo.getCanadaRevenueBusinessNumber() + "</NoId>").append( "\r\n");
		result.append("<TypeDossier>RS</TypeDossier>").append( "\r\n");
		result.append("<NoDossier>" + FormParam.getFileNumber() +"</NoDossier>").append( "\r\n");
		result.append("<NEQ>" + FormParam.getNEQ() + "</NEQ>").append( "\r\n");
		if ( Form.getValue().trim().equals("R1"))
		{
			if ( FormParam.getAD_Org_ID() == 0 )
			{
				result.append("<Nom1>" + nvl(Client.getName() + "                              ").substring(0,29).trim() + "</Nom1>").append( "\r\n");
			}
			else result.append("<Nom1>" + nvl(Org.getDescription() + "                              ").substring(0,29).trim() + "</Nom1>").append( "\r\n");
			//		result.append("<Nom2></Nom2>").append( "\r\n");
		}	
		if ( Form.getValue().trim().equals("R2"))
		{
			if ( FormParam.getAD_Org_ID() == 0 )
			{
				result.append("<Nom>" + nvl(Client.getName() + "                              ").substring(0,29).trim() + "</Nom>").append( "\r\n");
			}
			else result.append("<Nom>" + nvl(Org.getDescription() + "                              ").substring(0,29).trim() + "</Nom>").append( "\r\n");
			//		result.append("<Nom2></Nom2>").append( "\r\n");
		}	
		if ( Form.getValue().trim().equals("R3"))
		{
			if ( FormParam.getAD_Org_ID() == 0 )
			{
				result.append("<Nom>" + nvl( Client.getName() + "                              ").substring(0,29).trim() + "</Nom>").append( "\r\n");
			}
			else result.append("<Nom>" + nvl( Org.getDescription() + "                              ").substring(0,29).trim() + "</Nom>").append( "\r\n");
			//		result.append("<Nom2></Nom2>").append( "\r\n");
		}	
		result.append("<Adresse>").append( "\r\n");
		if  ( PgiUtil.nvl( EmployerInfoLocation.getAddress1(), " ").length() > 30 )
			result.append("<Ligne1>" + nvl(EmployerInfoLocation.getAddress1().substring(0,29)) + "</Ligne1>").append( "\r\n");
		else
			result.append("<Ligne1>" + nvl(EmployerInfoLocation.getAddress1()) + "</Ligne1>").append( "\r\n");
		if ( PgiUtil.nvl(EmployerInfoLocation.getAddress2() , " ").trim().length() != 0 )
			result.append("<Ligne2>" + nvl( EmployerInfoLocation.getAddress2(), " ") + "</Ligne2>").append( "\r\n");
		result.append("<Ville>"  + EmployerInfoLocation.getCity() + "</Ville>").append( "\r\n");
		result.append("<Province>" + nvl(EmployerInfoRegion.getName()) +"</Province>").append( "\r\n");
		result.append("<CodePostal>" + EmployerInfoLocation.getPostal().replace(" ", "").replace("-", "") + "</CodePostal>").append( "\r\n");
		result.append("</Adresse>").append( "\r\n");
		if ( Form.getValue().trim().equals("R1"))
		{
			result.append("</Employeur>").append( "\r\n");
		}
		if ( Form.getValue().trim().equals("R2"))
		{
			result.append("</PayeurEmetteur>").append( "\r\n");
		}

		if ( Form.getValue().trim().equals("R3"))
		{
			result.append("</PayeurMandataire>").append( "\r\n");
		}

		
		result.append("</T>").append( "\r\n");
		return result;
	}
	
	private String getBox( int P_Form_Employee_ID, String box)
	{
		String result = "";
		String sql = "Select FormFieldText From P_Form_Employee_Detail "
			       + " inner join P_Form_Field on P_Form_Field.P_Form_Field_ID = P_Form_Employee_Detail.P_Form_Field_ID "
				   + " Where P_Form_Employee_ID = " + P_Form_Employee_ID
				   + " AND P_Form_Field.Value = '" + box + "'";
	    PreparedStatement ps = null;
	    try
	  	{ 
            ps = DB.prepareStatement(sql, null);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
            {
            	result = nvl( rs.getString("FormFieldText"));
            }
	        rs.close();
	        ps.close();
	     }
	     catch (Exception e)
	     {
	         log.log(Level.WARNING, "XML_Provincial.getBox", e);
	     }

	     return result;
	}
	
	private P_Year Year;
	private P_Employer EmployerInfo;
//	private MClient Client;
	private MClient Client;
	private MOrg Org;
	private P_Form_Param FormParam;
	private MLocation EmployerInfoLocation;
	private MRegion EmployerInfoRegion;
	private P_Form Form; 
	
	private boolean firstTime = true;
	
	private StringBuffer generateNEQ( Properties ctx, String trxName, boolean isLast )
	{
		Form = P_Form.get( ctx, P_Form_ID, trxName );
//		Client = MClient.get( Env.getCtx());
		
		StringBuffer xmlStr = new StringBuffer( "");

		P_Employee TechResource = P_Employee.get( ctx, FormParam.getP_Employee_ID(), trxName);
		P_Employee AccountingResource = P_Employee.get( ctx, FormParam.getP_Employee_Accounting_ID(), trxName);
		
		EmployerInfo = P_Employer.getProvincialWithOrg( ctx, FormParam.getAD_Org_ID(), trxName);
		EmployerInfoLocation = MLocation.get( ctx, EmployerInfo.getC_Location_ID(), trxName);
		EmployerInfoRegion = MRegion.get( ctx, EmployerInfoLocation.getC_Region_ID());
		

		String sql = "Select * from P_Form_XML Where P_Form_ID = " + P_Form_ID + " Order By [LineNo] ";
	    PreparedStatement psdoc = null;
	    try
	  	{ 
            psdoc = DB.prepareStatement(sql, null);
            ResultSet rsdoc = psdoc.executeQuery();
            String XmlInfo = "";
            
            if ( firstTime == false )
            while (rsdoc.next() && rsdoc.getString( "XmlInfo").compareTo( "</P>") != 0)
            {
            	XmlInfo = rsdoc.getString( "XmlInfo");
            }
            
            firstTime = false;
            
            while (rsdoc.next())
            {
            	XmlInfo = rsdoc.getString( "XmlInfo");

            	if ( XmlInfo.equals("</Transmission>") && isLast == false )
            	{
            		XmlInfo = "";
            	}
        		if (XmlInfo.equals("Detail"))
        		{
        			xmlStr.append( generateEmployeeDetail( ctx, FormType, FormParam, trxName) );
//        			xmlStr.append( generateEmployeeDetail( ctx, "M", trxName) );
//        			xmlStr.append( generateEmployeeDetail( ctx, "C", trxName) );
        		}
        		else if (XmlInfo.equals("Total"))
        		{
        			xmlStr.append( generateTotal( ctx, trxName ) );
        		}
        		else
        		{

        			if ( FormParam.getPackageType().equals( P_Form_Param.PACKAGETYPE_File_Test))
        				XmlInfo = XmlInfo.replace( "SendType", "3" );
        			else
        			{
            			if ( FormType.equals( P_Form_Employee.FORMTYPE_Original))
            				XmlInfo = XmlInfo.replace( "SendType", "1" );
            			else if ( FormType.equals( P_Form_Employee.FORMTYPE_Modified))
            				XmlInfo = XmlInfo.replace( "SendType", "4" );
            			else if ( FormType.equals( P_Form_Employee.FORMTYPE_Cancelled))
            				XmlInfo = XmlInfo.replace( "SendType", "6" );
        				
        			}

                	XmlInfo = XmlInfo.replace( "TransmitterNumber", FormParam.getTransmitterNumber() );
                	XmlInfo = XmlInfo.replace( "TransmitterType", FormParam.getTransmitterType() );
                	XmlInfo = XmlInfo.replace( "TransmitterName2", " " );
        			if ( FormParam.getAD_Org_ID() == 0 )
        			{
                    	XmlInfo = XmlInfo.replace( "TransmitterName", nvl(Client.getName()+ "                              ").substring(0,29).trim() );
        			}
        			else if ( Org.getDescription() != null )
        					XmlInfo = XmlInfo.replace( "TransmitterName", nvl(Org.getDescription() + "                              ").substring(0,29).trim() );
        			else
        				XmlInfo = XmlInfo.replace( "TransmitterName", nvl(Org.getName() + "                              ").substring(0,29).trim() );

                	if ( PgiUtil.nvl(EmployerInfoLocation.getAddress1() , " ").length() > 30 )
                		XmlInfo = XmlInfo.replace( "TransmitterAddress1", nvl(EmployerInfoLocation.getAddress1().substring(0,29) , " ") );
                	else 
                		XmlInfo = XmlInfo.replace( "TransmitterAddress1", nvl(EmployerInfoLocation.getAddress1() , " ") );
                	XmlInfo = XmlInfo.replace( "TransmitterAddress2", nvl( EmployerInfoLocation.getAddress2(), " ") );
                	XmlInfo = XmlInfo.replace( "TransmitterCity", EmployerInfoLocation.getCity() );
                	XmlInfo = XmlInfo.replace( "TransmitterProvince", PgiUtil.nvl(EmployerInfoRegion.getName(), " "));
                	XmlInfo = XmlInfo.replace( "TransmitterZipCode", EmployerInfoLocation.getPostal().replace(" ", "").replace("-", ""));
                	
                	XmlInfo = XmlInfo.replace( "P_Year.Year", String.valueOf( Year.getYear()) );
                	XmlInfo = XmlInfo.replace( "TechResource.Name", nvl(TechResource.getName()) );
                	XmlInfo = XmlInfo.replace( "TechResource.PhoneAreaCode" , nvl(FormParam.getPhoneAreaCodeContactPerson(), " "));
                	XmlInfo = XmlInfo.replace( "TechResource.PhoneNumber", nvl(FormParam.getPhoneNumberContactPerson(), " "));
           			XmlInfo = XmlInfo.replace( "TechResource.PhoneExtension", nvl(PgiUtil.nvl(FormParam.getPhoneExtensionContactPerson(), "0")));
                	XmlInfo = XmlInfo.replace( "TechResource.CommunicationPreferred", nvl(FormParam.getCommunicationPreferred(), "F"));

                	XmlInfo = XmlInfo.replace( "AccountingResource.Name", AccountingResource.getName() );
                	XmlInfo = XmlInfo.replace( "AccountingResource.PhoneAreaCode" , FormParam.getPhoneAreaCodeAccountingPerson());
                	XmlInfo = XmlInfo.replace( "AccountingResource.PhoneNumber", FormParam.getPhoneNumberAccountingPerson());
               		XmlInfo = XmlInfo.replace( "AccountingResource.PhoneExtension", PgiUtil.nvl(FormParam.getPhoneExtensionAccountingPerson(), "0"));
                	XmlInfo = XmlInfo.replace( "AccountingResource.CommunicationPreferred", PgiUtil.nvl(FormParam.getCommunicationPreferredAccoungtingPerson(), "F"));

            		xmlStr.append( XmlInfo ).append( "\r\n");
        		}

            	
        		
            }
            rsdoc.close();
            psdoc.close();
         }
         catch (Exception e)
         {
             log.log(Level.WARNING, "XML_Provincial.doIt", e);
         }
		 
         return xmlStr;
	}

	private String readFormEmployee( Properties ctx , int Parent_Org_ID )
	{
		String msg = "";

		FormEmployeeVect.clear();

		//Gestion de block de 1000 Releve a la fois
		// 2013.02.21 SM Ajout inner join RV_AD_ORG_PARENT et Order by sur le parent
		String sqlForm = "Select P_Form_Employee.*"
			       + " FROM P_Form_Employee"
			       + " INNER JOIN P_Employee on P_Employee.P_Employee_ID = P_Form_Employee.P_Employee_ID "
			       + " INNER JOIN RV_AD_ORG_PARENT on RV_AD_ORG_PARENT.AD_ORG_ID = P_Form_Employee.AD_ORG_ID "
			       + " WHERE P_Form_Employee.P_Year_ID = " + P_Year_ID
			       + " AND P_Form_Employee.P_Form_ID = " + P_Form_ID
			       + " AND P_Form_Employee.FormType = '" + FormType + "'"
			       + " AND RV_AD_ORG_PARENT.Parent_Org_ID = " + Parent_Org_ID
			       ;
			
		if ( this.P_Employee_ID != 0 )
			sqlForm += " AND P_Form_Employee.P_Employee_ID = " + this.P_Employee_ID; 

		sqlForm += " ORDER BY RV_AD_ORG_PARENT.PARENT_ORG_ID, P_Employee.value ";

		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sqlForm);
			while(rs.next() )
			{
				FormEmployeeVect.add( new P_Form_Employee( ctx, rs, null));
			}
	        rs.close();
	        stmt.close();		
	    }
		catch (Exception e)
		{
		    log.log(Level.WARNING, "XML_Provincial.doIt", e);
		}
		
		int nbrFile =  new BigDecimal( FormEmployeeVect.size()).divide( new BigDecimal(1000), 0, BigDecimal.ROUND_UP ).intValue() ;
		
		for ( int z=0; z < nbrFile ; z++)
		{
			StartInformation = z * 1000; 
					
			StringBuffer xmlStr = new StringBuffer( "");
			
			Year = P_Year.get( ctx, P_Year_ID, trxName);
            // 2013.02.21 SM Ajout Order By
			String sql = "Select P_Form_Param.P_Form_Param_ID from P_Form_Param " 
					   + "WHERE P_Form_Param.Type = 'P' "
					   + " AND AD_Org_ID in ( 0, " + Parent_Org_ID + " ) "  
					   + "Order by AD_Org_ID";
		    PreparedStatement psdoc = null;
		    try
		  	{ 
	            psdoc = DB.prepareStatement(sql, null);
	            ResultSet rsdoc = psdoc.executeQuery();
	            while (rsdoc.next())
	            {
	        		FormParam = P_Form_Param.get(ctx, rsdoc.getInt("P_Form_Param_ID"), trxName);
	        		Org = MOrg.get( ctx, FormParam.getAD_Org_ID());
	        		Client = MClient.get( ctx );
	        		xmlStr.append( generateNEQ( ctx,  trxName, rsdoc.isLast() ) );
	            }
		        rsdoc.close();
		        psdoc.close();
		     }
		     catch (Exception e)
		     {
		         log.log(Level.WARNING, "XML_Provincial.doIt", e);
		     }
			
			 String filePath = PgiUtil.getSolsticeParameter(getCtx(), "TransfertPath"); //"C:\\Solstice\\XML";
			 if ( filePath.endsWith( File.separator ) == false )
				 filePath = filePath + File.separator;

			 String FileName =  Form.getValue() + "_" + Year.getYear() + "_" + FormParam.getTransmitterNumber() + "_" + FormTypeXml + "_00" + nbrFile + "_" + FormParam.getNEQ(); 
				
			 String fileName = filePath + FileName + ".xml";

			 log.log(Level.INFO, "Write file :" + fileName );
			 

			 WriteToFile( fileName, xmlStr, z, FormParam.getNEQ() );
	         
	 		 String path = PgiUtil.getSolsticeParameter(getCtx(), "XmlValidationPath"); 
	 		 String schemaFileName = path + "/Provincial/Transmission.xsd";

//	         msg = PgiUtil.validateWithXsd( fileName, schemaFileName );
         	 firstTime = true;
			
		}
		
		return msg;
		
	}

	//
	// Remplacez les caractères spéciaux (<, >, ', " et &) par leur entité XML correspondante (&lt;, &gt;, &apos;, &quot; et &amp;)
	//
	private String nvl( String string )
	{
	    if ( string == null )
	        return "";

	    string = string.replace( "<" , "&lt;");
	    string = string.replace( ">" , "&gt;");
	    string = string.replace( "'" , "&apos;");
	    string = string.replace( "&" , "&amp;");

	    return string;
		
	}

	//
	// Remplacez les caractères spéciaux (<, >, ', " et &) par leur entité XML correspondante (&lt;, &gt;, &apos;, &quot; et &amp;)
	//
	private String nvl( String string, String ret )
	{
	    if ( string == null )
	        return ret;

	    string = string.replace( "<" , "&lt;");
	    string = string.replace( ">" , "&gt;");
	    string = string.replace( "'" , "&apos;");
	    string = string.replace( "&" , "&amp;");

	    return string;
		
	}

	
	protected String doIt() throws Exception 
	{
		String msg = "";
		Properties ctx = Env.getCtx();

		String sql = "Select distinct Parent_Org_ID from RV_AD_ORG_PARENT" ;
		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next() )
			{
				msg = msg + readFormEmployee( ctx, rs.getInt("Parent_Org_ID") );
			}
	        rs.close();
	        stmt.close();		
	    }
		catch (Exception e)
		{
		    log.log(Level.WARNING, "XML_Provincial.doIt", e);
		}
		
		
		return msg;
	}


	private void WriteToFile(String fileName, StringBuffer NewFile, int nbrFile, String NEQ ) 
	{
		File aFile = null;
		aFile = new File( fileName );
		FileOutputStream out; // declare a file output object
		PrintStream p; // declare a print stream object
		try {
			// Create a new file output stream
			out = new FileOutputStream(aFile, false);

			// Connect print stream to the output stream
			p = new PrintStream(out);

			p.println(NewFile.toString().trim());
			// System.out.println(NewFile.toString());
			p.close();
			
			out.close();
		} catch (Exception e) {
			log.log(Level.SEVERE, "Error writing to file :", e);
		}
	}
	
	
	public static void main (String[] args)
	{
		org.compiere.Compiere.startupEnvironment(true);
		
		log.info("----------------------------------");
		new XmlProvincial( true );
		
		log.info("----------------------------------");
	}

}
