package com.aws.codestar.projecttemplates.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.aws.codestar.projecttemplates.ApplicationProperties;
import com.aws.codestar.projecttemplates.api.DataGovModel;
import com.aws.codestar.projecttemplates.api.DataGovResource;
import com.aws.codestar.projecttemplates.api.DataGovResult;
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
		} catch (IncompatibleColumnValuesException e) {
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

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@RequestMapping(path = "search", method = RequestMethod.GET, produces = APPLICATION_JSON)
	public ResponseEntity fetchDataGovPackages(@RequestParam(value = "term") String term) {
		String url = "https://catalog.data.gov/api/3/action/package_search?q=" + term;
		RestTemplate rest = new RestTemplate();
		DataGovModel response = rest.getForObject(url, DataGovModel.class);
		List<DataGovResource> resources = filterCsv(response);
		return ResponseEntity.ok(resources);
	}

	/**
	 * Retrieves a dataset from the requested URL and returns the filename back to
	 * the caller.
	 *
	 * @param url the URL for the dataset being fetched
	 * @return the local filename of the dataset
	 */
	@RequestMapping(path = "fetch", method = RequestMethod.GET, produces = APPLICATION_JSON)
	public ResponseEntity fetchDataset(@RequestParam(value = "url") String url,
			@RequestParam(value = "name") String name) {
		updateTrustStore();
		try {
			if (url.endsWith("xls")) {
				URL oldUrl = new URL(url);
				URL newUrl = new URL("https", oldUrl.getHost(), oldUrl.getFile());

				InputStream in = newUrl.openStream();
				StringBuilder generatedFile = FileUtils.xlsToCSV(in, DEFAULT_SHEET_NAME);
				long time = System.nanoTime();

				String fileName = name + DASH + time + PERIOD + CSV_EXTENSION;
				String filePath = properties.getOutputDir() + fileName;
				File file = new File(filePath);
				Files.asCharSink(file, Charsets.UTF_8).write(generatedFile);

				DataSet newDataSet = new DataSet();
				String displayName = name;
				newDataSet.setFileName(fileName);
				newDataSet.setDisplayName(displayName);
				return ResponseEntity.ok(newDataSet);
			} else if (url.endsWith(CSV_EXTENSION)) {
				URL oldUrl = new URL(url);
				URL newUrl = new URL("https", oldUrl.getHost(), oldUrl.getFile());
				String fileName = name + DASH + System.nanoTime() + PERIOD + CSV_EXTENSION;
				String filePath = properties.getOutputDir() + fileName;
				org.apache.commons.io.FileUtils.copyURLToFile(newUrl, new File(filePath));

				DataSet newDataSet = new DataSet();
				String displayName = name;
				newDataSet.setFileName(fileName);
				newDataSet.setDisplayName(displayName);
				return ResponseEntity.ok(newDataSet);
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
	 * Filters the data retrieved from the Data.gov API and returns only the
	 * {@link DataGovResource}s that point to CSV files
	 * 
	 * @return Array of resources that point to CSV files
	 */
	private List<DataGovResource> filterCsv(DataGovModel data) {
		List<DataGovResource> resources = new ArrayList<>();

		for (DataGovResult result : data.getResult().getResults()) {
			for (DataGovResource resource : result.getResources()) {
				if (resource.getUrl().endsWith(".csv") || resource.getUrl().endsWith(".xls")) {
					resources.add(resource);
				}
			}
		}

		return resources;
	}

	/**
	 * Updates the trust store to trust the certificates.
	 */
	private void updateTrustStore() {
		// Create a new trust manager that trust all certificates
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}
		} };

		// Activate the new trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
		}
	}

}
