package com.dinuka.dev.product_service.service;

import com.dinuka.dev.product_service.dto.ProductInput;
import com.dinuka.dev.product_service.model.Category;
import com.dinuka.dev.product_service.model.Product;
import com.dinuka.dev.product_service.model.Vendor;
import com.dinuka.dev.product_service.repository.CategoryRepository;
import com.dinuka.dev.product_service.repository.ProductRepository;
import com.dinuka.dev.product_service.repository.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    public List<Product> search(String q, Long categoryId, Long vendorId, Boolean featured, String sort) {
        List<Product> products = productRepository.findAll();

        if (q != null && !q.isBlank()) {
            String lower = q.toLowerCase();
            products = products.stream()
                    .filter(p -> p.getName().toLowerCase().contains(lower)
                            || (p.getShortDescription() != null && p.getShortDescription().toLowerCase().contains(lower)))
                    .toList();
        }
        if (categoryId != null) {
            products = products.stream().filter(p -> p.getCategoryId().equals(categoryId)).toList();
        }
        if (vendorId != null) {
            products = products.stream().filter(p -> p.getVendorId().equals(vendorId)).toList();
        }
        if (featured != null) {
            products = products.stream().filter(p -> p.isFeatured() == featured).toList();
        }

        for (Product p : products) {
            enrichProduct(p);
        }

        if (sort != null) {
            products = new java.util.ArrayList<>(products);
            switch (sort) {
                case "price-asc" -> products.sort((a, b) -> Double.compare(a.getPrice(), b.getPrice()));
                case "price-desc" -> products.sort((a, b) -> Double.compare(b.getPrice(), a.getPrice()));
                case "rating" -> products.sort((a, b) -> Double.compare(b.getRating(), a.getRating()));
            }
        }

        return products;
    }

    public Product findById(Long id) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        enrichProduct(p);
        return p;
    }

    public Product findByIdOrSlug(String idOrSlug) {
        try {
            Long id = Long.parseLong(idOrSlug);
            return findById(id);
        } catch (NumberFormatException e) {
            Product p = productRepository.findBySlug(idOrSlug)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            enrichProduct(p);
            return p;
        }
    }

    public Product create(ProductInput input, Long vendorId) {
        String slug = input.getName().toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");

        Product product = new Product();
        product.setName(input.getName());
        product.setSlug(slug);
        product.setShortDescription(input.getShortDescription());
        product.setLongDescription(input.getLongDescription() != null ? input.getLongDescription() : "");
        product.setPrice(input.getPrice());
        product.setCompareAtPrice(input.getCompareAtPrice());
        product.setCategoryId(input.getCategoryId());
        product.setVendorId(vendorId);
        product.setStock(input.getStock() != null ? input.getStock() : 0);

        if (input.getImages() != null && !input.getImages().isEmpty()) {
            product.setImagesRaw(String.join(",", input.getImages()));
        } else {
            product.setImagesRaw("https://picsum.photos/seed/" + slug + "/800/800");
        }

        if (input.getTags() != null) {
            product.setTagsRaw(String.join(",", input.getTags()));
        }

        Product saved = productRepository.save(product);
        enrichProduct(saved);
        return saved;
    }

    public Product update(Long id, ProductInput input) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        String slug = input.getName().toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");

        product.setName(input.getName());
        product.setSlug(slug);
        product.setShortDescription(input.getShortDescription());
        if (input.getLongDescription() != null) product.setLongDescription(input.getLongDescription());
        product.setPrice(input.getPrice());
        product.setCompareAtPrice(input.getCompareAtPrice());
        product.setCategoryId(input.getCategoryId());
        if (input.getStock() != null) product.setStock(input.getStock());

        if (input.getImages() != null && !input.getImages().isEmpty()) {
            product.setImagesRaw(String.join(",", input.getImages()));
        }

        if (input.getTags() != null) {
            product.setTagsRaw(String.join(",", input.getTags()));
        }

        Product saved = productRepository.save(product);
        enrichProduct(saved);
        return saved;
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    private void enrichProduct(Product product) {
        categoryRepository.findById(product.getCategoryId())
                .ifPresent(cat -> product.setCategoryName(cat.getName()));
        vendorRepository.findById(product.getVendorId())
                .ifPresent(vendor -> product.setVendorName(vendor.getName()));
    }
}
