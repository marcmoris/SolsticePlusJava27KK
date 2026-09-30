package solstice.process;

import java.io.File;
import java.io.FileWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.model.MColumn;
import org.compiere.model.M_Element;
import org.compiere.util.CLogMgt;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class SolsticeGenerateHtml {

	/**	Logger			*/
	private static CLogger	log	= CLogger.getCLogger (SolsticeGenerateHtml.class);

	
	public SolsticeGenerateHtml (ResultSet rs, String directory) throws Exception
	{
		//	create column access methods
		StringBuffer sb = new StringBuffer();
		
		sb.append("<!DOCTYPE html>");
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<title>" + rs.getString("Name") + "</title>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append("<form>");
		sb.append("<table>");
				
		sb = sb.append( 	createField( rs) );
		//	Save it

		sb.append("</table>");

		sb.append("</form>");

		sb.append("</body>");
		sb.append("</html>");

		writeToFile (sb, directory + rs.getString("AD_Window_ID") + "_" + rs.getString("AD_Tab_ID") + ".html");
	}	//	GenerateModel
	
	int old = 0;
	private StringBuffer Add_Field( ResultSet rs, int col) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		MColumn column = new MColumn( Env.getCtx(), rs.getInt("AD_COLUMN_ID"), null);
		M_Element element = new M_Element( Env.getCtx(), column.getAD_Element_ID(), null);

		sb.append( "<td>");
			
		sb.append( "<label for=\"" + column.getColumnName() + "\">" + element.getName() + ":</label> " );
		sb.append( "</td>");
		sb.append( "<td>");
		// String
		if ( column.getAD_Reference_ID() == 10 )
			sb.append( "<input type=\"text\" id=\"" + column.getColumnName() + "\" name=\"" + column.getColumnName() + "\" value=\"\"> ");
		// Integer
		if ( column.getAD_Reference_ID() == 11 )
			sb.append( "<input type=\"number\" id=\"" + column.getColumnName() + "\" name=\"" + column.getColumnName() + "\" value=\"\"> ");

		// Amount
		if ( column.getAD_Reference_ID() == 12 )
			sb.append( "<input type=\"number\" id=\"" + column.getColumnName() + "\" name=\"" + column.getColumnName() + "\" value=\"\"> ");

		// ID
		if ( column.getAD_Reference_ID() == 13 )
			sb.append( "<input type=\"number\" id=\"" + column.getColumnName() + "\" name=\"" + column.getColumnName() + "\" value=\"\"> ");

		// Text
		if ( column.getAD_Reference_ID() == 14 )
			sb.append( "<input type=\"text\" id=\"" + column.getColumnName() + "\" name=\"" + column.getColumnName() + "\" value=\"\"> ");

		// Date
		if ( column.getAD_Reference_ID() == 15 )
			sb.append( "<input type=\"date\" id=\"" + column.getColumnName() + "\" name=\"" + column.getColumnName() + "\" value=\"\"> ");

		// Date Time
		if ( column.getAD_Reference_ID() == 16 )
			sb.append( "<input type=\"date\" id=\"" + column.getColumnName() + "\" name=\"" + column.getColumnName() + "\" value=\"\"> ");

		// List
		if ( column.getAD_Reference_ID() == 17 )
			sb.append( "<input type=\"text\" id=\"" + column.getColumnName() + "\" name=\"" + column.getColumnName() + "\" value=\"\"> ");

		// Table
		if ( column.getAD_Reference_ID() == 18 )
			sb.append( "<input type=\"text\" id=\"" + column.getColumnName() + "\" name=\"" + column.getColumnName() + "\" value=\"\"> ");

		// Table Direct
		if ( column.getAD_Reference_ID() == 19 )
			sb = getTableDirSql( column.getAD_Reference_ID(), column.getColumnName()  ) ;
//			sb.append( "<input type=\"text\" id=\"" + column.getColumnName() + "\" name=\"" + column.getColumnName() + "\" value=\"\"> ");

		// Yes-No
		if ( column.getAD_Reference_ID() == 20 )
			sb.append( "<input type=\"checkbox\" id=\"" + column.getColumnName() + "\" name=\"" + column.getColumnName() + "\" value=\"\"> ");

		// Location (Address)
		if ( column.getAD_Reference_ID() == 21 )
			sb.append( "<input type=\"text\" id=\"" + column.getColumnName() + "\" name=\"" + column.getColumnName() + "\" value=\"\"> ");

		// Number
		if ( column.getAD_Reference_ID() == 22 )
			sb.append( "<input type=\"number\" id=\"" + column.getColumnName() + "\" name=\"" + column.getColumnName() + "\" value=\"\"> ");

		// Binary
		// Time
		if ( column.getAD_Reference_ID() == 24 )
			sb.append( "<input type=\"color\" id=\"" + column.getColumnName() + "\" name=\"" + column.getColumnName() + "\" value=\"\"> ");

		//Color
		if ( column.getAD_Reference_ID() == 27 )
			sb.append( "<input type=\"color\" id=\"" + column.getColumnName() + "\" name=\"" + column.getColumnName() + "\" value=\"\"> ");

		//Button
		if ( column.getAD_Reference_ID() == 28 )
			sb.append( "<input type=\"button\" id=\"" + column.getColumnName() + "\" name=\"" + column.getColumnName() + "\" value=\"\"> ");
		
		// Quantity
		if ( column.getAD_Reference_ID() == 29 )
			sb.append( "<input type=\"number\" id=\"" + column.getColumnName() + "\" name=\"" + column.getColumnName() + "\" value=\"\"> ");

		// Search
		if ( column.getAD_Reference_ID() == 29 )
			sb.append( "<input type=\"number\" id=\"" + column.getColumnName() + "\" name=\"" + column.getColumnName() + "\" value=\"\"> ");

		
		sb.append( "</td>");

		old = col;

		
		return sb;
	}
	
	private StringBuffer createField (ResultSet rsWin ) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		String sql = "SELECT * "
			+ " FROM AD_FIELD "
			+ " WHERE AD_FIELD.AD_TAB_ID = " + rsWin.getInt("AD_TAB_ID")
			+ " AND ISDISPLAYED = 'Y' " 
			+ " ORDER BY SEQNO ";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				sb.append("<tr>");
				sb.append( Add_Field(  rs , 1 ) );
				
				if ( (rs.next()) )
				{
					if ( rs.getString("ISSameLine").equals("Y"))
					{
						sb.append( Add_Field(  rs , 2 ) );
					}
					else
					{
						sb.append("</tr>");
						sb.append("<tr>");
						sb.append( Add_Field(  rs , 1 ) );
						sb.append("</tr>");
					}
				}
				sb.append("</tr>");

					
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			try
			{
				if (pstmt != null)
					pstmt.close ();
			}
			catch (Exception e)
			{}
			pstmt = null;
		}
		return sb;
	}	//	createColumns

	private StringBuffer getTableDirSql( int AD_Reference_ID, String ColumnName) 
	{
		StringBuffer sb = new StringBuffer();
        String TableName =  ColumnName.replace( "_ID", "" );
        String sql = "SELECT NAME, " + ColumnName + " as ColumnName FROM " + TableName + " WHERE ISACTIVE = 'Y' " ;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			sb.append( "<select id=\"" + ColumnName + "\" name=\"" + ColumnName + "\">");
			
			while (rs.next())
			{
				sb.append( "<option value=\"" + rs.getString("ColumnName") + "\"> " + rs.getString("Name") + " </option>");
			}
			
			sb.append( "</select>");
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}

        return sb;
	}

	private StringBuffer getListSql( int AD_Reference_ID, String ColumnName) 
	{
		StringBuffer sb = new StringBuffer();
		return sb;
	}


	
	/**************************************************************************
	 * 	Write to file
	 * 	@param sb string buffer
	 * 	@param fileName file name
	 */
	private void writeToFile (StringBuffer sb, String fileName)
	{
		try
		{
			File out = new File (fileName);
			FileWriter fw = new FileWriter (out);
			for (int i = 0; i < sb.length(); i++)
			{
				char c = sb.charAt(i);
				//	after
				if (c == ';' || c == '}')
				{
					fw.write (c);
					if (sb.substring(i+1).startsWith("//"))
						fw.write('\t');
					else
						fw.write(Env.NL);
				}
				//	before & after
				else if (c == '{')
				{
					fw.write(Env.NL);
					fw.write (c);
					fw.write(Env.NL);
				}
				else
					fw.write (c);
			}
			fw.flush ();
			fw.close ();
			float size = out.length();
			size /= 1024;
			log.info(out.getAbsolutePath() + " - " + size + " kB");
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, fileName, ex);
		}
	}	//	writeToFile

	
	public static void main (String[] args)
	{
		org.compiere.Compiere.startupEnvironment(true);
		CLogMgt.setLevel(Level.FINE);
		log.info("Generate HTML page   $Revision: 1.1 $");
		log.info("----------------------------------");
		//	first parameter
		String directory = "C:\\SOLSTICE\\HTML\\";
		if (args.length > 0)
			directory = args[0];
		if (directory == null || directory.length() == 0)
		{
			System.err.println("No Directory");
			System.exit(1);
		}
		log.info("Directory: " + directory);
		
		StringBuffer sql = new StringBuffer("");
		//	complete sql
		sql.insert(0, "SELECT AD_WINDOW.AD_WINDOW_ID, AD_WINDOW.name, AD_TAB.AD_TAB_ID, AD_TAB.name as TabName, AD_TAB.SEQNO "
			+ "FROM AD_WINDOW "
			+ " INNER JOIN AD_TAB ON AD_TAB.AD_WINDOW_ID = AD_WINDOW.AD_WINDOW_ID "
			+ " WHERE AD_WINDOW.EntityType IN ('U' )  ");
		
		sql.append(" ORDER BY AD_WINDOW.Name, AD_TAB.SEQNO  ");
		
		//
		int count = 0;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				new SolsticeGenerateHtml(rs , directory);
				count++;
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.severe(e.toString());
		}
		finally
		{
			try
			{
				if (pstmt != null)
					pstmt.close ();
			}
			catch (Exception e)
			{}
			pstmt = null;
		}
		log.info("Generated = " + count);

	}	//	main
	
}
