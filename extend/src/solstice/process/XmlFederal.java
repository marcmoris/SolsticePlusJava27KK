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
import java.util.Hashtable;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MClient;
import org.compiere.model.MOrg;
import org.compiere.model.MCountry;
import org.compiere.model.MLocation;
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


public class XmlFederal extends SvrProcess  {

	/** Logger */
	private static CLogger log = CLogger.getCLogger(XmlFederal.class);
	//
	
	private int P_Year_ID = 1000094;
	private int P_Form_ID = 1000002;
	private int P_Employee_ID = 0;
	private int P_Employer_ID = 0;
	private String trxName = this.get_TrxName();
	private String FormType = "O";
	
    private int T4SlipPosition = 0;
	private String m_Format = "PDF";

	public XmlFederal( )
	{
	}
	
	public XmlFederal( boolean standalone )
	{	   
		try
		{ 
			doIt();
        }
        catch (Exception e)
        {
            log.log(Level.WARNING, "XML_Federal", e);
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
  	        else if (paramName.equals("FormType"))
  	        	this.FormType = (String)para[i].getParameter();
  	        else if (paramName.equals("P_Employee_ID"))
  	        	this.P_Employee_ID = para[i].getParameterAsInt();
  	        else if (paramName.equals("P_Employer_ID"))
  	        	this.P_Employer_ID = para[i].getParameterAsInt();
            else if (paramName.equals("CrystalFormat"))
				m_Format = (String)para[i].getParameter();
	  	}
  	
	}

