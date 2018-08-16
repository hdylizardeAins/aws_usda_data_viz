package com.aws.codestart.projecttemplates.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.aws.codestar.projecttemplates.ApplicationProperties;
import com.aws.codestar.projecttemplates.api.DataSet;
import com.aws.codestar.projecttemplates.api.FilteredDataSet;
import com.aws.codestar.projecttemplates.controller.DataSetController;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { DataSetController.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@SpringBootConfiguration
@EnableAutoConfiguration
@EnableConfigurationProperties(ApplicationProperties.class)
public class DataSetControllerIT {

	@LocalServerPort
	private int port;

	@Test
	public void testColumnsEnpointForCsv() {

		FilteredDataSet dataSet = new FilteredDataSet();
		dataSet.setFileName("alltablesGEcrops.csv");
		dataSet.setGroupColumn("Variety");
		Map<String, List<String>> filters = new HashMap<>();
		filters.put("State", Arrays.asList("U.S."));
		filters.put("Crop", Arrays.asList("Corn"));
		dataSet.setFilters(filters);
		RequestSpecification given = given().basePath("/datasets").port(this.port).contentType(ContentType.JSON)
				.body(dataSet, ObjectMapperType.JACKSON_2);

		Response when = given.when().post("/columns");
		String[] columns = when.body().as(String[].class);
		assertTrue(columns.length > 0);
	}

	@Test
	public void testColumnsEnpointForExcel() {

		FilteredDataSet dataSet = new FilteredDataSet();
		dataSet.setFileName("CornCostReturn.xlsx");
		dataSet.setGroupColumn("Item2");
		Map<String, List<String>> excelFilters = new HashMap<>();
		excelFilters.put("Region", Arrays.asList("U.S. total"));
		excelFilters.put("Item2", Arrays.asList("Total, operating costs", "Seed", "Total, gross value of production"));
		dataSet.setFilters(excelFilters);
		RequestSpecification given = given().basePath("/datasets").port(this.port).contentType(ContentType.JSON)
				.body(dataSet, ObjectMapperType.JACKSON_2);

		Response when = given.when().post("/columns");
		String[] columns = when.body().as(String[].class);
		assertTrue(columns.length > 0);
	}

	@Test
	public void testMergeEnpoint() {
		FilteredDataSet dataSet = new FilteredDataSet();
		dataSet.setFileName("alltablesGEcrops.csv");
		dataSet.setPivotColumn("Year");
		dataSet.setGroupColumn("Unit");
		dataSet.setValueColumn("Value");
		Map<String, List<String>> filters = new HashMap<>();
		filters.put("State", Arrays.asList("U.S."));
		filters.put("Crop", Arrays.asList("Corn"));
		filters.put("Variety", Arrays.asList("All GE varieties"));

		FilteredDataSet dataSet1 = new FilteredDataSet();
		dataSet1.setFileName("CornCostReturnMR.csv");
		dataSet1.setPivotColumn("Year");
		dataSet1.setGroupColumn("Item2");
		dataSet1.setValueColumn("Value");

		Map<String, List<String>> excelFilters = new HashMap<>();
		excelFilters.put("Region", Arrays.asList("U.S. total"));
		excelFilters.put("Item2", Arrays.asList("Total, operating costs", "Seed", "Total, gross value of production"));
		dataSet1.setFilters(excelFilters);
		
		List<FilteredDataSet> dataSetList = Arrays.asList(dataSet, dataSet1);
		
		RequestSpecification given = given().basePath("/datasets").port(this.port).contentType(ContentType.JSON)
				.body(dataSetList, ObjectMapperType.JACKSON_2).queryParam("name", "Test Name");

		Response when = given.when().post("/merge");
		DataSet mergeDataSet = when.body().as(DataSet.class);
		assertNotNull(mergeDataSet);
	}

}
