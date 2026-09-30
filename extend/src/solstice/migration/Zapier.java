package solstice.migration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.Compiere;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.P_Period;

public class Zapier {

	Properties ctx = Env.getCtx();
	/**	Logger							*/
	protected CLogger			log = CLogger.getCLogger (getClass());

	/** Transaction			*/
	private String m_trxName = null; // Trx.createTrxName();

	
	public Zapier () 
	{
		Env.setContext( ctx, "#AD_Client_ID", 1000004);
		Env.setContext( ctx, "#AD_Org_ID", 0);

		String sql = " SELECT id, [Produits retournés] json "
				+ "  FROM [dbo].[ZAPIER_V2] "
				;
		try
		{

			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next())
			{ 
				String id = rs.getString( "id");
				String json = "[";
				String tmp = rs.getString("json");
				//
				String[] myArray  = tmp.split("Produit");
				for (String s : myArray) {
//					System.out.println(  s.indexOf(",", 1 ) );
					if ( s.length() > 13)
					{
						json = json + "{";
//						String j = "\"Produit\":\"" +  s.substring(2,6) + "\" " + s.substring( s.indexOf(",", 1 ), s.length()).replace(",", "\",\"").replace(":", "\":\"") ;
						String j = "\"Produit\":\"" +  s.substring( 2, s.length()).replace(",", "\",\"").replace(":", "\":\"") ;
						json = json + j ;
						json = json + "\"\"},";
					}
				
//					System.out.println(s);


				}

				json = json.substring(0, json.length() -1);
				json = json + ']';

				
				String sql2 = "Update ZAPIER_V2 set jsoninfo = " + DB.TO_STRING( json ) + " where id = " + id;
				DB.executeUpdate(sql2, null);


				System.out.println(  json );

			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e)
		{
			System.err.println( "error : " + e);
		}
	}
	
    private Properties getCtx()
    {
    	return Env.getCtx();
    }
	
	
	public static void main (String[] args)
	{
		System.out.println("Test   $Revision: 1.3 $");
		System.out.println("----------------------------------");
		//
		Compiere.startup(true);
		
		
		int count = 0;
		new Zapier();
		count++;
		System.out.println("Generated = " + count);

	}	//	main
	
}
