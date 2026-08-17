package com.dinuka.dev.product_service.controller;

import com.dinuka.dev.product_service.dto.ProductInput;
import com.dinuka.dev.product_service.model.Product;
import com.dinuka.dev.product_service.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
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
    public ResponseEntity<Product> create(@Valid @RequestBody ProductInput input,
                                          @RequestHeader(value = "X-Vendor-Id", defaultValue = "1") Long vendorId) {
        return ResponseEntity.ok(productService.create(input, vendorId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @Valid @RequestBody ProductInput input) {
        return ResponseEntity.ok(productService.update(id, input));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
