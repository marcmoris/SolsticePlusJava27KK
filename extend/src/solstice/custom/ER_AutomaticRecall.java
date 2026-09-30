package solstice.custom;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.compiere.model.MProcess;
import org.compiere.model.MProcessPara;
import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.P_ER_DistributionNoRecall;
import solstice.model.P_ER_Parameter;
import solstice.model.P_Web_Parameters;

public class ER_AutomaticRecall extends ValidationBase
{

    public ER_AutomaticRecall()
    {
        super();
    }

    protected String[] doValidate() throws JspException
    {
        return null;
    }

    /**
     * Traitement du post
     */
    public boolean afterValidation(JspWriter out) throws JspException
    {
        String action = this.request.getParameter("action");
        if("enregistrer".equals(action))
        {
            // L'utilisateur a choisi d'enregistrer les paramètres. Puisqu'il ne peut
            // y avoir qu'un seul enregistrement pour les paramètres dans la base de
            // données, on va d'abord vider la table avant d'enregistrer ce qui a été
            // saisi par l'utilisateur.
            DB.executeUpdate("delete from P_ER_Parameter", null);
            
            P_ER_Parameter parameter = new P_ER_Parameter(Env.getCtx(), -1, null);
            parameter.setDayOfMonth(Integer.parseInt(this.request.getParameter("dayOfMonth")));
            parameter.setP_Assignment_State_ID(Integer.parseInt(this.request.getParameter("assignmentState")));
            parameter.setMinimumDuration(Integer.parseInt(this.request.getParameter("minimumDuration")));
            parameter.setNewHeader(this.request.getParameter("newHeader"));
            parameter.setNewFooter(this.request.getParameter("newFooter"));
            parameter.setNewCol1(this.request.getParameter("newCol1"));
            parameter.setNewCol2(this.request.getParameter("newCol2"));
            parameter.setTransfertHeader(this.request.getParameter("transfertHeader"));
            parameter.setTransfertFooter(this.request.getParameter("transfertFooter"));
            parameter.setTransfertCol1(this.request.getParameter("transfertCol1"));
            parameter.setTransfertCol2(this.request.getParameter("transfertCol2"));
            parameter.setTransfertCol3(this.request.getParameter("transfertCol3"));
            parameter.setNoticeHeader(this.request.getParameter("noticeHeader"));
            parameter.setNoticeFooter(this.request.getParameter("noticeFooter"));
            parameter.setNoticeCol1(this.request.getParameter("noticeCol1"));
            parameter.setNoticeCol2(this.request.getParameter("noticeCol2"));
            parameter.setNoticeCol3(this.request.getParameter("noticeCol3"));
            parameter.setNoticeCol4(this.request.getParameter("noticeCol4"));
            parameter.setRecallHeader(this.request.getParameter("recallHeader"));
            parameter.setRecallFooter(this.request.getParameter("recallFooter"));
            parameter.setRecallCol1(this.request.getParameter("recallCol1"));
            parameter.setRecallCol2(this.request.getParameter("recallCol2"));
            parameter.setRecallCol3(this.request.getParameter("recallCol3"));
            parameter.setRecallCol4(this.request.getParameter("recallCol4"));
            
            if(parameter.save())
                System.out.println("Enregistrement des paramètres réussi");
            else
                System.out.println("Enregistrement des paramètres échoué");
            
            // On enregistre maintenant les codes de distribution de livret à
            // exclure du rappel automatique
            String[] values = this.request.getParameterValues("distributionL");
            if(values != null)
            {
                String sql = "";
                for(int i = 0; i < values.length; i++)
                {
                    // On vérifie si cette valeur a été modifiée
                    if(values[i].charAt(0) == '1')
                    {
                        String id = values[i].substring(1, values[i].indexOf("|"));
                        sql += "delete from P_ER_DistributionNoRecall where P_Distribution_Booklet_ID = " + id + "; ";
                    }
                }
                
                // On effectue la mise à jour
                if(!sql.equals(""))
                {
                    DB.executeUpdate(sql, null);
                }
            }
            
            // On récupère maitenant la liste des distributions qui n'ont pas accès
            values = this.request.getParameterValues("distributionR");
            if(values != null)
            {
                for(int i = 0; i < values.length; i++)
                {
                    // On vérifie si cette valeur a été modifiée
                    if(values[i].charAt(0) == '1')
                    {
                        int id = Integer.parseInt(values[i].substring(1, values[i].indexOf("|")));
                        
                        P_ER_DistributionNoRecall obj = new P_ER_DistributionNoRecall(Env.getCtx(), -1, null);
                        obj.setP_Distribution_Booklet_ID(id);
                        obj.save();
                    }
                }
            }

            // Il reste maintenant à enregistrer le nom des rapports avec leur paramètre
            // pour imprimer les évaluations du rendement.
            String processes[] = {"GES", "OUV", "PTB"};
            for(int i = 0; i < processes.length; i++)
            {
                MProcess workerProcess = getWorkerProcess(processes[i]);
                workerProcess.setValue(request.getParameter("workerProcess" + processes[i]));
                if(!workerProcess.save())
                {
                    System.out.println("erreur dans l'enregistrement du workerProcess" + processes[i]);
                    continue;
                }
                
                MProcessPara workerProcessPara = getWorkerProcessPara(workerProcess);
                workerProcessPara.setColumnName(request.getParameter("workerProcessPara" + processes[i]));
                if(!workerProcessPara.save())
                    System.out.println("erreur dans l'enregistrement du workerProcessPara" + processes[i]);
            }
            
            // On enregistre maitenant les conventions collectives qui ont droit à une double
            // évaluation probatoire.
            DB.executeUpdate("delete from P_Web_Parameters where Parameter = 'COLLECT_LABOUR_AGR_DOUBLE_PROBATION'", null);
            values = this.request.getParameterValues("collectiveLabourR");
            if(values != null)
            {
                for(int i = 0; i < values.length; i++)
                {
                    int id = Integer.parseInt(values[i].substring(1, values[i].indexOf("|")));
                    P_Web_Parameters obj = new P_Web_Parameters(Env.getCtx(), -1, null);
                    obj.setParameter("COLLECT_LABOUR_AGR_DOUBLE_PROBATION");
                    obj.setValue(String.valueOf(id));
                    obj.save();
                }
            }
            
            
            try
            {
                this.response.sendRedirect("index.jsp");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return true;
    }
    
    /**
     * Charge ou crée un workerProcess pour le type demandé
     */
    private MProcess getWorkerProcess(String type) throws JspException
    {
        int workerProcessId = -1;
        
        try
        {
            // On tente d'abord de récupérer l'id du processus à partir
            // de la base de donnée.
            PreparedStatement stmt = DB.prepareStatement(
                    "select AD_Process_ID" +
                    "  from AD_Process" +
                    " where Name = 'WorkerProcess" + type + "'", null);
            
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
                workerProcessId = rs.getInt("AD_Process_ID");
            
            rs.close();
            stmt.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            throw new JspException(e);
        }
        
        // Si on a trouvé un id, on charge ce processus
        if(workerProcessId != -1)
            return MProcess.get(Env.getCtx(), workerProcessId);
        
        // On n'a pas trouvé de process, on va donc en créer un nouveau
        MProcess workerProcess = new MProcess(Env.getCtx(), -1, null);
        workerProcess.setName("WorkerProcess" + type);
        workerProcess.setAccessLevel("3");
        workerProcess.setEntityType("U");
        return workerProcess;
    }

    /**
     * Charge ou crée le workerProcessPara associé au workerProcess passé en paramètre
     */
    private MProcessPara getWorkerProcessPara(MProcess workerProcess) throws JspException
    {
        int workerProcessParaId = -1;
        
        try
        {
            // On tente d'abord de retrouver le paramètre à partir
            // de la base de données et du workerProcess
            PreparedStatement stmt = DB.prepareStatement(
                    "select AD_Process_Para_ID" +
                    "  from AD_Process_Para" +
                    " where AD_Process_ID = " + workerProcess.getAD_Process_ID() +
                    " order by SeqNo", null);
            
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
                workerProcessParaId = rs.getInt("AD_Process_Para_ID");
            
            rs.close();
            stmt.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            throw new JspException(e);
        }
        
        // Si on a trouvé un id, on retourne ce paramètre
        if(workerProcessParaId != -1)
            return new MProcessPara(Env.getCtx(), workerProcessParaId, null);
        
        // On n'a pas trouvé de paramètre pour ce processus alors on en crée un nouveau
        MProcessPara workerProcessPara = new MProcessPara(Env.getCtx(), -1, null);
        workerProcessPara.setAD_Process_ID(workerProcess.getAD_Process_ID());
        workerProcessPara.setName("P_ER_Evaluation_ID");
        workerProcessPara.setSeqNo(0);
        workerProcessPara.setAD_Reference_ID(13);
        workerProcessPara.setFieldLength(0);
        workerProcessPara.setEntityType("U");
        return workerProcessPara;
    }
    
    /**
     * Cette méthode retourne la liste des codes de distribution de livret
     * à inclure dans le rappel automatique.
     */
    public static Enumeration getDistributionBookletToInclude(PageContext pageContext)
    {
        Vector list = new Vector();
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(
                    "select P_Distribution_Booklet.P_Distribution_Booklet_ID," +
                    "       P_Distribution_Booklet.Value," +
                    "       P_Distribution_Booklet.Name" +
                    "  from P_Distribution_Booklet" +
                    "       left join P_ER_DistributionNoRecall" +
                    "              on P_ER_DistributionNoRecall.P_Distribution_Booklet_ID = P_Distribution_Booklet.P_Distribution_Booklet_ID" +
                    " where P_ER_DistributionNoRecall.P_ER_DistributionNoRecall_ID is null", null);
            
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next())
            {
                list.add(SwitchTag.newEntryInstance(
                        rs.getString("P_Distribution_Booklet_ID"),
                        rs.getString("Value") + " - " + rs.getString("Name")));
            }
            
            rs.close();
            stmt.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return list.elements();
    }
    
    /**
     * Cette méthode retourne la liste des codes de distribution de livret
     * à exclure dans le rappel automatique.
     */
    public static Enumeration getDistributionBookletToExclude(PageContext pageContext)
    {
        Vector list = new Vector();
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(
                    "select P_Distribution_Booklet.P_Distribution_Booklet_ID," +
                    "       P_Distribution_Booklet.Value," +
                    "       P_Distribution_Booklet.Name" +
                    "  from P_Distribution_Booklet" +
                    "       left join P_ER_DistributionNoRecall" +
                    "              on P_ER_DistributionNoRecall.P_Distribution_Booklet_ID = P_Distribution_Booklet.P_Distribution_Booklet_ID" +
                    " where P_ER_DistributionNoRecall.P_ER_DistributionNoRecall_ID is not null", null);
            
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next())
            {
                list.add(SwitchTag.newEntryInstance(
                        rs.getString("P_Distribution_Booklet_ID"),
                        rs.getString("Value") + " - " + rs.getString("Name")));
            }
            
            rs.close();
            stmt.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return list.elements();
    }
    
