package com.aws.codestar.projecttemplates.api;

public class DataGovObject {
	private Integer count;
	private DataGovResult[] results;
	/**
	 * @return the count
	 */
	public Integer getCount() {
		return count;
	}
	/**
	 * @param count the count to set
	 */
	public void setCount(Integer count) {
		this.count = count;
	}
	/**
	 * @return the results
	 */
	public DataGovResult[] getResults() {
		return results;
	}
	/**
	 * @param results the results to set
	 */
	public void setResults(DataGovResult[] results) {
		this.results = results;
	}
	
	
}
