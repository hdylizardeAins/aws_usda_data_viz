package com.aws.codestar.projecttemplates.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Basic Spring web service controller that handles all GET requests.
 */
@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

	private static final String EMPTY_STRING = "";
	private static final String BOXPLOT = "boxplot";
	private static final String PARAM_LIST_FILTER = "[^A-Za-z0-9,.]";
	private static final String X_Y_FILTER = "[^A-Za-z0-9.]";
	private static final String APPLICATION_JSON = "application/json";
	private static final String TREND = "trend-line";
	private static final String PLOT_OUTPUT_DIR = "/var/www/html/";
	private static final String PROTOTYPE_R = "prototype.R";
	private static final String COLUMNS = "columns";
	private static final String REGRESSION = "regression";
	private static final String SUMMARY = "summary";
	private static final String PLOT = "plot";
	private static final String RSCRIPT = "Rscript ";
	
	private String scriptLoc;
	
	public AnalyticsController() {
		scriptLoc = RSCRIPT + " " + getPathToFile(PROTOTYPE_R);
	}

	//TODO Add functionality to select multiple data sets that are compatible before running analytics
	@RequestMapping(path = PLOT, method = RequestMethod.GET, produces = APPLICATION_JSON)
	public ResponseEntity plot(@RequestParam(value = "inputFile") String inputFile, @RequestParam(value = "columns") String columns) {
		String cmd = scriptLoc;
		String inputLoc = new File(PLOT_OUTPUT_DIR, inputFile).getAbsolutePath();
		try {
			//String response = runR(cmd, PLOT, inputLoc, PLOT_OUTPUT_DIR + " \"" + columns + "\"");
			String columnsFiltered = columns.replaceAll(PARAM_LIST_FILTER, EMPTY_STRING);
			String response = runR(cmd, PLOT, inputLoc, PLOT_OUTPUT_DIR + " " + columnsFiltered);
			return ResponseEntity.ok(response);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

	@RequestMapping(path = REGRESSION, method = RequestMethod.GET, produces = APPLICATION_JSON)
	public ResponseEntity regression(@RequestParam(value = "inputFile") String inputFile, @RequestParam(value = "x") String x, @RequestParam(value = "y") String y) {
		String cmd = scriptLoc;
		String inputLoc = new File(PLOT_OUTPUT_DIR, inputFile).getAbsolutePath();
		try {
			String xFiltered = x.replaceAll(X_Y_FILTER, EMPTY_STRING);
			String yFiltered = y.replaceAll(X_Y_FILTER, EMPTY_STRING);
			String response = runR(cmd, REGRESSION, inputLoc, PLOT_OUTPUT_DIR + " linear " + xFiltered + " " + yFiltered);
			return ResponseEntity.ok(response);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
	
	@RequestMapping(path = BOXPLOT, method = RequestMethod.GET, produces = APPLICATION_JSON)
	public ResponseEntity boxplot(@RequestParam(value = "inputFile") String inputFile, @RequestParam(value = "x") String x, @RequestParam(value = "y") String y) {
		String cmd = scriptLoc;
		String inputLoc = new File(PLOT_OUTPUT_DIR, inputFile).getAbsolutePath();
		try {
			String xFiltered = x.replaceAll(X_Y_FILTER, EMPTY_STRING);
			String yFiltered = y.replaceAll(X_Y_FILTER, EMPTY_STRING);
			String response = runR(cmd, BOXPLOT, inputLoc, PLOT_OUTPUT_DIR + " " + xFiltered + " " + yFiltered);
			return ResponseEntity.ok(response);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
	
	@RequestMapping(path = TREND, method = RequestMethod.GET, produces = APPLICATION_JSON)
	public ResponseEntity trend(@RequestParam(value = "inputFile") String inputFile, @RequestParam(value = "x") String x, @RequestParam(value = "y") String y) {
		String cmd = scriptLoc;
		String inputLoc = new File(PLOT_OUTPUT_DIR, inputFile).getAbsolutePath();
		try {
			String xFiltered = x.replaceAll(X_Y_FILTER, EMPTY_STRING);
			String yFiltered = y.replaceAll(X_Y_FILTER, EMPTY_STRING);
			String response = runR(cmd, TREND, inputLoc, PLOT_OUTPUT_DIR + " " + xFiltered + " " + yFiltered);
			return ResponseEntity.ok(response);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

	@RequestMapping(path = SUMMARY, method = RequestMethod.GET, produces = APPLICATION_JSON)
	public ResponseEntity summary(@RequestParam(value = "inputFile") String inputFile) {
		String cmd = scriptLoc;
		String inputLoc = new File(PLOT_OUTPUT_DIR, inputFile).getAbsolutePath();
		try {
			String response = runR(cmd, SUMMARY, inputLoc, EMPTY_STRING);
			return ResponseEntity.ok(response);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

	@RequestMapping(path = COLUMNS, method = RequestMethod.GET, produces = APPLICATION_JSON)
	public ResponseEntity columns(@RequestParam(value = "inputFile") String inputFile) {
		String cmd = scriptLoc;
		String inputLoc = new File(PLOT_OUTPUT_DIR, inputFile).getAbsolutePath();
		try {
			String response = runR(cmd, COLUMNS, inputLoc, EMPTY_STRING);
			return ResponseEntity.ok(response);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

	private String runR(String cmd, String action, String inputFile, String params) throws IOException, InterruptedException {
		Runtime run = Runtime.getRuntime();
		String exec = cmd + " " + action + " " + inputFile + " " + params;
		System.out.println(exec);
		Process pr = run.exec(exec);
		pr.waitFor();
		BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String line = EMPTY_STRING;
		
		//print errors
		BufferedReader errBuf = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
		while ((line = errBuf.readLine()) != null) {
			System.err.println(line);
		}
		
		String data = EMPTY_STRING;
		while ((line = buf.readLine()) != null) {
			System.out.println(line);
			data = line.substring(0, line.length());
		}
		return data;
		// return ResponseEntity.ok(createResponse(name));

	}
	
	private String getPathToFile(String fileName) {
		ClassLoader classLoader = getClass().getClassLoader();
		URL url = classLoader.getResource(fileName);
		File file = new File(url.getFile());
		return file.getAbsolutePath();
	}

}
