package solstice.custom;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.jsp.JspWriter;
import solstice.model.P_ER_Evaluation;

public class ERCreationValidation extends ValidationBase
{

    public ERCreationValidation()
    {
        super();
    }

    /**
     * Validations
     */
    protected String[] doValidate()
    {
        if(request.getParameter("action") != null
                && request.getParameter("action").equals("create"))
        {
            // On doit s'assurer que l'employé a été sélectionné
            if(request.getParameter("employeeId") != null
                    && !request.getParameter("employeeId").equals(""))
            {
                return null;
            }
            return new String[] {"Vous devez d'abord sélectionner un employé"};
        }
        return null;
    }

    public boolean afterValidation(JspWriter out)
    {
        String action = this.request.getParameter("action");
        System.out.println("cIsCopy  : " + this.request.getParameter("cIsCopy"));

        boolean isCopy = "on".equals( this.request.getParameter("cIsCopy") );
        if(action != null)
        {
            if(action.equals("create"))
            {
                // On renvoit l'utilisateur au bon formulaire de création
                String page;
                if(request.getParameter("formType").equals("GES"))
                    page = "evalges.jsp";
                else if(request.getParameter("formType").equals("PTB"))
                    page = "evalptb.jsp";
                else
                    page = "evalout.jsp";

                if ( isCopy )
                {
                    System.out.println("cIsCopy  2 : " + request.getParameter("employeeId") );
                	Properties ctx = new Properties();
            	    P_ER_Evaluation original = P_ER_Evaluation.getLastEvaluation( ctx, Integer.parseInt( request.getParameter("employeeId")) );

                	
                	if ( original != null )
                	{
//                        int gestionnaireId = ((IUserInfo)request.getSession().getAttribute("userInfo")).getEmployeeId();
                    	P_ER_Evaluation Evaluation = P_ER_Evaluation.copy( original, Integer.parseInt( request.getParameter("employeeId")) );
//                        System.out.println("isCopy  3 : " + Evaluation.getID() );
                    	if ( Evaluation != null )
                    		page += "?evaluationId=" + Evaluation.getP_ER_Evaluation_ID();
                    	else
                            page += "?employeeId=" + request.getParameter("employeeId");
                	}
                	else
                	{
                        System.out.println("la copie n'a pas trouvée d'enregistrement. " );
                        page += "?employeeId=" + request.getParameter("employeeId");
                	}
                	
                }
                else
                {
                    page += "?employeeId=" + request.getParameter("employeeId");
                }

                try
                {
                    response.sendRedirect(page);
                }
                catch (IOException e)
                {
                    e.printStackTrace(System.out);
                }
            }
        }
        return true;
    }
    

}
