package com.aws.codestar.projecttemplates.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.text.WordUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.RowSortedTable;
import com.google.common.collect.Sets;
import com.google.common.collect.TreeBasedTable;
import java.io.InputStreamReader;

/**
 * Provides utility methods for converting excel and csv files into Guava Tables
 * and methods to merge and convert the tables into csv string
 *
 * @author ccruz-rodriguez
 *
 */
public class FileUtils {

	private static final String EMPTY_STRING = "";
	private static final String NON_ALPHANUMBERIC_REGEX = "[^A-Za-z0-9]";
	private static final String EXCEL_SHEET_NAME = "Data Sheet (machine readable)";
	private static final String EXCEL_FILE = "CornCostReturn.xlsx";
	private static final char OUTPUT_CSV_DELIMITER = ',';
	private static final String CSV_FILE = "alltablesGEcrops.csv";
	private final static ByteOrderMark[] EXCLUSION_BOM_ARR = new ByteOrderMark[] { ByteOrderMark.UTF_8,
			ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_32BE, ByteOrderMark.UTF_32LE };

	/**
	 * Example on how to use the methods of the class
	 *
	 * @param args arguments to the program
	 */
	public static void main(String[] args) {

		try {

			// TODO Retrieve files to merge from the client as well as the pivot and the
			// filter information
			String excelPivotIdx = "Year";
			String excelColumnIdx = "Item2";
			String excelValueIdx = "Value";

			Map<String, List<String>> excelFilters = new HashMap<>();
			excelFilters.put("Region", Arrays.asList("U.S. total"));
			excelFilters.put("Item2",
					Arrays.asList("Total, operating costs", "Seed", "Total, gross value of production"));

			final RowSortedTable<String, String, String> excelGraph = FileUtils.excelToGraph(EXCEL_FILE,
					EXCEL_SHEET_NAME, excelPivotIdx, excelColumnIdx, excelValueIdx, excelFilters);

			String csvPivotIdx = "Year";
			String csvColumnIdx = "Unit";
			String csvValueIdx = "Value";

			Map<String, List<String>> csvFilters = new HashMap<>();
			csvFilters.put("State", Arrays.asList("U.S."));
			csvFilters.put("Variety", Arrays.asList("All GE varieties"));
			csvFilters.put("Crop", Arrays.asList("Corn"));

			final RowSortedTable<String, String, String> csvGraph = FileUtils.csvToGraph(CSV_FILE, csvPivotIdx,
					csvColumnIdx, csvValueIdx, csvFilters);

			final RowSortedTable<String, String, String> mergedGraph = FileUtils.mergeGraphsByPivot(excelGraph,
					csvGraph);

			final StringBuilder out = FileUtils.graphToCSV(excelPivotIdx, mergedGraph);

			System.out.println(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Merges two table graphs by their pivot column
	 *
	 * @param graph1 the first graph to merge
	 * @param graph2 the second graph to merge
	 * @return the merged table graph
	 */
	public static RowSortedTable<String, String, String> mergeGraphsByPivot(
			final RowSortedTable<String, String, String> graph1, final RowSortedTable<String, String, String> graph2) {
		// Identify the graph nodes that are different between the graphs that are being
		// merged
		Set<String> differencePivotSet = Sets.symmetricDifference(graph1.rowKeySet(), graph2.rowKeySet());

		final RowSortedTable<String, String, String> merged = TreeBasedTable.create();
		// Merge the graphs
		merged.putAll(graph1);
		merged.putAll(graph2);

		// Remove the difference from the merged graph
		for (String pivot : differencePivotSet) {
			merged.row(pivot).clear();
		}

		return merged;
	}

	/**
	 * Converts a graph table into a CSV String Builder
	 *
	 * @param pivotColumn the name of the graph's pivot column
	 * @param graph       the table graph to convert
	 * @return a string builder containing the csv format
	 * @throws IOException if an error occurs printing the graph to a CSV format
	 */
	public static StringBuilder graphToCSV(String pivotColumn, final RowSortedTable<String, String, String> graph)
			throws IOException {

		// Build a csv object from the graph
		final StringBuilder out = new StringBuilder();
		final CSVPrinter printer = CSVFormat.DEFAULT.withDelimiter(OUTPUT_CSV_DELIMITER).print(out);

		// Make header for the csv file
		LinkedHashSet<String> sorted = new LinkedHashSet<String>();
		sorted.add(pivotColumn);
		//
		List<String> otherColumns = graph.columnKeySet().stream().sequential().map(s -> WordUtils.capitalize(s))
				.map(s -> s.replaceAll(NON_ALPHANUMBERIC_REGEX, EMPTY_STRING)).collect(Collectors.toList());
		sorted.addAll(otherColumns);
		printer.printRecord(sorted);
		// Add data to csv
		printer.printRecords(graph.rowMap().entrySet().stream()
				.map(entry -> ImmutableList.builder().add(entry.getKey()).addAll(entry.getValue().values()).build())
				.collect(Collectors.toList()));
		printer.close();
		return out;
	}

	/**
	 * Transforms the content of a excel file into a table graph based on the
	 * filters and the key structured specified.
	 *
	 * @param filePath    the path to the excel file
	 * @param rowKey      the column to use as the row key
	 * @param columnKey   the column to use for the column key
	 * @param valueColumn the column to use for the value
	 * @param filters     the filters to apply to the excel file
	 * @return a filtered Table graph
	 * @throws IOException if there is an error reading the excel file
	 */
	public static RowSortedTable<String, String, String> excelToGraph(String filePath, String excelSheetName,
			String rowKey, String columnKey, String valueColumn, Map<String, List<String>> filters) throws IOException {

		// TODO Determine if the file is excel and if not throw exception
		final RowSortedTable<String, String, String> graph = TreeBasedTable.create();
		try (InputStream stream = FileUtils.class.getClassLoader().getResourceAsStream(filePath);
				XSSFWorkbook wb = new XSSFWorkbook(stream);) {

			XSSFSheet sheet = wb.getSheet(excelSheetName);

			// Determine where the first row of data begins
			int headerRowIdx = sheet.getFirstRowNum();
			int lastRowIdx = sheet.getLastRowNum();
			int firstRowIdx = headerRowIdx < lastRowIdx ? headerRowIdx + 1 : lastRowIdx;

			DataFormatter dataFormatter = new DataFormatter();

			// Retrieve the header row and map each header column to its column index
			Row headerRow = sheet.getRow(headerRowIdx);
			Map<String, Integer> headerToCol = new HashMap<>();
			Iterator<Cell> cells = headerRow.cellIterator();
			while (cells.hasNext()) {
				Cell cell = cells.next();
				String value = dataFormatter.formatCellValue(cell);
				int colIdx = cell.getColumnIndex();
				headerToCol.put(value, colIdx);
			}

			// Process each row
			for (int r = firstRowIdx; r <= lastRowIdx; r++) {
				XSSFRow row = sheet.getRow(r);
				boolean keepRow = true;

				// Determine if the row should be kept based on the filters
				for (Map.Entry<String, List<String>> entry : filters.entrySet()) {
					String column = entry.getKey();
					List<String> criteria = entry.getValue();
					int columnId = headerToCol.get(column);
					Cell cell = row.getCell(columnId);
					String cellValue = dataFormatter.formatCellValue(cell);
					if (!criteria.contains(cellValue)) {
						keepRow = false;
						break;
					}
				}

				// If row is to be kept, add it to the graph
				if (keepRow) {

					int pivotId = headerToCol.get(rowKey);
					Cell pivotCell = row.getCell(pivotId);
					String pivotCellStr = dataFormatter.formatCellValue(pivotCell);

					int columnId = headerToCol.get(columnKey);
					Cell columnCell = row.getCell(columnId);
					String columnCellStr = dataFormatter.formatCellValue(columnCell);

					int valueId = headerToCol.get(valueColumn);
					Cell valueCell = row.getCell(valueId);
					String valueCellStr = dataFormatter.formatCellValue(valueCell);

					graph.put(pivotCellStr, columnCellStr, valueCellStr);
				}
			}
		}
		return graph;
	}

	/**
	 * Transforms the content of a CSV file into a table graph based on the filters
	 * and the key structured specified.
	 *
	 * @param filePath    the path to the csv file
	 * @param rowKey      the column to use as the row key
	 * @param columnKey   the column to use for the column key
	 * @param valueColumn the column to use for the value
	 * @param filters     the filters to apply to the CSV file
	 * @return a filtered Table graph
	 * @throws IOException if there is an error reading the CSV file
	 */
	public static RowSortedTable<String, String, String> csvToGraph(String filePath, String rowKey, String columnKey,
			String valueColumn, Map<String, List<String>> filters) throws IOException {
		// TODO Determine if the file is csv and if not throw exception
		final RowSortedTable<String, String, String> graph2 = TreeBasedTable.create();
		try (InputStream stream2 = FileUtils.class.getClassLoader().getResourceAsStream(filePath);
				Reader reader = new InputStreamReader(new BOMInputStream(stream2, false, EXCLUSION_BOM_ARR));
				CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());) {

			// Retrieve csv records
			Iterable<CSVRecord> records = parser.getRecords();

			// Process each record
			for (CSVRecord record : records) {
				boolean keepRecord = true;

				// Determine if the record should be kept based on the filters
				for (Map.Entry<String, List<String>> entry : filters.entrySet()) {
					String column = entry.getKey();
					List<String> criteria = entry.getValue();
					String value = record.get(column);
					if (!criteria.contains(value)) {
						keepRecord = false;
						break;
					}
				}

				// If record is to be kept, add it to the graph
				if (keepRecord) {
					String pivotStr = record.get(rowKey);
					String columnStr = record.get(columnKey);
					String valueStr = record.get(valueColumn);
					graph2.put(pivotStr, columnStr, valueStr);
				}
			}
		}
		return graph2;
	}
}
