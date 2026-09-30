/*
 * Created on 2005-09-09
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.custom;

import org.compiere.model.GridField;
import org.compiere.model.GridTab;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.Level;
import org.compiere.model.CalloutEngine;
import org.compiere.util.DB;

/**
 * @author vivdun01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CalloutParticularSheet extends CalloutEngine
{
	public String loadDate (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) 
	{
		// Si rien de saisi ou alors pas encore un jour valide (2 char)
		if (value == null || value.toString().length() != 2) return "";
		
		int jour = Integer.parseInt(value.toString());
		
		PreparedStatement ps = null;
		ResultSet rs = null;
	 		 	
 		Calendar startDate = Calendar.getInstance();
 		Calendar endDate = Calendar.getInstance();
	 	try  {
			ps = DB.prepareStatement( "SELECT StartDate, EndDate FROM P_Period WHERE Name = ? " , null);
			ps.setString( 1, (String)mTab.getValue("Period") );
	 		
	 		rs = ps.executeQuery();
			if( rs.next() ) {
	 			startDate.setTimeInMillis( rs.getTimestamp("StartDate").getTime() );
	 			endDate.setTimeInMillis( rs.getTimestamp("EndDate").getTime() );
			}
			else return "";
	 	}
	 	catch( Exception e ) {
	 		log.log(Level.SEVERE, "CalloutParticularSheet.loadDate", e);
	 	}
	 		
	 	
	 	Calendar date;
	 	// si le jour est inférieur au début de la période, on suppose qu'on arrive à la fin du mois
	 	// donc la fin de la période est probablement dans le mois suivant
	 	if( jour < startDate.get(Calendar.DAY_OF_MONTH) )
	 	    date = (Calendar)endDate.clone();
	 	else
	 	    date = (Calendar)startDate.clone();
	 	
	 	date.set(Calendar.DAY_OF_MONTH, jour);
	 	
	 	// Extérieur de la période
		if (date.after(endDate)) return ""; 

		mTab.setValue("Day", new Timestamp(date.getTimeInMillis()));
		mTab.setValue("Day_Character", null);
		
	 	return "";
	}

}	// CalloutParticularSheet
