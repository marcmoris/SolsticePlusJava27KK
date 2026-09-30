/******************************************************************************
 * Product: Solstice+ Payroll & Human Resources Management                    *
 * Copyright (C) 2008-2026 ProGestion Informatique, Inc. All Rights Reserved. *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 ******************************************************************************/

package org.solstice.apps.form;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.compiere.apps.ADialog;
import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.grid.ed.VDate;
import org.compiere.model.MActivity;
import org.compiere.model.MLocation;
import org.compiere.model.MOrg;
import org.compiere.model.MRegion;
import org.compiere.swing.CPanel;
import org.compiere.swing.CTextField;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.ValueNamePair;

import solstice.model.P_Assignment;
import solstice.model.P_Employee;
import solstice.model.P_Employee_Roe;
import solstice.model.P_Employer;
import solstice.model.P_Frequency;
import solstice.model.P_Job_Title;
import solstice.model.P_Job_Type;
import solstice.model.P_Payment_Group;
import solstice.model.P_Period;
import solstice.model.P_Post;
import solstice.utils.PgiUtil;

/**
 * Ecran graphique Swing du Relev\u00e9 d'Emploi (ROE) canadien.
 * Reproduit fid\u00e8lement le formulaire officiel avec ses 22 bo\u00eetes num\u00e9rot\u00e9es,
 * son agencement en BorderLayout et ses actions m\u00e9tier.
 * 
 * Remplace l'ancien flux web JSP (releve.jsp / index.jsp).
 * 
 * @author Solstice / Antigravity
 */
public class VReleveEmploi extends CPanel implements FormPanel, ActionListener 
{
    private static final long serialVersionUID = 1L;
    private static final CLogger s_log = CLogger.getCLogger(VReleveEmploi.class);

    // Couleurs officielles du formulaire
    private static final Color BOX_HEADER_BG = new Color(214, 212, 172); // Teinte olive-beige officielle
    private static final Color BOX_BORDER_COLOR = new Color(130, 130, 130);
    private static final Color BADGE_BG = Color.BLACK;
    private static final Color BADGE_FG = Color.WHITE;

    private int m_WindowNo = 0;
    private FormFrame m_frame;
    private int m_roeId = -1;

    private P_Employee_Roe m_employeeRoe;
    private P_Employee m_employee;
    private P_Employer m_employer;

    // --- Composants de l'interface ---
    private JCheckBox chkOvertimeIncluded;

    // Bo\u00eete 1, 2, 3
    private JLabel lblBox1_Serial;
    private JTextField txtBox2_ReplacedSerial;
    private JLabel lblBox3_EmployerRef;

    // Bo\u00eete 4, 5, 6
    private JTextArea txtBox4_EmployerAddress;
    private JTextField txtBox5_CRA_BN;
    private JComboBox<ValueNamePair> cboBox6_PayPeriodType;

    // Bo\u00eete 7, 8
    private JTextField txtBox7_PostalCode;
    private JTextField txtBox8_SIN;

    // Bo\u00eete 9, 10, 11, 12
    private JTextArea txtBox9_EmployeeAddress;
    private VDate dateBox10_FirstDayWorked;
    private VDate dateBox11_LastDayPaid;
    private VDate dateBox12_FinalPayPeriodEnding;

    // Bo\u00eete 13, 14
    private JTextField txtBox13_Occupation;
    private JRadioButton rdoRecallDate;
    private JRadioButton rdoRecallNone;
    private JRadioButton rdoRecallUnknown;
    private ButtonGroup bgRecall;
    private VDate dateBox14_RecallDate;

    // Bo\u00eete 15A, 15B, 16
    private JTextField txtBox15a_TotalHours;
    private JTextField txtBox15b_TotalEarnings;
    private JComboBox<ValueNamePair> cboBox16_Reason;
    private JTextField txtBox16_ContactFirstName;
    private JTextField txtBox16_ContactLastName;
    private JTextField txtBox16_AreaCode;
    private JTextField txtBox16_Phone;
    private JTextField txtBox16_PhoneExt;

    // Bo\u00eete 15C (P\u00e9riodes 01 \u00e0 53)
    private JTextField[] txtPeriodEarnings = new JTextField[53];

    // Bo\u00eete 17 (Vacances, F\u00e9ri\u00e9s, Autres sommes)
    private JComboBox<ValueNamePair> cboBox17_VacationCode;
    private JTextField txtBox17_VacationAmount;
    private VDate dateBox17_VacationStart;
    private VDate dateBox17_VacationEnd;

    private VDate[] dateStatutoryHolidays = new VDate[10];
    private JTextField[] txtStatutoryHolidayAmounts = new JTextField[10];

    private JComboBox<ValueNamePair>[] cboOtherMoniesCode = new JComboBox[3];
    private VDate[] dateOtherMoniesStart = new VDate[3];
    private VDate[] dateOtherMoniesEnd = new VDate[3];
    private JTextField[] txtOtherMoniesAmount = new JTextField[3];

    // Bo\u00eete 18 (Observations 4 lignes)
    private JTextField[] txtBox18_Comments = new JTextField[4];

    // Bo\u00eete 19 (Paiements sp\u00e9ciaux 4 types)
    private VDate[] dateSpecialPaymentStart = new VDate[4];
    private VDate[] dateSpecialPaymentEnd = new VDate[4];
    private JTextField[] txtSpecialPaymentAmount = new JTextField[4];
    private JRadioButton[] rdoSpecialPaymentDay = new JRadioButton[4];
    private JRadioButton[] rdoSpecialPaymentWeek = new JRadioButton[4];

    // Bo\u00eete 20, 21, 22, 23
    private JComboBox<KeyNamePair> cboBox20_PrefLanguage;
    private JComboBox<KeyNamePair> cboBox23_PrintLanguage;
    private JTextField txtBox21_Phone;
    private JTextField txtBox21_PhoneExt;
    private JTextField txtBox22_Signatory;

    // Boutons de commande
    private JButton bSearch;
    private JButton bDelete;
    private JButton bSave;
    private JButton bUpdate;
    private JButton bCopy;
    private JButton bExport;

    /**
     * Constructeur par d\u00e9faut
     */
    public VReleveEmploi() 
    {
    }

    /**
     * Constructeur avec ID de relev\u00e9 d'emploi
     */
    public VReleveEmploi(int roeId) 
    {
        this.m_roeId = roeId;
    }

    /**
     * Initialisation du FormPanel
     */
    public void init(int WindowNo, FormFrame frame) 
    {
        this.m_WindowNo = WindowNo;
        this.m_frame = frame;
        
        // Attacher le panneau au FormFrame de Compiere
        if (m_frame != null) 
        {
            m_frame.setTitle(Msg.getMsg(Env.getLanguage(Env.getCtx()), "ROE_Title", true));
            m_frame.setPreferredSize(new Dimension(920, 840));
            m_frame.getContentPane().add(this, BorderLayout.CENTER);
        }

        try 
        {
            jbInit();
        } 
        catch (Exception e) 
        {
            s_log.log(Level.SEVERE, "VReleveEmploi.jbInit", e);
        }

        try 
        {
            dynInit();
        } 
        catch (Exception e) 
        {
            s_log.log(Level.SEVERE, "VReleveEmploi.dynInit", e);
        }

        if (m_frame != null) 
        {
            m_frame.validate();
            m_frame.repaint();
        }

        // Afficher automatiquement la fen\u00eatre de recherche \u00e0 l'ouverture de l'\u00e9cran
        SwingUtilities.invokeLater(new Runnable() 
        {
            public void run() 
            {
                showSearchDialog();
            }
        });
    }

    /**
     * Lib\u00e9ration des ressources
     */
    public void dispose() 
    {
        if (m_frame != null)
            m_frame.dispose();
        m_frame = null;
    }

    /**
     * Construction de l'interface graphique Swing
     */
    private void jbInit() throws Exception 
    {
        this.setLayout(new BorderLayout(0, 4));
        this.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

        // 1. Haut : Option temps suppl\u00e9mentaire
        chkOvertimeIncluded = new JCheckBox("Inclure les heures de temps suppl\u00e9mentaire mise en banque durant la p\u00e9riode du pr\u00e9sent relev\u00e9.");
        chkOvertimeIncluded.setFont(new Font("SansSerif", Font.BOLD, 12));
        this.add(chkOvertimeIncluded, BorderLayout.NORTH);

        // 2. Centre : Formulaire principal dans un JScrollPane
        JPanel formContainer = new JPanel(new GridBagLayout());
        formContainer.setBackground(Color.WHITE);
        buildFormLayout(formContainer);

        JScrollPane scrollPane = new JScrollPane(formContainer);
        scrollPane.getVerticalScrollBar().setUnitIncrement(18);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        this.add(scrollPane, BorderLayout.CENTER);

        // 3. Bas : Barre de commandes avec les 6 boutons
        JPanel commandPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        bSearch = new JButton("Chercher");
        bDelete = new JButton("Supprimer");
        bSave = new JButton("Enregistrer");
        bUpdate = new JButton("Mise \u00e0 jour");
        bCopy = new JButton("Copier");
        bExport = new JButton("Export");

        JButton[] buttons = { bSearch, bDelete, bSave, bUpdate, bCopy, bExport };
        for (JButton btn : buttons) 
        {
            btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
            btn.addActionListener(this);
            commandPanel.add(btn);
        }
        this.add(commandPanel, BorderLayout.SOUTH);
    }

