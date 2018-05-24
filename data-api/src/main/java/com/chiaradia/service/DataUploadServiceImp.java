package com.chiaradia.service;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.buf.HexUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chiaradia.component.CsvDataLoader;
import com.chiaradia.domain.CompanyDO;
import com.chiaradia.dto.UploadDataDTO;
import com.chiaradia.exception.DataUploadException;
import com.chiaradia.util.FileHandlerUtils;

@Service
public class DataUploadServiceImp implements DataUploadService {

	@Autowired
	private CompanyService companyService;

	@Autowired
	private CsvDataLoader dataLoader;

	@Override
	@Transactional
	@EventListener(ApplicationReadyEvent.class)
	public void loadCompanyDataOnStartup() throws IOException {
		File file = new ClassPathResource("companies_catalog.csv").getFile();
		List<CompanyDO> loadObjectList = dataLoader.loadObjectList(file);
		this.companyService.create(loadObjectList);
	}

	@Override
	public UploadDataDTO uploadCsv(HttpServletRequest request)
			throws IOException, ServletException, NoSuchAlgorithmException {

		UploadDataDTO uploadDataDTO = new UploadDataDTO();
		String id = HexUtils.toHexString(SecureRandom.getInstance("SHA1PRNG").getSeed(20));
		uploadDataDTO.setId(id);
		if (ServletFileUpload.isMultipartContent(request)) {
			List<File> csvFiles = FileHandlerUtils.createFiles(request, id);
			if (csvFiles.isEmpty())
				throw new DataUploadException("Empty list");
			for (File f : csvFiles) {
				List<CompanyDO> merged = mergeData(dataLoader.loadObjectList(f));
				companyService.create(merged);
			}
			uploadDataDTO.setStatus("OK");
		} else {
			throw new DataUploadException("Request must be multipart");
		}
		return uploadDataDTO;
	}

	public List<CompanyDO> mergeData(List<CompanyDO> loadedFromCsv) {

		List<CompanyDO> companies = companyService.findAll();

		return mergeLists(companies, loadedFromCsv);

	}

	public List<CompanyDO> mergeLists (List<CompanyDO> companies, List<CompanyDO> loadedFromCsv)  {
		
		return companies.stream().flatMap(db -> loadedFromCsv.stream()
					.filter(csv -> db.getName().equalsIgnoreCase(csv.getName()))
						.map(csv -> {
							db.setWebsite(csv.getWebsite());
							db.setZipCode(csv.getZipCode());
							return db;
						})).collect(Collectors.toList());
		
	}
	

}
