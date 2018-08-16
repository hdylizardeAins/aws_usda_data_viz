package com.aws.codestart.projecttemplates.controller;

import org.springframework.validation.BeanPropertyBindingResult;
import com.aws.codestar.projecttemplates.api.DataSet;
import com.aws.codestar.projecttemplates.api.FilteredDataSet;
import com.aws.codestar.projecttemplates.controller.FilteredDataSetValidator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the validator of FilteredDataSet models
 *
 * @author ccruz-rodriguez
 *
 */
public class FilteredDataSetValidatorTest {

	private static final String TEST_OBJECT_NAME = "Test Object";
	private static final String VALUE_COLUMN_EMPTY_ERROR_CODE = "valueColumn.empty";
	private static final String GROUP_COLUMN_EMPTY_ERROR_CODE = "groupColumn.empty";
	private static final String PIVOT_COLUMN_EMPTY_ERROR_CODE = "pivotColumn.empty";
	private static final String FILE_NAME_EMPTY_ERROR_CODE = "fileName.empty";
	private static final String TEST_VALUE_COLUMN = "Test Value Column";
	private static final String TEST_GROUP_COLUMN = "Test Group Column";
	private static final String TEST_PIVOT_COLUMN = "Test Pivot Column";
	private static final String TEST_FILE_NAME = "Test File Name";
	private FilteredDataSetValidator validator;

	@BeforeEach
	public void setUp() {
		validator = new FilteredDataSetValidator();
	}

	@AfterEach
	public void tearDown() {
		validator = null;
	}

	@Test
	public void testSupportsMethod() {
		boolean supportIsFalse = validator.supports(DataSet.class);
		assertFalse(supportIsFalse);
		boolean supportIsTrue = validator.supports(FilteredDataSet.class);
		assertTrue(supportIsTrue);
	}

	@ParameterizedTest
	@MethodSource("createDataSetValidationInput")
	public void testValidation(String fileName, String pivotColumn, String groupColumn, String valueColumn,
			boolean hasNameError, boolean hasPivotError, boolean hasGroupError, boolean hasValueError) {
		FilteredDataSet dataSet = new FilteredDataSet();
		dataSet.setFileName(fileName);
		dataSet.setPivotColumn(pivotColumn);
		dataSet.setGroupColumn(groupColumn);
		dataSet.setValueColumn(valueColumn);
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dataSet, TEST_OBJECT_NAME);
		validator.validate(dataSet, errors);
		boolean hasFileNameMessage = errors.getAllErrors().stream()
				.anyMatch(e -> FILE_NAME_EMPTY_ERROR_CODE.equals(e.getCode()));
		assertEquals(hasNameError, hasFileNameMessage);
		boolean hasPivotMessage = errors.getAllErrors().stream()
				.anyMatch(e -> PIVOT_COLUMN_EMPTY_ERROR_CODE.equals(e.getCode()));
		assertEquals(hasPivotError, hasPivotMessage);
		boolean hasGroupMessage = errors.getAllErrors().stream()
				.anyMatch(e -> GROUP_COLUMN_EMPTY_ERROR_CODE.equals(e.getCode()));
		assertEquals(hasGroupError, hasGroupMessage);
		boolean hasValueMessage = errors.getAllErrors().stream()
				.anyMatch(e -> VALUE_COLUMN_EMPTY_ERROR_CODE.equals(e.getCode()));
		assertEquals(hasValueError, hasValueMessage);
	}

	/**
	 * Retrieves a stream of the inputs to use for testing the validator of datasets
	 *
	 * @return a stream of the inputs to use for testing the validator of datasets
	 */
	@SuppressWarnings("unused")
	private static Stream<Arguments> createDataSetValidationInput() {
		return Stream.of(Arguments.of(null, null, null, null, true, true, true, true),
				Arguments.of(TEST_FILE_NAME, null, null, null, false, true, true, true),
				Arguments.of(TEST_FILE_NAME, TEST_PIVOT_COLUMN, null, null, false, false, true, true),
				Arguments.of(TEST_FILE_NAME, TEST_PIVOT_COLUMN, TEST_GROUP_COLUMN, null, false, false, false, true),
				Arguments.of(TEST_FILE_NAME, TEST_PIVOT_COLUMN, TEST_GROUP_COLUMN, TEST_VALUE_COLUMN, false, false,
						false, false)

		);

	}

}
