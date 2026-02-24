package com.skishop.service;

import com.skishop.model.entity.Category;
import com.skishop.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService extends BaseService<Category, String> {
    public CategoryService(CategoryRepository repository) { super(repository); }
}
