/******************************************************************************
 * Product: Solstice+ Payroll & Human Resources Management                    *
 * Copyright (C) 2008-2026 ProGestion Informatique, Inc. All Rights Reserved. *
 ******************************************************************************/
package org.solstice.apps.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.compiere.apps.AEnv;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.model.MTable;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.solstice.util.ExcelExportUtil;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Interface d'audit et de validation des remboursements Zoho Expense vs Paie Solstice.
 * <p>
 * Permet de r\u00e9concilier sur une ou plusieurs p\u00e9riodes de paie :
 * <ul>
 *   <li>Les d\u00e9penses import\u00e9es depuis Zoho Expense (P_Employee_Expensereports_Validation & Detail)</li>
 *   <li>Les feuilles de temps de comptes de d\u00e9penses g\u00e9n\u00e9r\u00e9es (P_Time_Sheet)</li>
 *   <li>Les paiements r\u00e9ellement vers\u00e9s dans la paie (P_Payment & P_Payment_Gain)</li>
 * </ul>
 * D\u00e9tecte automatiquement :
 * <ul>
 *   <li>\uD83D\uDD34 Les DOUBLONS : Employ\u00e9s ayant re\u00e7u 2 remboursements ou plus pour une m\u00eame d\u00e9pense</li>
 *   <li>\uD83D\uDFE0 Les NON REMBOURS\u00c9S : D\u00e9penses Zoho approuv\u00e9es mais non pay\u00e9es en paie</li>
 *   <li>\uD83D\uDFE1 Les \u00c9CARTS DE MONTANT : Diff\u00e9rence entre montant Zoho et montant vers\u00e9</li>
 *   <li>\uD83D\uDFE2 Les CONFORMES : 1 d\u00e9pense = 1 paiement avec montant identique</li>
 * </ul>
 *
 * @author Solstice+ / Marc Morissette / Antigravity
 */
public class VZohoExpenseValidation extends CPanel implements FormPanel, ActionListener, ListSelectionListener
{
    private static final long serialVersionUID = 1L;
    private static final CLogger s_log = CLogger.getCLogger(VZohoExpenseValidation.class);

    private static final DecimalFormat CURRENCY_FMT = new DecimalFormat("#,##0.00 $");
    private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("yyyy-MM-dd");

    // Constantes de statut d'audit
    public static final String STATUS_DUPLICATE = "DOUBLON";
    public static final String STATUS_UNREIMBURSED = "NON_REMBOURSE";
    public static final String STATUS_AMOUNT_DIFF = "ECART_MONTANT";
    public static final String STATUS_MATCHED = "CONFORME";
    public static final String STATUS_NO_ZOHO = "PAIEMENT_SANS_ZOHO";

    private Properties m_ctx;
    private FormFrame m_frame = null;
    private int m_WindowNo = 0;

    // Filtres & S\u00e9lecteurs
    private JComboBox<KeyNamePair> comboPeriodFrom;
    private JComboBox<KeyNamePair> comboPeriodTo;
    private JComboBox<String> comboPreset;
    private JComboBox<KeyNamePair> comboOrg;
    private JComboBox<String> comboAnomaly;
    private JTextField txtSearch;

    // Boutons d'action
    private JButton btnSyncZoho;
    private JButton btnRefresh;
    private JButton btnExportExcel;
    private JButton btnZoomTS;
    private JButton btnZoomPay;
    private JButton btnZoomEmp;

    // Cache jeton OAuth2 Zoho
    private static String s_cachedToken = null;
    private static long s_tokenExpiryTime = 0;

    // Cartes KPIs
    private JLabel lblKpiZohoTotal;
    private JLabel lblKpiZohoCount;
    private JLabel lblKpiPayTotal;
    private JLabel lblKpiPayCount;
    private JLabel lblKpiDupTotal;
    private JLabel lblKpiDupCount;
    private JLabel lblKpiUnreimbTotal;
    private JLabel lblKpiUnreimbCount;
    private JLabel lblKpiDiffTotal;
    private JLabel lblKpiDiffCount;
    private JLabel lblKpiMatchedTotal;
    private JLabel lblKpiMatchedCount;

    // Table principale d'audit
    private JTable tblMaster;
    private AuditMasterTableModel masterModel;
    private TableRowSorter<AuditMasterTableModel> masterSorter;

    // Tables d\u00e9tails du bas
    private JTable tblZohoDetail;
    private ZohoDetailTableModel zohoDetailModel;

    private JTable tblPayrollDetail;
    private PayrollDetailTableModel payrollDetailModel;

    private JTable tblEmpHistory;
    private EmpHistoryTableModel empHistoryModel;

    private JEditorPane guidePane;

    // Donn\u00e9es en m\u00e9moire
    private final List<PeriodInfo> m_allPeriods = new ArrayList<PeriodInfo>();
    private final List<AuditRow> m_rows = new ArrayList<AuditRow>();
    private final List<ZohoItemDetail> m_currentZohoDetails = new ArrayList<ZohoItemDetail>();
    private final List<PayrollGainDetail> m_currentPayrollDetails = new ArrayList<PayrollGainDetail>();
    private final List<EmpHistoryItem> m_currentEmpHistory = new ArrayList<EmpHistoryItem>();

    private AuditRow m_selectedRow = null;

    /**
     * Constructeur standard
     */
    public VZohoExpenseValidation()
    {
        super();
        m_ctx = Env.getCtx();
        jbInit();
        dynInit();
    }

    @Override
    public void init(int WindowNo, FormFrame frame)
    {
        this.m_WindowNo = WindowNo;
        this.m_frame = frame;
        if (m_frame != null)
        {
            m_frame.setTitle("Audit & Validation des Remboursements Zoho Expense vs Paie");
            m_frame.setPreferredSize(new Dimension(1380, 880));
            m_frame.getContentPane().add(this, BorderLayout.CENTER);
            m_frame.pack();
        }
    }

    @Override
    public void dispose()
    {
        if (m_frame != null)
            m_frame.dispose();
        m_frame = null;
    }

    /**
     * Construction de l'interface graphique
     */
    private void jbInit()
    {
        setLayout(new BorderLayout(0, 6));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        // Panneau Sup\u00e9rieur : Barre de Filtres + Cartes KPIs
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        northPanel.setOpaque(false);

        northPanel.add(buildFilterPanel());
        northPanel.add(Box.createVerticalStrut(8));
        northPanel.add(buildKpiPanel());

        add(northPanel, BorderLayout.NORTH);

        // Panneau Central : SplitPane (Tableau d'audit en haut / Onglets d\u00e9tails en bas)
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.62);
        splitPane.setContinuousLayout(true);
        splitPane.setBorder(null);

        // Haut : Table principale
        JPanel masterPanel = new JPanel(new BorderLayout(0, 4));
        masterPanel.setOpaque(false);

        JLabel lblMasterTitle = new JLabel("  Enregistrements d'audit & r\u00e9conciliation Zoho vs Paie Solstice :");
        lblMasterTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblMasterTitle.setForeground(new Color(40, 50, 70));
        masterPanel.add(lblMasterTitle, BorderLayout.NORTH);

        masterModel = new AuditMasterTableModel();
        tblMaster = new JTable(masterModel);
        masterSorter = new TableRowSorter<AuditMasterTableModel>(masterModel);
        tblMaster.setRowSorter(masterSorter);
        setupMasterTable(tblMaster);

