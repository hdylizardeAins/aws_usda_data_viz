package com.aws.codestar.projecttemplates.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.text.WordUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.aws.codestar.projecttemplates.exception.IncompatibleColumnValuesException;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.RowSortedTable;
import com.google.common.collect.Sets;
import com.google.common.collect.TreeBasedTable;

/**
 * Provides utility methods for converting excel and csv files into Guava Tables
 * and methods to merge and convert the tables into csv string
 *
 * @author ccruz-rodriguez
 *
 */
public class FileUtils {

	private static final String THE_PIVOT_COLUMN_CONTAIN_MORE_THAN_ONE_DATA_TYPE = "The pivot column contain more than one data type.";
	private static final String THE_VALUE_COLUMN_CONTAIN_MORE_THAN_ONE_DATA_TYPE = "The value column contain more than one data type.";
	private static final String EMPTY_STRING = "";
	private static final String NON_ALPHANUMBERIC_REGEX = "[^A-Za-z0-9]";
	private static final char OUTPUT_CSV_COMMA_DELIMITER = ',';

	private final static ByteOrderMark[] EXCLUSION_BOM_ARR = new ByteOrderMark[] { ByteOrderMark.UTF_8,
			ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_32BE, ByteOrderMark.UTF_32LE };
	private static final char LF = '\n';

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
		Set<String> differencePivotSet = !graph1.isEmpty() && !graph2.isEmpty()
				? Sets.symmetricDifference(graph1.rowKeySet(), graph2.rowKeySet())
				: Collections.emptySet();

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
		final CSVPrinter printer = CSVFormat.DEFAULT.withDelimiter(OUTPUT_CSV_COMMA_DELIMITER).withRecordSeparator(LF)
				.print(out);

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
	 * Generates a map containing the column Id for its respective header column.
	 *
	 * @param dataFormatter formatter for the header column's values
	 * @param headerRow     the row containing the header
	 * @return a map containing the column Id for each header column.
	 */
	private static Map<String, Integer> generateHeaderColumnIndexMap(DataFormatter dataFormatter, Row headerRow) {
		// Retrieve the header row and map each header column to its column index
		Map<String, Integer> headerToCol = new HashMap<>();
		Iterator<Cell> cells = headerRow.cellIterator();
		while (cells.hasNext()) {
			Cell cell = cells.next();
			String value = dataFormatter.formatCellValue(cell);
			int colIdx = cell.getColumnIndex();
			headerToCol.put(value, colIdx);
		}
		return headerToCol;
	}

	/**
	 * Retrieves the distinct values contained in the column of a excel spreadsheet
	 *
	 * @param filePath       the path to the excel file
	 * @param excelSheetName the name of the sheet which contains the column
	 * @param column         the column to retrieve the values from
	 * @param filters        the filters to apply to the excel file
	 * @return a set containing the possible values of the column
	 * @throws IOException if there is an error reading the excel file
	 */
	public static Set<String> retrieveExcelColumnValues(String filePath, String excelSheetName, String column,
			Map<String, List<String>> filters) throws IOException {

		// TODO Determine if the file is excel and if not throw exception
		final Set<String> values = new TreeSet<String>();
		if (filePath == null) {
			return values;
		}

		try (InputStream stream = org.apache.commons.io.FileUtils.openInputStream(new File(filePath));
				XSSFWorkbook wb = new XSSFWorkbook(stream);) {

			XSSFSheet sheet = wb.getSheet(excelSheetName);

			// Determine where the first row of data begins
			int headerRowIdx = sheet.getFirstRowNum();
			int lastRowIdx = sheet.getLastRowNum();
			int firstRowIdx = headerRowIdx < lastRowIdx ? headerRowIdx + 1 : lastRowIdx;

			DataFormatter dataFormatter = new DataFormatter();

			// Retrieve the header row and the column index of the selected column
			Row headerRow = sheet.getRow(headerRowIdx);

			Map<String, Integer> headerToCol = generateHeaderColumnIndexMap(dataFormatter, headerRow);

			int columnId = headerToCol.getOrDefault(column, -1);
			if (columnId != -1) {

				// Process each row
				for (int r = firstRowIdx; r <= lastRowIdx; r++) {
					XSSFRow row = sheet.getRow(r);

					boolean keepRow = shouldKeepExcelRow(row, filters, headerToCol, dataFormatter);

					// If row is to be kept, add it to the graph
					if (keepRow) {
						Cell cell = row.getCell(columnId);
						String cellValue = dataFormatter.formatCellValue(cell);
						if (StringUtils.isNotBlank(cellValue)) {
							values.add(cellValue.trim());
						}
					}
				}
			}
		}

		return values;
	}

