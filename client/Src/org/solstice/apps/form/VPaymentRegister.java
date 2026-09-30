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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

/**
 * Registre de paie interactif (Payroll Register) de Solstice+.
 * <p>
 * Affiche la synthese et le detail complet des paiements pour une periode, un groupe
 * de paiement et une organisation selectionnes, avec synchronisation dynamique entre :
 * <ul>
 *   <li>P_Payment (en-tete de paie, salaires bruts, retenues, avantages, net, cotisations)</li>
 *   <li>P_Payment_Gain (gains, heures et primes)</li>
 *   <li>P_Payment_Deduction (retenues employe et cotisations employeur)</li>
 *   <li>P_Payment_Taxable_Benefit (avantages imposables)</li>
 * </ul>
 * Propose egalement une recapitulation globale pour les remises/comptabilite et un
 * export complet multi-onglets vers Excel au format natif (.xlsx).
 *
 * @author Solstice / Antigravity
 */
public class VPaymentRegister extends CPanel implements FormPanel, ActionListener
{
    private static final long serialVersionUID = 1L;
    private static final CLogger s_log = CLogger.getCLogger(VPaymentRegister.class);

    private static final DecimalFormat CURRENCY_FMT = new DecimalFormat("#,##0.00 $");
    private static final DecimalFormat HOURS_FMT = new DecimalFormat("#,##0.00");
    private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("yyyy-MM-dd");

    private Properties m_ctx;
    private FormFrame m_frame = null;
    private int m_WindowNo = 0;

    // Filtres
    private JComboBox<KeyNamePair> comboPeriod;
    private JComboBox<KeyNamePair> comboPaymentGroup;
    private JComboBox<KeyNamePair> comboOrg;
    private JTextField txtSearch;
    private JButton btnRefresh;
    private JButton btnExportExcel;
    private JButton btnZoom;

    // Cartes KPIs
    private JLabel lblKpiEmployees;
    private JLabel lblKpiHours;
    private JLabel lblKpiGross;
    private JLabel lblKpiTaxable;
    private JLabel lblKpiDeductions;
    private JLabel lblKpiNet;
    private JLabel lblKpiEmployerPart;
    private JLabel lblKpiEmployerCost;

    // Tables principales & details
    private JTable tblMaster;
    private MasterPaymentTableModel masterModel;
    private TableRowSorter<MasterPaymentTableModel> masterSorter;

    private JTable tblGains;
    private GainsTableModel gainsModel;

    private JTable tblDeductions;
    private DeductionsTableModel deductionsModel;

    private JTable tblBenefits;
    private BenefitsTableModel benefitsModel;

    // Tables de recapitulation globale
    private JTable tblSumGains;
    private SummaryGainsTableModel sumGainsModel;

    private JTable tblSumDeductions;
    private SummaryDeductionsTableModel sumDeductionsModel;

    private JTable tblSumBenefits;
    private SummaryBenefitsTableModel sumBenefitsModel;

    // Labels du panneau de balancement comptable
    private JLabel lblBalGross;
    private JLabel lblBalDeductions;
    private JLabel lblBalNet;
    private JLabel lblBalEmployerPart;
    private JLabel lblBalTotalCost;
    private JLabel lblBalStatus;

    // Donnees en memoire
    private final List<PaymentMasterRow> m_masterRows = new ArrayList<PaymentMasterRow>();
    private final List<GainRow> m_gainRows = new ArrayList<GainRow>();
    private final List<DeductionRow> m_deductionRows = new ArrayList<DeductionRow>();
    private final List<BenefitRow> m_benefitRows = new ArrayList<BenefitRow>();

    private final List<SummaryItemRow> m_sumGainsRows = new ArrayList<SummaryItemRow>();
    private final List<SummaryDeductionRow> m_sumDeductionRows = new ArrayList<SummaryDeductionRow>();
    private final List<SummaryItemRow> m_sumBenefitRows = new ArrayList<SummaryItemRow>();

    private int m_selectedPaymentId = 0;
    private int m_selectedEmployeeId = 0;

    /**
     * Constructeur
     */
    public VPaymentRegister()
    {
        super();
        m_ctx = Env.getCtx();
        jbInit();
        dynInit();
    }

    /**
     * Implementation FormPanel : initialisation dans le FormFrame de Compiere
     */
    @Override
    public void init(int WindowNo, FormFrame frame)
    {
        this.m_WindowNo = WindowNo;
        this.m_frame = frame;
        if (m_frame != null)
        {
            m_frame.setTitle("Registre de paie");
            m_frame.setPreferredSize(new Dimension(1280, 860));
            m_frame.getContentPane().add(this, BorderLayout.CENTER);
        }
    }

    @Override
    public void dispose()
    {
        if (m_frame != null)
        {
            m_frame.dispose();
            m_frame = null;
        }
    }

    /**
     * Construction de l'interface graphique (Swing)
     */
    private void jbInit()
    {
        setLayout(new BorderLayout(0, 8));
        setBackground(new Color(243, 244, 246));
        setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        // 1. Panneau Superieur : Filtres + KPIs
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        northPanel.setOpaque(false);

        northPanel.add(createFilterPanel());
        northPanel.add(Box.createVerticalStrut(8));
        northPanel.add(createKpiPanel());

        add(northPanel, BorderLayout.NORTH);

        // 2. Panneau Central : Onglets (Registre detaille vs Recapitulation de periode)
        JTabbedPane mainTabs = new JTabbedPane();
        mainTabs.setFont(new Font("Segoe UI", Font.BOLD, 12));

        mainTabs.addTab("  Registre D\u00e9taill\u00e9 par Employ\u00e9  ", createMasterDetailPanel());
        mainTabs.addTab("  R\u00e9capitulation Globale de la P\u00e9riode  ", createSummaryPanel());

        add(mainTabs, BorderLayout.CENTER);
    }

