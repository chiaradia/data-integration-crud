package com.chiaradia.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chiaradia.dto.UploadDataDTO;
import com.chiaradia.service.DataUploadService;

@RestController
@RequestMapping("v1/data")
public class DataUploadController {
	
	private final DataUploadService dataUploadService;
	
	@Autowired
	public DataUploadController(final DataUploadService dataUploadService) {
		this.dataUploadService = dataUploadService;
	}
	
	@PostMapping
	public UploadDataDTO uploadCsv(HttpServletRequest request) throws NoSuchAlgorithmException, IOException, ServletException {
		
		return dataUploadService.uploadCsv(request);
	}

}
