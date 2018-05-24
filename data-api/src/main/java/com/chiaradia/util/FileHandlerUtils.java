package com.chiaradia.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.chiaradia.exception.DataUploadException;

public class FileHandlerUtils {
	
	public static List<File> createFiles(HttpServletRequest request, String id) throws IOException, ServletException{
		List<InputStream> inputs = createInputStreamList(request);
		return createFiles(inputs, id);
	}
	
	public static List<File> createFiles(List<InputStream> files, String id) throws IOException {
		List<File> csvFiles = new ArrayList<>();
		for (InputStream file : files) {
			if (file != null) {
				File targetFile = new File("/opt/tmp/" + id + "/" + file.hashCode());
				FileUtils.copyInputStreamToFile(file, targetFile);
				csvFiles.add(targetFile);
			}
		}
		return csvFiles;
	}
	
	public static List<InputStream> createInputStreamList (HttpServletRequest request) throws IOException, ServletException {
		
		return request.getParts().stream().map(a -> {
			String extension = FilenameUtils.getExtension(a.getSubmittedFileName());
			if (extension != null && (extension.equalsIgnoreCase("csv") || extension.equalsIgnoreCase("txt"))) {
				if (a.getName().equals("csv"))
					try {
						return a.getInputStream();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
			throw new DataUploadException("All files must be valid");
		}).collect(Collectors.toList());
	}

}
