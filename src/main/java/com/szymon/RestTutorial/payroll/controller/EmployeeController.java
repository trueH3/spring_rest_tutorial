package com.szymon.RestTutorial.payroll.controller;

import com.szymon.RestTutorial.payroll.service.EmployeeNotFoundException;
import com.szymon.RestTutorial.payroll.service.EmployeeSourceAssembler;
import com.szymon.RestTutorial.payroll.model.EmployeeRepository;
import com.szymon.RestTutorial.payroll.model.Employee;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author szymon
 */
@Slf4j
@RestController
public class EmployeeController {

    private final EmployeeRepository repository;
    private final EmployeeSourceAssembler assembler;

    public EmployeeController(EmployeeRepository repository, EmployeeSourceAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping(value = "/employees", produces = "application/json; charset=UTF-8")
    public Resources<Resource<Employee>> all() {
        List<Resource<Employee>> employees = repository.findAll().stream()
                .map(e -> assembler.toResource(e))
                .collect(Collectors.toList());

        return new Resources<>(employees,
                linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }

    @PostMapping("/employees")
    public ResponseEntity newEmployee(@RequestBody Employee newEmployee) throws URISyntaxException {
        Resource<Employee> resource = assembler.toResource(repository.save(newEmployee));
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @GetMapping(value = "/employees/{id}", produces = "application/json; charset=UTF-8")
    public Resource<Employee> one(@PathVariable Long id) {

        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        return assembler.toResource(employee);
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
        return repository.findById(id)
                .map(e -> {
                    e.setName(newEmployee.getName());
                    e.setRole(newEmployee.getRole());
                    Resource<Employee> resource = assembler.toResource(repository.save(e));
                    ResponseEntity re = null;
                    try {
                        re = ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
                    } catch (URISyntaxException ex) {
                        log.error("error", ex);
                    }
                    return re;
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    Resource<Employee> resource = assembler.toResource(repository.save(newEmployee));
                    ResponseEntity re = null;
                    try {
                        re = ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
                    } catch (URISyntaxException ex) {
                        log.error("error", ex);
                    }
                    return re;
                });
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}