package com.dinuka.dev.product_service.service;

import com.dinuka.dev.product_service.model.Product;
import com.dinuka.dev.product_service.repository.CategoryRepository;
import com.dinuka.dev.product_service.repository.ProductRepository;
import com.dinuka.dev.product_service.repository.VendorRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;
    private final GcpStorageService gcpStorageService;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, 
                          VendorRepository vendorRepository, GcpStorageService gcpStorageService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
        this.gcpStorageService = gcpStorageService;
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

    public Product create(String name, Double price, Double compareAtPrice, String shortDescription,
                          String longDescription, Long categoryId, Integer stock, String tags,
                          List<MultipartFile> images, Long vendorId) {
        String slug = name.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");

        Product product = new Product();
        product.setName(name);
        product.setSlug(slug);
        product.setShortDescription(shortDescription);
        product.setLongDescription(longDescription != null ? longDescription : "");
        product.setPrice(price);
        product.setCompareAtPrice(compareAtPrice);
        product.setCategoryId(categoryId);
        product.setVendorId(vendorId);
        product.setStock(stock != null ? stock : 0);

        // Upload images to GCP Storage
        if (images != null && !images.isEmpty()) {
            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    try {
                        String imageUrl = gcpStorageService.uploadFile(image);
                        imageUrls.add(imageUrl);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to upload image to GCP Storage", e);
                    }
                }
            }
            if (!imageUrls.isEmpty()) {
                product.setImagesRaw(String.join(",", imageUrls));
            } else {
                product.setImagesRaw("https://picsum.photos/seed/" + slug + "/800/800");
            }
        } else {
            product.setImagesRaw("https://picsum.photos/seed/" + slug + "/800/800");
        }

        if (tags != null && !tags.isBlank()) {
            product.setTagsRaw(tags);
        }

        Product saved = productRepository.save(product);
        enrichProduct(saved);
        return saved;
    }

    public Product update(Long id, String name, Double price, Double compareAtPrice, String shortDescription,
                          String longDescription, Long categoryId, Integer stock, String tags,
                          List<MultipartFile> images, Long vendorId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        String slug = name.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");

        product.setName(name);
        product.setSlug(slug);
        product.setShortDescription(shortDescription);
        if (longDescription != null) product.setLongDescription(longDescription);
        product.setPrice(price);
        product.setCompareAtPrice(compareAtPrice);
        product.setCategoryId(categoryId);
        if (stock != null) product.setStock(stock);
        if (vendorId != null) product.setVendorId(vendorId);

        // Upload new images to GCP Storage if provided
        if (images != null && !images.isEmpty()) {
            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    try {
                        String imageUrl = gcpStorageService.uploadFile(image);
                        imageUrls.add(imageUrl);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to upload image to GCP Storage", e);
                    }
                }
            }
            if (!imageUrls.isEmpty()) {
                product.setImagesRaw(String.join(",", imageUrls));
            }
        }

        if (tags != null && !tags.isBlank()) {
            product.setTagsRaw(tags);
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
