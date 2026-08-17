package com.dinuka.dev.product_service.repository;

import com.dinuka.dev.product_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByFeaturedTrue();

    Optional<Product> findBySlug(String slug);

    long countByVendorId(Long vendorId);
}
