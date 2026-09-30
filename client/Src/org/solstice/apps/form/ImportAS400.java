/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.solstice.apps.form;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;
import java.math.BigDecimal;

import org.compiere.apps.*;
import org.compiere.apps.SwingWorker;
import org.compiere.grid.ed.*;
import org.compiere.plaf.*;
import org.compiere.swing.*;
import org.compiere.util.*;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;

import solstice.model.P_Particular_Sheet;
import solstice.model.P_Period;
import solstice.utils.PgiUtil;
import solstice.model.P_Particular_Credits;


/**
 *	Fixed length file import
 *
 *  @author 	Steven Nadeau
 *  @version 	$Id: ImportAS400.java,v 1.2 
 */
public class ImportAS400 extends CPanel
	implements FormPanel, ActionListener
{
	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame)
	{
		log.info("");
		m_WindowNo = WindowNo;
		m_frame = frame;
		try
		{
			jbInit();
			dynInit();
			frame.getContentPane().add(northPanel, BorderLayout.NORTH);
			frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
			frame.getContentPane().add(confirmPanel, BorderLayout.SOUTH);
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "init", e);
		}
	}	//	init

	/**	Window No			*/
	private int         		m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 			m_frame;

	private ArrayList<String>	m_data = new ArrayList<String>();
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ImportAS400.class);
	//
