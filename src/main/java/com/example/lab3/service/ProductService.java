package com.example.lab3.service;

import com.example.lab3.dto.ProductDto;
import com.example.lab3.entity.Category;
import com.example.lab3.entity.Product;
import com.example.lab3.repository.CategoryRepository;
import com.example.lab3.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    @Transactional
    public ProductDto create(ProductDto dto) {
        Category cat = categoryRepo.findById(dto.categoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        Product p = new Product(null, dto.name(), dto.price(), cat);
        Product saved = productRepo.save(p);
        return new ProductDto(saved.getId(), saved.getName(), saved.getPrice(), cat.getId());
    }

    public ProductDto findById(Long id) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        return new ProductDto(p.getId(), p.getName(), p.getPrice(), p.getCategory().getId());
    }

    @Transactional
    public ProductDto update(Long id, ProductDto dto) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        Category cat = categoryRepo.findById(dto.categoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        p.setName(dto.name());
        p.setPrice(dto.price());
        p.setCategory(cat);

        Product saved = productRepo.save(p);
        return new ProductDto(saved.getId(), saved.getName(), saved.getPrice(), cat.getId());
    }

    @Transactional
    public void delete(Long id) {
        if (!productRepo.existsById(id)) {
            throw new EntityNotFoundException("Product not found");
        }
        productRepo.deleteById(id);
    }


    public Page<ProductDto> findAll(Pageable pageable) {
        return productRepo.findAll(pageable)
                .map(p -> new ProductDto(p.getId(), p.getName(), p.getPrice(), p.getCategory().getId()));
    }


    public List<ProductDto> findByCategoryId(Long categoryId) {
        return productRepo.findByCategoryId(categoryId).stream()
                .map(p -> new ProductDto(p.getId(), p.getName(), p.getPrice(), p.getCategory().getId()))
                .toList();
    }
}