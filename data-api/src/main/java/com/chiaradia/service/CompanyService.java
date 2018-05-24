package com.chiaradia.service;

import java.util.List;

import com.chiaradia.domain.CompanyDO;

public interface CompanyService {
	
	CompanyDO find(String id);
	List<CompanyDO> findAll();
	List<CompanyDO> find(String name, String zipCode);
	void update(CompanyDO companyDO, String id);
	void delete(String id);
	CompanyDO create(CompanyDO companyDO);
	void create(List<CompanyDO> companies);

}
