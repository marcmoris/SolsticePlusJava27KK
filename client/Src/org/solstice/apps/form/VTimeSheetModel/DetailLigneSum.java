/******************************************************************************
* Product: Solstice+ Payroll & Human Resources Management                    *
* Copyright (C) 2008 ProGestion Informatique, Inc. All Rights Reserved.      *
* This program is free software, you can redistribute it and/or modify it    *
* under the terms version 2 of the GNU General Public License as published   *
* by the Free Software Foundation. This program is distributed in the hope   *
* that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
* warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
* See the GNU General Public License for more details.                       *
* You should have received a copy of the GNU General Public License along    *
* with this program, if not, write to the Free Software Foundation, Inc.,    *
* 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
* For the text or an alternative of this public license, you may reach us    *
* ProGestion Informatique, 210-5300 Bld des Galerie, Quebec, G2K 2A2 Canada  *
* or via info@progestion.net or http://www.progestion.net/license.html       *
******************************************************************************/
package org.solstice.apps.form.VTimeSheetModel;

import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Level;
import java.text.*;

import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import javax.swing.JOptionPane;

import java.text.SimpleDateFormat; 
import solstice.model.P_Assignment_Param;
import solstice.model.P_Gain;
import solstice.model.P_UOM;
import solstice.model.P_Period;

/**
 * Abstraction et stockage d'une ligne de détail
 */
public class DetailLigneSum {
    
    public static final String[] COLUMN_NAMES = { 
    	"Org",
    	Msg.translate(Env.getCtx(), "Period"),
		Msg.translate(Env.getCtx(), "Assignment"), 
		Msg.translate(Env.getCtx(), "GainCode"), 
		Msg.translate(Env.getCtx(), "StartDate" ),  
		Msg.translate(Env.getCtx(), "EndDate" ), 
		Msg.translate(Env.getCtx(), "Hourly_Rate" ), 
		Msg.translate(Env.getCtx(), "Unit" ),  
		Msg.translate(Env.getCtx(), "Quantity" ), 
		Msg.translate(Env.getCtx(), "Total" ), 
		Msg.translate(Env.getCtx(), "Organization" ),
		Msg.translate(Env.getCtx(), "Distribution" ),
		" " 
    };
//	Msg.translate(Env.getCtx(), "C_SalesRegion_ID" ),

    public static final int[] COLUMN_WIDTHS = {
    	30, 70, 150, 150, 90, 90, 70, 70, 60, 60, 100, 100, 20  		
    };
//	
    public static final int COL_ORG = 0;
	public static final int COL_PERIOD = 1;
	public static final int COL_ASSIGNMENT = 2;
	public static final int COL_GAIN = 3; 
	public static final int COL_STARTDATE = 4;
	public static final int COL_ENDDATE = 5;
	public static final int COL_HOURLY_RATE = 6;
	public static final int COL_UNIT = 7;
	public static final int COL_QUANTITY = 8;
	public static final int COL_TOTAL = 9;
	public static final int COL_ORGANIZATION = 10;
	public static final int COL_DISTRIBUTION = 11;
	public static final int COL_DELETE_ROW = 12;
//	public static final int COL_REGION = 10;
	
	public static final int IDX_SEMAINE = COL_STARTDATE;
	
	
	private DetailModelSum m_model;
	
	public DetailLigneSum( DetailModelSum model ) {
	    this( model, model.getPeriod() );
	}
	
	public DetailLigneSum( DetailModelSum model, P_Period period ) {
	    this( model, model.getLookup().lookupPeriod(period.getP_Period_ID()) );
	}
	
	private static CLogger		s_log = CLogger.getCLogger (DetailLigneSum.class);

