package com.chiaradia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.chiaradia.domain.CompanyDO;

@Repository
public interface CompanyRepository extends MongoRepository<CompanyDO, Long> {

	public Optional<CompanyDO> findById(String id);
	
	public CompanyDO findByName(String name);
	
	public List<CompanyDO> findByNameContainingAndZipCode(String name, String zipCode);
}