	/**
	 * Determines if the row should be kept based on the collection of filters to be
	 * applied to each column of the row
	 *
	 * @param row           the row to check
	 * @param filters       the filters to use for determining if the row should be
	 *                      kept
	 * @param headerToCol   the map of headers to column ids
	 * @param dataFormatter formatter for the values in the cells
	 * @return True if the row should be kept, otherwise False
	 */
	private static boolean shouldKeepExcelRow(XSSFRow row, Map<String, List<String>> filters,
			Map<String, Integer> headerToCol, DataFormatter dataFormatter) {
		boolean keepRow = true;
		// Determine if the row should be kept based on the filters
		for (Map.Entry<String, List<String>> entry : filters.entrySet()) {
			String columnFilter = entry.getKey();
			List<String> criteria = entry.getValue();
			int columnFilterId = headerToCol.get(columnFilter);
			Cell cell = row.getCell(columnFilterId);
			String cellValue = dataFormatter.formatCellValue(cell);
			if (!criteria.contains(cellValue.trim())) {
				keepRow = false;
				break;
			}
		}
		return keepRow;
	}

	/**
	 * Transforms the content of a excel file into a table graph based on the
	 * filters and the key structured specified.
	 *
	 * @param filePath       the path to the excel file
	 * @param excelSheetName the name of the sheet containing the data to convert
	 * @param rowKey         the column to use as the row key
	 * @param columnKey      the column to use for the column key
	 * @param valueColumn    the column to use for the value
	 * @param filters        the filters to apply to the excel file
	 * @return a filtered Table graph
	 * @throws IOException                       if there is an error reading the
	 *                                           excel file
	 * @throws IncompatibleColumnValuesException if an multiple value types were
	 *                                           found for a column
	 */
	public static RowSortedTable<String, String, String> excelToGraph(String filePath, String excelSheetName,
			String rowKey, String columnKey, String valueColumn, Map<String, List<String>> filters)
			throws IOException, IncompatibleColumnValuesException {

		// TODO Determine if the file is excel and if not throw exception
		final RowSortedTable<String, String, String> graph = TreeBasedTable.create();
		if (filePath == null) {
			return graph;
		}

		try (InputStream stream = org.apache.commons.io.FileUtils.openInputStream(new File(filePath));
				XSSFWorkbook wb = new XSSFWorkbook(stream);) {

			XSSFSheet sheet = wb.getSheet(excelSheetName);

			// Determine where the first row of data begins
			int headerRowIdx = sheet.getFirstRowNum();
			int lastRowIdx = sheet.getLastRowNum();
			int firstRowIdx = headerRowIdx < lastRowIdx ? headerRowIdx + 1 : lastRowIdx;

			DataFormatter dataFormatter = new DataFormatter();

			// Retrieve the header row and map each header column to its column index
			Row headerRow = sheet.getRow(headerRowIdx);
			Map<String, Integer> headerToCol = generateHeaderColumnIndexMap(dataFormatter, headerRow);

			int pivotId = headerToCol.get(rowKey);
			int columnId = headerToCol.get(columnKey);
			int valueId = headerToCol.get(valueColumn);

			// Process each row

			for (int r = firstRowIdx; r <= lastRowIdx; r++) {
				XSSFRow row = sheet.getRow(r);

				boolean keepRow = shouldKeepExcelRow(row, filters, headerToCol, dataFormatter);

				// If row is to be kept, add it to the graph
				if (keepRow) {

					Cell pivotCell = row.getCell(pivotId);
					String pivotCellStr = dataFormatter.formatCellValue(pivotCell);

					Cell columnCell = row.getCell(columnId);
					String columnCellStr = dataFormatter.formatCellValue(columnCell);

					Cell valueCell = row.getCell(valueId);
					String valueCellStr = dataFormatter.formatCellValue(valueCell);

					graph.put(pivotCellStr, columnCellStr, valueCellStr);
				}
			}

			for (String column : graph.columnKeySet()) {
				long valueTypes = graph.column(column).values().stream().map(NumberUtils::isCreatable).distinct()
						.count();
				if (valueTypes > 1) {
					throw new IncompatibleColumnValuesException(THE_VALUE_COLUMN_CONTAIN_MORE_THAN_ONE_DATA_TYPE);
				}
			}

			long pivotValueTypes = graph.rowKeySet().stream().map(NumberUtils::isCreatable).distinct().count();
			if (pivotValueTypes > 1) {
				throw new IncompatibleColumnValuesException(THE_PIVOT_COLUMN_CONTAIN_MORE_THAN_ONE_DATA_TYPE);
			}

		}
		return graph;
	}