	public DetailLigneSum( DetailModelSum model, KeyNamePair periode ) { 
		
		m_org = "PER";
		m_model = model; 
		P_Period period = P_Period.get(Env.getCtx(), periode.getKey() , null );  //m_model.getPeriod();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		m_mapDayIdQty = new HashMap<Integer, IdQty>();
		m_period = periode;
		m_week = 1;
		m_assignment = null; 
		m_poste = null;
		m_startDate = sdf.format(period.getStartDate().getTime()).toString();
		m_endDate = sdf.format(period.getEndDate().getTime()).toString();
		m_hourlyRate = null;
		m_unit = null;
		m_gainCode = null; 
		m_quantity = null; 
		m_amount = null;
		//m_schedule = null; 
		m_distribution = null;
		m_deleteRow = Boolean.FALSE;
		m_organization = null;
//		m_salesRegion = null;

		m_P_Employee_LongTermLeave_ID = 0;

	}

	
	public class IdQty {
		private int m_dayOfWeek;
		private int m_id = -1;
		private BigDecimal m_qty = null;
		private BigDecimal m_amount = null;
		public IdQty( int dayOfWeek, int id, BigDecimal qty ) { m_dayOfWeek = dayOfWeek; m_id = id; m_qty = qty; }
		public IdQty( int dayOfWeek, BigDecimal qty ) { m_dayOfWeek = dayOfWeek; m_qty = qty; }
		public int getDayOfWeek() { return m_dayOfWeek; }
		public int getId() { return m_id; }
		public BigDecimal getQty() { return m_qty; }
		public BigDecimal getAmount() { return m_amount; }
		public void setQty( BigDecimal qty ) { m_qty = qty; }
		public void setAmount( BigDecimal amount ) { m_amount = amount; }
	}
	
	
	
	private HashMap<Integer, IdQty> m_mapDayIdQty;
	public void setDayIdQty( int dayOfWeek, int detailId, BigDecimal qty ) {
		m_mapDayIdQty.put( new Integer(dayOfWeek), new IdQty(dayOfWeek, detailId, qty) ); 
	}
	
	public Collection getAllIdQty() { return m_mapDayIdQty.values(); }
	
	private String m_org;
	public void setOrg( String org ) { m_org = org; }
	public String getOrg() { return m_org; }
	
	private Object m_period;
	public void setPeriod( Object period ) { m_period = period; }
	public Object getPeriod() { return m_period; }
	
	private int m_week;
	public void setWeek( int week ) { m_week = week; }
	public int getWeek() { return m_week; }

	private Object m_assignment; 
	// ----------- Pgi Solstice 15-04-2008 ------------
	private boolean m_isSansValeur = false;
	// ---------------------------------
	public void setAssignment( Object assignment )
	{
		m_assignment = assignment;
		String lDate = (String)getStartDate();
		if (lDate != null)
		{
			if (!lDate.equals(""))
			{
				setDefautHourlyRate(lDate);
			}
		}
		// Assigne le poste automagiquement selon l'assignement
		//String name = assignment instanceof KeyNamePair ? ((KeyNamePair)assignment).getName() : (String)assignment;
		//setPoste( m_model.getPosteFromAssignment(name) );
	}
	public Object getAssignment() { return m_assignment; }
	
	private Object m_poste;
	public void setPoste( Object poste ) { m_poste = poste; }
	public Object getPoste() { return m_poste; }
	
	private Object m_gainCode; 
	
	// ----------- Pgi Solstice 15-04-2008 ------------
	public boolean isM_isSansValeur() {
		return m_isSansValeur;
	}

	public void setM_isSansValeur(boolean sansValeur) {
		m_isSansValeur = sansValeur;
	}
	
	// ----------------------------------------

	public void setGain( Object gain ) 
	{ 
		m_gainCode = gain;
		setUnit(m_gainCode);
		String lDate = (String)getStartDate();
		if (lDate != null)
		{
			if (!lDate.equals(""))
			{
				setDefautHourlyRate(lDate);
			}
		}

	}
	public Object getGain() { return m_gainCode; }

	private BigDecimal m_quantity; 
	public void setQuantity( BigDecimal quantity ) 
	{ 
		if ( quantity == null)
		{
			m_quantity = null;
			return;
		}
		
		if (quantity.stripTrailingZeros().scale() < 2 )
			m_quantity = quantity.setScale(2);
		else
			m_quantity = quantity.stripTrailingZeros();

		if (quantity.compareTo(Env.ZERO) == 0)
			m_quantity = null;

		
	}
	public BigDecimal getQuantity() { return m_quantity; }

	private BigDecimal m_amount; 
	public void setAmount( BigDecimal amount ) 
	{ 
		m_amount = getTotal();
/*		if ( amount == null  )
		{
			m_amount = getTotal();
		}
		else
		{
			m_amount = amount; 
		}
*/		
	}
	public BigDecimal getAmount() { return m_amount; }

	private int m_quantityId = -1;
	public void setQuantityId( int id ) { m_quantityId = id; }
	public int getQuantityId() { return m_quantityId; }
	
