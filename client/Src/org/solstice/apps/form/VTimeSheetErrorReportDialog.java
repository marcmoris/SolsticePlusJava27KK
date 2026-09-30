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
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

import org.compiere.apps.AEnv;
import org.compiere.apps.ConfirmPanel;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.swing.CDialog;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.solstice.util.ExcelExportUtil;
import solstice.model.P_Time_Sheet;

/**
 * Rapport d'ecran affichant les messages et erreurs de la table P_Time_Sheet_Error
 * suite a la generation du temps (TimeGeneration).
 * 
 * Permet la visualisation rapide, le filtrage par severite, la recherche textuelle,
 * le zoom vers la feuille de temps et l'export direct vers Excel (.xlsx).
 *
 * @author Solstice / Antigravity
 */
public class VTimeSheetErrorReportDialog extends CDialog implements FormPanel, ActionListener
{
    private static final long serialVersionUID = 1L;
    private static final CLogger s_log = CLogger.getCLogger(VTimeSheetErrorReportDialog.class);

    private Properties m_ctx;
    private int m_periodId = 0;
    private int m_employeeId = 0;
    private int m_timeSheetId = 0;
    private FormFrame m_formFrame = null;
    private int m_windowNo = 0;

    // UI - Header & Filters
    private JComboBox<KeyNamePair> comboPeriod;
    private JButton btnRefresh;
    private JLabel lblTotal;
    private JLabel lblErrors;
    private JLabel lblWarnings;
    private JLabel lblInfos;

    private JRadioButton rdoAll;
    private JRadioButton rdoError;
    private JRadioButton rdoWarning;
    private JRadioButton rdoInfo;
    private ButtonGroup filterGroup;

    private JTextField txtSearch;
    private JButton btnClearSearch;

    // UI - Table
    private JTable tableErrors;
    private TimeSheetErrorTableModel tableModel;
    private TableRowSorter<TimeSheetErrorTableModel> rowSorter;

    // UI - Commands
    private JButton btnZoom;
    private JButton btnExcel;
    private JButton btnCopy;
    private JButton btnPrint;
    private JButton btnClose;

    private JPanel mainContentPanel;

    /**
     * Constructeur pour ouverture en Formulaire Compiere (FormPanel)
     */
    public VTimeSheetErrorReportDialog()
    {
        super((Frame) null, "Rapport des messages et erreurs - Feuilles de temps", false);
    }

    /**
     * Constructeur modal/non-modal direct
     */
    public VTimeSheetErrorReportDialog(Frame parent, Properties ctx, int periodId, int employeeId, int timeSheetId)
    {
        super(parent, "Rapport des messages et erreurs - Feuilles de temps", false);
        this.m_ctx = (ctx != null) ? ctx : Env.getCtx();
        this.m_periodId = periodId;
        this.m_employeeId = employeeId;
        this.m_timeSheetId = timeSheetId;

        jbInit();
        loadPeriods();
        loadData(m_periodId, m_employeeId, m_timeSheetId);

        pack();
        setSize(1050, 640);
        setLocationRelativeTo(parent);
    }

    @Override
    public void init(int WindowNo, FormFrame frame)
    {
        this.m_windowNo = WindowNo;
        this.m_formFrame = frame;
        this.m_ctx = Env.getCtx();
        frame.setTitle("Rapport des messages et erreurs - Feuilles de temps");

        jbInit();
        frame.getContentPane().add(mainContentPanel, BorderLayout.CENTER);
        loadPeriods();
        loadData(m_periodId, 0, 0);
    }

    @Override
    public void dispose()
    {
        if (m_formFrame != null)
        {
            m_formFrame.dispose();
            m_formFrame = null;
        }
        super.dispose();
    }

