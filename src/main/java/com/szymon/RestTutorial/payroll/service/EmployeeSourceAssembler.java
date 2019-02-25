package com.szymon.RestTutorial.payroll.service;

import com.szymon.RestTutorial.payroll.controller.EmployeeController;
import com.szymon.RestTutorial.payroll.model.Employee;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import org.springframework.stereotype.Component;

/**
 *
 * @author szymon
 */
@Component
public class EmployeeSourceAssembler implements ResourceAssembler<Employee, Resource<Employee>> {

    @Override
    public Resource<Employee> toResource(Employee e) {
        return new Resource<>(e,
                linkTo(methodOn(EmployeeController.class).one(e.getId())).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).all()).withRel("employees")
        );
    }

}
