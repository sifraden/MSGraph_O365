package com.demo.api.myseat;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.demo.model.myseat.chairs.Content;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MySeatAPIServiceImpl implements MySeatAPIService {

	private static final Logger LOG = LoggerFactory.getLogger(MySeatAPIServiceImpl.class);

	RestTemplate restTemplate = new RestTemplate();

	public Content getChairs(String keyApi) throws Exception{
		LOG.debug("Entering to getChairs ");
		CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier())
				.build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);
		restTemplate = new RestTemplate(requestFactory);
		ResponseEntity<String> response = restTemplate.getForEntity("https://apiV3.myseat.fr/Request/GetChairs/key/" + keyApi, String.class);
		
		ObjectMapper objectMapper = new ObjectMapper();
		Content chairs = objectMapper.readValue(response.getBody(), Content.class);
		
		return chairs;
	}

	public Content getChairsInGroup(String keyApi, String groupId) throws Exception {
		LOG.debug("Entering to getChairsInGroup for this group: {} ", groupId);
		CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier())
				.build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);
		restTemplate = new RestTemplate(requestFactory);
		ResponseEntity<String> response = restTemplate.getForEntity("https://apiV3.myseat.fr/Request/GetChairsInGroup/key/" + keyApi + "/id/" + groupId, String.class);

		ObjectMapper objectMapper = new ObjectMapper();
		Content chairs = objectMapper.readValue(response.getBody(), Content.class);
		
		return chairs;
	}

}
