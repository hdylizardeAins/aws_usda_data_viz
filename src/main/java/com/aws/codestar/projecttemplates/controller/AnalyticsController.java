package com.aws.codestar.projecttemplates.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

	private static final String REGRESSION = "regression";
	private static final String SUMMARY = "summary";
	private static final String PLOT = "plot";
	private static final String RSCRIPT = "Rscript ";
	private static final String R_SCRIPT_LOC = "/home/ewimberley/Documents/workspace-sts-3.9.5.RELEASE/aws_usda_data_viz/prototype.R";
	private static final String MESSAGE_FORMAT = "Hello %s!";

	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity helloWorldPost(@RequestParam(value = "action") String action, @RequestParam(value = "dataFile") String name) {
		String rAction = "";
		if(action.equals(PLOT)){
			rAction = PLOT; 
		} else if(action.equals(SUMMARY)){
			rAction = SUMMARY;
		} else if(action.equals(REGRESSION)){
			rAction = REGRESSION;
		}
		String cmd = RSCRIPT + R_SCRIPT_LOC + " " + rAction;
		Runtime run = Runtime.getRuntime();
		try {
			Process pr = run.exec(cmd);
			pr.waitFor();
			BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line = "";
			while ((line = buf.readLine()) != null) {
				System.out.println(line);
			}
			return ResponseEntity.ok(createResponse(name));
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
		
	}

	private String createResponse(String name) {
		return new JSONObject().put("Output", String.format(MESSAGE_FORMAT, name)).toString();
	}
}
