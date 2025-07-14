package com.dailycodework.buynowdotcom.service.product;

import com.dailycodework.buynowdotcom.dto.ProductDto;
import com.dailycodework.buynowdotcom.model.Product;
import com.dailycodework.buynowdotcom.request.AddProductRequest;
import com.dailycodework.buynowdotcom.request.ProductUpdateRequest;

import java.util.List;

public interface IProductService {

    Product addProducts(AddProductRequest request);

    Product updateProduct(ProductUpdateRequest request, Long productId);

    Product getProductById(Long productId);

    void deleteProductById(Long productId);

    List<Product> getAllProducts();
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrandAndName(String brand, String name);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrand(String brand);

    List<ProductDto> convertedProducts(List<Product> products);

    ProductDto converttToDto(Product product);
}
