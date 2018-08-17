package com.aws.codestar.projecttemplates.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aws.codestar.projecttemplates.ApplicationProperties;
import com.aws.codestar.projecttemplates.api.DataSet;
import com.aws.codestar.projecttemplates.api.FilteredDataSet;
import com.aws.codestar.projecttemplates.exception.IncompatibleColumnValuesException;
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

	private static final String DASH = "-";
	private static final String PERIOD = ".";
	private static final String TEXT_PLAIN = "text/plain";
	private static final String OBJECT_NAME = "dataSet";
	private static final String APPLICATION_JSON = "application/json";
	private static final String DELIMITER = ",";
	private static final String CSV_EXTENSION = "csv";
	private static final String EXCEL_EXTENSION = "xlsx";
	private static final String GENETIC_ENGINEERING_ADOPTION_FILE_EXT = CSV_EXTENSION;
	private static final String DEFAULT_SHEET_NAME = "Data Sheet (machine readable)";

	private static final FilteredDataSetValidator VALIDATOR = new FilteredDataSetValidator();

	@Autowired
	private ApplicationProperties properties;

	/**
	 * Merges the filtered datasets, stores the merged file and returns a handle for
	 * the file
	 *
	 * @param dataSets the datasets to merge
	 * @return the handle for the merged dataset
	 */
	@RequestMapping(path = "merge", method = RequestMethod.POST, consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
	public ResponseEntity merge(@RequestBody List<FilteredDataSet> dataSets,
			@RequestParam(value = "name") String mergedDatasetName) {
		mergedDatasetName = mergedDatasetName.trim();
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
				String inputPath = new File(properties.getInputDir(), inputFile).getAbsolutePath();

				String pivotColumn = filteredDataSet.getPivotColumn();
				String groupColumn = filteredDataSet.getGroupColumn();
				String valueColumn = filteredDataSet.getValueColumn();
				Map<String, List<String>> filters = filteredDataSet.getFilters();

				RowSortedTable<String, String, String> dataSetGraph = null;
				String extenstion = FilenameUtils.getExtension(inputFile);
				if (EXCEL_EXTENSION.equalsIgnoreCase(extenstion)) {
					dataSetGraph = FileUtils.excelToGraph(inputPath, DEFAULT_SHEET_NAME, pivotColumn, groupColumn,
							valueColumn, filters);
				} else if (CSV_EXTENSION.equalsIgnoreCase(extenstion)) {
					dataSetGraph = FileUtils.csvToGraph(inputPath, pivotColumn, groupColumn, valueColumn, filters);
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

			String fileName = mergedDatasetName + DASH + time + PERIOD + GENETIC_ENGINEERING_ADOPTION_FILE_EXT;
			String filePath = properties.getOutputDir() + fileName;

			String displayName = mergedDatasetName;
			File file = new File(filePath);
			Files.asCharSink(file, Charsets.UTF_8).write(generatedCSVFile);
			DataSet dataSet = new DataSet();
			dataSet.setFileName(fileName);
			dataSet.setDisplayName(displayName);
			return ResponseEntity.ok(dataSet);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} catch ( IncompatibleColumnValuesException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	/**
	 * Merges the filtered datasets, stores the merged file and returns a handle for
	 * the file
	 *
	 * @param dataSets the datasets to merge
	 * @return the handle for the merged dataset
	 */
	@RequestMapping(path = "convert", method = RequestMethod.POST, consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
	public ResponseEntity convertToCsv(@RequestBody DataSet dataSet) {
		try {
			String inputFile = dataSet.getFileName();
			String fileName = FilenameUtils.getName(inputFile);
			String extenstion = FilenameUtils.getExtension(fileName);
			if (EXCEL_EXTENSION.equalsIgnoreCase(extenstion)) {
				String inputPath = new File(properties.getOutputDir(), fileName).getAbsolutePath();

				StringBuilder generatedFile = FileUtils.excelToCSV(inputPath, DEFAULT_SHEET_NAME);
				long time = System.nanoTime();

				String fileNameNoExt = FilenameUtils.removeExtension(fileName);
				String newfileName = fileNameNoExt + DASH + time + PERIOD + CSV_EXTENSION;
				String filePath = properties.getOutputDir() + newfileName;
				File file = new File(filePath);
				Files.asCharSink(file, Charsets.UTF_8).write(generatedFile);

				DataSet newDataSet = new DataSet();
				String displayName = dataSet.getDisplayName();
				newDataSet.setFileName(newfileName);
				newDataSet.setDisplayName(displayName);
				return ResponseEntity.ok(newDataSet);
			} else if (CSV_EXTENSION.equalsIgnoreCase(extenstion)) {
				return ResponseEntity.ok(dataSet);
			} else {
				// TODO return invalid conversion message
				return ResponseEntity.badRequest().build();
			}

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
			String inputPath = new File(properties.getInputDir(), inputFile).getAbsolutePath();
			String column = dataSet.getGroupColumn();
			Map<String, List<String>> filters = dataSet.getFilters();

			String extenstion = FilenameUtils.getExtension(inputFile);
			if (EXCEL_EXTENSION.equalsIgnoreCase(extenstion)) {
				valueList = FileUtils.retrieveExcelColumnValues(inputPath, DEFAULT_SHEET_NAME, column, filters);
			} else if (CSV_EXTENSION.equalsIgnoreCase(extenstion)) {
				valueList = FileUtils.retrieveCSVColumnValues(inputPath, column, filters);
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

	@RequestMapping(path = "data", method = RequestMethod.GET, produces = TEXT_PLAIN)
	public ResponseEntity data(@RequestParam(value = "file") String file) {
		return ResponseEntity.ok(getFileContents(file));
	}

	private String getFileContents(String fileName) {
		if (fileName.endsWith(".csv")) {
			String content;
			try {
				Scanner scan = new Scanner(new File(properties.getOutputDir() + fileName));
				content = scan.useDelimiter("\\Z").next();
				scan.close();
				return content;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

	private String getPathFromeFilePath(String path) {
		return path.substring(0, path.lastIndexOf("/") + 1);
	}

	private File getPathToFile(String fileName) {
		ClassLoader classLoader = getClass().getClassLoader();
		URL url = classLoader.getResource(fileName);
		File file = new File(url.getFile());
		return file;
	}

}