	private StringBuffer generateVariableInformationDetail( Properties ctx, int P_Form_Employee_ID, String trxName, boolean IsOtherInformation )
	{
		StringBuffer result = new StringBuffer("");
		
		String OtherInformation = "'N'";
		if ( IsOtherInformation )
			OtherInformation = "'Y'";
		
		String sql = "Select P_FORM_EMPLOYEE_DETAIL.P_FORM_EMPLOYEE_DETAIL_ID, XmlTag"
			       + " FROM P_FORM_EMPLOYEE_DETAIL  "
			       + "  INNER JOIN P_FORM_FIELD ON P_FORM_FIELD.P_FORM_FIELD_ID = P_FORM_EMPLOYEE_DETAIL.P_FORM_FIELD_ID "
			       + " WHERE P_FORM_EMPLOYEE_DETAIL.P_FORM_EMPLOYEE_ID = " + P_Form_Employee_ID
			       + " AND isnull( P_Form_Field.XmlTag, '') <> '' "
			       + " AND IsOtherInformation = " + OtherInformation
			       + " AND P_FORM_FIELD.FormFieldType in (  '4'  , '9' ) "
			       + " Order BY P_Form_Field.Value  "
			       ;
//		sql = MRole.getDefault().addAccessSQL (sql, "P_Form_Employee", true, false);	// fully qualidfied - RO 
		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next() )
			{
				String xmlTag = rs.getString("xmlTag");
				P_Form_Employee_Detail Detail = P_Form_Employee_Detail.get( ctx, rs.getInt("P_Form_Employee_Detail_ID"), trxName );
				
				if ( PgiUtil.nvl(Detail.getFormFieldAmount()).compareTo(Env.ZERO) != 0 || PgiUtil.nvl( Detail.getFormFieldText()).trim().length() >= 0 )
				{
					if ( xmlTag.equals( "<rpp_dpsp_rgst_nbr>" ) && Detail.getFormFieldText() == null)
					{
						// pas de tag ajouté.
					}
					else if ( xmlTag.equals("<tot_empr_eip_amt>") ||  xmlTag.equals("<tot_empr_cpp_amt>") )
					{
						// Noting. on ajout uniquement le total au tableau s_Total
						BigDecimal amt = Env.ZERO;
						if ( s_Total.containsKey( xmlTag ) )
						{
							amt = (BigDecimal)s_Total.get( xmlTag );
							s_Total.remove( xmlTag );
						}
						s_Total.put( xmlTag , amt.add( Detail.getFormFieldAmount() ) );
					}
					else
					{
						result.append( xmlTag );
						if ( Detail.getFormFieldAmount() != null && Detail.getFormFieldAmount().compareTo(Env.ZERO) != 0)
							result.append( String.valueOf( Detail.getFormFieldAmount()) );
						else 
							if ( Detail.getFormFieldText() != null )
							{
								if ( xmlTag.equals( "<cpp_qpp_xmpt_cd>" ) && Detail.getFormFieldText() != null)
									result = result.append("1");
								else if ( xmlTag.equals( "<ei_xmpt_cd>" ) && Detail.getFormFieldText() != null )
									result = result.append("1");
								else if ( xmlTag.equals( "<prov_pip_xmpt_cd>" ) && Detail.getFormFieldText() != null)
									result = result.append("1");
								else
									result.append( Detail.getFormFieldText() );
							}

						
						if ( xmlTag.equals( "<cpp_qpp_xmpt_cd>" ) && Detail.getFormFieldText() == null)
							result = result.append("0");
						if ( xmlTag.equals( "<ei_xmpt_cd>" ) && Detail.getFormFieldText() == null )
							result = result.append("0");
						if ( xmlTag.equals( "<prov_pip_xmpt_cd>" ) && Detail.getFormFieldText() == null)
							result = result.append("0");
			
							
//						if ( xmlTag.equals( "<empt_cd>" ))
//							result = "0";
						result.append( xmlTag.replace( "<", "</") ).append( "\r\n");
						
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

	private StringBuffer generateAmountDetail( Properties ctx, int P_Form_Employee_ID, String trxName, boolean IsOtherInformation )
	{
		StringBuffer result = new StringBuffer("");
		
		String OtherInformation = "'N'";
		if ( IsOtherInformation )
			OtherInformation = "'Y'";
		
		String sql = "Select P_FORM_EMPLOYEE_DETAIL.P_FORM_EMPLOYEE_DETAIL_ID, XmlTag"
			       + " FROM P_FORM_EMPLOYEE_DETAIL  "
			       + "  INNER JOIN P_FORM_FIELD ON P_FORM_FIELD.P_FORM_FIELD_ID = P_FORM_EMPLOYEE_DETAIL.P_FORM_FIELD_ID "
			       + " WHERE P_FORM_EMPLOYEE_DETAIL.P_FORM_EMPLOYEE_ID = " + P_Form_Employee_ID
			       + " AND isnull( P_Form_Field.XmlTag, '') <> '' "
			       + " AND IsOtherInformation = " + OtherInformation
			       + " AND P_FORM_FIELD.FormFieldType not in (  '4'  , '9' ) "
			       + " AND isnull( FormFieldAmount, 0) <> 0 "
			       + " Order BY P_Form_Field.Value  "
			       ;
//		sql = MRole.getDefault().addAccessSQL (sql, "P_Form_Employee", true, false);	// fully qualidfied - RO 
		boolean found = false;

		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next() )
			{
				String xmlTag = rs.getString("xmlTag");
				P_Form_Employee_Detail Detail = P_Form_Employee_Detail.get( ctx, rs.getInt("P_Form_Employee_Detail_ID"), trxName );
				
				if ( Detail.getFormFieldText() != null || PgiUtil.nvl( Detail.getFormFieldAmount()).compareTo( Env.ZERO) != 0 )
				{
					found = true;

					if ( xmlTag.equals("<tot_empr_eip_amt>") ||  xmlTag.equals("<tot_empr_cpp_amt>") )
					{
						// Noting. on ajout uniquement le total au tableau s_Total
					}
					else
					{
						result.append( xmlTag );
						if ( Detail.getFormFieldAmount() != null && Detail.getFormFieldAmount().compareTo(Env.ZERO) != 0  )
							result.append( String.valueOf( Detail.getFormFieldAmount()).replace( ".", ",") );
						
						result.append( xmlTag.replace( "<", "</") );
						
					}
					
					if ( ! rs.isLast() )
						result.append( "\r\n");
					
					BigDecimal amt = Env.ZERO;
					if ( s_Total.containsKey( xmlTag ) )
					{
						amt = (BigDecimal)s_Total.get( xmlTag );
						s_Total.remove( xmlTag );
					}
					s_Total.put( xmlTag , amt.add( Detail.getFormFieldAmount() ) );

				}
				
			}
			rs.close();
			stmt.close();

		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		if ( found )
			result = new StringBuffer("\r\n").append( result );

		return result;
	}

	int NbReleves = 0;
	
	private P_Year Year;

	private P_Employee Employee;
	private P_Employer EmployerForm;
	private P_Form_Employee FormEmployee;
	private MLocation EmployeeLocation;
	private MRegion EmployeeRegion;
	private MCountry EmployeeCountry;

		
//	private void generateEmployeeDetail( Properties ctx, String FormType, ResultSet rsdoc, String trxName )
	private int generateEmployeeDetail( Properties ctx, String FormType, int i, String trxName )
	{
		
		String sql = "Select P_FORM_EMPLOYEE.P_FORM_EMPLOYEE_ID"
			       + " FROM P_Form_Employee"
			       + " INNER JOIN P_Employee on P_Employee.P_Employee_ID = P_Form_Employee.P_Employee_ID "
			       + " WHERE P_Form_Employee.P_Year_ID = " + P_Year_ID
			       + " AND P_Form_Employee.P_Form_ID = " + P_Form_ID
			       + " AND P_Form_Employee.FormType = '" + FormType + "'"   
			       + " AND P_Form_Employee.P_Employer_ID = " + EmployerInfo.getP_Employer_ID();
		
		if ( FormParam.getAD_Org_ID() != 0 )
		      sql += " AND ( P_Form_Employee.AD_ORG_ID IN ( select AD_ORG_ID from AD_ORGINFO where Parent_Org_ID = " + FormParam.getAD_Org_ID() + " ) "
		           + "  OR  P_Form_Employee.AD_ORG_ID IN ( 0," + FormParam.getAD_Org_ID() + "))"
  		          ;

		if ( this.P_Employee_ID != 0 )
			sql += " AND P_Form_Employee.P_Employee_ID = " + this.P_Employee_ID; 
			
		sql += " ORDER BY P_Employee.value ";
//		sql = MRole.getDefault().addAccessSQL (sql, "P_Form_Employee", true, false);	// fully qualidfied - RO

		
		String XmlInfo = "";
		StringBuffer result = new StringBuffer("");
		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			TotalRecordEmployer = 0 ;
			boolean formExists = false;
			
			while(rs.next())
			{
				formExists = true;
				TotalRecordEmployer = TotalRecordEmployer + 1;
				NbReleves = NbReleves + 1;
				FormEmployee = P_Form_Employee.get(ctx, rs.getInt( "P_Form_Employee_ID" ), trxName);
				Employee  = P_Employee.get( ctx, FormEmployee.getP_Employee_ID(), trxName);
				EmployeeLocation = MLocation.get(ctx, Employee.getC_Location_ID(), trxName);
				EmployeeRegion = MRegion.get( ctx, EmployeeLocation.getC_Region_ID());
				EmployeeCountry = MCountry.get( ctx, EmployeeRegion.getC_Country_ID()); 
				EmployerForm = P_Employer.get( ctx, FormEmployee.getP_Employer_ID(), trxName);
				
	            log.log(Level.INFO, "   Employee : " + Employee.getValue() + " " + Employee.getName());

				XmlInfo = "";
				i = T4SlipPosition ;
//				rsdoc.absolute( T4SlipPosition -1 );
            	for (; ((String)P_Form_Xml.get(i)).equals("</T4Slip>") == false ; i++) 

//	            while (rsdoc.next() && rsdoc.getString( "XmlInfo").trim().equals("</T4Slip>") == false )
	            {
	            	result = new StringBuffer("");
	            	
	            	XmlInfo = ((String)P_Form_Xml.get(i));
//	            	XmlInfo = rsdoc.getString( "XmlInfo");
        			if ( XmlInfo.startsWith("</") == false )
        				XmlInfo = parseXmlInfo( XmlInfo );
	    			
	            	if ( XmlInfo != null)
	            	{
		    			if (XmlInfo.equals("<T4_AMT>"))
		    			{
		    				result.append(generateVariableInformationDetail( ctx, FormEmployee.getP_Form_Employee_ID(), trxName, false ) );
		    				result.append( XmlInfo );
		    		        result.append( generateAmountDetail( ctx, FormEmployee.getP_Form_Employee_ID(), trxName, false ) );
		    			}
		    			else if (XmlInfo.equals("<OTH_INFO>"))
		    			{
		    				result.append( XmlInfo );
		    		        result.append( generateAmountDetail( ctx, FormEmployee.getP_Form_Employee_ID(), trxName, true ) );
		    			}
		    			else 
		    				result.append( XmlInfo );
	            	}
	            	if ( result.length() != 0)
	            		p.println(result);
	            }
            	//2012.02.24
            	XmlInfo = ((String)P_Form_Xml.get(i));

				FormEmployee.setIsGenerated( true );
				FormEmployee.save();
				
				if ( ((String)P_Form_Xml.get(i)).equals("</T4Slip>") )
					p.println(((String)P_Form_Xml.get(i)).trim());
			}

			if (formExists) 
			  i = i + 1;
			
//2012.02.24
//			p.println(result);
			result = new StringBuffer( "");

			rs.close();
			stmt.close();
			
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}

		return i;
		
	}
/*
	private StringBuffer generateTotal( Properties ctx, String trxName )
	{
		StringBuffer result = new StringBuffer("<T>").append( "\r\n");
		result.append("<Annee>" + Year.getYear() + "</Annee>").append( "\r\n");
		result.append("<NbReleves>" + String.valueOf( NbReleves ) + "</NbReleves>").append( "\r\n");
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
			result.append("<Nom1>" + EmployerInfo.getName() + "</Nom1>").append( "\r\n");
			//		result.append("<Nom2></Nom2>").append( "\r\n");
		}	
		if ( Form.getValue().trim().equals("R2"))
		{
			result.append("<Nom>" + EmployerInfo.getName() + "</Nom>").append( "\r\n");
			//		result.append("<Nom2></Nom2>").append( "\r\n");
		}	
		result.append("<Adresse>").append( "\r\n");
		if  ( PgiUtil.nvl( EmployerInfoLocation.getAddress1(), " ").length() > 30 )
			result.append("<Ligne1>" + EmployerInfoLocation.getAddress1().substring(0,29) + "</Ligne1>").append( "\r\n");
		else
			result.append("<Ligne1>" + EmployerInfoLocation.getAddress1() + "</Ligne1>").append( "\r\n");
		result.append("<Ligne2>" + PgiUtil.nvl( EmployerInfoLocation.getAddress2(), " ") + "</Ligne2>").append( "\r\n");
		result.append("<Ville>"  + EmployerInfoLocation.getCity() + "</Ville>").append( "\r\n");
		result.append("<Province>" + PgiUtil.nvl(EmployerInfoRegion.getName()) +"</Province>").append( "\r\n");
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
	*/
	private P_Employer EmployerInfo;
	private P_Form_Param FormParam;
	private MLocation EmployerInfoLocation;
	private MRegion EmployerInfoRegion;
	private MCountry EmployerInfoCountry;
	private P_Form Form;
	private Hashtable<String, BigDecimal> 	s_Total = new Hashtable<String, BigDecimal>();
	
	private Hashtable<Integer, String> 	P_Form_Xml = new Hashtable<Integer, String>();
	
	private int TotalRecordEmployer = 0;
	
	private String parseXmlInfo( String xmlInfo )
	{
		String xmlTagStart = "";
		String xmlTagEnd = "";
		String info = "";
		String ret = " ";
		if ( xmlInfo.contains("</"))
		{
			xmlTagStart = xmlInfo.substring(0, xmlInfo.indexOf('>')+1);
			xmlTagEnd = "<" + xmlInfo.substring(xmlInfo.indexOf('/'), xmlInfo.length() );
			info = xmlInfo.replace(xmlTagStart, "").replace( xmlTagEnd, "");
            
            if ( info.equals("Submission reference identification" ))
        		ret = String.valueOf( FormParam.getSubmissionNumber() );
     		if ( info.equals("Report type code" ))
        		ret = FormType;

     		if ( info.equals("Report Type Code" ))
        		ret = FormType;

     		if ( info.equals("Transmitter number" ))
        		ret = FormParam.getTransmitterNumber();
       		if ( info.equals("Transmitter type indicator" ))
        		ret = "1";

       		if ( info.equals("Total number of summary records" ))
        		ret = String.valueOf( countNbrOfRecord( ) );

       		if ( info.equals("Language of communication indicator" ))
        		ret = FormParam.getCommunicationPreferred();
       		if ( info.equals("Transmitter name - line 1" ))
       		{
       			if ( EmployerInfo.getAD_Org_ID() != 0)
       			{
           			MOrg Org = MOrg.get( Env.getCtx(), EmployerInfo.getAD_Org_ID() );
           			if ( Org.getDescription()  != null )
           				ret = ( Org.getDescription() + "                              ").substring(0,29).trim();   // Utilise la decscription pour avoir le nom en Anglais
           			else
           				ret = ( Org.getName()  + "                              ").substring(0,29).trim();   // Utilise la decscription pour avoir le nom en Anglais
       			}
       			else
       			{
       				MClient Client = MClient.get( Env.getCtx());
       				if  ( Client.getDescription() != null)
       					ret = (Client.getDescription() + "                              ").substring(0,29).trim();
       				else
       					ret = (Client.getName() + "                              ").substring(0,29).trim();
       			}
       				
//       			
       		}
       		if ( info.equals("Transmitter name - line 2" ))
        		ret = " ";
       		if ( info.equals("Transmitter address - line 1" ))
        		ret = EmployerInfoLocation.getAddress1();
       		if ( info.equals("Transmitter address - line 2" ))
       			ret = PgiUtil.nvl( EmployerInfoLocation.getAddress2() ).trim();
       		if ( info.equals("Transmitter city" ))
       			ret = EmployerInfoLocation.getCity();
       		if ( info.equals("Transmitter province or territory code" ))
       			ret = EmployerInfoRegion.getName();
       		if ( info.equals("Transmitter country code" ))
        		ret = EmployerInfoCountry.getCountryCode_ISO3166();
       		if ( info.equals("Transmitter postal code" ))
        		ret = EmployerInfoLocation.getPostal().replace( " ", "");
       		if ( info.equals("Contact name" ))
       		{
       			P_Employee TechResource = P_Employee.get( Env.getCtx(), FormParam.getP_Employee_ID(), trxName);
        		ret = TechResource.getName();
       		}
       		if ( info.equals("Contact area code" ))
        		ret = FormParam.getPhoneAreaCodeContactPerson();
       		if ( info.equals("Contact telephone number" ))
        		ret = FormParam.getPhoneNumberContactPerson();
       		if ( info.equals("Contact extension number" ))
        		ret = FormParam.getPhoneExtensionContactPerson();
       		if ( info.equals("Contact email" ))
        		ret = FormParam.getEMail();
       		if ( info.equals("Contact email address" ))
        		ret = FormParam.getEMail();
       		
       		if ( Employee != null)
       		{
           		if ( info.equals("Employee surname" ))
           		{
           			if ( Employee.getSurname().trim().length() > 20)
           				ret = Employee.getSurname().trim().substring(0,19);
           			else	
           				ret = Employee.getSurname().trim();
           			
           		}
           		if ( info.equals("Employee first name" ))
           		{
           			if ( Employee.getFirstName().trim().length() > 12)
           				ret = Employee.getFirstName().trim().substring(0,11);
           			else
           				ret = Employee.getFirstName().trim();
           			
           		}
           		if ( info.equals("Employee initial" ))
           		{
           			if ( Employee.getFirstLetter() != null)
                		ret = Employee.getFirstLetter().substring(0,1);
           			else
           				ret = "";
           		}

           		if ( info.equals("Employee social insurance number" ))
            		ret = Employee.getSin().replace( "-", "");;
           		if ( info.equals("Employee number" ))
            		ret = Employee.getValue().trim();

       		}
       		if ( EmployeeLocation != null )
       		{
           		if ( info.equals("Employee address - line 1" ))
           		{
           			if ( EmployeeLocation.getAddress1().trim().length() > 30)
           				ret = EmployeeLocation.getAddress1().trim().substring(0,29);
           			else ret = EmployeeLocation.getAddress1().trim();
           			
           		}
           		if ( info.equals("Employee address - line 2" ) )
           		{
/*           			if ( EmployeeLocation.getAddress1().trim().length() > 30)
           			{
           				ret = PgiUtil.nvl( EmployeeLocation.getAddress1()).trim().substring(30) + " " + PgiUtil.nvl(EmployeeLocation.getAddress2()).trim().substring(0,29);
           			}
               		else 
*/               		
               		if ( PgiUtil.nvl(EmployeeLocation.getAddress2()).trim().length() > 30 )
               			ret = PgiUtil.nvl(EmployeeLocation.getAddress2()).trim().substring(0,29);
               		else
               			ret = PgiUtil.nvl(EmployeeLocation.getAddress2()).trim();
               			
           		}
           		if ( info.equals("Employee city" ))
           		{
           			if ( EmployeeLocation.getCity().length() > 28)
           				ret = EmployeeLocation.getCity().substring(0,27);
           			else ret = EmployeeLocation.getCity();
           		}
           		if ( info.equals("Employee province or territory code" ))
           			ret = EmployeeRegion.getName();
           		if ( info.equals("Employee country code" ))
            		ret = EmployeeCountry.getCountryCode_ISO3166();
           		if ( info.equals("Employee postal code" ))
            		ret = EmployeeLocation.getPostal().replace( " ", "");

       		}
       		
       		if (  EmployerForm != null)
       		{

           		if ( info.equals("Employer name - line 1" ))
           		{
           			if ( EmployerInfo.getAD_Org_ID() != 0 )
           			{
               			MOrg Org = MOrg.get( Env.getCtx(), EmployerInfo.getAD_Org_ID() );
               			if ( Org.getDescription()  != null )
               				ret = (Org.getDescription() + "                              ").substring(0,29).trim();   // Utilise la decscription pour avoir le nom en Anglais
               			else
               				ret = (Org.getName() + "                              ").substring(0,29).trim();   // Utilise la decscription pour avoir le nom en Anglais
           			}
           			else
           			{
               			MClient Client = MClient.get( Env.getCtx());
           				if  ( Client.getDescription() != null)
           					ret = (Client.getDescription() + "                              ").substring(0,29).trim();
           				else
           					ret = (Client.getName() + "                              ").substring(0,29).trim();
           			}
           		}
           		if ( info.equals("Employer name - line 2" ))
       			{
           			if (PgiUtil.getSolsticeParameter(Env.getCtx(), "Client").equals("CHL"))
           			{
               			MClient Client = MClient.get( Env.getCtx());
               			ret = Client.getDescription();
           			}
       			}

           		if ( info.equals("Payroll Account Number" ))
            		ret = EmployerForm.getValue();

       		}

       		if ( info.equals("Employer name - line 1" ))
       		{
       			if ( EmployerInfo.getAD_Org_ID() != 0)
       			{
           			MOrg Org = MOrg.get( Env.getCtx(), EmployerInfo.getAD_Org_ID() );
           			if ( Org.getDescription()  != null )
           				ret = (Org.getDescription() + "                              ").substring(0,29).trim();   // Utilise la decscription pour avoir le nom en Anglais
           			else
           				ret = (Org.getName() + "                              ").substring(0,29).trim();   // Utilise la decscription pour avoir le nom en Anglais
       			}
       			else
       			{
       				MClient Client = MClient.get( Env.getCtx());
       				if  ( Client.getDescription() != null)
       					ret = (Client.getDescription() + "                              ").substring(0,29).trim();
       				else
       					ret = (Client.getName() + "                              ").substring(0,29).trim();
       			}

//           			ret = "";
           		if ( info.equals("Employer name - line 3" ))
            		ret = "";
           		
           		if ( info.equals("Employer address - line 1" ))
            		ret = EmployerInfoLocation.getAddress1();
           		if ( info.equals("Employer address - line 2" ))
            		ret = PgiUtil.nvl(EmployerInfoLocation.getAddress2()).trim();
           		if ( info.equals("Employer city" ))
           			ret = EmployerInfoLocation.getCity();
           		if ( info.equals("Employer province or territory code" ))
            		ret = PgiUtil.nvl(EmployerInfoRegion.getName());
           		if ( info.equals("Employer country code" ))
            		ret = EmployerInfoCountry.getCountryCode_ISO3166();
           		if ( info.equals("Employer postal code" ))
            		ret = EmployerInfoLocation.getPostal().replace(" ", "").replace("-", "");
       			
       		}
       			
       		if ( info.equals("Contact area code" ))
        		ret = FormParam.getPhoneAreaCodeContactPerson();
       		if ( info.equals("Contact telephone number" ))
        		ret = FormParam.getPhoneNumberContactPerson();
       		if ( info.equals("Contact email" ))
        		ret = FormParam.getEMail();
      		if ( info.equals("Contact email address" ))
        		ret = FormParam.getEMail();
       		if ( info.equals("Contact extension" ))
        		ret = FormParam.getPhoneExtensionContactPerson();
       		if ( info.equals("Taxation year" ))
        		ret = String.valueOf( Year.getYear() );

       		/*
       		if ( info.equals("Canada Pension Plan exempt code" ))
        		ret = "0"; 
       		if ( info.equals("Employment Insurance exempt code" ))
        		ret = "0";  
       		if ( info.equals("PPIP exempt code" ))
        		ret = "0"; 
       		if ( info.equals("Employment code" ))
        		ret = "0"; 
       		if ( info.equals("Report Type Code" ))
        		ret = "0"; 
*/

/*        			
   			<cpp_qpp_xmpt_cd>Canada Pension Plan exempt code</cpp_qpp_xmpt_cd>
       		<ei_xmpt_cd>Employment Insurance exempt code</ei_xmpt_cd>
       		<prov_pip_xmpt_cd>PPIP exempt code</prov_pip_xmpt_cd>
       		<empt_cd>Employment code</empt_cd>
       		<rpt_tcd>Report Type Code</rpt_tcd>
  */     		
       		if ( info.equals("Total number of T4 slip records" ) )
       		    ret = String.valueOf( TotalRecordEmployer );
       		
       		if ( xmlTagStart.startsWith("<tot_"))
       		{
           		ret = String.valueOf( (BigDecimal)s_Total.get( xmlTagStart.replace("<tot_", "<") ) ).replace( ".", ",");
           		if ( ret.equals("null"))
           			ret = "0,00";
       		}
       		if ( xmlTagStart.startsWith("<tot_empe_cpp_amt>"))
       		{
           		ret = String.valueOf( (BigDecimal)s_Total.get( "<cpp_cntrb_amt>" ));
           		if ( ret.equals("null"))
           			ret = "0,00";
       		}

       		//2013-12-13 Ajout des totals employeurs
       		if ( xmlTagStart.startsWith("<tot_empr_cpp_amt>"))
       		{
           		ret = String.valueOf( (BigDecimal)s_Total.get( "<tot_empr_cpp_amt>" ));
           		if ( ret.equals("null"))
           			ret = "0,00";
       		}

       		//2013-12-13 Ajout des totals employeurs
       		if ( xmlTagStart.startsWith("<tot_empr_eip_amt>"))
       		{
           		ret = String.valueOf( (BigDecimal)s_Total.get( "<tot_empr_eip_amt>" ));
           		if ( ret.equals("null"))
           			ret = "0,00";
       		}

       		
       		if ( ret != null &&  ret.trim().length() != 0)	
       			return xmlTagStart + ret + xmlTagEnd;

       		return null;
       		/*

             */

		}
		
		return xmlInfo;
	}
	
	private Boolean firstTime = true;
	
	private void generateFederal( Properties ctx, int P_Employer_ID, String trxName )
	{
		
		StringBuffer xmlStr = new StringBuffer( "");

//		P_Employee TechResource = P_Employee.get( ctx, FormParam.getP_Employee_ID(), trxName);
//		P_Employee AccountingResource = P_Employee.get( ctx, FormParam.getP_Employee_Accounting_ID(), trxName);
		
		EmployerInfo = P_Employer.get( ctx, P_Employer_ID, trxName);
		
        log.log(Level.INFO, "Employer : " + EmployerInfo.getValue() + " " + EmployerInfo.getName());

		EmployerInfoLocation = MLocation.get( ctx, EmployerInfo.getC_Location_ID(), trxName);
		EmployerInfoRegion = MRegion.get( ctx, EmployerInfoLocation.getC_Region_ID());
		EmployerInfoCountry = MCountry.get( ctx, EmployerInfoRegion.getC_Country_ID());
		int EndT619 = 0;
		String sql = "Select * from P_Form_XML Where P_Form_ID = " + P_Form_ID + " and isActive = 'Y' Order By [LineNo] ";
	    PreparedStatement psdoc = null;
	    try
	  	{ 
            psdoc = DB.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, null);
            ResultSet rsdoc = psdoc.executeQuery();
            int i =0;
            while (rsdoc.next())
            {
				P_Form_Xml.put( i , rsdoc.getString( "XmlInfo") );
				if ( ((String)P_Form_Xml.get(i)).equals("</T619>") )
					EndT619 = i;
				i = i + 1;
            }
            rsdoc.close();
            psdoc.close();
	  	}	
	    catch (Exception e)
	    {
	    	log.log(Level.WARNING, "XML_Federal.generateFederal", e);
	    }

/*	    
		String sql = "Select * from P_Form_XML Where P_Form_ID = " + P_Form_ID + " and isActive = 'Y' Order By [LineNo] ";
	    PreparedStatement psdoc = null;
	    try
	  	{ 
            psdoc = DB.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, null);
            ResultSet rsdoc = psdoc.executeQuery();
*/            
            String XmlInfo = "";

            int i = 0;
            if ( ! firstTime )
            {
            	i = EndT619 + 1;
            }
/*
 *          if ( ! firstTime )
 *             	for (i = 0; ((String)P_Form_Xml.get(i)).equals("</T619>") == false ; i++) 
//                while (rsdoc.next() && rsdoc.getString( "XmlInfo").equals("</T619>") == false )
                {
                	// Skip the T619 information
                }
            }
*/          
            firstTime = false;
//            while (rsdoc.next())
           	for ( ; P_Form_Xml.containsKey( i ) && ((String)P_Form_Xml.get(i)).equals("</Submission>") == false ; i++) 
            {

//           		XmlInfo = rsdoc.getString( "XmlInfo");
           		XmlInfo = (String)P_Form_Xml.get(i);
        		if (XmlInfo.equals("<T4Slip>"))
        		{
//        			T4SlipPosition = rsdoc.getRow();
        			T4SlipPosition = i;
        			
//        			generateEmployeeDetail( ctx, FormType, rsdoc, trxName);
        			i = generateEmployeeDetail( ctx, FormType, i, trxName);
               		XmlInfo = (String)P_Form_Xml.get(i);
//                	XmlInfo = rsdoc.getString( "XmlInfo");
    				xmlStr.append( XmlInfo );
    				
        		}
        		else if (XmlInfo.equals("<T4Summary>"))
        		{
     				xmlStr.append( XmlInfo );
//        			xmlStr.append( generateTotal( ctx, trxName ) );
        		}
        		else
        		{
        			if ( XmlInfo.startsWith("</"))
        				xmlStr.append( XmlInfo );
        			else
        			{
            			XmlInfo = parseXmlInfo( XmlInfo );
            			if ( XmlInfo != null)
            				xmlStr.append( XmlInfo );
        			}
        			
        		}

				// Write in the file
        		if ( xmlStr != null && xmlStr.length() != 0)
        			p.println(xmlStr);
				xmlStr = new StringBuffer( "");
            	
        		
            }
  /*          rsdoc.close();
            psdoc.close();
         }
         catch (Exception e)
         {
             log.log(Level.WARNING, "XML_Federal.doIt", e);
         }
*/		 
	}
	
