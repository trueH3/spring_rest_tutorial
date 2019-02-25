package com.szymon.RestTutorial.payroll.model;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author szymon
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
}
