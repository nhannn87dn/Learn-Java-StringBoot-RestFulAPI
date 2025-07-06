
package com.example.ecommerce.controllers;

import com.example.ecommerce.responses.ApiResponse;

import com.example.ecommerce.dtos.brand.BrandCreateDto;
import com.example.ecommerce.dtos.brand.BrandUpdateDto;
import com.example.ecommerce.dtos.brand.BrandDeleteDto;
import com.example.ecommerce.dtos.brand.BrandResponseDto;
import com.example.ecommerce.services.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@RestController
@RequestMapping("/api/brands")
public class BrandController {
    @Autowired
    private BrandService brandService;


    @GetMapping
    public ResponseEntity<ApiResponse<List<BrandResponseDto>>> getAllBrands(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "2") int size) {
        // Spring Data JPA uses 0-based page index
        Page<BrandResponseDto> brandPage = brandService.getAllBrands(PageRequest.of(page - 1, size));
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("page", brandPage.getNumber() + 1); // trả về 1-based cho client
        metadata.put("size", brandPage.getSize());
        metadata.put("totalItems", brandPage.getTotalElements());
        metadata.put("totalPages", brandPage.getTotalPages());
        System.out.println("[DEBUG] Metadata: " + metadata);
        return ResponseEntity.ok(ApiResponse.success(brandPage.getContent(), metadata));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BrandResponseDto>> getBrandById(@PathVariable Long id) {
        BrandResponseDto brand = brandService.getBrandById(id);
        return ResponseEntity.ok(ApiResponse.success(brand));
    }


    @PostMapping
    public ResponseEntity<ApiResponse<BrandResponseDto>> createBrand(@Valid @RequestBody BrandCreateDto brandCreateDto) {
        BrandResponseDto createdBrand = brandService.createBrand(brandCreateDto);
        return ResponseEntity.ok(ApiResponse.success("Brand created successfully", createdBrand));
    }


    @PutMapping
    public ResponseEntity<ApiResponse<BrandResponseDto>> updateBrand(@Valid @RequestBody BrandUpdateDto brandUpdateDto) {
        BrandResponseDto updatedBrand = brandService.updateBrand(brandUpdateDto);
        return ResponseEntity.ok(ApiResponse.success("Brand updated successfully", updatedBrand));
    }


    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteBrand(@Valid @RequestBody BrandDeleteDto brandDeleteDto) {
        brandService.deleteBrand(brandDeleteDto);
        return ResponseEntity.ok(ApiResponse.success("Brand deleted successfully"));
    }
}