	private File aFile;
	private FileOutputStream out; // declare a file output object
	private PrintStream p; // declare a print stream object

	private int countNbrOfRecord()
	{
		int nbrRec = 0;
		String sql = "Select Count(*)"
		       + " FROM P_Form_Employee"
		       + " WHERE P_Form_Employee.P_Year_ID = " + P_Year_ID
		       + " AND P_Form_Employee.P_Form_ID = " + P_Form_ID
		       + " AND P_Form_Employee.FormType = '" + FormType + "'"
		       ;
//		       + " AND P_Form_Employee.P_Employer_ID = " + EmployerInfo.getP_Employer_ID();
	
//		if ( FormParam.getAD_Org_ID() != 0 )
//	      sql += " AND ( P_Form_Employee.AD_ORG_ID IN ( select AD_ORG_ID from AD_ORGINFO where Parent_Org_ID = " + FormParam.getAD_Org_ID() + " ) "
//	           + "  OR  P_Form_Employee.AD_ORG_ID IN ( 0," + FormParam.getAD_Org_ID() + "))"
//	          ;
//	sql = MRole.getDefault().addAccessSQL (sql, "P_Form_Employee", true, false);	// fully qualidfied - RO

	    PreparedStatement ps = null;
	    try
	  	{ 
            ps = DB.prepareStatement(sql, null);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
            {
            	nbrRec = rs.getInt( 1 );
            }
	        rs.close();
	        ps.close();
        
	     }
	     catch (Exception e)
	     {
	         log.log(Level.WARNING, "XMLFederal.Count", e);
	     }

	     return nbrRec;
	}
	
