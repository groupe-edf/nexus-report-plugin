package fr.edf.nexus.plugins.report.builder;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Assert;
import org.junit.Test;

import fr.edf.nexus.plugins.report.builder.pojo.PojoBuilder;
import fr.edf.nexus.plugins.report.internal.builders.ComponentInfos;
import fr.edf.nexus.plugins.report.internal.builders.ExcelReportBuilder;

public class ExcelReportBuilderTest {

    @Test
    public void buildSheet_should_not_throw_exception_with_empty_data() {
        List<ComponentInfos> componentsInfosList = new ArrayList<>();
        ExcelReportBuilder excelBuilder = new ExcelReportBuilder();
        Sheet sheet = excelBuilder.buildSheet("test", componentsInfosList);
        assert sheet.getRow(0).getCell(0).getStringCellValue() == "Group";
        assert sheet.getLastRowNum() == 0; // only header
    }

    @Test
    public void buildSheet_should_return_sheet_with_10_datas_rows() {
        List<ComponentInfos> componentsInfosList = PojoBuilder.buildComponentInfosList();
        ExcelReportBuilder excelBuilder = new ExcelReportBuilder();
        Sheet sheet = excelBuilder.buildSheet("test", componentsInfosList);
        assert sheet.getRow(0).getCell(0).getStringCellValue() == "Group";
        assert sheet.getLastRowNum() == 10;
    }

    @Test
    public void buildSheet_should_not_throw_exception_with_empty_infos() {
        List<ComponentInfos> componentsInfosList = new ArrayList<>();
        ComponentInfos infos = new ComponentInfos();
        componentsInfosList.add(infos);
        ExcelReportBuilder excelBuilder = new ExcelReportBuilder();
        Sheet sheet = excelBuilder.buildSheet("test", componentsInfosList);
        assert sheet.getRow(0).getCell(0).getStringCellValue() == "Group";
        assert sheet.getLastRowNum() == 1;
    }

    @Test
    public void should_not_throw_exception_and_return_ByteArrayOutputStream() {
        List<ComponentInfos> componentsInfosList = new ArrayList<>();
        ComponentInfos infos = new ComponentInfos();
        componentsInfosList.add(infos);
        ExcelReportBuilder excelBuilder = new ExcelReportBuilder();
        Sheet sheet = excelBuilder.buildSheet("test", componentsInfosList);
        assert sheet.getRow(0).getCell(0).getStringCellValue() == "Group";
        assert sheet.getLastRowNum() == 1;
        OutputStream os = null;
        try {
            os = excelBuilder.buildExcelFile();
        } catch (IOException e) {
            Assert.fail();
        }
        assert os != null;
        assert os instanceof ByteArrayOutputStream;
    }
}
