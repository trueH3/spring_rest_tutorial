package com.szymon.RestTutorial;

import com.szymon.RestTutorial.orders.model.Order;
import com.szymon.RestTutorial.orders.model.OrderRepository;
import com.szymon.RestTutorial.orders.controller.Status;
import com.szymon.RestTutorial.payroll.model.Employee;
import com.szymon.RestTutorial.payroll.model.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author szymon
 */
@Configuration
@Slf4j
public class LoadDatabase {

    @Bean
    public CommandLineRunner initDatabase(EmployeeRepository eRepository, OrderRepository oRepository) {

        return args -> {
            log.info("Preloading " + eRepository.save(new Employee("Bilbo", "Baggins", "burglar")));
            log.info("Preloading " + eRepository.save(new Employee("Frodo", "Baggins", "thief")));
            log.info("Preloading " + oRepository.save(new Order("MacBook Pro", Status.COMPLETED)));
            log.info("Preloading " + oRepository.save(new Order("iPhone", Status.IN_PROGRESS)));
        };

    }

}