	private int m_sansValeurId = -1;
	public void setSansValeurId( int id ) { m_sansValeurId = id; }
	public int getSansValeurId() { return m_sansValeurId; }
	
	
	private Object m_startDate;
	public void setStartDate (Object sdate) 
	{ 
		Date Rep;
		Rep = munchDate(sdate.toString());
		
		// ----------- Pgi Solstice 15-04-2008 ------------
		if ((getHourlyRate() == null) && (! sdate.equals("")) && (getAssignment() != null) &&(!m_isSansValeur))
		{
			setDefautHourlyRate((String)sdate);
		}
		// ------------------------------
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (Rep != null )
		{
			m_startDate = sdf.format(Rep.getTime()).toString();
		}
		else
		{
			m_startDate = "";
		}
	}
	public Object getStartDate () {return m_startDate;}
	
	private Object m_endDate;
	public void setEndDate (Object edate) 
	{
		Date Rep;
		Rep = munchDate(edate.toString());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (Rep != null )
		{
			m_endDate = sdf.format(Rep.getTime()).toString();
		}
		else
		{
			m_endDate = "";
		}
	}
	public Object getEndDate () {return m_endDate;}
	
   public Date munchDate(String value) 
   {
		if (value == null ) return null;
//		P_Period period = m_model.getPeriod();
		P_Period period = P_Period.get(Env.getCtx(), ((KeyNamePair)this.m_period).getKey() , null );  //m_model.getPeriod();
		
 		
		Calendar startDate = Calendar.getInstance();
	 	startDate.setTimeInMillis( period.getStartDate().getTime() );
	 	
 		Calendar endDate = Calendar.getInstance();
	 	endDate.setTimeInMillis( period.getEndDate().getTime() );

		int length = value.toString().length();
		if( length == 10 ) {
			try
			{
	            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(value);
	            Calendar cal = Calendar.getInstance();
	            cal.setTimeInMillis(date.getTime());
/*	    		if( cal.before(startDate) || cal.after(endDate) ) 
	    		{
	    			JOptionPane.showMessageDialog(null,Msg.translate(Env.getCtx(), "dateTS1")," warning",JOptionPane.WARNING_MESSAGE);
	    			return null; 
	    		}
*/	    		
	            return date;				
		    }
			catch (ParseException e)
			{
				return null; 
			}

        }
		if( length != 1 && length != 2 ) return null;
		
		int jour;
		try 
		{
			jour = Integer.parseInt(value);
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null,Msg.translate(Env.getCtx(), "dateTS2").toString(),"Solstice",JOptionPane.WARNING_MESSAGE);
			return null;
		}
	 	
	 	Calendar date;
	 	// si le jour est inférieur au début de la période, on suppose qu'on arrive à la fin du mois
	 	// donc la fin de la période est probablement dans le mois suivant
	 	if( jour < startDate.get(Calendar.DAY_OF_MONTH) )
	 	    date = (Calendar)endDate.clone();
	 	else
	 	    date = (Calendar)startDate.clone();
	 	
