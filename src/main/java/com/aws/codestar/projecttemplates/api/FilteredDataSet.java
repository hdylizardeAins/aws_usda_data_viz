package com.aws.codestar.projecttemplates.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Model that represents a dataset that can be filtered
 *
 * @author ccruz-rodriguez
 *
 */
public class FilteredDataSet extends DataSet {

	/**
	 * Serialization Identifier
	 */
	private static final long serialVersionUID = 7388713304306065209L;

	private Map<String, List<String>> filters = new HashMap<>();

	private String pivotColumn;

	private String groupColumn;

	private String valueColumn;

	/**
	 * Retrieves the filters to use on the dataset
	 *
	 * @return the filters to use on the dataset
	 */
	public Map<String, List<String>> getFilters() {
		return filters;
	}

	/**
	 * Sets the filters to use on the dataset
	 *
	 * @param filters the filters to set
	 */
	public void setFilters(Map<String, List<String>> filters) {
		this.filters = filters;
	}

	/**
	 * Retrieves the column to use as a pivot
	 *
	 * @return the column to use as a pivot
	 */
	public String getPivotColumn() {
		return pivotColumn;
	}

	/**
	 * Sets the column to use as a pivot
	 * 
	 * @param pivotColumn the column to set
	 */
	public void setPivotColumn(String pivotColumn) {
		this.pivotColumn = pivotColumn;
	}

	/**
	 * Retrieves the column to use for grouping
	 *
	 * @return the column to use for grouping
	 */
	public String getGroupColumn() {
		return groupColumn;
	}

	/**
	 * Sets the column to use for grouping values
	 * 
	 * @param groupColumn the column to use
	 */
	public void setGroupColumn(String groupColumn) {
		this.groupColumn = groupColumn;
	}

	/**
	 * Retrieves the column that contains the values of the dataset
	 *
	 * @return the column that contains the values of the dataset
	 */
	public String getValueColumn() {
		return valueColumn;
	}

	/**
	 * Sets the column that contains the values of the dataset
	 *
	 * @param valueColumn the column to set
	 */
	public void setValueColumn(String valueColumn) {
		this.valueColumn = valueColumn;
	}

}
