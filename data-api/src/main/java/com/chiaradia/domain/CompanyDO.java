package com.chiaradia.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

//@Entity
//@Table(name = "companies")
@Document(collection = "companies")
public class CompanyDO {

	@Id
	private String id;

	@Indexed
	private String name;

	private String zipCode;

	private String website;
	
	public CompanyDO() {
		
	}
	
	public CompanyDO(String name) {
		this.name = name;
	}

	public CompanyDO(String name, String zipCode, String website) {
		this.name = name;
		this.zipCode = zipCode;
		this.website = website;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

}