	 	date.set(Calendar.DAY_OF_MONTH, jour);
/*	 	
	 	// Extérieur de la période
		if( date.before(startDate) || date.after(endDate) ) 
		{
			JOptionPane.showMessageDialog(null,Msg.translate(Env.getCtx(), "dateTS1"),"Solstice",JOptionPane.WARNING_MESSAGE);
			return null; 
		}
*/
        return date.getTime();
    }
   
	//TODO Avertissement (MSGBox) changement taux horaire
	private Object m_hourlyRate;
	public void setDefautHourlyRate (String StartDate) 
	{
		Object assignment = getAssignment();
		
		Object HourlyRate = 0;

		if ( assignment != null)
		{
			int assignmentID = Integer.parseInt(((KeyNamePair)assignment).getID());
			try
			{
				SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date parsedSDate = sdf.parse(StartDate);
				java.sql.Timestamp StartDate2 = new java.sql.Timestamp(parsedSDate.getTime());			

				P_Assignment_Param assignmentParam = P_Assignment_Param.get( Env.getCtx(), assignmentID,StartDate2,null);
				
				if ( assignmentParam != null )
					HourlyRate = assignmentParam.getHourlyRate( Gain, StartDate2 );
				
			}
			catch (Exception e)
			{
	    		s_log.log( Level.SEVERE, "Erreur ", e );
				System.out.println("Exception :"+e);   
			}
			
		}
		//Calendar lDate = PgiUtil.stringToDate(StartDate);
		
		m_hourlyRate = HourlyRate;
	}

	
	public void setHourlyRate (Object HourlyRate) 
	{ 
		m_hourlyRate = HourlyRate; 		
	}	
	
	public Object getHourlyRate () 
	{
		if ( Gain != null &&Gain.getType_Rate() != null  &&  Gain.getType_Rate().equals(  P_Gain.TYPE_RATE_FixedRate ) )
			return Gain.getHourly_Rate();

		
		return m_hourlyRate;
	}
	
	
	private int m_P_Employee_LongTermLeave_ID;
	
	public int getP_Employee_LongTermLeave_ID () {return m_P_Employee_LongTermLeave_ID;}
	public void setP_Employee_LongTermLeave_ID ( int ID ) { m_P_Employee_LongTermLeave_ID = ID; }
	
	private Object m_unit;
	private P_Gain Gain;
	private P_UOM  Uom;
	public void setUnit (Object gain) 
	{ 
		Gain = P_Gain.get(Env.getCtx(),Integer.parseInt(((KeyNamePair)gain).getID()) , null);	
		Uom = P_UOM.get(Env.getCtx(), Gain.getP_UOM_ID(), null);
		m_unit = Uom.getTrlName();
//	    m_unit = Msg.translate(Env.getCtx(), unit.getName());
	}
	public Object getUnit() {return m_unit;}

	public void setQty( int dayOfWeek, BigDecimal qty ) {
		IdQty idQty = (IdQty)m_mapDayIdQty.get( new Integer(dayOfWeek));
		if( idQty == null ) {
			m_mapDayIdQty.put( new Integer(dayOfWeek), new IdQty(dayOfWeek, qty) ); 
		}
		else {
			idQty.setQty( qty );
		}
	}
	
	public void setAmount( int dayOfWeek, BigDecimal amount ) {
		IdQty idQty = (IdQty)m_mapDayIdQty.get( new Integer(dayOfWeek));
		if( idQty == null ) {
			m_mapDayIdQty.put( new Integer(dayOfWeek), new IdQty(dayOfWeek, amount) ); 
		}
		else {
			idQty.setAmount( amount );
		}
	}

	public BigDecimal getDayQty( int dayOfWeek ) { 
		IdQty idQty = (IdQty)m_mapDayIdQty.get(new Integer(dayOfWeek));
		return idQty != null ? idQty.getQty() : null; 
	}

/*	public BigDecimal getDayAmount( int dayOfWeek ) { 
		IdQty idQty = (IdQty)m_mapDayIdQty.get(new Integer(dayOfWeek));
		return idQty != null ? idQty.getAmount() : null; 
	}
*/
	public int getDayId( int dayOfWeek ) { 
		IdQty idQty = (IdQty)m_mapDayIdQty.get(new Integer(dayOfWeek));
		return idQty != null ? idQty.getId() : -1; 
	}
	
	public BigDecimal getTotal() {
		BigDecimal total = new BigDecimal(0);
		
		if ( Gain != null && Gain.isHNR())
			return total;

		if ( this.getQuantity() != null && this.getHourlyRate() != null && safeBigDecimal( this.getHourlyRate()).compareTo(Env.ZERO) != 0 )
			total = (this.getQuantity().multiply( safeBigDecimal( this.getHourlyRate()) )).setScale(2, BigDecimal.ROUND_HALF_UP);
		else
		{
			//2010.03.30 
			if ( Gain != null && Gain.getP_UOM_ID() == 100 )
				total = Env.ZERO;
			else
				total = this.getQuantity();
//			total = this.getQuantity();
		}
		return total;
	}
	
	private Object m_distribution;
	public void setDistribution( Object distribution ) { m_distribution = distribution; }
	public Object getDistribution() { return m_distribution; }
	
	private Boolean m_deleteRow;
	public void setDeleteRow( Boolean deleteRow) { m_deleteRow = deleteRow; }
	public Boolean getDeleteRow() { return m_deleteRow; }
	
    private boolean m_newRow = false;
    public void tagNewRow() { m_newRow = true; }
    public boolean isNewRow() { return m_newRow; }
    private BigDecimal safeBigDecimal( Object value ) {
    	if( value instanceof BigDecimal ) return (BigDecimal)value;
    	if( value instanceof String ) {
    		if( "".equals(value) ) return new BigDecimal(0);
    		return new BigDecimal((String)value);
    	}
    	return null;
    }
	private Boolean safeBoolean( Object value ) {
		if( value instanceof Boolean ) return (Boolean)value;
		if( value instanceof String ) {
			return Boolean.valueOf((String)value);
		}
		return null;
	}

	private Object m_organization;
	
	public Object getOrganization() {
		return m_organization;
	}

	public void setOrganization(Object m_organization) {
		this.m_organization = m_organization;
	}
