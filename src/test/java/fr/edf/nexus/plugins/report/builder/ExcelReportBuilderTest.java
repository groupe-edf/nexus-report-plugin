package fr.edf.nexus.plugins.report.builder;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;
import org.sonatype.nexus.plugins.report.internal.builders.ComponentInfos;
import org.sonatype.nexus.plugins.report.internal.builders.ExcelReportBuilder;

import fr.edf.nexus.plugins.report.builder.pojo.PojoBuilder;

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
    public void buildSheet_should_return_sheet() {
        List<ComponentInfos> componentsInfosList = PojoBuilder.buildComponentInfosList();

    }

}
