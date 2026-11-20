package com.example.lab3.service;

import com.example.lab3.entity.*;
import com.example.lab3.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo; // <-- Nouveau


    @Transactional
    public Order createOrder(String username, List<Long> productIds) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());

        List<OrderItem> items = new ArrayList<>();
        for (Long productId : productIds) {
            Product product = productRepo.findById(productId)
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(1);
            item.setOrder(order);
            items.add(item);
        }
        order.setItems(items);
        return orderRepo.save(order);
    }


    public List<Order> findAll() { return orderRepo.findAll(); }

    public List<Order> findByUsername(String username) {
        return orderRepo.findByUserUsername(username);
    }

    public void delete(Long id) { orderRepo.deleteById(id); }
}