//	private static final String s_none = "----";	//	no format indicator
	//
	private CPanel northPanel = new CPanel();
	private JButton bFile = new JButton();
	private VComboBox pickFormat = new VComboBox();
	private CPanel centerPanel = new CPanel();
	private BorderLayout centerLayout = new BorderLayout();
	private JScrollPane rawDataPane = new JScrollPane();
	private JTextArea rawData = new JTextArea();
	private JScrollPane previewPane = new JScrollPane();
	private CPanel previewPanel = new CPanel();
	private ConfirmPanel confirmPanel = new ConfirmPanel(true);
	private JLabel info = new JLabel();
	private JLabel labelFormat = new JLabel();
	private GridBagLayout previewLayout = new GridBagLayout();
	private JLabel record = new JLabel();
	
	private File[] file = null;

	/**
	 *	Static Init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		CompiereColor.setBackground(this);
		bFile.setText(Msg.getMsg(Env.getCtx(), "FileImportFile"));
		bFile.setToolTipText(Msg.getMsg(Env.getCtx(), "FileImportFileInfo"));
		bFile.addActionListener(this);
		info.setText("   ");
		labelFormat.setText(Msg.translate(Env.getCtx(), "AD_ImpFormat_ID"));
		//

		//
		northPanel.setBorder(BorderFactory.createEtchedBorder());
		northPanel.add(bFile, null);
		northPanel.add(info, null);
		northPanel.add(labelFormat, null);
		northPanel.add(pickFormat, null);
		northPanel.add(record, null);
		//
		centerPanel.setLayout(centerLayout);
		rawData.setFont(new java.awt.Font("Monospaced", 0, 10));
		rawData.setColumns(80);
		rawData.setRows(5);
		rawDataPane.getViewport().add(rawData, null);
		centerPanel.add(rawDataPane, BorderLayout.NORTH);
		centerPanel.add(previewPane, BorderLayout.CENTER);
		//
		previewPanel.setLayout(previewLayout);
		previewPane.getViewport().add(previewPanel, null);
		previewPane.setPreferredSize(new Dimension(700,80));
		//
		confirmPanel.addActionListener(this);
	}	//	jbInit

	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	}	//	dispose

	/**
	 *	Dynamic Init
	 */
	private void dynInit()
	{
		//	Load Formats
//		pickFormat.addItem(new KeyNamePair(0,s_none));
		pickFormat.addItem(new KeyNamePair(0,Msg.translate(Env.getCtx(), "Time")));
//		pickFormat.addItem(new KeyNamePair(2,Msg.translate(Env.getCtx(), "BankMove")));
		pickFormat.setSelectedIndex(0);
		pickFormat.addActionListener(this);
		//
		confirmPanel.getOKButton().setEnabled(false);
	}	//	dynInit

	
	/**************************************************************************
	 *	Action Listener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		if (e.getSource() == bFile)
		{
			cmd_loadFile();
			invalidate();
			m_frame.pack();
		}
		else if (e.getSource() == pickFormat)
		{
			m_frame.pack();
		}

		
		else if (e.getActionCommand().equals(ConfirmPanel.A_OK))
		{
			m_frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			confirmPanel.setEnabled(false);
			m_frame.setBusy(true);
			//
			SwingWorker worker = new SwingWorker()
			{
				public Object construct()
			    {
			    	cmd_process();
					return Boolean.TRUE;
				}
			};
			worker.start();
			//  when you need the result:
			//	x = worker.get();   //  this blocks the UI !!
		}
		else if (e.getActionCommand().equals(ConfirmPanel.A_CANCEL))
		{
			dispose();
		}

		confirmPanel.getOKButton().setEnabled(true);

/*		if (Integer.parseInt(this.pickFormat.getValue().toString()) != 0)
		{
			confirmPanel.getOKButton().setEnabled(true);
		}
		else
		{
			confirmPanel.getOKButton().setEnabled(false);
		}
		*/

	}	//	actionPerformed


	/**************************************************************************
	 *	Load File
	 */
	private void cmd_loadFile()
	{
		String directory = "I:\\CPR\\X104\\Data";
		String path = PgiUtil.getSolsticeParameter(Env.getCtx(), "X104Path");
		if ( path.endsWith( File.separator ) == false )
			path = path + File.separator;
		
		if ( path.length() != 0 )
		directory = path;
		
/*			org.compiere.Compiere.getCompiereHome() 
			+ File.separator + "data" 
			+ File.separator + "import";
*/			
		log.config(directory);
		//
		JFileChooser chooser = new JFileChooser(directory);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(true);
		chooser.setDialogTitle("Sélectionner les fichiers à enregistrer");
		if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
			return;
		file = chooser.getSelectedFiles();
		rawData.setText("");
		for (int i = 0; i < file.length; i++)
		{
			log.config(file[0].getName());
			setCursor (Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			m_data.clear();
			rawData.append(file[i].getName());
			rawData.append("\n");
			rawData.setCaretPosition(0);
		}
	}	//	cmd_loadFile

	void cmd_process()
	{
		int nbImp = 0;
		int nbInv = 0;
		String error = "";
		String rep = "";
		for (int i = 0; i < file.length; i++)
		{		
			try
			{
				BufferedReader in = new BufferedReader(new FileReader(file[i]), 10240);
				int nbLineHead = 0;
				String s = null;
				while ((s = in.readLine()) != null)
				{
					if ( s.length() > 30)  // nbLineHead > 1 &&
					{
						if (Integer.parseInt(this.pickFormat.getValue().toString()) == 0)
						{
	
								rep = ImportTime(s, file[i].getName() );
								if ((!rep.equals("0")) &&  (rep.equals("1")))
								{
									nbImp += Integer.parseInt(rep);
								}
								else if (!rep.equals("0"))
								{
									nbInv++;
									error += rep;
									error += "\n";
								}
						}
					}
					nbLineHead++;
				}
				in.close();
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, "", e);
				bFile.setText(Msg.getMsg(Env.getCtx(), "FileImportFile"));
			}
		}
		String titre = Msg.translate(Env.getCtx(), "ReportImportation");
		String label1 = Msg.translate(Env.getCtx(), "NbImportedLines");
		String label2 = Msg.translate(Env.getCtx(), "NbErrorLines");
		String label3 = Msg.translate(Env.getCtx(), "DescInvLines");
		
		ADialog.info(m_WindowNo, this,titre + ": \n" + label1 + ": " + nbImp + "\n" + label2 + ": " + nbInv +  "\n" + label3 + ": \n" + error);
		dispose();
	}	//	cmd_process
	
	

	private String ImportTime(String Line, String fileName)
	{
		//String[] arrayGain = {"02","04","05","20","30","31","32","33","34","35","36","39","40","41","42","43","44","45","47","49","50","51","52","54","55","57","58","59","60","69","73","74","80","90","97","98" };
		//String[] arrayDesc = {"B","U","U","B","U","U","B","B","B","B","B","B","B","$","B","B","B","B","B","B","B","B","B","B","B","B","B","B","B","B","B","B","B","U","B","$"};
		//Important que les gains soit classé en ordres croissants et que les arrayDesc Soit correspondant
		String[] arrayGain = {"02","04","05","20","30","31","32","33","34","35","36","39","40","41","42","43","44","45","47","49","50","51","52","54","55","57","58","59","60","69","70","71","73","74","80","90","97","98" };
		String[] arrayDesc = {"B" ,"U" ,"U" ,"B" ,"U" ,"U" ,"B" ,"B" ,"B" ,"B" ,"B" ,"B" ,"B" ,"$" ,"B" ,"B" ,"B" ,"B" ,"B" ,"B" ,"B" ,"B" ,"B" ,"B" ,"B" ,"B" ,"B" ,"B" ,"B" ,"B" ,"B" ,"B" ,"B" ,"B" ,"B" ,"U" ,"B" ,"$"};

		
		boolean invalid = false; //Si le code n'est pas dans le array , on ne doit pas faire l'update

		if (Line.codePointAt(0) != 26)
		{	
			String cie = Line.substring(0,4);
			String dept = Line.substring(4, 5); 
			String act = Line.substring(5,9); 
//			String codeTransac = Line.substring(20, 22); //ne pas le mettre pour l'instant
			String Empl = Line.substring(14, 20);
			String codeGain = Line.substring(28,30);			
			StringBuffer montant = null;
			StringBuffer unit = null;
			StringBuffer taux = null;
			BigDecimal montantBD = null;
			BigDecimal unitBD = null;
			BigDecimal tauxBD = null;
			boolean stop = false;

			P_Period Period = P_Period.getOpenPeriod(Env.getCtx(), null);

			int pos = 0;
			while ((pos <= arrayGain.length) && (stop == false))
			{
				if (arrayGain[pos].equals(codeGain))
				{
					stop = true;
				}
				else
				{
					if (Integer.parseInt(arrayGain[pos]) > Integer.parseInt(codeGain))
					{
						invalid = true;
						stop = true;
					}
					else
					{
						pos++;
					}	
				}
			}
			
			if (! invalid)
			{
				if (arrayDesc[pos].equals("U"))
				{
					unit = convertSpecialCaracter(new StringBuffer(Line.substring(30,36)));
					unitBD = new BigDecimal(unit.toString());	
					unitBD = unitBD.divide(new BigDecimal(100) );
//					unitBD = new BigDecimal(unit.insert(5, ".").toString());	
				}
				else if (arrayDesc[pos].equals( "B" )  )
				{
					unit = convertSpecialCaracter(new StringBuffer(Line.substring(30,36)));
					unitBD = new BigDecimal(unit.toString());	
					unitBD = unitBD.divide(new BigDecimal(100) );
//					unitBD = new BigDecimal(unit.insert(5, ".").toString());
					//String temp = unitBD.toString();
					taux = convertSpecialCaracter(new StringBuffer(Line.substring(36,42)));
					tauxBD = new BigDecimal(taux.toString());
//					tauxBD = tauxBD.divide(new BigDecimal(10000));
					tauxBD = tauxBD.divide(new BigDecimal(100));
//					tauxBD = new BigDecimal(taux.insert(2, ".").toString());
				}
				else if (arrayDesc[pos].equals( "$" ) )
				{
					montant = convertSpecialCaracter(new StringBuffer(Line.substring(30,36)));
					montantBD = new BigDecimal( montant.toString() );
					montantBD = montantBD.divide(new BigDecimal(100));
//					montantBD = new BigDecimal(montant.insert(2, ".").toString());
				}

				
				int GainID = getSpecifiedID("select P_Gain_ID from P_Gain where value = '" + codeGain + "'", "P_Gain_ID");
				int EmplID = getSpecifiedID("select P_Employee_ID from P_Employee where value = '" + Empl + "'", "P_Employee_ID");
				int CieID = getSpecifiedID("select AD_Org_ID from AD_Org where value = '" + cie + "'","AD_Org_ID");
				int DeptID = getSpecifiedID("select P_Department_ID from P_Department where value = '" + dept + "'","P_Department_ID");
				int ActID =  getSpecifiedID("select C_Activity_ID from C_Activity where value = '" + act + "'","C_Activity_ID");

				if ( codeGain.equals("30") || codeGain.equals("31") || codeGain.equals("90"))
				{
					P_Particular_Credits sheet = new P_Particular_Credits(Env.getCtx(),-1,null);
					if ((GainID != 0 ) && (EmplID != 0 ) && (CieID != 0 ) && (DeptID != 0 ) && (ActID != 0 ))
					{
						int P_Credits_ID = 0;
						if ( codeGain.equals("30") ) P_Credits_ID = getSpecifiedID("select P_Credits_ID from P_Credits where value = '3000'", "P_Credits_ID");
					    if ( codeGain.equals("31") ) P_Credits_ID = getSpecifiedID("select P_Credits_ID from P_Credits where value = '4000'", "P_Credits_ID");
					    if ( codeGain.equals("90") ) P_Credits_ID = getSpecifiedID("select P_Credits_ID from P_Credits where value = '2000'", "P_Credits_ID");
						sheet.setAD_Org_ID(CieID);
						sheet.setP_Employee_ID(EmplID);
						sheet.setP_Gain_ID(GainID);
						sheet.setP_Credits_ID(P_Credits_ID);
						sheet.setDayQty(unitBD);
						sheet.setDay( Period.getEndDate());
						sheet.setP_Period_ID( Period.getP_Period_ID());
						sheet.setOrigine( fileName );
						sheet.save();
						return "1";
					}
					else
					{
						return Line + " - " + Msg.translate(Env.getCtx(), "InvLineValue");
					}
					
				}
				else
				{
					P_Particular_Sheet sheet = new P_Particular_Sheet(Env.getCtx(),-1,null);
					if ((GainID != 0 ) && (EmplID != 0 ) && (CieID != 0 ) && (DeptID != 0 ) && (ActID != 0 ))
					{
						sheet.setAD_Org_ID(CieID);
						sheet.setP_Department_ID(DeptID);
						sheet.setC_Activity_ID(ActID);
						sheet.setP_Employee_ID(EmplID);
						sheet.setP_Gain_ID(GainID);
						if (unit != null)
						{
							sheet.setDayQty(unitBD);
						}
						else if (montant != null)
						{
							sheet.setDayQty(montantBD);
						}
						sheet.setHourly_Rate(tauxBD);
						sheet.setDay( Period.getEndDate());
						sheet.setP_Period_ID( Period.getP_Period_ID());
						sheet.setOrigine( fileName );
						sheet.save();
						return "1";
					}
					else
					{
						return Line + " - " + Msg.translate(Env.getCtx(), "InvLineValue");
					}
					
				}
			}
			else
			{
				return Line + " - " + Msg.translate(Env.getCtx(), "InvGainCode");
			}
		}
		return "0";
	}	//	cmd_loadFile
	
	private StringBuffer convertSpecialCaracter(StringBuffer value)
	{
		char[] caracter = {'}','J','K','L','M','N','O', 'P', 'Q', 'R'};
		char[] carReplace = {'0','1','2','3','4','5','6', '7', '8', '9'};
		int last = value.length() - 1;

		boolean found = false;
		char toAppend = ' ';
		for (int i = 0; i < caracter.length; i++)
		{
			if (caracter[i] == value.charAt(last))
			{
				found = true ; //value.replace(last, last+1, carReplace[i]);
				toAppend = carReplace[i];
				break;
//				i = caracter.length;
			}
		}		

		if ( found )//( value.charAt(value.length() -1 ) == '-' )
		{
			value = new StringBuffer ( "-" + value.substring(0, value.length() -1) );
			value.append(toAppend);
		}
	
		return value;
	}
	
	private int getSpecifiedID(String sql, String colID)
	{
		int rep = 0;
		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				rep =  rs.getInt(colID);
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		return rep;
	}

		

	
}	//	ImportAS400