/*	
	private Object m_salesRegion;
	
	public Object getSalesRegion() {
		return m_salesRegion;
	}

	public void setSalesRegion(Object region) {
		m_salesRegion = region;
	}
*/
	// Accès aux données par colonne
	// Devrait seulement être utilisé par la JTable/Renderer pour l'affichage et l'édition
    public void setColumn( int col, Object value ) {
    	switch( col ) {
    		case COL_ORG: setOrg((String)value); break;
    		case COL_PERIOD: setPeriod(value); break;
    		case COL_ASSIGNMENT: setAssignment(value); break;
	        case COL_GAIN: setGain(value); break; 
	        case COL_QUANTITY: setQuantity(safeBigDecimal(value)); break;
	        case COL_STARTDATE: setStartDate(value); break;
	        case COL_ENDDATE: setEndDate(value); break;
	        case COL_HOURLY_RATE: setHourlyRate( value); break;
	        case COL_TOTAL: setAmount(safeBigDecimal(value)); break;
//	        case COL_REGION: setSalesRegion(value); break;
	        case COL_ORGANIZATION: setOrganization(value); break;
	        //case COL_UNIT: setQty(Calendar.WEDNESDAY, safeBigDecimal(value)); break;
	        case COL_DISTRIBUTION: setDistribution(value); break;
	        case COL_DELETE_ROW: setDeleteRow(safeBoolean(value)); break;
    	}
    }
    
	// Accès aux données par colonne
	// Devrait seulement être utilisé par la JTable/Renderer pour l'affichage et l'édition
    public Object getColumn( int col ) {
    	switch( col ) {
    		case COL_ORG: return getOrg();	
    		case COL_PERIOD: return getPeriod();
    		case COL_ASSIGNMENT: return getAssignment();
	        case COL_GAIN: return getGain(); 
	        case COL_QUANTITY: return getQuantity(); 
	        case COL_STARTDATE: return getStartDate(); 
	        case COL_ENDDATE: return getEndDate();
	        case COL_HOURLY_RATE: return getHourlyRate();
	        case COL_UNIT: return getUnit();
	        case COL_TOTAL: return getTotal();
//	        case COL_REGION: return getSalesRegion();
	        case COL_ORGANIZATION: return getOrganization();
	        case COL_DISTRIBUTION: return getDistribution();
	        case COL_DELETE_ROW: return getDeleteRow();
    	}
    	return null;
    }
    
    public boolean isEditable( int col ) {
    	//+2011.07.27 - Ligne en provenance de la feuille de temps employé sont non modifiable
    	if ( getOrg().equals("FTE"))
    		return false;
    	//-2011.07.27 
    	if( isJour(col) || col == COL_QUANTITY ) {
			if( m_model.getLookup().isGainSansValeur(m_gainCode) )
			{
				return false;
			}
			else
			{
				return  COL_QUANTITY == col;
			}
			
		/*	if( m_model.getLookup().isGainMonetaire(m_gainCode) ) 
				return COL_QUANTITY == col;
			else
				return COL_QUANTITY != col;*/
		}
		if( COL_ORG == col ) return false;
    	return true;
    }
    
    // Identifier les colonnes contenant une journée
    public static boolean isJour( int col ) { return false; }; //col >= COL_STARTDATE && col <= COL_ENDDATE; }
    
    // Identifier les colonnes ayant un total
    public static boolean isSummable( int col ) { return isJour(col) || col == COL_QUANTITY || col == COL_TOTAL; }
    
    public static Class getColumnClass( int col ) {
    	if( isSummable(col) ) return BigDecimal.class;
    	if( col == COL_DELETE_ROW ) return Boolean.class;
    	return String.class;
    }
}

