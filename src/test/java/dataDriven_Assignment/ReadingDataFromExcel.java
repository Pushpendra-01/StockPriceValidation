package dataDriven_Assignment;

import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;

import java.util.HashMap;
import java.util.Map;

public class ReadingDataFromExcel {

	public static Map<String, String> readExcelData(String filePath, String sheetName) {
        Map<String, String> dataMap = new HashMap<>();

        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(fileInputStream);
            Sheet sheet = workbook.getSheet(sheetName);

            // Iterate through rows and columns to read data
            for (Row row : sheet) {
                // Assuming the key is in the first column and value is in the second column
                Cell keyCell = row.getCell(0);
                Cell valueCell = row.getCell(3);

                if (keyCell != null && valueCell != null) {
                    String key = getKeyCellValue(keyCell);
                    String value = getKeyCellValue(valueCell);
                    dataMap.put(key, value);
                }
            }
        } catch (Exception e) {
			e.printStackTrace();
        }

        return dataMap;
    }

    private static String getKeyCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                // Handle numeric cells
                return String.valueOf(cell.getNumericCellValue());
            default:
                return "";
        }
    }

	/*
	 * private static String getValueCellValue(Cell cell) { switch
	 * (cell.getCellType()) { case STRING: return cell.getStringCellValue(); case
	 * NUMERIC: // Handle numeric cells return
	 * String.valueOf(cell.getNumericCellValue()); default: return ""; } }
	 */
    public static void main(String[] args) {
        // Provide the file path to your Excel file
        String filePath = System.getProperty("user.dir") + "//src//test//java//resource//dataDriven.xlsx";
        // Provide the name of the sheet containing the data
        String sheetName = "Sheet1";

        // Call the method to read Excel data into a HashMap
        Map<String, String> excelData = readExcelData(filePath, sheetName);

        // Print the HashMap
        for (Map.Entry<String, String> entry : excelData.entrySet()) {
            System.out.println( entry.getKey() + ":" + entry.getValue());
        }
    }
}

