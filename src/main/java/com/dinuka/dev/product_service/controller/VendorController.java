package com.dinuka.dev.product_service.controller;

import com.dinuka.dev.product_service.dto.VendorInput;
import com.dinuka.dev.product_service.dto.VendorStatusInput;
import com.dinuka.dev.product_service.model.Vendor;
import com.dinuka.dev.product_service.service.VendorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/vendors")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping
    public ResponseEntity<List<Vendor>> getAll() {
        return ResponseEntity.ok(vendorService.findAll());
    }

    @GetMapping("/me")
    public ResponseEntity<Vendor> me(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestHeader(value = "X-User-Email", required = false) String email,
            @RequestHeader(value = "X-User-Name", required = false) String name,
            @RequestHeader(value = "X-User-Role", required = false) String role) {
        if (userId == null || !"vendor".equals(role)) {
            throw new RuntimeException("Vendor account required");
        }
        return ResponseEntity.ok(vendorService.findOrCreateForUser(userId, email, name));
    }

    @GetMapping("/{idOrSlug}")
    public ResponseEntity<Vendor> getById(@PathVariable String idOrSlug) {
        return ResponseEntity.ok(vendorService.findByIdOrSlug(idOrSlug));
    }

    @PostMapping
    public ResponseEntity<Vendor> create(
            @RequestParam String name,
            @RequestParam(required = false) String tagline,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) MultipartFile logo,
            @RequestParam(required = false) MultipartFile cover) {
        VendorInput input = new VendorInput();
        input.setName(name);
        input.setTagline(tagline);
        input.setDescription(description);
        input.setLocation(location);
        return ResponseEntity.ok(vendorService.create(input, logo, cover));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vendor> update(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam(required = false) String tagline,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) MultipartFile logo,
            @RequestParam(required = false) MultipartFile cover) {
        VendorInput input = new VendorInput();
        input.setName(name);
        input.setTagline(tagline);
        input.setDescription(description);
        input.setLocation(location);
        return ResponseEntity.ok(vendorService.update(id, input, logo, cover));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vendorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Vendor> setStatus(@PathVariable Long id, @Valid @RequestBody VendorStatusInput input) {
        return ResponseEntity.ok(vendorService.setStatus(id, input.getStatus()));
    }

    @PatchMapping("/{id}/images")
    public ResponseEntity<Vendor> updateImages(
            @PathVariable Long id,
            @RequestParam(required = false) MultipartFile logo,
            @RequestParam(required = false) MultipartFile cover) {
        return ResponseEntity.ok(vendorService.updateImages(id, logo, cover));
    }
}
