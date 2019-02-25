package com.szymon.RestTutorial.orders.model;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author szymon
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
    
}
