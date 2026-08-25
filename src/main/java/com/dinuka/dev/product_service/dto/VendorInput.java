package com.dinuka.dev.product_service.dto;

import jakarta.validation.constraints.NotBlank;

public class VendorInput {

    @NotBlank(message = "Name is required")
    private String name;

    private String tagline;
    private String description;
    private String location;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTagline() { return tagline; }
    public void setTagline(String tagline) { this.tagline = tagline; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}
