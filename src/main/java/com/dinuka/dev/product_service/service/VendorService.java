package com.dinuka.dev.product_service.service;

import com.dinuka.dev.product_service.dto.VendorInput;
import com.dinuka.dev.product_service.model.Vendor;
import com.dinuka.dev.product_service.repository.VendorRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class VendorService {

    private final VendorRepository vendorRepository;
    private final GcpStorageService gcpStorageService;

    public VendorService(VendorRepository vendorRepository, GcpStorageService gcpStorageService) {
        this.vendorRepository = vendorRepository;
        this.gcpStorageService = gcpStorageService;
    }

    public List<Vendor> findAll() {
        return vendorRepository.findAll();
    }

    public Vendor findById(Long id) {
        return vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
    }

    public Vendor findByIdOrSlug(String idOrSlug) {
        try {
            Long id = Long.parseLong(idOrSlug);
            return findById(id);
        } catch (NumberFormatException e) {
            return vendorRepository.findBySlug(idOrSlug)
                    .orElseThrow(() -> new RuntimeException("Vendor not found"));
        }
    }

    public Vendor create(VendorInput input, MultipartFile logo, MultipartFile cover) {
        String slug = input.getName().toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");

        Vendor vendor = new Vendor();
        vendor.setName(input.getName());
        vendor.setSlug(slug);
        vendor.setTagline(input.getTagline());
        vendor.setDescription(input.getDescription() != null ? input.getDescription() : "");
        vendor.setLocation(input.getLocation());

        if (logo != null && !logo.isEmpty()) {
            try {
                vendor.setLogo(gcpStorageService.uploadFile(logo));
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload logo to GCP Storage", e);
            }
        }

        if (cover != null && !cover.isEmpty()) {
            try {
                vendor.setCover(gcpStorageService.uploadFile(cover));
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload cover to GCP Storage", e);
            }
        }

        return vendorRepository.save(vendor);
    }

    public Vendor update(Long id, VendorInput input, MultipartFile logo, MultipartFile cover) {
        Vendor vendor = findById(id);
        String slug = input.getName().toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");

        vendor.setName(input.getName());
        vendor.setSlug(slug);
        vendor.setTagline(input.getTagline());
        if (input.getDescription() != null) vendor.setDescription(input.getDescription());
        vendor.setLocation(input.getLocation());

        if (logo != null && !logo.isEmpty()) {
            try {
                vendor.setLogo(gcpStorageService.uploadFile(logo));
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload logo to GCP Storage", e);
            }
        }

        if (cover != null && !cover.isEmpty()) {
            try {
                vendor.setCover(gcpStorageService.uploadFile(cover));
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload cover to GCP Storage", e);
            }
        }

        return vendorRepository.save(vendor);
    }

    public void delete(Long id) {
        vendorRepository.deleteById(id);
    }

    public Vendor setStatus(Long id, String status) {
        Vendor vendor = findById(id);
        vendor.setStatus(status);
        return vendorRepository.save(vendor);
    }

    /**
     * Resolves the storefront owned by the given user account, creating a
     * starter profile on first access so vendors can list products right away.
     */
    public Vendor findOrCreateForUser(Long userId, String email, String name) {
        if (userId == null) {
            throw new RuntimeException("Sign in as a vendor to manage a storefront");
        }
        return vendorRepository.findByUserId(userId).orElseGet(() -> {
            String displayName = (name != null && !name.isBlank()) ? name.trim()
                    : (email != null && email.contains("@") ? email.substring(0, email.indexOf('@')) : "Vendor " + userId);

            Vendor vendor = new Vendor();
            vendor.setUserId(userId);
            vendor.setName(displayName);
            vendor.setSlug(generateUniqueSlug(displayName) + "-" + userId);
            vendor.setDescription("");
            return vendorRepository.save(vendor);
        });
    }

    private String generateUniqueSlug(String name) {
        String base = name.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
        if (base.isBlank()) base = "vendor";
        String slug = base;
        int suffix = 2;
        while (vendorRepository.findBySlug(slug).isPresent()) {
            slug = base + "-" + suffix++;
        }
        return slug;
    }

    public Vendor updateImages(Long id, MultipartFile logo, MultipartFile cover) {
        Vendor vendor = findById(id);

        if (logo != null && !logo.isEmpty()) {
            try {
                vendor.setLogo(gcpStorageService.uploadFile(logo));
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload logo to GCP Storage", e);
            }
        }

        if (cover != null && !cover.isEmpty()) {
            try {
                vendor.setCover(gcpStorageService.uploadFile(cover));
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload cover to GCP Storage", e);
            }
        }

        return vendorRepository.save(vendor);
    }
}