        tblMaster.getSelectionModel().addListSelectionListener(this);
        tblMaster.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getClickCount() == 2 && tblMaster.getSelectedRow() >= 0)
                {
                    zoomToSelected();
                }
            }
        });

        JScrollPane scrollMaster = new JScrollPane(tblMaster);
        scrollMaster.setBorder(BorderFactory.createLineBorder(new Color(210, 218, 230)));
        masterPanel.add(scrollMaster, BorderLayout.CENTER);

        splitPane.setTopComponent(masterPanel);

        // Bas : Onglets de d\u00e9tail
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Onglet 1 : D\u00e9tail Zoho (re\u00e7us & taxes)
        zohoDetailModel = new ZohoDetailTableModel();
        tblZohoDetail = new JTable(zohoDetailModel);
        setupDetailTable(tblZohoDetail);
        JScrollPane scrollZoho = new JScrollPane(tblZohoDetail);
        scrollZoho.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        tabbedPane.addTab("D\u00e9tail Re\u00e7us & Taxes Zoho (P_Employee_Expensereports_Detail)", scrollZoho);

        // Onglet 2 : Gains de paie
        payrollDetailModel = new PayrollDetailTableModel();
        tblPayrollDetail = new JTable(payrollDetailModel);
        setupDetailTable(tblPayrollDetail);
        JScrollPane scrollPayroll = new JScrollPane(tblPayrollDetail);
        scrollPayroll.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        tabbedPane.addTab("Gains de Paie G\u00e9n\u00e9r\u00e9s (GL02 / TPS / TVQ / TVH)", scrollPayroll);

        // Onglet 3 : Historique de l'employ\u00e9
        empHistoryModel = new EmpHistoryTableModel();
        tblEmpHistory = new JTable(empHistoryModel);
        setupDetailTable(tblEmpHistory);
        JScrollPane scrollHistory = new JScrollPane(tblEmpHistory);
        scrollHistory.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        tabbedPane.addTab("Historique D\u00e9penses Employ\u00e9 (Toutes p\u00e9riodes)", scrollHistory);

        // Onglet 4 : Guide d'audit
        guidePane = new JEditorPane();
        guidePane.setContentType("text/html");
        guidePane.setEditable(false);
        updateGuideContent();
        JScrollPane scrollGuide = new JScrollPane(guidePane);
        scrollGuide.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        tabbedPane.addTab("Guide d'Audit & Proc\u00e9dure de R\u00e9gularisation", scrollGuide);

        splitPane.setBottomComponent(tabbedPane);

        add(splitPane, BorderLayout.CENTER);
    }

    /**
     * Panneau sup\u00e9rieur avec titre, s\u00e9lecteurs multi-p\u00e9riodes et actions
     */
    private JPanel buildFilterPanel()
    {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 226, 235), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 4, 3, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Ligne 0 : Titre & Boutons d'action
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4; gbc.anchor = GridBagConstraints.WEST;
        JLabel lblTitle = new JLabel("Audit des Remboursements Zoho Expense dans la Paie");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(new Color(20, 35, 60));
        panel.add(lblTitle, gbc);

        gbc.gridx = 4; gbc.gridwidth = 4; gbc.anchor = GridBagConstraints.EAST;
        JPanel btnBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        btnBox.setOpaque(false);

        btnSyncZoho = createActionButton("\uD83D\uDCE5 Actualiser Zoho Expense", new Color(124, 58, 237));
        btnSyncZoho.setToolTipText("R\u00e9importer et synchroniser les rapports et d\u00e9tails depuis Zoho Expense vers P_Employee_Expensereports_Validation");

        btnRefresh = createActionButton("Actualiser", new Color(37, 99, 235));
        btnRefresh.setToolTipText("Recharger et auditer les donn\u00e9es pour les p\u00e9riodes s\u00e9lectionn\u00e9es");

        btnExportExcel = createActionButton("Exporter Excel (.xlsx)", new Color(22, 101, 52));
        btnExportExcel.setToolTipText("G\u00e9n\u00e9rer un rapport Excel d\u00e9taill\u00e9 avec codes couleurs et synth\u00e8se");

        btnZoomTS = createActionButton("Zoom Feuille de Temps", new Color(107, 114, 128));
        btnZoomTS.setToolTipText("Ouvrir la feuille de temps Solstice de la ligne s\u00e9lectionn\u00e9e");

        btnZoomPay = createActionButton("Zoom Paiement", new Color(107, 114, 128));
        btnZoomPay.setToolTipText("Ouvrir le paiement Solstice de la ligne s\u00e9lectionn\u00e9e");

        btnZoomEmp = createActionButton("Zoom Employ\u00e9", new Color(107, 114, 128));
        btnZoomEmp.setToolTipText("Ouvrir la fiche employ\u00e9");

        btnSyncZoho.addActionListener(this);
        btnBox.add(btnSyncZoho);
        btnBox.add(btnRefresh);
        btnBox.add(btnExportExcel);
        btnBox.add(btnZoomTS);
        btnBox.add(btnZoomPay);
        btnBox.add(btnZoomEmp);
        panel.add(btnBox, gbc);

        // Ligne 1 : S\u00e9lecteurs de p\u00e9riodes et plage rapide
        gbc.gridy = 1; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;

        // De P\u00e9riode
        gbc.gridx = 0;
        JLabel lblFrom = new JLabel("De P\u00e9riode :");
        lblFrom.setFont(new Font("Segoe UI", Font.BOLD, 11));
        panel.add(lblFrom, gbc);

        gbc.gridx = 1;
        comboPeriodFrom = new JComboBox<KeyNamePair>();
        comboPeriodFrom.setPreferredSize(new Dimension(210, 25));
        comboPeriodFrom.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        comboPeriodFrom.addActionListener(this);
        panel.add(comboPeriodFrom, gbc);

        // \u00c0 P\u00e9riode
        gbc.gridx = 2;
        JLabel lblTo = new JLabel("\u00c0 P\u00e9riode :");
        lblTo.setFont(new Font("Segoe UI", Font.BOLD, 11));
        panel.add(lblTo, gbc);

        gbc.gridx = 3;
        comboPeriodTo = new JComboBox<KeyNamePair>();
        comboPeriodTo.setPreferredSize(new Dimension(210, 25));
        comboPeriodTo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        comboPeriodTo.addActionListener(this);
        panel.add(comboPeriodTo, gbc);

        // Plage rapide
        gbc.gridx = 4;
        JLabel lblPreset = new JLabel("Plage rapide :");
        lblPreset.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        panel.add(lblPreset, gbc);

        gbc.gridx = 5;
        comboPreset = new JComboBox<String>(new String[] {
            "S\u00e9lection manuelle",
            "P\u00e9riode ouverte courante",
            "Derni\u00e8re p\u00e9riode ferm\u00e9e",
            "3 derni\u00e8res p\u00e9riodes",
            "6 derni\u00e8res p\u00e9riodes",
            "Ann\u00e9e courante compl\u00e8te",
            "Toutes les p\u00e9riodes (Historique)"
        });
        comboPreset.setPreferredSize(new Dimension(175, 25));
        comboPreset.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        comboPreset.addActionListener(this);
        panel.add(comboPreset, gbc);

        // Organisation
        gbc.gridx = 6;
        JLabel lblOrg = new JLabel("Organisation :");
        lblOrg.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        panel.add(lblOrg, gbc);

        gbc.gridx = 7;
        comboOrg = new JComboBox<KeyNamePair>();
        comboOrg.setPreferredSize(new Dimension(160, 25));
        comboOrg.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        comboOrg.addActionListener(this);
        panel.add(comboOrg, gbc);

        // Ligne 2 : Filtres d'anomalies et recherche textuelle
        gbc.gridy = 2;

        gbc.gridx = 0;
        JLabel lblAnomaly = new JLabel("Filtre Audit :");
        lblAnomaly.setFont(new Font("Segoe UI", Font.BOLD, 11));
        panel.add(lblAnomaly, gbc);

        gbc.gridx = 1;
        comboAnomaly = new JComboBox<String>(new String[] {
            "Tous les enregistrements",
            "\u26A0\uFE0F Toutes les anomalies (Doublons + Non rembours\u00e9s + \u00c9carts)",
            "\uD83D\uDD34 Doublons seulement (Paiements multiples)",
            "\uD83D\uDFE0 Non rembours\u00e9s seulement (En souffrance)",
            "\uD83D\uDFE1 \u00c9carts de montant seulement",
            "\uD83D\uDFE2 Conformes seulement (Sold\u00e9s sans \u00e9cart)",
            "\uD83D\uDFEA Paiements orphelins (Sans rapport Zoho)"
        });
        comboAnomaly.setPreferredSize(new Dimension(210, 25));
        comboAnomaly.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        comboAnomaly.addActionListener(this);
        panel.add(comboAnomaly, gbc);

        gbc.gridx = 2;
        JLabel lblSearch = new JLabel("Rechercher :");
        lblSearch.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        panel.add(lblSearch, gbc);

        gbc.gridx = 3; gbc.gridwidth = 3;
        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(280, 25));
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtSearch.setToolTipText("Filtrer par nom, matricule employ\u00e9, no rapport Zoho (ER-...)");
        txtSearch.getDocument().addDocumentListener(new DocumentListener()
        {
            @Override public void insertUpdate(DocumentEvent e) { applyFilters(); }
            @Override public void removeUpdate(DocumentEvent e) { applyFilters(); }
            @Override public void changedUpdate(DocumentEvent e) { applyFilters(); }
        });
        panel.add(txtSearch, gbc);

        return panel;
    }

    private JButton createActionButton(String text, Color bg)
    {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(this);
        return btn;
    }

    /**
     * Panneau de cartes indicateurs KPIs
     */
    private JPanel buildKpiPanel()
    {
        JPanel panel = new JPanel(new GridLayout(1, 6, 8, 0));
        panel.setOpaque(false);

        lblKpiZohoTotal = new JLabel("0.00 $");
        lblKpiZohoCount = new JLabel("0 rapport(s)");
        panel.add(createKpiCard("D\u00e9penses Zoho", lblKpiZohoTotal, lblKpiZohoCount, new Color(37, 99, 235)));

        lblKpiPayTotal = new JLabel("0.00 $");
        lblKpiPayCount = new JLabel("0 paiement(s)");
        panel.add(createKpiCard("Pay\u00e9 en Paie", lblKpiPayTotal, lblKpiPayCount, new Color(8, 145, 178)));

        lblKpiDupTotal = new JLabel("0.00 $");
        lblKpiDupCount = new JLabel("0 cas d\u00e9tect\u00e9(s)");
        panel.add(createKpiCard("\uD83D\uDD34 Doublons de Paie", lblKpiDupTotal, lblKpiDupCount, new Color(220, 38, 38)));

        lblKpiUnreimbTotal = new JLabel("0.00 $");
        lblKpiUnreimbCount = new JLabel("0 non rembours\u00e9(s)");
        panel.add(createKpiCard("\uD83D\uDFE0 Non Rembours\u00e9s", lblKpiUnreimbTotal, lblKpiUnreimbCount, new Color(234, 88, 12)));

        lblKpiDiffTotal = new JLabel("0.00 $");
        lblKpiDiffCount = new JLabel("0 \u00e9cart(s)");
        panel.add(createKpiCard("\uD83D\uDFE1 \u00c9carts Montant", lblKpiDiffTotal, lblKpiDiffCount, new Color(202, 138, 4)));

        lblKpiMatchedTotal = new JLabel("0.00 $");
        lblKpiMatchedCount = new JLabel("0 conforme(s)");
        panel.add(createKpiCard("\uD83D\uDFE2 Conformes (Sold\u00e9s)", lblKpiMatchedTotal, lblKpiMatchedCount, new Color(22, 163, 74)));

        return panel;
    }

    private JPanel createKpiCard(String title, JLabel lblAmount, JLabel lblCount, Color accent)
    {
        JPanel card = new JPanel(new BorderLayout(0, 2));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(3, 0, 0, 0, accent),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 230, 238), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
            )
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTitle.setForeground(new Color(90, 105, 125));

        lblAmount.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblAmount.setForeground(new Color(25, 35, 55));

        lblCount.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblCount.setForeground(new Color(120, 135, 155));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblAmount, BorderLayout.CENTER);
        card.add(lblCount, BorderLayout.SOUTH);

        return card;
    }

    /**
     * Configuration stylis\u00e9e du tableau principal avec rendu conditionnel des statuts
     */
    private void setupMasterTable(JTable table)
    {
        table.setRowHeight(24);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 235, 242));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 11));
        header.setBackground(new Color(240, 244, 250));
        header.setForeground(new Color(30, 45, 65));
        header.setPreferredSize(new Dimension(0, 28));

        // Rendu personnalis\u00e9 pour les montants
        DefaultTableCellRenderer currencyRenderer = new DefaultTableCellRenderer()
        {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r, int c)
            {
                super.getTableCellRendererComponent(t, val, sel, foc, r, c);
                setHorizontalAlignment(SwingConstants.RIGHT);
                if (val instanceof BigDecimal)
                {
                    BigDecimal bd = (BigDecimal) val;
                    setText(CURRENCY_FMT.format(bd));
                    if (c == 13) // Colonne \u00c9cart
                    {
                        if (bd.compareTo(BigDecimal.ZERO) > 0)
                            setForeground(new Color(185, 28, 28)); // Trop pay\u00e9
                        else if (bd.compareTo(BigDecimal.ZERO) < 0)
                            setForeground(new Color(194, 65, 12)); // Moins pay\u00e9
                        else
                            setForeground(sel ? t.getSelectionForeground() : new Color(30, 41, 59));
                    }
                    else
                    {
                        setForeground(sel ? t.getSelectionForeground() : new Color(30, 41, 59));
                    }
                }
                return this;
            }
        };

        // Rendu personnalis\u00e9 pour le statut d'audit (Badges color\u00e9s)
        DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer()
        {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r, int c)
            {
                super.getTableCellRendererComponent(t, val, sel, foc, r, c);
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 11));

                String str = val != null ? val.toString() : "";
                if (!sel)
                {
                    if (str.contains(STATUS_DUPLICATE))
                    {
                        setBackground(new Color(254, 226, 226)); // Rouge clair
                        setForeground(new Color(153, 27, 27));   // Rouge fonc\u00e9
                    }
                    else if (str.contains(STATUS_UNREIMBURSED))
                    {
                        setBackground(new Color(255, 237, 213)); // Orange clair
                        setForeground(new Color(154, 52, 18));   // Orange fonc\u00e9
                    }
                    else if (str.contains(STATUS_AMOUNT_DIFF))
                    {
                        setBackground(new Color(254, 249, 195)); // Jaune clair
                        setForeground(new Color(133, 77, 14));   // Marron/Jaune
                    }
                    else if (str.contains(STATUS_MATCHED))
                    {
                        setBackground(new Color(220, 252, 231)); // Vert clair
                        setForeground(new Color(22, 101, 52));   // Vert fonc\u00e9
                    }
                    else
                    {
                        setBackground(new Color(243, 232, 255)); // Mauve clair
                        setForeground(new Color(107, 33, 168));  // Mauve fonc\u00e9
                    }
                }
                return this;
            }
        };

        table.getColumnModel().getColumn(0).setCellRenderer(statusRenderer); // Statut
        table.getColumnModel().getColumn(7).setCellRenderer(currencyRenderer); // Montant Zoho
        table.getColumnModel().getColumn(12).setCellRenderer(currencyRenderer); // Montant Paie
        table.getColumnModel().getColumn(13).setCellRenderer(currencyRenderer); // \u00c9cart

        // Largeurs de colonnes
        table.getColumnModel().getColumn(0).setPreferredWidth(170); // Statut
        table.getColumnModel().getColumn(1).setPreferredWidth(110); // P\u00e9riode
        table.getColumnModel().getColumn(2).setPreferredWidth(80);  // Matricule
        table.getColumnModel().getColumn(3).setPreferredWidth(140); // Nom employ\u00e9
        table.getColumnModel().getColumn(4).setPreferredWidth(95);  // No Zoho
        table.getColumnModel().getColumn(5).setPreferredWidth(150); // Nom rapport
        table.getColumnModel().getColumn(6).setPreferredWidth(85);  // Date approbation
        table.getColumnModel().getColumn(7).setPreferredWidth(90);  // Montant Zoho
        table.getColumnModel().getColumn(8).setPreferredWidth(75);  // ID Feuille
        table.getColumnModel().getColumn(9).setPreferredWidth(60);  // Statut TS
        table.getColumnModel().getColumn(10).setPreferredWidth(75); // ID Paiement
        table.getColumnModel().getColumn(11).setPreferredWidth(85); // Date Paie
        table.getColumnModel().getColumn(12).setPreferredWidth(95); // Montant Paie
        table.getColumnModel().getColumn(13).setPreferredWidth(85); // \u00c9cart
        table.getColumnModel().getColumn(14).setPreferredWidth(65); // Nb Paies
        table.getColumnModel().getColumn(15).setPreferredWidth(210);// D\u00e9tail multi-paies
        table.getColumnModel().getColumn(16).setPreferredWidth(80); // Statut Zoho
        table.getColumnModel().getColumn(17).setPreferredWidth(220);// Diagnostic
    }

    private void setupDetailTable(JTable table)
    {
        table.setRowHeight(22);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 235, 242));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 11));
        header.setBackground(new Color(245, 247, 252));
        header.setForeground(new Color(30, 45, 65));
        header.setPreferredSize(new Dimension(0, 24));
    }

    /**
     * Initialisation dynamique : chargement des p\u00e9riodes et des organisations
     */
    private void dynInit()
    {
        loadPeriods();
        loadOrganizations();

        // Par d\u00e9faut : S\u00e9lectionner la p\u00e9riode courante
        selectCurrentPeriodPreset();
        executeQuery();
    }

    private void loadPeriods()
    {
        m_allPeriods.clear();
        comboPeriodFrom.removeAllItems();
        comboPeriodTo.removeAllItems();

        String sql = "SELECT p.P_Period_ID, p.PeriodNo, p.Name, p.StartDate, p.EndDate, p.PeriodStatus Status, y.Year FiscalYear " +
                     "FROM P_Period p " +
                     "INNER JOIN P_Year y ON y.P_Year_ID = p.P_Year_ID " +
                     "WHERE p.AD_Client_ID = " + Env.getAD_Client_ID(m_ctx) + " " +
                     "ORDER BY p.StartDate DESC, p.PeriodNo DESC";

        try (PreparedStatement pstmt = DB.prepareStatement(sql, null);
             ResultSet rs = pstmt.executeQuery())
        {
            while (rs.next())
            {
                PeriodInfo pi = new PeriodInfo();
                pi.periodId = rs.getInt("P_Period_ID");
                pi.periodNo = rs.getInt("PeriodNo");
                pi.name = rs.getString("Name");
                pi.startDate = rs.getTimestamp("StartDate");
                pi.endDate = rs.getTimestamp("EndDate");
                pi.status = rs.getString("Status");
                pi.fiscalYear = rs.getString("FiscalYear");

                m_allPeriods.add(pi);

                String label = pi.name + " (" + DATE_FMT.format(pi.startDate) + " \u00e0 " + DATE_FMT.format(pi.endDate) + ")";
                KeyNamePair knp = new KeyNamePair(pi.periodId, label);
                comboPeriodFrom.addItem(knp);
                comboPeriodTo.addItem(knp);
            }
        }
        catch (Exception e)
        {
            s_log.log(Level.SEVERE, "loadPeriods", e);
        }
    }

    private void loadOrganizations()
    {
        comboOrg.removeAllItems();
        comboOrg.addItem(new KeyNamePair(0, "Toutes les organisations"));

        String sql = "SELECT AD_Org_ID, Name FROM AD_Org WHERE AD_Client_ID IN (0, " + Env.getAD_Client_ID(m_ctx) + ") AND IsActive = 'Y' ORDER BY Value, Name";
        try (PreparedStatement pstmt = DB.prepareStatement(sql, null);
             ResultSet rs = pstmt.executeQuery())
        {
            while (rs.next())
            {
                comboOrg.addItem(new KeyNamePair(rs.getInt("AD_Org_ID"), rs.getString("Name")));
            }
        }
        catch (Exception e)
        {
            s_log.log(Level.SEVERE, "loadOrganizations", e);
        }
    }

    private void selectCurrentPeriodPreset()
    {
        if (m_allPeriods.isEmpty())
            return;

        // Trouver la p\u00e9riode ouverte
        int openIdx = -1;
        for (int i = 0; i < m_allPeriods.size(); i++)
        {
            if ("O".equalsIgnoreCase(m_allPeriods.get(i).status) || "Open".equalsIgnoreCase(m_allPeriods.get(i).status))
            {
                openIdx = i;
                break;
            }
        }
        if (openIdx < 0)
            openIdx = 0;

        comboPeriodFrom.setSelectedIndex(openIdx);
        comboPeriodTo.setSelectedIndex(openIdx);
    }

    /**
     * R\u00e9cup\u00e8re la liste des P_Period_ID dans la plage s\u00e9lectionn\u00e9e
     */
    private List<Integer> getSelectedPeriodIds()
    {
        List<Integer> list = new ArrayList<Integer>();
        KeyNamePair knpFrom = (KeyNamePair) comboPeriodFrom.getSelectedItem();
        KeyNamePair knpTo = (KeyNamePair) comboPeriodTo.getSelectedItem();

        if (knpFrom == null && knpTo == null)
            return list;

        int fromId = knpFrom != null ? knpFrom.getKey() : 0;
        int toId = knpTo != null ? knpTo.getKey() : 0;

        if (fromId == toId && fromId > 0)
        {
            list.add(fromId);
            return list;
        }

        PeriodInfo pFrom = findPeriod(fromId);
        PeriodInfo pTo = findPeriod(toId);

        if (pFrom == null || pTo == null)
        {
            if (fromId > 0) list.add(fromId);
            if (toId > 0 && toId != fromId) list.add(toId);
            return list;
        }

        Date minDate = pFrom.startDate.before(pTo.startDate) ? pFrom.startDate : pTo.startDate;
        Date maxDate = pFrom.endDate.after(pTo.endDate) ? pFrom.endDate : pTo.endDate;

        for (PeriodInfo p : m_allPeriods)
        {
            if (!p.endDate.before(minDate) && !p.startDate.after(maxDate))
            {
                list.add(p.periodId);
            }
        }
        return list;
    }

    private PeriodInfo findPeriod(int periodId)
    {
        for (PeriodInfo p : m_allPeriods)
        {
            if (p.periodId == periodId)
                return p;
        }
        return null;
    }

    /**
     * Moteur principal d'audit et de r\u00e9conciliation
     */
    public void executeQuery()
    {
        List<Integer> periodIds = getSelectedPeriodIds();
        if (periodIds.isEmpty())
        {
            m_rows.clear();
            masterModel.fireTableDataChanged();
            updateKpis();
            return;
        }

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        m_rows.clear();

        StringBuilder periodListSb = new StringBuilder();
        for (int i = 0; i < periodIds.size(); i++)
        {
            if (i > 0) periodListSb.append(",");
            periodListSb.append(periodIds.get(i));
        }
        String periodInClause = periodListSb.toString();

        KeyNamePair orgKnp = (KeyNamePair) comboOrg.getSelectedItem();
        int orgId = orgKnp != null ? orgKnp.getKey() : 0;
        String orgFilter = orgId > 0 ? " AND emp.AD_Org_ID = " + orgId : "";

        try
        {
            // 1. Charger tous les rapports Zoho import\u00e9s dans la p\u00e9riode depuis la table permanente P_Employee_Expensereports_Validation
            String sqlZoho = "SELECT " +
                " zr.P_Employee_Expensereports_Validation_ID, zr.P_Employee_Expensereports_ID, zr.Report_Id, zr.Report_Number, zr.Report_Name, zr.Description, " +
                " zr.P_Employee_ID, zr.Employee_Number AS Zoho_Emp_No, emp.Value AS Solstice_Emp_No, " +
                " emp.Name AS Emp_LastName, emp.FirstName AS Emp_FirstName, " +
                " zr.P_Period_ID, per.Name AS Period_Name, per.PeriodNo, " +
                " zr.P_Time_Sheet_ID, zr.P_Payment_ID, " +
                " zr.Reimbursable_Total, zr.Amount_To_Be_Reimbursed, zr.Total, " +
                " zr.Status AS Zoho_Status, zr.Approved_Date, zr.Submitted_Date, zr.Reimbursement_Date, " +
                " CASE WHEN zr.Reimbursable_Total > 0 THEN 'Y' ELSE 'N' END AS Is_Reimbursable " +
                "FROM P_Employee_Expensereports_Validation zr " +
                "LEFT JOIN P_Employee emp ON emp.P_Employee_ID = zr.P_Employee_ID " +
                "LEFT JOIN P_Period per ON per.P_Period_ID = zr.P_Period_ID " +
                "WHERE zr.AD_Client_ID = " + Env.getAD_Client_ID(m_ctx) + " " +
                "  AND (zr.ExpensereportsType IS NULL OR zr.ExpensereportsType = 'ZohoExpense') " +
                "  AND zr.P_Period_ID IN (" + periodInClause + ") " +
                orgFilter + " " +
                "ORDER BY per.PeriodNo DESC, emp.Value ASC, zr.Report_Number ASC";

            List<ZohoRowRaw> zohoRows = new ArrayList<ZohoRowRaw>();
            Set<Integer> employeesInQuery = new HashSet<Integer>();

            try (PreparedStatement pstmt = DB.prepareStatement(sqlZoho, null);
                 ResultSet rs = pstmt.executeQuery())
            {
                while (rs.next())
                {
                    ZohoRowRaw z = new ZohoRowRaw();
                    int valId = rs.getInt("P_Employee_Expensereports_Validation_ID");
                    z.expenseReportId = valId > 0 ? valId : rs.getInt("P_Employee_Expensereports_ID");
                    z.reportId = rs.getString("Report_Id");
                    z.reportNumber = rs.getString("Report_Number");
                    z.reportName = rs.getString("Report_Name");
                    z.description = rs.getString("Description");
                    z.employeeId = rs.getInt("P_Employee_ID");
                    z.zohoEmpNo = rs.getString("Zoho_Emp_No");
                    z.empValue = rs.getString("Solstice_Emp_No");
                    z.empName = (rs.getString("Emp_LastName") != null ? rs.getString("Emp_LastName") : "") +
                                (rs.getString("Emp_FirstName") != null ? " " + rs.getString("Emp_FirstName") : "");
                    z.periodId = rs.getInt("P_Period_ID");
                    z.periodName = rs.getString("Period_Name");
                    z.periodNo = rs.getInt("PeriodNo");
                    z.timeSheetId = rs.getInt("P_Time_Sheet_ID");
                    z.paymentId = rs.getInt("P_Payment_ID");

                    BigDecimal amt = rs.getBigDecimal("Reimbursable_Total");
                    if (amt == null) amt = rs.getBigDecimal("Amount_To_Be_Reimbursed");
                    if (amt == null) amt = rs.getBigDecimal("Total");
                    z.amount = amt != null ? amt : BigDecimal.ZERO;

                    z.status = rs.getString("Zoho_Status");
                    z.approvedDate = rs.getTimestamp("Approved_Date");
                    z.submittedDate = rs.getTimestamp("Submitted_Date");
                    z.reimbursementDate = rs.getTimestamp("Reimbursement_Date");
                    z.isReimbursable = "Y".equalsIgnoreCase(rs.getString("Is_Reimbursable"));

                    zohoRows.add(z);
                    if (z.employeeId > 0)
                        employeesInQuery.add(z.employeeId);
                }
            }

            // 2. Charger toutes les feuilles de temps de comptes de d\u00e9penses (ExpenseAccount)
            String sqlTS = "SELECT ts.P_Time_Sheet_ID, ts.P_Employee_ID, ts.P_Period_ID, ts.TimesheetStatus, ts.P_Payment_ID, " +
                " pay.PayDate, pay.TimeSheetStatus DocStatus, pay.NetPay, gains.Total_Gain_Amt " +
                "FROM P_Time_Sheet ts " +
                "LEFT JOIN P_Payment pay ON pay.P_Payment_ID = ts.P_Payment_ID " +
                "LEFT JOIN ( " +
                "    SELECT pg.P_Payment_ID, SUM(pg.AmountCalc) AS Total_Gain_Amt " +
                "    FROM P_Payment_Gain pg " +
                "    INNER JOIN P_Gain g ON g.P_Gain_ID = pg.P_Gain_ID AND g.Value IN ('GL02', 'GL14', 'GL15', 'GL16', 'GL16NB', 'GL16NS') " +
                "    GROUP BY pg.P_Payment_ID " +
                ") gains ON gains.P_Payment_ID = ts.P_Payment_ID " +
                "WHERE ts.SheetType = 'ExpenseAccount' " +
                "  AND ts.AD_Client_ID = " + Env.getAD_Client_ID(m_ctx) + " " +
                "  AND ts.P_Period_ID IN (" + periodInClause + ")";

            Map<Integer, List<TimeSheetPayInfo>> empTsMap = new HashMap<Integer, List<TimeSheetPayInfo>>();
            Map<Integer, TimeSheetPayInfo> tsByIdMap = new HashMap<Integer, TimeSheetPayInfo>();

            try (PreparedStatement pstmt = DB.prepareStatement(sqlTS, null);
                 ResultSet rs = pstmt.executeQuery())
            {
                while (rs.next())
                {
                    TimeSheetPayInfo info = new TimeSheetPayInfo();
                    info.timeSheetId = rs.getInt("P_Time_Sheet_ID");
                    info.employeeId = rs.getInt("P_Employee_ID");
                    info.periodId = rs.getInt("P_Period_ID");
                    info.status = rs.getString("TimesheetStatus");
                    info.paymentId = rs.getInt("P_Payment_ID");
                    info.payDate = rs.getTimestamp("PayDate");
                    info.docStatus = rs.getString("DocStatus");
                    info.netPay = rs.getBigDecimal("NetPay");
                    BigDecimal gainAmt = rs.getBigDecimal("Total_Gain_Amt");
                    info.expenseGainAmount = gainAmt != null ? gainAmt : BigDecimal.ZERO;

                    tsByIdMap.put(info.timeSheetId, info);

                    List<TimeSheetPayInfo> list = empTsMap.get(info.employeeId);
                    if (list == null)
                    {
                        list = new ArrayList<TimeSheetPayInfo>();
                        empTsMap.put(info.employeeId, list);
                    }
                    list.add(info);
                }
            }

            // 3. Charger tous les paiements avec gains de d\u00e9penses (GL02/GL14/GL15/GL16) m\u00eame sans feuille
            String sqlAllGains = "SELECT p.P_Payment_ID, p.P_Employee_ID, p.P_Period_ID, p.PayDate, p.TimeSheetStatus DocStatus, " +
                " p.NetPay, SUM(pg.AmountCalc) AS Expense_Gains_Amt " +
                "FROM P_Payment p " +
                "INNER JOIN P_Payment_Gain pg ON pg.P_Payment_ID = p.P_Payment_ID " +
                "INNER JOIN P_Gain g ON g.P_Gain_ID = pg.P_Gain_ID AND g.Value IN ('GL02', 'GL14', 'GL15', 'GL16', 'GL16NB', 'GL16NS') " +
                "WHERE p.AD_Client_ID = " + Env.getAD_Client_ID(m_ctx) + " " +
                "  AND p.P_Period_ID IN (" + periodInClause + ") " +
//                "  AND p.TimeSheetStatus NOT IN ('VO', 'RE') " +
                "GROUP BY p.P_Payment_ID, p.P_Employee_ID, p.P_Period_ID, p.PayDate, p.TimeSheetStatus, p.NetPay";

            Map<Integer, List<PaymentGainSummary>> empPaymentsMap = new HashMap<Integer, List<PaymentGainSummary>>();
            try (PreparedStatement pstmt = DB.prepareStatement(sqlAllGains, null);
                 ResultSet rs = pstmt.executeQuery())
            {
                while (rs.next())
                {
                    PaymentGainSummary p = new PaymentGainSummary();
                    p.paymentId = rs.getInt("P_Payment_ID");
                    p.employeeId = rs.getInt("P_Employee_ID");
                    p.periodId = rs.getInt("P_Period_ID");
                    p.payDate = rs.getTimestamp("PayDate");
                    p.docStatus = rs.getString("DocStatus");
                    p.netPay = rs.getBigDecimal("NetPay");
                    p.expenseAmount = rs.getBigDecimal("Expense_Gains_Amt");

                    List<PaymentGainSummary> list = empPaymentsMap.get(p.employeeId);
                    if (list == null)
                    {
                        list = new ArrayList<PaymentGainSummary>();
                        empPaymentsMap.put(p.employeeId, list);
                    }
                    list.add(p);
                }
            }

            // 4. R\u00e9conciliation et D\u00e9tection des Anomalies
            Set<Integer> handledPaymentIds = new HashSet<Integer>();

            // Regrouper par employ\u00e9 pour d\u00e9tecter les doublons de rapports
            Map<Integer, List<ZohoRowRaw>> empZohoMap = new HashMap<Integer, List<ZohoRowRaw>>();
            for (ZohoRowRaw z : zohoRows)
            {
                List<ZohoRowRaw> list = empZohoMap.get(z.employeeId);
                if (list == null)
                {
                    list = new ArrayList<ZohoRowRaw>();
                    empZohoMap.put(z.employeeId, list);
                }
                list.add(z);
            }

            for (ZohoRowRaw z : zohoRows)
            {
                AuditRow row = new AuditRow();
                row.expenseReportId = z.expenseReportId;
                row.reportId = z.reportId;
                row.reportNumber = z.reportNumber;
                row.reportName = z.reportName;
                row.description = z.description;
                row.employeeId = z.employeeId;
                row.employeeValue = z.empValue != null ? z.empValue : z.zohoEmpNo;
                row.employeeName = z.empName;
                row.periodId = z.periodId;
                row.periodName = z.periodName;
                row.zohoAmount = z.amount;
                row.zohoStatus = z.status;
                row.zohoApprovedDate = z.approvedDate;
                row.timeSheetId = z.timeSheetId;

                // V\u00e9rification 1 : Matricule employ\u00e9 inconnu
                if (z.employeeId <= 0)
                {
                    row.auditStatus = STATUS_UNREIMBURSED;
                    row.statusDisplay = "\uD83D\uDFE0 NON REMBOURS\u00c9 (Matricule inconnu)";
                    row.paidAmount = BigDecimal.ZERO;
                    row.difference = z.amount.negate();
                    row.diagnostic = "Matricule Zoho '" + z.zohoEmpNo + "' non trouv\u00e9 dans Solstice (P_Employee). Aucun paiement possible.";
                    m_rows.add(row);
                    continue;
                }

                // V\u00e9rification 2 : Recherche des feuilles de temps et paiements
                List<TimeSheetPayInfo> tsList = empTsMap.get(z.employeeId);
                List<PaymentGainSummary> payList = empPaymentsMap.get(z.employeeId);

                TimeSheetPayInfo directTs = z.timeSheetId > 0 ? tsByIdMap.get(z.timeSheetId) : null;

                // Filtrer les paiements valides pour cet employ\u00e9 dans cette p\u00e9riode
                List<PaymentGainSummary> periodPayments = new ArrayList<PaymentGainSummary>();
                if (payList != null)
                {
                    for (PaymentGainSummary p : payList)
                    {
                        if (p.periodId == z.periodId)
                        {
                            periodPayments.add(p);
                        }
                    }
                }

                // V\u00e9rifier combien de rapports Zoho existent pour cet employ\u00e9 dans cette p\u00e9riode
                List<ZohoRowRaw> empPeriodZoho = new ArrayList<ZohoRowRaw>();
                List<ZohoRowRaw> allEmpZoho = empZohoMap.get(z.employeeId);
                if (allEmpZoho != null)
                {
                    for (ZohoRowRaw ez : allEmpZoho)
                    {
                        if (ez.periodId == z.periodId)
                            empPeriodZoho.add(ez);
                    }
                }

                // CAS A : DOUBLON D\u00c9TECT\u00c9 (Plus de paiements que de rapports, ou paiements multiples)
                if (periodPayments.size() > empPeriodZoho.size() && periodPayments.size() > 1)
                {
                    row.auditStatus = STATUS_DUPLICATE;
                    row.statusDisplay = "\uD83D\uDD34 DOUBLON (" + periodPayments.size() + " PAIEMENTS)";

                    BigDecimal totalPaid = BigDecimal.ZERO;
                    StringBuilder payDetails = new StringBuilder();
                    for (int pIdx = 0; pIdx < periodPayments.size(); pIdx++)
                    {
                        PaymentGainSummary p = periodPayments.get(pIdx);
                        totalPaid = totalPaid.add(p.expenseAmount);
                        if (pIdx > 0) payDetails.append(", ");
                        payDetails.append("Paiement #").append(p.paymentId)
                            .append(" (").append(DATE_FMT.format(p.payDate)).append(" : ")
                            .append(CURRENCY_FMT.format(p.expenseAmount)).append(")");
                        handledPaymentIds.add(p.paymentId);
                    }

                    row.paymentCount = periodPayments.size();
                    row.multiplePaymentsDetail = payDetails.toString();
                    row.paidAmount = totalPaid;
                    row.difference = totalPaid.subtract(z.amount);

                    PaymentGainSummary firstP = periodPayments.get(0);
                    row.paymentId = firstP.paymentId;
                    row.payDate = firstP.payDate;
                    row.payDocStatus = firstP.docStatus;

                    if (directTs != null)
                    {
                        row.timeSheetStatus = directTs.status;
                    }

                    row.diagnostic = "\uD83D\uDD34 ANOMALIE : " + periodPayments.size() + " paiements de d\u00e9penses \u00e9mis pour un seul rapport ! Trop-pay\u00e9 : " + CURRENCY_FMT.format(row.difference);
                    m_rows.add(row);
                    continue;
                }

                // CAS B : UN SEUL PAIEMENT OU FEUILLE LI\u00c9E DIRECTEMENT
                if (directTs != null && directTs.paymentId > 0)
                {
                    row.timeSheetStatus = directTs.status;
                    row.paymentId = directTs.paymentId;
                    row.payDate = directTs.payDate;
                    row.payDocStatus = directTs.docStatus;
                    row.paidAmount = directTs.expenseGainAmount;
                    row.paymentCount = 1;
                    handledPaymentIds.add(directTs.paymentId);

                    BigDecimal diff = row.paidAmount.subtract(z.amount);
                    row.difference = diff;

                    if (diff.abs().compareTo(new BigDecimal("0.01")) <= 0)
                    {
                        row.auditStatus = STATUS_MATCHED;
                        row.statusDisplay = "\uD83D\uDFE2 CONFORME";
                        row.diagnostic = "Remboursement exact valid\u00e9 en paie (Feuille #" + directTs.timeSheetId + ", Paiement #" + directTs.paymentId + ").";
                    }
                    else
                    {
                        row.auditStatus = STATUS_AMOUNT_DIFF;
                        row.statusDisplay = "\uD83D\uDFE1 \u00c9CART DE MONTANT";
                        row.diagnostic = "\u00c9cart de " + CURRENCY_FMT.format(diff) + " (Zoho : " + CURRENCY_FMT.format(z.amount) + " vs Paie : " + CURRENCY_FMT.format(row.paidAmount) + ").";
                    }
                    m_rows.add(row);
                    continue;
                }

                // Si pas li\u00e9 par P_Time_Sheet_ID mais un paiement existe dans la p\u00e9riode
                if (!periodPayments.isEmpty())
                {
                    PaymentGainSummary p = periodPayments.get(0);
                    row.paymentId = p.paymentId;
                    row.payDate = p.payDate;
                    row.payDocStatus = p.docStatus;
                    row.paidAmount = p.expenseAmount;
                    row.paymentCount = 1;
                    handledPaymentIds.add(p.paymentId);

                    if (directTs != null)
                        row.timeSheetStatus = directTs.status;

                    BigDecimal diff = row.paidAmount.subtract(z.amount);
                    row.difference = diff;

                    if (diff.abs().compareTo(new BigDecimal("0.01")) <= 0)
                    {
                        row.auditStatus = STATUS_MATCHED;
                        row.statusDisplay = "\uD83D\uDFE2 CONFORME (Par p\u00e9riode)";
                        row.diagnostic = "Remboursement valid\u00e9 sur Paiement #" + p.paymentId + " (lien r\u00e9concili\u00e9 par employ\u00e9/p\u00e9riode).";
                    }
                    else
                    {
                        row.auditStatus = STATUS_AMOUNT_DIFF;
                        row.statusDisplay = "\uD83D\uDFE1 \u00c9CART DE MONTANT";
                        row.diagnostic = "\u00c9cart de " + CURRENCY_FMT.format(diff) + " entre Zoho et la paie.";
                    }
                    m_rows.add(row);
                    continue;
                }

                // CAS C : NON REMBOURS\u00c9 (Pas de paiement trouv\u00e9)
                row.auditStatus = STATUS_UNREIMBURSED;
                row.paidAmount = BigDecimal.ZERO;
                row.difference = z.amount.negate();
                row.paymentCount = 0;

                if (directTs != null)
                {
                    row.timeSheetStatus = directTs.status;
                    if ("E".equalsIgnoreCase(directTs.status))
                    {
                        row.statusDisplay = "\uD83D\uDFE0 NON REMBOURS\u00c9 (Erreur Feuille)";
                        row.diagnostic = "Feuille de temps #" + directTs.timeSheetId + " en statut Erreur ('E'). Le paiement n'a pas \u00e9t\u00e9 g\u00e9n\u00e9r\u00e9.";
                    }
                    else if ("I".equalsIgnoreCase(directTs.status))
                    {
                        row.statusDisplay = "\uD83D\uDFE0 NON REMBOURS\u00c9 (Feuille non valid\u00e9e)";
                        row.diagnostic = "Feuille de temps #" + directTs.timeSheetId + " en statut Initial ('I'). La validation de paie n'a pas \u00e9t\u00e9 compl\u00e9t\u00e9e.";
                    }
                    else
                    {
                        row.statusDisplay = "\uD83D\uDFE0 NON REMBOURS\u00c9";
                        row.diagnostic = "Feuille de temps g\u00e9n\u00e9r\u00e9e mais aucun paiement associ\u00e9 n'a \u00e9t\u00e9 trouv\u00e9.";
                    }
                }
                else
                {
                    row.statusDisplay = "\uD83D\uDFE0 NON REMBOURS\u00c9 (Sans feuille)";
                    row.diagnostic = "Aucune feuille de temps cr\u00e9\u00e9e par TimeGeneration pour ce rapport. Traitement omis.";
                }
                m_rows.add(row);
            }

            // 5. D\u00e9tecter les paiements orphelins (Paiements de d\u00e9penses sans rapport Zoho dans la p\u00e9riode)
            for (Map.Entry<Integer, List<PaymentGainSummary>> entry : empPaymentsMap.entrySet())
            {
                int empId = entry.getKey();
                for (PaymentGainSummary p : entry.getValue())
                {
                    if (!handledPaymentIds.contains(p.paymentId))
                    {
                        AuditRow orphanRow = new AuditRow();
                        orphanRow.auditStatus = STATUS_NO_ZOHO;
                        orphanRow.statusDisplay = "\uD83D\uDFEA PAIEMENT SANS ZOHO";
                        orphanRow.employeeId = empId;
                        orphanRow.periodId = p.periodId;
                        orphanRow.paymentId = p.paymentId;
                        orphanRow.payDate = p.payDate;
                        orphanRow.payDocStatus = p.docStatus;
                        orphanRow.paidAmount = p.expenseAmount;
                        orphanRow.zohoAmount = BigDecimal.ZERO;
                        orphanRow.difference = p.expenseAmount;
                        orphanRow.paymentCount = 1;
                        orphanRow.diagnostic = "Paiement de compte de d\u00e9penses (" + CURRENCY_FMT.format(p.expenseAmount) + ") effectu\u00e9 en paie sans rapport Zoho correspondant.";

                        // R\u00e9cup\u00e9rer le nom de l'employ\u00e9
                        solstice.model.P_Employee emp = solstice.model.P_Employee.get(m_ctx, empId, null);
                        if (emp != null)
                        {
                            orphanRow.employeeValue = emp.getValue();
                            orphanRow.employeeName = (emp.getName() != null ? emp.getName() : "") +
                                                     (emp.getFirstName() != null ? " " + emp.getFirstName() : "");
                        }
                        PeriodInfo pi = findPeriod(p.periodId);
                        if (pi != null)
                            orphanRow.periodName = pi.name;

                        m_rows.add(orphanRow);
                    }
                }
            }
        }
        catch (Exception e)
        {
            s_log.log(Level.SEVERE, "executeQuery", e);
            JOptionPane.showMessageDialog(this, "Erreur lors de l'audit :\n" + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        finally
        {
            setCursor(Cursor.getDefaultCursor());
        }

        masterModel.fireTableDataChanged();
        applyFilters();
        updateKpis();
    }

    /**
     * Mise \u00e0 jour des indicateurs KPIs
     */
    private void updateKpis()
    {
        BigDecimal totalZoho = BigDecimal.ZERO;
        int countZoho = 0;

        BigDecimal totalPay = BigDecimal.ZERO;
        int countPay = 0;

        BigDecimal totalDup = BigDecimal.ZERO;
        int countDup = 0;

        BigDecimal totalUnreimb = BigDecimal.ZERO;
        int countUnreimb = 0;

        BigDecimal totalDiff = BigDecimal.ZERO;
        int countDiff = 0;

        BigDecimal totalMatched = BigDecimal.ZERO;
        int countMatched = 0;

        for (AuditRow r : m_rows)
        {
            if (r.zohoAmount != null && r.zohoAmount.compareTo(BigDecimal.ZERO) > 0)
            {
                totalZoho = totalZoho.add(r.zohoAmount);
                countZoho++;
            }
            if (r.paidAmount != null && r.paidAmount.compareTo(BigDecimal.ZERO) > 0)
            {
                totalPay = totalPay.add(r.paidAmount);
                countPay++;
            }

            if (STATUS_DUPLICATE.equals(r.auditStatus))
            {
                totalDup = totalDup.add(r.difference); // Montant trop pay\u00e9
                countDup++;
            }
            else if (STATUS_UNREIMBURSED.equals(r.auditStatus))
            {
                totalUnreimb = totalUnreimb.add(r.zohoAmount);
                countUnreimb++;
            }
            else if (STATUS_AMOUNT_DIFF.equals(r.auditStatus))
            {
                totalDiff = totalDiff.add(r.difference.abs());
                countDiff++;
            }
            else if (STATUS_MATCHED.equals(r.auditStatus))
            {
                totalMatched = totalMatched.add(r.paidAmount);
                countMatched++;
            }
        }

        lblKpiZohoTotal.setText(CURRENCY_FMT.format(totalZoho));
        lblKpiZohoCount.setText(countZoho + " rapport(s)");

        lblKpiPayTotal.setText(CURRENCY_FMT.format(totalPay));
        lblKpiPayCount.setText(countPay + " versement(s)");

        lblKpiDupTotal.setText(CURRENCY_FMT.format(totalDup));
        lblKpiDupCount.setText(countDup + " doublon(s)");

        lblKpiUnreimbTotal.setText(CURRENCY_FMT.format(totalUnreimb));
        lblKpiUnreimbCount.setText(countUnreimb + " en attente");

        lblKpiDiffTotal.setText(CURRENCY_FMT.format(totalDiff));
        lblKpiDiffCount.setText(countDiff + " \u00e9cart(s)");

        lblKpiMatchedTotal.setText(CURRENCY_FMT.format(totalMatched));
        lblKpiMatchedCount.setText(countMatched + " conforme(s)");
    }

    /**
     * Application dynamique des filtres (Combo anomalie + Recherche textuelle)
     */
    private void applyFilters()
    {
        final String selectedAnomaly = (String) comboAnomaly.getSelectedItem();
        final String search = txtSearch != null ? txtSearch.getText().trim().toLowerCase() : "";

        RowFilter<AuditMasterTableModel, Integer> filter = new RowFilter<AuditMasterTableModel, Integer>()
        {
            @Override
            public boolean include(Entry<? extends AuditMasterTableModel, ? extends Integer> entry)
            {
                AuditMasterTableModel model = entry.getModel();
                int idx = entry.getIdentifier();
                if (idx >= m_rows.size())
                    return false;

                AuditRow row = m_rows.get(idx);

                // Filtre anomalie
                if (selectedAnomaly != null)
                {
                    if (selectedAnomaly.startsWith("\u26A0\uFE0F")) // Toutes anomalies
                    {
                        if (STATUS_MATCHED.equals(row.auditStatus))
                            return false;
                    }
                    else if (selectedAnomaly.startsWith("\uD83D\uDD34")) // Doublons
                    {
                        if (!STATUS_DUPLICATE.equals(row.auditStatus))
                            return false;
                    }
                    else if (selectedAnomaly.startsWith("\uD83D\uDFE0")) // Non rembours\u00e9s
                    {
                        if (!STATUS_UNREIMBURSED.equals(row.auditStatus))
                            return false;
                    }
                    else if (selectedAnomaly.startsWith("\uD83D\uDFE1")) // \u00c9carts
                    {
                        if (!STATUS_AMOUNT_DIFF.equals(row.auditStatus))
                            return false;
                    }
                    else if (selectedAnomaly.startsWith("\uD83D\uDFE2")) // Conformes
                    {
                        if (!STATUS_MATCHED.equals(row.auditStatus))
                            return false;
                    }
                    else if (selectedAnomaly.startsWith("\uD83D\uDFEA")) // Orphelins
                    {
                        if (!STATUS_NO_ZOHO.equals(row.auditStatus))
                            return false;
                    }
                }

                // Filtre texte
                if (!search.isEmpty())
                {
                    boolean match = (row.employeeValue != null && row.employeeValue.toLowerCase().contains(search))
                        || (row.employeeName != null && row.employeeName.toLowerCase().contains(search))
                        || (row.reportNumber != null && row.reportNumber.toLowerCase().contains(search))
                        || (row.reportName != null && row.reportName.toLowerCase().contains(search))
                        || (row.diagnostic != null && row.diagnostic.toLowerCase().contains(search));
                    if (!match)
                        return false;
                }

                return true;
            }
        };

        masterSorter.setRowFilter(filter);
    }

    @Override
    public void valueChanged(ListSelectionEvent e)
    {
        if (e.getValueIsAdjusting())
            return;

        int viewRow = tblMaster.getSelectedRow();
        if (viewRow >= 0)
        {
            int modelRow = tblMaster.convertRowIndexToModel(viewRow);
            if (modelRow >= 0 && modelRow < m_rows.size())
            {
                m_selectedRow = m_rows.get(modelRow);
                loadDetailPanels(m_selectedRow);
                return;
            }
        }
        m_selectedRow = null;
        clearDetailPanels();
    }

    private void loadDetailPanels(AuditRow row)
    {
        if (row == null)
        {
            clearDetailPanels();
            return;
        }

        // 1. Charger le d\u00e9tail des lignes Zoho (P_Employee_Expensereports_Detail)
        m_currentZohoDetails.clear();
        if (row.expenseReportId > 0 || (row.reportId != null && !row.reportId.isEmpty()))
        {
            String repIdEscaped = row.reportId != null ? DB.TO_STRING(row.reportId) : "''";
            String sql = "SELECT Category_Name, Amount, tps_amt, tvq_amt, tvh_amt, Item_Date, Description, Gl_Code " +
                "FROM P_Employee_Expensereports_Detail " +
                "WHERE (P_Employee_Expensereports_ID = " + row.expenseReportId + " " +
                "       OR (report_id IS NOT NULL AND report_id = " + repIdEscaped + ")) " +
                "ORDER BY Item_Date ASC, P_Employee_Expensereports_Detail_ID ASC";

            try (PreparedStatement pstmt = DB.prepareStatement(sql, null);
                 ResultSet rs = pstmt.executeQuery())
            {
                while (rs.next())
                {
                    ZohoItemDetail d = new ZohoItemDetail();
                    d.category = rs.getString("Category_Name");
                    d.amount = rs.getBigDecimal("Amount");
                    d.tps = rs.getBigDecimal("tps_amt");
                    d.tvq = rs.getBigDecimal("tvq_amt");
                    d.tvh = rs.getBigDecimal("tvh_amt");
                    d.itemDate = rs.getTimestamp("Item_Date");
                    d.description = rs.getString("Description");
                    d.glCode = rs.getString("Gl_Code");
                    m_currentZohoDetails.add(d);
                }
            }
            catch (Exception e)
            {
                s_log.log(Level.SEVERE, "loadZohoDetail", e);
            }

            // Si les d\u00e9tails ne sont pas encore t\u00e9l\u00e9charg\u00e9s, les r\u00e9cup\u00e9rer \u00e0 la vol\u00e9e depuis Zoho
            if (m_currentZohoDetails.isEmpty() && row.reportId != null && !row.reportId.isEmpty())
            {
                fetchSingleReportDetailFromZoho(row.expenseReportId, row.reportId);
                try (PreparedStatement pstmt = DB.prepareStatement(sql, null);
                     ResultSet rs = pstmt.executeQuery())
                {
                    while (rs.next())
                    {
                        ZohoItemDetail d = new ZohoItemDetail();
                        d.category = rs.getString("Category_Name");
                        d.amount = rs.getBigDecimal("Amount");
                        d.tps = rs.getBigDecimal("tps_amt");
                        d.tvq = rs.getBigDecimal("tvq_amt");
                        d.tvh = rs.getBigDecimal("tvh_amt");
                        d.itemDate = rs.getTimestamp("Item_Date");
                        d.description = rs.getString("Description");
                        d.glCode = rs.getString("Gl_Code");
                        m_currentZohoDetails.add(d);
                    }
                }
                catch (Exception e)
                {
                    s_log.log(Level.SEVERE, "loadZohoDetail reload", e);
                }
            }
        }
        zohoDetailModel.fireTableDataChanged();

        // 2. Charger les gains de paie (GL02/GL14/GL15/GL16)
        m_currentPayrollDetails.clear();
        if (row.paymentId > 0)
        {
            String sql = "SELECT g.Value AS Gain_Code, g.Name AS Gain_Name, pg.AmountCalc, pg.Hourly_Rate, pg.DayQty " +
                "FROM P_Payment_Gain pg " +
                "INNER JOIN P_Gain g ON g.P_Gain_ID = pg.P_Gain_ID " +
                "WHERE pg.P_Payment_ID = " + row.paymentId + " " +
                "ORDER BY g.Value ASC";

            try (PreparedStatement pstmt = DB.prepareStatement(sql, null);
                 ResultSet rs = pstmt.executeQuery())
            {
                while (rs.next())
                {
                    PayrollGainDetail gd = new PayrollGainDetail();
                    gd.gainCode = rs.getString("Gain_Code");
                    gd.gainName = rs.getString("Gain_Name");
                    gd.amount = rs.getBigDecimal("AmountCalc");
                    gd.rate = rs.getBigDecimal("Hourly_Rate");
                    gd.qty = rs.getBigDecimal("DayQty");
                    m_currentPayrollDetails.add(gd);
                }
            }
            catch (Exception e)
            {
                s_log.log(Level.SEVERE, "loadPayrollDetail", e);
            }
        }
        payrollDetailModel.fireTableDataChanged();

        // 3. Charger l'historique complet des d\u00e9penses pour cet employ\u00e9
        m_currentEmpHistory.clear();
        if (row.employeeId > 0)
        {
            String sql = "SELECT p.P_Payment_ID, per.Name AS Period_Name, p.PayDate, p.TimeSheetStatus DocStatus, " +
                " ts.P_Time_Sheet_ID, ts.TimesheetStatus, gains.Total_Gain_Amt " +
                "FROM P_Payment p " +
                "INNER JOIN P_Period per ON per.P_Period_ID = p.P_Period_ID " +
                "LEFT JOIN P_Time_Sheet ts ON ts.P_Payment_ID = p.P_Payment_ID " +
                "LEFT JOIN ( " +
                "    SELECT pg.P_Payment_ID, SUM(pg.AmountCalc) AS Total_Gain_Amt " +
                "    FROM P_Payment_Gain pg " +
                "    INNER JOIN P_Gain g ON g.P_Gain_ID = pg.P_Gain_ID AND g.Value IN ('GL02', 'GL14', 'GL15', 'GL16', 'GL16NB', 'GL16NS') " +
                "    GROUP BY pg.P_Payment_ID " +
                ") gains ON gains.P_Payment_ID = p.P_Payment_ID " +
                "WHERE p.P_Employee_ID = " + row.employeeId + " " +
                "  AND (ts.SheetType = 'ExpenseAccount' OR gains.Total_Gain_Amt > 0) " +
                "ORDER BY p.PayDate DESC";

            try (PreparedStatement pstmt = DB.prepareStatement(sql, null);
                 ResultSet rs = pstmt.executeQuery())
            {
                while (rs.next())
                {
                    EmpHistoryItem h = new EmpHistoryItem();
                    h.paymentId = rs.getInt("P_Payment_ID");
                    h.periodName = rs.getString("Period_Name");
                    h.payDate = rs.getTimestamp("PayDate");
                    h.docStatus = rs.getString("DocStatus");
                    h.timeSheetId = rs.getInt("P_Time_Sheet_ID");
                    h.timeSheetStatus = rs.getString("TimesheetStatus");
                    h.amount = rs.getBigDecimal("Total_Gain_Amt");
                    m_currentEmpHistory.add(h);
                }
            }
            catch (Exception e)
            {
                s_log.log(Level.SEVERE, "loadEmpHistory", e);
            }
        }
        empHistoryModel.fireTableDataChanged();
    }

    private void clearDetailPanels()
    {
        m_currentZohoDetails.clear();
        zohoDetailModel.fireTableDataChanged();
        m_currentPayrollDetails.clear();
        payrollDetailModel.fireTableDataChanged();
        m_currentEmpHistory.clear();
        empHistoryModel.fireTableDataChanged();
    }

    private void updateGuideContent()
    {
        StringBuilder html = new StringBuilder();
        html.append("<html><body style='font-family:Segoe UI, sans-serif; font-size:11px; padding:10px; color:#223344;'>");
        html.append("<h3 style='color:#1E3A8A; margin-top:0;'>Guide d'Audit & Proc\u00e9dure de R\u00e9gularisation Zoho Expense</h3>");

        html.append("<p><b>1. \uD83D\uDD34 DOUBLONS DE REMBOURSEMENT (Employ\u00e9s rembours\u00e9s 2 fois) :</b><br/>");
        html.append("<i>Cause technique :</i> Lorsqu'un compte de d\u00e9penses est import\u00e9 et pay\u00e9 en p\u00e9riode N, mais que Zoho Expense n'a pas \u00e9t\u00e9 marqu\u00e9 \u00e0 temps comme 'Reimbursed' (ou si la synchronisation a \u00e9chou\u00e9), ");
        html.append("<code>ImportExportZohoV2</code> re-t\u00e9l\u00e9charge le rapport en p\u00e9riode N+1, \u00e9crase la ligne dans <code>P_Employee_Expensereports</code> avec la nouvelle p\u00e9riode et remet <code>P_Time_Sheet_ID</code> \u00e0 z\u00e9ro. ");
        html.append("<code>TimeGeneration</code> cr\u00e9e alors une <b>deuxi\u00e8me feuille de temps</b> et la paie \u00e9met un <b>deuxi\u00e8me paiement</b>.<br/>");
        html.append("<b>Action corrective :</b> V\u00e9rifiez dans l'onglet 'Historique D\u00e9penses' les num\u00e9ros des deux paiements. Dans Solstice, appliquez une d\u00e9duction ou un ajustement n\u00e9gatif de gain sur la paie suivante pour r\u00e9cup\u00e9rer le trop-pay\u00e9.</p>");

        html.append("<p><b>2. \uD83D\uDFE0 D\u00c9PENSES NON REMBOURS\u00c9ES (En souffrance) :</b><br/>");
        html.append("<i>Causes possibles :</i><br/>");
        html.append("&bull; <b>Matricule inconnu :</b> Le matricule employ\u00e9 renseign\u00e9 dans Zoho ne correspond pas exactement au matricule dans Solstice (z\u00e9ros devant, espace). <code>P_Employee_ID</code> vaut 0, donc <code>TimeGeneration</code> ignore le rapport.<br/>");
        html.append("&bull; <b>Feuille en erreur ('E') :</b> La feuille a \u00e9t\u00e9 g\u00e9n\u00e9r\u00e9e mais bloqu\u00e9e lors de la validation du temps.<br/>");
        html.append("&bull; <b>Fausse confirmation Zoho :</b> <code>ImportExportZohoReimburse</code> a fait <code>SELECT * FROM P_Employee_Expensereports</code> sans WHERE et a notifi\u00e9 Zoho que le rapport \u00e9tait rembours\u00e9 alors que la paie n'avait pas \u00e9t\u00e9 trait\u00e9e.<br/>");
        html.append("<b>Action corrective :</b> Si le matricule \u00e9tait erron\u00e9, corrigez-le. Si la feuille de temps est en statut 'I' ou omise, relancez <code>TimeGeneration</code> pour le type 'ExpenseAccount'.</p>");

        html.append("<p><b>3. \uD83D\uDFE1 \u00c9CARTS DE MONTANT :</b><br/>");
        html.append("V\u00e9rifiez l'onglet 'D\u00e9tail Re\u00e7us & Taxes Zoho'. L'\u00e9cart provient souvent d'une ventilation des taxes TPS (GL14), TVQ (GL15) ou TVH (GL16) mal param\u00e9tr\u00e9e ou d'une d\u00e9pense marqu\u00e9e non-remboursable.</p>");

        html.append("</body></html>");
        guidePane.setText(html.toString());
        guidePane.setCaretPosition(0);
    }

    /**
     * Actions des boutons et s\u00e9lecteurs
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();

        if (src == btnSyncZoho)
        {
            syncFromZoho();
        }
        else if (src == btnRefresh)
        {
            executeQuery();
        }
        else if (src == btnExportExcel)
        {
            exportToExcel();
        }
        else if (src == btnZoomTS)
        {
            zoomToTimeSheet();
        }
        else if (src == btnZoomPay)
        {
            zoomToPayment();
        }
        else if (src == btnZoomEmp)
        {
            zoomToEmployee();
        }
        else if (src == comboPreset)
        {
            handlePresetSelection();
        }
        else if (src == comboAnomaly || src == comboOrg)
        {
            applyFilters();
        }
        else if (src == comboPeriodFrom || src == comboPeriodTo)
        {
            executeQuery();
        }
    }

    private void handlePresetSelection()
    {
        int idx = comboPreset.getSelectedIndex();
        if (idx <= 0 || m_allPeriods.isEmpty())
            return;

        if (idx == 1) // P\u00e9riode ouverte courante
        {
            selectCurrentPeriodPreset();
        }
        else if (idx == 2) // Derni\u00e8re p\u00e9riode ferm\u00e9e
        {
            for (int i = 0; i < m_allPeriods.size(); i++)
            {
                if ("C".equalsIgnoreCase(m_allPeriods.get(i).status) || "Closed".equalsIgnoreCase(m_allPeriods.get(i).status))
                {
                    comboPeriodFrom.setSelectedIndex(i);
                    comboPeriodTo.setSelectedIndex(i);
                    break;
                }
            }
        }
        else if (idx == 3) // 3 derni\u00e8res p\u00e9riodes
        {
            int toIdx = 0;
            int fromIdx = Math.min(2, m_allPeriods.size() - 1);
            comboPeriodTo.setSelectedIndex(toIdx);
            comboPeriodFrom.setSelectedIndex(fromIdx);
        }
        else if (idx == 4) // 6 derni\u00e8res p\u00e9riodes
        {
            int toIdx = 0;
            int fromIdx = Math.min(5, m_allPeriods.size() - 1);
            comboPeriodTo.setSelectedIndex(toIdx);
            comboPeriodFrom.setSelectedIndex(fromIdx);
        }
        else if (idx == 5) // Ann\u00e9e courante
        {
            if (!m_allPeriods.isEmpty())
            {
                String curYear = m_allPeriods.get(0).fiscalYear;
                int firstIdx = -1;
                int lastIdx = -1;
                for (int i = 0; i < m_allPeriods.size(); i++)
                {
                    if (curYear != null && curYear.equals(m_allPeriods.get(i).fiscalYear))
                    {
                        if (firstIdx < 0) firstIdx = i;
                        lastIdx = i;
                    }
                }
                if (firstIdx >= 0 && lastIdx >= 0)
                {
                    comboPeriodTo.setSelectedIndex(firstIdx);
                    comboPeriodFrom.setSelectedIndex(lastIdx);
                }
            }
        }
        else if (idx == 6) // Toutes les p\u00e9riodes
        {
            comboPeriodTo.setSelectedIndex(0);
            comboPeriodFrom.setSelectedIndex(m_allPeriods.size() - 1);
        }
    }

    private void zoomToSelected()
    {
        if (m_selectedRow != null)
        {
            if (m_selectedRow.paymentId > 0)
                zoomToPayment();
            else if (m_selectedRow.timeSheetId > 0)
                zoomToTimeSheet();
            else if (m_selectedRow.employeeId > 0)
                zoomToEmployee();
        }
    }

    private void zoomToTimeSheet()
    {
        if (m_selectedRow != null && m_selectedRow.timeSheetId > 0)
        {
            AEnv.zoom(solstice.model.P_Time_Sheet.Table_ID, m_selectedRow.timeSheetId);
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Aucune feuille de temps associ\u00e9e sur cette ligne.", "Zoom", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void zoomToPayment()
    {
        if (m_selectedRow != null && m_selectedRow.paymentId > 0)
        {
            AEnv.zoom(solstice.model.P_Payment.Table_ID, m_selectedRow.paymentId);
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Aucun paiement associ\u00e9 sur cette ligne.", "Zoom", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void zoomToEmployee()
    {
        if (m_selectedRow != null && m_selectedRow.employeeId > 0)
        {
            AEnv.zoom(solstice.model.P_Employee.Table_ID, m_selectedRow.employeeId);
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Aucun employ\u00e9 associ\u00e9 sur cette ligne.", "Zoom", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Actualiser et synchroniser les rapports et d\u00e9tails depuis Zoho Expense vers la table permanente P_Employee_Expensereports_Validation
     */
    private void syncFromZoho()
    {
        int choice = JOptionPane.showOptionDialog(
            this,
            "Souhaitez-vous actualiser les donn\u00e9es depuis Zoho Expense ?\n\n" +
            "Cette op\u00e9ration interroge l'API Zoho Expense pour synchroniser la table permanente\n" +
            "'P_Employee_Expensereports_Validation' et ses re\u00e7us d\u00e9taill\u00e9s.\n\n" +
            "Choisissez le mode d'actualisation :",
            "Actualisation Zoho Expense",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            new Object[] {
                "Actualiser tout (En-t\u00eates + Re\u00e7us d\u00e9taill\u00e9s)",
                "Actualiser en-t\u00eates seulement (Rapide)",
                "Annuler"
            },
            "Actualiser tout (En-t\u00eates + Re\u00e7us d\u00e9taill\u00e9s)"
        );

        if (choice != 0 && choice != 1)
            return;

        final boolean fetchDetails = (choice == 0);

        final javax.swing.JDialog progressDialog = new javax.swing.JDialog(
            m_frame != null ? m_frame : (JFrame) SwingUtilities.getWindowAncestor(this),
            "Synchronisation Zoho Expense",
            true
        );
        progressDialog.setDefaultCloseOperation(javax.swing.JDialog.DO_NOTHING_ON_CLOSE);
        progressDialog.setSize(480, 160);
        progressDialog.setLocationRelativeTo(this);
        progressDialog.setLayout(new BorderLayout(10, 10));

        JPanel pnlContent = new JPanel(new BorderLayout(5, 8));
        pnlContent.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        pnlContent.setBackground(Color.WHITE);

        final JLabel lblStatus = new JLabel("Connexion \u00e0 l'API Zoho Expense en cours...");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStatus.setForeground(new Color(30, 45, 70));
        pnlContent.add(lblStatus, BorderLayout.NORTH);

        final javax.swing.JProgressBar progressBar = new javax.swing.JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(420, 22));
        pnlContent.add(progressBar, BorderLayout.CENTER);

        final JLabel lblSub = new JLabel("Veuillez patienter pendant la r\u00e9cup\u00e9ration des donn\u00e9es...");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSub.setForeground(new Color(100, 115, 130));
        pnlContent.add(lblSub, BorderLayout.SOUTH);

        progressDialog.add(pnlContent, BorderLayout.CENTER);

        btnSyncZoho.setEnabled(false);
        btnRefresh.setEnabled(false);

        javax.swing.SwingWorker<SyncStats, String> worker = new javax.swing.SwingWorker<SyncStats, String>()
        {
            @Override
            protected SyncStats doInBackground() throws Exception
            {
                publish("Authentification OAuth2 aupr\u00e8s de Zoho...");
                String token = getZohoAccessToken();
                if (token == null || token.isEmpty())
                    throw new Exception("Impossible d'obtenir un jeton d'acc\u00e8s Zoho.");

                publish("Chargement des r\u00e9f\u00e9rentiels de p\u00e9riodes et employ\u00e9s...");
                List<PeriodInfo> periods = new ArrayList<PeriodInfo>();
                String sqlP = "SELECT P_Period_ID, StartDate, EndDate, PeriodNo, Name FROM P_Period ORDER BY StartDate ASC";
                try (PreparedStatement ps = DB.prepareStatement(sqlP, null);
                     ResultSet rs = ps.executeQuery())
                {
                    while (rs.next())
                    {
                        PeriodInfo pi = new PeriodInfo();
                        pi.periodId = rs.getInt("P_Period_ID");
                        pi.startDate = rs.getTimestamp("StartDate");
                        pi.endDate = rs.getTimestamp("EndDate");
                        pi.periodNo = rs.getInt("PeriodNo");
                        pi.name = rs.getString("Name");
                        periods.add(pi);
                    }
                }

                Map<String, Integer> empIdMap = new HashMap<String, Integer>();
                Map<String, Integer> empOrgMap = new HashMap<String, Integer>();
                String sqlEmp = "SELECT P_Employee_ID, Value, AD_Org_ID FROM P_Employee WHERE AD_Client_ID = " + Env.getAD_Client_ID(m_ctx);
                try (PreparedStatement ps = DB.prepareStatement(sqlEmp, null);
                     ResultSet rs = ps.executeQuery())
                {
                    while (rs.next())
                    {
                        int id = rs.getInt("P_Employee_ID");
                        int org = rs.getInt("AD_Org_ID");
                        String val = rs.getString("Value");
                        if (val != null)
                        {
                            val = val.trim();
                            empIdMap.put(val, id);
                            empOrgMap.put(val, org);
                            String stripped = val.replaceFirst("^0+(?!$)", "");
                            empIdMap.put(stripped, id);
                            empOrgMap.put(stripped, org);
                        }
                    }
                }

                // Charger feuilles de temps de comptes de d\u00e9penses existantes
                Map<String, Integer> tsMap = new HashMap<String, Integer>();
                Map<Integer, Integer> tsPayMap = new HashMap<Integer, Integer>();
                String sqlTS = "SELECT P_Time_Sheet_ID, P_Employee_ID, P_Period_ID, P_Payment_ID FROM P_Time_Sheet " +
                    "WHERE SheetType = 'ExpenseAccount' AND AD_Client_ID = " + Env.getAD_Client_ID(m_ctx);
                try (PreparedStatement ps = DB.prepareStatement(sqlTS, null);
                     ResultSet rs = ps.executeQuery())
                {
                    while (rs.next())
                    {
                        int tsId = rs.getInt("P_Time_Sheet_ID");
                        int eId = rs.getInt("P_Employee_ID");
                        int pId = rs.getInt("P_Period_ID");
                        int payId = rs.getInt("P_Payment_ID");
                        tsMap.put(eId + "_" + pId, tsId);
                        if (payId > 0)
                            tsPayMap.put(tsId, payId);
                    }
                }

                SyncStats stats = new SyncStats();
                List<JsonObject> allReports = new ArrayList<JsonObject>();
                OkHttpClient client = new OkHttpClient();
                String orgId = "851698770";

                // 1. T\u00e9l\u00e9charger Approved
                publish("T\u00e9l\u00e9chargement des rapports approuv\u00e9s...");
                downloadReportsWithFilter(client, token, orgId, "Type.Approval,Status.Approved", allReports);

                // 2. T\u00e9l\u00e9charger Reimbursed
                publish("T\u00e9l\u00e9chargement des rapports rembours\u00e9s...");
                downloadReportsWithFilter(client, token, orgId, "Type.Approval,Status.Reimbursed", allReports);

                stats.totalProcessed = allReports.size();
                publish("Enregistrement de " + allReports.size() + " rapports dans P_Employee_Expensereports_Validation...");

                // Charger les enregistrements existants dans la table de validation
                Map<String, Long> existingValIds = new HashMap<String, Long>();
                String sqlExist = "SELECT report_id, P_Employee_Expensereports_Validation_ID FROM P_Employee_Expensereports_Validation";
                try (PreparedStatement ps = DB.prepareStatement(sqlExist, null);
                     ResultSet rs = ps.executeQuery())
                {
                    while (rs.next())
                    {
                        String rid = rs.getString("report_id");
                        if (rid != null)
                            existingValIds.put(rid, rs.getLong("P_Employee_Expensereports_Validation_ID"));
                    }
                }

                int clientIdVal = Env.getAD_Client_ID(m_ctx);
                int userIdVal = Env.getAD_User_ID(m_ctx);

                List<RepDetailQueueItem> detailQueue = new ArrayList<RepDetailQueueItem>();

                for (int i = 0; i < allReports.size(); i++)
                {
                    JsonObject jobj = allReports.get(i);
                    String repId = jobj.get("report_id").getAsString();
                    String repNo = jobj.has("report_number") && !jobj.get("report_number").isJsonNull() ? jobj.get("report_number").getAsString() : "";
                    String repName = jobj.has("report_name") && !jobj.get("report_name").isJsonNull() ? jobj.get("report_name").getAsString() : "";
                    String desc = jobj.has("description") && !jobj.get("description").isJsonNull() ? jobj.get("description").getAsString() : "";
                    String empNo = jobj.has("employee_number") && !jobj.get("employee_number").isJsonNull() ? jobj.get("employee_number").getAsString().trim() : "";
                    String status = jobj.has("status") && !jobj.get("status").isJsonNull() ? jobj.get("status").getAsString() : "";

                    BigDecimal reimbTotal = getJsonBigDecimal(jobj, "reimbursable_total");
                    BigDecimal amtToBeReimb = getJsonBigDecimal(jobj, "amount_to_be_reimbursed");
                    if (amtToBeReimb.compareTo(BigDecimal.ZERO) == 0 && reimbTotal.compareTo(BigDecimal.ZERO) > 0)
                        amtToBeReimb = reimbTotal;
                    BigDecimal total = getJsonBigDecimal(jobj, "total");
                    if (total.compareTo(BigDecimal.ZERO) == 0 && reimbTotal.compareTo(BigDecimal.ZERO) > 0)
                        total = reimbTotal;

                    Timestamp approvedDate = parseJsonTimestamp(jobj, "approved_date");
                    Timestamp submittedDate = parseJsonTimestamp(jobj, "submitted_date");
                    Timestamp reimbDate = parseJsonTimestamp(jobj, "reimbursement_date");
                    Timestamp startDate = parseJsonTimestamp(jobj, "start_date");
                    Timestamp endDate = parseJsonTimestamp(jobj, "end_date");

                    // R\u00e9soudre l'employ\u00e9
                    Integer empIdObj = empIdMap.get(empNo);
                    int empId = empIdObj != null ? empIdObj : 0;
                    Integer orgIdObj = empOrgMap.get(empNo);
                    int empOrgId = orgIdObj != null ? orgIdObj : Env.getAD_Org_ID(m_ctx);

                    // R\u00e9soudre la p\u00e9riode
                    Date refDate = approvedDate != null ? approvedDate : (reimbDate != null ? reimbDate : submittedDate);
                    int matchedPeriodId = 0;
                    if (refDate != null)
                    {
                        for (PeriodInfo pi : periods)
                        {
                            if (!refDate.before(pi.startDate) && !refDate.after(pi.endDate))
                            {
                                matchedPeriodId = pi.periodId;
                                break;
                            }
                        }
                    }

                    // R\u00e9soudre la feuille de temps et le paiement
                    Integer tsIdObj = tsMap.get(empId + "_" + matchedPeriodId);
                    int timeSheetId = tsIdObj != null ? tsIdObj : 0;
                    int paymentId = 0;
                    if (timeSheetId > 0)
                    {
                        Integer pIdObj = tsPayMap.get(timeSheetId);
                        paymentId = pIdObj != null ? pIdObj : 0;
                    }

                    Long existingValId = existingValIds.get(repId);
                    long valId = 0;

                    if (existingValId != null && existingValId > 0)
                    {
                        valId = existingValId;
                        String sqlUpd = "UPDATE P_Employee_Expensereports_Validation SET " +
                            " status = ?, reimbursable_total = ?, amount_to_be_reimbursed = ?, total = ?, " +
                            " approved_date = ?, submitted_date = ?, reimbursement_date = ?, start_date = ?, end_date = ?, " +
                            " P_Period_ID = ?, P_Employee_ID = ?, " +
                            " P_Time_Sheet_ID = CASE WHEN ? > 0 THEN ? ELSE P_Time_Sheet_ID END, " +
                            " P_Payment_ID = CASE WHEN ? > 0 THEN ? ELSE P_Payment_ID END, " +
                            " Updated = GETDATE(), UpdatedBy = ? " +
                            "WHERE P_Employee_Expensereports_Validation_ID = ?";
                        try (PreparedStatement ps = DB.prepareStatement(sqlUpd, null))
                        {
                            ps.setString(1, status);
                            ps.setBigDecimal(2, reimbTotal);
                            ps.setBigDecimal(3, amtToBeReimb);
                            ps.setBigDecimal(4, total);
                            ps.setTimestamp(5, approvedDate);
                            ps.setTimestamp(6, submittedDate);
                            ps.setTimestamp(7, reimbDate);
                            ps.setTimestamp(8, startDate);
                            ps.setTimestamp(9, endDate);
                            ps.setInt(10, matchedPeriodId);
                            ps.setInt(11, empId);
                            ps.setInt(12, timeSheetId);
                            ps.setInt(13, timeSheetId);
                            ps.setInt(14, paymentId);
                            ps.setInt(15, paymentId);
                            ps.setInt(16, userIdVal);
                            ps.setLong(17, valId);
                            ps.executeUpdate();
                        }
                        stats.updatedCount++;
                    }
                    else
                    {
                        long nextSeq = getNextSequenceValue("P_Employee_Expensereports_ValidationSEQ");
                        valId = nextSeq;

                        String sqlIns = "INSERT INTO P_Employee_Expensereports_Validation (" +
                            " P_Employee_Expensereports_Validation_ID, P_Employee_Expensereports_ID, AD_Client_ID, AD_Org_ID, " +
                            " IsActive, Created, CreatedBy, Updated, UpdatedBy, report_id, report_name, description, report_number, " +
                            " status, reimbursable_total, amount_to_be_reimbursed, total, employee_number, P_Employee_ID, " +
                            " ExpensereportsType, P_Period_ID, P_Time_Sheet_ID, P_Payment_ID, approved_date, submitted_date, " +
                            " reimbursement_date, start_date, end_date " +
                            ") VALUES (?, ?, ?, ?, 'Y', GETDATE(), ?, GETDATE(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'ZohoExpense', ?, ?, ?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement ps = DB.prepareStatement(sqlIns, null))
                        {
                            ps.setLong(1, valId);
                            ps.setLong(2, valId);
                            ps.setInt(3, clientIdVal);
                            ps.setInt(4, empOrgId);
                            ps.setInt(5, userIdVal);
                            ps.setInt(6, userIdVal);
                            ps.setString(7, repId);
                            ps.setString(8, repName);
                            ps.setString(9, desc);
                            ps.setString(10, repNo);
                            ps.setString(11, status);
                            ps.setBigDecimal(12, reimbTotal);
                            ps.setBigDecimal(13, amtToBeReimb);
                            ps.setBigDecimal(14, total);
                            ps.setString(15, empNo);
                            ps.setInt(16, empId);
                            ps.setInt(17, matchedPeriodId);
                            ps.setInt(18, timeSheetId);
                            ps.setInt(19, paymentId);
                            ps.setTimestamp(20, approvedDate);
                            ps.setTimestamp(21, submittedDate);
                            ps.setTimestamp(22, reimbDate);
                            ps.setTimestamp(23, startDate);
                            ps.setTimestamp(24, endDate);
                            ps.executeUpdate();
                        }
                        existingValIds.put(repId, valId);
                        stats.insertedCount++;
                    }

                    if (fetchDetails)
                    {
                        detailQueue.add(new RepDetailQueueItem(valId, repId, repNo, empOrgId));
                    }
                }

                // 3. T\u00e9l\u00e9chargement des re\u00e7us d\u00e9taill\u00e9s si demand\u00e9
                if (fetchDetails && !detailQueue.isEmpty())
                {
                    for (int d = 0; d < detailQueue.size(); d++)
                    {
                        RepDetailQueueItem item = detailQueue.get(d);
                        if (d % 10 == 0 || d == detailQueue.size() - 1)
                        {
                            publish("Import des re\u00e7us et calcul des taxes (" + (d + 1) + " / " + detailQueue.size() + ")...");
                        }

                        int imported = syncReportDetails(client, token, orgId, item.valId, item.repId, item.orgId, clientIdVal, userIdVal);
                        stats.detailCount += imported;
                    }
                }

                return stats;
            }

            @Override
            protected void process(List<String> chunks)
            {
                if (!chunks.isEmpty())
                {
                    String latest = chunks.get(chunks.size() - 1);
                    lblStatus.setText(latest);
                }
            }

            @Override
            protected void done()
            {
                progressDialog.dispose();
                btnSyncZoho.setEnabled(true);
                btnRefresh.setEnabled(true);

                try
                {
                    SyncStats res = get();
                    JOptionPane.showMessageDialog(
                        VZohoExpenseValidation.this,
                        "\u2705 Synchronisation Zoho Expense termin\u00e9e avec succ\u00e8s !\n\n" +
                        "\u2022 Rapports trait\u00e9s : " + res.totalProcessed + "\n" +
                        "\u2022 Nouveaux rapports ins\u00e9r\u00e9s : " + res.insertedCount + "\n" +
                        "\u2022 Rapports mis \u00e0 jour : " + res.updatedCount + "\n" +
                        (fetchDetails ? "\u2022 Lignes de d\u00e9tail/taxes import\u00e9es : " + res.detailCount + "\n" : "") +
                        "\nLa table permanente P_Employee_Expensereports_Validation est \u00e0 jour.",
                        "Succ\u00e8s",
                        JOptionPane.INFORMATION_MESSAGE
                    );

                    // Recharger imm\u00e9diatement le rapport d'audit
                    executeQuery();
                }
                catch (Exception ex)
                {
                    s_log.log(Level.SEVERE, "syncFromZoho", ex);
                    JOptionPane.showMessageDialog(
                        VZohoExpenseValidation.this,
                        "Erreur lors de la synchronisation Zoho Expense :\n" + ex.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };

        worker.execute();
        progressDialog.setVisible(true);
    }

    private int syncReportDetails(OkHttpClient client, String token, String orgId, long valId, String repId, int empOrgId, int clientIdVal, int userIdVal)
    {
        try
        {
            Request reqDet = new Request.Builder()
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("X-com-zoho-expense-organizationid", orgId)
                .addHeader("Content-Type", "application/json;")
                .url("https://www.zohoapis.com/expense/v1/expensereports/" + repId + "/approvalhistory")
                .get()
                .build();

            Response respDet = client.newCall(reqDet).execute();
            if (!respDet.isSuccessful())
                return 0;

            JsonObject objDet = JsonParser.parseString(respDet.body().string()).getAsJsonObject();
            JsonObject expRep = objDet.getAsJsonObject("expense_report");
            if (expRep == null) return 0;
            JsonArray expenses = expRep.getAsJsonArray("expenses");
            if (expenses == null || expenses.size() == 0) return 0;

            // Supprimer anciens d\u00e9tails
            String sqlDel = "DELETE FROM P_Employee_Expensereports_Detail WHERE report_id = " + DB.TO_STRING(repId);
            DB.executeUpdate(sqlDel, null);

            BigDecimal repTps = BigDecimal.ZERO;
            BigDecimal repTvq = BigDecimal.ZERO;
            BigDecimal repTvh = BigDecimal.ZERO;
            int count = 0;

            for (int i = 0; i < expenses.size(); i++)
            {
                JsonObject expObj = expenses.get(i).getAsJsonObject();
                JsonArray lineItems = expObj.getAsJsonArray("line_items");
                if (lineItems == null) continue;

                for (int t = 0; t < lineItems.size(); t++)
                {
                    JsonObject item = lineItems.get(t).getAsJsonObject();

                    BigDecimal amount = getJsonBigDecimal(item, "claimed_bcy_total");
                    BigDecimal totalTax = getJsonBigDecimal(item, "tax_amount");
                    String taxName = item.has("tax_name") && !item.get("tax_name").isJsonNull() ? item.get("tax_name").getAsString() : "";

                    BigDecimal tps = BigDecimal.ZERO;
                    BigDecimal tvq = BigDecimal.ZERO;
                    BigDecimal tvh = BigDecimal.ZERO;

                    if (taxName.startsWith("TVH"))
                    {
                        tvh = totalTax;
                    }
                    else if (taxName.startsWith("TPS/TVQ"))
                    {
                        tvq = totalTax.multiply(new BigDecimal("0.66611")).setScale(2, RoundingMode.HALF_UP);
                        tps = totalTax.subtract(tvq);
                    }
                    else if (taxName.startsWith("TVQ"))
                    {
                        tvq = totalTax;
                    }
                    else if (taxName.startsWith("TPS"))
                    {
                        tps = totalTax;
                    }
                    else if (taxName.startsWith("NS HST"))
                    {
                        tvh = totalTax;
                    }

                    repTps = repTps.add(tps);
                    repTvq = repTvq.add(tvq);
                    repTvh = repTvh.add(tvh);

                    long detId = getNextSequenceValue("P_Employee_Expensereports_DetailSEQ");

                    String catName = item.has("category_name") && !item.get("category_name").isJsonNull() ? item.get("category_name").getAsString() : "";
                    String desc = item.has("description") && !item.get("description").isJsonNull() ? item.get("description").getAsString() : "";
                    String glCode = item.has("gl_code") && !item.get("gl_code").isJsonNull() ? item.get("gl_code").getAsString() : "";
                    String lineItemId = item.has("line_item_id") && !item.get("line_item_id").isJsonNull() ? item.get("line_item_id").getAsString() : "";
                    String userName = item.has("user_name") && !item.get("user_name").isJsonNull() ? item.get("user_name").getAsString() : "";
                    Timestamp itemDate = parseJsonTimestamp(item, "item_date");

                    String sqlInsDet = "INSERT INTO P_Employee_Expensereports_Detail (" +
                        " P_Employee_Expensereports_Detail_ID, P_Employee_Expensereports_ID, AD_Client_ID, AD_Org_ID, " +
                        " IsActive, Created, CreatedBy, Updated, UpdatedBy, report_id, Category_Name, Amount, " +
                        " tps_amt, tvq_amt, tvh_amt, tax_amount, Item_Date, Description, Gl_Code, line_item_code, user_name " +
                        ") VALUES (?, ?, ?, ?, 'Y', GETDATE(), ?, GETDATE(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement ps = DB.prepareStatement(sqlInsDet, null))
                    {
                        ps.setLong(1, detId);
                        ps.setLong(2, valId);
                        ps.setInt(3, clientIdVal);
                        ps.setInt(4, empOrgId);
                        ps.setInt(5, userIdVal);
                        ps.setInt(6, userIdVal);
                        ps.setString(7, repId);
                        ps.setString(8, catName);
                        ps.setBigDecimal(9, amount);
                        ps.setBigDecimal(10, tps);
                        ps.setBigDecimal(11, tvq);
                        ps.setBigDecimal(12, tvh);
                        ps.setBigDecimal(13, totalTax);
                        ps.setTimestamp(14, itemDate);
                        ps.setString(15, desc);
                        ps.setString(16, glCode);
                        ps.setString(17, lineItemId);
                        ps.setString(18, userName);
                        ps.executeUpdate();
                    }
                    count++;
                }
            }

            // Mettre \u00e0 jour les taxes dans l'en-t\u00eate
            String sqlHdr = "UPDATE P_Employee_Expensereports_Validation SET tps_amt = ?, tvq_amt = ?, tvh_amt = ? " +
                "WHERE P_Employee_Expensereports_Validation_ID = ?";
            try (PreparedStatement ps = DB.prepareStatement(sqlHdr, null))
            {
                ps.setBigDecimal(1, repTps);
                ps.setBigDecimal(2, repTvq);
                ps.setBigDecimal(3, repTvh);
                ps.setLong(4, valId);
                ps.executeUpdate();
            }

            return count;
        }
        catch (Exception e)
        {
            s_log.log(Level.WARNING, "syncReportDetails error for " + repId, e);
            return 0;
        }
    }

    private void fetchSingleReportDetailFromZoho(long valId, String reportId)
    {
        try
        {
            String token = getZohoAccessToken();
            if (token == null || token.isEmpty())
                return;

            String orgId = "851698770";
            int clientIdVal = Env.getAD_Client_ID(m_ctx);
            int userIdVal = Env.getAD_User_ID(m_ctx);

            int empOrgId = Env.getAD_Org_ID(m_ctx);
            String sqlOrg = "SELECT AD_Org_ID FROM P_Employee_Expensereports_Validation WHERE P_Employee_Expensereports_Validation_ID = " + valId;
            try (PreparedStatement ps = DB.prepareStatement(sqlOrg, null);
                 ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                    empOrgId = rs.getInt(1);
            }

            OkHttpClient client = new OkHttpClient();
            syncReportDetails(client, token, orgId, valId, reportId, empOrgId, clientIdVal, userIdVal);
        }
        catch (Exception e)
        {
            s_log.log(Level.WARNING, "fetchSingleReportDetailFromZoho error for " + reportId, e);
        }
    }

    private void downloadReportsWithFilter(OkHttpClient client, String token, String orgId, String filter, List<JsonObject> targetList) throws Exception
    {
        int page = 1;
        boolean hasMore = true;

        while (hasMore)
        {
            String url = "https://www.zohoapis.com/expense/v1/expensereports?filter_by=" + filter + "&page=" + page + "&per_page=200";
            Request req = new Request.Builder()
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("X-com-zoho-expense-organizationid", orgId)
                .addHeader("Content-Type", "application/json;")
                .url(url)
                .get()
                .build();

            Response resp = client.newCall(req).execute();
            if (!resp.isSuccessful())
                throw new Exception("Erreur appel API Zoho reports (" + filter + "): HTTP " + resp.code());

            JsonObject obj = JsonParser.parseString(resp.body().string()).getAsJsonObject();
            JsonArray arr = obj.getAsJsonArray("expense_reports");
            if (arr != null)
            {
                for (int i = 0; i < arr.size(); i++)
                {
                    targetList.add(arr.get(i).getAsJsonObject());
                }
            }

            hasMore = false;
            if (obj.has("page_context"))
            {
                JsonObject pCtx = obj.getAsJsonObject("page_context");
                if (pCtx.has("has_more_page"))
                {
                    hasMore = pCtx.get("has_more_page").getAsBoolean();
                }
            }
            page++;
        }
    }

    private static synchronized String getZohoAccessToken() throws Exception
    {
        long now = System.currentTimeMillis();
        if (s_cachedToken != null && now < s_tokenExpiryTime)
        {
            return s_cachedToken;
        }

        String clientId = "1000.LEHQGZ4J5ODKNV6IZTI5Y2YOM74NER";
        String clientSecret = "8e7ecb00424af024312f9f3d507680fc3d602c7784";
        String scope = "ZohoExpense.fullaccess.ALL";

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
            .add("grant_type", "client_credentials")
            .add("client_secret", clientSecret)
            .add("client_id", clientId)
            .add("scope", scope)
            .build();

        Request authReq = new Request.Builder()
            .url("https://accounts.zoho.com/oauth/v2/token")
            .post(formBody)
            .addHeader("Content-Type", "application/json")
            .build();

        Response authResp = client.newCall(authReq).execute();
        if (!authResp.isSuccessful())
            throw new Exception("Erreur HTTP " + authResp.code() + " lors de l'authentification Zoho");

        JsonObject authJson = JsonParser.parseString(authResp.body().string()).getAsJsonObject();
        if (!authJson.has("access_token"))
            throw new Exception("Jeton Zoho introuvable dans la r\u00e9ponse");

        s_cachedToken = authJson.get("access_token").getAsString();
        int expiresIn = authJson.has("expires_in") ? authJson.get("expires_in").getAsInt() : 3600;
        s_tokenExpiryTime = now + (expiresIn - 300) * 1000L;

        return s_cachedToken;
    }

    private static long getNextSequenceValue(String sequenceName)
    {
        String sql = "SELECT NEXT VALUE FOR " + sequenceName;
        try (PreparedStatement ps = DB.prepareStatement(sql, null);
             ResultSet rs = ps.executeQuery())
        {
            if (rs.next())
                return rs.getLong(1);
        }
        catch (Exception e)
        {
            s_log.log(Level.SEVERE, "getNextSequenceValue: " + sequenceName, e);
        }
        return 0;
    }

    private static BigDecimal getJsonBigDecimal(JsonObject jobj, String key)
    {
        if (jobj.has(key) && !jobj.get(key).isJsonNull())
        {
            try
            {
                return jobj.get(key).getAsBigDecimal();
            }
            catch (Exception ignored) {}
        }
        return BigDecimal.ZERO;
    }

    private static Timestamp parseJsonTimestamp(JsonObject jobj, String key)
    {
        if (!jobj.has(key) || jobj.get(key).isJsonNull()) return null;
        String s = jobj.get(key).getAsString().trim();
        if (s.isEmpty()) return null;
        try
        {
            if (s.length() >= 19)
            {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                return new Timestamp(sdf.parse(s.substring(0, 19)).getTime());
            }
            else if (s.length() == 10)
            {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                return new Timestamp(sdf.parse(s).getTime());
            }
        }
        catch (Exception ignored) {}
        return null;
    }

    /**
     * Exportation professionnelle multi-onglets vers Excel XLSX avec Apache POI
     */
    public void exportToExcel()
    {
        if (m_rows.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Aucune donn\u00e9e \u00e0 exporter.", "Export Excel", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String defaultFileName = "Audit_Zoho_Expense_" + new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date()) + ".xlsx";
        File file = ExcelExportUtil.chooseFile(defaultFileName, this);
        if (file == null)
            return;

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try (XSSFWorkbook wb = new XSSFWorkbook())
        {
            XSSFDataFormat df = wb.createDataFormat();
            short currencyFmt = df.getFormat("$#,##0.00;($#,##0.00);\"-\"");
            short dateFmt = df.getFormat("yyyy-mm-dd");

            // Styles
            XSSFCellStyle headerStyle = wb.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            XSSFFont headerFont = wb.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setBorderBottom(BorderStyle.THIN);

            XSSFCellStyle currencyStyle = wb.createCellStyle();
            currencyStyle.setDataFormat(currencyFmt);

            XSSFCellStyle dateStyle = wb.createCellStyle();
            dateStyle.setDataFormat(dateFmt);

            // Styles d'anomalies
            XSSFCellStyle dupStyle = wb.createCellStyle();
            dupStyle.setFillForegroundColor(IndexedColors.CORAL.getIndex());
            dupStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            XSSFFont dupFont = wb.createFont();
            dupFont.setBold(true);
            dupFont.setColor(IndexedColors.DARK_RED.getIndex());
            dupStyle.setFont(dupFont);

            XSSFCellStyle unreimbStyle = wb.createCellStyle();
            unreimbStyle.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
            unreimbStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            XSSFFont unreimbFont = wb.createFont();
            unreimbFont.setBold(true);
            unreimbFont.setColor(IndexedColors.DARK_YELLOW.getIndex());
            unreimbStyle.setFont(unreimbFont);

            XSSFCellStyle matchedStyle = wb.createCellStyle();
            matchedStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            matchedStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            XSSFFont matchedFont = wb.createFont();
            matchedFont.setBold(true);
            matchedFont.setColor(IndexedColors.DARK_GREEN.getIndex());
            matchedStyle.setFont(matchedFont);

            // Feuille 1 : Audit Global
            XSSFSheet sheetGlobal = wb.createSheet("Audit_Global");
            writeAuditSheet(sheetGlobal, m_rows, headerStyle, currencyStyle, dateStyle, dupStyle, unreimbStyle, matchedStyle);

            // Feuille 2 : Doublons \u00e0 r\u00e9gulariser
            List<AuditRow> dupRows = new ArrayList<AuditRow>();
            for (AuditRow r : m_rows)
            {
                if (STATUS_DUPLICATE.equals(r.auditStatus))
                    dupRows.add(r);
            }
            XSSFSheet sheetDup = wb.createSheet("Doublons_A_Regulariser");
            writeAuditSheet(sheetDup, dupRows, headerStyle, currencyStyle, dateStyle, dupStyle, unreimbStyle, matchedStyle);

            // Feuille 3 : Non rembours\u00e9s
            List<AuditRow> unreimbRows = new ArrayList<AuditRow>();
            for (AuditRow r : m_rows)
            {
                if (STATUS_UNREIMBURSED.equals(r.auditStatus))
                    unreimbRows.add(r);
            }
            XSSFSheet sheetUnreimb = wb.createSheet("Non_Rembourses");
            writeAuditSheet(sheetUnreimb, unreimbRows, headerStyle, currencyStyle, dateStyle, dupStyle, unreimbStyle, matchedStyle);

            try (FileOutputStream fos = new FileOutputStream(file))
            {
                wb.write(fos);
            }

            int open = JOptionPane.showConfirmDialog(this,
                "Exportation r\u00e9ussie vers :\n" + file.getAbsolutePath() + "\n\nSouhaitez-vous ouvrir le fichier maintenant ?",
                "Export Excel termin\u00e9", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

            if (open == JOptionPane.YES_OPTION)
            {
                java.awt.Desktop.getDesktop().open(file);
            }
        }
        catch (Exception e)
        {
            s_log.log(Level.SEVERE, "exportToExcel", e);
            JOptionPane.showMessageDialog(this, "Erreur lors de l'exportation Excel :\n" + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        finally
        {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    private void writeAuditSheet(XSSFSheet sheet, List<AuditRow> rows, XSSFCellStyle headerStyle,
        XSSFCellStyle currencyStyle, XSSFCellStyle dateStyle, XSSFCellStyle dupStyle,
        XSSFCellStyle unreimbStyle, XSSFCellStyle matchedStyle)
    {
        String[] headers = {
            "Statut Audit", "P\u00e9riode", "Matricule", "Employ\u00e9", "No Rapport Zoho", "Nom Rapport",
            "Date Approbation", "Montant Zoho", "Feuille Temps", "Statut TS", "No Paiement",
            "Date Paie", "Montant Paie", "\u00c9cart ($)", "Nb Paies", "D\u00e9tail Paiements Multiples", "Diagnostic"
        };

        XSSFRow hRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++)
        {
            XSSFCell c = hRow.createCell(i);
            c.setCellValue(headers[i]);
            c.setCellStyle(headerStyle);
        }

        int rIdx = 1;
        for (AuditRow r : rows)
        {
            XSSFRow row = sheet.createRow(rIdx++);

            XSSFCell c0 = row.createCell(0);
            c0.setCellValue(r.statusDisplay);
            if (STATUS_DUPLICATE.equals(r.auditStatus)) c0.setCellStyle(dupStyle);
            else if (STATUS_UNREIMBURSED.equals(r.auditStatus)) c0.setCellStyle(unreimbStyle);
            else if (STATUS_MATCHED.equals(r.auditStatus)) c0.setCellStyle(matchedStyle);

            row.createCell(1).setCellValue(r.periodName != null ? r.periodName : "");
            row.createCell(2).setCellValue(r.employeeValue != null ? r.employeeValue : "");
            row.createCell(3).setCellValue(r.employeeName != null ? r.employeeName : "");
            row.createCell(4).setCellValue(r.reportNumber != null ? r.reportNumber : "");
            row.createCell(5).setCellValue(r.reportName != null ? r.reportName : "");

            XSSFCell cDate = row.createCell(6);
            if (r.zohoApprovedDate != null)
            {
                cDate.setCellValue(r.zohoApprovedDate);
                cDate.setCellStyle(dateStyle);
            }

            XSSFCell cZohoAmt = row.createCell(7);
            cZohoAmt.setCellValue(r.zohoAmount != null ? r.zohoAmount.doubleValue() : 0.0);
            cZohoAmt.setCellStyle(currencyStyle);

            row.createCell(8).setCellValue(r.timeSheetId > 0 ? String.valueOf(r.timeSheetId) : "");
            row.createCell(9).setCellValue(r.timeSheetStatus != null ? r.timeSheetStatus : "");
            row.createCell(10).setCellValue(r.paymentId > 0 ? String.valueOf(r.paymentId) : "");

            XSSFCell cPayDate = row.createCell(11);
            if (r.payDate != null)
            {
                cPayDate.setCellValue(r.payDate);
                cPayDate.setCellStyle(dateStyle);
            }

            XSSFCell cPayAmt = row.createCell(12);
            cPayAmt.setCellValue(r.paidAmount != null ? r.paidAmount.doubleValue() : 0.0);
            cPayAmt.setCellStyle(currencyStyle);

            XSSFCell cDiff = row.createCell(13);
            cDiff.setCellValue(r.difference != null ? r.difference.doubleValue() : 0.0);
            cDiff.setCellStyle(currencyStyle);

            row.createCell(14).setCellValue(r.paymentCount);
            row.createCell(15).setCellValue(r.multiplePaymentsDetail != null ? r.multiplePaymentsDetail : "");
            row.createCell(16).setCellValue(r.diagnostic != null ? r.diagnostic : "");
        }

        // Auto-size columns (limit to 12000 width)
        for (int i = 0; i < headers.length; i++)
        {
            sheet.autoSizeColumn(i);
            if (sheet.getColumnWidth(i) > 12000)
                sheet.setColumnWidth(i, 12000);
        }
    }

    /**
     * Point d'entr\u00e9e pour test standalone
     */
    public static void open()
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                JFrame frame = new JFrame("Audit des Remboursements Zoho Expense vs Paie");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                VZohoExpenseValidation panel = new VZohoExpenseValidation();
                FormFrame ff = new FormFrame();
                panel.init(0, ff);
                frame.getContentPane().add(panel, BorderLayout.CENTER);
                frame.setSize(1380, 880);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    // =========================================================================
    // Mod\u00e8les de Tables & Classes Internes de Donn\u00e9es
    // =========================================================================

    private static class PeriodInfo
    {
        int periodId;
        int periodNo;
        String name;
        Timestamp startDate;
        Timestamp endDate;
        String status;
        String fiscalYear;
    }

    private static class ZohoRowRaw
    {
        int expenseReportId;
        String reportId;
        String reportNumber;
        String reportName;
        String description;
        int employeeId;
        String zohoEmpNo;
        String empValue;
        String empName;
        int periodId;
        String periodName;
        int periodNo;
        int timeSheetId;
        int paymentId;
        BigDecimal amount;
        String status;
        Timestamp approvedDate;
        Timestamp submittedDate;
        Timestamp reimbursementDate;
        boolean isReimbursable;
    }

    private static class TimeSheetPayInfo
    {
        int timeSheetId;
        int employeeId;
        int periodId;
        String status;
        int paymentId;
        Timestamp payDate;
        String docStatus;
        BigDecimal netPay;
        BigDecimal expenseGainAmount;
    }

    private static class PaymentGainSummary
    {
        int paymentId;
        int employeeId;
        int periodId;
        Timestamp payDate;
        String docStatus;
        BigDecimal netPay;
        BigDecimal expenseAmount;
    }

    public static class AuditRow
    {
        public int expenseReportId;
        public String reportId;
        public String reportNumber;
        public String reportName;
        public String description;
        public int employeeId;
        public String employeeValue;
        public String employeeName;
        public int periodId;
        public String periodName;

        public String auditStatus;
        public String statusDisplay;

        public BigDecimal zohoAmount;
        public String zohoStatus;
        public Timestamp zohoApprovedDate;

        public int timeSheetId;
        public String timeSheetStatus;

        public int paymentId;
        public Timestamp payDate;
        public String payDocStatus;
        public BigDecimal paidAmount;
        public BigDecimal difference;

        public int paymentCount;
        public String multiplePaymentsDetail;
        public String diagnostic;
    }

    private class AuditMasterTableModel extends AbstractTableModel
    {
        private static final long serialVersionUID = 1L;
        private final String[] COLS = {
            "Statut Audit", "P\u00e9riode", "Matricule", "Employ\u00e9", "No Zoho", "Nom Rapport",
            "Approbation", "Montant Zoho", "ID TS", "Statut TS", "ID Paie", "Date Paie",
            "Montant Paie", "\u00c9cart ($)", "Nb Paies", "D\u00e9tail Multi-Paiements", "Statut Zoho", "Diagnostic"
        };

        @Override public int getRowCount() { return m_rows.size(); }
        @Override public int getColumnCount() { return COLS.length; }
        @Override public String getColumnName(int c) { return COLS[c]; }

        @Override
        public Class<?> getColumnClass(int c)
        {
            if (c == 7 || c == 12 || c == 13) return BigDecimal.class;
            if (c == 8 || c == 10 || c == 14) return Integer.class;
            return String.class;
        }

        @Override
        public Object getValueAt(int r, int c)
        {
            AuditRow row = m_rows.get(r);
            switch (c)
            {
                case 0: return row.statusDisplay;
                case 1: return row.periodName;
                case 2: return row.employeeValue;
                case 3: return row.employeeName;
                case 4: return row.reportNumber;
                case 5: return row.reportName;
                case 6: return row.zohoApprovedDate != null ? DATE_FMT.format(row.zohoApprovedDate) : "";
                case 7: return row.zohoAmount;
                case 8: return row.timeSheetId > 0 ? row.timeSheetId : null;
                case 9: return row.timeSheetStatus;
                case 10: return row.paymentId > 0 ? row.paymentId : null;
                case 11: return row.payDate != null ? DATE_FMT.format(row.payDate) : "";
                case 12: return row.paidAmount;
                case 13: return row.difference;
                case 14: return row.paymentCount;
                case 15: return row.multiplePaymentsDetail;
                case 16: return row.zohoStatus;
                case 17: return row.diagnostic;
                default: return null;
            }
        }
    }

    private static class ZohoItemDetail
    {
        String category;
        BigDecimal amount;
        BigDecimal tps;
        BigDecimal tvq;
        BigDecimal tvh;
        Timestamp itemDate;
        String description;
        String glCode;
    }

    private class ZohoDetailTableModel extends AbstractTableModel
    {
        private static final long serialVersionUID = 1L;
        private final String[] COLS = {
            "Date", "Cat\u00e9gorie", "Montant Total", "TPS (GL14)", "TVQ (GL15)", "TVH (GL16)", "Code GL", "Description"
        };

        @Override public int getRowCount() { return m_currentZohoDetails.size(); }
        @Override public int getColumnCount() { return COLS.length; }
        @Override public String getColumnName(int c) { return COLS[c]; }

        @Override
        public Object getValueAt(int r, int c)
        {
            ZohoItemDetail d = m_currentZohoDetails.get(r);
            switch (c)
            {
                case 0: return d.itemDate != null ? DATE_FMT.format(d.itemDate) : "";
                case 1: return d.category;
                case 2: return d.amount != null ? CURRENCY_FMT.format(d.amount) : "0.00 $";
                case 3: return d.tps != null ? CURRENCY_FMT.format(d.tps) : "0.00 $";
                case 4: return d.tvq != null ? CURRENCY_FMT.format(d.tvq) : "0.00 $";
                case 5: return d.tvh != null ? CURRENCY_FMT.format(d.tvh) : "0.00 $";
                case 6: return d.glCode;
                case 7: return d.description;
                default: return null;
            }
        }
    }

    private static class PayrollGainDetail
    {
        String gainCode;
        String gainName;
        BigDecimal amount;
        BigDecimal rate;
        BigDecimal qty;
    }

    private class PayrollDetailTableModel extends AbstractTableModel
    {
        private static final long serialVersionUID = 1L;
        private final String[] COLS = { "Code Gain", "Description du Gain", "Quantit\u00e9", "Taux", "Montant Calcul\u00e9 ($)" };

        @Override public int getRowCount() { return m_currentPayrollDetails.size(); }
        @Override public int getColumnCount() { return COLS.length; }
        @Override public String getColumnName(int c) { return COLS[c]; }

        @Override
        public Object getValueAt(int r, int c)
        {
            PayrollGainDetail g = m_currentPayrollDetails.get(r);
            switch (c)
            {
                case 0: return g.gainCode;
                case 1: return g.gainName;
                case 2: return g.qty != null ? g.qty.toString() : "";
                case 3: return g.rate != null ? g.rate.toString() : "";
                case 4: return g.amount != null ? CURRENCY_FMT.format(g.amount) : "0.00 $";
                default: return null;
            }
        }
    }

    private static class EmpHistoryItem
    {
        int paymentId;
        String periodName;
        Timestamp payDate;
        String docStatus;
        int timeSheetId;
        String timeSheetStatus;
        BigDecimal amount;
    }

    private class EmpHistoryTableModel extends AbstractTableModel
    {
        private static final long serialVersionUID = 1L;
        private final String[] COLS = { "No Paiement", "P\u00e9riode", "Date de Paie", "Statut Paiement", "Feuille Temps", "Statut TS", "Montant D\u00e9penses ($)" };

        @Override public int getRowCount() { return m_currentEmpHistory.size(); }
        @Override public int getColumnCount() { return COLS.length; }
        @Override public String getColumnName(int c) { return COLS[c]; }

        @Override
        public Object getValueAt(int r, int c)
        {
            EmpHistoryItem h = m_currentEmpHistory.get(r);
            switch (c)
            {
                case 0: return h.paymentId > 0 ? String.valueOf(h.paymentId) : "";
                case 1: return h.periodName;
                case 2: return h.payDate != null ? DATE_FMT.format(h.payDate) : "";
                case 3: return h.docStatus;
                case 4: return h.timeSheetId > 0 ? String.valueOf(h.timeSheetId) : "";
                case 5: return h.timeSheetStatus;
                case 6: return h.amount != null ? CURRENCY_FMT.format(h.amount) : "0.00 $";
                default: return null;
            }
        }
    }

    private static class SyncStats
    {
        int totalProcessed = 0;
        int insertedCount = 0;
        int updatedCount = 0;
        int detailCount = 0;
    }

    private static class RepDetailQueueItem
    {
        long valId;
        String repId;
        String repNo;
        int orgId;

        RepDetailQueueItem(long valId, String repId, String repNo, int orgId)
        {
            this.valId = valId;
            this.repId = repId;
            this.repNo = repNo;
            this.orgId = orgId;
        }
    }
}
