package com.szymon.RestTutorial.orders.model;

import com.szymon.RestTutorial.orders.controller.Status;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 *
 * @author szymon
 */
@Data
@Entity
@Table(name = "CUSTOMER_ORDER")
public class Order {
    
    @Id
    @GeneratedValue
    private Long id;
    
    private String description;
    private Status status;

    public Order(String description, Status status) {
        this.description = description;
        this.status = status;
    }
    
    public Order(){
    }
    
    
    
}
