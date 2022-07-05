package com.savvycom.productservice.repository;

import com.savvycom.productservice.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByColorAndSizeAndPriceBetween(String color, String size, Long priceFrom, Long priceTo);
}
