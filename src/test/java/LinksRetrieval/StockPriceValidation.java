package LinksRetrieval;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockPriceValidation {

	static WebDriver driver;

	public static void setUp() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.get("https://money.rediff.com/losers/bse/daily/groupall?");
	}

	@Test
	public void testTableDataToExcel() {
		
		setUp();

		// Waiting for the table to load
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".dataTable")));

		// Get all rows of the table
		WebElement table = driver.findElement(By.cssSelector(".dataTable"));
		List<WebElement> rows = table.findElements(By.tagName("tr"));

		// Create Excel workbook and sheet
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet("Table Data");// setting sheet name

		// Write data to excel
		int rowNum = 0;
		for (WebElement row : rows) {
			List<WebElement> columns = row.findElements(By.tagName("td"));
			Row excelRow = sheet.createRow(rowNum++);
			int colNum = 0;
			for (WebElement column : columns) {
				excelRow.createCell(colNum++).setCellValue(column.getText());
			}
		}

		// Write Excel to file
		try {
			FileOutputStream outputStream = new FileOutputStream(new File("table_data.xls"));
			workbook.write(outputStream);
			outputStream.close();
			System.out.println("Table data has been written to table_data.xls");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Map<String, String> excelDataMap =readTheDataFromTheSheet();
		Map<String, String> webDataMap = testTableDataExtraction();
		comparingTwoHashmap(excelDataMap, webDataMap);
	}

	//reading the data from the excel sheet and storing in to one map
	public Map<String, String> readTheDataFromTheSheet() {

		// Provide the file path to your Excel file
		String filePath = System.getProperty("user.dir") + "//table_data.xls";

		// Provide the name of the sheet containing the data
		String sheetName = "Table Data";

		// Call the method to read Excel data into a HashMap
		return readExcelData(filePath, sheetName);

	}

	private Map<String, String> readExcelData(String filePath, String sheetName) {
		Map<String, String> excelDataMap = new HashMap<>();

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
					excelDataMap.put(key, value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return excelDataMap;
	}

	private String getKeyCellValue(Cell cell) {
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


	//extracting the data from the web and storing into map2 
	public  Map<String, String>  testTableDataExtraction() {

		setUp();

		// Find the table element
		WebElement table = driver.findElement(By.cssSelector(".dataTable"));

		// Initialize HashMap to store column 1 and column 4 data
		HashMap<String, String> webDataMap = new HashMap<>();

		// Find all rows of the table
		List<WebElement> rows = table.findElements(By.tagName("tr"));

		// Loop through each row
		for (WebElement row : rows) {

			// Find all columns of the row
			List<WebElement> columns = row.findElements(By.tagName("td"));

			// Check if the row has at least 4 columns
			if (columns.size() >= 4) {

				// Get data from column 1 and column 4

				String companyName = columns.get(0).getText();
				String currentPrice = columns.get(3).getText();

				// Store data into HashMap

				webDataMap.put(companyName, currentPrice);
				
			}
		}
		return webDataMap;
	}

	
	public void comparingTwoHashmap(Map<String, String> map1, Map<String, String> map2) {
		// Compare map1 and map2
		boolean areEqual = map1.entrySet().stream().allMatch(
				entry -> map2.containsKey(entry.getKey()) && map2.get(entry.getKey()).equals(entry.getValue()));

		// Print the result
		if (areEqual) {
			System.out.println("Map1 and Map2 are equal");
		} else {
			System.out.println("Map1 and Map2 are not equal");
		}
		
	}

}
