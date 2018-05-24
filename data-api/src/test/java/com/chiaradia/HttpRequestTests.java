package com.chiaradia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import com.chiaradia.controller.mapper.CompanyMapper;
import com.chiaradia.domain.CompanyDO;
import com.chiaradia.dto.CompanyDTO;
import com.chiaradia.repository.CompanyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpRequestTests {

	@LocalServerPort
    int localPort;
	
	@Autowired
	private CompanyRepository repository;

	@Autowired
	private WebApplicationContext wac;
	
	@Autowired
	private TestRestTemplate testRestTemplate;

	private MockMvc mockMvc;

	private ObjectMapper mapper = new ObjectMapper();

	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(wac).build();
		this.repository.save(createCompany("5b04b4263cc4de2024975107", "GOOGLE", "12345", "https://google.com"));
		this.repository.save(createCompany("5b04bee43cc4de28f4100288", "AMAZON", "12345", "https://amazon.com"));
	}

	@Test
	public void shouldReturnListOfCompanies() throws Exception {

		MvcResult mvcResult = mockMvc.perform(
				get("/v1/companies").accept(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andReturn();

		CompanyDTO[] companyDTO = getCompaniesDTO(mvcResult.getResponse().getContentAsString());
		assertNotNull(companyDTO);

	}

	@Test
	public void shouldReturnOnlyOneCompany() throws Exception {

		MvcResult mvcResult = mockMvc.perform(get("/v1/companies/5b04b4263cc4de2024975107")
				.accept(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andReturn();

		CompanyDTO companyDTO = getCompanyDTO(mvcResult.getResponse().getContentAsString());

		assertNotNull(companyDTO);
		assertEquals("5b04b4263cc4de2024975107", companyDTO.getId());
		assertEquals("GOOGLE", companyDTO.getName());

	}

	@Test
	public void shouldNotReturnACompany() throws Exception {

		mockMvc.perform(get("/v1/companies/5b04b4263cc4de2024975507").accept(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is4xxClientError());

	}

	@Test
	public void shouldCreateACompanyAndReturnStatusCreated() throws Exception {

		CompanyDO companyDO = new CompanyDO("CREATED", "98765", "https://www.created.com");

		String json = json(CompanyMapper.makeCompanyDTO(companyDO));

		mockMvc.perform(post("/v1/companies").content(json).contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept("application/json;charset=UTF-8")).andExpect(status().isCreated());

	}

	@Test
	public void shouldUpdateCompanyByIdAndReturnStatusOk() throws Exception {

		CompanyDO companyDO = new CompanyDO("GOOGLE", "12345", "https://www.google.com");

		String json = json(CompanyMapper.makeCompanyDTO(companyDO));

		mockMvc.perform(put("/v1/companies/5b04b4263cc4de2024975107").content(json)
				.contentType(MediaType.APPLICATION_JSON_UTF8).accept("application/json;charset=UTF-8"))
				.andExpect(status().isOk());

	}

	@Test
	public void shouldDeleteCompanyByIdAndReturnStatusOk() throws Exception {
		mockMvc.perform(delete("/v1/companies/5b04bee43cc4de28f4100288").accept(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk());
	}

	@Test
	public void shouldFindSomethingByContainsNameAndZip() throws Exception {
		mockMvc.perform(get("/v1/companies/search").param("name", "goo").param("zip", "12345")
				.accept(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());
	}

	@Test
	public void shouldFindGoogleByContainsNameAndZip() throws Exception {

		MvcResult mvcResult = mockMvc
				.perform(get("/v1/companies/search").param("name", "goo").param("zip", "12345")
						.accept(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andReturn();

		CompanyDTO[] companyDTO = getCompaniesDTO(mvcResult.getResponse().getContentAsString());
		assertNotNull(companyDTO);
		assertEquals("5b04b4263cc4de2024975107", companyDTO[0].getId());
	}

	@Test
	public void shouldMergeDataAndReturnStatusClientError() throws Exception {

		LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
	    File file = new File(this.getClass().getClassLoader().getResource("client_data.pdf").getPath());
		parameters.add("csv", new FileSystemResource(file));
	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

	    HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<LinkedMultiValueMap<String, Object>>(parameters, headers);
	    ResponseEntity<String> response = testRestTemplate.exchange("http://localhost:"+localPort+"/v1/data", HttpMethod.POST, entity, String.class, "");
	    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

	}
	
	@Test
	public void shouldMergeDataAndReturnStatusOk() throws Exception {

		LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
	    File file = new File(this.getClass().getClassLoader().getResource("client_data.csv").getPath());
		parameters.add("csv", new FileSystemResource(file));
	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

	    HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<LinkedMultiValueMap<String, Object>>(parameters, headers);

	    ResponseEntity<String> response = testRestTemplate.exchange("http://localhost:"+localPort+"/v1/data", HttpMethod.POST, entity, String.class, "");
	    assertEquals(HttpStatus.OK, response.getStatusCode());

	}

	protected String json(Object o) throws IOException {
		return mapper.writeValueAsString(o);
	}

	protected CompanyDTO getCompanyDTO(String json) throws IOException {
		return mapper.readValue(json, CompanyDTO.class);
	}

	protected CompanyDTO[] getCompaniesDTO(String json) throws IOException {
		return mapper.readValue(json, CompanyDTO[].class);
	}

	protected CompanyDO createCompany(String id, String name, String zip, String website) {
		CompanyDO company = new CompanyDO();
		company.setId(id);
		company.setName(name);
		company.setZipCode(zip);
		company.setWebsite(website);

		return company;
	}

	@Bean
	public TestRestTemplate getTestRestTemplate() {
		return new TestRestTemplate();
	}
}
