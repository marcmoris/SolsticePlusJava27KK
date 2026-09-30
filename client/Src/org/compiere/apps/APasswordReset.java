/******************************************************************************
 * Product: Solstice+ Payroll & Human Resources Management                     *
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
 * ProGestion Informatique, 5300 Bld des Galerie, Suite 210, Quebec, G2K 2A2 Canada      *
 * or via info@progestion.net or http://www.progestion.net/license.html       *
 *****************************************************************************/

package org.compiere.apps;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import java.sql.*;

import org.compiere.*;
import org.compiere.db.*;
import org.compiere.model.MUser;
import org.compiere.swing.*;
import org.compiere.util.*;

import java.util.regex.*;
import solstice.process.PgiUtil;

/**
 * A modal dialog that asks the user for a user name and password.
 * More information about this class is available from <a target="_top" href=
 * "http://ostermiller.org/utils/APasswordReset.html">ostermiller.org</a>.
 *
 * <code>
 * <pre>
 * APasswordReset p = new APasswordReset(null, "Test");
 * if(p.showDialog()){
 *     System.out.println("Name: " + p.getName());
 *     System.out.println("Pass: " + p.getPass());
 * } else {
 *     System.out.println("User selected cancel");
 * }
 * </pre>
 * </code>
 *
 * @author Stephen Ostermiller http://ostermiller.org/contact.pl?regarding=Java+Utilities
 * @since ostermillerutils 1.00.00
 * 
 * 
 	alter table ad_user add [PasswdUnexpirable] [char](1) NULL DEFAULT ('N'),
	alter table ad_user add [MustChangePassword] [char](1) NULL DEFAULT ('N'),
    alter table ad_user add PasswordFinished DateTime

    alter table ad_user add isFullBPAccess char(1)
    alter table ad_user add LDAPUser char(1)

 */
public class APasswordReset extends CDialog 
{

	/**
	 * Serial version id
	 */
	private static final long serialVersionUID = -832548326686122133L;

	/**
	 * Locale specific strings displayed to the user.
	 *
	 */
	protected ResourceBundle labels;


	/**
	 * Where the name is typed.
	 *
	 */
	private CTextField userTextField = new CTextField( " ", 20 );
	/**
	 * Where the password is typed.
	 *
	 */
	protected JPasswordField passwordFieldOld  = new JPasswordField( " ", 20);
	protected JPasswordField passwordFieldNew1 = new JPasswordField( " ", 20);
	protected JPasswordField passwordFieldNew2 = new JPasswordField( " ", 20);

	/**
	 * The label for the field in which the name is typed.
	 *
	 */
	private CLabel userLabel = new CLabel();
	/**
	 * The label for the field in which the password is typed.
	 *
	 */
	private CLabel passwordLabelOld = new CLabel();
	private CLabel passwordLabelNew1 = new CLabel();
	private CLabel passwordLabelNew2 = new CLabel();


	/**
	 * Set the password that appears as the default
	 * An empty string will be used if this in not specified
	 * before the dialog is displayed.
	 *
	 * @param pass default password to be displayed.
	 *
	 */
	public void setPass(String pass){
		this.passwordFieldOld.setText(pass);
		this.passwordFieldNew1.setText("");
		this.passwordFieldNew2.setText("");
	}


	/**
	 * Set the label for the field in which the name is entered.
	 * The default is a localized string.
	 *
	 * @param name label for the name field.
	 *
	 */
	public void setuserLabel(String name){
		this.userLabel.setText(name);
		pack();
	}


	/**
	 * Get the name that was entered into the dialog before
	 * the dialog was closed.
	 *
	 * @return the name from the name field.
	 *
	 */
	@Override public String getName(){
		return userTextField.getText();
	}


	/**
	 * Finds out if user used the OK button or an equivalent action
	 * to close the dialog.
	 * Pressing enter in the password field may be the same as
	 * 'OK' but closing the dialog and pressing the cancel button
	 * are not.
	 *
	 * @return true if the the user hit OK, false if the user canceled.
	 *
	 */
	public boolean okPressed(){
		return pressed_OK;
	}

