package com.jg.spring.reactive.webflux.EmployeeManagementWebflux;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jg.spring.reactive.webflux.EmployeeManagementWebflux.dto.EmployeeDTO;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;


import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;

public class StreamingApp {

	private static int ix = 0;
	private static MockWebServer mockBackEnd;
	private static ObjectMapper mapper = new ObjectMapper();
	private static Random r = new Random();

	public static void main(String[] args) throws IOException {
		final Dispatcher dispatcher = new Dispatcher() {

			@NotNull
			@Override
			public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) throws InterruptedException {
				String pathParam = recordedRequest.getPath().replaceAll("/slow/", "");
				
				List<EmployeeDTO> EmployeesPart2 = List.of(
						new EmployeeDTO((long) r.nextInt(100000), "Name" + pathParam),
						new EmployeeDTO((long) r.nextInt(100000), "Name" + pathParam),
						new EmployeeDTO((long) r.nextInt(100000), "Name" + pathParam));
				try {
					return new MockResponse()
							.setResponseCode(200)
							.setBody(mapper.writeValueAsString(EmployeesPart2))
							.setHeader("Content-Type", "application/json")
							.setBodyDelay(200, TimeUnit.MILLISECONDS);
				}
				catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				return null;
			}
		};
		mockBackEnd = new MockWebServer();
		mockBackEnd.setDispatcher(dispatcher);
		mockBackEnd.start(8082);
		System.setProperty("target.uri", "http://localhost:" + mockBackEnd.getPort());
		TestRestTemplate template = new TestRestTemplate();
		for (int i = 0; i < 20; i++) {
			new Thread(() -> {
				for (int j = 0; j < 10000; j++) {
					template.exchange("http://localhost:" + mockBackEnd.getPort() + "/employees/integration/{param}", HttpMethod.GET, null, EmployeeDTO[].class, ++ix);
				}
			}).start();
		}
	}
}
