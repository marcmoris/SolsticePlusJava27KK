package solstice.custom;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.P_ER_Form;
import solstice.model.P_ER_Form_Struct;
import solstice.process.PgiUtil;

public class EvaluationRendementModification extends ValidationBase
{
    
    /**
     * Crée le formulaire principal
     */
    public static Hashtable createMainForm(PageContext pageContext, String formType) throws JspException
    {
        // On doit d'abord s'assurer qu'on a une date de formulaire valide
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        if(request.getParameter("formDate") != null
                && !request.getParameter("formDate").equals("")
                && PgiUtil.stringToDate(request.getParameter("formDate")) != null)
        {
            
            // On vérifie ensuite s'il existe déjà un formulaire passé en attribut
            // de page. Cette condition sera valide lorsque le formulaire reviendra
            // d'un post et aura été récupéré dans le afterValidation
            if(pageContext.getAttribute("formulaire") != null
                    && pageContext.getAttribute("formulaire") instanceof Hashtable)
            {
                return (Hashtable)pageContext.getAttribute("formulaire");
            }
            
            // On doit maintenant créer soit un formulaire à partir de la version la plus
            // récente pour le type de formulaire s'il existe ou sinon à partir de rien.
            int originalFormId;
            if(request.getParameter("modifiedFormId") != null
                    && !request.getParameter("modifiedFormId").equals(""))
            {
                originalFormId = Integer.parseInt(request.getParameter("modifiedFormId"));
            }
            else
            {
                originalFormId = getOrifinalFormId("GES");
            }
            
            if(originalFormId != 0)
            {
                pageContext.setAttribute("originalFormId", new Integer(originalFormId), PageContext.PAGE_SCOPE);
                return buildFromOriginal(originalFormId, request.getParameter("formDate"));
            }
            return buildFromScratch(formType, request.getParameter("formDate"));
        }
        
        throw new JspException("Aucune date de formulaire n'a été définie");
    }
    
