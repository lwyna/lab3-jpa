package com.example.lab3.service;

import com.example.lab3.dto.OrderDto;
import com.example.lab3.dto.OrderItemDto;
import com.example.lab3.entity.Order;
import com.example.lab3.entity.OrderItem;
import com.example.lab3.entity.Product;
import com.example.lab3.repository.OrderRepository;
import com.example.lab3.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepo;
    private final ProductRepository productRepo;

    @Transactional
    public OrderDto create(OrderDto dto) {

        Order order = new Order();
        List<OrderItem> items = new ArrayList<>();


        for (OrderItemDto itemDto : dto.items()) {

            Product product = productRepo.findById(itemDto.productId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + itemDto.productId()));


            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(itemDto.quantity());
            item.setOrder(order);

            items.add(item);
        }


        order.setItems(items);
        order.setOrderDate(java.time.LocalDateTime.now());


        Order savedOrder = orderRepo.save(order);

        return mapToDto(savedOrder);
    }

    public OrderDto findById(Long id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return mapToDto(order);
    }

    private OrderDto mapToDto(Order order) {
        List<OrderItemDto> itemDtos = order.getItems().stream()
                .map(item -> new OrderItemDto(item.getId(), item.getProduct().getId(), item.getQuantity()))
                .toList();
        return new OrderDto(order.getId(), order.getOrderDate(), itemDtos);
    }
}