	/**
	 * update this variable when the user makes an action
	 *
	 */
	private boolean pressed_OK = false;

	
	/**
	 * Create this dialog with the given parent and the default title.
	 *
	 * @param parent window from which this dialog is launched
	 *
	 */
	public APasswordReset(Frame parent) {
		super (parent, Label( "STR_PWDMANAGEMENT"), true);	//	Modal
		log.finer("");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		m_WindowNo = Env.createWindowNo (null);
		res = ResourceBundle.getBundle(RESOURCE);
		//
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			log.severe(e.toString());
		}
		//  Focus to OK
		this.getRootPane().setDefaultButton(confirmPanel.getOKButton());

		parent.setIconImage(Compiere.getImage16());
	}

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(APasswordReset.class);
	protected static final String RESOURCE = "org.compiere.apps.ALoginRes";
	private static ResourceBundle res = ResourceBundle.getBundle(RESOURCE);
	
	
	/**	Window No				*/
	private int			    m_WindowNo;
	/** Context					*/
	private Properties      m_ctx = Env.getCtx();
	private String 			trxName = null; 

	public boolean initResetPassword()
	{

		//  Application/PWD
		userTextField.setText(Ini.getProperty(Ini.P_UID));
		if (Ini.isPropertyBool(Ini.P_STORE_PWD))
			passwordFieldOld.setText(Ini.getProperty(Ini.P_PWD));
		else
			passwordFieldOld.setText("");

		userTextField.setEditable(false);
		passwordFieldOld.setEditable(false);
		passwordFieldNew1.setText("");
		passwordFieldNew2.setText("");

		//
		return true;
	}

	private CPanel connectionPanel = new CPanel();
	private GridBagLayout connectionLayout = new GridBagLayout();
	private CPanel southPanel = new CPanel();
	private ConfirmPanel confirmPanel = new ConfirmPanel(true, false, false, false, false, false, false);
	private CPanel mainPanel = new CPanel(new BorderLayout());
	private BorderLayout southLayout = new BorderLayout();
	private StatusBar statusBar = new StatusBar();


	/**	Reset OK			*/
	private boolean		    m_ResetOK = false;


	private void jbInit() throws Exception
	{

		if (labels == null){
			setLocale(Locale.getDefault());
		}

		southPanel.setLayout(southLayout);
		


		userLabel.setText(res.getString("User"));
		userLabel.setToolTipText(res.getString("EnterUser"));
		userLabel.setHorizontalTextPosition(JLabel.LEFT);

		passwordLabelOld.setText(  Label( "STR_ENTEROLDPASS"));
		passwordLabelOld.setToolTipText(Label( "STR_ENTEROLDPASS"));
		passwordLabelOld.setHorizontalTextPosition(JLabel.LEFT);

		

		passwordLabelNew1.setText(Label( "STR_ENTERNEWPASS1"));
		passwordLabelNew1.setToolTipText(Label( "STR_ENTERNEWPASS1"));
		passwordLabelNew1.setHorizontalTextPosition(JLabel.LEFT);

		passwordLabelNew2.setText(Label( "STR_ENTERNEWPASS2"));
		passwordLabelNew2.setToolTipText(Label( "STR_ENTERNEWPASS2"));
		passwordLabelNew2.setHorizontalTextPosition(JLabel.LEFT);

		super.dialogInit();


		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		c.insets.top = 5;
		c.insets.bottom = 5;
		
		connectionPanel.setLayout(connectionLayout);

		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 5, 20));
		c.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(userLabel, c);
		connectionPanel.add(userLabel,        new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 12, 5, 5), 0, 0));

		gridbag.setConstraints(userTextField, c);
		userTextField.addActionListener(this);
		connectionPanel.add(userTextField,         new GridBagConstraints(1, 3, 3, 1, 1.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 12), 0, 0));

		c.gridy = 1;
		gridbag.setConstraints(passwordLabelOld, c);
		connectionPanel.add(passwordLabelOld,         new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 5), 0, 0));

		gridbag.setConstraints(passwordFieldOld, c);
		passwordFieldOld.addActionListener(this);
		connectionPanel.add(passwordFieldOld,         new GridBagConstraints(1, 4, 3, 1, 1.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 12), 0, 0));

		c.gridy = 2;

		gridbag.setConstraints(passwordLabelNew1, c);
		connectionPanel.add(passwordLabelNew1,         new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 5), 0, 0));

		gridbag.setConstraints(passwordFieldNew1, c);
		passwordFieldNew1.addActionListener(this);
		connectionPanel.add(passwordFieldNew1,         new GridBagConstraints(1, 5, 3, 1, 1.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 12), 0, 0));

		c.gridy = 3;

		gridbag.setConstraints(passwordLabelNew2, c);
		connectionPanel.add(passwordLabelNew2,         new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 5), 0, 0));

		gridbag.setConstraints(passwordFieldNew2, c);
		passwordFieldNew2.addActionListener(this);
		connectionPanel.add(passwordFieldNew2,         new GridBagConstraints(1, 6, 3, 1, 1.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 12), 0, 0));

		southPanel.add(confirmPanel, BorderLayout.NORTH);
		southPanel.add(statusBar, BorderLayout.SOUTH);
		confirmPanel.addActionListener(this);

		confirmPanel.getOKButton().setToolTipText(res.getString("Ok"));
		confirmPanel.getCancelButton().setToolTipText(res.getString("Cancel"));

		statusBar.setStatusDB(null);
		statusBar.setStatusLine( WarningMessage( "STR_PWDEXPIREMSG", "" ), true);

		
		c.gridy = 4;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.CENTER;

		mainPanel.add(connectionPanel);

		
		mainPanel.add(southPanel, BorderLayout.SOUTH);

		
		getContentPane().add(mainPanel);


		
		pack();
	}

	/**************************************************************************
	 *	Action Event handler
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals(ConfirmPanel.A_OK))
		{
				defaultsOK();	//	disposes
		}
		else if (e.getActionCommand().equals(ConfirmPanel.A_CANCEL))
			appExit();
		//
	}	//	actionPerformed

	/*************************************************************************
	 *	Exit action performed
	 */
	private void appExit()
	{
		m_ResetOK = false;
		dispose();
	}	//	appExit_actionPerformed


	/**************************************************************************
	 *	Defaults OK pressed
	 *	@return true if ok
	 */
	private boolean defaultsOK ()
	{
		log.info("");

		String warningMessage = null;
		
		String oldPassword = String.valueOf(passwordFieldOld.getPassword()); 
		String newPassword = String.valueOf(passwordFieldNew1.getPassword()); 
		String newPassword2 = String.valueOf(passwordFieldNew2.getPassword()); 
		
		// make reset password.
		warningMessage = CheckPasswordSyntax( oldPassword, newPassword, newPassword2 );
		
		if ( warningMessage != null)
		{
			m_ResetOK = false;
			statusBar.setStatusLine(warningMessage, true);
		}
		else
		{
			m_ResetOK = true;
			MUser User = MUser.get( Env.getCtx(), Env.getAD_User_ID(Env.getCtx()));
//			User.setPassword(  SecureEngine.hashPassword( newPassword ) );
			User.setPassword(   newPassword  );
			Timestamp PasswordFinished = TimeUtil.getDay(TimeUtil.getToday().getTimeInMillis() );
			PasswordFinished = TimeUtil.addDays( PasswordFinished,  Integer.parseInt( PgiUtil.getSolsticeParameter(Env.getCtx(), "PWD_DAYSEXPIRE")));
			User.setPasswordFinished( PasswordFinished );
			User.setMustChangePassword(false);
			User.save();

		}
		
		//  Close - we are done
		if (m_ResetOK)
			this.dispose();

		return m_ResetOK;
	}	//	defaultsOK

	private static String Label( String msg  )
	{
        String Label = Msg.getMsg(Env.getCtx(), msg);

//        Label = Label.replace(msg, "");
		return Label;
	}

	private String WarningMessage( String msg , String info )
	{
        String warningMessage = Msg.getMsg(Env.getCtx(), msg);

        if ( info != null || info.length() != 0)
        	warningMessage = warningMessage.replace("%1", info);

//        warningMessage = warningMessage.replace(msg, "");
		return warningMessage;
	}
	
	
    private String CheckPasswordSyntax(String strOldPass, String strNewPass, String strNewPass2)
    {
    	

       String warningMessage = null;

		if ( String.valueOf( passwordFieldNew1.getPassword() ).compareTo( strNewPass2 ) != 0)
			return  WarningMessage("STR_INVALIDNEWPASS_NOTEQUALNEWPASS", "" );

       
       //si le les politiques de sécurité sont activées
       if(PgiUtil.getSolsticeParameter(Env.getCtx(), "PWD_ACTIVE").equals("1"))
       {
    	  Pattern  patternAlpha   = Pattern.compile("[a-zA-Z]");
    	  Pattern  patternNumeric = Pattern.compile("[0-9]");
    	  Pattern  patternUpper   = Pattern.compile("[A-Z]");

          Matcher mAlpha   = patternAlpha.matcher(strNewPass);
          Matcher mNumeric = patternNumeric.matcher(strNewPass);
          Matcher mUpper   = patternUpper.matcher(strNewPass);

          boolean isAlpha = mAlpha.find();
          boolean isNumeric = mNumeric.find();
          boolean isUpper = mUpper.find();

    
          //si le mot de passe est plus grand que 32
          if(strNewPass.length() > 32)
          {
             warningMessage = WarningMessage("STR_ERRNBMINCARLEN", "");
          }
          
          //si le mot de passe ne contient pas le nb de caractère requis
          int minChars = Integer.parseInt(PgiUtil.getSolsticeParameter(Env.getCtx(),"PWD_NBMINCHAR"));
          if(strNewPass.length() < minChars )
          {
             warningMessage = WarningMessage("STR_ERRNBMAXCARLEN", String.valueOf(minChars));
          }
          //si le mot de passe doit contenir un caractère alphabétique et n'en contient pas
          else if(PgiUtil.getSolsticeParameter(Env.getCtx(),"PWD_CHARALPHA").equals("1") && !isAlpha )
          {

             warningMessage = WarningMessage("STR_ERRPWDCARACT", WarningMessage("STR_AALPHACHAR", "") );
          }
          //si le mot de passe doit contenir un caractère numérique et n'en contient pas
          else if(PgiUtil.getSolsticeParameter(Env.getCtx(), "PWD_CHARNUM").equals("1") && !isNumeric)
          {

             warningMessage = WarningMessage("STR_ERRPWDCARACT", WarningMessage("STR_ANUMCHAR", ""));

          }
          //si le mot de passe doit contenir un caractère alphabétique en majuscule
          else if(PgiUtil.getSolsticeParameter(Env.getCtx(), "PWD_CHARUPPER").equals("1") && !isUpper)
          {

             warningMessage = WarningMessage("STR_ERRPWDCARACT", WarningMessage("STR_AUPPERCHAR", ""));

          }

          // The new password could not be identical of one of your recent password.
          else if  (  strOldPass.compareTo( strNewPass  ) == 0 ) 
          {
              warningMessage = WarningMessage("STR_ERRSAMEPWD", "");
          }
          
/*          //si le mot de passe doit contenir un caractère spécial
          else if(PgiUtil.getSolsticeParameter(Env.getCtx(), "PWD_CHARSPECIAL").equals("1"))
          {
        	 CharSequence charspeciaux = PgiUtil.getSolsticeParameter(Env.getCtx(), "STR_LISTSPECIALCHAR" );

             boolean found = false;
             if ( strNewPass.contains(charspeciaux))
                 found = true;
          
             if(!found)
             {

                String innerMessage = WarningMessage("STR_ONEOFTHOSECHAR", WarningMessage("STR_LISTSPECIALCHAR", ""));
                warningMessage = WarningMessage("STR_ERRPWDCARACT", innerMessage);
             }
          }
*/

       }
       return warningMessage;
    }

	/**
	 * 	Did the user press OK
	 *	@return true if user pressed final OK button
	 */
	public boolean isResetOK()
	{
		return m_ResetOK;
	}	//	isOKpressed

	
}	//	APasswordReset