	protected String doIt() throws Exception 
	{
		String fileName = null;
		Properties ctx = Env.getCtx();
//		Env.setContext( ctx, "#AD_Client_ID", 11);
//		Env.setContext( ctx, "#AD_Org_ID", 11);

		
		Form = P_Form.get( ctx, P_Form_ID, trxName );

//		String filePath = "C:\\Solstice\\XML";
		String filePath = PgiUtil.getSolsticeParameter(getCtx(), "TransfertPath");
		if ( filePath.endsWith( File.separator ) == false )
			filePath = filePath + File.separator;

//		filePath = "D:\\PGI\\Solstice\\Xml\\";
		
		try {
			// Create a new file output stream

			Year = P_Year.get( ctx, P_Year_ID, trxName);

    		FormParam = P_Form_Param.get(ctx, "F", trxName);

    		FormParam.setSubmissionNumber( FormParam.getSubmissionNumber() + 1);
    		FormParam.save();
    		
    		String FileName =  Form.getValue() + "_" + FormParam.getSubmissionNumber() + "_" + Year.getYear(); 
//    		String FileName =  Form.getValue(); 
    		
    		fileName = filePath + FileName + ".xml";
    		log.log(Level.INFO, "Write file :" + fileName );
    		aFile = null;
    		aFile = new File( fileName );
			out = new FileOutputStream(aFile, false);
			// Connect print stream to the output stream
			p = new PrintStream(out);
			StringBuffer xmlStr = new StringBuffer( "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
			p.println(xmlStr);
			xmlStr = new StringBuffer( "");

			String sql = "Select P_Form_Param.P_Form_Param_ID, P_Employer.P_Employer_ID "
				       + " from P_Form_Param" 
				       + " Inner join P_Employer on P_Employer.Type = 'F' and P_Employer.IsActive = 'Y' "
		       		   + " WHERE P_Form_Param.Type = 'F' "
				       + " AND P_Form_Param.ISACTIVE = 'Y' "
		       		   ;

			if ( P_Employer_ID != 0 )
			       sql += " AND P_Employer.P_Employer_ID = " + P_Employer_ID;
					
			//Sélection les employeurs uniquements s'il y a des formulaires employés pour eux.
			sql += " AND EXISTS( Select 1 FROM P_FORM_EMPLOYEE " 
				       + " WHERE P_Form_Employee.P_Year_ID = " + P_Year_ID
				       + " AND P_Form_Employee.P_Form_ID = " + P_Form_ID
				       + " AND P_Form_Employee.FormType = '" + FormType + "'"   
					   + " AND P_FORM_EMPLOYEE.P_Employer_ID = P_Employer.P_Employer_ID  ) "
				;

		    sql += " order by P_Form_Param.P_Form_Param_ID, P_Employer.P_Employer_ID"		       ;
		       		   
//				       + " AND P_Employer.P_Employer_ID <= 1000044" 
				       ;
		    PreparedStatement psdoc = null;
		    try
		  	{ 
	            psdoc = DB.prepareStatement(sql, null);
	            ResultSet rsdoc = psdoc.executeQuery();
	            while (rsdoc.next())
	            {
					s_Total.clear();
	
	        		FormParam = P_Form_Param.get(ctx, rsdoc.getInt("P_Form_Param_ID"), trxName);
	        		generateFederal( ctx, rsdoc.getInt("P_Employer_ID" ), trxName ) ;
	            }
		        rsdoc.close();
		        psdoc.close();
		        xmlStr.append("</Submission>");
				// Write in the file
				p.println(xmlStr);
				xmlStr = new StringBuffer( "");
		        
		     }
		     catch (Exception e)
		     {
		         log.log(Level.WARNING, "XMLFederal.doIt", e);
		     }
		
			// System.out.println(NewFile.toString());
			p.close();
		} catch (Exception e) {
			log.log(Level.SEVERE, "Error writing to file :", e);
		}

//		AddDateParameters();

//		int l_Role = Env.getAD_Role_ID(Env.getCtx());
        //ReportLauncher.callReport( this.getAD_PInstance_ID(), l_Role, m_Format, getParameter() );

		String msg = "";
		if ( fileName != null)
		{
			String path = PgiUtil.getSolsticeParameter(getCtx(), "XmlValidationPath"); 
			String schemaFileName = path + "/federal/layout-topologie.xsd";

	        msg = PgiUtil.validateWithXsd( fileName, schemaFileName );
		}
		
		return msg;
	}

	
	public static void main (String[] args)
	{
		org.compiere.Compiere.startupEnvironment(true);
		
		log.info("----------------------------------");
		new XmlFederal( true );
		
		log.info("----------------------------------");
	}

	
}