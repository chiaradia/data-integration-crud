package com.chiaradia.controller.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.chiaradia.domain.CompanyDO;
import com.chiaradia.dto.CompanyDTO;

public class CompanyMapper {

	public static CompanyDO makeCompanyDO(CompanyDTO companyDTO) {
		CompanyDO companyDO = new CompanyDO(companyDTO.getName());

		if (companyDTO.getZipCode() != null && !companyDTO.getZipCode().equals(""))
			companyDO.setZipCode(companyDTO.getZipCode());

		if (companyDTO.getWebsite() != null && !companyDTO.getWebsite().equals(""))
			companyDO.setWebsite(companyDTO.getWebsite());

		return companyDO;
	}

	public static CompanyDTO makeCompanyDTO(CompanyDO companyDO) {

		CompanyDTO.CompanyDTOBuilder companyDTOBuilder = 
				CompanyDTO.newBuilder()
					.setId(companyDO.getId())
					.setName(companyDO.getName());
		
		if (companyDO.getZipCode() != null && !companyDO.getZipCode().equals(""))
			companyDTOBuilder.setZipCode(companyDO.getZipCode());

		if (companyDO.getWebsite() != null && !companyDO.getWebsite().equals(""))
			companyDTOBuilder.setWebsite(companyDO.getWebsite());
		
		return companyDTOBuilder.createCompanyDTO();
	}
	
	public static List<CompanyDTO> makeCompanyDTOList(Collection<CompanyDO> companies) {
		
		return companies
	            .stream()
	            .map(CompanyMapper::makeCompanyDTO)
	            .collect(Collectors.toList());
	}
}
