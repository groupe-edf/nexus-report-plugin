package org.sonatype.nexus.plugins.report.internal.builders;

import java.io.IOException;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumn;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyleInfo;
import org.sonatype.nexus.repository.storage.Component;

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
        final List<String> headers = ComponentInfos.getComponentInfosTitles();

        XSSFTable table = sheet.createTable(new AreaReference(new CellReference(0, 0),
                new CellReference(componentsInfos.size() - 1, headers.size() - 1), SpreadsheetVersion.EXCEL2007));
        CTTable cttable = table.getCTTable();
        CTTableStyleInfo cttableStyle = cttable.addNewTableStyleInfo();
        cttableStyle.setName("TableStyleMedium13");
        CTTableColumns columns = cttable.addNewTableColumns();
        columns.setCount(headers.size());
        for (int i = 0; i < headers.size(); i++) {
            CTTableColumn column = columns.addNewTableColumn();
            column.setName(headers.get(i));
            column.setId(i + 1);
        }
        int rownum = 0;
        for (int i = 0; i < componentsInfos.size(); i++) {
            XSSFRow row = sheet.createRow(i);
            if (rownum == 0) {
                for (int j = 0; j < headers.size(); j++) {
                    row.createCell(j).setCellValue(headers.get(j));
                }
            } else {
                row.createCell(0).setCellValue(componentsInfos.get(i).getGroup());
                row.createCell(1).setCellValue(componentsInfos.get(i).getName());
                row.createCell(2).setCellValue(componentsInfos.get(i).getVersion());
                row.createCell(3).setCellValue(componentsInfos.get(i).getFormat());
                row.createCell(4).setCellValue(componentsInfos.get(i).getSize());
                row.createCell(5).setCellValue(componentsInfos.get(i).getSizeMo());
                row.createCell(6).setCellValue(componentsInfos.get(i).getSizeGo());
                row.createCell(7).setCellValue(componentsInfos.get(i).getCreatedBy());
                row.createCell(8).setCellValue(componentsInfos.get(i).getLastUpdated());
                row.createCell(9).setCellValue(componentsInfos.get(i).getLastDownloaded());
                row.createCell(10).setCellValue(componentsInfos.get(i).getEncoded());
            }
            rownum++;
        }
        cttable.addNewAutoFilter();

        return sheet;
    }

}
