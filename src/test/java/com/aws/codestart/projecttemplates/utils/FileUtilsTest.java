package com.aws.codestart.projecttemplates.utils;

import com.aws.codestar.projecttemplates.utils.FileUtils;
import com.google.common.collect.RowSortedTable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Tests for the file utility functions
 *
 * @author ccruz-rodriguez
 *
 */
public class FileUtilsTest {

	private static final String EXCEL_SHEET_NAME = "Data Sheet (machine readable)";
	private static final String EXCEL_FILE = "CornCostReturn.xlsx";
	private static final String EXCEL_CSV_FILE = "CornCostReturnMR.csv";
	private static final String CSV_FILE = "alltablesGEcrops.csv";
	private static final String PIVOT_COLUMN = "Year";
	private static final String EXCEL_GROUP_COLUMN = "Item2";
	private static final String CSV_GROUP_COLUMN = "Unit";
	private static final String VALUE_COLUMN = "Value";
	
	private static final String PLOT_OUTPUT_DIR = "./src/main/resources/";
	
	private static final String LF = "\n";

	private static final Map<String, List<String>> EXCEL_FILTERS;
	private static final Map<String, List<String>> CSV_FILTERS;
	static {
		Map<String, List<String>> excelFilters = new HashMap<>();
		excelFilters.put("Region", Arrays.asList("U.S. total"));
		excelFilters.put("Item2", Arrays.asList("Total, operating costs", "Seed", "Total, gross value of production"));
		EXCEL_FILTERS = Collections.unmodifiableMap(excelFilters);

		Map<String, List<String>> csvFilters = new HashMap<>();
		csvFilters.put("State", Arrays.asList("U.S."));
		csvFilters.put("Variety", Arrays.asList("All GE varieties"));
		csvFilters.put("Crop", Arrays.asList("Corn"));
		CSV_FILTERS = Collections.unmodifiableMap(csvFilters);

	}

	@Test
	public void testCsvToGraph() throws IOException {
		
		String inputPath = new File(PLOT_OUTPUT_DIR, EXCEL_CSV_FILE).getAbsolutePath();

		RowSortedTable<String, String, String> csvToGraph = FileUtils.csvToGraph(inputPath, PIVOT_COLUMN,
				EXCEL_GROUP_COLUMN, VALUE_COLUMN, EXCEL_FILTERS);
		assertFalse(csvToGraph.isEmpty());
		int numberOfColumns = csvToGraph.columnKeySet().size();
		assertEquals(3, numberOfColumns);
	}

	@Test
	public void testExcelToGraph() throws IOException {
		String inputPath = new File(PLOT_OUTPUT_DIR, EXCEL_FILE).getAbsolutePath();

		RowSortedTable<String, String, String> excelToGraph = FileUtils.excelToGraph(inputPath, EXCEL_SHEET_NAME,
				PIVOT_COLUMN, EXCEL_GROUP_COLUMN, VALUE_COLUMN, EXCEL_FILTERS);
		assertFalse(excelToGraph.isEmpty());
		int numberOfColumns = excelToGraph.columnKeySet().size();
		assertEquals(3, numberOfColumns);
	}

	@Test
	public void testGraphToCsv() throws IOException {
		String inputPath = new File(PLOT_OUTPUT_DIR, EXCEL_FILE).getAbsolutePath();

		RowSortedTable<String, String, String> excelToGraph = FileUtils.excelToGraph(inputPath, EXCEL_SHEET_NAME,
				PIVOT_COLUMN, EXCEL_GROUP_COLUMN, VALUE_COLUMN, EXCEL_FILTERS);

		StringBuilder graphToCSV = FileUtils.graphToCSV(PIVOT_COLUMN, excelToGraph);

		String[] records = graphToCSV.toString().split(LF);

		assertEquals(excelToGraph.rowMap().size(), records.length - 1);

	}

	@Test
	public void testMergeGraphs() throws IOException {
		String inputPath = new File(PLOT_OUTPUT_DIR, EXCEL_CSV_FILE).getAbsolutePath();

		RowSortedTable<String, String, String> excelCsvToGraph = FileUtils.csvToGraph(inputPath, PIVOT_COLUMN,
				EXCEL_GROUP_COLUMN, VALUE_COLUMN, EXCEL_FILTERS);

		String inputPath2 = new File(PLOT_OUTPUT_DIR, CSV_FILE).getAbsolutePath();

		RowSortedTable<String, String, String> csvToGraph = FileUtils.csvToGraph(inputPath2, PIVOT_COLUMN,
				CSV_GROUP_COLUMN, VALUE_COLUMN, CSV_FILTERS);

		RowSortedTable<String, String, String> mergedGraph = FileUtils.mergeGraphsByPivot(excelCsvToGraph, csvToGraph);
		assertFalse(mergedGraph.isEmpty());

		assertTrue(csvToGraph.rowKeySet().containsAll(mergedGraph.rowKeySet()));
		assertTrue(excelCsvToGraph.rowKeySet().containsAll(mergedGraph.rowKeySet()));
	}
	
	@Test
	public void testRetrievalOfExcelColumns() throws IOException {
		String inputPath = new File(PLOT_OUTPUT_DIR, EXCEL_FILE).getAbsolutePath();

		Set<String> columns = FileUtils.retrieveExcelColumnValues(inputPath, EXCEL_SHEET_NAME, EXCEL_GROUP_COLUMN, EXCEL_FILTERS);
		assertEquals(3, columns.size());
	}
	
	@Test
	public void testRetrievalOfCsvColumns() throws IOException {
		String inputPath = new File(PLOT_OUTPUT_DIR, CSV_FILE).getAbsolutePath();

		Set<String> columns = FileUtils.retrieveCSVColumnValues(inputPath, CSV_GROUP_COLUMN, CSV_FILTERS);
		assertEquals(1, columns.size());
	}

}
