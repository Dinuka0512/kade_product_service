package com.dinuka.dev.product_service.model;

import com.fasterxml.jackson.annotation.*;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private String shortDescription;

    @Column(columnDefinition = "TEXT")
    private String longDescription;

    @Column(nullable = false)
    private double price;

    private Double compareAtPrice;

    @Column(columnDefinition = "TEXT")
    private String imagesRaw;

    @Column(nullable = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long categoryId;

    private String categoryName;

    @Column(nullable = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long vendorId;

    private String vendorName;

    @Column(nullable = false)
    private int stock = 0;

    private int soldCount = 0;
    private double rating = 0;
    private int reviewCount = 0;

    @JsonProperty("isFeatured")
    private boolean featured = false;

    @Column(columnDefinition = "TEXT")
    private String tagsRaw;

    public Product() {}

    @JsonGetter("images")
    public List<String> getImages() {
        if (imagesRaw == null || imagesRaw.isBlank()) return List.of();
        return List.of(imagesRaw.split(",", -1));
    }

    @JsonSetter("images")
    public void setImagesFromList(List<String> images) {
        this.imagesRaw = images != null ? String.join(",", images) : "";
    }

    public void setImagesRaw(String images) {
        this.imagesRaw = images;
    }

    @JsonIgnore
    public String getImagesRaw() { return imagesRaw; }

    @JsonGetter("tags")
    public List<String> getTags() {
        if (tagsRaw == null || tagsRaw.isBlank()) return List.of();
        return List.of(tagsRaw.split(",", -1));
    }

    @JsonSetter("tags")
    public void setTagsFromList(List<String> tags) {
        this.tagsRaw = tags != null ? String.join(",", tags) : "";
    }

    public void setTagsRaw(String tags) {
        this.tagsRaw = tags;
    }

    @JsonIgnore
    public String getTagsRaw() { return tagsRaw; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

    public String getLongDescription() { return longDescription; }
    public void setLongDescription(String longDescription) { this.longDescription = longDescription; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public Double getCompareAtPrice() { return compareAtPrice; }
    public void setCompareAtPrice(Double compareAtPrice) { this.compareAtPrice = compareAtPrice; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public Long getVendorId() { return vendorId; }
    public void setVendorId(Long vendorId) { this.vendorId = vendorId; }

    public String getVendorName() { return vendorName; }
    public void setVendorName(String vendorName) { this.vendorName = vendorName; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public int getSoldCount() { return soldCount; }
    public void setSoldCount(int soldCount) { this.soldCount = soldCount; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public int getReviewCount() { return reviewCount; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }

    public boolean isFeatured() { return featured; }
    public void setFeatured(boolean featured) { this.featured = featured; }
}
