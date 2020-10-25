package com.jg.spring.reactive.webflux.EmployeeManagementWebflux;

import java.util.concurrent.TimeoutException;

import net.jodah.concurrentunit.Waiter;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import com.jg.spring.reactive.webflux.EmployeeManagementWebflux.dto.EmployeeDTO;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class EmployeeManagementSpringWebFluxTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeManagementSpringWebFluxTest.class);
    final WebClient client = WebClient.builder().baseUrl("http://localhost:8080").build();

    @Test
    public void testFindEmployeesJson() throws TimeoutException, InterruptedException {
    	LOGGER.info("Client subscribes: EmployeesJson");
        final Waiter waiter = new Waiter();
        Flux<EmployeeDTO> Employees = client.get().uri("/employees/json").retrieve().bodyToFlux(EmployeeDTO.class);
        Employees.subscribe(Employee -> {
            waiter.assertNotNull(Employee);
            LOGGER.info("Client subscribes: Employees Json :: {}", Employee);
            waiter.resume();
        });
        waiter.await(3000, 9);
    }

    @Test
    public void testFindEmployeesStream() throws TimeoutException, InterruptedException {
    	LOGGER.info("Client subscribes: EmployeesStream");
        final Waiter waiter = new Waiter();
        Flux<EmployeeDTO> Employees = client.get().uri("/employees/stream").retrieve().bodyToFlux(EmployeeDTO.class);
        Employees.subscribe(Employee -> {
            LOGGER.info("Client subscribes: Employees stream:: {}", Employee);
            waiter.assertNotNull(Employee);
            waiter.resume();
        });
        waiter.await(3000, 9);
    }

    @Test
    public void testFindEmployeesStreamBackPressure() throws TimeoutException, InterruptedException {
        final Waiter waiter = new Waiter();
        LOGGER.info("Client subscribes: Employees Stream back-pressure");
        Flux<EmployeeDTO> Employees = client.get().uri("/employees/stream/back-pressure").retrieve().bodyToFlux(EmployeeDTO.class);
        Employees.map(this::doSomeSlowWork).subscribe(employee -> {
            waiter.assertNotNull(employee);
            LOGGER.info("Client subscribes: Employees stream back-pressure:: {}", employee);
            waiter.resume();
        });
        waiter.await(3000, 9);
    }

    private EmployeeDTO doSomeSlowWork(EmployeeDTO Employee) {
        try {
            Thread.sleep(90);
        }
        catch (InterruptedException e) { }
        return Employee;
    }
}
