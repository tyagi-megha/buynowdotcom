package com.dailycodework.buynowdotcom.controller;

import com.dailycodework.buynowdotcom.dto.ProductDto;
import com.dailycodework.buynowdotcom.model.Product;
import com.dailycodework.buynowdotcom.request.AddProductRequest;
import com.dailycodework.buynowdotcom.request.ProductUpdateRequest;
import com.dailycodework.buynowdotcom.response.ApiResponse;
import com.dailycodework.buynowdotcom.service.product.IProductService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {

    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts(){

        List<Product> products = productService.getAllProducts();
        List<ProductDto> convertedProducts = productService.convertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Found", convertedProducts));

    }

    @GetMapping("/product/{productId}/product")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId){

            Product product = productService.getProductById(productId);
            ProductDto productDto = productService.converttToDto(product);
            return ResponseEntity.ok(new ApiResponse("Found", productDto));

    }

    @GetMapping("/product/productByCategoryAndBrand")
    public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(@RequestParam String category,@RequestParam String brand){

            List<Product> products = productService.getProductsByCategoryAndBrand(category, brand);
            List<ProductDto> productDtos = productService.convertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Found", productDtos));

    }

    @GetMapping("/product/{category}/productByCategory")
    public ResponseEntity<ApiResponse> getProductsByCategory(@PathVariable String category){

            List<Product> products = productService.getProductsByCategory(category);
            List<ProductDto> productDtos = productService.convertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Found", productDtos));

    }

    @GetMapping("/product/productByBrandAndName")
    public ResponseEntity<ApiResponse> getProductsByBrandAndName(@RequestParam String brand, @RequestParam String name){

            List<Product> products = productService.getProductsByBrandAndName(brand, name);
            List<ProductDto> productDtos = productService.convertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Found", productDtos));

    }

    @GetMapping("/product/{name}/productByName")
    public ResponseEntity<ApiResponse> getProductsByName( @PathVariable String name){

            List<Product> products = productService.getProductsByName( name);
            List<ProductDto> productDtos = productService.convertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Found", productDtos));

    }

    @GetMapping("/product/{brand}/productByBrand")
    public ResponseEntity<ApiResponse> getProductsByBrand( @PathVariable String brand){

            List<Product> products = productService.getProductsByBrand( brand);
            List<ProductDto> productDtos = productService.convertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Found", productDtos));

    }

    @DeleteMapping("/product/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProductById(@PathVariable Long productId){

            productService.deleteProductById(productId);
            return ResponseEntity.ok().body(new ApiResponse("Deleted product", productId));

    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProducts(@RequestBody AddProductRequest request){

            Product product = productService.addProducts(request);
            ProductDto productDto = productService.converttToDto(product);
            return ResponseEntity.ok(new ApiResponse("Product added successfully!", productDto));
    }

    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest request,@PathVariable Long productId){

            Product product = productService.updateProduct(request, productId);
            ProductDto productDto = productService.converttToDto(product);
            return ResponseEntity.ok(new ApiResponse("Updated", productDto));

    }

}
