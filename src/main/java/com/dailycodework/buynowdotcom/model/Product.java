package com.dailycodework.buynowdotcom.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
/*
In Spring Boot, the @NoArgsConstructor annotation (from Lombok) is used to automatically generate a no-argument constructor
for a class. This is particularly useful when working with JPA entities, DTOs, and dependency injection.
Why Use @NoArgsConstructor?
✅ Required by JPA (Hibernate) – JPA needs a default constructor for entity mapping.
✅ Avoid Manual Code – Reduces boilerplate by automatically creating constructors.
✅ Supports Dependency Injection – Spring Beans often require no-arg constructors.
✅ Useful in Serialization – Helps frameworks like Jackson deserialize JSON to objects.
 */
@NoArgsConstructor
//to make it a table in database
@Entity
public class Product {

    @Id //PK
    @GeneratedValue(strategy= GenerationType.IDENTITY)//generated value for the auto generate
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;

    //cascade allows you to propagate the remove operation from the parent entity to its child entities.
    //orphanremoval allows you to remove the child table row upon removing the child entity from the collection.
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    //not adding ALLARGCONSTR because we dont need id and images to be added hence
    //creating our own constructor
    //createProduct in service class using the below constructor hence didnt use AllArgsConstructor
    public Product(String name, String brand, BigDecimal price, int inventory, String description, Category category) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.inventory = inventory;
        this.description = description;
        this.category = category;
    }


}
