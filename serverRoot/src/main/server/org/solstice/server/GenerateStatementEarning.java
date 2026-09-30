package org.solstice.server;

import java.util.Properties;
import java.util.Stack;
import java.util.logging.Level;

import org.compiere.model.MClient;
import org.compiere.util.CLogMgt;
import org.compiere.util.CLogger;
import org.compiere.util.Env;

public class GenerateStatementEarning extends Thread {

	private Thread t;
	private String threadName;
	 
	/** Client info					*/
	private MClient 			m_client = null;
    
	private static CLogger	s_log	= CLogger.getCLogger (GenerateStatementEarning.class);

	Properties ctx = Env.getCtx();
	
	public Stack<String> m_stack;
	
    public void run() {
        System.out.println("Run");
		
    	CLogMgt.setLevel(Level.INFO);

		int Org_ID = 0;

		System.out.println("GenerateStatementEarning Thread " +  threadName + " exiting.");
	   
    }
	
	
}
