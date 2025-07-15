package api.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XLUtility {

    public FileInputStream fi;
    public FileOutputStream fo;
    public XSSFWorkbook workbook;
    public XSSFSheet sheet;
    public XSSFRow row;
    public XSSFCell cell;
    public CellStyle style;
    String excelFilePath;

    public XLUtility(String excelFilePath) {
        this.excelFilePath = excelFilePath;
    }

    // 🔧 Helper methods to manage file I/O
    private void openWorkbook() throws IOException {
        fi = new FileInputStream(excelFilePath);
        workbook = new XSSFWorkbook(fi);
    }

    private void closeWorkbook() throws IOException {
        workbook.close();
        fi.close();
        if (fo != null) fo.close();
    }

    public int getRowCount(String sheetName) throws IOException {
        openWorkbook();
        sheet = workbook.getSheet(sheetName);
        int rowcount = sheet.getLastRowNum();
        closeWorkbook();
        return rowcount;
    }

    public int getCellCount(String sheetName, int rownum) throws IOException {
        openWorkbook();
        sheet = workbook.getSheet(sheetName);
        row = sheet.getRow(rownum);
        if (row == null) {
            throw new IllegalArgumentException("Missing row at [" + rownum + "]");
        }
        int cellcount = (row != null) ? row.getLastCellNum() : 0;
        closeWorkbook();
        return cellcount;
    }

    public String getCellData(String sheetName, int rownum, int colnum) throws IOException {
        openWorkbook();
        sheet = workbook.getSheet(sheetName);
        row = sheet.getRow(rownum);
        if (row == null || row.getCell(colnum) == null) {
            throw new IllegalArgumentException("Missing row or cell at [" + rownum + "," + colnum + "]");
        }
        cell = row.getCell(colnum);
        cell = (row != null) ? row.getCell(colnum) : null;

        DataFormatter formatter = new DataFormatter();
        String data;
        try {
            data = formatter.formatCellValue(cell);
        } catch (Exception e) {
            data = "";
        }
        closeWorkbook();
        return data;
    }

    public void clearCell(String sheetName, int rownum, int colnum) throws IOException {
        openWorkbook();
        sheet = workbook.getSheet(sheetName);
        if (sheet == null) throw new IllegalArgumentException("Sheet '" + sheetName + "' does not exist");

        row = sheet.getRow(rownum);
        if (row == null) row = sheet.createRow(rownum);

        cell = row.getCell(colnum);
        if (cell == null) cell = row.createCell(colnum);
        cell.setBlank();

        fo = new FileOutputStream(excelFilePath);
        workbook.write(fo);
        closeWorkbook();
    }

    public int findRowByDeviceName(String sheetName, String targetName) throws IOException {
        int totalRows = getRowCount(sheetName);
        for (int i = 1; i <= totalRows; i++) {
            String nameInCell = getCellData(sheetName, i, 0);
            if (nameInCell != null && nameInCell.equalsIgnoreCase(targetName)) {
                return i;
            }
        }
        return totalRows + 1;
    }

    public void setCellData(String sheetName, int rownum, int colnum, String data) throws IOException {
        File xlfile = new File(excelFilePath);
        if (!xlfile.exists()) {
            workbook = new XSSFWorkbook();
            fo = new FileOutputStream(excelFilePath);
            workbook.write(fo);
        }

        openWorkbook();
        if (workbook.getSheetIndex(sheetName) == -1) workbook.createSheet(sheetName);
        sheet = workbook.getSheet(sheetName);

        if (sheet.getRow(rownum) == null) sheet.createRow(rownum);
        row = sheet.getRow(rownum);

        cell = row.getCell(colnum);
        if (cell == null) cell = row.createCell(colnum);

        cell.setCellValue(data);
        fo = new FileOutputStream(excelFilePath);
        workbook.write(fo);
        closeWorkbook();
    }

    public void fillGreenColor(String sheetName, int rownum, int colnum) throws IOException {
        openWorkbook();
        sheet = workbook.getSheet(sheetName);
        row = sheet.getRow(rownum);
        cell = (row != null) ? row.getCell(colnum) : null;
        if (row == null || cell == null) {
            throw new IllegalArgumentException("Missing row or cell at [" + rownum + "," + colnum + "]");
        }
        if (row == null) row = sheet.createRow(rownum);

        cell = row.getCell(colnum);
        if (cell == null) cell = row.createCell(colnum);

        style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        cell.setCellStyle(style);
        fo = new FileOutputStream(excelFilePath);
        workbook.write(fo);
        closeWorkbook();
    }

    public void fillRedColor(String sheetName, int rownum, int colnum) throws IOException {
        openWorkbook();
        sheet = workbook.getSheet(sheetName);
        row = sheet.getRow(rownum);
        cell = (row != null) ? row.getCell(colnum) : null;
        if (row == null || cell == null) {
            throw new IllegalArgumentException("Missing row or cell at [" + rownum + "," + colnum + "]");
        }
        if (row == null) row = sheet.createRow(rownum);

        cell = row.getCell(colnum);
        if (cell == null) cell = row.createCell(colnum);

        style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.RED.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        cell.setCellStyle(style);
        fo = new FileOutputStream(excelFilePath);
        workbook.write(fo);
        closeWorkbook();
    }
}

