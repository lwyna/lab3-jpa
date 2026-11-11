package com.example.lab3.service;

import com.example.lab3.dto.CategoryDto;
import com.example.lab3.entity.Category;
import com.example.lab3.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepo;

    @Transactional
    public CategoryDto create(CategoryDto dto) {
        Category category = new Category(null, dto.name(), null);
        Category saved = categoryRepo.save(category);
        return new CategoryDto(saved.getId(), saved.getName());
    }

    public CategoryDto findById(Long id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        return new CategoryDto(category.getId(), category.getName());
    }

    public List<CategoryDto> findAll() {
        return categoryRepo.findAll().stream()
                .map(cat -> new CategoryDto(cat.getId(), cat.getName()))
                .toList();
    }
}