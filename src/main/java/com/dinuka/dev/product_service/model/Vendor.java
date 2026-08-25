package com.dinuka.dev.product_service.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "vendors")
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    private String tagline;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String logo;
    private String cover;
    private String location;

    private double rating = 0;
    private int reviewCount = 0;
    private LocalDate joinedAt = LocalDate.now();
    private int productCount = 0;

    @Column(nullable = false)
    private String status = "active";

    /** Linked user account (from user_service) that owns this storefront. */
    @Column(name = "user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    public Vendor() {}

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getTagline() { return tagline; }
    public void setTagline(String tagline) { this.tagline = tagline; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }

    public String getCover() { return cover; }
    public void setCover(String cover) { this.cover = cover; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public int getReviewCount() { return reviewCount; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }

    @JsonIgnore
    public LocalDate getJoinedAtDate() { return joinedAt; }

    @JsonGetter("joinedAt")
    public String getJoinedAt() {
        return joinedAt != null ? joinedAt.toString() : null;
    }

    public void setJoinedAt(LocalDate joinedAt) { this.joinedAt = joinedAt; }

    public int getProductCount() { return productCount; }
    public void setProductCount(int productCount) { this.productCount = productCount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