	/**
	 * Transforms the content of a excel file into csv format
	 *
	 * @param filePath       the path to the excel file
	 * @param excelSheetName the name of the sheet containing the data to convert
	 * @return a string builder containing the csv data
	 * @throws IOException if there is an error reading the excel file
	 */
	public static StringBuilder excelToCSV(String filePath, String excelSheetName) throws IOException {

		if (filePath == null) {
			return null;
		}

		return excelToCSV(org.apache.commons.io.FileUtils.openInputStream(new File(filePath)), excelSheetName);
	}

	/**
	 * Transforms the content of a excel file into csv format
	 *
	 * @param excelInputStream the input stream containing the excel file
	 * @param excelSheetName   the name of the sheet containing the data to convert
	 * @return a string builder containing the csv data
	 * @throws IOException if there is an error reading the excel file
	 */
	public static StringBuilder excelToCSV(InputStream excelInputStream, String excelSheetName) throws IOException {

		if (excelInputStream == null) {
			return null;
		}

		// Build a csv object from the graph
		final StringBuilder out = new StringBuilder();

		try (InputStream stream = excelInputStream;
				XSSFWorkbook wb = new XSSFWorkbook(stream);
				CSVPrinter printer = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL)
						.withDelimiter(OUTPUT_CSV_COMMA_DELIMITER).withNullString(EMPTY_STRING).withRecordSeparator(LF)
						.print(out);) {
			XSSFSheet sheet = wb.getSheet(excelSheetName);
			if (sheet == null) {
				sheet = wb.getSheetAt(0);
			}
			DataFormatter dataFormatter = new DataFormatter();
			for (Row row : sheet) {
				// Add data to csv
				List<String> values = new LinkedList<>();
				for (int cellnum = 0; cellnum < row.getLastCellNum(); cellnum++) {
					Cell cell = row.getCell(cellnum);
					String cellValue = dataFormatter.formatCellValue(cell);
					values.add(cellValue);
				}

				printer.printRecord(values);
			}
		}
		return out;
	}

	public static StringBuilder xlsToCSV(InputStream excelInputStream, String excelSheetName) throws IOException {
		if (excelInputStream == null) {
			return null;
		}

		// Build a csv object from the graph
		final StringBuilder out = new StringBuilder();

		try (InputStream stream = excelInputStream;
				HSSFWorkbook wb = new HSSFWorkbook(stream);
				CSVPrinter printer = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL)
						.withDelimiter(OUTPUT_CSV_COMMA_DELIMITER).withNullString(EMPTY_STRING).withRecordSeparator(LF)
						.print(out);) {
			HSSFSheet sheet = wb.getSheet(excelSheetName);
			if (sheet == null) {
				sheet = wb.getSheetAt(0);
			}
			DataFormatter dataFormatter = new DataFormatter();
			for (Row row : sheet) {
				// Add data to csv
				List<String> values = new LinkedList<>();
				for (int cellnum = 0; cellnum < row.getLastCellNum(); cellnum++) {
					Cell cell = row.getCell(cellnum);
					String cellValue = dataFormatter.formatCellValue(cell);
					values.add(cellValue);
				}

				printer.printRecord(values);
			}
		}
		return out;
	}

	/**
	 * Retrieves the distinct values contained in the column of a csv file
	 *
	 * @param filePath the path to the csv file
	 * @param column   the column to retrieve the values from
	 * @param filters  the filters to apply to the csv file
	 * @return a set containing the possible values of the column
	 * @throws IOException if there is an error reading the csv file
	 */
	public static Set<String> retrieveCSVColumnValues(String filePath, String column, Map<String, List<String>> filters)
			throws IOException {

		// TODO Determine if the file is csv and if not throw exception
		final Set<String> values = new TreeSet<String>();
		if (filePath == null) {
			return values;
		}
		try (InputStream stream2 = org.apache.commons.io.FileUtils.openInputStream(new File(filePath));
				Reader reader = new InputStreamReader(new BOMInputStream(stream2, false, EXCLUSION_BOM_ARR));
				CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());) {

			// Retrieve csv records
			Iterable<CSVRecord> records = parser.getRecords();

			// Process each record
			for (CSVRecord record : records) {

				boolean keepRecord = shouldKeepCSVRecord(record, filters);
				if (keepRecord) {
					String columnValue = record.get(column);
					if (StringUtils.isNotBlank(columnValue)) {
						values.add(columnValue.trim());
					}
				}
			}
		}
		return values;
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
	 * @throws IOException                       if there is an error reading the
	 *                                           CSV file
	 * @throws IncompatibleColumnValuesException if an multiple value types were
	 *                                           found for a column
	 */
	public static RowSortedTable<String, String, String> csvToGraph(String filePath, String rowKey, String columnKey,
			String valueColumn, Map<String, List<String>> filters)
			throws IOException, IncompatibleColumnValuesException {
		// TODO Determine if the file is csv and if not throw exception
		final RowSortedTable<String, String, String> graph2 = TreeBasedTable.create();
		if (filePath == null) {
			return graph2;
		}
		try (InputStream stream2 = org.apache.commons.io.FileUtils.openInputStream(new File(filePath));
				Reader reader = new InputStreamReader(new BOMInputStream(stream2, false, EXCLUSION_BOM_ARR));
				CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());) {

			// Retrieve csv records
			Iterable<CSVRecord> records = parser.getRecords();

			// Process each record
			for (CSVRecord record : records) {
				boolean keepRecord = shouldKeepCSVRecord(record, filters);

				// If record is to be kept, add it to the graph
				if (keepRecord) {
					String pivotStr = record.get(rowKey);
					String columnStr = record.get(columnKey);
					String valueStr = record.get(valueColumn);
					graph2.put(pivotStr, columnStr, valueStr);
				}
			}

			for (String column : graph2.columnKeySet()) {
				long valueTypes = graph2.column(column).values().stream().map(NumberUtils::isCreatable).distinct()
						.count();
				if (valueTypes > 1) {
					throw new IncompatibleColumnValuesException(THE_VALUE_COLUMN_CONTAIN_MORE_THAN_ONE_DATA_TYPE);
				}
			}

			long pivotValueTypes = graph2.rowKeySet().stream().map(NumberUtils::isCreatable).distinct().count();
			if (pivotValueTypes > 1) {
				throw new IncompatibleColumnValuesException(THE_PIVOT_COLUMN_CONTAIN_MORE_THAN_ONE_DATA_TYPE);
			}
		}
		return graph2;
	}

	/**
	 * Determines if the record should be kept based on the collection of filters to
	 * be applied to each column of the record
	 *
	 * @param record  the record to check
	 * @param filters the filters to use for determining if the record should be
	 *                kept
	 * @return True if the record should be kept, otherwise False
	 */
	private static boolean shouldKeepCSVRecord(CSVRecord record, Map<String, List<String>> filters) {
		boolean keepRecord = true;

		// Determine if the record should be kept based on the filters
		for (Map.Entry<String, List<String>> entry : filters.entrySet()) {
			String column = entry.getKey();
			List<String> criteria = entry.getValue();
			String value = record.get(column);
			if (value == null || !criteria.contains(value.trim())) {
				keepRecord = false;
				break;
			}
		}
		return keepRecord;
	}
}
