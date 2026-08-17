package com.dinuka.dev.product_service.service;

import com.dinuka.dev.product_service.model.Vendor;
import com.dinuka.dev.product_service.repository.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorService {

    private final VendorRepository vendorRepository;

    public VendorService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
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

    public Vendor setStatus(Long id, String status) {
        Vendor vendor = findById(id);
        vendor.setStatus(status);
        return vendorRepository.save(vendor);
    }
}