    /**
     * Construction structur\u00e9e des 22 bo\u00eetes en 2 colonnes parfaitement align\u00e9es
     */
    private void buildFormLayout(JPanel p) 
    {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(2, 2, 2, 2);

        // LIGNE 1 (y=0) : Bo\u00eetes 1 & 2 (gauche) vs Bo\u00eete 3 (droite)
        JPanel p1_2 = new JPanel(new GridBagLayout());
        p1_2.setOpaque(false);
        GridBagConstraints g12 = new GridBagConstraints();
        g12.fill = GridBagConstraints.BOTH;
        g12.weighty = 1.0;

        lblBox1_Serial = new JLabel(" ", SwingConstants.CENTER);
        lblBox1_Serial.setFont(new Font("SansSerif", Font.BOLD, 12));
        JPanel p1 = createBox("1", "No de s\u00e9rie", false, createSingleComponentPanel(lblBox1_Serial));

        txtBox2_ReplacedSerial = new JTextField(8);
        JPanel p2 = createBox("2", "<html>Num\u00e9ro de s\u00e9rie du RE<br>modifi\u00e9 ou remplac\u00e9</html>", false, createSingleComponentPanel(txtBox2_ReplacedSerial));

        g12.gridx = 0; g12.weightx = 0.35; g12.insets = new Insets(0, 0, 0, 1); p1_2.add(p1, g12);
        g12.gridx = 1; g12.weightx = 0.65; g12.insets = new Insets(0, 1, 0, 0); p1_2.add(p2, g12);

        lblBox3_EmployerRef = new JLabel(" ", SwingConstants.LEFT);
        lblBox3_EmployerRef.setFont(new Font("SansSerif", Font.PLAIN, 12));
        JPanel p3 = createBox("3", "<html>No de r\u00e9f\u00e9rence du registre de paie<br>de l'employeur</html>", false, createSingleComponentPanel(lblBox3_EmployerRef));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1; gbc.weightx = 0.38; p.add(p1_2, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 1; gbc.weightx = 0.62; p.add(p3, gbc);

        // LIGNE 2 (y=1..2) : Bo\u00eete 4 (gauche, hauteur 2) vs Bo\u00eetes 5 & 6 (droite)
        txtBox4_EmployerAddress = new JTextArea(4, 22);
        txtBox4_EmployerAddress.setEditable(false);
        txtBox4_EmployerAddress.setLineWrap(true);
        txtBox4_EmployerAddress.setFont(new Font("SansSerif", Font.PLAIN, 11));
        JPanel p4 = createBox("4", "Nom et adresse de l'employeur", false, new JScrollPane(txtBox4_EmployerAddress));

        txtBox5_CRA_BN = new JTextField(15);
        txtBox5_CRA_BN.setEditable(false);
        JPanel p5 = createBox("5", "Num\u00e9ro d'entreprise (NE) de l'ADRC", false, createSingleComponentPanel(txtBox5_CRA_BN));

        cboBox6_PayPeriodType = new JComboBox<ValueNamePair>();
        initPayPeriodTypes(cboBox6_PayPeriodType);
        JPanel p6 = createBox("6", "Genre de p\u00e9riode de paye", true, createSingleComponentPanel(cboBox6_PayPeriodType));

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.gridheight = 2; gbc.weightx = 0.38; p.add(p4, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 1; gbc.gridheight = 1; gbc.weightx = 0.62; p.add(p5, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 1; gbc.gridheight = 1; gbc.weightx = 0.62; p.add(p6, gbc);
        gbc.gridheight = 1;

        // LIGNE 3 (y=3) : Bo\u00eete 7 (gauche) vs Bo\u00eete 8 (droite)
        txtBox7_PostalCode = new JTextField(8);
        txtBox7_PostalCode.setEditable(false);
        JPanel p7 = createBox("7", "Code Postal", false, createSingleComponentPanel(txtBox7_PostalCode));

        txtBox8_SIN = new JTextField(12);
        txtBox8_SIN.setEditable(false);
        JPanel p8 = createBox("8", "N.A.S", true, createSingleComponentPanel(txtBox8_SIN));

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; gbc.weightx = 0.38; p.add(p7, gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 1; gbc.weightx = 0.62; p.add(p8, gbc);

        // LIGNE 4 (y=4..6) : Bo\u00eete 9 (gauche, hauteur 3) vs Bo\u00eetes 10, 11, 12 (droite)
        txtBox9_EmployeeAddress = new JTextArea(4, 22);
        txtBox9_EmployeeAddress.setEditable(false);
        txtBox9_EmployeeAddress.setLineWrap(true);
        txtBox9_EmployeeAddress.setFont(new Font("SansSerif", Font.PLAIN, 11));
        JPanel p9 = createBox("9", "Nom et adresse de l'employ\u00e9(e)", true, new JScrollPane(txtBox9_EmployeeAddress));

        dateBox10_FirstDayWorked = new VDate();
        JPanel p10 = createBox("10", "Premier jour de travail", true, createSingleComponentPanel(dateBox10_FirstDayWorked));

        dateBox11_LastDayPaid = new VDate();
        JPanel p11 = createBox("11", "Dernier jour pay\u00e9", true, createSingleComponentPanel(dateBox11_LastDayPaid));

        dateBox12_FinalPayPeriodEnding = new VDate();
        JPanel p12 = createBox("12", "Date de fin de la derni\u00e8re p\u00e9riode de paie", true, createSingleComponentPanel(dateBox12_FinalPayPeriodEnding));

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1; gbc.gridheight = 3; gbc.weightx = 0.38; p.add(p9, gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 1; gbc.gridheight = 1; gbc.weightx = 0.62; p.add(p10, gbc);
        gbc.gridx = 1; gbc.gridy = 5; gbc.gridwidth = 1; gbc.gridheight = 1; gbc.weightx = 0.62; p.add(p11, gbc);
        gbc.gridx = 1; gbc.gridy = 6; gbc.gridwidth = 1; gbc.gridheight = 1; gbc.weightx = 0.62; p.add(p12, gbc);
        gbc.gridheight = 1;

        // LIGNE 5 (y=7) : Bo\u00eete 13 (gauche) vs Bo\u00eete 14 (droite)
        txtBox13_Occupation = new JTextField(24);
        JPanel p13 = createBox("13", "Profession", false, createSingleComponentPanel(txtBox13_Occupation));

        JPanel pRecall = new JPanel(new GridBagLayout());
        GridBagConstraints rcGbc = new GridBagConstraints();
        rcGbc.anchor = GridBagConstraints.WEST;
        rcGbc.insets = new Insets(1, 4, 1, 4);

        rdoRecallDate = new JRadioButton("Date de rappel");
        rdoRecallNone = new JRadioButton("Retour non pr\u00e9vu");
        rdoRecallUnknown = new JRadioButton("Date non connue");
        bgRecall = new ButtonGroup();
        bgRecall.add(rdoRecallDate);
        bgRecall.add(rdoRecallNone);
        bgRecall.add(rdoRecallUnknown);

        dateBox14_RecallDate = new VDate();

        rcGbc.gridx = 0; rcGbc.gridy = 0; pRecall.add(rdoRecallDate, rcGbc);
        rcGbc.gridx = 1; rcGbc.gridy = 0; pRecall.add(dateBox14_RecallDate, rcGbc);
        rcGbc.gridx = 0; rcGbc.gridy = 1; rcGbc.gridwidth = 2; pRecall.add(rdoRecallNone, rcGbc);
        rcGbc.gridx = 0; rcGbc.gridy = 2; rcGbc.gridwidth = 2; pRecall.add(rdoRecallUnknown, rcGbc);

        JPanel p14 = createBox("14", "Date de rappel pr\u00e9vu", false, pRecall);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 1; gbc.weightx = 0.38; p.add(p13, gbc);
        gbc.gridx = 1; gbc.gridy = 7; gbc.gridwidth = 1; gbc.weightx = 0.62; p.add(p14, gbc);

        // LIGNE 6 & 7 (y=8..9) : Bo\u00eete 15A & 15B (gauche) vs Bo\u00eete 16 (droite, hauteur 2)
        txtBox15a_TotalHours = new JTextField("0", 8);
        txtBox15a_TotalHours.setHorizontalAlignment(JTextField.RIGHT);
        JPanel p15a = createBox("15A", "Heures assurables totales", true, createSingleComponentPanel(txtBox15a_TotalHours));

        txtBox15b_TotalEarnings = new JTextField("0", 8);
        txtBox15b_TotalEarnings.setHorizontalAlignment(JTextField.RIGHT);
        JPanel p15b = createBox("15B", "R\u00e9mun\u00e9ration assurable totale", true, createSingleComponentPanel(txtBox15b_TotalEarnings));

        JPanel pContact = new JPanel(new GridBagLayout());
        GridBagConstraints cGbc = new GridBagConstraints();
        cGbc.fill = GridBagConstraints.HORIZONTAL;
        cGbc.insets = new Insets(1, 3, 1, 3);

        cboBox16_Reason = new JComboBox<ValueNamePair>();
        initReasons(cboBox16_Reason);
        cGbc.gridx = 0; cGbc.gridy = 0; cGbc.gridwidth = 6; pContact.add(cboBox16_Reason, cGbc);

        JLabel lblContactInfo = new JLabel("Pour plus de renseignements appeler :");
        lblContactInfo.setFont(new Font("SansSerif", Font.PLAIN, 10));
        cGbc.gridx = 0; cGbc.gridy = 1; cGbc.gridwidth = 6; pContact.add(lblContactInfo, cGbc);

        txtBox16_ContactFirstName = new JTextField(10);
        txtBox16_ContactLastName = new JTextField(10);
        cGbc.gridx = 0; cGbc.gridy = 2; cGbc.gridwidth = 3; pContact.add(txtBox16_ContactFirstName, cGbc);
        cGbc.gridx = 3; cGbc.gridy = 2; cGbc.gridwidth = 3; pContact.add(txtBox16_ContactLastName, cGbc);

        txtBox16_AreaCode = new JTextField(3);
        txtBox16_Phone = new JTextField(10);
        txtBox16_PhoneExt = new JTextField(4);

        cGbc.gridwidth = 1;
        cGbc.gridx = 0; cGbc.gridy = 3; pContact.add(new JLabel("Ind. R\u00e9g :"), cGbc);
        cGbc.gridx = 1; cGbc.gridy = 3; pContact.add(txtBox16_AreaCode, cGbc);
        cGbc.gridx = 2; cGbc.gridy = 3; pContact.add(new JLabel("T\u00e9l. :"), cGbc);
        cGbc.gridx = 3; cGbc.gridy = 3; pContact.add(txtBox16_Phone, cGbc);
        cGbc.gridx = 4; cGbc.gridy = 3; pContact.add(new JLabel("ext. :"), cGbc);
        cGbc.gridx = 5; cGbc.gridy = 3; pContact.add(txtBox16_PhoneExt, cGbc);

        JPanel p16 = createBox("16", "Raison du pr\u00e9sent relev\u00e9", true, pContact);

        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 1; gbc.gridheight = 1; gbc.weightx = 0.38; p.add(p15a, gbc);
        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 1; gbc.gridheight = 1; gbc.weightx = 0.38; p.add(p15b, gbc);
        gbc.gridx = 1; gbc.gridy = 8; gbc.gridwidth = 1; gbc.gridheight = 2; gbc.weightx = 0.62; p.add(p16, gbc);
        gbc.gridheight = 1;

        // LIGNE 8 (y=10) : Bo\u00eete 15C (gauche - Grille des 53 p\u00e9riodes) vs Bo\u00eete 17 (droite - Vacances, F\u00e9ri\u00e9s, Autres sommes)
        JPanel pGrid15c = buildPeriodGridPanel();
        JPanel p15c = createBox("15C", "<html>\u00c0 compl\u00e9ter seulement s'il y a une p\u00e9riode<br>de paie sans r\u00e9mun\u00e9ration assurable.</html>", false, pGrid15c);

        JPanel pDetails17 = buildBox17Panel();
        JPanel p17 = createBox("17", "<html><b>Paiements autres que le salaire habituel</b><br><span style='font-size:9px; color:#444444;'>Vacances, f\u00e9ri\u00e9s, autres sommes pay\u00e9es lors de ou apr\u00e8s la derni\u00e8re p\u00e9riode</span></html>", false, pDetails17);

        gbc.gridx = 0; gbc.gridy = 10; gbc.gridwidth = 1; gbc.weightx = 0.38; p.add(p15c, gbc);
        gbc.gridx = 1; gbc.gridy = 10; gbc.gridwidth = 1; gbc.weightx = 0.62; p.add(p17, gbc);

        // LIGNE 9 (y=11) : Bo\u00eete 18 (gauche - Observations) vs Bo\u00eete 19 (droite - Cong\u00e9s / Indemnit\u00e9s)
        JPanel pComments18 = new JPanel(new GridLayout(4, 1, 2, 2));
        pComments18.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        for (int i = 0; i < 4; i++) 
        {
            txtBox18_Comments[i] = new JTextField(25);
            pComments18.add(txtBox18_Comments[i]);
        }
        JPanel p18 = createBox("18", "Observations", false, pComments18);

        JPanel pSpecial19 = buildBox19Panel();
        JPanel p19 = createBox("19", "<html><b>Cong\u00e9s et indemnit\u00e9s sp\u00e9ciales</b><br><span style='font-size:9px; color:#444444;'>Maladie, maternit\u00e9, parental ou assurance salaire</span></html>", false, pSpecial19);

        gbc.gridx = 0; gbc.gridy = 11; gbc.gridwidth = 1; gbc.weightx = 0.38; p.add(p18, gbc);
        gbc.gridx = 1; gbc.gridy = 11; gbc.gridwidth = 1; gbc.weightx = 0.62; p.add(p19, gbc);

        // LIGNE 10 (y=12) : Bo\u00eete 20 & 23 (gauche - Langues) vs Bo\u00eete 21 (droite - T\u00e9l\u00e9phone)
        JPanel pLang = new JPanel(new GridLayout(2, 2, 4, 4));
        pLang.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        pLang.add(new JLabel("20 Comm :"));
        cboBox20_PrefLanguage = new JComboBox<KeyNamePair>();
        pLang.add(cboBox20_PrefLanguage);
        pLang.add(new JLabel("23 Impr :"));
        cboBox23_PrintLanguage = new JComboBox<KeyNamePair>();
        pLang.add(cboBox23_PrintLanguage);
        JPanel p20_23 = createBox("20/23", "Langues", false, pLang);

        JPanel pPhone21 = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
        txtBox21_Phone = new JTextField(12);
        txtBox21_PhoneExt = new JTextField(5);
        pPhone21.add(txtBox21_Phone);
        pPhone21.add(new JLabel("ext."));
        pPhone21.add(txtBox21_PhoneExt);
        JPanel p21 = createBox("21", "No de t\u00e9l\u00e9phone", false, pPhone21);

        gbc.gridx = 0; gbc.gridy = 12; gbc.gridwidth = 1; gbc.weightx = 0.38; p.add(p20_23, gbc);
        gbc.gridx = 1; gbc.gridy = 12; gbc.gridwidth = 1; gbc.weightx = 0.62; p.add(p21, gbc);

        // LIGNE 11 (y=13) : Bo\u00eete 22 (D\u00e9claration et signature - pleine largeur)
        JPanel pSign22 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        txtBox22_Signatory = new JTextField(28);
        pSign22.add(new JLabel("Nom du signataire :"));
        pSign22.add(txtBox22_Signatory);
        JPanel p22 = createBox("22", "<html>Je reconnais que toute fausse d\u00e9claration constitue une infraction et j'atteste, par les pr\u00e9sentes,<br>que toutes les d\u00e9clarations de ce formulaire sont v\u00e9ridiques.</html>", false, pSign22);

        gbc.gridx = 0; gbc.gridy = 13; gbc.gridwidth = 2; gbc.weightx = 1.0; p.add(p22, gbc);

        // Remplissage bas pour aligner le formulaire vers le haut
        gbc.gridx = 0; gbc.gridy = 14; gbc.gridwidth = 2; gbc.weighty = 1.0;
        p.add(new JLabel(""), gbc);
    }

    /**
     * Grille 3 colonnes pour les 53 p\u00e9riodes de r\u00e9mun\u00e9ration (Bo\u00eete 15C)
     */
    private JPanel buildPeriodGridPanel() 
    {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(1, 1, 1, 1);

        // En-t\u00eates
        for (int col = 0; col < 3; col++) 
        {
            JLabel lblPP = new JLabel("P.P", SwingConstants.CENTER);
            lblPP.setFont(new Font("SansSerif", Font.BOLD, 10));
            gbc.gridx = col * 2; gbc.gridy = 0; gbc.weightx = 0.05; panel.add(lblPP, gbc);

            JLabel lblAmt = new JLabel("Montant", SwingConstants.CENTER);
            lblAmt.setFont(new Font("SansSerif", Font.BOLD, 10));
            gbc.gridx = col * 2 + 1; gbc.gridy = 0; gbc.weightx = 0.28; panel.add(lblAmt, gbc);
        }

        // 18 rang\u00e9es
        for (int row = 0; row < 18; row++) 
        {
            for (int col = 0; col < 3; col++) 
            {
                int periodNum = row * 3 + col + 1;
                if (periodNum <= 53) 
                {
                    JLabel lblNum = new JLabel(String.format("%02d", periodNum), SwingConstants.CENTER);
                    lblNum.setFont(new Font("SansSerif", Font.PLAIN, 10));

                    JTextField txtField = new JTextField("0", 5);
                    txtField.setHorizontalAlignment(JTextField.RIGHT);
                    txtField.setFont(new Font("SansSerif", Font.PLAIN, 11));
                    txtPeriodEarnings[periodNum - 1] = txtField;

                    gbc.gridx = col * 2; gbc.gridy = row + 1; gbc.weightx = 0.05; panel.add(lblNum, gbc);
                    gbc.gridx = col * 2 + 1; gbc.gridy = row + 1; gbc.weightx = 0.28; panel.add(txtField, gbc);
                }
            }
        }
        return panel;
    }

    /**
     * Bo\u00eete 17 : Vacances, F\u00e9ri\u00e9s, Autres sommes
     */
    private JPanel buildBox17Panel() 
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));

        // Section A) Paye de vacances
        JPanel pVac = new JPanel(new GridBagLayout());
        pVac.setBorder(BorderFactory.createTitledBorder("A) Paye de vacances"));
        GridBagConstraints gbcVac = new GridBagConstraints();
        gbcVac.fill = GridBagConstraints.HORIZONTAL;
        gbcVac.insets = new Insets(2, 3, 2, 3);

        cboBox17_VacationCode = new JComboBox<ValueNamePair>();
        initVacationCodes(cboBox17_VacationCode);
        cboBox17_VacationCode.setPreferredSize(new Dimension(185, 22));
        txtBox17_VacationAmount = new JTextField("0", 6);
        txtBox17_VacationAmount.setHorizontalAlignment(JTextField.RIGHT);

        dateBox17_VacationStart = new VDate();
        dateBox17_VacationEnd = new VDate();

        gbcVac.gridx = 0; gbcVac.gridy = 0; gbcVac.gridwidth = 3; pVac.add(cboBox17_VacationCode, gbcVac);
        gbcVac.gridx = 3; gbcVac.gridy = 0; gbcVac.gridwidth = 1; pVac.add(new JLabel("$"), gbcVac);
        gbcVac.gridx = 4; gbcVac.gridy = 0; gbcVac.gridwidth = 1; pVac.add(txtBox17_VacationAmount, gbcVac);

        gbcVac.gridx = 0; gbcVac.gridy = 1; gbcVac.gridwidth = 1; pVac.add(new JLabel("Date de d\u00e9but :"), gbcVac);
        gbcVac.gridx = 1; gbcVac.gridy = 1; gbcVac.gridwidth = 1; pVac.add(dateBox17_VacationStart, gbcVac);
        gbcVac.gridx = 2; gbcVac.gridy = 1; gbcVac.gridwidth = 1; pVac.add(new JLabel("Date de fin :"), gbcVac);
        gbcVac.gridx = 3; gbcVac.gridy = 1; gbcVac.gridwidth = 2; pVac.add(dateBox17_VacationEnd, gbcVac);

        panel.add(pVac);

        // Section B) Jour(s) f\u00e9ri\u00e9(s) (10 entr\u00e9es en 2 colonnes de 5)
        JPanel pHolidays = new JPanel(new GridLayout(5, 2, 4, 1));
        pHolidays.setBorder(BorderFactory.createTitledBorder("B) Jour(s) f\u00e9ri\u00e9(s)"));
        for (int i = 0; i < 10; i++) 
        {
            JPanel rowP = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 1));
            dateStatutoryHolidays[i] = new VDate();
            txtStatutoryHolidayAmounts[i] = new JTextField("0", 6);
            txtStatutoryHolidayAmounts[i].setHorizontalAlignment(JTextField.RIGHT);

            rowP.add(dateStatutoryHolidays[i]);
            rowP.add(new JLabel("$"));
            rowP.add(txtStatutoryHolidayAmounts[i]);
            pHolidays.add(rowP);
        }
        panel.add(pHolidays);

        // Section C) Autres sommes (3 lignes)
        JPanel pOther = new JPanel(new GridLayout(3, 1, 2, 2));
        pOther.setBorder(BorderFactory.createTitledBorder("C) Autres sommes (pr\u00e9cisez)"));
        for (int i = 0; i < 3; i++) 
        {
            JPanel rowP = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 1));
            cboOtherMoniesCode[i] = new JComboBox<ValueNamePair>();
            initOtherMoniesCodes(cboOtherMoniesCode[i]);
            cboOtherMoniesCode[i].setPreferredSize(new Dimension(175, 22));

