package com.tonthepduybao.api.app.utils;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * ExcelUtils
 *
 * @author khal
 * @since 2023/07/21
 */
public class ExcelUtils {

    public static CellStyle getStyle(final XSSFWorkbook workbook,
                                     final boolean isBold, final boolean isItalic, final short color,
                                     final HorizontalAlignment alignment) {
        Font font = workbook.createFont();
        font.setBold(isBold);
        font.setItalic(isItalic);
        font.setFontHeightInPoints((short) 14);
        font.setColor(color);

        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setAlignment(alignment);

        return style;
    }

    public static CellStyle getBoldStyle(final XSSFWorkbook workbook) {
        return getStyle(workbook, true, false, HSSFColor.HSSFColorPredefined.BLACK.getIndex(), HorizontalAlignment.LEFT);
    }

    public static CellStyle getNormalStyle(final XSSFWorkbook workbook) {
        return getStyle(workbook, false, false, HSSFColor.HSSFColorPredefined.BLACK.getIndex(), HorizontalAlignment.LEFT);
    }

    public static CellStyle getBoldRedStyle(final XSSFWorkbook workbook) {
        return getStyle(workbook, true, false, HSSFColor.HSSFColorPredefined.RED.getIndex(), HorizontalAlignment.LEFT);
    }

    public static CellStyle getNormalRedStyle(final XSSFWorkbook workbook) {
        return getStyle(workbook, false, false, HSSFColor.HSSFColorPredefined.RED.getIndex(), HorizontalAlignment.LEFT);
    }

    public static CellStyle getPlaceholderStyle(final XSSFWorkbook workbook) {
        return getStyle(workbook, false, true, HSSFColor.HSSFColorPredefined.GREY_80_PERCENT.getIndex(), HorizontalAlignment.LEFT);
    }

    public static void setDropdownConstraint(final Workbook workbook, final XSSFSheet sheet,
                                             final String hiddenSheetName, final int hiddenSheetIndex,
                                             final int firstRow, final int lastRow, final int firstCol, final int lastCol,
                                             final List<String> data) {
        if (CollectionUtils.isEmpty(data))
            return;

        Sheet hiddenSheet = workbook.createSheet(hiddenSheetName);
        for (int i = 0; i < data.size(); i++) {
            String name = data.get(i);
            Row row = hiddenSheet.createRow(i);
            Cell cell = row.createCell(0);
            cell.setCellValue(name);
        }
        workbook.setSheetHidden(hiddenSheetIndex, true);

        Name namedCell = workbook.createName();
        namedCell.setNameName(hiddenSheetName);
        namedCell.setRefersToFormula(hiddenSheetName.concat("!$A$1:$A$" + data.size()));
        XSSFDataValidationHelper userRoleDataValidationHelper = new XSSFDataValidationHelper(sheet);
        XSSFDataValidationConstraint userRoleConstraint = (XSSFDataValidationConstraint) userRoleDataValidationHelper
                .createFormulaListConstraint(hiddenSheetName);
        CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
        XSSFDataValidation userStatusValidation = (XSSFDataValidation) userRoleDataValidationHelper
                .createValidation(userRoleConstraint, addressList);
        sheet.addValidationData(userStatusValidation);
    }

    public static void setCell(final Row row, final int cellIndex, final Object cellValue, final CellStyle cellStyle) {
        Cell cell = row.createCell(cellIndex);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(cellValue.toString());
    }

    public static String getCellValue(final XSSFWorkbook workbook, final Row row, final int cellIndex) {
        Cell cell = row.getCell(cellIndex);

        if (cell.getCellType().equals(CellType.FORMULA)) {
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            return getCellValue(cell, evaluator.evaluateFormulaCell(cell));
        }

        return getCellValue(cell, cell.getCellType());
    }

    public static String getCellValue(final Cell cell, final CellType cellType) {
        return switch (cellType) {
            case NUMERIC -> {
                Object numeric = cell.getNumericCellValue();
                String doubleStr = new BigDecimal(numeric.toString()).toPlainString();
                yield doubleStr.endsWith(".0") ? doubleStr.replace(".0", "") : doubleStr;
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());

            default -> cell.getStringCellValue();
        };
    }

    public static ResponseEntity<Resource> write(final XSSFWorkbook workbook, final String fileName) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

        ByteArrayResource byteArrayResource = new ByteArrayResource(outputStream.toByteArray());

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(byteArrayResource.contentLength())
                .body(byteArrayResource);
    }

}
