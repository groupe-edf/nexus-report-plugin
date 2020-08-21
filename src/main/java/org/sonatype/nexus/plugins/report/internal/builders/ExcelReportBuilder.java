package org.sonatype.nexus.plugins.report.internal.builders;

import java.io.IOException;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;

public class ExcelReportBuilder {

    private Workbook workbook;

    public ExcelReportBuilder() {
        this.workbook = new XSSFWorkbook();
    }

    public ByteArrayOutputStream buildExcelFile() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        return outputStream;
    }

    public Sheet buildSheet(String sheetName, List<ComponentInfos> componentsInfos) {
        XSSFSheet sheet = (XSSFSheet) workbook.createSheet(sheetName);
        DataFormat format = workbook.createDataFormat();
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(format.getFormat("#,##0.00"));
        final List<String> headers = ComponentInfos.getComponentInfosTitles();

        int rowIndex = 0;
        // Header row
        XSSFRow headerRow = sheet.createRow(rowIndex);
        rowIndex++;
        for (int colIndex = 0; colIndex < headers.size(); colIndex++) {
            headerRow.createCell(colIndex).setCellValue(headers.get(colIndex));
        }
        // Datas rows
        for (int i = 0; i < componentsInfos.size(); i++) {
            XSSFRow row = sheet.createRow(rowIndex);
            rowIndex++;
            row.createCell(0).setCellValue(componentsInfos.get(i).getGroup());
            row.createCell(1).setCellValue(componentsInfos.get(i).getName());
            row.createCell(2).setCellValue(componentsInfos.get(i).getVersion());
            row.createCell(3).setCellValue(componentsInfos.get(i).getFormat());
            row.createCell(4).setCellValue(componentsInfos.get(i).getSize());
            XSSFCell cell = row.createCell(5);
            cell.setCellValue(componentsInfos.get(i).getSizeMo());
            cell.setCellStyle(style);
            cell = row.createCell(6);
            cell.setCellValue(componentsInfos.get(i).getSizeGo());
            cell.setCellStyle(style);
            row.createCell(7).setCellValue(componentsInfos.get(i).getCreatedBy());
            row.createCell(8).setCellValue(componentsInfos.get(i).getLastUpdated());
            row.createCell(9).setCellValue(componentsInfos.get(i).getLastDownloaded());
            row.createCell(10).setCellValue(componentsInfos.get(i).getEncoded());
        }
        // Apply table style
        if (!componentsInfos.isEmpty()) {
            XSSFTable table = sheet.createTable(new AreaReference(new CellReference(0, 0),
                    new CellReference(sheet.getLastRowNum(), sheet.getRow(sheet.getFirstRowNum()).getLastCellNum() - 1),
                    SpreadsheetVersion.EXCEL2007));
            table.setStyleName("TableStyleMedium13");
            CTTable cttable = table.getCTTable();
            cttable.addNewAutoFilter();
        }
        // AutoSize Column
        for (int colIndex = 0; colIndex < headers.size(); colIndex++) {
            sheet.autoSizeColumn(colIndex);
        }
        return sheet;
    }
}
