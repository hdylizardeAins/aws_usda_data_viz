package com.aws.codestar.projecttemplates.controller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aws.codestar.projecttemplates.api.DataSet;
import com.aws.codestar.projecttemplates.api.FilteredDataSet;
import com.aws.codestar.projecttemplates.utils.FileUtils;
import com.google.common.base.Charsets;
import com.google.common.collect.RowSortedTable;
import com.google.common.collect.TreeBasedTable;
import com.google.common.io.Files;

/**
 * Basic Spring web service controller that handles all requests for datasets.
 */
@RestController
@RequestMapping("/datasets")
public class DataSetController {

	private static final String OBJECT_NAME = "dataSet";
	private static final String APPLICATION_JSON = "application/json";
	private static final String DELIMITER = ",";
	private static final String PLOT_OUTPUT_DIR = "/var/www/html/";
	private static final String CSV_EXTENSION = ".csv";
	private static final String EXCEL_EXTENTION = ".xlsx";
	private static final String GENETIC_ENGINEERING_ADOPTION_DISPLAY_PREFIX = "Genetic Engineering Adoption ";
	private static final String GENETIC_ENGINEERING_ADOPTION_FILE_PREFIX = "geneticEngineeringAdoption-";
	private static final String GENETIC_ENGINEERING_ADOPTION_FILE_EXT = CSV_EXTENSION;
	private static final String DEFAULT_SHEET_NAME = "Data Sheet (machine readable)";

	private static final FilteredDataSetValidator VALIDATOR = new FilteredDataSetValidator();

	/**
	 * Merges the filtered datasets, stores the merged file and returns a handle for
	 * the file
	 *
	 * @param dataSets the datasets to merge
	 * @return the handle for the merged dataset
	 */
	@RequestMapping(path = "merge", method = RequestMethod.POST, consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
	public ResponseEntity merge(@RequestBody List<FilteredDataSet> dataSets) {
		try {
			RowSortedTable<String, String, String> finalGraph = TreeBasedTable.create();
			String finalPivotColumn = null;
			for (FilteredDataSet filteredDataSet : dataSets) {

				Errors errors = validateDataSet(filteredDataSet);
				if (errors.hasErrors()) {
					String error = errors.getAllErrors().stream().map(e -> e.getDefaultMessage())
							.collect(Collectors.joining(DELIMITER));
					return new ResponseEntity<String>(error, HttpStatus.BAD_REQUEST);
				}

				String inputFile = filteredDataSet.getFileName();
				String pivotColumn = filteredDataSet.getPivotColumn();
				String groupColumn = filteredDataSet.getGroupColumn();
				String valueColumn = filteredDataSet.getValueColumn();
				Map<String, List<String>> filters = filteredDataSet.getFilters();

				RowSortedTable<String, String, String> dataSetGraph = null;
				if (inputFile.endsWith(EXCEL_EXTENTION)) {
					dataSetGraph = FileUtils.excelToGraph(inputFile, DEFAULT_SHEET_NAME, pivotColumn, groupColumn,
							valueColumn, filters);
				} else if (inputFile.endsWith(CSV_EXTENSION)) {
					dataSetGraph = FileUtils.csvToGraph(inputFile, pivotColumn, groupColumn, valueColumn, filters);
				} else {
					// TODO return invalid extension message
					return ResponseEntity.badRequest().build();
				}

				finalGraph = FileUtils.mergeGraphsByPivot(finalGraph, dataSetGraph);
				finalPivotColumn = pivotColumn;
			}

			// TODO Verify that all the dataSets have the same pivot or return invalid
			// dataSet message

			StringBuilder generatedCSVFile = FileUtils.graphToCSV(finalPivotColumn, finalGraph);
			long time = System.nanoTime();
			String fileName = GENETIC_ENGINEERING_ADOPTION_FILE_PREFIX + time + GENETIC_ENGINEERING_ADOPTION_FILE_EXT;
			String filePath = PLOT_OUTPUT_DIR + fileName;

			String displayName = GENETIC_ENGINEERING_ADOPTION_DISPLAY_PREFIX + time;
			File file = new File(filePath);
			Files.asCharSink(file, Charsets.UTF_8).write(generatedCSVFile);
			DataSet dataSet = new DataSet();
			dataSet.setFileName(fileName);
			dataSet.setDisplayName(displayName);
			return ResponseEntity.ok(dataSet);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Retrieves the values of the columns that can be used for filtering
	 *
	 * @param dataSet the dataset to use for retrieving the column's values
	 * @return a list of values for the filtering column
	 */
	@RequestMapping(path = "columns", method = RequestMethod.POST, produces = APPLICATION_JSON)
	public ResponseEntity columns(@RequestBody FilteredDataSet dataSet) {
		try {
			Set<String> valueList = Collections.emptySet();
			String inputFile = dataSet.getFileName();
			String column = dataSet.getGroupColumn();
			Map<String, List<String>> filters = dataSet.getFilters();
			if (inputFile.endsWith(EXCEL_EXTENTION)) {
				valueList = FileUtils.retrieveExcelColumnValues(inputFile, DEFAULT_SHEET_NAME, column, filters);
			} else if (inputFile.endsWith(CSV_EXTENSION)) {
				valueList = FileUtils.retrieveCSVColumnValues(inputFile, column, filters);
			}
			return ResponseEntity.ok(valueList);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Validates the filtered dataset model
	 *
	 * @param filteredDataSet the filtered dataset model to validate
	 * @return the erros found during validation
	 */
	private Errors validateDataSet(FilteredDataSet filteredDataSet) {
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(filteredDataSet, OBJECT_NAME);
		VALIDATOR.validate(filteredDataSet, errors);
		return errors;
	}

}
