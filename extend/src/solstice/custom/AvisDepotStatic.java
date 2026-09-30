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
package solstice.custom;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.P_Period;
import solstice.utils.PgiUtil;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AvisDepotStatic
{
	
	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (AvisDepotStatic.class);

    /**
     * On doit formatter le texte dans un comboBoxTag pour afficher correctement
     * les dates des périodes.
     */
    public static String formatPeriodComboBox(ResultSet rs) throws SQLException
    {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        return rs.getString("Name") + " - " + "Du " + f.format(rs.getTimestamp("StartDate")) + " au " + f.format(rs.getTimestamp("EndDate"));
    }

    public static String formatComboBox(ResultSet rs) throws SQLException
    {
        return rs.getString("Name");
    }

    
    /**
     * Dans le formulaire, on doit afficher les banques en ordre alphabétique
     * en séparant au milieu du count par un tableau de gauche et un tableau 
     * de droite. dans le code jsp, on retrouvera donc une dynamicStructure
     * qui countera le nombre d'enregistrements et le placera dans un vecteur
     * nommé bankCount. Ensuite, on retrouvera une autre dynamicStructure qui
     * sortira les information relatives aux banques dans l'ordre alphabétique.
     * Finalement, on utilisera cette méthode pour savoir à quel moment il faudra
     * ajouter la séparation dans la page.
     */
    public static boolean isMiddle(PageContext pageContext)
    {
    	if ( pageContext.getAttribute("bankCount") == null)
    		return false;
    	
        Vector<?> v = (Vector<?>)pageContext.getAttribute("bankCount");
        int count = ((Integer)((DictionaryEntry)v.elementAt(0)).getValue()).intValue();
        count = (int)Math.ceil(count / 2.0d);
        if(pageContext.getAttribute("bc") == null)
        {
            pageContext.setAttribute("bc", new Integer(2));
            if(count == 1)
                return true;
            return false;
        }
        int val = ((Integer)pageContext.getAttribute("bc")).intValue();
        pageContext.setAttribute("bc", new Integer(val + 1));
        if(val == count)
            return true;
        return false;
    }
    
    /**
     * On vérifie si l'utilisateur est administrateur.
     */
//    public static boolean isAdmin(PageContext pageContext)
//    {
//        int roleId = ((Integer)pageContext.getSession().getAttribute("roleId")).intValue();
//        boolean admin = false;
//        String sql
//        = "select 1"
//            + " from AD_Role"
//            + " where AD_Role_ID = " + roleId
//            + " and Name like '%(ADMI)%'";
//        
//        try
//        {
//            PreparedStatement stmt = DB.prepareStatement(sql, null);
//            ResultSet rs = stmt.executeQuery();
//            
//            admin = rs.next();
//            
//            rs.close();
//            stmt.close();
//        }
//        catch (SQLException e)
//        {
//            e.printStackTrace(System.out);
//        }
//        
//        return admin;
//    }

    
    public static boolean isSoquij(PageContext pageContext)
    {
        String client = PgiUtil.getSolsticeParameter(Env.getCtx(),"Client").toUpperCase();
        if ( client.equals("SOQUIJ") )
        	return true;
        
        return false;
    }

    public static boolean isSIQ(PageContext pageContext)
    {
        String client = PgiUtil.getSolsticeParameter(Env.getCtx(),"Client").toUpperCase();
        if ( client.equals("SIQ") )
        	return true;
        
        return false;
    }

    public static boolean isChl(PageContext pageContext)
    {
        String client = PgiUtil.getSolsticeParameter(Env.getCtx(),"Client").toUpperCase();
        if ( client.equals("CHL") )
        	return true;
        
        return false;
    }


    public static boolean isAcrualBankExist(PageContext pageContext)
    {
        String sql = "select 1 "
		           + " from P_Statement_Earning_Credits "
		           + " where P_Statement_Earning_ID = " + (String)pageContext.getAttribute("Statement_Earning_ID")
		;

        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            boolean existing = rs.next();
            
            rs.close();
            stmt.close();
            
            return existing;
        }
        catch(SQLException e)
        {
           e.printStackTrace(System.out);
        }

        return false;

    }

    private static BigDecimal getTaxationCreditbase( int P_Employee_ID, Timestamp PayDate)
    {
    	BigDecimal ProvincialTaxationCredit = Env.ZERO;
    	BigDecimal FederalTaxationCredit = Env.ZERO;
    	
        try
        {

        	String sql = "select top 1 taxFederal.Amount FederalTaxationCredit"
                + "   from P_Tax_Federal_TD1 taxFederal"
                + "   where taxFederal.Value = 'TD1.01' "
                + "   and taxFederal.P_Tax_ID = ("
                + "     select top 1 P_Tax_ID"
                + "     from P_Tax"
                + "     where EffectIn <= " + DB.TO_DATE(PayDate)
                + "     order by EffectIn desc )";
                
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            if ( rs.next() )
    	        FederalTaxationCredit = rs.getBigDecimal("FederalTaxationCredit");
            rs.close();
            stmt.close();
        }
        catch (Exception e)
        {
        	s_log.log(Level.SEVERE,"CreateStatementEarning ",e);
        }

        try
        {
            String sql = "select top 1 taxProvincialTD1.Amount ProvincialTaxationCredit"
    	        + "   from P_Tax_Provincial_TD1 taxProvincialTD1 inner join P_Tax_Provincial taxProvincial"
    	        + "   on taxProvincialTD1.P_Tax_Provincial_ID = taxProvincial.P_Tax_Provincial_ID"
    	        + "   where taxProvincialTD1.Value = 'TP1015.01' "
    	        + "   and taxProvincial.C_Region_ID = (select top 1 Taxation_Region_ID from P_Employee where P_Employee_ID = " + P_Employee_ID + ")"
    	        + "   and taxProvincial.P_Tax_ID = ("
    	        + "     select top 1 P_Tax_ID"
    	        + "     from P_Tax"
    	        + "     where EffectIn <= " + DB.TO_DATE(PayDate)
    	        + "     order by EffectIn desc )" ;

                
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            if ( rs.next() )
            	ProvincialTaxationCredit = rs.getBigDecimal("ProvincialTaxationCredit");
            rs.close();
            stmt.close();
        }
        catch (Exception e)
        {
        	s_log.log(Level.SEVERE,"CreateStatementEarning ",e);
        }
        
        if ( FederalTaxationCredit == null)
        	FederalTaxationCredit = Env.ZERO;

        if ( ProvincialTaxationCredit == null)
        	ProvincialTaxationCredit = Env.ZERO;

        return ProvincialTaxationCredit.add( FederalTaxationCredit );
    }
    	

    public static boolean isTaxationCreditExist(PageContext pageContext, boolean admin )
    {
    	int employeeId = 0 ;
    	if ( admin )
    		if ( (String)pageContext.getAttribute("employeeId") != null)
    			employeeId = Integer.parseInt((String)pageContext.getAttribute("employeeId"));
    	else
    	{
            HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
            solstice.custom.IUserInfo userInfo = (solstice.custom.IUserInfo)request.getSession().getAttribute("userInfo");
    		employeeId = userInfo.getEmployeeId();
    	}
        
        int P_Period_ID = Integer.parseInt((String)pageContext.getAttribute("periodId"));

        P_Period Period = P_Period.get( Env.getCtx(), P_Period_ID, null);
        
    	boolean existing = false;
    	
        String sql = "select isnull( FederalTaxationCredit,0) + isnull( ProvincialTaxationCredit, 0 ) "
		           + " from P_Statement_Earning "
		           + " where P_Statement_Earning_ID = " + (String)pageContext.getAttribute("Statement_Earning_ID")
		;

        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            if ( rs.next()  && rs.getBigDecimal(1).compareTo( Env.ZERO) != 0 ) //  && rs.getBigDecimal(1).compareTo( getTaxationCreditbase( employeeId , Period.getPayDate() ) ) != 0 )
            	existing = true;
            
            rs.close();
            stmt.close();
            
            return existing;
        }
        catch(SQLException e)
        {
           e.printStackTrace(System.out);
        }

        return false;
    	
    }

    public static boolean isTaxationCreditExist(PageContext pageContext)
    {
    	return isTaxationCreditExist(pageContext, false) ;
    }

    public static boolean isTaxationCreditExistAdmin(PageContext pageContext)
    {
    	return isTaxationCreditExist(pageContext, true) ;
    }
    	
    
    /**
     * On vérifie s'il existe un avis de dépôt pour l'employé et la période
     * passée en paramètre
     */
    public static boolean existsAvis(PageContext pageContext)
    {
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        int employeeId;
        String periodId;
        periodId = (String)pageContext.getAttribute("periodId");
        System.out.println( "Avis Static Period : " + periodId);
        
//        if((employeeId = request.getParameter("employeeId")) != null
        solstice.custom.IUserInfo userInfo = (solstice.custom.IUserInfo)request.getSession().getAttribute("userInfo");

        employeeId = userInfo.getEmployeeId();
//        if ((periodId = request.getParameter("periodId")) != null)
        {
            String sql
            = "select 1"
                + " from P_Statement_Earning"
                + " where P_Employee_ID = " + employeeId
                + " and P_Period_ID = " + periodId
                + " and PrePrintedNo like 'D%' " ;
            
            try
            {
                PreparedStatement stmt = DB.prepareStatement(sql, null);
                ResultSet rs = stmt.executeQuery();
                
                boolean existing = rs.next();
                
                rs.close();
                stmt.close();
                
                return existing;
            }
            catch(SQLException e)
            {
               e.printStackTrace(System.out);
            }
        }
        return false;
    }

    /**
     * On vérifie s'il existe plusiers avis de dépôt pour l'employé et la période
     * passée en paramètre
     */
    public static boolean existsMultipleAvis(PageContext pageContext)
    {
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        int employeeId;
        String periodId;
        periodId = (String)pageContext.getAttribute("periodId");
        
//        if((employeeId = request.getParameter("employeeId")) != null
        solstice.custom.IUserInfo userInfo = (solstice.custom.IUserInfo)request.getSession().getAttribute("userInfo");

        employeeId = userInfo.getEmployeeId();
//        if ((periodId = request.getParameter("periodId")) != null)
        {
            String sql
            = "select count(*)"
                + " from P_Statement_Earning"
                + " where P_Employee_ID = " + employeeId
                + " and P_Period_ID = " + periodId
                + " and PrePrintedNo like 'D%' " ;
            
            try
            {
                PreparedStatement stmt = DB.prepareStatement(sql, null);
                ResultSet rs = stmt.executeQuery();
                
                boolean existing = false;
                if (rs.next() )
                  if ( rs.getInt(1) > 1 )
                	  existing = true;
                
                rs.close();
                stmt.close();
                
                return existing;
            }
            catch(SQLException e)
            {
               e.printStackTrace(System.out);
            }
        }
        return false;
    }

    
    /**
     * On vérifie s'il existe plusiers avis de dépôt pour l'employé et la période
     * passée en paramètre
     */
    public static boolean existsMultipleAvisAdmin(PageContext pageContext)
    {
        String employeeId;
        String periodId;
        periodId = (String)pageContext.getAttribute("periodId");
        
        employeeId = (String)pageContext.getAttribute("employeeId");
        {
            String sql
            = "select count(*)"
                + " from P_Statement_Earning"
                + " where P_Employee_ID = " + employeeId
                + " and P_Period_ID = " + periodId
                + " and PrePrintedNo like 'D%' " ;
            
            try
            {
                PreparedStatement stmt = DB.prepareStatement(sql, null);
                ResultSet rs = stmt.executeQuery();
                
                boolean existing = false;
                if (rs.next() )
                  if ( rs.getInt(1) > 1 )
                	  existing = true;
                
                rs.close();
                stmt.close();
                
                return existing;
            }
            catch(SQLException e)
            {
               e.printStackTrace(System.out);
            }
        }
        return false;
    }

    /**
     * On vérifie s'il existe un avis de dépôt pour l'employé et la période
     * passée en paramètre
     */
    public static boolean existsAvisAdmin(PageContext pageContext)
    {
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        String employeeId;
        String periodId;
        periodId = (String)pageContext.getAttribute("periodId");
//        employeeId = (String)pageContext.getAttribute("employeeId");

        solstice.custom.IUserInfo userInfo = (solstice.custom.IUserInfo)request.getSession().getAttribute("userInfo");

        if( ! userInfo.hasPolicy("avis_allemployee"))
        	return false;

        if((employeeId = request.getParameter("employeeId")) != null ) 
        {
            String sql
            = "select 1"
                + " from P_Statement_Earning"
                + " where P_Employee_ID = " + employeeId
                + " and P_Period_ID = " + periodId
                + " and PrePrintedNo like 'D%' " ;
            
            try
            {
                PreparedStatement stmt = DB.prepareStatement(sql, null);
                ResultSet rs = stmt.executeQuery();
                
                boolean existing = rs.next();
                
                rs.close();
                stmt.close();
                
                return existing;
            }
            catch(SQLException e)
            {
               e.printStackTrace(System.out);
            }
        }
        return false;
    }

    /**
     * Formatage des montants
     */
    public static String formatMontant(Object value)
    {
        if(value != null)
        {
            double montant = value.getClass().equals(BigDecimal.class) ? ((BigDecimal)value).doubleValue() : ((Double)value).doubleValue();
            if (montant == 0)
            {
            	return "&nbsp";
            }
            else
            {
	            DecimalFormat format = new DecimalFormat("#0.00");
	            return format.format(montant).replace('.', ',');
            }
        }
        return "&nbsp";
    }
    
    /**
     * Formattage sur 4 décimales
     */
    public static String formatHourly_Rate(Object value)
    {
        if(value != null && value instanceof Number)
        {
            double rate = ((Number)value).doubleValue();
            DecimalFormat decimalFormat = new DecimalFormat("#0.0000");
            return decimalFormat.format(rate);
        }
        return "";
    }
    
    /**
     * On formate le nombre sur deux chiffres
     */
    public static String formatDate2(Object value)
    {
        try
        {
            if(value == null)
                return "&nbsp;";
	        double dvalue = ((Number)value).doubleValue();
	        DecimalFormat format = new DecimalFormat("00");
	        return format.format(dvalue % 100);
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
            return "";
        }
    }
    
}
