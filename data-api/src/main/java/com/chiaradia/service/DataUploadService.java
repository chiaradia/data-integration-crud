package com.chiaradia.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.chiaradia.dto.UploadDataDTO;

public interface DataUploadService {

	void loadCompanyDataOnStartup() throws IOException;
	UploadDataDTO uploadCsv(HttpServletRequest request) throws IOException, ServletException, NoSuchAlgorithmException;
	
}
