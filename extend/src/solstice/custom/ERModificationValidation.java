package solstice.custom;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.jsp.JspWriter;

import org.compiere.util.DB;

import solstice.process.PgiUtil;

public class ERModificationValidation extends ValidationBase
{

    public ERModificationValidation()
    {
        super();
    }

    /**
     * Validation : On doit s'assurer que l'utilisateur a saisi une date et sélectionné un type de formulaire
     */
    protected String[] doValidate()
    {
        if(this.request.getParameter("action") != null
                && this.request.getParameter("action").equals("modification"))
        {
            if(this.request.getParameter("formType") == null
                    || this.request.getParameter("formType").equals("")
                    || this.request.getParameter("formDate") == null
                    || this.request.getParameter("formDate").equals(""))
            {
                return new String[] {"Vous devez obligatoirement sélectionner un type de formulaire et saisir une date d'entrée en vigueur"};
            }
            
            // On doit maitenant vérifier qu'il n'y a pas d'employé qui utilise
            // le formulaire à modifier. 
            if(this.request.getParameter("formId") != null
                    && !this.request.getParameter("formId").equals(""))
            {
                Calendar calendar = PgiUtil.stringToDate(this.request.getParameter("formDate"));
                String sql;
                sql = " select 1"
                    + "   from P_ER_Evaluation,"
                    + "        P_ER_Form"
                    + "  where P_ER_Form.P_ER_Form_ID = " + this.request.getParameter("formId")
                    + "    and P_ER_Evaluation.P_ER_Form_ID = P_ER_Form.P_ER_Form_ID"
                    + "    and P_ER_Form.FormDate = " + DB.TO_DATE(new Timestamp(calendar.getTimeInMillis()));
                
                try
                {
                    PreparedStatement stmt = DB.prepareStatement(sql, null);
                    ResultSet rs = stmt.executeQuery();
                    
                    String erreur = null;
                    if(rs.next())
                    {
                        erreur = "Vous ne pouvez pas modifier ce formulaire pour la même date car il existe déjà une évaluation pour celui-ci";
                    }
                    
                    rs.close();
                    stmt.close();
                    
                    if(erreur != null) return new String[] {erreur};
                }
                catch (SQLException e)
                {
                    e.printStackTrace(System.out);
                    return new String[] {e.getMessage()};
                }
            }
        }
        return null;
    }

    /**
     * On renvoit l'utilisateur au bon formulaire de modification
     */
    public boolean afterValidation(JspWriter out)
    {
        if(this.request.getParameter("action") != null
                && this.request.getParameter("action").equals("modification"))
        {
            String formType = this.request.getParameter("formType");
            String page;
            if(formType.equals("GES"))
                page = "modifges.jsp";
            else if(formType.equals("PTB"))
                page = "modifptb.jsp";
            else
                page = "modifout.jsp";
            
            String paramFormId = "";
            if(this.request.getParameter("modifiedFormDate") != null
                    && this.request.getParameter("modifiedFormDate").equals(this.request.getParameter("formDate")))
            {
                paramFormId = "&modifiedFormId=" + this.request.getParameter("formId");
            }
            
            try
            {
                response.sendRedirect(page + "?formDate=" + request.getParameter("formDate") + paramFormId);
            }
            catch (IOException e)
            {
                e.printStackTrace(System.out);
            }
            
            return false;
        }
        return true;
    }

}
