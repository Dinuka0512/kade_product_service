package com.dinuka.dev.product_service.dto;

import jakarta.validation.constraints.Pattern;

public class VendorStatusInput {

    @Pattern(regexp = "active|suspended", message = "Status must be active or suspended")
    private String status;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
