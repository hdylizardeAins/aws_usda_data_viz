package com.aws.codestar.projecttemplates.controller;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.aws.codestar.projecttemplates.api.FilteredDataSet;

/**
 * Performs validations on FilteredDataSet models
 *
 * @author ccruz-rodriguez
 *
 */
public class FilteredDataSetValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return FilteredDataSet.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors e) {
		ValidationUtils.rejectIfEmpty(e, "fileName", "fileName.empty", "The 'fileName' field is empty.");
		ValidationUtils.rejectIfEmpty(e, "pivotColumn", "pivotColumn.empty", "The 'pivotColumn' field is empty.");
		ValidationUtils.rejectIfEmpty(e, "groupColumn", "groupColumn.empty", "The 'groupColumn' field is empty.");
		ValidationUtils.rejectIfEmpty(e, "valueColumn", "valueColumn.empty", "The 'valueColumn' field is empty.");
	}
}
