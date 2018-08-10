package com.aws.codestar.projecttemplates.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.JSONObject;
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

	private static final String COLUMNS = "columns";
	private static final String REGRESSION = "regression";
	private static final String SUMMARY = "summary";
	private static final String PLOT = "plot";
	private static final String RSCRIPT = "Rscript ";
	//private static final String R_SCRIPT_LOC = "/home/ewimberley/Documents/workspace-sts-3.9.5.RELEASE/aws_usda_data_viz/src/main/resources/prototype.R";
	private static final String MESSAGE_FORMAT = "Hello %s!";
	
	private String scriptLoc;
	
	public AnalyticsController() {
		ClassLoader classLoader = getClass().getClassLoader();
		URL url = classLoader.getResource("prototype.R");
		File file = new File(url.getFile());
		scriptLoc = file.getAbsolutePath();
	}

	@RequestMapping(path = "plot", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity plot(@RequestParam(value = "inputFile") String inputFile) {
		String cmd = RSCRIPT + scriptLoc;
		try {
			String response = runR(cmd, PLOT, inputFile);
			return ResponseEntity.ok(response);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

	@RequestMapping(path = "regression", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity regression(@RequestParam(value = "inputFile") String inputFile) {
		String cmd = RSCRIPT + scriptLoc;
		try {
			String response = runR(cmd, REGRESSION, inputFile);
			return ResponseEntity.ok(response);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

	@RequestMapping(path = "summary", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity summary(@RequestParam(value = "inputFile") String inputFile) {
		String cmd = RSCRIPT + scriptLoc;
		try {
			String response = runR(cmd, SUMMARY, inputFile);
			return ResponseEntity.ok(response);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

	@RequestMapping(path = "columns", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity columns(@RequestParam(value = "inputFile") String inputFile) {
		String cmd = RSCRIPT + scriptLoc;
		try {
			String response = runR(cmd, COLUMNS, inputFile);
			return ResponseEntity.ok(response);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

	private String runR(String cmd, String action, String inputFile) throws IOException, InterruptedException {
		Runtime run = Runtime.getRuntime();
		Process pr = run.exec(cmd + " " + action);
		pr.waitFor();
		BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String data = "";
		String line = "";
		while ((line = buf.readLine()) != null) {
			System.out.println(line);
			data = line.substring(0, line.length());
		}
		return data;
		// return ResponseEntity.ok(createResponse(name));

	}

	private String createResponse(String name) {
		return new JSONObject().put("Output", String.format(MESSAGE_FORMAT, name)).toString();
	}
}