    /**
     * Cette méthode retourne le id du formulaire le plus récent pour un type passé
     * en paramètre. S'il n'existe pas de formulaire, on retourne 0
     */
    private static int getOrifinalFormId(String formType) throws JspException
    {
        String sql
        = "select top 1 P_ER_Form_ID"
            + "    from P_ER_Form"
            + "   where FormType = '" + formType + "' "
            + "order by Version desc";
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            int originalFormId = 0;
            
            if(rs.next()) originalFormId = rs.getInt("P_ER_Form_ID");
            
            rs.close();
            stmt.close();
            
            return originalFormId;
        }
        catch (SQLException e)
        {
            throw new JspException(e);
        }
    }
    
    /**
     * Construit le formulaire à partir de rien
     */
    private static Hashtable buildFromScratch(String formType, String formDate) throws JspException
    {
        Hashtable<String, Object> formulaire = new Hashtable<String, Object>();
        
        formulaire.put("formType", formType);
        formulaire.put("originalVersion", "0");
        formulaire.put("formDate", formDate);
        formulaire.put("originalFormId", "");
        
        // La première section modifiable est la section des directives générales.
        formulaire.put("dg_Text", "");
        formulaire.put("consultAct_Description", "http://");
        formulaire.put("addAct_Description", "http://");
        formulaire.put("hyperlien", "http://");
        
        // On a ensuite la section des attentes. Il doit y avoir 5 fois le code, la description et le texte
        Hashtable<String, String>[] expectations = new Hashtable[5];
        for(int i = 0; i < expectations.length; i++)
        {
            expectations[i] = new Hashtable<String, String>();
            expectations[i].put("Name", "");
            expectations[i].put("Description", "");
            expectations[i].put("Text", "");
        }
        formulaire.put("expectations", expectations);
        
        // Les 4 codes de compétences
        Hashtable<String, String>[] compCotes = new Hashtable[4];
        for(int i = 0; i < compCotes.length; i++)
        {
            compCotes[i] = new Hashtable<String, String>();
            compCotes[i].put("Name", "");
            compCotes[i].put("Description", "");
        }
        formulaire.put("compCotes", compCotes);
        
        //
        // La liste des compétences est gérée par le ManipulableStructureTag
        //
        
        
        // Il reste les 5 cotes d'appréciation globale du rendement
        Hashtable<String, String>[] globalApp = new Hashtable[5];
        for(int i = 0; i < globalApp.length; i++)
        {
            globalApp[i] = new Hashtable<String, String>();
            globalApp[i].put("Name", "");
            globalApp[i].put("Description", "");
        }
        formulaire.put("globalApp", globalApp);
        
        return formulaire;
    }
    
    /**
     * Construit le formulaire à partir d'un autre
     */
    private static Hashtable buildFromOriginal(int originalFormId, String formDate) throws JspException
    {
        Hashtable<String, Object> formulaire = new Hashtable<String, Object>();
        int expectation_Count = 0;
        int compCote_Count = 0;
        int globalApp_Count = 0;
        formulaire.put("originalFormId", String.valueOf(originalFormId));
        
        // On doit d'abord récupérer la version et le type du formulaire et les informations nécessaires
        // pour la suite des informations
        String sql;
        sql = " select formType,"
            + "        version,"
            + "        ( select count(1)"
            + "            from P_ER_Form_Struct"
            + "           where fieldType = 'expectation'"
            + "             and P_ER_Form_ID = P_ER_Form.P_ER_Form_ID ) as expectation_Count,"
            + "        ( select count(1)"
            + "            from P_ER_Form_Struct"
            + "           where fieldType = 'compCote'"
            + "             and P_ER_Form_ID = P_ER_Form.P_ER_Form_ID ) as compCote_Count,"
            + "        ( select count(1)"
            + "            from P_ER_Form_Struct"
            + "           where fieldType = 'globalApp'"
            + "             and P_ER_Form_ID = P_ER_Form.P_ER_Form_ID ) as globalApp_Count"
            + "   from P_ER_Form"
            + "  where P_ER_Form_ID = " + originalFormId;
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
                formulaire.put("formType", rs.getString("FormType"));
                formulaire.put("originalVersion", rs.getString("Version"));
                expectation_Count = rs.getInt("expectation_Count");
                compCote_Count = rs.getInt("compCote_Count");
                globalApp_Count = rs.getInt("globalApp_Count");
            }
            else
            {
                throw new JspException("Erreur dans le chargement du formulaire original");
            }
            
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            throw new JspException(e);
        }
        
        formulaire.put("formDate", formDate);
        
        // On charge d'abord les directives générales et les adresses du plan de développement
        sql = " select isnull((select convert(nvarchar(2000), Text)"
            + "                  from P_ER_Form_Struct"
            + "                 where P_ER_Form_ID = " + originalFormId
            + "                   and FieldType = 'dg'), '') as dg_Text,"
            + "        isnull((select convert(nvarchar(2000), Description)"
            + "                  from P_ER_Form_Struct"
            + "                 where P_ER_Form_ID = " + originalFormId
            + "                   and FieldType = 'consultAct'), '') as consultAct_Description,"
            + "        isnull((select convert(nvarchar(2000), Description)"
            + "                  from P_ER_Form_Struct"
            + "                 where P_ER_Form_ID = " + originalFormId
            + "                   and FieldType = 'addAct'), '') as addAct_Description,"
            + "        isnull((select convert(nvarchar(2000), Description)"
            + "                  from P_ER_Form_Struct"
            + "                 where P_ER_Form_ID = " + originalFormId
            + "                   and FieldType = 'hyperlien'), '') as hyperlien";
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
                formulaire.put("dg_Text", rs.getString("dg_Text"));
                formulaire.put("consultAct_Description", rs.getString("consultAct_Description"));
                formulaire.put("addAct_Description", rs.getString("addAct_Description"));
                formulaire.put("hyperlien", rs.getString("hyperlien"));
            }
        }
        catch (SQLException e)
        {
            throw new JspException(e);
        }
        
        // On récupère les attentes
        sql = "   select isnull(Name, '') as Name,"
            + "          isnull(Description, '') as Description,"
            + "          isnull(Text, '') as Text"
            + "     from P_ER_Form_Struct"
            + "    where P_ER_Form_ID = " + originalFormId
            + "      and FieldType = 'expectation'"
            + " order by PNumber";
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            Hashtable<String, String>[] expectations = new Hashtable[expectation_Count];
            int i = 0;
            while(rs.next())
            {
                expectations[i] = new Hashtable<String, String>();
                expectations[i].put("Name", rs.getString("Name"));
                expectations[i].put("Description", rs.getString("Description"));
                expectations[i].put("Text", rs.getString("Text"));
                i++;
            }
            formulaire.put("expectations", expectations);
            
            rs.close();
            stmt.close();
        }
        catch(SQLException e)
        {
            throw new JspException(e);
        }
        
        // Les codes de compétences
        sql = "   select isnull(Name, '') as Name,"
            + "          isnull(Description, '') as Description"
            + "     from P_ER_Form_Struct"
            + "    where P_ER_Form_ID = " + originalFormId
            + "      and FieldType = 'compCote'"
            + " order by PNumber";
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            Hashtable<String, String>[] compCotes = new Hashtable[compCote_Count];
            int i = 0;
            while(rs.next())
            {
                compCotes[i] = new Hashtable<String, String>();
                compCotes[i].put("Name", rs.getString("Name"));
                compCotes[i].put("Description", rs.getString("Description"));
                i++;
            }
            formulaire.put("compCotes", compCotes);
            
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            throw new JspException(e);
        }
        
        // Appréciation globale
        sql = "   select isnull(Name, '') as Name,"
            + "          isnull(Description, '') as Description"
            + "     from P_ER_Form_Struct"
            + "    where P_ER_Form_ID = " + originalFormId
            + "      and FieldType = 'globalApp'"
            + " order by PNumber";
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            Hashtable<String, String>[] globalApp = new Hashtable[globalApp_Count];
            int i = 0;
            while(rs.next())
            {
                globalApp[i] = new Hashtable<String, String>();
                globalApp[i].put("Name", rs.getString("Name"));
                globalApp[i].put("Description", rs.getString("Description"));
                i++;
            }
            formulaire.put("globalApp", globalApp);
            
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            throw new JspException(e);
        }
        
        return formulaire;
    }

    /**
     * Cette méthode retourne une énumération de table de hachage
     * contenant les valeurs initiale pour les compétences
     */
    public static Enumeration initialCompetence(PageContext pageContext) throws JspException
    {
        Vector<Object> competences = new Vector<Object>();
        
        if(pageContext.getAttribute("originalFormId") != null
                && pageContext.getAttribute("originalFormId") instanceof Integer)
        {
            // On récupère les compétences selon celle du formulaire d'origine
            int originalFormId = ((Integer)pageContext.getAttribute("originalFormId")).intValue();
            String sql
            = "       select isnull(Name, '') as Name,"
                + "          isnull(Description, '') as Description"
                + "     from P_ER_Form_Struct"
                + "    where P_ER_Form_ID = " + originalFormId
                + "      and FieldType = 'competence'"
                + " order by PNumber";
            
            try
            {
                PreparedStatement stmt = DB.prepareStatement(sql, null);
                ResultSet rs = stmt.executeQuery();
                
                while(rs.next())
                {
                    Hashtable<String, String> table = new Hashtable<String, String>();
                    table.put("competence_Name", rs.getString("Name"));
                    table.put("competence_Description", rs.getString("Description"));
                    competences.add(table);
                }
                
                rs.close();
                stmt.close();
            }
            catch (SQLException e)
            {
                throw new JspException(e);
            }
        }
        else
        {
            // Il s'agit d'un nouveau formulaire. On initialise donc les
            // compétence en placant un enregistrement vide
            Hashtable<String, String> defaultTable = new Hashtable<String, String>();
            defaultTable.put("competence_Name", "Titre");
            defaultTable.put("competence_Description", "Description");
            competences.add(defaultTable);
        }
        
        return competences.elements();
    }

    public EvaluationRendementModification()
    {
        super();
    }

    protected String[] doValidate()
    {
        return null;
    }

    /**
     * Traitement principal du dernier post
     */
    public boolean afterValidation(JspWriter out)
    {
        String action = this.request.getParameter("action");
        if(action != null)
        {
            if(action.equals("save"))
            {
                // L'utilisateur a choisi de sauvegarder le formulaire. On sait
                // que lors de la sauvegarde d'un formulaire de modification, on
                // doit créer une nouvelle version de celui-ci dans la base de
                // données. On crée donc ce nouvel enregistrement en incrémentant
                // le numéro de version de l'original
                int version = Integer.parseInt(this.request.getParameter("originalVersion")) + 1;
                
                
                P_ER_Form form = new P_ER_Form(Env.getCtx(), -1, null);
                form.setVersion(version);
                form.setFormDate(this.getDate(request.getParameter("formDate")));
                form.setFormType(request.getParameter("formType"));
                
                if(form.save())
                {
                    boolean ok = true;
                    
                    // directives générales
                    {
                        P_ER_Form_Struct formStruct = new P_ER_Form_Struct(Env.getCtx(), -1, null);
                        formStruct.setP_ER_Form_ID(form.getP_ER_Form_ID());
                        formStruct.setPNumber(0);
                        formStruct.setFieldType("dg");
                        formStruct.setText(request.getParameter("dg_Text"));
                        ok = ok && formStruct.save();
                    }
                    
                    {
                        P_ER_Form_Struct formStruct = new P_ER_Form_Struct(Env.getCtx(), -1, null);
                        formStruct.setP_ER_Form_ID(form.getP_ER_Form_ID());
                        formStruct.setPNumber(0);
                        formStruct.setFieldType("consultAct");
                        formStruct.setDescription(request.getParameter("consultAct_Description"));
                        ok = ok && formStruct.save();
                    }
                    
                    {
                        P_ER_Form_Struct formStruct = new P_ER_Form_Struct(Env.getCtx(), -1, null);
                        formStruct.setP_ER_Form_ID(form.getP_ER_Form_ID());
                        formStruct.setPNumber(0);
                        formStruct.setFieldType("addAct");
                        formStruct.setDescription(request.getParameter("addAct_Description"));
                        ok = ok && formStruct.save();
                    }
                    
                    {
                        P_ER_Form_Struct formStruct = new P_ER_Form_Struct(Env.getCtx(), -1, null);
                        formStruct.setP_ER_Form_ID(form.getP_ER_Form_ID());
                        formStruct.setPNumber(0);
                        formStruct.setFieldType("hyperlien");
                        formStruct.setDescription(request.getParameter("hyperlien"));
                        ok = ok && formStruct.save();
                    }
                    
                    // code d'attentes
                    String[] expectation_Names = request.getParameterValues("expectation_Name");
                    String[] expectation_Descriptions = request.getParameterValues("expectation_Description");
                    String[] expectation_Texts = request.getParameterValues("expectation_Text");
                    for(int i = 0; i < expectation_Names.length; i++)
                    {
                        P_ER_Form_Struct formStruct = new P_ER_Form_Struct(Env.getCtx(), -1, null);
                        formStruct.setP_ER_Form_ID(form.getP_ER_Form_ID());
                        formStruct.setPNumber(i);
                        formStruct.setFieldType("expectation");
                        formStruct.setName(expectation_Names[i]);
                        formStruct.setDescription(expectation_Descriptions[i]);
                        formStruct.setText(expectation_Texts[i]);
                        ok = ok && formStruct.save();
                    }
                    
                    // Les 4 codes de compétences
                    String[] compCote_Names = request.getParameterValues("compCote_Name");
                    String[] compCote_Descriptions = request.getParameterValues("compCote_Description");
                    for(int i = 0; i < compCote_Names.length; i++)
                    {
                        P_ER_Form_Struct formStruct = new P_ER_Form_Struct(Env.getCtx(), -1, null);
                        formStruct.setP_ER_Form_ID(form.getP_ER_Form_ID());
                        formStruct.setPNumber(i);
                        formStruct.setFieldType("compCote");
                        formStruct.setName(compCote_Names[i]);
                        formStruct.setDescription(compCote_Descriptions[i]);
                        ok = ok && formStruct.save();
                    }
                    
                    // Les compétences
                    String[] competence_Names = request.getParameterValues("competence_Name");
                    String[] competence_Descriptions = request.getParameterValues("competence_Description");
                    for(int i = 0; i < competence_Names.length; i++)
                    {
                        P_ER_Form_Struct formStruct = new P_ER_Form_Struct(Env.getCtx(), -1, null);
                        formStruct.setP_ER_Form_ID(form.getP_ER_Form_ID());
                        formStruct.setPNumber(i);
                        formStruct.setFieldType("competence");
                        formStruct.setName(competence_Names[i]);
                        System.out.println("Competences trouvés : ");
                        System.out.println(competence_Descriptions[i]);
                        formStruct.setDescription(competence_Descriptions[i]);
                        ok = ok && formStruct.save();
                    }
                    
                    String[] globalApp_Names = request.getParameterValues("globalApp_Name");
                    String[] globalApp_Descriptions = request.getParameterValues("globalApp_Description");
                    for(int i = 0; i < globalApp_Names.length; i++)
                    {
                        P_ER_Form_Struct formStruct = new P_ER_Form_Struct(Env.getCtx(), -1, null);
                        formStruct.setPNumber(i);
                        formStruct.setP_ER_Form_ID(form.getP_ER_Form_ID());
                        formStruct.setFieldType("globalApp");
                        formStruct.setName(globalApp_Names[i]);
                        formStruct.setDescription(globalApp_Descriptions[i]);
                        ok = ok && formStruct.save();
                    }
                    
                    if(!ok)
                    {
                        System.out.println("Erreur dans la sauvegarde d'une structure de formulaire");
                    }
                    
                    // On doit maintenant rediriger l'utilisateur vers le menu principal
                    try
                    {
                        this.response.sendRedirect("index.jsp");
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace(System.out);
                    }
                }
                else
                {
                    System.out.println("Erreur dans la sauvegarde du formulaire");
                }
            }
        }
        
        // On doit récupérer le formulaire posté
        pageContext.setAttribute("formulaire", this.getPostedForm());
        
        return true;
    }

    /**
     * Cette méthode retourne la table de hachage contenant le formulaire ayant été posté
     */
    private Hashtable getPostedForm()
    {
        Hashtable<String, Object> formulaire = new Hashtable<String, Object>();
        
        formulaire.put("formType", request.getParameter("formType"));
        formulaire.put("originalVersion", request.getParameter("originalVersion"));
        formulaire.put("formDate", request.getParameter("formDate"));
        formulaire.put("originalFormId", request.getParameter("originalFormId"));
        
        if(request.getParameter("originalFormId") != null
                && !request.getParameter("originalFormId").equals(""))
        {
            pageContext.setAttribute("originalFormId", new Integer(Integer.parseInt(request.getParameter("originalFormId"))));
        }
        
        // La première section modifiable est la section des directives générales.
        formulaire.put("dg_Text", request.getParameter("dg_Text"));
        formulaire.put("consultAct_Description", request.getParameter("consultAct_Description"));
        formulaire.put("addAct_Description", request.getParameter("addAct_Description"));
        formulaire.put("hyperlien", request.getParameter("hyperlien"));
        
        // On a ensuite la section des attentes. Il doit y avoir 5 fois le code, la description et le texte
        String[] expectation_Names = request.getParameterValues("expectation_Name");
        String[] expectation_Descriptions = request.getParameterValues("expectation_Description");
        String[] expectation_Texts = request.getParameterValues("expectation_Text");
        Hashtable<String, String>[] expectations = new Hashtable[expectation_Names.length];
        for(int i = 0; i < expectations.length; i++)
        {
            expectations[i] = new Hashtable<String,String>();
            expectations[i].put("Name", expectation_Names[i]);
            expectations[i].put("Description", expectation_Descriptions[i]);
            expectations[i].put("Text", expectation_Texts[i]);
        }
        formulaire.put("expectations", expectations);
        
        // Les 4 codes de compétences
        String[] compCote_Names = request.getParameterValues("compCote_Name");
        String[] compCote_Descriptions = request.getParameterValues("compCote_Description");
        Hashtable<String, String>[] compCotes = new Hashtable[compCote_Names.length];
        for(int i = 0; i < compCotes.length; i++)
        {
            compCotes[i] = new Hashtable<String, String>();
            compCotes[i].put("Name", compCote_Names[i]);
            compCotes[i].put("Description", compCote_Descriptions[i]);
        }
        formulaire.put("compCotes", compCotes);
        
        //
        // La liste des compétences est gérée par le ManipulableStructureTag
        //
        
        
        // Il reste les 5 cotes d'appréciation globale du rendement
        String[] globalApp_Names = request.getParameterValues("globalApp_Name");
        String[] globalApp_Descriptions = request.getParameterValues("globalApp_Description");
        Hashtable<String, String>[] globalApp = new Hashtable[globalApp_Names.length];
        for(int i = 0; i < globalApp.length; i++)
        {
            globalApp[i] = new Hashtable<String, String>();
            globalApp[i].put("Name", globalApp_Names[i]);
            globalApp[i].put("Description", globalApp_Descriptions[i]);
        }
        formulaire.put("globalApp", globalApp);
        
        return formulaire;
    }
    
    private Timestamp getDate(String date)
    {
        try
        {
            Calendar calendar = PgiUtil.stringToDate(date);
            return new Timestamp(calendar.getTimeInMillis());
        }
        catch (Exception e)
        {
            System.out.println("Erreur dans la conversion vers un Timestamp. Valeur d'entrée : " + date);
            return null;
        }
    }
    
}