    /**
     * Panneau des filtres
     */
    private JPanel createFilterPanel()
    {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 224, 230), 1),
            BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Titre
        JLabel lblTitle = new JLabel("Registre de paie");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(24, 43, 73));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);

        // Recherche rapide
        gbc.gridx = 2; gbc.gridy = 0; gbc.gridwidth = 2;
        JPanel searchBox = new JPanel(new BorderLayout(5, 0));
        searchBox.setOpaque(false);
        JLabel lblFind = new JLabel("Recherche rapide :");
        lblFind.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtSearch = new JTextField(15);
        txtSearch.setToolTipText("Filtrer par matricule, nom ou pr\u00e9nom");
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { applySearchFilter(); }
            @Override public void removeUpdate(DocumentEvent e) { applySearchFilter(); }
            @Override public void changedUpdate(DocumentEvent e) { applySearchFilter(); }
        });
        searchBox.add(lblFind, BorderLayout.WEST);
        searchBox.add(txtSearch, BorderLayout.CENTER);
        panel.add(searchBox, gbc);

        // Boutons d'actions
        gbc.gridx = 4; gbc.gridy = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.EAST;
        JPanel btnBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        btnBox.setOpaque(false);

        btnRefresh = new JButton("Actualiser");
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRefresh.setIcon(Env.getImageIcon("Refresh16.gif"));
        btnRefresh.addActionListener(this);

        btnExportExcel = new JButton("Exporter Excel (.xlsx)");
        btnExportExcel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExportExcel.setIcon(Env.getImageIcon("Export16.gif"));
        btnExportExcel.setBackground(new Color(30, 126, 52));
        btnExportExcel.setForeground(Color.WHITE);
        btnExportExcel.addActionListener(this);

        btnZoom = new JButton("Voir Paie");
        btnZoom.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnZoom.setIcon(Env.getImageIcon("Zoom16.gif"));
        btnZoom.setToolTipText("Ouvrir la fiche de paiement de l'employ\u00e9 s\u00e9lectionn\u00e9");
        btnZoom.addActionListener(this);

        btnBox.add(btnRefresh);
        btnBox.add(btnExportExcel);
        btnBox.add(btnZoom);
        panel.add(btnBox, gbc);

        // Ligne 2 : Les filtres
        gbc.gridy = 1; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;

        // P\u00e9riode
        gbc.gridx = 0;
        JLabel lblPeriod = new JLabel("P\u00e9riode de paie :");
        lblPeriod.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(lblPeriod, gbc);

        gbc.gridx = 1;
        comboPeriod = new JComboBox<KeyNamePair>();
        comboPeriod.setPreferredSize(new Dimension(240, 26));
        comboPeriod.addActionListener(this);
        panel.add(comboPeriod, gbc);

        // Groupe de paiement
        gbc.gridx = 2;
        JLabel lblGroup = new JLabel("Groupe de paiement :");
        lblGroup.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(lblGroup, gbc);

        gbc.gridx = 3;
        comboPaymentGroup = new JComboBox<KeyNamePair>();
        comboPaymentGroup.setPreferredSize(new Dimension(210, 26));
        comboPaymentGroup.addActionListener(this);
        panel.add(comboPaymentGroup, gbc);

        // Organisation
        gbc.gridx = 4;
        JLabel lblOrg = new JLabel("Organisation :");
        lblOrg.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(lblOrg, gbc);

        gbc.gridx = 5;
        comboOrg = new JComboBox<KeyNamePair>();
        comboOrg.setPreferredSize(new Dimension(200, 26));
        comboOrg.addActionListener(this);
        panel.add(comboOrg, gbc);

        return panel;
    }

    /**
     * Panneau des indicateurs KPIs
     */
    private JPanel createKpiPanel()
    {
        JPanel panel = new JPanel(new GridLayout(1, 7, 8, 0));
        panel.setOpaque(false);

        lblKpiEmployees = new JLabel("0", SwingConstants.CENTER);
        lblKpiHours = new JLabel("0.00 h", SwingConstants.CENTER);
        lblKpiGross = new JLabel("0.00 $", SwingConstants.CENTER);
        lblKpiTaxable = new JLabel("0.00 $", SwingConstants.CENTER);
        lblKpiDeductions = new JLabel("0.00 $", SwingConstants.CENTER);
        lblKpiNet = new JLabel("0.00 $", SwingConstants.CENTER);
        lblKpiEmployerCost = new JLabel("0.00 $", SwingConstants.CENTER);

        panel.add(createKpiCard("Employ\u00e9s Pay\u00e9s", lblKpiEmployees, new Color(44, 62, 80)));
        panel.add(createKpiCard("Heures Totales", lblKpiHours, new Color(52, 73, 94)));
        panel.add(createKpiCard("Total Brut", lblKpiGross, new Color(41, 128, 185)));
        panel.add(createKpiCard("Avantages Impos.", lblKpiTaxable, new Color(142, 68, 173)));
        panel.add(createKpiCard("Retenues Employ\u00e9", lblKpiDeductions, new Color(192, 57, 43)));
        panel.add(createKpiCard("Net Pay\u00e9", lblKpiNet, new Color(39, 174, 96)));
        panel.add(createKpiCard("Co\u00fbt Total Employeur", lblKpiEmployerCost, new Color(211, 84, 0)));

        return panel;
    }

    private JPanel createKpiCard(String title, JLabel valueLabel, Color accentColor)
    {
        JPanel card = new JPanel(new BorderLayout(0, 4));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(225, 229, 235), 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTitle.setForeground(new Color(110, 118, 129));

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        valueLabel.setForeground(accentColor);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    /**
     * Panneau Ma\u00eetre-D\u00e9tail : Liste des employes en haut + details en bas
     */
    private JPanel createMasterDetailPanel()
    {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setOpaque(false);

        // Table Maitre
        masterModel = new MasterPaymentTableModel();
        tblMaster = new JTable(masterModel);
        masterSorter = new TableRowSorter<MasterPaymentTableModel>(masterModel);
        tblMaster.setRowSorter(masterSorter);
        setupTableStyle(tblMaster);

        tblMaster.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                if (!e.getValueIsAdjusting())
                {
                    int viewRow = tblMaster.getSelectedRow();
                    if (viewRow >= 0)
                    {
                        int modelRow = tblMaster.convertRowIndexToModel(viewRow);
                        PaymentMasterRow row = masterModel.getRow(modelRow);
                        if (row != null)
                        {
                            m_selectedPaymentId = row.paymentId;
                            m_selectedEmployeeId = row.employeeId;
                            loadPaymentDetails(row.paymentId);
                        }
                    }
                    else
                    {
                        m_selectedPaymentId = 0;
                        m_selectedEmployeeId = 0;
                        clearPaymentDetails();
                    }
                }
            }
        });

        tblMaster.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getClickCount() == 2 && tblMaster.getSelectedRow() >= 0)
                {
                    zoomToPayment();
                }
            }
        });

        JScrollPane scrollMaster = new JScrollPane(tblMaster);
        scrollMaster.setBorder(BorderFactory.createLineBorder(new Color(220, 224, 230)));

        JPanel masterContainer = new JPanel(new BorderLayout(0, 4));
        masterContainer.setBackground(Color.WHITE);
        JLabel lblMasterHeader = new JLabel("  Liste des paiements de la p\u00e9riode (s\u00e9lectionnez un employ\u00e9 pour afficher le d\u00e9tail ci-dessous) :");
        lblMasterHeader.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblMasterHeader.setForeground(new Color(100, 110, 120));
        masterContainer.add(lblMasterHeader, BorderLayout.NORTH);
        masterContainer.add(scrollMaster, BorderLayout.CENTER);

        // Sous-onglets de detail
        JTabbedPane detailTabs = new JTabbedPane();
        detailTabs.setFont(new Font("Segoe UI", Font.BOLD, 11));

        // Tab Gains
        gainsModel = new GainsTableModel();
        tblGains = new JTable(gainsModel);
        setupTableStyle(tblGains);
        detailTabs.addTab("Gains & Heures (P_Payment_Gain)", new JScrollPane(tblGains));

        // Tab Deductions
        deductionsModel = new DeductionsTableModel();
        tblDeductions = new JTable(deductionsModel);
        setupTableStyle(tblDeductions);
        detailTabs.addTab("Retenues & Cotisations (P_Payment_Deduction)", new JScrollPane(tblDeductions));

        // Tab Avantages
        benefitsModel = new BenefitsTableModel();
        tblBenefits = new JTable(benefitsModel);
        setupTableStyle(tblBenefits);
        detailTabs.addTab("Avantages Imposables (P_Payment_Taxable_Benefit)", new JScrollPane(tblBenefits));

        // SplitPane vertical
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, masterContainer, detailTabs);
        split.setResizeWeight(0.55);
        split.setDividerLocation(360);
        split.setContinuousLayout(true);
        split.setBorder(null);

        panel.add(split, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Panneau de recapitulation globale de la periode (sommaire comptable)
     */
    private JPanel createSummaryPanel()
    {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // 3 Tables : Sommaire Gains, Sommaire Retenues, Sommaire Avantages
        JPanel tablesGrid = new JPanel(new GridLayout(1, 3, 8, 0));
        tablesGrid.setOpaque(false);

        // Gains
        sumGainsModel = new SummaryGainsTableModel();
        tblSumGains = new JTable(sumGainsModel);
        setupTableStyle(tblSumGains);
        JPanel pnlG = new JPanel(new BorderLayout(0, 4));
        pnlG.setOpaque(false);
        pnlG.add(createSectionHeader("Sommaire des Gains par Code"), BorderLayout.NORTH);
        pnlG.add(new JScrollPane(tblSumGains), BorderLayout.CENTER);
        tablesGrid.add(pnlG);

        // Deductions
        sumDeductionsModel = new SummaryDeductionsTableModel();
        tblSumDeductions = new JTable(sumDeductionsModel);
        setupTableStyle(tblSumDeductions);
        JPanel pnlD = new JPanel(new BorderLayout(0, 4));
        pnlD.setOpaque(false);
        pnlD.add(createSectionHeader("Sommaire des Retenues par Code"), BorderLayout.NORTH);
        pnlD.add(new JScrollPane(tblSumDeductions), BorderLayout.CENTER);
        tablesGrid.add(pnlD);

        // Avantages
        sumBenefitsModel = new SummaryBenefitsTableModel();
        tblSumBenefits = new JTable(sumBenefitsModel);
        setupTableStyle(tblSumBenefits);
        JPanel pnlB = new JPanel(new BorderLayout(0, 4));
        pnlB.setOpaque(false);
        pnlB.add(createSectionHeader("Sommaire des Avantages Imposables"), BorderLayout.NORTH);
        pnlB.add(new JScrollPane(tblSumBenefits), BorderLayout.CENTER);
        tablesGrid.add(pnlB);

        panel.add(tablesGrid, BorderLayout.CENTER);

        // Panneau inferieur de balance comptable
        JPanel balancePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        balancePanel.setBackground(new Color(248, 250, 252));
        balancePanel.setBorder(BorderFactory.createLineBorder(new Color(220, 224, 230), 1));

        lblBalGross = new JLabel("Brut : 0.00 $");
        lblBalGross.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblBalGross.setForeground(new Color(41, 128, 185));

        JLabel lblMinus = new JLabel("-", SwingConstants.CENTER);
        lblMinus.setFont(new Font("Segoe UI", Font.BOLD, 16));

        lblBalDeductions = new JLabel("Retenues Emp. : 0.00 $");
        lblBalDeductions.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblBalDeductions.setForeground(new Color(192, 57, 43));

        JLabel lblEqual = new JLabel("=", SwingConstants.CENTER);
        lblEqual.setFont(new Font("Segoe UI", Font.BOLD, 16));

        lblBalNet = new JLabel("Net Pay\u00e9 : 0.00 $");
        lblBalNet.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblBalNet.setForeground(new Color(39, 174, 96));

        JLabel lblSep = new JLabel("|", SwingConstants.CENTER);
        lblSep.setForeground(Color.LIGHT_GRAY);

        lblBalEmployerPart = new JLabel("Cotis. Employeur : 0.00 $");
        lblBalEmployerPart.setFont(new Font("Segoe UI", Font.BOLD, 13));

        lblBalTotalCost = new JLabel("Co\u00fbt Employeur : 0.00 $");
        lblBalTotalCost.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblBalTotalCost.setForeground(new Color(211, 84, 0));

        lblBalStatus = new JLabel("  OK  ", SwingConstants.CENTER);
        lblBalStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblBalStatus.setOpaque(true);
        lblBalStatus.setBackground(new Color(212, 239, 223));
        lblBalStatus.setForeground(new Color(30, 126, 52));
        lblBalStatus.setBorder(BorderFactory.createLineBorder(new Color(46, 204, 113)));

        balancePanel.add(lblBalGross);
        balancePanel.add(lblMinus);
        balancePanel.add(lblBalDeductions);
        balancePanel.add(lblEqual);
        balancePanel.add(lblBalNet);
        balancePanel.add(lblSep);
        balancePanel.add(lblBalEmployerPart);
        balancePanel.add(lblBalTotalCost);
        balancePanel.add(lblBalStatus);

        panel.add(balancePanel, BorderLayout.SOUTH);
        return panel;
    }

    private JLabel createSectionHeader(String title)
    {
        JLabel lbl = new JLabel(" " + title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(24, 43, 73));
        lbl.setBorder(BorderFactory.createEmptyBorder(2, 4, 4, 4));
        return lbl;
    }

    private void setupTableStyle(JTable table)
    {
        table.setRowHeight(24);
        table.setGridColor(new Color(230, 233, 238));
        table.setShowGrid(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 11));
        header.setBackground(new Color(242, 245, 249));
        header.setForeground(new Color(30, 41, 59));
        header.setPreferredSize(new Dimension(0, 28));

        DefaultTableCellRenderer currencyRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r, int c)
            {
                super.getTableCellRendererComponent(t, val, sel, foc, r, c);
                setHorizontalAlignment(SwingConstants.RIGHT);
                if (val instanceof BigDecimal)
                {
                    setText(CURRENCY_FMT.format((BigDecimal) val));
                }
                else if (val instanceof Number)
                {
                    setText(CURRENCY_FMT.format(((Number) val).doubleValue()));
                }
                return this;
            }
        };

        DefaultTableCellRenderer numberRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r, int c)
            {
                super.getTableCellRendererComponent(t, val, sel, foc, r, c);
                setHorizontalAlignment(SwingConstants.RIGHT);
                if (val instanceof BigDecimal)
                {
                    setText(HOURS_FMT.format((BigDecimal) val));
                }
                else if (val instanceof Number)
                {
                    setText(HOURS_FMT.format(((Number) val).doubleValue()));
                }
                return this;
            }
        };

        table.setDefaultRenderer(BigDecimal.class, currencyRenderer);
        table.setDefaultRenderer(Double.class, currencyRenderer);
    }

    /**
     * Initialisation des listes deroulantes et chargement initial
     */
    private void dynInit()
    {
        loadPeriods();
        loadPaymentGroups();
        loadOrganizations();
        loadData();
    }

    private void loadPeriods()
    {
        comboPeriod.removeActionListener(this);
        comboPeriod.removeAllItems();

        String sql = "SELECT P_Period_ID, Name, StartDate, EndDate, PeriodNo, PeriodStatus FROM P_Period ORDER BY StartDate DESC";
        int openPeriodIndex = -1;
        int currentIndex = 0;

        try (PreparedStatement pstmt = DB.prepareStatement(sql, null);
             ResultSet rs = pstmt.executeQuery())
        {
            while (rs.next())
            {
                int id = rs.getInt("P_Period_ID");
                String name = rs.getString("Name");
                Date start = rs.getDate("StartDate");
                Date end = rs.getDate("EndDate");
                String status = rs.getString("PeriodStatus");
                String label = (name != null ? name : "P\u00e9riode " + rs.getInt("PeriodNo"));
                if (start != null && end != null)
                {
                    label += " (" + DATE_FMT.format(start) + " au " + DATE_FMT.format(end) + ")";
                }
                comboPeriod.addItem(new KeyNamePair(id, label));

                if (openPeriodIndex == -1 && status != null && "O".equalsIgnoreCase(status.trim()))
                {
                    openPeriodIndex = currentIndex;
                }
                currentIndex++;
            }
        }
        catch (Exception e)
        {
            s_log.log(Level.SEVERE, "loadPeriods", e);
        }

        if (openPeriodIndex >= 0 && openPeriodIndex < comboPeriod.getItemCount())
        {
            comboPeriod.setSelectedIndex(openPeriodIndex);
        }
        else if (comboPeriod.getItemCount() > 0)
        {
            comboPeriod.setSelectedIndex(0);
        }

        comboPeriod.addActionListener(this);
    }

    private void loadPaymentGroups()
    {
        comboPaymentGroup.removeActionListener(this);
        comboPaymentGroup.removeAllItems();
        comboPaymentGroup.addItem(new KeyNamePair(0, "Tous les groupes de paiement"));

        String sql = "SELECT P_Payment_Group_ID, Name FROM P_Payment_Group WHERE IsActive = 'Y' ORDER BY Name";
        try (PreparedStatement pstmt = DB.prepareStatement(sql, null);
             ResultSet rs = pstmt.executeQuery())
        {
            while (rs.next())
            {
                comboPaymentGroup.addItem(new KeyNamePair(rs.getInt("P_Payment_Group_ID"), rs.getString("Name")));
            }
        }
        catch (Exception e)
        {
            s_log.log(Level.SEVERE, "loadPaymentGroups", e);
        }
        comboPaymentGroup.addActionListener(this);
    }

    private void loadOrganizations()
    {
        comboOrg.removeActionListener(this);
        comboOrg.removeAllItems();
        comboOrg.addItem(new KeyNamePair(0, "Toutes les organisations"));

        int clientId = Env.getAD_Client_ID(m_ctx);
        String sql = "SELECT AD_Org_ID, Value, Name FROM AD_Org WHERE IsActive = 'Y' AND (AD_Client_ID = ? OR AD_Client_ID = 0) ORDER BY Value, Name";
        try (PreparedStatement pstmt = DB.prepareStatement(sql, null))
        {
            pstmt.setInt(1, clientId);
            try (ResultSet rs = pstmt.executeQuery())
            {
                while (rs.next())
                {
                    int id = rs.getInt("AD_Org_ID");
                    String name = rs.getString("Name");
                    String val = rs.getString("Value");
                    String label = (val != null && !val.trim().isEmpty() ? val + " - " : "") + name;
                    comboOrg.addItem(new KeyNamePair(id, label));
                }
            }
        }
        catch (Exception e)
        {
            s_log.log(Level.SEVERE, "loadOrganizations", e);
        }
        comboOrg.addActionListener(this);
    }

    /**
     * Chargement de l'ensemble des donnees pour la periode et filtres actifs
     */
    public void loadData()
    {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try
        {
            int periodId = getSelectedPeriodId();
            if (periodId <= 0)
            {
                m_masterRows.clear();
                masterModel.fireTableDataChanged();
                clearPaymentDetails();
                clearSummaryData();
                updateKpis();
                return;
            }

            int paymentGroupId = getSelectedPaymentGroupId();
            int orgId = getSelectedOrgId();

            // 1. Chargement de la liste maitresse des paiements
            m_masterRows.clear();

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ")
               .append("  p.P_Payment_ID, p.P_Employee_ID, emp.Value AS Emp_Value, emp.Name AS Emp_Name, emp.FirstName AS Emp_FirstName, ")
               .append("  p.P_Payment_Group_ID, COALESCE(pg.Name, '-') AS Group_Name, ")
               .append("  p.AD_Org_ID, COALESCE(org.Name, '-') AS Org_Name, ")
               .append("  p.PayDate, COALESCE(p.PrePrintedNo, '') AS PrePrintedNo, COALESCE(p.PaymentTypeDoc, '') AS PaymentTypeDoc, ")
               .append("  COALESCE(p.GrossEarnings, 0) AS GrossEarnings, ")
               .append("  COALESCE(p.TotalTaxableBenefit, 0) AS TotalTaxableBenefit, ")
               .append("  COALESCE(p.TotalDeduction, 0) AS TotalDeduction, ")
               .append("  COALESCE(p.NetPay, 0) AS NetPay, ")
               .append("  COALESCE(g_tot.TotalHours, 0) AS TotalHours, ")
               .append("  COALESCE(d_tot.TotalEmployerPart, 0) AS TotalEmployerPart ")
               .append("FROM P_Payment p ")
               .append("INNER JOIN P_Employee emp ON (p.P_Employee_ID = emp.P_Employee_ID) ")
               .append("LEFT OUTER JOIN P_Payment_Group pg ON (p.P_Payment_Group_ID = pg.P_Payment_Group_ID) ")
               .append("LEFT OUTER JOIN AD_Org org ON (p.AD_Org_ID = org.AD_Org_ID) ")
               .append("LEFT OUTER JOIN ( ")
               .append("   SELECT P_Payment_ID, SUM(QuantityCalc) AS TotalHours FROM P_Payment_Gain GROUP BY P_Payment_ID ")
               .append(") g_tot ON (p.P_Payment_ID = g_tot.P_Payment_ID) ")
               .append("LEFT OUTER JOIN ( ")
               .append("   SELECT P_Payment_ID, SUM(Employer_Part) AS TotalEmployerPart FROM P_Payment_Deduction GROUP BY P_Payment_ID ")
               .append(") d_tot ON (p.P_Payment_ID = d_tot.P_Payment_ID) ")
               .append("WHERE p.P_Period_ID = ? ")
               .append("  AND (p.IsCancelled IS NULL OR p.IsCancelled = 'N') ");

            if (paymentGroupId > 0)
                sql.append(" AND p.P_Payment_Group_ID = ? ");
            if (orgId > 0)
                sql.append(" AND p.AD_Org_ID = ? ");

            sql.append("ORDER BY emp.Value, emp.Name, emp.FirstName");

            try (PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null))
            {
                int pIndex = 1;
                pstmt.setInt(pIndex++, periodId);
                if (paymentGroupId > 0)
                    pstmt.setInt(pIndex++, paymentGroupId);
                if (orgId > 0)
                    pstmt.setInt(pIndex++, orgId);

                try (ResultSet rs = pstmt.executeQuery())
                {
                    while (rs.next())
                    {
                        PaymentMasterRow row = new PaymentMasterRow();
                        row.paymentId = rs.getInt("P_Payment_ID");
                        row.employeeId = rs.getInt("P_Employee_ID");
                        row.empValue = rs.getString("Emp_Value");
                        String name = rs.getString("Emp_Name");
                        String fName = rs.getString("Emp_FirstName");
                        row.empFullName = (name != null ? name : "") + (fName != null && !fName.trim().isEmpty() ? ", " + fName : "");
                        row.groupName = rs.getString("Group_Name");
                        row.orgName = rs.getString("Org_Name");
                        row.payDate = rs.getDate("PayDate");
                        row.prePrintedNo = rs.getString("PrePrintedNo");
                        row.paymentTypeDoc = rs.getString("PaymentTypeDoc");

                        row.hours = rs.getBigDecimal("TotalHours");
                        row.grossEarnings = rs.getBigDecimal("GrossEarnings");
                        row.taxableBenefit = rs.getBigDecimal("TotalTaxableBenefit");
                        row.totalDeductions = rs.getBigDecimal("TotalDeduction");
                        row.netPay = rs.getBigDecimal("NetPay");
                        row.employerPart = rs.getBigDecimal("TotalEmployerPart");
                        row.employerCost = row.grossEarnings.add(row.employerPart);

                        m_masterRows.add(row);
                    }
                }
            }
            masterModel.fireTableDataChanged();

            // 2. Chargement des donnees de recapitulation
            loadSummaryData(periodId, paymentGroupId, orgId);

            // 3. Selection automatique de la premiere ligne si disponible
            if (!m_masterRows.isEmpty())
            {
                tblMaster.setRowSelectionInterval(0, 0);
            }
            else
            {
                clearPaymentDetails();
            }

            // 4. Mise a jour des indicateurs KPIs
            updateKpis();
        }
        catch (Exception e)
        {
            s_log.log(Level.SEVERE, "loadData", e);
        }
        finally
        {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    /**
     * Chargement du detail des gains, retenues et avantages pour un paiement precis
     */
    private void loadPaymentDetails(int paymentId)
    {
        if (paymentId <= 0)
        {
            clearPaymentDetails();
            return;
        }

        // 1. Gains
        m_gainRows.clear();
        String sqlGains = "SELECT g.Value AS Gain_Code, g.Name AS Gain_Name, "
            + " COALESCE(pg.QuantityCalc, 0) AS Hours, COALESCE(pg.Hourly_Rate, 0) AS Rate, COALESCE(pg.AmountCalc, 0) AS Amount "
            + " FROM P_Payment_Gain pg "
            + " INNER JOIN P_Gain g ON (pg.P_Gain_ID = g.P_Gain_ID) "
            + " WHERE pg.P_Payment_ID = ? ORDER BY g.Value, g.Name";
        try (PreparedStatement pstmt = DB.prepareStatement(sqlGains, null))
        {
            pstmt.setInt(1, paymentId);
            try (ResultSet rs = pstmt.executeQuery())
            {
                while (rs.next())
                {
                    GainRow r = new GainRow();
                    r.code = rs.getString("Gain_Code");
                    r.name = rs.getString("Gain_Name");
                    r.hours = rs.getBigDecimal("Hours");
                    r.rate = rs.getBigDecimal("Rate");
                    r.amount = rs.getBigDecimal("Amount");
                    m_gainRows.add(r);
                }
            }
        }
        catch (Exception e)
        {
            s_log.log(Level.SEVERE, "loadPaymentDetails(Gains)", e);
        }
        gainsModel.fireTableDataChanged();

        // 2. Deductions
        m_deductionRows.clear();
        String sqlDeductions = "SELECT d.Value AS Deduction_Code, d.Name AS Deduction_Name, "
            + " COALESCE(pd.Salary_Eligible, 0) AS Salary_Eligible, "
            + " COALESCE(pd.Employee_Part, 0) AS Employee_Part, "
            + " COALESCE(pd.Employer_Part, 0) AS Employer_Part "
            + " FROM P_Payment_Deduction pd "
            + " INNER JOIN P_Deduction d ON (pd.P_Deduction_ID = d.P_Deduction_ID) "
            + " WHERE pd.P_Payment_ID = ? ORDER BY d.Value, d.Name";
        try (PreparedStatement pstmt = DB.prepareStatement(sqlDeductions, null))
        {
            pstmt.setInt(1, paymentId);
            try (ResultSet rs = pstmt.executeQuery())
            {
                while (rs.next())
                {
                    DeductionRow r = new DeductionRow();
                    r.code = rs.getString("Deduction_Code");
                    r.name = rs.getString("Deduction_Name");
                    r.salaryEligible = rs.getBigDecimal("Salary_Eligible");
                    r.employeePart = rs.getBigDecimal("Employee_Part");
                    r.employerPart = rs.getBigDecimal("Employer_Part");
                    r.total = r.employeePart.add(r.employerPart);
                    m_deductionRows.add(r);
                }
            }
        }
        catch (Exception e)
        {
            s_log.log(Level.SEVERE, "loadPaymentDetails(Deductions)", e);
        }
        deductionsModel.fireTableDataChanged();

        // 3. Avantages Imposables
        m_benefitRows.clear();
        String sqlBenefits = "SELECT tb.Value AS Benefit_Code, tb.Name AS Benefit_Name, "
            + " COALESCE(ptb.Taxable_Benefit_Amount, 0) AS Amount "
            + " FROM P_Payment_Taxable_Benefit ptb "
            + " INNER JOIN P_Taxable_Benefit tb ON (ptb.P_Taxable_Benefit_ID = tb.P_Taxable_Benefit_ID) "
            + " WHERE ptb.P_Payment_ID = ? ORDER BY tb.Value, tb.Name";
        try (PreparedStatement pstmt = DB.prepareStatement(sqlBenefits, null))
        {
            pstmt.setInt(1, paymentId);
            try (ResultSet rs = pstmt.executeQuery())
            {
                while (rs.next())
                {
                    BenefitRow r = new BenefitRow();
                    r.code = rs.getString("Benefit_Code");
                    r.name = rs.getString("Benefit_Name");
                    r.amount = rs.getBigDecimal("Amount");
                    m_benefitRows.add(r);
                }
            }
        }
        catch (Exception e)
        {
            s_log.log(Level.SEVERE, "loadPaymentDetails(Benefits)", e);
        }
        benefitsModel.fireTableDataChanged();
    }

    private void clearPaymentDetails()
    {
        m_gainRows.clear();
        gainsModel.fireTableDataChanged();

        m_deductionRows.clear();
        deductionsModel.fireTableDataChanged();

        m_benefitRows.clear();
        benefitsModel.fireTableDataChanged();
    }

    /**
     * Chargement des 3 tables de recapitulation globale de la periode
     */
    private void loadSummaryData(int periodId, int paymentGroupId, int orgId)
    {
        clearSummaryData();

        String baseFilter = " FROM P_Payment_Gain pg "
            + " INNER JOIN P_Payment p ON (pg.P_Payment_ID = p.P_Payment_ID) "
            + " INNER JOIN P_Gain g ON (pg.P_Gain_ID = g.P_Gain_ID) "
            + " WHERE p.P_Period_ID = ? AND (p.IsCancelled IS NULL OR p.IsCancelled = 'N') ";
        if (paymentGroupId > 0) baseFilter += " AND p.P_Payment_Group_ID = " + paymentGroupId;
        if (orgId > 0) baseFilter += " AND p.AD_Org_ID = " + orgId;

        // Sommaire Gains
        String sqlG = "SELECT g.Value AS Code, g.Name AS Name, "
            + " SUM(COALESCE(pg.QuantityCalc, 0)) AS Total_Hours, SUM(COALESCE(pg.AmountCalc, 0)) AS Total_Amount "
            + baseFilter
            + " GROUP BY g.Value, g.Name ORDER BY g.Value, g.Name";
        try (PreparedStatement pstmt = DB.prepareStatement(sqlG, null))
        {
            pstmt.setInt(1, periodId);
            try (ResultSet rs = pstmt.executeQuery())
            {
                while (rs.next())
                {
                    SummaryItemRow r = new SummaryItemRow();
                    r.code = rs.getString("Code");
                    r.name = rs.getString("Name");
                    r.quantity = rs.getBigDecimal("Total_Hours");
                    r.amount = rs.getBigDecimal("Total_Amount");
                    m_sumGainsRows.add(r);
                }
            }
        }
        catch (Exception e)
        {
            s_log.log(Level.SEVERE, "loadSummaryData(Gains)", e);
        }
        sumGainsModel.fireTableDataChanged();

        // Sommaire Deductions
        String dedFilter = " FROM P_Payment_Deduction pd "
            + " INNER JOIN P_Payment p ON (pd.P_Payment_ID = p.P_Payment_ID) "
            + " INNER JOIN P_Deduction d ON (pd.P_Deduction_ID = d.P_Deduction_ID) "
            + " WHERE p.P_Period_ID = ? AND (p.IsCancelled IS NULL OR p.IsCancelled = 'N') ";
        if (paymentGroupId > 0) dedFilter += " AND p.P_Payment_Group_ID = " + paymentGroupId;
        if (orgId > 0) dedFilter += " AND p.AD_Org_ID = " + orgId;

        String sqlD = "SELECT d.Value AS Code, d.Name AS Name, "
            + " SUM(COALESCE(pd.Employee_Part, 0)) AS Emp_Part, SUM(COALESCE(pd.Employer_Part, 0)) AS Empl_Part "
            + dedFilter
            + " GROUP BY d.Value, d.Name ORDER BY d.Value, d.Name";
        try (PreparedStatement pstmt = DB.prepareStatement(sqlD, null))
        {
            pstmt.setInt(1, periodId);
            try (ResultSet rs = pstmt.executeQuery())
            {
                while (rs.next())
                {
                    SummaryDeductionRow r = new SummaryDeductionRow();
                    r.code = rs.getString("Code");
                    r.name = rs.getString("Name");
                    r.empPart = rs.getBigDecimal("Emp_Part");
                    r.emplPart = rs.getBigDecimal("Empl_Part");
                    r.total = r.empPart.add(r.emplPart);
                    m_sumDeductionRows.add(r);
                }
            }
        }
        catch (Exception e)
        {
            s_log.log(Level.SEVERE, "loadSummaryData(Deductions)", e);
        }
        sumDeductionsModel.fireTableDataChanged();

        // Sommaire Avantages
        String benFilter = " FROM P_Payment_Taxable_Benefit ptb "
            + " INNER JOIN P_Payment p ON (ptb.P_Payment_ID = p.P_Payment_ID) "
            + " INNER JOIN P_Taxable_Benefit tb ON (ptb.P_Taxable_Benefit_ID = tb.P_Taxable_Benefit_ID) "
            + " WHERE p.P_Period_ID = ? AND (p.IsCancelled IS NULL OR p.IsCancelled = 'N') ";
        if (paymentGroupId > 0) benFilter += " AND p.P_Payment_Group_ID = " + paymentGroupId;
        if (orgId > 0) benFilter += " AND p.AD_Org_ID = " + orgId;

        String sqlB = "SELECT tb.Value AS Code, tb.Name AS Name, "
            + " SUM(COALESCE(ptb.Taxable_Benefit_Amount, 0)) AS Total_Amount "
            + benFilter
            + " GROUP BY tb.Value, tb.Name ORDER BY tb.Value, tb.Name";
        try (PreparedStatement pstmt = DB.prepareStatement(sqlB, null))
        {
            pstmt.setInt(1, periodId);
            try (ResultSet rs = pstmt.executeQuery())
            {
                while (rs.next())
                {
                    SummaryItemRow r = new SummaryItemRow();
                    r.code = rs.getString("Code");
                    r.name = rs.getString("Name");
                    r.quantity = BigDecimal.ZERO;
                    r.amount = rs.getBigDecimal("Total_Amount");
                    m_sumBenefitRows.add(r);
                }
            }
        }
        catch (Exception e)
        {
            s_log.log(Level.SEVERE, "loadSummaryData(Benefits)", e);
        }
        sumBenefitsModel.fireTableDataChanged();
    }

    private void clearSummaryData()
    {
        m_sumGainsRows.clear();
        sumGainsModel.fireTableDataChanged();

        m_sumDeductionRows.clear();
        sumDeductionsModel.fireTableDataChanged();

        m_sumBenefitRows.clear();
        sumBenefitsModel.fireTableDataChanged();
    }

    /**
     * Calcul et rafraichissement des cartes KPIs et du balancement
     */
    private void updateKpis()
    {
        BigDecimal totHours = BigDecimal.ZERO;
        BigDecimal totGross = BigDecimal.ZERO;
        BigDecimal totTaxable = BigDecimal.ZERO;
        BigDecimal totDeductions = BigDecimal.ZERO;
        BigDecimal totNet = BigDecimal.ZERO;
        BigDecimal totEmployerPart = BigDecimal.ZERO;
        BigDecimal totEmployerCost = BigDecimal.ZERO;

        int rowCount = tblMaster.getRowCount();
        for (int i = 0; i < rowCount; i++)
        {
            int mRow = tblMaster.convertRowIndexToModel(i);
            PaymentMasterRow r = masterModel.getRow(mRow);
            if (r != null)
            {
                totHours = totHours.add(r.hours);
                totGross = totGross.add(r.grossEarnings);
                totTaxable = totTaxable.add(r.taxableBenefit);
                totDeductions = totDeductions.add(r.totalDeductions);
                totNet = totNet.add(r.netPay);
                totEmployerPart = totEmployerPart.add(r.employerPart);
                totEmployerCost = totEmployerCost.add(r.employerCost);
            }
        }

        lblKpiEmployees.setText(String.valueOf(rowCount));
        lblKpiHours.setText(HOURS_FMT.format(totHours) + " h");
        lblKpiGross.setText(CURRENCY_FMT.format(totGross));
        lblKpiTaxable.setText(CURRENCY_FMT.format(totTaxable));
        lblKpiDeductions.setText(CURRENCY_FMT.format(totDeductions));
        lblKpiNet.setText(CURRENCY_FMT.format(totNet));
        lblKpiEmployerCost.setText(CURRENCY_FMT.format(totEmployerCost));

        // Panneau de balancement
        lblBalGross.setText("Brut : " + CURRENCY_FMT.format(totGross));
        lblBalDeductions.setText("Retenues : " + CURRENCY_FMT.format(totDeductions));
        lblBalNet.setText("Net : " + CURRENCY_FMT.format(totNet));
        lblBalEmployerPart.setText("Cotis. Employeur : " + CURRENCY_FMT.format(totEmployerPart));
        lblBalTotalCost.setText("Co\u00fbt Total : " + CURRENCY_FMT.format(totEmployerCost));

        BigDecimal checkNet = totGross.subtract(totDeductions);
        BigDecimal diff = checkNet.subtract(totNet).abs();
        if (diff.compareTo(new BigDecimal("0.02")) <= 0)
        {
            lblBalStatus.setText("  PAIE BALANC\u00c9E  ");
            lblBalStatus.setBackground(new Color(212, 239, 223));
            lblBalStatus.setForeground(new Color(30, 126, 52));
            lblBalStatus.setBorder(BorderFactory.createLineBorder(new Color(46, 204, 113)));
        }
        else
        {
            lblBalStatus.setText("  \u00c9CART : " + CURRENCY_FMT.format(diff) + "  ");
            lblBalStatus.setBackground(new Color(249, 219, 216));
            lblBalStatus.setForeground(new Color(192, 57, 43));
            lblBalStatus.setBorder(BorderFactory.createLineBorder(new Color(231, 76, 60)));
        }
    }

    /**
     * Filtrage textuel de recherche
     */
    private void applySearchFilter()
    {
        String txt = txtSearch.getText();
        if (txt == null || txt.trim().isEmpty())
        {
            masterSorter.setRowFilter(null);
        }
        else
        {
            masterSorter.setRowFilter(RowFilter.regexFilter("(?i)" + txt.trim()));
        }
        updateKpis();
    }

    /**
     * Navigation vers la fiche du paiement selectionne
     */
    private void zoomToPayment()
    {
        if (m_selectedPaymentId > 0)
        {
            AEnv.zoom(solstice.model.P_Payment.Table_ID, m_selectedPaymentId);
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Veuillez s\u00e9lectionner un paiement dans la liste.", "S\u00e9lection requise", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Exportation compl\u00e8te vers Excel (.xlsx) natif avec Apache POI
     */
    public void exportToExcel()
    {
        int periodId = getSelectedPeriodId();
        if (periodId <= 0 || m_masterRows.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Aucune donn\u00e9e \u00e0 exporter pour la s\u00e9lection actuelle.", "Export Excel", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String periodName = getSelectedPeriodName().replaceAll("[^a-zA-Z0-9_-]", "_");
        String defaultFileName = "Registre_Paie_" + periodName + "_" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xlsx";

        File file = ExcelExportUtil.chooseFile(defaultFileName, this);
        if (file == null)
            return;

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try (XSSFWorkbook wb = new XSSFWorkbook())
        {
            XSSFDataFormat df = wb.createDataFormat();

            // Styles
            XSSFFont titleFont = wb.createFont();
            titleFont.setFontName("Calibri");
            titleFont.setFontHeightInPoints((short) 14);
            titleFont.setBold(true);
            titleFont.setColor(IndexedColors.DARK_BLUE.getIndex());

            XSSFCellStyle titleStyle = wb.createCellStyle();
            titleStyle.setFont(titleFont);

            XSSFFont headerFont = wb.createFont();
            headerFont.setFontName("Calibri");
            headerFont.setFontHeightInPoints((short) 10);
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());

            XSSFCellStyle headerStyle = wb.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            XSSFFont normalFont = wb.createFont();
            normalFont.setFontName("Calibri");
            normalFont.setFontHeightInPoints((short) 10);

            XSSFCellStyle textStyle = wb.createCellStyle();
            textStyle.setFont(normalFont);
            textStyle.setBorderTop(BorderStyle.THIN);
            textStyle.setBorderBottom(BorderStyle.THIN);
            textStyle.setBorderLeft(BorderStyle.THIN);
            textStyle.setBorderRight(BorderStyle.THIN);

            XSSFCellStyle currencyStyle = wb.createCellStyle();
            currencyStyle.cloneStyleFrom(textStyle);
            currencyStyle.setDataFormat(df.getFormat("#,##0.00 $"));

            XSSFCellStyle numberStyle = wb.createCellStyle();
            numberStyle.cloneStyleFrom(textStyle);
            numberStyle.setDataFormat(df.getFormat("#,##0.00"));

            XSSFFont totalFont = wb.createFont();
            totalFont.setFontName("Calibri");
            totalFont.setFontHeightInPoints((short) 10);
            totalFont.setBold(true);

            XSSFCellStyle totalStyle = wb.createCellStyle();
            totalStyle.setFont(totalFont);
            totalStyle.setDataFormat(df.getFormat("#,##0.00 $"));
            totalStyle.setBorderTop(BorderStyle.THIN);
            totalStyle.setBorderBottom(BorderStyle.DOUBLE);

            // ==========================================
            // FEUILLE 1 : Sommaire Employes
            // ==========================================
            XSSFSheet sheet1 = wb.createSheet("Sommaire Employ\u00e9s");
            writeSheetHeader(sheet1, titleStyle, "SOLSTICE+ - REGISTRE DE PAIE (SOMMAIRE)");

            String[] h1 = { "Matricule", "Nom & Pr\u00e9nom", "Groupe de paie", "Organisation", "Ch\u00e8que / D\u00e9p\u00f4t",
                            "Heures", "Brut ($)", "Avantages ($)", "Retenues ($)", "Net ($)", "Cotis. Empl. ($)", "Co\u00fbt Total ($)" };
            XSSFRow rHead1 = sheet1.createRow(3);
            for (int c = 0; c < h1.length; c++)
            {
                XSSFCell cell = rHead1.createCell(c);
                cell.setCellValue(h1[c]);
                cell.setCellStyle(headerStyle);
            }

            int rIdx1 = 4;
            for (PaymentMasterRow r : m_masterRows)
            {
                XSSFRow row = sheet1.createRow(rIdx1++);
                createCell(row, 0, r.empValue, textStyle);
                createCell(row, 1, r.empFullName, textStyle);
                createCell(row, 2, r.groupName, textStyle);
                createCell(row, 3, r.orgName, textStyle);
                createCell(row, 4, r.prePrintedNo, textStyle);
                createNumCell(row, 5, r.hours, numberStyle);
                createNumCell(row, 6, r.grossEarnings, currencyStyle);
                createNumCell(row, 7, r.taxableBenefit, currencyStyle);
                createNumCell(row, 8, r.totalDeductions, currencyStyle);
                createNumCell(row, 9, r.netPay, currencyStyle);
                createNumCell(row, 10, r.employerPart, currencyStyle);
                createNumCell(row, 11, r.employerCost, currencyStyle);
            }

            // Ligne de totaux
            XSSFRow rTot1 = sheet1.createRow(rIdx1);
            createCell(rTot1, 1, "TOTAL G\u00c9N\u00c9RAL (" + m_masterRows.size() + " employ\u00e9s)", totalStyle);
            createFormulaCell(rTot1, 5, "SUM(F5:F" + rIdx1 + ")", numberStyle);
            createFormulaCell(rTot1, 6, "SUM(G5:G" + rIdx1 + ")", totalStyle);
            createFormulaCell(rTot1, 7, "SUM(H5:H" + rIdx1 + ")", totalStyle);
            createFormulaCell(rTot1, 8, "SUM(I5:I" + rIdx1 + ")", totalStyle);
            createFormulaCell(rTot1, 9, "SUM(J5:J" + rIdx1 + ")", totalStyle);
            createFormulaCell(rTot1, 10, "SUM(K5:K" + rIdx1 + ")", totalStyle);
            createFormulaCell(rTot1, 11, "SUM(L5:L" + rIdx1 + ")", totalStyle);

            autoFitColumns(sheet1, h1.length);

            // ==========================================
            // FEUILLE 2 : Detail des Gains
            // ==========================================
            XSSFSheet sheet2 = wb.createSheet("D\u00e9tail Gains");
            writeSheetHeader(sheet2, titleStyle, "D\u00c9TAIL DE TOUS LES GAINS");

            String[] h2 = { "Matricule", "Employ\u00e9", "Code Gain", "Description du gain", "Heures / Qt\u00e9", "Taux Horaire ($)", "Montant Calcul\u00e9 ($)" };
            XSSFRow rHead2 = sheet2.createRow(3);
            for (int c = 0; c < h2.length; c++)
            {
                XSSFCell cell = rHead2.createCell(c);
                cell.setCellValue(h2[c]);
                cell.setCellStyle(headerStyle);
            }

            int rIdx2 = 4;
            String sqlAllGains = "SELECT emp.Value AS Emp_Value, emp.Name AS Emp_Name, emp.FirstName AS Emp_FirstName, "
                + " g.Value AS Gain_Code, g.Name AS Gain_Name, "
                + " COALESCE(pg.QuantityCalc, 0) AS Hours, COALESCE(pg.Hourly_Rate, 0) AS Rate, COALESCE(pg.AmountCalc, 0) AS Amount "
                + " FROM P_Payment_Gain pg "
                + " INNER JOIN P_Payment p ON (pg.P_Payment_ID = p.P_Payment_ID) "
                + " INNER JOIN P_Employee emp ON (p.P_Employee_ID = emp.P_Employee_ID) "
                + " INNER JOIN P_Gain g ON (pg.P_Gain_ID = g.P_Gain_ID) "
                + " WHERE p.P_Period_ID = ? AND (p.IsCancelled IS NULL OR p.IsCancelled = 'N') "
                + (getSelectedPaymentGroupId() > 0 ? " AND p.P_Payment_Group_ID = " + getSelectedPaymentGroupId() : "")
                + (getSelectedOrgId() > 0 ? " AND p.AD_Org_ID = " + getSelectedOrgId() : "")
                + " ORDER BY emp.Value, g.Value";

            try (PreparedStatement pstmt = DB.prepareStatement(sqlAllGains, null))
            {
                pstmt.setInt(1, periodId);
                try (ResultSet rs = pstmt.executeQuery())
                {
                    while (rs.next())
                    {
                        XSSFRow row = sheet2.createRow(rIdx2++);
                        createCell(row, 0, rs.getString("Emp_Value"), textStyle);
                        createCell(row, 1, rs.getString("Emp_Name") + ", " + rs.getString("Emp_FirstName"), textStyle);
                        createCell(row, 2, rs.getString("Gain_Code"), textStyle);
                        createCell(row, 3, rs.getString("Gain_Name"), textStyle);
                        createNumCell(row, 4, rs.getBigDecimal("Hours"), numberStyle);
                        createNumCell(row, 5, rs.getBigDecimal("Rate"), currencyStyle);
                        createNumCell(row, 6, rs.getBigDecimal("Amount"), currencyStyle);
                    }
                }
            }
            if (rIdx2 > 4)
            {
                XSSFRow rTot2 = sheet2.createRow(rIdx2);
                createCell(rTot2, 3, "TOTAL GAINS", totalStyle);
                createFormulaCell(rTot2, 4, "SUM(E5:E" + rIdx2 + ")", numberStyle);
                createFormulaCell(rTot2, 6, "SUM(G5:G" + rIdx2 + ")", totalStyle);
            }
            autoFitColumns(sheet2, h2.length);

            // ==========================================
            // FEUILLE 3 : Detail des Retenues
            // ==========================================
            XSSFSheet sheet3 = wb.createSheet("D\u00e9tail Retenues");
            writeSheetHeader(sheet3, titleStyle, "D\u00c9TAIL DE TOUTES LES RETENUES & COTISATIONS");

            String[] h3 = { "Matricule", "Employ\u00e9", "Code Retenue", "Description", "Salaire Admissible ($)", "Part Employ\u00e9 ($)", "Part Employeur ($)", "Total D\u00e9duction ($)" };
            XSSFRow rHead3 = sheet3.createRow(3);
            for (int c = 0; c < h3.length; c++)
            {
                XSSFCell cell = rHead3.createCell(c);
                cell.setCellValue(h3[c]);
                cell.setCellStyle(headerStyle);
            }

            int rIdx3 = 4;
            String sqlAllDed = "SELECT emp.Value AS Emp_Value, emp.Name AS Emp_Name, emp.FirstName AS Emp_FirstName, "
                + " d.Value AS Deduction_Code, d.Name AS Deduction_Name, "
                + " COALESCE(pd.Salary_Eligible, 0) AS Salary_Eligible, "
                + " COALESCE(pd.Employee_Part, 0) AS Employee_Part, "
                + " COALESCE(pd.Employer_Part, 0) AS Employer_Part "
                + " FROM P_Payment_Deduction pd "
                + " INNER JOIN P_Payment p ON (pd.P_Payment_ID = p.P_Payment_ID) "
                + " INNER JOIN P_Employee emp ON (p.P_Employee_ID = emp.P_Employee_ID) "
                + " INNER JOIN P_Deduction d ON (pd.P_Deduction_ID = d.P_Deduction_ID) "
                + " WHERE p.P_Period_ID = ? AND (p.IsCancelled IS NULL OR p.IsCancelled = 'N') "
                + (getSelectedPaymentGroupId() > 0 ? " AND p.P_Payment_Group_ID = " + getSelectedPaymentGroupId() : "")
                + (getSelectedOrgId() > 0 ? " AND p.AD_Org_ID = " + getSelectedOrgId() : "")
                + " ORDER BY emp.Value, d.Value";

            try (PreparedStatement pstmt = DB.prepareStatement(sqlAllDed, null))
            {
                pstmt.setInt(1, periodId);
                try (ResultSet rs = pstmt.executeQuery())
                {
                    while (rs.next())
                    {
                        XSSFRow row = sheet3.createRow(rIdx3++);
                        BigDecimal empP = rs.getBigDecimal("Employee_Part");
                        BigDecimal emplP = rs.getBigDecimal("Employer_Part");
                        createCell(row, 0, rs.getString("Emp_Value"), textStyle);
                        createCell(row, 1, rs.getString("Emp_Name") + ", " + rs.getString("Emp_FirstName"), textStyle);
                        createCell(row, 2, rs.getString("Deduction_Code"), textStyle);
                        createCell(row, 3, rs.getString("Deduction_Name"), textStyle);
                        createNumCell(row, 4, rs.getBigDecimal("Salary_Eligible"), currencyStyle);
                        createNumCell(row, 5, empP, currencyStyle);
                        createNumCell(row, 6, emplP, currencyStyle);
                        createNumCell(row, 7, empP.add(emplP), currencyStyle);
                    }
                }
            }
            if (rIdx3 > 4)
            {
                XSSFRow rTot3 = sheet3.createRow(rIdx3);
                createCell(rTot3, 3, "TOTAL RETENUES", totalStyle);
                createFormulaCell(rTot3, 5, "SUM(F5:F" + rIdx3 + ")", totalStyle);
                createFormulaCell(rTot3, 6, "SUM(G5:G" + rIdx3 + ")", totalStyle);
                createFormulaCell(rTot3, 7, "SUM(H5:H" + rIdx3 + ")", totalStyle);
            }
            autoFitColumns(sheet3, h3.length);

            // ==========================================
            // FEUILLE 4 : Avantages Imposables
            // ==========================================
            XSSFSheet sheet4 = wb.createSheet("Avantages Imposables");
            writeSheetHeader(sheet4, titleStyle, "D\u00c9TAIL DES AVANTAGES IMPOSABLES");

            String[] h4 = { "Matricule", "Employ\u00e9", "Code Avantage", "Description", "Montant Imposable ($)" };
            XSSFRow rHead4 = sheet4.createRow(3);
            for (int c = 0; c < h4.length; c++)
            {
                XSSFCell cell = rHead4.createCell(c);
                cell.setCellValue(h4[c]);
                cell.setCellStyle(headerStyle);
            }

            int rIdx4 = 4;
            String sqlAllBen = "SELECT emp.Value AS Emp_Value, emp.Name AS Emp_Name, emp.FirstName AS Emp_FirstName, "
                + " tb.Value AS Benefit_Code, tb.Name AS Benefit_Name, "
                + " COALESCE(ptb.Taxable_Benefit_Amount, 0) AS Amount "
                + " FROM P_Payment_Taxable_Benefit ptb "
                + " INNER JOIN P_Payment p ON (ptb.P_Payment_ID = p.P_Payment_ID) "
                + " INNER JOIN P_Employee emp ON (p.P_Employee_ID = emp.P_Employee_ID) "
                + " INNER JOIN P_Taxable_Benefit tb ON (ptb.P_Taxable_Benefit_ID = tb.P_Taxable_Benefit_ID) "
                + " WHERE p.P_Period_ID = ? AND (p.IsCancelled IS NULL OR p.IsCancelled = 'N') "
                + (getSelectedPaymentGroupId() > 0 ? " AND p.P_Payment_Group_ID = " + getSelectedPaymentGroupId() : "")
                + (getSelectedOrgId() > 0 ? " AND p.AD_Org_ID = " + getSelectedOrgId() : "")
                + " ORDER BY emp.Value, tb.Value";

            try (PreparedStatement pstmt = DB.prepareStatement(sqlAllBen, null))
            {
                pstmt.setInt(1, periodId);
                try (ResultSet rs = pstmt.executeQuery())
                {
                    while (rs.next())
                    {
                        XSSFRow row = sheet4.createRow(rIdx4++);
                        createCell(row, 0, rs.getString("Emp_Value"), textStyle);
                        createCell(row, 1, rs.getString("Emp_Name") + ", " + rs.getString("Emp_FirstName"), textStyle);
                        createCell(row, 2, rs.getString("Benefit_Code"), textStyle);
                        createCell(row, 3, rs.getString("Benefit_Name"), textStyle);
                        createNumCell(row, 4, rs.getBigDecimal("Amount"), currencyStyle);
                    }
                }
            }
            if (rIdx4 > 4)
            {
                XSSFRow rTot4 = sheet4.createRow(rIdx4);
                createCell(rTot4, 3, "TOTAL AVANTAGES", totalStyle);
                createFormulaCell(rTot4, 4, "SUM(E5:E" + rIdx4 + ")", totalStyle);
            }
            autoFitColumns(sheet4, h4.length);

            // ==========================================
            // FEUILLE 5 : Recapitulation Globale
            // ==========================================
            XSSFSheet sheet5 = wb.createSheet("R\u00e9capitulation P\u00e9riode");
            writeSheetHeader(sheet5, titleStyle, "R\u00c9CAPITULATION GLOBALE & BALANCEMENT DE PAIE");

            // Sommaire Gains
            int rIdx5 = 3;
            XSSFRow rSumGHeader = sheet5.createRow(rIdx5++);
            createCell(rSumGHeader, 0, "SOMMAIRE DES GAINS", headerStyle);
            createCell(rSumGHeader, 1, "", headerStyle);
            createCell(rSumGHeader, 2, "Heures", headerStyle);
            createCell(rSumGHeader, 3, "Montant ($)", headerStyle);

            for (SummaryItemRow r : m_sumGainsRows)
            {
                XSSFRow row = sheet5.createRow(rIdx5++);
                createCell(row, 0, r.code, textStyle);
                createCell(row, 1, r.name, textStyle);
                createNumCell(row, 2, r.quantity, numberStyle);
                createNumCell(row, 3, r.amount, currencyStyle);
            }

            // Sommaire Deductions
            rIdx5++;
            XSSFRow rSumDHeader = sheet5.createRow(rIdx5++);
            createCell(rSumDHeader, 0, "SOMMAIRE DES RETENUES", headerStyle);
            createCell(rSumDHeader, 1, "", headerStyle);
            createCell(rSumDHeader, 2, "Part Emp. ($)", headerStyle);
            createCell(rSumDHeader, 3, "Part Empl. ($)", headerStyle);
            createCell(rSumDHeader, 4, "Total ($)", headerStyle);

            for (SummaryDeductionRow r : m_sumDeductionRows)
            {
                XSSFRow row = sheet5.createRow(rIdx5++);
                createCell(row, 0, r.code, textStyle);
                createCell(row, 1, r.name, textStyle);
                createNumCell(row, 2, r.empPart, currencyStyle);
                createNumCell(row, 3, r.emplPart, currencyStyle);
                createNumCell(row, 4, r.total, currencyStyle);
            }

            // Sommaire Avantages
            rIdx5++;
            XSSFRow rSumBHeader = sheet5.createRow(rIdx5++);
            createCell(rSumBHeader, 0, "SOMMAIRE DES AVANTAGES IMPOSABLES", headerStyle);
            createCell(rSumBHeader, 1, "", headerStyle);
            createCell(rSumBHeader, 2, "Montant ($)", headerStyle);

            for (SummaryItemRow r : m_sumBenefitRows)
            {
                XSSFRow row = sheet5.createRow(rIdx5++);
                createCell(row, 0, r.code, textStyle);
                createCell(row, 1, r.name, textStyle);
                createNumCell(row, 2, r.amount, currencyStyle);
            }

            autoFitColumns(sheet5, 5);

            // Ecriture du fichier
            try (FileOutputStream fos = new FileOutputStream(file))
            {
                wb.write(fos);
            }

            ExcelExportUtil.postExportSuccess(file, this);
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

    private void writeSheetHeader(XSSFSheet sheet, XSSFCellStyle titleStyle, String title)
    {
        XSSFRow r0 = sheet.createRow(0);
        XSSFCell c0 = r0.createCell(0);
        c0.setCellValue(title);
        c0.setCellStyle(titleStyle);

        XSSFRow r1 = sheet.createRow(1);
        r1.createCell(0).setCellValue("P\u00e9riode : " + getSelectedPeriodName()
            + "  |  Groupe : " + getSelectedPaymentGroupName()
            + "  |  Organisation : " + getSelectedOrgName()
            + "  |  Date extraction : " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
    }

    private void createCell(XSSFRow row, int col, String val, XSSFCellStyle style)
    {
        XSSFCell cell = row.createCell(col);
        cell.setCellValue(val != null ? val : "");
        cell.setCellStyle(style);
    }

    private void createNumCell(XSSFRow row, int col, BigDecimal val, XSSFCellStyle style)
    {
        XSSFCell cell = row.createCell(col);
        cell.setCellValue(val != null ? val.doubleValue() : 0.0);
        cell.setCellStyle(style);
    }

    private void createFormulaCell(XSSFRow row, int col, String formula, XSSFCellStyle style)
    {
        XSSFCell cell = row.createCell(col);
        cell.setCellFormula(formula);
        cell.setCellStyle(style);
    }

    private void autoFitColumns(XSSFSheet sheet, int colCount)
    {
        for (int c = 0; c < colCount; c++)
        {
            sheet.autoSizeColumn(c);
            int width = sheet.getColumnWidth(c);
            sheet.setColumnWidth(c, Math.min(256 * 60, width + 1200));
        }
    }

    private int getSelectedPeriodId()
    {
        KeyNamePair p = (KeyNamePair) comboPeriod.getSelectedItem();
        return p != null ? p.getKey() : 0;
    }

    private String getSelectedPeriodName()
    {
        KeyNamePair p = (KeyNamePair) comboPeriod.getSelectedItem();
        return p != null ? p.getName() : "";
    }

    private int getSelectedPaymentGroupId()
    {
        KeyNamePair p = (KeyNamePair) comboPaymentGroup.getSelectedItem();
        return p != null ? p.getKey() : 0;
    }

    private String getSelectedPaymentGroupName()
    {
        KeyNamePair p = (KeyNamePair) comboPaymentGroup.getSelectedItem();
        return p != null ? p.getName() : "Tous";
    }

    private int getSelectedOrgId()
    {
        KeyNamePair p = (KeyNamePair) comboOrg.getSelectedItem();
        return p != null ? p.getKey() : 0;
    }

    private String getSelectedOrgName()
    {
        KeyNamePair p = (KeyNamePair) comboOrg.getSelectedItem();
        return p != null ? p.getName() : "Toutes";
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();
        if (src == btnRefresh || src == comboPeriod || src == comboPaymentGroup || src == comboOrg)
        {
            loadData();
        }
        else if (src == btnExportExcel)
        {
            exportToExcel();
        }
        else if (src == btnZoom)
        {
            zoomToPayment();
        }
    }

    /**
     * Point d'entree pour ouverture de test
     */
    public static void open()
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run()
            {
                JFrame frame = new JFrame("Registre de paie");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                VPaymentRegister panel = new VPaymentRegister();
                FormFrame ff = new FormFrame();
                panel.init(0, ff);
                frame.getContentPane().add(panel, BorderLayout.CENTER);
                frame.pack();
                frame.setSize(1260, 840);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    // =========================================================================
    // Modeles de tables et structures de donnees
    // =========================================================================

    public static class PaymentMasterRow
    {
        public int paymentId;
        public int employeeId;
        public String empValue;
        public String empFullName;
        public String groupName;
        public String orgName;
        public Date payDate;
        public String prePrintedNo;
        public String paymentTypeDoc;

        public BigDecimal hours = BigDecimal.ZERO;
        public BigDecimal grossEarnings = BigDecimal.ZERO;
        public BigDecimal taxableBenefit = BigDecimal.ZERO;
        public BigDecimal totalDeductions = BigDecimal.ZERO;
        public BigDecimal netPay = BigDecimal.ZERO;
        public BigDecimal employerPart = BigDecimal.ZERO;
        public BigDecimal employerCost = BigDecimal.ZERO;
    }

    private class MasterPaymentTableModel extends AbstractTableModel
    {
        private static final long serialVersionUID = 1L;
        private final String[] COLUMNS = {
            "Matricule", "Nom & Pr\u00e9nom", "Groupe", "Org.", "Ch\u00e8que / D\u00e9p\u00f4t",
            "Heures", "Brut ($)", "Av. Impos. ($)", "Retenues ($)", "Net Pay\u00e9 ($)", "Cotis. Empl. ($)", "Co\u00fbt Total ($)"
        };

        @Override public int getRowCount() { return m_masterRows.size(); }
        @Override public int getColumnCount() { return COLUMNS.length; }
        @Override public String getColumnName(int c) { return COLUMNS[c]; }

        @Override
        public Class<?> getColumnClass(int c)
        {
            if (c >= 5) return BigDecimal.class;
            return String.class;
        }

        public PaymentMasterRow getRow(int r)
        {
            if (r >= 0 && r < m_masterRows.size()) return m_masterRows.get(r);
            return null;
        }

        @Override
        public Object getValueAt(int r, int c)
        {
            PaymentMasterRow row = m_masterRows.get(r);
            switch (c)
            {
                case 0: return row.empValue;
                case 1: return row.empFullName;
                case 2: return row.groupName;
                case 3: return row.orgName;
                case 4: return row.prePrintedNo;
                case 5: return row.hours;
                case 6: return row.grossEarnings;
                case 7: return row.taxableBenefit;
                case 8: return row.totalDeductions;
                case 9: return row.netPay;
                case 10: return row.employerPart;
                case 11: return row.employerCost;
                default: return null;
            }
        }
    }

    public static class GainRow
    {
        public String code;
        public String name;
        public BigDecimal hours = BigDecimal.ZERO;
        public BigDecimal rate = BigDecimal.ZERO;
        public BigDecimal amount = BigDecimal.ZERO;
    }

    private class GainsTableModel extends AbstractTableModel
    {
        private static final long serialVersionUID = 1L;
        private final String[] COLS = { "Code", "Description du Gain", "Heures / Quantit\u00e9", "Taux Horaire ($)", "Montant Calcul\u00e9 ($)" };

        @Override public int getRowCount() { return m_gainRows.size(); }
        @Override public int getColumnCount() { return COLS.length; }
        @Override public String getColumnName(int c) { return COLS[c]; }

        @Override
        public Class<?> getColumnClass(int c)
        {
            if (c >= 2) return BigDecimal.class;
            return String.class;
        }

        @Override
        public Object getValueAt(int r, int c)
        {
            GainRow row = m_gainRows.get(r);
            switch (c)
            {
                case 0: return row.code;
                case 1: return row.name;
                case 2: return row.hours;
                case 3: return row.rate;
                case 4: return row.amount;
                default: return null;
            }
        }
    }

    public static class DeductionRow
    {
        public String code;
        public String name;
        public BigDecimal salaryEligible = BigDecimal.ZERO;
        public BigDecimal employeePart = BigDecimal.ZERO;
        public BigDecimal employerPart = BigDecimal.ZERO;
        public BigDecimal total = BigDecimal.ZERO;
    }

    private class DeductionsTableModel extends AbstractTableModel
    {
        private static final long serialVersionUID = 1L;
        private final String[] COLS = { "Code", "Description de la Retenue", "Salaire Admissible ($)", "Part Employ\u00e9 ($)", "Part Employeur ($)", "Total Retenue ($)" };

        @Override public int getRowCount() { return m_deductionRows.size(); }
        @Override public int getColumnCount() { return COLS.length; }
        @Override public String getColumnName(int c) { return COLS[c]; }

        @Override
        public Class<?> getColumnClass(int c)
        {
            if (c >= 2) return BigDecimal.class;
            return String.class;
        }

        @Override
        public Object getValueAt(int r, int c)
        {
            DeductionRow row = m_deductionRows.get(r);
            switch (c)
            {
                case 0: return row.code;
                case 1: return row.name;
                case 2: return row.salaryEligible;
                case 3: return row.employeePart;
                case 4: return row.employerPart;
                case 5: return row.total;
                default: return null;
            }
        }
    }

    public static class BenefitRow
    {
        public String code;
        public String name;
        public BigDecimal amount = BigDecimal.ZERO;
    }

    private class BenefitsTableModel extends AbstractTableModel
    {
        private static final long serialVersionUID = 1L;
        private final String[] COLS = { "Code", "Description de l'Avantage Imposable", "Montant Imposable ($)" };

        @Override public int getRowCount() { return m_benefitRows.size(); }
        @Override public int getColumnCount() { return COLS.length; }
        @Override public String getColumnName(int c) { return COLS[c]; }

        @Override
        public Class<?> getColumnClass(int c)
        {
            if (c == 2) return BigDecimal.class;
            return String.class;
        }

        @Override
        public Object getValueAt(int r, int c)
        {
            BenefitRow row = m_benefitRows.get(r);
            switch (c)
            {
                case 0: return row.code;
                case 1: return row.name;
                case 2: return row.amount;
                default: return null;
            }
        }
    }

    public static class SummaryItemRow
    {
        public String code;
        public String name;
        public BigDecimal quantity = BigDecimal.ZERO;
        public BigDecimal amount = BigDecimal.ZERO;
    }

    private class SummaryGainsTableModel extends AbstractTableModel
    {
        private static final long serialVersionUID = 1L;
        private final String[] COLS = { "Code", "Nom du Gain", "Total Heures", "Total Montant ($)" };

        @Override public int getRowCount() { return m_sumGainsRows.size(); }
        @Override public int getColumnCount() { return COLS.length; }
        @Override public String getColumnName(int c) { return COLS[c]; }

        @Override
        public Class<?> getColumnClass(int c)
        {
            if (c >= 2) return BigDecimal.class;
            return String.class;
        }

        @Override
        public Object getValueAt(int r, int c)
        {
            SummaryItemRow row = m_sumGainsRows.get(r);
            switch (c)
            {
                case 0: return row.code;
                case 1: return row.name;
                case 2: return row.quantity;
                case 3: return row.amount;
                default: return null;
            }
        }
    }

    public static class SummaryDeductionRow
    {
        public String code;
        public String name;
        public BigDecimal empPart = BigDecimal.ZERO;
        public BigDecimal emplPart = BigDecimal.ZERO;
        public BigDecimal total = BigDecimal.ZERO;
    }

    private class SummaryDeductionsTableModel extends AbstractTableModel
    {
        private static final long serialVersionUID = 1L;
        private final String[] COLS = { "Code", "Nom de la Retenue", "Part Employ\u00e9 ($)", "Part Employeur ($)", "Total Retenue ($)" };

        @Override public int getRowCount() { return m_sumDeductionRows.size(); }
        @Override public int getColumnCount() { return COLS.length; }
        @Override public String getColumnName(int c) { return COLS[c]; }

        @Override
        public Class<?> getColumnClass(int c)
        {
            if (c >= 2) return BigDecimal.class;
            return String.class;
        }

        @Override
        public Object getValueAt(int r, int c)
        {
            SummaryDeductionRow row = m_sumDeductionRows.get(r);
            switch (c)
            {
                case 0: return row.code;
                case 1: return row.name;
                case 2: return row.empPart;
                case 3: return row.emplPart;
                case 4: return row.total;
                default: return null;
            }
        }
    }

    private class SummaryBenefitsTableModel extends AbstractTableModel
    {
        private static final long serialVersionUID = 1L;
        private final String[] COLS = { "Code", "Nom de l'Avantage", "Total Montant ($)" };

        @Override public int getRowCount() { return m_sumBenefitRows.size(); }
        @Override public int getColumnCount() { return COLS.length; }
        @Override public String getColumnName(int c) { return COLS[c]; }

        @Override
        public Class<?> getColumnClass(int c)
        {
            if (c == 2) return BigDecimal.class;
            return String.class;
        }

        @Override
        public Object getValueAt(int r, int c)
        {
            SummaryItemRow row = m_sumBenefitRows.get(r);
            switch (c)
            {
                case 0: return row.code;
                case 1: return row.name;
                case 2: return row.amount;
                default: return null;
            }
        }
    }
}
