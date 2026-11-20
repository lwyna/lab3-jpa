package com.example.lab3.controller;

import com.example.lab3.dto.OrderRequest;
import com.example.lab3.entity.Order;
import com.example.lab3.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<Order> list(@AuthenticationPrincipal UserDetails user) {
        if (user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return orderService.findAll();
        }
        return orderService.findByUsername(user.getUsername());
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public Order create(@AuthenticationPrincipal UserDetails user, @RequestBody OrderRequest req) {
        return orderService.createOrder(user.getUsername(), req.getProductIds());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        orderService.delete(id);
    }
}