    /**
     * Cette méthode retourne une énumération des conventions collectives qui n'ont
     * droit qu'à une seule évaluation probatoire.
     */
    public static Enumeration getCollectiveLabourSingleER(PageContext pageContext)
    {
        Vector list = new Vector();
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(
                    "select P_Collective_Labour_Agr.P_Collective_Labour_Agr_ID," +
                    "       P_Collective_Labour_Agr.Value," +
                    "       P_Collective_Labour_Agr.Name" +
                    "  from P_Collective_Labour_Agr" +
                    " where not exists ( select 1" +
                    "                      from P_Web_Parameters" +
                    "                     where Parameter = 'COLLECT_LABOUR_AGR_DOUBLE_PROBATION'" +
                    "                       and Value = convert( nvarchar(10), P_Collective_Labour_Agr.P_Collective_Labour_Agr_ID ) )", null);
            
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next())
            {
                list.add(SwitchTag.newEntryInstance(
                        rs.getString("P_Collective_Labour_Agr_ID"),
                        rs.getString("Value") + " - " + rs.getString("Name")));
            }
            
            rs.close();
            stmt.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return list.elements();
    }
    
    /**
     * Cette méthode retourne une énumération des conventions collectives qui ont
     * droit à deux évaluations probatoires.
     */
    public static Enumeration getCollectiveLabourDoubleER(PageContext pageContext)
    {
        Vector list = new Vector();
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(
                    "select P_Collective_Labour_Agr.P_Collective_Labour_Agr_ID," +
                    "       P_Collective_Labour_Agr.Value," +
                    "       P_Collective_Labour_Agr.Name" +
                    "  from P_Collective_Labour_Agr" +
                    " where exists ( select 1" +
                    "                  from P_Web_Parameters" +
                    "                 where Parameter = 'COLLECT_LABOUR_AGR_DOUBLE_PROBATION'" +
                    "                   and Value = convert( nvarchar(10), P_Collective_Labour_Agr.P_Collective_Labour_Agr_ID ) )", null);
            
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next())
            {
                list.add(SwitchTag.newEntryInstance(
                        rs.getString("P_Collective_Labour_Agr_ID"),
                        rs.getString("Value") + " - " + rs.getString("Name")));
            }
            
            rs.close();
            stmt.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return list.elements();
    }
    
}
