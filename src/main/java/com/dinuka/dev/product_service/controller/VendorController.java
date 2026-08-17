package com.dinuka.dev.product_service.controller;

import com.dinuka.dev.product_service.dto.VendorStatusInput;
import com.dinuka.dev.product_service.model.Vendor;
import com.dinuka.dev.product_service.service.VendorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{idOrSlug}")
    public ResponseEntity<Vendor> getById(@PathVariable String idOrSlug) {
        return ResponseEntity.ok(vendorService.findByIdOrSlug(idOrSlug));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Vendor> setStatus(@PathVariable Long id, @Valid @RequestBody VendorStatusInput input) {
        return ResponseEntity.ok(vendorService.setStatus(id, input.getStatus()));
    }
}
