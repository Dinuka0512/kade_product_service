package com.dinuka.dev.product_service.repository;

import com.dinuka.dev.product_service.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
    Optional<Vendor> findBySlug(String slug);
}
