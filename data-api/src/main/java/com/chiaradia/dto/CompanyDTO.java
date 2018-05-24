package com.chiaradia.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyDTO {

	private String id;

	private String name;

	private String zipCode;

	private String website;

	private CompanyDTO() {
	}

	public CompanyDTO(String id, String name, String zipCode, String website) {
		this.id = id;
		this.name = name;
		this.zipCode = zipCode;
		this.website = website;
	}

	public static CompanyDTOBuilder newBuilder() {
		return new CompanyDTOBuilder();
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getZipCode() {
		return zipCode;
	}

	public String getWebsite() {
		return website;
	}

	public static class CompanyDTOBuilder {

		private String id;
		private String name;
		private String zipCode;
		private String website;

		public CompanyDTOBuilder setId(String id) {
			this.id = id;
			return this;
		}

		public CompanyDTOBuilder setName(String name) {
			this.name = name;
			return this;
		}

		public CompanyDTOBuilder setZipCode(String zipCode) {
			this.zipCode = zipCode;
			return this;
		}

		public CompanyDTOBuilder setWebsite(String website) {
			this.website = website;
			return this;
		}

		public CompanyDTO createCompanyDTO() {
			return new CompanyDTO(id, name, zipCode, website);
		}

	}

}
