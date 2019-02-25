package com.szymon.RestTutorial.orders.service;

import com.szymon.RestTutorial.orders.controller.Status;
import com.szymon.RestTutorial.orders.controller.OrderController;
import com.szymon.RestTutorial.orders.model.Order;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.ResourceSupport;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import org.springframework.stereotype.Component;

/**
 *
 * @author szymon
 */
@Component
public class OrderResourceAssembler implements ResourceAssembler<Order, Resource<Order>> {

    @Override
    public Resource<Order> toResource(Order order) {

        Resource<Order> orderResource = new Resource<>(order,
                linkTo(methodOn(OrderController.class).one(order.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).all()).withRel("orders")
        );

        if (order.getStatus() == Status.IN_PROGRESS) {
            
            orderResource.add(
                    linkTo(methodOn(OrderController.class)
                    .cancel(order.getId())).withRel("cancel"));
            
             orderResource.add(
                    linkTo(methodOn(OrderController.class)
                    .complete(order.getId())).withRel("complete"));
        }
        return orderResource;
    }

}
