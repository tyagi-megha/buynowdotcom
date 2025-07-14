package com.dailycodework.buynowdotcom.controller;

import com.dailycodework.buynowdotcom.model.Category;
import com.dailycodework.buynowdotcom.response.ApiResponse;
import com.dailycodework.buynowdotcom.service.category.ICategoryService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    private final ICategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories() {
        //as exception  not handled in service class hence handling it here
            List<Category> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(new ApiResponse("Found", categories));


    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category) {

            Category theCategory = categoryService.addCategory(category);
            return ResponseEntity.ok(new ApiResponse("Added", theCategory));

    }

    @GetMapping("/category/{id}/category")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id) {

            Category theCategory = categoryService.findCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Found", theCategory));

    }

    @GetMapping("/category/{name}/category")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {

            Category theCategory = categoryService.findCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse("Found", theCategory));

    }

    @DeleteMapping("/category/{id}/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id) {

            categoryService.deleteCategory(id);
            return ResponseEntity.ok(new ApiResponse("Deleted", id));

    }

    @PutMapping("/category/{id}/update")
    public ResponseEntity<ApiResponse> updateCategory(@RequestBody Category category, @PathVariable Long id) {

            Category theCategory = categoryService.updateCategory(category, id);
            return ResponseEntity.ok(new ApiResponse("Updated", theCategory));

    }
}
