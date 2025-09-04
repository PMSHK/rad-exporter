package com.radiation.exporter.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelTemplateProcessor {
    /**
     * Заменяет метку в книге на значение
     */
    public void replacePlaceholder(Workbook workbook, String placeholder, String value) {
        Pattern pattern = Pattern.compile("\\$" + Pattern.quote(placeholder) + "\\$");

        for (Sheet sheet : workbook) {
            for (Row row : sheet) {
                for (Cell cell : row) {
                    if (cell.getCellType() == CellType.STRING) {
                        String cellValue = cell.getStringCellValue();
                        Matcher matcher = pattern.matcher(cellValue);

                        if (matcher.find()) {
                            String newValue = cellValue.replace("$" + placeholder + "$", value);
                            cell.setCellValue(newValue);
                        }
                    }
                }
            }
        }
    }

    public void replacePlaceholders(Workbook workbook, Map<String, Object> replacements) {
        for (Map.Entry<String, Object> entry : replacements.entrySet()) {
            replacePlaceholder(workbook, entry.getKey(), entry.getValue().toString());
        }
    }

    /**
     * Заменяет метку в именованном диапазоне
     */
    public void replaceInNamedRange(Workbook workbook, String rangeName, String placeholder, String value) {
        Name namedRange = workbook.getName(rangeName);
        if (namedRange != null) {
            AreaReference areaRef = new AreaReference(namedRange.getRefersToFormula(),
                    workbook.getSpreadsheetVersion());
            // ... обработка диапазона
        }
    }

    /**
     * Находит все ячейки с меткой
     */
    public List<Cell> findCellsWithPlaceholder(Workbook workbook, String placeholder) {
        List<Cell> cells = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\$" + Pattern.quote(placeholder) + "\\$");

        for (Sheet sheet : workbook) {
            for (Row row : sheet) {
                for (Cell cell : row) {
                    if (cell.getCellType() == CellType.STRING &&
                            pattern.matcher(cell.getStringCellValue()).find()) {
                        cells.add(cell);
                    }
                }
            }
        }
        return cells;
    }

    public void insertAndFillNamedRange(Workbook workbook, String rangeName, String newRangeName, Map<String, Object> replacements) {
        Name sourceRange = workbook.getName(rangeName);
        if (sourceRange == null) {
            throw new IllegalArgumentException("Range was not found");
        }
        AreaReference sourceArea = new AreaReference(
                sourceRange.getRefersToFormula(),
                workbook.getSpreadsheetVersion()
        );
    }

    private void copyNamedRangeWithOffset(Workbook workbook, String rangeName, String newRangeName
            , int rowOffset, int colOffset,
                                          Map<String, Object> replacements) {
        Name sourceRange = workbook.getName(rangeName);
        if (sourceRange == null) {
            throw new IllegalArgumentException("Range was not found");
        }
        AreaReference sourceArea = new AreaReference(
                sourceRange.getRefersToFormula(),
                workbook.getSpreadsheetVersion()
        );
        CellReference firstCell = sourceArea.getFirstCell();
        CellReference lastCell = sourceArea.getLastCell();
        Sheet sheet = workbook.getSheet(firstCell.getSheetName());
        if (sheet == null) {
            throw new IllegalArgumentException("Sheet not found: " + firstCell.getSheetName());
        }
        int sourceStartRow = firstCell.getRow();
        int sourceStartCol = firstCell.getCol();
        int targetStartRow = sourceStartRow + rowOffset;
        int targetStartCol = sourceStartCol + colOffset;
        int rowCount = lastCell.getRow() - sourceStartRow + 1;
        int colCount = lastCell.getCol() - sourceStartCol + 1;

        if (isOverlapping(sourceStartRow, sourceStartCol, rowCount, colCount,
                targetStartRow, targetStartCol)) {
            throw new IllegalArgumentException("Source and target ranges overlap!");
        }

        copyRangeContent(sheet, sourceStartRow, sourceStartCol,
                targetStartRow, targetStartCol, rowCount, colCount);

        createNamedRange(workbook, newRangeName, sheet,
                targetStartCol, targetStartRow, colCount, rowCount);
    }



    private void copyRangeContent(Sheet sheet, int sourceStartRow, int sourceStartCol,
                                  int targetStartRow, int targetStartCol,
                                  int rowCount, int colCount) {

        for (int i = 0; i < rowCount; i++) {
            Row sourceRow = sheet.getRow(sourceStartRow + i);
            if (sourceRow == null) continue;

            Row targetRow = getOrCreateRow(sheet, targetStartRow + i);
            targetRow.setHeight(sourceRow.getHeight()); // Сохраняем высоту

            for (int j = 0; j < colCount; j++) {
                Cell sourceCell = sourceRow.getCell(sourceStartCol + j);
                if (sourceCell == null) continue;

                Cell targetCell = getOrCreateCell(targetRow, targetStartCol + j);
                copyCellContentAndStyle(sourceCell, targetCell);
            }
        }

        // Копируем объединенные области
        copyMergedRegions(sheet, sourceStartRow, sourceStartCol,
                targetStartRow, targetStartCol, rowCount, colCount);
    }

    private Row getOrCreateRow(Sheet sheet, int rowIndex) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }
        return row;
    }

    private Cell getOrCreateCell(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex);
        if (cell == null) {
            cell = row.createCell(columnIndex);
        }
        return cell;
    }

    private boolean isOverlapping(int srcRow, int srcCol, int srcRows, int srcCols,
                                  int destRow, int destCol) {
        return (destRow >= srcRow && destRow < srcRow + srcRows) ||
                (destCol >= srcCol && destCol < srcCol + srcCols);
    }

    private void copyMergedRegions(Sheet sheet, int sourceStartRow, int sourceStartCol,
                                   int targetStartRow, int targetStartCol,
                                   int rowCount, int colCount) {

        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress mergedRegion = sheet.getMergedRegion(i);

            // Проверяем, входит ли merged region в наш диапазон
            if (mergedRegion.getFirstRow() >= sourceStartRow &&
                    mergedRegion.getLastRow() <= sourceStartRow + rowCount - 1 &&
                    mergedRegion.getFirstColumn() >= sourceStartCol &&
                    mergedRegion.getLastColumn() <= sourceStartCol + colCount - 1) {

                // Создаем новый merged region со смещением
                CellRangeAddress newMergedRegion = new CellRangeAddress(
                        targetStartRow + (mergedRegion.getFirstRow() - sourceStartRow),
                        targetStartRow + (mergedRegion.getLastRow() - sourceStartRow),
                        targetStartCol + (mergedRegion.getFirstColumn() - sourceStartCol),
                        targetStartCol + (mergedRegion.getLastColumn() - sourceStartCol)
                );

                sheet.addMergedRegion(newMergedRegion);
            }
        }
    }

    private void createNamedRange(Workbook workbook, String rangeName, Sheet sheet,
                                  int startCol, int startRow, int colCount, int rowCount) {

        CellReference firstCell = new CellReference(sheet.getSheetName(),
                startRow, startCol, false, false);
        CellReference lastCell = new CellReference(sheet.getSheetName(),
                startRow + rowCount - 1, startCol + colCount - 1, false, false);

        String formula = firstCell.formatAsString() + ":" + lastCell.formatAsString();

        Name newNamedRange = workbook.createName();
        newNamedRange.setNameName(rangeName);
        newNamedRange.setRefersToFormula(formula);
    }

    public void copyNamedRangeWithContent(Workbook workbook, String sourceRangeName,
                                          String newRangeName, int rowOffset, int colOffset,
                                          Map<String, Object> data) {

        // 1. Находим исходный диапазон
        Name sourceRange = workbook.getName(sourceRangeName);
        if (sourceRange == null) {
            throw new IllegalArgumentException("Range was not found: " + sourceRangeName);
        }

        // 2. Получаем координаты
        AreaReference sourceArea = new AreaReference(
                sourceRange.getRefersToFormula(),
                workbook.getSpreadsheetVersion()
        );

        CellReference firstCell = sourceArea.getFirstCell();
        CellReference lastCell = sourceArea.getLastCell();
        Sheet sheet = workbook.getSheet(firstCell.getSheetName());

        // 3. Смещаем координаты
        int newFirstRow = firstCell.getRow() + rowOffset;
        int newFirstCol = firstCell.getCol() + colOffset;
        int rowCount = lastCell.getRow() - firstCell.getRow() + 1;
        int colCount = lastCell.getCol() - firstCell.getCol() + 1;

        // 4. Копируем содержимое и форматирование
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                Cell sourceCell = sheet.getRow(firstCell.getRow() + i)
                        .getCell(firstCell.getCol() + j);

                Row newRow = sheet.getRow(newFirstRow + i);
                if (newRow == null) {
                    newRow = sheet.createRow(newFirstRow + i);
                }

                Cell newCell = newRow.createCell(newFirstCol + j);

                // Копируем ВСЁ: значение, стиль, формулу
//                copyCellAndReplace(workbook, sourceCell, newCell,);
            }
        }

        // 5. Создаем новый именованный диапазон
        createNamedRange(workbook, newRangeName, sheet,
                newFirstCol, newFirstRow, colCount, rowCount);
    }

    private void copyCellAndReplace(Workbook workbook, Cell sourceCell, Cell targetCell, Map<String, Object> values) {
        targetCell.setCellStyle(sourceCell.getCellStyle());

        if (sourceCell.getCellType() == CellType.STRING) {
            String text = sourceCell.getStringCellValue();
//            String replaced = replacePlaceholders(workbook, values);
            replacePlaceholders(workbook, values);
//            targetCell.setCellValue(replaced);
        } else {
            copyCellContentAndStyle(sourceCell, targetCell);
        }
    }

    private void copyCellContentAndStyle(Cell sourceCell, Cell targetCell) {
        // 1. Копируем стиль (включая границы!)
        targetCell.setCellStyle(sourceCell.getCellStyle());

        // 2. Копируем значение в зависимости от типа
        switch (sourceCell.getCellType()) {
            case STRING:
                targetCell.setCellValue(sourceCell.getStringCellValue());
                break;
            case NUMERIC:
                targetCell.setCellValue(sourceCell.getNumericCellValue());
                break;
            case BOOLEAN:
                targetCell.setCellValue(sourceCell.getBooleanCellValue());
                break;
            case FORMULA:
                targetCell.setCellFormula(sourceCell.getCellFormula());
                break;
            case BLANK:
                targetCell.setBlank();
                break;
            case ERROR:
                targetCell.setCellErrorValue(sourceCell.getErrorCellValue());
                break;
        }

        // 3. Копируем ширину колонки (опционально)
        Sheet sheet = sourceCell.getSheet();
        int columnIndex = sourceCell.getColumnIndex();
        int sourceWidth = sheet.getColumnWidth(columnIndex);
        sheet.setColumnWidth(targetCell.getColumnIndex(), sourceWidth);
    }
}
