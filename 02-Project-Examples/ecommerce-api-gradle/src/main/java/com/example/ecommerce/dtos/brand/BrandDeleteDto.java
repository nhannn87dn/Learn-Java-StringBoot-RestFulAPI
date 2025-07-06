package com.example.ecommerce.dtos.brand;

import jakarta.validation.constraints.NotNull;

public class BrandDeleteDto {
    @NotNull(message = "ID không được để trống")
    private Long id;

    public BrandDeleteDto() {}

    public BrandDeleteDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
