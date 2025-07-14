package com.dailycodework.buynowdotcom.service.category;

import com.dailycodework.buynowdotcom.model.Category;
import com.dailycodework.buynowdotcom.repository.CategoryRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
//this annotation is used to inject constructor creation. dependency must be declared final to let this annotation work
/*
Certainly! @RequiredArgsConstructor is a Lombok annotation used in Java to automatically generate a constructor
with required arguments. It creates a constructor containing final fields and fields annotated with @NonNull,
eliminating the need to manually define constructors.
How It Works:
If a field is marked as final or @NonNull, Lombok includes it in the generated constructor.
This is particularly useful for dependency injection, ensuring required fields are initialized properly.
 */
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{

    private final CategoryRepository categoryRepository;

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category)
                .filter(c -> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository :: save)
                .orElseThrow(() ->
                    new EntityExistsException(category.getName() + " already exists"));
        /*
        simplified version:
        return categoryRepository.existsByName(category.getName())
    ? throw new EntityExistsException(category.getName() + " already exists")
    : categoryRepository.save(category);
         */
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        return Optional.ofNullable(
                findCategoryById(categoryId)).map(oldCategory -> {
                    oldCategory.setName(category.getName());
                    return categoryRepository.save(oldCategory);
        }).orElseThrow(() -> new EntityNotFoundException("Category not found!"));

      /* more simplified version
       return Optional.ofNullable(findCategoryById(categoryId))
                .map(categoryRepository::save)
                .orElseThrow(() -> new EntityNotFoundException("Category not found!")); */
    }

    @Override
    public void deleteCategory(Long categoryId) {
        categoryRepository.findById(categoryId)
                .ifPresentOrElse(categoryRepository :: delete,
                        () -> {
                    throw new EntityNotFoundException("Category not found") ;
                        }
                );
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                     return new EntityNotFoundException("Category not found");
                });
    }

    @Override
    public Category findCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }
}
