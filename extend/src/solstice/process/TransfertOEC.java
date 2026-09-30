
/*
 * Created on 05 Décembre, 2005
 *
 * Procédure de transfert électronique des obligations d'épargne du Canada ( Canada Savings Bonds )
 * 
 */

package solstice.process;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.CLogger;
import solstice.model.P_Employee;
import solstice.model.P_Period;




/**
 * @author Marc Morissette - Solstice
 *
 * Procédure de transfert électronique des obligations d'épargne du Canada
 *	
 */
public class TransfertOEC  extends SvrProcess 
{

	private int Client_ID = 0;
    private int Org_ID = 0;
    private int Period_ID = 0;
	private int Payment_Group_ID = 0;
    private boolean IsRework = false;

	private static CLogger		log = CLogger.getCLogger (TransfertOEC.class);

	private String numberTransfert = "";
	private String identity = "28803";

	private StringBuffer Email = new StringBuffer( "MDube@canadianhelicopters.com" );

    private String trxName = null; //	Trx.createTrxName();

    private String DateDisponibility = null;
    private P_Period Period;
    private int nbrRecord = 0;
    private int nbrEmployee = 0;
    private BigDecimal total = Env.ZERO;
    private int sequence = 0;
    
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("P_Payment_Group_ID"))
				Payment_Group_ID = para[i].getParameterAsInt();
			else if (name.equals("P_Period_ID"))
				Period_ID = para[i].getParameterAsInt();
			else if (name.equals("AD_Org_ID"))
				Org_ID = para[i].getParameterAsInt();
   		    else if (name.equals("DisponibilityDate"))
			    DateDisponibility = String.valueOf(para[i].getParameter());
			else if (name.equals("Rework"))
			    IsRework = "Y".equals(para[i].getParameter());
			else
				log.log (Level.INFO, "prepare - Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 * 	Read each time sheet 
	 *	@return 
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{

	  	if (Period_ID == 0)
	  	{
	  		log.log (Level.INFO,"Period not found");
	  		return ("*** Error - Period not found *");
	  	}

	  	GetParameter();
	  	
	  	Period = P_Period.get( Env.getCtx(), Period_ID, trxName);

	  	if ( DateDisponibility == null)
	  		DateDisponibility = Period.getPayDate().toString();

	  	DateDisponibility = DateDisponibility.substring(0, 10 );

	  	numberTransfert = Period.getName().replace("-", "") + "00";
		String retValue = null;
		StringBuffer result = new StringBuffer(""); 

		String sql = " SELECT P_Payment.P_Employee_ID, sum ( isnull(Employee_Part,0) + isnull(Employer_Part,0) ) as Amount" 
				   + "  FROM P_Payment_Deduction, P_Payment, P_Deduction "
				   + "  WHERE P_Payment.P_Payment_ID = P_Payment_Deduction.P_Payment_ID "
				   + "    AND P_Payment_Deduction.P_Deduction_ID = P_Deduction.P_Deduction_ID "
				   + "    AND P_Deduction.Value = '46' "
				   + "    AND P_Payment.P_Period_ID = " + Period_ID
				   + "    GROUP BY P_Payment.P_Employee_ID "
				   ;
		
	    if (Payment_Group_ID != 0)
	        sql += "  and P_Payment.P_Payment_Group_ID = " + Payment_Group_ID;
		
	    if (Org_ID != 0)
	        sql += "  and P_Payment.AD_Org_ID = " + Org_ID;
		
	    PreparedStatement pstmp = null;
	  	try
		{
	  		pstmp  = DB.prepareStatement(sql, trxName);
	  		ResultSet rs = pstmp.executeQuery ();

			InsertBlank( Email, 40 );

	  		CreateDisposition1( result);
	        CreateDisposition2( result);
  	        CreateDisposition3( rs , result);
	        CreateDisposition4( result);
  	        CreateDisposition5( result );

		}
	  	catch (Exception e)
		{
	  		log.log (Level.SEVERE,"TransfertOEC.doIt", e);
		}
		
		WriteToFile( result );
		
		retValue = "@Inserted@ " + nbrRecord + " Total : " + total.divide(new BigDecimal(100)).toString(); 
		
		return retValue;

	}

	
    public void WriteToFile(StringBuffer NewFile)
    {
    	String Path = null;
    	String sql = "Select TransfertPath from P_System_Parameters ";
        PreparedStatement psdoc = null;
       try
  	   { 
            psdoc = DB.prepareStatement(sql, null);
            ResultSet rsdoc = psdoc.executeQuery();
            if (rsdoc.next())
            {
                
                Path = rsdoc.getString("TransfertPath");
            }
            rsdoc.close();
            psdoc.close();
        }
        catch (Exception e)
        {
            log.log(Level.SEVERE, "TransfertOEC", e);
        }

		StringBuffer no = new StringBuffer( String.valueOf(sequence) );
		no = ZeroFill( no, 3 );

        String FileName =  identity + no.toString() + ".##p";

    	File aFile = null;
  	    Path = PgiUtil.getSolsticeParameter(Env.getCtx(), "TransfertPathOEC");
  	    aFile = new File(Path + FileName ); 
        FileOutputStream out; // declare a file output object
        PrintStream p; // declare a print stream object
        try
        {
              // Create a new file output stream
              out = new FileOutputStream(aFile);

              // Connect print stream to the output stream
              p = new PrintStream( out );

              p.println (NewFile.toString());
              System.out.println(NewFile.toString());
              p.close();
        }
        catch (Exception e)
        {
        	log.log (Level.SEVERE, "Error writing to file :", e);
        }
   }

	private void CreateDisposition1( StringBuffer result )
	{
		nbrRecord ++;
		
		result.append( "10" );  // Type d'enregistrement
		result.append(identity); // Identifieur d'organisation transférante
		result.append( DateDisponibility ); //Date du transfer
		result.append( numberTransfert ); // Identifieur du transfert
		
		StringBuffer Reference = new StringBuffer( "Transfert période" + Period.getName() );
		InsertBlank( Reference, 30 );
		result.append( Reference);
		result.append( "L"); // T=transfert  L=Lot

		result.append( "01");  // 01 - Courrier Electronique, 02 Télécopieur
		result.append( Email );
		InsertBlank( result, 119 );

		result.append( "x\r\n" );
	}

	private void CreateDisposition2( StringBuffer out )
	{
		nbrRecord ++;
		
		StringBuffer result = new StringBuffer(""); 
		result.append( "20" );  // Type d'enregistrement
		result.append(identity); // Identifieur d'organisation transférante
		result.append( DateDisponibility ); //Date du transfer
//		result.append( numberTransfert ); // Identifieur du transfert
		
		StringBuffer Reference = new StringBuffer( "" );
		InsertBlank( Reference, 30 );
		result.append( Reference);

		result.append( "01");  // 01 - Courrier Electronique, 02 Télécopieur

		result.append( Email );
		InsertBlank( result, 119 );

		result.append( "x\r\n" );
		out.append(result);
	}

	private void CreateDisposition3( ResultSet rs, StringBuffer out ) throws Exception
	{
		int count = 0;
		P_Employee Employee = null;
		StringBuffer result ; 
		total = Env.ZERO;
		
        while ( rs.next())
   		{
        	result = new StringBuffer("");
        	Employee = P_Employee.get( Env.getCtx(), rs.getInt("P_Employee_ID"), trxName);
        	count ++;
        	nbrEmployee ++;
        	
        	BigDecimal amount = rs.getBigDecimal("Amount");
    		total = total.add( amount );

        	result.append( "50" );  // Type d'enregistrement
    		result.append( identity ); // Identifieur d'organisation transférante
    		result.append( DateDisponibility ); //Date du transfer
    		
    		StringBuffer no = new StringBuffer( String.valueOf(count) );
    		no = ZeroFill( no, 5 );
    		result.append( no );
    		result.append( Employee.getSin().replaceAll("-", ""));
    		StringBuffer name = new StringBuffer( Employee.getName() );
    		InsertBlank( name, 50 );
    		result.append( name );
    		
    		if ( amount.compareTo(Env.ZERO) >= 0 )
    			result.append( " " );
    		else
    			result.append( "-" );

    		amount = amount.multiply(new BigDecimal(100)).setScale(0);
    		StringBuffer sAmount = new StringBuffer( String.valueOf( amount.intValue() ));
    		sAmount = ZeroFill( sAmount, 8 );
    		
    		result.append( sAmount ); // 

    		StringBuffer birthDate = new StringBuffer( String.valueOf( Employee.getBirthDate() ).substring(0,10) );
    		InsertBlank( birthDate, 10 );
    		
    		result.append( birthDate );

    		InsertBlank( result, 119 );
    		result.append( "x\r\n" );

    		out.append(result);
    
    		nbrRecord ++;
   		}


	}

	private void CreateDisposition4( StringBuffer out )
	{
		StringBuffer result = new StringBuffer(""); 
		nbrRecord ++;
		result.append( "80" );  // Type d'enregistrement
		result.append( identity ); // Identifieur d'organisation transférante
		result.append( DateDisponibility ); //Date du transfer
//		result.append( numberTransfert ); // Identifieur du transfert
		StringBuffer numberRecord = new StringBuffer( String.valueOf( nbrEmployee ) );
		numberRecord = ZeroFill( numberRecord, 6 );
		result.append( numberRecord ); // Nombre total d'enregistrement dans le transfert

		if ( total.compareTo(Env.ZERO) >= 0 )
			result.append( " " );
		else
			result.append( "-" );

		total = total.multiply(new BigDecimal(100)).setScale(0);
		StringBuffer stotal = new StringBuffer( String.valueOf( total.intValue() ));
		stotal = ZeroFill( stotal, 15 );
		
		result.append( stotal ); // Nombre total d'enregistrement dans le transfert

		InsertBlank( result, 119 );

		result.append( "x\r\n" );
		out.append(result);
	}
	

	private void CreateDisposition5( StringBuffer out )
	{
		StringBuffer result = new StringBuffer(""); 
		nbrRecord ++;
		result.append( "90" );  // Type d'enregistrement
		result.append( identity ); // Identifieur d'organisation transférante
		result.append( DateDisponibility ); //Date du transfer
		result.append( numberTransfert ); // Identifieur du transfert
		
		StringBuffer numberRecord = new StringBuffer( String.valueOf( nbrRecord) );
		numberRecord = ZeroFill( numberRecord, 9 );
		result.append( numberRecord ); // Nombre total d'enregistrement dans le transfert

		InsertBlank( result, 119 );

		result.append( "x\r\n" );
		out.append(result);
	}


    public void InsertBlank(StringBuffer Bl, int Pos)
    {
    	Pos = Pos - Bl.length();
    	for (int i = 0; i < Pos; i++)
    	{
    		Bl.append(" ");
    	}
    }

    public StringBuffer ZeroFill(StringBuffer Bl, int Pos)
    {
/*    	for (int i = 0; i < Pos; i++)
    	{
    		Bl.append("0");
    	}
*/
    	StringBuffer tmp = new StringBuffer( "");
    	Pos = Pos - Bl.length();
    	for (int i = 0; i < Pos; i++)
    	{
    		tmp.append("0");
    	}
    	tmp.append( Bl );
    	return tmp;
    }
    
    
	private void GetParameter()
	{
		String sql = " Select * From P_Form_Param Where Type = 'O'";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				identity = rs.getString( "TransmitterNumber" );
				Email = new StringBuffer( rs.getString("Email") );		
				sequence = rs.getInt( "FileNumber") + 1;
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("GetParameter, - " + sql);
		}

		sql = "update P_Form_Param  set FileNumber = " + sequence + " Where Type = 'O'" ;
		DB.executeUpdate(sql.toString(), null);

	}



}