    /**
     * Initialisation graphique des composants
     */
    private void jbInit()
    {
        mainContentPanel = new JPanel(new BorderLayout(5, 5));
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // -------------------------------------------------------------
        // Panneau superieur : Periode + Statistiques
        // -------------------------------------------------------------
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEtchedBorder(),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));

        JPanel periodPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
        JLabel lblPeriodTitle = new JLabel("P\u00e9riode de paie :");
        lblPeriodTitle.setFont(lblPeriodTitle.getFont().deriveFont(Font.BOLD));
        periodPanel.add(lblPeriodTitle);

        comboPeriod = new JComboBox<KeyNamePair>();
        comboPeriod.setPreferredSize(new Dimension(280, 24));
        comboPeriod.addActionListener(this);
        periodPanel.add(comboPeriod);

        btnRefresh = new JButton("Actualiser");
        btnRefresh.setIcon(Env.getImageIcon("Refresh16.gif"));
        btnRefresh.setToolTipText("Recharger les messages pour la p\u00e9riode");
        btnRefresh.addActionListener(this);
        periodPanel.add(btnRefresh);

        topPanel.add(periodPanel, BorderLayout.WEST);

        // Badges compteurs
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 2));
        lblTotal = new JLabel("Total : 0");
        lblTotal.setFont(lblTotal.getFont().deriveFont(Font.BOLD));

        lblErrors = new JLabel("\ud83d\udd34 0 Erreur(s)");
        lblErrors.setForeground(new Color(192, 0, 0));
        lblErrors.setFont(lblErrors.getFont().deriveFont(Font.BOLD));

        lblWarnings = new JLabel("\ud83d\udfe1 0 Avertissement(s)");
        lblWarnings.setForeground(new Color(180, 100, 0));
        lblWarnings.setFont(lblWarnings.getFont().deriveFont(Font.BOLD));

        lblInfos = new JLabel("\ud83d\udd35 0 Info(s)");
        lblInfos.setForeground(new Color(0, 102, 204));
        lblInfos.setFont(lblInfos.getFont().deriveFont(Font.BOLD));

        statsPanel.add(lblTotal);
        statsPanel.add(lblErrors);
        statsPanel.add(lblWarnings);
        statsPanel.add(lblInfos);
        topPanel.add(statsPanel, BorderLayout.EAST);

        // -------------------------------------------------------------
        // Panneau filtre & recherche rapide
        // -------------------------------------------------------------
        JPanel filterPanel = new JPanel(new BorderLayout(5, 5));
        filterPanel.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
        JLabel lblFilter = new JLabel("Filtrer par s\u00e9v\u00e9rit\u00e9 :");
        lblFilter.setFont(lblFilter.getFont().deriveFont(Font.BOLD));
        radioPanel.add(lblFilter);

        filterGroup = new ButtonGroup();
        rdoAll = new JRadioButton("Tous", true);
        rdoError = new JRadioButton("\ud83d\udd34 Erreurs");
        rdoWarning = new JRadioButton("\ud83d\udfe1 Avertissements");
        rdoInfo = new JRadioButton("\ud83d\udd35 Informations");

        filterGroup.add(rdoAll);
        filterGroup.add(rdoError);
        filterGroup.add(rdoWarning);
        filterGroup.add(rdoInfo);

        rdoAll.addActionListener(this);
        rdoError.addActionListener(this);
        rdoWarning.addActionListener(this);
        rdoInfo.addActionListener(this);

        radioPanel.add(rdoAll);
        radioPanel.add(rdoError);
        radioPanel.add(rdoWarning);
        radioPanel.add(rdoInfo);
        filterPanel.add(radioPanel, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 2));
        JLabel lblSearch = new JLabel("Recherche rapide :");
        searchPanel.add(lblSearch);

        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(200, 24));
        txtSearch.setToolTipText("Filtrer par nom, num\u00e9ro d'employ\u00e9, feuille ou texte du message");
        txtSearch.getDocument().addDocumentListener(new DocumentListener()
        {
            public void insertUpdate(DocumentEvent e) { applyFilter(); }
            public void removeUpdate(DocumentEvent e) { applyFilter(); }
            public void changedUpdate(DocumentEvent e) { applyFilter(); }
        });
        searchPanel.add(txtSearch);

        btnClearSearch = new JButton("\u2715");
        btnClearSearch.setToolTipText("Effacer la recherche");
        btnClearSearch.setPreferredSize(new Dimension(24, 24));
        btnClearSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txtSearch.setText("");
                applyFilter();
            }
        });
        searchPanel.add(btnClearSearch);
        filterPanel.add(searchPanel, BorderLayout.EAST);

        // Assemblage En-tete
        JPanel northContainer = new JPanel(new BorderLayout());
        northContainer.add(topPanel, BorderLayout.NORTH);
        northContainer.add(filterPanel, BorderLayout.SOUTH);
        mainContentPanel.add(northContainer, BorderLayout.NORTH);

        // -------------------------------------------------------------
        // Table des messages
        // -------------------------------------------------------------
        tableModel = new TimeSheetErrorTableModel();
        tableErrors = new JTable(tableModel);
        tableErrors.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableErrors.setRowHeight(22);
        tableErrors.setAutoCreateRowSorter(false); // géré par notre rowSorter

        rowSorter = new TableRowSorter<TimeSheetErrorTableModel>(tableModel);
        tableErrors.setRowSorter(rowSorter);

        // Tailles préférentielles des colonnes
        tableErrors.getColumnModel().getColumn(0).setPreferredWidth(125); // Sévérité
        tableErrors.getColumnModel().getColumn(1).setPreferredWidth(90);  // Code Emp
        tableErrors.getColumnModel().getColumn(2).setPreferredWidth(180); // Nom Emp
        tableErrors.getColumnModel().getColumn(3).setPreferredWidth(110); // Feuille
        tableErrors.getColumnModel().getColumn(4).setPreferredWidth(140); // Crédit / Banque
        tableErrors.getColumnModel().getColumn(5).setPreferredWidth(380); // Message
        tableErrors.getColumnModel().getColumn(6).setPreferredWidth(140); // Date / Heure

        // Rendu personnalisé des cellules (couleurs et zébrage)
        tableErrors.setDefaultRenderer(Object.class, new ErrorTableCellRenderer());

        // Double-clic : afficher détail et possibilité de zoomer
        tableErrors.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                if (e.getClickCount() == 2 && tableErrors.getSelectedRow() >= 0)
                {
                    showSelectedRowDetail();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableErrors);
        mainContentPanel.add(scrollPane, BorderLayout.CENTER);

        // -------------------------------------------------------------
        // Barre de boutons inférieure
        // -------------------------------------------------------------
        JPanel commandPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));

        btnZoom = new JButton("Ouvrir la feuille...");
        btnZoom.setIcon(Env.getImageIcon("Zoom16.gif"));
        btnZoom.setToolTipText("Ouvrir la feuille de temps dans Solstice");
        btnZoom.addActionListener(this);
        commandPanel.add(btnZoom);

        btnExcel = new JButton("Exporter vers Excel (.xlsx)");
        btnExcel.setIcon(Env.getImageIcon("Export16.gif"));
        btnExcel.setToolTipText("Exporter la liste actuelle des messages dans un fichier Excel natif XLSX");
        btnExcel.addActionListener(this);
        commandPanel.add(btnExcel);

        btnCopy = new JButton("Copier");
        btnCopy.setIcon(Env.getImageIcon("Copy16.gif"));
        btnCopy.setToolTipText("Copier le tableau dans le presse-papiers");
        btnCopy.addActionListener(this);
        commandPanel.add(btnCopy);

        btnPrint = new JButton("Imprimer...");
        btnPrint.setIcon(Env.getImageIcon("Print16.gif"));
        btnPrint.setToolTipText("Imprimer le tableau des messages");
        btnPrint.addActionListener(this);
        commandPanel.add(btnPrint);

        btnClose = new JButton("Fermer");
        btnClose.setIcon(Env.getImageIcon("Cancel16.gif"));
        btnClose.addActionListener(this);
        commandPanel.add(btnClose);

        mainContentPanel.add(commandPanel, BorderLayout.SOUTH);

        getContentPane().add(mainContentPanel);
    }

    /**
     * Charge les périodes de paie dans la liste déroulante
     */
    private void loadPeriods()
    {
        comboPeriod.removeActionListener(this);
        comboPeriod.removeAllItems();

        int clientId = Env.getAD_Client_ID(m_ctx);
        String sql = "SELECT P_Period_ID, Name, StartDate, EndDate "
            + "FROM P_Period "
            + "WHERE AD_Client_ID IN (0, " + clientId + ") "
            + "ORDER BY EndDate DESC";

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int selectIndex = -1;
        int currentIndex = 0;

        try
        {
            pstmt = DB.prepareStatement(sql, null);
            rs = pstmt.executeQuery();
            while (rs.next())
            {
                int pid = rs.getInt("P_Period_ID");
                String name = rs.getString("Name");
                comboPeriod.addItem(new KeyNamePair(pid, name));
                if (pid == m_periodId)
                {
                    selectIndex = currentIndex;
                }
                currentIndex++;
            }
        }
        catch (Exception e)
        {
            s_log.log(Level.SEVERE, "loadPeriods - " + sql, e);
        }
        finally
        {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (pstmt != null) pstmt.close(); } catch (Exception ignored) {}
        }

        if (selectIndex >= 0)
        {
            comboPeriod.setSelectedIndex(selectIndex);
        }
        else if (comboPeriod.getItemCount() > 0)
        {
            comboPeriod.setSelectedIndex(0);
            KeyNamePair knp = (KeyNamePair) comboPeriod.getSelectedItem();
            if (knp != null)
            {
                m_periodId = knp.getKey();
            }
        }

        comboPeriod.addActionListener(this);
    }

    /**
     * Charge les messages d'erreurs de la table P_Time_Sheet_Error
     */
    public void loadData(int periodId, int employeeId, int timeSheetId)
    {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        List<ErrorRow> rows = new ArrayList<ErrorRow>();

        String sql = "SELECT "
            + " err.P_Time_Sheet_Error_ID, "
            + " err.Alert_Severity_Level, "
            + " COALESCE(err.Alert_Message, adm.MsgText, '') AS Alert_Message, "
            + " emp.P_Employee_ID, "
            + " emp.Value AS Emp_Value, "
            + " emp.Name AS Emp_Name, "
            + " ts.P_Time_Sheet_ID, "
            + " ts.Value AS Time_Sheet_DocNo, "
            + " p.P_Period_ID, "
            + " p.Name AS Period_Name, "
            + " c.P_Credits_ID, "
            + " c.Value AS Credit_Value, "
            + " c.Name AS Credit_Name, "
            + " err.Created "
            + "FROM P_Time_Sheet_Error err "
            + "LEFT OUTER JOIN P_Time_Sheet ts ON (err.P_Time_Sheet_ID = ts.P_Time_Sheet_ID) "
            + "LEFT OUTER JOIN P_Employee emp ON (ts.P_Employee_ID = emp.P_Employee_ID) "
            + "LEFT OUTER JOIN P_Period p ON (ts.P_Period_ID = p.P_Period_ID) "
            + "LEFT OUTER JOIN P_Credits c ON (err.P_Credits_ID = c.P_Credits_ID) "
            + "LEFT OUTER JOIN AD_Message adm ON (err.AD_Message_ID = adm.AD_Message_ID) "
            + "WHERE 1=1 ";

        if (periodId > 0)
        {
            sql += " AND (ts.P_Period_ID = " + periodId 
                + " OR (ts.P_Period_ID IS NULL AND err.Created >= (SELECT StartDate FROM P_Period WHERE P_Period_ID = " + periodId + ")))";
        }
        if (employeeId > 0)
        {
            sql += " AND ts.P_Employee_ID = " + employeeId;
        }
        if (timeSheetId > 0)
        {
            sql += " AND ts.P_Time_Sheet_ID = " + timeSheetId;
        }

        sql += " ORDER BY CASE err.Alert_Severity_Level WHEN 'E' THEN 1 WHEN 'W' THEN 2 WHEN 'I' THEN 3 ELSE 4 END, emp.Value, err.Created DESC";

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        int errorCount = 0;
        int warningCount = 0;
        int infoCount = 0;

        try
        {
            pstmt = DB.prepareStatement(sql, null);
            rs = pstmt.executeQuery();
            while (rs.next())
            {
                ErrorRow r = new ErrorRow();
                r.errorId = rs.getInt("P_Time_Sheet_Error_ID");
                r.rawSeverity = rs.getString("Alert_Severity_Level");
                if (r.rawSeverity == null) r.rawSeverity = "I";

                if ("E".equalsIgnoreCase(r.rawSeverity))
                {
                    r.severityDisplay = "\ud83d\udd34 Erreur";
                    errorCount++;
                }
                else if ("W".equalsIgnoreCase(r.rawSeverity))
                {
                    r.severityDisplay = "\ud83d\udfe1 Avertissement";
                    warningCount++;
                }
                else if ("I".equalsIgnoreCase(r.rawSeverity))
                {
                    r.severityDisplay = "\ud83d\udd35 Information";
                    infoCount++;
                }
                else if ("C".equalsIgnoreCase(r.rawSeverity))
                {
                    r.severityDisplay = "Cha\u00eene";
                    infoCount++;
                }
                else
                {
                    r.severityDisplay = r.rawSeverity;
                    infoCount++;
                }

                r.employeeId = rs.getInt("P_Employee_ID");
                r.employeeValue = rs.getString("Emp_Value");
                r.employeeName = rs.getString("Emp_Name");

                r.timeSheetId = rs.getInt("P_Time_Sheet_ID");
                r.timeSheetDocNo = rs.getString("Time_Sheet_DocNo");

                r.periodId = rs.getInt("P_Period_ID");
                r.periodName = rs.getString("Period_Name");

                r.creditsId = rs.getInt("P_Credits_ID");
                String cVal = rs.getString("Credit_Value");
                String cName = rs.getString("Credit_Name");
                if (cVal != null && !cVal.trim().isEmpty())
                {
                    r.creditDisplay = cVal + (cName != null ? (" - " + cName) : "");
                }
                else
                {
                    r.creditDisplay = "-";
                }

                r.message = rs.getString("Alert_Message");
                r.created = rs.getTimestamp("Created");
                if (r.created != null)
                {
                    r.createdDisplay = sdf.format(r.created);
                }
                else
                {
                    r.createdDisplay = "";
                }

                rows.add(r);
            }
        }
        catch (Exception e)
        {
            s_log.log(Level.SEVERE, "loadData - " + sql, e);
        }
        finally
        {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (pstmt != null) pstmt.close(); } catch (Exception ignored) {}
            setCursor(Cursor.getDefaultCursor());
        }

        tableModel.setRows(rows);

        int totalCount = rows.size();
        lblTotal.setText("Total : " + totalCount);
        lblErrors.setText("\ud83d\udd34 " + errorCount + " Erreur(s)");
        lblWarnings.setText("\ud83d\udfe1 " + warningCount + " Avertissement(s)");
        lblInfos.setText("\ud83d\udd35 " + infoCount + " Information(s)");

        rdoAll.setText("Tous (" + totalCount + ")");
        rdoError.setText("\ud83d\udd34 Erreurs (" + errorCount + ")");
        rdoWarning.setText("\ud83d\udfe1 Avertissements (" + warningCount + ")");
        rdoInfo.setText("\ud83d\udd35 Informations (" + infoCount + ")");

        applyFilter();
    }

    /**
     * Applique les filtres de sévérité et le filtre texte sur la table
     */
    private void applyFilter()
    {
        String text = (txtSearch != null) ? txtSearch.getText().trim() : "";
        String selectedSeverity = null;
        if (rdoError.isSelected()) selectedSeverity = "E";
        else if (rdoWarning.isSelected()) selectedSeverity = "W";
        else if (rdoInfo.isSelected()) selectedSeverity = "I";

        final String fSeverity = selectedSeverity;
        final String fText = text.toLowerCase();

        RowFilter<TimeSheetErrorTableModel, Integer> filter = new RowFilter<TimeSheetErrorTableModel, Integer>()
        {
            @Override
            public boolean include(Entry<? extends TimeSheetErrorTableModel, ? extends Integer> entry)
            {
                int modelRow = entry.getIdentifier().intValue();
                ErrorRow row = tableModel.getRow(modelRow);
                if (row == null) return false;

                // 1. Filtre par sévérité
                if (fSeverity != null)
                {
                    if (!fSeverity.equalsIgnoreCase(row.rawSeverity))
                        return false;
                }

                // 2. Filtre par texte de recherche
                if (!fText.isEmpty())
                {
                    boolean match = false;
                    if (row.employeeValue != null && row.employeeValue.toLowerCase().contains(fText)) match = true;
                    else if (row.employeeName != null && row.employeeName.toLowerCase().contains(fText)) match = true;
                    else if (row.timeSheetDocNo != null && row.timeSheetDocNo.toLowerCase().contains(fText)) match = true;
                    else if (row.creditDisplay != null && row.creditDisplay.toLowerCase().contains(fText)) match = true;
                    else if (row.message != null && row.message.toLowerCase().contains(fText)) match = true;
                    else if (row.severityDisplay != null && row.severityDisplay.toLowerCase().contains(fText)) match = true;
                    if (!match) return false;
                }

                return true;
            }
        };

        rowSorter.setRowFilter(filter);
    }

    /**
     * Gestionnaire des actions des boutons
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();

        if (src == comboPeriod || src == btnRefresh)
        {
            KeyNamePair knp = (KeyNamePair) comboPeriod.getSelectedItem();
            if (knp != null)
            {
                m_periodId = knp.getKey();
                loadData(m_periodId, m_employeeId, m_timeSheetId);
            }
        }
        else if (src == rdoAll || src == rdoError || src == rdoWarning || src == rdoInfo)
        {
            applyFilter();
        }
        else if (src == btnZoom)
        {
            zoomToSelectedTimeSheet();
        }
        else if (src == btnExcel)
        {
            exportToExcel();
        }
        else if (src == btnCopy)
        {
            copyTableToClipboard();
        }
        else if (src == btnPrint)
        {
            printTable();
        }
        else if (src == btnClose)
        {
            dispose();
        }
    }

    /**
     * Ouvre la feuille de temps sélectionnée dans Solstice
     */
    private void zoomToSelectedTimeSheet()
    {
        int viewRow = tableErrors.getSelectedRow();
        if (viewRow < 0)
        {
            JOptionPane.showMessageDialog(this, "Veuillez s\u00e9lectionner une ligne dans le tableau.", "S\u00e9lection requise", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int modelRow = tableErrors.convertRowIndexToModel(viewRow);
        ErrorRow row = tableModel.getRow(modelRow);
        if (row != null && row.timeSheetId > 0)
        {
            AEnv.zoom(P_Time_Sheet.Table_ID, row.timeSheetId);
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Aucune feuille de temps n'est associ\u00e9e \u00e0 ce message.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Affiche une boîte de dialogue avec le message complet et les informations de l'employé
     */
    private void showSelectedRowDetail()
    {
        int viewRow = tableErrors.getSelectedRow();
        if (viewRow < 0) return;

        int modelRow = tableErrors.convertRowIndexToModel(viewRow);
        final ErrorRow row = tableModel.getRow(modelRow);
        if (row == null) return;

        final JDialog detailDialog = new JDialog(this, "D\u00e9tail du message", true);
        detailDialog.setLayout(new BorderLayout(8, 8));

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 12, 6, 12));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 4, 3, 4);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        infoPanel.add(new JLabel("S\u00e9v\u00e9rit\u00e9 :"), gbc);
        gbc.gridx = 1;
        JLabel lblSev = new JLabel(row.severityDisplay);
        lblSev.setFont(lblSev.getFont().deriveFont(Font.BOLD));
        infoPanel.add(lblSev, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        infoPanel.add(new JLabel("Employ\u00e9 :"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel((row.employeeValue != null ? row.employeeValue : "") + " - " + (row.employeeName != null ? row.employeeName : "")), gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        infoPanel.add(new JLabel("Feuille de temps :"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(row.timeSheetDocNo != null ? row.timeSheetDocNo : "-"), gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        infoPanel.add(new JLabel("Banque / Cr\u00e9dit :"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(row.creditDisplay != null ? row.creditDisplay : "-"), gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        infoPanel.add(new JLabel("Date / Heure :"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(row.createdDisplay != null ? row.createdDisplay : "-"), gbc);

        detailDialog.add(infoPanel, BorderLayout.NORTH);

        JTextArea txtMessage = new JTextArea(row.message != null ? row.message : "");
        txtMessage.setEditable(false);
        txtMessage.setLineWrap(true);
        txtMessage.setWrapStyleWord(true);
        txtMessage.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtMessage.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        JScrollPane scroll = new JScrollPane(txtMessage);
        scroll.setBorder(BorderFactory.createTitledBorder("Message d'alerte"));
        scroll.setPreferredSize(new Dimension(540, 160));
        detailDialog.add(scroll, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));

        if (row.timeSheetId > 0)
        {
            JButton bZoomDetail = new JButton("Ouvrir la feuille...");
            bZoomDetail.setIcon(Env.getImageIcon("Zoom16.gif"));
            bZoomDetail.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    detailDialog.dispose();
                    AEnv.zoom(P_Time_Sheet.Table_ID, row.timeSheetId);
                }
            });
            btnPanel.add(bZoomDetail);
        }

        JButton bCopyMsg = new JButton("Copier le message");
        bCopyMsg.setIcon(Env.getImageIcon("Copy16.gif"));
        bCopyMsg.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                StringSelection sel = new StringSelection(row.message != null ? row.message : "");
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, sel);
                JOptionPane.showMessageDialog(detailDialog, "Message copi\u00e9.", "Copier", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        btnPanel.add(bCopyMsg);

        JButton bCloseDetail = new JButton("Fermer");
        bCloseDetail.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                detailDialog.dispose();
            }
        });
        btnPanel.add(bCloseDetail);

        detailDialog.add(btnPanel, BorderLayout.SOUTH);
        detailDialog.pack();
        detailDialog.setLocationRelativeTo(this);
        detailDialog.setVisible(true);
    }

    /**
     * Exporte la table des erreurs vers Excel via ExcelExportUtil
     */
    private void exportToExcel()
    {
        String periodName = "";
        KeyNamePair knp = (KeyNamePair) comboPeriod.getSelectedItem();
        if (knp != null)
        {
            periodName = "_" + knp.getName().replaceAll("[^a-zA-Z0-9_-]", "_");
        }
        ExcelExportUtil.exportTable(tableErrors, "Messages_Erreurs_Temps" + periodName, this);
    }

    /**
     * Copie le contenu tabulé dans le presse-papiers
     */
    private void copyTableToClipboard()
    {
        StringBuilder sb = new StringBuilder();
        int cols = tableErrors.getColumnCount();
        int rows = tableErrors.getRowCount();

        for (int c = 0; c < cols; c++)
        {
            if (c > 0) sb.append("\t");
            sb.append(tableErrors.getColumnName(c));
        }
        sb.append("\n");

        for (int r = 0; r < rows; r++)
        {
            for (int c = 0; c < cols; c++)
            {
                if (c > 0) sb.append("\t");
                Object val = tableErrors.getValueAt(r, c);
                sb.append(val != null ? val.toString().replace("\n", " ").replace("\r", " ") : "");
            }
            sb.append("\n");
        }

        StringSelection sel = new StringSelection(sb.toString());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, sel);
        JOptionPane.showMessageDialog(this, "Le contenu du tableau a \u00e9t\u00e9 copi\u00e9 dans le presse-papiers (" + rows + " lignes).", "Copier", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Imprime le tableau
     */
    private void printTable()
    {
        try
        {
            String title = "Rapport des erreurs de g\u00e9n\u00e9ration du temps";
            KeyNamePair knp = (KeyNamePair) comboPeriod.getSelectedItem();
            if (knp != null)
            {
                title += " - " + knp.getName();
            }
            tableErrors.print(JTable.PrintMode.FIT_WIDTH, new java.text.MessageFormat(title), new java.text.MessageFormat("Page {0}"));
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this, "Erreur d'impression : " + e.getMessage(), "Impression", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================================================================
    // Modèle de données de la table
    // =========================================================================

    public static class ErrorRow
    {
        public int errorId;
        public String rawSeverity;
        public String severityDisplay;
        public int employeeId;
        public String employeeValue;
        public String employeeName;
        public int timeSheetId;
        public String timeSheetDocNo;
        public int periodId;
        public String periodName;
        public int creditsId;
        public String creditDisplay;
        public String message;
        public Timestamp created;
        public String createdDisplay;
    }

    private static class TimeSheetErrorTableModel extends AbstractTableModel
    {
        private static final long serialVersionUID = 1L;
        private final String[] columnNames = {
            "S\u00e9v\u00e9rit\u00e9",
            "Code Emp.",
            "Nom de l'employ\u00e9",
            "Feuille No",
            "Banque / Cr\u00e9dit",
            "Message d'alerte",
            "Date / Heure"
        };
        private List<ErrorRow> data = new ArrayList<ErrorRow>();

        public void setRows(List<ErrorRow> rows)
        {
            this.data = (rows != null) ? rows : new ArrayList<ErrorRow>();
            fireTableDataChanged();
        }

        public ErrorRow getRow(int row)
        {
            if (row >= 0 && row < data.size())
            {
                return data.get(row);
            }
            return null;
        }

        @Override
        public int getRowCount()
        {
            return data.size();
        }

        @Override
        public int getColumnCount()
        {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column)
        {
            return columnNames[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex)
        {
            ErrorRow r = data.get(rowIndex);
            switch (columnIndex)
            {
                case 0: return r.severityDisplay;
                case 1: return r.employeeValue != null ? r.employeeValue : "";
                case 2: return r.employeeName != null ? r.employeeName : "";
                case 3: return r.timeSheetDocNo != null ? r.timeSheetDocNo : (r.timeSheetId > 0 ? String.valueOf(r.timeSheetId) : "-");
                case 4: return r.creditDisplay != null ? r.creditDisplay : "-";
                case 5: return r.message != null ? r.message : "";
                case 6: return r.createdDisplay != null ? r.createdDisplay : "";
                default: return "";
            }
        }
    }

    /**
     * Rendu soigné des cellules du tableau avec zébrage et couleurs adaptées à la sévérité
     */
    private class ErrorTableCellRenderer extends DefaultTableCellRenderer
    {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column)
        {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            int modelRow = table.convertRowIndexToModel(row);
            ErrorRow errorRow = tableModel.getRow(modelRow);

            if (!isSelected)
            {
                if (row % 2 == 1)
                {
                    c.setBackground(new Color(248, 250, 252));
                }
                else
                {
                    c.setBackground(Color.WHITE);
                }
            }

            // Mise en valeur de la colonne Sévérité
            if (column == 0 && errorRow != null)
            {
                setFont(getFont().deriveFont(Font.BOLD));
                if ("E".equalsIgnoreCase(errorRow.rawSeverity))
                {
                    setForeground(new Color(192, 0, 0));
                    if (!isSelected) c.setBackground(new Color(255, 238, 238));
                }
                else if ("W".equalsIgnoreCase(errorRow.rawSeverity))
                {
                    setForeground(new Color(190, 110, 0));
                    if (!isSelected) c.setBackground(new Color(255, 250, 230));
                }
                else if ("I".equalsIgnoreCase(errorRow.rawSeverity))
                {
                    setForeground(new Color(0, 102, 204));
                    if (!isSelected) c.setBackground(new Color(238, 246, 255));
                }
                else
                {
                    setForeground(Color.DARK_GRAY);
                }
            }
            else if (!isSelected)
            {
                setForeground(Color.BLACK);
            }

            // Tooltip sur la colonne Message
            if (column == 5 && value != null)
            {
                setToolTipText(value.toString());
            }
            else
            {
                setToolTipText(null);
            }

            return c;
        }
    }

    // =========================================================================
    // Méthodes statiques de lancement
    // =========================================================================

    /**
     * Compte le nombre d'erreurs/messages pour une période donnée dans P_Time_Sheet_Error
     */
    public static int getErrorCount(int periodId, int employeeId, int timeSheetId)
    {
        String sql = "SELECT COUNT(1) FROM P_Time_Sheet_Error err "
            + "LEFT OUTER JOIN P_Time_Sheet ts ON (err.P_Time_Sheet_ID = ts.P_Time_Sheet_ID) "
            + "WHERE 1=1 ";

        if (periodId > 0)
        {
            sql += " AND (ts.P_Period_ID = " + periodId 
                + " OR (ts.P_Period_ID IS NULL AND err.Created >= (SELECT StartDate FROM P_Period WHERE P_Period_ID = " + periodId + ")))";
        }
        if (employeeId > 0)
        {
            sql += " AND ts.P_Employee_ID = " + employeeId;
        }
        if (timeSheetId > 0)
        {
            sql += " AND ts.P_Time_Sheet_ID = " + timeSheetId;
        }

        int count = DB.getSQLValue(null, sql);
        return count > 0 ? count : 0;
    }

    /**
     * Ouvre toujours la boîte de dialogue de rapport des erreurs
     */
    public static void showReport(final Properties ctx, final int periodId, final int employeeId, final int timeSheetId)
    {
        if (java.awt.GraphicsEnvironment.isHeadless())
            return;

        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    VTimeSheetErrorReportDialog dialog = new VTimeSheetErrorReportDialog(null, ctx, periodId, employeeId, timeSheetId);
                    dialog.setVisible(true);
                }
                catch (Throwable t)
                {
                    s_log.log(Level.SEVERE, "showReport error", t);
                }
            }
        });
    }

    /**
     * Ouvre le rapport des erreurs si au moins 1 message existe,
     * sinon affiche une notification discrète que la génération s'est terminée sans aucune erreur.
     */
    public static void showReportIfErrorsOrNotify(final Properties ctx, final int periodId, final int employeeId, final int timeSheetId)
    {
        if (java.awt.GraphicsEnvironment.isHeadless())
            return;

        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    int count = getErrorCount(periodId, employeeId, timeSheetId);
                    if (count > 0)
                    {
                        VTimeSheetErrorReportDialog dialog = new VTimeSheetErrorReportDialog(null, ctx, periodId, employeeId, timeSheetId);
                        dialog.setVisible(true);
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(
                            null,
                            "G\u00e9n\u00e9ration du temps termin\u00e9e avec succ\u00e8s.\n"
                            + "Aucun message d'erreur ou d'avertissement n'a \u00e9t\u00e9 g\u00e9n\u00e9r\u00e9 dans P_Time_Sheet_Error.",
                            "G\u00e9n\u00e9ration du temps - Rapport",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                }
                catch (Throwable t)
                {
                    s_log.log(Level.SEVERE, "showReportIfErrorsOrNotify error", t);
                }
            }
        });
    }
}
