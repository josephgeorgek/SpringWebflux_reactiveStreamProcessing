package com.jg.spring.reactive.webflux.EmployeeManagementWebflux.controller;

import java.time.Duration;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.jg.spring.reactive.webflux.EmployeeManagementWebflux.entity.Employee;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/employees")
public class EmployeeSteamController {

    @Autowired
    ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    WebClient client;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeSteamController.class);

    @GetMapping("/json")
    public Flux<Employee> findEmployeesJson() {
    	LOGGER.info("Server produces: Employees Json");
        return Flux.fromStream(this::prepareStream)
                .doOnNext(employee -> LOGGER.info("Server produces: {}", employee));
    }

    @GetMapping(value = "/stream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Employee> findEmployeesStream() {
    	LOGGER.info("Server produces: Employees Stream");
        return Flux.fromStream(this::prepareStream).delaySequence(Duration.ofMillis(100))
                .doOnNext(employee -> LOGGER.info("Server produces:Employees Stream : {}", employee));
    }

    @GetMapping(value = "/stream/back-pressure", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Employee> findEmployeesStreamBackPressure() {
    	LOGGER.info("Server produces: Employees Stream BackPressure");
        return Flux.fromStream(this::prepareStream).delayElements(Duration.ofMillis(100))
                .doOnNext(employee -> LOGGER.info("Server produces:Employees Stream BackPressure:: {}", employee));
    }

    private Stream<Employee> prepareStream() {
        return Stream.of(
            new Employee(1, "Name01"),
            new Employee(2, "Name02"),
            new Employee(3, "Name03"),
            new Employee(4, "Name04"),
            new Employee(5, "Name05"),
            new Employee(6, "Name06"),
            new Employee(7, "Name07"),
            new Employee(8, "Name08"),
            new Employee(9, "Name09")
        );
    }

	private Stream<Employee> prepareStreamPart1() {
		return Stream.of(
				  new Employee(1, "Name01"),
		            new Employee(2, "Name02"),
		            new Employee(3, "Name03")
		);
	}

    @GetMapping("/integration/{param}")
    public Flux<Employee> findEmployeesIntegration(@PathVariable("param") String param) {
        return Flux.fromStream(this::prepareStreamPart1).log()
				.mergeWith(
						client.get().uri("/slow/" + param)
								.retrieve()
								.bodyToFlux(Employee.class)
								.log()
				);
    }

    @GetMapping("/integration-in-different-pool/{param}")
    public Flux<Employee> findEmployeesIntegrationInDifferentPool(@PathVariable("param") String param) {
		return Flux.fromStream(this::prepareStreamPart1).log()
				.mergeWith(
						client.get().uri("/slow/" + param)
								.retrieve()
								.bodyToFlux(Employee.class)
								.log()
								.publishOn(Schedulers.fromExecutor(taskExecutor))
				);
    }

}
