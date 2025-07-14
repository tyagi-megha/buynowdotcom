package com.dailycodework.buynowdotcom.service.product;

import com.dailycodework.buynowdotcom.dto.ImageDto;
import com.dailycodework.buynowdotcom.dto.ProductDto;
import com.dailycodework.buynowdotcom.model.*;
import com.dailycodework.buynowdotcom.repository.*;
import com.dailycodework.buynowdotcom.request.AddProductRequest;
import com.dailycodework.buynowdotcom.request.ProductUpdateRequest;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    @Override
    public Product addProducts(AddProductRequest request) {
        if(productExists(request.getName(), request.getBrand())){
            throw new EntityExistsException(request.getName() + " already exists");
        }
        //else need to use CategoryRepository to check if category already exists, if does then simply add product to it
        //else add category as new category in category table
        /*
        This Java code snippet is handling the retrieval or creation of a Category entity using Optional and
        the repository pattern.
Breakdown of what’s happening:
Find the Category:
categoryRepository.findByName(request.getCategory().getName()) attempts to fetch an existing category by name from the database.
Optional.ofNullable(...) ensures that if the result is null, it wraps it in an Optional.
Handle Missing Category:
.orElseGet(() -> { ... }) executes a fallback function if no existing category is found.
It creates a new Category object with the name retrieved from request.
The new category is saved to the database using categoryRepository.save(newCategory).
What does this accomplish?
If the category already exists, it returns the existing entity.
If the category does not exist, it creates and persists a new one in the database.
This approach ensures that a category is always available and avoids potential null references.
         */
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        //set the category got above to product category
        request.setCategory(category);

        return productRepository.save(createProduct(request, category));
    }

    //before adding should check if product already exists by name and brand
    private boolean productExists(String name, String brand) {
        return productRepository.existsByNameAndBrand(name, brand);
    }

    //help us to save attributes for new products.AddProductRequest is a DTO to specify attributes we're going to take from
    //Product entity
    private Product createProduct(AddProductRequest request, Category category ) {
        return new Product(request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category);
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
        /*
        This statement is handling the retrieval, update, and saving of a Product entity using Java Streams and Optional in a clean, functional programming style. Let’s break it down step by step:
Find the Product by ID
productRepository.findById(productId):
Looks for a Product with the given productId.
Returns an Optional<Product>, meaning it could contain a value or be empty.
Map Over the Existing Product & Update It
.map(existingProduct -> updateExistingProduct(existingProduct, request)):
If a product is found, this function updates the existing product using updateExistingProduct(), which likely modifies its fields based on request.
If no product exists, nothing happens at this step.
Save the Updated Product
.map(productRepository::save):
Takes the updated product and saves it back into the repository.
If the product wasn’t found initially, this step is skipped.
Handle the Case Where Product Doesn't Exist
.orElseThrow(() -> new EntityNotFoundException("Product not found")):
If the product was never found, this throws an EntityNotFoundException, signaling that the product does not exist in the database.
What’s the Purpose?
Ensures only existing products are updated.
Uses functional programming to improve readability.
Avoids null checks by leveraging Optional.
Throws a clear exception when the product doesn’t exist.

         */
        return productRepository.findById(productId)
                .map(existingProduct -> updateExisitingProduct(existingProduct, request))
                .map(productRepository :: save).orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    private Product updateExisitingProduct(Product existingProduct, ProductUpdateRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());
        //makes sure category sent for update is already in db
        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public void deleteProductById(Long productId) {
        productRepository.findById(productId)
                .ifPresentOrElse(product -> {
                    List<CartItem> cartItems = cartItemRepository.findByProductId(productId);
                    cartItems.forEach(cartItem -> {
                        Cart cart = cartItem.getCart();
                        cart.removeItem(cartItem);
                        cartItemRepository.delete(cartItem);
                    });

                    List<OrderItem> orderItems = orderItemRepository.findByProductId(productId);
                    orderItems.forEach(orderItem -> {
                        orderItem.setProduct(null);
                        orderItemRepository.save(orderItem);
                    });

                    Optional.ofNullable(product.getCategory()).ifPresent(category -> {
                        category.getProducts().remove(product);
                    });
                    product.setCategory(null);

                    productRepository.deleteById(product.getId());
                }, () -> {
                    throw new EntityNotFoundException("Product not found");
                });

    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    //custom JPA finder method, will be created by us in productrepository
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<ProductDto> convertedProducts(List<Product> products) {
        return products.stream().map(this::converttToDto).toList();
        //lambda refrenece will be:  return products.stream().map(product-> convertToDto(product)).toList();
    }
    @Override
    public ProductDto converttToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream().map(image -> modelMapper.map(image, ImageDto.class)).toList();
        productDto.setImages(imageDtos);
        return productDto;
    }
}
