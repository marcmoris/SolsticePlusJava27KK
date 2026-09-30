package solstice.process;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

public class krispy_load_journal extends SvrProcess{
	protected void prepare()
    {
		
    }

	protected String doIt() throws Exception
	{
		boolean retValue = true;
		
        DB.executeUpdate("exec [dbo].[KRISPY_LOAD_JOURNAL]",  null);

		String ret = "Mise a jour terminée";
		return retValue == true ? ret : "error";
    }		


		

}
