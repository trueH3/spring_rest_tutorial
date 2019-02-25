package com.szymon.RestTutorial.orders.service;

/**
 *
 * @author szymon
 */
public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(Long id) {
        super("Could not found order "+id);
    }
}
