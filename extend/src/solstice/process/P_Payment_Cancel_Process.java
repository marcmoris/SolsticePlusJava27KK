package solstice.process;

import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import solstice.model.P_Payment;

public class P_Payment_Cancel_Process extends SvrProcess
{
    private P_Payment payment;

    public P_Payment_Cancel_Process()
    {
        super();
    }

    /**
     * Récupération des paramètres
     */
    protected void prepare()
    {
        // On doit récupérer le paiement
        this.payment = P_Payment.get(Env.getCtx(), this.getRecord_ID(), null);
    }

    /**
     * Traitement principal
     */
    protected String doIt() throws Exception
    {
        P_Payment_Cancel paymentCancel = new P_Payment_Cancel(this.payment);
        String message = paymentCancel.execute();
        return Msg.translate(Env.getAD_Language(Env.getCtx()), message);
    }

}