            dateOtherMoniesStart[i] = new VDate();
            dateOtherMoniesEnd[i] = new VDate();
            txtOtherMoniesAmount[i] = new JTextField("0", 6);
            txtOtherMoniesAmount[i].setHorizontalAlignment(JTextField.RIGHT);

            rowP.add(cboOtherMoniesCode[i]);
            rowP.add(dateOtherMoniesStart[i]);
            rowP.add(dateOtherMoniesEnd[i]);
            rowP.add(new JLabel("$"));
            rowP.add(txtOtherMoniesAmount[i]);
            pOther.add(rowP);
        }
        panel.add(pOther);

        return panel;
    }

    /**
     * Bo\u00eete 19 : Cong\u00e9s maladie, maternit\u00e9, parental
     */
    private JPanel buildBox19Panel() 
    {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(1, 2, 1, 2);

        String[] headers = { "Type", "Date d\u00e9but", "Date fin", "Montant", "Par jour", "Par semaine" };
        for (int c = 0; c < headers.length; c++) 
        {
            JLabel h = new JLabel(headers[c], SwingConstants.CENTER);
            h.setFont(new Font("SansSerif", Font.BOLD, 10));
            gbc.gridx = c; gbc.gridy = 0; panel.add(h, gbc);
        }

        String[] types = { "CMP", "IAS - Non ass.", "IAS - Ass.", "MAT/PAR/CO/PEGM" };
        for (int i = 0; i < 4; i++) 
        {
            JLabel lblType = new JLabel(types[i]);
            lblType.setFont(new Font("SansSerif", Font.PLAIN, 11));

            dateSpecialPaymentStart[i] = new VDate();
            dateSpecialPaymentEnd[i] = new VDate();
            txtSpecialPaymentAmount[i] = new JTextField("0", 6);
            txtSpecialPaymentAmount[i].setHorizontalAlignment(JTextField.RIGHT);

            rdoSpecialPaymentDay[i] = new JRadioButton();
            rdoSpecialPaymentWeek[i] = new JRadioButton();
            ButtonGroup bg = new ButtonGroup();
            bg.add(rdoSpecialPaymentDay[i]);
            bg.add(rdoSpecialPaymentWeek[i]);
            rdoSpecialPaymentWeek[i].setSelected(true);

            gbc.gridy = i + 1;
            gbc.gridx = 0; panel.add(lblType, gbc);
            gbc.gridx = 1; panel.add(dateSpecialPaymentStart[i], gbc);
            gbc.gridx = 2; panel.add(dateSpecialPaymentEnd[i], gbc);
            gbc.gridx = 3; panel.add(txtSpecialPaymentAmount[i], gbc);
            gbc.gridx = 4; panel.add(rdoSpecialPaymentDay[i], gbc);
            gbc.gridx = 5; panel.add(rdoSpecialPaymentWeek[i], gbc);
        }

        return panel;
    }

    /**
     * Initialisation dynamique des donn\u00e9es et listes d\u00e9roulantes
     */
    private void dynInit() 
    {
        initLanguages();

        if (m_roeId > 0) 
        {
            loadData(m_roeId);
        } 
        else 
        {
            int ctxRoeId = Env.getContextAsInt(Env.getCtx(), m_WindowNo, "P_Employee_Roe_ID");
            if (ctxRoeId <= 0)
                ctxRoeId = Env.getContextAsInt(Env.getCtx(), "P_Employee_Roe_ID");

            if (ctxRoeId > 0) 
            {
                loadData(ctxRoeId);
            } 
            else 
            {
                int ctxEmpId = Env.getContextAsInt(Env.getCtx(), m_WindowNo, "P_Employee_ID");
                if (ctxEmpId <= 0)
                    ctxEmpId = Env.getContextAsInt(Env.getCtx(), "P_Employee_ID");

                if (ctxEmpId > 0) 
                {
                    int lastRoe = DB.getSQLValue(null, "SELECT MAX(P_Employee_Roe_ID) FROM P_Employee_Roe WHERE P_Employee_ID = " + ctxEmpId);
                    if (lastRoe > 0)
                        loadData(lastRoe);
                    else
                        createNewRoeForEmployee(ctxEmpId);
                } 
                else 
                {
                    int lastRoe = DB.getSQLValue(null, "SELECT MAX(P_Employee_Roe_ID) FROM P_Employee_Roe WHERE IsActive = 'Y'");
                    if (lastRoe > 0)
                        loadData(lastRoe);
                }
            }
        }
    }

    /**
     * Charge les donn\u00e9es d'un Relev\u00e9 d'emploi existant
     */
    public void loadData(int employeeRoeId) 
    {
        m_roeId = employeeRoeId;
        if (m_roeId <= 0) 
        {
            clearForm();
            return;
        }

        try 
        {
            m_employeeRoe = new P_Employee_Roe(Env.getCtx(), m_roeId, null);
            if (m_employeeRoe.getP_Employee_Roe_ID() == 0) 
            {
                clearForm();
                return;
            }

            m_employee = m_employeeRoe.getP_Employee_ID() > 0 ? P_Employee.get(Env.getCtx(), m_employeeRoe.getP_Employee_ID(), null) : null;
            m_employer = (m_employee != null && m_employee.getP_Employer_ID() > 0) ? P_Employer.get(Env.getCtx(), m_employee.getP_Employer_ID(), null) : null;

            // Option temps suppl\u00e9mentaire
            chkOvertimeIncluded.setSelected(m_employeeRoe.isOvertimeIncluded());

            // Bo\u00eetes 1, 2, 3
            lblBox1_Serial.setText(String.valueOf(m_employeeRoe.getP_Employee_Roe_ID()));
            txtBox2_ReplacedSerial.setText(m_employeeRoe.getSerialNumber() != null ? m_employeeRoe.getSerialNumber() : "");

            if (m_employee != null) 
            {
                if ("2".equals(PgiUtil.getSolsticeParameter(Env.getCtx(), "ROE_EmployeeNo")) && m_employee.getC_Activity_ID() > 0) 
                {
                    MActivity activity = new MActivity(Env.getCtx(), m_employee.getC_Activity_ID(), null);
                    lblBox3_EmployerRef.setText(m_employee.getValue() + " (" + activity.getValue() + ")");
                } 
                else 
                {
                    lblBox3_EmployerRef.setText(m_employee.getValue() != null ? m_employee.getValue() : "");
                }
            } 
            else 
            {
                lblBox3_EmployerRef.setText("");
            }

            // Bo\u00eete 4 : Adresse de l'employeur
            MLocation locEmployer = (m_employer != null && m_employer.getC_Location_ID() > 0) ? MLocation.get(Env.getCtx(), m_employer.getC_Location_ID(), null) : null;
            MRegion regEmployer = (locEmployer != null && locEmployer.getC_Region_ID() > 0) ? new MRegion(Env.getCtx(), locEmployer.getC_Region_ID(), null) : null;
            MOrg org = (m_employee != null && m_employee.getAD_Org_ID() > 0) ? MOrg.get(Env.getCtx(), m_employee.getAD_Org_ID()) : null;

            StringBuilder empAddr = new StringBuilder();
            if (org != null) 
            {
                if (org.getDescription() != null && org.getDescription().trim().length() > 0)
                    empAddr.append(org.getDescription());
                else if (org.getName() != null)
                    empAddr.append(org.getName());
            }

            if (locEmployer != null) 
            {
                if (locEmployer.getAddress1() != null) empAddr.append("\n").append(locEmployer.getAddress1());
                if (locEmployer.getAddress2() != null) empAddr.append("\n").append(locEmployer.getAddress2());
                if (locEmployer.getCity() != null) empAddr.append("\n").append(locEmployer.getCity());
                if (regEmployer != null && regEmployer.getDescription() != null) empAddr.append(", ").append(regEmployer.getDescription());
                if (locEmployer.getCountryName() != null) empAddr.append(", ").append(locEmployer.getCountryName());
            }
            txtBox4_EmployerAddress.setText(empAddr.toString());

            // Bo\u00eete 5 & 6
            String craBn = m_employeeRoe.getCanadaRevenueBusinessNumber();
            if ((craBn == null || craBn.trim().length() == 0) && m_employer != null)
                craBn = m_employer.getCanadaRevenueBusinessNumber();
            txtBox5_CRA_BN.setText(craBn != null ? craBn : "");

            selectComboValue(cboBox6_PayPeriodType, m_employeeRoe.getPayPeriodType());

            // Bo\u00eete 7 & 8
            txtBox7_PostalCode.setText(locEmployer != null && locEmployer.getPostal() != null ? locEmployer.getPostal() : "");
            txtBox8_SIN.setText(m_employee != null && m_employee.getSin() != null ? m_employee.getSin() : "");

            // Bo\u00eete 9 : Adresse employ\u00e9
            MLocation locEmployee = (m_employee != null && m_employee.getC_Location_ID() > 0) ? MLocation.get(Env.getCtx(), m_employee.getC_Location_ID(), null) : null;
            MRegion regEmployee = (locEmployee != null && locEmployee.getC_Region_ID() > 0) ? new MRegion(Env.getCtx(), locEmployee.getC_Region_ID(), null) : null;

            StringBuilder eeAddr = new StringBuilder();
            if (m_employee != null && m_employee.getName() != null)
                eeAddr.append(m_employee.getName());
            if (locEmployee != null) 
            {
                if (locEmployee.getAddress1() != null) eeAddr.append("\n").append(locEmployee.getAddress1());
                if (locEmployee.getAddress2() != null) eeAddr.append("\n").append(locEmployee.getAddress2());
                if (locEmployee.getCity() != null) eeAddr.append("\n").append(locEmployee.getCity());
                if (regEmployee != null && regEmployee.getDescription() != null) eeAddr.append(", ").append(regEmployee.getDescription());
                if (locEmployee.getPostal() != null) eeAddr.append(", ").append(locEmployee.getPostal());
                if (locEmployee.getCountryName() != null) eeAddr.append("\n").append(locEmployee.getCountryName());
            }
            txtBox9_EmployeeAddress.setText(eeAddr.toString());

            // Bo\u00eete 10, 11, 12
            dateBox10_FirstDayWorked.setValue(m_employeeRoe.getFirstDayWorked());
            dateBox11_LastDayPaid.setValue(m_employeeRoe.getLastDayForWhichPaid());
            dateBox12_FinalPayPeriodEnding.setValue(m_employeeRoe.getFinalPayPeriodEndingDate());

            // Bo\u00eete 13
            txtBox13_Occupation.setText(m_employeeRoe.getEmployeeOccupation() != null ? m_employeeRoe.getEmployeeOccupation() : "");

            // Bo\u00eete 14 : Rappel
            String recallCode = m_employeeRoe.getExpectedRecallCode();
            if ("Y".equalsIgnoreCase(recallCode)) 
                rdoRecallDate.setSelected(true);
            else if ("N".equalsIgnoreCase(recallCode)) 
                rdoRecallNone.setSelected(true);
            else if ("U".equalsIgnoreCase(recallCode)) 
                rdoRecallUnknown.setSelected(true);
            else 
                bgRecall.clearSelection();

            dateBox14_RecallDate.setValue(m_employeeRoe.getExpectedDateOfRecall());

            // Bo\u00eete 15A & 15B
            txtBox15a_TotalHours.setText(m_employeeRoe.getTotalInsurableHours() != null ? m_employeeRoe.getTotalInsurableHours().toPlainString() : "0");
            txtBox15b_TotalEarnings.setText(m_employeeRoe.getTotalInsurableEarnings() != null ? m_employeeRoe.getTotalInsurableEarnings().toPlainString() : "0");

            // Bo\u00eete 16 : Raison & Contact
            String reason = m_employeeRoe.getReasonForIssuingThisRoe();
            if ((reason == null || reason.trim().length() == 0) && m_employee != null)
                reason = m_employee.getLayoffCode();
            selectComboValue(cboBox16_Reason, reason);

            txtBox16_ContactFirstName.setText(m_employeeRoe.getFirstNameContactPerson() != null ? m_employeeRoe.getFirstNameContactPerson() : "");
            txtBox16_ContactLastName.setText(m_employeeRoe.getLastNameContactPerson() != null ? m_employeeRoe.getLastNameContactPerson() : "");
            txtBox16_AreaCode.setText(m_employeeRoe.getPhoneAreaCode() != null ? m_employeeRoe.getPhoneAreaCode() : "");
            txtBox16_Phone.setText(m_employeeRoe.getPhone() != null ? m_employeeRoe.getPhone() : "");
            txtBox16_PhoneExt.setText(m_employeeRoe.getPhoneExt() != null ? m_employeeRoe.getPhoneExt() : "");

            // Bo\u00eete 15C : P\u00e9riodes 01 \u00e0 53
            for (int i = 1; i <= 53; i++) 
            {
                BigDecimal amt = null;
                try { amt = m_employeeRoe.getEarningsForPayPeriod(i); } catch (Exception e) {}
                txtPeriodEarnings[i - 1].setText(amt != null ? amt.toPlainString() : "0");
            }

            // Bo\u00eete 17 : Vacances, F\u00e9ri\u00e9s, Autres sommes
            selectComboValue(cboBox17_VacationCode, m_employeeRoe.getVacationPayCode());
            txtBox17_VacationAmount.setText(m_employeeRoe.getVacationPayAmount() != null ? m_employeeRoe.getVacationPayAmount().toPlainString() : "0");
            dateBox17_VacationStart.setValue(m_employeeRoe.getVacationPayStartDate());
            dateBox17_VacationEnd.setValue(m_employeeRoe.getVacationPayEndDate());

            for (int i = 1; i <= 10; i++) 
            {
                dateStatutoryHolidays[i - 1].setValue(getStatutoryHolidayDate(m_employeeRoe, i));
                BigDecimal amt = getStatutoryHolidayAmount(m_employeeRoe, i);
                txtStatutoryHolidayAmounts[i - 1].setText(amt != null ? amt.toPlainString() : "0");
            }

            for (int i = 1; i <= 3; i++) 
            {
                selectComboValue(cboOtherMoniesCode[i - 1], getOtherMoniesCode(m_employeeRoe, i));
                dateOtherMoniesStart[i - 1].setValue(getOtherMoniesStartDate(m_employeeRoe, i));
                dateOtherMoniesEnd[i - 1].setValue(getOtherMoniesEndDate(m_employeeRoe, i));
                BigDecimal amt = getOtherMoniesAmount(m_employeeRoe, i);
                txtOtherMoniesAmount[i - 1].setText(amt != null ? amt.toPlainString() : "0");
            }

            // Bo\u00eete 18 : Observations
            txtBox18_Comments[0].setText(m_employeeRoe.getCommentsLine01() != null ? m_employeeRoe.getCommentsLine01() : "");
            txtBox18_Comments[1].setText(m_employeeRoe.getCommentsLine02() != null ? m_employeeRoe.getCommentsLine02() : "");
            txtBox18_Comments[2].setText(m_employeeRoe.getCommentsLine03() != null ? m_employeeRoe.getCommentsLine03() : "");
            txtBox18_Comments[3].setText(m_employeeRoe.getCommentsLine04() != null ? m_employeeRoe.getCommentsLine04() : "");

            // Bo\u00eete 19 : Cong\u00e9s sp\u00e9ciaux
            for (int i = 1; i <= 4; i++) 
            {
                dateSpecialPaymentStart[i - 1].setValue(getSpecialPaymentStartDate(m_employeeRoe, i));
                dateSpecialPaymentEnd[i - 1].setValue(getSpecialPaymentEndDate(m_employeeRoe, i));
                BigDecimal amt = getSpecialPaymentAmount(m_employeeRoe, i);
                txtSpecialPaymentAmount[i - 1].setText(amt != null ? amt.toPlainString() : "0");

                String period = getSpecialPaymentPeriod(m_employeeRoe, i);
                if ("D".equalsIgnoreCase(period))
                    rdoSpecialPaymentDay[i - 1].setSelected(true);
                else
                    rdoSpecialPaymentWeek[i - 1].setSelected(true);
            }

            // Bo\u00eete 20 & 23 : Langues
            int prefLang = m_employeeRoe.getP_Language_ID() > 0 ? m_employeeRoe.getP_Language_ID() : (m_employee != null ? m_employee.getP_Language_ID() : 0);
            selectKeyComboValue(cboBox20_PrefLanguage, prefLang);

            int printLang = m_employeeRoe.getPrintLanguageID() > 0 ? m_employeeRoe.getPrintLanguageID() : (m_employee != null ? m_employee.getP_Language_ID() : 0);
            selectKeyComboValue(cboBox23_PrintLanguage, printLang);

            // Bo\u00eete 21 : T\u00e9l\u00e9phone 2
            txtBox21_Phone.setText(m_employeeRoe.getPhone2() != null ? m_employeeRoe.getPhone2() : "");
            txtBox21_PhoneExt.setText(m_employeeRoe.getPhoneExt2() != null ? m_employeeRoe.getPhoneExt2() : "");

            // Bo\u00eete 22 : Signataire
            String sign = m_employeeRoe.getSignataire2();
            if (sign == null || sign.trim().length() == 0)
                sign = m_employeeRoe.getSignataire();
            txtBox22_Signatory.setText(sign != null ? sign : "");

            // Transfert
            boolean isTransfered = m_employeeRoe.isTransfered();
            bDelete.setEnabled(!isTransfered);
            bSave.setEnabled(!isTransfered);
            bUpdate.setEnabled(!isTransfered);
        }
        catch (Exception ex)
        {
            s_log.log(Level.SEVERE, "VReleveEmploi.loadData", ex);
        }
    }

    /**
     * Initialise un nouveau relev\u00e9 d'emploi pour un employ\u00e9 donn\u00e9
     */
    public void createNewRoeForEmployee(int employeeId) 
    {
        m_employee = P_Employee.get(Env.getCtx(), employeeId, null);
        if (m_employee == null || m_employee.getP_Employee_ID() == 0) return;

        m_employer = P_Employer.get(Env.getCtx(), m_employee.getP_Employer_ID(), null);
        m_employeeRoe = new P_Employee_Roe(Env.getCtx(), -1, null);
        m_employeeRoe.setAD_User_ID(Env.getAD_User_ID(Env.getCtx()));
        m_employeeRoe.setP_Employee_ID(m_employee.getP_Employee_ID());
        m_employeeRoe.setP_Employer_ID(m_employee.getP_Employer_ID());
        m_employeeRoe.setAD_Org_ID(m_employee.getAD_Org_ID());
        m_employeeRoe.setP_Job_Title_ID(m_employee.getP_Job_Title_ID());
        m_employeeRoe.setP_Language_ID(m_employee.getP_Language_ID());
        m_employeeRoe.setPrintLanguageID(m_employee.getP_Language_ID());
        m_employeeRoe.setCanadaRevenueBusinessNumber(m_employer != null ? m_employer.getCanadaRevenueBusinessNumber() : null);

        // Profession par d\u00e9faut
        P_Assignment assignment = P_Assignment.get(Env.getCtx(), m_employee.GetAssignmentPrincipal(), null);
        P_Post post = assignment != null ? P_Post.get(Env.getCtx(), assignment.getP_Post_ID(), null) : null;
        P_Job_Type jobType = P_Job_Type.get(Env.getCtx(), m_employee.getP_Job_Type_ID(), null);
        if (post != null && jobType != null)
            m_employeeRoe.setEmployeeOccupation(post.getName() + " (" + jobType.getName() + ")");
        else if (jobType != null)
            m_employeeRoe.setEmployeeOccupation("(" + jobType.getName() + ")");

        // P\u00e9riode et fr\u00e9quence
        P_Payment_Group pg = P_Payment_Group.get(Env.getCtx(), m_employee.getP_Payment_Group_ID(), null);
        if (pg != null) 
        {
            P_Period period = P_Period.getOpenPeriodWithCalendar(Env.getCtx(), pg.getP_Calendar_ID(), null);
            if (period != null) 
            {
                m_employeeRoe.setP_Period_ID(period.getP_Period_ID());
                m_employeeRoe.setP_Frequency_ID(period.getP_Frequency_ID());
                P_Frequency freq = P_Frequency.get(Env.getCtx(), period.getP_Frequency_ID(), null);
                if (freq != null)
                    m_employeeRoe.setPayPeriodType(freq.getPayPeriodType());
            }
        }

        m_employeeRoe.save();
        m_roeId = m_employeeRoe.getP_Employee_Roe_ID();
        loadData(m_roeId);
    }

    /**
     * Sauvegarde les modifications du formulaire dans P_Employee_Roe
     */
    private boolean saveData() 
    {
        if (m_employeeRoe == null) return false;

        m_employeeRoe.setisOvertimeIncluded(chkOvertimeIncluded.isSelected());
        m_employeeRoe.setSerialNumber(txtBox2_ReplacedSerial.getText().trim());

        ValueNamePair vpPayPeriod = (ValueNamePair) cboBox6_PayPeriodType.getSelectedItem();
        if (vpPayPeriod != null)
            m_employeeRoe.setPayPeriodType(vpPayPeriod.getValue());

        m_employeeRoe.setFirstDayWorked(dateBox10_FirstDayWorked.getTimestamp());
        m_employeeRoe.setLastDayForWhichPaid(dateBox11_LastDayPaid.getTimestamp());
        m_employeeRoe.setFinalPayPeriodEndingDate(dateBox12_FinalPayPeriodEnding.getTimestamp());

        m_employeeRoe.setEmployeeOccupation(txtBox13_Occupation.getText().trim());

        if (rdoRecallDate.isSelected()) 
        {
            m_employeeRoe.setExpectedRecallCode("Y");
            m_employeeRoe.setExpectedDateOfRecall(dateBox14_RecallDate.getTimestamp());
        } 
        else if (rdoRecallNone.isSelected()) 
        {
            m_employeeRoe.setExpectedRecallCode("N");
            m_employeeRoe.setExpectedDateOfRecall(null);
        } 
        else if (rdoRecallUnknown.isSelected()) 
        {
            m_employeeRoe.setExpectedRecallCode("U");
            m_employeeRoe.setExpectedDateOfRecall(null);
        } 
        else 
        {
            m_employeeRoe.setExpectedRecallCode(null);
            m_employeeRoe.setExpectedDateOfRecall(null);
        }

        m_employeeRoe.setTotalInsurableHours(parseBigDecimal(txtBox15a_TotalHours.getText()));
        m_employeeRoe.setTotalInsurableEarnings(parseBigDecimal(txtBox15b_TotalEarnings.getText()));

        ValueNamePair vpReason = (ValueNamePair) cboBox16_Reason.getSelectedItem();
        if (vpReason != null)
            m_employeeRoe.setReasonForIssuingThisRoe(vpReason.getValue());

        m_employeeRoe.setFirstNameContactPerson(txtBox16_ContactFirstName.getText().trim());
        m_employeeRoe.setLastNameContactPerson(txtBox16_ContactLastName.getText().trim());
        m_employeeRoe.setPhoneAreaCode(txtBox16_AreaCode.getText().trim());
        m_employeeRoe.setPhone(txtBox16_Phone.getText().trim());
        m_employeeRoe.setPhoneExt(txtBox16_PhoneExt.getText().trim());

        // P\u00e9riodes 1 \u00e0 53
        for (int i = 1; i <= 53; i++) 
        {
            m_employeeRoe.setEarningsForPayPeriod(i, parseBigDecimal(txtPeriodEarnings[i - 1].getText()));
        }

        // Vacances
        ValueNamePair vpVac = (ValueNamePair) cboBox17_VacationCode.getSelectedItem();
        m_employeeRoe.setVacationPayCode(vpVac != null ? vpVac.getValue() : null);
        m_employeeRoe.setVacationPayAmount(parseBigDecimal(txtBox17_VacationAmount.getText()));
        m_employeeRoe.setVacationPayStartDate(dateBox17_VacationStart.getTimestamp());
        m_employeeRoe.setVacationPayEndDate(dateBox17_VacationEnd.getTimestamp());

        // F\u00e9ri\u00e9s (1 \u00e0 10)
        for (int i = 1; i <= 10; i++) 
        {
            setStatutoryHoliday(m_employeeRoe, i, dateStatutoryHolidays[i - 1].getTimestamp(), parseBigDecimal(txtStatutoryHolidayAmounts[i - 1].getText()));
        }

        // Autres sommes (1 \u00e0 3)
        for (int i = 1; i <= 3; i++) 
        {
            ValueNamePair vpOther = (ValueNamePair) cboOtherMoniesCode[i - 1].getSelectedItem();
            setOtherMonies(m_employeeRoe, i, 
                vpOther != null ? vpOther.getValue() : null, 
                dateOtherMoniesStart[i - 1].getTimestamp(), 
                dateOtherMoniesEnd[i - 1].getTimestamp(), 
                parseBigDecimal(txtOtherMoniesAmount[i - 1].getText()));
        }

        // Observations (4 lignes)
        m_employeeRoe.setCommentsLine01(txtBox18_Comments[0].getText().trim());
        m_employeeRoe.setCommentsLine02(txtBox18_Comments[1].getText().trim());
        m_employeeRoe.setCommentsLine03(txtBox18_Comments[2].getText().trim());
        m_employeeRoe.setCommentsLine04(txtBox18_Comments[3].getText().trim());

        // Cong\u00e9s sp\u00e9ciaux (1 \u00e0 4)
        for (int i = 1; i <= 4; i++) 
        {
            setSpecialPayment(m_employeeRoe, i, 
                dateSpecialPaymentStart[i - 1].getTimestamp(), 
                dateSpecialPaymentEnd[i - 1].getTimestamp(), 
                parseBigDecimal(txtSpecialPaymentAmount[i - 1].getText()), 
                rdoSpecialPaymentDay[i - 1].isSelected() ? "D" : "W");
        }

        // Langues
        KeyNamePair kpLang = (KeyNamePair) cboBox20_PrefLanguage.getSelectedItem();
        if (kpLang != null) m_employeeRoe.setP_Language_ID(kpLang.getKey());
        KeyNamePair kpPrint = (KeyNamePair) cboBox23_PrintLanguage.getSelectedItem();
        if (kpPrint != null) m_employeeRoe.setPrintLanguageID(kpPrint.getKey());

        // T\u00e9l\u00e9phone 2 et Signataire
        m_employeeRoe.setPhone2(txtBox21_Phone.getText().trim());
        m_employeeRoe.setPhoneExt2(txtBox21_PhoneExt.getText().trim());
        m_employeeRoe.setSignataire2(txtBox22_Signatory.getText().trim());

        boolean ok = m_employeeRoe.save();
        if (ok) 
        {
            m_roeId = m_employeeRoe.getP_Employee_Roe_ID();
            lblBox1_Serial.setText(String.valueOf(m_roeId));
        }
        return ok;
    }

    /**
     * R\u00e9initialise le formulaire \u00e0 vide
     */
    private void clearForm() 
    {
        m_roeId = -1;
        m_employeeRoe = null;
        m_employee = null;
        m_employer = null;

        chkOvertimeIncluded.setSelected(false);
        lblBox1_Serial.setText(" ");
        txtBox2_ReplacedSerial.setText("");
        lblBox3_EmployerRef.setText(" ");
        txtBox4_EmployerAddress.setText("");
        txtBox5_CRA_BN.setText("");
        txtBox7_PostalCode.setText("");
        txtBox8_SIN.setText("");
        txtBox9_EmployeeAddress.setText("");
        dateBox10_FirstDayWorked.setValue(null);
        dateBox11_LastDayPaid.setValue(null);
        dateBox12_FinalPayPeriodEnding.setValue(null);
        txtBox13_Occupation.setText("");
        bgRecall.clearSelection();
        dateBox14_RecallDate.setValue(null);
        txtBox15a_TotalHours.setText("0");
        txtBox15b_TotalEarnings.setText("0");
        txtBox16_ContactFirstName.setText("");
        txtBox16_ContactLastName.setText("");
        txtBox16_AreaCode.setText("");
        txtBox16_Phone.setText("");
        txtBox16_PhoneExt.setText("");

        for (int i = 0; i < 53; i++)
            txtPeriodEarnings[i].setText("0");

        txtBox17_VacationAmount.setText("0");
        dateBox17_VacationStart.setValue(null);
        dateBox17_VacationEnd.setValue(null);

        for (int i = 0; i < 10; i++) 
        {
            dateStatutoryHolidays[i].setValue(null);
            txtStatutoryHolidayAmounts[i].setText("0");
        }

        for (int i = 0; i < 3; i++) 
        {
            dateOtherMoniesStart[i].setValue(null);
            dateOtherMoniesEnd[i].setValue(null);
            txtOtherMoniesAmount[i].setText("0");
        }

        for (int i = 0; i < 4; i++) 
            txtBox18_Comments[i].setText("");

        for (int i = 0; i < 4; i++) 
        {
            dateSpecialPaymentStart[i].setValue(null);
            dateSpecialPaymentEnd[i].setValue(null);
            txtSpecialPaymentAmount[i].setText("0");
            rdoSpecialPaymentWeek[i].setSelected(true);
        }

        txtBox21_Phone.setText("");
        txtBox21_PhoneExt.setText("");
        txtBox22_Signatory.setText("");
    }

    /**
     * Gestionnaire des actions des boutons
     */
    public void actionPerformed(ActionEvent e) 
    {
        Object src = e.getSource();

        if (src == bSearch) 
        {
            showSearchDialog();
        } 
        else if (src == bSave) 
        {
            if (saveData())
                ADialog.info(m_WindowNo, this, "SaveOk");
            else
                ADialog.error(m_WindowNo, this, "SaveError");
        } 
        else if (src == bUpdate) 
        {
            if (m_employeeRoe == null || m_employeeRoe.getP_Employee_Roe_ID() <= 0) return;
            if (ADialog.ask(m_WindowNo, this, "Voulez-vous vraiment recalculer ce relev\u00e9 d'emploi?")) 
            {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                try 
                {
                    saveData();
                    m_employeeRoe.fillRoe();
                    m_employeeRoe.save();
                    loadData(m_employeeRoe.getP_Employee_Roe_ID());
                    ADialog.info(m_WindowNo, this, "Updated");
                } 
                finally 
                {
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        } 
        else if (src == bCopy) 
        {
            if (m_employeeRoe == null || m_employeeRoe.getP_Employee_Roe_ID() <= 0) return;
            if (ADialog.ask(m_WindowNo, this, "Voulez-vous vraiment copier ce relev\u00e9 d'emploi?")) 
            {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                try 
                {
                    P_Employee_Roe copy = P_Employee_Roe.copyFrom(Env.getCtx(), m_employeeRoe.getP_Employee_Roe_ID(), null);
                    if (copy != null && copy.getP_Employee_Roe_ID() > 0) 
                    {
                        loadData(copy.getP_Employee_Roe_ID());
                        ADialog.info(m_WindowNo, this, "RecordCopied");
                    }
                } 
                finally 
                {
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        } 
        else if (src == bDelete) 
        {
            if (m_employeeRoe == null || m_employeeRoe.getP_Employee_Roe_ID() <= 0) return;
            if (ADialog.ask(m_WindowNo, this, "Voulez-vous vraiment supprimer ce relev\u00e9 d'emploi?")) 
            {
                int id = m_employeeRoe.getP_Employee_Roe_ID();
                DB.executeUpdate("DELETE FROM P_Employee_Roe WHERE P_Employee_Roe_ID = " + id, null);
                clearForm();
                ADialog.info(m_WindowNo, this, "RecordDeleted");
            }
        } 
        else if (src == bExport) 
        {
            if (m_employeeRoe == null || m_employeeRoe.getP_Employee_Roe_ID() <= 0) return;
            if (ADialog.ask(m_WindowNo, this, "Voulez-vous exporter ce relev\u00e9 d'emploi?")) 
            {
                saveData();
                m_employeeRoe.exportRoe();
                ADialog.info(m_WindowNo, this, "Export complete");
            }
        }
    }

    /**
     * Classe interne repr\u00e9sentant un employ\u00e9 pour la liste d\u00e9roulante et la recherche.
     */
    private static class EmployeeSearchItem 
    {
        final int empId;
        final String value;     // Num\u00e9ro d'employ\u00e9
        final String name;      // Nom usuel (Nom, Pr\u00e9nom)
        final String firstName; // Pr\u00e9nom
        final String surname;   // Nom de famille
        final String display;   // Format d'affichage

        EmployeeSearchItem(int empId, String value, String name, String firstName, String surname) 
        {
            this.empId = empId;
            this.value = value != null ? value.trim() : "";
            this.name = name != null ? name.trim() : "";
            this.firstName = firstName != null ? firstName.trim() : "";
            this.surname = surname != null ? surname.trim() : "";

            if (empId == 0) 
            {
                this.display = "";
            } 
            else 
            {
                String disp = this.name;
                if (this.value.length() > 0)
                    disp += " (" + this.value + ")";
                this.display = disp;
            }
        }

        public boolean matches(String filterNom, String filterPrenom, String filterNumero) 
        {
            if (empId == 0) return true;

            if (filterNom != null && filterNom.length() > 0) 
            {
                String f = filterNom.toUpperCase();
                boolean m = surname.toUpperCase().contains(f) || name.toUpperCase().contains(f);
                if (!m) return false;
            }

            if (filterPrenom != null && filterPrenom.length() > 0) 
            {
                String f = filterPrenom.toUpperCase();
                boolean m = firstName.toUpperCase().contains(f) || name.toUpperCase().contains(f);
                if (!m) return false;
            }

            if (filterNumero != null && filterNumero.length() > 0) 
            {
                String f = filterNumero.toUpperCase();
                boolean m = value.toUpperCase().contains(f);
                if (!m) return false;
            }

            return true;
        }

        @Override
        public String toString() 
        {
            return display;
        }
    }

    /**
     * Bo\u00eete de dialogue modale de recherche d'employ\u00e9 et de ses relev\u00e9s d'emploi.
     * Combine 3 champs textes (Nom, Pr\u00e9nom, Num\u00e9ro) et une liste d\u00e9roulante (ComboBox) d'employ\u00e9s.
     */
    private void showSearchDialog() 
    {
        JFrame parentFrame = m_frame != null ? m_frame : Env.getWindow(m_WindowNo);
        if (parentFrame == null) parentFrame = Env.getFrame(this);
        final JDialog dlg = new JDialog(parentFrame, "Liste des relev\u00e9s d'emplois", true);
        dlg.setLayout(new BorderLayout(8, 8));
        dlg.setSize(840, 560);
        dlg.setLocationRelativeTo(this);

        // Charger tous les employ\u00e9s
        final List<EmployeeSearchItem> allEmployees = new ArrayList<EmployeeSearchItem>();
        allEmployees.add(new EmployeeSearchItem(0, "", "", "", "")); // Ligne vide par d\u00e9faut

        String sqlEmp = "SELECT P_Employee_ID, Value, Name, FirstName, Surname FROM P_Employee WHERE IsActive='Y' OR DATELAYOFF >= getdate() - 365   ORDER BY Name";
        try 
        {
            PreparedStatement pstmt = DB.prepareStatement(sqlEmp, null);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) 
            {
                int empId = rs.getInt(1);
                String val = rs.getString(2);
                String nm = rs.getString(3);
                String fn = rs.getString(4);
                String sn = rs.getString(5);
                allEmployees.add(new EmployeeSearchItem(empId, val, nm, fn, sn));
            }
            rs.close();
            pstmt.close();
        } 
        catch (Exception ex) 
        {
            s_log.log(Level.SEVERE, "SearchDialog load employees", ex);
        }

        // --- 1. Panneau Sup\u00e9rieur : "Recherche d'employ\u00e9" ---
        JPanel searchBoxPanel = new JPanel();
        searchBoxPanel.setLayout(new BoxLayout(searchBoxPanel, BoxLayout.Y_AXIS));
        searchBoxPanel.setBorder(BorderFactory.createTitledBorder("Recherche d'employ\u00e9"));

        // Ligne 1 : Nom, Pr\u00e9nom, Num\u00e9ro
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 3));
        JLabel lblNom = new JLabel("Nom :");
        lblNom.setPreferredSize(new Dimension(58, 22));
        row1.add(lblNom);

        final JTextField txtNom = new JTextField(12);
        txtNom.setPreferredSize(new Dimension(140, 24));
        txtNom.setToolTipText("Filtrer par nom de famille ou nom complet");
        row1.add(txtNom);

        row1.add(Box.createHorizontalStrut(10));
        row1.add(new JLabel("Pr\u00e9nom :"));
        final JTextField txtPrenom = new JTextField(12);
        txtPrenom.setPreferredSize(new Dimension(140, 24));
        txtPrenom.setToolTipText("Filtrer par pr\u00e9nom");
        row1.add(txtPrenom);

        row1.add(Box.createHorizontalStrut(10));
        row1.add(new JLabel("Num\u00e9ro :"));
        final JTextField txtNumero = new JTextField(8);
        txtNumero.setPreferredSize(new Dimension(100, 24));
        txtNumero.setToolTipText("Filtrer par num\u00e9ro / matricule");
        row1.add(txtNumero);

        // Ligne 2 : Liste d\u00e9roulante Employ\u00e9 + Boutons
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 3));
        JLabel lblEmp = new JLabel("Employ\u00e9 :");
        lblEmp.setPreferredSize(new Dimension(58, 22));
        row2.add(lblEmp);

        final JComboBox<EmployeeSearchItem> cboEmployees = new JComboBox<EmployeeSearchItem>();
        cboEmployees.setPreferredSize(new Dimension(340, 24));
        row2.add(cboEmployees);

        row2.add(Box.createHorizontalStrut(12));
        JButton bLaunchSearch = new JButton("Lancer la recherche");
        bLaunchSearch.setFont(new Font("SansSerif", Font.BOLD, 12));
        row2.add(bLaunchSearch);

        JButton bReset = new JButton("Effacer");
        bReset.setFont(new Font("SansSerif", Font.PLAIN, 12));
        row2.add(bReset);

        searchBoxPanel.add(row1);
        searchBoxPanel.add(row2);
        dlg.add(searchBoxPanel, BorderLayout.NORTH);

        // Flag pour \u00e9viter les d\u00e9clenchements d'actionListener en boucle lors du repeuplement
        final boolean[] isUpdatingCombo = { false };

        final Runnable populateCombo = new Runnable() 
        {
            public void run() 
            {
                isUpdatingCombo[0] = true;
                try 
                {
                    String nom = txtNom.getText().trim();
                    String prenom = txtPrenom.getText().trim();
                    String num = txtNumero.getText().trim();

                    EmployeeSearchItem prevSel = (EmployeeSearchItem) cboEmployees.getSelectedItem();
                    int prevEmpId = prevSel != null ? prevSel.empId : 0;

                    cboEmployees.removeAllItems();
                    boolean hasFilter = nom.length() > 0 || prenom.length() > 0 || num.length() > 0;

                    for (EmployeeSearchItem item : allEmployees) 
                    {
                        if (!hasFilter || item.empId == 0 || item.matches(nom, prenom, num)) 
                        {
                            cboEmployees.addItem(item);
                        }
                    }

                    // Si un seul employ\u00e9 correspond, le s\u00e9lectionner automatiquement
                    if (hasFilter && cboEmployees.getItemCount() == 2) 
                    {
                        cboEmployees.setSelectedIndex(1);
                    } 
                    else if (prevEmpId > 0) 
                    {
                        for (int i = 0; i < cboEmployees.getItemCount(); i++) 
                        {
                            if (cboEmployees.getItemAt(i).empId == prevEmpId) 
                            {
                                cboEmployees.setSelectedIndex(i);
                                break;
                            }
                        }
                    }
                } 
                finally 
                {
                    isUpdatingCombo[0] = false;
                }
            }
        };

        // Remplissage initial de la liste d\u00e9roulante
        populateCombo.run();

        // Pr\u00e9-remplissage si un employ\u00e9 est d\u00e9j\u00e0 charg\u00e9 sur l'\u00e9cran
        if (m_employee != null && m_employee.getP_Employee_ID() > 0) 
        {
/*        	
            if (m_employee.getSurname() != null && m_employee.getSurname().trim().length() > 0)
                txtNom.setText(m_employee.getSurname().trim());
            else if (m_employee.getName() != null)
                txtNom.setText(m_employee.getName().trim());

            if (m_employee.getFirstName() != null)
                txtPrenom.setText(m_employee.getFirstName().trim());

            if (m_employee.getValue() != null)
                txtNumero.setText(m_employee.getValue().trim());
*/
            populateCombo.run();
            for (int i = 0; i < cboEmployees.getItemCount(); i++) 
            {
                if (cboEmployees.getItemAt(i).empId == m_employee.getP_Employee_ID()) 
                {
                    cboEmployees.setSelectedIndex(i);
                    break;
                }
            }
        }

        // Filtrage en direct de la ComboBox lors de la frappe dans les champs textes
        KeyAdapter filterKeyAdapter = new KeyAdapter() 
        {
            public void keyReleased(KeyEvent e) 
            {
                populateCombo.run();
            }
        };
        txtNom.addKeyListener(filterKeyAdapter);
        txtPrenom.addKeyListener(filterKeyAdapter);
        txtNumero.addKeyListener(filterKeyAdapter);

        // --- 2. Panneau Centre : "R\u00e9sultats de la recherche" ---
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder("R\u00e9sultats de la recherche"));

        final String[] colHeaders = { 
            "N\u00b0 de s\u00e9rie", 
            "No employ\u00e9",
            "Employ\u00e9",
            "Premier jour de travail", 
            "Dernier jour pay\u00e9", 
            "Date fin p\u00e9riode paie"
        };
        final DefaultTableModel tblModel = new DefaultTableModel(colHeaders, 0) 
        {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        final JTable resultTable = new JTable(tblModel);
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultTable.setFont(new Font("SansSerif", Font.PLAIN, 12));
        resultTable.setRowHeight(22);

        resultTable.getColumnModel().getColumn(0).setPreferredWidth(85);
        resultTable.getColumnModel().getColumn(1).setPreferredWidth(85);
        resultTable.getColumnModel().getColumn(2).setPreferredWidth(190);
        resultTable.getColumnModel().getColumn(3).setPreferredWidth(125);
        resultTable.getColumnModel().getColumn(4).setPreferredWidth(125);
        resultTable.getColumnModel().getColumn(5).setPreferredWidth(130);

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final List<Integer> rowEmployeeIds = new ArrayList<Integer>();

        final Runnable executeSearch = new Runnable() 
        {
            public void run() 
            {
                tblModel.setRowCount(0);
                rowEmployeeIds.clear();

                EmployeeSearchItem selectedEmp = (EmployeeSearchItem) cboEmployees.getSelectedItem();
                int empId = selectedEmp != null ? selectedEmp.empId : 0;

                StringBuilder sql = new StringBuilder();
                sql.append("SELECT Roe.P_Employee_Roe_ID, Emp.Value, Emp.Name, Roe.FirstDayWorked, ")
                   .append("       Roe.LastDayForWhichPaid, Roe.FinalPayPeriodEndingDate, Emp.P_Employee_ID ")
                   .append("FROM P_Employee_Roe Roe ")
                   .append("INNER JOIN P_Employee Emp ON (Roe.P_Employee_ID = Emp.P_Employee_ID) ")
                   .append("WHERE Roe.IsActive = 'Y' ");

                if (empId > 0) 
                {
                    sql.append("AND Roe.P_Employee_ID = ").append(empId).append(" ");
                }
                else 
                {
                    String nom = txtNom.getText().trim();
                    String prenom = txtPrenom.getText().trim();
                    String numero = txtNumero.getText().trim();

                    if (nom.length() > 0) 
                    {
                        String cleanNom = nom.replace("'", "''").toUpperCase();
                        sql.append("AND (UPPER(Emp.Surname) LIKE '%").append(cleanNom).append("%' ")
                           .append(" OR UPPER(Emp.Name) LIKE '%").append(cleanNom).append("%') ");
                    }

                    if (prenom.length() > 0) 
                    {
                        String cleanPrenom = prenom.replace("'", "''").toUpperCase();
                        sql.append("AND (UPPER(Emp.FirstName) LIKE '%").append(cleanPrenom).append("%' ")
                           .append(" OR UPPER(Emp.Name) LIKE '%").append(cleanPrenom).append("%') ");
                    }

                    if (numero.length() > 0) 
                    {
                        String cleanNum = numero.replace("'", "''").toUpperCase();
                        sql.append("AND UPPER(Emp.Value) LIKE '%").append(cleanNum).append("%' ");
                    }
                }

                sql.append("ORDER BY Roe.FirstDayWorked DESC, Roe.P_Employee_Roe_ID DESC");

                try 
                {
                    PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) 
                    {
                        Vector<Object> row = new Vector<Object>();
                        row.add(rs.getInt(1));               // No de serie
                        row.add(rs.getString(2));            // No employe
                        row.add(rs.getString(3));            // Nom employe
                        Timestamp ts1 = rs.getTimestamp(4);  // Premier jour
                        row.add(ts1 != null ? sdf.format(ts1) : "");
                        Timestamp ts2 = rs.getTimestamp(5);  // Dernier jour
                        row.add(ts2 != null ? sdf.format(ts2) : "");
                        Timestamp ts3 = rs.getTimestamp(6);  // Fin periode paie
                        row.add(ts3 != null ? sdf.format(ts3) : "");

                        tblModel.addRow(row);
                        rowEmployeeIds.add(rs.getInt(7));    // P_Employee_ID
                    }
                    rs.close();
                    pstmt.close();
                } 
                catch (Exception ex) 
                {
                    s_log.log(Level.SEVERE, "Search ROE table query", ex);
                }
            }
        };

        // Lancer la recherche sur le clic du bouton
        bLaunchSearch.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) { executeSearch.run(); }
        });

        // Changement de s\u00e9lection dans la liste d\u00e9roulante lance automatiquement la recherche
        cboEmployees.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                if (isUpdatingCombo[0]) return;
                executeSearch.run();
            }
        });

        // Bouton Effacer
        bReset.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                txtNom.setText("");
                txtPrenom.setText("");
                txtNumero.setText("");
                populateCombo.run();
                if (cboEmployees.getItemCount() > 0)
                    cboEmployees.setSelectedIndex(0);
                executeSearch.run();
                txtNom.requestFocusInWindow();
            }
        });

        // Appuyer sur Entr\u00e9e dans n'importe lequel des 3 champs lance la recherche
        ActionListener enterListener = new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                if (cboEmployees.getItemCount() == 2) 
                {
                    cboEmployees.setSelectedIndex(1);
                }
                executeSearch.run();
            }
        };
        txtNom.addActionListener(enterListener);
        txtPrenom.addActionListener(enterListener);
        txtNumero.addActionListener(enterListener);

        // Ex\u00e9cuter la recherche initiale
        executeSearch.run();

        // Double-clic pour ouvrir directement
        resultTable.addMouseListener(new MouseAdapter() 
        {
            public void mouseClicked(MouseEvent e) 
            {
                if (e.getClickCount() == 2 && resultTable.getSelectedRow() >= 0) 
                {
                    int selRow = resultTable.getSelectedRow();
                    int roeId = (Integer) tblModel.getValueAt(selRow, 0);
                    loadData(roeId);
                    dlg.dispose();
                }
            }
        });

        resultPanel.add(new JScrollPane(resultTable), BorderLayout.CENTER);
        dlg.add(resultPanel, BorderLayout.CENTER);

        // --- 3. Panneau Inf\u00e9rieur : Actions ---
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));

        JButton bOpen = new JButton("Ouvrir le relev\u00e9 s\u00e9lectionn\u00e9");
        bOpen.setFont(new Font("SansSerif", Font.PLAIN, 12));
        bOpen.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                int selRow = resultTable.getSelectedRow();
                if (selRow >= 0) 
                {
                    int roeId = (Integer) tblModel.getValueAt(selRow, 0);
                    loadData(roeId);
                    dlg.dispose();
                } 
                else 
                {
                    ADialog.warn(m_WindowNo, dlg, "Veuillez s\u00e9lectionner un relev\u00e9 dans la liste.");
                }
            }
        });

        JButton bNewRoe = new JButton("Nouveau");
        bNewRoe.setFont(new Font("SansSerif", Font.BOLD, 12));
        bNewRoe.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                int selRow = resultTable.getSelectedRow();
                int empId = 0;
                String empDisplayName = "";

                // 1. Si un employ\u00e9 est s\u00e9lectionn\u00e9 dans la liste d\u00e9roulante
                EmployeeSearchItem selectedEmp = (EmployeeSearchItem) cboEmployees.getSelectedItem();
                if (selectedEmp != null && selectedEmp.empId > 0) 
                {
                    empId = selectedEmp.empId;
                    empDisplayName = selectedEmp.display;
                }
                // 2. Sinon, si une ligne est s\u00e9lectionn\u00e9e dans la table
                else if (selRow >= 0 && selRow < rowEmployeeIds.size()) 
                {
                    empId = rowEmployeeIds.get(selRow);
                    empDisplayName = (String) tblModel.getValueAt(selRow, 2);
                    String num = (String) tblModel.getValueAt(selRow, 1);
                    if (num != null && num.trim().length() > 0)
                        empDisplayName += " (" + num + ")";
                } 
                // 3. Sinon, v\u00e9rifier si le filtrage correspond \u00e0 un seul employ\u00e9
                else if (cboEmployees.getItemCount() == 2) 
                {
                    EmployeeSearchItem singleEmp = cboEmployees.getItemAt(1);
                    if (singleEmp != null && singleEmp.empId > 0) 
                    {
                        empId = singleEmp.empId;
                        empDisplayName = singleEmp.display;
                    }
                }

                if (empId <= 0) 
                {
                    ADialog.warn(m_WindowNo, dlg, "Veuillez s\u00e9lectionner un employ\u00e9 dans la liste d\u00e9roulante ou un relev\u00e9 dans la table pour cr\u00e9er son nouveau relev\u00e9.");
                    return;
                }

                if (ADialog.ask(m_WindowNo, dlg, "Voulez-vous cr\u00e9er un nouveau relev\u00e9 pour " + empDisplayName + " ?")) 
                {
                    createNewRoeForEmployee(empId);
                    dlg.dispose();
                }
            }
        });

        JButton bClose = new JButton("Fermer");
        bClose.setFont(new Font("SansSerif", Font.PLAIN, 12));
        bClose.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) { dlg.dispose(); }
        });

        actionPanel.add(bOpen);
        actionPanel.add(bNewRoe);
        actionPanel.add(bClose);
        dlg.add(actionPanel, BorderLayout.SOUTH);

        // Donner le focus au premier champ de recherche (Nom) pour saisie imm\u00e9diate
        SwingUtilities.invokeLater(new Runnable() 
        {
            public void run() 
            {
                txtNom.requestFocusInWindow();
            }
        });

        dlg.setVisible(true);
    }

    // --- Helpers pour Propri\u00e9t\u00e9s ROE ---

    private void setStatutoryHoliday(P_Employee_Roe roe, int index, Timestamp date, BigDecimal amount) 
    {
        switch (index) 
        {
            case 1: roe.setStatutoryHolidayStartDate01(date); roe.setStatutoryHolidayAmount01(amount); break;
            case 2: roe.setStatutoryHolidayStartDate02(date); roe.setStatutoryHolidayAmount02(amount); break;
            case 3: roe.setStatutoryHolidayStartDate03(date); roe.setStatutoryHolidayAmount03(amount); break;
            case 4: roe.setStatutoryHolidayStartDate04(date); roe.setStatutoryHolidayAmount04(amount); break;
            case 5: roe.setStatutoryHolidayStartDate05(date); roe.setStatutoryHolidayAmount05(amount); break;
            case 6: roe.setStatutoryHolidayStartDate06(date); roe.setStatutoryHolidayAmount06(amount); break;
            case 7: roe.setStatutoryHolidayStartDate07(date); roe.setStatutoryHolidayAmount07(amount); break;
            case 8: roe.setStatutoryHolidayStartDate08(date); roe.setStatutoryHolidayAmount08(amount); break;
            case 9: roe.setStatutoryHolidayStartDate09(date); roe.setStatutoryHolidayAmount09(amount); break;
            case 10: roe.setStatutoryHolidayStartDate10(date); roe.setStatutoryHolidayAmount10(amount); break;
        }
    }

    private Timestamp getStatutoryHolidayDate(P_Employee_Roe roe, int index) 
    {
        switch (index) 
        {
            case 1: return roe.getStatutoryHolidayStartDate01();
            case 2: return roe.getStatutoryHolidayStartDate02();
            case 3: return roe.getStatutoryHolidayStartDate03();
            case 4: return roe.getStatutoryHolidayStartDate04();
            case 5: return roe.getStatutoryHolidayStartDate05();
            case 6: return roe.getStatutoryHolidayStartDate06();
            case 7: return roe.getStatutoryHolidayStartDate07();
            case 8: return roe.getStatutoryHolidayStartDate08();
            case 9: return roe.getStatutoryHolidayStartDate09();
            case 10: return roe.getStatutoryHolidayStartDate10();
            default: return null;
        }
    }

    private BigDecimal getStatutoryHolidayAmount(P_Employee_Roe roe, int index) 
    {
        switch (index) 
        {
            case 1: return roe.getStatutoryHolidayAmount01();
            case 2: return roe.getStatutoryHolidayAmount02();
            case 3: return roe.getStatutoryHolidayAmount03();
            case 4: return roe.getStatutoryHolidayAmount04();
            case 5: return roe.getStatutoryHolidayAmount05();
            case 6: return roe.getStatutoryHolidayAmount06();
            case 7: return roe.getStatutoryHolidayAmount07();
            case 8: return roe.getStatutoryHolidayAmount08();
            case 9: return roe.getStatutoryHolidayAmount09();
            case 10: return roe.getStatutoryHolidayAmount10();
            default: return BigDecimal.ZERO;
        }
    }

    private void setOtherMonies(P_Employee_Roe roe, int index, String code, Timestamp start, Timestamp end, BigDecimal amount) 
    {
        switch (index) 
        {
            case 1:
                roe.setOtherMoniesCode01(code);
                roe.setOtherMoniesStartDate01(start);
                roe.setOtherMoniesEndDate01(end);
                roe.setOtherMoniesAmount01(amount);
                break;
            case 2:
                roe.setOtherMoniesCode02(code);
                roe.setOtherMoniesStartDate02(start);
                roe.setOtherMoniesEndDate02(end);
                roe.setOtherMoniesAmount02(amount);
                break;
            case 3:
                roe.setOtherMoniesCode03(code);
                roe.setOtherMoniesStartDate03(start);
                roe.setOtherMoniesEndDate03(end);
                roe.setOtherMoniesAmount03(amount);
                break;
        }
    }

    private String getOtherMoniesCode(P_Employee_Roe roe, int index) 
    {
        switch (index) 
        {
            case 1: return roe.getOtherMoniesCode01();
            case 2: return roe.getOtherMoniesCode02();
            case 3: return roe.getOtherMoniesCode03();
            default: return null;
        }
    }

    private Timestamp getOtherMoniesStartDate(P_Employee_Roe roe, int index) 
    {
        switch (index) 
        {
            case 1: return roe.getOtherMoniesStartDate01();
            case 2: return roe.getOtherMoniesStartDate02();
            case 3: return roe.getOtherMoniesStartDate03();
            default: return null;
        }
    }

    private Timestamp getOtherMoniesEndDate(P_Employee_Roe roe, int index) 
    {
        switch (index) 
        {
            case 1: return roe.getOtherMoniesEndDate01();
            case 2: return roe.getOtherMoniesEndDate02();
            case 3: return roe.getOtherMoniesEndDate03();
            default: return null;
        }
    }

    private BigDecimal getOtherMoniesAmount(P_Employee_Roe roe, int index) 
    {
        switch (index) 
        {
            case 1: return roe.getOtherMoniesAmount01();
            case 2: return roe.getOtherMoniesAmount02();
            case 3: return roe.getOtherMoniesAmount03();
            default: return BigDecimal.ZERO;
        }
    }

    private void setSpecialPayment(P_Employee_Roe roe, int index, Timestamp start, Timestamp end, BigDecimal amount, String period) 
    {
        switch (index) 
        {
            case 1:
                roe.setSpecialPaymentStartDate01(start);
                roe.setSpecialPaymentEndDate01(end);
                roe.setSpecialPaymentAmount01(amount);
                roe.setSpecialPaymentPeriod01(period);
                break;
            case 2:
                roe.setSpecialPaymentStartDate02(start);
                roe.setSpecialPaymentEndDate02(end);
                roe.setSpecialPaymentAmount02(amount);
                roe.setSpecialPaymentPeriod02(period);
                break;
            case 3:
                roe.setSpecialPaymentStartDate03(start);
                roe.setSpecialPaymentEndDate03(end);
                roe.setSpecialPaymentAmount03(amount);
                roe.setSpecialPaymentPeriod03(period);
                break;
            case 4:
                roe.setSpecialPaymentStartDate04(start);
                roe.setSpecialPaymentEndDate04(end);
                roe.setSpecialPaymentAmount04(amount);
                roe.setSpecialPaymentPeriod04(period);
                break;
        }
    }

    private Timestamp getSpecialPaymentStartDate(P_Employee_Roe roe, int index) 
    {
        switch (index) 
        {
            case 1: return roe.getSpecialPaymentStartDate01();
            case 2: return roe.getSpecialPaymentStartDate02();
            case 3: return roe.getSpecialPaymentStartDate03();
            case 4: return roe.getSpecialPaymentStartDate04();
            default: return null;
        }
    }

    private Timestamp getSpecialPaymentEndDate(P_Employee_Roe roe, int index) 
    {
        switch (index) 
        {
            case 1: return roe.getSpecialPaymentEndDate01();
            case 2: return roe.getSpecialPaymentEndDate02();
            case 3: return roe.getSpecialPaymentEndDate03();
            case 4: return roe.getSpecialPaymentEndDate04();
            default: return null;
        }
    }

    private BigDecimal getSpecialPaymentAmount(P_Employee_Roe roe, int index) 
    {
        switch (index) 
        {
            case 1: return roe.getSpecialPaymentAmount01();
            case 2: return roe.getSpecialPaymentAmount02();
            case 3: return roe.getSpecialPaymentAmount03();
            case 4: return roe.getSpecialPaymentAmount04();
            default: return BigDecimal.ZERO;
        }
    }

    private String getSpecialPaymentPeriod(P_Employee_Roe roe, int index) 
    {
        switch (index) 
        {
            case 1: return roe.getSpecialPaymentPeriod01();
            case 2: return roe.getSpecialPaymentPeriod02();
            case 3: return roe.getSpecialPaymentPeriod03();
            case 4: return roe.getSpecialPaymentPeriod04();
            default: return "W";
        }
    }

    // --- Utilitaires graphiques ---

    /**
     * Cr\u00e9e une bo\u00eete avec son en-t\u00eate officiel num\u00e9rot\u00e9
     */
    private JPanel createBox(String number, String title, boolean required, JComponent content) 
    {
        JPanel box = new JPanel(new BorderLayout());
        box.setBorder(BorderFactory.createLineBorder(BOX_BORDER_COLOR, 1));
        box.setBackground(Color.WHITE);

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
        header.setBackground(BOX_HEADER_BG);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BOX_BORDER_COLOR));

        if (number != null && number.trim().length() > 0) 
        {
            JLabel badge = new JLabel(" " + number + " ");
            badge.setOpaque(true);
            badge.setBackground(BADGE_BG);
            badge.setForeground(BADGE_FG);
            badge.setFont(new Font("SansSerif", Font.BOLD, 10));
            badge.setBorder(BorderFactory.createEmptyBorder(1, 4, 1, 4));
            header.add(badge);
        }

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblTitle.setForeground(new Color(30, 30, 30));
        header.add(lblTitle);

        if (required) 
        {
            JLabel star = new JLabel("*");
            star.setForeground(Color.RED);
            star.setFont(new Font("SansSerif", Font.BOLD, 12));
            header.add(star);
        }

        box.add(header, BorderLayout.NORTH);
        if (content != null) 
        {
            content.setOpaque(false);
            box.add(content, BorderLayout.CENTER);
        }
        return box;
    }

    private JPanel createSingleComponentPanel(JComponent comp) 
    {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
        p.add(comp);
        return p;
    }

    private BigDecimal parseBigDecimal(String str) 
    {
        if (str == null || str.trim().length() == 0) return BigDecimal.ZERO;
        try 
        {
            String cleaned = str.trim().replace(" ", "").replace(",", ".");
            return new BigDecimal(cleaned).setScale(2, BigDecimal.ROUND_HALF_UP);
        } 
        catch (Exception e) 
        {
            return BigDecimal.ZERO;
        }
    }

    private void selectComboValue(JComboBox<ValueNamePair> cbo, String value) 
    {
        if (cbo == null) return;
        if (value == null) 
        {
            cbo.setSelectedIndex(-1);
            return;
        }
        for (int i = 0; i < cbo.getItemCount(); i++) 
        {
            ValueNamePair vp = cbo.getItemAt(i);
            if (vp != null && value.equalsIgnoreCase(vp.getValue())) 
            {
                cbo.setSelectedIndex(i);
                return;
            }
        }
        cbo.setSelectedIndex(-1);
    }

    private void selectKeyComboValue(JComboBox<KeyNamePair> cbo, int key) 
    {
        if (cbo == null) return;
        for (int i = 0; i < cbo.getItemCount(); i++) 
        {
            KeyNamePair kp = cbo.getItemAt(i);
            if (kp != null && kp.getKey() == key) 
            {
                cbo.setSelectedIndex(i);
                return;
            }
        }
        cbo.setSelectedIndex(-1);
    }

    // --- Listes d\u00e9roulantes statiques ---

    private void initPayPeriodTypes(JComboBox<ValueNamePair> cbo) 
    {
        cbo.addItem(new ValueNamePair("", ""));
        cbo.addItem(new ValueNamePair("B", "B - Quinzaine"));
        cbo.addItem(new ValueNamePair("M", "M = Mensuel"));
        cbo.addItem(new ValueNamePair("O", "O = Mensuel non conventionnel"));
        cbo.addItem(new ValueNamePair("S", "S = Bimensuel"));
        cbo.addItem(new ValueNamePair("E", "E = Bimensuel non conventionnel"));
        cbo.addItem(new ValueNamePair("H", "H = 13 p\u00e9riodes de paye par ann\u00e9e"));
        cbo.addItem(new ValueNamePair("W", "W = Hebdomadaire"));
    }

    private void initReasons(JComboBox<ValueNamePair> cbo) 
    {
        cbo.addItem(new ValueNamePair("", ""));
        cbo.addItem(new ValueNamePair("A00", "A00 Manque de travail / Fin de saison ou de contrat"));
        cbo.addItem(new ValueNamePair("A01", "A01 Faillite de l'employeur ou mise sous s\u00e9questre"));
        cbo.addItem(new ValueNamePair("B00", "B00 Gr\u00e8ve ou lockout"));
        cbo.addItem(new ValueNamePair("D00", "D00 Maladie ou blessure"));
        cbo.addItem(new ValueNamePair("E00", "E00 D\u00e9part volontaire"));
        cbo.addItem(new ValueNamePair("E02", "E02 D\u00e9part volontaire / Pour accompagner un(e) conjoint(e)"));
        cbo.addItem(new ValueNamePair("E03", "E03 D\u00e9part volontaire / Retour aux \u00e9tudes"));
        cbo.addItem(new ValueNamePair("E04", "E04 D\u00e9part volontaire / Raisons m\u00e9dicales"));
        cbo.addItem(new ValueNamePair("E05", "E05 D\u00e9part volontaire / Retraite volontaire"));
        cbo.addItem(new ValueNamePair("E06", "E06 D\u00e9part volontaire / Autre emploi"));
        cbo.addItem(new ValueNamePair("E09", "E09 D\u00e9part volontaire / D\u00e9m\u00e9nagement de l'employeur"));
        cbo.addItem(new ValueNamePair("E10", "E10 D\u00e9part volontaire / Prendre soin d'une personne \u00e0 charge"));
        cbo.addItem(new ValueNamePair("E11", "E11 D\u00e9part volontaire / Pour devenir travailleur ind\u00e9pendant"));
        cbo.addItem(new ValueNamePair("F00", "F00 Maternit\u00e9"));
        cbo.addItem(new ValueNamePair("G00", "G00 Retraite obligatoire"));
        cbo.addItem(new ValueNamePair("G07", "G07 Retraite / Approuv\u00e9e dans le cadre d'un programme de compression du personnel"));
        cbo.addItem(new ValueNamePair("H00", "H00 Travail partag\u00e9"));
        cbo.addItem(new ValueNamePair("J00", "J00 Formation en apprentissage"));
        cbo.addItem(new ValueNamePair("K00", "K00 Autre"));
        cbo.addItem(new ValueNamePair("K12", "K12 Autre / Changement de la fr\u00e9quence de paie"));
        cbo.addItem(new ValueNamePair("K13", "K13 Autre / Changement de propri\u00e9taire"));
        cbo.addItem(new ValueNamePair("K14", "K14 Autre / \u00c0 la demande de l'assurance-emploi"));
        cbo.addItem(new ValueNamePair("K15", "K15 Autre / Forces canadiennes - Ordonnances/r\u00e8glements royaux"));
        cbo.addItem(new ValueNamePair("K16", "K16 Autre / \u00c0 la demande de l'employ\u00e9(e)"));
        cbo.addItem(new ValueNamePair("K17", "K17 Autre / Changement de fournisseur de service"));
        cbo.addItem(new ValueNamePair("M00", "M00 Cong\u00e9diement"));
        cbo.addItem(new ValueNamePair("M08", "M08 Cong\u00e9diement / Cong\u00e9di\u00e9 avant la fin de la p\u00e9riode de probation"));
        cbo.addItem(new ValueNamePair("N00", "N00 Cong\u00e9"));
        cbo.addItem(new ValueNamePair("P00", "P00 Parental"));
        cbo.addItem(new ValueNamePair("Z00", "Z00 Cong\u00e9 de compassion"));
    }

    private void initVacationCodes(JComboBox<ValueNamePair> cbo) 
    {
        cbo.addItem(new ValueNamePair("", " "));
        cbo.addItem(new ValueNamePair("1", "Montant vers\u00e9 \u00e0 chaque paie"));
        cbo.addItem(new ValueNamePair("2", "Montant vers\u00e9 parce que l'employ\u00e9 cesse de travailler"));
        cbo.addItem(new ValueNamePair("3", "Montant vers\u00e9 pour une p\u00e9riode de vacances"));
        cbo.addItem(new ValueNamePair("4", "Anniversaire (Montant vers\u00e9 \u00e0 la m\u00eame date chaque ann\u00e9e)"));
    }

    private void initOtherMoniesCodes(JComboBox<ValueNamePair> cbo) 
    {
        cbo.addItem(new ValueNamePair("", " "));
        cbo.addItem(new ValueNamePair("B05", "B05 - Prime (jours f\u00e9ri\u00e9s)"));
        cbo.addItem(new ValueNamePair("B06", "B06 - Prime (pour r\u00e9compenser le rendement/la productivit\u00e9)"));
        cbo.addItem(new ValueNamePair("B07", "B07 - Prime (\u00e9v\u00e9nements particuliers)"));
        cbo.addItem(new ValueNamePair("B08", "B08 - Prime (engagement/fin de contrat/fin de saison)"));
        cbo.addItem(new ValueNamePair("B09", "B09 - Prime (d\u00e9part ou retraite)"));
        cbo.addItem(new ValueNamePair("B10", "B10 - Prime (fermeture)"));
        cbo.addItem(new ValueNamePair("B11", "B11 - Prime (autre)"));
        cbo.addItem(new ValueNamePair("E00", "E00 - Indemnit\u00e9 de d\u00e9part"));
        cbo.addItem(new ValueNamePair("G00", "G00 - Gratifications"));
        cbo.addItem(new ValueNamePair("H00", "H00 - Honoraires"));
        cbo.addItem(new ValueNamePair("I00", "I00 - Cr\u00e9dits de cong\u00e9 de maladie"));
        cbo.addItem(new ValueNamePair("J00", "J00 - Ajustement r\u00e9troactif de paie"));
        cbo.addItem(new ValueNamePair("O00", "O00 - Autre"));
        cbo.addItem(new ValueNamePair("Q00", "Q00 - Participation aux b\u00e9n\u00e9fices"));
        cbo.addItem(new ValueNamePair("R00", "R00 - Indemnit\u00e9 de retraite/cr\u00e9dits de cong\u00e9 de retraite"));
        cbo.addItem(new ValueNamePair("S00", "S00 - R\u00e8glement d'un diff\u00e9rend"));
        cbo.addItem(new ValueNamePair("T00", "T00 - Heures suppl\u00e9mentaires accumul\u00e9es pay\u00e9es"));
        cbo.addItem(new ValueNamePair("U12", "U12 - PSC - Maternit\u00e9/parental/compassion/parents d'enfants gravement malades"));
        cbo.addItem(new ValueNamePair("U13", "U13 - PSC Mise \u00e0 pied"));
        cbo.addItem(new ValueNamePair("U14", "U14 - PSC Maladie"));
        cbo.addItem(new ValueNamePair("U15", "U15 - PSC Formation"));
        cbo.addItem(new ValueNamePair("Y00", "Y00 - Indemnit\u00e9 de pr\u00e9avis"));
    }

    private void initLanguages() 
    {
        cboBox20_PrefLanguage.removeAllItems();
        cboBox23_PrintLanguage.removeAllItems();

        String sql = "SELECT p_language.p_language_id, COALESCE(p_language_trl.name, p_language.name) AS name "
                   + "FROM p_language "
                   + "LEFT OUTER JOIN p_language_trl ON (p_language_trl.p_language_id = p_language.p_language_id "
                   + "                                  AND ad_language = '" + Env.getContext(Env.getCtx(), "#AD_Language") + "') "
                   + "WHERE p_language.value LIKE '%CA' "
                   + "ORDER BY name";

        try 
        {
            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) 
            {
                KeyNamePair kp = new KeyNamePair(rs.getInt(1), rs.getString(2));
                cboBox20_PrefLanguage.addItem(kp);
                cboBox23_PrintLanguage.addItem(kp);
            }
            rs.close();
            pstmt.close();
        } 
        catch (Exception e) 
        {
            s_log.log(Level.SEVERE, "VReleveEmploi.initLanguages", e);
        }
    }
}
