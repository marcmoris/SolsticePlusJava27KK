package org.solstice.util;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.Lookup;
import org.compiere.util.CLogger;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumn;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyleInfo;

/**
 * Utilitaire centralise d'exportation vers Excel au format natif XLSX.
 * Remplace l'ancienne implementation JXL et les conversions Python.
 */
public class ExcelExportUtil {

    private static final CLogger s_log = CLogger.getCLogger(ExcelExportUtil.class);
    private static final String TABLE_STYLE = "TableStyleMedium9";

    /**
     * Boite de dialogue de selection du fichier .xlsx avec confirmation d'ecrasement.
     */
    public static File chooseFile(String defaultFileName, Component parent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Exporter vers Excel");
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Fichiers Excel (*.xlsx)", "xlsx"));

        if (defaultFileName != null && !defaultFileName.trim().isEmpty()) {
            if (!defaultFileName.toLowerCase().endsWith(".xlsx")) {
                defaultFileName += ".xlsx";
            }
            chooser.setSelectedFile(new File(defaultFileName));
        }

        int userSelection = chooser.showSaveDialog(parent);
        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        File file = chooser.getSelectedFile();
        if (file == null) {
            return null;
        }

        String path = file.getAbsolutePath();
        if (!path.toLowerCase().endsWith(".xlsx")) {
            file = new File(path + ".xlsx");
        }

        if (file.exists()) {
            int confirm = JOptionPane.showConfirmDialog(
                parent,
                "Le fichier existe deja :\n" + file.getName() + "\nVoulez-vous le remplacer ?",
                "Confirmer le remplacement",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            if (confirm != JOptionPane.YES_OPTION) {
                return null;
            }
        }

        return file;
    }

    /**
     * Garantit qu'un nom de colonne est non vide, nettoye et strictement unique dans la table Excel.
     */
    private static String getUniqueHeader(String rawHeader, int colIndex, Set<String> usedNames) {
        String header = rawHeader;
        if (header == null || header.trim().isEmpty()) {
            header = "Colonne_" + (colIndex + 1);
        } else {
            header = header.replace("\r", "").replace("\n", "").replace("\t", "").trim();
            if (header.isEmpty()) {
                header = "Colonne_" + (colIndex + 1);
            }
        }
        String unique = header;
        int counter = 2;
        while (usedNames.contains(unique.toLowerCase())) {
            unique = header + "_" + counter;
            counter++;
        }
        usedNames.add(unique.toLowerCase());
        return unique;
    }

    /**
     * Exporte les donnees d'un onglet de grille Compiere (GridTab) vers un fichier .xlsx.
     * Utilise par APanel.cmd_exportToExcel().
     */
    public static void exportGrid(GridTab curTab, Component parent) {
        if (curTab == null || curTab.getRowCount() == 0) {
            JOptionPane.showMessageDialog(parent, "Aucune donnee a exporter.", "Export Excel", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String suggestedName = curTab.getName();
        if (suggestedName == null || suggestedName.trim().isEmpty()) {
            suggestedName = "Export";
        }
        suggestedName = suggestedName.replaceAll("[\\\\/:*?\"<>|]", "_");

        File file = chooseFile(suggestedName + ".xlsx", parent);
        if (file == null) {
            return;
        }

        Cursor prevCursor = null;
        if (parent != null) {
            prevCursor = parent.getCursor();
            parent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        }

        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            XSSFSheet sheet = wb.createSheet("Detail");
            XSSFDataFormat dataFormat = wb.createDataFormat();

            // Styles
            XSSFCellStyle intStyle = wb.createCellStyle();
            intStyle.setDataFormat(dataFormat.getFormat("#,##0"));

            XSSFCellStyle floatStyle = wb.createCellStyle();
            floatStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));

            XSSFCellStyle dateStyle = wb.createCellStyle();
            dateStyle.setDataFormat(dataFormat.getFormat("yyyy-mm-dd"));

            XSSFCellStyle dateTimeStyle = wb.createCellStyle();
            dateTimeStyle.setDataFormat(dataFormat.getFormat("yyyy-mm-dd hh:mm:ss"));

            // 1. En-tetes uniques
            List<Integer> displayedColIndices = new ArrayList<>();
            List<String> colNames = new ArrayList<>();
            Set<String> usedNames = new HashSet<>();

            XSSFRow headerRow = sheet.createRow(0);
            int colIdx = 0;
            for (int i = 0; i < curTab.getFieldCount(); i++) {
                GridField f = curTab.getField(i);
                if (f != null && f.isDisplayed()) {
                    String header = f.getHeader();
                    if (header == null || header.trim().isEmpty()) {
                        header = f.getColumnName();
                    }
                    String uniqueHeader = getUniqueHeader(header, colIdx, usedNames);
                    XSSFCell cell = headerRow.createCell(colIdx);
                    cell.setCellValue(uniqueHeader);
                    displayedColIndices.add(i);
                    colNames.add(uniqueHeader);
                    colIdx++;
                }
            }

            int numCols = colNames.size();
            if (numCols == 0) {
                JOptionPane.showMessageDialog(parent, "Aucune colonne affichable a exporter.", "Export Excel", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Lignes de donnees
            int rowCount = curTab.getRowCount();
            for (int r = 0; r < rowCount; r++) {
                curTab.navigate(r);
                XSSFRow row = sheet.createRow(r + 1);

                for (int c = 0; c < numCols; c++) {
                    int fieldIndex = displayedColIndices.get(c);
                    GridField field = curTab.getField(fieldIndex);
                    XSSFCell cell = row.createCell(c);

                    if (field == null) {
                        continue;
                    }

                    Object valObj = field.getValue();
                    if (valObj == null) {
                        continue;
                    }

                    int dt = field.getDisplayType();
                    Lookup lookup = field.getLookup();
                    if (lookup != null) {
                        String display = lookup.getDisplay(valObj);
                        cell.setCellValue(display != null ? display : valObj.toString());
                        continue;
                    }

                    try {
                        if (dt == DisplayType.YesNo) {
                            if (valObj instanceof Boolean) {
                                cell.setCellValue((Boolean) valObj);
                            } else {
                                String s = valObj.toString();
                                cell.setCellValue("Y".equalsIgnoreCase(s) || "true".equalsIgnoreCase(s));
                            }
                        } else if (dt == DisplayType.Integer) {
                            if (valObj instanceof Number) {
                                cell.setCellValue(((Number) valObj).doubleValue());
                            } else {
                                cell.setCellValue(Double.parseDouble(valObj.toString().trim()));
                            }
                            cell.setCellStyle(intStyle);
                        } else if (dt == DisplayType.Amount || dt == DisplayType.Number
                                || dt == DisplayType.Quantity || dt == DisplayType.CostPrice) {
                            if (valObj instanceof Number) {
                                cell.setCellValue(((Number) valObj).doubleValue());
                            } else {
                                cell.setCellValue(Double.parseDouble(valObj.toString().trim()));
                            }
                            cell.setCellStyle(floatStyle);
                        } else if (dt == DisplayType.Date) {
                            if (valObj instanceof Date) {
                                cell.setCellValue((Date) valObj);
                            } else if (valObj instanceof Timestamp) {
                                cell.setCellValue(new Date(((Timestamp) valObj).getTime()));
                            } else {
                                cell.setCellValue(valObj.toString());
                            }
                            cell.setCellStyle(dateStyle);
                        } else if (dt == DisplayType.DateTime) {
                            if (valObj instanceof Date) {
                                cell.setCellValue((Date) valObj);
                            } else if (valObj instanceof Timestamp) {
                                cell.setCellValue(new Date(((Timestamp) valObj).getTime()));
                            } else {
                                cell.setCellValue(valObj.toString());
                            }
                            cell.setCellStyle(dateTimeStyle);
                        } else {
                            cell.setCellValue(valObj.toString());
                        }
                    } catch (Exception ex) {
                        cell.setCellValue(valObj.toString());
                    }
                }
            }

            // 3. Formatage en Tableau Excel natif (TableStyleMedium9)
            if (rowCount > 0 && numCols > 0) {
                applyTableStyle(sheet, colNames, rowCount, numCols);
            }

            // 4. Largeurs de colonnes
            for (int c = 0; c < numCols; c++) {
                sheet.autoSizeColumn(c);
                int currentWidth = sheet.getColumnWidth(c);
                sheet.setColumnWidth(c, Math.min(256 * 80, currentWidth + 1200));
            }

            // 5. Sauvegarde
            try (FileOutputStream fos = new FileOutputStream(file)) {
                wb.write(fos);
            }

            postExportSuccess(file, parent);

        } catch (Exception e) {
            s_log.severe("Erreur export Excel GridTab: " + e.getMessage());
            JOptionPane.showMessageDialog(parent, "Erreur lors de l'export Excel :\n" + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (parent != null && prevCursor != null) {
                parent.setCursor(prevCursor);
            }
        }
    }

    /**
     * Exporte les donnees d'un ResultSet JDBC (avec eventuellement des ColumnInfo[] de formulaire) vers un fichier .xlsx.
     * Utilise par les formulaires CustomWindow / CustomWindowQuery (VCreditsTotal, etc.).
     */
    public static void exportResultSet(ResultSet rs, ColumnInfo[] colInfo, String defaultName, Component parent) {
        if (rs == null) {
            JOptionPane.showMessageDialog(parent, "Aucune donnee a exporter.", "Export Excel", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        File file = chooseFile(defaultName != null ? (defaultName + ".xlsx") : "Export.xlsx", parent);
        if (file == null) {
            return;
        }

        Cursor prevCursor = null;
        if (parent != null) {
            prevCursor = parent.getCursor();
            parent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        }

        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            XSSFSheet sheet = wb.createSheet("Detail");
            XSSFDataFormat dataFormat = wb.createDataFormat();

            XSSFCellStyle intStyle = wb.createCellStyle();
            intStyle.setDataFormat(dataFormat.getFormat("#,##0"));

            XSSFCellStyle floatStyle = wb.createCellStyle();
            floatStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));

            XSSFCellStyle dateStyle = wb.createCellStyle();
            dateStyle.setDataFormat(dataFormat.getFormat("yyyy-mm-dd"));

            XSSFCellStyle dateTimeStyle = wb.createCellStyle();
            dateTimeStyle.setDataFormat(dataFormat.getFormat("yyyy-mm-dd hh:mm:ss"));

            ResultSetMetaData rsMetaData = rs.getMetaData();
            int totalSqlCols = rsMetaData.getColumnCount();

            // 1. Filtrer et construire les en-tetes (ignorer les colonnes _ID et vides)
            List<Integer> validSqlColIndices = new ArrayList<>(); // 1-based index
            List<String> colNames = new ArrayList<>();
            Set<String> usedNames = new HashSet<>();

            XSSFRow headerRow = sheet.createRow(0);
            int validColIdx = 0;
            for (int i = 1; i <= totalSqlCols; i++) {
                String colHeader = null;
                if (colInfo != null && i - 1 < colInfo.length && colInfo[i - 1] != null) {
                    colHeader = colInfo[i - 1].getColHeader();
                }
                if (colHeader == null || colHeader.trim().isEmpty()) {
                    colHeader = rsMetaData.getColumnLabel(i);
                }

                if (colHeader != null) {
                    String trimmed = colHeader.replace("\r", "").replace("\n", "").trim();
                    if (!trimmed.endsWith("_ID") && !trimmed.isEmpty()) {
                        String uniqueHeader = getUniqueHeader(trimmed, validColIdx, usedNames);
                        validSqlColIndices.add(i);
                        colNames.add(uniqueHeader);
                        XSSFCell cell = headerRow.createCell(validColIdx);
                        cell.setCellValue(uniqueHeader);
                        validColIdx++;
                    }
                }
            }

            int numCols = colNames.size();
            if (numCols == 0) {
                JOptionPane.showMessageDialog(parent, "Aucune colonne a exporter.", "Export Excel", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Lignes
            int rowIdx = 1;
            while (rs.next()) {
                XSSFRow row = sheet.createRow(rowIdx);
                for (int c = 0; c < numCols; c++) {
                    int sqlCol = validSqlColIndices.get(c);
                    Object val = rs.getObject(sqlCol);
                    XSSFCell cell = row.createCell(c);

                    if (val == null) {
                        continue;
                    }

                    if (val instanceof BigDecimal || val instanceof Double || val instanceof Float) {
                        cell.setCellValue(((Number) val).doubleValue());
                        cell.setCellStyle(floatStyle);
                    } else if (val instanceof Integer || val instanceof Long || val instanceof Short) {
                        cell.setCellValue(((Number) val).doubleValue());
                        cell.setCellStyle(intStyle);
                    } else if (val instanceof Timestamp) {
                        cell.setCellValue(new Date(((Timestamp) val).getTime()));
                        cell.setCellStyle(dateTimeStyle);
                    } else if (val instanceof java.sql.Date) {
                        cell.setCellValue(new Date(((java.sql.Date) val).getTime()));
                        cell.setCellStyle(dateStyle);
                    } else if (val instanceof Boolean) {
                        cell.setCellValue((Boolean) val);
                    } else {
                        cell.setCellValue(val.toString());
                    }
                }
                rowIdx++;
            }

            int dataRowCount = rowIdx - 1;

            // 3. Table format
            if (dataRowCount > 0 && numCols > 0) {
                applyTableStyle(sheet, colNames, dataRowCount, numCols);
            }

            // 4. Auto-size
            for (int c = 0; c < numCols; c++) {
                sheet.autoSizeColumn(c);
                int currentWidth = sheet.getColumnWidth(c);
                sheet.setColumnWidth(c, Math.min(256 * 80, currentWidth + 1200));
            }

            // 5. Sauvegarde
            try (FileOutputStream fos = new FileOutputStream(file)) {
                wb.write(fos);
            }

            postExportSuccess(file, parent);

        } catch (Exception e) {
            s_log.severe("Erreur export Excel ResultSet: " + e.getMessage());
            JOptionPane.showMessageDialog(parent, "Erreur lors de l'export Excel :\n" + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (parent != null && prevCursor != null) {
                parent.setCursor(prevCursor);
            }
        }
    }

    /**
     * Applique la structure Table native (TableStyleMedium9 avec lignes bicolores) au range de donnees.
     */
    private static void applyTableStyle(XSSFSheet sheet, List<String> colNames, int numDataRows, int numCols) {
        if (numDataRows <= 0 || numCols <= 0 || colNames == null || colNames.isEmpty()) {
            return;
        }
        try {
            AreaReference area = new AreaReference(
                new CellReference(0, 0),
                new CellReference(numDataRows, numCols - 1),
                SpreadsheetVersion.EXCEL2007
            );

            XSSFTable table = sheet.createTable(area);
            String tableName = "Table_" + System.currentTimeMillis();
            table.setName(tableName);
            table.setDisplayName(tableName);

            CTTable ctTable = table.getCTTable();

            // Style de tableau
            CTTableStyleInfo style = ctTable.isSetTableStyleInfo() ? ctTable.getTableStyleInfo() : ctTable.addNewTableStyleInfo();
            style.setName(TABLE_STYLE);
            style.setShowRowStripes(true);
            style.setShowColumnStripes(false);

            // Filtres automatiques
            if (!ctTable.isSetAutoFilter()) {
                ctTable.addNewAutoFilter().setRef(area.formatAsString());
            }

            // Synchroniser les noms de colonnes dans la definition du tableau
            CTTableColumns columns = ctTable.getTableColumns();
            if (columns == null) {
                columns = ctTable.addNewTableColumns();
                columns.setCount(colNames.size());
                for (int i = 0; i < colNames.size(); i++) {
                    CTTableColumn col = columns.addNewTableColumn();
                    col.setId(i + 1);
                    col.setName(colNames.get(i));
                }
            } else {
                List<CTTableColumn> colList = columns.getTableColumnList();
                for (int i = 0; i < colList.size() && i < colNames.size(); i++) {
                    colList.get(i).setName(colNames.get(i));
                }
            }
        } catch (Exception e) {
            s_log.warning("Impossible d'appliquer le style de table Excel: " + e.getMessage());
        }
    }

    /**
     * Exporte les donnees d'une JTable vers un fichier .xlsx.
     * Conserve les filtres et l'ordre visuel des colonnes et des lignes.
     */
    public static void exportTable(JTable table, String defaultName, Component parent) {
        if (table == null || table.getRowCount() == 0) {
            JOptionPane.showMessageDialog(parent, "Aucune donnee a exporter.", "Export Excel", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        File file = chooseFile(defaultName != null ? (defaultName + ".xlsx") : "Export.xlsx", parent);
        if (file == null) {
            return;
        }

        Cursor prevCursor = null;
        if (parent != null) {
            prevCursor = parent.getCursor();
            parent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        }

        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            XSSFSheet sheet = wb.createSheet("Donnees");
            XSSFDataFormat dataFormat = wb.createDataFormat();

            XSSFCellStyle intStyle = wb.createCellStyle();
            intStyle.setDataFormat(dataFormat.getFormat("#,##0"));

            XSSFCellStyle floatStyle = wb.createCellStyle();
            floatStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));

            XSSFCellStyle dateStyle = wb.createCellStyle();
            dateStyle.setDataFormat(dataFormat.getFormat("yyyy-mm-dd"));

            XSSFCellStyle dateTimeStyle = wb.createCellStyle();
            dateTimeStyle.setDataFormat(dataFormat.getFormat("yyyy-mm-dd hh:mm:ss"));

            int numCols = table.getColumnCount();
            List<String> colNames = new ArrayList<>();
            Set<String> usedNames = new HashSet<>();

            XSSFRow headerRow = sheet.createRow(0);
            for (int c = 0; c < numCols; c++) {
                String colHeader = table.getColumnName(c);
                if (colHeader == null || colHeader.trim().isEmpty()) {
                    colHeader = "Col_" + (c + 1);
                }
                String uniqueHeader = getUniqueHeader(colHeader.trim(), c, usedNames);
                colNames.add(uniqueHeader);
                XSSFCell cell = headerRow.createCell(c);
                cell.setCellValue(uniqueHeader);
            }

            int numRows = table.getRowCount();
            for (int r = 0; r < numRows; r++) {
                XSSFRow row = sheet.createRow(r + 1);
                for (int c = 0; c < numCols; c++) {
                    Object val = table.getValueAt(r, c);
                    XSSFCell cell = row.createCell(c);
                    if (val == null) {
                        continue;
                    }
                    if (val instanceof BigDecimal || val instanceof Double || val instanceof Float) {
                        cell.setCellValue(((Number) val).doubleValue());
                        cell.setCellStyle(floatStyle);
                    } else if (val instanceof Integer || val instanceof Long || val instanceof Short) {
                        cell.setCellValue(((Number) val).doubleValue());
                        cell.setCellStyle(intStyle);
                    } else if (val instanceof Timestamp) {
                        cell.setCellValue(new Date(((Timestamp) val).getTime()));
                        cell.setCellStyle(dateTimeStyle);
                    } else if (val instanceof java.sql.Date) {
                        cell.setCellValue(new Date(((java.sql.Date) val).getTime()));
                        cell.setCellStyle(dateStyle);
                    } else if (val instanceof Date) {
                        cell.setCellValue((Date) val);
                        cell.setCellStyle(dateTimeStyle);
                    } else if (val instanceof Boolean) {
                        cell.setCellValue((Boolean) val);
                    } else {
                        cell.setCellValue(val.toString());
                    }
                }
            }

            if (numRows > 0 && numCols > 0) {
                applyTableStyle(sheet, colNames, numRows, numCols);
            }

            for (int c = 0; c < numCols; c++) {
                sheet.autoSizeColumn(c);
                int currentWidth = sheet.getColumnWidth(c);
                sheet.setColumnWidth(c, Math.min(256 * 80, currentWidth + 1200));
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                wb.write(fos);
            }

            postExportSuccess(file, parent);

        } catch (Exception e) {
            s_log.severe("Erreur export Excel JTable: " + e.getMessage());
            JOptionPane.showMessageDialog(parent, "Erreur lors de l'export Excel :\n" + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (parent != null && prevCursor != null) {
                parent.setCursor(prevCursor);
            }
        }
    }

    /**
     * Notification apres reussite et proposition d'ouverture directe du fichier.
     */
    public static void postExportSuccess(File file, Component parent) {
        int choice = JOptionPane.showConfirmDialog(
            parent,
            "Fichier genere avec succes :\n" + file.getAbsolutePath() + "\n\nVoulez-vous l'ouvrir maintenant ?",
            "Export Excel termine",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            try {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                    Desktop.getDesktop().open(file);
                } else {
                    Env.startBrowser(file.toURI().toString());
                }
            } catch (Exception e) {
                s_log.warning("Impossible d'ouvrir automatiquement le fichier: " + e.getMessage());
            }
        }
    }
}