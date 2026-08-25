package com.dinuka.dev.product_service.controller;

import com.dinuka.dev.product_service.model.Product;
import com.dinuka.dev.product_service.model.Vendor;
import com.dinuka.dev.product_service.service.ProductService;
import com.dinuka.dev.product_service.service.VendorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final VendorService vendorService;

    public ProductController(ProductService productService, VendorService vendorService) {
        this.productService = productService;
        this.vendorService = vendorService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long vendorId,
            @RequestParam(required = false) Boolean featured,
            @RequestParam(required = false) String sort) {
        return ResponseEntity.ok(productService.search(q, categoryId, vendorId, featured, sort));
    }

    @GetMapping("/{idOrSlug}")
    public ResponseEntity<Product> getById(@PathVariable String idOrSlug) {
        return ResponseEntity.ok(productService.findByIdOrSlug(idOrSlug));
    }

    @PostMapping
    public ResponseEntity<Product> create(
            @RequestParam String name,
            @RequestParam Double price,
            @RequestParam(required = false) Double compareAtPrice,
            @RequestParam String shortDescription,
            @RequestParam(required = false) String longDescription,
            @RequestParam Long categoryId,
            @RequestParam(required = false, defaultValue = "0") Integer stock,
            @RequestParam(required = false) String tags,
            @RequestParam(required = false) List<MultipartFile> images,
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestHeader(value = "X-User-Email", required = false) String email,
            @RequestHeader(value = "X-User-Name", required = false) String nameHeader,
            @RequestHeader(value = "X-User-Role", required = false) String role) {

        Long vendorId = resolveVendorIdForWrite(userId, email, nameHeader, role);
        return ResponseEntity.ok(productService.create(
                name, price, compareAtPrice, shortDescription, longDescription,
                categoryId, stock, tags, images, vendorId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam Double price,
            @RequestParam(required = false) Double compareAtPrice,
            @RequestParam String shortDescription,
            @RequestParam(required = false) String longDescription,
            @RequestParam Long categoryId,
            @RequestParam(required = false) Integer stock,
            @RequestParam(required = false) String tags,
            @RequestParam(required = false) List<MultipartFile> images,
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestHeader(value = "X-User-Email", required = false) String email,
            @RequestHeader(value = "X-User-Name", required = false) String nameHeader,
            @RequestHeader(value = "X-User-Role", required = false) String role) {

        if (userId == null || role == null || role.isBlank()) {
            throw new RuntimeException("Sign in to manage products");
        }
        Long vendorId;
        if ("vendor".equals(role)) {
            Vendor own = vendorService.findOrCreateForUser(userId, email, nameHeader);
            Product existing = productService.findById(id);
            if (!own.getId().equals(existing.getVendorId())) {
                throw new RuntimeException("You can only edit products from your own storefront");
            }
            vendorId = own.getId();
        } else if ("admin".equals(role)) {
            vendorId = null; // keep the product's current storefront
        } else {
            throw new RuntimeException("You do not have permission to manage products");
        }
        return ResponseEntity.ok(productService.update(
                id, name, price, compareAtPrice, shortDescription, longDescription,
                categoryId, stock, tags, images, vendorId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestHeader(value = "X-User-Email", required = false) String email,
            @RequestHeader(value = "X-User-Name", required = false) String nameHeader,
            @RequestHeader(value = "X-User-Role", required = false) String role) {

        if (userId == null || role == null || role.isBlank()) {
            throw new RuntimeException("Sign in to manage products");
        }
        if ("vendor".equals(role)) {
            Vendor own = vendorService.findOrCreateForUser(userId, email, nameHeader);
            Product existing = productService.findById(id);
            if (!own.getId().equals(existing.getVendorId())) {
                throw new RuntimeException("You can only delete products from your own storefront");
            }
        } else if (!"admin".equals(role)) {
            throw new RuntimeException("You do not have permission to manage products");
        }
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Vendors always act on their OWN storefront (the trusted identity headers
     * win over anything the client sends). Admins may target any storefront
     * via the legacy X-Vendor-Id header. Everyone else is rejected.
     */
    private Long resolveVendorIdForWrite(Long userId, String email, String nameHeader, String role) {
        if (userId == null || role == null || role.isBlank()) {
            throw new RuntimeException("Sign in as a vendor to create products");
        }
        switch (role) {
            case "vendor" -> {
                return vendorService.findOrCreateForUser(userId, email, nameHeader).getId();
            }
            case "admin" -> {
                return 1L; // platform-managed listing
            }
            default -> throw new RuntimeException("You do not have permission to create products");
        }
    }
}
