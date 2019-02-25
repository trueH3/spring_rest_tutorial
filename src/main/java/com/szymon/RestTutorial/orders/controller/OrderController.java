package com.szymon.RestTutorial.orders.controller;

import com.szymon.RestTutorial.orders.service.OrderNotFoundException;
import com.szymon.RestTutorial.orders.service.OrderResourceAssembler;
import com.szymon.RestTutorial.orders.model.Order;
import com.szymon.RestTutorial.orders.model.OrderRepository;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.VndErrors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author szymon
 */
@RestController
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderResourceAssembler assembler;

    public OrderController(OrderRepository orderRepository, OrderResourceAssembler assembler) {
        this.orderRepository = orderRepository;
        this.assembler = assembler;
    }

    @GetMapping(value = "/orders", produces = "application/json; charset=UTF-8")
    public Resources<Resource<Order>> all() {
        List<Resource<Order>> orders = orderRepository.findAll().stream()
                .map(a -> assembler.toResource(a)).collect(Collectors.toList());

        return new Resources<>(orders,
                linkTo(methodOn(OrderController.class).all()).withSelfRel()
        );
    }

    @GetMapping(value = "/orders/{id}", produces = "application/json; charset=UTF-8")
    public Resource<Order> one(@PathVariable Long id) {
        return assembler.toResource(orderRepository.findById(id).
                orElseThrow(() -> new OrderNotFoundException(id)));
    }

    @PostMapping(value = "/orders")
    public ResponseEntity newOrder(@RequestBody Order newOrder) throws URISyntaxException {

        newOrder.setStatus(Status.IN_PROGRESS);
        Resource<Order> resource = assembler.toResource(orderRepository.save(newOrder));
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @DeleteMapping(value = "/orders/{id}/cancel")
    public ResponseEntity cancel(@PathVariable Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

        if (order.getStatus() == Status.IN_PROGRESS) {
            order.setStatus(Status.CANCELLED);
            return ResponseEntity.ok(assembler.toResource(orderRepository.save(order)));
        }
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors.VndError("Method not allowed", "You can't cancel an order that is in the " + order.getStatus() + " status"));
    }

    @PutMapping(value = "/orders/{id}/complete")
    public ResponseEntity complete(@PathVariable Long id) {
        
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

        if(order.getStatus() == Status.IN_PROGRESS){
        order.setStatus(Status.COMPLETED);
        return ResponseEntity.ok(assembler.toResource(orderRepository.save(order)));
        }
        
        return ResponseEntity
			.status(HttpStatus.METHOD_NOT_ALLOWED)
			.body(new VndErrors.VndError("Method not allowed", "You can't complete an order that is in the " + order.getStatus() + " status"));
    }

}
